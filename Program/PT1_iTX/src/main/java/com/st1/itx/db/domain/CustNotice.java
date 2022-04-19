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
 * CustNotice 客戶通知設定檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CustNotice`")
public class CustNotice implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 3870560472500330273L;

@EmbeddedId
  private CustNoticeId custNoticeId;

  // 戶號
  @Column(name = "`CustNo`", insertable = false, updatable = false)
  private int custNo = 0;

  // 額度編號
  @Column(name = "`FacmNo`", insertable = false, updatable = false)
  private int facmNo = 0;

  // 報表代號
  @Column(name = "`FormNo`", length = 10, insertable = false, updatable = false)
  private String formNo;

  // 書面通知與否
  /* Y:寄送N:不寄送 */
  @Column(name = "`PaperNotice`", length = 1)
  private String paperNotice;

  // 簡訊發送與否
  /* Y:發送N:不發送 */
  @Column(name = "`MsgNotice`", length = 1)
  private String msgNotice;

  // 電子郵件發送與否
  /* Y:發送N:不發送 */
  @Column(name = "`EmailNotice`", length = 1)
  private String emailNotice;

  // 申請日期
  @Column(name = "`ApplyDate`")
  private int applyDate = 0;

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


  public CustNoticeId getCustNoticeId() {
    return this.custNoticeId;
  }

  public void setCustNoticeId(CustNoticeId custNoticeId) {
    this.custNoticeId = custNoticeId;
  }

/**
	* 戶號<br>
	* 
	* @return Integer
	*/
  public int getCustNo() {
    return this.custNo;
  }

/**
	* 戶號<br>
	* 
  *
  * @param custNo 戶號
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
	* 報表代號<br>
	* 
	* @return String
	*/
  public String getFormNo() {
    return this.formNo == null ? "" : this.formNo;
  }

/**
	* 報表代號<br>
	* 
  *
  * @param formNo 報表代號
	*/
  public void setFormNo(String formNo) {
    this.formNo = formNo;
  }

/**
	* 書面通知與否<br>
	* Y:寄送
N:不寄送
	* @return String
	*/
  public String getPaperNotice() {
    return this.paperNotice == null ? "" : this.paperNotice;
  }

/**
	* 書面通知與否<br>
	* Y:寄送
N:不寄送
  *
  * @param paperNotice 書面通知與否
	*/
  public void setPaperNotice(String paperNotice) {
    this.paperNotice = paperNotice;
  }

/**
	* 簡訊發送與否<br>
	* Y:發送
N:不發送
	* @return String
	*/
  public String getMsgNotice() {
    return this.msgNotice == null ? "" : this.msgNotice;
  }

/**
	* 簡訊發送與否<br>
	* Y:發送
N:不發送
  *
  * @param msgNotice 簡訊發送與否
	*/
  public void setMsgNotice(String msgNotice) {
    this.msgNotice = msgNotice;
  }

/**
	* 電子郵件發送與否<br>
	* Y:發送
N:不發送
	* @return String
	*/
  public String getEmailNotice() {
    return this.emailNotice == null ? "" : this.emailNotice;
  }

/**
	* 電子郵件發送與否<br>
	* Y:發送
N:不發送
  *
  * @param emailNotice 電子郵件發送與否
	*/
  public void setEmailNotice(String emailNotice) {
    this.emailNotice = emailNotice;
  }

/**
	* 申請日期<br>
	* 
	* @return Integer
	*/
  public int getApplyDate() {
    return StaticTool.bcToRoc(this.applyDate);
  }

/**
	* 申請日期<br>
	* 
  *
  * @param applyDate 申請日期
  * @throws LogicException when Date Is Warn	*/
  public void setApplyDate(int applyDate) throws LogicException {
    this.applyDate = StaticTool.rocToBc(applyDate);
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
    return "CustNotice [custNoticeId=" + custNoticeId + ", paperNotice=" + paperNotice + ", msgNotice=" + msgNotice + ", emailNotice=" + emailNotice
           + ", applyDate=" + applyDate + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
