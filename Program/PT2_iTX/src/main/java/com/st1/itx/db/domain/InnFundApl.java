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
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * InnFundApl 資金運用概況檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`InnFundApl`")
public class InnFundApl implements Serializable {


  // 日期
  @Id
  @Column(name = "`AcDate`")
  private int acDate = 0;

  // 責任準備金
  @Column(name = "`ResrvStndrd`")
  private BigDecimal resrvStndrd = new BigDecimal("0");

  // 可放款比率%
  @Column(name = "`PosbleBorPsn`")
  private BigDecimal posbleBorPsn = new BigDecimal("0");

  // 可放款金額
  @Column(name = "`PosbleBorAmt`")
  private BigDecimal posbleBorAmt = new BigDecimal("0");

  // 已放款金額
  @Column(name = "`AlrdyBorAmt`")
  private BigDecimal alrdyBorAmt = new BigDecimal("0");

  // 股東權益
  @Column(name = "`StockHoldersEqt`")
  private BigDecimal stockHoldersEqt = new BigDecimal("0");

  // 可運用資金
  @Column(name = "`AvailableFunds`")
  private BigDecimal availableFunds = new BigDecimal("0");

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
	* 日期<br>
	* 
	* @return Integer
	*/
  public int getAcDate() {
    return StaticTool.bcToRoc(this.acDate);
  }

/**
	* 日期<br>
	* 
  *
  * @param acDate 日期
  * @throws LogicException when Date Is Warn	*/
  public void setAcDate(int acDate) throws LogicException {
    this.acDate = StaticTool.rocToBc(acDate);
  }

/**
	* 責任準備金<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getResrvStndrd() {
    return this.resrvStndrd;
  }

/**
	* 責任準備金<br>
	* 
  *
  * @param resrvStndrd 責任準備金
	*/
  public void setResrvStndrd(BigDecimal resrvStndrd) {
    this.resrvStndrd = resrvStndrd;
  }

/**
	* 可放款比率%<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getPosbleBorPsn() {
    return this.posbleBorPsn;
  }

/**
	* 可放款比率%<br>
	* 
  *
  * @param posbleBorPsn 可放款比率%
	*/
  public void setPosbleBorPsn(BigDecimal posbleBorPsn) {
    this.posbleBorPsn = posbleBorPsn;
  }

/**
	* 可放款金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getPosbleBorAmt() {
    return this.posbleBorAmt;
  }

/**
	* 可放款金額<br>
	* 
  *
  * @param posbleBorAmt 可放款金額
	*/
  public void setPosbleBorAmt(BigDecimal posbleBorAmt) {
    this.posbleBorAmt = posbleBorAmt;
  }

/**
	* 已放款金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getAlrdyBorAmt() {
    return this.alrdyBorAmt;
  }

/**
	* 已放款金額<br>
	* 
  *
  * @param alrdyBorAmt 已放款金額
	*/
  public void setAlrdyBorAmt(BigDecimal alrdyBorAmt) {
    this.alrdyBorAmt = alrdyBorAmt;
  }

/**
	* 股東權益<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getStockHoldersEqt() {
    return this.stockHoldersEqt;
  }

/**
	* 股東權益<br>
	* 
  *
  * @param stockHoldersEqt 股東權益
	*/
  public void setStockHoldersEqt(BigDecimal stockHoldersEqt) {
    this.stockHoldersEqt = stockHoldersEqt;
  }

/**
	* 可運用資金<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getAvailableFunds() {
    return this.availableFunds;
  }

/**
	* 可運用資金<br>
	* 
  *
  * @param availableFunds 可運用資金
	*/
  public void setAvailableFunds(BigDecimal availableFunds) {
    this.availableFunds = availableFunds;
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
    return "InnFundApl [acDate=" + acDate + ", resrvStndrd=" + resrvStndrd + ", posbleBorPsn=" + posbleBorPsn + ", posbleBorAmt=" + posbleBorAmt + ", alrdyBorAmt=" + alrdyBorAmt + ", stockHoldersEqt=" + stockHoldersEqt
           + ", availableFunds=" + availableFunds + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
