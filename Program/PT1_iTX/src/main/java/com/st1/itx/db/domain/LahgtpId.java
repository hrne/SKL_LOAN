package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Lahgtp AS400建物明細資料檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class LahgtpId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -4444330186921940115L;

// 營業單位別
  @Column(name = "`Cusbrh`")
  private int cusbrh = 0;

  // 押品別1
  @Column(name = "`Gdrid1`")
  private int gdrid1 = 0;

  // 押品別2
  @Column(name = "`Gdrid2`")
  private int gdrid2 = 0;

  // 押品號碼
  @Column(name = "`Gdrnum`")
  private int gdrnum = 0;

  // 序號
  @Column(name = "`Lgtseq`")
  private int lgtseq = 0;

  public LahgtpId() {
  }

  public LahgtpId(int cusbrh, int gdrid1, int gdrid2, int gdrnum, int lgtseq) {
    this.cusbrh = cusbrh;
    this.gdrid1 = gdrid1;
    this.gdrid2 = gdrid2;
    this.gdrnum = gdrnum;
    this.lgtseq = lgtseq;
  }

/**
	* 營業單位別<br>
	* 
	* @return Integer
	*/
  public int getCusbrh() {
    return this.cusbrh;
  }

/**
	* 營業單位別<br>
	* 
  *
  * @param cusbrh 營業單位別
	*/
  public void setCusbrh(int cusbrh) {
    this.cusbrh = cusbrh;
  }

/**
	* 押品別1<br>
	* 
	* @return Integer
	*/
  public int getGdrid1() {
    return this.gdrid1;
  }

/**
	* 押品別1<br>
	* 
  *
  * @param gdrid1 押品別1
	*/
  public void setGdrid1(int gdrid1) {
    this.gdrid1 = gdrid1;
  }

/**
	* 押品別2<br>
	* 
	* @return Integer
	*/
  public int getGdrid2() {
    return this.gdrid2;
  }

/**
	* 押品別2<br>
	* 
  *
  * @param gdrid2 押品別2
	*/
  public void setGdrid2(int gdrid2) {
    this.gdrid2 = gdrid2;
  }

/**
	* 押品號碼<br>
	* 
	* @return Integer
	*/
  public int getGdrnum() {
    return this.gdrnum;
  }

/**
	* 押品號碼<br>
	* 
  *
  * @param gdrnum 押品號碼
	*/
  public void setGdrnum(int gdrnum) {
    this.gdrnum = gdrnum;
  }

/**
	* 序號<br>
	* 
	* @return Integer
	*/
  public int getLgtseq() {
    return this.lgtseq;
  }

/**
	* 序號<br>
	* 
  *
  * @param lgtseq 序號
	*/
  public void setLgtseq(int lgtseq) {
    this.lgtseq = lgtseq;
  }


  @Override
  public int hashCode() {
    return Objects.hash(cusbrh, gdrid1, gdrid2, gdrnum, lgtseq);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    LahgtpId lahgtpId = (LahgtpId) obj;
    return cusbrh == lahgtpId.cusbrh && gdrid1 == lahgtpId.gdrid1 && gdrid2 == lahgtpId.gdrid2 && gdrnum == lahgtpId.gdrnum && lgtseq == lahgtpId.lgtseq;
  }

  @Override
  public String toString() {
    return "LahgtpId [cusbrh=" + cusbrh + ", gdrid1=" + gdrid1 + ", gdrid2=" + gdrid2 + ", gdrnum=" + gdrnum + ", lgtseq=" + lgtseq + "]";
  }
}
