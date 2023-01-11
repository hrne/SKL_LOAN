package com.st1.itx.trade.L7;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.SlipMedia2022;
import com.st1.itx.db.service.AcDetailService;
import com.st1.itx.db.service.SlipMedia2022Service;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.EbsCom;

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

		lineNum = 1;
		drAmtTotal = BigDecimal.ZERO;
		journalTbl = new JSONArray();
		String dbCr;
		BigDecimal txAmt;
		String txBal;

		for (SlipMedia2022 slipMedia : listSlipMedia2022) {
			JSONObject dataJo = new JSONObject();

			slipDate = "" + slipMedia.getAcDate();
			dbCr = slipMedia.getDbCr();
			txAmt = slipMedia.getTxAmt();

			// 借方金額累加
			drAmtTotal = dbCr.equals("D") ? drAmtTotal.add(txAmt) : drAmtTotal;

			// 傳票媒體檔的金額處理,借方為正數,貸方為負數
			txBal = dbCr.equals("D") ? txAmt.toString() : txAmt.negate().toString();

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
			lineNum++;
		}

		// 統計並送出
		boolean sendEbsOK = doSummaryAndSendToEbs(titaVo);

		// 2022-05-18 ST1 Wei 新增:
		// 若 批號>=90 且 上傳EBS結果為成功時
		// 將AcDetail內 本次上傳資料 的 EntAc 更新為9
		if (sendEbsOK) {
			for (SlipMedia2022 slipMedia : listSlipMedia2022) {
				SlipMedia2022 tempSlipMedia = sSlipMedia2022Service.holdById(slipMedia, titaVo);
				if (tempSlipMedia != null) {
					tempSlipMedia.setTransferFlag("Y");
					try {
						sSlipMedia2022Service.update(tempSlipMedia, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0003", "傳票媒體檔");
					}
				}
			}
			this.batchTransaction.commit();
			if (iBatchNo >= 90) {
				// iAcDate + 19110000
				// iBatchNo
				Slice<AcDetail> slAcDetail = sAcDetailService.findSlipBatNo(iAcDate + 19110000, iBatchNo, 0,
						Integer.MAX_VALUE, titaVo);

				if (slAcDetail != null && !slAcDetail.isEmpty()) {
					List<AcDetail> lAcDetail = slAcDetail.getContent();
					AcDetail tempAcDetail;
					for (AcDetail tAcDetail : lAcDetail) {
						tempAcDetail = sAcDetailService.holdById(tAcDetail, titaVo);
						tempAcDetail.setEntAc(9);
						try {
							sAcDetailService.update(tempAcDetail, titaVo);
						} catch (DBException e) {
							throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
						}
					}
					this.batchTransaction.commit();
				}
			}
		}

		sSlipMedia2022Service.Usp_L7_SlipMedia_Upd(titaVo.getEntDyI() + 19110000, titaVo.getTlrNo(), iAcDate, iBatchNo,
				titaVo);

		this.addList(this.totaVo);
		return this.sendList();
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
