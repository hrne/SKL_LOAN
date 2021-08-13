package com.st1.itx.db.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.Id;
import javax.persistence.Column;

/**
 * CustRelMain 客戶關係人/關係企業資料維護主檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CustRelMain`")
public class CustRelMain implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -3329438223157672427L;

// 客戶識別碼
  @Id
  @Column(name = "`Ukey`", length = 32)
  private String ukey = " ";

  // 客戶統編
  @Column(name = "`CustRelId`", length = 11)
  private String custRelId;

  // 客戶名稱
  @Column(name = "`CustRelName`", length = 70)
  private String custRelName;

  // 護照或居留證
  /* 0:否1:是 */
  @Column(name = "`IsForeigner`", length = 1)
  private String isForeigner;

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


/**
	* 客戶識別碼<br>
	* 
	* @return String
	*/
  public String getUkey() {
    return this.ukey == null ? "" : this.ukey;
  }

/**
	* 客戶識別碼<br>
	* 
  *
  * @param ukey 客戶識別碼
	*/
  public void setUkey(String ukey) {
    this.ukey = ukey;
  }

/**
	* 客戶統編<br>
	* 
	* @return String
	*/
  public String getCustRelId() {
    return this.custRelId == null ? "" : this.custRelId;
  }

/**
	* 客戶統編<br>
	* 
  *
  * @param custRelId 客戶統編
	*/
  public void setCustRelId(String custRelId) {
    this.custRelId = custRelId;
  }

/**
	* 客戶名稱<br>
	* 
	* @return String
	*/
  public String getCustRelName() {
    return this.custRelName == null ? "" : this.custRelName;
  }

/**
	* 客戶名稱<br>
	* 
  *
  * @param custRelName 客戶名稱
	*/
  public void setCustRelName(String custRelName) {
    this.custRelName = custRelName;
  }

/**
	* 護照或居留證<br>
	* 0:否
1:是
	* @return String
	*/
  public String getIsForeigner() {
    return this.isForeigner == null ? "" : this.isForeigner;
  }

/**
	* 護照或居留證<br>
	* 0:否
1:是
  *
  * @param isForeigner 護照或居留證
	*/
  public void setIsForeigner(String isForeigner) {
    this.isForeigner = isForeigner;
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
    return "CustRelMain [ukey=" + ukey + ", custRelId=" + custRelId + ", custRelName=" + custRelName + ", isForeigner=" + isForeigner + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
           + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
