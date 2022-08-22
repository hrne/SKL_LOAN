package com.st1.itx.util.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.CdBank;
import com.st1.itx.db.domain.CdBankId;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdCodeId;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.domain.LoanBorTxId;
import com.st1.itx.db.domain.LoanFacTmp;
import com.st1.itx.db.domain.MlaundryRecord;
import com.st1.itx.db.domain.TxRecord;
import com.st1.itx.db.domain.TxRecordId;
import com.st1.itx.db.domain.TxTemp;
import com.st1.itx.db.domain.TxTempId;
import com.st1.itx.db.service.CdBankService;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.db.service.LoanFacTmpService;
import com.st1.itx.db.service.MlaundryRecordService;
import com.st1.itx.db.service.TxRecordService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.data.BaTxVo;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

/**
 * 放款公用程式<BR>
 * setTxTemp 設定交易暫存檔(TxTemp)的共同資料<BR>
 * setFacmBorTx 設定放款交易內容檔(到額度)的共同資料<BR>
 * setFacmBorTxHcode 訂正時註記放款交易內容檔(到額度)<BR>
 * setFacmBorTxHcodeByTx 訂正放款交易內容檔by交易 <BR>
 * setLoanBorTx 設定放款交易內容檔(到撥款)的共同資料<BR>
 * setLoanBorTxHcode 訂正時註記放款交易內容檔(到撥款)<BR>
 * setIntAcctCode 抓取本金科目對應的利息科目<BR>
 * setShortPrinAcctCode 抓取本金科目對應的欠繳本金科目<BR>
 * setShortIntAcctCode 抓取本金科目對應的欠繳利息科目<BR>
 * getNextPayIntDate 依上次繳息日推算下次收息日<BR>
 * getNextRepayDate 依上次還本日計算下次還本日<BR>
 * getCustUKey 以 CustId 取得 CustUKey<BR>
 * getCustNameById 以 CustId 取得 CustName<BR>
 * getCustNameByNo 以 CustNo 取得 CustName<BR>
 * getCustId以 CustUKey 取得 CustId<BR>
 * getEmpFullnameByEmpNo 以 EmployeeNo 取得 Employee FullName<BR>
 * getBankItemByBankCode 以 BankCode 取得 BankItem<BR>
 * getBranchItemByBankCode以 BankCode 取得 BranchItem<BR>
 * getTermNo 計算到入帳日之繳息期數<BR>
 * getPayIntEndDate 用期數計算收息迄日<BR>
 * getTotalPeriod 計算總期數<BR>
 * getGracePeriod 依寬限期到期日計算寬限期數<BR>
 * getSpecificDate 依首次應繳日推算指定基準日期<BR>
 * getCdCodeX 取得代碼說明<BR>
 * checkEraseBormTxSeqNo 檢查到撥款的放款交易訂正須由最近一筆交易開始訂正<BR>
 * checkEraseFacmTxSeqNo 檢查到額度的的放款交易訂正須由最近一筆交易開始訂正<BR>
 * updateMlaundryRecord 更新疑似洗錢交易訪談記錄檔<BR>
 * getTmpFacmNo 取得暫收款額度<BR>
 * isLoanFacTmp 檢查額度是否為暫收指定額度<BR>
 * addFeeBorTxRoutine 新增放款交易內容檔(收回費用)<BR>
 * settleFeeRoutine 收回費用處理(新增帳務及放款交易內容檔)<BR>
 * settleOverflow 累溢收(暫收貸)帳務處理<BR>
 * settleTempAmt 暫收款金額(暫收借)帳務處理 <BR>
 * updBorTxAcDetail 更新放款明細檔及帳務明細檔關聯欄<BR>
 * 
 * @author st1
 *
 */
@Component("loanCom")
@Scope("prototype")
public class LoanCom extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public CdEmpService cdEmpService;
	@Autowired
	public CdBankService cdBankService;
	@Autowired
	public CustMainService custMainService;
	@Autowired
	public FacMainService facMainService;
	@Autowired
	public LoanBorMainService loanBorMainService;
	@Autowired
	public LoanBorTxService loanBorTxService;
	@Autowired
	public TxRecordService txRecordService;
	@Autowired
	public MlaundryRecordService mlaundryRecordService;
	@Autowired
	public CdCodeService cdCodeService;
	@Autowired
	public LoanFacTmpService loanFacTmpService;

	@Autowired
	Parse parse;
	@Autowired
	DataLog datalog;
	@Autowired
	DateUtil dDateUtil;

	/**
	 * 設定交易暫存檔(TxTemp)的共同資料
	 * 
	 * @param txTempId TxTempId
	 * @param txTemp   TxTemp
	 * @param iCustNo  戶號
	 * @param iFacmNo  額度
	 * @param iBormNo  撥款
	 * @param iOvduNo  催收序號
	 * @param titaVo   TitaVo
	 * @throws LogicException LogicException
	 */
	public void setTxTemp(TxTempId txTempId, TxTemp txTemp, int iCustNo, int iFacmNo, int iBormNo, int iOvduNo,
			TitaVo titaVo) throws LogicException {
		this.info("setTxTemp ... ");
		this.info("   titaVo.getEntDyI() = " + titaVo.getEntDyI());
		this.info("   titaVo.getKinbr()  = " + titaVo.getKinbr());
		this.info("   titaVo.getTlrNo()  = " + titaVo.getTlrNo());
		this.info("   titaVo.getTxtNo()  = " + titaVo.getTxtNo());

		String wkSeqNo = FormatUtil.pad9(String.valueOf(iCustNo), 7) + FormatUtil.pad9(String.valueOf(iFacmNo), 3)
				+ FormatUtil.pad9(String.valueOf(iBormNo), 3) + FormatUtil.pad9(String.valueOf(iOvduNo), 3);
		txTempId.setEntdy(titaVo.getEntDyI());
		txTempId.setKinbr(titaVo.getKinbr());
		txTempId.setTlrNo(titaVo.getTlrNo());
		txTempId.setTxtNo(titaVo.getTxtNo());
		txTempId.setSeqNo(wkSeqNo);
		txTemp.setEntdy(titaVo.getEntDyI());
		txTemp.setKinbr(titaVo.getKinbr());
		txTemp.setTlrNo(titaVo.getTlrNo());
		txTemp.setTxtNo(titaVo.getTxtNo());
		txTemp.setSeqNo(wkSeqNo);
		txTemp.setTxTempId(txTempId);
	}

	/**
	 * 設定放款交易內容檔(到額度)的共同資料
	 * 
	 * @param tLoanBorTx   LoanBorTx
	 * @param tLoanBorTxId LoanBorTxId
	 * @param iCustNo      戶號
	 * @param iFacmNo      額度
	 * @param titaVo       TitaVo
	 * @return 交易內容檔序號
	 * @throws LogicException LogicException
	 */
	public int setFacmBorTx(LoanBorTx tLoanBorTx, LoanBorTxId tLoanBorTxId, int iCustNo, int iFacmNo, TitaVo titaVo)
			throws LogicException {
		this.info("setLastBorTx ... ");
		int borxNo;
		LoanBorTx tBorTx = loanBorTxService.bormNoDescFirst(iCustNo, iFacmNo, 0, titaVo);
		if (tBorTx == null) {
			borxNo = 1;
		} else {
			borxNo = tBorTx.getBorxNo() + 1;
		}
		setLoanBorTx(tLoanBorTx, tLoanBorTxId, iCustNo, iFacmNo, 0, borxNo, titaVo);
		return borxNo;
	}

	/**
	 * 訂正時註記放款交易內容檔(到額度)
	 * 
	 * @param iCustNo 戶號
	 * @param iFacmNo 額度
	 * @param iBorxNo 交易內容檔序號
	 * @param titaVo  TitaVo
	 * @return 交易內容檔序號
	 * @throws LogicException LogicException
	 */
	public int setFacmBorTxHcode(int iCustNo, int iFacmNo, int iBorxNo, TitaVo titaVo) throws LogicException {
		this.info("setFacmBorTxHcode ... ");
		int oldBorxNo = 0;
		int newBorxNo = 0;
		LoanBorTx tBorTx;
		tBorTx = loanBorTxService.findById(new LoanBorTxId(iCustNo, iFacmNo, 0, iBorxNo), titaVo);
		if (tBorTx == null) {
			throw new LogicException(titaVo, "E0006",
					"放款交易內容檔 戶號 = " + iCustNo + "-" + iFacmNo + "-0  交易內容檔序號 = " + iBorxNo); // 鎖定資料時，發生錯誤
		}
		oldBorxNo = tBorTx.getBorxNo();
		tBorTx = loanBorTxService.bormNoDescFirst(iCustNo, iFacmNo, 0, titaVo);
		if (tBorTx != null) {
			newBorxNo = tBorTx.getBorxNo() + 1;
		}
		setLoanBorTxHcode(iCustNo, iFacmNo, 0, oldBorxNo, newBorxNo, BigDecimal.ZERO, titaVo);
		return newBorxNo;
	}

	/**
	 * 設定放款交易內容檔(到撥款)的共同資料
	 * 
	 * @param tLoanBorTx   LoanBorTx
	 * @param tLoanBorTxId LoanBorTxId
	 * @param iCustNo      戶號
	 * @param iFacmNo      額度
	 * @param iBormNo      撥款
	 * @param iBorxNo      交易內容檔序號
	 * @param titaVo       TitaVo
	 * @throws LogicException LogicException
	 */
	public void setLoanBorTx(LoanBorTx tLoanBorTx, LoanBorTxId tLoanBorTxId, int iCustNo, int iFacmNo, int iBormNo,
			int iBorxNo, TitaVo titaVo) throws LogicException {
		this.info("setLoanBorTx ... ");
		this.info("   getCurName() = " + titaVo.getCurName());

		tLoanBorTxId.setCustNo(iCustNo);
		tLoanBorTxId.setFacmNo(iFacmNo);
		tLoanBorTxId.setBormNo(iBormNo);
		tLoanBorTxId.setBorxNo(iBorxNo);
		tLoanBorTx.setCustNo(iCustNo);
		tLoanBorTx.setFacmNo(iFacmNo);
		tLoanBorTx.setBormNo(iBormNo);
		tLoanBorTx.setBorxNo(iBorxNo);
		tLoanBorTx.setLoanBorTxId(tLoanBorTxId);
		tLoanBorTx.setTitaCalDy(this.parse.stringToInteger(titaVo.getCalDy()));
		tLoanBorTx.setTitaCalTm(this.parse.stringToInteger(titaVo.getCalTm()));
		tLoanBorTx.setTitaKinBr(titaVo.getKinbr());
		tLoanBorTx.setTitaTlrNo(titaVo.getTlrNo());
		tLoanBorTx.setTitaTxtNo(titaVo.getTxtNo());
		tLoanBorTx.setTitaTxCd(titaVo.getTxcd());
		tLoanBorTx.setTitaCrDb(titaVo.getCrdb());
		tLoanBorTx.setTitaHCode(titaVo.getHCode());
		tLoanBorTx.setTitaCurCd(titaVo.getCurName());
		tLoanBorTx.setTitaEmpNoS(titaVo.getEmpNos());
		tLoanBorTx.setAcDate(titaVo.getEntDyI());
		tLoanBorTx.setDisplayflag("Y");
		this.info("   TitaCurCd = " + tLoanBorTx.getTitaCurCd());
		this.info("setLoanBorTx end ");
	}

	/**
	 * 訂正時註記放款交易內容檔(到撥款)
	 * 
	 * @param iCustNo    戶號
	 * @param iFacmNo    額度
	 * @param iBormNo    撥款
	 * @param iBorxNo    交易內容檔序號
	 * @param iNewBorxNo 新交易內容檔序號
	 * @param iLoanBal   放款餘額
	 * @param titaVo     TitaVo
	 * @throws LogicException LogicException
	 */
	public void setLoanBorTxHcode(int iCustNo, int iFacmNo, int iBormNo, int iBorxNo, int iNewBorxNo,
			BigDecimal iLoanBal, TitaVo titaVo) throws LogicException {
		this.info("setLoanBorTxHcode ... " + iBorxNo + "," + iNewBorxNo);

		int wkAcDate = 0;

		LoanBorTx tLoanBorTx = loanBorTxService.holdById(new LoanBorTxId(iCustNo, iFacmNo, iBormNo, iBorxNo));
		if (tLoanBorTx == null) {
			throw new LogicException(titaVo, "E0006",
					"放款交易內容檔 戶號 = " + iCustNo + " 額度編號 = " + iFacmNo + " 撥款序號 = " + iBormNo + " 交易內容檔序號 = " + iBorxNo); // 鎖定資料時，發生錯誤
		}
		wkAcDate = tLoanBorTx.getAcDate();
		tLoanBorTx.setTitaHCode(wkAcDate == titaVo.getEntDyI() ? "2" : "4"); // 0: 未訂正 1: 訂正 2: 被訂正 3: 沖正 4: 被沖正
		tLoanBorTx.setCorrectSeq(parse.IntegerToString(titaVo.getEntDyI() + 19110000, 8) + titaVo.getTxSeq());
		try {
			loanBorTxService.update(tLoanBorTx);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "放款交易內容檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
		}

		// 新增放款交易內容檔
		LoanBorTx tLoanBorTx2 = (LoanBorTx) datalog.clone(tLoanBorTx);
		LoanBorTxId tLoanBorTx2Id = new LoanBorTxId();
		setLoanBorTx(tLoanBorTx2, tLoanBorTx2Id, iCustNo, iFacmNo, iBormNo, iNewBorxNo, titaVo);
		tLoanBorTx2.setTitaHCode(wkAcDate == titaVo.getEntDyI() ? "1" : "3"); // 0: 未訂正 1: 訂正 2: 被訂正 3: 沖正 4: 被沖正
		tLoanBorTx2.setCorrectSeq(parse.IntegerToString(tLoanBorTx.getAcDate() + 19110000, 8)
				+ tLoanBorTx.getTitaKinBr() + tLoanBorTx.getTitaTlrNo() + tLoanBorTx.getTitaTxtNo());
		// 訂正轉換資料為帳務交易
		if (!"Y".equals(tLoanBorTx.getDisplayflag())) {
			tLoanBorTx2.setDisplayflag("A");
		}
		// 訂正轉換資料為帳務交易
		if ("L3240".equals(titaVo.getTxcd()) || "L3250".equals(titaVo.getTxcd())) {
			tLoanBorTx2.setDisplayflag("A");
		}
		tLoanBorTx2.setLoanBal(iLoanBal);
		try {
			loanBorTxService.insert(tLoanBorTx2);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "放款交易內容檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
		}
		this.info("setLoanBorTxHcode end LoanBorTx " + tLoanBorTx2.toString());

	}

	/**
	 * 抓取本金科目對應的利息科目
	 * 
	 * @param iAcctCode 本金業務科目
	 * @return 利息科目
	 */
	public String setIntAcctCode(String iAcctCode) {
		this.info("setIntAcctCode ... ");
		this.info("   iAcctCode = " + iAcctCode);

		String wkIntCodeCode = "";
		switch (iAcctCode) {
		case "310": // 短期擔保放款
			wkIntCodeCode = "IC1";
			break;
		case "320": // 中期擔保放款
			wkIntCodeCode = "IC2";
			break;
		case "330": // 長期擔保放款
			wkIntCodeCode = "IC3";
			break;
		case "340": // 三十年房貸
			wkIntCodeCode = "IC4";
			break;
		}

		this.info("   wkIntCodeCode = " + wkIntCodeCode);
		this.info("setIntAcctCode end ");
		return wkIntCodeCode;
	}

	/**
	 * 抓取本金科目對應的欠繳本金科目
	 * 
	 * @param iAcctCode 本金業務科目
	 * @return 欠繳本金科目
	 */
	public String setShortPrinAcctCode(String iAcctCode) {
		this.info("setShortPrinAcctCode ... ");
		this.info("   iAcctCode = " + iAcctCode);

		String wkShortPrinCodeCode = "";
		switch (iAcctCode) {
		case "310": // 短期擔保放款
			wkShortPrinCodeCode = "Z10";
			break;
		case "320": // 中期擔保放款
			wkShortPrinCodeCode = "Z20";
			break;
		case "330": // 長期擔保放款
			wkShortPrinCodeCode = "Z30";
			break;
		case "340": // 三十年房貸
			wkShortPrinCodeCode = "Z40";
			break;
		}

		this.info("   wkShortPrinCodeCode = " + wkShortPrinCodeCode);
		this.info("setShortPrinAcctCode end ");
		return wkShortPrinCodeCode;
	}

	/**
	 * 抓取本金科目對應的欠繳利息科目
	 * 
	 * @param iAcctCode 本金業務科目
	 * @return 欠繳利息科目
	 */
	public String setShortIntAcctCode(String iAcctCode) {
		this.info("setShortIntAcctCode ... ");
		this.info("   iAcctCode = " + iAcctCode);

		String wkShortIntCodeCode = "";
		switch (iAcctCode) {
		case "310": // 短期擔保放款
			wkShortIntCodeCode = "IC1";
			break;
		case "320": // 中期擔保放款
			wkShortIntCodeCode = "IC2";
			break;
		case "330": // 長期擔保放款
			wkShortIntCodeCode = "IC3";
			break;
		case "340": // 三十年房貸
			wkShortIntCodeCode = "IC4";
			break;
		}

		this.info("   wkShortIntCodeCode = " + wkShortIntCodeCode);
		this.info("setShortIntAcctCode end ");
		return wkShortIntCodeCode;
	}

	// 計算下次繳息日
	/**
	 * 依上次繳息日推算下次繳息日
	 * 
	 * @param iAmortizedCode   攤還方式
	 * @param iPayIntFreq      繳息週期
	 * @param iFreqBase        週期基準
	 * @param iSpecificDate    指定基準日期
	 * @param iSpecificDd      指定應繳日
	 * @param iPrevPaidIntDate 繳息迄日
	 * @param iMaturityDate    到期日
	 * @return 下次繳息日
	 * @throws LogicException LogicException
	 */
	public int getNextPayIntDate(String iAmortizedCode, int iPayIntFreq, int iFreqBase, int iSpecificDate,
			int iSpecificDd, int iPrevPaidIntDate, int iMaturityDate) throws LogicException {
		this.info("getNextPayIntDate ... ");
		this.info("   iAmortizedCode  = " + iAmortizedCode);
		this.info("   iPayIntFreq     = " + iPayIntFreq);
		this.info("   iFreqBase       = " + iFreqBase);
		this.info("   iSpecificDate   = " + iSpecificDate);
		this.info("   iSpecificDd     = " + iSpecificDd);
		this.info("   iPrevPaidIntDate= " + iPrevPaidIntDate);
		this.info("   iMaturityDate   = " + iMaturityDate);

		int wkDate = 0;
		int wkMons = 0;
		int wkDays = 0;
		int wkTerms = 0;
		int wkNextPayIntDate = 0;
		int wkSpecificDate = 0;
		int wkSpecificMons = 0;
		// 指定基準日期、指定基準月差
		// 指定應繳日超過28且指定基準日與指定應繳日不同 => 指定基準日期調整為當年1月
		// iSpecificDate = 20200228, iSpecificDd = 31 => wkSpecificDate=20200131,
		// wkSpecificMons=1
		if (iSpecificDd >= 28 && iSpecificDate % 100 != iSpecificDd) {
			wkSpecificDate = (iSpecificDate / 10000) * 10000 + 100 + iSpecificDd;
			wkSpecificMons = (iSpecificDate / 100) - (wkSpecificDate / 100);
		} else {
			wkSpecificDate = iSpecificDate;
			wkSpecificMons = 0;
		}
		this.info("iSpecificDate=" + iSpecificDate + ", Dd=" + iSpecificDd + ", wkSpecificDate=" + wkSpecificDate
				+ ", wkSpecificMons=" + wkSpecificMons);

		if (iAmortizedCode.equals("2")) { // 2.到期取息(到期繳息還本)
			wkNextPayIntDate = iMaturityDate;
			this.info("    = " + wkNextPayIntDate);
			this.info("getNextPayIntDate end ");
			return wkNextPayIntDate;
		}
		if (iPayIntFreq == 00 || iPayIntFreq == 99) {
			wkNextPayIntDate = 9991231;
			this.info("    = " + wkNextPayIntDate);
			this.info("getNextPayIntDate end ");
		}
		dDateUtil.init();
		dDateUtil.setDate_1(iSpecificDate);
		if (iPrevPaidIntDate < iSpecificDate) {
			dDateUtil.setDate_2(iSpecificDate);
		} else {
			dDateUtil.setDate_2(iPrevPaidIntDate);
		}
		switch (iFreqBase) { // 週期基準 1:日 2:月 3:週
		case 2:
			dDateUtil.dateDiffSp();
			wkMons = (dDateUtil.getYears() * 12) + dDateUtil.getMons();
			wkTerms = wkMons / iPayIntFreq;
			wkTerms += 1;
			wkMons = wkTerms * iPayIntFreq;
			// 指定基準日期調整為當年1月
			// iSpecificDate = 20200228, iSpecificDd = 31 => wkSpecificDate=20200131,
			// wkSpecificMons=1
			dDateUtil.init();
			dDateUtil.setDate_1(wkSpecificDate);
			dDateUtil.setMons(wkMons + wkSpecificMons);
			wkDate = dDateUtil.getCalenderDay();
			break;
		case 3:
			dDateUtil.dateDiff();
			wkDays = dDateUtil.getDays();
			wkDays = wkDays + (iPayIntFreq * 7);
			dDateUtil.init();
			dDateUtil.setDate_1(iSpecificDate);
			dDateUtil.setDays(wkDays);
			wkDate = dDateUtil.getCalenderDay();
			break;
		}
		// 預定收息日是否已超過契約止日
		if (iPrevPaidIntDate < iMaturityDate && wkDate > iMaturityDate) {
			wkNextPayIntDate = iMaturityDate;
		} else {
			wkNextPayIntDate = wkDate;
		}
		this.info("    = " + wkNextPayIntDate);
		this.info("getNextPayIntDate end ");
		return wkNextPayIntDate;
	}

	/**
	 * 依上次還本日計算下次還本日
	 * 
	 * @param iAmortizedCode  攤還方式
	 * @param iRepayFreq      還本週期
	 * @param iFreqBase       週期基準
	 * @param iSpecificDate   指定基準日期
	 * @param iSpecificDd     指定應繳日
	 * @param iPrevRepaidDate 上次還本日
	 * @param iMaturityDate   到期日
	 * @param iGraceDate      寬限到期日
	 * @return 下次還本日
	 * @throws LogicException LogicException
	 */
	public int getNextRepayDate(String iAmortizedCode, int iRepayFreq, int iFreqBase, int iSpecificDate,
			int iSpecificDd, int iPrevRepaidDate, int iMaturityDate, int iGraceDate) throws LogicException {
		this.info("getNextRepayDate ... ");
		this.info("   iAmortizedCode  = " + iAmortizedCode);
		this.info("   iRepayFreq      = " + iRepayFreq);
		this.info("   iFreqBase       = " + iFreqBase);
		this.info("   iSpecificDate   = " + iSpecificDate);
		this.info("   iSpecificDd     = " + iSpecificDd);
		this.info("   iPrevRepaidDate = " + iPrevRepaidDate);
		this.info("   iMaturityDate   = " + iMaturityDate);
		this.info("   iGraceDate      = " + iGraceDate);

		int wkDays = 0;
		int wkMons = 0;
		int wkRemainder = 0;
		int wkNextRepayDate = 0;
		int wkSpecificDate = 0;
		int wkSpecificMons = 0;
		// 指定基準日期、指定基準月差
		// 指定應繳日超過28且指定基準日與指定應繳日不同 => 指定基準日期調整為當年1月
		// iSpecificDate = 20200228, iSpecificDd = 31 => wkSpecificDate=20200131,
		// wkSpecificMons=1
		if (iSpecificDd >= 28 && iSpecificDate % 100 != iSpecificDd) {
			wkSpecificDate = (iSpecificDate / 10000) * 10000 + 100 + iSpecificDd;
			wkSpecificMons = (iSpecificDate / 100) - (wkSpecificDate / 100);
		} else {
			wkSpecificDate = iSpecificDate;
			wkSpecificMons = 0;
		}
		this.info("iSpecificDate=" + iSpecificDate + ", Dd=" + iSpecificDd + ", wkSpecificDate=" + wkSpecificDate
				+ ", wkSpecificMons=" + wkSpecificMons);

		if (iAmortizedCode.equals("1") || iAmortizedCode.equals("2")) { // 1.按月繳息(按期繳息到期還本) 2.到期取息(到期繳息還本)
			wkNextRepayDate = iMaturityDate;
			this.info("   wkNextRepayDate = " + wkNextRepayDate);
			this.info("getNextRepayDate end ");
			return wkNextRepayDate;
		}

		if (iRepayFreq == 00 || iRepayFreq == 99) {
			wkNextRepayDate = iMaturityDate;
			this.info("   wkNextRepayDate = " + wkNextRepayDate);
			this.info("getNextRepayDate end ");
			return wkNextRepayDate;
		}

		// 計算已繳期數
		if (iPrevRepaidDate > iSpecificDate) {
			dDateUtil.init();
			dDateUtil.setDate_1(iSpecificDate);
			dDateUtil.setDate_2(iPrevRepaidDate);
			dDateUtil.dateDiff();
			wkMons = dDateUtil.getMons();
			wkDays = dDateUtil.getDays();
		}
		// 已繳期數再加一期
		switch (iFreqBase) { // 週期基準 1:日 2:月 3:週
		case 2:
			wkRemainder = wkMons % iRepayFreq;
			wkMons = wkMons + iRepayFreq - wkRemainder + wkSpecificMons;
			dDateUtil.init();
			dDateUtil.setDate_1(wkSpecificDate);
			dDateUtil.setMons(wkMons);
			wkNextRepayDate = dDateUtil.getCalenderDay();
			if (iGraceDate == 0) {
				break;
			}
			if (wkNextRepayDate > iGraceDate) {
				break;
			}
			do {
				wkMons = wkMons + iRepayFreq;
				dDateUtil.init();
				dDateUtil.setDate_1(wkSpecificDate);
				dDateUtil.setMons(wkMons);
				wkNextRepayDate = dDateUtil.getCalenderDay();
			} while (wkNextRepayDate <= iGraceDate);
			break;
		case 3:
			wkDays = wkDays + (iRepayFreq * 7);
			dDateUtil.init();
			dDateUtil.setDate_1(iSpecificDate);
			dDateUtil.setDays(wkDays);
			wkNextRepayDate = dDateUtil.getCalenderDay();
			if (iGraceDate == 0) {
				break;
			}
			if (wkNextRepayDate > iGraceDate) {
				break;
			}
			do {
				dDateUtil.init();
				dDateUtil.setDate_1(wkNextRepayDate);
				dDateUtil.setDays(iRepayFreq * 7);
				wkNextRepayDate = dDateUtil.getCalenderDay();
			} while (wkNextRepayDate <= iGraceDate);
			break;
		}
		// 預定還本日是否已超過契約止日
		if (iPrevRepaidDate < iMaturityDate && wkNextRepayDate > iMaturityDate) {
			wkNextRepayDate = iMaturityDate;
		}

		this.info("   wkNextRepayDate = " + wkNextRepayDate);
		this.info("getNextRepayDate end ");
		return wkNextRepayDate;
	}

	/**
	 * 以 CustId 取得 CustUKey
	 * 
	 * @param iCustId 身份證字號/統一編號
	 * @param titaVo  TitaVo
	 * @return CustUKey
	 * @throws LogicException LogicException
	 */
	public String getCustUKey(String iCustId, TitaVo titaVo) throws LogicException {
		String wkCustUKey = "";

		if (iCustId.trim().equals("")) {
			return wkCustUKey;
		}

		CustMain tCustMain = custMainService.custIdFirst(iCustId, titaVo);
		if (tCustMain == null) {
			throw new LogicException(titaVo, "E0001", "客戶資料主檔  身份證字號/統一編號 = " + iCustId); // 查無資料
		}

		wkCustUKey = tCustMain.getCustUKey();
		return wkCustUKey;
	}

	/**
	 * 以 CustId 取得 CustName
	 * 
	 * @param iCustId 身份證字號/統一編號
	 * @return 戶名/公司名稱
	 */
	public String getCustNameById(String iCustId) {
		String wkCustName = "";

		if (iCustId.trim().equals("")) {
			return wkCustName;
		}

		CustMain tCustMain = custMainService.custIdFirst(iCustId);
		if (tCustMain != null) {
			wkCustName = tCustMain.getCustName();
		}

		return wkCustName;
	}

	/**
	 * 以 CustNo 取得 CustName
	 * 
	 * @param iCustNo 戶號
	 * @return 戶名/公司名稱
	 */
	public String getCustNameByNo(int iCustNo) {
		String wkCustName = "";

		if (iCustNo == 0) {
			return wkCustName;
		}

		CustMain tCustMain = custMainService.custNoFirst(iCustNo, iCustNo);
		if (tCustMain != null) {
			wkCustName = tCustMain.getCustName();
		}

		return wkCustName;
	}

	/**
	 * 以 CustUKey 取得 CustId
	 * 
	 * @param iCustUKey CustUKey
	 * @param titaVo    TitaVo
	 * @return 身份證字號/統一編號
	 * @throws LogicException LogicException
	 */
	public String getCustId(String iCustUKey, TitaVo titaVo) throws LogicException {
		String wkCustId = "";

		if (iCustUKey.trim().equals("")) {
			return wkCustId;
		}

		CustMain tCustMain = custMainService.findById(iCustUKey, titaVo);
		if (tCustMain == null) {
			throw new LogicException(titaVo, "E0001", "客戶資料主檔  客戶識別碼 = " + iCustUKey); // 查無資料
		}

		wkCustId = tCustMain.getCustId();
		return wkCustId;
	}

	/**
	 * 以 EmployeeNo 取得 Employee FullName
	 * 
	 * @param iEmployeeNo 員工編號
	 * @return 員工姓名
	 */
	public String getEmpFullnameByEmpNo(String iEmployeeNo) {
		String wkFullname = "";

		if (iEmployeeNo.trim().equals("")) {
			return wkFullname;
		}

		CdEmp tCdEmp = cdEmpService.findById(iEmployeeNo);
		if (tCdEmp != null) {
			wkFullname = tCdEmp.getFullname();
		}

		return wkFullname;
	}

	/**
	 * 以 BankCode 取得 BankItem
	 * 
	 * @param iBankCode 行庫代號(3)+分行代號(4)
	 * @return 行庫名稱
	 */
	public String getBankItemByBankCode(String iBankCode) {
		String wkBankItem = "";
		iBankCode = FormatUtil.padX(iBankCode, 7);
		String bankCode = FormatUtil.padX(iBankCode, 3);
		String branchCode = FormatUtil.right(iBankCode, 4);

		if (iBankCode.trim().equals("")) {
			return wkBankItem;
		}

		CdBank tCdBank = cdBankService.findById(new CdBankId(bankCode, branchCode));
		if (tCdBank != null) {
			wkBankItem = tCdBank.getBankItem();
		}

		return wkBankItem;
	}

	/**
	 * 以 BankCode 取得 BranchItem
	 * 
	 * @param iBankCode 行庫代號(3)+分行代號(4)
	 * @return 行庫名稱
	 */
	public String getBranchItemByBankCode(String iBankCode) {
		String wkBranchItem = "";
		iBankCode = FormatUtil.padX(iBankCode, 7);
		String bankCode = FormatUtil.padX(iBankCode, 3);
		String branchCode = FormatUtil.right(iBankCode, 4);

		if (iBankCode.trim().equals("")) {
			return wkBranchItem;
		}

		CdBank tCdBank = cdBankService.findById(new CdBankId(bankCode, branchCode));
		if (tCdBank != null) {
			// wkBranchItem = tCdBank.getBankItem();
			wkBranchItem = tCdBank.getBranchItem();
		}

		return wkBranchItem;
	}

	/**
	 * 計算逾期數
	 * 
	 * @param iStartDate  起日
	 * @param iEndDate    止日
	 * @param iSpecificDd 指定應繳日
	 * @return 逾期數
	 * @throws LogicException ...
	 */
	public int getOvduTerms(int iStartDate, int iEndDate, int iSpecificDd) throws LogicException {
		this.info("getOvduTerms ... ");
		this.info("   iStartDate   = " + iStartDate);
		this.info("   iEndDate     = " + iEndDate);
		this.info("   iSpecificDd   = " + iSpecificDd);

		int wkOcduTerms = 0;
		int wkSpecificDate = 0;
		wkSpecificDate = (iStartDate / 10000) * 10000 + 100 + iSpecificDd;
		dDateUtil.init();
		dDateUtil.setDate_1(wkSpecificDate);
		dDateUtil.setDate_2(iStartDate);
		dDateUtil.dateDiff();
		wkOcduTerms = dDateUtil.getMons();
		dDateUtil.init();
		dDateUtil.setDate_1(wkSpecificDate);
		dDateUtil.setDate_2(iEndDate);
		dDateUtil.dateDiff();
		wkOcduTerms = dDateUtil.getMons() - wkOcduTerms;

		this.info("getOvduTerms = " + wkOcduTerms);
		return wkOcduTerms;
	}

	/**
	 * 用期數計算收息迄日
	 * 
	 * @param iFreqBase     週期基準
	 * @param iPayIntFreq   繳息週期
	 * @param iSpecificDate 指定基準日期
	 * @param iSpecificDd   指定應繳日
	 * @param iTerms        繳息期數
	 * @param iMaturityDate 到期日
	 * @return 收息迄日
	 * @throws LogicException LogicException
	 */
	public int getPayIntEndDate(int iFreqBase, int iPayIntFreq, int iSpecificDate, int iSpecificDd, int iTerms,
			int iMaturityDate) throws LogicException {
		this.info("getPayIntEndDate ... ");
		this.info("   iFreqBase     = " + iFreqBase);
		this.info("   iPayIntFreq   = " + iPayIntFreq);
		this.info("   iSpecificDate = " + iSpecificDate);
		this.info("   iSpecificDd   = " + iSpecificDd);
		this.info("   iTerms        = " + iTerms);

		int wkPayIntEndDate = 0;

		if (iPayIntFreq == 0 || iPayIntFreq == 99) {
			this.info("getPayIntEndDate end wkPayIntEndDate = " + iMaturityDate);
			return iMaturityDate;
		}

		int wkSpecificDate = 0;
		int wkSpecificMons = 0;
		// 指定基準日期、指定基準月差
		// 指定應繳日超過28且指定基準日與指定應繳日不同 => 指定基準日期調整為當年1月
		// iSpecificDate = 20200228, iSpecificDd = 31 => wkSpecificDate=20200131,
		// wkSpecificMons=1
		if (iSpecificDd >= 28 && iSpecificDate % 100 != iSpecificDd) {
			wkSpecificDate = (iSpecificDate / 10000) * 10000 + 100 + iSpecificDd;
			wkSpecificMons = (iSpecificDate / 100) - (wkSpecificDate / 100);
		} else {
			wkSpecificDate = iSpecificDate;
			wkSpecificMons = 0;
		}
		switch (iFreqBase) { // 週期基準 1:日 2:月 3:週
		case 2:
			dDateUtil.init();
			dDateUtil.setDate_1(wkSpecificDate);
			dDateUtil.setMons(iTerms * iPayIntFreq + wkSpecificMons);
			wkPayIntEndDate = dDateUtil.getCalenderDay();
			break;
		case 3:
			dDateUtil.init();
			dDateUtil.setDate_1(iSpecificDate);
			dDateUtil.setDays(iTerms * iPayIntFreq * 7);
			wkPayIntEndDate = dDateUtil.getCalenderDay();
			break;
		}

		this.info("getPayIntEndDate end wkPayIntEndDate = " + wkPayIntEndDate);
		return wkPayIntEndDate;
	}

	/**
	 * 計算總期數
	 * 
	 * @param iAmortizedCode 攤還方式
	 * @param iFreqBase      週期基準
	 * @param iPayIntFreq    繳息週期
	 * @param iDrawdownDate  撥款日期
	 * @param iMaturityDate  到期日
	 * @return 總期數
	 * @throws LogicException LogicException
	 */
	public int getTotalPeriod(String iAmortizedCode, int iFreqBase, int iPayIntFreq, int iDrawdownDate,
			int iMaturityDate) throws LogicException {
		this.info("getTotalPeriod ... ");
		this.info("   iAmortizedCode = " + iAmortizedCode);
		this.info("   iFreqBase      = " + iFreqBase);
		this.info("   iPayIntFreq    = " + iPayIntFreq);
		this.info("   iDrawdownDate  = " + iDrawdownDate);
		this.info("   iMaturityDate  = " + iMaturityDate);

		int oTotalPeriod = 0;
		int wkMons = 0;
		int wkDays = 0;

		if (iAmortizedCode.equals("2")) { // 2.到期取息(到期繳息還本)
			this.info("getTotalPeriod end ");
			this.info("   oTotalPeriod = 1");
			return 1;
		}
		switch (iFreqBase) { // 週期基準 1:日 2:月 3:週
		case 2:
			dDateUtil.init();
			dDateUtil.setDate_1(iDrawdownDate);
			dDateUtil.setDate_2(iMaturityDate);
			dDateUtil.dateDiffSp();
			wkMons = (dDateUtil.getYears() * 12) + dDateUtil.getMons();
			if (dDateUtil.getDays() > 0) {
				wkMons++;
			}
			oTotalPeriod = wkMons / iPayIntFreq;
			if ((wkMons % iPayIntFreq) > 0) {
				oTotalPeriod++;
			}
			break;
		case 3:
			dDateUtil.init();
			dDateUtil.setDate_1(iDrawdownDate);
			dDateUtil.setDate_2(iMaturityDate);
			dDateUtil.dateDiff();
			wkDays = dDateUtil.getDays();
			oTotalPeriod = wkDays / (iPayIntFreq * 7);
			if ((wkDays % (iPayIntFreq * 7)) > 0) {
				oTotalPeriod++;
			}
			break;
		}
		this.info("getTotalPeriod end ");
		this.info("   oTotalPeriod = " + oTotalPeriod);

		return oTotalPeriod;
	}

	/**
	 * 依寬限期到期日計算寬限期數
	 * 
	 * @param iAmortizedCode 攤還方式
	 * @param iFreqBase      週期基準
	 * @param iPayIntFreq    繳息週期
	 * @param iSpecificDate  指定基準日期
	 * @param iSpecificDd    指定應繳日
	 * @param iGraceDate     寬限到期日
	 * @return 寬限期
	 * @throws LogicException LogicException
	 */
	public int getGracePeriod(String iAmortizedCode, int iFreqBase, int iPayIntFreq, int iSpecificDate, int iSpecificDd,
			int iGraceDate) throws LogicException {
		this.info("getGracePeriod ... ");
		this.info("   iAmortizedCode = " + iAmortizedCode);
		this.info("   iFreqBase      = " + iFreqBase);
		this.info("   iPayIntFreq    = " + iPayIntFreq);
		this.info("   iSpecificDate  = " + iSpecificDate);
		this.info("   iSpecificDd    = " + iSpecificDd);
		this.info("   iGraceDate     = " + iGraceDate);

		int oGracePeriod = 0;
		int wkMons = 0;
		int wkDays = 0;

		if (iAmortizedCode.equals("1") || iAmortizedCode.equals("2") || iGraceDate <= iSpecificDate) { // 1.按月繳息(按期繳息到期還本)
																										// 2.到期取息(到期繳息還本)
			this.info("getGracePeriod end ");
			this.info("   oGracePeriod = " + oGracePeriod);
			return oGracePeriod;
		}
		int wkSpecificDate = 0;
		int wkSpecificMons = 0;
		// 指定基準日期、指定基準月差
		// 指定應繳日超過28且指定基準日與指定應繳日不同 => 指定基準日期調整為當年1月
		// iSpecificDate = 20200228, iSpecificDd = 31 => wkSpecificDate=20200131,
		// wkSpecificMons=1
		if (iSpecificDd >= 28 && iSpecificDate % 100 != iSpecificDd) {
			wkSpecificDate = (iSpecificDate / 10000) * 10000 + 100 + iSpecificDd;
			wkSpecificMons = (iSpecificDate / 100) - (wkSpecificDate / 100);
		} else {
			wkSpecificDate = iSpecificDate;
			wkSpecificMons = 0;
		}
		switch (iFreqBase) { // 週期基準 1:日 2:月 3:週
		case 2:
			dDateUtil.init();
			dDateUtil.setDate_1(wkSpecificDate);
			dDateUtil.setDate_2(iGraceDate);
			dDateUtil.dateDiffSp();
			wkMons = (dDateUtil.getYears() * 12) + dDateUtil.getMons() - wkSpecificMons;
			oGracePeriod = wkMons / iPayIntFreq;
			break;
		case 3:
			dDateUtil.init();
			dDateUtil.setDate_1(iSpecificDate);
			dDateUtil.setDate_2(iGraceDate);
			dDateUtil.dateDiff();
			wkDays = dDateUtil.getDays();
			oGracePeriod = wkDays / (iPayIntFreq * 7);
			break;
		}
		this.info("getGracePeriod end ");
		this.info("   oGracePeriod = " + oGracePeriod);

		return oGracePeriod;
	}

	/**
	 * 依首次應繳日推算指定基準日期
	 * 
	 * @param iSpecificDd   指定應繳日
	 * @param iFirstDueDate 首次應繳日
	 * @param iPayIntFreq   繳息週期
	 * @return 指定基準日期
	 * @throws LogicException LogicException
	 */
	public int getSpecificDate(int iSpecificDd, int iFirstDueDate, int iPayIntFreq) throws LogicException {
		this.info("getSpecificDate ... ");
		this.info("   iSpecificDd   = " + iSpecificDd);
		this.info("   iFirstDueDate = " + iFirstDueDate);
		this.info("   iPayIntFreq = " + iPayIntFreq);

		int oSpecificDate = 0;
		int wkMons = 0;
		int wkSpecificDate = 0; // 暫定基準日期
		int wkDate = 0;

// 首次應繳日  應繳日  暫定基準日期     首次應繳日月差  指定基準日月差   指定基準日期
//  1090115      31      1080131             12            11             1081231 
//  1090120      10      1080110             13            12             1090110 
//  暫定基準日期  ： 首次應繳日前1年1月 
//  首次應繳日月差： 暫定基準日期往前推至日期>=首次應繳日
//  指定基準日月差： 首次應繳日月差減一期
		wkSpecificDate = (iFirstDueDate / 10000 - 1) * 10000 + 100 + iSpecificDd;
		do {
			wkMons++;
			dDateUtil.init();
			dDateUtil.setDate_1(wkSpecificDate);
			dDateUtil.setMons(wkMons);
			wkDate = dDateUtil.getCalenderDay();
		} while (wkDate < iFirstDueDate);

		// 以月差反推指定基準日期
		dDateUtil.init();
		dDateUtil.setDate_1(wkSpecificDate);
		dDateUtil.setMons(wkMons - iPayIntFreq);
		oSpecificDate = dDateUtil.getCalenderDay();

		this.info("getSpecificDate end ");
		this.info("   oSpecificDate = " + oSpecificDate);

		return oSpecificDate;

	}

	/**
	 * 計算到入帳日之繳息期數
	 * 
	 * @param iIntEndCode   指定本次還款日期代碼 1:指定日期之當期數 2:指定日期之已到期應繳期數
	 * @param iFreqBase     週期基準
	 * @param iPayIntFreq   繳息週期
	 * @param iSpecificDate 指定基準日期
	 * @param iSpecificDd   指定應繳日
	 * @param iEntryDate    入帳日
	 * @return 繳息期數
	 * @throws LogicException LogicException
	 */
	public int getTermNo(int iIntEndCode, int iFreqBase, int iPayIntFreq, int iSpecificDate, int iSpecificDd,
			int iEntryDate) throws LogicException {
		this.info("getTermNo ... ");
		this.info("   iIntEndCode   = " + iIntEndCode);
		this.info("   iFreqBase     = " + iFreqBase);
		this.info("   iPayIntFreq   = " + iPayIntFreq);
		this.info("   iSpecificDate = " + iSpecificDate);
		this.info("   iSpecificDd   = " + iSpecificDd);
		this.info("   iTbsDy        = " + iEntryDate);

		int wkMons = 0;
		int wkDays = 0;
		int wkTermNo = 0;
		int wkSpecificDate = 0;
		int wkSpecificMons = 0;
		// 指定基準日期、指定基準月差
		// 指定應繳日超過28且指定基準日與指定應繳日不同 => 指定基準日期調整為當年1月
		// iSpecificDate = 20200228, iSpecificDd = 31 => wkSpecificDate=20200131,
		// wkSpecificMons=1
		if (iSpecificDd >= 28 && iSpecificDate % 100 != iSpecificDd) {
			wkSpecificDate = (iSpecificDate / 10000) * 10000 + 100 + iSpecificDd;
			wkSpecificMons = (iSpecificDate / 100) - (wkSpecificDate / 100);
		} else {
			wkSpecificDate = iSpecificDate;
			wkSpecificMons = 0;
		}

		if (iEntryDate <= iSpecificDate || iPayIntFreq == 0 || iPayIntFreq == 99) {
			this.info("getTermNo end wkTermNo = 1 ");
			if (iIntEndCode == 2) {
				return 0;
			} else {
				return 1;
			}
		}
		switch (iFreqBase)

		{ // 週期基準 1:日 2:月 3:週
		case 2:
			dDateUtil.init();
			dDateUtil.setDate_1(wkSpecificDate);
			dDateUtil.setDate_2(iEntryDate);
			dDateUtil.dateDiffSp();
			wkMons = (dDateUtil.getYears() * 12) + dDateUtil.getMons() - wkSpecificMons;
			if (dDateUtil.getDays() > 0 && iIntEndCode == 1) {
				wkMons++;
			}
			wkTermNo = wkMons / iPayIntFreq;
			if ((wkMons % iPayIntFreq) > 0 && iIntEndCode == 1) {
				wkTermNo++;
			}
			break;
		case 3:
			dDateUtil.init();
			dDateUtil.setDate_1(iSpecificDate);
			dDateUtil.setDate_2(iEntryDate);
			dDateUtil.dateDiff();
			wkDays = dDateUtil.getDays();
			wkTermNo = wkDays / (iPayIntFreq * 7);
			if ((wkDays % (iPayIntFreq * 7)) > 0 && iIntEndCode == 1) {
				wkTermNo++;
			}
			break;
		}

		this.info("getTermNo end wkTermNo = " + wkTermNo);
		return wkTermNo;
	}

	/**
	 * 檢查到撥款的放款交易訂正須由最近一筆交易開始訂正
	 * 
	 * @param ln     LoanBorMain
	 * @param titaVo TitaVo
	 * @throws LogicException LogicException
	 */
	public void checkEraseBormTxSeqNo(LoanBorMain ln, TitaVo titaVo) throws LogicException {
		this.info("checkEraseBormTxSeqNo ... ");
		this.info("   RelCode      = " + titaVo.getRelCodeI());
		this.info("   LastEntDy    = " + ln.getLastEntDy());
		this.info("   LastKinbr    = " + ln.getLastKinbr());
		this.info("   LastTlerNo   = " + ln.getLastTlrNo());
		this.info("   LastTxtNo    = " + ln.getLastTxtNo());
		this.info("   OrgEntdyI    = " + titaVo.getOrgEntdyI());
		this.info("   OrgKin       = " + titaVo.getOrgKin());
		this.info("   OrgTlr       = " + titaVo.getOrgTlr());
		this.info("   OrgTno       = " + titaVo.getOrgTno());

		if (ln.getLastEntDy() != titaVo.getOrgEntdyI() || !ln.getLastKinbr().equals(titaVo.getOrgKin())
				|| !ln.getLastTlrNo().equals(titaVo.getOrgTlr()) || !ln.getLastTxtNo().equals(titaVo.getOrgTno())) {
			TxRecord tTxRecord = txRecordService.findById(new TxRecordId(ln.getLastEntDy() + 19110000,
					ln.getLastKinbr() + ln.getLastTlrNo() + ln.getLastTxtNo()), titaVo);
			String s = "";
			if (tTxRecord != null) {
				s = ", " + tTxRecord.getTranNo() + (tTxRecord.getFlowStep() == 2 ? "主管放行" : "");
			}
			throw new LogicException(titaVo, "E3088", "最近一筆交易序號 = " + ln.getLastEntDy() + "-" + ln.getLastKinbr() + "-"
					+ ln.getLastTlrNo() + "-" + ln.getLastTxtNo() + s); // 放款交易訂正須由最後一筆交易開始訂正
		}
		// 登錄訂正
		if (titaVo.isActfgEntry() && titaVo.isHcodeErase()) {
			if (titaVo.getRelCodeI() == 2) {
				if (ln.getActFg() != 1) {
					throw new LogicException(titaVo, "E0016", "撥款主檔 戶號 = " + ln.getCustNo() + "額度編號 = " + ln.getFacmNo()
							+ "撥款序號 = " + ln.getBormNo() + " 交易流程步驟 = " + ln.getActFg()); // 該筆交易狀態非登錄，不可做訂正登錄交易
																							// ??
				}
			} else {
				if (ln.getActFg() != 0 && ln.getActFg() != 2) {
					throw new LogicException(titaVo, "E0016", "撥款主檔 戶號 = " + ln.getCustNo() + "額度編號 = " + ln.getFacmNo()
							+ "撥款序號 = " + ln.getBormNo() + " 交易流程步驟 = " + ln.getActFg()); // 該筆交易狀態非登錄，不可做訂正登錄交易
																							// ??
				}
			}
		}
		// 放行訂正
		if (titaVo.isActfgSuprele() && titaVo.isHcodeErase()) {
			if (ln.getActFg() != 0 && ln.getActFg() != 2) {
				throw new LogicException(titaVo, "E0018", "撥款主檔 戶號 = " + ln.getCustNo() + "額度編號 = " + ln.getFacmNo()
						+ "撥款序號 = " + ln.getBormNo() + " 交易流程步驟 = " + ln.getActFg()); // 該筆交易狀態非已放行，不可做訂正已放行交易
			}
		}
	}

	/**
	 * 檢查到額度的的放款交易訂正須由最近一筆交易開始訂正
	 * 
	 * @param fac    FacMain
	 * @param titaVo TitaVo
	 * @throws LogicException LogicException
	 */
	public void checkEraseFacmTxSeqNo(FacMain fac, TitaVo titaVo) throws LogicException {
		this.info("chkEraseTxSeqNo ... ");
		this.info("   RelCode      = " + titaVo.getRelCodeI());
		this.info("   LastAcctDate = " + fac.getLastAcctDate());
		this.info("   LastKinbr    = " + fac.getLastKinbr());
		this.info("   LastTlerNo   = " + fac.getLastTlrNo());
		this.info("   LastTxtNo    = " + fac.getLastTxtNo());
		this.info("   OrgEntdyI    = " + titaVo.getOrgEntdyI());
		this.info("   OrgKin       = " + titaVo.getOrgKin());
		this.info("   OrgTlr       = " + titaVo.getOrgTlr());
		this.info("   OrgTno       = " + titaVo.getOrgTno());

		if (fac.getLastAcctDate() != titaVo.getOrgEntdyI() || !fac.getLastKinbr().equals(titaVo.getOrgKin())
				|| !fac.getLastTlrNo().equals(titaVo.getOrgTlr()) || !fac.getLastTxtNo().equals(titaVo.getOrgTno())) {
			throw new LogicException(titaVo, "E3088", "最近一筆交易序號 = " + fac.getLastAcctDate() + "-" + fac.getLastKinbr()
					+ "-" + fac.getLastTlrNo() + "-" + fac.getLastTxtNo()); // 放款交易訂正須由最後一筆交易開始訂正
		}
		// 登錄訂正
		if (titaVo.isActfgEntry() && titaVo.isHcodeErase()) {
			if (titaVo.getRelCodeI() == 2) {
				if (fac.getActFg() != 1) {
					throw new LogicException(titaVo, "E0016", "撥款主檔 戶號 = " + fac.getCustNo() + "額度編號 = "
							+ fac.getFacmNo() + " 交易流程步驟 = " + fac.getActFg()); // 該筆交易狀態非登錄，不可做訂正登錄交易
																				// ??
				}
			} else {
				if (fac.getActFg() != 0 && fac.getActFg() != 2) {
					throw new LogicException(titaVo, "E0016", "撥款主檔 戶號 = " + fac.getCustNo() + "額度編號 = "
							+ fac.getFacmNo() + " 交易流程步驟 = " + fac.getActFg()); // 該筆交易狀態非登錄，不可做訂正登錄交易
																				// ??
				}
			}
		}
		// 放行訂正
		if (titaVo.isActfgSuprele() && titaVo.isHcodeErase()) {
			if (fac.getActFg() != 0 && fac.getActFg() != 2) {
				throw new LogicException(titaVo, "E0018",
						"撥款主檔 戶號 = " + fac.getCustNo() + "額度編號 = " + fac.getFacmNo() + " 交易流程步驟 = " + fac.getActFg()); // 該筆交易狀態非已放行，不可做訂正已放行交易
			}
		}
	}

	/**
	 * 收回費用處理(新增帳務及放款交易內容檔)
	 * 
	 * @param baTxList   還款明細
	 * @param iRpCode    還款來源
	 * @param iEntryDate 入帳日
	 * @param iTempVo    OtherFields
	 * @param lAcDetail  帳務明細
	 * @param titaVo     TitaVo
	 * @throws LogicException ....
	 */
	public void settleFeeRoutine(ArrayList<BaTxVo> baTxList, int iRpCode, int iEntryDate, TempVo iTempVo,
			List<AcDetail> lAcDetail, TitaVo titaVo) throws LogicException {
		for (BaTxVo ba : baTxList) {
			if (ba.getRepayType() >= 4 && ba.getAcctAmt().compareTo(BigDecimal.ZERO) > 0) {
				// 暫收款金額 (暫收借)
				settleTempAmt(baTxList, lAcDetail, titaVo);
				AcDetail acDetail = new AcDetail();
				acDetail.setDbCr("C");
				acDetail.setAcctCode(ba.getAcctCode());
				acDetail.setTxAmt(ba.getAcctAmt());
				acDetail.setCustNo(ba.getCustNo());
				acDetail.setFacmNo(ba.getFacmNo());
				acDetail.setBormNo(ba.getBormNo());
				acDetail.setRvNo(ba.getRvNo());
				acDetail.setReceivableFlag(ba.getReceivableFlag());
				lAcDetail.add(acDetail);
				// 累溢收入帳(暫收貸)
				settleOverflow(lAcDetail, titaVo);
				// 新增放款交易內容檔(收回費用)
				String desc = "";
				if ("L3210".equals(titaVo.getTxcd())) {
					desc = "暫收銷";
				}
				// 費用科目代碼
				desc += getCdCodeX("AcctCode", ba.getAcctCode(), titaVo);
				addFeeBorTxRoutine(ba, iRpCode, desc, iEntryDate, iTempVo, lAcDetail, titaVo);
				ba.setAcctAmt(BigDecimal.ZERO);
			}
		}
	}

	/**
	 * 新增放款交易內容檔(收回費用)
	 * 
	 * @param ba         BaTxVo
	 * @param iRpCode    還款來源
	 * @param iDesc      交易別
	 * @param iEntryDate 入帳日
	 * @param iTempVo    TempVo
	 * @param lAcDatail  List of AcDetail
	 * @param titaVo     TitaVo
	 * @return LoanBorTx
	 * @throws LogicException ....
	 */
	public LoanBorTx addFeeBorTxRoutine(BaTxVo ba, int iRpCode, String iDesc, int iEntryDate, TempVo iTempVo,
			List<AcDetail> lAcDatail, TitaVo titaVo) throws LogicException {
		this.info("addFeeBorTxRoutine ... ");

		LoanBorTx tLoanBorTx = new LoanBorTx();
		LoanBorTxId tLoanBorTxId = new LoanBorTxId();
		setFacmBorTx(tLoanBorTx, tLoanBorTxId, ba.getCustNo(), ba.getFacmNo(), titaVo);
		tLoanBorTx.setDesc(iDesc);
		tLoanBorTx.setRepayCode(iRpCode); // 還款來源
		tLoanBorTx.setEntryDate(iEntryDate);
		tLoanBorTx.setDueDate(ba.getPayIntDate());
		tLoanBorTx.setFeeAmt(ba.getAcctAmt()); // 實收費用金額
		tLoanBorTx.setDisplayflag("A"); // 無繳息

		// 其他欄位
		switch (ba.getRepayType()) {
		case 4:
			iTempVo.putParam("AcctFee", ba.getAcctAmt());
			break;
		case 5:
			iTempVo.putParam("FireFee", ba.getAcctAmt());
			break;
		case 6:
			iTempVo.putParam("ModifyFee", ba.getAcctAmt());
			break;
		case 7:
			iTempVo.putParam("LawFee", ba.getAcctAmt());
			break;
		}
		iTempVo.putParam("AcctCode", ba.getAcctCode()); // 業務科目銷
		iTempVo.putParam("RvNo", ba.getRvNo()); // 銷帳編號
		tLoanBorTx.setOtherFields(iTempVo.getJsonString());

		// 更新放款明細檔及帳務明細檔關聯欄
		this.updBorTxAcDetail(tLoanBorTx, lAcDatail);

		try {
			loanBorTxService.insert(tLoanBorTx, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "放款交易內容檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
		}
		return tLoanBorTx;
	}

	/**
	 * 訂正放款交易內容檔by交易
	 * 
	 * @param iCustNo 戶號
	 * @param titaVo  Tita
	 * @throws LogicException ....
	 */
	public void setFacmBorTxHcodeByTx(int iCustNo, TitaVo titaVo) throws LogicException {
		this.info("setFacmBorTxHcodeByTx ...");

		Slice<LoanBorTx> slLoanBortx = loanBorTxService.custNoTxtNoEq(iCustNo, titaVo.getOrgEntdyI() + 19110000,
				titaVo.getOrgKin(), titaVo.getOrgTlr(), titaVo.getOrgTno(), 0, Integer.MAX_VALUE, titaVo);
		if (slLoanBortx == null) {
			return;
		}
		for (LoanBorTx tx : slLoanBortx.getContent()) {
			setFacmBorTxHcode(iCustNo, tx.getFacmNo(), tx.getBorxNo(), titaVo);
		}
	}

	/**
	 * 更新疑似洗錢交易訪談記錄檔
	 * 
	 * @param iCustNo    戶號
	 * @param iFacmNo    額度
	 * @param iBormNo    撥款
	 * @param iEntryDate 入帳日
	 * @param iRepayAmt  還款金額
	 * @param titaVo     TitaVo
	 * @throws LogicException ....
	 */
	public void updateMlaundryRecord(int iCustNo, int iFacmNo, int iBormNo, int iEntryDate, BigDecimal iRepayAmt,
			TitaVo titaVo) throws LogicException {
		this.info("mlaundryRecordRoutine ...");
		Slice<MlaundryRecord> slMlaundryRecord = mlaundryRecordService.findCustNoEq(iCustNo, iFacmNo,
				iFacmNo > 0 ? iFacmNo : 999, iBormNo, iBormNo > 0 ? iBormNo : 900, iEntryDate + 19110000, this.index,
				Integer.MAX_VALUE, titaVo);
		if (slMlaundryRecord == null) {
			return;
		}
		for (MlaundryRecord t : slMlaundryRecord.getContent()) {
			MlaundryRecord tMlaundryRecord = mlaundryRecordService.holdById(t, titaVo);
			if (titaVo.isHcodeNormal()) {
				if (t.getActualRepayDate() == 0) {
					tMlaundryRecord.setActualRepayDate(iEntryDate);
					tMlaundryRecord.setActualRepayAmt(iRepayAmt);// 實際還本金額
					break;
				}
			}
			try {
				mlaundryRecordService.update(tMlaundryRecord, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", "疑似洗錢交易訪談記錄檔 " + e.getErrorMsg()); // 更新資料時，發生錯誤
			}
		}
	}

	/**
	 * 取得暫收款額度
	 * 
	 * @param iCustNo      戶號
	 * @param iFacmNo      額度
	 * @param iFirstFacmNo 資料首筆額度
	 * @param titaVo       TitaVo
	 * @return 暫收款額度
	 * @throws LogicException ....
	 */
	public int getTmpFacmNo(int iCustNo, int iFacmNo, int iFirstFacmNo, TitaVo titaVo) throws LogicException {
//  暫收款額度 > 0 => 單一額度
//  暫收款額度 = 0 => 全部非指定額度
		if (iFacmNo > 0) {
			return iFacmNo;
		}
		Slice<LoanFacTmp> slLoanFacTmp = loanFacTmpService.findCustNo(iCustNo, 0, Integer.MAX_VALUE, titaVo);
		if (slLoanFacTmp == null) {
			return 0;
		}
		int firstFacmNo = iFirstFacmNo;
		Boolean isLoanFacTmp = false;
		for (LoanFacTmp t : slLoanFacTmp.getContent()) {
			if (iFacmNo == t.getFacmNo()) {
				isLoanFacTmp = true;
			}
		}
		if (isLoanFacTmp) {
			return firstFacmNo;
		} else {
			return 0;
		}
	}

	/**
	 * 檢查額度是否為暫收指定額度
	 * 
	 * @param iCustNo 戶號
	 * @param iFacmNo 額度
	 * @param titaVo  TitaVo
	 * @return true/false
	 * @throws LogicException ....
	 */
	public Boolean isLoanFacTmp(int iCustNo, int iFacmNo, TitaVo titaVo) throws LogicException {

		Slice<LoanFacTmp> slLoanFacTmp = loanFacTmpService.findCustNo(iCustNo, 0, Integer.MAX_VALUE, titaVo);
		if (slLoanFacTmp == null) {
			return false;
		}
		for (LoanFacTmp t : slLoanFacTmp.getContent()) {
			if (iFacmNo == t.getFacmNo()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 累溢收(暫收貸)帳務處理
	 * 
	 * @param lAcDetail List of AcDetail
	 * @param titaVo    titaVo
	 * @throws LogicException ....
	 */

	public void settleOverflow(List<AcDetail> lAcDetail, TitaVo titaVo) throws LogicException {
		this.info("settleOverflow ..." + lAcDetail.size());

		int wkCustNo = 0;
		int wkFacmNo = 0;
		BigDecimal wkOverflow = BigDecimal.ZERO;

		// 暫收款餘額
		for (AcDetail ac : lAcDetail) {
			wkCustNo = ac.getCustNo();
			// Overflow 累溢收金額 暫收貸(正值)
			if ("D".equals(ac.getDbCr())) {
				wkOverflow = wkOverflow.add(ac.getTxAmt());
			} else {
				wkOverflow = wkOverflow.subtract((ac.getTxAmt()));
			}
//			this.info(ac.getDbCr() + " " + ac.getAcctCode() + " " + FormatUtil.padLeft("" + ac.getTxAmt(), 11) + " "
//					+ wkOverflow + " " + ac.getCustNo() + "-" + ac.getFacmNo() + "-" + ac.getBormNo() + " "
//					+ ac.getRvNo());
			int sumNo = 0;
			if (parse.isNumeric(ac.getSumNo())) {
				sumNo = parse.stringToInteger(ac.getSumNo());
			}
			if (sumNo <= 90 && ac.getFacmNo() > 0) {
				wkFacmNo = ac.getFacmNo();
			}
		}

		if (wkOverflow.compareTo(BigDecimal.ZERO) > 0) {
			AcDetail acDetail = new AcDetail();
			acDetail.setDbCr("C");
			acDetail.setAcctCode("TAV");
			acDetail.setTxAmt(wkOverflow);
			acDetail.setCustNo(wkCustNo);
			acDetail.setFacmNo(wkFacmNo);
			acDetail.setBormNo(0);
			acDetail.setSumNo("090"); // 暫收可抵繳
			lAcDetail.add(acDetail);
		}

		this.info("settleOverflow end" + ", wkOverflow=" + wkOverflow);

	}

	/**
	 * 暫收款金額 (暫收借) 帳務處理
	 * 
	 * @param baTxList  ArrayList of BaTxVo
	 * @param lAcDetail List of AcDetail
	 * @param titaVo    TitaVo
	 * @throws LogicException ....
	 */
	public void settleTempAmt(ArrayList<BaTxVo> baTxList, List<AcDetail> lAcDetail, TitaVo titaVo)
			throws LogicException {
		this.info("settleTempAmt ... " + lAcDetail.size());
		int acPaymentSize = 0;
		if (this.txBuffer.getAcDetailList() != null) {
			acPaymentSize = this.txBuffer.getAcDetailList().size();
		}
		// 僅有收付欄分錄為首筆
		if (lAcDetail.size() == acPaymentSize) {
			// 借方：暫收抵繳 (交易前累溢收)
			for (BaTxVo ba : baTxList) {
				if (ba.getDataKind() == 3 && ba.getAcctAmt().compareTo(BigDecimal.ZERO) > 0) {
					AcDetail acDetail = new AcDetail();
					acDetail.setDbCr("D");
					acDetail.setAcctCode(ba.getAcctCode());
					acDetail.setSumNo("090");
					acDetail.setTxAmt(ba.getAcctAmt());
					acDetail.setCustNo(ba.getCustNo());
					acDetail.setFacmNo(ba.getFacmNo());
					acDetail.setBormNo(ba.getBormNo());
					acDetail.setRvNo(ba.getRvNo());
					acDetail.setReceivableFlag(ba.getReceivableFlag());
					lAcDetail.add(acDetail);
					this.info("settleTempAmt ba " + acDetail.toString());
				}
			}

		} else {
			AcDetail tAc = lAcDetail.get(lAcDetail.size() - 1);

			if ("090".equals(tAc.getSumNo()) && "C".equals(tAc.getDbCr())) {
				AcDetail acDetail = new AcDetail();
				acDetail.setDbCr("D");
				acDetail.setAcctCode(tAc.getAcctCode());
				acDetail.setSumNo("090");
				acDetail.setTxAmt(tAc.getTxAmt());
				acDetail.setCustNo(tAc.getCustNo());
				acDetail.setFacmNo(tAc.getFacmNo());
				acDetail.setBormNo(tAc.getBormNo());
				lAcDetail.add(acDetail);
				this.info("settleTempAmt Last-090  " + acDetail.toString());
			}
		}
		this.info("settleTempAmt end " + lAcDetail.size());
	}

	//
	/**
	 * 更新放款明細檔及帳務明細檔關聯欄
	 * 
	 * @param tLoanBorTx LoanBorTx
	 * @param lAcDetail  List of AcDetail
	 * @throws LogicException ....
	 */

	public void updBorTxAcDetail(LoanBorTx tLoanBorTx, List<AcDetail> lAcDetail) throws LogicException {
		this.info("updAcDetailBorxNo ..." + lAcDetail.size());
// 交易金額 + 暫收借 = 還款金額 + 暫收貸
// TempAmt  暫收借 = SumNo90~98 借方
// OverAmt  暫收貸 = SumNo90~98 貸方 
// RepayAmt 還款金額 = [SumNo==0]貸方-借方
// TxAmt    交易金額 = 還款金額 + 暫收貸 - 暫收借 
// 交易金額在暫收款轉帳時需運算
//  額度 001 轉 額度 002 $100
//     額度 001 TxAmt = -$100
//          Db: TAV $550 
//                 Cr: TAV $450
//     額度 002 TxAmt = $100
//          Db: TAV $210
//                 Cr: TAV $310
// 
		BigDecimal txAmt = BigDecimal.ZERO;
		BigDecimal tempAmt = BigDecimal.ZERO;
		BigDecimal overAmt = BigDecimal.ZERO;
		BigDecimal repayAmt = BigDecimal.ZERO;
		int acSeq = 0;
		int ii = 0;
		for (AcDetail ac : lAcDetail) {
			ii++;
			TempVo acTempVo = new TempVo();
			if (ac.getJsonFields() != null) {
				acTempVo = acTempVo.getVo(ac.getJsonFields());
			}
			if (acTempVo.get("BorxNo") == null) {
				if (acSeq == 0) {
					acSeq = ii;
				}
				int sumNo = 0;
				if (parse.isNumeric(ac.getSumNo())) {
					sumNo = parse.stringToInteger(ac.getSumNo());
				}
				if (sumNo == 0) {
					if ("C".equals(ac.getDbCr())) {
						repayAmt = repayAmt.add(ac.getTxAmt());
					} else {
						repayAmt = repayAmt.subtract(ac.getTxAmt());
					}
				} else if (sumNo >= 90 && sumNo <= 98) {
					if ("D".equals(ac.getDbCr())) {
						tempAmt = tempAmt.add(ac.getTxAmt());
					} else {
						overAmt = overAmt.add(ac.getTxAmt());
					}
				}
				acTempVo.putParam("BorxNo", tLoanBorTx.getBorxNo());
				if (ac.getFacmNo() != tLoanBorTx.getFacmNo()) {
					acTempVo.putParam("FacmNo", tLoanBorTx.getFacmNo());
				}
				if (ac.getBormNo() != tLoanBorTx.getBormNo()) {
					acTempVo.putParam("BormNo", tLoanBorTx.getBormNo());
				}
				acTempVo.putParam("EntryDate", tLoanBorTx.getEntryDate() + 19110000);
				ac.setJsonFields(acTempVo.getJsonString());
			}
		}
		txAmt = repayAmt.add(overAmt).subtract(tempAmt);
		tLoanBorTx.setTxAmt(txAmt);
		tLoanBorTx.setTempAmt(tempAmt);
		tLoanBorTx.setOverflow(overAmt);
		TempVo txTempVo = new TempVo();
		if (tLoanBorTx.getOtherFields() != null) {
			txTempVo = txTempVo.getVo(tLoanBorTx.getOtherFields());
		}
		txTempVo.putParam("AcSeq", parse.IntegerToString(acSeq, 4));
		tLoanBorTx.setOtherFields(txTempVo.getJsonString());
		this.info("updAcDetailBorxNo end txAmt=" + txAmt + ", tempAmt=" + tempAmt + ", repayAmt=" + repayAmt
				+ ", overAmt=" + overAmt);

	}

	/**
	 * 取得代碼說明
	 * 
	 * @param defCode 代碼檔代號
	 * @param cdCode  代碼
	 * @param titaVo  TitaVo
	 * @return 代碼說明
	 * @throws LogicException ....
	 */
	public String getCdCodeX(String defCode, String cdCode, TitaVo titaVo) throws LogicException {
		CdCode tCdCode = cdCodeService.findById(new CdCodeId(defCode, cdCode), titaVo);
		return tCdCode == null ? "" : tCdCode.getItem();
	}

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		// TODO Auto-generated method stub
		return null;
	}

}
