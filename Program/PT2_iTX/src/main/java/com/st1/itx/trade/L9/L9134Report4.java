package com.st1.itx.trade.L9;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxBizDate;
import com.st1.itx.db.service.TxBizDateService;
import com.st1.itx.db.service.springjpa.cm.L9134ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.parse.Parse;

@Component("L9134Report4")
@Scope("prototype")
public class L9134Report4 extends MakeReport {

	@Autowired
	L9134ServiceImpl l9134ServiceImpl;
	@Autowired
	private TxBizDateService iTxBizDateService;

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	Parse parse;

	public void exec(int endDate,TitaVo titaVo) throws LogicException {
		String reportCode = "L9134";
		String reportItem = "暫收款對帳-日調結表";
		String fileName = "暫收款對帳-日調結表";
		String defaultFile = "暫收款對帳-日調結表.xlsx";

		ReportVo reportVo = ReportVo.builder().setRptDate(endDate).setBrno(titaVo.getBrno()).setRptCode(reportCode)
				.setRptItem(reportItem).build();

		makeExcel.open(titaVo, reportVo, fileName, defaultFile, "工作表1");

		List<Map<String, String>> findList = new ArrayList<>();

		TxBizDate tTxBizDate = new TxBizDate();

		tTxBizDate = iTxBizDateService.findById("ONLINE", titaVo);

		// 上個月底日(民國年)
		int iLmnDy = tTxBizDate.getLmnDy();
		// 本營業日(民國年)
		int iTmnDy = tTxBizDate.getTmnDy();

		this.info("iLmnDy    = " + tTxBizDate.getLmnDy());
		this.info("iTmnDy    = " + tTxBizDate.getTbsDy());

		try {
			findList = l9134ServiceImpl.doQueryL9134_4(titaVo, iLmnDy, iTmnDy);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("l9134ServiceImpl.doQueryL9134_4 error = " + errors.toString());
		}
		int row = 5;

		makeExcel.setShiftRow(row, findList.size() - 1);

		if (findList.size() == 0 || findList.isEmpty()) {

			makeExcel.setValue(5, 1, "本日無資料");

		} else {

			for (Map<String, String> r : findList) {
				BigDecimal iTdBal = parse.stringToBigDecimal(r.get("TdBal"));
				BigDecimal iDifTdBal = parse.stringToBigDecimal(r.get("DifTdBal"));
				BigDecimal didTdBal = parse.stringToBigDecimal(r.get("didTdBal"));
				BigDecimal didDifTdBal = parse.stringToBigDecimal(r.get("didDifTdBal"));
				BigDecimal drAmt = parse.stringToBigDecimal(r.get("DrAmt"));
				BigDecimal crAmt = parse.stringToBigDecimal(r.get("CrAmt"));
//				int AcDate = parse.stringToInteger(r.get("AcDate"));

				makeExcel.setValue(row, 1, iTdBal, "#,##0");
				makeExcel.setValue(row, 2, iDifTdBal, "#,##0");
				makeExcel.setValue(row, 3, didTdBal, "#,##0");
				makeExcel.setValue(row, 4, didDifTdBal, "#,##0");
				makeExcel.setValue(row, 5, iDifTdBal.add(didDifTdBal), "#,##0");
				makeExcel.setValue(row, 6, r.get("AcDate"), "R");
				makeExcel.setValue(row, 12, drAmt, "#,##0");
				makeExcel.setValue(row, 13, crAmt, "#,##0");
				BigDecimal ix = crAmt.subtract(drAmt);
				BigDecimal ii = iDifTdBal.add(didDifTdBal);
				BigDecimal iy = ix.subtract(ii);
				BigDecimal io = BigDecimal.ZERO;
				if (iy.compareTo(io) == 0) {
					makeExcel.setValue(row, 14, "V");
				} else {
					makeExcel.setValue(row, 14, "X");
				}
				row++;
			}
		}
		makeExcel.close();
	}

}
