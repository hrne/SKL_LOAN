package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * PfCoOfficer 協辦人員等級檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class PfCoOfficerId implements Serializable {


  // 員工代號
  @Column(name = "`EmpNo`", length = 6)
  private String empNo = " ";

  // 生效日期
  @Column(name = "`EffectiveDate`")
  private int effectiveDate = 0;

  public PfCoOfficerId() {
  }

  public PfCoOfficerId(String empNo, int effectiveDate) {
    this.empNo = empNo;
    this.effectiveDate = effectiveDate;
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
	* 生效日期<br>
	* 
	* @return Integer
	*/
  public int getEffectiveDate() {
    return  StaticTool.bcToRoc(this.effectiveDate);
  }

/**
	* 生效日期<br>
	* 
  *
  * @param effectiveDate 生效日期
  * @throws LogicException when Date Is Warn	*/
  public void setEffectiveDate(int effectiveDate) throws LogicException {
    this.effectiveDate = StaticTool.rocToBc(effectiveDate);
  }


  @Override
  public int hashCode() {
    return Objects.hash(empNo, effectiveDate);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    PfCoOfficerId pfCoOfficerId = (PfCoOfficerId) obj;
    return empNo.equals(pfCoOfficerId.empNo) && effectiveDate == pfCoOfficerId.effectiveDate;
  }

  @Override
  public String toString() {
    return "PfCoOfficerId [empNo=" + empNo + ", effectiveDate=" + effectiveDate + "]";
  }
}
