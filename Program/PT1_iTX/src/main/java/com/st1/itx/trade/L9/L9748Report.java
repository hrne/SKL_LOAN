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
import com.st1.itx.db.service.springjpa.cm.L9748ServiceImpl;
import com.st1.itx.tradeService.CommBuffer;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.report.ReportUtil;

@Component
@Scope("prototype")
public class L9748Report extends CommBuffer {

	@Autowired
	private L9748ServiceImpl l9748ServiceImpl;

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


	private final String tranCode = "L9748";
	private final String tranName = "金檢查核火險資料";

	private List<Map<String, String>> resultList;

	public void exec(TitaVo titaVo) throws LogicException {
		this.titaVo = titaVo;
		makeExcel.setTitaVo(titaVo);
		dateUtil.init();
		exec();
	}

	@Override
	public void exec() throws LogicException {
		this.info("L9748Report exec");

		findData();

		report();

		showMsg();
	}

	private void findData() {
		this.info("L9748Report findData");
		try {
			resultList = l9748ServiceImpl.findAll(titaVo);
		} catch (LogicException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("l9748ServiceImpl.findAll error = " + errors.toString());
		}
	}

	private void report() throws LogicException {
		this.info("L9748Report report");
		ReportVo reportVo = ReportVo.builder().setRptDate(titaVo.getEntDyI()).setBrno(titaVo.getKinbr())
				.setRptCode(tranCode).setRptItem(tranName).build();

		makeExcel.open(titaVo, reportVo, tranName, "L9748_底稿_金檢查核火險資料.xlsx", "明細");

		if (resultList == null || resultList.isEmpty()) {
			makeExcel.setValue(2, 1, "查無資料");
		} else {
			int rowCursor = 2;
			for (Map<String, String> result : resultList) {
				makeExcel.setValue(rowCursor, 1, result.get("NowInsuNo"), "L");
				makeExcel.setValue(rowCursor, 2, result.get("PrevInsuNo"), "L");
				makeExcel.setValue(rowCursor, 3, result.get("CustName"), "L");
				makeExcel.setValue(rowCursor, 4, result.get("CustId"), "L");
				makeExcel.setValue(rowCursor, 5, rptUtil.showBcDate(result.get("InsuStartDate"), 0), "L");
				makeExcel.setValue(rowCursor, 6, rptUtil.showBcDate(result.get("InsuEndDate"), 0), "L");
				makeExcel.setValue(rowCursor, 7, result.get("InsuCate"), "L");
				makeExcel.setValue(rowCursor, 8, rptUtil.getBigDecimal(result.get("FireInsuCovrg")), "#,##0");
				makeExcel.setValue(rowCursor, 9, rptUtil.getBigDecimal(result.get("FireInsuPrem")), "#,##0");
				makeExcel.setValue(rowCursor, 10, rptUtil.getBigDecimal(result.get("EthqInsuCovrg")), "#,##0");
				makeExcel.setValue(rowCursor, 11, rptUtil.getBigDecimal(result.get("EthqInsuPrem")), "#,##0");
				makeExcel.setValue(rowCursor, 12, result.get("EmpName"), "L");
				makeExcel.setValue(rowCursor, 13, result.get("FireOfficer"), "L");
				makeExcel.setValue(rowCursor, 14, result.get("DueAmt"), "#,##0");
				makeExcel.setValue(rowCursor, 15, result.get("InsuCompany"), "L");
				makeExcel.setValue(rowCursor, 16, result.get("ClCode1"), "0");
				makeExcel.setValue(rowCursor, 17, result.get("ClCode2"), "00");
				makeExcel.setValue(rowCursor, 18, result.get("ClNo"), "0000000");
				rowCursor++;
			}
		}
		long excelNo = makeExcel.close();
		makeExcel.toExcel(excelNo);
	}

	private void showMsg() {
		this.info("L9748Report showMsg");
		// MSG帶入預設值
		String ntxbuf = titaVo.getTlrNo() + FormatUtil.padX("L9748", 60) + titaVo.getEntDyI();
		this.info("ntxbuf = " + ntxbuf);
		webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", ntxbuf,
				tranCode + tranName + "已完成", titaVo);
	}
}
