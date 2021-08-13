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
 * CustRelDetail 客戶關係人/關係企業資料維護明細檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CustRelDetail`")
public class CustRelDetail implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -887029553578770990L;

@EmbeddedId
  private CustRelDetailId custRelDetailId;

  // 客戶識別碼
  @Column(name = "`CustRelMainUKey`", length = 32, insertable = false, updatable = false)
  private String custRelMainUKey;

  // 關聯戶識別碼
  @Column(name = "`Ukey`", length = 32, insertable = false, updatable = false)
  private String ukey;

  // 關係別
  /* 共用代碼檔1:關係人2:關係企業3:所營事業4:關係人所營事業 */
  @Column(name = "`RelTypeCode`", length = 1)
  private String relTypeCode;

  // 關係人統編
  @Column(name = "`RelId`", length = 11)
  private String relId;

  // 關係人姓名
  @Column(name = "`RelName`", length = 70)
  private String relName;

  // 關係
  @Column(name = "`RelationCode`", length = 2)
  private String relationCode;

  // 備註類型
  @Column(name = "`RemarkTypeCode`", length = 2)
  private String remarkTypeCode;

  // 備註
  @Column(name = "`Remark`", length = 100)
  private String remark;

  // 說明
  @Column(name = "`Note`", length = 500)
  private String note;

  // 狀態
  /* 0:停用1:啟用 */
  @Column(name = "`Status`", length = 1)
  private String status;

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


  public CustRelDetailId getCustRelDetailId() {
    return this.custRelDetailId;
  }

  public void setCustRelDetailId(CustRelDetailId custRelDetailId) {
    this.custRelDetailId = custRelDetailId;
  }

/**
	* 客戶識別碼<br>
	* 
	* @return String
	*/
  public String getCustRelMainUKey() {
    return this.custRelMainUKey == null ? "" : this.custRelMainUKey;
  }

/**
	* 客戶識別碼<br>
	* 
  *
  * @param custRelMainUKey 客戶識別碼
	*/
  public void setCustRelMainUKey(String custRelMainUKey) {
    this.custRelMainUKey = custRelMainUKey;
  }

/**
	* 關聯戶識別碼<br>
	* 
	* @return String
	*/
  public String getUkey() {
    return this.ukey == null ? "" : this.ukey;
  }

/**
	* 關聯戶識別碼<br>
	* 
  *
  * @param ukey 關聯戶識別碼
	*/
  public void setUkey(String ukey) {
    this.ukey = ukey;
  }

/**
	* 關係別<br>
	* 共用代碼檔
1:關係人
2:關係企業
3:所營事業
4:關係人所營事業
	* @return String
	*/
  public String getRelTypeCode() {
    return this.relTypeCode == null ? "" : this.relTypeCode;
  }

/**
	* 關係別<br>
	* 共用代碼檔
1:關係人
2:關係企業
3:所營事業
4:關係人所營事業
  *
  * @param relTypeCode 關係別
	*/
  public void setRelTypeCode(String relTypeCode) {
    this.relTypeCode = relTypeCode;
  }

/**
	* 關係人統編<br>
	* 
	* @return String
	*/
  public String getRelId() {
    return this.relId == null ? "" : this.relId;
  }

/**
	* 關係人統編<br>
	* 
  *
  * @param relId 關係人統編
	*/
  public void setRelId(String relId) {
    this.relId = relId;
  }

/**
	* 關係人姓名<br>
	* 
	* @return String
	*/
  public String getRelName() {
    return this.relName == null ? "" : this.relName;
  }

/**
	* 關係人姓名<br>
	* 
  *
  * @param relName 關係人姓名
	*/
  public void setRelName(String relName) {
    this.relName = relName;
  }

/**
	* 關係<br>
	* 
	* @return String
	*/
  public String getRelationCode() {
    return this.relationCode == null ? "" : this.relationCode;
  }

/**
	* 關係<br>
	* 
  *
  * @param relationCode 關係
	*/
  public void setRelationCode(String relationCode) {
    this.relationCode = relationCode;
  }

/**
	* 備註類型<br>
	* 
	* @return String
	*/
  public String getRemarkTypeCode() {
    return this.remarkTypeCode == null ? "" : this.remarkTypeCode;
  }

/**
	* 備註類型<br>
	* 
  *
  * @param remarkTypeCode 備註類型
	*/
  public void setRemarkTypeCode(String remarkTypeCode) {
    this.remarkTypeCode = remarkTypeCode;
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
	* 說明<br>
	* 
	* @return String
	*/
  public String getNote() {
    return this.note == null ? "" : this.note;
  }

/**
	* 說明<br>
	* 
  *
  * @param note 說明
	*/
  public void setNote(String note) {
    this.note = note;
  }

/**
	* 狀態<br>
	* 0:停用
1:啟用
	* @return String
	*/
  public String getStatus() {
    return this.status == null ? "" : this.status;
  }

/**
	* 狀態<br>
	* 0:停用
1:啟用
  *
  * @param status 狀態
	*/
  public void setStatus(String status) {
    this.status = status;
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
    return "CustRelDetail [custRelDetailId=" + custRelDetailId + ", relTypeCode=" + relTypeCode + ", relId=" + relId + ", relName=" + relName + ", relationCode=" + relationCode
           + ", remarkTypeCode=" + remarkTypeCode + ", remark=" + remark + ", note=" + note + ", status=" + status + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
           + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
