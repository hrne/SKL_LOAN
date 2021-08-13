package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * PfDeparment 單位、區部、部室業績目標檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class PfDeparmentId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 5357899064475689448L;

// 區部代號
  /* v、v、 */
  @Column(name = "`DistCode`", length = 6)
  private String distCode = " ";

  // 部室代號
  /* v、v、v */
  @Column(name = "`DeptCode`", length = 6)
  private String deptCode = " ";

  // 單位代號
  /* v、 、 */
  @Column(name = "`UnitCode`", length = 6)
  private String unitCode = " ";

  public PfDeparmentId() {
  }

  public PfDeparmentId(String distCode, String deptCode, String unitCode) {
    this.distCode = distCode;
    this.deptCode = deptCode;
    this.unitCode = unitCode;
  }

/**
	* 區部代號<br>
	* v、v、
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
	* 單位代號<br>
	* v、 、
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


  @Override
  public int hashCode() {
    return Objects.hash(distCode, deptCode, unitCode);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    PfDeparmentId pfDeparmentId = (PfDeparmentId) obj;
    return distCode.equals(pfDeparmentId.distCode) && deptCode.equals(pfDeparmentId.deptCode) && unitCode.equals(pfDeparmentId.unitCode);
  }

  @Override
  public String toString() {
    return "PfDeparmentId [distCode=" + distCode + ", deptCode=" + deptCode + ", unitCode=" + unitCode + "]";
  }
}
