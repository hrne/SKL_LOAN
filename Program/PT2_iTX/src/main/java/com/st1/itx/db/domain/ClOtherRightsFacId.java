package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * ClOtherRightsFac 擔保品他項權利額度關聯檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class ClOtherRightsFacId implements Serializable {


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

  // 他項權利登記次序
  /* ex：0002-000 */
  @Column(name = "`Seq`", length = 8)
  private String seq = " ";

  // 借款人戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 額度編號
  @Column(name = "`FacmNo`")
  private int facmNo = 0;

  public ClOtherRightsFacId() {
  }

  public ClOtherRightsFacId(int clCode1, int clCode2, int clNo, String seq, int custNo, int facmNo) {
    this.clCode1 = clCode1;
    this.clCode2 = clCode2;
    this.clNo = clNo;
    this.seq = seq;
    this.custNo = custNo;
    this.facmNo = facmNo;
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
	* 他項權利登記次序<br>
	* ex：0002-000
	* @return String
	*/
  public String getSeq() {
    return this.seq == null ? "" : this.seq;
  }

/**
	* 他項權利登記次序<br>
	* ex：0002-000
  *
  * @param seq 他項權利登記次序
	*/
  public void setSeq(String seq) {
    this.seq = seq;
  }

/**
	* 借款人戶號<br>
	* 
	* @return Integer
	*/
  public int getCustNo() {
    return this.custNo;
  }

/**
	* 借款人戶號<br>
	* 
  *
  * @param custNo 借款人戶號
	*/
  public void setCustNo(int custNo) {
    this.custNo = custNo;
  }

/**
	* 額度編號<br>
	* 
	* @return Integer
	*/
  public int getFacmNo() {
    return this.facmNo;
  }

/**
	* 額度編號<br>
	* 
  *
  * @param facmNo 額度編號
	*/
  public void setFacmNo(int facmNo) {
    this.facmNo = facmNo;
  }


  @Override
  public int hashCode() {
    return Objects.hash(clCode1, clCode2, clNo, seq, custNo, facmNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    ClOtherRightsFacId clOtherRightsFacId = (ClOtherRightsFacId) obj;
    return clCode1 == clOtherRightsFacId.clCode1 && clCode2 == clOtherRightsFacId.clCode2 && clNo == clOtherRightsFacId.clNo && seq.equals(clOtherRightsFacId.seq) && custNo == clOtherRightsFacId.custNo && facmNo == clOtherRightsFacId.facmNo;
  }

  @Override
  public String toString() {
    return "ClOtherRightsFacId [clCode1=" + clCode1 + ", clCode2=" + clCode2 + ", clNo=" + clNo + ", seq=" + seq + ", custNo=" + custNo + ", facmNo=" + facmNo + "]";
  }
}
