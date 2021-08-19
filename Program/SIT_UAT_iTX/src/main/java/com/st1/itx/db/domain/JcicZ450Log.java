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
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * JcicZ450Log 前置調解債務人繳款資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicZ450Log`")
public class JcicZ450Log implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 8818248142203853936L;

@EmbeddedId
  private JcicZ450LogId jcicZ450LogId;

  // 流水號
  @Column(name = "`Ukey`", length = 32, insertable = false, updatable = false)
  private String ukey;

  // 交易序號
  @Column(name = "`TxSeq`", length = 18, insertable = false, updatable = false)
  private String txSeq;

  // 交易代碼
  /* A:新增;C:異動;D:刪除 */
  @Column(name = "`TranKey`", length = 1)
  private String tranKey;

  // 本次繳款金額
  @Column(name = "`PayAmt`")
  private int payAmt = 0;

  // 累計實際還款金額
  @Column(name = "`SumRepayActualAmt`")
  private int sumRepayActualAmt = 0;

  // 截至目前累計應還款金額
  @Column(name = "`SumRepayShouldAmt`")
  private int sumRepayShouldAmt = 0;

  // 債權結案註記
  /* Y:債務全數清償N:債務尚未全數清償 */
  @Column(name = "`PayStatus`", length = 1)
  private String payStatus;

  // 轉JCIC文字檔日期
  @Column(name = "`OutJcicTxtDate`")
  private int outJcicTxtDate = 0;

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


  public JcicZ450LogId getJcicZ450LogId() {
    return this.jcicZ450LogId;
  }

  public void setJcicZ450LogId(JcicZ450LogId jcicZ450LogId) {
    this.jcicZ450LogId = jcicZ450LogId;
  }

/**
	* 流水號<br>
	* 
	* @return String
	*/
  public String getUkey() {
    return this.ukey == null ? "" : this.ukey;
  }

/**
	* 流水號<br>
	* 
  *
  * @param ukey 流水號
	*/
  public void setUkey(String ukey) {
    this.ukey = ukey;
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
	* 交易代碼<br>
	* A:新增;C:異動;D:刪除
	* @return String
	*/
  public String getTranKey() {
    return this.tranKey == null ? "" : this.tranKey;
  }

/**
	* 交易代碼<br>
	* A:新增;C:異動;D:刪除
  *
  * @param tranKey 交易代碼
	*/
  public void setTranKey(String tranKey) {
    this.tranKey = tranKey;
  }

/**
	* 本次繳款金額<br>
	* 
	* @return Integer
	*/
  public int getPayAmt() {
    return this.payAmt;
  }

/**
	* 本次繳款金額<br>
	* 
  *
  * @param payAmt 本次繳款金額
	*/
  public void setPayAmt(int payAmt) {
    this.payAmt = payAmt;
  }

/**
	* 累計實際還款金額<br>
	* 
	* @return Integer
	*/
  public int getSumRepayActualAmt() {
    return this.sumRepayActualAmt;
  }

/**
	* 累計實際還款金額<br>
	* 
  *
  * @param sumRepayActualAmt 累計實際還款金額
	*/
  public void setSumRepayActualAmt(int sumRepayActualAmt) {
    this.sumRepayActualAmt = sumRepayActualAmt;
  }

/**
	* 截至目前累計應還款金額<br>
	* 
	* @return Integer
	*/
  public int getSumRepayShouldAmt() {
    return this.sumRepayShouldAmt;
  }

/**
	* 截至目前累計應還款金額<br>
	* 
  *
  * @param sumRepayShouldAmt 截至目前累計應還款金額
	*/
  public void setSumRepayShouldAmt(int sumRepayShouldAmt) {
    this.sumRepayShouldAmt = sumRepayShouldAmt;
  }

/**
	* 債權結案註記<br>
	* Y:債務全數清償
N:債務尚未全數清償
	* @return String
	*/
  public String getPayStatus() {
    return this.payStatus == null ? "" : this.payStatus;
  }

/**
	* 債權結案註記<br>
	* Y:債務全數清償
N:債務尚未全數清償
  *
  * @param payStatus 債權結案註記
	*/
  public void setPayStatus(String payStatus) {
    this.payStatus = payStatus;
  }

/**
	* 轉JCIC文字檔日期<br>
	* 
	* @return Integer
	*/
  public int getOutJcicTxtDate() {
    return StaticTool.bcToRoc(this.outJcicTxtDate);
  }

/**
	* 轉JCIC文字檔日期<br>
	* 
  *
  * @param outJcicTxtDate 轉JCIC文字檔日期
  * @throws LogicException when Date Is Warn	*/
  public void setOutJcicTxtDate(int outJcicTxtDate) throws LogicException {
    this.outJcicTxtDate = StaticTool.rocToBc(outJcicTxtDate);
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
    return "JcicZ450Log [jcicZ450LogId=" + jcicZ450LogId + ", tranKey=" + tranKey + ", payAmt=" + payAmt + ", sumRepayActualAmt=" + sumRepayActualAmt + ", sumRepayShouldAmt=" + sumRepayShouldAmt
           + ", payStatus=" + payStatus + ", outJcicTxtDate=" + outJcicTxtDate + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo
           + "]";
  }
}
