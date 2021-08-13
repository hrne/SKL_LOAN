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
 * CollList 法催紀錄清單檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CollList`")
public class CollList implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 7857009143873099007L;

@EmbeddedId
  private CollListId collListId;

  // 戶號
  @Column(name = "`CustNo`", insertable = false, updatable = false)
  private int custNo = 0;

  // 額度
  @Column(name = "`FacmNo`", insertable = false, updatable = false)
  private int facmNo = 0;

  // 案件種類
  /* 1:法催2:債協 */
  @Column(name = "`CaseCode`", length = 1)
  private String caseCode;

  // 作業日期
  @Column(name = "`TxDate`")
  private int txDate = 0;

  // 作業項目
  /* 1 */
  @Column(name = "`TxCode`", length = 1)
  private String txCode;

  // 繳息迄日
  @Column(name = "`PrevIntDate`")
  private int prevIntDate = 0;

  // 應繳息日
  @Column(name = "`NextIntDate`")
  private int nextIntDate = 0;

  // 逾期期數
  @Column(name = "`OvduTerm`")
  private int ovduTerm = 0;

  // 逾期天數
  @Column(name = "`OvduDays`")
  private int ovduDays = 0;

  // 幣別
  @Column(name = "`CurrencyCode`", length = 3)
  private String currencyCode;

  // 本金餘額
  @Column(name = "`PrinBalance`")
  private BigDecimal prinBalance = new BigDecimal("0");

  // 呆帳餘額
  @Column(name = "`BadDebtBal`")
  private BigDecimal badDebtBal = new BigDecimal("0");

  // 催收員
  @Column(name = "`AccCollPsn`", length = 6)
  private String accCollPsn;

  // 法務人員
  @Column(name = "`LegalPsn`", length = 6)
  private String legalPsn;

  // 戶況
  /* 00: 正常戶  02: 催收戶03: 結案戶04: 逾期戶(逾期期數&amp;gt;=1)05: 催收結案戶06: 呆帳戶 07: 部分轉呆戶08: 債權轉讓戶09: 呆帳結案戶 */
  @Column(name = "`Status`")
  private int status = 0;

  // 業務科目代號
  /* CdAcCode會計科子細目設定檔310: 短期擔保放款 320: 中期擔保放款330: 長期擔保放款340: 三十年房貸990: 催收款項 */
  @Column(name = "`AcctCode`", length = 3)
  private String acctCode;

  // 額度業務科目
  /* CdAcCode會計科子細目設定檔310: 短期擔保放款 320: 中期擔保放款330: 長期擔保放款340: 三十年房貸 */
  @Column(name = "`FacAcctCode`", length = 3)
  private String facAcctCode;

  // 同擔保品戶號
  @Column(name = "`ClCustNo`")
  private int clCustNo = 0;

  // 同擔保品額度
  @Column(name = "`ClFacmNo`")
  private int clFacmNo = 0;

  // 同擔保品序列號
  /* 同擔保品逾期天數最久者為1,其餘依序排列 */
  @Column(name = "`ClRowNo`")
  private int clRowNo = 0;

  // 擔保品代號1
  @Column(name = "`ClCode1`")
  private int clCode1 = 0;

  // 擔保品代號2
  @Column(name = "`ClCode2`")
  private int clCode2 = 0;

  // 擔保品號碼
  /* 此額度在額度與擔保品關聯檔的主要擔保品號碼 */
  @Column(name = "`ClNo`")
  private int clNo = 0;

  // 展期記號
  /* 空白、1.展期一般 2.展期協議 */
  @Column(name = "`RenewCode`", length = 1)
  private String renewCode;

  // 會計日期
  @Column(name = "`AcDate`")
  private int acDate = 0;

  // 是否指定
  /* 空白 Y:是,N:否若催收或法務人員非CdCity裡設定的人，則此欄位為Y，否則預設N */
  @Column(name = "`IsSpecify`", length = 1)
  private String isSpecify;

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


  public CollListId getCollListId() {
    return this.collListId;
  }

  public void setCollListId(CollListId collListId) {
    this.collListId = collListId;
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
	* 案件種類<br>
	* 1:法催
2:債協
	* @return String
	*/
  public String getCaseCode() {
    return this.caseCode == null ? "" : this.caseCode;
  }

/**
	* 案件種類<br>
	* 1:法催
2:債協
  *
  * @param caseCode 案件種類
	*/
  public void setCaseCode(String caseCode) {
    this.caseCode = caseCode;
  }

/**
	* 作業日期<br>
	* 
	* @return Integer
	*/
  public int getTxDate() {
    return StaticTool.bcToRoc(this.txDate);
  }

/**
	* 作業日期<br>
	* 
  *
  * @param txDate 作業日期
  * @throws LogicException when Date Is Warn	*/
  public void setTxDate(int txDate) throws LogicException {
    this.txDate = StaticTool.rocToBc(txDate);
  }

/**
	* 作業項目<br>
	* 1
	* @return String
	*/
  public String getTxCode() {
    return this.txCode == null ? "" : this.txCode;
  }

/**
	* 作業項目<br>
	* 1
  *
  * @param txCode 作業項目
	*/
  public void setTxCode(String txCode) {
    this.txCode = txCode;
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
	* 應繳息日<br>
	* 
	* @return Integer
	*/
  public int getNextIntDate() {
    return StaticTool.bcToRoc(this.nextIntDate);
  }

/**
	* 應繳息日<br>
	* 
  *
  * @param nextIntDate 應繳息日
  * @throws LogicException when Date Is Warn	*/
  public void setNextIntDate(int nextIntDate) throws LogicException {
    this.nextIntDate = StaticTool.rocToBc(nextIntDate);
  }

/**
	* 逾期期數<br>
	* 
	* @return Integer
	*/
  public int getOvduTerm() {
    return this.ovduTerm;
  }

/**
	* 逾期期數<br>
	* 
  *
  * @param ovduTerm 逾期期數
	*/
  public void setOvduTerm(int ovduTerm) {
    this.ovduTerm = ovduTerm;
  }

/**
	* 逾期天數<br>
	* 
	* @return Integer
	*/
  public int getOvduDays() {
    return this.ovduDays;
  }

/**
	* 逾期天數<br>
	* 
  *
  * @param ovduDays 逾期天數
	*/
  public void setOvduDays(int ovduDays) {
    this.ovduDays = ovduDays;
  }

/**
	* 幣別<br>
	* 
	* @return String
	*/
  public String getCurrencyCode() {
    return this.currencyCode == null ? "" : this.currencyCode;
  }

/**
	* 幣別<br>
	* 
  *
  * @param currencyCode 幣別
	*/
  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
  }

/**
	* 本金餘額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getPrinBalance() {
    return this.prinBalance;
  }

/**
	* 本金餘額<br>
	* 
  *
  * @param prinBalance 本金餘額
	*/
  public void setPrinBalance(BigDecimal prinBalance) {
    this.prinBalance = prinBalance;
  }

/**
	* 呆帳餘額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getBadDebtBal() {
    return this.badDebtBal;
  }

/**
	* 呆帳餘額<br>
	* 
  *
  * @param badDebtBal 呆帳餘額
	*/
  public void setBadDebtBal(BigDecimal badDebtBal) {
    this.badDebtBal = badDebtBal;
  }

/**
	* 催收員<br>
	* 
	* @return String
	*/
  public String getAccCollPsn() {
    return this.accCollPsn == null ? "" : this.accCollPsn;
  }

/**
	* 催收員<br>
	* 
  *
  * @param accCollPsn 催收員
	*/
  public void setAccCollPsn(String accCollPsn) {
    this.accCollPsn = accCollPsn;
  }

/**
	* 法務人員<br>
	* 
	* @return String
	*/
  public String getLegalPsn() {
    return this.legalPsn == null ? "" : this.legalPsn;
  }

/**
	* 法務人員<br>
	* 
  *
  * @param legalPsn 法務人員
	*/
  public void setLegalPsn(String legalPsn) {
    this.legalPsn = legalPsn;
  }

/**
	* 戶況<br>
	* 00: 正常戶  
02: 催收戶
03: 結案戶
04: 逾期戶(逾期期數&amp;gt;=1)
05: 催收結案戶
06: 呆帳戶 
07: 部分轉呆戶
08: 債權轉讓戶
09: 呆帳結案戶
	* @return Integer
	*/
  public int getStatus() {
    return this.status;
  }

/**
	* 戶況<br>
	* 00: 正常戶  
02: 催收戶
03: 結案戶
04: 逾期戶(逾期期數&amp;gt;=1)
05: 催收結案戶
06: 呆帳戶 
07: 部分轉呆戶
08: 債權轉讓戶
09: 呆帳結案戶
  *
  * @param status 戶況
	*/
  public void setStatus(int status) {
    this.status = status;
  }

/**
	* 業務科目代號<br>
	* CdAcCode會計科子細目設定檔
310: 短期擔保放款 
320: 中期擔保放款
330: 長期擔保放款
340: 三十年房貸
990: 催收款項
	* @return String
	*/
  public String getAcctCode() {
    return this.acctCode == null ? "" : this.acctCode;
  }

/**
	* 業務科目代號<br>
	* CdAcCode會計科子細目設定檔
310: 短期擔保放款 
320: 中期擔保放款
330: 長期擔保放款
340: 三十年房貸
990: 催收款項
  *
  * @param acctCode 業務科目代號
	*/
  public void setAcctCode(String acctCode) {
    this.acctCode = acctCode;
  }

/**
	* 額度業務科目<br>
	* CdAcCode會計科子細目設定檔
310: 短期擔保放款 
320: 中期擔保放款
330: 長期擔保放款
340: 三十年房貸
	* @return String
	*/
  public String getFacAcctCode() {
    return this.facAcctCode == null ? "" : this.facAcctCode;
  }

/**
	* 額度業務科目<br>
	* CdAcCode會計科子細目設定檔
310: 短期擔保放款 
320: 中期擔保放款
330: 長期擔保放款
340: 三十年房貸
  *
  * @param facAcctCode 額度業務科目
	*/
  public void setFacAcctCode(String facAcctCode) {
    this.facAcctCode = facAcctCode;
  }

/**
	* 同擔保品戶號<br>
	* 
	* @return Integer
	*/
  public int getClCustNo() {
    return this.clCustNo;
  }

/**
	* 同擔保品戶號<br>
	* 
  *
  * @param clCustNo 同擔保品戶號
	*/
  public void setClCustNo(int clCustNo) {
    this.clCustNo = clCustNo;
  }

/**
	* 同擔保品額度<br>
	* 
	* @return Integer
	*/
  public int getClFacmNo() {
    return this.clFacmNo;
  }

/**
	* 同擔保品額度<br>
	* 
  *
  * @param clFacmNo 同擔保品額度
	*/
  public void setClFacmNo(int clFacmNo) {
    this.clFacmNo = clFacmNo;
  }

/**
	* 同擔保品序列號<br>
	* 同擔保品逾期天數最久者為1,其餘依序排列
	* @return Integer
	*/
  public int getClRowNo() {
    return this.clRowNo;
  }

/**
	* 同擔保品序列號<br>
	* 同擔保品逾期天數最久者為1,其餘依序排列
  *
  * @param clRowNo 同擔保品序列號
	*/
  public void setClRowNo(int clRowNo) {
    this.clRowNo = clRowNo;
  }

/**
	* 擔保品代號1<br>
	* 
	* @return Integer
	*/
  public int getClCode1() {
    return this.clCode1;
  }

/**
	* 擔保品代號1<br>
	* 
  *
  * @param clCode1 擔保品代號1
	*/
  public void setClCode1(int clCode1) {
    this.clCode1 = clCode1;
  }

/**
	* 擔保品代號2<br>
	* 
	* @return Integer
	*/
  public int getClCode2() {
    return this.clCode2;
  }

/**
	* 擔保品代號2<br>
	* 
  *
  * @param clCode2 擔保品代號2
	*/
  public void setClCode2(int clCode2) {
    this.clCode2 = clCode2;
  }

/**
	* 擔保品號碼<br>
	* 此額度在額度與擔保品關聯檔的主要擔保品號碼
	* @return Integer
	*/
  public int getClNo() {
    return this.clNo;
  }

/**
	* 擔保品號碼<br>
	* 此額度在額度與擔保品關聯檔的主要擔保品號碼
  *
  * @param clNo 擔保品號碼
	*/
  public void setClNo(int clNo) {
    this.clNo = clNo;
  }

/**
	* 展期記號<br>
	* 空白、1.展期一般 2.展期協議
	* @return String
	*/
  public String getRenewCode() {
    return this.renewCode == null ? "" : this.renewCode;
  }

/**
	* 展期記號<br>
	* 空白、1.展期一般 2.展期協議
  *
  * @param renewCode 展期記號
	*/
  public void setRenewCode(String renewCode) {
    this.renewCode = renewCode;
  }

/**
	* 會計日期<br>
	* 
	* @return Integer
	*/
  public int getAcDate() {
    return StaticTool.bcToRoc(this.acDate);
  }

/**
	* 會計日期<br>
	* 
  *
  * @param acDate 會計日期
  * @throws LogicException when Date Is Warn	*/
  public void setAcDate(int acDate) throws LogicException {
    this.acDate = StaticTool.rocToBc(acDate);
  }

/**
	* 是否指定<br>
	* 空白 Y:是,N:否
若催收或法務人員非CdCity裡設定的人，則此欄位為Y，否則預設N
	* @return String
	*/
  public String getIsSpecify() {
    return this.isSpecify == null ? "" : this.isSpecify;
  }

/**
	* 是否指定<br>
	* 空白 Y:是,N:否
若催收或法務人員非CdCity裡設定的人，則此欄位為Y，否則預設N
  *
  * @param isSpecify 是否指定
	*/
  public void setIsSpecify(String isSpecify) {
    this.isSpecify = isSpecify;
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
    return "CollList [collListId=" + collListId + ", caseCode=" + caseCode + ", txDate=" + txDate + ", txCode=" + txCode + ", prevIntDate=" + prevIntDate
           + ", nextIntDate=" + nextIntDate + ", ovduTerm=" + ovduTerm + ", ovduDays=" + ovduDays + ", currencyCode=" + currencyCode + ", prinBalance=" + prinBalance + ", badDebtBal=" + badDebtBal
           + ", accCollPsn=" + accCollPsn + ", legalPsn=" + legalPsn + ", status=" + status + ", acctCode=" + acctCode + ", facAcctCode=" + facAcctCode + ", clCustNo=" + clCustNo
           + ", clFacmNo=" + clFacmNo + ", clRowNo=" + clRowNo + ", clCode1=" + clCode1 + ", clCode2=" + clCode2 + ", clNo=" + clNo + ", renewCode=" + renewCode
           + ", acDate=" + acDate + ", isSpecify=" + isSpecify + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo
           + "]";
  }
}
