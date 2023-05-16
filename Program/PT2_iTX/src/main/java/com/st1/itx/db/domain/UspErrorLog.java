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
 * UspErrorLog 預存程序錯誤記錄檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`UspErrorLog`")
public class UspErrorLog implements Serializable {


  // 記錄識別碼
  @Id
  @Column(name = "`LogUkey`", length = 32)
  private String logUkey = " ";

  // 記錄日期
  @Column(name = "`LogDate`")
  private int logDate = 0;

  // 記錄時間
  @Column(name = "`LogTime`")
  private int logTime = 0;

  // 預存程序名稱
  @Column(name = "`UspName`", length = 100)
  private String uspName;

  // 錯誤代碼
  /* SQLCODE */
  @Column(name = "`ErrorCode`")
  private BigDecimal errorCode = new BigDecimal("0");

  // 錯誤訊息
  /* SQLERRM */
  @Column(name = "`ErrorMessage`", length = 1500)
  private String errorMessage;

  // ErrorBackTrace
  /* dbms_utility.format_error_backtrace */
  @Column(name = "`ErrorBackTrace`", length = 1500)
  private String errorBackTrace;

  // 啟動人員員編
  /* 預存程序啟動人員員編註：999999為系統批次 */
  @Column(name = "`ExecEmpNo`", length = 6)
  private String execEmpNo;

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
	* 記錄識別碼<br>
	* 
	* @return String
	*/
  public String getLogUkey() {
    return this.logUkey == null ? "" : this.logUkey;
  }

/**
	* 記錄識別碼<br>
	* 
  *
  * @param logUkey 記錄識別碼
	*/
  public void setLogUkey(String logUkey) {
    this.logUkey = logUkey;
  }

/**
	* 記錄日期<br>
	* 
	* @return Integer
	*/
  public int getLogDate() {
    return this.logDate;
  }

/**
	* 記錄日期<br>
	* 
  *
  * @param logDate 記錄日期
	*/
  public void setLogDate(int logDate) {
    this.logDate = logDate;
  }

/**
	* 記錄時間<br>
	* 
	* @return Integer
	*/
  public int getLogTime() {
    return this.logTime;
  }

/**
	* 記錄時間<br>
	* 
  *
  * @param logTime 記錄時間
	*/
  public void setLogTime(int logTime) {
    this.logTime = logTime;
  }

/**
	* 預存程序名稱<br>
	* 
	* @return String
	*/
  public String getUspName() {
    return this.uspName == null ? "" : this.uspName;
  }

/**
	* 預存程序名稱<br>
	* 
  *
  * @param uspName 預存程序名稱
	*/
  public void setUspName(String uspName) {
    this.uspName = uspName;
  }

/**
	* 錯誤代碼<br>
	* SQLCODE
	* @return BigDecimal
	*/
  public BigDecimal getErrorCode() {
    return this.errorCode;
  }

/**
	* 錯誤代碼<br>
	* SQLCODE
  *
  * @param errorCode 錯誤代碼
	*/
  public void setErrorCode(BigDecimal errorCode) {
    this.errorCode = errorCode;
  }

/**
	* 錯誤訊息<br>
	* SQLERRM
	* @return String
	*/
  public String getErrorMessage() {
    return this.errorMessage == null ? "" : this.errorMessage;
  }

/**
	* 錯誤訊息<br>
	* SQLERRM
  *
  * @param errorMessage 錯誤訊息
	*/
  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

/**
	* ErrorBackTrace<br>
	* dbms_utility.format_error_backtrace
	* @return String
	*/
  public String getErrorBackTrace() {
    return this.errorBackTrace == null ? "" : this.errorBackTrace;
  }

/**
	* ErrorBackTrace<br>
	* dbms_utility.format_error_backtrace
  *
  * @param errorBackTrace ErrorBackTrace
	*/
  public void setErrorBackTrace(String errorBackTrace) {
    this.errorBackTrace = errorBackTrace;
  }

/**
	* 啟動人員員編<br>
	* 預存程序啟動人員員編
註：999999為系統批次
	* @return String
	*/
  public String getExecEmpNo() {
    return this.execEmpNo == null ? "" : this.execEmpNo;
  }

/**
	* 啟動人員員編<br>
	* 預存程序啟動人員員編
註：999999為系統批次
  *
  * @param execEmpNo 啟動人員員編
	*/
  public void setExecEmpNo(String execEmpNo) {
    this.execEmpNo = execEmpNo;
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
    return "UspErrorLog [logUkey=" + logUkey + ", logDate=" + logDate + ", logTime=" + logTime + ", uspName=" + uspName + ", errorCode=" + errorCode + ", errorMessage=" + errorMessage
           + ", errorBackTrace=" + errorBackTrace + ", execEmpNo=" + execEmpNo + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo
           + "]";
  }
}
