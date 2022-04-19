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
 * FacShareRelation 共同借款人闗係檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`FacShareRelation`")
public class FacShareRelation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8845768038446311175L;

	@EmbeddedId
	private FacShareRelationId facShareRelationId;

	// 核准號碼
	@Column(name = "`ApplNo`", insertable = false, updatable = false)
	private int applNo = 0;

	// 共同借款人核准號碼
	@Column(name = "`RelApplNo`", insertable = false, updatable = false)
	private int relApplNo = 0;

	// 與共同借款人關係
	/* 參考CdGuarantor */
	@Column(name = "`RelCode`", length = 2)
	private String relCode;

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

	public FacShareRelationId getFacShareRelationId() {
		return this.facShareRelationId;
	}

	public void setFacShareRelationId(FacShareRelationId facShareRelationId) {
		this.facShareRelationId = facShareRelationId;
	}

	/**
	 * 核准號碼<br>
	 * 
	 * @return Integer
	 */
	public int getApplNo() {
		return this.applNo;
	}

	/**
	 * 核准號碼<br>
	 * 
	 *
	 * @param applNo 核准號碼
	 */
	public void setApplNo(int applNo) {
		this.applNo = applNo;
	}

	/**
	 * 共同借款人核准號碼<br>
	 * 
	 * @return Integer
	 */
	public int getRelApplNo() {
		return this.relApplNo;
	}

	/**
	 * 共同借款人核准號碼<br>
	 * 
	 *
	 * @param relApplNo 共同借款人核准號碼
	 */
	public void setRelApplNo(int relApplNo) {
		this.relApplNo = relApplNo;
	}

	/**
	 * 與共同借款人關係<br>
	 * 參考CdGuarantor
	 * 
	 * @return String
	 */
	public String getRelCode() {
		return this.relCode == null ? "" : this.relCode;
	}

	/**
	 * 與共同借款人關係<br>
	 * 參考CdGuarantor
	 *
	 * @param relCode 與共同借款人關係
	 */
	public void setRelCode(String relCode) {
		this.relCode = relCode;
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
		return "FacShareRelation [facShareRelationId=" + facShareRelationId + ", relCode=" + relCode + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
				+ ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
