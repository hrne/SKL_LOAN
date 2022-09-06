package com.st1.itx.util.report;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.tradeService.CommBuffer;

@Component
@Scope("prototype")
public class ReportUtil extends CommBuffer {

	/**
	 * 為了 formatAmt() 預先建好的單位 prefabs 利用 static block 做初始化, 確保 formatAmtTemplates
	 * 只會創建一次 unmodifiableMap 讓此 map 變成唯讀狀態
	 */
	private static final Map<Integer, BigDecimal> formatAmtTemplates;

	static {
		HashMap<Integer, BigDecimal> m = new HashMap<Integer, BigDecimal>();
		m.put(1, BigDecimal.TEN); // 十
		m.put(2, new BigDecimal(100)); // 百
		m.put(3, new BigDecimal(1000)); // 千
		m.put(4, new BigDecimal(10000)); // 萬
		m.put(5, new BigDecimal(100000)); // 十萬
		m.put(6, new BigDecimal(1000000)); // 百萬
		m.put(7, new BigDecimal(10000000)); // 千萬
		m.put(8, new BigDecimal(100000000)); // 億
		formatAmtTemplates = Collections.unmodifiableMap(m);
	}

	/**
	 * 除法-四捨五入<br>
	 * 分母為0時回傳0
	 * 
	 * @param dividend 被除數(分子)
	 * @param divisor  除數(分母)
	 * @param n        四捨五入至小數點後第n位
	 * @return result 結果值
	 */
	public BigDecimal computeDivide(BigDecimal dividend, BigDecimal divisor, int n) {

		BigDecimal result = BigDecimal.ZERO;

		// 除數(分母)大於零時才運算
		if (divisor.compareTo(BigDecimal.ZERO) > 0) {
			result = dividend.divide(divisor, n, RoundingMode.HALF_UP);
		}

		return result;
	}

	/**
	 * 金額轉中文大寫<br>
	 * 例如:<br>
	 * 傳入 new BigDecimal("1234567890")<br>
	 * 回傳 壹拾貳億參仟肆佰伍拾陸萬柒仟捌佰玖拾<br>
	 * <br>
	 * ps.<br>
	 * 1.傳入值會被四捨五入至個位數<br>
	 * 2.傳入值小於零時,回傳值的第一個字為"負"<br>
	 * 3.最大處理數值為九千九百九十九兆,超過時拋錯<br>
	 * 
	 * @param amt 金額
	 * @return String 中文大寫金額
	 * @throws LogicException EC009 金額轉中文大寫時超過最大處理數值
	 */
	public String convertAmtToChinese(BigDecimal amt) throws LogicException {

		String result = "";

		// 將傳入值四捨五入至個位數
		amt = amt.setScale(0, RoundingMode.HALF_UP);

		// 若傳入值小於零
		if (amt.compareTo(BigDecimal.ZERO) < 0) {
			result = "負";
			amt = amt.abs(); // 取絕對值
		}

		// 金額轉字串
		String sAmt = amt.toString();

		// 最大處理數值 九千九百九十九兆...
		if (amt.compareTo(new BigDecimal("999999999999999")) > 0) {
			throw new LogicException("EC009", "(MakeReport)金額轉中文大寫時超過最大處理數值,傳入金額為:" + sAmt);
		}

		String[] chineseNumber = new String[] { "零", "壹", "貳", "參", "肆", "伍", "陸", "柒", "捌", "玖" }; // 漢字的數字
		String[] chineseBasicUnit = new String[] { "", "拾", "佰", "仟" }; // 基本單位
		String[] chineseAdvanceUnit = new String[] { "", "萬", "億", "兆" }; // 對應整數部分擴充套件單位

		// 若金額為零直接回傳零
		if (amt.compareTo(BigDecimal.ZERO) == 0) {
			result = chineseNumber[0];
			return result;
		}

		// 零的計數器
		int zeroCount = 0;

		// 金額轉字串後的長度
		int amtLength = sAmt.length();

		for (int i = 0; i < amtLength; i++) {

			// 目前處理的值
			String num = sAmt.substring(i, i + 1);

			// 目前處理的位數
			int digit = amtLength - i - 1;

			int advanceDigit = digit / 4;

			int basicDigit = digit % 4;

			if (num.equals("0")) {

				zeroCount++;

			} else {

				if (zeroCount > 0) {
					// 補中文零
					result += chineseNumber[0];
				}

				zeroCount = 0; // 歸零

				// 組中文數字+基本單位
				result += chineseNumber[Integer.parseInt(num)] + chineseBasicUnit[basicDigit];
			}

			// 組進階單位
			if (basicDigit == 0 && zeroCount < 4) {

				zeroCount = 0; // 歸零

				result += chineseAdvanceUnit[advanceDigit];
			}
		}

		return result;
	}

	@Override
	public void exec() throws LogicException {
	}

	/**
	 * @param amt 金額
	 * @param n   四捨五入至第n位
	 * @return String 具撇節的金額格式
	 */
	public String formatAmt(BigDecimal amt, int n) {

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

	/**
	 * @param amt     金額
	 * @param n       四捨五入至第n位
	 * @param unitPow 每多少元為單位之次方數（如單位為千元，輸入3）
	 * @return String 具撇節並已除好的金額格式
	 */
	public String formatAmt(BigDecimal amt, int n, int unitPow) {
		if (unitPow <= 1) {
			// do nothing
		} else if (formatAmtTemplates.containsKey(unitPow)) {
			amt = computeDivide(amt, formatAmtTemplates.get(unitPow), n);
		} else {
			// Math.Pow(a,b) -> a 的 b 次方
			amt = computeDivide(amt, BigDecimal.valueOf(Math.pow(10, unitPow)), n);
		}

		return formatAmt(amt, n);
	}

	/**
	 * @param amt 金額
	 * @param n   四捨五入至第n位
	 * @return String 具撇節的金額格式
	 */
	public String formatAmt(String amt, int n) {

		BigDecimal tmpAmt = BigDecimal.ZERO;

		if (amt == null || amt.isEmpty()) {
			this.warn("formatAmt input amt(String) is null or empty");
		} else {
			try {
				tmpAmt = new BigDecimal(amt);
			} catch (NumberFormatException e) {
				this.error("formatAmt input amt:\"" + amt + "\" parse to BigDecimal has NumberFormatException.");
				tmpAmt = BigDecimal.ZERO;
			}
		}

		return formatAmt(tmpAmt, n);
	}

	// key 是次方數
	// value 是 prefab

	/**
	 * @param amt     金額
	 * @param n       四捨五入至第n位
	 * @param unitPow 每多少元為單位之次方數（如單位為千元，輸入3）
	 * @return String 具撇節並已除好的金額格式
	 */
	public String formatAmt(String amt, int n, int unitPow) {
		return formatAmt(getBigDecimal(amt), n, unitPow);
	}

	/**
	 * 傳入double,回傳BigDecimal,無法轉換為BigDecimal時給零
	 * 
	 * @param inputdouble 傳入字串
	 * @return BigDecimal
	 */
	public BigDecimal getBigDecimal(double inputdouble) {
		BigDecimal result = BigDecimal.ZERO;

		try {
			result = BigDecimal.valueOf(inputdouble);
		} catch (NumberFormatException e) {
			this.error("getBigDecimal inputdouble : \"" + inputdouble + "\" parse to BigDecimal has NumberFormatException.");
			result = BigDecimal.ZERO;
		}
		return result;
	}

	/**
	 * 傳入字串,回傳BigDecimal,無法轉換為BigDecimal時給零
	 * 
	 * @param inputString 傳入字串
	 * @return BigDecimal
	 */
	public BigDecimal getBigDecimal(String inputString) {
		BigDecimal result = BigDecimal.ZERO;

		if (inputString == null || inputString.isEmpty()) {
			this.warn("getBigDecimal inputString is null or empty");
		} else {
			try {
				result = new BigDecimal(inputString);
			} catch (NumberFormatException e) {
				this.error("getBigDecimal inputString : \"" + inputString + "\" parse to BigDecimal has NumberFormatException.");
				result = BigDecimal.ZERO;
			}
		}
		return result;
	}

	/**
	 * 取中文日期<br>
	 * 
	 * @param date 日期
	 * @return String 中文日期
	 */
	public String getChineseRocDate(int date) {
		return this.showRocDate(date);
	}

	/**
	 * 顯示西元年<BR>
	 * 
	 * @param date 西曆日期
	 * @param type 樣式<BR>
	 *             type = 0: yyyy/mm/dd<BR>
	 *             type = 1: mm/dd/yy<BR>
	 *             type = 2: yyyymmdd<BR>
	 * @return 西曆日期
	 */
	public String showBcDate(int date, int type) {

		if (date < 10101) {
			return "";
		}

		int bcdate = date;

		if (bcdate < 19110000) {
			bcdate += 19110000;
		}

		String xBcDate = String.valueOf(bcdate);

		String year = "";
		String month = "";
		String day = "";

		if (xBcDate.length() >= 8) {
			year = xBcDate.substring(0, 4);
			month = xBcDate.substring(4, 6);
			day = xBcDate.substring(6, 8);
		} else if (xBcDate.length() == 7) {
			year = xBcDate.substring(0, 3);
			month = xBcDate.substring(3, 5);
			day = xBcDate.substring(5, 7);
		} else if (xBcDate.length() == 6) {
			year = xBcDate.substring(0, 2);
			month = xBcDate.substring(2, 4);
			day = xBcDate.substring(4, 6);
		} else if (xBcDate.length() == 5) {
			year = xBcDate.substring(0, 1);
			month = xBcDate.substring(1, 3);
			day = xBcDate.substring(3, 5);
		}

		String result = "";
		switch (type) {
		case 0:
			result = year + "/" + month + "/" + day;
			break;
		case 1:
			result = month + "/" + day + "/" + year.substring(2, 4);
			break;
		case 2:
			result = year + month + day;
			break;
		default:
			result = year + month + day;
			break;
		}

		return result;
	}

	/**
	 * 顯示西元年<BR>
	 * 
	 * @param date 西曆日期
	 * @param type 樣式<BR>
	 *             type = 0: yyyy/mm/dd<BR>
	 *             type = 1: mm/dd/yy<BR>
	 *             type = 2: yyyymmdd<BR>
	 * @return 西曆日期
	 */
	public String showBcDate(String date, int type) {
		if (date == null || date.isEmpty()) {
			return "";
		}
		int iDate = Integer.parseInt(date);
		return showBcDate(iDate, type);
	}

	/**
	 * 顯示民國年(樣式:yyy/mm/dd)
	 * 
	 * @param date 民國年yyymmdd
	 * @return 民國年yyy/mm/dd
	 */
	public String showDate(String date) {
		if (date == null || date.isEmpty()) {
			return "";
		}
		int ceDate = Integer.valueOf(date) + 19110000;
		return showRocDate(ceDate, 1);
	}

	/**
	 * 顯示民國年(預設樣式:xxx 年 xx 月 xx 日)
	 * 
	 * @param date 西曆日期
	 * @return 中曆日期
	 */
	public String showRocDate(int date) {
		return showRocDate(date, 0);
	}

	/**
	 * 顯示民國年<BR>
	 * <BR>
	 * type = 0: yyy 年 mm 月 dd 日<BR>
	 * type = 1: yyy/mm/dd<BR>
	 * type = 2: yyy-mm-dd<BR>
	 * type = 3: yyymmdd<BR>
	 * type = 4: （中文） yyy 年 mm 月<BR>
	 * type = 5: yyy 年 mm 月<BR>
	 * type = 6: yyy.mm.dd<BR>
	 * 
	 * @param date 西曆日期
	 * @param type 樣式<BR>
	 * @return 中曆日期
	 */
	public String showRocDate(int date, int type) {

		if (date <= 10101) {
			return "";
		}

		int rocdate = date;

		if (rocdate > 19110000) {
			rocdate -= 19110000;
		}
		String rocdatex = String.valueOf(rocdate);

		String rocYear = "";
		String rocMonth = "";
		String rocDay = "";

		if (rocdatex.length() >= 7) {
			rocYear = rocdatex.substring(0, 3);
			rocMonth = rocdatex.substring(3, 5);
			rocDay = rocdatex.substring(5, 7);
		} else if (rocdatex.length() == 6) {
			rocYear = rocdatex.substring(0, 2);
			rocMonth = rocdatex.substring(2, 4);
			rocDay = rocdatex.substring(4, 6);
		} else if (rocdatex.length() == 5) {
			rocYear = rocdatex.substring(0, 1);
			rocMonth = rocdatex.substring(1, 3);
			rocDay = rocdatex.substring(3, 5);
		}

		String result = "";

		switch (type) {
		case 0:
			result = rocYear + "年" + rocMonth + "月" + rocDay + "日";
			break;
		case 1:
			result = rocYear + "/" + rocMonth + "/" + rocDay;
			break;
		case 2:
			result = rocYear + "-" + rocMonth + "-" + rocDay;
			break;
		case 3:
			result = rocYear + rocMonth + rocDay;
			break;
		case 4:
			// 2020-12-29 Mata增加 取得中文年月
			char[] yearNumbers = { '零', '一', '二', '三', '四', '五', '六', '七', '八', '九' };
			char[] yearDigit = { '百', '拾' }; // 年份的數位
			char[] monthNumbers = { ' ', '一', '二', '三', '四', '五', '六', '七', '八', '九' };
			char[] monthDigit = { ' ', '十' }; // 月份的數位
			StringBuilder yearInChinese = new StringBuilder(); // 年份數值的國字
			StringBuilder monthInChinese = new StringBuilder(); // 月份數值的國字
			int nowYear; // 當前年份數字
			int nowMonth; // 當前月份數字
			int zero = 0; // 是否要補零 (待確認)
			int s = 0; // 判斷處理到第幾位 (待確認)

			for (int i = 0; i < rocYear.length(); i++) {
				nowYear = Character.getNumericValue(rocYear.charAt(i));
				if (nowYear == 0) {
					zero++;
				} else {
					zero = 0;
				}
				if (nowYear == 0 && zero > 1) {
					;
				} else if (nowYear == 0 && i == rocYear.length() - 1) {
					;
				} else {
					yearInChinese.append(yearNumbers[nowYear]);
					if (s < 1 && yearInChinese.length() != 0) {
						yearInChinese.append(yearDigit[0]);
					}
					if (nowYear != 0 && s != 0 && s != 2 && yearInChinese.length() != 0) {
						yearInChinese.append(yearDigit[1]);
					}
					s++;
				}
			}

			for (int i = 0; i < rocMonth.length(); i++) {
				nowMonth = Character.getNumericValue(rocMonth.charAt(i));
				if (nowMonth == 0) {
					zero++;
				} else {
					zero = 0;
				}

				if (nowMonth == 0 && zero > 1) {
					;
				} else if (nowMonth == 0 && i == rocMonth.length() - 1) {
					;
				} else {
					monthInChinese.append(monthNumbers[nowMonth]);

					if (s < 1 && monthInChinese.length() != 0 && monthInChinese.length() != -1) {
						monthInChinese.append(monthDigit[0]);
					}
					if (nowMonth != 0 && s != 0 && s != 2 && s != 4 && monthInChinese.length() != 0) {
						monthInChinese.append(monthDigit[1]);
					}
					s++;
				}
			}

			result = yearInChinese.append("年").append(monthInChinese).append("月份").toString();
			break;
		case 5:
			result = rocYear + "年" + rocMonth + "月";
			break;
		case 6:
			result = rocYear + "." + rocMonth + "." + rocDay;
			break;
		default:
			result = rocYear + rocMonth + rocDay;
			break;
		}

		return result;

	}

	/**
	 * 顯示民國年<BR>
	 * <BR>
	 * type = 0: yyy 年 mm 月 dd 日<BR>
	 * type = 1: yyy/mm/dd<BR>
	 * type = 2: yyy-mm-dd<BR>
	 * type = 3: yyymmdd<BR>
	 * type = 4: （中文） yyy 年 mm 月<BR>
	 * type = 5: yyy 年 mm 月<BR>
	 * type = 6: yyy.mm.dd<BR>
	 * 
	 * @param date 西曆日期
	 * @param type 樣式<BR>
	 * @return 中曆日期
	 */
	public String showRocDate(String date, int type) {
		if (date == null || date.isEmpty()) {
			return "";
		}
		return showRocDate(Integer.parseInt(date), type);
	}

	/**
	 * 顯示時間(樣式:hh:mm:ss)
	 * 
	 * @param time 時間hhmmss
	 * @return 時間hh:mm:ss
	 */
	public String showTime(String time) {
		return time.substring(0, 2) + ":" + time.substring(2, 4) + ":" + time.substring(4, 6);
	}

	public boolean haveChinese(String string) {
		for (int i = 0; i < string.length(); i++) {
			String c = string.substring(i, i + 1);
			if (c.matches("[\\u0391-\\uFFE5]+")) {
				return true;
			}
		}
		return false;
	}
}
