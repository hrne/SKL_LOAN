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
 * TxAuthorize 主管授權紀錄<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`TxAuthorize`")
public class TxAuthorize implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

// 序號
  @Id
  @Column(name = "`AutoSeq`")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "`TxAuthorize_SEQ`")
  @SequenceGenerator(name = "`TxAuthorize_SEQ`", sequenceName = "`TxAuthorize_SEQ`", allocationSize = 1)
  private Long autoSeq = 0L;

  // 授權主管編號
  @Column(name = "`SupNo`", length = 6)
  private String supNo;

  // 交易人員
  @Column(name = "`TlrNo`", length = 6)
  private String tlrNo;

  // 交易理由
  @Column(name = "`TradeReason`", length = 100)
  private String tradeReason;

  // 授權編號和理由
  @Column(name = "`ReasonFAJson`", length = 1200)
  private String reasonFAJson;

  // 會計日
  @Column(name = "`Entdy`")
  private int entdy = 0;

  // 交易代號
  @Column(name = "`Txcd`", length = 10)
  private String txcd;

  // 交易序號
  @Column(name = "`TxSeq`", length = 18)
  private String txSeq;

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
	* 授權主管編號<br>
	* 
	* @return String
	*/
  public String getSupNo() {
    return this.supNo == null ? "" : this.supNo;
  }

/**
	* 授權主管編號<br>
	* 
  *
  * @param supNo 授權主管編號
	*/
  public void setSupNo(String supNo) {
    this.supNo = supNo;
  }

/**
	* 交易人員<br>
	* 
	* @return String
	*/
  public String getTlrNo() {
    return this.tlrNo == null ? "" : this.tlrNo;
  }

/**
	* 交易人員<br>
	* 
  *
  * @param tlrNo 交易人員
	*/
  public void setTlrNo(String tlrNo) {
    this.tlrNo = tlrNo;
  }

/**
	* 交易理由<br>
	* 
	* @return String
	*/
  public String getTradeReason() {
    return this.tradeReason == null ? "" : this.tradeReason;
  }

/**
	* 交易理由<br>
	* 
  *
  * @param tradeReason 交易理由
	*/
  public void setTradeReason(String tradeReason) {
    this.tradeReason = tradeReason;
  }

/**
	* 授權編號和理由<br>
	* 
	* @return String
	*/
  public String getReasonFAJson() {
    return this.reasonFAJson == null ? "" : this.reasonFAJson;
  }

/**
	* 授權編號和理由<br>
	* 
  *
  * @param reasonFAJson 授權編號和理由
	*/
  public void setReasonFAJson(String reasonFAJson) {
    this.reasonFAJson = reasonFAJson;
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
	* 交易代號<br>
	* 
	* @return String
	*/
  public String getTxcd() {
    return this.txcd == null ? "" : this.txcd;
  }

/**
	* 交易代號<br>
	* 
  *
  * @param txcd 交易代號
	*/
  public void setTxcd(String txcd) {
    this.txcd = txcd;
  }

/**
	* 交易序號<br>
	* 
	* @return String
	*/
  public String getTxSeq() {
    return this.txSeq == null ? "" : this.txSeq;
  }

/**
	* 交易序號<br>
	* 
  *
  * @param txSeq 交易序號
	*/
  public void setTxSeq(String txSeq) {
    this.txSeq = txSeq;
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
    return "TxAuthorize [autoSeq=" + autoSeq + ", supNo=" + supNo + ", tlrNo=" + tlrNo + ", tradeReason=" + tradeReason + ", reasonFAJson=" + reasonFAJson + ", entdy=" + entdy
           + ", txcd=" + txcd + ", txSeq=" + txSeq + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo
           + "]";
  }
}
