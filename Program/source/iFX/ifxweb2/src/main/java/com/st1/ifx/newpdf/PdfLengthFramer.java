package com.st1.ifx.newpdf;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

import com.st1.ifx.hcomm.sock.Framer;

public class PdfLengthFramer implements Framer {
	private static final int MAXMESSAGELENGTH = 9999;
	private static final int BYTEMASK = 0xff;
	private static final int SHORTMASK = 0xffff;
	private static final int BYTESHIFT = 8;

	private DataInputStream in;

	public PdfLengthFramer(InputStream in) {
		this.in = new DataInputStream(in);
	}

	@Override
	public void frameMsg(byte[] message, OutputStream out) throws IOException {
		if (message.length > MAXMESSAGELENGTH) {
			throw new IOException("message too long");
		}
		// write length prefix
		String sLen = String.format("%04d", message.length);
		System.out.println("send len:" + sLen);
		byte[] bb = sLen.getBytes();
		out.write(bb);
		out.write(message);
		out.flush();
		System.out.println("sent!!");
	}

	@Override
	public byte[] nextMsg() throws Exception {
		byte[] bb = new byte[4];
		// in.readFully(bb);
		System.out.println("read snagate length");
		bb = readN(4);
		int length;
		length = Integer.parseInt(new String(bb));
		System.out.println("receive len:" + length);

		// System.out.println("read snagate status");
		// byte[] success = new byte[1];
		// success = readN(1);
		// System.out.println("sna gate success?"+ new String(success));

		System.out.println("read snagate data, len:" + length);
		byte[] msg = new byte[length];
		// in.readFully(msg);
		msg = readN(length); // 總長? 內容長? -4
		String result3 = new String(msg, "cp937");
		System.out.println("###sna-allback::" + new String(bb) + new String(msg));
		System.out.println("###sna-allback::" + new String(bb) + result3);
		return msg;

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
