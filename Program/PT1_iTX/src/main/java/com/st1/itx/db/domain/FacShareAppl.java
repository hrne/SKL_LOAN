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
 * FacShareAppl 共同借款人資料檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`FacShareAppl`")
public class FacShareAppl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6290559835941195671L;

// 核准號碼
	@Id
	@Column(name = "`ApplNo`")
	private int applNo = 0;

	// 主核准號碼(登錄順序=1)
	@Column(name = "`MainApplNo`")
	private int mainApplNo = 0;

	// 戶號
	@Column(name = "`CustNo`")
	private int custNo = 0;

	// 額度
	@Column(name = "`FacmNo`")
	private int facmNo = 0;

	// 登錄順序(由1起編)
	@Column(name = "`KeyinSeq`")
	private int keyinSeq = 0;

	// 是否合併申報(Y/N)
	/* 固定為Y，合併申報時至第一筆登錄戶號申報 */
	@Column(name = "`JcicMergeFlag`", length = 1)
	private String jcicMergeFlag;

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
	 * 主核准號碼(登錄順序=1)<br>
	 * 
	 * @return Integer
	 */
	public int getMainApplNo() {
		return this.mainApplNo;
	}

	/**
	 * 主核准號碼(登錄順序=1)<br>
	 * 
	 *
	 * @param mainApplNo 主核准號碼(登錄順序=1)
	 */
	public void setMainApplNo(int mainApplNo) {
		this.mainApplNo = mainApplNo;
	}

	/**
	 * 戶號<br>
	 * 
	 * @return Integer
	 */
	public int getCustNo() {
		return this.custNo;
	}

	/**
	 * 戶號<br>
	 * 
	 *
	 * @param custNo 戶號
	 */
	public void setCustNo(int custNo) {
		this.custNo = custNo;
	}

	/**
	 * 額度<br>
	 * 
	 * @return Integer
	 */
	public int getFacmNo() {
		return this.facmNo;
	}

	/**
	 * 額度<br>
	 * 
	 *
	 * @param facmNo 額度
	 */
	public void setFacmNo(int facmNo) {
		this.facmNo = facmNo;
	}

	/**
	 * 登錄順序(由1起編)<br>
	 * 
	 * @return Integer
	 */
	public int getKeyinSeq() {
		return this.keyinSeq;
	}

	/**
	 * 登錄順序(由1起編)<br>
	 * 
	 *
	 * @param keyinSeq 登錄順序(由1起編)
	 */
	public void setKeyinSeq(int keyinSeq) {
		this.keyinSeq = keyinSeq;
	}

	/**
	 * 是否合併申報(Y/N)<br>
	 * 固定為Y，合併申報時至第一筆登錄戶號申報
	 * 
	 * @return String
	 */
	public String getJcicMergeFlag() {
		return this.jcicMergeFlag == null ? "" : this.jcicMergeFlag;
	}

	/**
	 * 是否合併申報(Y/N)<br>
	 * 固定為Y，合併申報時至第一筆登錄戶號申報
	 *
	 * @param jcicMergeFlag 是否合併申報(Y/N)
	 */
	public void setJcicMergeFlag(String jcicMergeFlag) {
		this.jcicMergeFlag = jcicMergeFlag;
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
		return "FacShareAppl [applNo=" + applNo + ", mainApplNo=" + mainApplNo + ", custNo=" + custNo + ", facmNo=" + facmNo + ", keyinSeq=" + keyinSeq + ", jcicMergeFlag=" + jcicMergeFlag
				+ ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
