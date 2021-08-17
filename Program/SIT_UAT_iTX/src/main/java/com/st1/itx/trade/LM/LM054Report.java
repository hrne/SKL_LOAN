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
import com.st1.itx.db.service.springjpa.cm.LM054ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LM054Report extends MakeReport {

	@Autowired
	LM054ServiceImpl LM054ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	String fdnm = "";
	int row = 1;
	BigDecimal tot = new BigDecimal("0");

	public void exec(TitaVo titaVo) throws LogicException {

		List<Map<String, String>> ias34List = new ArrayList<>();
		List<Map<String, String>> fnAllList = new ArrayList<>();

		this.info("LM054Report exec");

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM054", "A041重要放款餘額明細表(大額、逾期、催收、國外)", "LM054-A041重要放款餘額明細表(大額、逾期、催收、國外)_手搞", "LM054_底稿_A041重要放款餘額明細表(大額、逾期、催收、國外)_手搞.xlsx",
				"LNM34AP");
		try {
			ias34List = LM054ServiceImpl.ias34Ap(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM054ServiceImpl.ias34Ap error = " + errors.toString());
		}

		if (ias34List.size() > 0) {
			tot = new BigDecimal("0");
			row = 1;
			for (Map<String, String> tLDVo : ias34List) {
				exportExcel_1(tLDVo);
			}
			makeExcel.setValue(1, 14, tot, "#,##0");
		} else {
			makeExcel.setValue(2, 1, "本日無資料");
		}

		makeExcel.setSheet("10809", titaVo.get("ENTDY").substring(1, 6));

		try {
			fnAllList = LM054ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM054ServiceImpl.findAll error = " + errors.toString());
		}

		if (fnAllList.size() > 0) {
			tot = new BigDecimal("0");
			row = 1;
			for (Map<String, String> tLDVo : fnAllList) {
				exportExcel_2(tLDVo);
			}
			this.info("row = " + (row - 1));
			makeExcel.setValue(1, 3, row - 1, "#,##0.0");
			makeExcel.setValue(1, 4, tot, "#,##0.0");
		} else {
			makeExcel.setValue(2, 1, "本日無資料");
		}

		int yearMonth = (Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 100;
		makeExcel.setSheet("A041重要放款餘額明細表(大額、逾期、催收、國外)");
		makeExcel.setValue(2, 3, yearMonth);

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

	private void exportExcel_1(Map<String, String> tLDVo) throws LogicException {
		if (tLDVo.size() == 0) {
			makeExcel.setValue(2, 1, "本日無資料");
		}
		row++;
		makeExcel.setValue(row, 1, tLDVo.get("F0") + tLDVo.get("F2") + tLDVo.get("F4"));

		for (int i = 0; i < tLDVo.size(); i++) {
			fdnm = "F" + String.valueOf(i);
			switch (i) {
			case 1:
			case 3:
			case 5:
			case 29:
			case 30:
			case 31:
			case 32:
			case 33:
			case 35:
			case 39:
			case 40:
			case 43:
				// 字串左靠
				makeExcel.setValue(row, i + 2, tLDVo.get(fdnm));
				break;
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
			case 16:
				// 金額
				if (tLDVo.get(fdnm).equals("")) {
					makeExcel.setValue(row, i + 2, 0, "#,##0");
				} else {
					makeExcel.setValue(row, i + 2, Float.valueOf(tLDVo.get(fdnm)), "#,##0");
					if (i == 14) {
						tot = tot.add(tLDVo.get(fdnm) == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get(fdnm)));
					}
				}
				break;
			case 17:
				// 利率
				if (tLDVo.get(fdnm).equals("")) {
					makeExcel.setValue(row, i + 2, 0);
				} else {
					makeExcel.setValue(row, i + 2, Float.valueOf(tLDVo.get(fdnm)), "0.0000");
				}
				break;
			case 24:
				// 利率
				if (tLDVo.get(fdnm).equals("")) {
					makeExcel.setValue(row, i + 2, 0);
				} else {
					makeExcel.setValue(row, i + 2, Float.valueOf(tLDVo.get(fdnm)));
				}
				break;
			case 41:
				break;
			default:
				// 戶號(數字右靠)
				if (tLDVo.get(fdnm).equals("")) {
					makeExcel.setValue(row, i + 2, 0);
				} else {
					makeExcel.setValue(row, i + 2, tLDVo.get(fdnm));
				}
				break;
			}
		}
	}

	private void exportExcel_2(Map<String, String> tLDVo) throws LogicException {
		if (tLDVo.size() == 0) {
			makeExcel.setValue(2, 1, "本日無資料");
		}
		row++;
		for (int i = 0; i < tLDVo.size(); i++) {
			fdnm = "F" + String.valueOf(i);
			switch (i) {
			case 0:
				// 戶號(數字右靠)
				if (tLDVo.get(fdnm).equals("")) {
					makeExcel.setValue(row, i + 1, 0);
				} else {
					makeExcel.setValue(row, i + 1, Integer.valueOf(tLDVo.get(fdnm)));
				}
				break;
			case 3:
				// 金額
				if (tLDVo.get(fdnm).equals("")) {
					makeExcel.setValue(row, i + 1, 0, "#,##0.00");
				} else {
					makeExcel.setValue(row, i + 1, Float.valueOf(tLDVo.get(fdnm)), "#,##0.00");
					tot = tot.add(tLDVo.get(fdnm) == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get(fdnm)));
				}
				break;
			default:
				// 字串左靠
				makeExcel.setValue(row, i + 1, tLDVo.get(fdnm));
				break;
			}
		}
	}
}
