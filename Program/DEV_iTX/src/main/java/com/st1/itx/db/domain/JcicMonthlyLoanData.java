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
 * JcicMonthlyLoanData 聯徵放款月報資料檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicMonthlyLoanData`")
public class JcicMonthlyLoanData implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 7577121629720956778L;

@EmbeddedId
  private JcicMonthlyLoanDataId jcicMonthlyLoanDataId;

  // 資料年月
  @Column(name = "`DataYM`", insertable = false, updatable = false)
  private int dataYM = 0;

  // 戶號
  @Column(name = "`CustNo`", insertable = false, updatable = false)
  private int custNo = 0;

  // 額度編號
  @Column(name = "`FacmNo`", insertable = false, updatable = false)
  private int facmNo = 0;

  // 撥款序號
  @Column(name = "`BormNo`", insertable = false, updatable = false)
  private int bormNo = 0;

  // 借款人ID / 統編
  @Column(name = "`CustId`", length = 10)
  private String custId;

  // 戶況
  /* 0: 正常戶1:展期2: 催收戶3: 結案戶4: 逾期戶5: 催收結案戶6: 呆帳戶7: 部分轉呆戶8: 債權轉讓戶9: 呆帳結案戶 */
  @Column(name = "`Status`")
  private int status = 0;

  // 企金別
  /* 0:個金 1:企金 2:企金自然人 */
  @Column(name = "`EntCode`", length = 1)
  private String entCode;

  // 負責人IDN/負責之事業體BAN
  /* 企業負責人之身分證統一編號個人授信戶負責之事業體(僅限非法人組織)營利事業統一編號 */
  @Column(name = "`SuvId`", length = 10)
  private String suvId;

  // 外僑兼具中華民國國籍IDN
  /* 授信戶或授信戶負責人外僑兼具中華民國國籍，並以外橋身份編號為借款人或負責人，本欄填其十位文數字國民身份證統一編號;如無前述狀況請填空白 */
  @Column(name = "`OverseasId`", length = 10)
  private String overseasId;

  // 授信戶行業別
  @Column(name = "`IndustryCode`", length = 6)
  private String industryCode;

  // 科目別
  @Column(name = "`AcctCode`", length = 3)
  private String acctCode;

  // 科目別註記
  /* 以 S 註記為十足擔保之授信，辦理保證或承兌業務發生墊款時，以M註記為已墊款有擔保之授信，以N註記為已墊款無擔保之授信，如無前述狀況請填X;若該擔保品於當月處理完畢，則當月份本欄位。屬部分擔保及副擔保之授信，本欄位亦請田X，但仍需將該部分擔保品或副擔保品相關資料填報於第45、46、47欄位;另辦理留學生就學貸款(授信科目Z)業務時，以R註記為留學生就學貸款，以X註記為高級中等以上學校就學貸款，請參考附件三之一科目別註記代號表 */
  @Column(name = "`SubAcctCode`", length = 1)
  private String subAcctCode;

  // 轉催收款(或呆帳)前原科目別
  @Column(name = "`OrigAcctCode`", length = 3)
  private String origAcctCode;

  // (額度)貸出金額(放款餘額)
  @Column(name = "`UtilAmt`")
  private BigDecimal utilAmt = new BigDecimal("0");

  // (額度)已動用額度餘額
  /* 循環動用還款時會減少,非循環動用還款時不會減少 */
  @Column(name = "`UtilBal`")
  private BigDecimal utilBal = new BigDecimal("0");

  // 循環動用
  /* 0: 非循環動用  1: 循環動用 */
  @Column(name = "`RecycleCode`", length = 1)
  private String recycleCode;

  // 循環動用期限
  @Column(name = "`RecycleDeadline`")
  private int recycleDeadline = 0;

  // 不可撤銷
  /* Y:是  N:否 */
  @Column(name = "`IrrevocableFlag`", length = 1)
  private String irrevocableFlag;

  // 融資分類
  /* 法人特殊融資分為五類 A 專案融資 B 商品融資 C標的融資 D 收益型商用不動產融資 E 高風險商用不動產融資 K 非屬前述特殊融資之其他一般法人金融貸款個人(含非法人組織)金融貸款分類為L 購買住宅貸款(非自用) M 購買住宅貸款(自用) N 一般房屋抵押貸款(房屋修繕貸款) O 由信用卡衍生之小額信用貸款 R 由現金卡衍生之小額信用貸款 S 創業貸款 T 所營事業營運周轉金貸款 U 建築用融資貸款 V 代墊投標保證金貸款 W 農業用途貸款 X 參與都市更新更新計畫貸款 Y 企業員工認購股票(或可轉換公司債)貸款 ,Z 其他個人金融貸款 ,1 個人投資理財貸款 */
  @Column(name = "`FinCode`", length = 1)
  private String finCode;

  // 政府專業補助貸款分類
  /* 填報該筆授信屬何種政府專案補助性質之貸款：專案補助貸款大項分類為01 振興傳統產業專案貸款 02 輔導中小企業升級或協助紮根貸款 03 振興景氣分案專案貸款 04 購置自動化機器 05 行政院開發基金或中小企業發展基金 06 微型企業創業貸款 07 九二一大地震金融因應措施 08 SARS 金融因應措施 09 青年優惠房屋貸款 10 優惠購屋專案貸款 11 輔助人民自購住宅貸款(首購或非首購) 12 國民住宅貸款 13 公教人員購置住宅 14 勞工貸款(含自購住宅及房屋修繕) 15 國軍官兵貸款 17 原住民貸款 18 學生助學貸款 19 獎勵數位內容產業及文化創意產業優惠貸款 20 留學生就學貸款 21 青年創業貸款(非屬農業部分 ) 22 天然災害房屋整修及重建貸款(非屬九二一地震受災戶部分) 23 參與度是更新計畫貸款  31 農機貸款 32 輔導農糧業經營貸款 33 輔導漁業經營貸款 34提升處勤產業經營貸款 35 農民經營改善貸款 36 農業科技園區進駐業者貸款 37 山坡地保育利用貸款 38 農業綜合貸款 39 改善財物貸款 40 農會事業發展貸款 41 農業產銷班貸款 50 民營事業污染防治設備低利貸款 51 行政院醫療發展基金構建醫療機構(設備)貸款 ，52 經濟部中小企業發展基金支援辦理四項專案貸款 99 其他政府專案補助貸款，如非屬專案補助貸款之授信填XX。 */
  @Column(name = "`ProjCode`", length = 2)
  private String projCode;

  // 不計入授信項目
  @Column(name = "`NonCreditCode`", length = 1)
  private String nonCreditCode;

  // 用途別
  @Column(name = "`UsageCode`", length = 2)
  private String usageCode;

  // 本筆撥款利率
  @Column(name = "`ApproveRate`")
  private BigDecimal approveRate = new BigDecimal("0");

  // 計息利率
  @Column(name = "`StoreRate`")
  private BigDecimal storeRate = new BigDecimal("0");

  // 撥款日期
  @Column(name = "`DrawdownDate`")
  private int drawdownDate = 0;

  // 到期日
  @Column(name = "`MaturityDate`")
  private int maturityDate = 0;

  // 攤還方式
  /* 1.按月繳息(按期繳息到期還本)2.到期取息(到期繳息還本)3.本息平均法(期金)4.本金平均法 */
  @Column(name = "`AmortizedCode`", length = 1)
  private String amortizedCode;

  // 幣別
  @Column(name = "`CurrencyCode`", length = 3)
  private String currencyCode;

  // 撥款金額
  @Column(name = "`DrawdownAmt`")
  private BigDecimal drawdownAmt = new BigDecimal("0");

  // 放款餘額
  @Column(name = "`LoanBal`")
  private BigDecimal loanBal = new BigDecimal("0");

  // 本月應收本金
  @Column(name = "`PrevAmt`")
  private BigDecimal prevAmt = new BigDecimal("0");

  // 本月應收利息
  @Column(name = "`IntAmt`")
  private BigDecimal intAmt = new BigDecimal("0");

  // 本月實收本金
  @Column(name = "`PrevAmtRcv`")
  private BigDecimal prevAmtRcv = new BigDecimal("0");

  // 本月實收利息
  @Column(name = "`IntAmtRcv`")
  private BigDecimal intAmtRcv = new BigDecimal("0");

  // 本月收取費用
  @Column(name = "`FeeAmtRcv`")
  private BigDecimal feeAmtRcv = new BigDecimal("0");

  // 上次繳息日
  @Column(name = "`PrevPayIntDate`")
  private int prevPayIntDate = 0;

  // 上次還本日
  @Column(name = "`PrevRepaidDate`")
  private int prevRepaidDate = 0;

  // 下次繳息日
  @Column(name = "`NextPayIntDate`")
  private int nextPayIntDate = 0;

  // 下次還本日
  @Column(name = "`NextRepayDate`")
  private int nextRepayDate = 0;

  // 利息逾期月數
  /* 甲乙類逾期放款判斷 */
  @Column(name = "`IntDelayMon`")
  private int intDelayMon = 0;

  // 本金逾期月數
  /* 甲乙類逾期放款判斷 */
  @Column(name = "`RepayDelayMon`")
  private int repayDelayMon = 0;

  // 本金逾到期日(清償期)月數
  /* 甲乙類逾期放款判斷 */
  @Column(name = "`RepaidEndMon`")
  private int repaidEndMon = 0;

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

  // 主要擔保品類別代碼
  @Column(name = "`ClTypeCode`", length = 3)
  private String clTypeCode;

  // 擔保品組合型態
  /* 0:純信用                    1:單一擔保品(或保證)2:多種擔保品含股票          3:多種擔保品不含股票 */
  @Column(name = "`ClType`", length = 1)
  private String clType;

  // 鑑估總值
  @Column(name = "`EvaAmt`")
  private BigDecimal evaAmt = new BigDecimal("0");

  // 擔保品處分日期
  @Column(name = "`DispDate`")
  private int dispDate = 0;

  // 聯貸案序號
  @Column(name = "`SyndNo`")
  private int syndNo = 0;

  // 聯貸案類型
  /* 1:主辦行 2:參貸行 */
  @Column(name = "`SyndCode`", length = 1)
  private String syndCode;

  // 聯貸合約訂定日期
  @Column(name = "`SigningDate`")
  private int signingDate = 0;

  // 聯貸總金額
  @Column(name = "`SyndAmt`")
  private BigDecimal syndAmt = new BigDecimal("0");

  // 參貸金額
  @Column(name = "`PartAmt`")
  private BigDecimal partAmt = new BigDecimal("0");

  // 共同債務人或債務關係人身份代號1
  @Column(name = "`GuaTypeCode1`", length = 1)
  private String guaTypeCode1;

  // 共同債務人或債務關係人身份統一編號1
  @Column(name = "`GuaId1`", length = 10)
  private String guaId1;

  // 與主債務人關係1
  @Column(name = "`GuaRelCode1`", length = 2)
  private String guaRelCode1;

  // 共同債務人或債務關係人身份代號2
  @Column(name = "`GuaTypeCode2`", length = 1)
  private String guaTypeCode2;

  // 共同債務人或債務關係人身份統一編號2
  @Column(name = "`GuaId2`", length = 10)
  private String guaId2;

  // 與主債務人關係2
  @Column(name = "`GuaRelCode2`", length = 2)
  private String guaRelCode2;

  // 共同債務人或債務關係人身份代號3
  @Column(name = "`GuaTypeCode3`", length = 1)
  private String guaTypeCode3;

  // 共同債務人或債務關係人身份統一編號3
  @Column(name = "`GuaId3`", length = 10)
  private String guaId3;

  // 與主債務人關係3
  @Column(name = "`GuaRelCode3`", length = 2)
  private String guaRelCode3;

  // 共同債務人或債務關係人身份代號4
  @Column(name = "`GuaTypeCode4`", length = 1)
  private String guaTypeCode4;

  // 共同債務人或債務關係人身份統一編號4
  @Column(name = "`GuaId4`", length = 10)
  private String guaId4;

  // 與主債務人關係4
  @Column(name = "`GuaRelCode4`", length = 2)
  private String guaRelCode4;

  // 共同債務人或債務關係人身份代號5
  @Column(name = "`GuaTypeCode5`", length = 1)
  private String guaTypeCode5;

  // 共同債務人或債務關係人身份統一編號5
  @Column(name = "`GuaId5`", length = 10)
  private String guaId5;

  // 與主債務人關係5
  @Column(name = "`GuaRelCode5`", length = 2)
  private String guaRelCode5;

  // 共同債務人或債務關係人身份代號6
  @Column(name = "`GuaTypeCode6`", length = 1)
  private String guaTypeCode6;

  // 共同債務人或債務關係人身份統一編號6
  @Column(name = "`GuaId6`", length = 10)
  private String guaId6;

  // 與主債務人關係6
  @Column(name = "`GuaRelCode6`", length = 2)
  private String guaRelCode6;

  // 共同債務人或債務關係人身份代號7
  @Column(name = "`GuaTypeCode7`", length = 1)
  private String guaTypeCode7;

  // 共同債務人或債務關係人身份統一編號7
  @Column(name = "`GuaId7`", length = 10)
  private String guaId7;

  // 與主債務人關係7
  @Column(name = "`GuaRelCode7`", length = 2)
  private String guaRelCode7;

  // 共同債務人或債務關係人身份代號8
  @Column(name = "`GuaTypeCode8`", length = 1)
  private String guaTypeCode8;

  // 共同債務人或債務關係人身份統一編號8
  @Column(name = "`GuaId8`", length = 10)
  private String guaId8;

  // 與主債務人關係8
  @Column(name = "`GuaRelCode8`", length = 2)
  private String guaRelCode8;

  // 共同債務人或債務關係人身份代號9
  @Column(name = "`GuaTypeCode9`", length = 1)
  private String guaTypeCode9;

  // 共同債務人或債務關係人身份統一編號9
  @Column(name = "`GuaId9`", length = 10)
  private String guaId9;

  // 與主債務人關係9
  @Column(name = "`GuaRelCode9`", length = 2)
  private String guaRelCode9;

  // 共同債務人或債務關係人身份代號10
  @Column(name = "`GuaTypeCode10`", length = 1)
  private String guaTypeCode10;

  // 共同債務人或債務關係人身份統一編號10
  @Column(name = "`GuaId10`", length = 10)
  private String guaId10;

  // 與主債務人關係10
  @Column(name = "`GuaRelCode10`", length = 2)
  private String guaRelCode10;

  // 轉催收日期
  @Column(name = "`OvduDate`")
  private int ovduDate = 0;

  // 轉呆帳日期
  @Column(name = "`BadDebtDate`")
  private int badDebtDate = 0;

  // 不報送呆帳記號
  /* Y:不報送呆帳(聯徵授信) */
  @Column(name = "`BadDebtSkipFg`", length = 1)
  private String badDebtSkipFg;

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


  public JcicMonthlyLoanDataId getJcicMonthlyLoanDataId() {
    return this.jcicMonthlyLoanDataId;
  }

  public void setJcicMonthlyLoanDataId(JcicMonthlyLoanDataId jcicMonthlyLoanDataId) {
    this.jcicMonthlyLoanDataId = jcicMonthlyLoanDataId;
  }

/**
	* 資料年月<br>
	* 
	* @return Integer
	*/
  public int getDataYM() {
    return this.dataYM;
  }

/**
	* 資料年月<br>
	* 
  *
  * @param dataYM 資料年月
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
	* 戶況<br>
	* 0: 正常戶1:展期2: 催收戶3: 結案戶4: 逾期戶5: 催收結案戶6: 呆帳戶7: 部分轉呆戶8: 債權轉讓戶9: 呆帳結案戶
	* @return Integer
	*/
  public int getStatus() {
    return this.status;
  }

/**
	* 戶況<br>
	* 0: 正常戶1:展期2: 催收戶3: 結案戶4: 逾期戶5: 催收結案戶6: 呆帳戶7: 部分轉呆戶8: 債權轉讓戶9: 呆帳結案戶
  *
  * @param status 戶況
	*/
  public void setStatus(int status) {
    this.status = status;
  }

/**
	* 企金別<br>
	* 0:個金 1:企金 2:企金自然人
	* @return String
	*/
  public String getEntCode() {
    return this.entCode == null ? "" : this.entCode;
  }

/**
	* 企金別<br>
	* 0:個金 1:企金 2:企金自然人
  *
  * @param entCode 企金別
	*/
  public void setEntCode(String entCode) {
    this.entCode = entCode;
  }

/**
	* 負責人IDN/負責之事業體BAN<br>
	* 企業負責人之身分證統一編號
個人授信戶負責之事業體(僅限非法人組織)營利事業統一編號
	* @return String
	*/
  public String getSuvId() {
    return this.suvId == null ? "" : this.suvId;
  }

/**
	* 負責人IDN/負責之事業體BAN<br>
	* 企業負責人之身分證統一編號
個人授信戶負責之事業體(僅限非法人組織)營利事業統一編號
  *
  * @param suvId 負責人IDN/負責之事業體BAN
	*/
  public void setSuvId(String suvId) {
    this.suvId = suvId;
  }

/**
	* 外僑兼具中華民國國籍IDN<br>
	* 授信戶或授信戶負責人外僑兼具中華民國國籍，並以外橋身份編號為借款人或負責人，本欄填其十位文數字國民身份證統一編號;如無前述狀況請填空白
	* @return String
	*/
  public String getOverseasId() {
    return this.overseasId == null ? "" : this.overseasId;
  }

/**
	* 外僑兼具中華民國國籍IDN<br>
	* 授信戶或授信戶負責人外僑兼具中華民國國籍，並以外橋身份編號為借款人或負責人，本欄填其十位文數字國民身份證統一編號;如無前述狀況請填空白
  *
  * @param overseasId 外僑兼具中華民國國籍IDN
	*/
  public void setOverseasId(String overseasId) {
    this.overseasId = overseasId;
  }

/**
	* 授信戶行業別<br>
	* 
	* @return String
	*/
  public String getIndustryCode() {
    return this.industryCode == null ? "" : this.industryCode;
  }

/**
	* 授信戶行業別<br>
	* 
  *
  * @param industryCode 授信戶行業別
	*/
  public void setIndustryCode(String industryCode) {
    this.industryCode = industryCode;
  }

/**
	* 科目別<br>
	* 
	* @return String
	*/
  public String getAcctCode() {
    return this.acctCode == null ? "" : this.acctCode;
  }

/**
	* 科目別<br>
	* 
  *
  * @param acctCode 科目別
	*/
  public void setAcctCode(String acctCode) {
    this.acctCode = acctCode;
  }

/**
	* 科目別註記<br>
	* 以 S 註記為十足擔保之授信，辦理保證或承兌業務發生墊款時，以M註記為已墊款有擔保之授信，以N註記為已墊款無擔保之授信，如無前述狀況請填X;若該擔保品於當月處理完畢，則當月份本欄位。屬部分擔保及副擔保之授信，本欄位亦請田X，但仍需將該部分擔保品或副擔保品相關資料填報於第45、46、47欄位;另辦理留學生就學貸款(授信科目Z)業務時，以R註記為留學生就學貸款，以X註記為高級中等以上學校就學貸款，請參考附件三之一科目別註記代號表
	* @return String
	*/
  public String getSubAcctCode() {
    return this.subAcctCode == null ? "" : this.subAcctCode;
  }

/**
	* 科目別註記<br>
	* 以 S 註記為十足擔保之授信，辦理保證或承兌業務發生墊款時，以M註記為已墊款有擔保之授信，以N註記為已墊款無擔保之授信，如無前述狀況請填X;若該擔保品於當月處理完畢，則當月份本欄位。屬部分擔保及副擔保之授信，本欄位亦請田X，但仍需將該部分擔保品或副擔保品相關資料填報於第45、46、47欄位;另辦理留學生就學貸款(授信科目Z)業務時，以R註記為留學生就學貸款，以X註記為高級中等以上學校就學貸款，請參考附件三之一科目別註記代號表
  *
  * @param subAcctCode 科目別註記
	*/
  public void setSubAcctCode(String subAcctCode) {
    this.subAcctCode = subAcctCode;
  }

/**
	* 轉催收款(或呆帳)前原科目別<br>
	* 
	* @return String
	*/
  public String getOrigAcctCode() {
    return this.origAcctCode == null ? "" : this.origAcctCode;
  }

/**
	* 轉催收款(或呆帳)前原科目別<br>
	* 
  *
  * @param origAcctCode 轉催收款(或呆帳)前原科目別
	*/
  public void setOrigAcctCode(String origAcctCode) {
    this.origAcctCode = origAcctCode;
  }

/**
	* (額度)貸出金額(放款餘額)<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getUtilAmt() {
    return this.utilAmt;
  }

/**
	* (額度)貸出金額(放款餘額)<br>
	* 
  *
  * @param utilAmt (額度)貸出金額(放款餘額)
	*/
  public void setUtilAmt(BigDecimal utilAmt) {
    this.utilAmt = utilAmt;
  }

/**
	* (額度)已動用額度餘額<br>
	* 循環動用還款時會減少,非循環動用還款時不會減少
	* @return BigDecimal
	*/
  public BigDecimal getUtilBal() {
    return this.utilBal;
  }

/**
	* (額度)已動用額度餘額<br>
	* 循環動用還款時會減少,非循環動用還款時不會減少
  *
  * @param utilBal (額度)已動用額度餘額
	*/
  public void setUtilBal(BigDecimal utilBal) {
    this.utilBal = utilBal;
  }

/**
	* 循環動用<br>
	* 0: 非循環動用  1: 循環動用
	* @return String
	*/
  public String getRecycleCode() {
    return this.recycleCode == null ? "" : this.recycleCode;
  }

/**
	* 循環動用<br>
	* 0: 非循環動用  1: 循環動用
  *
  * @param recycleCode 循環動用
	*/
  public void setRecycleCode(String recycleCode) {
    this.recycleCode = recycleCode;
  }

/**
	* 循環動用期限<br>
	* 
	* @return Integer
	*/
  public int getRecycleDeadline() {
    return StaticTool.bcToRoc(this.recycleDeadline);
  }

/**
	* 循環動用期限<br>
	* 
  *
  * @param recycleDeadline 循環動用期限
  * @throws LogicException when Date Is Warn	*/
  public void setRecycleDeadline(int recycleDeadline) throws LogicException {
    this.recycleDeadline = StaticTool.rocToBc(recycleDeadline);
  }

/**
	* 不可撤銷<br>
	* Y:是  N:否
	* @return String
	*/
  public String getIrrevocableFlag() {
    return this.irrevocableFlag == null ? "" : this.irrevocableFlag;
  }

/**
	* 不可撤銷<br>
	* Y:是  N:否
  *
  * @param irrevocableFlag 不可撤銷
	*/
  public void setIrrevocableFlag(String irrevocableFlag) {
    this.irrevocableFlag = irrevocableFlag;
  }

/**
	* 融資分類<br>
	* 法人特殊融資分為五類 A 專案融資 B 商品融資 C標的融資 D 收益型商用不動產融資 E 高風險商用不動產融資 K 非屬前述特殊融資之其他一般法人金融貸款

個人(含非法人組織)金融貸款分類為L 購買住宅貸款(非自用) M 購買住宅貸款(自用) N 一般房屋抵押貸款(房屋修繕貸款) O 由信用卡衍生之小額信用貸款 R 由現金卡衍生之小額信用貸款 S 創業貸款 T 所營事業營運周轉金貸款 U 建築用融資貸款 V 代墊投標保證金貸款 W 農業用途貸款 X 參與都市更新更新計畫貸款 Y 企業員工認購股票(或可轉換公司債)貸款 ,Z 其他個人金融貸款 ,1 個人投資理財貸款
	* @return String
	*/
  public String getFinCode() {
    return this.finCode == null ? "" : this.finCode;
  }

/**
	* 融資分類<br>
	* 法人特殊融資分為五類 A 專案融資 B 商品融資 C標的融資 D 收益型商用不動產融資 E 高風險商用不動產融資 K 非屬前述特殊融資之其他一般法人金融貸款

個人(含非法人組織)金融貸款分類為L 購買住宅貸款(非自用) M 購買住宅貸款(自用) N 一般房屋抵押貸款(房屋修繕貸款) O 由信用卡衍生之小額信用貸款 R 由現金卡衍生之小額信用貸款 S 創業貸款 T 所營事業營運周轉金貸款 U 建築用融資貸款 V 代墊投標保證金貸款 W 農業用途貸款 X 參與都市更新更新計畫貸款 Y 企業員工認購股票(或可轉換公司債)貸款 ,Z 其他個人金融貸款 ,1 個人投資理財貸款
  *
  * @param finCode 融資分類
	*/
  public void setFinCode(String finCode) {
    this.finCode = finCode;
  }

/**
	* 政府專業補助貸款分類<br>
	* 填報該筆授信屬何種政府專案補助性質之貸款：專案補助貸款大項分類為01 振興傳統產業專案貸款 02 輔導中小企業升級或協助紮根貸款 03 振興景氣分案專案貸款 04 購置自動化機器 05 行政院開發基金或中小企業發展基金 06 微型企業創業貸款 07 九二一大地震金融因應措施 08 SARS 金融因應措施 09 青年優惠房屋貸款 10 優惠購屋專案貸款 11 輔助人民自購住宅貸款(首購或非首購) 12 國民住宅貸款 13 公教人員購置住宅 14 勞工貸款(含自購住宅及房屋修繕) 15 國軍官兵貸款 17 原住民貸款 18 學生助學貸款 19 獎勵數位內容產業及文化創意產業優惠貸款 20 留學生就學貸款 21 青年創業貸款(非屬農業部分 ) 22 天然災害房屋整修及重建貸款(非屬九二一地震受災戶部分) 23 參與度是更新計畫貸款  31 農機貸款 32 輔導農糧業經營貸款 33 輔導漁業經營貸款 34提升處勤產業經營貸款 35 農民經營改善貸款 36 農業科技園區進駐業者貸款 37 山坡地保育利用貸款 38 農業綜合貸款 39 改善財物貸款 40 農會事業發展貸款 41 農業產銷班貸款 50 民營事業污染防治設備低利貸款 51 行政院醫療發展基金構建醫療機構(設備)貸款 ，52 經濟部中小企業發展基金支援辦理四項專案貸款 99 其他政府專案補助貸款，如非屬專案補助貸款之授信填XX。
	* @return String
	*/
  public String getProjCode() {
    return this.projCode == null ? "" : this.projCode;
  }

/**
	* 政府專業補助貸款分類<br>
	* 填報該筆授信屬何種政府專案補助性質之貸款：專案補助貸款大項分類為01 振興傳統產業專案貸款 02 輔導中小企業升級或協助紮根貸款 03 振興景氣分案專案貸款 04 購置自動化機器 05 行政院開發基金或中小企業發展基金 06 微型企業創業貸款 07 九二一大地震金融因應措施 08 SARS 金融因應措施 09 青年優惠房屋貸款 10 優惠購屋專案貸款 11 輔助人民自購住宅貸款(首購或非首購) 12 國民住宅貸款 13 公教人員購置住宅 14 勞工貸款(含自購住宅及房屋修繕) 15 國軍官兵貸款 17 原住民貸款 18 學生助學貸款 19 獎勵數位內容產業及文化創意產業優惠貸款 20 留學生就學貸款 21 青年創業貸款(非屬農業部分 ) 22 天然災害房屋整修及重建貸款(非屬九二一地震受災戶部分) 23 參與度是更新計畫貸款  31 農機貸款 32 輔導農糧業經營貸款 33 輔導漁業經營貸款 34提升處勤產業經營貸款 35 農民經營改善貸款 36 農業科技園區進駐業者貸款 37 山坡地保育利用貸款 38 農業綜合貸款 39 改善財物貸款 40 農會事業發展貸款 41 農業產銷班貸款 50 民營事業污染防治設備低利貸款 51 行政院醫療發展基金構建醫療機構(設備)貸款 ，52 經濟部中小企業發展基金支援辦理四項專案貸款 99 其他政府專案補助貸款，如非屬專案補助貸款之授信填XX。
  *
  * @param projCode 政府專業補助貸款分類
	*/
  public void setProjCode(String projCode) {
    this.projCode = projCode;
  }

/**
	* 不計入授信項目<br>
	* 
	* @return String
	*/
  public String getNonCreditCode() {
    return this.nonCreditCode == null ? "" : this.nonCreditCode;
  }

/**
	* 不計入授信項目<br>
	* 
  *
  * @param nonCreditCode 不計入授信項目
	*/
  public void setNonCreditCode(String nonCreditCode) {
    this.nonCreditCode = nonCreditCode;
  }

/**
	* 用途別<br>
	* 
	* @return String
	*/
  public String getUsageCode() {
    return this.usageCode == null ? "" : this.usageCode;
  }

/**
	* 用途別<br>
	* 
  *
  * @param usageCode 用途別
	*/
  public void setUsageCode(String usageCode) {
    this.usageCode = usageCode;
  }

/**
	* 本筆撥款利率<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getApproveRate() {
    return this.approveRate;
  }

/**
	* 本筆撥款利率<br>
	* 
  *
  * @param approveRate 本筆撥款利率
	*/
  public void setApproveRate(BigDecimal approveRate) {
    this.approveRate = approveRate;
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
	* 撥款日期<br>
	* 
	* @return Integer
	*/
  public int getDrawdownDate() {
    return StaticTool.bcToRoc(this.drawdownDate);
  }

/**
	* 撥款日期<br>
	* 
  *
  * @param drawdownDate 撥款日期
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
	* 攤還方式<br>
	* 1.按月繳息(按期繳息到期還本)2.到期取息(到期繳息還本)3.本息平均法(期金)4.本金平均法
	* @return String
	*/
  public String getAmortizedCode() {
    return this.amortizedCode == null ? "" : this.amortizedCode;
  }

/**
	* 攤還方式<br>
	* 1.按月繳息(按期繳息到期還本)2.到期取息(到期繳息還本)3.本息平均法(期金)4.本金平均法
  *
  * @param amortizedCode 攤還方式
	*/
  public void setAmortizedCode(String amortizedCode) {
    this.amortizedCode = amortizedCode;
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
	* 撥款金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getDrawdownAmt() {
    return this.drawdownAmt;
  }

/**
	* 撥款金額<br>
	* 
  *
  * @param drawdownAmt 撥款金額
	*/
  public void setDrawdownAmt(BigDecimal drawdownAmt) {
    this.drawdownAmt = drawdownAmt;
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
	* 本月應收本金<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getPrevAmt() {
    return this.prevAmt;
  }

/**
	* 本月應收本金<br>
	* 
  *
  * @param prevAmt 本月應收本金
	*/
  public void setPrevAmt(BigDecimal prevAmt) {
    this.prevAmt = prevAmt;
  }

/**
	* 本月應收利息<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getIntAmt() {
    return this.intAmt;
  }

/**
	* 本月應收利息<br>
	* 
  *
  * @param intAmt 本月應收利息
	*/
  public void setIntAmt(BigDecimal intAmt) {
    this.intAmt = intAmt;
  }

/**
	* 本月實收本金<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getPrevAmtRcv() {
    return this.prevAmtRcv;
  }

/**
	* 本月實收本金<br>
	* 
  *
  * @param prevAmtRcv 本月實收本金
	*/
  public void setPrevAmtRcv(BigDecimal prevAmtRcv) {
    this.prevAmtRcv = prevAmtRcv;
  }

/**
	* 本月實收利息<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getIntAmtRcv() {
    return this.intAmtRcv;
  }

/**
	* 本月實收利息<br>
	* 
  *
  * @param intAmtRcv 本月實收利息
	*/
  public void setIntAmtRcv(BigDecimal intAmtRcv) {
    this.intAmtRcv = intAmtRcv;
  }

/**
	* 本月收取費用<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getFeeAmtRcv() {
    return this.feeAmtRcv;
  }

/**
	* 本月收取費用<br>
	* 
  *
  * @param feeAmtRcv 本月收取費用
	*/
  public void setFeeAmtRcv(BigDecimal feeAmtRcv) {
    this.feeAmtRcv = feeAmtRcv;
  }

/**
	* 上次繳息日<br>
	* 
	* @return Integer
	*/
  public int getPrevPayIntDate() {
    return StaticTool.bcToRoc(this.prevPayIntDate);
  }

/**
	* 上次繳息日<br>
	* 
  *
  * @param prevPayIntDate 上次繳息日
  * @throws LogicException when Date Is Warn	*/
  public void setPrevPayIntDate(int prevPayIntDate) throws LogicException {
    this.prevPayIntDate = StaticTool.rocToBc(prevPayIntDate);
  }

/**
	* 上次還本日<br>
	* 
	* @return Integer
	*/
  public int getPrevRepaidDate() {
    return StaticTool.bcToRoc(this.prevRepaidDate);
  }

/**
	* 上次還本日<br>
	* 
  *
  * @param prevRepaidDate 上次還本日
  * @throws LogicException when Date Is Warn	*/
  public void setPrevRepaidDate(int prevRepaidDate) throws LogicException {
    this.prevRepaidDate = StaticTool.rocToBc(prevRepaidDate);
  }

/**
	* 下次繳息日<br>
	* 
	* @return Integer
	*/
  public int getNextPayIntDate() {
    return StaticTool.bcToRoc(this.nextPayIntDate);
  }

/**
	* 下次繳息日<br>
	* 
  *
  * @param nextPayIntDate 下次繳息日
  * @throws LogicException when Date Is Warn	*/
  public void setNextPayIntDate(int nextPayIntDate) throws LogicException {
    this.nextPayIntDate = StaticTool.rocToBc(nextPayIntDate);
  }

/**
	* 下次還本日<br>
	* 
	* @return Integer
	*/
  public int getNextRepayDate() {
    return StaticTool.bcToRoc(this.nextRepayDate);
  }

/**
	* 下次還本日<br>
	* 
  *
  * @param nextRepayDate 下次還本日
  * @throws LogicException when Date Is Warn	*/
  public void setNextRepayDate(int nextRepayDate) throws LogicException {
    this.nextRepayDate = StaticTool.rocToBc(nextRepayDate);
  }

/**
	* 利息逾期月數<br>
	* 甲乙類逾期放款判斷
	* @return Integer
	*/
  public int getIntDelayMon() {
    return this.intDelayMon;
  }

/**
	* 利息逾期月數<br>
	* 甲乙類逾期放款判斷
  *
  * @param intDelayMon 利息逾期月數
	*/
  public void setIntDelayMon(int intDelayMon) {
    this.intDelayMon = intDelayMon;
  }

/**
	* 本金逾期月數<br>
	* 甲乙類逾期放款判斷
	* @return Integer
	*/
  public int getRepayDelayMon() {
    return this.repayDelayMon;
  }

/**
	* 本金逾期月數<br>
	* 甲乙類逾期放款判斷
  *
  * @param repayDelayMon 本金逾期月數
	*/
  public void setRepayDelayMon(int repayDelayMon) {
    this.repayDelayMon = repayDelayMon;
  }

/**
	* 本金逾到期日(清償期)月數<br>
	* 甲乙類逾期放款判斷
	* @return Integer
	*/
  public int getRepaidEndMon() {
    return this.repaidEndMon;
  }

/**
	* 本金逾到期日(清償期)月數<br>
	* 甲乙類逾期放款判斷
  *
  * @param repaidEndMon 本金逾到期日(清償期)月數
	*/
  public void setRepaidEndMon(int repaidEndMon) {
    this.repaidEndMon = repaidEndMon;
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
	* 主要擔保品類別代碼<br>
	* 
	* @return String
	*/
  public String getClTypeCode() {
    return this.clTypeCode == null ? "" : this.clTypeCode;
  }

/**
	* 主要擔保品類別代碼<br>
	* 
  *
  * @param clTypeCode 主要擔保品類別代碼
	*/
  public void setClTypeCode(String clTypeCode) {
    this.clTypeCode = clTypeCode;
  }

/**
	* 擔保品組合型態<br>
	* 0:純信用                    1:單一擔保品(或保證)
2:多種擔保品含股票          3:多種擔保品不含股票
	* @return String
	*/
  public String getClType() {
    return this.clType == null ? "" : this.clType;
  }

/**
	* 擔保品組合型態<br>
	* 0:純信用                    1:單一擔保品(或保證)
2:多種擔保品含股票          3:多種擔保品不含股票
  *
  * @param clType 擔保品組合型態
	*/
  public void setClType(String clType) {
    this.clType = clType;
  }

/**
	* 鑑估總值<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getEvaAmt() {
    return this.evaAmt;
  }

/**
	* 鑑估總值<br>
	* 
  *
  * @param evaAmt 鑑估總值
	*/
  public void setEvaAmt(BigDecimal evaAmt) {
    this.evaAmt = evaAmt;
  }

/**
	* 擔保品處分日期<br>
	* 
	* @return Integer
	*/
  public int getDispDate() {
    return StaticTool.bcToRoc(this.dispDate);
  }

/**
	* 擔保品處分日期<br>
	* 
  *
  * @param dispDate 擔保品處分日期
  * @throws LogicException when Date Is Warn	*/
  public void setDispDate(int dispDate) throws LogicException {
    this.dispDate = StaticTool.rocToBc(dispDate);
  }

/**
	* 聯貸案序號<br>
	* 
	* @return Integer
	*/
  public int getSyndNo() {
    return this.syndNo;
  }

/**
	* 聯貸案序號<br>
	* 
  *
  * @param syndNo 聯貸案序號
	*/
  public void setSyndNo(int syndNo) {
    this.syndNo = syndNo;
  }

/**
	* 聯貸案類型<br>
	* 1:主辦行 2:參貸行
	* @return String
	*/
  public String getSyndCode() {
    return this.syndCode == null ? "" : this.syndCode;
  }

/**
	* 聯貸案類型<br>
	* 1:主辦行 2:參貸行
  *
  * @param syndCode 聯貸案類型
	*/
  public void setSyndCode(String syndCode) {
    this.syndCode = syndCode;
  }

/**
	* 聯貸合約訂定日期<br>
	* 
	* @return Integer
	*/
  public int getSigningDate() {
    return StaticTool.bcToRoc(this.signingDate);
  }

/**
	* 聯貸合約訂定日期<br>
	* 
  *
  * @param signingDate 聯貸合約訂定日期
  * @throws LogicException when Date Is Warn	*/
  public void setSigningDate(int signingDate) throws LogicException {
    this.signingDate = StaticTool.rocToBc(signingDate);
  }

/**
	* 聯貸總金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getSyndAmt() {
    return this.syndAmt;
  }

/**
	* 聯貸總金額<br>
	* 
  *
  * @param syndAmt 聯貸總金額
	*/
  public void setSyndAmt(BigDecimal syndAmt) {
    this.syndAmt = syndAmt;
  }

/**
	* 參貸金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getPartAmt() {
    return this.partAmt;
  }

/**
	* 參貸金額<br>
	* 
  *
  * @param partAmt 參貸金額
	*/
  public void setPartAmt(BigDecimal partAmt) {
    this.partAmt = partAmt;
  }

/**
	* 共同債務人或債務關係人身份代號1<br>
	* 
	* @return String
	*/
  public String getGuaTypeCode1() {
    return this.guaTypeCode1 == null ? "" : this.guaTypeCode1;
  }

/**
	* 共同債務人或債務關係人身份代號1<br>
	* 
  *
  * @param guaTypeCode1 共同債務人或債務關係人身份代號1
	*/
  public void setGuaTypeCode1(String guaTypeCode1) {
    this.guaTypeCode1 = guaTypeCode1;
  }

/**
	* 共同債務人或債務關係人身份統一編號1<br>
	* 
	* @return String
	*/
  public String getGuaId1() {
    return this.guaId1 == null ? "" : this.guaId1;
  }

/**
	* 共同債務人或債務關係人身份統一編號1<br>
	* 
  *
  * @param guaId1 共同債務人或債務關係人身份統一編號1
	*/
  public void setGuaId1(String guaId1) {
    this.guaId1 = guaId1;
  }

/**
	* 與主債務人關係1<br>
	* 
	* @return String
	*/
  public String getGuaRelCode1() {
    return this.guaRelCode1 == null ? "" : this.guaRelCode1;
  }

/**
	* 與主債務人關係1<br>
	* 
  *
  * @param guaRelCode1 與主債務人關係1
	*/
  public void setGuaRelCode1(String guaRelCode1) {
    this.guaRelCode1 = guaRelCode1;
  }

/**
	* 共同債務人或債務關係人身份代號2<br>
	* 
	* @return String
	*/
  public String getGuaTypeCode2() {
    return this.guaTypeCode2 == null ? "" : this.guaTypeCode2;
  }

/**
	* 共同債務人或債務關係人身份代號2<br>
	* 
  *
  * @param guaTypeCode2 共同債務人或債務關係人身份代號2
	*/
  public void setGuaTypeCode2(String guaTypeCode2) {
    this.guaTypeCode2 = guaTypeCode2;
  }

/**
	* 共同債務人或債務關係人身份統一編號2<br>
	* 
	* @return String
	*/
  public String getGuaId2() {
    return this.guaId2 == null ? "" : this.guaId2;
  }

/**
	* 共同債務人或債務關係人身份統一編號2<br>
	* 
  *
  * @param guaId2 共同債務人或債務關係人身份統一編號2
	*/
  public void setGuaId2(String guaId2) {
    this.guaId2 = guaId2;
  }

/**
	* 與主債務人關係2<br>
	* 
	* @return String
	*/
  public String getGuaRelCode2() {
    return this.guaRelCode2 == null ? "" : this.guaRelCode2;
  }

/**
	* 與主債務人關係2<br>
	* 
  *
  * @param guaRelCode2 與主債務人關係2
	*/
  public void setGuaRelCode2(String guaRelCode2) {
    this.guaRelCode2 = guaRelCode2;
  }

/**
	* 共同債務人或債務關係人身份代號3<br>
	* 
	* @return String
	*/
  public String getGuaTypeCode3() {
    return this.guaTypeCode3 == null ? "" : this.guaTypeCode3;
  }

/**
	* 共同債務人或債務關係人身份代號3<br>
	* 
  *
  * @param guaTypeCode3 共同債務人或債務關係人身份代號3
	*/
  public void setGuaTypeCode3(String guaTypeCode3) {
    this.guaTypeCode3 = guaTypeCode3;
  }

/**
	* 共同債務人或債務關係人身份統一編號3<br>
	* 
	* @return String
	*/
  public String getGuaId3() {
    return this.guaId3 == null ? "" : this.guaId3;
  }

/**
	* 共同債務人或債務關係人身份統一編號3<br>
	* 
  *
  * @param guaId3 共同債務人或債務關係人身份統一編號3
	*/
  public void setGuaId3(String guaId3) {
    this.guaId3 = guaId3;
  }

/**
	* 與主債務人關係3<br>
	* 
	* @return String
	*/
  public String getGuaRelCode3() {
    return this.guaRelCode3 == null ? "" : this.guaRelCode3;
  }

/**
	* 與主債務人關係3<br>
	* 
  *
  * @param guaRelCode3 與主債務人關係3
	*/
  public void setGuaRelCode3(String guaRelCode3) {
    this.guaRelCode3 = guaRelCode3;
  }

/**
	* 共同債務人或債務關係人身份代號4<br>
	* 
	* @return String
	*/
  public String getGuaTypeCode4() {
    return this.guaTypeCode4 == null ? "" : this.guaTypeCode4;
  }

/**
	* 共同債務人或債務關係人身份代號4<br>
	* 
  *
  * @param guaTypeCode4 共同債務人或債務關係人身份代號4
	*/
  public void setGuaTypeCode4(String guaTypeCode4) {
    this.guaTypeCode4 = guaTypeCode4;
  }

/**
	* 共同債務人或債務關係人身份統一編號4<br>
	* 
	* @return String
	*/
  public String getGuaId4() {
    return this.guaId4 == null ? "" : this.guaId4;
  }

/**
	* 共同債務人或債務關係人身份統一編號4<br>
	* 
  *
  * @param guaId4 共同債務人或債務關係人身份統一編號4
	*/
  public void setGuaId4(String guaId4) {
    this.guaId4 = guaId4;
  }

/**
	* 與主債務人關係4<br>
	* 
	* @return String
	*/
  public String getGuaRelCode4() {
    return this.guaRelCode4 == null ? "" : this.guaRelCode4;
  }

/**
	* 與主債務人關係4<br>
	* 
  *
  * @param guaRelCode4 與主債務人關係4
	*/
  public void setGuaRelCode4(String guaRelCode4) {
    this.guaRelCode4 = guaRelCode4;
  }

/**
	* 共同債務人或債務關係人身份代號5<br>
	* 
	* @return String
	*/
  public String getGuaTypeCode5() {
    return this.guaTypeCode5 == null ? "" : this.guaTypeCode5;
  }

/**
	* 共同債務人或債務關係人身份代號5<br>
	* 
  *
  * @param guaTypeCode5 共同債務人或債務關係人身份代號5
	*/
  public void setGuaTypeCode5(String guaTypeCode5) {
    this.guaTypeCode5 = guaTypeCode5;
  }

/**
	* 共同債務人或債務關係人身份統一編號5<br>
	* 
	* @return String
	*/
  public String getGuaId5() {
    return this.guaId5 == null ? "" : this.guaId5;
  }

/**
	* 共同債務人或債務關係人身份統一編號5<br>
	* 
  *
  * @param guaId5 共同債務人或債務關係人身份統一編號5
	*/
  public void setGuaId5(String guaId5) {
    this.guaId5 = guaId5;
  }

/**
	* 與主債務人關係5<br>
	* 
	* @return String
	*/
  public String getGuaRelCode5() {
    return this.guaRelCode5 == null ? "" : this.guaRelCode5;
  }

/**
	* 與主債務人關係5<br>
	* 
  *
  * @param guaRelCode5 與主債務人關係5
	*/
  public void setGuaRelCode5(String guaRelCode5) {
    this.guaRelCode5 = guaRelCode5;
  }

/**
	* 共同債務人或債務關係人身份代號6<br>
	* 
	* @return String
	*/
  public String getGuaTypeCode6() {
    return this.guaTypeCode6 == null ? "" : this.guaTypeCode6;
  }

/**
	* 共同債務人或債務關係人身份代號6<br>
	* 
  *
  * @param guaTypeCode6 共同債務人或債務關係人身份代號6
	*/
  public void setGuaTypeCode6(String guaTypeCode6) {
    this.guaTypeCode6 = guaTypeCode6;
  }

/**
	* 共同債務人或債務關係人身份統一編號6<br>
	* 
	* @return String
	*/
  public String getGuaId6() {
    return this.guaId6 == null ? "" : this.guaId6;
  }

/**
	* 共同債務人或債務關係人身份統一編號6<br>
	* 
  *
  * @param guaId6 共同債務人或債務關係人身份統一編號6
	*/
  public void setGuaId6(String guaId6) {
    this.guaId6 = guaId6;
  }

/**
	* 與主債務人關係6<br>
	* 
	* @return String
	*/
  public String getGuaRelCode6() {
    return this.guaRelCode6 == null ? "" : this.guaRelCode6;
  }

/**
	* 與主債務人關係6<br>
	* 
  *
  * @param guaRelCode6 與主債務人關係6
	*/
  public void setGuaRelCode6(String guaRelCode6) {
    this.guaRelCode6 = guaRelCode6;
  }

/**
	* 共同債務人或債務關係人身份代號7<br>
	* 
	* @return String
	*/
  public String getGuaTypeCode7() {
    return this.guaTypeCode7 == null ? "" : this.guaTypeCode7;
  }

/**
	* 共同債務人或債務關係人身份代號7<br>
	* 
  *
  * @param guaTypeCode7 共同債務人或債務關係人身份代號7
	*/
  public void setGuaTypeCode7(String guaTypeCode7) {
    this.guaTypeCode7 = guaTypeCode7;
  }

/**
	* 共同債務人或債務關係人身份統一編號7<br>
	* 
	* @return String
	*/
  public String getGuaId7() {
    return this.guaId7 == null ? "" : this.guaId7;
  }

/**
	* 共同債務人或債務關係人身份統一編號7<br>
	* 
  *
  * @param guaId7 共同債務人或債務關係人身份統一編號7
	*/
  public void setGuaId7(String guaId7) {
    this.guaId7 = guaId7;
  }

/**
	* 與主債務人關係7<br>
	* 
	* @return String
	*/
  public String getGuaRelCode7() {
    return this.guaRelCode7 == null ? "" : this.guaRelCode7;
  }

/**
	* 與主債務人關係7<br>
	* 
  *
  * @param guaRelCode7 與主債務人關係7
	*/
  public void setGuaRelCode7(String guaRelCode7) {
    this.guaRelCode7 = guaRelCode7;
  }

/**
	* 共同債務人或債務關係人身份代號8<br>
	* 
	* @return String
	*/
  public String getGuaTypeCode8() {
    return this.guaTypeCode8 == null ? "" : this.guaTypeCode8;
  }

/**
	* 共同債務人或債務關係人身份代號8<br>
	* 
  *
  * @param guaTypeCode8 共同債務人或債務關係人身份代號8
	*/
  public void setGuaTypeCode8(String guaTypeCode8) {
    this.guaTypeCode8 = guaTypeCode8;
  }

/**
	* 共同債務人或債務關係人身份統一編號8<br>
	* 
	* @return String
	*/
  public String getGuaId8() {
    return this.guaId8 == null ? "" : this.guaId8;
  }

/**
	* 共同債務人或債務關係人身份統一編號8<br>
	* 
  *
  * @param guaId8 共同債務人或債務關係人身份統一編號8
	*/
  public void setGuaId8(String guaId8) {
    this.guaId8 = guaId8;
  }

/**
	* 與主債務人關係8<br>
	* 
	* @return String
	*/
  public String getGuaRelCode8() {
    return this.guaRelCode8 == null ? "" : this.guaRelCode8;
  }

/**
	* 與主債務人關係8<br>
	* 
  *
  * @param guaRelCode8 與主債務人關係8
	*/
  public void setGuaRelCode8(String guaRelCode8) {
    this.guaRelCode8 = guaRelCode8;
  }

/**
	* 共同債務人或債務關係人身份代號9<br>
	* 
	* @return String
	*/
  public String getGuaTypeCode9() {
    return this.guaTypeCode9 == null ? "" : this.guaTypeCode9;
  }

/**
	* 共同債務人或債務關係人身份代號9<br>
	* 
  *
  * @param guaTypeCode9 共同債務人或債務關係人身份代號9
	*/
  public void setGuaTypeCode9(String guaTypeCode9) {
    this.guaTypeCode9 = guaTypeCode9;
  }

/**
	* 共同債務人或債務關係人身份統一編號9<br>
	* 
	* @return String
	*/
  public String getGuaId9() {
    return this.guaId9 == null ? "" : this.guaId9;
  }

/**
	* 共同債務人或債務關係人身份統一編號9<br>
	* 
  *
  * @param guaId9 共同債務人或債務關係人身份統一編號9
	*/
  public void setGuaId9(String guaId9) {
    this.guaId9 = guaId9;
  }

/**
	* 與主債務人關係9<br>
	* 
	* @return String
	*/
  public String getGuaRelCode9() {
    return this.guaRelCode9 == null ? "" : this.guaRelCode9;
  }

/**
	* 與主債務人關係9<br>
	* 
  *
  * @param guaRelCode9 與主債務人關係9
	*/
  public void setGuaRelCode9(String guaRelCode9) {
    this.guaRelCode9 = guaRelCode9;
  }

/**
	* 共同債務人或債務關係人身份代號10<br>
	* 
	* @return String
	*/
  public String getGuaTypeCode10() {
    return this.guaTypeCode10 == null ? "" : this.guaTypeCode10;
  }

/**
	* 共同債務人或債務關係人身份代號10<br>
	* 
  *
  * @param guaTypeCode10 共同債務人或債務關係人身份代號10
	*/
  public void setGuaTypeCode10(String guaTypeCode10) {
    this.guaTypeCode10 = guaTypeCode10;
  }

/**
	* 共同債務人或債務關係人身份統一編號10<br>
	* 
	* @return String
	*/
  public String getGuaId10() {
    return this.guaId10 == null ? "" : this.guaId10;
  }

/**
	* 共同債務人或債務關係人身份統一編號10<br>
	* 
  *
  * @param guaId10 共同債務人或債務關係人身份統一編號10
	*/
  public void setGuaId10(String guaId10) {
    this.guaId10 = guaId10;
  }

/**
	* 與主債務人關係10<br>
	* 
	* @return String
	*/
  public String getGuaRelCode10() {
    return this.guaRelCode10 == null ? "" : this.guaRelCode10;
  }

/**
	* 與主債務人關係10<br>
	* 
  *
  * @param guaRelCode10 與主債務人關係10
	*/
  public void setGuaRelCode10(String guaRelCode10) {
    this.guaRelCode10 = guaRelCode10;
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
	* 轉呆帳日期<br>
	* 
	* @return Integer
	*/
  public int getBadDebtDate() {
    return StaticTool.bcToRoc(this.badDebtDate);
  }

/**
	* 轉呆帳日期<br>
	* 
  *
  * @param badDebtDate 轉呆帳日期
  * @throws LogicException when Date Is Warn	*/
  public void setBadDebtDate(int badDebtDate) throws LogicException {
    this.badDebtDate = StaticTool.rocToBc(badDebtDate);
  }

/**
	* 不報送呆帳記號<br>
	* Y:不報送呆帳(聯徵授信)
	* @return String
	*/
  public String getBadDebtSkipFg() {
    return this.badDebtSkipFg == null ? "" : this.badDebtSkipFg;
  }

/**
	* 不報送呆帳記號<br>
	* Y:不報送呆帳(聯徵授信)
  *
  * @param badDebtSkipFg 不報送呆帳記號
	*/
  public void setBadDebtSkipFg(String badDebtSkipFg) {
    this.badDebtSkipFg = badDebtSkipFg;
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
    return "JcicMonthlyLoanData [jcicMonthlyLoanDataId=" + jcicMonthlyLoanDataId + ", custId=" + custId + ", status=" + status
           + ", entCode=" + entCode + ", suvId=" + suvId + ", overseasId=" + overseasId + ", industryCode=" + industryCode + ", acctCode=" + acctCode + ", subAcctCode=" + subAcctCode
           + ", origAcctCode=" + origAcctCode + ", utilAmt=" + utilAmt + ", utilBal=" + utilBal + ", recycleCode=" + recycleCode + ", recycleDeadline=" + recycleDeadline + ", irrevocableFlag=" + irrevocableFlag
           + ", finCode=" + finCode + ", projCode=" + projCode + ", nonCreditCode=" + nonCreditCode + ", usageCode=" + usageCode + ", approveRate=" + approveRate + ", storeRate=" + storeRate
           + ", drawdownDate=" + drawdownDate + ", maturityDate=" + maturityDate + ", amortizedCode=" + amortizedCode + ", currencyCode=" + currencyCode + ", drawdownAmt=" + drawdownAmt + ", loanBal=" + loanBal
           + ", prevAmt=" + prevAmt + ", intAmt=" + intAmt + ", prevAmtRcv=" + prevAmtRcv + ", intAmtRcv=" + intAmtRcv + ", feeAmtRcv=" + feeAmtRcv + ", prevPayIntDate=" + prevPayIntDate
           + ", prevRepaidDate=" + prevRepaidDate + ", nextPayIntDate=" + nextPayIntDate + ", nextRepayDate=" + nextRepayDate + ", intDelayMon=" + intDelayMon + ", repayDelayMon=" + repayDelayMon + ", repaidEndMon=" + repaidEndMon
           + ", clCode1=" + clCode1 + ", clCode2=" + clCode2 + ", clNo=" + clNo + ", clTypeCode=" + clTypeCode + ", clType=" + clType + ", evaAmt=" + evaAmt
           + ", dispDate=" + dispDate + ", syndNo=" + syndNo + ", syndCode=" + syndCode + ", signingDate=" + signingDate + ", syndAmt=" + syndAmt + ", partAmt=" + partAmt
           + ", guaTypeCode1=" + guaTypeCode1 + ", guaId1=" + guaId1 + ", guaRelCode1=" + guaRelCode1 + ", guaTypeCode2=" + guaTypeCode2 + ", guaId2=" + guaId2 + ", guaRelCode2=" + guaRelCode2
           + ", guaTypeCode3=" + guaTypeCode3 + ", guaId3=" + guaId3 + ", guaRelCode3=" + guaRelCode3 + ", guaTypeCode4=" + guaTypeCode4 + ", guaId4=" + guaId4 + ", guaRelCode4=" + guaRelCode4
           + ", guaTypeCode5=" + guaTypeCode5 + ", guaId5=" + guaId5 + ", guaRelCode5=" + guaRelCode5 + ", guaTypeCode6=" + guaTypeCode6 + ", guaId6=" + guaId6 + ", guaRelCode6=" + guaRelCode6
           + ", guaTypeCode7=" + guaTypeCode7 + ", guaId7=" + guaId7 + ", guaRelCode7=" + guaRelCode7 + ", guaTypeCode8=" + guaTypeCode8 + ", guaId8=" + guaId8 + ", guaRelCode8=" + guaRelCode8
           + ", guaTypeCode9=" + guaTypeCode9 + ", guaId9=" + guaId9 + ", guaRelCode9=" + guaRelCode9 + ", guaTypeCode10=" + guaTypeCode10 + ", guaId10=" + guaId10 + ", guaRelCode10=" + guaRelCode10
           + ", ovduDate=" + ovduDate + ", badDebtDate=" + badDebtDate + ", badDebtSkipFg=" + badDebtSkipFg + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
           + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
