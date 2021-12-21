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
 * FinReportCashFlow 客戶財務報表.現金流量表<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`FinReportCashFlow`")
public class FinReportCashFlow implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8573619994001575139L;

	@EmbeddedId
	private FinReportCashFlowId finReportCashFlowId;

	// 客戶識別碼
	@Column(name = "`CustUKey`", length = 32, insertable = false, updatable = false)
	private String custUKey;

	// 識別碼
	@Column(name = "`UKey`", length = 32, insertable = false, updatable = false)
	private String uKey;

	// 營業活動淨現金流入(出)
	@Column(name = "`BusCash`")
	private BigDecimal busCash = new BigDecimal("0");

	// 投資活動淨現金流入(出)
	@Column(name = "`InvestCash`")
	private BigDecimal investCash = new BigDecimal("0");

	// 理財活動淨現金流入(出)
	@Column(name = "`FinCash`")
	private BigDecimal finCash = new BigDecimal("0");

	// 會計科目01
	@Column(name = "`AccountItem01`", length = 20)
	private String accountItem01;

	// 會計科目02
	@Column(name = "`AccountItem02`", length = 20)
	private String accountItem02;

	// 會計科目值01
	@Column(name = "`AccountValue01`")
	private BigDecimal accountValue01 = new BigDecimal("0");

	// 會計科目值02
	@Column(name = "`AccountValue02`")
	private BigDecimal accountValue02 = new BigDecimal("0");

	// 期末現金餘額
	@Column(name = "`EndCash`")
	private BigDecimal endCash = new BigDecimal("0");

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

	public FinReportCashFlowId getFinReportCashFlowId() {
		return this.finReportCashFlowId;
	}

	public void setFinReportCashFlowId(FinReportCashFlowId finReportCashFlowId) {
		this.finReportCashFlowId = finReportCashFlowId;
	}

	/**
	 * 客戶識別碼<br>
	 * 
	 * @return String
	 */
	public String getCustUKey() {
		return this.custUKey == null ? "" : this.custUKey;
	}

	/**
	 * 客戶識別碼<br>
	 * 
	 *
	 * @param custUKey 客戶識別碼
	 */
	public void setCustUKey(String custUKey) {
		this.custUKey = custUKey;
	}

	/**
	 * 識別碼<br>
	 * 
	 * @return String
	 */
	public String getUKey() {
		return this.uKey == null ? "" : this.uKey;
	}

	/**
	 * 識別碼<br>
	 * 
	 *
	 * @param uKey 識別碼
	 */
	public void setUKey(String uKey) {
		this.uKey = uKey;
	}

	/**
	 * 營業活動淨現金流入(出)<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getBusCash() {
		return this.busCash;
	}

	/**
	 * 營業活動淨現金流入(出)<br>
	 * 
	 *
	 * @param busCash 營業活動淨現金流入(出)
	 */
	public void setBusCash(BigDecimal busCash) {
		this.busCash = busCash;
	}

	/**
	 * 投資活動淨現金流入(出)<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getInvestCash() {
		return this.investCash;
	}

	/**
	 * 投資活動淨現金流入(出)<br>
	 * 
	 *
	 * @param investCash 投資活動淨現金流入(出)
	 */
	public void setInvestCash(BigDecimal investCash) {
		this.investCash = investCash;
	}

	/**
	 * 理財活動淨現金流入(出)<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getFinCash() {
		return this.finCash;
	}

	/**
	 * 理財活動淨現金流入(出)<br>
	 * 
	 *
	 * @param finCash 理財活動淨現金流入(出)
	 */
	public void setFinCash(BigDecimal finCash) {
		this.finCash = finCash;
	}

	/**
	 * 會計科目01<br>
	 * 
	 * @return String
	 */
	public String getAccountItem01() {
		return this.accountItem01 == null ? "" : this.accountItem01;
	}

	/**
	 * 會計科目01<br>
	 * 
	 *
	 * @param accountItem01 會計科目01
	 */
	public void setAccountItem01(String accountItem01) {
		this.accountItem01 = accountItem01;
	}

	/**
	 * 會計科目02<br>
	 * 
	 * @return String
	 */
	public String getAccountItem02() {
		return this.accountItem02 == null ? "" : this.accountItem02;
	}

	/**
	 * 會計科目02<br>
	 * 
	 *
	 * @param accountItem02 會計科目02
	 */
	public void setAccountItem02(String accountItem02) {
		this.accountItem02 = accountItem02;
	}

	/**
	 * 會計科目值01<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getAccountValue01() {
		return this.accountValue01;
	}

	/**
	 * 會計科目值01<br>
	 * 
	 *
	 * @param accountValue01 會計科目值01
	 */
	public void setAccountValue01(BigDecimal accountValue01) {
		this.accountValue01 = accountValue01;
	}

	/**
	 * 會計科目值02<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getAccountValue02() {
		return this.accountValue02;
	}

	/**
	 * 會計科目值02<br>
	 * 
	 *
	 * @param accountValue02 會計科目值02
	 */
	public void setAccountValue02(BigDecimal accountValue02) {
		this.accountValue02 = accountValue02;
	}

	/**
	 * 期末現金餘額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getEndCash() {
		return this.endCash;
	}

	/**
	 * 期末現金餘額<br>
	 * 
	 *
	 * @param endCash 期末現金餘額
	 */
	public void setEndCash(BigDecimal endCash) {
		this.endCash = endCash;
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
		return "FinReportCashFlow [finReportCashFlowId=" + finReportCashFlowId + ", busCash=" + busCash + ", investCash=" + investCash + ", finCash=" + finCash + ", accountItem01=" + accountItem01
				+ ", accountItem02=" + accountItem02 + ", accountValue01=" + accountValue01 + ", accountValue02=" + accountValue02 + ", endCash=" + endCash + ", createDate=" + createDate
				+ ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
