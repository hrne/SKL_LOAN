package com.st1.itx.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st1.itx.Exception.LogicException;

public class StaticTool {
	private static final Logger logger = LoggerFactory.getLogger(StaticTool.class);

	public static int bcToRoc(int value) {
		if (value != 0 && value > 19110000)
			value = value - 19110000;
		return value;
	}

	public static int rocToBc(int value) throws LogicException {
		if (value != 0 && value < 19110000)
			value = value + 19110000;

		if (value != 0) {
			SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");// 括号内为日期格式，y代表年份，M代表年份中的月份（为避免与小时中的分钟数m冲突，此处用M），d代表月份中的天数
			try {
				sd.setLenient(false);// 此处指定日期/时间解析是否不严格，在true是不严格，false时为严格
				sd.parse(Integer.toString(value));// 从给定字符串的开始解析文本，以生成一个日期
			} catch (Exception e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				logger.error(errors.toString());
				throw new LogicException("EC000", "日期格式錯誤!!!!");
			}
		}
		return value;
	}

	public static boolean checkID(String id) {
		boolean checkResult = false;
		String orgId = id;
		String idHeader = "ABCDEFGHJKLMNPQRSTUVXYWZIO";

		if (Objects.isNull(id) || id.trim().isEmpty())
			return false;

		id = id.trim().toUpperCase(Locale.TAIWAN);

		if (id.length() == 8)
			return checkUniNo(id);

		String pattern = "^[A-Z]$";
		// 驗證填入身分證字號長度及格式
		if (id.length() != 10)
			return false;
		// 格式，用正則表示式比對第一個字母是否為英文字母
		try {
			Integer.parseInt(id.substring(1, 9));
			if (!id.substring(0, 1).matches(pattern))
				return false;
			// 按照轉換後權數的大小進行排序
			// 這邊把身分證字號轉換成準備要對應的
			id = (idHeader.indexOf(id.substring(0, 1)) + 10) + "" + id.substring(1);
			// 開始進行身分證數字的相乘與累加，依照順序乘上1987654321

			int checkNum = Integer.parseInt(id.substring(0, 1)) + Integer.parseInt(id.substring(1, 2)) * 9 + Integer.parseInt(id.substring(2, 3)) * 8 + Integer.parseInt(id.substring(3, 4)) * 7
					+ Integer.parseInt(id.substring(4, 5)) * 6 + Integer.parseInt(id.substring(5, 6)) * 5 + Integer.parseInt(id.substring(6, 7)) * 4 + Integer.parseInt(id.substring(7, 8)) * 3
					+ Integer.parseInt(id.substring(8, 9)) * 2 + Integer.parseInt(id.substring(9, 10));
			int checkNum2 = Integer.parseInt(id.substring(id.length() - 1));
			// 模數 - 總和/模數(10)之餘數若等於第九碼的檢查碼，則驗證成功
			// 若餘數為0，檢查碼就是0
			if ((checkNum % 10) == 0 || (10 - checkNum % 10) == checkNum2)
				checkResult = true;
			else
				checkResult = false;
		} catch (Exception e) {
			checkResult = false;
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}

		if (checkResult)
			return checkResult;
		// 外籍
		id = orgId;
		try {
			Integer.parseInt(id.substring(2, 8));
			if (!id.substring(0, 1).matches(pattern) || !id.substring(1, 2).matches(pattern))
				return false;
			// 按照轉換後權數的大小進行排序
			// 這邊把身分證字號轉換成準備要對應的
			id = (idHeader.indexOf(id.substring(0, 1)) + 10) + "" + ((idHeader.indexOf(id.substring(1, 2)) + 10) % 10) + "" + id.substring(2, 8);
			// 開始進行身分證數字的相乘與累加，依照順序乘上1987654321
			int checkNum = Integer.parseInt(id.substring(0, 1)) + Integer.parseInt(id.substring(1, 2)) * 9 + Integer.parseInt(id.substring(2, 3)) * 8 + Integer.parseInt(id.substring(3, 4)) * 7
					+ Integer.parseInt(id.substring(4, 5)) * 6 + Integer.parseInt(id.substring(5, 6)) * 5 + Integer.parseInt(id.substring(6, 7)) * 4 + Integer.parseInt(id.substring(7, 8)) * 3
					+ Integer.parseInt(id.substring(8, 9)) * 2 + Integer.parseInt(id.substring(9, 10));
			// 檢查號碼 = 10 - 相乘後個位數相加總和之尾數。
			int checkNum2 = Integer.parseInt(id.substring(id.length() - 1));
			/// 若餘數為0，檢查碼就是0
			if ((checkNum % 10) == 0 || (10 - checkNum % 10) == checkNum2)
				checkResult = true;
			else
				checkResult = false;
		} catch (Exception e) {
			checkResult = false;
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}
		return checkResult;
	}

	private static boolean checkUniNo(String id) {
		String tmp = "12121241";
		int sum = 0;

		if (!id.matches("\\d{8}$"))
			return false;

		for (int i = 0; i < 8; i++) {
			int s1 = Integer.parseInt(id.substring(i, i + 1));
			int s2 = Integer.parseInt(tmp.substring(i, i + 1));
			sum += cal(s1 * s2);
		}

		return sum % 10 == 0 ? true : false;
	}

	private static int cal(int n) {
		int sum = 0;
		while (n != 0) {
			sum += (n % 10);
			n = (n - n % 10) / 10;
		}
		return sum;
	}
}
