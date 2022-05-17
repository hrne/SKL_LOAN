package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * HlThreeDetail 介紹人業績明細檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class HlThreeDetailId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -6493985985586207493L;

// 營業單位別
  @Column(name = "`BrNo`", length = 4)
  private String brNo = " ";

  // 借款人戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 額度編號
  @Column(name = "`FacmNo`")
  private int facmNo = 0;

  public HlThreeDetailId() {
  }

  public HlThreeDetailId(String brNo, int custNo, int facmNo) {
    this.brNo = brNo;
    this.custNo = custNo;
    this.facmNo = facmNo;
  }

/**
	* 營業單位別<br>
	* 
	* @return String
	*/
  public String getBrNo() {
    return this.brNo == null ? "" : this.brNo;
  }

/**
	* 營業單位別<br>
	* 
  *
  * @param brNo 營業單位別
	*/
  public void setBrNo(String brNo) {
    this.brNo = brNo;
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
    return Objects.hash(brNo, custNo, facmNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    HlThreeDetailId hlThreeDetailId = (HlThreeDetailId) obj;
    return brNo.equals(hlThreeDetailId.brNo) && custNo == hlThreeDetailId.custNo && facmNo == hlThreeDetailId.facmNo;
  }

  @Override
  public String toString() {
    return "HlThreeDetailId [brNo=" + brNo + ", custNo=" + custNo + ", facmNo=" + facmNo + "]";
  }
}
