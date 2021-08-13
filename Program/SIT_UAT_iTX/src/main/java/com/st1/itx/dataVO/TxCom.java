package com.st1.itx.dataVO;

import com.st1.itx.db.domain.TxBizDate;

public class TxCom {

	/* 流程控制序號 */
	private int FlowEntday;

	/* 流程控制序號 */
	private String FlowNo;

	/* 審核單位 */
	private String ConfirmBrNo;

	/* 審核科組別 */
	private String ConfirmGroupNo;

	/* 可訂正記號 */
	private int CanCancel;

	/* 可修改記號 */
	private int CanModify;

	/* 登錄需提交記號 */
	private int SubmitFg;

	/* IT-TXCOM-FLAGAR */
	/* 本次日控制(''-本次日,'B'-夜間批次,'N'-次日) */
	private String dateCtl = "";

	/* 登放序號 */
	private String relKin = "";

	private String relTlr = "";

	private int relTno = 0;

	/* 完整交易序號 */
	private String relNo = "";

	/* 交易類別 (0.查詢類別 1.更新類別 2.特殊類別) */
	private int txType = 0;

	// 本／次日帳務記號
	private boolean todayAcc = true;

	// 基礎幣
	private int bCurcd = 0;

	// 櫃員資料
	private int tlrLevel = 0;

	// 櫃員權限群組
	private String AuthNo = "";

	private int conFirm = 0;

	private String tlrDept = "0";

	/* 本／次日帳務記號 0. 本日帳 1. 次日帳 */
	private int accDyFg = 0;

	/*
	 * -----------------------------------------------------------------------------
	 * ----------------------------
	 */

	/*
	 * -----------------------------------------------------------------------------
	 * ----------------------------
	 */
	/* IT-TXCOM-ENTACAR */
	// 會計分錄登錄 SET BY APCTL(RELCD & ACTFG )
	private int bookAc = 0;

	// 帳務用HCode
	private int bookAcHcode = 0;

	// 帳務筆數
	private int AcCnt = 0;

	/*
	 * -----------------------------------------------------------------------------
	 * ----------------------------
	 */
	/* Date Area */
	// 本營業日
	private int tbsdy = 0;

	// 下營業日
	private int nbsdy = 0;

	// 下下營業日
	private int nnbsdy = 0;

	// 上營業日
	private int lbsdy = 0;

	// 上個月底日
	private int lmndy = 0;

	// 本月月底日
	private int tmndy = 0;

	// 本月月底營業日
	private int mfbsdy = 0;

	// 本營業日西元
	private int tbsdyf = 0;

	// 下營業日西元
	private int nbsdyf = 0;

	// 下下營業日西元
	private int nnbsdyf = 0;

	// 上營業日西元
	private int lbsdyf = 0;

	// 上個月底日西元
	private int lmndyf = 0;

	// 本月月底日西元
	private int tmndyf = 0;

	// 本月月底營業日西元
	private int mfbsdyf = 0;

	/*
	 * -----------------------------------------------------------------------------
	 * ----------------------------
	 */
	// 交易登放序號日期
	private int reldy = 0;

	// 起始登錄序號
	private int orgEntdy = 0;

	private String orgTxSeq = "";

	private String orgKin = "";
	private String orgTlr = "";
	private String orgTno = "";

	private int scanCd = 0;

	// 起始登錄、序號
	private String oTxSeq = "";

	// 更正時原始交易日期，國曆
	private int orgCalDy = 0;

	// 更正時原始交易時間
	private int orgCalTm = 0;

	/*
	 * -----------------------------------------------------------------------------
	 * ----------------------------
	 */

	private int txRsut = 0;

	private String msgId = "";

	private String errorMsg = "";

	/*
	 * -----------------------------------------------------------------------------
	 * ----------------------------
	 */
	/* 交易屬性 */
	// 更正屬性
	private int txHcode = 0;

	// 修改屬性
	private int txModifyFg = 0;

	public TxCom(TxBizDate txBizDate) {
		this.setTbsdy(txBizDate.getTbsDy());
		this.setNbsdy(txBizDate.getNbsDy());
		this.setNnbsdy(txBizDate.getNnbsDy());
		this.setLbsdy(txBizDate.getLbsDy());
		this.setLmndy(txBizDate.getLmnDy());
		this.setTmndy(txBizDate.getTmnDy());
		this.setMfbsdy(txBizDate.getMfbsDy());
		this.setTbsdyf(txBizDate.getTbsDyf());
		this.setNbsdyf(txBizDate.getNbsDyf());
		this.setNnbsdyf(txBizDate.getNnbsDyf());
		this.setLbsdyf(txBizDate.getLbsDyf());
		this.setLmndyf(txBizDate.getLmnDyf());
		this.setTmndyf(txBizDate.getTmnDyf());
		this.setMfbsdyf(txBizDate.getMfbsDyf());
	}

	/**
	 * if Date is Next Day return true
	 * 
	 * @return boolean true or false
	 */
	public boolean isNextDay() {
		return this.getDateCtl().equals("N");
	}

	/**
	 * if In Batch Mode return true
	 * 
	 * @return boolean true or false
	 */
	public boolean isNightBatch() {
		return this.getDateCtl().equals("B");
	}

	/**
	 * 流程控制序號
	 * 
	 * @return String 流程控制序號
	 */
	public String getFlowNo() {
		return FlowNo;
	}

	/**
	 * 可訂正記號
	 * 
	 * @return int 可訂正記號
	 */
	public int getCanCancel() {
		return CanCancel;
	}

	/**
	 * 可修改記號
	 * 
	 * @return int 可修改記號
	 */
	public int getCanModify() {
		return CanModify;
	}

	/**
	 * 流程控制序號
	 * 
	 * @param flowno 流程控制序號
	 */
	public void setFlowNo(String flowno) {
		this.FlowNo = flowno;
	}

	/**
	 * 可訂正記號
	 * 
	 * @param cancancel 可訂正記號
	 */
	public void setCanCancel(int cancancel) {
		this.CanCancel = cancancel;
	}

	/**
	 * 可修改記號
	 * 
	 * @param canmodify 可修改記號
	 */
	public void setCanModify(int canmodify) {
		this.CanModify = canmodify;
	}

	/**
	 * 本次日控制
	 * 
	 * @return String ' '-本次日,'B'-夜間批次,'N'-次日
	 */
	public String getDateCtl() {
		return dateCtl;
	}

	/**
	 * 本次日控制
	 * 
	 * @param dateCtl ' '-本次日,'B'-夜間批次,'N'-次日
	 */
	public void setDateCtl(String dateCtl) {
		this.dateCtl = dateCtl;
	}

	/**
	 * 登放序號<br>
	 * 行別
	 * 
	 * @return String 行別
	 */
	public String getRelKin() {
		return relKin;
	}

	/**
	 * 登放序號
	 * 
	 * @param relKin 行別
	 */
	public void setRelKin(String relKin) {
		this.relKin = relKin;
	}

	/**
	 * 登放序號<br>
	 * 櫃員邊鰾
	 * 
	 * @return String 櫃員編號
	 */
	public String getRelTlr() {
		return relTlr;
	}

	/**
	 * 登放序號<br>
	 * 櫃員編號
	 * 
	 * @param relTlr 櫃員編號
	 */
	public void setRelTlr(String relTlr) {
		this.relTlr = relTlr;
	}

	/**
	 * Auto Tlrno
	 * 
	 * @return boolen tlrno endWith C, B, X true
	 */
	public boolean isRelAuto() {
		return this.getRelTlr().endsWith("C") || this.getRelTlr().endsWith("B") || this.getRelTlr().endsWith("X");
	}

	/**
	 * 登放序號<br>
	 * 序號
	 * 
	 * @return Integer 序號
	 */
	public int getRelTno() {
		return relTno;
	}

	/**
	 * 登放序號<br>
	 * 序號
	 * 
	 * @param relTno 序號
	 */
	public void setRelTno(int relTno) {
		this.relTno = relTno;
	}

	/**
	 * 登放序號<br>
	 * kinbr + tlrno + txtno
	 * 
	 * @return String kinbr + tlrno + txtno
	 */
	public String getRelNo() {
		return relNo;
	}

	/**
	 * 登放序號<br>
	 * kinbr + tlrno + txtno
	 * 
	 * @param relNo kinbr + tlrno + txtno
	 */
	public void setRelNo(String relNo) {
		this.relNo = relNo;
	}

	/**
	 * 交易類別<br>
	 * 0.查詢類別 1.更新類別 2.特殊類別
	 * 
	 * @return Integer
	 */
	public int getTxType() {
		return txType;
	}

	/**
	 * 交易類跌
	 * 
	 * @param txType 0.查詢類別 1.更新類別 2.特殊類別
	 */
	public void setTxType(int txType) {
		this.txType = txType;
	}

	/**
	 * is INQ return true
	 * 
	 * @return boolean true or false
	 */
	public boolean isTxTypeInq() {
		return this.getTxType() == 0;
	}

	/**
	 * is UPD return true
	 * 
	 * @return boolean true or false
	 */
	public boolean isTxTypeUpd() {
		return this.getTxType() == 1;
	}

	/**
	 * 本位幣 BCURCD : 依 Tita OBUFG 決定
	 * 
	 * @return Integer
	 */
	public int getbCurcd() {
		return bCurcd;
	}

	/**
	 * 本位幣 BCURCD : 依 Tita OBUFG 決定
	 * 
	 * @param bCurcd 本位幣
	 */
	public void setbCurcd(int bCurcd) {
		this.bCurcd = bCurcd;
	}

	/**
	 * 櫃員等級
	 * 
	 * @return Integer sup = 1,2 tlr = 3,4
	 */
	public int getTlrLevel() {
		return tlrLevel;
	}

	/**
	 * 櫃員等級
	 * 
	 * @param tlrLevel sup = 1,2 tlr = 3,4
	 */
	public void setTlrLevel(int tlrLevel) {
		this.tlrLevel = tlrLevel;
	}

	public int getConFirm() {
		return conFirm;
	}

	public void setConFirm(int conFirm) {
		this.conFirm = conFirm;
	}

	/**
	 * 櫃員Dept
	 * 
	 * @return Integer
	 */
	public String getTlrDept() {
		return tlrDept;
	}

	/**
	 * 櫃員Dept
	 * 
	 * @param tlrDept Integer
	 */
	public void setTlrDept(String tlrDept) {
		this.tlrDept = tlrDept;
	}

	/**
	 * 本／次日帳務記號<br>
	 * 0. 本日帳 1. 次日帳
	 * 
	 * @return Integer
	 */
	public int getAccDyFg() {
		return accDyFg;
	}

	/**
	 * 本／次日帳務記號
	 * 
	 * @param accDyFg 0. 本日帳 1. 次日帳
	 */
	public void setAccDyFg(int accDyFg) {
		this.accDyFg = accDyFg;
	}

	/**
	 * 三段式(1:代理登錄 2:指定審核) 3:登錄 0:更正、放行
	 * 
	 * @return Integer 1:代理登錄 2:指定審核 3:登錄 0:更正、放行
	 */
	public int getBookAc() {
		return bookAc;
	}

	/**
	 * 出帳記號<br>
	 * 一段式 0:登錄 2:訂正<br>
	 * 二段式 1:登錄 2:訂正、放行
	 * 
	 * @param bookAc 0,1:登錄 2:更正、放行
	 */
	public void setBookAc(int bookAc) {
		this.bookAc = bookAc;
	}

	/**
	 * 一段式 0:登錄 2:訂正<br>
	 * 二段式 1:登錄 2:訂正、放行
	 * 
	 * @return boolean if BookAc == 0 || 1 true
	 */
	public boolean isBookAcYes() {
		return this.getBookAc() == 0 || this.getBookAc() == 1;
	}

	/**
	 * 帳務用HCode 判斷本日次日修正
	 * 
	 * @return Integer 0.正常 1.當日訂正 2.隔日訂正
	 * 
	 */
	public int getBookAcHcode() {
		return bookAcHcode;
	}

	/**
	 * 帳務用HCode 判斷本日次日修正
	 * 
	 * @param bookAcHcode 0.正常 1.當日訂正 2.隔日訂正
	 */
	public void setBookAcHcode(int bookAcHcode) {
		this.bookAcHcode = bookAcHcode;
	}

	/**
	 * @return the acCnt
	 */
	public int getAcCnt() {
		return AcCnt;
	}

	/**
	 * @param acCnt the acCnt to set
	 */
	public void setAcCnt(int acCnt) {
		AcCnt = acCnt;
	}

	/**
	 * 本營業日(國曆)
	 * 
	 * @return Integer 本營業日(國曆)
	 */
	public int getTbsdy() {
		return tbsdy;
	}

	/**
	 * 本營業日(國曆)
	 * 
	 * @param tbsdy 本營業日(國曆)
	 */
	public void setTbsdy(int tbsdy) {
		this.tbsdy = tbsdy;
	}

	/**
	 * 下營業日(國曆)
	 * 
	 * @return Integer 下營業日(國曆)
	 */
	public int getNbsdy() {
		return nbsdy;
	}

	/**
	 * 下營業日(國曆)
	 * 
	 * @param nbsdy 下營業日(國曆)
	 */
	public void setNbsdy(int nbsdy) {
		this.nbsdy = nbsdy;
	}

	/**
	 * 下下營業日(國曆)
	 * 
	 * @return Integer 下下營業日(國曆)
	 */
	public int getNnbsdy() {
		return nnbsdy;
	}

	/**
	 * 下下營業日(國曆)
	 * 
	 * @param nnbsdy 下下營業日(國曆)
	 */
	public void setNnbsdy(int nnbsdy) {
		this.nnbsdy = nnbsdy;
	}

	/**
	 * 上營業日(國曆)
	 * 
	 * @return Integer 上營業日(國曆)
	 */
	public int getLbsdy() {
		return lbsdy;
	}

	/**
	 * 上營業日(國曆)
	 * 
	 * @param lbsdy 上營業日(國曆)
	 */
	public void setLbsdy(int lbsdy) {
		this.lbsdy = lbsdy;
	}

	/**
	 * 上月月底日(國曆)
	 * 
	 * @return Integer 上月月底日(國曆)
	 */
	public int getLmndy() {
		return lmndy;
	}

	/**
	 * 上月月底日(國曆)
	 * 
	 * @param lmndy 上月月底日(國曆)
	 */
	public void setLmndy(int lmndy) {
		this.lmndy = lmndy;
	}

	/**
	 * 本月月底日(國曆)
	 * 
	 * @return Integer 本月月底日(國曆)
	 */
	public int getTmndy() {
		return tmndy;
	}

	/**
	 * 本月月底日(國曆)
	 * 
	 * @param tmndy 本月月底日(國曆)
	 */
	public void setTmndy(int tmndy) {
		this.tmndy = tmndy;
	}

	/**
	 * 本月月底營業日(國曆)
	 * 
	 * @return Integer 本月月底營業日(國曆)
	 */
	public int getMfbsdy() {
		return mfbsdy;
	}

	/**
	 * 本月月底營業日(國曆)
	 * 
	 * @param mfbsdy 本月月底營業日(國曆)
	 */
	public void setMfbsdy(int mfbsdy) {
		this.mfbsdy = mfbsdy;
	}

	/**
	 * 本營業日(西曆)
	 * 
	 * @return Integer 本營業日(西曆)
	 */
	public int getTbsdyf() {
		return tbsdyf;
	}

	/**
	 * 本營業日(西曆)
	 * 
	 * @param tbsdyf 本營業日(西曆)
	 */
	public void setTbsdyf(int tbsdyf) {
		this.tbsdyf = tbsdyf;
	}

	/**
	 * 下營業日(西曆)
	 * 
	 * @return Integer 下營業日(西曆)
	 */
	public int getNbsdyf() {
		return nbsdyf;
	}

	/**
	 * 下營業日(西曆)
	 * 
	 * @param nbsdyf 下營業日(西曆)
	 */
	public void setNbsdyf(int nbsdyf) {
		this.nbsdyf = nbsdyf;
	}

	/**
	 * 下下營業日(西曆)
	 * 
	 * @return Integer 下下營業日(西曆)
	 */
	public int getNnbsdyf() {
		return nnbsdyf;
	}

	/**
	 * 下下營業日(西曆)
	 * 
	 * @param nnbsdyf 下下營業日(西曆)
	 */
	public void setNnbsdyf(int nnbsdyf) {
		this.nnbsdyf = nnbsdyf;
	}

	/**
	 * 上營業日(西曆)
	 * 
	 * @return Integer 上營業日(西曆)
	 */
	public int getLbsdyf() {
		return lbsdyf;
	}

	/**
	 * 上營業日(西曆)
	 * 
	 * @param lbsdyf 上營業日(西曆)
	 */
	public void setLbsdyf(int lbsdyf) {
		this.lbsdyf = lbsdyf;
	}

	/**
	 * 上月月底日(西曆)
	 * 
	 * @return Integer 上月月底日(西曆)
	 */
	public int getLmndyf() {
		return lmndyf;
	}

	/**
	 * 上月月底日(西曆)
	 * 
	 * @param lmndyf 上月月底日(西曆)
	 */
	public void setLmndyf(int lmndyf) {
		this.lmndyf = lmndyf;
	}

	/**
	 * 本月月底日(西曆)
	 * 
	 * @return Integer 本月月底日(西曆)
	 */
	public int getTmndyf() {
		return tmndyf;
	}

	/**
	 * 本月月底日(西曆)
	 * 
	 * @param tmndyf 本月月底日(西曆)
	 */
	public void setTmndyf(int tmndyf) {
		this.tmndyf = tmndyf;
	}

	/**
	 * 本月月底營業日(西曆)
	 * 
	 * @return Integer 本月月底營業日(西曆)
	 */
	public int getMfbsdyf() {
		return mfbsdyf;
	}

	/**
	 * 本月月底營業日(西曆)
	 * 
	 * @param mfbsdyf 本月月底營業日(西曆)
	 */
	public void setMfbsdyf(int mfbsdyf) {
		this.mfbsdyf = mfbsdyf;
	}

	/**
	 * 交易登放序號日期
	 * 
	 * @return Integer 交易登放序號日期
	 */
	public int getReldy() {
		return reldy;
	}

	/**
	 * 交易登放序號日期
	 * 
	 * @param reldy 交易登放序號日期
	 */
	public void setReldy(int reldy) {
		this.reldy = reldy;
	}

	/**
	 * 起始登錄序號-日期
	 * 
	 * @return Integer 起始登錄序號-日期
	 */
	public int getOrgEntdy() {
		return orgEntdy;
	}

	/**
	 * 起始登錄序號-日期
	 * 
	 * @param orgEntdy 起始登錄序號-日期
	 */
	public void setOrgEntdy(int orgEntdy) {
		this.orgEntdy = orgEntdy;
	}

	/**
	 * 起始登錄序號-全14碼
	 * 
	 * @return String 起始登錄序號-全14碼
	 */
	public String getOrgTxSeq() {
		return orgTxSeq;
	}

	/**
	 * 起始登錄序號-全14碼
	 * 
	 * @param orgTxSeq 起始登錄序號-全14碼
	 */
	public void setOrgTxSeq(String orgTxSeq) {
		this.orgTxSeq = orgTxSeq;
	}

	/**
	 * 起始登錄序號-分行
	 * 
	 * @return Integer 起始登錄序號-分行
	 */
	public String getOrgKin() {
		return orgKin;
	}

	/**
	 * 起始登錄序號-分行
	 * 
	 * @param orgKin 起始登錄序號-分行
	 */
	public void setOrgKin(String orgKin) {
		this.orgKin = orgKin;
	}

	/**
	 * 起始登錄序號-櫃員
	 * 
	 * @return Integer 起始登錄序號-分行
	 */
	public String getOrgTlr() {
		return orgTlr;
	}

	/**
	 * 起始登錄序號-櫃員
	 * 
	 * @param orgTlr 起始登錄序號-櫃員
	 */
	public void setOrgTlr(String orgTlr) {
		this.orgTlr = orgTlr;
	}

	/**
	 * 起始登錄序號-序號
	 * 
	 * @return String 起始登錄序號-序號
	 */
	public String getOrgTno() {
		return orgTno;
	}

	/**
	 * 起始登錄序號-序號
	 * 
	 * @param orgTno 起始登錄序號-序號
	 */
	public void setOrgTno(String orgTno) {
		this.orgTno = orgTno;
	}

	/**
	 * 過帳記號0-連線1-整批2-CMD CALL <br>
	 * 3-轉換不過帳4-更正後重新過帳
	 * 
	 * @return Integer 0,1,2,3,4
	 */
	public int getScanCd() {
		return scanCd;
	}

	/**
	 * 過帳記號0-連線1-整批2-CMD CALL <br>
	 * 3-轉換不過帳4-更正後重新過帳
	 * 
	 * @param scanCd 過帳記號
	 */
	public void setScanCd(int scanCd) {
		this.scanCd = scanCd;
	}

	/**
	 * 過帳記號
	 * 
	 * @return Boolean If ScanCd is OnLine return true
	 */
	public boolean isScanCdOnLine() {
		return this.getScanCd() == 0;
	}

	/**
	 * 過帳記號
	 * 
	 * @return Boolean If ScanCd is Batch return true
	 */
	public boolean isScanCdBatch() {
		return this.getScanCd() == 1;
	}

	/**
	 * 過帳記號
	 * 
	 * @return Boolean If ScanCd is CmdCall return true
	 */
	public boolean isScanCdCmdCall() {
		return this.getScanCd() == 2;
	}

	/**
	 * 過帳記號
	 * 
	 * @return Boolean If ScanCd is CvSkip return true
	 */
	public boolean isScanCdCvSkip() {
		return this.getScanCd() == 3;
	}

	/**
	 * 過帳記號
	 * 
	 * @return Boolean If ScanCd is ReenTry return true
	 */
	public boolean isScanCdReenTry() {
		return this.getScanCd() == 4;
	}

	/**
	 * 過帳記號
	 * 
	 * @return Boolean If ScanCd is Acen return true
	 */
	public boolean isScanCdAcen() {
		return this.getScanCd() == 0 || this.getScanCd() == 1 || this.getScanCd() == 2 || this.getScanCd() == 3 || this.getScanCd() == 4;
	}

	/**
	 * 起始登錄、審核序號 FOR XCXRES USE
	 * 
	 * @return String 起始登錄、審核序號 FOR XCXRES USE
	 */
	public String getOtxSeq() {
		return oTxSeq;
	}

	/**
	 * 起始登錄、審核序號 FOR XCXRES USE
	 * 
	 * @param oTxSeq 起始登錄、審核序號 FOR XCXRES USE
	 */
	public void setOtxSeq(String oTxSeq) {
		this.oTxSeq = oTxSeq;
	}

	/**
	 * 
	 * @return Integer 更正時原始交易日期，國曆
	 */
	public int getOrgCalDy() {
		return orgCalDy;
	}

	/**
	 * 
	 * @param orgCalDy 更正時原始交易日期，國曆
	 */
	public void setOrgCalDy(int orgCalDy) {
		this.orgCalDy = orgCalDy;
	}

	/**
	 * 
	 * @return Integer 更正時原始交易時間
	 */
	public int getOrgCalTm() {
		return orgCalTm;
	}

	/**
	 * 
	 * @param orgCalTm 更正時原始交易時間
	 */
	public void setOrgCalTm(int orgCalTm) {
		this.orgCalTm = orgCalTm;
	}

	public int getTxRsut() {
		return txRsut;
	}

	public void setTxRsut(int txRsut) {
		this.txRsut = txRsut;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	/**
	 * 更正屬性
	 * 
	 * @return Integer 0:禁止,1:允許
	 */
	public int getTxHcode() {
		return txHcode;
	}

	/**
	 * 更正屬性
	 * 
	 * @param txHcode 0:禁止,1:允許
	 */
	public void setTxHcode(int txHcode) {
		this.txHcode = txHcode;
	}

	/**
	 * 修改屬性<br>
	 * 0:禁止,1:允許
	 * 
	 * @return Integer 0:禁止,1:允許
	 */
	public int getTxModifyFg() {
		return txModifyFg;
	}

	/**
	 * 修改屬性
	 * 
	 * @param txModifyFg 0:禁止,1:允許
	 */
	public void setTxModifyFg(int txModifyFg) {
		this.txModifyFg = txModifyFg;
	}

	public int getFlowEntday() {
		return FlowEntday;
	}

	public void setFlowEntday(int flowEntday) {
		FlowEntday = flowEntday;
	}

	public String getConfirmBrNo() {
		return ConfirmBrNo;
	}

	/**
	 * @param confirmBrNo the confirmBrNo to set
	 */
	public void setConfirmBrNo(String confirmBrNo) {
		ConfirmBrNo = confirmBrNo;
	}

	/**
	 * @return the confirmGroupNo
	 */
	public String getConfirmGroupNo() {
		return ConfirmGroupNo;
	}

	/**
	 * @param confirmGroupNo the confirmGroupNo to set
	 */
	public void setConfirmGroupNo(String confirmGroupNo) {
		ConfirmGroupNo = confirmGroupNo;
	}

	public boolean isTodayAcc() {
		return todayAcc;
	}

	public void setTodayAcc(boolean todayAcc) {
		this.todayAcc = todayAcc;
	}

	public String getoTxSeq() {
		return oTxSeq;
	}

	public void setoTxSeq(String oTxSeq) {
		this.oTxSeq = oTxSeq;
	}

	public String getAuthNo() {
		return AuthNo;
	}

	/**
	 * @param authNo the authNo to set
	 */
	public void setAuthNo(String authNo) {
		AuthNo = authNo;
	}

	/**
	 * @return the submitFg
	 */
	public int getSubmitFg() {
		return SubmitFg;
	}

	/**
	 * @param submitFg the submitFg to set
	 */
	public void setSubmitFg(int submitFg) {
		SubmitFg = submitFg;
	}

	@Override
	public String toString() {
		return "TxCom [FlowEntday=" + FlowEntday + ", FlowNo=" + FlowNo + ", ConfirmBrNo=" + ConfirmBrNo + ", ConfirmGroupNo=" + ConfirmGroupNo + ", CanCancel=" + CanCancel + ", CanModify="
				+ CanModify + ", SubmitFg=" + SubmitFg + ", dateCtl=" + dateCtl + ", relKin=" + relKin + ", relTlr=" + relTlr + ", relTno=" + relTno + ", relNo=" + relNo + ", txType=" + txType
				+ ", todayAcc=" + todayAcc + ", bCurcd=" + bCurcd + ", tlrLevel=" + tlrLevel + ", AuthNo=" + AuthNo + ", conFirm=" + conFirm + ", tlrDept=" + tlrDept + ", accDyFg=" + accDyFg
				+ ", bookAc=" + bookAc + ", bookAcHcode=" + bookAcHcode + ", AcCnt=" + AcCnt + ", tbsdy=" + tbsdy + ", nbsdy=" + nbsdy + ", nnbsdy=" + nnbsdy + ", lbsdy=" + lbsdy + ", lmndy=" + lmndy
				+ ", tmndy=" + tmndy + ", mfbsdy=" + mfbsdy + ", tbsdyf=" + tbsdyf + ", nbsdyf=" + nbsdyf + ", nnbsdyf=" + nnbsdyf + ", lbsdyf=" + lbsdyf + ", lmndyf=" + lmndyf + ", tmndyf=" + tmndyf
				+ ", mfbsdyf=" + mfbsdyf + ", reldy=" + reldy + ", orgEntdy=" + orgEntdy + ", orgTxSeq=" + orgTxSeq + ", orgKin=" + orgKin + ", orgTlr=" + orgTlr + ", orgTno=" + orgTno + ", scanCd="
				+ scanCd + ", oTxSeq=" + oTxSeq + ", orgCalDy=" + orgCalDy + ", orgCalTm=" + orgCalTm + ", txRsut=" + txRsut + ", msgId=" + msgId + ", errorMsg=" + errorMsg + ", txHcode=" + txHcode
				+ ", txModifyFg=" + txModifyFg + "]";
	}

}
