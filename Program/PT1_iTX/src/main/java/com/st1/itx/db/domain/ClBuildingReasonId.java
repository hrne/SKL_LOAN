package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * ClBuildingReason 擔保品-建物修改原因檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class ClBuildingReasonId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 6194120703027723384L;

// 擔保品-代號1
  @Column(name = "`ClCode1`")
  private int clCode1 = 0;

  // 擔保品-代號2
  @Column(name = "`ClCode2`")
  private int clCode2 = 0;

  // 擔保品編號
  @Column(name = "`ClNo`")
  private int clNo = 0;

  public ClBuildingReasonId() {
  }

  public ClBuildingReasonId(int clCode1, int clCode2, int clNo) {
    this.clCode1 = clCode1;
    this.clCode2 = clCode2;
    this.clNo = clNo;
  }

/**
	* 擔保品-代號1<br>
	* 
	* @return Integer
	*/
  public int getClCode1() {
    return this.clCode1;
  }

/**
	* 擔保品-代號1<br>
	* 
  *
  * @param clCode1 擔保品-代號1
	*/
  public void setClCode1(int clCode1) {
    this.clCode1 = clCode1;
  }

/**
	* 擔保品-代號2<br>
	* 
	* @return Integer
	*/
  public int getClCode2() {
    return this.clCode2;
  }

/**
	* 擔保品-代號2<br>
	* 
  *
  * @param clCode2 擔保品-代號2
	*/
  public void setClCode2(int clCode2) {
    this.clCode2 = clCode2;
  }

/**
	* 擔保品編號<br>
	* 
	* @return Integer
	*/
  public int getClNo() {
    return this.clNo;
  }

/**
	* 擔保品編號<br>
	* 
  *
  * @param clNo 擔保品編號
	*/
  public void setClNo(int clNo) {
    this.clNo = clNo;
  }


  @Override
  public int hashCode() {
    return Objects.hash(clCode1, clCode2, clNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    ClBuildingReasonId clBuildingReasonId = (ClBuildingReasonId) obj;
    return clCode1 == clBuildingReasonId.clCode1 && clCode2 == clBuildingReasonId.clCode2 && clNo == clBuildingReasonId.clNo;
  }

  @Override
  public String toString() {
    return "ClBuildingReasonId [clCode1=" + clCode1 + ", clCode2=" + clCode2 + ", clNo=" + clNo + "]";
  }
}
