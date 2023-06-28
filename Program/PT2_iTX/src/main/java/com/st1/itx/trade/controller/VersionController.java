package com.st1.itx.trade.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.st1.itx.web.TradeController;

@Controller
public class VersionController extends TradeController {

	@Value("${app.version}")
	private String appVersion;

	@Override
	@PostConstruct
	public void init() {
		super.mustInfo("VersionController Init....");
	}

	/* method = { RequestMethod.POST, RequestMethod.GET } */

	// http://localhost:8080/iTX/mvc/trade/VS
	@RequestMapping(value = "VS", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> testSec(@RequestBody String requestBody, HttpServletResponse response) {
		this.mustInfo("VersionController appVersion = " + appVersion);
		Map<String, String> map = new HashMap<>();
		map.put("appVersion", appVersion);
		return this.makeJsonResponse(map, true);
	}
}
