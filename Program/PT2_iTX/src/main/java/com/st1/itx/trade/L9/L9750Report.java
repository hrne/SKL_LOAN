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
import com.st1.itx.db.service.springjpa.cm.L9750ServiceImpl;
import com.st1.itx.tradeService.CommBuffer;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.report.ReportUtil;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")
public class L9750Report extends CommBuffer {

	@Autowired
	private L9750ServiceImpl l9750ServiceImpl;

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

	@Autowired
	private Parse parse;

	private final String tranCode = "L9750";
	private final String tranName = "會計師查核";
	private int iType = 0;
	private List<Map<String, String>> resultList;

	public void exec(TitaVo titaVo) throws LogicException {
		// 會計師查核種類:1.撥款案件-撥款日期 2.展期案件-撥款日期 3.契變案件-會計日期 4.清償案件-入帳日期
		this.titaVo = titaVo;
		iType = parse.stringToInteger(titaVo.get("Type"));
		makeExcel.setTitaVo(titaVo);
		dateUtil.init();
		exec();
	}

	@Override
	public void exec() throws LogicException {
		this.info("L9750Report exec");

		findData();

		report();

		showMsg();
	}

	private void findData() {
		this.info("L9750Report findData");

		if (iType == 1) {
			try {
				resultList = l9750ServiceImpl.findType1(titaVo);
			} catch (LogicException e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.error("l9750ServiceImpl.findType1 error = " + errors.toString());
			}
		}
		if (iType == 2) {
			try {
				resultList = l9750ServiceImpl.findType2(titaVo);
			} catch (LogicException e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.error("l9750ServiceImpl.findType2 error = " + errors.toString());
			}
		}
		if (iType == 3) {
			try {
				resultList = l9750ServiceImpl.findType3(titaVo);
			} catch (LogicException e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.error("l9750ServiceImpl.findType3 error = " + errors.toString());
			}
		}
		if (iType == 4) {
			try {
				resultList = l9750ServiceImpl.findType4(titaVo);
			} catch (LogicException e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.error("l9750ServiceImpl.findType4 error = " + errors.toString());
			}
		}
	}

	private void report() throws LogicException {
		this.info("L9750Report report");

		String formName = "";
		switch (iType) {
		case 1:
			formName = "(撥款案件)";
			break;
		case 2:
			formName = "(展期案件)";
			break;
		case 3:
			formName = "(契變案件)";
			break;
		case 4:
			formName = "(清償案件)";
			break;
		default:
			break;
		}
		
		ReportVo reportVo = ReportVo.builder().setRptDate(titaVo.getEntDyI()).setBrno(titaVo.getKinbr())
				.setRptCode(tranCode).setRptItem(tranName+formName).build();

		makeExcel.open(titaVo, reportVo, tranName+formName);

		printExcelHeader();

		if (resultList == null || resultList.isEmpty()) {
			makeExcel.setValue(2, 1, "查無資料");
		} else {
			int rowCursor = 2;
			for (Map<String, String> result : resultList) {
				if (iType == 1 || iType == 2) {
					makeExcel.setValue(rowCursor, 1, padStart(result.get("CustNo"), 7, "0"));
					makeExcel.setValue(rowCursor, 2, padStart(result.get("FacmNo"), 3, "0"));
					makeExcel.setValue(rowCursor, 3, padStart(result.get("BormNo"), 3, "0"));
					makeExcel.setValue(rowCursor, 4, rptUtil.showRocDate(result.get("DrawdownDate"), 1), "L");
					makeExcel.setValue(rowCursor, 5, rptUtil.getBigDecimal(result.get("DrawdownAmt")), "#,##0");
					makeExcel.setValue(rowCursor, 6, result.get("Item"), "L");
					makeExcel.setValue(rowCursor, 7, result.get("CompensateFlag"), "L");
				} else if (iType == 3) {
					makeExcel.setValue(rowCursor, 1, padStart(result.get("CustNo"), 7, "0"));
					makeExcel.setValue(rowCursor, 2, result.get("ContractChgCode"), "L");
					makeExcel.setValue(rowCursor, 3, result.get("Item"), "L");
					makeExcel.setValue(rowCursor, 4, rptUtil.showRocDate(result.get("OpenAcDate"), 1), "L");
				} else {
					makeExcel.setValue(rowCursor, 1, padStart(result.get("CustNo"), 7, "0"));
					makeExcel.setValue(rowCursor, 2, padStart(result.get("FacmNo"), 3, "0"));
					makeExcel.setValue(rowCursor, 3, rptUtil.showRocDate(result.get("EntryDate"), 1), "L");
					makeExcel.setValue(rowCursor, 4, result.get("CloseReasonCode"), "L");
					makeExcel.setValue(rowCursor, 5, result.get("Item"), "L");
				}
				rowCursor++;
			}
		}
		long excelNo = makeExcel.close();
		makeExcel.toExcel(excelNo);
	}

	private void showMsg() {
		this.info("L9750Report showMsg");
		// MSG帶入預設值
		String ntxbuf = titaVo.getTlrNo() + FormatUtil.padX("L9750", 60) + titaVo.getEntDyI();
		this.info("ntxbuf = " + ntxbuf);
		webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", ntxbuf,
				tranCode + tranName + "已完成", titaVo);
	}

	private void printExcelHeader() throws LogicException {

		if (iType == 1 || iType == 2) {
			makeExcel.setValue(1, 1, "戶號");
			makeExcel.setValue(1, 2, "額度編號");
			makeExcel.setValue(1, 3, "撥款序號");
			makeExcel.setValue(1, 4, "撥款日期");
			makeExcel.setValue(1, 5, "撥款金額");
			makeExcel.setValue(1, 6, "企金別");
			makeExcel.setValue(1, 7, "代償碼");
			makeExcel.setWidth(1, 15);
			makeExcel.setWidth(2, 15);
			makeExcel.setWidth(3, 15);
			makeExcel.setWidth(4, 15);
			makeExcel.setWidth(5, 20);
			makeExcel.setWidth(6, 15);
			makeExcel.setWidth(7, 15);
		} else if (iType == 3) {
			makeExcel.setValue(1, 1, "戶號");
			makeExcel.setValue(1, 2, "契約變更類型");
			makeExcel.setValue(1, 3, "契約變更中文");
			makeExcel.setValue(1, 4, "會計日");
			makeExcel.setWidth(1, 15);
			makeExcel.setWidth(2, 20);
			makeExcel.setWidth(3, 20);
			makeExcel.setWidth(4, 20);
		} else {
			makeExcel.setValue(1, 1, "戶號");
			makeExcel.setValue(1, 2, "額度編號");
			makeExcel.setValue(1, 3, "入帳日");
			makeExcel.setValue(1, 4, "提前清償原因");
			makeExcel.setValue(1, 5, "提前清償原因中文");
			makeExcel.setWidth(1, 15);
			makeExcel.setWidth(2, 15);
			makeExcel.setWidth(3, 15);
			makeExcel.setWidth(4, 15);
			makeExcel.setWidth(5, 20);
		}
	}

	private String padStart(String temp, int len, String tran) {
		if (temp.length() < len) {
			for (int i = temp.length(); i < len; i++) {
				temp = tran + temp;
			}
		}
		return temp;
	}

}
