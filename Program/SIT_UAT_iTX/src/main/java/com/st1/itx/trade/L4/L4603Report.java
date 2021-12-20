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
	@Override
	public void printHeader() {

		if(reporttype == 1) {
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
		this.print(-2, 48, "續保資料錯誤明細表", "C");
		this.print(-4, 1, " 擔保品號碼     原保單號碼       戶號   額度 戶名                 新保險起日 新保險迄日           火險保額           火線保費         地震險保額         地震險保費             總保費  錯誤說明");
	}
	
	public void exec(TitaVo titaVo, List<OccursList> reportlist,int reporttype) throws LogicException {
		this.info("L4603Report exec");
		
		this.reporttype = reporttype;
		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L4603", "續保資料錯誤明細表", "續保資料錯誤明細表", "A4", "P");
		
		for(OccursList t : reportlist) {
			this.print(1, 3, t.get("ReportCClCode1"));
			this.print(0, 8, t.get("ReportCClCode2"));
			this.print(0, 12, t.get("ReportCClNo"));
			this.print(0, 15, t.get("ReportCPrevInsuNo"));
			this.print(0, 18, t.get("ReportCCustNo"));
			this.print(0, 25, t.get("ReportCFacmNo"));
			this.print(0, 40, t.get("ReportCCustName"));
			this.print(0, 50, t.get("ReportCNewInsuStartDate"));
			this.print(0, 50, t.get("ReportCNewInsuEndDate"));
			this.print(0, 50, t.get("ReportCFireAmt"));
			this.print(0, 50, t.get("ReportCFireFee"));
			this.print(0, 50, t.get("ReportCEthqAmt"));
			this.print(0, 50, t.get("ReportCEthqFee"));
			this.print(0, 50, t.get("ReportCTotlFee"));
			this.print(0, 50, t.get("ReportCErrMsg"));
		}
		
		long sno = this.close();
		this.toPdf(sno);
	}
	
	public void exec1(TitaVo titaVo, List<OccursList> reportlist,int reporttype) throws LogicException {
		this.info("L4603Report exec1");
		
		this.reporttype = reporttype;
		
		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L4603", "火險通知作業明細", "火險通知作業明細", "A4", "P");
		
		String OOLableA = "";
		for(OccursList t : reportlist) {
			this.print(1, 3, FormatUtil.pad9(t.get("OOCustNo"),7) + "-" + FormatUtil.pad9(t.get("OOFacmNo"), 3));
			this.print(0, 18, t.get("OOClCode1") + "-"+ FormatUtil.pad9(t.get("OOClCode2"),2) + "-" +FormatUtil.pad9(t.get("OOClNo"),7));
			this.print(0, 35, t.get("OOInsuNo"));
			
			int nameLength = 20;
			if (t.get("OOCustName").length() < 20) {
				nameLength = t.get("OOCustName").length();
			}
			this.print(0, 52, t.get("OOCustName").substring(0, nameLength));// 員工姓名
			
			switch(t.get("OOLableA")) {
			case "0":
				OOLableA = "火險通知單";
				break;
			case "1":
				OOLableA = "書面通知";
				break;
			case "2":
				OOLableA = "簡訊通知";
				break;
			case "3":
				OOLableA = "電子郵件";
				break;
			case "4":
				OOLableA = "不通知";
				break;
			default:
				break;
				
			}
			this.print(0, 74, OOLableA);
		}
		
		long sno = this.close();
		this.toPdf(sno);
	}
	
//	// 押品號碼 原保單號碼 戶號 額度 戶名 新保險起日 新保險迄日 火險保額 火線保費 地震險保額 地震險保費 總保費 錯誤說明
//	OccursList occursListReport = new OccursList();
//	occursListReport.putParam("ReportCClCode1", t.getClCode1());
//	occursListReport.putParam("ReportCClCode2", t.getClCode2());
//	occursListReport.putParam("ReportCClNo", t.getClNo());
//	occursListReport.putParam("ReportCPrevInsuNo", t.getPrevInsuNo());
//	occursListReport.putParam("ReportCCustNo", t.getCustNo());
//	occursListReport.putParam("ReportCFacmNo", t.getFacmNo());
//	occursListReport.putParam("ReportCCustName", custName);
//	occursListReport.putParam("ReportCNewInsuStartDate", t.getInsuStartDate());
//	occursListReport.putParam("ReportCNewInsuEndDate", t.getInsuEndDate());
//	occursListReport.putParam("ReportCFireAmt", t.getFireInsuCovrg());
//	occursListReport.putParam("ReportCFireFee", t.getFireInsuPrem());
//	occursListReport.putParam("ReportCEthqAmt", t.getEthqInsuCovrg());
//	occursListReport.putParam("ReportCEthqFee", t.getEthqInsuPrem());
//	occursListReport.putParam("ReportCTotlFee", t.getTotInsuPrem());
//	if ("31".equals(checkResultC)) {
//		occursListReport.putParam("ReportCErrMsg", "此額度已結案");
//	}
//	if ("32".equals(checkResultC)) {
//		occursListReport.putParam("ReportCErrMsg", "此額度未撥款");
//	}
}
