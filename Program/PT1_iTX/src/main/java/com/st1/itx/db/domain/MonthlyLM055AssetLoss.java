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
 * MonthlyLM055AssetLoss LM055重要放款餘額明細表<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`MonthlyLM055AssetLoss`")
public class MonthlyLM055AssetLoss implements Serializable {


  @EmbeddedId
  private MonthlyLM055AssetLossId monthlyLM055AssetLossId;

  // 資料年月
  @Column(name = "`YearMonth`", insertable = false, updatable = false)
  private int yearMonth = 0;

  // 放款種類
  /* A.銀行保證放款B.動產擔保放款C.不動產抵押放款D.有價證券質押放款G.政策性專案貸款Z.折溢價與費用 */
  @Column(name = "`LoanType`", length = 1, insertable = false, updatable = false)
  private String loanType;

  // 逾期放款
  @Column(name = "`OverdueAmount`")
  private BigDecimal overdueAmount = new BigDecimal("0");

  // 未列入逾期應予評估放款
  @Column(name = "`ObserveAmount`")
  private BigDecimal observeAmount = new BigDecimal("0");

  // 正常放款
  @Column(name = "`NormalAmount`")
  private BigDecimal normalAmount = new BigDecimal("0");

  // 政策性專案貸款調整數
  /* ToTalLoanBal 專案貸款總額-調整-oToTalLoanBal 專案貸款總額-88LoanBal 88風災調整數G.政策性專案貸款：GovProjectAdjustAmtC.不動產抵押放款  : 0 - GovProjectAdjustAmt */
  @Column(name = "`GovProjectAdjustAmt`")
  private BigDecimal govProjectAdjustAmt = new BigDecimal("0");

  // 逾期1放款金額
  @Column(name = "`LoanAmount1`")
  private BigDecimal loanAmount1 = new BigDecimal("0");

  // 逾期2放款金額
  @Column(name = "`LoanAmount2`")
  private BigDecimal loanAmount2 = new BigDecimal("0");

  // 逾期3放款金額
  @Column(name = "`LoanAmount3`")
  private BigDecimal loanAmount3 = new BigDecimal("0");

  // 逾期4放款金額
  @Column(name = "`LoanAmount4`")
  private BigDecimal loanAmount4 = new BigDecimal("0");

  // 逾期5放款金額
  @Column(name = "`LoanAmount5`")
  private BigDecimal loanAmount5 = new BigDecimal("0");

  // 逾期6放款金額
  @Column(name = "`LoanAmount6`")
  private BigDecimal loanAmount6 = new BigDecimal("0");

  // 協議逾期數0放款金額
  @Column(name = "`LoanAmountNeg0`")
  private BigDecimal loanAmountNeg0 = new BigDecimal("0");

  // 催收金額
  @Column(name = "`LoanAmount990`")
  private BigDecimal loanAmount990 = new BigDecimal("0");

  // 正常逾期數0放款金額
  /* 放款餘額+政策性專案貸款調整數 */
  @Column(name = "`LoanAmountNor0`")
  private BigDecimal loanAmountNor0 = new BigDecimal("0");

  // 備呆金額五分類1
  /* 備呆總金額(不含應收利息)、備呆比率 ref MonthlyLM052AssetClass(LM052資產分類表)調整數放放款種類=C.不動產抵押放款A.銀行保證放款=&amp;gt;按備呆比率重算B.動產擔保放款=&amp;gt;按備呆比率重算C.不動產抵押放款=&amp;gt;總金額-其他重算金額D.有價證券質押放款=&amp;gt;按備呆比率重算G.政策性專案貸款 =&amp;gt;按備呆比率重算 */
  @Column(name = "`ReserveLossAmt1`")
  private BigDecimal reserveLossAmt1 = new BigDecimal("0");

  // 備呆金額五分類2
  /* 同上 */
  @Column(name = "`ReserveLossAmt2`")
  private BigDecimal reserveLossAmt2 = new BigDecimal("0");

  // 備呆金額五分類3
  /* 同上 */
  @Column(name = "`ReserveLossAmt3`")
  private BigDecimal reserveLossAmt3 = new BigDecimal("0");

  // 備呆金額五分類4
  /* 同上 */
  @Column(name = "`ReserveLossAmt4`")
  private BigDecimal reserveLossAmt4 = new BigDecimal("0");

  // 備呆金額五分類5
  /* 同上 */
  @Column(name = "`ReserveLossAmt5`")
  private BigDecimal reserveLossAmt5 = new BigDecimal("0");

  // IFRS9增提金額(含應收利息)
  /* 放款種類=C.不動產抵押放款備呆總金額(不含應收利息) - 會計部核定備抵損失(MonthlyLM052Loss.ApprovedLoss) */
  @Column(name = "`IFRS9AdjustAmt`")
  private BigDecimal iFRS9AdjustAmt = new BigDecimal("0");

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


  public MonthlyLM055AssetLossId getMonthlyLM055AssetLossId() {
    return this.monthlyLM055AssetLossId;
  }

  public void setMonthlyLM055AssetLossId(MonthlyLM055AssetLossId monthlyLM055AssetLossId) {
    this.monthlyLM055AssetLossId = monthlyLM055AssetLossId;
  }

/**
	* 資料年月<br>
	* 
	* @return Integer
	*/
  public int getYearMonth() {
    return this.yearMonth;
  }

/**
	* 資料年月<br>
	* 
  *
  * @param yearMonth 資料年月
	*/
  public void setYearMonth(int yearMonth) {
    this.yearMonth = yearMonth;
  }

/**
	* 放款種類<br>
	* A.銀行保證放款
B.動產擔保放款
C.不動產抵押放款
D.有價證券質押放款
G.政策性專案貸款
Z.折溢價與費用
	* @return String
	*/
  public String getLoanType() {
    return this.loanType == null ? "" : this.loanType;
  }

/**
	* 放款種類<br>
	* A.銀行保證放款
B.動產擔保放款
C.不動產抵押放款
D.有價證券質押放款
G.政策性專案貸款
Z.折溢價與費用
  *
  * @param loanType 放款種類
	*/
  public void setLoanType(String loanType) {
    this.loanType = loanType;
  }

/**
	* 逾期放款<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getOverdueAmount() {
    return this.overdueAmount;
  }

/**
	* 逾期放款<br>
	* 
  *
  * @param overdueAmount 逾期放款
	*/
  public void setOverdueAmount(BigDecimal overdueAmount) {
    this.overdueAmount = overdueAmount;
  }

/**
	* 未列入逾期應予評估放款<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getObserveAmount() {
    return this.observeAmount;
  }

/**
	* 未列入逾期應予評估放款<br>
	* 
  *
  * @param observeAmount 未列入逾期應予評估放款
	*/
  public void setObserveAmount(BigDecimal observeAmount) {
    this.observeAmount = observeAmount;
  }

/**
	* 正常放款<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getNormalAmount() {
    return this.normalAmount;
  }

/**
	* 正常放款<br>
	* 
  *
  * @param normalAmount 正常放款
	*/
  public void setNormalAmount(BigDecimal normalAmount) {
    this.normalAmount = normalAmount;
  }

/**
	* 政策性專案貸款調整數<br>
	* ToTalLoanBal 專案貸款總額-調整
-oToTalLoanBal 專案貸款總額
-88LoanBal 88風災調整數
G.政策性專案貸款：GovProjectAdjustAmt
C.不動產抵押放款  : 0 - GovProjectAdjustAmt
	* @return BigDecimal
	*/
  public BigDecimal getGovProjectAdjustAmt() {
    return this.govProjectAdjustAmt;
  }

/**
	* 政策性專案貸款調整數<br>
	* ToTalLoanBal 專案貸款總額-調整
-oToTalLoanBal 專案貸款總額
-88LoanBal 88風災調整數
G.政策性專案貸款：GovProjectAdjustAmt
C.不動產抵押放款  : 0 - GovProjectAdjustAmt
  *
  * @param govProjectAdjustAmt 政策性專案貸款調整數
	*/
  public void setGovProjectAdjustAmt(BigDecimal govProjectAdjustAmt) {
    this.govProjectAdjustAmt = govProjectAdjustAmt;
  }

/**
	* 逾期1放款金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLoanAmount1() {
    return this.loanAmount1;
  }

/**
	* 逾期1放款金額<br>
	* 
  *
  * @param loanAmount1 逾期1放款金額
	*/
  public void setLoanAmount1(BigDecimal loanAmount1) {
    this.loanAmount1 = loanAmount1;
  }

/**
	* 逾期2放款金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLoanAmount2() {
    return this.loanAmount2;
  }

/**
	* 逾期2放款金額<br>
	* 
  *
  * @param loanAmount2 逾期2放款金額
	*/
  public void setLoanAmount2(BigDecimal loanAmount2) {
    this.loanAmount2 = loanAmount2;
  }

/**
	* 逾期3放款金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLoanAmount3() {
    return this.loanAmount3;
  }

/**
	* 逾期3放款金額<br>
	* 
  *
  * @param loanAmount3 逾期3放款金額
	*/
  public void setLoanAmount3(BigDecimal loanAmount3) {
    this.loanAmount3 = loanAmount3;
  }

/**
	* 逾期4放款金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLoanAmount4() {
    return this.loanAmount4;
  }

/**
	* 逾期4放款金額<br>
	* 
  *
  * @param loanAmount4 逾期4放款金額
	*/
  public void setLoanAmount4(BigDecimal loanAmount4) {
    this.loanAmount4 = loanAmount4;
  }

/**
	* 逾期5放款金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLoanAmount5() {
    return this.loanAmount5;
  }

/**
	* 逾期5放款金額<br>
	* 
  *
  * @param loanAmount5 逾期5放款金額
	*/
  public void setLoanAmount5(BigDecimal loanAmount5) {
    this.loanAmount5 = loanAmount5;
  }

/**
	* 逾期6放款金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLoanAmount6() {
    return this.loanAmount6;
  }

/**
	* 逾期6放款金額<br>
	* 
  *
  * @param loanAmount6 逾期6放款金額
	*/
  public void setLoanAmount6(BigDecimal loanAmount6) {
    this.loanAmount6 = loanAmount6;
  }

/**
	* 協議逾期數0放款金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLoanAmountNeg0() {
    return this.loanAmountNeg0;
  }

/**
	* 協議逾期數0放款金額<br>
	* 
  *
  * @param loanAmountNeg0 協議逾期數0放款金額
	*/
  public void setLoanAmountNeg0(BigDecimal loanAmountNeg0) {
    this.loanAmountNeg0 = loanAmountNeg0;
  }

/**
	* 催收金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLoanAmount990() {
    return this.loanAmount990;
  }

/**
	* 催收金額<br>
	* 
  *
  * @param loanAmount990 催收金額
	*/
  public void setLoanAmount990(BigDecimal loanAmount990) {
    this.loanAmount990 = loanAmount990;
  }

/**
	* 正常逾期數0放款金額<br>
	* 放款餘額+政策性專案貸款調整數
	* @return BigDecimal
	*/
  public BigDecimal getLoanAmountNor0() {
    return this.loanAmountNor0;
  }

/**
	* 正常逾期數0放款金額<br>
	* 放款餘額+政策性專案貸款調整數
  *
  * @param loanAmountNor0 正常逾期數0放款金額
	*/
  public void setLoanAmountNor0(BigDecimal loanAmountNor0) {
    this.loanAmountNor0 = loanAmountNor0;
  }

/**
	* 備呆金額五分類1<br>
	* 備呆總金額(不含應收利息)、備呆比率 
ref MonthlyLM052AssetClass(LM052資產分類表)
調整數放放款種類=C.不動產抵押放款
A.銀行保證放款=&amp;gt;按備呆比率重算
B.動產擔保放款=&amp;gt;按備呆比率重算
C.不動產抵押放款=&amp;gt;總金額-其他重算金額
D.有價證券質押放款=&amp;gt;按備呆比率重算
G.政策性專案貸款 =&amp;gt;按備呆比率重算
	* @return BigDecimal
	*/
  public BigDecimal getReserveLossAmt1() {
    return this.reserveLossAmt1;
  }

/**
	* 備呆金額五分類1<br>
	* 備呆總金額(不含應收利息)、備呆比率 
ref MonthlyLM052AssetClass(LM052資產分類表)
調整數放放款種類=C.不動產抵押放款
A.銀行保證放款=&amp;gt;按備呆比率重算
B.動產擔保放款=&amp;gt;按備呆比率重算
C.不動產抵押放款=&amp;gt;總金額-其他重算金額
D.有價證券質押放款=&amp;gt;按備呆比率重算
G.政策性專案貸款 =&amp;gt;按備呆比率重算
  *
  * @param reserveLossAmt1 備呆金額五分類1
	*/
  public void setReserveLossAmt1(BigDecimal reserveLossAmt1) {
    this.reserveLossAmt1 = reserveLossAmt1;
  }

/**
	* 備呆金額五分類2<br>
	* 同上
	* @return BigDecimal
	*/
  public BigDecimal getReserveLossAmt2() {
    return this.reserveLossAmt2;
  }

/**
	* 備呆金額五分類2<br>
	* 同上
  *
  * @param reserveLossAmt2 備呆金額五分類2
	*/
  public void setReserveLossAmt2(BigDecimal reserveLossAmt2) {
    this.reserveLossAmt2 = reserveLossAmt2;
  }

/**
	* 備呆金額五分類3<br>
	* 同上
	* @return BigDecimal
	*/
  public BigDecimal getReserveLossAmt3() {
    return this.reserveLossAmt3;
  }

/**
	* 備呆金額五分類3<br>
	* 同上
  *
  * @param reserveLossAmt3 備呆金額五分類3
	*/
  public void setReserveLossAmt3(BigDecimal reserveLossAmt3) {
    this.reserveLossAmt3 = reserveLossAmt3;
  }

/**
	* 備呆金額五分類4<br>
	* 同上
	* @return BigDecimal
	*/
  public BigDecimal getReserveLossAmt4() {
    return this.reserveLossAmt4;
  }

/**
	* 備呆金額五分類4<br>
	* 同上
  *
  * @param reserveLossAmt4 備呆金額五分類4
	*/
  public void setReserveLossAmt4(BigDecimal reserveLossAmt4) {
    this.reserveLossAmt4 = reserveLossAmt4;
  }

/**
	* 備呆金額五分類5<br>
	* 同上
	* @return BigDecimal
	*/
  public BigDecimal getReserveLossAmt5() {
    return this.reserveLossAmt5;
  }

/**
	* 備呆金額五分類5<br>
	* 同上
  *
  * @param reserveLossAmt5 備呆金額五分類5
	*/
  public void setReserveLossAmt5(BigDecimal reserveLossAmt5) {
    this.reserveLossAmt5 = reserveLossAmt5;
  }

/**
	* IFRS9增提金額(含應收利息)<br>
	* 放款種類=C.不動產抵押放款
備呆總金額(不含應收利息) 
- 會計部核定備抵損失(MonthlyLM052Loss.ApprovedLoss)
	* @return BigDecimal
	*/
  public BigDecimal getIFRS9AdjustAmt() {
    return this.iFRS9AdjustAmt;
  }

/**
	* IFRS9增提金額(含應收利息)<br>
	* 放款種類=C.不動產抵押放款
備呆總金額(不含應收利息) 
- 會計部核定備抵損失(MonthlyLM052Loss.ApprovedLoss)
  *
  * @param iFRS9AdjustAmt IFRS9增提金額(含應收利息)
	*/
  public void setIFRS9AdjustAmt(BigDecimal iFRS9AdjustAmt) {
    this.iFRS9AdjustAmt = iFRS9AdjustAmt;
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
    return "MonthlyLM055AssetLoss [monthlyLM055AssetLossId=" + monthlyLM055AssetLossId + ", overdueAmount=" + overdueAmount + ", observeAmount=" + observeAmount + ", normalAmount=" + normalAmount + ", govProjectAdjustAmt=" + govProjectAdjustAmt
           + ", loanAmount1=" + loanAmount1 + ", loanAmount2=" + loanAmount2 + ", loanAmount3=" + loanAmount3 + ", loanAmount4=" + loanAmount4 + ", loanAmount5=" + loanAmount5 + ", loanAmount6=" + loanAmount6
           + ", loanAmountNeg0=" + loanAmountNeg0 + ", loanAmount990=" + loanAmount990 + ", loanAmountNor0=" + loanAmountNor0 + ", reserveLossAmt1=" + reserveLossAmt1 + ", reserveLossAmt2=" + reserveLossAmt2 + ", reserveLossAmt3=" + reserveLossAmt3
           + ", reserveLossAmt4=" + reserveLossAmt4 + ", reserveLossAmt5=" + reserveLossAmt5 + ", iFRS9AdjustAmt=" + iFRS9AdjustAmt + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
           + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
