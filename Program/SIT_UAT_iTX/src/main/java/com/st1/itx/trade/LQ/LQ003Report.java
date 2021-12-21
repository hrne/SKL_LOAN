package com.st1.itx.trade.LQ;

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
import com.st1.itx.db.service.springjpa.cm.LQ003ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")
public class LQ003Report extends MakeReport {

	@Autowired
	LQ003ServiceImpl LQ003ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	public void exec(TitaVo titaVo) throws LogicException {

		exportExcel(titaVo);

	}

	private void exportExcel(TitaVo titaVo) throws LogicException {
		this.info("===========in exportExcel");
		int iEntdy = Integer.valueOf(titaVo.get("ENTDY")) + 19110000;
		int iYear = (Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 10000;
		int iMonth = ((Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 100) % 100;

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		// 當日(int)
		int nowDate = Integer.valueOf(iEntdy);
		Calendar calMonthLastDate = Calendar.getInstance();
		// 設當年月底日
		calMonthLastDate.set(iYear, iMonth, 0);

		int monthLastDate = Integer.valueOf(dateFormat.format(calMonthLastDate.getTime()));

		boolean isMonthZero = iMonth - 1 == 0;

		if (nowDate < monthLastDate) {
			iYear = isMonthZero ? (iYear - 1) : iYear;
			iMonth = isMonthZero ? 12 : iMonth - 1;
		}

		// 年
		String rocYear = String.valueOf(iYear - 1911);
		// 月
		String rocMon = String.format("%02d", iMonth);

		// 上一季
		String q = "";

		int inputYearMonth1 = 0;
		int inputYearMonth2 = 0;

		switch (rocMon) {
		case "01":
		case "02":
		case "03":
			inputYearMonth1 = ((iYear - 1) * 100) + 12;
			inputYearMonth2 = (iYear * 100) + 3;
			q = String.valueOf(Integer.valueOf(rocYear) - 1) + "Q4";
			break;
		case "04":
		case "05":
		case "06":
			inputYearMonth1 = (iYear * 100) + 3;
			inputYearMonth2 = (iYear * 100) + 6;
			q = rocYear + "Q1";
			break;
		case "07":
		case "08":
		case "09":
			inputYearMonth1 = (iYear * 100) + 6;
			inputYearMonth2 = (iYear * 100) + 9;
			q = rocYear + "Q2";
			break;
		case "10":
		case "11":
		case "12":
			inputYearMonth1 = (iYear * 100) + 9;
			inputYearMonth2 = (iYear * 100) + 12;
			q = rocYear + "Q3";
			break;
		default:
			break;
		}

		List<Map<String, String>> findList = new ArrayList<>();

		List<Map<String, String>> findList2 = new ArrayList<>();

		try {
			// 當季
			findList = LQ003ServiceImpl.findAll(titaVo, inputYearMonth2);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LQP003ServiceImpl findAll error = " + errors.toString());
		}
		try {
			// 上季
			findList2 = LQ003ServiceImpl.findAll(titaVo, inputYearMonth1);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LQP003ServiceImpl findAll error = " + errors.toString());
		}

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LQ003", "住宅違約統計季報_服務課申報表", "LQ003住宅違約統計季報" + q, "LQ003_底稿_放款管理課_住宅違約統計季報_服務課申報表.xlsx", "填報");

		makeExcel.setValue(1, 9, q, "C");

		// 設欄寬
		makeExcel.setWidth(3, 15);
		makeExcel.setWidth(4, 15);
		makeExcel.setWidth(5, 18);
		makeExcel.setWidth(6, 17);
		makeExcel.setWidth(7, 10);
		// 設列高
		makeExcel.setHeight(1, 50);

		if (findList != null && findList.size() > 0 && findList2 != null && findList2.size() > 0) {
			int rowCursor = 2;

			BigDecimal totalF2 = BigDecimal.ZERO;
			BigDecimal totalF3 = BigDecimal.ZERO;
			BigDecimal totalF4 = BigDecimal.ZERO;
			BigDecimal totalF5 = BigDecimal.ZERO;
			BigDecimal totalLF3 = BigDecimal.ZERO;

			int loopPointer = 0;
			int loopMax = findList.size() - 1;
			if (loopMax > (findList2.size() - 1)) {
				loopMax = findList2.size() - 1;
			}

			this.info("loopPointer = " + loopPointer);
			this.info("loopMax = " + loopMax);

			for (; loopPointer <= loopMax; loopPointer++) {

				Map<String, String> tLDVo = findList.get(loopPointer);
				Map<String, String> tLDVo2 = findList2.get(loopPointer);

				// 縣市名稱
				int f0 = tLDVo.get("F0") == null || tLDVo.get("F0").length() == 0 ? 0 : Integer.parseInt(tLDVo.get("F0"));
				makeExcel.setValue(rowCursor, 1, f0, "C");

				// 縣市代號
				makeExcel.setValue(rowCursor, 2, tLDVo.get("F1"), "C");

				// 季末貸款總額
				BigDecimal f2 = getBigDecimal(tLDVo.get("F2"));
				makeExcel.setValue(rowCursor, 3, f2, "#,##0", "R");
				totalF2 = totalF2.add(f2);

				// 季末逾放金額
				BigDecimal f3 = getBigDecimal(tLDVo.get("F3"));
				makeExcel.setValue(rowCursor, 4, f3, "#,##0.00", "R");
				totalF3 = totalF3.add(f3);

				// 催收
				BigDecimal f4 = getBigDecimal(tLDVo.get("F4"));
				makeExcel.setValue(rowCursor, 6, f4, "#,##0.00", "R");
				totalF4 = totalF4.add(f4);

				// 逾放筆數
				BigDecimal f5 = getBigDecimal(tLDVo.get("F5"));
				makeExcel.setValue(rowCursor, 7, f5, "#,##0", "R");
				totalF5 = totalF5.add(f5);

				// 上季末逾放金額
				BigDecimal lf3 = getBigDecimal(tLDVo2.get("F3"));
				makeExcel.setValue(rowCursor, 9, lf3, "#,##0.00", "R");
				totalLF3 = totalLF3.add(lf3);

				// 本季末購置住宅貸款逾放金額變化
				BigDecimal diff = f3.subtract(lf3);
				makeExcel.setValue(rowCursor, 5, diff, "#,##0", "R");

				rowCursor++;
			}

			// 印合計
			makeExcel.setValue(rowCursor, 1, "合計", "C");
			makeExcel.setValue(rowCursor, 3, totalF2, "#,##0", "R");
			makeExcel.setValue(rowCursor, 4, totalF3, "#,##0.00", "R");
			makeExcel.setValue(rowCursor, 6, totalF4, "#,##0.00", "R");
			makeExcel.setValue(rowCursor, 7, totalF5, "#,##0", "R");
			makeExcel.setValue(rowCursor, 9, totalLF3, "#,##0.00", "R");

		} else {
			makeExcel.setValue(2, 2, "本日無資料");
		}

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}
}