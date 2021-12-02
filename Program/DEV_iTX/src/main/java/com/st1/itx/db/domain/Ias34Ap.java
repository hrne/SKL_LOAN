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
 * Ias34Ap IAS34欄位清單A檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`Ias34Ap`")
public class Ias34Ap implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -8353793754471558496L;

@EmbeddedId
  private Ias34ApId ias34ApId;

  // 年月份
  @Column(name = "`DataYM`", insertable = false, updatable = false)
  private int dataYM = 0;

  // 戶號
  @Column(name = "`CustNo`", insertable = false, updatable = false)
  private int custNo = 0;

  // 借款人ID / 統編
  @Column(name = "`CustId`", length = 10)
  private String custId;

  // 額度編號
  @Column(name = "`FacmNo`", insertable = false, updatable = false)
  private int facmNo = 0;

  // 核准號碼
  @Column(name = "`ApplNo`")
  private int applNo = 0;

  // 撥款序號
  @Column(name = "`BormNo`", insertable = false, updatable = false)
  private int bormNo = 0;

  // 會計科目
  @Column(name = "`AcCode`", length = 11)
  private String acCode;

  // 戶況
  /* 1=正常2=催收 */
  @Column(name = "`Status`")
  private int status = 0;

  // 初貸日期
  /* 額度初貸日 */
  @Column(name = "`FirstDrawdownDate`")
  private int firstDrawdownDate = 0;

  // 撥款日期
  @Column(name = "`DrawdownDate`")
  private int drawdownDate = 0;

  // 到期日(額度)
  @Column(name = "`FacLineDate`")
  private int facLineDate = 0;

  // 到期日(撥款)
  @Column(name = "`MaturityDate`")
  private int maturityDate = 0;

  // 核准金額
  /* 每額度編號項下之放款帳號皆同核准額度金額 */
  @Column(name = "`LineAmt`")
  private BigDecimal lineAmt = new BigDecimal("0");

  // 撥款金額
  @Column(name = "`DrawdownAmt`")
  private BigDecimal drawdownAmt = new BigDecimal("0");

  // 帳管費
  @Column(name = "`AcctFee`")
  private BigDecimal acctFee = new BigDecimal("0");

  // 本金餘額(撥款)
  @Column(name = "`LoanBal`")
  private BigDecimal loanBal = new BigDecimal("0");

  // 應收利息
  /* 計算至每月月底之撥款應收利息 */
  @Column(name = "`IntAmt`")
  private BigDecimal intAmt = new BigDecimal("0");

  // 法拍及火險費用
  @Column(name = "`Fee`")
  private BigDecimal fee = new BigDecimal("0");

  // 利率(撥款)
  /* 抓取月底時適用利率至小數點後第6位。例如，利率為2.1234%，則本欄位值表示0.021234 (台幣放款無法蒐集此欄位則以空白表示) */
  @Column(name = "`Rate`")
  private BigDecimal rate = new BigDecimal("0");

  // 逾期繳款天數
  /* 抓取月底日資料，並以天數表示 */
  @Column(name = "`OvduDays`")
  private int ovduDays = 0;

  // 轉催收款日期
  /* 抓取最近一次的轉催收日期 */
  @Column(name = "`OvduDate`")
  private int ovduDate = 0;

  // 轉銷呆帳日期
  /* 最早之轉銷呆帳日期 */
  @Column(name = "`BadDebtDate`")
  private int badDebtDate = 0;

  // 轉銷呆帳金額
  /* 無論轉呆次數，計算全部轉銷呆帳之金額 */
  @Column(name = "`BadDebtAmt`")
  private BigDecimal badDebtAmt = new BigDecimal("0");

  // 符合減損客觀證據之條件
  /* 1,2,3,4,5,6,7… */
  @Column(name = "`DerCode`")
  private int derCode = 0;

  // 初貸時約定還本寬限期
  /* 約定客戶得只繳息不繳本之寬限期。以月為單位，例如3年寬限期，則本欄位值為36 */
  @Column(name = "`GracePeriod`")
  private int gracePeriod = 0;

  // 核准利率
  @Column(name = "`ApproveRate`")
  private BigDecimal approveRate = new BigDecimal("0");

  // 契約當時還款方式
  /* 1=按期繳息(到期還本)；2=平均攤還本息；3=平均攤還本金；4=到期繳息還本 */
  @Column(name = "`AmortizedCode`")
  private int amortizedCode = 0;

  // 契約當時利率調整方式
  /* 1=機動；2=固定；3=固定階梯；4=浮動階梯 */
  @Column(name = "`RateCode`")
  private int rateCode = 0;

  // 契約約定當時還本週期
  /* 若為到期還本，則填入0；若按月還本，則填入1；季繳，3；半年，6；年繳，12。 */
  @Column(name = "`RepayFreq`")
  private int repayFreq = 0;

  // 契約約定當時繳息週期
  /* 若為到期繳息，則填入0；若按月還本，則填入1；季繳，3；半年，6；年繳，12。 */
  @Column(name = "`PayIntFreq`")
  private int payIntFreq = 0;

  // 授信行業別
  @Column(name = "`IndustryCode`", length = 6)
  private String industryCode;

  // 擔保品類別
  /* 以對應至JCIC的類別 */
  @Column(name = "`ClTypeJCIC`", length = 2)
  private String clTypeJCIC;

  // 擔保品地區別
  /* 擔保品郵遞區號 */
  @Column(name = "`Zip3`", length = 3)
  private String zip3;

  // 商品利率代碼
  /* (新系統改為5) */
  @Column(name = "`ProdNo`", length = 5)
  private String prodNo;

  // 企業戶/個人戶
  /* 1=企業戶2=個人戶 */
  @Column(name = "`CustKind`")
  private int custKind = 0;

  // 五類資產分類
  @Column(name = "`AssetClass`")
  private int assetClass = 0;

  // 產品別
  /* 作為群組分類。Ex:1=員工；2=車貸；3＝房貸；4＝政府優惠貸款…etc */
  @Column(name = "`Ifrs9ProdCode`", length = 1)
  private String ifrs9ProdCode;

  // 原始鑑價金額
  @Column(name = "`EvaAmt`")
  private BigDecimal evaAmt = new BigDecimal("0");

  // 首次應繳日
  @Column(name = "`FirstDueDate`")
  private int firstDueDate = 0;

  // 總期數
  @Column(name = "`TotalPeriod`")
  private int totalPeriod = 0;

  // 協議前之額度編號
  @Column(name = "`AgreeBefFacmNo`")
  private int agreeBefFacmNo = 0;

  // 協議前之撥款序號
  @Column(name = "`AgreeBefBormNo`")
  private int agreeBefBormNo = 0;

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


  public Ias34ApId getIas34ApId() {
    return this.ias34ApId;
  }

  public void setIas34ApId(Ias34ApId ias34ApId) {
    this.ias34ApId = ias34ApId;
  }

/**
	* 年月份<br>
	* 
	* @return Integer
	*/
  public int getDataYM() {
    return this.dataYM;
  }

/**
	* 年月份<br>
	* 
  *
  * @param dataYM 年月份
	*/
  public void setDataYM(int dataYM) {
    this.dataYM = dataYM;
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
	* 借款人ID / 統編<br>
	* 
	* @return String
	*/
  public String getCustId() {
    return this.custId == null ? "" : this.custId;
  }

/**
	* 借款人ID / 統編<br>
	* 
  *
  * @param custId 借款人ID / 統編
	*/
  public void setCustId(String custId) {
    this.custId = custId;
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
	* 核准號碼<br>
	* 
	* @return Integer
	*/
  public int getApplNo() {
    return this.applNo;
  }

/**
	* 核准號碼<br>
	* 
  *
  * @param applNo 核准號碼
	*/
  public void setApplNo(int applNo) {
    this.applNo = applNo;
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
	* 會計科目<br>
	* 
	* @return String
	*/
  public String getAcCode() {
    return this.acCode == null ? "" : this.acCode;
  }

/**
	* 會計科目<br>
	* 
  *
  * @param acCode 會計科目
	*/
  public void setAcCode(String acCode) {
    this.acCode = acCode;
  }

/**
	* 戶況<br>
	* 1=正常
2=催收
	* @return Integer
	*/
  public int getStatus() {
    return this.status;
  }

/**
	* 戶況<br>
	* 1=正常
2=催收
  *
  * @param status 戶況
	*/
  public void setStatus(int status) {
    this.status = status;
  }

/**
	* 初貸日期<br>
	* 額度初貸日
	* @return Integer
	*/
  public int getFirstDrawdownDate() {
    return StaticTool.bcToRoc(this.firstDrawdownDate);
  }

/**
	* 初貸日期<br>
	* 額度初貸日
  *
  * @param firstDrawdownDate 初貸日期
  * @throws LogicException when Date Is Warn	*/
  public void setFirstDrawdownDate(int firstDrawdownDate) throws LogicException {
    this.firstDrawdownDate = StaticTool.rocToBc(firstDrawdownDate);
  }

/**
	* 撥款日期<br>
	* 
	* @return Integer
	*/
  public int getDrawdownDate() {
    return StaticTool.bcToRoc(this.drawdownDate);
  }

/**
	* 撥款日期<br>
	* 
  *
  * @param drawdownDate 撥款日期
  * @throws LogicException when Date Is Warn	*/
  public void setDrawdownDate(int drawdownDate) throws LogicException {
    this.drawdownDate = StaticTool.rocToBc(drawdownDate);
  }

/**
	* 到期日(額度)<br>
	* 
	* @return Integer
	*/
  public int getFacLineDate() {
    return StaticTool.bcToRoc(this.facLineDate);
  }

/**
	* 到期日(額度)<br>
	* 
  *
  * @param facLineDate 到期日(額度)
  * @throws LogicException when Date Is Warn	*/
  public void setFacLineDate(int facLineDate) throws LogicException {
    this.facLineDate = StaticTool.rocToBc(facLineDate);
  }

/**
	* 到期日(撥款)<br>
	* 
	* @return Integer
	*/
  public int getMaturityDate() {
    return StaticTool.bcToRoc(this.maturityDate);
  }

/**
	* 到期日(撥款)<br>
	* 
  *
  * @param maturityDate 到期日(撥款)
  * @throws LogicException when Date Is Warn	*/
  public void setMaturityDate(int maturityDate) throws LogicException {
    this.maturityDate = StaticTool.rocToBc(maturityDate);
  }

/**
	* 核准金額<br>
	* 每額度編號項下之放款帳號皆同核准額度金額
	* @return BigDecimal
	*/
  public BigDecimal getLineAmt() {
    return this.lineAmt;
  }

/**
	* 核准金額<br>
	* 每額度編號項下之放款帳號皆同核准額度金額
  *
  * @param lineAmt 核准金額
	*/
  public void setLineAmt(BigDecimal lineAmt) {
    this.lineAmt = lineAmt;
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
	* 本金餘額(撥款)<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLoanBal() {
    return this.loanBal;
  }

/**
	* 本金餘額(撥款)<br>
	* 
  *
  * @param loanBal 本金餘額(撥款)
	*/
  public void setLoanBal(BigDecimal loanBal) {
    this.loanBal = loanBal;
  }

/**
	* 應收利息<br>
	* 計算至每月月底之撥款應收利息
	* @return BigDecimal
	*/
  public BigDecimal getIntAmt() {
    return this.intAmt;
  }

/**
	* 應收利息<br>
	* 計算至每月月底之撥款應收利息
  *
  * @param intAmt 應收利息
	*/
  public void setIntAmt(BigDecimal intAmt) {
    this.intAmt = intAmt;
  }

/**
	* 法拍及火險費用<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getFee() {
    return this.fee;
  }

/**
	* 法拍及火險費用<br>
	* 
  *
  * @param fee 法拍及火險費用
	*/
  public void setFee(BigDecimal fee) {
    this.fee = fee;
  }

/**
	* 利率(撥款)<br>
	* 抓取月底時適用利率
至小數點後第6位。例如，利率為2.1234%，則本欄位值表示0.021234 (台幣放款無法蒐集此欄位則以空白表示)
	* @return BigDecimal
	*/
  public BigDecimal getRate() {
    return this.rate;
  }

/**
	* 利率(撥款)<br>
	* 抓取月底時適用利率
至小數點後第6位。例如，利率為2.1234%，則本欄位值表示0.021234 (台幣放款無法蒐集此欄位則以空白表示)
  *
  * @param rate 利率(撥款)
	*/
  public void setRate(BigDecimal rate) {
    this.rate = rate;
  }

/**
	* 逾期繳款天數<br>
	* 抓取月底日資料，並以天數表示
	* @return Integer
	*/
  public int getOvduDays() {
    return this.ovduDays;
  }

/**
	* 逾期繳款天數<br>
	* 抓取月底日資料，並以天數表示
  *
  * @param ovduDays 逾期繳款天數
	*/
  public void setOvduDays(int ovduDays) {
    this.ovduDays = ovduDays;
  }

/**
	* 轉催收款日期<br>
	* 抓取最近一次的轉催收日期
	* @return Integer
	*/
  public int getOvduDate() {
    return StaticTool.bcToRoc(this.ovduDate);
  }

/**
	* 轉催收款日期<br>
	* 抓取最近一次的轉催收日期
  *
  * @param ovduDate 轉催收款日期
  * @throws LogicException when Date Is Warn	*/
  public void setOvduDate(int ovduDate) throws LogicException {
    this.ovduDate = StaticTool.rocToBc(ovduDate);
  }

/**
	* 轉銷呆帳日期<br>
	* 最早之轉銷呆帳日期
	* @return Integer
	*/
  public int getBadDebtDate() {
    return StaticTool.bcToRoc(this.badDebtDate);
  }

/**
	* 轉銷呆帳日期<br>
	* 最早之轉銷呆帳日期
  *
  * @param badDebtDate 轉銷呆帳日期
  * @throws LogicException when Date Is Warn	*/
  public void setBadDebtDate(int badDebtDate) throws LogicException {
    this.badDebtDate = StaticTool.rocToBc(badDebtDate);
  }

/**
	* 轉銷呆帳金額<br>
	* 無論轉呆次數，計算全部轉銷呆帳之金額
	* @return BigDecimal
	*/
  public BigDecimal getBadDebtAmt() {
    return this.badDebtAmt;
  }

/**
	* 轉銷呆帳金額<br>
	* 無論轉呆次數，計算全部轉銷呆帳之金額
  *
  * @param badDebtAmt 轉銷呆帳金額
	*/
  public void setBadDebtAmt(BigDecimal badDebtAmt) {
    this.badDebtAmt = badDebtAmt;
  }

/**
	* 符合減損客觀證據之條件<br>
	* 1,2,3,4,5,6,7…
	* @return Integer
	*/
  public int getDerCode() {
    return this.derCode;
  }

/**
	* 符合減損客觀證據之條件<br>
	* 1,2,3,4,5,6,7…
  *
  * @param derCode 符合減損客觀證據之條件
	*/
  public void setDerCode(int derCode) {
    this.derCode = derCode;
  }

/**
	* 初貸時約定還本寬限期<br>
	* 約定客戶得只繳息不繳本之寬限期。
以月為單位，例如3年寬限期，則本欄位值為36
	* @return Integer
	*/
  public int getGracePeriod() {
    return this.gracePeriod;
  }

/**
	* 初貸時約定還本寬限期<br>
	* 約定客戶得只繳息不繳本之寬限期。
以月為單位，例如3年寬限期，則本欄位值為36
  *
  * @param gracePeriod 初貸時約定還本寬限期
	*/
  public void setGracePeriod(int gracePeriod) {
    this.gracePeriod = gracePeriod;
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
	* 契約當時還款方式<br>
	* 1=按期繳息(到期還本)；
2=平均攤還本息；
3=平均攤還本金；
4=到期繳息還本
	* @return Integer
	*/
  public int getAmortizedCode() {
    return this.amortizedCode;
  }

/**
	* 契約當時還款方式<br>
	* 1=按期繳息(到期還本)；
2=平均攤還本息；
3=平均攤還本金；
4=到期繳息還本
  *
  * @param amortizedCode 契約當時還款方式
	*/
  public void setAmortizedCode(int amortizedCode) {
    this.amortizedCode = amortizedCode;
  }

/**
	* 契約當時利率調整方式<br>
	* 1=機動；
2=固定；
3=固定階梯；
4=浮動階梯
	* @return Integer
	*/
  public int getRateCode() {
    return this.rateCode;
  }

/**
	* 契約當時利率調整方式<br>
	* 1=機動；
2=固定；
3=固定階梯；
4=浮動階梯
  *
  * @param rateCode 契約當時利率調整方式
	*/
  public void setRateCode(int rateCode) {
    this.rateCode = rateCode;
  }

/**
	* 契約約定當時還本週期<br>
	* 若為到期還本，則填入0；
若按月還本，則填入1；
季繳，3；
半年，6；
年繳，12。
	* @return Integer
	*/
  public int getRepayFreq() {
    return this.repayFreq;
  }

/**
	* 契約約定當時還本週期<br>
	* 若為到期還本，則填入0；
若按月還本，則填入1；
季繳，3；
半年，6；
年繳，12。
  *
  * @param repayFreq 契約約定當時還本週期
	*/
  public void setRepayFreq(int repayFreq) {
    this.repayFreq = repayFreq;
  }

/**
	* 契約約定當時繳息週期<br>
	* 若為到期繳息，則填入0；
若按月還本，則填入1；
季繳，3；
半年，6；
年繳，12。
	* @return Integer
	*/
  public int getPayIntFreq() {
    return this.payIntFreq;
  }

/**
	* 契約約定當時繳息週期<br>
	* 若為到期繳息，則填入0；
若按月還本，則填入1；
季繳，3；
半年，6；
年繳，12。
  *
  * @param payIntFreq 契約約定當時繳息週期
	*/
  public void setPayIntFreq(int payIntFreq) {
    this.payIntFreq = payIntFreq;
  }

/**
	* 授信行業別<br>
	* 
	* @return String
	*/
  public String getIndustryCode() {
    return this.industryCode == null ? "" : this.industryCode;
  }

/**
	* 授信行業別<br>
	* 
  *
  * @param industryCode 授信行業別
	*/
  public void setIndustryCode(String industryCode) {
    this.industryCode = industryCode;
  }

/**
	* 擔保品類別<br>
	* 以對應至JCIC的類別
	* @return String
	*/
  public String getClTypeJCIC() {
    return this.clTypeJCIC == null ? "" : this.clTypeJCIC;
  }

/**
	* 擔保品類別<br>
	* 以對應至JCIC的類別
  *
  * @param clTypeJCIC 擔保品類別
	*/
  public void setClTypeJCIC(String clTypeJCIC) {
    this.clTypeJCIC = clTypeJCIC;
  }

/**
	* 擔保品地區別<br>
	* 擔保品郵遞區號
	* @return String
	*/
  public String getZip3() {
    return this.zip3 == null ? "" : this.zip3;
  }

/**
	* 擔保品地區別<br>
	* 擔保品郵遞區號
  *
  * @param zip3 擔保品地區別
	*/
  public void setZip3(String zip3) {
    this.zip3 = zip3;
  }

/**
	* 商品利率代碼<br>
	* (新系統改為5)
	* @return String
	*/
  public String getProdNo() {
    return this.prodNo == null ? "" : this.prodNo;
  }

/**
	* 商品利率代碼<br>
	* (新系統改為5)
  *
  * @param prodNo 商品利率代碼
	*/
  public void setProdNo(String prodNo) {
    this.prodNo = prodNo;
  }

/**
	* 企業戶/個人戶<br>
	* 1=企業戶
2=個人戶
	* @return Integer
	*/
  public int getCustKind() {
    return this.custKind;
  }

/**
	* 企業戶/個人戶<br>
	* 1=企業戶
2=個人戶
  *
  * @param custKind 企業戶/個人戶
	*/
  public void setCustKind(int custKind) {
    this.custKind = custKind;
  }

/**
	* 五類資產分類<br>
	* 
	* @return Integer
	*/
  public int getAssetClass() {
    return this.assetClass;
  }

/**
	* 五類資產分類<br>
	* 
  *
  * @param assetClass 五類資產分類
	*/
  public void setAssetClass(int assetClass) {
    this.assetClass = assetClass;
  }

/**
	* 產品別<br>
	* 作為群組分類。Ex:1=員工；2=車貸；3＝房貸；4＝政府優惠貸款…etc
	* @return String
	*/
  public String getIfrs9ProdCode() {
    return this.ifrs9ProdCode == null ? "" : this.ifrs9ProdCode;
  }

/**
	* 產品別<br>
	* 作為群組分類。Ex:1=員工；2=車貸；3＝房貸；4＝政府優惠貸款…etc
  *
  * @param ifrs9ProdCode 產品別
	*/
  public void setIfrs9ProdCode(String ifrs9ProdCode) {
    this.ifrs9ProdCode = ifrs9ProdCode;
  }

/**
	* 原始鑑價金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getEvaAmt() {
    return this.evaAmt;
  }

/**
	* 原始鑑價金額<br>
	* 
  *
  * @param evaAmt 原始鑑價金額
	*/
  public void setEvaAmt(BigDecimal evaAmt) {
    this.evaAmt = evaAmt;
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
	* 協議前之額度編號<br>
	* 
	* @return Integer
	*/
  public int getAgreeBefFacmNo() {
    return this.agreeBefFacmNo;
  }

/**
	* 協議前之額度編號<br>
	* 
  *
  * @param agreeBefFacmNo 協議前之額度編號
	*/
  public void setAgreeBefFacmNo(int agreeBefFacmNo) {
    this.agreeBefFacmNo = agreeBefFacmNo;
  }

/**
	* 協議前之撥款序號<br>
	* 
	* @return Integer
	*/
  public int getAgreeBefBormNo() {
    return this.agreeBefBormNo;
  }

/**
	* 協議前之撥款序號<br>
	* 
  *
  * @param agreeBefBormNo 協議前之撥款序號
	*/
  public void setAgreeBefBormNo(int agreeBefBormNo) {
    this.agreeBefBormNo = agreeBefBormNo;
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
    return "Ias34Ap [ias34ApId=" + ias34ApId + ", custId=" + custId + ", applNo=" + applNo
           + ", acCode=" + acCode + ", status=" + status + ", firstDrawdownDate=" + firstDrawdownDate + ", drawdownDate=" + drawdownDate + ", facLineDate=" + facLineDate + ", maturityDate=" + maturityDate
           + ", lineAmt=" + lineAmt + ", drawdownAmt=" + drawdownAmt + ", acctFee=" + acctFee + ", loanBal=" + loanBal + ", intAmt=" + intAmt + ", fee=" + fee
           + ", rate=" + rate + ", ovduDays=" + ovduDays + ", ovduDate=" + ovduDate + ", badDebtDate=" + badDebtDate + ", badDebtAmt=" + badDebtAmt + ", derCode=" + derCode
           + ", gracePeriod=" + gracePeriod + ", approveRate=" + approveRate + ", amortizedCode=" + amortizedCode + ", rateCode=" + rateCode + ", repayFreq=" + repayFreq + ", payIntFreq=" + payIntFreq
           + ", industryCode=" + industryCode + ", clTypeJCIC=" + clTypeJCIC + ", zip3=" + zip3 + ", prodNo=" + prodNo + ", custKind=" + custKind + ", assetClass=" + assetClass
           + ", ifrs9ProdCode=" + ifrs9ProdCode + ", evaAmt=" + evaAmt + ", firstDueDate=" + firstDueDate + ", totalPeriod=" + totalPeriod + ", agreeBefFacmNo=" + agreeBefFacmNo + ", agreeBefBormNo=" + agreeBefBormNo
           + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
