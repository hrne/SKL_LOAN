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
 * FacShareSub 共用額度副檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`FacShareSub`")
public class FacShareSub implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6688957223140762406L;

	@EmbeddedId
	private FacShareSubId facShareSubId;

	// 共用戶號
	@Column(name = "`ShareCustNo`", insertable = false, updatable = false)
	private int shareCustNo = 0;

	// 共用額度
	@Column(name = "`ShareFacmNo`", insertable = false, updatable = false)
	private int shareFacmNo = 0;

	// 主要戶號
	@Column(name = "`MainCustNo`")
	private int mainCustNo = 0;

	// 主要額度
	@Column(name = "`MainFacmNo`")
	private int mainFacmNo = 0;

	// 共用序號(0-主要戶號、共用戶號由1續編)
	@Column(name = "`ShareSeq`")
	private int shareSeq = 0;

	// 主要案件編號
	@Column(name = "`CreditSysNo`")
	private int creditSysNo = 0;

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

	public FacShareSubId getFacShareSubId() {
		return this.facShareSubId;
	}

	public void setFacShareSubId(FacShareSubId facShareSubId) {
		this.facShareSubId = facShareSubId;
	}

	/**
	 * 共用戶號<br>
	 * 
	 * @return Integer
	 */
	public int getShareCustNo() {
		return this.shareCustNo;
	}

	/**
	 * 共用戶號<br>
	 * 
	 *
	 * @param shareCustNo 共用戶號
	 */
	public void setShareCustNo(int shareCustNo) {
		this.shareCustNo = shareCustNo;
	}

	/**
	 * 共用額度<br>
	 * 
	 * @return Integer
	 */
	public int getShareFacmNo() {
		return this.shareFacmNo;
	}

	/**
	 * 共用額度<br>
	 * 
	 *
	 * @param shareFacmNo 共用額度
	 */
	public void setShareFacmNo(int shareFacmNo) {
		this.shareFacmNo = shareFacmNo;
	}

	/**
	 * 主要戶號<br>
	 * 
	 * @return Integer
	 */
	public int getMainCustNo() {
		return this.mainCustNo;
	}

	/**
	 * 主要戶號<br>
	 * 
	 *
	 * @param mainCustNo 主要戶號
	 */
	public void setMainCustNo(int mainCustNo) {
		this.mainCustNo = mainCustNo;
	}

	/**
	 * 主要額度<br>
	 * 
	 * @return Integer
	 */
	public int getMainFacmNo() {
		return this.mainFacmNo;
	}

	/**
	 * 主要額度<br>
	 * 
	 *
	 * @param mainFacmNo 主要額度
	 */
	public void setMainFacmNo(int mainFacmNo) {
		this.mainFacmNo = mainFacmNo;
	}

	/**
	 * 共用序號(0-主要戶號、共用戶號由1續編)<br>
	 * 
	 * @return Integer
	 */
	public int getShareSeq() {
		return this.shareSeq;
	}

	/**
	 * 共用序號(0-主要戶號、共用戶號由1續編)<br>
	 * 
	 *
	 * @param shareSeq 共用序號(0-主要戶號、共用戶號由1續編)
	 */
	public void setShareSeq(int shareSeq) {
		this.shareSeq = shareSeq;
	}

	/**
	 * 主要案件編號<br>
	 * 
	 * @return Integer
	 */
	public int getCreditSysNo() {
		return this.creditSysNo;
	}

	/**
	 * 主要案件編號<br>
	 * 
	 *
	 * @param creditSysNo 主要案件編號
	 */
	public void setCreditSysNo(int creditSysNo) {
		this.creditSysNo = creditSysNo;
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
		return "FacShareSub [facShareSubId=" + facShareSubId + ", mainCustNo=" + mainCustNo + ", mainFacmNo=" + mainFacmNo + ", shareSeq=" + shareSeq + ", creditSysNo=" + creditSysNo + ", createDate="
				+ createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
