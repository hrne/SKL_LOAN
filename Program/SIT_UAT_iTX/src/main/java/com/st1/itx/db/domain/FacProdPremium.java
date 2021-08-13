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
 * FacProdPremium 商品參數副檔年繳保費優惠減碼<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`FacProdPremium`")
public class FacProdPremium implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 2372839584222801179L;

@EmbeddedId
  private FacProdPremiumId facProdPremiumId;

  // 商品代碼
  @Column(name = "`ProdNo`", length = 5, insertable = false, updatable = false)
  private String prodNo;

  // 保戶壽險年繳化保費(含)以上
  @Column(name = "`PremiumLow`", insertable = false, updatable = false)
  private BigDecimal premiumLow = new BigDecimal("0");

  // 保戶壽險年繳化保費(含)以下
  @Column(name = "`PremiumHigh`")
  private BigDecimal premiumHigh = new BigDecimal("0");

  // 優惠減碼
  @Column(name = "`PremiumIncr`")
  private BigDecimal premiumIncr = new BigDecimal("0");

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


  public FacProdPremiumId getFacProdPremiumId() {
    return this.facProdPremiumId;
  }

  public void setFacProdPremiumId(FacProdPremiumId facProdPremiumId) {
    this.facProdPremiumId = facProdPremiumId;
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
	* 保戶壽險年繳化保費(含)以上<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getPremiumLow() {
    return this.premiumLow;
  }

/**
	* 保戶壽險年繳化保費(含)以上<br>
	* 
  *
  * @param premiumLow 保戶壽險年繳化保費(含)以上
	*/
  public void setPremiumLow(BigDecimal premiumLow) {
    this.premiumLow = premiumLow;
  }

/**
	* 保戶壽險年繳化保費(含)以下<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getPremiumHigh() {
    return this.premiumHigh;
  }

/**
	* 保戶壽險年繳化保費(含)以下<br>
	* 
  *
  * @param premiumHigh 保戶壽險年繳化保費(含)以下
	*/
  public void setPremiumHigh(BigDecimal premiumHigh) {
    this.premiumHigh = premiumHigh;
  }

/**
	* 優惠減碼<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getPremiumIncr() {
    return this.premiumIncr;
  }

/**
	* 優惠減碼<br>
	* 
  *
  * @param premiumIncr 優惠減碼
	*/
  public void setPremiumIncr(BigDecimal premiumIncr) {
    this.premiumIncr = premiumIncr;
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
    return "FacProdPremium [facProdPremiumId=" + facProdPremiumId + ", premiumHigh=" + premiumHigh + ", premiumIncr=" + premiumIncr + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
           + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
