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
 * LoanIfrs9Ip IFRS9欄位清單9<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`LoanIfrs9Ip`")
public class LoanIfrs9Ip implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 2888234781562227925L;

@EmbeddedId
  private LoanIfrs9IpId loanIfrs9IpId;

  // 年月份
  @Column(name = "`DataYM`", insertable = false, updatable = false)
  private int dataYM = 0;

  // 戶號
  /* 產檔時顯示空白 */
  @Column(name = "`CustNo`", insertable = false, updatable = false)
  private int custNo = 0;

  // 借款人ID / 統編
  @Column(name = "`CustId`", length = 10)
  private String custId;

  // 額度編號
  @Column(name = "`FacmNo`", insertable = false, updatable = false)
  private int facmNo = 0;

  // 核准號碼
  @Column(name = "`ApplNo`")
  private int applNo = 0;

  // 已核撥記號
  /* 0=未核撥 1=已核撥供產出媒體檔排序用 */
  @Column(name = "`DrawdownFg`")
  private int drawdownFg = 0;

  // 核准日期
  /* 1.優先取用對保日期2.無對保日採用准駁日3.若無上述二個日期，以空值提供 */
  @Column(name = "`ApproveDate`")
  private int approveDate = 0;

  // 初貸日期
  /* 額度初貸日 */
  @Column(name = "`FirstDrawdownDate`")
  private int firstDrawdownDate = 0;

  // 核准金額
  /* 每額度編號項下之放款帳號皆同核准額度金額 */
  @Column(name = "`LineAmt`")
  private BigDecimal lineAmt = new BigDecimal("0");

  // 帳管費
  @Column(name = "`AcctFee`")
  private BigDecimal acctFee = new BigDecimal("0");

  // 法拍及火險費用
  @Column(name = "`Fee`")
  private BigDecimal fee = new BigDecimal("0");

  // 核准利率
  /* 至小數點後第6位。例如，利率為2.12%，則本欄位值表示0.021200。契約是階梯式...etc,抓取第一年的合約利率(不管加碼利率)(ex:第一年1.4%，第二年1.5%，則本欄位填入1.4%) */
  @Column(name = "`ApproveRate`")
  private BigDecimal approveRate = new BigDecimal("0");

  // 初貸時約定還本寬限期
  /* 約定客戶得只繳息不繳本之寬限期。以月為單位，例如3年寬限期，則本欄位值為36 */
  @Column(name = "`GracePeriod`")
  private int gracePeriod = 0;

  // 契約當時還款方式
  /* 會計日當時契約當時還款方式1=按期繳息(到期還本)；2=平均攤還本息；3=平均攤還本金；4=到期繳息還本 */
  @Column(name = "`AmortizedCode`", length = 1)
  private String amortizedCode;

  // 契約當時利率調整方式
  /* 會計日當時契約當時利率調整方式1=機動；2=固定；3=固定階梯；4=浮動階梯 */
  @Column(name = "`RateCode`", length = 1)
  private String rateCode;

  // 契約約定當時還本週期
  /* 若為到期還本，則填入0；若按月還本，則填入1；季繳，3；半年，6；年繳，12 */
  @Column(name = "`RepayFreq`")
  private int repayFreq = 0;

  // 契約約定當時繳息週期
  /* 若為到期繳息，則填入0；若按月還本，則填入1；季繳，3；半年，6；年繳，12 */
  @Column(name = "`PayIntFreq`")
  private int payIntFreq = 0;

  // 授信行業別
  @Column(name = "`IndustryCode`", length = 6)
  private String industryCode;

  // 擔保品類別
  /* 以對應至JCIC的類別 */
  @Column(name = "`ClTypeJCIC`", length = 2)
  private String clTypeJCIC;

  // 擔保品地區別
  /* A=臺北市B=新北市C=桃園市D=台中市E=台南市F=高雄市G=其他(基隆市、新竹縣、新竹市、苗栗縣、彰化縣、南投縣、雲林縣、嘉義縣、嘉義市、屏東縣、宜蘭縣、花蓮縣、臺東縣、金門縣、澎湖縣、連江縣、南海島、釣魚臺 */
  @Column(name = "`CityCode`", length = 3)
  private String cityCode;

  // 商品利率代碼
  @Column(name = "`ProdNo`", length = 5)
  private String prodNo;

  // 企業戶/個人戶
  /* 1=企業戶2=個人戶自然人採用企金自然人評等模型者，應歸類為企業戶 */
  @Column(name = "`CustKind`")
  private int custKind = 0;

  // 產品別
  /* 作為群組分類。Ex:1=員工；2=車貸；3＝房貸；4＝政府優惠貸款…etc */
  @Column(name = "`Ifrs9ProdCode`", length = 1)
  private String ifrs9ProdCode;

  // 原始鑑價金額
  @Column(name = "`EvaAmt`")
  private BigDecimal evaAmt = new BigDecimal("0");

  // 可動用餘額(台幣)
  @Column(name = "`AvblBal`")
  private BigDecimal avblBal = new BigDecimal("0");

  // 該筆額度是否可循環動用
  /* 0: 非循環動用  1: 循環動用若註記為1，且本金餘額＜核准金額，但是可動用餘額=0；需確認是否實為循環額度 */
  @Column(name = "`RecycleCode`", length = 1)
  private String recycleCode;

  // 該筆額度是否為不可撤銷
  /* 1=是 0=否 */
  @Column(name = "`IrrevocableFlag`", length = 1)
  private String irrevocableFlag;

  // 合約期限
  /* [額度首撥日]~[到期日]轉成幾年幾月幾天 */
  @Column(name = "`LoanTerm`", length = 8)
  private String loanTerm;

  // 備忘分錄會計科目
  @Column(name = "`AcCode`", length = 11)
  private String acCode;

  // 記帳幣別
  /* 1=台幣 2=美元 3=澳幣 4=人民幣 5=歐元 */
  @Column(name = "`AcCurcd`")
  private int acCurcd = 0;

  // 會計帳冊
  /* 1=一般 2=分紅 3=利變 4=OIU */
  @Column(name = "`AcBookCode`", length = 1)
  private String acBookCode;

  // 交易幣別
  /* NTD */
  @Column(name = "`CurrencyCode`", length = 4)
  private String currencyCode;

  // 報導日匯率
  /* 1 */
  @Column(name = "`ExchangeRate`")
  private BigDecimal exchangeRate = new BigDecimal("0");

  // 核准金額(交易幣)
  @Column(name = "`LineAmtCurr`")
  private BigDecimal lineAmtCurr = new BigDecimal("0");

  // 帳管費(交易幣)
  @Column(name = "`AcctFeeCurr`")
  private BigDecimal acctFeeCurr = new BigDecimal("0");

  // 法拍及火險費用(交易幣)
  @Column(name = "`FeeCurr`")
  private BigDecimal feeCurr = new BigDecimal("0");

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


  public LoanIfrs9IpId getLoanIfrs9IpId() {
    return this.loanIfrs9IpId;
  }

  public void setLoanIfrs9IpId(LoanIfrs9IpId loanIfrs9IpId) {
    this.loanIfrs9IpId = loanIfrs9IpId;
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
	* 產檔時顯示空白
	* @return Integer
	*/
  public int getCustNo() {
    return this.custNo;
  }

/**
	* 戶號<br>
	* 產檔時顯示空白
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
	* 核准號碼<br>
	* 
	* @return Integer
	*/
  public int getApplNo() {
    return this.applNo;
  }

/**
	* 核准號碼<br>
	* 
  *
  * @param applNo 核准號碼
	*/
  public void setApplNo(int applNo) {
    this.applNo = applNo;
  }

/**
	* 已核撥記號<br>
	* 0=未核撥 1=已核撥
供產出媒體檔排序用
	* @return Integer
	*/
  public int getDrawdownFg() {
    return this.drawdownFg;
  }

/**
	* 已核撥記號<br>
	* 0=未核撥 1=已核撥
供產出媒體檔排序用
  *
  * @param drawdownFg 已核撥記號
	*/
  public void setDrawdownFg(int drawdownFg) {
    this.drawdownFg = drawdownFg;
  }

/**
	* 核准日期<br>
	* 1.優先取用對保日期
2.無對保日採用准駁日
3.若無上述二個日期，以空值提供
	* @return Integer
	*/
  public int getApproveDate() {
    return StaticTool.bcToRoc(this.approveDate);
  }

/**
	* 核准日期<br>
	* 1.優先取用對保日期
2.無對保日採用准駁日
3.若無上述二個日期，以空值提供
  *
  * @param approveDate 核准日期
  * @throws LogicException when Date Is Warn	*/
  public void setApproveDate(int approveDate) throws LogicException {
    this.approveDate = StaticTool.rocToBc(approveDate);
  }

/**
	* 初貸日期<br>
	* 額度初貸日
	* @return Integer
	*/
  public int getFirstDrawdownDate() {
    return StaticTool.bcToRoc(this.firstDrawdownDate);
  }

/**
	* 初貸日期<br>
	* 額度初貸日
  *
  * @param firstDrawdownDate 初貸日期
  * @throws LogicException when Date Is Warn	*/
  public void setFirstDrawdownDate(int firstDrawdownDate) throws LogicException {
    this.firstDrawdownDate = StaticTool.rocToBc(firstDrawdownDate);
  }

/**
	* 核准金額<br>
	* 每額度編號項下之放款帳號皆同核准額度金額
	* @return BigDecimal
	*/
  public BigDecimal getLineAmt() {
    return this.lineAmt;
  }

/**
	* 核准金額<br>
	* 每額度編號項下之放款帳號皆同核准額度金額
  *
  * @param lineAmt 核准金額
	*/
  public void setLineAmt(BigDecimal lineAmt) {
    this.lineAmt = lineAmt;
  }

/**
	* 帳管費<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getAcctFee() {
    return this.acctFee;
  }

/**
	* 帳管費<br>
	* 
  *
  * @param acctFee 帳管費
	*/
  public void setAcctFee(BigDecimal acctFee) {
    this.acctFee = acctFee;
  }

/**
	* 法拍及火險費用<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getFee() {
    return this.fee;
  }

/**
	* 法拍及火險費用<br>
	* 
  *
  * @param fee 法拍及火險費用
	*/
  public void setFee(BigDecimal fee) {
    this.fee = fee;
  }

/**
	* 核准利率<br>
	* 至小數點後第6位。例如，利率為2.12%，則本欄位值表示0.021200。契約是階梯式...etc,抓取第一年的合約利率(不管加碼利率)(ex:第一年1.4%，第二年1.5%，則本欄位填入1.4%)
	* @return BigDecimal
	*/
  public BigDecimal getApproveRate() {
    return this.approveRate;
  }

/**
	* 核准利率<br>
	* 至小數點後第6位。例如，利率為2.12%，則本欄位值表示0.021200。契約是階梯式...etc,抓取第一年的合約利率(不管加碼利率)(ex:第一年1.4%，第二年1.5%，則本欄位填入1.4%)
  *
  * @param approveRate 核准利率
	*/
  public void setApproveRate(BigDecimal approveRate) {
    this.approveRate = approveRate;
  }

/**
	* 初貸時約定還本寬限期<br>
	* 約定客戶得只繳息不繳本之寬限期。
以月為單位，例如3年寬限期，則本欄位值為36
	* @return Integer
	*/
  public int getGracePeriod() {
    return this.gracePeriod;
  }

/**
	* 初貸時約定還本寬限期<br>
	* 約定客戶得只繳息不繳本之寬限期。
以月為單位，例如3年寬限期，則本欄位值為36
  *
  * @param gracePeriod 初貸時約定還本寬限期
	*/
  public void setGracePeriod(int gracePeriod) {
    this.gracePeriod = gracePeriod;
  }

/**
	* 契約當時還款方式<br>
	* 會計日當時契約當時還款方式
1=按期繳息(到期還本)；
2=平均攤還本息；
3=平均攤還本金；
4=到期繳息還本
	* @return String
	*/
  public String getAmortizedCode() {
    return this.amortizedCode == null ? "" : this.amortizedCode;
  }

/**
	* 契約當時還款方式<br>
	* 會計日當時契約當時還款方式
1=按期繳息(到期還本)；
2=平均攤還本息；
3=平均攤還本金；
4=到期繳息還本
  *
  * @param amortizedCode 契約當時還款方式
	*/
  public void setAmortizedCode(String amortizedCode) {
    this.amortizedCode = amortizedCode;
  }

/**
	* 契約當時利率調整方式<br>
	* 會計日當時契約當時利率調整方式
1=機動；
2=固定；
3=固定階梯；
4=浮動階梯
	* @return String
	*/
  public String getRateCode() {
    return this.rateCode == null ? "" : this.rateCode;
  }

/**
	* 契約當時利率調整方式<br>
	* 會計日當時契約當時利率調整方式
1=機動；
2=固定；
3=固定階梯；
4=浮動階梯
  *
  * @param rateCode 契約當時利率調整方式
	*/
  public void setRateCode(String rateCode) {
    this.rateCode = rateCode;
  }

/**
	* 契約約定當時還本週期<br>
	* 若為到期還本，則填入0；
若按月還本，則填入1；
季繳，3；
半年，6；
年繳，12
	* @return Integer
	*/
  public int getRepayFreq() {
    return this.repayFreq;
  }

/**
	* 契約約定當時還本週期<br>
	* 若為到期還本，則填入0；
若按月還本，則填入1；
季繳，3；
半年，6；
年繳，12
  *
  * @param repayFreq 契約約定當時還本週期
	*/
  public void setRepayFreq(int repayFreq) {
    this.repayFreq = repayFreq;
  }

/**
	* 契約約定當時繳息週期<br>
	* 若為到期繳息，則填入0；
若按月還本，則填入1；
季繳，3；
半年，6；
年繳，12
	* @return Integer
	*/
  public int getPayIntFreq() {
    return this.payIntFreq;
  }

/**
	* 契約約定當時繳息週期<br>
	* 若為到期繳息，則填入0；
若按月還本，則填入1；
季繳，3；
半年，6；
年繳，12
  *
  * @param payIntFreq 契約約定當時繳息週期
	*/
  public void setPayIntFreq(int payIntFreq) {
    this.payIntFreq = payIntFreq;
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
  public String getCityCode() {
    return this.cityCode == null ? "" : this.cityCode;
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
  * @param cityCode 擔保品地區別
	*/
  public void setCityCode(String cityCode) {
    this.cityCode = cityCode;
  }

/**
	* 商品利率代碼<br>
	* 
	* @return String
	*/
  public String getProdNo() {
    return this.prodNo == null ? "" : this.prodNo;
  }

/**
	* 商品利率代碼<br>
	* 
  *
  * @param prodNo 商品利率代碼
	*/
  public void setProdNo(String prodNo) {
    this.prodNo = prodNo;
  }

/**
	* 企業戶/個人戶<br>
	* 1=企業戶
2=個人戶
自然人採用企金自然人評等模型者，應歸類為企業戶
	* @return Integer
	*/
  public int getCustKind() {
    return this.custKind;
  }

/**
	* 企業戶/個人戶<br>
	* 1=企業戶
2=個人戶
自然人採用企金自然人評等模型者，應歸類為企業戶
  *
  * @param custKind 企業戶/個人戶
	*/
  public void setCustKind(int custKind) {
    this.custKind = custKind;
  }

/**
	* 產品別<br>
	* 作為群組分類。Ex:1=員工；2=車貸；3＝房貸；4＝政府優惠貸款…etc
	* @return String
	*/
  public String getIfrs9ProdCode() {
    return this.ifrs9ProdCode == null ? "" : this.ifrs9ProdCode;
  }

/**
	* 產品別<br>
	* 作為群組分類。Ex:1=員工；2=車貸；3＝房貸；4＝政府優惠貸款…etc
  *
  * @param ifrs9ProdCode 產品別
	*/
  public void setIfrs9ProdCode(String ifrs9ProdCode) {
    this.ifrs9ProdCode = ifrs9ProdCode;
  }

/**
	* 原始鑑價金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getEvaAmt() {
    return this.evaAmt;
  }

/**
	* 原始鑑價金額<br>
	* 
  *
  * @param evaAmt 原始鑑價金額
	*/
  public void setEvaAmt(BigDecimal evaAmt) {
    this.evaAmt = evaAmt;
  }

/**
	* 可動用餘額(台幣)<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getAvblBal() {
    return this.avblBal;
  }

/**
	* 可動用餘額(台幣)<br>
	* 
  *
  * @param avblBal 可動用餘額(台幣)
	*/
  public void setAvblBal(BigDecimal avblBal) {
    this.avblBal = avblBal;
  }

/**
	* 該筆額度是否可循環動用<br>
	* 0: 非循環動用  1: 循環動用
若註記為1，且本金餘額＜核准金額，但是可動用餘額=0；需確認是否實為循環額度
	* @return String
	*/
  public String getRecycleCode() {
    return this.recycleCode == null ? "" : this.recycleCode;
  }

/**
	* 該筆額度是否可循環動用<br>
	* 0: 非循環動用  1: 循環動用
若註記為1，且本金餘額＜核准金額，但是可動用餘額=0；需確認是否實為循環額度
  *
  * @param recycleCode 該筆額度是否可循環動用
	*/
  public void setRecycleCode(String recycleCode) {
    this.recycleCode = recycleCode;
  }

/**
	* 該筆額度是否為不可撤銷<br>
	* 1=是 0=否
	* @return String
	*/
  public String getIrrevocableFlag() {
    return this.irrevocableFlag == null ? "" : this.irrevocableFlag;
  }

/**
	* 該筆額度是否為不可撤銷<br>
	* 1=是 0=否
  *
  * @param irrevocableFlag 該筆額度是否為不可撤銷
	*/
  public void setIrrevocableFlag(String irrevocableFlag) {
    this.irrevocableFlag = irrevocableFlag;
  }

/**
	* 合約期限<br>
	* [額度首撥日]~[到期日]轉成幾年幾月幾天
	* @return String
	*/
  public String getLoanTerm() {
    return this.loanTerm == null ? "" : this.loanTerm;
  }

/**
	* 合約期限<br>
	* [額度首撥日]~[到期日]轉成幾年幾月幾天
  *
  * @param loanTerm 合約期限
	*/
  public void setLoanTerm(String loanTerm) {
    this.loanTerm = loanTerm;
  }

/**
	* 備忘分錄會計科目<br>
	* 
	* @return String
	*/
  public String getAcCode() {
    return this.acCode == null ? "" : this.acCode;
  }

/**
	* 備忘分錄會計科目<br>
	* 
  *
  * @param acCode 備忘分錄會計科目
	*/
  public void setAcCode(String acCode) {
    this.acCode = acCode;
  }

/**
	* 記帳幣別<br>
	* 1=台幣 2=美元 3=澳幣 4=人民幣 5=歐元
	* @return Integer
	*/
  public int getAcCurcd() {
    return this.acCurcd;
  }

/**
	* 記帳幣別<br>
	* 1=台幣 2=美元 3=澳幣 4=人民幣 5=歐元
  *
  * @param acCurcd 記帳幣別
	*/
  public void setAcCurcd(int acCurcd) {
    this.acCurcd = acCurcd;
  }

/**
	* 會計帳冊<br>
	* 1=一般 2=分紅 3=利變 4=OIU
	* @return String
	*/
  public String getAcBookCode() {
    return this.acBookCode == null ? "" : this.acBookCode;
  }

/**
	* 會計帳冊<br>
	* 1=一般 2=分紅 3=利變 4=OIU
  *
  * @param acBookCode 會計帳冊
	*/
  public void setAcBookCode(String acBookCode) {
    this.acBookCode = acBookCode;
  }

/**
	* 交易幣別<br>
	* NTD
	* @return String
	*/
  public String getCurrencyCode() {
    return this.currencyCode == null ? "" : this.currencyCode;
  }

/**
	* 交易幣別<br>
	* NTD
  *
  * @param currencyCode 交易幣別
	*/
  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
  }

/**
	* 報導日匯率<br>
	* 1
	* @return BigDecimal
	*/
  public BigDecimal getExchangeRate() {
    return this.exchangeRate;
  }

/**
	* 報導日匯率<br>
	* 1
  *
  * @param exchangeRate 報導日匯率
	*/
  public void setExchangeRate(BigDecimal exchangeRate) {
    this.exchangeRate = exchangeRate;
  }

/**
	* 核准金額(交易幣)<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLineAmtCurr() {
    return this.lineAmtCurr;
  }

/**
	* 核准金額(交易幣)<br>
	* 
  *
  * @param lineAmtCurr 核准金額(交易幣)
	*/
  public void setLineAmtCurr(BigDecimal lineAmtCurr) {
    this.lineAmtCurr = lineAmtCurr;
  }

/**
	* 帳管費(交易幣)<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getAcctFeeCurr() {
    return this.acctFeeCurr;
  }

/**
	* 帳管費(交易幣)<br>
	* 
  *
  * @param acctFeeCurr 帳管費(交易幣)
	*/
  public void setAcctFeeCurr(BigDecimal acctFeeCurr) {
    this.acctFeeCurr = acctFeeCurr;
  }

/**
	* 法拍及火險費用(交易幣)<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getFeeCurr() {
    return this.feeCurr;
  }

/**
	* 法拍及火險費用(交易幣)<br>
	* 
  *
  * @param feeCurr 法拍及火險費用(交易幣)
	*/
  public void setFeeCurr(BigDecimal feeCurr) {
    this.feeCurr = feeCurr;
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
    return "LoanIfrs9Ip [loanIfrs9IpId=" + loanIfrs9IpId + ", custId=" + custId + ", applNo=" + applNo + ", drawdownFg=" + drawdownFg
           + ", approveDate=" + approveDate + ", firstDrawdownDate=" + firstDrawdownDate + ", lineAmt=" + lineAmt + ", acctFee=" + acctFee + ", fee=" + fee + ", approveRate=" + approveRate
           + ", gracePeriod=" + gracePeriod + ", amortizedCode=" + amortizedCode + ", rateCode=" + rateCode + ", repayFreq=" + repayFreq + ", payIntFreq=" + payIntFreq + ", industryCode=" + industryCode
           + ", clTypeJCIC=" + clTypeJCIC + ", cityCode=" + cityCode + ", prodNo=" + prodNo + ", custKind=" + custKind + ", ifrs9ProdCode=" + ifrs9ProdCode + ", evaAmt=" + evaAmt
           + ", avblBal=" + avblBal + ", recycleCode=" + recycleCode + ", irrevocableFlag=" + irrevocableFlag + ", loanTerm=" + loanTerm + ", acCode=" + acCode + ", acCurcd=" + acCurcd
           + ", acBookCode=" + acBookCode + ", currencyCode=" + currencyCode + ", exchangeRate=" + exchangeRate + ", lineAmtCurr=" + lineAmtCurr + ", acctFeeCurr=" + acctFeeCurr + ", feeCurr=" + feeCurr
           + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
