package com.st1.itx.db.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.EmbeddedId;
import javax.persistence.Column;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * LoanIntDetail 計息明細檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`LoanIntDetail`")
public class LoanIntDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8196005220052387774L;

	@EmbeddedId
	private LoanIntDetailId loanIntDetailId;

	// 借款人戶號
	@Column(name = "`CustNo`", insertable = false, updatable = false)
	private int custNo = 0;

	// 額度編號
	@Column(name = "`FacmNo`", insertable = false, updatable = false)
	private int facmNo = 0;

	// 撥款序號
	@Column(name = "`BormNo`", insertable = false, updatable = false)
	private int bormNo = 0;

	// 交易序號-會計日期
	@Column(name = "`AcDate`", insertable = false, updatable = false)
	private int acDate = 0;

	// 交易序號-櫃員別
	@Column(name = "`TlrNo`", length = 6, insertable = false, updatable = false)
	private String tlrNo;

	// 交易序號-流水號
	@Column(name = "`TxtNo`", length = 8, insertable = false, updatable = false)
	private String txtNo;

	// 計息流水號
	@Column(name = "`IntSeq`", insertable = false, updatable = false)
	private int intSeq = 0;

	// 計息起日
	@Column(name = "`IntStartDate`")
	private int intStartDate = 0;

	// 計息止日
	@Column(name = "`IntEndDate`")
	private int intEndDate = 0;

	// 計息日數
	@Column(name = "`IntDays`")
	private int intDays = 0;

	// 違約金日數
	@Column(name = "`BreachDays`")
	private int breachDays = 0;

	// 當月日數
	@Column(name = "`MonthLimit`")
	private int monthLimit = 0;

	// 計息記號
	/* 1:按日計息 2:按月計息 */
	@Column(name = "`IntFlag`")
	private int intFlag = 0;

	// 幣別
	@Column(name = "`CurrencyCode`", length = 3)
	private String currencyCode;

	// 計息本金
	@Column(name = "`Amount`")
	private BigDecimal amount = new BigDecimal("0");

	// 計息利率
	@Column(name = "`IntRate`")
	private BigDecimal intRate = new BigDecimal("0");

	// 回收本金
	@Column(name = "`Principal`")
	private BigDecimal principal = new BigDecimal("0");

	// 利息
	@Column(name = "`Interest`")
	private BigDecimal interest = new BigDecimal("0");

	// 延滯息
	@Column(name = "`DelayInt`")
	private BigDecimal delayInt = new BigDecimal("0");

	// 違約金
	@Column(name = "`BreachAmt`")
	private BigDecimal breachAmt = new BigDecimal("0");

	// 清償違約金
	@Column(name = "`CloseBreachAmt`")
	private BigDecimal closeBreachAmt = new BigDecimal("0");

	// 清償違約金收取方式
	/* 共用代碼檔1:即時收取2:領清償證明時收取 */
	@Column(name = "`BreachGetCode`", length = 1)
	private String breachGetCode;

	// 放款餘額
	@Column(name = "`LoanBal`")
	private BigDecimal loanBal = new BigDecimal("0");

	// 部分償還記號
	/* 0:否 1:是 */
	@Column(name = "`ExtraRepayFlag`")
	private int extraRepayFlag = 0;

	// 商品代碼
	@Column(name = "`ProdNo`", length = 5)
	private String prodNo;

	// 指標利率代碼
	@Column(name = "`BaseRateCode`", length = 2)
	private String baseRateCode;

	// 加碼利率
	@Column(name = "`RateIncr`")
	private BigDecimal rateIncr = new BigDecimal("0");

	// 個別加碼利率
	@Column(name = "`IndividualIncr`")
	private BigDecimal individualIncr = new BigDecimal("0");

	// 建檔日期時間
	@CreatedDate
	@Column(name = "`CreateDate`")
	private java.sql.Timestamp createDate;

	// 建檔人員
	@Column(name = "`CreateEmpNo`", length = 6)
	private String createEmpNo;

	// 最後更新日期時間
	@LastModifiedDate
	@Column(name = "`LastUpdate`")
	private java.sql.Timestamp lastUpdate;

	// 最後更新人員
	@Column(name = "`LastUpdateEmpNo`", length = 6)
	private String lastUpdateEmpNo;

	public LoanIntDetailId getLoanIntDetailId() {
		return this.loanIntDetailId;
	}

	public void setLoanIntDetailId(LoanIntDetailId loanIntDetailId) {
		this.loanIntDetailId = loanIntDetailId;
	}

	/**
	 * 借款人戶號<br>
	 * 
	 * @return Integer
	 */
	public int getCustNo() {
		return this.custNo;
	}

	/**
	 * 借款人戶號<br>
	 * 
	 *
	 * @param custNo 借款人戶號
	 */
	public void setCustNo(int custNo) {
		this.custNo = custNo;
	}

	/**
	 * 額度編號<br>
	 * 
	 * @return Integer
	 */
	public int getFacmNo() {
		return this.facmNo;
	}

	/**
	 * 額度編號<br>
	 * 
	 *
	 * @param facmNo 額度編號
	 */
	public void setFacmNo(int facmNo) {
		this.facmNo = facmNo;
	}

	/**
	 * 撥款序號<br>
	 * 
	 * @return Integer
	 */
	public int getBormNo() {
		return this.bormNo;
	}

	/**
	 * 撥款序號<br>
	 * 
	 *
	 * @param bormNo 撥款序號
	 */
	public void setBormNo(int bormNo) {
		this.bormNo = bormNo;
	}

	/**
	 * 交易序號-會計日期<br>
	 * 
	 * @return Integer
	 */
	public int getAcDate() {
		return StaticTool.bcToRoc(this.acDate);
	}

	/**
	 * 交易序號-會計日期<br>
	 * 
	 *
	 * @param acDate 交易序號-會計日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setAcDate(int acDate) throws LogicException {
		this.acDate = StaticTool.rocToBc(acDate);
	}

	/**
	 * 交易序號-櫃員別<br>
	 * 
	 * @return String
	 */
	public String getTlrNo() {
		return this.tlrNo == null ? "" : this.tlrNo;
	}

	/**
	 * 交易序號-櫃員別<br>
	 * 
	 *
	 * @param tlrNo 交易序號-櫃員別
	 */
	public void setTlrNo(String tlrNo) {
		this.tlrNo = tlrNo;
	}

	/**
	 * 交易序號-流水號<br>
	 * 
	 * @return String
	 */
	public String getTxtNo() {
		return this.txtNo == null ? "" : this.txtNo;
	}

	/**
	 * 交易序號-流水號<br>
	 * 
	 *
	 * @param txtNo 交易序號-流水號
	 */
	public void setTxtNo(String txtNo) {
		this.txtNo = txtNo;
	}

	/**
	 * 計息流水號<br>
	 * 
	 * @return Integer
	 */
	public int getIntSeq() {
		return this.intSeq;
	}

	/**
	 * 計息流水號<br>
	 * 
	 *
	 * @param intSeq 計息流水號
	 */
	public void setIntSeq(int intSeq) {
		this.intSeq = intSeq;
	}

	/**
	 * 計息起日<br>
	 * 
	 * @return Integer
	 */
	public int getIntStartDate() {
		return StaticTool.bcToRoc(this.intStartDate);
	}

	/**
	 * 計息起日<br>
	 * 
	 *
	 * @param intStartDate 計息起日
	 * @throws LogicException when Date Is Warn
	 */
	public void setIntStartDate(int intStartDate) throws LogicException {
		this.intStartDate = StaticTool.rocToBc(intStartDate);
	}

	/**
	 * 計息止日<br>
	 * 
	 * @return Integer
	 */
	public int getIntEndDate() {
		return StaticTool.bcToRoc(this.intEndDate);
	}

	/**
	 * 計息止日<br>
	 * 
	 *
	 * @param intEndDate 計息止日
	 * @throws LogicException when Date Is Warn
	 */
	public void setIntEndDate(int intEndDate) throws LogicException {
		this.intEndDate = StaticTool.rocToBc(intEndDate);
	}

	/**
	 * 計息日數<br>
	 * 
	 * @return Integer
	 */
	public int getIntDays() {
		return this.intDays;
	}

	/**
	 * 計息日數<br>
	 * 
	 *
	 * @param intDays 計息日數
	 */
	public void setIntDays(int intDays) {
		this.intDays = intDays;
	}

	/**
	 * 違約金日數<br>
	 * 
	 * @return Integer
	 */
	public int getBreachDays() {
		return this.breachDays;
	}

	/**
	 * 違約金日數<br>
	 * 
	 *
	 * @param breachDays 違約金日數
	 */
	public void setBreachDays(int breachDays) {
		this.breachDays = breachDays;
	}

	/**
	 * 當月日數<br>
	 * 
	 * @return Integer
	 */
	public int getMonthLimit() {
		return this.monthLimit;
	}

	/**
	 * 當月日數<br>
	 * 
	 *
	 * @param monthLimit 當月日數
	 */
	public void setMonthLimit(int monthLimit) {
		this.monthLimit = monthLimit;
	}

	/**
	 * 計息記號<br>
	 * 1:按日計息 2:按月計息
	 * 
	 * @return Integer
	 */
	public int getIntFlag() {
		return this.intFlag;
	}

	/**
	 * 計息記號<br>
	 * 1:按日計息 2:按月計息
	 *
	 * @param intFlag 計息記號
	 */
	public void setIntFlag(int intFlag) {
		this.intFlag = intFlag;
	}

	/**
	 * 幣別<br>
	 * 
	 * @return String
	 */
	public String getCurrencyCode() {
		return this.currencyCode == null ? "" : this.currencyCode;
	}

	/**
	 * 幣別<br>
	 * 
	 *
	 * @param currencyCode 幣別
	 */
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	/**
	 * 計息本金<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getAmount() {
		return this.amount;
	}

	/**
	 * 計息本金<br>
	 * 
	 *
	 * @param amount 計息本金
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * 計息利率<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getIntRate() {
		return this.intRate;
	}

	/**
	 * 計息利率<br>
	 * 
	 *
	 * @param intRate 計息利率
	 */
	public void setIntRate(BigDecimal intRate) {
		this.intRate = intRate;
	}

	/**
	 * 回收本金<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getPrincipal() {
		return this.principal;
	}

	/**
	 * 回收本金<br>
	 * 
	 *
	 * @param principal 回收本金
	 */
	public void setPrincipal(BigDecimal principal) {
		this.principal = principal;
	}

	/**
	 * 利息<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getInterest() {
		return this.interest;
	}

	/**
	 * 利息<br>
	 * 
	 *
	 * @param interest 利息
	 */
	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}

	/**
	 * 延滯息<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getDelayInt() {
		return this.delayInt;
	}

	/**
	 * 延滯息<br>
	 * 
	 *
	 * @param delayInt 延滯息
	 */
	public void setDelayInt(BigDecimal delayInt) {
		this.delayInt = delayInt;
	}

	/**
	 * 違約金<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getBreachAmt() {
		return this.breachAmt;
	}

	/**
	 * 違約金<br>
	 * 
	 *
	 * @param breachAmt 違約金
	 */
	public void setBreachAmt(BigDecimal breachAmt) {
		this.breachAmt = breachAmt;
	}

	/**
	 * 清償違約金<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getCloseBreachAmt() {
		return this.closeBreachAmt;
	}

	/**
	 * 清償違約金<br>
	 * 
	 *
	 * @param closeBreachAmt 清償違約金
	 */
	public void setCloseBreachAmt(BigDecimal closeBreachAmt) {
		this.closeBreachAmt = closeBreachAmt;
	}

	/**
	 * 清償違約金收取方式<br>
	 * 共用代碼檔 1:即時收取 2:領清償證明時收取
	 * 
	 * @return String
	 */
	public String getBreachGetCode() {
		return this.breachGetCode == null ? "" : this.breachGetCode;
	}

	/**
	 * 清償違約金收取方式<br>
	 * 共用代碼檔 1:即時收取 2:領清償證明時收取
	 *
	 * @param breachGetCode 清償違約金收取方式
	 */
	public void setBreachGetCode(String breachGetCode) {
		this.breachGetCode = breachGetCode;
	}

	/**
	 * 放款餘額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getLoanBal() {
		return this.loanBal;
	}

	/**
	 * 放款餘額<br>
	 * 
	 *
	 * @param loanBal 放款餘額
	 */
	public void setLoanBal(BigDecimal loanBal) {
		this.loanBal = loanBal;
	}

	/**
	 * 部分償還記號<br>
	 * 0:否 1:是
	 * 
	 * @return Integer
	 */
	public int getExtraRepayFlag() {
		return this.extraRepayFlag;
	}

	/**
	 * 部分償還記號<br>
	 * 0:否 1:是
	 *
	 * @param extraRepayFlag 部分償還記號
	 */
	public void setExtraRepayFlag(int extraRepayFlag) {
		this.extraRepayFlag = extraRepayFlag;
	}

	/**
	 * 商品代碼<br>
	 * 
	 * @return String
	 */
	public String getProdNo() {
		return this.prodNo == null ? "" : this.prodNo;
	}

	/**
	 * 商品代碼<br>
	 * 
	 *
	 * @param prodNo 商品代碼
	 */
	public void setProdNo(String prodNo) {
		this.prodNo = prodNo;
	}

	/**
	 * 指標利率代碼<br>
	 * 
	 * @return String
	 */
	public String getBaseRateCode() {
		return this.baseRateCode == null ? "" : this.baseRateCode;
	}

	/**
	 * 指標利率代碼<br>
	 * 
	 *
	 * @param baseRateCode 指標利率代碼
	 */
	public void setBaseRateCode(String baseRateCode) {
		this.baseRateCode = baseRateCode;
	}

	/**
	 * 加碼利率<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getRateIncr() {
		return this.rateIncr;
	}

	/**
	 * 加碼利率<br>
	 * 
	 *
	 * @param rateIncr 加碼利率
	 */
	public void setRateIncr(BigDecimal rateIncr) {
		this.rateIncr = rateIncr;
	}

	/**
	 * 個別加碼利率<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getIndividualIncr() {
		return this.individualIncr;
	}

	/**
	 * 個別加碼利率<br>
	 * 
	 *
	 * @param individualIncr 個別加碼利率
	 */
	public void setIndividualIncr(BigDecimal individualIncr) {
		this.individualIncr = individualIncr;
	}

	/**
	 * 建檔日期時間<br>
	 * 
	 * @return java.sql.Timestamp
	 */
	public java.sql.Timestamp getCreateDate() {
		return this.createDate;
	}

	/**
	 * 建檔日期時間<br>
	 * 
	 *
	 * @param createDate 建檔日期時間
	 */
	public void setCreateDate(java.sql.Timestamp createDate) {
		this.createDate = createDate;
	}

	/**
	 * 建檔人員<br>
	 * 
	 * @return String
	 */
	public String getCreateEmpNo() {
		return this.createEmpNo == null ? "" : this.createEmpNo;
	}

	/**
	 * 建檔人員<br>
	 * 
	 *
	 * @param createEmpNo 建檔人員
	 */
	public void setCreateEmpNo(String createEmpNo) {
		this.createEmpNo = createEmpNo;
	}

	/**
	 * 最後更新日期時間<br>
	 * 
	 * @return java.sql.Timestamp
	 */
	public java.sql.Timestamp getLastUpdate() {
		return this.lastUpdate;
	}

	/**
	 * 最後更新日期時間<br>
	 * 
	 *
	 * @param lastUpdate 最後更新日期時間
	 */
	public void setLastUpdate(java.sql.Timestamp lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	/**
	 * 最後更新人員<br>
	 * 
	 * @return String
	 */
	public String getLastUpdateEmpNo() {
		return this.lastUpdateEmpNo == null ? "" : this.lastUpdateEmpNo;
	}

	/**
	 * 最後更新人員<br>
	 * 
	 *
	 * @param lastUpdateEmpNo 最後更新人員
	 */
	public void setLastUpdateEmpNo(String lastUpdateEmpNo) {
		this.lastUpdateEmpNo = lastUpdateEmpNo;
	}

	@Override
	public String toString() {
		return "LoanIntDetail [loanIntDetailId=" + loanIntDetailId + ", intStartDate=" + intStartDate + ", intEndDate=" + intEndDate + ", intDays=" + intDays + ", breachDays=" + breachDays
				+ ", monthLimit=" + monthLimit + ", intFlag=" + intFlag + ", currencyCode=" + currencyCode + ", amount=" + amount + ", intRate=" + intRate + ", principal=" + principal + ", interest="
				+ interest + ", delayInt=" + delayInt + ", breachAmt=" + breachAmt + ", closeBreachAmt=" + closeBreachAmt + ", breachGetCode=" + breachGetCode + ", loanBal=" + loanBal
				+ ", extraRepayFlag=" + extraRepayFlag + ", prodNo=" + prodNo + ", baseRateCode=" + baseRateCode + ", rateIncr=" + rateIncr + ", individualIncr=" + individualIncr + ", createDate="
				+ createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
