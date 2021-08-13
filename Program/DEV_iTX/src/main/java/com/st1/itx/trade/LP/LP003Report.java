package com.st1.itx.trade.LP;

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
import com.st1.itx.db.service.springjpa.cm.LP003ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LP003Report extends MakeReport {
	// private static final Logger logger = LoggerFactory.getLogger(LP003Report.class);

	@Autowired
	LP003ServiceImpl LP003ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printTitle() {
	}
 
	List<Map<String, String>> findList = new ArrayList<>();

	BigDecimal[] gamtArr = null;
	BigDecimal[] cntArr = null;
	BigDecimal[] amtArr = null;

	// 前工作月累計
	BigDecimal gamtTotal = BigDecimal.ZERO;
	BigDecimal cntTotal = BigDecimal.ZERO;
	BigDecimal amtTotal = BigDecimal.ZERO;

	// 兩欄位一組
	// 第1組欄位 縱向累計
	BigDecimal gamtColTotal1 = BigDecimal.ZERO;
	BigDecimal cntColTotal1 = BigDecimal.ZERO;
	BigDecimal amtColTotal1 = BigDecimal.ZERO;

	// 第2組欄位 縱向累計
	BigDecimal gamtColTotal2 = BigDecimal.ZERO;
	BigDecimal cntColTotal2 = BigDecimal.ZERO;
	BigDecimal amtColTotal2 = BigDecimal.ZERO;

	// 第3組欄位 縱向累計
	BigDecimal gamtColTotal3 = BigDecimal.ZERO;
	BigDecimal cntColTotal3 = BigDecimal.ZERO;
	BigDecimal amtColTotal3 = BigDecimal.ZERO;

	// 第4組欄位 縱向累計
	BigDecimal gamtColTotal4 = BigDecimal.ZERO;
	BigDecimal cntColTotal4 = BigDecimal.ZERO;
	BigDecimal amtColTotal4 = BigDecimal.ZERO;

	// 第5組欄位 縱向累計
	BigDecimal gamtColTotal5 = BigDecimal.ZERO;
	BigDecimal cntColTotal5 = BigDecimal.ZERO;
	BigDecimal amtColTotal5 = BigDecimal.ZERO;

	// 最後一列的 縱向累計
	BigDecimal gamtColTotal = BigDecimal.ZERO;
	BigDecimal cntColTotal = BigDecimal.ZERO;
	BigDecimal amtColTotal = BigDecimal.ZERO;

	// 單一列 合計
	BigDecimal gamtRowTotal = BigDecimal.ZERO;
	BigDecimal cntRowTotal = BigDecimal.ZERO;
	BigDecimal amtRowTotal = BigDecimal.ZERO;

	// 全工作月合計
	BigDecimal gamtColAllTotal = BigDecimal.ZERO;
	BigDecimal cntColAllTotal = BigDecimal.ZERO;
	BigDecimal amtColAllTotal = BigDecimal.ZERO;

	int dmm = 0;
	int cnt = 0;
	int amt = 0;
	int scol = 0;

	// 欄位文字 月分head
	String monthHead = "";

	// 起始列
	int sRow = 0;
	int tRow = 0;

	// 陣列大小
	int size = 0;

	public void exec(TitaVo titaVo) throws LogicException {

		this.info("LP003Report exec");

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LP003", "部專暨房專業績累計表", "LP003部專暨房專業績累計表",
				"部專暨房專業績累計表.xls", "部專");

		List<Map<String, String>> wkSsnList = new ArrayList<>();

		try {
			wkSsnList = LP003ServiceImpl.wkSsn(titaVo);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LP003ServiceImpl.wkSsn error = " + errors.toString());
		}

		exportExcel(titaVo, wkSsnList.get(0));

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

	/**
	 * 執行Excel產表
	 * 
	 * @param titaVo
	 * @param wkSsnVo 工作年&月
	 * 
	 */
	private void exportExcel(TitaVo titaVo, Map<String, String> wkSsnVo) throws LogicException {
		// 民國年
		String ROCYear = String.valueOf(Integer.valueOf(wkSsnVo.get("F0")) - 1911);

		// 當前工作月
		int wkMonth = Integer.valueOf(wkSsnVo.get("F1"));

		// 工作月
//		monthHead = wkSsnVo.get("F1") == "1" ? "1" : "1~" + wkSsnVo.get("F1");

		if (Integer.parseInt(wkSsnVo.get("F1")) == 1) {
			ROCYear = String.valueOf(Integer.parseInt(wkSsnVo.get("F0")) - 1912);
			wkMonth = 13;
			monthHead = "13";
		} else {
			ROCYear = String.valueOf(Integer.valueOf(wkSsnVo.get("F0")) - 1911);
			wkMonth = Integer.parseInt(wkSsnVo.get("F1")) - 1;
			monthHead = String.valueOf(Integer.parseInt(wkSsnVo.get("F1")) - 1);
		}

		// 起始欄位
		int col = 0;

		// 上季末 工作月
		int lastWkMonth = 0;

		// 陣列大小
		int pos = 6;

		// 區分上季末 工作月
		if (wkMonth <= 3) {

			lastWkMonth = 0;

		} else if (wkMonth <= 6) {

			lastWkMonth = 3;

		} else if (wkMonth <= 9) {

			lastWkMonth = 6;

		} else if (wkMonth <= 13) {

			lastWkMonth = 9;

		}

		gamtArr = new BigDecimal[pos];

		cntArr = new BigDecimal[pos];

		amtArr = new BigDecimal[pos];

		/*--------------------------------------------------------------------------*/
		col = 4;
		makeExcel.setSheet("部專");
		makeExcel.setValue(1, 1, ROCYear + "年第1~" + monthHead + "工作月房貸部專業績累計明細表");

		// 建攔位
		setColTitle(col, wkMonth, lastWkMonth);

		// 產出部專
		setDept(titaVo, wkSsnVo, wkMonth, lastWkMonth);
		/*--------------------------------------------------------------------------*/

		/*--------------------------------------------------------------------------*/
		col = 6;

		makeExcel.setSheet("專員");
		makeExcel.setValue(1, 1, ROCYear + "年第1~" + monthHead + "工作月房貸專員業績累計明細表");

		// 建攔位
		setColTitle(col, wkMonth, lastWkMonth);

		// 產出專員
		setEmp(titaVo, wkSsnVo, wkMonth, lastWkMonth);

		/*--------------------------------------------------------------------------*/

		// 產出專員明細(sheet1)

		setEmpThisWKM(titaVo, wkSsnVo, wkMonth);

		// 產出部專明細(sheet2)

		setDeptThisWKM(titaVo, wkSsnVo, wkMonth);

	}

	/**
	 * 建立標頭
	 * 
	 * @param startCol    起始攔
	 * @param wkMonth     本工作月
	 * @param lastWkMonth 上季末工作月
	 */
	private void setColTitle(int startCol, int wkMonth, int lastWkMonth) throws LogicException {
		// 起始列
		int row2 = 2;
		int row3 = 3;

		// 是否建立前工作月累計 欄位
		boolean firstCreate = true;

		for (int i = 1; i <= wkMonth; i++) {

			// 只有 3工作月以前 不建累計欄位
			if (wkMonth <= 3) {

				// 設置當前攔寬度
				makeExcel.setWidth(startCol, 15);
				makeExcel.setWidth(startCol + 1, 15);
				makeExcel.setWidth(startCol + 2, 10);
				makeExcel.setWidth(startCol + 3, 10);

				makeExcel.setValue(row2, startCol, "第" + i + "工作月", "C");

				makeExcel.setValue(row3, startCol, "房貸\n責任額", "C");
				makeExcel.setValue(row3, startCol + 1, "房貸\n撥款金額", "C");
				makeExcel.setValue(row3, startCol + 2, "房貸\n件數", "C");
				makeExcel.setValue(row3, startCol + 3, "達成率", "C");

				startCol += 4;

			} else {
				// 當 第i工作月 超過 上季末工作月 再開始建攔位
				if (i > lastWkMonth) {

					// 建立一次 累計欄位
					if (firstCreate) {
						// 設置當前攔寬度
						makeExcel.setWidth(startCol, 15);
						makeExcel.setWidth(startCol + 1, 15);
						makeExcel.setWidth(startCol + 2, 10);
						makeExcel.setWidth(startCol + 3, 10);

//						makeExcel.setMergedRegionValue(row2, row2, startCol, startCol + 1,
//								"第1~" + lastWkMonth + "工作月累計", "C");

						makeExcel.setValue(row2, startCol, "第1~" + lastWkMonth + "工作月", "C");

						makeExcel.setValue(row3, startCol, "房貸\n責任額", "C");
						makeExcel.setValue(row3, startCol + 1, "房貸\n撥款金額", "C");
						makeExcel.setValue(row3, startCol + 2, "房貸\n件數", "C");
						makeExcel.setValue(row3, startCol + 3, "達成率", "C");

						firstCreate = false;
						startCol += 4;
					}

					// 設置當前攔寬度
					makeExcel.setWidth(startCol, 15);
					makeExcel.setWidth(startCol + 1, 15);
					makeExcel.setWidth(startCol + 2, 10);
					makeExcel.setWidth(startCol + 3, 10);

					makeExcel.setValue(row2, startCol, "第" + i + "工作月", "C");

					makeExcel.setValue(row3, startCol, "房貸\n責任額", "C");
					makeExcel.setValue(row3, startCol + 1, "房貸\n撥款金額", "C");
					makeExcel.setValue(row3, startCol + 2, "房貸\n件數", "C");
					makeExcel.setValue(row3, startCol + 3, "達成率", "C");

					startCol += 4;
				}

			}

		}

		// 設置當前攔寬度
		makeExcel.setWidth(startCol, 15);
		makeExcel.setWidth(startCol + 1, 15);
		makeExcel.setWidth(startCol + 2, 10);
		makeExcel.setWidth(startCol + 3, 10);

		makeExcel.setValue(row2, startCol, "第1~" + wkMonth + "工作月", "C");

		makeExcel.setValue(row3, startCol, "房貸\n責任額", "C");
		makeExcel.setValue(row3, startCol + 1, "房貸\n撥款金額", "C");
		makeExcel.setValue(row3, startCol + 2, "房貸\n件數", "C");
		makeExcel.setValue(row3, startCol + 3, "達成率", "C");
	}

	/**
	 * 產部室資料
	 * 
	 * @param titaVofindDept
	 * @param wkSsnVo        工作年&月
	 * @param wkMonth        本工作月
	 * @param lastWkMonth    上季末工作月
	 * 
	 */
	private void setDept(TitaVo titaVo, Map<String, String> wkSsnVo, int wkMonth, int lastWkMonth)
			throws LogicException {
		this.info("===========exportExcelDept");

		try {

			findList = LP003ServiceImpl.findDept(titaVo, wkSsnVo);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LP003ServiceImpl.findDept error = " + errors.toString());
		}

		if (findList.size() > 0) {

			// 部室
			String deptcode = "";

			// 起始列
			sRow = 5;
			tRow = 4;

			// 起始欄
			scol = 4;

			// 陣列大小
			size = 0;
			// 陣列大小(暫存)
			int sizeT = 0;

			gamtColTotal1 = BigDecimal.ZERO;
			cntColTotal1 = BigDecimal.ZERO;
			amtColTotal1 = BigDecimal.ZERO;

			gamtColTotal2 = BigDecimal.ZERO;
			cntColTotal2 = BigDecimal.ZERO;
			amtColTotal2 = BigDecimal.ZERO;

			gamtColTotal3 = BigDecimal.ZERO;
			cntColTotal3 = BigDecimal.ZERO;
			amtColTotal3 = BigDecimal.ZERO;

			gamtColTotal4 = BigDecimal.ZERO;
			cntColTotal4 = BigDecimal.ZERO;
			amtColTotal4 = BigDecimal.ZERO;

			gamtColTotal5 = BigDecimal.ZERO;
			cntColTotal5 = BigDecimal.ZERO;
			amtColTotal5 = BigDecimal.ZERO;

			gamtColAllTotal = BigDecimal.ZERO;
			cntColAllTotal = BigDecimal.ZERO;
			amtColAllTotal = BigDecimal.ZERO;

			gamtColTotal = BigDecimal.ZERO;
			cntColTotal = BigDecimal.ZERO;
			amtColTotal = BigDecimal.ZERO;

			// 是否 產資料至 前工作月累計 欄位
			boolean firstCreate = true;

			for (Map<String, String> tLDVo : findList) {
//				this.info("list" + tLDVo);

				BigDecimal gamt = tLDVo.get("F4") == null || tLDVo.get("F4").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F4"));
				BigDecimal cnt = tLDVo.get("F5") == null || tLDVo.get("F5").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F5"));
				BigDecimal amt = tLDVo.get("F6") == null || tLDVo.get("F6").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F6"));

				if (!deptcode.equals(tLDVo.get("F7"))) {

					// 部室代號
					deptcode = tLDVo.get("F7");
					// 排行
					makeExcel.setValue(sRow, 1, tLDVo.get("F0"));
					// 部室
					makeExcel.setValue(sRow, 2, tLDVo.get("F1"));
					// 主管
					makeExcel.setValue(sRow, 3, tLDVo.get("F2"));

					scol = 4;

					sRow++;
					tRow++;

					gamtTotal = BigDecimal.ZERO;
					cntTotal = BigDecimal.ZERO;
					amtTotal = BigDecimal.ZERO;

					gamtRowTotal = BigDecimal.ZERO;
					cntRowTotal = BigDecimal.ZERO;
					amtRowTotal = BigDecimal.ZERO;

					firstCreate = true;
				}

				// 月份
				dmm = Integer.valueOf(tLDVo.get("F3").substring(4, 6));

				if (wkMonth <= 3) {

					makeExcel.setValue(tRow, scol, gamt, "#,##0", "R");
					makeExcel.setValue(tRow, scol + 1, amt, "#,##0", "R");
					makeExcel.setValue(tRow, scol + 2, cnt, "#,##0", "R");
					makeExcel.setValue(tRow, scol + 3, cpRate(gamt, amt), "R");

					// 累計+陸續工作月的 總計
					gamtRowTotal = gamtRowTotal.add(gamt);
					cntRowTotal = cntRowTotal.add(cnt);
					amtRowTotal = amtRowTotal.add(amt);

					scol += 4;

				} else {
					// 只會加總到前一季工作月
					if (dmm <= lastWkMonth) {

						// 列 前工作月總計
						gamtTotal = gamtTotal.add(gamt);
						cntTotal = cntTotal.add(cnt);
						amtTotal = amtTotal.add(amt);

						// 累計+陸續工作月的 總計
						gamtRowTotal = gamtRowTotal.add(gamt);
						cntRowTotal = cntRowTotal.add(cnt);
						amtRowTotal = amtRowTotal.add(amt);

						// 累計的總計
						gamtColTotal = gamtColTotal.add(gamt);
						cntColTotal = cntColTotal.add(cnt);
						amtColTotal = amtColTotal.add(amt);

					} else if (dmm > lastWkMonth) {
						// 當 第i工作月 超過 上季末工作月 再開始建攔位

						// 第一次 建立累計欄位
						if (firstCreate) {

							makeExcel.setValue(tRow, scol, gamtTotal, "#,##0", "R");
							makeExcel.setValue(tRow, scol + 1, amtTotal, "#,##0", "R");
							makeExcel.setValue(tRow, scol + 2, cntTotal, "#,##0", "R");
							makeExcel.setValue(tRow, scol + 3, cpRate(gamtTotal, amtTotal), "R");

							firstCreate = false;
							scol += 4;
						}
						// 累計+陸續工作月的 總計
						gamtRowTotal = gamtRowTotal.add(gamt);
						cntRowTotal = cntRowTotal.add(cnt);
						amtRowTotal = amtRowTotal.add(amt);

						makeExcel.setValue(tRow, scol, gamt, "#,##0", "R");
						makeExcel.setValue(tRow, scol + 1, amt, "#,##0", "R");
						makeExcel.setValue(tRow, scol + 2, cnt, "#,##0", "R");
						makeExcel.setValue(tRow, scol + 3, cpRate(gamt, amt), "R");

						scol += 4;

					}

				}

				// 這沒有防，一直覆蓋同一位置 暫時

				makeExcel.setValue(tRow, scol, gamtRowTotal, "#,##0", "R");
				makeExcel.setValue(tRow, scol + 1, amtRowTotal, "#,##0", "R");
				makeExcel.setValue(tRow, scol + 2, cntRowTotal, "#,##0", "R");
				makeExcel.setValue(tRow, scol + 3, cpRate(gamtRowTotal, amtRowTotal), "R");

				if (dmm > lastWkMonth) {

					sizeT = toWkMonth(dmm, gamt, cnt, amt);

				}

				gamtColAllTotal = gamtColAllTotal.add(gamt);
				cntColAllTotal = cntColAllTotal.add(cnt);
				amtColAllTotal = amtColAllTotal.add(amt);

			}
			tRow++;

			// 全總計
			gamtArr[5] = gamtColAllTotal;
			cntArr[5] = cntColAllTotal;
			amtArr[5] = amtColAllTotal;

			scol = 4;

			// 最後一行合計
			int cj = 0;
			// 超過第3工作月 會插入累計
			if (wkMonth > 3) {

				makeExcel.setValue(tRow, scol, gamtColTotal, "#,##0", "R");
				makeExcel.setValue(tRow, scol + 1, amtColTotal, "#,##0", "R");
				makeExcel.setValue(tRow, scol + 2, cntColTotal, "#,##0", "R");
				makeExcel.setValue(tRow, scol + 3, cpRate(gamtColTotal, amtColTotal), "R");

				scol += 4;
				cj = 1;

			}

			for (int j = cj; j < sizeT - 1; j++) {

				makeExcel.setValue(tRow, scol, gamtArr[j], "#,##0", "R");
				makeExcel.setValue(tRow, scol + 1, amtArr[j], "#,##0", "R");
				makeExcel.setValue(tRow, scol + 2, cntArr[j], "#,##0", "R");
				makeExcel.setValue(tRow, scol + 3, cpRate(gamtArr[j], amtArr[j]), "R");

				scol += 4;

			}

			makeExcel.setMergedRegionValue(tRow, tRow, 1, 3, "合計", "C");

			makeExcel.setValue(tRow, scol, gamtColAllTotal, "#,##0", "R");
			makeExcel.setValue(tRow, scol + 1, amtColAllTotal, "#,##0", "R");
			makeExcel.setValue(tRow, scol + 2, cntColAllTotal, "#,##0", "R");
			makeExcel.setValue(tRow, scol + 3, cpRate(gamtColAllTotal, amtColAllTotal), "R");

		} else {
			makeExcel.setValue(4, 1, "本日無資料");
		}
	}

	/**
	 * 產部室資料
	 * 
	 * @param titaVo
	 * @param wkSsnVo     工作年&月
	 * @param wkMonth     本工作月
	 * @param lastWkMonth 上季末工作月
	 * 
	 */
	private void setEmp(TitaVo titaVo, Map<String, String> wkSsnVo, int wkMonth, int lastWkMonth)
			throws LogicException {
		this.info("===========exportExcelDept");

		try {

			findList = LP003ServiceImpl.findEmp(titaVo, wkSsnVo, 0);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LP003ServiceImpl.findEmp error = " + errors.toString());
		}

		if (findList.size() > 0) {

			// 部室
			String deptcode = "";

			// 起始列
			sRow = 5;
			tRow = 4;

			// 起始欄
			scol = 6;

			// 陣列大小
			size = 0;
			// 陣列大小(暫存)
			int sizeT = 0;

			gamtColTotal1 = BigDecimal.ZERO;
			cntColTotal1 = BigDecimal.ZERO;
			amtColTotal1 = BigDecimal.ZERO;

			gamtColTotal2 = BigDecimal.ZERO;
			cntColTotal2 = BigDecimal.ZERO;
			amtColTotal2 = BigDecimal.ZERO;

			gamtColTotal3 = BigDecimal.ZERO;
			cntColTotal3 = BigDecimal.ZERO;
			amtColTotal3 = BigDecimal.ZERO;

			gamtColTotal4 = BigDecimal.ZERO;
			cntColTotal4 = BigDecimal.ZERO;
			amtColTotal4 = BigDecimal.ZERO;

			gamtColTotal5 = BigDecimal.ZERO;
			cntColTotal5 = BigDecimal.ZERO;
			amtColTotal5 = BigDecimal.ZERO;

			gamtColAllTotal = BigDecimal.ZERO;
			cntColAllTotal = BigDecimal.ZERO;
			amtColAllTotal = BigDecimal.ZERO;

			gamtColTotal = BigDecimal.ZERO;
			cntColTotal = BigDecimal.ZERO;
			amtColTotal = BigDecimal.ZERO;

			// 是否 產資料至 前工作月累計 欄位
			boolean firstCreate = true;

			for (Map<String, String> tLDVo : findList) {
//				this.info("list" + tLDVo);

				BigDecimal gamt = tLDVo.get("F5") == null || tLDVo.get("F5").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F5"));
				BigDecimal cnt = tLDVo.get("F6") == null || tLDVo.get("F6").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F6"));
				BigDecimal amt = tLDVo.get("F7") == null || tLDVo.get("F7").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F7"));

				if (!deptcode.equals(tLDVo.get("F8"))) {

					// 部室代號
					deptcode = tLDVo.get("F8");
					// 排行
					makeExcel.setValue(sRow, 1, tLDVo.get("F0"));
					// 部室
					makeExcel.setValue(sRow, 2, tLDVo.get("F1"));
					// 區部
					makeExcel.setValue(sRow, 3, tLDVo.get("F2"));
					// 主管
					makeExcel.setValue(sRow, 4, tLDVo.get("F3"));
					// 起月
					makeExcel.setValue(sRow, 5, Integer.parseInt(tLDVo.get("F4").substring(4, 6)));

					scol = 6;

					sRow++;
					tRow++;

					gamtTotal = BigDecimal.ZERO;
					cntTotal = BigDecimal.ZERO;
					amtTotal = BigDecimal.ZERO;

					gamtRowTotal = BigDecimal.ZERO;
					cntRowTotal = BigDecimal.ZERO;
					amtRowTotal = BigDecimal.ZERO;

					firstCreate = true;
				}

				// 月份
				dmm = Integer.valueOf(tLDVo.get("F4").substring(4, 6));

				if (wkMonth <= 3) {

					makeExcel.setValue(tRow, scol, gamt, "#,##0", "R");
					makeExcel.setValue(tRow, scol + 1, amt, "#,##0", "R");
					makeExcel.setValue(tRow, scol + 2, cnt, "#,##0", "R");
					makeExcel.setValue(tRow, scol + 3, cpRate(gamt, amt), "R");
					// 累計+陸續工作月的 總計
					gamtRowTotal = gamtRowTotal.add(gamt);
					cntRowTotal = cntRowTotal.add(cnt);
					amtRowTotal = amtRowTotal.add(amt);

					scol += 4;

				} else {
					// 只會加總到前一季工作月
					if (dmm <= lastWkMonth) {

						// 列 前工作月總計
						gamtTotal = gamtTotal.add(gamt);
						cntTotal = cntTotal.add(cnt);
						amtTotal = amtTotal.add(amt);

						// 累計+陸續工作月的 總計
						gamtRowTotal = gamtRowTotal.add(gamt);
						cntRowTotal = cntRowTotal.add(cnt);
						amtRowTotal = amtRowTotal.add(amt);

						// 累計的總計
						gamtColTotal = gamtColTotal.add(gamt);
						cntColTotal = cntColTotal.add(cnt);
						amtColTotal = amtColTotal.add(amt);

					} else if (dmm > lastWkMonth) {
						// 當 第i工作月 超過 上季末工作月 再開始建攔位

						// 第一次 建立累計欄位
						if (firstCreate) {

							makeExcel.setValue(tRow, scol, gamtTotal, "#,##0", "R");
							makeExcel.setValue(tRow, scol + 1, amtTotal, "#,##0", "R");
							makeExcel.setValue(tRow, scol + 2, cntTotal, "#,##0", "R");
							makeExcel.setValue(tRow, scol + 3, cpRate(gamtTotal, amtTotal), "R");

							firstCreate = false;
							scol += 4;
						}

						// 累計+陸續工作月的 總計
						gamtRowTotal = gamtRowTotal.add(gamt);
						cntRowTotal = cntRowTotal.add(cnt);
						amtRowTotal = amtRowTotal.add(amt);

						makeExcel.setValue(tRow, scol, gamt, "#,##0", "R");
						makeExcel.setValue(tRow, scol + 1, amt, "#,##0", "R");
						makeExcel.setValue(tRow, scol + 2, cnt, "#,##0", "R");
						makeExcel.setValue(tRow, scol + 3, cpRate(gamt, amt), "R");

						scol += 4;

					}

				}

				// 這沒有防，一直覆蓋同一位置 暫時

				makeExcel.setValue(tRow, scol, gamtRowTotal, "#,##0", "R");
				makeExcel.setValue(tRow, scol + 1, amtRowTotal, "#,##0", "R");
				makeExcel.setValue(tRow, scol + 2, cntRowTotal, "#,##0", "R");
				makeExcel.setValue(tRow, scol + 3, cpRate(gamtRowTotal, amtRowTotal), "R");

				if (dmm > lastWkMonth) {

					sizeT = toWkMonth(dmm, gamt, cnt, amt);

				}

				gamtColAllTotal = gamtColAllTotal.add(gamt);
				cntColAllTotal = cntColAllTotal.add(cnt);
				amtColAllTotal = amtColAllTotal.add(amt);

			}

			// 再找房貸部專
			try {

				findList = LP003ServiceImpl.findEmp(titaVo, wkSsnVo, 1);

			} catch (Exception e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.info("LP003ServiceImpl.findEmp error = " + errors.toString());
			}

			for (Map<String, String> tLDVo : findList) {
//				this.info("list" + tLDVo);

				BigDecimal gamt = tLDVo.get("F5") == null || tLDVo.get("F5").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F5"));
				BigDecimal cnt = tLDVo.get("F6") == null || tLDVo.get("F6").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F6"));
				BigDecimal amt = tLDVo.get("F7") == null || tLDVo.get("F7").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F7"));

				if (!deptcode.equals(tLDVo.get("F8"))) {

					// 部室代號
					deptcode = tLDVo.get("F8");
					// 排行
					makeExcel.setValue(sRow, 1, "");
					// 部室
					makeExcel.setValue(sRow, 2, tLDVo.get("F1"));
					// 區部
					makeExcel.setValue(sRow, 3, tLDVo.get("F2"));
					// 主管
					makeExcel.setValue(sRow, 4, tLDVo.get("F3"));
					// 起月
					makeExcel.setValue(sRow, 5, "");

					scol = 6;

					sRow++;
					tRow++;

					gamtTotal = BigDecimal.ZERO;
					cntTotal = BigDecimal.ZERO;
					amtTotal = BigDecimal.ZERO;

					gamtRowTotal = BigDecimal.ZERO;
					cntRowTotal = BigDecimal.ZERO;
					amtRowTotal = BigDecimal.ZERO;

					firstCreate = true;
				}

				// 月份
				dmm = Integer.valueOf(tLDVo.get("F4").substring(4, 6));

				if (wkMonth <= 3) {

					makeExcel.setValue(tRow, scol, gamt, "#,##0", "R");
					makeExcel.setValue(tRow, scol + 1, amt, "#,##0", "R");
					makeExcel.setValue(tRow, scol + 2, cnt, "#,##0", "R");
					makeExcel.setValue(tRow, scol + 3, cpRate(gamt, amt), "R");
					// 累計+陸續工作月的 總計
					gamtRowTotal = gamtRowTotal.add(gamt);
					cntRowTotal = cntRowTotal.add(cnt);
					amtRowTotal = amtRowTotal.add(amt);

					scol += 4;

				} else {
					// 只會加總到前一季工作月
					if (dmm <= lastWkMonth) {

						// 列 前工作月總計
						gamtTotal = gamtTotal.add(gamt);
						cntTotal = cntTotal.add(cnt);
						amtTotal = amtTotal.add(amt);

						// 累計+陸續工作月的 總計
						gamtRowTotal = gamtRowTotal.add(gamt);
						cntRowTotal = cntRowTotal.add(cnt);
						amtRowTotal = amtRowTotal.add(amt);

						// 累計的總計
						gamtColTotal = gamtColTotal.add(gamt);
						cntColTotal = cntColTotal.add(cnt);
						amtColTotal = amtColTotal.add(amt);

					} else if (dmm > lastWkMonth) {
						// 當 第i工作月 超過 上季末工作月 再開始建攔位

						// 第一次 建立累計欄位
						if (firstCreate) {

							makeExcel.setValue(tRow, scol, gamtTotal, "#,##0", "R");
							makeExcel.setValue(tRow, scol + 1, amtTotal, "#,##0", "R");
							makeExcel.setValue(tRow, scol + 2, cntTotal, "#,##0", "R");
							makeExcel.setValue(tRow, scol + 3, cpRate(gamtTotal, amtTotal), "R");

							firstCreate = false;
							scol += 4;
						}

						// 累計+陸續工作月的 總計
						gamtRowTotal = gamtRowTotal.add(gamt);
						cntRowTotal = cntRowTotal.add(cnt);
						amtRowTotal = amtRowTotal.add(amt);

						makeExcel.setValue(tRow, scol, gamt, "#,##0", "R");
						makeExcel.setValue(tRow, scol + 1, amt, "#,##0", "R");
						makeExcel.setValue(tRow, scol + 2, cnt, "#,##0", "R");
						makeExcel.setValue(tRow, scol + 3, cpRate(gamt, amt), "R");

						scol += 4;

					}

				}

				// 這沒有防，一直覆蓋同一位置 暫時

				makeExcel.setValue(tRow, scol, gamtRowTotal, "#,##0", "R");
				makeExcel.setValue(tRow, scol + 1, amtRowTotal, "#,##0", "R");
				makeExcel.setValue(tRow, scol + 2, cntRowTotal, "#,##0", "R");
				makeExcel.setValue(tRow, scol + 3, cpRate(gamtRowTotal, amtRowTotal), "R");

				if (dmm > lastWkMonth) {

					sizeT = toWkMonth(dmm, gamt, cnt, amt);

				}

				gamtColAllTotal = gamtColAllTotal.add(gamt);
				cntColAllTotal = cntColAllTotal.add(cnt);
				amtColAllTotal = amtColAllTotal.add(amt);

			}

			tRow++;

			// 全總計
			gamtArr[5] = gamtColAllTotal;
			cntArr[5] = cntColAllTotal;
			amtArr[5] = amtColAllTotal;

			scol = 6;

			// 最後一行合計
			int cj = 0;
			// 超過第3工作月 會插入累計
			if (wkMonth > 3) {

				makeExcel.setValue(tRow, scol, gamtColTotal, "#,##0", "R");
				makeExcel.setValue(tRow, scol + 1, amtColTotal, "#,##0", "R");
				makeExcel.setValue(tRow, scol + 2, cntColTotal, "#,##0", "R");
				makeExcel.setValue(tRow, scol + 3, cpRate(gamtColTotal, amtColTotal), "R");

				scol += 4;
				cj = 1;

			}

			for (int j = cj; j < sizeT - 1; j++) {

				makeExcel.setValue(tRow, scol, gamtArr[j], "#,##0", "R");
				makeExcel.setValue(tRow, scol + 1, amtArr[j], "#,##0", "R");
				makeExcel.setValue(tRow, scol + 2, cntArr[j], "#,##0", "R");
				makeExcel.setValue(tRow, scol + 3, cpRate(gamtArr[j], amtArr[j]), "R");

				scol += 4;

			}

			makeExcel.setMergedRegionValue(tRow, tRow, 1, 5, "合計", "C");

			makeExcel.setValue(tRow, scol, gamtColAllTotal, "#,##0", "R");
			makeExcel.setValue(tRow, scol + 1, amtColAllTotal, "#,##0", "R");
			makeExcel.setValue(tRow, scol + 2, cntColAllTotal, "#,##0", "R");
			makeExcel.setValue(tRow, scol + 3, cpRate(gamtColAllTotal, amtColAllTotal), "R");

		} else {
			makeExcel.setValue(4, 1, "本日無資料");
		}
	}

	/**
	 * 當工作月房貸部專業績統計
	 * 
	 * @param titaVo
	 * @param wkSsnVo 工作年&月
	 * @param wkMonth 本工作月
	 * 
	 */
	private void setDeptThisWKM(TitaVo titaVo, Map<String, String> wkSsnVo, int wkMonth) throws LogicException {
		this.info("===========setDeptThisWKM");
		// 民國年
		String ROCYear = String.valueOf(Integer.valueOf(wkSsnVo.get("F0")) - 1911);

		String dateM = String.valueOf(Integer.valueOf(titaVo.get("ENTDY")));

		try {

			findList = LP003ServiceImpl.findDeptThisWKM(titaVo, wkSsnVo);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LP003ServiceImpl.findEmp error = " + errors.toString());
		}

		makeExcel.setSheet("Sheet2");

		// 標題
		makeExcel.setValue(1, 1, ROCYear + "." + wkMonth + "工作月房貸部專業績統計報表", "C");
		makeExcel.setValue(2, 1,
				"結算日期：" + dateM.substring(0, 3) + "." + dateM.substring(3, 5) + "." + dateM.substring(5, 7), "R");

		if (findList.size() > 0) {
			// 起始列
			sRow = 4;

//			makeExcel.setShiftRow(sRow, findList.size() - 1);

			for (Map<String, String> tLDVo : findList) {

				BigDecimal gamt = tLDVo.get("F4") == null || tLDVo.get("F4").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F4"));

				BigDecimal amt = tLDVo.get("F5") == null || tLDVo.get("F5").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F5"));

				BigDecimal cnt = tLDVo.get("F6") == null || tLDVo.get("F6").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F6"));

				BigDecimal percent = tLDVo.get("F7") == null || tLDVo.get("F7").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F7"));

				// 排行
				makeExcel.setValue(sRow, 1, tLDVo.get("F0"), "C");
				// 駐在地
				makeExcel.setValue(sRow, 2, tLDVo.get("F1"), "C");
				// 姓名
				makeExcel.setValue(sRow, 3, tLDVo.get("F2"), "C");
				// 電腦編號
				makeExcel.setValue(sRow, 4, tLDVo.get("F3"), "C");
				// 房貸責任額
				makeExcel.setValue(sRow, 5, gamt, "#,##0");
				// 房貸撥款金額
				makeExcel.setValue(sRow, 6, amt, "#,##0");
				// 房貸撥款金額
				makeExcel.setValue(sRow, 7, cnt, "0.0");
				// 達成率
				makeExcel.setValue(sRow, 8, percent, "0.00");
				// 服務部室
				makeExcel.setValue(sRow, 9, tLDVo.get("F8"), "C");

//			
				sRow++;

			}

		} else {
			makeExcel.setValue(4, 1, "本日無資料");
		}

	}

	/**
	 * 當工作月房貸專員業績統計
	 * 
	 * @param titaVo
	 * @param wkSsnVo 工作年&月
	 * @param wkMonth 本工作月
	 * 
	 */
	private void setEmpThisWKM(TitaVo titaVo, Map<String, String> wkSsnVo, int wkMonth) throws LogicException {
		this.info("===========setEmpThisWKM");
		// 民國年
		String ROCYear = String.valueOf(Integer.valueOf(wkSsnVo.get("F0")) - 1911);

		String dateM = String.valueOf(Integer.valueOf(titaVo.get("ENTDY")));

		List<Map<String, String>> findListBS0 = null;
		List<Map<String, String>> findListBS1 = null;
		try {

			findListBS0 = LP003ServiceImpl.findEmpThisWKM(titaVo, wkSsnVo, 0);

			findListBS1 = LP003ServiceImpl.findEmpThisWKM(titaVo, wkSsnVo, 1);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LP003ServiceImpl.findEmp error = " + errors.toString());
		}

		makeExcel.setSheet("Sheet1");

		// 標題
		makeExcel.setValue(1, 1, ROCYear + "." + wkMonth + "工作月房貸專員業績統計報表", "C");
		makeExcel.setValue(2, 1,
				"結算日期：" + dateM.substring(0, 3) + "." + dateM.substring(3, 5) + "." + dateM.substring(5, 7), "R");

		if (findListBS0.size() > 0 && findListBS1.size() > 0) {

			// 起始列
			sRow = 4;
//			int shiftRow = findListBS0.size() + findListBS1.size() - 1;
//			makeExcel.setShiftRow(sRow, shiftRow);

//			this.info("從" + sRow + "~" + shiftRow);
			//房貸專員
			for (Map<String, String> tLDVo : findListBS0) {

				BigDecimal gamt = tLDVo.get("F3") == null || tLDVo.get("F3").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F3"));
				BigDecimal amt = tLDVo.get("F4") == null || tLDVo.get("F4").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F4"));
				BigDecimal cnt = tLDVo.get("F5") == null || tLDVo.get("F5").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F5"));
				BigDecimal percent = tLDVo.get("F6") == null || tLDVo.get("F6").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F6"));

				// 排行
				makeExcel.setValue(sRow, 1, tLDVo.get("F0"), "C");
				// 姓名
				makeExcel.setValue(sRow, 2, tLDVo.get("F1"), "C");
				// 電腦編號
				makeExcel.setValue(sRow, 3, tLDVo.get("F2"), "C");
				// 房貸責任額
				makeExcel.setValue(sRow, 4, gamt, "#,##0");
				// 房貸撥款金額
				makeExcel.setValue(sRow, 5, amt, "#,##0");
				// 房貸撥款金額
				makeExcel.setValue(sRow, 6, cnt, "0.0");
				// 達成率
				makeExcel.setValue(sRow, 7, percent, "0.00");
				// 服務部室
				makeExcel.setValue(sRow, 8, tLDVo.get("F7"), "C");
				// 服務區部
				makeExcel.setValue(sRow, 9, tLDVo.get("F8"), "C");

				sRow++;

			}

			//房貸部專
			for (Map<String, String> tLDVo : findListBS1) {

				BigDecimal gamt = tLDVo.get("F3") == null || tLDVo.get("F3").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F3"));
				BigDecimal amt = tLDVo.get("F4") == null || tLDVo.get("F4").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F4"));
				BigDecimal cnt = tLDVo.get("F5") == null || tLDVo.get("F5").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F5"));
				BigDecimal percent = tLDVo.get("F6") == null || tLDVo.get("F6").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F6"));

				// 排行
				makeExcel.setValue(sRow, 1, "", "C");
				// 姓名
				makeExcel.setValue(sRow, 2, tLDVo.get("F1"), "C");
				// 電腦編號
				makeExcel.setValue(sRow, 3, tLDVo.get("F2"), "C");
				// 房貸責任額
				makeExcel.setValue(sRow, 4, gamt, "#,##0");
				// 房貸撥款金額
				makeExcel.setValue(sRow, 5, amt, "#,##0");
				// 房貸撥款金額
				makeExcel.setValue(sRow, 6, cnt, "0.0");
				// 達成率
				makeExcel.setValue(sRow, 7, percent, "0.00");
				// 服務部室
				makeExcel.setValue(sRow, 8, tLDVo.get("F7"), "C");
				// 服務區部
				makeExcel.setValue(sRow, 9, tLDVo.get("F8"), "C");
				sRow++;

			}

		} else {
			makeExcel.setValue(4, 1, "本日無資料");
		}

	}

	/**
	 * 各工作月合計
	 * 
	 * @param dmm 工作月
	 * @param cnt 件數
	 * @param amt 餘額
	 * 
	 */
	private int toWkMonth(int dmm, BigDecimal gamt, BigDecimal cnt, BigDecimal amt) {
		this.info(dmm+"工作月～責任:"+gamt+"~撥款:"+amt+"~件數"+cnt);
		// 各工作月 加總
		switch (dmm) {
		case 1:
			gamtColTotal1 = gamtColTotal1.add(gamt);
			cntColTotal1 = cntColTotal1.add(cnt);
			amtColTotal1 = amtColTotal1.add(amt);

			gamtArr[0] = gamtColTotal1;
			cntArr[0] = cntColTotal1;
			amtArr[0] = amtColTotal1;
			this.info("第1組:責任額:"+gamtArr[0]+"~撥款"+amtArr[0]);
			
			size = 2;
			break;
		case 2:
		case 4:
		case 7:
		case 10:
			gamtColTotal2 = gamtColTotal2.add(gamt);
			cntColTotal2 = cntColTotal2.add(cnt);
			amtColTotal2 = amtColTotal2.add(amt);

			gamtArr[1] = gamtColTotal2;
			cntArr[1] = cntColTotal2;
			amtArr[1] = amtColTotal2;
			this.info("第2組:責任額:"+gamtArr[1]+"~撥款"+amtArr[1]);
			size = 3;
			break;
		case 3:
		case 5:
		case 8:
		case 11:
			gamtColTotal3 = gamtColTotal3.add(gamt);
			cntColTotal3 = cntColTotal3.add(cnt);
			amtColTotal3 = amtColTotal3.add(amt);

			gamtArr[2] = gamtColTotal3;
			cntArr[2] = cntColTotal3;
			amtArr[2] = cntColTotal3;
			this.info("第3組:責任額:"+gamtArr[2]+"~撥款"+amtArr[2]);
			
			size = 4;
			break;

		case 6:
		case 9:
		case 12:
			gamtColTotal4 = gamtColTotal4.add(gamt);
			cntColTotal4 = cntColTotal4.add(cnt);
			amtColTotal4 = amtColTotal4.add(amt);

			gamtArr[3] = gamtColTotal4;
			cntArr[3] = cntColTotal4;
			amtArr[3] = amtColTotal4;
			this.info("第4組:責任額:"+gamtArr[3]+"~撥款"+amtArr[3]);
			size = 5;
			break;
		case 13:
			gamtColTotal5 = gamtColTotal5.add(gamt);
			cntColTotal5 = cntColTotal5.add(cnt);
			amtColTotal5 = amtColTotal5.add(amt);

			gamtArr[4] = gamtColTotal5;
			cntArr[4] = cntColTotal5;
			amtArr[4] = amtColTotal5;
			this.info("第5組:責任額:"+gamtArr[4]+"~撥款"+amtArr[4]);
			size = 6;
			break;
		default:
			break;
		}

		
	
	

//		this.info(""++""++""++""++""++"");
//		this.info("累計工作月"++""++""++""++""++"");
//		this.info("累計工作月"++""++""++""++""++"");
//		this.info("累計工作月"++""++""++""++""++"");
		return size;
	}

	private double cpRate(BigDecimal igal, BigDecimal iamt) {
//		BigDecimal gal = new BigDecimal(igal);
//		BigDecimal amt = new BigDecimal(iamt);
		double rate = 0.0;
//		this.info("LP003Report gal = " + gal.toString());
//		this.info("LP003Report amt = " + amt.toString());
		if (igal.equals(BigDecimal.ZERO) || iamt.equals(BigDecimal.ZERO)) {
			rate = 0.0;
		} else {

			rate = iamt.divide(igal, 3, 3).doubleValue();
		}
		return rate;
	}
}
