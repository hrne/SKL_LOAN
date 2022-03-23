package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * HlThreeLaqhcp 單位、區部、部室業績累計檔<br>
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
	private static final long serialVersionUID = 341327590192502959L;

// 年月日
  @Column(name = "`CalDate`")
  private int calDate = 0;

  // 部室代號
  @Column(name = "`DeptCode`", length = 6)
  private String deptCode = " ";

  // 區部代號
  @Column(name = "`DistCode`", length = 6)
  private String distCode = " ";

  // 單位代號
  @Column(name = "`UnitCode`", length = 6)
  private String unitCode = " ";

  public HlThreeLaqhcpId() {
  }

  public HlThreeLaqhcpId(int calDate, String deptCode, String distCode, String unitCode) {
    this.calDate = calDate;
    this.deptCode = deptCode;
    this.distCode = distCode;
    this.unitCode = unitCode;
  }

/**
	* 年月日<br>
	* 
	* @return Integer
	*/
  public int getCalDate() {
    return  StaticTool.bcToRoc(this.calDate);
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


  @Override
  public int hashCode() {
    return Objects.hash(calDate, deptCode, distCode, unitCode);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    HlThreeLaqhcpId hlThreeLaqhcpId = (HlThreeLaqhcpId) obj;
    return calDate == hlThreeLaqhcpId.calDate && deptCode.equals(hlThreeLaqhcpId.deptCode) && distCode.equals(hlThreeLaqhcpId.distCode) && unitCode.equals(hlThreeLaqhcpId.unitCode);
  }

  @Override
  public String toString() {
    return "HlThreeLaqhcpId [calDate=" + calDate + ", deptCode=" + deptCode + ", distCode=" + distCode + ", unitCode=" + unitCode + "]";
  }
}
