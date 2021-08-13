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
 * CdBudget 利息收入預算檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CdBudget`")
public class CdBudget implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 5895742889271261302L;

@EmbeddedId
  private CdBudgetId cdBudgetId;

  // 預算年度
  @Column(name = "`Year`", insertable = false, updatable = false)
  private int year = 0;

  // 預算月份
  /* 12個月 */
  @Column(name = "`Month`", insertable = false, updatable = false)
  private int month = 0;

  // 預算數
  @Column(name = "`Budget`")
  private BigDecimal budget = new BigDecimal("0");

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


  public CdBudgetId getCdBudgetId() {
    return this.cdBudgetId;
  }

  public void setCdBudgetId(CdBudgetId cdBudgetId) {
    this.cdBudgetId = cdBudgetId;
  }

/**
	* 預算年度<br>
	* 
	* @return Integer
	*/
  public int getYear() {
    return this.year;
  }

/**
	* 預算年度<br>
	* 
  *
  * @param year 預算年度
	*/
  public void setYear(int year) {
    this.year = year;
  }

/**
	* 預算月份<br>
	* 12個月
	* @return Integer
	*/
  public int getMonth() {
    return this.month;
  }

/**
	* 預算月份<br>
	* 12個月
  *
  * @param month 預算月份
	*/
  public void setMonth(int month) {
    this.month = month;
  }

/**
	* 預算數<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getBudget() {
    return this.budget;
  }

/**
	* 預算數<br>
	* 
  *
  * @param budget 預算數
	*/
  public void setBudget(BigDecimal budget) {
    this.budget = budget;
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
    return "CdBudget [cdBudgetId=" + cdBudgetId + ", budget=" + budget + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
           + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
