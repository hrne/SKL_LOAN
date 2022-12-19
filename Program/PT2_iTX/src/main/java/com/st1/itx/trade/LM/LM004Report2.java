package com.st1.itx.trade.LM;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;

@Component("lM004Report2")
@Scope("prototype")

// Making Excel

public class LM004Report2 extends MakeReport {

	@Autowired
	public MakeExcel makeExcel;

	public void exec(TitaVo titaVo, List<Map<String, String>> LM004List) throws LogicException {

		if (LM004List.size() != 0) {
			exportExcel(titaVo, LM004List);
		}
	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> LDList) throws LogicException {
		this.info("===========in testExcel");
		String entdy = titaVo.get("ENTDY").toString();
		
		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "LM004";
		String fileItem = "長中短期放款到期追蹤表";
		String fileName = "LM004長中短期放款到期追蹤表";
		String defaultExcel = "LM004長中短期放款到期追蹤表.xls";
		String defaultSheet = "10806";
		String  newSheetName =showDate(entdy, 1);

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);
		makeExcel.setSheet(defaultSheet, newSheetName);
//		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM004", "長中短期放款到期追蹤表", "LM004長中短期放款到期追蹤表",
//				"LM004長中短期放款到期追蹤表.xls", "10806", );

		int row = 3;
		int num = 1;
		makeExcel.setValue(1, 1, showDate(entdy, 2) + "長中短期放款到期追蹤表");

		for (Map<String, String> tLDVo : LDList) {

			int col = 0;

			for (col = 0; col < tLDVo.size(); col++) {

				switch (col) {
				case 1:
					// 序號
					makeExcel.setValue(row, col, num);
					num++;
					break;
				case 2:
					// 經辦單位
					makeExcel.setValue(row, col, tLDVo.get("F14"));
					break;
				case 3:
					// 押品地區
					makeExcel.setValue(row, col, tLDVo.get("F3"));
					break;
				case 4:
					// 經辦人
					makeExcel.setValue(row, col, tLDVo.get("F4"));
					break;
				case 5:
					// 戶名
					makeExcel.setValue(row, col, tLDVo.get("F7"));
					break;
				case 6:
					// 戶號
					makeExcel.setValue(row, col, tLDVo.get("F5"));
					break;
				case 7:
					// 額度
					makeExcel.setValue(row, col, tLDVo.get("F6"));
					break;
				case 8:

					// 到期日
					makeExcel.setValue(row, col, Integer.valueOf(tLDVo.get("F8")));
					break;
				case 9:
					// 應完成日
					makeExcel.setValue(row, col, Integer.valueOf(tLDVo.get("F9")));
					break;
				case 10:
					// 金額
					makeExcel.setValue(row, col, new BigDecimal(tLDVo.get("F10").toString()), "#,##0");
					break;
				case 11:
					// 介紹人
					makeExcel.setValue(row, col, tLDVo.get("F12"));
					break;
				case 12:
					// 部室
					makeExcel.setValue(row, col, tLDVo.get("F14"));
					break;
				case 13:
					// 區部
					makeExcel.setValue(row, col, tLDVo.get("F15"));
					break;
				case 14:
					// 通訊處
					makeExcel.setValue(row, col, tLDVo.get("F16"));
					break;
				case 15:
					// 追蹤日期
					makeExcel.setValue(row, col, tLDVo.get("F11"));
					break;
				case 16:
					// 處理情形

					break;
				default:

					break;
				}
			}
			row++;
		}

		makeExcel.close();
		// makeExcel.toExcel(sno);
	}

	private String showDate(String date, int iType) {
		this.info("MakeReport.toPdf showRocDate1 = " + date);
		if (date == null || date.equals("") || date.equals("0")) {
			return "";
		}
		int rocdate = Integer.valueOf(date);
		if (rocdate > 19110000) {
			rocdate -= 19110000;
		}
		String rocdatex = String.valueOf(rocdate);
		this.info("MakeReport.toPdf showRocDate2 = " + rocdatex);
		if (iType == 1) {
			if (rocdatex.length() == 6) {
				return rocdatex.substring(0, 4);
			} else {
				return rocdatex.substring(0, 5);
			}
		} else if (iType == 2) {
			if (rocdatex.length() == 6) {
				return rocdatex.substring(0, 2) + "年" + rocdatex.substring(2, 4) + "月份";
			} else {
				return rocdatex.substring(0, 3) + "年" + rocdatex.substring(3, 5) + "月份";
			}
		} else {
			return rocdatex;
		}
	}

}
