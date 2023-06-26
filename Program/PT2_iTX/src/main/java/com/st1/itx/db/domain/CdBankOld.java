package com.st1.itx.db.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Time;
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
 * CdBankOld 舊行庫資料檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CdBankOld`")
public class CdBankOld implements Serializable {


  @EmbeddedId
  private CdBankOldId cdBankOldId;

  // 行庫代號
  @Column(name = "`BankCode`", length = 3, insertable = false, updatable = false)
  private String bankCode;

  // 分行代號
  @Column(name = "`BranchCode`", length = 4, insertable = false, updatable = false)
  private String branchCode;

  // 行庫名稱
  /* 參考bankno.csv */
  @Column(name = "`BankItem`", length = 50)
  private String bankItem;

  // 分行名稱
  @Column(name = "`BranchItem`", length = 50)
  private String branchItem;

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


  public CdBankOldId getCdBankOldId() {
    return this.cdBankOldId;
  }

  public void setCdBankOldId(CdBankOldId cdBankOldId) {
    this.cdBankOldId = cdBankOldId;
  }

/**
	* 行庫代號<br>
	* 
	* @return String
	*/
  public String getBankCode() {
    return this.bankCode == null ? "" : this.bankCode;
  }

/**
	* 行庫代號<br>
	* 
  *
  * @param bankCode 行庫代號
	*/
  public void setBankCode(String bankCode) {
    this.bankCode = bankCode;
  }

/**
	* 分行代號<br>
	* 
	* @return String
	*/
  public String getBranchCode() {
    return this.branchCode == null ? "" : this.branchCode;
  }

/**
	* 分行代號<br>
	* 
  *
  * @param branchCode 分行代號
	*/
  public void setBranchCode(String branchCode) {
    this.branchCode = branchCode;
  }

/**
	* 行庫名稱<br>
	* 參考bankno.csv
	* @return String
	*/
  public String getBankItem() {
    return this.bankItem == null ? "" : this.bankItem;
  }

/**
	* 行庫名稱<br>
	* 參考bankno.csv
  *
  * @param bankItem 行庫名稱
	*/
  public void setBankItem(String bankItem) {
    this.bankItem = bankItem;
  }

/**
	* 分行名稱<br>
	* 
	* @return String
	*/
  public String getBranchItem() {
    return this.branchItem == null ? "" : this.branchItem;
  }

/**
	* 分行名稱<br>
	* 
  *
  * @param branchItem 分行名稱
	*/
  public void setBranchItem(String branchItem) {
    this.branchItem = branchItem;
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
    return "CdBankOld [cdBankOldId=" + cdBankOldId + ", bankItem=" + bankItem + ", branchItem=" + branchItem + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
           + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
