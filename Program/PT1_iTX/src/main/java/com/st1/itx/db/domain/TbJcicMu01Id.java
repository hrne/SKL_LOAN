package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * TbJcicMu01 聯徵人員名冊<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class TbJcicMu01Id implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -5763635857238255959L;

// 總行代號
  @Column(name = "`HeadOfficeCode`", length = 3)
  private String headOfficeCode = " ";

  // 分行代號
  @Column(name = "`BranchCode`", length = 4)
  private String branchCode = " ";

  // 員工代號
  @Column(name = "`EmpId`", length = 6)
  private String empId = " ";

  public TbJcicMu01Id() {
  }

  public TbJcicMu01Id(String headOfficeCode, String branchCode, String empId) {
    this.headOfficeCode = headOfficeCode;
    this.branchCode = branchCode;
    this.empId = empId;
  }

/**
	* 總行代號<br>
	* 
	* @return String
	*/
  public String getHeadOfficeCode() {
    return this.headOfficeCode == null ? "" : this.headOfficeCode;
  }

/**
	* 總行代號<br>
	* 
  *
  * @param headOfficeCode 總行代號
	*/
  public void setHeadOfficeCode(String headOfficeCode) {
    this.headOfficeCode = headOfficeCode;
  }

/**
	* 分行代號<br>
	* 
	* @return String
	*/
  public String getBranchCode() {
    return this.branchCode == null ? "" : this.branchCode;
  }

/**
	* 分行代號<br>
	* 
  *
  * @param branchCode 分行代號
	*/
  public void setBranchCode(String branchCode) {
    this.branchCode = branchCode;
  }

/**
	* 員工代號<br>
	* 
	* @return String
	*/
  public String getEmpId() {
    return this.empId == null ? "" : this.empId;
  }

/**
	* 員工代號<br>
	* 
  *
  * @param empId 員工代號
	*/
  public void setEmpId(String empId) {
    this.empId = empId;
  }


  @Override
  public int hashCode() {
    return Objects.hash(headOfficeCode, branchCode, empId);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    TbJcicMu01Id tbJcicMu01Id = (TbJcicMu01Id) obj;
    return headOfficeCode.equals(tbJcicMu01Id.headOfficeCode) && branchCode.equals(tbJcicMu01Id.branchCode) && empId.equals(tbJcicMu01Id.empId);
  }

  @Override
  public String toString() {
    return "TbJcicMu01Id [headOfficeCode=" + headOfficeCode + ", branchCode=" + branchCode + ", empId=" + empId + "]";
  }
}
