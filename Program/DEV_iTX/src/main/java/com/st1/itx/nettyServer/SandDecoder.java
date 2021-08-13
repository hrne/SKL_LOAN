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
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

public class SandDecoder extends MessageToMessageDecoder<ByteBuf> {
	private static final Logger logger = LoggerFactory.getLogger(SandDecoder.class);

	private final Charset charset;
	private int length = 0;
	private int recLen = 0;
	private String buffer = "";
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
		this.charset = charset;
		baos = new ByteArrayOutputStream();
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
		// TODO Auto-generated method stub
		if (!msg.isReadable()) {
			logger.info("UnReadable...");
			return;
		}

		logger.info("len = " + msg.readableBytes());
		logger.info("from : " + ctx.channel().remoteAddress().toString());

		if (!lenFlag) {
			String bufLen = msg.toString(0, 5, charset);
			length = Integer.valueOf(bufLen) + 5;
			lenFlag = true;
		}

		recLen += msg.readableBytes();
		buffer += msg.toString(charset);

		byte[] req = new byte[msg.readableBytes()];
		msg.readBytes(req);
		baos.write(req);

		if (recLen < length) {
			return;
		}

		out.add(buffer);
//		logger.info("msg : " + buffer);
		this.tita = buffer.substring(5);
		this.tita = new String(baos.toByteArray(), "UTF-8").substring(5);

		// init redo
		buffer = "";
		recLen = 0;
		lenFlag = false;
		length = 0;
		baos = null;
		this.ReadComplete(ctx);
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

//		logger.info("server send message :" + this.tota);
		ctx.channel().writeAndFlush(this.tota);
		ctx.close();
		ThreadVariable.clearThreadLocal();
	}
}
