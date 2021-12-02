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
 * LoanIfrs9Jp IFRS9欄位清單10<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`LoanIfrs9Jp`")
public class LoanIfrs9Jp implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -7455081651850583248L;

@EmbeddedId
  private LoanIfrs9JpId loanIfrs9JpId;

  // 年月份
  @Column(name = "`DataYM`", insertable = false, updatable = false)
  private int dataYM = 0;

  // 發生時會計日期年月
  @Column(name = "`AcDateYM`", insertable = false, updatable = false)
  private int acDateYM = 0;

  // 戶號
  @Column(name = "`CustNo`", insertable = false, updatable = false)
  private int custNo = 0;

  // 新額度編號
  @Column(name = "`NewFacmNo`", insertable = false, updatable = false)
  private int newFacmNo = 0;

  // 新撥款序號
  @Column(name = "`NewBormNo`", insertable = false, updatable = false)
  private int newBormNo = 0;

  // 舊額度編號
  @Column(name = "`OldFacmNo`", insertable = false, updatable = false)
  private int oldFacmNo = 0;

  // 舊撥款序號
  @Column(name = "`OldBormNo`", insertable = false, updatable = false)
  private int oldBormNo = 0;

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


  public LoanIfrs9JpId getLoanIfrs9JpId() {
    return this.loanIfrs9JpId;
  }

  public void setLoanIfrs9JpId(LoanIfrs9JpId loanIfrs9JpId) {
    this.loanIfrs9JpId = loanIfrs9JpId;
  }

/**
	* 年月份<br>
	* 
	* @return Integer
	*/
  public int getDataYM() {
    return this.dataYM;
  }

/**
	* 年月份<br>
	* 
  *
  * @param dataYM 年月份
	*/
  public void setDataYM(int dataYM) {
    this.dataYM = dataYM;
  }

/**
	* 發生時會計日期年月<br>
	* 
	* @return Integer
	*/
  public int getAcDateYM() {
    return this.acDateYM;
  }

/**
	* 發生時會計日期年月<br>
	* 
  *
  * @param acDateYM 發生時會計日期年月
	*/
  public void setAcDateYM(int acDateYM) {
    this.acDateYM = acDateYM;
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
	* 新額度編號<br>
	* 
	* @return Integer
	*/
  public int getNewFacmNo() {
    return this.newFacmNo;
  }

/**
	* 新額度編號<br>
	* 
  *
  * @param newFacmNo 新額度編號
	*/
  public void setNewFacmNo(int newFacmNo) {
    this.newFacmNo = newFacmNo;
  }

/**
	* 新撥款序號<br>
	* 
	* @return Integer
	*/
  public int getNewBormNo() {
    return this.newBormNo;
  }

/**
	* 新撥款序號<br>
	* 
  *
  * @param newBormNo 新撥款序號
	*/
  public void setNewBormNo(int newBormNo) {
    this.newBormNo = newBormNo;
  }

/**
	* 舊額度編號<br>
	* 
	* @return Integer
	*/
  public int getOldFacmNo() {
    return this.oldFacmNo;
  }

/**
	* 舊額度編號<br>
	* 
  *
  * @param oldFacmNo 舊額度編號
	*/
  public void setOldFacmNo(int oldFacmNo) {
    this.oldFacmNo = oldFacmNo;
  }

/**
	* 舊撥款序號<br>
	* 
	* @return Integer
	*/
  public int getOldBormNo() {
    return this.oldBormNo;
  }

/**
	* 舊撥款序號<br>
	* 
  *
  * @param oldBormNo 舊撥款序號
	*/
  public void setOldBormNo(int oldBormNo) {
    this.oldBormNo = oldBormNo;
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
    return "LoanIfrs9Jp [loanIfrs9JpId=" + loanIfrs9JpId
           + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
