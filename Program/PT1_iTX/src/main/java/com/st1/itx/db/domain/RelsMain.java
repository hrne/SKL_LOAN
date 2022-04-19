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
 * RelsMain (準)利害關係人主檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`RelsMain`")
public class RelsMain implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5294750172330228551L;

	// (準)利害關係人識別碼
	@Id
	@Column(name = "`RelsUKey`", length = 32)
	private String relsUKey = " ";

	// (準)利害關係人身分證字號
	@Column(name = "`RelsId`", length = 10)
	private String relsId;

	// (準)利害關係人姓名
	@Column(name = "`RelsName`", length = 100)
	private String relsName;

	// (準)利害關係人職稱
	/*
	 * 共用代碼檔01: 董事長02: 副董事長03: 董事04: 監察人05: 總經理06: 副總經理07: 協理08: 經理09: 副理10:
	 * 辦理授信職員11: 十五日薪98: 其他關係人99: 非關係人
	 */
	@Column(name = "`RelsCode`", length = 2)
	private String relsCode;

	// 戶別
	/* 1:自然人2:法人 */
	@Column(name = "`RelsType`")
	private int relsType = 0;

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
	 * (準)利害關係人識別碼<br>
	 * 
	 * @return String
	 */
	public String getRelsUKey() {
		return this.relsUKey == null ? "" : this.relsUKey;
	}

	/**
	 * (準)利害關係人識別碼<br>
	 * 
	 *
	 * @param relsUKey (準)利害關係人識別碼
	 */
	public void setRelsUKey(String relsUKey) {
		this.relsUKey = relsUKey;
	}

	/**
	 * (準)利害關係人身分證字號<br>
	 * 
	 * @return String
	 */
	public String getRelsId() {
		return this.relsId == null ? "" : this.relsId;
	}

	/**
	 * (準)利害關係人身分證字號<br>
	 * 
	 *
	 * @param relsId (準)利害關係人身分證字號
	 */
	public void setRelsId(String relsId) {
		this.relsId = relsId;
	}

	/**
	 * (準)利害關係人姓名<br>
	 * 
	 * @return String
	 */
	public String getRelsName() {
		return this.relsName == null ? "" : this.relsName;
	}

	/**
	 * (準)利害關係人姓名<br>
	 * 
	 *
	 * @param relsName (準)利害關係人姓名
	 */
	public void setRelsName(String relsName) {
		this.relsName = relsName;
	}

	/**
	 * (準)利害關係人職稱<br>
	 * 共用代碼檔 01: 董事長 02: 副董事長 03: 董事 04: 監察人 05: 總經理 06: 副總經理 07: 協理 08: 經理 09: 副理
	 * 10: 辦理授信職員 11: 十五日薪 98: 其他關係人 99: 非關係人
	 * 
	 * @return String
	 */
	public String getRelsCode() {
		return this.relsCode == null ? "" : this.relsCode;
	}

	/**
	 * (準)利害關係人職稱<br>
	 * 共用代碼檔 01: 董事長 02: 副董事長 03: 董事 04: 監察人 05: 總經理 06: 副總經理 07: 協理 08: 經理 09: 副理
	 * 10: 辦理授信職員 11: 十五日薪 98: 其他關係人 99: 非關係人
	 *
	 * @param relsCode (準)利害關係人職稱
	 */
	public void setRelsCode(String relsCode) {
		this.relsCode = relsCode;
	}

	/**
	 * 戶別<br>
	 * 1:自然人 2:法人
	 * 
	 * @return Integer
	 */
	public int getRelsType() {
		return this.relsType;
	}

	/**
	 * 戶別<br>
	 * 1:自然人 2:法人
	 *
	 * @param relsType 戶別
	 */
	public void setRelsType(int relsType) {
		this.relsType = relsType;
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
		return "RelsMain [relsUKey=" + relsUKey + ", relsId=" + relsId + ", relsName=" + relsName + ", relsCode=" + relsCode + ", relsType=" + relsType + ", createDate=" + createDate
				+ ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
