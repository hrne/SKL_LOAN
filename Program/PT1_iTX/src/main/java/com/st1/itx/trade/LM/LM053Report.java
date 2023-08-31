package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM053ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class LM053Report extends MakeReport {

	@Autowired
	LM053ServiceImpl lM053ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	Parse parse;

	@Override
	public void printTitle() {

	}

	/**
	 * @param titaVo
	 * @throws LogicException
	 */
	public void exec(TitaVo titaVo) throws LogicException {
		List<Map<String, String>> fnAllList = new ArrayList<>();

		this.info("LM053Report exec");

//		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM053", "法務分配款明細表_內部控管", 
//				"LM053法務分配款明細表_內部控管",
//				"LM053_底稿_法務分配款明細表_內部控管.xlsx", "法務分配表");
		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "LM053";
		String fileItem = "法務分配款明細表_內部控管";
		String fileName = "LM053法務分配款明細表_內部控管";
		String defaultExcel = "LM053_底稿_法務分配款明細表_內部控管.xlsx";
		String defaultSheet = "法務分配表";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);

		try {
			fnAllList = lM053ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM053ServiceImpl.findAll error = " + errors.toString());
		}

		// 分配表日期 地區 戶號 額度 戶名 拍定金額 債權金額 分配金額 不足額 領取分配金額 入帳日 損失率 % 法務人員 備註

		int row = 2;
		if (fnAllList.size() > 0) {

			for (Map<String, String> r : fnAllList) {

				row++;

				String entryDate = " ";
				if (r.get("RecordDate") != null) {
					entryDate = this.showRocDate(r.get("RecordDate"), 6);
				}

				// MainSeq判斷是否為 法務進度092
				if ("1".equals(r.get("MainSeq"))) {

					// 分配表日期
					makeExcel.setValue(row, 1, this.showRocDate(r.get("RecordDate"), 3), "C");
					// 地區
					String cityItem = r.get("CityItem") == null ? " " : r.get("CityItem");
					makeExcel.setValue(row, 2, cityItem, "C");
					// 戶號
					int custNo = parse.stringToInteger(r.get("CustNo"));
					makeExcel.setValue(row, 3, custNo, "C");
					// 額度
					String facmNo = parse.stringToInteger(r.get("FacmNo")) == 0 ? " " : r.get("FacmNo");
					makeExcel.setValue(row, 4, facmNo, "C");
					// 戶名
					String custName = r.get("CustName") == null ? " " : r.get("CustName");
					makeExcel.setValue(row, 5, custName, "C");
					// 拍定金額 56
					makeExcel.setValue(row, 6, getBigDecimal(r.get("Amount056")), "#,##0", "R");
					// 債權金額 57
					makeExcel.setValue(row, 7, getBigDecimal(r.get("Amount057")), "#,##0", "R");
					// 分配金額 58
					makeExcel.setValue(row, 8, getBigDecimal(r.get("Amount058")), "#,##0", "R");
					// 不足額 57(58)-60
					makeExcel.setValue(row, 9, getBigDecimal(r.get("deficitAmount")), "#,##0", "R");
					// 領取分配金額60
					makeExcel.setValue(row, 10, getBigDecimal(r.get("Amount060")), "#,##0", "R");
					// 入帳日
					makeExcel.setValue(row, 11, entryDate, "C");
					// 法催人員
					String collName = r.get("Fullname") == null ? " " : r.get("Fullname");
					makeExcel.setValue(row, 13, collName, "C");

				} else {
					// 領取分配金額92
					makeExcel.setValue(row, 10, getBigDecimal(r.get("Amount092")), "#,##0", "R");
					// 入帳日
					makeExcel.setValue(row, 11, entryDate, "C");
					// 法催人員
					String collName = r.get("Fullname") == null ? " " : r.get("Fullname");
					makeExcel.setValue(row, 13, collName, "C");
				}

			}
		} else {
			makeExcel.setValue(3, 1, "本日無資料");
		}
		makeExcel.setValue(1, 3, row - 2);
		makeExcel.close();
	}
}
