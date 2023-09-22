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
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * L7206Emp 利害關係人員工檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`L7206Emp`")
public class L7206Emp implements Serializable {


  // 序號
  /* 產出LNM63H3P.txt逗點分隔且固定長度5個欄位長度加總為80加上4個逗點為84 */
  @Id
  @Column(name = "`LogNo`")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "`L7206Emp_SEQ`")
  @SequenceGenerator(name = "`L7206Emp_SEQ`", sequenceName = "`L7206Emp_SEQ`", allocationSize = 1)
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

  // 資料年月
  /* 西元年月YYYYMM */
  @Column(name = "`DataMonth`")
  private int dataMonth = 0;

  // 合計放款餘額
  /* 借款戶放款餘額加總 */
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
	* 產出LNM63H3P.txt
逗點分隔且固定長度
5個欄位長度加總為80
加上4個逗點為84
	* @return Long
	*/
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long getLogNo() {
    return this.logNo;
  }

/**
	* 序號<br>
	* 產出LNM63H3P.txt
逗點分隔且固定長度
5個欄位長度加總為80
加上4個逗點為84
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
	* 合計放款餘額<br>
	* 借款戶放款餘額加總
	* @return BigDecimal
	*/
  public BigDecimal getSumLoanBal() {
    return this.sumLoanBal;
  }

/**
	* 合計放款餘額<br>
	* 借款戶放款餘額加總
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
    return "L7206Emp [logNo=" + logNo + ", custId=" + custId + ", custName=" + custName + ", custNo=" + custNo + ", dataMonth=" + dataMonth + ", sumLoanBal=" + sumLoanBal
           + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
