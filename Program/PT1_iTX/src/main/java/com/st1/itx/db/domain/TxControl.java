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
 * TxControl 作業流程控制檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`TxControl`")
public class TxControl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5506284571410062076L;

// 控制項目
	@Id
	@Column(name = "`Code`", length = 50)
	private String code = " ";

	// 控制內容
	@Column(name = "`Desc`", length = 50)
	private String desc;

	// 建立者櫃員編號
	@Column(name = "`CreateEmpNo`", length = 6)
	private String createEmpNo;

	// 建立日期時間
	@CreatedDate
	@Column(name = "`CreateDate`")
	private java.sql.Timestamp createDate;

	// 修改者櫃員編號
	@Column(name = "`LastUpdateEmpNo`", length = 6)
	private String lastUpdateEmpNo;

	// 修改日期時間
	@LastModifiedDate
	@Column(name = "`LastUpdate`")
	private java.sql.Timestamp lastUpdate;

	/**
	 * 控制項目<br>
	 * 
	 * @return String
	 */
	public String getCode() {
		return this.code == null ? "" : this.code;
	}

	/**
	 * 控制項目<br>
	 * 
	 *
	 * @param code 控制項目
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 控制內容<br>
	 * 
	 * @return String
	 */
	public String getDesc() {
		return this.desc == null ? "" : this.desc;
	}

	/**
	 * 控制內容<br>
	 * 
	 *
	 * @param desc 控制內容
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * 建立者櫃員編號<br>
	 * 
	 * @return String
	 */
	public String getCreateEmpNo() {
		return this.createEmpNo == null ? "" : this.createEmpNo;
	}

	/**
	 * 建立者櫃員編號<br>
	 * 
	 *
	 * @param createEmpNo 建立者櫃員編號
	 */
	public void setCreateEmpNo(String createEmpNo) {
		this.createEmpNo = createEmpNo;
	}

	/**
	 * 建立日期時間<br>
	 * 
	 * @return java.sql.Timestamp
	 */
	public java.sql.Timestamp getCreateDate() {
		return this.createDate;
	}

	/**
	 * 建立日期時間<br>
	 * 
	 *
	 * @param createDate 建立日期時間
	 */
	public void setCreateDate(java.sql.Timestamp createDate) {
		this.createDate = createDate;
	}

	/**
	 * 修改者櫃員編號<br>
	 * 
	 * @return String
	 */
	public String getLastUpdateEmpNo() {
		return this.lastUpdateEmpNo == null ? "" : this.lastUpdateEmpNo;
	}

	/**
	 * 修改者櫃員編號<br>
	 * 
	 *
	 * @param lastUpdateEmpNo 修改者櫃員編號
	 */
	public void setLastUpdateEmpNo(String lastUpdateEmpNo) {
		this.lastUpdateEmpNo = lastUpdateEmpNo;
	}

	/**
	 * 修改日期時間<br>
	 * 
	 * @return java.sql.Timestamp
	 */
	public java.sql.Timestamp getLastUpdate() {
		return this.lastUpdate;
	}

	/**
	 * 修改日期時間<br>
	 * 
	 *
	 * @param lastUpdate 修改日期時間
	 */
	public void setLastUpdate(java.sql.Timestamp lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	@Override
	public String toString() {
		return "TxControl [code=" + code + ", desc=" + desc + ", createEmpNo=" + createEmpNo + ", createDate=" + createDate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + ", lastUpdate=" + lastUpdate
				+ "]";
	}
}
