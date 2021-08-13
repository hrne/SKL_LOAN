package com.st1.itx.util.common;

import java.io.IOException;

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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.SystemParas;
import com.st1.itx.db.service.SystemParasService;

@Component("EbsCom")
@Scope("prototype")
public class EbsCom {

	@Autowired
	private SystemParasService sSystemParasService;

	ObjectMapper objectMapper = new ObjectMapper();

	public String sendSlipMediaToEbs(JSONArray summaryTbl, JSONArray journalTbl, TitaVo titaVo) throws IOException, JSONException {

		JSONObject requestJO = new JSONObject();
		JSONObject main = new JSONObject();
		JSONObject inputParameters = new JSONObject();

		JSONObject summaryTblItem = new JSONObject();

		summaryTblItem.putOpt("P_SUMMARY_TBL_ITEM", summaryTbl);
		inputParameters.putOpt("P_SUMMARY_TBL", summaryTblItem);

		JSONObject journalTblItem = new JSONObject();

		journalTblItem.putOpt("P_JOURNAL_TBL_ITEM", journalTbl);
		inputParameters.putOpt("P_JOURNAL_TBL", journalTblItem);

		main.putOpt("InputParameters", inputParameters);

		requestJO.putOpt("main", main);

		return post(requestJO, titaVo);
	}

	private String post(JSONObject requestJO, TitaVo titaVo) throws IOException {

		SystemParas tSystemParas = sSystemParasService.findById("LN", titaVo);

		String ebsFg = tSystemParas.getEbsFg();

		if (ebsFg == null || ebsFg.isEmpty() || !ebsFg.equals("Y")) {
			return "";
		}

		// 取得Url
		String slipMediaUrl = tSystemParas.getEbsUrl();

		String ebsAuth = tSystemParas.getEbsAuth();

		HttpEntity<String> request = setRequest(ebsAuth, requestJO);

		RestTemplate restTemplate = new RestTemplate();

		String resultAsJsonStr = restTemplate.postForObject(slipMediaUrl, request, String.class);

		JsonNode root = objectMapper.readTree(resultAsJsonStr);

		return root.path("X_RETURN_STATUS").asText();

	}

	private HttpEntity<String> setRequest(String ebsAuth, JSONObject requestJO) {

		String jsonString = requestJO.toString();

		String[] splitAuth = ebsAuth.split(":");

		HttpHeaders headers = new HttpHeaders();

		headers.setBasicAuth(splitAuth[0], splitAuth[1]);

		headers.setContentType(MediaType.APPLICATION_JSON);

		return new HttpEntity<String>(jsonString, headers);
	}
}
