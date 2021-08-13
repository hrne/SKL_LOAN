package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * PostAuthLogHistory 郵局帳號授權記錄檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class PostAuthLogHistoryId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 2451297300639461485L;

// 戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 額度
  @Column(name = "`FacmNo`")
  private int facmNo = 0;

  // 授權方式
  /* 1期款2火險 */
  @Column(name = "`AuthCode`", length = 1)
  private String authCode = " ";

  // 明細序號
  /* 該戶號、額度之授權帳號歷程序號 */
  @Column(name = "`DetailSeq`")
  private int detailSeq = 0;

  public PostAuthLogHistoryId() {
  }

  public PostAuthLogHistoryId(int custNo, int facmNo, String authCode, int detailSeq) {
    this.custNo = custNo;
    this.facmNo = facmNo;
    this.authCode = authCode;
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
	* 額度<br>
	* 
	* @return Integer
	*/
  public int getFacmNo() {
    return this.facmNo;
  }

/**
	* 額度<br>
	* 
  *
  * @param facmNo 額度
	*/
  public void setFacmNo(int facmNo) {
    this.facmNo = facmNo;
  }

/**
	* 授權方式<br>
	* 1期款2火險
	* @return String
	*/
  public String getAuthCode() {
    return this.authCode == null ? "" : this.authCode;
  }

/**
	* 授權方式<br>
	* 1期款2火險
  *
  * @param authCode 授權方式
	*/
  public void setAuthCode(String authCode) {
    this.authCode = authCode;
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
    return Objects.hash(custNo, facmNo, authCode, detailSeq);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    PostAuthLogHistoryId postAuthLogHistoryId = (PostAuthLogHistoryId) obj;
    return custNo == postAuthLogHistoryId.custNo && facmNo == postAuthLogHistoryId.facmNo && authCode.equals(postAuthLogHistoryId.authCode) && detailSeq == postAuthLogHistoryId.detailSeq;
  }

  @Override
  public String toString() {
    return "PostAuthLogHistoryId [custNo=" + custNo + ", facmNo=" + facmNo + ", authCode=" + authCode + ", detailSeq=" + detailSeq + "]";
  }
}
