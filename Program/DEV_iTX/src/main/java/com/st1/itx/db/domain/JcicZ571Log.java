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
 * JcicZ571Log 更生款項統一收付回報債權資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicZ571Log`")
public class JcicZ571Log implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 7604069853758358460L;

@EmbeddedId
  private JcicZ571LogId jcicZ571LogId;

  // 流水號
  @Column(name = "`Ukey`", length = 32, insertable = false, updatable = false)
  private String ukey;

  // 交易序號
  @Column(name = "`TxSeq`", length = 18, insertable = false, updatable = false)
  private String txSeq;

  // 交易代碼
  /* A:新增;C:異動 */
  @Column(name = "`TranKey`", length = 1)
  private String tranKey;

  // 是否為更生債權人
  /* Y;NN:以下欄位皆為空白 */
  @Column(name = "`OwnerYn`", length = 1)
  private String ownerYn;

  // 債務人是否仍依更生方案正常還款予本金融機構
  /* Y;N若本次[參與分配債權金額]為0者,本欄位談報為"Y" */
  @Column(name = "`PayYn`", length = 1)
  private String payYn;

  // 本金融機構更生債權總金額
  @Column(name = "`OwnerAmt`")
  private int ownerAmt = 0;

  // 參與分配債權金額
  @Column(name = "`AllotAmt`")
  private int allotAmt = 0;

  // 未參與分配債權金額
  @Column(name = "`UnallotAmt`")
  private int unallotAmt = 0;

  // 轉出JCIC文字檔日期
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


  public JcicZ571LogId getJcicZ571LogId() {
    return this.jcicZ571LogId;
  }

  public void setJcicZ571LogId(JcicZ571LogId jcicZ571LogId) {
    this.jcicZ571LogId = jcicZ571LogId;
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
	* A:新增;C:異動
	* @return String
	*/
  public String getTranKey() {
    return this.tranKey == null ? "" : this.tranKey;
  }

/**
	* 交易代碼<br>
	* A:新增;C:異動
  *
  * @param tranKey 交易代碼
	*/
  public void setTranKey(String tranKey) {
    this.tranKey = tranKey;
  }

/**
	* 是否為更生債權人<br>
	* Y;N
N:以下欄位皆為空白
	* @return String
	*/
  public String getOwnerYn() {
    return this.ownerYn == null ? "" : this.ownerYn;
  }

/**
	* 是否為更生債權人<br>
	* Y;N
N:以下欄位皆為空白
  *
  * @param ownerYn 是否為更生債權人
	*/
  public void setOwnerYn(String ownerYn) {
    this.ownerYn = ownerYn;
  }

/**
	* 債務人是否仍依更生方案正常還款予本金融機構<br>
	* Y;N
若本次[參與分配債權金額]為0者,本欄位談報為"Y"
	* @return String
	*/
  public String getPayYn() {
    return this.payYn == null ? "" : this.payYn;
  }

/**
	* 債務人是否仍依更生方案正常還款予本金融機構<br>
	* Y;N
若本次[參與分配債權金額]為0者,本欄位談報為"Y"
  *
  * @param payYn 債務人是否仍依更生方案正常還款予本金融機構
	*/
  public void setPayYn(String payYn) {
    this.payYn = payYn;
  }

/**
	* 本金融機構更生債權總金額<br>
	* 
	* @return Integer
	*/
  public int getOwnerAmt() {
    return this.ownerAmt;
  }

/**
	* 本金融機構更生債權總金額<br>
	* 
  *
  * @param ownerAmt 本金融機構更生債權總金額
	*/
  public void setOwnerAmt(int ownerAmt) {
    this.ownerAmt = ownerAmt;
  }

/**
	* 參與分配債權金額<br>
	* 
	* @return Integer
	*/
  public int getAllotAmt() {
    return this.allotAmt;
  }

/**
	* 參與分配債權金額<br>
	* 
  *
  * @param allotAmt 參與分配債權金額
	*/
  public void setAllotAmt(int allotAmt) {
    this.allotAmt = allotAmt;
  }

/**
	* 未參與分配債權金額<br>
	* 
	* @return Integer
	*/
  public int getUnallotAmt() {
    return this.unallotAmt;
  }

/**
	* 未參與分配債權金額<br>
	* 
  *
  * @param unallotAmt 未參與分配債權金額
	*/
  public void setUnallotAmt(int unallotAmt) {
    this.unallotAmt = unallotAmt;
  }

/**
	* 轉出JCIC文字檔日期<br>
	* 
	* @return Integer
	*/
  public int getOutJcicTxtDate() {
    return StaticTool.bcToRoc(this.outJcicTxtDate);
  }

/**
	* 轉出JCIC文字檔日期<br>
	* 
  *
  * @param outJcicTxtDate 轉出JCIC文字檔日期
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
    return "JcicZ571Log [jcicZ571LogId=" + jcicZ571LogId + ", tranKey=" + tranKey + ", ownerYn=" + ownerYn + ", payYn=" + payYn + ", ownerAmt=" + ownerAmt
           + ", allotAmt=" + allotAmt + ", unallotAmt=" + unallotAmt + ", outJcicTxtDate=" + outJcicTxtDate + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
           + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
