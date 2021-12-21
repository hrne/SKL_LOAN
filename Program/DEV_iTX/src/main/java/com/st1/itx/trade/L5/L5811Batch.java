package com.st1.itx.trade.L5;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.*;
import com.st1.itx.db.service.YearlyHouseLoanIntService;
import com.st1.itx.db.service.springjpa.cm.L5811ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Component("L5811Batch")
@Scope("prototype")

public class L5811Batch extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	DateUtil dDateUtil;

	@Autowired
	public MakeExcel makeExcel;

	@Autowired
	WebClient webClient;

	@Autowired
	L5811ServiceImpl l5811ServiceImpl;

	@Autowired
	public YearlyHouseLoanIntService sYearlyHouseLoanIntService;

	@Autowired
	Parse parse;

	private Boolean checkFlag = true;
	private String sendMsg = " ";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.totaVo.init(titaVo);

		this.info("L5811 titaVo.getTxcd() = " + titaVo.getTxcd());

		String iYear = titaVo.getParam("Year");

		try {
			checkAll(iYear, titaVo);
			doJsonField(iYear, titaVo);
		} catch (LogicException e) {
			checkFlag = false;
			sendMsg = e.getErrorMsg();
		}

		if (checkFlag) {
			webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", titaVo.getTlrNo(), "L5811國稅局申報檢核檔已完成", titaVo);
		} else {
			webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", titaVo.getTlrNo(), sendMsg, titaVo);
		}

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
		totaVo.put("ExcelSnoM", "" + sno);

		this.addList(this.totaVo);
		return this.sendList();

	}

	public void checkAll(String iYear, TitaVo titaVo) throws LogicException {

		int ExcelYear = Integer.parseInt(iYear) + 1;
		List<Map<String, String>> resultList = null;

		try {
			resultList = l5811ServiceImpl.checkAll(iYear, titaVo);
		} catch (Exception e) {
			throw new LogicException("E0013", e.getMessage());
		}

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L5811", "每年房屋擔保借款繳息檢核檔", "每年房屋擔保借款繳息檢核檔" + ExcelYear + "年度", "L5811_每年房屋擔保借款繳息檢核檔.xls", "Sheet1");
		int i = 3;

		for (Map<String, String> result : resultList) {

			makeExcel.setValue(i, 1, result.get("F0")); // r1 借戶姓名空白
			makeExcel.setValue(i, 2, result.get("F1")); // r2 統一編號空白
			makeExcel.setValue(i, 3, result.get("F2")); // r3 貸款帳號空白
			makeExcel.setValue(i, 4, result.get("F3")); // r4 最初貸款金額為０
			makeExcel.setValue(i, 5, result.get("F4")); // r5 最初貸款金額＞核准額度
			makeExcel.setValue(i, 6, result.get("F5")); // r6 最初貸款金額＜放款餘額
			makeExcel.setValue(i, 7, result.get("F6")); // r7 貸款起日空白
			makeExcel.setValue(i, 8, result.get("F7")); // r8 貸款訖日空白
			makeExcel.setValue(i, 9, result.get("F8")); // r10 繳息所屬年月空白
			makeExcel.setValue(i, 10, result.get("F9")); // r11 繳息金額為 0
			makeExcel.setValue(i, 11, result.get("F10"));// r12 科子細目代號暨說明空白
			makeExcel.setValue(i, 12, result.get("F11"));// c1 一額度一撥款
			makeExcel.setValue(i, 13, result.get("F12"));// c2 一額度多撥款
			makeExcel.setValue(i, 14, result.get("F13"));// c3 多額度多撥款
			makeExcel.setValue(i, 15, result.get("F14"));// c4 借新還舊件
			makeExcel.setValue(i, 16, result.get("F15"));// c5 清償件
			makeExcel.setValue(i, 17, result.get("F16"));// CK01 郵遞區號
			makeExcel.setValue(i, 18, result.get("F17"));// CK02 戶號
			makeExcel.setValue(i, 19, result.get("F18"));// CK04 額度
			makeExcel.setValue(i, 20, result.get("F19"));// CK05 聯絡人姓名
			makeExcel.setValue(i, 21, result.get("F20"));// CK06 戶名
			makeExcel.setValue(i, 22, result.get("F21"));// 地址
			makeExcel.setValue(i, 23, result.get("F22"));// 借戶姓名
			makeExcel.setValue(i, 24, result.get("F23"));// 統一編號

			makeExcel.setValue(i, 26, result.get("F17"));// 戶號
			makeExcel.setValue(i, 27, result.get("F18"));// 額度
			BigDecimal LoanAmt = parse.stringToBigDecimal(result.get("F24"));
			makeExcel.setValue(i, 28, LoanAmt, "#,##0"); // 初貸金額
			if (Integer.parseInt(result.get("F25")) <= 0) { // 貸款起日
				makeExcel.setValue(i, 29, "0");
			} else {
				makeExcel.setValue(i, 29, result.get("F25"));
			}
			if (Integer.parseInt(result.get("F26")) <= 0) { // 貸款迄日
				makeExcel.setValue(i, 30, "0");
			} else {
				makeExcel.setValue(i, 30, result.get("F26"));
			}
			BigDecimal LoanBal = parse.stringToBigDecimal(result.get("F27"));
			makeExcel.setValue(i, 31, LoanBal, "#,##0");// 本期未償還本金額

			if (Integer.parseInt(result.get("F28")) <= 0) {// 繳息所屬年月(起)
				makeExcel.setValue(i, 32, "0");
			} else {
				makeExcel.setValue(i, 32, result.get("F28"));
			}

			if (Integer.parseInt(result.get("F29")) <= 0) {// 繳息所屬年月(止)
				makeExcel.setValue(i, 33, 0);
			} else {
				makeExcel.setValue(i, 33, result.get("F29"));
			}
			BigDecimal YearlyInt = parse.stringToBigDecimal(result.get("F30"));
			makeExcel.setValue(i, 34, YearlyInt, "#,##0");// 繳息金額
			makeExcel.setValue(i, 35, result.get("F31"));// 科子細目代號暨說明

			String UsageCode = result.get("F31");
			String UsageCodeItem = "";
			if ((UsageCode).equals("310")) {
				UsageCodeItem = "短期擔保放款";
			} else if ((UsageCode).equals("320")) {
				UsageCodeItem = "中期擔保放款";
			} else if ((UsageCode).equals("330")) {
				UsageCodeItem = "長期擔保放款";
			} else if ((UsageCode).equals("340")) {
				UsageCodeItem = "三十年房貸";
			}
			makeExcel.setValue(i, 36, UsageCodeItem);// 科子細目代號暨說明

			i++;

		}

	}

	public void doJsonField(String iYear, TitaVo titaVo) throws LogicException {

		List<Map<String, String>> resultList = null;

		try {
			resultList = l5811ServiceImpl.doJsonField(iYear, titaVo);
		} catch (Exception e) {
			throw new LogicException("E0013", e.getMessage());
		}

		TempVo tTempVo = new TempVo();

		for (Map<String, String> result : resultList) {
			YearlyHouseLoanIntId tYearlyHouseLoanIntId = new YearlyHouseLoanIntId();
			YearlyHouseLoanInt tYearlyHouseLoanInt = new YearlyHouseLoanInt();

			tYearlyHouseLoanIntId.setYearMonth(Integer.parseInt(result.get("F9")));
			tYearlyHouseLoanIntId.setCustNo(Integer.parseInt(result.get("F2")));
			tYearlyHouseLoanIntId.setFacmNo(Integer.parseInt(result.get("F3")));
			tYearlyHouseLoanIntId.setUsageCode(result.get("F12"));
			tYearlyHouseLoanInt = sYearlyHouseLoanIntService.findById(tYearlyHouseLoanIntId, titaVo);
			if (tYearlyHouseLoanInt == null) {
				continue;
			}
			tTempVo.putParam("F0", result.get("F0"));// 戶名
			tTempVo.putParam("F1", result.get("F1"));// 統編
//			tTempVo.putParam("F2",result.get("F2"));
//			tTempVo.putParam("F3",result.get("F3"));
//			tTempVo.putParam("F4",result.get("F4"));
			tTempVo.putParam("F5", result.get("F5"));// 核准額度
//			tTempVo.putParam("F6",result.get("F6"));
//			tTempVo.putParam("F7",result.get("F7"));
//			tTempVo.putParam("F8",result.get("F8"));
//			tTempVo.putParam("F9",result.get("F9"));
//			tTempVo.putParam("F10",result.get("F10"));
//			tTempVo.putParam("F11",result.get("F11"));
			tTempVo.putParam("F21", result.get("F21"));
			tYearlyHouseLoanInt.setYearlyHouseLoanIntId(tYearlyHouseLoanIntId);
			tYearlyHouseLoanInt.setJsonFields(tTempVo.getJsonString());

			try {
				sYearlyHouseLoanIntService.update(tYearlyHouseLoanInt, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg());
			}

		}
	}

}
