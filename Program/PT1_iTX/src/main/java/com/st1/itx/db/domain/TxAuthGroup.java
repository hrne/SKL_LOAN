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
 * TxAuthGroup 權限群組檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`TxAuthGroup`")
public class TxAuthGroup implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3590419758770090972L;

// 權限群組編號
	@Id
	@Column(name = "`AuthNo`", length = 6)
	private String authNo = " ";

	// 權限群組名稱
	@Column(name = "`AuthItem`", length = 20)
	private String authItem;

	// 權限群組說明
	@Column(name = "`Desc`", length = 60)
	private String desc;

	// 使用單位別
	@Column(name = "`BranchNo`", length = 4)
	private String branchNo;

	// 櫃員等級
	/* 1.主管 3.經辦 */
	@Column(name = "`LevelFg`")
	private int levelFg = 0;

	// 狀態
	/* 0.未啟用 1:啟用 9.停用 */
	@Column(name = "`Status`")
	private int status = 0;

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
	 * 權限群組編號<br>
	 * 
	 * @return String
	 */
	public String getAuthNo() {
		return this.authNo == null ? "" : this.authNo;
	}

	/**
	 * 權限群組編號<br>
	 * 
	 *
	 * @param authNo 權限群組編號
	 */
	public void setAuthNo(String authNo) {
		this.authNo = authNo;
	}

	/**
	 * 權限群組名稱<br>
	 * 
	 * @return String
	 */
	public String getAuthItem() {
		return this.authItem == null ? "" : this.authItem;
	}

	/**
	 * 權限群組名稱<br>
	 * 
	 *
	 * @param authItem 權限群組名稱
	 */
	public void setAuthItem(String authItem) {
		this.authItem = authItem;
	}

	/**
	 * 權限群組說明<br>
	 * 
	 * @return String
	 */
	public String getDesc() {
		return this.desc == null ? "" : this.desc;
	}

	/**
	 * 權限群組說明<br>
	 * 
	 *
	 * @param desc 權限群組說明
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * 使用單位別<br>
	 * 
	 * @return String
	 */
	public String getBranchNo() {
		return this.branchNo == null ? "" : this.branchNo;
	}

	/**
	 * 使用單位別<br>
	 * 
	 *
	 * @param branchNo 使用單位別
	 */
	public void setBranchNo(String branchNo) {
		this.branchNo = branchNo;
	}

	/**
	 * 櫃員等級<br>
	 * 1.主管 3.經辦
	 * 
	 * @return Integer
	 */
	public int getLevelFg() {
		return this.levelFg;
	}

	/**
	 * 櫃員等級<br>
	 * 1.主管 3.經辦
	 *
	 * @param levelFg 櫃員等級
	 */
	public void setLevelFg(int levelFg) {
		this.levelFg = levelFg;
	}

	/**
	 * 狀態<br>
	 * 0.未啟用 1:啟用 9.停用
	 * 
	 * @return Integer
	 */
	public int getStatus() {
		return this.status;
	}

	/**
	 * 狀態<br>
	 * 0.未啟用 1:啟用 9.停用
	 *
	 * @param status 狀態
	 */
	public void setStatus(int status) {
		this.status = status;
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
		return "TxAuthGroup [authNo=" + authNo + ", authItem=" + authItem + ", desc=" + desc + ", branchNo=" + branchNo + ", levelFg=" + levelFg + ", status=" + status + ", createDate=" + createDate
				+ ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
