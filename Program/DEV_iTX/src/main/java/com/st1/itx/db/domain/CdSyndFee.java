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
 * CdSyndFee 聯貸費用代碼檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CdSyndFee`")
public class CdSyndFee implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -5360712441314715226L;

// 聯貸費用代碼
  @Id
  @Column(name = "`SyndFeeCode`", length = 2)
  private String syndFeeCode = " ";

  // 聯貸費用說明
  @Column(name = "`SyndFeeItem`", length = 30)
  private String syndFeeItem;

  // 業務科目代號
  @Column(name = "`AcctCode`", length = 3)
  private String acctCode;

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
	* 聯貸費用代碼<br>
	* 
	* @return String
	*/
  public String getSyndFeeCode() {
    return this.syndFeeCode == null ? "" : this.syndFeeCode;
  }

/**
	* 聯貸費用代碼<br>
	* 
  *
  * @param syndFeeCode 聯貸費用代碼
	*/
  public void setSyndFeeCode(String syndFeeCode) {
    this.syndFeeCode = syndFeeCode;
  }

/**
	* 聯貸費用說明<br>
	* 
	* @return String
	*/
  public String getSyndFeeItem() {
    return this.syndFeeItem == null ? "" : this.syndFeeItem;
  }

/**
	* 聯貸費用說明<br>
	* 
  *
  * @param syndFeeItem 聯貸費用說明
	*/
  public void setSyndFeeItem(String syndFeeItem) {
    this.syndFeeItem = syndFeeItem;
  }

/**
	* 業務科目代號<br>
	* 
	* @return String
	*/
  public String getAcctCode() {
    return this.acctCode == null ? "" : this.acctCode;
  }

/**
	* 業務科目代號<br>
	* 
  *
  * @param acctCode 業務科目代號
	*/
  public void setAcctCode(String acctCode) {
    this.acctCode = acctCode;
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
    return "CdSyndFee [syndFeeCode=" + syndFeeCode + ", syndFeeItem=" + syndFeeItem + ", acctCode=" + acctCode + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
           + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
