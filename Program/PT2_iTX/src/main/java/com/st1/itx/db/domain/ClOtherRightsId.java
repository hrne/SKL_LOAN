package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * ClOtherRights 擔保品他項權利檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class ClOtherRightsId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -3346732696726706955L;

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

  // 他項權利序號
  /* ex：0000-000 */
  @Column(name = "`Seq`", length = 8)
  private String seq = " ";

  public ClOtherRightsId() {
  }

  public ClOtherRightsId(int clCode1, int clCode2, int clNo, String seq) {
    this.clCode1 = clCode1;
    this.clCode2 = clCode2;
    this.clNo = clNo;
    this.seq = seq;
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
	* 他項權利序號<br>
	* ex：0000-000
	* @return String
	*/
  public String getSeq() {
    return this.seq == null ? "" : this.seq;
  }

/**
	* 他項權利序號<br>
	* ex：0000-000
  *
  * @param seq 他項權利序號
	*/
  public void setSeq(String seq) {
    this.seq = seq;
  }


  @Override
  public int hashCode() {
    return Objects.hash(clCode1, clCode2, clNo, seq);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    ClOtherRightsId clOtherRightsId = (ClOtherRightsId) obj;
    return clCode1 == clOtherRightsId.clCode1 && clCode2 == clOtherRightsId.clCode2 && clNo == clOtherRightsId.clNo && seq.equals(clOtherRightsId.seq);
  }

  @Override
  public String toString() {
    return "ClOtherRightsId [clCode1=" + clCode1 + ", clCode2=" + clCode2 + ", clNo=" + clNo + ", seq=" + seq + "]";
  }
}
