package com.st1.ifx.filter;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilterUtils {
	private static final Logger logger = LoggerFactory.getLogger(FilterUtils.class);

	private static String lowerChar[] = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o",
			"p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z" };
	private static String upperChar[] = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
			"P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
	private static String specChar[] = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "-", "=", "[", "]", ";", "'",
			",", ".", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "_", "+", "{", "}", "|", "/", ":", "\"", "<",
			">", "?", " " };
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
	 * @param inStr
	 * @return
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
	 * @param msg
	 * @return
	 */
	public static String escape(Object msg) {

		if (msg == null)
			return null;

		String escapMsg = msg.toString().replace("\n", "").replace("\r", "");
		escapMsg = StringEscapeUtils.escapeJava(escapMsg);

		return escapMsg;
	}

	public static byte[] stripXSS(byte[] value1) {
		String value = null;
		byte[] value2 = null;
		try {
			value = new String(value1, "UTF-8");
			logger.info(value);
		} catch (Throwable t) {
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			logger.warn(errors.toString());
		}
		if (value != null) {
			// NOTE: It's highly recommended to use the ESAPI library and uncomment the
			// following line to
			// avoid encoded attacks.
			// value = ESAPI.encoder().canonicalize(value);

			// Avoid null characters
			value = value.replaceAll("", "");

			// Avoid anything between script tags
			Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
			value = scriptPattern.matcher(value).replaceAll("");

			// Avoid anything in a src='...' type of expression
			scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			value = scriptPattern.matcher(value).replaceAll("");

			scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			value = scriptPattern.matcher(value).replaceAll("");

			// Remove any lonesome </script> tag
			scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
			value = scriptPattern.matcher(value).replaceAll("");

			// Remove any lonesome <script ...> tag
			scriptPattern = Pattern.compile("<script(.*?)>",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			value = scriptPattern.matcher(value).replaceAll("");

			// Avoid eval(...) expressions
			scriptPattern = Pattern.compile("eval\\((.*?)\\)",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			value = scriptPattern.matcher(value).replaceAll("");

			// Avoid expression(...) expressions
			scriptPattern = Pattern.compile("expression\\((.*?)\\)",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			value = scriptPattern.matcher(value).replaceAll("");

			// Avoid javascript:... expressions
			scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
			value = scriptPattern.matcher(value).replaceAll("");

			// Avoid vbscript:... expressions
			scriptPattern = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
			value = scriptPattern.matcher(value).replaceAll("");

			// Avoid onload= expressions
			scriptPattern = Pattern.compile("onload(.*?)=",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			value = scriptPattern.matcher(value).replaceAll("");
		}

		try {
			if (value != null)
				value2 = value.getBytes("UTF-8");
		} catch (Throwable t) {
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			logger.warn(errors.toString());
		}
		return value2;
	}

	public static void main(String[] args) {
		System.out.println(FilterUtils.filter("! , @&^\\/"));
	}

}
