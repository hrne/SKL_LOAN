package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * ClOwnerRelation 擔保品所有權人與授信戶關係檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class ClOwnerRelationId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 4146233659032207895L;

// 擔保品代號1
  /* 擔保品代號檔CdCl */
  @Column(name = "`ClCode1`")
  private int clCode1 = 0;

  // 擔保品代號2
  /* 擔保品代號檔CdCl */
  @Column(name = "`ClCode2`")
  private int clCode2 = 0;

  // 擔保品編號
  @Column(name = "`ClNo`")
  private int clNo = 0;

  // 核准號碼
  @Column(name = "`ApplNo`")
  private int applNo = 0;

  // 客戶識別碼
  @Column(name = "`OwnerCustUKey`", length = 32)
  private String ownerCustUKey = " ";

  public ClOwnerRelationId() {
  }

  public ClOwnerRelationId(int clCode1, int clCode2, int clNo, int applNo, String ownerCustUKey) {
    this.clCode1 = clCode1;
    this.clCode2 = clCode2;
    this.clNo = clNo;
    this.applNo = applNo;
    this.ownerCustUKey = ownerCustUKey;
  }

/**
	* 擔保品代號1<br>
	* 擔保品代號檔CdCl
	* @return Integer
	*/
  public int getClCode1() {
    return this.clCode1;
  }

/**
	* 擔保品代號1<br>
	* 擔保品代號檔CdCl
  *
  * @param clCode1 擔保品代號1
	*/
  public void setClCode1(int clCode1) {
    this.clCode1 = clCode1;
  }

/**
	* 擔保品代號2<br>
	* 擔保品代號檔CdCl
	* @return Integer
	*/
  public int getClCode2() {
    return this.clCode2;
  }

/**
	* 擔保品代號2<br>
	* 擔保品代號檔CdCl
  *
  * @param clCode2 擔保品代號2
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
	* 核准號碼<br>
	* 
	* @return Integer
	*/
  public int getApplNo() {
    return this.applNo;
  }

/**
	* 核准號碼<br>
	* 
  *
  * @param applNo 核准號碼
	*/
  public void setApplNo(int applNo) {
    this.applNo = applNo;
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
    return Objects.hash(clCode1, clCode2, clNo, applNo, ownerCustUKey);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    ClOwnerRelationId clOwnerRelationId = (ClOwnerRelationId) obj;
    return clCode1 == clOwnerRelationId.clCode1 && clCode2 == clOwnerRelationId.clCode2 && clNo == clOwnerRelationId.clNo && applNo == clOwnerRelationId.applNo && ownerCustUKey.equals(clOwnerRelationId.ownerCustUKey);
  }

  @Override
  public String toString() {
    return "ClOwnerRelationId [clCode1=" + clCode1 + ", clCode2=" + clCode2 + ", clNo=" + clNo + ", applNo=" + applNo + ", ownerCustUKey=" + ownerCustUKey + "]";
  }
}
