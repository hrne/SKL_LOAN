package com.st1.itx.trade.LY;

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
import com.st1.itx.db.service.springjpa.cm.LY004ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")
public class LY004Report extends MakeReport {

	@Autowired
	LY004ServiceImpl lY004ServiceImpl;

	@Autowired
	Parse parse;

	@Autowired
	MakeExcel makeExcel;

	public boolean exec(TitaVo titaVo) throws LogicException {

		int entdyf = titaVo.getEntDyI() + 19110000;

		int iYear = entdyf / 10000;
		
		int iMonth = entdyf % 10000;
		
		if(iMonth != 12) {
			iYear = iYear - 1;
		}
		
		int inputYearMonth = (iYear * 100) + 12;

		List<Map<String, String>> lY004List = null;

		try {
			lY004List = lY004ServiceImpl.findAll(inputYearMonth, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LY004ServiceImpl.testExcel error = " + errors.toString());
		}

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LY004", "非RBC_表14-4_會計部年度檢查報表", "LY004-非RBC_表14-4_會計部年度檢查報表", "LY004_底稿_非RBC_表14-4_會計部年度檢查報表.xlsx", "表14-4");

		// 通用處理
		// 設定年月份
		String iRoc = String.valueOf(inputYearMonth/100 - 1911);
		String iMon =String.valueOf(inputYearMonth% 100 - 1911);
		makeExcel.setValue(1, 1, String.format("新光人壽保險股份有限公司  %s年度(%s月)報表", iRoc,iMon));

		if (lY004List != null && !lY004List.isEmpty()) {
			// 有資料時處理

			for (Map<String, String> tLDVo : lY004List) {
				// F0~F12 在 row 6, col 2~14
				// F13~F25 在 row 13, col 3~15

				for (int i = 0; i <= 25; i++) {
					int col = i <= 12 ? i + 2 : i - 10;
					int row = i <= 12 ? 6 : 13;
					String valueStr = tLDVo.get("F" + i);
					BigDecimal valueNum = BigDecimal.ZERO;

					if (valueStr != null && !valueStr.isEmpty() && parse.isNumeric(valueStr)) {
						valueNum = getBigDecimal(valueStr);
					}

					// F4 是 % 數
					switch (i) {
					case 4:
						makeExcel.setValue(row, col, valueNum, "0.00%");
						break;
					default:
						makeExcel.setValue(row, col, valueNum, "#,##0");
						break;
					}
				}
			}
		} else {
			// 無資料時處理
			makeExcel.setValue(6, 2, "本日無資料!!");
			
			return false;
		}

		 makeExcel.close();
		 
		 return true;

	}
}
