package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM059ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ExcelFontStyleVo;

@Component
@Scope("prototype")

public class LM059Report extends MakeReport {
	private static final Logger logger = LoggerFactory.getLogger(LM059Report.class);

	@Autowired
	LM059ServiceImpl lM059ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	public void exec(TitaVo titaVo) throws LogicException {
 
		List<Map<String, String>> fnAllList = new ArrayList<>();

		this.info("LM059Report exec");

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM059", "表F22_會計部申報表", "LM059-表F22_會計部申報表",
				"LM059-表F22_會計部申報表.xlsx", "108.04.30");

		String iENTDY = titaVo.get("ENTDY");

		makeExcel.setSheet("108.04.30",
				iENTDY.substring(1, 4) + "." + iENTDY.substring(4, 6) + "." + iENTDY.substring(6, 8));

		ExcelFontStyleVo tmpStyle = new ExcelFontStyleVo();

		tmpStyle = tmpStyle.init();

		tmpStyle.setFont((short) 1);

		makeExcel.setValue(2, 5,
				"民國" + iENTDY.substring(1, 4) + "年" + iENTDY.substring(4, 6) + "月" + iENTDY.substring(6, 8) + "日",
				tmpStyle);

		try {
			fnAllList = lM059ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LM059ServiceImpl.findAll error = " + errors.toString());
		}

		ExcelFontStyleVo tmpStyle2 = new ExcelFontStyleVo();

		tmpStyle2 = tmpStyle2.init();

		tmpStyle2.setFont((short) 1);

		tmpStyle2.setBorderAll((short) 2);

		if (fnAllList.size() > 0) {
			Map<String, String> tLDVo = fnAllList.get(0);

			BigDecimal loanBal = tLDVo.get("F0") == null || tLDVo.get("F0").isEmpty() ? BigDecimal.ZERO
					: new BigDecimal(tLDVo.get("F0"));
			BigDecimal badDebt = tLDVo.get("F1") == null || tLDVo.get("F1").isEmpty() ? BigDecimal.ZERO
					: new BigDecimal(tLDVo.get("F1"));
			BigDecimal loanNet = tLDVo.get("F2") == null || tLDVo.get("F2").isEmpty() ? BigDecimal.ZERO
					: new BigDecimal(tLDVo.get("F2"));

			makeExcel.setValue(45, 4, loanBal, "#,##0", "R", tmpStyle2);
			makeExcel.setValue(46, 4, loanBal, "#,##0", "R", tmpStyle2);

			makeExcel.setValue(45, 5, badDebt, "#,##0", "R", tmpStyle2);
			makeExcel.setValue(46, 5, badDebt, "#,##0", "R", tmpStyle2);

			makeExcel.setValue(45, 6, loanNet, "#,##0", "R", tmpStyle2);
			makeExcel.setValue(46, 6, loanNet, "#,##0", "R", tmpStyle2);

		} else {
			makeExcel.setValue(7, 3, "本日無資料", tmpStyle2);
		}

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}
}
