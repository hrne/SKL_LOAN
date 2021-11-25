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
 * NegFinAcct 債務協商債權機構帳戶檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`NegFinAcct`")
public class NegFinAcct implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 3385564391147560673L;

// 債權機構代號
  @Id
  @Column(name = "`FinCode`", length = 8)
  private String finCode = " ";

  // 債權機構名稱
  @Column(name = "`FinItem`", length = 60)
  private String finItem;

  // 匯款銀行
  @Column(name = "`RemitBank`", length = 7)
  private String remitBank;

  // 匯款帳號
  @Column(name = "`RemitAcct`", length = 16)
  private String remitAcct;

  // 資料傳送單位
  @Column(name = "`DataSendSection`", length = 8)
  private String dataSendSection;

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
	* 債權機構代號<br>
	* 
	* @return String
	*/
  public String getFinCode() {
    return this.finCode == null ? "" : this.finCode;
  }

/**
	* 債權機構代號<br>
	* 
  *
  * @param finCode 債權機構代號
	*/
  public void setFinCode(String finCode) {
    this.finCode = finCode;
  }

/**
	* 債權機構名稱<br>
	* 
	* @return String
	*/
  public String getFinItem() {
    return this.finItem == null ? "" : this.finItem;
  }

/**
	* 債權機構名稱<br>
	* 
  *
  * @param finItem 債權機構名稱
	*/
  public void setFinItem(String finItem) {
    this.finItem = finItem;
  }

/**
	* 匯款銀行<br>
	* 
	* @return String
	*/
  public String getRemitBank() {
    return this.remitBank == null ? "" : this.remitBank;
  }

/**
	* 匯款銀行<br>
	* 
  *
  * @param remitBank 匯款銀行
	*/
  public void setRemitBank(String remitBank) {
    this.remitBank = remitBank;
  }

/**
	* 匯款帳號<br>
	* 
	* @return String
	*/
  public String getRemitAcct() {
    return this.remitAcct == null ? "" : this.remitAcct;
  }

/**
	* 匯款帳號<br>
	* 
  *
  * @param remitAcct 匯款帳號
	*/
  public void setRemitAcct(String remitAcct) {
    this.remitAcct = remitAcct;
  }

/**
	* 資料傳送單位<br>
	* 
	* @return String
	*/
  public String getDataSendSection() {
    return this.dataSendSection == null ? "" : this.dataSendSection;
  }

/**
	* 資料傳送單位<br>
	* 
  *
  * @param dataSendSection 資料傳送單位
	*/
  public void setDataSendSection(String dataSendSection) {
    this.dataSendSection = dataSendSection;
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
    return "NegFinAcct [finCode=" + finCode + ", finItem=" + finItem + ", remitBank=" + remitBank + ", remitAcct=" + remitAcct + ", dataSendSection=" + dataSendSection + ", createDate=" + createDate
           + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
