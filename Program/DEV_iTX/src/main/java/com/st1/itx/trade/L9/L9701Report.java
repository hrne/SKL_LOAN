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
import com.st1.itx.util.common.data.BaTxVo;
import com.st1.itx.util.parse.Parse;

/**
 * L9701Report 客戶往來本息明細表
 * 
 * @author ST1 Chih Wei
 *
 */
@Component
@Scope("prototype")
public class L9701Report extends MakeReport {

	@Autowired
	L9701ServiceImpl l9701ServiceImpl;

	@Autowired
	CustNoticeCom custNoticeCom;

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

	int ptfg = 0; // ???

	// 於本次產生報表中是否已列印未繳資料 0:尚未列印 1:已列印
	int batxFg = 0;

	int entday = 0;
	int stday = 0;

	int maturityDate = 0; // 到期日
	BigDecimal loanBal = BigDecimal.ZERO; // 放款餘額
	BigDecimal shortInt = BigDecimal.ZERO; // 短收利息
	BigDecimal shortPrin = BigDecimal.ZERO; // 短收本金
	BigDecimal overflow = BigDecimal.ZERO; // 溢收
	BigDecimal repaidPrin = BigDecimal.ZERO; // 應繳本金
	BigDecimal repaidInt = BigDecimal.ZERO; // 應繳利息
	BigDecimal repaidExp = BigDecimal.ZERO; // 應繳費用

	BigDecimal loanBalTotal = BigDecimal.ZERO; // 放款餘額合計
	BigDecimal shortIntTotal = BigDecimal.ZERO; // 短收利息合計
	BigDecimal shortPrinTotal = BigDecimal.ZERO; // 短收本金合計
	BigDecimal overflowTotal = BigDecimal.ZERO; // 溢收合計
	BigDecimal repaidPrinTotal = BigDecimal.ZERO; // 應繳本金合計
	BigDecimal repaidIntTotal = BigDecimal.ZERO; // 應繳利息合計
	BigDecimal repaidExpTotal = BigDecimal.ZERO; // 應繳費用合計

	@Override
	public void printHeader() {

		this.print(-2, 70, "客 戶 往 來 本 息 明 細 表", "C");

		this.print(-3, 70, showRocDate(this.titaVo.get("ENTDY"), 0), "C");

		this.print(-4, 110, "印表日期：" + showRocDate(this.NowDate, 1) + " " + showTime(this.NowTime));

		String iTYPE = titaVo.get("DateType");
		String iSDAY = showRocDate(titaVo.get("BeginDate"), 1);
		String iEDAY = showRocDate(titaVo.get("EndDate"), 1);

		if (iTYPE.equals("1")) {
			this.print(-5, 1, "入帳日期 : " + iSDAY + " - " + iEDAY);
		} else {
			this.print(-5, 1, "會計日期 : " + iSDAY + " - " + iEDAY);
		}

		this.print(-5, 110, "頁　　次：" + Integer.toString(this.getNowPage()));

		String iCUSTNO = titaVo.get("CustNo");

		if (this.facmNo.equals("")) {
			this.print(-6, 1, "戶號     : " + iCUSTNO);
		} else {
			this.print(-6, 1, "戶號     : " + iCUSTNO + " " + custName);
		}

		if (ptfg == 0) {
			printFacHead();
			this.setBeginRow(12);
		} else if (ptfg == 1) {
			printCustHead();
			this.setBeginRow(11);
		} else {
			this.setBeginRow(7);
		}

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(40);

	}

	private void printFacHead() {

		String tmpFacmNo = String.format("%03d", Integer.valueOf(facmNo));

		this.print(1, 1, " ");
		this.print(1, 1, "額度     : " + tmpFacmNo);
		this.print(0, 22, "押品地址 : " + clAddr);
		this.print(1, 7, "－－－－－　－－－－－－　－－－－－－－－　－－－－　－－－－－－　－－－－－－－　－－－－－－－　－－－－－－　－－－－－－　－－－－－");
		this.print(1, 7, "　入帳日　　　計息本金　　　　計息期間　　　　利率　　　　利息　　　　　逾期息　　　　　違約金　　　　　本金　　　　本息合計　　　應繳日");
		this.print(1, 7, "－－－－－　－－－－－－　－－－－－－－－　－－－－　－－－－－－　－－－－－－－　－－－－－－－　－－－－－－　－－－－－－　－－－－－");

	}

	private void printCustHead() {
		this.print(1, 1, " ");
		this.print(1, 7, "至" + showRocDate(entday, 1) + "未還本金餘額：");
		this.print(1, 7, "　到期日　　　　放款餘額　　　　短收利息　  　短收本金　　　　溢收　　　　　應繳本金　　　　　應繳利息　　　　　應繳費用");
		this.print(1, 7, "－－－－－　－－－－－－－－　－－－－－－　－－－－－－　－－－－－－　－－－－－－－－　－－－－－－－－　－－－－－－－－");

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

		this.open(titaVo, entday, titaVo.getKinbr(), "L9701", "客戶往來本息明細表", "", "A4", "L");

		this.custName = "";
		this.facmNo = "";
		this.clAddr = "";

		Map<String, String> lastL9701Vo = null;

		boolean isFirst = true;

		int detailCounts = 0;

		for (Map<String, String> tL9701Vo : listL9701) {

			// F11 CustNo
			// F13 FacmNo
			// INPUT #CustNo

//			// 檢查 CustNoticeCom 確認該筆戶號額度是否能產出郵寄文件
//			int recordCustNo = parse.stringToInteger(tL9701Vo.get("F11"));
//			int recordFacmNo = parse.stringToInteger(tL9701Vo.get("F13"));
//			if (!custNoticeCom.checkIsLetterSendable(titaVo.get("CustNo"), recordCustNo, recordFacmNo,
//					this.getRptCode(), titaVo)) {
//				continue;
//			}

			if (!this.facmNo.equals(tL9701Vo.get("F13"))) {

				ptfg = 1;

				if (!facmNo.isEmpty() && detailCounts > 0) {
					printCustHead();

					printEndData(lastL9701Vo, listBaTxVo);

					printEndDataSum();
				}

				ptfg = 0;
				batxFg = 0;
				stday = 0;

				detailCounts = 0;

				if (tL9701Vo.get("F15").equals("2")) {
					BigDecimal unpaidLoanBal = tL9701Vo.get("F1") == null ? BigDecimal.ZERO
							: new BigDecimal(tL9701Vo.get("F1"));

					if (unpaidLoanBal.compareTo(BigDecimal.ZERO) == 0) {
						detailCounts = 1;
						continue;
					}
				}

				this.custName = tL9701Vo.get("F12");
				this.facmNo = tL9701Vo.get("F13");
				this.clAddr = tL9701Vo.get("F14");

				if (!facmNo.isEmpty() && !isFirst) {
					printFacHead();
				}

			}

			if (tL9701Vo.get("F15").equals("1")) {
				printDetail1(tL9701Vo);
				detailCounts++;
			} else {
				if (batxFg == 0) {
					if (listBaTxVo != null && listBaTxVo.size() != 0) {
						detailCounts = printUnpaidDetail(detailCounts, listBaTxVo);
					}
					batxFg = 1;
				}
			}
			lastL9701Vo = tL9701Vo;

			isFirst = false;
		}

		ptfg = 1;

		if (!facmNo.isEmpty() && detailCounts > 0) {
			printCustHead();

			printEndData(lastL9701Vo, listBaTxVo);

			printEndDataSum();
		}

		this.close();
	}

	/**
	 * 列印已收明細
	 * 
	 * @param tL9701Vo tL9701Vo
	 */
	private void printDetail1(Map<String, String> tL9701Vo) {
		this.print(1, 7, showRocDate(tL9701Vo.get("F0"), 1));
		this.print(0, 29, formatAmt(tL9701Vo.get("F1"), 0), "R"); // 計息本金
		this.print(0, 31, showRocDate(tL9701Vo.get("F2"), 3) + "-" + showRocDate(tL9701Vo.get("F3"), 3)); // 計息期間
		this.print(0, 54, formatAmt(tL9701Vo.get("F4"), 4), "R"); // 利率
		this.print(0, 67, formatAmt(tL9701Vo.get("F5"), 0), "R"); // 利息
		this.print(0, 82, formatAmt(tL9701Vo.get("F6"), 0), "R"); // 逾期息
		this.print(0, 96, formatAmt(tL9701Vo.get("F7"), 0), "R"); // 違約金
		this.print(0, 109, formatAmt(tL9701Vo.get("F8"), 0), "R"); // 本金
		this.print(0, 122, formatAmt(tL9701Vo.get("F9"), 0), "R"); // 本息合計
//		 this.print(0, 124, showRocDate(tL9701Vo.get("F10"), 1)); // 未繳才會有應繳日 但這裡只出已繳
	}

	/**
	 * 列印應收未收資料
	 * 
	 * @param detailCounts detailCounts
	 * @param listBaTxVo   listBaTxVo
	 * @return detailCounts
	 */
	private int printUnpaidDetail(int detailCounts, List<BaTxVo> listBaTxVo) {

		for (BaTxVo tBaTxVo : listBaTxVo) {
			if (tBaTxVo.getFacmNo() == Integer.valueOf(this.facmNo)) {
				if (tBaTxVo.getDataKind() == 2 && tBaTxVo.getPayIntDate() <= entday) {
					this.print(1, 29, formatAmt(tBaTxVo.getAmount(), 0), "R"); // 計息本金
					this.print(0, 31,
							showRocDate(tBaTxVo.getIntStartDate(), 3) + "-" + showRocDate(tBaTxVo.getIntEndDate(), 3)); // 計息期間
					this.print(0, 54, formatAmt(tBaTxVo.getIntRate(), 4), "R"); // 利率
					this.print(0, 67, formatAmt(tBaTxVo.getInterest(), 0), "R"); // 利息
					this.print(0, 82, formatAmt(tBaTxVo.getDelayInt(), 0), "R"); // 逾期息
					this.print(0, 96, formatAmt(tBaTxVo.getBreachAmt(), 0), "R"); // 違約金
					this.print(0, 109, formatAmt(tBaTxVo.getPrincipal(), 0), "R"); // 本金
					this.print(0, 122, formatAmt(tBaTxVo.getUnPaidAmt(), 0), "R"); // 本息合計
					this.print(0, 124, showRocDate(tBaTxVo.getPayIntDate(), 1)); // 應繳日
					detailCounts++;
				}
			}
		}
		return detailCounts;
	}

	/**
	 * 列印截止至某日餘額
	 * 
	 * @param tL9701Vo
	 * @param listBaTxVo
	 */
	private void printEndData(Map<String, String> tL9701Vo, List<BaTxVo> listBaTxVo) {

		int tmpFacmNo = Integer.parseInt(this.facmNo);
		maturityDate = Integer.valueOf(tL9701Vo.get("F0")) - 19110000;
		loanBal = tL9701Vo.get("F1") == null ? BigDecimal.ZERO : new BigDecimal(tL9701Vo.get("F1"));

		if (listBaTxVo != null && listBaTxVo.size() != 0) {
			for (BaTxVo tBaTxVo : listBaTxVo) {
				if (tBaTxVo.getFacmNo() == tmpFacmNo) {

					if (tBaTxVo.getDataKind() == 1) {

						if (tBaTxVo.getReceivableFlag() == 4) {

							shortInt = shortInt
									.add(tBaTxVo.getInterest().add(tBaTxVo.getDelayInt()).add(tBaTxVo.getBreachAmt()));
							shortPrin = shortPrin.add(tBaTxVo.getPrincipal());

						} else {

							repaidExp = repaidExp.add(tBaTxVo.getUnPaidAmt());
						}
					} else if (tBaTxVo.getDataKind() == 3) {

						overflow = overflow.add(tBaTxVo.getUnPaidAmt());

					} else if (tBaTxVo.getDataKind() == 2 && tBaTxVo.getPayIntDate() <= entday) {

						repaidPrin = repaidPrin.add(tBaTxVo.getPrincipal());
						repaidInt = repaidInt
								.add(tBaTxVo.getInterest().add(tBaTxVo.getDelayInt()).add(tBaTxVo.getBreachAmt()));

					}
				}
			}
		}

		printEndDataDetail();
	}

	/**
	 * 列印截止至某日餘額明細
	 */
	private void printEndDataDetail() {
		if (loanBal.add(shortInt).add(shortPrin).add(overflow).add(repaidPrin).add(repaidInt).add(repaidExp)
				.compareTo(BigDecimal.ZERO) == 0) {
			return;
		}

		this.print(1, 7, showRocDate(maturityDate, 1)); // 到期日
		this.print(0, 32, formatAmt(loanBal, 0), "R"); // 放款餘額
		this.print(0, 45, formatAmt(shortInt, 0), "R"); // 短收利息
		this.print(0, 58, formatAmt(shortPrin, 0), "R"); // 短收本金
		this.print(0, 71, formatAmt(overflow, 0), "R"); // 溢收
		this.print(0, 87, formatAmt(repaidPrin, 0), "R"); // 應繳本金
		this.print(0, 103, formatAmt(repaidInt, 0), "R"); // 應繳利息
		this.print(0, 120, formatAmt(repaidExp, 0), "R"); // 應繳費用

		loanBalTotal = loanBalTotal.add(loanBal);
		shortIntTotal = shortIntTotal.add(shortInt);
		shortPrinTotal = shortPrinTotal.add(shortPrin);
		overflowTotal = overflowTotal.add(overflow);
		repaidPrinTotal = repaidPrinTotal.add(repaidPrin);
		repaidIntTotal = repaidIntTotal.add(repaidInt);
		repaidExpTotal = repaidExpTotal.add(repaidExp);

		loanBal = BigDecimal.ZERO;
		shortInt = BigDecimal.ZERO;
		shortPrin = BigDecimal.ZERO;
		overflow = BigDecimal.ZERO;
		repaidPrin = BigDecimal.ZERO;
		repaidInt = BigDecimal.ZERO;
		repaidExp = BigDecimal.ZERO;
	}

	/**
	 * 列印截止至某日餘額加總
	 */
	private void printEndDataSum() {

		this.print(1, 7, "－－－－－　－－－－－－－－　－－－－－－　－－－－－－　－－－－－－　－－－－－－－－　－－－－－－－－　－－－－－－－－");
		this.print(1, 32, formatAmt(loanBalTotal, 0), "R"); // 放款餘額
		this.print(0, 45, formatAmt(shortIntTotal, 0), "R"); // 短收利息
		this.print(0, 58, formatAmt(shortPrinTotal, 0), "R"); // 短收本金
		this.print(0, 71, formatAmt(overflowTotal, 0), "R"); // 溢收
		this.print(0, 87, formatAmt(repaidPrinTotal, 0), "R"); // 應繳本金
		this.print(0, 103, formatAmt(repaidIntTotal, 0), "R"); // 應繳利息
		this.print(0, 120, formatAmt(repaidExpTotal, 0), "R"); // 應繳費用

		loanBalTotal = BigDecimal.ZERO;
		shortIntTotal = BigDecimal.ZERO;
		shortPrinTotal = BigDecimal.ZERO;
		overflowTotal = BigDecimal.ZERO;
		repaidPrinTotal = BigDecimal.ZERO;
		repaidIntTotal = BigDecimal.ZERO;
		repaidExpTotal = BigDecimal.ZERO;
	}

}
