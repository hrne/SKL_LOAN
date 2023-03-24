package com.st1.itx.trade.L9;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L9723ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;

@Component
@Scope("prototype")

public class L9723Report extends MakeReport {

	L9723ServiceImpl l9723ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	/**
	 * 報表輸出
	 * 
	 * @param titaVo
	 * @param l9723  查詢資料
	 * @return true/false
	 * @throws LogicException
	 * 
	 */
	public boolean exec(TitaVo titaVo, List<Map<String, String>> l9723) throws LogicException {
		this.info("exec export report ...");

		int iRocYearMonth = Integer.valueOf(titaVo.getParam("ReportDateY")) * 100
				+ Integer.valueOf(titaVo.getParam("ReportDateM"));

		// 報表
		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "L9723";
		String fileItem = "放款有效戶數明細表";
		String fileName = "L9723-放款有效戶數明細表_" + iRocYearMonth;
		String sheetName = "有效客戶數";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, sheetName);

		// 欄位寬度
		makeExcel.setWidth(1, 8);
		makeExcel.setWidth(2, 15);
		makeExcel.setWidth(3, 35);
		makeExcel.setWidth(4, 20);
		makeExcel.setWidth(5, 10);
		makeExcel.setWidth(6, 10);
		makeExcel.setWidth(7, 15);

		// 欄位
		makeExcel.setValue(1, 1, "戶號", "L");
		makeExcel.setValue(1, 2, "餘額", "L");
		makeExcel.setValue(1, 3, "通訊地址", "L");
		makeExcel.setValue(1, 4, "AML 組織", "L");
		makeExcel.setValue(1, 5, "國籍", "L");
		makeExcel.setValue(1, 6, "行業別", "L");
		makeExcel.setValue(1, 7, "職位名稱", "L");

		if (l9723 != null && l9723.size() > 0) {
			// 起始列
			int row = 2;

			for (Map<String, String> r : l9723) {

				// 戶號
				int custNo = Integer.valueOf(r.get("CustNo"));
				makeExcel.setValue(row, 1, custNo, "R");
				// 餘額
				BigDecimal loanBal = new BigDecimal(r.get("LoanBalance"));
				makeExcel.setValue(row, 2, loanBal, "R");
				// 通訊地址
				String address = r.get("Address") == null ? "" : r.get("Address");
				makeExcel.setValue(row, 3, address, "L");
				// AML 組織
				String amlGroup = r.get("AMLGroup") == null ? "" : r.get("AMLGroup");
				makeExcel.setValue(row, 4, amlGroup, "R");
				// 國籍
				String nationalityCode = r.get("NationalityCode") == null ? "" : r.get("NationalityCode");
				makeExcel.setValue(row, 5, nationalityCode, "L");
				// 行業別
				int industryCode = r.get("IndustryCode") == null ? 0 : Integer.valueOf(r.get("IndustryCode"));
				makeExcel.setValue(row, 6, industryCode, "R");
				// 職位名稱
				String jobTitle = r.get("JobTitle") == null ? "" : r.get("JobTitle");
				makeExcel.setValue(row, 7, jobTitle, "L");

				row++;

			} // for

			makeExcel.close();

			return true;
		} else {
			makeExcel.setValue(2, 1, "本日無資料");
			makeExcel.close();
			return false;
		}
	}
}