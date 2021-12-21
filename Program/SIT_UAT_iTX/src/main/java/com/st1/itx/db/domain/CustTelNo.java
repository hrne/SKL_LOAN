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
 * CustTelNo 客戶聯絡電話檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CustTelNo`")
public class CustTelNo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1586868066181137014L;

// 電話識別碼
	@Id
	@Column(name = "`TelNoUKey`", length = 32)
	private String telNoUKey = " ";

	// 客戶識別碼
	@Column(name = "`CustUKey`", length = 32)
	private String custUKey;

	// 電話種類
	/* 共用代碼檔01:公司02:住家03:手機04:傳真05:簡訊06:催收聯絡09:其他 */
	@Column(name = "`TelTypeCode`", length = 2)
	private String telTypeCode;

	// 電話區碼
	@Column(name = "`TelArea`", length = 5)
	private String telArea;

	// 電話號碼
	/* 選擇手機或簡訊則只填入此欄 */
	@Column(name = "`TelNo`", length = 10)
	private String telNo;

	// 分機號碼
	@Column(name = "`TelExt`", length = 5)
	private String telExt;

	// 異動原因
	/* 共用代碼檔01:客戶申請…… */
	@Column(name = "`TelChgRsnCode`", length = 2)
	private String telChgRsnCode;

	// 與借款人關係
	/* 聯絡人與借款戶關係共用代碼檔00:本人01:夫02:妻03:父04:母05:子06:女07:兄08:弟09:姊10:妹11:姪子99:其他 */
	@Column(name = "`RelationCode`", length = 2)
	private String relationCode;

	// 聯絡人姓名
	@Column(name = "`LiaisonName`", length = 100)
	private String liaisonName;

	// 備註
	@Column(name = "`Rmk`", length = 40)
	private String rmk;

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

	/**
	 * 電話識別碼<br>
	 * 
	 * @return String
	 */
	public String getTelNoUKey() {
		return this.telNoUKey == null ? "" : this.telNoUKey;
	}

	/**
	 * 電話識別碼<br>
	 * 
	 *
	 * @param telNoUKey 電話識別碼
	 */
	public void setTelNoUKey(String telNoUKey) {
		this.telNoUKey = telNoUKey;
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
	 * 電話種類<br>
	 * 共用代碼檔 01:公司 02:住家 03:手機 04:傳真 05:簡訊 06:催收聯絡 09:其他
	 * 
	 * @return String
	 */
	public String getTelTypeCode() {
		return this.telTypeCode == null ? "" : this.telTypeCode;
	}

	/**
	 * 電話種類<br>
	 * 共用代碼檔 01:公司 02:住家 03:手機 04:傳真 05:簡訊 06:催收聯絡 09:其他
	 *
	 * @param telTypeCode 電話種類
	 */
	public void setTelTypeCode(String telTypeCode) {
		this.telTypeCode = telTypeCode;
	}

	/**
	 * 電話區碼<br>
	 * 
	 * @return String
	 */
	public String getTelArea() {
		return this.telArea == null ? "" : this.telArea;
	}

	/**
	 * 電話區碼<br>
	 * 
	 *
	 * @param telArea 電話區碼
	 */
	public void setTelArea(String telArea) {
		this.telArea = telArea;
	}

	/**
	 * 電話號碼<br>
	 * 選擇手機或簡訊則只填入此欄
	 * 
	 * @return String
	 */
	public String getTelNo() {
		return this.telNo == null ? "" : this.telNo;
	}

	/**
	 * 電話號碼<br>
	 * 選擇手機或簡訊則只填入此欄
	 *
	 * @param telNo 電話號碼
	 */
	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}

	/**
	 * 分機號碼<br>
	 * 
	 * @return String
	 */
	public String getTelExt() {
		return this.telExt == null ? "" : this.telExt;
	}

	/**
	 * 分機號碼<br>
	 * 
	 *
	 * @param telExt 分機號碼
	 */
	public void setTelExt(String telExt) {
		this.telExt = telExt;
	}

	/**
	 * 異動原因<br>
	 * 共用代碼檔 01:客戶申請 ……
	 * 
	 * @return String
	 */
	public String getTelChgRsnCode() {
		return this.telChgRsnCode == null ? "" : this.telChgRsnCode;
	}

	/**
	 * 異動原因<br>
	 * 共用代碼檔 01:客戶申請 ……
	 *
	 * @param telChgRsnCode 異動原因
	 */
	public void setTelChgRsnCode(String telChgRsnCode) {
		this.telChgRsnCode = telChgRsnCode;
	}

	/**
	 * 與借款人關係<br>
	 * 聯絡人與借款戶關係 共用代碼檔 00:本人 01:夫 02:妻 03:父 04:母 05:子 06:女 07:兄 08:弟 09:姊 10:妹 11:姪子
	 * 99:其他
	 * 
	 * @return String
	 */
	public String getRelationCode() {
		return this.relationCode == null ? "" : this.relationCode;
	}

	/**
	 * 與借款人關係<br>
	 * 聯絡人與借款戶關係 共用代碼檔 00:本人 01:夫 02:妻 03:父 04:母 05:子 06:女 07:兄 08:弟 09:姊 10:妹 11:姪子
	 * 99:其他
	 *
	 * @param relationCode 與借款人關係
	 */
	public void setRelationCode(String relationCode) {
		this.relationCode = relationCode;
	}

	/**
	 * 聯絡人姓名<br>
	 * 
	 * @return String
	 */
	public String getLiaisonName() {
		return this.liaisonName == null ? "" : this.liaisonName;
	}

	/**
	 * 聯絡人姓名<br>
	 * 
	 *
	 * @param liaisonName 聯絡人姓名
	 */
	public void setLiaisonName(String liaisonName) {
		this.liaisonName = liaisonName;
	}

	/**
	 * 備註<br>
	 * 
	 * @return String
	 */
	public String getRmk() {
		return this.rmk == null ? "" : this.rmk;
	}

	/**
	 * 備註<br>
	 * 
	 *
	 * @param rmk 備註
	 */
	public void setRmk(String rmk) {
		this.rmk = rmk;
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
		return "CustTelNo [telNoUKey=" + telNoUKey + ", custUKey=" + custUKey + ", telTypeCode=" + telTypeCode + ", telArea=" + telArea + ", telNo=" + telNo + ", telExt=" + telExt + ", telChgRsnCode="
				+ telChgRsnCode + ", relationCode=" + relationCode + ", liaisonName=" + liaisonName + ", rmk=" + rmk + ", stopReason=" + stopReason + ", enable=" + enable + ", createDate="
				+ createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
