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
import com.st1.itx.db.service.springjpa.cm.LM019ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class LM019Report extends MakeReport {

	@Autowired
	LM019ServiceImpl lM019ServiceImpl;

	@Autowired
	MakeExcel makeExcel;
	
	@Autowired
	Parse parse;

	@Override
	public void printTitle() {
	}

	public void exec(TitaVo titaVo) throws LogicException {
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM019", "利息收入明細表(印花稅)", "LM019-利息收入明細表(印花稅)", "LM019印花稅.xlsx", "10912明細");
		int yyyMM = parse.stringToInteger(titaVo.get("ENTDY").toString()) / 100;
		// sheet: 明細
		makeExcel.setSheet("10912明細", yyyMM + "明細");
		List<Map<String, String>> lM019List = null;
		try {
			lM019List = lM019ServiceImpl.findAll(titaVo, yyyMM + 191100);
			exportExcel(titaVo, lM019List);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM019ServiceImpl.testExcel error = " + errors.toString());
		}
		// sheet: 250元以上
		makeExcel.setSheet("10912(250以上)");
		makeExcel.setSheet("10912(250以上)", yyyMM + "(250以上)");
		List<Map<String, String>> lM019List_1 = null;
		try {
			lM019List_1 = lM019ServiceImpl.findAll_1(titaVo, yyyMM + 191100);
			exportExcel_1(titaVo, lM019List_1);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM019ServiceImpl.testExcel error = " + errors.toString());
		}
		// sheet: 放款部
		makeExcel.setSheet("10912放款部");
		makeExcel.setSheet("10912放款部", yyyMM + "放款部");
		List<Map<String, String>> lM019List_2 = null;
		try {
			lM019List_2 = lM019ServiceImpl.findAll_2(titaVo, yyyMM + 191100);
			exportExcel_2(titaVo, lM019List_2);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM019ServiceImpl.testExcel error = " + errors.toString());
		}
		makeExcel.close();
		//makeExcel.toExcel(sno);
	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> lDList) throws LogicException {
		if (lDList == null || lDList.isEmpty()) {
			makeExcel.setValue(2, 1, "本日無資料");
		} else {
			int row = 2;
			for (Map<String, String> tLDVo : lDList) {
				String value = "";
				for (int i = 0; i <= 3; i++) {
					String acnacc = "";
					value = tLDVo.get("F" + i);
					switch (i) {
					case 0:
						acnacc = value;
						break;
					case 1:
						makeExcel.setValue(row, i, acnacc + value);
						break;
					default:
						makeExcel.setValue(row, i, getBigDecimal(value));
						break;
					}
				}
				row++;
			}
		}
	}

	private void exportExcel_1(TitaVo titaVo, List<Map<String, String>> lDList) throws LogicException {
		if (lDList == null || lDList.isEmpty()) {
			makeExcel.setValue(2, 1, "本日無資料");
		} else {
			int row = 2;
			for (Map<String, String> tLDVo : lDList) {
				String value = "";
				for (int i = 0; i <= 4; i++) {
					String acnacc = "";
					value = tLDVo.get("F" + i);
					switch (i) {
					case 0:
						acnacc = value;
						break;
					case 1:
						makeExcel.setValue(row, i, acnacc + value);
						break;
					case 4:
						makeExcel.setValue(row, i, getBigDecimal(value), "#,##0.00");
						break;
					default:
						makeExcel.setValue(row, i, getBigDecimal(value));
						break;
					}
				}
				row++;
			}
		}
	}

	private void exportExcel_2(TitaVo titaVo, List<Map<String, String>> lDList) throws LogicException {
		String entdy = titaVo.get("ENTDY").toString();
		makeExcel.setValue(2, 1, entdy.substring(1, 4) + "/" + entdy.substring(4, 6) + "/01~" + entdy.substring(1, 4) + "/" + entdy.substring(4, 6) + "/" + entdy.substring(6, 8));
		if (lDList == null || lDList.isEmpty()) {
			makeExcel.setValue(5, 1, "本日無資料");
		} else {
			BigDecimal total[] = new BigDecimal[5];
			for (int i = 0; i < total.length; i++) {
				total[i] = BigDecimal.ZERO;
			}
			int row = 5;
			for (Map<String, String> tLDVo : lDList) {
				String value = "";
				for (int i = 0; i <= 6; i++) {
					String acnacc = "";
					value = tLDVo.get("F" + i);
					switch (i) {
					case 0:
						acnacc = value;
						break;
					case 1:
						makeExcel.setValue(row, i, acnacc + value);
						break;
					default:
						makeExcel.setValue(row, i, getBigDecimal(value), "#,##0");
						total[i - 2] = total[i - 2].add(getBigDecimal(value));
						break;
					}
				}
				row++;
			}
			for (int i = 0; i < 5; i++) {
				makeExcel.setValue(13, i + 2, total[i], "#,##0");
			}
		}
	}
}
