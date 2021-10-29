package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM045ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class LM045Report extends MakeReport {

	@Autowired
	LM045ServiceImpl lM045ServiceImpl;

	@Autowired
	MakeExcel makeExcel;
	
	@Autowired
	Parse parse;

	@Override
	public void printTitle() {

	}

	private int yyymm = 0;

	public void exec(TitaVo titaVo) throws LogicException {

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM045", "年度催收逾放總額明細表_內部控管", "LM045_年度催收逾放總額明細表_內部控管", "LM045年度催收逾放總額明細表_內部控管.xlsx", "YYY年逾放總表");

		yyymm = parse.stringToInteger(titaVo.get("ENTDY")) / 100;
		makeExcel.setSize(36);
		makeExcel.setValue(1, 1, (yyymm / 100) + "年度催收逾放總額明細表");
		makeExcel.setSheet("YYY年逾放總表", (yyymm / 100) + "年逾放總表");

		List<Map<String, String>> LM045List = null;
		try {
			LM045List = lM045ServiceImpl.findAll(titaVo);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM045ServiceImpl.testExcel error = " + errors.toString());
		}

		exportExcel(LM045List);
	}

	private void exportExcel(List<Map<String, String>> LDList) throws LogicException {
		this.info("LM045Report exportExcel yyymm=" + yyymm); // yyymm

		String lastCollPsn = "";
		int collPsnCount = -1;

		int colShift = 0;

		BigDecimal[] lastYearAmounts = new BigDecimal[3];
		BigDecimal[] thisMonthAmounts = new BigDecimal[3];

		for (Map<String, String> tLDVo : LDList) {

			// F0 : 姓名/員編
			// F1 : 資料年月 YYYYMM
			// F2 : 超過清償期三個月
			// F3 : 催收款
			// F4 : 催收總額

			// Already ordered by CityCode, YearMonth in SQL query.

			// 第一筆輸出或是姓名/員編有變時，
			// 為新一大欄輸出所有年月和姓名/員編

			if (collPsnCount == -1 || !lastCollPsn.equals(tLDVo.get("F0"))) {

				lastCollPsn = tLDVo.get("F0");
				collPsnCount++;

				// 輸出年月 去年12月 - 今年12月

				for (int i = 0; i < 13; i++) {
					int ymOutput = yyymm / 100 * 100;
					
					if (i == 0)
					{
						ymOutput = ymOutput - 100 + 12; // 去年十二月
					} else
					{
						ymOutput += i;
					}
					
					makeExcel.setValue(3 + collPsnCount * 5, 3 + i, ymOutput);
				}

				// 輸出 "與yyy年底相較"

				makeExcel.setValue(3 + collPsnCount * 5, 16, "與" + (yyymm/100-1) + "年底相較");

				// 輸出姓名 / 員編

				makeExcel.setValue(4 + collPsnCount * 5, 1, lastCollPsn);

				// 重置 thisMonthAmounts, lastYearAmounts
				Arrays.fill(lastYearAmounts, BigDecimal.ZERO);
				Arrays.fill(thisMonthAmounts, BigDecimal.ZERO);
			}

			// 此筆資料年月餘額輸出

			colShift = (yyymm / 100 * 100 + 12) - (parse.stringToInteger(tLDVo.get("F1")) - 191100);
			this.info("LM045Report yyymm=" + yyymm);
			this.info("LM045Report dataYearMonth=" + tLDVo.get("F1"));
			this.info("LM045Report colShift=" + colShift);

			// YYY12 - dataYYYMM

			// 0 : 今年 12 月
			// 1 : 今年 11 月
			// ...
			// 11 : 今年 1 月

			// 100 : 去年 12 月

			makeExcel.setValue(4 + collPsnCount * 5, colShift == 100 ? 3 : 15 - colShift, getBigDecimal(tLDVo.get("F2")), "#,##0");
			makeExcel.setValue(5 + collPsnCount * 5, colShift == 100 ? 3 : 15 - colShift, getBigDecimal(tLDVo.get("F3")), "#,##0");
			makeExcel.setValue(6 + collPsnCount * 5, colShift == 100 ? 3 : 15 - colShift, getBigDecimal(tLDVo.get("F4")), "#,##0");

			// 登錄 lastYearAmounts, thisYearAmounts

			if (colShift == 100) {
				// 去年12月
				for (int i = 2; i < 5; i++) {
					lastYearAmounts[i - 2] = getBigDecimal(tLDVo.get("F" + i));
				}
			} else if (Integer.valueOf(tLDVo.get("F1")) == yyymm + 191100) {
				// 營業日當月

				for (int i = 2; i < 5; i++) {
					thisMonthAmounts[i - 2] = getBigDecimal(tLDVo.get("F" + i));
				}

				// 因為 query ordered by "YearMonth", 到這裡已經輸出完所有友資料的月份了
				// 直接算最底下的 " 與yyy年相較 " 的金額

				for (int i = 0; i < 3; i++) {
					makeExcel.setValue(4 + collPsnCount * 5 + i, 16, thisMonthAmounts[i].subtract(lastYearAmounts[i]), "#,##0");
				}
			}
		}

		// 設定所有行高 per collPsn

		for (int i = 0; i < collPsnCount + 1; i++) {
			for (int j = 0; j < 4; j++) {
				makeExcel.setHeight(3 + i * 5 + j, 40);
			}
		}

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);

	}

}
