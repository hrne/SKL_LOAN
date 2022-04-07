package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM002ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")
public class LM002Report extends MakeReport {

	@Autowired
	LM002ServiceImpl lM002ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	Parse parse;

	@Override
	public void printHeader() {
	}

	@Override
	public void printTitle() {
	}

	public void exec(TitaVo titaVo) throws LogicException {

		this.info("LM002Report exec ...");
		exportExcel(titaVo);

	}

	private void exportExcel(TitaVo titaVo) throws LogicException {
		this.info("LM062Report exportExcel------");
		List<Map<String, String>> fnAllList = new ArrayList<>();

		// 民國年
		int rocYY = parse.stringToInteger(titaVo.get("ENTDY").substring(0, 4));
		int currentYear = rocYY + 1911;

		// 打開excel
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM002", "專案放款", "LM002-專案放款", "LM002_專案放款.xls", "預警申請表");
		// 工作表更名
		makeExcel.setSheet("預警申請表", rocYY + "預警");

		makeExcel.setValue(2, 2, rocYY - 2 + " 年 度");
		makeExcel.setValue(10, 2, rocYY - 1 + " 年 度");
		makeExcel.setValue(18, 2, rocYY + " 年 度");

		try {
			fnAllList = lM002ServiceImpl.doQuery(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM002ServiceImpl.findAll error = " + errors.toString());
		}

		if (fnAllList.size() > 0) {

			String[] F = new String[4];

			for (Map<String, String> tLDVo : fnAllList) {
				// F0 Year
				// F1 Type
				// F2 Month
				// F3 Amount

				for (int i = 0; i < 4; i++) {
					F[i] = tLDVo.get("F" + i);
				}

				try {
					// 第三排為當年分 為20
					// 第二排 12
					// 第一排 4
					makeExcel.setValue(20 - (currentYear - parse.stringToInteger(F[0])) * 8 + parse.stringToInteger(F[1]) - 1, parse.stringToInteger(F[2]) + 1, getBigDecimal(F[3]), "R");
				} catch (Exception e) {
					this.warn("LM002 trying to parse data failed: ");
					this.info("F0: " + F[0]);
					this.info("F1: " + F[1]);
					this.info("F2: " + F[2]);
					this.info("F3: " + F[3]);
				}

				// 從上到下每排啟動 excel formulas
				for (int y = 0; y < 3; y++) {
					for (int x = 0; x < 12; x++) {
						makeExcel.formulaCaculate(8 + y * 8, 2 + x);
					}
				}

			}

		} else {
			makeExcel.setValue(4, 2, "本日無資料");
		}

		makeExcel.close();
		//makeExcel.toExcel(sno);

	}

}
