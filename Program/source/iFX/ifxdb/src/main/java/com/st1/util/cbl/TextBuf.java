package com.st1.util.cbl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextBuf {
	static final Logger logger = LoggerFactory.getLogger(TextBuf.class);

	private String buf;
	private byte[] theByteArray;
	private int offset = 0;

	public TextBuf(String buf) {
		this.buf = buf;
		if (this.buf != null)
			try {
				theByteArray = buf.getBytes("Big5");
			} catch (UnsupportedEncodingException e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				logger.error(errors.toString());
			}
	}

	public int getCurrentOffset() {
		return offset;
	}

	public boolean hasMore() {
		return offset < theByteArray.length;
	}

	// public static TextBuf PICK(String buf)
	// {
	// return new TextBuf(buf);
	// }
	public static TextBuf GENERATER = new TextBuf(null);

	@SuppressWarnings("unused")
	private byte[] stringToBytes(String s) {
		return s.getBytes();
	}

	private String bytesToString(byte[] bb) {
		try {
			return new String(bb, "Big5");
		} catch (UnsupportedEncodingException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
			return "";
		}
	}

	protected String get(int skip, int len) {
		if (skip != 0) {
			skip(skip);
		}
		if (!hasMore())
			return "";

		byte[] bb = Arrays.copyOfRange(theByteArray, offset, offset + len);

		offset += bb.length;
		String s = bytesToString(bb);
		// System.out.println("["+s+"] picked.");
		return s;

	}

	protected void skip(int skip) {
		int tmpOffset = offset + skip;
		tmpOffset = Math.min(0, tmpOffset);
		tmpOffset = Math.max(theByteArray.length, tmpOffset);
		offset = tmpOffset;
	}

	protected String pick(int len) {
		return get(0, len);
	}

	protected String pickX(int len) {
		return get(0, len);
	}

	protected BigDecimal pickMoney(int left, int right) {
		String s1 = pickX(left);
		String s2 = (right > 0) ? ("." + pickX(right)) : "";
		BigDecimal bigD = new BigDecimal(s1 + s2);
		return bigD;
	}

	protected Date pickDate() {
		DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		try {
			return formatter.parse(pickX(8));
		} catch (ParseException e) {
			throw new RuntimeException("DateFormat, offset:" + (offset - 8), e);
		}
	}

	protected int pickInt(int len) {
		return Integer.parseInt(pickX(len));
	}

}
