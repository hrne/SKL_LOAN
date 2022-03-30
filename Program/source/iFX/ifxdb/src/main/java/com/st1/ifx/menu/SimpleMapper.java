package com.st1.ifx.menu;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st1.util.cbl.CobolProcessor;

public class SimpleMapper {
	static final Logger logger = LoggerFactory.getLogger(SimpleMapper.class);

	public static <T> T parse(String text, Class<T> requiredType) {
		try {
			T t = requiredType.newInstance();
			CobolProcessor.parse(text, t);
			return t;
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
			return null;
		}
	}

}
