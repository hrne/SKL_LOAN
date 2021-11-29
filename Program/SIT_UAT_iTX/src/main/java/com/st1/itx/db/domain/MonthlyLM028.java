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

/**
 * MonthlyLM028 LM028預估現金流量月報工作檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`MonthlyLM028`")
public class MonthlyLM028 implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 808865319457777101L;

@EmbeddedId
  private MonthlyLM028Id monthlyLM028Id;

  // 資料年月
  /* YYYYMM */
  @Column(name = "`DataMonth`", insertable = false, updatable = false)
  private int dataMonth = 0;

  // 戶況
  /* 原LMSSTS */
  @Column(name = "`Status`")
  private int status = 0;

  // 企金別
  /* 原CUSENT */
  @Column(name = "`EntCode`")
  private int entCode = 0;

  // 營業單位別
  /* 原CUSBRH */
  @Column(name = "`BranchNo`", length = 4)
  private String branchNo;

  // 借款人戶號
  /* 原LMSACN */
  @Column(name = "`CustNo`", insertable = false, updatable = false)
  private int custNo = 0;

  // 額度編號
  /* 原LMSAPN */
  @Column(name = "`FacmNo`", insertable = false, updatable = false)
  private int facmNo = 0;

  // 撥款序號
  /* 原LMSASQ */
  @Column(name = "`BormNo`", insertable = false, updatable = false)
  private int bormNo = 0;

  // 利率
  /* 原IRTRAT */
  @Column(name = "`StoreRate`")
  private BigDecimal storeRate = new BigDecimal("0");

  // 繳息週期
  /* 原LMSISC */
  @Column(name = "`PayIntFreq`")
  private int payIntFreq = 0;

  // 扣款銀行
  /* 原LMSPBK */
  @Column(name = "`RepayBank`", length = 3)
  private String repayBank;

  // 貸款期間－月
  /* 原APLMON */
  @Column(name = "`LoanTermMm`")
  private int loanTermMm = 0;

  // 貸款期間－日
  /* 原APLDAY */
  @Column(name = "`LoanTermDd`")
  private int loanTermDd = 0;

  // 放款餘額
  /* 原LMSLBL */
  @Column(name = "`LoanBal`")
  private BigDecimal loanBal = new BigDecimal("0");

  // 利率區分
  /* 原AILIRT */
  @Column(name = "`RateCode`", length = 1)
  private String rateCode;

  // 郵局存款別
  /* 原POSCDE */
  @Column(name = "`PostDepCode`", length = 1)
  private String postDepCode;

  // 應繳日
  /* 原LMSPDY */
  @Column(name = "`SpecificDd`")
  private int specificDd = 0;

  // 首次調整週期
  /* 原IRTFSC */
  @Column(name = "`FirstRateAdjFreq`")
  private int firstRateAdjFreq = 0;

  // 基本利率代碼
  /* 原IRTBCD */
  @Column(name = "`BaseRateCode`", length = 2)
  private String baseRateCode;

  // 利率1
  /* 原IRTRATYR1 */
  @Column(name = "`FitRate1`")
  private BigDecimal fitRate1 = new BigDecimal("0");

  // 利率2
  /* 原IRTRATYR2 */
  @Column(name = "`FitRate2`")
  private BigDecimal fitRate2 = new BigDecimal("0");

  // 利率3
  /* 原IRTRATYR3 */
  @Column(name = "`FitRate3`")
  private BigDecimal fitRate3 = new BigDecimal("0");

  // 利率4
  /* 原IRTRATYR4 */
  @Column(name = "`FitRate4`")
  private BigDecimal fitRate4 = new BigDecimal("0");

  // 利率5
  /* 原IRTRATYR5 */
  @Column(name = "`FitRate5`")
  private BigDecimal fitRate5 = new BigDecimal("0");

  // 押品別１
  /* 原GDRID1 */
  @Column(name = "`ClCode1`")
  private int clCode1 = 0;

  // 押品別２
  /* 原GDRID2 */
  @Column(name = "`ClCode2`")
  private int clCode2 = 0;

  // 撥款日-年
  /* 原YYYY */
  @Column(name = "`DrawdownYear`")
  private int drawdownYear = 0;

  // 撥款日-月
  /* 原MONTH */
  @Column(name = "`DrawdownMonth`")
  private int drawdownMonth = 0;

  // 撥款日-日
  /* 原DAY */
  @Column(name = "`DrawdownDay`")
  private int drawdownDay = 0;

  // 到期日碼
  /* 原W08CDE */
  @Column(name = "`W08Code`")
  private int w08Code = 0;

  // 是否為關係人
  /* 原RELATION */
  @Column(name = "`IsRelation`", length = 1)
  private String isRelation;

  // 制度別
  /* 原DPTLVL */
  @Column(name = "`AgType1`", length = 1)
  private String agType1;

  // 資金來源
  /* 原ACTFSC */
  @Column(name = "`AcctSource`", length = 1)
  private String acctSource;

  // 最新利率
  /* 原LIRTRATYR */
  @Column(name = "`LastestRate`")
  private BigDecimal lastestRate = new BigDecimal("0");

  // 最新利率生效起日
  /* 原LIRTDAY */
  @Column(name = "`LastestRateStartDate`")
  private int lastestRateStartDate = 0;

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


  public MonthlyLM028Id getMonthlyLM028Id() {
    return this.monthlyLM028Id;
  }

  public void setMonthlyLM028Id(MonthlyLM028Id monthlyLM028Id) {
    this.monthlyLM028Id = monthlyLM028Id;
  }

/**
	* 資料年月<br>
	* YYYYMM
	* @return Integer
	*/
  public int getDataMonth() {
    return this.dataMonth;
  }

/**
	* 資料年月<br>
	* YYYYMM
  *
  * @param dataMonth 資料年月
	*/
  public void setDataMonth(int dataMonth) {
    this.dataMonth = dataMonth;
  }

/**
	* 戶況<br>
	* 原LMSSTS
	* @return Integer
	*/
  public int getStatus() {
    return this.status;
  }

/**
	* 戶況<br>
	* 原LMSSTS
  *
  * @param status 戶況
	*/
  public void setStatus(int status) {
    this.status = status;
  }

/**
	* 企金別<br>
	* 原CUSENT
	* @return Integer
	*/
  public int getEntCode() {
    return this.entCode;
  }

/**
	* 企金別<br>
	* 原CUSENT
  *
  * @param entCode 企金別
	*/
  public void setEntCode(int entCode) {
    this.entCode = entCode;
  }

/**
	* 營業單位別<br>
	* 原CUSBRH
	* @return String
	*/
  public String getBranchNo() {
    return this.branchNo == null ? "" : this.branchNo;
  }

/**
	* 營業單位別<br>
	* 原CUSBRH
  *
  * @param branchNo 營業單位別
	*/
  public void setBranchNo(String branchNo) {
    this.branchNo = branchNo;
  }

/**
	* 借款人戶號<br>
	* 原LMSACN
	* @return Integer
	*/
  public int getCustNo() {
    return this.custNo;
  }

/**
	* 借款人戶號<br>
	* 原LMSACN
  *
  * @param custNo 借款人戶號
	*/
  public void setCustNo(int custNo) {
    this.custNo = custNo;
  }

/**
	* 額度編號<br>
	* 原LMSAPN
	* @return Integer
	*/
  public int getFacmNo() {
    return this.facmNo;
  }

/**
	* 額度編號<br>
	* 原LMSAPN
  *
  * @param facmNo 額度編號
	*/
  public void setFacmNo(int facmNo) {
    this.facmNo = facmNo;
  }

/**
	* 撥款序號<br>
	* 原LMSASQ
	* @return Integer
	*/
  public int getBormNo() {
    return this.bormNo;
  }

/**
	* 撥款序號<br>
	* 原LMSASQ
  *
  * @param bormNo 撥款序號
	*/
  public void setBormNo(int bormNo) {
    this.bormNo = bormNo;
  }

/**
	* 利率<br>
	* 原IRTRAT
	* @return BigDecimal
	*/
  public BigDecimal getStoreRate() {
    return this.storeRate;
  }

/**
	* 利率<br>
	* 原IRTRAT
  *
  * @param storeRate 利率
	*/
  public void setStoreRate(BigDecimal storeRate) {
    this.storeRate = storeRate;
  }

/**
	* 繳息週期<br>
	* 原LMSISC
	* @return Integer
	*/
  public int getPayIntFreq() {
    return this.payIntFreq;
  }

/**
	* 繳息週期<br>
	* 原LMSISC
  *
  * @param payIntFreq 繳息週期
	*/
  public void setPayIntFreq(int payIntFreq) {
    this.payIntFreq = payIntFreq;
  }

/**
	* 扣款銀行<br>
	* 原LMSPBK
	* @return String
	*/
  public String getRepayBank() {
    return this.repayBank == null ? "" : this.repayBank;
  }

/**
	* 扣款銀行<br>
	* 原LMSPBK
  *
  * @param repayBank 扣款銀行
	*/
  public void setRepayBank(String repayBank) {
    this.repayBank = repayBank;
  }

/**
	* 貸款期間－月<br>
	* 原APLMON
	* @return Integer
	*/
  public int getLoanTermMm() {
    return this.loanTermMm;
  }

/**
	* 貸款期間－月<br>
	* 原APLMON
  *
  * @param loanTermMm 貸款期間－月
	*/
  public void setLoanTermMm(int loanTermMm) {
    this.loanTermMm = loanTermMm;
  }

/**
	* 貸款期間－日<br>
	* 原APLDAY
	* @return Integer
	*/
  public int getLoanTermDd() {
    return this.loanTermDd;
  }

/**
	* 貸款期間－日<br>
	* 原APLDAY
  *
  * @param loanTermDd 貸款期間－日
	*/
  public void setLoanTermDd(int loanTermDd) {
    this.loanTermDd = loanTermDd;
  }

/**
	* 放款餘額<br>
	* 原LMSLBL
	* @return BigDecimal
	*/
  public BigDecimal getLoanBal() {
    return this.loanBal;
  }

/**
	* 放款餘額<br>
	* 原LMSLBL
  *
  * @param loanBal 放款餘額
	*/
  public void setLoanBal(BigDecimal loanBal) {
    this.loanBal = loanBal;
  }

/**
	* 利率區分<br>
	* 原AILIRT
	* @return String
	*/
  public String getRateCode() {
    return this.rateCode == null ? "" : this.rateCode;
  }

/**
	* 利率區分<br>
	* 原AILIRT
  *
  * @param rateCode 利率區分
	*/
  public void setRateCode(String rateCode) {
    this.rateCode = rateCode;
  }

/**
	* 郵局存款別<br>
	* 原POSCDE
	* @return String
	*/
  public String getPostDepCode() {
    return this.postDepCode == null ? "" : this.postDepCode;
  }

/**
	* 郵局存款別<br>
	* 原POSCDE
  *
  * @param postDepCode 郵局存款別
	*/
  public void setPostDepCode(String postDepCode) {
    this.postDepCode = postDepCode;
  }

/**
	* 應繳日<br>
	* 原LMSPDY
	* @return Integer
	*/
  public int getSpecificDd() {
    return this.specificDd;
  }

/**
	* 應繳日<br>
	* 原LMSPDY
  *
  * @param specificDd 應繳日
	*/
  public void setSpecificDd(int specificDd) {
    this.specificDd = specificDd;
  }

/**
	* 首次調整週期<br>
	* 原IRTFSC
	* @return Integer
	*/
  public int getFirstRateAdjFreq() {
    return this.firstRateAdjFreq;
  }

/**
	* 首次調整週期<br>
	* 原IRTFSC
  *
  * @param firstRateAdjFreq 首次調整週期
	*/
  public void setFirstRateAdjFreq(int firstRateAdjFreq) {
    this.firstRateAdjFreq = firstRateAdjFreq;
  }

/**
	* 基本利率代碼<br>
	* 原IRTBCD
	* @return String
	*/
  public String getBaseRateCode() {
    return this.baseRateCode == null ? "" : this.baseRateCode;
  }

/**
	* 基本利率代碼<br>
	* 原IRTBCD
  *
  * @param baseRateCode 基本利率代碼
	*/
  public void setBaseRateCode(String baseRateCode) {
    this.baseRateCode = baseRateCode;
  }

/**
	* 利率1<br>
	* 原IRTRATYR1
	* @return BigDecimal
	*/
  public BigDecimal getFitRate1() {
    return this.fitRate1;
  }

/**
	* 利率1<br>
	* 原IRTRATYR1
  *
  * @param fitRate1 利率1
	*/
  public void setFitRate1(BigDecimal fitRate1) {
    this.fitRate1 = fitRate1;
  }

/**
	* 利率2<br>
	* 原IRTRATYR2
	* @return BigDecimal
	*/
  public BigDecimal getFitRate2() {
    return this.fitRate2;
  }

/**
	* 利率2<br>
	* 原IRTRATYR2
  *
  * @param fitRate2 利率2
	*/
  public void setFitRate2(BigDecimal fitRate2) {
    this.fitRate2 = fitRate2;
  }

/**
	* 利率3<br>
	* 原IRTRATYR3
	* @return BigDecimal
	*/
  public BigDecimal getFitRate3() {
    return this.fitRate3;
  }

/**
	* 利率3<br>
	* 原IRTRATYR3
  *
  * @param fitRate3 利率3
	*/
  public void setFitRate3(BigDecimal fitRate3) {
    this.fitRate3 = fitRate3;
  }

/**
	* 利率4<br>
	* 原IRTRATYR4
	* @return BigDecimal
	*/
  public BigDecimal getFitRate4() {
    return this.fitRate4;
  }

/**
	* 利率4<br>
	* 原IRTRATYR4
  *
  * @param fitRate4 利率4
	*/
  public void setFitRate4(BigDecimal fitRate4) {
    this.fitRate4 = fitRate4;
  }

/**
	* 利率5<br>
	* 原IRTRATYR5
	* @return BigDecimal
	*/
  public BigDecimal getFitRate5() {
    return this.fitRate5;
  }

/**
	* 利率5<br>
	* 原IRTRATYR5
  *
  * @param fitRate5 利率5
	*/
  public void setFitRate5(BigDecimal fitRate5) {
    this.fitRate5 = fitRate5;
  }

/**
	* 押品別１<br>
	* 原GDRID1
	* @return Integer
	*/
  public int getClCode1() {
    return this.clCode1;
  }

/**
	* 押品別１<br>
	* 原GDRID1
  *
  * @param clCode1 押品別１
	*/
  public void setClCode1(int clCode1) {
    this.clCode1 = clCode1;
  }

/**
	* 押品別２<br>
	* 原GDRID2
	* @return Integer
	*/
  public int getClCode2() {
    return this.clCode2;
  }

/**
	* 押品別２<br>
	* 原GDRID2
  *
  * @param clCode2 押品別２
	*/
  public void setClCode2(int clCode2) {
    this.clCode2 = clCode2;
  }

/**
	* 撥款日-年<br>
	* 原YYYY
	* @return Integer
	*/
  public int getDrawdownYear() {
    return this.drawdownYear;
  }

/**
	* 撥款日-年<br>
	* 原YYYY
  *
  * @param drawdownYear 撥款日-年
	*/
  public void setDrawdownYear(int drawdownYear) {
    this.drawdownYear = drawdownYear;
  }

/**
	* 撥款日-月<br>
	* 原MONTH
	* @return Integer
	*/
  public int getDrawdownMonth() {
    return this.drawdownMonth;
  }

/**
	* 撥款日-月<br>
	* 原MONTH
  *
  * @param drawdownMonth 撥款日-月
	*/
  public void setDrawdownMonth(int drawdownMonth) {
    this.drawdownMonth = drawdownMonth;
  }

/**
	* 撥款日-日<br>
	* 原DAY
	* @return Integer
	*/
  public int getDrawdownDay() {
    return this.drawdownDay;
  }

/**
	* 撥款日-日<br>
	* 原DAY
  *
  * @param drawdownDay 撥款日-日
	*/
  public void setDrawdownDay(int drawdownDay) {
    this.drawdownDay = drawdownDay;
  }

/**
	* 到期日碼<br>
	* 原W08CDE
	* @return Integer
	*/
  public int getW08Code() {
    return this.w08Code;
  }

/**
	* 到期日碼<br>
	* 原W08CDE
  *
  * @param w08Code 到期日碼
	*/
  public void setW08Code(int w08Code) {
    this.w08Code = w08Code;
  }

/**
	* 是否為關係人<br>
	* 原RELATION
	* @return String
	*/
  public String getIsRelation() {
    return this.isRelation == null ? "" : this.isRelation;
  }

/**
	* 是否為關係人<br>
	* 原RELATION
  *
  * @param isRelation 是否為關係人
	*/
  public void setIsRelation(String isRelation) {
    this.isRelation = isRelation;
  }

/**
	* 制度別<br>
	* 原DPTLVL
	* @return String
	*/
  public String getAgType1() {
    return this.agType1 == null ? "" : this.agType1;
  }

/**
	* 制度別<br>
	* 原DPTLVL
  *
  * @param agType1 制度別
	*/
  public void setAgType1(String agType1) {
    this.agType1 = agType1;
  }

/**
	* 資金來源<br>
	* 原ACTFSC
	* @return String
	*/
  public String getAcctSource() {
    return this.acctSource == null ? "" : this.acctSource;
  }

/**
	* 資金來源<br>
	* 原ACTFSC
  *
  * @param acctSource 資金來源
	*/
  public void setAcctSource(String acctSource) {
    this.acctSource = acctSource;
  }

/**
	* 最新利率<br>
	* 原LIRTRATYR
	* @return BigDecimal
	*/
  public BigDecimal getLastestRate() {
    return this.lastestRate;
  }

/**
	* 最新利率<br>
	* 原LIRTRATYR
  *
  * @param lastestRate 最新利率
	*/
  public void setLastestRate(BigDecimal lastestRate) {
    this.lastestRate = lastestRate;
  }

/**
	* 最新利率生效起日<br>
	* 原LIRTDAY
	* @return Integer
	*/
  public int getLastestRateStartDate() {
    return this.lastestRateStartDate;
  }

/**
	* 最新利率生效起日<br>
	* 原LIRTDAY
  *
  * @param lastestRateStartDate 最新利率生效起日
	*/
  public void setLastestRateStartDate(int lastestRateStartDate) {
    this.lastestRateStartDate = lastestRateStartDate;
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
    return "MonthlyLM028 [monthlyLM028Id=" + monthlyLM028Id + ", status=" + status + ", entCode=" + entCode + ", branchNo=" + branchNo
           + ", storeRate=" + storeRate + ", payIntFreq=" + payIntFreq + ", repayBank=" + repayBank + ", loanTermMm=" + loanTermMm + ", loanTermDd=" + loanTermDd
           + ", loanBal=" + loanBal + ", rateCode=" + rateCode + ", postDepCode=" + postDepCode + ", specificDd=" + specificDd + ", firstRateAdjFreq=" + firstRateAdjFreq + ", baseRateCode=" + baseRateCode
           + ", fitRate1=" + fitRate1 + ", fitRate2=" + fitRate2 + ", fitRate3=" + fitRate3 + ", fitRate4=" + fitRate4 + ", fitRate5=" + fitRate5 + ", clCode1=" + clCode1
           + ", clCode2=" + clCode2 + ", drawdownYear=" + drawdownYear + ", drawdownMonth=" + drawdownMonth + ", drawdownDay=" + drawdownDay + ", w08Code=" + w08Code + ", isRelation=" + isRelation
           + ", agType1=" + agType1 + ", acctSource=" + acctSource + ", lastestRate=" + lastestRate + ", lastestRateStartDate=" + lastestRateStartDate + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
           + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
