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
 * AcReceivable 會計銷帳檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`AcReceivable`")
public class AcReceivable implements Serializable {


  @EmbeddedId
  private AcReceivableId acReceivableId;

  // 業務科目代號
  /* CdAcCode會計科子細目設定檔 */
  @Column(name = "`AcctCode`", length = 3, insertable = false, updatable = false)
  private String acctCode;

  // 戶號
  @Column(name = "`CustNo`", insertable = false, updatable = false)
  private int custNo = 0;

  // 額度編號
  @Column(name = "`FacmNo`", insertable = false, updatable = false)
  private int facmNo = 0;

  // 銷帳編號
  /* 1:暫收款－可抵繳 : ''primary key 不可有null, 放一個空白2:擔保放款、催收款項 : 撥款序號(3)3:會計銷帳科目：系統自編(AC+西元年後兩碼+流水號六碼)4:暫收款－支票：支票帳號(9)-支票號碼(7) 5:未收帳管費：第一筆撥款序號(3) 6:未收契變手續費：契變日期(8,西元)+契變序號(02)7:未收、暫收、暫付、催收火險保費：原保單號碼(17)+批單號碼(1)8:暫付、催收法務費：記錄號碼(8)9:短繳期金：撥款序號(3)10:暫收款－借新還舊: 'FacmNo' + 額度編號(3)11:暫收款-冲正：''11:聯貸手續費:SL-費用代號(2)-流水號(3)-攤提年月(YYYMM) */
  @Column(name = "`RvNo`", length = 30, insertable = false, updatable = false)
  private String rvNo;

  // 科目代號
  /* CdAcCode會計科子細目設定檔，銷帳科目記號&amp;gt;=3放空白 */
  @Column(name = "`AcNoCode`", length = 11)
  private String acNoCode;

  // 子目代號
  /* CdAcCode會計科子細目設定檔 */
  @Column(name = "`AcSubCode`", length = 5)
  private String acSubCode;

  // 細目代號
  /* CdAcCode會計科子細目設定檔 */
  @Column(name = "`AcDtlCode`", length = 2)
  private String acDtlCode;

  // 單位別
  @Column(name = "`BranchNo`", length = 4)
  private String branchNo;

  // 幣別
  @Column(name = "`CurrencyCode`", length = 3)
  private String currencyCode;

  // 銷帳記號
  /* 0:未銷1:已銷 */
  @Column(name = "`ClsFlag`")
  private int clsFlag = 0;

  // 業務科目記號
  /* 0:一般科目1:資負明細科目 */
  @Column(name = "`AcctFlag`")
  private int acctFlag = 0;

  // 銷帳科目記號
  /* 1:會計銷帳科目2:業務銷帳科目3:未收費用4:短繳期金5:另收欠款 */
  @Column(name = "`ReceivableFlag`")
  private int receivableFlag = 0;

  // 起帳金額
  @Column(name = "`RvAmt`")
  private BigDecimal rvAmt = new BigDecimal("0");

  // 未銷餘額
  /* 上次作帳日餘額 */
  @Column(name = "`RvBal`")
  private BigDecimal rvBal = new BigDecimal("0");

  // 會計日餘額
  @Column(name = "`AcBal`")
  private BigDecimal acBal = new BigDecimal("0");

  // 傳票摘要
  @Column(name = "`SlipNote`", length = 80)
  private String slipNote;

  // 帳冊別
  /* 共用代碼檔201:利變年金 */
  @Column(name = "`AcBookCode`", length = 3)
  private String acBookCode;

  // 區隔帳冊
  /* 00A:傳統帳冊201:利變年金帳冊 */
  @Column(name = "`AcSubBookCode`", length = 3)
  private String acSubBookCode;

  // 起帳日期
  /* TMI,F09 火險保費：續保保單保險起日F07 暫付法務費：單據日期F10 帳管費：首次應繳日 */
  @Column(name = "`OpenAcDate`")
  private int openAcDate = 0;

  // 起帳交易代號
  @Column(name = "`OpenTxCd`", length = 5)
  private String openTxCd;

  // 起帳單位別
  @Column(name = "`OpenKinBr`", length = 4)
  private String openKinBr;

  // 起帳經辦
  @Column(name = "`OpenTlrNo`", length = 6)
  private String openTlrNo;

  // 起帳交易序號
  @Column(name = "`OpenTxtNo`")
  private int openTxtNo = 0;

  // 上次作帳日
  /* 本日帳交易:上次會計日、次日帳交易:本會計日 */
  @Column(name = "`LastAcDate`")
  private int lastAcDate = 0;

  // 最後交易日
  /* 含次日交易 */
  @Column(name = "`LastTxDate`")
  private int lastTxDate = 0;

  // 交易代號
  /* 起帳/銷帳 */
  @Column(name = "`TitaTxCd`", length = 5)
  private String titaTxCd;

  // 單位別
  /* 起帳/銷帳 */
  @Column(name = "`TitaKinBr`", length = 4)
  private String titaKinBr;

  // 經辦
  /* 起帳/銷帳 */
  @Column(name = "`TitaTlrNo`", length = 6)
  private String titaTlrNo;

  // 交易序號
  /* 起帳/銷帳 */
  @Column(name = "`TitaTxtNo`")
  private int titaTxtNo = 0;

  // jason格式紀錄欄
  @Column(name = "`JsonFields`", length = 300)
  private String jsonFields;

  // 建檔人員
  @Column(name = "`CreateEmpNo`", length = 6)
  private String createEmpNo;

  // 建檔日期
  @CreatedDate
  @Column(name = "`CreateDate`")
  private java.sql.Timestamp createDate;

  // 最後維護人員
  @Column(name = "`LastUpdateEmpNo`", length = 6)
  private String lastUpdateEmpNo;

  // 最後維護日期
  @LastModifiedDate
  @Column(name = "`LastUpdate`")
  private java.sql.Timestamp lastUpdate;


  public AcReceivableId getAcReceivableId() {
    return this.acReceivableId;
  }

  public void setAcReceivableId(AcReceivableId acReceivableId) {
    this.acReceivableId = acReceivableId;
  }

/**
	* 業務科目代號<br>
	* CdAcCode會計科子細目設定檔
	* @return String
	*/
  public String getAcctCode() {
    return this.acctCode == null ? "" : this.acctCode;
  }

/**
	* 業務科目代號<br>
	* CdAcCode會計科子細目設定檔
  *
  * @param acctCode 業務科目代號
	*/
  public void setAcctCode(String acctCode) {
    this.acctCode = acctCode;
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
	* 額度編號<br>
	* 
	* @return Integer
	*/
  public int getFacmNo() {
    return this.facmNo;
  }

/**
	* 額度編號<br>
	* 
  *
  * @param facmNo 額度編號
	*/
  public void setFacmNo(int facmNo) {
    this.facmNo = facmNo;
  }

/**
	* 銷帳編號<br>
	* 1:暫收款－可抵繳 : ''primary key 不可有null, 放一個空白
2:擔保放款、催收款項 : 撥款序號(3)
3:會計銷帳科目：系統自編(AC+西元年後兩碼+流水號六碼)
4:暫收款－支票：支票帳號(9)-支票號碼(7) 
5:未收帳管費：第一筆撥款序號(3) 
6:未收契變手續費：契變日期(8,西元)+契變序號(02)
7:未收、暫收、暫付、催收火險保費：原保單號碼(17)+批單號碼(1)
8:暫付、催收法務費：記錄號碼(8)
9:短繳期金：撥款序號(3)
10:暫收款－借新還舊: 'FacmNo' + 額度編號(3)
11:暫收款-冲正：''
11:聯貸手續費:SL-費用代號(2)-流水號(3)-攤提年月(YYYMM)
	* @return String
	*/
  public String getRvNo() {
    return this.rvNo == null ? "" : this.rvNo;
  }

/**
	* 銷帳編號<br>
	* 1:暫收款－可抵繳 : ''primary key 不可有null, 放一個空白
2:擔保放款、催收款項 : 撥款序號(3)
3:會計銷帳科目：系統自編(AC+西元年後兩碼+流水號六碼)
4:暫收款－支票：支票帳號(9)-支票號碼(7) 
5:未收帳管費：第一筆撥款序號(3) 
6:未收契變手續費：契變日期(8,西元)+契變序號(02)
7:未收、暫收、暫付、催收火險保費：原保單號碼(17)+批單號碼(1)
8:暫付、催收法務費：記錄號碼(8)
9:短繳期金：撥款序號(3)
10:暫收款－借新還舊: 'FacmNo' + 額度編號(3)
11:暫收款-冲正：''
11:聯貸手續費:SL-費用代號(2)-流水號(3)-攤提年月(YYYMM)
  *
  * @param rvNo 銷帳編號
	*/
  public void setRvNo(String rvNo) {
    this.rvNo = rvNo;
  }

/**
	* 科目代號<br>
	* CdAcCode會計科子細目設定檔，銷帳科目記號&amp;gt;=3放空白
	* @return String
	*/
  public String getAcNoCode() {
    return this.acNoCode == null ? "" : this.acNoCode;
  }

/**
	* 科目代號<br>
	* CdAcCode會計科子細目設定檔，銷帳科目記號&amp;gt;=3放空白
  *
  * @param acNoCode 科目代號
	*/
  public void setAcNoCode(String acNoCode) {
    this.acNoCode = acNoCode;
  }

/**
	* 子目代號<br>
	* CdAcCode會計科子細目設定檔
	* @return String
	*/
  public String getAcSubCode() {
    return this.acSubCode == null ? "" : this.acSubCode;
  }

/**
	* 子目代號<br>
	* CdAcCode會計科子細目設定檔
  *
  * @param acSubCode 子目代號
	*/
  public void setAcSubCode(String acSubCode) {
    this.acSubCode = acSubCode;
  }

/**
	* 細目代號<br>
	* CdAcCode會計科子細目設定檔
	* @return String
	*/
  public String getAcDtlCode() {
    return this.acDtlCode == null ? "" : this.acDtlCode;
  }

/**
	* 細目代號<br>
	* CdAcCode會計科子細目設定檔
  *
  * @param acDtlCode 細目代號
	*/
  public void setAcDtlCode(String acDtlCode) {
    this.acDtlCode = acDtlCode;
  }

/**
	* 單位別<br>
	* 
	* @return String
	*/
  public String getBranchNo() {
    return this.branchNo == null ? "" : this.branchNo;
  }

/**
	* 單位別<br>
	* 
  *
  * @param branchNo 單位別
	*/
  public void setBranchNo(String branchNo) {
    this.branchNo = branchNo;
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
	* 銷帳記號<br>
	* 0:未銷
1:已銷
	* @return Integer
	*/
  public int getClsFlag() {
    return this.clsFlag;
  }

/**
	* 銷帳記號<br>
	* 0:未銷
1:已銷
  *
  * @param clsFlag 銷帳記號
	*/
  public void setClsFlag(int clsFlag) {
    this.clsFlag = clsFlag;
  }

/**
	* 業務科目記號<br>
	* 0:一般科目
1:資負明細科目
	* @return Integer
	*/
  public int getAcctFlag() {
    return this.acctFlag;
  }

/**
	* 業務科目記號<br>
	* 0:一般科目
1:資負明細科目
  *
  * @param acctFlag 業務科目記號
	*/
  public void setAcctFlag(int acctFlag) {
    this.acctFlag = acctFlag;
  }

/**
	* 銷帳科目記號<br>
	* 1:會計銷帳科目
2:業務銷帳科目
3:未收費用
4:短繳期金
5:另收欠款
	* @return Integer
	*/
  public int getReceivableFlag() {
    return this.receivableFlag;
  }

/**
	* 銷帳科目記號<br>
	* 1:會計銷帳科目
2:業務銷帳科目
3:未收費用
4:短繳期金
5:另收欠款
  *
  * @param receivableFlag 銷帳科目記號
	*/
  public void setReceivableFlag(int receivableFlag) {
    this.receivableFlag = receivableFlag;
  }

/**
	* 起帳金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getRvAmt() {
    return this.rvAmt;
  }

/**
	* 起帳金額<br>
	* 
  *
  * @param rvAmt 起帳金額
	*/
  public void setRvAmt(BigDecimal rvAmt) {
    this.rvAmt = rvAmt;
  }

/**
	* 未銷餘額<br>
	* 上次作帳日餘額
	* @return BigDecimal
	*/
  public BigDecimal getRvBal() {
    return this.rvBal;
  }

/**
	* 未銷餘額<br>
	* 上次作帳日餘額
  *
  * @param rvBal 未銷餘額
	*/
  public void setRvBal(BigDecimal rvBal) {
    this.rvBal = rvBal;
  }

/**
	* 會計日餘額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getAcBal() {
    return this.acBal;
  }

/**
	* 會計日餘額<br>
	* 
  *
  * @param acBal 會計日餘額
	*/
  public void setAcBal(BigDecimal acBal) {
    this.acBal = acBal;
  }

/**
	* 傳票摘要<br>
	* 
	* @return String
	*/
  public String getSlipNote() {
    return this.slipNote == null ? "" : this.slipNote;
  }

/**
	* 傳票摘要<br>
	* 
  *
  * @param slipNote 傳票摘要
	*/
  public void setSlipNote(String slipNote) {
    this.slipNote = slipNote;
  }

/**
	* 帳冊別<br>
	* 共用代碼檔
201:利變年金
	* @return String
	*/
  public String getAcBookCode() {
    return this.acBookCode == null ? "" : this.acBookCode;
  }

/**
	* 帳冊別<br>
	* 共用代碼檔
201:利變年金
  *
  * @param acBookCode 帳冊別
	*/
  public void setAcBookCode(String acBookCode) {
    this.acBookCode = acBookCode;
  }

/**
	* 區隔帳冊<br>
	* 00A:傳統帳冊
201:利變年金帳冊
	* @return String
	*/
  public String getAcSubBookCode() {
    return this.acSubBookCode == null ? "" : this.acSubBookCode;
  }

/**
	* 區隔帳冊<br>
	* 00A:傳統帳冊
201:利變年金帳冊
  *
  * @param acSubBookCode 區隔帳冊
	*/
  public void setAcSubBookCode(String acSubBookCode) {
    this.acSubBookCode = acSubBookCode;
  }

/**
	* 起帳日期<br>
	* TMI,F09 火險保費：續保保單保險起日
F07 暫付法務費：單據日期
F10 帳管費：首次應繳日
	* @return Integer
	*/
  public int getOpenAcDate() {
    return StaticTool.bcToRoc(this.openAcDate);
  }

/**
	* 起帳日期<br>
	* TMI,F09 火險保費：續保保單保險起日
F07 暫付法務費：單據日期
F10 帳管費：首次應繳日
  *
  * @param openAcDate 起帳日期
  * @throws LogicException when Date Is Warn	*/
  public void setOpenAcDate(int openAcDate) throws LogicException {
    this.openAcDate = StaticTool.rocToBc(openAcDate);
  }

/**
	* 起帳交易代號<br>
	* 
	* @return String
	*/
  public String getOpenTxCd() {
    return this.openTxCd == null ? "" : this.openTxCd;
  }

/**
	* 起帳交易代號<br>
	* 
  *
  * @param openTxCd 起帳交易代號
	*/
  public void setOpenTxCd(String openTxCd) {
    this.openTxCd = openTxCd;
  }

/**
	* 起帳單位別<br>
	* 
	* @return String
	*/
  public String getOpenKinBr() {
    return this.openKinBr == null ? "" : this.openKinBr;
  }

/**
	* 起帳單位別<br>
	* 
  *
  * @param openKinBr 起帳單位別
	*/
  public void setOpenKinBr(String openKinBr) {
    this.openKinBr = openKinBr;
  }

/**
	* 起帳經辦<br>
	* 
	* @return String
	*/
  public String getOpenTlrNo() {
    return this.openTlrNo == null ? "" : this.openTlrNo;
  }

/**
	* 起帳經辦<br>
	* 
  *
  * @param openTlrNo 起帳經辦
	*/
  public void setOpenTlrNo(String openTlrNo) {
    this.openTlrNo = openTlrNo;
  }

/**
	* 起帳交易序號<br>
	* 
	* @return Integer
	*/
  public int getOpenTxtNo() {
    return this.openTxtNo;
  }

/**
	* 起帳交易序號<br>
	* 
  *
  * @param openTxtNo 起帳交易序號
	*/
  public void setOpenTxtNo(int openTxtNo) {
    this.openTxtNo = openTxtNo;
  }

/**
	* 上次作帳日<br>
	* 本日帳交易:上次會計日、次日帳交易:本會計日
	* @return Integer
	*/
  public int getLastAcDate() {
    return StaticTool.bcToRoc(this.lastAcDate);
  }

/**
	* 上次作帳日<br>
	* 本日帳交易:上次會計日、次日帳交易:本會計日
  *
  * @param lastAcDate 上次作帳日
  * @throws LogicException when Date Is Warn	*/
  public void setLastAcDate(int lastAcDate) throws LogicException {
    this.lastAcDate = StaticTool.rocToBc(lastAcDate);
  }

/**
	* 最後交易日<br>
	* 含次日交易
	* @return Integer
	*/
  public int getLastTxDate() {
    return StaticTool.bcToRoc(this.lastTxDate);
  }

/**
	* 最後交易日<br>
	* 含次日交易
  *
  * @param lastTxDate 最後交易日
  * @throws LogicException when Date Is Warn	*/
  public void setLastTxDate(int lastTxDate) throws LogicException {
    this.lastTxDate = StaticTool.rocToBc(lastTxDate);
  }

/**
	* 交易代號<br>
	* 起帳/銷帳
	* @return String
	*/
  public String getTitaTxCd() {
    return this.titaTxCd == null ? "" : this.titaTxCd;
  }

/**
	* 交易代號<br>
	* 起帳/銷帳
  *
  * @param titaTxCd 交易代號
	*/
  public void setTitaTxCd(String titaTxCd) {
    this.titaTxCd = titaTxCd;
  }

/**
	* 單位別<br>
	* 起帳/銷帳
	* @return String
	*/
  public String getTitaKinBr() {
    return this.titaKinBr == null ? "" : this.titaKinBr;
  }

/**
	* 單位別<br>
	* 起帳/銷帳
  *
  * @param titaKinBr 單位別
	*/
  public void setTitaKinBr(String titaKinBr) {
    this.titaKinBr = titaKinBr;
  }

/**
	* 經辦<br>
	* 起帳/銷帳
	* @return String
	*/
  public String getTitaTlrNo() {
    return this.titaTlrNo == null ? "" : this.titaTlrNo;
  }

/**
	* 經辦<br>
	* 起帳/銷帳
  *
  * @param titaTlrNo 經辦
	*/
  public void setTitaTlrNo(String titaTlrNo) {
    this.titaTlrNo = titaTlrNo;
  }

/**
	* 交易序號<br>
	* 起帳/銷帳
	* @return Integer
	*/
  public int getTitaTxtNo() {
    return this.titaTxtNo;
  }

/**
	* 交易序號<br>
	* 起帳/銷帳
  *
  * @param titaTxtNo 交易序號
	*/
  public void setTitaTxtNo(int titaTxtNo) {
    this.titaTxtNo = titaTxtNo;
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
	* 建檔日期<br>
	* 
	* @return java.sql.Timestamp
	*/
  public java.sql.Timestamp getCreateDate() {
    return this.createDate;
  }

/**
	* 建檔日期<br>
	* 
  *
  * @param createDate 建檔日期
	*/
  public void setCreateDate(java.sql.Timestamp createDate) {
    this.createDate = createDate;
  }

/**
	* 最後維護人員<br>
	* 
	* @return String
	*/
  public String getLastUpdateEmpNo() {
    return this.lastUpdateEmpNo == null ? "" : this.lastUpdateEmpNo;
  }

/**
	* 最後維護人員<br>
	* 
  *
  * @param lastUpdateEmpNo 最後維護人員
	*/
  public void setLastUpdateEmpNo(String lastUpdateEmpNo) {
    this.lastUpdateEmpNo = lastUpdateEmpNo;
  }

/**
	* 最後維護日期<br>
	* 
	* @return java.sql.Timestamp
	*/
  public java.sql.Timestamp getLastUpdate() {
    return this.lastUpdate;
  }

/**
	* 最後維護日期<br>
	* 
  *
  * @param lastUpdate 最後維護日期
	*/
  public void setLastUpdate(java.sql.Timestamp lastUpdate) {
    this.lastUpdate = lastUpdate;
  }


  @Override
  public String toString() {
    return "AcReceivable [acReceivableId=" + acReceivableId + ", acNoCode=" + acNoCode + ", acSubCode=" + acSubCode
           + ", acDtlCode=" + acDtlCode + ", branchNo=" + branchNo + ", currencyCode=" + currencyCode + ", clsFlag=" + clsFlag + ", acctFlag=" + acctFlag + ", receivableFlag=" + receivableFlag
           + ", rvAmt=" + rvAmt + ", rvBal=" + rvBal + ", acBal=" + acBal + ", slipNote=" + slipNote + ", acBookCode=" + acBookCode + ", acSubBookCode=" + acSubBookCode
           + ", openAcDate=" + openAcDate + ", openTxCd=" + openTxCd + ", openKinBr=" + openKinBr + ", openTlrNo=" + openTlrNo + ", openTxtNo=" + openTxtNo + ", lastAcDate=" + lastAcDate
           + ", lastTxDate=" + lastTxDate + ", titaTxCd=" + titaTxCd + ", titaKinBr=" + titaKinBr + ", titaTlrNo=" + titaTlrNo + ", titaTxtNo=" + titaTxtNo + ", jsonFields=" + jsonFields
           + ", createEmpNo=" + createEmpNo + ", createDate=" + createDate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + ", lastUpdate=" + lastUpdate + "]";
  }
}
