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
import com.st1.itx.db.service.springjpa.cm.LM078ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class LM078Report extends MakeReport {

	@Autowired
	LM078ServiceImpl lM078ServiceImpl;

	@Autowired
	Parse parse;
	
	@Autowired
	MakeExcel makeExcel;
	

//	private int limitAcDate1 = 0;
//	private int limitAcDate2 = 0;

	
	//B044 現在不產 還是會放0
	public boolean exec(TitaVo titaVo) throws LogicException {
		this.info("LM078Report exec start ...");

		int iAcDate = titaVo.getEntDyI() + 19110000;
		this.info("LM078Report exec AcDate = " + iAcDate);

		List<Map<String, String>> lLM078List = new ArrayList<Map<String, String>>();

		try {
			lLM078List = lM078ServiceImpl.findAll(titaVo, iAcDate / 100);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LM078ServiceImpl.findAll error = " + errors.toString());
		}

		exportExcel(titaVo, lLM078List, iAcDate);

		return true;

	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> list, int date) throws LogicException {
		this.info("LM078Report exportExcel");
		int entdy = date - 19110000; // expects date to be in BC Date format.
		String YearMonth = entdy / 10000 + " 年 " + String.format("%02d", entdy / 100 % 100) + " 月";

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "LM078";
		String fileItem = "B044「借款戶向金融機構申請並經錄案」之不動產抵押貸款案件辦理情形";
		String fileName = "LM078_B044「借款戶向金融機構申請並經錄案」之不動產抵押貸款案件辦理情形" + showRocDate(entdy, 0).substring(0, 7);
		String defaultExcel = "LM078_底稿_B044「借款戶向金融機構申請並經錄案」之不動產抵押貸款案件辦理情形.xlsx";
		String defaultSheet = "FOA";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();
		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);

		// 資料期間 E2
		makeExcel.setValue(2, 5, "新光人壽保險股份有限公司", "C");
		// 民國年月E3
		makeExcel.setValue(3, 5, "民國 " + YearMonth, "C");

		if (list == null) {
			makeExcel.setValue(4, 1, "本月無資料");

		} else {

			int row = 0;
			int col = 0;
			BigDecimal amt = BigDecimal.ZERO;

			int count = 0 ;

			for (Map<String, String> r : list) {

				row = 6 + parse.stringToInteger(r.get("CitySeq"));
				col = 3;
						
				count = parse.stringToInteger(r.get("Count"));
				amt = getBigDecimal(r.get("LineAmt"));

				makeExcel.setValue(row, col, count, "0", "R");
				makeExcel.setValue(row, col + 1, amt, "0.00", "R");

			}

			// 處理此日期範圍的全國合計
			for (int i = 3; i <= 14; i++) {
				makeExcel.formulaCaculate(7, i);
			}
		}

		makeExcel.close();

	}
}
