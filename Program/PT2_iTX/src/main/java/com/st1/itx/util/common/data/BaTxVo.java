package com.st1.itx.util.common.data;

import java.math.BigDecimal;

import javax.persistence.Entity;

/**
 * @author st1
 *
 */
@Entity
public class BaTxVo implements Comparable<BaTxVo> {

	/**
	 * 資料類型<BR>
	 * 1.應收費用+未收費用+短繳期金 <BR>
	 * 2.本金利息 <BR>
	 * 3.暫收抵繳 <BR>
	 * 4.溢(C)短(D)繳 <BR>
	 * 5.其他額度暫收可抵繳 <BR>
	 * 6.另收欠款(未到期火險費用、費用收取之短繳期金、繳本息之聯貸費用) <BR>
	 */
	private int dataKind = 0;

	/**
	 * 還款類別 <BR>
	 * 01-期款 <BR>
	 * 02-部分償還 <BR>
	 * 03-結案 <BR>
	 * 04-帳管費 <BR>
	 * 05-火險費 <BR>
	 * 06-契變手續費 <BR>
	 * 07-法務費 <BR>
	 * 09-其他(清償違約金) <BR>
	 */
	private int repayType = 0;

	/**
	 * 借款人戶號
	 */
	private int custNo = 0;

	/**
	 * 額度編號
	 */
	private int facmNo = 0;

	/**
	 * 撥款序號
	 */
	private int bormNo = 0;

	/**
	 * 銷帳科目記號
	 */
	private int receivableFlag = 0;

	/**
	 * 銷帳編號
	 */
	private String rvNo = " ";

	/**
	 * 應繳息日、應繳日
	 */
	private int payIntDate = 0;

	/**
	 * 繳息期數
	 */
	private int paidTerms = 0;

	/**
	 * 未收金額
	 */
	private BigDecimal unPaidAmt = BigDecimal.ZERO;

	/**
	 * 還款順序 1.還款類別(費用)相同 > 2.應收費用 > 3:未收費用 > 4:短繳期金 > 5:已到期應繳本息 > 6.另收欠款> 7.未到期應繳本息
	 */
	private int repayPriority;

	/**
	 * 業務科目 (利息、費用)
	 */
	private String acctCode = " ";

	/**
	 * 借貸別
	 */
	private String dbCr = " ";

	/**
	 * 出帳金額
	 */
	private BigDecimal acctAmt = BigDecimal.ZERO;

	/**
	 * 放款餘額(還款前、只放第一期)
	 */
	private BigDecimal loanBal = BigDecimal.ZERO;

	/**
	 * 提前還款金額
	 */
	private BigDecimal extraAmt = BigDecimal.ZERO;

	/* ------------------- 計息明細 (按繳息期數) ---------------- */

	/**
	 * 計息起日
	 */
	private int intStartDate = 0;

	/**
	 * 計息止日
	 */
	private int intEndDate = 0;

	/**
	 * 計息本金
	 */
	private BigDecimal amount = BigDecimal.ZERO;

	/**
	 * 計息利率
	 */
	private BigDecimal intRate = BigDecimal.ZERO;

	/**
	 * 本金、短繳本金
	 */
	private BigDecimal principal = BigDecimal.ZERO;

	/**
	 * 利息、短繳利息
	 */
	private BigDecimal interest = BigDecimal.ZERO;

	/**
	 * 延滯息
	 */
	private BigDecimal delayInt = BigDecimal.ZERO;

	/**
	 * 違約金
	 */
	private BigDecimal breachAmt = BigDecimal.ZERO;

	/**
	 * 短繳清償違約金
	 */
	private BigDecimal closeBreachAmt = BigDecimal.ZERO;

	/**
	 * 費用金額
	 */
	private BigDecimal feeAmt = BigDecimal.ZERO;
	
	/**
	 * 銷帳jason紀錄欄
	 */
	private String rvJsonFields = "";
	
	/* ------------------- 備註欄 ---------------- */
	/**
	 * 短繳本金(備註於本金利息該筆)
	 */
	private BigDecimal unpaidPrin = BigDecimal.ZERO;

	/**
	 * 短繳利息(備註於 資料類型: 2.本金利息)
	 */
	private BigDecimal unpaidInt = BigDecimal.ZERO;
	/**
	 * 還款後餘額
	 */
	private BigDecimal loanBalPaid = BigDecimal.ZERO;

	/**
	 * 加碼利率
	 */
	private BigDecimal rateIncr = BigDecimal.ZERO;

	/**
	 * 個別加碼利率
	 */
	private BigDecimal individualIncr = BigDecimal.ZERO;

	/**
	 * 結案記號 1.正常結案 2.提前結案
	 */
	private int closeFg = 0;

	/* ------------------- 檢核表計算用 ---------------- */
	/**
	 * 累短收本金(檢核表計算用)
	 */
	private BigDecimal shortFallPrin = BigDecimal.ZERO;

	/**
	 * 累短收利息(檢核表計算用)
	 */
	private BigDecimal shortFallInt = BigDecimal.ZERO;

	/**
	 * 累短收清償違約金(檢核表計算用)
	 */
	private BigDecimal shortFallCloseBreach = BigDecimal.ZERO;

	/**
	 * 暫收款金額(暫收借) (檢核表計算用)
	 */
	private BigDecimal tempAmt = BigDecimal.ZERO;

	/**
	 * 溢收金額(暫收貸) (檢核表計算用)
	 */
	private BigDecimal overflow = BigDecimal.ZERO;
	/**
	 * 交易金額 (檢核表計算用)
	 */
	private BigDecimal txAmt = BigDecimal.ZERO;

	/**
	 * 作帳金額  (檢核表計算用)
	 */
	private BigDecimal acAmt = BigDecimal.ZERO;
	
	/**
	 * 分錄序號 (檢核表計算用)
	 */
	private int acSeq = 0;
	
	/**
	 * 額度科目 (檢核表計算用)
	 */
	private String facAcctCode = " ";

	@Override
	public String toString() {
		return "BaTxVo [dataKind=" + dataKind + ", repayType=" + repayType + ", custNo=" + custNo + ", facmNo=" + facmNo
				+ ", bormNo=" + bormNo + ", receivableFlag=" + receivableFlag + ", rvNo=" + rvNo + ", payIntDate="
				+ payIntDate + ", paidTerms=" + paidTerms + ", unPaidAmt=" + unPaidAmt + ", repayPriority="
				+ repayPriority + ", acctCode=" + acctCode + ", dbCr=" + dbCr + ", acctAmt=" + acctAmt + ", loanBal="
				+ loanBal + ", extraAmt=" + extraAmt + ", intStartDate=" + intStartDate + ", intEndDate=" + intEndDate
				+ ", amount=" + amount + ", intRate=" + intRate + ", principal=" + principal + ", interest=" + interest
				+ ", delayInt=" + delayInt + ", breachAmt=" + breachAmt + ", closeBreachAmt=" + closeBreachAmt
				+ ", feeAmt=" + feeAmt + ", rvJsonFields=" + rvJsonFields + ", unpaidPrin=" + unpaidPrin
				+ ", unpaidInt=" + unpaidInt + ", loanBalPaid=" + loanBalPaid + ", rateIncr=" + rateIncr
				+ ", individualIncr=" + individualIncr + ", closeFg=" + closeFg + ", shortFallPrin=" + shortFallPrin
				+ ", shortFallInt=" + shortFallInt + ", shortFallCloseBreach=" + shortFallCloseBreach + ", tempAmt="
				+ tempAmt + ", overflow=" + overflow + ", txAmt=" + txAmt + ", acAmt=" + acAmt + ", acSeq=" + acSeq
				+ ", facAcctCode=" + facAcctCode + "]";
	}

	public int getDataKind() {
		return dataKind;
	}

	public void setDataKind(int dataKind) {
		this.dataKind = dataKind;
	}

	public int getRepayType() {
		return repayType;
	}

	public void setRepayType(int repayType) {
		this.repayType = repayType;
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

	public int getReceivableFlag() {
		return receivableFlag;
	}

	public void setReceivableFlag(int receivableFlag) {
		this.receivableFlag = receivableFlag;
	}

	public String getRvNo() {
		return rvNo;
	}

	public void setRvNo(String rvNo) {
		this.rvNo = rvNo;
	}

	public int getPayIntDate() {
		return payIntDate;
	}

	public void setPayIntDate(int payIntDate) {
		this.payIntDate = payIntDate;
	}

	public int getPaidTerms() {
		return paidTerms;
	}

	public void setPaidTerms(int paidTerms) {
		this.paidTerms = paidTerms;
	}

	public BigDecimal getUnPaidAmt() {
		return unPaidAmt;
	}

	public void setUnPaidAmt(BigDecimal unPaidAmt) {
		this.unPaidAmt = unPaidAmt;
	}

	public int getRepayPriority() {
		return repayPriority;
	}

	public void setRepayPriority(int repayPriority) {
		this.repayPriority = repayPriority;
	}

	public String getAcctCode() {
		return acctCode;
	}

	public void setAcctCode(String acctCode) {
		this.acctCode = acctCode;
	}

	public String getDbCr() {
		return dbCr;
	}

	public void setDbCr(String dbCr) {
		this.dbCr = dbCr;
	}

	public BigDecimal getAcctAmt() {
		return acctAmt;
	}

	public void setAcctAmt(BigDecimal acctAmt) {
		this.acctAmt = acctAmt;
	}

	public BigDecimal getLoanBal() {
		return loanBal;
	}

	public void setLoanBal(BigDecimal loanBal) {
		this.loanBal = loanBal;
	}

	public BigDecimal getExtraAmt() {
		return extraAmt;
	}

	public void setExtraAmt(BigDecimal extraAmt) {
		this.extraAmt = extraAmt;
	}

	public int getIntStartDate() {
		return intStartDate;
	}

	public void setIntStartDate(int intStartDate) {
		this.intStartDate = intStartDate;
	}

	public int getIntEndDate() {
		return intEndDate;
	}

	public void setIntEndDate(int intEndDate) {
		this.intEndDate = intEndDate;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getIntRate() {
		return intRate;
	}

	public void setIntRate(BigDecimal intRate) {
		this.intRate = intRate;
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

	public BigDecimal getDelayInt() {
		return delayInt;
	}

	public void setDelayInt(BigDecimal delayInt) {
		this.delayInt = delayInt;
	}

	public BigDecimal getBreachAmt() {
		return breachAmt;
	}

	public void setBreachAmt(BigDecimal breachAmt) {
		this.breachAmt = breachAmt;
	}

	public BigDecimal getCloseBreachAmt() {
		return closeBreachAmt;
	}

	public void setCloseBreachAmt(BigDecimal closeBreachAmt) {
		this.closeBreachAmt = closeBreachAmt;
	}

	public BigDecimal getUnpaidPrin() {
		return unpaidPrin;
	}

	public void setUnpaidPrin(BigDecimal unpaidPrin) {
		this.unpaidPrin = unpaidPrin;
	}

	public BigDecimal getUnpaidInt() {
		return unpaidInt;
	}

	public void setUnpaidInt(BigDecimal unpaidInt) {
		this.unpaidInt = unpaidInt;
	}

	public BigDecimal getShortFallPrin() {
		return shortFallPrin;
	}

	public void setShortFallPrin(BigDecimal shortFallPrin) {
		this.shortFallPrin = shortFallPrin;
	}

	public BigDecimal getShortFallInt() {
		return shortFallInt;
	}

	public BigDecimal getShortFallCloseBreach() {
		return shortFallCloseBreach;
	}

	public void setShortFallCloseBreach(BigDecimal shortFallCloseBreach) {
		this.shortFallCloseBreach = shortFallCloseBreach;
	}

	public void setShortFallInt(BigDecimal shortFallInt) {
		this.shortFallInt = shortFallInt;
	}

	public BigDecimal getFeeAmt() {
		return feeAmt;
	}

	public void setFeeAmt(BigDecimal feeAmt) {
		this.feeAmt = feeAmt;
	}


	public String getRvJsonFields() {
		return rvJsonFields;
	}

	public void setRvJsonFields(String rvJsonFields) {
		this.rvJsonFields = rvJsonFields;
	}

	public BigDecimal getTempAmt() {
		return tempAmt;
	}

	public void setTempAmt(BigDecimal tempAmt) {
		this.tempAmt = tempAmt;
	}

	public BigDecimal getOverflow() {
		return overflow;
	}

	public void setOverflow(BigDecimal overFlow) {
		this.overflow = overFlow;
	}


	public BigDecimal getTxAmt() {
		return txAmt;
	}

	public void setTxAmt(BigDecimal txAmt) {
		this.txAmt = txAmt;
	}

	public BigDecimal getAcAmt() {
		return acAmt;
	}

	public void setAcAmt(BigDecimal acAmt) {
		this.acAmt = acAmt;
	}

	public String getFacAcctCode() {
		return facAcctCode;
	}

	public void setFacAcctCode(String facAcctCode) {
		this.facAcctCode = facAcctCode;
	}

	public BigDecimal getLoanBalPaid() {
		return loanBalPaid;
	}

	public void setLoanBalPaid(BigDecimal loanBalPaid) {
		this.loanBalPaid = loanBalPaid;
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

	public int getCloseFg() {
		return closeFg;
	}

	public void setCloseFg(int closeFg) {
		this.closeFg = closeFg;
	}

	public int getAcSeq() {
		return acSeq;
	}

	public void setAcSeq(int acSeq) {
		this.acSeq = acSeq;
	}

	@Override
	public int compareTo(BaTxVo o) {
		if (this == o)
			return 1;
		if (o == null)
			return 0;
		if (getClass() != o.getClass())
			return 0;

		BaTxVo other = (BaTxVo) o;

		/**
		 * 排序優先度(由小到大) payIntDate 應繳日
		 */
		if (this.payIntDate - other.payIntDate != 0) {
			return this.payIntDate - other.payIntDate;
		} else {
			return 0;
		}
	}

}