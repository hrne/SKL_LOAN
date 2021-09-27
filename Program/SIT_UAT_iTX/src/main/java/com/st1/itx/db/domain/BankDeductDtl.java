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
 * BankDeductDtl 銀行扣款明細檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`BankDeductDtl`")
public class BankDeductDtl implements Serializable {


  @EmbeddedId
  private BankDeductDtlId bankDeductDtlId;

  // 入帳日期
  @Column(name = "`EntryDate`", insertable = false, updatable = false)
  private int entryDate = 0;

  // 戶號
  @Column(name = "`CustNo`", insertable = false, updatable = false)
  private int custNo = 0;

  // 額度
  @Column(name = "`FacmNo`", insertable = false, updatable = false)
  private int facmNo = 0;

  // 撥款
  @Column(name = "`BormNo`", insertable = false, updatable = false)
  private int bormNo = 0;

  // 還款類別
  /* CdCode:RepayType1.期款2.部分償還3.結案4.帳管費5.火險費6.契變手續費7.法務費9.其他 */
  @Column(name = "`RepayType`", insertable = false, updatable = false)
  private int repayType = 0;

  // 應繳日
  @Column(name = "`PayIntDate`", insertable = false, updatable = false)
  private int payIntDate = 0;

  // 繳息迄日
  @Column(name = "`PrevIntDate`")
  private int prevIntDate = 0;

  // 科目
  /* L4451建檔交易產生者，此欄位由額度檔抓取 */
  @Column(name = "`AcctCode`", length = 3)
  private String acctCode;

  // 扣款銀行
  /* L4451建檔交易產生者，此欄位由額度檔抓取CdCode:BankCdAppl */
  @Column(name = "`RepayBank`", length = 3)
  private String repayBank;

  // 扣款帳號
  /* L4451建檔交易產生者，此欄位由額度檔抓取 */
  @Column(name = "`RepayAcctNo`", length = 14)
  private String repayAcctNo;

  // 帳號碼
  /* 郵局用 */
  @Column(name = "`RepayAcctSeq`", length = 2)
  private String repayAcctSeq;

  // 應扣金額
  @Column(name = "`UnpaidAmt`")
  private BigDecimal unpaidAmt = new BigDecimal("0");

  // 暫收款抵繳金額
  @Column(name = "`TempAmt`")
  private BigDecimal tempAmt = new BigDecimal("0");

  // 扣款金額
  /* 期款時撥款會有多筆，多筆sum扣款金額 = 應扣金額 - 暫收抵繳金額 */
  @Column(name = "`RepayAmt`")
  private BigDecimal repayAmt = new BigDecimal("0");

  // 計息起日
  /* L4451建檔交易產生者，此欄位由撥款檔抓取 */
  @Column(name = "`IntStartDate`")
  private int intStartDate = 0;

  // 計息迄日
  /* L4451建檔交易產生者，此欄位由撥款檔抓取 */
  @Column(name = "`IntEndDate`")
  private int intEndDate = 0;

  // 郵局存款別
  /* L4451建檔交易產生者，此欄位由額度檔抓取CdCode:PostDepCodeG: 劃撥 P: 存簿 */
  @Column(name = "`PostCode`", length = 1)
  private String postCode;

  // 媒體碼
  /* NA:未產Y:已產N:已產，扣款金額為0 */
  @Column(name = "`MediaCode`", length = 1)
  private String mediaCode;

  // 與借款人關係
  /* L4451建檔交易產生者，此欄位由額度檔抓取CdCode:RelationCode */
  @Column(name = "`RelationCode`", length = 2)
  private String relationCode;

  // 第三人帳戶戶名
  /* L4451建檔交易產生者，此欄位由額度檔抓取 */
  @Column(name = "`RelCustName`", length = 100)
  private String relCustName;

  // 第三人身分證字號
  /* L4451建檔交易產生者，此欄位由額度檔抓取 */
  @Column(name = "`RelCustId`", length = 10)
  private String relCustId;

  // 第三人出生日期
  @Column(name = "`RelAcctBirthday`")
  private int relAcctBirthday = 0;

  // 第三人性別
  /* CdCode:Sex */
  @Column(name = "`RelAcctGender`", length = 1)
  private String relAcctGender;

  // 媒體日期
  /* ACH、郵局扣款媒體檔 */
  @Column(name = "`MediaDate`")
  private int mediaDate = 0;

  // 媒體別
  /* 1:ACH新光2:ACH他行3:郵局 */
  @Column(name = "`MediaKind`", length = 1)
  private String mediaKind;

  // 媒體序號
  /* ACH、郵局扣款媒體檔 */
  @Column(name = "`MediaSeq`")
  private int mediaSeq = 0;

  // 會計日期
  /* Default為0，整批入帳成功，回寫此欄位 */
  @Column(name = "`AcDate`")
  private int acDate = 0;

  // AML回應碼
  /* CdCode:AmlCheckItem0.非可疑名單/已完成名單確認1.需審查/確認2.為凍結名單/未確定名單 */
  @Column(name = "`AmlRsp`", length = 1)
  private String amlRsp;

  // 回應代碼
  /* 空白:未回 00:扣款成功 &amp;gt;00 :扣款失敗失敗原因 : ref. CdCode ProcCode 處理說明 ACH  : 002 + ReturnCode(2) 郵局 : 003 + ReturnCode(2) */
  @Column(name = "`ReturnCode`", length = 2)
  private String returnCode;

  // jason格式紀錄欄
  @Column(name = "`JsonFields`", length = 300)
  private String jsonFields;

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


  public BankDeductDtlId getBankDeductDtlId() {
    return this.bankDeductDtlId;
  }

  public void setBankDeductDtlId(BankDeductDtlId bankDeductDtlId) {
    this.bankDeductDtlId = bankDeductDtlId;
  }

/**
	* 入帳日期<br>
	* 
	* @return Integer
	*/
  public int getEntryDate() {
    return StaticTool.bcToRoc(this.entryDate);
  }

/**
	* 入帳日期<br>
	* 
  *
  * @param entryDate 入帳日期
  * @throws LogicException when Date Is Warn	*/
  public void setEntryDate(int entryDate) throws LogicException {
    this.entryDate = StaticTool.rocToBc(entryDate);
  }

/**
	* 戶號<br>
	* 
	* @return Integer
	*/
  public int getCustNo() {
    return this.custNo;
  }

/**
	* 戶號<br>
	* 
  *
  * @param custNo 戶號
	*/
  public void setCustNo(int custNo) {
    this.custNo = custNo;
  }

/**
	* 額度<br>
	* 
	* @return Integer
	*/
  public int getFacmNo() {
    return this.facmNo;
  }

/**
	* 額度<br>
	* 
  *
  * @param facmNo 額度
	*/
  public void setFacmNo(int facmNo) {
    this.facmNo = facmNo;
  }

/**
	* 撥款<br>
	* 
	* @return Integer
	*/
  public int getBormNo() {
    return this.bormNo;
  }

/**
	* 撥款<br>
	* 
  *
  * @param bormNo 撥款
	*/
  public void setBormNo(int bormNo) {
    this.bormNo = bormNo;
  }

/**
	* 還款類別<br>
	* CdCode:RepayType
1.期款
2.部分償還
3.結案
4.帳管費
5.火險費
6.契變手續費
7.法務費
9.其他
	* @return Integer
	*/
  public int getRepayType() {
    return this.repayType;
  }

/**
	* 還款類別<br>
	* CdCode:RepayType
1.期款
2.部分償還
3.結案
4.帳管費
5.火險費
6.契變手續費
7.法務費
9.其他
  *
  * @param repayType 還款類別
	*/
  public void setRepayType(int repayType) {
    this.repayType = repayType;
  }

/**
	* 應繳日<br>
	* 
	* @return Integer
	*/
  public int getPayIntDate() {
    return StaticTool.bcToRoc(this.payIntDate);
  }

/**
	* 應繳日<br>
	* 
  *
  * @param payIntDate 應繳日
  * @throws LogicException when Date Is Warn	*/
  public void setPayIntDate(int payIntDate) throws LogicException {
    this.payIntDate = StaticTool.rocToBc(payIntDate);
  }

/**
	* 繳息迄日<br>
	* 
	* @return Integer
	*/
  public int getPrevIntDate() {
    return StaticTool.bcToRoc(this.prevIntDate);
  }

/**
	* 繳息迄日<br>
	* 
  *
  * @param prevIntDate 繳息迄日
  * @throws LogicException when Date Is Warn	*/
  public void setPrevIntDate(int prevIntDate) throws LogicException {
    this.prevIntDate = StaticTool.rocToBc(prevIntDate);
  }

/**
	* 科目<br>
	* L4451建檔交易產生者，此欄位由額度檔抓取
	* @return String
	*/
  public String getAcctCode() {
    return this.acctCode == null ? "" : this.acctCode;
  }

/**
	* 科目<br>
	* L4451建檔交易產生者，此欄位由額度檔抓取
  *
  * @param acctCode 科目
	*/
  public void setAcctCode(String acctCode) {
    this.acctCode = acctCode;
  }

/**
	* 扣款銀行<br>
	* L4451建檔交易產生者，此欄位由額度檔抓取
CdCode:BankCdAppl
	* @return String
	*/
  public String getRepayBank() {
    return this.repayBank == null ? "" : this.repayBank;
  }

/**
	* 扣款銀行<br>
	* L4451建檔交易產生者，此欄位由額度檔抓取
CdCode:BankCdAppl
  *
  * @param repayBank 扣款銀行
	*/
  public void setRepayBank(String repayBank) {
    this.repayBank = repayBank;
  }

/**
	* 扣款帳號<br>
	* L4451建檔交易產生者，此欄位由額度檔抓取
	* @return String
	*/
  public String getRepayAcctNo() {
    return this.repayAcctNo == null ? "" : this.repayAcctNo;
  }

/**
	* 扣款帳號<br>
	* L4451建檔交易產生者，此欄位由額度檔抓取
  *
  * @param repayAcctNo 扣款帳號
	*/
  public void setRepayAcctNo(String repayAcctNo) {
    this.repayAcctNo = repayAcctNo;
  }

/**
	* 帳號碼<br>
	* 郵局用
	* @return String
	*/
  public String getRepayAcctSeq() {
    return this.repayAcctSeq == null ? "" : this.repayAcctSeq;
  }

/**
	* 帳號碼<br>
	* 郵局用
  *
  * @param repayAcctSeq 帳號碼
	*/
  public void setRepayAcctSeq(String repayAcctSeq) {
    this.repayAcctSeq = repayAcctSeq;
  }

/**
	* 應扣金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getUnpaidAmt() {
    return this.unpaidAmt;
  }

/**
	* 應扣金額<br>
	* 
  *
  * @param unpaidAmt 應扣金額
	*/
  public void setUnpaidAmt(BigDecimal unpaidAmt) {
    this.unpaidAmt = unpaidAmt;
  }

/**
	* 暫收款抵繳金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getTempAmt() {
    return this.tempAmt;
  }

/**
	* 暫收款抵繳金額<br>
	* 
  *
  * @param tempAmt 暫收款抵繳金額
	*/
  public void setTempAmt(BigDecimal tempAmt) {
    this.tempAmt = tempAmt;
  }

/**
	* 扣款金額<br>
	* 期款時撥款會有多筆，多筆sum
扣款金額 = 應扣金額 - 暫收抵繳金額
	* @return BigDecimal
	*/
  public BigDecimal getRepayAmt() {
    return this.repayAmt;
  }

/**
	* 扣款金額<br>
	* 期款時撥款會有多筆，多筆sum
扣款金額 = 應扣金額 - 暫收抵繳金額
  *
  * @param repayAmt 扣款金額
	*/
  public void setRepayAmt(BigDecimal repayAmt) {
    this.repayAmt = repayAmt;
  }

/**
	* 計息起日<br>
	* L4451建檔交易產生者，此欄位由撥款檔抓取
	* @return Integer
	*/
  public int getIntStartDate() {
    return StaticTool.bcToRoc(this.intStartDate);
  }

/**
	* 計息起日<br>
	* L4451建檔交易產生者，此欄位由撥款檔抓取
  *
  * @param intStartDate 計息起日
  * @throws LogicException when Date Is Warn	*/
  public void setIntStartDate(int intStartDate) throws LogicException {
    this.intStartDate = StaticTool.rocToBc(intStartDate);
  }

/**
	* 計息迄日<br>
	* L4451建檔交易產生者，此欄位由撥款檔抓取
	* @return Integer
	*/
  public int getIntEndDate() {
    return StaticTool.bcToRoc(this.intEndDate);
  }

/**
	* 計息迄日<br>
	* L4451建檔交易產生者，此欄位由撥款檔抓取
  *
  * @param intEndDate 計息迄日
  * @throws LogicException when Date Is Warn	*/
  public void setIntEndDate(int intEndDate) throws LogicException {
    this.intEndDate = StaticTool.rocToBc(intEndDate);
  }

/**
	* 郵局存款別<br>
	* L4451建檔交易產生者，此欄位由額度檔抓取
CdCode:PostDepCode
G: 劃撥 P: 存簿
	* @return String
	*/
  public String getPostCode() {
    return this.postCode == null ? "" : this.postCode;
  }

/**
	* 郵局存款別<br>
	* L4451建檔交易產生者，此欄位由額度檔抓取
CdCode:PostDepCode
G: 劃撥 P: 存簿
  *
  * @param postCode 郵局存款別
	*/
  public void setPostCode(String postCode) {
    this.postCode = postCode;
  }

/**
	* 媒體碼<br>
	* NA:未產
Y:已產
N:已產，扣款金額為0
	* @return String
	*/
  public String getMediaCode() {
    return this.mediaCode == null ? "" : this.mediaCode;
  }

/**
	* 媒體碼<br>
	* NA:未產
Y:已產
N:已產，扣款金額為0
  *
  * @param mediaCode 媒體碼
	*/
  public void setMediaCode(String mediaCode) {
    this.mediaCode = mediaCode;
  }

/**
	* 與借款人關係<br>
	* L4451建檔交易產生者，此欄位由額度檔抓取
CdCode:RelationCode
	* @return String
	*/
  public String getRelationCode() {
    return this.relationCode == null ? "" : this.relationCode;
  }

/**
	* 與借款人關係<br>
	* L4451建檔交易產生者，此欄位由額度檔抓取
CdCode:RelationCode
  *
  * @param relationCode 與借款人關係
	*/
  public void setRelationCode(String relationCode) {
    this.relationCode = relationCode;
  }

/**
	* 第三人帳戶戶名<br>
	* L4451建檔交易產生者，此欄位由額度檔抓取
	* @return String
	*/
  public String getRelCustName() {
    return this.relCustName == null ? "" : this.relCustName;
  }

/**
	* 第三人帳戶戶名<br>
	* L4451建檔交易產生者，此欄位由額度檔抓取
  *
  * @param relCustName 第三人帳戶戶名
	*/
  public void setRelCustName(String relCustName) {
    this.relCustName = relCustName;
  }

/**
	* 第三人身分證字號<br>
	* L4451建檔交易產生者，此欄位由額度檔抓取
	* @return String
	*/
  public String getRelCustId() {
    return this.relCustId == null ? "" : this.relCustId;
  }

/**
	* 第三人身分證字號<br>
	* L4451建檔交易產生者，此欄位由額度檔抓取
  *
  * @param relCustId 第三人身分證字號
	*/
  public void setRelCustId(String relCustId) {
    this.relCustId = relCustId;
  }

/**
	* 第三人出生日期<br>
	* 
	* @return Integer
	*/
  public int getRelAcctBirthday() {
    return StaticTool.bcToRoc(this.relAcctBirthday);
  }

/**
	* 第三人出生日期<br>
	* 
  *
  * @param relAcctBirthday 第三人出生日期
  * @throws LogicException when Date Is Warn	*/
  public void setRelAcctBirthday(int relAcctBirthday) throws LogicException {
    this.relAcctBirthday = StaticTool.rocToBc(relAcctBirthday);
  }

/**
	* 第三人性別<br>
	* CdCode:Sex
	* @return String
	*/
  public String getRelAcctGender() {
    return this.relAcctGender == null ? "" : this.relAcctGender;
  }

/**
	* 第三人性別<br>
	* CdCode:Sex
  *
  * @param relAcctGender 第三人性別
	*/
  public void setRelAcctGender(String relAcctGender) {
    this.relAcctGender = relAcctGender;
  }

/**
	* 媒體日期<br>
	* ACH、郵局扣款媒體檔
	* @return Integer
	*/
  public int getMediaDate() {
    return this.mediaDate;
  }

/**
	* 媒體日期<br>
	* ACH、郵局扣款媒體檔
  *
  * @param mediaDate 媒體日期
	*/
  public void setMediaDate(int mediaDate) {
    this.mediaDate = mediaDate;
  }

/**
	* 媒體別<br>
	* 1:ACH新光
2:ACH他行
3:郵局
	* @return String
	*/
  public String getMediaKind() {
    return this.mediaKind == null ? "" : this.mediaKind;
  }

/**
	* 媒體別<br>
	* 1:ACH新光
2:ACH他行
3:郵局
  *
  * @param mediaKind 媒體別
	*/
  public void setMediaKind(String mediaKind) {
    this.mediaKind = mediaKind;
  }

/**
	* 媒體序號<br>
	* ACH、郵局扣款媒體檔
	* @return Integer
	*/
  public int getMediaSeq() {
    return this.mediaSeq;
  }

/**
	* 媒體序號<br>
	* ACH、郵局扣款媒體檔
  *
  * @param mediaSeq 媒體序號
	*/
  public void setMediaSeq(int mediaSeq) {
    this.mediaSeq = mediaSeq;
  }

/**
	* 會計日期<br>
	* Default為0，整批入帳成功，回寫此欄位
	* @return Integer
	*/
  public int getAcDate() {
    return StaticTool.bcToRoc(this.acDate);
  }

/**
	* 會計日期<br>
	* Default為0，整批入帳成功，回寫此欄位
  *
  * @param acDate 會計日期
  * @throws LogicException when Date Is Warn	*/
  public void setAcDate(int acDate) throws LogicException {
    this.acDate = StaticTool.rocToBc(acDate);
  }

/**
	* AML回應碼<br>
	* CdCode:AmlCheckItem
0.非可疑名單/已完成名單確認
1.需審查/確認
2.為凍結名單/未確定名單
	* @return String
	*/
  public String getAmlRsp() {
    return this.amlRsp == null ? "" : this.amlRsp;
  }

/**
	* AML回應碼<br>
	* CdCode:AmlCheckItem
0.非可疑名單/已完成名單確認
1.需審查/確認
2.為凍結名單/未確定名單
  *
  * @param amlRsp AML回應碼
	*/
  public void setAmlRsp(String amlRsp) {
    this.amlRsp = amlRsp;
  }

/**
	* 回應代碼<br>
	* 空白:未回 00:扣款成功 &amp;gt;00 :扣款失敗
失敗原因 : ref. CdCode ProcCode 處理說明
 ACH  : 002 + ReturnCode(2)
 郵局 : 003 + ReturnCode(2)
	* @return String
	*/
  public String getReturnCode() {
    return this.returnCode == null ? "" : this.returnCode;
  }

/**
	* 回應代碼<br>
	* 空白:未回 00:扣款成功 &amp;gt;00 :扣款失敗
失敗原因 : ref. CdCode ProcCode 處理說明
 ACH  : 002 + ReturnCode(2)
 郵局 : 003 + ReturnCode(2)
  *
  * @param returnCode 回應代碼
	*/
  public void setReturnCode(String returnCode) {
    this.returnCode = returnCode;
  }

/**
	* jason格式紀錄欄<br>
	* 
	* @return String
	*/
  public String getJsonFields() {
    return this.jsonFields == null ? "" : this.jsonFields;
  }

/**
	* jason格式紀錄欄<br>
	* 
  *
  * @param jsonFields jason格式紀錄欄
	*/
  public void setJsonFields(String jsonFields) {
    this.jsonFields = jsonFields;
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
    return "BankDeductDtl [bankDeductDtlId=" + bankDeductDtlId
           + ", prevIntDate=" + prevIntDate + ", acctCode=" + acctCode + ", repayBank=" + repayBank + ", repayAcctNo=" + repayAcctNo + ", repayAcctSeq=" + repayAcctSeq + ", unpaidAmt=" + unpaidAmt
           + ", tempAmt=" + tempAmt + ", repayAmt=" + repayAmt + ", intStartDate=" + intStartDate + ", intEndDate=" + intEndDate + ", postCode=" + postCode + ", mediaCode=" + mediaCode
           + ", relationCode=" + relationCode + ", relCustName=" + relCustName + ", relCustId=" + relCustId + ", relAcctBirthday=" + relAcctBirthday + ", relAcctGender=" + relAcctGender + ", mediaDate=" + mediaDate
           + ", mediaKind=" + mediaKind + ", mediaSeq=" + mediaSeq + ", acDate=" + acDate + ", amlRsp=" + amlRsp + ", returnCode=" + returnCode + ", jsonFields=" + jsonFields
           + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
