package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * HlThreeLaqhcp 房貸排行邏輯檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class HlThreeLaqhcpId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -3642705676859360182L;

// 年月日
  @Column(name = "`CalDate`", length = 10)
  private String calDate = " ";

  // 員工代號
  @Column(name = "`EmpNo`", length = 6)
  private String empNo = " ";

  // 區部代號
  @Column(name = "`BranchNo`", length = 6)
  private String branchNo = " ";

  // 部室代號
  @Column(name = "`DeptNo`", length = 6)
  private String deptNo = " ";

  // 單位代號
  @Column(name = "`UnitNo`", length = 6)
  private String unitNo = " ";

  public HlThreeLaqhcpId() {
  }

  public HlThreeLaqhcpId(String calDate, String empNo, String branchNo, String deptNo, String unitNo) {
    this.calDate = calDate;
    this.empNo = empNo;
    this.branchNo = branchNo;
    this.deptNo = deptNo;
    this.unitNo = unitNo;
  }

/**
	* 年月日<br>
	* 
	* @return String
	*/
  public String getCalDate() {
    return this.calDate == null ? "" : this.calDate;
  }

/**
	* 年月日<br>
	* 
  *
  * @param calDate 年月日
	*/
  public void setCalDate(String calDate) {
    this.calDate = calDate;
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
	* 區部代號<br>
	* 
	* @return String
	*/
  public String getBranchNo() {
    return this.branchNo == null ? "" : this.branchNo;
  }

/**
	* 區部代號<br>
	* 
  *
  * @param branchNo 區部代號
	*/
  public void setBranchNo(String branchNo) {
    this.branchNo = branchNo;
  }

/**
	* 部室代號<br>
	* 
	* @return String
	*/
  public String getDeptNo() {
    return this.deptNo == null ? "" : this.deptNo;
  }

/**
	* 部室代號<br>
	* 
  *
  * @param deptNo 部室代號
	*/
  public void setDeptNo(String deptNo) {
    this.deptNo = deptNo;
  }

/**
	* 單位代號<br>
	* 
	* @return String
	*/
  public String getUnitNo() {
    return this.unitNo == null ? "" : this.unitNo;
  }

/**
	* 單位代號<br>
	* 
  *
  * @param unitNo 單位代號
	*/
  public void setUnitNo(String unitNo) {
    this.unitNo = unitNo;
  }


  @Override
  public int hashCode() {
    return Objects.hash(calDate, empNo, branchNo, deptNo, unitNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    HlThreeLaqhcpId hlThreeLaqhcpId = (HlThreeLaqhcpId) obj;
    return calDate.equals(hlThreeLaqhcpId.calDate) && empNo.equals(hlThreeLaqhcpId.empNo) && branchNo.equals(hlThreeLaqhcpId.branchNo) && deptNo.equals(hlThreeLaqhcpId.deptNo) && unitNo.equals(hlThreeLaqhcpId.unitNo);
  }

  @Override
  public String toString() {
    return "HlThreeLaqhcpId [calDate=" + calDate + ", empNo=" + empNo + ", branchNo=" + branchNo + ", deptNo=" + deptNo + ", unitNo=" + unitNo + "]";
  }
}
