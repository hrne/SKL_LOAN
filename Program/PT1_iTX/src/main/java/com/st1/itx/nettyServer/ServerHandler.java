package com.st1.itx.nettyServer;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st1.itx.main.ApControl;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.dump.HexDump;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerHandler extends SimpleChannelInboundHandler<byte[]> {
	private static final Logger logger = LoggerFactory.getLogger(ServerHandler.class);

	public String tita = "", tota = "";

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {

		ByteBuf buf = (ByteBuf) msg;
		byte[] data = new byte[buf.readableBytes()];
		buf.readBytes(data);

		logger.info(HexDump.dumpHexString(data));

		try {
			this.tita = new String(data, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}

		logger.info("server receive message Len:" + buf.readableBytes());
		logger.info("server receive message    :" + this.tita);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		logger.info("channelActive>>>>>>>>");
	}

	// ReadCompleteHandler
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		logger.info("ReadComplete...");
		logger.info("Tita = " + this.tita);

		/* AP ConTroller */
		ApControl apControl = (ApControl) MySpring.getBean("apControl");
		try {
			this.tota = apControl.callTrade(this.tita);
		} catch (Throwable e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}

		logger.info("server send message :" + this.tota);
		ctx.channel().writeAndFlush(this.tota);
		ctx.close();
	}

	// ReadExceptionHandler
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		StringWriter errors = new StringWriter();
		cause.printStackTrace(new PrintWriter(errors));
		logger.error(errors.toString());
		ctx.close();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {
		logger.info(HexDump.dumpHexString(msg));
	}
}
