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
import com.st1.itx.db.domain.CustNotice;
import com.st1.itx.db.domain.CustNoticeId;
import com.st1.itx.db.service.CustNoticeService;
import com.st1.itx.db.service.springjpa.cm.L9701ServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.BaTxVo;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class L9701Report extends MakeReport {

	@Autowired
	L9701ServiceImpl l9701ServiceImpl;

	@Autowired
	private CustNoticeService sCustNoticeService;

	@Autowired
	Parse parse;
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

	public int headNo = 1;

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

	int rowMax = 60;

	@Override
	public void printHeader() {

		this.setFontSize(8);

		this.print(-2, this.getMidXAxis(), "放 款 往 來 交 易 明 細 表", "C");

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

		this.print(-6, 1, "戶號　　 : " + iCUSTNO + " " + custName);

		this.setBeginRow(7);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(rowMax);

	}

	private void printFacHead() {

		String tmpFacmNo = String.format("%03d", Integer.valueOf(facmNo));

		this.print(1, 1, " ");
		this.print(1, 1, "額度　　 : " + tmpFacmNo);

		this.print(0, 22, "押品地址 : " + clAddr);

		divider();
		this.print(1, 1, "");
		this.print(0, 5, "0".equals(titaVo.get("CorrectType")) ? "" : "會計日期");// 未訂正的時候顯示
		this.print(0, 16, "入帳日期");
		this.print(0, 26, "繳款方式");
		this.print(0, 38, "交易內容", "C");
		this.print(0, 47, "放款本金");
		this.print(0, 60, "計息期間");
		this.print(0, 74, "利率");
		this.print(0, 84, "交易金額");
		this.print(0, 101, "本金");
		this.print(0, 111, "利息");
		this.print(0, 123, "違約金");
		this.print(0, 138, "費用");
		this.print(0, 149, "累溢短收");
//		this.print(0, 141, "短繳");
//		this.print(0, 155, "溢繳");
		this.print(0, 160, "0".equals(titaVo.get("CorrectType")) ? "" : "訂正別");// 未訂正的時候顯示代號
		divider();

	}

	private void printFacHead2() {

		divider();
		this.print(1, 1, "");
		this.print(0, 5, "0".equals(titaVo.get("CorrectType")) ? "" : "會計日期");// 未訂正的時候顯示
		this.print(0, 16, "入帳日期");
		this.print(0, 26, "繳款方式");
		this.print(0, 38, "交易內容", "C");
		this.print(0, 47, "放款本金");
		this.print(0, 60, "計息期間");
		this.print(0, 74, "利率");
		this.print(0, 84, "交易金額");
		this.print(0, 101, "本金");
		this.print(0, 111, "利息");
		this.print(0, 123, "違約金");
		this.print(0, 138, "費用");
		this.print(0, 149, "累溢短收");
//		this.print(0, 141, "短繳");
//		this.print(0, 155, "溢繳");
		this.print(0, 160, "0".equals(titaVo.get("CorrectType")) ? "" : "訂正別");// 未訂正的時候顯示代號
		divider();

	}

	/**
	 * 分割線
	 * 
	 */
	public void divider() {

		this.print(1, 3,
				"－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");
	}

	public void exec(TitaVo titaVo, List<BaTxVo> listBaTxVo) throws LogicException {

		entday = titaVo.getEntDyI();

		this.NowDate = dDateUtil.getNowStringRoc();
		this.NowTime = dDateUtil.getNowStringTime();

		List<Map<String, String>> listL9701 = null;

		try {
			listL9701 = l9701ServiceImpl.doQuery1(false, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("L9701ServiceImpl.LoanBorTx error = " + errors.toString());
		}

		// 客戶往來本息明細表（額度）
		String tradeReportName = "放款往來交易明細表";

		this.info("entday=" + entday);
		this.info("titaVo.getEntDyI()=" + titaVo.getEntDyI());
		this.info("getBrno=" + titaVo.getBrno());
		this.info("getKinbr=" + titaVo.getKinbr());

		ReportVo reportVo = ReportVo.builder().setRptDate(titaVo.getEntDyI()).setBrno(titaVo.getKinbr())
				.setRptCode("L9701").setRptItem(tradeReportName).setRptSize("A4").setPageOrientation("L").build();

		this.open(titaVo, reportVo);

		this.custName = "";
		this.facmNo = "";
		this.clAddr = "";

		int detailCounts = 0;
		int cntAll = 0;
//		int cntFirst = 0;
		if (listL9701 != null && listL9701.size() > 0) {

			for (Map<String, String> tL9701Vo : listL9701) {

				int custNo = parse.stringToInteger(tL9701Vo.get("CustNo"));
				int facmNo = parse.stringToInteger(tL9701Vo.get("FacmNo"));

				CustNotice lCustNotice = new CustNotice();
				CustNoticeId lCustNoticeId = new CustNoticeId();

				lCustNoticeId.setCustNo(custNo);
				lCustNoticeId.setFacmNo(facmNo);
				lCustNoticeId.setFormNo("L9701");
				lCustNotice = sCustNoticeService.findById(lCustNoticeId, titaVo);

				// paper為N 表示不印
				if (lCustNotice == null) {
				} else {
					if ("N".equals(lCustNotice.getPaperNotice())) {
						continue;
					}
				}

				String nextFacmNo = facmNo + "";
				String lastsFacmNo = facmNo + "";

				// 下一個額度
				if (cntAll + 1 < listL9701.size()) {
					nextFacmNo = listL9701.get(cntAll + 1).get("FacmNo");
				}

				// 上一個額度
				if (cntAll > 0 && facmNo > 0) {
					lastsFacmNo = listL9701.get(cntAll - 1).get("FacmNo");
				}

				// 計算筆數
				cntAll++;

				// 額度0跳過
				if (facmNo == 0) {
					continue;
				}

				// 不是額度0後的開始計算
//				cntFirst++;

				this.custName = tL9701Vo.get("CustName");
				this.facmNo = tL9701Vo.get("FacmNo");
				this.clAddr = tL9701Vo.get("Location");
				printHead();

				this.info("lastsFacmNo vs facmno = " + lastsFacmNo + " vs " + tL9701Vo.get("FacmNo"));
				this.info("nextFacmNo vs facmno = " + nextFacmNo + " vs " + tL9701Vo.get("FacmNo"));
				if (!this.facmNo.equals(nextFacmNo)) {
					// 無交易明細且無餘額
					if (detailCounts == 0) {
						if (!this.facmNo.equals(nextFacmNo) || detailCounts == listL9701.size()) {
							BigDecimal unpaidLoanBal = tL9701Vo.get("Amount").isEmpty()
									|| tL9701Vo.get("Amount") == null ? BigDecimal.ZERO
											: new BigDecimal(tL9701Vo.get("Amount"));
							if (unpaidLoanBal.compareTo(BigDecimal.ZERO) == 0) {
								continue;
							}
						}
					}

					// 當前額度0 下一筆額度不是0的
					if ("0".equals(this.facmNo) && !"0".equals(tL9701Vo.get("FacmNo"))) {
						divider();
						detailCounts = 0;
//						isFirst = true;
					}

					this.custName = tL9701Vo.get("CustName");
					this.facmNo = tL9701Vo.get("FacmNo");
					this.clAddr = tL9701Vo.get("Location");

				}

				printDetail(tL9701Vo);
				detailCounts++;

				// 每一筆都會判斷是否換頁
				this.headNo = 0;
//				this.nextPage(0);

				// 先判斷大於43行 (是因為要結算留位置並換換下一頁新的額度
				// 再判斷1 上一個額度與當前額度不同
				boolean isNotSameFacmNo = tL9701Vo.get("FacmNo").equals(nextFacmNo);

				this.info("isNotSameFacmNo = " + (isNotSameFacmNo ? "Yes" : "No"));


				// 額度不同 且 列數大於46，因36~43列數空間不足以列印總計加錶頭列數所以要換頁用表頭1
				if (!isNotSameFacmNo && (this.NowRow > 46)) {
					this.info("type3");
					printFacEnd(this.facmNo);
					this.nextPage(1);
					continue;
				}
				if (isNotSameFacmNo && this.NowRow >= 52) {
					this.info("type1");
					this.nextPage(1);
					continue;
				}

				if (!isNotSameFacmNo && this.NowRow < 52) {
					printFacEnd(this.facmNo);
					this.nextPage(1);
					continue;

				}


				// 最後一筆 列印結束報表
				if (cntAll == listL9701.size()) {
					printFacEnd(this.facmNo);

				}

			}
		} else {
			printFacHead2();
			this.print(1, 20, "*******    查無資料   ******");
		}
		// 列印結束報表
		this.print(-56, this.getMidXAxis(), endText, "C");

		this.close();

	}

	private void printDetail(Map<String, String> tL9701Vo) {
		this.print(1, 1, "");
	
		// 會計日期
		this.print(0, 4, "0".equals(titaVo.get("CorrectType")) ? "  " : showRocDate(tL9701Vo.get("AcDate"), 1));

		// 入帳日
		this.print(0, 15, showRocDate(tL9701Vo.get("EntryDate"), 1));

		// 繳款方式RepayItem
		this.print(0, 26, tL9701Vo.get("RepayItem"));

		// 交易內容
		this.print(0, 34, tL9701Vo.get("Desc"), "L");
		// 放款本金
		if (!"0".equals(tL9701Vo.get("Amount"))) {
			this.print(0, 55, formatAmt(tL9701Vo.get("Amount"), 0), "R");
		}
		// 計息期間
		this.print(0, 56,
				showRocDate(tL9701Vo.get("IntStartDate"), 3) + "-" + showRocDate(tL9701Vo.get("IntEndDate"), 3)); // 計息期間
		// 利率
		if (!"0".equals(tL9701Vo.get("Rate"))) {
			this.print(0, 79, formatAmt(tL9701Vo.get("Rate"), 4), "R");
		}

		// 交易金額
		this.print(0, 92, formatAmt(tL9701Vo.get("TxAmt"), 0), "R");
		// 本金Principal
		this.print(0, 105, formatAmt(tL9701Vo.get("Principal"), 0), "R");
		// 利息
		this.print(0, 115, formatAmt(tL9701Vo.get("Interest"), 0), "R");
		// 違約金
		this.print(0, 128, formatAmt(tL9701Vo.get("BreachAmt"), 0), "R");
		// 費用
		this.print(0, 142, formatAmt(tL9701Vo.get("FeeAmt"), 0), "R");
		//累溢短收
		this.print(0, 156, formatAmt(tL9701Vo.get("Excessive"), 0), "R");
//		// 短繳
//		this.print(0, 156, formatAmt(tL9701Vo.get("ShortAmt"), 0), "R");
//		// 溢繳
//		this.print(0, 170, formatAmt(tL9701Vo.get("Overflow"), 0), "R");
		// 訂正別TitaHCode

		this.print(0, 165, "0".equals(titaVo.get("CorrectType")) ? "  " : tL9701Vo.get("TitaHCode"), "R");

		BigDecimal tmpLoanBal = new BigDecimal(tL9701Vo.get("Amount"));

		loanBal = tmpLoanBal.compareTo(BigDecimal.ZERO) == 0 ? loanBal : tmpLoanBal;

		principal = new BigDecimal(tL9701Vo.get("Principal"));
		interest = new BigDecimal(tL9701Vo.get("Interest"));
		breachAmt = new BigDecimal(tL9701Vo.get("BreachAmt"));
		feeAmt = new BigDecimal(tL9701Vo.get("FeeAmt"));
		principalTotal = principalTotal.add(principal);
		interestTotal = interestTotal.add(interest);
		breachAmtTotal = breachAmtTotal.add(breachAmt);
		feeAmtTotal = feeAmtTotal.add(feeAmt);
	}

	private void printFacEnd(String facmNo) {
		List<Map<String, String>> listL9701 = null;

		try {
			listL9701 = l9701ServiceImpl.doQuery1(true, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("L9701ServiceImpl.LoanBorTx error = " + errors.toString());
		}

		// 只抓每個額度的最後一筆
		for (Map<String, String> tL9701Vo : listL9701) {
			if (facmNo.equals(tL9701Vo.get("FacmNo"))) {
				excessive = getBigDecimal(tL9701Vo.get("Excessive"));
			}
		}

		divider();

		
		this.print(1, 21, "至" + showRocDate(entday, 1) + " 當日餘額：");
		this.print(0, 55, formatAmt(loanBal, 0), "R"); // 放款餘額
		this.print(0, 64, "累溢短收：");
//		this.print(0, 72, formatAmt(excessive.subtract(shortFall), 0), "R"); // 累溢短收
		this.print(0, 84, formatAmt(excessive, 0), "R"); // 累溢短收
		this.print(0, 86, "小計：");
		this.print(0, 104, formatAmt(principalTotal, 0), "R");
		this.print(0, 115, formatAmt(interestTotal, 0), "R");
		this.print(0, 128, formatAmt(breachAmtTotal, 0), "R");
		this.print(0, 142, formatAmt(feeAmtTotal, 0), "R");

		loanBal = BigDecimal.ZERO;
		excessive = BigDecimal.ZERO;
		principalTotal = BigDecimal.ZERO;
		interestTotal = BigDecimal.ZERO;
		breachAmtTotal = BigDecimal.ZERO;
		feeAmtTotal = BigDecimal.ZERO;
	}

	private void nextPage(int headNo) {

		if (headNo == 1 || headNo == 2) {
			this.print(-56, this.getMidXAxis(), nextPageText, "C");
			this.newPage();
		}

		this.headNo = headNo;

	}

	private void printHead() {

		switch (headNo) {
		case 1:
			printFacHead();
			break;
		case 2:
			printFacHead2();
			break;
		case 11:
			printFacHead();
			break;

		}

	}

}
