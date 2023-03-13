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
import com.st1.itx.db.service.springjpa.cm.L9729ServiceImpl;
import com.st1.itx.db.service.springjpa.cm.L9729ServiceImpl.WorkType;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.StringCut;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")
public class L9729Report extends MakeReport {

	@Autowired
	L9729ServiceImpl l9729ServiceImpl;

	@Autowired
	DateUtil dateUtil;

	@Autowired
	Parse parse;

	String txcd = "L9729";
	String txname = "歷史封存資料搬運結果明細";

	WorkType workType;
	int inputDate;

	public boolean exec(TitaVo titaVo) throws LogicException {
		this.info("L9729Report exec start ...");

		workType = WorkType.getWorkTypeByHelp(titaVo.getParam("InputType"));

		// 存取 inputDate
		inputDate = parse.stringToInteger(titaVo.getParam("InputDate"));

		List<Map<String, String>> listL9729 = null;

		try {
			listL9729 = l9729ServiceImpl.findAll(workType.getCode(), titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("L9729ServiceImpl.findAll error = " + errors.toString());
		}

		return exportPdf(titaVo, listL9729);
	}

	@Override
	public void printHeader() {

		this.setCharSpaces(0);
		this.setFontSize(12);

		this.print(-1, 1, "  程式ID：" + this.getParentTranCode());
		this.print(-1, 68, "新光人壽保險股份有限公司", "C");
		this.print(-1, 123, "日  期：" + this.showBcDate(dateUtil.getNowStringBc(), 1));
		this.print(-2, 1, "  報  表：" + this.getRptCode());
		this.print(-2, 68, "歷史封存資料搬運結果明細", "C");
		this.print(-2, 123, "時  間：" + dateUtil.getNowStringTime().substring(0, 2) + ":"
				+ dateUtil.getNowStringTime().substring(2, 4) + ":" + dateUtil.getNowStringTime().substring(4, 6));
		this.print(-3, 123, "頁  數：" + this.getNowPage());
		this.print(-5, 1, String.format("  封存對象... %s ", workType.getDesc()));
		this.print(-4, 1, String.format("  執行日期... %s ", this.showRocDate(inputDate, 1))); // YYY/MM/DD
		/**
		 * ------------------------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6
		 * ---------------1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
		 */
		this.print(-7, 1, "   執行批次  封存／復原　資料表名              戶號     額度  撥款編號  結果  搬運結果說明");
		this.print(-8, 1,
				"  ========================================================================================================================================");

		this.setBeginRow(9);

		this.setMaxRows(29);
	}

	private Boolean exportPdf(TitaVo titaVo, List<Map<String, String>> list) throws LogicException {

		Boolean isSuccess = true;

		this.info("L9729Report exportPdf");

		ReportVo reportVo = ReportVo.builder().setRptDate(titaVo.getEntDyI()).setBrno(titaVo.getBrno()).setRptCode(txcd)
				.setRptItem(txname).setSecurity(this.getSecurity()).setRptSize("A4").setPageOrientation("L").build();
		this.open(titaVo, reportVo);

		if (list == null || list.isEmpty()) {
			// 本日無資料
			this.print(1, 1, "  本日無資料!");
			isSuccess = false;
		} else {
			for (Map<String, String> l9728Vo : list) {
				String route;
				String dataFrom = l9728Vo.get("DataFrom");
				String dataTo = l9728Vo.get("DataTo");

				if ("HISTORY".equals(dataFrom) && "ONLINE".equals(dataTo)) {
					route = "復原";
				} else if ("ONLINE".equals(dataFrom) && "HISTORY".equals(dataTo)) {
					route = "封存";
				} else {
					route = "紀錄錯誤";
				}
				this.print(1, 8, l9728Vo.get("BatchNo"), "C");
				this.print(0, 18, route, "C");
				this.print(0, 26, l9728Vo.get("TableName"));
				this.print(0, 48, l9728Vo.get("CustNo"));
				this.print(0, 57, l9728Vo.get("FacmNo"));
				this.print(0, 63, l9728Vo.get("BormNo"));
				this.print(0, 73, l9728Vo.get("Result"));
				this.print(0, 79, StringCut.stringCut(l9728Vo.get("Description"), 0, 59));
			}
		}

		// close as pdf
		// long sno =
		this.close();
		// this.toPdf(sno);

		return isSuccess;
	}
}
