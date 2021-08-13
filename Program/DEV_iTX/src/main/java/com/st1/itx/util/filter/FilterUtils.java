package com.st1.itx.util.filter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.text.StringEscapeUtils;

public class FilterUtils {

	private static String lowerChar[] = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z" };
	private static String upperChar[] = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
	private static String specChar[] = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "-", "=", "[", "]", ";", "'", ",", ".", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "_", "+", "{", "}",
			"|", "/", ":", "\"", "<", ">", "?", " " };
	private static Map<String, String> map = new HashMap<String, String>();

	static {
		for (int i = 0; i < lowerChar.length; i++) {
			map.put(lowerChar[i], lowerChar[i]);
		}
		for (int i = 0; i < upperChar.length; i++) {
			map.put(upperChar[i], upperChar[i]);
		}
		for (int i = 0; i < specChar.length; i++) {
			map.put(specChar[i], specChar[i]);
		}
		map.put(File.separator, File.separator);
	}

	/**
	 * Filter Command Content. Solve Command Injection
	 * 
	 * @param inStr String
	 * @return String str
	 */
	public static String filter(String inStr) {
		String outStr = null;
		if (inStr != null) {
			outStr = "";
			char[] chars = inStr.toCharArray();
			int length = chars.length;

			for (int i = 0; i < length; i++) {
				if (map.get(String.valueOf(chars[i])) != null) {
					outStr += map.get(String.valueOf(chars[i]));
				}
			}
		}
		return outStr;
	}

	/**
	 * Escape special character. solve Log Forging
	 * 
	 * @param msg Object
	 * @return String escapMsg
	 */
	public static String escape(Object msg) {

		if (msg == null)
			return null;

		String escapMsg = msg.toString().replace("\n", "").replace("\r", "");
		escapMsg = StringEscapeUtils.escapeJava(escapMsg);

		return escapMsg;
	}
}
