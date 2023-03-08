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
import com.st1.itx.db.service.springjpa.cm.LM032ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;

@Component
@Scope("prototype")

public class LM032Report extends MakeReport {

	@Autowired
	LM032ServiceImpl lM032ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	public void exec(TitaVo titaVo) throws LogicException {

		List<Map<String, String>> LM032List = null;
		try {

			LM032List = lM032ServiceImpl.findAll(titaVo);
			exportExcel(titaVo, LM032List);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM032ServiceImpl.testExcel error = " + errors.toString());
		}
	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> LDList) throws LogicException {

		this.info("LM032Report exportExcel");
		
		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "LM032";
		String fileItem = "逾期案件滾動率明細";
		String fileName = "LM032-逾期案件滾動率明細";
		String defaultExcel = "LM032_底稿_逾期案件滾動率明細.xlsx";
		String defaultSheet = "D9612263";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);
//		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM032", "逾期案件滾動率明細", "LM032逾期案件滾動率明細",
//				"逾期案件滾動率明細.xlsx", "D9612263");
		if (LDList == null || LDList.isEmpty()) {
			makeExcel.setValue(3, 1, "本日無資料");
		} else {
			int row = 3;
			BigDecimal dataCount = BigDecimal.ZERO;
			BigDecimal loanBalTotal_LastMonth = BigDecimal.ZERO;
			BigDecimal loanBalTotal_ThisMonth = BigDecimal.ZERO;

			for (Map<String, String> tLDVo : LDList) {

				for (int i = 0; i <= 15; i++) {

					String value = tLDVo.get("F" + i);

					switch (i) {

					case 3:
						// 戶號，算資料筆數
						dataCount = dataCount.add(BigDecimal.ONE);
						break;

					case 5:
						// 上月放款餘額，算總計
						loanBalTotal_LastMonth = loanBalTotal_LastMonth.add(getBigDecimal(value));
						break;

					case 13:
						// 當月放款餘額，算總計
						loanBalTotal_ThisMonth = loanBalTotal_ThisMonth.add(getBigDecimal(value));
						break;

					default:
						break;
					}

					makeExcel.setValue(row, i + 1, value, "R");
				} // for

				row++;
			} // for

			// 寫總計
			makeExcel.setValue(1, 4, formatAmt(dataCount, 0));
			makeExcel.setValue(1, 6, formatAmt(loanBalTotal_LastMonth, 0));
			makeExcel.setValue(1, 14, formatAmt(loanBalTotal_ThisMonth, 0));

		}

		makeExcel.close();
		//makeExcel.toExcel(sno);
	}

}
