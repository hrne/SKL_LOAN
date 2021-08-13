package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * ClLand 擔保品不動產土地檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class ClLandId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -4510424708205787289L;

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

  // 土地序號
  /* 房地:從1起編土地:固定000 */
  @Column(name = "`LandSeq`")
  private int landSeq = 0;

  public ClLandId() {
  }

  public ClLandId(int clCode1, int clCode2, int clNo, int landSeq) {
    this.clCode1 = clCode1;
    this.clCode2 = clCode2;
    this.clNo = clNo;
    this.landSeq = landSeq;
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
	* 土地序號<br>
	* 房地:從1起編
土地:固定000
	* @return Integer
	*/
  public int getLandSeq() {
    return this.landSeq;
  }

/**
	* 土地序號<br>
	* 房地:從1起編
土地:固定000
  *
  * @param landSeq 土地序號
	*/
  public void setLandSeq(int landSeq) {
    this.landSeq = landSeq;
  }


  @Override
  public int hashCode() {
    return Objects.hash(clCode1, clCode2, clNo, landSeq);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    ClLandId clLandId = (ClLandId) obj;
    return clCode1 == clLandId.clCode1 && clCode2 == clLandId.clCode2 && clNo == clLandId.clNo && landSeq == clLandId.landSeq;
  }

  @Override
  public String toString() {
    return "ClLandId [clCode1=" + clCode1 + ", clCode2=" + clCode2 + ", clNo=" + clNo + ", landSeq=" + landSeq + "]";
  }
}
