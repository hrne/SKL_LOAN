package com.st1.itx.trade.L7;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.Ias34Ap;
import com.st1.itx.db.domain.Ifrs9FacData;
import com.st1.itx.db.domain.Ifrs9FacDataId;
import com.st1.itx.db.domain.LoanIfrs9Ap;
import com.st1.itx.db.domain.MonthlyFacBal;
import com.st1.itx.db.domain.MonthlyFacBalId;
import com.st1.itx.db.domain.SystemParas;
import com.st1.itx.db.domain.TxBizDate;
import com.st1.itx.db.service.Ias34ApService;
import com.st1.itx.db.service.Ifrs9FacDataService;
import com.st1.itx.db.service.JobMainService;
import com.st1.itx.db.service.LoanIfrs9ApService;
import com.st1.itx.db.service.MonthlyFacBalService;
import com.st1.itx.db.service.MonthlyLM052AssetClassService;
import com.st1.itx.db.service.MonthlyLM052LoanAssetService;
import com.st1.itx.db.service.MonthlyLM052OvduService;
import com.st1.itx.db.service.SystemParasService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.FileCom;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

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

	// http://10.11.100.22:7001/api-ics/tran-data/Loan
	private String apiUrl = "";

	private String icsFg = "";
	
	private String icsAuth = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L7300 ");
		this.totaVo.init(titaVo);

		setApiUrl(titaVo);

		if (!icsFg.equals("Y")) {
			// ICS啟用記號不為Y
			throw new LogicException("E0001", "ICS啟用記號不為Y,請檢查系統參數設定(L6501)");
		}

		// doQuery
		List<Map<String, String>> resultList = new ArrayList<>();

		if (resultList != null && !resultList.isEmpty()) {
			JSONObject requestJO = setIcsRequestJo(resultList, titaVo);
			post(requestJO, titaVo);
		} else {
			throw new LogicException("E0001", "ICS需傳輸資料為0筆");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void setApiUrl(TitaVo titaVo) throws LogicException {
		SystemParas tSystemParas = sSystemParasService.findById("LN", titaVo);
		if (tSystemParas == null) {
			throw new LogicException("E0001", "L7300,SystemParas");
		}
		icsFg = tSystemParas.getIcsFg();
		apiUrl = tSystemParas.getIcsUrl();
		this.info("icsFg = " + (icsFg == null ? "" : icsFg));
	}

	private JSONObject setIcsRequestJo(List<Map<String, String>> resultList, TitaVo titaVo) throws LogicException {
		JSONArray assetsDataInfo = setLoanDto(resultList, titaVo);
		JSONObject requestJO = new JSONObject();
		try {
			requestJO.putOpt("tranSerialSeq", ""); // 傳輸交易序號
			requestJO.putOpt("tranBatchSeq", ""); // 傳輸母批次序號
			requestJO.putOpt("tranSubBatchSeq", ""); // 傳輸子批次序號
			requestJO.putOpt("tranDataSum", ""); // 傳輸資料金額加總
			requestJO.putOpt("tranDataCount", ""); // 傳輸資料總數
			requestJO.putOpt("opType", "A"); // 作業別
			requestJO.putOpt("sysSrc", "LN"); // 系統來源
			requestJO.putOpt("tranTime", ""); // 傳輸日期時間
			requestJO.putOpt("cvDate", ""); // 評價日
			requestJO.putOpt("version", "v0"); // 版本
			requestJO.putOpt("assetsCode", "A"); // 資產代號
			requestJO.putOpt("assetsDataInfo", assetsDataInfo);
		} catch (JSONException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("L7300 setLoanDto error = " + errors);
			throw new LogicException("E9004", "設定上傳資料發生錯誤");
		}
		return requestJO;
	}

	private JSONArray setLoanDto(List<Map<String, String>> dataList, TitaVo titaVo) throws LogicException {
		JSONArray assetsDataInfo = new JSONArray();
		for (Map<String, String> data : dataList) {
			JSONObject loanDto = new JSONObject();
			try {
				loanDto.put("tranDataId", ""); // 傳輸筆數序號
				loanDto.put("assetsCodeDtl", "A25"); // 資產細項代號
				loanDto.put("stockDate", ""); // 庫存日(帳務日)
				loanDto.put("maturityDate", ""); // 到期日
				loanDto.put("loanType", ""); // 貸款類別(以過往提供ICS填報作業規則進行分類，
				// 若為「企金，商業及農業抵押貸款」填入1；若為「房貸，住宅不動產抵押貸款」填入2。)
				loanDto.put("counterparty", ""); // 交易對手(公司戶填入客戶名稱，個人戶則填入代碼)
				loanDto.put("loanInt", ""); // 放款利率
				loanDto.put("ltvRatio", ""); // 貸款成數(LTV,%)
				loanDto.put("subCompanyCode", ""); // 區隔帳冊別(資金來源)
				loanDto.put("acCurrency", "NTD"); // 記帳幣
				loanDto.put("currency", "NTD"); // 交易幣別
				loanDto.put("mrktValue", ""); // 市價
				loanDto.put("bookValue", ""); // 期初帳面金額
			} catch (JSONException e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.error("L7300 setLoanDto error = " + errors);
				throw new LogicException("E9004", "設定放款傳輸資料發生錯誤");
			}
			assetsDataInfo.put(loanDto);
		}
		return assetsDataInfo;
	}

	private String post(JSONObject requestJo, TitaVo titaVo) throws LogicException {
		String jsonString = requestJo.toString();
		HttpHeaders headers = setIcsHeader();
		HttpEntity<?> request = new HttpEntity<Object>(jsonString, headers);
		RestTemplate restTemplate = new RestTemplate();
		String result = null;
		this.info("ICS request = " + request.toString());
		try {
			result = restTemplate.postForObject(apiUrl, request, String.class);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("ICS Exception = " + e.getMessage());
			throw new LogicException("E9004", "ICS上傳資料時發生錯誤");
		}
		this.info("ICS result = " + result);

		return result;
	}
	
	private HttpHeaders setIcsHeader() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8); // 指定編碼方式
		String[] icsAuthArray = icsAuth.split(":");
		headers.setBasicAuth(icsAuthArray[0], icsAuthArray[1], StandardCharsets.UTF_8); // 指定帳密編碼方式
		headers.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8)); // 指定傳送/接收資料時可接受編碼方式
		return headers;
	}
}