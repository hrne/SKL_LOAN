package com.st1.itx.util.common.data;

import java.math.BigDecimal;

import javax.persistence.Entity;

@Entity
public class L5060Vo {

	// 戶號
	private int custNo = 0;

	// 額度
	private int facmNo = 0;

	// 作業日期
	private int txDate = 0;

	// 作業項目
	private String txCode;

	// 提醒日期
	private int remindDate = 0;

	// 繳息迄日
	private int prevIntDate = 0;

	// 逾期期數
	private int ovduTerm = 0;

	// 逾期天數
	private int ovduDays = 0;

	// 幣別
	private String currencyCode;

	// 本金餘額
	private BigDecimal prinBalance = new BigDecimal("0");

	// 催收員
	private String accCollPsn;

	// 法務人員
	private String legalPsn;

	// 催收員姓名
	private String accCollPsnName;

	// 法務人員姓名
	private String legalPsnName;

	// 戶況
	/*
	 * 01: 逾期/催收戶02: 正常/呆帳戶-全部03: 正常/呆帳戶-正常戶04: 正常/呆帳戶-催收戶05: 正常/呆帳戶-結案戶06:
	 * 正常/呆帳戶-逾期戶07: 正常/呆帳戶-催收結案戶08: 正常/呆帳戶-部分轉呆戶 09: 正常/呆帳戶-呆帳戶10: 正常/呆帳戶-呆帳結案戶11:
	 * 正常/呆帳戶-債權轉讓戶
	 */
	private int status = 0;

	// 同擔保品戶號
	private int clCustNo = 0;

	// 同擔保品額度
	private int clFacmNo = 0;

	// 同擔保品序列號
	/* 同擔保品逾期天數最久者為1,其餘依序排列 */
	private int clRowNo = 0;

	// 是否指定
	private String isSpecify;

	public L5060Vo(int custNo, int facmNo, int txDate, String txCode, int remindDate, int prevIntDate, int ovduTerm, int ovduDays, String currencyCode, BigDecimal prinBalance, String accCollPsn,
			String legalPsn, String accCollPsnName, String legalPsnName, int status, int clCustNo, int clFacmNo, int clRowNo, String isSpecify) {
		super();
		this.custNo = custNo;
		this.facmNo = facmNo;
		this.txDate = txDate;
		this.txCode = txCode;
		this.remindDate = remindDate;
		this.prevIntDate = prevIntDate;
		this.ovduTerm = ovduTerm;
		this.ovduDays = ovduDays;
		this.currencyCode = currencyCode;
		this.prinBalance = prinBalance;
		this.accCollPsn = accCollPsn;
		this.legalPsn = legalPsn;
		this.accCollPsnName = accCollPsnName;
		this.legalPsnName = legalPsnName;
		this.status = status;
		this.clCustNo = clCustNo;
		this.clFacmNo = clFacmNo;
		this.clRowNo = clRowNo;
		this.isSpecify = isSpecify;
	}

	public int getCustNo() {
		return custNo;
	}

	public void setCustNo(int custNo) {
		this.custNo = custNo;
	}

	public int getFacmNo() {
		return facmNo;
	}

	public void setFacmNo(int facmNo) {
		this.facmNo = facmNo;
	}

	public int getTxDate() {
		return txDate;
	}

	public void setTxDate(int txDate) {
		this.txDate = txDate;
	}

	public String getTxCode() {
		return txCode;
	}

	public void setTxCode(String txCode) {
		this.txCode = txCode;
	}

	public int getRemindDate() {
		return remindDate;
	}

	public void setRemindDate(int remindDate) {
		this.remindDate = remindDate;
	}

	public int getPrevIntDate() {
		return prevIntDate;
	}

	public void setPrevIntDate(int prevIntDate) {
		this.prevIntDate = prevIntDate;
	}

	public int getOvduTerm() {
		return ovduTerm;
	}

	public void setOvduTerm(int ovduTerm) {
		this.ovduTerm = ovduTerm;
	}

	public int getOvduDays() {
		return ovduDays;
	}

	public void setOvduDays(int ovduDays) {
		this.ovduDays = ovduDays;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public BigDecimal getPrinBalance() {
		return prinBalance;
	}

	public void setPrinBalance(BigDecimal prinBalance) {
		this.prinBalance = prinBalance;
	}

	public String getAccCollPsn() {
		return accCollPsn;
	}

	public void setAccCollPsn(String accCollPsn) {
		this.accCollPsn = accCollPsn;
	}

	public String getLegalPsn() {
		return legalPsn;
	}

	public void setLegalPsn(String legalPsn) {
		this.legalPsn = legalPsn;
	}

	public String getAccCollPsnName() {
		return accCollPsnName;
	}

	public void setAccCollPsnName(String accCollPsnName) {
		this.accCollPsnName = accCollPsnName;
	}

	public String getLegalPsnName() {
		return legalPsnName;
	}

	public void setLegalPsnName(String legalPsnName) {
		this.legalPsnName = legalPsnName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getClCustNo() {
		return clCustNo;
	}

	public void setClCustNo(int clCustNo) {
		this.clCustNo = clCustNo;
	}

	public int getClFacmNo() {
		return clFacmNo;
	}

	public void setClFacmNo(int clFacmNo) {
		this.clFacmNo = clFacmNo;
	}

	public int getClRowNo() {
		return clRowNo;
	}

	public void setClRowNo(int clRowNo) {
		this.clRowNo = clRowNo;
	}

	public String getIsSpecify() {
		return isSpecify;
	}

	public void setIsSpecify(String isSpecify) {
		this.isSpecify = isSpecify;
	}

	@Override
	public String toString() {
		return "L5060Vo [custNo=" + custNo + ", facmNo=" + facmNo + ", txDate=" + txDate + ", txCode=" + txCode + ", remindDate=" + remindDate + ", prevIntDate=" + prevIntDate + ", ovduTerm="
				+ ovduTerm + ", ovduDays=" + ovduDays + ", currencyCode=" + currencyCode + ", prinBalance=" + prinBalance + ", accCollPsn=" + accCollPsn + ", legalPsn=" + legalPsn
				+ ", accCollPsnName=" + accCollPsnName + ", legalPsnName=" + legalPsnName + ", status=" + status + ", clCustNo=" + clCustNo + ", clFacmNo=" + clFacmNo + ", clRowNo=" + clRowNo
				+ ", isSpecify=" + isSpecify + "]";
	}

}
