package com.st1.test;

/**
 * createtime : 2018年8月23日 下午3:29:04
 */

import java.net.InetSocketAddress;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.Log4JLoggerFactory;

/**
 * TODO
 * 
 * @author XWF
 */
@Sharable
public class NettyClientHandler extends SimpleChannelInboundHandler<byte[]> {

	private static InternalLogger logger = Log4JLoggerFactory.getInstance(NettyClientHandler.class);

	private NettyClient nettyClient;

	ChannelHandlerContext ctx = null;

	public void setClient(NettyClient nettyClient) {
		this.nettyClient = nettyClient;
	}

	// 與伺服器建立連線
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		this.ctx = ctx;
		String ipStr = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress();
		int port = ((InetSocketAddress) ctx.channel().remoteAddress()).getPort();
		logger.info("連線建立，連線到伺服器：" + ipStr + ":" + port);
		nettyClient.connectEstablished(true);
	}

	// 與伺服器斷開連線
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		ctx = null;
		logger.info("與伺服器連線已斷開");
		nettyClient.connectEstablished(false);
	}

	// 發生異常
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// 關閉管道
		ctx.channel().close();
		logger.error("異常");
		nettyClient.connectEstablished(false);
		// 列印異常資訊
		cause.printStackTrace();
	}

	// 發訊息
	public void sendMessage(byte[] bytes) {
		if (ctx != null) {
			// 給伺服器發訊息
			ctx.channel().writeAndFlush(bytes);
		} else {
			logger.warn("連線已斷開，無法傳送訊息。");
		}
	}

	// 如果繼承SimpleChannelInboundHandler<byte[]>，使用
	// 接收訊息
//	@Override
//	protected void messageReceived(ChannelHandlerContext ctx, byte[] bytes) throws Exception {
//		if (null != nettyClient) {
//			nettyClient.messageReceived(bytes);
//		}
//	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {
		// TODO Auto-generated method stub

	}

	// 如果繼承ChannelHandlerAdapter，使用
	// 接收訊息
//	@Override
//	public void channelRead(ChannelHandlerContext ctx, Object msg) {
//		if(msg instanceof byte[]) {
//			byte[] data = (byte[]) msg;
//			if(data != null && ctx != null && data.length > 0){
//				if(nettyClient != null){
//					nettyClient.messageReceived(data);
//				}
//			}
//		}
//	}

}