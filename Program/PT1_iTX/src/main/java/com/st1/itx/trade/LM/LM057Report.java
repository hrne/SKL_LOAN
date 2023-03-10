package com.st1.itx.trade.LM;

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
import com.st1.itx.db.service.springjpa.cm.LM057ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;

@Component
@Scope("prototype")

public class LM057Report extends MakeReport {

	@Autowired
	LM057ServiceImpl lM057ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	public String dateF = "";
	public int yearMon = 0;

	/**
	 * 執行報表輸出
	 * 
	 * @param titaVo
	 * @param date   西元年月底日
	 * 
	 */
	public void exec(TitaVo titaVo, int date) throws LogicException {

		List<Map<String, String>> findList = new ArrayList<>();
		List<Map<String, String>> findListTotal = new ArrayList<>();

		this.info("LM057Report exec");

		dateF = String.valueOf(date).substring(0, 4) + "/" + String.valueOf(date).substring(4, 6) + "/"
				+ String.valueOf(date).substring(6, 8);

		yearMon = date / 100;

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "LM057";
		String fileItem = "表14-5、14-6會計部申報表";
		String fileName = "LM057-表14-5、14-6_會計部申報表";
		String defaultExcel = "LM057_底稿_表14-5、14-6_會計部申報表.xlsx";
		String defaultSheet = "14-5申報表";

		this.info("reportVo open");

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();
		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);
		
//		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM057", "表14-5、14-6會計部申報表",
//				"LM057-表14-5、14-6_會計部申報表", "LM057_底稿_表14-5、14-6_會計部申報表.xlsx", "14-5申報表");

		try {

			findList = lM057ServiceImpl.findAll(titaVo, date);
			findListTotal = lM057ServiceImpl.findTotal(titaVo, date);
//			findList3 = lM057ServiceImpl.findAll(titaVo);

		} catch (Exception e) {

			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM057ServiceImpl.findAll error = " + errors.toString());

		}

		reportData(findList);

		exportExcel14_5(findListTotal);

		exportExcel14_6();

		makeExcel.close();
	}

	private void reportData(List<Map<String, String>> listData) throws LogicException {
		this.info("LM057report.reportData");

		String sheetName = yearMon + "工作表";

		makeExcel.setSheet("XXX工作表", sheetName);

		int row = 1;

		if (listData.size() > 0) {

//			for (Map<String, String> lM057Vo : listData) {
//				row++;
//				// A~C 921受災戶 (要vlookup的)
//				makeExcel.setValue(row, 1, lM057Vo.get("F??"));
//				makeExcel.setValue(row, 2, "C7", "C");
//				makeExcel.setValue(row, 3, lM057Vo.get("F??"));
//			}

			// 起始列
			row = 2;

			for (Map<String, String> lM057Vo : listData) {

				String subbookCode = lM057Vo.get("F0").isEmpty() ? " " : lM057Vo.get("F0");
				int custNo = lM057Vo.get("F1").isEmpty() ? 0 : Integer.valueOf(lM057Vo.get("F1"));
				int facmNo = lM057Vo.get("F2").isEmpty() ? 0 : Integer.valueOf(lM057Vo.get("F2"));
				int bormNo = lM057Vo.get("F3").isEmpty() ? 0 : Integer.valueOf(lM057Vo.get("F3"));
				String custName = lM057Vo.get("F4").isEmpty() ? " " : lM057Vo.get("F4");
				BigDecimal loan = lM057Vo.get("F5").isEmpty() ? BigDecimal.ZERO : new BigDecimal(lM057Vo.get("F5"));
				String date = showBcDate(lM057Vo.get("F6"), 0);
				BigDecimal rate = getBigDecimal(lM057Vo.get("F7"));
				String type = lM057Vo.get("F8");
				String leg = lM057Vo.get("F9").isEmpty() ? " " : lM057Vo.get("F9");
				String prodNo = lM057Vo.get("F11").isEmpty() ? " " : lM057Vo.get("F11");
				// 戶號+額度
				makeExcel.setValue(row, 4, custNo + "" + facmNo);
				// 帳冊別
				makeExcel.setValue(row, 5, subbookCode, "L");
				// 戶號
				makeExcel.setValue(row, 6, custNo, "R");
				// 額度
				makeExcel.setValue(row, 7, facmNo, "R");
				// 序號
				makeExcel.setValue(row, 8, bormNo, "R");
				// 姓名
				makeExcel.setValue(row, 9, custName, "L");
				// 金額
				makeExcel.setValue(row, 10, loan, "#,##0", "R");
				// 到期日
				makeExcel.setValue(row, 11, date, "R");
				// 利率
				makeExcel.setValue(row, 12, rate, "#.000", "R");
				// 分類(需加判斷分類)
				makeExcel.setValue(row, 13, type);

				// 法務進度
				makeExcel.setValue(row, 15, leg);
				// 跟921受災戶比
//				makeExcel.setValue(row, 14, "=VLOOKUP(F" + row + ",A:B,2,0)");

				// 利率代碼
				makeExcel.setValue(row, 16, prodNo);

				row++;
			}

			// 重整公式
			makeExcel.formulaCaculate(1, 9);
			makeExcel.formulaCaculate(1, 10);

		} else {

			makeExcel.setValue(2, 1, "本日無資料");

		}

	}

	private void exportExcel14_5(List<Map<String, String>> listData) throws LogicException {
		this.info("LM057report.report14_5");

		makeExcel.setSheet("14-5申報表");

		// D2 日期
		makeExcel.setValue(2, 4, dateF);

		BigDecimal amount = BigDecimal.ZERO;
		BigDecimal colTotal = BigDecimal.ZERO;
		BigDecimal total = BigDecimal.ZERO;
		// 可能可去掉這層，
		for (Map<String, String> lM057Vo : listData) {

			amount = lM057Vo.get("F1").equals("0") ? BigDecimal.ZERO : new BigDecimal(lM057Vo.get("F1"));

			switch (lM057Vo.get("F0")) {
			case "B1":
				// 甲類-放款本金超過清償期三個月而未獲清償，或雖未屆滿三個月，但以向主、從償務人訴追或楚芬擔保品者
				makeExcel.setValue(5, 4, amount, "#,##0");
//				colTotal = colTotal.add(amount);
				break;
			case "B3":
				// 甲類-放款本金未按期攤超過六個月
				makeExcel.setValue(7, 4, amount, "#,##0");
//				colTotal = colTotal.add(amount);
				break;
//			case "C2":
//				// 乙類-分期償還放款未按期攤超過三至六個月
//				makeExcel.setValue(11, 4, amount, "#,##0");
//				colTotal = colTotal.add(amount);
//				break;
			case "Ovdu":
			case "C2":
				// 乙類-分期償還放款未按期攤超過三至六個月
				makeExcel.setValue(11, 4, amount, "#,##0");
//				colTotal = colTotal.add(amount);
				break;
			case "C5":
				// 已確定分配之債權，惟尚未獲分款者
				makeExcel.setValue(14, 4, amount, "#,##0");
//				colTotal = colTotal.add(amount);
				break;
			case "C7":
				// 其它
				makeExcel.setValue(16, 4, amount, "#,##0");
//				colTotal = colTotal.add(amount);
				break;

			default:
				break;
			}

			
			switch (lM057Vo.get("F0")) {
			case "B1":
			case "B3":
			case "Ovdu":
			case "C2":
			case "C5":
			case "C7":
		
				
				makeExcel.setValue(19, 4, colTotal, "#,##0");

				break;


			default:
				break;
			}
			
			
			if ("Ovdu".equals(lM057Vo.get("F0")) || "B1".equals(lM057Vo.get("F0")) || "B3".equals(lM057Vo.get("F0"))|| "Collection".equals(lM057Vo.get("F0"))) {
				// 逾期放款總額
				this.info("colTotal"+lM057Vo.get("F0")+"="+colTotal + "+" +amount);
				colTotal = colTotal.add(amount);

			}

			if ("Total".equals(lM057Vo.get("F0")) || "Collection".equals(lM057Vo.get("F0")) || "Loss".equals(lM057Vo.get("F0"))) {
				// 放款總額
				this.info("total"+lM057Vo.get("F0")+"="+total + "+" +amount);
				total = total.add(amount);
			}
				

		}
		makeExcel.setValue(18, 4, total, "#,##0");
		makeExcel.setValue(19, 4, colTotal, "#,##0");
		// 重整公式
		makeExcel.formulaCaculate(9, 4);
		makeExcel.formulaCaculate(17, 4);
//		makeExcel.formulaCaculate(19, 4);
		makeExcel.formulaCaculate(20, 4);
		makeExcel.formulaCaculate(21, 4);
		makeExcel.formulaCaculate(22, 4);

	}

	private void exportExcel14_6() throws LogicException {
		this.info("LM057report.report14_6");

		makeExcel.setSheet("表14-6");

		// 重整 D2~D24
		makeExcel.formulaCaculate(2, 4);

		for (int i = 5; i <= 24; i++) {

			makeExcel.formulaCaculate(i, 4);

		}

	}
}
