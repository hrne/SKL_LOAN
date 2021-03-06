package com.st1.itx.trade.L9;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
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
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.SlipMedia2022;
import com.st1.itx.db.domain.SlipMedia2022Id;
import com.st1.itx.db.service.AcCloseService;
import com.st1.itx.db.service.AcDetailService;
import com.st1.itx.db.service.SlipMedia2022Service;
import com.st1.itx.db.service.springjpa.cm.L9130ServiceImpl;
import com.st1.itx.util.common.EbsCom;
import com.st1.itx.util.common.GSeqCom;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.format.FormatUtil;

/**
 * 產製L9130Report2022 <BR>
 * 2022年啟用新傳票格式 <BR>
 * <BR>
 * 2021-06-22 新增 智偉 <BR>
 * 說明:根據"ac034m 其他介面上傳格式_20210408.xlsx"建立傳票 <BR>
 * 2021-07-12 修改 智偉 <BR>
 * 說明:傳票媒體檔以RESTful協定傳遞至EBS會計資訊系統 <BR>
 * 2022-02-09 修改 智偉 <BR>
 * 說明:From 珮琪 新傳票媒體檔也要作應收調撥款特殊處理<BR>
 * 2022-02-10 修改 智偉 <BR>
 * 說明:修改應收調撥款的特殊處理 <BR>
 * 2022-02-22 修改 智偉 <BR>
 * 說明:相同上傳核心序號(MediaSeq)下， <BR>
 * 有不同區隔帳冊(AcSubBookCode)時， <BR>
 * 需產生不同傳票號碼(MediaSlipNo)。 <BR>
 * 
 * @author st1-ChihWei
 *
 */
@Component("L9130Report2022")
@Scope("prototype")
public class L9130Report2022 extends MakeReport {

	/* DB服務注入 */
	@Autowired
	private AcCloseService sAcCloseService;

	@Autowired
	private AcDetailService sAcDetailService;

	/* DB服務注入 */
	@Autowired
	private SlipMedia2022Service sSlipMedia2022Service;

	@Autowired
	private MakeFile makeFile;

	@Autowired
	private L9130ServiceImpl l9130ServiceImpl;

	@Autowired
	private EbsCom ebsCom;

	/* 自動取號 */
	@Autowired
	GSeqCom gGSeqCom;

	JSONArray journalTbl;
	BigInteger groupId;

	// 會計日期 #AcDate=D,7,I
	int iAcDate;

	// 傳票批號 #BatchNo=A,2,I
	int iBatchNo;

	// 核心傳票媒體上傳序號 #MediaSeq=A,3,I
	int iMediaSeq;

	String slipNo;

	// 帳冊別
	String acBookCode = "";

	// 帳冊別中文
	String acBookItem = "";

	// 科目中文
	String acNoItem = "";

	// 借貸別
	String dbCr = "";

	// 交易金額
	BigDecimal txAmt;

	// 傳票日期
	String slipDate;

	// 科目代號
	String acNoCode = "";

	// 子目代號
	String acSubNoCode = "";

	// 金額
	String txBal = "";

	// 傳票摘要
	String slipRmk = " ";

	// 會計科目銷帳碼
	String acReceivableCode = " ";

	// 成本月份
	String costMonth = " ";

	// 保單號碼
	String insuNo = " ";

	// 業務員代號
	String empNo = " ";

	// 薪碼
	String salaryCode = " ";

	// 幣別
	String currencyCode = "NTD";

	// 區隔帳冊
	String acSubBookCode = " ";

	// 部門代號
	String deptCode = "10H000";

	// 業務員代號
	String salesmanCode = " ";

	// 成本單位
	String costUnit = "10H000";

	// 通路別
	String salesChannelType = " ";

	// 會計準則類型
	String ifrsType = "1";

	// 關係人ID
	String relationId = " ";

	// 關聯方代號
	String relateCode = " ";

	// IFRS17群組
	String ifrs17Group = " ";

	String lastAcSubBookCode;

	// 應收調撥款金額
	BigDecimal transferAmt;

	// For EBS WS P_SUMMARY_TBL.TOTAL_AMOUNT - 該批號下各幣別傳票借方總金額
	BigDecimal drAmtTotal;

	// 作業人員
	String tellerNo = "";

	// For EBS WS JE_LINE_NUM
	int lineNum;

	public void exec(TitaVo titaVo) throws LogicException {
		this.info("L9130Report2022 exec ...");

		// 作業人員
		tellerNo = titaVo.getTlrNo();

		// 會計日期 #AcDate=D,7,I
		iAcDate = Integer.parseInt(titaVo.getParam("AcDate"));

		// 傳票批號 #BatchNo=A,2,I
		iBatchNo = Integer.parseInt(titaVo.getParam("BatchNo"));

		// 核心傳票媒體上傳序號 #MediaSeq=A,3,I
		iMediaSeq = Integer.parseInt(titaVo.getParam("MediaSeq"));

		// 取得傳票號碼
		slipNo = getSlipNo(iAcDate, titaVo);

		this.info("L9130Report2022 iAcDate = " + iAcDate);
		this.info("L9130Report2022 iBatchNo = " + iBatchNo);
		this.info("L9130Report2022 iMediaSeq = " + iMediaSeq);
		this.info("L9130Report2022 slipNo = " + slipNo);

		BigDecimal tmpGroupId = new BigDecimal(iAcDate + 19110000).multiply(new BigDecimal(1000))
				.add(new BigDecimal(iMediaSeq));

		groupId = tmpGroupId.toBigInteger();

		// brno 單位
		String brno = titaVo.getBrno();
		// date 檔案日期
		int date = iAcDate + 19110000;
		// no 檔案編號
		String no = "L9130";
		// desc 檔案說明
		String desc = "總帳傳票媒體檔_" + FormatUtil.pad9("" + iMediaSeq, 3);

		// 檔名編碼方式
		// 固定值 核心傳票媒體上傳序號
		// jori 999

		// name 輸出檔案名稱
		String name = "總帳傳票媒體檔_jori" + FormatUtil.pad9("" + iMediaSeq, 3) + ".csv";

		// format 輸出檔案格式 1.UTF8 2.BIG5
		int format = 2;

		makeFile.open(titaVo, date, brno, no, desc, name, format);

		Slice<SlipMedia2022> sSlipMedia2022 = sSlipMedia2022Service.findMediaSeq(iAcDate + 19110000, iBatchNo,
				iMediaSeq, "Y", 0, Integer.MAX_VALUE, titaVo);

		SlipMedia2022 tempTableSlipMedia2022;

		// 若已存在,是重新製作傳票媒體
		if (sSlipMedia2022 != null && !sSlipMedia2022.isEmpty()) {
			// 更新原傳票媒體的"是否為最新"(LatestFlag)欄位
			for (SlipMedia2022 tSlipMedia2022 : sSlipMedia2022.getContent()) {
				SlipMedia2022Id pkSlipMedia2022Id = tSlipMedia2022.getSlipMedia2022Id();
				tempTableSlipMedia2022 = sSlipMedia2022Service.holdById(pkSlipMedia2022Id, titaVo);
				if (tempTableSlipMedia2022 != null) {
					tempTableSlipMedia2022.setLatestFlag("N");
					try {
						sSlipMedia2022Service.update(tempTableSlipMedia2022, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0003", "傳票媒體檔");
					}
				}
			}
		}

		List<Map<String, String>> l9130List = null;

		try {
			l9130List = l9130ServiceImpl.doQuery2022(iAcDate + 19110000, iBatchNo, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("l9130ServiceImpl.doQuery error = " + e.getMessage());
		}

		int i = 1;
		lineNum = 1;

		lastAcSubBookCode = "00A";

		transferAmt = BigDecimal.ZERO;

		drAmtTotal = BigDecimal.ZERO;

		journalTbl = new JSONArray();

		for (Map<String, String> l9130 : l9130List) {

			acSubBookCode = l9130.get("F6"); // 區隔帳冊

			// 此筆區隔帳冊與上一筆不同
			if (!acSubBookCode.equals(lastAcSubBookCode)) {
				// 特殊處理:同區隔帳冊借貸金額加總後，寫一筆反向10340000000 應收調撥款
				specialHandling(i, titaVo);
				// 特殊處理結束

				// 取新的傳票號碼
				slipNo = getSlipNo(iAcDate, titaVo);

				i = 1;
			}

			acBookCode = l9130.get("F0"); // 帳冊別
			slipDate = l9130.get("F1"); // 會計日期
			acNoCode = l9130.get("F2"); // 科目代號
			acSubNoCode = l9130.get("F3"); // 子目代號
			dbCr = l9130.get("F4"); // 借貸別
			txAmt = l9130.get("F5") == null ? BigDecimal.ZERO : new BigDecimal(l9130.get("F5")); // 金額
			slipRmk = l9130.get("F7"); // 科目名稱
			acReceivableCode = l9130.get("F8"); // 銷帳碼

			// 借方金額累加
			drAmtTotal = dbCr.equals("D") ? drAmtTotal.add(txAmt) : drAmtTotal;

			// 傳票媒體檔的金額處理,借方為正數,貸方為負數
			txBal = dbCr.equals("D") ? txAmt.toString() : txAmt.negate().toString();

			JSONObject dataJo = new JSONObject();

			try {
				dataJo.put("GROUP_ID", groupId);
				dataJo.put("BATCH_DATE", slipDate);
				dataJo.put("JE_SOURCE_NAME", "LN");
				dataJo.put("USER_LEDGER_CODE", acBookCode);
				dataJo.put("CONVENTION", ifrsType);
				dataJo.put("JOURNAL_NAME", slipNo);
				dataJo.put("CURRENCY_CODE", currencyCode);
				dataJo.put("ISSUED_BY", tellerNo);
				dataJo.put("ACCOUNTING_DATE", slipDate);
				dataJo.put("JE_LINE_NUM", "" + lineNum);
				dataJo.put("SEGREGATE_CODE", acSubBookCode);
				dataJo.put("ACCOUNT_CODE", acNoCode);
				dataJo.put("SUBACCOUNT_CODE", acSubNoCode.trim().isEmpty() ? "00000" : acSubNoCode);
				dataJo.put("COSTCENTER_CODE", costUnit);
				dataJo.put("CHANNEL_CODE", "00"); // 通路代號
				dataJo.put("IFRS17_GROUP_CODE", "000000000000000"); // IFRS17群組代號，若無IFRS17群組代號需放000000000000000
				dataJo.put("INTERCOMPANY_CODE", "999");
				dataJo.put("DEPARTMENT_CODE", deptCode);
				dataJo.put("ENTERED_AMOUNT", txBal);
				dataJo.put("LINE_DESC", slipRmk);
				dataJo.put("WRITE_OFF_CODE", acReceivableCode);
				dataJo.put("RELATIONSHIP", "");
			} catch (JSONException e) {
				this.error("L9130Report22 error = " + e.getMessage());
			}

			journalTbl.put(dataJo);

			String data = "";

			data += acBookCode + ","; // 帳冊別
			data += slipNo + ","; // 傳票號碼
			data += i + ","; // 傳票明細序號
			data += slipDate + ","; // 傳票日期
			data += acNoCode + ","; // 科目代號
			data += acSubNoCode + ","; // 子目代號
			data += deptCode + ","; // 部門代號
			data += txBal + ","; // 金額
			data += slipRmk + ","; // 傳票摘要
			data += acReceivableCode + ","; // 會計科目銷帳碼
			data += costMonth + ","; // 成本月份
			data += insuNo + ","; // 保單號碼
			data += salesmanCode + ","; // 業務員代號
			data += salaryCode + ","; // 薪碼
			data += currencyCode + ","; // 幣別
			data += acSubBookCode + ","; // 區隔帳冊
			data += costUnit + ","; // 成本單位
			data += salesChannelType + ","; // 通路別
			data += ifrsType + ","; // 會計準則類型
			data += relationId + ","; // 關係人ID
			data += relateCode + ","; // 關聯方代號
			data += ifrs17Group; // IFRS17群組

			// 寫入一筆到報表檔
			makeFile.put(data);

			// 寫入一筆到SlipMedia2022
			SlipMedia2022 tSlipMedia2022 = new SlipMedia2022();

			SlipMedia2022Id tSlipMedia2022Id = new SlipMedia2022Id();

			tSlipMedia2022Id.setMediaSlipNo(slipNo);
			tSlipMedia2022Id.setSeq(i);

			tSlipMedia2022.setSlipMedia2022Id(tSlipMedia2022Id);

			tSlipMedia2022.setAcDate(iAcDate);
			tSlipMedia2022.setBatchNo(iBatchNo);
			tSlipMedia2022.setMediaSeq(iMediaSeq);
			tSlipMedia2022.setMediaSlipNo(slipNo);
			tSlipMedia2022.setAcBookCode(acBookCode);
			tSlipMedia2022.setSeq(i);
			tSlipMedia2022.setAcNoCode(acNoCode);
			tSlipMedia2022.setAcSubCode(acSubNoCode);
			tSlipMedia2022.setCurrencyCode(currencyCode);
			tSlipMedia2022.setAcSubBookCode(acSubBookCode);// 區隔帳冊
			tSlipMedia2022.setDbCr(dbCr);
			tSlipMedia2022.setTxAmt(txAmt);
			tSlipMedia2022.setReceiveCode(acReceivableCode);
			tSlipMedia2022.setSlipRmk(slipRmk);
			tSlipMedia2022.setCostMonth(costMonth);
			tSlipMedia2022.setDeptCode(deptCode);
			tSlipMedia2022.setLatestFlag("Y");

			try {
				sSlipMedia2022Service.insert(tSlipMedia2022, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "傳票媒體檔");
			}

			// 特殊處理:同區隔帳冊借貸金額加總後，寫一筆反向10340000000 應收調撥款
			// 計算借貸方金額加總
			if (dbCr.equals("D")) {
				transferAmt = transferAmt.add(txAmt);
			} else {
				transferAmt = transferAmt.subtract(txAmt);
			}
			// 特殊處理結束

			lastAcSubBookCode = acSubBookCode;

			i++;
			lineNum++;
		}

		// 全部傳票印完，執行特殊處理
		specialHandling(i, titaVo);

		// 統計並送出
		boolean sendEbsOK = doSummaryAndSendToEbs(titaVo);

		// 寫產檔記錄到TxFile
		long fileno = makeFile.close();

		// 2022-05-18 ST1 Wei 新增:
		// 若 批號>=90 且 上傳EBS結果為成功時
		// 將AcDetail內 本次上傳資料 的 EntAc 更新為9
		if (sendEbsOK && iBatchNo >= 90) {
			// iAcDate + 19110000
			// iBatchNo
			Slice<AcDetail> slAcDetail = sAcDetailService.findSlipBatNo(iAcDate + 19110000, iBatchNo, 0,
					Integer.MAX_VALUE, titaVo);

			if (slAcDetail == null || slAcDetail.isEmpty()) {
				return;
			} else {
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
			}
		}

		this.info("makeFile close fileno = " + fileno);
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
			this.error("L9130Report22 Exception = " + e.getMessage());
			throw new LogicException("CE000", "彙總上傳資料時有誤");
		}

		summaryTbl.put(summaryMap);

//        "GROUP_ID": 20200615002,
//        "BATCH_DATE": "20200615",
//        "JE_SOURCE_NAME": "PL",
//        "TOTAL_LINES": 2,
//        "CURRENCY_CODE": "NTD",
//        "TOTAL_AMOUNT": 12450

		boolean sendEbsOK = ebsCom.sendSlipMediaToEbs(summaryTbl, journalTbl, titaVo);

		drAmtTotal = BigDecimal.ZERO;
		journalTbl = new JSONArray();

		return sendEbsOK;
	}

	/**
	 * 組傳票編號<BR>
	 * 固定值 民國年 月份 日期 3碼序號<BR>
	 * // F10 + YYY + M + DD + 999
	 * 
	 * @param acDate 國曆會計日期
	 * @param titaVo titaVo
	 * @return 傳票號碼
	 */
	private String getSlipNo(int acDate, TitaVo titaVo) {

		int slipNoSeq = 0;

		try {
			slipNoSeq = gGSeqCom.getSeqNo(acDate, 3, "L2", "SLIP", 999, titaVo);
		} catch (LogicException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("L9130Report2022 gGSeqCom getSeqNo error = " + e.getMessage());
		}

		// 傳票編號 "F"+year+month(123456789ABC)+day+3碼序號
		String tmpSlipNo = "";

		String sAcDate = FormatUtil.pad9(String.valueOf(acDate), 7);

		String year = sAcDate.substring(0, 3);

		String month = sAcDate.substring(3, 5);

		String day = sAcDate.substring(5, 7);

		// 固定值 民國年 月份 日期 3碼序號
		// F10 + YYY + M + DD + 999

		// 月份特殊處理
		// 1 2 3 4 5 6 7 8 9 10 11 12
		// 1 2 3 4 5 6 7 8 9 A B C
		int iMonth = Integer.parseInt(month);

		if (iMonth > 9) {
			switch (iMonth) {
			case 10:
				month = "A";
				break;
			case 11:
				month = "B";
				break;
			case 12:
				month = "C";
				break;
			default:
				this.error("L9130Report2022 getSlipNo month > 12.");
				break;
			}
		} else {
			month = String.valueOf(iMonth);
		}

		String sSlipNoSeq = FormatUtil.pad9(String.valueOf(slipNoSeq), 3);

		tmpSlipNo = "F10" + year + month + day + sSlipNoSeq;

		this.info("L9130Report2022 getSlipNo slipNo : " + tmpSlipNo);
		return tmpSlipNo;
	}

	/**
	 * 特殊處理:同區隔帳冊借貸金額加總後，寫一筆反向10340000000 應收調撥款
	 * 
	 * @param i      印到第i筆
	 * @param titaVo titaVo
	 * @return 處理後的新筆數
	 * @throws LogicException LogicException
	 */
	private void specialHandling(int i, TitaVo titaVo) throws LogicException {

		this.info("L9130 specialHandling i = " + i);
		this.info("L9130 specialHandling lastAcSubBookCode = " + lastAcSubBookCode);
		this.info("L9130 specialHandling transferAmt = " + transferAmt);

		if (transferAmt.compareTo(BigDecimal.ZERO) != 0) {

			// 於借貸方各寫一筆10340000000 應收調撥款
			String tempAcNoCode = "10340000000";
			String tempAcSubNoCode = "     ";
			String slipDateROC = String.valueOf(Integer.parseInt(slipDate) - 19110000);
			String tempAcReceivableCode = deptCode + slipDateROC + FormatUtil.pad9(String.valueOf(iBatchNo), 2);
			this.info("tempAcReceivableCode = " + tempAcReceivableCode);
			String tempSlipRmk = "應收調撥款";

			JSONObject dataJo = new JSONObject();

			try {
				dataJo.put("GROUP_ID", groupId);
				dataJo.put("BATCH_DATE", slipDate);
				dataJo.put("JE_SOURCE_NAME", "LN");
				dataJo.put("USER_LEDGER_CODE", acBookCode);
				dataJo.put("CONVENTION", ifrsType);
				dataJo.put("JOURNAL_NAME", slipNo);
				dataJo.put("CURRENCY_CODE", currencyCode);
				dataJo.put("ISSUED_BY", tellerNo);
				dataJo.put("ACCOUNTING_DATE", slipDate);
				dataJo.put("JE_LINE_NUM", "" + lineNum);
				dataJo.put("SEGREGATE_CODE", lastAcSubBookCode);
				dataJo.put("ACCOUNT_CODE", tempAcNoCode);
				dataJo.put("SUBACCOUNT_CODE", tempAcSubNoCode.trim().isEmpty() ? "00000" : tempAcSubNoCode);
				dataJo.put("COSTCENTER_CODE", costUnit);
				dataJo.put("CHANNEL_CODE", "00"); // 通路代號
				dataJo.put("IFRS17_GROUP_CODE", "000000000000000"); // IFRS17群組代號，若無IFRS17群組代號需放000000000000000
				dataJo.put("INTERCOMPANY_CODE", "999"); // 關聯方代號
				dataJo.put("DEPARTMENT_CODE", deptCode);
				dataJo.put("ENTERED_AMOUNT", transferAmt.negate()); // 寫一筆反向
				dataJo.put("LINE_DESC", tempSlipRmk);
				dataJo.put("WRITE_OFF_CODE", tempAcReceivableCode);
				dataJo.put("RELATIONSHIP", "");
			} catch (JSONException e) {
				this.error("L9130Report22 error = " + e.getMessage());
			}

			journalTbl.put(dataJo);

			String data = "";

			data += acBookCode + ","; // 帳冊別
			data += slipNo + ","; // 傳票號碼
			data += i + ","; // 傳票明細序號
			data += slipDate + ","; // 傳票日期
			data += tempAcNoCode + ","; // 科目代號
			data += tempAcSubNoCode + ","; // 子目代號
			data += deptCode + ","; // 部門代號
			// 傳票媒體檔的金額處理,借方為正數,貸方為負數
			// 這裡無論如何，反向即可
			data += transferAmt.negate().toString() + ","; // 金額
			data += tempSlipRmk + ","; // 傳票摘要
			data += tempAcReceivableCode + ","; // 會計科目銷帳碼
			data += costMonth + ","; // 成本月份
			data += insuNo + ","; // 保單號碼
			data += salesmanCode + ","; // 業務員代號
			data += salaryCode + ","; // 薪碼
			data += currencyCode + ","; // 幣別
			data += lastAcSubBookCode + ","; // 區隔帳冊
			data += costUnit + ","; // 成本單位
			data += salesChannelType + ","; // 通路別
			data += ifrsType + ","; // 會計準則類型
			data += relationId + ","; // 關係人ID
			data += relateCode + ","; // 關聯方代號
			data += ifrs17Group; // IFRS17群組

			// 寫入一筆到報表檔
			makeFile.put(data);

			// 寫入一筆到SlipMedia2022
			SlipMedia2022 tSlipMedia2022 = new SlipMedia2022();

			SlipMedia2022Id tSlipMedia2022Id = new SlipMedia2022Id();

			tSlipMedia2022Id.setMediaSlipNo(slipNo);
			tSlipMedia2022Id.setSeq(i);

			tSlipMedia2022.setSlipMedia2022Id(tSlipMedia2022Id);

			tSlipMedia2022.setAcDate(iAcDate);
			tSlipMedia2022.setBatchNo(iBatchNo);
			tSlipMedia2022.setMediaSeq(iMediaSeq);
			tSlipMedia2022.setMediaSlipNo(slipNo);
			tSlipMedia2022.setAcBookCode(acBookCode);
			tSlipMedia2022.setSeq(i);
			tSlipMedia2022.setAcNoCode(tempAcNoCode);
			tSlipMedia2022.setAcSubCode(tempAcSubNoCode);
			tSlipMedia2022.setCurrencyCode(currencyCode);
			tSlipMedia2022.setAcSubBookCode(lastAcSubBookCode);// 區隔帳冊
			// 如果為正數要寫一筆貸方
			// 如果為負數要寫一筆借方
			tSlipMedia2022.setDbCr(transferAmt.compareTo(BigDecimal.ZERO) > 0 ? "C" : "D");
			tSlipMedia2022.setTxAmt(transferAmt.abs());
			tSlipMedia2022.setReceiveCode(tempAcReceivableCode);
			tSlipMedia2022.setSlipRmk(tempSlipRmk);
			tSlipMedia2022.setCostMonth(costMonth);
			tSlipMedia2022.setDeptCode(deptCode);
			tSlipMedia2022.setLatestFlag("Y");

			try {
				sSlipMedia2022Service.insert(tSlipMedia2022, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "傳票媒體檔");
			}

			i++;
			lineNum++;
		}

		// 計算借方加總
		drAmtTotal = transferAmt.compareTo(BigDecimal.ZERO) > 0 ? drAmtTotal : drAmtTotal.add(transferAmt.abs());

		// 此區隔帳冊的印完，歸零
		transferAmt = BigDecimal.ZERO;
		// 特殊處理結束
	}
}
