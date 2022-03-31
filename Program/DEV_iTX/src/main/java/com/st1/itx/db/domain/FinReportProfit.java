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
 * FinReportProfit 客戶財務報表.損益表<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`FinReportProfit`")
public class FinReportProfit implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 3814762709777659494L;

@EmbeddedId
  private FinReportProfitId finReportProfitId;

  // 客戶識別碼
  @Column(name = "`CustUKey`", length = 32, insertable = false, updatable = false)
  private String custUKey;

  // 識別碼
  @Column(name = "`Ukey`", length = 32, insertable = false, updatable = false)
  private String ukey;

  // 營業收入
  @Column(name = "`BusIncome`")
  private BigDecimal busIncome = new BigDecimal("0");

  // 較去年同期營收成長率
  @Column(name = "`GrowRate`")
  private BigDecimal growRate = new BigDecimal("0");

  // 減︰營業成本
  @Column(name = "`BusCost`")
  private BigDecimal busCost = new BigDecimal("0");

  // 營業毛利
  @Column(name = "`BusGrossProfit`")
  private BigDecimal busGrossProfit = new BigDecimal("0");

  // 減:管銷費用
  @Column(name = "`ManageFee`")
  private BigDecimal manageFee = new BigDecimal("0");

  // 營業損益
  @Column(name = "`BusLossProfit`")
  private BigDecimal busLossProfit = new BigDecimal("0");

  // 加︰營業外收入
  @Column(name = "`BusOtherIncome`")
  private BigDecimal busOtherIncome = new BigDecimal("0");

  // 減︰利息支出
  @Column(name = "`Interest`")
  private BigDecimal interest = new BigDecimal("0");

  // 營業外費用
  @Column(name = "`BusOtherFee`")
  private BigDecimal busOtherFee = new BigDecimal("0");

  // 稅前淨利
  @Column(name = "`BeforeTaxNet`")
  private BigDecimal beforeTaxNet = new BigDecimal("0");

  // 減︰營利事業所得稅
  @Column(name = "`BusTax`")
  private BigDecimal busTax = new BigDecimal("0");

  // 本期損益
  @Column(name = "`HomeLossProfit`")
  private BigDecimal homeLossProfit = new BigDecimal("0");

  // 其他綜合損益
  @Column(name = "`OtherComLossProfit`")
  private BigDecimal otherComLossProfit = new BigDecimal("0");

  // 本期綜合損益總額
  @Column(name = "`HomeComLossProfit`")
  private BigDecimal homeComLossProfit = new BigDecimal("0");

  // 非控制權益
  @Column(name = "`UncontrolRight`")
  private BigDecimal uncontrolRight = new BigDecimal("0");

  // 歸屬於母公司之權益
  @Column(name = "`ParentCompanyRight`")
  private BigDecimal parentCompanyRight = new BigDecimal("0");

  // 每股盈餘EPS(元)
  @Column(name = "`EPS`")
  private BigDecimal ePS = new BigDecimal("0");

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


  public FinReportProfitId getFinReportProfitId() {
    return this.finReportProfitId;
  }

  public void setFinReportProfitId(FinReportProfitId finReportProfitId) {
    this.finReportProfitId = finReportProfitId;
  }

/**
	* 客戶識別碼<br>
	* 
	* @return String
	*/
  public String getCustUKey() {
    return this.custUKey == null ? "" : this.custUKey;
  }

/**
	* 客戶識別碼<br>
	* 
  *
  * @param custUKey 客戶識別碼
	*/
  public void setCustUKey(String custUKey) {
    this.custUKey = custUKey;
  }

/**
	* 識別碼<br>
	* 
	* @return String
	*/
  public String getUkey() {
    return this.ukey == null ? "" : this.ukey;
  }

/**
	* 識別碼<br>
	* 
  *
  * @param ukey 識別碼
	*/
  public void setUkey(String ukey) {
    this.ukey = ukey;
  }

/**
	* 營業收入<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getBusIncome() {
    return this.busIncome;
  }

/**
	* 營業收入<br>
	* 
  *
  * @param busIncome 營業收入
	*/
  public void setBusIncome(BigDecimal busIncome) {
    this.busIncome = busIncome;
  }

/**
	* 較去年同期營收成長率<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getGrowRate() {
    return this.growRate;
  }

/**
	* 較去年同期營收成長率<br>
	* 
  *
  * @param growRate 較去年同期營收成長率
	*/
  public void setGrowRate(BigDecimal growRate) {
    this.growRate = growRate;
  }

/**
	* 減︰營業成本<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getBusCost() {
    return this.busCost;
  }

/**
	* 減︰營業成本<br>
	* 
  *
  * @param busCost 減︰營業成本
	*/
  public void setBusCost(BigDecimal busCost) {
    this.busCost = busCost;
  }

/**
	* 營業毛利<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getBusGrossProfit() {
    return this.busGrossProfit;
  }

/**
	* 營業毛利<br>
	* 
  *
  * @param busGrossProfit 營業毛利
	*/
  public void setBusGrossProfit(BigDecimal busGrossProfit) {
    this.busGrossProfit = busGrossProfit;
  }

/**
	* 減:管銷費用<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getManageFee() {
    return this.manageFee;
  }

/**
	* 減:管銷費用<br>
	* 
  *
  * @param manageFee 減:管銷費用
	*/
  public void setManageFee(BigDecimal manageFee) {
    this.manageFee = manageFee;
  }

/**
	* 營業損益<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getBusLossProfit() {
    return this.busLossProfit;
  }

/**
	* 營業損益<br>
	* 
  *
  * @param busLossProfit 營業損益
	*/
  public void setBusLossProfit(BigDecimal busLossProfit) {
    this.busLossProfit = busLossProfit;
  }

/**
	* 加︰營業外收入<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getBusOtherIncome() {
    return this.busOtherIncome;
  }

/**
	* 加︰營業外收入<br>
	* 
  *
  * @param busOtherIncome 加︰營業外收入
	*/
  public void setBusOtherIncome(BigDecimal busOtherIncome) {
    this.busOtherIncome = busOtherIncome;
  }

/**
	* 減︰利息支出<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getInterest() {
    return this.interest;
  }

/**
	* 減︰利息支出<br>
	* 
  *
  * @param interest 減︰利息支出
	*/
  public void setInterest(BigDecimal interest) {
    this.interest = interest;
  }

/**
	* 營業外費用<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getBusOtherFee() {
    return this.busOtherFee;
  }

/**
	* 營業外費用<br>
	* 
  *
  * @param busOtherFee 營業外費用
	*/
  public void setBusOtherFee(BigDecimal busOtherFee) {
    this.busOtherFee = busOtherFee;
  }

/**
	* 稅前淨利<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getBeforeTaxNet() {
    return this.beforeTaxNet;
  }

/**
	* 稅前淨利<br>
	* 
  *
  * @param beforeTaxNet 稅前淨利
	*/
  public void setBeforeTaxNet(BigDecimal beforeTaxNet) {
    this.beforeTaxNet = beforeTaxNet;
  }

/**
	* 減︰營利事業所得稅<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getBusTax() {
    return this.busTax;
  }

/**
	* 減︰營利事業所得稅<br>
	* 
  *
  * @param busTax 減︰營利事業所得稅
	*/
  public void setBusTax(BigDecimal busTax) {
    this.busTax = busTax;
  }

/**
	* 本期損益<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getHomeLossProfit() {
    return this.homeLossProfit;
  }

/**
	* 本期損益<br>
	* 
  *
  * @param homeLossProfit 本期損益
	*/
  public void setHomeLossProfit(BigDecimal homeLossProfit) {
    this.homeLossProfit = homeLossProfit;
  }

/**
	* 其他綜合損益<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getOtherComLossProfit() {
    return this.otherComLossProfit;
  }

/**
	* 其他綜合損益<br>
	* 
  *
  * @param otherComLossProfit 其他綜合損益
	*/
  public void setOtherComLossProfit(BigDecimal otherComLossProfit) {
    this.otherComLossProfit = otherComLossProfit;
  }

/**
	* 本期綜合損益總額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getHomeComLossProfit() {
    return this.homeComLossProfit;
  }

/**
	* 本期綜合損益總額<br>
	* 
  *
  * @param homeComLossProfit 本期綜合損益總額
	*/
  public void setHomeComLossProfit(BigDecimal homeComLossProfit) {
    this.homeComLossProfit = homeComLossProfit;
  }

/**
	* 非控制權益<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getUncontrolRight() {
    return this.uncontrolRight;
  }

/**
	* 非控制權益<br>
	* 
  *
  * @param uncontrolRight 非控制權益
	*/
  public void setUncontrolRight(BigDecimal uncontrolRight) {
    this.uncontrolRight = uncontrolRight;
  }

/**
	* 歸屬於母公司之權益<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getParentCompanyRight() {
    return this.parentCompanyRight;
  }

/**
	* 歸屬於母公司之權益<br>
	* 
  *
  * @param parentCompanyRight 歸屬於母公司之權益
	*/
  public void setParentCompanyRight(BigDecimal parentCompanyRight) {
    this.parentCompanyRight = parentCompanyRight;
  }

/**
	* 每股盈餘EPS(元)<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getEPS() {
    return this.ePS;
  }

/**
	* 每股盈餘EPS(元)<br>
	* 
  *
  * @param ePS 每股盈餘EPS(元)
	*/
  public void setEPS(BigDecimal ePS) {
    this.ePS = ePS;
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
    return "FinReportProfit [finReportProfitId=" + finReportProfitId + ", busIncome=" + busIncome + ", growRate=" + growRate + ", busCost=" + busCost + ", busGrossProfit=" + busGrossProfit
           + ", manageFee=" + manageFee + ", busLossProfit=" + busLossProfit + ", busOtherIncome=" + busOtherIncome + ", interest=" + interest + ", busOtherFee=" + busOtherFee + ", beforeTaxNet=" + beforeTaxNet
           + ", busTax=" + busTax + ", homeLossProfit=" + homeLossProfit + ", otherComLossProfit=" + otherComLossProfit + ", homeComLossProfit=" + homeComLossProfit + ", uncontrolRight=" + uncontrolRight + ", parentCompanyRight=" + parentCompanyRight
           + ", ePS=" + ePS + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
