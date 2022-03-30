package com.st1.ifx.web;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.google.gson.Gson;
import com.st1.ifx.filter.FilterUtils;

public class MyAjaxBase {
	static final Logger logger = LoggerFactory.getLogger(MyAjaxBase.class);

	public MyAjaxBase() {
		super();
	}

	@SuppressWarnings("rawtypes")
	protected ResponseEntity<String> makeJsonResponse(Map m) {
		Gson gson = new Gson();
		String output = gson.toJson(m);
		logger.info("return json:{}", FilterUtils.escape(output));
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=utf-8");
		return new ResponseEntity<String>(output, responseHeaders, HttpStatus.CREATED);
	}

}