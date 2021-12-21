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
 * PfBsOfficer 房貸專員業績目標檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`PfBsOfficer`")
public class PfBsOfficer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3301015468883252433L;

	@EmbeddedId
	private PfBsOfficerId pfBsOfficerId;

	// 年月份
	@Column(name = "`WorkMonth`", insertable = false, updatable = false)
	private int workMonth = 0;

	// 員工代號
	@Column(name = "`EmpNo`", length = 6, insertable = false, updatable = false)
	private String empNo;

	// 員工姓名
	@Column(name = "`Fullname`", length = 40)
	private String fullname;

	// 區域中心
	@Column(name = "`AreaCode`", length = 6)
	private String areaCode;

	// 中心中文
	@Column(name = "`AreaItem`", length = 12)
	private String areaItem;

	// 部室代號
	@Column(name = "`DeptCode`", length = 6)
	private String deptCode;

	// 部室中文
	@Column(name = "`DepItem`", length = 12)
	private String depItem;

	// 區部代號
	/* 可不輸入 */
	@Column(name = "`DistCode`", length = 6)
	private String distCode;

	// 區部中文
	/* 2021/4/9 調整欄位長度 */
	@Column(name = "`DistItem`", length = 30)
	private String distItem;

	// 駐在地
	/* 2021/3/30 新增欄位，預設空白 */
	@Column(name = "`StationName`", length = 30)
	private String stationName;

	// 目標金額
	@Column(name = "`GoalAmt`")
	private BigDecimal goalAmt = new BigDecimal("0");

	// 累計目標金額
	@Column(name = "`SmryGoalAmt`")
	private BigDecimal smryGoalAmt = new BigDecimal("0");

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

	public PfBsOfficerId getPfBsOfficerId() {
		return this.pfBsOfficerId;
	}

	public void setPfBsOfficerId(PfBsOfficerId pfBsOfficerId) {
		this.pfBsOfficerId = pfBsOfficerId;
	}

	/**
	 * 年月份<br>
	 * 
	 * @return Integer
	 */
	public int getWorkMonth() {
		return this.workMonth;
	}

	/**
	 * 年月份<br>
	 * 
	 *
	 * @param workMonth 年月份
	 */
	public void setWorkMonth(int workMonth) {
		this.workMonth = workMonth;
	}

	/**
	 * 員工代號<br>
	 * 
	 * @return String
	 */
	public String getEmpNo() {
		return this.empNo == null ? "" : this.empNo;
	}

	/**
	 * 員工代號<br>
	 * 
	 *
	 * @param empNo 員工代號
	 */
	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}

	/**
	 * 員工姓名<br>
	 * 
	 * @return String
	 */
	public String getFullname() {
		return this.fullname == null ? "" : this.fullname;
	}

	/**
	 * 員工姓名<br>
	 * 
	 *
	 * @param fullname 員工姓名
	 */
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	/**
	 * 區域中心<br>
	 * 
	 * @return String
	 */
	public String getAreaCode() {
		return this.areaCode == null ? "" : this.areaCode;
	}

	/**
	 * 區域中心<br>
	 * 
	 *
	 * @param areaCode 區域中心
	 */
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	/**
	 * 中心中文<br>
	 * 
	 * @return String
	 */
	public String getAreaItem() {
		return this.areaItem == null ? "" : this.areaItem;
	}

	/**
	 * 中心中文<br>
	 * 
	 *
	 * @param areaItem 中心中文
	 */
	public void setAreaItem(String areaItem) {
		this.areaItem = areaItem;
	}

	/**
	 * 部室代號<br>
	 * 
	 * @return String
	 */
	public String getDeptCode() {
		return this.deptCode == null ? "" : this.deptCode;
	}

	/**
	 * 部室代號<br>
	 * 
	 *
	 * @param deptCode 部室代號
	 */
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	/**
	 * 部室中文<br>
	 * 
	 * @return String
	 */
	public String getDepItem() {
		return this.depItem == null ? "" : this.depItem;
	}

	/**
	 * 部室中文<br>
	 * 
	 *
	 * @param depItem 部室中文
	 */
	public void setDepItem(String depItem) {
		this.depItem = depItem;
	}

	/**
	 * 區部代號<br>
	 * 可不輸入
	 * 
	 * @return String
	 */
	public String getDistCode() {
		return this.distCode == null ? "" : this.distCode;
	}

	/**
	 * 區部代號<br>
	 * 可不輸入
	 *
	 * @param distCode 區部代號
	 */
	public void setDistCode(String distCode) {
		this.distCode = distCode;
	}

	/**
	 * 區部中文<br>
	 * 2021/4/9 調整欄位長度
	 * 
	 * @return String
	 */
	public String getDistItem() {
		return this.distItem == null ? "" : this.distItem;
	}

	/**
	 * 區部中文<br>
	 * 2021/4/9 調整欄位長度
	 *
	 * @param distItem 區部中文
	 */
	public void setDistItem(String distItem) {
		this.distItem = distItem;
	}

	/**
	 * 駐在地<br>
	 * 2021/3/30 新增欄位，預設空白
	 * 
	 * @return String
	 */
	public String getStationName() {
		return this.stationName == null ? "" : this.stationName;
	}

	/**
	 * 駐在地<br>
	 * 2021/3/30 新增欄位，預設空白
	 *
	 * @param stationName 駐在地
	 */
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	/**
	 * 目標金額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getGoalAmt() {
		return this.goalAmt;
	}

	/**
	 * 目標金額<br>
	 * 
	 *
	 * @param goalAmt 目標金額
	 */
	public void setGoalAmt(BigDecimal goalAmt) {
		this.goalAmt = goalAmt;
	}

	/**
	 * 累計目標金額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getSmryGoalAmt() {
		return this.smryGoalAmt;
	}

	/**
	 * 累計目標金額<br>
	 * 
	 *
	 * @param smryGoalAmt 累計目標金額
	 */
	public void setSmryGoalAmt(BigDecimal smryGoalAmt) {
		this.smryGoalAmt = smryGoalAmt;
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
		return "PfBsOfficer [pfBsOfficerId=" + pfBsOfficerId + ", fullname=" + fullname + ", areaCode=" + areaCode + ", areaItem=" + areaItem + ", deptCode=" + deptCode + ", depItem=" + depItem
				+ ", distCode=" + distCode + ", distItem=" + distItem + ", stationName=" + stationName + ", goalAmt=" + goalAmt + ", smryGoalAmt=" + smryGoalAmt + ", createDate=" + createDate
				+ ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
