package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * LifeRelEmp 人壽利關人職員檔 T07_2
(使用報表：LM013)<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class LifeRelEmpId implements Serializable {


  // 會計日期
  @Column(name = "`AcDate`")
  private int acDate = 0;

  // 職員身分證/統一編號
  @Column(name = "`EmpId`", length = 10)
  private String empId = " ";

  public LifeRelEmpId() {
  }

  public LifeRelEmpId(int acDate, String empId) {
    this.acDate = acDate;
    this.empId = empId;
  }

/**
	* 會計日期<br>
	* 
	* @return Integer
	*/
  public int getAcDate() {
    return  StaticTool.bcToRoc(this.acDate);
  }

/**
	* 會計日期<br>
	* 
  *
  * @param acDate 會計日期
  * @throws LogicException when Date Is Warn	*/
  public void setAcDate(int acDate) throws LogicException {
    this.acDate = StaticTool.rocToBc(acDate);
  }

/**
	* 職員身分證/統一編號<br>
	* 
	* @return String
	*/
  public String getEmpId() {
    return this.empId == null ? "" : this.empId;
  }

/**
	* 職員身分證/統一編號<br>
	* 
  *
  * @param empId 職員身分證/統一編號
	*/
  public void setEmpId(String empId) {
    this.empId = empId;
  }


  @Override
  public int hashCode() {
    return Objects.hash(acDate, empId);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    LifeRelEmpId lifeRelEmpId = (LifeRelEmpId) obj;
    return acDate == lifeRelEmpId.acDate && empId.equals(lifeRelEmpId.empId);
  }

  @Override
  public String toString() {
    return "LifeRelEmpId [acDate=" + acDate + ", empId=" + empId + "]";
  }
}
