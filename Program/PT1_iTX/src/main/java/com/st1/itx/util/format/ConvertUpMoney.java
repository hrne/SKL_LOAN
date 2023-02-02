package com.st1.itx.util.format;

import org.apache.commons.lang3.StringUtils;

import com.st1.itx.Exception.LogicException;

/**
 * 轉換中文金額
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public class ConvertUpMoney {

//大寫數字  
	private static final String[] NUMBERS = { "零", "壹", "貳", "叁", "肆", "伍", "陸", "柒", "捌", "玖" };
// 整數部分的單位  
	private static final String[] IUNIT = { "元", "拾", "佰", "仟", "萬", "拾", "佰", "仟", "億", "拾", "佰", "仟", "萬", "拾", "佰", "仟" };
//小數部分的單位  
	private static final String[] DUNIT = { "角", "分", "厘" };

//轉成中文的大寫金額  
	public static String toChinese(String str) throws LogicException {
//判斷輸入的金額字串是否符合要求  
		if (StringUtils.isBlank(str) || !str.matches("(-)?[\\d]*(.)?[\\d]*"))
			throw new LogicException("EC000", "金額轉換請輸入數字...");

		if ("0".equals(str) || "0.00".equals(str) || "0.0".equals(str)) {
			return "零元";
		}

//判斷是否存在負號"-"  
		boolean flag = false;
		if (str.startsWith("-")) {
			flag = true;
			str = str.replaceAll("-", "");
		}

		str = str.replaceAll(",", "");// 去掉","
		String integerStr;// 整數部分數字
		String decimalStr;// 小數部分數字

//初始化：分離整數部分和小數部分  
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

//beyond超出計算能力，直接返回  
		if (integerStr.length() > IUNIT.length)
			throw new LogicException("EC000", "超出範圍..");

		int[] integers = toIntArray(integerStr);// 整數部分數字
//判斷整數部分是否存在輸入012的情況  
		if (integers.length > 1 && integers[0] == 0) {
			if (flag)
				str = "-" + str;
			throw new LogicException("EC000", "金額轉換請輸入數字...");
		}
		boolean isWan = isWan5(integerStr);// 設定萬單位
		int[] decimals = toIntArray(decimalStr);// 小數部分數字
		String result = getChineseInteger(integers, isWan) + getChineseDecimal(decimals);// 返回最終的大寫金額
		if (flag) {
			return "負" + result;// 如果是負數，加上"負"
		} else {
			return result;
		}
	}

	// 將字串轉為int陣列
	private static int[] toIntArray(String number) {
		int[] array = new int[number.length()];
		for (int i = 0; i < number.length(); i++) {
			array[i] = Integer.parseInt(number.substring(i, i + 1));
		}
		return array;
	}

	// 將整數部分轉為大寫的金額
	public static String getChineseInteger(int[] integers, boolean isWan) {
		StringBuffer chineseInteger = new StringBuffer("");
		int length = integers.length;
		if (length == 1 && integers[0] == 0) {
			return "";
		}
		for (int i = 0; i < length; i++) {
			String k = "";
			if (integers[i] == 0) {
				if ((length - i) == 13)// 萬（億）
					k = IUNIT[4];
				else if ((length - i) == 9) {// 億
					k = IUNIT[8];
				} else if ((length - i) == 5 && isWan) {// 萬
					k = IUNIT[4];
				} else if ((length - i) == 1) {// 元
					k = IUNIT[0];
				}
				if ((length - i) > 1 && integers[i + 1] != 0) {
					k += NUMBERS[0];
				}
			}
			chineseInteger.append(integers[i] == 0 ? k : (NUMBERS[integers[i]] + IUNIT[length - i - 1]));
		}
		return chineseInteger.toString();
	}

	// 將小數部分轉為大寫的金額
	private static String getChineseDecimal(int[] decimals) {
		StringBuffer chineseDecimal = new StringBuffer("");
		for (int i = 0; i < decimals.length; i++) {
			if (i == 3) {
				break;
			}
			chineseDecimal.append(decimals[i] == 0 ? "" : (NUMBERS[decimals[i]] + DUNIT[i]));
		}
		return chineseDecimal.toString();
	}

	// 判斷目前整數部分是否已經是達到【萬】
	private static boolean isWan5(String integerStr) {
		int length = integerStr.length();
		if (length > 4) {
			String subInteger = "";
			if (length > 8) {
				subInteger = integerStr.substring(length - 8, length - 4);
			} else {
				subInteger = integerStr.substring(0, length - 4);
			}
			return Integer.parseInt(subInteger) > 0;
		} else {
			return false;
		}
	}
}