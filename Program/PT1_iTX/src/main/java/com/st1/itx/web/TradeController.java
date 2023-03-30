package com.st1.itx.web;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.st1.itx.dataVO.TotaVoList;
import com.st1.itx.eum.ThreadVariable;
import com.st1.itx.main.ApControl;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.log.SysLogger;

@Controller
@RequestMapping("trade/*")
public class TradeController extends SysLogger {

	@PostConstruct
	public void init() {
		this.mustInfo("TradeController Init....");
	}

	protected TotaVoList callTradeByApCtrl(String tita) {
		ApControl apControl = (ApControl) MySpring.getBean("apControl");
		TotaVoList totaLi = null;

		try {
			totaLi = (TotaVoList) new ObjectMapper().configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true).readValue(apControl.callTrade(tita).substring(5), TotaVoList.class).clone();
			apControl.clearV();
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		}

		return totaLi;
	}

	@SuppressWarnings("unchecked")
	protected Map<String, String> stringToMap(String _d) {
		Gson gson = new Gson();
		Map<String, String> m = new LinkedHashMap<String, String>();
		m = gson.fromJson(_d, m.getClass());

		return m;
	}

	protected ResponseEntity<String> makeJsonResponse(Map<String, ?> m, boolean isOtherNet) {

		Gson gson = null;
		String output = null;
		HttpHeaders responseHeaders = null;
		try {
			gson = new Gson();
			output = gson.toJson(m);
			responseHeaders = new HttpHeaders();
			if (isOtherNet)
				responseHeaders.add("Access-Control-Allow-Origin", "*");
			responseHeaders.add("Content-Type", "application/json; charset=utf-8");
			return new ResponseEntity<String>(output, responseHeaders, HttpStatus.CREATED);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
			return new ResponseEntity<String>("", responseHeaders, HttpStatus.CREATED);
		} finally {
			ThreadVariable.clearThreadLocal();
		}
	}

	protected ResponseEntity<String> makeJsonResponse(List<?> m, boolean isOtherNet) {

		Gson gson = null;
		String output = null;
		HttpHeaders responseHeaders = null;
		try {
			gson = new Gson();
			output = gson.toJson(m);
			responseHeaders = new HttpHeaders();
			if (isOtherNet)
				responseHeaders.add("Access-Control-Allow-Origin", "*");
			responseHeaders.add("Content-Type", "application/json; charset=utf-8");
			return new ResponseEntity<String>(output, responseHeaders, HttpStatus.CREATED);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
			return new ResponseEntity<String>("", responseHeaders, HttpStatus.CREATED);
		} finally {
			ThreadVariable.clearThreadLocal();
		}
	}
}
