package com.st1.ifx.hcomm.netty;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st1.util.dump.HexDump;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

public class MsgEncoder extends MessageToMessageEncoder<byte[]> {
	private static final Logger logger = LoggerFactory.getLogger(MsgEncoder.class);

	@Override
	protected void encode(ChannelHandlerContext ctx, byte[] msg, List<Object> out) throws Exception {
		logger.info(HexDump.dumpHexString(msg));
		out.add(msg);
	}
}
