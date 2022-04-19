package com.st1.test;

import java.net.InetSocketAddress;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.Log4JLoggerFactory;

/**
 * TODO
 * 
 * @author XWF
 */
@Component
public class NettyClient {

	private static InternalLogger logger = Log4JLoggerFactory.getInstance(NettyClient.class);

	EventLoopGroup worker = null;
	ChannelFuture futrue = null;
	private static boolean needReset = false;// 是否需要重連

	NettyClientHandler clientHandler = new NettyClientHandler();

	@Value("${serveripforclient:'127.0.0.1'}")
	private String serveripforclient;

	@Value("${serverportforclient:50001}")
	private int serverportforclient;

	@Value("${clientport:50001}")
	private int clientport;

	public void startNettyClient() {
		System.out.println("啟動Netty客戶端");
		new Thread(new Runnable() {
			@Override
			public void run() {
				startClient();
			}
		}).start();
	}

	private void startClient() {
		// worker負責讀寫資料
		worker = new NioEventLoopGroup();
		try {
			// 輔助啟動類
			Bootstrap bootstrap = new Bootstrap();
			// 設定執行緒池
			bootstrap.group(worker);
			// 設定socket工廠
			bootstrap.channel(NioSocketChannel.class);
			bootstrap.localAddress(clientport);
			bootstrap.option(ChannelOption.TCP_NODELAY, true); // 不延遲，訊息立即傳送
			// 如果設定緩衝過小，訊息過長可能會：java.lang.IllegalArgumentException: minimumReadableBytes:
			// -1769478984 (expected: >= 0)
			bootstrap.option(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(512, 1024, 2048));// 緩衝大小，initial要介於minimum和maximum之間

			clientHandler.setClient(this);

			// 設定管道
			bootstrap.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel socketChannel) throws Exception {
					// 獲取管道
					ChannelPipeline pipeline = socketChannel.pipeline();
//                    pipeline.addLast(new MsgDecoder(1024, 0, 4, 0, 4));
//                    pipeline.addLast(new MsgEncoder());
					pipeline.addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4));
					pipeline.addLast(new ByteArrayDecoder());
					pipeline.addLast(new LengthFieldPrepender(4));
					pipeline.addLast(new ByteArrayEncoder());
					// 處理類
					pipeline.addLast(clientHandler);
				}
			});
			// 發起非同步連線操作
			futrue = bootstrap.connect(new InetSocketAddress(serveripforclient, serverportforclient)).sync();
			startReConnectThread();
			// 等待客戶端鏈路關閉
			futrue.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			needReset = true;
			logger.error("發生異常1：" + e.getMessage());
		} catch (Exception e) {
			needReset = true;
			logger.error("發生異常2：" + e.getMessage());
		} finally {
			// 優雅的退出，釋放NIO執行緒組
			worker.shutdownGracefully();
		}
	}

	public void startReConnectThread() {
		while (true) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (needReset) {
				logger.warn("與伺服器連線丟失，重新連線伺服器。");
				restartClient();
			} else {
				logger.info("與伺服器已經連線...");
			}
		}

	}

	public void closeClient() {
		try {
			// 等待客戶端鏈路關閉
			futrue.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			// 優雅的退出，釋放NIO執行緒組
			worker.shutdownGracefully();
		}

	}

	public void restartClient() {
		logger.info("關閉客戶端釋放資源，5s後重新連線伺服器");
		closeClient();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		startClient();
	}

	public void connectEstablished(boolean bool) {
		needReset = !bool;
	}

	// 發訊息
	public void sendMessage(byte[] bytes) {
		System.out.println(">>>>>>給服務端發訊息的長度：" + bytes.length);
		clientHandler.sendMessage(bytes);
	}

	// 接收訊息
	protected void messageReceived(byte[] bytes) {
		System.out.println("<<<<<<接收服務端訊息長度：" + bytes.length);
		System.out.println("客戶端收到了訊息：" + new String(bytes));
	}

}