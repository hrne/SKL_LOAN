package com.st1.itx.trade.LM;

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
import com.st1.itx.db.service.springjpa.cm.LM044ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class LM044Report extends MakeReport {

	@Autowired
	LM044ServiceImpl lM044ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	Parse parse;

	@Override
	public void printTitle() {
	}

	BigDecimal sNorthLoanBal = BigDecimal.ZERO;// 前2月北部區放款加總
	BigDecimal sNorthOvduBal = BigDecimal.ZERO;// 前2月北部區逾期加催收款加總
	BigDecimal sCenterLoanBal = BigDecimal.ZERO;// 前2月中部區放款加總
	BigDecimal sCenterOvduBal = BigDecimal.ZERO;// 前2月中部區逾期加催收款加總
	BigDecimal sSouthLoanBal = BigDecimal.ZERO;// 前2月南部區放款加總
	BigDecimal sSouthOvduBal = BigDecimal.ZERO;// 前2月南部區逾期加催收款加總
	BigDecimal lNorthLoanBal = BigDecimal.ZERO;// 前1月北部區放款加總
	BigDecimal lNorthOvduBal = BigDecimal.ZERO;// 前1月北部區逾期加催收款加總
	BigDecimal lCenterLoanBal = BigDecimal.ZERO;// 前1月中部區放款加總
	BigDecimal lCenterOvduBal = BigDecimal.ZERO;// 前1月中部區逾期加催收款加總
	BigDecimal lSouthLoanBal = BigDecimal.ZERO;// 前1月南部區放款加總
	BigDecimal lSouthOvduBal = BigDecimal.ZERO;// 前1月南部區逾期加催收款加總
	BigDecimal loanBal = BigDecimal.ZERO;// 當月放款
	BigDecimal ovduBal = BigDecimal.ZERO;// 當月催收款
	BigDecimal colBal = BigDecimal.ZERO;// 當月逾放款
	BigDecimal tLoanBal = BigDecimal.ZERO;// 當月放款加總
	BigDecimal tOvduBal = BigDecimal.ZERO;// 當月催收款加總
	BigDecimal tColBal = BigDecimal.ZERO;// 當月逾放款加總
	BigDecimal northLoanBal = BigDecimal.ZERO;// 北部區域放款加總
	BigDecimal centerLoanBal = BigDecimal.ZERO;// 中部區域放款加總
	BigDecimal southLoanBal = BigDecimal.ZERO;// 南部區域放款加總
	BigDecimal northOvduBal = BigDecimal.ZERO;// 北部區域催收款加總
	BigDecimal centerOvduBal = BigDecimal.ZERO;// 中部區域催收款加總
	BigDecimal southOvduBal = BigDecimal.ZERO;// 南部區域催收款加總
	BigDecimal northColBal = BigDecimal.ZERO;// 北部區域逾放款加總
	BigDecimal centerColBal = BigDecimal.ZERO;// 中部區域逾放款加總
	BigDecimal southColBal = BigDecimal.ZERO;// 南部區域逾放款加總
	BigDecimal companyLoanBal = BigDecimal.ZERO;
	BigDecimal lCompanyLoanBal = BigDecimal.ZERO;
	BigDecimal sCompanyLoanBal = BigDecimal.ZERO;
	BigDecimal companyColBal = BigDecimal.ZERO;
	BigDecimal companyOvduBal = BigDecimal.ZERO;
	BigDecimal lCompanyColBal = BigDecimal.ZERO;
	BigDecimal sCompanyColBal = BigDecimal.ZERO;

	String tmp = "";
	double rate = 0.0000;
	int row = 3;

	public void exec(TitaVo titaVo) throws LogicException {

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM044", "地區_區域中心逾比及分級管理逾放比明細表",
				"LM044-地區逾放比分級管理明細表_內部控管", "LM044地區逾放比分級管理明細表_內部控管.xlsx", "10804_地區逾放比");

		int iEntdy = parse.stringToInteger(titaVo.get("ENTDY")); // YYYMMDD

		makeExcel.setSheet("10804_地區逾放比", iEntdy / 100 + "_地區逾放比");

		makeExcel.setValue(1, 2,
				iEntdy / 100 + "  地區 / 區域中心逾比及分級管理逾放比明細表                                                   密等:密");
		int thirdMonth = iEntdy / 100;
		int secondMonth = thirdMonth - 1;
		if (secondMonth % 13 == 0) {
			secondMonth -= 88;
		}
		int firstMonth = secondMonth - 1;
		if (firstMonth % 13 == 0) {
			firstMonth -= 88;
		}
		makeExcel.setValue(3, 6, firstMonth / 100 + "." + parse.IntegerToString(firstMonth % 100, 2));
		makeExcel.setValue(3, 15, firstMonth / 100 + "." + parse.IntegerToString(firstMonth % 100, 2));
		makeExcel.setValue(3, 7, secondMonth / 100 + "." + parse.IntegerToString(secondMonth % 100, 2));
		makeExcel.setValue(3, 16, secondMonth / 100 + "." + parse.IntegerToString(secondMonth % 100, 2));
		makeExcel.setValue(3, 8, thirdMonth / 100 + "." + parse.IntegerToString(thirdMonth % 100, 2));
		makeExcel.setValue(3, 17, thirdMonth / 100 + "." + parse.IntegerToString(thirdMonth % 100, 2));
		List<Map<String, String>> LM044List = null;
		try {
			LM044List = lM044ServiceImpl.findAll(titaVo);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM044ServiceImpl.testExcel error = " + errors.toString());
		}
		exportExcel(LM044List);
		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

	private void exportExcel(List<Map<String, String>> LDList) throws LogicException {
		this.info("LM044Report exportExcel");
		if (LDList == null || LDList.isEmpty()) {
			makeExcel.setValue(4, 3, "本日無資料");
		} else {
			int count = 0;
			for (Map<String, String> tLDVo : LDList) {
				row++;
				loanBal = getBigDecimal(tLDVo.get("F2"));
				makeExcel.setValue(row, 3, loanBal, "#,##0");
				tLoanBal = tLoanBal.add(loanBal);
				ovduBal = getBigDecimal(tLDVo.get("F3"));
				makeExcel.setValue(row, 4, ovduBal, "#,##0");
				tOvduBal = tOvduBal.add(ovduBal);
				colBal = getBigDecimal(tLDVo.get("F4"));
				makeExcel.setValue(row, 5, colBal, "#,##0");
				tColBal = tColBal.add(colBal);
				makeExcel.setValue(row, 6, getBigDecimal(tLDVo.get("F5")), "0.00%");
				makeExcel.setValue(row, 7, getBigDecimal(tLDVo.get("F6")), "0.00%");
				makeExcel.setValue(row, 8, getBigDecimal(tLDVo.get("F7")), "0.00%");
				makeExcel.setValue(row, 19, loanBal.add(ovduBal), "#,##0");
				northOvduBal = northOvduBal.add(getBigDecimal(tLDVo.get("F10")));
				centerOvduBal = centerOvduBal.add(getBigDecimal(tLDVo.get("F11")));
				southOvduBal = southOvduBal.add(getBigDecimal(tLDVo.get("F12")));
				northColBal = northColBal.add(getBigDecimal(tLDVo.get("F13")));
				centerColBal = centerColBal.add(getBigDecimal(tLDVo.get("F14")));
				southColBal = southColBal.add(getBigDecimal(tLDVo.get("F15")));
				sNorthOvduBal = sNorthOvduBal.add(getBigDecimal(tLDVo.get("F16")));
				sCenterOvduBal = sCenterOvduBal.add(getBigDecimal(tLDVo.get("F17")));
				sSouthOvduBal = sSouthOvduBal.add(getBigDecimal(tLDVo.get("F18")));
				lNorthOvduBal = lNorthOvduBal.add(getBigDecimal(tLDVo.get("F19")));
				lCenterOvduBal = lCenterOvduBal.add(getBigDecimal(tLDVo.get("F20")));
				lSouthOvduBal = lSouthOvduBal.add(getBigDecimal(tLDVo.get("F21")));
				companyOvduBal = companyOvduBal.add(getBigDecimal(tLDVo.get("F22")));
				companyColBal = companyColBal.add(getBigDecimal(tLDVo.get("F23")));
				sCompanyColBal = sCompanyColBal.add(getBigDecimal(tLDVo.get("F24")));
				lCompanyColBal = lCompanyColBal.add(getBigDecimal(tLDVo.get("F25")));
				if (count <= 6) {
					northLoanBal = northLoanBal.add(loanBal);
					sNorthLoanBal = sNorthLoanBal.add(getBigDecimal(tLDVo.get("F8")));
					lNorthLoanBal = lNorthLoanBal.add(getBigDecimal(tLDVo.get("F9")));
				} else if (count <= 10 && count > 6) {
					centerLoanBal = centerLoanBal.add(loanBal);
					sCenterLoanBal = sCenterLoanBal.add(getBigDecimal(tLDVo.get("F8")));
					lCenterLoanBal = lCenterLoanBal.add(getBigDecimal(tLDVo.get("F9")));
				} else if (count <= 15 && count > 10) {
					southLoanBal = southLoanBal.add(loanBal);
					sSouthLoanBal = sSouthLoanBal.add(getBigDecimal(tLDVo.get("F8")));
					lSouthLoanBal = lSouthLoanBal.add(getBigDecimal(tLDVo.get("F9")));
				} else if (count <= 15 && count > 10) {
					southLoanBal = southLoanBal.add(loanBal);
					sSouthLoanBal = sSouthLoanBal.add(getBigDecimal(tLDVo.get("F8")));
					lSouthLoanBal = lSouthLoanBal.add(getBigDecimal(tLDVo.get("F9")));
				} else if (count >= 20 && count <= 21) {
					companyLoanBal = companyLoanBal.add(loanBal);
					sCompanyLoanBal = sCompanyLoanBal.add(getBigDecimal(tLDVo.get("F8")));
					lCompanyLoanBal = lCompanyLoanBal.add(getBigDecimal(tLDVo.get("F9")));
				}
				count++;
			}
			makeExcel.setValue(row + 1, 3, tLoanBal);
			makeExcel.setValue(row + 1, 4, tOvduBal);
			makeExcel.setValue(row + 1, 5, tColBal);
			makeExcel.setValue(10, 12, northLoanBal, "#,##0");
			makeExcel.setValue(10, 13, northOvduBal, "#,##0");
			makeExcel.setValue(10, 14, northColBal, "#,##0");
			makeExcel.setValue(10, 15, computeDivide(sNorthOvduBal, sNorthLoanBal, 4), "0.00%");
			makeExcel.setValue(10, 16, computeDivide(lNorthOvduBal, lNorthLoanBal, 4), "0.00%");
			makeExcel.setValue(10, 17, computeDivide(northOvduBal.add(northColBal), northOvduBal.add(northLoanBal), 4), "0.00%");
			makeExcel.setValue(14, 12, centerLoanBal, "#,##0");
			makeExcel.setValue(14, 13, centerOvduBal, "#,##0");
			makeExcel.setValue(14, 14, centerColBal, "#,##0");
			makeExcel.setValue(14, 15, computeDivide(sCenterLoanBal, sCenterLoanBal, 4), "0.00%");
			makeExcel.setValue(14, 16, computeDivide(lCenterLoanBal, lCenterLoanBal, 4), "0.00%");
			makeExcel.setValue(14, 17, computeDivide(centerOvduBal.add(centerColBal), centerOvduBal.add(centerLoanBal), 4), "0.00%");
			makeExcel.setValue(19, 12, southLoanBal, "#,##0");
			makeExcel.setValue(19, 13, southOvduBal, "#,##0");
			makeExcel.setValue(19, 14, southColBal, "#,##0");
			makeExcel.setValue(19, 15, computeDivide(sSouthLoanBal, sSouthLoanBal, 4), "0.00%");
			makeExcel.setValue(19, 16, computeDivide(lSouthLoanBal, lSouthLoanBal, 4), "0.00%");
			makeExcel.setValue(19, 17, computeDivide(southOvduBal.add(southColBal), southOvduBal.add(southLoanBal), 4), "0.00%");

			makeExcel.setValue(25, 12, companyLoanBal, "#,##0");
			makeExcel.setValue(25, 13, companyOvduBal, "#,##0");
			makeExcel.setValue(25, 14, companyColBal, "#,##0");
			makeExcel.setValue(25, 15, computeDivide(sCompanyLoanBal, sCompanyLoanBal, 4), "0.00%");
			makeExcel.setValue(25, 16, computeDivide(lCompanyLoanBal, lCompanyLoanBal, 4), "0.00%");
			makeExcel.setValue(25, 17, computeDivide(companyOvduBal.add(companyColBal), companyOvduBal.add(companyLoanBal), 4), "0.00%");

		}
	}
}
