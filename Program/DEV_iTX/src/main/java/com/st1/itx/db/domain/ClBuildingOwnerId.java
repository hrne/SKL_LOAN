package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * ClBuildingOwner 擔保品-建物所有權人檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class ClBuildingOwnerId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 7022847863421215108L;

// 擔保品-代號1
  @Column(name = "`ClCode1`")
  private int clCode1 = 0;

  // 擔保品-代號2
  @Column(name = "`ClCode2`")
  private int clCode2 = 0;

  // 擔保品編號
  @Column(name = "`ClNo`")
  private int clNo = 0;

  // 所有權人統編
  @Column(name = "`OwnerId`", length = 10)
  private String ownerId = " ";

  public ClBuildingOwnerId() {
  }

  public ClBuildingOwnerId(int clCode1, int clCode2, int clNo, String ownerId) {
    this.clCode1 = clCode1;
    this.clCode2 = clCode2;
    this.clNo = clNo;
    this.ownerId = ownerId;
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

/**
	* 所有權人統編<br>
	* 
	* @return String
	*/
  public String getOwnerId() {
    return this.ownerId == null ? "" : this.ownerId;
  }

/**
	* 所有權人統編<br>
	* 
  *
  * @param ownerId 所有權人統編
	*/
  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }


  @Override
  public int hashCode() {
    return Objects.hash(clCode1, clCode2, clNo, ownerId);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    ClBuildingOwnerId clBuildingOwnerId = (ClBuildingOwnerId) obj;
    return clCode1 == clBuildingOwnerId.clCode1 && clCode2 == clBuildingOwnerId.clCode2 && clNo == clBuildingOwnerId.clNo && ownerId.equals(clBuildingOwnerId.ownerId);
  }

  @Override
  public String toString() {
    return "ClBuildingOwnerId [clCode1=" + clCode1 + ", clCode2=" + clCode2 + ", clNo=" + clNo + ", ownerId=" + ownerId + "]";
  }
}
