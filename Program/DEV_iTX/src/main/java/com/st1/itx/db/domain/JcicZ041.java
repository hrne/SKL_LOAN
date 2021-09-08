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
 * JcicZ041 協商開始暨停催通知資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicZ041`")
public class JcicZ041 implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -8079237959210825437L;

@EmbeddedId
  private JcicZ041Id jcicZ041Id;

  // 交易代碼
  /* A:新增,C:異動 */
  @Column(name = "`TranKey`", length = 1)
  private String tranKey;

  // 報送單位代號
  /* 三位文數字 */
  @Column(name = "`SubmitKey`", length = 3, insertable = false, updatable = false)
  private String submitKey;

  // 債務人IDN
  /* 身份證字號 */
  @Column(name = "`CustId`", length = 10, insertable = false, updatable = false)
  private String custId;

  // 協商申請日
  /* 西元年月 */
  @Column(name = "`RcDate`", insertable = false, updatable = false)
  private int rcDate = 0;

  // 停催日期
  /* 西元年月 */
  @Column(name = "`ScDate`")
  private int scDate = 0;

  // 協商開始日
  /* 西元年月 */
  @Column(name = "`NegoStartDate`")
  private int negoStartDate = 0;

  // 非金融機構債權金額
  /* 單位新台幣元,右靠左補零。指債務人於申請前置協商時檢附之民間債權人清冊中,各項非金融機構債權之總金額,以債務人自行填寫之金額為主。 */
  @Column(name = "`NonFinClaimAmt`")
  private int nonFinClaimAmt = 0;

  // 轉出JCIC文字檔日期
  @Column(name = "`OutJcicTxtDate`")
  private int outJcicTxtDate = 0;

  // 流水號
  @Column(name = "`Ukey`", length = 32)
  private String ukey;

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


  public JcicZ041Id getJcicZ041Id() {
    return this.jcicZ041Id;
  }

  public void setJcicZ041Id(JcicZ041Id jcicZ041Id) {
    this.jcicZ041Id = jcicZ041Id;
  }

/**
	* 交易代碼<br>
	* A:新增,C:異動
	* @return String
	*/
  public String getTranKey() {
    return this.tranKey == null ? "" : this.tranKey;
  }

/**
	* 交易代碼<br>
	* A:新增,C:異動
  *
  * @param tranKey 交易代碼
	*/
  public void setTranKey(String tranKey) {
    this.tranKey = tranKey;
  }

/**
	* 報送單位代號<br>
	* 三位文數字
	* @return String
	*/
  public String getSubmitKey() {
    return this.submitKey == null ? "" : this.submitKey;
  }

/**
	* 報送單位代號<br>
	* 三位文數字
  *
  * @param submitKey 報送單位代號
	*/
  public void setSubmitKey(String submitKey) {
    this.submitKey = submitKey;
  }

/**
	* 債務人IDN<br>
	* 身份證字號
	* @return String
	*/
  public String getCustId() {
    return this.custId == null ? "" : this.custId;
  }

/**
	* 債務人IDN<br>
	* 身份證字號
  *
  * @param custId 債務人IDN
	*/
  public void setCustId(String custId) {
    this.custId = custId;
  }

/**
	* 協商申請日<br>
	* 西元年月
	* @return Integer
	*/
  public int getRcDate() {
    return StaticTool.bcToRoc(this.rcDate);
  }

/**
	* 協商申請日<br>
	* 西元年月
  *
  * @param rcDate 協商申請日
  * @throws LogicException when Date Is Warn	*/
  public void setRcDate(int rcDate) throws LogicException {
    this.rcDate = StaticTool.rocToBc(rcDate);
  }

/**
	* 停催日期<br>
	* 西元年月
	* @return Integer
	*/
  public int getScDate() {
    return StaticTool.bcToRoc(this.scDate);
  }

/**
	* 停催日期<br>
	* 西元年月
  *
  * @param scDate 停催日期
  * @throws LogicException when Date Is Warn	*/
  public void setScDate(int scDate) throws LogicException {
    this.scDate = StaticTool.rocToBc(scDate);
  }

/**
	* 協商開始日<br>
	* 西元年月
	* @return Integer
	*/
  public int getNegoStartDate() {
    return StaticTool.bcToRoc(this.negoStartDate);
  }

/**
	* 協商開始日<br>
	* 西元年月
  *
  * @param negoStartDate 協商開始日
  * @throws LogicException when Date Is Warn	*/
  public void setNegoStartDate(int negoStartDate) throws LogicException {
    this.negoStartDate = StaticTool.rocToBc(negoStartDate);
  }

/**
	* 非金融機構債權金額<br>
	* 單位新台幣元,右靠左補零。指債務人於申請前置協商時檢附之民間債權人清冊中,各項非金融機構債權之總金額,以債務人自行填寫之金額為主。
	* @return Integer
	*/
  public int getNonFinClaimAmt() {
    return this.nonFinClaimAmt;
  }

/**
	* 非金融機構債權金額<br>
	* 單位新台幣元,右靠左補零。指債務人於申請前置協商時檢附之民間債權人清冊中,各項非金融機構債權之總金額,以債務人自行填寫之金額為主。
  *
  * @param nonFinClaimAmt 非金融機構債權金額
	*/
  public void setNonFinClaimAmt(int nonFinClaimAmt) {
    this.nonFinClaimAmt = nonFinClaimAmt;
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
    return "JcicZ041 [jcicZ041Id=" + jcicZ041Id + ", tranKey=" + tranKey + ", scDate=" + scDate + ", negoStartDate=" + negoStartDate
           + ", nonFinClaimAmt=" + nonFinClaimAmt + ", outJcicTxtDate=" + outJcicTxtDate + ", ukey=" + ukey + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
           + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
