package com.st1.itx.db.domain;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * NegFinShareLog 債務協商債權分攤檔歷程檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`NegFinShareLog`")
public class NegFinShareLog implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -7837307827422522229L;

@EmbeddedId
  private NegFinShareLogId negFinShareLogId;

  // 債務人戶號
  /* 保貸戶須建立客戶主檔 */
  @Column(name = "`CustNo`", insertable = false, updatable = false)
  private int custNo = 0;

  // 案件序號
  @Column(name = "`CaseSeq`", insertable = false, updatable = false)
  private int caseSeq = 0;

  // 歷程序號
  @Column(name = "`Seq`", insertable = false, updatable = false)
  private int seq = 0;

  // 債權機構
  @Column(name = "`FinCode`", length = 8, insertable = false, updatable = false)
  private String finCode;

  // 簽約金額
  @Column(name = "`ContractAmt`")
  private BigDecimal contractAmt = new BigDecimal("0");

  // 債權比例%
  @Column(name = "`AmtRatio`")
  private BigDecimal amtRatio = new BigDecimal("0");

  // 期款
  @Column(name = "`DueAmt`")
  private BigDecimal dueAmt = new BigDecimal("0");

  // 註銷日期
  @Column(name = "`CancelDate`")
  private int cancelDate = 0;

  // 註銷本金
  @Column(name = "`CancelAmt`")
  private BigDecimal cancelAmt = new BigDecimal("0");

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


  public NegFinShareLogId getNegFinShareLogId() {
    return this.negFinShareLogId;
  }

  public void setNegFinShareLogId(NegFinShareLogId negFinShareLogId) {
    this.negFinShareLogId = negFinShareLogId;
  }

/**
	* 債務人戶號<br>
	* 保貸戶須建立客戶主檔
	* @return Integer
	*/
  public int getCustNo() {
    return this.custNo;
  }

/**
	* 債務人戶號<br>
	* 保貸戶須建立客戶主檔
  *
  * @param custNo 債務人戶號
	*/
  public void setCustNo(int custNo) {
    this.custNo = custNo;
  }

/**
	* 案件序號<br>
	* 
	* @return Integer
	*/
  public int getCaseSeq() {
    return this.caseSeq;
  }

/**
	* 案件序號<br>
	* 
  *
  * @param caseSeq 案件序號
	*/
  public void setCaseSeq(int caseSeq) {
    this.caseSeq = caseSeq;
  }

/**
	* 歷程序號<br>
	* 
	* @return Integer
	*/
  public int getSeq() {
    return this.seq;
  }

/**
	* 歷程序號<br>
	* 
  *
  * @param seq 歷程序號
	*/
  public void setSeq(int seq) {
    this.seq = seq;
  }

/**
	* 債權機構<br>
	* 
	* @return String
	*/
  public String getFinCode() {
    return this.finCode == null ? "" : this.finCode;
  }

/**
	* 債權機構<br>
	* 
  *
  * @param finCode 債權機構
	*/
  public void setFinCode(String finCode) {
    this.finCode = finCode;
  }

/**
	* 簽約金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getContractAmt() {
    return this.contractAmt;
  }

/**
	* 簽約金額<br>
	* 
  *
  * @param contractAmt 簽約金額
	*/
  public void setContractAmt(BigDecimal contractAmt) {
    this.contractAmt = contractAmt;
  }

/**
	* 債權比例%<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getAmtRatio() {
    return this.amtRatio;
  }

/**
	* 債權比例%<br>
	* 
  *
  * @param amtRatio 債權比例%
	*/
  public void setAmtRatio(BigDecimal amtRatio) {
    this.amtRatio = amtRatio;
  }

/**
	* 期款<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getDueAmt() {
    return this.dueAmt;
  }

/**
	* 期款<br>
	* 
  *
  * @param dueAmt 期款
	*/
  public void setDueAmt(BigDecimal dueAmt) {
    this.dueAmt = dueAmt;
  }

/**
	* 註銷日期<br>
	* 
	* @return Integer
	*/
  public int getCancelDate() {
    return StaticTool.bcToRoc(this.cancelDate);
  }

/**
	* 註銷日期<br>
	* 
  *
  * @param cancelDate 註銷日期
  * @throws LogicException when Date Is Warn	*/
  public void setCancelDate(int cancelDate) throws LogicException {
    this.cancelDate = StaticTool.rocToBc(cancelDate);
  }

/**
	* 註銷本金<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getCancelAmt() {
    return this.cancelAmt;
  }

/**
	* 註銷本金<br>
	* 
  *
  * @param cancelAmt 註銷本金
	*/
  public void setCancelAmt(BigDecimal cancelAmt) {
    this.cancelAmt = cancelAmt;
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
    return "NegFinShareLog [negFinShareLogId=" + negFinShareLogId + ", contractAmt=" + contractAmt + ", amtRatio=" + amtRatio
           + ", dueAmt=" + dueAmt + ", cancelDate=" + cancelDate + ", cancelAmt=" + cancelAmt + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
           + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
