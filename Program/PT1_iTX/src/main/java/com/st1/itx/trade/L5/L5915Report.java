package com.st1.itx.trade.L5;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L5915ServiceImpl;
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
	private L5915ServiceImpl l5915ServiceImpl;

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

		ReportVo reportVo = ReportVo.builder().setBrno(titaVo.getKinbr()).setRptDate(titaVo.getEntDyI()).setRptCode(REPORT_CODE).setRptItem(REPORT_ITEM).build();

		makeExcel.open(titaVo, reportVo, FILE_NAME + "_" + workMonth, DEFAULT_EXCEL, "件數");

		setCounts();

		makeExcel.setSheet("金額");

		setAmt();

		makeExcel.toExcel(makeExcel.close());

		return true;
	}

	private void setAmt() throws LogicException {
		this.info("setAmt ... ");

		List<Map<String, String>> listAmt = l5915ServiceImpl.queryAmt(workMonth, titaVo);

		if (listAmt == null || listAmt.isEmpty()) {
			return;
		}

		this.info("result size = " + listAmt.size());

		int rowCursor = 2;

		// 2022-08-23 ST1-智偉修改
		// SKL User 李珮君 要求跟AS400產一樣的檔案
		// 協辦人員業績金額的檔案要產出不只有協辦人員業績金額的檔案
		for (Map<String, String> m : listAmt) {

			// 戶號
			makeExcel.setValue(rowCursor, 1, Integer.valueOf(m.get("CustNo")));
			// 額度
			makeExcel.setValue(rowCursor, 2, Integer.valueOf(m.get("FacmNo")));
			// 撥款金額
			makeExcel.setValue(rowCursor, 3, getBigDecimal(m.get("DrawdownAmt")), "#,##0");
			// 計件代碼
			makeExcel.setValue(rowCursor, 4, m.get("PieceCode"), "L");
			// 員工代碼
			makeExcel.setValue(rowCursor, 5, m.get("EmpNo"), "L");
			// 員工姓名
			makeExcel.setValue(rowCursor, 6, m.get("EmpName"), "L");
			// 部室
			makeExcel.setValue(rowCursor, 7, m.get("Dept"), "L");
			// 區部
			makeExcel.setValue(rowCursor, 8, m.get("Dist"), "L");
			// 單位
			makeExcel.setValue(rowCursor, 9, m.get("Unit"), "L");

			rowCursor++;
		}
	}

	private void setCounts() throws LogicException {
		this.info("setCounts ... ");

		List<Map<String, String>> listCnt = l5915ServiceImpl.queryCounts(workMonth, titaVo);

		if (listCnt == null || listCnt.isEmpty()) {
			return;
		}

		this.info("result size = " + listCnt.size());

		int rowCursor = 2;

		// 2022-08-23 ST1-智偉修改
		// SKL User 李珮君 要求跟AS400產一樣的檔案
		// 協辦人員業績件數的檔案要產出不只有協辦人員業績件數的檔案
		for (Map<String, String> m : listCnt) {

			// 獎金類別
			makeExcel.setValue(rowCursor, 1, Integer.valueOf(m.get("RewardType")));
			// 計件代碼
			makeExcel.setValue(rowCursor, 2, m.get("PieceCode"), "L");
			// 撥款金額
			makeExcel.setValue(rowCursor, 3, getBigDecimal(m.get("DrawdownAmt")), "#,##0");
			// 車馬費發放日期
			makeExcel.setValue(rowCursor, 4, this.showBcDate(m.get("BonusDate"), 2));
			// 戶號
			makeExcel.setValue(rowCursor, 5, Integer.valueOf(m.get("CustNo")));
			// 戶名
			makeExcel.setValue(rowCursor, 6, m.get("CustName"), "L");
			// 額度號碼
			makeExcel.setValue(rowCursor, 7, Integer.valueOf(m.get("FacmNo")));
			// 統一編號
			makeExcel.setValue(rowCursor, 8, m.get("CustId"), "L");
			// 已用額度
			makeExcel.setValue(rowCursor, 9, getBigDecimal(m.get("UtilBal")), "#,##0");
			// 車馬費發放額
			makeExcel.setValue(rowCursor, 10, getBigDecimal(m.get("Bonus")), "#,##0");
			// 員工代號
			makeExcel.setValue(rowCursor, 11, m.get("EmpNo"), "L");
			// 員工姓名
			makeExcel.setValue(rowCursor, 12, m.get("EmpName"), "L");
			// 部室
			makeExcel.setValue(rowCursor, 13, m.get("Dept"), "L");
			// 區部
			makeExcel.setValue(rowCursor, 14, m.get("Dist"), "L");
			// 單位
			makeExcel.setValue(rowCursor, 15, m.get("Unit"), "L");

			rowCursor++;
		}
	}
}
