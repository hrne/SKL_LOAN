package com.st1.itx.trade.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.st1.itx.util.common.VersionCom;
import com.st1.itx.web.TradeController;

@Controller
public class VersionController extends TradeController {

	@Value("${appVersion}")
	private String appVersion;

	@Autowired
	private VersionCom versionCom;

	@Override
	@PostConstruct
	public void init() {
		super.mustInfo("VersionController Init....");
		super.mustInfo("VersionController appVersion = " + appVersion);
	}

	/* method = { RequestMethod.POST, RequestMethod.GET } */

	// http://localhost:8080/iTX/mvc/trade/VS
	@RequestMapping(value = "VS", method = { RequestMethod.GET })
	@ResponseBody
	public ResponseEntity<String> testSec(HttpServletResponse response) {
		this.mustInfo("VersionController appVersion = " + versionCom.getAppVersion());
		Map<String, String> map = new HashMap<>();
		map.put("appVersion", versionCom.getAppVersion());
		return this.makeJsonResponse(map, true);
	}
}
