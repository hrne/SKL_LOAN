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
 * RptJcic 呆帳不報送檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`RptJcic`")
public class RptJcic implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -5535091074445761408L;

@EmbeddedId
  private RptJcicId rptJcicId;

  // 單位別
  @Column(name = "`BranchNo`", length = 4, insertable = false, updatable = false)
  private String branchNo;

  // 戶號
  @Column(name = "`CustNo`", insertable = false, updatable = false)
  private int custNo = 0;

  // 額度號碼
  @Column(name = "`FacmNo`", insertable = false, updatable = false)
  private int facmNo = 0;

  // Jcic名稱
  @Column(name = "`JcicName`", length = 100)
  private String jcicName;

  // Jcic戶況
  @Column(name = "`JcicStatus`")
  private int jcicStatus = 0;

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


  public RptJcicId getRptJcicId() {
    return this.rptJcicId;
  }

  public void setRptJcicId(RptJcicId rptJcicId) {
    this.rptJcicId = rptJcicId;
  }

/**
	* 單位別<br>
	* 
	* @return String
	*/
  public String getBranchNo() {
    return this.branchNo == null ? "" : this.branchNo;
  }

/**
	* 單位別<br>
	* 
  *
  * @param branchNo 單位別
	*/
  public void setBranchNo(String branchNo) {
    this.branchNo = branchNo;
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
	* Jcic名稱<br>
	* 
	* @return String
	*/
  public String getJcicName() {
    return this.jcicName == null ? "" : this.jcicName;
  }

/**
	* Jcic名稱<br>
	* 
  *
  * @param jcicName Jcic名稱
	*/
  public void setJcicName(String jcicName) {
    this.jcicName = jcicName;
  }

/**
	* Jcic戶況<br>
	* 
	* @return Integer
	*/
  public int getJcicStatus() {
    return this.jcicStatus;
  }

/**
	* Jcic戶況<br>
	* 
  *
  * @param jcicStatus Jcic戶況
	*/
  public void setJcicStatus(int jcicStatus) {
    this.jcicStatus = jcicStatus;
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
    return "RptJcic [rptJcicId=" + rptJcicId + ", jcicName=" + jcicName + ", jcicStatus=" + jcicStatus + ", createDate=" + createDate
           + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
