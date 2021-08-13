package com.st1.itx.util.common;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;

@Component("mtToChinese")
@Scope("prototype")
public class AmtToChinese {

	// 大寫數字
	private static final String[] NUMBERS = { "零", "壹", "貳", "參", "肆", "伍", "陸", "柒", "捌", "玖" };
	// 整數部分的單位
	private static final String[] IUNIT = { "元", "拾", "佰", "仟", "萬", "拾", "佰", "仟", "億", "拾", "佰", "仟", "萬", "拾", "佰", "仟" };
	// 小數部分的單位
	private static final String[] DUNIT = { "角", "分", "釐" };

	/**
	 * 轉成中文的大寫金額
	 * 
	 * @param amt 整數金額
	 * @return 中文的大寫金額
	 * @throws LogicException ...
	 */
	public static String toChinese(int amt) throws LogicException {
		String amtx = Integer.toString(amt);

		return toChinese(amtx);
	}

	/**
	 * 轉成中文的大寫金額
	 * 
	 * @param amt BigDecimal 金額
	 * @return 中文的大寫金額
	 * @throws LogicException ...
	 */
	public static String toChinese(BigDecimal amt) throws LogicException {
		String amtx = amt.toString();

		return toChinese(amtx);
	}

	/**
	 * 轉成中文的大寫金額
	 * 
	 * @param str 字串金額
	 * @return 中文的大寫金額
	 * @throws LogicException ...
	 */
	public static String toChinese(String str) throws LogicException {
		// 判斷輸入的金額字符串是否符合要求
		if (StringUtils.isBlank(str) || !str.matches("(-)?[\\d]*(.)?[\\d]*"))
			throw new LogicException("E0015", "請輸入數值資料");

		if ("0".equals(str) || "0.00".equals(str) || "0.0".equals(str))
			return "零元";

		// 判斷是否存在負號"-"
		boolean flag = false;
		if (str.startsWith("-")) {
			flag = true;
			str = str.replaceAll("-", "");
		}

		str = str.replaceAll(",", "");// 去掉","
		String integerStr;// 整數部分數字
		String decimalStr;// 小數部分數字

		// 初始化：分離整數部分和小數部分
		if (str.indexOf(".") > 0) {
			integerStr = str.substring(0, str.indexOf("."));
			decimalStr = str.substring(str.indexOf(".") + 1);
		} else if (str.indexOf(".") == 0) {
			integerStr = "";
			decimalStr = str.substring(1);
		} else {
			integerStr = str;
			decimalStr = "";
		}

		// beyond超出計算能力，直接返回
		if (integerStr.length() > IUNIT.length) {
			throw new LogicException("E0015", "金額超過處理上限仟億");
		}
		int[] integers = toIntArray(integerStr);// 整數部分數字

		// 判斷整數部分是否存在輸入012的情況
		if (integers.length > 1 && integers[0] == 0) {
			throw new LogicException("E0015", "請輸入數值資料");
		}
		boolean isWan = isWan5(integerStr);// 設置萬單位
		int[] decimals = toIntArray(decimalStr);// 小數部分數字
		String result = getChineseInteger(integers, isWan) + getChineseDecimal(decimals);// 返回最終的大寫金額

		if (flag) {
			return "負" + result;// 如果是負數，加上"負"
		} else {
			return result;
		}

	}

	// 將字符串轉爲int數組
	private static int[] toIntArray(String number) {
		int[] array = new int[number.length()];

		for (int i = 0; i < number.length(); i++)
			array[i] = Integer.parseInt(number.substring(i, i + 1));

		return array;
	}

	// 將整數部分轉爲大寫的金額
	private static String getChineseInteger(int[] integers, boolean isWan) {
		StringBuffer chineseInteger = new StringBuffer("");
		int length = integers.length;

		if (length == 1 && integers[0] == 0)
			return "";

		for (int i = 0; i < length; i++) {
			String key = "";
			if (integers[i] == 0) {
				if ((length - i) == 13)// 萬（億）
					key = IUNIT[4];
				else if ((length - i) == 9) {// 億
					key = IUNIT[8];
				} else if ((length - i) == 5 && isWan) {// 萬
					key = IUNIT[4];
				} else if ((length - i) == 1) {// 元
					key = IUNIT[0];
				}
				if ((length - i) > 1 && integers[i + 1] != 0) {
					key += NUMBERS[0];
				}
			}
			chineseInteger.append(integers[i] == 0 ? key : (NUMBERS[integers[i]] + IUNIT[length - i - 1]));
		}
		return chineseInteger.toString();
	}

	// 將小數部分轉爲大寫的金額
	private static String getChineseDecimal(int[] decimals) {
		StringBuffer chineseDecimal = new StringBuffer("");
		for (int i = 0; i < decimals.length; i++) {

			if (i == 3)
				break;

			chineseDecimal.append(decimals[i] == 0 ? "" : (NUMBERS[decimals[i]] + DUNIT[i]));
		}
		return chineseDecimal.toString();
	}

	// 判斷當前整數部分是否已經是達到【萬】
	private static boolean isWan5(String integerStr) {
		int length = integerStr.length();
		if (length > 4) {
			String subInteger = "";

			if (length > 8)
				subInteger = integerStr.substring(length - 8, length - 4);
			else
				subInteger = integerStr.substring(0, length - 4);

			return Integer.parseInt(subInteger) > 0;
		} else
			return false;

	}
}
