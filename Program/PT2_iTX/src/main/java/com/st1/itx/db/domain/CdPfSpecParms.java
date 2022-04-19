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
 * CdPfSpecParms 業績計算特殊參數設定檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CdPfSpecParms`")
public class CdPfSpecParms implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 556892709707454225L;

	@EmbeddedId
	private CdPfSpecParmsId cdPfSpecParmsId;

	// 條件記號
	/* 1:計算介紹人業績時排除商品別2:計算介紹人業績時排徐部門別 */
	@Column(name = "`ConditionCode`", length = 1, insertable = false, updatable = false)
	private String conditionCode;

	// 標準條件
	/* 條件記號=1，寫入商品別代號條件記號=2，寫入部門別代號 */
	@Column(name = "`Condition`", length = 6, insertable = false, updatable = false)
	private String condition;

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

	public CdPfSpecParmsId getCdPfSpecParmsId() {
		return this.cdPfSpecParmsId;
	}

	public void setCdPfSpecParmsId(CdPfSpecParmsId cdPfSpecParmsId) {
		this.cdPfSpecParmsId = cdPfSpecParmsId;
	}

	/**
	 * 條件記號<br>
	 * 1:計算介紹人業績時排除商品別 2:計算介紹人業績時排徐部門別
	 * 
	 * @return String
	 */
	public String getConditionCode() {
		return this.conditionCode == null ? "" : this.conditionCode;
	}

	/**
	 * 條件記號<br>
	 * 1:計算介紹人業績時排除商品別 2:計算介紹人業績時排徐部門別
	 *
	 * @param conditionCode 條件記號
	 */
	public void setConditionCode(String conditionCode) {
		this.conditionCode = conditionCode;
	}

	/**
	 * 標準條件<br>
	 * 條件記號=1，寫入商品別代號 條件記號=2，寫入部門別代號
	 * 
	 * @return String
	 */
	public String getCondition() {
		return this.condition == null ? "" : this.condition;
	}

	/**
	 * 標準條件<br>
	 * 條件記號=1，寫入商品別代號 條件記號=2，寫入部門別代號
	 *
	 * @param condition 標準條件
	 */
	public void setCondition(String condition) {
		this.condition = condition;
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
		return "CdPfSpecParms [cdPfSpecParmsId=" + cdPfSpecParmsId + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo="
				+ lastUpdateEmpNo + "]";
	}
}
