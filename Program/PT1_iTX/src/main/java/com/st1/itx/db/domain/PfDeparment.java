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
 * PfDeparment 單位、區部、部室業績目標檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`PfDeparment`")
public class PfDeparment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2802929281026394659L;

	@EmbeddedId
	private PfDeparmentId pfDeparmentId;

	// 單位代號
	/* v、 、 */
	@Column(name = "`UnitCode`", length = 6, insertable = false, updatable = false)
	private String unitCode;

	// 區部代號
	/* v、v、 */
	@Column(name = "`DistCode`", length = 6, insertable = false, updatable = false)
	private String distCode;

	// 部室代號
	/* v、v、v */
	@Column(name = "`DeptCode`", length = 6, insertable = false, updatable = false)
	private String deptCode;

	// 員工代號
	@Column(name = "`EmpNo`", length = 6)
	private String empNo;

	// 單位中文
	@Column(name = "`UnitItem`", length = 20)
	private String unitItem;

	// 區部中文
	@Column(name = "`DistItem`", length = 20)
	private String distItem;

	// 部室中文
	@Column(name = "`DeptItem`", length = 20)
	private String deptItem;

	// 處長主任別
	@Column(name = "`DirectorCode`", length = 1)
	private String directorCode;

	// 員工姓名
	@Column(name = "`EmpName`", length = 8)
	private String empName;

	// 專員姓名
	@Column(name = "`DepartOfficer`", length = 8)
	private String departOfficer;

	// 目標件數
	@Column(name = "`GoalCnt`")
	private int goalCnt = 0;

	// 累計目標件數
	@Column(name = "`SumGoalCnt`")
	private BigDecimal sumGoalCnt = new BigDecimal("0");

	// 目標金額
	@Column(name = "`GoalAmt`")
	private BigDecimal goalAmt = new BigDecimal("0");

	// 累計目標金額
	@Column(name = "`SumGoalAmt`")
	private BigDecimal sumGoalAmt = new BigDecimal("0");

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

	public PfDeparmentId getPfDeparmentId() {
		return this.pfDeparmentId;
	}

	public void setPfDeparmentId(PfDeparmentId pfDeparmentId) {
		this.pfDeparmentId = pfDeparmentId;
	}

	/**
	 * 單位代號<br>
	 * v、 、
	 * 
	 * @return String
	 */
	public String getUnitCode() {
		return this.unitCode == null ? "" : this.unitCode;
	}

	/**
	 * 單位代號<br>
	 * v、 、
	 *
	 * @param unitCode 單位代號
	 */
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	/**
	 * 區部代號<br>
	 * v、v、
	 * 
	 * @return String
	 */
	public String getDistCode() {
		return this.distCode == null ? "" : this.distCode;
	}

	/**
	 * 區部代號<br>
	 * v、v、
	 *
	 * @param distCode 區部代號
	 */
	public void setDistCode(String distCode) {
		this.distCode = distCode;
	}

	/**
	 * 部室代號<br>
	 * v、v、v
	 * 
	 * @return String
	 */
	public String getDeptCode() {
		return this.deptCode == null ? "" : this.deptCode;
	}

	/**
	 * 部室代號<br>
	 * v、v、v
	 *
	 * @param deptCode 部室代號
	 */
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
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
	 * 單位中文<br>
	 * 
	 * @return String
	 */
	public String getUnitItem() {
		return this.unitItem == null ? "" : this.unitItem;
	}

	/**
	 * 單位中文<br>
	 * 
	 *
	 * @param unitItem 單位中文
	 */
	public void setUnitItem(String unitItem) {
		this.unitItem = unitItem;
	}

	/**
	 * 區部中文<br>
	 * 
	 * @return String
	 */
	public String getDistItem() {
		return this.distItem == null ? "" : this.distItem;
	}

	/**
	 * 區部中文<br>
	 * 
	 *
	 * @param distItem 區部中文
	 */
	public void setDistItem(String distItem) {
		this.distItem = distItem;
	}

	/**
	 * 部室中文<br>
	 * 
	 * @return String
	 */
	public String getDeptItem() {
		return this.deptItem == null ? "" : this.deptItem;
	}

	/**
	 * 部室中文<br>
	 * 
	 *
	 * @param deptItem 部室中文
	 */
	public void setDeptItem(String deptItem) {
		this.deptItem = deptItem;
	}

	/**
	 * 處長主任別<br>
	 * 
	 * @return String
	 */
	public String getDirectorCode() {
		return this.directorCode == null ? "" : this.directorCode;
	}

	/**
	 * 處長主任別<br>
	 * 
	 *
	 * @param directorCode 處長主任別
	 */
	public void setDirectorCode(String directorCode) {
		this.directorCode = directorCode;
	}

	/**
	 * 員工姓名<br>
	 * 
	 * @return String
	 */
	public String getEmpName() {
		return this.empName == null ? "" : this.empName;
	}

	/**
	 * 員工姓名<br>
	 * 
	 *
	 * @param empName 員工姓名
	 */
	public void setEmpName(String empName) {
		this.empName = empName;
	}

	/**
	 * 專員姓名<br>
	 * 
	 * @return String
	 */
	public String getDepartOfficer() {
		return this.departOfficer == null ? "" : this.departOfficer;
	}

	/**
	 * 專員姓名<br>
	 * 
	 *
	 * @param departOfficer 專員姓名
	 */
	public void setDepartOfficer(String departOfficer) {
		this.departOfficer = departOfficer;
	}

	/**
	 * 目標件數<br>
	 * 
	 * @return Integer
	 */
	public int getGoalCnt() {
		return this.goalCnt;
	}

	/**
	 * 目標件數<br>
	 * 
	 *
	 * @param goalCnt 目標件數
	 */
	public void setGoalCnt(int goalCnt) {
		this.goalCnt = goalCnt;
	}

	/**
	 * 累計目標件數<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getSumGoalCnt() {
		return this.sumGoalCnt;
	}

	/**
	 * 累計目標件數<br>
	 * 
	 *
	 * @param sumGoalCnt 累計目標件數
	 */
	public void setSumGoalCnt(BigDecimal sumGoalCnt) {
		this.sumGoalCnt = sumGoalCnt;
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
	public BigDecimal getSumGoalAmt() {
		return this.sumGoalAmt;
	}

	/**
	 * 累計目標金額<br>
	 * 
	 *
	 * @param sumGoalAmt 累計目標金額
	 */
	public void setSumGoalAmt(BigDecimal sumGoalAmt) {
		this.sumGoalAmt = sumGoalAmt;
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
		return "PfDeparment [pfDeparmentId=" + pfDeparmentId + ", empNo=" + empNo + ", unitItem=" + unitItem + ", distItem=" + distItem + ", deptItem=" + deptItem + ", directorCode=" + directorCode
				+ ", empName=" + empName + ", departOfficer=" + departOfficer + ", goalCnt=" + goalCnt + ", sumGoalCnt=" + sumGoalCnt + ", goalAmt=" + goalAmt + ", sumGoalAmt=" + sumGoalAmt
				+ ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
