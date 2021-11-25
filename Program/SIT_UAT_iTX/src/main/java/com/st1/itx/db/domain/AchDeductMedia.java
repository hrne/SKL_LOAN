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
 * AchDeductMedia ACH扣款媒體檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`AchDeductMedia`")
public class AchDeductMedia implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 7204657702002842999L;

@EmbeddedId
  private AchDeductMediaId achDeductMediaId;

  // 媒體日期
  @Column(name = "`MediaDate`", insertable = false, updatable = false)
  private int mediaDate = 0;

  // 媒體別
  /* 0:非ACH1:ACH新光2:ACH他行 */
  @Column(name = "`MediaKind`", length = 1, insertable = false, updatable = false)
  private String mediaKind;

  // 媒體序號
  @Column(name = "`MediaSeq`", insertable = false, updatable = false)
  private int mediaSeq = 0;

  // 戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 額度號碼
  @Column(name = "`FacmNo`")
  private int facmNo = 0;

  // 還款類別
  /* CdCode:RepayType1.期款2.部分償還3.結案4.帳管費5.火險費6.契變手續費7.法務費9.其他銀扣媒體檔用1.火險費 2.帳管費 3.期款 4.貸後契變手續費 */
  @Column(name = "`RepayType`")
  private int repayType = 0;

  // 扣款金額,還款金額
  @Column(name = "`RepayAmt`")
  private BigDecimal repayAmt = new BigDecimal("0");

  // 退件理由代號
  /* 提回規格，00-成功 */
  @Column(name = "`ReturnCode`", length = 2)
  private String returnCode;

  // 入帳日期
  @Column(name = "`EntryDate`")
  private int entryDate = 0;

  // 繳息迄日
  @Column(name = "`PrevIntDate`")
  private int prevIntDate = 0;

  // 扣款銀行
  @Column(name = "`RepayBank`", length = 3)
  private String repayBank;

  // 扣款帳號
  @Column(name = "`RepayAcctNo`", length = 14)
  private String repayAcctNo;

  // 入帳扣款別
  /* 1.火險費2.帳管費3.期款4.貸後契變手續費 */
  @Column(name = "`AchRepayCode`", length = 1)
  private String achRepayCode;

  // 科目
  @Column(name = "`AcctCode`", length = 3)
  private String acctCode;

  // 計息起日
  @Column(name = "`IntStartDate`")
  private int intStartDate = 0;

  // 計息迄日
  @Column(name = "`IntEndDate`")
  private int intEndDate = 0;

  // 存摺代號
  @Column(name = "`DepCode`", length = 2)
  private String depCode;

  // 與借款人關係
  @Column(name = "`RelationCode`", length = 2)
  private String relationCode;

  // 帳戶戶名
  @Column(name = "`RelCustName`", length = 100)
  private String relCustName;

  // 身分證字號
  @Column(name = "`RelCustId`", length = 10)
  private String relCustId;

  // 會計日期
  /* 提回會計日期 */
  @Column(name = "`AcDate`")
  private int acDate = 0;

  // 批號
  @Column(name = "`BatchNo`", length = 6)
  private String batchNo;

  // 明細序號
  @Column(name = "`DetailSeq`")
  private int detailSeq = 0;

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


  public AchDeductMediaId getAchDeductMediaId() {
    return this.achDeductMediaId;
  }

  public void setAchDeductMediaId(AchDeductMediaId achDeductMediaId) {
    this.achDeductMediaId = achDeductMediaId;
  }

/**
	* 媒體日期<br>
	* 
	* @return Integer
	*/
  public int getMediaDate() {
    return StaticTool.bcToRoc(this.mediaDate);
  }

/**
	* 媒體日期<br>
	* 
  *
  * @param mediaDate 媒體日期
  * @throws LogicException when Date Is Warn	*/
  public void setMediaDate(int mediaDate) throws LogicException {
    this.mediaDate = StaticTool.rocToBc(mediaDate);
  }

/**
	* 媒體別<br>
	* 0:非ACH
1:ACH新光
2:ACH他行
	* @return String
	*/
  public String getMediaKind() {
    return this.mediaKind == null ? "" : this.mediaKind;
  }

/**
	* 媒體別<br>
	* 0:非ACH
1:ACH新光
2:ACH他行
  *
  * @param mediaKind 媒體別
	*/
  public void setMediaKind(String mediaKind) {
    this.mediaKind = mediaKind;
  }

/**
	* 媒體序號<br>
	* 
	* @return Integer
	*/
  public int getMediaSeq() {
    return this.mediaSeq;
  }

/**
	* 媒體序號<br>
	* 
  *
  * @param mediaSeq 媒體序號
	*/
  public void setMediaSeq(int mediaSeq) {
    this.mediaSeq = mediaSeq;
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
	* 額度號碼<br>
	* 
	* @return Integer
	*/
  public int getFacmNo() {
    return this.facmNo;
  }

/**
	* 額度號碼<br>
	* 
  *
  * @param facmNo 額度號碼
	*/
  public void setFacmNo(int facmNo) {
    this.facmNo = facmNo;
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

銀扣媒體檔用
1.火險費 
2.帳管費 
3.期款 
4.貸後契變手續費
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

銀扣媒體檔用
1.火險費 
2.帳管費 
3.期款 
4.貸後契變手續費
  *
  * @param repayType 還款類別
	*/
  public void setRepayType(int repayType) {
    this.repayType = repayType;
  }

/**
	* 扣款金額,還款金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getRepayAmt() {
    return this.repayAmt;
  }

/**
	* 扣款金額,還款金額<br>
	* 
  *
  * @param repayAmt 扣款金額,還款金額
	*/
  public void setRepayAmt(BigDecimal repayAmt) {
    this.repayAmt = repayAmt;
  }

/**
	* 退件理由代號<br>
	* 提回規格，00-成功
	* @return String
	*/
  public String getReturnCode() {
    return this.returnCode == null ? "" : this.returnCode;
  }

/**
	* 退件理由代號<br>
	* 提回規格，00-成功
  *
  * @param returnCode 退件理由代號
	*/
  public void setReturnCode(String returnCode) {
    this.returnCode = returnCode;
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
	* 扣款銀行<br>
	* 
	* @return String
	*/
  public String getRepayBank() {
    return this.repayBank == null ? "" : this.repayBank;
  }

/**
	* 扣款銀行<br>
	* 
  *
  * @param repayBank 扣款銀行
	*/
  public void setRepayBank(String repayBank) {
    this.repayBank = repayBank;
  }

/**
	* 扣款帳號<br>
	* 
	* @return String
	*/
  public String getRepayAcctNo() {
    return this.repayAcctNo == null ? "" : this.repayAcctNo;
  }

/**
	* 扣款帳號<br>
	* 
  *
  * @param repayAcctNo 扣款帳號
	*/
  public void setRepayAcctNo(String repayAcctNo) {
    this.repayAcctNo = repayAcctNo;
  }

/**
	* 入帳扣款別<br>
	* 1.火險費
2.帳管費
3.期款
4.貸後契變手續費
	* @return String
	*/
  public String getAchRepayCode() {
    return this.achRepayCode == null ? "" : this.achRepayCode;
  }

/**
	* 入帳扣款別<br>
	* 1.火險費
2.帳管費
3.期款
4.貸後契變手續費
  *
  * @param achRepayCode 入帳扣款別
	*/
  public void setAchRepayCode(String achRepayCode) {
    this.achRepayCode = achRepayCode;
  }

/**
	* 科目<br>
	* 
	* @return String
	*/
  public String getAcctCode() {
    return this.acctCode == null ? "" : this.acctCode;
  }

/**
	* 科目<br>
	* 
  *
  * @param acctCode 科目
	*/
  public void setAcctCode(String acctCode) {
    this.acctCode = acctCode;
  }

/**
	* 計息起日<br>
	* 
	* @return Integer
	*/
  public int getIntStartDate() {
    return StaticTool.bcToRoc(this.intStartDate);
  }

/**
	* 計息起日<br>
	* 
  *
  * @param intStartDate 計息起日
  * @throws LogicException when Date Is Warn	*/
  public void setIntStartDate(int intStartDate) throws LogicException {
    this.intStartDate = StaticTool.rocToBc(intStartDate);
  }

/**
	* 計息迄日<br>
	* 
	* @return Integer
	*/
  public int getIntEndDate() {
    return StaticTool.bcToRoc(this.intEndDate);
  }

/**
	* 計息迄日<br>
	* 
  *
  * @param intEndDate 計息迄日
  * @throws LogicException when Date Is Warn	*/
  public void setIntEndDate(int intEndDate) throws LogicException {
    this.intEndDate = StaticTool.rocToBc(intEndDate);
  }

/**
	* 存摺代號<br>
	* 
	* @return String
	*/
  public String getDepCode() {
    return this.depCode == null ? "" : this.depCode;
  }

/**
	* 存摺代號<br>
	* 
  *
  * @param depCode 存摺代號
	*/
  public void setDepCode(String depCode) {
    this.depCode = depCode;
  }

/**
	* 與借款人關係<br>
	* 
	* @return String
	*/
  public String getRelationCode() {
    return this.relationCode == null ? "" : this.relationCode;
  }

/**
	* 與借款人關係<br>
	* 
  *
  * @param relationCode 與借款人關係
	*/
  public void setRelationCode(String relationCode) {
    this.relationCode = relationCode;
  }

/**
	* 帳戶戶名<br>
	* 
	* @return String
	*/
  public String getRelCustName() {
    return this.relCustName == null ? "" : this.relCustName;
  }

/**
	* 帳戶戶名<br>
	* 
  *
  * @param relCustName 帳戶戶名
	*/
  public void setRelCustName(String relCustName) {
    this.relCustName = relCustName;
  }

/**
	* 身分證字號<br>
	* 
	* @return String
	*/
  public String getRelCustId() {
    return this.relCustId == null ? "" : this.relCustId;
  }

/**
	* 身分證字號<br>
	* 
  *
  * @param relCustId 身分證字號
	*/
  public void setRelCustId(String relCustId) {
    this.relCustId = relCustId;
  }

/**
	* 會計日期<br>
	* 提回會計日期
	* @return Integer
	*/
  public int getAcDate() {
    return StaticTool.bcToRoc(this.acDate);
  }

/**
	* 會計日期<br>
	* 提回會計日期
  *
  * @param acDate 會計日期
  * @throws LogicException when Date Is Warn	*/
  public void setAcDate(int acDate) throws LogicException {
    this.acDate = StaticTool.rocToBc(acDate);
  }

/**
	* 批號<br>
	* 
	* @return String
	*/
  public String getBatchNo() {
    return this.batchNo == null ? "" : this.batchNo;
  }

/**
	* 批號<br>
	* 
  *
  * @param batchNo 批號
	*/
  public void setBatchNo(String batchNo) {
    this.batchNo = batchNo;
  }

/**
	* 明細序號<br>
	* 
	* @return Integer
	*/
  public int getDetailSeq() {
    return this.detailSeq;
  }

/**
	* 明細序號<br>
	* 
  *
  * @param detailSeq 明細序號
	*/
  public void setDetailSeq(int detailSeq) {
    this.detailSeq = detailSeq;
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
    return "AchDeductMedia [achDeductMediaId=" + achDeductMediaId + ", custNo=" + custNo + ", facmNo=" + facmNo + ", repayType=" + repayType
           + ", repayAmt=" + repayAmt + ", returnCode=" + returnCode + ", entryDate=" + entryDate + ", prevIntDate=" + prevIntDate + ", repayBank=" + repayBank + ", repayAcctNo=" + repayAcctNo
           + ", achRepayCode=" + achRepayCode + ", acctCode=" + acctCode + ", intStartDate=" + intStartDate + ", intEndDate=" + intEndDate + ", depCode=" + depCode + ", relationCode=" + relationCode
           + ", relCustName=" + relCustName + ", relCustId=" + relCustId + ", acDate=" + acDate + ", batchNo=" + batchNo + ", detailSeq=" + detailSeq + ", createDate=" + createDate
           + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
