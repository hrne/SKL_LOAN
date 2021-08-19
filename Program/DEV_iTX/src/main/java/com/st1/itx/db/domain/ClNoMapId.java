package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * ClNoMap 擔保品編號新舊對照檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class ClNoMapId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 8523471102772901791L;

// 原擔保品代號1
  /* 原擔保品 */
  @Column(name = "`GdrId1`")
  private int gdrId1 = 0;

  // 原擔保品代號2
  /* 原擔保品 */
  @Column(name = "`GdrId2`")
  private int gdrId2 = 0;

  // 原擔保品編號
  /* 原擔保品 */
  @Column(name = "`GdrNum`")
  private int gdrNum = 0;

  // 原擔保品序號
  /* 原擔保品 */
  @Column(name = "`LgtSeq`")
  private int lgtSeq = 0;

  public ClNoMapId() {
  }

  public ClNoMapId(int gdrId1, int gdrId2, int gdrNum, int lgtSeq) {
    this.gdrId1 = gdrId1;
    this.gdrId2 = gdrId2;
    this.gdrNum = gdrNum;
    this.lgtSeq = lgtSeq;
  }

/**
	* 原擔保品代號1<br>
	* 原擔保品
	* @return Integer
	*/
  public int getGdrId1() {
    return this.gdrId1;
  }

/**
	* 原擔保品代號1<br>
	* 原擔保品
  *
  * @param gdrId1 原擔保品代號1
	*/
  public void setGdrId1(int gdrId1) {
    this.gdrId1 = gdrId1;
  }

/**
	* 原擔保品代號2<br>
	* 原擔保品
	* @return Integer
	*/
  public int getGdrId2() {
    return this.gdrId2;
  }

/**
	* 原擔保品代號2<br>
	* 原擔保品
  *
  * @param gdrId2 原擔保品代號2
	*/
  public void setGdrId2(int gdrId2) {
    this.gdrId2 = gdrId2;
  }

/**
	* 原擔保品編號<br>
	* 原擔保品
	* @return Integer
	*/
  public int getGdrNum() {
    return this.gdrNum;
  }

/**
	* 原擔保品編號<br>
	* 原擔保品
  *
  * @param gdrNum 原擔保品編號
	*/
  public void setGdrNum(int gdrNum) {
    this.gdrNum = gdrNum;
  }

/**
	* 原擔保品序號<br>
	* 原擔保品
	* @return Integer
	*/
  public int getLgtSeq() {
    return this.lgtSeq;
  }

/**
	* 原擔保品序號<br>
	* 原擔保品
  *
  * @param lgtSeq 原擔保品序號
	*/
  public void setLgtSeq(int lgtSeq) {
    this.lgtSeq = lgtSeq;
  }


  @Override
  public int hashCode() {
    return Objects.hash(gdrId1, gdrId2, gdrNum, lgtSeq);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    ClNoMapId clNoMapId = (ClNoMapId) obj;
    return gdrId1 == clNoMapId.gdrId1 && gdrId2 == clNoMapId.gdrId2 && gdrNum == clNoMapId.gdrNum && lgtSeq == clNoMapId.lgtSeq;
  }

  @Override
  public String toString() {
    return "ClNoMapId [gdrId1=" + gdrId1 + ", gdrId2=" + gdrId2 + ", gdrNum=" + gdrNum + ", lgtSeq=" + lgtSeq + "]";
  }
}
