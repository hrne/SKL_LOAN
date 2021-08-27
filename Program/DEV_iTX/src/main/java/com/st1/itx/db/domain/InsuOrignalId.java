package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * InsuOrignal 火險初保檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class InsuOrignalId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -281421461567121318L;

// 擔保品-代號1
  @Column(name = "`ClCode1`")
  private int clCode1 = 0;

  // 擔保品-代號2
  @Column(name = "`ClCode2`")
  private int clCode2 = 0;

  // 擔保品編號
  @Column(name = "`ClNo`")
  private int clNo = 0;

  // 原始保險單號碼
  @Column(name = "`OrigInsuNo`", length = 17)
  private String origInsuNo = " ";

  // 批單號碼
  @Column(name = "`EndoInsuNo`", length = 17)
  private String endoInsuNo = " ";

  public InsuOrignalId() {
  }

  public InsuOrignalId(int clCode1, int clCode2, int clNo, String origInsuNo, String endoInsuNo) {
    this.clCode1 = clCode1;
    this.clCode2 = clCode2;
    this.clNo = clNo;
    this.origInsuNo = origInsuNo;
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
	* 原始保險單號碼<br>
	* 
	* @return String
	*/
  public String getOrigInsuNo() {
    return this.origInsuNo == null ? "" : this.origInsuNo;
  }

/**
	* 原始保險單號碼<br>
	* 
  *
  * @param origInsuNo 原始保險單號碼
	*/
  public void setOrigInsuNo(String origInsuNo) {
    this.origInsuNo = origInsuNo;
  }

/**
	* 批單號碼<br>
	* 
	* @return String
	*/
  public String getEndoInsuNo() {
    return this.endoInsuNo == null ? "" : this.endoInsuNo;
  }

/**
	* 批單號碼<br>
	* 
  *
  * @param endoInsuNo 批單號碼
	*/
  public void setEndoInsuNo(String endoInsuNo) {
    this.endoInsuNo = endoInsuNo;
  }


  @Override
  public int hashCode() {
    return Objects.hash(clCode1, clCode2, clNo, origInsuNo, endoInsuNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    InsuOrignalId insuOrignalId = (InsuOrignalId) obj;
    return clCode1 == insuOrignalId.clCode1 && clCode2 == insuOrignalId.clCode2 && clNo == insuOrignalId.clNo && origInsuNo.equals(insuOrignalId.origInsuNo) && endoInsuNo.equals(insuOrignalId.endoInsuNo);
  }

  @Override
  public String toString() {
    return "InsuOrignalId [clCode1=" + clCode1 + ", clCode2=" + clCode2 + ", clNo=" + clNo + ", origInsuNo=" + origInsuNo + ", endoInsuNo=" + endoInsuNo + "]";
  }
}
