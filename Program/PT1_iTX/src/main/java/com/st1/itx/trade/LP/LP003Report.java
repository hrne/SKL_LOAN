package com.st1.itx.trade.LP;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

	@Autowired
	LP003ServiceImpl lP003ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	// 標題中的月份(工作月)
	String monthHead = "";

	public void exec(TitaVo titaVo) throws LogicException {
		this.info("LP003Report exec");
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LP003", "部專暨房專業績累計表", "LP003部專暨房專業績累計表", "LP003_底稿_部專暨房專業績累計表.xlsx", "部專");
		List<Map<String, String>> wkSsnList = new ArrayList<>();
		try {
			wkSsnList = lP003ServiceImpl.wkSsn(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LP003ServiceImpl.wkSsn error = " + errors.toString());
		}
		exportExcel(titaVo, wkSsnList.get(0));
		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

	/**
	 * 執行Excel產表
	 * 
	 * @param titaVo  titaVo
	 * @param wkSsnVo 工作年月
	 */
	private void exportExcel(TitaVo titaVo, Map<String, String> wkSsnVo) throws LogicException {
		// 民國年
		String rocYear;
		// 當前工作月
		int wkMonth;
		
		// 找上個工作月
		if (Integer.parseInt(wkSsnVo.get("F1")) == 1) {
			rocYear = String.valueOf(Integer.parseInt(wkSsnVo.get("F0")) - 1912);
			wkMonth = 13;
			monthHead = "13";
		} else {
			rocYear = String.valueOf(Integer.valueOf(wkSsnVo.get("F0")) - 1911);
			wkMonth = Integer.parseInt(wkSsnVo.get("F1"));
			monthHead = String.valueOf(Integer.parseInt(wkSsnVo.get("F1")));
		}
		monthHead = String.valueOf(wkMonth);
		// 起始欄位
		int col = 0;
		// 上季末 工作月
		int lastWkMonth = 0;
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
		col = 4;
		makeExcel.setSheet("部專");
		makeExcel.setValue(1, 1, rocYear + "年第1~" + monthHead + "工作月房貸部專業績累計明細表");
		// 建攔位
		setColTitle(col, wkMonth, lastWkMonth);
		// 產出部專
		setDept(titaVo, wkSsnVo, wkMonth, lastWkMonth);
		col = 6;
		makeExcel.setSheet("專員");
		makeExcel.setValue(1, 1, rocYear + "年第1~" + monthHead + "工作月房貸專員業績累計明細表");
		// 建攔位
		setColTitle(col, wkMonth, lastWkMonth);
		// 產出專員
		setEmp(titaVo, wkSsnVo, wkMonth, lastWkMonth);
		// 產出專員明細(sheet1)
		setEmpThisWKM(titaVo, wkSsnVo, wkMonth);
		// 產出部專明細(sheet2)
		setDeptThisWKM(titaVo, wkSsnVo, wkMonth);
	}

	/**
	 * 建立標題
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
						makeExcel.setValue(row2, startCol, "第1~" + lastWkMonth + "工作月累計", "C");
						makeExcel.setValue(row3, startCol, "房貸\n責任額", "C");
						makeExcel.setValue(row3, startCol + 1, "房貸\n撥款金額", "C");
						makeExcel.setValue(row3, startCol + 2, "房貸\n件數", "C");
						makeExcel.setValue(row3, startCol + 3, "達成率", "C");
						firstCreate = false;
						startCol += 4;
					}
					makeExcel.setValue(row2, startCol, "第" + i + "工作月", "C");
					makeExcel.setValue(row3, startCol, "房貸\n責任額", "C");
					makeExcel.setValue(row3, startCol + 1, "房貸\n撥款金額", "C");
					makeExcel.setValue(row3, startCol + 2, "房貸\n件數", "C");
					makeExcel.setValue(row3, startCol + 3, "達成率", "C");
					startCol += 4;
				}
			}
		}
		makeExcel.setValue(row2, startCol, "第1~" + wkMonth + "工作月累計", "C");
		makeExcel.setValue(row3, startCol, "房貸\n責任額", "C");
		makeExcel.setValue(row3, startCol + 1, "房貸\n撥款金額", "C");
		makeExcel.setValue(row3, startCol + 2, "房貸\n件數", "C");
		makeExcel.setValue(row3, startCol + 3, "達成率", "C");
	}

	/**
	 * 全年 部專
	 * 
	 * @param titaVo      titaVo
	 * @param wkSsnVo     工作年月
	 * @param wkMonth     本工作月
	 * @param lastWkMonth 上季末工作月
	 */
	private void setDept(TitaVo titaVo, Map<String, String> wkSsnVo, int wkMonth, int lastWkMonth) throws LogicException {
		this.info("setDept start ...");
		List<Map<String, String>> queryResultList = null;
		try {
			// 取得部專本年度業績資料
			queryResultList = lP003ServiceImpl.findDept(titaVo, wkSsnVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LP003ServiceImpl.findDept error = " + errors.toString());
		}
		if (queryResultList != null && queryResultList.size() > 0) {
			// 決定每個排名的RowCursors
			Map<String, Integer> rowCursorsMap = new HashMap<>();
			int rowCursor = 4; // 明細起始列 = 4
			int rowCursorOfTotal = 5; // 合計列
			for (Map<String, String> m : queryResultList) {
				String rank = m.get("F0");
				if (!rowCursorsMap.containsKey(rank)) {
					rowCursorsMap.put(rank, rowCursor);
					rowCursor++;
				}
			}
			// 明細總列數 = rowCursor - 表頭列數 - 最後一筆多加1次
			int detailRowSize = rowCursor - 3 - 1;
			// 把合計往下搬
			if (detailRowSize > 1) {
				// 底稿的合計列在第6列
				makeExcel.setShiftRow(rowCursorOfTotal, detailRowSize - 1);
				rowCursorOfTotal = rowCursorOfTotal + detailRowSize - 1;
			}
			Map<String, BigDecimal[]> lastSeasonSummary = new HashMap<>();
			Map<String, BigDecimal[]> yearSummary = new HashMap<>();
			Map<Integer, BigDecimal[]> total = new HashMap<>();
			for (Map<String, String> m : queryResultList) {
				String rank = m.get("F0"); // 排行
				String dept = m.get("F1"); // 部室
				String emp = m.get("F2"); // 房貸部專-姓名
				int workMonth = Integer.parseInt(m.get("F3")); // 工作月
				workMonth = workMonth % 100;// 去掉年度
				BigDecimal goalAmt = getBigDecimal(m.get("F4")); // 責任額
				BigDecimal perfCnt = getBigDecimal(m.get("F5")); // 撥款件數
				BigDecimal perfAmt = getBigDecimal(m.get("F6")); // 撥款金額
				if (lastWkMonth > 0 && workMonth <= lastWkMonth) {
					// 第1~lastWkMonth工作月累計
					BigDecimal[] summary = new BigDecimal[3];
					if (lastSeasonSummary.containsKey(rank)) {
						summary = lastSeasonSummary.get(rank);
					} else {
						summary[0] = BigDecimal.ZERO;
						summary[1] = BigDecimal.ZERO;
						summary[2] = BigDecimal.ZERO;
					}
					summary[0] = summary[0].add(goalAmt);
					summary[1] = summary[1].add(perfCnt);
					summary[2] = summary[2].add(perfAmt);
					lastSeasonSummary.put(rank, summary);
					// 合計
					BigDecimal[] tmpToal = new BigDecimal[3];
					if (total.containsKey(lastWkMonth)) {
						tmpToal = total.get(lastWkMonth);
					} else {
						tmpToal[0] = BigDecimal.ZERO;
						tmpToal[1] = BigDecimal.ZERO;
						tmpToal[2] = BigDecimal.ZERO;
					}
					tmpToal[0] = tmpToal[0].add(goalAmt);
					tmpToal[1] = tmpToal[1].add(perfCnt);
					tmpToal[2] = tmpToal[2].add(perfAmt);
					total.put(lastWkMonth, tmpToal);
				} else {
					rowCursor = rowCursorsMap.get(rank);
					int columnCursor = 1 + ((workMonth - lastWkMonth - 1) * 4) + (lastWkMonth == 0 ? 3 : 7);
					// 排行
					makeExcel.setValue(rowCursor, 1, getBigDecimal(rank));
					// 部室
					makeExcel.setValue(rowCursor, 2, dept);
					// 主管
					makeExcel.setValue(rowCursor, 3, emp);
					// 責任額
					makeExcel.setValue(rowCursor, columnCursor, goalAmt, "#,##0");
					// 撥款金額
					makeExcel.setValue(rowCursor, columnCursor + 1, perfAmt, "#,##0");
					// 撥款件數
					makeExcel.setValue(rowCursor, columnCursor + 2, perfCnt, "#,##0.0");
					// 達成率
					makeExcel.setValue(rowCursor, columnCursor + 3, computeDivide(perfAmt, goalAmt, 4), "#,##0%");
					// 合計
					BigDecimal[] tmpToal = new BigDecimal[3];
					if (total.containsKey(workMonth)) {
						tmpToal = total.get(workMonth);
					} else {
						tmpToal[0] = BigDecimal.ZERO;
						tmpToal[1] = BigDecimal.ZERO;
						tmpToal[2] = BigDecimal.ZERO;
					}
					tmpToal[0] = tmpToal[0].add(goalAmt);
					tmpToal[1] = tmpToal[1].add(perfCnt);
					tmpToal[2] = tmpToal[2].add(perfAmt);
					total.put(workMonth, tmpToal);
				}
				// 全年累計
				BigDecimal[] summary = new BigDecimal[3];
				if (yearSummary.containsKey(rank)) {
					summary = yearSummary.get(rank);
				} else {
					summary[0] = BigDecimal.ZERO;
					summary[1] = BigDecimal.ZERO;
					summary[2] = BigDecimal.ZERO;
				}
				summary[0] = summary[0].add(goalAmt);
				summary[1] = summary[1].add(perfCnt);
				summary[2] = summary[2].add(perfAmt);
				yearSummary.put(rank, summary);
			}
			// 列印第1~lastWkMonth工作月累計
			if (lastWkMonth > 0) {
				for (Entry<String, BigDecimal[]> entry : lastSeasonSummary.entrySet()) {
					String k = entry.getKey();
					BigDecimal[] v = entry.getValue();
					rowCursor = rowCursorsMap.get(k);
					// 責任額
					makeExcel.setValue(rowCursor, 4, v[0], "#,##0");
					// 撥款金額
					makeExcel.setValue(rowCursor, 5, v[2], "#,##0");
					// 撥款件數
					makeExcel.setValue(rowCursor, 6, v[1], "#,##0.0");
					// 達成率
					makeExcel.setValue(rowCursor, 7, computeDivide(v[2], v[0], 4), "#,##0%");
				}
			}
			// 列印全年累計
			int columnCursor = 1 + ((wkMonth + 1 - lastWkMonth - 1) * 4) + (lastWkMonth == 0 ? 3 : 7);
			// 全年累計
			BigDecimal[] summaryTotal = new BigDecimal[3];
			summaryTotal[0] = BigDecimal.ZERO;
			summaryTotal[1] = BigDecimal.ZERO;
			summaryTotal[2] = BigDecimal.ZERO;
			for (Entry<String, BigDecimal[]> entry : yearSummary.entrySet()) {
				String k = entry.getKey();
				BigDecimal[] v = entry.getValue();
				rowCursor = rowCursorsMap.get(k);
				// 責任額
				makeExcel.setValue(rowCursor, columnCursor, v[0], "#,##0");
				// 撥款金額
				makeExcel.setValue(rowCursor, columnCursor + 1, v[2], "#,##0");
				// 撥款件數
				makeExcel.setValue(rowCursor, columnCursor + 2, v[1], "#,##0.0");
				// 達成率
				makeExcel.setValue(rowCursor, columnCursor + 3, computeDivide(v[2], v[0], 4), "#,##0%");
				summaryTotal[0] = summaryTotal[0].add(v[0]);
				summaryTotal[1] = summaryTotal[1].add(v[1]);
				summaryTotal[2] = summaryTotal[2].add(v[2]);
			}
			// 列印合計
			for (Entry<Integer, BigDecimal[]> entry : total.entrySet()) {
				int tmpWorkMonth = entry.getKey();
				BigDecimal[] v = entry.getValue();
				columnCursor = 1 + ((tmpWorkMonth - lastWkMonth - 1) * 4) + (lastWkMonth == 0 ? 3 : 7);
				// 責任額
				makeExcel.setValue(rowCursorOfTotal, columnCursor, v[0], "#,##0");
				// 撥款金額
				makeExcel.setValue(rowCursorOfTotal, columnCursor + 1, v[2], "#,##0");
				// 撥款件數
				makeExcel.setValue(rowCursorOfTotal, columnCursor + 2, v[1], "#,##0.0");
				// 達成率
				makeExcel.setValue(rowCursorOfTotal, columnCursor + 3, computeDivide(v[2], v[0], 4), "#,##0%");
			}
			// 列印全年累計的合計
			columnCursor = 1 + ((wkMonth + 1 - lastWkMonth - 1) * 4) + (lastWkMonth == 0 ? 3 : 7);
			// 責任額
			makeExcel.setValue(rowCursorOfTotal, columnCursor, summaryTotal[0], "#,##0");
			// 撥款金額
			makeExcel.setValue(rowCursorOfTotal, columnCursor + 1, summaryTotal[2], "#,##0");
			// 撥款件數
			makeExcel.setValue(rowCursorOfTotal, columnCursor + 2, summaryTotal[1], "#,##0.0");
			// 達成率
			makeExcel.setValue(rowCursorOfTotal, columnCursor + 3, computeDivide(summaryTotal[2], summaryTotal[0], 4), "#,##0%");
		} else {
			makeExcel.setValue(4, 1, "本日無資料");
		}
	}

	/**
	 * 全年 專員業績
	 * 
	 * @param titaVo      titaVo
	 * @param wkSsnVo     工作年月
	 * @param wkMonth     本工作月
	 * @param lastWkMonth 上季末工作月
	 */
	private void setEmp(TitaVo titaVo, Map<String, String> wkSsnVo, int wkMonth, int lastWkMonth) throws LogicException {
		this.info("setEmp start ...");
		List<Map<String, String>> queryResultList = null;
		try {
			// 取得專員本年度業績資料
			queryResultList = lP003ServiceImpl.findEmp(titaVo, wkSsnVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LP003ServiceImpl.findDept error = " + errors.toString());
		}
		if (queryResultList != null && queryResultList.size() > 0) {
			// 決定每個排名的RowCursors
			Map<String, Integer> rowCursorsMap = new HashMap<>();
			int rowCursor = 4; // 明細起始列 = 4
			int rowCursorOfTotal = 5; // 合計列
			for (Map<String, String> m : queryResultList) {
				String rank = m.get("F0");
				if (!rowCursorsMap.containsKey(rank)) {
					rowCursorsMap.put(rank, rowCursor);
					rowCursor++;
				}
			}
			// 明細總列數 = rowCursor - 表頭列數 - 最後一筆多加1次
			int detailRowSize = rowCursor - 3 - 1;
			// 把合計往下搬
			if (detailRowSize > 1) {
				// 底稿的合計列在第6列
				makeExcel.setShiftRow(rowCursorOfTotal, detailRowSize - 1);
				rowCursorOfTotal = rowCursorOfTotal + detailRowSize - 1;
			}
			Map<String, BigDecimal[]> lastSeasonSummary = new HashMap<>();
			Map<String, BigDecimal[]> yearSummary = new HashMap<>();
			Map<Integer, BigDecimal[]> total = new HashMap<>();
			for (Map<String, String> m : queryResultList) {
				String rank = m.get("F0"); // 排行
				String dept = m.get("F1"); // 部室
				String dist = m.get("F2"); // 區部
				String emp = m.get("F3"); // 房貸部專-姓名
				int workMonth = Integer.parseInt(m.get("F4")); // 工作月
				workMonth = workMonth % 100;// 去掉年度
				BigDecimal goalAmt = getBigDecimal(m.get("F5")); // 責任額
				BigDecimal perfCnt = getBigDecimal(m.get("F6")); // 撥款件數
				BigDecimal perfAmt = getBigDecimal(m.get("F7")); // 撥款金額
				if (lastWkMonth > 0 && workMonth <= lastWkMonth) {
					// 第1~lastWkMonth工作月累計
					BigDecimal[] summary = new BigDecimal[3];
					if (lastSeasonSummary.containsKey(rank)) {
						summary = lastSeasonSummary.get(rank);
					} else {
						summary[0] = BigDecimal.ZERO;
						summary[1] = BigDecimal.ZERO;
						summary[2] = BigDecimal.ZERO;
					}
					summary[0] = summary[0].add(goalAmt);
					summary[1] = summary[1].add(perfCnt);
					summary[2] = summary[2].add(perfAmt);
					lastSeasonSummary.put(rank, summary);
					// 合計
					BigDecimal[] tmpToal = new BigDecimal[3];
					if (total.containsKey(lastWkMonth)) {
						tmpToal = total.get(lastWkMonth);
					} else {
						tmpToal[0] = BigDecimal.ZERO;
						tmpToal[1] = BigDecimal.ZERO;
						tmpToal[2] = BigDecimal.ZERO;
					}
					tmpToal[0] = tmpToal[0].add(goalAmt);
					tmpToal[1] = tmpToal[1].add(perfCnt);
					tmpToal[2] = tmpToal[2].add(perfAmt);
					total.put(lastWkMonth, tmpToal);
				} else {
					rowCursor = rowCursorsMap.get(rank);
					int columnCursor = 1 + ((workMonth - lastWkMonth - 1) * 4) + (lastWkMonth == 0 ? 5 : 9);
					// 排行
					makeExcel.setValue(rowCursor, 1, getBigDecimal(rank));
					// 部室
					makeExcel.setValue(rowCursor, 2, dept);
					// 區部
					makeExcel.setValue(rowCursor, 3, dist);
					// 主管
					makeExcel.setValue(rowCursor, 4, emp);
					// 起月
					makeExcel.setValue(rowCursor, 5, 1);
					// 責任額
					makeExcel.setValue(rowCursor, columnCursor, goalAmt, "#,##0");
					// 撥款金額
					makeExcel.setValue(rowCursor, columnCursor + 1, perfAmt, "#,##0");
					// 撥款件數
					makeExcel.setValue(rowCursor, columnCursor + 2, perfCnt, "#,##0.0");
					// 達成率
					makeExcel.setValue(rowCursor, columnCursor + 3, computeDivide(perfAmt, goalAmt, 4), "#,##0%");
					// 合計
					BigDecimal[] tmpToal = new BigDecimal[3];
					if (total.containsKey(workMonth)) {
						tmpToal = total.get(workMonth);
					} else {
						tmpToal[0] = BigDecimal.ZERO;
						tmpToal[1] = BigDecimal.ZERO;
						tmpToal[2] = BigDecimal.ZERO;
					}
					tmpToal[0] = tmpToal[0].add(goalAmt);
					tmpToal[1] = tmpToal[1].add(perfCnt);
					tmpToal[2] = tmpToal[2].add(perfAmt);
					total.put(workMonth, tmpToal);
				}
				// 全年累計
				BigDecimal[] summary = new BigDecimal[3];
				if (yearSummary.containsKey(rank)) {
					summary = yearSummary.get(rank);
				} else {
					summary[0] = BigDecimal.ZERO;
					summary[1] = BigDecimal.ZERO;
					summary[2] = BigDecimal.ZERO;
				}
				summary[0] = summary[0].add(goalAmt);
				summary[1] = summary[1].add(perfCnt);
				summary[2] = summary[2].add(perfAmt);
				yearSummary.put(rank, summary);
			}
			// 列印第1~lastWkMonth工作月累計
			if (lastWkMonth > 0) {
				for (Entry<String, BigDecimal[]> entry : lastSeasonSummary.entrySet()) {
					String k = entry.getKey();
					BigDecimal[] v = entry.getValue();
					rowCursor = rowCursorsMap.get(k);
					// 責任額
					makeExcel.setValue(rowCursor, 6, v[0], "#,##0");
					// 撥款金額
					makeExcel.setValue(rowCursor, 7, v[2], "#,##0");
					// 撥款件數
					makeExcel.setValue(rowCursor, 8, v[1], "#,##0.0");
					// 達成率
					makeExcel.setValue(rowCursor, 9, computeDivide(v[2], v[0], 4), "#,##0%");
				}
			}
			// 列印全年累計
			int columnCursor = 1 + ((wkMonth + 1 - lastWkMonth - 1) * 4) + (lastWkMonth == 0 ? 5 : 9);
			// 全年累計
			BigDecimal[] summaryTotal = new BigDecimal[3];
			summaryTotal[0] = BigDecimal.ZERO;
			summaryTotal[1] = BigDecimal.ZERO;
			summaryTotal[2] = BigDecimal.ZERO;
			for (Entry<String, BigDecimal[]> entry : yearSummary.entrySet()) {
				String k = entry.getKey();
				BigDecimal[] v = entry.getValue();
				rowCursor = rowCursorsMap.get(k);
				// 責任額
				makeExcel.setValue(rowCursor, columnCursor, v[0], "#,##0");
				// 撥款金額
				makeExcel.setValue(rowCursor, columnCursor + 1, v[2], "#,##0");
				// 撥款件數
				makeExcel.setValue(rowCursor, columnCursor + 2, v[1], "#,##0.0");
				// 達成率
				makeExcel.setValue(rowCursor, columnCursor + 3, computeDivide(v[2], v[0], 4), "#,##0%");
				summaryTotal[0] = summaryTotal[0].add(v[0]);
				summaryTotal[1] = summaryTotal[1].add(v[1]);
				summaryTotal[2] = summaryTotal[2].add(v[2]);
			}
			// 列印合計
			for (Entry<Integer, BigDecimal[]> entry : total.entrySet()) {
				int tmpWorkMonth = entry.getKey();
				BigDecimal[] v = entry.getValue();
				columnCursor = 1 + ((tmpWorkMonth - lastWkMonth - 1) * 4) + (lastWkMonth == 0 ? 5 : 9);
				// 責任額
				makeExcel.setValue(rowCursorOfTotal, columnCursor, v[0], "#,##0");
				// 撥款金額
				makeExcel.setValue(rowCursorOfTotal, columnCursor + 1, v[2], "#,##0");
				// 撥款件數
				makeExcel.setValue(rowCursorOfTotal, columnCursor + 2, v[1], "#,##0.0");
				// 達成率
				makeExcel.setValue(rowCursorOfTotal, columnCursor + 3, computeDivide(v[2], v[0], 4), "#,##0%");
			}
			// 列印全年累計的合計
			columnCursor = 1 + ((wkMonth + 1 - lastWkMonth - 1) * 4) + (lastWkMonth == 0 ? 5 : 9);
			// 責任額
			makeExcel.setValue(rowCursorOfTotal, columnCursor, summaryTotal[0], "#,##0");
			// 撥款金額
			makeExcel.setValue(rowCursorOfTotal, columnCursor + 1, summaryTotal[2], "#,##0");
			// 撥款件數
			makeExcel.setValue(rowCursorOfTotal, columnCursor + 2, summaryTotal[1], "#,##0.0");
			// 達成率
			makeExcel.setValue(rowCursorOfTotal, columnCursor + 3, computeDivide(summaryTotal[2], summaryTotal[0], 4), "#,##0%");
		} else {
			makeExcel.setValue(4, 1, "本日無資料");
		}
	}

	/**
	 * 當月 房貸部專業績
	 * 
	 * @param titaVo  titaVo
	 * @param wkSsnVo 工作年月
	 * @param wkMonth 本工作月
	 */
	private void setDeptThisWKM(TitaVo titaVo, Map<String, String> wkSsnVo, int wkMonth) throws LogicException {
		this.info("setDeptThisWKM start ... ");
		// 民國年
		String rocYear = String.valueOf(Integer.valueOf(wkSsnVo.get("F0")) - 1911);
		String dateM = titaVo.getEntDy();
		List<Map<String, String>> queryResult = null;
		try {
			queryResult = lP003ServiceImpl.findDeptThisWKM(titaVo, wkSsnVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LP003ServiceImpl setDeptThisWKM error = " + errors.toString());
		}
		// 房貸部專業績統計報表
		makeExcel.setSheet("Sheet2");
		// 標題
		makeExcel.setValue(1, 1, rocYear + "." + wkMonth + "工作月房貸部專業績統計報表", "C");
		makeExcel.setValue(2, 1, "結算日期：" + this.showRocDate(dateM, 6), "R");
		if (queryResult != null && queryResult.size() > 0) {
			// 起始列
			int rowCursor = 4;
			// 合計列
			int rowCursorOfTotal = 5;
			// 把合計往下搬
			if (queryResult.size() > 1) {
				makeExcel.setShiftRow(rowCursorOfTotal, queryResult.size() - 1);
				rowCursorOfTotal = rowCursorOfTotal + queryResult.size() - 1;
			}
			// 合計-房貸責任額
			BigDecimal targetTotal = BigDecimal.ZERO;
			BigDecimal drawdownTotal = BigDecimal.ZERO;
			BigDecimal countTotal = BigDecimal.ZERO;
			for (Map<String, String> m : queryResult) {
				// 排行
				makeExcel.setValue(rowCursor, 1, Integer.parseInt(m.get("F0")));
				// 駐在地
				makeExcel.setValue(rowCursor, 2, m.get("F1"));
				// 姓名
				makeExcel.setValue(rowCursor, 3, m.get("F2"));
				// 電腦編號
				makeExcel.setValue(rowCursor, 4, m.get("F3"));
				// 房貸責任額
				BigDecimal targetAmt = getBigDecimal(m.get("F4"));
				makeExcel.setValue(rowCursor, 5, targetAmt, "#,##0");
				targetTotal = targetTotal.add(targetAmt); // 加總
				// 房貸撥款金額
				BigDecimal drawdownAmt = getBigDecimal(m.get("F5"));
				makeExcel.setValue(rowCursor, 6, drawdownAmt, "#,##0");
				drawdownTotal = drawdownTotal.add(drawdownAmt); // 加總
				// 房貸件數
				BigDecimal count = getBigDecimal(m.get("F6"));
				makeExcel.setValue(rowCursor, 7, count, "0.0");
				countTotal = countTotal.add(count); // 加總
				// 達成率
				makeExcel.setValue(rowCursor, 8, this.computeDivide(drawdownAmt, targetAmt, 4), "0.00%");
				// 服務部室
				makeExcel.setValue(rowCursor, 9, m.get("F7"), "C");
				rowCursor++;
			}
			// 印合計
			// 合計-房貸責任額
			makeExcel.setValue(rowCursorOfTotal, 5, targetTotal, "#,##0");
			// 合計-房貸撥款金額
			makeExcel.setValue(rowCursorOfTotal, 6, drawdownTotal, "#,##0");
			// 合計-房貸件數
			makeExcel.setValue(rowCursorOfTotal, 7, countTotal, "0.0");
			// 合計-達成率
			makeExcel.setValue(rowCursorOfTotal, 8, this.computeDivide(drawdownTotal, targetTotal, 4), "0.00%");
		} else {
			makeExcel.setValue(4, 1, "本日無資料");
		}
	}

	/**
	 * 當月-房貸專員業績
	 * 
	 * @param titaVo  titaVo
	 * @param wkSsnVo 工作年月
	 * @param wkMonth 本工作月
	 */
	private void setEmpThisWKM(TitaVo titaVo, Map<String, String> wkSsnVo, int wkMonth) throws LogicException {
		this.info("setEmpThisWKM start ...");
		// 民國年
		String rocYear = String.valueOf(Integer.valueOf(wkSsnVo.get("F0")) - 1911);
		String dateM = titaVo.getEntDy();
		List<Map<String, String>> queryResult = null;
		try {
			queryResult = lP003ServiceImpl.findEmpThisWKM(titaVo, wkSsnVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LP003ServiceImpl.findEmp error = " + errors.toString());
		}
		// 房貸專員業績統計報表
		makeExcel.setSheet("Sheet1");
		// 標題
		makeExcel.setValue(1, 1, rocYear + "." + wkMonth + "工作月房貸專員業績統計報表", "C");
		makeExcel.setValue(2, 1, "結算日期：" + this.showRocDate(dateM, 6), "R");
		if (queryResult != null && queryResult.size() > 0) {
			// 起始列
			int rowCursor = 4;
			// 合計列
			int rowCursorOfTotal = 5;
			// 把合計往下搬
			if (queryResult.size() > 1) {
				makeExcel.setShiftRow(rowCursorOfTotal, queryResult.size() - 1);
				rowCursorOfTotal = rowCursorOfTotal + queryResult.size() - 1;
			}
			// 合計-房貸責任額
			BigDecimal targetTotal = BigDecimal.ZERO;
			BigDecimal drawdownTotal = BigDecimal.ZERO;
			BigDecimal countTotal = BigDecimal.ZERO;
			// 房貸專員
			for (Map<String, String> m : queryResult) {
				// 排行
				makeExcel.setValue(rowCursor, 1, Integer.parseInt(m.get("F0")));
				// 姓名
				makeExcel.setValue(rowCursor, 2, m.get("F1"), "C");
				// 電腦編號
				makeExcel.setValue(rowCursor, 3, m.get("F2"), "C");
				// 房貸責任額
				BigDecimal targetAmt = getBigDecimal(m.get("F3"));
				makeExcel.setValue(rowCursor, 4, targetAmt, "#,##0");
				targetTotal = targetTotal.add(targetAmt);
				// 房貸撥款金額
				BigDecimal drawdownAmt = getBigDecimal(m.get("F4"));
				makeExcel.setValue(rowCursor, 5, drawdownAmt, "#,##0");
				drawdownTotal = drawdownTotal.add(drawdownAmt);
				// 房貸件數
				BigDecimal count = getBigDecimal(m.get("F5"));
				makeExcel.setValue(rowCursor, 6, count, "0.0");
				countTotal = countTotal.add(count);
				// 達成率
				makeExcel.setValue(rowCursor, 7, this.computeDivide(drawdownAmt, targetAmt, 4), "0.00%");
				// 服務部室
				makeExcel.setValue(rowCursor, 8, m.get("F6"), "C");
				// 服務區部
				makeExcel.setValue(rowCursor, 9, m.get("F7"), "C");
				rowCursor++;
			}
			// 印合計
			// 合計-房貸責任額
			makeExcel.setValue(rowCursorOfTotal, 4, targetTotal, "#,##0");
			// 合計-房貸撥款金額
			makeExcel.setValue(rowCursorOfTotal, 5, drawdownTotal, "#,##0");
			// 合計-房貸件數
			makeExcel.setValue(rowCursorOfTotal, 6, countTotal, "0.0");
			// 合計-達成率
			makeExcel.setValue(rowCursorOfTotal, 7, this.computeDivide(drawdownTotal, targetTotal, 4), "0.00%");
		} else {
			makeExcel.setValue(4, 1, "本日無資料");
		}
	}
}
