package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * ClBuildingPublic 擔保品-建物公設建號檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class ClBuildingPublicId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 8477184117603313886L;

// 擔保品-代號1
  /* 擔保品代號檔CdCl */
  @Column(name = "`ClCode1`")
  private int clCode1 = 0;

  // 擔保品-代號2
  /* 擔保品代號檔CdCl */
  @Column(name = "`ClCode2`")
  private int clCode2 = 0;

  // 擔保品編號
  @Column(name = "`ClNo`")
  private int clNo = 0;

  // 公設資料序號
  /* 公設資料流水號 */
  @Column(name = "`PublicSeq`")
  private int publicSeq = 0;

  public ClBuildingPublicId() {
  }

  public ClBuildingPublicId(int clCode1, int clCode2, int clNo, int publicSeq) {
    this.clCode1 = clCode1;
    this.clCode2 = clCode2;
    this.clNo = clNo;
    this.publicSeq = publicSeq;
  }

/**
	* 擔保品-代號1<br>
	* 擔保品代號檔CdCl
	* @return Integer
	*/
  public int getClCode1() {
    return this.clCode1;
  }

/**
	* 擔保品-代號1<br>
	* 擔保品代號檔CdCl
  *
  * @param clCode1 擔保品-代號1
	*/
  public void setClCode1(int clCode1) {
    this.clCode1 = clCode1;
  }

/**
	* 擔保品-代號2<br>
	* 擔保品代號檔CdCl
	* @return Integer
	*/
  public int getClCode2() {
    return this.clCode2;
  }

/**
	* 擔保品-代號2<br>
	* 擔保品代號檔CdCl
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
	* 公設資料序號<br>
	* 公設資料流水號
	* @return Integer
	*/
  public int getPublicSeq() {
    return this.publicSeq;
  }

/**
	* 公設資料序號<br>
	* 公設資料流水號
  *
  * @param publicSeq 公設資料序號
	*/
  public void setPublicSeq(int publicSeq) {
    this.publicSeq = publicSeq;
  }


  @Override
  public int hashCode() {
    return Objects.hash(clCode1, clCode2, clNo, publicSeq);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    ClBuildingPublicId clBuildingPublicId = (ClBuildingPublicId) obj;
    return clCode1 == clBuildingPublicId.clCode1 && clCode2 == clBuildingPublicId.clCode2 && clNo == clBuildingPublicId.clNo && publicSeq == clBuildingPublicId.publicSeq;
  }

  @Override
  public String toString() {
    return "ClBuildingPublicId [clCode1=" + clCode1 + ", clCode2=" + clCode2 + ", clNo=" + clNo + ", publicSeq=" + publicSeq + "]";
  }
}
