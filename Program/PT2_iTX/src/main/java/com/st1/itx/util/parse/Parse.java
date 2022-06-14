package com.st1.itx.util.parse;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.util.format.FormatUtil;

/**
 * Parse<br>
 * Parse String to Integer or Double or Float or BigDecimal
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Component("parse")
@Scope("singleton")
public class Parse {
	private final Logger logger = LoggerFactory.getLogger(Parse.class);

	private Pattern pattern = Pattern.compile("-?[0-9]+(\\.[0-9]+)?");

	/**
	 * Integer to String
	 * 
	 * @param value Integer
	 * @param width Length
	 * @return String Numeric
	 */
	public String IntegerToString(int value, int width) {
		String format = String.format("%%0%dd", width);
		String result = String.format(format, value);
		return result;
	}

	/**
	 * String to Integer
	 * 
	 * @param value String number
	 * @return Integer of value
	 * @throws LogicException When Value Is Not Numeric
	 */
	public int stringToInteger(String value) throws LogicException {
		value = value == null || value.trim().isEmpty() ? "0" : value.trim();
		int res = 0;
		try {
			res = Integer.parseInt(value.replaceAll(",", "").replaceAll("/", ""));
		} catch (NumberFormatException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
			throw new LogicException("CE000", "Text Is Not numeric...");
		}
		return res;
	}

	/**
	 * String to Double
	 * 
	 * @param value String number
	 * @return Double of value
	 * @throws LogicException When Value Is Not Numeric
	 */
	public double stringToDouble(String value) throws LogicException {
		value = value == null || value.trim().isEmpty() ? "0.0" : value.trim();
		Double res = 0.0;
		try {
			res = Double.parseDouble(value.replaceAll(",", ""));
		} catch (Throwable e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
			throw new LogicException("CE000", "Text Is Not numeric...");
		}
		return res;
	}

	/**
	 * String to Float
	 * 
	 * @param value String
	 * @return Float of value
	 * @throws LogicException When Value Is Not Numeric
	 */
	public float stringToFloat(String value) throws LogicException {
		value = value == null || value.trim().isEmpty() ? "0.0" : value.trim();
		Float res = 0f;
		try {
			res = Float.parseFloat(value.replaceAll(",", ""));
		} catch (Throwable e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
			throw new LogicException("CE000", "Text Is Not numeric...");
		}
		return res;
	}

	/**
	 * String to BigDecimal
	 * 
	 * @param value String
	 * @return BigDecimal of value
	 * @throws LogicException When Value Is Not Numeric
	 */
	public BigDecimal stringToBigDecimal(String value) throws LogicException {
		value = value == null || value.trim().isEmpty() ? "0" : value.trim();
		BigDecimal res = null;
		try {
			res = new BigDecimal(value.replaceAll(",", ""));
		} catch (Throwable e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
			throw new LogicException("CE000", "Text Is Not numeric...");
		}
		return res;
	}

	/**
	 * Integer to Timestamp
	 * 
	 * @param date Integer for B.C
	 * @param time Integer for time HHmmss
	 * @return Timestamp
	 */
	public Timestamp IntegerToSqlDateO(int date, int time) {
		String times = FormatUtil.pad9(time + "", 6);
		String dateS = date + times;
		SimpleDateFormat sp = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			Date dateU = sp.parse(dateS);
			Timestamp dateT = new Timestamp(dateU.getTime());
			return dateT;
		} catch (ParseException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.warn("IntegerToSqlDateO Erroe!!!");
			logger.warn(errors.toString());
			return null;
		}
	}

	/**
	 * String to Timestamp
	 * 
	 * @param date String for B.C
	 * @param time String for Time HHmmss
	 * @return TimeStamp
	 */
	public Timestamp StringToSqlDateO(String date, String time) {
		String dateS = date + time;
		SimpleDateFormat sp = null;
		if (date.indexOf("-") != -1)
			sp = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
		else
			sp = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			Date dateU = sp.parse(dateS.trim());
			Timestamp dateT = new Timestamp(dateU.getTime());
			return dateT;
		} catch (ParseException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.warn("StringToSqlDateO Erroe!!!");
			logger.warn(errors.toString());
			return null;
		}
	}

	/**
	 * TimeStamp To String
	 * 
	 * @param value TimeStamp
	 * @return String yyy/MM/dd HH:mm:ss (民國年)
	 */
	public String timeStampToString(Timestamp value) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");
		try {
			int dateI = Integer.parseInt(sdf.format(value)) - 19110000;
			int year = dateI / 10000;
			int mon = (dateI - (year * 10000)) / 100;
			int day = dateI % 100;
			return year + "/" + (mon < 10 ? "0" + mon : mon) + "/" + (day < 10 ? "0" + day : day) + " " + stf.format(value);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.warn("timesTampToString Erroe!!!");
			logger.warn(errors.toString());
			return "";
		}
	}

	/**
	 * TimeStamp To String
	 * 
	 * @param value TimeStamp
	 * @return String yyy/MM/dd (民國年)
	 */
	public String timeStampToStringDate(Timestamp value) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		try {
			int dateI = Integer.parseInt(sdf.format(value)) - 19110000;
			int year = dateI / 10000;
			int mon = (dateI - (year * 10000)) / 100;
			int day = dateI % 100;
			return year + "/" + (mon < 10 ? "0" + mon : mon) + "/" + (day < 10 ? "0" + day : day);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.warn("timesTampToString Erroe!!!");
			logger.warn(errors.toString());
			return "";
		}
	}

	/**
	 * TimeStamp To String
	 * 
	 * @param value TimeStamp
	 * @return String HH:mm:ss
	 */
	public String timeStampToStringTime(Timestamp value) {
		SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");
		try {
			return stf.format(value) + "";
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.warn("timesTampToString Erroe!!!");
			logger.warn(errors.toString());
			return "";
		}
	}

	public String stringToStringDate(String value) {
		if (value == null || value.trim().isEmpty())
			return "";

		String p[] = value.split(" ");

		Timestamp t = null;
		if (p.length > 1)
			t = this.StringToSqlDateO(p[0], p[1]);
		else
			t = this.StringToSqlDateO(value, "");

		return this.timeStampToStringDate(t);
	}

	public String stringToStringTime(String value) {
		if (value == null || value.trim().isEmpty())
			return "";

		String p[] = value.split(" ");
		Timestamp t = null;
		if (p.length > 1)
			t = this.StringToSqlDateO(p[0], p[1]);
		else
			t = this.StringToSqlDateO(value, "");

		return this.timeStampToStringTime(t);
	}

	public String stringToStringDateTime(String value) {
		if (value == null || value.trim().isEmpty())
			return "";

		String p[] = value.split(" ");
		Timestamp t = null;
		if (p.length > 1)
			t = this.StringToSqlDateO(p[0], p[1]);
		else
			t = this.StringToSqlDateO(value, "");

		return this.timeStampToString(t);
	}

	/**
	 * is numeric
	 * 
	 * @param str String
	 * @return Numeric true else false
	 */
	public boolean isNumeric(String str) {
		if (str == null) {
			return false;
		}
		/*
		 * int sz = str.length(); for (int i = 0; i < sz; i++) { if
		 * (Character.isDigit(str.charAt(i)) == false) { return false; } } return true;
		 */
		Matcher m = pattern.matcher(str);
		return m.matches();
	}
}
