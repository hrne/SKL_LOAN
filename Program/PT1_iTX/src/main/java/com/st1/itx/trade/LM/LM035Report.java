package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM035ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class LM035Report extends MakeReport {

	@Autowired
	LM035ServiceImpl lM035ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	Parse parse;

	private static final BigDecimal million = new BigDecimal("1000000");
	private static final BigDecimal hundred = new BigDecimal("100");

	public void exec(TitaVo titaVo) throws LogicException {
		this.info("LM035Report exportExcel");

		int bcYearMonth = (parse.stringToInteger(titaVo.get("ENTDY")) + 19110000) / 100;
		int bcYear = (parse.stringToInteger(titaVo.get("ENTDY")) + 19110000) / 10000;
		int bcMonth = (parse.stringToInteger(titaVo.get("ENTDY")) + 19110000) / 100 % 100;

		List<Integer> yearMonthList = new ArrayList<Integer>();

		// 出表範圍為舉例:
		// 202301、202212、202209、202206
		// 202203、202009、202006、202003
		// 201912、201812、201712、201612

		int thisYM_3 = bcYear * 100 + 3;
		int thisYM_6 = bcYear * 100 + 6;
		int thisYM_9 = bcYear * 100 + 9;

		int lastYM_3 = (bcYear - 1) * 100 + 3;
		int lastYM_6 = (bcYear - 1) * 100 + 6;
		int lastYM_9 = (bcYear - 1) * 100 + 9;
		int lastYM_12 = (bcYear - 1) * 100 + 12;

		int lastY_2 = (bcYear - 2) * 100 + 12;
		int lastY_3 = (bcYear - 3) * 100 + 12;
		int lastY_4 = (bcYear - 4) * 100 + 12;
		int lastY_5 = (bcYear - 5) * 100 + 12;

		// 目前年月
		yearMonthList.add(bcYearMonth);

		// 根據目前月份決定需加多少季的年月
		switch (bcMonth) {
		case 4:
		case 5:
		case 6:
			yearMonthList.add(thisYM_3);
			break;
		case 7:
		case 8:
		case 9:
			yearMonthList.add(thisYM_3);
			yearMonthList.add(thisYM_6);
			break;
		case 10:
		case 11:
		case 12:
			yearMonthList.add(thisYM_3);
			yearMonthList.add(thisYM_6);
			yearMonthList.add(thisYM_9);
			break;
		}

		// 去年整年度四個季度年月
		yearMonthList.add(lastYM_3);
		yearMonthList.add(lastYM_6);
		yearMonthList.add(lastYM_9);
		yearMonthList.add(lastYM_12);
		// 前2~5年的季度年月(只顯示最後一個季度年月)
		yearMonthList.add(lastY_2);
		yearMonthList.add(lastY_3);
		yearMonthList.add(lastY_4);
		yearMonthList.add(lastY_5);

		// 排序
		Collections.sort(yearMonthList);

		List<Map<String, String>> findData = null;

		try {

			findData = lM035ServiceImpl.findAll(titaVo, yearMonthList);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM035ServiceImpl.exportExcel error = " + errors.toString());
		}

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "LM035";
		String fileItem = "地區逾放比";
		String fileName = "LM035-地區逾放比";
		String defaultExcel = "LM035_底稿_地區逾放比.xlsx";
		String defaultSheet = "10804";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);
		// 工作表命名
		makeExcel.setSheet(defaultSheet, bcYearMonth - 191100 + "");

		// 表頭文字
		String textYMQ = "";
		// 民國年
		int tmpRocYear = 0;
		// 月
		int tmpRocMonth = 0;
		// 季
		int tmpRocQ = 0;
		// 輸出季度和年份表頭
		for (int i = 0; i < yearMonthList.size(); i++) {

			tmpRocYear = (yearMonthList.get(i) / 100) - 1911;
			tmpRocMonth = yearMonthList.get(i) % 100;
			tmpRocQ = tmpRocMonth / 3;

			if (tmpRocMonth != 3 || tmpRocMonth != 6 || tmpRocMonth != 9 || tmpRocMonth != 12) {
				textYMQ = tmpRocYear + "Q" + tmpRocQ;
			} else {
				textYMQ = String.valueOf(tmpRocYear * 100 + tmpRocMonth);
			}

			makeExcel.setValue(2, i + 2, textYMQ);

		}

		makeExcel.setValue(1, yearMonthList.size() + 2, "單位：百萬元");
		makeExcel.setValue(2, yearMonthList.size() + 2, "放款餘額");

		// 資料輸出
		exportData(titaVo, findData, bcYearMonth);

		makeExcel.close();
		// makeExcel.toExcel(sno);
	}

	private void exportData(TitaVo titaVo, List<Map<String, String>> data, int thisYM) throws LogicException {

		BigDecimal ovduBal = BigDecimal.ZERO;
		BigDecimal total = BigDecimal.ZERO;
		int row = 2;// 起始列
		int col = 1;// 起始欄
		int yymm = 0;// 年月
		String cityCode = "00";// 地區別代碼

		for (Map<String, String> r : data) {

			// 年月不一樣就換欄
			if (yymm != Integer.valueOf(r.get("YearMonth"))) {
				yymm = Integer.valueOf(r.get("YearMonth"));
				col++;
				row = 2;
			}

			// 第二round的時候需要做地區平均
			if (row == 2 && col > 2) {

				if (total.compareTo(BigDecimal.ZERO) > 0) {
					BigDecimal division = computeDivide(ovduBal, total, 4);
//					this.info("Print Avg !!!" + "    row = " + row + "    col = " + col + "    value = " + division);
					makeExcel.setValue(22, col - 1, division.multiply(hundred).setScale(2) + "%", "C");
				} else {
//					this.info("Print Avg0 !!!" + "    row = " + row + "    col = " + col + "    value = "
//							+ BigDecimal.ZERO);
					makeExcel.setValue(22, col - 1, BigDecimal.ZERO.setScale(2) + "%", "C");
				}

				ovduBal = BigDecimal.ZERO;
				total = BigDecimal.ZERO;
			}

			if (!cityCode.equals(r.get("CityCode").toString())) {
				cityCode = r.get("CityCode").toString();
				row++;
			}
			// 原本的做法是用 excel format "0.00%", 但實際輸出時不會穩定輸出百分比格式
			// 這邊改成完整用字串組好後輸出, 實際輸出後 excel grids 不會跳綠箭頭
			makeExcel.setValue(row, col, getBigDecimal(r.get("Ratio")).multiply(hundred).setScale(2) + "%", "C");

			// 表示為最後一輪輸出 (當前年月 == 資料年月)
			if (thisYM == yymm) {
				makeExcel.setValue(row, col + 1, computeDivide(getBigDecimal(r.get("LoanBal")), million, 2), "#,##0.00",
						"R");
			}

			// 放款餘額加總
			total = total.add(getBigDecimal(r.get("LoanBal")));
			// 逾期餘額加總
			ovduBal = ovduBal.add(getBigDecimal(r.get("OvduBal"))).add(getBigDecimal(r.get("ColBal")));

		}

		if (total.compareTo(BigDecimal.ZERO) > 0) {
			BigDecimal division = computeDivide(ovduBal, total, 4);
			makeExcel.setValue(row + 1, col, division.multiply(hundred).setScale(2) + "%", "C");
		} else {
			makeExcel.setValue(row + 1, col, BigDecimal.ZERO.setScale(2) + "%", "C");
		}

		// 最後的最後, 輸出放款餘額的累積
		makeExcel.setValue(row + 1, col + 1, computeDivide(total, million, 0), "#,##0", "R");

	}

}
