package com.st1.itx.nettyServer;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st1.itx.eum.ThreadVariable;
import com.st1.itx.main.ApControl;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.dump.HexDump;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;

public class SandDecoder extends MessageToMessageDecoder<ByteBuf> {
	private static final Logger logger = LoggerFactory.getLogger(SandDecoder.class);

	private int length = 0;
	private int recLen = 0;
	private int rSeq = 0;
	private boolean lenFlag = false;
	private ByteArrayOutputStream baos = null;

	public String tita = "", tota = "";

	public SandDecoder() {
		this(Charset.defaultCharset());
		baos = new ByteArrayOutputStream();
	}

	public SandDecoder(Charset charset) {
		if (charset == null) {
			throw new NullPointerException("charset");
		}
		baos = new ByteArrayOutputStream();
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {

		if (!msg.isReadable()) {
			logger.info("UnReadable...");
			return;
		}

		logger.info("readableBytesLen = " + msg.readableBytes());
		logger.info("from : " + ctx.channel().remoteAddress().toString());

		if (!lenFlag) {
			byte[] dumb = new byte[msg.readableBytes()];
			msg.readBytes(dumb);
			logger.info(HexDump.dumpHexString(dumb));
			msg.resetReaderIndex();
			this.rSeq = msg.readInt();
			this.length = msg.readInt();
			lenFlag = true;
			logger.info("rSeq : [" + this.rSeq + "]");
			logger.info("rLen : [" + this.length + "]");
		}

		recLen += msg.readableBytes();
		byte[] req = new byte[msg.readableBytes()];
		msg.readBytes(req);
		baos.write(req);

		logger.info("recLen : [" + recLen + "]");
		logger.info("length : [" + length + "]");
		if (recLen < length) {
			return;
		}

		this.tita = new String(baos.toByteArray(), CharsetUtil.UTF_8);
		out.add(baos.toByteArray());
		// init redo

		recLen = 0;
		lenFlag = false;
		length = 0;
		baos.close();
		baos = null;
		this.ReadComplete(ctx);
//		msg.release();
	}

	public void ReadComplete(ChannelHandlerContext ctx) throws java.lang.Exception {
//		logger.info("ReadComplete...");
//		logger.info("Tita = " + this.tita);

		/* AP ConTroller */
		ApControl apControl = (ApControl) MySpring.getBean("apControl");
		try {
			this.tota = apControl.callTrade(this.tita);
		} catch (Throwable e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}

		UnpooledByteBufAllocator allocator = new UnpooledByteBufAllocator(false, false, false);
		ByteBuf bytesAll = allocator.buffer(this.tota.getBytes(CharsetUtil.UTF_8).length + 8);
		bytesAll.writeInt(this.rSeq);
		bytesAll.writeInt(this.tota.getBytes(CharsetUtil.UTF_8).length);
		bytesAll.writeBytes(this.tota.getBytes(CharsetUtil.UTF_8));

		ctx.channel().writeAndFlush(bytesAll);
		ctx.channel().close();
		ctx.close();
		apControl.clearV();
		this.tota = null;
		ThreadVariable.clearThreadLocal();
	}
}
