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
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.BaTxVo;
import com.st1.itx.util.format.FormatUtil;

@Component
@Scope("prototype")
public class L9701Report3 extends MakeReport {

	@Autowired
	L9701ServiceImpl l9701ServiceImpl;

	// 製表日期
	private String nowDate;
	// 製表時間
	private String nowTime;

	private String custName;
	private String facmNo;

	// 最大明細筆數
	private int maxDetailCnt = 50;

	/**
	 * 帳務日
	 */
	int entday = 0;

	BigDecimal principal = BigDecimal.ZERO; // 本金
	BigDecimal interest = BigDecimal.ZERO; // 利息
	BigDecimal breachAmt = BigDecimal.ZERO; // 違約金
	BigDecimal feeAmt = BigDecimal.ZERO; // 費用
	BigDecimal principalTotal = BigDecimal.ZERO; // 本金合計
	BigDecimal interestTotal = BigDecimal.ZERO; // 利息合計
	BigDecimal breachAmtTotal = BigDecimal.ZERO; // 違約金合計
	BigDecimal feeAmtTotal = BigDecimal.ZERO; // 費用合計

	BigDecimal loanBal = BigDecimal.ZERO; // 放款餘額
	BigDecimal shortFall = BigDecimal.ZERO; // 累短收
	BigDecimal excessive = BigDecimal.ZERO; // 累溢收

	@Override
	public void printHeader() {

		this.setFontSize(9);
		this.info("L9701 exec" + this.titaVo.get("ENTDY"));
		this.print(-2, 86, "客 戶 往 來 交 易 明 細 表", "C");
		this.print(-3, 86, showRocDate(this.titaVo.get("ENTDY"), 0), "C");
		this.print(-4, 130, "印表日期：" + showRocDate(this.nowDate, 1) + " " + showTime(this.nowTime));
		String iTYPE = titaVo.get("DateType");
		String iSDAY = showRocDate(titaVo.get("BeginDate"), 1);
		String iEDAY = showRocDate(titaVo.get("EndDate"), 1);
		if (iTYPE.equals("1")) {
			this.print(-5, 1, "入帳日期 : " + iSDAY + " - " + iEDAY);
		} else {
			this.print(-5, 1, "會計日期 : " + iSDAY + " - " + iEDAY);
		}
		this.print(-5, 130, "頁　　次：" + Integer.toString(this.getNowPage()));
		String iCUSTNO = titaVo.get("CustNo");
		if (this.facmNo.equals("")) {
			this.print(-6, 1, "戶號     : " + iCUSTNO);
		} else {
			this.print(-6, 1, "戶號     : " + iCUSTNO + " " + custName);
		}
		if (this.NowRow >= maxDetailCnt - 4) {
			// 若剩餘行數不足四行,先換頁
			this.newPage(); // 換頁時會印表頭
		} else {
			printDataHeader();
		}

		// 明細起始列(自訂亦必須)
		this.setBeginRow(11);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(maxDetailCnt);
	}

	/**
	 * 印資料表頭
	 */
	private void printDataHeader() {

		String tmpFacmNo = FormatUtil.pad9(facmNo, 3);

		this.print(1, 1, "額度     : " + tmpFacmNo);
		this.print(1, 5, "－－－－－　－－－－－－　－－－－－－　－－－－－－　－－－－－－　－－－－－－　－－－－－－　－－－－－－　－－－－－－");
		this.print(1, 5, "　入帳日　　　交易內容　　交易金額(Ａ)　暫收轉帳(Ｂ)　償還本金(ａ)　償還利息(ｂ)　償還費用(ｃ)　溢短收(Ｘ)　　暫收差額(Ｍ)");
		this.print(1, 5, "－－－－－　－－－－－－　－－－－－－　－－－－－－　－－－－－－　－－－－－－　－－－－－－　－－－－－－　－－－－－－");
	}

	public void exec(TitaVo titaVo) throws LogicException {

		this.info("L9701Report3 exec");
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

		this.custName = "";
		this.facmNo = "";
		boolean isFirst = true;

		int detailCounts = 0;

		if (listL9701 != null && listL9701.size() > 0) {

			this.open(titaVo, entday, titaVo.getKinbr(), "L9701_3", "客戶往來交易明細表", "", "A4", "L");

			for (Map<String, String> tL9701Vo : listL9701) {
				if (!this.facmNo.equals(tL9701Vo.get("FacmNo")) || tL9701Vo.get("DB").equals("2")) {
					// 無交易明細且無餘額
					if (detailCounts == 0) {
						if (tL9701Vo.get("DB").equals("2")) {
							BigDecimal unpaidLoanBal = new BigDecimal(tL9701Vo.get("Amount"));
							if (unpaidLoanBal.compareTo(BigDecimal.ZERO) == 0) {
								continue;
							}
						}
					}
					if (detailCounts > 0) {
						reportTotal();
					}
					this.custName = tL9701Vo.get("CustName");
					this.facmNo = tL9701Vo.get("FacmNo");
					if (isFirst) {
						printDataHeader();
						isFirst = false;
					}
					if (tL9701Vo.get("DB").equals("2")) {
						reportTotal();
						detailCounts = 0;
						isFirst = true;
					}
				}

				if (tL9701Vo.get("DB").equals("1")) {
					printDetail1(tL9701Vo);
					detailCounts++;
				}

			}
		} else {

			this.open(titaVo, entday, titaVo.getKinbr(), "L9701_3", "客戶往來交易明細表", "", "A4", "L");

			this.print(1, 20, "*******    查無資料   ******");
		}

		this.close();

		// 測試用
		// this.toPdf(sno);
	}

	private void printDetail1(Map<String, String> tL9701Vo) {
		//入帳日
		this.print(1, 7, showRocDate(tL9701Vo.get("EntryDate"), 1));
		//交易內容
		this.print(0, 8, tL9701Vo.get("Desc"));
		
		//交易金額
		this.print(0, 40, formatAmt(tL9701Vo.get("TxAmt"), 0), "R"); //

		//暫收借
		BigDecimal tempAmt = new BigDecimal(tL9701Vo.get("TempAmt"));
		BigDecimal tempDbAmt = tempAmt.compareTo(BigDecimal.ZERO) > 0 ? tempAmt : BigDecimal.ZERO;
		BigDecimal tempCrAmt = tempAmt.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO.subtract(tempAmt)
				: BigDecimal.ZERO;
		this.print(0, 57, formatAmt(tempDbAmt, 0), "R"); 
		//本金
		this.print(0, 72, formatAmt(tL9701Vo.get("Principal"), 0), "R");
		//利息
		this.print(0, 86, formatAmt(tL9701Vo.get("Interest"), 0), "R");
		//違約金
		this.print(0, 99, formatAmt(tL9701Vo.get("BreachAmt"), 0), "R");
		//費用
		this.print(0, 112, formatAmt(tL9701Vo.get("FeeAmt"), 0), "R");
		//短繳
		this.print(0, 125, formatAmt(tL9701Vo.get("ShortAmt"), 0), "R");
		//暫收貸
//		this.print(0, 135, formatAmt(tL9701Vo.get("tempCrAmt"), 0), "R");
		
		principal = new BigDecimal(tL9701Vo.get("Principal"));
		interest = new BigDecimal(tL9701Vo.get("Interest"));
		breachAmt = new BigDecimal(tL9701Vo.get("BreachAmt"));
		feeAmt = new BigDecimal(tL9701Vo.get("FeeAmt"));
		principalTotal = principalTotal.add(principal);
		interestTotal = interestTotal.add(interest);
		breachAmtTotal = breachAmtTotal.add(breachAmt);
		feeAmtTotal = feeAmtTotal.add(feeAmt);
	}

	private void reportTotal() {

		this.print(1, 7, "－－－－－　－－－－－－－－　－－－－－－　－－－－－－　－－－－－－　－－－－－－－－　－－－－－－－－　－－－－－－－－");
		this.print(0, 67, "小計：");
		this.print(0, 82, formatAmt(principalTotal, 0), "R");
		this.print(0, 96, formatAmt(interestTotal, 0), "R");
		this.print(0, 109, formatAmt(breachAmtTotal, 0), "R");
		this.print(0, 122, formatAmt(feeAmtTotal, 0), "R");

		principalTotal = BigDecimal.ZERO;
		interestTotal = BigDecimal.ZERO;
		breachAmtTotal = BigDecimal.ZERO;
		feeAmtTotal = BigDecimal.ZERO;
	}

}
