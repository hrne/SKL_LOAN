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
 * FacProdBreach 商品參數副檔清償金類型<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`FacProdBreach`")
public class FacProdBreach implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5562493010919973004L;

	@EmbeddedId
	private FacProdBreachId facProdBreachId;

	// 商品代碼或戶號+額度編號
	@Column(name = "`BreachNo`", length = 10, insertable = false, updatable = false)
	private String breachNo;

	// 違約適用方式
	@Column(name = "`BreachCode`", length = 3, insertable = false, updatable = false)
	private String breachCode;

	// 月數(含)以上
	@Column(name = "`MonthStart`", insertable = false, updatable = false)
	private int monthStart = 0;

	// 月數(不含)以下
	@Column(name = "`MonthEnd`")
	private int monthEnd = 0;

	// 違約金百分比
	@Column(name = "`BreachPercent`")
	private BigDecimal breachPercent = new BigDecimal("0");

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

	public FacProdBreachId getFacProdBreachId() {
		return this.facProdBreachId;
	}

	public void setFacProdBreachId(FacProdBreachId facProdBreachId) {
		this.facProdBreachId = facProdBreachId;
	}

	/**
	 * 商品代碼或戶號+額度編號<br>
	 * 
	 * @return String
	 */
	public String getBreachNo() {
		return this.breachNo == null ? "" : this.breachNo;
	}

	/**
	 * 商品代碼或戶號+額度編號<br>
	 * 
	 *
	 * @param breachNo 商品代碼或戶號+額度編號
	 */
	public void setBreachNo(String breachNo) {
		this.breachNo = breachNo;
	}

	/**
	 * 違約適用方式<br>
	 * 
	 * @return String
	 */
	public String getBreachCode() {
		return this.breachCode == null ? "" : this.breachCode;
	}

	/**
	 * 違約適用方式<br>
	 * 
	 *
	 * @param breachCode 違約適用方式
	 */
	public void setBreachCode(String breachCode) {
		this.breachCode = breachCode;
	}

	/**
	 * 月數(含)以上<br>
	 * 
	 * @return Integer
	 */
	public int getMonthStart() {
		return this.monthStart;
	}

	/**
	 * 月數(含)以上<br>
	 * 
	 *
	 * @param monthStart 月數(含)以上
	 */
	public void setMonthStart(int monthStart) {
		this.monthStart = monthStart;
	}

	/**
	 * 月數(不含)以下<br>
	 * 
	 * @return Integer
	 */
	public int getMonthEnd() {
		return this.monthEnd;
	}

	/**
	 * 月數(不含)以下<br>
	 * 
	 *
	 * @param monthEnd 月數(不含)以下
	 */
	public void setMonthEnd(int monthEnd) {
		this.monthEnd = monthEnd;
	}

	/**
	 * 違約金百分比<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getBreachPercent() {
		return this.breachPercent;
	}

	/**
	 * 違約金百分比<br>
	 * 
	 *
	 * @param breachPercent 違約金百分比
	 */
	public void setBreachPercent(BigDecimal breachPercent) {
		this.breachPercent = breachPercent;
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
		return "FacProdBreach [facProdBreachId=" + facProdBreachId + ", monthEnd=" + monthEnd + ", breachPercent=" + breachPercent + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
				+ ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
