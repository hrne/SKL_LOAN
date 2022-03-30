package com.st1.ifx.swiftauto;

import java.util.HashMap;

public class SwiftCur {

	static String[] swfCodeb = new String[] {
			// var swfCodeb = new Array(
			// 允許小數點|幣別|全名
			"Y|ATS|AUSTRIAN BCHILLING", "Y|AUD|AUSTRALIAN", "N|BEC|BELGIAN FRANCE", "N|BEF|BELGIAN FRANCE", "Y|CAD|CANADIAN DOLLAR", "Y|CHF|SWISS FRANCE", "Y|CNY|CHINESE YUAN", "Y|DEM|DEUTSCHE MARK",
			"Y|DKK|DANISH KRONE", "Y|EUR|EUROPEAN CURRENCY", "Y|FRF|FRANCE", "Y|GBP|POUND STERLING", "Y|HKD|HONK KONG DOLLAR", "Y|IDR|INDONESIAN RUPIAH", "Y|INR|INDIAN RUPEE", "N|ITL|ITALIAN LIRA",
			"N|JPY|JAPANESE YEN", "N|KHR|CAMBODIAN RIEL", "Y|MYR|RINGGIT", "Y|NLG|NETHERLANDS GUILDER", "Y|NOK|NORWEGIAN KRONE", "Y|NZD|NEW ZEALAND DOLLAR", "Y|PCT|", "Y|PHP|PHILIPPINE PESO",
			"Y|REN|", "Y|SAR|SAUDI ARABIAN RIYAL", "Y|SEK|SWEDISH KRANC", "Y|SGD|SINGARPORE DOLLAR", "Y|THB|THAI BAHT", "N|TWD|NEW TAIWAN DOLLAR", "Y|USD|U.S. DOLLAR", "Y|XEU|EUROPEAN CURRENCY",
			"Y|ZAR|SOUTH AFRICAN RAND" };
	static HashMap<String, String> codeMap = new HashMap<String, String>();
	static {
		for (String s : swfCodeb) {
			String[] sss = s.split("\\|");
			if (sss.length == 3)
				codeMap.put(sss[1], sss[2]);
			else
				codeMap.put(sss[1], "");
		}
	}

	static String getMoneyName(String cur) {
		if (codeMap.containsKey(cur))
			return cur + "   " + codeMap.get(cur);
		else
			return cur;
	}
}
