package com.st1.util.cbl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract public class Fmt {
	static final Logger logger = LoggerFactory.getLogger(Fmt.class);

	final static SimpleDateFormat dateFromatter = new SimpleDateFormat("yyyyMMdd");
	final static SimpleDateFormat timeFromatter = new SimpleDateFormat("HHmmssS");
	protected TextBuf textBuf;

	public Fmt() {
	}

	public Fmt(TextBuf textBuf) {
		this.textBuf = textBuf;
	}

	public void setTextBuf(TextBuf textBuf) {
		this.textBuf = textBuf;
	}

	public int getCurrentOffset() {
		return this.textBuf.getCurrentOffset();
	}

	abstract public void parse();

	public Date pickDate() {
		return textBuf.pickDate();
	}

	public Date pickDate(int len) throws ParseException {
		String value = pick(8);
		return dateFromatter.parse(value);

	}

	public String pickTime(int len) {

		return textBuf.pick(len);
	}

	public String pick(int len) {
		return textBuf.pick(len);
	}

	public String pick9AsString(int len) {
		return new Integer(textBuf.pickInt(len)).toString();
	}

	public int pick9(int len) {
		return textBuf.pickInt(len);
	}

	public BigDecimal pickMoney(int integerLen, int fractionLen) {
		return textBuf.pickMoney(integerLen, fractionLen);
	}

	public abstract StringBuilder generate();

	protected StringBuilder builder = new StringBuilder();

	public void resetBuilder() {
		builder = new StringBuilder();
	}

	public StringBuilder getBuilder() {
		return builder;
	}

	public Fmt putDate(Date d) {
		String t = "";

		if (d != null) {
			t = dateFromatter.format(d);
		}
		t = padRight(t, 8); // space(8)
		builder.append(t);
		return this;
	}

	public Fmt putDate(Object o, int len) {

		if (o instanceof Date) {
			putDate((Date) o);
		} else { // if(o instanceof String) {
			builder.append(o.toString());
		}
		return this;

	}

	public Fmt putTime(Object o, int len) {
		if (o instanceof Date) {
			String value = timeFromatter.format(o);
			if (value.length() > len)
				value = value.substring(0, len);
			put(value, len);
		} else {

			put9(o.toString(), len);
		}
		return this;

	}

	public Fmt put(String s) {
		if (s == null)
			s = " ";
		builder.append(s);
		return this;
	}

	public Fmt put(String s, int len) {
		if (s == null)
			s = "";
		String t = padRight(s, len);
		builder.append(t);
		return this;
	}

	public Fmt put9(String s, int len) {
		if (s == null)
			s = "0";
		if (s.length() > len) {
			System.err.print("too long, trim " + s);
			s = s.substring(s.length() - len);
			System.err.println(" to " + s);
		}
		builder.append(zeroPad(s, len));
		return this;
	}

	public Fmt put9(Integer i, int len) {
		if (i == null) {
			put9("0", len);
		} else {
			put9(i.toString(), len);
		}
		return this;
	}

	public Fmt putMoney(String s, int integerLen, int fractionLen) {
		if (s == null || s.trim().length() == 0)
			s = "0";
		String[] ss = s.split("[.]");
		if (ss.length == 0) {
			builder.append(zeroPad("", integerLen + fractionLen));
		} else if (ss.length == 1) {
			if (s.charAt(0) == '.') { // ".123"
				builder.append(zeroPad("", integerLen));
				builder.append(padZero(ss[0], fractionLen));
			} else { // "123." , "123"
				builder.append(zeroPad(ss[0], integerLen));
				builder.append(padZero("", fractionLen));
			}
		} else {
			builder.append(zeroPad(ss[0], integerLen));
			builder.append(padZero(ss[1], fractionLen));
		}
		return this;
	}

	static String zeroPad(String s, int width) {
		if (width == 0)
			return "";
		StringBuffer result = new StringBuffer("");
		for (int i = 0; i < width - s.length(); i++) {
			result.append("0");
		}
		result.append(s);
		return result.toString();
	}

	static String padZero(String s, int width) {
		if (width == 0)
			return "";
		StringBuffer result = new StringBuffer("");
		result.append(s);
		for (int i = 0; i < width - s.length(); i++) {
			result.append("0");
		}
		return result.toString();
	}

	public static String padRight(String s, int n) {
		try {
			int len = s.getBytes("big5").length;
			if (len < n) {
				return s + String.format("%1$-" + (n - len) + "s", "");
			} else if (len > n) {
				return new String(s.getBytes("big5"), 0, n, "big5");
			} else {
				return s;
			}
			// return String.format("%1$-" + (n) + "s", s);
		} catch (UnsupportedEncodingException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}
		return padRight("<null>", n);
	}

	public static String padLeft(String s, int n) {
		try {
			int len = s.getBytes("big5").length;
			if (len < n) {
				return String.format("%1$-" + (n - len) + "s", "") + s;
			} else if (len > n) {
				return new String(s.getBytes("big5"), 0, n, "big5");
			} else {
				return s;
			}
		} catch (UnsupportedEncodingException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}
		return padLeft("<null>", n);
		// return String.format("%1$#" + n + "s", s);
	}

}
