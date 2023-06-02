package com.st1.itx.trade.LY;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.InnFundApl;
import com.st1.itx.db.service.InnFundAplService;
import com.st1.itx.db.service.springjpa.cm.LY007ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")
public class LY007Report extends MakeReport {

	@Autowired
	LY007ServiceImpl lY007ServiceImpl;

	@Autowired
	InnFundAplService sInnFundAplService;

	@Autowired
	Parse parse;

	@Autowired
	MakeExcel makeExcel;

	private BigDecimal totalEquity = BigDecimal.ZERO;
	private String equityDataMonthOutput = "";

	public boolean exec(TitaVo titaVo) throws LogicException {

		this.info("LY007Report exec");

		int inputYearMonth = (Integer.valueOf(titaVo.getParam("RocYear")) + 1911) * 100 + 12;
	
		// 西元月底日
		int mfbsdy = this.txBuffer.getTxCom().getMfbsdyf();

		List<InnFundApl> lInnFundApl = new ArrayList<InnFundApl>();
		// 先取得淨值
		Slice<InnFundApl> slInnFundApl = sInnFundAplService.acDateYearEq(mfbsdy, mfbsdy, 0, Integer.MAX_VALUE, titaVo);

		lInnFundApl = slInnFundApl == null ? null : slInnFundApl.getContent();

		if (lInnFundApl != null) {

			int equityDataMonth = (lInnFundApl.get(0).getAcDate() / 100) - 191100;
			totalEquity = lInnFundApl.get(0).getStockHoldersEqt();
			equityDataMonthOutput = String.valueOf(equityDataMonth);
			equityDataMonthOutput = equityDataMonthOutput.substring(0, 3) + "." + equityDataMonthOutput.substring(3)
					+ ".31";
		}
		this.info("equityDataMonthOutput = " + equityDataMonthOutput);

		
		List<Map<String, String>> lY007List = null;

		try {

			// 各筆資料
			lY007List = lY007ServiceImpl.queryDetail(inputYearMonth, titaVo);

		} catch (Exception e) {

			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LY007erviceImpl.exportExcel error = " + errors.toString());
		}

		this.info("lY007List.size() =" + lY007List.size());
		
		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "LY007";
		String fileItem = "Z100關係人交易明細表";
		String fileName = "LY007_Z100關係人交易明細表_" + ((inputYearMonth - 191100));
		String defaultExcel = "LY007_底稿_Z100關係人交易明細表.xlsx";
		String defaultSheet = "Z100關係人交易明細表";

		this.info("reportVo open");

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();
		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);

//		int entdy = reportDate / 100;

		makeExcel.setValue(2, 2, inputYearMonth, "L"); // 申報年月

//		ExcelFontStyleVo headerStyleVo = new ExcelFontStyleVo();
//		headerStyleVo.setBold(true);

		// 通用處理
		// 設定表中顯示的日期

		makeExcel.setValue(6, 15, showRocDate(inputYearMonth*100+31,6)); // 資料日期

//		makeExcel.setValue(6, 16, equityDataMonthOutput, "C"); // 核閱數資料日期

		makeExcel.setValue(7, 15, totalEquity, "#,##0"); // 核閱數


		eptExcel(lY007List, titaVo);
		
		
		return true;

	}

	private void eptExcel(List<Map<String, String>> lY007List, TitaVo titaVo) throws LogicException {

		this.info("eptExcel");
		int iYear =Integer.valueOf(titaVo.getParam("RocYear"));

		if (lY007List != null && !lY007List.isEmpty()) {

			makeExcel.setValue(7, 15, iYear + ".12.31", "C");
			makeExcel.setValue(8, 15, formatAmt(totalEquity, 0), "C");
			int rowCursor = 7; // 列指標	
			this.info("有值");
			for (Map<String, String> r : lY007List) {
				BigDecimal loanBal = getBigDecimal(r.get("LoanBal"));
				BigDecimal gPercent = this.computeDivide(loanBal, totalEquity, 4);
				String rel = r.get("Rel");
				if(rel.equals("N")) {
					continue;
				}
				this.info("setShiftRow="+rowCursor);
				makeExcel.setShiftRow(rowCursor+1, 1);
				makeExcel.setValue(rowCursor, 1, r.get("Rel"));// 與本公司之關係
				makeExcel.setValue(rowCursor, 2, r.get("CustNo"));// 交易對象代號
				makeExcel.setValue(rowCursor, 3, r.get("CustName"));// 交易對象名稱
				makeExcel.setValue(rowCursor, 4, "A");// 交易種類
				makeExcel.setValue(rowCursor, 5, "A");// 交易型態
				makeExcel.setValue(rowCursor, 6, r.get("BdLoaction"));// 交易標的內容
				makeExcel.setValue(rowCursor, 7, r.get("DrawdownDate"));// 交易日期
				makeExcel.setValue(rowCursor, 8, loanBal);// 交易金額
				makeExcel.setValue(rowCursor, 9, "");// 最近交易日之參考市價
				makeExcel.setValue(rowCursor, 10, "");// 已實現損益
				makeExcel.setValue(rowCursor, 11, "");// 未實現損益
				makeExcel.setValue(rowCursor, 12, gPercent, "C");// 交易金額占業主權益比率%
				makeExcel.setValue(rowCursor, 13, r.get("Supervisor"), "C");// 最後決定權人員
				makeExcel.setValue(rowCursor, 14, "");// 備註
				rowCursor++;
			}
		} else {
			// 無資料時處理
			makeExcel.setValue(8, 1, "本日無資料", "L");
		}
		makeExcel.close();
		this.info("eptExcel close");
	}

}
