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
 * MonthlyFacBal 額度月報工作檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`MonthlyFacBal`")
public class MonthlyFacBal implements Serializable {


  @EmbeddedId
  private MonthlyFacBalId monthlyFacBalId;

  // 資料年月
  @Column(name = "`YearMonth`", insertable = false, updatable = false)
  private int yearMonth = 0;

  // 戶號
  @Column(name = "`CustNo`", insertable = false, updatable = false)
  private int custNo = 0;

  // 額度
  @Column(name = "`FacmNo`", insertable = false, updatable = false)
  private int facmNo = 0;

  // 繳息迄日
  @Column(name = "`PrevIntDate`")
  private int prevIntDate = 0;

  // 應繳息日
  @Column(name = "`NextIntDate`")
  private int nextIntDate = 0;

  // 最近應繳日
  /* LM038報表用最近應繳日 */
  @Column(name = "`DueDate`")
  private int dueDate = 0;

  // 逾期期數
  @Column(name = "`OvduTerm`")
  private int ovduTerm = 0;

  // 逾期天數
  @Column(name = "`OvduDays`")
  private int ovduDays = 0;

  // 幣別
  @Column(name = "`CurrencyCode`", length = 3)
  private String currencyCode;

  // 本金餘額
  @Column(name = "`PrinBalance`")
  private BigDecimal prinBalance = new BigDecimal("0");

  // 呆帳餘額
  @Column(name = "`BadDebtBal`")
  private BigDecimal badDebtBal = new BigDecimal("0");

  // 催收人員
  @Column(name = "`AccCollPsn`", length = 6)
  private String accCollPsn;

  // 法務人員
  @Column(name = "`LegalPsn`", length = 6)
  private String legalPsn;

  // 戶況
  /* 00:正常戶02:催收戶03:結案戶(結清日=本月)04:逾期戶(改為00:正常戶)05:催收結案戶(結清日=本月)06:呆帳戶07:部分轉呆戶08:債權轉讓戶(結清日=本月)09:呆帳結案戶(結清日=本月)(不含債協) */
  @Column(name = "`Status`")
  private int status = 0;

  // 業務科目代號
  /* CdAcCode會計科子細目設定檔310:短期擔保放款 320:中期擔保放款330:長期擔保放款340:三十年房貸990:催收款項 */
  @Column(name = "`AcctCode`", length = 3)
  private String acctCode;

  // 額度業務科目
  /* CdAcCode會計科子細目設定檔310:短期擔保放款 320:中期擔保放款330:長期擔保放款340:三十年房貸 */
  @Column(name = "`FacAcctCode`", length = 3)
  private String facAcctCode;

  // 同擔保品戶號
  @Column(name = "`ClCustNo`")
  private int clCustNo = 0;

  // 同擔保品額度
  @Column(name = "`ClFacmNo`")
  private int clFacmNo = 0;

  // 同擔保品序列號
  /* 同擔保品逾期天數最久者為1,其餘依序排列 */
  @Column(name = "`ClRowNo`")
  private int clRowNo = 0;

  // 展期記號
  /* 空白、1:展期一般2:展期協議 */
  @Column(name = "`RenewCode`", length = 1)
  private String renewCode;

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
  /* 共用代碼檔01:董事長02:副董事長03:董事04:監察人05:總經理06:副總經理07:協理08:經理09:副理10:辦理授信職員11:十五日薪98:其他關係人99:非關係人 */
  @Column(name = "`RelsCode`", length = 2)
  private String relsCode;

  // 案件隸屬單位
  /* 共用代碼檔0:非企金單位  1:企金推展課 */
  @Column(name = "`DepartmentCode`", length = 1)
  private String departmentCode;

  // 已到期本金/轉催收本金
  @Column(name = "`UnpaidPrincipal`")
  private BigDecimal unpaidPrincipal = new BigDecimal("0");

  // 已到期利息/轉催收利息
  @Column(name = "`UnpaidInterest`")
  private BigDecimal unpaidInterest = new BigDecimal("0");

  // 已到期違約金/轉催收違約金
  @Column(name = "`UnpaidBreachAmt`")
  private BigDecimal unpaidBreachAmt = new BigDecimal("0");

  // 已到期延滯息
  @Column(name = "`UnpaidDelayInt`")
  private BigDecimal unpaidDelayInt = new BigDecimal("0");

  // 未到期回收本金
  @Column(name = "`AcdrPrincipal`")
  private BigDecimal acdrPrincipal = new BigDecimal("0");

  // 未到期利息
  @Column(name = "`AcdrInterest`")
  private BigDecimal acdrInterest = new BigDecimal("0");

  // 未到期違約金
  @Column(name = "`AcdrBreachAmt`")
  private BigDecimal acdrBreachAmt = new BigDecimal("0");

  // 未到期延滯息
  @Column(name = "`AcdrDelayInt`")
  private BigDecimal acdrDelayInt = new BigDecimal("0");

  // 火險費用
  /* AcctCode：TMI(未收)+F09(墊付)+F25(催收) */
  @Column(name = "`FireFee`")
  private BigDecimal fireFee = new BigDecimal("0");

  // 法務費用
  /* AcctCode：F07(墊付)+F24(催收) */
  @Column(name = "`LawFee`")
  private BigDecimal lawFee = new BigDecimal("0");

  // 契變手續費
  /* AcctCode：F29(未收) */
  @Column(name = "`ModifyFee`")
  private BigDecimal modifyFee = new BigDecimal("0");

  // 帳管費用
  /* AcctCode：F10(未收) */
  @Column(name = "`AcctFee`")
  private BigDecimal acctFee = new BigDecimal("0");

  // 短繳本金
  /* 欠繳本金 */
  @Column(name = "`ShortfallPrin`")
  private BigDecimal shortfallPrin = new BigDecimal("0");

  // 短繳利息
  /* 欠繳利息 */
  @Column(name = "`ShortfallInt`")
  private BigDecimal shortfallInt = new BigDecimal("0");

  // 暫收金額
  @Column(name = "`TempAmt`")
  private BigDecimal tempAmt = new BigDecimal("0");

  // 主要擔保品代號1
  /* 擔保品代號檔CdCl1 房地2 土地3 股票4 其他有價證券5 銀行保證9 動產 */
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

  // 轉催收日期
  @Column(name = "`OvduDate`")
  private int ovduDate = 0;

  // 催收本金餘額
  @Column(name = "`OvduPrinBal`")
  private BigDecimal ovduPrinBal = new BigDecimal("0");

  // 催收利息餘額
  @Column(name = "`OvduIntBal`")
  private BigDecimal ovduIntBal = new BigDecimal("0");

  // 催收違約金餘額
  @Column(name = "`OvduBreachBal`")
  private BigDecimal ovduBreachBal = new BigDecimal("0");

  // 催收餘額
  @Column(name = "`OvduBal`")
  private BigDecimal ovduBal = new BigDecimal("0");

  // 無擔保金額
  /* 1.無擔保債權設定金額(法務進度:901)，資產分類一律為五2.L7205上傳更新 */
  @Column(name = "`LawAmount`")
  private BigDecimal lawAmount = new BigDecimal("0");

  // 資產五分類代號(有擔保部分)
  /* 1~5 (有擔保部分) */
  @Column(name = "`AssetClass`", length = 1)
  private String assetClass;

  // 計息利率
  @Column(name = "`StoreRate`")
  private BigDecimal storeRate = new BigDecimal("0");

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

  // 無擔保資產分類代號
  /* L7205上傳更新4:四5:五 */
  @Column(name = "`LawAssetClass`", length = 2)
  private String lawAssetClass;

  // 資產五分類代號2(有擔保部分)
  /* L7205上傳更新11:一之1(正常繳息)12:一之2(特定放款資產項目)21:二之122:二之223:二之33:三  4:四   5:五此為有擔保金額(PrinBalance-LawAmount)的分類 */
  @Column(name = "`AssetClass2`", length = 2)
  private String assetClass2;

  // 是否為利害關係人
  /* 寫入時為空值，由L7206[利關人名單檔上傳]上傳更新Y/N */
  @Column(name = "`BankRelationFlag`", length = 1)
  private String bankRelationFlag;

  // 政策性專案貸款
  /* 寫入時為空值，L7205上傳更新政策性專案中:Y:初貸日大於等於20110101C:初貸日小於20110101N:無 */
  @Column(name = "`GovProjectFlag`", length = 1)
  private String govProjectFlag;

  // 建築貸款記號
  /* 寫入時為空值，L7205上傳更新Y/N */
  @Column(name = "`BuildingFlag`", length = 1)
  private String buildingFlag;

  // 特定資產記號
  /* 寫入時為空值，L7205上傳更新Y/N */
  @Column(name = "`SpecialAssetFlag`", length = 1)
  private String specialAssetFlag;


  public MonthlyFacBalId getMonthlyFacBalId() {
    return this.monthlyFacBalId;
  }

  public void setMonthlyFacBalId(MonthlyFacBalId monthlyFacBalId) {
    this.monthlyFacBalId = monthlyFacBalId;
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
	* 額度<br>
	* 
	* @return Integer
	*/
  public int getFacmNo() {
    return this.facmNo;
  }

/**
	* 額度<br>
	* 
  *
  * @param facmNo 額度
	*/
  public void setFacmNo(int facmNo) {
    this.facmNo = facmNo;
  }

/**
	* 繳息迄日<br>
	* 
	* @return Integer
	*/
  public int getPrevIntDate() {
    return StaticTool.bcToRoc(this.prevIntDate);
  }

/**
	* 繳息迄日<br>
	* 
  *
  * @param prevIntDate 繳息迄日
  * @throws LogicException when Date Is Warn	*/
  public void setPrevIntDate(int prevIntDate) throws LogicException {
    this.prevIntDate = StaticTool.rocToBc(prevIntDate);
  }

/**
	* 應繳息日<br>
	* 
	* @return Integer
	*/
  public int getNextIntDate() {
    return StaticTool.bcToRoc(this.nextIntDate);
  }

/**
	* 應繳息日<br>
	* 
  *
  * @param nextIntDate 應繳息日
  * @throws LogicException when Date Is Warn	*/
  public void setNextIntDate(int nextIntDate) throws LogicException {
    this.nextIntDate = StaticTool.rocToBc(nextIntDate);
  }

/**
	* 最近應繳日<br>
	* LM038報表用最近應繳日
	* @return Integer
	*/
  public int getDueDate() {
    return StaticTool.bcToRoc(this.dueDate);
  }

/**
	* 最近應繳日<br>
	* LM038報表用最近應繳日
  *
  * @param dueDate 最近應繳日
  * @throws LogicException when Date Is Warn	*/
  public void setDueDate(int dueDate) throws LogicException {
    this.dueDate = StaticTool.rocToBc(dueDate);
  }

/**
	* 逾期期數<br>
	* 
	* @return Integer
	*/
  public int getOvduTerm() {
    return this.ovduTerm;
  }

/**
	* 逾期期數<br>
	* 
  *
  * @param ovduTerm 逾期期數
	*/
  public void setOvduTerm(int ovduTerm) {
    this.ovduTerm = ovduTerm;
  }

/**
	* 逾期天數<br>
	* 
	* @return Integer
	*/
  public int getOvduDays() {
    return this.ovduDays;
  }

/**
	* 逾期天數<br>
	* 
  *
  * @param ovduDays 逾期天數
	*/
  public void setOvduDays(int ovduDays) {
    this.ovduDays = ovduDays;
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
	* 本金餘額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getPrinBalance() {
    return this.prinBalance;
  }

/**
	* 本金餘額<br>
	* 
  *
  * @param prinBalance 本金餘額
	*/
  public void setPrinBalance(BigDecimal prinBalance) {
    this.prinBalance = prinBalance;
  }

/**
	* 呆帳餘額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getBadDebtBal() {
    return this.badDebtBal;
  }

/**
	* 呆帳餘額<br>
	* 
  *
  * @param badDebtBal 呆帳餘額
	*/
  public void setBadDebtBal(BigDecimal badDebtBal) {
    this.badDebtBal = badDebtBal;
  }

/**
	* 催收人員<br>
	* 
	* @return String
	*/
  public String getAccCollPsn() {
    return this.accCollPsn == null ? "" : this.accCollPsn;
  }

/**
	* 催收人員<br>
	* 
  *
  * @param accCollPsn 催收人員
	*/
  public void setAccCollPsn(String accCollPsn) {
    this.accCollPsn = accCollPsn;
  }

/**
	* 法務人員<br>
	* 
	* @return String
	*/
  public String getLegalPsn() {
    return this.legalPsn == null ? "" : this.legalPsn;
  }

/**
	* 法務人員<br>
	* 
  *
  * @param legalPsn 法務人員
	*/
  public void setLegalPsn(String legalPsn) {
    this.legalPsn = legalPsn;
  }

/**
	* 戶況<br>
	* 00:正常戶
02:催收戶
03:結案戶(結清日=本月)
04:逾期戶(改為00:正常戶)
05:催收結案戶(結清日=本月)
06:呆帳戶
07:部分轉呆戶
08:債權轉讓戶(結清日=本月)
09:呆帳結案戶(結清日=本月)
(不含債協)
	* @return Integer
	*/
  public int getStatus() {
    return this.status;
  }

/**
	* 戶況<br>
	* 00:正常戶
02:催收戶
03:結案戶(結清日=本月)
04:逾期戶(改為00:正常戶)
05:催收結案戶(結清日=本月)
06:呆帳戶
07:部分轉呆戶
08:債權轉讓戶(結清日=本月)
09:呆帳結案戶(結清日=本月)
(不含債協)
  *
  * @param status 戶況
	*/
  public void setStatus(int status) {
    this.status = status;
  }

/**
	* 業務科目代號<br>
	* CdAcCode會計科子細目設定檔
310:短期擔保放款 
320:中期擔保放款
330:長期擔保放款
340:三十年房貸
990:催收款項
	* @return String
	*/
  public String getAcctCode() {
    return this.acctCode == null ? "" : this.acctCode;
  }

/**
	* 業務科目代號<br>
	* CdAcCode會計科子細目設定檔
310:短期擔保放款 
320:中期擔保放款
330:長期擔保放款
340:三十年房貸
990:催收款項
  *
  * @param acctCode 業務科目代號
	*/
  public void setAcctCode(String acctCode) {
    this.acctCode = acctCode;
  }

/**
	* 額度業務科目<br>
	* CdAcCode會計科子細目設定檔
310:短期擔保放款 
320:中期擔保放款
330:長期擔保放款
340:三十年房貸
	* @return String
	*/
  public String getFacAcctCode() {
    return this.facAcctCode == null ? "" : this.facAcctCode;
  }

/**
	* 額度業務科目<br>
	* CdAcCode會計科子細目設定檔
310:短期擔保放款 
320:中期擔保放款
330:長期擔保放款
340:三十年房貸
  *
  * @param facAcctCode 額度業務科目
	*/
  public void setFacAcctCode(String facAcctCode) {
    this.facAcctCode = facAcctCode;
  }

/**
	* 同擔保品戶號<br>
	* 
	* @return Integer
	*/
  public int getClCustNo() {
    return this.clCustNo;
  }

/**
	* 同擔保品戶號<br>
	* 
  *
  * @param clCustNo 同擔保品戶號
	*/
  public void setClCustNo(int clCustNo) {
    this.clCustNo = clCustNo;
  }

/**
	* 同擔保品額度<br>
	* 
	* @return Integer
	*/
  public int getClFacmNo() {
    return this.clFacmNo;
  }

/**
	* 同擔保品額度<br>
	* 
  *
  * @param clFacmNo 同擔保品額度
	*/
  public void setClFacmNo(int clFacmNo) {
    this.clFacmNo = clFacmNo;
  }

/**
	* 同擔保品序列號<br>
	* 同擔保品逾期天數最久者為1,其餘依序排列
	* @return Integer
	*/
  public int getClRowNo() {
    return this.clRowNo;
  }

/**
	* 同擔保品序列號<br>
	* 同擔保品逾期天數最久者為1,其餘依序排列
  *
  * @param clRowNo 同擔保品序列號
	*/
  public void setClRowNo(int clRowNo) {
    this.clRowNo = clRowNo;
  }

/**
	* 展期記號<br>
	* 空白、1:展期一般
2:展期協議
	* @return String
	*/
  public String getRenewCode() {
    return this.renewCode == null ? "" : this.renewCode;
  }

/**
	* 展期記號<br>
	* 空白、1:展期一般
2:展期協議
  *
  * @param renewCode 展期記號
	*/
  public void setRenewCode(String renewCode) {
    this.renewCode = renewCode;
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
01:董事長
02:副董事長
03:董事
04:監察人
05:總經理
06:副總經理
07:協理
08:經理
09:副理
10:辦理授信職員
11:十五日薪
98:其他關係人
99:非關係人
	* @return String
	*/
  public String getRelsCode() {
    return this.relsCode == null ? "" : this.relsCode;
  }

/**
	* (準)利害關係人職稱<br>
	* 共用代碼檔
01:董事長
02:副董事長
03:董事
04:監察人
05:總經理
06:副總經理
07:協理
08:經理
09:副理
10:辦理授信職員
11:十五日薪
98:其他關係人
99:非關係人
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
	* 已到期本金/轉催收本金<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getUnpaidPrincipal() {
    return this.unpaidPrincipal;
  }

/**
	* 已到期本金/轉催收本金<br>
	* 
  *
  * @param unpaidPrincipal 已到期本金/轉催收本金
	*/
  public void setUnpaidPrincipal(BigDecimal unpaidPrincipal) {
    this.unpaidPrincipal = unpaidPrincipal;
  }

/**
	* 已到期利息/轉催收利息<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getUnpaidInterest() {
    return this.unpaidInterest;
  }

/**
	* 已到期利息/轉催收利息<br>
	* 
  *
  * @param unpaidInterest 已到期利息/轉催收利息
	*/
  public void setUnpaidInterest(BigDecimal unpaidInterest) {
    this.unpaidInterest = unpaidInterest;
  }

/**
	* 已到期違約金/轉催收違約金<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getUnpaidBreachAmt() {
    return this.unpaidBreachAmt;
  }

/**
	* 已到期違約金/轉催收違約金<br>
	* 
  *
  * @param unpaidBreachAmt 已到期違約金/轉催收違約金
	*/
  public void setUnpaidBreachAmt(BigDecimal unpaidBreachAmt) {
    this.unpaidBreachAmt = unpaidBreachAmt;
  }

/**
	* 已到期延滯息<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getUnpaidDelayInt() {
    return this.unpaidDelayInt;
  }

/**
	* 已到期延滯息<br>
	* 
  *
  * @param unpaidDelayInt 已到期延滯息
	*/
  public void setUnpaidDelayInt(BigDecimal unpaidDelayInt) {
    this.unpaidDelayInt = unpaidDelayInt;
  }

/**
	* 未到期回收本金<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getAcdrPrincipal() {
    return this.acdrPrincipal;
  }

/**
	* 未到期回收本金<br>
	* 
  *
  * @param acdrPrincipal 未到期回收本金
	*/
  public void setAcdrPrincipal(BigDecimal acdrPrincipal) {
    this.acdrPrincipal = acdrPrincipal;
  }

/**
	* 未到期利息<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getAcdrInterest() {
    return this.acdrInterest;
  }

/**
	* 未到期利息<br>
	* 
  *
  * @param acdrInterest 未到期利息
	*/
  public void setAcdrInterest(BigDecimal acdrInterest) {
    this.acdrInterest = acdrInterest;
  }

/**
	* 未到期違約金<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getAcdrBreachAmt() {
    return this.acdrBreachAmt;
  }

/**
	* 未到期違約金<br>
	* 
  *
  * @param acdrBreachAmt 未到期違約金
	*/
  public void setAcdrBreachAmt(BigDecimal acdrBreachAmt) {
    this.acdrBreachAmt = acdrBreachAmt;
  }

/**
	* 未到期延滯息<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getAcdrDelayInt() {
    return this.acdrDelayInt;
  }

/**
	* 未到期延滯息<br>
	* 
  *
  * @param acdrDelayInt 未到期延滯息
	*/
  public void setAcdrDelayInt(BigDecimal acdrDelayInt) {
    this.acdrDelayInt = acdrDelayInt;
  }

/**
	* 火險費用<br>
	* AcctCode：TMI(未收)+F09(墊付)+F25(催收)
	* @return BigDecimal
	*/
  public BigDecimal getFireFee() {
    return this.fireFee;
  }

/**
	* 火險費用<br>
	* AcctCode：TMI(未收)+F09(墊付)+F25(催收)
  *
  * @param fireFee 火險費用
	*/
  public void setFireFee(BigDecimal fireFee) {
    this.fireFee = fireFee;
  }

/**
	* 法務費用<br>
	* AcctCode：F07(墊付)+F24(催收)
	* @return BigDecimal
	*/
  public BigDecimal getLawFee() {
    return this.lawFee;
  }

/**
	* 法務費用<br>
	* AcctCode：F07(墊付)+F24(催收)
  *
  * @param lawFee 法務費用
	*/
  public void setLawFee(BigDecimal lawFee) {
    this.lawFee = lawFee;
  }

/**
	* 契變手續費<br>
	* AcctCode：F29(未收)
	* @return BigDecimal
	*/
  public BigDecimal getModifyFee() {
    return this.modifyFee;
  }

/**
	* 契變手續費<br>
	* AcctCode：F29(未收)
  *
  * @param modifyFee 契變手續費
	*/
  public void setModifyFee(BigDecimal modifyFee) {
    this.modifyFee = modifyFee;
  }

/**
	* 帳管費用<br>
	* AcctCode：F10(未收)
	* @return BigDecimal
	*/
  public BigDecimal getAcctFee() {
    return this.acctFee;
  }

/**
	* 帳管費用<br>
	* AcctCode：F10(未收)
  *
  * @param acctFee 帳管費用
	*/
  public void setAcctFee(BigDecimal acctFee) {
    this.acctFee = acctFee;
  }

/**
	* 短繳本金<br>
	* 欠繳本金
	* @return BigDecimal
	*/
  public BigDecimal getShortfallPrin() {
    return this.shortfallPrin;
  }

/**
	* 短繳本金<br>
	* 欠繳本金
  *
  * @param shortfallPrin 短繳本金
	*/
  public void setShortfallPrin(BigDecimal shortfallPrin) {
    this.shortfallPrin = shortfallPrin;
  }

/**
	* 短繳利息<br>
	* 欠繳利息
	* @return BigDecimal
	*/
  public BigDecimal getShortfallInt() {
    return this.shortfallInt;
  }

/**
	* 短繳利息<br>
	* 欠繳利息
  *
  * @param shortfallInt 短繳利息
	*/
  public void setShortfallInt(BigDecimal shortfallInt) {
    this.shortfallInt = shortfallInt;
  }

/**
	* 暫收金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getTempAmt() {
    return this.tempAmt;
  }

/**
	* 暫收金額<br>
	* 
  *
  * @param tempAmt 暫收金額
	*/
  public void setTempAmt(BigDecimal tempAmt) {
    this.tempAmt = tempAmt;
  }

/**
	* 主要擔保品代號1<br>
	* 擔保品代號檔CdCl
1 房地
2 土地
3 股票
4 其他有價證券
5 銀行保證
9 動產
	* @return Integer
	*/
  public int getClCode1() {
    return this.clCode1;
  }

/**
	* 主要擔保品代號1<br>
	* 擔保品代號檔CdCl
1 房地
2 土地
3 股票
4 其他有價證券
5 銀行保證
9 動產
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
	* 轉催收日期<br>
	* 
	* @return Integer
	*/
  public int getOvduDate() {
    return StaticTool.bcToRoc(this.ovduDate);
  }

/**
	* 轉催收日期<br>
	* 
  *
  * @param ovduDate 轉催收日期
  * @throws LogicException when Date Is Warn	*/
  public void setOvduDate(int ovduDate) throws LogicException {
    this.ovduDate = StaticTool.rocToBc(ovduDate);
  }

/**
	* 催收本金餘額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getOvduPrinBal() {
    return this.ovduPrinBal;
  }

/**
	* 催收本金餘額<br>
	* 
  *
  * @param ovduPrinBal 催收本金餘額
	*/
  public void setOvduPrinBal(BigDecimal ovduPrinBal) {
    this.ovduPrinBal = ovduPrinBal;
  }

/**
	* 催收利息餘額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getOvduIntBal() {
    return this.ovduIntBal;
  }

/**
	* 催收利息餘額<br>
	* 
  *
  * @param ovduIntBal 催收利息餘額
	*/
  public void setOvduIntBal(BigDecimal ovduIntBal) {
    this.ovduIntBal = ovduIntBal;
  }

/**
	* 催收違約金餘額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getOvduBreachBal() {
    return this.ovduBreachBal;
  }

/**
	* 催收違約金餘額<br>
	* 
  *
  * @param ovduBreachBal 催收違約金餘額
	*/
  public void setOvduBreachBal(BigDecimal ovduBreachBal) {
    this.ovduBreachBal = ovduBreachBal;
  }

/**
	* 催收餘額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getOvduBal() {
    return this.ovduBal;
  }

/**
	* 催收餘額<br>
	* 
  *
  * @param ovduBal 催收餘額
	*/
  public void setOvduBal(BigDecimal ovduBal) {
    this.ovduBal = ovduBal;
  }

/**
	* 無擔保金額<br>
	* 1.無擔保債權設定金額(法務進度:901)，資產分類一律為五
2.L7205上傳更新
	* @return BigDecimal
	*/
  public BigDecimal getLawAmount() {
    return this.lawAmount;
  }

/**
	* 無擔保金額<br>
	* 1.無擔保債權設定金額(法務進度:901)，資產分類一律為五
2.L7205上傳更新
  *
  * @param lawAmount 無擔保金額
	*/
  public void setLawAmount(BigDecimal lawAmount) {
    this.lawAmount = lawAmount;
  }

/**
	* 資產五分類代號(有擔保部分)<br>
	* 1~5 (有擔保部分)
	* @return String
	*/
  public String getAssetClass() {
    return this.assetClass == null ? "" : this.assetClass;
  }

/**
	* 資產五分類代號(有擔保部分)<br>
	* 1~5 (有擔保部分)
  *
  * @param assetClass 資產五分類代號(有擔保部分)
	*/
  public void setAssetClass(String assetClass) {
    this.assetClass = assetClass;
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

/**
	* 無擔保資產分類代號<br>
	* L7205上傳更新
4:四
5:五
	* @return String
	*/
  public String getLawAssetClass() {
    return this.lawAssetClass == null ? "" : this.lawAssetClass;
  }

/**
	* 無擔保資產分類代號<br>
	* L7205上傳更新
4:四
5:五
  *
  * @param lawAssetClass 無擔保資產分類代號
	*/
  public void setLawAssetClass(String lawAssetClass) {
    this.lawAssetClass = lawAssetClass;
  }

/**
	* 資產五分類代號2(有擔保部分)<br>
	* L7205上傳更新
11:一之1(正常繳息)
12:一之2(特定放款資產項目)
21:二之1
22:二之2
23:二之3
3:三  
4:四   
5:五
此為有擔保金額(PrinBalance-LawAmount)的分類
	* @return String
	*/
  public String getAssetClass2() {
    return this.assetClass2 == null ? "" : this.assetClass2;
  }

/**
	* 資產五分類代號2(有擔保部分)<br>
	* L7205上傳更新
11:一之1(正常繳息)
12:一之2(特定放款資產項目)
21:二之1
22:二之2
23:二之3
3:三  
4:四   
5:五
此為有擔保金額(PrinBalance-LawAmount)的分類
  *
  * @param assetClass2 資產五分類代號2(有擔保部分)
	*/
  public void setAssetClass2(String assetClass2) {
    this.assetClass2 = assetClass2;
  }

/**
	* 是否為利害關係人<br>
	* 寫入時為空值，由L7206[利關人名單檔上傳]上傳更新
Y/N
	* @return String
	*/
  public String getBankRelationFlag() {
    return this.bankRelationFlag == null ? "" : this.bankRelationFlag;
  }

/**
	* 是否為利害關係人<br>
	* 寫入時為空值，由L7206[利關人名單檔上傳]上傳更新
Y/N
  *
  * @param bankRelationFlag 是否為利害關係人
	*/
  public void setBankRelationFlag(String bankRelationFlag) {
    this.bankRelationFlag = bankRelationFlag;
  }

/**
	* 政策性專案貸款<br>
	* 寫入時為空值，L7205上傳更新
政策性專案中:
Y:初貸日大於等於20110101
C:初貸日小於20110101
N:無
	* @return String
	*/
  public String getGovProjectFlag() {
    return this.govProjectFlag == null ? "" : this.govProjectFlag;
  }

/**
	* 政策性專案貸款<br>
	* 寫入時為空值，L7205上傳更新
政策性專案中:
Y:初貸日大於等於20110101
C:初貸日小於20110101
N:無
  *
  * @param govProjectFlag 政策性專案貸款
	*/
  public void setGovProjectFlag(String govProjectFlag) {
    this.govProjectFlag = govProjectFlag;
  }

/**
	* 建築貸款記號<br>
	* 寫入時為空值，L7205上傳更新
Y/N
	* @return String
	*/
  public String getBuildingFlag() {
    return this.buildingFlag == null ? "" : this.buildingFlag;
  }

/**
	* 建築貸款記號<br>
	* 寫入時為空值，L7205上傳更新
Y/N
  *
  * @param buildingFlag 建築貸款記號
	*/
  public void setBuildingFlag(String buildingFlag) {
    this.buildingFlag = buildingFlag;
  }

/**
	* 特定資產記號<br>
	* 寫入時為空值，L7205上傳更新
Y/N
	* @return String
	*/
  public String getSpecialAssetFlag() {
    return this.specialAssetFlag == null ? "" : this.specialAssetFlag;
  }

/**
	* 特定資產記號<br>
	* 寫入時為空值，L7205上傳更新
Y/N
  *
  * @param specialAssetFlag 特定資產記號
	*/
  public void setSpecialAssetFlag(String specialAssetFlag) {
    this.specialAssetFlag = specialAssetFlag;
  }


  @Override
  public String toString() {
    return "MonthlyFacBal [monthlyFacBalId=" + monthlyFacBalId + ", prevIntDate=" + prevIntDate + ", nextIntDate=" + nextIntDate + ", dueDate=" + dueDate
           + ", ovduTerm=" + ovduTerm + ", ovduDays=" + ovduDays + ", currencyCode=" + currencyCode + ", prinBalance=" + prinBalance + ", badDebtBal=" + badDebtBal + ", accCollPsn=" + accCollPsn
           + ", legalPsn=" + legalPsn + ", status=" + status + ", acctCode=" + acctCode + ", facAcctCode=" + facAcctCode + ", clCustNo=" + clCustNo + ", clFacmNo=" + clFacmNo
           + ", clRowNo=" + clRowNo + ", renewCode=" + renewCode + ", prodNo=" + prodNo + ", acBookCode=" + acBookCode + ", entCode=" + entCode + ", relsCode=" + relsCode
           + ", departmentCode=" + departmentCode + ", unpaidPrincipal=" + unpaidPrincipal + ", unpaidInterest=" + unpaidInterest + ", unpaidBreachAmt=" + unpaidBreachAmt + ", unpaidDelayInt=" + unpaidDelayInt + ", acdrPrincipal=" + acdrPrincipal
           + ", acdrInterest=" + acdrInterest + ", acdrBreachAmt=" + acdrBreachAmt + ", acdrDelayInt=" + acdrDelayInt + ", fireFee=" + fireFee + ", lawFee=" + lawFee + ", modifyFee=" + modifyFee
           + ", acctFee=" + acctFee + ", shortfallPrin=" + shortfallPrin + ", shortfallInt=" + shortfallInt + ", tempAmt=" + tempAmt + ", clCode1=" + clCode1 + ", clCode2=" + clCode2
           + ", clNo=" + clNo + ", cityCode=" + cityCode + ", ovduDate=" + ovduDate + ", ovduPrinBal=" + ovduPrinBal + ", ovduIntBal=" + ovduIntBal + ", ovduBreachBal=" + ovduBreachBal
           + ", ovduBal=" + ovduBal + ", lawAmount=" + lawAmount + ", assetClass=" + assetClass + ", storeRate=" + storeRate + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
           + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + ", acSubBookCode=" + acSubBookCode + ", lawAssetClass=" + lawAssetClass + ", assetClass2=" + assetClass2 + ", bankRelationFlag=" + bankRelationFlag
           + ", govProjectFlag=" + govProjectFlag + ", buildingFlag=" + buildingFlag + ", specialAssetFlag=" + specialAssetFlag + "]";
  }
}
