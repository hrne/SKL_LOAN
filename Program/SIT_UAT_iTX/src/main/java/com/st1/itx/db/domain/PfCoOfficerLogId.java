package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * PfCoOfficerLog 協辦人員等級歷程檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class PfCoOfficerLogId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 1280660411520140969L;

// 員工代號
  @Column(name = "`EmpNo`", length = 6)
  private String empNo = " ";

  // 生效日期
  @Column(name = "`EffectiveDate`")
  private int effectiveDate = 0;

  // 序號
  /* 流水號用 */
  @Column(name = "`SerialNo`")
  private int serialNo = 0;

  public PfCoOfficerLogId() {
  }

  public PfCoOfficerLogId(String empNo, int effectiveDate, int serialNo) {
    this.empNo = empNo;
    this.effectiveDate = effectiveDate;
    this.serialNo = serialNo;
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

/**
	* 序號<br>
	* 流水號用
	* @return Integer
	*/
  public int getSerialNo() {
    return this.serialNo;
  }

/**
	* 序號<br>
	* 流水號用
  *
  * @param serialNo 序號
	*/
  public void setSerialNo(int serialNo) {
    this.serialNo = serialNo;
  }


  @Override
  public int hashCode() {
    return Objects.hash(empNo, effectiveDate, serialNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    PfCoOfficerLogId pfCoOfficerLogId = (PfCoOfficerLogId) obj;
    return empNo.equals(pfCoOfficerLogId.empNo) && effectiveDate == pfCoOfficerLogId.effectiveDate && serialNo == pfCoOfficerLogId.serialNo;
  }

  @Override
  public String toString() {
    return "PfCoOfficerLogId [empNo=" + empNo + ", effectiveDate=" + effectiveDate + ", serialNo=" + serialNo + "]";
  }
}
