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
import javax.persistence.Id;
import javax.persistence.Column;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

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

  // 提供書面寄送
  /* Y:是 , N:否 */
  @Column(name = "`LetterFg`", length = 1)
  private String letterFg;

  // 提供簡訊寄送
  /* Y:是 , N:否 */
  @Column(name = "`MessageFg`", length = 1)
  private String messageFg;

  // 提供電子郵件寄送
  /* Y:是 , N:否 */
  @Column(name = "`EmailFg`", length = 1)
  private String emailFg;

  // 書面寄送順序
  /* 0:無 , 優先序1/2/3 ; 優先序同為1時則同時寄送 */
  @Column(name = "`Letter`")
  private int letter = 0;

  // 簡訊寄送順序
  /* 0:無 , 優先序1/2/3 ; 優先序同為1時則同時寄送 */
  @Column(name = "`Message`")
  private int message = 0;

  // 電子郵件寄送順序
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

  // 浮水印記號
  /* 0:不需浮水印 1:需浮水印 */
  @Column(name = "`WatermarkFlag`")
  private int watermarkFlag = 0;

  // 啟用記號
  /* Y:啟用 , N:停用 */
  @Column(name = "`Enable`", length = 1)
  private String enable;

  // 機密等級
  /* 0:普通1:密2:機密3:極機密 */
  @Column(name = "`Confidentiality`", length = 1)
  private String confidentiality;

  // 敏感性資料記錄記號
  /* 0-否1-是 */
  @Column(name = "`ApLogFlag`")
  private int apLogFlag = 0;

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
	* 0:不送 
1:依利率調整通知方式
2:依設定優先序
	* @return Integer
	*/
  public int getSendCode() {
    return this.sendCode;
  }

/**
	* 寄送記號<br>
	* 0:不送 
1:依利率調整通知方式
2:依設定優先序
  *
  * @param sendCode 寄送記號
	*/
  public void setSendCode(int sendCode) {
    this.sendCode = sendCode;
  }

/**
	* 提供書面寄送<br>
	* Y:是 , N:否
	* @return String
	*/
  public String getLetterFg() {
    return this.letterFg == null ? "" : this.letterFg;
  }

/**
	* 提供書面寄送<br>
	* Y:是 , N:否
  *
  * @param letterFg 提供書面寄送
	*/
  public void setLetterFg(String letterFg) {
    this.letterFg = letterFg;
  }

/**
	* 提供簡訊寄送<br>
	* Y:是 , N:否
	* @return String
	*/
  public String getMessageFg() {
    return this.messageFg == null ? "" : this.messageFg;
  }

/**
	* 提供簡訊寄送<br>
	* Y:是 , N:否
  *
  * @param messageFg 提供簡訊寄送
	*/
  public void setMessageFg(String messageFg) {
    this.messageFg = messageFg;
  }

/**
	* 提供電子郵件寄送<br>
	* Y:是 , N:否
	* @return String
	*/
  public String getEmailFg() {
    return this.emailFg == null ? "" : this.emailFg;
  }

/**
	* 提供電子郵件寄送<br>
	* Y:是 , N:否
  *
  * @param emailFg 提供電子郵件寄送
	*/
  public void setEmailFg(String emailFg) {
    this.emailFg = emailFg;
  }

/**
	* 書面寄送順序<br>
	* 0:無 , 優先序1/2/3 ; 優先序同為1時則同時寄送
	* @return Integer
	*/
  public int getLetter() {
    return this.letter;
  }

/**
	* 書面寄送順序<br>
	* 0:無 , 優先序1/2/3 ; 優先序同為1時則同時寄送
  *
  * @param letter 書面寄送順序
	*/
  public void setLetter(int letter) {
    this.letter = letter;
  }

/**
	* 簡訊寄送順序<br>
	* 0:無 , 優先序1/2/3 ; 優先序同為1時則同時寄送
	* @return Integer
	*/
  public int getMessage() {
    return this.message;
  }

/**
	* 簡訊寄送順序<br>
	* 0:無 , 優先序1/2/3 ; 優先序同為1時則同時寄送
  *
  * @param message 簡訊寄送順序
	*/
  public void setMessage(int message) {
    this.message = message;
  }

/**
	* 電子郵件寄送順序<br>
	* 0:無 , 優先序1/2/3 ; 優先序同為1時則同時寄送
	* @return Integer
	*/
  public int getEmail() {
    return this.email;
  }

/**
	* 電子郵件寄送順序<br>
	* 0:無 , 優先序1/2/3 ; 優先序同為1時則同時寄送
  *
  * @param email 電子郵件寄送順序
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
	* 浮水印記號<br>
	* 0:不需浮水印 1:需浮水印
	* @return Integer
	*/
  public int getWatermarkFlag() {
    return this.watermarkFlag;
  }

/**
	* 浮水印記號<br>
	* 0:不需浮水印 1:需浮水印
  *
  * @param watermarkFlag 浮水印記號
	*/
  public void setWatermarkFlag(int watermarkFlag) {
    this.watermarkFlag = watermarkFlag;
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
	* 機密等級<br>
	* 0:普通
1:密
2:機密
3:極機密
	* @return String
	*/
  public String getConfidentiality() {
    return this.confidentiality == null ? "" : this.confidentiality;
  }

/**
	* 機密等級<br>
	* 0:普通
1:密
2:機密
3:極機密
  *
  * @param confidentiality 機密等級
	*/
  public void setConfidentiality(String confidentiality) {
    this.confidentiality = confidentiality;
  }

/**
	* 敏感性資料記錄記號<br>
	* 0-否
1-是
	* @return Integer
	*/
  public int getApLogFlag() {
    return this.apLogFlag;
  }

/**
	* 敏感性資料記錄記號<br>
	* 0-否
1-是
  *
  * @param apLogFlag 敏感性資料記錄記號
	*/
  public void setApLogFlag(int apLogFlag) {
    this.apLogFlag = apLogFlag;
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
    return "CdReport [formNo=" + formNo + ", formName=" + formName + ", cycle=" + cycle + ", sendCode=" + sendCode + ", letterFg=" + letterFg + ", messageFg=" + messageFg
           + ", emailFg=" + emailFg + ", letter=" + letter + ", message=" + message + ", email=" + email + ", usageDesc=" + usageDesc + ", signCode=" + signCode
           + ", watermarkFlag=" + watermarkFlag + ", enable=" + enable + ", confidentiality=" + confidentiality + ", apLogFlag=" + apLogFlag + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
           + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
