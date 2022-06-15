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
import com.st1.itx.db.service.springjpa.cm.LM066ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LM066Report extends MakeReport {

	@Autowired
	LM066ServiceImpl lm066ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	public void exec(TitaVo titaVo,int yearMonth) throws LogicException {

		this.info("LM066Report exec");

		// 取得會計日(同頁面上會計日)
		// 年月日
//		int iEntdy = Integer.valueOf(titaVo.get("ENTDY")) + 19110000;
		// 年
		int iYear = yearMonth/ 100;
		// 月
		int iMonth = yearMonth % 100;


		String iYearMonth = String.valueOf(((iYear - 1911) * 100) + iMonth);

		this.info("yymm=" + iYearMonth);
		
		String txCD = titaVo.getTxcd();

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), txCD, "05-企金未達3000萬-" + iYearMonth,
				txCD+"_05-企金未達3000萬-" + iYearMonth, "LM066_底稿_企金未達3000萬.xls", "簡表");

		// 設定欄寬
		makeExcel.setWidth(2, 12);
		makeExcel.setWidth(3, 7);
		makeExcel.setWidth(4, 28);
		makeExcel.setWidth(5, 15);
		makeExcel.setWidth(6, 10);
		makeExcel.setWidth(7, 12);
		makeExcel.setWidth(8, 18);
		makeExcel.setWidth(9, 18);
		makeExcel.setWidth(10, 10);
		makeExcel.setWidth(11, 10);
		makeExcel.setWidth(12, 10);
		makeExcel.setWidth(13, 10);
		makeExcel.setWidth(14, 10);

		// 粗體
		makeExcel.setIBU("B");

		// 機密等級及基準日期
		makeExcel.setValue(1, 13, "機密等級：機密\n" + (iYear - 1911) + "." + String.format("%02d", iMonth), "R");

		List<Map<String, String>> fnAllList = new ArrayList<>();

		try {

			fnAllList = lm066ServiceImpl.findAll(titaVo,yearMonth);

		} catch (Exception e) {

			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM066ServiceImpl.findAll error = " + errors.toString());

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

			int yymm = 0;
			String xyymm = "";
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
						makeExcel.setIBU("B");
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
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "(空白)" : tLDVo.get(fdnm),
								"L");
						break;
					case 6:

						// F欄 覆審月份
						day = tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? 0
								: Integer.valueOf(tLDVo.get(fdnm));
						day = day % 100;

						makeExcel.setValue(row, i, day, "C");
						break;

					case 7:
						// G欄 撥款日期
						makeExcel.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? BigDecimal.ZERO
										: Integer.parseInt(tLDVo.get(fdnm)) - 19110000,
								"R");

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
					case 8:
						// H欄 放款餘額

						makeExcel.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? BigDecimal.ZERO
										: new BigDecimal(tLDVo.get(fdnm)),
								"#,##0", "R");
						// 所有金額總計
						tot = tot.add(new BigDecimal(tLDVo.get(fdnm)));
						break;
					case 9:
						// I欄 全戶餘額
						makeExcel.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? BigDecimal.ZERO
										: new BigDecimal(tLDVo.get(fdnm)),
								"#,##0", "R");
						break;
					case 10:
						// J欄 展期記號
						makeExcel.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "C");
						break;

					case 11:
						// K欄 應覆審案件
						makeExcel.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "C");
						break;
					/** 如何判斷舊件 **/
					case 12:
						// L欄 已覆審
						makeExcel.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "C");
						break;
					case 13:
						// M欄 已覆審日
						xyymm = tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm);
						if (xyymm.length() == 6) {
							// 202011
							yymm = Integer.parseInt(xyymm) - 191100;
							xyymm = String.valueOf(yymm).substring(0, 3) + "." + String.valueOf(yymm).substring(3, 5);
						} else if (xyymm.length() == 1) {

						} else {
							// 20209-19110=1099
							yymm = Integer.parseInt(xyymm) - 19110;
							if (String.valueOf(yymm).substring(3, 4).equals("0")) {
								xyymm = "";
							} else {

								xyymm = String.valueOf(yymm).substring(0, 3) + "."
										+ String.valueOf(yymm).substring(3, 4);
							}
						}

						makeExcel.setValue(row, i, xyymm, "C");

						break;
					case 14:
						// N欄 評等
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
			makeExcel.setValue(row + 1, 7, lday - 19110000, "R");
			makeExcel.setValue(row + 1, 8, tot, "#,##0", "R");

			// 畫框線
//			makeExcel.setAddRengionBorder("B", 3, "L", row + 1, 1);
			makeExcel.setColor("RED");
			makeExcel.setMergedRegionValue(row + 2, row + 2, 2, 14,
					"◎ 展期件當年度免辦理覆審，惟若第二年餘額超過1500萬（含）以上，須列為（展期後第二年度）必要覆審案件。", "L");
		} else {

			makeExcel.setValue(3, 2, "本日無資料");
		}

		makeExcel.close();
		//makeExcel.toExcel(sno);

	}

}
