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
 * CoreAcMain 核心會計總帳檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CoreAcMain`")
public class CoreAcMain implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -8650501122958344604L;

@EmbeddedId
  private CoreAcMainId coreAcMainId;

  // 帳冊別
  /* 000:全公司 */
  @Column(name = "`AcBookCode`", length = 3, insertable = false, updatable = false)
  private String acBookCode;

  // 區隔帳冊
  /* 00A:傳統帳冊201:利變年金帳冊 */
  @Column(name = "`AcSubBookCode`", length = 3, insertable = false, updatable = false)
  private String acSubBookCode;

  // 幣別
  @Column(name = "`CurrencyCode`", length = 3, insertable = false, updatable = false)
  private String currencyCode;

  // 科目代號
  @Column(name = "`AcNoCode`", length = 11, insertable = false, updatable = false)
  private String acNoCode;

  // 科目名稱
  @Column(name = "`AcNoName`", length = 100)
  private String acNoName;

  // 子目代號
  @Column(name = "`AcSubCode`", length = 5, insertable = false, updatable = false)
  private String acSubCode;

  // 會計日期
  /* 西元年月日 */
  @Column(name = "`AcDate`", insertable = false, updatable = false)
  private int acDate = 0;

  // 前日餘額
  @Column(name = "`YdBal`")
  private BigDecimal ydBal = new BigDecimal("0");

  // 本日餘額
  @Column(name = "`TdBal`")
  private BigDecimal tdBal = new BigDecimal("0");

  // 借方金額
  @Column(name = "`DbAmt`")
  private BigDecimal dbAmt = new BigDecimal("0");

  // 貸方金額
  @Column(name = "`CrAmt`")
  private BigDecimal crAmt = new BigDecimal("0");

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


  public CoreAcMainId getCoreAcMainId() {
    return this.coreAcMainId;
  }

  public void setCoreAcMainId(CoreAcMainId coreAcMainId) {
    this.coreAcMainId = coreAcMainId;
  }

/**
	* 帳冊別<br>
	* 000:全公司
	* @return String
	*/
  public String getAcBookCode() {
    return this.acBookCode == null ? "" : this.acBookCode;
  }

/**
	* 帳冊別<br>
	* 000:全公司
  *
  * @param acBookCode 帳冊別
	*/
  public void setAcBookCode(String acBookCode) {
    this.acBookCode = acBookCode;
  }

/**
	* 區隔帳冊<br>
	* 00A:傳統帳冊
201:利變年金帳冊
	* @return String
	*/
  public String getAcSubBookCode() {
    return this.acSubBookCode == null ? "" : this.acSubBookCode;
  }

/**
	* 區隔帳冊<br>
	* 00A:傳統帳冊
201:利變年金帳冊
  *
  * @param acSubBookCode 區隔帳冊
	*/
  public void setAcSubBookCode(String acSubBookCode) {
    this.acSubBookCode = acSubBookCode;
  }

/**
	* 幣別<br>
	* 
	* @return String
	*/
  public String getCurrencyCode() {
    return this.currencyCode == null ? "" : this.currencyCode;
  }

/**
	* 幣別<br>
	* 
  *
  * @param currencyCode 幣別
	*/
  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
  }

/**
	* 科目代號<br>
	* 
	* @return String
	*/
  public String getAcNoCode() {
    return this.acNoCode == null ? "" : this.acNoCode;
  }

/**
	* 科目代號<br>
	* 
  *
  * @param acNoCode 科目代號
	*/
  public void setAcNoCode(String acNoCode) {
    this.acNoCode = acNoCode;
  }

/**
	* 科目名稱<br>
	* 
	* @return String
	*/
  public String getAcNoName() {
    return this.acNoName == null ? "" : this.acNoName;
  }

/**
	* 科目名稱<br>
	* 
  *
  * @param acNoName 科目名稱
	*/
  public void setAcNoName(String acNoName) {
    this.acNoName = acNoName;
  }

/**
	* 子目代號<br>
	* 
	* @return String
	*/
  public String getAcSubCode() {
    return this.acSubCode == null ? "" : this.acSubCode;
  }

/**
	* 子目代號<br>
	* 
  *
  * @param acSubCode 子目代號
	*/
  public void setAcSubCode(String acSubCode) {
    this.acSubCode = acSubCode;
  }

/**
	* 會計日期<br>
	* 西元年月日
	* @return Integer
	*/
  public int getAcDate() {
    return StaticTool.bcToRoc(this.acDate);
  }

/**
	* 會計日期<br>
	* 西元年月日
  *
  * @param acDate 會計日期
  * @throws LogicException when Date Is Warn	*/
  public void setAcDate(int acDate) throws LogicException {
    this.acDate = StaticTool.rocToBc(acDate);
  }

/**
	* 前日餘額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getYdBal() {
    return this.ydBal;
  }

/**
	* 前日餘額<br>
	* 
  *
  * @param ydBal 前日餘額
	*/
  public void setYdBal(BigDecimal ydBal) {
    this.ydBal = ydBal;
  }

/**
	* 本日餘額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getTdBal() {
    return this.tdBal;
  }

/**
	* 本日餘額<br>
	* 
  *
  * @param tdBal 本日餘額
	*/
  public void setTdBal(BigDecimal tdBal) {
    this.tdBal = tdBal;
  }

/**
	* 借方金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getDbAmt() {
    return this.dbAmt;
  }

/**
	* 借方金額<br>
	* 
  *
  * @param dbAmt 借方金額
	*/
  public void setDbAmt(BigDecimal dbAmt) {
    this.dbAmt = dbAmt;
  }

/**
	* 貸方金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getCrAmt() {
    return this.crAmt;
  }

/**
	* 貸方金額<br>
	* 
  *
  * @param crAmt 貸方金額
	*/
  public void setCrAmt(BigDecimal crAmt) {
    this.crAmt = crAmt;
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
    return "CoreAcMain [coreAcMainId=" + coreAcMainId + ", acNoName=" + acNoName
           + ", ydBal=" + ydBal + ", tdBal=" + tdBal + ", dbAmt=" + dbAmt + ", crAmt=" + crAmt + ", createDate=" + createDate
           + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
