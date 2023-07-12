package com.st1.itx.trade.LQ;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LQ005ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class LQ005Report extends MakeReport {

	@Autowired
	public LQ005ServiceImpl lQ005ServiceImpl;

	@Autowired
	Parse parse;

	@Autowired
	MakeExcel makeExcel;
	
	public void exec(TitaVo titaVo) throws LogicException {

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "LQ005";
		String fileItem = "表18-金融控股公司及其利害關係人交易分析表";
		String fileName = "LQ005-表18-金融控股公司及其利害關係人交易分析表";
		String defaultExcel = "LQ005_底稿_表18.xlsx";
		String defaultSheet = "YYY.MM.DD";

		int entdy = (parse.stringToInteger(titaVo.getParam("ENTDY")) + 19110000);

		int yymm = entdy / 100;

		this.info("reportVo open");

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();
		
		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);

		List<Map<String, String>> lq005List = new ArrayList<Map<String, String>>();

		try {
			
			lq005List = lQ005ServiceImpl.findAll(yymm, titaVo);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("lQ005ServiceImpl.findAll error = " + errors.toString());
		}

		makeExcel.setSheet("YYY.MM.DD", this.showRocDate(entdy, 6));
		int rocYMD = entdy - 19110000;
		int rocY = rocYMD / 10000;
		String rocM = String.format(rocYMD / 100 % 100 +"", "%0d");
		String rocD = String.format(rocYMD % 100 + "", "%0d");

		makeExcel.setValue(2, 1, "民國 " + rocY + " 年 " + rocM + " 月 " + rocD + " 日 ", "C");

		if (lq005List != null && !lq005List.isEmpty()) {

			// ex:size = 10,從第9列開始往下插入9列(10-1)
			makeExcel.setShiftRow(9, lq005List.size() - 1);

			int cnt = 0;// 筆數
			int row = 7;// 列數
			BigDecimal million = new BigDecimal("1000000");
			for (Map<String, String> r : lq005List) {
				cnt++;
				row++;
				// 交易序號
				makeExcel.setValue(row, 1, String.format(cnt + "", "%03d"), "C");
				// 交易對象別代號
				makeExcel.setValue(row, 2, "02", "C");
				// 提供者 交易對象名稱(代號)
				makeExcel.setValue(row, 3, "03458902", "C");
				makeExcel.setValue(row, 4, "新光人壽", "C");

				// 收受者 交易對象名稱(代號)
				makeExcel.setValue(row, 5, "B", "C");
				makeExcel.setValue(row, 6, r.get("Id"), "C");
				makeExcel.setValue(row, 7, r.get("Name"), "C");

				// 交易性質
				makeExcel.setValue(row, 8, "放款 02", "C");

				// 當季最高餘額或本年累積交易總額
				makeExcel.setValue(row, 9,
						this.getBigDecimal(r.get("MaxLoanBal")).divide(million).setScale(2, RoundingMode.HALF_UP),
						"#,##0.00", "R");

				// 季底交易帳列餘額
				makeExcel.setValue(row, 10,
						this.getBigDecimal(r.get("LoanBal")).divide(million).setScale(2, RoundingMode.HALF_UP),
						"#,##0.00", "R");

			}

			// 最後一筆的行數
			int endRow = row;
			row = row + 2;
			// 設定公式
			makeExcel.setFormula(row, 9, BigDecimal.ZERO, "SUM(I8:I" + endRow + ")", "#,##0.00");
			makeExcel.setFormula(row, 10, BigDecimal.ZERO, "SUM(J8:J" + endRow + ")", "#,##0.00");
			// 重整公式
			makeExcel.formulaCaculate(row, 9);
			makeExcel.formulaCaculate(row, 10);

		} else {
			makeExcel.setValue(8, 1, "本日無資料");
		}

		makeExcel.close();

	}

}
