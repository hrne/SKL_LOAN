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
 * FacProdStepRate 商品參數副檔階梯式利率<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`FacProdStepRate`")
public class FacProdStepRate implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -6990718784273101507L;

@EmbeddedId
  private FacProdStepRateId facProdStepRateId;

  // 商品代碼或戶號+額度編號
  @Column(name = "`ProdNo`", length = 10, insertable = false, updatable = false)
  private String prodNo;

  // 月數(含)以上
  @Column(name = "`MonthStart`", insertable = false, updatable = false)
  private int monthStart = 0;

  // 月數(含)以下
  @Column(name = "`MonthEnd`")
  private int monthEnd = 0;

  // 利率型態
  /* 1: 固定利率  2: 加碼利率 */
  @Column(name = "`RateType`", length = 1)
  private String rateType;

  // 加碼利率
  @Column(name = "`RateIncr`")
  private BigDecimal rateIncr = new BigDecimal("0");

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


  public FacProdStepRateId getFacProdStepRateId() {
    return this.facProdStepRateId;
  }

  public void setFacProdStepRateId(FacProdStepRateId facProdStepRateId) {
    this.facProdStepRateId = facProdStepRateId;
  }

/**
	* 商品代碼或戶號+額度編號<br>
	* 
	* @return String
	*/
  public String getProdNo() {
    return this.prodNo == null ? "" : this.prodNo;
  }

/**
	* 商品代碼或戶號+額度編號<br>
	* 
  *
  * @param prodNo 商品代碼或戶號+額度編號
	*/
  public void setProdNo(String prodNo) {
    this.prodNo = prodNo;
  }

/**
	* 月數(含)以上<br>
	* 
	* @return Integer
	*/
  public int getMonthStart() {
    return this.monthStart;
  }

/**
	* 月數(含)以上<br>
	* 
  *
  * @param monthStart 月數(含)以上
	*/
  public void setMonthStart(int monthStart) {
    this.monthStart = monthStart;
  }

/**
	* 月數(含)以下<br>
	* 
	* @return Integer
	*/
  public int getMonthEnd() {
    return this.monthEnd;
  }

/**
	* 月數(含)以下<br>
	* 
  *
  * @param monthEnd 月數(含)以下
	*/
  public void setMonthEnd(int monthEnd) {
    this.monthEnd = monthEnd;
  }

/**
	* 利率型態<br>
	* 1: 固定利率  
2: 加碼利率
	* @return String
	*/
  public String getRateType() {
    return this.rateType == null ? "" : this.rateType;
  }

/**
	* 利率型態<br>
	* 1: 固定利率  
2: 加碼利率
  *
  * @param rateType 利率型態
	*/
  public void setRateType(String rateType) {
    this.rateType = rateType;
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
    return "FacProdStepRate [facProdStepRateId=" + facProdStepRateId + ", monthEnd=" + monthEnd + ", rateType=" + rateType + ", rateIncr=" + rateIncr + ", createDate=" + createDate
           + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
