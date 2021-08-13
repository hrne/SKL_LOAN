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
 * CdSupv 主管理由檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CdSupv`")
public class CdSupv implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -8157851126313292535L;

// 理由代碼
  /* 現行資料長度只有3碼 */
  @Id
  @Column(name = "`SupvReasonCode`", length = 4)
  private String supvReasonCode = " ";

  // 理由說明
  @Column(name = "`SupvReasonItem`", length = 40)
  private String supvReasonItem;

  // 理由階層
  @Column(name = "`SupvReasonLevel`", length = 1)
  private String supvReasonLevel;

  // 啟用記號
  /* Y:啟用 , N:未啟用 */
  @Column(name = "`Enable`", length = 1)
  private String enable;

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
	* 理由代碼<br>
	* 現行資料長度只有3碼
	* @return String
	*/
  public String getSupvReasonCode() {
    return this.supvReasonCode == null ? "" : this.supvReasonCode;
  }

/**
	* 理由代碼<br>
	* 現行資料長度只有3碼
  *
  * @param supvReasonCode 理由代碼
	*/
  public void setSupvReasonCode(String supvReasonCode) {
    this.supvReasonCode = supvReasonCode;
  }

/**
	* 理由說明<br>
	* 
	* @return String
	*/
  public String getSupvReasonItem() {
    return this.supvReasonItem == null ? "" : this.supvReasonItem;
  }

/**
	* 理由說明<br>
	* 
  *
  * @param supvReasonItem 理由說明
	*/
  public void setSupvReasonItem(String supvReasonItem) {
    this.supvReasonItem = supvReasonItem;
  }

/**
	* 理由階層<br>
	* 
	* @return String
	*/
  public String getSupvReasonLevel() {
    return this.supvReasonLevel == null ? "" : this.supvReasonLevel;
  }

/**
	* 理由階層<br>
	* 
  *
  * @param supvReasonLevel 理由階層
	*/
  public void setSupvReasonLevel(String supvReasonLevel) {
    this.supvReasonLevel = supvReasonLevel;
  }

/**
	* 啟用記號<br>
	* Y:啟用 , N:未啟用
	* @return String
	*/
  public String getEnable() {
    return this.enable == null ? "" : this.enable;
  }

/**
	* 啟用記號<br>
	* Y:啟用 , N:未啟用
  *
  * @param enable 啟用記號
	*/
  public void setEnable(String enable) {
    this.enable = enable;
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
    return "CdSupv [supvReasonCode=" + supvReasonCode + ", supvReasonItem=" + supvReasonItem + ", supvReasonLevel=" + supvReasonLevel + ", enable=" + enable + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
           + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
