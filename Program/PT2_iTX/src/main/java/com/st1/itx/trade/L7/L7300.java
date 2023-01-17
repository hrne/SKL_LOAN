package com.st1.itx.trade.L7;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
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
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.SystemParas;
import com.st1.itx.db.service.SystemParasService;
import com.st1.itx.db.service.springjpa.cm.L7300ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.report.ReportUtil;

/**
 * ICS資產資料傳輸
 *
 * @author Wei
 * @version 1.0.0
 */
@Service("L7300")
@Scope("prototype")
public class L7300 extends TradeBuffer {

	@Autowired
	private SystemParasService sSystemParasService;

	@Autowired
	private L7300ServiceImpl sL7300ServiceImpl;

	@Autowired
	private ReportUtil rptUtil;

	// http://10.11.100.22:7001/api-ics/tran-data/Loan
	private String apiUrl = "";

	private String icsFg = "";

	private String icsAuth = "";

	private String stockDate = "";

	private int tranBatchSeq = 0;

	private int tranDataCount = 0;

	private String tranTime = "";

	private BigDecimal mrktValue = BigDecimal.ZERO;

	private String tranSerialSeq = "";

	private int seqL7300 = 0;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L7300 ");
		this.totaVo.init(titaVo);

		setApiUrl(titaVo);

		if (!icsFg.equals("Y")) {
			// ICS啟用記號不為Y
			throw new LogicException("E0001", "ICS啟用記號不為Y,請檢查系統參數設定(L6501)");
		}

		stockDate = rptUtil.showBcDate(titaVo.getEntDyI(), 3);

		tranTime += rptUtil.showBcDate(titaVo.getCalDy(), 3);
		tranTime += " " + rptUtil.showTime(titaVo.getCalTm());

		tranSerialSeq = "LN_V0_" + stockDate + "_" + seqL7300;

		int lmnDy = this.txBuffer.getTxBizDate().getLmnDyf();

		String yearMonth = "" + (int) (lmnDy / 100);

		// doQuery
		List<Map<String, String>> resultList = sL7300ServiceImpl.findAll(yearMonth, titaVo);

		if (resultList == null || resultList.isEmpty()) {
			throw new LogicException("E0001", "ICS需傳輸資料為0筆");
		}

		this.info("ICS需傳輸資料為" + resultList.size() + "筆");
		List<JSONObject> requestJOList = setIcsRequestJo(resultList, titaVo);
		for (JSONObject requestJO : requestJOList) {
			String response = post(requestJO, titaVo);
			String tranStatus = getResponseDetail(response, titaVo);
			totaVo.putParam("TranStatus", tranStatus);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void setApiUrl(TitaVo titaVo) throws LogicException {
		this.info("L7300 setApiUrl");
		SystemParas tSystemParas = sSystemParasService.findById("LN", titaVo);
		if (tSystemParas == null) {
			throw new LogicException("E0001", "L7300,SystemParas");
		}
		icsFg = tSystemParas.getIcsFg();
		apiUrl = tSystemParas.getIcsUrl();
		this.info("icsFg = " + (icsFg == null ? "" : icsFg));
	}

	private List<JSONObject> setIcsRequestJo(List<Map<String, String>> resultList, TitaVo titaVo)
			throws LogicException {
		this.info("L7300 setIcsRequestJo");
		List<JSONArray> assetsDataInfoList = setLoanDto(resultList, titaVo);
		List<JSONObject> requestJOList = new ArrayList<>();
		int tranSubBatchSeq = 1;
		for (JSONArray assetsDataInfo : assetsDataInfoList) {
			JSONObject requestJO = new JSONObject();
			try {
				// 變數名稱:駝峰
				requestJO.putOpt("tranSerialSeq", tranSerialSeq); // 傳輸交易序號
				requestJO.putOpt("tranBatchSeq", tranBatchSeq); // 傳輸母批次序號
				requestJO.putOpt("tranSubBatchSeq", tranSubBatchSeq); // 傳輸子批次序號
				requestJO.putOpt("tranDataSum", mrktValue); // 傳輸資料金額加總
				requestJO.putOpt("tranDataCount", tranDataCount); // 傳輸資料總數
				requestJO.putOpt("opType", "A"); // 作業別
				requestJO.putOpt("sysSrc", "LN"); // 系統來源
				requestJO.putOpt("tranTime", tranTime); // 傳輸日期時間
				requestJO.putOpt("cvDate", stockDate); // 評價日
				requestJO.putOpt("version", "V0"); // 版本
				requestJO.putOpt("assetsCode", "A"); // 資產代號
				requestJO.putOpt("assetsDataInfo", assetsDataInfo);
			} catch (JSONException e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.error("L7300 setLoanDto error = " + errors);
				throw new LogicException("E9005", "設定上傳資料發生錯誤");
			}
			requestJOList.add(requestJO);
			this.info("傳輸交易序號 tranSerialSeq =" + tranSerialSeq);
			this.info("傳輸母批次序號 tranBatchSeq =" + tranBatchSeq);
			this.info("傳輸子批次序號 tranSubBatchSeq =" + tranSubBatchSeq);
			this.info("傳輸資料總數 tranDataCount =" + tranDataCount);
			tranSubBatchSeq++;
		}
		return requestJOList;
	}

	private List<JSONArray> setLoanDto(List<Map<String, String>> dataList, TitaVo titaVo) throws LogicException {
		this.info("L7300 setLoanDto");
		List<JSONArray> assetsDataInfoList = new ArrayList<>();
		JSONArray assetsDataInfo = new JSONArray();
		for (Map<String, String> data : dataList) {
			if (assetsDataInfo.length() >= 100000) {
				assetsDataInfoList.add(assetsDataInfo);
				tranBatchSeq++;
				assetsDataInfo = new JSONArray();
			}
			JSONObject loanDto = new JSONObject();
			mrktValue = mrktValue.add(rptUtil.getBigDecimal(data.get("MrktValue")));
			try {
				// 變數名稱:駝峰
				loanDto.put("tranDataId", data.get("TranDataId")); // 傳輸筆數序號
				loanDto.put("assetsCodeDtl", "A25"); // 資產細項代號
				loanDto.put("stockDate", stockDate); // 庫存日(帳務日)
				loanDto.put("maturityDate", data.get("MaturityDate")); // 到期日
				loanDto.put("loanType", data.get("LoanType")); // 貸款類別(以過往提供ICS填報作業規則進行分類，
				// 若為「企金，商業及農業抵押貸款」填入1；若為「房貸，住宅不動產抵押貸款」填入2。)
				loanDto.put("counterparty", data.get("Counterparty")); // 交易對手(公司戶填入客戶名稱，個人戶則填入代碼)
				loanDto.put("loanInt", data.get("LoanInt")); // 放款利率
				loanDto.put("ltvRatio", data.get("LtvRatio")); // 貸款成數(LTV,%)
				loanDto.put("subCompanyCode", data.get("SubCompanyCode")); // 區隔帳冊別(資金來源)
				loanDto.put("acCurrency", "NTD"); // 記帳幣
				loanDto.put("currency", "NTD"); // 交易幣別
				loanDto.put("mrktValue", data.get("MrktValue")); // 市價
				loanDto.put("bookValue", data.get("BookValue")); // 期初帳面金額
			} catch (JSONException e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.error("L7300 setLoanDto error = " + errors);
				throw new LogicException("E9005", "設定放款傳輸資料發生錯誤");
			}
			assetsDataInfo.put(loanDto);
			tranDataCount++;
		}
		if (assetsDataInfo.length() > 0) {
			assetsDataInfoList.add(assetsDataInfo);
			tranBatchSeq++;
		}
		return assetsDataInfoList;
	}

	private String post(JSONObject requestJo, TitaVo titaVo) throws LogicException {
		this.info("L7300 post");
		String jsonString = requestJo.toString();
		HttpHeaders headers = setIcsHeader();
		HttpEntity<?> request = new HttpEntity<Object>(jsonString, headers);
		RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());
		String result = null;
//		this.info("ICS request = " + request.toString());
		try {
			result = restTemplate.postForObject(apiUrl, request, String.class);
		} catch (RestClientException re) {
			StringWriter errors = new StringWriter();
			re.printStackTrace(new PrintWriter(errors));
			this.error("ICS RestClientException = " + re.getMessage());
			throw new LogicException("E9005", re.getMessage());
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("ICS Exception = " + e.getMessage());
			throw new LogicException("E9005", "");
		}
		this.info("ICS result = " + result);

		return result;
	}

	private HttpHeaders setIcsHeader() {
		this.info("L7300 setIcsHeader");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8); // 指定編碼方式
//		String[] icsAuthArray = icsAuth.split(":");
//		headers.setBasicAuth(icsAuthArray[0], icsAuthArray[1], StandardCharsets.UTF_8); // 指定帳密編碼方式
//		headers.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8)); // 指定傳送/接收資料時可接受編碼方式
		return headers;
	}

	// Override timeouts in request factory
	private SimpleClientHttpRequestFactory getClientHttpRequestFactory() {
		SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
		// Connect timeout
		clientHttpRequestFactory.setConnectTimeout(10_000); // 10_000 = 10000

		// Read timeout
		clientHttpRequestFactory.setReadTimeout(10_000);
		return clientHttpRequestFactory;
	}

	private String getResponseDetail(String response, TitaVo titaVo) throws LogicException {
		this.info("L7300 getResponseDetail");
//		S01	作業執行成功，資料庫新增傳輸內容
//		S02	作業執行成功，資料庫新增傳輸內容，並根據邏輯刪除資料庫歷史資料
//		E01	作業執行失敗，傳輸格式必輸入欄位不可為空值
//		E02	作業執行失敗，傳輸格式必輸入欄位格式錯誤
//		E03	作業執行失敗，傳輸格式子批次須小於等於母批次序號
//		E04	作業執行失敗，本次傳輸筆數不一致
//		E05	作業執行失敗，資產類別與傳輸資料格式有誤
		JSONObject responseJO = null;
		String tranStatus = null;
		String errorMessage = null;
		try {
			responseJO = new JSONObject(response);
			tranStatus = responseJO.getString("tran_status");
			errorMessage = responseJO.getString("error_message");
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("ICS Exception = " + e.getMessage());
		}
		tranStatus += errorMessage == null ? "" : errorMessage;
		return tranStatus;
	}
}