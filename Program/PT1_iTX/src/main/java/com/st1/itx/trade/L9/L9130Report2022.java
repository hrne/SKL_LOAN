package com.st1.itx.trade.L9;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.st1.itx.util.common.GSeqCom;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
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
 * 2023-01-06 修改 智偉 <BR>
 * 說明:本支程式改為不傳送,只產檔產表 <BR>
 * 由新增程式L7400傳送 <BR>
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
	private L9130ServiceImpl l9130ServiceImpl;

	@Autowired
	private MakeExcel makeExcel;

	/* 自動取號 */
	@Autowired
	private GSeqCom gGSeqCom;

	private BigInteger groupId;

	// 會計日期 #AcDate=D,7,I
	private int iAcDate;

	// 傳票批號 #BatchNo=A,2,I
	private int iBatchNo;

	// 核心傳票媒體上傳序號 #MediaSeq=A,3,I
	private int iMediaSeq;

	private String slipNo;

	// 帳冊別
	private String acBookCode = "";

	// 帳冊別中文
	private String acBookItem = "";

	// 科目中文
	private String acNoItem = "";

	// 借貸別
	private String dbCr = "";

	// 交易金額
	private BigDecimal txAmt;

	// 傳票日期
	private String slipDate;

	// 科目代號
	private String acNoCode = "";

	// 子目代號
	private String acSubNoCode = "";

	// 金額
	private String txBal = "";

	// 傳票摘要
	private String slipRmk = " ";

	// 會計科目銷帳碼
	private String acReceivableCode = " ";

	// 成本月份
	private String costMonth = " ";

	// 保單號碼
	private String insuNo = " ";

	// 業務員代號
	private String empNo = " ";

	// 薪碼
	private String salaryCode = " ";

	// 幣別
	private String currencyCode = "NTD";

	// 區隔帳冊
	private String acSubBookCode = " ";

	// 部門代號
	private String deptCode = "10H000";

	// 業務員代號
	private String salesmanCode = " ";

	// 成本單位
	private String costUnit = "10H000";

	// 通路別
	private String salesChannelType = " ";

	// 會計準則類型
	private String ifrsType = "1";

	// 關係人ID
	private String relationId = " ";

	// 關聯方代號
	private String relateCode = " ";

	// IFRS17群組
	private String ifrs17Group = " ";

	private String lastAcSubBookCode;

	// 應收調撥款金額
	private BigDecimal transferAmt;

	// For EBS WS P_SUMMARY_TBL.TOTAL_AMOUNT - 該批號下各幣別傳票借方總金額
	private BigDecimal drAmtTotal;

	// 作業人員
	private String tellerNo = "";

	// For EBS WS JE_LINE_NUM
	private int lineNum;

	private int rowCursor = 1;

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

		Slice<SlipMedia2022> sSlipMedia2022 = sSlipMedia2022Service.findBatchNo(iAcDate + 19110000, iBatchNo, 0,
				Integer.MAX_VALUE, titaVo);

		SlipMedia2022 tempTableSlipMedia2022;
		List<SlipMedia2022> lSlipMedia2022old = new ArrayList<SlipMedia2022>();

		// 若已存在,是重新製作傳票媒體
		if (sSlipMedia2022 != null && !sSlipMedia2022.isEmpty()) {
			lSlipMedia2022old = sSlipMedia2022.getContent();
			// 更新原傳票媒體的"是否為最新"(LatestFlag)欄位
			for (SlipMedia2022 tSlipMedia2022 : lSlipMedia2022old) {
				tSlipMedia2022.setLatestFlag("N");
			}
			try {
				sSlipMedia2022Service.updateAll(lSlipMedia2022old, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0003", "傳票媒體檔");
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

		ReportVo reportVo = ReportVo.builder().setRptDate(iAcDate + 19110000).setBrno(titaVo.getKinbr())
				.setRptCode("L9130").setRptItem("總帳傳票資料_" + iBatchNo).build();

		makeExcel.open(titaVo, reportVo, "總帳傳票資料_" + iBatchNo, "L9130_底稿_總帳傳票資料.xlsx", "明細");

		rowCursor = 3;

		List<SlipMedia2022> lSlipMedia2022 = new ArrayList<SlipMedia2022>();
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
			tSlipMedia2022.setTransferFlag("N");
			lSlipMedia2022.add(tSlipMedia2022);
			try {
				sSlipMedia2022Service.insert(tSlipMedia2022, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "傳票媒體檔");
			}

			// 入Excel
			makeExcel.setValue(rowCursor, 1, groupId); // GROUP_ID
			makeExcel.setValue(rowCursor, 2, slipDate); // BATCH_DATE
			makeExcel.setValue(rowCursor, 3, "LN"); // JE_SOURCE_NAME
			makeExcel.setValue(rowCursor, 4, acBookCode); // USER_LEDGER_CODE
			makeExcel.setValue(rowCursor, 5, "1"); // CONVENTION
			makeExcel.setValue(rowCursor, 6, slipNo); // JOURNAL_NAME
			makeExcel.setValue(rowCursor, 7, currencyCode); // CURRENCY_CODE
			makeExcel.setValue(rowCursor, 8, ""); // ISSUED_BY
			makeExcel.setValue(rowCursor, 9, slipDate); // ACCOUNTING_DATE
			makeExcel.setValue(rowCursor, 10, lineNum); // JE_LINE_NUM
			makeExcel.setValue(rowCursor, 11, acSubBookCode); // SEGREGATE_CODE
			makeExcel.setValue(rowCursor, 12, acNoCode); // ACCOUNT_CODE
			makeExcel.setValue(rowCursor, 13, acSubNoCode.trim().isEmpty() ? "00000" : acSubNoCode); // SUBACCOUNT_CODE
			makeExcel.setValue(rowCursor, 14, "10H000"); // COSTCENTER_CODE
			makeExcel.setValue(rowCursor, 15, "00"); // CHANNEL_CODE
			makeExcel.setValue(rowCursor, 16, "000000000000000"); // IFRS17_GROUP_CODE
			makeExcel.setValue(rowCursor, 17, "999"); // INTERCOMPANY_CODE
			makeExcel.setValue(rowCursor, 18, deptCode); // DEPARTMENT_CODE
			makeExcel.setValue(rowCursor, 19, txBal); // ENTERED_AMOUNT
			makeExcel.setValue(rowCursor, 20, slipRmk); // LINE_DESC
			makeExcel.setValue(rowCursor, 21, acReceivableCode); // WRITE_OFF_CODE
			makeExcel.setValue(rowCursor, 22, ""); // RELATIONSHIP

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
			rowCursor++;
		}

		// 全部傳票印完，執行特殊處理
		specialHandling(i, titaVo);

		// 寫產檔記錄到TxFile
		long excelNo = makeExcel.close();

		makeExcel.toExcel(excelNo);

		// 2022-05-18 ST1 Wei 新增:
		// 若 批號>=90 且 上傳EBS結果為成功時
		// 將AcDetail內 本次上傳資料 的 EntAc 更新為9
		// iAcDate + 19110000
		// iBatchNo
		Slice<AcDetail> slAcDetail = sAcDetailService.findSlipBatNo(iAcDate + 19110000, iBatchNo, 0, Integer.MAX_VALUE,
				titaVo);

		if (slAcDetail == null || slAcDetail.isEmpty()) {
			return;
		} else {
			List<AcDetail> lAcDetail = slAcDetail.getContent();
			for (AcDetail ac : lAcDetail) {
				if (iBatchNo >= 90) {
					ac.setEntAc(9);
				}
				// 媒體傳票號碼
				for (SlipMedia2022 md : lSlipMedia2022) {
					String receivableCode = " ";
					if (ac.getReceivableFlag() == 8) {
						receivableCode = ac.getRvNo();
					}
					if (ac.getAcBookCode().equals(md.getAcBookCode())
							&& ac.getAcSubBookCode().equals(md.getAcSubBookCode())
							&& ac.getAcNoCode().equals(md.getAcNoCode()) && ac.getAcSubCode().equals(md.getAcSubCode())
							&& ac.getDbCr().equals(md.getDbCr()) && receivableCode.equals(md.getReceiveCode())) {
						this.info("成功 >" + md.getMediaSlipNo());
						ac.setMediaSlipNo(md.getMediaSlipNo());
					}
				}
			}
			try {
				sAcDetailService.updateAll(lAcDetail, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
			}
		}

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
			tSlipMedia2022.setTransferFlag("N");

			try {
				sSlipMedia2022Service.insert(tSlipMedia2022, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "傳票媒體檔");
			}
			// 入Excel
			makeExcel.setValue(rowCursor, 1, groupId); // GROUP_ID
			makeExcel.setValue(rowCursor, 2, slipDate); // BATCH_DATE
			makeExcel.setValue(rowCursor, 3, "LN"); // JE_SOURCE_NAME
			makeExcel.setValue(rowCursor, 4, acBookCode); // USER_LEDGER_CODE
			makeExcel.setValue(rowCursor, 5, "1"); // CONVENTION
			makeExcel.setValue(rowCursor, 6, slipNo); // JOURNAL_NAME
			makeExcel.setValue(rowCursor, 7, currencyCode); // CURRENCY_CODE
			makeExcel.setValue(rowCursor, 8, ""); // ISSUED_BY
			makeExcel.setValue(rowCursor, 9, slipDate); // ACCOUNTING_DATE
			makeExcel.setValue(rowCursor, 10, lineNum); // JE_LINE_NUM
			makeExcel.setValue(rowCursor, 11, lastAcSubBookCode); // SEGREGATE_CODE
			makeExcel.setValue(rowCursor, 12, tempAcNoCode); // ACCOUNT_CODE
			makeExcel.setValue(rowCursor, 13, tempAcSubNoCode.trim().isEmpty() ? "00000" : tempAcSubNoCode); // SUBACCOUNT_CODE
			makeExcel.setValue(rowCursor, 14, "10H000"); // COSTCENTER_CODE
			makeExcel.setValue(rowCursor, 15, "00"); // CHANNEL_CODE
			makeExcel.setValue(rowCursor, 16, "000000000000000"); // IFRS17_GROUP_CODE
			makeExcel.setValue(rowCursor, 17, "999"); // INTERCOMPANY_CODE
			makeExcel.setValue(rowCursor, 18, deptCode); // DEPARTMENT_CODE
			makeExcel.setValue(rowCursor, 19, transferAmt); // ENTERED_AMOUNT
			makeExcel.setValue(rowCursor, 20, tempSlipRmk); // LINE_DESC
			makeExcel.setValue(rowCursor, 21, tempAcReceivableCode); // WRITE_OFF_CODE
			makeExcel.setValue(rowCursor, 22, ""); // RELATIONSHIP

			lineNum++;
			rowCursor++;
		}

		// 計算借方加總
		drAmtTotal = transferAmt.compareTo(BigDecimal.ZERO) > 0 ? drAmtTotal : drAmtTotal.add(transferAmt.abs());

		// 此區隔帳冊的印完，歸零
		transferAmt = BigDecimal.ZERO;
		// 特殊處理結束
	}
}
