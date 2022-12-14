package com.st1.itx.trade.L4;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.util.common.CustNoticeCom;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ExcelFontStyleVo;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Component("L4454Report4")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4454Report4 extends MakeReport {

	@Autowired
	public MakeExcel makeExcel;

	@Autowired
	public WebClient webClient;

	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public Parse parse;

	ExcelFontStyleVo fontStyleVo;

	@Autowired
	public CustMainService custMainService;

	@Autowired
	CustNoticeCom custNoticeCom;

	@Override
	public void printTitle() {

	}

	public void exec(TitaVo titaVo, List<Map<String, String>> tList) throws LogicException {

		this.info("交寄大宗限時掛號及掛號函件執據存根2聯單 Excel Start...");
		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "L4454";
		String fileItem = "交寄大宗限時掛號及掛號函件執據存根名單";
		String fileName = "交寄大宗限時掛號及掛號函件執據存根名單";
//		String expFileName = "交寄大宗限時掛號及掛號函件執據存根名單";
//		String defaultExcel = "一年內新貸件扣款失敗表-底稿.xlsx";
//		String defaultSheet = "LAW7U1Pqp";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName);
//		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L4454", "交寄大宗限時掛號及掛號函件執據存根名單",
//				"交寄大宗限時掛號及掛號函件執據存根名單","交寄大宗限時掛號及掛號函件執據存根名單");

		fontStyleVo = new ExcelFontStyleVo();

		fontStyleVo.setFont((short) 1); // 字體 : 標楷體

		fontStyleVo.setSize((short) 12); // 字體大小 : 12

		makeExcel.setValue(1, 1, "姓名", fontStyleVo);
		makeExcel.setValue(1, 2, "地址", fontStyleVo);

		makeExcel.setWidth(1, 22);
		makeExcel.setWidth(2, 54);

		int printRow = 2; // 從第二行開始印

		for (Map<String, String> t : tList) {

			int custNo = parse.stringToInteger(t.get("CustNo"));
			String currAddress = "";

			CustMain custMain = custMainService.custNoFirst(custNo, custNo, titaVo);

			if (custMain != null) {
				currAddress = custNoticeCom.getCurrAddress(custMain, titaVo);
				makeExcel.setValue(printRow, 1, custMain.getCustName());
				makeExcel.setValue(printRow, 2, currAddress);
			} else {
				makeExcel.setValue(printRow, 1, "");
				makeExcel.setValue(printRow, 2, "");
			}
			printRow++;
		}

		makeExcel.close();
//		makeExcel.toExcel(sno);
	}
}