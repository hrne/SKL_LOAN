package com.st1.ifx.hcomm.netty;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.servlet.GlobalValues;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.Attribute;

@Service
@Scope("singleton")
public class NettyChannelPool {
	private Logger logger = LoggerFactory.getLogger(NettyChannelPool.class);

	@Value("${sna_server}")
	private String server = "192.168.10.8";

	@Value("${sna_port}")
	private String sServPort = "50001";

	private Channel[] channels;

	private Object[] locks;

	private final int MAX_CHANNEL_COUNT = 16;

	EventLoopGroup eventLoopGroup;

	Bootstrap bootstrap;

	public NettyChannelPool() {
	}

	@PostConstruct
	public void init() {
		this.logger.info("init NettyChannelPool Channl...");
		this.channels = new Channel[MAX_CHANNEL_COUNT];
		this.locks = new Object[MAX_CHANNEL_COUNT];

		for (int i = 0; i < MAX_CHANNEL_COUNT; i++)
			this.locks[i] = new Object();

		this.eventLoopGroup = new NioEventLoopGroup(16);
		this.bootstrap = new Bootstrap();
		this.bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).option(ChannelOption.SO_KEEPALIVE, Boolean.TRUE).option(ChannelOption.TCP_NODELAY, Boolean.TRUE)
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 40000).handler(new LoggingHandler(LogLevel.INFO)).handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ChannelPipeline pipeline = ch.pipeline();
						pipeline.addLast(new ReadTimeoutHandler(90));
						pipeline.addLast(new MsgEncoder());
						pipeline.addLast(new MsgDecoder());
//						pipeline.addLast(new SocketClientHandler());
					}
				});
	}

	private Channel connectToServer(String txcd) throws InterruptedException {
		ChannelFuture channelFuture;
		if (!Objects.isNull(txcd) && GlobalValues.sendIp != null && GlobalValues.sendIp.get(txcd) != null)
			channelFuture = bootstrap.connect(GlobalValues.sendIp.get(txcd.trim()).trim(), Integer.parseInt(sServPort));
		else
			channelFuture = bootstrap.connect(server, Integer.parseInt(sServPort));

		Channel channel = channelFuture.sync().channel();

		Attribute<Map<Integer, Object>> attribute = channel.attr(ChannelUtils.DATA_MAP_ATTRIBUTEKEY);
		ConcurrentHashMap<Integer, Object> dataMap = new ConcurrentHashMap<>();
		attribute.set(dataMap);
		return channel;
	}

	public Channel syncGetChannel(String txcd) throws InterruptedException {
//		int index = new Random().nextInt(MAX_CHANNEL_COUNT);
//		Channel channel = channels[index];
//
//		if (channel != null && channel.isActive())
//			return channel;
//
//		synchronized (locks[index]) {
//			channel = channels[index];
//			if (channel != null && channel.isActive())
//				return channel;
//			channel = this.connectToServer();
//			channels[index] = channel;
//		}

		return this.connectToServer(txcd);
	}

	@PreDestroy
	private void destroy() {
		this.logger.info("release NettyNIO...");
		try {
			this.eventLoopGroup.shutdownGracefully();
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}
//		for (int i = 0; i < MAX_CHANNEL_COUNT; i++)
//			if (channels[i] != null)
//				if (channels[i].isRegistered() || channels[i].isActive() || channels[i].isOpen())
//					channels[i].close();

//         for (int j = 0; j < MAX_CHANNEL_COUNT; j++) {
//				if (channels[i][j] != null)
//					if (channels[i][j].isRegistered() || channels[i][j].isActive() || channels[i][j].isOpen())
//						channels[i][j].close();
//			}

	}
}
