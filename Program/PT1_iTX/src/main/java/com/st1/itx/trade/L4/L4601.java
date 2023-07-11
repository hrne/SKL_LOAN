package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.InsuRenewMediaTemp;
import com.st1.itx.db.service.ClBuildingService;
import com.st1.itx.db.service.InsuRenewMediaTempService;
import com.st1.itx.db.service.springjpa.cm.L4601ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.parse.Parse;

@Service("L4601")
@Scope("prototype")
/**
 * 出表：重複投保 及 險種不足LN5811P
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
	L4601ServiceImpl l4601ServiceImpl;

	@Autowired
	public TotaVo totaA;
	@Autowired
	public TotaVo totaC;

	private int errorACnt = 0;
	private int errorCCnt = 0;

	private int iInsuEndMonth = 0;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4601 ");
		this.totaVo.init(titaVo);

		iInsuEndMonth = parse.stringToInteger(titaVo.getParam("InsuEndMonth")) + 191100;
		String reportA = titaVo.getParam("ReportA");
//		String reportB = titaVo.getParam("ReportB");
		String reportC = titaVo.getParam("ReportC");
//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
//		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
//		this.limit = 500;
		// 執行更新
		if (titaVo.getParam("FunCd").equals("1")) {
			this.totaVo.setWarnMsg("背景作業中,待處理完畢訊息通知");
			MySpring.newTask("L4601Batch", this.txBuffer, titaVo);
			this.addList(this.totaVo);
		}
		// 執行報表
		else {
			totaA.putParam("MSGID", "L461A");
//			totaB.putParam("MSGID", "L461B");
			totaC.putParam("MSGID", "L461C");
			Slice<InsuRenewMediaTemp> slInsuRenewMediaTemp = insuRenewMediaTempService
					.fireInsuMonthRg(iInsuEndMonth + "", iInsuEndMonth + "", 0, Integer.MAX_VALUE, titaVo);
			if (slInsuRenewMediaTemp != null) {
				for (InsuRenewMediaTemp t : slInsuRenewMediaTemp.getContent()) {
					if (!"".equals(reportA) && !"".equals(t.getCheckResultA())) {
						String[] checkResultA = t.getCheckResultA().split(",");
						List<String> strListA = Arrays.asList(checkResultA);
						for (String checkA : strListA) {
							errorReportA(t, parse.stringToInteger(checkA), titaVo);
						}
					}
				}
				List<Map<String, String>> list = null;
				try {
					list = l4601ServiceImpl.findNoMediaTemp(titaVo);
				} catch (Exception e) {
					this.error(e.getMessage());
				}
				if (list != null) {
					for (Map<String, String> m : list) {
						errorReportA1(m, titaVo);
					}
				}
				for (InsuRenewMediaTemp t : slInsuRenewMediaTemp.getContent()) {

					if (!"".equals(reportC) && !"".equals(t.getCheckResultC())) {
						String[] checkResultC = t.getCheckResultC().split(",");
						List<String> strListC = Arrays.asList(checkResultC);
						for (String checkC : strListC) {
							errorReportC(t, parse.stringToInteger(checkC), titaVo);
						}
					}
				}
			}
			this.addList(this.totaVo);
			this.info("errorACnt  = " + errorACnt);
			totaA.putParam("ErrorACnt", errorACnt);
			this.addList(totaA);

//			this.info("ErrorBCnt  = " + errorBCnt);
//			totaB.putParam("ErrorBCnt", errorBCnt);
//			this.addList(totaB);

			this.info("errorCCnt  = " + errorCCnt);
			totaC.putParam("ErrorCCnt", errorCCnt);
			this.addList(totaC);
			// 產重複投保報表
			MySpring.newTask("L4601Batch", this.txBuffer, titaVo);
		}

		this.info("totavoList L4601  = " + this.sendList());

		return this.sendList();
	}

	private void errorReportA(InsuRenewMediaTemp t, int errorCode, TitaVo titaVo) throws LogicException {
		this.info("ReportA Start, errorCode :" + +errorCode);
//			戶號 額度 擔保品序號 錯誤原因	總筆數
		OccursList occursListReport = new OccursList();
		occursListReport.putParam("ReportACustNo", t.getCustNo());
		occursListReport.putParam("ReportAFacmNo", t.getFacmNo());
		occursListReport.putParam("ReportAClCode1", t.getClCode1());
		occursListReport.putParam("ReportAClCode2", t.getClCode2());
		occursListReport.putParam("ReportAClNo", t.getClNo());
		occursListReport.putParam("ReportAPrevInsuNo", t.getInsuNo());
		if (errorCode == 10) {
			occursListReport.putParam("ReportAErrorMsg", "無此保單號碼");
		} else if (errorCode == 11) {
			occursListReport.putParam("ReportAErrorMsg", "總保費 = 0");
		} else if (errorCode == 12) {
			occursListReport.putParam("ReportAErrorMsg", "此戶號額度，無火險單續保檔資料");
		} else if (errorCode == 13) {
			occursListReport.putParam("ReportAErrorMsg", "已入通知檔");
		} else if (errorCode == 14) {
			occursListReport.putParam("ReportAErrorMsg", "處理代碼非0.正常");
		} else if (errorCode == 15) {
			occursListReport.putParam("ReportAErrorMsg", "已入帳，總保費與入帳金額不符 ");
		} else if (errorCode == 16) {
			occursListReport.putParam("ReportAErrorMsg", "已自保");
		} else {
			occursListReport.putParam("ReportAErrorMsg", errorCode);
		}

		totaA.addOccursList(occursListReport);
		errorACnt = errorACnt + 1;
	}

	private void errorReportA1(Map<String, String> m, TitaVo titaVo) throws LogicException {
		this.info("ReportA1 Start :");
//			戶號 額度 擔保品序號 錯誤原因	總筆數
		OccursList occursListReport = new OccursList();
		occursListReport.putParam("ReportACustNo", m.get("CustNo"));
		occursListReport.putParam("ReportAFacmNo", m.get("FacmNo"));
		occursListReport.putParam("ReportAClCode1", m.get("ClCode1"));
		occursListReport.putParam("ReportAClCode2", m.get("ClCode2"));
		occursListReport.putParam("ReportAClNo", m.get("ClNo"));
		occursListReport.putParam("ReportAPrevInsuNo", m.get("PrevInsuNo"));
		occursListReport.putParam("ReportAErrorMsg", "無詢價資料");

		totaA.addOccursList(occursListReport);
		errorACnt = errorACnt + 1;
	}

	private void errorReportC(InsuRenewMediaTemp t, int errorCode, TitaVo titaVo) throws LogicException {
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
			occursListReport.putParam("ReportCErrMsg", "此額度已結案");
		} else if (errorCode == 32) {
			occursListReport.putParam("ReportCErrMsg", "此額度未撥款");
		} else {
			occursListReport.putParam("ReportCErrMsg", errorCode);
		}

		totaC.addOccursList(occursListReport);

		errorCCnt = errorCCnt + 1;
	}
}