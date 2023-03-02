package com.st1.itx.trade.LQ;

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
import com.st1.itx.db.service.springjpa.cm.LQ007ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class LQ007Report extends MakeReport {

	@Autowired
	public LQ007ServiceImpl lQ007ServiceImpl;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	Parse parse;

	@Autowired
	MakeExcel makeExcel;

	BigDecimal totleIntSum = BigDecimal.ZERO;

	public void exec(TitaVo titaVo) throws LogicException {

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "LQ007";
		String fileItem = "專案放款餘額及利息收入";
		String fileName = "LQ007-專案放款餘額及利息收入";
		String defaultExcel = "LQ007_底稿_專案放款餘額及利息收入.xlsx";
		String defaultSheet = "YYYMM";

		this.info("reportVo open");

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();
		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);

		List<Map<String, String>> LQ007List = null;
		try {
			LQ007List = lQ007ServiceImpl.findAll(titaVo);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("lQ007ServiceImpl.findAll error = " + errors.toString());
		}

		int YearMonth = reportDate / 10000;
		int col = 2;
		makeExcel.setSheet("YYYMM", String.valueOf(titaVo.getEntDyI()/100));

		if (LQ007List != null && !LQ007List.isEmpty()) {
			

			for (Map<String, String> LQ007Vo : LQ007List) {

				// only works if sql query is ordered by YearMonth
				int visibleMonth = Integer.valueOf(LQ007Vo.get("F1"));

				if (YearMonth > visibleMonth / 100) {

					if (visibleMonth % 100 == 12) {
						makeExcel.setValue(3, col, (visibleMonth - 191100) / 100);
						if (LQ007Vo.get("F0").equals("AA")) {
							makeExcel.setValue(5, col, LQ007Vo.get("F2"), "C");
							makeExcel.setValue(5, col + 1, LQ007Vo.get("F3"), "C");
						} else if (LQ007Vo.get("F0").equals("ZZ")) {
							makeExcel.setValue(13, col, LQ007Vo.get("F2"), "C");
							makeExcel.setValue(13, col + 1, LQ007Vo.get("F3"), "C");
						} else {
							for (int row = 6; row <= 12; row++) {

								makeExcel.setValue(row, col, LQ007Vo.get("F2"), "C");
								BigDecimal IntSum = getBigDecimal(LQ007Vo.get("F3"));
								totleIntSum = totleIntSum.add(IntSum);
								makeExcel.setValue(6, col + 1, formatAmt(totleIntSum, 3, 8), "C");
							}

						}

					}
					col += 2;

				} else {
						int tmpCol = visibleMonth % 100 / 3 - 1;
					
						makeExcel.setValue(3, col + (2 * tmpCol), (visibleMonth - 191100) / 100);
						if (LQ007Vo.get("F0").equals("AA")) {
							makeExcel.setValue(5, col+ (2 * tmpCol), LQ007Vo.get("F2"), "C");
							makeExcel.setValue(5, col + (2 * tmpCol)+ 1, LQ007Vo.get("F3"), "C");
						} else if (LQ007Vo.get("F0").equals("ZZ")) {
							makeExcel.setValue(13, col+ (2 * tmpCol), LQ007Vo.get("F2"), "C");
							makeExcel.setValue(13, col + (2 * tmpCol)+ 1, LQ007Vo.get("F3"), "C");
						} else {
							for (int row = 6; row <= 12; row++) {

								makeExcel.setValue(row, col+ (2 * tmpCol), LQ007Vo.get("F2"), "C");
								BigDecimal IntSum = getBigDecimal(LQ007Vo.get("F3"));
								totleIntSum = totleIntSum.add(IntSum);
								makeExcel.setValue(6, col + (2 * tmpCol)+ 1, formatAmt(totleIntSum, 3, 8), "C");
							}

						}

					}
					// 第二個表格
					if (visibleMonth == reportDate) {
						makeExcel.setValue(3, col+8 , (visibleMonth - 191100) / 100);
						if (LQ007Vo.get("F0").equals("AA")) {
							makeExcel.setValue(5, col+8, LQ007Vo.get("F2"), "C");
							makeExcel.setValue(5, col + 9, LQ007Vo.get("F3"), "C");
						} else if (LQ007Vo.get("F0").equals("ZZ")) {
							makeExcel.setValue(13, col+8, LQ007Vo.get("F2"), "C");
							makeExcel.setValue(13, col + 9, LQ007Vo.get("F3"), "C");
						} else {
							for (int row = 6; row <= 12; row++) {

								makeExcel.setValue(row, col+8, LQ007Vo.get("F2"), "C");
								BigDecimal IntSum = getBigDecimal(LQ007Vo.get("F3"));
								totleIntSum = totleIntSum.add(IntSum);
								makeExcel.setValue(6, col + 9, formatAmt(totleIntSum, 3, 8), "C");
							}

						}

					}

				
			}

		} else {
			makeExcel.setValue(5, 2, "本日無資料");

		}

		makeExcel.close();
		// this.toPdf(sno);

	}

}
