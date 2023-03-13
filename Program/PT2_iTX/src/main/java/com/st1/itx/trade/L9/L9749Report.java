package com.st1.itx.trade.L9;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L9749ServiceImpl;
import com.st1.itx.tradeService.CommBuffer;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.report.ReportUtil;

@Component
@Scope("prototype")
public class L9749Report extends CommBuffer {

	@Autowired
	private L9749ServiceImpl l9749ServiceImpl;

	@Autowired
	private MakeExcel makeExcel;

	@Autowired
	private DateUtil dateUtil;

	@Autowired
	private ReportUtil rptUtil;

	@Autowired
	private WebClient webClient;

	@Autowired
	private DateUtil dDateUtil;


	private final String tranCode = "L9749";
	private final String tranName = "公平待客銀扣二扣資料";

	private List<Map<String, String>> resultList;

	public void exec(TitaVo titaVo) throws LogicException {
		this.titaVo = titaVo;
		makeExcel.setTitaVo(titaVo);
		dateUtil.init();
		exec();
	}

	@Override
	public void exec() throws LogicException {
		this.info("L9749Report exec");

		findData();

		report();

		showMsg();
	}

	private void findData() {
		this.info("L9749Report findData");
		try {
			resultList = l9749ServiceImpl.findAll(titaVo);
		} catch (LogicException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("L9749ServiceImpl.findAll error = " + errors.toString());
		}
	}

	private void report() throws LogicException {
		this.info("L9749Report report");
		ReportVo reportVo = ReportVo.builder().setRptDate(titaVo.getEntDyI()).setBrno(titaVo.getKinbr())
				.setRptCode(tranCode).setRptItem(tranName).build();

		makeExcel.open(titaVo, reportVo, tranName, "L9749_底稿_公平待客銀扣二扣資料.xlsx", "明細");

		if (resultList == null || resultList.isEmpty()) {
			makeExcel.setValue(2, 1, "查無資料");
		} else {
			int rowCursor = 2;
			for (Map<String, String> result : resultList) {
				makeExcel.setValue(rowCursor, 1, result.get("EntryDate"), "L");
				makeExcel.setValue(rowCursor, 2, result.get("CustNo"), "L");
				makeExcel.setValue(rowCursor, 3, result.get("FacmNo"), "L");
				makeExcel.setValue(rowCursor, 4, rptUtil.showBcDate(result.get("PrevIntDate"), 0), "L");
				makeExcel.setValue(rowCursor, 5, rptUtil.getBigDecimal(result.get("RepayAmt")), "#,##0");
				makeExcel.setValue(rowCursor, 6, result.get("ReturnCode"), "L");
				makeExcel.setValue(rowCursor, 7, rptUtil.showBcDate(result.get("AcDate"), 0), "L");
				makeExcel.setValue(rowCursor, 8, result.get("RepayType"), "L");
				rowCursor++;
			}
		}
		long excelNo = makeExcel.close();
		makeExcel.toExcel(excelNo);
	}

	private void showMsg() {
		this.info("L9749Report showMsg");
		// MSG帶入預設值
		String ntxbuf = titaVo.getTlrNo() + FormatUtil.padX("L9749", 60) + titaVo.getEntDyI();
		this.info("ntxbuf = " + ntxbuf);
		webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", ntxbuf,
				tranCode + tranName + "已完成", titaVo);
	}
}
