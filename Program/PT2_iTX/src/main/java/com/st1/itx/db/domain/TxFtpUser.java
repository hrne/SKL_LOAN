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
 * TxFtpUser FTP權限檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`TxFtpUser`")
public class TxFtpUser implements Serializable {


  // 使用者ID
  @Id
  @Column(name = "`UserId`", length = 64)
  private String userId = " ";

  // 使用者密碼
  @Column(name = "`UserPassword`", length = 64)
  private String userPassword;

  // 根目錄
  @Column(name = "`HomeDirectory`", length = 128)
  private String homeDirectory;

  // 啟用記號
  @Column(name = "`EnableFlag`", length = 1)
  private String enableFlag;

  // 寫入權限
  @Column(name = "`WritePermission`", length = 1)
  private String writePermission;

  // Idel時間
  @Column(name = "`IdleTime`")
  private BigDecimal idleTime = new BigDecimal("0");

  // 上傳大小限制
  @Column(name = "`UploadRate`")
  private BigDecimal uploadRate = new BigDecimal("0");

  // 下載大小限制
  @Column(name = "`DownloadRate`")
  private BigDecimal downloadRate = new BigDecimal("0");

  // 同一使用者同時登入數量
  @Column(name = "`MaxloginNumber`")
  private BigDecimal maxloginNumber = new BigDecimal("0");

  // 不同IP同時登入限制
  @Column(name = "`MaxloginPerIp`")
  private BigDecimal maxloginPerIp = new BigDecimal("0");

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
	* 使用者ID<br>
	* 
	* @return String
	*/
  public String getUserId() {
    return this.userId == null ? "" : this.userId;
  }

/**
	* 使用者ID<br>
	* 
  *
  * @param userId 使用者ID
	*/
  public void setUserId(String userId) {
    this.userId = userId;
  }

/**
	* 使用者密碼<br>
	* 
	* @return String
	*/
  public String getUserPassword() {
    return this.userPassword == null ? "" : this.userPassword;
  }

/**
	* 使用者密碼<br>
	* 
  *
  * @param userPassword 使用者密碼
	*/
  public void setUserPassword(String userPassword) {
    this.userPassword = userPassword;
  }

/**
	* 根目錄<br>
	* 
	* @return String
	*/
  public String getHomeDirectory() {
    return this.homeDirectory == null ? "" : this.homeDirectory;
  }

/**
	* 根目錄<br>
	* 
  *
  * @param homeDirectory 根目錄
	*/
  public void setHomeDirectory(String homeDirectory) {
    this.homeDirectory = homeDirectory;
  }

/**
	* 啟用記號<br>
	* 
	* @return String
	*/
  public String getEnableFlag() {
    return this.enableFlag == null ? "" : this.enableFlag;
  }

/**
	* 啟用記號<br>
	* 
  *
  * @param enableFlag 啟用記號
	*/
  public void setEnableFlag(String enableFlag) {
    this.enableFlag = enableFlag;
  }

/**
	* 寫入權限<br>
	* 
	* @return String
	*/
  public String getWritePermission() {
    return this.writePermission == null ? "" : this.writePermission;
  }

/**
	* 寫入權限<br>
	* 
  *
  * @param writePermission 寫入權限
	*/
  public void setWritePermission(String writePermission) {
    this.writePermission = writePermission;
  }

/**
	* Idel時間<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getIdleTime() {
    return this.idleTime;
  }

/**
	* Idel時間<br>
	* 
  *
  * @param idleTime Idel時間
	*/
  public void setIdleTime(BigDecimal idleTime) {
    this.idleTime = idleTime;
  }

/**
	* 上傳大小限制<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getUploadRate() {
    return this.uploadRate;
  }

/**
	* 上傳大小限制<br>
	* 
  *
  * @param uploadRate 上傳大小限制
	*/
  public void setUploadRate(BigDecimal uploadRate) {
    this.uploadRate = uploadRate;
  }

/**
	* 下載大小限制<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getDownloadRate() {
    return this.downloadRate;
  }

/**
	* 下載大小限制<br>
	* 
  *
  * @param downloadRate 下載大小限制
	*/
  public void setDownloadRate(BigDecimal downloadRate) {
    this.downloadRate = downloadRate;
  }

/**
	* 同一使用者同時登入數量<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getMaxloginNumber() {
    return this.maxloginNumber;
  }

/**
	* 同一使用者同時登入數量<br>
	* 
  *
  * @param maxloginNumber 同一使用者同時登入數量
	*/
  public void setMaxloginNumber(BigDecimal maxloginNumber) {
    this.maxloginNumber = maxloginNumber;
  }

/**
	* 不同IP同時登入限制<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getMaxloginPerIp() {
    return this.maxloginPerIp;
  }

/**
	* 不同IP同時登入限制<br>
	* 
  *
  * @param maxloginPerIp 不同IP同時登入限制
	*/
  public void setMaxloginPerIp(BigDecimal maxloginPerIp) {
    this.maxloginPerIp = maxloginPerIp;
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
    return "TxFtpUser [userId=" + userId + ", userPassword=" + userPassword + ", homeDirectory=" + homeDirectory + ", enableFlag=" + enableFlag + ", writePermission=" + writePermission + ", idleTime=" + idleTime
           + ", uploadRate=" + uploadRate + ", downloadRate=" + downloadRate + ", maxloginNumber=" + maxloginNumber + ", maxloginPerIp=" + maxloginPerIp + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
           + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
