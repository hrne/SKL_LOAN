package com.st1.ifx.hcomm.sock;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LengthFramer implements Framer {

	static final Logger logger = LoggerFactory.getLogger(LengthFramer.class);

	private static final int MAXMESSAGELENGTH = 60000;
	private static final int BYTEMASK = 0xff;
	private static final int SHORTMASK = 0xffff;
	private static final int BYTESHIFT = 8;

	private DataInputStream in;

	public LengthFramer(InputStream in) {
		this.in = new DataInputStream(in);
	}

	@Override
	public void frameMsg(byte[] message, OutputStream out) throws IOException {
		if (message.length > MAXMESSAGELENGTH) {
			throw new IOException("message too long");
		}
		// write length prefix
		String sLen = String.format("%05d", message.length);
		logger.info("send len:" + sLen);
		byte[] bb = sLen.getBytes();
		byte[] msg = new byte[message.length + 5];

		System.arraycopy(bb, 0, msg, 0, 5);
		System.arraycopy(message, 0, msg, 5, message.length);

		out.write(msg);
		logger.info("frameMsg bb:" + new String(bb));
		logger.info("frameMsg message:" + new String(msg));
		out.flush();
		logger.info("sent!!");
	}

	@Override
	public byte[] nextMsg() throws Exception {
		byte[] bb = new byte[5];
		// in.readFully(bb);
		logger.info("read snagate length not mq");
		bb = readN(5);
		int length;
		logger.info("receive bb:" + HexDump.dumpHexString(bb));
		length = Integer.parseInt(new String(bb));
		logger.info("receive int len:" + length);
		byte[] msg = new byte[length];
		msg = readN(length - 5); // minus data length(4) and status(1)
		return msg;
	}

	public byte[] readN(int size) throws Exception {
		byte[] data = new byte[size];

		int totalBytesRcvd = 0;
		int bytesRcvd;
		logger.info("readN::" + data.length + ",total:" + totalBytesRcvd);
		/*
		 * read(byte[] b, int off, int len) throws ??： b - ?入?据的???。 off - ?? b
		 * 中??入?据的初始偏移量。 len - 要?取的最大字??。 返回： ?入???的?字??；如果因?已到?流末尾而不再有?据可用，?返回 -1。
		 */
		try {
			while (totalBytesRcvd < data.length) {
				if ((bytesRcvd = in.read(data, totalBytesRcvd, data.length - totalBytesRcvd)) == -1) {
					logger.info("readN error");
					throw new SocketException("Connection closed prematurely");
				}
				totalBytesRcvd += bytesRcvd;
				logger.info("readNEnd::" + bytesRcvd + ",total:" + totalBytesRcvd);
			} // data array is full
			logger.info("readN end");
		} catch (Exception e) {
			logger.info("readN Exception:" + e);
		}
		return data;
	};
}
