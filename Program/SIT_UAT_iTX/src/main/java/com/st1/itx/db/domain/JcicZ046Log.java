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
 * JcicZ046Log 結案通知資料檔案格式<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicZ046Log`")
public class JcicZ046Log implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -7700127012386303671L;

@EmbeddedId
  private JcicZ046LogId jcicZ046LogId;

  // 流水號
  @Column(name = "`Ukey`", length = 32, insertable = false, updatable = false)
  private String ukey;

  // 交易序號
  @Column(name = "`TxSeq`", length = 18, insertable = false, updatable = false)
  private String txSeq;

  // 交易代碼
  /* A:新增C:異動D:刪除 */
  @Column(name = "`TranKey`", length = 1)
  private String tranKey;

  // 毀諾原因代號
  /* 01:債務人失業02:債務人收入減少03:債務人支出增加04:債務人往生05:債務人入獄06:債務人欲聲請前置調解/更生/清算07:債務人失聯或聯絡困難 */
  @Column(name = "`BreakCode`", length = 2)
  private String breakCode;

  // 結案日期
  @Column(name = "`CloseDate`")
  private int closeDate = 0;

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


  public JcicZ046LogId getJcicZ046LogId() {
    return this.jcicZ046LogId;
  }

  public void setJcicZ046LogId(JcicZ046LogId jcicZ046LogId) {
    this.jcicZ046LogId = jcicZ046LogId;
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
	* A:新增C:異動D:刪除
	* @return String
	*/
  public String getTranKey() {
    return this.tranKey == null ? "" : this.tranKey;
  }

/**
	* 交易代碼<br>
	* A:新增C:異動D:刪除
  *
  * @param tranKey 交易代碼
	*/
  public void setTranKey(String tranKey) {
    this.tranKey = tranKey;
  }

/**
	* 毀諾原因代號<br>
	* 01:債務人失業
02:債務人收入減少
03:債務人支出增加
04:債務人往生
05:債務人入獄
06:債務人欲聲請前置調解/更生/清算
07:債務人失聯或聯絡困難
	* @return String
	*/
  public String getBreakCode() {
    return this.breakCode == null ? "" : this.breakCode;
  }

/**
	* 毀諾原因代號<br>
	* 01:債務人失業
02:債務人收入減少
03:債務人支出增加
04:債務人往生
05:債務人入獄
06:債務人欲聲請前置調解/更生/清算
07:債務人失聯或聯絡困難
  *
  * @param breakCode 毀諾原因代號
	*/
  public void setBreakCode(String breakCode) {
    this.breakCode = breakCode;
  }

/**
	* 結案日期<br>
	* 
	* @return Integer
	*/
  public int getCloseDate() {
    return StaticTool.bcToRoc(this.closeDate);
  }

/**
	* 結案日期<br>
	* 
  *
  * @param closeDate 結案日期
  * @throws LogicException when Date Is Warn	*/
  public void setCloseDate(int closeDate) throws LogicException {
    this.closeDate = StaticTool.rocToBc(closeDate);
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
    return "JcicZ046Log [jcicZ046LogId=" + jcicZ046LogId + ", tranKey=" + tranKey + ", breakCode=" + breakCode + ", closeDate=" + closeDate + ", outJcicTxtDate=" + outJcicTxtDate
           + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
