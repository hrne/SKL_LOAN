package com.st1.itx.trade.LY;

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
import com.st1.itx.db.service.springjpa.cm.LY004ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")
public class LY004Report extends MakeReport {

	@Autowired
	LY004ServiceImpl lY004ServiceImpl;

	@Autowired
	Parse parse;

	@Autowired
	MakeExcel makeExcel;

	public boolean exec(TitaVo titaVo) throws LogicException {

		int entdyf = titaVo.getEntDyI() + 19110000;

		int iYear = entdyf / 10000;

		int iMonth = entdyf % 10000;

		if (iMonth != 12) {
			iYear = iYear - 1;
		}

		int inputYearMonth = (iYear * 100) + 12;

		List<Map<String, String>> lY004List = null;

		try {
			lY004List = lY004ServiceImpl.findAll(inputYearMonth, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LY004ServiceImpl.testExcel error = " + errors.toString());
		}

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "LY004";
		String fileItem = "非RBC_表14-4_會計部年度檢查報表";
		String fileName = "LY004-非RBC_表14-4_會計部年度檢查報表";
		String defaultExcel = "LY004_底稿_非RBC_表14-4_會計部年度檢查報表.xlsx";
		String defaultSheet = "表14-4";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();
		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);

//		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LY004", "非RBC_表14-4_會計部年度檢查報表", "LY004-非RBC_表14-4_會計部年度檢查報表", "LY004_底稿_非RBC_表14-4_會計部年度檢查報表.xlsx", "表14-4");

		// 通用處理
		// 設定年月份
		String iRoc = String.valueOf(inputYearMonth / 100 - 1911);
		String iMon = String.valueOf(inputYearMonth % 100);
		makeExcel.setValue(1, 1, String.format("新光人壽保險股份有限公司  %s年度(%s月)報表", iRoc, iMon));

		int row = 6;
		if (lY004List != null && !lY004List.isEmpty()) {

			BigDecimal OvduAmt = new BigDecimal(lY004List.get(0).get("OvduAmt"));
			BigDecimal DiffOvduAmt = new BigDecimal(lY004List.get(0).get("DiffOvduAmt"));
			BigDecimal LoanBal = new BigDecimal(lY004List.get(0).get("LoanBal"));
			BigDecimal DiffLoanBal = new BigDecimal(lY004List.get(0).get("DiffLoanBal"));
			BigDecimal Ratio = new BigDecimal(lY004List.get(0).get("Ratio"));

			makeExcel.setValue(row, 2, OvduAmt, "#,##0");
			makeExcel.setValue(row, 3, DiffOvduAmt, "#,##0");
			makeExcel.setValue(row, 4, LoanBal, "#,##0");
			makeExcel.setValue(row, 5, DiffLoanBal, "#,##0");
			makeExcel.setValue(row, 6, Ratio, "0.00%");

		} else {
			// 無資料時處理
			makeExcel.setValue(6, 2, "本日無資料!!");

			return false;
		}

		makeExcel.close();

		return true;

	}
}
