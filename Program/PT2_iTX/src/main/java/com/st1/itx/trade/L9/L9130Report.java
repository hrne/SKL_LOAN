package com.st1.itx.trade.L9;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdAcCode;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.SlipMedia;
import com.st1.itx.db.domain.SlipMediaId;
import com.st1.itx.db.service.CdAcCodeService;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.SlipMediaService;
import com.st1.itx.db.service.springjpa.cm.L9130ServiceImpl;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.format.FormatUtil;

/**
 * 產製L9130Report
 * 
 * @author st1
 *
 */
@Component("L9130Report")
@Scope("prototype")
public class L9130Report extends MakeReport {

	/* DB服務注入 */
	@Autowired
	CdAcCodeService sCdAcCodeService;

	/* DB服務注入 */
	@Autowired
	CdCodeService sCdCodeService;

	/* DB服務注入 */
	@Autowired
	SlipMediaService sSlipMediaService;

	@Autowired
	MakeFile makeFile;

	@Autowired
	L9130ServiceImpl l9130ServiceImpl;

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
	String slipRmk = "";

	// 會計科目銷帳碼
	String acReceivableCode;

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

	String lastAcBookCode;

	String lastAcBookItem;

	BigDecimal drAmt;

	BigDecimal crAmt;

	public void exec(TitaVo titaVo) throws LogicException {
		this.info("L9130Report exec ...");

		// 會計日期 #AcDate=D,7,I
		iAcDate = Integer.parseInt(titaVo.getParam("AcDate"));

		// 傳票批號 #BatchNo=A,2,I
		iBatchNo = Integer.parseInt(titaVo.getParam("BatchNo"));

		// 核心傳票媒體上傳序號 #MediaSeq=A,3,I
		iMediaSeq = Integer.parseInt(titaVo.getParam("MediaSeq"));

		slipNo = getSlipNo(iAcDate, iMediaSeq);

		this.info("L9130Report iAcDate = " + iAcDate);
		this.info("L9130Report iBatchNo = " + iBatchNo);
		this.info("L9130Report iMediaSeq = " + iMediaSeq);
		this.info("L9130Report slipNo = " + slipNo);

		// brno 單位
		String brno = titaVo.getBrno();
		// date 檔案日期
		int date = iAcDate + 19110000;
		// no 檔案編號
		String no = "L9130";
		// desc 檔案說明
		String desc = "核心傳票媒體檔";

		// 檔名編碼方式
		// 固定值 核心傳票媒體上傳序號
		// jori 999

		// name 輸出檔案名稱
		String name = "核心傳票媒體檔_jori" + FormatUtil.pad9("" + iMediaSeq, 3) + ".csv";

		// format 輸出檔案格式 1.UTF8 2.BIG5
		int format = 2;

		makeFile.open(titaVo, date, brno, no, desc, name, format);

		Slice<SlipMedia> sSlipMedia = sSlipMediaService.findMediaSeq(iAcDate + 19110000, iBatchNo, iMediaSeq, 0, Integer.MAX_VALUE, titaVo);

		if (sSlipMedia != null && !sSlipMedia.isEmpty()) {
			// 若已存在,將該筆舊傳票刪除
			for (SlipMedia tSlipMedia : sSlipMedia.getContent()) {
				try {
					sSlipMediaService.delete(tSlipMedia, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "傳票媒體檔");
				}
			}
		}

		List<Map<String, String>> l9130List = null;

		try {
			l9130List = l9130ServiceImpl.doQuery(iAcDate + 19110000, iBatchNo, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("l9130ServiceImpl.doQuery error = " + e.getMessage());
		}

		int i = 1;

		lastAcBookCode = "000";
		drAmt = BigDecimal.ZERO;
		crAmt = BigDecimal.ZERO;

		for (Map<String, String> l9130 : l9130List) {

			slipDate = l9130.get("F0"); // AcDate
			l9130.get("F1"); // SlipBatNo
			acBookCode = l9130.get("F2"); // AcBookCode

			// 上一筆帳冊別不為000 且 此筆帳冊別與上一筆不同
			if (!lastAcBookCode.equals("000") && !acBookCode.equals(lastAcBookCode)) {
				// 特殊處理:帳冊別非000時將借貸方金額分別加總後,借貸方各寫一筆反向10340000 應收調撥款
				specialHandling(i, titaVo);
				// 特殊處理結束
			}

			acNoCode = l9130.get("F3"); // AcNoCode
			acSubNoCode = l9130.get("F4"); // AcSubCode
			dbCr = l9130.get("F5"); // DbCr

			txAmt = l9130.get("F6") == null ? BigDecimal.ZERO : new BigDecimal(l9130.get("F6")); // TxAmt

			// 報表檔的金額處理,借方為正數,貸方為負數
			txBal = dbCr.equals("D") ? txAmt.toString() : txAmt.negate().toString();

			slipRmk = l9130.get("F7") == null ? " " : l9130.get("F7"); // SlipNote
			acReceivableCode = l9130.get("F8") == null ? " " : l9130.get("F8"); // RvNo

			CdCode tCdCode = sCdCodeService.getItemFirst(6, "AcBookCode", acBookCode, titaVo);

			acBookItem = tCdCode == null ? " " : tCdCode.getItem();

			Slice<CdAcCode> slCdAcCode = sCdAcCodeService.findAcCodeOld(acNoCode, acNoCode, acSubNoCode, acSubNoCode, "  ", "  ", 0, Integer.MAX_VALUE, titaVo);

			List<CdAcCode> lCdAcCode = slCdAcCode == null ? null : slCdAcCode.getContent();

			if (lCdAcCode == null || lCdAcCode.isEmpty()) {
				throw new LogicException("E9002", "會計科子細目設定檔");
			} else {
				CdAcCode tCdAcCode = lCdAcCode.get(0);

				acNoItem = tCdAcCode == null ? " " : tCdAcCode.getAcNoItem();
			}

			// 寫入一筆到報表檔
			makeFile.put(acBookCode + "," + slipNo + "," + i + "," + slipDate + "," + acNoCode + "," + acSubNoCode + "," + txBal + "," + slipRmk + "," + acReceivableCode + "," + costMonth + ","
					+ insuNo + "," + empNo + "," + salaryCode + "," + currencyCode);

			// 寫入一筆到SlipMedia
			SlipMedia tSlipMedia = new SlipMedia();

			SlipMediaId tSlipMediaId = new SlipMediaId();

			tSlipMediaId.setAcDate(iAcDate);
			tSlipMediaId.setBatchNo(iBatchNo);
			tSlipMediaId.setMediaSeq(iMediaSeq);
			tSlipMediaId.setMediaSlipNo(slipNo);
			tSlipMediaId.setAcBookCode(acBookCode);
			tSlipMediaId.setSeq(i);

			tSlipMedia.setSlipMediaId(tSlipMediaId);

			tSlipMedia.setAcDate(iAcDate);
			tSlipMedia.setBatchNo(iBatchNo);
			tSlipMedia.setMediaSeq(iMediaSeq);
			tSlipMedia.setMediaSlipNo(slipNo);
			tSlipMedia.setAcBookCode(acBookCode);
			tSlipMedia.setSeq(i);
			tSlipMedia.setAcBookItem(acBookItem);
			tSlipMedia.setAcNoCode(acNoCode);
			tSlipMedia.setAcSubCode(acSubNoCode);
			tSlipMedia.setAcNoItem(acNoItem);
			tSlipMedia.setCurrencyCode(currencyCode);
			tSlipMedia.setDbCr(dbCr);
			tSlipMedia.setTxAmt(txAmt);
			tSlipMedia.setReceiveCode(acReceivableCode);
			tSlipMedia.setSlipRmk(slipRmk);
			tSlipMedia.setCostMonth(costMonth);

			try {
				sSlipMediaService.insert(tSlipMedia, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "傳票媒體檔");
			}

			i++;

			// 若當筆帳冊別不是000
			if (!acBookCode.equals("000")) {
				// 特殊處理:帳冊別非000時將借貸方金額分別加總後,借貸方各寫一筆反向10340000 應收調撥款
				// 計算借貸方金額加總
				if (dbCr.equals("D")) {
					drAmt = drAmt.add(txAmt);
				} else {
					crAmt = crAmt.add(txAmt);
				}
				// 特殊處理結束
			}

			lastAcBookCode = acBookCode;
			lastAcBookItem = acBookItem;
		}

		specialHandling(i, titaVo);

		// 寫產檔記錄到TxFile
		long fileno = makeFile.close();

		// 產生CSV檔案
		makeFile.toFile(fileno);
	}

	/**
	 * 組傳票編號<BR>
	 * "F"+year+month(123456789ABC)+day+mediaSeq核心傳票媒體上傳序號
	 * 
	 * @param acDate   國曆會計日期
	 * @param mediaSeq 核心傳票媒體上傳序號
	 * @return 傳票號碼
	 */
	private String getSlipNo(int acDate, int mediaSeq) {

		// 傳票編號 "F"+year+month(123456789ABC)+day+mediaSeq核心傳票媒體上傳序號
		String tmpSlipNo = "";

		String sAcDate = FormatUtil.pad9(String.valueOf(acDate), 7);

		String year = sAcDate.substring(0, 3);

		String month = sAcDate.substring(3, 5);

		String day = sAcDate.substring(5, 7);

		int iYear = Integer.parseInt(year);

		// 固定值 民國年 月份 日期 核心傳票媒體上傳序號
		// F10 Y M dd 999

		// 民國年特殊處理
		// 99年啟用 只有1碼
		// 108年起為A
		// 依此類推
		// 99~107 = 1~9
		// 108~ = A~

		// 月份特殊處理
		// 1 2 3 4 5 6 7 8 9 10 11 12
		// 1 2 3 4 5 6 7 8 9 A B C

		if (iYear >= 99 && iYear <= 107) {
			// 99年是1
			// 107年是9
			year = String.valueOf(1 + (iYear - 98));
		} else if (iYear >= 108 && iYear <= 133) {
			// 108 是 A
			// 109 是 B
			// B = A + (109 - 108)
			char cYear = (char) ('A' + (iYear - 108));
			year = Character.toString(cYear);
		} else {
			this.error("L9130Report getSlipNo year < 98 or year > 133.");
			year = " ";
		}

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
				this.error("L9130Report getSlipNo month > 12.");
				break;
			}
		} else {
			month = String.valueOf(iMonth);
		}

		String sMediaSeq = FormatUtil.pad9(String.valueOf(mediaSeq), 3);

		tmpSlipNo = "F10" + year + month + day + sMediaSeq;

		this.info("L9130Report getSlipNo slipNo : " + tmpSlipNo);
		return tmpSlipNo;
	}

	/**
	 * 特殊處理：帳冊別非000時將借貸方金額分別加總後,借貸方各寫一筆反向10340000 應收調撥款
	 * 
	 * @param i      印到第i筆
	 * @param titaVo titaVo
	 * @return 處理後的新筆數
	 * @throws LogicException LogicException
	 */
	private int specialHandling(int i, TitaVo titaVo) throws LogicException {
		// 特殊處理:帳冊別非000時將借貸方金額分別加總後,借貸方各寫一筆反向10340000 應收調撥款
		// 此筆帳冊別非000
		if (!acBookCode.equals("000")) {
			// 於借貸方各寫一筆10340000 應收調撥款
			acNoCode = "10340000";
			acSubNoCode = "";
			acNoItem = "應收調撥款";
			if (crAmt.compareTo(BigDecimal.ZERO) != 0) {
				// 寫入一筆
				makeFile.put(lastAcBookCode + "," + slipNo + "," + i + "," + slipDate + "," + acNoCode + "," + acSubNoCode + "," + crAmt.toString() + "," + slipRmk + "," + " " + "," + costMonth + ","
						+ insuNo + "," + empNo + "," + salaryCode + "," + currencyCode);

				SlipMedia tSlipMedia = new SlipMedia();

				SlipMediaId tSlipMediaId = new SlipMediaId();

				tSlipMediaId.setAcDate(iAcDate);
				tSlipMediaId.setBatchNo(iBatchNo);
				tSlipMediaId.setMediaSeq(iMediaSeq);
				tSlipMediaId.setMediaSlipNo(slipNo);
				tSlipMediaId.setAcBookCode(lastAcBookCode);
				tSlipMediaId.setSeq(i);

				tSlipMedia.setSlipMediaId(tSlipMediaId);

				tSlipMedia.setAcDate(iAcDate);
				tSlipMedia.setBatchNo(iBatchNo);
				tSlipMedia.setMediaSeq(iMediaSeq);
				tSlipMedia.setMediaSlipNo(slipNo);
				tSlipMedia.setAcBookCode(lastAcBookCode);
				tSlipMedia.setSeq(i);
				tSlipMedia.setAcBookItem(lastAcBookItem);
				tSlipMedia.setAcNoCode(acNoCode);
				tSlipMedia.setAcSubCode(acSubNoCode);
				tSlipMedia.setAcNoItem(acNoItem);
				tSlipMedia.setCurrencyCode(currencyCode);
				tSlipMedia.setDbCr("D");
				tSlipMedia.setTxAmt(crAmt.abs());
				tSlipMedia.setReceiveCode(" ");
				tSlipMedia.setSlipRmk(slipRmk);
				tSlipMedia.setCostMonth(costMonth);

				try {
					sSlipMediaService.insert(tSlipMedia, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0005", "傳票媒體檔");
				}

				i++;
			}

			if (drAmt.compareTo(BigDecimal.ZERO) != 0) {

				// 寫入一筆
				makeFile.put(lastAcBookCode + "," + slipNo + "," + i + "," + slipDate + "," + acNoCode + "," + acSubNoCode + "," + drAmt.negate().toString() + "," + slipRmk + "," + " " + ","
						+ costMonth + "," + insuNo + "," + empNo + "," + salaryCode + "," + currencyCode);

				SlipMedia tSlipMedia = new SlipMedia();

				SlipMediaId tSlipMediaId = new SlipMediaId();

				tSlipMediaId.setAcDate(iAcDate);
				tSlipMediaId.setBatchNo(iBatchNo);
				tSlipMediaId.setMediaSeq(iMediaSeq);
				tSlipMediaId.setMediaSlipNo(slipNo);
				tSlipMediaId.setAcBookCode(lastAcBookCode);
				tSlipMediaId.setSeq(i);

				tSlipMedia.setSlipMediaId(tSlipMediaId);

				tSlipMedia.setAcDate(iAcDate);
				tSlipMedia.setBatchNo(iBatchNo);
				tSlipMedia.setMediaSeq(iMediaSeq);
				tSlipMedia.setMediaSlipNo(slipNo);
				tSlipMedia.setAcBookCode(lastAcBookCode);
				tSlipMedia.setSeq(i);
				tSlipMedia.setAcBookItem(lastAcBookItem);
				tSlipMedia.setAcNoCode(acNoCode);
				tSlipMedia.setAcSubCode(acSubNoCode);
				tSlipMedia.setAcNoItem(acNoItem);
				tSlipMedia.setCurrencyCode(currencyCode);
				tSlipMedia.setDbCr("C");
				tSlipMedia.setTxAmt(drAmt.abs());
				tSlipMedia.setReceiveCode("");
				tSlipMedia.setSlipRmk("");
				tSlipMedia.setCostMonth(costMonth);

				try {
					sSlipMediaService.insert(tSlipMedia, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0005", "傳票媒體檔");
				}

				i++;
			}

			// 此帳冊別的印完，歸零
			drAmt = BigDecimal.ZERO;
			crAmt = BigDecimal.ZERO;
		}
		// 特殊處理結束

		return i;
	}
}
