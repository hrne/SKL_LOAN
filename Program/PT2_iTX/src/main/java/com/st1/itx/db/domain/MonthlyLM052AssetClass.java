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
 * MonthlyLM052AssetClass LM052資產分類表<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`MonthlyLM052AssetClass`")
public class MonthlyLM052AssetClass implements Serializable {


  @EmbeddedId
  private MonthlyLM052AssetClassId monthlyLM052AssetClassId;

  // 資料年月
  @Column(name = "`YearMonth`", insertable = false, updatable = false)
  private int yearMonth = 0;

  // 資產五分類
  /* 11:一類(正常繳息)12:一類(特定放款資產項目)21/22/23:二類3:三類4:四類5:五類61:擔保放款折溢價62:催收折溢價與催收費用7:應收利息提列 */
  @Column(name = "`AssetClassNo`", length = 2, insertable = false, updatable = false)
  private String assetClassNo;

  // 區隔帳冊
  /* 00A:一般201:利變 */
  @Column(name = "`AcSubBookCode`", length = 3, insertable = false, updatable = false)
  private String acSubBookCode;

  // 放款餘額
  @Column(name = "`LoanBal`")
  private BigDecimal loanBal = new BigDecimal("0");

  // 提存比率
  @Column(name = "`StorageRate`")
  private BigDecimal storageRate = new BigDecimal("0");

  // 提存金額
  @Column(name = "`StorageAmt`")
  private BigDecimal storageAmt = new BigDecimal("0");

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


  public MonthlyLM052AssetClassId getMonthlyLM052AssetClassId() {
    return this.monthlyLM052AssetClassId;
  }

  public void setMonthlyLM052AssetClassId(MonthlyLM052AssetClassId monthlyLM052AssetClassId) {
    this.monthlyLM052AssetClassId = monthlyLM052AssetClassId;
  }

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
	* 資產五分類<br>
	* 11:一類(正常繳息)
12:一類(特定放款資產項目)
21/22/23:二類
3:三類
4:四類
5:五類
61:擔保放款折溢價
62:催收折溢價與催收費用
7:應收利息提列
	* @return String
	*/
  public String getAssetClassNo() {
    return this.assetClassNo == null ? "" : this.assetClassNo;
  }

/**
	* 資產五分類<br>
	* 11:一類(正常繳息)
12:一類(特定放款資產項目)
21/22/23:二類
3:三類
4:四類
5:五類
61:擔保放款折溢價
62:催收折溢價與催收費用
7:應收利息提列
  *
  * @param assetClassNo 資產五分類
	*/
  public void setAssetClassNo(String assetClassNo) {
    this.assetClassNo = assetClassNo;
  }

/**
	* 區隔帳冊<br>
	* 00A:一般
201:利變
	* @return String
	*/
  public String getAcSubBookCode() {
    return this.acSubBookCode == null ? "" : this.acSubBookCode;
  }

/**
	* 區隔帳冊<br>
	* 00A:一般
201:利變
  *
  * @param acSubBookCode 區隔帳冊
	*/
  public void setAcSubBookCode(String acSubBookCode) {
    this.acSubBookCode = acSubBookCode;
  }

/**
	* 放款餘額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLoanBal() {
    return this.loanBal;
  }

/**
	* 放款餘額<br>
	* 
  *
  * @param loanBal 放款餘額
	*/
  public void setLoanBal(BigDecimal loanBal) {
    this.loanBal = loanBal;
  }

/**
	* 提存比率<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getStorageRate() {
    return this.storageRate;
  }

/**
	* 提存比率<br>
	* 
  *
  * @param storageRate 提存比率
	*/
  public void setStorageRate(BigDecimal storageRate) {
    this.storageRate = storageRate;
  }

/**
	* 提存金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getStorageAmt() {
    return this.storageAmt;
  }

/**
	* 提存金額<br>
	* 
  *
  * @param storageAmt 提存金額
	*/
  public void setStorageAmt(BigDecimal storageAmt) {
    this.storageAmt = storageAmt;
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
    return "MonthlyLM052AssetClass [monthlyLM052AssetClassId=" + monthlyLM052AssetClassId + ", loanBal=" + loanBal + ", storageRate=" + storageRate + ", storageAmt=" + storageAmt
           + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
