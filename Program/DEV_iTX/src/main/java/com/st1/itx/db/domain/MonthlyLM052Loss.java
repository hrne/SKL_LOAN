package com.st1.itx.db.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.Id;
import javax.persistence.Column;

/**
 * MonthlyLM052Loss LM052備抵損失資料檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`MonthlyLM052Loss`")
public class MonthlyLM052Loss implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 3991625091063406154L;

// 資料年月
  @Id
  @Column(name = "`YearMonth`")
  private int yearMonth = 0;

  // 五類資產評估合計
  @Column(name = "`AssetEvaTotal`")
  private BigDecimal assetEvaTotal = new BigDecimal("0");

  // 法定備抵損失提撥
  @Column(name = "`LegalLoss`")
  private BigDecimal legalLoss = new BigDecimal("0");

  // 會計部核定備抵損失
  @Column(name = "`ApprovedLoss`")
  private BigDecimal approvedLoss = new BigDecimal("0");

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
	* 五類資產評估合計<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getAssetEvaTotal() {
    return this.assetEvaTotal;
  }

/**
	* 五類資產評估合計<br>
	* 
  *
  * @param assetEvaTotal 五類資產評估合計
	*/
  public void setAssetEvaTotal(BigDecimal assetEvaTotal) {
    this.assetEvaTotal = assetEvaTotal;
  }

/**
	* 法定備抵損失提撥<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLegalLoss() {
    return this.legalLoss;
  }

/**
	* 法定備抵損失提撥<br>
	* 
  *
  * @param legalLoss 法定備抵損失提撥
	*/
  public void setLegalLoss(BigDecimal legalLoss) {
    this.legalLoss = legalLoss;
  }

/**
	* 會計部核定備抵損失<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getApprovedLoss() {
    return this.approvedLoss;
  }

/**
	* 會計部核定備抵損失<br>
	* 
  *
  * @param approvedLoss 會計部核定備抵損失
	*/
  public void setApprovedLoss(BigDecimal approvedLoss) {
    this.approvedLoss = approvedLoss;
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
    return "MonthlyLM052Loss [yearMonth=" + yearMonth + ", assetEvaTotal=" + assetEvaTotal + ", legalLoss=" + legalLoss + ", approvedLoss=" + approvedLoss + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
           + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
