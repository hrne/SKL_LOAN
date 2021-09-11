package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.ClBuilding;
import com.st1.itx.db.domain.ClBuildingId;
import com.st1.itx.db.domain.InsuRenewMediaTemp;
import com.st1.itx.db.service.ClBuildingService;
import com.st1.itx.db.service.InsuRenewMediaTempService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.parse.Parse;

@Service("L4601")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4601 extends TradeBuffer {

	@Autowired
	public Parse parse;

	@Autowired
	public InsuRenewMediaTempService insuRenewMediaTempService;

	@Autowired
	public ClBuildingService clBuildingService;

	@Autowired
	public TotaVo totaA;

	@Autowired
	public TotaVo totaB;

	@Autowired
	public TotaVo totaC;

	private int errorACnt = 0;
	private int errorBCnt = 0;
	private int errorCCnt = 0;

	private int iInsuEndMonth = 0;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4601 ");
		this.totaVo.init(titaVo);

		iInsuEndMonth = parse.stringToInteger(titaVo.getParam("InsuEndMonth")) + 191100;
		String reportA = titaVo.getParam("ReportA");
		String reportB = titaVo.getParam("ReportB");
		String reportC = titaVo.getParam("ReportC");
//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
//		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
//		this.limit = 500;
		// 執行更新
		if ( titaVo.getParam("FunCd").equals("1")) {
			MySpring.newTask("L4601Batch", this.txBuffer, titaVo);
			this.addList(this.totaVo);
		}
		// 執行報表
		else {
			totaA.putParam("MSGID", "L461A");
			totaB.putParam("MSGID", "L461B");
			totaC.putParam("MSGID", "L461C");
			Slice<InsuRenewMediaTemp> slInsuRenewMediaTemp = insuRenewMediaTempService
					.fireInsuMonthRg(iInsuEndMonth + "", iInsuEndMonth + "", 0, Integer.MAX_VALUE, titaVo);
			if (slInsuRenewMediaTemp != null) {
				for (InsuRenewMediaTemp t : slInsuRenewMediaTemp.getContent()) {
					if (!"".equals(reportA) && !"".equals(t.getCheckResultA())) {
						String[] checkResultA = t.getCheckResultA().split(",");
						List<String> strListA = Arrays.asList(checkResultA);
						for (String checkA : strListA) {
							totaA = errorReportA(t, parse.stringToInteger(checkA), titaVo);
						}
					}
					if (!"".equals(reportB) && !"".equals(t.getCheckResultB())) {
						String[] checkResultB = t.getCheckResultA().split(",");
						List<String> strListB = Arrays.asList(checkResultB);
						for (String checkA : strListB) {
							totaB = errorReportB(t, parse.stringToInteger(checkA), titaVo);
						}
					}
					if (!"".equals(reportC) && !"".equals(t.getCheckResultC())) {
						String[] checkResultC = t.getCheckResultA().split(",");
						List<String> strListC = Arrays.asList(checkResultC);
						for (String checkC : strListC) {
							totaC = errorReportC(t, parse.stringToInteger(checkC), titaVo);
						}
					}
				}
			}
			this.addList(this.totaVo);
			this.info("errorACnt  = " + errorACnt);
			totaA.putParam("ErrorACnt", errorACnt);
			this.addList(totaA);

			this.info("ErrorBCnt  = " + errorBCnt);
			totaB.putParam("ErrorBCnt", errorBCnt);
			this.addList(totaB);

			this.info("errorCCnt  = ");
			totaC.putParam("ErrorCCnt", errorCCnt);
			this.addList(totaC);
		}

		this.info("totavoList L4601  = " + this.sendList());

		return this.sendList();
	}

	private TotaVo errorReportA(InsuRenewMediaTemp t, int errorCode, TitaVo titaVo) throws LogicException {
		this.info("ReportA Start, errorCode :" + +errorCode);
//			戶號 額度 擔保品序號 錯誤原因	總筆數
		OccursList occursListReport = new OccursList();
		occursListReport.putParam("ReportACustNo", t.getCustNo());
		occursListReport.putParam("ReportAFacmNo", t.getFacmNo());
		occursListReport.putParam("ReportAClCode1", t.getClCode1());
		occursListReport.putParam("ReportAClCode2", t.getClCode2());
		occursListReport.putParam("ReportAClNo", t.getClNo());
		if (errorCode == 10) {
			occursListReport.putParam("ReportAErrorMsg", "無此保單號碼");
		} else if (errorCode == 11) {
			occursListReport.putParam("ReportAErrorMsg", "總保費 = 0");
		} else if (errorCode == 12) {
			occursListReport.putParam("ReportAErrorMsg", "無此戶號額度");
		} else if (errorCode == 13) {
			occursListReport.putParam("ReportAErrorMsg", "已入通知檔");
		} else {
			occursListReport.putParam("ReportAErrorMsg", errorCode);
		}

		totaA.addOccursList(occursListReport);
		errorACnt = errorACnt + 1;
		return totaA;
	}

	private TotaVo errorReportB(InsuRenewMediaTemp t, int errorCode, TitaVo titaVo) throws LogicException {
		this.info("ReportB Start, errorCode :" + +errorCode);
//		戶號 額度 借款人 押品號碼 新保險單起日 新保險單迄日 原有保險單號 保險起日 保險迄日
		OccursList occursListReport = new OccursList();

		occursListReport.putParam("ReportBCustNo", t.getCustNo());
		occursListReport.putParam("ReportBFacmNo", t.getFacmNo());
		occursListReport.putParam("ReportBCustName", t.getLoanCustName());
		occursListReport.putParam("ReportBClCode1", t.getClCode1());
		occursListReport.putParam("ReportBClCode2", t.getClCode2());
		occursListReport.putParam("ReportBClNo", t.getClNo());
		occursListReport.putParam("ReportBNewInsuStartDate", t.getNewInsuStartDate());
		occursListReport.putParam("ReportBNewInsuEndDate", t.getNewInsuEndDate());
		occursListReport.putParam("ReportBPrevInsuNo", t.getInsuNo());
		occursListReport.putParam("ReportBInsuStartDate", t.getInsuStartDate());
		occursListReport.putParam("ReportBInsuEndDate", t.getInsuEndDate());
		ClBuilding tClBuilding = clBuildingService.findById(new ClBuildingId(parse.stringToInteger(t.getClCode1()),
				parse.stringToInteger(t.getClCode2()), parse.stringToInteger(t.getClNo())), titaVo);

		if (tClBuilding != null) {
			occursListReport.putParam("ReportBAddress", tClBuilding.getBdLocation().trim());
		} else {
			occursListReport.putParam("ReportBAddress", "");
		}
		totaB.addOccursList(occursListReport);

		errorBCnt = errorBCnt + 1;
		return totaB;
	}

	private TotaVo errorReportC(InsuRenewMediaTemp t, int errorCode, TitaVo titaVo) throws LogicException {
		this.info("ReportC Start, errorCode :" + +errorCode);
//		押品號碼 原保單號碼 戶號 額度 戶名 新保險起日 新保險迄日 火險保額 火線保費 地震險保額 地震險保費 總保費 錯誤說明
		OccursList occursListReport = new OccursList();
		occursListReport.putParam("ReportCClCode1", t.getClCode1());
		occursListReport.putParam("ReportCClCode2", t.getClCode2());
		occursListReport.putParam("ReportCClNo", t.getClNo());
		occursListReport.putParam("ReportCPrevInsuNo", t.getInsuNo());
		occursListReport.putParam("ReportCCustNo", t.getCustNo());
		occursListReport.putParam("ReportCFacmNo", t.getFacmNo());
		occursListReport.putParam("ReportCCustName", t.getLoanCustName());
		occursListReport.putParam("ReportCNewInsuStartDate", t.getNewInsuStartDate());
		occursListReport.putParam("ReportCNewInsuEndDate", t.getNewInsuEndDate());
		occursListReport.putParam("ReportCFireAmt", t.getNewFireInsuAmt());
		occursListReport.putParam("ReportCFireFee", t.getNewFireInsuFee());
		occursListReport.putParam("ReportCEthqAmt", t.getNewEqInsuAmt());
		occursListReport.putParam("ReportCEthqFee", t.getNewEqInsuFee());
		occursListReport.putParam("ReportCTotlFee", t.getNewTotalFee());
		if (errorCode == 31) {
			occursListReport.putParam("ReportCErrMsg", "此額度無正常戶／催收戶之撥款");
		} else {
			occursListReport.putParam("ReportCErrMsg", errorCode);
		}

		totaC.addOccursList(occursListReport);

		errorCCnt = errorCCnt + 1;
		return totaC;
	}
}