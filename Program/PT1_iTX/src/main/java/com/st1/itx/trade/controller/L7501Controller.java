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

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.util.date.DateUtil;
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
		this.mustInfo("L7501Controller POST requestBody = " + requestBody);
		JSONObject rb = null;
		try {
			rb = new JSONObject(requestBody);
		} catch (JSONException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.mustInfo("L7501Controller JSONException = " + e.getMessage());
		}
		String tita = settingTitaVo(rb);
		this.mustInfo("L7501Controller tita = " + tita);
		return this.makeJsonResponse(this.callTradeByApCtrl(tita), true);
	}

	private String settingTitaVo(JSONObject rb) {
		if (rb == null) {
			return "";
		}
		TitaVo titaVo = new TitaVo();
		titaVo.init();
		DateUtil d = new DateUtil();
		String tellerNo = "";
		String custId = "";
		try {
			tellerNo = rb.getString("TellerNo");
			custId = rb.getString("CustId");
		} catch (JSONException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.mustInfo("L7501Controller JSONException = " + e.getMessage());
		}
		titaVo.putParam("KINBR", "0000");
		titaVo.putParam("TLRNO", tellerNo);
		titaVo.putParam("TXTNO", 0);
		titaVo.putParam("ENTDY", d.getNowIntegerRoc());
		titaVo.putParam("ORGKIN", "");
		titaVo.putParam("ORGTLR", "");
		titaVo.putParam("ORGTNO", 0);
		titaVo.putParam("ORGDD", 0);
		titaVo.putParam("TRMTYP", "SW");
		titaVo.putParam("TXCD", "L7501");
		titaVo.putParam("MRKEY", "");
		titaVo.putParam("CIFKEY", "");
		titaVo.putParam("CIFERR", "");
		titaVo.putParam("HCODE", 0);
		titaVo.putParam("CRDB", 0);
		titaVo.putParam("HSUPCD", 0);
		titaVo.putParam("CURCD", 00);
		titaVo.putParam("CURNM", "TWD");
		titaVo.putParam("TXAMT", "000000000000.00");
		titaVo.putParam("EMPNOT", tellerNo);
		titaVo.putParam("EMPNOS", tellerNo);
		titaVo.putParam("CALDY", d.getNowIntegerForBC());
		titaVo.putParam("CALTM", d.getNowIntegerTime(true));
		titaVo.putParam("MTTPSEQ", 0);
		titaVo.putParam("TOTAFG", 0);
		titaVo.putParam("OBUFG", 0);
		titaVo.putParam("ACBRNO", "0000");
		titaVo.putParam("RBRNO", "0000");
		titaVo.putParam("FBRNO", "0000");
		titaVo.putParam("RELCD", 1);
		titaVo.putParam("ACTFG", 0);
		titaVo.putParam("SECNO", "  ");
		titaVo.putParam("MCNT", 0);
		titaVo.putParam("TITFCD", 0);
		titaVo.putParam("RELOAD", 0);
		titaVo.putParam("BATCHNO", "      ");
		titaVo.putParam("DELAY", "0");
		titaVo.putParam("FMTCHK", " ");
		titaVo.putParam("FROMMQ", "    ");
		titaVo.putParam("FUNCIND", "0");
		titaVo.putParam("LockNo", 0);
		titaVo.putParam("LockCustNo", 0);
		titaVo.putParam("AUTHNO", "      ");
		titaVo.putParam("AGENT", "      ");
		titaVo.putParam("CustId", custId);
		String result = "";
		try {
			result = titaVo.getJsonString();
		} catch (LogicException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.mustInfo("L7501Controller LogicException = " + e.getMessage());
		}

//		result = result.replaceAll("\"", "\\\"");
		return result;
	}
}
