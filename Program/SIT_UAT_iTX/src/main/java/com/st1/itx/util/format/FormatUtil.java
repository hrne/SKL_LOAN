package com.st1.itx.util.format;

import java.util.HashMap;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st1.itx.eum.ThreadVariable;
import com.st1.itx.util.filter.FilterUtils;

/**
 * FormatUtil
 * 
 * @author AdamPan
 * @version 1.0.0
 *
 */
public class FormatUtil {
	static final Logger logger = LoggerFactory.getLogger(FormatUtil.class);

	public static String padX(String s, int n) {
		// 將tab取代
		s = s.replaceAll("\t", "");

		char ch[] = s.toCharArray();
		int i = n;
		int len = n;
		for (char c : ch) {
			if ((isChinese(c) || !IsPrintableAsciiChar(c)) && c != '\n') {
				i--;
				len = len - 2;
			} else
				len--;

			if (len <= 0)
				break;
		}

		String re = s.length() >= i ? s.substring(0, i) : s;
		return String.format("%1$-" + i + "s", re);
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
		Iterator<String> iter = map.keySet().iterator();
		while (iter.hasNext()) {
			String k = (String) iter.next();
			String v = (String) map.get(k);
			if (ThreadVariable.isLogger())
				logger.info(FilterUtils.escape(k + ":" + v));
		}
	}

	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
			return true;
		}
		return false;
	}

	private static boolean IsPrintableAsciiChar(char ch) {
		if (32 <= ch && ch <= 126)
			return true;
		return false;
	}
}
