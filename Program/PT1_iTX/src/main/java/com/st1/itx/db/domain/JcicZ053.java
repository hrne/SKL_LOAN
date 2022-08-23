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
 * JcicZ053 同意報送例外處理檔案<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicZ053`")
public class JcicZ053 implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 3681848529054576976L;

@EmbeddedId
  private JcicZ053Id jcicZ053Id;

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
  /* 指最大債權金融機構實際之收件日期 */
  @Column(name = "`RcDate`", insertable = false, updatable = false)
  private int rcDate = 0;

  // 最大債權金融機構代號
  /* 三位文數字 */
  @Column(name = "`MaxMainCode`", length = 3, insertable = false, updatable = false)
  private String maxMainCode;

  // 是否同意報送例外處理檔案格式
  /* Y;N */
  @Column(name = "`AgreeSend`", length = 1)
  private String agreeSend;

  // 同意補報送檔案格式資料別1
  /* 二位文數字42,43,61 */
  @Column(name = "`AgreeSendData1`", length = 2)
  private String agreeSendData1;

  // 同意補報送檔案格式資料別2
  /* 二位文數字42,43,61 */
  @Column(name = "`AgreeSendData2`", length = 2)
  private String agreeSendData2;

  // 申請變更還款條件日
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


  public JcicZ053Id getJcicZ053Id() {
    return this.jcicZ053Id;
  }

  public void setJcicZ053Id(JcicZ053Id jcicZ053Id) {
    this.jcicZ053Id = jcicZ053Id;
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
	* 指最大債權金融機構實際之收件日期
	* @return Integer
	*/
  public int getRcDate() {
    return StaticTool.bcToRoc(this.rcDate);
  }

/**
	* 協商申請日<br>
	* 指最大債權金融機構實際之收件日期
  *
  * @param rcDate 協商申請日
  * @throws LogicException when Date Is Warn	*/
  public void setRcDate(int rcDate) throws LogicException {
    this.rcDate = StaticTool.rocToBc(rcDate);
  }

/**
	* 最大債權金融機構代號<br>
	* 三位文數字
	* @return String
	*/
  public String getMaxMainCode() {
    return this.maxMainCode == null ? "" : this.maxMainCode;
  }

/**
	* 最大債權金融機構代號<br>
	* 三位文數字
  *
  * @param maxMainCode 最大債權金融機構代號
	*/
  public void setMaxMainCode(String maxMainCode) {
    this.maxMainCode = maxMainCode;
  }

/**
	* 是否同意報送例外處理檔案格式<br>
	* Y;N
	* @return String
	*/
  public String getAgreeSend() {
    return this.agreeSend == null ? "" : this.agreeSend;
  }

/**
	* 是否同意報送例外處理檔案格式<br>
	* Y;N
  *
  * @param agreeSend 是否同意報送例外處理檔案格式
	*/
  public void setAgreeSend(String agreeSend) {
    this.agreeSend = agreeSend;
  }

/**
	* 同意補報送檔案格式資料別1<br>
	* 二位文數字42,43,61
	* @return String
	*/
  public String getAgreeSendData1() {
    return this.agreeSendData1 == null ? "" : this.agreeSendData1;
  }

/**
	* 同意補報送檔案格式資料別1<br>
	* 二位文數字42,43,61
  *
  * @param agreeSendData1 同意補報送檔案格式資料別1
	*/
  public void setAgreeSendData1(String agreeSendData1) {
    this.agreeSendData1 = agreeSendData1;
  }

/**
	* 同意補報送檔案格式資料別2<br>
	* 二位文數字42,43,61
	* @return String
	*/
  public String getAgreeSendData2() {
    return this.agreeSendData2 == null ? "" : this.agreeSendData2;
  }

/**
	* 同意補報送檔案格式資料別2<br>
	* 二位文數字42,43,61
  *
  * @param agreeSendData2 同意補報送檔案格式資料別2
	*/
  public void setAgreeSendData2(String agreeSendData2) {
    this.agreeSendData2 = agreeSendData2;
  }

/**
	* 申請變更還款條件日<br>
	* 
	* @return Integer
	*/
  public int getChangePayDate() {
    return StaticTool.bcToRoc(this.changePayDate);
  }

/**
	* 申請變更還款條件日<br>
	* 
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
    return "JcicZ053 [jcicZ053Id=" + jcicZ053Id + ", tranKey=" + tranKey + ", agreeSend=" + agreeSend
           + ", agreeSendData1=" + agreeSendData1 + ", agreeSendData2=" + agreeSendData2 + ", changePayDate=" + changePayDate + ", outJcicTxtDate=" + outJcicTxtDate + ", ukey=" + ukey + ", createDate=" + createDate
           + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + ", actualFilingDate=" + actualFilingDate + ", actualFilingMark=" + actualFilingMark + "]";
  }
}
