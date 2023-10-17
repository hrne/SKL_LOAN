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
 * MonthlyLM042Statis LM042RBC統計數<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`MonthlyLM042Statis`")
public class MonthlyLM042Statis implements Serializable {


  @EmbeddedId
  private MonthlyLM042StatisId monthlyLM042StatisId;

  // 資料年月
  @Column(name = "`YearMonth`", insertable = false, updatable = false)
  private int yearMonth = 0;

  // 放款項目
  /* A：銀行保證放款B：動產擔保放款C：不動產擔保放款D：有價證券質押放款Z：政策性專案貸款6：折溢價與費用 */
  @Column(name = "`LoanItem`", length = 1, insertable = false, updatable = false)
  private String loanItem;

  // 是否為利害關係人
  /* Y/N */
  @Column(name = "`RelatedCode`", length = 1, insertable = false, updatable = false)
  private String relatedCode;

  // 資產五分類代號
  @Column(name = "`AssetClass`", length = 1, insertable = false, updatable = false)
  private String assetClass;

  // 放款餘額
  /* LoanItem=C,Z 含專案差異數 */
  @Column(name = "`LoanBal`")
  private BigDecimal loanBal = new BigDecimal("0");

  // 備呆金額
  @Column(name = "`ReserveLossAmt`")
  private BigDecimal reserveLossAmt = new BigDecimal("0");

  // 備呆差額
  /* 備呆差額ReserveLossAmt-MonthlyLM052AssetClass.StorageAmtAssetClass=1(11.12.61)、AssetClass=2(21.22.23.7)、AssetClass=3(3)、AssetClass=4(4)、AssetClass=5(5)、 */
  @Column(name = "`ReserveLossDiff`")
  private BigDecimal reserveLossDiff = new BigDecimal("0");

  // 淨額
  /* 放款餘額扣除備呆LoanItem=CRelatedCode=N減1.5%差異數(會計室備呆減不含應付利息的五分類提存數(MonthlyAssetClass) */
  @Column(name = "`NetAmt`")
  private BigDecimal netAmt = new BigDecimal("0");

  // (購置住宅+修繕貸款)餘額
  @Column(name = "`HouseAndRepairBal`")
  private BigDecimal houseAndRepairBal = new BigDecimal("0");

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


  public MonthlyLM042StatisId getMonthlyLM042StatisId() {
    return this.monthlyLM042StatisId;
  }

  public void setMonthlyLM042StatisId(MonthlyLM042StatisId monthlyLM042StatisId) {
    this.monthlyLM042StatisId = monthlyLM042StatisId;
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
	* 放款項目<br>
	* A：銀行保證放款
B：動產擔保放款
C：不動產擔保放款
D：有價證券質押放款
Z：政策性專案貸款
6：折溢價與費用
	* @return String
	*/
  public String getLoanItem() {
    return this.loanItem == null ? "" : this.loanItem;
  }

/**
	* 放款項目<br>
	* A：銀行保證放款
B：動產擔保放款
C：不動產擔保放款
D：有價證券質押放款
Z：政策性專案貸款
6：折溢價與費用
  *
  * @param loanItem 放款項目
	*/
  public void setLoanItem(String loanItem) {
    this.loanItem = loanItem;
  }

/**
	* 是否為利害關係人<br>
	* Y/N
	* @return String
	*/
  public String getRelatedCode() {
    return this.relatedCode == null ? "" : this.relatedCode;
  }

/**
	* 是否為利害關係人<br>
	* Y/N
  *
  * @param relatedCode 是否為利害關係人
	*/
  public void setRelatedCode(String relatedCode) {
    this.relatedCode = relatedCode;
  }

/**
	* 資產五分類代號<br>
	* 
	* @return String
	*/
  public String getAssetClass() {
    return this.assetClass == null ? "" : this.assetClass;
  }

/**
	* 資產五分類代號<br>
	* 
  *
  * @param assetClass 資產五分類代號
	*/
  public void setAssetClass(String assetClass) {
    this.assetClass = assetClass;
  }

/**
	* 放款餘額<br>
	* LoanItem=C,Z 含專案差異數
	* @return BigDecimal
	*/
  public BigDecimal getLoanBal() {
    return this.loanBal;
  }

/**
	* 放款餘額<br>
	* LoanItem=C,Z 含專案差異數
  *
  * @param loanBal 放款餘額
	*/
  public void setLoanBal(BigDecimal loanBal) {
    this.loanBal = loanBal;
  }

/**
	* 備呆金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getReserveLossAmt() {
    return this.reserveLossAmt;
  }

/**
	* 備呆金額<br>
	* 
  *
  * @param reserveLossAmt 備呆金額
	*/
  public void setReserveLossAmt(BigDecimal reserveLossAmt) {
    this.reserveLossAmt = reserveLossAmt;
  }

/**
	* 備呆差額<br>
	* 備呆差額
ReserveLossAmt-MonthlyLM052AssetClass.StorageAmt
AssetClass=1(11.12.61)、AssetClass=2(21.22.23.7)、AssetClass=3(3)、
AssetClass=4(4)、
AssetClass=5(5)、
	* @return BigDecimal
	*/
  public BigDecimal getReserveLossDiff() {
    return this.reserveLossDiff;
  }

/**
	* 備呆差額<br>
	* 備呆差額
ReserveLossAmt-MonthlyLM052AssetClass.StorageAmt
AssetClass=1(11.12.61)、AssetClass=2(21.22.23.7)、AssetClass=3(3)、
AssetClass=4(4)、
AssetClass=5(5)、
  *
  * @param reserveLossDiff 備呆差額
	*/
  public void setReserveLossDiff(BigDecimal reserveLossDiff) {
    this.reserveLossDiff = reserveLossDiff;
  }

/**
	* 淨額<br>
	* 放款餘額扣除備呆
LoanItem=C
RelatedCode=N
減1.5%差異數(會計室備呆減不含應付利息的五分類提存數(MonthlyAssetClass)
	* @return BigDecimal
	*/
  public BigDecimal getNetAmt() {
    return this.netAmt;
  }

/**
	* 淨額<br>
	* 放款餘額扣除備呆
LoanItem=C
RelatedCode=N
減1.5%差異數(會計室備呆減不含應付利息的五分類提存數(MonthlyAssetClass)
  *
  * @param netAmt 淨額
	*/
  public void setNetAmt(BigDecimal netAmt) {
    this.netAmt = netAmt;
  }

/**
	* (購置住宅+修繕貸款)餘額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getHouseAndRepairBal() {
    return this.houseAndRepairBal;
  }

/**
	* (購置住宅+修繕貸款)餘額<br>
	* 
  *
  * @param houseAndRepairBal (購置住宅+修繕貸款)餘額
	*/
  public void setHouseAndRepairBal(BigDecimal houseAndRepairBal) {
    this.houseAndRepairBal = houseAndRepairBal;
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
    return "MonthlyLM042Statis [monthlyLM042StatisId=" + monthlyLM042StatisId + ", loanBal=" + loanBal + ", reserveLossAmt=" + reserveLossAmt
           + ", reserveLossDiff=" + reserveLossDiff + ", netAmt=" + netAmt + ", houseAndRepairBal=" + houseAndRepairBal + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
           + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
