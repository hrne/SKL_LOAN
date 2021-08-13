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
 * AcDetail 會計帳務明細檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`AcDetail`")
public class AcDetail implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 2074956841333392659L;

@EmbeddedId
  private AcDetailId acDetailId;

  // 登放日期
  @Column(name = "`RelDy`", insertable = false, updatable = false)
  private int relDy = 0;

  // 登放序號
  /* 單位別(4)+經辦(6)+交易序號(8) */
  @Column(name = "`RelTxseq`", length = 18, insertable = false, updatable = false)
  private String relTxseq;

  // 分錄序號
  @Column(name = "`AcSeq`", insertable = false, updatable = false)
  private int acSeq = 0;

  // 會計日期
  @Column(name = "`AcDate`")
  private int acDate = 0;

  // 單位別
  @Column(name = "`BranchNo`", length = 4)
  private String branchNo;

  // 幣別
  @Column(name = "`CurrencyCode`", length = 3)
  private String currencyCode;

  // 科目代號
  /* CdAcCode會計科子細目設定檔 */
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

  // 業務科目代號
  /* CdAcCode會計科子細目設定檔 */
  @Column(name = "`AcctCode`", length = 3)
  private String acctCode;

  // 借貸別
  /* D-借，C-貸 */
  @Column(name = "`DbCr`", length = 1)
  private String dbCr;

  // 記帳金額
  @Column(name = "`TxAmt`")
  private BigDecimal txAmt = new BigDecimal("0");

  // 入總帳記號
  /* 0:未入帳 1:已入帳  2:被沖正(隔日訂正) 3.沖正(隔日訂正) */
  @Column(name = "`EntAc`")
  private int entAc = 0;

  // 戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 額度編號
  @Column(name = "`FacmNo`")
  private int facmNo = 0;

  // 撥款序號
  @Column(name = "`BormNo`")
  private int bormNo = 0;

  // 銷帳編號
  /* 銷帳科目記號=1時，由會計銷帳檔處理公用程式自動編號；其他由業務自行編號 */
  @Column(name = "`RvNo`", length = 30)
  private String rvNo;

  // 業務科目記號
  /* 0: 非業務科目1: 資負明細科目（放款、催收款項..) */
  @Column(name = "`AcctFlag`")
  private int acctFlag = 0;

  // 銷帳科目記號
  /* 0－非銷帳科目(會計科子細目設定檔帶入)1－會計銷帳科目(會計科子細目設定檔帶入)2－業務銷帳科目(會計科子細目設定檔帶入)3－未收費用、4-短繳期金、5-另收欠款(銷帳檔帶入)8－核心銷帳碼科目，須以銷帳編號(銷帳碼彙總)上傳核心(會計科子細目設定檔帶入) */
  @Column(name = "`ReceivableFlag`")
  private int receivableFlag = 0;

  // 帳冊別記號
  /* CdAcCode會計科子細目設定檔帶入(0,1,2)0: 不細分，區隔帳冊固定為00A:傳統帳冊1: 細分，區隔帳冊By戶號設定(AcBookCom)2: 中介，應收調撥款科目   ※明細檔無(只寫入總帳檔)，應收調撥款之核心傳票媒體檔，係自動產生。3: 指定帳冊，區隔帳冊由業務交易指定   ※L6201:其他傳票輸入、L618D各項提存 */
  @Column(name = "`AcBookFlag`")
  private int acBookFlag = 0;

  // 帳冊別
  /* 系統參數設定檔帶入000：全公司 */
  @Column(name = "`AcBookCode`", length = 3)
  private String acBookCode;

  // 區隔帳冊
  /* 00A:傳統帳冊;201:利變年金帳冊帳冊別記號=0時，不須處理帳冊別記號=1時，由帳冊別處理公用程式設定帳冊別記號=3時，由業務交易帶入 */
  @Column(name = "`AcSubBookCode`", length = 3)
  private String acSubBookCode;

  // 彙總別
  /* 撥還共用(0XX)／還款來源(1xx)／撥款方式(2xx)090.暫收抵繳091:借新還舊092:暫收轉帳093:抽退票094:轉債協暫收款095:轉債協退還款101.匯款轉帳102.銀行扣款103.員工扣款104.支票兌現105.法院扣薪106.理賠金107.代收款-債權協商109.其他111.匯款轉帳預先作業201:整批匯款202:單筆匯款204:退款台新(存款憑條)205:退款他行(整批匯款)211:退款新光(存款憑條) */
  @Column(name = "`SumNo`", length = 3)
  private String sumNo;

  // 摘要代號
  @Column(name = "`DscptCode`", length = 4)
  private String dscptCode;

  // 傳票摘要
  @Column(name = "`SlipNote`", length = 80)
  private String slipNote;

  // 傳票批號
  /* 01~10-關帳11-支票繳款90~99 提存帳務，不會更新AcMain總帳檔99-利息提存 98-迴轉上月96-放款承諾 97-迴轉上月95-未付火險費提存 94-迴轉上月 */
  @Column(name = "`SlipBatNo`")
  private int slipBatNo = 0;

  // 傳票號碼
  @Column(name = "`SlipNo`")
  private int slipNo = 0;

  // 登錄單位別
  @Column(name = "`TitaKinbr`", length = 4)
  private String titaKinbr;

  // 登錄經辦
  @Column(name = "`TitaTlrNo`", length = 6)
  private String titaTlrNo;

  // 登錄交易序號
  @Column(name = "`TitaTxtNo`")
  private int titaTxtNo = 0;

  // 交易代號
  @Column(name = "`TitaTxCd`", length = 5)
  private String titaTxCd;

  // 業務類別
  @Column(name = "`TitaSecNo`", length = 2)
  private String titaSecNo;

  // 整批批號
  @Column(name = "`TitaBatchNo`", length = 6)
  private String titaBatchNo;

  // 整批明細序號
  @Column(name = "`TitaBatchSeq`", length = 6)
  private String titaBatchSeq;

  // 核准主管
  @Column(name = "`TitaSupNo`", length = 6)
  private String titaSupNo;

  // 作業模式
  /* 1一段式，2二段式，3三段式 */
  @Column(name = "`TitaRelCd`")
  private int titaRelCd = 0;

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


  public AcDetailId getAcDetailId() {
    return this.acDetailId;
  }

  public void setAcDetailId(AcDetailId acDetailId) {
    this.acDetailId = acDetailId;
  }

/**
	* 登放日期<br>
	* 
	* @return Integer
	*/
  public int getRelDy() {
    return StaticTool.bcToRoc(this.relDy);
  }

/**
	* 登放日期<br>
	* 
  *
  * @param relDy 登放日期
  * @throws LogicException when Date Is Warn	*/
  public void setRelDy(int relDy) throws LogicException {
    this.relDy = StaticTool.rocToBc(relDy);
  }

/**
	* 登放序號<br>
	* 單位別(4)+經辦(6)+交易序號(8)
	* @return String
	*/
  public String getRelTxseq() {
    return this.relTxseq == null ? "" : this.relTxseq;
  }

/**
	* 登放序號<br>
	* 單位別(4)+經辦(6)+交易序號(8)
  *
  * @param relTxseq 登放序號
	*/
  public void setRelTxseq(String relTxseq) {
    this.relTxseq = relTxseq;
  }

/**
	* 分錄序號<br>
	* 
	* @return Integer
	*/
  public int getAcSeq() {
    return this.acSeq;
  }

/**
	* 分錄序號<br>
	* 
  *
  * @param acSeq 分錄序號
	*/
  public void setAcSeq(int acSeq) {
    this.acSeq = acSeq;
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
	* 科目代號<br>
	* CdAcCode會計科子細目設定檔
	* @return String
	*/
  public String getAcNoCode() {
    return this.acNoCode == null ? "" : this.acNoCode;
  }

/**
	* 科目代號<br>
	* CdAcCode會計科子細目設定檔
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
	* 借貸別<br>
	* D-借，C-貸
	* @return String
	*/
  public String getDbCr() {
    return this.dbCr == null ? "" : this.dbCr;
  }

/**
	* 借貸別<br>
	* D-借，C-貸
  *
  * @param dbCr 借貸別
	*/
  public void setDbCr(String dbCr) {
    this.dbCr = dbCr;
  }

/**
	* 記帳金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getTxAmt() {
    return this.txAmt;
  }

/**
	* 記帳金額<br>
	* 
  *
  * @param txAmt 記帳金額
	*/
  public void setTxAmt(BigDecimal txAmt) {
    this.txAmt = txAmt;
  }

/**
	* 入總帳記號<br>
	* 0:未入帳 1:已入帳  2:被沖正(隔日訂正) 3.沖正(隔日訂正)
	* @return Integer
	*/
  public int getEntAc() {
    return this.entAc;
  }

/**
	* 入總帳記號<br>
	* 0:未入帳 1:已入帳  2:被沖正(隔日訂正) 3.沖正(隔日訂正)
  *
  * @param entAc 入總帳記號
	*/
  public void setEntAc(int entAc) {
    this.entAc = entAc;
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
	* 撥款序號<br>
	* 
	* @return Integer
	*/
  public int getBormNo() {
    return this.bormNo;
  }

/**
	* 撥款序號<br>
	* 
  *
  * @param bormNo 撥款序號
	*/
  public void setBormNo(int bormNo) {
    this.bormNo = bormNo;
  }

/**
	* 銷帳編號<br>
	* 銷帳科目記號=1時，由會計銷帳檔處理公用程式自動編號；其他由業務自行編號
	* @return String
	*/
  public String getRvNo() {
    return this.rvNo == null ? "" : this.rvNo;
  }

/**
	* 銷帳編號<br>
	* 銷帳科目記號=1時，由會計銷帳檔處理公用程式自動編號；其他由業務自行編號
  *
  * @param rvNo 銷帳編號
	*/
  public void setRvNo(String rvNo) {
    this.rvNo = rvNo;
  }

/**
	* 業務科目記號<br>
	* 0: 非業務科目
1: 資負明細科目（放款、催收款項..)
	* @return Integer
	*/
  public int getAcctFlag() {
    return this.acctFlag;
  }

/**
	* 業務科目記號<br>
	* 0: 非業務科目
1: 資負明細科目（放款、催收款項..)
  *
  * @param acctFlag 業務科目記號
	*/
  public void setAcctFlag(int acctFlag) {
    this.acctFlag = acctFlag;
  }

/**
	* 銷帳科目記號<br>
	* 0－非銷帳科目(會計科子細目設定檔帶入)
1－會計銷帳科目(會計科子細目設定檔帶入)
2－業務銷帳科目(會計科子細目設定檔帶入)
3－未收費用、4-短繳期金、5-另收欠款(銷帳檔帶入)
8－核心銷帳碼科目，須以銷帳編號(銷帳碼彙總)上傳核心(會計科子細目設定檔帶入)
	* @return Integer
	*/
  public int getReceivableFlag() {
    return this.receivableFlag;
  }

/**
	* 銷帳科目記號<br>
	* 0－非銷帳科目(會計科子細目設定檔帶入)
1－會計銷帳科目(會計科子細目設定檔帶入)
2－業務銷帳科目(會計科子細目設定檔帶入)
3－未收費用、4-短繳期金、5-另收欠款(銷帳檔帶入)
8－核心銷帳碼科目，須以銷帳編號(銷帳碼彙總)上傳核心(會計科子細目設定檔帶入)
  *
  * @param receivableFlag 銷帳科目記號
	*/
  public void setReceivableFlag(int receivableFlag) {
    this.receivableFlag = receivableFlag;
  }

/**
	* 帳冊別記號<br>
	* CdAcCode會計科子細目設定檔帶入(0,1,2)
0: 不細分，區隔帳冊固定為00A:傳統帳冊
1: 細分，區隔帳冊By戶號設定(AcBookCom)
2: 中介，應收調撥款科目
   ※明細檔無(只寫入總帳檔)，應收調撥款之核心傳票媒體檔，係自動產生。
3: 指定帳冊，區隔帳冊由業務交易指定
   ※L6201:其他傳票輸入、L618D各項提存
	* @return Integer
	*/
  public int getAcBookFlag() {
    return this.acBookFlag;
  }

/**
	* 帳冊別記號<br>
	* CdAcCode會計科子細目設定檔帶入(0,1,2)
0: 不細分，區隔帳冊固定為00A:傳統帳冊
1: 細分，區隔帳冊By戶號設定(AcBookCom)
2: 中介，應收調撥款科目
   ※明細檔無(只寫入總帳檔)，應收調撥款之核心傳票媒體檔，係自動產生。
3: 指定帳冊，區隔帳冊由業務交易指定
   ※L6201:其他傳票輸入、L618D各項提存
  *
  * @param acBookFlag 帳冊別記號
	*/
  public void setAcBookFlag(int acBookFlag) {
    this.acBookFlag = acBookFlag;
  }

/**
	* 帳冊別<br>
	* 系統參數設定檔帶入
000：全公司
	* @return String
	*/
  public String getAcBookCode() {
    return this.acBookCode == null ? "" : this.acBookCode;
  }

/**
	* 帳冊別<br>
	* 系統參數設定檔帶入
000：全公司
  *
  * @param acBookCode 帳冊別
	*/
  public void setAcBookCode(String acBookCode) {
    this.acBookCode = acBookCode;
  }

/**
	* 區隔帳冊<br>
	* 00A:傳統帳冊;201:利變年金帳冊
帳冊別記號=0時，不須處理
帳冊別記號=1時，由帳冊別處理公用程式設定
帳冊別記號=3時，由業務交易帶入
	* @return String
	*/
  public String getAcSubBookCode() {
    return this.acSubBookCode == null ? "" : this.acSubBookCode;
  }

/**
	* 區隔帳冊<br>
	* 00A:傳統帳冊;201:利變年金帳冊
帳冊別記號=0時，不須處理
帳冊別記號=1時，由帳冊別處理公用程式設定
帳冊別記號=3時，由業務交易帶入
  *
  * @param acSubBookCode 區隔帳冊
	*/
  public void setAcSubBookCode(String acSubBookCode) {
    this.acSubBookCode = acSubBookCode;
  }

/**
	* 彙總別<br>
	* 撥還共用(0XX)／還款來源(1xx)／撥款方式(2xx)
090.暫收抵繳
091:借新還舊
092:暫收轉帳
093:抽退票
094:轉債協暫收款
095:轉債協退還款
101.匯款轉帳
102.銀行扣款
103.員工扣款
104.支票兌現
105.法院扣薪
106.理賠金
107.代收款-債權協商
109.其他
111.匯款轉帳預先作業
201:整批匯款
202:單筆匯款
204:退款台新(存款憑條)
205:退款他行(整批匯款)
211:退款新光(存款憑條)
	* @return String
	*/
  public String getSumNo() {
    return this.sumNo == null ? "" : this.sumNo;
  }

/**
	* 彙總別<br>
	* 撥還共用(0XX)／還款來源(1xx)／撥款方式(2xx)
090.暫收抵繳
091:借新還舊
092:暫收轉帳
093:抽退票
094:轉債協暫收款
095:轉債協退還款
101.匯款轉帳
102.銀行扣款
103.員工扣款
104.支票兌現
105.法院扣薪
106.理賠金
107.代收款-債權協商
109.其他
111.匯款轉帳預先作業
201:整批匯款
202:單筆匯款
204:退款台新(存款憑條)
205:退款他行(整批匯款)
211:退款新光(存款憑條)
  *
  * @param sumNo 彙總別
	*/
  public void setSumNo(String sumNo) {
    this.sumNo = sumNo;
  }

/**
	* 摘要代號<br>
	* 
	* @return String
	*/
  public String getDscptCode() {
    return this.dscptCode == null ? "" : this.dscptCode;
  }

/**
	* 摘要代號<br>
	* 
  *
  * @param dscptCode 摘要代號
	*/
  public void setDscptCode(String dscptCode) {
    this.dscptCode = dscptCode;
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
	* 傳票批號<br>
	* 01~10-關帳
11-支票繳款
90~99 提存帳務，不會更新AcMain總帳檔
99-利息提存 98-迴轉上月
96-放款承諾 97-迴轉上月
95-未付火險費提存 94-迴轉上月
	* @return Integer
	*/
  public int getSlipBatNo() {
    return this.slipBatNo;
  }

/**
	* 傳票批號<br>
	* 01~10-關帳
11-支票繳款
90~99 提存帳務，不會更新AcMain總帳檔
99-利息提存 98-迴轉上月
96-放款承諾 97-迴轉上月
95-未付火險費提存 94-迴轉上月
  *
  * @param slipBatNo 傳票批號
	*/
  public void setSlipBatNo(int slipBatNo) {
    this.slipBatNo = slipBatNo;
  }

/**
	* 傳票號碼<br>
	* 
	* @return Integer
	*/
  public int getSlipNo() {
    return this.slipNo;
  }

/**
	* 傳票號碼<br>
	* 
  *
  * @param slipNo 傳票號碼
	*/
  public void setSlipNo(int slipNo) {
    this.slipNo = slipNo;
  }

/**
	* 登錄單位別<br>
	* 
	* @return String
	*/
  public String getTitaKinbr() {
    return this.titaKinbr == null ? "" : this.titaKinbr;
  }

/**
	* 登錄單位別<br>
	* 
  *
  * @param titaKinbr 登錄單位別
	*/
  public void setTitaKinbr(String titaKinbr) {
    this.titaKinbr = titaKinbr;
  }

/**
	* 登錄經辦<br>
	* 
	* @return String
	*/
  public String getTitaTlrNo() {
    return this.titaTlrNo == null ? "" : this.titaTlrNo;
  }

/**
	* 登錄經辦<br>
	* 
  *
  * @param titaTlrNo 登錄經辦
	*/
  public void setTitaTlrNo(String titaTlrNo) {
    this.titaTlrNo = titaTlrNo;
  }

/**
	* 登錄交易序號<br>
	* 
	* @return Integer
	*/
  public int getTitaTxtNo() {
    return this.titaTxtNo;
  }

/**
	* 登錄交易序號<br>
	* 
  *
  * @param titaTxtNo 登錄交易序號
	*/
  public void setTitaTxtNo(int titaTxtNo) {
    this.titaTxtNo = titaTxtNo;
  }

/**
	* 交易代號<br>
	* 
	* @return String
	*/
  public String getTitaTxCd() {
    return this.titaTxCd == null ? "" : this.titaTxCd;
  }

/**
	* 交易代號<br>
	* 
  *
  * @param titaTxCd 交易代號
	*/
  public void setTitaTxCd(String titaTxCd) {
    this.titaTxCd = titaTxCd;
  }

/**
	* 業務類別<br>
	* 
	* @return String
	*/
  public String getTitaSecNo() {
    return this.titaSecNo == null ? "" : this.titaSecNo;
  }

/**
	* 業務類別<br>
	* 
  *
  * @param titaSecNo 業務類別
	*/
  public void setTitaSecNo(String titaSecNo) {
    this.titaSecNo = titaSecNo;
  }

/**
	* 整批批號<br>
	* 
	* @return String
	*/
  public String getTitaBatchNo() {
    return this.titaBatchNo == null ? "" : this.titaBatchNo;
  }

/**
	* 整批批號<br>
	* 
  *
  * @param titaBatchNo 整批批號
	*/
  public void setTitaBatchNo(String titaBatchNo) {
    this.titaBatchNo = titaBatchNo;
  }

/**
	* 整批明細序號<br>
	* 
	* @return String
	*/
  public String getTitaBatchSeq() {
    return this.titaBatchSeq == null ? "" : this.titaBatchSeq;
  }

/**
	* 整批明細序號<br>
	* 
  *
  * @param titaBatchSeq 整批明細序號
	*/
  public void setTitaBatchSeq(String titaBatchSeq) {
    this.titaBatchSeq = titaBatchSeq;
  }

/**
	* 核准主管<br>
	* 
	* @return String
	*/
  public String getTitaSupNo() {
    return this.titaSupNo == null ? "" : this.titaSupNo;
  }

/**
	* 核准主管<br>
	* 
  *
  * @param titaSupNo 核准主管
	*/
  public void setTitaSupNo(String titaSupNo) {
    this.titaSupNo = titaSupNo;
  }

/**
	* 作業模式<br>
	* 1一段式，2二段式，3三段式
	* @return Integer
	*/
  public int getTitaRelCd() {
    return this.titaRelCd;
  }

/**
	* 作業模式<br>
	* 1一段式，2二段式，3三段式
  *
  * @param titaRelCd 作業模式
	*/
  public void setTitaRelCd(int titaRelCd) {
    this.titaRelCd = titaRelCd;
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
    return "AcDetail [acDetailId=" + acDetailId + ", acDate=" + acDate + ", branchNo=" + branchNo + ", currencyCode=" + currencyCode
           + ", acNoCode=" + acNoCode + ", acSubCode=" + acSubCode + ", acDtlCode=" + acDtlCode + ", acctCode=" + acctCode + ", dbCr=" + dbCr + ", txAmt=" + txAmt
           + ", entAc=" + entAc + ", custNo=" + custNo + ", facmNo=" + facmNo + ", bormNo=" + bormNo + ", rvNo=" + rvNo + ", acctFlag=" + acctFlag
           + ", receivableFlag=" + receivableFlag + ", acBookFlag=" + acBookFlag + ", acBookCode=" + acBookCode + ", acSubBookCode=" + acSubBookCode + ", sumNo=" + sumNo + ", dscptCode=" + dscptCode
           + ", slipNote=" + slipNote + ", slipBatNo=" + slipBatNo + ", slipNo=" + slipNo + ", titaKinbr=" + titaKinbr + ", titaTlrNo=" + titaTlrNo + ", titaTxtNo=" + titaTxtNo
           + ", titaTxCd=" + titaTxCd + ", titaSecNo=" + titaSecNo + ", titaBatchNo=" + titaBatchNo + ", titaBatchSeq=" + titaBatchSeq + ", titaSupNo=" + titaSupNo + ", titaRelCd=" + titaRelCd
           + ", jsonFields=" + jsonFields + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
