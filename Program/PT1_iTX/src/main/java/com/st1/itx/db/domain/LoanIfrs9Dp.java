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
 * LoanIfrs9Dp IFRS9欄位清單4<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`LoanIfrs9Dp`")
public class LoanIfrs9Dp implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -3191549821185244989L;

@EmbeddedId
  private LoanIfrs9DpId loanIfrs9DpId;

  // 年月份
  @Column(name = "`DataYM`", insertable = false, updatable = false)
  private int dataYM = 0;

  // 戶號
  @Column(name = "`CustNo`", insertable = false, updatable = false)
  private int custNo = 0;

  // 借款人ID / 統編
  @Column(name = "`CustId`", length = 10)
  private String custId;

  // 額度編號
  @Column(name = "`FacmNo`", insertable = false, updatable = false)
  private int facmNo = 0;

  // 撥款序號
  @Column(name = "`BormNo`", insertable = false, updatable = false)
  private int bormNo = 0;

  // 資料類別
  /* 媒體檔排序用1=法拍完成資料檔2=逾期天數&amp;gt;=90天且未在法拍完成資料檔中 */
  @Column(name = "`DataFg`")
  private int dataFg = 0;

  // 會計科目
  @Column(name = "`AcCode`", length = 11)
  private String acCode;

  // 案件狀態
  /* 1=正常2=催收3=呆帳(新壽有部份轉呆的狀態，但若屬本項情形，狀態轉列為"呆帳") */
  @Column(name = "`Status`")
  private int status = 0;

  // 初貸日期
  @Column(name = "`FirstDrawdownDate`")
  private int firstDrawdownDate = 0;

  // 貸放日期
  @Column(name = "`DrawdownDate`")
  private int drawdownDate = 0;

  // 到期日
  @Column(name = "`MaturityDate`")
  private int maturityDate = 0;

  // 核准金額(台幣)
  /* 每額度編號項下之放款帳號皆同 */
  @Column(name = "`LineAmt`")
  private BigDecimal lineAmt = new BigDecimal("0");

  // 撥款金額(台幣)
  @Column(name = "`DrawdownAmt`")
  private BigDecimal drawdownAmt = new BigDecimal("0");

  // 本金餘額(撥款)(台幣)
  @Column(name = "`LoanBal`")
  private BigDecimal loanBal = new BigDecimal("0");

  // 應收利息(台幣)
  @Column(name = "`IntAmt`")
  private BigDecimal intAmt = new BigDecimal("0");

  // 法拍及火險費用(台幣)
  @Column(name = "`Fee`")
  private BigDecimal fee = new BigDecimal("0");

  // 逾期繳款天數
  @Column(name = "`OvduDays`")
  private int ovduDays = 0;

  // 轉催收款日期
  @Column(name = "`OvduDate`")
  private int ovduDate = 0;

  // 轉銷呆帳日期
  /* 最早之轉銷呆帳日期 */
  @Column(name = "`BadDebtDate`")
  private int badDebtDate = 0;

  // 轉銷呆帳金額
  @Column(name = "`BadDebtAmt`")
  private BigDecimal badDebtAmt = new BigDecimal("0");

  // stage3發生日期
  /* YYYYMMDD 例：20100108一般  :繳息迄日+120協議件:60~62, MIN(撥款日期, 繳息迄日+120) */
  @Column(name = "`DerDate`")
  private int derDate = 0;

  // 上述發生日期前之最近一次利率
  /* stage3發生日期 當時利率 */
  @Column(name = "`DerRate`")
  private BigDecimal derRate = new BigDecimal("0");

  // 上述發生日期時之本金餘額(台幣)
  /* 轉催本金+轉催利息-還款金額-轉銷呆帳金額正常戶本金-還款金額-轉銷呆帳金額stage3發生日期 當月底餘額 */
  @Column(name = "`DerLoanBal`")
  private BigDecimal derLoanBal = new BigDecimal("0");

  // 上述發生日期時之應收利息(台幣)
  /* 正常戶利息 */
  @Column(name = "`DerIntAmt`")
  private BigDecimal derIntAmt = new BigDecimal("0");

  // 上述發生日期時之法拍及火險費用(台幣)
  @Column(name = "`DerFee`")
  private BigDecimal derFee = new BigDecimal("0");

  // stage3發生後第一年本金回收金額(台幣)
  /* 以月底資料判斷，假設發生之日期為2005.11.30，則第一年本金回收金額為 (2005.11.30本金總餘額 ~ 2006.11.30本金總餘額) */
  @Column(name = "`DerY1Amt`")
  private BigDecimal derY1Amt = new BigDecimal("0");

  // stage3發生後第二年本金回收金額(台幣)
  /* 以月底資料判斷，假設發生之日期為2005.11.30，則第二年本金回收金額為 (2006.11.30本金總餘額 ~ 2007.11.30本金總餘額) */
  @Column(name = "`DerY2Amt`")
  private BigDecimal derY2Amt = new BigDecimal("0");

  // stage3發生後第三年本金回收金額(台幣)
  /* 以月底資料判斷，假設發生之日期為2005.11.30，則第三年本金回收金額為 (2007.11.30本金總餘額 ~ 2008.11.30本金總餘額) */
  @Column(name = "`DerY3Amt`")
  private BigDecimal derY3Amt = new BigDecimal("0");

  // stage3發生後第四年本金回收金額(台幣)
  /* 計算邏輯同上，若資料期間不足則以0表示 */
  @Column(name = "`DerY4Amt`")
  private BigDecimal derY4Amt = new BigDecimal("0");

  // stage3發生後第五年本金回收金額(台幣)
  /* 計算邏輯同上，若資料期間不足則以0表示 */
  @Column(name = "`DerY5Amt`")
  private BigDecimal derY5Amt = new BigDecimal("0");

  // stage3發生後第一年應收利息回收金額(台幣)
  /* 以月底資料判斷，計算方式同本金回收金額實收利息(不含延遲息,違約金) */
  @Column(name = "`DerY1Int`")
  private BigDecimal derY1Int = new BigDecimal("0");

  // stage3發生後第二年應收利息回收金額(台幣)
  /* 以月底資料判斷，計算方式同本金回收金額 */
  @Column(name = "`DerY2Int`")
  private BigDecimal derY2Int = new BigDecimal("0");

  // stage3發生後第三年應收利息回收金額(台幣)
  /* 以月底資料判斷，計算方式同本金回收金額 */
  @Column(name = "`DerY3Int`")
  private BigDecimal derY3Int = new BigDecimal("0");

  // stage3發生後第四年應收利息回收金額(台幣)
  /* 以月底資料判斷，計算方式同本金回收金額 */
  @Column(name = "`DerY4Int`")
  private BigDecimal derY4Int = new BigDecimal("0");

  // stage3發生後第五年應收利息回收金額(台幣)
  /* 以月底資料判斷，計算方式同本金回收金額 */
  @Column(name = "`DerY5Int`")
  private BigDecimal derY5Int = new BigDecimal("0");

  // stage3發生後第一年法拍及火險費用回收金額(台幣)
  /* 以月底資料判斷，計算方式同本金回收金額實收法拍及火險費 */
  @Column(name = "`DerY1Fee`")
  private BigDecimal derY1Fee = new BigDecimal("0");

  // stage3發生後第二年法拍及火險費用回收金額(台幣)
  /* 以月底資料判斷，計算方式同本金回收金額 */
  @Column(name = "`DerY2Fee`")
  private BigDecimal derY2Fee = new BigDecimal("0");

  // stage3發生後第三年法拍及火險費用回收金額(台幣)
  /* 以月底資料判斷，計算方式同本金回收金額 */
  @Column(name = "`DerY3Fee`")
  private BigDecimal derY3Fee = new BigDecimal("0");

  // stage3發生後第四年法拍及火險費用回收金額(台幣)
  /* 以月底資料判斷，計算方式同本金回收金額 */
  @Column(name = "`DerY4Fee`")
  private BigDecimal derY4Fee = new BigDecimal("0");

  // stage3發生後第五年法拍及火險費用回收金額(台幣)
  /* 以月底資料判斷，計算方式同本金回收金額 */
  @Column(name = "`DerY5Fee`")
  private BigDecimal derY5Fee = new BigDecimal("0");

  // 授信行業別
  @Column(name = "`IndustryCode`", length = 6)
  private String industryCode;

  // 擔保品類別
  /* 以對應至JCIC的類別 */
  @Column(name = "`ClTypeJCIC`", length = 2)
  private String clTypeJCIC;

  // 擔保品地區別
  /* A=臺北市B=新北市C=桃園市D=台中市E=台南市F=高雄市G=其他(基隆市、新竹縣、新竹市、苗栗縣、彰化縣、南投縣、雲林縣、嘉義縣、嘉義市、屏東縣、宜蘭縣、花蓮縣、臺東縣、金門縣、澎湖縣、連江縣、南海島、釣魚臺 */
  @Column(name = "`AreaCode`", length = 3)
  private String areaCode;

  // 商品利率代碼
  @Column(name = "`ProdCode`", length = 5)
  private String prodCode;

  // 企業戶/個人戶
  /* 1=企業戶2=個人戶依現行代碼，唯企業戶與個人戶之分類需參考信用評等模型。自然人採用企金自然人評等模型者，應歸類為企業戶 */
  @Column(name = "`CustKind`")
  private int custKind = 0;

  // 產品別
  @Column(name = "`Ifrs9ProdCode`", length = 2)
  private String ifrs9ProdCode;

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


  public LoanIfrs9DpId getLoanIfrs9DpId() {
    return this.loanIfrs9DpId;
  }

  public void setLoanIfrs9DpId(LoanIfrs9DpId loanIfrs9DpId) {
    this.loanIfrs9DpId = loanIfrs9DpId;
  }

/**
	* 年月份<br>
	* 
	* @return Integer
	*/
  public int getDataYM() {
    return this.dataYM;
  }

/**
	* 年月份<br>
	* 
  *
  * @param dataYM 年月份
	*/
  public void setDataYM(int dataYM) {
    this.dataYM = dataYM;
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
	* 借款人ID / 統編<br>
	* 
	* @return String
	*/
  public String getCustId() {
    return this.custId == null ? "" : this.custId;
  }

/**
	* 借款人ID / 統編<br>
	* 
  *
  * @param custId 借款人ID / 統編
	*/
  public void setCustId(String custId) {
    this.custId = custId;
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
	* 資料類別<br>
	* 媒體檔排序用
1=法拍完成資料檔
2=逾期天數&amp;gt;=90天且未在法拍完成資料檔中
	* @return Integer
	*/
  public int getDataFg() {
    return this.dataFg;
  }

/**
	* 資料類別<br>
	* 媒體檔排序用
1=法拍完成資料檔
2=逾期天數&amp;gt;=90天且未在法拍完成資料檔中
  *
  * @param dataFg 資料類別
	*/
  public void setDataFg(int dataFg) {
    this.dataFg = dataFg;
  }

/**
	* 會計科目<br>
	* 
	* @return String
	*/
  public String getAcCode() {
    return this.acCode == null ? "" : this.acCode;
  }

/**
	* 會計科目<br>
	* 
  *
  * @param acCode 會計科目
	*/
  public void setAcCode(String acCode) {
    this.acCode = acCode;
  }

/**
	* 案件狀態<br>
	* 1=正常
2=催收
3=呆帳(新壽有部份轉呆的狀態，但若屬本項情形，狀態轉列為"呆帳")
	* @return Integer
	*/
  public int getStatus() {
    return this.status;
  }

/**
	* 案件狀態<br>
	* 1=正常
2=催收
3=呆帳(新壽有部份轉呆的狀態，但若屬本項情形，狀態轉列為"呆帳")
  *
  * @param status 案件狀態
	*/
  public void setStatus(int status) {
    this.status = status;
  }

/**
	* 初貸日期<br>
	* 
	* @return Integer
	*/
  public int getFirstDrawdownDate() {
    return StaticTool.bcToRoc(this.firstDrawdownDate);
  }

/**
	* 初貸日期<br>
	* 
  *
  * @param firstDrawdownDate 初貸日期
  * @throws LogicException when Date Is Warn	*/
  public void setFirstDrawdownDate(int firstDrawdownDate) throws LogicException {
    this.firstDrawdownDate = StaticTool.rocToBc(firstDrawdownDate);
  }

/**
	* 貸放日期<br>
	* 
	* @return Integer
	*/
  public int getDrawdownDate() {
    return StaticTool.bcToRoc(this.drawdownDate);
  }

/**
	* 貸放日期<br>
	* 
  *
  * @param drawdownDate 貸放日期
  * @throws LogicException when Date Is Warn	*/
  public void setDrawdownDate(int drawdownDate) throws LogicException {
    this.drawdownDate = StaticTool.rocToBc(drawdownDate);
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
	* 核准金額(台幣)<br>
	* 每額度編號項下之放款帳號皆同
	* @return BigDecimal
	*/
  public BigDecimal getLineAmt() {
    return this.lineAmt;
  }

/**
	* 核准金額(台幣)<br>
	* 每額度編號項下之放款帳號皆同
  *
  * @param lineAmt 核准金額(台幣)
	*/
  public void setLineAmt(BigDecimal lineAmt) {
    this.lineAmt = lineAmt;
  }

/**
	* 撥款金額(台幣)<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getDrawdownAmt() {
    return this.drawdownAmt;
  }

/**
	* 撥款金額(台幣)<br>
	* 
  *
  * @param drawdownAmt 撥款金額(台幣)
	*/
  public void setDrawdownAmt(BigDecimal drawdownAmt) {
    this.drawdownAmt = drawdownAmt;
  }

/**
	* 本金餘額(撥款)(台幣)<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLoanBal() {
    return this.loanBal;
  }

/**
	* 本金餘額(撥款)(台幣)<br>
	* 
  *
  * @param loanBal 本金餘額(撥款)(台幣)
	*/
  public void setLoanBal(BigDecimal loanBal) {
    this.loanBal = loanBal;
  }

/**
	* 應收利息(台幣)<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getIntAmt() {
    return this.intAmt;
  }

/**
	* 應收利息(台幣)<br>
	* 
  *
  * @param intAmt 應收利息(台幣)
	*/
  public void setIntAmt(BigDecimal intAmt) {
    this.intAmt = intAmt;
  }

/**
	* 法拍及火險費用(台幣)<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getFee() {
    return this.fee;
  }

/**
	* 法拍及火險費用(台幣)<br>
	* 
  *
  * @param fee 法拍及火險費用(台幣)
	*/
  public void setFee(BigDecimal fee) {
    this.fee = fee;
  }

/**
	* 逾期繳款天數<br>
	* 
	* @return Integer
	*/
  public int getOvduDays() {
    return this.ovduDays;
  }

/**
	* 逾期繳款天數<br>
	* 
  *
  * @param ovduDays 逾期繳款天數
	*/
  public void setOvduDays(int ovduDays) {
    this.ovduDays = ovduDays;
  }

/**
	* 轉催收款日期<br>
	* 
	* @return Integer
	*/
  public int getOvduDate() {
    return StaticTool.bcToRoc(this.ovduDate);
  }

/**
	* 轉催收款日期<br>
	* 
  *
  * @param ovduDate 轉催收款日期
  * @throws LogicException when Date Is Warn	*/
  public void setOvduDate(int ovduDate) throws LogicException {
    this.ovduDate = StaticTool.rocToBc(ovduDate);
  }

/**
	* 轉銷呆帳日期<br>
	* 最早之轉銷呆帳日期
	* @return Integer
	*/
  public int getBadDebtDate() {
    return StaticTool.bcToRoc(this.badDebtDate);
  }

/**
	* 轉銷呆帳日期<br>
	* 最早之轉銷呆帳日期
  *
  * @param badDebtDate 轉銷呆帳日期
  * @throws LogicException when Date Is Warn	*/
  public void setBadDebtDate(int badDebtDate) throws LogicException {
    this.badDebtDate = StaticTool.rocToBc(badDebtDate);
  }

/**
	* 轉銷呆帳金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getBadDebtAmt() {
    return this.badDebtAmt;
  }

/**
	* 轉銷呆帳金額<br>
	* 
  *
  * @param badDebtAmt 轉銷呆帳金額
	*/
  public void setBadDebtAmt(BigDecimal badDebtAmt) {
    this.badDebtAmt = badDebtAmt;
  }

/**
	* stage3發生日期<br>
	* YYYYMMDD 例：20100108
一般  :繳息迄日+120
協議件:60~62, MIN(撥款日期, 繳息迄日+120)
	* @return Integer
	*/
  public int getDerDate() {
    return StaticTool.bcToRoc(this.derDate);
  }

/**
	* stage3發生日期<br>
	* YYYYMMDD 例：20100108
一般  :繳息迄日+120
協議件:60~62, MIN(撥款日期, 繳息迄日+120)
  *
  * @param derDate stage3發生日期
  * @throws LogicException when Date Is Warn	*/
  public void setDerDate(int derDate) throws LogicException {
    this.derDate = StaticTool.rocToBc(derDate);
  }

/**
	* 上述發生日期前之最近一次利率<br>
	* stage3發生日期 當時利率
	* @return BigDecimal
	*/
  public BigDecimal getDerRate() {
    return this.derRate;
  }

/**
	* 上述發生日期前之最近一次利率<br>
	* stage3發生日期 當時利率
  *
  * @param derRate 上述發生日期前之最近一次利率
	*/
  public void setDerRate(BigDecimal derRate) {
    this.derRate = derRate;
  }

/**
	* 上述發生日期時之本金餘額(台幣)<br>
	* 轉催本金+轉催利息-還款金額-轉銷呆帳金額
正常戶本金-還款金額-轉銷呆帳金額
stage3發生日期 當月底餘額
	* @return BigDecimal
	*/
  public BigDecimal getDerLoanBal() {
    return this.derLoanBal;
  }

/**
	* 上述發生日期時之本金餘額(台幣)<br>
	* 轉催本金+轉催利息-還款金額-轉銷呆帳金額
正常戶本金-還款金額-轉銷呆帳金額
stage3發生日期 當月底餘額
  *
  * @param derLoanBal 上述發生日期時之本金餘額(台幣)
	*/
  public void setDerLoanBal(BigDecimal derLoanBal) {
    this.derLoanBal = derLoanBal;
  }

/**
	* 上述發生日期時之應收利息(台幣)<br>
	* 正常戶利息
	* @return BigDecimal
	*/
  public BigDecimal getDerIntAmt() {
    return this.derIntAmt;
  }

/**
	* 上述發生日期時之應收利息(台幣)<br>
	* 正常戶利息
  *
  * @param derIntAmt 上述發生日期時之應收利息(台幣)
	*/
  public void setDerIntAmt(BigDecimal derIntAmt) {
    this.derIntAmt = derIntAmt;
  }

/**
	* 上述發生日期時之法拍及火險費用(台幣)<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getDerFee() {
    return this.derFee;
  }

/**
	* 上述發生日期時之法拍及火險費用(台幣)<br>
	* 
  *
  * @param derFee 上述發生日期時之法拍及火險費用(台幣)
	*/
  public void setDerFee(BigDecimal derFee) {
    this.derFee = derFee;
  }

/**
	* stage3發生後第一年本金回收金額(台幣)<br>
	* 以月底資料判斷，假設發生之日期為2005.11.30，則第一年本金回收金額為 (2005.11.30本金總餘額 ~ 2006.11.30本金總餘額)
	* @return BigDecimal
	*/
  public BigDecimal getDerY1Amt() {
    return this.derY1Amt;
  }

/**
	* stage3發生後第一年本金回收金額(台幣)<br>
	* 以月底資料判斷，假設發生之日期為2005.11.30，則第一年本金回收金額為 (2005.11.30本金總餘額 ~ 2006.11.30本金總餘額)
  *
  * @param derY1Amt stage3發生後第一年本金回收金額(台幣)
	*/
  public void setDerY1Amt(BigDecimal derY1Amt) {
    this.derY1Amt = derY1Amt;
  }

/**
	* stage3發生後第二年本金回收金額(台幣)<br>
	* 以月底資料判斷，假設發生之日期為2005.11.30，則第二年本金回收金額為 (2006.11.30本金總餘額 ~ 2007.11.30本金總餘額)
	* @return BigDecimal
	*/
  public BigDecimal getDerY2Amt() {
    return this.derY2Amt;
  }

/**
	* stage3發生後第二年本金回收金額(台幣)<br>
	* 以月底資料判斷，假設發生之日期為2005.11.30，則第二年本金回收金額為 (2006.11.30本金總餘額 ~ 2007.11.30本金總餘額)
  *
  * @param derY2Amt stage3發生後第二年本金回收金額(台幣)
	*/
  public void setDerY2Amt(BigDecimal derY2Amt) {
    this.derY2Amt = derY2Amt;
  }

/**
	* stage3發生後第三年本金回收金額(台幣)<br>
	* 以月底資料判斷，假設發生之日期為2005.11.30，則第三年本金回收金額為 (2007.11.30本金總餘額 ~ 2008.11.30本金總餘額)
	* @return BigDecimal
	*/
  public BigDecimal getDerY3Amt() {
    return this.derY3Amt;
  }

/**
	* stage3發生後第三年本金回收金額(台幣)<br>
	* 以月底資料判斷，假設發生之日期為2005.11.30，則第三年本金回收金額為 (2007.11.30本金總餘額 ~ 2008.11.30本金總餘額)
  *
  * @param derY3Amt stage3發生後第三年本金回收金額(台幣)
	*/
  public void setDerY3Amt(BigDecimal derY3Amt) {
    this.derY3Amt = derY3Amt;
  }

/**
	* stage3發生後第四年本金回收金額(台幣)<br>
	* 計算邏輯同上，若資料期間不足則以0表示
	* @return BigDecimal
	*/
  public BigDecimal getDerY4Amt() {
    return this.derY4Amt;
  }

/**
	* stage3發生後第四年本金回收金額(台幣)<br>
	* 計算邏輯同上，若資料期間不足則以0表示
  *
  * @param derY4Amt stage3發生後第四年本金回收金額(台幣)
	*/
  public void setDerY4Amt(BigDecimal derY4Amt) {
    this.derY4Amt = derY4Amt;
  }

/**
	* stage3發生後第五年本金回收金額(台幣)<br>
	* 計算邏輯同上，若資料期間不足則以0表示
	* @return BigDecimal
	*/
  public BigDecimal getDerY5Amt() {
    return this.derY5Amt;
  }

/**
	* stage3發生後第五年本金回收金額(台幣)<br>
	* 計算邏輯同上，若資料期間不足則以0表示
  *
  * @param derY5Amt stage3發生後第五年本金回收金額(台幣)
	*/
  public void setDerY5Amt(BigDecimal derY5Amt) {
    this.derY5Amt = derY5Amt;
  }

/**
	* stage3發生後第一年應收利息回收金額(台幣)<br>
	* 以月底資料判斷，計算方式同本金回收金額
實收利息(不含延遲息,違約金)
	* @return BigDecimal
	*/
  public BigDecimal getDerY1Int() {
    return this.derY1Int;
  }

/**
	* stage3發生後第一年應收利息回收金額(台幣)<br>
	* 以月底資料判斷，計算方式同本金回收金額
實收利息(不含延遲息,違約金)
  *
  * @param derY1Int stage3發生後第一年應收利息回收金額(台幣)
	*/
  public void setDerY1Int(BigDecimal derY1Int) {
    this.derY1Int = derY1Int;
  }

/**
	* stage3發生後第二年應收利息回收金額(台幣)<br>
	* 以月底資料判斷，計算方式同本金回收金額
	* @return BigDecimal
	*/
  public BigDecimal getDerY2Int() {
    return this.derY2Int;
  }

/**
	* stage3發生後第二年應收利息回收金額(台幣)<br>
	* 以月底資料判斷，計算方式同本金回收金額
  *
  * @param derY2Int stage3發生後第二年應收利息回收金額(台幣)
	*/
  public void setDerY2Int(BigDecimal derY2Int) {
    this.derY2Int = derY2Int;
  }

/**
	* stage3發生後第三年應收利息回收金額(台幣)<br>
	* 以月底資料判斷，計算方式同本金回收金額
	* @return BigDecimal
	*/
  public BigDecimal getDerY3Int() {
    return this.derY3Int;
  }

/**
	* stage3發生後第三年應收利息回收金額(台幣)<br>
	* 以月底資料判斷，計算方式同本金回收金額
  *
  * @param derY3Int stage3發生後第三年應收利息回收金額(台幣)
	*/
  public void setDerY3Int(BigDecimal derY3Int) {
    this.derY3Int = derY3Int;
  }

/**
	* stage3發生後第四年應收利息回收金額(台幣)<br>
	* 以月底資料判斷，計算方式同本金回收金額
	* @return BigDecimal
	*/
  public BigDecimal getDerY4Int() {
    return this.derY4Int;
  }

/**
	* stage3發生後第四年應收利息回收金額(台幣)<br>
	* 以月底資料判斷，計算方式同本金回收金額
  *
  * @param derY4Int stage3發生後第四年應收利息回收金額(台幣)
	*/
  public void setDerY4Int(BigDecimal derY4Int) {
    this.derY4Int = derY4Int;
  }

/**
	* stage3發生後第五年應收利息回收金額(台幣)<br>
	* 以月底資料判斷，計算方式同本金回收金額
	* @return BigDecimal
	*/
  public BigDecimal getDerY5Int() {
    return this.derY5Int;
  }

/**
	* stage3發生後第五年應收利息回收金額(台幣)<br>
	* 以月底資料判斷，計算方式同本金回收金額
  *
  * @param derY5Int stage3發生後第五年應收利息回收金額(台幣)
	*/
  public void setDerY5Int(BigDecimal derY5Int) {
    this.derY5Int = derY5Int;
  }

/**
	* stage3發生後第一年法拍及火險費用回收金額(台幣)<br>
	* 以月底資料判斷，計算方式同本金回收金額
實收法拍及火險費
	* @return BigDecimal
	*/
  public BigDecimal getDerY1Fee() {
    return this.derY1Fee;
  }

/**
	* stage3發生後第一年法拍及火險費用回收金額(台幣)<br>
	* 以月底資料判斷，計算方式同本金回收金額
實收法拍及火險費
  *
  * @param derY1Fee stage3發生後第一年法拍及火險費用回收金額(台幣)
	*/
  public void setDerY1Fee(BigDecimal derY1Fee) {
    this.derY1Fee = derY1Fee;
  }

/**
	* stage3發生後第二年法拍及火險費用回收金額(台幣)<br>
	* 以月底資料判斷，計算方式同本金回收金額
	* @return BigDecimal
	*/
  public BigDecimal getDerY2Fee() {
    return this.derY2Fee;
  }

/**
	* stage3發生後第二年法拍及火險費用回收金額(台幣)<br>
	* 以月底資料判斷，計算方式同本金回收金額
  *
  * @param derY2Fee stage3發生後第二年法拍及火險費用回收金額(台幣)
	*/
  public void setDerY2Fee(BigDecimal derY2Fee) {
    this.derY2Fee = derY2Fee;
  }

/**
	* stage3發生後第三年法拍及火險費用回收金額(台幣)<br>
	* 以月底資料判斷，計算方式同本金回收金額
	* @return BigDecimal
	*/
  public BigDecimal getDerY3Fee() {
    return this.derY3Fee;
  }

/**
	* stage3發生後第三年法拍及火險費用回收金額(台幣)<br>
	* 以月底資料判斷，計算方式同本金回收金額
  *
  * @param derY3Fee stage3發生後第三年法拍及火險費用回收金額(台幣)
	*/
  public void setDerY3Fee(BigDecimal derY3Fee) {
    this.derY3Fee = derY3Fee;
  }

/**
	* stage3發生後第四年法拍及火險費用回收金額(台幣)<br>
	* 以月底資料判斷，計算方式同本金回收金額
	* @return BigDecimal
	*/
  public BigDecimal getDerY4Fee() {
    return this.derY4Fee;
  }

/**
	* stage3發生後第四年法拍及火險費用回收金額(台幣)<br>
	* 以月底資料判斷，計算方式同本金回收金額
  *
  * @param derY4Fee stage3發生後第四年法拍及火險費用回收金額(台幣)
	*/
  public void setDerY4Fee(BigDecimal derY4Fee) {
    this.derY4Fee = derY4Fee;
  }

/**
	* stage3發生後第五年法拍及火險費用回收金額(台幣)<br>
	* 以月底資料判斷，計算方式同本金回收金額
	* @return BigDecimal
	*/
  public BigDecimal getDerY5Fee() {
    return this.derY5Fee;
  }

/**
	* stage3發生後第五年法拍及火險費用回收金額(台幣)<br>
	* 以月底資料判斷，計算方式同本金回收金額
  *
  * @param derY5Fee stage3發生後第五年法拍及火險費用回收金額(台幣)
	*/
  public void setDerY5Fee(BigDecimal derY5Fee) {
    this.derY5Fee = derY5Fee;
  }

/**
	* 授信行業別<br>
	* 
	* @return String
	*/
  public String getIndustryCode() {
    return this.industryCode == null ? "" : this.industryCode;
  }

/**
	* 授信行業別<br>
	* 
  *
  * @param industryCode 授信行業別
	*/
  public void setIndustryCode(String industryCode) {
    this.industryCode = industryCode;
  }

/**
	* 擔保品類別<br>
	* 以對應至JCIC的類別
	* @return String
	*/
  public String getClTypeJCIC() {
    return this.clTypeJCIC == null ? "" : this.clTypeJCIC;
  }

/**
	* 擔保品類別<br>
	* 以對應至JCIC的類別
  *
  * @param clTypeJCIC 擔保品類別
	*/
  public void setClTypeJCIC(String clTypeJCIC) {
    this.clTypeJCIC = clTypeJCIC;
  }

/**
	* 擔保品地區別<br>
	* A=臺北市
B=新北市
C=桃園市
D=台中市
E=台南市
F=高雄市
G=其他(基隆市、新竹縣、新竹市、苗栗縣、彰化縣、南投縣、雲林縣、嘉義縣、嘉義市、屏東縣、宜蘭縣、花蓮縣、臺東縣、金門縣、澎湖縣、連江縣、南海島、釣魚臺
	* @return String
	*/
  public String getAreaCode() {
    return this.areaCode == null ? "" : this.areaCode;
  }

/**
	* 擔保品地區別<br>
	* A=臺北市
B=新北市
C=桃園市
D=台中市
E=台南市
F=高雄市
G=其他(基隆市、新竹縣、新竹市、苗栗縣、彰化縣、南投縣、雲林縣、嘉義縣、嘉義市、屏東縣、宜蘭縣、花蓮縣、臺東縣、金門縣、澎湖縣、連江縣、南海島、釣魚臺
  *
  * @param areaCode 擔保品地區別
	*/
  public void setAreaCode(String areaCode) {
    this.areaCode = areaCode;
  }

/**
	* 商品利率代碼<br>
	* 
	* @return String
	*/
  public String getProdCode() {
    return this.prodCode == null ? "" : this.prodCode;
  }

/**
	* 商品利率代碼<br>
	* 
  *
  * @param prodCode 商品利率代碼
	*/
  public void setProdCode(String prodCode) {
    this.prodCode = prodCode;
  }

/**
	* 企業戶/個人戶<br>
	* 1=企業戶
2=個人戶
依現行代碼，唯企業戶與個人戶之分類需參考信用評等模型。自然人採用企金自然人評等模型者，應歸類為企業戶
	* @return Integer
	*/
  public int getCustKind() {
    return this.custKind;
  }

/**
	* 企業戶/個人戶<br>
	* 1=企業戶
2=個人戶
依現行代碼，唯企業戶與個人戶之分類需參考信用評等模型。自然人採用企金自然人評等模型者，應歸類為企業戶
  *
  * @param custKind 企業戶/個人戶
	*/
  public void setCustKind(int custKind) {
    this.custKind = custKind;
  }

/**
	* 產品別<br>
	* 
	* @return String
	*/
  public String getIfrs9ProdCode() {
    return this.ifrs9ProdCode == null ? "" : this.ifrs9ProdCode;
  }

/**
	* 產品別<br>
	* 
  *
  * @param ifrs9ProdCode 產品別
	*/
  public void setIfrs9ProdCode(String ifrs9ProdCode) {
    this.ifrs9ProdCode = ifrs9ProdCode;
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
    return "LoanIfrs9Dp [loanIfrs9DpId=" + loanIfrs9DpId + ", custId=" + custId + ", dataFg=" + dataFg
           + ", acCode=" + acCode + ", status=" + status + ", firstDrawdownDate=" + firstDrawdownDate + ", drawdownDate=" + drawdownDate + ", maturityDate=" + maturityDate + ", lineAmt=" + lineAmt
           + ", drawdownAmt=" + drawdownAmt + ", loanBal=" + loanBal + ", intAmt=" + intAmt + ", fee=" + fee + ", ovduDays=" + ovduDays + ", ovduDate=" + ovduDate
           + ", badDebtDate=" + badDebtDate + ", badDebtAmt=" + badDebtAmt + ", derDate=" + derDate + ", derRate=" + derRate + ", derLoanBal=" + derLoanBal + ", derIntAmt=" + derIntAmt
           + ", derFee=" + derFee + ", derY1Amt=" + derY1Amt + ", derY2Amt=" + derY2Amt + ", derY3Amt=" + derY3Amt + ", derY4Amt=" + derY4Amt + ", derY5Amt=" + derY5Amt
           + ", derY1Int=" + derY1Int + ", derY2Int=" + derY2Int + ", derY3Int=" + derY3Int + ", derY4Int=" + derY4Int + ", derY5Int=" + derY5Int + ", derY1Fee=" + derY1Fee
           + ", derY2Fee=" + derY2Fee + ", derY3Fee=" + derY3Fee + ", derY4Fee=" + derY4Fee + ", derY5Fee=" + derY5Fee + ", industryCode=" + industryCode + ", clTypeJCIC=" + clTypeJCIC
           + ", areaCode=" + areaCode + ", prodCode=" + prodCode + ", custKind=" + custKind + ", ifrs9ProdCode=" + ifrs9ProdCode + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
           + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
