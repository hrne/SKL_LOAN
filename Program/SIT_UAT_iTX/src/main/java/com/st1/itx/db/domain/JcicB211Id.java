package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * JcicB211 聯徵每日授信餘額變動資料檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicB211Id implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -2921092904367664689L;

// 資料日期
  @Column(name = "`DataYMD`")
  private int dataYMD = 0;

  // 總行代號
  /* Key,金融機構總機構之代號，三位數字 */
  @Column(name = "`BankItem`", length = 3)
  private String bankItem = " ";

  // 分行代號
  /* Key,金融機構分支機構之代號，四位數字 */
  @Column(name = "`BranchItem`", length = 4)
  private String branchItem = " ";

  // 授信戶IDN/BAN
  /* Key,左靠，身份證或統一證號 */
  @Column(name = "`CustId`", length = 10)
  private String custId = " ";

  // 交易日期
  /* Key,以'YYYMMDD'(民國年)表示 */
  @Column(name = "`AcDate`")
  private int acDate = 0;

  // 本筆撥款／還款帳號
  /* Key, */
  @Column(name = "`AcctNo`", length = 50)
  private String acctNo = " ";

  // 交易內容檔序號
  /* Key,避免資料重複 */
  @Column(name = "`BorxNo`")
  private int borxNo = 0;

  public JcicB211Id() {
  }

  public JcicB211Id(int dataYMD, String bankItem, String branchItem, String custId, int acDate, String acctNo, int borxNo) {
    this.dataYMD = dataYMD;
    this.bankItem = bankItem;
    this.branchItem = branchItem;
    this.custId = custId;
    this.acDate = acDate;
    this.acctNo = acctNo;
    this.borxNo = borxNo;
  }

/**
	* 資料日期<br>
	* 
	* @return Integer
	*/
  public int getDataYMD() {
    return this.dataYMD;
  }

/**
	* 資料日期<br>
	* 
  *
  * @param dataYMD 資料日期
	*/
  public void setDataYMD(int dataYMD) {
    this.dataYMD = dataYMD;
  }

/**
	* 總行代號<br>
	* Key,金融機構總機構之代號，三位數字
	* @return String
	*/
  public String getBankItem() {
    return this.bankItem == null ? "" : this.bankItem;
  }

/**
	* 總行代號<br>
	* Key,金融機構總機構之代號，三位數字
  *
  * @param bankItem 總行代號
	*/
  public void setBankItem(String bankItem) {
    this.bankItem = bankItem;
  }

/**
	* 分行代號<br>
	* Key,金融機構分支機構之代號，四位數字
	* @return String
	*/
  public String getBranchItem() {
    return this.branchItem == null ? "" : this.branchItem;
  }

/**
	* 分行代號<br>
	* Key,金融機構分支機構之代號，四位數字
  *
  * @param branchItem 分行代號
	*/
  public void setBranchItem(String branchItem) {
    this.branchItem = branchItem;
  }

/**
	* 授信戶IDN/BAN<br>
	* Key,左靠，身份證或統一證號
	* @return String
	*/
  public String getCustId() {
    return this.custId == null ? "" : this.custId;
  }

/**
	* 授信戶IDN/BAN<br>
	* Key,左靠，身份證或統一證號
  *
  * @param custId 授信戶IDN/BAN
	*/
  public void setCustId(String custId) {
    this.custId = custId;
  }

/**
	* 交易日期<br>
	* Key,以'YYYMMDD'(民國年)表示
	* @return Integer
	*/
  public int getAcDate() {
    return this.acDate;
  }

/**
	* 交易日期<br>
	* Key,以'YYYMMDD'(民國年)表示
  *
  * @param acDate 交易日期
	*/
  public void setAcDate(int acDate) {
    this.acDate = acDate;
  }

/**
	* 本筆撥款／還款帳號<br>
	* Key,
	* @return String
	*/
  public String getAcctNo() {
    return this.acctNo == null ? "" : this.acctNo;
  }

/**
	* 本筆撥款／還款帳號<br>
	* Key,
  *
  * @param acctNo 本筆撥款／還款帳號
	*/
  public void setAcctNo(String acctNo) {
    this.acctNo = acctNo;
  }

/**
	* 交易內容檔序號<br>
	* Key,避免資料重複
	* @return Integer
	*/
  public int getBorxNo() {
    return this.borxNo;
  }

/**
	* 交易內容檔序號<br>
	* Key,避免資料重複
  *
  * @param borxNo 交易內容檔序號
	*/
  public void setBorxNo(int borxNo) {
    this.borxNo = borxNo;
  }


  @Override
  public int hashCode() {
    return Objects.hash(dataYMD, bankItem, branchItem, custId, acDate, acctNo, borxNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    JcicB211Id jcicB211Id = (JcicB211Id) obj;
    return dataYMD == jcicB211Id.dataYMD && bankItem.equals(jcicB211Id.bankItem) && branchItem.equals(jcicB211Id.branchItem) && custId.equals(jcicB211Id.custId) && acDate == jcicB211Id.acDate && acctNo.equals(jcicB211Id.acctNo) && borxNo == jcicB211Id.borxNo;
  }

  @Override
  public String toString() {
    return "JcicB211Id [dataYMD=" + dataYMD + ", bankItem=" + bankItem + ", branchItem=" + branchItem + ", custId=" + custId + ", acDate=" + acDate + ", acctNo=" + acctNo + ", borxNo=" + borxNo + "]";
  }
}
