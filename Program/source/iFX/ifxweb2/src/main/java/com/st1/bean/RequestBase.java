package com.st1.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestBase {
	static final Logger logger = LoggerFactory.getLogger(RequestBase.class);

	public static boolean isNullOrEmpty(String s) {
		return s == null || s.length() == 0;
	}

	public static boolean isNullOrZero(String s) {
		return s == null || isZero(s);
	}

	private static boolean isZero(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) != '0')
				return false;
		}
		return true;
	}

	public static String trimLeadingZero(String s) {
		// The ^ anchor will make sure that the 0+ being matched is at the beginning of
		// the input.
		// The (?!$) negative lookahead ensures that not the entire string will be
		// matched
		return s.replaceFirst("^0+(?!$)", "");
	}

	public static void main(String[] args) {
		String[] in = { "01234", // "[1234]"
				"0001234a", // "[1234a]"
				"101234", // "[101234]"
				"000002829839", // "[2829839]"
				"0", // "[0]"
				"0000000", // "[0]"
				"0000009", // "[9]"
				"000000z", // "[z]"
				"000000.z", // "[.z]"
		};
		for (String s : in) {
			logger.info("[" + s.replaceFirst("^0+(?!$)", "") + "]");
		}
	}
}
