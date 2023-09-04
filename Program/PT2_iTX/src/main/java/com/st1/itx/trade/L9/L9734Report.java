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
	MakeExcel makeExcel1;

	@Autowired
	MakeExcel makeExcel2;

	@Autowired
	MakeExcel makeExcel3;

	@Autowired
	MakeExcel makeExcel4;

	@Autowired
	MakeExcel makeExcel5;

	@Autowired
	MakeExcel makeExcel6;

	@Autowired
	L9734ServiceImpl l9734ServiceImpl;

	String txCD = "L9734";
	String txName = "覆審報表產製";

	/**
	 * 執行報表輸出
	 * 
	 * @param titaVo
	 * @param acDate    會計日期
	 * @param condition 報表區分代號
	 * @return
	 * @throws LogicException
	 *
	 * 
	 */
	public boolean exec(TitaVo titaVo, int acDate, int condition) throws LogicException {
		this.info(txCD + " exec-" + condition);

		switch (condition) {
		case 1:
			exportCond01(titaVo, acDate, condition);
			break;
		case 2:
			exportCond02(titaVo, acDate, condition);
			break;
		case 3:
			exportCond03(titaVo, acDate, condition);
			break;
		case 4:
			exportCond04(titaVo, acDate, condition);
			break;
		case 5:
			exportCond05(titaVo, acDate, condition);
			break;
		case 6:
			exportCond06(titaVo, acDate, condition);
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
	 * @param acDate
	 * @param condition
	 * @throws LogicException
	 */
	public void exportCond01(TitaVo titaVo, int acDate, int condition) throws LogicException {

		// 取得會計日(同頁面上會計日)
		// 年月日
		// 年
		int iYear = acDate / 10000;
		// 月
		int iMonth = acDate / 100 % 100;

		String itemName = "01-個金3000萬以上";

		int rocYYYMMDD = acDate - 19110000;

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "L9734";
		String fileItem = itemName + "-" + rocYYYMMDD;
		String fileName = txcd + "_" + itemName + "-" + rocYYYMMDD;
		String defaultExcel = txCD + "_底稿_個金3000萬以上.xls";
		String defaultSheet = "簡表";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		// 開啟報表
		makeExcel1.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);

		// 設定欄寬
		makeExcel1.setWidth(2, 12);
		makeExcel1.setWidth(3, 7);
		makeExcel1.setWidth(4, 10);
		makeExcel1.setWidth(5, 8);
		makeExcel1.setWidth(6, 18);
		makeExcel1.setWidth(7, 18);
		makeExcel1.setWidth(8, 15);
		makeExcel1.setWidth(9, 15);
		makeExcel1.setWidth(10, 15);
		makeExcel1.setWidth(11, 15);
		makeExcel1.setWidth(12, 15);
		makeExcel1.setWidth(13, 15);

		// 粗體
		makeExcel1.setIBU("B");

		// 機密等級及基準日期
		makeExcel1.setValue(1, 11,
				"機密等級：" + makeExcel1.getSecurity() + "\n" + (iYear - 1911) + "." + String.format("%02d", iMonth), "R");

		List<Map<String, String>> fnAllList = new ArrayList<>();

		try {

			fnAllList = l9734ServiceImpl.findAll(titaVo, acDate, condition);
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

			// 初貸日期
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
						// 戶號(數字右靠)
						if (!tempNo.equals(tLDVo.get(fdnm))) {

							tempNo = tLDVo.get(fdnm);
							makeExcel1.setValue(row, i,
									tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? BigDecimal.ZERO
											: Integer.valueOf(tLDVo.get(fdnm)),
									"R");

						} else {

							makeExcel1.setValue(row, i, "", "R");
						}
						break;
					case 3:
						// 額度
						makeExcel1.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? BigDecimal.ZERO
										: Integer.valueOf(tLDVo.get(fdnm)),
								"C");
						break;
					case 4:
						// 戶名
						makeExcel1.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "L");
						break;
					case 5:
						// 覆審月份
						day = tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? 0
								: Integer.valueOf(tLDVo.get(fdnm));
						day = day % 100;

						makeExcel1.setValue(row, i, day, "C");
						break;
					case 6:
						// 初貸日期
						makeExcel1.setValue(row, i, tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? 0
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
						// 繳息迄日
						makeExcel1.setValue(row, i, tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? 0
								: Integer.parseInt(tLDVo.get(fdnm)) - 19110000, "R");

						break;
					case 8:
						// 放款餘額

						makeExcel1.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? BigDecimal.ZERO
										: new BigDecimal(tLDVo.get(fdnm)),
								"#,##0", "R");

						// 所有金額總計
						tot = tot.add(new BigDecimal(tLDVo.get(fdnm)));

						break;
					case 9:
						// 全戶餘額
						makeExcel1.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? BigDecimal.ZERO
										: new BigDecimal(tLDVo.get(fdnm)),
								"#,##0", "R");
						break;
					case 10:
						// 展期記號
						makeExcel1.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "C");
						break;
					case 11:
						// 應覆審案件
						makeExcel1.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "C");
						break;
					case 12:
						// 評等
						makeExcel1.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 || tLDVo.get(fdnm).equals("0")
										? ""
										: tLDVo.get(fdnm),
								"C");
						break;
					case 13:
						// 覆審人員
						makeExcel1.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 || tLDVo.get(fdnm).equals("0")
										? ""
										: tLDVo.get(fdnm),
								"C");
						break;
					case 14:
						// 經辦人員
						makeExcel1.setValue(row, i,
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

			makeExcel1.setValue(row + 1, 2, "總計", "L");
			makeExcel1.setValue(row + 1, 7, lday - 19110000, "R");
			makeExcel1.setValue(row + 1, 9, tot, "#,##0", "R");

			// 畫框線
//			makeExcel6.setAddRengionBorder("B", 3, "H", row + 1, 1);

		} else {
			makeExcel1.setValue(3, 2, "本日無資料");
		}

//		dataList(fnAllList2, itemName);
		makeExcel1.close();

	}

	/**
	 * 覆審案件資料表-企金3000萬以上
	 * 
	 * @param titaVo
	 * @param acDate
	 * @param condition
	 * @throws LogicException
	 */
	public void exportCond02(TitaVo titaVo, int acDate, int condition) throws LogicException {

		// 取得會計日(同頁面上會計日)
		// 年月日
		// 年
		int iYear = acDate / 10000;
		// 月
		int iMonth = acDate / 100 % 100;

		String itemName = "02-企金3000萬以上";

		int rocYYYMMDD = acDate - 19110000;

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "L9734";
		String fileItem = itemName + "-" + rocYYYMMDD;
		String fileName = txcd + "_" + itemName + "-" + rocYYYMMDD;
		String defaultExcel = txCD + "_底稿_企金3000萬以上.xls";
		String defaultSheet = "簡表";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		// 開啟報表
		makeExcel2.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);

		// 設定欄寬
		makeExcel2.setWidth(2, 12);
		makeExcel2.setWidth(3, 7);
		makeExcel2.setWidth(4, 40);
		makeExcel2.setWidth(5, 15);
		makeExcel2.setWidth(6, 18);
		makeExcel2.setWidth(7, 20);
		makeExcel2.setWidth(8, 15);
		makeExcel2.setWidth(9, 15);
		makeExcel2.setWidth(10, 15);
		makeExcel2.setWidth(11, 15);
		makeExcel2.setWidth(12, 15);
		makeExcel2.setWidth(13, 15);

		// 粗體
		makeExcel2.setIBU("B");

		// 機密等級及基準日期
		makeExcel2.setValue(1, 11, "機密等級：機密\n" + (iYear - 1911) + "." + String.format("%02d", iMonth), "R");

		List<Map<String, String>> fnAllList = new ArrayList<>();

		try {

			fnAllList = l9734ServiceImpl.findAll(titaVo, acDate, condition);

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

			// 初貸日期
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
						// 戶號(數字右靠)
						if (!tempNo.equals(tLDVo.get(fdnm))) {
							tempNo = tLDVo.get(fdnm);
							makeExcel2.setValue(row, i, tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? 0
									: Integer.valueOf(tLDVo.get(fdnm)), "R");
						} else {
							makeExcel2.setValue(row, i, "", "R");
						}

						break;
					case 3:
						// 額度
						makeExcel2.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? BigDecimal.ZERO
										: Integer.valueOf(tLDVo.get(fdnm)),
								"C");
						break;
					case 4:
						// 戶名
						makeExcel2.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "L");
						break;
					case 5:
						// 覆審月份
						day = tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? 0
								: Integer.valueOf(tLDVo.get(fdnm));
						day = day % 100;

						makeExcel2.setValue(row, i, day, "C");
						break;
					case 6:
						// 初貸日期
						makeExcel2.setValue(row, i, tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? 0
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
						// 繳息迄日
						makeExcel2.setValue(row, i, tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? 0
								: Integer.parseInt(tLDVo.get(fdnm)) - 19110000, "R");

						break;
					case 8:
						// 放款餘額

						makeExcel2.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? BigDecimal.ZERO
										: new BigDecimal(tLDVo.get(fdnm)),
								"#,##0", "R");

						// 所有金額總計
						tot = tot.add(new BigDecimal(tLDVo.get(fdnm)));

						break;
					case 9:
						// 全戶餘額
						makeExcel2.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? BigDecimal.ZERO
										: new BigDecimal(tLDVo.get(fdnm)),
								"#,##0", "R");
						break;
					case 10:
						// 展期記號
						makeExcel2.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "C");
						break;
					case 11:
						// 應覆審案件
						makeExcel2.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "C");
						break;
					case 12:
						// 評等
						makeExcel2.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 || tLDVo.get(fdnm).equals("0")
										? ""
										: tLDVo.get(fdnm),
								"C");
						break;
					case 13:
						// 覆審人員
						makeExcel2.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 || tLDVo.get(fdnm).equals("0")
										? ""
										: tLDVo.get(fdnm),
								"C");
						break;
					case 14:
						// 經辦人員
						makeExcel2.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 || tLDVo.get(fdnm).equals("0")
										? ""
										: tLDVo.get(fdnm),
								"C");
						break;

					}
				}
			}

			makeExcel2.setValue(row + 1, 2, "總計", "L");
			makeExcel2.setValue(row + 1, 6, lday - 19110000, "R");
			makeExcel2.setValue(row + 1, 8, tot, "#,##0", "R");

		} else {
			makeExcel2.setValue(3, 2, "本日無資料");
		}

		makeExcel2.close();

	}

	/**
	 * 覆審案件資料表-個金2000萬以上小於3000萬
	 * 
	 * @param titaVo
	 * @param acDate
	 * @param condition
	 * @throws LogicException
	 */
	public void exportCond03(TitaVo titaVo, int acDate, int condition) throws LogicException {
		this.info("L9734Report exec");
		// 取得會計日(同頁面上會計日)
		// 年月日
		// 年
		int iYear = acDate / 10000;
		// 月
		int iMonth = acDate / 100 % 100;

		String itemName = "03-個金2000萬以上小於3000萬";

		int rocYYYMMDD = acDate - 19110000;

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "L9734";
		String fileItem = itemName + "-" + rocYYYMMDD;
		String fileName = txcd + "_" + itemName + "-" + rocYYYMMDD;
		String defaultExcel = txCD + "_底稿_個金2000萬以上小於3000萬.xls";
		String defaultSheet = "簡表";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		// 開啟報表
		makeExcel3.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);

		// 設定欄寬
		makeExcel3.setWidth(2, 12);
		makeExcel3.setWidth(3, 7);
		makeExcel3.setWidth(4, 13);
		makeExcel3.setWidth(5, 15);
		makeExcel3.setWidth(6, 12);
		makeExcel3.setWidth(7, 10);
		makeExcel3.setWidth(8, 20);
		makeExcel3.setWidth(9, 17);
		makeExcel3.setWidth(10, 15);
		makeExcel3.setWidth(11, 15);
		makeExcel3.setWidth(12, 15);
		makeExcel3.setWidth(13, 15);
		makeExcel3.setWidth(14, 15);
		makeExcel3.setWidth(15, 15);

		// 粗體
		makeExcel3.setIBU("B");

		// 機密等級及基準日期
		makeExcel3.setValue(1, 10, "機密等級：機密\n" + (iYear - 1911) + "." + String.format("%02d", iMonth), "R");

		List<Map<String, String>> fnAllList = new ArrayList<>();

		try {

			fnAllList = l9734ServiceImpl.findAll(titaVo, acDate, condition);

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

			// 初貸日期
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
						// 戶號(數字右靠)
						if (!tempNo.equals(tLDVo.get(fdnm))) {
							tempNo = tLDVo.get(fdnm);
							makeExcel3.setValue(row, i,
									tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? BigDecimal.ZERO
											: Integer.valueOf(tLDVo.get(fdnm)),
									"R");
						} else {
							makeExcel3.setValue(row, i, "", "R");
						}

						break;
					case 3:
						// 額度
						makeExcel3.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? BigDecimal.ZERO
										: Integer.valueOf(tLDVo.get(fdnm)),
								"C");
						break;
					case 4:
						// 戶名
						makeExcel3.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "L");
						break;
					// case 5 跟 6 先交換 再確認賦審單位 跟區域中心區別
					case 5:
						// 區域中心名稱
						makeExcel3.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "L");
						break;
					case 6:
						// 覆審單位
						makeExcel3.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "(空白)" : " ", "C");
						break;

					case 7:
						// 覆審月份
						day = tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? 0
								: Integer.valueOf(tLDVo.get(fdnm));
						day = day % 100;

						makeExcel3.setValue(row, i, day, "C");

						break;

					case 8:
						// 初貸日期
						makeExcel3.setValue(row, i, tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? 0
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
						// 繳息迄日
						makeExcel3.setValue(row, i, tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? 0
								: Integer.parseInt(tLDVo.get(fdnm)) - 19110000, "R");

						break;
					case 10:
						// 放款餘額

						makeExcel3.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? BigDecimal.ZERO
										: new BigDecimal(tLDVo.get(fdnm)),
								"#,##0", "R");
						// 所有金額總計
						tot = tot.add(new BigDecimal(tLDVo.get(fdnm)));
						break;

					case 11:
						// 全戶餘額
						makeExcel3.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? BigDecimal.ZERO
										: new BigDecimal(tLDVo.get(fdnm)),
								"#,##0", "R");
						break;
					case 12:
						// 展期記號
						makeExcel3.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "C");
						break;
					case 13:
						// 應覆審案件
						makeExcel3.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "C");
						break;
					case 14:
						// 評等
						makeExcel3.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 || tLDVo.get(fdnm).equals("0")
										? ""
										: tLDVo.get(fdnm),
								"C");
						break;
					case 15:
						// 覆審人員
						makeExcel3.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 || tLDVo.get(fdnm).equals("0")
										? ""
										: tLDVo.get(fdnm),
								"C");
						break;
					case 16:
						// 經辦人員
						makeExcel3.setValue(row, i,
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

			makeExcel3.setValue(row + 1, 2, "總計", "L");
			makeExcel3.setValue(row + 1, 8, lday - 19110000, "R");
			makeExcel3.setValue(row + 1, 10, tot, "#,##0", "R");

			// 畫框線
//			makeExcel3.setAddRengionBorder("B", 3, "K", row + 1, 1);

		} else {

			makeExcel3.setValue(3, 2, "本日無資料");
		}

		makeExcel3.close();

	}

	/**
	 * 覆審案件資料表-個金100萬以上小於2000萬
	 * 
	 * @param titaVo
	 * @param acDate
	 * @param condition
	 * @throws LogicException
	 */
	public void exportCond04(TitaVo titaVo, int acDate, int condition) throws LogicException {

		// 取得會計日(同頁面上會計日)
		// 年月日
		// 年
		int iYear = acDate / 10000;
		// 月
		int iMonth = acDate / 100 % 100;

		String itemName = "04-個金100萬以上小於2000萬";

		int rocYYYMMDD = acDate - 19110000;

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "L9734";
		String fileItem = itemName + "-" + rocYYYMMDD;
		String fileName = txcd + "_" + itemName + "-" + rocYYYMMDD;
		String defaultExcel = txCD + "_底稿_個金100萬以上小於2000萬.xls";
		String defaultSheet = "簡表";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		// 開啟報表
		makeExcel4.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);
		// 設定欄寬
		makeExcel4.setWidth(2, 12);
		makeExcel4.setWidth(3, 7);
		makeExcel4.setWidth(4, 15);
		makeExcel4.setWidth(5, 15);
		makeExcel4.setWidth(6, 12);
		makeExcel4.setWidth(7, 10);
		makeExcel4.setWidth(8, 20);
		makeExcel4.setWidth(9, 20);
		makeExcel4.setWidth(10, 15);
		makeExcel4.setWidth(11, 15);
		makeExcel4.setWidth(12, 15);
		makeExcel4.setWidth(13, 15);
		makeExcel4.setWidth(14, 15);
		makeExcel4.setWidth(15, 15);
		// 粗體
		makeExcel4.setIBU("B");

		// 機密等級及基準日期
		makeExcel4.setValue(1, 13, "機密等級：機密\n" + (iYear - 1911) + "." + String.format("%02d", iMonth), "R");

		List<Map<String, String>> fnAllList = new ArrayList<>();

		try {

			fnAllList = l9734ServiceImpl.findAll(titaVo, acDate, condition);

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

			// 初貸日期
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
						// 戶號(數字右靠)
						if (!tempNo.equals(tLDVo.get(fdnm))) {
							tempNo = tLDVo.get(fdnm);
							makeExcel4.setValue(row, i, tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? 0
									: Integer.valueOf(tLDVo.get(fdnm)), "R");
						} else {
							makeExcel4.setValue(row, i, "", "R");
						}

						break;
					case 3:
						// 額度
						makeExcel4.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? BigDecimal.ZERO
										: Integer.valueOf(tLDVo.get(fdnm)),
								"C");
						break;
					case 4:
						// 戶名
						makeExcel4.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "L");
						break;
					case 5:
						// 區域中心名稱
						makeExcel4.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "L");
						break;
					case 6:
						// 覆審單位
						makeExcel4.setValue(row, i, tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "(空白)"
								: tLDVo.get(fdnm).equals("") ? "(空白)" : " ", "C");
						break;

					case 7:
						// 覆審月份
						day = tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? 0
								: Integer.valueOf(tLDVo.get(fdnm));
						day = day % 100;

						makeExcel4.setValue(row, i, day, "C");

						break;

					case 8:
						// 初貸日期
						makeExcel4.setValue(row, i, tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? 0
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
						// 繳息迄日
						makeExcel4.setValue(row, i, tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? 0
								: Integer.parseInt(tLDVo.get(fdnm)) - 19110000, "R");

						break;
					case 10:
						// 放款餘額

						makeExcel4.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? BigDecimal.ZERO
										: new BigDecimal(tLDVo.get(fdnm)),
								"#,##0", "R");
						// 所有金額總計
						tot = tot.add(new BigDecimal(tLDVo.get(fdnm)));
						break;
					case 11:
						// 全戶餘額
						makeExcel4.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? BigDecimal.ZERO
										: new BigDecimal(tLDVo.get(fdnm)),
								"#,##0", "R");
						break;
					case 12:
						// 展期記號
						makeExcel4.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "C");
						break;
					case 13:
						// 應覆審案件
						makeExcel4.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "C");
						break;
					case 14:
						// 評等
						makeExcel4.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 || tLDVo.get(fdnm).equals("0")
										? ""
										: tLDVo.get(fdnm),
								"C");
						break;
					case 15:
						// 覆審人員
						makeExcel4.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 || tLDVo.get(fdnm).equals("0")
										? ""
										: tLDVo.get(fdnm),
								"C");
						break;
					case 16:
						// 經辦人員
						makeExcel4.setValue(row, i,
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

			makeExcel4.setValue(row + 1, 2, "總計", "L");
			makeExcel4.setValue(row + 1, 8, lday - 19110000, "C");
			makeExcel4.setValue(row + 1, 10, tot, "#,##0", "R");

		} else {

			makeExcel4.setValue(3, 2, "本日無資料");
		}

		// 設定高度
		makeExcel4.setHeight(3, 30);

		makeExcel4.close();

	}

	/**
	 * 覆審案件資料表-企金未達3000萬
	 * 
	 * @param titaVo
	 * @param acDate
	 * @param condition
	 * @throws LogicException
	 */
	public void exportCond05(TitaVo titaVo, int acDate, int condition) throws LogicException {

		// 取得會計日(同頁面上會計日)
		// 年月日
		// 年
		int iYear = acDate / 10000;
		// 月
		int iMonth = acDate / 100 % 100;

		String itemName = "05-企金未達3000萬";

		int rocYYYMMDD = acDate - 19110000;

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "L9734";
		String fileItem = itemName + "-" + rocYYYMMDD;
		String fileName = txcd + "_" + itemName + "-" + rocYYYMMDD;
		String defaultExcel = txCD + "_底稿_企金未達3000萬.xls";
		String defaultSheet = "簡表";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		// 開啟報表
		makeExcel5.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);

		// 設定欄寬
		makeExcel5.setWidth(2, 12);
		makeExcel5.setWidth(3, 7);
		makeExcel5.setWidth(4, 28);
		makeExcel5.setWidth(5, 15);
		makeExcel5.setWidth(6, 10);
		makeExcel5.setWidth(7, 12);
		makeExcel5.setWidth(8, 18);
		makeExcel5.setWidth(9, 18);
		makeExcel5.setWidth(10, 10);
		makeExcel5.setWidth(11, 10);
		makeExcel5.setWidth(12, 10);
		makeExcel5.setWidth(13, 10);
		makeExcel5.setWidth(14, 10);
		makeExcel5.setWidth(15, 10);
		makeExcel5.setWidth(16, 10);

		// 粗體
		makeExcel5.setIBU("B");

		// 機密等級及基準日期
		makeExcel5.setValue(1, 13, "機密等級：機密\n" + (iYear - 1911) + "." + String.format("%02d", iMonth), "R");

		List<Map<String, String>> fnAllList = new ArrayList<>();

		try {

			fnAllList = l9734ServiceImpl.findAll(titaVo, acDate, condition);

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

			// 初貸日期
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
						// 戶號(數字右靠)
						if (!tempNo.equals(tLDVo.get(fdnm))) {
							tempNo = tLDVo.get(fdnm);

							makeExcel5.setValue(row, i,
									tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? BigDecimal.ZERO
											: Integer.valueOf(tLDVo.get(fdnm)),
									"R");
						} else {
							makeExcel5.setValue(row, i, "", "R");
						}

						break;
					case 3:
						// 額度
						makeExcel5.setIBU("B");
						makeExcel5.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? BigDecimal.ZERO
										: Integer.valueOf(tLDVo.get(fdnm)),
								"C");
						break;
					case 4:
						// 戶名

						makeExcel5.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "L");
						break;
					case 5:
						// 區域中心名稱
						makeExcel5.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "(空白)" : tLDVo.get(fdnm),
								"L");
						break;
					case 6:

						// 覆審月份
						day = tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? 0
								: Integer.valueOf(tLDVo.get(fdnm));
						day = day % 100;

						makeExcel5.setValue(row, i, day, "C");
						break;

					case 7:
						// 初貸日期
						makeExcel5.setValue(row, i,
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
						// 繳息迄日
						makeExcel5.setValue(row, i, tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? 0
								: Integer.parseInt(tLDVo.get(fdnm)) - 19110000, "R");

						break;
					case 9:
						// 放款餘額

						makeExcel5.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? BigDecimal.ZERO
										: new BigDecimal(tLDVo.get(fdnm)),
								"#,##0", "R");
						// 所有金額總計
						tot = tot.add(new BigDecimal(tLDVo.get(fdnm)));
						break;
					case 10:
						// 全戶餘額
						makeExcel5.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? BigDecimal.ZERO
										: new BigDecimal(tLDVo.get(fdnm)),
								"#,##0", "R");
						break;
					case 11:
						// 展期記號
						makeExcel5.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "C");
						break;

					case 12:
						// 應覆審案件
						makeExcel5.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "C");
						break;
					/** 如何判斷舊件 **/
					case 13:
						// 已覆審
						makeExcel5.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "C");
						break;
					case 14:
						// 已覆審日
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

						makeExcel5.setValue(row, i, xyymm, "C");

						break;
					case 15:
						// 評等
						makeExcel5.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 || tLDVo.get(fdnm).equals("0")
										? ""
										: tLDVo.get(fdnm),
								"C");
						break;
					case 16:
						// 覆審人員
						makeExcel5.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 || tLDVo.get(fdnm).equals("0")
										? ""
										: tLDVo.get(fdnm),
								"C");
						break;
					case 17:
						// 經辦人員
						makeExcel5.setValue(row, i,
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

			makeExcel5.setValue(row + 1, 2, "總計", "L");
			makeExcel5.setValue(row + 1, 7, lday - 19110000, "R");
			makeExcel5.setValue(row + 1, 9, tot, "#,##0", "R");

			// 畫框線
//			makeExcel5.setAddRengionBorder("B", 3, "L", row + 1, 1);
			makeExcel5.setColor("RED");
			makeExcel5.setMergedRegionValue(row + 2, row + 2, 2, 14,
					"◎ 展期件當年度免辦理覆審，惟若第二年餘額超過1500萬（含）以上，須列為（展期後第二年度）必要覆審案件。", "L");
		} else {

			makeExcel5.setValue(3, 2, "本日無資料");
		}

		makeExcel5.close();

	}

	/**
	 * 土地貸款覆審表
	 * 
	 * @param titaVo
	 * @param acDate
	 * @param condition
	 * @throws LogicException
	 */
	public void exportCond06(TitaVo titaVo, int acDate, int condition) throws LogicException {
		// 取得會計日(同頁面上會計日)
		// 年月日
		// 年
		int iYear = acDate / 10000;
		// 月
		int iMonth = acDate / 100 % 100;

		String itemName = "06-土地覆審";

		int rocYYYMMDD = acDate - 19110000;

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "L9734";
		String fileItem = itemName + "-" + rocYYYMMDD;
		String fileName = txcd + "_" + itemName + "-" + rocYYYMMDD;
		String defaultExcel = txCD + "_底稿_土地追蹤.xls";
		String defaultSheet = "簡表";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		// 開啟報表
		makeExcel6.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);

		// 設定欄寬
		makeExcel6.setWidth(2, 12);
		makeExcel6.setWidth(3, 7);
		makeExcel6.setWidth(4, 28);
		makeExcel6.setWidth(5, 13);
		makeExcel6.setWidth(6, 10);
		makeExcel6.setWidth(7, 10);
		makeExcel6.setWidth(8, 20);
		makeExcel6.setWidth(9, 20);
		makeExcel6.setWidth(10, 20);
		makeExcel6.setWidth(11, 20);
		makeExcel6.setWidth(12, 20);
		makeExcel6.setWidth(13, 15);
		makeExcel6.setWidth(14, 15);
		makeExcel6.setWidth(15, 15);
		makeExcel6.setWidth(16, 50);
		makeExcel6.setWidth(17, 15);
		makeExcel6.setWidth(18, 15);

		// 粗體
		makeExcel6.setIBU("B");

		// 機密等級及基準日期
		makeExcel6.setValue(1, 17, "機密等級：機密\n" + (iYear - 1911) + "." + String.format("%02d", iMonth), "R");

		List<Map<String, String>> fnAllList = new ArrayList<>();

		try {
			fnAllList = l9734ServiceImpl.findAll(titaVo, acDate, condition);

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

			// 初貸日期
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
						// 戶號(數字右靠)
						if (!tempNo.equals(tLDVo.get(fdnm))) {
							tempNo = tLDVo.get(fdnm);
							makeExcel6.setValue(row, i,
									tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? BigDecimal.ZERO
											: Integer.valueOf(tLDVo.get(fdnm)),
									"R");
						} else {
							makeExcel6.setValue(row, i, "", "R");
						}

						break;
					case 3:
						// 額度
						makeExcel6.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? BigDecimal.ZERO
										: Integer.valueOf(tLDVo.get(fdnm)),
								"C");
						break;
					case 4:
						// 戶名
						makeExcel6.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "L");
						break;
					case 5:
						// 客戶別
						makeExcel6.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "L");
						break;
					case 6:
						// 用途別
						makeExcel6.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "L");

						break;

					case 7:
						// G欄位 地區別
						makeExcel6.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "C");

						break;

					case 8:
						// 覆審月份
						day = tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? 0
								: Integer.valueOf(tLDVo.get(fdnm));
						day = day % 100;

						makeExcel6.setValue(row, i, day, "C");

						break;
					case 9:
						// 初貸日期
						makeExcel6.setValue(row, i,
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
					case 10:
						// 繳息迄日
						makeExcel6.setValue(row, i, tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? 0
								: Integer.parseInt(tLDVo.get(fdnm)) - 19110000, "R");

						break;
					case 11:
						// 放款餘額
						BigDecimal f10 = tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? BigDecimal.ZERO
								: new BigDecimal(tLDVo.get(fdnm));
						makeExcel6.setValue(row, i, f10, "#,##0", "R");
						// 所有金額總計

						tot = tot.add(f10);

						break;

					case 12:
						// 全戶餘額
						makeExcel6.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? BigDecimal.ZERO
										: new BigDecimal(tLDVo.get(fdnm)),
								"#,##0", "R");
						break;
					case 13:
						// 展期記號
						makeExcel6.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "C");
						break;

					case 14:
						// 是否追蹤
						makeExcel6.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "C");
						break;
					case 15:
						// 應覆審單位
						makeExcel6.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "C");
						break;
					case 16:
						// 評等
						makeExcel6.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 || tLDVo.get(fdnm).equals("0")
										? ""
										: tLDVo.get(fdnm),
								"C");
						break;
					case 17:
						// 備註
						makeExcel6.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "L");
						break;
					case 18:
						// 覆審人員
						makeExcel6.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 || tLDVo.get(fdnm).equals("0")
										? ""
										: tLDVo.get(fdnm),
								"C");
						break;
					case 19:
						// 經辦人員
						makeExcel6.setValue(row, i,
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

			makeExcel6.setValue(row + 1, 2, "總計", "L");
			makeExcel6.setValue(row + 1, 9, lday, "R");
			makeExcel6.setValue(row + 1, 11, tot, "#,##0", "R");

		} else {

			makeExcel6.setValue(3, 2, "本日無資料");
		}

		makeExcel6.close();

	}

	/**
	 * 資料明細
	 * 
	 * @param list
	 * @param sheetName 工作表名稱
	 * @throws LogicException
	 */
	public void dataList(List<Map<String, String>> list, String sheetName) throws LogicException {

		makeExcel6.setSheet(sheetName);

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
			makeExcel6.setValue(row, 1, conditionCode, "R");
			// 營業單位
			makeExcel6.setValue(row, 2, branchNo, "R");
			// 戶號
			makeExcel6.setValue(row, 3, custNo, "#######", "R");
			// 額度
			makeExcel6.setValue(row, 4, facmNo, "R");
			// 撥款
			makeExcel6.setValue(row, 5, bormNo, "R");
			// 戶名
			makeExcel6.setValue(row, 6, custName, "L");

			// 初貸日期
			makeExcel6.setValue(row, 7, drawdownDate, "", "R");
			// 放款餘額
			makeExcel6.setValue(row, 8, loanBal, "#,##0", "R");
			// 到期日
			makeExcel6.setValue(row, 9, maturityDate, "R");
			// 押品1
			makeExcel6.setValue(row, 10, clcode1, "R");
			// 押品2
			makeExcel6.setValue(row, 11, clcode2, "R");
			// 押品號碼
			makeExcel6.setValue(row, 12, clno, "R");
			// 縣市
			makeExcel6.setValue(row, 13, cityShort, "L");
			// 鄉鎮區
			makeExcel6.setValue(row, 14, areaShort, "L");
			// 段
			makeExcel6.setValue(row, 15, part1, "L");
			// 小段
			makeExcel6.setValue(row, 16, part2, "L");
			// 門牌號碼
			makeExcel6.setValue(row, 17, location, "L");
			// 地區別
			makeExcel6.setValue(row, 18, cityCode, "L");
			// 區域中心
			makeExcel6.setValue(row, 19, unit, "L");
			// 抽樣總戶數
			makeExcel6.setValue(row, 20, sampleNum, "R");
			// 抽樣別
			makeExcel6.setValue(row, 21, sampleType, "L");
			// 覆審月份
			makeExcel6.setValue(row, 22, rechYM, "R");
			// 增貸案件
			makeExcel6.setValue(row, 23, usageItem, "L");
			// 資料說明(備註)
			makeExcel6.setValue(row, 24, remark, "L");

			row++;
		}

	}
}
