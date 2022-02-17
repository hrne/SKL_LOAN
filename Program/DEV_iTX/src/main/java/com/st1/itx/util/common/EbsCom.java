package com.st1.itx.util.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
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

	public String sendSlipMediaToEbs(JSONArray summaryTbl, JSONArray journalTbl, TitaVo titaVo)
			throws IOException, JSONException {
		this.info("EbsCom sendSlipMediaToEbs.");

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

		this.info("EbsCom post ebsFg = " + ebsFg);

		if (ebsFg == null || ebsFg.isEmpty() || !ebsFg.equals("Y")) {
			return "";
		}

		// 原本用RestTemplate送出對方收到都是亂碼
		byte[] bytes = requestJO.toString().getBytes(StandardCharsets.UTF_8);
		String jsonString = new String(bytes, StandardCharsets.UTF_8);
		this.info("jsonString = " + jsonString);

		// 取得Url
		String slipMediaUrl = tSystemParas.getEbsUrl();

		String ebsAuth = tSystemParas.getEbsAuth();

		// 清河提供程式碼
		URL url = new URL(slipMediaUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestMethod("POST");
		String basicAuth = "Basic " + new String(Base64.getEncoder().encode(ebsAuth.getBytes()));
		conn.setRequestProperty("Authorization", basicAuth);
		conn.setDoOutput(true);
		conn.setDoInput(true);
		OutputStream os = conn.getOutputStream();
		OutputStreamWriter out = new OutputStreamWriter(os, StandardCharsets.UTF_8);
		out.write(jsonString);
		out.flush();
		InputStream is = conn.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
		StringBuilder response = new StringBuilder();
		while (reader.ready()) {
			response.append(reader.readLine());
			response.append('\r');
		}
		reader.close();

		this.info("HttpURLConnection response = " + response);
		conn.disconnect();

		HttpEntity<String> request = setRequest(ebsAuth, jsonString);

		RestTemplate restTemplate = new RestTemplate();

		restTemplate.getMessageConverters().clear();
		restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

		
		
		this.info("restTemplate post request = " + request.toString());

		String resultAsJsonStr = restTemplate.postForObject(slipMediaUrl, request, String.class);

		this.info("RestTemplate resultAsJsonStr = " + resultAsJsonStr);

		return response.toString();
	}

	private HttpEntity<String> setRequest(String ebsAuth, String jsonString) {

		String[] splitAuth = ebsAuth.split(":");

		String username = splitAuth[0];
		String password = splitAuth[1];

		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);

		headers.setBasicAuth(username, password, StandardCharsets.UTF_8);

		return new HttpEntity<String>(jsonString, headers);
	}

	@Override
	public void exec() throws LogicException {
		this.info("EbsCom exec .");
	}
}
