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
 * AcMain 會計總帳檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`AcMain`")
public class AcMain implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 7313998285054276226L;

@EmbeddedId
  private AcMainId acMainId;

  // 帳冊別
  /* 000:全公司 */
  @Column(name = "`AcBookCode`", length = 3, insertable = false, updatable = false)
  private String acBookCode;

  // 區隔帳冊
  /* 00A:傳統帳冊201:利變年金帳冊 */
  @Column(name = "`AcSubBookCode`", length = 3, insertable = false, updatable = false)
  private String acSubBookCode;

  // 單位別
  @Column(name = "`BranchNo`", length = 4, insertable = false, updatable = false)
  private String branchNo;

  // 幣別
  @Column(name = "`CurrencyCode`", length = 3, insertable = false, updatable = false)
  private String currencyCode;

  // 科目代號
  /* CdAcCode會計科子細目設定檔 */
  @Column(name = "`AcNoCode`", length = 11, insertable = false, updatable = false)
  private String acNoCode;

  // 子目代號
  /* CdAcCode會計科子細目設定檔 */
  @Column(name = "`AcSubCode`", length = 5, insertable = false, updatable = false)
  private String acSubCode;

  // 細目代號
  /* CdAcCode會計科子細目設定檔有細目者，另計記細目為&amp;lt;空白&amp;gt;為加總至科子目 */
  @Column(name = "`AcDtlCode`", length = 2, insertable = false, updatable = false)
  private String acDtlCode;

  // 會計日期
  @Column(name = "`AcDate`", insertable = false, updatable = false)
  private int acDate = 0;

  // 前日餘額
  @Column(name = "`YdBal`")
  private BigDecimal ydBal = new BigDecimal("0");

  // 本日餘額
  @Column(name = "`TdBal`")
  private BigDecimal tdBal = new BigDecimal("0");

  // 借方筆數
  @Column(name = "`DbCnt`")
  private int dbCnt = 0;

  // 借方金額
  @Column(name = "`DbAmt`")
  private BigDecimal dbAmt = new BigDecimal("0");

  // 貸方筆數
  @Column(name = "`CrCnt`")
  private int crCnt = 0;

  // 貸方金額
  @Column(name = "`CrAmt`")
  private BigDecimal crAmt = new BigDecimal("0");

  // 核心借方筆數
  /* 銷帳記號=2－準銷帳科目(入銷帳在核心系統與放款系統分開處理)，ex：暫付及待結轉帳項－火險保費，需將核心出帳的借方金額寫入，餘額才可正確計算。 */
  @Column(name = "`CoreDbCnt`")
  private int coreDbCnt = 0;

  // 核心借方金額
  @Column(name = "`CoreDbAmt`")
  private BigDecimal coreDbAmt = new BigDecimal("0");

  // 核心貸方筆數
  @Column(name = "`CoreCrCnt`")
  private int coreCrCnt = 0;

  // 核心貸方金額
  @Column(name = "`CoreCrAmt`")
  private BigDecimal coreCrAmt = new BigDecimal("0");

  // 業務科目代號
  /* CdAcCode會計科子細目設定檔 */
  @Column(name = "`AcctCode`", length = 3)
  private String acctCode;

  // 月底年月
  /* 平常日-&amp;gt; 0, 月底日資料 -&amp;gt; ex.202005 */
  @Column(name = "`MonthEndYm`")
  private int monthEndYm = 0;

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


  public AcMainId getAcMainId() {
    return this.acMainId;
  }

  public void setAcMainId(AcMainId acMainId) {
    this.acMainId = acMainId;
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
	* 單位別<br>
	* 
	* @return String
	*/
  public String getBranchNo() {
    return this.branchNo == null ? "" : this.branchNo;
  }

/**
	* 單位別<br>
	* 
  *
  * @param branchNo 單位別
	*/
  public void setBranchNo(String branchNo) {
    this.branchNo = branchNo;
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
	* CdAcCode會計科子細目設定檔
	* @return String
	*/
  public String getAcNoCode() {
    return this.acNoCode == null ? "" : this.acNoCode;
  }

/**
	* 科目代號<br>
	* CdAcCode會計科子細目設定檔
  *
  * @param acNoCode 科目代號
	*/
  public void setAcNoCode(String acNoCode) {
    this.acNoCode = acNoCode;
  }

/**
	* 子目代號<br>
	* CdAcCode會計科子細目設定檔
	* @return String
	*/
  public String getAcSubCode() {
    return this.acSubCode == null ? "" : this.acSubCode;
  }

/**
	* 子目代號<br>
	* CdAcCode會計科子細目設定檔
  *
  * @param acSubCode 子目代號
	*/
  public void setAcSubCode(String acSubCode) {
    this.acSubCode = acSubCode;
  }

/**
	* 細目代號<br>
	* CdAcCode會計科子細目設定檔
有細目者，另計記細目為&amp;lt;空白&amp;gt;為加總至科子目
	* @return String
	*/
  public String getAcDtlCode() {
    return this.acDtlCode == null ? "" : this.acDtlCode;
  }

/**
	* 細目代號<br>
	* CdAcCode會計科子細目設定檔
有細目者，另計記細目為&amp;lt;空白&amp;gt;為加總至科子目
  *
  * @param acDtlCode 細目代號
	*/
  public void setAcDtlCode(String acDtlCode) {
    this.acDtlCode = acDtlCode;
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
	* 借方筆數<br>
	* 
	* @return Integer
	*/
  public int getDbCnt() {
    return this.dbCnt;
  }

/**
	* 借方筆數<br>
	* 
  *
  * @param dbCnt 借方筆數
	*/
  public void setDbCnt(int dbCnt) {
    this.dbCnt = dbCnt;
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
	* 貸方筆數<br>
	* 
	* @return Integer
	*/
  public int getCrCnt() {
    return this.crCnt;
  }

/**
	* 貸方筆數<br>
	* 
  *
  * @param crCnt 貸方筆數
	*/
  public void setCrCnt(int crCnt) {
    this.crCnt = crCnt;
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
	* 核心借方筆數<br>
	* 銷帳記號=2－準銷帳科目(入銷帳在核心系統與放款系統分開處理)，ex：暫付及待結轉帳項－火險保費，需將核心出帳的借方金額寫入，餘額才可正確計算。
	* @return Integer
	*/
  public int getCoreDbCnt() {
    return this.coreDbCnt;
  }

/**
	* 核心借方筆數<br>
	* 銷帳記號=2－準銷帳科目(入銷帳在核心系統與放款系統分開處理)，ex：暫付及待結轉帳項－火險保費，需將核心出帳的借方金額寫入，餘額才可正確計算。
  *
  * @param coreDbCnt 核心借方筆數
	*/
  public void setCoreDbCnt(int coreDbCnt) {
    this.coreDbCnt = coreDbCnt;
  }

/**
	* 核心借方金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getCoreDbAmt() {
    return this.coreDbAmt;
  }

/**
	* 核心借方金額<br>
	* 
  *
  * @param coreDbAmt 核心借方金額
	*/
  public void setCoreDbAmt(BigDecimal coreDbAmt) {
    this.coreDbAmt = coreDbAmt;
  }

/**
	* 核心貸方筆數<br>
	* 
	* @return Integer
	*/
  public int getCoreCrCnt() {
    return this.coreCrCnt;
  }

/**
	* 核心貸方筆數<br>
	* 
  *
  * @param coreCrCnt 核心貸方筆數
	*/
  public void setCoreCrCnt(int coreCrCnt) {
    this.coreCrCnt = coreCrCnt;
  }

/**
	* 核心貸方金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getCoreCrAmt() {
    return this.coreCrAmt;
  }

/**
	* 核心貸方金額<br>
	* 
  *
  * @param coreCrAmt 核心貸方金額
	*/
  public void setCoreCrAmt(BigDecimal coreCrAmt) {
    this.coreCrAmt = coreCrAmt;
  }

/**
	* 業務科目代號<br>
	* CdAcCode會計科子細目設定檔
	* @return String
	*/
  public String getAcctCode() {
    return this.acctCode == null ? "" : this.acctCode;
  }

/**
	* 業務科目代號<br>
	* CdAcCode會計科子細目設定檔
  *
  * @param acctCode 業務科目代號
	*/
  public void setAcctCode(String acctCode) {
    this.acctCode = acctCode;
  }

/**
	* 月底年月<br>
	* 平常日-&amp;gt; 0, 月底日資料 -&amp;gt; ex.202005
	* @return Integer
	*/
  public int getMonthEndYm() {
    return this.monthEndYm;
  }

/**
	* 月底年月<br>
	* 平常日-&amp;gt; 0, 月底日資料 -&amp;gt; ex.202005
  *
  * @param monthEndYm 月底年月
	*/
  public void setMonthEndYm(int monthEndYm) {
    this.monthEndYm = monthEndYm;
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
    return "AcMain [acMainId=" + acMainId
           + ", ydBal=" + ydBal + ", tdBal=" + tdBal + ", dbCnt=" + dbCnt + ", dbAmt=" + dbAmt
           + ", crCnt=" + crCnt + ", crAmt=" + crAmt + ", coreDbCnt=" + coreDbCnt + ", coreDbAmt=" + coreDbAmt + ", coreCrCnt=" + coreCrCnt + ", coreCrAmt=" + coreCrAmt
           + ", acctCode=" + acctCode + ", monthEndYm=" + monthEndYm + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo
           + "]";
  }
}
