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
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.LoanBorMainId;
import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.domain.LoanBorTxId;
import com.st1.itx.db.domain.LoanIntDetail;
import com.st1.itx.db.domain.LoanIntDetailId;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.db.service.LoanIntDetailService;
import com.st1.itx.db.service.TxTempService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcRepayCom;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.LoanCalcRepayIntCom;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.LoanDueAmtCom;
import com.st1.itx.util.common.LoanSetRepayIntCom;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.common.data.BaTxVo;
import com.st1.itx.util.common.data.CalcRepayIntVo;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
/*
 * L3711 應繳日變更-不可欠繳
 * a.有1期(含)以上期款未繳,不可變更繳款日
 * b.變更應繳日需落在上次繳息迄日與下次應繳息日之內
 * c.不可短繳
 */

/*
 * Tita
 * TimCustNo=9,7
 * EntryDate=9,7
 * FacmNo=9,3
 * BormNo=9,3
 * NewSpecificDd=9,2
 * NewPayIntDate=9,7
 * TimReduceAmt=9,14.2
 * TotalRepayAmt=9,14.2
 * RealRepayAmt=9,14.2
 * RqspFlag=X,1
 */
/**
 * L3711 應繳日變更-不可欠繳
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3711")
@Scope("prototype")
public class L3711 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public FacMainService facMainService;
	@Autowired
	public LoanBorMainService loanBorMainService;
	@Autowired
	public LoanBorTxService loanBorTxService;
	@Autowired
	public TxTempService txTempService;
	@Autowired
	public LoanIntDetailService loanIntDetailService;

	@Autowired
	Parse parse;
	@Autowired
	SendRsp sendRsp;
	@Autowired
	DataLog datalog;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	LoanDueAmtCom loanDueAmtCom;
	@Autowired
	LoanCom loanCom;
	@Autowired
	AcRepayCom acRepayCom;
	@Autowired
	LoanSetRepayIntCom loanSetRepayIntCom;
	@Autowired
	LoanCalcRepayIntCom loanCalcRepayIntCom;
	@Autowired
	BaTxCom baTxCom;
	private TitaVo titaVo;
	private int iCustNo;
	private int iFacmNo;
	private int iBormNo;
	private int iEntryDate;
	private int iNewSpecificDd;
	private int iNewPayIntDate;
	private BigDecimal iReduceAmt;
	private String iRqspFlag;
	private int iRpCode; // 還款來源

	// work area
	private int wkTbsDy;
	private int wkCustNo;
	private int wkFacmNo;
	private int wkBormNo;
	private int wkBorxNo;
	private int wkNewBorxNo;
	private int wkIntStartDate = 99991231;
	private int wkIntEndDate = 0;
	private int wkOldSpecificDd;
	private int wkOldSpecificDate;
	private int wkOldPrevRepaidDate;
	private int wkOldNextRepayDate;
	private int wkNewSpecificDate;
	private int wkLoanPrevIntDate;
	private BigDecimal wkInterest = BigDecimal.ZERO;
	private BigDecimal wkLoanBal = BigDecimal.ZERO;
	private BigDecimal wkDueAmt = BigDecimal.ZERO;
	private BigDecimal wkReduceAmt = BigDecimal.ZERO;
	private BigDecimal wkTotalInterest = BigDecimal.ZERO;
	private FacMain tFacMain;
	private LoanBorMain tLoanBorMain;
	private LoanBorTx tLoanBorTx;
	private LoanBorTxId tLoanBorTxId;
	private LoanIntDetail tLoanIntDetail;
	private LoanIntDetailId tLoanIntDetailId;
	private TempVo tTempVo = new TempVo();
	private List<LoanBorTx> lLoanBorTx = new ArrayList<LoanBorTx>();
	private List<LoanBorMain> lLoanBorMain;
	private ArrayList<CalcRepayIntVo> lCalcRepayIntVo = new ArrayList<CalcRepayIntVo>();
	private ArrayList<BaTxVo> baTxList = new ArrayList<BaTxVo>();
	private boolean isFirstBorm = true;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3711 ");

		this.totaVo.init(titaVo);
		this.titaVo = titaVo;
		this.wkTbsDy = this.txBuffer.getTxCom().getTbsdy();
		baTxCom.setTxBuffer(this.txBuffer);
		loanCom.setTxBuffer(this.txBuffer);
		loanSetRepayIntCom.setTxBuffer(this.txBuffer);

		// 取得輸入資料
		iCustNo = this.parse.stringToInteger(titaVo.getParam("TimCustNo"));
		iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		iBormNo = this.parse.stringToInteger(titaVo.getParam("BormNo"));
		iEntryDate = this.parse.stringToInteger(titaVo.getParam("EntryDate"));
		iNewSpecificDd = this.parse.stringToInteger(titaVo.getParam("NewSpecificDd"));
		iNewPayIntDate = this.parse.stringToInteger(titaVo.getParam("NewPayIntDate"));
		iReduceAmt = this.parse.stringToBigDecimal(titaVo.getParam("TimReduceAmt"));
		iRqspFlag = titaVo.getParam("RqspFlag");
		iRpCode = 90;
		
		// 檢查到同戶帳務交易需由最近一筆交易開始訂正
		if (titaVo.isHcodeErase()) {
			loanCom.checkEraseCustNoTxSeqNo(iCustNo, titaVo);
		}
		
		wkReduceAmt = iReduceAmt;
				
		if (titaVo.isHcodeNormal()) {
			// call 應繳試算
			this.baTxList = baTxCom.settingUnPaid(titaVo.getEntDyI(), iCustNo, iFacmNo, 0, 0, BigDecimal.ZERO, titaVo); 
			SpecificNormalRoutine();
			titaVo.setTxAmt(wkTotalInterest.subtract(iReduceAmt));
		} else {
			SpecificEraseRoutine();
		}
		// 帳務處理
		acRepayCom.setTxBuffer(this.txBuffer);
		acRepayCom.settleRun(this.lLoanBorTx, this.baTxList, titaVo);
		
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void SpecificNormalRoutine() throws LogicException {
		this.info("specificNormalRoutine ... ");

		int wkFacmNoStart = 1;
		int wkFacmNoEnd = 999;
		int wkBormNoStart = 1;
		int wkBormNoEnd = 900;
		int wkTotaCount = 0;
		wkLoanPrevIntDate = 0;

		// 減免金額超過限額，需主管核可
		if (iRqspFlag.equals("Y")) {
			if (!titaVo.getHsupCode().equals("1")) {
				sendRsp.addvReason(this.txBuffer, titaVo, "0007", "");
			}
		}
		if (iFacmNo > 0) {
			wkFacmNoStart = iFacmNo;
			wkFacmNoEnd = iFacmNo;
		}
		if (iBormNo > 0) {
			wkBormNoStart = iBormNo;
			wkBormNoEnd = iBormNo;
		}

		Slice<LoanBorMain> slLoanBorMain = loanBorMainService.bormCustNoEq(iCustNo, wkFacmNoStart, wkFacmNoEnd,
				wkBormNoStart, wkBormNoEnd, 0, Integer.MAX_VALUE);
		lLoanBorMain = slLoanBorMain == null ? null : slLoanBorMain.getContent();
		if (lLoanBorMain == null || lLoanBorMain.size() == 0) {
			throw new LogicException(titaVo, "E0001", "放款主檔"); // 查詢資料不存在
		}
		for (LoanBorMain ln : lLoanBorMain) {
			wkCustNo = ln.getCustNo();
			wkFacmNo = ln.getFacmNo();
			wkBormNo = ln.getBormNo();
			wkLoanPrevIntDate = ln.getPrevPayIntDate() == 0 ? ln.getDrawdownDate() : ln.getPrevPayIntDate();
			if (ln.getStatus() != 0) {
				if (iFacmNo > 0 && iBormNo > 0) {
					throw new LogicException(titaVo, "E3063", ""); // 該筆放款戶況非正常戶
				} else {
					continue;
				}
			}
			if (ln.getNextPayIntDate() <= this.txBuffer.getTxCom().getTbsdy()) {
				throw new LogicException(titaVo, "E3062", " 應繳息日 = " + ln.getNextPayIntDate()); // 有1期(含)以上期款未繳,不可變更繳款日
			}

			if (iNewPayIntDate < wkLoanPrevIntDate || iNewPayIntDate >= ln.getNextPayIntDate()) {
				throw new LogicException(titaVo, "E3075", wkCustNo + "-" + wkFacmNo + "-" + wkBormNo + " 上次繳息迄日 = "
						+ ln.getPrevPayIntDate() + " 下次應繳息日 = " + ln.getNextPayIntDate()); // 變更應繳日需落在上次繳息迄日與下次應繳息日之內
			}

			if (ln.getActFg() == 1) {
				throw new LogicException(titaVo, "E0021",
						"放款主檔 戶號 = " + ln.getCustNo() + " 額度編號 =  " + ln.getFacmNo() + " 撥款序號 = " + ln.getBormNo()); // 該筆資料待放行中
			}

			wkTotaCount++;
			// 計息至應繳日利息

			loanCalcRepayIntCom = loanSetRepayIntCom.setRepayInt(ln, 0, iNewPayIntDate, 1, iEntryDate, titaVo);
			lCalcRepayIntVo = loanCalcRepayIntCom.getRepayInt(titaVo);
			wkInterest = loanCalcRepayIntCom.getInterest();
			wkTotalInterest = wkTotalInterest.add(wkInterest);

			if (iReduceAmt.compareTo(BigDecimal.ZERO) > 0) {
				if (wkReduceAmt.compareTo(BigDecimal.ZERO) > 0) {
					if (wkReduceAmt.compareTo(wkInterest) >= 0) {
						wkReduceAmt = wkReduceAmt.subtract(wkInterest);
						wkInterest = BigDecimal.ZERO;
					} else {
						wkInterest = wkInterest.subtract(wkReduceAmt);
						wkReduceAmt = BigDecimal.ZERO;
					}
				}
			}
			// 查詢額度主檔
			tFacMain = facMainService.findById(new FacMainId(iCustNo, wkFacmNo));
			if (tFacMain == null) {
				throw new LogicException(titaVo, "E0001", "額度主檔 借款人戶號 = " + iCustNo + " 額度編號 = " + wkFacmNo); // 查詢資料不存在
			}
			if (tFacMain.getActFg() == 1) {
				throw new LogicException(titaVo, "E0021",
						"額度檔 戶號 = " + tFacMain.getCustNo() + " 額度編號 =  " + tFacMain.getFacmNo()); // 該筆資料待放行中
			}
			// 鎖定撥款主檔
			tLoanBorMain = loanBorMainService.holdById(new LoanBorMainId(iCustNo, wkFacmNo, wkBormNo));
			if (tLoanBorMain == null) {
				throw new LogicException(titaVo, "E0006",
						"撥款主檔 戶號 = " + iCustNo + " 額度編號 = " + wkFacmNo + " 撥款序號 = " + wkBormNo); // 鎖定資料時，發生錯誤
			}
			tTempVo.clear();
			// 新增交易暫存檔(放款資料)
			AddTxTempBormRoutine();
			// 更新撥款主檔
			UpdLoanBorMainRoutine();
			// 新增計息明細
			if (wkLoanPrevIntDate != ln.getDrawdownDate()) {
				AddLoanIntDetailRoutine();
			}
			// 新增放款交易內容檔
			AddLoanBorTxRoutine();
			// FirstBorm
			isFirstBorm = false;
		}
		if (wkTotaCount == 0) {
			throw new LogicException(titaVo, "E3076", ""); // 查無可變更應繳日的資料
		}
	}

	// 訂正
	private void SpecificEraseRoutine() throws LogicException {
		this.info("SpecificEraseRoutine ... ");
		Slice<LoanBorTx> slLoanBortx = loanBorTxService.custNoTxtNoEq(iCustNo, titaVo.getOrgEntdyI() + 19110000,
				titaVo.getOrgKin(), titaVo.getOrgTlr(), titaVo.getOrgTno(), 0, Integer.MAX_VALUE, titaVo);
		lLoanBorTx = slLoanBortx == null ? null : slLoanBortx.getContent();
		if (lLoanBorTx == null || lLoanBorTx.size() == 0) {
			throw new LogicException(titaVo, "E0001",
					"交易明細檔 交易序號=" + titaVo.getOrgKin() + titaVo.getOrgTlr() + titaVo.getOrgTno()); // 查詢資料不存在
		}
		for (LoanBorTx tx : lLoanBorTx) {
			wkCustNo = tx.getCustNo();
			wkFacmNo = tx.getFacmNo();
			wkBormNo = tx.getBormNo();
			wkBorxNo = tx.getBorxNo();
			tTempVo = tTempVo.getVo(tx.getOtherFields());
			wkInterest = this.parse.stringToBigDecimal(tTempVo.get("Interest"));
			this.info("   wkCustNo = " + wkCustNo);
			this.info("   wkFacmNo = " + wkFacmNo);
			this.info("   wkBormNo = " + wkBormNo);
			this.info("   wkBorxNo = " + wkBorxNo);
			// 查詢額度主檔
			tFacMain = facMainService.findById(new FacMainId(wkCustNo, wkFacmNo));
			if (tFacMain == null) {
				throw new LogicException(titaVo, "E0001", "額度主檔 借款人戶號 = " + wkCustNo + " 額度編號 = " + wkFacmNo); // 查詢資料不存在
			}
			if (tFacMain.getActFg() == 1) {
				throw new LogicException(titaVo, "E0021",
						"額度檔 戶號 = " + tFacMain.getCustNo() + " 額度編號 =  " + tFacMain.getFacmNo()); // 該筆資料待放行中
			}
			// 還原撥款主檔
			RestoredLoanBorMainRoutine();
			// 註記交易內容檔
			loanCom.setLoanBorTxHcode(wkCustNo, wkFacmNo, wkBormNo, wkBorxNo, wkNewBorxNo, tLoanBorMain.getLoanBal(),
					titaVo);

			// FirstBorm
			isFirstBorm = false;
		}
	}

	// 新增交易暫存檔(放款資料)
	private void AddTxTempBormRoutine() throws LogicException {
		this.info("AddTxTempBormRoutine ... ");

		wkBorxNo = tLoanBorMain.getLastBorxNo() + 1;
		wkLoanBal = tLoanBorMain.getLoanBal();
		wkDueAmt = tLoanBorMain.getDueAmt();
		wkOldSpecificDd = tLoanBorMain.getSpecificDd();
		wkOldSpecificDate = tLoanBorMain.getSpecificDate();
		wkOldPrevRepaidDate = tLoanBorMain.getPrevRepaidDate();
		tTempVo.putParam("SpecificDd", tLoanBorMain.getSpecificDd());
		tTempVo.putParam("SpecificDate", tLoanBorMain.getSpecificDate());
		tTempVo.putParam("PrevPayIntDate", tLoanBorMain.getPrevPayIntDate());
		tTempVo.putParam("PrevRepaidDate", tLoanBorMain.getPrevRepaidDate());
		tTempVo.putParam("NextPayIntDate", tLoanBorMain.getNextPayIntDate());
		tTempVo.putParam("NextRepayDate", tLoanBorMain.getNextRepayDate());
		tTempVo.putParam("DueAmt", tLoanBorMain.getDueAmt());
		tTempVo.putParam("LastEntDy", tLoanBorMain.getLastEntDy());
		tTempVo.putParam("LastKinbr", tLoanBorMain.getLastKinbr());
		tTempVo.putParam("LastTlrNo", tLoanBorMain.getLastTlrNo());
		tTempVo.putParam("LastTxtNo", tLoanBorMain.getLastTxtNo());
	}

	// 更新撥款主檔
	private void UpdLoanBorMainRoutine() throws LogicException {
		this.info("UpdLoanBorMainRoutine ... ");
		this.info("   wkOldSpecificDd     = " + wkOldSpecificDd);
		this.info("   wkOldSpecificDate   = " + wkOldSpecificDate);
		this.info("   wkOldPrevRepaidDate = " + wkOldPrevRepaidDate);
		this.info("   iNewPayIntDate      = " + iNewPayIntDate);
		this.info("   getTotalPeriod()    = " + tLoanBorMain.getTotalPeriod());
		this.info("   getDueAmt()         = " + tLoanBorMain.getDueAmt());

		int wkNewPrevRepaidDate = 0;
		int wkNextPayIntDate = 0;
		int wkNextRepayDate = 0;
		int wkRestPeriod = 0;
		int wkMons = 0;
		wkNextRepayDate = wkOldNextRepayDate;

		// 新下次繳息日 = 應收息迄日 + 1 期
		wkNextPayIntDate = loanCom.getPayIntEndDate(tLoanBorMain.getFreqBase(), tLoanBorMain.getPayIntFreq(),
				iNewPayIntDate, iNewSpecificDd, 1, tLoanBorMain.getMaturityDate());

		// 重算新指定基準日期
		wkNewSpecificDate = loanCom.getSpecificDate(iNewSpecificDd, tLoanBorMain.getFirstDueDate(),
				tLoanBorMain.getPayIntFreq());
		this.info("wkNewSpecificDate = " + tLoanBorMain.getBormNo() + " " + wkNewSpecificDate);

		// 新上次還本日
		wkNewPrevRepaidDate = iNewPayIntDate;

		// 新下次還本日
		wkNextRepayDate = loanCom.getNextRepayDate(tLoanBorMain.getAmortizedCode(), tLoanBorMain.getRepayFreq(),
				tLoanBorMain.getFreqBase(), wkNewSpecificDate, iNewSpecificDd, wkNewPrevRepaidDate,
				tLoanBorMain.getMaturityDate(), tLoanBorMain.getGraceDate());

		// 過了寬限到期日重算期金，剩餘期數 = 總期數減已繳期數
		if (wkNewPrevRepaidDate > 0 && wkNextRepayDate > tLoanBorMain.getGraceDate()) {
			wkRestPeriod = tLoanBorMain.getTotalPeriod() - loanCom.getTermNo(2, tLoanBorMain.getFreqBase(),
					tLoanBorMain.getRepayFreq(), wkNewSpecificDate, iNewSpecificDd, wkNewPrevRepaidDate);
			if (wkRestPeriod > 0) {
				wkDueAmt = loanDueAmtCom.getDueAmt(tLoanBorMain.getLoanBal(), tLoanBorMain.getStoreRate(),
						tLoanBorMain.getAmortizedCode(), tLoanBorMain.getFreqBase(), wkRestPeriod, 0,
						tLoanBorMain.getPayIntFreq(), tLoanBorMain.getFinalBal(), titaVo);
			}
		}

		tLoanBorMain.setLastBorxNo(wkBorxNo);
		tLoanBorMain.setSpecificDd(iNewSpecificDd);
		tLoanBorMain.setSpecificDate(wkNewSpecificDate);
		tLoanBorMain.setPrevPayIntDate(iNewPayIntDate);
		tLoanBorMain.setPrevRepaidDate(wkNewPrevRepaidDate);
		tLoanBorMain.setNextPayIntDate(wkNextPayIntDate);
		tLoanBorMain.setNextRepayDate(wkNextRepayDate);
		tLoanBorMain.setDueAmt(wkDueAmt);
		tLoanBorMain.setLastEntDy(titaVo.getEntDyI());
		tLoanBorMain.setLastKinbr(titaVo.getKinbr());
		tLoanBorMain.setLastTlrNo(titaVo.getTlrNo());
		tLoanBorMain.setLastTxtNo(titaVo.getTxtNo());
		try {
			loanBorMainService.update(tLoanBorMain);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007",
					"撥款主檔 戶號 = " + iCustNo + " 額度編號 = " + iFacmNo + " 撥款序號 = " + iBormNo); // 更新資料時，發生錯誤
		}

		this.info("   wkNextPayIntDate = " + wkNextPayIntDate);
		this.info("   wkNextRepayDate  = " + wkNextRepayDate);
		this.info("   wkMons        = " + wkMons);
		this.info("   wkRestPeriod  = " + wkRestPeriod);
		this.info("   wkDueAmt      = " + wkDueAmt);
		this.info("UpdLoanBorMainRoutine end ");
	}

	// 新增計息明細
	private void AddLoanIntDetailRoutine() throws LogicException {
		this.info("AddLoanIntDetailRoutine ... ");

		int wkIntSeq = 0;

		for (CalcRepayIntVo c : lCalcRepayIntVo) {
			wkIntSeq++;
			wkIntStartDate = c.getStartDate() < wkIntStartDate ? c.getStartDate() : wkIntStartDate;
			wkIntEndDate = c.getEndDate() > wkIntEndDate ? c.getEndDate() : wkIntEndDate;
			wkLoanBal = wkLoanBal.subtract(c.getPrincipal());
			tLoanIntDetailId = new LoanIntDetailId();
			tLoanIntDetailId.setCustNo(c.getCustNo());
			tLoanIntDetailId.setFacmNo(c.getFacmNo());
			tLoanIntDetailId.setBormNo(c.getBormNo());
			tLoanIntDetailId.setAcDate(wkTbsDy);
			tLoanIntDetailId.setTlrNo(titaVo.getTlrNo());
			tLoanIntDetailId.setTxtNo(titaVo.getTxtNo());
			tLoanIntDetailId.setIntSeq(wkIntSeq);
			tLoanIntDetail = new LoanIntDetail();
			tLoanIntDetail.setCustNo(c.getCustNo());
			tLoanIntDetail.setFacmNo(c.getFacmNo());
			tLoanIntDetail.setBormNo(c.getBormNo());
			tLoanIntDetail.setAcDate(wkTbsDy);
			tLoanIntDetail.setTlrNo(titaVo.getTlrNo());
			tLoanIntDetail.setTxtNo(titaVo.getTxtNo());
			tLoanIntDetail.setIntSeq(wkIntSeq);
			tLoanIntDetail.setLoanIntDetailId(tLoanIntDetailId);
			tLoanIntDetail.setIntStartDate(c.getStartDate());
			tLoanIntDetail.setIntEndDate(c.getEndDate());
			tLoanIntDetail.setIntDays(c.getDays());
			tLoanIntDetail.setBreachDays(c.getOdDays());
			tLoanIntDetail.setMonthLimit(c.getMonthLimit());
			tLoanIntDetail.setIntFlag(c.getInterestFlag());
			tLoanIntDetail.setCurrencyCode(tFacMain.getCurrencyCode());
			tLoanIntDetail.setIntRate(c.getStoreRate());
			tLoanIntDetail.setRateIncr(c.getRateIncr());
			tLoanIntDetail.setAmount(c.getAmount());
			tLoanIntDetail.setIndividualIncr(c.getIndividualIncr());
			tLoanIntDetail.setPrincipal(c.getPrincipal());
			tLoanIntDetail.setInterest(c.getInterest());
			tLoanIntDetail.setDelayInt(c.getDelayInt());
			tLoanIntDetail.setBreachAmt(c.getBreachAmt());
			tLoanIntDetail.setCloseBreachAmt(c.getCloseBreachAmt());
			tLoanIntDetail.setBreachGetCode(c.getBreachGetCode());
			tLoanIntDetail.setLoanBal(wkLoanBal);
			tLoanIntDetail.setExtraRepayFlag(c.getExtraRepayFlag());
			tLoanIntDetail.setProdNo(tFacMain.getProdNo());
			tLoanIntDetail.setBaseRateCode(tFacMain.getBaseRateCode());
			try {
				loanIntDetailService.insert(tLoanIntDetail);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "計息明細 Key = " + tLoanIntDetailId); // 新增資料時，發生錯誤
			}
		}
	}

	// 新增放款交易內容檔
	private void AddLoanBorTxRoutine() throws LogicException {
		this.info("AddLoanBorTxRoutine ... ");

		tLoanBorTx = new LoanBorTx();
		tLoanBorTxId = new LoanBorTxId();
		loanCom.setLoanBorTx(tLoanBorTx, tLoanBorTxId, iCustNo, wkFacmNo, wkBormNo, wkBorxNo, titaVo);
		// 3711 應繳日變更
		tLoanBorTx.setTxDescCode("3711");
		tLoanBorTx.setDesc("應繳日變更-不可欠繳");
		tLoanBorTx.setRepayCode(iRpCode); // 還款來源
		tLoanBorTx.setEntryDate(iEntryDate);
		tLoanBorTx.setAcctCode(tFacMain.getAcctCode());
		tLoanBorTx.setLoanBal(tLoanBorMain.getLoanBal());
		tLoanBorTx.setRate(tLoanBorMain.getStoreRate());
		tLoanBorTx.setIntStartDate(wkIntStartDate);
		tLoanBorTx.setIntEndDate(wkIntEndDate);
		tLoanBorTx.setInterest(wkInterest);
		tLoanBorTx.setUnpaidInterest(BigDecimal.ZERO);
		tLoanBorTx.setShortfall(BigDecimal.ZERO);
		// 繳息首筆、繳息次筆
		if (isFirstBorm) {
			tLoanBorTx.setDisplayflag("F"); // 繳息首筆
		} else {
			tLoanBorTx.setDisplayflag("I"); // 繳息次筆
		}

		// 其他欄位
		tTempVo.putParam("ReduceAmt", wkReduceAmt); // 違約金減免金額
		// 支票繳款利息免印花稅
		if (iRpCode == 4) {
			tTempVo.putParam("StampFreeAmt", wkInterest);
		}
		tTempVo.putParam("OldSpecificDd", wkOldSpecificDd); // 原指定應繳日
		tTempVo.putParam("NewSpecificDd", tLoanBorMain.getSpecificDd()); // 新指定應繳日
		tLoanBorTx.setOtherFields(tTempVo.getJsonString());
		try {
			loanBorTxService.insert(tLoanBorTx);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "放款交易內容檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
		}
		this.lLoanBorTx.add(tLoanBorTx); 
	}

	// 還原撥款主檔
	private void RestoredLoanBorMainRoutine() throws LogicException {
		this.info("RestoredLoanBorMainRoutine ... ");

		tLoanBorMain = loanBorMainService.holdById(new LoanBorMainId(wkCustNo, wkFacmNo, wkBormNo));
		if (tLoanBorMain == null) {
			throw new LogicException(titaVo, "E0006",
					"撥款主檔 戶號 = " + iCustNo + " 額度編號 = " + wkFacmNo + " 撥款序號 = " + wkBormNo); // 鎖定資料時，發生錯誤
		}
		wkNewBorxNo = tLoanBorMain.getLastBorxNo() + 1;
		// 放款交易訂正交易須由最後一筆交易開始訂正
		loanCom.checkEraseBormTxSeqNo(tLoanBorMain, titaVo);
		tLoanBorMain.setLastBorxNo(wkNewBorxNo);
		tLoanBorMain.setSpecificDd(this.parse.stringToInteger(tTempVo.get("SpecificDd")));
		tLoanBorMain.setSpecificDate(this.parse.stringToInteger(tTempVo.get("SpecificDate")));
		tLoanBorMain.setPrevPayIntDate(this.parse.stringToInteger(tTempVo.get("PrevPayIntDate")));
		tLoanBorMain.setPrevRepaidDate(this.parse.stringToInteger(tTempVo.get("PrevRepaidDate")));
		tLoanBorMain.setNextPayIntDate(this.parse.stringToInteger(tTempVo.get("NextPayIntDate")));
		tLoanBorMain.setNextRepayDate(this.parse.stringToInteger(tTempVo.get("NextRepayDate")));
		tLoanBorMain.setDueAmt(this.parse.stringToBigDecimal(tTempVo.get("DueAmt")));
		tLoanBorMain.setLastEntDy(this.parse.stringToInteger(tTempVo.get("LastEntDy")));
		tLoanBorMain.setLastKinbr(tTempVo.get("LastKinbr"));
		tLoanBorMain.setLastTlrNo(tTempVo.get("LastTlrNo"));
		tLoanBorMain.setLastTxtNo(tTempVo.get("LastTxtNo"));
		try {
			loanBorMainService.update(tLoanBorMain);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007",
					"撥款主檔 戶號 = " + wkCustNo + " 額度編號 = " + wkFacmNo + " 撥款序號 = " + wkBormNo); // 更新資料時，發生錯誤
		}
	}

}