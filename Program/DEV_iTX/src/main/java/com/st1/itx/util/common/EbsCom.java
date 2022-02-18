package com.st1.itx.util.common;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.HttpMessageConverter;
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

	public void sendSlipMediaToEbs(JSONArray summaryTbl, JSONArray journalTbl, TitaVo titaVo) throws LogicException {
		this.info("EbsCom sendSlipMediaToEbs.");

		SystemParas tSystemParas = sSystemParasService.findById("LN", titaVo);

		String ebsFg = tSystemParas.getEbsFg();

		this.info("EbsCom post ebsFg = " + ebsFg);

		if (ebsFg == null || ebsFg.isEmpty() || !ebsFg.equals("Y")) {
			return;
		}

		// 取得Url
		String slipMediaUrl = tSystemParas.getEbsUrl();

		String ebsAuth = tSystemParas.getEbsAuth();

		String returnStatus = null;
		JSONObject outputParameters = null;

		try {
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

			String result = post(slipMediaUrl, ebsAuth, requestJO, titaVo);

			outputParameters = new JSONObject(result).getJSONObject("OutputParameters");

			returnStatus = outputParameters.getString("X_RETURN_STATUS");

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("EbsCom Exception = " + e.getMessage());
			throw new LogicException("E9004", "EbsCom");
		}

		if (returnStatus != null && returnStatus.equals("E")) {
			throw new LogicException("E9004", "SlipMediaLog");
		}
	}

	private String post(String slipMediaUrl,String ebsAuth,JSONObject requestJO, TitaVo titaVo) {

		// 原本用RestTemplate送出對方收到都是亂碼
		// 先把資料字串用UTF_8編碼一次
		byte[] bytes = requestJO.toString().getBytes(StandardCharsets.UTF_8);
		String jsonString = new String(bytes, StandardCharsets.UTF_8);
		this.info("jsonString = " + jsonString);
		
		// 清河提供程式碼
//		URL url = new URL(slipMediaUrl);
//		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//		conn.setRequestProperty("Content-Type", "application/json");
//		conn.setRequestMethod("POST");
		String basicAuth = "Basic " + new String(Base64.getEncoder().encode(ebsAuth.getBytes())); // 帳號密碼指定用Base64編碼
//		this.info("basicAuth = " + basicAuth);
//		conn.setRequestProperty("Authorization", basicAuth);
//		conn.setDoOutput(true);
//		conn.setDoInput(true);
//		OutputStream os = conn.getOutputStream();
//		OutputStreamWriter out = new OutputStreamWriter(os, StandardCharsets.UTF_8);
//		out.write(jsonString);
//		out.flush();
//		InputStream is = conn.getInputStream();
//		BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
//		StringBuilder response = new StringBuilder();
//		while (reader.ready()) {
//			response.append(reader.readLine());
//			response.append('\r');
//		}
//		reader.close();
//		this.info("HttpURLConnection response = " + response);
//		conn.disconnect();

		// 我的程式碼
		HttpHeaders headers = setEbsHeader(basicAuth);
		HttpEntity<?> request = new HttpEntity<Object>(jsonString, headers);
		RestTemplate restTemplate = setEbsRestTemplate();
		String result = restTemplate.postForObject(slipMediaUrl, request, String.class);
		this.info("result = " + result);

		return result;
	}

	private HttpHeaders setEbsHeader(String basicAuth) {
		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.add("Content-Type", "application/json; charset=UTF-8"); // 指定編碼方式
		headers.add("Authorization", basicAuth);
		headers.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8)); // 指定可接受編碼方式
		return headers;
	}

	private RestTemplate setEbsRestTemplate() {

		RestTemplate restTemplate = new RestTemplate();

		// 取得HttpMessageConverter
		// 原本附的太多了，我只要一個，而且要指定編碼方式為UTF-8
		List<HttpMessageConverter<?>> listM = restTemplate.getMessageConverters();

		// 清空
		listM.clear();

		// 宣告一個新的，並指定編碼方式
		HttpMessageConverter<?> thisM = new StringHttpMessageConverter(StandardCharsets.UTF_8);

		// 設定不讓它覆寫可接受編碼方式
		((StringHttpMessageConverter) thisM).setWriteAcceptCharset(false);

		// 記得放進list
		listM.add(thisM);

		// 存回模板
		restTemplate.setMessageConverters(listM);

		return restTemplate;
	}

	@Override
	public void exec() throws LogicException {
		this.info("EbsCom exec .");
	}
}
