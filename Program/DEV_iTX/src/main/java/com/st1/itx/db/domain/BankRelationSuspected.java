package com.st1.itx.db.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.EmbeddedId;
import javax.persistence.Column;

/**
 * BankRelationSuspected 是否為疑似準利害關係人檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`BankRelationSuspected`")
public class BankRelationSuspected implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4928331465983012977L;

	@EmbeddedId
	private BankRelationSuspectedId bankRelationSuspectedId;

	// 自然人姓名
	@Column(name = "`RepCusName`", length = 100, insertable = false, updatable = false)
	private String repCusName;

	// 該自然人擔任董事長之公司統一編號
	@Column(name = "`CustId`", length = 11, insertable = false, updatable = false)
	private String custId;

	// 公司名稱
	@Column(name = "`CustName`", length = 100)
	private String custName;

	// 職務名稱
	@Column(name = "`SubCom`", length = 100)
	private String subCom;

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

	public BankRelationSuspectedId getBankRelationSuspectedId() {
		return this.bankRelationSuspectedId;
	}

	public void setBankRelationSuspectedId(BankRelationSuspectedId bankRelationSuspectedId) {
		this.bankRelationSuspectedId = bankRelationSuspectedId;
	}

	/**
	 * 自然人姓名<br>
	 * 
	 * @return String
	 */
	public String getRepCusName() {
		return this.repCusName == null ? "" : this.repCusName;
	}

	/**
	 * 自然人姓名<br>
	 * 
	 *
	 * @param repCusName 自然人姓名
	 */
	public void setRepCusName(String repCusName) {
		this.repCusName = repCusName;
	}

	/**
	 * 該自然人擔任董事長之公司統一編號<br>
	 * 
	 * @return String
	 */
	public String getCustId() {
		return this.custId == null ? "" : this.custId;
	}

	/**
	 * 該自然人擔任董事長之公司統一編號<br>
	 * 
	 *
	 * @param custId 該自然人擔任董事長之公司統一編號
	 */
	public void setCustId(String custId) {
		this.custId = custId;
	}

	/**
	 * 公司名稱<br>
	 * 
	 * @return String
	 */
	public String getCustName() {
		return this.custName == null ? "" : this.custName;
	}

	/**
	 * 公司名稱<br>
	 * 
	 *
	 * @param custName 公司名稱
	 */
	public void setCustName(String custName) {
		this.custName = custName;
	}

	/**
	 * 職務名稱<br>
	 * 
	 * @return String
	 */
	public String getSubCom() {
		return this.subCom == null ? "" : this.subCom;
	}

	/**
	 * 職務名稱<br>
	 * 
	 *
	 * @param subCom 職務名稱
	 */
	public void setSubCom(String subCom) {
		this.subCom = subCom;
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
		return "BankRelationSuspected [bankRelationSuspectedId=" + bankRelationSuspectedId + ", custName=" + custName + ", subCom=" + subCom + ", createDate=" + createDate + ", createEmpNo="
				+ createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
