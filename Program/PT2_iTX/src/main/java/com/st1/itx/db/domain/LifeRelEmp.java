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
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * LifeRelEmp 人壽利關人職員檔 T07_2
(使用報表：LM013)<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`LifeRelEmp`")
public class LifeRelEmp implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -5397050247741508377L;

@EmbeddedId
  private LifeRelEmpId lifeRelEmpId;

  // 會計日期
  @Column(name = "`AcDate`", insertable = false, updatable = false)
  private int acDate = 0;

  // 職員身分證/統一編號
  @Column(name = "`EmpId`", length = 10, insertable = false, updatable = false)
  private String empId;

  // 職員名稱
  @Column(name = "`EmpName`", length = 100)
  private String empName;

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


  public LifeRelEmpId getLifeRelEmpId() {
    return this.lifeRelEmpId;
  }

  public void setLifeRelEmpId(LifeRelEmpId lifeRelEmpId) {
    this.lifeRelEmpId = lifeRelEmpId;
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
	* 職員身分證/統一編號<br>
	* 
	* @return String
	*/
  public String getEmpId() {
    return this.empId == null ? "" : this.empId;
  }

/**
	* 職員身分證/統一編號<br>
	* 
  *
  * @param empId 職員身分證/統一編號
	*/
  public void setEmpId(String empId) {
    this.empId = empId;
  }

/**
	* 職員名稱<br>
	* 
	* @return String
	*/
  public String getEmpName() {
    return this.empName == null ? "" : this.empName;
  }

/**
	* 職員名稱<br>
	* 
  *
  * @param empName 職員名稱
	*/
  public void setEmpName(String empName) {
    this.empName = empName;
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
    return "LifeRelEmp [lifeRelEmpId=" + lifeRelEmpId + ", empName=" + empName + ", loanBalance=" + loanBalance + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
           + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
