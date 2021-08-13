package com.st1.itx.trade.LQ;

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
import com.st1.itx.db.service.springjpa.cm.LQ001ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LQ001Report extends MakeReport {
	private static final Logger logger = LoggerFactory.getLogger(LQ001Report.class);

	@Autowired
	LQ001ServiceImpl LQ001ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	public void exec(TitaVo titaVo) throws LogicException {
		this.info("===========in LQ001report.exec");
		exportExcel(titaVo);
	}

	private void exportExcel(TitaVo titaVo) throws LogicException {
		this.info("===========in exportExcel");
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LQ001", "營建署季報_購置住宅貸款餘額", "LQ001營建署季報_購置住宅貸款餘額",
				"購置住宅貸款餘額.xlsx", "LNM53P");

		List<Map<String, String>> findList = new ArrayList<>();

		try {
			findList = LQ001ServiceImpl.findAll(titaVo);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LQP001ServiceImpl.findDept error = " + errors.toString());
		}
		// 設定欄寬
		makeExcel.setWidth(1, 10);
		makeExcel.setWidth(2, 18);
		makeExcel.setWidth(3, 18);
		makeExcel.setWidth(4, 18);
		makeExcel.setWidth(5, 18);
		makeExcel.setWidth(6, 18);
		makeExcel.setWidth(7, 18);
		makeExcel.setWidth(8, 18);
		makeExcel.setWidth(9, 18);
		makeExcel.setWidth(10, 18);
		makeExcel.setWidth(11, 18);
		makeExcel.setWidth(12, 5);

		// 起始列數
		int i = 2;
	
		//百萬
		BigDecimal million = new BigDecimal("1000000");

		if (findList.size() > 0) {
			for (Map<String, String> tLDVo : findList) {
				this.info("LQ001.tLDVo=" + tLDVo.toString());
				// 縣市名稱
				makeExcel.setValue(i, 1, tLDVo.get("F0"), "C");

				// 季末住宅餘額
				BigDecimal f1 = tLDVo.get("F1") == null || tLDVo.get("F1").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F1"));

				makeExcel.setValue(i, 2, this.computeDivide(f1, million, 0),"#,##0","R");

				// 本季核准金額
				BigDecimal f2 = tLDVo.get("F2") == null || tLDVo.get("F2").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F2"));
				makeExcel.setValue(i, 3,
						this.computeDivide(f1, million, 0),"#,##0", "R");

				// 本季住宅筆數
				BigDecimal f3 = tLDVo.get("F3") == null || tLDVo.get("F3").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F3"));
				makeExcel.setValue(i, 4, f3, "R");

				// 季末平均利率
				BigDecimal f4 = tLDVo.get("F4") == null || tLDVo.get("F4").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F4"));
				makeExcel.setValue(i, 5, f4, "R");

				// 季末總利息
				BigDecimal f5 = tLDVo.get("F5") == null || tLDVo.get("F5").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F5"));
				makeExcel.setValue(i, 6, f5,"#,##0", "R");

				// 季增平均利率
				BigDecimal f6 = tLDVo.get("F6") == null || tLDVo.get("F6").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F6"));
				makeExcel.setValue(i, 7, f6, "R");

				// 本季總利息
				BigDecimal f7 = tLDVo.get("F7") == null || tLDVo.get("F7").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F7"));
				makeExcel.setValue(i, 8,  f7,"#,##0", "R");

				// 季增餘額
				BigDecimal f8 = tLDVo.get("F8") == null || tLDVo.get("F8").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F8"));
				makeExcel.setValue(i, 9,  f8,"#,##0","R");

				// 季增平均期數
				BigDecimal f9 = tLDVo.get("F9") == null || tLDVo.get("F9").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F9"));
				makeExcel.setValue(i, 10, f9, "R");

				// 本季筆數
				BigDecimal f10 = tLDVo.get("F10") == null || tLDVo.get("F10").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F10"));
				makeExcel.setValue(i, 11, f10, "R");

				// 最後一欄未知
				makeExcel.setValue(i, 12, 1, "R");

				this.info(i + ",f0=" + tLDVo.get("F0") + ",f1=" + f1 + ",f2=" + f2 + ",f3=" + f3 + ",f4=" + f4
						+ ",f5=" + f5 + ",f6=" + f6 + ",f7=" + f7 + ",f8=" + f8 + ",f9=" + f9 + ",f10=" + f10);
				i++;

			}
		} else {
			makeExcel.setValue(2, 2, "本日無資料");
		}
		// 設定列高
		makeExcel.setHeight(1, 18);
		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

}
