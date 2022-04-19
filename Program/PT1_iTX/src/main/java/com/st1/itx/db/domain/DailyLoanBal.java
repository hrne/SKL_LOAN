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
 * DailyLoanBal 每日放款餘額檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`DailyLoanBal`")
public class DailyLoanBal implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9170607439769067520L;

	@EmbeddedId
	private DailyLoanBalId dailyLoanBalId;

	// 資料日期
	/* 1.&amp;lt;餘額、利率、業務科目&amp;gt;變動日2.月底日 */
	@Column(name = "`DataDate`", insertable = false, updatable = false)
	private int dataDate = 0;

	// 戶號
	@Column(name = "`CustNo`", insertable = false, updatable = false)
	private int custNo = 0;

	// 額度編號
	@Column(name = "`FacmNo`", insertable = false, updatable = false)
	private int facmNo = 0;

	// 撥款序號
	@Column(name = "`BormNo`", insertable = false, updatable = false)
	private int bormNo = 0;

	// 資料是否為最新
	/* 0.一般變動日 1.最後變動日 */
	@Column(name = "`LatestFlag`")
	private int latestFlag = 0;

	// 月底年月
	/* 平常日-&amp;gt; 0, 月底日資料 -&amp;gt; yyyymm,ex.202005 */
	@Column(name = "`MonthEndYm`")
	private int monthEndYm = 0;

	// 幣別
	@Column(name = "`CurrencyCode`", length = 3)
	private String currencyCode;

	// 業務科目代號
	/* CdAcCode會計科子細目設定檔310: 短期擔保放款 320: 中期擔保放款330: 長期擔保放款340: 三十年房貸990: 催收款項 */
	@Column(name = "`AcctCode`", length = 3)
	private String acctCode;

	// 額度業務科目
	/* CdAcCode會計科子細目設定檔310: 短期擔保放款 320: 中期擔保放款330: 長期擔保放款340: 三十年房貸 */
	@Column(name = "`FacAcctCode`", length = 3)
	private String facAcctCode;

	// 商品代碼
	@Column(name = "`ProdNo`", length = 5)
	private String prodNo;

	// 放款餘額
	@Column(name = "`LoanBalance`")
	private BigDecimal loanBalance = new BigDecimal("0");

	// 計息利率
	@Column(name = "`StoreRate`")
	private BigDecimal storeRate = new BigDecimal("0");

	// 實收利息
	/* 不含逾期息、違約金 */
	@Column(name = "`IntAmtRcv`")
	private BigDecimal intAmtRcv = new BigDecimal("0");

	// 提存利息
	/* 月底日 */
	@Column(name = "`IntAmtAcc`")
	private BigDecimal intAmtAcc = new BigDecimal("0");

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

	public DailyLoanBalId getDailyLoanBalId() {
		return this.dailyLoanBalId;
	}

	public void setDailyLoanBalId(DailyLoanBalId dailyLoanBalId) {
		this.dailyLoanBalId = dailyLoanBalId;
	}

	/**
	 * 資料日期<br>
	 * 1.&amp;lt;餘額、利率、業務科目&amp;gt;變動日 2.月底日
	 * 
	 * @return Integer
	 */
	public int getDataDate() {
		return StaticTool.bcToRoc(this.dataDate);
	}

	/**
	 * 資料日期<br>
	 * 1.&amp;lt;餘額、利率、業務科目&amp;gt;變動日 2.月底日
	 *
	 * @param dataDate 資料日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setDataDate(int dataDate) throws LogicException {
		this.dataDate = StaticTool.rocToBc(dataDate);
	}

	/**
	 * 戶號<br>
	 * 
	 * @return Integer
	 */
	public int getCustNo() {
		return this.custNo;
	}

	/**
	 * 戶號<br>
	 * 
	 *
	 * @param custNo 戶號
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
	 * 資料是否為最新<br>
	 * 0.一般變動日 1.最後變動日
	 * 
	 * @return Integer
	 */
	public int getLatestFlag() {
		return this.latestFlag;
	}

	/**
	 * 資料是否為最新<br>
	 * 0.一般變動日 1.最後變動日
	 *
	 * @param latestFlag 資料是否為最新
	 */
	public void setLatestFlag(int latestFlag) {
		this.latestFlag = latestFlag;
	}

	/**
	 * 月底年月<br>
	 * 平常日-&amp;gt; 0, 月底日資料 -&amp;gt; yyyymm,ex.202005
	 * 
	 * @return Integer
	 */
	public int getMonthEndYm() {
		return this.monthEndYm;
	}

	/**
	 * 月底年月<br>
	 * 平常日-&amp;gt; 0, 月底日資料 -&amp;gt; yyyymm,ex.202005
	 *
	 * @param monthEndYm 月底年月
	 */
	public void setMonthEndYm(int monthEndYm) {
		this.monthEndYm = monthEndYm;
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
	 * 業務科目代號<br>
	 * CdAcCode會計科子細目設定檔 310: 短期擔保放款 320: 中期擔保放款 330: 長期擔保放款 340: 三十年房貸 990: 催收款項
	 * 
	 * @return String
	 */
	public String getAcctCode() {
		return this.acctCode == null ? "" : this.acctCode;
	}

	/**
	 * 業務科目代號<br>
	 * CdAcCode會計科子細目設定檔 310: 短期擔保放款 320: 中期擔保放款 330: 長期擔保放款 340: 三十年房貸 990: 催收款項
	 *
	 * @param acctCode 業務科目代號
	 */
	public void setAcctCode(String acctCode) {
		this.acctCode = acctCode;
	}

	/**
	 * 額度業務科目<br>
	 * CdAcCode會計科子細目設定檔 310: 短期擔保放款 320: 中期擔保放款 330: 長期擔保放款 340: 三十年房貸
	 * 
	 * @return String
	 */
	public String getFacAcctCode() {
		return this.facAcctCode == null ? "" : this.facAcctCode;
	}

	/**
	 * 額度業務科目<br>
	 * CdAcCode會計科子細目設定檔 310: 短期擔保放款 320: 中期擔保放款 330: 長期擔保放款 340: 三十年房貸
	 *
	 * @param facAcctCode 額度業務科目
	 */
	public void setFacAcctCode(String facAcctCode) {
		this.facAcctCode = facAcctCode;
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
	 * 放款餘額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getLoanBalance() {
		return this.loanBalance;
	}

	/**
	 * 放款餘額<br>
	 * 
	 *
	 * @param loanBalance 放款餘額
	 */
	public void setLoanBalance(BigDecimal loanBalance) {
		this.loanBalance = loanBalance;
	}

	/**
	 * 計息利率<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getStoreRate() {
		return this.storeRate;
	}

	/**
	 * 計息利率<br>
	 * 
	 *
	 * @param storeRate 計息利率
	 */
	public void setStoreRate(BigDecimal storeRate) {
		this.storeRate = storeRate;
	}

	/**
	 * 實收利息<br>
	 * 不含逾期息、違約金
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getIntAmtRcv() {
		return this.intAmtRcv;
	}

	/**
	 * 實收利息<br>
	 * 不含逾期息、違約金
	 *
	 * @param intAmtRcv 實收利息
	 */
	public void setIntAmtRcv(BigDecimal intAmtRcv) {
		this.intAmtRcv = intAmtRcv;
	}

	/**
	 * 提存利息<br>
	 * 月底日
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getIntAmtAcc() {
		return this.intAmtAcc;
	}

	/**
	 * 提存利息<br>
	 * 月底日
	 *
	 * @param intAmtAcc 提存利息
	 */
	public void setIntAmtAcc(BigDecimal intAmtAcc) {
		this.intAmtAcc = intAmtAcc;
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
		return "DailyLoanBal [dailyLoanBalId=" + dailyLoanBalId + ", latestFlag=" + latestFlag + ", monthEndYm=" + monthEndYm + ", currencyCode=" + currencyCode + ", acctCode=" + acctCode
				+ ", facAcctCode=" + facAcctCode + ", prodNo=" + prodNo + ", loanBalance=" + loanBalance + ", storeRate=" + storeRate + ", intAmtRcv=" + intAmtRcv + ", intAmtAcc=" + intAmtAcc
				+ ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
