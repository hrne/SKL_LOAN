package com.st1.itx.util.format;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
	private static final Logger logger = LoggerFactory.getLogger(FormatUtil.class);

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
		if (len == -1)
			i++;
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
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
			return true;
		}
		return false;
	}

	private static boolean IsPrintableAsciiChar(char ch) {
		if (32 <= ch && ch <= 126)
			return true;
		return false;
	}

	/**
	 * 金額加撇節
	 *
	 * @param amt 金額
	 * @param n   四捨五入至第n位
	 * @return String 具撇節的金額格式
	 */
	public static String formatAmt(BigDecimal amt, int n) {

		amt = amt == null ? BigDecimal.ZERO : amt;

		String result = "";

		String sAmt = amt.setScale(n, RoundingMode.HALF_UP).toString();

		String dec = "";

		// 若有保留小數位數 先擷取小數點及小數點後數字
		// 拆成兩段,僅有小數點前的數值需要加撇節
		if (n > 0) {
			int point = sAmt.indexOf(".");

			dec = sAmt.substring(point);

			sAmt = sAmt.substring(0, point);
		}

		String sign = "";

		// 負數時先把負號拔掉
		if (amt.compareTo(BigDecimal.ZERO) < 0) {
			sign = "-";
			sAmt = sAmt.substring(1);
		}

		// 取得整數總長
		int amtLength = sAmt.length();

		int remainder = amtLength % 3;

		for (int i = 1; i <= amtLength; i++) {
			result += sAmt.substring(i - 1, i);
			if ((i == remainder || (i - remainder) % 3 == 0) && i != amtLength) {
				result += ",";
			}
		}

		result += dec;

		// 負數時把負號組回
		if (amt.compareTo(BigDecimal.ZERO) < 0) {
			result = sign + result;
		}

		return result;
	}
}
