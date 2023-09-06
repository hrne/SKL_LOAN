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
import com.st1.itx.db.service.springjpa.cm.LM088ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")
public class LM088Report extends MakeReport {

	@Autowired
	LM088ServiceImpl lM088ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	Parse parse;

	public boolean exec(TitaVo titaVo) throws LogicException {
		this.info("LM088Report exec ...");

		int iAcDate = titaVo.getEntDyI() + 19110000;
		int iYearMonth = iAcDate / 100;

		List<Map<String, String>> lLM088List = new ArrayList<Map<String, String>>();

		try {
			lLM088List = lM088ServiceImpl.findAll(iYearMonth, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LM088ServiceImpl.findAll error = " + errors.toString());
		}

		exportExcel(titaVo, lLM088List, iAcDate);

		return true;

	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> lLM088List, int iAcDate) throws LogicException {
		this.info("LM088Report exportExcel");

//		String rocYearMonth = this.showRocDate(iAcDate, 5);
		String rocDate = this.showRocDate(iAcDate, 6);
		String rocYYYMM = ((iAcDate - 19110000) / 100) + "";
		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "LM088";
		String fileItem = "放款內部控制_關係企業查核" + rocYYYMM;
		String fileName = "LM088_8-2放款內部控制_關係企業查核" + rocYYYMM;
		String defaultExcel = "LM088_底稿_8-2 放款內部控制_關係企業查核.xlsx";
		String defaultSheet = "同一關係企業";
		String reNameSheet = rocYYYMM + "-同一關係企業";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();
		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);
		makeExcel.setSheet(defaultSheet, reNameSheet);

		makeExcel.setValue(1, 2, rocDate, "L");

		makeExcel.setValue(3, 5, "戶名", "C");
		makeExcel.setValue(3, 6, "戶號", "C");
		makeExcel.setValue(3, 7, "ID", "C");
		makeExcel.setValue(3, 8, "餘額", "C");

		if (lLM088List == null || lLM088List.size() == 0) {

			makeExcel.setValue(2, 2, "本日無資料");
		} else {
			BigDecimal loanBalTol = BigDecimal.ZERO;
			int custNoMainCnt = 0;
			int cnt = 0;
			int row = 3;

			makeExcel.setValue(1, 5, "同一關係企業", "L");

			for (Map<String, String> r : lLM088List) {
				String groupName = r.get("GroupName");
				int entCode = parse.stringToInteger(r.get("EntCode"));
				int custNo = parse.stringToInteger(r.get("CustNo"));
				int custNoMain = parse.stringToInteger(r.get("CustNoMain"));
				String custName = r.get("CustName");
				String custid = r.get("CustId");
				BigDecimal loanBal = getBigDecimal(r.get("LoanBal"));
				loanBalTol = loanBalTol.add(loanBal);

				int entCodeNext = entCode;
				int custNoNext = custNoMain;
				if (cnt + 1 < lLM088List.size()) {
					entCodeNext = parse.stringToInteger(lLM088List.get(cnt + 1).get("EntCode"));
					custNoNext = parse.stringToInteger(lLM088List.get(cnt + 1).get("CustNoMain"));
				}

				this.info("custNo vs next custNo =" + custNo + " vs " + custNoNext);

				custNoMainCnt++;
				cnt++;
				row++;	
				
				
				if (entCode != entCodeNext) {
					makeExcel.setValue(row, 5, "同一關係人", "L");
					row++;
				}

				makeExcel.setValue(row, 5, custName, "L");
				makeExcel.setValue(row, 6, custNo, "C");
				makeExcel.setValue(row, 7, custid, "C");
				makeExcel.setValue(row, 8, loanBal, "#,##0", "R");

				// 若當前戶號與下一個戶號不同 或 整資料的最後一筆 需列印統計數

				if (custNoMain != custNoNext || lLM088List.size() == cnt) {
					// ex:目標是印第4列，當筆數到第7列結束(row=7)，同時紀錄同一關係企業戶號的4筆數量(custNoMain=4)最後再+1
					int tmpRow = row - custNoMainCnt + 1;
					makeExcel.setValue(tmpRow, 2, groupName, "L");
					makeExcel.setValue(tmpRow, 3, loanBalTol, "#,##0", "R");

					row++;
					makeExcel.setValue(row, 8, loanBalTol, "#,##0", "R");
					custNoMainCnt = 0;
					loanBalTol = BigDecimal.ZERO;

					row++;// 空一列
				}

			}

		}

		makeExcel.close();
		// makeExcel.toExcel(sno);
	}

}
