package com.st1.itx.trade.controller;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.st1.itx.web.TradeController;

@Controller
public class L7501Controller extends TradeController {

	@Override
	@PostConstruct
	public void init() {
		super.mustInfo("L7501Controller Init....");
	}

	/* method = { RequestMethod.POST, RequestMethod.GET } */

	// http://localhost:8080/iTX/mvc/trade/L7501
	@RequestMapping(value = "L7501", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<String> testSec(@RequestBody String requestBody, HttpServletResponse response) {
		this.mustInfo("L7501 POST requestBody = " + requestBody);
		String tita = "";
		try {
			tita = new JSONObject(requestBody).getString("TITA");
		} catch (JSONException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.mustInfo("L7501 JSONException = " + e.getMessage());
		}
		this.mustInfo("L7501 tita = " + tita);
		return this.makeJsonResponse(this.callTradeByApCtrl(tita), true);
	}

}
