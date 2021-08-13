package com.st1.itx.util.common.data;

import java.math.BigDecimal;

import javax.persistence.Entity;

@Entity
public class BaTxVo implements Comparable<BaTxVo> {

	/**
	 * 資料類型<BR>
	 * 1.應收費用+未收費用+短繳期金 <BR>
	 * 2.本金利息 <BR>
	 * 3.暫收抵繳 <BR>
	 * 4.溢(C)短(D)繳 <BR>
	 * 5.其他額度暫收可抵繳 <BR>
	 * 6.另收欠款(未到期火險費用、掛帳利息) <BR>
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
	 * 09-其他 <BR>
	 */
	private int repayType = 0;

	private int custNo = 0; // 借款人戶號

	private int facmNo = 0; // 額度編號

	private int bormNo = 0; // 撥款序號

	private int receivableFlag = 0; // 銷帳科目記號

	private String rvNo = " "; // 銷帳編號

	private int payIntDate = 0; // 應繳息日、應繳日

	private int paidTerms = 0; // 繳息期數

	private BigDecimal unPaidAmt = BigDecimal.ZERO; // 未收金額

	private int repayPriority; // 還款順序 1.還款類別(費用)相同 > 2.應收費用 > 3:未收費用 > 4:短繳期金 > 5:已到期應繳本息 > 6.另收欠款> 7.未到期應繳本息

	private String acctCode = " "; // 業務科目 (利息、費用)

	private String dbCr = " "; // 借貸別

	private BigDecimal acctAmt = BigDecimal.ZERO; // 出帳金額

	private BigDecimal loanBal = BigDecimal.ZERO; // 放款餘額(還款前、只放第一期)

	private BigDecimal extraAmt = BigDecimal.ZERO; // 提前還款金額

	/* ------------------- 計息明細 (按繳息期數) ---------------- */

	private int intStartDate = 0; // 計息起日

	private int intEndDate = 0; // 計息止日

	private BigDecimal amount = BigDecimal.ZERO; // 計息本金

	private BigDecimal intRate = BigDecimal.ZERO; // 計息利率

	private BigDecimal principal = BigDecimal.ZERO; // 本金、短繳本金

	private BigDecimal interest = BigDecimal.ZERO; // 利息、短繳利息

	private BigDecimal delayInt = BigDecimal.ZERO; // 延滯息

	private BigDecimal breachAmt = BigDecimal.ZERO; // 違約金

	private BigDecimal closeBreachAmt = BigDecimal.ZERO; // 短繳清償違約金

	private BigDecimal rateIncr = BigDecimal.ZERO; // 加碼利率

	private BigDecimal individualIncr = BigDecimal.ZERO; // 個別加碼利率

	private int closeFg = 0; // 結案記號 1.正常結案 2.提前結案

	@Override
	public String toString() {
		return "BaTxVo [dataKind=" + dataKind + ", repayType=" + repayType + ", custNo=" + custNo + ", facmNo=" + facmNo + ", bormNo=" + bormNo + ", receivableFlag=" + receivableFlag + ", rvNo="
				+ rvNo + ", payIntDate=" + payIntDate + ", paidTerms=" + paidTerms + ", unPaidAmt=" + unPaidAmt + ", repayPriority=" + repayPriority + ", acctCode=" + acctCode + ", dbCr=" + dbCr
				+ ", acctAmt=" + acctAmt + ", loanBal=" + loanBal + ", extraAmt=" + extraAmt + ", intStartDate=" + intStartDate + ", intEndDate=" + intEndDate + ", amount=" + amount + ", intRate="
				+ intRate + ", principal=" + principal + ", interest=" + interest + ", delayInt=" + delayInt + ", breachAmt=" + breachAmt + ", closeBreachAmt=" + closeBreachAmt + ", rateIncr="
				+ rateIncr + ", individualIncr=" + individualIncr + ", closeFg=" + closeFg + "]";
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

	@Override
	public int compareTo(BaTxVo o) {
		if (this == o)
			return 1;
		if (o == null)
			return 0;
		if (getClass() != o.getClass())
			return 0;

		int result = 0;

		BaTxVo other = (BaTxVo) o;

		/**
		 * 排序優先度(由小到大) payIntDate 應繳息日、應繳日 custNo 借款人戶號 facmNo 額度編號 bormNo 撥款序號
		 */
		if (this.payIntDate - other.payIntDate != 0) {
			result = this.payIntDate - other.payIntDate;
		} else if (this.custNo - other.custNo != 0) {
			result = this.custNo - other.custNo;
		} else if (this.facmNo - other.facmNo != 0) {
			result = this.facmNo - other.facmNo;
		} else if (this.bormNo - other.bormNo != 0) {
			result = this.bormNo - other.bormNo;
		} else {
			result = 0;
		}
		return result;
	}

}