package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * ClStock 擔保品股票檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class ClStockId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 1117334168634529373L;

// 擔保品代號1
  /* 擔保品代號檔CdCl */
  @Column(name = "`ClCode1`")
  private int clCode1 = 0;

  // 擔保品代號2
  /* 擔保品代號檔CdCl */
  @Column(name = "`ClCode2`")
  private int clCode2 = 0;

  // 擔保品號碼
  @Column(name = "`ClNo`")
  private int clNo = 0;

  public ClStockId() {
  }

  public ClStockId(int clCode1, int clCode2, int clNo) {
    this.clCode1 = clCode1;
    this.clCode2 = clCode2;
    this.clNo = clNo;
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
	* 擔保品號碼<br>
	* 
	* @return Integer
	*/
  public int getClNo() {
    return this.clNo;
  }

/**
	* 擔保品號碼<br>
	* 
  *
  * @param clNo 擔保品號碼
	*/
  public void setClNo(int clNo) {
    this.clNo = clNo;
  }


  @Override
  public int hashCode() {
    return Objects.hash(clCode1, clCode2, clNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    ClStockId clStockId = (ClStockId) obj;
    return clCode1 == clStockId.clCode1 && clCode2 == clStockId.clCode2 && clNo == clStockId.clNo;
  }

  @Override
  public String toString() {
    return "ClStockId [clCode1=" + clCode1 + ", clCode2=" + clCode2 + ", clNo=" + clNo + "]";
  }
}
