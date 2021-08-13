package com.st1.itx.db.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.EmbeddedId;
import javax.persistence.Column;

/**
 * TxAuthority 權限檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`TxAuthority`")
public class TxAuthority implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 1569345587577862333L;

@EmbeddedId
  private TxAuthorityId txAuthorityId;

  // 權限群組編號
  @Column(name = "`AuthNo`", length = 6, insertable = false, updatable = false)
  private String authNo;

  // 交易代號
  @Column(name = "`TranNo`", length = 5, insertable = false, updatable = false)
  private String tranNo;

  // 權限記號
  /* 0.無權限1.僅查詢權限2.全部權限 */
  @Column(name = "`AuthFg`")
  private int authFg = 0;

  // 建檔日期時間
  @CreatedDate
  @Column(name = "`CreateDate`")
  private java.sql.Timestamp createDate;

  // 建檔人員
  @Column(name = "`CreateEmpNo`", length = 6)
  private String createEmpNo;

  // 最後更新日期時間
  @LastModifiedDate
  @Column(name = "`LastUpdate`")
  private java.sql.Timestamp lastUpdate;

  // 最後更新人員
  @Column(name = "`LastUpdateEmpNo`", length = 6)
  private String lastUpdateEmpNo;


  public TxAuthorityId getTxAuthorityId() {
    return this.txAuthorityId;
  }

  public void setTxAuthorityId(TxAuthorityId txAuthorityId) {
    this.txAuthorityId = txAuthorityId;
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

/**
	* 權限記號<br>
	* 0.無權限1.僅查詢權限2.全部權限
	* @return Integer
	*/
  public int getAuthFg() {
    return this.authFg;
  }

/**
	* 權限記號<br>
	* 0.無權限1.僅查詢權限2.全部權限
  *
  * @param authFg 權限記號
	*/
  public void setAuthFg(int authFg) {
    this.authFg = authFg;
  }

/**
	* 建檔日期時間<br>
	* 
	* @return java.sql.Timestamp
	*/
  public java.sql.Timestamp getCreateDate() {
    return this.createDate;
  }

/**
	* 建檔日期時間<br>
	* 
  *
  * @param createDate 建檔日期時間
	*/
  public void setCreateDate(java.sql.Timestamp createDate) {
    this.createDate = createDate;
  }

/**
	* 建檔人員<br>
	* 
	* @return String
	*/
  public String getCreateEmpNo() {
    return this.createEmpNo == null ? "" : this.createEmpNo;
  }

/**
	* 建檔人員<br>
	* 
  *
  * @param createEmpNo 建檔人員
	*/
  public void setCreateEmpNo(String createEmpNo) {
    this.createEmpNo = createEmpNo;
  }

/**
	* 最後更新日期時間<br>
	* 
	* @return java.sql.Timestamp
	*/
  public java.sql.Timestamp getLastUpdate() {
    return this.lastUpdate;
  }

/**
	* 最後更新日期時間<br>
	* 
  *
  * @param lastUpdate 最後更新日期時間
	*/
  public void setLastUpdate(java.sql.Timestamp lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

/**
	* 最後更新人員<br>
	* 
	* @return String
	*/
  public String getLastUpdateEmpNo() {
    return this.lastUpdateEmpNo == null ? "" : this.lastUpdateEmpNo;
  }

/**
	* 最後更新人員<br>
	* 
  *
  * @param lastUpdateEmpNo 最後更新人員
	*/
  public void setLastUpdateEmpNo(String lastUpdateEmpNo) {
    this.lastUpdateEmpNo = lastUpdateEmpNo;
  }


  @Override
  public String toString() {
    return "TxAuthority [txAuthorityId=" + txAuthorityId + ", authFg=" + authFg + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
           + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
