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
 * ReltFamily 利害關係人親屬檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`ReltFamily`")
public class ReltFamily implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7897772314869146222L;

	@EmbeddedId
	private ReltFamilyId reltFamilyId;

	// 利害關係人識別碼
	@Column(name = "`ReltUKey`", length = 32, insertable = false, updatable = false)
	private String reltUKey;

	// 序號
	@Column(name = "`ReltSeq`", insertable = false, updatable = false)
	private int reltSeq = 0;

	// 親等代碼
	/* 共用代碼檔1: 配偶2: 三等親血親3: 二等親姻親4: 二親等內血親 */
	@Column(name = "`FamilyCode`", length = 1)
	private String familyCode;

	// 親屬稱謂代碼
	/* 共用代碼檔 */
	@Column(name = "`FamilyCallCode`", length = 2)
	private String familyCallCode;

	// 親屬身分證字號
	@Column(name = "`FamilyId`", length = 10)
	private String familyId;

	// 親屬姓名
	@Column(name = "`FamilyName`", length = 100)
	private String familyName;

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

	public ReltFamilyId getReltFamilyId() {
		return this.reltFamilyId;
	}

	public void setReltFamilyId(ReltFamilyId reltFamilyId) {
		this.reltFamilyId = reltFamilyId;
	}

	/**
	 * 利害關係人識別碼<br>
	 * 
	 * @return String
	 */
	public String getReltUKey() {
		return this.reltUKey == null ? "" : this.reltUKey;
	}

	/**
	 * 利害關係人識別碼<br>
	 * 
	 *
	 * @param reltUKey 利害關係人識別碼
	 */
	public void setReltUKey(String reltUKey) {
		this.reltUKey = reltUKey;
	}

	/**
	 * 序號<br>
	 * 
	 * @return Integer
	 */
	public int getReltSeq() {
		return this.reltSeq;
	}

	/**
	 * 序號<br>
	 * 
	 *
	 * @param reltSeq 序號
	 */
	public void setReltSeq(int reltSeq) {
		this.reltSeq = reltSeq;
	}

	/**
	 * 親等代碼<br>
	 * 共用代碼檔 1: 配偶 2: 三等親血親 3: 二等親姻親 4: 二親等內血親
	 * 
	 * @return String
	 */
	public String getFamilyCode() {
		return this.familyCode == null ? "" : this.familyCode;
	}

	/**
	 * 親等代碼<br>
	 * 共用代碼檔 1: 配偶 2: 三等親血親 3: 二等親姻親 4: 二親等內血親
	 *
	 * @param familyCode 親等代碼
	 */
	public void setFamilyCode(String familyCode) {
		this.familyCode = familyCode;
	}

	/**
	 * 親屬稱謂代碼<br>
	 * 共用代碼檔
	 * 
	 * @return String
	 */
	public String getFamilyCallCode() {
		return this.familyCallCode == null ? "" : this.familyCallCode;
	}

	/**
	 * 親屬稱謂代碼<br>
	 * 共用代碼檔
	 *
	 * @param familyCallCode 親屬稱謂代碼
	 */
	public void setFamilyCallCode(String familyCallCode) {
		this.familyCallCode = familyCallCode;
	}

	/**
	 * 親屬身分證字號<br>
	 * 
	 * @return String
	 */
	public String getFamilyId() {
		return this.familyId == null ? "" : this.familyId;
	}

	/**
	 * 親屬身分證字號<br>
	 * 
	 *
	 * @param familyId 親屬身分證字號
	 */
	public void setFamilyId(String familyId) {
		this.familyId = familyId;
	}

	/**
	 * 親屬姓名<br>
	 * 
	 * @return String
	 */
	public String getFamilyName() {
		return this.familyName == null ? "" : this.familyName;
	}

	/**
	 * 親屬姓名<br>
	 * 
	 *
	 * @param familyName 親屬姓名
	 */
	public void setFamilyName(String familyName) {
		this.familyName = familyName;
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
		return "ReltFamily [reltFamilyId=" + reltFamilyId + ", familyCode=" + familyCode + ", familyCallCode=" + familyCallCode + ", familyId=" + familyId + ", familyName=" + familyName
				+ ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
