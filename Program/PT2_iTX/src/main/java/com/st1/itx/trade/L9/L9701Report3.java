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
import com.st1.itx.util.common.data.ReportVo;

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

	public int tempPage = 0;
	
	

	String nextPageText = "=====  續下頁  =====";
	String endText = "=====  報  表  結  束  =====";

	// 最大明細筆數
//	private int maxDetailCnt = 50;

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

		this.setFontSize(8);

		this.print(-2, this.getMidXAxis(), "客 戶 往 來 交 易 明 細 表", "C");

		this.print(-3, this.getMidXAxis(), showRocDate(this.titaVo.get("ENTDY"), 0), "C");

		this.print(-4, 138, "印表日期：" + showRocDate(this.nowDate, 1) + " " + showTime(this.nowTime), "L");

		String iTYPE = titaVo.get("DateType");
		String iSDAY = showRocDate(titaVo.get("BeginDate"), 1);
		String iEDAY = showRocDate(titaVo.get("EndDate"), 1);

		if (iTYPE.equals("1")) {
			this.print(-5, 1, "入帳日期 : " + iSDAY + " - " + iEDAY);
		} else {
			this.print(-5, 1, "會計日期 : " + iSDAY + " - " + iEDAY);
		}

		this.print(-5, 138, "頁　　次：" + Integer.toString(this.getNowPage()), "L");

		String iCUSTNO = titaVo.get("CustNo");

		if (this.facmNo.equals("")) {
			this.print(-6, 1, "戶號　　  : " + iCUSTNO);
		} else {
			this.print(-6, 1, "戶號　　 : " + iCUSTNO + " " + custName);
		}

		this.setBeginRow(7);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(45);

	}

	private void printDataHeader() {


		String tmpFacmNo = String.format("%03d", Integer.valueOf(facmNo));

		this.print(1, 1, " ");
		this.print(1, 1, "額度　　 : " + tmpFacmNo);
		divider();
		this.print(1, 2, "撥款");
		this.print(0, 10, "入帳日期");
		this.print(0, 28, "交易內容","C");
		this.print(0, 40, "交易金額");
		this.print(0, 59, "暫收借");
		this.print(0, 76, "本金");
		this.print(0, 91, "利息");
		this.print(0, 106, "違約金");
		this.print(0, 123, "費用");
		this.print(0, 139, "短繳");
		this.print(0, 154, "暫收貸");
		divider();

//		}

	}

	/**
	 * 分割線
	 * 
	 */
	public void divider() {
		this.print(1, 2, "－－");
		this.print(0, 9, "－－－－－");
		this.print(0, 28, "－－－－－－","C");
		this.print(0, 38, "－－－－－－");
		this.print(0, 56, "－－－－－－");
		this.print(0, 72, "－－－－－－");
		this.print(0, 87, "－－－－－－");
		this.print(0, 103, "－－－－－－");
		this.print(0, 119, "－－－－－－");
		this.print(0, 135, "－－－－－－");
		this.print(0, 151, "－－－－－－");
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

		String tradeReportName = "客戶往來交易明細表";

		ReportVo reportVo = ReportVo.builder().setRptDate(titaVo.getEntDyI()).setBrno(titaVo.getKinbr())
				.setRptCode("L9701").setRptItem(tradeReportName).setSecurity("")
				.setRptSize("A4").setPageOrientation("L").build();
		
		this.open(titaVo, reportVo);

		if (listL9701 != null && listL9701.size() > 0) {

			for (Map<String, String> tL9701Vo : listL9701) {
//				this.info("nowrow=" + this.NowRow);
				if (this.NowRow - 7 >= 40) {
					this.print(1, this.getMidXAxis(), nextPageText, "C");
					this.newPage();
					this.print(1, 1, " ");
				}

				if (!this.facmNo.equals(tL9701Vo.get("FacmNo")) || tL9701Vo.get("DB").equals("2")) {
					// 無交易明細且無餘額
					if (detailCounts == 0) {
						if (tL9701Vo.get("DB").equals("2")) {
							BigDecimal unpaidLoanBal = tL9701Vo.get("Amount").isEmpty()
									|| tL9701Vo.get("Amount") == null ? BigDecimal.ZERO
											: new BigDecimal(tL9701Vo.get("Amount"));
							if (unpaidLoanBal.compareTo(BigDecimal.ZERO) == 0) {
								continue;
							}
						}
					}
//					if (detailCounts > 0) {
//						reportTotal();
//					}
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
//
//				if (this.getNowPage() > tempPage && tempPage > 0) {
//					this.print(1, 1, " ");
//					tempPage = getNowPage();
//				}
//				this.info("tempPage=" +tempPage);

				if (tL9701Vo.get("DB").equals("1")) {
					printDetail1(tL9701Vo);
					detailCounts++;
				}

			}
		} else {

			this.print(1, 20, "*******    查無資料   ******");
		}

		this.close();

	}

	private void printDetail1(Map<String, String> tL9701Vo) {

		// 撥款
		this.print(1, 2, tL9701Vo.get("BormNo"));
		// 入帳日
		this.print(0, 9, showRocDate(tL9701Vo.get("EntryDate"), 1));
		// 交易內容
		this.print(0, 28, tL9701Vo.get("Desc"), "C");
		// 交易金額
		this.print(0, 49, formatAmt(tL9701Vo.get("TxAmt"), 0), "R"); //

		// 暫收借
//		BigDecimal tempAmt = new BigDecimal(tL9701Vo.get("TempAmt"));
//		BigDecimal tempDbAmt = tempAmt.compareTo(BigDecimal.ZERO) > 0 ? tempAmt : BigDecimal.ZERO;
//		BigDecimal tempCrAmt = tempAmt.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO.subtract(tempAmt)
//				: BigDecimal.ZERO;
		this.print(0, 67, formatAmt(tL9701Vo.get("TempAmt"), 0), "R");
		// 本金
		this.print(0, 83, formatAmt(tL9701Vo.get("Principal"), 0), "R");

		// 利息
		this.print(0, 98, formatAmt(tL9701Vo.get("Interest"), 0), "R");
		// 違約金
		this.print(0, 114, formatAmt(tL9701Vo.get("BreachAmt"), 0), "R");
		// 費用
		this.print(0, 130, formatAmt(tL9701Vo.get("FeeAmt"), 0), "R");
		// 短繳
		this.print(0, 146, formatAmt(tL9701Vo.get("ShortAmt"), 0), "R");
		// 暫收貸
		this.print(0, 162, formatAmt(tL9701Vo.get("Overflow"), 0), "R");

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

		divider();
		this.print(1, 66, "小計：");
		this.print(0, 83, formatAmt(principalTotal, 0), "R");
		this.print(0, 98, formatAmt(interestTotal, 0), "R");
		this.print(0, 114, formatAmt(breachAmtTotal, 0), "R");
		this.print(0, 130, formatAmt(feeAmtTotal, 0), "R");

		principalTotal = BigDecimal.ZERO;
		interestTotal = BigDecimal.ZERO;
		breachAmtTotal = BigDecimal.ZERO;
		feeAmtTotal = BigDecimal.ZERO;
	}

}
