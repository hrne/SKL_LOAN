package com.st1.itx.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.eum.ThreadVariable;

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
				if (ThreadVariable.isLogger())
					logger.info(value + "");
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
}
