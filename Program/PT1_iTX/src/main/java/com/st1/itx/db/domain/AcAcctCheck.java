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
 * AcAcctCheck 會計業務檢核檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`AcAcctCheck`")
public class AcAcctCheck implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -3139078024282713933L;

@EmbeddedId
  private AcAcctCheckId acAcctCheckId;

  // 會計日期
  @Column(name = "`AcDate`", insertable = false, updatable = false)
  private int acDate = 0;

  // 單位別
  @Column(name = "`BranchNo`", length = 4, insertable = false, updatable = false)
  private String branchNo;

  // 幣別
  @Column(name = "`CurrencyCode`", length = 3, insertable = false, updatable = false)
  private String currencyCode;

  // 區隔帳冊
  @Column(name = "`AcSubBookCode`", length = 3, insertable = false, updatable = false)
  private String acSubBookCode;

  // 業務科目代號
  @Column(name = "`AcctCode`", length = 3, insertable = false, updatable = false)
  private String acctCode;

  // 業務科目名稱
  @Column(name = "`AcctItem`", length = 20)
  private String acctItem;

  // 本日餘額
  @Column(name = "`TdBal`")
  private BigDecimal tdBal = new BigDecimal("0");

  // 本日件數
  @Column(name = "`TdCnt`")
  private int tdCnt = 0;

  // 本日開戶件數
  @Column(name = "`TdNewCnt`")
  private int tdNewCnt = 0;

  // 本日結清件數
  @Column(name = "`TdClsCnt`")
  private int tdClsCnt = 0;

  // 本日展期件數
  @Column(name = "`TdExtCnt`")
  private int tdExtCnt = 0;

  // 本日展期金額
  @Column(name = "`TdExtAmt`")
  private BigDecimal tdExtAmt = new BigDecimal("0");

  // 銷帳檔餘額
  @Column(name = "`ReceivableBal`")
  private BigDecimal receivableBal = new BigDecimal("0");

  // 業務檔餘額
  @Column(name = "`AcctMasterBal`")
  private BigDecimal acctMasterBal = new BigDecimal("0");

  // 前日餘額
  @Column(name = "`YdBal`")
  private BigDecimal ydBal = new BigDecimal("0");

  // 借方金額
  @Column(name = "`DbAmt`")
  private BigDecimal dbAmt = new BigDecimal("0");

  // 貸方金額
  @Column(name = "`CrAmt`")
  private BigDecimal crAmt = new BigDecimal("0");

  // 核心借方金額
  @Column(name = "`CoreDbAmt`")
  private BigDecimal coreDbAmt = new BigDecimal("0");

  // 核心貸方金額
  @Column(name = "`CoreCrAmt`")
  private BigDecimal coreCrAmt = new BigDecimal("0");

  // 業務檔已銷金額
  @Column(name = "`MasterClsAmt`")
  private BigDecimal masterClsAmt = new BigDecimal("0");

  // 建檔人員
  @Column(name = "`CreateEmpNo`", length = 6)
  private String createEmpNo;

  // 建檔日期
  @CreatedDate
  @Column(name = "`CreateDate`")
  private java.sql.Timestamp createDate;

  // 最後維護人員
  @Column(name = "`LastUpdateEmpNo`", length = 6)
  private String lastUpdateEmpNo;

  // 最後維護日期
  @LastModifiedDate
  @Column(name = "`LastUpdate`")
  private java.sql.Timestamp lastUpdate;


  public AcAcctCheckId getAcAcctCheckId() {
    return this.acAcctCheckId;
  }

  public void setAcAcctCheckId(AcAcctCheckId acAcctCheckId) {
    this.acAcctCheckId = acAcctCheckId;
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
	* 區隔帳冊<br>
	* 
	* @return String
	*/
  public String getAcSubBookCode() {
    return this.acSubBookCode == null ? "" : this.acSubBookCode;
  }

/**
	* 區隔帳冊<br>
	* 
  *
  * @param acSubBookCode 區隔帳冊
	*/
  public void setAcSubBookCode(String acSubBookCode) {
    this.acSubBookCode = acSubBookCode;
  }

/**
	* 業務科目代號<br>
	* 
	* @return String
	*/
  public String getAcctCode() {
    return this.acctCode == null ? "" : this.acctCode;
  }

/**
	* 業務科目代號<br>
	* 
  *
  * @param acctCode 業務科目代號
	*/
  public void setAcctCode(String acctCode) {
    this.acctCode = acctCode;
  }

/**
	* 業務科目名稱<br>
	* 
	* @return String
	*/
  public String getAcctItem() {
    return this.acctItem == null ? "" : this.acctItem;
  }

/**
	* 業務科目名稱<br>
	* 
  *
  * @param acctItem 業務科目名稱
	*/
  public void setAcctItem(String acctItem) {
    this.acctItem = acctItem;
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
	* 本日件數<br>
	* 
	* @return Integer
	*/
  public int getTdCnt() {
    return this.tdCnt;
  }

/**
	* 本日件數<br>
	* 
  *
  * @param tdCnt 本日件數
	*/
  public void setTdCnt(int tdCnt) {
    this.tdCnt = tdCnt;
  }

/**
	* 本日開戶件數<br>
	* 
	* @return Integer
	*/
  public int getTdNewCnt() {
    return this.tdNewCnt;
  }

/**
	* 本日開戶件數<br>
	* 
  *
  * @param tdNewCnt 本日開戶件數
	*/
  public void setTdNewCnt(int tdNewCnt) {
    this.tdNewCnt = tdNewCnt;
  }

/**
	* 本日結清件數<br>
	* 
	* @return Integer
	*/
  public int getTdClsCnt() {
    return this.tdClsCnt;
  }

/**
	* 本日結清件數<br>
	* 
  *
  * @param tdClsCnt 本日結清件數
	*/
  public void setTdClsCnt(int tdClsCnt) {
    this.tdClsCnt = tdClsCnt;
  }

/**
	* 本日展期件數<br>
	* 
	* @return Integer
	*/
  public int getTdExtCnt() {
    return this.tdExtCnt;
  }

/**
	* 本日展期件數<br>
	* 
  *
  * @param tdExtCnt 本日展期件數
	*/
  public void setTdExtCnt(int tdExtCnt) {
    this.tdExtCnt = tdExtCnt;
  }

/**
	* 本日展期金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getTdExtAmt() {
    return this.tdExtAmt;
  }

/**
	* 本日展期金額<br>
	* 
  *
  * @param tdExtAmt 本日展期金額
	*/
  public void setTdExtAmt(BigDecimal tdExtAmt) {
    this.tdExtAmt = tdExtAmt;
  }

/**
	* 銷帳檔餘額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getReceivableBal() {
    return this.receivableBal;
  }

/**
	* 銷帳檔餘額<br>
	* 
  *
  * @param receivableBal 銷帳檔餘額
	*/
  public void setReceivableBal(BigDecimal receivableBal) {
    this.receivableBal = receivableBal;
  }

/**
	* 業務檔餘額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getAcctMasterBal() {
    return this.acctMasterBal;
  }

/**
	* 業務檔餘額<br>
	* 
  *
  * @param acctMasterBal 業務檔餘額
	*/
  public void setAcctMasterBal(BigDecimal acctMasterBal) {
    this.acctMasterBal = acctMasterBal;
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
	* 業務檔已銷金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getMasterClsAmt() {
    return this.masterClsAmt;
  }

/**
	* 業務檔已銷金額<br>
	* 
  *
  * @param masterClsAmt 業務檔已銷金額
	*/
  public void setMasterClsAmt(BigDecimal masterClsAmt) {
    this.masterClsAmt = masterClsAmt;
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
	* 建檔日期<br>
	* 
	* @return java.sql.Timestamp
	*/
  public java.sql.Timestamp getCreateDate() {
    return this.createDate;
  }

/**
	* 建檔日期<br>
	* 
  *
  * @param createDate 建檔日期
	*/
  public void setCreateDate(java.sql.Timestamp createDate) {
    this.createDate = createDate;
  }

/**
	* 最後維護人員<br>
	* 
	* @return String
	*/
  public String getLastUpdateEmpNo() {
    return this.lastUpdateEmpNo == null ? "" : this.lastUpdateEmpNo;
  }

/**
	* 最後維護人員<br>
	* 
  *
  * @param lastUpdateEmpNo 最後維護人員
	*/
  public void setLastUpdateEmpNo(String lastUpdateEmpNo) {
    this.lastUpdateEmpNo = lastUpdateEmpNo;
  }

/**
	* 最後維護日期<br>
	* 
	* @return java.sql.Timestamp
	*/
  public java.sql.Timestamp getLastUpdate() {
    return this.lastUpdate;
  }

/**
	* 最後維護日期<br>
	* 
  *
  * @param lastUpdate 最後維護日期
	*/
  public void setLastUpdate(java.sql.Timestamp lastUpdate) {
    this.lastUpdate = lastUpdate;
  }


  @Override
  public String toString() {
    return "AcAcctCheck [acAcctCheckId=" + acAcctCheckId + ", acctItem=" + acctItem
           + ", tdBal=" + tdBal + ", tdCnt=" + tdCnt + ", tdNewCnt=" + tdNewCnt + ", tdClsCnt=" + tdClsCnt + ", tdExtCnt=" + tdExtCnt + ", tdExtAmt=" + tdExtAmt
           + ", receivableBal=" + receivableBal + ", acctMasterBal=" + acctMasterBal + ", ydBal=" + ydBal + ", dbAmt=" + dbAmt + ", crAmt=" + crAmt + ", coreDbAmt=" + coreDbAmt
           + ", coreCrAmt=" + coreCrAmt + ", masterClsAmt=" + masterClsAmt + ", createEmpNo=" + createEmpNo + ", createDate=" + createDate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + ", lastUpdate=" + lastUpdate
           + "]";
  }
}
