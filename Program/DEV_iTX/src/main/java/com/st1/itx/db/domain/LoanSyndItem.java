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
 * LoanSyndItem 聯貸案動撥條件檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`LoanSyndItem`")
public class LoanSyndItem implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -8992585753764081371L;

@EmbeddedId
  private LoanSyndItemId loanSyndItemId;

  // 借款人戶號
  @Column(name = "`CustNo`", insertable = false, updatable = false)
  private int custNo = 0;

  // 聯貸案序號
  @Column(name = "`SyndNo`", insertable = false, updatable = false)
  private int syndNo = 0;

  // 項別
  @Column(name = "`Item`", length = 10, insertable = false, updatable = false)
  private String item;

  // 利率基準
  @Column(name = "`Rate`")
  private BigDecimal rate = new BigDecimal("0");

  // 加碼
  @Column(name = "`Incr`")
  private BigDecimal incr = new BigDecimal("0");

  // 動用日期
  @Column(name = "`UseDate`")
  private int useDate = 0;

  // 到期日
  @Column(name = "`MaturityDate`")
  private int maturityDate = 0;

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


  public LoanSyndItemId getLoanSyndItemId() {
    return this.loanSyndItemId;
  }

  public void setLoanSyndItemId(LoanSyndItemId loanSyndItemId) {
    this.loanSyndItemId = loanSyndItemId;
  }

/**
	* 借款人戶號<br>
	* 
	* @return Integer
	*/
  public int getCustNo() {
    return this.custNo;
  }

/**
	* 借款人戶號<br>
	* 
  *
  * @param custNo 借款人戶號
	*/
  public void setCustNo(int custNo) {
    this.custNo = custNo;
  }

/**
	* 聯貸案序號<br>
	* 
	* @return Integer
	*/
  public int getSyndNo() {
    return this.syndNo;
  }

/**
	* 聯貸案序號<br>
	* 
  *
  * @param syndNo 聯貸案序號
	*/
  public void setSyndNo(int syndNo) {
    this.syndNo = syndNo;
  }

/**
	* 項別<br>
	* 
	* @return String
	*/
  public String getItem() {
    return this.item == null ? "" : this.item;
  }

/**
	* 項別<br>
	* 
  *
  * @param item 項別
	*/
  public void setItem(String item) {
    this.item = item;
  }

/**
	* 利率基準<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getRate() {
    return this.rate;
  }

/**
	* 利率基準<br>
	* 
  *
  * @param rate 利率基準
	*/
  public void setRate(BigDecimal rate) {
    this.rate = rate;
  }

/**
	* 加碼<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getIncr() {
    return this.incr;
  }

/**
	* 加碼<br>
	* 
  *
  * @param incr 加碼
	*/
  public void setIncr(BigDecimal incr) {
    this.incr = incr;
  }

/**
	* 動用日期<br>
	* 
	* @return Integer
	*/
  public int getUseDate() {
    return StaticTool.bcToRoc(this.useDate);
  }

/**
	* 動用日期<br>
	* 
  *
  * @param useDate 動用日期
  * @throws LogicException when Date Is Warn	*/
  public void setUseDate(int useDate) throws LogicException {
    this.useDate = StaticTool.rocToBc(useDate);
  }

/**
	* 到期日<br>
	* 
	* @return Integer
	*/
  public int getMaturityDate() {
    return StaticTool.bcToRoc(this.maturityDate);
  }

/**
	* 到期日<br>
	* 
  *
  * @param maturityDate 到期日
  * @throws LogicException when Date Is Warn	*/
  public void setMaturityDate(int maturityDate) throws LogicException {
    this.maturityDate = StaticTool.rocToBc(maturityDate);
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
    return "LoanSyndItem [loanSyndItemId=" + loanSyndItemId + ", rate=" + rate + ", incr=" + incr + ", useDate=" + useDate
           + ", maturityDate=" + maturityDate + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
