package com.st1.itx.trade.L9;

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
import com.st1.itx.db.service.springjpa.cm.L9734ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")
public class L9734Report extends MakeReport {

	@Autowired
	DateUtil dateUtil;

	@Autowired
	Parse parse;

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	L9734ServiceImpl l9734ServiceImpl;

	String txCD = "L9734";
	String txName = "覆審報表產製";

	/**
	 * 執行報表輸出
	 * 
	 * @param titaVo
	 * @param yearMonth 西元年月
	 * @param condition 報表區分代號
	 * @return
	 * @throws LogicException
	 *
	 * 
	 */
	public boolean exec(TitaVo titaVo, int yearMonth, int condition) throws LogicException {
		this.info(txCD + " exec-" + condition);

		switch (condition) {
		case 1:
			exportCond01(titaVo, yearMonth, condition);
			break;
		case 2:
			exportCond02(titaVo, yearMonth, condition);
			break;
		case 3:
			exportCond03(titaVo, yearMonth, condition);
			break;
		case 4:
			exportCond04(titaVo, yearMonth, condition);
			break;
		case 5:
			exportCond05(titaVo, yearMonth, condition);
			break;
		case 6:
			exportCond06(titaVo, yearMonth, condition);
			break;
		default:
			break;
		}
		return true;

	}

	/**
	 * 覆審案件資料表-個金3000萬以上
	 * 
	 * @param titaVo
	 * @param yearMonth
	 * @param condition
	 * @throws LogicException
	 */
	public void exportCond01(TitaVo titaVo, int yearMonth, int condition) throws LogicException {

		// 取得會計日(同頁面上會計日)
		// 年月日
		// 年
		int iYear = yearMonth / 100;
		// 月
		int iMonth = yearMonth % 100;

		String iYearMonth = String.valueOf(((iYear - 1911) * 100) + iMonth);

		this.info("yymm=" + iYearMonth);

		String itemName = "01-個金3000萬以上";

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "L9734";
		String fileItem = itemName + "-" + iYearMonth;
		String fileName = txcd + "_" + itemName + "-" + iYearMonth;
		String defaultExcel = txCD + "_底稿_個金3000萬以上.xls";
		String defaultSheet = "簡表";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);

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
		makeExcel.setValue(1, 10,
				"機密等級：" + makeExcel.getSecurity() + "\n" + (iYear - 1911) + "." + String.format("%02d", iMonth), "R");

		List<Map<String, String>> fnAllList = new ArrayList<>();
//		List<Map<String, String>> fnAllList2 = new ArrayList<>();

		try {

			fnAllList = l9734ServiceImpl.findAll(titaVo, yearMonth, condition);
//			fnAllList2 = l9734ServiceImpl.findList(titaVo, yearMonth, condition);
		} catch (Exception e) {

			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L9734ServiceImpl.findAll error = " + errors.toString());

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
			makeExcel.setValue(row + 1, 6, lday - 19110000, "R");
			makeExcel.setValue(row + 1, 7, tot, "#,##0", "R");

			// 畫框線
//			makeExcel.setAddRengionBorder("B", 3, "H", row + 1, 1);

		} else {
			makeExcel.setValue(3, 2, "本日無資料");
		}

//		dataList(fnAllList2, itemName);

		makeExcel.close();

	}

	/**
	 * 覆審案件資料表-企金3000萬以上
	 * 
	 * @param titaVo
	 * @param yearMonth
	 * @param condition
	 * @throws LogicException
	 */
	public void exportCond02(TitaVo titaVo, int yearMonth, int condition) throws LogicException {

		// 年
		int iYear = yearMonth / 100;
		// 月
		int iMonth = yearMonth % 100;

		String iYearMonth = String.valueOf(((iYear - 1911) * 100) + iMonth);

		String itemName = "02-企金3000萬以上";

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "L9734";
		String fileItem = itemName + "-" + iYearMonth;
		String fileName = txcd + "_" + itemName + "-" + iYearMonth;
		String defaultExcel = txCD + "_底稿_企金3000萬以上.xls";
		String defaultSheet = "簡表";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);

		// 設定欄寬
		makeExcel.setWidth(2, 12);
		makeExcel.setWidth(3, 7);
		makeExcel.setWidth(4, 40);
		makeExcel.setWidth(5, 15);
		makeExcel.setWidth(6, 18);
		makeExcel.setWidth(7, 20);
		makeExcel.setWidth(8, 15);
		makeExcel.setWidth(9, 15);
		makeExcel.setWidth(10, 15);
		makeExcel.setWidth(11, 15);

		// 粗體
		makeExcel.setIBU("B");

		// 機密等級及基準日期
		makeExcel.setValue(1, 10, "機密等級："+ makeExcel.getSecurity() +"\n" + (iYear - 1911) + "." + String.format("%02d", iMonth), "R");

		List<Map<String, String>> fnAllList = new ArrayList<>();
//		List<Map<String, String>> fnAllList2 = new ArrayList<>();

		try {

			fnAllList = l9734ServiceImpl.findAll(titaVo, yearMonth, condition);
//			fnAllList2 = l9734ServiceImpl.findList(titaVo, yearMonth, condition);

		} catch (Exception e) {

			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L9734ServiceImpl.findAll error = " + errors.toString());

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
			makeExcel.setValue(row + 1, 6, lday - 19110000, "R");
			makeExcel.setValue(row + 1, 7, tot, "#,##0", "R");

		} else {
			makeExcel.setValue(3, 2, "本日無資料");
		}

//		dataList(fnAllList2, itemName);

		makeExcel.close();

	}

	/**
	 * 覆審案件資料表-個金2000萬以上小於3000萬
	 * 
	 * @param titaVo
	 * @param yearMonth
	 * @param condition
	 * @throws LogicException
	 */
	public void exportCond03(TitaVo titaVo, int yearMonth, int condition) throws LogicException {
		this.info("L9734Report exec");

		// 年
		int iYear = yearMonth / 100;
		// 月
		int iMonth = yearMonth % 100;

		String iYearMonth = String.valueOf(((iYear - 1911) * 100) + iMonth);

		this.info("yymm=" + iYearMonth);

		String itemName = "03-個金2000萬以上小於3000萬";

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "L9734";
		String fileItem = itemName + "-" + iYearMonth;
		String fileName = txcd + "_" + itemName + "-" + iYearMonth;
		String defaultExcel = txCD + "_底稿_個金2000萬以上小於3000萬.xls";
		String defaultSheet = "簡表";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);

		// 設定欄寬
		makeExcel.setWidth(2, 12);
		makeExcel.setWidth(3, 7);
		makeExcel.setWidth(4, 13);
		makeExcel.setWidth(5, 15);
		makeExcel.setWidth(6, 12);
		makeExcel.setWidth(7, 10);
		makeExcel.setWidth(8, 20);
		makeExcel.setWidth(9, 17);
		makeExcel.setWidth(10, 15);
		makeExcel.setWidth(11, 15);
		makeExcel.setWidth(12, 15);
		makeExcel.setWidth(13, 15);

		// 粗體
		makeExcel.setIBU("B");

		// 機密等級及基準日期
		makeExcel.setValue(1, 12, "機密等級："+ makeExcel.getSecurity() +"\n" + (iYear - 1911) + "." + String.format("%02d", iMonth), "R");

		List<Map<String, String>> fnAllList = new ArrayList<>();
//		List<Map<String, String>> fnAllList2 = new ArrayList<>();

		try {

			fnAllList = l9734ServiceImpl.findAll(titaVo, yearMonth, condition);
//			fnAllList2 = l9734ServiceImpl.findList(titaVo, yearMonth, condition);

		} catch (Exception e) {

			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L9734ServiceImpl.findAll error = " + errors.toString());

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
					// case 5 跟 6 先交換 再確認賦審單位 跟區域中心區別
					case 5:
						// E欄 區域中心名稱
						makeExcel.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "L");
						break;
					case 6:
						// F欄 覆審單位
						makeExcel.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "(空白)" : " ", "C");
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
			makeExcel.setValue(row + 1, 8, lday - 19110000, "R");
			makeExcel.setValue(row + 1, 9, tot, "#,##0", "R");

			// 畫框線
//			makeExcel.setAddRengionBorder("B", 3, "K", row + 1, 1);

		} else {

			makeExcel.setValue(3, 2, "本日無資料");
		}

//		dataList(fnAllList2, itemName);

		makeExcel.close();

	}

	/**
	 * 覆審案件資料表-個金100萬以上小於2000萬
	 * 
	 * @param titaVo
	 * @param yearMonth
	 * @param condition
	 * @throws LogicException
	 */
	public void exportCond04(TitaVo titaVo, int yearMonth, int condition) throws LogicException {

		// 年
		int iYear = yearMonth / 100;
		// 月
		int iMonth = yearMonth % 100;

		String iYearMonth = String.valueOf(((iYear - 1911) * 100) + iMonth);

		this.info("yymm=" + iYearMonth);

		String itemName = "04-個金100萬以上小於2000萬";

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "L9734";
		String fileItem = itemName + "-" + iYearMonth;
		String fileName = txcd + "_" + itemName + "-" + iYearMonth;
		String defaultExcel = txCD + "_底稿_個金100萬以上小於2000萬.xls";
		String defaultSheet = "簡表";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);
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
		makeExcel.setValue(1, 12, "機密等級："+ makeExcel.getSecurity() +"\n" + (iYear - 1911) + "." + String.format("%02d", iMonth), "R");

		List<Map<String, String>> fnAllList = new ArrayList<>();
//		List<Map<String, String>> fnAllList2 = new ArrayList<>();

		try {

			fnAllList = l9734ServiceImpl.findAll(titaVo, yearMonth, condition);
//			fnAllList2 = l9734ServiceImpl.findList(titaVo, yearMonth, condition);

		} catch (Exception e) {

			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L9734ServiceImpl.findAll error = " + errors.toString());

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

//		dataList(fnAllList2, itemName);

		makeExcel.close();

	}

	/**
	 * 覆審案件資料表-企金未達3000萬
	 * 
	 * @param titaVo
	 * @param yearMonth
	 * @param condition
	 * @throws LogicException
	 */
	public void exportCond05(TitaVo titaVo, int yearMonth, int condition) throws LogicException {

		// 年
		int iYear = yearMonth / 100;
		// 月
		int iMonth = yearMonth % 100;

		String iYearMonth = String.valueOf(((iYear - 1911) * 100) + iMonth);

		this.info("yymm=" + iYearMonth);

		String itemName = "05-企金未達3000萬";

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "L9734";
		String fileItem = itemName + "-" + iYearMonth;
		String fileName = txcd + "_" + itemName + "-" + iYearMonth;
		String defaultExcel = txCD + "_底稿_企金未達3000萬.xls";
		String defaultSheet = "簡表";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);

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
		makeExcel.setValue(1, 13, "機密等級："+ makeExcel.getSecurity() +"\n" + (iYear - 1911) + "." + String.format("%02d", iMonth), "R");

		List<Map<String, String>> fnAllList = new ArrayList<>();
//		List<Map<String, String>> fnAllList2 = new ArrayList<>();

		try {

			fnAllList = l9734ServiceImpl.findAll(titaVo, yearMonth, condition);
//			fnAllList2 = l9734ServiceImpl.findList(titaVo, yearMonth, condition);

		} catch (Exception e) {

			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L9734ServiceImpl.findAll error = " + errors.toString());

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

//		dataList(fnAllList2, itemName);

		makeExcel.close();

	}

	/**
	 * 土地貸款覆審表
	 * 
	 * @param titaVo
	 * @param yearMonth
	 * @param condition
	 * @throws LogicException
	 */
	public void exportCond06(TitaVo titaVo, int yearMonth, int condition) throws LogicException {
		// 年
		int iYear = yearMonth / 100;
		// 月
		int iMonth = yearMonth % 100;

		String iYearMonth = String.valueOf(((iYear - 1911) * 100) + iMonth);

		this.info("yymm=" + iYearMonth);

		String itemName = "土地覆審";

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "L9734";
		String fileItem = itemName + "-" + iYearMonth;
		String fileName = txcd + "_" + itemName + "-" + iYearMonth;
		String defaultExcel = txCD + "_底稿_土地追蹤.xls";
		String defaultSheet = "簡表";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);

		// 設定欄寬
		makeExcel.setWidth(2, 12);
		makeExcel.setWidth(3, 7);
		makeExcel.setWidth(4, 28);
		makeExcel.setWidth(5, 13);
		makeExcel.setWidth(6, 10);
		makeExcel.setWidth(7, 10);
		makeExcel.setWidth(8, 20);
		makeExcel.setWidth(9, 20);
		makeExcel.setWidth(10, 20);
		makeExcel.setWidth(11, 20);
		makeExcel.setWidth(12, 20);
		makeExcel.setWidth(13, 15);
		makeExcel.setWidth(14, 15);
		makeExcel.setWidth(15, 15);
		makeExcel.setWidth(16, 50);

		// 粗體
		makeExcel.setIBU("B");

		// 機密等級及基準日期
		makeExcel.setValue(1, 16, "機密等級："+ makeExcel.getSecurity() +"\n" + (iYear - 1911) + "." + String.format("%02d", iMonth), "R");

		List<Map<String, String>> fnAllList = new ArrayList<>();
//		List<Map<String, String>> fnAllList2 = new ArrayList<>();

		try {
			fnAllList = l9734ServiceImpl.findAll(titaVo, yearMonth, condition);
//			fnAllList2 = l9734ServiceImpl.findList(titaVo, yearMonth, condition);

		} catch (Exception e) {

			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L9734ServiceImpl.findAll error = " + errors.toString());

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
						// E欄 客戶別
						makeExcel.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "L");
						break;
					case 6:
						// F欄 用途別
						makeExcel.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "L");

						break;

					case 7:
						// G欄位 地區別
						makeExcel.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "C");

						break;

					case 8:
						// H欄 覆審月份
						day = tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? 0
								: Integer.valueOf(tLDVo.get(fdnm));
						day = day % 100;

						makeExcel.setValue(row, i, day, "C");

						break;
					case 9:
						// I欄 撥款日期
						makeExcel.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? BigDecimal.ZERO
										: Integer.parseInt(tLDVo.get(fdnm)),
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
					case 10:
						// J欄 放款餘額
						BigDecimal f10 = tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? BigDecimal.ZERO
								: new BigDecimal(tLDVo.get(fdnm));
						makeExcel.setValue(row, i, f10, "#,##0", "R");
						// 所有金額總計

						tot = tot.add(f10);

						break;

					case 11:
						// K欄 全戶餘額
						makeExcel.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? BigDecimal.ZERO
										: new BigDecimal(tLDVo.get(fdnm)),
								"#,##0", "R");
						break;
					case 12:
						// L欄 展期記號
						makeExcel.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "C");
						break;

					case 13:
						// M欄 是否追蹤
						makeExcel.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "C");
						break;
					case 14:
						// N欄 應覆審單位
						makeExcel.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "C");
						break;
					case 15:
						// O欄 評等
						makeExcel.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 || tLDVo.get(fdnm).equals("0")
										? ""
										: tLDVo.get(fdnm),
								"C");
						break;
					case 16:
						// P欄 1備註
						makeExcel.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "L");
						break;
					default:

						break;
					}
				}
			}

			makeExcel.setValue(row + 1, 2, "總計", "L");
			makeExcel.setValue(row + 1, 9, lday, "R");
			makeExcel.setValue(row + 1, 10, tot, "#,##0", "R");

		} else {

			makeExcel.setValue(3, 2, "本日無資料");
		}

//		dataList(fnAllList2, itemName);

		makeExcel.close();

	}

	/**
	 * 資料明細
	 * 
	 * @param list
	 * @param sheetName 工作表名稱
	 * @throws LogicException
	 */
	public void dataList(List<Map<String, String>> list, String sheetName) throws LogicException {

		makeExcel.setSheet(sheetName);

		int row = 2;

		for (Map<String, String> r : list) {

			int conditionCode = Integer.valueOf(r.get("F0"));
			String branchNo = r.get("F1");
			int custNo = Integer.valueOf(r.get("F2").isEmpty() || r.get("F2") == null ? "0" : r.get("F2"));
			int facmNo = Integer.valueOf(r.get("F3").isEmpty() || r.get("F3") == null ? "0" : r.get("F3"));
			int bormNo = Integer.valueOf(r.get("F4").isEmpty() || r.get("F4") == null ? "0" : r.get("F4"));
			String custName = r.get("F5");
			int drawdownDate = Integer.valueOf(r.get("F6").isEmpty() || r.get("F6") == null ? "0" : r.get("F6"));
			BigDecimal loanBal = new BigDecimal(r.get("F7"));
			int maturityDate = Integer.valueOf(r.get("F8"));
			int clcode1 = Integer.valueOf(r.get("F9").isEmpty() || r.get("F9") == null ? "0" : r.get("F9"));
			int clcode2 = Integer.valueOf(r.get("F10").isEmpty() || r.get("F10") == null ? "0" : r.get("F10"));
			int clno = Integer.valueOf(r.get("F11").isEmpty() || r.get("F11") == null ? "0" : r.get("F11"));
			String cityShort = r.get("F12");
			String areaShort = r.get("F13");
			String part1 = r.get("F14");
			String part2 = r.get("F15");
			String location = r.get("F16");
			int cityCode = Integer.valueOf(r.get("F17").isEmpty() || r.get("F17") == null ? "0" : r.get("F17"));
			String unit = r.get("F18");
			String sampleNum = r.get("F19");
			String sampleType = r.get("F20");
			int rechYM = Integer.valueOf(r.get("F21").trim().length() == 0 ? "0" : r.get("F21"));
			String usageItem = r.get("F22");
			String remark = r.get("F23");

			// 條件代碼
			makeExcel.setValue(row, 1, conditionCode, "R");
			// 營業單位
			makeExcel.setValue(row, 2, branchNo, "R");
			// 戶號
			makeExcel.setValue(row, 3, custNo, "#######", "R");
			// 額度
			makeExcel.setValue(row, 4, facmNo, "R");
			// 撥款
			makeExcel.setValue(row, 5, bormNo, "R");
			// 戶名
			makeExcel.setValue(row, 6, custName, "L");

			// 撥款日期
			makeExcel.setValue(row, 7, drawdownDate, "", "R");
			// 放款餘額
			makeExcel.setValue(row, 8, loanBal, "#,##0", "R");
			// 到期日
			makeExcel.setValue(row, 9, maturityDate, "R");
			// 押品1
			makeExcel.setValue(row, 10, clcode1, "R");
			// 押品2
			makeExcel.setValue(row, 11, clcode2, "R");
			// 押品號碼
			makeExcel.setValue(row, 12, clno, "R");
			// 縣市
			makeExcel.setValue(row, 13, cityShort, "L");
			// 鄉鎮區
			makeExcel.setValue(row, 14, areaShort, "L");
			// 段
			makeExcel.setValue(row, 15, part1, "L");
			// 小段
			makeExcel.setValue(row, 16, part2, "L");
			// 門牌號碼
			makeExcel.setValue(row, 17, location, "L");
			// 地區別
			makeExcel.setValue(row, 18, cityCode, "L");
			// 區域中心
			makeExcel.setValue(row, 19, unit, "L");
			// 抽樣總戶數
			makeExcel.setValue(row, 20, sampleNum, "R");
			// 抽樣別
			makeExcel.setValue(row, 21, sampleType, "L");
			// 覆審月份
			makeExcel.setValue(row, 22, rechYM, "R");
			// 增貸案件
			makeExcel.setValue(row, 23, usageItem, "L");
			// 資料說明(備註)
			makeExcel.setValue(row, 24, remark, "L");

			row++;
		}

	}
}
