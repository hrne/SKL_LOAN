package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * CdCl 擔保品代號檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class CdClId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 7875561742050880284L;

// 擔保品代號1
  @Column(name = "`ClCode1`")
  private int clCode1 = 0;

  // 擔保品代號2
  @Column(name = "`ClCode2`")
  private int clCode2 = 0;

  public CdClId() {
  }

  public CdClId(int clCode1, int clCode2) {
    this.clCode1 = clCode1;
    this.clCode2 = clCode2;
  }

/**
	* 擔保品代號1<br>
	* 
	* @return Integer
	*/
  public int getClCode1() {
    return this.clCode1;
  }

/**
	* 擔保品代號1<br>
	* 
  *
  * @param clCode1 擔保品代號1
	*/
  public void setClCode1(int clCode1) {
    this.clCode1 = clCode1;
  }

/**
	* 擔保品代號2<br>
	* 
	* @return Integer
	*/
  public int getClCode2() {
    return this.clCode2;
  }

/**
	* 擔保品代號2<br>
	* 
  *
  * @param clCode2 擔保品代號2
	*/
  public void setClCode2(int clCode2) {
    this.clCode2 = clCode2;
  }


  @Override
  public int hashCode() {
    return Objects.hash(clCode1, clCode2);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    CdClId cdClId = (CdClId) obj;
    return clCode1 == cdClId.clCode1 && clCode2 == cdClId.clCode2;
  }

  @Override
  public String toString() {
    return "CdClId [clCode1=" + clCode1 + ", clCode2=" + clCode2 + "]";
  }
}
