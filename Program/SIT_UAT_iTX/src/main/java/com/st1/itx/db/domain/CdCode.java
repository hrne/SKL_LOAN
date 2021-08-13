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
 * CdCode 共用代碼檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CdCode`")
public class CdCode implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -3577505854559583230L;

@EmbeddedId
  private CdCodeId cdCodeId;

  // 代碼檔代號
  @Column(name = "`DefCode`", length = 20, insertable = false, updatable = false)
  private String defCode;

  // 代碼檔業務類別
  /* 01:顧客管理作業02:業務作業03:帳務作業04:批次作業05:管理性作業06:共同作業07:介接外部系統08:遵循法令作業09:報表作業 */
  @Column(name = "`DefType`")
  private int defType = 0;

  // 代碼
  @Column(name = "`Code`", length = 20, insertable = false, updatable = false)
  private String code;

  // 代碼說明
  @Column(name = "`Item`", length = 50)
  private String item;

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


  public CdCodeId getCdCodeId() {
    return this.cdCodeId;
  }

  public void setCdCodeId(CdCodeId cdCodeId) {
    this.cdCodeId = cdCodeId;
  }

/**
	* 代碼檔代號<br>
	* 
	* @return String
	*/
  public String getDefCode() {
    return this.defCode == null ? "" : this.defCode;
  }

/**
	* 代碼檔代號<br>
	* 
  *
  * @param defCode 代碼檔代號
	*/
  public void setDefCode(String defCode) {
    this.defCode = defCode;
  }

/**
	* 代碼檔業務類別<br>
	* 01:顧客管理作業
02:業務作業
03:帳務作業
04:批次作業
05:管理性作業
06:共同作業
07:介接外部系統
08:遵循法令作業
09:報表作業
	* @return Integer
	*/
  public int getDefType() {
    return this.defType;
  }

/**
	* 代碼檔業務類別<br>
	* 01:顧客管理作業
02:業務作業
03:帳務作業
04:批次作業
05:管理性作業
06:共同作業
07:介接外部系統
08:遵循法令作業
09:報表作業
  *
  * @param defType 代碼檔業務類別
	*/
  public void setDefType(int defType) {
    this.defType = defType;
  }

/**
	* 代碼<br>
	* 
	* @return String
	*/
  public String getCode() {
    return this.code == null ? "" : this.code;
  }

/**
	* 代碼<br>
	* 
  *
  * @param code 代碼
	*/
  public void setCode(String code) {
    this.code = code;
  }

/**
	* 代碼說明<br>
	* 
	* @return String
	*/
  public String getItem() {
    return this.item == null ? "" : this.item;
  }

/**
	* 代碼說明<br>
	* 
  *
  * @param item 代碼說明
	*/
  public void setItem(String item) {
    this.item = item;
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
    return "CdCode [cdCodeId=" + cdCodeId + ", defType=" + defType + ", item=" + item + ", enable=" + enable + ", createDate=" + createDate
           + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
