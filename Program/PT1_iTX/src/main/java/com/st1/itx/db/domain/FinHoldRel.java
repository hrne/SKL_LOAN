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
 * FinHoldRel 金控利關人名單檔 T044
(使用報表：LM049、LQ005)<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`FinHoldRel`")
public class FinHoldRel implements Serializable {


  @EmbeddedId
  private FinHoldRelId finHoldRelId;

  // 會計日期
  @Column(name = "`AcDate`", insertable = false, updatable = false)
  private int acDate = 0;

  // 所在公司
  @Column(name = "`CompanyName`", length = 100)
  private String companyName;

  // 身分證/統一編號
  @Column(name = "`Id`", length = 10, insertable = false, updatable = false)
  private String id;

  // 姓名
  @Column(name = "`Name`", length = 100)
  private String name;

  // 職務
  /* 暫不以代號列入：A：董事長B：副董事長C：董事D：監事E：總經理F：副總經理G：協理H：經理I：副理J：其他 */
  @Column(name = "`BusTitle`", length = 100)
  private String busTitle;

  // 核貸金額
  @Column(name = "`LineAmt`")
  private BigDecimal lineAmt = new BigDecimal("0");

  // 放款金額
  @Column(name = "`LoanBalance`")
  private BigDecimal loanBalance = new BigDecimal("0");

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


  public FinHoldRelId getFinHoldRelId() {
    return this.finHoldRelId;
  }

  public void setFinHoldRelId(FinHoldRelId finHoldRelId) {
    this.finHoldRelId = finHoldRelId;
  }

/**
	* 會計日期<br>
	* 
	* @return Integer
	*/
  public int getAcDate() {
    return StaticTool.bcToRoc(this.acDate);
  }

/**
	* 會計日期<br>
	* 
  *
  * @param acDate 會計日期
  * @throws LogicException when Date Is Warn	*/
  public void setAcDate(int acDate) throws LogicException {
    this.acDate = StaticTool.rocToBc(acDate);
  }

/**
	* 所在公司<br>
	* 
	* @return String
	*/
  public String getCompanyName() {
    return this.companyName == null ? "" : this.companyName;
  }

/**
	* 所在公司<br>
	* 
  *
  * @param companyName 所在公司
	*/
  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

/**
	* 身分證/統一編號<br>
	* 
	* @return String
	*/
  public String getId() {
    return this.id == null ? "" : this.id;
  }

/**
	* 身分證/統一編號<br>
	* 
  *
  * @param id 身分證/統一編號
	*/
  public void setId(String id) {
    this.id = id;
  }

/**
	* 姓名<br>
	* 
	* @return String
	*/
  public String getName() {
    return this.name == null ? "" : this.name;
  }

/**
	* 姓名<br>
	* 
  *
  * @param name 姓名
	*/
  public void setName(String name) {
    this.name = name;
  }

/**
	* 職務<br>
	* 暫不以代號列入：
A：董事長
B：副董事長
C：董事
D：監事
E：總經理
F：副總經理
G：協理
H：經理
I：副理
J：其他
	* @return String
	*/
  public String getBusTitle() {
    return this.busTitle == null ? "" : this.busTitle;
  }

/**
	* 職務<br>
	* 暫不以代號列入：
A：董事長
B：副董事長
C：董事
D：監事
E：總經理
F：副總經理
G：協理
H：經理
I：副理
J：其他
  *
  * @param busTitle 職務
	*/
  public void setBusTitle(String busTitle) {
    this.busTitle = busTitle;
  }

/**
	* 核貸金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLineAmt() {
    return this.lineAmt;
  }

/**
	* 核貸金額<br>
	* 
  *
  * @param lineAmt 核貸金額
	*/
  public void setLineAmt(BigDecimal lineAmt) {
    this.lineAmt = lineAmt;
  }

/**
	* 放款金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLoanBalance() {
    return this.loanBalance;
  }

/**
	* 放款金額<br>
	* 
  *
  * @param loanBalance 放款金額
	*/
  public void setLoanBalance(BigDecimal loanBalance) {
    this.loanBalance = loanBalance;
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
    return "FinHoldRel [finHoldRelId=" + finHoldRelId + ", companyName=" + companyName + ", name=" + name + ", busTitle=" + busTitle + ", lineAmt=" + lineAmt
           + ", loanBalance=" + loanBalance + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
