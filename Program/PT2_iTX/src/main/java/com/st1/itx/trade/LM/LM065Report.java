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
import com.st1.itx.db.service.springjpa.cm.LM062ServiceImpl;
import com.st1.itx.db.service.springjpa.cm.LM065ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LM065Report extends MakeReport {

	@Autowired
	LM065ServiceImpl lm065ServiceImpl;

	@Autowired
	LM062ServiceImpl lm062ServiceImpl;
	
	@Autowired
	LM062Report lm062report;
	
	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printTitle() {
	}

	public void exec(TitaVo titaVo, int yearMonth) throws LogicException {

		this.info("LM065Report exec");

		// 取得會計日(同頁面上會計日)
		// 年月日
//		int iEntdy = Integer.valueOf(titaVo.get("ENTDY")) + 19110000;
		// 年
		int iYear = yearMonth / 100;
		// 月
		int iMonth = yearMonth % 100;

		String iYearMonth = String.valueOf(((iYear - 1911) * 100) + iMonth);

		this.info("yymm=" + iYearMonth);

		String txCD = "LM065";
		String itemName = "04-個金100萬以上小於2000萬";

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), txCD, itemName + "-" + iYearMonth,
				txCD + "_" + itemName + "-" + iYearMonth, "LM065_底稿_個金100萬以上小於2000萬.xls", "簡表");
		// 設定欄寬
		makeExcel.setWidth(2, 12);
		makeExcel.setWidth(3, 7);
		makeExcel.setWidth(4, 15);
		makeExcel.setWidth(5, 15);
		makeExcel.setWidth(6, 12);
		makeExcel.setWidth(7, 10);
		makeExcel.setWidth(8, 20);
		makeExcel.setWidth(9, 20);
		makeExcel.setWidth(10, 15);
		makeExcel.setWidth(11, 15);
		makeExcel.setWidth(12, 15);
		makeExcel.setWidth(13, 15);

		// 粗體
		makeExcel.setIBU("B");

		// 機密等級及基準日期
		makeExcel.setValue(1, 12, "機密等級：機密\n" + (iYear - 1911) + "." + String.format("%02d", iMonth), "R");

		List<Map<String, String>> fnAllList = new ArrayList<>();
		List<Map<String, String>> fnAllList2 = new ArrayList<>();

		try {

			fnAllList = lm065ServiceImpl.findAll(titaVo, yearMonth);

			// 共用LM062Impl
			fnAllList2 = lm062ServiceImpl.findList(titaVo, yearMonth, 4);

		} catch (Exception e) {

			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM065ServiceImpl.findAll error = " + errors.toString());

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
							makeExcel.setValue(row, i, tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? 0
									: Integer.valueOf(tLDVo.get(fdnm)), "R");
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
						// E欄 區域中心名稱
						makeExcel.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "L");
						break;
					case 6:
						// F欄 覆審單位
						makeExcel.setValue(row, i, tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "(空白)"
								: tLDVo.get(fdnm).equals("") ? "(空白)" : " ", "C");
						break;

					case 7:
						// G欄 覆審月份
						day = tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? 0
								: Integer.valueOf(tLDVo.get(fdnm));
						day = day % 100;

						makeExcel.setValue(row, i, day, "C");

						break;

					case 8:
						// H欄 撥款日期
						makeExcel.setValue(row, i, tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? 0
								: Integer.parseInt(tLDVo.get(fdnm)) - 19110000, "C");

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
					case 9:
						// I欄 放款餘額

						makeExcel.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? BigDecimal.ZERO
										: new BigDecimal(tLDVo.get(fdnm)),
								"#,##0", "R");
						// 所有金額總計
						tot = tot.add(new BigDecimal(tLDVo.get(fdnm)));
						break;
					case 10:
						// J欄 全戶餘額
						makeExcel.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? BigDecimal.ZERO
										: new BigDecimal(tLDVo.get(fdnm)),
								"#,##0", "R");
						break;
					case 11:
						// K欄 展期記號
						makeExcel.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "C");
						break;
					case 12:
						// L欄 應覆審案件
						makeExcel.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "C");
						break;
					case 13:
						// M欄 評等
						makeExcel.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 || tLDVo.get(fdnm).equals("0")
										? ""
										: tLDVo.get(fdnm),
								"C");
						break;
					default:

						break;
					}
				}
			}

			makeExcel.setValue(row + 1, 2, "總計", "L");
			makeExcel.setValue(row + 1, 8, lday - 19110000, "C");
			makeExcel.setValue(row + 1, 9, tot, "#,##0", "R");

		} else {

			makeExcel.setValue(3, 2, "本日無資料");
		}

		// 設定高度
		makeExcel.setHeight(3, 30);

		lm062report.dataList(fnAllList2, itemName);

		makeExcel.close();

	}

}
