package com.st1.itx.trade.LQ;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LQ008ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;

@Component
@Scope("prototype")
public class LQ008Report extends MakeReport {

	@Autowired
	LQ008ServiceImpl LQ008ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	public void exec(TitaVo titaVo) throws LogicException {
		List<Map<String, String>> listQueryA = null;
		List<Map<String, String>> listQueryB = null;
		List<Map<String, String>> listQueryC = null;

		try {
			listQueryA = LQ008ServiceImpl.queryA(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LQ008ServiceImpl.testExcel error = " + errors.toString());
		}

		try {
			listQueryB = LQ008ServiceImpl.queryB(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LQ008ServiceImpl.testExcel error = " + errors.toString());
		}
		try {
			listQueryC = LQ008ServiceImpl.queryC(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LQ008ServiceImpl.testExcel error = " + errors.toString());
		}
		exportExcel(listQueryA, listQueryB, listQueryC, titaVo);
	}

	private void exportExcel(List<Map<String, String>> listA, List<Map<String, String>> listB,
			List<Map<String, String>> listC, TitaVo titaVo) throws LogicException {

		this.info("exportExcel");

		if (listA == null) {
			listA = new ArrayList<>();
		}
		if (listB == null) {
			listB = new ArrayList<>();
		}
		if (listC == null) {
			listC = new ArrayList<>();
		}

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "LQ008";
		String fileItem = "表A07_會計部申報表";
		String fileName = "LQ008表A07_會計部申報表";
		String defaultExcel = "LQ008_底稿_表A07_會計部申報表.xlsx";
		String defaultSheet = "新表7(108.03.31)";

		this.info("reportVo open");

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();
		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);

//		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LQ008", "表A07_會計部申報表", "LQ008表A07_會計部申報表", "LQ008_底稿_表A07_會計部申報表.xlsx", "新表7(108.03.31)");

		int bcAcDate = titaVo.getEntDyI() + 19110000;

		// 輸出日期
		makeExcel.setValue(2, 5, "中華民國" + showRocDate(bcAcDate, 0), "C");

		makeExcel.setSheet("新表7(108.03.31)", "新表7(" + showRocDate(bcAcDate, 6) + ")");

		int rowCursorA = 5;
		int rowCursorB = 8;
		int rowCursorC = 11;

		if (listA.size() > 1) {
			// 將表格往下移，移出空間
			makeExcel.setShiftRow(rowCursorA + 1, listA.size() - 1);
			// 更新行數指標
			rowCursorB += listA.size() - 1;
			rowCursorC += listA.size() - 1;
		}
		if (listA.size() > 0) {
			// 寫入資料
			setValueToExcel(rowCursorA, listA, false);
		}
		if (listB.size() > 1) {
			// 將表格往下移，移出空間
			makeExcel.setShiftRow(rowCursorB + 1, listB.size() - 1);
			// 更新行數指標
			rowCursorC += listB.size() - 1;
		}
		if (listB.size() > 0) {
			// 寫入資料
			setValueToExcel(rowCursorB, listB, true);
		}
		if (listC.size() > 1) {
			// 將表格往下移，移出空間
			makeExcel.setShiftRow(rowCursorC + 1, listC.size() - 1);
		}
		if (listC.size() > 0) {
			// 寫入資料
			setValueToExcel(rowCursorC, listC, true);
		}

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

	private void setValueToExcel(int rowCursor, List<Map<String, String>> list, boolean mergeFlag)
			throws LogicException {

		Map<String, int[]> mergeRowRange = new HashMap<>();

		for (Map<String, String> map : list) {
			// 戶名
			makeExcel.setValue(rowCursor, 1, map.get("F0"));
			// 身分證號碼/統一編號
			makeExcel.setValue(rowCursor, 2, map.get("F1"));
			// 交易行為類別代號
			makeExcel.setValue(rowCursor, 5, "1");
			// 交易行為類別
			makeExcel.setValue(rowCursor, 6, "授信（不含短期票券之保證或背書）");
			// 內容說明類別代號
			makeExcel.setValue(rowCursor, 7, "12");

			BigDecimal amt = divThousand(getBigDecimal(map.get("F2")));

			// 內容說明
			makeExcel.setValue(rowCursor, 8, "有擔保品金額:" + formatAmt(amt, 0));
			// 有無保證機構
			makeExcel.setValue(rowCursor, 9, "N");

			BigDecimal groupAmt = divThousand(getBigDecimal(map.get("F3")));

			// 交易金額
			makeExcel.setValue(rowCursor, 10, groupAmt);

			// 判斷是否需要合併
			if (mergeFlag) {

				// 取得PK
				String custRelMainUKey = map.get("F4");

				int[] rowRange = new int[2];

				if (mergeRowRange.containsKey(custRelMainUKey)) {

					rowRange = mergeRowRange.get(custRelMainUKey);

					if (rowRange[0] > rowCursor) {
						rowRange[0] = rowCursor;
					}
					if (rowRange[1] < rowCursor) {
						rowRange[1] = rowCursor;
					}
				} else {
					rowRange[0] = rowCursor;
					rowRange[1] = rowCursor;
				}
				this.info("rowRange[0] = " + rowRange[0]);
				this.info("rowRange[1] = " + rowRange[1]);

				mergeRowRange.put(custRelMainUKey, rowRange);
			}
			rowCursor++;
		}

		// 合併儲存格
		if (mergeFlag && mergeRowRange.size() > 0) {
			mergeRows(mergeRowRange);
		}
	}

	/**
	 * 合併一批資料的儲存格
	 * 
	 * @param mergeColumnRange 一批資料中需要合併的起訖行數
	 */
	private void mergeRows(Map<String, int[]> mergeRowRange) {

		mergeRowRange.forEach((k, v) -> {
//			this.info("mergeRows k " + k);
//			this.info("mergeRows v[0] " + v[0]);
//			this.info("mergeRows v[1] " + v[1]);

			if (v[0] < v[1]) {
				// 交易金額
				makeExcel.setMergedRegion(v[0], v[1], 10, 10);
			}
		});

	}

	/**
	 * 千元單位運算
	 * 
	 * @param input 數值，此報表應僅有淨值、擔保品估價及授信餘額需要做千元單位運算
	 * @return 傳入值除1000之結果
	 */
	private BigDecimal divThousand(BigDecimal input) {

//		this.info("divThousand input = " + input);

		if (input == null) {
			return BigDecimal.ZERO;
		}

		input = computeDivide(input, new BigDecimal("1000"), 3);

//		this.info("divThousand return = " + input);

		return input;
	}
}
