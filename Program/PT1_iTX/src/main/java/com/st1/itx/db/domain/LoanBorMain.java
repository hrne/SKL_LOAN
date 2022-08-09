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
 * LoanBorMain 放款主檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`LoanBorMain`")
public class LoanBorMain implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 6905585463111370464L;

@EmbeddedId
  private LoanBorMainId loanBorMainId;

  // 借款人戶號
  @Column(name = "`CustNo`", insertable = false, updatable = false)
  private int custNo = 0;

  // 額度編號
  @Column(name = "`FacmNo`", insertable = false, updatable = false)
  private int facmNo = 0;

  // 撥款序號, 預約序號
  @Column(name = "`BormNo`", insertable = false, updatable = false)
  private int bormNo = 0;

  // 已編BorTx流水號
  @Column(name = "`LastBorxNo`")
  private int lastBorxNo = 0;

  // 已編Overdue流水號
  @Column(name = "`LastOvduNo`")
  private int lastOvduNo = 0;

  // 戶況
  /* 0:正常戶1:展期2:催收戶3:結案戶4:逾期戶(顯示用)5:催收結案戶6:呆帳戶7:部分轉呆戶8:債權轉讓戶9:呆帳結案戶97:預約撥款已刪除98:預約已撥款99:預約撥款 */
  @Column(name = "`Status`")
  private int status = 0;

  // 加碼利率
  @Column(name = "`RateIncr`")
  private BigDecimal rateIncr = new BigDecimal("0");

  // 個別加碼利率
  @Column(name = "`IndividualIncr`")
  private BigDecimal individualIncr = new BigDecimal("0");

  // 核准利率
  @Column(name = "`ApproveRate`")
  private BigDecimal approveRate = new BigDecimal("0");

  // 實際計息利率
  @Column(name = "`StoreRate`")
  private BigDecimal storeRate = new BigDecimal("0");

  // 利率區分
  /* 共用代碼檔1:機動 2:固定3:定期機動 */
  @Column(name = "`RateCode`", length = 1)
  private String rateCode;

  // 利率調整週期
  @Column(name = "`RateAdjFreq`")
  private int rateAdjFreq = 0;

  // 撥款方式
  /* 共用代碼檔1:整批匯款(預撥)  2:單筆匯款(即時) */
  @Column(name = "`DrawdownCode`", length = 1)
  private String drawdownCode;

  // 幣別
  @Column(name = "`CurrencyCode`", length = 3)
  private String currencyCode;

  // 撥款金額
  @Column(name = "`DrawdownAmt`")
  private BigDecimal drawdownAmt = new BigDecimal("0");

  // 放款餘額
  @Column(name = "`LoanBal`")
  private BigDecimal loanBal = new BigDecimal("0");

  // 撥款日期, 預約日期
  @Column(name = "`DrawdownDate`")
  private int drawdownDate = 0;

  // 貸款期間年
  @Column(name = "`LoanTermYy`")
  private int loanTermYy = 0;

  // 貸款期間月
  @Column(name = "`LoanTermMm`")
  private int loanTermMm = 0;

  // 貸款期間日
  @Column(name = "`LoanTermDd`")
  private int loanTermDd = 0;

  // 到期日
  @Column(name = "`MaturityDate`")
  private int maturityDate = 0;

  // 計息方式
  /* 共用代碼檔1:按日計息  2:按月計息 */
  @Column(name = "`IntCalcCode`", length = 1)
  private String intCalcCode;

  // 攤還方式
  /* 共用代碼檔1.按月繳息(按期繳息到期還本)2.到期取息(到期繳息還本)3.本息平均法(期金)4.本金平均法5.按月撥款收息(逆向貸款) */
  @Column(name = "`AmortizedCode`", length = 1)
  private String amortizedCode;

  // 週期基準
  /* 1:日2:月3:週 */
  @Column(name = "`FreqBase`")
  private int freqBase = 0;

  // 繳息週期
  @Column(name = "`PayIntFreq`")
  private int payIntFreq = 0;

  // 還本週期
  @Column(name = "`RepayFreq`")
  private int repayFreq = 0;

  // 總期數
  @Column(name = "`TotalPeriod`")
  private int totalPeriod = 0;

  // 已還本期數
  @Column(name = "`RepaidPeriod`")
  private int repaidPeriod = 0;

  // 已繳息期數
  @Column(name = "`PaidTerms`")
  private int paidTerms = 0;

  // 上次繳息日,繳息迄日
  @Column(name = "`PrevPayIntDate`")
  private int prevPayIntDate = 0;

  // 上次還本日,最後還本日
  @Column(name = "`PrevRepaidDate`")
  private int prevRepaidDate = 0;

  // 下次繳息日,應繳息日
  /* 最後一期依應繳日計算 */
  @Column(name = "`NextPayIntDate`")
  private int nextPayIntDate = 0;

  // 下次還本日,應還本日
  /* 最後一期為到期日 */
  @Column(name = "`NextRepayDate`")
  private int nextRepayDate = 0;

  // 每期攤還金額
  @Column(name = "`DueAmt`")
  private BigDecimal dueAmt = new BigDecimal("0");

  // 寬限期
  @Column(name = "`GracePeriod`")
  private int gracePeriod = 0;

  // 寬限到期日
  @Column(name = "`GraceDate`")
  private int graceDate = 0;

  // 指定應繳日
  @Column(name = "`SpecificDd`")
  private int specificDd = 0;

  // 指定基準日期
  @Column(name = "`SpecificDate`")
  private int specificDate = 0;

  // 首次應繳日
  @Column(name = "`FirstDueDate`")
  private int firstDueDate = 0;

  // 首次利率調整日期
  @Column(name = "`FirstAdjRateDate`")
  private int firstAdjRateDate = 0;

  // 下次利率調整日期
  @Column(name = "`NextAdjRateDate`")
  private int nextAdjRateDate = 0;

  // 帳管費
  @Column(name = "`AcctFee`")
  private BigDecimal acctFee = new BigDecimal("0");

  // 手續費
  @Column(name = "`HandlingFee`")
  private BigDecimal handlingFee = new BigDecimal("0");

  // 最後一期本金餘額
  @Column(name = "`FinalBal`")
  private BigDecimal finalBal = new BigDecimal("0");

  // 未齊件
  /* Y:是N:否 */
  @Column(name = "`NotYetFlag`", length = 1)
  private String notYetFlag;

  // 展期/借新還舊
  /* 0:正常 1.展期撥款(不同額度)2.借新還舊撥款(同額度) */
  @Column(name = "`RenewFlag`", length = 1)
  private String renewFlag;

  // 計件代碼
  /* 共用代碼檔(PieceCode)A:新貸件B:其他額度C:原額度D:新增額度E:展期1:新貸件2:其他額度3:原額度4:新增額度5:展期件6:六個月動支7:服務件8:特殊件9:固特利契轉 */
  @Column(name = "`PieceCode`", length = 1)
  private String pieceCode;

  // 計件代碼2
  /* 共用代碼檔(PieceCode) */
  @Column(name = "`PieceCodeSecond`", length = 1)
  private String pieceCodeSecond;

  // 計件代碼2金額
  @Column(name = "`PieceCodeSecondAmt`")
  private BigDecimal pieceCodeSecondAmt = new BigDecimal("0");

  // 資金用途別
  /* 共用代碼檔01:週轉金02:購置不動產03:營業用資產04:固定資產05:企業投資06:購置動產09:其他 */
  @Column(name = "`UsageCode`", length = 2)
  private String usageCode;

  // 聯貸案序號
  @Column(name = "`SyndNo`")
  private int syndNo = 0;

  // 與借款人關係
  /* 共用代碼檔00:本人01:夫02:妻03:父04:母05:子06:女07:兄08:弟09:姊10:妹11:姪子99:其他 */
  @Column(name = "`RelationCode`", length = 2)
  private String relationCode;

  // 第三人帳戶戶名
  @Column(name = "`RelationName`", length = 100)
  private String relationName;

  // 第三人身份證字號
  @Column(name = "`RelationId`", length = 10)
  private String relationId;

  // 第三人生日
  @Column(name = "`RelationBirthday`")
  private int relationBirthday = 0;

  // 第三人性別
  /* 1:男2:女 */
  @Column(name = "`RelationGender`", length = 1)
  private String relationGender;

  // 交易進行記號
  /* 0:1STEP TX1/2:2STEP TX1/2/3/4:3STEP TX */
  @Column(name = "`ActFg`")
  private int actFg = 0;

  // 上次交易日
  /* 更正時, 檢查是否為最近一筆交易 */
  @Column(name = "`LastEntDy`")
  private int lastEntDy = 0;

  // 上次交易行別
  @Column(name = "`LastKinbr`", length = 4)
  private String lastKinbr;

  // 上次櫃員編號
  @Column(name = "`LastTlrNo`", length = 6)
  private String lastTlrNo;

  // 上次交易序號
  @Column(name = "`LastTxtNo`", length = 8)
  private String lastTxtNo;

  // 匯款銀行
  /* 預約撥款匯款資料 */
  @Column(name = "`RemitBank`", length = 3)
  private String remitBank;

  // 匯款分行
  @Column(name = "`RemitBranch`", length = 4)
  private String remitBranch;

  // 匯款帳號
  @Column(name = "`RemitAcctNo`")
  private BigDecimal remitAcctNo = new BigDecimal("0");

  // 匯款戶名(代償專戶)
  @Column(name = "`CompensateAcct`", length = 60)
  private String compensateAcct;

  // 解付單位代號
  @Column(name = "`PaymentBank`", length = 7)
  private String paymentBank;

  // 附言
  @Column(name = "`Remark`", length = 40)
  private String remark;

  // 會計日期
  @Column(name = "`AcDate`")
  private int acDate = 0;

  // 次日交易會計日期
  @Column(name = "`NextAcDate`")
  private int nextAcDate = 0;

  // 單位別
  @Column(name = "`BranchNo`", length = 4)
  private String branchNo;

  // 寬限區分
  /* LA$LMSP.LMSGTP0:不寬限1:寬限 */
  @Column(name = "`GraceFlag`")
  private int graceFlag = 0;

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


  public LoanBorMainId getLoanBorMainId() {
    return this.loanBorMainId;
  }

  public void setLoanBorMainId(LoanBorMainId loanBorMainId) {
    this.loanBorMainId = loanBorMainId;
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
	* 撥款序號, 預約序號<br>
	* 
	* @return Integer
	*/
  public int getBormNo() {
    return this.bormNo;
  }

/**
	* 撥款序號, 預約序號<br>
	* 
  *
  * @param bormNo 撥款序號, 預約序號
	*/
  public void setBormNo(int bormNo) {
    this.bormNo = bormNo;
  }

/**
	* 已編BorTx流水號<br>
	* 
	* @return Integer
	*/
  public int getLastBorxNo() {
    return this.lastBorxNo;
  }

/**
	* 已編BorTx流水號<br>
	* 
  *
  * @param lastBorxNo 已編BorTx流水號
	*/
  public void setLastBorxNo(int lastBorxNo) {
    this.lastBorxNo = lastBorxNo;
  }

/**
	* 已編Overdue流水號<br>
	* 
	* @return Integer
	*/
  public int getLastOvduNo() {
    return this.lastOvduNo;
  }

/**
	* 已編Overdue流水號<br>
	* 
  *
  * @param lastOvduNo 已編Overdue流水號
	*/
  public void setLastOvduNo(int lastOvduNo) {
    this.lastOvduNo = lastOvduNo;
  }

/**
	* 戶況<br>
	* 0:正常戶
1:展期
2:催收戶
3:結案戶
4:逾期戶(顯示用)
5:催收結案戶
6:呆帳戶
7:部分轉呆戶
8:債權轉讓戶
9:呆帳結案戶
97:預約撥款已刪除
98:預約已撥款
99:預約撥款
	* @return Integer
	*/
  public int getStatus() {
    return this.status;
  }

/**
	* 戶況<br>
	* 0:正常戶
1:展期
2:催收戶
3:結案戶
4:逾期戶(顯示用)
5:催收結案戶
6:呆帳戶
7:部分轉呆戶
8:債權轉讓戶
9:呆帳結案戶
97:預約撥款已刪除
98:預約已撥款
99:預約撥款
  *
  * @param status 戶況
	*/
  public void setStatus(int status) {
    this.status = status;
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
	* 個別加碼利率<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getIndividualIncr() {
    return this.individualIncr;
  }

/**
	* 個別加碼利率<br>
	* 
  *
  * @param individualIncr 個別加碼利率
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
	* 實際計息利率<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getStoreRate() {
    return this.storeRate;
  }

/**
	* 實際計息利率<br>
	* 
  *
  * @param storeRate 實際計息利率
	*/
  public void setStoreRate(BigDecimal storeRate) {
    this.storeRate = storeRate;
  }

/**
	* 利率區分<br>
	* 共用代碼檔
1:機動 
2:固定
3:定期機動
	* @return String
	*/
  public String getRateCode() {
    return this.rateCode == null ? "" : this.rateCode;
  }

/**
	* 利率區分<br>
	* 共用代碼檔
1:機動 
2:固定
3:定期機動
  *
  * @param rateCode 利率區分
	*/
  public void setRateCode(String rateCode) {
    this.rateCode = rateCode;
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
	* 撥款方式<br>
	* 共用代碼檔
1:整批匯款(預撥)  
2:單筆匯款(即時)
	* @return String
	*/
  public String getDrawdownCode() {
    return this.drawdownCode == null ? "" : this.drawdownCode;
  }

/**
	* 撥款方式<br>
	* 共用代碼檔
1:整批匯款(預撥)  
2:單筆匯款(即時)
  *
  * @param drawdownCode 撥款方式
	*/
  public void setDrawdownCode(String drawdownCode) {
    this.drawdownCode = drawdownCode;
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
	* 撥款金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getDrawdownAmt() {
    return this.drawdownAmt;
  }

/**
	* 撥款金額<br>
	* 
  *
  * @param drawdownAmt 撥款金額
	*/
  public void setDrawdownAmt(BigDecimal drawdownAmt) {
    this.drawdownAmt = drawdownAmt;
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
	* 撥款日期, 預約日期<br>
	* 
	* @return Integer
	*/
  public int getDrawdownDate() {
    return StaticTool.bcToRoc(this.drawdownDate);
  }

/**
	* 撥款日期, 預約日期<br>
	* 
  *
  * @param drawdownDate 撥款日期, 預約日期
  * @throws LogicException when Date Is Warn	*/
  public void setDrawdownDate(int drawdownDate) throws LogicException {
    this.drawdownDate = StaticTool.rocToBc(drawdownDate);
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
	* 到期日<br>
	* 
	* @return Integer
	*/
  public int getMaturityDate() {
    return StaticTool.bcToRoc(this.maturityDate);
  }

/**
	* 到期日<br>
	* 
  *
  * @param maturityDate 到期日
  * @throws LogicException when Date Is Warn	*/
  public void setMaturityDate(int maturityDate) throws LogicException {
    this.maturityDate = StaticTool.rocToBc(maturityDate);
  }

/**
	* 計息方式<br>
	* 共用代碼檔
1:按日計息  
2:按月計息
	* @return String
	*/
  public String getIntCalcCode() {
    return this.intCalcCode == null ? "" : this.intCalcCode;
  }

/**
	* 計息方式<br>
	* 共用代碼檔
1:按日計息  
2:按月計息
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
	* 1:日
2:月
3:週
	* @return Integer
	*/
  public int getFreqBase() {
    return this.freqBase;
  }

/**
	* 週期基準<br>
	* 1:日
2:月
3:週
  *
  * @param freqBase 週期基準
	*/
  public void setFreqBase(int freqBase) {
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
	* 總期數<br>
	* 
	* @return Integer
	*/
  public int getTotalPeriod() {
    return this.totalPeriod;
  }

/**
	* 總期數<br>
	* 
  *
  * @param totalPeriod 總期數
	*/
  public void setTotalPeriod(int totalPeriod) {
    this.totalPeriod = totalPeriod;
  }

/**
	* 已還本期數<br>
	* 
	* @return Integer
	*/
  public int getRepaidPeriod() {
    return this.repaidPeriod;
  }

/**
	* 已還本期數<br>
	* 
  *
  * @param repaidPeriod 已還本期數
	*/
  public void setRepaidPeriod(int repaidPeriod) {
    this.repaidPeriod = repaidPeriod;
  }

/**
	* 已繳息期數<br>
	* 
	* @return Integer
	*/
  public int getPaidTerms() {
    return this.paidTerms;
  }

/**
	* 已繳息期數<br>
	* 
  *
  * @param paidTerms 已繳息期數
	*/
  public void setPaidTerms(int paidTerms) {
    this.paidTerms = paidTerms;
  }

/**
	* 上次繳息日,繳息迄日<br>
	* 
	* @return Integer
	*/
  public int getPrevPayIntDate() {
    return StaticTool.bcToRoc(this.prevPayIntDate);
  }

/**
	* 上次繳息日,繳息迄日<br>
	* 
  *
  * @param prevPayIntDate 上次繳息日,繳息迄日
  * @throws LogicException when Date Is Warn	*/
  public void setPrevPayIntDate(int prevPayIntDate) throws LogicException {
    this.prevPayIntDate = StaticTool.rocToBc(prevPayIntDate);
  }

/**
	* 上次還本日,最後還本日<br>
	* 
	* @return Integer
	*/
  public int getPrevRepaidDate() {
    return StaticTool.bcToRoc(this.prevRepaidDate);
  }

/**
	* 上次還本日,最後還本日<br>
	* 
  *
  * @param prevRepaidDate 上次還本日,最後還本日
  * @throws LogicException when Date Is Warn	*/
  public void setPrevRepaidDate(int prevRepaidDate) throws LogicException {
    this.prevRepaidDate = StaticTool.rocToBc(prevRepaidDate);
  }

/**
	* 下次繳息日,應繳息日<br>
	* 最後一期依應繳日計算
	* @return Integer
	*/
  public int getNextPayIntDate() {
    return StaticTool.bcToRoc(this.nextPayIntDate);
  }

/**
	* 下次繳息日,應繳息日<br>
	* 最後一期依應繳日計算
  *
  * @param nextPayIntDate 下次繳息日,應繳息日
  * @throws LogicException when Date Is Warn	*/
  public void setNextPayIntDate(int nextPayIntDate) throws LogicException {
    this.nextPayIntDate = StaticTool.rocToBc(nextPayIntDate);
  }

/**
	* 下次還本日,應還本日<br>
	* 最後一期為到期日
	* @return Integer
	*/
  public int getNextRepayDate() {
    return StaticTool.bcToRoc(this.nextRepayDate);
  }

/**
	* 下次還本日,應還本日<br>
	* 最後一期為到期日
  *
  * @param nextRepayDate 下次還本日,應還本日
  * @throws LogicException when Date Is Warn	*/
  public void setNextRepayDate(int nextRepayDate) throws LogicException {
    this.nextRepayDate = StaticTool.rocToBc(nextRepayDate);
  }

/**
	* 每期攤還金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getDueAmt() {
    return this.dueAmt;
  }

/**
	* 每期攤還金額<br>
	* 
  *
  * @param dueAmt 每期攤還金額
	*/
  public void setDueAmt(BigDecimal dueAmt) {
    this.dueAmt = dueAmt;
  }

/**
	* 寬限期<br>
	* 
	* @return Integer
	*/
  public int getGracePeriod() {
    return this.gracePeriod;
  }

/**
	* 寬限期<br>
	* 
  *
  * @param gracePeriod 寬限期
	*/
  public void setGracePeriod(int gracePeriod) {
    this.gracePeriod = gracePeriod;
  }

/**
	* 寬限到期日<br>
	* 
	* @return Integer
	*/
  public int getGraceDate() {
    return StaticTool.bcToRoc(this.graceDate);
  }

/**
	* 寬限到期日<br>
	* 
  *
  * @param graceDate 寬限到期日
  * @throws LogicException when Date Is Warn	*/
  public void setGraceDate(int graceDate) throws LogicException {
    this.graceDate = StaticTool.rocToBc(graceDate);
  }

/**
	* 指定應繳日<br>
	* 
	* @return Integer
	*/
  public int getSpecificDd() {
    return this.specificDd;
  }

/**
	* 指定應繳日<br>
	* 
  *
  * @param specificDd 指定應繳日
	*/
  public void setSpecificDd(int specificDd) {
    this.specificDd = specificDd;
  }

/**
	* 指定基準日期<br>
	* 
	* @return Integer
	*/
  public int getSpecificDate() {
    return StaticTool.bcToRoc(this.specificDate);
  }

/**
	* 指定基準日期<br>
	* 
  *
  * @param specificDate 指定基準日期
  * @throws LogicException when Date Is Warn	*/
  public void setSpecificDate(int specificDate) throws LogicException {
    this.specificDate = StaticTool.rocToBc(specificDate);
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
	* 首次利率調整日期<br>
	* 
	* @return Integer
	*/
  public int getFirstAdjRateDate() {
    return StaticTool.bcToRoc(this.firstAdjRateDate);
  }

/**
	* 首次利率調整日期<br>
	* 
  *
  * @param firstAdjRateDate 首次利率調整日期
  * @throws LogicException when Date Is Warn	*/
  public void setFirstAdjRateDate(int firstAdjRateDate) throws LogicException {
    this.firstAdjRateDate = StaticTool.rocToBc(firstAdjRateDate);
  }

/**
	* 下次利率調整日期<br>
	* 
	* @return Integer
	*/
  public int getNextAdjRateDate() {
    return StaticTool.bcToRoc(this.nextAdjRateDate);
  }

/**
	* 下次利率調整日期<br>
	* 
  *
  * @param nextAdjRateDate 下次利率調整日期
  * @throws LogicException when Date Is Warn	*/
  public void setNextAdjRateDate(int nextAdjRateDate) throws LogicException {
    this.nextAdjRateDate = StaticTool.rocToBc(nextAdjRateDate);
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
	* 手續費<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getHandlingFee() {
    return this.handlingFee;
  }

/**
	* 手續費<br>
	* 
  *
  * @param handlingFee 手續費
	*/
  public void setHandlingFee(BigDecimal handlingFee) {
    this.handlingFee = handlingFee;
  }

/**
	* 最後一期本金餘額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getFinalBal() {
    return this.finalBal;
  }

/**
	* 最後一期本金餘額<br>
	* 
  *
  * @param finalBal 最後一期本金餘額
	*/
  public void setFinalBal(BigDecimal finalBal) {
    this.finalBal = finalBal;
  }

/**
	* 未齊件<br>
	* Y:是
N:否
	* @return String
	*/
  public String getNotYetFlag() {
    return this.notYetFlag == null ? "" : this.notYetFlag;
  }

/**
	* 未齊件<br>
	* Y:是
N:否
  *
  * @param notYetFlag 未齊件
	*/
  public void setNotYetFlag(String notYetFlag) {
    this.notYetFlag = notYetFlag;
  }

/**
	* 展期/借新還舊<br>
	* 0:正常 
1.展期撥款(不同額度)
2.借新還舊撥款(同額度)
	* @return String
	*/
  public String getRenewFlag() {
    return this.renewFlag == null ? "" : this.renewFlag;
  }

/**
	* 展期/借新還舊<br>
	* 0:正常 
1.展期撥款(不同額度)
2.借新還舊撥款(同額度)
  *
  * @param renewFlag 展期/借新還舊
	*/
  public void setRenewFlag(String renewFlag) {
    this.renewFlag = renewFlag;
  }

/**
	* 計件代碼<br>
	* 共用代碼檔(PieceCode)
A:新貸件
B:其他額度
C:原額度
D:新增額度
E:展期
1:新貸件
2:其他額度
3:原額度
4:新增額度
5:展期件
6:六個月動支
7:服務件
8:特殊件
9:固特利契轉
	* @return String
	*/
  public String getPieceCode() {
    return this.pieceCode == null ? "" : this.pieceCode;
  }

/**
	* 計件代碼<br>
	* 共用代碼檔(PieceCode)
A:新貸件
B:其他額度
C:原額度
D:新增額度
E:展期
1:新貸件
2:其他額度
3:原額度
4:新增額度
5:展期件
6:六個月動支
7:服務件
8:特殊件
9:固特利契轉
  *
  * @param pieceCode 計件代碼
	*/
  public void setPieceCode(String pieceCode) {
    this.pieceCode = pieceCode;
  }

/**
	* 計件代碼2<br>
	* 共用代碼檔(PieceCode)
	* @return String
	*/
  public String getPieceCodeSecond() {
    return this.pieceCodeSecond == null ? "" : this.pieceCodeSecond;
  }

/**
	* 計件代碼2<br>
	* 共用代碼檔(PieceCode)
  *
  * @param pieceCodeSecond 計件代碼2
	*/
  public void setPieceCodeSecond(String pieceCodeSecond) {
    this.pieceCodeSecond = pieceCodeSecond;
  }

/**
	* 計件代碼2金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getPieceCodeSecondAmt() {
    return this.pieceCodeSecondAmt;
  }

/**
	* 計件代碼2金額<br>
	* 
  *
  * @param pieceCodeSecondAmt 計件代碼2金額
	*/
  public void setPieceCodeSecondAmt(BigDecimal pieceCodeSecondAmt) {
    this.pieceCodeSecondAmt = pieceCodeSecondAmt;
  }

/**
	* 資金用途別<br>
	* 共用代碼檔
01:週轉金
02:購置不動產
03:營業用資產
04:固定資產
05:企業投資
06:購置動產
09:其他
	* @return String
	*/
  public String getUsageCode() {
    return this.usageCode == null ? "" : this.usageCode;
  }

/**
	* 資金用途別<br>
	* 共用代碼檔
01:週轉金
02:購置不動產
03:營業用資產
04:固定資產
05:企業投資
06:購置動產
09:其他
  *
  * @param usageCode 資金用途別
	*/
  public void setUsageCode(String usageCode) {
    this.usageCode = usageCode;
  }

/**
	* 聯貸案序號<br>
	* 
	* @return Integer
	*/
  public int getSyndNo() {
    return this.syndNo;
  }

/**
	* 聯貸案序號<br>
	* 
  *
  * @param syndNo 聯貸案序號
	*/
  public void setSyndNo(int syndNo) {
    this.syndNo = syndNo;
  }

/**
	* 與借款人關係<br>
	* 共用代碼檔
00:本人
01:夫
02:妻
03:父
04:母
05:子
06:女
07:兄
08:弟
09:姊
10:妹
11:姪子
99:其他
	* @return String
	*/
  public String getRelationCode() {
    return this.relationCode == null ? "" : this.relationCode;
  }

/**
	* 與借款人關係<br>
	* 共用代碼檔
00:本人
01:夫
02:妻
03:父
04:母
05:子
06:女
07:兄
08:弟
09:姊
10:妹
11:姪子
99:其他
  *
  * @param relationCode 與借款人關係
	*/
  public void setRelationCode(String relationCode) {
    this.relationCode = relationCode;
  }

/**
	* 第三人帳戶戶名<br>
	* 
	* @return String
	*/
  public String getRelationName() {
    return this.relationName == null ? "" : this.relationName;
  }

/**
	* 第三人帳戶戶名<br>
	* 
  *
  * @param relationName 第三人帳戶戶名
	*/
  public void setRelationName(String relationName) {
    this.relationName = relationName;
  }

/**
	* 第三人身份證字號<br>
	* 
	* @return String
	*/
  public String getRelationId() {
    return this.relationId == null ? "" : this.relationId;
  }

/**
	* 第三人身份證字號<br>
	* 
  *
  * @param relationId 第三人身份證字號
	*/
  public void setRelationId(String relationId) {
    this.relationId = relationId;
  }

/**
	* 第三人生日<br>
	* 
	* @return Integer
	*/
  public int getRelationBirthday() {
    return StaticTool.bcToRoc(this.relationBirthday);
  }

/**
	* 第三人生日<br>
	* 
  *
  * @param relationBirthday 第三人生日
  * @throws LogicException when Date Is Warn	*/
  public void setRelationBirthday(int relationBirthday) throws LogicException {
    this.relationBirthday = StaticTool.rocToBc(relationBirthday);
  }

/**
	* 第三人性別<br>
	* 1:男
2:女
	* @return String
	*/
  public String getRelationGender() {
    return this.relationGender == null ? "" : this.relationGender;
  }

/**
	* 第三人性別<br>
	* 1:男
2:女
  *
  * @param relationGender 第三人性別
	*/
  public void setRelationGender(String relationGender) {
    this.relationGender = relationGender;
  }

/**
	* 交易進行記號<br>
	* 0:1STEP TX
1/2:2STEP TX
1/2/3/4:3STEP TX
	* @return Integer
	*/
  public int getActFg() {
    return this.actFg;
  }

/**
	* 交易進行記號<br>
	* 0:1STEP TX
1/2:2STEP TX
1/2/3/4:3STEP TX
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
  public int getLastEntDy() {
    return StaticTool.bcToRoc(this.lastEntDy);
  }

/**
	* 上次交易日<br>
	* 更正時, 檢查是否為最近一筆交易
  *
  * @param lastEntDy 上次交易日
  * @throws LogicException when Date Is Warn	*/
  public void setLastEntDy(int lastEntDy) throws LogicException {
    this.lastEntDy = StaticTool.rocToBc(lastEntDy);
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
	* 匯款銀行<br>
	* 預約撥款匯款資料
	* @return String
	*/
  public String getRemitBank() {
    return this.remitBank == null ? "" : this.remitBank;
  }

/**
	* 匯款銀行<br>
	* 預約撥款匯款資料
  *
  * @param remitBank 匯款銀行
	*/
  public void setRemitBank(String remitBank) {
    this.remitBank = remitBank;
  }

/**
	* 匯款分行<br>
	* 
	* @return String
	*/
  public String getRemitBranch() {
    return this.remitBranch == null ? "" : this.remitBranch;
  }

/**
	* 匯款分行<br>
	* 
  *
  * @param remitBranch 匯款分行
	*/
  public void setRemitBranch(String remitBranch) {
    this.remitBranch = remitBranch;
  }

/**
	* 匯款帳號<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getRemitAcctNo() {
    return this.remitAcctNo;
  }

/**
	* 匯款帳號<br>
	* 
  *
  * @param remitAcctNo 匯款帳號
	*/
  public void setRemitAcctNo(BigDecimal remitAcctNo) {
    this.remitAcctNo = remitAcctNo;
  }

/**
	* 匯款戶名(代償專戶)<br>
	* 
	* @return String
	*/
  public String getCompensateAcct() {
    return this.compensateAcct == null ? "" : this.compensateAcct;
  }

/**
	* 匯款戶名(代償專戶)<br>
	* 
  *
  * @param compensateAcct 匯款戶名(代償專戶)
	*/
  public void setCompensateAcct(String compensateAcct) {
    this.compensateAcct = compensateAcct;
  }

/**
	* 解付單位代號<br>
	* 
	* @return String
	*/
  public String getPaymentBank() {
    return this.paymentBank == null ? "" : this.paymentBank;
  }

/**
	* 解付單位代號<br>
	* 
  *
  * @param paymentBank 解付單位代號
	*/
  public void setPaymentBank(String paymentBank) {
    this.paymentBank = paymentBank;
  }

/**
	* 附言<br>
	* 
	* @return String
	*/
  public String getRemark() {
    return this.remark == null ? "" : this.remark;
  }

/**
	* 附言<br>
	* 
  *
  * @param remark 附言
	*/
  public void setRemark(String remark) {
    this.remark = remark;
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
	* 次日交易會計日期<br>
	* 
	* @return Integer
	*/
  public int getNextAcDate() {
    return StaticTool.bcToRoc(this.nextAcDate);
  }

/**
	* 次日交易會計日期<br>
	* 
  *
  * @param nextAcDate 次日交易會計日期
  * @throws LogicException when Date Is Warn	*/
  public void setNextAcDate(int nextAcDate) throws LogicException {
    this.nextAcDate = StaticTool.rocToBc(nextAcDate);
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
	* 寬限區分<br>
	* LA$LMSP.LMSGTP
0:不寬限
1:寬限
	* @return Integer
	*/
  public int getGraceFlag() {
    return this.graceFlag;
  }

/**
	* 寬限區分<br>
	* LA$LMSP.LMSGTP
0:不寬限
1:寬限
  *
  * @param graceFlag 寬限區分
	*/
  public void setGraceFlag(int graceFlag) {
    this.graceFlag = graceFlag;
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
    return "LoanBorMain [loanBorMainId=" + loanBorMainId + ", lastBorxNo=" + lastBorxNo + ", lastOvduNo=" + lastOvduNo + ", status=" + status
           + ", rateIncr=" + rateIncr + ", individualIncr=" + individualIncr + ", approveRate=" + approveRate + ", storeRate=" + storeRate + ", rateCode=" + rateCode + ", rateAdjFreq=" + rateAdjFreq
           + ", drawdownCode=" + drawdownCode + ", currencyCode=" + currencyCode + ", drawdownAmt=" + drawdownAmt + ", loanBal=" + loanBal + ", drawdownDate=" + drawdownDate + ", loanTermYy=" + loanTermYy
           + ", loanTermMm=" + loanTermMm + ", loanTermDd=" + loanTermDd + ", maturityDate=" + maturityDate + ", intCalcCode=" + intCalcCode + ", amortizedCode=" + amortizedCode + ", freqBase=" + freqBase
           + ", payIntFreq=" + payIntFreq + ", repayFreq=" + repayFreq + ", totalPeriod=" + totalPeriod + ", repaidPeriod=" + repaidPeriod + ", paidTerms=" + paidTerms + ", prevPayIntDate=" + prevPayIntDate
           + ", prevRepaidDate=" + prevRepaidDate + ", nextPayIntDate=" + nextPayIntDate + ", nextRepayDate=" + nextRepayDate + ", dueAmt=" + dueAmt + ", gracePeriod=" + gracePeriod + ", graceDate=" + graceDate
           + ", specificDd=" + specificDd + ", specificDate=" + specificDate + ", firstDueDate=" + firstDueDate + ", firstAdjRateDate=" + firstAdjRateDate + ", nextAdjRateDate=" + nextAdjRateDate + ", acctFee=" + acctFee
           + ", handlingFee=" + handlingFee + ", finalBal=" + finalBal + ", notYetFlag=" + notYetFlag + ", renewFlag=" + renewFlag + ", pieceCode=" + pieceCode + ", pieceCodeSecond=" + pieceCodeSecond
           + ", pieceCodeSecondAmt=" + pieceCodeSecondAmt + ", usageCode=" + usageCode + ", syndNo=" + syndNo + ", relationCode=" + relationCode + ", relationName=" + relationName + ", relationId=" + relationId
           + ", relationBirthday=" + relationBirthday + ", relationGender=" + relationGender + ", actFg=" + actFg + ", lastEntDy=" + lastEntDy + ", lastKinbr=" + lastKinbr + ", lastTlrNo=" + lastTlrNo
           + ", lastTxtNo=" + lastTxtNo + ", remitBank=" + remitBank + ", remitBranch=" + remitBranch + ", remitAcctNo=" + remitAcctNo + ", compensateAcct=" + compensateAcct + ", paymentBank=" + paymentBank
           + ", remark=" + remark + ", acDate=" + acDate + ", nextAcDate=" + nextAcDate + ", branchNo=" + branchNo + ", graceFlag=" + graceFlag + ", createDate=" + createDate
           + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
