package com.st1.itx.nettyServer;

import java.io.UnsupportedEncodingException;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.CharsetUtil;

public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel socketChannel) {

		try {
			Unpooled.copiedBuffer("$_".getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(65535, delimiter));
//		socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(2048, delimiter));
//		socketChannel.pipeline().addLast(new LengthFieldBasedFrameDecoder(64 * 1024, 0, 4, 0, 4));
//		socketChannel.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));
//		socketChannel.pipeline().addLast(new ByteArrayDecoder());
//		socketChannel.pipeline().addLast(new LengthFieldPrepender(4));
		socketChannel.pipeline().addLast(new SandDecoder(CharsetUtil.UTF_8));
		socketChannel.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
//		socketChannel.pipeline().addLast(new ByteArrayEncoder());		
		socketChannel.pipeline().addLast(new ReadTimeoutHandler(90));
		socketChannel.pipeline().addLast(new ServerHandler());
	}
}
