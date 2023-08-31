package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * LaLgtp AS400土地明細資料檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class LaLgtpId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -1375712814229500538L;

// 營業單位別
  @Column(name = "`Cusbrh`")
  private int cusbrh = 0;

  // 押品別１
  @Column(name = "`Gdrid1`")
  private int gdrid1 = 0;

  // 押品別２
  @Column(name = "`Gdrid2`")
  private int gdrid2 = 0;

  // 押品號碼
  @Column(name = "`Gdrnum`")
  private int gdrnum = 0;

  // 序號
  @Column(name = "`Lgtseq`")
  private int lgtseq = 0;

  public LaLgtpId() {
  }

  public LaLgtpId(int cusbrh, int gdrid1, int gdrid2, int gdrnum, int lgtseq) {
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
	* 押品別１<br>
	* 
	* @return Integer
	*/
  public int getGdrid1() {
    return this.gdrid1;
  }

/**
	* 押品別１<br>
	* 
  *
  * @param gdrid1 押品別１
	*/
  public void setGdrid1(int gdrid1) {
    this.gdrid1 = gdrid1;
  }

/**
	* 押品別２<br>
	* 
	* @return Integer
	*/
  public int getGdrid2() {
    return this.gdrid2;
  }

/**
	* 押品別２<br>
	* 
  *
  * @param gdrid2 押品別２
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
    LaLgtpId laLgtpId = (LaLgtpId) obj;
    return cusbrh == laLgtpId.cusbrh && gdrid1 == laLgtpId.gdrid1 && gdrid2 == laLgtpId.gdrid2 && gdrnum == laLgtpId.gdrnum && lgtseq == laLgtpId.lgtseq;
  }

  @Override
  public String toString() {
    return "LaLgtpId [cusbrh=" + cusbrh + ", gdrid1=" + gdrid1 + ", gdrid2=" + gdrid2 + ", gdrnum=" + gdrnum + ", lgtseq=" + lgtseq + "]";
  }
}
