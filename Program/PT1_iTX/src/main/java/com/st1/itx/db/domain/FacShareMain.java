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
 * FacShareMain 共用額度主檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`FacShareMain`")
public class FacShareMain implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7646901369329753750L;

	@EmbeddedId
	private FacShareMainId facShareMainId;

	// 主要戶號
	@Column(name = "`CustNo`", insertable = false, updatable = false)
	private int custNo = 0;

	// 主要案件編號
	@Column(name = "`CreditSysNo`", insertable = false, updatable = false)
	private int creditSysNo = 0;

	// 幣別
	@Column(name = "`CurrencyCode`", length = 3)
	private String currencyCode;

	// 總額度
	@Column(name = "`LineAmt`")
	private BigDecimal lineAmt = new BigDecimal("0");

	// 循環動用
	@Column(name = "`LineAmtCycle`")
	private BigDecimal lineAmtCycle = new BigDecimal("0");

	// 以主要戶號申報聯徵(Y/空白)
	@Column(name = "`JcicMiainCustFlag`", length = 1)
	private String jcicMiainCustFlag;

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

	public FacShareMainId getFacShareMainId() {
		return this.facShareMainId;
	}

	public void setFacShareMainId(FacShareMainId facShareMainId) {
		this.facShareMainId = facShareMainId;
	}

	/**
	 * 主要戶號<br>
	 * 
	 * @return Integer
	 */
	public int getCustNo() {
		return this.custNo;
	}

	/**
	 * 主要戶號<br>
	 * 
	 *
	 * @param custNo 主要戶號
	 */
	public void setCustNo(int custNo) {
		this.custNo = custNo;
	}

	/**
	 * 主要案件編號<br>
	 * 
	 * @return Integer
	 */
	public int getCreditSysNo() {
		return this.creditSysNo;
	}

	/**
	 * 主要案件編號<br>
	 * 
	 *
	 * @param creditSysNo 主要案件編號
	 */
	public void setCreditSysNo(int creditSysNo) {
		this.creditSysNo = creditSysNo;
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
	 * 總額度<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getLineAmt() {
		return this.lineAmt;
	}

	/**
	 * 總額度<br>
	 * 
	 *
	 * @param lineAmt 總額度
	 */
	public void setLineAmt(BigDecimal lineAmt) {
		this.lineAmt = lineAmt;
	}

	/**
	 * 循環動用<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getLineAmtCycle() {
		return this.lineAmtCycle;
	}

	/**
	 * 循環動用<br>
	 * 
	 *
	 * @param lineAmtCycle 循環動用
	 */
	public void setLineAmtCycle(BigDecimal lineAmtCycle) {
		this.lineAmtCycle = lineAmtCycle;
	}

	/**
	 * 以主要戶號申報聯徵(Y/空白)<br>
	 * 
	 * @return String
	 */
	public String getJcicMiainCustFlag() {
		return this.jcicMiainCustFlag == null ? "" : this.jcicMiainCustFlag;
	}

	/**
	 * 以主要戶號申報聯徵(Y/空白)<br>
	 * 
	 *
	 * @param jcicMiainCustFlag 以主要戶號申報聯徵(Y/空白)
	 */
	public void setJcicMiainCustFlag(String jcicMiainCustFlag) {
		this.jcicMiainCustFlag = jcicMiainCustFlag;
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
		return "FacShareMain [facShareMainId=" + facShareMainId + ", currencyCode=" + currencyCode + ", lineAmt=" + lineAmt + ", lineAmtCycle=" + lineAmtCycle + ", jcicMiainCustFlag="
				+ jcicMiainCustFlag + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
