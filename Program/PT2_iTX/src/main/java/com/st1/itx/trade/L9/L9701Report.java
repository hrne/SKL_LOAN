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
import com.st1.itx.util.common.data.ReportVo;

@Component
@Scope("prototype")

public class L9701Report extends MakeReport {

	@Autowired
	L9701ServiceImpl l9701ServiceImpl;

	// 製表日期
	private String NowDate;
	// 製表時間
	private String NowTime;

	// 客戶姓名
	private String custName;

	// 額度號碼
	private String facmNo;

	// 客戶主要擔保品地址
	private String clAddr;

	int entday = 0;
	int stday = 0;

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


	String nextPageText = "=====  續下頁  =====";
	String endText = "=====  報  表  結  束  =====";
	
	@Override
	public void printHeader() {

		this.setFontSize(8);

		this.print(-2, this.getMidXAxis(), "客 戶 往 來 本 息 明 細 表", "C");

		this.print(-3, this.getMidXAxis(), showRocDate(this.titaVo.get("ENTDY"), 0), "C");

		this.print(-4, 138, "印表日期：" + showRocDate(this.NowDate, 1) + " " + showTime(this.NowTime), "L");

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

	private void printFacHead() {

			String tmpFacmNo = String.format("%03d", Integer.valueOf(facmNo));

			this.print(1, 1, " ");
			this.print(1, 1, "額度　　 : " + tmpFacmNo);
			this.print(0, 22, "押品地址 : " + clAddr);
			divider();
			this.print(1, 8, "入帳日期");
			this.print(0, 24, "交易內容","C");
			this.print(0, 35, "計息本金");
			this.print(0, 52, "計息期間");
			this.print(0, 69, "利率");
			this.print(0, 79, "交易金額");
			this.print(0, 96, "本金");
			this.print(0, 111, "利息");
			this.print(0, 125, "違約金");
			this.print(0, 141, "費用");
			this.print(0, 155, "短繳");
			divider();
//			this.print(1, 7,
//					"　入帳日　　　計息本金　　　　計息期間　　　　利率　　　　交易金額          本金                 利息　    　　　違約金　　　　　費用　　　　  溢短繳   ");
//			this.print(1, 7, "－－－－－　－－－－－－　－－－－－－－－　－－－－　－－－－－－　－－－－－－  －－－－－－－　－－－－－－－　－－－－－－　－－－－－－");
//		}

	}

	/**
	 * 分割線
	 * 
	 */
	public void divider() {
		this.print(1, 7, "－－－－－");
		this.print(0, 24, "－－－－－－","C");
		this.print(0, 33, "－－－－－－");
		this.print(0, 48, "－－－－－－－－");
		this.print(0, 67, "－－－－");
		this.print(0, 77, "－－－－－－");
		this.print(0, 92, "－－－－－－");
		this.print(0, 107, "－－－－－－");
		this.print(0, 122, "－－－－－－");
		this.print(0, 137, "－－－－－－");
		this.print(0, 152, "－－－－－－");
	}

	public void exec(TitaVo titaVo, List<BaTxVo> listBaTxVo) throws LogicException {

		entday = titaVo.getEntDyI();

		this.NowDate = dDateUtil.getNowStringRoc();
		this.NowTime = dDateUtil.getNowStringTime();

		List<Map<String, String>> listL9701 = null;

		try {
			listL9701 = l9701ServiceImpl.doQuery1(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("L9701ServiceImpl.LoanBorTx error = " + errors.toString());
		}

		String tradeReportName = "客戶往來本息明細表（額度）";

		this.info("entday="+entday);
		this.info("titaVo.getEntDyI()=" + titaVo.getEntDyI());
		this.info("getBrno="+titaVo.getBrno());
		this.info("getKinbr="+titaVo.getKinbr());

	
		ReportVo reportVo = ReportVo.builder().setRptDate(titaVo.getEntDyI()).setBrno(titaVo.getKinbr())
				.setRptCode("L9701").setRptItem(tradeReportName).setSecurity("")
				.setRptSize("A4").setPageOrientation("L").build();
		
		
		this.open(titaVo, reportVo);
		
		this.custName = "";
		this.facmNo = "";
		this.clAddr = "";

		boolean isFirst = true;

		int detailCounts = 0;
		if (listL9701 != null && listL9701.size() > 0) {

			for (Map<String, String> tL9701Vo : listL9701) {
				
				if (this.NowRow - 7 >= 40) {
					this.print(1, this.getMidXAxis(), nextPageText,"C"); //
					this.newPage();
					this.print(1, 1, " "); //
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
					this.custName = tL9701Vo.get("CustName");
					this.facmNo = tL9701Vo.get("FacmNo");
					this.clAddr = tL9701Vo.get("Location");
					if (isFirst) {
						printFacHead();
						isFirst = false;
					}
					if (tL9701Vo.get("DB").equals("2")) {
						printFacEnd(tL9701Vo, listBaTxVo);
						detailCounts = 0;
						isFirst = true;
					}
				}

				if (tL9701Vo.get("DB").equals("1")) {
					printDetail(tL9701Vo);
					detailCounts++;
				}
			}
		} else {

			this.print(1, 20, "*******    查無資料   ******");
		}

		this.close();

	}

	private void printDetail(Map<String, String> tL9701Vo) {

		// 入帳日
		this.print(1, 7, showRocDate(tL9701Vo.get("EntryDate"), 1));
		// 交易內容
		this.print(0, 24, tL9701Vo.get("Desc"), "C");
		// 計息本金
		if (!"0".equals(tL9701Vo.get("Amount"))) {
			this.print(0, 44, formatAmt(tL9701Vo.get("Amount"), 0), "R");
		}
		// 計息期間
		this.print(0, 48,
				showRocDate(tL9701Vo.get("IntStartDate"), 3) + "-" + showRocDate(tL9701Vo.get("IntEndDate"), 3)); // 計息期間
		// 利率
		if (!"0".equals(tL9701Vo.get("Rate"))) {
			this.print(0, 74, formatAmt(tL9701Vo.get("Rate"), 4), "R");
		}

		// 交易金額
		this.print(0, 89, formatAmt(tL9701Vo.get("TxAmt"), 0), "R");
		// 本金
		this.print(0, 103, formatAmt(tL9701Vo.get("Principal"), 0), "R");
		// 利息
		this.print(0, 118, formatAmt(tL9701Vo.get("Interest"), 0), "R");
		// 違約金
		this.print(0, 133, formatAmt(tL9701Vo.get("BreachAmt"), 0), "R");
		// 費用
		this.print(0, 148, formatAmt(tL9701Vo.get("FeeAmt"), 0), "R");
		// 短繳
		this.print(0, 163, formatAmt(tL9701Vo.get("ShortAmt"), 0), "R");

		principal = new BigDecimal(tL9701Vo.get("Principal"));
		interest = new BigDecimal(tL9701Vo.get("Interest"));
		breachAmt = new BigDecimal(tL9701Vo.get("BreachAmt"));
		feeAmt = new BigDecimal(tL9701Vo.get("FeeAmt"));
		principalTotal = principalTotal.add(principal);
		interestTotal = interestTotal.add(interest);
		breachAmtTotal = breachAmtTotal.add(breachAmt);
		feeAmtTotal = feeAmtTotal.add(feeAmt);
	}

	private void printFacEnd(Map<String, String> tL9701Vo, List<BaTxVo> listBaTxVo) {
		shortFall = BigDecimal.ZERO;
		excessive = BigDecimal.ZERO;
		
		int tmpFacmNo = Integer.parseInt(tL9701Vo.get("FacmNo"));
		loanBal = new BigDecimal(tL9701Vo.get("Amount"));
		if (listBaTxVo != null && listBaTxVo.size() != 0) {
			for (BaTxVo tBaTxVo : listBaTxVo) {
				if (tBaTxVo.getFacmNo() == tmpFacmNo) {
					if (tBaTxVo.getDataKind() == 1 && tBaTxVo.getRepayType() == 1) {
						shortFall = shortFall.add(tBaTxVo.getUnPaidAmt());
					}
					if (tBaTxVo.getDataKind() == 3) {
						excessive = excessive.add(tBaTxVo.getUnPaidAmt());
					}
				}
			}
		}

		divider();
		this.print(1, 9, "至" + showRocDate(entday, 1) + " 當日餘額：");
		this.print(0, 44, formatAmt(loanBal, 0), "R"); // 放款餘額
		this.print(0, 52, "累溢短收：");
		this.print(0, 74, formatAmt(excessive.subtract(shortFall), 0), "R"); // 累溢短收
		this.print(0, 84, "小計：");
		this.print(0, 103, formatAmt(principalTotal, 0), "R");
		this.print(0, 118, formatAmt(interestTotal, 0), "R");
		this.print(0, 133, formatAmt(breachAmtTotal, 0), "R");
		this.print(0, 148, formatAmt(feeAmtTotal, 0), "R");

		principalTotal = BigDecimal.ZERO;
		interestTotal = BigDecimal.ZERO;
		breachAmtTotal = BigDecimal.ZERO;
		feeAmtTotal = BigDecimal.ZERO;
	}

}
