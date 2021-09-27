package com.st1.itx.trade.LM;

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
import com.st1.itx.db.service.springjpa.cm.LM056ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LM056Report extends MakeReport {

	@Autowired
	LM056ServiceImpl lM056ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	public String dateF = "";
	public int yearMon = 0;

	public void exec(TitaVo titaVo) throws LogicException {

		this.info("LM056Report exec");

		int iEntdy = Integer.valueOf(titaVo.get("ENTDY")) + 19110000;
		int iYear = (Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 10000;
		int iMonth = ((Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 100) % 100;

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		// 當日
		int nowDate = Integer.valueOf(iEntdy);
		Calendar calMonthDate = Calendar.getInstance();
		// 設當年月底日 0是月底
		calMonthDate.set(iYear, iMonth, 0);

		int thisMonthEndDate = Integer.valueOf(dateFormat.format(calMonthDate.getTime()));

		boolean isMonthZero = iMonth - 1 == 0;

		if (nowDate < thisMonthEndDate) {
			iYear = isMonthZero ? (iYear - 1) : iYear;
			iMonth = isMonthZero ? 12 : iMonth - 1;
		}

		int iDay = iEntdy % 100;

		String date = iYear + "/" + iMonth + "/" + iDay;

		dateF = date;

		yearMon = (Integer.valueOf(titaVo.get("ENTDY")) - 19110000) / 100;

		List<Map<String, String>> findList = new ArrayList<>();
		List<Map<String, String>> findList2 = new ArrayList<>();
		List<Map<String, String>> findList3 = new ArrayList<>();

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM056", "表14-1、14-2會計部申報表",
				"LM056-表14-1、14-2_會計部申報表", "LM056_底稿_表14-1、14-2_會計部申報表.xlsx", "YYYMM");

		try {

			// YYYMM工作表
			findList = lM056ServiceImpl.findAll(titaVo, "Y");
			// 14-1工作表
			findList2 = lM056ServiceImpl.findAll(titaVo, "N");
			// 14-2工作表
			findList3 = lM056ServiceImpl.findAll2(titaVo);

		} catch (Exception e) {

			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM056ServiceImpl.findAll error = " + errors.toString());

		}

		reportExcel(findList);
		reportExcel14_1(findList2);
		reportExcel14_2(findList3);

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

	private void reportExcel(List<Map<String, String>> listData) throws LogicException {

		this.info("LM056report.reportExcel");

		makeExcel.setSheet("YYYMM", yearMon + "");

		BigDecimal tempAmt = BigDecimal.ZERO;

		int row = 1;

		if (listData.size() > 0) {

			for (Map<String, String> lM056Vo : listData) {

				row++;

				// 戶號
				makeExcel.setValue(row, 1, lM056Vo.get("F0"));

				// 戶名
				makeExcel.setValue(row, 2, lM056Vo.get("F1"), "L");

				tempAmt = new BigDecimal(lM056Vo.get("F2"));

				// 放款額逾
				makeExcel.setValue(row, 3, tempAmt, "#,##0");

				// 種類
				makeExcel.setValue(row, 4, lM056Vo.get("F3"), "C");

				// 利害關係人
				makeExcel.setValue(row, 5, lM056Vo.get("F4"), "L");
			}

		} else {

			makeExcel.setValue(2, 1, "本日無資料");

		}
	}

	private void reportExcel14_1(List<Map<String, String>> listData) throws LogicException {

		this.info("LM056report.reportExcel14_1");

		makeExcel.setSheet("表14-1");

		int row = 5;

		BigDecimal tempAmt = BigDecimal.ZERO;

		if (listData.size() > 0) {

			for (Map<String, String> lM056Vo : listData) {

				row++;

				tempAmt = new BigDecimal(lM056Vo.get("F2"));

				// 放款額逾
				makeExcel.setValue(row, 19, tempAmt, "#,##0");

				// 繳還情形??
				makeExcel.setValue(row, 23, "A", "C");
			}

		}
	}

	private void reportExcel14_2(List<Map<String, String>> listData) throws LogicException {

		this.info("LM056report.reportExcel14_2");

		makeExcel.setSheet("表14-2");

		int row = 0;
		int col = 0;
		BigDecimal tempAmt = BigDecimal.ZERO;
		BigDecimal tempTotal = BigDecimal.ZERO;

		// 參考 LM057的表14-5
		for (Map<String, String> lM056Vo : listData) {

			// 金額
			tempAmt = new BigDecimal(lM056Vo.get("F1"));

			// H37 放款總計
			// D40 甲類逾期放款金額
			// D41 乙類逾期放款金額
			// D44 逾期放款比率%
			if (lM056Vo.get("F0") == "B") {

				row = 40;
				col = 4;

			} else if (lM056Vo.get("F0") == "C") {

				row = 41;
				col = 4;

			} else if (lM056Vo.get("F0") == "TOTAL") {
				row = 37;
				col = 8;

				tempTotal = new BigDecimal(lM056Vo.get("F1"));

			} else if (lM056Vo.get("F0") == "NTOTAL") {
				row = 44;
				col = 4;

				tempAmt = tempAmt.divide(tempTotal).setScale(4);

			}

			if (lM056Vo.get("F0") == "NTOTAL") {
				makeExcel.setValue(row, col, tempAmt, "R");
			}

			makeExcel.setValue(row, col, tempAmt, "#,##0", "R");

		}
		// 重整
		// D42 甲類逾期放款比率%
		makeExcel.formulaCaculate(42, 4);
		// D43 乙類逾期放款比率%
		makeExcel.formulaCaculate(43, 4);

	}

}
