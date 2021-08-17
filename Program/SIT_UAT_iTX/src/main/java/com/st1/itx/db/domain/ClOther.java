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
 * ClOther 擔保品其他檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`ClOther`")
public class ClOther implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -4342315285698689151L;

@EmbeddedId
  private ClOtherId clOtherId;

  // 擔保品代號1
  /* 擔保品代號檔CdCl */
  @Column(name = "`ClCode1`", insertable = false, updatable = false)
  private int clCode1 = 0;

  // 擔保品代號2
  /* 擔保品代號檔CdCl */
  @Column(name = "`ClCode2`", insertable = false, updatable = false)
  private int clCode2 = 0;

  // 擔保品編號
  @Column(name = "`ClNo`", insertable = false, updatable = false)
  private int clNo = 0;

  // 保證起日
  @Column(name = "`PledgeStartDate`")
  private int pledgeStartDate = 0;

  // 保證迄日
  @Column(name = "`PledgeEndDate`")
  private int pledgeEndDate = 0;

  // 保證銀行
  /* 共用代碼檔01:台新扣款02:華僑商銀03:匯通商銀04:中央信託05:中國農民06:交通銀行07:工業銀行08:陽信商銀09:上海銀行10:台北銀行11:世華商銀12:東京三菱13:高雄銀行14:中國商銀15:合庫扣款16:第一勸業17:美商花旗18:美國商銀19:泰國盤古20:美國運通21:菲律賓首22:美商大通23:日商東海24:紐約銀行25:郵局口款26:加大帝國27:波士頓28:日商富士29:法商百利30:荷蘭荷蘭31:新光銀行32:法國興業33:商豐業銀34:土地銀行35:中小企銀36:澳洲國民37:法國百利38:加大豐業39:中華農民40:上海農民41:比利聯合42:比利中國43:台北商銀44:新竹企銀45:台中企銀46:台南企銀47:高雄企銀48:花蓮企銀49:台東企銀50:第一銀行51:郵局52:德意志銀53:美商漢華54:加大皇銀55:華南銀行56:法國里昂57:萬通銀行58:大安銀行59:聯邦銀行60:中華商銀61:遠東商銀62:亞太商銀63:華信銀行64:玉山商銀65:萬泰銀行66:匯豐銀行67:泛亞銀行68:中興商銀69:富邦商銀70:大眾銀行71:寶島商銀72:安泰商銀73:巴黎銀行74:中國信託75:慶豐商銀76:英商渣打77:澳洲國銀78:彰化銀行79:瑞聯加豐80:安泰大眾81:中銀澳紐82:三家銀行83:花旗台新84:里昂百利85:奧紐西蘭86:日商東京87:比利信貸 */
  @Column(name = "`PledgeBankCode`", length = 2)
  private String pledgeBankCode;

  // 保證書字號
  @Column(name = "`PledgeNO`", length = 30)
  private String pledgeNO;

  // 客戶識別碼
  @Column(name = "`OwnerCustUKey`", length = 32)
  private String ownerCustUKey;

  // 發行機構統編
  @Column(name = "`IssuingId`", length = 10)
  private String issuingId;

  // 發行機構所在國別
  @Column(name = "`IssuingCounty`", length = 3)
  private String issuingCounty;

  // 憑證編號
  @Column(name = "`DocNo`", length = 30)
  private String docNo;

  // 貸放成數(%)
  @Column(name = "`LoanToValue`")
  private BigDecimal loanToValue = new BigDecimal("0");

  // 有價證券類別
  /* 01:股票02:基金03:債券04:票券/國庫儲蓄券05:其他 */
  @Column(name = "`SecuritiesType`", length = 2)
  private String securitiesType;

  // 掛牌交易所
  /* 01:臺灣證交所02:櫃檯買賣中心03:紐約證券交易所（NYSE）04:那斯達克（Nasdaq）05:倫敦證券交易所（LSE）06:德國證券交易所（GSE）07:歐洲交易所（Euronext）08:東京證券交易所（TSE）99:無 */
  @Column(name = "`Listed`", length = 2)
  private String listed;

  // 發行日
  @Column(name = "`OfferingDate`")
  private int offeringDate = 0;

  // 到期日
  @Column(name = "`ExpirationDate`")
  private int expirationDate = 0;

  // 發行者對象別
  /* 01:主權國家02:銀行03:企業98:無99:其他 */
  @Column(name = "`TargetIssuer`", length = 2)
  private String targetIssuer;

  // 發行者次對象別
  /* 01:主權國家02:銀行03:企業98:無99:其他 */
  @Column(name = "`SubTargetIssuer`", length = 2)
  private String subTargetIssuer;

  // 評等日期
  @Column(name = "`CreditDate`")
  private int creditDate = 0;

  // 評等公司
  /* 10:中華信評20:穆迪30:惠譽40:TCRI50:標準普爾90:其他 */
  @Column(name = "`Credit`", length = 2)
  private String credit;

  // 外部評等
  @Column(name = "`ExternalCredit`", length = 3)
  private String externalCredit;

  // 主要指數
  /* 01:臺灣加權指數02:日經指數03:恆生指數99:無 */
  @Column(name = "`Index`", length = 2)
  private String index;

  // 交易方法
  /* 0:正常1:全額交割 */
  @Column(name = "`TradingMethod`", length = 1)
  private String tradingMethod;

  // 受償順位
  @Column(name = "`Compensation`", length = 3)
  private String compensation;

  // 投資內容
  @Column(name = "`Investment`", length = 300)
  private String investment;

  // 公開價值
  @Column(name = "`PublicValue`", length = 300)
  private String publicValue;

  // 設定狀態
  /* 1:設定2:解除 */
  @Column(name = "`SettingStat`", length = 1)
  private String settingStat;

  // 擔保品狀態
  /* 0:正常1:塗銷2:處分3:抵押權確定 */
  @Column(name = "`ClStat`", length = 1)
  private String clStat;

  // 設定日期
  @Column(name = "`SettingDate`")
  private int settingDate = 0;

  // 設定金額
  @Column(name = "`SettingAmt`")
  private BigDecimal settingAmt = new BigDecimal("0");

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


  public ClOtherId getClOtherId() {
    return this.clOtherId;
  }

  public void setClOtherId(ClOtherId clOtherId) {
    this.clOtherId = clOtherId;
  }

/**
	* 擔保品代號1<br>
	* 擔保品代號檔CdCl
	* @return Integer
	*/
  public int getClCode1() {
    return this.clCode1;
  }

/**
	* 擔保品代號1<br>
	* 擔保品代號檔CdCl
  *
  * @param clCode1 擔保品代號1
	*/
  public void setClCode1(int clCode1) {
    this.clCode1 = clCode1;
  }

/**
	* 擔保品代號2<br>
	* 擔保品代號檔CdCl
	* @return Integer
	*/
  public int getClCode2() {
    return this.clCode2;
  }

/**
	* 擔保品代號2<br>
	* 擔保品代號檔CdCl
  *
  * @param clCode2 擔保品代號2
	*/
  public void setClCode2(int clCode2) {
    this.clCode2 = clCode2;
  }

/**
	* 擔保品編號<br>
	* 
	* @return Integer
	*/
  public int getClNo() {
    return this.clNo;
  }

/**
	* 擔保品編號<br>
	* 
  *
  * @param clNo 擔保品編號
	*/
  public void setClNo(int clNo) {
    this.clNo = clNo;
  }

/**
	* 保證起日<br>
	* 
	* @return Integer
	*/
  public int getPledgeStartDate() {
    return StaticTool.bcToRoc(this.pledgeStartDate);
  }

/**
	* 保證起日<br>
	* 
  *
  * @param pledgeStartDate 保證起日
  * @throws LogicException when Date Is Warn	*/
  public void setPledgeStartDate(int pledgeStartDate) throws LogicException {
    this.pledgeStartDate = StaticTool.rocToBc(pledgeStartDate);
  }

/**
	* 保證迄日<br>
	* 
	* @return Integer
	*/
  public int getPledgeEndDate() {
    return StaticTool.bcToRoc(this.pledgeEndDate);
  }

/**
	* 保證迄日<br>
	* 
  *
  * @param pledgeEndDate 保證迄日
  * @throws LogicException when Date Is Warn	*/
  public void setPledgeEndDate(int pledgeEndDate) throws LogicException {
    this.pledgeEndDate = StaticTool.rocToBc(pledgeEndDate);
  }

/**
	* 保證銀行<br>
	* 共用代碼檔
01:台新扣款
02:華僑商銀
03:匯通商銀
04:中央信託
05:中國農民
06:交通銀行
07:工業銀行
08:陽信商銀
09:上海銀行
10:台北銀行
11:世華商銀
12:東京三菱
13:高雄銀行
14:中國商銀
15:合庫扣款
16:第一勸業
17:美商花旗
18:美國商銀
19:泰國盤古
20:美國運通
21:菲律賓首
22:美商大通
23:日商東海
24:紐約銀行
25:郵局口款
26:加大帝國
27:波士頓
28:日商富士
29:法商百利
30:荷蘭荷蘭
31:新光銀行
32:法國興業
33:商豐業銀
34:土地銀行
35:中小企銀
36:澳洲國民
37:法國百利
38:加大豐業
39:中華農民
40:上海農民
41:比利聯合
42:比利中國
43:台北商銀
44:新竹企銀
45:台中企銀
46:台南企銀
47:高雄企銀
48:花蓮企銀
49:台東企銀
50:第一銀行
51:郵局
52:德意志銀
53:美商漢華
54:加大皇銀
55:華南銀行
56:法國里昂
57:萬通銀行
58:大安銀行
59:聯邦銀行
60:中華商銀
61:遠東商銀
62:亞太商銀
63:華信銀行
64:玉山商銀
65:萬泰銀行
66:匯豐銀行
67:泛亞銀行
68:中興商銀
69:富邦商銀
70:大眾銀行
71:寶島商銀
72:安泰商銀
73:巴黎銀行
74:中國信託
75:慶豐商銀
76:英商渣打
77:澳洲國銀
78:彰化銀行
79:瑞聯加豐
80:安泰大眾
81:中銀澳紐
82:三家銀行
83:花旗台新
84:里昂百利
85:奧紐西蘭
86:日商東京
87:比利信貸
	* @return String
	*/
  public String getPledgeBankCode() {
    return this.pledgeBankCode == null ? "" : this.pledgeBankCode;
  }

/**
	* 保證銀行<br>
	* 共用代碼檔
01:台新扣款
02:華僑商銀
03:匯通商銀
04:中央信託
05:中國農民
06:交通銀行
07:工業銀行
08:陽信商銀
09:上海銀行
10:台北銀行
11:世華商銀
12:東京三菱
13:高雄銀行
14:中國商銀
15:合庫扣款
16:第一勸業
17:美商花旗
18:美國商銀
19:泰國盤古
20:美國運通
21:菲律賓首
22:美商大通
23:日商東海
24:紐約銀行
25:郵局口款
26:加大帝國
27:波士頓
28:日商富士
29:法商百利
30:荷蘭荷蘭
31:新光銀行
32:法國興業
33:商豐業銀
34:土地銀行
35:中小企銀
36:澳洲國民
37:法國百利
38:加大豐業
39:中華農民
40:上海農民
41:比利聯合
42:比利中國
43:台北商銀
44:新竹企銀
45:台中企銀
46:台南企銀
47:高雄企銀
48:花蓮企銀
49:台東企銀
50:第一銀行
51:郵局
52:德意志銀
53:美商漢華
54:加大皇銀
55:華南銀行
56:法國里昂
57:萬通銀行
58:大安銀行
59:聯邦銀行
60:中華商銀
61:遠東商銀
62:亞太商銀
63:華信銀行
64:玉山商銀
65:萬泰銀行
66:匯豐銀行
67:泛亞銀行
68:中興商銀
69:富邦商銀
70:大眾銀行
71:寶島商銀
72:安泰商銀
73:巴黎銀行
74:中國信託
75:慶豐商銀
76:英商渣打
77:澳洲國銀
78:彰化銀行
79:瑞聯加豐
80:安泰大眾
81:中銀澳紐
82:三家銀行
83:花旗台新
84:里昂百利
85:奧紐西蘭
86:日商東京
87:比利信貸
  *
  * @param pledgeBankCode 保證銀行
	*/
  public void setPledgeBankCode(String pledgeBankCode) {
    this.pledgeBankCode = pledgeBankCode;
  }

/**
	* 保證書字號<br>
	* 
	* @return String
	*/
  public String getPledgeNO() {
    return this.pledgeNO == null ? "" : this.pledgeNO;
  }

/**
	* 保證書字號<br>
	* 
  *
  * @param pledgeNO 保證書字號
	*/
  public void setPledgeNO(String pledgeNO) {
    this.pledgeNO = pledgeNO;
  }

/**
	* 客戶識別碼<br>
	* 
	* @return String
	*/
  public String getOwnerCustUKey() {
    return this.ownerCustUKey == null ? "" : this.ownerCustUKey;
  }

/**
	* 客戶識別碼<br>
	* 
  *
  * @param ownerCustUKey 客戶識別碼
	*/
  public void setOwnerCustUKey(String ownerCustUKey) {
    this.ownerCustUKey = ownerCustUKey;
  }

/**
	* 發行機構統編<br>
	* 
	* @return String
	*/
  public String getIssuingId() {
    return this.issuingId == null ? "" : this.issuingId;
  }

/**
	* 發行機構統編<br>
	* 
  *
  * @param issuingId 發行機構統編
	*/
  public void setIssuingId(String issuingId) {
    this.issuingId = issuingId;
  }

/**
	* 發行機構所在國別<br>
	* 
	* @return String
	*/
  public String getIssuingCounty() {
    return this.issuingCounty == null ? "" : this.issuingCounty;
  }

/**
	* 發行機構所在國別<br>
	* 
  *
  * @param issuingCounty 發行機構所在國別
	*/
  public void setIssuingCounty(String issuingCounty) {
    this.issuingCounty = issuingCounty;
  }

/**
	* 憑證編號<br>
	* 
	* @return String
	*/
  public String getDocNo() {
    return this.docNo == null ? "" : this.docNo;
  }

/**
	* 憑證編號<br>
	* 
  *
  * @param docNo 憑證編號
	*/
  public void setDocNo(String docNo) {
    this.docNo = docNo;
  }

/**
	* 貸放成數(%)<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLoanToValue() {
    return this.loanToValue;
  }

/**
	* 貸放成數(%)<br>
	* 
  *
  * @param loanToValue 貸放成數(%)
	*/
  public void setLoanToValue(BigDecimal loanToValue) {
    this.loanToValue = loanToValue;
  }

/**
	* 有價證券類別<br>
	* 01:股票
02:基金
03:債券
04:票券/國庫儲蓄券
05:其他
	* @return String
	*/
  public String getSecuritiesType() {
    return this.securitiesType == null ? "" : this.securitiesType;
  }

/**
	* 有價證券類別<br>
	* 01:股票
02:基金
03:債券
04:票券/國庫儲蓄券
05:其他
  *
  * @param securitiesType 有價證券類別
	*/
  public void setSecuritiesType(String securitiesType) {
    this.securitiesType = securitiesType;
  }

/**
	* 掛牌交易所<br>
	* 01:臺灣證交所
02:櫃檯買賣中心
03:紐約證券交易所（NYSE）
04:那斯達克（Nasdaq）
05:倫敦證券交易所（LSE）
06:德國證券交易所（GSE）
07:歐洲交易所（Euronext）
08:東京證券交易所（TSE）
99:無
	* @return String
	*/
  public String getListed() {
    return this.listed == null ? "" : this.listed;
  }

/**
	* 掛牌交易所<br>
	* 01:臺灣證交所
02:櫃檯買賣中心
03:紐約證券交易所（NYSE）
04:那斯達克（Nasdaq）
05:倫敦證券交易所（LSE）
06:德國證券交易所（GSE）
07:歐洲交易所（Euronext）
08:東京證券交易所（TSE）
99:無
  *
  * @param listed 掛牌交易所
	*/
  public void setListed(String listed) {
    this.listed = listed;
  }

/**
	* 發行日<br>
	* 
	* @return Integer
	*/
  public int getOfferingDate() {
    return StaticTool.bcToRoc(this.offeringDate);
  }

/**
	* 發行日<br>
	* 
  *
  * @param offeringDate 發行日
  * @throws LogicException when Date Is Warn	*/
  public void setOfferingDate(int offeringDate) throws LogicException {
    this.offeringDate = StaticTool.rocToBc(offeringDate);
  }

/**
	* 到期日<br>
	* 
	* @return Integer
	*/
  public int getExpirationDate() {
    return StaticTool.bcToRoc(this.expirationDate);
  }

/**
	* 到期日<br>
	* 
  *
  * @param expirationDate 到期日
  * @throws LogicException when Date Is Warn	*/
  public void setExpirationDate(int expirationDate) throws LogicException {
    this.expirationDate = StaticTool.rocToBc(expirationDate);
  }

/**
	* 發行者對象別<br>
	* 01:主權國家
02:銀行
03:企業
98:無
99:其他
	* @return String
	*/
  public String getTargetIssuer() {
    return this.targetIssuer == null ? "" : this.targetIssuer;
  }

/**
	* 發行者對象別<br>
	* 01:主權國家
02:銀行
03:企業
98:無
99:其他
  *
  * @param targetIssuer 發行者對象別
	*/
  public void setTargetIssuer(String targetIssuer) {
    this.targetIssuer = targetIssuer;
  }

/**
	* 發行者次對象別<br>
	* 01:主權國家
02:銀行
03:企業
98:無
99:其他
	* @return String
	*/
  public String getSubTargetIssuer() {
    return this.subTargetIssuer == null ? "" : this.subTargetIssuer;
  }

/**
	* 發行者次對象別<br>
	* 01:主權國家
02:銀行
03:企業
98:無
99:其他
  *
  * @param subTargetIssuer 發行者次對象別
	*/
  public void setSubTargetIssuer(String subTargetIssuer) {
    this.subTargetIssuer = subTargetIssuer;
  }

/**
	* 評等日期<br>
	* 
	* @return Integer
	*/
  public int getCreditDate() {
    return StaticTool.bcToRoc(this.creditDate);
  }

/**
	* 評等日期<br>
	* 
  *
  * @param creditDate 評等日期
  * @throws LogicException when Date Is Warn	*/
  public void setCreditDate(int creditDate) throws LogicException {
    this.creditDate = StaticTool.rocToBc(creditDate);
  }

/**
	* 評等公司<br>
	* 10:中華信評
20:穆迪
30:惠譽
40:TCRI
50:標準普爾
90:其他
	* @return String
	*/
  public String getCredit() {
    return this.credit == null ? "" : this.credit;
  }

/**
	* 評等公司<br>
	* 10:中華信評
20:穆迪
30:惠譽
40:TCRI
50:標準普爾
90:其他
  *
  * @param credit 評等公司
	*/
  public void setCredit(String credit) {
    this.credit = credit;
  }

/**
	* 外部評等<br>
	* 
	* @return String
	*/
  public String getExternalCredit() {
    return this.externalCredit == null ? "" : this.externalCredit;
  }

/**
	* 外部評等<br>
	* 
  *
  * @param externalCredit 外部評等
	*/
  public void setExternalCredit(String externalCredit) {
    this.externalCredit = externalCredit;
  }

/**
	* 主要指數<br>
	* 01:臺灣加權指數
02:日經指數
03:恆生指數
99:無
	* @return String
	*/
  public String getIndex() {
    return this.index == null ? "" : this.index;
  }

/**
	* 主要指數<br>
	* 01:臺灣加權指數
02:日經指數
03:恆生指數
99:無
  *
  * @param index 主要指數
	*/
  public void setIndex(String index) {
    this.index = index;
  }

/**
	* 交易方法<br>
	* 0:正常
1:全額交割
	* @return String
	*/
  public String getTradingMethod() {
    return this.tradingMethod == null ? "" : this.tradingMethod;
  }

/**
	* 交易方法<br>
	* 0:正常
1:全額交割
  *
  * @param tradingMethod 交易方法
	*/
  public void setTradingMethod(String tradingMethod) {
    this.tradingMethod = tradingMethod;
  }

/**
	* 受償順位<br>
	* 
	* @return String
	*/
  public String getCompensation() {
    return this.compensation == null ? "" : this.compensation;
  }

/**
	* 受償順位<br>
	* 
  *
  * @param compensation 受償順位
	*/
  public void setCompensation(String compensation) {
    this.compensation = compensation;
  }

/**
	* 投資內容<br>
	* 
	* @return String
	*/
  public String getInvestment() {
    return this.investment == null ? "" : this.investment;
  }

/**
	* 投資內容<br>
	* 
  *
  * @param investment 投資內容
	*/
  public void setInvestment(String investment) {
    this.investment = investment;
  }

/**
	* 公開價值<br>
	* 
	* @return String
	*/
  public String getPublicValue() {
    return this.publicValue == null ? "" : this.publicValue;
  }

/**
	* 公開價值<br>
	* 
  *
  * @param publicValue 公開價值
	*/
  public void setPublicValue(String publicValue) {
    this.publicValue = publicValue;
  }

/**
	* 設定狀態<br>
	* 1:設定
2:解除
	* @return String
	*/
  public String getSettingStat() {
    return this.settingStat == null ? "" : this.settingStat;
  }

/**
	* 設定狀態<br>
	* 1:設定
2:解除
  *
  * @param settingStat 設定狀態
	*/
  public void setSettingStat(String settingStat) {
    this.settingStat = settingStat;
  }

/**
	* 擔保品狀態<br>
	* 0:正常
1:塗銷
2:處分
3:抵押權確定
	* @return String
	*/
  public String getClStat() {
    return this.clStat == null ? "" : this.clStat;
  }

/**
	* 擔保品狀態<br>
	* 0:正常
1:塗銷
2:處分
3:抵押權確定
  *
  * @param clStat 擔保品狀態
	*/
  public void setClStat(String clStat) {
    this.clStat = clStat;
  }

/**
	* 設定日期<br>
	* 
	* @return Integer
	*/
  public int getSettingDate() {
    return StaticTool.bcToRoc(this.settingDate);
  }

/**
	* 設定日期<br>
	* 
  *
  * @param settingDate 設定日期
  * @throws LogicException when Date Is Warn	*/
  public void setSettingDate(int settingDate) throws LogicException {
    this.settingDate = StaticTool.rocToBc(settingDate);
  }

/**
	* 設定金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getSettingAmt() {
    return this.settingAmt;
  }

/**
	* 設定金額<br>
	* 
  *
  * @param settingAmt 設定金額
	*/
  public void setSettingAmt(BigDecimal settingAmt) {
    this.settingAmt = settingAmt;
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
    return "ClOther [clOtherId=" + clOtherId + ", pledgeStartDate=" + pledgeStartDate + ", pledgeEndDate=" + pledgeEndDate + ", pledgeBankCode=" + pledgeBankCode
           + ", pledgeNO=" + pledgeNO + ", ownerCustUKey=" + ownerCustUKey + ", issuingId=" + issuingId + ", issuingCounty=" + issuingCounty + ", docNo=" + docNo + ", loanToValue=" + loanToValue
           + ", securitiesType=" + securitiesType + ", listed=" + listed + ", offeringDate=" + offeringDate + ", expirationDate=" + expirationDate + ", targetIssuer=" + targetIssuer + ", subTargetIssuer=" + subTargetIssuer
           + ", creditDate=" + creditDate + ", credit=" + credit + ", externalCredit=" + externalCredit + ", index=" + index + ", tradingMethod=" + tradingMethod + ", compensation=" + compensation
           + ", investment=" + investment + ", publicValue=" + publicValue + ", settingStat=" + settingStat + ", clStat=" + clStat + ", settingDate=" + settingDate + ", settingAmt=" + settingAmt
           + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
