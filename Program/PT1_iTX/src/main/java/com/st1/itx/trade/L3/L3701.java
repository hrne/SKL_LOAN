package com.st1.itx.trade.L3;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.domain.AcReceivableId;
import com.st1.itx.db.domain.BatxRateChange;
import com.st1.itx.db.domain.BatxRateChangeId;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.LoanBorMainId;
import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.domain.LoanBorTxId;
import com.st1.itx.db.domain.LoanOverdue;
import com.st1.itx.db.domain.LoanOverdueId;
import com.st1.itx.db.domain.LoanRateChange;
import com.st1.itx.db.domain.LoanRateChangeId;
import com.st1.itx.db.domain.TxTemp;
import com.st1.itx.db.domain.TxTempId;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.db.service.BatxRateChangeService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.db.service.LoanOverdueService;
import com.st1.itx.db.service.LoanRateChangeService;
import com.st1.itx.db.service.TxTempService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcDetailCom;
import com.st1.itx.util.common.AcReceivableCom;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.LoanDueAmtCom;
import com.st1.itx.util.common.PfDetailCom;
import com.st1.itx.util.common.data.PfDetailVo;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

/*
 * L3701 放款內容變更
 * a.此功能提供撥款後,如果撥款相關資料(如繳息週期,還本週期,利率區分等)有變動時,更改期資料用
 * b.如為催收戶,僅可變更繳款方式,扣款銀行,扣款帳號及郵局存款別.
 * c.還本週期如有變更,新推算出之下次還本日需>繳息迄日.例如:原還本週期為3個月,繳息期日=83/02/01,原下次還本日=83/03/01,如新還本週期變更為1個月,則新下次還本日即為83/01/01,小於繳息迄日,故將顯示錯誤訊息.
 * d.繳息週期如有變更,將重算總期數.
 * e.畫面中所顯示之初值,為檔案內目前之值.如同時變更兩筆以上撥款,其原始值不同時,則讀取任一筆顯示出,以供參考
 * f.放款內容變更為2段式交易,未放行前,該筆資料鎖住,不能做任何交易
 * g.延長年期、寬限期變更時要收取契變手續費、因此要連結[貸後契變手續費維護]。
 * h.警告訊息(warning):變更繳款方式前應確認期款已繳至最近一期，若無，需通知客戶繳入。
 */

/**
 * L3701 放款內容變更
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3701")
@Scope("prototype")
public class L3701 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public TxTempService txTempService;
	@Autowired
	public BatxRateChangeService batxRateChangeService;

	@Autowired
	public FacMainService facMainService;
	@Autowired
	public LoanBorTxService loanBorTxService;
	@Autowired
	public LoanBorMainService loanBorMainService;
	@Autowired
	public LoanOverdueService loanOverdueService;
	@Autowired
	public AcReceivableService acReceivableService;
	@Autowired
	public LoanRateChangeService loanRateChangeService;

	@Autowired
	Parse parse;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	LoanCom loanCom;
	@Autowired
	LoanDueAmtCom loanDueAmtCom;
	@Autowired
	AcDetailCom acDetailCom;
	@Autowired
	DataLog datalog;
	@Autowired
	AcReceivableCom acReceivableCom;
	@Autowired
	public PfDetailCom pfDetailCom;

	private TitaVo titaVo = new TitaVo();
	private int iCustNo;
	private int iFacmNo;
	private int iBormNo;
	private String iFacmOnlyFlag;
	private String iRateCodeY;
	private String iApproveRateY;
	private String iRateIncrY;
	private String iIndividualIncrY;
	private String iRateAdjFreqY;
	private String iFirstRateAdjDateY;
	private String iNextRateAdjDateY;
	private String iAcctCodeY;
	private String iMaturityDateY;
	private String iAmortizedCodeY;
	private String iPayIntFreqY;
	private String iRepayFreqY;
	private String iGraceDateY;
	private String iFirstDueDateY;
	private String iSpecificDdY;
	private String iAcctFeeY;
	private String iPieceCodeY;
	private String iPieceCodeSecondY;
	private String iPieceCodeSecondAmtY;
	private String iRemarkY;
	private String iProcessDateY;
	private String iIntCalcCodeY;
	private String iRemark;

	// work area
	private boolean updFacMain = false;
	private boolean updLoanBorMain = false;
	private boolean updLoanOverdue = false;
	private int wkTbsDy;
	private int wkCustNo = 0;
	private int wkFacmNo = 0;
	private int wkBormNo = 0;
	private int wkBorxNo = 0;
	private int wkOvduNo = 0;
	private int wkNewBorxNo = 0;
	private int wkFacmNoStart = 1;
	private int wkFacmNoEnd = 999;
	private int wkBormNoStart = 1;
	private int wkBormNoEnd = 900;
	private int wkTotaCount = 0;
	private int wkBeforeFacmNo = 0;
	private String wkBeforeAcctCode = "";
	private AcDetail acDetail;
	private TempVo tTemp1Vo = new TempVo(); // 其他欄位 LoanBorTX
	private TempVo tTemp2Vo = new TempVo(); // 其他欄位 TxTemp
	private FacMain tFacMain;
	private FacMain beforeFacMain;
	private LoanBorMain tLoanBorMain;
	private LoanBorMain beforeLoanBorMain;
	private LoanOverdue tLoanOverdue;
	private LoanOverdue beforeLoanOverdue;
	private LoanBorTx tLoanBorTx;
	private LoanBorTxId tLoanBorTxId;
	private AcReceivable tAcReceivable = new AcReceivable();
	private List<TxTemp> lTxTemp;
	private List<AcDetail> lAcDetail = new ArrayList<AcDetail>();
	private List<LoanBorMain> lLoanBorMain = new ArrayList<LoanBorMain>();
	private List<AcReceivable> lAcReceivable = new ArrayList<AcReceivable>();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3701 ");
		this.info("   ActFgI         = " + titaVo.getActFgI());
		this.info("   isActfgEntry   = " + titaVo.isActfgEntry());
		this.info("   isActfgSuprele = " + titaVo.isActfgSuprele());
		this.info("   isHcodeNormal  = " + titaVo.isHcodeNormal());
		this.info("   isHcodeErase   = " + titaVo.isHcodeErase());
		this.info("   isHcodeModify  = " + titaVo.isHcodeModify());
		this.info("   EntdyI         = " + titaVo.getEntDyI());
		this.info("   Kinbr          = " + titaVo.getKinbr());
		this.info("   TlrNo          = " + titaVo.getTlrNo());
		this.info("   Tno            = " + titaVo.getTxtNo());
		this.info("   OrgEntdyI      = " + titaVo.getOrgEntdyI());
		this.info("   OrgKin         = " + titaVo.getOrgKin());
		this.info("   OrgTlr         = " + titaVo.getOrgTlr());
		this.info("   OrgTno         = " + titaVo.getOrgTno());

		this.totaVo.init(titaVo);
		this.titaVo = titaVo;
		this.wkTbsDy = this.txBuffer.getTxCom().getTbsdy();

		// 取得輸入資料
		iCustNo = this.parse.stringToInteger(titaVo.getParam("TimCustNo"));
		iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		iBormNo = this.parse.stringToInteger(titaVo.getParam("BormNo"));
		iFacmOnlyFlag = titaVo.getParam("FacmOnlyFlag");
		iRateCodeY = titaVo.getParam("RateCodeY");
		iApproveRateY = titaVo.getParam("ApproveRateY");
		iRateIncrY = titaVo.getParam("RateIncrY");
		iIndividualIncrY = titaVo.getParam("IndividualIncrY");
		iRateAdjFreqY = titaVo.getParam("RateAdjFreqY");
		iFirstRateAdjDateY = titaVo.getParam("FirstRateAdjDateY");
		iNextRateAdjDateY = titaVo.getParam("NextRateAdjDateY");
		iAcctCodeY = titaVo.getParam("AcctCodeY");
		iMaturityDateY = titaVo.getParam("MaturityDateY");
		iAmortizedCodeY = titaVo.getParam("AmortizedCodeY");
		iPayIntFreqY = titaVo.getParam("PayIntFreqY");
		iRepayFreqY = titaVo.getParam("RepayFreqY");
		iGraceDateY = titaVo.getParam("GraceDateY");
		iFirstDueDateY = titaVo.getParam("FirstDueDateY");
		iSpecificDdY = titaVo.getParam("SpecificDdY");
		iAcctFeeY = titaVo.getParam("AcctFeeY");
		iPieceCodeY = titaVo.getParam("PieceCodeY");
		iPieceCodeSecondY = titaVo.getParam("PieceCodeSecondY");
		iPieceCodeSecondAmtY = titaVo.getParam("PieceCodeSecondAmtY");
		iRemarkY = titaVo.getParam("RemarkY");
		iProcessDateY = titaVo.getParam("ProcessDateY");
		iIntCalcCodeY = titaVo.getParam("IntCalcCodeY");
		iRemark = titaVo.getParam("Remark");

		// 登錄
		if (titaVo.isActfgEntry() && titaVo.isHcodeNormal()) {
			EntryNormalRoutine();
		}
		// 登錄 修正
		if (titaVo.isActfgEntry() && titaVo.isHcodeModify()) {
			EntryModifyRoutine();
			EntryNormalRoutine();
		}
		// 登錄 訂正
		if (titaVo.isActfgEntry() && titaVo.isHcodeErase()) {
			EntryEraseRoutine();
		}
		// 放行及放行訂正
		if (titaVo.isActfgSuprele()) {
			ReleaseRoutine();
		}
		// 產生會計分錄
		if (this.txBuffer.getTxCom().isBookAcYes() && lAcDetail.size() > 0) {
			this.txBuffer.addAllAcDetailList(lAcDetail);
			acDetailCom.setTxBuffer(this.txBuffer);
			acDetailCom.run(titaVo);
			this.setTxBuffer(acDetailCom.getTxBuffer());
		}
		// 欠繳金額銷帳檔處理
		if (lAcReceivable.size() > 0) {
			acReceivableCom.setTxBuffer(this.getTxBuffer());
			acReceivableCom.mnt(0, lAcReceivable, titaVo); // 0-起帳 1-銷帳
		}

		// end
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void EntryNormalRoutine() throws LogicException {
		this.info("EntryNormalRoutine ... ");

		if (iFacmNo > 0) {
			wkFacmNoStart = iFacmNo;
			wkFacmNoEnd = iFacmNo;
		}

		if (iBormNo > 0) {
			wkBormNoStart = iBormNo;
			wkBormNoEnd = iBormNo;
		}

		if ((iPieceCodeY.equals("X") || iPieceCodeSecondY.equals("X") || (iPieceCodeSecondAmtY.equals("X")))
				&& iBormNo == 0) {
			throw new LogicException(titaVo, "E0019", "變更計件代碼相關欄位需輸入撥款序號"); // E0019 輸入資料錯誤
		}

		if (iAcctCodeY.equals("X") && iBormNo != 0) {
			throw new LogicException(titaVo, "E0019", "變更業務科目需全額度，請勿輸入撥款序號"); // E0019 輸入資料錯誤
		}
		this.info("iFacmOnlyFlag = " + iFacmOnlyFlag);
		if (iFacmOnlyFlag.equals("Y")) {
			FacmOnlyEntryNormalRoutine();
		} else {
			FacmNotOnlyEntryNormalRoutine();
		}
		if (wkTotaCount == 0) {
			throw new LogicException(titaVo, "E3084", "戶號 = " + iCustNo + " 額度編號 = " + iFacmNo + " 撥款序號 = " + iBormNo); // 查無資料可內容變更
		}

		if (updFacMain == false && updLoanBorMain == false && updLoanOverdue == false) {
			throw new LogicException(titaVo, "E3086", "戶號 = " + iCustNo + " 額度編號 = " + iFacmNo + " 撥款序號 = " + iBormNo); // 放款內容變更，必須至少變更一個項目
		}
	}

	private void FacmOnlyEntryNormalRoutine() throws LogicException {
		this.info("FacmOnlyEntryNormalRoutine ... ");

		Slice<FacMain> slFacMain = facMainService.facmCustNoRange(iCustNo, iCustNo, wkFacmNoStart, wkFacmNoEnd, 0,
				Integer.MAX_VALUE, titaVo);
		List<FacMain> lFacMain = slFacMain == null ? null : slFacMain.getContent();
		if (lFacMain == null || lFacMain.size() == 0) {
			throw new LogicException(titaVo, "E0001", "額度主檔"); // 查詢資料不存在
		}
		for (FacMain fac : lFacMain) {
			if (fac.getActFg() == 1 && !titaVo.isHcodeErase()) {
				throw new LogicException(titaVo, "E0021",
						"額度檔 戶號 = " + fac.getCustNo() + " 額度編號 =  " + fac.getFacmNo()); // 該筆資料待放行中
			}
			wkFacmNo = fac.getFacmNo();
			wkFacmNo = fac.getFacmNo();
			wkBormNo = 0;
			wkBorxNo = 0;
			// 維護額度主檔
			FacMainRoutine();
			// 新增交易暫存檔
			AddTxTempRoutine();
			// 更新額度主檔
			if (updFacMain) {
				try {
					tFacMain = facMainService.update2(tFacMain, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007",
							"額度主檔 戶號 = " + iCustNo + " 額度編號 = " + wkFacmNo + " " + e.getErrorMsg()); // 更新資料時，發生錯誤
				}
				datalog.setEnv(titaVo, beforeFacMain, tFacMain);
				datalog.exec(iRemark);
			}
			wkTotaCount++;
		}
	}

	private void FacmNotOnlyEntryNormalRoutine() throws LogicException {
		this.info("FacmNotOnlyRoutine ... ");

		Slice<LoanBorMain> slLoanBorMain = loanBorMainService.bormCustNoEq(iCustNo, wkFacmNoStart, wkFacmNoEnd,
				wkBormNoStart, wkBormNoEnd, 0, Integer.MAX_VALUE, titaVo);
		lLoanBorMain = slLoanBorMain == null ? null : slLoanBorMain.getContent();
		if (lLoanBorMain == null || lLoanBorMain.size() == 0) {
			throw new LogicException(titaVo, "E0001", "放款主檔"); // 查詢資料不存在
		}
		// 戶況 0:正常戶1:展期2:催收戶3: 結案戶4:逾期戶5:催收結案戶6:呆帳戶7:部分轉呆戶8:債權轉讓戶9:呆帳結案戶
		for (LoanBorMain ln : lLoanBorMain) {
			if (!(ln.getStatus() == 0 || ln.getStatus() == 2 || ln.getStatus() == 4 || ln.getStatus() == 7)) {
				continue;
			}
			if (ln.getActFg() == 1 && !titaVo.isHcodeErase()) {
				throw new LogicException(titaVo, "E0021",
						"放款主檔 戶號 = " + ln.getCustNo() + " 額度編號 =  " + ln.getFacmNo() + " 撥款序號 = " + ln.getBormNo()); // 該筆資料待放行中
			}
			wkCustNo = ln.getCustNo();
			wkFacmNo = ln.getFacmNo();
			wkBormNo = ln.getBormNo();
			wkBorxNo = ln.getLastBorxNo() + 1;
			wkOvduNo = ln.getLastOvduNo();
			// 準備放款交易內容檔
			tLoanBorTx = new LoanBorTx();
			tLoanBorTxId = new LoanBorTxId();
			loanCom.setLoanBorTx(tLoanBorTx, tLoanBorTxId, iCustNo, wkFacmNo, wkBormNo, wkBorxNo, titaVo);
			tLoanBorTx.setDesc("放款內容變更");
			tLoanBorTx.setLoanBal(ln.getLoanBal());
			tLoanBorTx.setRate(ln.getStoreRate());
			if (iAcctCodeY.equals("X"))
				tLoanBorTx.setDisplayflag("A");
			else
				tLoanBorTx.setDisplayflag("Y");
			tTemp1Vo.clear(); // 其他欄位 LoanBorTX
			tTemp2Vo.clear(); // 其他欄位 TxTemp
			// 維護額度主檔
			if (wkFacmNo != wkBeforeFacmNo) {
				FacMainRoutine();
				wkBeforeFacmNo = wkFacmNo;
			}
			// 維護撥款檔
			LoanBorMainRoutine();
			// 催收呆帳檔
			if (ln.getStatus() == 2 || ln.getStatus() == 7) {
				LoanOverdueRoutine();
			}
			// 新增交易暫存檔
			AddTxTempRoutine();
			// 新增放款交易內容檔
			tLoanBorTx.setOtherFields(tTemp1Vo.getJsonString());
			try {
				loanBorTxService.insert(tLoanBorTx, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "放款交易內容檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
			}
			// 更新額度主檔
			if (updFacMain) {
				try {
					tFacMain = facMainService.update2(tFacMain, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007",
							"額度主檔 戶號 = " + iCustNo + " 額度編號 = " + wkFacmNo + " " + e.getErrorMsg()); // 更新資料時，發生錯誤
				}
				datalog.setEnv(titaVo, beforeFacMain, tFacMain);
				datalog.exec(iRemark);
			}
			// 更新撥款主檔
			tLoanBorMain.setLastBorxNo(wkBorxNo);
			tLoanBorMain.setActFg(titaVo.getActFgI());
			tLoanBorMain.setLastEntDy(titaVo.getEntDyI());
			tLoanBorMain.setLastKinbr(titaVo.getKinbr());
			tLoanBorMain.setLastTlrNo(titaVo.getTlrNo());
			tLoanBorMain.setLastTxtNo(titaVo.getTxtNo());
			try {
				tLoanBorMain = loanBorMainService.update2(tLoanBorMain, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007",
						"放款主檔 戶號 = " + iCustNo + " 額度編號 = " + wkFacmNo + " 撥款序號 = " + wkBormNo + " " + e.getErrorMsg()); // 更新資料時，發生錯誤
			}
			this.titaVo.putParam("BormNo", tLoanBorMain.getBormNo());
			datalog.setEnv(titaVo, beforeLoanBorMain, tLoanBorMain);
			datalog.exec(iRemark);
			// 更新催收呆帳檔
			if (updLoanOverdue) {
				try {
					tLoanOverdue = loanOverdueService.update2(tLoanOverdue, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "催收呆帳檔 戶號 = " + iCustNo + " 額度編號 = " + wkFacmNo
							+ " 撥款序號 = " + wkBormNo + " 催收序號 = " + wkOvduNo + " " + e.getErrorMsg()); // 更新資料時，發生錯誤
				}
				this.titaVo.putParam("BormNo", tLoanOverdue.getBormNo());
				datalog.setEnv(titaVo, beforeLoanOverdue, tLoanOverdue);
				datalog.exec(iRemark);
			}
			wkTotaCount++;
			// 計件代碼、計件代碼2或計件代碼2金額變更，新增業績處理明細
			if (iPieceCodeY.equals("X") || iPieceCodeSecondY.equals("X") || iPieceCodeSecondAmtY.equals("X")) {
				PfDetailRoutine();
			}
		}
	}

	private void ReleaseRoutine() throws LogicException {
		this.info("ReleaseRoutine ... ");

		String wkUpdFacMain = "";
		String wkUpdLoanBorMain = "";
		String wkUpdLoanOverdue = "";

		Slice<TxTemp> slTxTemp = txTempService.txTempTxtNoEq(titaVo.getOrgEntdyI() + 19110000, titaVo.getOrgKin(),
				titaVo.getOrgTlr(), titaVo.getOrgTno(), 0, Integer.MAX_VALUE, titaVo);
		lTxTemp = slTxTemp == null ? null : slTxTemp.getContent();

		if (lTxTemp == null || lTxTemp.size() == 0) {
			throw new LogicException(titaVo, "E0001", "交易暫存檔 分行別 = " + titaVo.getOrgKin() + " 交易員代號 = "
					+ titaVo.getOrgTlr() + " 交易序號 = " + titaVo.getOrgTno()); // 查詢資料不存在
		}
		for (TxTemp tx : lTxTemp) {
			wkCustNo = this.parse.stringToInteger(tx.getSeqNo().substring(0, 7));
			wkFacmNo = this.parse.stringToInteger(tx.getSeqNo().substring(7, 10));
			wkBormNo = this.parse.stringToInteger(tx.getSeqNo().substring(10, 13));
			wkOvduNo = this.parse.stringToInteger(tx.getSeqNo().substring(13, 16));
			tTemp1Vo = tTemp1Vo.getVo(tx.getText());
			wkBorxNo = this.parse.stringToInteger(tTemp1Vo.getParam("BorxNo"));
			wkUpdFacMain = tTemp1Vo.getParam("UpdFacMain");
			wkUpdLoanBorMain = tTemp1Vo.getParam("UpdLoanBorMain");
			wkUpdLoanOverdue = tTemp1Vo.getParam("UpdLoanOverdue");
			this.info("   wkCustNo = " + wkCustNo);
			this.info("   wkFacmNo = " + wkFacmNo);
			this.info("   wkBormNo = " + wkBormNo);
			this.info("   wkOvduNo = " + wkOvduNo);
			this.info("   wkBorxNo = " + wkBorxNo);
			this.info("   wkUpdFacMain     = " + wkUpdFacMain);
			this.info("   wkUpdLoanBorMain = " + wkUpdLoanBorMain);
			this.info("   wkUpdLoanOverdue = " + wkUpdLoanOverdue);
			if (wkUpdFacMain.equals("X")) {
				ReleaseFacMainRoutine();
			}
			if (wkUpdLoanBorMain.equals("X")) {
				ReleaseLoanBorMainRoutine();
			}
		}
	}

	private void ReleaseFacMainRoutine() throws LogicException {
		this.info("ReleaseFacMainRoutine ...");

		tFacMain = facMainService.holdById(new FacMainId(wkCustNo, wkFacmNo), titaVo);
		if (tFacMain == null) {
			throw new LogicException(titaVo, "E0006", "額度主檔"); // 鎖定資料時，發生錯誤
		}
		// if (tFacMain.getActFg() == 1 && !titaVo.isHcodeErase()) {
		// throw new LogicException(titaVo, "E0021",
		// "額度檔 戶號 = " + tFacMain.getCustNo() + " 額度編號 = " + tFacMain.getFacmNo()); //
		// 該筆資料待放行中
		// }
		beforeFacMain = (FacMain) datalog.clone(tFacMain);
		// 放行 一般
		if (titaVo.isHcodeNormal()) {
			if (tFacMain.getActFg() != 1) {
				throw new LogicException(titaVo, "E0017",
						"額度主檔 戶號 = " + wkCustNo + "額度編號 = " + wkFacmNo + " 交易進行記號 = " + tFacMain.getActFg()); // 該筆交易狀態非待放行，不可做交易放行
			}
			// 新增交易暫存檔
			TxTemp tTxTemp = new TxTemp();
			TxTempId tTxTempId = new TxTempId();
			loanCom.setTxTemp(tTxTempId, tTxTemp, wkCustNo, wkFacmNo, 0, 0, titaVo);
			tTemp1Vo.clear();
			tTemp1Vo.putParam("UpdFacMain", "X");
			tTemp1Vo.putParam("FcActFg", tFacMain.getActFg());
			tTemp1Vo.putParam("FcLastEntDy", tFacMain.getLastAcctDate());
			tTemp1Vo.putParam("FcLastKinbr", tFacMain.getLastKinbr());
			tTemp1Vo.putParam("FcLastTlrNo", tFacMain.getLastTlrNo());
			tTemp1Vo.putParam("FcLastTxtNo", tFacMain.getLastTxtNo());
			tTxTemp.setText(tTemp1Vo.getJsonString());
			try {
				txTempService.insert(tTxTemp, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "交易暫存檔 Key = " + tTxTempId); // 新增資料時，發生錯誤
			}
			// 更新額度主檔
			tFacMain.setActFg(titaVo.getActFgI());
			tFacMain.setLastAcctDate(titaVo.getEntDyI());
			tFacMain.setLastKinbr(titaVo.getKinbr());
			tFacMain.setLastTlrNo(titaVo.getTlrNo());
			tFacMain.setLastTxtNo(titaVo.getTxtNo());
		}
		// 放行訂正
		if (titaVo.isHcodeErase()) {
			// 放款交易訂正交易須由最後一筆交易開始訂正
			loanCom.checkEraseFacmTxSeqNo(tFacMain, titaVo);
			// 查詢交易暫存檔
			String wkSeqNo = FormatUtil.pad9(String.valueOf(iCustNo), 7) + FormatUtil.pad9(String.valueOf(iFacmNo), 3)
					+ "000000";
			TxTemp tTxTemp = txTempService.findById(new TxTempId(titaVo.getOrgEntdyI() + 19110000, titaVo.getOrgKin(),
					titaVo.getOrgTlr(), titaVo.getOrgTno(), wkSeqNo), titaVo);
			if (tTxTemp == null) {
				throw new LogicException(titaVo, "E0001", "交易暫存檔"); // 查詢資料不存在
			}
			tTemp1Vo = tTemp1Vo.getVo(tTxTemp.getText());
			tFacMain.setActFg(this.parse.stringToInteger(tTemp1Vo.getParam("FcActFg")));
			tFacMain.setLastAcctDate(this.parse.stringToInteger(tTemp1Vo.getParam("FcLastEntDy")));
			tFacMain.setLastKinbr(tTemp1Vo.getParam("FcLastKinbr"));
			tFacMain.setLastTlrNo(tTemp1Vo.getParam("FcLastTlrNo"));
			tFacMain.setLastTxtNo(tTemp1Vo.getParam("FcLastTxtNo"));
		}
		try {
			tFacMain = facMainService.update2(tFacMain, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0008", "額度主檔 戶號 = " + iCustNo + "額度編號 = " + iFacmNo + e.getErrorMsg()); // 新增資料時，發生錯誤
		}

	}

	private void ReleaseLoanBorMainRoutine() throws LogicException {
		this.info("ReleaseLoanBorMainRoutine ...");

		tLoanBorMain = loanBorMainService.holdById(new LoanBorMainId(wkCustNo, wkFacmNo, wkBormNo), titaVo);
		if (tLoanBorMain == null) {
			throw new LogicException(titaVo, "E0006", "撥款主檔"); // 鎖定資料時，發生錯誤
		}

		beforeLoanBorMain = (LoanBorMain) datalog.clone(tLoanBorMain);
		// 放行 一般
		if (titaVo.isHcodeNormal()) {
			if (tLoanBorMain.getActFg() != 1) {
				throw new LogicException(titaVo, "E0017", "撥款主檔 戶號 = " + wkCustNo + "額度編號 = " + wkFacmNo + "撥款序號 = "
						+ wkBormNo + " 交易進行記號 = " + tLoanBorMain.getActFg()); // 該筆交易狀態非待放行，不可做交易放行
			}
			// 新增交易暫存檔
			TxTemp tTxTemp = new TxTemp();
			TxTempId tTxTempId = new TxTempId();
			loanCom.setTxTemp(tTxTempId, tTxTemp, wkCustNo, wkFacmNo, wkBormNo, 0, titaVo);
			tTemp1Vo.clear();
			tTemp1Vo.putParam("UpdLoanBorMain", "X");
			tTemp1Vo.putParam("ActFg", tLoanBorMain.getActFg());
			tTemp1Vo.putParam("LastEntDy", tLoanBorMain.getLastEntDy());
			tTemp1Vo.putParam("LastKinbr", tLoanBorMain.getLastKinbr());
			tTemp1Vo.putParam("LastTlrNo", tLoanBorMain.getLastTlrNo());
			tTemp1Vo.putParam("LastTxtNo", tLoanBorMain.getLastTxtNo());
			tTxTemp.setText(tTemp1Vo.getJsonString());
			try {
				txTempService.insert(tTxTemp, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "交易暫存檔 Key = " + tTxTempId); // 新增資料時，發生錯誤
			}
			// 更新撥款主檔
			tLoanBorMain.setActFg(titaVo.getActFgI());
			tLoanBorMain.setLastEntDy(titaVo.getEntDyI());
			tLoanBorMain.setLastKinbr(titaVo.getKinbr());
			tLoanBorMain.setLastTlrNo(titaVo.getTlrNo());
			tLoanBorMain.setLastTxtNo(titaVo.getTxtNo());
		}
		// 放行訂正
		if (titaVo.isHcodeErase()) {
			// 放款交易訂正交易須由最後一筆交易開始訂正
			loanCom.checkEraseBormTxSeqNo(tLoanBorMain, titaVo);
			// 查詢交易暫存檔
			String wkSeqNo = FormatUtil.pad9(String.valueOf(iCustNo), 7) + FormatUtil.pad9(String.valueOf(iFacmNo), 3)
					+ FormatUtil.pad9(String.valueOf(wkBormNo), 3) + "000";
			TxTemp tTxTemp = txTempService.findById(new TxTempId(titaVo.getOrgEntdyI() + 19110000, titaVo.getOrgKin(),
					titaVo.getOrgTlr(), titaVo.getOrgTno(), wkSeqNo), titaVo);
			if (tTxTemp == null) {
				throw new LogicException(titaVo, "E0001", "交易暫存檔"); // 查詢資料不存在
			}
			tTemp1Vo = tTemp1Vo.getVo(tTxTemp.getText());
			tLoanBorMain.setActFg(this.parse.stringToInteger(tTemp1Vo.getParam("ActFg")));
			tLoanBorMain.setLastEntDy(this.parse.stringToInteger(tTemp1Vo.getParam("LastEntDy")));
			tLoanBorMain.setLastKinbr(tTemp1Vo.getParam("LastKinbr"));
			tLoanBorMain.setLastTlrNo(tTemp1Vo.getParam("LastTlrNo"));
			tLoanBorMain.setLastTxtNo(tTemp1Vo.getParam("LastTxtNo"));
		}
		try {
			tLoanBorMain = loanBorMainService.update2(tLoanBorMain, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0008",
					"撥款主檔 戶號 = " + iCustNo + "額度編號 = " + iFacmNo + "撥款序號 = " + wkBormNo + e.getErrorMsg()); // 新增資料時，發生錯誤
		}

		// 計件代碼、計件代碼2或計件代碼2金額變更，新增業績處理明細
		if (iPieceCodeY.equals("X") || iPieceCodeSecondY.equals("X") || iPieceCodeSecondAmtY.equals("X")) {
			PfDetailRoutine();
		}

	}

	private void EntryModifyRoutine() throws LogicException {
		this.info("EntryModifyRoutine ... ");

		String wkUpdFacMain = "";
		String wkUpdLoanBorMain = "";
		String wkUpdLoanOverdue = "";

		Slice<TxTemp> slTxTemp = txTempService.txTempTxtNoEq(titaVo.getOrgEntdyI() + 19110000, titaVo.getOrgKin(),
				titaVo.getOrgTlr(), titaVo.getOrgTno(), 0, Integer.MAX_VALUE, titaVo);
		lTxTemp = slTxTemp == null ? null : slTxTemp.getContent();
		if (lTxTemp == null || lTxTemp.size() == 0) {
			throw new LogicException(titaVo, "E0001", "交易暫存檔 分行別 = " + titaVo.getOrgKin() + " 交易員代號 = "
					+ titaVo.getOrgTlr() + " 交易序號 = " + titaVo.getOrgTno()); // 查詢資料不存在
		}
		for (TxTemp tx : lTxTemp) {
			wkCustNo = this.parse.stringToInteger(tx.getSeqNo().substring(0, 7));
			wkFacmNo = this.parse.stringToInteger(tx.getSeqNo().substring(7, 10));
			wkBormNo = this.parse.stringToInteger(tx.getSeqNo().substring(10, 13));
			wkOvduNo = this.parse.stringToInteger(tx.getSeqNo().substring(13, 16));
			tTemp1Vo = tTemp1Vo.getVo(tx.getText());
			wkBorxNo = this.parse.stringToInteger(tTemp1Vo.getParam("BorxNo"));
			wkUpdFacMain = tTemp1Vo.getParam("UpdFacMain");
			wkUpdLoanBorMain = tTemp1Vo.getParam("UpdLoanBorMain");
			wkUpdLoanOverdue = tTemp1Vo.getParam("UpdLoanOverdue");
			this.info("   wkCustNo = " + wkCustNo);
			this.info("   wkFacmNo = " + wkFacmNo);
			this.info("   wkBormNo = " + wkBormNo);
			this.info("   wkOvduNo = " + wkOvduNo);
			this.info("   wkBorxNo = " + wkBorxNo);
			this.info("   wkUpdFacMain     = " + wkUpdFacMain);
			this.info("   wkUpdLoanBorMain = " + wkUpdLoanBorMain);
			this.info("   wkUpdLoanOverdue = " + wkUpdLoanOverdue);
			// 還原額度檔
			if (wkUpdFacMain.equals("X")) {
				RestoredFacMainRoutine();
			}
			// 還原撥款主檔
			if (wkUpdLoanBorMain.equals("X")) {
				RestoredLoanBorMainRoutine();
			}
			// 還原催收檔
			if (wkUpdLoanOverdue.equals("X")) {
				RestoredLoanOverdueRoutine();
			}
			// 刪除原交易內容檔
			if (titaVo.isHcodeModify() && !iFacmOnlyFlag.equals("Y")) {
				LoanBorTx tLoanBorTx = loanBorTxService
						.holdById(new LoanBorTxId(wkCustNo, wkFacmNo, wkBormNo, wkBorxNo), titaVo);
				if (tLoanBorTx == null) {
					throw new LogicException(titaVo, "E0006", "放款交易內容檔 戶號 = " + wkCustNo + " 額度編號 = " + wkFacmNo
							+ " 撥款序號 = " + wkBormNo + " 交易內容檔序號 = " + wkBorxNo); // 鎖定資料時，發生錯誤
				}
				try {
					loanBorTxService.delete(tLoanBorTx, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", "放款交易內容檔 " + e.getErrorMsg()); // 刪除資料時，發生錯誤
				}
			}
		}
	}

	private void EntryEraseRoutine() throws LogicException {
		this.info("EntryEraseRoutine ... ");

		String wkUpdFacMain = "";
		String wkUpdLoanBorMain = "";
		String wkUpdLoanOverdue = "";

		Slice<TxTemp> slTxTemp = txTempService.txTempTxtNoEq(titaVo.getOrgEntdyI() + 19110000, titaVo.getOrgKin(),
				titaVo.getOrgTlr(), titaVo.getOrgTno(), 0, Integer.MAX_VALUE, titaVo);
		lTxTemp = slTxTemp == null ? null : slTxTemp.getContent();
		if (lTxTemp == null || lTxTemp.size() == 0) {
			throw new LogicException(titaVo, "E0001", "交易暫存檔 分行別 = " + titaVo.getOrgKin() + " 交易員代號 = "
					+ titaVo.getOrgTlr() + " 交易序號 = " + titaVo.getOrgTno()); // 查詢資料不存在
		}
		for (TxTemp tx : lTxTemp) {
			wkCustNo = this.parse.stringToInteger(tx.getSeqNo().substring(0, 7));
			wkFacmNo = this.parse.stringToInteger(tx.getSeqNo().substring(7, 10));
			wkBormNo = this.parse.stringToInteger(tx.getSeqNo().substring(10, 13));
			wkOvduNo = this.parse.stringToInteger(tx.getSeqNo().substring(13, 16));
			tTemp1Vo = tTemp1Vo.getVo(tx.getText());
			wkBorxNo = this.parse.stringToInteger(tTemp1Vo.getParam("BorxNo"));
			wkUpdFacMain = tTemp1Vo.getParam("UpdFacMain");
			wkUpdLoanBorMain = tTemp1Vo.getParam("UpdLoanBorMain");
			wkUpdLoanOverdue = tTemp1Vo.getParam("UpdLoanOverdue");
			this.info("   wkCustNo = " + wkCustNo);
			this.info("   wkFacmNo = " + wkFacmNo);
			this.info("   wkBormNo = " + wkBormNo);
			this.info("   wkOvduNo = " + wkOvduNo);
			this.info("   wkBorxNo = " + wkBorxNo);
			this.info("   wkUpdFacMain     = " + wkUpdFacMain);
			this.info("   wkUpdLoanBorMain = " + wkUpdLoanBorMain);
			this.info("   wkUpdLoanOverdue = " + wkUpdLoanOverdue);
			// 還原額度檔
			if (wkUpdFacMain.equals("X")) {
				RestoredFacMainRoutine();
			}
			// 還原撥款主檔
			if (wkUpdLoanBorMain.equals("X")) {
				RestoredLoanBorMainRoutine();
			}
			// 還原催收檔
			if (wkUpdLoanOverdue.equals("X")) {
				RestoredLoanOverdueRoutine();
			}
			// 註記交易內容檔
			if (!iFacmOnlyFlag.equals("Y")) {
				loanCom.setLoanBorTxHcode(wkCustNo, wkFacmNo, wkBormNo, wkBorxNo, wkNewBorxNo,
						tLoanBorMain.getLoanBal(), titaVo);
			}
		}
	}

	private void FacMainRoutine() throws LogicException {
		this.info("FacmMainRoutine ... ");

		updFacMain = false;
		// 鎖定額度檔
		tFacMain = facMainService.holdById(new FacMainId(iCustNo, wkFacmNo), titaVo);
		if (tFacMain == null) {
			throw new LogicException(titaVo, "E0006", "額度主檔 戶號 = " + iCustNo + " 額度編號 = " + wkFacmNo); // 鎖定資料時，發生錯誤
		}
		if (tFacMain.getActFg() == 1 && !titaVo.isHcodeErase()) {
			throw new LogicException(titaVo, "E0021",
					"額度檔 戶號 = " + tFacMain.getCustNo() + " 額度編號 =  " + tFacMain.getFacmNo()); // 該筆資料待放行中
		}

		beforeFacMain = (FacMain) datalog.clone(tFacMain);
		tTemp1Vo.putParam("FcAcCd1", tFacMain.getAcctCode());
		tTemp1Vo.putParam("FcRpyCd1", tFacMain.getRepayCode());
		tTemp1Vo.putParam("FcRtCd1", tFacMain.getRateCode());
		tTemp1Vo.putParam("FcRtIncr1", tFacMain.getRateIncr());
		tTemp1Vo.putParam("FcIduIncr1", tFacMain.getIndividualIncr());
		tTemp1Vo.putParam("FcRtAdjFq1", tFacMain.getRateAdjFreq());
		tTemp1Vo.putParam("FcAmoCd1", tFacMain.getAmortizedCode());
		tTemp1Vo.putParam("FcPayIntFq1", tFacMain.getPayIntFreq());
		tTemp1Vo.putParam("FcRpyFq1", tFacMain.getRepayFreq());
		tTemp1Vo.putParam("FcAcFee1", tFacMain.getAcctFee());
		//
		tTemp2Vo.putParam("FcAcCd1", tFacMain.getAcctCode());
		tTemp2Vo.putParam("FcRpyCd1", tFacMain.getRepayCode());
		tTemp2Vo.putParam("FcRtCd1", tFacMain.getRateCode());
		tTemp2Vo.putParam("FcRtIncr1", tFacMain.getRateIncr());
		tTemp2Vo.putParam("FcIduIncr1", tFacMain.getIndividualIncr());
		tTemp2Vo.putParam("FcRtAdjFq1", tFacMain.getRateAdjFreq());
		tTemp2Vo.putParam("FcAmoCd1", tFacMain.getAmortizedCode());
		tTemp2Vo.putParam("FcPayIntFq1", tFacMain.getPayIntFreq());
		tTemp2Vo.putParam("FcRpyFq1", tFacMain.getRepayFreq());
		tTemp2Vo.putParam("FcAcFee1", tFacMain.getAcctFee());

		tTemp2Vo.putParam("FcMatDt1", tFacMain.getMaturityDate());
		tTemp2Vo.putParam("FcGrcPrd1", tFacMain.getGracePeriod());
		tTemp2Vo.putParam("FcActFg", tFacMain.getActFg());
		tTemp2Vo.putParam("FcLastEntDy", tFacMain.getLastAcctDate());
		tTemp2Vo.putParam("FcLastKinbr", tFacMain.getLastKinbr());
		tTemp2Vo.putParam("FcLastTlrNo", tFacMain.getLastTlrNo());
		tTemp2Vo.putParam("FcLastTxtNo", tFacMain.getLastTxtNo());
		// 科目
		if (iAcctCodeY.equals("X")) {
			updFacMain = true;
			wkBeforeAcctCode = tFacMain.getAcctCode();
			tTemp2Vo.putParam("FcAcCdY", iAcctCodeY);
			tTemp1Vo.putParam("FcAcCdY", iAcctCodeY);
			tTemp1Vo.putParam("FcAcCd2", titaVo.getParam("AcctCode2"));
			tFacMain.setAcctCode(titaVo.getParam("AcctCode2"));

			// 帳務處理
			AcDetailRoutine();
		}
		// 到期日
		if (iMaturityDateY.equals("X")) {
			tTemp2Vo.putParam("FcMatDtY", iMaturityDateY);
			tFacMain.setMaturityDate(this.parse.stringToInteger(titaVo.getParam("MaturityDate2")));
		}

		// 計息方式
		if (iIntCalcCodeY.equals("X")) {
			updFacMain = true;
			tTemp2Vo.putParam("FcIntCaCdY", iIntCalcCodeY);
			tTemp1Vo.putParam("FcIntCaCdY", iIntCalcCodeY);
			tTemp2Vo.putParam("FcIntCalcCode1", tFacMain.getIntCalcCode());
			tTemp1Vo.putParam("FcIntCaCd2", titaVo.getParam("iIntCalcCode2"));
			tFacMain.setIntCalcCode(titaVo.getParam("iIntCalcCode2"));
		}
//		if (iRepayCodeY.equals("X")) {
//			updFacMain = true;
//			tTemp2Vo.putParam("FcRpyCdY", iRepayCodeY);
//			tTemp1Vo.putParam("FcRpyCdY", iRepayCodeY);
//			tTemp1Vo.putParam("FcRpyCe2", titaVo.getParam("RepayCode2"));
//			tFacMain.setRepayCode(this.parse.stringToInteger(titaVo.getParam("RepayCode2")));
//		}
//		// 扣款銀行代號
//		if (iRepayBankY.equals("X")) {
//			updFacMain = true;
//			tTemp2Vo.putParam("FcRpyBkY", iRepayBankY);
//			tTemp1Vo.putParam("FcRpyBkY", iRepayBankY);
//			tTemp1Vo.putParam("FcRpyBk2", titaVo.getParam("RepayBank2"));
//			tFacMain.setRepayBank(titaVo.getParam("RepayBank2"));
//		}
//		// 扣款帳號
//		if (iRepayAcctNoY.equals("X")) {
//			updFacMain = true;
//			tTemp2Vo.putParam("FcRpyAcNoY", iRepayAcctNoY);
//			tTemp1Vo.putParam("FcRpyAcNoY", iRepayAcctNoY);
//			tTemp1Vo.putParam("FcRpyAcNo2", titaVo.getParam("RepayAcctNo2"));
//			tFacMain.setRepayAcctNo(this.parse.stringToBigDecimal(titaVo.getParam("RepayAcctNo2")));
//		}
//		// 郵局存款別
//		if (iPostCodeY.equals("X")) {
//			updFacMain = true;
//			tTemp2Vo.putParam("FcPostCdY", iPostCodeY);
//			tTemp1Vo.putParam("FcPostCdY", iPostCodeY);
//			tTemp1Vo.putParam("FcPostCd2", titaVo.getParam("PostCode2"));
//			tFacMain.setPostCode(titaVo.getParam("PostCode2"));
//		}
		// 利率區分
		if (iRateCodeY.equals("X")) {
			updFacMain = true;
			tTemp2Vo.putParam("FcRtCdY", iRateCodeY);
			tTemp1Vo.putParam("FcRtCdY", iRateCodeY);
			tTemp1Vo.putParam("FcRtCd2", titaVo.getParam("RateCode2"));
			tFacMain.setRateCode(titaVo.getParam("RateCode2"));
		}
		// TODO:核准利率 催收戶不可變更
		this.info("iApproveRateY = " + iApproveRateY);
		// 10/27 只能修改第一筆撥款或全撥款
		if (iApproveRateY.equals("X") && (iBormNo == 0 || iBormNo == 1)) {
			updFacMain = true;
			tTemp2Vo.putParam("FcApproveRateY", iApproveRateY);
			tTemp1Vo.putParam("FcApproveRateY", iApproveRateY);
			tTemp1Vo.putParam("FcApproveRate2", titaVo.getParam("ApproveRate2"));
			tFacMain.setApproveRate(this.parse.stringToBigDecimal(titaVo.getParam("ApproveRate2N")));
		}

		// 利率加減碼 催收戶不可變更
		if (iRateIncrY.equals("X")) {
			updFacMain = true;
			tTemp2Vo.putParam("FcRtIncrY", iRateIncrY);
			tTemp1Vo.putParam("FcRtIncrY", iRateIncrY);
			tTemp1Vo.putParam("FcRtIncr2", titaVo.getParam("RateIncr2"));
			tFacMain.setRateIncr(this.parse.stringToBigDecimal(titaVo.getParam("RateIncr2N")));
		}
		// 個別加減碼 催收戶不可變更
		if (iIndividualIncrY.equals("X")) {
			updFacMain = true;
			tTemp2Vo.putParam("FcIduIncrY", iIndividualIncrY);
			tTemp1Vo.putParam("FcIduIncrY", iIndividualIncrY);
			tTemp1Vo.putParam("FcIduIncr2", titaVo.getParam("IndividualIncr2"));
			tFacMain.setIndividualIncr(this.parse.stringToBigDecimal(titaVo.getParam("IndividualIncr2N")));
		}
		// 利率調整週期 利率區分為 '3' 者，必須輸入利率調整週
		if (iRateAdjFreqY.equals("X")) {
			updFacMain = true;
			tTemp2Vo.putParam("FcRtAdjFqY", iRateAdjFreqY);
			tTemp1Vo.putParam("FcRtAdjFqY", iRateAdjFreqY);
			tTemp1Vo.putParam("FcRtAdjFq2", titaVo.getParam("RateAdjFreq2"));
			tFacMain.setRateAdjFreq(this.parse.stringToInteger(titaVo.getParam("RateAdjFreq2")));
		}
		// 攤還方式 按月繳息限中、短期
		if (iAmortizedCodeY.equals("X") && iFacmOnlyFlag.equals("Y")) {
			updFacMain = true;
			String wkAmortizedCode = titaVo.getParam("AmortizedCode2");
			if (wkAmortizedCode.equals("1")) { // 1.按月繳息(按期繳息到期還本)
				if (!(tFacMain.getAcctCode().equals("310") || tFacMain.getAcctCode().equals("320"))) {
					throw new LogicException(titaVo, "E0019", "攤還方式按月繳息限中、短期"); // 輸入資料錯誤
				}
			}
			tTemp2Vo.putParam("FcAmoCdY", iAmortizedCodeY);
			tTemp1Vo.putParam("FcAmoCdY", iAmortizedCodeY);
			tTemp1Vo.putParam("FcAmoCd2", wkAmortizedCode);
			tFacMain.setAmortizedCode(wkAmortizedCode);
			tTemp2Vo.putParam("FcGrcPrdY", "X");
			if (wkAmortizedCode.equals("1") || wkAmortizedCode.equals("2")) {
				tFacMain.setGracePeriod(0);
			}
		}
		// 繳息週期
		if (iPayIntFreqY.equals("X") && iFacmOnlyFlag.equals("Y")) {
			updFacMain = true;
			tTemp2Vo.putParam("FcPayIntFqY", iPayIntFreqY);
			tTemp1Vo.putParam("FcPayIntFqY", iPayIntFreqY);
			tTemp1Vo.putParam("FcPayIntFq2", titaVo.getParam("PayIntFreq2"));
			tFacMain.setPayIntFreq(this.parse.stringToInteger(titaVo.getParam("PayIntFreq2")));
		}
		// 還本週期
		if (iRepayFreqY.equals("X") && iFacmOnlyFlag.equals("Y")) {
			updFacMain = true;
			tTemp2Vo.putParam("FcRpyFqY", iRepayFreqY);
			tTemp1Vo.putParam("FcRpyFqY", iRepayFreqY);
			tTemp1Vo.putParam("FcRpyFq2", titaVo.getParam("RepayFreq2"));
			tFacMain.setRepayFreq(this.parse.stringToInteger(titaVo.getParam("RepayFreq2")));
		}
		// 帳管費 未繳期款才可變更, 帳管費已達帳,不可修改
		if (iAcctFeeY.equals("X") && iFacmOnlyFlag.equals("Y")) {
			updFacMain = true;
			tTemp2Vo.putParam("FcAcFeeY", iAcctFeeY);
			tTemp1Vo.putParam("FcAcFeeY", iAcctFeeY);
			tTemp1Vo.putParam("FcAcFee2", titaVo.getParam("AcctFee2"));
			tFacMain.setAcctFee(this.parse.stringToBigDecimal(titaVo.getParam("AcctFee2N")));
		}
//		// 與借款人關係
//		if (iRelationCodeY.equals("X") && iFacmOnlyFlag.equals("Y")) {
//			updFacMain = true;
//			tTemp2Vo.putParam("FcRelCdY", iRelationCodeY);
//			tTemp1Vo.putParam("FcRelCdY", iRelationCodeY);
//			tTemp1Vo.putParam("FcRelCd2", titaVo.getParam("RelationCode2"));
//			tFacMain.setRelationCode(titaVo.getParam("RelationCode2"));
//		}
//		// 帳戶戶名
//		if (iRelationNameY.equals("X") && iFacmOnlyFlag.equals("Y")) {
//			updFacMain = true;
//			tTemp2Vo.putParam("FcRelNmY", iRelationNameY);
//			tTemp1Vo.putParam("FcRelNmY", iRelationNameY);
//			tTemp1Vo.putParam("FcRelNm2", titaVo.getParam("RelationName2"));
//			tFacMain.setRelationName(titaVo.getParam("RelationName2"));
//		}
//		// 身份證字號
//		if (iRelationIdY.equals("X") && iFacmOnlyFlag.equals("Y")) {
//			updFacMain = true;
//			tTemp2Vo.putParam("FcRelIdY", iRelationIdY);
//			tTemp1Vo.putParam("FcRelIdY", iRelationIdY);
//			tTemp1Vo.putParam("FcRelId2", titaVo.getParam("RelationId2"));
//			tFacMain.setRelationId(titaVo.getParam("RelationId2"));
//		}
//		// 出生日期
//		if (iRelationBirthdayY.equals("X") && iFacmOnlyFlag.equals("Y")) {
//			updFacMain = true;
//			tTemp2Vo.putParam("FcRelBirdyY", iRelationBirthdayY);
//			tTemp1Vo.putParam("FcRelBirdyY", iRelationBirthdayY);
//			tTemp1Vo.putParam("FcRelBirdy2", titaVo.getParam("RelationBirthday2"));
//			tFacMain.setRelationBirthday(this.parse.stringToInteger(titaVo.getParam("RelationBirthday2")));
//		}
//		// 性別
//		if (iRelationGenderY.equals("X") && iFacmOnlyFlag.equals("Y")) {
//			updFacMain = true;
//			tTemp2Vo.putParam("FcRelGenderY", iRelationGenderY);
//			tTemp1Vo.putParam("FcRelGenderY", iRelationGenderY);
//			tTemp1Vo.putParam("FcRelGender2", titaVo.getParam("RelationGender2"));
//			tFacMain.setRelationGender(titaVo.getParam("RelationGender2"));
//		}
		if (updFacMain) {
			tTemp2Vo.putParam("UpdFacMain", "X");
			tFacMain.setActFg(titaVo.getActFgI());
			tFacMain.setLastAcctDate(titaVo.getEntDyI());
			tFacMain.setLastKinbr(titaVo.getKinbr());
			tFacMain.setLastTlrNo(titaVo.getTlrNo());
			tFacMain.setLastTxtNo(titaVo.getTxtNo());
		}
	}

	private void LoanBorMainRoutine() throws LogicException {
		this.info("LoanBorMainRoutine ... ");

		int wkDate = 0;
		int wkGracePeriod = 0;
		boolean paidTermFlag = false;
		boolean overdueFlag = false;
		BigDecimal wkDueAmt = BigDecimal.ZERO;

		// 鎖定撥款主檔
		tLoanBorMain = loanBorMainService.holdById(new LoanBorMainId(iCustNo, wkFacmNo, wkBormNo), titaVo);
		if (tLoanBorMain == null) {
			throw new LogicException(titaVo, "E0006",
					"撥款主檔 戶號 = " + iCustNo + " 額度編號 = " + wkFacmNo + " 撥款序號 = " + wkBormNo); // 鎖定資料時，發生錯誤
		}

		// 更新撥款檔時檢核如L4321已確認未放行，則出錯誤訊息
		CheckBatxRateChangeRoutine();

		beforeLoanBorMain = (LoanBorMain) datalog.clone(tLoanBorMain);
		if (tLoanBorMain.getPaidTerms() > 0) {
			paidTermFlag = true;
		}
		if (tLoanBorMain.getStatus() == 2 || tLoanBorMain.getStatus() == 7) {
			overdueFlag = true;
		}
		updLoanBorMain = true;
		tTemp1Vo.putParam("RtCd1", tLoanBorMain.getRateCode());
		tTemp1Vo.putParam("RtApproveRate1", tLoanBorMain.getApproveRate());
		tTemp1Vo.putParam("RtIncr1", tLoanBorMain.getRateIncr());
		tTemp1Vo.putParam("IduIncr1", tLoanBorMain.getIndividualIncr());
		tTemp1Vo.putParam("RtAdjFq1", tLoanBorMain.getRateAdjFreq());
		tTemp1Vo.putParam("FstRtAdjDt1", tLoanBorMain.getFirstAdjRateDate());
		tTemp1Vo.putParam("NxtRtAdjDt1", tLoanBorMain.getNextAdjRateDate());
		tTemp1Vo.putParam("MatDt1", tLoanBorMain.getMaturityDate());
		tTemp1Vo.putParam("AmoCd1", tLoanBorMain.getAmortizedCode());
		tTemp1Vo.putParam("PayIntFq1", tLoanBorMain.getPayIntFreq());
		tTemp1Vo.putParam("RpyFq1", tLoanBorMain.getRepayFreq());
		tTemp1Vo.putParam("GrcDt1", tLoanBorMain.getGraceDate());
		tTemp1Vo.putParam("FstDueDt1", tLoanBorMain.getFirstDueDate());
		tTemp1Vo.putParam("SpcDd1", tLoanBorMain.getSpecificDd());
		tTemp1Vo.putParam("AcFee1", tLoanBorMain.getAcctFee());
		tTemp1Vo.putParam("RelCd1", tLoanBorMain.getRelationCode());
		tTemp1Vo.putParam("RelNm1", tLoanBorMain.getRelationName());
		tTemp1Vo.putParam("RelId1", tLoanBorMain.getRelationId());
		tTemp1Vo.putParam("RelBirdy1", tLoanBorMain.getRelationBirthday());
		tTemp1Vo.putParam("RelGender1", tLoanBorMain.getRelationGender());
		tTemp1Vo.putParam("PieceCode1", tLoanBorMain.getPieceCode());
		tTemp1Vo.putParam("PieceCodeSecond1", tLoanBorMain.getPieceCodeSecond());
		tTemp1Vo.putParam("PieceCodeSecondAmt1", tLoanBorMain.getPieceCodeSecondAmt());
		tTemp1Vo.putParam("IntCalcCode1", tLoanBorMain.getIntCalcCode());

		tTemp2Vo.putParam("RtCd1", tLoanBorMain.getRateCode());
		tTemp2Vo.putParam("RtIncr1", tLoanBorMain.getRateIncr());
		tTemp2Vo.putParam("RtApproveRate1", tLoanBorMain.getApproveRate());
		tTemp2Vo.putParam("IduIncr1", tLoanBorMain.getIndividualIncr());
		tTemp2Vo.putParam("RtAdjFq1", tLoanBorMain.getRateAdjFreq());
		tTemp2Vo.putParam("FstRtAdjDt1", tLoanBorMain.getFirstAdjRateDate());
		tTemp2Vo.putParam("NxtRtAdjDt1", tLoanBorMain.getNextAdjRateDate());
		tTemp2Vo.putParam("MatDt1", tLoanBorMain.getMaturityDate());
		tTemp2Vo.putParam("AmoCd1", tLoanBorMain.getAmortizedCode());
		tTemp2Vo.putParam("PayIntFq1", tLoanBorMain.getPayIntFreq());
		tTemp2Vo.putParam("RpyFq1", tLoanBorMain.getRepayFreq());
		tTemp2Vo.putParam("FstDueDt1", tLoanBorMain.getFirstDueDate());
		tTemp2Vo.putParam("GrcDt1", tLoanBorMain.getGraceDate());
		tTemp2Vo.putParam("SpcDd1", tLoanBorMain.getSpecificDd());
		tTemp2Vo.putParam("AcFee1", tLoanBorMain.getAcctFee());
		tTemp2Vo.putParam("RelCd1", tLoanBorMain.getRelationCode());
		tTemp2Vo.putParam("RelNm1", tLoanBorMain.getRelationName());
		tTemp2Vo.putParam("RelId1", tLoanBorMain.getRelationId());
		tTemp2Vo.putParam("RelBirdy1", tLoanBorMain.getRelationBirthday());
		tTemp2Vo.putParam("RelGender1", tLoanBorMain.getRelationGender());
		tTemp2Vo.putParam("PieceCode1", tLoanBorMain.getPieceCode());
		tTemp2Vo.putParam("PieceCodeSecond1", tLoanBorMain.getPieceCodeSecond());
		tTemp2Vo.putParam("PieceCodeSecondAmt1", tLoanBorMain.getPieceCodeSecondAmt());
		tTemp2Vo.putParam("IntCalcCode1", tLoanBorMain.getIntCalcCode());
		//
		tTemp2Vo.putParam("LnTmYy1", tLoanBorMain.getLoanTermYy());
		tTemp2Vo.putParam("LnTmMm1", tLoanBorMain.getLoanTermMm());
		tTemp2Vo.putParam("LnTmDd1", tLoanBorMain.getLoanTermDd());
		tTemp2Vo.putParam("GrcPrd1", tLoanBorMain.getGracePeriod());
		tTemp2Vo.putParam("SpcDt1", tLoanBorMain.getSpecificDate());
		tTemp2Vo.putParam("TotPrd1", tLoanBorMain.getTotalPeriod());
		tTemp2Vo.putParam("DueAmt1", tLoanBorMain.getDueAmt());
		tTemp2Vo.putParam("NxtPyDt1", tLoanBorMain.getNextPayIntDate());
		tTemp2Vo.putParam("NxtRpyDt1", tLoanBorMain.getNextRepayDate());
		tTemp2Vo.putParam("ActFg", tLoanBorMain.getActFg());
		tTemp2Vo.putParam("LastEntDy", tLoanBorMain.getLastEntDy());
		tTemp2Vo.putParam("LastKinbr", tLoanBorMain.getLastKinbr());
		tTemp2Vo.putParam("LastTlrNo", tLoanBorMain.getLastTlrNo());
		tTemp2Vo.putParam("LastTxtNo", tLoanBorMain.getLastTxtNo());

		// TODO:核准利率
		if (iApproveRateY.equals("X")) {
			if (overdueFlag) {
				throw new LogicException(titaVo, "E0019", "催收戶不可變更[利率區分]"); // 輸入資料錯誤
			}
			this.info("ApproveRate2N =" + this.parse.stringToBigDecimal(titaVo.getParam("ApproveRate2N")));
			updLoanBorMain = true;
			tTemp2Vo.putParam("RtApproveRateY", iApproveRateY);
			tTemp1Vo.putParam("RtApproveRateY", iApproveRateY);
			tTemp1Vo.putParam("RtApproveRate2", titaVo.getParam("ApproveRate2"));
			tLoanBorMain.setApproveRate(this.parse.stringToBigDecimal(titaVo.getParam("ApproveRate2N")));
			tLoanBorMain.setStoreRate(this.parse.stringToBigDecimal(titaVo.getParam("ApproveRate2N")));
		}
		// 利率區分 催收戶不可變更
		if (iRateCodeY.equals("X")) {
			if (overdueFlag) {
				throw new LogicException(titaVo, "E0019", "催收戶不可變更[利率區分]"); // 輸入資料錯誤
			}
			updLoanBorMain = true;
			tTemp2Vo.putParam("RtCdY", iRateCodeY);
			tTemp1Vo.putParam("RtCdY", iRateCodeY);
			tTemp1Vo.putParam("RtCd2", titaVo.getParam("RateCode2"));
			tLoanBorMain.setRateCode(titaVo.getParam("RateCode2"));
		}
		// 利率加減碼 催收戶、已繳過息不可變更
		if (iRateIncrY.equals("X")) {
			if (overdueFlag) {
				throw new LogicException(titaVo, "E0019", "催收戶不可變[利率加減碼]"); // 輸入資料錯誤
			}
			if (tLoanBorMain.getPrevPayIntDate() > tLoanBorMain.getDrawdownDate()) { // 上次繳息日,繳息迄日
				throw new LogicException(titaVo, "E0019", "已繳過息須執行[L3721] 借戶利率變更"); // 輸入資料錯誤
			}
			updLoanBorMain = true;
			tTemp2Vo.putParam("RtIncrY", iRateIncrY);
			tTemp1Vo.putParam("RtIncrY", iRateIncrY);
			tTemp1Vo.putParam("RtIncr2", titaVo.getParam("RateIncr2"));
			tLoanBorMain.setRateIncr(this.parse.stringToBigDecimal(titaVo.getParam("RateIncr2N")));
		}
		// 個別加減碼 催收戶、已繳過息不可變更
		if (iIndividualIncrY.equals("X")) {
			if (overdueFlag) {
				throw new LogicException(titaVo, "E0019", "催收戶不可變[個別加減碼]"); // 輸入資料錯誤
			}
			if (tLoanBorMain.getPrevPayIntDate() > tLoanBorMain.getDrawdownDate()) { // 上次繳息日,繳息迄日
				throw new LogicException(titaVo, "E0019", "已繳過息須執行[L3721] 借戶利率變更"); // 輸入資料錯誤
			}
			updLoanBorMain = true;
			tTemp2Vo.putParam("IduIncrY", iIndividualIncrY);
			tTemp1Vo.putParam("IduIncrY", iIndividualIncrY);
			tTemp1Vo.putParam("IduIncr2", titaVo.getParam("IndividualIncr2"));
			tLoanBorMain.setIndividualIncr(this.parse.stringToBigDecimal(titaVo.getParam("IndividualIncr2N")));
		}

		// 更新放款利率變動檔
		if (iRateIncrY.equals("X") || iIndividualIncrY.equals("X") || iApproveRateY.equals("X")) {
			updLoanRateChangeRoutine();
		}

		// 利率調整週期 催收戶不可變更, 利率區分為 '3' 者，必須輸入利率調整週
		if (iRateAdjFreqY.equals("X")) {
			if (overdueFlag) {
				throw new LogicException(titaVo, "E0019", "催收戶不可變[利率調整週期]"); // 輸入資料錯誤
			}
			updLoanBorMain = true;
			tTemp2Vo.putParam("RtAdjFqY", iRateAdjFreqY);
			tTemp1Vo.putParam("RtAdjFqY", iRateAdjFreqY);
			tTemp1Vo.putParam("RtAdjFq2", titaVo.getParam("RateAdjFreq2"));
			tLoanBorMain.setRateAdjFreq(this.parse.stringToInteger(titaVo.getParam("RateAdjFreq2")));
		}
		// 首次調整日期 催收戶不可變更, 已過首次調整日,不可變更, 利率區分為定期機動者，必須輸入,其他不可輸入
		if (iFirstRateAdjDateY.equals("X")) {
			if (overdueFlag) {
				throw new LogicException(titaVo, "E0019", "催收戶不可變[首次調整日期]"); // 輸入資料錯誤
			}
//			if (tLoanBorMain.getFirstAdjRateDate() < wkTbsDy) {
//				throw new LogicException(titaVo, "E0019", "已過首次調整日,不可變更[首次調整日期]"); // 輸入資料錯誤
//			}
			updLoanBorMain = true;
			tTemp2Vo.putParam("FstRtAdjDtY", iFirstRateAdjDateY);
			tTemp1Vo.putParam("FstRtAdjDtY", iFirstRateAdjDateY);
			tTemp1Vo.putParam("FstRtAdjDt2", titaVo.getParam("FirstRateAdjDate2"));
			tLoanBorMain.setFirstAdjRateDate(this.parse.stringToInteger(titaVo.getParam("FirstRateAdjDate2")));
		}
		// 下次調整日期 催收戶不可變更, 利率區分為定期機動者，必須輸入,其他不可輸入
		if (iNextRateAdjDateY.equals("X")) {
			if (overdueFlag) {
				throw new LogicException(titaVo, "E0019", "催收戶不可變[下次調整日期]"); // 輸入資料錯誤
			}
			updLoanBorMain = true;
			tTemp2Vo.putParam("NxtRtAdjDtY", iNextRateAdjDateY);
			tTemp1Vo.putParam("NxtRtAdjDtY", iNextRateAdjDateY);
			tTemp1Vo.putParam("NxtRtAdjDt2", titaVo.getParam("NextRateAdjDate2"));
			tLoanBorMain.setNextAdjRateDate(this.parse.stringToInteger(titaVo.getParam("NextRateAdjDate2")));
		}
		// 到期日 到期日不可小於繳息迄日
		if (iMaturityDateY.equals("X")) {
			int wkMaturityDate = this.parse.stringToInteger(titaVo.getParam("MaturityDate2"));
			if (wkMaturityDate < tLoanBorMain.getPrevPayIntDate()) {
				throw new LogicException(titaVo, "E0019", "[到期日]不可小於繳息迄日"); // 輸入資料錯誤
			}
			updLoanBorMain = true;
			tTemp2Vo.putParam("MatDtY", iMaturityDateY);
			tTemp1Vo.putParam("MatDtY", iMaturityDateY);
			tTemp1Vo.putParam("MatDt2", titaVo.getParam("MaturityDate2"));
			tLoanBorMain.setMaturityDate(wkMaturityDate);
			dDateUtil.init();
			dDateUtil.setDate_1(tLoanBorMain.getDrawdownDate());
			dDateUtil.setDate_2(tLoanBorMain.getMaturityDate());
			dDateUtil.dateDiffSp();
			tLoanBorMain.setLoanTermYy(dDateUtil.getYears());
			tLoanBorMain.setLoanTermMm(dDateUtil.getMons());
			tLoanBorMain.setLoanTermDd(dDateUtil.getDays());
			if (tLoanBorMain.getBormNo() == 1) {
				tFacMain.setMaturityDate(wkMaturityDate);
				tFacMain.setLoanTermYy(dDateUtil.getYears());
				tFacMain.setLoanTermMm(dDateUtil.getMons());
				tFacMain.setLoanTermDd(dDateUtil.getDays());
			}
		}
		// 攤還方式 催收戶不可變更,按月繳息限中、短期
		if (iAmortizedCodeY.equals("X")) {
			String wkAmortizedCode = titaVo.getParam("AmortizedCode2");
			if (overdueFlag) {
				throw new LogicException(titaVo, "E0019", "催收戶不可變[攤還方式]"); // 輸入資料錯誤
			}
			if (wkAmortizedCode.equals("1")) { // 1.按月繳息(按期繳息到期還本)
				if (!(tFacMain.getAcctCode().equals("310") || tFacMain.getAcctCode().equals("320"))) {
					throw new LogicException(titaVo, "E0019", "攤還方式按月繳息限中、短期"); // 輸入資料錯誤
				}
			}
			updLoanBorMain = true;
			tTemp2Vo.putParam("AmoCdY", iAmortizedCodeY);
			tTemp1Vo.putParam("AmoCdY", iAmortizedCodeY);
			tTemp1Vo.putParam("AmoCd2", wkAmortizedCode);
			tLoanBorMain.setAmortizedCode(wkAmortizedCode);
			if (wkAmortizedCode.equals("1") || wkAmortizedCode.equals("2")) { // 1.按月繳息(按期繳息到期還本) 2.到期取息(到期繳息還本)
				tTemp2Vo.putParam("GrcPrdY", "X");
				tTemp2Vo.putParam("GrcDtY", "X");
				tLoanBorMain.setGracePeriod(0);
				tLoanBorMain.setGraceDate(0);
			}
			if (wkAmortizedCode.equals("2")) { // 2.到期取息(到期繳息還本)
				tTemp2Vo.putParam("FstDueDtY", "X");
				tLoanBorMain.setFirstDueDate(tLoanBorMain.getMaturityDate());
			}
		}
		// 繳息週期 催收戶不可變更
		// a.繳息週期不可為零
		// b.到期取息者是到期還本繳息，因此沒有繳息週期
		// c.週期基準為週者，繳息週期固定為雙週
		// d.繳息週期如有變更,將重算總期數.
		if (iPayIntFreqY.equals("X")) {
			if (overdueFlag) {
				throw new LogicException(titaVo, "E0019", "催收戶不可變[繳息週期]"); // 輸入資料錯誤
			}
			updLoanBorMain = true;
			tTemp2Vo.putParam("PayIntFqY", iPayIntFreqY);
			tTemp1Vo.putParam("PayIntFqY", iPayIntFreqY);
			tTemp1Vo.putParam("PayIntFq2", titaVo.getParam("PayIntFreq2"));
			tLoanBorMain.setPayIntFreq(this.parse.stringToInteger(titaVo.getParam("PayIntFreq2")));
		}
		// 還本週期 催收戶不可變更
		// a.還本週期如有變更,新推算出之下次還本日需>繳息迄日.
		// b.攤還方式為本金或本息平均時，還本週期不可為零
		// c.繳息週期不等於１時，還本週期須等於繳息週期
		// d.攤還方式為本息平均法時，還本週期須等於繳息週期
		// !按月繳息者是到期還本，因此沒有還本週期
		// !到期取息者是到期還本繳息，因此沒有還本週期
		// !攤還方式為本息平均法時，還本週期須等於繳息週期
		// !繳息週期不等於１時，還本週期須等於繳息週期
		// !繳息週期不等於１時，還本週期須等於繳息週期
		if (iRepayFreqY.equals("X")) {
			if (overdueFlag) {
				throw new LogicException(titaVo, "E0019", "催收戶不可變[還本週期]"); // 輸入資料錯誤
			}
			updLoanBorMain = true;
			tTemp2Vo.putParam("RpyFqY", iRepayFreqY);
			tTemp1Vo.putParam("RpyFqY", iRepayFreqY);
			tTemp1Vo.putParam("RpyFq2", titaVo.getParam("RepayFreq2"));
			tLoanBorMain.setRepayFreq(this.parse.stringToInteger(titaVo.getParam("RepayFreq2")));
		}
		// 禁領清償期限 催收戶不可變更

		// 寬限到期日 未繳期款才可變更
		// a.攤還方式為本息或本金平均戶時，才可輸入寬限到期日
		// b.寬限到期日需≧首次應繳日且寬限到期日需<撥款到期日
		if (iGraceDateY.equals("X")) {
			int wkGraceDate = this.parse.stringToInteger(titaVo.getParam("GraceDate2"));
			if (paidTermFlag) {
				throw new LogicException(titaVo, "E0019", "已繳期款不可變更[寬限到期日]"); // 輸入資料錯誤
			}
			if (!(tLoanBorMain.getAmortizedCode().equals("3") || tLoanBorMain.getAmortizedCode().equals("4"))) {
				if (wkGraceDate > 0) {
					throw new LogicException(titaVo, "E0019", "攤還方式為本息或本金平均戶時，才可輸入[寬限到期日]"); // 輸入資料錯誤
				}
			}
			updLoanBorMain = true;
			tTemp2Vo.putParam("GrcDtY", iGraceDateY);
			tTemp1Vo.putParam("GrcDtY", iGraceDateY);
			tTemp1Vo.putParam("GrcDt2", titaVo.getParam("GraceDate2"));
			tLoanBorMain.setGraceDate(wkGraceDate == 9999999 ? 0 : wkGraceDate);
		}
		// 首次應繳日 未繳期款才可變更
		// 首次繳款日必須大於撥款日期
		// 首次繳款日不可大於到期日
		// 攤還方式為到期取息者，其首次應繳日應與為到期日
		if (iFirstDueDateY.equals("X")) {
			int wkFirstDueDate = this.parse.stringToInteger(titaVo.getParam("FirstDueDate2"));
			if (paidTermFlag) {
				throw new LogicException(titaVo, "E0019", "已繳期款不可變更[首次應繳日]"); // 輸入資料錯誤
			}
			if (wkFirstDueDate < tLoanBorMain.getDrawdownDate()) {
				throw new LogicException(titaVo, "E0019", "[首次繳款日]必須大於撥款日期"); // 輸入資料錯誤
			}
			if (wkFirstDueDate > tLoanBorMain.getMaturityDate()) {
				throw new LogicException(titaVo, "E0019", "[首次繳款日]不可大於到期日"); // 輸入資料錯誤
			}
			if (tLoanBorMain.getAmortizedCode().equals("2")) { // 2.到期取息(到期繳息還本)
				if (wkFirstDueDate != tLoanBorMain.getMaturityDate()) {
					throw new LogicException(titaVo, "E0019", "攤還方式為到期取息者，其[首次應繳日]應與為到期日"); // 輸入資料錯誤
				}
			}
			updLoanBorMain = true;
			tTemp2Vo.putParam("FstDueDtY", iFirstDueDateY);
			tTemp1Vo.putParam("FstDueDtY", iFirstDueDateY);
			tTemp1Vo.putParam("FstDueDt2", titaVo.getParam("FirstDueDate2"));
			tLoanBorMain.setFirstDueDate(wkFirstDueDate);
		}
		// 指定應繳日 未繳期款才可變更
		if (iSpecificDdY.equals("X")) {
			if (paidTermFlag) {
				throw new LogicException(titaVo, "E0019", "已繳期款不可變更[指定應繳日]"); // 輸入資料錯誤
			}
			updLoanBorMain = true;
			tTemp2Vo.putParam("SpcDdY", iSpecificDdY);
			tTemp1Vo.putParam("SpcDdY", iSpecificDdY);
			tTemp1Vo.putParam("SpcDd2", titaVo.getParam("SpecificDd2"));
			tLoanBorMain.setSpecificDd(this.parse.stringToInteger(titaVo.getParam("SpecificDd2")));
			// 重算指定基準日期
			int wkSpecificDate = loanCom.getSpecificDate(tLoanBorMain.getSpecificDd(), tLoanBorMain.getFirstDueDate(),
					tLoanBorMain.getPayIntFreq());
			tLoanBorMain.setSpecificDate(wkSpecificDate);
		}
		// 帳管費 未繳期款才可變更, 帳管費已達帳,不可修改
		if (iAcctFeeY.equals("X")) {
			if (paidTermFlag) {
				throw new LogicException(titaVo, "E0019", "已繳期款不可變更[帳管費]"); // 輸入資料錯誤
			}
			if (iFacmNo == 0 || iBormNo == 0) {
				throw new LogicException(titaVo, "E0019", "變更[帳管費]，額度編號及撥款序號必須輸入"); // 輸入資料錯誤
			}
			updLoanBorMain = true;
			BigDecimal wkAcctFee = this.parse.stringToBigDecimal(titaVo.getParam("AcctFee2N"));

			// 帳管費處理
			if (tLoanBorMain.getAcctFee().compareTo(BigDecimal.ZERO) > 0) {
				if (wkAcctFee.compareTo(tLoanBorMain.getAcctFee()) >= 0) {
				}
				BigDecimal rvAmt = wkAcctFee.compareTo(tLoanBorMain.getAcctFee()) >= 0
						? wkAcctFee.subtract(tLoanBorMain.getAcctFee())
						: tLoanBorMain.getAcctFee().subtract(wkAcctFee);
				// 查詢會計銷帳檔
				AcReceivable tAcReceivable = acReceivableService.findById(
						new AcReceivableId("F10", iCustNo, wkFacmNo, FormatUtil.pad9(String.valueOf(wkBormNo), 3)),
						titaVo);
				if (tAcReceivable == null) {
					throw new LogicException(titaVo, "E0001", "會計銷帳檔 業務科目代號 = F10 戶號 = " + iCustNo + " 額度編號 = "
							+ wkFacmNo + " 銷帳編號 = " + FormatUtil.pad9(String.valueOf(wkBormNo), 3)); // 輸入資料錯誤
				}
				if (tAcReceivable.getClsFlag() == 1) {
					throw new LogicException(titaVo, "E3087", "會計銷帳檔 業務科目代號 = F10 戶號 = " + iCustNo + " 額度編號 = "
							+ wkFacmNo + " 銷帳編號 = " + FormatUtil.pad9(String.valueOf(wkBormNo), 3)); // 帳管費已達帳,不可修改
				}
				tAcReceivable = new AcReceivable();
				tAcReceivable.setReceivableFlag(3); // 3-未收費用
				tAcReceivable.setAcctCode("F10"); // F10 帳管費
				tAcReceivable.setCustNo(iCustNo);
				tAcReceivable.setFacmNo(wkFacmNo);
				tAcReceivable.setRvNo(FormatUtil.pad9(String.valueOf(wkBormNo), 3));
				tAcReceivable.setRvAmt(rvAmt);
				lAcReceivable.add(tAcReceivable);
			} else {
				tAcReceivable = new AcReceivable();
				tAcReceivable.setReceivableFlag(3); // 3-未收費用
				tAcReceivable.setAcctCode("F10"); // F10 帳管費
				tAcReceivable.setCustNo(iCustNo);
				tAcReceivable.setFacmNo(wkFacmNo);
				tAcReceivable.setRvNo(FormatUtil.pad9(String.valueOf(wkBormNo), 3));
				tAcReceivable.setRvAmt(wkAcctFee);
				lAcReceivable.add(tAcReceivable);
			}
			tTemp2Vo.putParam("AcFeeY", iAcctFeeY);
			tTemp1Vo.putParam("AcFeeY", iAcctFeeY);
			tTemp1Vo.putParam("AcFee2", titaVo.getParam("AcctFee2"));
			tLoanBorMain.setAcctFee(wkAcctFee);
		}
//		// 與借款人關係
//		if (iRelationCodeY.equals("X")) {
//			updLoanBorMain = true;
//			tTemp2Vo.putParam("RelCdY", iRelationCodeY);
//			tTemp1Vo.putParam("RelCdY", iRelationCodeY);
//			tTemp1Vo.putParam("RelCd2", titaVo.getParam("RelationCode2"));
//			tLoanBorMain.setRelationCode(titaVo.getParam("RelationCode2"));
//		}
//		// 帳戶戶名
//		if (iRelationNameY.equals("X")) {
//			updLoanBorMain = true;
//			tTemp2Vo.putParam("RelNmY", iRelationNameY);
//			tTemp1Vo.putParam("RelNmY", iRelationNameY);
//			tTemp1Vo.putParam("RelNm2", titaVo.getParam("RelationName2"));
//			tLoanBorMain.setRelationName(titaVo.getParam("RelationName2"));
//		}
//		// 身份證字號
//		if (iRelationIdY.equals("X")) {
//			updLoanBorMain = true;
//			tTemp2Vo.putParam("RelIdY", iRelationIdY);
//			tTemp1Vo.putParam("RelIdY", iRelationIdY);
//			tTemp1Vo.putParam("RelId2", titaVo.getParam("RelationId2"));
//			tLoanBorMain.setRelationId(titaVo.getParam("RelationId2"));
//		}
//		// 出生日期
//		if (iRelationBirthdayY.equals("X")) {
//			updLoanBorMain = true;
//			tTemp2Vo.putParam("RelBirdyY", iRelationBirthdayY);
//			tTemp1Vo.putParam("RelBirdyY", iRelationBirthdayY);
//			tTemp1Vo.putParam("RelBirdy2", titaVo.getParam("RelationBirthday2"));
//			tLoanBorMain.setRelationBirthday(this.parse.stringToInteger(titaVo.getParam("RelationBirthday2")));
//		}
//		// 性別
//		if (iRelationGenderY.equals("X")) {
//			updLoanBorMain = true;
//			tTemp2Vo.putParam("RelGenderY", iRelationGenderY);
//			tTemp1Vo.putParam("RelGenderY", iRelationGenderY);
//			tTemp1Vo.putParam("RelGender2", titaVo.getParam("RelationGender2"));
//			tLoanBorMain.setRelationGender(titaVo.getParam("RelationGender2"));
//		}
		// 計件代碼
		if (iPieceCodeY.equals("X")) {
			updLoanBorMain = true;
			tTemp2Vo.putParam("PieceCodeY", iPieceCodeY);
			tTemp1Vo.putParam("PieceCodeY", iPieceCodeY);
			tTemp1Vo.putParam("PieceCode2", titaVo.getParam("PieceCode2"));
			tLoanBorMain.setPieceCode(titaVo.getParam("PieceCode2"));
		}

		// 計件代碼2
		if (iPieceCodeSecondY.equals("X")) {
			updLoanBorMain = true;
			tTemp2Vo.putParam("PieceCodeSecondY", iPieceCodeSecondY);
			tTemp1Vo.putParam("PieceCodeSecondY", iPieceCodeSecondY);
			tTemp1Vo.putParam("PieceCodeSecond2", titaVo.getParam("PieceCodeSecond2"));
			tLoanBorMain.setPieceCodeSecond(titaVo.getParam("PieceCodeSecond2"));
		}

		// 計件代碼2金額
		if (iPieceCodeSecondAmtY.equals("X")) {
			updLoanBorMain = true;
			tTemp2Vo.putParam("PieceCodeSecondAmtY", iPieceCodeSecondAmtY);
			tTemp1Vo.putParam("PieceCodeSecondAmtY", iPieceCodeSecondAmtY);
			tTemp1Vo.putParam("PieceCodeSecondAmt2", titaVo.getParam("PieceCodeSecondAmt2"));
			tLoanBorMain.setPieceCodeSecondAmt(parse.stringToBigDecimal(titaVo.getParam("PieceCodeSecondAmt2")));
		}

		// 計息方式
		if (iIntCalcCodeY.equals("X")) {
			updLoanBorMain = true;
			tTemp2Vo.putParam("IntCalcCodeY", iIntCalcCodeY);
			tTemp1Vo.putParam("IntCalcCodeY", iIntCalcCodeY);
			tTemp1Vo.putParam("IntCalcCode2", titaVo.getParam("IntCalcCode2"));
			tLoanBorMain.setIntCalcCode(titaVo.getParam("IntCalcCode2"));

		}
		// -----------------------------------------------------------------------
		// 內容變更後一致性檢查及維護
		if (iRateCodeY.equals("X")) {
			if (tLoanBorMain.getRateCode().equals("3")) {
				if (tLoanBorMain.getRateAdjFreq() == 0) {
					throw new LogicException(titaVo, "E0019", "利率區分為定期機動者，必須輸入利率調整週"); // 輸入資料錯誤
				}
				if (tLoanBorMain.getFirstAdjRateDate() == 0) {
					throw new LogicException(titaVo, "E0019", "利率區分為定期機動者，必須輸入[首次調整日期]"); // 輸入資料錯誤
				}
				if (tLoanBorMain.getNextAdjRateDate() == 0) {
					throw new LogicException(titaVo, "E0019", "利率區分為定期機動者，必須輸入[下次調整日期]"); // 輸入資料錯誤
				}
			} else {
				tLoanBorMain.setRateAdjFreq(0); // 利率調整週期
				tLoanBorMain.setFirstAdjRateDate(0); // 首次調整日期
				tLoanBorMain.setNextAdjRateDate(0); // 下次調整日期
			}
		}
		// 寬限到期日需≧首次應繳日且寬限到期日需<撥款到期日
		if (iGraceDateY.equals("X") && tLoanBorMain.getGraceDate() > 0) {
			if (!(tLoanBorMain.getGraceDate() >= tLoanBorMain.getFirstDueDate()
					&& tLoanBorMain.getGraceDate() < tLoanBorMain.getMaturityDate())) {
				throw new LogicException(titaVo, "E0019",
						"[寬限到期日]需≧首次應繳日且寬限到期日需<撥款到期日 " + " 寬限到期日 =" + tLoanBorMain.getGraceDate() + " 首次應繳日 = "
								+ tLoanBorMain.getFirstDueDate() + " 撥款到期日 = " + tLoanBorMain.getMaturityDate()); // 輸入資料錯誤
			}
		}
		// 重算寬限期數, 下次還本日
		if (iGraceDateY.equals("X")) {
			if (tLoanBorMain.getGraceDate() > 0) {
				wkGracePeriod = loanCom.getGracePeriod(tLoanBorMain.getAmortizedCode(), tLoanBorMain.getFreqBase(),
						tLoanBorMain.getPayIntFreq(), tLoanBorMain.getSpecificDate(), tLoanBorMain.getSpecificDd(),
						tLoanBorMain.getGraceDate());
			}
			tLoanBorMain.setGracePeriod(wkGracePeriod);
			wkDate = loanCom.getNextRepayDate(tLoanBorMain.getAmortizedCode(), tLoanBorMain.getRepayFreq(),
					tLoanBorMain.getFreqBase(), tLoanBorMain.getSpecificDate(), tLoanBorMain.getSpecificDd(),
					tLoanBorMain.getPrevRepaidDate() == 0 ? tLoanBorMain.getDrawdownDate()
							: tLoanBorMain.getPrevRepaidDate(),
					tLoanBorMain.getMaturityDate(), tLoanBorMain.getGraceDate());
			tLoanBorMain.setNextRepayDate(wkDate);
		}
		// 到期日變更，須重算期數
		if (iMaturityDateY.equals("X")) {
			int wkTotalPeriod = loanCom.getTotalPeriod(tLoanBorMain.getAmortizedCode(), tLoanBorMain.getFreqBase(),
					tLoanBorMain.getPayIntFreq(), tLoanBorMain.getDrawdownDate(), tLoanBorMain.getMaturityDate());
			tLoanBorMain.setTotalPeriod(wkTotalPeriod);
		}
		// 到期日、 攤還方式、 繳息週期、 寬限到期日變更，須重算期金
		if (iMaturityDateY.equals("X") || iAmortizedCodeY.equals("X") || iPayIntFreqY.equals("X")
				|| iGraceDateY.equals("X") || iApproveRateY.equals("X")) {
			wkGracePeriod = loanCom.getGracePeriod(tLoanBorMain.getAmortizedCode(), tLoanBorMain.getFreqBase(),
					tLoanBorMain.getPayIntFreq(), tLoanBorMain.getSpecificDate(), tLoanBorMain.getSpecificDd(),
					tLoanBorMain.getGraceDate());
			wkDueAmt = loanDueAmtCom.getDueAmt(tLoanBorMain.getDrawdownAmt(), tLoanBorMain.getStoreRate(),
					tLoanBorMain.getAmortizedCode(), tLoanBorMain.getFreqBase(),
					tLoanBorMain.getPaidTerms() > wkGracePeriod
							? tLoanBorMain.getTotalPeriod() - tLoanBorMain.getPaidTerms()
							: tLoanBorMain.getTotalPeriod() - wkGracePeriod,
					0, tLoanBorMain.getPayIntFreq(), tLoanBorMain.getFinalBal(), titaVo);
			tTemp2Vo.putParam("DueAmtY", "X");
			tLoanBorMain.setDueAmt(wkDueAmt);
		}
		// 指定應繳日, 須重算下次繳息日,下次還本日,首次應繳日
		if (iSpecificDdY.equals("X")) {
			wkDate = loanCom.getNextPayIntDate(tLoanBorMain.getAmortizedCode(), tLoanBorMain.getPayIntFreq(),
					tLoanBorMain.getFreqBase(), tLoanBorMain.getSpecificDate(), tLoanBorMain.getSpecificDd(),
					tLoanBorMain.getSpecificDate(), tLoanBorMain.getMaturityDate());
			tLoanBorMain.setNextPayIntDate(wkDate);
			tLoanBorMain.setFirstDueDate(wkDate);
			wkDate = loanCom.getNextRepayDate(tLoanBorMain.getAmortizedCode(), tLoanBorMain.getRepayFreq(),
					tLoanBorMain.getFreqBase(), tLoanBorMain.getSpecificDate(), tLoanBorMain.getSpecificDd(),
					tLoanBorMain.getSpecificDate(), tLoanBorMain.getMaturityDate(), tLoanBorMain.getGraceDate());
			tLoanBorMain.setNextRepayDate(wkDate);
		}
		if (updLoanBorMain) {
			tTemp2Vo.putParam("UpdLoanBorMain", "X");
			tTemp2Vo.putParam("BorxNo", wkBorxNo);
		}
	}

	private void LoanOverdueRoutine() throws LogicException {
		this.info("LoanOverdueRoutine ... ");
		updLoanOverdue = false;
		// 催收呆帳檔
		tLoanOverdue = loanOverdueService.holdById(new LoanOverdueId(iCustNo, wkFacmNo, wkBormNo, wkOvduNo), titaVo);
		if (tLoanOverdue == null) {
			throw new LogicException(titaVo, "E0006",
					"催收呆帳檔 戶號 = " + iCustNo + " 額度編號 = " + wkFacmNo + " 撥款序號 = " + wkBormNo + " 催收序號 = " + wkOvduNo); // 鎖定資料時，發生錯誤
		}
		tTemp1Vo.putParam("Rmk1", tLoanOverdue.getRemark());
		tTemp1Vo.putParam("PrsDt1", tLoanOverdue.getProcessDate());
		//
		tTemp2Vo.putParam("Rmk1", tLoanOverdue.getRemark());
		tTemp2Vo.putParam("PrsDt1", tLoanOverdue.getProcessDate());
		beforeLoanOverdue = (LoanOverdue) datalog.clone(tLoanOverdue);
		// 備註
		if (iRemarkY.equals("X")) {
			updLoanOverdue = true;
			tTemp2Vo.putParam("RmkY", iRemarkY);
			tTemp1Vo.putParam("RmkY", iRemarkY);
			tTemp1Vo.putParam("Rmk2", titaVo.getParam("Remark2"));
			tLoanOverdue.setRemark(titaVo.getParam("Remark2"));
		}
		// 處理日期
		if (iProcessDateY.equals("X")) {
			updLoanOverdue = true;
			tTemp2Vo.putParam("PrsDtY", iProcessDateY);
			tTemp1Vo.putParam("PrsDtY", iProcessDateY);
			tTemp1Vo.putParam("PrsDt2", titaVo.getParam("ProcessDate2"));
			tLoanOverdue.setProcessDate(this.parse.stringToInteger(titaVo.getParam("ProcessDate2")));
		}
		if (updLoanOverdue) {
			tTemp2Vo.putParam("UpdLoanOverdue", "X");
		}
	}

	private void AcDetailRoutine() throws LogicException {
		this.info("AcDetailRoutine ... ");

		if (!this.txBuffer.getTxCom().isBookAcYes()) {
			return;
		}
		Slice<LoanBorMain> slBor = loanBorMainService.bormCustNoEq(iCustNo, wkFacmNo, wkFacmNo, wkBormNoStart,
				wkBormNoEnd, 0, Integer.MAX_VALUE, titaVo);
		List<LoanBorMain> lBor = slBor == null ? null : slBor.getContent();
		if (lBor == null || lBor.size() == 0) {
			return;
		}
		for (LoanBorMain ln : lBor) {
			if (!(ln.getStatus() == 0 || ln.getStatus() == 4)) {
				continue;
			}
			// 借方 變更後
			acDetail = new AcDetail();
			acDetail.setDbCr("D");
			acDetail.setAcctCode(tFacMain.getAcctCode());
			acDetail.setTxAmt(ln.getLoanBal());
			acDetail.setCustNo(ln.getCustNo());
			acDetail.setFacmNo(ln.getFacmNo());
			acDetail.setBormNo(ln.getBormNo());
			lAcDetail.add(acDetail);
			// 貸方 變更前
			acDetail = new AcDetail();
			acDetail.setDbCr("C");
			acDetail.setAcctCode(wkBeforeAcctCode);
			acDetail.setTxAmt(ln.getLoanBal());
			acDetail.setCustNo(ln.getCustNo());
			acDetail.setFacmNo(ln.getFacmNo());
			acDetail.setBormNo(ln.getBormNo());
			lAcDetail.add(acDetail);
		}
	}

	// 新增交易暫存檔(放款資料)
	private void AddTxTempRoutine() throws LogicException {
		this.info("addRepayTxTempBormRoutine ... ");

		TxTemp tTxTemp = new TxTemp();
		TxTempId tTxTempId = new TxTempId();
		loanCom.setTxTemp(tTxTempId, tTxTemp, iCustNo, wkFacmNo, wkBormNo, wkOvduNo, titaVo);
		tTxTemp.setText(tTemp2Vo.getJsonString());
		try {
			txTempService.insert(tTxTemp, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "交易暫存檔 Key = " + tTxTempId); // 新增資料時，發生錯誤 }
		}
	}

	// 訂正時, 還原額度檔
	private void RestoredFacMainRoutine() throws LogicException {
		this.info("RestoredFacMainRoutine ... ");

		tFacMain = facMainService.holdById(new FacMainId(wkCustNo, wkFacmNo), titaVo);
		if (tFacMain == null) {
			throw new LogicException(titaVo, "E0006", "額度主檔 戶號 = " + wkCustNo + " 額度編號 = " + wkFacmNo); // 鎖定資料時，發生錯誤
		}
		// if (tFacMain.getActFg() == 1 && !titaVo.isHcodeErase()) {
		// throw new LogicException(titaVo, "E0021",
		// "額度檔 戶號 = " + tFacMain.getCustNo() + " 額度編號 = " + tFacMain.getFacmNo()); //
		// 該筆資料待放行中
		// }
		beforeFacMain = (FacMain) datalog.clone(tFacMain);
		if (titaVo.isHcodeErase()) {
			// 額度交易訂正交易須由最後一筆交易開始訂正
			loanCom.checkEraseFacmTxSeqNo(tFacMain, titaVo);
		}
		// 科目
		if (tTemp1Vo.getParam("FcAcCdY").equals("X")) {
			tFacMain.setAcctCode(tTemp1Vo.getParam("FcAcCd1"));
			// 帳務處理
			AcDetailRoutine();
		}
		// 到期日
		if (tTemp1Vo.getParam("FcMatDtY").equals("X")) {
			tFacMain.setMaturityDate(this.parse.stringToInteger(tTemp1Vo.getParam("FcMatDt1")));
		}

		// 到期日(額度)
		if (tTemp1Vo.getParam("FcMatDtY").equals("X")) {
			tFacMain.setLoanTermYy(this.parse.stringToInteger(tTemp1Vo.getParam("FcLnTmYy1")));
			tFacMain.setLoanTermMm(this.parse.stringToInteger(tTemp1Vo.getParam("FcLnTmMm1")));
			tFacMain.setLoanTermDd(this.parse.stringToInteger(tTemp1Vo.getParam("FcLnTmDd1")));
		}
		// 繳款方式
		if (tTemp1Vo.getParam("FcRpyCdY").equals("X")) {
			tFacMain.setRepayCode(this.parse.stringToInteger(tTemp1Vo.getParam("FcRpyCd1")));
		}
//		// 扣款銀行代號
//		if (tTemp1Vo.getParam("FcRpyBkY").equals("X")) {
//			tFacMain.setRepayBank(tTemp1Vo.getParam("FcRpyBk1"));
//		}
//		// 扣款帳號
//		if (tTemp1Vo.getParam("FcRpyAcNoY").equals("X")) {
//			tFacMain.setRepayAcctNo(this.parse.stringToBigDecimal(tTemp1Vo.getParam("FcRpyAcNo1")));
//		}
//		// 郵局存款別
//		if (tTemp1Vo.getParam("FcPostCdY").equals("X")) {
//			tFacMain.setPostCode(tTemp1Vo.getParam("FcPostCd1"));
//		}
		// 利率區分
		if (tTemp1Vo.getParam("FcRtCdY").equals("X")) {
			tFacMain.setRateCode(tTemp1Vo.getParam("FcRtCd1"));
		}
		// TODO:核准利率
		if (tTemp1Vo.getParam("FcApproveRateY").equals("X")) {
			tFacMain.setApproveRate(this.parse.stringToBigDecimal(tTemp1Vo.getParam("FcApproveRate1")));
		}
		// 利率加減碼
		if (tTemp1Vo.getParam("FcRtIncrY").equals("X")) {
			tFacMain.setRateIncr(this.parse.stringToBigDecimal(tTemp1Vo.getParam("FcRtIncr1")));
		}
		// 個別加減碼
		if (tTemp1Vo.getParam("FcIduIncrY").equals("X")) {
			tFacMain.setIndividualIncr(this.parse.stringToBigDecimal(tTemp1Vo.getParam("FcIduIncr1")));
		}
		// 利率調整週期
		if (tTemp1Vo.getParam("FcRtAdjFqY").equals("X")) {
			tFacMain.setRateAdjFreq(this.parse.stringToInteger(tTemp1Vo.getParam("FcRtAdjFq1")));
		}
		// 攤還方式
		if (tTemp1Vo.getParam("FcAmoCdY").equals("X")) {
			tFacMain.setAmortizedCode(tTemp1Vo.getParam("FcAmoCd1"));
			if (tTemp1Vo.getParam("FcGrcPrdY").equals("X")) {
				tFacMain.setGracePeriod(this.parse.stringToInteger(tTemp1Vo.getParam("FcGrcPrd1")));
			}
		}
		// 繳息週期
		if (tTemp1Vo.getParam("FcPayIntFqY").equals("X")) {
			tFacMain.setPayIntFreq(this.parse.stringToInteger(tTemp1Vo.getParam("FcPayIntFq1")));
		}
		// 還本週期
		if (tTemp1Vo.getParam("FcRpyFqY").equals("X")) {
			tFacMain.setRepayFreq(this.parse.stringToInteger(tTemp1Vo.getParam("FcRpyFq1")));
		}
		// 帳管費
		if (tTemp1Vo.getParam("FcAcFeeY").equals("X")) {
			tFacMain.setAcctFee(this.parse.stringToBigDecimal(tTemp1Vo.getParam("FcAcFee1")));
		}
//		// 與借款人關係
//		if (tTemp1Vo.getParam("FcRelCdY").equals("X")) {
//			tFacMain.setRelationCode(tTemp1Vo.getParam("FcRelCd1"));
//		}
//		// 帳戶戶名
//		if (tTemp1Vo.getParam("FcRelNmY").equals("X")) {
//			tFacMain.setRelationName(tTemp1Vo.getParam("FcRelNm1"));
//		}
//		// 身份證字號
//		if (tTemp1Vo.getParam("FcRelIdY").equals("X")) {
//			tFacMain.setRelationId(tTemp1Vo.getParam("FcRelId1"));
//		}
//		// 出生日期
//		if (tTemp1Vo.getParam("FcRelBirdyY").equals("X")) {
//			tFacMain.setRelationBirthday(this.parse.stringToInteger(tTemp1Vo.getParam("FcRelBirdy1")));
//		}
//		// 性別
//		if (tTemp1Vo.getParam("FcRelGenderY").equals("X")) {
//			tFacMain.setRelationGender(tTemp1Vo.getParam("FcRelGender1"));
//		}
		// 計息方式
		if (tTemp1Vo.getParam("FcIntCaCdY").equals("X")) {
			tFacMain.setIntCalcCode(tTemp1Vo.getParam("FcIntCaCd1"));
		}
		tFacMain.setActFg(this.parse.stringToInteger(tTemp1Vo.getParam("FcActFg")));
		tFacMain.setLastAcctDate(this.parse.stringToInteger(tTemp1Vo.getParam("FcLastEntDy")));
		tFacMain.setLastKinbr(tTemp1Vo.getParam("FcLastKinbr"));
		tFacMain.setLastTlrNo(tTemp1Vo.getParam("FcLastTlrNo"));
		tFacMain.setLastTxtNo(tTemp1Vo.getParam("FcLastTxtNo"));
		try {
			tFacMain = facMainService.update2(tFacMain, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007",
					"額度主檔 戶號 = " + iCustNo + " 額度編號 = " + wkFacmNo + " " + e.getErrorMsg()); // 更新資料時，發生錯誤
		}
		datalog.setEnv(titaVo, beforeFacMain, tFacMain);
		datalog.exec(iRemark);
	}

	// 訂正時, 還原撥款檔
	private void RestoredLoanBorMainRoutine() throws LogicException {
		this.info("RestoredLoanBorMainRoutine ... ");

		// 鎖定撥款主檔
		tLoanBorMain = loanBorMainService.holdById(new LoanBorMainId(iCustNo, wkFacmNo, wkBormNo), titaVo);
		if (tLoanBorMain == null) {
			throw new LogicException(titaVo, "E0006",
					"撥款主檔 戶號 = " + iCustNo + " 額度編號 = " + wkFacmNo + " 撥款序號 = " + wkBormNo); // 鎖定資料時，發生錯誤
		}
		wkNewBorxNo = tLoanBorMain.getLastBorxNo() + 1;
		beforeLoanBorMain = (LoanBorMain) datalog.clone(tLoanBorMain);
		if (titaVo.isHcodeErase()) {
			// 放款交易訂正交易須由最後一筆交易開始訂正
			loanCom.checkEraseBormTxSeqNo(tLoanBorMain, titaVo);
		}
		// 利率區分
		if (tTemp1Vo.getParam("RtCdY").equals("X")) {
			tLoanBorMain.setRateCode(tTemp1Vo.getParam("RtCd1"));
		}
		// TODO:核准利率
		if (tTemp1Vo.getParam("FcApproveRateY").equals("X")) {
			tLoanBorMain.setApproveRate(this.parse.stringToBigDecimal(tTemp1Vo.getParam("FcApproveRate2")));
			tLoanBorMain.setStoreRate(this.parse.stringToBigDecimal(tTemp1Vo.getParam("FcApproveRate2")));
		}
		// 利率加減碼
		if (tTemp1Vo.getParam("RtIncrY").equals("X")) {
			tLoanBorMain.setRateIncr(this.parse.stringToBigDecimal(tTemp1Vo.getParam("RtIncr1")));
		}
		// 個別加減碼
		if (tTemp1Vo.getParam("IduIncrY").equals("X")) {
			tLoanBorMain.setIndividualIncr(this.parse.stringToBigDecimal(tTemp1Vo.getParam("IduIncr1")));
		}
		// 更新放款利率變動檔
		if (tTemp1Vo.getParam("RtIncrY").equals("X") || tTemp1Vo.getParam("IduIncrY").equals("X")
				|| tTemp1Vo.getParam("FcApproveRateY").equals("X")) {
			updLoanRateChangeRoutine();
		}

		// 利率調整週期
		if (tTemp1Vo.getParam("RtAdjFqY").equals("X")) {
			tLoanBorMain.setRateAdjFreq(this.parse.stringToInteger(tTemp1Vo.getParam("RtAdjFq1")));
		}
		// 首次調整日期
		if (tTemp1Vo.getParam("FstRtAdjDtY").equals("X")) {
			tLoanBorMain.setFirstAdjRateDate(this.parse.stringToInteger(tTemp1Vo.getParam("FstRtAdjDt1")));
		}
		// 下次調整日期
		if (tTemp1Vo.getParam("NxtRtAdjDtY").equals("X")) {
			tLoanBorMain.setNextAdjRateDate(this.parse.stringToInteger(tTemp1Vo.getParam("NxtRtAdjDt1")));
		}
		// 到期日
		if (tTemp1Vo.getParam("MatDtY").equals("X")) {
			tLoanBorMain.setMaturityDate(this.parse.stringToInteger(tTemp1Vo.getParam("MatDt1")));
			tLoanBorMain.setTotalPeriod(this.parse.stringToInteger(tTemp1Vo.getParam("TotPrd1")));
			tLoanBorMain.setLoanTermYy(this.parse.stringToInteger(tTemp1Vo.getParam("LnTmYy1")));
			tLoanBorMain.setLoanTermMm(this.parse.stringToInteger(tTemp1Vo.getParam("LnTmMm1")));
			tLoanBorMain.setLoanTermDd(this.parse.stringToInteger(tTemp1Vo.getParam("LnTmDd1")));
		}
		// 攤還方式
		if (tTemp1Vo.getParam("AmoCdY").equals("X")) {
			tLoanBorMain.setAmortizedCode(tTemp1Vo.getParam("AmoCd1"));
			if (tTemp1Vo.getParam("GrcPrdY").equals("X")) {
				tLoanBorMain.setGracePeriod(this.parse.stringToInteger(tTemp1Vo.getParam("GrcPrd1")));
			}
			if (tTemp1Vo.getParam("GrcDtY").equals("X")) {
				tLoanBorMain.setGraceDate(this.parse.stringToInteger(tTemp1Vo.getParam("GrcDt1")));
			}
			if (tTemp1Vo.getParam("FstDueDtY").equals("X")) {
				tLoanBorMain.setFirstDueDate(this.parse.stringToInteger(tTemp1Vo.getParam("FstDueDt1")));
			}
		}
		// 繳息週期
		if (tTemp1Vo.getParam("PayIntFqY").equals("X")) {
			tLoanBorMain.setPayIntFreq(this.parse.stringToInteger(tTemp1Vo.getParam("PayIntFq1")));
		}
		// 還本週期
		if (tTemp1Vo.getParam("RpyFqY").equals("X")) {
			tLoanBorMain.setRepayFreq(this.parse.stringToInteger(tTemp1Vo.getParam("RpyFq1")));
		}
		// 寬限到期日
		if (tTemp1Vo.getParam("GrcDtY").equals("X")) {
			tLoanBorMain.setGraceDate(this.parse.stringToInteger(tTemp1Vo.getParam("GrcDt1")));
			tLoanBorMain.setGracePeriod(this.parse.stringToInteger(tTemp1Vo.getParam("GrcPrd1")));
			tLoanBorMain.setFirstDueDate(this.parse.stringToInteger(tTemp1Vo.getParam("FstDueDt1")));
			tLoanBorMain.setNextRepayDate(this.parse.stringToInteger(tTemp1Vo.getParam("NxtRpyDt1")));
		}
		// 首次應繳日
		if (tTemp1Vo.getParam("FstDueDtY").equals("X")) {
			tLoanBorMain.setFirstDueDate(this.parse.stringToInteger(tTemp1Vo.getParam("FstDueDt1")));
		}
		// 指定應繳日
		if (tTemp1Vo.getParam("SpcDdY").equals("X")) {
			tLoanBorMain.setSpecificDd(this.parse.stringToInteger(tTemp1Vo.getParam("SpcDd1")));
			tLoanBorMain.setSpecificDate(this.parse.stringToInteger(tTemp1Vo.getParam("SpcDt1")));
			tLoanBorMain.setNextPayIntDate(this.parse.stringToInteger(tTemp1Vo.getParam("NxtPyDt1")));
			tLoanBorMain.setNextRepayDate(this.parse.stringToInteger(tTemp1Vo.getParam("NxtRpyDt1")));
			tLoanBorMain.setFirstDueDate(this.parse.stringToInteger(tTemp1Vo.getParam("FstDueDt1")));
		}
		// 帳管費
		if (tTemp1Vo.getParam("AcFeeY").equals("X")) {
			tLoanBorMain.setAcctFee(this.parse.stringToBigDecimal(tTemp1Vo.getParam("AcFee1")));
		}
		// 與借款人關係
		if (tTemp1Vo.getParam("RelCdY").equals("X")) {
			tLoanBorMain.setRelationCode(tTemp1Vo.getParam("RelCd1"));
		}
		// 帳戶戶名
		if (tTemp1Vo.getParam("RelNmY").equals("X")) {
			tLoanBorMain.setRelationName(tTemp1Vo.getParam("RelNm1"));
		}
		// 身份證字號
		if (tTemp1Vo.getParam("RelIdY").equals("X")) {
			tLoanBorMain.setRelationId(tTemp1Vo.getParam("RelId1"));
		}
		// 出生日期
		if (tTemp1Vo.getParam("RelBirdyY").equals("X")) {
			tLoanBorMain.setRelationBirthday(this.parse.stringToInteger(tTemp1Vo.getParam("RelBirdy1")));
		}
		// 性別
		if (tTemp1Vo.getParam("RelGenderY").equals("X")) {
			tLoanBorMain.setRelationGender(tTemp1Vo.getParam("RelGender1"));
		}
		// 期金
		if (tTemp1Vo.getParam("DueAmtY").equals("X")) {
			tLoanBorMain.setDueAmt(this.parse.stringToBigDecimal(tTemp1Vo.getParam("DueAmt1")));
		}
		// 計件代碼
		if (tTemp1Vo.getParam("PieceCodeY").equals("X")) {
			tLoanBorMain.setPieceCode(tTemp1Vo.getParam("PieceCode1"));
		}
		// 計件代碼2
		if (tTemp1Vo.getParam("PieceCodeSecondY").equals("X")) {
			tLoanBorMain.setPieceCodeSecond(tTemp1Vo.getParam("PieceCodeSecond1"));
		}
		// 計件代碼2金額
		if (tTemp1Vo.getParam("PieceCodeSecondAmtY").equals("X")) {
			tLoanBorMain.setPieceCodeSecondAmt(parse.stringToBigDecimal(tTemp1Vo.getParam("PieceCodeSecondAmt1")));
		}

		// 計息方式
		if (tTemp1Vo.getParam("IntCalcCodeY").equals("X")) {
			tLoanBorMain.setIntCalcCode(tTemp1Vo.getParam("IntCalcCode1"));
		}
		tLoanBorMain.setLastBorxNo(wkNewBorxNo);
		tLoanBorMain.setActFg(this.parse.stringToInteger(tTemp1Vo.getParam("ActFg")));
		tLoanBorMain.setLastEntDy(this.parse.stringToInteger(tTemp1Vo.getParam("LastEntDy")));
		tLoanBorMain.setLastKinbr(tTemp1Vo.getParam("LastKinbr"));
		tLoanBorMain.setLastTlrNo(tTemp1Vo.getParam("LastTlrNo"));
		tLoanBorMain.setLastTxtNo(tTemp1Vo.getParam("LastTxtNo"));
		try {
			tLoanBorMain = loanBorMainService.update2(tLoanBorMain, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007",
					"放款主檔 戶號 = " + iCustNo + " 額度編號 = " + wkFacmNo + " 撥款序號 = " + wkBormNo + " " + e.getErrorMsg()); // 更新資料時，發生錯誤
		}
		this.titaVo.putParam("BormNo", tLoanBorMain.getBormNo());
		datalog.setEnv(titaVo, beforeLoanBorMain, tLoanBorMain);
		datalog.exec(iRemark);
	}

	// 訂正時, 還原催收檔
	private void RestoredLoanOverdueRoutine() throws LogicException {
		this.info("RestoredLoanOverdueRoutine ... ");

		tLoanOverdue = loanOverdueService.holdById(new LoanOverdueId(iCustNo, wkFacmNo, wkBormNo, wkOvduNo), titaVo);
		if (tLoanOverdue == null) {
			throw new LogicException(titaVo, "E0006",
					"催收呆帳檔 戶號 = " + iCustNo + " 額度編號 = " + wkFacmNo + " 撥款序號 = " + wkBormNo + " 催收序號 = " + wkOvduNo); // 鎖定資料時，發生錯誤
		}
		beforeLoanOverdue = (LoanOverdue) datalog.clone(tLoanOverdue);
		// 備註
		if (tTemp1Vo.getParam("RmkY").equals("X")) {
			tLoanOverdue.setRemark(tTemp1Vo.getParam("Rmk1"));
		}
		// 處理日期
		if (tTemp1Vo.getParam("PrsDtY").equals("X")) {
			tLoanOverdue.setProcessDate(this.parse.stringToInteger(tTemp1Vo.getParam("PrsDt1")));
		}
		try {
			tLoanOverdue = loanOverdueService.update2(tLoanOverdue, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "催收呆帳檔 戶號 = " + iCustNo + " 額度編號 = " + wkFacmNo + " 撥款序號 = "
					+ wkBormNo + " 催收序號 = " + wkOvduNo + " " + e.getErrorMsg()); // 更新資料時，發生錯誤
		}
		this.titaVo.putParam("BormNo", tLoanOverdue.getBormNo());
		datalog.setEnv(titaVo, beforeLoanOverdue, tLoanOverdue);
		datalog.exec(iRemark);
	}

	// 更新放款利率變動檔
	private void updLoanRateChangeRoutine() throws LogicException {
		this.info("updLoanRateChangeRoutine ... ");
		BigDecimal wkBaseRate = BigDecimal.ZERO;
		// 已繳息不可變更利率
		if (tLoanBorMain.getPrevPayIntDate() > tLoanBorMain.getDrawdownDate()) {
			throw new LogicException(titaVo, "E0015", "上次繳息日:" + tLoanBorMain.getPrevPayIntDate()); // 檢查錯誤
		}
		LoanRateChange tLoanRateChange = loanRateChangeService.holdById(new LoanRateChangeId(tLoanBorMain.getCustNo(),
				tLoanBorMain.getFacmNo(), tLoanBorMain.getBormNo(), tLoanBorMain.getDrawdownDate() + 19110000), titaVo);

		if (tLoanRateChange == null) {
			throw new LogicException(titaVo, "E0006", "階梯式利率請由L3932借戶利率變更 " + tLoanBorMain.getLoanBorMainId()
					+ " 生效日期 = " + tLoanBorMain.getDrawdownDate()); // 鎖定資料時，發生錯誤
		}
		BigDecimal iApproveRate = BigDecimal.ZERO;
		if (iApproveRateY.equals("X")) {
			iApproveRate = parse.stringToBigDecimal(titaVo.getParam("ApproveRate2"));
		} else {
			iApproveRate = parse.stringToBigDecimal(titaVo.getParam("ApproveRate1"));
		}

		if ("Y".equals(tLoanRateChange.getIncrFlag())) {
			wkBaseRate = tLoanRateChange.getFitRate().subtract(tLoanRateChange.getRateIncr());
			tLoanRateChange.setRateIncr(tLoanBorMain.getRateIncr());
			tLoanRateChange.setFitRate(iApproveRate);

			// 核准利率
		} else {
			wkBaseRate = tLoanRateChange.getFitRate().subtract(tLoanRateChange.getIndividualIncr());
			tLoanRateChange.setIndividualIncr(tLoanBorMain.getIndividualIncr());
			tLoanRateChange.setFitRate(iApproveRate);
			// 核准利率
		}

		try {
			loanRateChangeService.update(tLoanRateChange, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0006", "放款利率變動檔  " + tLoanBorMain.getLoanBorMainId() + " 生效日期 = "
					+ tLoanBorMain.getDrawdownDate() + e.getErrorMsg()); // 更新資料時，發生錯誤
		}
	}

	// 新增業績處理明細
	private void PfDetailRoutine() throws LogicException {
		this.info("PfDetailRoutine ...");
		pfDetailCom.setTxBuffer(this.getTxBuffer());
		PfDetailVo pf = new PfDetailVo();
		pf.setCustNo(tLoanBorMain.getCustNo()); // 借款人戶號
		pf.setFacmNo(tLoanBorMain.getFacmNo()); // 額度編號
		pf.setBormNo(tLoanBorMain.getBormNo()); // 撥款序號
		pf.setBorxNo(wkBorxNo); // 交易內容檔序號
		pf.setRepayType(1); // 還款類別 1.計件代碼變更
		pf.setDrawdownAmt(tLoanBorMain.getDrawdownAmt());// 撥款金額/追回金額
		pf.setPieceCode(tLoanBorMain.getPieceCode()); // 計件代碼
		pf.setPieceCodeSecond(tLoanBorMain.getPieceCodeSecond()); // 計件代碼2
		pf.setPieceCodeSecondAmt(tLoanBorMain.getPieceCodeSecondAmt());// 計件代碼2金額
		pf.setDrawdownDate(tLoanBorMain.getDrawdownDate());// 撥款日期
		pf.setRepaidPeriod(tLoanBorMain.getRepaidPeriod()); // 已攤還期數
		pfDetailCom.addDetail(pf, titaVo);
	}

	// 登錄時檢核如L4321已確認未放行，則出錯誤訊息
	private void CheckBatxRateChangeRoutine() throws LogicException {
		this.info("CheckBatxRateChangeRoutine ... ");

		BatxRateChange tBatxRateChange = batxRateChangeService
				.findById(new BatxRateChangeId(titaVo.getEntDyI() + 19110000, iCustNo, wkFacmNo, wkBormNo), titaVo);
		if (tBatxRateChange != null && tBatxRateChange.getConfirmFlag() == 1) {
			throw new LogicException(titaVo, "E0007", "整批利率調整確認未放行"); // 更新資料時，發生錯誤

		}
	}
}