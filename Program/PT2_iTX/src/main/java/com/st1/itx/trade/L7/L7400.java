package com.st1.itx.trade.L7;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.SlipMedia2022;
import com.st1.itx.db.domain.TxToDoDetailId;
import com.st1.itx.db.service.AcDetailService;
import com.st1.itx.db.service.SlipMedia2022Service;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.StaticTool;
import com.st1.itx.util.common.EbsCom;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

/**
 * L7400 <BR>
 * 2023-01-06 新增 智偉 <BR>
 * 說明:L9130改為不傳送,只產檔產表 <BR>
 * 由本支程式傳送 <BR>
 * 
 * @author st1-ChihWei
 *
 */
@Component("L7400")
@Scope("prototype")
public class L7400 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	private AcDetailService sAcDetailService;

	/* DB服務注入 */
	@Autowired
	private SlipMedia2022Service sSlipMedia2022Service;

	@Autowired
	private EbsCom ebsCom;
	@Autowired
	public Parse parse;
	@Autowired
	private TxToDoCom txToDoCom;

	@Autowired
	private WebClient webClient;

	@Autowired
	private DateUtil dDateUtil;

	private JSONArray journalTbl;
	private BigInteger groupId;

	// 傳票日期
	private String slipDate;

	// 成本單位
	private static final String COST_UNIT = "10H000";

	// 會計準則類型
	private static final String IFRS_TYPE = "1";

	// For EBS WS P_SUMMARY_TBL.TOTAL_AMOUNT - 該批號下各幣別傳票借方總金額
	private BigDecimal drAmtTotal;

	// For EBS WS JE_LINE_NUM
	private int lineNum;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L7400 ");
		this.totaVo.init(titaVo);

		// 作業人員
		String tellerNo = titaVo.getTlrNo();

		// 會計日期 #AcDate=D,7,I
		int iAcDate = Integer.parseInt(titaVo.getParam("AcDate"));

		// 傳票批號 #BatchNo=A,2,I
		int iBatchNo = Integer.parseInt(titaVo.getParam("BatchNo"));

		// 核心傳票媒體上傳序號 #MediaSeq=A,3,I
		int iMediaSeq = Integer.parseInt(titaVo.getParam("MediaSeq"));

		this.info("L7400 iAcDate = " + iAcDate);
		this.info("L7400 iBatchNo = " + iBatchNo);
		this.info("L7400 iMediaSeq = " + iMediaSeq);

		BigDecimal tmpGroupId = new BigDecimal(iAcDate + 19110000).multiply(new BigDecimal(1000))
				.add(new BigDecimal(iMediaSeq));

		groupId = tmpGroupId.toBigInteger();

		Slice<SlipMedia2022> sSlipMedia2022 = sSlipMedia2022Service.findMediaSeq(iAcDate + 19110000, iBatchNo,
				iMediaSeq, "Y", 0, Integer.MAX_VALUE, titaVo);

		List<SlipMedia2022> listSlipMedia2022 = sSlipMedia2022 == null ? null : sSlipMedia2022.getContent();

		if (listSlipMedia2022 == null || listSlipMedia2022.isEmpty()) {
			this.info("無資料");
			// TxToDo回寫狀態
			updateTxToDo(iAcDate, iBatchNo, iMediaSeq, titaVo);
			throw new LogicException("E0001", "總帳傳票檔");
		}

		lineNum = 1;
		drAmtTotal = BigDecimal.ZERO;
		journalTbl = new JSONArray();
		boolean sendEbsOK = true;
		String dbCr;
		BigDecimal txAmt;
		String txBal;
		for (SlipMedia2022 slipMedia : listSlipMedia2022) {
			slipDate = "" + StaticTool.rocToBc(slipMedia.getAcDate()); // 資料庫拿出來是民國年,傳輸介面要西元年
			dbCr = slipMedia.getDbCr();
			txAmt = slipMedia.getTxAmt();

			// 借方金額累加
			drAmtTotal = dbCr.equals("D") ? drAmtTotal.add(txAmt) : drAmtTotal;

			// 傳票媒體檔的金額處理,借方為正數,貸方為負數
			txBal = dbCr.equals("D") ? txAmt.toString() : txAmt.negate().toString();

			putDataJo(slipMedia, tellerNo, txBal);

			lineNum++;
		}
		// 統計並送出
		sendEbsOK = doSummaryAndSendToEbs(titaVo);

		// 2022-05-18 ST1 Wei 新增:
		// 若 批號>=90 且 上傳EBS結果為成功時
		// 將AcDetail內 本次上傳資料 的 EntAc 更新為9
		if (sendEbsOK) {
			totaVo.putParam("SendStatus", "S");

			updateSlipMediaAll(listSlipMedia2022, titaVo);

			sSlipMedia2022Service.Usp_L7_SlipMedia_Upd(titaVo.getEntDyI() + 19110000, titaVo.getTlrNo(), iAcDate,
					iBatchNo, titaVo);

			// TxToDo回寫狀態
			updateTxToDo(iAcDate, iBatchNo, iMediaSeq, titaVo);
		} else {
			totaVo.putParam("SendStatus", "F");
			List<Map<String, String>> errorList = ebsCom.getErrorList();
			for (Map<String, String> error : errorList) {
				OccursList occurslist = new OccursList();
				occurslist.putParam("GroupId", error.get("GroupId"));
				occurslist.putParam("JournalName", error.get("JournalName"));
				occurslist.putParam("JeLineNum", error.get("JeLineNum"));
				occurslist.putParam("ErrorCode", error.get("ErrorCode"));
				occurslist.putParam("ErrorMessage", error.get("ErrorMessage"));
				this.totaVo.addOccursList(occurslist);
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void putDataJo(SlipMedia2022 slipMedia, String tellerNo, String txBal) throws LogicException {
		JSONObject dataJo = new JSONObject();
		try {
			dataJo.put("GROUP_ID", groupId);
			dataJo.put("BATCH_DATE", slipDate);
			dataJo.put("JE_SOURCE_NAME", "LN");
			dataJo.put("USER_LEDGER_CODE", slipMedia.getAcBookCode());
			dataJo.put("CONVENTION", IFRS_TYPE);
			dataJo.put("JOURNAL_NAME", slipMedia.getMediaSlipNo());
			dataJo.put("CURRENCY_CODE", slipMedia.getCurrencyCode());
			dataJo.put("ISSUED_BY", tellerNo);
			dataJo.put("ACCOUNTING_DATE", slipDate);
			dataJo.put("JE_LINE_NUM", "" + lineNum);
			dataJo.put("SEGREGATE_CODE", slipMedia.getAcSubBookCode());
			dataJo.put("ACCOUNT_CODE", slipMedia.getAcNoCode());
			dataJo.put("SUBACCOUNT_CODE",
					slipMedia.getAcSubCode().trim().isEmpty() ? "00000" : slipMedia.getAcSubCode());
			dataJo.put("COSTCENTER_CODE", COST_UNIT);
			dataJo.put("CHANNEL_CODE", "00"); // 通路代號
			dataJo.put("IFRS17_GROUP_CODE", "000000000000000"); // IFRS17群組代號，若無IFRS17群組代號需放000000000000000
			dataJo.put("INTERCOMPANY_CODE", "999");
			dataJo.put("DEPARTMENT_CODE", slipMedia.getDeptCode());
			dataJo.put("ENTERED_AMOUNT", txBal);
			dataJo.put("LINE_DESC", slipMedia.getSlipRmk());
			dataJo.put("WRITE_OFF_CODE", slipMedia.getReceiveCode());
			dataJo.put("RELATIONSHIP", "");
		} catch (JSONException e) {
			this.error("L7400 error = " + e.getMessage());
		}
		journalTbl.put(dataJo);
	}

	private void updateSlipMediaAll(List<SlipMedia2022> listSlipMedia2022, TitaVo titaVo) throws LogicException {
		for (SlipMedia2022 slipMedia : listSlipMedia2022) {
			slipMedia.setTransferFlag("Y");
		}
		try {
			sSlipMedia2022Service.updateAll(listSlipMedia2022, titaVo);
		} catch (DBException e) {
			throw new LogicException("E0003", "傳票媒體檔");
		}
	}

	private void updateTxToDo(int iAcDate, int iBatchNo, int iMediaSeq, TitaVo titaVo) throws LogicException {
		TxToDoDetailId tTxToDoDetailId = new TxToDoDetailId();
		tTxToDoDetailId.setCustNo(0);
		tTxToDoDetailId.setFacmNo(0);
		tTxToDoDetailId.setBormNo(0);
		tTxToDoDetailId.setDtlValue(iAcDate + parse.IntegerToString(iBatchNo, 2) + parse.IntegerToString(iMediaSeq, 3));
		tTxToDoDetailId.setItemCode("L7400");
		txToDoCom.setTxBuffer(txBuffer);
		txToDoCom.updDetailStatus(2, tTxToDoDetailId, titaVo);
	}

	private boolean doSummaryAndSendToEbs(TitaVo titaVo) throws LogicException {
		JSONArray summaryTbl = new JSONArray();
		JSONObject summaryMap = new JSONObject();
		try {
			summaryMap.put("GROUP_ID", groupId);
			summaryMap.put("BATCH_DATE", slipDate);
			summaryMap.put("JE_SOURCE_NAME", "LN");
			summaryMap.put("TOTAL_LINES", lineNum - 1);
			summaryMap.put("CURRENCY_CODE", "NTD");
			summaryMap.put("TOTAL_AMOUNT", drAmtTotal);
		} catch (JSONException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("L7400 Exception = " + e.getMessage());
			throw new LogicException("CE000", "彙總上傳資料時有誤");
		}

		summaryTbl.put(summaryMap);

		boolean sendEbsOK = ebsCom.sendSlipMediaToEbs(summaryTbl, journalTbl, titaVo);

		drAmtTotal = BigDecimal.ZERO;
		journalTbl = new JSONArray();

		return sendEbsOK;
	}
}
