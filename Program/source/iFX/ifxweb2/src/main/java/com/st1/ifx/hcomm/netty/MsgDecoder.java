package com.st1.ifx.hcomm.netty;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

public class MsgDecoder extends MessageToMessageDecoder<ByteBuf> {
	private static final Logger logger = LoggerFactory.getLogger(MsgDecoder.class);

	private int length = 0;
	private int recLen = 0;
	private int rSeq = 0;
	private boolean lenFlag = false;
	private ByteArrayOutputStream baos = null;

	public String tita = "", tota = "";

	public MsgDecoder() {
		baos = new ByteArrayOutputStream();
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
		if (!msg.isReadable()) {
			logger.info("UnReadable...");
			return;
		}

		logger.info("readableBytesLen = " + msg.readableBytes());
//		logger.info("from : " + ctx.channel().remoteAddress().toString());

		if (!lenFlag) {
			this.rSeq = msg.readInt();
			this.length = msg.readInt();
			lenFlag = true;
			logger.info("rSeq : [" + this.rSeq + "]");
			logger.info("rLen : [" + this.length + "]");
		}

		this.recLen += msg.readableBytes();
		byte[] req = new byte[msg.readableBytes()];
		msg.readBytes(req);
		baos.write(req);

		if (recLen < length)
			return;

		out.add(baos.toByteArray());
		this.ReadComplete(ctx);
//		logger.info("byteBuf release : " + msg.release());
	}

	public void ReadComplete(ChannelHandlerContext ctx) throws java.lang.Exception {
		UnpooledByteBufAllocator allocator = new UnpooledByteBufAllocator(false, false, false);
		ByteBuf bytesAll = allocator.buffer(baos.toByteArray().length + 8);

		bytesAll.writeInt(this.rSeq);
		bytesAll.writeInt(baos.toByteArray().length);
		bytesAll.writeBytes(baos.toByteArray());

		CallbackService callbackService = ChannelUtils.<CallbackService>removeCallback(ctx.channel(), this.rSeq);
		callbackService.receiveMessage(bytesAll);

		// init redo
		this.recLen = 0;
		this.lenFlag = false;
		this.length = 0;
		baos.close();
		baos = null;
	}
}
