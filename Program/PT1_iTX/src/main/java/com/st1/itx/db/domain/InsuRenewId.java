package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * InsuRenew 火險單續保檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class InsuRenewId implements Serializable {


  // 擔保品-代號1
  @Column(name = "`ClCode1`")
  private int clCode1 = 0;

  // 擔保品-代號2
  @Column(name = "`ClCode2`")
  private int clCode2 = 0;

  // 擔保品編號
  @Column(name = "`ClNo`")
  private int clNo = 0;

  // 原保單號碼
  @Column(name = "`PrevInsuNo`", length = 17)
  private String prevInsuNo = " ";

  // 批單號碼
  /* 修改時需填入 */
  @Column(name = "`EndoInsuNo`", length = 17)
  private String endoInsuNo = " ";

  public InsuRenewId() {
  }

  public InsuRenewId(int clCode1, int clCode2, int clNo, String prevInsuNo, String endoInsuNo) {
    this.clCode1 = clCode1;
    this.clCode2 = clCode2;
    this.clNo = clNo;
    this.prevInsuNo = prevInsuNo;
    this.endoInsuNo = endoInsuNo;
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
	* 原保單號碼<br>
	* 
	* @return String
	*/
  public String getPrevInsuNo() {
    return this.prevInsuNo == null ? "" : this.prevInsuNo;
  }

/**
	* 原保單號碼<br>
	* 
  *
  * @param prevInsuNo 原保單號碼
	*/
  public void setPrevInsuNo(String prevInsuNo) {
    this.prevInsuNo = prevInsuNo;
  }

/**
	* 批單號碼<br>
	* 修改時需填入
	* @return String
	*/
  public String getEndoInsuNo() {
    return this.endoInsuNo == null ? "" : this.endoInsuNo;
  }

/**
	* 批單號碼<br>
	* 修改時需填入
  *
  * @param endoInsuNo 批單號碼
	*/
  public void setEndoInsuNo(String endoInsuNo) {
    this.endoInsuNo = endoInsuNo;
  }


  @Override
  public int hashCode() {
    return Objects.hash(clCode1, clCode2, clNo, prevInsuNo, endoInsuNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    InsuRenewId insuRenewId = (InsuRenewId) obj;
    return clCode1 == insuRenewId.clCode1 && clCode2 == insuRenewId.clCode2 && clNo == insuRenewId.clNo && prevInsuNo.equals(insuRenewId.prevInsuNo) && endoInsuNo.equals(insuRenewId.endoInsuNo);
  }

  @Override
  public String toString() {
    return "InsuRenewId [clCode1=" + clCode1 + ", clCode2=" + clCode2 + ", clNo=" + clNo + ", prevInsuNo=" + prevInsuNo + ", endoInsuNo=" + endoInsuNo + "]";
  }
}
