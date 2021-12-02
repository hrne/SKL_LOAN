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
import com.st1.itx.db.service.springjpa.cm.LM047ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class LM047Report extends MakeReport {

	@Autowired
	LM047ServiceImpl lM047ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	Parse parse;

	@Override
	public void printTitle() {

	}

	public void exec(TitaVo titaVo) throws LogicException {

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM047", "放款分期協議案件明細_內部控管",
				"LM047_放款分期協議案件明細_內部控管", "LM047放款分期協議案件明細_內部控管.xlsx", "協議控管表");

		int iEntdy = parse.stringToInteger(titaVo.get("ENTDY"));
		makeExcel.setValue(1, 2, "         " + iEntdy / 10000 + "年 " + iEntdy / 100 % 100 + "月 分期協議案件明細表");
		makeExcel.setValue(1, 23, "機密等級：密\n單位：元\n" + this.showRocDate(6) + "止");

		List<Map<String, String>> LM047List = null;

		try {
			LM047List = lM047ServiceImpl.findAll(titaVo);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM047ServiceImpl.testExcel error = " + errors.toString());
		}
		exportExcel(LM047List);
		long sno = makeExcel.close();
		//makeExcel.toExcel(sno);
	}

	private void exportExcel(List<Map<String, String>> LDList) throws LogicException {
		this.info("LM047Report exportExcel");
		if (LDList == null || LDList.isEmpty()) {
			makeExcel.setValue(4, 1, "本日無資料");
		} else {
			int row = 3;
			for (Map<String, String> tLDVo : LDList) {
				row++;
				makeExcel.setValue(row, 2, row - 3);

				for (int i = 0; i <= 20; i++) {
					String value = tLDVo.get("F" + i);

					switch (i) {
					case 0:
						makeExcel.setValue(row, i + 3, parse.stringToInteger(value));
						break;
					case 1:
						makeExcel.setValue(row, i + 3, String.format("%03d", parse.stringToInteger(value)));
						break;
					case 3:
					case 5:
					case 6:
					case 7:
					case 8:
					case 9:
					case 10:
						if (!value.isEmpty()) {
							makeExcel.setValue(row, i + 3, parse.stringToFloat(value), "#,##0");
						}
						break;
					case 4:
						makeExcel.setValue(row, i + 3, showStatus(value));
						break;
					case 13:
						makeExcel.setValue(row, i + 3, showTerm(value));
						break;
					case 14:
					case 15:
						makeExcel.setValue(row, i + 3, this.showRocDate(value, 1));
						break;
					default:
						makeExcel.setValue(row, i + 3, value);
						break;
					}
				}
			}
		}
	}

	private String showStatus(String xstatus) throws LogicException {
		String tmp = "";
		switch (xstatus) {
		case "0":
			tmp = "正常";
			break;
		case "1":
			tmp = "展期";
			break;
		case "2":
			tmp = "催收";
			break;
		case "3":
			tmp = "結案";
			break;
		case "4":
			tmp = "正常";
			break;
		case "5":
			tmp = "催結";
			break;
		case "6":
			tmp = "呆帳";
			break;
		case "7":
			tmp = "部呆";
			break;
		case "8":
			tmp = "債轉";
			break;
		case "9":
			tmp = "呆結";
			break;
		default:
			break;
		}
		return tmp;
	}

	private String showTerm(String data) throws LogicException {
		String tmp = "";
		String str[] = data.split(";");

		if (str.length == 0) {
			str = new String[] { "", "", "" };
			return tmp;
		}

		if (!str[0].isEmpty() && !str[0].equals("0")) {
			tmp = tmp + str[0] + "年";
		}
		if (!str[1].isEmpty() && !str[1].equals("0")) {
			tmp = tmp + str[1] + "月";
		}
		if (!str[2].isEmpty() && !str[2].equals("0")) {
			tmp = tmp + str[2] + "天";
		}
		return tmp;
	}
}
