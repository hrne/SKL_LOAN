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
import com.st1.itx.db.service.springjpa.cm.L9702ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")
public class L9702Report extends MakeReport {

	@Autowired
	L9702ServiceImpl l9702ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	private static int rptFg = 0;

	/**
	 * 期初餘額-加總
	 */
	BigDecimal totalOfBeginBal = BigDecimal.ZERO;
	/**
	 * 撥款金額-加總
	 */
	BigDecimal totalOfDrawdownAmt = BigDecimal.ZERO;
	/**
	 * 期末餘額-加總
	 */
	BigDecimal totalOfEndBal = BigDecimal.ZERO;
	/**
	 * 轉催收金額-加總
	 */
	BigDecimal totalOfOvduPrinAmt = BigDecimal.ZERO;
	/**
	 * 期末催收餘額-加總
	 */
	BigDecimal totalOfEndvduBal = BigDecimal.ZERO;
	/**
	 * 還款金額-加總
	 */
	BigDecimal totalOfRepaidAmt = BigDecimal.ZERO;
	/**
	 * 當期利息收入
	 */
	BigDecimal totalOfIntRcv = BigDecimal.ZERO;

	@Override
	public void printHeader() {

		this.print(-1, 3, "程式ID：" + this.getParentTranCode());
		this.print(-1, 60, "新光人壽保險股份有限公司", "C");

		String today = dDateUtil.getNowStringBc();

		this.print(-1, 120, "日　　期：" + this.showBcDate(today, 1), "R");

		this.print(-2, 3, "報　表：" + this.getRptCode());

		if (rptFg == 0) {
			this.print(-2, 60, "放款餘額及財收統計表", "C");
		} else if (rptFg == 1) {
			this.print(-2, 60, "放款餘額及財收統計表－企金", "C");
		} else if (rptFg == 2) {
			this.print(-2, 60, "放款餘額及財收統計表－非企金", "C");
		}

		this.print(-2, 120, "時　　間：" + dDateUtil.getNowStringTime().substring(0, 2) + ":"
				+ dDateUtil.getNowStringTime().substring(2, 4) + ":" + dDateUtil.getNowStringTime().substring(4, 6),
				"R");

		this.print(-3, 115, "頁　　次：　" + this.getNowPage(), "R");

		// 明細起始列(自訂亦必須)
		this.setBeginRow(4);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(35);

	}

	@Override
	public void printContinueNext() {
		this.print(1, this.getMidXAxis(), "=====　續　　下　　頁　=====", "C");
	}

	@Override
	public void printRptFooter() {
		this.print(1, this.getMidXAxis(), "=====　報　表　結　束　=====", "C");
	}

	public void exec(TitaVo titaVo) throws LogicException {

		// 範圍起日
		int startDate = Integer.parseInt(titaVo.get("ACCTDATE_ST")) + 19110000;

		// 範圍止日
		int endDate = Integer.parseInt(titaVo.get("ACCTDATE_ED")) + 19110000;

		// 產表種類
		String type = titaVo.getParam("REPORT_TYPE");

		List<Map<String, String>> listL9702 = null;

		switch (type) {
		case "1":
			try {
				listL9702 = l9702ServiceImpl.getType1(startDate, endDate, titaVo);
			} catch (Exception e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.error("L9702ServiceImpl getType1 error = " + errors.toString());
			}
			break;
		case "2":
			try {
				listL9702 = l9702ServiceImpl.getType2(startDate, endDate, titaVo);
			} catch (Exception e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.error("L9702ServiceImpl getType2 error = " + errors.toString());
			}
			break;
		case "3":
			try {
				listL9702 = l9702ServiceImpl.finddbf(titaVo);
			} catch (Exception e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.error("L9702ServiceImpl getType1 error = " + errors.toString());
			}
			testExcel(titaVo, listL9702);
			break;
		default:
			// 輸入錯誤離開程式
			this.error("L9702Report titaVo REPORT_TYPE = " + type + " , 不在範圍(1~3)內");
			return;
		}

		if ("1".equals(type)) {

			this.info("type = 1");

			this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L9702", "放款餘額及財收統計表", "", "A4", "");

			this.setFontSize(12);

			this.print(1, 2, "會計日期：　" + this.showRocDate(startDate, 1) + " － " + this.showRocDate(endDate, 1));
			this.print(2, 2, "類別　　　　　　期初餘額　　　　 撥款金額　　　　 催收回復　　　　 還款金額　　　　　 轉催收　　　　　　期末餘額");
			this.print(1, 0,
					"----------------------------------------------------------------------------------------------------------------------");
			this.print(1, 2, "企金");

			Map<String, String> tL9702 = null;

			BigDecimal beginBal = null; // 期初餘額
			BigDecimal drawdownAmt = null; // 撥款金額
			BigDecimal endBal = null; // 期末餘額
			BigDecimal ovduPrinAmt = null; // 轉催收金額
			BigDecimal ovduBal = null; // 期末催收餘額
			BigDecimal intRcv = null;

			BigDecimal repaidAmt = BigDecimal.ZERO; // 還款金額

			if (listL9702 != null && listL9702.size() >= 2) {
				tL9702 = listL9702.get(1); // 企金
				beginBal = tL9702.get("F1") == null ? BigDecimal.ZERO : new BigDecimal(tL9702.get("F1")); // 期初餘額
				drawdownAmt = tL9702.get("F2") == null ? BigDecimal.ZERO : new BigDecimal(tL9702.get("F2")); // 撥款金額
				endBal = tL9702.get("F3") == null ? BigDecimal.ZERO : new BigDecimal(tL9702.get("F3")); // 期末餘額
				ovduPrinAmt = tL9702.get("F4") == null ? BigDecimal.ZERO : new BigDecimal(tL9702.get("F4")); // 轉催收金額
				ovduBal = tL9702.get("F5") == null ? BigDecimal.ZERO : new BigDecimal(tL9702.get("F5")); // 期末催收餘額
				intRcv = tL9702.get("F6") == null ? BigDecimal.ZERO : new BigDecimal(tL9702.get("F6")); // 當期利息收入

				repaidAmt = beginBal.add(drawdownAmt).subtract(ovduPrinAmt).subtract(endBal);

				totalOfBeginBal = totalOfBeginBal.add(beginBal);
				totalOfDrawdownAmt = totalOfDrawdownAmt.add(drawdownAmt);
				totalOfEndBal = totalOfEndBal.add(endBal);
				totalOfOvduPrinAmt = totalOfOvduPrinAmt.add(ovduPrinAmt);
				totalOfEndvduBal = totalOfEndvduBal.add(ovduBal);
				totalOfRepaidAmt = totalOfRepaidAmt.add(repaidAmt);
				totalOfIntRcv = totalOfIntRcv.add(intRcv);
			} else {
				beginBal = BigDecimal.ZERO; // 期初餘額
				drawdownAmt = BigDecimal.ZERO; // 撥款金額
				endBal = BigDecimal.ZERO; // 期末餘額
				ovduPrinAmt = BigDecimal.ZERO; // 轉催收金額
			}

//			tL9702A.get("F0"); // 企金別

			this.print(0, 25, formatAmt(beginBal, 0), "R"); // 期初餘額
			this.print(0, 40, formatAmt(drawdownAmt, 0), "R"); // 撥款金額
			this.print(0, 56, "", "R"); // 催收回復
			this.print(0, 72, formatAmt(repaidAmt, 0), "R"); // 還款金額
			this.print(0, 88, formatAmt(ovduPrinAmt, 0), "R"); // 轉催收
			this.print(0, 106, formatAmt(endBal, 0), "R"); // 期末餘額

			this.print(1, 2, "個金");

			if (listL9702 != null && listL9702.size() >= 1) {
				tL9702 = listL9702.get(0); // 個金
				beginBal = tL9702.get("F1") == null ? BigDecimal.ZERO : new BigDecimal(tL9702.get("F1")); // 期初餘額
				drawdownAmt = tL9702.get("F2") == null ? BigDecimal.ZERO : new BigDecimal(tL9702.get("F2")); // 撥款金額
				endBal = tL9702.get("F3") == null ? BigDecimal.ZERO : new BigDecimal(tL9702.get("F3")); // 期末餘額
				ovduPrinAmt = tL9702.get("F4") == null ? BigDecimal.ZERO : new BigDecimal(tL9702.get("F4")); // 轉催收金額
				ovduBal = tL9702.get("F5") == null ? BigDecimal.ZERO : new BigDecimal(tL9702.get("F5")); // 期末催收餘額
				intRcv = tL9702.get("F6") == null ? BigDecimal.ZERO : new BigDecimal(tL9702.get("F6")); // 當期利息收入

				repaidAmt = beginBal.add(drawdownAmt).subtract(ovduPrinAmt).subtract(endBal);

				totalOfBeginBal = totalOfBeginBal.add(beginBal);
				totalOfDrawdownAmt = totalOfDrawdownAmt.add(drawdownAmt);
				totalOfEndBal = totalOfEndBal.add(endBal);
				totalOfOvduPrinAmt = totalOfOvduPrinAmt.add(ovduPrinAmt);
				totalOfEndvduBal = totalOfEndvduBal.add(ovduBal);
				totalOfRepaidAmt = totalOfRepaidAmt.add(repaidAmt);
				totalOfIntRcv = totalOfIntRcv.add(intRcv);
			} else {
				beginBal = BigDecimal.ZERO; // 期初餘額
				drawdownAmt = BigDecimal.ZERO; // 撥款金額
				endBal = BigDecimal.ZERO; // 期末餘額
				ovduPrinAmt = BigDecimal.ZERO; // 轉催收金額
			}

			this.print(0, 25, formatAmt(beginBal, 0), "R"); // 期初餘額
			this.print(0, 40, formatAmt(drawdownAmt, 0), "R"); // 撥款金額
			this.print(0, 56, "", "R"); // 催收回復
			this.print(0, 72, formatAmt(repaidAmt, 0), "R"); // 還款金額
			this.print(0, 88, formatAmt(ovduPrinAmt, 0), "R"); // 轉催收
			this.print(0, 106, formatAmt(endBal, 0), "R"); // 期末餘額

			this.print(1, 2, "車貸");
			this.print(1, 0,
					"----------------------------------------------------------------------------------------------------------------------");
			this.print(1, 2, "合計：");
			this.print(0, 25, formatAmt(totalOfBeginBal, 0), "R"); // 期初餘額
			this.print(0, 40, formatAmt(totalOfDrawdownAmt, 0), "R"); // 撥款金額
			this.print(0, 56, "", "R"); // 催收回復
			this.print(0, 72, formatAmt(totalOfRepaidAmt, 0), "R"); // 還款金額
			this.print(0, 88, formatAmt(totalOfOvduPrinAmt, 0), "R"); // 轉催收
			this.print(0, 106, formatAmt(totalOfEndBal, 0), "R"); // 期末餘額

			this.print(2, 2, "期末　　　催收款餘額：");
			this.print(0, 40, formatAmt(totalOfEndvduBal, 0), "R");
			this.print(1, 2, "　　　　　放款餘額　：");
			this.print(0, 40, formatAmt(totalOfEndBal, 0), "R");
			this.print(1, 2, "當期　　　利息收入　：");
			this.print(0, 40, formatAmt(totalOfIntRcv, 0), "R");
			this.print(0, 37, "", "R");

			this.close();

		} else if ("2".equals(type)) {
			rptFg = 1;

			// 期末催收餘額先累加
			if (listL9702 != null) {
				if (listL9702.size() >= 1) {
					totalOfEndvduBal = totalOfEndvduBal.add(listL9702.get(0).get("F6") == null ? BigDecimal.ZERO
							: new BigDecimal(listL9702.get(0).get("F6"))); // 期末催收餘額
					if (listL9702.size() >= 2) {
						totalOfEndvduBal = totalOfEndvduBal.add(listL9702.get(1).get("F6") == null ? BigDecimal.ZERO
								: new BigDecimal(listL9702.get(1).get("F6"))); // 期末催收餘額
						if (listL9702.size() >= 3) {
							totalOfEndvduBal = totalOfEndvduBal.add(listL9702.get(2).get("F6") == null ? BigDecimal.ZERO
									: new BigDecimal(listL9702.get(2).get("F6"))); // 期末催收餘額
							if (listL9702.size() >= 4) {
								totalOfEndvduBal = totalOfEndvduBal
										.add(listL9702.get(3).get("F6") == null ? BigDecimal.ZERO
												: new BigDecimal(listL9702.get(3).get("F6"))); // 期末催收餘額
							}
						}
					}
				}
			}
			this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L9702", "放款餘額及財收統計表-通路別", "", "A4", "");

			this.setFontSize(12);

			this.print(1, 2, "會計日期：　" + this.showRocDate(startDate, 1) + " － " + this.showRocDate(endDate, 1));
			this.print(2, 2, "類別　　　　　　期初餘額　　　　 撥款金額　　　　 催收回復　　　　 還款金額　　　　　 轉催收　　　　　　期末餘額");
			this.print(1, 0,
					"----------------------------------------------------------------------------------------------------------------------");
			this.print(1, 2, "企金");

			Map<String, String> tL9702 = null;

			BigDecimal beginBal = null; // 期初餘額
			BigDecimal drawdownAmt = null; // 撥款金額
			BigDecimal endBal = null; // 期末餘額
			BigDecimal ovduPrinAmt = null; // 轉催收金額
			BigDecimal intRcv = null;

			BigDecimal repaidAmt = BigDecimal.ZERO; // 還款金額

			if (listL9702 != null && listL9702.size() >= 4) {
				tL9702 = listL9702.get(3); // 企金
				beginBal = tL9702.get("F2") == null ? BigDecimal.ZERO : new BigDecimal(tL9702.get("F2")); // 期初餘額
				drawdownAmt = tL9702.get("F3") == null ? BigDecimal.ZERO : new BigDecimal(tL9702.get("F3")); // 撥款金額
				endBal = tL9702.get("F4") == null ? BigDecimal.ZERO : new BigDecimal(tL9702.get("F4")); // 期末餘額
				ovduPrinAmt = tL9702.get("F5") == null ? BigDecimal.ZERO : new BigDecimal(tL9702.get("F5")); // 轉催收金額
				intRcv = tL9702.get("F7") == null ? BigDecimal.ZERO : new BigDecimal(tL9702.get("F7")); // 當期利息收入

				repaidAmt = beginBal.add(drawdownAmt).subtract(ovduPrinAmt).subtract(endBal);

				totalOfBeginBal = totalOfBeginBal.add(beginBal);
				totalOfDrawdownAmt = totalOfDrawdownAmt.add(drawdownAmt);
				totalOfEndBal = totalOfEndBal.add(endBal);
				totalOfOvduPrinAmt = totalOfOvduPrinAmt.add(ovduPrinAmt);
				totalOfRepaidAmt = totalOfRepaidAmt.add(repaidAmt);
				totalOfIntRcv = totalOfIntRcv.add(intRcv);
			} else {
				beginBal = BigDecimal.ZERO; // 期初餘額
				drawdownAmt = BigDecimal.ZERO; // 撥款金額
				endBal = BigDecimal.ZERO; // 期末餘額
				ovduPrinAmt = BigDecimal.ZERO; // 轉催收金額
			}

//			tL9702A.get("F0"); // 企金別

			this.print(0, 25, formatAmt(beginBal, 0), "R"); // 期初餘額
			this.print(0, 40, formatAmt(drawdownAmt, 0), "R"); // 撥款金額
			this.print(0, 56, "", "R"); // 催收回復
			this.print(0, 72, formatAmt(repaidAmt, 0), "R"); // 還款金額
			this.print(0, 88, formatAmt(ovduPrinAmt, 0), "R"); // 轉催收
			this.print(0, 106, formatAmt(endBal, 0), "R"); // 期末餘額

			this.print(1, 2, "個金");

			if (listL9702 != null && listL9702.size() >= 3) {
				tL9702 = listL9702.get(2); // 個金
				beginBal = tL9702.get("F2") == null ? BigDecimal.ZERO : new BigDecimal(tL9702.get("F2")); // 期初餘額
				drawdownAmt = tL9702.get("F3") == null ? BigDecimal.ZERO : new BigDecimal(tL9702.get("F3")); // 撥款金額
				endBal = tL9702.get("F4") == null ? BigDecimal.ZERO : new BigDecimal(tL9702.get("F4")); // 期末餘額
				ovduPrinAmt = tL9702.get("F5") == null ? BigDecimal.ZERO : new BigDecimal(tL9702.get("F5")); // 轉催收金額
				intRcv = tL9702.get("F7") == null ? BigDecimal.ZERO : new BigDecimal(tL9702.get("F7")); // 當期利息收入

				repaidAmt = beginBal.add(drawdownAmt).subtract(ovduPrinAmt).subtract(endBal);

				totalOfBeginBal = totalOfBeginBal.add(beginBal);
				totalOfDrawdownAmt = totalOfDrawdownAmt.add(drawdownAmt);
				totalOfEndBal = totalOfEndBal.add(endBal);
				totalOfOvduPrinAmt = totalOfOvduPrinAmt.add(ovduPrinAmt);
				totalOfRepaidAmt = totalOfRepaidAmt.add(repaidAmt);
				totalOfIntRcv = totalOfIntRcv.add(intRcv);
			} else {
				beginBal = BigDecimal.ZERO; // 期初餘額
				drawdownAmt = BigDecimal.ZERO; // 撥款金額
				endBal = BigDecimal.ZERO; // 期末餘額
				ovduPrinAmt = BigDecimal.ZERO; // 轉催收金額
			}

			this.print(0, 25, formatAmt(beginBal, 0), "R"); // 期初餘額
			this.print(0, 40, formatAmt(drawdownAmt, 0), "R"); // 撥款金額
			this.print(0, 56, "", "R"); // 催收回復
			this.print(0, 72, formatAmt(repaidAmt, 0), "R"); // 還款金額
			this.print(0, 88, formatAmt(ovduPrinAmt, 0), "R"); // 轉催收
			this.print(0, 106, formatAmt(endBal, 0), "R"); // 期末餘額

			this.print(1, 2, "車貸");
			this.print(1, 0,
					"----------------------------------------------------------------------------------------------------------------------");
			this.print(1, 2, "合計：");
			this.print(0, 25, formatAmt(totalOfBeginBal, 0), "R"); // 期初餘額
			this.print(0, 40, formatAmt(totalOfDrawdownAmt, 0), "R"); // 撥款金額
			this.print(0, 56, "", "R"); // 催收回復
			this.print(0, 72, formatAmt(totalOfRepaidAmt, 0), "R"); // 還款金額
			this.print(0, 88, formatAmt(totalOfOvduPrinAmt, 0), "R"); // 轉催收
			this.print(0, 106, formatAmt(totalOfEndBal, 0), "R"); // 期末餘額

			this.print(2, 2, "期末　　　催收款餘額：");
			this.print(0, 40, formatAmt(totalOfEndvduBal, 0), "R");
			this.print(1, 2, "　　　　　放款餘額　：");
			this.print(0, 40, formatAmt(totalOfEndBal, 0), "R");
			this.print(1, 2, "當期　　　利息收入　：");
			this.print(0, 40, formatAmt(totalOfIntRcv, 0), "R");
			this.print(0, 37, "", "R");

			// 換頁
			rptFg = 2;
			this.newPage();
			totalOfBeginBal = BigDecimal.ZERO;
			totalOfDrawdownAmt = BigDecimal.ZERO;
			totalOfRepaidAmt = BigDecimal.ZERO;
			totalOfOvduPrinAmt = BigDecimal.ZERO;
			totalOfEndBal = BigDecimal.ZERO;
			totalOfEndBal = BigDecimal.ZERO;
			totalOfIntRcv = BigDecimal.ZERO;

			this.print(1, 2, "會計日期：　" + this.showRocDate(startDate, 1) + " － " + this.showRocDate(endDate, 1));
			this.print(2, 2, "類別　　　　　　期初餘額　　　　 撥款金額　　　　 催收回復　　　　 還款金額　　　　　 轉催收　　　　　　期末餘額");
			this.print(1, 0,
					"----------------------------------------------------------------------------------------------------------------------");
			this.print(1, 2, "企金");

			tL9702 = null;

			beginBal = null; // 期初餘額
			drawdownAmt = null; // 撥款金額
			endBal = null; // 期末餘額
			ovduPrinAmt = null; // 轉催收金額
			intRcv = null;

			repaidAmt = BigDecimal.ZERO; // 還款金額

			if (listL9702 != null && listL9702.size() >= 2) {
				tL9702 = listL9702.get(1); // 企金
				beginBal = tL9702.get("F2") == null ? BigDecimal.ZERO : new BigDecimal(tL9702.get("F2")); // 期初餘額
				drawdownAmt = tL9702.get("F3") == null ? BigDecimal.ZERO : new BigDecimal(tL9702.get("F3")); // 撥款金額
				endBal = tL9702.get("F4") == null ? BigDecimal.ZERO : new BigDecimal(tL9702.get("F4")); // 期末餘額
				ovduPrinAmt = tL9702.get("F5") == null ? BigDecimal.ZERO : new BigDecimal(tL9702.get("F5")); // 轉催收金額
				intRcv = tL9702.get("F7") == null ? BigDecimal.ZERO : new BigDecimal(tL9702.get("F7")); // 當期利息收入

				repaidAmt = beginBal.add(drawdownAmt).subtract(ovduPrinAmt).subtract(endBal);

				totalOfBeginBal = totalOfBeginBal.add(beginBal);
				totalOfDrawdownAmt = totalOfDrawdownAmt.add(drawdownAmt);
				totalOfEndBal = totalOfEndBal.add(endBal);
				totalOfOvduPrinAmt = totalOfOvduPrinAmt.add(ovduPrinAmt);
				totalOfRepaidAmt = totalOfRepaidAmt.add(repaidAmt);
				totalOfIntRcv = totalOfIntRcv.add(intRcv);
			} else {
				beginBal = BigDecimal.ZERO; // 期初餘額
				drawdownAmt = BigDecimal.ZERO; // 撥款金額
				endBal = BigDecimal.ZERO; // 期末餘額
				ovduPrinAmt = BigDecimal.ZERO; // 轉催收金額
			}

//			tL9702A.get("F0"); // 企金別

			this.print(0, 25, formatAmt(beginBal, 0), "R"); // 期初餘額
			this.print(0, 40, formatAmt(drawdownAmt, 0), "R"); // 撥款金額
			this.print(0, 56, "", "R"); // 催收回復
			this.print(0, 72, formatAmt(repaidAmt, 0), "R"); // 還款金額
			this.print(0, 88, formatAmt(ovduPrinAmt, 0), "R"); // 轉催收
			this.print(0, 106, formatAmt(endBal, 0), "R"); // 期末餘額

			this.print(1, 2, "個金");

			if (listL9702 != null && listL9702.size() >= 1) {
				tL9702 = listL9702.get(0); // 個金
				beginBal = tL9702.get("F2") == null ? BigDecimal.ZERO : new BigDecimal(tL9702.get("F2")); // 期初餘額
				drawdownAmt = tL9702.get("F3") == null ? BigDecimal.ZERO : new BigDecimal(tL9702.get("F3")); // 撥款金額
				endBal = tL9702.get("F4") == null ? BigDecimal.ZERO : new BigDecimal(tL9702.get("F4")); // 期末餘額
				ovduPrinAmt = tL9702.get("F5") == null ? BigDecimal.ZERO : new BigDecimal(tL9702.get("F5")); // 轉催收金額
				intRcv = tL9702.get("F7") == null ? BigDecimal.ZERO : new BigDecimal(tL9702.get("F7")); // 當期利息收入

				repaidAmt = beginBal.add(drawdownAmt).subtract(ovduPrinAmt).subtract(endBal);

				totalOfBeginBal = totalOfBeginBal.add(beginBal);
				totalOfDrawdownAmt = totalOfDrawdownAmt.add(drawdownAmt);
				totalOfEndBal = totalOfEndBal.add(endBal);
				totalOfOvduPrinAmt = totalOfOvduPrinAmt.add(ovduPrinAmt);
				totalOfRepaidAmt = totalOfRepaidAmt.add(repaidAmt);
				totalOfIntRcv = totalOfIntRcv.add(intRcv);
			} else {
				beginBal = BigDecimal.ZERO; // 期初餘額
				drawdownAmt = BigDecimal.ZERO; // 撥款金額
				endBal = BigDecimal.ZERO; // 期末餘額
				ovduPrinAmt = BigDecimal.ZERO; // 轉催收金額
			}

			this.print(0, 25, formatAmt(beginBal, 0), "R"); // 期初餘額
			this.print(0, 40, formatAmt(drawdownAmt, 0), "R"); // 撥款金額
			this.print(0, 56, "", "R"); // 催收回復
			this.print(0, 72, formatAmt(repaidAmt, 0), "R"); // 還款金額
			this.print(0, 88, formatAmt(ovduPrinAmt, 0), "R"); // 轉催收
			this.print(0, 106, formatAmt(endBal, 0), "R"); // 期末餘額

			this.print(1, 2, "車貸");
			this.print(1, 0,
					"----------------------------------------------------------------------------------------------------------------------");
			this.print(1, 2, "合計：");
			this.print(0, 25, formatAmt(totalOfBeginBal, 0), "R"); // 期初餘額
			this.print(0, 40, formatAmt(totalOfDrawdownAmt, 0), "R"); // 撥款金額
			this.print(0, 56, "", "R"); // 催收回復
			this.print(0, 72, formatAmt(totalOfRepaidAmt, 0), "R"); // 還款金額
			this.print(0, 88, formatAmt(totalOfOvduPrinAmt, 0), "R"); // 轉催收
			this.print(0, 106, formatAmt(totalOfEndBal, 0), "R"); // 期末餘額

			this.print(2, 2, "期末　　　催收款餘額：");
			this.print(0, 40, formatAmt(totalOfEndvduBal, 0), "R");
			this.print(1, 2, "　　　　　放款餘額　：");
			this.print(0, 40, formatAmt(totalOfEndBal, 0), "R");
			this.print(1, 2, "當期　　　利息收入　：");
			this.print(0, 40, formatAmt(totalOfIntRcv, 0), "R");
			this.print(0, 37, "", "R");

			this.close();

		} else { // type = 3
			List<Map<String, String>> L9702List3 = null;
			try {

				L9702List3 = l9702ServiceImpl.finddbf(titaVo);

				testExcel(titaVo, L9702List3);
			} catch (Exception e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.error("L9702ServiceImpl.finddbf error = " + errors.toString());
			}
			testExcel(titaVo, L9702List3);
		} // else

	}

	private void testExcel(TitaVo titaVo, List<Map<String, String>> LDList) throws LogicException {
		this.info("===========in testExcel");
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L9702", "利息收入明細檔LNW63A3P", "LNW63A3P",
				"LNW63A3P.xlsx", "LNW63A3P");

		this.info("-----------------" + LDList);
		if (LDList.size() == 0) {
			makeExcel.setValue(2, 1, "本日無資料");
		}
		int row = 2;
		for (Map<String, String> tLDVo : LDList) {
			String ad = "";
			int col = 0;
			for (int i = 0; i < tLDVo.size(); i++) {

				ad = "F" + String.valueOf(col);
				col++;
				switch (col) {
				case 6:
					makeExcel.setValue(row, col, tLDVo.get(ad), "0.0000%", "R");
					break;
				default:
					makeExcel.setValue(row, col, tLDVo.get(ad), "R");
					break;
				}
			} // for
			row++;
		}

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

}
