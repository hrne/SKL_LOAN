package com.st1.itx.trade.L3;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.LoanOverdue;
import com.st1.itx.db.domain.LoanOverdueId;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.LoanOverdueService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.LoanCalcRepayIntCom;
import com.st1.itx.util.common.LoanCloseBreachCom;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.LoanSetRepayIntCom;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.BaTxVo;
import com.st1.itx.util.common.data.CalcRepayIntVo;
import com.st1.itx.util.common.data.LoanCloseBreachVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/*
 * L3922 結案試算
 * a.此功能供結案登錄前試算其應繳交之本金、利息、逾期息及違約金。
 */

/**
 * L3922 結案試算
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3922")
@Scope("prototype")
public class L3922 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public LoanBorMainService loanBorMainService;
	@Autowired
	public LoanOverdueService loanOverdueService;
	@Autowired
	public FacMainService facMainService;

	@Autowired
	Parse parse;
	@Autowired
	LoanCom loanCom;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	MakeReport makeReport;

	@Autowired
	BaTxCom baTxCom;
	@Autowired
	LoanSetRepayIntCom loanSetRepayIntCom;
	@Autowired
	LoanCalcRepayIntCom loanCalcRepayIntCom;
	@Autowired
	LoanCloseBreachCom loanCloseBreachCom;

	private ArrayList<BaTxVo> baTxList;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3922 ");
		this.totaVo.init(titaVo);
		loanSetRepayIntCom.setTxBuffer(this.txBuffer);
		loanCloseBreachCom.setTxBuffer(this.txBuffer);
		baTxCom.setTxBuffer(txBuffer);
		makeReport.setTxBuffer(txBuffer);
		// 取得輸入資料
		int iCustNo = this.parse.stringToInteger(titaVo.getParam("TimCustNo"));
		int iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		int iBormNo = this.parse.stringToInteger(titaVo.getParam("BormNo"));
		int iEntryDate = this.parse.stringToInteger(titaVo.getParam("EntryDate"));
		int iCaseCloseCode = this.parse.stringToInteger(titaVo.getParam("CaseCloseCode"));

		// work area
		List<LoanBorMain> lLoanBorMain = new ArrayList<LoanBorMain>();
		ArrayList<CalcRepayIntVo> lCalcRepayIntVo = new ArrayList<CalcRepayIntVo>();
		ArrayList<LoanCloseBreachVo> iListCloseBreach = new ArrayList<LoanCloseBreachVo>();
		ArrayList<LoanCloseBreachVo> oListCloseBreach = new ArrayList<LoanCloseBreachVo>();
		LoanOverdue tLoanOverdue;
		int wkFacmNoStart = 1;
		int wkFacmNoEnd = 999;
		int wkBormNoStart = 1;
		int wkBormNoEnd = 900;
		String oRemark = "";
		BigDecimal oLoanBal = BigDecimal.ZERO;
		int oIntStartDate = 9991231;
		int oIntEndDate = 0;
		BigDecimal oRate = BigDecimal.ZERO;
		String oBuildingRemark = "";
		BigDecimal oPrincipal = BigDecimal.ZERO;
		BigDecimal oInterest = BigDecimal.ZERO;
		BigDecimal oDelayInt = BigDecimal.ZERO;
		BigDecimal oBreachAmt = BigDecimal.ZERO;
		BigDecimal oSubTotal = BigDecimal.ZERO;
		BigDecimal oTotal = BigDecimal.ZERO;
		BigDecimal oTotal2 = BigDecimal.ZERO;
		BigDecimal oCloseBreachAmt = BigDecimal.ZERO;
		BigDecimal oCloseBreachAmtPaid = BigDecimal.ZERO;
		BigDecimal oCloseBreachAmtUnpaid = BigDecimal.ZERO;
		BigDecimal wkOvduPaidPrin = BigDecimal.ZERO;
		BigDecimal wkOvduPaidInt = BigDecimal.ZERO;
		BigDecimal wkExtraRepay = BigDecimal.ZERO;

		// work area 7.8 CaseCloseCode
		int wkOvduDate = 9991231;
		BigDecimal wkOvduPrinAmt = BigDecimal.ZERO;
		BigDecimal wkOvduIntAmt = BigDecimal.ZERO;
		BigDecimal wkOvduBreachAmt = BigDecimal.ZERO;
		BigDecimal wkOvduAmt = BigDecimal.ZERO;
		BigDecimal wkOvduPrinBal = BigDecimal.ZERO;
		BigDecimal wkOvduIntBal = BigDecimal.ZERO;
		BigDecimal wkOvduBreachBal = BigDecimal.ZERO;
		BigDecimal wkOvduBal = BigDecimal.ZERO;
		BigDecimal wkReduceInt = BigDecimal.ZERO;
		BigDecimal wkReduceBreach = BigDecimal.ZERO;
		BigDecimal wkBadDebtAmt = BigDecimal.ZERO;
		BigDecimal wkBadDebtBal = BigDecimal.ZERO;
		BigDecimal wkReplyReduceAmt = BigDecimal.ZERO;

		if (iFacmNo > 0) {
			wkFacmNoStart = iFacmNo;
			wkFacmNoEnd = iFacmNo;
		}

		if (iBormNo > 0) {
			wkBormNoStart = iBormNo;
			wkBormNoEnd = iBormNo;
		}

		// 查詢各項費用
		this.baTxList = new ArrayList<BaTxVo>();
		this.baTxList = baTxCom.settingUnPaid(iEntryDate, iCustNo, iFacmNo, iBormNo, 99, BigDecimal.ZERO, titaVo); // 99-費用全部(含未到期)

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 300; // 412 + 154 * 300 = 46612

		Slice<LoanBorMain> slLoanBorMain = loanBorMainService.bormCustNoEq(iCustNo, wkFacmNoStart, wkFacmNoEnd,
				wkBormNoStart, wkBormNoEnd, this.index, this.limit, titaVo);
		lLoanBorMain = slLoanBorMain == null ? null : slLoanBorMain.getContent();
		if (lLoanBorMain == null || lLoanBorMain.size() == 0) {
			throw new LogicException(titaVo, "E0001", "放款主檔"); // 查詢資料不存在
		}

		Boolean isCalcu = false;
		for (LoanBorMain ln : lLoanBorMain) {
			// 戶況 0: 正常戶1:展期2: 催收戶3: 結案戶4: 逾期戶5: 催收結案戶6: 呆帳戶7: 部分轉呆戶8: 債權轉讓戶9: 呆帳結案戶
			switch (iCaseCloseCode) { // 結案區分
			case 0: // 0:正常結案
			case 1: // 1:展期
			case 2: // 2:借新還舊
			case 3: // 3:轉催收
				if (!(ln.getStatus() == 0 || ln.getStatus() == 4)) {
					continue;
				}
				isCalcu = true;
				break;
			case 4: // 4:催收戶本人清償
			case 5: // 5:催收戶保證人代償
			case 6: // 6:催收戶強制執行
				if (ln.getStatus() != 2 && ln.getStatus() != 7) {
					continue;
				} else {
					// 查詢催收呆帳檔
					tLoanOverdue = loanOverdueService.findById(
							new LoanOverdueId(iCustNo, ln.getFacmNo(), ln.getBormNo(), ln.getLastOvduNo()), titaVo);
					if (tLoanOverdue == null) {
						throw new LogicException(titaVo, "E0006", "催收呆帳檔 Key = " + iCustNo + "-" + ln.getFacmNo() + "-"
								+ ln.getBormNo() + "-" + ln.getLastOvduNo()); // 鎖定資料時，發生錯誤
					}
					wkOvduPaidPrin = tLoanOverdue.getOvduPrinAmt().subtract(tLoanOverdue.getOvduPrinBal()); // 催收還款本金
					wkOvduPaidInt = tLoanOverdue.getOvduIntAmt().subtract(tLoanOverdue.getOvduIntBal()); // 催收還款利息
				}
				isCalcu = true;
			case 7: // 7:轉列呆帳
			case 8: // 8:催收部分轉呆
				if (ln.getStatus() != 2 && ln.getStatus() != 7) {
					continue;
				}
				isCalcu = true;
				break;
			default:
				throw new LogicException(titaVo, "E0010", "結案區分 = " + iCaseCloseCode); // 功能選擇錯誤
			}
			if (iCaseCloseCode <= 6) {
				loanCalcRepayIntCom = loanSetRepayIntCom.setRepayInt(ln, 0, iEntryDate, 1, iEntryDate, titaVo);
				loanCalcRepayIntCom.setCaseCloseFlag("Y");
				lCalcRepayIntVo = loanCalcRepayIntCom.getRepayInt(titaVo);
				oLoanBal = oLoanBal.add(ln.getLoanBal());
				oRate = ln.getStoreRate();
				oPrincipal = oPrincipal.add(loanCalcRepayIntCom.getPrincipal()).subtract(wkOvduPaidPrin);
				oInterest = oInterest.add(loanCalcRepayIntCom.getInterest()).subtract(wkOvduPaidInt);
				// 轉催收不含延滯息、違約金
				if (iCaseCloseCode == 3) {
					oDelayInt = BigDecimal.ZERO;
					oBreachAmt = BigDecimal.ZERO;
				} else {
					oDelayInt = oDelayInt.add(loanCalcRepayIntCom.getDelayInt());
					oBreachAmt = oBreachAmt.add(loanCalcRepayIntCom.getBreachAmt());
				}
				// 提前還款金額=> add 清償違約金 List
				wkExtraRepay = loanCalcRepayIntCom.getExtraAmt();
				LoanCloseBreachVo v = new LoanCloseBreachVo();
				v.setCustNo(ln.getCustNo());
				v.setFacmNo(ln.getFacmNo());
				v.setBormNo(ln.getBormNo());
				v.setExtraRepay(wkExtraRepay);
				v.setEndDate(iEntryDate);
				iListCloseBreach.add(v);

				for (CalcRepayIntVo c : lCalcRepayIntVo) {
					OccursList occursList = new OccursList();
					oIntStartDate = c.getStartDate() < oIntStartDate ? c.getStartDate() : oIntStartDate;
					oIntEndDate = c.getEndDate() > oIntEndDate ? c.getEndDate() : oIntEndDate;
					occursList.putParam("OOFacmNo", ln.getFacmNo());
					occursList.putParam("OOBormNo", ln.getBormNo());
					occursList.putParam("OOIntStartDate", c.getStartDate());
					occursList.putParam("OOIntEndDate", c.getEndDate());
					occursList.putParam("OOAmount", c.getAmount());
					occursList.putParam("OORate", c.getStoreRate());
					occursList.putParam("OOPrincipal", c.getPrincipal());
					occursList.putParam("OOInterest", c.getInterest());
					occursList.putParam("OODelayInt", c.getDelayInt());
					occursList.putParam("OOBreachAmt", c.getBreachAmt());
					occursList.putParam("OOCloseBreachAmt", c.getCloseBreachAmt());
					occursList.putParam("OOLoanBal", c.getLoanBal());
					// 將每筆資料放入Tota的OcList
					this.totaVo.addOccursList(occursList);
				}
			}
		}
		if (!isCalcu) {
			if (iCaseCloseCode < 4) {
				throw new LogicException(titaVo, "E3068", ""); // 該筆放款戶況非正常戶及逾期戶
			} else {
				throw new LogicException(titaVo, "E3069", ""); // 該筆放款戶況非催收戶
			}
		}

//		if (wkTotaCount == 0) {
//			throw new LogicException(titaVo, "E3070", ""); // 查無可計息的放款資料
//		}

		// 計算全部清償違約金 ；Y=>結案區分= 0, 4 ,5 ,6
		// 結案區分 0:正常 1:展期 2:借新還舊 3:轉催收 4:催收戶本人清償 5:催收戶保證人代償 6:催收戶強制執行 7:轉列呆帳
		// 8:催收部分轉呆 9:債權轉讓戶
		if (iCaseCloseCode == 0 || iCaseCloseCode == 4 || iCaseCloseCode == 5 || iCaseCloseCode == 6) {
			oListCloseBreach = loanCloseBreachCom.getCloseBreachAmtAll(iEntryDate, iCustNo, iFacmNo, iBormNo,
					iListCloseBreach, titaVo);
		}

		String BreachDescription = "";
		int wkFacmNo = 0;
		// 清償違約金
		if (oListCloseBreach != null && oListCloseBreach.size() > 0) {
			for (LoanCloseBreachVo v : oListCloseBreach) {
//				if (v.getCloseBreachAmt().compareTo(BigDecimal.ZERO) > 0) {
					oCloseBreachAmt = oCloseBreachAmt.add(v.getCloseBreachAmt());
					oCloseBreachAmtPaid = oCloseBreachAmtPaid.add(v.getCloseBreachAmtPaid());
					oCloseBreachAmtUnpaid = oCloseBreachAmtUnpaid.add(v.getCloseBreachAmtUnpaid());
					if (wkFacmNo != v.getFacmNo()) {
						wkFacmNo = v.getFacmNo();
						FacMain tFacMain = facMainService.findById(new FacMainId(iCustNo, v.getFacmNo()), titaVo);
						if (!BreachDescription.isEmpty()) {
							BreachDescription = BreachDescription + "。";
						}

						BreachDescription = BreachDescription + "額度:" + tFacMain.getFacmNo() + "，";

						int Prohibitperiod = 0;
						if (tFacMain.getProhibitMonth() > 0 && tFacMain.getFirstDrawdownDate() > 0) {
							dDateUtil.init();
							dDateUtil.setDate_1(tFacMain.getFirstDrawdownDate());
							dDateUtil.setMons(tFacMain.getProhibitMonth());
							Prohibitperiod = dDateUtil.getCalenderDay(); // 綁約期限
						}
						if (tFacMain.getProhibitMonth() == 0) {
							BreachDescription = BreachDescription + " 無";
						} else {
							BreachDescription = BreachDescription + makeReport.showDate("" + Prohibitperiod);
						}
					}
//				}
			}
		}

		this.totaVo.putParam("ORemark", oRemark);
		this.totaVo.putParam("OLoanBal", oLoanBal);
		this.totaVo.putParam("OIntStartDate", oIntStartDate == 9991231 ? 0 : oIntStartDate);
		this.totaVo.putParam("OIntEndDate", oIntEndDate);
		this.totaVo.putParam("ORate", oRate);
		this.totaVo.putParam("OBuildingRemark", oBuildingRemark);
		this.totaVo.putParam("OPrincipal", oPrincipal);
		this.totaVo.putParam("OInterest", oInterest);
		this.totaVo.putParam("ODelayInt", oDelayInt);
		this.totaVo.putParam("OBreachAmt", oBreachAmt);
		this.totaVo.putParam("OShortfall", baTxCom.getShortfall());
		this.totaVo.putParam("OShortfallInt", baTxCom.getShortfallInterest());
		this.totaVo.putParam("OShortfallPrin", baTxCom.getShortfallPrincipal());
		this.totaVo.putParam("OShortCloseBreach", baTxCom.getShortCloseBreach());
		this.totaVo.putParam("OExcessive", baTxCom.getExcessive());
		oSubTotal = oPrincipal.add(oInterest).add(oDelayInt).add(oBreachAmt).add(baTxCom.getShortfall())
				.subtract(baTxCom.getExcessive());
		this.totaVo.putParam("SubTotal", oSubTotal);
		this.totaVo.putParam("OModifyFee", baTxCom.getModifyFee());
		this.totaVo.putParam("OAcctFee", baTxCom.getAcctFee());
		this.totaVo.putParam("OFireFee", baTxCom.getFireFee());
		this.totaVo.putParam("OUnOpenfireFee", baTxCom.getUnOpenfireFee());
		this.totaVo.putParam("OOvduFireFee", baTxCom.getCollFireFee());
		this.totaVo.putParam("OLawFee", baTxCom.getLawFee());
		this.totaVo.putParam("OOvduLawFee", baTxCom.getCollLawFee());
		this.totaVo.putParam("OCloseBreachAmtpaid", oCloseBreachAmtPaid);
		oTotal = oSubTotal.add(baTxCom.getModifyFee()).add(baTxCom.getAcctFee()).add(baTxCom.getFireFee())
				.subtract(baTxCom.getUnOpenfireFee()).add(baTxCom.getLawFee()).add(baTxCom.getCollLawFee())
				.add(oCloseBreachAmtPaid);
		this.totaVo.putParam("OTotal", oTotal);
		this.totaVo.putParam("OCloseBreachAmtUnpaid", oCloseBreachAmtUnpaid);
		oTotal2 = oTotal.add(oCloseBreachAmtUnpaid);
		this.totaVo.putParam("OTotal2", oTotal2);

		if (iCaseCloseCode >= 7) {

			List<LoanOverdue> lLoanOverdue = new ArrayList<LoanOverdue>();

			if (iFacmNo > 0) {
				wkFacmNoStart = iFacmNo;
				wkFacmNoEnd = iFacmNo;
			}
			if (iBormNo > 0) {
				wkBormNoStart = iBormNo;
				wkBormNoEnd = iBormNo;
			}
			this.baTxList = baTxCom.settingUnPaid(this.txBuffer.getTxCom().getTbsdy(), iCustNo, iFacmNo, iBormNo, 0,
					BigDecimal.ZERO, titaVo); // 00-費用全部
			List<Integer> lStatus = new ArrayList<Integer>(); // 1:催收 2:部分轉呆 3:呆帳 4:催收回復
			lStatus.add(1);
			lStatus.add(2);
			Slice<LoanOverdue> slLoanOverdue = loanOverdueService.ovduCustNoRange(iCustNo, wkFacmNoStart, wkFacmNoEnd,
					wkBormNoStart, wkBormNoEnd, 1, 999, lStatus, 0, Integer.MAX_VALUE, titaVo);
			lLoanOverdue = slLoanOverdue == null ? null : slLoanOverdue.getContent();

			if (lLoanOverdue != null && lLoanOverdue.size() > 0) {
				for (LoanOverdue od : lLoanOverdue) {
					if (od.getOvduDate() != 0 && od.getOvduDate() < wkOvduDate) {
						wkOvduDate = od.getOvduDate();
					}
					wkOvduPrinAmt = wkOvduPrinAmt.add(od.getOvduPrinAmt());
					wkOvduIntAmt = wkOvduIntAmt.add(od.getOvduIntAmt());
					wkOvduBreachAmt = wkOvduBreachAmt.add(od.getOvduBreachAmt());
					wkOvduAmt = wkOvduAmt.add(od.getOvduAmt());
					wkOvduPrinBal = wkOvduPrinBal.add(od.getOvduPrinBal());
					wkOvduIntBal = wkOvduIntBal.add(od.getOvduIntBal());
					wkOvduBreachBal = wkOvduBreachBal.add(od.getOvduBreachBal());
					wkOvduBal = wkOvduBal.add(od.getOvduBal());
					wkReduceInt = wkReduceInt.add(od.getReduceInt());
					wkReduceBreach = wkReduceBreach.add(od.getReduceBreach());
					wkBadDebtAmt = wkBadDebtAmt.add(od.getBadDebtAmt());
					wkBadDebtBal = wkBadDebtBal.add(od.getBadDebtBal());
					wkReplyReduceAmt = wkReplyReduceAmt.add(od.getReplyReduceAmt());
				}
			}
		}

		this.totaVo.putParam("OOvDuTrfPrincipal", wkOvduPrinAmt);
		this.totaVo.putParam("OOvduAmt", wkOvduAmt);
		this.totaVo.putParam("OOvduBal", wkOvduBal);
		this.totaVo.putParam("OBadDebtAmt", wkBadDebtAmt);
		this.totaVo.putParam("OOvDuTrfInterest", wkOvduIntAmt);
		this.totaVo.putParam("OFireFee", baTxCom.getFireFee());
		this.totaVo.putParam("OLawFee", baTxCom.getLawFee());
		this.totaVo.putParam("OCollFireFee", baTxCom.getCollFireFee());
		this.totaVo.putParam("OCollLawFee", baTxCom.getCollLawFee());
		this.totaVo.putParam("OTmpFacmNoX", baTxCom.getTmpFacmNoX());
		this.totaVo.putParam("OBreachDescription", BreachDescription);

		this.addList(this.totaVo);
		return this.sendList();
	}
}