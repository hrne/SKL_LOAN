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
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * HlThreeLaqhcp 單位、區部、部室業績累計檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`HlThreeLaqhcp`")
public class HlThreeLaqhcp implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 7117061325269473341L;

@EmbeddedId
  private HlThreeLaqhcpId hlThreeLaqhcpId;

  // 年月日
  @Column(name = "`CalDate`")
  private int calDate = 0;

  // 員工代號
  @Column(name = "`EmpNo`", length = 6)
  private String empNo;

  // 單位代號
  @Column(name = "`UnitCode`", length = 6, insertable = false, updatable = false)
  private String unitCode;

  // 區部代號
  @Column(name = "`DistCode`", length = 6, insertable = false, updatable = false)
  private String distCode;

  // 部室代號
  @Column(name = "`DeptCode`", length = 6, insertable = false, updatable = false)
  private String deptCode;

  // 單位中文
  @Column(name = "`UnitName`", length = 20)
  private String unitName;

  // 區部中文
  @Column(name = "`DistName`", length = 20)
  private String distName;

  // 部室中文
  @Column(name = "`DeptName`", length = 20)
  private String deptName;

  // 員工姓名
  @Column(name = "`EmpName`", length = 15)
  private String empName;

  // 專員姓名
  @Column(name = "`DepartOfficer`", length = 15)
  private String departOfficer;

  // 處長主任別
  @Column(name = "`DirectorCode`", length = 1)
  private String directorCode;

  // 目標件數
  @Column(name = "`GoalNum`")
  private BigDecimal goalNum = new BigDecimal("0");

  // 目標金額
  @Column(name = "`GoalAmt`")
  private BigDecimal goalAmt = new BigDecimal("0");

  // 達成件數
  @Column(name = "`ActNum`")
  private BigDecimal actNum = new BigDecimal("0");

  // 達成金額
  @Column(name = "`ActAmt`")
  private BigDecimal actAmt = new BigDecimal("0");

  // 本月達成率
  @Column(name = "`ActRate`")
  private BigDecimal actRate = new BigDecimal("0");

  // 累計目標件數
  @Column(name = "`TGoalNum`")
  private BigDecimal tGoalNum = new BigDecimal("0");

  // 累計目標金額
  @Column(name = "`TGoalAmt`")
  private BigDecimal tGoalAmt = new BigDecimal("0");

  // 累計達成件數
  @Column(name = "`TActNum`")
  private BigDecimal tActNum = new BigDecimal("0");

  // 累計達成金額
  @Column(name = "`TActAmt`")
  private BigDecimal tActAmt = new BigDecimal("0");

  // 累計達成率
  @Column(name = "`TActRate`")
  private BigDecimal tActRate = new BigDecimal("0");

  // UpdateIdentifier
  @Column(name = "`UpNo`")
  private int upNo = 0;

  // 更新日期
  @Column(name = "`ProcessDate`")
  private int processDate = 0;

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


  public HlThreeLaqhcpId getHlThreeLaqhcpId() {
    return this.hlThreeLaqhcpId;
  }

  public void setHlThreeLaqhcpId(HlThreeLaqhcpId hlThreeLaqhcpId) {
    this.hlThreeLaqhcpId = hlThreeLaqhcpId;
  }

/**
	* 年月日<br>
	* 
	* @return Integer
	*/
  public int getCalDate() {
    return StaticTool.bcToRoc(this.calDate);
  }

/**
	* 年月日<br>
	* 
  *
  * @param calDate 年月日
  * @throws LogicException when Date Is Warn	*/
  public void setCalDate(int calDate) throws LogicException {
    this.calDate = StaticTool.rocToBc(calDate);
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
	* 單位代號<br>
	* 
	* @return String
	*/
  public String getUnitCode() {
    return this.unitCode == null ? "" : this.unitCode;
  }

/**
	* 單位代號<br>
	* 
  *
  * @param unitCode 單位代號
	*/
  public void setUnitCode(String unitCode) {
    this.unitCode = unitCode;
  }

/**
	* 區部代號<br>
	* 
	* @return String
	*/
  public String getDistCode() {
    return this.distCode == null ? "" : this.distCode;
  }

/**
	* 區部代號<br>
	* 
  *
  * @param distCode 區部代號
	*/
  public void setDistCode(String distCode) {
    this.distCode = distCode;
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
	* 單位中文<br>
	* 
	* @return String
	*/
  public String getUnitName() {
    return this.unitName == null ? "" : this.unitName;
  }

/**
	* 單位中文<br>
	* 
  *
  * @param unitName 單位中文
	*/
  public void setUnitName(String unitName) {
    this.unitName = unitName;
  }

/**
	* 區部中文<br>
	* 
	* @return String
	*/
  public String getDistName() {
    return this.distName == null ? "" : this.distName;
  }

/**
	* 區部中文<br>
	* 
  *
  * @param distName 區部中文
	*/
  public void setDistName(String distName) {
    this.distName = distName;
  }

/**
	* 部室中文<br>
	* 
	* @return String
	*/
  public String getDeptName() {
    return this.deptName == null ? "" : this.deptName;
  }

/**
	* 部室中文<br>
	* 
  *
  * @param deptName 部室中文
	*/
  public void setDeptName(String deptName) {
    this.deptName = deptName;
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
	* 目標件數<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getGoalNum() {
    return this.goalNum;
  }

/**
	* 目標件數<br>
	* 
  *
  * @param goalNum 目標件數
	*/
  public void setGoalNum(BigDecimal goalNum) {
    this.goalNum = goalNum;
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
	* 達成件數<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getActNum() {
    return this.actNum;
  }

/**
	* 達成件數<br>
	* 
  *
  * @param actNum 達成件數
	*/
  public void setActNum(BigDecimal actNum) {
    this.actNum = actNum;
  }

/**
	* 達成金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getActAmt() {
    return this.actAmt;
  }

/**
	* 達成金額<br>
	* 
  *
  * @param actAmt 達成金額
	*/
  public void setActAmt(BigDecimal actAmt) {
    this.actAmt = actAmt;
  }

/**
	* 本月達成率<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getActRate() {
    return this.actRate;
  }

/**
	* 本月達成率<br>
	* 
  *
  * @param actRate 本月達成率
	*/
  public void setActRate(BigDecimal actRate) {
    this.actRate = actRate;
  }

/**
	* 累計目標件數<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getTGoalNum() {
    return this.tGoalNum;
  }

/**
	* 累計目標件數<br>
	* 
  *
  * @param tGoalNum 累計目標件數
	*/
  public void setTGoalNum(BigDecimal tGoalNum) {
    this.tGoalNum = tGoalNum;
  }

/**
	* 累計目標金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getTGoalAmt() {
    return this.tGoalAmt;
  }

/**
	* 累計目標金額<br>
	* 
  *
  * @param tGoalAmt 累計目標金額
	*/
  public void setTGoalAmt(BigDecimal tGoalAmt) {
    this.tGoalAmt = tGoalAmt;
  }

/**
	* 累計達成件數<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getTActNum() {
    return this.tActNum;
  }

/**
	* 累計達成件數<br>
	* 
  *
  * @param tActNum 累計達成件數
	*/
  public void setTActNum(BigDecimal tActNum) {
    this.tActNum = tActNum;
  }

/**
	* 累計達成金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getTActAmt() {
    return this.tActAmt;
  }

/**
	* 累計達成金額<br>
	* 
  *
  * @param tActAmt 累計達成金額
	*/
  public void setTActAmt(BigDecimal tActAmt) {
    this.tActAmt = tActAmt;
  }

/**
	* 累計達成率<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getTActRate() {
    return this.tActRate;
  }

/**
	* 累計達成率<br>
	* 
  *
  * @param tActRate 累計達成率
	*/
  public void setTActRate(BigDecimal tActRate) {
    this.tActRate = tActRate;
  }

/**
	* UpdateIdentifier<br>
	* 
	* @return Integer
	*/
  public int getUpNo() {
    return this.upNo;
  }

/**
	* UpdateIdentifier<br>
	* 
  *
  * @param upNo UpdateIdentifier
	*/
  public void setUpNo(int upNo) {
    this.upNo = upNo;
  }

/**
	* 更新日期<br>
	* 
	* @return Integer
	*/
  public int getProcessDate() {
    return StaticTool.bcToRoc(this.processDate);
  }

/**
	* 更新日期<br>
	* 
  *
  * @param processDate 更新日期
  * @throws LogicException when Date Is Warn	*/
  public void setProcessDate(int processDate) throws LogicException {
    this.processDate = StaticTool.rocToBc(processDate);
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
    return "HlThreeLaqhcp [hlThreeLaqhcpId=" + hlThreeLaqhcpId + ", calDate=" + calDate + ", empNo=" + empNo + ", unitName=" + unitName
           + ", distName=" + distName + ", deptName=" + deptName + ", empName=" + empName + ", departOfficer=" + departOfficer + ", directorCode=" + directorCode + ", goalNum=" + goalNum
           + ", goalAmt=" + goalAmt + ", actNum=" + actNum + ", actAmt=" + actAmt + ", actRate=" + actRate + ", tGoalNum=" + tGoalNum + ", tGoalAmt=" + tGoalAmt
           + ", tActNum=" + tActNum + ", tActAmt=" + tActAmt + ", tActRate=" + tActRate + ", upNo=" + upNo + ", processDate=" + processDate + ", createDate=" + createDate
           + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
