package com.st1.itx.nettyServer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetSocketAddress;

import javax.annotation.PreDestroy;

@Service
public class NettyServer {
	private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

	private final EventLoopGroup bossGroup = new NioEventLoopGroup();
	private final EventLoopGroup workerGroup = new NioEventLoopGroup();

	private Channel channel;

	/**
	 * Start Service
	 * 
	 * @param address InetSocketAddress
	 * @return ChannelFuture ChannelFuture
	 */
	public ChannelFuture start(InetSocketAddress address) {
		ChannelFuture f = null;

		try {
			ServerBootstrap b = new ServerBootstrap();
//			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new ServerChannelInitializer()).option(ChannelOption.SO_BACKLOG, 1000)
//					.option(ChannelOption.SO_RCVBUF, 4 * 1024).childOption(ChannelOption.SO_SNDBUF, 4 * 1024).childOption(ChannelOption.SO_KEEPALIVE, true);
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 100).option(ChannelOption.TCP_NODELAY, true)
					.option(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(1024, 2048, 65535)).handler(new LoggingHandler(LogLevel.INFO))
					.childOption(ChannelOption.SO_KEEPALIVE, true).childHandler(new ServerChannelInitializer());

			f = b.bind(address).syncUninterruptibly();
			channel = f.channel();
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error("Netty start error: " + errors.toString());
		} finally {
			if (f != null && f.isSuccess()) {
				logger.info("Netty server listening " + address.getHostName() + " on port " + address.getPort() + " and ready for connections...");
			} else {
				logger.error("Netty server start up Error!");
			}
		}
		return f;
	}

	@PreDestroy
	public void destroy() {
		logger.info("Shutdown Netty Server...");
		if (channel != null) {
			channel.close();
		}
		workerGroup.shutdownGracefully();
		bossGroup.shutdownGracefully();
		logger.info("Shutdown Netty Server Success!");
	}
}
