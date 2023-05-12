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
import javax.persistence.SequenceGenerator;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * TxInquiry 查詢紀錄檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`TxInquiry`")
public class TxInquiry implements Serializable {


  // 序號
  @Id
  @Column(name = "`LogNo`")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "`TxInquiry_SEQ`")
  @SequenceGenerator(name = "`TxInquiry_SEQ`", sequenceName = "`TxInquiry_SEQ`", allocationSize = 1)
  private Long logNo = 0L;

  // 會計日
  @Column(name = "`Entdy`")
  private int entdy = 0;

  // 交易日期
  @Column(name = "`CalDate`")
  private int calDate = 0;

  // 單位
  @Column(name = "`BrNo`", length = 4)
  private String brNo;

  // 使用者編號
  @Column(name = "`TlrNo`", length = 6)
  private String tlrNo;

  // 主管編號
  @Column(name = "`SupNo`", length = 6)
  private String supNo;

  // 交易代號
  @Column(name = "`TranNo`", length = 5)
  private String tranNo;

  // 交易編號/帳號
  @Column(name = "`MrKey`", length = 20)
  private String mrKey;

  // 戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 交易結果
  /* S:成功 E:失敗 */
  @Column(name = "`TxResult`", length = 1)
  private String txResult;

  // 訊息代號
  @Column(name = "`MsgId`", length = 5)
  private String msgId;

  // 錯誤訊息
  @Column(name = "`ErrMsg`", length = 300)
  private String errMsg;

  // 交易完整電文
  @Column(name = "`TranData`", length = 3000)
  private String tranData;

  // 交易重要註記
  /* 1:查詢結清五年後客戶資料 */
  @Column(name = "`ImportFg`", length = 1)
  private String importFg;

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
  public Long getLogNo() {
    return this.logNo;
  }

/**
	* 序號<br>
	* 
  *
  * @param logNo 序號
	*/
  public void setLogNo(Long logNo) {
    this.logNo = logNo;
  }

/**
	* 會計日<br>
	* 
	* @return Integer
	*/
  public int getEntdy() {
    return StaticTool.bcToRoc(this.entdy);
  }

/**
	* 會計日<br>
	* 
  *
  * @param entdy 會計日
  * @throws LogicException when Date Is Warn	*/
  public void setEntdy(int entdy) throws LogicException {
    this.entdy = StaticTool.rocToBc(entdy);
  }

/**
	* 交易日期<br>
	* 
	* @return Integer
	*/
  public int getCalDate() {
    return StaticTool.bcToRoc(this.calDate);
  }

/**
	* 交易日期<br>
	* 
  *
  * @param calDate 交易日期
  * @throws LogicException when Date Is Warn	*/
  public void setCalDate(int calDate) throws LogicException {
    this.calDate = StaticTool.rocToBc(calDate);
  }

/**
	* 單位<br>
	* 
	* @return String
	*/
  public String getBrNo() {
    return this.brNo == null ? "" : this.brNo;
  }

/**
	* 單位<br>
	* 
  *
  * @param brNo 單位
	*/
  public void setBrNo(String brNo) {
    this.brNo = brNo;
  }

/**
	* 使用者編號<br>
	* 
	* @return String
	*/
  public String getTlrNo() {
    return this.tlrNo == null ? "" : this.tlrNo;
  }

/**
	* 使用者編號<br>
	* 
  *
  * @param tlrNo 使用者編號
	*/
  public void setTlrNo(String tlrNo) {
    this.tlrNo = tlrNo;
  }

/**
	* 主管編號<br>
	* 
	* @return String
	*/
  public String getSupNo() {
    return this.supNo == null ? "" : this.supNo;
  }

/**
	* 主管編號<br>
	* 
  *
  * @param supNo 主管編號
	*/
  public void setSupNo(String supNo) {
    this.supNo = supNo;
  }

/**
	* 交易代號<br>
	* 
	* @return String
	*/
  public String getTranNo() {
    return this.tranNo == null ? "" : this.tranNo;
  }

/**
	* 交易代號<br>
	* 
  *
  * @param tranNo 交易代號
	*/
  public void setTranNo(String tranNo) {
    this.tranNo = tranNo;
  }

/**
	* 交易編號/帳號<br>
	* 
	* @return String
	*/
  public String getMrKey() {
    return this.mrKey == null ? "" : this.mrKey;
  }

/**
	* 交易編號/帳號<br>
	* 
  *
  * @param mrKey 交易編號/帳號
	*/
  public void setMrKey(String mrKey) {
    this.mrKey = mrKey;
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
	* 交易結果<br>
	* S:成功 E:失敗
	* @return String
	*/
  public String getTxResult() {
    return this.txResult == null ? "" : this.txResult;
  }

/**
	* 交易結果<br>
	* S:成功 E:失敗
  *
  * @param txResult 交易結果
	*/
  public void setTxResult(String txResult) {
    this.txResult = txResult;
  }

/**
	* 訊息代號<br>
	* 
	* @return String
	*/
  public String getMsgId() {
    return this.msgId == null ? "" : this.msgId;
  }

/**
	* 訊息代號<br>
	* 
  *
  * @param msgId 訊息代號
	*/
  public void setMsgId(String msgId) {
    this.msgId = msgId;
  }

/**
	* 錯誤訊息<br>
	* 
	* @return String
	*/
  public String getErrMsg() {
    return this.errMsg == null ? "" : this.errMsg;
  }

/**
	* 錯誤訊息<br>
	* 
  *
  * @param errMsg 錯誤訊息
	*/
  public void setErrMsg(String errMsg) {
    this.errMsg = errMsg;
  }

/**
	* 交易完整電文<br>
	* 
	* @return String
	*/
  public String getTranData() {
    return this.tranData == null ? "" : this.tranData;
  }

/**
	* 交易完整電文<br>
	* 
  *
  * @param tranData 交易完整電文
	*/
  public void setTranData(String tranData) {
    this.tranData = tranData;
  }

/**
	* 交易重要註記<br>
	* 1:查詢結清五年後客戶資料
	* @return String
	*/
  public String getImportFg() {
    return this.importFg == null ? "" : this.importFg;
  }

/**
	* 交易重要註記<br>
	* 1:查詢結清五年後客戶資料
  *
  * @param importFg 交易重要註記
	*/
  public void setImportFg(String importFg) {
    this.importFg = importFg;
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
    return "TxInquiry [logNo=" + logNo + ", entdy=" + entdy + ", calDate=" + calDate + ", brNo=" + brNo + ", tlrNo=" + tlrNo + ", supNo=" + supNo
           + ", tranNo=" + tranNo + ", mrKey=" + mrKey + ", custNo=" + custNo + ", txResult=" + txResult + ", msgId=" + msgId + ", errMsg=" + errMsg
           + ", tranData=" + tranData + ", importFg=" + importFg + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo
           + "]";
  }
}
