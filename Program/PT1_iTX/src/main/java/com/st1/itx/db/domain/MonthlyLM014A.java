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

/**
 * MonthlyLM014A 月報LM014工作檔A<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`MonthlyLM014A`")
public class MonthlyLM014A implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5329527847525645190L;

	@EmbeddedId
	private MonthlyLM014AId monthlyLM014AId;

	// 資料年月
	/* 西元年月YYYYMM */
	@Column(name = "`DataYM`", insertable = false, updatable = false)
	private int dataYM = 0;

	// 業務科目代號
	/* CdAcCode會計科子細目設定檔 */
	@Column(name = "`AcctCode`", length = 3, insertable = false, updatable = false)
	private String acctCode;

	// 帳戶類別
	/* 1:個人 2:公司 3:合計*早期資料才有3:合計,近年資料無3:合計 */
	@Column(name = "`AccountType`", insertable = false, updatable = false)
	private int accountType = 0;

	// 帳冊別
	@Column(name = "`AcBookCode`", length = 3, insertable = false, updatable = false)
	private String acBookCode;

	// 本月利息收入
	/* 當月利息 */
	@Column(name = "`MonthLoanIns`")
	private BigDecimal monthLoanIns = new BigDecimal("0");

	// 本月月底餘額
	/* 當月餘額*早期資料才有值,近年資料擺0 */
	@Column(name = "`MonthLoanBal`")
	private BigDecimal monthLoanBal = new BigDecimal("0");

	// 累計利息收入
	/* 年初到本月份之累計利息收入 */
	@Column(name = "`YearLoanIns`")
	private BigDecimal yearLoanIns = new BigDecimal("0");

	// 每月平均放款餘額
	/* 年初到本月份之每月平均放款餘額*早期資料才有值,近年資料擺0 */
	@Column(name = "`YearLoanBal`")
	private BigDecimal yearLoanBal = new BigDecimal("0");

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

	public MonthlyLM014AId getMonthlyLM014AId() {
		return this.monthlyLM014AId;
	}

	public void setMonthlyLM014AId(MonthlyLM014AId monthlyLM014AId) {
		this.monthlyLM014AId = monthlyLM014AId;
	}

	/**
	 * 資料年月<br>
	 * 西元年月YYYYMM
	 * 
	 * @return Integer
	 */
	public int getDataYM() {
		return this.dataYM;
	}

	/**
	 * 資料年月<br>
	 * 西元年月YYYYMM
	 *
	 * @param dataYM 資料年月
	 */
	public void setDataYM(int dataYM) {
		this.dataYM = dataYM;
	}

	/**
	 * 業務科目代號<br>
	 * CdAcCode會計科子細目設定檔
	 * 
	 * @return String
	 */
	public String getAcctCode() {
		return this.acctCode == null ? "" : this.acctCode;
	}

	/**
	 * 業務科目代號<br>
	 * CdAcCode會計科子細目設定檔
	 *
	 * @param acctCode 業務科目代號
	 */
	public void setAcctCode(String acctCode) {
		this.acctCode = acctCode;
	}

	/**
	 * 帳戶類別<br>
	 * 1:個人 2:公司 3:合計 早期資料才有3:合計,近年資料無3:合計
	 * 
	 * @return Integer
	 */
	public int getAccountType() {
		return this.accountType;
	}

	/**
	 * 帳戶類別<br>
	 * 1:個人 2:公司 3:合計 早期資料才有3:合計,近年資料無3:合計
	 *
	 * @param accountType 帳戶類別
	 */
	public void setAccountType(int accountType) {
		this.accountType = accountType;
	}

	/**
	 * 帳冊別<br>
	 * 
	 * @return String
	 */
	public String getAcBookCode() {
		return this.acBookCode == null ? "" : this.acBookCode;
	}

	/**
	 * 帳冊別<br>
	 * 
	 *
	 * @param acBookCode 帳冊別
	 */
	public void setAcBookCode(String acBookCode) {
		this.acBookCode = acBookCode;
	}

	/**
	 * 本月利息收入<br>
	 * 當月利息
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getMonthLoanIns() {
		return this.monthLoanIns;
	}

	/**
	 * 本月利息收入<br>
	 * 當月利息
	 *
	 * @param monthLoanIns 本月利息收入
	 */
	public void setMonthLoanIns(BigDecimal monthLoanIns) {
		this.monthLoanIns = monthLoanIns;
	}

	/**
	 * 本月月底餘額<br>
	 * 當月餘額 早期資料才有值,近年資料擺0
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getMonthLoanBal() {
		return this.monthLoanBal;
	}

	/**
	 * 本月月底餘額<br>
	 * 當月餘額 早期資料才有值,近年資料擺0
	 *
	 * @param monthLoanBal 本月月底餘額
	 */
	public void setMonthLoanBal(BigDecimal monthLoanBal) {
		this.monthLoanBal = monthLoanBal;
	}

	/**
	 * 累計利息收入<br>
	 * 年初到本月份之累計利息收入
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getYearLoanIns() {
		return this.yearLoanIns;
	}

	/**
	 * 累計利息收入<br>
	 * 年初到本月份之累計利息收入
	 *
	 * @param yearLoanIns 累計利息收入
	 */
	public void setYearLoanIns(BigDecimal yearLoanIns) {
		this.yearLoanIns = yearLoanIns;
	}

	/**
	 * 每月平均放款餘額<br>
	 * 年初到本月份之每月平均放款餘額 早期資料才有值,近年資料擺0
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getYearLoanBal() {
		return this.yearLoanBal;
	}

	/**
	 * 每月平均放款餘額<br>
	 * 年初到本月份之每月平均放款餘額 早期資料才有值,近年資料擺0
	 *
	 * @param yearLoanBal 每月平均放款餘額
	 */
	public void setYearLoanBal(BigDecimal yearLoanBal) {
		this.yearLoanBal = yearLoanBal;
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
		return "MonthlyLM014A [monthlyLM014AId=" + monthlyLM014AId + ", monthLoanIns=" + monthLoanIns + ", monthLoanBal=" + monthLoanBal + ", yearLoanIns=" + yearLoanIns + ", yearLoanBal="
				+ yearLoanBal + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
