package com.st1.ifx.swiftauto;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IncomingMessage {
	String text;

	public IncomingMessage(String t) {
		this.text = t;
	}

	public boolean isFromMessage() {

		String patternString = "\\{2:(O.+?)}";
		Pattern p = Pattern.compile(patternString);
		Matcher m = p.matcher(this.text);
		if (m.find()) {
			String s = m.group(); // text.substring(m.start(), m.end());
			// System.out.println(s);
		}
		return m.find();
	}

	static String reBlk1 = "\\{1:(.+?)}";
	static String reBlk4 = "\\{4:(.+?)-}";
	// 柯:新增此段判斷是否為特殊解析電文
	static String reBlk4spc = "\\{4:(.+?)}}";
	static HashMap<String, String> regMap = new HashMap<String, String>();
	static {
		regMap.put("b2", "\\{2:.+?}");
		regMap.put("b3", "\\{3:.+?}");
		regMap.put("b451", "\\{451:(.+?)}}");
		regMap.put("b5", "\\{5:.+?}");
		regMap.put("s", "\\{S:.+?}");
		regMap.put("trn", "\\{TRN:.+?}}");
	}
	private String mt;
	HashMap<String, String> resultMap;

	public HashMap<String, String> parse() {
		resultMap = new HashMap<String, String>();

		findAll("b1", reBlk1);
		for (String k : regMap.keySet()) {
			findOne(k, regMap.get(k));
		}

		String b2 = resultMap.get("b2");
		mt = b2.substring(4, 7);
		// 電文代號是0開頭的 則把 {與}取代
		if (mt.startsWith("0")) {
			findAll("b4", reBlk4spc);
		} else {
			findAll("b4", reBlk4);
		}
		return resultMap;
	}

	private void findOne(String k, String regString) {
		Pattern p = Pattern.compile(regString);
		Matcher m = p.matcher(this.text);
		if (m.find()) {
			resultMap.put(k, m.group());
			System.out.printf("%s:%s\n", k, m.group());
		}

	}

	private void findAll(String tagPrefix, String regString) {
		Pattern p = Pattern.compile(regString, Pattern.DOTALL);
		Matcher m = p.matcher(this.text);
		int i = 0;
		while (m.find()) {
			String tag = String.format("%s.%d", tagPrefix, i++);
			resultMap.put(tag, m.group());
			System.out.printf("%s:%s\n", tag, m.group());
		}
	}

	public String getMt() {
		return mt;
	}

	public HashMap<String, String> getResultMap() {
		return resultMap;
	}

}
