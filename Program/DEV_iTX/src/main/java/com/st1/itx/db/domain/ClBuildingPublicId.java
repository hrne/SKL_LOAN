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
	private static final long serialVersionUID = -3311803712228466976L;

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

  // 公設建號
  /* 建號格式為5-3 */
  @Column(name = "`PublicBdNo1`")
  private int publicBdNo1 = 0;

  // 公設建號(子號)
  /* 建號格式為5-3 */
  @Column(name = "`PublicBdNo2`")
  private int publicBdNo2 = 0;

  public ClBuildingPublicId() {
  }

  public ClBuildingPublicId(int clCode1, int clCode2, int clNo, int publicBdNo1, int publicBdNo2) {
    this.clCode1 = clCode1;
    this.clCode2 = clCode2;
    this.clNo = clNo;
    this.publicBdNo1 = publicBdNo1;
    this.publicBdNo2 = publicBdNo2;
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
	* 公設建號<br>
	* 建號格式為5-3
	* @return Integer
	*/
  public int getPublicBdNo1() {
    return this.publicBdNo1;
  }

/**
	* 公設建號<br>
	* 建號格式為5-3
  *
  * @param publicBdNo1 公設建號
	*/
  public void setPublicBdNo1(int publicBdNo1) {
    this.publicBdNo1 = publicBdNo1;
  }

/**
	* 公設建號(子號)<br>
	* 建號格式為5-3
	* @return Integer
	*/
  public int getPublicBdNo2() {
    return this.publicBdNo2;
  }

/**
	* 公設建號(子號)<br>
	* 建號格式為5-3
  *
  * @param publicBdNo2 公設建號(子號)
	*/
  public void setPublicBdNo2(int publicBdNo2) {
    this.publicBdNo2 = publicBdNo2;
  }


  @Override
  public int hashCode() {
    return Objects.hash(clCode1, clCode2, clNo, publicBdNo1, publicBdNo2);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    ClBuildingPublicId clBuildingPublicId = (ClBuildingPublicId) obj;
    return clCode1 == clBuildingPublicId.clCode1 && clCode2 == clBuildingPublicId.clCode2 && clNo == clBuildingPublicId.clNo && publicBdNo1 == clBuildingPublicId.publicBdNo1 && publicBdNo2 == clBuildingPublicId.publicBdNo2;
  }

  @Override
  public String toString() {
    return "ClBuildingPublicId [clCode1=" + clCode1 + ", clCode2=" + clCode2 + ", clNo=" + clNo + ", publicBdNo1=" + publicBdNo1 + ", publicBdNo2=" + publicBdNo2 + "]";
  }
}
