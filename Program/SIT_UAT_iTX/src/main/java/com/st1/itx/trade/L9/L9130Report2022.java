package com.st1.itx.trade.L9;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
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
import com.st1.itx.db.domain.SlipMedia2022;
import com.st1.itx.db.domain.SlipMedia2022Id;
import com.st1.itx.db.service.SlipMedia2022Service;
import com.st1.itx.db.service.springjpa.cm.L9130ServiceImpl;
import com.st1.itx.util.common.EbsCom;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.format.FormatUtil;

/**
 * 產製L9130Report2022<BR>
 * 2022年啟用新傳票格式<BR>
 * <BR>
 * 2021-06-22 新增 智偉<BR>
 * 說明:根據"ac034m 其他介面上傳格式_20210408.xlsx"建立傳票 <BR>
 * 2021-07-12 修改 智偉<BR>
 * 說明:傳票媒體檔以RESTful協定傳遞至EBS會計資訊系統
 * 
 * @author st1-ChihWei
 *
 */
@Component("L9130Report2022")
@Scope("prototype")
public class L9130Report2022 extends MakeReport {

	/* DB服務注入 */
	@Autowired
	private SlipMedia2022Service sSlipMedia2022Service;

	@Autowired
	private MakeFile makeFile;

	@Autowired
	private L9130ServiceImpl l9130ServiceImpl;

	@Autowired
	private EbsCom ebsCom;

	JSONArray summaryTbl;
	JSONArray journalTbl;
	int groupId;

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
	String deptCode = " ";

	// 業務員代號
	String salesmanCode = " ";

	// 成本單位
	String costUnit = " ";

	// 通路別
	String salesChannelType = " ";

	// 會計準則類型
	String ifrsType = " ";

	// 關係人ID
	String relationId = " ";

	// 關聯方代號
	String relateCode = " ";

	// IFRS17群組
	String ifrs17Group = " ";

	BigDecimal drAmt;

	BigDecimal crAmt;

	public void exec(TitaVo titaVo) throws LogicException {
		this.info("L9130Report2022 exec ...");

		// 會計日期 #AcDate=D,7,I
		iAcDate = Integer.parseInt(titaVo.getParam("AcDate"));

		// 傳票批號 #BatchNo=A,2,I
		iBatchNo = Integer.parseInt(titaVo.getParam("BatchNo"));

		// 核心傳票媒體上傳序號 #MediaSeq=A,3,I
		iMediaSeq = Integer.parseInt(titaVo.getParam("MediaSeq"));

		// 取得傳票號碼
		slipNo = getSlipNo(iAcDate, iMediaSeq);

		this.info("L9130Report2022 iAcDate = " + iAcDate);
		this.info("L9130Report2022 iBatchNo = " + iBatchNo);
		this.info("L9130Report2022 iMediaSeq = " + iMediaSeq);
		this.info("L9130Report2022 slipNo = " + slipNo);

		groupId = iAcDate + 19110000;
		groupId *= 1000;
		groupId += iMediaSeq;

		// brno 單位
		String brno = titaVo.getBrno();
		// date 檔案日期
		int date = iAcDate + 19110000;
		// no 檔案編號
		String no = "L9130";
		// desc 檔案說明
		String desc = "總帳傳票媒體檔";

		// 檔名編碼方式
		// 固定值 核心傳票媒體上傳序號
		// jori 999

		// name 輸出檔案名稱
		String name = "總帳傳票媒體檔_jori" + FormatUtil.pad9("" + iMediaSeq, 3) + ".csv";

		// format 輸出檔案格式 1.UTF8 2.BIG5
		int format = 2;

		makeFile.open(titaVo, date, brno, no, desc, name, format);

		Slice<SlipMedia2022> sSlipMedia2022 = sSlipMedia2022Service.findMediaSeq(iAcDate + 19110000, iBatchNo, iMediaSeq, 0, Integer.MAX_VALUE, titaVo);

		if (sSlipMedia2022 != null && !sSlipMedia2022.isEmpty()) {
			// 若已存在,將該筆舊傳票刪除
			for (SlipMedia2022 tSlipMedia2022 : sSlipMedia2022.getContent()) {
				try {
					sSlipMedia2022Service.delete(tSlipMedia2022, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "傳票媒體檔");
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

		drAmt = BigDecimal.ZERO;
		crAmt = BigDecimal.ZERO;

		journalTbl = new JSONArray();

		/**
		 * AcBookCode F0 帳冊別<BR>
		 * AcDate F1 會計日期 <BR>
		 * AcNoCode F2 科目代號 <BR>
		 * AcSubCode F3 子目代號 <BR>
		 * DbCr F4 借貸別 <BR>
		 * TxAmt F5 金額 <BR>
		 * AcSubBookCode F6 區隔帳冊
		 */
		for (Map<String, String> l9130 : l9130List) {

			acBookCode = l9130.get("F0"); // 帳冊別
			slipDate = l9130.get("F1"); // 會計日期
			acNoCode = l9130.get("F2"); // 科目代號
			acSubNoCode = l9130.get("F3"); // 子目代號
			dbCr = l9130.get("F4"); // 借貸別
			txAmt = l9130.get("F5") == null ? BigDecimal.ZERO : new BigDecimal(l9130.get("F5")); // 金額
			acSubBookCode = l9130.get("F6"); // 區隔帳冊

			// 借方金額累加
			drAmt = dbCr.equals("D") ? drAmt.add(txAmt) : drAmt;

			// 傳票媒體檔的金額處理,借方為正數,貸方為負數
			txBal = dbCr.equals("D") ? txAmt.toString() : txAmt.negate().toString();

			JSONObject dataJo = new JSONObject();

			try {
				dataJo.put("GROUP_ID", groupId);
				dataJo.put("BATCH_DATE", slipDate);
				dataJo.put("JE_SOURCE_NAME", "LN");
				dataJo.put("USER_LEDGER_CODE", acBookCode);
				dataJo.put("CONVENTION", "");
				dataJo.put("JOURNAL_NAME", slipNo);
				dataJo.put("CURRENCY_CODE", currencyCode);
				dataJo.put("ISSUED_BY", "");
				dataJo.put("ACCOUNTING_DATE", slipDate);
				dataJo.put("JE_LINE_NUM", "" + i);
				dataJo.put("SEGREGATE_CODE", "");
				dataJo.put("ACCOUNT_CODE", acNoCode);
				dataJo.put("SUBACCOUNT_CODE", acSubNoCode);
				dataJo.put("COSTCENTER_CODE", "");
				dataJo.put("IFRS17_GROUP_CODE", "");
				dataJo.put("INTERCOMPANY_CODE", "");
				dataJo.put("DEPARTMENT_CODE", "");
				dataJo.put("ENTERED_AMOUNT", txBal);
				dataJo.put("LINE_DESC", "");
				dataJo.put("WRITE_OFF_CODE", "");
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
			tSlipMedia2022.setDbCr(dbCr);
			tSlipMedia2022.setTxAmt(txAmt);
			tSlipMedia2022.setReceiveCode(acReceivableCode);
			tSlipMedia2022.setSlipRmk(slipRmk);
			tSlipMedia2022.setCostMonth(costMonth);

			try {
				sSlipMedia2022Service.insert(tSlipMedia2022, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "傳票媒體檔");
			}

			i++;
		}

		summaryTbl = new JSONArray();

		JSONObject summaryMap = new JSONObject();

		try {
			summaryMap.put("GROUP_ID", groupId);
			summaryMap.put("BATCH_DATE", slipDate);
			summaryMap.put("JE_SOURCE_NAME", "LN");
			summaryMap.put("TOTAL_LINES", "");
			summaryMap.put("CURRENCY_CODE", "NTD");
			summaryMap.put("TOTAL_AMOUNT", drAmt);
		} catch (JSONException e) {
			this.error("L9130Report22 error = " + e.getMessage());
		}

		summaryTbl.put(summaryMap);

//        "GROUP_ID": 20200615002,
//        "BATCH_DATE": "20200615",
//        "JE_SOURCE_NAME": "PL",
//        "TOTAL_LINES": 2,
//        "CURRENCY_CODE": "NTD",
//        "TOTAL_AMOUNT": 12450

		String result = "";
		try {
			result = ebsCom.sendSlipMediaToEbs(summaryTbl, journalTbl, titaVo);
		} catch (IOException | JSONException e) {
			this.error("L9130Report22 error = " + e.getMessage());
		}

		this.info("result = " + result);

		// 寫產檔記錄到TxFile
		long fileno = makeFile.close();

		// 產生CSV檔案
		makeFile.toFile(fileno);

	}

	/**
	 * 組傳票編號<BR>
	 * 固定值 民國年 月份 日期 核心傳票媒體上傳序號<BR>
	 * // F10 + YYY + M + DD + 999
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

		// 固定值 民國年 月份 日期 核心傳票媒體上傳序號
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

		String sMediaSeq = FormatUtil.pad9(String.valueOf(mediaSeq), 3);

		tmpSlipNo = "F10" + year + month + day + sMediaSeq;

		this.info("L9130Report2022 getSlipNo slipNo : " + tmpSlipNo);
		return tmpSlipNo;
	}
}
