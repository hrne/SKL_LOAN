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
 * CollTel 法催紀錄電催檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CollTel`")
public class CollTel implements Serializable {


  @EmbeddedId
  private CollTelId collTelId;

  // 案件種類
  @Column(name = "`CaseCode`", length = 1, insertable = false, updatable = false)
  private String caseCode;

  // 借款人戶號
  @Column(name = "`CustNo`", insertable = false, updatable = false)
  private int custNo = 0;

  // 額度編號
  @Column(name = "`FacmNo`", insertable = false, updatable = false)
  private int facmNo = 0;

  // 作業日期
  @Column(name = "`AcDate`", insertable = false, updatable = false)
  private int acDate = 0;

  // 經辦
  @Column(name = "`TitaTlrNo`", length = 6, insertable = false, updatable = false)
  private String titaTlrNo;

  // 交易序號
  @Column(name = "`TitaTxtNo`", length = 8, insertable = false, updatable = false)
  private String titaTxtNo;

  // 電催日期
  @Column(name = "`TelDate`")
  private int telDate = 0;

  // 電催時間
  /* 2021/8/18 已與舜雯確認只需填入時間此欄位由6碼改成4碼 */
  @Column(name = "`TelTime`", length = 4)
  private String telTime;

  // 聯絡對象
  /* 1=借款人 2=保證人 */
  @Column(name = "`ContactCode`", length = 1)
  private String contactCode;

  // 接話人
  /* 1=本人 2=親屬 3=朋友 4=其他 */
  @Column(name = "`RecvrCode`", length = 1)
  private String recvrCode;

  // 連絡電話
  @Column(name = "`TelArea`", length = 5)
  private String telArea;

  // 連絡電話
  @Column(name = "`TelNo`", length = 10)
  private String telNo;

  // 連絡電話
  @Column(name = "`TelExt`", length = 5)
  private String telExt;

  // 通話結果
  @Column(name = "`ResultCode`", length = 1)
  private String resultCode;

  // 其他記錄
  @Column(name = "`Remark`", length = 500)
  private String remark;

  // 通話日期
  /* 舊資料由PAID_DATE轉入 */
  @Column(name = "`CallDate`")
  private int callDate = 0;

  // 接話人備註
  /* 2023/8/23新增,舊資料由RECEIVE_PERSONNAME轉入 */
  @Column(name = "`RecvrNote`", length = 50)
  private String recvrNote;

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


  public CollTelId getCollTelId() {
    return this.collTelId;
  }

  public void setCollTelId(CollTelId collTelId) {
    this.collTelId = collTelId;
  }

/**
	* 案件種類<br>
	* 
	* @return String
	*/
  public String getCaseCode() {
    return this.caseCode == null ? "" : this.caseCode;
  }

/**
	* 案件種類<br>
	* 
  *
  * @param caseCode 案件種類
	*/
  public void setCaseCode(String caseCode) {
    this.caseCode = caseCode;
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
	* 額度編號<br>
	* 
	* @return Integer
	*/
  public int getFacmNo() {
    return this.facmNo;
  }

/**
	* 額度編號<br>
	* 
  *
  * @param facmNo 額度編號
	*/
  public void setFacmNo(int facmNo) {
    this.facmNo = facmNo;
  }

/**
	* 作業日期<br>
	* 
	* @return Integer
	*/
  public int getAcDate() {
    return StaticTool.bcToRoc(this.acDate);
  }

/**
	* 作業日期<br>
	* 
  *
  * @param acDate 作業日期
  * @throws LogicException when Date Is Warn	*/
  public void setAcDate(int acDate) throws LogicException {
    this.acDate = StaticTool.rocToBc(acDate);
  }

/**
	* 經辦<br>
	* 
	* @return String
	*/
  public String getTitaTlrNo() {
    return this.titaTlrNo == null ? "" : this.titaTlrNo;
  }

/**
	* 經辦<br>
	* 
  *
  * @param titaTlrNo 經辦
	*/
  public void setTitaTlrNo(String titaTlrNo) {
    this.titaTlrNo = titaTlrNo;
  }

/**
	* 交易序號<br>
	* 
	* @return String
	*/
  public String getTitaTxtNo() {
    return this.titaTxtNo == null ? "" : this.titaTxtNo;
  }

/**
	* 交易序號<br>
	* 
  *
  * @param titaTxtNo 交易序號
	*/
  public void setTitaTxtNo(String titaTxtNo) {
    this.titaTxtNo = titaTxtNo;
  }

/**
	* 電催日期<br>
	* 
	* @return Integer
	*/
  public int getTelDate() {
    return StaticTool.bcToRoc(this.telDate);
  }

/**
	* 電催日期<br>
	* 
  *
  * @param telDate 電催日期
  * @throws LogicException when Date Is Warn	*/
  public void setTelDate(int telDate) throws LogicException {
    this.telDate = StaticTool.rocToBc(telDate);
  }

/**
	* 電催時間<br>
	* 2021/8/18 已與舜雯確認只需填入時間此欄位由6碼改成4碼
	* @return String
	*/
  public String getTelTime() {
    return this.telTime == null ? "" : this.telTime;
  }

/**
	* 電催時間<br>
	* 2021/8/18 已與舜雯確認只需填入時間此欄位由6碼改成4碼
  *
  * @param telTime 電催時間
	*/
  public void setTelTime(String telTime) {
    this.telTime = telTime;
  }

/**
	* 聯絡對象<br>
	* 1=借款人 2=保證人
	* @return String
	*/
  public String getContactCode() {
    return this.contactCode == null ? "" : this.contactCode;
  }

/**
	* 聯絡對象<br>
	* 1=借款人 2=保證人
  *
  * @param contactCode 聯絡對象
	*/
  public void setContactCode(String contactCode) {
    this.contactCode = contactCode;
  }

/**
	* 接話人<br>
	* 1=本人 2=親屬 3=朋友 4=其他
	* @return String
	*/
  public String getRecvrCode() {
    return this.recvrCode == null ? "" : this.recvrCode;
  }

/**
	* 接話人<br>
	* 1=本人 2=親屬 3=朋友 4=其他
  *
  * @param recvrCode 接話人
	*/
  public void setRecvrCode(String recvrCode) {
    this.recvrCode = recvrCode;
  }

/**
	* 連絡電話<br>
	* 
	* @return String
	*/
  public String getTelArea() {
    return this.telArea == null ? "" : this.telArea;
  }

/**
	* 連絡電話<br>
	* 
  *
  * @param telArea 連絡電話
	*/
  public void setTelArea(String telArea) {
    this.telArea = telArea;
  }

/**
	* 連絡電話<br>
	* 
	* @return String
	*/
  public String getTelNo() {
    return this.telNo == null ? "" : this.telNo;
  }

/**
	* 連絡電話<br>
	* 
  *
  * @param telNo 連絡電話
	*/
  public void setTelNo(String telNo) {
    this.telNo = telNo;
  }

/**
	* 連絡電話<br>
	* 
	* @return String
	*/
  public String getTelExt() {
    return this.telExt == null ? "" : this.telExt;
  }

/**
	* 連絡電話<br>
	* 
  *
  * @param telExt 連絡電話
	*/
  public void setTelExt(String telExt) {
    this.telExt = telExt;
  }

/**
	* 通話結果<br>
	* 
	* @return String
	*/
  public String getResultCode() {
    return this.resultCode == null ? "" : this.resultCode;
  }

/**
	* 通話結果<br>
	* 
  *
  * @param resultCode 通話結果
	*/
  public void setResultCode(String resultCode) {
    this.resultCode = resultCode;
  }

/**
	* 其他記錄<br>
	* 
	* @return String
	*/
  public String getRemark() {
    return this.remark == null ? "" : this.remark;
  }

/**
	* 其他記錄<br>
	* 
  *
  * @param remark 其他記錄
	*/
  public void setRemark(String remark) {
    this.remark = remark;
  }

/**
	* 通話日期<br>
	* 舊資料由PAID_DATE轉入
	* @return Integer
	*/
  public int getCallDate() {
    return StaticTool.bcToRoc(this.callDate);
  }

/**
	* 通話日期<br>
	* 舊資料由PAID_DATE轉入
  *
  * @param callDate 通話日期
  * @throws LogicException when Date Is Warn	*/
  public void setCallDate(int callDate) throws LogicException {
    this.callDate = StaticTool.rocToBc(callDate);
  }

/**
	* 接話人備註<br>
	* 2023/8/23新增,舊資料由RECEIVE_PERSONNAME轉入
	* @return String
	*/
  public String getRecvrNote() {
    return this.recvrNote == null ? "" : this.recvrNote;
  }

/**
	* 接話人備註<br>
	* 2023/8/23新增,舊資料由RECEIVE_PERSONNAME轉入
  *
  * @param recvrNote 接話人備註
	*/
  public void setRecvrNote(String recvrNote) {
    this.recvrNote = recvrNote;
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
    return "CollTel [collTelId=" + collTelId
           + ", telDate=" + telDate + ", telTime=" + telTime + ", contactCode=" + contactCode + ", recvrCode=" + recvrCode + ", telArea=" + telArea + ", telNo=" + telNo
           + ", telExt=" + telExt + ", resultCode=" + resultCode + ", remark=" + remark + ", callDate=" + callDate + ", recvrNote=" + recvrNote + ", createDate=" + createDate
           + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
