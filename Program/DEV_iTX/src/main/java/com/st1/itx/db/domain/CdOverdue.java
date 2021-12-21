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
 * CdOverdue 逾期新增減少原因檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CdOverdue`")
public class CdOverdue implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3089352492706998145L;

	@EmbeddedId
	private CdOverdueId cdOverdueId;

	// 逾期增減碼
	/* 1: 增加 2: 減少 */
	@Column(name = "`OverdueSign`", length = 1, insertable = false, updatable = false)
	private String overdueSign;

	// 增減原因代號
	/* 目前資料看起來取前2碼就足夠 */
	@Column(name = "`OverdueCode`", length = 4, insertable = false, updatable = false)
	private String overdueCode;

	// 增減原因說明
	@Column(name = "`OverdueItem`", length = 50)
	private String overdueItem;

	// 啟用記號
	/* Y:啟用 , N:未啟用 */
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

	public CdOverdueId getCdOverdueId() {
		return this.cdOverdueId;
	}

	public void setCdOverdueId(CdOverdueId cdOverdueId) {
		this.cdOverdueId = cdOverdueId;
	}

	/**
	 * 逾期增減碼<br>
	 * 1: 增加 2: 減少
	 * 
	 * @return String
	 */
	public String getOverdueSign() {
		return this.overdueSign == null ? "" : this.overdueSign;
	}

	/**
	 * 逾期增減碼<br>
	 * 1: 增加 2: 減少
	 *
	 * @param overdueSign 逾期增減碼
	 */
	public void setOverdueSign(String overdueSign) {
		this.overdueSign = overdueSign;
	}

	/**
	 * 增減原因代號<br>
	 * 目前資料看起來取前2碼就足夠
	 * 
	 * @return String
	 */
	public String getOverdueCode() {
		return this.overdueCode == null ? "" : this.overdueCode;
	}

	/**
	 * 增減原因代號<br>
	 * 目前資料看起來取前2碼就足夠
	 *
	 * @param overdueCode 增減原因代號
	 */
	public void setOverdueCode(String overdueCode) {
		this.overdueCode = overdueCode;
	}

	/**
	 * 增減原因說明<br>
	 * 
	 * @return String
	 */
	public String getOverdueItem() {
		return this.overdueItem == null ? "" : this.overdueItem;
	}

	/**
	 * 增減原因說明<br>
	 * 
	 *
	 * @param overdueItem 增減原因說明
	 */
	public void setOverdueItem(String overdueItem) {
		this.overdueItem = overdueItem;
	}

	/**
	 * 啟用記號<br>
	 * Y:啟用 , N:未啟用
	 * 
	 * @return String
	 */
	public String getEnable() {
		return this.enable == null ? "" : this.enable;
	}

	/**
	 * 啟用記號<br>
	 * Y:啟用 , N:未啟用
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
		return "CdOverdue [cdOverdueId=" + cdOverdueId + ", overdueItem=" + overdueItem + ", enable=" + enable + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate="
				+ lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
