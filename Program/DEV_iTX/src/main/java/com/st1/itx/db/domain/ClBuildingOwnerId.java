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
	private static final long serialVersionUID = 5996867312232338692L;

// 擔保品-代號1
  @Column(name = "`ClCode1`")
  private int clCode1 = 0;

  // 擔保品-代號2
  @Column(name = "`ClCode2`")
  private int clCode2 = 0;

  // 擔保品編號
  @Column(name = "`ClNo`")
  private int clNo = 0;

  // 客戶識別碼
  @Column(name = "`OwnerCustUKey`", length = 32)
  private String ownerCustUKey = " ";

  public ClBuildingOwnerId() {
  }

  public ClBuildingOwnerId(int clCode1, int clCode2, int clNo, String ownerCustUKey) {
    this.clCode1 = clCode1;
    this.clCode2 = clCode2;
    this.clNo = clNo;
    this.ownerCustUKey = ownerCustUKey;
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
	* 客戶識別碼<br>
	* 
	* @return String
	*/
  public String getOwnerCustUKey() {
    return this.ownerCustUKey == null ? "" : this.ownerCustUKey;
  }

/**
	* 客戶識別碼<br>
	* 
  *
  * @param ownerCustUKey 客戶識別碼
	*/
  public void setOwnerCustUKey(String ownerCustUKey) {
    this.ownerCustUKey = ownerCustUKey;
  }


  @Override
  public int hashCode() {
    return Objects.hash(clCode1, clCode2, clNo, ownerCustUKey);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    ClBuildingOwnerId clBuildingOwnerId = (ClBuildingOwnerId) obj;
    return clCode1 == clBuildingOwnerId.clCode1 && clCode2 == clBuildingOwnerId.clCode2 && clNo == clBuildingOwnerId.clNo && ownerCustUKey.equals(clBuildingOwnerId.ownerCustUKey);
  }

  @Override
  public String toString() {
    return "ClBuildingOwnerId [clCode1=" + clCode1 + ", clCode2=" + clCode2 + ", clNo=" + clNo + ", ownerCustUKey=" + ownerCustUKey + "]";
  }
}
