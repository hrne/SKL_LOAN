package com.st1.ifx.hcomm.sock;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.SocketException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LengthmqFramer implements Framer {

	static final Logger logger = LoggerFactory.getLogger(LengthmqFramer.class);

	private static final int MAXMESSAGELENGTH = 99999;
	private static final int BYTEMASK = 0xff;
	private static final int SHORTMASK = 0xffff;
	private static final int BYTESHIFT = 8;

	private DataInputStream in;

	public LengthmqFramer(InputStream in) {
		this.in = new DataInputStream(in);
	}

	@Override
	public void frameMsg(byte[] message, OutputStream out) throws IOException {
		if (message.length > MAXMESSAGELENGTH) {
			throw new IOException("message too long");
		}
		logger.info("LengthmqFramer frameMsg");
		// write length prefix
		// String sLen = String.format("%04d", message.length);
		// logger.info("send len:"+sLen);

		int totallength = message.length;
		byte[] bb = new byte[2];
		byte[] bbt = new byte[2];
		bb = IntToByteArray(totallength); // 固定兩byte
		bbt[0] = (byte) (totallength / 256);
		bbt[1] = (byte) (totallength % 256);
		logger.info("bb:" + totallength + ", HEX:" + HexDump.dumpHexString(bb));
		logger.info("bbt:" + totallength + ", HEX:" + HexDump.dumpHexString(bbt));
		logger.info("toByteArray:" + toByteArray(totallength, 2));
		out.write(bb);
		out.write(message);

		logger.info("frameMsg mq message:" + new String(message));
		out.flush();
		logger.info("sent!!");
	}

	byte[] IntToByteArray(int data) {

		byte[] result = new byte[2];

		result[0] = (byte) ((data & 0xFF00) >> 8);
		result[1] = (byte) ((data & 0x00FF) >> 0);

		return result;
	}

	public static byte[] toByteArray(int iSource, int iArrayLen) {
		byte[] bLocalArr = new byte[iArrayLen];
		for (int i = 0; (i < 4) && (i < iArrayLen); i++) {
			bLocalArr[i] = (byte) (iSource >> 8 * i & 0xFF);
		}
		return bLocalArr;
	}

	// 誤
	public static int toInt(byte[] bRefArr) {
		int iOutcome = 0;
		byte bLoop;

		for (int i = 0; i < bRefArr.length; i++) {
			bLoop = bRefArr[i];
			iOutcome += (bLoop & 0xFF) << (8 * i);
		}
		return iOutcome;
	}

	@Override
	public byte[] nextMsg() throws Exception {
		byte[] bb = new byte[2];
		byte[] b = new byte[1];
		// in.readFully(bb);
		logger.info("LengthmqFramer nextMsg");
		logger.info("read snagate length");
		bb = readN(2);
		int length, recstatus;
		logger.info("bb HEX:" + HexDump.dumpHexString(bb));
		length = new BigInteger(bb).intValue();
		logger.info("use BigInteger!");
		logger.info("mq length:" + length);
		b = readN(1);
		logger.info("b HEX:" + HexDump.dumpHexString(b));
		recstatus = new BigInteger(b).intValue();
		logger.info("mq recstatus:" + recstatus);

		byte[] msg = new byte[length];

		// in.readFully(msg);
		msg = readN(length - 2 - 1); // 長度(2)與成功失敗(1)
		return msg; // ascii?? utf8?
	}

	private byte[] readN(int size) throws Exception {
		byte[] data = new byte[size];

		int totalBytesRcvd = 0;
		int bytesRcvd;
		while (totalBytesRcvd < data.length) {
			if ((bytesRcvd = in.read(data, totalBytesRcvd, data.length - totalBytesRcvd)) == -1)
				throw new SocketException("Connection closed prematurely");
			totalBytesRcvd += bytesRcvd;
		} // data array is full

		return data;
	};
}
