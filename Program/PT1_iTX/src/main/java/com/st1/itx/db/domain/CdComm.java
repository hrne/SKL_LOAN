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
 * CdComm 雜項代碼檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CdComm`")
public class CdComm implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 6850222877246280909L;

@EmbeddedId
  private CdCommId cdCommId;

  // 代碼類別
  /* 01:政府補貼利率02:專案放款 */
  @Column(name = "`CdType`", length = 5, insertable = false, updatable = false)
  private String cdType;

  // 代碼項目
  /* 01:補貼息02:放款金額 */
  @Column(name = "`CdItem`", length = 5, insertable = false, updatable = false)
  private String cdItem;

  // 生效日期
  /* 專案放款生效日期固定取1日(02:專案放款) */
  @Column(name = "`EffectDate`", insertable = false, updatable = false)
  private int effectDate = 0;

  // 啟用記號
  /* 政府補貼利率: N:未生效(未執行整批利率變更) Y:已生效(不可修改、刪除) */
  @Column(name = "`Enable`", length = 1)
  private String enable;

  // 備註
  @Column(name = "`Remark`", length = 40)
  private String remark;

  // jason格式紀錄欄
  @Column(name = "`JsonFields`", length = 1000)
  private String jsonFields;

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


  public CdCommId getCdCommId() {
    return this.cdCommId;
  }

  public void setCdCommId(CdCommId cdCommId) {
    this.cdCommId = cdCommId;
  }

/**
	* 代碼類別<br>
	* 01:政府補貼利率
02:專案放款
	* @return String
	*/
  public String getCdType() {
    return this.cdType == null ? "" : this.cdType;
  }

/**
	* 代碼類別<br>
	* 01:政府補貼利率
02:專案放款
  *
  * @param cdType 代碼類別
	*/
  public void setCdType(String cdType) {
    this.cdType = cdType;
  }

/**
	* 代碼項目<br>
	* 01:補貼息
02:放款金額
	* @return String
	*/
  public String getCdItem() {
    return this.cdItem == null ? "" : this.cdItem;
  }

/**
	* 代碼項目<br>
	* 01:補貼息
02:放款金額
  *
  * @param cdItem 代碼項目
	*/
  public void setCdItem(String cdItem) {
    this.cdItem = cdItem;
  }

/**
	* 生效日期<br>
	* 專案放款生效日期固定取1日(02:專案放款)
	* @return Integer
	*/
  public int getEffectDate() {
    return StaticTool.bcToRoc(this.effectDate);
  }

/**
	* 生效日期<br>
	* 專案放款生效日期固定取1日(02:專案放款)
  *
  * @param effectDate 生效日期
  * @throws LogicException when Date Is Warn	*/
  public void setEffectDate(int effectDate) throws LogicException {
    this.effectDate = StaticTool.rocToBc(effectDate);
  }

/**
	* 啟用記號<br>
	* 政府補貼利率:
 N:未生效(未執行整批利率變更)
 Y:已生效(不可修改、刪除)
	* @return String
	*/
  public String getEnable() {
    return this.enable == null ? "" : this.enable;
  }

/**
	* 啟用記號<br>
	* 政府補貼利率:
 N:未生效(未執行整批利率變更)
 Y:已生效(不可修改、刪除)
  *
  * @param enable 啟用記號
	*/
  public void setEnable(String enable) {
    this.enable = enable;
  }

/**
	* 備註<br>
	* 
	* @return String
	*/
  public String getRemark() {
    return this.remark == null ? "" : this.remark;
  }

/**
	* 備註<br>
	* 
  *
  * @param remark 備註
	*/
  public void setRemark(String remark) {
    this.remark = remark;
  }

/**
	* jason格式紀錄欄<br>
	* 
	* @return String
	*/
  public String getJsonFields() {
    return this.jsonFields == null ? "" : this.jsonFields;
  }

/**
	* jason格式紀錄欄<br>
	* 
  *
  * @param jsonFields jason格式紀錄欄
	*/
  public void setJsonFields(String jsonFields) {
    this.jsonFields = jsonFields;
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
    return "CdComm [cdCommId=" + cdCommId + ", enable=" + enable + ", remark=" + remark + ", jsonFields=" + jsonFields
           + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
