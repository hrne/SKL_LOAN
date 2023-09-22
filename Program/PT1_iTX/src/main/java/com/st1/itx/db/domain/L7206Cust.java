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
import javax.persistence.SequenceGenerator;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

/**
 * L7206Cust 利害關係人借款人檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`L7206Cust`")
public class L7206Cust implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -555268454203616687L;

// 序號
  /* 產出LNM63H1P.txt逗點分隔且固定長度7個欄位長度加總為98加上6個逗點為104 */
  @Id
  @Column(name = "`LogNo`")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "`L7206Cust_SEQ`")
  @SequenceGenerator(name = "`L7206Cust_SEQ`", sequenceName = "`L7206Cust_SEQ`", allocationSize = 1)
  private Long logNo = 0L;

  // 身分證/統一編號
  /* 借款戶身分證/統一編號 */
  @Column(name = "`CustId`", length = 10)
  private String custId;

  // 姓名
  /* 借款戶姓名 */
  @Column(name = "`CustName`", length = 42)
  private String custName;

  // 戶號
  /* 借款戶戶號 */
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 額度號碼
  /* 該借款戶之放款餘額大於零的額度號碼逐筆列出 */
  @Column(name = "`FacmNo`")
  private int facmNo = 0;

  // 資料年月
  /* 西元年月YYYYMM */
  @Column(name = "`DataMonth`")
  private int dataMonth = 0;

  // 平均核准額度
  /* 實際為該額度的核准額度,並未有平均之計算動作,維持原命名為遵照AS400 */
  @Column(name = "`AvgLineAmt`")
  private BigDecimal avgLineAmt = new BigDecimal("0");

  // 合計放款餘額
  /* 該額度所有撥款序號的放款餘額加總 */
  @Column(name = "`SumLoanBal`")
  private BigDecimal sumLoanBal = new BigDecimal("0");

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
	* 序號<br>
	* 產出LNM63H1P.txt
逗點分隔且固定長度
7個欄位長度加總為98
加上6個逗點為104
	* @return Long
	*/
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long getLogNo() {
    return this.logNo;
  }

/**
	* 序號<br>
	* 產出LNM63H1P.txt
逗點分隔且固定長度
7個欄位長度加總為98
加上6個逗點為104
  *
  * @param logNo 序號
	*/
  public void setLogNo(Long logNo) {
    this.logNo = logNo;
  }

/**
	* 身分證/統一編號<br>
	* 借款戶身分證/統一編號
	* @return String
	*/
  public String getCustId() {
    return this.custId == null ? "" : this.custId;
  }

/**
	* 身分證/統一編號<br>
	* 借款戶身分證/統一編號
  *
  * @param custId 身分證/統一編號
	*/
  public void setCustId(String custId) {
    this.custId = custId;
  }

/**
	* 姓名<br>
	* 借款戶姓名
	* @return String
	*/
  public String getCustName() {
    return this.custName == null ? "" : this.custName;
  }

/**
	* 姓名<br>
	* 借款戶姓名
  *
  * @param custName 姓名
	*/
  public void setCustName(String custName) {
    this.custName = custName;
  }

/**
	* 戶號<br>
	* 借款戶戶號
	* @return Integer
	*/
  public int getCustNo() {
    return this.custNo;
  }

/**
	* 戶號<br>
	* 借款戶戶號
  *
  * @param custNo 戶號
	*/
  public void setCustNo(int custNo) {
    this.custNo = custNo;
  }

/**
	* 額度號碼<br>
	* 該借款戶之放款餘額大於零的額度號碼逐筆列出
	* @return Integer
	*/
  public int getFacmNo() {
    return this.facmNo;
  }

/**
	* 額度號碼<br>
	* 該借款戶之放款餘額大於零的額度號碼逐筆列出
  *
  * @param facmNo 額度號碼
	*/
  public void setFacmNo(int facmNo) {
    this.facmNo = facmNo;
  }

/**
	* 資料年月<br>
	* 西元年月YYYYMM
	* @return Integer
	*/
  public int getDataMonth() {
    return this.dataMonth;
  }

/**
	* 資料年月<br>
	* 西元年月YYYYMM
  *
  * @param dataMonth 資料年月
	*/
  public void setDataMonth(int dataMonth) {
    this.dataMonth = dataMonth;
  }

/**
	* 平均核准額度<br>
	* 實際為該額度的核准額度,並未有平均之計算動作,維持原命名為遵照AS400
	* @return BigDecimal
	*/
  public BigDecimal getAvgLineAmt() {
    return this.avgLineAmt;
  }

/**
	* 平均核准額度<br>
	* 實際為該額度的核准額度,並未有平均之計算動作,維持原命名為遵照AS400
  *
  * @param avgLineAmt 平均核准額度
	*/
  public void setAvgLineAmt(BigDecimal avgLineAmt) {
    this.avgLineAmt = avgLineAmt;
  }

/**
	* 合計放款餘額<br>
	* 該額度所有撥款序號的放款餘額加總
	* @return BigDecimal
	*/
  public BigDecimal getSumLoanBal() {
    return this.sumLoanBal;
  }

/**
	* 合計放款餘額<br>
	* 該額度所有撥款序號的放款餘額加總
  *
  * @param sumLoanBal 合計放款餘額
	*/
  public void setSumLoanBal(BigDecimal sumLoanBal) {
    this.sumLoanBal = sumLoanBal;
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
    return "L7206Cust [logNo=" + logNo + ", custId=" + custId + ", custName=" + custName + ", custNo=" + custNo + ", facmNo=" + facmNo + ", dataMonth=" + dataMonth
           + ", avgLineAmt=" + avgLineAmt + ", sumLoanBal=" + sumLoanBal + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo
           + "]";
  }
}
