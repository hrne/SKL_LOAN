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
 * CustRel 客戶關聯檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CustRel`")
public class CustRel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4877486325409465747L;

	@EmbeddedId
	private CustRelId custRelId;

	// 客戶識別碼
	@Column(name = "`CustUKey`", length = 32, insertable = false, updatable = false)
	private String custUKey;

	// 關聯戶識別碼
	@Column(name = "`RelUKey`", length = 32, insertable = false, updatable = false)
	private String relUKey;

	// 關聯戶種類
	/* 共用代碼檔01:關係企業02:關係戶 */
	@Column(name = "`RelCode`", length = 2)
	private String relCode;

	// 停用原因
	@Column(name = "`StopReason`", length = 40)
	private String stopReason;

	// 啟用記號
	/* Y:啟用N:停用 */
	@Column(name = "`Enable`", length = 1)
	private String enable;

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

	public CustRelId getCustRelId() {
		return this.custRelId;
	}

	public void setCustRelId(CustRelId custRelId) {
		this.custRelId = custRelId;
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
	 * 關聯戶識別碼<br>
	 * 
	 * @return String
	 */
	public String getRelUKey() {
		return this.relUKey == null ? "" : this.relUKey;
	}

	/**
	 * 關聯戶識別碼<br>
	 * 
	 *
	 * @param relUKey 關聯戶識別碼
	 */
	public void setRelUKey(String relUKey) {
		this.relUKey = relUKey;
	}

	/**
	 * 關聯戶種類<br>
	 * 共用代碼檔 01:關係企業 02:關係戶
	 * 
	 * @return String
	 */
	public String getRelCode() {
		return this.relCode == null ? "" : this.relCode;
	}

	/**
	 * 關聯戶種類<br>
	 * 共用代碼檔 01:關係企業 02:關係戶
	 *
	 * @param relCode 關聯戶種類
	 */
	public void setRelCode(String relCode) {
		this.relCode = relCode;
	}

	/**
	 * 停用原因<br>
	 * 
	 * @return String
	 */
	public String getStopReason() {
		return this.stopReason == null ? "" : this.stopReason;
	}

	/**
	 * 停用原因<br>
	 * 
	 *
	 * @param stopReason 停用原因
	 */
	public void setStopReason(String stopReason) {
		this.stopReason = stopReason;
	}

	/**
	 * 啟用記號<br>
	 * Y:啟用 N:停用
	 * 
	 * @return String
	 */
	public String getEnable() {
		return this.enable == null ? "" : this.enable;
	}

	/**
	 * 啟用記號<br>
	 * Y:啟用 N:停用
	 *
	 * @param enable 啟用記號
	 */
	public void setEnable(String enable) {
		this.enable = enable;
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
		return "CustRel [custRelId=" + custRelId + ", relCode=" + relCode + ", stopReason=" + stopReason + ", enable=" + enable + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
				+ ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
