package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * ClLandReason 擔保品-土地修改原因檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class ClLandReasonId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 6354302114462297592L;

// 擔保品-代號1
  @Column(name = "`ClCode1`")
  private int clCode1 = 0;

  // 擔保品-代號2
  @Column(name = "`ClCode2`")
  private int clCode2 = 0;

  // 擔保品編號
  @Column(name = "`ClNo`")
  private int clNo = 0;

  // 修改原因序號
  @Column(name = "`ReasonSeq`")
  private int reasonSeq = 0;

  public ClLandReasonId() {
  }

  public ClLandReasonId(int clCode1, int clCode2, int clNo, int reasonSeq) {
    this.clCode1 = clCode1;
    this.clCode2 = clCode2;
    this.clNo = clNo;
    this.reasonSeq = reasonSeq;
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
	* 修改原因序號<br>
	* 
	* @return Integer
	*/
  public int getReasonSeq() {
    return this.reasonSeq;
  }

/**
	* 修改原因序號<br>
	* 
  *
  * @param reasonSeq 修改原因序號
	*/
  public void setReasonSeq(int reasonSeq) {
    this.reasonSeq = reasonSeq;
  }


  @Override
  public int hashCode() {
    return Objects.hash(clCode1, clCode2, clNo, reasonSeq);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    ClLandReasonId clLandReasonId = (ClLandReasonId) obj;
    return clCode1 == clLandReasonId.clCode1 && clCode2 == clLandReasonId.clCode2 && clNo == clLandReasonId.clNo && reasonSeq == clLandReasonId.reasonSeq;
  }

  @Override
  public String toString() {
    return "ClLandReasonId [clCode1=" + clCode1 + ", clCode2=" + clCode2 + ", clNo=" + clNo + ", reasonSeq=" + reasonSeq + "]";
  }
}
