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

@Component
@Scope("prototype")

public class LM047Report extends MakeReport {
	// private static final Logger logger = LoggerFactory.getLogger(LM047Report.class);

	@Autowired
	LM047ServiceImpl lM047ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	public void exec(TitaVo titaVo) throws LogicException {

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM047", "放款分期協議案件明細_內部控管", "LM047_放款分期協議案件明細_內部控管", "LM047放款分期協議案件明細_內部控管.xlsx", "協議控管表");

		String entdy = titaVo.get("ENTDY").substring(1, 4) + "." + titaVo.get("ENTDY").substring(4, 6) + "." + titaVo.get("ENTDY").substring(6, 8);
		String yy = titaVo.get("ENTDY").substring(1, 4);
		String mm = String.valueOf(Integer.valueOf(titaVo.get("ENTDY").substring(4, 6)));

		makeExcel.setValue(1, 2, "         " + yy + "年 " + Integer.parseInt(mm) + "月 分期協議案件明細表");
		makeExcel.setValue(1, 23, "機密等級：密\n單位：元\n" + entdy + "止");

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
		makeExcel.toExcel(sno);
	}

	private void exportExcel(List<Map<String, String>> LDList) throws LogicException {
		this.info("LM047Report exportExcel");
		if(LDList.size() == 0) {
			makeExcel.setValue(4, 1, "本日無資料");
		}
		int row = 3;
		String ad = "";
		for (Map<String, String> tLDVo : LDList) {
			row++;
			makeExcel.setValue(row, 2, String.valueOf(row - 3));

			for (int i = 0; i < tLDVo.size(); i++) {
				ad = "F" + String.valueOf(i);
				switch (i) {
				case 0:
					makeExcel.setValue(row, i + 3, Integer.valueOf(tLDVo.get(ad)));
					break;
				case 1:
					makeExcel.setValue(row, i + 3, String.format("%03d", Integer.valueOf(tLDVo.get(ad))));
					break;
				case 3:
				case 5:
				case 6:
				case 7:
				case 8:
				case 9:
				case 10:
					if (!tLDVo.get(ad).equals("")) {
						makeExcel.setValue(row, i + 3, Float.valueOf(tLDVo.get(ad)), "#,##0");
					}
					break;
				default:
					if (i == 4) {
						makeExcel.setValue(row, i + 3, showStatus(tLDVo.get(ad)));
					} else if (i == 13) {
						makeExcel.setValue(row, i + 3, showTerm(tLDVo.get(ad)));
					} else if (i == 14 || i == 15) {
						makeExcel.setValue(row, i + 3, showDate(tLDVo.get(ad), 1));
					} else {
						makeExcel.setValue(row, i + 3, tLDVo.get(ad));
					}
					break;
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
		}
		return tmp;
	}

	// 顯示民國年
	private String showDate(String date, int iType) throws LogicException {
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
				return rocdatex.substring(0, 2) + "/" + rocdatex.substring(2, 4) + "/" + rocdatex.substring(4, 6);
			} else {
				return rocdatex.substring(0, 3) + "/" + rocdatex.substring(3, 5) + "/" + rocdatex.substring(5, 7);
			}
		} else if (iType == 2) {
			if (rocdatex.length() == 6) {
				return rocdatex.substring(0, 2) + "年" + rocdatex.substring(2, 4) + "月" + rocdatex.substring(4, 6) + "日";
			} else {
				return rocdatex.substring(0, 3) + "年" + rocdatex.substring(3, 5) + "月" + rocdatex.substring(5, 7) + "日";
			}
		} else {
			return rocdatex;
		}
	}

	private String showTerm(String data) throws LogicException {
		String tmp = "";
		String str[] = data.split(";");
		if(str.length == 0) {
			str = new String[] {"","",""};
		}
		if (!str[0].equals("") && !str[0].equals("0")) {
			tmp = tmp + str[0] + "年";
		}
		if (!str[1].equals("") && !str[1].equals("0")) {
			tmp = tmp + str[1] + "月";
		}
		if (!str[2].equals("") && !str[2].equals("0")) {
			tmp = tmp + str[2] + "天";
		}
		return tmp;
	}
}
