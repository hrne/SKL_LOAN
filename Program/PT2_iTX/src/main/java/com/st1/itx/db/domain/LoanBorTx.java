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
 * LoanBorTx 放款交易內容檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`LoanBorTx`")
public class LoanBorTx implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 9119226293997969828L;

@EmbeddedId
  private LoanBorTxId loanBorTxId;

  // 借款人戶號
  @Column(name = "`CustNo`", insertable = false, updatable = false)
  private int custNo = 0;

  // 額度編號
  /* 999;聯貸訂約案 */
  @Column(name = "`FacmNo`", insertable = false, updatable = false)
  private int facmNo = 0;

  // 撥款序號
  @Column(name = "`BormNo`", insertable = false, updatable = false)
  private int bormNo = 0;

  // 交易內容檔序號
  @Column(name = "`BorxNo`", insertable = false, updatable = false)
  private int borxNo = 0;

  // 交易日期
  @Column(name = "`TitaCalDy`")
  private int titaCalDy = 0;

  // 交易時間
  @Column(name = "`TitaCalTm`")
  private int titaCalTm = 0;

  // 單位別
  @Column(name = "`TitaKinBr`", length = 4)
  private String titaKinBr;

  // 經辦
  @Column(name = "`TitaTlrNo`", length = 6)
  private String titaTlrNo;

  // 交易序號
  @Column(name = "`TitaTxtNo`", length = 8)
  private String titaTxtNo;

  // 交易代號
  @Column(name = "`TitaTxCd`", length = 5)
  private String titaTxCd;

  // 借貸別
  /* (空白):無1:借2:貸 */
  @Column(name = "`TitaCrDb`", length = 1)
  private String titaCrDb;

  // 訂正別
  /* 0:正常1:訂正2:被訂正3:沖正4:被沖正 */
  @Column(name = "`TitaHCode`", length = 1)
  private String titaHCode;

  // 幣別
  @Column(name = "`TitaCurCd`", length = 3)
  private String titaCurCd;

  // 主管編號
  @Column(name = "`TitaEmpNoS`", length = 6)
  private String titaEmpNoS;

  // 還款來源
  /* CdCode.RepayCode01:匯款轉帳02:銀行扣款03:員工扣薪04:支票05:特約金06:人事特約金07:定存特約08:劃撥存款09:其他 */
  @Column(name = "`RepayCode`")
  private int repayCode = 0;

  // 摘要
  @Column(name = "`Desc`", length = 15)
  private String desc;

  // 會計日期
  @Column(name = "`AcDate`")
  private int acDate = 0;

  // 更正序號, 原交易序號
  /* 會計日期(8)+單位別(4)+經辦(6)+交易序號(8) */
  @Column(name = "`CorrectSeq`", length = 26)
  private String correctSeq;

  // 查詢時顯示否
  /* F:繳息、帳務I:繳息A:帳務 Y:是N:否 */
  @Column(name = "`Displayflag`", length = 1)
  private String displayflag;

  // 入帳日期
  @Column(name = "`EntryDate`")
  private int entryDate = 0;

  // 應繳日期
  @Column(name = "`DueDate`")
  private int dueDate = 0;

  // 交易金額
  /* 首筆 */
  @Column(name = "`TxAmt`")
  private BigDecimal txAmt = new BigDecimal("0");

  // 放款餘額
  @Column(name = "`LoanBal`")
  private BigDecimal loanBal = new BigDecimal("0");

  // 計息起日
  @Column(name = "`IntStartDate`")
  private int intStartDate = 0;

  // 計息迄日
  @Column(name = "`IntEndDate`")
  private int intEndDate = 0;

  // 回收期數
  @Column(name = "`PaidTerms`")
  private int paidTerms = 0;

  // 利率
  @Column(name = "`Rate`")
  private BigDecimal rate = new BigDecimal("0");

  // 實收本金
  /* 扣除短收本金、含收回欠繳本金催收結案時：沖催收款項+利息收入=實收本金+實收利息 */
  @Column(name = "`Principal`")
  private BigDecimal principal = new BigDecimal("0");

  // 實收利息
  /* 扣除減免、扣除短收利息、含收回欠繳利息 */
  @Column(name = "`Interest`")
  private BigDecimal interest = new BigDecimal("0");

  // 實收延滯息
  /* 已扣除減免 */
  @Column(name = "`DelayInt`")
  private BigDecimal delayInt = new BigDecimal("0");

  // 實收違約金
  /* 已扣除減免 */
  @Column(name = "`BreachAmt`")
  private BigDecimal breachAmt = new BigDecimal("0");

  // 實收清償違約金
  /* 已扣除減免，實收立即收取違約金 */
  @Column(name = "`CloseBreachAmt`")
  private BigDecimal closeBreachAmt = new BigDecimal("0");

  // 暫收款金額
  /* 存入暫收為正、暫收抵繳為負 */
  @Column(name = "`TempAmt`")
  private BigDecimal tempAmt = new BigDecimal("0");

  // 提前償還本金
  /* 實收本金含提前償還本金 */
  @Column(name = "`ExtraRepay`")
  private BigDecimal extraRepay = new BigDecimal("0");

  // 短繳利息
  @Column(name = "`UnpaidInterest`")
  private BigDecimal unpaidInterest = new BigDecimal("0");

  // 短繳本金
  @Column(name = "`UnpaidPrincipal`")
  private BigDecimal unpaidPrincipal = new BigDecimal("0");

  // 短繳清償違約金
  /* 即時收取清償違約金，下期期款收取 */
  @Column(name = "`UnpaidCloseBreach`")
  private BigDecimal unpaidCloseBreach = new BigDecimal("0");

  // 短收金額
  /* 首筆 */
  @Column(name = "`Shortfall`")
  private BigDecimal shortfall = new BigDecimal("0");

  // 溢收金額
  /* 首筆 */
  @Column(name = "`Overflow`")
  private BigDecimal overflow = new BigDecimal("0");

  // 其他欄位
  /* 1.減免的金額     2.收取的短繳本金、利息          3.收取的各項費用(首筆) */
  @Column(name = "`OtherFields`", length = 2000)
  private String otherFields;

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


  public LoanBorTxId getLoanBorTxId() {
    return this.loanBorTxId;
  }

  public void setLoanBorTxId(LoanBorTxId loanBorTxId) {
    this.loanBorTxId = loanBorTxId;
  }

/**
	* 借款人戶號<br>
	* 
	* @return Integer
	*/
  public int getCustNo() {
    return this.custNo;
  }

/**
	* 借款人戶號<br>
	* 
  *
  * @param custNo 借款人戶號
	*/
  public void setCustNo(int custNo) {
    this.custNo = custNo;
  }

/**
	* 額度編號<br>
	* 999;聯貸訂約案
	* @return Integer
	*/
  public int getFacmNo() {
    return this.facmNo;
  }

/**
	* 額度編號<br>
	* 999;聯貸訂約案
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
	* 交易內容檔序號<br>
	* 
	* @return Integer
	*/
  public int getBorxNo() {
    return this.borxNo;
  }

/**
	* 交易內容檔序號<br>
	* 
  *
  * @param borxNo 交易內容檔序號
	*/
  public void setBorxNo(int borxNo) {
    this.borxNo = borxNo;
  }

/**
	* 交易日期<br>
	* 
	* @return Integer
	*/
  public int getTitaCalDy() {
    return StaticTool.bcToRoc(this.titaCalDy);
  }

/**
	* 交易日期<br>
	* 
  *
  * @param titaCalDy 交易日期
  * @throws LogicException when Date Is Warn	*/
  public void setTitaCalDy(int titaCalDy) throws LogicException {
    this.titaCalDy = StaticTool.rocToBc(titaCalDy);
  }

/**
	* 交易時間<br>
	* 
	* @return Integer
	*/
  public int getTitaCalTm() {
    return this.titaCalTm;
  }

/**
	* 交易時間<br>
	* 
  *
  * @param titaCalTm 交易時間
	*/
  public void setTitaCalTm(int titaCalTm) {
    this.titaCalTm = titaCalTm;
  }

/**
	* 單位別<br>
	* 
	* @return String
	*/
  public String getTitaKinBr() {
    return this.titaKinBr == null ? "" : this.titaKinBr;
  }

/**
	* 單位別<br>
	* 
  *
  * @param titaKinBr 單位別
	*/
  public void setTitaKinBr(String titaKinBr) {
    this.titaKinBr = titaKinBr;
  }

/**
	* 經辦<br>
	* 
	* @return String
	*/
  public String getTitaTlrNo() {
    return this.titaTlrNo == null ? "" : this.titaTlrNo;
  }

/**
	* 經辦<br>
	* 
  *
  * @param titaTlrNo 經辦
	*/
  public void setTitaTlrNo(String titaTlrNo) {
    this.titaTlrNo = titaTlrNo;
  }

/**
	* 交易序號<br>
	* 
	* @return String
	*/
  public String getTitaTxtNo() {
    return this.titaTxtNo == null ? "" : this.titaTxtNo;
  }

/**
	* 交易序號<br>
	* 
  *
  * @param titaTxtNo 交易序號
	*/
  public void setTitaTxtNo(String titaTxtNo) {
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
	* 借貸別<br>
	* (空白):無
1:借
2:貸
	* @return String
	*/
  public String getTitaCrDb() {
    return this.titaCrDb == null ? "" : this.titaCrDb;
  }

/**
	* 借貸別<br>
	* (空白):無
1:借
2:貸
  *
  * @param titaCrDb 借貸別
	*/
  public void setTitaCrDb(String titaCrDb) {
    this.titaCrDb = titaCrDb;
  }

/**
	* 訂正別<br>
	* 0:正常
1:訂正
2:被訂正
3:沖正
4:被沖正
	* @return String
	*/
  public String getTitaHCode() {
    return this.titaHCode == null ? "" : this.titaHCode;
  }

/**
	* 訂正別<br>
	* 0:正常
1:訂正
2:被訂正
3:沖正
4:被沖正
  *
  * @param titaHCode 訂正別
	*/
  public void setTitaHCode(String titaHCode) {
    this.titaHCode = titaHCode;
  }

/**
	* 幣別<br>
	* 
	* @return String
	*/
  public String getTitaCurCd() {
    return this.titaCurCd == null ? "" : this.titaCurCd;
  }

/**
	* 幣別<br>
	* 
  *
  * @param titaCurCd 幣別
	*/
  public void setTitaCurCd(String titaCurCd) {
    this.titaCurCd = titaCurCd;
  }

/**
	* 主管編號<br>
	* 
	* @return String
	*/
  public String getTitaEmpNoS() {
    return this.titaEmpNoS == null ? "" : this.titaEmpNoS;
  }

/**
	* 主管編號<br>
	* 
  *
  * @param titaEmpNoS 主管編號
	*/
  public void setTitaEmpNoS(String titaEmpNoS) {
    this.titaEmpNoS = titaEmpNoS;
  }

/**
	* 還款來源<br>
	* CdCode.RepayCode
01:匯款轉帳
02:銀行扣款
03:員工扣薪
04:支票
05:特約金
06:人事特約金
07:定存特約
08:劃撥存款
09:其他
	* @return Integer
	*/
  public int getRepayCode() {
    return this.repayCode;
  }

/**
	* 還款來源<br>
	* CdCode.RepayCode
01:匯款轉帳
02:銀行扣款
03:員工扣薪
04:支票
05:特約金
06:人事特約金
07:定存特約
08:劃撥存款
09:其他
  *
  * @param repayCode 還款來源
	*/
  public void setRepayCode(int repayCode) {
    this.repayCode = repayCode;
  }

/**
	* 摘要<br>
	* 
	* @return String
	*/
  public String getDesc() {
    return this.desc == null ? "" : this.desc;
  }

/**
	* 摘要<br>
	* 
  *
  * @param desc 摘要
	*/
  public void setDesc(String desc) {
    this.desc = desc;
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
	* 更正序號, 原交易序號<br>
	* 會計日期(8)+單位別(4)+經辦(6)+交易序號(8)
	* @return String
	*/
  public String getCorrectSeq() {
    return this.correctSeq == null ? "" : this.correctSeq;
  }

/**
	* 更正序號, 原交易序號<br>
	* 會計日期(8)+單位別(4)+經辦(6)+交易序號(8)
  *
  * @param correctSeq 更正序號, 原交易序號
	*/
  public void setCorrectSeq(String correctSeq) {
    this.correctSeq = correctSeq;
  }

/**
	* 查詢時顯示否<br>
	* F:繳息、帳務
I:繳息
A:帳務 
Y:是
N:否
	* @return String
	*/
  public String getDisplayflag() {
    return this.displayflag == null ? "" : this.displayflag;
  }

/**
	* 查詢時顯示否<br>
	* F:繳息、帳務
I:繳息
A:帳務 
Y:是
N:否
  *
  * @param displayflag 查詢時顯示否
	*/
  public void setDisplayflag(String displayflag) {
    this.displayflag = displayflag;
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
	* 應繳日期<br>
	* 
	* @return Integer
	*/
  public int getDueDate() {
    return StaticTool.bcToRoc(this.dueDate);
  }

/**
	* 應繳日期<br>
	* 
  *
  * @param dueDate 應繳日期
  * @throws LogicException when Date Is Warn	*/
  public void setDueDate(int dueDate) throws LogicException {
    this.dueDate = StaticTool.rocToBc(dueDate);
  }

/**
	* 交易金額<br>
	* 首筆
	* @return BigDecimal
	*/
  public BigDecimal getTxAmt() {
    return this.txAmt;
  }

/**
	* 交易金額<br>
	* 首筆
  *
  * @param txAmt 交易金額
	*/
  public void setTxAmt(BigDecimal txAmt) {
    this.txAmt = txAmt;
  }

/**
	* 放款餘額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLoanBal() {
    return this.loanBal;
  }

/**
	* 放款餘額<br>
	* 
  *
  * @param loanBal 放款餘額
	*/
  public void setLoanBal(BigDecimal loanBal) {
    this.loanBal = loanBal;
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
	* 回收期數<br>
	* 
	* @return Integer
	*/
  public int getPaidTerms() {
    return this.paidTerms;
  }

/**
	* 回收期數<br>
	* 
  *
  * @param paidTerms 回收期數
	*/
  public void setPaidTerms(int paidTerms) {
    this.paidTerms = paidTerms;
  }

/**
	* 利率<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getRate() {
    return this.rate;
  }

/**
	* 利率<br>
	* 
  *
  * @param rate 利率
	*/
  public void setRate(BigDecimal rate) {
    this.rate = rate;
  }

/**
	* 實收本金<br>
	* 扣除短收本金、含收回欠繳本金
催收結案時：沖催收款項+利息收入=實收本金+實收利息
	* @return BigDecimal
	*/
  public BigDecimal getPrincipal() {
    return this.principal;
  }

/**
	* 實收本金<br>
	* 扣除短收本金、含收回欠繳本金
催收結案時：沖催收款項+利息收入=實收本金+實收利息
  *
  * @param principal 實收本金
	*/
  public void setPrincipal(BigDecimal principal) {
    this.principal = principal;
  }

/**
	* 實收利息<br>
	* 扣除減免、扣除短收利息、含收回欠繳利息
	* @return BigDecimal
	*/
  public BigDecimal getInterest() {
    return this.interest;
  }

/**
	* 實收利息<br>
	* 扣除減免、扣除短收利息、含收回欠繳利息
  *
  * @param interest 實收利息
	*/
  public void setInterest(BigDecimal interest) {
    this.interest = interest;
  }

/**
	* 實收延滯息<br>
	* 已扣除減免
	* @return BigDecimal
	*/
  public BigDecimal getDelayInt() {
    return this.delayInt;
  }

/**
	* 實收延滯息<br>
	* 已扣除減免
  *
  * @param delayInt 實收延滯息
	*/
  public void setDelayInt(BigDecimal delayInt) {
    this.delayInt = delayInt;
  }

/**
	* 實收違約金<br>
	* 已扣除減免
	* @return BigDecimal
	*/
  public BigDecimal getBreachAmt() {
    return this.breachAmt;
  }

/**
	* 實收違約金<br>
	* 已扣除減免
  *
  * @param breachAmt 實收違約金
	*/
  public void setBreachAmt(BigDecimal breachAmt) {
    this.breachAmt = breachAmt;
  }

/**
	* 實收清償違約金<br>
	* 已扣除減免，實收立即收取違約金
	* @return BigDecimal
	*/
  public BigDecimal getCloseBreachAmt() {
    return this.closeBreachAmt;
  }

/**
	* 實收清償違約金<br>
	* 已扣除減免，實收立即收取違約金
  *
  * @param closeBreachAmt 實收清償違約金
	*/
  public void setCloseBreachAmt(BigDecimal closeBreachAmt) {
    this.closeBreachAmt = closeBreachAmt;
  }

/**
	* 暫收款金額<br>
	* 存入暫收為正、暫收抵繳為負
	* @return BigDecimal
	*/
  public BigDecimal getTempAmt() {
    return this.tempAmt;
  }

/**
	* 暫收款金額<br>
	* 存入暫收為正、暫收抵繳為負
  *
  * @param tempAmt 暫收款金額
	*/
  public void setTempAmt(BigDecimal tempAmt) {
    this.tempAmt = tempAmt;
  }

/**
	* 提前償還本金<br>
	* 實收本金含提前償還本金
	* @return BigDecimal
	*/
  public BigDecimal getExtraRepay() {
    return this.extraRepay;
  }

/**
	* 提前償還本金<br>
	* 實收本金含提前償還本金
  *
  * @param extraRepay 提前償還本金
	*/
  public void setExtraRepay(BigDecimal extraRepay) {
    this.extraRepay = extraRepay;
  }

/**
	* 短繳利息<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getUnpaidInterest() {
    return this.unpaidInterest;
  }

/**
	* 短繳利息<br>
	* 
  *
  * @param unpaidInterest 短繳利息
	*/
  public void setUnpaidInterest(BigDecimal unpaidInterest) {
    this.unpaidInterest = unpaidInterest;
  }

/**
	* 短繳本金<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getUnpaidPrincipal() {
    return this.unpaidPrincipal;
  }

/**
	* 短繳本金<br>
	* 
  *
  * @param unpaidPrincipal 短繳本金
	*/
  public void setUnpaidPrincipal(BigDecimal unpaidPrincipal) {
    this.unpaidPrincipal = unpaidPrincipal;
  }

/**
	* 短繳清償違約金<br>
	* 即時收取清償違約金，下期期款收取
	* @return BigDecimal
	*/
  public BigDecimal getUnpaidCloseBreach() {
    return this.unpaidCloseBreach;
  }

/**
	* 短繳清償違約金<br>
	* 即時收取清償違約金，下期期款收取
  *
  * @param unpaidCloseBreach 短繳清償違約金
	*/
  public void setUnpaidCloseBreach(BigDecimal unpaidCloseBreach) {
    this.unpaidCloseBreach = unpaidCloseBreach;
  }

/**
	* 短收金額<br>
	* 首筆
	* @return BigDecimal
	*/
  public BigDecimal getShortfall() {
    return this.shortfall;
  }

/**
	* 短收金額<br>
	* 首筆
  *
  * @param shortfall 短收金額
	*/
  public void setShortfall(BigDecimal shortfall) {
    this.shortfall = shortfall;
  }

/**
	* 溢收金額<br>
	* 首筆
	* @return BigDecimal
	*/
  public BigDecimal getOverflow() {
    return this.overflow;
  }

/**
	* 溢收金額<br>
	* 首筆
  *
  * @param overflow 溢收金額
	*/
  public void setOverflow(BigDecimal overflow) {
    this.overflow = overflow;
  }

/**
	* 其他欄位<br>
	* 1.減免的金額
     2.收取的短繳本金、利息     
     3.收取的各項費用(首筆)
	* @return String
	*/
  public String getOtherFields() {
    return this.otherFields == null ? "" : this.otherFields;
  }

/**
	* 其他欄位<br>
	* 1.減免的金額
     2.收取的短繳本金、利息     
     3.收取的各項費用(首筆)
  *
  * @param otherFields 其他欄位
	*/
  public void setOtherFields(String otherFields) {
    this.otherFields = otherFields;
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
    return "LoanBorTx [loanBorTxId=" + loanBorTxId + ", titaCalDy=" + titaCalDy + ", titaCalTm=" + titaCalTm
           + ", titaKinBr=" + titaKinBr + ", titaTlrNo=" + titaTlrNo + ", titaTxtNo=" + titaTxtNo + ", titaTxCd=" + titaTxCd + ", titaCrDb=" + titaCrDb + ", titaHCode=" + titaHCode
           + ", titaCurCd=" + titaCurCd + ", titaEmpNoS=" + titaEmpNoS + ", repayCode=" + repayCode + ", desc=" + desc + ", acDate=" + acDate + ", correctSeq=" + correctSeq
           + ", displayflag=" + displayflag + ", entryDate=" + entryDate + ", dueDate=" + dueDate + ", txAmt=" + txAmt + ", loanBal=" + loanBal + ", intStartDate=" + intStartDate
           + ", intEndDate=" + intEndDate + ", paidTerms=" + paidTerms + ", rate=" + rate + ", principal=" + principal + ", interest=" + interest + ", delayInt=" + delayInt
           + ", breachAmt=" + breachAmt + ", closeBreachAmt=" + closeBreachAmt + ", tempAmt=" + tempAmt + ", extraRepay=" + extraRepay + ", unpaidInterest=" + unpaidInterest + ", unpaidPrincipal=" + unpaidPrincipal
           + ", unpaidCloseBreach=" + unpaidCloseBreach + ", shortfall=" + shortfall + ", overflow=" + overflow + ", otherFields=" + otherFields + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
           + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
