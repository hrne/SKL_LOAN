package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * Lagdtp AS400不動產押品主檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class LagdtpId implements Serializable {


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

  public LagdtpId() {
  }

  public LagdtpId(int cusbrh, int gdrid1, int gdrid2, int gdrnum) {
    this.cusbrh = cusbrh;
    this.gdrid1 = gdrid1;
    this.gdrid2 = gdrid2;
    this.gdrnum = gdrnum;
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


  @Override
  public int hashCode() {
    return Objects.hash(cusbrh, gdrid1, gdrid2, gdrnum);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    LagdtpId lagdtpId = (LagdtpId) obj;
    return cusbrh == lagdtpId.cusbrh && gdrid1 == lagdtpId.gdrid1 && gdrid2 == lagdtpId.gdrid2 && gdrnum == lagdtpId.gdrnum;
  }

  @Override
  public String toString() {
    return "LagdtpId [cusbrh=" + cusbrh + ", gdrid1=" + gdrid1 + ", gdrid2=" + gdrid2 + ", gdrnum=" + gdrnum + "]";
  }
}
