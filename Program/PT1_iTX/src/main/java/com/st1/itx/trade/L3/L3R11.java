package com.st1.itx.trade.L3;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.FacClose;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.LoanOverdue;
import com.st1.itx.db.domain.LoanOverdueId;
import com.st1.itx.db.service.FacCloseService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.LoanOverdueService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.LoanCalcRepayIntCom;
import com.st1.itx.util.common.LoanCloseBreachCom;
import com.st1.itx.util.common.LoanSetRepayIntCom;
import com.st1.itx.util.common.data.LoanCloseBreachVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * L3R11 結案試算
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3R11")
@Scope("prototype")
public class L3R11 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	private LoanBorMainService loanBorMainService;
	@Autowired
	private LoanOverdueService loanOverdueService;
	@Autowired
	private FacCloseService facCloseService;

	@Autowired
	private Parse parse;
	@Autowired
	private DateUtil dDateUtil;
	@Autowired
	private BaTxCom baTxCom;
	@Autowired
	private LoanSetRepayIntCom loanSetRepayIntCom;
	@Autowired
	private LoanCalcRepayIntCom loanCalcRepayIntCom;
	@Autowired
	private LoanCloseBreachCom loanCloseBreachCom;

	private List<LoanBorMain> lLoanBorMain = new ArrayList<LoanBorMain>();
	private String iTxCode;
	private int iFKey;
	private int iCustNo;
	private int iFacmNo;
	private int iBormNo;
	private int iEntryDate;
	private int iCaseCloseCode;
	private String wkCollectFlag = ""; // 是否領取清償證明(Y/N)
	private String wkCloseReasonCode = "00";
	private int wkFacmNoStart = 1;
	private int wkFacmNoEnd = 999;
	private int wkBormNoStart = 1;
	private int wkBormNoEnd = 900;
	private int wkTotaCount = 0;
	private int oRpFacmNo = 0;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3R11 ");
		this.totaVo.init(titaVo);
		loanSetRepayIntCom.setTxBuffer(txBuffer);
		baTxCom.setTxBuffer(txBuffer);
		loanCloseBreachCom.setTxBuffer(this.txBuffer);
		// 取得輸入資料
		iTxCode = titaVo.getParam("RimTxCode");
		iFKey = this.parse.stringToInteger(titaVo.getParam("RimFKey"));
		iCustNo = this.parse.stringToInteger(titaVo.getParam("RimCustNo"));
		iFacmNo = this.parse.stringToInteger(titaVo.getParam("RimFacmNo"));
		iBormNo = this.parse.stringToInteger(titaVo.getParam("RimBormNo"));
		iEntryDate = this.parse.stringToInteger(titaVo.getParam("RimEntryDate"));
		iCaseCloseCode = this.parse.stringToInteger(titaVo.getParam("RimCaseCloseCode"));
		// 清償作業
		if ("L2631".equals(iTxCode) || "L2632".equals(iTxCode)) {
			this.wkCollectFlag = titaVo.getParam("CollectFlag");
		}

		// work area
		if (iFacmNo > 0) {
			wkFacmNoStart = iFacmNo;
			wkFacmNoEnd = iFacmNo;
			oRpFacmNo = iFacmNo;
		}
		BigDecimal oPrincipal = BigDecimal.ZERO;
		BigDecimal oInterest = BigDecimal.ZERO;
		BigDecimal oDelayInt = BigDecimal.ZERO;
		BigDecimal oBreachAmt = BigDecimal.ZERO;
		BigDecimal oCloseBreachAmt = BigDecimal.ZERO;
		BigDecimal wkOvduPaidPrin = BigDecimal.ZERO;
		BigDecimal wkOvduPaidInt = BigDecimal.ZERO;
		BigDecimal wkExtraRepay = BigDecimal.ZERO;
		BigDecimal oCloseAmt = BigDecimal.ZERO;
		LoanOverdue tLoanOverdue;
		ArrayList<LoanCloseBreachVo> iListCloseBreach = new ArrayList<LoanCloseBreachVo>();
		ArrayList<LoanCloseBreachVo> oListCloseBreach = new ArrayList<LoanCloseBreachVo>();
		// 查詢各項費用
		baTxCom.settingUnPaid(iEntryDate, iCustNo, iFacmNo, iBormNo, 99, BigDecimal.ZERO, titaVo); // 99-費用全部(含未到期)
		// 清償違約金=1.清償違約金(領清償證明) or 2.清償違約金(立即收取)
//		oCloseBreachAmt = oCloseBreachAmt.add(loanCalcRepayIntCom.getCloseBreachAmtPaid());

		Slice<LoanBorMain> slLoanBorMain = loanBorMainService.bormCustNoEq(iCustNo, wkFacmNoStart, wkFacmNoEnd,
				wkBormNoStart, wkBormNoEnd, 0, Integer.MAX_VALUE, titaVo);
		lLoanBorMain = slLoanBorMain == null ? null : slLoanBorMain.getContent();
		if (lLoanBorMain == null || lLoanBorMain.size() == 0) {
			throw new LogicException(titaVo, "E0001", "放款主檔"); // 查詢資料不存在
		}
		// 戶況 0: 正常戶1:展期2: 催收戶3: 結案戶4: 逾期戶5: 催收結案戶6: 呆帳戶7: 部分轉呆戶8: 債權轉讓戶9: 呆帳結案戶
		for (LoanBorMain ln : lLoanBorMain) {
			if (iBormNo > 0 && ln.getBormNo() != iBormNo) {
				continue;
			}
			wkOvduPaidPrin = BigDecimal.ZERO;
			wkOvduPaidInt = BigDecimal.ZERO;
			if (iBormNo > 0) {
				switch (ln.getStatus()) {
				case 3:
					throw new LogicException(titaVo, "E3078",
							"撥款主檔 戶號 = " + iCustNo + " 額度編號 = " + ln.getFacmNo() + " 撥款序號 = " + ln.getBormNo()); // 該筆撥款戶況為結案戶
				case 5:
					throw new LogicException(titaVo, "E3079",
							"撥款主檔 戶號 = " + iCustNo + " 額度編號 = " + ln.getFacmNo() + " 撥款序號 = " + ln.getBormNo()); // 該筆撥款戶況為催收結案戶
				case 9:
					throw new LogicException(titaVo, "E3080",
							"撥款主檔 戶號 = " + iCustNo + " 額度編號 = " + ln.getFacmNo() + " 撥款序號 = " + ln.getBormNo()); // 該筆撥款戶況為呆帳結案戶
				}
			} else {
				if (ln.getStatus() == 3 || ln.getStatus() == 5 || ln.getStatus() == 9) {
					continue;
				}
			}
			switch (iCaseCloseCode) { // 結案區分
			case 0: // 0:正常結案
			case 1: // 1:展期-一般
			case 2: // 2:展期-協議
			case 3: // 3:轉催收
				if (!(ln.getStatus() == 0 || ln.getStatus() == 4)) {
					if (iBormNo > 0) {
						throw new LogicException(titaVo, "E3068",
								"撥款主檔 戶號 = " + iCustNo + " 額度編號 = " + ln.getFacmNo() + " 撥款序號 = " + ln.getBormNo()); // 該筆放款戶況非正常戶及逾期戶
					} else {
						continue;
					}
				}
				break;
			case 4: // 4:催收戶本人清償
			case 5: // 5:催收戶保證人代償
			case 6: // 6:催收戶強制執行
				if (ln.getStatus() != 2 && ln.getStatus() != 7) {
					if (iBormNo > 0) {
						throw new LogicException(titaVo, "E3069",
								"撥款主檔 戶號 = " + iCustNo + " 額度編號 = " + ln.getFacmNo() + " 撥款序號 = " + ln.getBormNo()); // 該筆放款戶況非催收戶
					} else {
						continue;
					}
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
				break;
			case 7: // 7:轉列呆帳
			case 8: // 8:催收部分轉呆
				// case 9: // 9:債權轉讓戶
				if (!(ln.getStatus() == 2 || ln.getStatus() == 7)) {
					if (iBormNo > 0) {
						throw new LogicException(titaVo, "E3069",
								"撥款主檔 戶號 = " + iCustNo + " 額度編號 = " + ln.getFacmNo() + " 撥款序號 = " + ln.getBormNo()); // 該筆放款戶況非催收戶
					} else {
						continue;
					}
				}
				break;
			default:
				throw new LogicException(titaVo, "E0010", "結案區分 = " + iCaseCloseCode); // 功能選擇錯誤
			}
			if (ln.getActFg() == 1 && iFKey == 0) {
				throw new LogicException(titaVo, "E0021",
						"放款主檔 戶號 = " + ln.getCustNo() + " 額度編號 =  " + ln.getFacmNo() + " 撥款序號 = " + ln.getBormNo()); // 該筆資料待放行中
			}

			loanCalcRepayIntCom = loanSetRepayIntCom.setRepayInt(ln, 0, iEntryDate, 1, iEntryDate, titaVo);
			loanCalcRepayIntCom.setCaseCloseFlag("Y");

			loanCalcRepayIntCom.getRepayInt(titaVo);

			// 自本金利息內扣除催收還款金額
			oPrincipal = oPrincipal.add(loanCalcRepayIntCom.getPrincipal()).subtract(wkOvduPaidPrin);
			oInterest = oInterest.add(loanCalcRepayIntCom.getInterest()).subtract(wkOvduPaidInt);
			// 轉催收不含延滯息、違約金
			if (iEntryDate < ln.getMaturityDate()) {
				wkCloseReasonCode = "";
			}

			if (iCaseCloseCode == 3) {
				oDelayInt = BigDecimal.ZERO;
				oBreachAmt = BigDecimal.ZERO;
			} else {
				oDelayInt = oDelayInt.add(loanCalcRepayIntCom.getDelayInt());
				oBreachAmt = oBreachAmt.add(loanCalcRepayIntCom.getBreachAmt());
			}

			if (oRpFacmNo == 0) {
				oRpFacmNo = ln.getFacmNo();
			}
			wkTotaCount++;
			// 提前還款金額=> add 清償違約金 List
			wkExtraRepay = loanCalcRepayIntCom.getExtraAmt();
			LoanCloseBreachVo v = new LoanCloseBreachVo();
			v.setCustNo(ln.getCustNo());
			v.setFacmNo(ln.getFacmNo());
			v.setBormNo(ln.getBormNo());
			v.setExtraRepay(wkExtraRepay);
			v.setEndDate(iEntryDate);
			iListCloseBreach.add(v);

		}
		if (wkTotaCount == 0) {
			throw new LogicException(titaVo, "E3081", ""); // 無符合結案區分之撥款資料
		}
		//
		if (iCaseCloseCode == 0 || iCaseCloseCode == 4 || iCaseCloseCode == 5 || iCaseCloseCode == 6) {
			// 是否清償
			if (!("L2631".equals(iTxCode) || "L2632".equals(iTxCode))) {
				this.info("iTxCode = " + iTxCode);
				facCloseCheck(titaVo);
			}
			// 計算清償違約金
			if ("Y".equals(wkCollectFlag)) {
				oListCloseBreach = loanCloseBreachCom.getCloseBreachAmtAll(iCustNo, iFacmNo, iBormNo, iListCloseBreach,
						titaVo);
			} else {
				oListCloseBreach = loanCloseBreachCom.getCloseBreachAmtPaid(iCustNo, iFacmNo, iBormNo, iListCloseBreach,
						titaVo);
			}
			// 輸出清償違約金
			if (oListCloseBreach != null && oListCloseBreach.size() > 0) {
				for (LoanCloseBreachVo v : oListCloseBreach) {
					oCloseBreachAmt = oCloseBreachAmt.add(v.getCloseBreachAmt());
				}
			}
		}
		// 還款本金已含短繳本金回收金額，須扣除
		this.totaVo.putParam("L3r11Principal", oPrincipal.subtract(baTxCom.getShortfallPrincipal()));
		this.totaVo.putParam("L3r11Interest", oInterest);
		this.totaVo.putParam("L3r11DelayInt", oDelayInt);
		this.totaVo.putParam("L3r11BreachAmt", oBreachAmt);
		this.totaVo.putParam("L3r11Shortfall", baTxCom.getShortfall());
		this.totaVo.putParam("L3r11ShortfallInt", baTxCom.getShortfallInterest());
		this.totaVo.putParam("L3r11ShortfallPrin", baTxCom.getShortfallPrincipal());
		this.totaVo.putParam("L3r11ShortCloseBreach", baTxCom.getShortCloseBreach());
		this.totaVo.putParam("L3r11Excessive", baTxCom.getExcessive().add(baTxCom.getExcessiveOther()));
		this.totaVo.putParam("L3r11ModifyFee", baTxCom.getModifyFee());
		this.totaVo.putParam("L3r11AcctFee", baTxCom.getAcctFee());
		this.totaVo.putParam("L3r11FireFee", baTxCom.getFireFee());
		this.totaVo.putParam("L3r11LawFee", baTxCom.getLawFee());
		this.totaVo.putParam("L3r11CollLawFee", baTxCom.getCollLawFee());
		this.totaVo.putParam("L3r11CollFireFee", baTxCom.getCollFireFee());
		this.totaVo.putParam("L3r11CloseBreachAmt", oCloseBreachAmt);

		oCloseAmt = oCloseAmt.add(oPrincipal).add(oInterest).add(oDelayInt).add(oBreachAmt).add(baTxCom.getShortfall())
				.add(baTxCom.getAcctFee()).add(baTxCom.getModifyFee()).add(baTxCom.getFireFee())
				.add(baTxCom.getCollFireFee()).add(baTxCom.getLawFee()).add(baTxCom.getCollLawFee())
				.add(oCloseBreachAmt);

		// 清償要扣除未到期火險費、溢收款
		if ("L2631".equals(iTxCode) || "L2632".equals(iTxCode)) {
			oCloseAmt = oCloseAmt.subtract(baTxCom.getUnOpenfireFee()).subtract(baTxCom.getExcessive());
		}
		this.totaVo.putParam("L3r11CloseAmt", oCloseAmt);
		this.totaVo.putParam("L3r11CloseReasonCode", wkCloseReasonCode);
		this.totaVo.putParam("L3r11RpFacmNo", oRpFacmNo);

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 是否清償
	private void facCloseCheck(TitaVo titaVo) throws LogicException {
		// 結案區分 0:正常 1:展期-一般 2:展期-協議 3:轉催收 4:催收戶本人清償 5:催收戶保證人代償 6:催收戶強制執行 7:轉列呆帳
		// 8:催收部分轉呆 9:債權轉讓戶
		this.info("FacCloseCheck ...");
		if (iCaseCloseCode == 0 || iCaseCloseCode == 4 || iCaseCloseCode == 5 || iCaseCloseCode == 6) {
			boolean isAllClose = true;
			if (iBormNo > 0) {
				for (LoanBorMain ln : lLoanBorMain) {
					if (ln.getBormNo() != iBormNo && (ln.getStatus() == 0 || ln.getStatus() == 4)) {
						isAllClose = false;
						break;
					}
				}
			}
			// 清償作業檔
			if (isAllClose) {
				boolean isFindFacClose = false;
				Slice<FacClose> facCloseList = facCloseService.findCustNo(iCustNo, this.index, Integer.MAX_VALUE,
						titaVo);
				if (facCloseList != null) {
					for (FacClose tFacClose : facCloseList.getContent()) {

						if (tFacClose.getFacmNo() == iFacmNo && tFacClose.getEntryDate() == iEntryDate) {
							wkCloseReasonCode = tFacClose.getCloseReasonCode();
							wkCollectFlag = tFacClose.getCollectFlag();
							isFindFacClose = true;
							break;
						}
					}
				}
				if (!isFindFacClose) {
					throw new LogicException(titaVo, "E0001", "請先執行 L2631-清償作業"); // 查詢資料不存在
				}
			}
		}
	}
}