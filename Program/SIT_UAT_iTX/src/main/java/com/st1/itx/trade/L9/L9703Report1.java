package com.st1.itx.trade.L9;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.buffer.TxBuffer;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L9703ServiceImpl;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.BaTxVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

/**
 * L9703Report1
 * 
 * @author ChihWei
 * @version 1.0.0
 */
@Component
@Scope("prototype")
public class L9703Report1 extends MakeReport {
	private static final Logger logger = LoggerFactory.getLogger(L9703Report1.class);

	@Autowired
	L9703ServiceImpl l9703ServiceImpl;

	@Autowired
	DateUtil dateUtil;

	@Autowired
	BaTxCom baTxCom;

	@Autowired
	Parse parse;

	/**
	 * 表頭種類<BR>
	 * 1=逾期期數<BR>
	 * 2=逾期日數<BR>
	 */
	private static int rptFlag = 0;

	/**
	 * 會計日期(西元年)
	 */
	private String bcAcDate = "";

	// 逾期期數範圍
	private String unpaidTermSt;
	private String unpaidTermEd;
	// 逾期日數範圍
	private String unpaidDaySt;
	private String unpaidDayEd;

	@Override
	public void printHeader() {

		this.info("L9703Report1.printHeader");

		String today = dDateUtil.getNowStringBc();
		String nowTime = dDateUtil.getNowStringTime();

		this.setFontSize(8);

		this.print(-1, 150, "機密等級：密");

		this.print(-2, 3, "程式ID：" + this.getParentTranCode());
		this.print(-2, 80, "新光人壽保險股份有限公司", "C");
		this.print(-2, 150, "入帳日：" + this.showBcDate(bcAcDate, 0));

		this.print(-3, 3, "報　表：" + this.getRptCode());
		this.print(-3, 80, "滯繳客戶明細表", "C");
		this.print(-3, 150, "會計日：" + this.showBcDate(bcAcDate, 0));

		this.print(-4, 150, "日　期：" + this.showBcDate(today, 1));

		if (rptFlag == 1) {
			// 逾期期數
			this.print(-5, 80, "逾　" + unpaidTermSt + " - " + unpaidTermEd + "　期", "C");
		} else if (rptFlag == 2) {
			// 逾期日數
			this.print(-5, 80, "逾　" + unpaidDaySt + " - " + unpaidDayEd + "　天", "C");
		}
		this.print(-5, 150, "時　間：" + this.showTime(nowTime));

		this.print(-6, 150, "頁　次：　" + this.getNowPage());
		
		this.print(-7, 1, "　押品 　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　    最近　　　逾期");
		this.print(-8, 1,
				" 地區別　 經辦　　　　戶號　　　　戶名　　　初貸日　　　本金餘額　 利率　 繳息迄日　 應繳日　　日數　　未收本息　　　　違約金　　　溢短繳　　　　合計　聯絡人　　　電話　　　　繳款方式");
		this.print(-9, 1,
				"-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

		// 明細起始列(自訂亦必須)
		this.setBeginRow(10);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(35);

	}

	public void exec(TitaVo titaVo, TxBuffer txbuffer) throws LogicException {
		this.info("L9703Report1 exec");

		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L9703", "滯繳客戶明細表", "密", "A4", "");

		bcAcDate = String.valueOf(titaVo.getEntDyI() + 19110000);

		rptFlag = Integer.parseInt(titaVo.getParam("UnpaidCond"));

		// 根據UnpaidCond不同，逾期期數區間、逾期日數區間只會有一組有值

		// 逾期期數區間
		unpaidTermSt = titaVo.getParam("UnpaidTermSt");
		unpaidTermEd = titaVo.getParam("UnpaidTermEd");
		// 逾期日數區間
		unpaidDaySt = titaVo.getParam("UnpaidDaySt");
		unpaidDayEd = titaVo.getParam("UnpaidDayEd");

		List<Map<String, String>> listL9703 = null;

		try {
			listL9703 = l9703ServiceImpl.queryForDetail(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("L9703ServiceImpl.findAll error = " + errors.toString());
		}

		int counts = 0;// 筆數計算

		BigDecimal totalOfLoanBal = BigDecimal.ZERO; // 本金餘額加總
		BigDecimal totalOfUnpaidPriInt = BigDecimal.ZERO; // 未收本息加總
		BigDecimal totalOfBreachAmtAndDelayInt = BigDecimal.ZERO; // 違約金加總
		BigDecimal totalOfOverflow = BigDecimal.ZERO; // 溢短繳加總
		BigDecimal totalOfTotal = BigDecimal.ZERO; // 合計加總

		int tbsdy = titaVo.getEntDyI();
		if (listL9703.size() == 0) {
			this.print(1, 1, "*******    查無資料   ******");
		}
		for (Map<String, String> tL9703 : listL9703) {
			List<BaTxVo> listBaTxVo = new ArrayList<>();

			int custNo = parse.stringToInteger(tL9703.get("F2"));
			int facmNo = parse.stringToInteger(tL9703.get("F3"));

			this.info("L9703Report1 tL9703.custNo = " + custNo);
			this.info("L9703Report1 tL9703.facmNo = " + facmNo);

			baTxCom.setTxBuffer(txbuffer);
			try {
				listBaTxVo = baTxCom.settingUnPaid(tbsdy, custNo, facmNo, 0, 1, BigDecimal.ZERO, titaVo);
			} catch (LogicException e) {
				this.error("baTxCom settingUnPaid ErrorMsg :" + e.getMessage());
			}

			BigDecimal unpaidPriInt = BigDecimal.ZERO; // 未收本息
			BigDecimal breachAmtAndDelayInt = BigDecimal.ZERO; // 違約金(報表最終產出的違約金包含違約金及延遲息)
			BigDecimal overflow = BigDecimal.ZERO; // 溢短繳
			BigDecimal intRate = BigDecimal.ZERO; // 利率
			BigDecimal total; // 合計

			// 未收本息 = 本金+利息 baTxVo.Principal + baTxVo.Interest
			// 違約金 有 但要扣成 0 baTxVo.BreachAmt
			// 溢短繳 baTxVo.UnPaidAmt
			for (BaTxVo baTxVo : listBaTxVo) {

				int dataKind = baTxVo.getDataKind();
				int repayType = baTxVo.getRepayType();

				if (dataKind == 2 && repayType == 1) {
					unpaidPriInt = unpaidPriInt.add(baTxVo.getPrincipal()); // 未收本
					unpaidPriInt = unpaidPriInt.add(baTxVo.getInterest()); // 未收息
					breachAmtAndDelayInt = breachAmtAndDelayInt.add(baTxVo.getBreachAmt()); // 違約金

					intRate = baTxVo.getIntRate();
				}

				// 溢短繳 = 溢繳 - 短繳

				// 溢繳 dataKind = 3.暫收抵繳
				if (dataKind == 3) {
					overflow = overflow.add(baTxVo.getUnPaidAmt());
				}

				// 短繳
				// dataKind = 1.應收費用+未收費用+短繳期金
				// repayType = 01 期款
				if (dataKind == 1 && repayType == 1) {
					overflow = overflow.subtract(baTxVo.getUnPaidAmt());
				}
			}
			total = unpaidPriInt.add(breachAmtAndDelayInt).add(overflow); // 合計

			totalOfUnpaidPriInt = totalOfUnpaidPriInt.add(unpaidPriInt); // 未收本息加總計算
			totalOfBreachAmtAndDelayInt = totalOfBreachAmtAndDelayInt.add(breachAmtAndDelayInt); // 違約金加總計算
			totalOfOverflow = totalOfOverflow.add(overflow); // 溢短繳加總計算
			totalOfTotal = totalOfTotal.add(total); // 合計加總計算

			// 地區別
			this.print(1, 2, tL9703.get("F0"));

			// 經辦
			this.print(0, 10, tL9703.get("F1"));

			// 戶號
			String tmpCustNo = tL9703.get("F2");
			String tmpFacmNo = tL9703.get("F3");
			this.print(0, 18, FormatUtil.pad9(tmpCustNo, 7) + "-" + FormatUtil.pad9(tmpFacmNo, 3));

			// 報表長度僅能容納5個中文字
			// 戶名
			String custName = tL9703.get("F4");
			if (custName.length() > 5) {
				custName = custName.substring(0, 5);
			}
			this.print(0, 30, custName);

			// 初貸日
			String drawdownDate = tL9703.get("F5");
			if (drawdownDate == null || "0".equals(drawdownDate) || drawdownDate.isEmpty()) {
				this.print(0, 47, "", "R");
			} else {
				this.print(0, 47, this.showRocDate(drawdownDate, 1), "R");
			}

			// 本金餘額
			BigDecimal loanBal = tL9703.get("F6") == null ? BigDecimal.ZERO : new BigDecimal(tL9703.get("F6"));
			this.print(0, 59, formatAmt(loanBal, 0), "R");
			totalOfLoanBal = totalOfLoanBal.add(loanBal); // 本金餘額加總計算

			// 利率
			this.print(0, 66, formatAmt(intRate, 4), "R");

			// 繳息迄日
			String interestEndDate = tL9703.get("F7");
			if (interestEndDate == null || "0".equals(interestEndDate) || interestEndDate.isEmpty()) {
				this.print(0, 75, "", "R");
			} else {
				this.print(0, 75, this.showRocDate(interestEndDate, 1), "R");
			}

			// 應繳日
			String lastRepaidDate = tL9703.get("F8");
			if (lastRepaidDate == null || "0".equals(lastRepaidDate) || lastRepaidDate.isEmpty()) {
				this.print(0, 85, "", "R");
			} else {
				this.print(0, 85, this.showRocDate(lastRepaidDate, 1), "R");
			}

			// 日數
			this.print(0, 90, tL9703.get("F9"), "R");

			// 未收本息
			this.print(0, 102, formatAmt(unpaidPriInt, 0), "R");

			// 違約金
			this.print(0, 114, formatAmt(breachAmtAndDelayInt, 0), "R");

			// 溢短繳
			this.print(0, 125, formatAmt(overflow, 0), "R");

			// 合計
			this.print(0, 135, formatAmt(total, 0), "R");

			// 聯絡人
			String cName = tL9703.get("F10");
			if (cName.length() > 10) {
				cName = cName.substring(0, 10);
			}
			this.print(0, 137, cName);

			// 電話
			this.print(0, 147, tL9703.get("F11"));

			// 繳款方式
			this.print(0, 159, tL9703.get("F13"));

			this.print(1, 1, "　　　　 　　　　　 　手機號碼..... ");
			if (tL9703.get("F12") != null) {
				this.print(0, 37, tL9703.get("F12"));
			} else {
				this.print(0, 42, "*");
			}

			counts++;

		} // for

		this.print(1, 1,
				"－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");
		this.print(1, 5, "合　　計　　　　　筆");
		this.print(0, 13, FormatUtil.pad9(String.valueOf(counts), 7));

		// 本金餘額總計
		this.print(0, 59, formatAmt(totalOfLoanBal, 0), "R");

		// 未收本息總計
		this.print(0, 102, formatAmt(totalOfUnpaidPriInt, 0), "R");

		// 違約金總計
		this.print(0, 114, formatAmt(totalOfBreachAmtAndDelayInt, 0), "R");

		// 溢短繳總計
		this.print(0, 125, formatAmt(totalOfOverflow, 0), "R");

		// 合計總計
		this.print(0, 135, formatAmt(totalOfTotal, 0), "R");
		this.print(1, 1,
				"－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");

		long sno = this.close();
		this.toPdf(sno);
	}
}
