package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * HlEmpLnYg5Pt 房貨專員目標檔案<br>
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
	private static final long serialVersionUID = 4416406823051458636L;

// 年月份
  /* eric 2022.1.6 */
  @Column(name = "`WorkYM`")
  private int workYM = 0;

  // 員工代號
  /* eric 2022.1.6 */
  @Column(name = "`EmpNo`", length = 6)
  private String empNo = " ";

  public HlEmpLnYg5PtId() {
  }

  public HlEmpLnYg5PtId(int workYM, String empNo) {
    this.workYM = workYM;
    this.empNo = empNo;
  }

/**
	* 年月份<br>
	* eric 2022.1.6
	* @return Integer
	*/
  public int getWorkYM() {
    return this.workYM;
  }

/**
	* 年月份<br>
	* eric 2022.1.6
  *
  * @param workYM 年月份
	*/
  public void setWorkYM(int workYM) {
    this.workYM = workYM;
  }

/**
	* 員工代號<br>
	* eric 2022.1.6
	* @return String
	*/
  public String getEmpNo() {
    return this.empNo == null ? "" : this.empNo;
  }

/**
	* 員工代號<br>
	* eric 2022.1.6
  *
  * @param empNo 員工代號
	*/
  public void setEmpNo(String empNo) {
    this.empNo = empNo;
  }


  @Override
  public int hashCode() {
    return Objects.hash(workYM, empNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    HlEmpLnYg5PtId hlEmpLnYg5PtId = (HlEmpLnYg5PtId) obj;
    return workYM == hlEmpLnYg5PtId.workYM && empNo.equals(hlEmpLnYg5PtId.empNo);
  }

  @Override
  public String toString() {
    return "HlEmpLnYg5PtId [workYM=" + workYM + ", empNo=" + empNo + "]";
  }
}
