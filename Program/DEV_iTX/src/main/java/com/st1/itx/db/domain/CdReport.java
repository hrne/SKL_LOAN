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
 * CdReport 報表代號對照檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CdReport`")
public class CdReport implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -8183848508379151877L;

// 報表代號
  @Id
  @Column(name = "`FormNo`", length = 10)
  private String formNo = " ";

  // 報表名稱
  @Column(name = "`FormName`", length = 40)
  private String formName;

  // 報表週期
  /* 01.日報02.月報03.週報04.季報05.半年報06.年報07.隨機 */
  @Column(name = "`Cycle`")
  private int cycle = 0;

  // 寄送記號
  /* 0:不送 1:依利率調整通知方式2:依設定優先序 */
  @Column(name = "`SendCode`")
  private int sendCode = 0;

  // 書面寄送
  /* 0:無 , 優先序1/2/3 ; 優先序同為1時則同時寄送 */
  @Column(name = "`Letter`")
  private int letter = 0;

  // 簡訊寄送
  /* 0:無 , 優先序1/2/3 ; 優先序同為1時則同時寄送 */
  @Column(name = "`Message`")
  private int message = 0;

  // 電子郵件寄送
  /* 0:無 , 優先序1/2/3 ; 優先序同為1時則同時寄送 */
  @Column(name = "`Email`")
  private int email = 0;

  // 用途說明
  @Column(name = "`UsageDesc`", length = 40)
  private String usageDesc;

  // 簽核記號
  /* 0:不需簽核  1:需簽核 */
  @Column(name = "`SignCode`")
  private int signCode = 0;

  // 啟用記號
  /* Y:啟用 , N:停用 */
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
	* 報表名稱<br>
	* 
	* @return String
	*/
  public String getFormName() {
    return this.formName == null ? "" : this.formName;
  }

/**
	* 報表名稱<br>
	* 
  *
  * @param formName 報表名稱
	*/
  public void setFormName(String formName) {
    this.formName = formName;
  }

/**
	* 報表週期<br>
	* 01.日報
02.月報
03.週報
04.季報
05.半年報
06.年報
07.隨機
	* @return Integer
	*/
  public int getCycle() {
    return this.cycle;
  }

/**
	* 報表週期<br>
	* 01.日報
02.月報
03.週報
04.季報
05.半年報
06.年報
07.隨機
  *
  * @param cycle 報表週期
	*/
  public void setCycle(int cycle) {
    this.cycle = cycle;
  }

/**
	* 寄送記號<br>
	* 0:不送 1:依利率調整通知方式
2:依設定優先序
	* @return Integer
	*/
  public int getSendCode() {
    return this.sendCode;
  }

/**
	* 寄送記號<br>
	* 0:不送 1:依利率調整通知方式
2:依設定優先序
  *
  * @param sendCode 寄送記號
	*/
  public void setSendCode(int sendCode) {
    this.sendCode = sendCode;
  }

/**
	* 書面寄送<br>
	* 0:無 , 優先序1/2/3 ; 優先序同為1時則同時寄送
	* @return Integer
	*/
  public int getLetter() {
    return this.letter;
  }

/**
	* 書面寄送<br>
	* 0:無 , 優先序1/2/3 ; 優先序同為1時則同時寄送
  *
  * @param letter 書面寄送
	*/
  public void setLetter(int letter) {
    this.letter = letter;
  }

/**
	* 簡訊寄送<br>
	* 0:無 , 優先序1/2/3 ; 優先序同為1時則同時寄送
	* @return Integer
	*/
  public int getMessage() {
    return this.message;
  }

/**
	* 簡訊寄送<br>
	* 0:無 , 優先序1/2/3 ; 優先序同為1時則同時寄送
  *
  * @param message 簡訊寄送
	*/
  public void setMessage(int message) {
    this.message = message;
  }

/**
	* 電子郵件寄送<br>
	* 0:無 , 優先序1/2/3 ; 優先序同為1時則同時寄送
	* @return Integer
	*/
  public int getEmail() {
    return this.email;
  }

/**
	* 電子郵件寄送<br>
	* 0:無 , 優先序1/2/3 ; 優先序同為1時則同時寄送
  *
  * @param email 電子郵件寄送
	*/
  public void setEmail(int email) {
    this.email = email;
  }

/**
	* 用途說明<br>
	* 
	* @return String
	*/
  public String getUsageDesc() {
    return this.usageDesc == null ? "" : this.usageDesc;
  }

/**
	* 用途說明<br>
	* 
  *
  * @param usageDesc 用途說明
	*/
  public void setUsageDesc(String usageDesc) {
    this.usageDesc = usageDesc;
  }

/**
	* 簽核記號<br>
	* 0:不需簽核  1:需簽核
	* @return Integer
	*/
  public int getSignCode() {
    return this.signCode;
  }

/**
	* 簽核記號<br>
	* 0:不需簽核  1:需簽核
  *
  * @param signCode 簽核記號
	*/
  public void setSignCode(int signCode) {
    this.signCode = signCode;
  }

/**
	* 啟用記號<br>
	* Y:啟用 , N:停用
	* @return String
	*/
  public String getEnable() {
    return this.enable == null ? "" : this.enable;
  }

/**
	* 啟用記號<br>
	* Y:啟用 , N:停用
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
    return "CdReport [formNo=" + formNo + ", formName=" + formName + ", cycle=" + cycle + ", sendCode=" + sendCode + ", letter=" + letter + ", message=" + message
           + ", email=" + email + ", usageDesc=" + usageDesc + ", signCode=" + signCode + ", enable=" + enable + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
           + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
