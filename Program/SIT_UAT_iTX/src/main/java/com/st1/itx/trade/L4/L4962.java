package com.st1.itx.trade.L4;

import java.util.ArrayList;
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
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdCodeId;
import com.st1.itx.db.domain.ClFac;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.InsuOrignal;
import com.st1.itx.db.domain.InsuRenew;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.InsuOrignalService;
import com.st1.itx.db.service.InsuRenewService;
import com.st1.itx.db.service.springjpa.cm.L4962ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L4962")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4962 extends TradeBuffer {
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public InsuRenewService insuRenewService;

	@Autowired
	public CustMainService custMainService;

	@Autowired
	public InsuOrignalService insuOrignalService;

	@Autowired
	public ClFacService sClFacService;

	@Autowired
	public L4962ServiceImpl l4962ServiceImpl;

	@Autowired
	public CdCodeService sCdCodeDefService;

	@Autowired
	public TotaVo totaA;

	@Autowired
	public TotaVo totaB;

	@Autowired
	public TotaVo totaC;

	private int cntA = 0;
	private int cntB = 0;
	private int cntC = 0;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4962 ");
		this.totaVo.init(titaVo);

		int iInsuEndMonthFrom = parse.stringToInteger(titaVo.getParam("InsuEndMonthFrom")) + 191100;
		int iInsuEndMonthTo = parse.stringToInteger(titaVo.getParam("InsuEndMonthTo")) + 191100;
		int insuStartDate = parse.stringToInteger(iInsuEndMonthFrom + "01");
//		int insuEndDate = parse.stringToInteger(iInsuEndMonthTo + "31");

		int iInsuEndMonthFrom1 = parse.stringToInteger(titaVo.getParam("InsuEndMonthFrom1")) + 191100;
		int iInsuEndMonthTo1 = parse.stringToInteger(titaVo.getParam("InsuEndMonthTo1")) + 191100;

		String flagA = titaVo.getParam("FlagA");
		String flagB = titaVo.getParam("FlagB");
		String CommericalFlag = titaVo.getParam("CommericalFlag");

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 200;

		Slice<InsuRenew> sInsuRenew = null;
		Slice<InsuOrignal> sInsuOrignal = null;

		List<InsuRenew> lInsuRenew = new ArrayList<InsuRenew>();

		List<InsuOrignal> lInsuOrignal = new ArrayList<InsuOrignal>();

		sInsuRenew = insuRenewService.findL4962A(iInsuEndMonthFrom, iInsuEndMonthTo, this.index, this.limit, titaVo);

		lInsuRenew = sInsuRenew == null ? null : sInsuRenew.getContent();

//		A.保費、保單未完成檢核表
//		年月 原保單號碼 戶號 額度 借款人 押品號碼 序號?? 新保單號碼 未完成狀況
//  		1.無新保單號碼 : 無新保單號碼
//  		2.保費未入帳 : 有新保單號碼，無會計日
		if (lInsuRenew != null && lInsuRenew.size() != 0) {
			for (InsuRenew tInsuRenew : lInsuRenew) {
				if (!"".equals(flagA)) {
					totaA.init(titaVo);
					if (tInsuRenew.getNowInsuNo() == null || "".equals(tInsuRenew.getNowInsuNo().trim())) {
						errorReportA(tInsuRenew, 1);
					} else if (tInsuRenew.getAcDate() == 0) {
						errorReportA(tInsuRenew, 2);
					}
				}
			}
		}

//			B.額度無保單檢核表
//			戶號 額度 借款人 首撥日 押品號碼 保單號碼(最末) 保險起日 保險迄日 說明
//				1.保單資料已到期 : 該月月底日前之所有保單(續保&正常)
//				2.無保單資料 : 無目前保單號碼者
		if ("Y".equals(flagB)) {
			totaB.init(titaVo);
//				1.CollList額度之戶況為0246者
//				2.clfac 1(房地)打頭者
//				3.於original檔無資料者
			List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

			try {
				// *** 折返控制相關 ***
				resultList = l4962ServiceImpl.findAll(this.index, this.limit, titaVo);
			} catch (Exception e) {
				this.error("l4962ServiceImpl findByCondition " + e.getMessage());
				throw new LogicException("E0013", e.getMessage());
			}

			if (resultList != null && resultList.size() > 0) {
//					/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
//					if (resultList.size() == this.limit && hasNext()) {
//						titaVo.setReturnIndex(this.setIndexNext());
//						/* 手動折返 */
//						this.totaVo.setMsgEndToEnter();
//					}

				for (Map<String, String> result : resultList) {
					String note = "";
					int startDate = parse.stringToInteger(result.get("F8"));
					int endDate = parse.stringToInteger(result.get("F9"));

					if (result.get("F10") == null || "".equals(result.get("F10").trim())) {
						note = "無保單資料";
					} else {
						if (endDate < insuStartDate) {
							note = "保單資料已到期";
						}
					}

					if (!"".equals(note)) {
						if (startDate > 19110000) {
							startDate = startDate - 19110000;
						}

						if (endDate > 19110000) {
							endDate = endDate - 19110000;
						}

						int drDwDate = parse.stringToInteger(result.get("F3"));

						if (drDwDate > 19110000) {
							drDwDate = drDwDate - 19110000;
						}

						OccursList occursListReport = new OccursList();
						occursListReport.putParam("ReportBCustNo", result.get("F0"));
						occursListReport.putParam("ReportBFacmNo", result.get("F1"));
						occursListReport.putParam("ReportBCustName", result.get("F2"));
						occursListReport.putParam("ReportBFirstDrDwDate", drDwDate);
						occursListReport.putParam("ReportBClCode1", result.get("F4"));
						occursListReport.putParam("ReportBClCode2", result.get("F5"));
						occursListReport.putParam("ReportBClNo", result.get("F6"));
						occursListReport.putParam("ReportBNowInsuNo", result.get("F7"));
						occursListReport.putParam("ReportBInsuStartDate", startDate);
						occursListReport.putParam("ReportBInsuEndDAte", endDate);
						occursListReport.putParam("ReportBErrMsg", note);
						totaB.addOccursList(occursListReport);

						cntB = cntB + 1;
					}
				}
			}
		}

		if (!"N".equals(CommericalFlag)) {

			sInsuRenew = insuRenewService.findL4962A(iInsuEndMonthFrom1, iInsuEndMonthTo1, this.index, this.limit,
					titaVo);

			lInsuRenew = sInsuRenew == null ? null : sInsuRenew.getContent();

			if (lInsuRenew != null && lInsuRenew.size() != 0) {
				for (InsuRenew tInsuRenew : lInsuRenew) {
					// 險種註記不為全部時 篩出特定種類資料
					if (!"00".equals(CommericalFlag)) {
						if (!CommericalFlag.equals(tInsuRenew.getCommericalFlag())) {
							continue;
						}
					}

//					if ("01".equals(tInsuRenew.getCommericalFlag())) { // 01 住宅險改商業險
//					totaC.init(titaVo);
//					if (tInsuRenew.getNowInsuNo() == null || "".equals(tInsuRenew.getNowInsuNo().trim())) {
//						errorReportC(tInsuRenew, 1, titaVo);
//					} else if (tInsuRenew.getAcDate() == 0) {
//						errorReportC(tInsuRenew, 2, titaVo);
//					}
//					} else 
					if (!"".equals(tInsuRenew.getCommericalFlag())) { // 00 全部
						totaC.init(titaVo);
						if (tInsuRenew.getNowInsuNo() == null || "".equals(tInsuRenew.getNowInsuNo().trim())) {
							errorReportC(tInsuRenew, 1, titaVo);
						} else if (tInsuRenew.getAcDate() == 0) {
							errorReportC(tInsuRenew, 2, titaVo);
						}
					}

				}
			}

			iInsuEndMonthFrom1 = iInsuEndMonthFrom1 * 100 + 1;
			iInsuEndMonthTo1 = iInsuEndMonthTo1 * 100 + 31;
			sInsuOrignal = insuOrignalService.insuEndDateRange(iInsuEndMonthFrom1, iInsuEndMonthTo1, this.index,
					this.limit, titaVo);

			lInsuOrignal = sInsuOrignal == null ? null : sInsuOrignal.getContent();

			if (lInsuOrignal != null && lInsuOrignal.size() != 0) {
				for (InsuOrignal tInsuOrignal : lInsuOrignal) {

					// 險種註記不為全部時 篩出特定種類資料
					if (!"00".equals(CommericalFlag)) {
						if (!CommericalFlag.equals(tInsuOrignal.getCommericalFlag())) {
							continue;
						}
					}
					
					if (!"".equals(tInsuOrignal.getCommericalFlag())) {
						totaC.init(titaVo);
						errorReportC2(tInsuOrignal, titaVo);
					}

				}
			}

			TotaVo t = totaC;
			t.getOccursList().sort((c1, c2) -> {
				int result = 0;
				if (c1.get("ReportCInsuEndMonth") != c2.get("ReportCInsuEndMonth")) {
					result = Integer.valueOf(c1.get("ReportCInsuEndMonth").compareTo(c2.get("ReportCInsuEndMonth")));
				} else {
					result = 0;
				}

				return result;
			});
			totaC = t;
		}

		totaA.putParam("MSGID", "L462A");
		totaB.putParam("MSGID", "L462B");
		totaC.putParam("MSGID", "L462C");
		totaA.putParam("OCntA", cntA);
		totaB.putParam("OCntB", cntB);
		totaC.putParam("OCntC", cntC);
		this.addList(totaA);
		this.addList(totaB);
		this.addList(totaC);
		return this.sendList();
	}
//	A.保費、保單未完成檢核表
//		年月 原保單號碼 戶號 額度 借款人 押品號碼 序號?? 新保單號碼 未完成狀況
//  		1.無新保單號碼 : 無新保單號碼
//  		2.保費未入帳 : 有新保單號碼，無會計日

	private void errorReportA(InsuRenew tInsuRenew, int errorFlag) {
		cntA = cntA + 1;

		CustMain tCustMain = new CustMain();
		tCustMain = custMainService.custNoFirst(tInsuRenew.getCustNo(), tInsuRenew.getCustNo());

		OccursList occursListReport = new OccursList();
		occursListReport.putParam("ReportAInsuEndMonth",
				tInsuRenew.getInsuYearMonth() > 191100 ? tInsuRenew.getInsuYearMonth() - 191100
						: tInsuRenew.getInsuYearMonth());
		occursListReport.putParam("ReportAPrevInsuNo", tInsuRenew.getPrevInsuNo());
		occursListReport.putParam("ReportACustNo", tInsuRenew.getCustNo());
		occursListReport.putParam("ReportAFacmNo", tInsuRenew.getFacmNo());
		occursListReport.putParam("ReportACustName", tCustMain.getCustName());
		occursListReport.putParam("ReportAClCode1", tInsuRenew.getClCode1());
		occursListReport.putParam("ReportAClCode2", tInsuRenew.getClCode2());
		occursListReport.putParam("ReportAClNo", tInsuRenew.getClNo());
		occursListReport.putParam("ReportANowInsuNo", tInsuRenew.getNowInsuNo());
		switch (errorFlag) {
		case 1:
			occursListReport.putParam("ReportAErrMsg", "無新保單號碼");
			break;
		case 2:
			occursListReport.putParam("ReportAErrMsg", "保費未入帳");
			break;
		default:
			occursListReport.putParam("ReportAErrMsg", "");
			break;
		}

		totaA.addOccursList(occursListReport);
	}

	private void errorReportC(InsuRenew tInsuRenew, int errorFlag, TitaVo titaVo) {
		cntC = cntC + 1;

		CustMain tCustMain = new CustMain();
		tCustMain = custMainService.custNoFirst(tInsuRenew.getCustNo(), tInsuRenew.getCustNo());

		OccursList occursListReport = new OccursList();
		occursListReport.putParam("ReportCInsuEndMonth",
				tInsuRenew.getInsuYearMonth() > 191100 ? tInsuRenew.getInsuYearMonth() - 191100
						: tInsuRenew.getInsuYearMonth());

		CdCode tCdCode = sCdCodeDefService.findById(new CdCodeId("CommericalFlag", tInsuRenew.getCommericalFlag()),
				titaVo);
		String CommericalFlagX = "";
		if (tCdCode != null) {
			CommericalFlagX = tCdCode.getItem();
		}
		this.info("InsuRenew_CommericalFlag = " + tInsuRenew.getCommericalFlag());
		this.info("InsuRenew_CommericalFlagX = " + CommericalFlagX);

		occursListReport.putParam("ReportCCommericalFlagX", CommericalFlagX);
		occursListReport.putParam("ReportCPrevInsuNo", tInsuRenew.getPrevInsuNo());
		occursListReport.putParam("ReportCCustNo", tInsuRenew.getCustNo());
		occursListReport.putParam("ReportCFacmNo", tInsuRenew.getFacmNo());
		occursListReport.putParam("ReportCCustName", tCustMain.getCustName());
		occursListReport.putParam("ReportCClCode1", tInsuRenew.getClCode1());
		occursListReport.putParam("ReportCClCode2", tInsuRenew.getClCode2());
		occursListReport.putParam("ReportCClNo", tInsuRenew.getClNo());
		occursListReport.putParam("ReportCNowInsuNo", tInsuRenew.getNowInsuNo());
		occursListReport.putParam("ReportCRemark", tInsuRenew.getRemark());
//		switch (errorFlag) {
//		case 1:
//			occursListReport.putParam("ReportCErrMsg", "無新保單號碼");
//			break;
//		case 2:
//			occursListReport.putParam("ReportCErrMsg", "保費未入帳");
//			break;
//		default:
//			occursListReport.putParam("ReportCErrMsg", "");
//			break;
//		}

		totaC.addOccursList(occursListReport);
	}

	private void errorReportC2(InsuOrignal tInsuOrignal, TitaVo titaVo) {
		cntC = cntC + 1;

		Slice<ClFac> sClFac = sClFacService.clNoEq(tInsuOrignal.getClCode1(), tInsuOrignal.getClCode2(),
				tInsuOrignal.getClNo(), this.index, this.limit, titaVo);

		int custno = 0;
		int facmno = 0;
		String custname = "";
		List<ClFac> lClFac = null;
		if (sClFac != null) {
			lClFac = sClFac.getContent();
		}

		if (lClFac != null) {
			custno = lClFac.get(0).getCustNo();
			facmno = lClFac.get(0).getFacmNo();
			CustMain tCustMain = custMainService.custNoFirst(custno, custno, titaVo);
			if (tCustMain != null) {
				custname = tCustMain.getCustName();
			}
		}

		OccursList occursListReport = new OccursList();
		occursListReport.putParam("ReportCInsuEndMonth", (tInsuOrignal.getInsuEndDate() / 100));

		CdCode tCdCode = sCdCodeDefService.findById(new CdCodeId("CommericalFlag", tInsuOrignal.getCommericalFlag()),
				titaVo);
		String CommericalFlagX = "";
		if (tCdCode != null) {
			CommericalFlagX = tCdCode.getItem();
		}
		this.info("InsuOrignal_CommericalFlag = " + tInsuOrignal.getCommericalFlag());
		this.info("InsuOrignal_CommericalFlagX = " + CommericalFlagX);

		occursListReport.putParam("ReportCCommericalFlagX", CommericalFlagX);
		occursListReport.putParam("ReportCPrevInsuNo", tInsuOrignal.getOrigInsuNo());
		occursListReport.putParam("ReportCCustNo", custno);
		occursListReport.putParam("ReportCFacmNo", facmno);
		occursListReport.putParam("ReportCCustName", custname);
		occursListReport.putParam("ReportCClCode1", tInsuOrignal.getClCode1());
		occursListReport.putParam("ReportCClCode2", tInsuOrignal.getClCode2());
		occursListReport.putParam("ReportCClNo", tInsuOrignal.getClNo());
		occursListReport.putParam("ReportCNowInsuNo", "");
		occursListReport.putParam("ReportCRemark", tInsuOrignal.getRemark());
//		switch (errorFlag) {
//		case 1:
//			occursListReport.putParam("ReportCErrMsg", "無新保單號碼");
//			break;
//		case 2:
//			occursListReport.putParam("ReportCErrMsg", "保費未入帳");
//			break;
//		default:
//			occursListReport.putParam("ReportCErrMsg", "");
//			break;
//		}

		totaC.addOccursList(occursListReport);
	}

}