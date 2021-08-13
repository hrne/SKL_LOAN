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
 * Ifrs9LoanData IFRS9撥款資料檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`Ifrs9LoanData`")
public class Ifrs9LoanData implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 6224771484377512593L;

@EmbeddedId
  private Ifrs9LoanDataId ifrs9LoanDataId;

  // 年月份
  @Column(name = "`DataYM`", insertable = false, updatable = false)
  private int dataYM = 0;

  // 戶號
  @Column(name = "`CustNo`", insertable = false, updatable = false)
  private int custNo = 0;

  // 額度編號
  @Column(name = "`FacmNo`", insertable = false, updatable = false)
  private int facmNo = 0;

  // 撥款序號
  @Column(name = "`BormNo`", insertable = false, updatable = false)
  private int bormNo = 0;

  // 借款人ID / 統編
  @Column(name = "`CustId`", length = 10)
  private String custId;

  // 業務科目代號
  /* 310: 短期擔保放款 320: 中期擔保放款330: 長期擔保放款340: 三十年房貸990: 催收款項 */
  @Column(name = "`AcctCode`", length = 3)
  private String acctCode;

  // 新會計科目(11碼)
  @Column(name = "`AcCode`", length = 11)
  private String acCode;

  // 舊會計科目(8碼)
  @Column(name = "`AcCodeOld`", length = 8)
  private String acCodeOld;

  // 戶況
  /* 0: 正常戶2: 催收戶3: 結案戶7: 部分轉呆戶 */
  @Column(name = "`Status`")
  private int status = 0;

  // 撥款日期
  @Column(name = "`DrawdownDate`")
  private int drawdownDate = 0;

  // 到期日(撥款)
  /* 案件到期日 */
  @Column(name = "`MaturityDate`")
  private int maturityDate = 0;

  // 撥款金額
  @Column(name = "`DrawdownAmt`")
  private BigDecimal drawdownAmt = new BigDecimal("0");

  // 本金餘額(撥款)
  @Column(name = "`LoanBal`")
  private BigDecimal loanBal = new BigDecimal("0");

  // 應收利息
  /* 計算至每月月底之撥款應收利息 */
  @Column(name = "`IntAmt`")
  private BigDecimal intAmt = new BigDecimal("0");

  // 利率(撥款)
  /* 抓取月底時適用利率 */
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

  // 核准利率
  @Column(name = "`ApproveRate`")
  private BigDecimal approveRate = new BigDecimal("0");

  // 契約當時還款方式(月底日)
  /* 1: 按月繳息(按期繳息到期還本)；2: 到期取息(到期繳息還本)；3: 本息平均法(期金)；4: 本金平均法；5: 按月撥款收息(逆向貸款)； */
  @Column(name = "`AmortizedCode`", length = 1)
  private String amortizedCode;

  // 契約當時利率調整方式(月底日)
  /* 1: 機動;2: 固定；3: 定期機動； */
  @Column(name = "`RateCode`", length = 1)
  private String rateCode;

  // 契約約定當時還本週期(月底日)
  /* 若為到期還本，則填入0；若按月還本，則填入1；季繳，3；半年，6；年繳,12。 */
  @Column(name = "`RepayFreq`")
  private int repayFreq = 0;

  // 契約約定當時繳息週期(月底日)
  /* 若為到期繳息，則填入0；若按月還本，則填入1；季繳，3；半年，6；年繳,12。 */
  @Column(name = "`PayIntFreq`")
  private int payIntFreq = 0;

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

  // 帳冊別
  /* 1=一般;2=分紅;3=利變;4=OIU */
  @Column(name = "`AcBookCode`", length = 1)
  private String acBookCode;

  // 上次繳息日(繳息迄日)
  @Column(name = "`PrevPayIntDate`")
  private int prevPayIntDate = 0;

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


  public Ifrs9LoanDataId getIfrs9LoanDataId() {
    return this.ifrs9LoanDataId;
  }

  public void setIfrs9LoanDataId(Ifrs9LoanDataId ifrs9LoanDataId) {
    this.ifrs9LoanDataId = ifrs9LoanDataId;
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
	* 業務科目代號<br>
	* 310: 短期擔保放款 
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
	* 310: 短期擔保放款 
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
	* 新會計科目(11碼)<br>
	* 
	* @return String
	*/
  public String getAcCode() {
    return this.acCode == null ? "" : this.acCode;
  }

/**
	* 新會計科目(11碼)<br>
	* 
  *
  * @param acCode 新會計科目(11碼)
	*/
  public void setAcCode(String acCode) {
    this.acCode = acCode;
  }

/**
	* 舊會計科目(8碼)<br>
	* 
	* @return String
	*/
  public String getAcCodeOld() {
    return this.acCodeOld == null ? "" : this.acCodeOld;
  }

/**
	* 舊會計科目(8碼)<br>
	* 
  *
  * @param acCodeOld 舊會計科目(8碼)
	*/
  public void setAcCodeOld(String acCodeOld) {
    this.acCodeOld = acCodeOld;
  }

/**
	* 戶況<br>
	* 0: 正常戶
2: 催收戶
3: 結案戶
7: 部分轉呆戶
	* @return Integer
	*/
  public int getStatus() {
    return this.status;
  }

/**
	* 戶況<br>
	* 0: 正常戶
2: 催收戶
3: 結案戶
7: 部分轉呆戶
  *
  * @param status 戶況
	*/
  public void setStatus(int status) {
    this.status = status;
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
	* 到期日(撥款)<br>
	* 案件到期日
	* @return Integer
	*/
  public int getMaturityDate() {
    return StaticTool.bcToRoc(this.maturityDate);
  }

/**
	* 到期日(撥款)<br>
	* 案件到期日
  *
  * @param maturityDate 到期日(撥款)
  * @throws LogicException when Date Is Warn	*/
  public void setMaturityDate(int maturityDate) throws LogicException {
    this.maturityDate = StaticTool.rocToBc(maturityDate);
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
	* 利率(撥款)<br>
	* 抓取月底時適用利率
	* @return BigDecimal
	*/
  public BigDecimal getRate() {
    return this.rate;
  }

/**
	* 利率(撥款)<br>
	* 抓取月底時適用利率
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
	* 契約當時還款方式(月底日)<br>
	* 1: 按月繳息(按期繳息到期還本)；
2: 到期取息(到期繳息還本)；
3: 本息平均法(期金)；
4: 本金平均法；
5: 按月撥款收息(逆向貸款)；
	* @return String
	*/
  public String getAmortizedCode() {
    return this.amortizedCode == null ? "" : this.amortizedCode;
  }

/**
	* 契約當時還款方式(月底日)<br>
	* 1: 按月繳息(按期繳息到期還本)；
2: 到期取息(到期繳息還本)；
3: 本息平均法(期金)；
4: 本金平均法；
5: 按月撥款收息(逆向貸款)；
  *
  * @param amortizedCode 契約當時還款方式(月底日)
	*/
  public void setAmortizedCode(String amortizedCode) {
    this.amortizedCode = amortizedCode;
  }

/**
	* 契約當時利率調整方式(月底日)<br>
	* 1: 機動;
2: 固定；
3: 定期機動；
	* @return String
	*/
  public String getRateCode() {
    return this.rateCode == null ? "" : this.rateCode;
  }

/**
	* 契約當時利率調整方式(月底日)<br>
	* 1: 機動;
2: 固定；
3: 定期機動；
  *
  * @param rateCode 契約當時利率調整方式(月底日)
	*/
  public void setRateCode(String rateCode) {
    this.rateCode = rateCode;
  }

/**
	* 契約約定當時還本週期(月底日)<br>
	* 若為到期還本，則填入0；
若按月還本，則填入1；
季繳，3；
半年，6；
年繳,12。
	* @return Integer
	*/
  public int getRepayFreq() {
    return this.repayFreq;
  }

/**
	* 契約約定當時還本週期(月底日)<br>
	* 若為到期還本，則填入0；
若按月還本，則填入1；
季繳，3；
半年，6；
年繳,12。
  *
  * @param repayFreq 契約約定當時還本週期(月底日)
	*/
  public void setRepayFreq(int repayFreq) {
    this.repayFreq = repayFreq;
  }

/**
	* 契約約定當時繳息週期(月底日)<br>
	* 若為到期繳息，則填入0；
若按月還本，則填入1；
季繳，3；
半年，6；
年繳,12。
	* @return Integer
	*/
  public int getPayIntFreq() {
    return this.payIntFreq;
  }

/**
	* 契約約定當時繳息週期(月底日)<br>
	* 若為到期繳息，則填入0；
若按月還本，則填入1；
季繳，3；
半年，6；
年繳,12。
  *
  * @param payIntFreq 契約約定當時繳息週期(月底日)
	*/
  public void setPayIntFreq(int payIntFreq) {
    this.payIntFreq = payIntFreq;
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
	* 帳冊別<br>
	* 1=一般;
2=分紅;
3=利變;
4=OIU
	* @return String
	*/
  public String getAcBookCode() {
    return this.acBookCode == null ? "" : this.acBookCode;
  }

/**
	* 帳冊別<br>
	* 1=一般;
2=分紅;
3=利變;
4=OIU
  *
  * @param acBookCode 帳冊別
	*/
  public void setAcBookCode(String acBookCode) {
    this.acBookCode = acBookCode;
  }

/**
	* 上次繳息日(繳息迄日)<br>
	* 
	* @return Integer
	*/
  public int getPrevPayIntDate() {
    return StaticTool.bcToRoc(this.prevPayIntDate);
  }

/**
	* 上次繳息日(繳息迄日)<br>
	* 
  *
  * @param prevPayIntDate 上次繳息日(繳息迄日)
  * @throws LogicException when Date Is Warn	*/
  public void setPrevPayIntDate(int prevPayIntDate) throws LogicException {
    this.prevPayIntDate = StaticTool.rocToBc(prevPayIntDate);
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
    return "Ifrs9LoanData [ifrs9LoanDataId=" + ifrs9LoanDataId + ", custId=" + custId + ", acctCode=" + acctCode
           + ", acCode=" + acCode + ", acCodeOld=" + acCodeOld + ", status=" + status + ", drawdownDate=" + drawdownDate + ", maturityDate=" + maturityDate + ", drawdownAmt=" + drawdownAmt
           + ", loanBal=" + loanBal + ", intAmt=" + intAmt + ", rate=" + rate + ", ovduDays=" + ovduDays + ", ovduDate=" + ovduDate + ", badDebtDate=" + badDebtDate
           + ", badDebtAmt=" + badDebtAmt + ", approveRate=" + approveRate + ", amortizedCode=" + amortizedCode + ", rateCode=" + rateCode + ", repayFreq=" + repayFreq + ", payIntFreq=" + payIntFreq
           + ", firstDueDate=" + firstDueDate + ", totalPeriod=" + totalPeriod + ", agreeBefFacmNo=" + agreeBefFacmNo + ", agreeBefBormNo=" + agreeBefBormNo + ", acBookCode=" + acBookCode + ", prevPayIntDate=" + prevPayIntDate
           + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
