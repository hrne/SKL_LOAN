package com.st1.itx.nettyServer;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st1.itx.eum.ThreadVariable;
import com.st1.itx.main.ApControl;
import com.st1.itx.util.MySpring;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class ServerHandler extends SimpleChannelInboundHandler<byte[]> {
	private static final Logger logger = LoggerFactory.getLogger(ServerHandler.class);

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		logger.info("channelActive>>>>>>>>");
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		logger.info("channelRead...." + msg.getClass().getSimpleName());
		/* AP ConTroller */
		UnpooledByteBufAllocator allocator = new UnpooledByteBufAllocator(false, false, false);

		ApControl apControl = (ApControl) MySpring.getBean("apControl");
		String tota = "";

		int rSeq = 0;
		try {
			ByteBuf byteBuf = allocator.buffer(((byte[]) msg).length);
			byteBuf.writeBytes((byte[]) msg);
			rSeq = byteBuf.readInt();
			byte[] req = new byte[byteBuf.readableBytes()];
			byteBuf.readBytes(req);
			logger.info("rSeq :" + rSeq);
			tota = apControl.callTrade(new String(req, CharsetUtil.UTF_8));
			byteBuf.release();
		} catch (Throwable e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}

		ByteBuf bytesAll = allocator.buffer(tota.getBytes(CharsetUtil.UTF_8).length + 8);
		bytesAll.writeInt(rSeq);
		bytesAll.writeInt(tota.getBytes(CharsetUtil.UTF_8).length);
		bytesAll.writeBytes(tota.getBytes(CharsetUtil.UTF_8));

		ctx.channel().writeAndFlush(bytesAll);
		ctx.channel().close();
		ctx.close();
		apControl.clearV();
		tota = null;
		ThreadVariable.clearThreadLocal();
	}

	// ReadCompleteHandler
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		logger.info("channelReadComplete");
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
		logger.info("channelRead0....");

	}
}
