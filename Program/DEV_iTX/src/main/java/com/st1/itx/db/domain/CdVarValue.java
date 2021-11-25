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
 * CdVarValue 會計變動數值設定檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CdVarValue`")
public class CdVarValue implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 485964143532135739L;

// 年月
  /* 每月一筆 */
  @Id
  @Column(name = "`YearMonth`")
  private int yearMonth = 0;

  // 可運用資金
  /* L6502維護，70000000000 */
  @Column(name = "`AvailableFunds`")
  private BigDecimal availableFunds = new BigDecimal("0");

  // 總借款限額
  /* L6502維護，可運用資金的35% */
  @Column(name = "`LoanTotalLmt`")
  private BigDecimal loanTotalLmt = new BigDecimal("0");

  // 無擔保限額
  /* L6502維護，0 */
  @Column(name = "`NoGurTotalLmt`")
  private BigDecimal noGurTotalLmt = new BigDecimal("0");

  // 股東權益(淨值)
  /* L6502維護 */
  @Column(name = "`Totalequity`")
  private BigDecimal totalequity = new BigDecimal("0");

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
	* 年月<br>
	* 每月一筆
	* @return Integer
	*/
  public int getYearMonth() {
    return this.yearMonth;
  }

/**
	* 年月<br>
	* 每月一筆
  *
  * @param yearMonth 年月
	*/
  public void setYearMonth(int yearMonth) {
    this.yearMonth = yearMonth;
  }

/**
	* 可運用資金<br>
	* L6502維護，70000000000
	* @return BigDecimal
	*/
  public BigDecimal getAvailableFunds() {
    return this.availableFunds;
  }

/**
	* 可運用資金<br>
	* L6502維護，70000000000
  *
  * @param availableFunds 可運用資金
	*/
  public void setAvailableFunds(BigDecimal availableFunds) {
    this.availableFunds = availableFunds;
  }

/**
	* 總借款限額<br>
	* L6502維護，可運用資金的35%
	* @return BigDecimal
	*/
  public BigDecimal getLoanTotalLmt() {
    return this.loanTotalLmt;
  }

/**
	* 總借款限額<br>
	* L6502維護，可運用資金的35%
  *
  * @param loanTotalLmt 總借款限額
	*/
  public void setLoanTotalLmt(BigDecimal loanTotalLmt) {
    this.loanTotalLmt = loanTotalLmt;
  }

/**
	* 無擔保限額<br>
	* L6502維護，0
	* @return BigDecimal
	*/
  public BigDecimal getNoGurTotalLmt() {
    return this.noGurTotalLmt;
  }

/**
	* 無擔保限額<br>
	* L6502維護，0
  *
  * @param noGurTotalLmt 無擔保限額
	*/
  public void setNoGurTotalLmt(BigDecimal noGurTotalLmt) {
    this.noGurTotalLmt = noGurTotalLmt;
  }

/**
	* 股東權益(淨值)<br>
	* L6502維護
	* @return BigDecimal
	*/
  public BigDecimal getTotalequity() {
    return this.totalequity;
  }

/**
	* 股東權益(淨值)<br>
	* L6502維護
  *
  * @param totalequity 股東權益(淨值)
	*/
  public void setTotalequity(BigDecimal totalequity) {
    this.totalequity = totalequity;
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
    return "CdVarValue [yearMonth=" + yearMonth + ", availableFunds=" + availableFunds + ", loanTotalLmt=" + loanTotalLmt + ", noGurTotalLmt=" + noGurTotalLmt + ", totalequity=" + totalequity + ", createDate=" + createDate
           + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
