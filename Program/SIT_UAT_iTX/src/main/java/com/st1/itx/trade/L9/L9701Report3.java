package com.st1.itx.trade.L9;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L9701ServiceImpl;
import com.st1.itx.util.common.CustNoticeCom;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")
public class L9701Report3 extends MakeReport {

	@Autowired
	L9701ServiceImpl l9701ServiceImpl;

	@Autowired
	Parse parse;

	@Autowired
	CustNoticeCom custNoticeCom;

	// 製表日期
	private String nowDate;
	// 製表時間
	private String nowTime;

	private String iCUSTNO = "";

	private String custName = "";

	// 最大明細筆數
	private int maxDetailCnt = 40;

	/**
	 * 帳務日
	 */
	int entday = 0;

	BigDecimal txAmtTotal = BigDecimal.ZERO; // 交易金額合計
	BigDecimal tempAmtTotal = BigDecimal.ZERO; // 暫收轉帳合計
	BigDecimal repaidPrinTotal = BigDecimal.ZERO; // 償還本金合計
	BigDecimal repaidInterestTotal = BigDecimal.ZERO; // 償還利息合計
	BigDecimal repaidExpTotal = BigDecimal.ZERO; // 償還費用合計
	BigDecimal tempDiffTotal = BigDecimal.ZERO; // 暫收差額合計

	@Override
	public void printHeader() {

		this.setFontSize(9);

		this.print(-1, 82, "新光人壽保險股份有限公司", "C");
		this.print(-2, 82, "客戶往來交易明細表", "C");
		this.print(-3, 82, showRocDate(this.titaVo.getEntDyI(), 0), "C");

		String iTYPE = titaVo.get("DateType");
		String iSDAY = showRocDate(titaVo.get("BeginDate"), 1);
		String iEDAY = showRocDate(titaVo.get("EndDate"), 1);
		if (iTYPE.equals("1")) {
			this.print(-4, 3, "入帳日期 : " + iSDAY + " - " + iEDAY);
		} else {
			this.print(-4, 3, "會計日期 : " + iSDAY + " - " + iEDAY);
		}

		this.print(-5, 3, "戶號     : " + iCUSTNO + " " + custName);

		this.print(-4, 134, "印表日期：" + showRocDate(this.nowDate, 1) + " " + showTime(this.nowTime));
		this.print(-5, 134, "頁　　次：" + Integer.toString(this.getNowPage()));

		printDataHeader();

		// 明細起始列(自訂亦必須)
		this.setBeginRow(9);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(maxDetailCnt);
	}

	@Override
	public void printContinueNext() {
		this.print(-49, 82, "－－－　續下頁　－－－", "C");
	}

	/**
	 * 印資料表頭
	 */
	private void printDataHeader() {
		this.print(1, 3, " ");
		this.print(1, 3, "額度　撥款　入帳日期　　　交易內容　　訂正別　　交易金額　　　　　計息期間　　　　　 實收本金 　　　 實收利息 　　　實收延滯息　　　實收違約金　　　　暫收款");
		this.print(1, 3, "－－　－－　－－－－－　－－－－－－　－－－　－－－－－－　－－－－－－－－－－　－－－－－－－　－－－－－－－　－－－－－－－　－－－－－－－　－－－－－－－");
	}

	public void exec(TitaVo titaVo) throws LogicException {
		this.info("L9701Report3 exec");

		iCUSTNO = titaVo.get("CustNo");

		entday = titaVo.getEntDyI();

		this.nowDate = dDateUtil.getNowStringRoc();
		this.nowTime = dDateUtil.getNowStringTime();

		List<Map<String, String>> listL9701 = null;

		try {
			listL9701 = l9701ServiceImpl.doQuery3(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("L9701ServiceImpl.LoanBorTx error = " + errors.toString());
		}

		int cnt = 0;

		if (listL9701 != null && listL9701.size() > 0) {
			this.custName = listL9701.get(0).get("CustName");
			this.open(titaVo, entday, titaVo.getKinbr(), "L9701", "客戶往來交易明細表", "", "A4", "L");

			for (Map<String, String> tL9701Vo : listL9701) {
				// 確認 CustNoticeCom 檢查是否能產出郵寄通知

				// inputCustNo: #CustNo
				// CustNo: Query.CustNo
				// FacmNo: Query.FacmNo

//				String inputCustNo = titaVo.get("CustNo");
//				String recordCustNoString = tL9701Vo.get("CustNo");
//				String recordFacmNoString = tL9701Vo.get("FacmNo");
//				int recordCustNo = parse.stringToInteger(recordCustNoString);
//				int recordFacmNo = parse.stringToInteger(recordFacmNoString);
//				if (!custNoticeCom.checkIsLetterSendable(inputCustNo, recordCustNo, recordFacmNo, "L9701", titaVo)) {
//					continue;
//				}

				// 印明細
				printDetail(tL9701Vo);
				cnt++;
			}
			this.print(1, 3, "－－　－－　－－－－－　－－－－－－　－－－　－－－－－－　－－－－－－－－－－　－－－－－－－　－－－－－－－　－－－－－－－　－－－－－－－　－－－－－－－");
		}

		if (cnt <= 0) {
			this.open(titaVo, entday, titaVo.getKinbr(), "L9701", "客戶往來交易明細表", "", "A4", "L");
			this.print(1, 1, "本日無資料");
			this.print(1, 3, "－－　－－　－－－－－　－－－－－－　－－－　－－－－－－　－－－－－－－－－－　－－－－－－－　－－－－－－－　－－－－－－－　－－－－－－－　－－－－－－－");
		}

		this.close();
	}

	/**
	 * 印明細
	 * 
	 * @param tL9701Vo 單筆資料
	 */
	private void printDetail(Map<String, String> tL9701Vo) {

		this.print(1, 3, tL9701Vo.get("FacmNo")); // 額度號碼
		this.print(0, 9, tL9701Vo.get("BormNo")); // 撥款序號
		this.print(0, 14, showRocDate(tL9701Vo.get("EntryDate"), 1)); // 入帳日期

		// 特殊處理:交易內容有可能長度太長,截取前六字
		String desc = tL9701Vo.get("Desc");
		if (desc.length() > 6) {
			desc = desc.substring(0, 6);
		}
		this.print(0, 27, desc); // 交易內容

		if (Integer.valueOf(tL9701Vo.get("TitaHCode")) > 0) {
			this.print(0, 41, tL9701Vo.get("HItem")); // 訂正別中文
		}

		BigDecimal txAmt = getBigDecimal(tL9701Vo.get("TxAmt"));
		this.print(0, 60, formatAmt(txAmt, 0), "R"); // 交易金額

		if (Integer.parseInt(tL9701Vo.get("IntStartDate")) > 0) {
			this.print(0, 63,
					showRocDate(tL9701Vo.get("IntStartDate"), 1) + "-" + showRocDate(tL9701Vo.get("IntEndDate"), 1)); // 計息期間
		}

		BigDecimal principal = getBigDecimal(tL9701Vo.get("Principal"));
		this.print(0, 99, formatAmt(principal, 0), "R"); // 實收本金

		BigDecimal interest = getBigDecimal(tL9701Vo.get("Interest"));
		this.print(0, 115, formatAmt(interest, 0), "R"); // 實收利息

		BigDecimal delayInt = getBigDecimal(tL9701Vo.get("DelayInt"));
		this.print(0, 131, formatAmt(delayInt, 0), "R"); // 實收延滯息

		BigDecimal breachAmt = getBigDecimal(tL9701Vo.get("BreachAmt"));
		this.print(0, 147, formatAmt(breachAmt, 0), "R"); // 實收違約金

		BigDecimal tempAmt = getBigDecimal(tL9701Vo.get("TempAmt"));
		this.print(0, 163, formatAmt(tempAmt, 0), "R"); // 暫收款
	}
}
