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
 * CdInsurer 保險公司資料檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CdInsurer`")
public class CdInsurer implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -5095369183823036778L;

@EmbeddedId
  private CdInsurerId cdInsurerId;

  // 公司種類
  /* 1:保險公司2:鑑定公司 */
  @Column(name = "`InsurerType`", length = 1, insertable = false, updatable = false)
  private String insurerType;

  // 公司代號
  @Column(name = "`InsurerCode`", length = 2, insertable = false, updatable = false)
  private String insurerCode;

  // 公司統編
  @Column(name = "`InsurerId`", length = 10)
  private String insurerId;

  // 公司名稱
  /* 目前最長19個中文字美商美國環球產物保險有限公司台灣分公司 */
  @Column(name = "`InsurerItem`", length = 40)
  private String insurerItem;

  // 公司簡稱
  @Column(name = "`InsurerShort`", length = 20)
  private String insurerShort;

  // 連絡電話區碼
  @Column(name = "`TelArea`", length = 5)
  private String telArea;

  // 連絡電話號碼
  @Column(name = "`TelNo`", length = 10)
  private String telNo;

  // 連絡電話分機號碼
  @Column(name = "`TelExt`", length = 5)
  private String telExt;

  // 啟用記號
  /* Y:啟用,N:停用 */
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


  public CdInsurerId getCdInsurerId() {
    return this.cdInsurerId;
  }

  public void setCdInsurerId(CdInsurerId cdInsurerId) {
    this.cdInsurerId = cdInsurerId;
  }

/**
	* 公司種類<br>
	* 1:保險公司
2:鑑定公司
	* @return String
	*/
  public String getInsurerType() {
    return this.insurerType == null ? "" : this.insurerType;
  }

/**
	* 公司種類<br>
	* 1:保險公司
2:鑑定公司
  *
  * @param insurerType 公司種類
	*/
  public void setInsurerType(String insurerType) {
    this.insurerType = insurerType;
  }

/**
	* 公司代號<br>
	* 
	* @return String
	*/
  public String getInsurerCode() {
    return this.insurerCode == null ? "" : this.insurerCode;
  }

/**
	* 公司代號<br>
	* 
  *
  * @param insurerCode 公司代號
	*/
  public void setInsurerCode(String insurerCode) {
    this.insurerCode = insurerCode;
  }

/**
	* 公司統編<br>
	* 
	* @return String
	*/
  public String getInsurerId() {
    return this.insurerId == null ? "" : this.insurerId;
  }

/**
	* 公司統編<br>
	* 
  *
  * @param insurerId 公司統編
	*/
  public void setInsurerId(String insurerId) {
    this.insurerId = insurerId;
  }

/**
	* 公司名稱<br>
	* 目前最長19個中文字
美商美國環球產物保險有限公司台灣分公司
	* @return String
	*/
  public String getInsurerItem() {
    return this.insurerItem == null ? "" : this.insurerItem;
  }

/**
	* 公司名稱<br>
	* 目前最長19個中文字
美商美國環球產物保險有限公司台灣分公司
  *
  * @param insurerItem 公司名稱
	*/
  public void setInsurerItem(String insurerItem) {
    this.insurerItem = insurerItem;
  }

/**
	* 公司簡稱<br>
	* 
	* @return String
	*/
  public String getInsurerShort() {
    return this.insurerShort == null ? "" : this.insurerShort;
  }

/**
	* 公司簡稱<br>
	* 
  *
  * @param insurerShort 公司簡稱
	*/
  public void setInsurerShort(String insurerShort) {
    this.insurerShort = insurerShort;
  }

/**
	* 連絡電話區碼<br>
	* 
	* @return String
	*/
  public String getTelArea() {
    return this.telArea == null ? "" : this.telArea;
  }

/**
	* 連絡電話區碼<br>
	* 
  *
  * @param telArea 連絡電話區碼
	*/
  public void setTelArea(String telArea) {
    this.telArea = telArea;
  }

/**
	* 連絡電話號碼<br>
	* 
	* @return String
	*/
  public String getTelNo() {
    return this.telNo == null ? "" : this.telNo;
  }

/**
	* 連絡電話號碼<br>
	* 
  *
  * @param telNo 連絡電話號碼
	*/
  public void setTelNo(String telNo) {
    this.telNo = telNo;
  }

/**
	* 連絡電話分機號碼<br>
	* 
	* @return String
	*/
  public String getTelExt() {
    return this.telExt == null ? "" : this.telExt;
  }

/**
	* 連絡電話分機號碼<br>
	* 
  *
  * @param telExt 連絡電話分機號碼
	*/
  public void setTelExt(String telExt) {
    this.telExt = telExt;
  }

/**
	* 啟用記號<br>
	* Y:啟用,N:停用
	* @return String
	*/
  public String getEnable() {
    return this.enable == null ? "" : this.enable;
  }

/**
	* 啟用記號<br>
	* Y:啟用,N:停用
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
    return "CdInsurer [cdInsurerId=" + cdInsurerId + ", insurerId=" + insurerId + ", insurerItem=" + insurerItem + ", insurerShort=" + insurerShort + ", telArea=" + telArea
           + ", telNo=" + telNo + ", telExt=" + telExt + ", enable=" + enable + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
           + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
