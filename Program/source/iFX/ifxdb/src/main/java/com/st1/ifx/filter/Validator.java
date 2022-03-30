package com.st1.ifx.filter;

import java.util.HashMap;
import java.util.Map;

public class Validator {
	/**
	 * 清除\ + ^ : , / 等特殊字元
	 * 
	 * @param str
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String removeSpecialCharacters(String str) {
		Map map = getClearStringCompareMap();
		String resultStr = "";
		if (str == null) {
			return null;
		}
		for (char c : str.toCharArray()) {
			String s = String.valueOf(c);
			if (map.get(s) != null) {
				resultStr += map.get(s);
			}
		}
		return resultStr;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Map getClearStringCompareMap() {
		Map map = new HashMap();
		map.put("a", "a");
		map.put("b", "b");
		map.put("c", "c");
		map.put("d", "d");
		map.put("e", "e");
		map.put("f", "f");
		map.put("g", "g");
		map.put("h", "h");
		map.put("i", "i");
		map.put("j", "j");
		map.put("k", "k");
		map.put("l", "l");
		map.put("m", "m");
		map.put("n", "n");
		map.put("o", "o");
		map.put("p", "p");
		map.put("q", "q");
		map.put("r", "r");
		map.put("s", "s");
		map.put("t", "t");
		map.put("u", "u");
		map.put("v", "v");
		map.put("w", "w");
		map.put("x", "x");
		map.put("y", "y");
		map.put("z", "z");
		map.put("A", "A");
		map.put("B", "B");
		map.put("C", "C");
		map.put("D", "D");
		map.put("E", "E");
		map.put("F", "F");
		map.put("G", "G");
		map.put("H", "H");
		map.put("I", "I");
		map.put("J", "J");
		map.put("K", "K");
		map.put("L", "L");
		map.put("M", "M");
		map.put("N", "N");
		map.put("O", "O");
		map.put("P", "P");
		map.put("Q", "Q");
		map.put("R", "R");
		map.put("S", "S");
		map.put("T", "T");
		map.put("U", "U");
		map.put("V", "V");
		map.put("W", "W");
		map.put("X", "X");
		map.put("Y", "Y");
		map.put("Z", "Z");
		map.put(".", ".");
		map.put("-", "-");
		map.put("1", "1");
		map.put("2", "2");
		map.put("3", "3");
		map.put("4", "4");
		map.put("5", "5");
		map.put("6", "6");
		map.put("7", "7");
		map.put("8", "8");
		map.put("9", "9");
		map.put("0", "0");
		map.put("/", "/");
		map.put("_", "_");
		map.put(" ", " ");
		return map;
	}

}
