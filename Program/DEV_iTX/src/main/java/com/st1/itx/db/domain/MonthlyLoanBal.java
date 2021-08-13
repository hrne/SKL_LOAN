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

/**
 * MonthlyLoanBal 每月放款餘額檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`MonthlyLoanBal`")
public class MonthlyLoanBal implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 4272880351580860094L;

@EmbeddedId
  private MonthlyLoanBalId monthlyLoanBalId;

  // 資料年月
  @Column(name = "`YearMonth`", insertable = false, updatable = false)
  private int yearMonth = 0;

  // 戶號
  @Column(name = "`CustNo`", insertable = false, updatable = false)
  private int custNo = 0;

  // 額度編號
  @Column(name = "`FacmNo`", insertable = false, updatable = false)
  private int facmNo = 0;

  // 撥款序號
  @Column(name = "`BormNo`", insertable = false, updatable = false)
  private int bormNo = 0;

  // 業務科目代號
  /* CdAcCode會計科子細目設定檔310: 短期擔保放款 320: 中期擔保放款330: 長期擔保放款340: 三十年房貸990: 催收款項 */
  @Column(name = "`AcctCode`", length = 3)
  private String acctCode;

  // 額度業務科目
  /* CdAcCode會計科子細目設定檔310: 短期擔保放款 320: 中期擔保放款330: 長期擔保放款340: 三十年房貸 */
  @Column(name = "`FacAcctCode`", length = 3)
  private String facAcctCode;

  // 幣別
  @Column(name = "`CurrencyCode`", length = 3)
  private String currencyCode;

  // 放款餘額
  @Column(name = "`LoanBalance`")
  private BigDecimal loanBalance = new BigDecimal("0");

  // 當月最高放款餘額
  /* 資料轉換來源LN$LBLP */
  @Column(name = "`MaxLoanBal`")
  private BigDecimal maxLoanBal = new BigDecimal("0");

  // 計息利率
  @Column(name = "`StoreRate`")
  private BigDecimal storeRate = new BigDecimal("0");

  // 實收利息
  /* 不含逾期息、違約金 */
  @Column(name = "`IntAmtRcv`")
  private BigDecimal intAmtRcv = new BigDecimal("0");

  // 提存利息
  /* 月底日 */
  @Column(name = "`IntAmtAcc`")
  private BigDecimal intAmtAcc = new BigDecimal("0");

  // 已到期未繳息
  /* 資料轉換來源LA$MSTP */
  @Column(name = "`UnpaidInt`")
  private BigDecimal unpaidInt = new BigDecimal("0");

  // 未到期應收息
  /* 資料轉換來源LA$MSTP */
  @Column(name = "`UnexpiredInt`")
  private BigDecimal unexpiredInt = new BigDecimal("0");

  // 累計回收利息
  /* 資料轉換來源LA$MSTP */
  @Column(name = "`SumRcvInt`")
  private BigDecimal sumRcvInt = new BigDecimal("0");

  // 本月利息
  /* 本月實收利息+本月提存利息-上月提存利息 */
  @Column(name = "`IntAmt`")
  private BigDecimal intAmt = new BigDecimal("0");

  // 商品代碼
  @Column(name = "`ProdNo`", length = 5)
  private String prodNo;

  // 帳冊別
  /* 000：全公司 */
  @Column(name = "`AcBookCode`", length = 3)
  private String acBookCode;

  // 企金別
  /* 共用代碼檔0:個金1:企金2:企金自然人 */
  @Column(name = "`EntCode`", length = 1)
  private String entCode;

  // (準)利害關係人職稱
  /* 共用代碼檔01: 董事長02: 副董事長03: 董事04: 監察人05: 總經理06: 副總經理07: 協理08: 經理09: 副理10: 辦理授信職員11: 十五日薪98: 其他關係人99: 非關係人 */
  @Column(name = "`RelsCode`", length = 2)
  private String relsCode;

  // 案件隸屬單位
  /* 共用代碼檔0:非企金單位  1:企金推展課 */
  @Column(name = "`DepartmentCode`", length = 1)
  private String departmentCode;

  // 主要擔保品代號1
  /* 擔保品代號檔CdCl */
  @Column(name = "`ClCode1`")
  private int clCode1 = 0;

  // 主要擔保品代號2
  /* 擔保品代號檔CdC2 */
  @Column(name = "`ClCode2`")
  private int clCode2 = 0;

  // 主要擔保品編號
  @Column(name = "`ClNo`")
  private int clNo = 0;

  // 主要擔保品地區別
  /* 地區別與鄉鎮區對照檔CdArea */
  @Column(name = "`CityCode`", length = 2)
  private String cityCode;

  // 轉催收本金
  @Column(name = "`OvduPrinAmt`")
  private BigDecimal ovduPrinAmt = new BigDecimal("0");

  // 轉催收利息
  @Column(name = "`OvduIntAmt`")
  private BigDecimal ovduIntAmt = new BigDecimal("0");

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

  // 區隔帳冊
  /* 00A:傳統帳冊201:利變年金帳冊 */
  @Column(name = "`AcSubBookCode`", length = 3)
  private String acSubBookCode;


  public MonthlyLoanBalId getMonthlyLoanBalId() {
    return this.monthlyLoanBalId;
  }

  public void setMonthlyLoanBalId(MonthlyLoanBalId monthlyLoanBalId) {
    this.monthlyLoanBalId = monthlyLoanBalId;
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
	* 戶號<br>
	* 
	* @return Integer
	*/
  public int getCustNo() {
    return this.custNo;
  }

/**
	* 戶號<br>
	* 
  *
  * @param custNo 戶號
	*/
  public void setCustNo(int custNo) {
    this.custNo = custNo;
  }

/**
	* 額度編號<br>
	* 
	* @return Integer
	*/
  public int getFacmNo() {
    return this.facmNo;
  }

/**
	* 額度編號<br>
	* 
  *
  * @param facmNo 額度編號
	*/
  public void setFacmNo(int facmNo) {
    this.facmNo = facmNo;
  }

/**
	* 撥款序號<br>
	* 
	* @return Integer
	*/
  public int getBormNo() {
    return this.bormNo;
  }

/**
	* 撥款序號<br>
	* 
  *
  * @param bormNo 撥款序號
	*/
  public void setBormNo(int bormNo) {
    this.bormNo = bormNo;
  }

/**
	* 業務科目代號<br>
	* CdAcCode會計科子細目設定檔
310: 短期擔保放款 
320: 中期擔保放款
330: 長期擔保放款
340: 三十年房貸
990: 催收款項
	* @return String
	*/
  public String getAcctCode() {
    return this.acctCode == null ? "" : this.acctCode;
  }

/**
	* 業務科目代號<br>
	* CdAcCode會計科子細目設定檔
310: 短期擔保放款 
320: 中期擔保放款
330: 長期擔保放款
340: 三十年房貸
990: 催收款項
  *
  * @param acctCode 業務科目代號
	*/
  public void setAcctCode(String acctCode) {
    this.acctCode = acctCode;
  }

/**
	* 額度業務科目<br>
	* CdAcCode會計科子細目設定檔
310: 短期擔保放款 
320: 中期擔保放款
330: 長期擔保放款
340: 三十年房貸
	* @return String
	*/
  public String getFacAcctCode() {
    return this.facAcctCode == null ? "" : this.facAcctCode;
  }

/**
	* 額度業務科目<br>
	* CdAcCode會計科子細目設定檔
310: 短期擔保放款 
320: 中期擔保放款
330: 長期擔保放款
340: 三十年房貸
  *
  * @param facAcctCode 額度業務科目
	*/
  public void setFacAcctCode(String facAcctCode) {
    this.facAcctCode = facAcctCode;
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
	* 放款餘額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLoanBalance() {
    return this.loanBalance;
  }

/**
	* 放款餘額<br>
	* 
  *
  * @param loanBalance 放款餘額
	*/
  public void setLoanBalance(BigDecimal loanBalance) {
    this.loanBalance = loanBalance;
  }

/**
	* 當月最高放款餘額<br>
	* 資料轉換來源LN$LBLP
	* @return BigDecimal
	*/
  public BigDecimal getMaxLoanBal() {
    return this.maxLoanBal;
  }

/**
	* 當月最高放款餘額<br>
	* 資料轉換來源LN$LBLP
  *
  * @param maxLoanBal 當月最高放款餘額
	*/
  public void setMaxLoanBal(BigDecimal maxLoanBal) {
    this.maxLoanBal = maxLoanBal;
  }

/**
	* 計息利率<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getStoreRate() {
    return this.storeRate;
  }

/**
	* 計息利率<br>
	* 
  *
  * @param storeRate 計息利率
	*/
  public void setStoreRate(BigDecimal storeRate) {
    this.storeRate = storeRate;
  }

/**
	* 實收利息<br>
	* 不含逾期息、違約金
	* @return BigDecimal
	*/
  public BigDecimal getIntAmtRcv() {
    return this.intAmtRcv;
  }

/**
	* 實收利息<br>
	* 不含逾期息、違約金
  *
  * @param intAmtRcv 實收利息
	*/
  public void setIntAmtRcv(BigDecimal intAmtRcv) {
    this.intAmtRcv = intAmtRcv;
  }

/**
	* 提存利息<br>
	* 月底日
	* @return BigDecimal
	*/
  public BigDecimal getIntAmtAcc() {
    return this.intAmtAcc;
  }

/**
	* 提存利息<br>
	* 月底日
  *
  * @param intAmtAcc 提存利息
	*/
  public void setIntAmtAcc(BigDecimal intAmtAcc) {
    this.intAmtAcc = intAmtAcc;
  }

/**
	* 已到期未繳息<br>
	* 資料轉換來源LA$MSTP
	* @return BigDecimal
	*/
  public BigDecimal getUnpaidInt() {
    return this.unpaidInt;
  }

/**
	* 已到期未繳息<br>
	* 資料轉換來源LA$MSTP
  *
  * @param unpaidInt 已到期未繳息
	*/
  public void setUnpaidInt(BigDecimal unpaidInt) {
    this.unpaidInt = unpaidInt;
  }

/**
	* 未到期應收息<br>
	* 資料轉換來源LA$MSTP
	* @return BigDecimal
	*/
  public BigDecimal getUnexpiredInt() {
    return this.unexpiredInt;
  }

/**
	* 未到期應收息<br>
	* 資料轉換來源LA$MSTP
  *
  * @param unexpiredInt 未到期應收息
	*/
  public void setUnexpiredInt(BigDecimal unexpiredInt) {
    this.unexpiredInt = unexpiredInt;
  }

/**
	* 累計回收利息<br>
	* 資料轉換來源LA$MSTP
	* @return BigDecimal
	*/
  public BigDecimal getSumRcvInt() {
    return this.sumRcvInt;
  }

/**
	* 累計回收利息<br>
	* 資料轉換來源LA$MSTP
  *
  * @param sumRcvInt 累計回收利息
	*/
  public void setSumRcvInt(BigDecimal sumRcvInt) {
    this.sumRcvInt = sumRcvInt;
  }

/**
	* 本月利息<br>
	* 本月實收利息+本月提存利息-上月提存利息
	* @return BigDecimal
	*/
  public BigDecimal getIntAmt() {
    return this.intAmt;
  }

/**
	* 本月利息<br>
	* 本月實收利息+本月提存利息-上月提存利息
  *
  * @param intAmt 本月利息
	*/
  public void setIntAmt(BigDecimal intAmt) {
    this.intAmt = intAmt;
  }

/**
	* 商品代碼<br>
	* 
	* @return String
	*/
  public String getProdNo() {
    return this.prodNo == null ? "" : this.prodNo;
  }

/**
	* 商品代碼<br>
	* 
  *
  * @param prodNo 商品代碼
	*/
  public void setProdNo(String prodNo) {
    this.prodNo = prodNo;
  }

/**
	* 帳冊別<br>
	* 000：全公司
	* @return String
	*/
  public String getAcBookCode() {
    return this.acBookCode == null ? "" : this.acBookCode;
  }

/**
	* 帳冊別<br>
	* 000：全公司
  *
  * @param acBookCode 帳冊別
	*/
  public void setAcBookCode(String acBookCode) {
    this.acBookCode = acBookCode;
  }

/**
	* 企金別<br>
	* 共用代碼檔
0:個金
1:企金
2:企金自然人
	* @return String
	*/
  public String getEntCode() {
    return this.entCode == null ? "" : this.entCode;
  }

/**
	* 企金別<br>
	* 共用代碼檔
0:個金
1:企金
2:企金自然人
  *
  * @param entCode 企金別
	*/
  public void setEntCode(String entCode) {
    this.entCode = entCode;
  }

/**
	* (準)利害關係人職稱<br>
	* 共用代碼檔
01: 董事長
02: 副董事長
03: 董事
04: 監察人
05: 總經理
06: 副總經理
07: 協理
08: 經理
09: 副理
10: 辦理授信職員
11: 十五日薪
98: 其他關係人
99: 非關係人
	* @return String
	*/
  public String getRelsCode() {
    return this.relsCode == null ? "" : this.relsCode;
  }

/**
	* (準)利害關係人職稱<br>
	* 共用代碼檔
01: 董事長
02: 副董事長
03: 董事
04: 監察人
05: 總經理
06: 副總經理
07: 協理
08: 經理
09: 副理
10: 辦理授信職員
11: 十五日薪
98: 其他關係人
99: 非關係人
  *
  * @param relsCode (準)利害關係人職稱
	*/
  public void setRelsCode(String relsCode) {
    this.relsCode = relsCode;
  }

/**
	* 案件隸屬單位<br>
	* 共用代碼檔
0:非企金單位  
1:企金推展課
	* @return String
	*/
  public String getDepartmentCode() {
    return this.departmentCode == null ? "" : this.departmentCode;
  }

/**
	* 案件隸屬單位<br>
	* 共用代碼檔
0:非企金單位  
1:企金推展課
  *
  * @param departmentCode 案件隸屬單位
	*/
  public void setDepartmentCode(String departmentCode) {
    this.departmentCode = departmentCode;
  }

/**
	* 主要擔保品代號1<br>
	* 擔保品代號檔CdCl
	* @return Integer
	*/
  public int getClCode1() {
    return this.clCode1;
  }

/**
	* 主要擔保品代號1<br>
	* 擔保品代號檔CdCl
  *
  * @param clCode1 主要擔保品代號1
	*/
  public void setClCode1(int clCode1) {
    this.clCode1 = clCode1;
  }

/**
	* 主要擔保品代號2<br>
	* 擔保品代號檔CdC2
	* @return Integer
	*/
  public int getClCode2() {
    return this.clCode2;
  }

/**
	* 主要擔保品代號2<br>
	* 擔保品代號檔CdC2
  *
  * @param clCode2 主要擔保品代號2
	*/
  public void setClCode2(int clCode2) {
    this.clCode2 = clCode2;
  }

/**
	* 主要擔保品編號<br>
	* 
	* @return Integer
	*/
  public int getClNo() {
    return this.clNo;
  }

/**
	* 主要擔保品編號<br>
	* 
  *
  * @param clNo 主要擔保品編號
	*/
  public void setClNo(int clNo) {
    this.clNo = clNo;
  }

/**
	* 主要擔保品地區別<br>
	* 地區別與鄉鎮區對照檔CdArea
	* @return String
	*/
  public String getCityCode() {
    return this.cityCode == null ? "" : this.cityCode;
  }

/**
	* 主要擔保品地區別<br>
	* 地區別與鄉鎮區對照檔CdArea
  *
  * @param cityCode 主要擔保品地區別
	*/
  public void setCityCode(String cityCode) {
    this.cityCode = cityCode;
  }

/**
	* 轉催收本金<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getOvduPrinAmt() {
    return this.ovduPrinAmt;
  }

/**
	* 轉催收本金<br>
	* 
  *
  * @param ovduPrinAmt 轉催收本金
	*/
  public void setOvduPrinAmt(BigDecimal ovduPrinAmt) {
    this.ovduPrinAmt = ovduPrinAmt;
  }

/**
	* 轉催收利息<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getOvduIntAmt() {
    return this.ovduIntAmt;
  }

/**
	* 轉催收利息<br>
	* 
  *
  * @param ovduIntAmt 轉催收利息
	*/
  public void setOvduIntAmt(BigDecimal ovduIntAmt) {
    this.ovduIntAmt = ovduIntAmt;
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


  @Override
  public String toString() {
    return "MonthlyLoanBal [monthlyLoanBalId=" + monthlyLoanBalId + ", acctCode=" + acctCode + ", facAcctCode=" + facAcctCode
           + ", currencyCode=" + currencyCode + ", loanBalance=" + loanBalance + ", maxLoanBal=" + maxLoanBal + ", storeRate=" + storeRate + ", intAmtRcv=" + intAmtRcv + ", intAmtAcc=" + intAmtAcc
           + ", unpaidInt=" + unpaidInt + ", unexpiredInt=" + unexpiredInt + ", sumRcvInt=" + sumRcvInt + ", intAmt=" + intAmt + ", prodNo=" + prodNo + ", acBookCode=" + acBookCode
           + ", entCode=" + entCode + ", relsCode=" + relsCode + ", departmentCode=" + departmentCode + ", clCode1=" + clCode1 + ", clCode2=" + clCode2 + ", clNo=" + clNo
           + ", cityCode=" + cityCode + ", ovduPrinAmt=" + ovduPrinAmt + ", ovduIntAmt=" + ovduIntAmt + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
           + ", lastUpdateEmpNo=" + lastUpdateEmpNo + ", acSubBookCode=" + acSubBookCode + "]";
  }
}
