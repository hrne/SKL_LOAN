package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM052ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;

@Service
@Scope("prototype")

public class LM052Report extends MakeReport {

	@Autowired
	LM052ServiceImpl lM052ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	/**
	 * 執行報表輸出
	 * 
	 * @param titaVo
	 * @param nowDate          今天日期(民國)
	 * @param thisMonthEndDate 當月底日期(民國)
	 * @throws LogicException
	 * 
	 */
	public void exec(TitaVo titaVo, int nowDate, int thisMonthEndDate) throws LogicException {

		// 年
		int iYear = thisMonthEndDate / 10000;
		// 月
		int iMonth = (thisMonthEndDate / 100) % 100;

		// 當年月
		int thisYM = 0;
		// 上年月
		int lastYM = 0;

		// 判斷帳務日與月底日是否同一天
		if (nowDate < thisMonthEndDate) {
			iYear = iMonth - 1 == 0 ? (iYear - 1) : iYear;
			iMonth = iMonth - 1 == 0 ? 12 : iMonth - 1;
		}

		thisYM = iYear * 100 + iMonth;

		// 判斷這個月是否為1月
		if (iMonth - 1 == 0) {
			lastYM = (iYear - 1) * 100 + 12;
		} else {
			lastYM = thisYM - 1;
		}
		
		List<Map<String, String>> lM052List = null;

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "LM052";
		String fileItem = "放款資產分類-會計部備呆計提";
		String fileName = "LM052_放款資產分類-會計部備呆計提";
		String defaultExcel = "LM052_底稿_放款資產分類-會計部備呆計提.xlsx";
		String defaultSheet = "總表";

		this.info("reportVo open");

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();
		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);
		
		
//		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM052", "放款資產分類-會計部備呆計提", "LM052_放款資產分類-會計部備呆計提",
//				"LM052_底稿_放款資產分類-會計部備呆計提.xlsx", "總表");


		makeExcel.setValue(12, 9, iMonth + "月月報表數", "C");
		makeExcel.setValue(14, 5, thisMonthEndDate, "C");
		makeExcel.setValue(14, 6, lastYM + "01", "C");

		// 有3個表格，要select 3次
		for (int i = 1; i <= 3; i++) {

			try {

				lM052List = lM052ServiceImpl.findAll2(titaVo, thisYM + 191100, i);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.info("LM051ServiceImpl.findAll error = " + errors.toString());
			}
			if (lM052List.size() > 0) {
				exportExcel2(lM052List, i);
			}
		}


		makeExcel.close();

	}

	// 總表
	private void exportExcel2(List<Map<String, String>> LDList, int formNum) throws LogicException {
		this.info("LM051Report exportExcel");

		int row = 0;
		int col = 0;

		BigDecimal tempAmt = BigDecimal.ZERO;

		for (Map<String, String> tLDVo : LDList) {

			switch (formNum) {
			// F0
			// 1 = 購置住宅+修繕貸款
			// 2 = 建築貸款
			// 3 = 100年後政策性貸款
			// 4 = 股票質押
			// 5 = 無意義
			// TOTAL = 放款餘額

			case 1:
				if (!tLDVo.get("F0").equals("99") && !tLDVo.get("F0").equals("5")) {

					if (tLDVo.get("F0").equals("TOTAL")) {
						row = 12;
						col = 8;
					} else if (tLDVo.get("F0").equals("1")) {
						row = 7;
						col = 3;
					} else if (tLDVo.get("F0").equals("2")) {
						row = 7;
						col = 4;
					} else if (tLDVo.get("F0").equals("3")) {
						row = 7;
						col = 5;
					} else if (tLDVo.get("F0").equals("4")) {
						row = 7;
						col = 6;
					}
					tempAmt = tLDVo.get("F1") == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F1"));
					this.info("row=" + row + ",col=" + col + ",tempAmt=" + tempAmt);

					makeExcel.setValue(row, col, tempAmt, "#,##0");

				}
				break;
			case 2:
				if (!tLDVo.get("F1").equals("99")) {

					if (tLDVo.get("F0").equals("1")) {
						row = 18;
					} else {
						row = 19;
					}

					if (tLDVo.get("F1").equals("1")) {
						col = 3;
					} else {
						col = 4;
					}

					tempAmt = tLDVo.get("F2") == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F2"));

					this.info("row=" + row + ",col=" + col + ",tempAmt=" + tempAmt);

					makeExcel.setValue(row, col, tempAmt, "#,##0");

				}

				break;

			case 3:
				if (!tLDVo.get("F0").equals("99") || !tLDVo.get("F1").equals("99")) {
					if (tLDVo.get("F0").equals("C")) {
						row = 27;
					} else if (tLDVo.get("F0").equals("D")) {
						row = 28;
					} else {
						row = 29;
					}

					if (tLDVo.get("F1").equals("1")) {
						col = 3;
					} else {
						col = 4;
					}

					tempAmt = tLDVo.get("F2") == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F2"));

					this.info("row=" + row + ",col=" + col + ",tempAmt=" + tempAmt);

					makeExcel.setValue(row, col, tempAmt, "#,##0");
				}
				break;
			default:

				break;
			}
			row = 0;
			col = 0;

		}
		// 表格一
		makeExcel.formulaCaculate(10, 3);// C10 特定放款資產 合計
		makeExcel.formulaCaculate(10, 5);// E10 100年後政策性貸款 合計
		makeExcel.formulaCaculate(7, 7);// G7 非特定-個金不動產抵押貸款
		makeExcel.formulaCaculate(9, 3);// C9 特定放款資產 提存
		makeExcel.formulaCaculate(9, 7);// G9 非特定放款資產 提存
		makeExcel.formulaCaculate(4, 8);// H4 法定備呆提存總額1
		makeExcel.formulaCaculate(7, 8);// H7 法定備呆提存總額2
		makeExcel.formulaCaculate(8, 8);// H8 法定備呆提存總額3
		makeExcel.formulaCaculate(11, 8);// H11 放款金額
		// 表格二
		makeExcel.formulaCaculate(20, 3);// C20 購置住宅+修繕貸款
		makeExcel.formulaCaculate(20, 4);// D20 建築貸款
		makeExcel.formulaCaculate(18, 5);// E18 一般
		makeExcel.formulaCaculate(19, 5);// E19 利變
		makeExcel.formulaCaculate(20, 5);// E20 縱向合計
		makeExcel.formulaCaculate(20, 6);// F20 橫向合計
		// 表格三
		makeExcel.formulaCaculate(30, 3);// C30 購置住宅+修繕貸款
		makeExcel.formulaCaculate(30, 4);// D30 建築貸款
		makeExcel.formulaCaculate(27, 5);// E27 C
		makeExcel.formulaCaculate(28, 5);// E28 D
		makeExcel.formulaCaculate(29, 5);// E29 Z
		makeExcel.formulaCaculate(30, 5);// E30 縱向合計
		makeExcel.formulaCaculate(30, 6);// F31 橫向合計

	}

}