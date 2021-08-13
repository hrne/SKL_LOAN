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
 * FacMain 額度主檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`FacMain`")
public class FacMain implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -1603378644037804261L;

@EmbeddedId
  private FacMainId facMainId;

  // 借款人戶號
  @Column(name = "`CustNo`", insertable = false, updatable = false)
  private int custNo = 0;

  // 額度編號
  @Column(name = "`FacmNo`", insertable = false, updatable = false)
  private int facmNo = 0;

  // 已撥款序號
  @Column(name = "`LastBormNo`")
  private int lastBormNo = 0;

  // 已預約序號
  @Column(name = "`LastBormRvNo`")
  private int lastBormRvNo = 0;

  // 核准號碼
  /* 案件申請號碼 */
  @Column(name = "`ApplNo`")
  private int applNo = 0;

  // 案件編號
  /* 徵審系統案號(eLoan案件編號) */
  @Column(name = "`CreditSysNo`")
  private int creditSysNo = 0;

  // 商品代碼
  @Column(name = "`ProdNo`", length = 5)
  private String prodNo;

  // 指標利率代碼
  @Column(name = "`BaseRateCode`", length = 2)
  private String baseRateCode;

  // 加碼利率
  @Column(name = "`RateIncr`")
  private BigDecimal rateIncr = new BigDecimal("0");

  // 個別加碼
  @Column(name = "`IndividualIncr`")
  private BigDecimal individualIncr = new BigDecimal("0");

  // 核准利率
  @Column(name = "`ApproveRate`")
  private BigDecimal approveRate = new BigDecimal("0");

  // 年繳比重優惠加減碼
  @Column(name = "`AnnualIncr`")
  private BigDecimal annualIncr = new BigDecimal("0");

  // 提供EMAIL優惠減碼
  @Column(name = "`EmailIncr`")
  private BigDecimal emailIncr = new BigDecimal("0");

  // 寬限逾一年利率加碼
  @Column(name = "`GraceIncr`")
  private BigDecimal graceIncr = new BigDecimal("0");

  // 利率區分
  /* 共用代碼檔1: 機動 2: 固動 3: 定期機動 */
  @Column(name = "`RateCode`", length = 1)
  private String rateCode;

  // 首次利率調整週期
  @Column(name = "`FirstRateAdjFreq`")
  private int firstRateAdjFreq = 0;

  // 利率調整週期
  @Column(name = "`RateAdjFreq`")
  private int rateAdjFreq = 0;

  // 核准幣別
  @Column(name = "`CurrencyCode`", length = 3)
  private String currencyCode;

  // 核准額度
  @Column(name = "`LineAmt`")
  private BigDecimal lineAmt = new BigDecimal("0");

  // 貸出金額(放款餘額)
  @Column(name = "`UtilAmt`")
  private BigDecimal utilAmt = new BigDecimal("0");

  // 已動用額度餘額
  /* 循環動用還款時會減少,非循環動用還款時不會減少 */
  @Column(name = "`UtilBal`")
  private BigDecimal utilBal = new BigDecimal("0");

  // 核准科目
  /* 共用代碼檔310: 短期擔保放款 320: 中期擔保放款330: 長期擔保放款340: 三十年房貸 */
  @Column(name = "`AcctCode`", length = 3)
  private String acctCode;

  // 貸款期間年
  @Column(name = "`LoanTermYy`")
  private int loanTermYy = 0;

  // 貸款期間月
  @Column(name = "`LoanTermMm`")
  private int loanTermMm = 0;

  // 貸款期間日
  @Column(name = "`LoanTermDd`")
  private int loanTermDd = 0;

  // 初貸日
  @Column(name = "`FirstDrawdownDate`")
  private int firstDrawdownDate = 0;

  // 到期日
  /* 首筆撥款的到期日 */
  @Column(name = "`MaturityDate`")
  private int maturityDate = 0;

  // 計息方式
  /* 共用代碼檔1: 按日計息  2: 按月計息 */
  @Column(name = "`IntCalcCode`", length = 1)
  private String intCalcCode;

  // 攤還方式
  /* 共用代碼檔1.按月繳息(按期繳息到期還本)2.到期取息(到期繳息還本)3.本息平均法(期金)4.本金平均法5.按月撥款收息(逆向貸款) */
  @Column(name = "`AmortizedCode`", length = 1)
  private String amortizedCode;

  // 週期基準
  /* 1:日 2:月 3:週 */
  @Column(name = "`FreqBase`", length = 1)
  private String freqBase;

  // 繳息週期
  @Column(name = "`PayIntFreq`")
  private int payIntFreq = 0;

  // 還本週期
  @Column(name = "`RepayFreq`")
  private int repayFreq = 0;

  // 動支期限
  @Column(name = "`UtilDeadline`")
  private int utilDeadline = 0;

  // 寬限總月數
  @Column(name = "`GracePeriod`")
  private int gracePeriod = 0;

  // 帳管費
  @Column(name = "`AcctFee`")
  private BigDecimal acctFee = new BigDecimal("0");

  // 規定管制代碼
  /* 規定管制代碼 00:一般01:自然人第三戶(央行管制)02:自然人第三戶且為高價住宅(央行管制)03:自然人第四戶以上(央行管制)04:自然人第四戶以上且為高價住宅(央行管制)05:自然人購置高價住宅(央行管制)06:法人購置住宅第一戶(央行管制)07:法人購置住宅第二戶以上(央行管制)08:購地貸款(央行管制)09:餘屋貸款(央行管制)10:工業區閒置土地抵押貸款(央行管制)11:增貸管制戶(舊央行管制)12:自然人特定地區第2戶購屋貸款(舊央行管制)13:投資戶(內部規範) */
  @Column(name = "`RuleCode`", length = 2)
  private String ruleCode;

  // 攤還額異動碼
  /* 共用代碼檔0: 不變  1: 變 */
  @Column(name = "`ExtraRepayCode`", length = 1)
  private String extraRepayCode;

  // 客戶別
  /* 共用代碼檔00 一般01 員工02 首購03 關企公司04 關企員工05 保戶07 員工二親等09 新二階員工10 保貸戶 */
  @Column(name = "`CustTypeCode`", length = 2)
  private String custTypeCode;

  // 循環動用
  /* 共用代碼檔0: 非循環動用  1: 循環動用 */
  @Column(name = "`RecycleCode`", length = 1)
  private String recycleCode;

  // 循環動用期限
  @Column(name = "`RecycleDeadline`")
  private int recycleDeadline = 0;

  // 資金用途別
  /* 共用代碼檔01: 週轉金02: 購置不動產03: 營業用資產04: 固定資產05: 企業投資06: 購置動產09: 其他 */
  @Column(name = "`UsageCode`", length = 2)
  private String usageCode;

  // 案件隸屬單位
  /* 共用代碼檔0:非企金單位  1:企金推展課 */
  @Column(name = "`DepartmentCode`", length = 1)
  private String departmentCode;

  // 代繳所得稅
  /* Y:是  N:否 */
  @Column(name = "`IncomeTaxFlag`", length = 1)
  private String incomeTaxFlag;

  // 代償碼
  /* Y:是  N:否 */
  @Column(name = "`CompensateFlag`", length = 1)
  private String compensateFlag;

  // 不可撤銷
  /* Y:是  N:否 */
  @Column(name = "`IrrevocableFlag`", length = 1)
  private String irrevocableFlag;

  // 利率調整通知
  /* 共用代碼檔1: 電子郵件 2: 書面通知 3: 簡訊通知 */
  @Column(name = "`RateAdjNoticeCode`", length = 1)
  private String rateAdjNoticeCode;

  // 計件代碼
  /* A: 新貸件B: 其他額度C: 原額度D: 新增額度E: 展期1: 新貸件2: 其他額度3: 原額度4: 新增額度5: 展期件6: 六個月動支7: 服務件8: 特殊件9: 固特利契轉 */
  @Column(name = "`PieceCode`", length = 1)
  private String pieceCode;

  // 繳款方式
  /* 1: 匯款轉帳2: 銀行扣款3: 員工扣薪4: 支票5: 特約金6: 人事特約金7: 定存特約8: 劃撥存款 */
  @Column(name = "`RepayCode`")
  private int repayCode = 0;

  // 介紹人
  @Column(name = "`Introducer`", length = 6)
  private String introducer;

  // 區部
  @Column(name = "`District`", length = 6)
  private String district;

  // 火險服務
  @Column(name = "`FireOfficer`", length = 6)
  private String fireOfficer;

  // 估價
  @Column(name = "`Estimate`", length = 6)
  private String estimate;

  // 授信
  @Column(name = "`CreditOfficer`", length = 6)
  private String creditOfficer;

  // 放款業務專員
  /* 目前未用(原AS400為協辦人員) */
  @Column(name = "`LoanOfficer`", length = 6)
  private String loanOfficer;

  // 放款專員/房貸專員/企金人員
  @Column(name = "`BusinessOfficer`", length = 6)
  private String businessOfficer;

  // 核決主管
  @Column(name = "`Supervisor`", length = 6)
  private String supervisor;

  // 徵信
  @Column(name = "`InvestigateOfficer`", length = 6)
  private String investigateOfficer;

  // 估價覆核
  @Column(name = "`EstimateReview`", length = 6)
  private String estimateReview;

  // 協辦人
  /* AS400 放款業務專員 */
  @Column(name = "`Coorgnizer`", length = 6)
  private String coorgnizer;

  // 提前清償原因
  /* 共用代碼檔00:無01:買賣02:自行還清03:軍功教勞工貸款轉貸04:利率過高轉貸05:增貸不准轉貸06:額度內動支不准轉貸07:內部代償08:借新還舊09:其他10:買回11:綁約期還款 */
  @Column(name = "`AdvanceCloseCode`")
  private int advanceCloseCode = 0;

  // 信用評分
  @Column(name = "`CreditScore`")
  private int creditScore = 0;

  // 對保日期
  @Column(name = "`GuaranteeDate`")
  private int guaranteeDate = 0;

  // 合約編號
  @Column(name = "`ContractNo`", length = 10)
  private String contractNo;

  // 擔保品設定記號
  /* Y:是 N:否 */
  @Column(name = "`ColSetFlag`", length = 1)
  private String colSetFlag;

  // 交易進行記號
  /* 1STEP TX -&amp;gt; 0    (from eloan)2STEP TX -&amp;gt; 1 2 */
  @Column(name = "`ActFg`")
  private int actFg = 0;

  // 上次交易日
  /* 更正時, 檢查是否為最近一筆交易 */
  @Column(name = "`LastAcctDate`")
  private int lastAcctDate = 0;

  // 上次交易行別
  @Column(name = "`LastKinbr`", length = 4)
  private String lastKinbr;

  // 上次櫃員編號
  @Column(name = "`LastTlrNo`", length = 6)
  private String lastTlrNo;

  // 上次交易序號
  @Column(name = "`LastTxtNo`", length = 8)
  private String lastTxtNo;

  // 會計日期
  @Column(name = "`AcDate`")
  private int acDate = 0;

  // 是否已列印[撥款審核資料表]
  /* Y:是 N:否 */
  @Column(name = "`L9110Flag`", length = 1)
  private String l9110Flag;

  // 單位別
  @Column(name = "`BranchNo`", length = 4)
  private String branchNo;

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


  public FacMainId getFacMainId() {
    return this.facMainId;
  }

  public void setFacMainId(FacMainId facMainId) {
    this.facMainId = facMainId;
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
	* 已撥款序號<br>
	* 
	* @return Integer
	*/
  public int getLastBormNo() {
    return this.lastBormNo;
  }

/**
	* 已撥款序號<br>
	* 
  *
  * @param lastBormNo 已撥款序號
	*/
  public void setLastBormNo(int lastBormNo) {
    this.lastBormNo = lastBormNo;
  }

/**
	* 已預約序號<br>
	* 
	* @return Integer
	*/
  public int getLastBormRvNo() {
    return this.lastBormRvNo;
  }

/**
	* 已預約序號<br>
	* 
  *
  * @param lastBormRvNo 已預約序號
	*/
  public void setLastBormRvNo(int lastBormRvNo) {
    this.lastBormRvNo = lastBormRvNo;
  }

/**
	* 核准號碼<br>
	* 案件申請號碼
	* @return Integer
	*/
  public int getApplNo() {
    return this.applNo;
  }

/**
	* 核准號碼<br>
	* 案件申請號碼
  *
  * @param applNo 核准號碼
	*/
  public void setApplNo(int applNo) {
    this.applNo = applNo;
  }

/**
	* 案件編號<br>
	* 徵審系統案號(eLoan案件編號)
	* @return Integer
	*/
  public int getCreditSysNo() {
    return this.creditSysNo;
  }

/**
	* 案件編號<br>
	* 徵審系統案號(eLoan案件編號)
  *
  * @param creditSysNo 案件編號
	*/
  public void setCreditSysNo(int creditSysNo) {
    this.creditSysNo = creditSysNo;
  }

/**
	* 商品代碼<br>
	* 
	* @return String
	*/
  public String getProdNo() {
    return this.prodNo == null ? "" : this.prodNo;
  }

/**
	* 商品代碼<br>
	* 
  *
  * @param prodNo 商品代碼
	*/
  public void setProdNo(String prodNo) {
    this.prodNo = prodNo;
  }

/**
	* 指標利率代碼<br>
	* 
	* @return String
	*/
  public String getBaseRateCode() {
    return this.baseRateCode == null ? "" : this.baseRateCode;
  }

/**
	* 指標利率代碼<br>
	* 
  *
  * @param baseRateCode 指標利率代碼
	*/
  public void setBaseRateCode(String baseRateCode) {
    this.baseRateCode = baseRateCode;
  }

/**
	* 加碼利率<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getRateIncr() {
    return this.rateIncr;
  }

/**
	* 加碼利率<br>
	* 
  *
  * @param rateIncr 加碼利率
	*/
  public void setRateIncr(BigDecimal rateIncr) {
    this.rateIncr = rateIncr;
  }

/**
	* 個別加碼<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getIndividualIncr() {
    return this.individualIncr;
  }

/**
	* 個別加碼<br>
	* 
  *
  * @param individualIncr 個別加碼
	*/
  public void setIndividualIncr(BigDecimal individualIncr) {
    this.individualIncr = individualIncr;
  }

/**
	* 核准利率<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getApproveRate() {
    return this.approveRate;
  }

/**
	* 核准利率<br>
	* 
  *
  * @param approveRate 核准利率
	*/
  public void setApproveRate(BigDecimal approveRate) {
    this.approveRate = approveRate;
  }

/**
	* 年繳比重優惠加減碼<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getAnnualIncr() {
    return this.annualIncr;
  }

/**
	* 年繳比重優惠加減碼<br>
	* 
  *
  * @param annualIncr 年繳比重優惠加減碼
	*/
  public void setAnnualIncr(BigDecimal annualIncr) {
    this.annualIncr = annualIncr;
  }

/**
	* 提供EMAIL優惠減碼<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getEmailIncr() {
    return this.emailIncr;
  }

/**
	* 提供EMAIL優惠減碼<br>
	* 
  *
  * @param emailIncr 提供EMAIL優惠減碼
	*/
  public void setEmailIncr(BigDecimal emailIncr) {
    this.emailIncr = emailIncr;
  }

/**
	* 寬限逾一年利率加碼<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getGraceIncr() {
    return this.graceIncr;
  }

/**
	* 寬限逾一年利率加碼<br>
	* 
  *
  * @param graceIncr 寬限逾一年利率加碼
	*/
  public void setGraceIncr(BigDecimal graceIncr) {
    this.graceIncr = graceIncr;
  }

/**
	* 利率區分<br>
	* 共用代碼檔
1: 機動 
2: 固動 
3: 定期機動
	* @return String
	*/
  public String getRateCode() {
    return this.rateCode == null ? "" : this.rateCode;
  }

/**
	* 利率區分<br>
	* 共用代碼檔
1: 機動 
2: 固動 
3: 定期機動
  *
  * @param rateCode 利率區分
	*/
  public void setRateCode(String rateCode) {
    this.rateCode = rateCode;
  }

/**
	* 首次利率調整週期<br>
	* 
	* @return Integer
	*/
  public int getFirstRateAdjFreq() {
    return this.firstRateAdjFreq;
  }

/**
	* 首次利率調整週期<br>
	* 
  *
  * @param firstRateAdjFreq 首次利率調整週期
	*/
  public void setFirstRateAdjFreq(int firstRateAdjFreq) {
    this.firstRateAdjFreq = firstRateAdjFreq;
  }

/**
	* 利率調整週期<br>
	* 
	* @return Integer
	*/
  public int getRateAdjFreq() {
    return this.rateAdjFreq;
  }

/**
	* 利率調整週期<br>
	* 
  *
  * @param rateAdjFreq 利率調整週期
	*/
  public void setRateAdjFreq(int rateAdjFreq) {
    this.rateAdjFreq = rateAdjFreq;
  }

/**
	* 核准幣別<br>
	* 
	* @return String
	*/
  public String getCurrencyCode() {
    return this.currencyCode == null ? "" : this.currencyCode;
  }

/**
	* 核准幣別<br>
	* 
  *
  * @param currencyCode 核准幣別
	*/
  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
  }

/**
	* 核准額度<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLineAmt() {
    return this.lineAmt;
  }

/**
	* 核准額度<br>
	* 
  *
  * @param lineAmt 核准額度
	*/
  public void setLineAmt(BigDecimal lineAmt) {
    this.lineAmt = lineAmt;
  }

/**
	* 貸出金額(放款餘額)<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getUtilAmt() {
    return this.utilAmt;
  }

/**
	* 貸出金額(放款餘額)<br>
	* 
  *
  * @param utilAmt 貸出金額(放款餘額)
	*/
  public void setUtilAmt(BigDecimal utilAmt) {
    this.utilAmt = utilAmt;
  }

/**
	* 已動用額度餘額<br>
	* 循環動用還款時會減少,非循環動用還款時不會減少
	* @return BigDecimal
	*/
  public BigDecimal getUtilBal() {
    return this.utilBal;
  }

/**
	* 已動用額度餘額<br>
	* 循環動用還款時會減少,非循環動用還款時不會減少
  *
  * @param utilBal 已動用額度餘額
	*/
  public void setUtilBal(BigDecimal utilBal) {
    this.utilBal = utilBal;
  }

/**
	* 核准科目<br>
	* 共用代碼檔
310: 短期擔保放款 
320: 中期擔保放款
330: 長期擔保放款
340: 三十年房貸
	* @return String
	*/
  public String getAcctCode() {
    return this.acctCode == null ? "" : this.acctCode;
  }

/**
	* 核准科目<br>
	* 共用代碼檔
310: 短期擔保放款 
320: 中期擔保放款
330: 長期擔保放款
340: 三十年房貸
  *
  * @param acctCode 核准科目
	*/
  public void setAcctCode(String acctCode) {
    this.acctCode = acctCode;
  }

/**
	* 貸款期間年<br>
	* 
	* @return Integer
	*/
  public int getLoanTermYy() {
    return this.loanTermYy;
  }

/**
	* 貸款期間年<br>
	* 
  *
  * @param loanTermYy 貸款期間年
	*/
  public void setLoanTermYy(int loanTermYy) {
    this.loanTermYy = loanTermYy;
  }

/**
	* 貸款期間月<br>
	* 
	* @return Integer
	*/
  public int getLoanTermMm() {
    return this.loanTermMm;
  }

/**
	* 貸款期間月<br>
	* 
  *
  * @param loanTermMm 貸款期間月
	*/
  public void setLoanTermMm(int loanTermMm) {
    this.loanTermMm = loanTermMm;
  }

/**
	* 貸款期間日<br>
	* 
	* @return Integer
	*/
  public int getLoanTermDd() {
    return this.loanTermDd;
  }

/**
	* 貸款期間日<br>
	* 
  *
  * @param loanTermDd 貸款期間日
	*/
  public void setLoanTermDd(int loanTermDd) {
    this.loanTermDd = loanTermDd;
  }

/**
	* 初貸日<br>
	* 
	* @return Integer
	*/
  public int getFirstDrawdownDate() {
    return StaticTool.bcToRoc(this.firstDrawdownDate);
  }

/**
	* 初貸日<br>
	* 
  *
  * @param firstDrawdownDate 初貸日
  * @throws LogicException when Date Is Warn	*/
  public void setFirstDrawdownDate(int firstDrawdownDate) throws LogicException {
    this.firstDrawdownDate = StaticTool.rocToBc(firstDrawdownDate);
  }

/**
	* 到期日<br>
	* 首筆撥款的到期日
	* @return Integer
	*/
  public int getMaturityDate() {
    return StaticTool.bcToRoc(this.maturityDate);
  }

/**
	* 到期日<br>
	* 首筆撥款的到期日
  *
  * @param maturityDate 到期日
  * @throws LogicException when Date Is Warn	*/
  public void setMaturityDate(int maturityDate) throws LogicException {
    this.maturityDate = StaticTool.rocToBc(maturityDate);
  }

/**
	* 計息方式<br>
	* 共用代碼檔
1: 按日計息  
2: 按月計息
	* @return String
	*/
  public String getIntCalcCode() {
    return this.intCalcCode == null ? "" : this.intCalcCode;
  }

/**
	* 計息方式<br>
	* 共用代碼檔
1: 按日計息  
2: 按月計息
  *
  * @param intCalcCode 計息方式
	*/
  public void setIntCalcCode(String intCalcCode) {
    this.intCalcCode = intCalcCode;
  }

/**
	* 攤還方式<br>
	* 共用代碼檔
1.按月繳息(按期繳息到期還本)
2.到期取息(到期繳息還本)
3.本息平均法(期金)
4.本金平均法
5.按月撥款收息(逆向貸款)
	* @return String
	*/
  public String getAmortizedCode() {
    return this.amortizedCode == null ? "" : this.amortizedCode;
  }

/**
	* 攤還方式<br>
	* 共用代碼檔
1.按月繳息(按期繳息到期還本)
2.到期取息(到期繳息還本)
3.本息平均法(期金)
4.本金平均法
5.按月撥款收息(逆向貸款)
  *
  * @param amortizedCode 攤還方式
	*/
  public void setAmortizedCode(String amortizedCode) {
    this.amortizedCode = amortizedCode;
  }

/**
	* 週期基準<br>
	* 1:日 2:月 3:週
	* @return String
	*/
  public String getFreqBase() {
    return this.freqBase == null ? "" : this.freqBase;
  }

/**
	* 週期基準<br>
	* 1:日 2:月 3:週
  *
  * @param freqBase 週期基準
	*/
  public void setFreqBase(String freqBase) {
    this.freqBase = freqBase;
  }

/**
	* 繳息週期<br>
	* 
	* @return Integer
	*/
  public int getPayIntFreq() {
    return this.payIntFreq;
  }

/**
	* 繳息週期<br>
	* 
  *
  * @param payIntFreq 繳息週期
	*/
  public void setPayIntFreq(int payIntFreq) {
    this.payIntFreq = payIntFreq;
  }

/**
	* 還本週期<br>
	* 
	* @return Integer
	*/
  public int getRepayFreq() {
    return this.repayFreq;
  }

/**
	* 還本週期<br>
	* 
  *
  * @param repayFreq 還本週期
	*/
  public void setRepayFreq(int repayFreq) {
    this.repayFreq = repayFreq;
  }

/**
	* 動支期限<br>
	* 
	* @return Integer
	*/
  public int getUtilDeadline() {
    return StaticTool.bcToRoc(this.utilDeadline);
  }

/**
	* 動支期限<br>
	* 
  *
  * @param utilDeadline 動支期限
  * @throws LogicException when Date Is Warn	*/
  public void setUtilDeadline(int utilDeadline) throws LogicException {
    this.utilDeadline = StaticTool.rocToBc(utilDeadline);
  }

/**
	* 寬限總月數<br>
	* 
	* @return Integer
	*/
  public int getGracePeriod() {
    return this.gracePeriod;
  }

/**
	* 寬限總月數<br>
	* 
  *
  * @param gracePeriod 寬限總月數
	*/
  public void setGracePeriod(int gracePeriod) {
    this.gracePeriod = gracePeriod;
  }

/**
	* 帳管費<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getAcctFee() {
    return this.acctFee;
  }

/**
	* 帳管費<br>
	* 
  *
  * @param acctFee 帳管費
	*/
  public void setAcctFee(BigDecimal acctFee) {
    this.acctFee = acctFee;
  }

/**
	* 規定管制代碼<br>
	* 規定管制代碼 
00:一般
01:自然人第三戶(央行管制)
02:自然人第三戶且為高價住宅(央行管制)
03:自然人第四戶以上(央行管制)
04:自然人第四戶以上且為高價住宅(央行管制)
05:自然人購置高價住宅(央行管制)
06:法人購置住宅第一戶(央行管制)
07:法人購置住宅第二戶以上(央行管制)
08:購地貸款(央行管制)
09:餘屋貸款(央行管制)
10:工業區閒置土地抵押貸款(央行管制)
11:增貸管制戶(舊央行管制)
12:自然人特定地區第2戶購屋貸款(舊央行管制)
13:投資戶(內部規範)
	* @return String
	*/
  public String getRuleCode() {
    return this.ruleCode == null ? "" : this.ruleCode;
  }

/**
	* 規定管制代碼<br>
	* 規定管制代碼 
00:一般
01:自然人第三戶(央行管制)
02:自然人第三戶且為高價住宅(央行管制)
03:自然人第四戶以上(央行管制)
04:自然人第四戶以上且為高價住宅(央行管制)
05:自然人購置高價住宅(央行管制)
06:法人購置住宅第一戶(央行管制)
07:法人購置住宅第二戶以上(央行管制)
08:購地貸款(央行管制)
09:餘屋貸款(央行管制)
10:工業區閒置土地抵押貸款(央行管制)
11:增貸管制戶(舊央行管制)
12:自然人特定地區第2戶購屋貸款(舊央行管制)
13:投資戶(內部規範)
  *
  * @param ruleCode 規定管制代碼
	*/
  public void setRuleCode(String ruleCode) {
    this.ruleCode = ruleCode;
  }

/**
	* 攤還額異動碼<br>
	* 共用代碼檔
0: 不變  
1: 變
	* @return String
	*/
  public String getExtraRepayCode() {
    return this.extraRepayCode == null ? "" : this.extraRepayCode;
  }

/**
	* 攤還額異動碼<br>
	* 共用代碼檔
0: 不變  
1: 變
  *
  * @param extraRepayCode 攤還額異動碼
	*/
  public void setExtraRepayCode(String extraRepayCode) {
    this.extraRepayCode = extraRepayCode;
  }

/**
	* 客戶別<br>
	* 共用代碼檔
00 一般
01 員工
02 首購
03 關企公司
04 關企員工
05 保戶
07 員工二親等
09 新二階員工
10 保貸戶
	* @return String
	*/
  public String getCustTypeCode() {
    return this.custTypeCode == null ? "" : this.custTypeCode;
  }

/**
	* 客戶別<br>
	* 共用代碼檔
00 一般
01 員工
02 首購
03 關企公司
04 關企員工
05 保戶
07 員工二親等
09 新二階員工
10 保貸戶
  *
  * @param custTypeCode 客戶別
	*/
  public void setCustTypeCode(String custTypeCode) {
    this.custTypeCode = custTypeCode;
  }

/**
	* 循環動用<br>
	* 共用代碼檔
0: 非循環動用  
1: 循環動用
	* @return String
	*/
  public String getRecycleCode() {
    return this.recycleCode == null ? "" : this.recycleCode;
  }

/**
	* 循環動用<br>
	* 共用代碼檔
0: 非循環動用  
1: 循環動用
  *
  * @param recycleCode 循環動用
	*/
  public void setRecycleCode(String recycleCode) {
    this.recycleCode = recycleCode;
  }

/**
	* 循環動用期限<br>
	* 
	* @return Integer
	*/
  public int getRecycleDeadline() {
    return StaticTool.bcToRoc(this.recycleDeadline);
  }

/**
	* 循環動用期限<br>
	* 
  *
  * @param recycleDeadline 循環動用期限
  * @throws LogicException when Date Is Warn	*/
  public void setRecycleDeadline(int recycleDeadline) throws LogicException {
    this.recycleDeadline = StaticTool.rocToBc(recycleDeadline);
  }

/**
	* 資金用途別<br>
	* 共用代碼檔
01: 週轉金
02: 購置不動產
03: 營業用資產
04: 固定資產
05: 企業投資
06: 購置動產
09: 其他
	* @return String
	*/
  public String getUsageCode() {
    return this.usageCode == null ? "" : this.usageCode;
  }

/**
	* 資金用途別<br>
	* 共用代碼檔
01: 週轉金
02: 購置不動產
03: 營業用資產
04: 固定資產
05: 企業投資
06: 購置動產
09: 其他
  *
  * @param usageCode 資金用途別
	*/
  public void setUsageCode(String usageCode) {
    this.usageCode = usageCode;
  }

/**
	* 案件隸屬單位<br>
	* 共用代碼檔
0:非企金單位  
1:企金推展課
	* @return String
	*/
  public String getDepartmentCode() {
    return this.departmentCode == null ? "" : this.departmentCode;
  }

/**
	* 案件隸屬單位<br>
	* 共用代碼檔
0:非企金單位  
1:企金推展課
  *
  * @param departmentCode 案件隸屬單位
	*/
  public void setDepartmentCode(String departmentCode) {
    this.departmentCode = departmentCode;
  }

/**
	* 代繳所得稅<br>
	* Y:是  N:否
	* @return String
	*/
  public String getIncomeTaxFlag() {
    return this.incomeTaxFlag == null ? "" : this.incomeTaxFlag;
  }

/**
	* 代繳所得稅<br>
	* Y:是  N:否
  *
  * @param incomeTaxFlag 代繳所得稅
	*/
  public void setIncomeTaxFlag(String incomeTaxFlag) {
    this.incomeTaxFlag = incomeTaxFlag;
  }

/**
	* 代償碼<br>
	* Y:是  N:否
	* @return String
	*/
  public String getCompensateFlag() {
    return this.compensateFlag == null ? "" : this.compensateFlag;
  }

/**
	* 代償碼<br>
	* Y:是  N:否
  *
  * @param compensateFlag 代償碼
	*/
  public void setCompensateFlag(String compensateFlag) {
    this.compensateFlag = compensateFlag;
  }

/**
	* 不可撤銷<br>
	* Y:是  N:否
	* @return String
	*/
  public String getIrrevocableFlag() {
    return this.irrevocableFlag == null ? "" : this.irrevocableFlag;
  }

/**
	* 不可撤銷<br>
	* Y:是  N:否
  *
  * @param irrevocableFlag 不可撤銷
	*/
  public void setIrrevocableFlag(String irrevocableFlag) {
    this.irrevocableFlag = irrevocableFlag;
  }

/**
	* 利率調整通知<br>
	* 共用代碼檔
1: 電子郵件 
2: 書面通知 
3: 簡訊通知
	* @return String
	*/
  public String getRateAdjNoticeCode() {
    return this.rateAdjNoticeCode == null ? "" : this.rateAdjNoticeCode;
  }

/**
	* 利率調整通知<br>
	* 共用代碼檔
1: 電子郵件 
2: 書面通知 
3: 簡訊通知
  *
  * @param rateAdjNoticeCode 利率調整通知
	*/
  public void setRateAdjNoticeCode(String rateAdjNoticeCode) {
    this.rateAdjNoticeCode = rateAdjNoticeCode;
  }

/**
	* 計件代碼<br>
	* A: 新貸件
B: 其他額度
C: 原額度
D: 新增額度
E: 展期
1: 新貸件
2: 其他額度
3: 原額度
4: 新增額度
5: 展期件
6: 六個月動支
7: 服務件
8: 特殊件
9: 固特利契轉
	* @return String
	*/
  public String getPieceCode() {
    return this.pieceCode == null ? "" : this.pieceCode;
  }

/**
	* 計件代碼<br>
	* A: 新貸件
B: 其他額度
C: 原額度
D: 新增額度
E: 展期
1: 新貸件
2: 其他額度
3: 原額度
4: 新增額度
5: 展期件
6: 六個月動支
7: 服務件
8: 特殊件
9: 固特利契轉
  *
  * @param pieceCode 計件代碼
	*/
  public void setPieceCode(String pieceCode) {
    this.pieceCode = pieceCode;
  }

/**
	* 繳款方式<br>
	* 1: 匯款轉帳
2: 銀行扣款
3: 員工扣薪
4: 支票
5: 特約金
6: 人事特約金
7: 定存特約
8: 劃撥存款
	* @return Integer
	*/
  public int getRepayCode() {
    return this.repayCode;
  }

/**
	* 繳款方式<br>
	* 1: 匯款轉帳
2: 銀行扣款
3: 員工扣薪
4: 支票
5: 特約金
6: 人事特約金
7: 定存特約
8: 劃撥存款
  *
  * @param repayCode 繳款方式
	*/
  public void setRepayCode(int repayCode) {
    this.repayCode = repayCode;
  }

/**
	* 介紹人<br>
	* 
	* @return String
	*/
  public String getIntroducer() {
    return this.introducer == null ? "" : this.introducer;
  }

/**
	* 介紹人<br>
	* 
  *
  * @param introducer 介紹人
	*/
  public void setIntroducer(String introducer) {
    this.introducer = introducer;
  }

/**
	* 區部<br>
	* 
	* @return String
	*/
  public String getDistrict() {
    return this.district == null ? "" : this.district;
  }

/**
	* 區部<br>
	* 
  *
  * @param district 區部
	*/
  public void setDistrict(String district) {
    this.district = district;
  }

/**
	* 火險服務<br>
	* 
	* @return String
	*/
  public String getFireOfficer() {
    return this.fireOfficer == null ? "" : this.fireOfficer;
  }

/**
	* 火險服務<br>
	* 
  *
  * @param fireOfficer 火險服務
	*/
  public void setFireOfficer(String fireOfficer) {
    this.fireOfficer = fireOfficer;
  }

/**
	* 估價<br>
	* 
	* @return String
	*/
  public String getEstimate() {
    return this.estimate == null ? "" : this.estimate;
  }

/**
	* 估價<br>
	* 
  *
  * @param estimate 估價
	*/
  public void setEstimate(String estimate) {
    this.estimate = estimate;
  }

/**
	* 授信<br>
	* 
	* @return String
	*/
  public String getCreditOfficer() {
    return this.creditOfficer == null ? "" : this.creditOfficer;
  }

/**
	* 授信<br>
	* 
  *
  * @param creditOfficer 授信
	*/
  public void setCreditOfficer(String creditOfficer) {
    this.creditOfficer = creditOfficer;
  }

/**
	* 放款業務專員<br>
	* 目前未用(原AS400為協辦人員)
	* @return String
	*/
  public String getLoanOfficer() {
    return this.loanOfficer == null ? "" : this.loanOfficer;
  }

/**
	* 放款業務專員<br>
	* 目前未用(原AS400為協辦人員)
  *
  * @param loanOfficer 放款業務專員
	*/
  public void setLoanOfficer(String loanOfficer) {
    this.loanOfficer = loanOfficer;
  }

/**
	* 放款專員/房貸專員/企金人員<br>
	* 
	* @return String
	*/
  public String getBusinessOfficer() {
    return this.businessOfficer == null ? "" : this.businessOfficer;
  }

/**
	* 放款專員/房貸專員/企金人員<br>
	* 
  *
  * @param businessOfficer 放款專員/房貸專員/企金人員
	*/
  public void setBusinessOfficer(String businessOfficer) {
    this.businessOfficer = businessOfficer;
  }

/**
	* 核決主管<br>
	* 
	* @return String
	*/
  public String getSupervisor() {
    return this.supervisor == null ? "" : this.supervisor;
  }

/**
	* 核決主管<br>
	* 
  *
  * @param supervisor 核決主管
	*/
  public void setSupervisor(String supervisor) {
    this.supervisor = supervisor;
  }

/**
	* 徵信<br>
	* 
	* @return String
	*/
  public String getInvestigateOfficer() {
    return this.investigateOfficer == null ? "" : this.investigateOfficer;
  }

/**
	* 徵信<br>
	* 
  *
  * @param investigateOfficer 徵信
	*/
  public void setInvestigateOfficer(String investigateOfficer) {
    this.investigateOfficer = investigateOfficer;
  }

/**
	* 估價覆核<br>
	* 
	* @return String
	*/
  public String getEstimateReview() {
    return this.estimateReview == null ? "" : this.estimateReview;
  }

/**
	* 估價覆核<br>
	* 
  *
  * @param estimateReview 估價覆核
	*/
  public void setEstimateReview(String estimateReview) {
    this.estimateReview = estimateReview;
  }

/**
	* 協辦人<br>
	* AS400 放款業務專員
	* @return String
	*/
  public String getCoorgnizer() {
    return this.coorgnizer == null ? "" : this.coorgnizer;
  }

/**
	* 協辦人<br>
	* AS400 放款業務專員
  *
  * @param coorgnizer 協辦人
	*/
  public void setCoorgnizer(String coorgnizer) {
    this.coorgnizer = coorgnizer;
  }

/**
	* 提前清償原因<br>
	* 共用代碼檔
00:無
01:買賣
02:自行還清
03:軍功教勞工貸款轉貸
04:利率過高轉貸
05:增貸不准轉貸
06:額度內動支不准轉貸
07:內部代償
08:借新還舊
09:其他
10:買回
11:綁約期還款
	* @return Integer
	*/
  public int getAdvanceCloseCode() {
    return this.advanceCloseCode;
  }

/**
	* 提前清償原因<br>
	* 共用代碼檔
00:無
01:買賣
02:自行還清
03:軍功教勞工貸款轉貸
04:利率過高轉貸
05:增貸不准轉貸
06:額度內動支不准轉貸
07:內部代償
08:借新還舊
09:其他
10:買回
11:綁約期還款
  *
  * @param advanceCloseCode 提前清償原因
	*/
  public void setAdvanceCloseCode(int advanceCloseCode) {
    this.advanceCloseCode = advanceCloseCode;
  }

/**
	* 信用評分<br>
	* 
	* @return Integer
	*/
  public int getCreditScore() {
    return this.creditScore;
  }

/**
	* 信用評分<br>
	* 
  *
  * @param creditScore 信用評分
	*/
  public void setCreditScore(int creditScore) {
    this.creditScore = creditScore;
  }

/**
	* 對保日期<br>
	* 
	* @return Integer
	*/
  public int getGuaranteeDate() {
    return StaticTool.bcToRoc(this.guaranteeDate);
  }

/**
	* 對保日期<br>
	* 
  *
  * @param guaranteeDate 對保日期
  * @throws LogicException when Date Is Warn	*/
  public void setGuaranteeDate(int guaranteeDate) throws LogicException {
    this.guaranteeDate = StaticTool.rocToBc(guaranteeDate);
  }

/**
	* 合約編號<br>
	* 
	* @return String
	*/
  public String getContractNo() {
    return this.contractNo == null ? "" : this.contractNo;
  }

/**
	* 合約編號<br>
	* 
  *
  * @param contractNo 合約編號
	*/
  public void setContractNo(String contractNo) {
    this.contractNo = contractNo;
  }

/**
	* 擔保品設定記號<br>
	* Y:是 N:否
	* @return String
	*/
  public String getColSetFlag() {
    return this.colSetFlag == null ? "" : this.colSetFlag;
  }

/**
	* 擔保品設定記號<br>
	* Y:是 N:否
  *
  * @param colSetFlag 擔保品設定記號
	*/
  public void setColSetFlag(String colSetFlag) {
    this.colSetFlag = colSetFlag;
  }

/**
	* 交易進行記號<br>
	* 1STEP TX -&amp;gt; 0    (from eloan)
2STEP TX -&amp;gt; 1 2
	* @return Integer
	*/
  public int getActFg() {
    return this.actFg;
  }

/**
	* 交易進行記號<br>
	* 1STEP TX -&amp;gt; 0    (from eloan)
2STEP TX -&amp;gt; 1 2
  *
  * @param actFg 交易進行記號
	*/
  public void setActFg(int actFg) {
    this.actFg = actFg;
  }

/**
	* 上次交易日<br>
	* 更正時, 檢查是否為最近一筆交易
	* @return Integer
	*/
  public int getLastAcctDate() {
    return StaticTool.bcToRoc(this.lastAcctDate);
  }

/**
	* 上次交易日<br>
	* 更正時, 檢查是否為最近一筆交易
  *
  * @param lastAcctDate 上次交易日
  * @throws LogicException when Date Is Warn	*/
  public void setLastAcctDate(int lastAcctDate) throws LogicException {
    this.lastAcctDate = StaticTool.rocToBc(lastAcctDate);
  }

/**
	* 上次交易行別<br>
	* 
	* @return String
	*/
  public String getLastKinbr() {
    return this.lastKinbr == null ? "" : this.lastKinbr;
  }

/**
	* 上次交易行別<br>
	* 
  *
  * @param lastKinbr 上次交易行別
	*/
  public void setLastKinbr(String lastKinbr) {
    this.lastKinbr = lastKinbr;
  }

/**
	* 上次櫃員編號<br>
	* 
	* @return String
	*/
  public String getLastTlrNo() {
    return this.lastTlrNo == null ? "" : this.lastTlrNo;
  }

/**
	* 上次櫃員編號<br>
	* 
  *
  * @param lastTlrNo 上次櫃員編號
	*/
  public void setLastTlrNo(String lastTlrNo) {
    this.lastTlrNo = lastTlrNo;
  }

/**
	* 上次交易序號<br>
	* 
	* @return String
	*/
  public String getLastTxtNo() {
    return this.lastTxtNo == null ? "" : this.lastTxtNo;
  }

/**
	* 上次交易序號<br>
	* 
  *
  * @param lastTxtNo 上次交易序號
	*/
  public void setLastTxtNo(String lastTxtNo) {
    this.lastTxtNo = lastTxtNo;
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
	* 是否已列印[撥款審核資料表]<br>
	* Y:是 N:否
	* @return String
	*/
  public String getL9110Flag() {
    return this.l9110Flag == null ? "" : this.l9110Flag;
  }

/**
	* 是否已列印[撥款審核資料表]<br>
	* Y:是 N:否
  *
  * @param l9110Flag 是否已列印[撥款審核資料表]
	*/
  public void setL9110Flag(String l9110Flag) {
    this.l9110Flag = l9110Flag;
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
    return "FacMain [facMainId=" + facMainId + ", lastBormNo=" + lastBormNo + ", lastBormRvNo=" + lastBormRvNo + ", applNo=" + applNo + ", creditSysNo=" + creditSysNo
           + ", prodNo=" + prodNo + ", baseRateCode=" + baseRateCode + ", rateIncr=" + rateIncr + ", individualIncr=" + individualIncr + ", approveRate=" + approveRate + ", annualIncr=" + annualIncr
           + ", emailIncr=" + emailIncr + ", graceIncr=" + graceIncr + ", rateCode=" + rateCode + ", firstRateAdjFreq=" + firstRateAdjFreq + ", rateAdjFreq=" + rateAdjFreq + ", currencyCode=" + currencyCode
           + ", lineAmt=" + lineAmt + ", utilAmt=" + utilAmt + ", utilBal=" + utilBal + ", acctCode=" + acctCode + ", loanTermYy=" + loanTermYy + ", loanTermMm=" + loanTermMm
           + ", loanTermDd=" + loanTermDd + ", firstDrawdownDate=" + firstDrawdownDate + ", maturityDate=" + maturityDate + ", intCalcCode=" + intCalcCode + ", amortizedCode=" + amortizedCode + ", freqBase=" + freqBase
           + ", payIntFreq=" + payIntFreq + ", repayFreq=" + repayFreq + ", utilDeadline=" + utilDeadline + ", gracePeriod=" + gracePeriod + ", acctFee=" + acctFee + ", ruleCode=" + ruleCode
           + ", extraRepayCode=" + extraRepayCode + ", custTypeCode=" + custTypeCode + ", recycleCode=" + recycleCode + ", recycleDeadline=" + recycleDeadline + ", usageCode=" + usageCode + ", departmentCode=" + departmentCode
           + ", incomeTaxFlag=" + incomeTaxFlag + ", compensateFlag=" + compensateFlag + ", irrevocableFlag=" + irrevocableFlag + ", rateAdjNoticeCode=" + rateAdjNoticeCode + ", pieceCode=" + pieceCode + ", repayCode=" + repayCode
           + ", introducer=" + introducer + ", district=" + district + ", fireOfficer=" + fireOfficer + ", estimate=" + estimate + ", creditOfficer=" + creditOfficer + ", loanOfficer=" + loanOfficer
           + ", businessOfficer=" + businessOfficer + ", supervisor=" + supervisor + ", investigateOfficer=" + investigateOfficer + ", estimateReview=" + estimateReview + ", coorgnizer=" + coorgnizer + ", advanceCloseCode=" + advanceCloseCode
           + ", creditScore=" + creditScore + ", guaranteeDate=" + guaranteeDate + ", contractNo=" + contractNo + ", colSetFlag=" + colSetFlag + ", actFg=" + actFg + ", lastAcctDate=" + lastAcctDate
           + ", lastKinbr=" + lastKinbr + ", lastTlrNo=" + lastTlrNo + ", lastTxtNo=" + lastTxtNo + ", acDate=" + acDate + ", l9110Flag=" + l9110Flag + ", branchNo=" + branchNo
           + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
