package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM011ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class LM011Report extends MakeReport {

	@Autowired
	LM011ServiceImpl lM011ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	private int dateYear = 0;
	private int dateMonth = 0;

	@Autowired
	Parse parse;

	public void exec(TitaVo titaVo) throws LogicException {
		this.info("LM011Report exec start ...");

		dateYear = parse.stringToInteger(titaVo.getParam("Year")) + 1911;
		dateMonth = parse.stringToInteger(titaVo.getParam("Month"));

		int dateSent = dateYear * 100 + dateMonth;

		this.info("LM011Report exec dateSent = " + dateSent);

		List<Map<String, String>> lLM011 = null;
		List<Map<String, String>> lLM011Drawdown = null;

		try {
			// 查未撥資料
			lLM011 = lM011ServiceImpl.findAll(dateSent, 0, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LM011ServiceImpl.findAll 查未撥資料 error = " + errors.toString());
		}

		try {
			// 查已撥資料
			lLM011Drawdown = lM011ServiceImpl.findAll(dateSent, 1, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM011ServiceImpl.findAll 查已撥資料 error = " + errors.toString());
		}

		exportExcel(titaVo, lLM011, lLM011Drawdown, dateSent);

	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> lLM011, List<Map<String, String>> lLM011Drawdown, int date) throws LogicException {
		this.info("LM011Report exportExcel");

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "LM011";
		String fileItem = "表外明細";
		String fileName =  "LM011表外明細" + dateYear + "年" + dateMonth + "月";
		String defaultExcel =  "LM011_底稿_表外明細.xlsx";
		String defaultSheet = "LNWLCTP";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);
		
//		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr()
//				, "LM011", "表外明細", "LM011表外明細" + dateYear + "年" + dateMonth + "月", "LM011表外明細.xlsx", "LNWLCTP");

		int row = 2;

		if (lLM011 != null && lLM011.size() != 0) {

			for (Map<String, String> tLDVo : lLM011) {

				for (int i = 0; i <= 20; i++) {

					int col = i + 1;

					String tmpValue = tLDVo.get("F" + i);

					switch (i) {
					case 11: // L欄:核准額度
					case 12: // M欄:放款餘額
					case 13: // N欄:可動用餘額
						// 金額
						makeExcel.setValue(row, col, getBigDecimal(tmpValue), "###0", "R");
						break;
					case 17: // R欄:信用風險轉換係數
					case 18: // S欄:表外曝險金額
						// 金額
						makeExcel.setValue(row, col, getBigDecimal(tmpValue), "###0.00", "R");
						break;
					default:
						makeExcel.setValue(row, col, tmpValue, "R");
						break;
					}
				} // for

				row++;
			} // for
		} else {
			makeExcel.setValue(2, 1, "本日無資料");
		}

		makeExcel.setSheet("LNWLCAP已核撥");

		row = 2;

		if (lLM011Drawdown != null && lLM011Drawdown.size() != 0) {

			for (Map<String, String> tLDVo : lLM011Drawdown) {

				for (int i = 0; i <= 21; i++) {

					int col = i + 1;

					String tmpValue = tLDVo.get("F" + i);

					switch (i) {
					case 11: // L欄:核准額度
					case 12: // M欄:放款餘額
					case 13: // N欄:可動用餘額
						// 金額
						makeExcel.setValue(row, col, getBigDecimal(tmpValue), "###0", "R");
						break;
					case 17: // R欄:信用風險轉換係數
					case 18: // S欄:表外曝險金額
						// 金額
						makeExcel.setValue(row, col, getBigDecimal(tmpValue), "###0.00", "R");
						break;
					case 19: // 第二個Sheet不顯示借方：備忘分錄會計科目
					case 20: // 第二個Sheet不顯示貸方：備忘分錄會計科目
						break;
					case 21: // 第二個Sheet此欄位實際印在第20欄
						makeExcel.setValue(row, 20, tmpValue, "R");
						break;
					default:
						makeExcel.setValue(row, col, tmpValue, "R");
						break;
					}
				} // for
				row++;
			} // for
		} else {
			makeExcel.setValue(2, 1, "本日無資料");
		}

		makeExcel.close();
		//makeExcel.toExcel(sno);
	}
}
