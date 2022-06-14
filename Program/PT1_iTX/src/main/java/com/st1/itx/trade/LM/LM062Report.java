package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM062ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LM062Report extends MakeReport {

	@Autowired
	LM062ServiceImpl lm062ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	public void exec(TitaVo titaVo,int yearMonth) throws LogicException {

		this.info("LM062Report exec");
		// 取得會計日(同頁面上會計日)
		// 年月日
		int iEntdy = Integer.valueOf(titaVo.get("ENTDY")) + 19110000;
		// 年
		int iYear = yearMonth / 100;
		// 月
		int iMonth = yearMonth % 100;

		String iYearMonth = String.valueOf(((iYear - 1911) * 100) + iMonth);

		this.info("yymm=" + iYearMonth);

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM062", "01-個金3000萬以上-" + iYearMonth,
				"LM062_01-個金3000萬以上-" + iYearMonth, "LM062_底稿_個金3000萬以上.xls", "簡表");

		// 設定欄寬
		makeExcel.setWidth(2, 12);
		makeExcel.setWidth(3, 7);
		makeExcel.setWidth(4, 10);
		makeExcel.setWidth(5, 8);
		makeExcel.setWidth(6, 18);
		makeExcel.setWidth(7, 18);
		makeExcel.setWidth(8, 15);
		makeExcel.setWidth(9, 15);
		makeExcel.setWidth(10, 15);
		makeExcel.setWidth(11, 15);

		// 粗體
		makeExcel.setIBU("B");

		// 機密等級及基準日期
		makeExcel.setValue(1, 10, "機密等級：機密\n" + (iYear - 1911) + "." + String.format("%02d", iMonth), "R");

		List<Map<String, String>> fnAllList = new ArrayList<>();

		try {

			fnAllList = lm062ServiceImpl.findAll(titaVo,yearMonth);

		} catch (Exception e) {

			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM062ServiceImpl.findAll error = " + errors.toString());

		}

		// 資料比數是否大於0筆
		if (fnAllList.size() > 0) {

			// 初始列
			int row = 2;
			String tempNo = "";
			String fdnm = "";

			// 總計
			BigDecimal tot = new BigDecimal("0");

			// 撥款日期
			int day = 0;
			int lday = 0;
			String date = "";

			for (Map<String, String> tLDVo : fnAllList) {

				row++;

				for (int col = 0; col < tLDVo.size(); col++) {

					fdnm = "F" + String.valueOf(col);

					// 實際EXCEL 欄位分配
					int i = col + 2;
					switch (i) {

					case 2:
						// B欄 戶號(數字右靠)
						if (!tempNo.equals(tLDVo.get(fdnm))) {

							tempNo = tLDVo.get(fdnm);
							makeExcel.setValue(row, i,
									tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? BigDecimal.ZERO
											: Integer.valueOf(tLDVo.get(fdnm)),
									"R");

						} else {

							makeExcel.setValue(row, i, "", "R");
						}
						break;
					case 3:
						// C欄 額度
						makeExcel.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? BigDecimal.ZERO
										: Integer.valueOf(tLDVo.get(fdnm)),
								"C");
						break;
					case 4:
						// D欄 戶名
						makeExcel.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "L");
						break;
					case 5:
						// E欄 覆審月份
						day = tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? 0
								: Integer.valueOf(tLDVo.get(fdnm));
						day = day % 100;

						makeExcel.setValue(row, i, day, "C");
						break;
					case 6:
						// F欄 撥款日期
						makeExcel.setValue(row, i, tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? 0
								: Integer.parseInt(tLDVo.get(fdnm)) - 19110000, "R");

						// 與前一筆是否相同
						if (!date.equals(tLDVo.get(fdnm))) {
							date = tLDVo.get(fdnm);
							if (lday == 0) {
								// 第一次賦值
								lday = Integer.parseInt(tLDVo.get(fdnm));
							} else {
								if (lday > Integer.parseInt(tLDVo.get(fdnm))) {
									lday = Integer.parseInt(tLDVo.get(fdnm));
								}
							}
						}
						break;

					case 7:
						// G欄 放款餘額

						makeExcel.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? BigDecimal.ZERO
										: new BigDecimal(tLDVo.get(fdnm)),
								"#,##0", "R");

						// 所有金額總計
						tot = tot.add(new BigDecimal(tLDVo.get(fdnm)));

						break;
					case 8:
						// H欄 全戶餘額
						makeExcel.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? BigDecimal.ZERO
										: new BigDecimal(tLDVo.get(fdnm)),
								"#,##0", "R");
						break;
					case 9:
						// I欄 展期記號
						makeExcel.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "C");
						break;
					case 10:
						// J欄 應覆審案件
						makeExcel.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "C");
						break;
					case 11:
						// K欄 評等
						makeExcel.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 || tLDVo.get(fdnm).equals("0") ? ""
										: tLDVo.get(fdnm),"C");
						break;
					default:

						break;
					}
				}
			}

			makeExcel.setValue(row + 1, 2, "總計", "L");
			makeExcel.setValue(row + 1, 6, lday - 19110000, "R");
			makeExcel.setValue(row + 1, 7, tot, "#,##0", "R");

			// 畫框線
//			makeExcel.setAddRengionBorder("B", 3, "H", row + 1, 1);

		} else {
			makeExcel.setValue(3, 2, "本日無資料");
		}

		makeExcel.close();
		// makeExcel.toExcel(sno);

	}
}
