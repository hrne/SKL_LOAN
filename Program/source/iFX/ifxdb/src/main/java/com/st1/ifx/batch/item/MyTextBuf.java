package com.st1.ifx.batch.item;

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

public class MyTextBuf {
	private static final Logger logger = LoggerFactory.getLogger(MyTextBuf.class);
	private String buf;
	private byte[] theByteArray;
	private int offset = 0;
	private String encoding = null;

	public MyTextBuf(String buf, String encoding) {
		this.buf = buf;
		if (this.buf != null) {
			if (encoding == null) {
				theByteArray = buf.getBytes();
			} else {
				try {
					this.encoding = encoding;
					theByteArray = buf.getBytes(encoding);
				} catch (UnsupportedEncodingException e) {
					StringWriter errors = new StringWriter();
					e.printStackTrace(new PrintWriter(errors));
					logger.warn(errors.toString());
				}
			}
		}
	}

	public int length() {
		return theByteArray.length;
	}

	public int getCurrentOffset() {
		return offset;
	}

	// public static TextBuf PICK(String buf)
	// {
	// return new TextBuf(buf);
	// }

	public String substring(int startPos, int endPos) {
		logger.info("my startPos:" + startPos + ", endPos:" + endPos + "," + length());
		byte[] bb = Arrays.copyOfRange(theByteArray, startPos, endPos);
		String s = bytesToString(bb);
		return s;
	}

	public String substring(int startPos) {
		int endPos = length();
		return substring(startPos, endPos);
	}

	@SuppressWarnings("unused")
	private byte[] stringToBytes(String s) {
		return s.getBytes();
	}

	private String bytesToString(byte[] bb) {
		try {
			return new String(bb, encoding);
		} catch (UnsupportedEncodingException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.warn(errors.toString());
			return null;
		}
	}

	protected String get(int skip, int len) {
		if (skip != 0) {
			skip(skip);
		}
		byte[] bb = Arrays.copyOfRange(theByteArray, offset, offset + len);

		offset += bb.length;
		String s = bytesToString(bb);
		logger.info("[" + s + "] picked.");
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
