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
import javax.persistence.EmbeddedId;
import javax.persistence.Column;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * JcicZ052 前置協商相關資料報送例外處理<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicZ052`")
public class JcicZ052 implements Serializable {


  @EmbeddedId
  private JcicZ052Id jcicZ052Id;

  // 交易代碼
  /* A:新增C:異動 */
  @Column(name = "`TranKey`", length = 1)
  private String tranKey;

  // 報送單位代號
  /* 三位文數字 */
  @Column(name = "`SubmitKey`", length = 3, insertable = false, updatable = false)
  private String submitKey;

  // 債務人IDN
  @Column(name = "`CustId`", length = 10, insertable = false, updatable = false)
  private String custId;

  // 協商申請日
  /* YYYYMMDD */
  @Column(name = "`RcDate`", insertable = false, updatable = false)
  private int rcDate = 0;

  // 補報送債權機構代號1
  /* 三位文數字 */
  @Column(name = "`BankCode1`", length = 3)
  private String bankCode1;

  // 補報送檔案格式資料別1
  /* 42或43或61，二位文數字 */
  @Column(name = "`DataCode1`", length = 2)
  private String dataCode1;

  // 補報送債權機構代號2
  /* 三位文數字 */
  @Column(name = "`BankCode2`", length = 3)
  private String bankCode2;

  // 補報送檔案格式資料別2
  /* 42或43或61，二位文數字 */
  @Column(name = "`DataCode2`", length = 2)
  private String dataCode2;

  // 補報送債權機構代號3
  /* 三位文數字 */
  @Column(name = "`BankCode3`", length = 3)
  private String bankCode3;

  // 補報送檔案格式資料別3
  /* 42或43或61，二位文數字 */
  @Column(name = "`DataCode3`", length = 2)
  private String dataCode3;

  // 補報送債權機構代號4
  /* 三位文數字 */
  @Column(name = "`BankCode4`", length = 3)
  private String bankCode4;

  // 補報送檔案格式資料別4
  /* 42或43或61，二位文數字 */
  @Column(name = "`DataCode4`", length = 2)
  private String dataCode4;

  // 補報送債權機構代號5
  /* 三位文數字 */
  @Column(name = "`BankCode5`", length = 3)
  private String bankCode5;

  // 補報送檔案格式資料別5
  /* 42或43或61，二位文數字 */
  @Column(name = "`DataCode5`", length = 2)
  private String dataCode5;

  // 申請變更還款條件日
  /* 為債務人備齊申請文件向最大債權金融機構申請變更還款日期 */
  @Column(name = "`ChangePayDate`")
  private int changePayDate = 0;

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

  // 實際報送日期
  @Column(name = "`ActualFilingDate`")
  private int actualFilingDate = 0;

  // 實際報送記號
  @Column(name = "`ActualFilingMark`", length = 3)
  private String actualFilingMark;


  public JcicZ052Id getJcicZ052Id() {
    return this.jcicZ052Id;
  }

  public void setJcicZ052Id(JcicZ052Id jcicZ052Id) {
    this.jcicZ052Id = jcicZ052Id;
  }

/**
	* 交易代碼<br>
	* A:新增
C:異動
	* @return String
	*/
  public String getTranKey() {
    return this.tranKey == null ? "" : this.tranKey;
  }

/**
	* 交易代碼<br>
	* A:新增
C:異動
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
	* 
	* @return String
	*/
  public String getCustId() {
    return this.custId == null ? "" : this.custId;
  }

/**
	* 債務人IDN<br>
	* 
  *
  * @param custId 債務人IDN
	*/
  public void setCustId(String custId) {
    this.custId = custId;
  }

/**
	* 協商申請日<br>
	* YYYYMMDD
	* @return Integer
	*/
  public int getRcDate() {
    return StaticTool.bcToRoc(this.rcDate);
  }

/**
	* 協商申請日<br>
	* YYYYMMDD
  *
  * @param rcDate 協商申請日
  * @throws LogicException when Date Is Warn	*/
  public void setRcDate(int rcDate) throws LogicException {
    this.rcDate = StaticTool.rocToBc(rcDate);
  }

/**
	* 補報送債權機構代號1<br>
	* 三位文數字
	* @return String
	*/
  public String getBankCode1() {
    return this.bankCode1 == null ? "" : this.bankCode1;
  }

/**
	* 補報送債權機構代號1<br>
	* 三位文數字
  *
  * @param bankCode1 補報送債權機構代號1
	*/
  public void setBankCode1(String bankCode1) {
    this.bankCode1 = bankCode1;
  }

/**
	* 補報送檔案格式資料別1<br>
	* 42或43或61，二位文數字
	* @return String
	*/
  public String getDataCode1() {
    return this.dataCode1 == null ? "" : this.dataCode1;
  }

/**
	* 補報送檔案格式資料別1<br>
	* 42或43或61，二位文數字
  *
  * @param dataCode1 補報送檔案格式資料別1
	*/
  public void setDataCode1(String dataCode1) {
    this.dataCode1 = dataCode1;
  }

/**
	* 補報送債權機構代號2<br>
	* 三位文數字
	* @return String
	*/
  public String getBankCode2() {
    return this.bankCode2 == null ? "" : this.bankCode2;
  }

/**
	* 補報送債權機構代號2<br>
	* 三位文數字
  *
  * @param bankCode2 補報送債權機構代號2
	*/
  public void setBankCode2(String bankCode2) {
    this.bankCode2 = bankCode2;
  }

/**
	* 補報送檔案格式資料別2<br>
	* 42或43或61，二位文數字
	* @return String
	*/
  public String getDataCode2() {
    return this.dataCode2 == null ? "" : this.dataCode2;
  }

/**
	* 補報送檔案格式資料別2<br>
	* 42或43或61，二位文數字
  *
  * @param dataCode2 補報送檔案格式資料別2
	*/
  public void setDataCode2(String dataCode2) {
    this.dataCode2 = dataCode2;
  }

/**
	* 補報送債權機構代號3<br>
	* 三位文數字
	* @return String
	*/
  public String getBankCode3() {
    return this.bankCode3 == null ? "" : this.bankCode3;
  }

/**
	* 補報送債權機構代號3<br>
	* 三位文數字
  *
  * @param bankCode3 補報送債權機構代號3
	*/
  public void setBankCode3(String bankCode3) {
    this.bankCode3 = bankCode3;
  }

/**
	* 補報送檔案格式資料別3<br>
	* 42或43或61，二位文數字
	* @return String
	*/
  public String getDataCode3() {
    return this.dataCode3 == null ? "" : this.dataCode3;
  }

/**
	* 補報送檔案格式資料別3<br>
	* 42或43或61，二位文數字
  *
  * @param dataCode3 補報送檔案格式資料別3
	*/
  public void setDataCode3(String dataCode3) {
    this.dataCode3 = dataCode3;
  }

/**
	* 補報送債權機構代號4<br>
	* 三位文數字
	* @return String
	*/
  public String getBankCode4() {
    return this.bankCode4 == null ? "" : this.bankCode4;
  }

/**
	* 補報送債權機構代號4<br>
	* 三位文數字
  *
  * @param bankCode4 補報送債權機構代號4
	*/
  public void setBankCode4(String bankCode4) {
    this.bankCode4 = bankCode4;
  }

/**
	* 補報送檔案格式資料別4<br>
	* 42或43或61，二位文數字
	* @return String
	*/
  public String getDataCode4() {
    return this.dataCode4 == null ? "" : this.dataCode4;
  }

/**
	* 補報送檔案格式資料別4<br>
	* 42或43或61，二位文數字
  *
  * @param dataCode4 補報送檔案格式資料別4
	*/
  public void setDataCode4(String dataCode4) {
    this.dataCode4 = dataCode4;
  }

/**
	* 補報送債權機構代號5<br>
	* 三位文數字
	* @return String
	*/
  public String getBankCode5() {
    return this.bankCode5 == null ? "" : this.bankCode5;
  }

/**
	* 補報送債權機構代號5<br>
	* 三位文數字
  *
  * @param bankCode5 補報送債權機構代號5
	*/
  public void setBankCode5(String bankCode5) {
    this.bankCode5 = bankCode5;
  }

/**
	* 補報送檔案格式資料別5<br>
	* 42或43或61，二位文數字
	* @return String
	*/
  public String getDataCode5() {
    return this.dataCode5 == null ? "" : this.dataCode5;
  }

/**
	* 補報送檔案格式資料別5<br>
	* 42或43或61，二位文數字
  *
  * @param dataCode5 補報送檔案格式資料別5
	*/
  public void setDataCode5(String dataCode5) {
    this.dataCode5 = dataCode5;
  }

/**
	* 申請變更還款條件日<br>
	* 為債務人備齊申請文件向最大債權金融機構申請變更還款日期
	* @return Integer
	*/
  public int getChangePayDate() {
    return StaticTool.bcToRoc(this.changePayDate);
  }

/**
	* 申請變更還款條件日<br>
	* 為債務人備齊申請文件向最大債權金融機構申請變更還款日期
  *
  * @param changePayDate 申請變更還款條件日
  * @throws LogicException when Date Is Warn	*/
  public void setChangePayDate(int changePayDate) throws LogicException {
    this.changePayDate = StaticTool.rocToBc(changePayDate);
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

/**
	* 實際報送日期<br>
	* 
	* @return Integer
	*/
  public int getActualFilingDate() {
    return StaticTool.bcToRoc(this.actualFilingDate);
  }

/**
	* 實際報送日期<br>
	* 
  *
  * @param actualFilingDate 實際報送日期
  * @throws LogicException when Date Is Warn	*/
  public void setActualFilingDate(int actualFilingDate) throws LogicException {
    this.actualFilingDate = StaticTool.rocToBc(actualFilingDate);
  }

/**
	* 實際報送記號<br>
	* 
	* @return String
	*/
  public String getActualFilingMark() {
    return this.actualFilingMark == null ? "" : this.actualFilingMark;
  }

/**
	* 實際報送記號<br>
	* 
  *
  * @param actualFilingMark 實際報送記號
	*/
  public void setActualFilingMark(String actualFilingMark) {
    this.actualFilingMark = actualFilingMark;
  }


  @Override
  public String toString() {
    return "JcicZ052 [jcicZ052Id=" + jcicZ052Id + ", tranKey=" + tranKey + ", bankCode1=" + bankCode1 + ", dataCode1=" + dataCode1
           + ", bankCode2=" + bankCode2 + ", dataCode2=" + dataCode2 + ", bankCode3=" + bankCode3 + ", dataCode3=" + dataCode3 + ", bankCode4=" + bankCode4 + ", dataCode4=" + dataCode4
           + ", bankCode5=" + bankCode5 + ", dataCode5=" + dataCode5 + ", changePayDate=" + changePayDate + ", outJcicTxtDate=" + outJcicTxtDate + ", ukey=" + ukey + ", createDate=" + createDate
           + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + ", actualFilingDate=" + actualFilingDate + ", actualFilingMark=" + actualFilingMark + "]";
  }
}
