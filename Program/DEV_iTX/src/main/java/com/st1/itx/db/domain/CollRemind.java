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
 * CollRemind 法催紀錄提醒事項檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CollRemind`")
public class CollRemind implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -4440767822709669627L;

@EmbeddedId
  private CollRemindId collRemindId;

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

  // 狀態
  /* 0.已到期 1.有效 4.已刪除 6.已解除 */
  @Column(name = "`CondCode`", length = 1)
  private String condCode;

  // 提醒日期
  @Column(name = "`RemindDate`")
  private int remindDate = 0;

  // 維護日期
  @Column(name = "`EditDate`")
  private int editDate = 0;

  // 維護時間
  /* 詢問過user意見，時間存入4碼即可 */
  @Column(name = "`EditTime`", length = 4)
  private String editTime;

  // 提醒項目
  @Column(name = "`RemindCode`", length = 2)
  private String remindCode;

  // 其他記錄
  @Column(name = "`Remark`", length = 500)
  private String remark;

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


  public CollRemindId getCollRemindId() {
    return this.collRemindId;
  }

  public void setCollRemindId(CollRemindId collRemindId) {
    this.collRemindId = collRemindId;
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
	* 狀態<br>
	* 0.已到期 1.有效 4.已刪除 6.已解除
	* @return String
	*/
  public String getCondCode() {
    return this.condCode == null ? "" : this.condCode;
  }

/**
	* 狀態<br>
	* 0.已到期 1.有效 4.已刪除 6.已解除
  *
  * @param condCode 狀態
	*/
  public void setCondCode(String condCode) {
    this.condCode = condCode;
  }

/**
	* 提醒日期<br>
	* 
	* @return Integer
	*/
  public int getRemindDate() {
    return StaticTool.bcToRoc(this.remindDate);
  }

/**
	* 提醒日期<br>
	* 
  *
  * @param remindDate 提醒日期
  * @throws LogicException when Date Is Warn	*/
  public void setRemindDate(int remindDate) throws LogicException {
    this.remindDate = StaticTool.rocToBc(remindDate);
  }

/**
	* 維護日期<br>
	* 
	* @return Integer
	*/
  public int getEditDate() {
    return StaticTool.bcToRoc(this.editDate);
  }

/**
	* 維護日期<br>
	* 
  *
  * @param editDate 維護日期
  * @throws LogicException when Date Is Warn	*/
  public void setEditDate(int editDate) throws LogicException {
    this.editDate = StaticTool.rocToBc(editDate);
  }

/**
	* 維護時間<br>
	* 詢問過user意見，時間存入4碼即可
	* @return String
	*/
  public String getEditTime() {
    return this.editTime == null ? "" : this.editTime;
  }

/**
	* 維護時間<br>
	* 詢問過user意見，時間存入4碼即可
  *
  * @param editTime 維護時間
	*/
  public void setEditTime(String editTime) {
    this.editTime = editTime;
  }

/**
	* 提醒項目<br>
	* 
	* @return String
	*/
  public String getRemindCode() {
    return this.remindCode == null ? "" : this.remindCode;
  }

/**
	* 提醒項目<br>
	* 
  *
  * @param remindCode 提醒項目
	*/
  public void setRemindCode(String remindCode) {
    this.remindCode = remindCode;
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
    return "CollRemind [collRemindId=" + collRemindId
           + ", condCode=" + condCode + ", remindDate=" + remindDate + ", editDate=" + editDate + ", editTime=" + editTime + ", remindCode=" + remindCode + ", remark=" + remark
           + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
