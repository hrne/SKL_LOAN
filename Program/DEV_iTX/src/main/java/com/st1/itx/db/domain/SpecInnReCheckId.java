package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * SpecInnReCheck 指定覆審名單檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class SpecInnReCheckId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -8147549975752950267L;

// 借款人戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 額度號碼
  @Column(name = "`FacmNo`")
  private int facmNo = 0;

  public SpecInnReCheckId() {
  }

  public SpecInnReCheckId(int custNo, int facmNo) {
    this.custNo = custNo;
    this.facmNo = facmNo;
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
	* 額度號碼<br>
	* 
	* @return Integer
	*/
  public int getFacmNo() {
    return this.facmNo;
  }

/**
	* 額度號碼<br>
	* 
  *
  * @param facmNo 額度號碼
	*/
  public void setFacmNo(int facmNo) {
    this.facmNo = facmNo;
  }


  @Override
  public int hashCode() {
    return Objects.hash(custNo, facmNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    SpecInnReCheckId specInnReCheckId = (SpecInnReCheckId) obj;
    return custNo == specInnReCheckId.custNo && facmNo == specInnReCheckId.facmNo;
  }

  @Override
  public String toString() {
    return "SpecInnReCheckId [custNo=" + custNo + ", facmNo=" + facmNo + "]";
  }
}
