package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * TxTellerAuth 使用者權限檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class TxTellerAuthId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -8935044265821432790L;

// 使用者編號
  @Column(name = "`TlrNo`", length = 6)
  private String tlrNo = " ";

  // 權限編號
  @Column(name = "`AuthNo`", length = 6)
  private String authNo = " ";

  public TxTellerAuthId() {
  }

  public TxTellerAuthId(String tlrNo, String authNo) {
    this.tlrNo = tlrNo;
    this.authNo = authNo;
  }

/**
	* 使用者編號<br>
	* 
	* @return String
	*/
  public String getTlrNo() {
    return this.tlrNo == null ? "" : this.tlrNo;
  }

/**
	* 使用者編號<br>
	* 
  *
  * @param tlrNo 使用者編號
	*/
  public void setTlrNo(String tlrNo) {
    this.tlrNo = tlrNo;
  }

/**
	* 權限編號<br>
	* 
	* @return String
	*/
  public String getAuthNo() {
    return this.authNo == null ? "" : this.authNo;
  }

/**
	* 權限編號<br>
	* 
  *
  * @param authNo 權限編號
	*/
  public void setAuthNo(String authNo) {
    this.authNo = authNo;
  }


  @Override
  public int hashCode() {
    return Objects.hash(tlrNo, authNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    TxTellerAuthId txTellerAuthId = (TxTellerAuthId) obj;
    return tlrNo.equals(txTellerAuthId.tlrNo) && authNo.equals(txTellerAuthId.authNo);
  }

  @Override
  public String toString() {
    return "TxTellerAuthId [tlrNo=" + tlrNo + ", authNo=" + authNo + "]";
  }
}
