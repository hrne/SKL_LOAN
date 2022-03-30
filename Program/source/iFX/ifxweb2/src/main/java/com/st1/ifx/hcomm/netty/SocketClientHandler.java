package com.st1.ifx.hcomm.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class SocketClientHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		Channel channel = ctx.channel();

		ByteBuf responseBuf = (ByteBuf) msg;
		responseBuf.markReaderIndex();

		int length = responseBuf.readInt();
		int seq = responseBuf.readInt();

		responseBuf.resetReaderIndex();

		CallbackService callbackService = ChannelUtils.<CallbackService>removeCallback(channel, seq);
		callbackService.receiveMessage(responseBuf);
	}
}
