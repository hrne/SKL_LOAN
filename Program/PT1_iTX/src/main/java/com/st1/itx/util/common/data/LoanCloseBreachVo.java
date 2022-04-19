package com.st1.itx.util.common.data;

import java.math.BigDecimal;

public class LoanCloseBreachVo {

	int custNo; // 戶號
	int facmNo; // 額度編號
	int bormNo; // 撥款序號
	String prodNo; // 商品代碼
	String amortizedCode; // 攤還方式,還本方式 1.按月繳息(按期繳息到期還本) 2.到期取息(到期繳息還本) 3.本息平均法(期金) 4.本金平均法
	String breachGetCode; // 清償違約金收取方式 "1":即時收取 "2":領清償證明時收取
	String breachCode; // 違約適用方式 "001":綁約專案[按年分段] "002":綁約專案[按月分段] "003":依核准額度 "004":依撥款金額
						// "005":本息均攤依提前償還金額
	int prohibitMonth;// 綁約年限(月)
	int breachStartPercent; // 還款起算比例
	BigDecimal breachPercent; // 違約金百分比;
	int breachDecreaseMonth; // 違約金分段月數
	BigDecimal breachDecrease; // 分段遞減百分比;
	BigDecimal amount; // 計算金額
	int startDate; // 計算起日(首撥日)
	int endDate; // 計算止日(入帳日)
	int monIdx; // 遞減段數
	BigDecimal breachRate; // 計算百分比
	BigDecimal closeBreachAmt; // 提前清償違約金
	BigDecimal extraRepay; // 提前還款金額
	BigDecimal lineAmt; // 核准額度
	BigDecimal utilBal; // 已動用額度餘額
	BigDecimal extraRepayAcc; // 提前還款金額累計
	BigDecimal breachStartAmt; // 違約起算金額
	BigDecimal closeBreachAmtPaid; // 提前清償違約金(即時收取)
	BigDecimal closeBreachAmtUnpaid; // 提前清償違約金(領清償證明時收取)
	int acDate; // 會計日期
	String titaTxCd; // 交易代號
	String titaKinBr; // 單位別
	String titaTlrNo;// 經辦
	String titaTxtNo;// 交易序號

	public LoanCloseBreachVo() {
		custNo = 0;
		facmNo = 0;
		bormNo = 0;
		amount = new BigDecimal(0);
		startDate = 0;
		endDate = 0;
		breachRate = new BigDecimal(0);
		breachGetCode = "";
		closeBreachAmt = new BigDecimal(0);
		closeBreachAmtPaid = new BigDecimal(0);
		closeBreachAmtUnpaid = new BigDecimal(0);
		acDate = 0;
		titaTxCd = "";
		titaKinBr = "";
		titaTlrNo = "";
		titaTxtNo = "";
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

	public String getAmortizedCode() {
		return amortizedCode;
	}

	public void setAmortizedCode(String amortizedCode) {
		this.amortizedCode = amortizedCode;
	}

	public String getProdNo() {
		return prodNo;
	}

	public void setProdNo(String prodNo) {
		this.prodNo = prodNo;
	}

	public String getBreachCode() {
		return breachCode;
	}

	public void setBreachCode(String breachCode) {
		this.breachCode = breachCode;
	}

	public String getBreachGetCode() {
		return breachGetCode;
	}

	public void setBreachGetCode(String breachGetCode) {
		this.breachGetCode = breachGetCode;
	}

	public int getProhibitMonth() {
		return prohibitMonth;
	}

	public void setProhibitMonth(int prohibitMonth) {
		this.prohibitMonth = prohibitMonth;
	}

	public int getBreachStartPercent() {
		return breachStartPercent;
	}

	public void setBreachStartPercent(int breachStartPercent) {
		this.breachStartPercent = breachStartPercent;
	}

	public BigDecimal getBreachPercent() {
		return breachPercent;
	}

	public void setBreachPercent(BigDecimal breachPercent) {
		this.breachPercent = breachPercent;
	}

	public int getBreachDecreaseMonth() {
		return breachDecreaseMonth;
	}

	public void setBreachDecreaseMonth(int breachDecreaseMonth) {
		this.breachDecreaseMonth = breachDecreaseMonth;
	}

	public BigDecimal getBreachDecrease() {
		return breachDecrease;
	}

	public void setBreachDecrease(BigDecimal breachDecrease) {
		this.breachDecrease = breachDecrease;
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

	public int getMonIdx() {
		return monIdx;
	}

	public void setMonIdx(int monIdx) {
		this.monIdx = monIdx;
	}

	public BigDecimal getBreachRate() {
		return breachRate;
	}

	public void setBreachRate(BigDecimal breachRate) {
		this.breachRate = breachRate;
	}

	public BigDecimal getCloseBreachAmt() {
		return closeBreachAmt;
	}

	public void setCloseBreachAmt(BigDecimal closeBreachAmt) {
		this.closeBreachAmt = closeBreachAmt;
	}

	public BigDecimal getCloseBreachAmtPaid() {
		return closeBreachAmtPaid;
	}

	public void setCloseBreachAmtPaid(BigDecimal closeBreachAmtPaid) {
		this.closeBreachAmtPaid = closeBreachAmtPaid;
	}

	public BigDecimal getCloseBreachAmtUnpaid() {
		return closeBreachAmtUnpaid;
	}

	public void setCloseBreachAmtUnpaid(BigDecimal closeBreachAmtUnpaid) {
		this.closeBreachAmtUnpaid = closeBreachAmtUnpaid;
	}

	public BigDecimal getLineAmt() {
		return lineAmt;
	}

	public void setLineAmt(BigDecimal lineAmt) {
		this.lineAmt = lineAmt;
	}

	public BigDecimal getUtilBal() {
		return utilBal;
	}

	public void setUtilBal(BigDecimal UtilBal) {
		this.utilBal = UtilBal;
	}

	public BigDecimal getExtraRepay() {
		return extraRepay;
	}

	public void setExtraRepay(BigDecimal extraRepay) {
		this.extraRepay = extraRepay;
	}

	public BigDecimal getExtraRepayAcc() {
		return extraRepayAcc;
	}

	public void setExtraRepayAcc(BigDecimal extraRepayAcc) {
		this.extraRepayAcc = extraRepayAcc;
	}

	public BigDecimal getBreachStartAmt() {
		return breachStartAmt;
	}

	public void setBreachStartAmt(BigDecimal breachStartAmt) {
		this.breachStartAmt = breachStartAmt;
	}

	public int getAcDate() {
		return acDate;
	}

	public void setAcDate(int acDate) {
		this.acDate = acDate;
	}

	public String getTitaTxCd() {
		return titaTxCd;
	}

	public void setTitaTxCd(String titaTxCd) {
		this.titaTxCd = titaTxCd;
	}

	public String getTitaKinBr() {
		return titaKinBr;
	}

	public void setTitaKinBr(String titaKinBr) {
		this.titaKinBr = titaKinBr;
	}

	public String getTitaTlrNo() {
		return titaTlrNo;
	}

	public void setTitaTlrNo(String titaTlrNo) {
		this.titaTlrNo = titaTlrNo;
	}

	public String getTitaTxtNo() {
		return titaTxtNo;
	}

	public void setTitaTxtNo(String titaTxtNo) {
		this.titaTxtNo = titaTxtNo;
	}

	@Override
	public String toString() {
		return "LoanCloseBreachVo [custNo=" + custNo + ", facmNo=" + facmNo + ", bormNo=" + bormNo + ", prodNo=" + prodNo + ", amortizedCode=" + amortizedCode + ", breachGetCode=" + breachGetCode
				+ ", breachCode=" + breachCode + ", prohibitMonth=" + prohibitMonth + ", breachStartPercent=" + breachStartPercent + ", breachPercent=" + breachPercent + ", breachDecreaseMonth="
				+ breachDecreaseMonth + ", breachDecrease=" + breachDecrease + ", amount=" + amount + ", startDate=" + startDate + ", endDate=" + endDate + ", monIdx=" + monIdx + ", breachRate="
				+ breachRate + ", closeBreachAmt=" + closeBreachAmt + ", extraRepay=" + extraRepay + ", lineAmt=" + lineAmt + ", UtilBal=" + utilBal + ", extraRepayAcc=" + extraRepayAcc
				+ ", breachStartAmt=" + breachStartAmt + ", closeBreachAmtPaid=" + closeBreachAmtPaid + ", closeBreachAmtUnpaid=" + closeBreachAmtUnpaid + ", acDate=" + acDate + ", titaTxCd="
				+ titaTxCd + ", titaKinBr=" + titaKinBr + ", titaTlrNo=" + titaTlrNo + ", titaTxtNo=" + titaTxtNo + "]";
	}

}
