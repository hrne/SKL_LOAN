package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * TxAuthority 權限檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class TxAuthorityId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -3040919831468844499L;

// 權限群組編號
  @Column(name = "`AuthNo`", length = 6)
  private String authNo = " ";

  // 交易代號
  @Column(name = "`TranNo`", length = 5)
  private String tranNo = " ";

  public TxAuthorityId() {
  }

  public TxAuthorityId(String authNo, String tranNo) {
    this.authNo = authNo;
    this.tranNo = tranNo;
  }

/**
	* 權限群組編號<br>
	* 
	* @return String
	*/
  public String getAuthNo() {
    return this.authNo == null ? "" : this.authNo;
  }

/**
	* 權限群組編號<br>
	* 
  *
  * @param authNo 權限群組編號
	*/
  public void setAuthNo(String authNo) {
    this.authNo = authNo;
  }

/**
	* 交易代號<br>
	* 
	* @return String
	*/
  public String getTranNo() {
    return this.tranNo == null ? "" : this.tranNo;
  }

/**
	* 交易代號<br>
	* 
  *
  * @param tranNo 交易代號
	*/
  public void setTranNo(String tranNo) {
    this.tranNo = tranNo;
  }


  @Override
  public int hashCode() {
    return Objects.hash(authNo, tranNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    TxAuthorityId txAuthorityId = (TxAuthorityId) obj;
    return authNo.equals(txAuthorityId.authNo) && tranNo.equals(txAuthorityId.tranNo);
  }

  @Override
  public String toString() {
    return "TxAuthorityId [authNo=" + authNo + ", tranNo=" + tranNo + "]";
  }
}
