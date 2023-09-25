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
 * TxPrinter 印表機設定檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`TxPrinter`")
public class TxPrinter implements Serializable {


  @EmbeddedId
  private TxPrinterId txPrinterId;

  // 工作站IP
  @Column(name = "`StanIp`", length = 15, insertable = false, updatable = false)
  private String stanIp;

  // 檔案編號
  @Column(name = "`FileCode`", length = 40, insertable = false, updatable = false)
  private String fileCode;

  // 印表機伺服器IP
  @Column(name = "`ServerIp`", length = 15)
  private String serverIp;

  // 預設印表機
  @Column(name = "`Printer`", length = 100)
  private String printer;

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

  // 產表環境
  /* O:Online環境D:Day環境M:Month環境H:History環境 */
  @Column(name = "`SourceEnv`", length = 1)
  private String sourceEnv;


  public TxPrinterId getTxPrinterId() {
    return this.txPrinterId;
  }

  public void setTxPrinterId(TxPrinterId txPrinterId) {
    this.txPrinterId = txPrinterId;
  }

/**
	* 工作站IP<br>
	* 
	* @return String
	*/
  public String getStanIp() {
    return this.stanIp == null ? "" : this.stanIp;
  }

/**
	* 工作站IP<br>
	* 
  *
  * @param stanIp 工作站IP
	*/
  public void setStanIp(String stanIp) {
    this.stanIp = stanIp;
  }

/**
	* 檔案編號<br>
	* 
	* @return String
	*/
  public String getFileCode() {
    return this.fileCode == null ? "" : this.fileCode;
  }

/**
	* 檔案編號<br>
	* 
  *
  * @param fileCode 檔案編號
	*/
  public void setFileCode(String fileCode) {
    this.fileCode = fileCode;
  }

/**
	* 印表機伺服器IP<br>
	* 
	* @return String
	*/
  public String getServerIp() {
    return this.serverIp == null ? "" : this.serverIp;
  }

/**
	* 印表機伺服器IP<br>
	* 
  *
  * @param serverIp 印表機伺服器IP
	*/
  public void setServerIp(String serverIp) {
    this.serverIp = serverIp;
  }

/**
	* 預設印表機<br>
	* 
	* @return String
	*/
  public String getPrinter() {
    return this.printer == null ? "" : this.printer;
  }

/**
	* 預設印表機<br>
	* 
  *
  * @param printer 預設印表機
	*/
  public void setPrinter(String printer) {
    this.printer = printer;
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

/**
	* 產表環境<br>
	* O:Online環境
D:Day環境
M:Month環境
H:History環境
	* @return String
	*/
  public String getSourceEnv() {
    return this.sourceEnv == null ? "" : this.sourceEnv;
  }

/**
	* 產表環境<br>
	* O:Online環境
D:Day環境
M:Month環境
H:History環境
  *
  * @param sourceEnv 產表環境
	*/
  public void setSourceEnv(String sourceEnv) {
    this.sourceEnv = sourceEnv;
  }


  @Override
  public String toString() {
    return "TxPrinter [txPrinterId=" + txPrinterId + ", serverIp=" + serverIp + ", printer=" + printer + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
           + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + ", sourceEnv=" + sourceEnv + "]";
  }
}
