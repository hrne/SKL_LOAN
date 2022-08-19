package com.st1.itx.trade.L5;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LP005ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")
public class L5915Report extends MakeReport {

	@Autowired
	private Parse parse;

	@Autowired
	private LP005ServiceImpl lp005ServiceImpl;

	@Autowired
	private MakeExcel makeExcel;

	private int workMonth;

	private static final String REPORT_CODE = "L5915";
	private static final String REPORT_ITEM = "協辦人員業績統計_件數及金額明細";
	private static final String FILE_NAME = "L5915_協辦人員業績統計_件數及金額明細";
	private static final String DEFAULT_EXCEL = "L5915_底稿_協辦人員業績統計_件數及金額明細.xlsx";

	public boolean exec(TitaVo titaVo) throws LogicException {
		this.info("L5915Report exec ...");

		this.titaVo = titaVo;

		workMonth = parse.stringToInteger(titaVo.getParam("Ym")) + 191100;

		this.info("workMonth = " + workMonth);

		ReportVo reportVo = ReportVo.builder().setBrno(titaVo.getKinbr()).setRptDate(titaVo.getEntDyI())
				.setRptCode(REPORT_CODE).setRptItem(REPORT_ITEM).build();

		makeExcel.open(titaVo, reportVo, FILE_NAME + "_" + workMonth, DEFAULT_EXCEL, "件數");

		setCounts();

		makeExcel.setSheet("金額");

		setAmt();

		makeExcel.toExcel(makeExcel.close());

		return true;
	}

	private void setAmt() throws LogicException {
		this.info("setAmt ... ");

		List<Map<String, String>> listAmt = lp005ServiceImpl.queryAmt(workMonth, titaVo);

		if (listAmt == null || listAmt.isEmpty()) {
			return;
		}

		this.info("result size = " + listAmt.size());

		int rowCursor = 2;

		for (Map<String, String> m : listAmt) {

			// 戶號
			makeExcel.setValue(rowCursor, 1, Integer.valueOf(m.get("F0")));
			// 額度
			makeExcel.setValue(rowCursor, 2, Integer.valueOf(m.get("F1")));
			// 撥款金額
			makeExcel.setValue(rowCursor, 3, getBigDecimal(m.get("F2")), "#,##0");
			// 計件代碼
			makeExcel.setValue(rowCursor, 4, m.get("F3"), "L");
			// 員工代碼
			makeExcel.setValue(rowCursor, 5, m.get("F4"), "L");
			// 員工姓名
			makeExcel.setValue(rowCursor, 6, m.get("F5"), "L");
			// 部室
			makeExcel.setValue(rowCursor, 7, m.get("F6"), "L");
			// 區部
			makeExcel.setValue(rowCursor, 8, m.get("F7"), "L");
			// 單位
			makeExcel.setValue(rowCursor, 9, m.get("F8"), "L");

			rowCursor++;
		}
	}

	private void setCounts() throws LogicException {
		this.info("setCounts ... ");

		List<Map<String, String>> listCnt = lp005ServiceImpl.queryCounts(workMonth, titaVo);

		if (listCnt == null || listCnt.isEmpty()) {
			return;
		}

		this.info("result size = " + listCnt.size());

		int rowCursor = 2;

		for (Map<String, String> m : listCnt) {

			// 獎金類別
			makeExcel.setValue(rowCursor, 1, Integer.valueOf(m.get("F0")));
			// 戶號
			makeExcel.setValue(rowCursor, 2, Integer.valueOf(m.get("F1")));
			// 戶名
			makeExcel.setValue(rowCursor, 3, m.get("F2"), "L");
			// 車馬費發放額
			makeExcel.setValue(rowCursor, 4, getBigDecimal(m.get("F3")), "#,##0");
			// 介紹人
			makeExcel.setValue(rowCursor, 5, m.get("F4"), "L");
			// 員工姓名
			makeExcel.setValue(rowCursor, 6, m.get("F5"), "L");
			// 部室
			makeExcel.setValue(rowCursor, 7, m.get("F6"), "L");
			// 區部
			makeExcel.setValue(rowCursor, 8, m.get("F7"), "L");
			// 單位
			makeExcel.setValue(rowCursor, 9, m.get("F8"), "L");

			rowCursor++;
		}
	}
}
