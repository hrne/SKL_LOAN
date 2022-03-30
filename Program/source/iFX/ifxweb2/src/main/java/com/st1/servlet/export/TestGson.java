package com.st1.servlet.export;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class TestGson {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		test1();

	}

	private static void test1() {
		Gson gson = new Gson();

		String[] strings1 = { "abc1", "def1", "ghi1" };
		String[] strings2 = { "abc2", "def2", "ghi2" };
		String[][] strings = { strings1, strings2 };
		String s = gson.toJson(strings);
		System.out.println(s);

		String[][] ss = gson.fromJson(s, String[][].class);
		for (String[] tt : ss) {
			for (String t : tt)
				System.out.print(t + ", ");
			System.out.println();
		}
		JsonParser jsonParser = new JsonParser();
		JsonArray cols = null;
		cols = jsonParser.parse(s).getAsJsonArray();
		for (JsonElement ee : cols) {
			JsonArray oo = ee.getAsJsonArray();
			for (JsonElement e : oo) {
				String x = e.getAsString();
				System.out.print(x + ',');
			}
			System.out.println();
		}
	}

}
