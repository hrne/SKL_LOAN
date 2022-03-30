package com.st1.ifx.hcomm.fmt;

import java.util.HashMap;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st1.ifx.filter.FilterUtils;

public class FormatUtil {
	static final Logger logger = LoggerFactory.getLogger(FormatUtil.class);

	public static String padX(String s, int n) {
		return String.format("%1$-" + n + "s", s);
	}

	public static String padLeft(String s, int width) {
		return String.format("%" + width + "s", s);
	}

	public static String pad9(String n, int width) {
		String format = String.format("%%0%dd", width);
		String result = String.format(format, 0) + n;
		return right(result, width);
	}

	public static String rightPad9(String n, int width) {
		String format = String.format("%%0%dd", width);
		String result = n + String.format(format, 0);
		return left(result, width);
	}

	public static String right(String s, int width) {
		if (s.length() <= width)
			return s;
		return s.substring(s.length() - width);
	}

	public static String left(String s, int width) {
		if (s.length() <= width)
			return s;
		return s.substring(0, width);
	}

	public static String pad9(String n, int width, int afterDecimalPoint) {
		String[] ss = n.split("\\.");
		String s1 = ss[0];
		String s2 = "0";
		if (ss.length > 1)
			s2 = ss[1];

		String result = pad9(s1, width);
		if (afterDecimalPoint > 0) {
			result = result + rightPad9(s2, afterDecimalPoint);
		}
		return result;
	}

	public static void dumpMap(HashMap<String, String> map) {
		Iterator iter = map.keySet().iterator();
		while (iter.hasNext()) {
			String k = (String) iter.next();
			String v = (String) map.get(k);
			logger.info(FilterUtils.escape(k + ":" + v));
		}
	}
}
