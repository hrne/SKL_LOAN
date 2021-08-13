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

@Component
@Scope("prototype")

public class LM044Report extends MakeReport {

	@Autowired
	LM044ServiceImpl lM044ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printTitle() {
	}

	BigDecimal sNorthLoanBal = new BigDecimal("0");// 前2月北部區放款加總
	BigDecimal sNorthOvduBal = new BigDecimal("0");// 前2月北部區逾期加催收款加總
	BigDecimal sCenterLoanBal = new BigDecimal("0");// 前2月中部區放款加總
	BigDecimal sCenterOvduBal = new BigDecimal("0");// 前2月中部區逾期加催收款加總
	BigDecimal sSouthLoanBal = new BigDecimal("0");// 前2月南部區放款加總
	BigDecimal sSouthOvduBal = new BigDecimal("0");// 前2月南部區逾期加催收款加總
	BigDecimal lNorthLoanBal = new BigDecimal("0");// 前1月北部區放款加總
	BigDecimal lNorthOvduBal = new BigDecimal("0");// 前1月北部區逾期加催收款加總
	BigDecimal lCenterLoanBal = new BigDecimal("0");// 前1月中部區放款加總
	BigDecimal lCenterOvduBal = new BigDecimal("0");// 前1月中部區逾期加催收款加總
	BigDecimal lSouthLoanBal = new BigDecimal("0");// 前1月南部區放款加總
	BigDecimal lSouthOvduBal = new BigDecimal("0");// 前1月南部區逾期加催收款加總
	BigDecimal loanBal = new BigDecimal("0");// 當月放款
	BigDecimal ovduBal = new BigDecimal("0");// 當月催收款
	BigDecimal colBal = new BigDecimal("0");// 當月逾放款
	BigDecimal tLoanBal = new BigDecimal("0");// 當月放款加總
	BigDecimal tOvduBal = new BigDecimal("0");// 當月催收款加總
	BigDecimal tColBal = new BigDecimal("0");// 當月逾放款加總
	BigDecimal northLoanBal = new BigDecimal("0");// 北部區域放款加總
	BigDecimal centerLoanBal = new BigDecimal("0");// 中部區域放款加總
	BigDecimal southLoanBal = new BigDecimal("0");// 南部區域放款加總
	BigDecimal northOvduBal = new BigDecimal("0");// 北部區域催收款加總
	BigDecimal centerOvduBal = new BigDecimal("0");// 中部區域催收款加總
	BigDecimal southOvduBal = new BigDecimal("0");// 南部區域催收款加總
	BigDecimal northColBal = new BigDecimal("0");// 北部區域逾放款加總
	BigDecimal centerColBal = new BigDecimal("0");// 中部區域逾放款加總
	BigDecimal southColBal = new BigDecimal("0");// 南部區域逾放款加總
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

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM044", "地區_區域中心逾比及分級管理逾放比明細表", "LM044-地區逾放比分級管理明細表_內部控管", "LM044地區逾放比分級管理明細表_內部控管.xlsx", "10804_地區逾放比");

		String iENTDY = titaVo.get("ENTDY").substring(1, 8);

		makeExcel.setSheet("10804_地區逾放比", iENTDY.substring(0, 5) + "_地區逾放比");

		makeExcel.setValue(1, 2, iENTDY.substring(0, 5) + "  地區 / 區域中心逾比及分級管理逾放比明細表                                                   密等:密");
		int thirdMonth = Integer.parseInt(iENTDY.substring(0, 5));
		int secondMonth = thirdMonth - 1;
		if (secondMonth % 13 == 0) {
			secondMonth -= 88;
		}
		int firstMonth = secondMonth - 1;
		if (firstMonth % 13 == 0) {
			firstMonth -= 88;
		}
		makeExcel.setValue(3, 6, String.valueOf(firstMonth).substring(0, 3) + "." + String.valueOf(firstMonth).substring(3, 5));
		makeExcel.setValue(3, 15, String.valueOf(firstMonth).substring(0, 3) + "." + String.valueOf(firstMonth).substring(3, 5));
		makeExcel.setValue(3, 7, String.valueOf(secondMonth).substring(0, 3) + "." + String.valueOf(secondMonth).substring(3, 5));
		makeExcel.setValue(3, 16, String.valueOf(secondMonth).substring(0, 3) + "." + String.valueOf(secondMonth).substring(3, 5));
		makeExcel.setValue(3, 8, String.valueOf(thirdMonth).substring(0, 3) + "." + String.valueOf(thirdMonth).substring(3, 5));
		makeExcel.setValue(3, 17, String.valueOf(thirdMonth).substring(0, 3) + "." + String.valueOf(thirdMonth).substring(3, 5));
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
		if (LDList.size() == 0) {
			makeExcel.setValue(4, 3, "本日無資料");
		}
		int count = 0;
		for (Map<String, String> tLDVo : LDList) {
			row++;
			loanBal = tLDVo.get("F2") == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F2"));
			makeExcel.setValue(row, 3, loanBal, "#,##0");
			tLoanBal = tLoanBal.add(loanBal);
			ovduBal = tLDVo.get("F3") == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F3"));
			makeExcel.setValue(row, 4, ovduBal, "#,##0");
			tOvduBal = tOvduBal.add(ovduBal);
			colBal = tLDVo.get("F4") == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F4"));
			makeExcel.setValue(row, 5, colBal, "#,##0");
			tColBal = tColBal.add(colBal);
			makeExcel.setValue(row, 6, tLDVo.get("F5") == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F5")), "0.00%");
			makeExcel.setValue(row, 7, tLDVo.get("F6") == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F6")), "0.00%");
			makeExcel.setValue(row, 8, tLDVo.get("F7") == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F7")), "0.00%");
			makeExcel.setValue(row, 19, loanBal.add(ovduBal), "#,##0");
			northOvduBal = northOvduBal.add(tLDVo.get("F10") == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F10")));
			centerOvduBal = centerOvduBal.add(tLDVo.get("F11") == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F11")));
			southOvduBal = southOvduBal.add(tLDVo.get("F12") == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F12")));
			northColBal = northColBal.add(tLDVo.get("F13") == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F13")));
			centerColBal = centerColBal.add(tLDVo.get("F14") == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F14")));
			southColBal = southColBal.add(tLDVo.get("F15") == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F15")));
			sNorthOvduBal = sNorthOvduBal.add(tLDVo.get("F16") == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F16")));
			sCenterOvduBal = sCenterOvduBal.add(tLDVo.get("F17") == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F17")));
			sSouthOvduBal = sSouthOvduBal.add(tLDVo.get("F18") == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F18")));
			lNorthOvduBal = lNorthOvduBal.add(tLDVo.get("F19") == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F19")));
			lCenterOvduBal = lCenterOvduBal.add(tLDVo.get("F20") == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F20")));
			lSouthOvduBal = lSouthOvduBal.add(tLDVo.get("F21") == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F21")));
			companyOvduBal = companyOvduBal.add(tLDVo.get("F22") == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F22")));
			companyColBal = companyColBal.add(tLDVo.get("F23") == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F23")));
			sCompanyColBal = sCompanyColBal.add(tLDVo.get("F24") == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F24")));
			lCompanyColBal = lCompanyColBal.add(tLDVo.get("F25") == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F25")));
			if (count <= 6) {
				northLoanBal = northLoanBal.add(loanBal);
				sNorthLoanBal = sNorthLoanBal.add(tLDVo.get("F8") == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F8")));
				lNorthLoanBal = lNorthLoanBal.add(tLDVo.get("F9") == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F9")));
			} else if (count <= 10 && count > 6) {
				centerLoanBal = centerLoanBal.add(loanBal);
				sCenterLoanBal = sCenterLoanBal.add(tLDVo.get("F8") == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F8")));
				lCenterLoanBal = lCenterLoanBal.add(tLDVo.get("F9") == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F9")));
			} else if (count <= 15 && count > 10) {
				southLoanBal = southLoanBal.add(loanBal);
				sSouthLoanBal = sSouthLoanBal.add(tLDVo.get("F8") == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F8")));
				lSouthLoanBal = lSouthLoanBal.add(tLDVo.get("F9") == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F9")));
			} else if (count <= 15 && count > 10) {
				southLoanBal = southLoanBal.add(loanBal);
				sSouthLoanBal = sSouthLoanBal.add(tLDVo.get("F8") == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F8")));
				lSouthLoanBal = lSouthLoanBal.add(tLDVo.get("F9") == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F9")));
			} else if (count >= 20 && count <= 21) {
				companyLoanBal = companyLoanBal.add(loanBal);
				sCompanyLoanBal = sCompanyLoanBal.add(tLDVo.get("F8") == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F8")));
				lCompanyLoanBal = lCompanyLoanBal.add(tLDVo.get("F9") == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F9")));
			}
			count++;
		}
		makeExcel.setValue(row + 1, 3, tLoanBal);
		makeExcel.setValue(row + 1, 4, tOvduBal);
		makeExcel.setValue(row + 1, 5, tColBal);
		makeExcel.setValue(10, 12, northLoanBal, "#,##0");
		makeExcel.setValue(10, 13, northOvduBal, "#,##0");
		makeExcel.setValue(10, 14, northColBal, "#,##0");
		if (sNorthLoanBal.compareTo(BigDecimal.ZERO) == 1) {
			makeExcel.setValue(10, 15, sNorthOvduBal.divide(sNorthLoanBal, 4, 4), "0.00%");
		} else {
			makeExcel.setValue(10, 15, BigDecimal.ZERO, "0.00%");
		}
		if (lNorthLoanBal.compareTo(BigDecimal.ZERO) == 1) {
			makeExcel.setValue(10, 16, lNorthOvduBal.divide(lNorthLoanBal, 4, 4), "0.00%");
		} else {
			makeExcel.setValue(10, 16, BigDecimal.ZERO, "0.00%");
		}
		if (northLoanBal.add(northOvduBal).compareTo(BigDecimal.ZERO) == 1) {
			makeExcel.setValue(10, 17, northOvduBal.add(northColBal).divide(northOvduBal.add(northLoanBal), 4, 4), "0.00%");
		} else {
			makeExcel.setValue(10, 17, BigDecimal.ZERO, "0.00%");
		}
		makeExcel.setValue(14, 12, centerLoanBal, "#,##0");
		makeExcel.setValue(14, 13, centerOvduBal, "#,##0");
		makeExcel.setValue(14, 14, centerColBal, "#,##0");
		if (sCenterLoanBal.compareTo(BigDecimal.ZERO) == 1) {
			makeExcel.setValue(14, 15, sCenterLoanBal.divide(sCenterLoanBal, 4, 4), "0.00%");
		} else {
			makeExcel.setValue(14, 15, BigDecimal.ZERO, "0.00%");
		}
		if (lCenterLoanBal.compareTo(BigDecimal.ZERO) == 1) {
			makeExcel.setValue(14, 16, lCenterLoanBal.divide(lCenterLoanBal, 4, 4), "0.00%");
		} else {
			makeExcel.setValue(14, 16, BigDecimal.ZERO, "0.00%");
		}
		if (centerLoanBal.add(centerOvduBal).compareTo(BigDecimal.ZERO) == 1) {
			makeExcel.setValue(14, 17, centerOvduBal.add(centerColBal).divide(centerOvduBal.add(centerLoanBal), 4, 4), "0.00%");
		} else {
			makeExcel.setValue(14, 17, BigDecimal.ZERO, "0.00%");
		}
		makeExcel.setValue(19, 12, southLoanBal, "#,##0");
		makeExcel.setValue(19, 13, southOvduBal, "#,##0");
		makeExcel.setValue(19, 14, southColBal, "#,##0");
		if (sSouthLoanBal.compareTo(BigDecimal.ZERO) == 1) {
			makeExcel.setValue(19, 15, sSouthLoanBal.divide(sSouthLoanBal, 4, 4), "0.00%");
		} else {
			makeExcel.setValue(19, 15, BigDecimal.ZERO, "0.00%");
		}
		if (lSouthLoanBal.compareTo(BigDecimal.ZERO) == 1) {
			makeExcel.setValue(19, 16, lSouthLoanBal.divide(lSouthLoanBal, 4, 4), "0.00%");
		} else {
			makeExcel.setValue(19, 16, BigDecimal.ZERO, "0.00%");
		}
		if (southLoanBal.add(southOvduBal).compareTo(BigDecimal.ZERO) == 1) {
			makeExcel.setValue(19, 17, southOvduBal.add(southColBal).divide(southOvduBal.add(southLoanBal), 4, 4), "0.00%");
		} else {
			makeExcel.setValue(19, 17, BigDecimal.ZERO, "0.00%");
		}

		makeExcel.setValue(25, 12, companyLoanBal, "#,##0");
		makeExcel.setValue(25, 13, companyOvduBal, "#,##0");
		makeExcel.setValue(25, 14, companyColBal, "#,##0");
		if (sCompanyLoanBal.compareTo(BigDecimal.ZERO) == 1) {
			makeExcel.setValue(25, 15, sCompanyLoanBal.divide(sCompanyLoanBal, 4, 4), "0.00%");
		} else {
			makeExcel.setValue(25, 15, BigDecimal.ZERO, "0.00%");
		}
		if (lCompanyLoanBal.compareTo(BigDecimal.ZERO) == 1) {
			makeExcel.setValue(25, 16, lCompanyLoanBal.divide(lCompanyLoanBal, 4, 4), "0.00%");
		} else {
			makeExcel.setValue(25, 16, BigDecimal.ZERO, "0.00%");
		}
		if (companyLoanBal.add(companyOvduBal).compareTo(BigDecimal.ZERO) == 1) {
			makeExcel.setValue(25, 17, companyOvduBal.add(companyColBal).divide(companyOvduBal.add(companyLoanBal), 4, 4), "0.00%");
		} else {
			makeExcel.setValue(25, 17, BigDecimal.ZERO, "0.00%");
		}
	}

	private double cpRate(String xbal00, String xbal02, String xoverbal) {

		BigDecimal bal00 = new BigDecimal(xbal00);
		BigDecimal bal02 = new BigDecimal(xbal02);
		BigDecimal overbal = new BigDecimal(xoverbal);

		return cpRate2(bal00, bal02, overbal);
	}

	private double cpRate2(BigDecimal bal00, BigDecimal bal02, BigDecimal overbal) {

		bal00 = bal00.add(bal02);
		overbal = overbal.add(bal02);

		if (bal00.compareTo(new BigDecimal("0")) == 0) {
			return 0.0000;
		} else {
			overbal = overbal.divide(bal00, 4, 4);
			return overbal.doubleValue();
		}
	}
}
