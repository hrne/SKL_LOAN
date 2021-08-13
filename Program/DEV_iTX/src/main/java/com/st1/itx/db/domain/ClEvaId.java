package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * ClEva 擔保品重評資料檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class ClEvaId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -2180583606329502301L;

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

  // 鑑估序號
  @Column(name = "`EvaNo`")
  private int evaNo = 0;

  public ClEvaId() {
  }

  public ClEvaId(int clCode1, int clCode2, int clNo, int evaNo) {
    this.clCode1 = clCode1;
    this.clCode2 = clCode2;
    this.clNo = clNo;
    this.evaNo = evaNo;
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
	* 鑑估序號<br>
	* 
	* @return Integer
	*/
  public int getEvaNo() {
    return this.evaNo;
  }

/**
	* 鑑估序號<br>
	* 
  *
  * @param evaNo 鑑估序號
	*/
  public void setEvaNo(int evaNo) {
    this.evaNo = evaNo;
  }


  @Override
  public int hashCode() {
    return Objects.hash(clCode1, clCode2, clNo, evaNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    ClEvaId clEvaId = (ClEvaId) obj;
    return clCode1 == clEvaId.clCode1 && clCode2 == clEvaId.clCode2 && clNo == clEvaId.clNo && evaNo == clEvaId.evaNo;
  }

  @Override
  public String toString() {
    return "ClEvaId [clCode1=" + clCode1 + ", clCode2=" + clCode2 + ", clNo=" + clNo + ", evaNo=" + evaNo + "]";
  }
}
