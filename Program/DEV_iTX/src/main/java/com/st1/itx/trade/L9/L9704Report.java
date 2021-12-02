package com.st1.itx.trade.L9;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L9704ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ExcelFontStyleVo;
import com.st1.itx.util.format.FormatUtil;

@Component("L9704Report")
@Scope("prototype")
public class L9704Report extends MakeReport {

	@Autowired
	L9704ServiceImpl l9704ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	ExcelFontStyleVo fontStyleVo;

	String lastAcBookItem;

	// 小計
	BigDecimal sumLastMonthOvduPrinBal = BigDecimal.ZERO;
	BigDecimal sumLastMonthOvduIntBal = BigDecimal.ZERO;
	BigDecimal sumThisMonthOvduPrinBal = BigDecimal.ZERO;
	BigDecimal sumThisMonthOvduIntBal = BigDecimal.ZERO;

	// 合計
	BigDecimal totalLastMonthOvduPrinBal = BigDecimal.ZERO;
	BigDecimal totalLastMonthOvduIntBal = BigDecimal.ZERO;
	BigDecimal totalThisMonthOvduPrinBal = BigDecimal.ZERO;
	BigDecimal totalThisMonthOvduIntBal = BigDecimal.ZERO;

	public void exec(int lastMonth, int thisMonth, TitaVo titaVo) throws LogicException {
		List<Map<String, String>> listL9704 = null;

		try {
			listL9704 = l9704ServiceImpl.findAll(lastMonth, thisMonth, titaVo);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L9704ServiceImpl.findAll error = " + errors.toString());
		}

		exportExcel(thisMonth, listL9704, titaVo);
	}

	private void exportExcel(int thisMonth, List<Map<String, String>> listL9704, TitaVo titaVo) throws LogicException {
		this.info("L9704 exportExcel");

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L9704", "催收款明細表", "L9704催收款明細表", "L9704_底稿_催收款明細表.xlsx", "工作表1", this.showRocDate(thisMonth * 100 + 1, 5));

		fontStyleVo = new ExcelFontStyleVo();

		fontStyleVo.setFont((short) 1); // 字體 : 標楷體

		fontStyleVo.setSize((short) 12); // 字體大小 : 12

		int printRow = 3; // 從第三行開始印

		if (listL9704 == null || listL9704.isEmpty()) {

			makeExcel.setValue(printRow, 1, "本日無資料", fontStyleVo);

		} else {

			lastAcBookItem = listL9704.get(0).get("F13");

			for (Map<String, String> mapL9704 : listL9704) {

				// F13 帳冊別中文
				String acBookItem = mapL9704.get("F13");
				if (!acBookItem.equals(lastAcBookItem)) {
					// 若帳冊別與上一筆不同，印小計
					printRow = printSum(printRow);
				}

				lastAcBookItem = acBookItem;

				// F0 戶號
				String custNo = FormatUtil.pad9(mapL9704.get("F0"), 7);
				// F1 額度
				String facmNo = FormatUtil.pad9(mapL9704.get("F1"), 3);

				makeExcel.setValue(printRow, 1, custNo + "-" + facmNo, fontStyleVo);

				// F2 戶名/公司名稱
				String custName = mapL9704.get("F2");

				makeExcel.setValue(printRow, 2, custName, fontStyleVo);

				// F3 初貸日
				String firstDrawdownDate = mapL9704.get("F3");

				makeExcel.setValue(printRow, 3, this.showRocDate(firstDrawdownDate, 1), fontStyleVo);

				// F4 繳息迄日
				String prevIntDate = mapL9704.get("F4");

				makeExcel.setValue(printRow, 4, this.showRocDate(prevIntDate, 1), fontStyleVo);

				// F5 核准科目
				String facAcctCode = mapL9704.get("F5");

				makeExcel.setValue(printRow, 5, facAcctCode, fontStyleVo);

				// F6 轉催收日期
				String ovduDate = mapL9704.get("F6");

				makeExcel.setValue(printRow, 6, this.showRocDate(ovduDate, 1), fontStyleVo);

				// F7 上月催收本金餘額
				BigDecimal lastMonthOvduPrinBal = getBigDecimal(mapL9704.get("F7"));

				makeExcel.setValue(printRow, 7, lastMonthOvduPrinBal, "#,##0", "R", fontStyleVo);

				// F8 上月催收利息餘額 + 上月催收違約金餘額
				BigDecimal lastMonthOvduIntBal = mapL9704.get("F8").equals("1") ? BigDecimal.ZERO : getBigDecimal(mapL9704.get("F8"));

				makeExcel.setValue(printRow, 8, lastMonthOvduIntBal, "#,##0", "R", fontStyleVo);

				// 上月催收餘額 = F7 + F8
				makeExcel.setValue(printRow, 9, lastMonthOvduPrinBal.add(lastMonthOvduIntBal), "#,##0", "R", fontStyleVo);

				// F9 本月催收本金餘額
				BigDecimal thisMonthOvduPrinBal = mapL9704.get("F8").equals("1") ? BigDecimal.ONE : getBigDecimal(mapL9704.get("F9"));

				makeExcel.setValue(printRow, 12, thisMonthOvduPrinBal, "#,##0", "R", fontStyleVo);

				// F10 本月催收利息餘額 + 本月催收違約金餘額
				BigDecimal thisMonthOvduIntBal = mapL9704.get("F8").equals("1") ? BigDecimal.ZERO : getBigDecimal(mapL9704.get("F10"));

				makeExcel.setValue(printRow, 13, thisMonthOvduIntBal, "#,##0", "R", fontStyleVo);

				// 本月催收餘額 = F9 + F10
				makeExcel.setValue(printRow, 14, thisMonthOvduPrinBal.add(thisMonthOvduIntBal), "#,##0", "R", fontStyleVo);

				// 本月增減-本金 = F9 - F7
				makeExcel.setValue(printRow, 10, thisMonthOvduPrinBal.subtract(lastMonthOvduPrinBal), "#,##0", "R", fontStyleVo);

				if (!mapL9704.get("F8").equals("1")) {
					// 本月增減-利息 = F10 - F8
					makeExcel.setValue(printRow, 11, thisMonthOvduIntBal.subtract(lastMonthOvduIntBal), "#,##0", "R", fontStyleVo);
				}

				// F11 催收人員姓名
				String fullname = mapL9704.get("F11");

				makeExcel.setValue(printRow, 15, fullname, fontStyleVo);

				// F12 地區別名稱
				String cityName = mapL9704.get("F12");

				makeExcel.setValue(printRow, 16, cityName, fontStyleVo);

				// 計算小計
				sumLastMonthOvduPrinBal = sumLastMonthOvduPrinBal.add(lastMonthOvduPrinBal);
				sumLastMonthOvduIntBal = sumLastMonthOvduIntBal.add(lastMonthOvduIntBal);
				sumThisMonthOvduPrinBal = sumThisMonthOvduPrinBal.add(thisMonthOvduPrinBal);
				sumThisMonthOvduIntBal = sumThisMonthOvduIntBal.add(thisMonthOvduIntBal);

				// 計算合計
				totalLastMonthOvduPrinBal = totalLastMonthOvduPrinBal.add(lastMonthOvduPrinBal);
				totalLastMonthOvduIntBal = totalLastMonthOvduIntBal.add(lastMonthOvduIntBal);
				totalThisMonthOvduPrinBal = totalThisMonthOvduPrinBal.add(thisMonthOvduPrinBal);
				totalThisMonthOvduIntBal = totalThisMonthOvduIntBal.add(thisMonthOvduIntBal);

				// 寫完一筆，行數+1
				printRow++;
			}

			// 印小計
			printRow = printSum(printRow);

			// 印合計
			printTotal(printRow);

		}

		// 畫框線
		makeExcel.setAddRengionBorder("A", 1, "P", printRow, 1);

		long sno = makeExcel.close();
		//makeExcel.toExcel(sno);
	}

	// 印小計
	public int printSum(int printRow) throws LogicException {

		makeExcel.setValue(printRow, 4, lastAcBookItem, fontStyleVo);

		makeExcel.setValue(printRow, 6, "小計:", "C", fontStyleVo);

		makeExcel.setValue(printRow, 7, sumLastMonthOvduPrinBal, "#,##0", "R", fontStyleVo);

		makeExcel.setValue(printRow, 8, sumLastMonthOvduIntBal, "#,##0", "R", fontStyleVo);

		// 上月催收餘額 = F7 + F8
		makeExcel.setValue(printRow, 9, sumLastMonthOvduPrinBal.add(sumLastMonthOvduIntBal), "#,##0", "R", fontStyleVo);

		makeExcel.setValue(printRow, 12, sumThisMonthOvduPrinBal, "#,##0", "R", fontStyleVo);

		makeExcel.setValue(printRow, 13, sumThisMonthOvduIntBal, "#,##0", "R", fontStyleVo);

		// 本月催收餘額 = F9 + F10
		makeExcel.setValue(printRow, 14, sumThisMonthOvduPrinBal.add(sumThisMonthOvduIntBal), "#,##0", "R", fontStyleVo);

		// 本月增減-本金 = F9 - F7
		makeExcel.setValue(printRow, 10, sumThisMonthOvduPrinBal.subtract(sumLastMonthOvduPrinBal), "#,##0", "R", fontStyleVo);

		// 本月增減-利息 = F10 - F8
		makeExcel.setValue(printRow, 11, sumThisMonthOvduIntBal.subtract(sumLastMonthOvduIntBal), "#,##0", "R", fontStyleVo);

		// 將小計歸零
		sumLastMonthOvduPrinBal = BigDecimal.ZERO;
		sumLastMonthOvduIntBal = BigDecimal.ZERO;
		sumThisMonthOvduPrinBal = BigDecimal.ZERO;
		sumThisMonthOvduIntBal = BigDecimal.ZERO;

		printRow++;

		return printRow;
	}

	// 印合計
	public void printTotal(int printRow) throws LogicException {

		makeExcel.setValue(printRow, 6, "合計:", "C", fontStyleVo);

		makeExcel.setValue(printRow, 7, totalLastMonthOvduPrinBal, "#,##0", "R", fontStyleVo);

		makeExcel.setValue(printRow, 8, totalLastMonthOvduIntBal, "#,##0", "R", fontStyleVo);

		// 上月催收餘額 = F7 + F8
		makeExcel.setValue(printRow, 9, totalLastMonthOvduPrinBal.add(totalLastMonthOvduIntBal), "#,##0", "R", fontStyleVo);

		makeExcel.setValue(printRow, 12, totalThisMonthOvduPrinBal, "#,##0", "R", fontStyleVo);

		makeExcel.setValue(printRow, 13, totalThisMonthOvduIntBal, "#,##0", "R", fontStyleVo);

		// 本月催收餘額 = F9 + F10
		makeExcel.setValue(printRow, 14, totalThisMonthOvduPrinBal.add(totalThisMonthOvduIntBal), "#,##0", "R", fontStyleVo);

		// 本月增減-本金 = F9 - F7
		makeExcel.setValue(printRow, 10, totalThisMonthOvduPrinBal.subtract(totalLastMonthOvduPrinBal), "#,##0", "R", fontStyleVo);

		// 本月增減-利息 = F10 - F8
		makeExcel.setValue(printRow, 11, totalThisMonthOvduIntBal.subtract(totalLastMonthOvduIntBal), "#,##0", "R", fontStyleVo);
	}

}
