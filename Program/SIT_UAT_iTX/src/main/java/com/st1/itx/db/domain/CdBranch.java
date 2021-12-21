package com.st1.itx.db.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.Id;
import javax.persistence.Column;

/**
 * CdBranch 營業單位資料檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CdBranch`")
public class CdBranch implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2383536561283872723L;

// 單位別
	/*
	 * 0000:放款部0025:信託部 0036:台中 0047:高雄 0058:台南 0069:中壢 0070:花蓮 0081:板橋 0092:東台北
	 * 0106:鳳山 0117:新竹
	 */
	@Id
	@Column(name = "`BranchNo`", length = 4)
	private String branchNo = " ";

	// 核心會計單位別
	@Column(name = "`AcBranchNo`", length = 4)
	private String acBranchNo;

	// 總分處
	@Column(name = "`CRH`", length = 2)
	private String cRH;

	// 單位控制碼
	@Column(name = "`BranchStatusCode`", length = 1)
	private String branchStatusCode;

	// 單位簡稱
	@Column(name = "`BranchShort`", length = 14)
	private String branchShort;

	// 單位全名
	@Column(name = "`BranchItem`", length = 40)
	private String branchItem;

	// 單位住址1
	@Column(name = "`BranchAddress1`", length = 30)
	private String branchAddress1;

	// 單位住址2
	@Column(name = "`BranchAddress2`", length = 30)
	private String branchAddress2;

	// 郵遞區號前三碼
	@Column(name = "`Zip3`", length = 3)
	private String zip3;

	// 郵遞區號後兩碼
	@Column(name = "`Zip2`", length = 2)
	private String zip2;

	// 負責人
	@Column(name = "`Owner`", length = 14)
	private String owner;

	// 營利統一編號
	@Column(name = "`BusinessID`", length = 10)
	private String businessID;

	// 稽徵機關代號
	/* RSO:Revenue Service Office */
	@Column(name = "`RSOCode`", length = 3)
	private String rSOCode;

	// 媒體單位代號
	@Column(name = "`MediaUnitCode`", length = 4)
	private String mediaUnitCode;

	// CIF KEY
	@Column(name = "`CIFKey`", length = 6)
	private String cIFKey;

	// 最終戶號
	@Column(name = "`LastestCustNo`", length = 7)
	private String lastestCustNo;

	// 課組別1
	/* 停用,改記錄於CdBranchGroup */
	@Column(name = "`Group1`", length = 10)
	private String group1;

	// 課組別2
	/* 停用,改記錄於CdBranchGroup */
	@Column(name = "`Group2`", length = 10)
	private String group2;

	// 課組別3
	/* 停用,改記錄於CdBranchGroup */
	@Column(name = "`Group3`", length = 10)
	private String group3;

	// 課組別4
	/* 停用,改記錄於CdBranchGroup */
	@Column(name = "`Group4`", length = 10)
	private String group4;

	// 課組別5
	/* 停用,改記錄於CdBranchGroup */
	@Column(name = "`Group5`", length = 10)
	private String group5;

	// 課組別6
	/* 停用,改記錄於CdBranchGroup */
	@Column(name = "`Group6`", length = 10)
	private String group6;

	// 課組別7
	/* 停用,改記錄於CdBranchGroup */
	@Column(name = "`Group7`", length = 10)
	private String group7;

	// 課組別8
	/* 停用,改記錄於CdBranchGroup */
	@Column(name = "`Group8`", length = 10)
	private String group8;

	// 課組別9
	/* 停用,改記錄於CdBranchGroup */
	@Column(name = "`Group9`", length = 10)
	private String group9;

	// 課組別10
	/* 停用,改記錄於CdBranchGroup */
	@Column(name = "`Group10`", length = 10)
	private String group10;

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

	/**
	 * 單位別<br>
	 * 0000:放款部 0025:信託部 0036:台中 0047:高雄 0058:台南 0069:中壢 0070:花蓮 0081:板橋 0092:東台北
	 * 0106:鳳山 0117:新竹
	 * 
	 * @return String
	 */
	public String getBranchNo() {
		return this.branchNo == null ? "" : this.branchNo;
	}

	/**
	 * 單位別<br>
	 * 0000:放款部 0025:信託部 0036:台中 0047:高雄 0058:台南 0069:中壢 0070:花蓮 0081:板橋 0092:東台北
	 * 0106:鳳山 0117:新竹
	 *
	 * @param branchNo 單位別
	 */
	public void setBranchNo(String branchNo) {
		this.branchNo = branchNo;
	}

	/**
	 * 核心會計單位別<br>
	 * 
	 * @return String
	 */
	public String getAcBranchNo() {
		return this.acBranchNo == null ? "" : this.acBranchNo;
	}

	/**
	 * 核心會計單位別<br>
	 * 
	 *
	 * @param acBranchNo 核心會計單位別
	 */
	public void setAcBranchNo(String acBranchNo) {
		this.acBranchNo = acBranchNo;
	}

	/**
	 * 總分處<br>
	 * 
	 * @return String
	 */
	public String getCRH() {
		return this.cRH == null ? "" : this.cRH;
	}

	/**
	 * 總分處<br>
	 * 
	 *
	 * @param cRH 總分處
	 */
	public void setCRH(String cRH) {
		this.cRH = cRH;
	}

	/**
	 * 單位控制碼<br>
	 * 
	 * @return String
	 */
	public String getBranchStatusCode() {
		return this.branchStatusCode == null ? "" : this.branchStatusCode;
	}

	/**
	 * 單位控制碼<br>
	 * 
	 *
	 * @param branchStatusCode 單位控制碼
	 */
	public void setBranchStatusCode(String branchStatusCode) {
		this.branchStatusCode = branchStatusCode;
	}

	/**
	 * 單位簡稱<br>
	 * 
	 * @return String
	 */
	public String getBranchShort() {
		return this.branchShort == null ? "" : this.branchShort;
	}

	/**
	 * 單位簡稱<br>
	 * 
	 *
	 * @param branchShort 單位簡稱
	 */
	public void setBranchShort(String branchShort) {
		this.branchShort = branchShort;
	}

	/**
	 * 單位全名<br>
	 * 
	 * @return String
	 */
	public String getBranchItem() {
		return this.branchItem == null ? "" : this.branchItem;
	}

	/**
	 * 單位全名<br>
	 * 
	 *
	 * @param branchItem 單位全名
	 */
	public void setBranchItem(String branchItem) {
		this.branchItem = branchItem;
	}

	/**
	 * 單位住址1<br>
	 * 
	 * @return String
	 */
	public String getBranchAddress1() {
		return this.branchAddress1 == null ? "" : this.branchAddress1;
	}

	/**
	 * 單位住址1<br>
	 * 
	 *
	 * @param branchAddress1 單位住址1
	 */
	public void setBranchAddress1(String branchAddress1) {
		this.branchAddress1 = branchAddress1;
	}

	/**
	 * 單位住址2<br>
	 * 
	 * @return String
	 */
	public String getBranchAddress2() {
		return this.branchAddress2 == null ? "" : this.branchAddress2;
	}

	/**
	 * 單位住址2<br>
	 * 
	 *
	 * @param branchAddress2 單位住址2
	 */
	public void setBranchAddress2(String branchAddress2) {
		this.branchAddress2 = branchAddress2;
	}

	/**
	 * 郵遞區號前三碼<br>
	 * 
	 * @return String
	 */
	public String getZip3() {
		return this.zip3 == null ? "" : this.zip3;
	}

	/**
	 * 郵遞區號前三碼<br>
	 * 
	 *
	 * @param zip3 郵遞區號前三碼
	 */
	public void setZip3(String zip3) {
		this.zip3 = zip3;
	}

	/**
	 * 郵遞區號後兩碼<br>
	 * 
	 * @return String
	 */
	public String getZip2() {
		return this.zip2 == null ? "" : this.zip2;
	}

	/**
	 * 郵遞區號後兩碼<br>
	 * 
	 *
	 * @param zip2 郵遞區號後兩碼
	 */
	public void setZip2(String zip2) {
		this.zip2 = zip2;
	}

	/**
	 * 負責人<br>
	 * 
	 * @return String
	 */
	public String getOwner() {
		return this.owner == null ? "" : this.owner;
	}

	/**
	 * 負責人<br>
	 * 
	 *
	 * @param owner 負責人
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * 營利統一編號<br>
	 * 
	 * @return String
	 */
	public String getBusinessID() {
		return this.businessID == null ? "" : this.businessID;
	}

	/**
	 * 營利統一編號<br>
	 * 
	 *
	 * @param businessID 營利統一編號
	 */
	public void setBusinessID(String businessID) {
		this.businessID = businessID;
	}

	/**
	 * 稽徵機關代號<br>
	 * RSO:Revenue Service Office
	 * 
	 * @return String
	 */
	public String getRSOCode() {
		return this.rSOCode == null ? "" : this.rSOCode;
	}

	/**
	 * 稽徵機關代號<br>
	 * RSO:Revenue Service Office
	 *
	 * @param rSOCode 稽徵機關代號
	 */
	public void setRSOCode(String rSOCode) {
		this.rSOCode = rSOCode;
	}

	/**
	 * 媒體單位代號<br>
	 * 
	 * @return String
	 */
	public String getMediaUnitCode() {
		return this.mediaUnitCode == null ? "" : this.mediaUnitCode;
	}

	/**
	 * 媒體單位代號<br>
	 * 
	 *
	 * @param mediaUnitCode 媒體單位代號
	 */
	public void setMediaUnitCode(String mediaUnitCode) {
		this.mediaUnitCode = mediaUnitCode;
	}

	/**
	 * CIF KEY<br>
	 * 
	 * @return String
	 */
	public String getCIFKey() {
		return this.cIFKey == null ? "" : this.cIFKey;
	}

	/**
	 * CIF KEY<br>
	 * 
	 *
	 * @param cIFKey CIF KEY
	 */
	public void setCIFKey(String cIFKey) {
		this.cIFKey = cIFKey;
	}

	/**
	 * 最終戶號<br>
	 * 
	 * @return String
	 */
	public String getLastestCustNo() {
		return this.lastestCustNo == null ? "" : this.lastestCustNo;
	}

	/**
	 * 最終戶號<br>
	 * 
	 *
	 * @param lastestCustNo 最終戶號
	 */
	public void setLastestCustNo(String lastestCustNo) {
		this.lastestCustNo = lastestCustNo;
	}

	/**
	 * 課組別1<br>
	 * 停用,改記錄於CdBranchGroup
	 * 
	 * @return String
	 */
	public String getGroup1() {
		return this.group1 == null ? "" : this.group1;
	}

	/**
	 * 課組別1<br>
	 * 停用,改記錄於CdBranchGroup
	 *
	 * @param group1 課組別1
	 */
	public void setGroup1(String group1) {
		this.group1 = group1;
	}

	/**
	 * 課組別2<br>
	 * 停用,改記錄於CdBranchGroup
	 * 
	 * @return String
	 */
	public String getGroup2() {
		return this.group2 == null ? "" : this.group2;
	}

	/**
	 * 課組別2<br>
	 * 停用,改記錄於CdBranchGroup
	 *
	 * @param group2 課組別2
	 */
	public void setGroup2(String group2) {
		this.group2 = group2;
	}

	/**
	 * 課組別3<br>
	 * 停用,改記錄於CdBranchGroup
	 * 
	 * @return String
	 */
	public String getGroup3() {
		return this.group3 == null ? "" : this.group3;
	}

	/**
	 * 課組別3<br>
	 * 停用,改記錄於CdBranchGroup
	 *
	 * @param group3 課組別3
	 */
	public void setGroup3(String group3) {
		this.group3 = group3;
	}

	/**
	 * 課組別4<br>
	 * 停用,改記錄於CdBranchGroup
	 * 
	 * @return String
	 */
	public String getGroup4() {
		return this.group4 == null ? "" : this.group4;
	}

	/**
	 * 課組別4<br>
	 * 停用,改記錄於CdBranchGroup
	 *
	 * @param group4 課組別4
	 */
	public void setGroup4(String group4) {
		this.group4 = group4;
	}

	/**
	 * 課組別5<br>
	 * 停用,改記錄於CdBranchGroup
	 * 
	 * @return String
	 */
	public String getGroup5() {
		return this.group5 == null ? "" : this.group5;
	}

	/**
	 * 課組別5<br>
	 * 停用,改記錄於CdBranchGroup
	 *
	 * @param group5 課組別5
	 */
	public void setGroup5(String group5) {
		this.group5 = group5;
	}

	/**
	 * 課組別6<br>
	 * 停用,改記錄於CdBranchGroup
	 * 
	 * @return String
	 */
	public String getGroup6() {
		return this.group6 == null ? "" : this.group6;
	}

	/**
	 * 課組別6<br>
	 * 停用,改記錄於CdBranchGroup
	 *
	 * @param group6 課組別6
	 */
	public void setGroup6(String group6) {
		this.group6 = group6;
	}

	/**
	 * 課組別7<br>
	 * 停用,改記錄於CdBranchGroup
	 * 
	 * @return String
	 */
	public String getGroup7() {
		return this.group7 == null ? "" : this.group7;
	}

	/**
	 * 課組別7<br>
	 * 停用,改記錄於CdBranchGroup
	 *
	 * @param group7 課組別7
	 */
	public void setGroup7(String group7) {
		this.group7 = group7;
	}

	/**
	 * 課組別8<br>
	 * 停用,改記錄於CdBranchGroup
	 * 
	 * @return String
	 */
	public String getGroup8() {
		return this.group8 == null ? "" : this.group8;
	}

	/**
	 * 課組別8<br>
	 * 停用,改記錄於CdBranchGroup
	 *
	 * @param group8 課組別8
	 */
	public void setGroup8(String group8) {
		this.group8 = group8;
	}

	/**
	 * 課組別9<br>
	 * 停用,改記錄於CdBranchGroup
	 * 
	 * @return String
	 */
	public String getGroup9() {
		return this.group9 == null ? "" : this.group9;
	}

	/**
	 * 課組別9<br>
	 * 停用,改記錄於CdBranchGroup
	 *
	 * @param group9 課組別9
	 */
	public void setGroup9(String group9) {
		this.group9 = group9;
	}

	/**
	 * 課組別10<br>
	 * 停用,改記錄於CdBranchGroup
	 * 
	 * @return String
	 */
	public String getGroup10() {
		return this.group10 == null ? "" : this.group10;
	}

	/**
	 * 課組別10<br>
	 * 停用,改記錄於CdBranchGroup
	 *
	 * @param group10 課組別10
	 */
	public void setGroup10(String group10) {
		this.group10 = group10;
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
		return "CdBranch [branchNo=" + branchNo + ", acBranchNo=" + acBranchNo + ", cRH=" + cRH + ", branchStatusCode=" + branchStatusCode + ", branchShort=" + branchShort + ", branchItem="
				+ branchItem + ", branchAddress1=" + branchAddress1 + ", branchAddress2=" + branchAddress2 + ", zip3=" + zip3 + ", zip2=" + zip2 + ", owner=" + owner + ", businessID=" + businessID
				+ ", rSOCode=" + rSOCode + ", mediaUnitCode=" + mediaUnitCode + ", cIFKey=" + cIFKey + ", lastestCustNo=" + lastestCustNo + ", group1=" + group1 + ", group2=" + group2 + ", group3="
				+ group3 + ", group4=" + group4 + ", group5=" + group5 + ", group6=" + group6 + ", group7=" + group7 + ", group8=" + group8 + ", group9=" + group9 + ", group10=" + group10
				+ ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
