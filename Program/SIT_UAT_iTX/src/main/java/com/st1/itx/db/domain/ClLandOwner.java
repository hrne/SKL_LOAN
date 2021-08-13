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
 * ClLandOwner 擔保品-土地所有權人檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`ClLandOwner`")
public class ClLandOwner implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1060909478293922880L;

	@EmbeddedId
	private ClLandOwnerId clLandOwnerId;

	// 擔保品代號1
	@Column(name = "`ClCode1`", insertable = false, updatable = false)
	private int clCode1 = 0;

	// 擔保品代號2
	@Column(name = "`ClCode2`", insertable = false, updatable = false)
	private int clCode2 = 0;

	// 擔保品編號
	@Column(name = "`ClNo`", insertable = false, updatable = false)
	private int clNo = 0;

	// 土地序號
	/* 房地:從1起編土地:固定000 */
	@Column(name = "`LandSeq`", insertable = false, updatable = false)
	private int landSeq = 0;

	// 所有權人統編
	@Column(name = "`OwnerId`", length = 10, insertable = false, updatable = false)
	private String ownerId;

	// 所有權人姓名
	@Column(name = "`OwnerName`", length = 100)
	private String ownerName;

	// 與授信戶關係
	/* 參考CdGuarantor */
	@Column(name = "`OwnerRelCode`", length = 2)
	private String ownerRelCode;

	// 持份比率(分子)
	@Column(name = "`OwnerPart`")
	private BigDecimal ownerPart = new BigDecimal("0");

	// 持份比率(分母)
	@Column(name = "`OwnerTotal`")
	private BigDecimal ownerTotal = new BigDecimal("0");

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

	public ClLandOwnerId getClLandOwnerId() {
		return this.clLandOwnerId;
	}

	public void setClLandOwnerId(ClLandOwnerId clLandOwnerId) {
		this.clLandOwnerId = clLandOwnerId;
	}

	/**
	 * 擔保品代號1<br>
	 * 
	 * @return Integer
	 */
	public int getClCode1() {
		return this.clCode1;
	}

	/**
	 * 擔保品代號1<br>
	 * 
	 *
	 * @param clCode1 擔保品代號1
	 */
	public void setClCode1(int clCode1) {
		this.clCode1 = clCode1;
	}

	/**
	 * 擔保品代號2<br>
	 * 
	 * @return Integer
	 */
	public int getClCode2() {
		return this.clCode2;
	}

	/**
	 * 擔保品代號2<br>
	 * 
	 *
	 * @param clCode2 擔保品代號2
	 */
	public void setClCode2(int clCode2) {
		this.clCode2 = clCode2;
	}

	/**
	 * 擔保品編號<br>
	 * 
	 * @return Integer
	 */
	public int getClNo() {
		return this.clNo;
	}

	/**
	 * 擔保品編號<br>
	 * 
	 *
	 * @param clNo 擔保品編號
	 */
	public void setClNo(int clNo) {
		this.clNo = clNo;
	}

	/**
	 * 土地序號<br>
	 * 房地:從1起編 土地:固定000
	 * 
	 * @return Integer
	 */
	public int getLandSeq() {
		return this.landSeq;
	}

	/**
	 * 土地序號<br>
	 * 房地:從1起編 土地:固定000
	 *
	 * @param landSeq 土地序號
	 */
	public void setLandSeq(int landSeq) {
		this.landSeq = landSeq;
	}

	/**
	 * 所有權人統編<br>
	 * 
	 * @return String
	 */
	public String getOwnerId() {
		return this.ownerId == null ? "" : this.ownerId;
	}

	/**
	 * 所有權人統編<br>
	 * 
	 *
	 * @param ownerId 所有權人統編
	 */
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	/**
	 * 所有權人姓名<br>
	 * 
	 * @return String
	 */
	public String getOwnerName() {
		return this.ownerName == null ? "" : this.ownerName;
	}

	/**
	 * 所有權人姓名<br>
	 * 
	 *
	 * @param ownerName 所有權人姓名
	 */
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
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
	 * 持份比率(分子)<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getOwnerPart() {
		return this.ownerPart;
	}

	/**
	 * 持份比率(分子)<br>
	 * 
	 *
	 * @param ownerPart 持份比率(分子)
	 */
	public void setOwnerPart(BigDecimal ownerPart) {
		this.ownerPart = ownerPart;
	}

	/**
	 * 持份比率(分母)<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getOwnerTotal() {
		return this.ownerTotal;
	}

	/**
	 * 持份比率(分母)<br>
	 * 
	 *
	 * @param ownerTotal 持份比率(分母)
	 */
	public void setOwnerTotal(BigDecimal ownerTotal) {
		this.ownerTotal = ownerTotal;
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
		return "ClLandOwner [clLandOwnerId=" + clLandOwnerId + ", ownerName=" + ownerName + ", ownerRelCode=" + ownerRelCode + ", ownerPart=" + ownerPart + ", ownerTotal=" + ownerTotal
				+ ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
