package com.st1.itx.trade.L4;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.ClBuildingService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.format.StringCut;
import com.st1.itx.util.parse.Parse;

/**
 * L4603Report1
 * 
 * @author ChihWei
 * @version 1.0.0
 */
@Component
@Scope("prototype")
public class L4603Report extends MakeReport {

	@Autowired
	DateUtil dateUtil;

	@Autowired
	Parse parse;

	@Autowired
	public CustMainService custMainService;

	@Autowired
	public ClBuildingService clBuildingService;

	@Autowired
	public LoanBorMainService loanBorMainService;

	private int reporttype = 0;

//	每頁筆數
	private int pageIndex = 48;

	private int pageCnt = 0;

	@Override
	public void printHeader() {

		if (reporttype == 1) {
			this.setFont(1, 8);
			printHeaderP1();
		} else {
			printHeaderP();
		}
		// 明細起始列(自訂亦必須)
		this.setBeginRow(5);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(60);

	}

	public void printHeaderP() {
		this.print(-2, 48, "火險通知作業明細", "C");
		this.print(-4, 1, "  戶號           擔保品代碼        原保單號碼        戶名                  通知種類");
	}

	public void printHeaderP1() {
		this.print(-2, 84, "續保資料錯誤明細表", "C");
		this.print(-4, 1,
				"  擔保品號碼    原保單號碼 戶號    額度 戶名                  新保險起日 新保險迄日     火險保額      火線保費      地震險保額      地震險保費       總保費      錯誤說明");
	}

	public void exec(TitaVo titaVo, List<OccursList> reportlist, int reporttype) throws LogicException {
		this.info("L4603Report exec");

		this.reporttype = reporttype;
		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L4603", "續保資料錯誤明細表", "續保資料錯誤明細表", "A4", "L");
		int pageCnt = 0;
		for (OccursList t : reportlist) {
			this.print(1, 3, t.get("ReportCClCode1") + "-" + FormatUtil.pad9(t.get("ReportCClCode2"), 2) + "-"
					+ FormatUtil.pad9(t.get("ReportCClNo"), 7));

			this.print(0, 16, t.get("ReportCPrevInsuNo"));
			this.print(0, 26, FormatUtil.pad9(t.get("ReportCCustNo"), 7));
			this.print(0, 34, FormatUtil.pad9(t.get("ReportCFacmNo"), 3));

			int nameLength = 20;
			if (StringCut.replaceLineUp(t.get("ReportCCustName")).length() < 20) {
				nameLength = StringCut.replaceLineUp(t.get("ReportCCustName")).length();
			}
			this.print(0, 38, StringCut.replaceLineUp(t.get("ReportCCustName")).substring(0, nameLength));

			this.print(0, 60, formatDate(parse.stringToInteger(t.get("ReportCNewInsuStartDate"))));
			this.print(0, 70, formatDate(parse.stringToInteger(t.get("ReportCNewInsuEndDate"))));
			this.print(0, 91, t.get("ReportCFireAmt"), "R");
			this.print(0, 104, t.get("ReportCFireFee"), "R");
			this.print(0, 119, t.get("ReportCEthqAmt"), "R");
			this.print(0, 134, t.get("ReportCEthqFee"), "R");
			this.print(0, 146, t.get("ReportCTotlFee"), "R");
			this.print(0, 153, t.get("ReportCErrMsg"));

			pageCnt++;

			if (pageCnt >= pageIndex) {
				pageCnt = 0;
				this.newPage();
			}
		}

		long sno = this.close();
		this.toPdf(sno);
	}

	public void exec1(TitaVo titaVo, List<OccursList> reportlist, List<OccursList> reportlist2, int reporttype)
			throws LogicException {
		this.info("L4603Report exec1");

		this.reporttype = reporttype;
		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L4603", "火險通知作業明細", "火險通知作業明細", "A4", "P");

		String OOLableA = "";
		for (OccursList t : reportlist) {
			setVal(t);
		}
		if (reportlist2.size() > 0) {
			this.newPage();
			this.print(-3, 48, "(已申請不列印書面通知書客戶)", "C");
			this.print(1, 1, "");
			pageCnt = 0;
			for (OccursList t : reportlist2) {
				setVal(t);
			}
		}

		long sno = this.close();
		this.toPdf(sno);
	}

	private String formatDate(int date) {
		String result = "";

		if (date > 19110000) {
			date = date - 19110000;
		}
		result = FormatUtil.pad9("" + date, 7);

		result = result.substring(0, 3) + "/" + result.substring(3, 5) + "/" + result.substring(5);

		return result;
	}

	private void setVal(OccursList t) {
		this.print(1, 3, FormatUtil.pad9(t.get("OOCustNo"), 7) + "-" + FormatUtil.pad9(t.get("OOFacmNo"), 3));
		this.print(0, 18, t.get("OOClCode1") + "-" + FormatUtil.pad9(t.get("OOClCode2"), 2) + "-"
				+ FormatUtil.pad9(t.get("OOClNo"), 7));
		this.print(0, 35, t.get("OOInsuNo"));

		int nameLength = 20;
		if (StringCut.replaceLineUp(t.get("OOCustName")).length() < 20) {
			nameLength = StringCut.replaceLineUp(t.get("OOCustName")).length();
		}
		this.print(0, 52, StringCut.replaceLineUp(t.get("OOCustName")).substring(0, nameLength));// 員工姓名

		this.print(0, 74, t.get("OONoticeFlagX"));

		pageCnt++;

		if (pageCnt >= pageIndex) {
			pageCnt = 0;
			this.newPage();
			this.print(-3, 48, "(已申請不列印書面通知書客戶)", "C");
			this.print(1, 1, "");
		}
	}

}
