package com.st1.itx.util.format;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st1.itx.eum.ThreadVariable;

/**
 * 字串切割 And 遮罩
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public class StringCut {
	private static final Logger logger = LoggerFactory.getLogger(StringCut.class);

	/**
	 * String Cut
	 * 
	 * @param text Content Text
	 * @param sPos Start Index, Start Position On Zero
	 * @param ePos End Index
	 * @return String After Cut
	 */
	public static String stringCut(String text, int sPos, int ePos) {
		if (ThreadVariable.isLogger())
			logger.info("stringCut text : [" + text + "], sPos : [" + sPos + "], ePos : [" + ePos + "]");

		int len = 0;
		String cutString = "";
		for (int i = 0; i < text.length(); ++i) {
			if ((isChinese(text.charAt(i)) || !IsPrintableAsciiChar(text.charAt(i))) && text.charAt(i) != '\n')
				len = len + 2;
			else
				len++;

			if (len >= sPos + 1 && len <= ePos)
				cutString += text.charAt(i);

		}

		return cutString;
	}

	/**
	 * String Mask
	 * 
	 * @param value String
	 * @return String
	 */
	public static String stringMask(String value) {
		if (value == null || value.trim().isEmpty())
			return "";

		char[] c = value.toCharArray();

		StringBuilder sb = new StringBuilder();
		for (int i = 1; i <= c.length; i++) {
			if (i % 2 == 0) {
				if (isChinese(c[i - 1]) || !IsPrintableAsciiChar(c[i - 1]))
					sb.append("＊");
				else
					sb.append("*");
			} else
				sb.append(c[i - 1]);
		}
		return sb.toString();
	}

	/**
	 * replace LineUp
	 * 
	 * @param value String
	 * @return String replace LineUp
	 */
	public static String replaceLineUp(String value) {
		if (value == null)
			value = "";
		String resValue = "";
		String[] ss = value.split("\\r\\n");
		if (ss.length == 1)
			ss = value.split("\\n");
		if (ss.length == 1)
			ss = value.split("\\$n");
		if (ss.length > 1)
			for (String s : ss)
				resValue += s.trim() + " ";
		else
			return value.trim();

//		value = value.replaceAll("\\r\\n", "");
//		value = value.replaceAll("\\n", "");
//		value = value.replaceAll("\\$n", "");

		return resValue.trim();
	}

	/**
	 * String Count Length<br>
	 * Fullwidth Length 2, Halfwidth Length 1
	 * 
	 * @param s String
	 * @return slen
	 * @throws UnsupportedEncodingException UnsupportedEncodingException
	 */
	public static String countLen(String s) throws UnsupportedEncodingException {
		int len = 0;
		for (int i = 0; i < s.length(); ++i) {
			if ((isChinese(s.charAt(i)) || !IsPrintableAsciiChar(s.charAt(i))) && s.charAt(i) != '\n')
				len = len + 2;
			else
				len++;
		}
		len = s.getBytes("UTF-8").length;
		return len + "";
	}

	private static boolean isChinese(char c) {
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
