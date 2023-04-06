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
import javax.persistence.SequenceGenerator;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * TxApLog ApLog敏感資料查詢紀錄檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`TxApLog`")
public class TxApLog implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 1113703034969933125L;

// 序號
  @Id
  @Column(name = "`AutoSeq`")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "`TxApLog_SEQ`")
  @SequenceGenerator(name = "`TxApLog_SEQ`", sequenceName = "`TxApLog_SEQ`", allocationSize = 1)
  private Long autoSeq = 0L;

  // 使用者員編
  @Column(name = "`UserID`", length = 8)
  private String userID;

  // 使用者身份證字號
  @Column(name = "`IDNumber`", length = 12)
  private String iDNumber;

  // 使用者姓名
  @Column(name = "`IDName`", length = 24)
  private String iDName;

  // Action事件
  @Column(name = "`ActionEvent`")
  private int actionEvent = 0;

  // 使用者IP
  @Column(name = "`UserIP`", length = 50)
  private String userIP;

  // 系統名稱
  @Column(name = "`SystemName`", length = 20)
  private String systemName;

  // 作業名稱
  @Column(name = "`OperationName`", length = 20)
  private String operationName;

  // 程式名稱
  @Column(name = "`ProgramName`", length = 50)
  private String programName;

  // 方法名稱
  @Column(name = "`MethodName`", length = 50)
  private String methodName;

  // 伺服器名稱
  @Column(name = "`ServerName`", length = 60)
  private String serverName;

  // 伺服器IP
  @Column(name = "`ServerIP`", length = 50)
  private String serverIP;

  // 輸入的參數
  @Column(name = "`InputDataforXMLorJson`", length = 3000)
  private String inputDataforXMLorJson;

  // 輸出的結果
  @Column(name = "`OutputDataforXMLorJson`", length = 3000)
  private String outputDataforXMLorJson;

  // 事件執行結果
  @Column(name = "`EnforcementResult`")
  private int enforcementResult = 0;

  // 備註
  @Column(name = "`Message`", length = 200)
  private String message;

  // 交易日期
  @Column(name = "`Entdy`")
  private int entdy = 0;

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
	* 序號<br>
	* 
	* @return Long
	*/
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long getAutoSeq() {
    return this.autoSeq;
  }

/**
	* 序號<br>
	* 
  *
  * @param autoSeq 序號
	*/
  public void setAutoSeq(Long autoSeq) {
    this.autoSeq = autoSeq;
  }

/**
	* 使用者員編<br>
	* 
	* @return String
	*/
  public String getUserID() {
    return this.userID == null ? "" : this.userID;
  }

/**
	* 使用者員編<br>
	* 
  *
  * @param userID 使用者員編
	*/
  public void setUserID(String userID) {
    this.userID = userID;
  }

/**
	* 使用者身份證字號<br>
	* 
	* @return String
	*/
  public String getIDNumber() {
    return this.iDNumber == null ? "" : this.iDNumber;
  }

/**
	* 使用者身份證字號<br>
	* 
  *
  * @param iDNumber 使用者身份證字號
	*/
  public void setIDNumber(String iDNumber) {
    this.iDNumber = iDNumber;
  }

/**
	* 使用者姓名<br>
	* 
	* @return String
	*/
  public String getIDName() {
    return this.iDName == null ? "" : this.iDName;
  }

/**
	* 使用者姓名<br>
	* 
  *
  * @param iDName 使用者姓名
	*/
  public void setIDName(String iDName) {
    this.iDName = iDName;
  }

/**
	* Action事件<br>
	* 
	* @return Integer
	*/
  public int getActionEvent() {
    return this.actionEvent;
  }

/**
	* Action事件<br>
	* 
  *
  * @param actionEvent Action事件
	*/
  public void setActionEvent(int actionEvent) {
    this.actionEvent = actionEvent;
  }

/**
	* 使用者IP<br>
	* 
	* @return String
	*/
  public String getUserIP() {
    return this.userIP == null ? "" : this.userIP;
  }

/**
	* 使用者IP<br>
	* 
  *
  * @param userIP 使用者IP
	*/
  public void setUserIP(String userIP) {
    this.userIP = userIP;
  }

/**
	* 系統名稱<br>
	* 
	* @return String
	*/
  public String getSystemName() {
    return this.systemName == null ? "" : this.systemName;
  }

/**
	* 系統名稱<br>
	* 
  *
  * @param systemName 系統名稱
	*/
  public void setSystemName(String systemName) {
    this.systemName = systemName;
  }

/**
	* 作業名稱<br>
	* 
	* @return String
	*/
  public String getOperationName() {
    return this.operationName == null ? "" : this.operationName;
  }

/**
	* 作業名稱<br>
	* 
  *
  * @param operationName 作業名稱
	*/
  public void setOperationName(String operationName) {
    this.operationName = operationName;
  }

/**
	* 程式名稱<br>
	* 
	* @return String
	*/
  public String getProgramName() {
    return this.programName == null ? "" : this.programName;
  }

/**
	* 程式名稱<br>
	* 
  *
  * @param programName 程式名稱
	*/
  public void setProgramName(String programName) {
    this.programName = programName;
  }

/**
	* 方法名稱<br>
	* 
	* @return String
	*/
  public String getMethodName() {
    return this.methodName == null ? "" : this.methodName;
  }

/**
	* 方法名稱<br>
	* 
  *
  * @param methodName 方法名稱
	*/
  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }

/**
	* 伺服器名稱<br>
	* 
	* @return String
	*/
  public String getServerName() {
    return this.serverName == null ? "" : this.serverName;
  }

/**
	* 伺服器名稱<br>
	* 
  *
  * @param serverName 伺服器名稱
	*/
  public void setServerName(String serverName) {
    this.serverName = serverName;
  }

/**
	* 伺服器IP<br>
	* 
	* @return String
	*/
  public String getServerIP() {
    return this.serverIP == null ? "" : this.serverIP;
  }

/**
	* 伺服器IP<br>
	* 
  *
  * @param serverIP 伺服器IP
	*/
  public void setServerIP(String serverIP) {
    this.serverIP = serverIP;
  }

/**
	* 輸入的參數<br>
	* 
	* @return String
	*/
  public String getInputDataforXMLorJson() {
    return this.inputDataforXMLorJson == null ? "" : this.inputDataforXMLorJson;
  }

/**
	* 輸入的參數<br>
	* 
  *
  * @param inputDataforXMLorJson 輸入的參數
	*/
  public void setInputDataforXMLorJson(String inputDataforXMLorJson) {
    this.inputDataforXMLorJson = inputDataforXMLorJson;
  }

/**
	* 輸出的結果<br>
	* 
	* @return String
	*/
  public String getOutputDataforXMLorJson() {
    return this.outputDataforXMLorJson == null ? "" : this.outputDataforXMLorJson;
  }

/**
	* 輸出的結果<br>
	* 
  *
  * @param outputDataforXMLorJson 輸出的結果
	*/
  public void setOutputDataforXMLorJson(String outputDataforXMLorJson) {
    this.outputDataforXMLorJson = outputDataforXMLorJson;
  }

/**
	* 事件執行結果<br>
	* 
	* @return Integer
	*/
  public int getEnforcementResult() {
    return this.enforcementResult;
  }

/**
	* 事件執行結果<br>
	* 
  *
  * @param enforcementResult 事件執行結果
	*/
  public void setEnforcementResult(int enforcementResult) {
    this.enforcementResult = enforcementResult;
  }

/**
	* 備註<br>
	* 
	* @return String
	*/
  public String getMessage() {
    return this.message == null ? "" : this.message;
  }

/**
	* 備註<br>
	* 
  *
  * @param message 備註
	*/
  public void setMessage(String message) {
    this.message = message;
  }

/**
	* 交易日期<br>
	* 
	* @return Integer
	*/
  public int getEntdy() {
    return StaticTool.bcToRoc(this.entdy);
  }

/**
	* 交易日期<br>
	* 
  *
  * @param entdy 交易日期
  * @throws LogicException when Date Is Warn	*/
  public void setEntdy(int entdy) throws LogicException {
    this.entdy = StaticTool.rocToBc(entdy);
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
    return "TxApLog [autoSeq=" + autoSeq + ", userID=" + userID + ", iDNumber=" + iDNumber + ", iDName=" + iDName + ", actionEvent=" + actionEvent + ", userIP=" + userIP
           + ", systemName=" + systemName + ", operationName=" + operationName + ", programName=" + programName + ", methodName=" + methodName + ", serverName=" + serverName + ", serverIP=" + serverIP
           + ", inputDataforXMLorJson=" + inputDataforXMLorJson + ", outputDataforXMLorJson=" + outputDataforXMLorJson + ", enforcementResult=" + enforcementResult + ", message=" + message + ", entdy=" + entdy + ", createDate=" + createDate
           + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
