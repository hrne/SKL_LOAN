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
import com.st1.itx.db.domain.BatxRateChange;
import com.st1.itx.db.domain.BatxRateChangeId;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.LoanBorMainId;
import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.domain.LoanBorTxId;
import com.st1.itx.db.domain.LoanRateChange;
import com.st1.itx.db.domain.LoanRateChangeId;
import com.st1.itx.db.service.BatxRateChangeService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.db.service.LoanRateChangeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/*
 * L3721 借戶利率變更
 * a.使用時機:欲針對某一戶號下各撥款,某額度下各撥款或單一撥款變更其加減碼時用.
 * b.變更時,直接輸入調整後利率,由電腦換算出加減碼.
 * c.無論約定利率區分係固定,機動或定期機動,均可變更.
 * d.本交易為2段式交易
 * e.撥款件〈新撥件〉利率屬定期機動者，須輸入第一年、第二年、…利率及調整日期。
 * f.增貸件，動撥件，撥尾款等，若有下次調整利率，也要作調整。
 */

/**
 * L3721 借戶利率變更
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3721")
@Scope("prototype")
public class L3721 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public LoanBorMainService loanBorMainService;
	@Autowired
	public LoanBorTxService loanBorTxService;
	@Autowired
	public LoanRateChangeService loanRateChangeService;
	@Autowired
	public BatxRateChangeService batxRateChangeService;

	@Autowired
	Parse parse;
	@Autowired
	LoanCom loanCom;
	@Autowired
	public DateUtil dateUtil;
	@Autowired
	DataLog datalog;
	@Autowired
	SendRsp sendRsp;

	private TitaVo titaVo = new TitaVo();
	private int iCustNo;
	private int iFacmNo;
	private int iBormNo;
	private int iBormNoS = 0;
	private int iBormNoE = 900;
	private int iEffectDate;
	private int iNextRateAdjDate;
	private String iRateCode;
	private String iProdNo;
//	private String iProdName;
	private String iBaseRateCode;
	private String iIncrFlag;
	private BigDecimal iBaseRate;
//	private BigDecimal iProdRate;
	private BigDecimal iFitRate;
	private BigDecimal iRateIncr;
	private BigDecimal iIndividualIncr;
	private String iRemark;
	private String iDeleteFg;

	// work area
	// private int wkTbsDy;
	private int wkCustNo;
	private int wkFacmNo;
	private int wkBormNo;
	private int wkBorxNo;
	private int wkEffectDate;
	private int wkNextRateAdjDate;
	private String wkInsertFlag = "";
	private TempVo tTempVo = new TempVo();
	private LoanBorMain tLoanBorMain;
	private LoanBorMain beforeLoanBorMain;
	private LoanBorTx tLoanBorTx;
	private LoanBorTxId tLoanBorTxId;
	private LoanRateChange tLoanRateChange;
	private LoanRateChange beforeLoanRateChange;
	private LoanRateChangeId tLoanRateChangeId;
	private List<LoanBorMain> lLoanBorMain;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3721 ");
		this.info("   isActfgEntry   = " + titaVo.isActfgEntry());
		this.info("   isActfgRelease = " + titaVo.isActfgRelease());
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
		// this.wkTbsDy = this.txBuffer.getTxCom().getTbsdy();
		loanCom.setTxBuffer(this.txBuffer);

		// 取得輸入資料
		iCustNo = this.parse.stringToInteger(titaVo.getParam("TimCustNo"));
		iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		iBormNo = this.parse.stringToInteger(titaVo.getParam("BormNo"));
		iEffectDate = this.parse.stringToInteger(titaVo.getParam("EffectDate2"));
		iRateCode = titaVo.getParam("RateCode2");
		iProdNo = titaVo.getParam("ProdNo2");
//		iProdName = titaVo.getParam("ProdNo2X");
		iBaseRateCode = titaVo.getParam("BaseRateCode2");
		iIncrFlag = titaVo.getParam("IncrFlag2");
		iBaseRate = this.parse.stringToBigDecimal(titaVo.getParam("BaseRate2"));
//		iProdRate = this.parse.stringToBigDecimal(titaVo.getParam("ProdRate2"));

		if (iBormNo > 0) {
			iBormNoS = iBormNo;
			iBormNoE = iBormNo;
		}

		// 自訂利率
		if ("99".equals(iBaseRateCode)) {
			iFitRate = this.parse.stringToBigDecimal(titaVo.getParam("FitRate2"));
			iIndividualIncr = BigDecimal.ZERO;
			iRateIncr = BigDecimal.ZERO;
		}

		// 指標利率
		else {
			// 輸入加碼利率
			if (this.parse.stringToBigDecimal(titaVo.getParam("RateIncr2")).compareTo(BigDecimal.ZERO) != 0) {
				// 利率按合約
				if ("Y".equals(iIncrFlag)) {
					iIndividualIncr = BigDecimal.ZERO;
					iRateIncr = this.parse.stringToBigDecimal(titaVo.getParam("RateIncr2"));
					iFitRate = iBaseRate.add(iRateIncr);
				} else {
					iRateIncr = this.parse.stringToBigDecimal(titaVo.getParam("RateIncr1"));
					iIndividualIncr = this.parse.stringToBigDecimal(titaVo.getParam("RateIncr2"));
					iFitRate = iBaseRate.add(iIndividualIncr);
				}
			}
			// 輸入適用利率
			else {
				iFitRate = this.parse.stringToBigDecimal(titaVo.getParam("FitRate2"));
				if ("Y".equals(iIncrFlag)) {
					iIndividualIncr = BigDecimal.ZERO;
					iRateIncr = iFitRate.subtract(iBaseRate);
				} else {
					iRateIncr = this.parse.stringToBigDecimal(titaVo.getParam("RateIncr1"));
					iIndividualIncr = iFitRate.subtract(iBaseRate);
				}
			}
		}

		iRemark = titaVo.getParam("Remark");
		iNextRateAdjDate = this.parse.stringToInteger(titaVo.getParam("NextRateAdjDate2")); // 下次利率調整日
		iDeleteFg = titaVo.getParam("DeleteFg"); // 是否刪除 Y/N

		// 刪除為1段式需主管刷卡
		if ("Y".equals(iDeleteFg)) {
			titaVo.putParam("RELCD", "0");
			titaVo.putParam("ACTFG", "0");
			// 交易需主管核可
			if (!titaVo.getHsupCode().equals("1")) {
				sendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}
		}

		// 登錄
		if (titaVo.isActfgEntry() && titaVo.isHcodeNormal()) {
			EntryNormalRoutine();
		}
		// 登錄 修正
		if (titaVo.isActfgEntry() && titaVo.isHcodeModify()) {
			if ("Y".equals(iDeleteFg)) {
				throw new LogicException(titaVo, "E0010", "刪除，不可修正"); // 功能選擇錯誤
			}
			EntryEraseRoutine();
			EntryNormalRoutine();
		}
		// 訂正
		if (titaVo.isHcodeErase()) {
			throw new LogicException(titaVo, "E0010", "本交易不可訂正"); // 功能選擇錯誤
		}
		// 放行
		if (titaVo.isActfgSuprele()) {
			ReleaseRoutine();
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void EntryNormalRoutine() throws LogicException {
		this.info("EntryNormalRoutine ... ");

		Slice<LoanBorMain> slLoanBorMain = loanBorMainService.bormCustNoEq(iCustNo, iFacmNo, iFacmNo, iBormNoS,
				iBormNoE, 0, Integer.MAX_VALUE, titaVo);
		lLoanBorMain = slLoanBorMain == null ? null : new ArrayList<LoanBorMain>(slLoanBorMain.getContent());
		if (lLoanBorMain == null || lLoanBorMain.size() == 0) {
			throw new LogicException(titaVo, "E0001", "放款主檔"); // 查詢資料不存在
		}
		for (LoanBorMain ln : lLoanBorMain) {
			wkCustNo = ln.getCustNo();
			wkFacmNo = ln.getFacmNo();
			wkBormNo = ln.getBormNo();
			if (ln.getStatus() != 0) {
				continue;
			}

			// 鎖定放款利率變動檔
			tLoanRateChange = loanRateChangeService
					.holdById(new LoanRateChangeId(iCustNo, iFacmNo, wkBormNo, iEffectDate + 19110000), titaVo);
			if ("Y".equals(iDeleteFg) && tLoanRateChange == null) {
				this.totaVo.setWarnMsg(
						"無放款利率變動檔資料 戶號 = " + iCustNo + " 額度編號 = " + iFacmNo + " 撥款序號 = " + "," + iEffectDate);
				this.addList(this.totaVo);
				continue;
			}
			wkInsertFlag = tLoanRateChange == null ? "Y" : "N";

			// 鎖定放款主檔
			tLoanBorMain = loanBorMainService.holdById(new LoanBorMainId(iCustNo, iFacmNo, wkBormNo), titaVo);
			if (tLoanBorMain == null) {
				throw new LogicException(titaVo, "E0006",
						"撥款主檔 戶號 = " + iCustNo + " 額度編號 = " + iFacmNo + " 撥款序號 = " + wkBormNo); // 鎖定資料時，發生錯誤
			}

			wkBorxNo = tLoanBorMain.getLastBorxNo() + 1;

			// 新增交易暫存檔
			AddTxTempRoutine();

			// 新增放款利率變動檔
			if ("Y".equals(iDeleteFg)) {
				wkEffectDate = iEffectDate;
				// 變更,刪除利率新增DATELOG,新增不寫DATELOG
				DelLoanRateChangeRoutine(wkEffectDate);
			} else {
				if (wkInsertFlag.equals("Y")) {
					InsLoanRateChangeRoutine();
				} else {
					// 變更,刪除利率新增DATELOG,新增不寫DATELOG
					UpdLoanRateChangeRoutine();
				}
				// 新增自訂利率的機動利率的預調利率
				if ("1".equals(iRateCode) && "99".equals(iBaseRateCode) && iNextRateAdjDate > 0) {
					InsLoanRateChangeRoutine2();
				}
			}

			// 登錄時檢核如L4321已確認未放行，則出錯誤訊息
			CheckBatxRateChangeRoutine();

			// 更新放款主檔
			updLoanBorMainRoutine();

			// 檢核下次利率調整日期
			if (iNextRateAdjDate > 0) {
				checkNextRateAdjDate();
			}

			// 新增放款交易內容檔
			addLoanBorTxRoutine();
		}
		if ("".equals(wkInsertFlag)) {
			throw new LogicException(titaVo, "E0010", "非正常戶"); // 功能選擇錯誤
		}
	}

	private void ReleaseRoutine() throws LogicException {
		this.info("ReleaseRoutine ... ");

		Slice<LoanBorTx> slLoanBorTx = loanBorTxService.custNoTxtNoEq(iCustNo, titaVo.getOrgEntdyI() + 19110000,
				titaVo.getOrgKin(), titaVo.getOrgTlr(), titaVo.getOrgTno(), 0, Integer.MAX_VALUE, titaVo);
		if (slLoanBorTx == null) {
			throw new LogicException(titaVo, "E0001", "交易明細檔 分行別 = " + titaVo.getOrgKin() + " 交易員代號 = "
					+ titaVo.getOrgTlr() + " 交易序號 = " + titaVo.getOrgTno()); // 查詢資料不存在
		}
		for (LoanBorTx tx : slLoanBorTx.getContent()) {
			wkCustNo = tx.getCustNo();
			wkFacmNo = tx.getFacmNo();
			wkBormNo = tx.getBormNo();
			wkBorxNo = tx.getBorxNo();
			tTempVo = tTempVo.getVo(tx.getOtherFields());
			this.info("   wkCustNo = " + wkCustNo);
			this.info("   wkFacmNo = " + wkFacmNo);
			this.info("   wkBormNo = " + wkBormNo);
			this.info("   wkBorxNo = " + wkBorxNo);
			ReleaseLoanBorMainRoutine();
		}
	}

	// 登錄時檢核如L4321已確認未放行，則出錯誤訊息
	private void CheckBatxRateChangeRoutine() throws LogicException {
		this.info("CheckBatxRateChangeRoutine ... ");

		BatxRateChange tBatxRateChange = batxRateChangeService
				.findById(new BatxRateChangeId(titaVo.getEntDyI() + 19110000, iCustNo, iFacmNo, wkBormNo), titaVo);
		if (tBatxRateChange != null && tBatxRateChange.getConfirmFlag() == 1) {
			throw new LogicException(titaVo, "E0007", "整批利率調整確認未放行"); // 更新資料時，發生錯誤

		}
	}

	// 新增交易暫存檔(放款資料)
	private void AddTxTempRoutine() throws LogicException {
		this.info("AddTxTempRoutine ... ");
		tTempVo.clear();
		tTempVo.putParam("InsertFlag", wkInsertFlag);
		tTempVo.putParam("EffectDate", iEffectDate);
		tTempVo.putParam("NextRateAdjDate", iNextRateAdjDate);
		tTempVo.putParam("NextAdjRateDate", tLoanBorMain.getNextAdjRateDate());
		tTempVo.putParam("StoreRate", tLoanBorMain.getStoreRate());
		tTempVo.putParam("DueAmt", tLoanBorMain.getDueAmt());
		tTempVo.putParam("Note", iRemark);

		if (wkInsertFlag.equals("N")) {
			tTempVo.putParam("Status", tLoanRateChange.getStatus());
			tTempVo.putParam("RateCode", tLoanRateChange.getRateCode());
			tTempVo.putParam("ProdNo", tLoanRateChange.getProdNo());
			tTempVo.putParam("BaseRateCode", tLoanRateChange.getBaseRateCode());
			tTempVo.putParam("IncrFlag", tLoanRateChange.getIncrFlag());
			tTempVo.putParam("RateIncr", tLoanRateChange.getRateIncr());
			tTempVo.putParam("IndividualIncr", tLoanRateChange.getIndividualIncr());
			tTempVo.putParam("FitRate", tLoanRateChange.getFitRate());
			tTempVo.putParam("Remark", tLoanRateChange.getRemark());
			tTempVo.putParam("AcDate", tLoanRateChange.getAcDate());
			tTempVo.putParam("TellerNo", tLoanRateChange.getTellerNo());
			tTempVo.putParam("TxtNo", tLoanRateChange.getTxtNo());
		}
	}

	// 更新撥款主檔
	private void updLoanBorMainRoutine() throws LogicException {
		this.info("updLoanBorMainRoutine ...  ");
		// 定期機動更新下次利率調整日期
		Boolean changeAdjDate = false;

		beforeLoanBorMain = (LoanBorMain) datalog.clone(tLoanBorMain);
		if ("3".equals(tLoanBorMain.getRateCode()) && iNextRateAdjDate > 0) {
			tLoanBorMain.setNextAdjRateDate(iNextRateAdjDate);
			// 下次利率調整日變更需寫DATELOG
			this.info("tLoanBorMain.NextAdjRateDate = " + tLoanBorMain.getNextAdjRateDate());
			this.info("iNextRateAdjDate = " + iNextRateAdjDate);
			if (tLoanBorMain.getNextAdjRateDate() == iNextRateAdjDate + 19110000)
				changeAdjDate = true;
		}
		// 更新實際計息利率，重算期金
		tLoanBorMain = loanCom.updStoreRateAndDueAmt(tLoanBorMain, titaVo);

		tLoanBorMain.setLastBorxNo(wkBorxNo);
		tLoanBorMain.setActFg(titaVo.getActFgI());
		try

		{
			loanBorMainService.update2(tLoanBorMain, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007",
					"撥款主檔 戶號 = " + iCustNo + " 額度編號 = " + iFacmNo + " 撥款序號 = " + wkBormNo); // 更新資料時，發生錯誤
		}
		// 下次利率調整日變更需寫DATELOG
		if (changeAdjDate) {
			datalog.setEnv(titaVo, beforeLoanBorMain, tLoanBorMain);
			datalog.exec();
		}
	}

	// 檢核下次利率調整日期
	private void checkNextRateAdjDate() throws LogicException {
		// 下次利率調整日期應是大於本月的第一筆
		LoanRateChange t2 = loanRateChangeService.rateChangeEffectDateDescFirst(iCustNo, iFacmNo, wkBormNo,
				iNextRateAdjDate + 19110000 - 1, titaVo);
		if (t2 != null && t2.getEffectDate() / 100 > this.txBuffer.getTxBizDate().getTbsDy() / 100) {
			this.totaVo.setWarnMsg("戶號" + iCustNo + "-" + iFacmNo + "-" + wkBormNo + "，未生效利率變動日" + t2.getEffectDate()
					+ " < 下次利率調整日期" + tLoanBorMain.getNextAdjRateDate());
		}
	}

	private void InsLoanRateChangeRoutine() throws LogicException {
		this.info("InsLoanRateChangeRoutine ...");

		tLoanRateChangeId = new LoanRateChangeId();
		tLoanRateChangeId.setCustNo(iCustNo);
		tLoanRateChangeId.setFacmNo(iFacmNo);
		tLoanRateChangeId.setBormNo(wkBormNo);
		tLoanRateChangeId.setEffectDate(iEffectDate);
		tLoanRateChange = new LoanRateChange();
		tLoanRateChange.setLoanRateChangeId(tLoanRateChangeId);
		tLoanRateChange.setCustNo(iCustNo);
		tLoanRateChange.setFacmNo(iFacmNo);
		tLoanRateChange.setBormNo(wkBormNo);
		tLoanRateChange.setEffectDate(iEffectDate);
		tLoanRateChange.setStatus(0);
		tLoanRateChange.setRateCode(iRateCode);
		tLoanRateChange.setProdNo(iProdNo);
		tLoanRateChange.setBaseRateCode(iBaseRateCode);
		tLoanRateChange.setIncrFlag(iIncrFlag);
		tLoanRateChange.setRateIncr(iRateIncr);
		tLoanRateChange.setIndividualIncr(iIndividualIncr);
		tLoanRateChange.setFitRate(iFitRate);
		tLoanRateChange.setRemark(iRemark);
		tLoanRateChange.setAcDate(this.txBuffer.getTxCom().getTbsdy());
		tLoanRateChange.setTellerNo(titaVo.getTlrNo());
		tLoanRateChange.setTxtNo(titaVo.getTxtNo());
		try {
			loanRateChangeService.insert(tLoanRateChange, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "放款利率變動檔"); // 新增資料時，發生錯誤
		}
	}

	// 新增預調利率
	private void InsLoanRateChangeRoutine2() throws LogicException {
		LoanRateChangeId tLoanRateChangeId2 = new LoanRateChangeId();
		tLoanRateChangeId2.setCustNo(iCustNo);
		tLoanRateChangeId2.setFacmNo(iFacmNo);
		tLoanRateChangeId2.setBormNo(wkBormNo);
		tLoanRateChangeId2.setEffectDate(iNextRateAdjDate);
		LoanRateChange tLoanRateChange2 = new LoanRateChange();
		tLoanRateChange2.setLoanRateChangeId(tLoanRateChangeId2);
		tLoanRateChange2.setCustNo(iCustNo);
		tLoanRateChange2.setFacmNo(iFacmNo);
		tLoanRateChange2.setBormNo(wkBormNo);
		tLoanRateChange2.setEffectDate(iNextRateAdjDate);
		tLoanRateChange2.setStatus(0);
		tLoanRateChange2.setRateCode(iRateCode);
		tLoanRateChange2.setProdNo(iProdNo);
		tLoanRateChange2.setBaseRateCode(iBaseRateCode);
		tLoanRateChange2.setIncrFlag(iIncrFlag);
		tLoanRateChange2.setRateIncr(iRateIncr);
		tLoanRateChange2.setIndividualIncr(iIndividualIncr);
		tLoanRateChange2.setFitRate(iFitRate);
		tLoanRateChange2.setRemark("預調利率");
		tLoanRateChange2.setAcDate(this.txBuffer.getTxCom().getTbsdy());
		tLoanRateChange2.setTellerNo(titaVo.getTlrNo());
		tLoanRateChange2.setTxtNo(titaVo.getTxtNo());
		try {
			loanRateChangeService.insert(tLoanRateChange2, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "新增預調利率"); // 新增資料時，發生錯誤
		}
	}

	private void UpdLoanRateChangeRoutine() throws LogicException {
		this.info("UpdLoanRateChangeRoutine ...");

		beforeLoanRateChange = (LoanRateChange) datalog.clone(tLoanRateChange);

		tLoanRateChange.setRateCode(iRateCode);
		tLoanRateChange.setProdNo(iProdNo);
		tLoanRateChange.setBaseRateCode(iBaseRateCode);
		tLoanRateChange.setIncrFlag(iIncrFlag);
		tLoanRateChange.setRateIncr(iRateIncr);
		tLoanRateChange.setIndividualIncr(iIndividualIncr);
		tLoanRateChange.setFitRate(iFitRate);
		tLoanRateChange.setRemark(iRemark);
		tLoanRateChange.setAcDate(titaVo.getEntDyI());
		tLoanRateChange.setTellerNo(titaVo.getTlrNo());
		tLoanRateChange.setTxtNo(titaVo.getTxtNo());
		try {
			tLoanRateChange = loanRateChangeService.update2(tLoanRateChange, titaVo);
		} catch (DBException f) {
			throw new LogicException(titaVo, "E0007", "放款利率變動檔"); // 更新資料時，發生錯誤
		}
		datalog.setEnv(titaVo, beforeLoanRateChange, tLoanRateChange);
		datalog.exec();
	}

	// 刪除放款利率變動檔
	private void DelLoanRateChangeRoutine(int effectDate) throws LogicException {
		this.info("DelLoanRateChangeRoutine ...");

		tLoanRateChange = loanRateChangeService
				.holdById(new LoanRateChangeId(wkCustNo, wkFacmNo, wkBormNo, effectDate + 19110000), titaVo);
		if (tLoanRateChange == null) {
			throw new LogicException(titaVo, "E0006", "放款利率變動檔"); // 鎖定資料時，發生錯誤
		}
		try {

			datalog.setEnv(titaVo, tLoanRateChange, tLoanRateChange);
			datalog.exec("刪除放款利率變動檔");
			loanRateChangeService.delete(tLoanRateChange, titaVo);
		} catch (DBException f) {
			throw new LogicException(titaVo, "E0007", "放款利率變動檔"); // 更新資料時，發生錯誤
		}
	}

	// 刪除放款利率變動檔(預調利率)
	private void DelLoanRateChangeNextAdj(int effectDate) throws LogicException {
		this.info("DelLoanRateChangeRoutine2 ...");

		LoanRateChange tLoanRateChange2 = loanRateChangeService
				.holdById(new LoanRateChangeId(wkCustNo, wkFacmNo, wkBormNo, effectDate + 19110000), titaVo);
		if (tLoanRateChange2 != null) {
			try {
				loanRateChangeService.delete(tLoanRateChange2, titaVo);
			} catch (DBException f) {
				throw new LogicException(titaVo, "E0007", "放款利率變動檔預調利率"); // 更新資料時，發生錯誤
			}
		}
	}

	// 新增放款交易內容檔
	private void addLoanBorTxRoutine() throws LogicException {
		this.info("addLoanBorTxRoutine ... ");

		tLoanBorTx = new LoanBorTx();
		tLoanBorTxId = new LoanBorTxId();
		loanCom.setLoanBorTx(tLoanBorTx, tLoanBorTxId, iCustNo, iFacmNo, wkBormNo, wkBorxNo, titaVo);
		tLoanBorTx.setRate(iFitRate);
		tLoanBorTx.setTxDescCode("3721"); // 借戶利率變更"
		// 其他欄位
		tLoanBorTx.setOtherFields(tTempVo.getJsonString());
		try {
			loanBorTxService.insert(tLoanBorTx, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "放款交易內容檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
		}
	}

	// 還原撥款主檔
	private void RestoredLoanBorMainRoutine() throws LogicException {
		this.info("RestoredLoanBorMainRoutine ... " + wkBorxNo);

		tLoanBorMain = loanBorMainService.holdById(new LoanBorMainId(wkCustNo, wkFacmNo, wkBormNo), titaVo);
		if (tLoanBorMain == null) {
			throw new LogicException(titaVo, "E0006",
					"撥款主檔 戶號 = " + iCustNo + " 額度編號 = " + wkFacmNo + " 撥款序號 = " + wkBormNo); // 鎖定資料時，發生錯誤
		}
		if (wkBorxNo == tLoanBorMain.getLastBorxNo()) {
			tLoanBorMain.setLastBorxNo(wkBorxNo - 1);
		}
		tLoanBorMain.setNextAdjRateDate(this.parse.stringToInteger(tTempVo.get("NextAdjRateDate")));
		tLoanBorMain.setStoreRate(this.parse.stringToBigDecimal(tTempVo.get("StoreRate")));
		tLoanBorMain.setDueAmt(this.parse.stringToBigDecimal(tTempVo.get("DueAmt")));

		try {
			loanBorMainService.update(tLoanBorMain, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007",
					"撥款主檔 戶號 = " + wkCustNo + " 額度編號 = " + wkFacmNo + " 撥款序號 = " + wkBormNo); // 更新資料時，發生錯誤
		}
	}

	// 還原放款利率變動檔
	private void RestoredLoanRateChangeRoutine() throws LogicException {
		this.info("RestoredLoanRateChangeRoutine ... ");

		tLoanRateChange = loanRateChangeService
				.holdById(new LoanRateChangeId(wkCustNo, wkFacmNo, wkBormNo, wkEffectDate + 19110000), titaVo);
		if (tLoanRateChange == null) {
			throw new LogicException(titaVo, "E0006", "放款利率變動檔"); // 鎖定資料時，發生錯誤
		}
		tLoanRateChange.setRateCode(tTempVo.get("RateCode"));
		tLoanRateChange.setProdNo(tTempVo.get("ProdNo"));
		tLoanRateChange.setBaseRateCode(tTempVo.get("BaseRateCode"));
		tLoanRateChange.setIncrFlag(tTempVo.get("IncrFlag"));
		tLoanRateChange.setRateIncr(this.parse.stringToBigDecimal(tTempVo.get("RateIncr")));
		tLoanRateChange.setIndividualIncr(this.parse.stringToBigDecimal(tTempVo.get("IndividualIncr")));
		tLoanRateChange.setFitRate(this.parse.stringToBigDecimal(tTempVo.get("FitRate")));
		tLoanRateChange.setRemark(tTempVo.get("Remark"));
		tLoanRateChange.setAcDate(this.parse.stringToInteger(tTempVo.get("AcDate")));
		tLoanRateChange.setTellerNo(tTempVo.get("TellerNo"));
		tLoanRateChange.setTxtNo(tTempVo.get("TxtNo"));
		try {
			loanRateChangeService.update(tLoanRateChange, titaVo);
		} catch (DBException f) {
			throw new LogicException(titaVo, "E0007", "放款利率變動檔"); // 更新資料時，發生錯誤
		}
	}

	private void ReleaseLoanBorMainRoutine() throws LogicException {
		tLoanBorMain = loanBorMainService.holdById(new LoanBorMainId(wkCustNo, wkFacmNo, wkBormNo));
		if (tLoanBorMain == null) {
			throw new LogicException(titaVo, "E0006", "撥款主檔"); // 鎖定資料時，發生錯誤
		}
		// 放行 一般
		if (titaVo.isHcodeNormal()) {
			// 更新撥款主檔

			if (tLoanBorMain.getLastBorxNo() == wkBorxNo) {
				tLoanBorMain.setActFg(titaVo.getActFgI());
			}
		}
		// 放行訂正
		if (titaVo.isHcodeErase()) {
			tLoanBorMain.setActFg(1);
		}
		try {
			tLoanBorMain = loanBorMainService.update2(tLoanBorMain, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0008",
					"撥款主檔 戶號 = " + iCustNo + "額度編號 = " + iFacmNo + "撥款序號 = " + wkBormNo + e.getErrorMsg()); // 新增資料時，發生錯誤
		}
	}

	private void EntryEraseRoutine() throws LogicException {
		this.info("EntryModifyRoutine ... ");
		Slice<LoanBorTx> slLoanBorTx = loanBorTxService.custNoTxtNoEq(iCustNo, titaVo.getOrgEntdyI() + 19110000,
				titaVo.getOrgKin(), titaVo.getOrgTlr(), titaVo.getOrgTno(), 0, Integer.MAX_VALUE, titaVo);
		if (slLoanBorTx == null) {
			throw new LogicException(titaVo, "E0001", "交易明細檔 分行別 = " + titaVo.getOrgKin() + " 交易員代號 = "
					+ titaVo.getOrgTlr() + " 交易序號 = " + titaVo.getOrgTno()); // 查詢資料不存在
		}
		for (LoanBorTx tx : slLoanBorTx.getContent()) {
			wkCustNo = tx.getCustNo();
			wkFacmNo = tx.getFacmNo();
			wkBormNo = tx.getBormNo();
			wkBorxNo = tx.getBorxNo();
			tTempVo = tTempVo.getVo(tx.getOtherFields());
			wkInsertFlag = tTempVo.getParam("InsertFlag");
			wkEffectDate = this.parse.stringToInteger(tTempVo.getParam("EffectDate"));
			wkNextRateAdjDate = this.parse.stringToInteger(tTempVo.getParam("NextRateAdjDate"));
			this.info("   wkCustNo = " + wkCustNo);
			this.info("   wkFacmNo = " + wkFacmNo);
			this.info("   wkBormNo = " + wkBormNo);
			this.info("   wkBorxNo = " + wkBorxNo);
			this.info("   wkEffectDate = " + wkEffectDate);
			this.info("   wkInsertFlag = " + wkInsertFlag);
			// 還原撥款主檔
			RestoredLoanBorMainRoutine();
			// 刪除放款利率變動檔
			if (wkInsertFlag.equals("Y")) {
				DelLoanRateChangeRoutine(wkEffectDate);
			} else {
				RestoredLoanRateChangeRoutine();
			}
			// 刪除放款利率變動檔(預調利率)
			if (wkNextRateAdjDate > 0) {
				DelLoanRateChangeNextAdj(wkNextRateAdjDate);
			}
			// 刪除原交易內容檔
			if (titaVo.isHcodeModify()) {
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
}