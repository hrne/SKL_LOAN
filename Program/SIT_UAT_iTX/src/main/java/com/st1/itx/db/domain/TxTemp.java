package com.st1.itx.db.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.EmbeddedId;
import javax.persistence.Column;

/**
 * TxTemp 交易暫存<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`TxTemp`")
public class TxTemp implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -1558919059752531718L;

@EmbeddedId
  private TxTempId txTempId;

  // 交易日
  @Column(name = "`Entdy`", insertable = false, updatable = false)
  private int entdy = 0;

  // 分行別
  @Column(name = "`Kinbr`", length = 4, insertable = false, updatable = false)
  private String kinbr;

  // 交易員代號
  @Column(name = "`TlrNo`", length = 6, insertable = false, updatable = false)
  private String tlrNo;

  // 交易序號
  @Column(name = "`TxtNo`", length = 8, insertable = false, updatable = false)
  private String txtNo;

  // 序號
  @Column(name = "`SeqNo`", length = 30, insertable = false, updatable = false)
  private String seqNo;

  // TEXT
  @Column(name = "`Text`", length = 3600)
  private String text;

  // 異動日期
  @Column(name = "`TxDate`")
  private int txDate = 0;

  // 異動時間
  @Column(name = "`TxTime`")
  private int txTime = 0;

  // 建立者櫃員編號
  @Column(name = "`CreateEmpNo`", length = 6)
  private String createEmpNo;

  // 建立日期時間
  @CreatedDate
  @Column(name = "`CreateDate`")
  private java.sql.Timestamp createDate;

  // 修改者櫃員編號
  @Column(name = "`LastUpdateEmpNo`", length = 6)
  private String lastUpdateEmpNo;

  // 修改日期時間
  @LastModifiedDate
  @Column(name = "`LastUpdate`")
  private java.sql.Timestamp lastUpdate;


  public TxTempId getTxTempId() {
    return this.txTempId;
  }

  public void setTxTempId(TxTempId txTempId) {
    this.txTempId = txTempId;
  }

/**
	* 交易日<br>
	* 
	* @return Integer
	*/
  public int getEntdy() {
    return this.entdy;
  }

/**
	* 交易日<br>
	* 
  *
  * @param entdy 交易日
	*/
  public void setEntdy(int entdy) {
    this.entdy = entdy;
  }

/**
	* 分行別<br>
	* 
	* @return String
	*/
  public String getKinbr() {
    return this.kinbr == null ? "" : this.kinbr;
  }

/**
	* 分行別<br>
	* 
  *
  * @param kinbr 分行別
	*/
  public void setKinbr(String kinbr) {
    this.kinbr = kinbr;
  }

/**
	* 交易員代號<br>
	* 
	* @return String
	*/
  public String getTlrNo() {
    return this.tlrNo == null ? "" : this.tlrNo;
  }

/**
	* 交易員代號<br>
	* 
  *
  * @param tlrNo 交易員代號
	*/
  public void setTlrNo(String tlrNo) {
    this.tlrNo = tlrNo;
  }

/**
	* 交易序號<br>
	* 
	* @return String
	*/
  public String getTxtNo() {
    return this.txtNo == null ? "" : this.txtNo;
  }

/**
	* 交易序號<br>
	* 
  *
  * @param txtNo 交易序號
	*/
  public void setTxtNo(String txtNo) {
    this.txtNo = txtNo;
  }

/**
	* 序號<br>
	* 
	* @return String
	*/
  public String getSeqNo() {
    return this.seqNo == null ? "" : this.seqNo;
  }

/**
	* 序號<br>
	* 
  *
  * @param seqNo 序號
	*/
  public void setSeqNo(String seqNo) {
    this.seqNo = seqNo;
  }

/**
	* TEXT<br>
	* 
	* @return String
	*/
  public String getText() {
    return this.text == null ? "" : this.text;
  }

/**
	* TEXT<br>
	* 
  *
  * @param text TEXT
	*/
  public void setText(String text) {
    this.text = text;
  }

/**
	* 異動日期<br>
	* 
	* @return Integer
	*/
  public int getTxDate() {
    return this.txDate;
  }

/**
	* 異動日期<br>
	* 
  *
  * @param txDate 異動日期
	*/
  public void setTxDate(int txDate) {
    this.txDate = txDate;
  }

/**
	* 異動時間<br>
	* 
	* @return Integer
	*/
  public int getTxTime() {
    return this.txTime;
  }

/**
	* 異動時間<br>
	* 
  *
  * @param txTime 異動時間
	*/
  public void setTxTime(int txTime) {
    this.txTime = txTime;
  }

/**
	* 建立者櫃員編號<br>
	* 
	* @return String
	*/
  public String getCreateEmpNo() {
    return this.createEmpNo == null ? "" : this.createEmpNo;
  }

/**
	* 建立者櫃員編號<br>
	* 
  *
  * @param createEmpNo 建立者櫃員編號
	*/
  public void setCreateEmpNo(String createEmpNo) {
    this.createEmpNo = createEmpNo;
  }

/**
	* 建立日期時間<br>
	* 
	* @return java.sql.Timestamp
	*/
  public java.sql.Timestamp getCreateDate() {
    return this.createDate;
  }

/**
	* 建立日期時間<br>
	* 
  *
  * @param createDate 建立日期時間
	*/
  public void setCreateDate(java.sql.Timestamp createDate) {
    this.createDate = createDate;
  }

/**
	* 修改者櫃員編號<br>
	* 
	* @return String
	*/
  public String getLastUpdateEmpNo() {
    return this.lastUpdateEmpNo == null ? "" : this.lastUpdateEmpNo;
  }

/**
	* 修改者櫃員編號<br>
	* 
  *
  * @param lastUpdateEmpNo 修改者櫃員編號
	*/
  public void setLastUpdateEmpNo(String lastUpdateEmpNo) {
    this.lastUpdateEmpNo = lastUpdateEmpNo;
  }

/**
	* 修改日期時間<br>
	* 
	* @return java.sql.Timestamp
	*/
  public java.sql.Timestamp getLastUpdate() {
    return this.lastUpdate;
  }

/**
	* 修改日期時間<br>
	* 
  *
  * @param lastUpdate 修改日期時間
	*/
  public void setLastUpdate(java.sql.Timestamp lastUpdate) {
    this.lastUpdate = lastUpdate;
  }


  @Override
  public String toString() {
    return "TxTemp [txTempId=" + txTempId + ", text=" + text
           + ", txDate=" + txDate + ", txTime=" + txTime + ", createEmpNo=" + createEmpNo + ", createDate=" + createDate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + ", lastUpdate=" + lastUpdate
           + "]";
  }
}
