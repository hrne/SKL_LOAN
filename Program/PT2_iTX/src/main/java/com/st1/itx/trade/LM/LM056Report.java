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

	public int yearMon = 0;

	/**
	 * 執行報表輸出
	 * 
	 * @param titaVo
	 * @param yearMonth 西元年月
	 * 
	 */
	public void exec(TitaVo titaVo, int yearMonth) throws LogicException {

		this.info("LM056Report exec");

		yearMon = ((yearMonth / 100) - 1911) * 100 + (yearMonth % 100);

		List<Map<String, String>> findList = new ArrayList<>();
		List<Map<String, String>> findList2 = new ArrayList<>();
		List<Map<String, String>> findList3 = new ArrayList<>();

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM056", "表14-1、14-2會計部申報表",
				"LM056-表14-1、14-2_會計部申報表", "LM056_底稿_表14-1、14-2_會計部申報表.xlsx", "YYYMM");

		try {

			// YYYMM工作表
			findList = lM056ServiceImpl.findAll(titaVo, yearMonth, "Y");
			// 14-1工作表
			findList2 = lM056ServiceImpl.findAll(titaVo, yearMonth, "N");
			// 14-2工作表
			findList3 = lM056ServiceImpl.findAll2(titaVo, yearMonth);

		} catch (Exception e) {

			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM056ServiceImpl.findAll error = " + errors.toString());

		}

		reportExcel(findList);
		reportExcel14_1(findList2);
		reportExcel14_2(findList3);

		makeExcel.close();

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

				// 身分證
				makeExcel.setValue(row, 3, lM056Vo.get("F2"));

				tempAmt = lM056Vo.get("F3").equals("0") ? BigDecimal.ZERO : new BigDecimal(lM056Vo.get("F3"));

				// 放款額逾
				makeExcel.setValue(row, 4, tempAmt, "#,##0");

				// 種類
				makeExcel.setValue(row, 5, lM056Vo.get("F4"), "C");

				// 利害關係人
				makeExcel.setValue(row, 6, lM056Vo.get("F4"), "L");
			}

			makeExcel.formulaCaculate(1, 3);
			makeExcel.formulaCaculate(1, 4);

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

				tempAmt = lM056Vo.get("F3").equals("0") ? BigDecimal.ZERO : new BigDecimal(lM056Vo.get("F3"));

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
		BigDecimal amt = BigDecimal.ZERO;

		// 參考 LM057的表14-5
		for (Map<String, String> r : listData) {

			col = enToNumber(r.get("F0").toString().substring(0, 1));
			// 排除N欄以後不設值 略過
			if (col > 12) {
				break;
			}
			row = Integer.valueOf(r.get("F0").toString().substring(1, 3));
			amt = getBigDecimal(r.get("F1").toString());


			makeExcel.setValue(row, col, amt, "#,##0", "R");

		}

		// 重整
		// D42 甲類逾期放款比率%(含壽險保單質押放款)
		makeExcel.formulaCaculate(42, 4);
		// D43 乙類逾期放款比率%(含壽險保單質押放款)
		makeExcel.formulaCaculate(43, 4);
		// D44 逾期放款比率%(含壽險保單質押放款)
		makeExcel.formulaCaculate(44, 4);
		
		makeExcel.formulaCaculate(45, 4);
		makeExcel.formulaCaculate(46, 4);
		makeExcel.formulaCaculate(47, 4);
	}
	/**
	 * 英文轉數字
	 * 
	 * @param colText 英文字母
	 */
	private int enToNumber(String colText) {
		String colTxt = "";
		int col = 0;

		colTxt = colText.toUpperCase();

		char[] tokens = colTxt.toCharArray();

		for (char token : tokens) {
			col = Integer.valueOf(token) - 64;
		}

		return col;

	}
}
