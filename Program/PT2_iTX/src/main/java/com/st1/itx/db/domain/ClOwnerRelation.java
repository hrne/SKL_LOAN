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
 * ClOwnerRelation 擔保品所有權人與授信戶關係檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`ClOwnerRelation`")
public class ClOwnerRelation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7969510520899723499L;

	@EmbeddedId
	private ClOwnerRelationId clOwnerRelationId;

	// 案件編號
	/* 徵審系統案號(eLoan案件編號) */
	@Column(name = "`CreditSysNo`", insertable = false, updatable = false)
	private int creditSysNo = 0;

	// 借款人戶號
	@Column(name = "`CustNo`", insertable = false, updatable = false)
	private int custNo = 0;

	// 客戶識別碼
	@Column(name = "`OwnerCustUKey`", length = 32, insertable = false, updatable = false)
	private String ownerCustUKey;

	// 與授信戶關係
	/* 參考CdGuarantor */
	@Column(name = "`OwnerRelCode`", length = 2)
	private String ownerRelCode;

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

	public ClOwnerRelationId getClOwnerRelationId() {
		return this.clOwnerRelationId;
	}

	public void setClOwnerRelationId(ClOwnerRelationId clOwnerRelationId) {
		this.clOwnerRelationId = clOwnerRelationId;
	}

	/**
	 * 案件編號<br>
	 * 徵審系統案號(eLoan案件編號)
	 * 
	 * @return Integer
	 */
	public int getCreditSysNo() {
		return this.creditSysNo;
	}

	/**
	 * 案件編號<br>
	 * 徵審系統案號(eLoan案件編號)
	 *
	 * @param creditSysNo 案件編號
	 */
	public void setCreditSysNo(int creditSysNo) {
		this.creditSysNo = creditSysNo;
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
	 * 客戶識別碼<br>
	 * 
	 * @return String
	 */
	public String getOwnerCustUKey() {
		return this.ownerCustUKey == null ? "" : this.ownerCustUKey;
	}

	/**
	 * 客戶識別碼<br>
	 * 
	 *
	 * @param ownerCustUKey 客戶識別碼
	 */
	public void setOwnerCustUKey(String ownerCustUKey) {
		this.ownerCustUKey = ownerCustUKey;
	}

	/**
	 * 與授信戶關係<br>
	 * 參考CdGuarantor
	 * 
	 * @return String
	 */
	public String getOwnerRelCode() {
		return this.ownerRelCode == null ? "" : this.ownerRelCode;
	}

	/**
	 * 與授信戶關係<br>
	 * 參考CdGuarantor
	 *
	 * @param ownerRelCode 與授信戶關係
	 */
	public void setOwnerRelCode(String ownerRelCode) {
		this.ownerRelCode = ownerRelCode;
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
		return "ClOwnerRelation [clOwnerRelationId=" + clOwnerRelationId + ", ownerRelCode=" + ownerRelCode + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate="
				+ lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
