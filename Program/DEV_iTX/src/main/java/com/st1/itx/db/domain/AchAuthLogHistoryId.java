package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * AchAuthLogHistory ACH帳號授權記錄檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class AchAuthLogHistoryId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 4528853329135179315L;

// 戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 額度號碼
  @Column(name = "`FacmNo`")
  private int facmNo = 0;

  // 明細序號
  /* 該戶號、額度之授權帳號歷程序號 */
  @Column(name = "`DetailSeq`")
  private int detailSeq = 0;

  public AchAuthLogHistoryId() {
  }

  public AchAuthLogHistoryId(int custNo, int facmNo, int detailSeq) {
    this.custNo = custNo;
    this.facmNo = facmNo;
    this.detailSeq = detailSeq;
  }

/**
	* 戶號<br>
	* 
	* @return Integer
	*/
  public int getCustNo() {
    return this.custNo;
  }

/**
	* 戶號<br>
	* 
  *
  * @param custNo 戶號
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

/**
	* 明細序號<br>
	* 該戶號、額度之授權帳號歷程序號
	* @return Integer
	*/
  public int getDetailSeq() {
    return this.detailSeq;
  }

/**
	* 明細序號<br>
	* 該戶號、額度之授權帳號歷程序號
  *
  * @param detailSeq 明細序號
	*/
  public void setDetailSeq(int detailSeq) {
    this.detailSeq = detailSeq;
  }


  @Override
  public int hashCode() {
    return Objects.hash(custNo, facmNo, detailSeq);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    AchAuthLogHistoryId achAuthLogHistoryId = (AchAuthLogHistoryId) obj;
    return custNo == achAuthLogHistoryId.custNo && facmNo == achAuthLogHistoryId.facmNo && detailSeq == achAuthLogHistoryId.detailSeq;
  }

  @Override
  public String toString() {
    return "AchAuthLogHistoryId [custNo=" + custNo + ", facmNo=" + facmNo + ", detailSeq=" + detailSeq + "]";
  }
}
