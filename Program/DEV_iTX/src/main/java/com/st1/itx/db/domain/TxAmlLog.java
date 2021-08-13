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
 * TxAmlLog AML檢查紀錄檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`TxAmlLog`")
public class TxAmlLog implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -7690484809220661704L;

// 序號
  @Id
  @Column(name = "`LogNo`")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "`TxAmlLog_SEQ`")
  @SequenceGenerator(name = "`TxAmlLog_SEQ`", sequenceName = "`TxAmlLog_SEQ`", allocationSize = 1)
  private Long logNo = 0L;

  // 帳務日
  @Column(name = "`Entdy`")
  private int entdy = 0;

  // 單位
  @Column(name = "`BrNo`", length = 4)
  private String brNo;

  // 相關編號
  @Column(name = "`RefNo`", length = 40)
  private String refNo;

  // 案號
  /* 可識別為同一批交易的號碼 */
  @Column(name = "`CaseNo`", length = 40)
  private String caseNo;

  // 放款案號
  @Column(name = "`AcctNo`", length = 30)
  private String acctNo;

  // AML 交易序號
  /* 線別(30)+唯一Key */
  @Column(name = "`TransactionId`", length = 100)
  private String transactionId;

  // 檢查XML
  @Column(name = "`MsgRg`", length = 3000)
  private String msgRg;

  // 回覆XML
  @Column(name = "`MsgRs`", length = 3000)
  private String msgRs;

  // 回覆狀態
  @Column(name = "`Status`", length = 10)
  private String status;

  // 回覆代碼
  @Column(name = "`StatusCode`", length = 10)
  private String statusCode;

  // 回覆敍述
  @Column(name = "`StatusDesc`", length = 100)
  private String statusDesc;

  // 是否有相似名單
  @Column(name = "`IsSimilar`", length = 1)
  private String isSimilar;

  // 疑似黑名單分類
  @Column(name = "`IsSan`", length = 1)
  private String isSan;

  // 是否為禁制國家
  @Column(name = "`IsBanNation`", length = 1)
  private String isBanNation;

  // 檢核狀態
  /* 0.非可疑名單/已完成名單確認1.需審查/確認2.為凍結名單/未確定名單 */
  @Column(name = "`ConfirmStatus`", length = 1)
  private String confirmStatus;

  // 人工檢核狀態
  /* 1.確認正常2.確認可疑3.確認未確定 */
  @Column(name = "`ConfirmCode`", length = 1)
  private String confirmCode;

  // 最後人工檢核人員
  @Column(name = "`ConfirmEmpNo`", length = 6)
  private String confirmEmpNo;

  // 人工檢核後續處理交易代號
  @Column(name = "`ConfirmTranCode`", length = 5)
  private String confirmTranCode;

  // 身份證字號/統一編號
  @Column(name = "`CustId`", length = 10)
  private String custId;

  // 戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

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
	* 帳務日<br>
	* 
	* @return Integer
	*/
  public int getEntdy() {
    return StaticTool.bcToRoc(this.entdy);
  }

/**
	* 帳務日<br>
	* 
  *
  * @param entdy 帳務日
  * @throws LogicException when Date Is Warn	*/
  public void setEntdy(int entdy) throws LogicException {
    this.entdy = StaticTool.rocToBc(entdy);
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
	* 相關編號<br>
	* 
	* @return String
	*/
  public String getRefNo() {
    return this.refNo == null ? "" : this.refNo;
  }

/**
	* 相關編號<br>
	* 
  *
  * @param refNo 相關編號
	*/
  public void setRefNo(String refNo) {
    this.refNo = refNo;
  }

/**
	* 案號<br>
	* 可識別為同一批交易的號碼
	* @return String
	*/
  public String getCaseNo() {
    return this.caseNo == null ? "" : this.caseNo;
  }

/**
	* 案號<br>
	* 可識別為同一批交易的號碼
  *
  * @param caseNo 案號
	*/
  public void setCaseNo(String caseNo) {
    this.caseNo = caseNo;
  }

/**
	* 放款案號<br>
	* 
	* @return String
	*/
  public String getAcctNo() {
    return this.acctNo == null ? "" : this.acctNo;
  }

/**
	* 放款案號<br>
	* 
  *
  * @param acctNo 放款案號
	*/
  public void setAcctNo(String acctNo) {
    this.acctNo = acctNo;
  }

/**
	* AML 交易序號<br>
	* 線別(30)+唯一Key
	* @return String
	*/
  public String getTransactionId() {
    return this.transactionId == null ? "" : this.transactionId;
  }

/**
	* AML 交易序號<br>
	* 線別(30)+唯一Key
  *
  * @param transactionId AML 交易序號
	*/
  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

/**
	* 檢查XML<br>
	* 
	* @return String
	*/
  public String getMsgRg() {
    return this.msgRg == null ? "" : this.msgRg;
  }

/**
	* 檢查XML<br>
	* 
  *
  * @param msgRg 檢查XML
	*/
  public void setMsgRg(String msgRg) {
    this.msgRg = msgRg;
  }

/**
	* 回覆XML<br>
	* 
	* @return String
	*/
  public String getMsgRs() {
    return this.msgRs == null ? "" : this.msgRs;
  }

/**
	* 回覆XML<br>
	* 
  *
  * @param msgRs 回覆XML
	*/
  public void setMsgRs(String msgRs) {
    this.msgRs = msgRs;
  }

/**
	* 回覆狀態<br>
	* 
	* @return String
	*/
  public String getStatus() {
    return this.status == null ? "" : this.status;
  }

/**
	* 回覆狀態<br>
	* 
  *
  * @param status 回覆狀態
	*/
  public void setStatus(String status) {
    this.status = status;
  }

/**
	* 回覆代碼<br>
	* 
	* @return String
	*/
  public String getStatusCode() {
    return this.statusCode == null ? "" : this.statusCode;
  }

/**
	* 回覆代碼<br>
	* 
  *
  * @param statusCode 回覆代碼
	*/
  public void setStatusCode(String statusCode) {
    this.statusCode = statusCode;
  }

/**
	* 回覆敍述<br>
	* 
	* @return String
	*/
  public String getStatusDesc() {
    return this.statusDesc == null ? "" : this.statusDesc;
  }

/**
	* 回覆敍述<br>
	* 
  *
  * @param statusDesc 回覆敍述
	*/
  public void setStatusDesc(String statusDesc) {
    this.statusDesc = statusDesc;
  }

/**
	* 是否有相似名單<br>
	* 
	* @return String
	*/
  public String getIsSimilar() {
    return this.isSimilar == null ? "" : this.isSimilar;
  }

/**
	* 是否有相似名單<br>
	* 
  *
  * @param isSimilar 是否有相似名單
	*/
  public void setIsSimilar(String isSimilar) {
    this.isSimilar = isSimilar;
  }

/**
	* 疑似黑名單分類<br>
	* 
	* @return String
	*/
  public String getIsSan() {
    return this.isSan == null ? "" : this.isSan;
  }

/**
	* 疑似黑名單分類<br>
	* 
  *
  * @param isSan 疑似黑名單分類
	*/
  public void setIsSan(String isSan) {
    this.isSan = isSan;
  }

/**
	* 是否為禁制國家<br>
	* 
	* @return String
	*/
  public String getIsBanNation() {
    return this.isBanNation == null ? "" : this.isBanNation;
  }

/**
	* 是否為禁制國家<br>
	* 
  *
  * @param isBanNation 是否為禁制國家
	*/
  public void setIsBanNation(String isBanNation) {
    this.isBanNation = isBanNation;
  }

/**
	* 檢核狀態<br>
	* 0.非可疑名單/已完成名單確認
1.需審查/確認
2.為凍結名單/未確定名單
	* @return String
	*/
  public String getConfirmStatus() {
    return this.confirmStatus == null ? "" : this.confirmStatus;
  }

/**
	* 檢核狀態<br>
	* 0.非可疑名單/已完成名單確認
1.需審查/確認
2.為凍結名單/未確定名單
  *
  * @param confirmStatus 檢核狀態
	*/
  public void setConfirmStatus(String confirmStatus) {
    this.confirmStatus = confirmStatus;
  }

/**
	* 人工檢核狀態<br>
	* 1.確認正常
2.確認可疑
3.確認未確定
	* @return String
	*/
  public String getConfirmCode() {
    return this.confirmCode == null ? "" : this.confirmCode;
  }

/**
	* 人工檢核狀態<br>
	* 1.確認正常
2.確認可疑
3.確認未確定
  *
  * @param confirmCode 人工檢核狀態
	*/
  public void setConfirmCode(String confirmCode) {
    this.confirmCode = confirmCode;
  }

/**
	* 最後人工檢核人員<br>
	* 
	* @return String
	*/
  public String getConfirmEmpNo() {
    return this.confirmEmpNo == null ? "" : this.confirmEmpNo;
  }

/**
	* 最後人工檢核人員<br>
	* 
  *
  * @param confirmEmpNo 最後人工檢核人員
	*/
  public void setConfirmEmpNo(String confirmEmpNo) {
    this.confirmEmpNo = confirmEmpNo;
  }

/**
	* 人工檢核後續處理交易代號<br>
	* 
	* @return String
	*/
  public String getConfirmTranCode() {
    return this.confirmTranCode == null ? "" : this.confirmTranCode;
  }

/**
	* 人工檢核後續處理交易代號<br>
	* 
  *
  * @param confirmTranCode 人工檢核後續處理交易代號
	*/
  public void setConfirmTranCode(String confirmTranCode) {
    this.confirmTranCode = confirmTranCode;
  }

/**
	* 身份證字號/統一編號<br>
	* 
	* @return String
	*/
  public String getCustId() {
    return this.custId == null ? "" : this.custId;
  }

/**
	* 身份證字號/統一編號<br>
	* 
  *
  * @param custId 身份證字號/統一編號
	*/
  public void setCustId(String custId) {
    this.custId = custId;
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
    return "TxAmlLog [logNo=" + logNo + ", entdy=" + entdy + ", brNo=" + brNo + ", refNo=" + refNo + ", caseNo=" + caseNo + ", acctNo=" + acctNo
           + ", transactionId=" + transactionId + ", msgRg=" + msgRg + ", msgRs=" + msgRs + ", status=" + status + ", statusCode=" + statusCode + ", statusDesc=" + statusDesc
           + ", isSimilar=" + isSimilar + ", isSan=" + isSan + ", isBanNation=" + isBanNation + ", confirmStatus=" + confirmStatus + ", confirmCode=" + confirmCode + ", confirmEmpNo=" + confirmEmpNo
           + ", confirmTranCode=" + confirmTranCode + ", custId=" + custId + ", custNo=" + custNo + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
           + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
