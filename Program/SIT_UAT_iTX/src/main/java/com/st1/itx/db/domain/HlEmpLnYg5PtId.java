package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * HlEmpLnYg5Pt 員工目標檔案<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class HlEmpLnYg5PtId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 7843193807541467532L;

// 年月份
  @Column(name = "`WorkYM`", length = 10)
  private String workYM = " ";

  // 單位代號
  @Column(name = "`AreaUnitNo`", length = 6)
  private String areaUnitNo = " ";

  // 員工代號
  @Column(name = "`HlEmpNo`", length = 6)
  private String hlEmpNo = " ";

  public HlEmpLnYg5PtId() {
  }

  public HlEmpLnYg5PtId(String workYM, String areaUnitNo, String hlEmpNo) {
    this.workYM = workYM;
    this.areaUnitNo = areaUnitNo;
    this.hlEmpNo = hlEmpNo;
  }

/**
	* 年月份<br>
	* 
	* @return String
	*/
  public String getWorkYM() {
    return this.workYM == null ? "" : this.workYM;
  }

/**
	* 年月份<br>
	* 
  *
  * @param workYM 年月份
	*/
  public void setWorkYM(String workYM) {
    this.workYM = workYM;
  }

/**
	* 單位代號<br>
	* 
	* @return String
	*/
  public String getAreaUnitNo() {
    return this.areaUnitNo == null ? "" : this.areaUnitNo;
  }

/**
	* 單位代號<br>
	* 
  *
  * @param areaUnitNo 單位代號
	*/
  public void setAreaUnitNo(String areaUnitNo) {
    this.areaUnitNo = areaUnitNo;
  }

/**
	* 員工代號<br>
	* 
	* @return String
	*/
  public String getHlEmpNo() {
    return this.hlEmpNo == null ? "" : this.hlEmpNo;
  }

/**
	* 員工代號<br>
	* 
  *
  * @param hlEmpNo 員工代號
	*/
  public void setHlEmpNo(String hlEmpNo) {
    this.hlEmpNo = hlEmpNo;
  }


  @Override
  public int hashCode() {
    return Objects.hash(workYM, areaUnitNo, hlEmpNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    HlEmpLnYg5PtId hlEmpLnYg5PtId = (HlEmpLnYg5PtId) obj;
    return workYM.equals(hlEmpLnYg5PtId.workYM) && areaUnitNo.equals(hlEmpLnYg5PtId.areaUnitNo) && hlEmpNo.equals(hlEmpLnYg5PtId.hlEmpNo);
  }

  @Override
  public String toString() {
    return "HlEmpLnYg5PtId [workYM=" + workYM + ", areaUnitNo=" + areaUnitNo + ", hlEmpNo=" + hlEmpNo + "]";
  }
}
