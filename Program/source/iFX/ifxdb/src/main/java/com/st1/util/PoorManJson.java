package com.st1.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PoorManJson {
	static final Logger logger = LoggerFactory.getLogger(PoorManJson.class);

	public static HashMap json2Map(String json) {
		HashMap<String, String> result = null;
		try {
			result = new ObjectMapper().readValue(json, HashMap.class);
		} catch (JsonParseException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
			throw new RuntimeException(e);
		} catch (IOException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
			throw new RuntimeException(e);
		}
		return result;
	}

	public static String toJson(Object obj) {
		String result = null;
		ObjectMapper m = new ObjectMapper();
		try {
			result = m.writeValueAsString(obj);
		} catch (JsonGenerationException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
			throw new RuntimeException(e);
		} catch (IOException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
			throw new RuntimeException(e);
		}
		return result;
	}

	public static <X> X toObject(String json, Class<X> clazz) {

		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(json, clazz);
		} catch (JsonParseException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		} catch (JsonMappingException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		} catch (IOException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}
		return null;
	}
}
