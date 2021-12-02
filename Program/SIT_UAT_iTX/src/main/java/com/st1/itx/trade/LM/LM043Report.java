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
import com.st1.itx.db.service.springjpa.cm.LM043ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class LM043Report extends MakeReport {

	@Autowired
	LM043ServiceImpl lM043ServiceImpl;

	@Autowired
	MakeExcel makeExcel;
	
	@Autowired
	Parse parse;

	@Override
	public void printTitle() {

	}

	int type = 0;// 種類，0: 正常戶，1: 催收戶。2: 逾期戶

	public void exec(TitaVo titaVo) throws LogicException {
		int entdy = parse.stringToInteger(titaVo.get("ENTDY"));
		int year = entdy / 10000;
		int month = entdy / 100 % 100;
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM043", "地區放款數_內部控管", "LM043地區放款數_內部控管", "LM043地區放款數_內部控管.xlsx", "N總額");
		makeExcel.setValue(1, 1, year  + "." + parse.IntegerToString(month, 2));
		List<Map<String, String>> LM043List = null;
		for (int i = 0; i < 3; i++) {// 3次，分別是正常戶，催收戶跟逾期戶
			type = i;
			try {
				LM043List = lM043ServiceImpl.findAll(i, titaVo);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.info("LM043ServiceImpl.testExcel error = " + errors.toString());
			}
			exportExcel(titaVo, LM043List);
		}
		long sno = makeExcel.close();
		//makeExcel.toExcel(sno);
	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> LDList) throws LogicException {
		this.info("LM043Report exportExcel");
		String yymm = titaVo.get("ENTDY");
		makeExcel.setValue(1, 1, yymm.substring(1, 4) + "." + yymm.substring(4, 6));
		int row = 2;
		BigDecimal entCode0 = BigDecimal.ZERO;// 個人戶餘額
		BigDecimal entCode1 = BigDecimal.ZERO;// 企金戶餘額
		BigDecimal cnt0 = BigDecimal.ZERO;// 個人戶件數
		BigDecimal cnt1 = BigDecimal.ZERO;// 企金戶件數
		int col = 0;
		switch (type) {// 控制正常戶，催收戶，逾期戶的輸出位置
		case 0:
			col = 2;
			break;
		case 1:
			col = 6;
			break;
		case 2:
			col = 12;
			break;
		default:
			col = 1;
		}
		for (Map<String, String> tLDVo : LDList) {

			if (row == 25) {// 個人戶小計
				if (type == 0) {
					makeExcel.setValue(row, col, "0", "R");
					makeExcel.setValue(row, col + 1, "0", "R");
					makeExcel.setValue(row, col + 2, entCode0, "#,##0", "R");
				} else {
					makeExcel.setValue(row, col, "0", "R");
					makeExcel.setValue(row, col + 1, "0", "R");
					makeExcel.setValue(row, col + 2, cnt0, "R");
					makeExcel.setValue(row, col + 3, entCode0, "#,##0", "R");
				}
				row++;
			}

			for (int i = 0; i <= 3; i++) {
				String value = tLDVo.get("F" + i);
				switch (i) {
				case 2:
					BigDecimal cnt = getBigDecimal(value);
					if (type != 0) {
						makeExcel.setValue(row, col + i, cnt, "R");
					}
					if (row <= 24) {
						cnt0 = cnt0.add(cnt);
					} else {
						cnt1 = cnt1.add(cnt);
					}
					break;
				case 3:
					BigDecimal prinBalance = getBigDecimal(value);
					if (type == 0) {
						makeExcel.setValue(row, col + 2, prinBalance, "#,##0", "R");
					} else {
						makeExcel.setValue(row, col + i, prinBalance, "#,##0", "R");
					}
					if (row <= 24) {
						entCode0 = entCode0.add(prinBalance);
					} else {
						entCode1 = entCode1.add(prinBalance);
					}
					break;
				default:
					makeExcel.setValue(row, col + i, value, "R");
				}
			} // for
			row++;
		} // for
		if (type == 0) {// 企金戶小計&總計
			makeExcel.setValue(row, col, "1", "R");
			makeExcel.setValue(row, col + 1, "0", "R");
			makeExcel.setValue(row, col + 2, entCode1, "#,##0", "R");
			row++;
			makeExcel.setValue(row, col + 1, "0", "R");
			makeExcel.setValue(row, col + 2, entCode0.add(entCode1), "#,##0", "R");
		} else {
			makeExcel.setValue(row, col, "1", "R");
			makeExcel.setValue(row, col + 1, "0", "R");
			makeExcel.setValue(row, col + 2, cnt1, "R");
			makeExcel.setValue(row, col + 3, entCode1, "#,##0", "R");
			row++;
			makeExcel.setValue(row, col + 1, "0", "R");
			makeExcel.setValue(row, col + 2, cnt0.add(cnt1), "R");
			makeExcel.setValue(row, col + 3, entCode0.add(entCode1), "#,##0", "R");
		}
	}

}