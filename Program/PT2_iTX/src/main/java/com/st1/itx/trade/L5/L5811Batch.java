package com.st1.itx.trade.L5;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.JobMainService;
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
	MakeExcel makeExcel;

	@Autowired
	WebClient webClient;

	@Autowired
	JobMainService sJobMainService;

	@Autowired
	L5811ServiceImpl l5811ServiceImpl;

	@Autowired
	Parse parse;

	private Boolean checkFlag = true;
	private String sendMsg = " ";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.totaVo.init(titaVo);

		this.info("L5811 titaVo.getTxcd() = " + titaVo.getTxcd());

		String iYear = titaVo.getParam("Year");

		String iTxcd = titaVo.getTxcd();

		int iYearMonth = 0;

		if (("L5810").equals(iTxcd)) {
			iYearMonth = Integer.parseInt(titaVo.getParam("EndMonth")) + 191100;
		} else {
			iYearMonth = Integer.parseInt(iYear + 12) + 191100;
		}

		this.info("iTxcd==" + iTxcd);
		this.info("iYearMonth==" + iYearMonth);

		int iYYYYMM = Integer.parseInt(iYear + 12) + 191100;
		int iCustNo = Integer.parseInt(titaVo.getParam("CustNo")); // L5810連動
		String iAcctCode = titaVo.getParam("Acct");// L5810連動
		int iStartMonth = Integer.parseInt(titaVo.get("StartMonth"));
		if (iStartMonth > 0) {
			iStartMonth = iStartMonth + 191100;
		}
		int iEndMonth = Integer.parseInt(titaVo.get("EndMonth"));
		if (iEndMonth > 0) {
			iEndMonth = iEndMonth + 191100;
		}
		if (iStartMonth > 0 && iEndMonth > 0) {
			iYYYYMM = iEndMonth;
		}

		// 發動產生檢核檔的StoredProcedure
		sJobMainService.Usp_L9_YearlyHouseLoanIntCheck_Upd(titaVo.getEntDyI(), titaVo.getTlrNo(), iYYYYMM, iStartMonth,
				iEndMonth, iCustNo, iAcctCode, titaVo);

		try {
			makeExcel(iYear, iYYYYMM, iCustNo, iAcctCode, iStartMonth, iEndMonth, titaVo);
		} catch (LogicException e) {
			checkFlag = false;
			sendMsg = e.getErrorMsg();
		}

		if (checkFlag) {
			webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", titaVo.getTlrNo()+"L5811",
					"L5811國稅局申報檢核檔已完成", titaVo);
		} else {
			webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L5811", titaVo.getTlrNo(),
					sendMsg, titaVo);
		}

		this.addList(this.totaVo);
		return this.sendList();

	}

	public void makeExcel(String iYear, int iYYYYMM, int iCustNo, String iAcctCode, int iStartMonth, int iEndMonth,
			TitaVo titaVo) throws LogicException {

		int excelYear = Integer.parseInt(iYear) ;
		List<Map<String, String>> resultList = null;

		try {
			resultList = l5811ServiceImpl.doQuery(iYYYYMM, iCustNo, iAcctCode, iStartMonth, iEndMonth, titaVo);
		} catch (Exception e) {
			throw new LogicException("E0013", e.getMessage());
		}

		if (resultList == null || resultList.size() == 0) {
			checkFlag = false;
			sendMsg = "每年房屋擔保借款繳息檔無資料";
		}

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L5811", "每年房屋擔保借款繳息檢核檔" + excelYear + "年度",
				"每年房屋擔保借款繳息檢核檔" + excelYear + "年度", "L5811_每年房屋擔保借款繳息檢核檔.xls", "Sheet1");
		int i = 3;

		this.info("resultList size==" + resultList.size());

		if (resultList != null && resultList.size() > 0) {

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

//				makeExcel.setValue(i, 20, result.get("F19"));// CK05 聯絡人姓名
				String icustukey = result.get("F33");//客戶識別碼
				Map<String, String> custTelNoMap = findCustTelNo(icustukey, titaVo);
				makeExcel.setValue(i, 20, custTelNoMap.get("ContactName"));// CK05 聯絡人姓名
				
				
				makeExcel.setValue(i, 21, result.get("F20"));// CK06 戶名
				makeExcel.setValue(i, 22, result.get("F21"));// 地址
				makeExcel.setValue(i, 23, result.get("F22"));// 借戶姓名
				makeExcel.setValue(i, 24, result.get("F23"));// 統一編號

				makeExcel.setValue(i, 26, result.get("F17"));// 戶號
				makeExcel.setValue(i, 27, result.get("F18"));// 額度
				BigDecimal loanAmt = parse.stringToBigDecimal(result.get("F24"));
				makeExcel.setValue(i, 28, loanAmt, "#,##0"); // 初貸金額
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
				BigDecimal loanBal = parse.stringToBigDecimal(result.get("F27"));
				makeExcel.setValue(i, 31, loanBal, "#,##0");// 本期未償還本金額

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
				BigDecimal yearlyInt = parse.stringToBigDecimal(result.get("F30"));
				makeExcel.setValue(i, 34, yearlyInt, "#,##0");// 繳息金額
				makeExcel.setValue(i, 35, result.get("F31"));// 科子細目代號暨說明

				String usageCode = result.get("F31");
				String usageCodeItem = "";
				if ((usageCode).equals("310")) {
					usageCodeItem = "短期擔保放款";
				} else if ((usageCode).equals("320")) {
					usageCodeItem = "中期擔保放款";
				} else if ((usageCode).equals("330")) {
					usageCodeItem = "長期擔保放款";
				} else if ((usageCode).equals("340")) {
					usageCodeItem = "三十年房貸";
				}
				makeExcel.setValue(i, 36, usageCodeItem);// 科子細目代號暨說明

				i++;
			}
		}

		long sno = makeExcel.close();
		totaVo.put("ExcelSnoM", "" + sno);
	}
	
	private Map<String, String> findCustTelNo(String custukey, TitaVo titaVo) {
		this.info("findCustTelNo start ...");

		Map<String, String> result = new HashMap<>();

		String contactName = "";
		String scustId = "";

		if (custukey == null || ("").equals(custukey)) {
		} else {
			List<Map<String, String>> queryResultList = null;
			try {
				queryResultList = l5811ServiceImpl.queryCustTelNo(custukey, titaVo);
			} catch (Exception e) {
				this.error("queryCustTelNo error = " + e.getMessage());
			}
			if (queryResultList != null && !queryResultList.isEmpty()) {
				Map<String, String> queryResult = queryResultList.get(0);
				scustId = queryResult.get("CustId");
				contactName = queryResult.get("ContactName");
			}

		}
		result.put("ScustId", scustId);
		result.put("ContactName", contactName);
		this.info("findCustTelNo end ... contactName = " + contactName + " , scustId = " + scustId);
		return result;
	}

}
