package com.st1.dao;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JustDateUtil {
	static final Logger logger = LoggerFactory.getLogger(JustDateUtil.class);

	static Calendar cal = Calendar.getInstance();

	public static String now() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}

	public java.sql.Time getSqlTime() {
		java.util.Date today = new java.util.Date();
		long t = today.getTime();
		return new java.sql.Time(t);
	}

	public java.sql.Date getSqlDate() {
		java.util.Date today = new java.util.Date();
		long t = today.getTime();
		return new java.sql.Date(t);
	}

	public static String formatDate(Date dt, String pattern) {
		if (pattern == null)
			pattern = "yyyy/MM/dd HH:mm:ss";
		DateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(dt);
	}

	public static String formatDate8(Date dt) {
		return formatDate(dt, "yyyyMMdd");
	}

	public static String formatTime6(Date dt) {
		return formatDate(dt, "HH:mm:ss");
	}

	public static String formatTimeString(String s) {
		return s.substring(0, 2) + ":" + s.substring(2, 4) + ":" + s.subSequence(4, 6);
	}

	public static java.sql.Time parseTime6(String s) {
		s = formatTimeString(s);
		return java.sql.Time.valueOf(s);
	}

	public static String formatDateString(String s) {
		return s.substring(0, 4) + "/" + s.substring(4, 6) + "/" + s.substring(6);
	}

	public static Date dateBegin(Date dt) {
		cal.setTime(dt);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		;
		cal.set(Calendar.MINUTE, 0);
		;
		cal.set(Calendar.SECOND, 0);
		;
		cal.set(Calendar.MILLISECOND, 0);
		;
		return cal.getTime();
	}

	public static Date add(Date dt, int days) {

		cal.setTime(dt);
		cal.add(Calendar.DATE, 1);
		Date end = cal.getTime();
		return end;
	}

	/**
	 * @param ��^java .sql.Date�榡��
	 */
	public static java.sql.Date strToDate(String strDate) {
		String str = strDate;
		if (str.indexOf('/') == -1) {
			str = formatDateString(strDate);
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		java.util.Date d = null;
		try {
			d = format.parse(str);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}
		java.sql.Date date = null;
		if (d != null)
			date = new java.sql.Date(d.getTime());

		return date;
	}

	/**
	 * @param ��^java .sql.Time�榡��
	 */
	public static java.sql.Time strToTime(String strDate) {
		String str = formatTimeString(strDate);
		SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
		java.util.Date d = null;
		try {
			d = format.parse(str);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}
		java.sql.Time time = null;
		if (d != null)
			time = new java.sql.Time(d.getTime());
		// return time.valueOf(str);
		return time;
	}

	public static Date strToDateTime(String strDateTime, String fromat) {
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat(fromat);
		Date dateTime = null;
		try {
			dateTime = dateTimeFormat.parse(strDateTime);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}
		return dateTime;
	}
}
