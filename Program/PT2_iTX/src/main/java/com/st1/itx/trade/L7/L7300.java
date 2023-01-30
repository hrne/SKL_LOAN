package com.st1.itx.trade.L7;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.SystemParas;
import com.st1.itx.db.service.SystemParasService;
import com.st1.itx.db.service.springjpa.cm.L7300ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.JsonDoubleCom;
import com.st1.itx.util.common.RestCom;
import com.st1.itx.util.format.FormatUtil;
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

	private String apiUrl = "";

	private String icsFg = "";

	private String icsAuth = "";

	private String stockDate = "";

	private int tranBatchSeq = 0;

	private int tranDataCount = 0;

	private String tranTime = "";

	private BigDecimal mrktValue = BigDecimal.ZERO;

	private String tranSerialSeq = "";

	private int seqL7300 = 1;

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

		tranSerialSeq = "LN_V0_" + rptUtil.showBcDate(titaVo.getEntDyI(), 2) + "_" + FormatUtil.pad9("" + seqL7300, 1);

		int lmnDy = this.txBuffer.getTxBizDate().getLmnDyf();

		String yearMonth = "" + (lmnDy / 100);

		// doQuery
		List<Map<String, String>> resultList = sL7300ServiceImpl.findAll(yearMonth, titaVo);

		if (resultList == null || resultList.isEmpty()) {
			throw new LogicException("E0001", "ICS需傳輸資料為0筆");
		}

		this.info("ICS需傳輸資料為" + resultList.size() + "筆");
		List<JSONObject> requestJOList = setIcsRequestJo(resultList);
		for (JSONObject requestJO : requestJOList) {
			String response = post(requestJO);
			String tranStatus = getResponseDetail(response);
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

	private List<JSONObject> setIcsRequestJo(List<Map<String, String>> resultList) throws LogicException {
		this.info("L7300 setIcsRequestJo");
		List<JSONArray> assetsDataInfoList = setLoanDto(resultList);
		List<JSONObject> requestJOList = new ArrayList<>();
		int tranSubBatchSeq = 1;
		for (JSONArray assetsDataInfo : assetsDataInfoList) {
			JSONObject requestJO = new JSONObject();
			try {
				// 變數名稱:駝峰
				requestJO.putOpt("tranSerialSeq", tranSerialSeq); // 傳輸交易序號
				requestJO.putOpt("tranBatchSeq", tranBatchSeq); // 傳輸母批次序號
				requestJO.putOpt("tranSubBatchSeq", tranSubBatchSeq); // 傳輸子批次序號
				requestJO.putOpt("tranDataSum", new JsonDoubleCom(mrktValue)); // 傳輸資料金額加總
				requestJO.putOpt("tranDataCount", tranDataCount); // 傳輸資料總數
				requestJO.putOpt("opType", "A"); // 作業別
				requestJO.putOpt("sysSrc", "LN"); // 系統來源
				requestJO.putOpt("tranTime", tranTime); // 傳輸日期時間
				requestJO.putOpt("cvDate", stockDate); // 評價日
				requestJO.putOpt("version", "V0"); // 版本
				requestJO.putOpt("assetsCode", "L"); // 資產代號
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

	private List<JSONArray> setLoanDto(List<Map<String, String>> dataList) throws LogicException {
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

				// 傳輸筆數序號
				// 左補零補足5碼
				loanDto.putOpt("tranDataId", FormatUtil.pad9(data.get("TranDataId"), 5));

				// 資產細項代號
				loanDto.putOpt("assetsCodeDtl", "A25");

				// 庫存日(帳務日)
				// yyyy-mm-dd
				loanDto.putOpt("stockDate", stockDate);

				// 到期日
				// yyyy-mm-dd
				loanDto.putOpt("maturityDate", rptUtil.showBcDate(data.get("MaturityDate"), 3));

				// 貸款類別(以過往提供ICS填報作業規則進行分類，
				// 若為「企金，商業及農業抵押貸款」填入1；若為「房貸，住宅不動產抵押貸款」填入2。)
				loanDto.putOpt("loanType", data.get("LoanType"));

				// 交易對手(公司戶填入客戶名稱，個人戶則填入代碼)
				loanDto.putOpt("counterparty", data.get("Counterparty"));

				// 放款利率
				// Number(5,4)
				loanDto.putOpt("loanInt", new JsonDoubleCom(
						rptUtil.getBigDecimal(data.get("LoanInt")).setScale(4, RoundingMode.HALF_UP)));

				// 貸款成數(LTV,%)
				// Number(5,2)
				loanDto.putOpt("ltvRatio", new JsonDoubleCom(
						rptUtil.getBigDecimal(data.get("LtvRatio")).setScale(2, RoundingMode.HALF_UP)));

				// 區隔帳冊別(資金來源)
				loanDto.putOpt("subCompanyCode", data.get("SubCompanyCode"));

				// 記帳幣
				loanDto.putOpt("acCurrency", "NTD");

				// 交易幣別
				loanDto.putOpt("currency", "NTD");

				// 市價
				// Number(20,5)
				loanDto.putOpt("mrktValue", new JsonDoubleCom(rptUtil.getBigDecimal(data.get("MrktValue"))));

				// 期初帳面金額
				// Number(20,5)
				loanDto.putOpt("bookValue", new JsonDoubleCom(rptUtil.getBigDecimal(data.get("BookValue"))));

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

	private String post(JSONObject requestJo) throws LogicException {
		this.info("L7300 post");
		String jsonString = requestJo.toString();
		HttpEntity<?> request = new HttpEntity<Object>(jsonString, setIcsHeader());
		String result = null;
		this.info("ICS request = " + request.toString());
		RestCom restCom = new RestCom();
		try {
			result = restCom.postForObject(apiUrl, request, String.class);
		} catch (RestClientException re) {
			StringWriter errors = new StringWriter();
			re.printStackTrace(new PrintWriter(errors));
			this.error("ICS RestClientException = " + re.getMessage());
			if (re.getCause() instanceof IOException) {
				throw new LogicException("E9005", "連線中斷");
			} else {
				throw new LogicException("E9005", re.getMessage());
			}
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
		headers.setContentType(MediaType.APPLICATION_JSON); // 指定編碼方式
//		String[] icsAuthArray = icsAuth.split(":");
//		headers.setBasicAuth(icsAuthArray[0], icsAuthArray[1], StandardCharsets.UTF_8); // 指定帳密編碼方式
//		headers.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8)); // 指定傳送/接收資料時可接受編碼方式
		return headers;
	}

	private String getResponseDetail(String response) {
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
		if (response != null && !response.isEmpty()) {
			this.info("ICS response = " + response);
		}
		try {
			responseJO = new JSONObject(response);
			tranStatus = responseJO.getString("tran_status");
			errorMessage = responseJO.getString("error_message");
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("ICS Exception = " + e.getMessage());
		}
		tranStatus += " ";
		if (errorMessage != null && !errorMessage.isEmpty()) {
			tranStatus += " ";
			tranStatus += errorMessage;
		}
		return tranStatus;
	}
}