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
 * CollMeet 法催紀錄面催檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CollMeet`")
public class CollMeet implements Serializable {


  @EmbeddedId
  private CollMeetId collMeetId;

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

  // 面催日期
  @Column(name = "`MeetDate`")
  private int meetDate = 0;

  // 面催時間
  /* 2021/8/18 已與舜雯確認只需填入時間此欄位由6碼改成4碼 */
  @Column(name = "`MeetTime`", length = 4)
  private String meetTime;

  // 聯絡對象
  /* 1.借款人 2.保證人 */
  @Column(name = "`ContactCode`", length = 1)
  private String contactCode;

  // 面晤人
  /* 1:本人 2:親屬 3:朋友 4:其他 */
  @Column(name = "`MeetPsnCode`", length = 1)
  private String meetPsnCode;

  // 催收人員
  /* 1:本人 2:代催收 */
  @Column(name = "`CollPsnCode`", length = 1)
  private String collPsnCode;

  // 催收人員員編
  /* 2022/3/1改為員編 */
  @Column(name = "`CollPsnName`", length = 8)
  private String collPsnName;

  // 面催地點選項
  /* 1:戶籍地址 2:通訊地址 3:擔保品地址 4:其他 */
  @Column(name = "`MeetPlaceCode`")
  private int meetPlaceCode = 0;

  // 面催地點
  @Column(name = "`MeetPlace`", length = 60)
  private String meetPlace;

  // 其他記錄
  @Column(name = "`Remark`", length = 500)
  private String remark;

  // 面晤人備註
  /* 2023/8/23新增,舊資料由RECEIVE_PERSONNAME轉入 */
  @Column(name = "`MeetPsnNote`", length = 50)
  private String meetPsnNote;

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


  public CollMeetId getCollMeetId() {
    return this.collMeetId;
  }

  public void setCollMeetId(CollMeetId collMeetId) {
    this.collMeetId = collMeetId;
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
	* 面催日期<br>
	* 
	* @return Integer
	*/
  public int getMeetDate() {
    return StaticTool.bcToRoc(this.meetDate);
  }

/**
	* 面催日期<br>
	* 
  *
  * @param meetDate 面催日期
  * @throws LogicException when Date Is Warn	*/
  public void setMeetDate(int meetDate) throws LogicException {
    this.meetDate = StaticTool.rocToBc(meetDate);
  }

/**
	* 面催時間<br>
	* 2021/8/18 已與舜雯確認只需填入時間此欄位由6碼改成4碼
	* @return String
	*/
  public String getMeetTime() {
    return this.meetTime == null ? "" : this.meetTime;
  }

/**
	* 面催時間<br>
	* 2021/8/18 已與舜雯確認只需填入時間此欄位由6碼改成4碼
  *
  * @param meetTime 面催時間
	*/
  public void setMeetTime(String meetTime) {
    this.meetTime = meetTime;
  }

/**
	* 聯絡對象<br>
	* 1.借款人 2.保證人
	* @return String
	*/
  public String getContactCode() {
    return this.contactCode == null ? "" : this.contactCode;
  }

/**
	* 聯絡對象<br>
	* 1.借款人 2.保證人
  *
  * @param contactCode 聯絡對象
	*/
  public void setContactCode(String contactCode) {
    this.contactCode = contactCode;
  }

/**
	* 面晤人<br>
	* 1:本人 2:親屬 3:朋友 4:其他
	* @return String
	*/
  public String getMeetPsnCode() {
    return this.meetPsnCode == null ? "" : this.meetPsnCode;
  }

/**
	* 面晤人<br>
	* 1:本人 2:親屬 3:朋友 4:其他
  *
  * @param meetPsnCode 面晤人
	*/
  public void setMeetPsnCode(String meetPsnCode) {
    this.meetPsnCode = meetPsnCode;
  }

/**
	* 催收人員<br>
	* 1:本人 2:代催收
	* @return String
	*/
  public String getCollPsnCode() {
    return this.collPsnCode == null ? "" : this.collPsnCode;
  }

/**
	* 催收人員<br>
	* 1:本人 2:代催收
  *
  * @param collPsnCode 催收人員
	*/
  public void setCollPsnCode(String collPsnCode) {
    this.collPsnCode = collPsnCode;
  }

/**
	* 催收人員員編<br>
	* 2022/3/1改為員編
	* @return String
	*/
  public String getCollPsnName() {
    return this.collPsnName == null ? "" : this.collPsnName;
  }

/**
	* 催收人員員編<br>
	* 2022/3/1改為員編
  *
  * @param collPsnName 催收人員員編
	*/
  public void setCollPsnName(String collPsnName) {
    this.collPsnName = collPsnName;
  }

/**
	* 面催地點選項<br>
	* 1:戶籍地址 2:通訊地址 3:擔保品地址 4:其他
	* @return Integer
	*/
  public int getMeetPlaceCode() {
    return this.meetPlaceCode;
  }

/**
	* 面催地點選項<br>
	* 1:戶籍地址 2:通訊地址 3:擔保品地址 4:其他
  *
  * @param meetPlaceCode 面催地點選項
	*/
  public void setMeetPlaceCode(int meetPlaceCode) {
    this.meetPlaceCode = meetPlaceCode;
  }

/**
	* 面催地點<br>
	* 
	* @return String
	*/
  public String getMeetPlace() {
    return this.meetPlace == null ? "" : this.meetPlace;
  }

/**
	* 面催地點<br>
	* 
  *
  * @param meetPlace 面催地點
	*/
  public void setMeetPlace(String meetPlace) {
    this.meetPlace = meetPlace;
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
	* 面晤人備註<br>
	* 2023/8/23新增,舊資料由RECEIVE_PERSONNAME轉入
	* @return String
	*/
  public String getMeetPsnNote() {
    return this.meetPsnNote == null ? "" : this.meetPsnNote;
  }

/**
	* 面晤人備註<br>
	* 2023/8/23新增,舊資料由RECEIVE_PERSONNAME轉入
  *
  * @param meetPsnNote 面晤人備註
	*/
  public void setMeetPsnNote(String meetPsnNote) {
    this.meetPsnNote = meetPsnNote;
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
    return "CollMeet [collMeetId=" + collMeetId
           + ", meetDate=" + meetDate + ", meetTime=" + meetTime + ", contactCode=" + contactCode + ", meetPsnCode=" + meetPsnCode + ", collPsnCode=" + collPsnCode + ", collPsnName=" + collPsnName
           + ", meetPlaceCode=" + meetPlaceCode + ", meetPlace=" + meetPlace + ", remark=" + remark + ", meetPsnNote=" + meetPsnNote + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
           + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
