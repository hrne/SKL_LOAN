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
 * JcicZ051Log 延期繳款（喘息期）資料檔案<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicZ051Log`")
public class JcicZ051Log implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 455040971145575781L;

@EmbeddedId
  private JcicZ051LogId jcicZ051LogId;

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

  // 延期繳款原因
  /* A:本人罹患重大傷病B:家屬罹患重大傷病C:非自願性失業D:繳稅E:繳付子女學費F:放無薪假或減薪G:莫拉克颱風受災戶H:本人為低收入戶I:本人為中度以上身心障礙者J:本人為重大災害災民K:0206 震災受災戶L:受嚴重特殊傳染性肺炎疫情影響繳款 */
  @Column(name = "`DelayCode`", length = 1)
  private String delayCode;

  // 延期繳款案情說明
  /* 20中文字 */
  @Column(name = "`DelayDesc`", length = 20)
  private String delayDesc;

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


  public JcicZ051LogId getJcicZ051LogId() {
    return this.jcicZ051LogId;
  }

  public void setJcicZ051LogId(JcicZ051LogId jcicZ051LogId) {
    this.jcicZ051LogId = jcicZ051LogId;
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
	* 延期繳款原因<br>
	* A:本人罹患重大傷病
B:家屬罹患重大傷病
C:非自願性失業
D:繳稅
E:繳付子女學費
F:放無薪假或減薪
G:莫拉克颱風受災戶
H:本人為低收入戶
I:本人為中度以上身心障礙者
J:本人為重大災害災民
K:0206 震災受災戶
L:受嚴重特殊傳染性肺炎疫情影響繳款
	* @return String
	*/
  public String getDelayCode() {
    return this.delayCode == null ? "" : this.delayCode;
  }

/**
	* 延期繳款原因<br>
	* A:本人罹患重大傷病
B:家屬罹患重大傷病
C:非自願性失業
D:繳稅
E:繳付子女學費
F:放無薪假或減薪
G:莫拉克颱風受災戶
H:本人為低收入戶
I:本人為中度以上身心障礙者
J:本人為重大災害災民
K:0206 震災受災戶
L:受嚴重特殊傳染性肺炎疫情影響繳款
  *
  * @param delayCode 延期繳款原因
	*/
  public void setDelayCode(String delayCode) {
    this.delayCode = delayCode;
  }

/**
	* 延期繳款案情說明<br>
	* 20中文字
	* @return String
	*/
  public String getDelayDesc() {
    return this.delayDesc == null ? "" : this.delayDesc;
  }

/**
	* 延期繳款案情說明<br>
	* 20中文字
  *
  * @param delayDesc 延期繳款案情說明
	*/
  public void setDelayDesc(String delayDesc) {
    this.delayDesc = delayDesc;
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
    return "JcicZ051Log [jcicZ051LogId=" + jcicZ051LogId + ", tranKey=" + tranKey + ", delayCode=" + delayCode + ", delayDesc=" + delayDesc + ", outJcicTxtDate=" + outJcicTxtDate
           + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
