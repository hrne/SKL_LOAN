package com.st1.itx.trade.LQ;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LQ007ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class LQ007Report extends MakeReport {

	@Autowired
	public LQ007ServiceImpl lQ007ServiceImpl;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	Parse parse;

	@Autowired
	MakeExcel makeExcel;

	BigDecimal iaiiBalTotal = BigDecimal.ZERO;
	BigDecimal iaiiIntTotal = BigDecimal.ZERO;

	BigDecimal balTotal = BigDecimal.ZERO;
	BigDecimal intTotal = BigDecimal.ZERO;

	public void exec(TitaVo titaVo) throws LogicException {

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "LQ007";
		String fileItem = "專案放款餘額及利息收入";
		String fileName = "LQ007-專案放款餘額及利息收入";
		String defaultExcel = "LQ007_底稿_專案放款餘額及利息收入.xlsx";
		String defaultSheet = "YYYMM";

		this.info("reportVo open");

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();
		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);

		List<Map<String, String>> LQ007List = null;
		try {
			LQ007List = lQ007ServiceImpl.findAll(titaVo);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("lQ007ServiceImpl.findAll error = " + errors.toString());
		}

		makeExcel.setSheet("YYYMM", String.valueOf(titaVo.getEntDyI() / 100));

		int entdy = (parse.stringToInteger(titaVo.getParam("ENTDY")) + 19110000);

		int endY = entdy / 10000;
		int iYear12Month = endY * 100 + 12;
		int endM = entdy / 100 % 100;
		int begY = endY - 3;
		// 所有需要輸出的工作月/月份 - 同年一月到當月

		List<Integer> ymList = new ArrayList<Integer>();

		int tmpCol = 0;

		int endColBal = 17;
		int endColInt = 18;
		for (int y = begY; y <= endY; y++) {

			int tmpYMonth = 0;
			for (int m = 1; m <= 12; m++) {
				int vYMonth = 0;
				String ymTitle = "";

				if (m <= 3) {
					vYMonth = y * 100 + 3;
				} else if (m <= 6) {
					vYMonth = y * 100 + 6;
				} else if (m <= 9) {
					vYMonth = y * 100 + 9;
				} else {
					vYMonth = y * 100 + 12;
				}
				this.info("endY:y =" + endY + ":" + y);
				this.info("tmpYMonth =" + tmpYMonth);
				this.info("vYMonth =" + vYMonth);
				if (endY > y) {
					if (tmpYMonth / 100 != vYMonth / 100) {
						tmpCol = tmpCol + 2;
						tmpYMonth = vYMonth;
						ymTitle = (vYMonth - 191100) / 100 + "年";

						makeExcel.setValue(3, tmpCol, ymTitle, "C");
					} else {
						if (m == 12) {
							ymList.add(vYMonth);
						}
					}

				} else {
					if (tmpYMonth != vYMonth) {
						tmpCol = tmpCol + 2;
						tmpYMonth = vYMonth;
						ymTitle = (vYMonth - 191100) / 100 + "年" + (vYMonth - 191100) % 100 + "月";
						makeExcel.setValue(3, tmpCol, ymTitle, "C");
						ymList.add(vYMonth);
					}
				}

				if (y == endY && m == endM) {
					break;
				}

			}
		}

		this.info("ymList = " + ymList.toString());

		if (LQ007List != null && !LQ007List.isEmpty()) {
			int colBal = 0;
			int colInt = 1;
			int tmpYM = 0;

			boolean isEmpty = true;
			// 清單上只會有 3 6 9 12月份
			for (Integer y : ymList) {

				isEmpty = true;

				// 查詢結果上只會有 3 6 9 12月份
				for (Map<String, String> r : LQ007List) {

					int visibleMonth = parse.stringToInteger(r.get("VisibleMonth"));
					String prodNo = "";
					BigDecimal balSum = BigDecimal.ZERO;
					BigDecimal intSum = BigDecimal.ZERO;
					if (y == visibleMonth) {

						prodNo = r.get("ProdNoShow");
						balSum = getBigDecimal(r.get("BalSum"));
						intSum = getBigDecimal(r.get("IntSum"));

						// AA=>首次購物貸款
						if ("AA".equals(prodNo)) {
							// 餘額
							colBal = colBal + 2;
							// 利收
							colInt = colInt + 2;

							makeExcel.setValue(5, colBal, formatAmt(balSum, 3, 8));
							makeExcel.setValue(5, colInt, formatAmt(intSum, 3, 8));

							// 最後月份的資料
							if (iYear12Month == visibleMonth) {
								makeExcel.setValue(5, endColBal, balSum, "#,##0");
								makeExcel.setValue(5, endColInt, intSum, "#,##0");
							}

						}

						// IA
						if ("IA".equals(prodNo)) {
							makeExcel.setValue(6, colBal, formatAmt(balSum, 3, 8));
							iaiiIntTotal = iaiiIntTotal.add(intSum);

							// 最後月份的資料
							if (iYear12Month == visibleMonth) {
								makeExcel.setValue(6, endColBal, balSum, "#,##0");
							}

						}
						// IB
						if ("IB".equals(prodNo)) {
							makeExcel.setValue(7, colBal, formatAmt(balSum, 3, 8));
							iaiiIntTotal = iaiiIntTotal.add(intSum);

							// 最後月份的資料
							if (iYear12Month == visibleMonth) {
								makeExcel.setValue(7, endColBal, balSum, "#,##0");
							}

						}
						// IC
						if ("IC".equals(prodNo)) {
							makeExcel.setValue(8, colBal, formatAmt(balSum, 3, 8));
							iaiiIntTotal = iaiiIntTotal.add(intSum);

							// 最後月份的資料
							if (iYear12Month == visibleMonth) {
								makeExcel.setValue(8, endColBal, balSum, "#,##0");
							}

						}
						// ID IE => ID
						if (prodNo.matches("ID")) {
							iaiiBalTotal = BigDecimal.ZERO;
							iaiiBalTotal = iaiiBalTotal.add(balSum);
							makeExcel.setValue(9, colBal, formatAmt(iaiiBalTotal, 3, 8));
							iaiiIntTotal = iaiiIntTotal.add(intSum);

							// 最後月份的資料
							if (iYear12Month == visibleMonth) {
								makeExcel.setValue(9, endColBal, iaiiBalTotal, "#,##0");
							}

						}
						// IF IG=>IF
						if (prodNo.matches("IF")) {
							iaiiBalTotal = BigDecimal.ZERO;
							iaiiBalTotal = iaiiBalTotal.add(balSum);
							makeExcel.setValue(10, colBal, formatAmt(iaiiBalTotal, 3, 8));
							iaiiIntTotal = iaiiIntTotal.add(intSum);

							// 最後月份的資料
							if (iYear12Month == visibleMonth) {
								makeExcel.setValue(10, endColBal, iaiiBalTotal, "#,##0");
							}

						}
						// IH II=>IH
						if (prodNo.matches("IH")) {
							iaiiBalTotal = BigDecimal.ZERO;
							iaiiBalTotal = iaiiBalTotal.add(balSum);
							makeExcel.setValue(11, colBal, formatAmt(iaiiBalTotal, 3, 8));
							iaiiIntTotal = iaiiIntTotal.add(intSum);

							// 最後月份的資料
							if (iYear12Month == visibleMonth) {
								makeExcel.setValue(11, endColBal, iaiiBalTotal, "#,##0");
							}

						}
						// 合計
						balTotal = balTotal.add(balSum);
						intTotal = intTotal.add(intSum);

						// ZZ=>921重優惠房貸
						if ("ZZ".equals(prodNo)) {
							makeExcel.setValue(12, colBal, formatAmt(balSum, 3, 8));

							intTotal = intTotal.add(intSum);
							iaiiIntTotal = iaiiIntTotal.add(intSum);
							makeExcel.setValue(6, colInt, formatAmt(iaiiIntTotal, 3, 8));

							// ZZ為最後一筆 同合計印出
							balTotal = balTotal.add(balSum);
							intTotal = intTotal.add(intSum);
							makeExcel.setValue(13, colBal, formatAmt(balTotal, 3, 8));
							makeExcel.setValue(13, colInt, formatAmt(intTotal, 3, 8));

							// 最後月份的資料
							if (iYear12Month == visibleMonth) {
								makeExcel.setValue(12, endColBal, balSum, "#,##0");
								makeExcel.setValue(6, endColBal, iaiiIntTotal, "#,##0");
								makeExcel.setValue(13, endColBal, balTotal, "#,##0");
								makeExcel.setValue(13, endColInt, intTotal, "#,##0");
							}

							iaiiBalTotal = BigDecimal.ZERO;
							iaiiIntTotal = BigDecimal.ZERO;
							balTotal = BigDecimal.ZERO;
							intTotal = BigDecimal.ZERO;

						}

						// 清單上有、查詢沒有則跑進一次
					} else if (isEmpty) {

						this.info("tmpYM = " + tmpYM);
						this.info("y = " + y);

						isEmpty = false;

						balSum = BigDecimal.ZERO;

						// 餘額
						colBal = colBal + 2;
						// 利收
						colInt = colInt + 2;

						makeExcel.setValue(5, colBal, formatAmt(balSum, 3, 8));
						makeExcel.setValue(5, colInt, formatAmt(balSum, 3, 8));
						makeExcel.setValue(6, colBal, formatAmt(balSum, 3, 8));
						makeExcel.setValue(7, colBal, formatAmt(balSum, 3, 8));
						makeExcel.setValue(8, colBal, formatAmt(balSum, 3, 8));
						makeExcel.setValue(9, colBal, formatAmt(balSum, 3, 8));
						makeExcel.setValue(10, colBal, formatAmt(balSum, 3, 8));
						makeExcel.setValue(11, colBal, formatAmt(balSum, 3, 8));
						makeExcel.setValue(12, colBal, formatAmt(balSum, 3, 8));
						makeExcel.setValue(6, colInt, formatAmt(balSum, 3, 8));
						makeExcel.setValue(13, colBal, formatAmt(balSum, 3, 8));
						makeExcel.setValue(13, colInt, formatAmt(balSum, 3, 8));

					}

				}

			}
		} else {
			makeExcel.setValue(5, 2, "本日無資料");
		}

		makeExcel.close();

	}

}
