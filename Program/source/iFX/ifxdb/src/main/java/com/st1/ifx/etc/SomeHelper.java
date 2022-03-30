package com.st1.ifx.etc;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SomeHelper {
	private static final Logger logger = LoggerFactory.getLogger(SomeHelper.class);

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

	public static java.sql.Time parseTime6(String s) {
		s = formatTimeString(s);
		return java.sql.Time.valueOf(s);
	}

	public static String formatTimeString(String s) {
		return s.substring(0, 2) + ":" + s.substring(2, 4) + ":" + s.subSequence(4, 6);
	}

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
			logger.warn(errors.toString());
		}
		java.sql.Date date = null;
		if (d != null)
			date = new java.sql.Date(d.getTime());

		return date;
	}

	public static String formatDateString(String s) {
		return s.substring(0, 4) + "/" + s.substring(4, 6) + "/" + s.substring(6);
	}

	public static boolean isNullOrEmpty(String s) {
		return s == null || s.trim().length() == 0;
	}
}
