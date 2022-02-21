package com.st1.itx.util.common;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.SystemParas;
import com.st1.itx.db.service.SystemParasService;
import com.st1.itx.tradeService.CommBuffer;

@Component("EbsCom")
@Scope("prototype")
public class EbsCom extends CommBuffer {

	@Autowired
	private SystemParasService sSystemParasService;

	ObjectMapper objectMapper = new ObjectMapper();

	public void sendSlipMediaToEbs(JSONArray summaryTbl, JSONArray journalTbl, TitaVo titaVo) throws LogicException {
		this.info("EbsCom sendSlipMediaToEbs.");

		SystemParas tSystemParas = sSystemParasService.findById("LN", titaVo);

		if (tSystemParas == null) {
			throw new LogicException("E0001", "EbsCom,SystemParas");
		}

		String ebsFg = tSystemParas.getEbsFg();

		this.info("EbsCom ebsFg = " + (ebsFg == null ? "" : ebsFg));

		if (ebsFg == null || ebsFg.isEmpty() || !ebsFg.equals("Y")) {
			return;
		}

		// 取得Url
		String slipMediaUrl = tSystemParas.getEbsUrl();
		String ebsAuth = tSystemParas.getEbsAuth();
		String returnStatus = null;
		JSONObject outputParameters = null;
		JSONObject requestJO = new JSONObject();
		JSONObject main = new JSONObject();
		JSONObject inputParameters = new JSONObject();
		JSONObject summaryTblItem = new JSONObject();
		JSONObject journalTblItem = new JSONObject();

		try {
			summaryTblItem.putOpt("P_SUMMARY_TBL_ITEM", summaryTbl);
			inputParameters.putOpt("P_SUMMARY_TBL", summaryTblItem);
			journalTblItem.putOpt("P_JOURNAL_TBL_ITEM", journalTbl);
			inputParameters.putOpt("P_JOURNAL_TBL", journalTblItem);
			main.putOpt("InputParameters", inputParameters);
			requestJO.putOpt("main", main);
		} catch (JSONException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("EbsCom Exception = " + e.getMessage());
			throw new LogicException("E9004", "EbsCom組合上傳資料時有誤");
		}

		String result = post(slipMediaUrl, ebsAuth, requestJO.toString(), titaVo);

		try {
			outputParameters = new JSONObject(result).getJSONObject("OutputParameters");
			returnStatus = outputParameters.getString("X_RETURN_STATUS");
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("EbsCom Exception = " + e.getMessage());
			throw new LogicException("E9004", "EbsCom分析收到的訊息時有誤");
		}

		if (returnStatus != null && returnStatus.equals("E")) {
			// TODO:寫進TABLE存起來
			throw new LogicException("E9004", "EbsCom上傳之資料檢核有誤");
		}
	}

	private String post(String slipMediaUrl, String ebsAuth, String jsonString, TitaVo titaVo) throws LogicException {
		HttpHeaders headers = setEbsHeader(ebsAuth);
		HttpEntity<?> request = new HttpEntity<Object>(jsonString, headers);
		RestTemplate restTemplate = new RestTemplate();
		String result = null;
		this.info("EbsCom request = " + request.toString());
		try {
			result = restTemplate.postForObject(slipMediaUrl, request, String.class);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("EbsCom Exception = " + e.getMessage());
			throw new LogicException("E9004", "EbsCom上傳資料中發生錯誤");
		}
		this.info("EbsCom result = " + result);

		return result;
	}

	private HttpHeaders setEbsHeader(String ebsAuth) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8); // 指定編碼方式
		String[] ebsAuthArray = ebsAuth.split(":");
		headers.setBasicAuth(ebsAuthArray[0], ebsAuthArray[1], StandardCharsets.UTF_8); // 指定帳密編碼方式
		headers.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8)); // 指定傳送/接收資料時可接受編碼方式
		return headers;
	}

	@Override
	public void exec() throws LogicException {
		this.info("EbsCom exec .");
	}
}
