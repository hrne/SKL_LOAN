package com.st1.itx.trade.LY;

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
import com.st1.itx.db.domain.CdVarValue;
import com.st1.itx.db.service.CdVarValueService;
import com.st1.itx.db.service.springjpa.cm.LY005ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")
public class LY005Report extends MakeReport {

	@Autowired
	LY005ServiceImpl lY005ServiceImpl;

	@Autowired
	CdVarValueService sCdVarValueService;

	@Autowired
	Parse parse;

	@Autowired
	MakeExcel makeExcel;

	private BigDecimal totalEquity = BigDecimal.ZERO;
	private String equityDataMonthOutput = "";

	public void exec(TitaVo titaVo) throws LogicException {

		int entdyf = titaVo.getEntDyI() + 19110000;

		int inputYearMonth = entdyf / 100;

		CdVarValue tCdVarValue = sCdVarValueService.findYearMonthFirst(inputYearMonth, titaVo);

		if (tCdVarValue != null) {
			int equityDataMonth = tCdVarValue.getYearMonth() - 191100;
			totalEquity = tCdVarValue.getTotalequity();
			equityDataMonthOutput = String.valueOf(equityDataMonth);
			equityDataMonthOutput = equityDataMonthOutput.substring(0, 3) + "." + equityDataMonthOutput.substring(3);
		}

		List<Map<String, String>> lY005List = null;

		try {
			lY005List = lY005ServiceImpl.queryDetail(inputYearMonth, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LY005ServiceImpl.findAll error = " + errors.toString());
		}

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LY005", "非RBC_表20_會計部年度檢查報表",
				"LY005_非RBC_表20_會計部年度檢查報表", "LY005_底稿_非RBC_表20_會計部年度檢查報表.xlsx", "YYY.MM");

		String entdy = titaVo.getEntDy();

		String year = entdy.substring(1, 4);
		String month = entdy.substring(4, 6);

		makeExcel.setSheet("YYY.MM", year + "." + month);

		// 通用處理
		// 設定表中顯示的日期

		makeExcel.setValue(1, 16, this.showRocDate(entdy, 6)); // 資料日期

		makeExcel.setValue(1, 17, equityDataMonthOutput, "C"); // 核閱數資料日期

		makeExcel.setValue(2, 17, totalEquity, "#,##0"); // 核閱數

		if (lY005List != null && !lY005List.isEmpty()) {

			// 與本公司關係為G者 合併為一筆
			lY005List = groupG(lY005List);

			int rowCursor = 5; // 列指標

			// 新增明細行數
			if (lY005List.size() > 1) {
				// 將表格往下移，移出空間
				makeExcel.setShiftRow(rowCursor + 1, lY005List.size() - 1);
			}

			BigDecimal txAmtTotal = BigDecimal.ZERO;

			// 列號
			int seq = 1;

			for (Map<String, String> tLDVo : lY005List) {

				String valueStr = "";
				BigDecimal valueNum = BigDecimal.ZERO;

				makeExcel.setValue(rowCursor, 2, seq, "C"); // 列號

				for (int i = 0; i <= 13; i++) {
					valueStr = tLDVo.get("F" + i);

					if (valueStr == null || valueStr.isEmpty()) {
						continue;
					}

					if (parse.isNumeric(valueStr)) {
						valueNum = getBigDecimal(valueStr);
					} else {
						valueNum = BigDecimal.ZERO;
					}

					int columnCursor = 3 + i; // 欄指標

					switch (i) {
					case 6:
						// 交易日期
						makeExcel.setValue(rowCursor, columnCursor, this.showBcDate(valueStr, 0));
						break;
					case 7:
						txAmtTotal = txAmtTotal.add(valueNum); // 交易金額加總
					case 8:
					case 9:
					case 10:
						// 金額欄位
						if (valueNum.compareTo(BigDecimal.ZERO) != 0) {
							makeExcel.setValue(rowCursor, columnCursor, valueNum, "#,##0");
						}
						break;
					case 11:
						// 交易金額占業主權益比率%
						valueNum = valueNum.multiply(getBigDecimal(100));
						makeExcel.setValue(rowCursor, columnCursor, valueNum, "#,##0.00");
						break;
					default:
						if (valueStr != null && !valueStr.isEmpty()) {
							makeExcel.setValue(rowCursor, columnCursor, valueStr);
						}
						break;
					}
				}
				rowCursor++;
				seq++;
			}

			// 交易金額 total 輸出
			makeExcel.setValue(rowCursor, 10, txAmtTotal, "#,##0");

			makeExcel.setValue(rowCursor + 1, 2,
					equityDataMonthOutput + " 淨值: " + formatAmt(totalEquity, 0) + "元 (核閱數)", "L");
		} else {
			// 無資料時處理
			makeExcel.setValue(5, 5, "本日無資料", "L");
		}

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);

	}

	/**
	 * 關係為G者合併為一筆
	 * 
	 * @param lY005List 資料明細
	 * @return 排除G的資料明細
	 */
	private List<Map<String, String>> groupG(List<Map<String, String>> lY005List) {

		String gCustName = "";
		BigDecimal gTxAmt = BigDecimal.ZERO;

		int counts = 0;

		List<Map<String, String>> result = new ArrayList<>();

		for (Map<String, String> m : lY005List) {
			String f0 = m.get("F0");

			if (f0.equals("G")) {
				gCustName = m.get("F2"); // 後蓋前取最後一筆姓名
				gTxAmt = gTxAmt.add(getBigDecimal(m.get("F7")));
				counts++;
			} else {
				result.add(m);
			}
		}

		gCustName += "等" + counts + "筆";

		BigDecimal gPercent = this.computeDivide(gTxAmt, totalEquity, 5);

		// 合併後的結果add回LIST
		this.info("gCustName = " + gCustName);
		this.info("gTxAmt = " + gTxAmt);
		this.info("gPercent = " + gPercent);

		Map<String, String> gMap = new HashMap<>();

		gMap.put("F0", "G"); // 與本公司關係
		gMap.put("F1", ""); // 交易對象代號
		gMap.put("F2", gCustName); // 交易對象名稱
		gMap.put("F3", "A"); // 交易種類
		gMap.put("F4", "C"); // 交易型態
		gMap.put("F5", "合併列示不另表述"); // 交易標的內容
		gMap.put("F6", ""); // 交易日期
		gMap.put("F7", gTxAmt.toString()); // 交易金額
		gMap.put("F8", ""); // 最近交易日之市價
		gMap.put("F9", ""); // 已實現損益
		gMap.put("F10", ""); // 未實現損益
		gMap.put("F11", gPercent.toString()); // 交易金額佔業主權益比率%
		gMap.put("F12", ""); // 最後決定權人員
		gMap.put("F13", ""); // 備註

		result.add(gMap);

		return result;
	}
}
