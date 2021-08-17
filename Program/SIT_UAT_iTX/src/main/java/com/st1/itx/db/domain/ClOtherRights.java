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
 * ClOtherRights 擔保品他項權利檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`ClOtherRights`")
public class ClOtherRights implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1082692662681058986L;

	@EmbeddedId
	private ClOtherRightsId clOtherRightsId;

	// 擔保品代號1
	/* 擔保品代號檔CdCl */
	@Column(name = "`ClCode1`", insertable = false, updatable = false)
	private int clCode1 = 0;

	// 擔保品代號2
	/* 擔保品代號檔CdCl */
	@Column(name = "`ClCode2`", insertable = false, updatable = false)
	private int clCode2 = 0;

	// 擔保品編號
	@Column(name = "`ClNo`", insertable = false, updatable = false)
	private int clNo = 0;

	// 他項權利序號
	/* ex:0000-000 */
	@Column(name = "`Seq`", length = 8, insertable = false, updatable = false)
	private String seq;

	// 縣市
	/* 縣市中文 */
	@Column(name = "`City`", length = 3)
	private String city;

	// 地政
	/* 地政所名稱 */
	@Column(name = "`LandAdm`", length = 3)
	private String landAdm;

	// 收件年
	@Column(name = "`RecYear`")
	private int recYear = 0;

	// 收件字
	@Column(name = "`RecWord`", length = 4)
	private String recWord;

	// 收件號
	@Column(name = "`RecNumber`", length = 6)
	private String recNumber;

	// 權利價值說明
	@Column(name = "`RightsNote`", length = 10)
	private String rightsNote;

	// 擔保債權總金額
	@Column(name = "`SecuredTotal`")
	private BigDecimal securedTotal = new BigDecimal("0");

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

	public ClOtherRightsId getClOtherRightsId() {
		return this.clOtherRightsId;
	}

	public void setClOtherRightsId(ClOtherRightsId clOtherRightsId) {
		this.clOtherRightsId = clOtherRightsId;
	}

	/**
	 * 擔保品代號1<br>
	 * 擔保品代號檔CdCl
	 * 
	 * @return Integer
	 */
	public int getClCode1() {
		return this.clCode1;
	}

	/**
	 * 擔保品代號1<br>
	 * 擔保品代號檔CdCl
	 *
	 * @param clCode1 擔保品代號1
	 */
	public void setClCode1(int clCode1) {
		this.clCode1 = clCode1;
	}

	/**
	 * 擔保品代號2<br>
	 * 擔保品代號檔CdCl
	 * 
	 * @return Integer
	 */
	public int getClCode2() {
		return this.clCode2;
	}

	/**
	 * 擔保品代號2<br>
	 * 擔保品代號檔CdCl
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
	 * 他項權利序號<br>
	 * ex:0000-000
	 * 
	 * @return String
	 */
	public String getSeq() {
		return this.seq == null ? "" : this.seq;
	}

	/**
	 * 他項權利序號<br>
	 * ex:0000-000
	 *
	 * @param seq 他項權利序號
	 */
	public void setSeq(String seq) {
		this.seq = seq;
	}

	/**
	 * 縣市<br>
	 * 縣市中文
	 * 
	 * @return String
	 */
	public String getCity() {
		return this.city == null ? "" : this.city;
	}

	/**
	 * 縣市<br>
	 * 縣市中文
	 *
	 * @param city 縣市
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * 地政<br>
	 * 地政所名稱
	 * 
	 * @return String
	 */
	public String getLandAdm() {
		return this.landAdm == null ? "" : this.landAdm;
	}

	/**
	 * 地政<br>
	 * 地政所名稱
	 *
	 * @param landAdm 地政
	 */
	public void setLandAdm(String landAdm) {
		this.landAdm = landAdm;
	}

	/**
	 * 收件年<br>
	 * 
	 * @return Integer
	 */
	public int getRecYear() {
		return this.recYear;
	}

	/**
	 * 收件年<br>
	 * 
	 *
	 * @param recYear 收件年
	 */
	public void setRecYear(int recYear) {
		this.recYear = recYear;
	}

	/**
	 * 收件字<br>
	 * 
	 * @return String
	 */
	public String getRecWord() {
		return this.recWord == null ? "" : this.recWord;
	}

	/**
	 * 收件字<br>
	 * 
	 *
	 * @param recWord 收件字
	 */
	public void setRecWord(String recWord) {
		this.recWord = recWord;
	}

	/**
	 * 收件號<br>
	 * 
	 * @return String
	 */
	public String getRecNumber() {
		return this.recNumber == null ? "" : this.recNumber;
	}

	/**
	 * 收件號<br>
	 * 
	 *
	 * @param recNumber 收件號
	 */
	public void setRecNumber(String recNumber) {
		this.recNumber = recNumber;
	}

	/**
	 * 權利價值說明<br>
	 * 
	 * @return String
	 */
	public String getRightsNote() {
		return this.rightsNote == null ? "" : this.rightsNote;
	}

	/**
	 * 權利價值說明<br>
	 * 
	 *
	 * @param rightsNote 權利價值說明
	 */
	public void setRightsNote(String rightsNote) {
		this.rightsNote = rightsNote;
	}

	/**
	 * 擔保債權總金額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getSecuredTotal() {
		return this.securedTotal;
	}

	/**
	 * 擔保債權總金額<br>
	 * 
	 *
	 * @param securedTotal 擔保債權總金額
	 */
	public void setSecuredTotal(BigDecimal securedTotal) {
		this.securedTotal = securedTotal;
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
		return "ClOtherRights [clOtherRightsId=" + clOtherRightsId + ", city=" + city + ", landAdm=" + landAdm + ", recYear=" + recYear + ", recWord=" + recWord + ", recNumber=" + recNumber
				+ ", rightsNote=" + rightsNote + ", securedTotal=" + securedTotal + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo="
				+ lastUpdateEmpNo + "]";
	}
}
