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
 * NegMain 債務協商案件主檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`NegMain`")
public class NegMain implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -3354707088890972407L;

@EmbeddedId
  private NegMainId negMainId;

  // 戶號
  /* 保貸戶須建立客戶主檔 */
  @Column(name = "`CustNo`", insertable = false, updatable = false)
  private int custNo = 0;

  // 案件序號
  @Column(name = "`CaseSeq`", insertable = false, updatable = false)
  private int caseSeq = 0;

  // 案件種類
  /* 共用代碼檔(CdCode.CaseKindCode)1:債協2:調解3:更生4:清算 */
  @Column(name = "`CaseKindCode`", length = 1)
  private String caseKindCode;

  // 債權戶況
  /* 共用代碼檔(CdCode.NegStatus)0:正常1:已變更2:毀諾3:結案4:未生效 */
  @Column(name = "`Status`", length = 1)
  private String status;

  // 債權戶別
  /* 共用代碼檔(CdCode.CustLoanKind)1:放款戶2:保貸戶3:保證人 */
  @Column(name = "`CustLoanKind`", length = 1)
  private String custLoanKind;

  // 付款人戶號
  /* 債權戶別為[保證人]才需要輸入依據問題單754保證人戶號債協還款時新壽分配款應存入此借款人戶號 */
  @Column(name = "`PayerCustNo`")
  private int payerCustNo = 0;

  // 延期繳款年月(起)
  @Column(name = "`DeferYMStart`")
  private int deferYMStart = 0;

  // 延期繳款年月(訖)
  @Column(name = "`DeferYMEnd`")
  private int deferYMEnd = 0;

  // 協商申請日
  @Column(name = "`ApplDate`")
  private int applDate = 0;

  // 月付金(期款)
  @Column(name = "`DueAmt`")
  private BigDecimal dueAmt = new BigDecimal("0");

  // 期數
  @Column(name = "`TotalPeriod`")
  private int totalPeriod = 0;

  // 計息條件(利率)
  @Column(name = "`IntRate`")
  private BigDecimal intRate = new BigDecimal("0");

  // 首次應繳日
  @Column(name = "`FirstDueDate`")
  private int firstDueDate = 0;

  // 還款結束日
  @Column(name = "`LastDueDate`")
  private int lastDueDate = 0;

  // 是否最大債權
  /* Y:是N:否 */
  @Column(name = "`IsMainFin`", length = 1)
  private String isMainFin;

  // 簽約總金額
  /* 本金 */
  @Column(name = "`TotalContrAmt`")
  private BigDecimal totalContrAmt = new BigDecimal("0");

  // 最大債權機構
  @Column(name = "`MainFinCode`", length = 8)
  private String mainFinCode;

  // 總本金餘額
  /* 一開始=TotalContrAmt */
  @Column(name = "`PrincipalBal`")
  private BigDecimal principalBal = new BigDecimal("0");

  // 累繳金額
  /* 結清時須減掉退還金額 */
  @Column(name = "`AccuTempAmt`")
  private BigDecimal accuTempAmt = new BigDecimal("0");

  // 累溢繳金額
  /* 結清時須減掉退還金額 */
  @Column(name = "`AccuOverAmt`")
  private BigDecimal accuOverAmt = new BigDecimal("0");

  // 累應還金額
  /* 繳款時更新,結清時等於累繳金額,其他等於期款乘以(首次應繳日至會計日之月差) */
  @Column(name = "`AccuDueAmt`")
  private BigDecimal accuDueAmt = new BigDecimal("0");

  // 累新壽分攤金額
  @Column(name = "`AccuSklShareAmt`")
  private BigDecimal accuSklShareAmt = new BigDecimal("0");

  // 已繳期數
  @Column(name = "`RepaidPeriod`")
  private int repaidPeriod = 0;

  // 二階段註記
  /* YN若有N階段還款的情況,此區會存入大於0之自然數I代表第I階段還款 */
  @Column(name = "`TwoStepCode`", length = 1)
  private String twoStepCode;

  // 申請變更還款條件日
  @Column(name = "`ChgCondDate`")
  private int chgCondDate = 0;

  // 下次應繳日
  /* 下次哪天該繳錢 */
  @Column(name = "`NextPayDate`")
  private int nextPayDate = 0;

  // 繳息迄日
  /* 這次已繳到哪一天了 */
  @Column(name = "`PayIntDate`")
  private int payIntDate = 0;

  // 償還本金
  /* 最後一次的還本本金,非累計 */
  @Column(name = "`RepayPrincipal`")
  private BigDecimal repayPrincipal = new BigDecimal("0");

  // 償還利息
  /* 最後一次的還本利息,非累計 */
  @Column(name = "`RepayInterest`")
  private BigDecimal repayInterest = new BigDecimal("0");

  // 戶況日期
  @Column(name = "`StatusDate`")
  private int statusDate = 0;

  // 受理調解機構代號
  /* 調解案件必須輸入,JCIC申報使用 */
  @Column(name = "`CourtCode`", length = 3)
  private String courtCode;

  // 本次會計日期
  /* NegTrans */
  @Column(name = "`ThisAcDate`")
  private int thisAcDate = 0;

  // 本次經辦
  /* NegTrans */
  @Column(name = "`ThisTitaTlrNo`", length = 6)
  private String thisTitaTlrNo;

  // 本次交易序號
  /* NegTrans */
  @Column(name = "`ThisTitaTxtNo`")
  private int thisTitaTxtNo = 0;

  // 上次會計日期
  /* NegTrans */
  @Column(name = "`LastAcDate`")
  private int lastAcDate = 0;

  // 上次經辦
  /* NegTrans */
  @Column(name = "`LastTitaTlrNo`", length = 6)
  private String lastTitaTlrNo;

  // 上次交易序號
  /* NegTrans */
  @Column(name = "`LastTitaTxtNo`")
  private int lastTitaTxtNo = 0;

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


  public NegMainId getNegMainId() {
    return this.negMainId;
  }

  public void setNegMainId(NegMainId negMainId) {
    this.negMainId = negMainId;
  }

/**
	* 戶號<br>
	* 保貸戶須建立客戶主檔
	* @return Integer
	*/
  public int getCustNo() {
    return this.custNo;
  }

/**
	* 戶號<br>
	* 保貸戶須建立客戶主檔
  *
  * @param custNo 戶號
	*/
  public void setCustNo(int custNo) {
    this.custNo = custNo;
  }

/**
	* 案件序號<br>
	* 
	* @return Integer
	*/
  public int getCaseSeq() {
    return this.caseSeq;
  }

/**
	* 案件序號<br>
	* 
  *
  * @param caseSeq 案件序號
	*/
  public void setCaseSeq(int caseSeq) {
    this.caseSeq = caseSeq;
  }

/**
	* 案件種類<br>
	* 共用代碼檔(CdCode.CaseKindCode)
1:債協
2:調解
3:更生
4:清算
	* @return String
	*/
  public String getCaseKindCode() {
    return this.caseKindCode == null ? "" : this.caseKindCode;
  }

/**
	* 案件種類<br>
	* 共用代碼檔(CdCode.CaseKindCode)
1:債協
2:調解
3:更生
4:清算
  *
  * @param caseKindCode 案件種類
	*/
  public void setCaseKindCode(String caseKindCode) {
    this.caseKindCode = caseKindCode;
  }

/**
	* 債權戶況<br>
	* 共用代碼檔(CdCode.NegStatus)
0:正常
1:已變更
2:毀諾
3:結案
4:未生效
	* @return String
	*/
  public String getStatus() {
    return this.status == null ? "" : this.status;
  }

/**
	* 債權戶況<br>
	* 共用代碼檔(CdCode.NegStatus)
0:正常
1:已變更
2:毀諾
3:結案
4:未生效
  *
  * @param status 債權戶況
	*/
  public void setStatus(String status) {
    this.status = status;
  }

/**
	* 債權戶別<br>
	* 共用代碼檔(CdCode.CustLoanKind)
1:放款戶
2:保貸戶
3:保證人
	* @return String
	*/
  public String getCustLoanKind() {
    return this.custLoanKind == null ? "" : this.custLoanKind;
  }

/**
	* 債權戶別<br>
	* 共用代碼檔(CdCode.CustLoanKind)
1:放款戶
2:保貸戶
3:保證人
  *
  * @param custLoanKind 債權戶別
	*/
  public void setCustLoanKind(String custLoanKind) {
    this.custLoanKind = custLoanKind;
  }

/**
	* 付款人戶號<br>
	* 債權戶別為[保證人]才需要輸入
依據問題單754
保證人戶號債協還款時新壽分配款應存入此借款人戶號
	* @return Integer
	*/
  public int getPayerCustNo() {
    return this.payerCustNo;
  }

/**
	* 付款人戶號<br>
	* 債權戶別為[保證人]才需要輸入
依據問題單754
保證人戶號債協還款時新壽分配款應存入此借款人戶號
  *
  * @param payerCustNo 付款人戶號
	*/
  public void setPayerCustNo(int payerCustNo) {
    this.payerCustNo = payerCustNo;
  }

/**
	* 延期繳款年月(起)<br>
	* 
	* @return Integer
	*/
  public int getDeferYMStart() {
    return this.deferYMStart;
  }

/**
	* 延期繳款年月(起)<br>
	* 
  *
  * @param deferYMStart 延期繳款年月(起)
	*/
  public void setDeferYMStart(int deferYMStart) {
    this.deferYMStart = deferYMStart;
  }

/**
	* 延期繳款年月(訖)<br>
	* 
	* @return Integer
	*/
  public int getDeferYMEnd() {
    return this.deferYMEnd;
  }

/**
	* 延期繳款年月(訖)<br>
	* 
  *
  * @param deferYMEnd 延期繳款年月(訖)
	*/
  public void setDeferYMEnd(int deferYMEnd) {
    this.deferYMEnd = deferYMEnd;
  }

/**
	* 協商申請日<br>
	* 
	* @return Integer
	*/
  public int getApplDate() {
    return StaticTool.bcToRoc(this.applDate);
  }

/**
	* 協商申請日<br>
	* 
  *
  * @param applDate 協商申請日
  * @throws LogicException when Date Is Warn	*/
  public void setApplDate(int applDate) throws LogicException {
    this.applDate = StaticTool.rocToBc(applDate);
  }

/**
	* 月付金(期款)<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getDueAmt() {
    return this.dueAmt;
  }

/**
	* 月付金(期款)<br>
	* 
  *
  * @param dueAmt 月付金(期款)
	*/
  public void setDueAmt(BigDecimal dueAmt) {
    this.dueAmt = dueAmt;
  }

/**
	* 期數<br>
	* 
	* @return Integer
	*/
  public int getTotalPeriod() {
    return this.totalPeriod;
  }

/**
	* 期數<br>
	* 
  *
  * @param totalPeriod 期數
	*/
  public void setTotalPeriod(int totalPeriod) {
    this.totalPeriod = totalPeriod;
  }

/**
	* 計息條件(利率)<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getIntRate() {
    return this.intRate;
  }

/**
	* 計息條件(利率)<br>
	* 
  *
  * @param intRate 計息條件(利率)
	*/
  public void setIntRate(BigDecimal intRate) {
    this.intRate = intRate;
  }

/**
	* 首次應繳日<br>
	* 
	* @return Integer
	*/
  public int getFirstDueDate() {
    return StaticTool.bcToRoc(this.firstDueDate);
  }

/**
	* 首次應繳日<br>
	* 
  *
  * @param firstDueDate 首次應繳日
  * @throws LogicException when Date Is Warn	*/
  public void setFirstDueDate(int firstDueDate) throws LogicException {
    this.firstDueDate = StaticTool.rocToBc(firstDueDate);
  }

/**
	* 還款結束日<br>
	* 
	* @return Integer
	*/
  public int getLastDueDate() {
    return StaticTool.bcToRoc(this.lastDueDate);
  }

/**
	* 還款結束日<br>
	* 
  *
  * @param lastDueDate 還款結束日
  * @throws LogicException when Date Is Warn	*/
  public void setLastDueDate(int lastDueDate) throws LogicException {
    this.lastDueDate = StaticTool.rocToBc(lastDueDate);
  }

/**
	* 是否最大債權<br>
	* Y:是
N:否
	* @return String
	*/
  public String getIsMainFin() {
    return this.isMainFin == null ? "" : this.isMainFin;
  }

/**
	* 是否最大債權<br>
	* Y:是
N:否
  *
  * @param isMainFin 是否最大債權
	*/
  public void setIsMainFin(String isMainFin) {
    this.isMainFin = isMainFin;
  }

/**
	* 簽約總金額<br>
	* 本金
	* @return BigDecimal
	*/
  public BigDecimal getTotalContrAmt() {
    return this.totalContrAmt;
  }

/**
	* 簽約總金額<br>
	* 本金
  *
  * @param totalContrAmt 簽約總金額
	*/
  public void setTotalContrAmt(BigDecimal totalContrAmt) {
    this.totalContrAmt = totalContrAmt;
  }

/**
	* 最大債權機構<br>
	* 
	* @return String
	*/
  public String getMainFinCode() {
    return this.mainFinCode == null ? "" : this.mainFinCode;
  }

/**
	* 最大債權機構<br>
	* 
  *
  * @param mainFinCode 最大債權機構
	*/
  public void setMainFinCode(String mainFinCode) {
    this.mainFinCode = mainFinCode;
  }

/**
	* 總本金餘額<br>
	* 一開始=TotalContrAmt
	* @return BigDecimal
	*/
  public BigDecimal getPrincipalBal() {
    return this.principalBal;
  }

/**
	* 總本金餘額<br>
	* 一開始=TotalContrAmt
  *
  * @param principalBal 總本金餘額
	*/
  public void setPrincipalBal(BigDecimal principalBal) {
    this.principalBal = principalBal;
  }

/**
	* 累繳金額<br>
	* 結清時須減掉退還金額
	* @return BigDecimal
	*/
  public BigDecimal getAccuTempAmt() {
    return this.accuTempAmt;
  }

/**
	* 累繳金額<br>
	* 結清時須減掉退還金額
  *
  * @param accuTempAmt 累繳金額
	*/
  public void setAccuTempAmt(BigDecimal accuTempAmt) {
    this.accuTempAmt = accuTempAmt;
  }

/**
	* 累溢繳金額<br>
	* 結清時須減掉退還金額
	* @return BigDecimal
	*/
  public BigDecimal getAccuOverAmt() {
    return this.accuOverAmt;
  }

/**
	* 累溢繳金額<br>
	* 結清時須減掉退還金額
  *
  * @param accuOverAmt 累溢繳金額
	*/
  public void setAccuOverAmt(BigDecimal accuOverAmt) {
    this.accuOverAmt = accuOverAmt;
  }

/**
	* 累應還金額<br>
	* 繳款時更新,結清時等於累繳金額,其他等於期款乘以(首次應繳日至會計日之月差)
	* @return BigDecimal
	*/
  public BigDecimal getAccuDueAmt() {
    return this.accuDueAmt;
  }

/**
	* 累應還金額<br>
	* 繳款時更新,結清時等於累繳金額,其他等於期款乘以(首次應繳日至會計日之月差)
  *
  * @param accuDueAmt 累應還金額
	*/
  public void setAccuDueAmt(BigDecimal accuDueAmt) {
    this.accuDueAmt = accuDueAmt;
  }

/**
	* 累新壽分攤金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getAccuSklShareAmt() {
    return this.accuSklShareAmt;
  }

/**
	* 累新壽分攤金額<br>
	* 
  *
  * @param accuSklShareAmt 累新壽分攤金額
	*/
  public void setAccuSklShareAmt(BigDecimal accuSklShareAmt) {
    this.accuSklShareAmt = accuSklShareAmt;
  }

/**
	* 已繳期數<br>
	* 
	* @return Integer
	*/
  public int getRepaidPeriod() {
    return this.repaidPeriod;
  }

/**
	* 已繳期數<br>
	* 
  *
  * @param repaidPeriod 已繳期數
	*/
  public void setRepaidPeriod(int repaidPeriod) {
    this.repaidPeriod = repaidPeriod;
  }

/**
	* 二階段註記<br>
	* Y
N
若有N階段還款的情況,此區會存入大於0之自然數I代表第I階段還款
	* @return String
	*/
  public String getTwoStepCode() {
    return this.twoStepCode == null ? "" : this.twoStepCode;
  }

/**
	* 二階段註記<br>
	* Y
N
若有N階段還款的情況,此區會存入大於0之自然數I代表第I階段還款
  *
  * @param twoStepCode 二階段註記
	*/
  public void setTwoStepCode(String twoStepCode) {
    this.twoStepCode = twoStepCode;
  }

/**
	* 申請變更還款條件日<br>
	* 
	* @return Integer
	*/
  public int getChgCondDate() {
    return StaticTool.bcToRoc(this.chgCondDate);
  }

/**
	* 申請變更還款條件日<br>
	* 
  *
  * @param chgCondDate 申請變更還款條件日
  * @throws LogicException when Date Is Warn	*/
  public void setChgCondDate(int chgCondDate) throws LogicException {
    this.chgCondDate = StaticTool.rocToBc(chgCondDate);
  }

/**
	* 下次應繳日<br>
	* 下次哪天該繳錢
	* @return Integer
	*/
  public int getNextPayDate() {
    return StaticTool.bcToRoc(this.nextPayDate);
  }

/**
	* 下次應繳日<br>
	* 下次哪天該繳錢
  *
  * @param nextPayDate 下次應繳日
  * @throws LogicException when Date Is Warn	*/
  public void setNextPayDate(int nextPayDate) throws LogicException {
    this.nextPayDate = StaticTool.rocToBc(nextPayDate);
  }

/**
	* 繳息迄日<br>
	* 這次已繳到哪一天了
	* @return Integer
	*/
  public int getPayIntDate() {
    return StaticTool.bcToRoc(this.payIntDate);
  }

/**
	* 繳息迄日<br>
	* 這次已繳到哪一天了
  *
  * @param payIntDate 繳息迄日
  * @throws LogicException when Date Is Warn	*/
  public void setPayIntDate(int payIntDate) throws LogicException {
    this.payIntDate = StaticTool.rocToBc(payIntDate);
  }

/**
	* 償還本金<br>
	* 最後一次的還本本金,非累計
	* @return BigDecimal
	*/
  public BigDecimal getRepayPrincipal() {
    return this.repayPrincipal;
  }

/**
	* 償還本金<br>
	* 最後一次的還本本金,非累計
  *
  * @param repayPrincipal 償還本金
	*/
  public void setRepayPrincipal(BigDecimal repayPrincipal) {
    this.repayPrincipal = repayPrincipal;
  }

/**
	* 償還利息<br>
	* 最後一次的還本利息,非累計
	* @return BigDecimal
	*/
  public BigDecimal getRepayInterest() {
    return this.repayInterest;
  }

/**
	* 償還利息<br>
	* 最後一次的還本利息,非累計
  *
  * @param repayInterest 償還利息
	*/
  public void setRepayInterest(BigDecimal repayInterest) {
    this.repayInterest = repayInterest;
  }

/**
	* 戶況日期<br>
	* 
	* @return Integer
	*/
  public int getStatusDate() {
    return StaticTool.bcToRoc(this.statusDate);
  }

/**
	* 戶況日期<br>
	* 
  *
  * @param statusDate 戶況日期
  * @throws LogicException when Date Is Warn	*/
  public void setStatusDate(int statusDate) throws LogicException {
    this.statusDate = StaticTool.rocToBc(statusDate);
  }

/**
	* 受理調解機構代號<br>
	* 調解案件必須輸入,JCIC申報使用
	* @return String
	*/
  public String getCourtCode() {
    return this.courtCode == null ? "" : this.courtCode;
  }

/**
	* 受理調解機構代號<br>
	* 調解案件必須輸入,JCIC申報使用
  *
  * @param courtCode 受理調解機構代號
	*/
  public void setCourtCode(String courtCode) {
    this.courtCode = courtCode;
  }

/**
	* 本次會計日期<br>
	* NegTrans
	* @return Integer
	*/
  public int getThisAcDate() {
    return StaticTool.bcToRoc(this.thisAcDate);
  }

/**
	* 本次會計日期<br>
	* NegTrans
  *
  * @param thisAcDate 本次會計日期
  * @throws LogicException when Date Is Warn	*/
  public void setThisAcDate(int thisAcDate) throws LogicException {
    this.thisAcDate = StaticTool.rocToBc(thisAcDate);
  }

/**
	* 本次經辦<br>
	* NegTrans
	* @return String
	*/
  public String getThisTitaTlrNo() {
    return this.thisTitaTlrNo == null ? "" : this.thisTitaTlrNo;
  }

/**
	* 本次經辦<br>
	* NegTrans
  *
  * @param thisTitaTlrNo 本次經辦
	*/
  public void setThisTitaTlrNo(String thisTitaTlrNo) {
    this.thisTitaTlrNo = thisTitaTlrNo;
  }

/**
	* 本次交易序號<br>
	* NegTrans
	* @return Integer
	*/
  public int getThisTitaTxtNo() {
    return this.thisTitaTxtNo;
  }

/**
	* 本次交易序號<br>
	* NegTrans
  *
  * @param thisTitaTxtNo 本次交易序號
	*/
  public void setThisTitaTxtNo(int thisTitaTxtNo) {
    this.thisTitaTxtNo = thisTitaTxtNo;
  }

/**
	* 上次會計日期<br>
	* NegTrans
	* @return Integer
	*/
  public int getLastAcDate() {
    return StaticTool.bcToRoc(this.lastAcDate);
  }

/**
	* 上次會計日期<br>
	* NegTrans
  *
  * @param lastAcDate 上次會計日期
  * @throws LogicException when Date Is Warn	*/
  public void setLastAcDate(int lastAcDate) throws LogicException {
    this.lastAcDate = StaticTool.rocToBc(lastAcDate);
  }

/**
	* 上次經辦<br>
	* NegTrans
	* @return String
	*/
  public String getLastTitaTlrNo() {
    return this.lastTitaTlrNo == null ? "" : this.lastTitaTlrNo;
  }

/**
	* 上次經辦<br>
	* NegTrans
  *
  * @param lastTitaTlrNo 上次經辦
	*/
  public void setLastTitaTlrNo(String lastTitaTlrNo) {
    this.lastTitaTlrNo = lastTitaTlrNo;
  }

/**
	* 上次交易序號<br>
	* NegTrans
	* @return Integer
	*/
  public int getLastTitaTxtNo() {
    return this.lastTitaTxtNo;
  }

/**
	* 上次交易序號<br>
	* NegTrans
  *
  * @param lastTitaTxtNo 上次交易序號
	*/
  public void setLastTitaTxtNo(int lastTitaTxtNo) {
    this.lastTitaTxtNo = lastTitaTxtNo;
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
    return "NegMain [negMainId=" + negMainId + ", caseKindCode=" + caseKindCode + ", status=" + status + ", custLoanKind=" + custLoanKind + ", payerCustNo=" + payerCustNo
           + ", deferYMStart=" + deferYMStart + ", deferYMEnd=" + deferYMEnd + ", applDate=" + applDate + ", dueAmt=" + dueAmt + ", totalPeriod=" + totalPeriod + ", intRate=" + intRate
           + ", firstDueDate=" + firstDueDate + ", lastDueDate=" + lastDueDate + ", isMainFin=" + isMainFin + ", totalContrAmt=" + totalContrAmt + ", mainFinCode=" + mainFinCode + ", principalBal=" + principalBal
           + ", accuTempAmt=" + accuTempAmt + ", accuOverAmt=" + accuOverAmt + ", accuDueAmt=" + accuDueAmt + ", accuSklShareAmt=" + accuSklShareAmt + ", repaidPeriod=" + repaidPeriod + ", twoStepCode=" + twoStepCode
           + ", chgCondDate=" + chgCondDate + ", nextPayDate=" + nextPayDate + ", payIntDate=" + payIntDate + ", repayPrincipal=" + repayPrincipal + ", repayInterest=" + repayInterest + ", statusDate=" + statusDate
           + ", courtCode=" + courtCode + ", thisAcDate=" + thisAcDate + ", thisTitaTlrNo=" + thisTitaTlrNo + ", thisTitaTxtNo=" + thisTitaTxtNo + ", lastAcDate=" + lastAcDate + ", lastTitaTlrNo=" + lastTitaTlrNo
           + ", lastTitaTxtNo=" + lastTitaTxtNo + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
