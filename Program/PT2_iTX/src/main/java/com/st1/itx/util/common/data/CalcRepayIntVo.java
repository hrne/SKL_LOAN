package com.st1.itx.util.common.data;

import java.math.BigDecimal;

public class CalcRepayIntVo {
	int custNo; // 戶號
	int facmNo; // 額度編號
	int bormNo; // 撥款序號
	int termNo; // 期數編號
	int type; // 種類
	BigDecimal amount; // 計算金額
	int startDate; // 計算起日
	int endDate; // 計算止日
	int days; // 計算日數
	BigDecimal storeRate; // 計算利率
	BigDecimal rateIncr; // 加碼利率
	BigDecimal individualIncr; // 個別加碼利率
	BigDecimal principal; // 應還本金
	BigDecimal interest; // 利息
	BigDecimal breachAmt; // 違約金,
	BigDecimal loanBal; // 放款餘額
	int odDays; // 違約日數
	BigDecimal delayInt; // 遲延息,延滯息
	String breachGetCode; // 清償違約金收取方式 1:即時收取 2:領清償證明時收取
	BigDecimal closeBreachAmt; // 提前清償違約金
	int duraFlag; // 分段計息記號
	int principalFlag; // 還本記號 0:不還本 1:要還
	int monthLimit; // 當月天數
	int interestFlag; // 計息記號 1:按日計息 2:按月計息
	int extraRepayFlag; // 部分償還金額記號 0:否 1:是
	BigDecimal dueAmt; // 期金
	String rateCode; // 利率區分 1: 機動 2: 固定 3: 定期機動
	BigDecimal nextStoreRate; // 下段適用利率

	public CalcRepayIntVo() {
		custNo = 0;
		facmNo = 0;
		bormNo = 0;
		type = 0;
		amount = new BigDecimal(0);
		startDate = 0;
		endDate = 0;
		days = 0;
		storeRate = new BigDecimal(0);
		rateIncr = new BigDecimal(0);
		individualIncr = new BigDecimal(0);
		principal = new BigDecimal(0);
		interest = new BigDecimal(0);
		breachAmt = new BigDecimal(0);
		loanBal = new BigDecimal(0);
		dueAmt = new BigDecimal(0);
		odDays = 0;
		delayInt = new BigDecimal(0);
		breachGetCode = "";
		closeBreachAmt = new BigDecimal(0);
		duraFlag = 0;
		principalFlag = 0;
		monthLimit = 0;
		interestFlag = 0;
		rateCode = "";
		nextStoreRate = BigDecimal.ZERO;
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

	public int getBormNo() {
		return bormNo;
	}

	public void setBormNo(int bormNo) {
		this.bormNo = bormNo;
	}

	public int getTermNo() {
		return termNo;
	}

	public void setTermNo(int termNo) {
		this.termNo = termNo;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public int getStartDate() {
		return startDate;
	}

	public void setStartDate(int startDate) {
		this.startDate = startDate;
	}

	public int getEndDate() {
		return endDate;
	}

	public void setEndDate(int endDate) {
		this.endDate = endDate;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public BigDecimal getStoreRate() {
		return storeRate;
	}

	public void setStoreRate(BigDecimal storeRate) {
		this.storeRate = storeRate;
	}

	public BigDecimal getRateIncr() {
		return rateIncr;
	}

	public void setRateIncr(BigDecimal rateIncr) {
		this.rateIncr = rateIncr;
	}

	public BigDecimal getIndividualIncr() {
		return individualIncr;
	}

	public void setIndividualIncr(BigDecimal individualIncr) {
		this.individualIncr = individualIncr;
	}

	public BigDecimal getPrincipal() {
		return principal;
	}

	public void setPrincipal(BigDecimal principal) {
		this.principal = principal;
	}

	public BigDecimal getInterest() {
		return interest;
	}

	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}

	public BigDecimal getBreachAmt() {
		return breachAmt;
	}

	public void setBreachAmt(BigDecimal breachAmt) {
		this.breachAmt = breachAmt;
	}

	public BigDecimal getLoanBal() {
		return loanBal;
	}

	public void setDueAmt(BigDecimal dueAmt) {
		this.dueAmt = dueAmt;
	}

	public BigDecimal getDueAmt() {
		return dueAmt;
	}

	public void setLoanBal(BigDecimal loanBal) {
		this.loanBal = loanBal;
	}

	public int getOdDays() {
		return odDays;
	}

	public void setOdDays(int odDays) {
		this.odDays = odDays;
	}

	public BigDecimal getDelayInt() {
		return delayInt;
	}

	public void setDelayInt(BigDecimal delayInt) {
		this.delayInt = delayInt;
	}

	public String getBreachGetCode() {
		return breachGetCode;
	}

	public void setBreachGetCode(String breachGetCode) {
		this.breachGetCode = breachGetCode;
	}

	public BigDecimal getCloseBreachAmt() {
		return closeBreachAmt;
	}

	public void setCloseBreachAmt(BigDecimal closeBreachAmt) {
		this.closeBreachAmt = closeBreachAmt;
	}

	public int getDuraFlag() {
		return duraFlag;
	}

	public void setDuraFlag(int duraFlag) {
		this.duraFlag = duraFlag;
	}

	public int getPrincipalFlag() {
		return principalFlag;
	}

	public void setPrincipalFlag(int principalFlag) {
		this.principalFlag = principalFlag;
	}

	public int getMonthLimit() {
		return monthLimit;
	}

	public void setMonthLimit(int monthLimit) {
		this.monthLimit = monthLimit;
	}

	public int getInterestFlag() {
		return interestFlag;
	}

	public void setInterestFlag(int interestFlag) {
		this.interestFlag = interestFlag;
	}

	public int getExtraRepayFlag() {
		return extraRepayFlag;
	}

	public void setExtraRepayFlag(int extraRepayFlag) {
		this.extraRepayFlag = extraRepayFlag;
	}

	public String getRateCode() {
		return rateCode;
	}

	public void setRateCode(String rateCode) {
		this.rateCode = rateCode;
	}

	public BigDecimal getNextStoreRate() {
		return nextStoreRate;
	}

	public void setNextStoreRate(BigDecimal nextStoreRate) {
		this.nextStoreRate = nextStoreRate;
	}
}
