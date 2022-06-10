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
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * AcLoanRenew 會計借新還舊檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`AcLoanRenew`")
public class AcLoanRenew implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -8067053747456000169L;

@EmbeddedId
  private AcLoanRenewId acLoanRenewId;

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

  // 展期記號
  /* 代碼檔: 02-業務作業RenewCode展期記號1:一般2:協議 */
  @Column(name = "`RenewCode`", length = 1)
  private String renewCode;

  // 主要記號
  /* Y.新撥款找舊撥款時，取舊撥款第一筆為Y新對舊為一對多時第一筆為Y,其餘為N */
  @Column(name = "`MainFlag`", length = 1)
  private String mainFlag;

  // 會計日期
  @Column(name = "`AcDate`")
  private int acDate = 0;

  // 其他欄位
  @Column(name = "`OtherFields`", length = 2000)
  private String otherFields;

  // 建檔人員
  @Column(name = "`CreateEmpNo`", length = 6)
  private String createEmpNo;

  // 建檔日期
  @CreatedDate
  @Column(name = "`CreateDate`")
  private java.sql.Timestamp createDate;

  // 最後維護人員
  @Column(name = "`LastUpdateEmpNo`", length = 6)
  private String lastUpdateEmpNo;

  // 最後維護日期
  @LastModifiedDate
  @Column(name = "`LastUpdate`")
  private java.sql.Timestamp lastUpdate;


  public AcLoanRenewId getAcLoanRenewId() {
    return this.acLoanRenewId;
  }

  public void setAcLoanRenewId(AcLoanRenewId acLoanRenewId) {
    this.acLoanRenewId = acLoanRenewId;
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
	* 展期記號<br>
	* 代碼檔: 02-業務作業
RenewCode展期記號
1:一般
2:協議
	* @return String
	*/
  public String getRenewCode() {
    return this.renewCode == null ? "" : this.renewCode;
  }

/**
	* 展期記號<br>
	* 代碼檔: 02-業務作業
RenewCode展期記號
1:一般
2:協議
  *
  * @param renewCode 展期記號
	*/
  public void setRenewCode(String renewCode) {
    this.renewCode = renewCode;
  }

/**
	* 主要記號<br>
	* Y.新撥款找舊撥款時，取舊撥款第一筆為Y
新對舊為一對多時第一筆為Y,其餘為N
	* @return String
	*/
  public String getMainFlag() {
    return this.mainFlag == null ? "" : this.mainFlag;
  }

/**
	* 主要記號<br>
	* Y.新撥款找舊撥款時，取舊撥款第一筆為Y
新對舊為一對多時第一筆為Y,其餘為N
  *
  * @param mainFlag 主要記號
	*/
  public void setMainFlag(String mainFlag) {
    this.mainFlag = mainFlag;
  }

/**
	* 會計日期<br>
	* 
	* @return Integer
	*/
  public int getAcDate() {
    return StaticTool.bcToRoc(this.acDate);
  }

/**
	* 會計日期<br>
	* 
  *
  * @param acDate 會計日期
  * @throws LogicException when Date Is Warn	*/
  public void setAcDate(int acDate) throws LogicException {
    this.acDate = StaticTool.rocToBc(acDate);
  }

/**
	* 其他欄位<br>
	* 
	* @return String
	*/
  public String getOtherFields() {
    return this.otherFields == null ? "" : this.otherFields;
  }

/**
	* 其他欄位<br>
	* 
  *
  * @param otherFields 其他欄位
	*/
  public void setOtherFields(String otherFields) {
    this.otherFields = otherFields;
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
	* 建檔日期<br>
	* 
	* @return java.sql.Timestamp
	*/
  public java.sql.Timestamp getCreateDate() {
    return this.createDate;
  }

/**
	* 建檔日期<br>
	* 
  *
  * @param createDate 建檔日期
	*/
  public void setCreateDate(java.sql.Timestamp createDate) {
    this.createDate = createDate;
  }

/**
	* 最後維護人員<br>
	* 
	* @return String
	*/
  public String getLastUpdateEmpNo() {
    return this.lastUpdateEmpNo == null ? "" : this.lastUpdateEmpNo;
  }

/**
	* 最後維護人員<br>
	* 
  *
  * @param lastUpdateEmpNo 最後維護人員
	*/
  public void setLastUpdateEmpNo(String lastUpdateEmpNo) {
    this.lastUpdateEmpNo = lastUpdateEmpNo;
  }

/**
	* 最後維護日期<br>
	* 
	* @return java.sql.Timestamp
	*/
  public java.sql.Timestamp getLastUpdate() {
    return this.lastUpdate;
  }

/**
	* 最後維護日期<br>
	* 
  *
  * @param lastUpdate 最後維護日期
	*/
  public void setLastUpdate(java.sql.Timestamp lastUpdate) {
    this.lastUpdate = lastUpdate;
  }


  @Override
  public String toString() {
    return "AcLoanRenew [acLoanRenewId=" + acLoanRenewId + ", renewCode=" + renewCode
           + ", mainFlag=" + mainFlag + ", acDate=" + acDate + ", otherFields=" + otherFields + ", createEmpNo=" + createEmpNo + ", createDate=" + createDate + ", lastUpdateEmpNo=" + lastUpdateEmpNo
           + ", lastUpdate=" + lastUpdate + "]";
  }
}
