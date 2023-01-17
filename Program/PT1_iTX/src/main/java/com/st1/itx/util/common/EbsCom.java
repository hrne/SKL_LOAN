package com.st1.itx.util.common;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.SlipEbsRecord;
import com.st1.itx.db.domain.SystemParas;
import com.st1.itx.db.service.SlipEbsRecordService;
import com.st1.itx.db.service.SystemParasService;
import com.st1.itx.tradeService.CommBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;

@Component("EbsCom")
@Scope("prototype")
public class EbsCom extends CommBuffer {

	@Autowired
	private DateUtil dDateUtil;
	@Autowired
	private WebClient webClient;
	@Autowired
	private SystemParasService sSystemParasService;
	@Autowired
	private SlipEbsRecordService sSlipEbsRecordService;

	private String ebsFg = "";
	private String slipMediaUrl = "";
	private String ebsAuth = "";

	public boolean sendSlipMediaToEbs(JSONArray summaryTbl, JSONArray journalTbl, TitaVo titaVo) throws LogicException {
		this.info("EbsCom sendSlipMediaToEbs.");

		// 設定連線資訊
		setConnection(titaVo);

		if (ebsFg == null || ebsFg.isEmpty() || !ebsFg.equals("Y")) {
			// 訊息通知 SystemParas.EbsFg != Y
			webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "", "",
					"系統參數設定檔的EBS啟用記號不為Y，L9130總帳傳票不上傳至EBS", titaVo);
			return false;
		}

		// 組合上傳資料
		JSONObject requestJO = setEbsRequestJo(summaryTbl, journalTbl);

		// 交換資料並記錄交換結果
		String result = postAndInsertRecord(requestJO, titaVo);

		// 分析回傳資料
		String returnStatus = analyzeResult(result);

		if (returnStatus != null && returnStatus.equals("S")) {
			// 發送成功訊息
			webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "", "", "L9130總帳傳票上傳至EBS成功",
					titaVo);
			return true;
		} else {
			return false;
		}
	}

	private void setConnection(TitaVo titaVo) throws LogicException {
		SystemParas tSystemParas = sSystemParasService.findById("LN", titaVo);

		if (tSystemParas == null) {
			throw new LogicException("E0001", "EbsCom,SystemParas");
		}

		ebsFg = tSystemParas.getEbsFg();

		this.info("EbsCom ebsFg = " + (ebsFg == null ? "" : ebsFg));

		// 取得Url
		slipMediaUrl = tSystemParas.getEbsUrl();
		// 取得帳密
		ebsAuth = tSystemParas.getEbsAuth();
	}

	private JSONObject setEbsRequestJo(JSONArray summaryTbl, JSONArray journalTbl) throws LogicException {
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
		return requestJO;
	}

	private String postAndInsertRecord(JSONObject requestJo, TitaVo titaVo) throws LogicException {
		String jsonString = requestJo.toString();
		HttpHeaders headers = setEbsHeader();
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
			insertSlipEbsRecord(requestJo, "EbsCom交換資料時發生錯誤,Exception = " + e.getMessage(), titaVo);
			throw new LogicException("E9004", "EbsCom交換資料時發生錯誤");
		}
		this.info("EbsCom result = " + result);
		insertSlipEbsRecord(requestJo, result, titaVo);

		return result;
	}

	private HttpHeaders setEbsHeader() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8); // 指定編碼方式
		String[] ebsAuthArray = ebsAuth.split(":");
		headers.setBasicAuth(ebsAuthArray[0], ebsAuthArray[1], StandardCharsets.UTF_8); // 指定帳密編碼方式
		headers.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8)); // 指定傳送/接收資料時可接受編碼方式
		return headers;
	}

	private void insertSlipEbsRecord(JSONObject requestJo, String result, TitaVo titaVo) throws LogicException {
		String groupId = null;
		try {
			groupId = requestJo.getJSONObject("main").getJSONObject("InputParameters").getJSONObject("P_SUMMARY_TBL")
					.getJSONArray("P_SUMMARY_TBL_ITEM").getJSONObject(0).getString("GROUP_ID");
		} catch (JSONException e1) {
			groupId = "RequestERR";
		}

		// 寫進TABLE存起來
		SlipEbsRecord tSlipEbsRecord = new SlipEbsRecord();

		tSlipEbsRecord.setGroupId(groupId);
		tSlipEbsRecord.setRequestData(requestJo.toString());
		tSlipEbsRecord.setResultData(result);

		try {
			sSlipEbsRecordService.insert(tSlipEbsRecord, titaVo);
		} catch (DBException e1) {
			throw new LogicException("E0001", "EbsCom,SlipEbsRecord");
		}
	}

	private String errorMsg = "";

	private List<Map<String, String>> errorList = null;

	private String analyzeResult(String result) throws LogicException {
		JSONObject outputParameters = null;
		String returnStatus = null;
		JSONObject errorDetailsTbl = null;
		JSONArray errorDetailsTblItem = null;
		errorMsg = "";
		List<Map<String, String>> errorList = new ArrayList<>();
		try {
			outputParameters = new JSONObject(result).getJSONObject("OutputParameters");
			returnStatus = outputParameters.getString("X_RETURN_STATUS");

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("EbsCom Exception = " + e.getMessage());
			throw new LogicException("E9004", "EbsCom分析回傳資料時有誤");
		}
		try {
			if (returnStatus.equals("E")) {
				errorDetailsTbl = outputParameters.getJSONObject("X_ERROR_DETAILS_TBL");
				if (errorDetailsTbl != null) {
					errorDetailsTblItem = errorDetailsTbl.getJSONArray("X_ERROR_DETAILS_TBL_ITEM");
					if (errorDetailsTblItem != null) {
						int size = errorDetailsTblItem.length();
						for (int i = 0; i < size; i++) {
							Map<String, String> error = new HashMap<>();
							JSONObject errorDetail = errorDetailsTblItem.getJSONObject(i);
							error.put("GroupId", errorDetail.getString("GROUP_ID"));
							error.put("JournalName", errorDetail.getString("JOURNAL_NAME"));
							error.put("JeLineNum", errorDetail.getString("JE_LINE_NUM"));
							error.put("ErrorCode", errorDetail.getString("ERROR_CODE"));
							error.put("ErrorMessage", errorDetail.getString("ERROR_MESSAGE"));
							errorList.add(error);
						}
					}
				}
			}
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("EbsCom Exception = " + e.getMessage());
			throw new LogicException("E9004", "EbsCom分析錯誤訊息時有誤");
		}
		return returnStatus;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public List<Map<String, String>> getErrorList() {
		return errorList == null ? new ArrayList<>() : errorList;
	}

	@Override
	public void exec() throws LogicException {
		// nothing
	}
}
