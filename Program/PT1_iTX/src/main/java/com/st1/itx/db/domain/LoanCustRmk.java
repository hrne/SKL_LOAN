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
 * LoanCustRmk 帳務備忘錄明細檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`LoanCustRmk`")
public class LoanCustRmk implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 1996294349443828375L;

@EmbeddedId
  private LoanCustRmkId loanCustRmkId;

  // 借款人戶號
  @Column(name = "`CustNo`", insertable = false, updatable = false)
  private int custNo = 0;

  // 會計日期
  /* 新增此筆帳務備忘錄時的系統會計日期 */
  @Column(name = "`AcDate`", insertable = false, updatable = false)
  private int acDate = 0;

  // 備忘錄序號
  @Column(name = "`RmkNo`", insertable = false, updatable = false)
  private int rmkNo = 0;

  // 備忘錄代碼
  /* 共用代碼檔301:309整批入帳備忘說明 */
  @Column(name = "`RmkCode`", length = 3)
  private String rmkCode;

  // 備忘錄說明
  @Column(name = "`RmkDesc`", length = 120)
  private String rmkDesc;

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


  public LoanCustRmkId getLoanCustRmkId() {
    return this.loanCustRmkId;
  }

  public void setLoanCustRmkId(LoanCustRmkId loanCustRmkId) {
    this.loanCustRmkId = loanCustRmkId;
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
	* 會計日期<br>
	* 新增此筆帳務備忘錄時的系統會計日期
	* @return Integer
	*/
  public int getAcDate() {
    return StaticTool.bcToRoc(this.acDate);
  }

/**
	* 會計日期<br>
	* 新增此筆帳務備忘錄時的系統會計日期
  *
  * @param acDate 會計日期
  * @throws LogicException when Date Is Warn	*/
  public void setAcDate(int acDate) throws LogicException {
    this.acDate = StaticTool.rocToBc(acDate);
  }

/**
	* 備忘錄序號<br>
	* 
	* @return Integer
	*/
  public int getRmkNo() {
    return this.rmkNo;
  }

/**
	* 備忘錄序號<br>
	* 
  *
  * @param rmkNo 備忘錄序號
	*/
  public void setRmkNo(int rmkNo) {
    this.rmkNo = rmkNo;
  }

/**
	* 備忘錄代碼<br>
	* 共用代碼檔
301:309整批入帳備忘說明
	* @return String
	*/
  public String getRmkCode() {
    return this.rmkCode == null ? "" : this.rmkCode;
  }

/**
	* 備忘錄代碼<br>
	* 共用代碼檔
301:309整批入帳備忘說明
  *
  * @param rmkCode 備忘錄代碼
	*/
  public void setRmkCode(String rmkCode) {
    this.rmkCode = rmkCode;
  }

/**
	* 備忘錄說明<br>
	* 
	* @return String
	*/
  public String getRmkDesc() {
    return this.rmkDesc == null ? "" : this.rmkDesc;
  }

/**
	* 備忘錄說明<br>
	* 
  *
  * @param rmkDesc 備忘錄說明
	*/
  public void setRmkDesc(String rmkDesc) {
    this.rmkDesc = rmkDesc;
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
    return "LoanCustRmk [loanCustRmkId=" + loanCustRmkId + ", rmkCode=" + rmkCode + ", rmkDesc=" + rmkDesc + ", createDate=" + createDate
           + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
