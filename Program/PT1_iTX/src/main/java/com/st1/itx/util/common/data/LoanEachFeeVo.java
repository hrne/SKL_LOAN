package com.st1.itx.util.common.data;

import java.math.BigDecimal;

public class LoanEachFeeVo {
	int entryDate; // 入帳日
	int custNo; // 戶號
	int facmNo; // 額度編號
	int bormNo; // 撥款序號
	BigDecimal shortfall; // 累短收
	BigDecimal shortfallInterest; // 累短收 - 利息
	BigDecimal shortfallPrincipal; // 累短收 - 本金
	BigDecimal excessive; // 累溢收
	BigDecimal tempTax; // 暫付所得稅
	BigDecimal modifyFee; // 契變手續費 F29
	BigDecimal acctFee; // 帳管費用 F10
	BigDecimal fireFee; // 火險費用 F09
	BigDecimal unOpenfireFee; // 未到期火險費用(續約保單起日>=入帳日)，火險費用也含
	BigDecimal collFireFee; // 催收火險費 F25
	BigDecimal lawFee; // 法務費用 F07
	BigDecimal collLawFee; // 催收法務費 F24
	BigDecimal closeBreachAmt; // 清償違約金

	public LoanEachFeeVo() {
		entryDate = 0;
		custNo = 0;
		facmNo = 0;
		bormNo = 0;
		shortfall = BigDecimal.ZERO;
		shortfallInterest = BigDecimal.ZERO;
		shortfallPrincipal = BigDecimal.ZERO;
		excessive = BigDecimal.ZERO;
		tempTax = BigDecimal.ZERO;
		modifyFee = BigDecimal.ZERO;
		acctFee = BigDecimal.ZERO;
		fireFee = BigDecimal.ZERO;
		collFireFee = BigDecimal.ZERO;
		lawFee = BigDecimal.ZERO;
		collLawFee = BigDecimal.ZERO;
		unOpenfireFee = BigDecimal.ZERO;
		closeBreachAmt = BigDecimal.ZERO;
	}

	public int getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(int entryDate) {
		this.entryDate = entryDate;
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

	public BigDecimal getShortfall() {
		return shortfall;
	}

	public void setShortfall(BigDecimal shortfall) {
		this.shortfall = shortfall;
	}

	public BigDecimal getShortfallInterest() {
		return shortfallInterest;
	}

	public void setShortfallInterest(BigDecimal shortfallInterest) {
		this.shortfallInterest = shortfallInterest;
	}

	public BigDecimal getShortfallPrincipal() {
		return shortfallPrincipal;
	}

	public void setShortfallPrincipal(BigDecimal shortfallPrincipal) {
		this.shortfallPrincipal = shortfallPrincipal;
	}

	public BigDecimal getExcessive() {
		return excessive;
	}

	public void setExcessive(BigDecimal excessive) {
		this.excessive = excessive;
	}

	public BigDecimal getTempTax() {
		return tempTax;
	}

	public void setTempTax(BigDecimal tempTax) {
		this.tempTax = tempTax;
	}

	public BigDecimal getModifyFee() {
		return modifyFee;
	}

	public void setModifyFee(BigDecimal modifyFee) {
		this.modifyFee = modifyFee;
	}

	public BigDecimal getAcctFee() {
		return acctFee;
	}

	public void setAcctFee(BigDecimal acctFee) {
		this.acctFee = acctFee;
	}

	public BigDecimal getFireFee() {
		return fireFee;
	}

	public void setFireFee(BigDecimal fireFee) {
		this.fireFee = fireFee;
	}

	public BigDecimal getUnOpenfireFee() {
		return unOpenfireFee;
	}

	public void setUnOpenfireFee(BigDecimal unOpenfireFee) {
		this.unOpenfireFee = unOpenfireFee;
	}

	public BigDecimal getCollFireFee() {
		return collFireFee;
	}

	public void setCollFireFee(BigDecimal collFireFee) {
		this.collFireFee = collFireFee;
	}

	public BigDecimal getLawFee() {
		return lawFee;
	}

	public void setLawFee(BigDecimal lawFee) {
		this.lawFee = lawFee;
	}

	public BigDecimal getCollLawFee() {
		return collLawFee;
	}

	public void setCollLawFee(BigDecimal collLawFee) {
		this.collLawFee = collLawFee;
	}

	public BigDecimal getCloseBreachAmt() {
		return closeBreachAmt;
	}

	public void setCloseBreachAmt(BigDecimal closeBreachAmt) {
		this.closeBreachAmt = closeBreachAmt;
	}

}
