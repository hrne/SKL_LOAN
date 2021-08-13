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
 * Ias39IntMethod 利息法帳面資料檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`Ias39IntMethod`")
public class Ias39IntMethod implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -8861790140270766888L;

@EmbeddedId
  private Ias39IntMethodId ias39IntMethodId;

  // 年月份
  @Column(name = "`YearMonth`", insertable = false, updatable = false)
  private int yearMonth = 0;

  // 戶號
  @Column(name = "`CustNo`", insertable = false, updatable = false)
  private int custNo = 0;

  // 額度編號
  @Column(name = "`FacmNo`", insertable = false, updatable = false)
  private int facmNo = 0;

  // 撥款序號
  @Column(name = "`BormNo`", insertable = false, updatable = false)
  private int bormNo = 0;

  // 本期本金餘額
  @Column(name = "`Principal`")
  private BigDecimal principal = new BigDecimal("0");

  // 本期帳面價值
  @Column(name = "`BookValue`")
  private BigDecimal bookValue = new BigDecimal("0");

  // 本期累應攤銷折溢價
  @Column(name = "`AccumDPAmortized`")
  private BigDecimal accumDPAmortized = new BigDecimal("0");

  // 本期累未攤銷折溢價
  @Column(name = "`AccumDPunAmortized`")
  private BigDecimal accumDPunAmortized = new BigDecimal("0");

  // 本期折溢價攤銷數
  @Column(name = "`DPAmortized`")
  private BigDecimal dPAmortized = new BigDecimal("0");

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


  public Ias39IntMethodId getIas39IntMethodId() {
    return this.ias39IntMethodId;
  }

  public void setIas39IntMethodId(Ias39IntMethodId ias39IntMethodId) {
    this.ias39IntMethodId = ias39IntMethodId;
  }

/**
	* 年月份<br>
	* 
	* @return Integer
	*/
  public int getYearMonth() {
    return this.yearMonth;
  }

/**
	* 年月份<br>
	* 
  *
  * @param yearMonth 年月份
	*/
  public void setYearMonth(int yearMonth) {
    this.yearMonth = yearMonth;
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
	* 本期本金餘額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getPrincipal() {
    return this.principal;
  }

/**
	* 本期本金餘額<br>
	* 
  *
  * @param principal 本期本金餘額
	*/
  public void setPrincipal(BigDecimal principal) {
    this.principal = principal;
  }

/**
	* 本期帳面價值<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getBookValue() {
    return this.bookValue;
  }

/**
	* 本期帳面價值<br>
	* 
  *
  * @param bookValue 本期帳面價值
	*/
  public void setBookValue(BigDecimal bookValue) {
    this.bookValue = bookValue;
  }

/**
	* 本期累應攤銷折溢價<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getAccumDPAmortized() {
    return this.accumDPAmortized;
  }

/**
	* 本期累應攤銷折溢價<br>
	* 
  *
  * @param accumDPAmortized 本期累應攤銷折溢價
	*/
  public void setAccumDPAmortized(BigDecimal accumDPAmortized) {
    this.accumDPAmortized = accumDPAmortized;
  }

/**
	* 本期累未攤銷折溢價<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getAccumDPunAmortized() {
    return this.accumDPunAmortized;
  }

/**
	* 本期累未攤銷折溢價<br>
	* 
  *
  * @param accumDPunAmortized 本期累未攤銷折溢價
	*/
  public void setAccumDPunAmortized(BigDecimal accumDPunAmortized) {
    this.accumDPunAmortized = accumDPunAmortized;
  }

/**
	* 本期折溢價攤銷數<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getDPAmortized() {
    return this.dPAmortized;
  }

/**
	* 本期折溢價攤銷數<br>
	* 
  *
  * @param dPAmortized 本期折溢價攤銷數
	*/
  public void setDPAmortized(BigDecimal dPAmortized) {
    this.dPAmortized = dPAmortized;
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
    return "Ias39IntMethod [ias39IntMethodId=" + ias39IntMethodId + ", principal=" + principal + ", bookValue=" + bookValue
           + ", accumDPAmortized=" + accumDPAmortized + ", accumDPunAmortized=" + accumDPunAmortized + ", dPAmortized=" + dPAmortized + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
           + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
