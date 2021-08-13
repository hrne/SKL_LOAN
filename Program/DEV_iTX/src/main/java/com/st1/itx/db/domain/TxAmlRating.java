package com.st1.itx.db.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

/**
 * TxAmlRating Eloan評級檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`TxAmlRating`")
public class TxAmlRating implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -4552108482459559115L;

// 序號
  @Id
  @Column(name = "`LogNo`")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "`TxAmlRating_SEQ`")
  @SequenceGenerator(name = "`TxAmlRating_SEQ`", sequenceName = "`TxAmlRating_SEQ`", allocationSize = 1)
  private Long logNo = 0L;

  // 查詢單位
  /* 疑似名單審核單位 */
  @Column(name = "`Unit`", length = 6)
  private String unit;

  // 代辦單位
  /* 若為網路服務案件，為必填 */
  @Column(name = "`AcceptanceUnit`", length = 6)
  private String acceptanceUnit;

  // 保單角色
  /* 1:要保人4.借款人 */
  @Column(name = "`RoleId`", length = 2)
  private String roleId;

  // AML 交易序號
  /* 線別(2)+各線別的唯一Key */
  @Column(name = "`TransactionId`", length = 100)
  private String transactionId;

  // 保單號碼/放款案號
  /* 保單號碼/放款案號 */
  @Column(name = "`AcctNo`", length = 30)
  private String acctNo;

  // 案號
  /* 可識別為同一批交易的號碼：案號、ProcessID、SubmitID */
  @Column(name = "`CaseNo`", length = 40)
  private String caseNo;

  // 保險證號
  /* 團保使用 */
  @Column(name = "`AcctId`", length = 30)
  private String acctId;

  // 投保次數
  /* 團保使用 */
  @Column(name = "`InsurCount`")
  private int insurCount = 0;

  // 個人出生日
  /* YYYYMMDD，民國(0105);若是法人填空白 */
  @Column(name = "`BirthEstDt`", length = 8)
  private String birthEstDt;

  // 查詢來源
  /* 系統代號 */
  @Column(name = "`SourceId`", length = 10)
  private String sourceId;

  // 異動時間
  /* YYYYMMDDHHmmss，民國(0105) */
  @Column(name = "`ModifyDate`", length = 14)
  private String modifyDate;

  // 職業代碼
  /* 各系統中有資訊時需提供;自然人使用風險因子項目-職業代碼 */
  @Column(name = "`OcupCd`", length = 8)
  private String ocupCd;

  // 組織型態
  /* 各系統中有資訊時需提供風險因子項目-組織型態代碼 */
  @Column(name = "`OrgType`", length = 5)
  private String orgType;

  // 行業代碼
  /* 各系統中有資訊時需提供;法人使用風險因子項目-行業代碼 */
  @Column(name = "`Bcode`", length = 5)
  private String bcode;

  // 行業別說明
  /* 各系統中有資訊時需提供不在行業代碼裡，需要在表單上說明 */
  @Column(name = "`OcupNote`", length = 100)
  private String ocupNote;

  // 繳費渠道
  /* 各系統中有資訊時需提供風險因子項目-繳費渠道代碼 */
  @Column(name = "`PayMethod`", length = 5)
  private String payMethod;

  // 繳費方式
  /* 各系統中有資訊時需提供風險因子項目-繳費方式代碼 */
  @Column(name = "`PayType`", length = 5)
  private String payType;

  // 渠道(進件通路)
  /* 各系統中有資訊時需提供風險因子項目-渠道代碼 */
  @Column(name = "`Channel`", length = 12)
  private String channel;

  // 險種
  /* 各系統中有資訊時需提供風險因子項目-險種代碼 */
  @Column(name = "`PolicyType`", length = 5)
  private String policyType;

  // 保費幣別/申貸幣別
  @Column(name = "`InsuranceCurrency`", length = 3)
  private String insuranceCurrency;

  // 保費(原幣)/申貸金額
  /* 以"元"為單位保費是指各別保險契約首期約定年繳化保費 */
  @Column(name = "`InsuranceAmount`")
  private BigDecimal insuranceAmount = new BigDecimal("0");

  // 額外風險因子
  /* 每項風險因子以"|"符號區隔，共需回傳5項風險因子代碼。風險因子順序：  1.現有無記名股票/股東  2.實質受益人為第三層以後之第一類型之自然人大股東  3.客戶拒絕或無法填寫實質受益人聲明書  4.無法辨識第一類型實質受益人之自然人大股東  5.為高階管理人員如：101|201|302|401|502 */
  @Column(name = "`AddnCd`", length = 200)
  private String addnCd;

  // 受益人-被保險人與受益人無身分利害關係
  /* 000：不適用BRI01：無身分利害關係BRI02：有身分利害關係999：無法提供相關資訊 */
  @Column(name = "`InsrStakesCd`", length = 5)
  private String insrStakesCd;

  // 受益人-受益人為非要保人之法人/團體
  /* 000：不適用BJP01：非要保人之法人/團體BJP02：為要保人之法人/團體999：無法提供相關資訊 */
  @Column(name = "`BnfcryNHdrGrpCd`", length = 5)
  private String bnfcryNHdrGrpCd;

  // 由代理人代理建立業務關係或交易(自然人、法人)
  /* 000：不適用ABT01：為代理人代理ABT02：非代理人代理999：無法提供相關資訊 */
  @Column(name = "`AgentTradeCd`", length = 5)
  private String agentTradeCd;

  // 繳費人-首期保費繳費人為非要保人之法人/團體
  /* 000：不適用PJP01：非要保人之法人/團體PJP02：為要保人之法人/團體999：無法提供相關資訊 */
  @Column(name = "`FstPayerNHdrGrpCd`", length = 5)
  private String fstPayerNHdrGrpCd;

  // 繳費人-首期保費繳費人與要保人間無利害關係
  /* 000：不適用PRP01：無利害關係PRP02：有利害關係999：無法提供相關資訊 */
  @Column(name = "`FstPayerNHdrStksCd`", length = 5)
  private String fstPayerNHdrStksCd;

  // 繳費人-首期保費來自境外帳戶繳費
  /* 000：不適用POA01：來自境外帳戶繳費POA02：非境外帳戶繳費999：無法提供相關資訊 */
  @Column(name = "`FstPrmOvrseaAcctCd`", length = 5)
  private String fstPrmOvrseaAcctCd;

  // 過往往來紀錄-所有有效保單累積總保價金(帳戶價值)(借款總額)
  /* 幣別：台幣以元為單位 */
  @Column(name = "`TotalAmtCd`")
  private BigDecimal totalAmtCd = new BigDecimal("0");

  // 過往往來紀錄-曾遭拒保(拒絕核貸)
  /* 000：不適用RAR01：曾遭拒保RAR02：未曾拒保999：無法提供相關資訊 */
  @Column(name = "`DeclinatureCd`", length = 5)
  private String declinatureCd;

  // 過往往來紀錄-首次投保(借款)
  /* 000：不適用RAF01：首次投保RAF02：非首次投保999：無法提供相關資訊 */
  @Column(name = "`FstInsuredCd`", length = 5)
  private String fstInsuredCd;

  // 地緣關係-自然人(借款人)要保人為外國人在台無居住地址/通訊地址
  /* 000：不適用GRP01：無居住/通訊地址GRP02：有居住/通訊地址999：無法提供相關資訊 */
  @Column(name = "`TWAddrHoldCd`", length = 5)
  private String tWAddrHoldCd;

  // 地緣關係-外國法人客戶在台澎金馬範圍無營業地址/通訊地址
  /* 000：不適用GRF01：無居住/通訊地址GRF02：有居住/通訊地址999：無法提供相關資訊 */
  @Column(name = "`TWAddrLegalCd`", length = 5)
  private String tWAddrLegalCd;

  // 存續狀態-AH2
  /* 000：不適用DJP02：籌備處DJP03：停業/歇業中DJP01：營業中999：無法提供相關資訊 */
  @Column(name = "`DurationCd`", length = 5)
  private String durationCd;

  // 特殊身分
  /* 001：是特殊身分002：非特殊身分 */
  @Column(name = "`SpecialIdentity`", length = 5)
  private String specialIdentity;

  // 法令規定強制投保之保單及其附屬保險
  /* 001:是002:否 */
  @Column(name = "`LawForceWarranty`", length = 5)
  private String lawForceWarranty;

  // 擔保品-是否提供動產擔保
  /* 000：不適用LCM01：有提供動產擔保LCM02：無提供動產擔保999：無法提供相關資訊 */
  @Column(name = "`MovableGrnteeCd`", length = 5)
  private String movableGrnteeCd;

  // 擔保品-是否提供無記名有價證券擔保
  /* 000：不適用LCS01：有提供無記名有價證券擔保LCS02：無提供無記名有價證券擔保999：無法提供相關資訊 */
  @Column(name = "`BearerScursGrnteeCd`", length = 5)
  private String bearerScursGrnteeCd;

  // 清償方案-自然人未約定「提前清償違約金」（不綁約）
  /* 000：不適用LLP01：自然人未約定「提前清償違約金」（不綁約）LLP02：自然人有約定「提前清償違約金」（不綁約）999：無法提供相關資訊 */
  @Column(name = "`AgreeDefaultFineCd`", length = 5)
  private String agreeDefaultFineCd;

  // 資金用途-資金用途非為購置不動產
  /* 000：不適用LPE01：資金用途非為購置不動產LPE02：資金用途為購置不動產 */
  @Column(name = "`NonBuyingRealEstateCd`", length = 5)
  private String nonBuyingRealEstateCd;

  // 保證-非利害關係人提供保證
  /* 000：不適用LMR01：非利害關係人提供保證LMR02：為利害關係人提供保證999：無法提供相關資訊 */
  @Column(name = "`NonStkHolderGrnteeCd`", length = 5)
  private String nonStkHolderGrnteeCd;

  // 多次借款-個人/法人申貸案件達一定筆數以上（含本次貸款申請）
  /* 提供額度編號與本案之合計筆數 */
  @Column(name = "`ReachCase`")
  private int reachCase = 0;

  // 受款帳戶-受款帳戶「非」本人或本人代償金融機構帳戶或履約保證或不動產出賣人帳戶
  /* 000：不適用LRA01：受款帳戶「非」本人或本人代償金融機構帳戶或履約保證或不動產出賣人帳戶LRA02：受款帳戶為本人或本人代償金融機構帳戶或履約保證或不動產出賣人帳戶999：無法提供相關資訊 */
  @Column(name = "`AccountTypeCd`", length = 5)
  private String accountTypeCd;

  // 區別要保人異動為暫時資料/確定資料
  /* 暫時：T確定：S */
  @Column(name = "`QueryType`", length = 1)
  private String queryType;

  // 身份別
  /* 1:自然人2:法人 */
  @Column(name = "`IdentityCd`", length = 1)
  private String identityCd;

  // Request Id
  /* 系統使用;各系統不需處理 */
  @Column(name = "`RspRequestId`", length = 36)
  private String rspRequestId;

  // 狀態
  /* Success : 表示處理完成，無需登入AML作業Warning : 表示需登入AML作業Fail : 表示處理過程有異常，無法正常處理 */
  @Column(name = "`RspStatus`", length = 10)
  private String rspStatus;

  // 狀態代碼
  /* 如:0001 */
  @Column(name = "`RspStatusCode`", length = 4)
  private String rspStatusCode;

  // 狀態說明
  /* 如：處理完成，無需進入AML作業 */
  @Column(name = "`RspStatusDesc`", length = 100)
  private String rspStatusDesc;

  // 查詢單位
  /* 引入Request內容 */
  @Column(name = "`RspUnit`", length = 6)
  private String rspUnit;

  // AML 交易序號
  /* 引入Request內容 */
  @Column(name = "`RspTransactionId`", length = 100)
  private String rspTransactionId;

  // 保單號碼
  /* 引入Request內容 */
  @Column(name = "`RspAcctNo`", length = 30)
  private String rspAcctNo;

  // 案號
  /* 引入Request內容 */
  @Column(name = "`RspCaseNo`", length = 40)
  private String rspCaseNo;

  // 投保次數
  /* 引入Request內容 */
  @Column(name = "`RspInsurCount`")
  private int rspInsurCount = 0;

  // 分數
  /* 引入Request內容 */
  @Column(name = "`RspTotalRatingsScore`")
  private BigDecimal rspTotalRatingsScore = new BigDecimal("0");

  // 總評級(WLF+CDD)
  /* 若Is_Similar="Y"且同筆一案件第一次查詢時填入空白若Is_Similar="Y"時或同一筆案件第二次查詢時填入H:高M:中L:低 */
  @Column(name = "`RspTotalRatings`", length = 1)
  private String rspTotalRatings;

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


/**
	* 序號<br>
	* 
	* @return Long
	*/
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long getLogNo() {
    return this.logNo;
  }

/**
	* 序號<br>
	* 
  *
  * @param logNo 序號
	*/
  public void setLogNo(Long logNo) {
    this.logNo = logNo;
  }

/**
	* 查詢單位<br>
	* 疑似名單審核單位
	* @return String
	*/
  public String getUnit() {
    return this.unit == null ? "" : this.unit;
  }

/**
	* 查詢單位<br>
	* 疑似名單審核單位
  *
  * @param unit 查詢單位
	*/
  public void setUnit(String unit) {
    this.unit = unit;
  }

/**
	* 代辦單位<br>
	* 若為網路服務案件，為必填
	* @return String
	*/
  public String getAcceptanceUnit() {
    return this.acceptanceUnit == null ? "" : this.acceptanceUnit;
  }

/**
	* 代辦單位<br>
	* 若為網路服務案件，為必填
  *
  * @param acceptanceUnit 代辦單位
	*/
  public void setAcceptanceUnit(String acceptanceUnit) {
    this.acceptanceUnit = acceptanceUnit;
  }

/**
	* 保單角色<br>
	* 1:要保人
4.借款人
	* @return String
	*/
  public String getRoleId() {
    return this.roleId == null ? "" : this.roleId;
  }

/**
	* 保單角色<br>
	* 1:要保人
4.借款人
  *
  * @param roleId 保單角色
	*/
  public void setRoleId(String roleId) {
    this.roleId = roleId;
  }

/**
	* AML 交易序號<br>
	* 線別(2)+各線別的唯一Key
	* @return String
	*/
  public String getTransactionId() {
    return this.transactionId == null ? "" : this.transactionId;
  }

/**
	* AML 交易序號<br>
	* 線別(2)+各線別的唯一Key
  *
  * @param transactionId AML 交易序號
	*/
  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

/**
	* 保單號碼/放款案號<br>
	* 保單號碼/放款案號
	* @return String
	*/
  public String getAcctNo() {
    return this.acctNo == null ? "" : this.acctNo;
  }

/**
	* 保單號碼/放款案號<br>
	* 保單號碼/放款案號
  *
  * @param acctNo 保單號碼/放款案號
	*/
  public void setAcctNo(String acctNo) {
    this.acctNo = acctNo;
  }

/**
	* 案號<br>
	* 可識別為同一批交易的號碼：案號、ProcessID、SubmitID
	* @return String
	*/
  public String getCaseNo() {
    return this.caseNo == null ? "" : this.caseNo;
  }

/**
	* 案號<br>
	* 可識別為同一批交易的號碼：案號、ProcessID、SubmitID
  *
  * @param caseNo 案號
	*/
  public void setCaseNo(String caseNo) {
    this.caseNo = caseNo;
  }

/**
	* 保險證號<br>
	* 團保使用
	* @return String
	*/
  public String getAcctId() {
    return this.acctId == null ? "" : this.acctId;
  }

/**
	* 保險證號<br>
	* 團保使用
  *
  * @param acctId 保險證號
	*/
  public void setAcctId(String acctId) {
    this.acctId = acctId;
  }

/**
	* 投保次數<br>
	* 團保使用
	* @return Integer
	*/
  public int getInsurCount() {
    return this.insurCount;
  }

/**
	* 投保次數<br>
	* 團保使用
  *
  * @param insurCount 投保次數
	*/
  public void setInsurCount(int insurCount) {
    this.insurCount = insurCount;
  }

/**
	* 個人出生日<br>
	* YYYYMMDD，民國(0105);若是法人填空白
	* @return String
	*/
  public String getBirthEstDt() {
    return this.birthEstDt == null ? "" : this.birthEstDt;
  }

/**
	* 個人出生日<br>
	* YYYYMMDD，民國(0105);若是法人填空白
  *
  * @param birthEstDt 個人出生日
	*/
  public void setBirthEstDt(String birthEstDt) {
    this.birthEstDt = birthEstDt;
  }

/**
	* 查詢來源<br>
	* 系統代號
	* @return String
	*/
  public String getSourceId() {
    return this.sourceId == null ? "" : this.sourceId;
  }

/**
	* 查詢來源<br>
	* 系統代號
  *
  * @param sourceId 查詢來源
	*/
  public void setSourceId(String sourceId) {
    this.sourceId = sourceId;
  }

/**
	* 異動時間<br>
	* YYYYMMDDHHmmss，民國(0105)
	* @return String
	*/
  public String getModifyDate() {
    return this.modifyDate == null ? "" : this.modifyDate;
  }

/**
	* 異動時間<br>
	* YYYYMMDDHHmmss，民國(0105)
  *
  * @param modifyDate 異動時間
	*/
  public void setModifyDate(String modifyDate) {
    this.modifyDate = modifyDate;
  }

/**
	* 職業代碼<br>
	* 各系統中有資訊時需提供;自然人使用
風險因子項目-職業代碼
	* @return String
	*/
  public String getOcupCd() {
    return this.ocupCd == null ? "" : this.ocupCd;
  }

/**
	* 職業代碼<br>
	* 各系統中有資訊時需提供;自然人使用
風險因子項目-職業代碼
  *
  * @param ocupCd 職業代碼
	*/
  public void setOcupCd(String ocupCd) {
    this.ocupCd = ocupCd;
  }

/**
	* 組織型態<br>
	* 各系統中有資訊時需提供
風險因子項目-組織型態代碼
	* @return String
	*/
  public String getOrgType() {
    return this.orgType == null ? "" : this.orgType;
  }

/**
	* 組織型態<br>
	* 各系統中有資訊時需提供
風險因子項目-組織型態代碼
  *
  * @param orgType 組織型態
	*/
  public void setOrgType(String orgType) {
    this.orgType = orgType;
  }

/**
	* 行業代碼<br>
	* 各系統中有資訊時需提供;法人使用
風險因子項目-行業代碼
	* @return String
	*/
  public String getBcode() {
    return this.bcode == null ? "" : this.bcode;
  }

/**
	* 行業代碼<br>
	* 各系統中有資訊時需提供;法人使用
風險因子項目-行業代碼
  *
  * @param bcode 行業代碼
	*/
  public void setBcode(String bcode) {
    this.bcode = bcode;
  }

/**
	* 行業別說明<br>
	* 各系統中有資訊時需提供
不在行業代碼裡，需要在表單上說明
	* @return String
	*/
  public String getOcupNote() {
    return this.ocupNote == null ? "" : this.ocupNote;
  }

/**
	* 行業別說明<br>
	* 各系統中有資訊時需提供
不在行業代碼裡，需要在表單上說明
  *
  * @param ocupNote 行業別說明
	*/
  public void setOcupNote(String ocupNote) {
    this.ocupNote = ocupNote;
  }

/**
	* 繳費渠道<br>
	* 各系統中有資訊時需提供
風險因子項目-繳費渠道代碼
	* @return String
	*/
  public String getPayMethod() {
    return this.payMethod == null ? "" : this.payMethod;
  }

/**
	* 繳費渠道<br>
	* 各系統中有資訊時需提供
風險因子項目-繳費渠道代碼
  *
  * @param payMethod 繳費渠道
	*/
  public void setPayMethod(String payMethod) {
    this.payMethod = payMethod;
  }

/**
	* 繳費方式<br>
	* 各系統中有資訊時需提供
風險因子項目-繳費方式代碼
	* @return String
	*/
  public String getPayType() {
    return this.payType == null ? "" : this.payType;
  }

/**
	* 繳費方式<br>
	* 各系統中有資訊時需提供
風險因子項目-繳費方式代碼
  *
  * @param payType 繳費方式
	*/
  public void setPayType(String payType) {
    this.payType = payType;
  }

/**
	* 渠道(進件通路)<br>
	* 各系統中有資訊時需提供
風險因子項目-渠道代碼
	* @return String
	*/
  public String getChannel() {
    return this.channel == null ? "" : this.channel;
  }

/**
	* 渠道(進件通路)<br>
	* 各系統中有資訊時需提供
風險因子項目-渠道代碼
  *
  * @param channel 渠道(進件通路)
	*/
  public void setChannel(String channel) {
    this.channel = channel;
  }

/**
	* 險種<br>
	* 各系統中有資訊時需提供
風險因子項目-險種代碼
	* @return String
	*/
  public String getPolicyType() {
    return this.policyType == null ? "" : this.policyType;
  }

/**
	* 險種<br>
	* 各系統中有資訊時需提供
風險因子項目-險種代碼
  *
  * @param policyType 險種
	*/
  public void setPolicyType(String policyType) {
    this.policyType = policyType;
  }

/**
	* 保費幣別/申貸幣別<br>
	* 
	* @return String
	*/
  public String getInsuranceCurrency() {
    return this.insuranceCurrency == null ? "" : this.insuranceCurrency;
  }

/**
	* 保費幣別/申貸幣別<br>
	* 
  *
  * @param insuranceCurrency 保費幣別/申貸幣別
	*/
  public void setInsuranceCurrency(String insuranceCurrency) {
    this.insuranceCurrency = insuranceCurrency;
  }

/**
	* 保費(原幣)/申貸金額<br>
	* 以"元"為單位
保費是指各別保險契約首期約定年繳化保費
	* @return BigDecimal
	*/
  public BigDecimal getInsuranceAmount() {
    return this.insuranceAmount;
  }

/**
	* 保費(原幣)/申貸金額<br>
	* 以"元"為單位
保費是指各別保險契約首期約定年繳化保費
  *
  * @param insuranceAmount 保費(原幣)/申貸金額
	*/
  public void setInsuranceAmount(BigDecimal insuranceAmount) {
    this.insuranceAmount = insuranceAmount;
  }

/**
	* 額外風險因子<br>
	* 每項風險因子以"|"符號區隔，共需回傳5項風險因子代碼。
風險因子順序：
  1.現有無記名股票/股東
  2.實質受益人為第三層以後之第一類型之自然人大股東
  3.客戶拒絕或無法填寫實質受益人聲明書
  4.無法辨識第一類型實質受益人之自然人大股東
  5.為高階管理人員
如：101|201|302|401|502
	* @return String
	*/
  public String getAddnCd() {
    return this.addnCd == null ? "" : this.addnCd;
  }

/**
	* 額外風險因子<br>
	* 每項風險因子以"|"符號區隔，共需回傳5項風險因子代碼。
風險因子順序：
  1.現有無記名股票/股東
  2.實質受益人為第三層以後之第一類型之自然人大股東
  3.客戶拒絕或無法填寫實質受益人聲明書
  4.無法辨識第一類型實質受益人之自然人大股東
  5.為高階管理人員
如：101|201|302|401|502
  *
  * @param addnCd 額外風險因子
	*/
  public void setAddnCd(String addnCd) {
    this.addnCd = addnCd;
  }

/**
	* 受益人-被保險人與受益人無身分利害關係<br>
	* 000：不適用
BRI01：無身分利害關係
BRI02：有身分利害關係
999：無法提供相關資訊
	* @return String
	*/
  public String getInsrStakesCd() {
    return this.insrStakesCd == null ? "" : this.insrStakesCd;
  }

/**
	* 受益人-被保險人與受益人無身分利害關係<br>
	* 000：不適用
BRI01：無身分利害關係
BRI02：有身分利害關係
999：無法提供相關資訊
  *
  * @param insrStakesCd 受益人-被保險人與受益人無身分利害關係
	*/
  public void setInsrStakesCd(String insrStakesCd) {
    this.insrStakesCd = insrStakesCd;
  }

/**
	* 受益人-受益人為非要保人之法人/團體<br>
	* 000：不適用
BJP01：非要保人之法人/團體
BJP02：為要保人之法人/團體
999：無法提供相關資訊
	* @return String
	*/
  public String getBnfcryNHdrGrpCd() {
    return this.bnfcryNHdrGrpCd == null ? "" : this.bnfcryNHdrGrpCd;
  }

/**
	* 受益人-受益人為非要保人之法人/團體<br>
	* 000：不適用
BJP01：非要保人之法人/團體
BJP02：為要保人之法人/團體
999：無法提供相關資訊
  *
  * @param bnfcryNHdrGrpCd 受益人-受益人為非要保人之法人/團體
	*/
  public void setBnfcryNHdrGrpCd(String bnfcryNHdrGrpCd) {
    this.bnfcryNHdrGrpCd = bnfcryNHdrGrpCd;
  }

/**
	* 由代理人代理建立業務關係或交易(自然人、法人)<br>
	* 000：不適用
ABT01：為代理人代理
ABT02：非代理人代理
999：無法提供相關資訊
	* @return String
	*/
  public String getAgentTradeCd() {
    return this.agentTradeCd == null ? "" : this.agentTradeCd;
  }

/**
	* 由代理人代理建立業務關係或交易(自然人、法人)<br>
	* 000：不適用
ABT01：為代理人代理
ABT02：非代理人代理
999：無法提供相關資訊
  *
  * @param agentTradeCd 由代理人代理建立業務關係或交易(自然人、法人)
	*/
  public void setAgentTradeCd(String agentTradeCd) {
    this.agentTradeCd = agentTradeCd;
  }

/**
	* 繳費人-首期保費繳費人為非要保人之法人/團體<br>
	* 000：不適用
PJP01：非要保人之法人/團體
PJP02：為要保人之法人/團體
999：無法提供相關資訊
	* @return String
	*/
  public String getFstPayerNHdrGrpCd() {
    return this.fstPayerNHdrGrpCd == null ? "" : this.fstPayerNHdrGrpCd;
  }

/**
	* 繳費人-首期保費繳費人為非要保人之法人/團體<br>
	* 000：不適用
PJP01：非要保人之法人/團體
PJP02：為要保人之法人/團體
999：無法提供相關資訊
  *
  * @param fstPayerNHdrGrpCd 繳費人-首期保費繳費人為非要保人之法人/團體
	*/
  public void setFstPayerNHdrGrpCd(String fstPayerNHdrGrpCd) {
    this.fstPayerNHdrGrpCd = fstPayerNHdrGrpCd;
  }

/**
	* 繳費人-首期保費繳費人與要保人間無利害關係<br>
	* 000：不適用
PRP01：無利害關係
PRP02：有利害關係
999：無法提供相關資訊
	* @return String
	*/
  public String getFstPayerNHdrStksCd() {
    return this.fstPayerNHdrStksCd == null ? "" : this.fstPayerNHdrStksCd;
  }

/**
	* 繳費人-首期保費繳費人與要保人間無利害關係<br>
	* 000：不適用
PRP01：無利害關係
PRP02：有利害關係
999：無法提供相關資訊
  *
  * @param fstPayerNHdrStksCd 繳費人-首期保費繳費人與要保人間無利害關係
	*/
  public void setFstPayerNHdrStksCd(String fstPayerNHdrStksCd) {
    this.fstPayerNHdrStksCd = fstPayerNHdrStksCd;
  }

/**
	* 繳費人-首期保費來自境外帳戶繳費<br>
	* 000：不適用
POA01：來自境外帳戶繳費
POA02：非境外帳戶繳費
999：無法提供相關資訊
	* @return String
	*/
  public String getFstPrmOvrseaAcctCd() {
    return this.fstPrmOvrseaAcctCd == null ? "" : this.fstPrmOvrseaAcctCd;
  }

/**
	* 繳費人-首期保費來自境外帳戶繳費<br>
	* 000：不適用
POA01：來自境外帳戶繳費
POA02：非境外帳戶繳費
999：無法提供相關資訊
  *
  * @param fstPrmOvrseaAcctCd 繳費人-首期保費來自境外帳戶繳費
	*/
  public void setFstPrmOvrseaAcctCd(String fstPrmOvrseaAcctCd) {
    this.fstPrmOvrseaAcctCd = fstPrmOvrseaAcctCd;
  }

/**
	* 過往往來紀錄-所有有效保單累積總保價金(帳戶價值)(借款總額)<br>
	* 幣別：台幣
以元為單位
	* @return BigDecimal
	*/
  public BigDecimal getTotalAmtCd() {
    return this.totalAmtCd;
  }

/**
	* 過往往來紀錄-所有有效保單累積總保價金(帳戶價值)(借款總額)<br>
	* 幣別：台幣
以元為單位
  *
  * @param totalAmtCd 過往往來紀錄-所有有效保單累積總保價金(帳戶價值)(借款總額)
	*/
  public void setTotalAmtCd(BigDecimal totalAmtCd) {
    this.totalAmtCd = totalAmtCd;
  }

/**
	* 過往往來紀錄-曾遭拒保(拒絕核貸)<br>
	* 000：不適用
RAR01：曾遭拒保
RAR02：未曾拒保
999：無法提供相關資訊
	* @return String
	*/
  public String getDeclinatureCd() {
    return this.declinatureCd == null ? "" : this.declinatureCd;
  }

/**
	* 過往往來紀錄-曾遭拒保(拒絕核貸)<br>
	* 000：不適用
RAR01：曾遭拒保
RAR02：未曾拒保
999：無法提供相關資訊
  *
  * @param declinatureCd 過往往來紀錄-曾遭拒保(拒絕核貸)
	*/
  public void setDeclinatureCd(String declinatureCd) {
    this.declinatureCd = declinatureCd;
  }

/**
	* 過往往來紀錄-首次投保(借款)<br>
	* 000：不適用
RAF01：首次投保
RAF02：非首次投保
999：無法提供相關資訊
	* @return String
	*/
  public String getFstInsuredCd() {
    return this.fstInsuredCd == null ? "" : this.fstInsuredCd;
  }

/**
	* 過往往來紀錄-首次投保(借款)<br>
	* 000：不適用
RAF01：首次投保
RAF02：非首次投保
999：無法提供相關資訊
  *
  * @param fstInsuredCd 過往往來紀錄-首次投保(借款)
	*/
  public void setFstInsuredCd(String fstInsuredCd) {
    this.fstInsuredCd = fstInsuredCd;
  }

/**
	* 地緣關係-自然人(借款人)要保人為外國人在台無居住地址/通訊地址<br>
	* 000：不適用
GRP01：無居住/通訊地址
GRP02：有居住/通訊地址
999：無法提供相關資訊
	* @return String
	*/
  public String getTWAddrHoldCd() {
    return this.tWAddrHoldCd == null ? "" : this.tWAddrHoldCd;
  }

/**
	* 地緣關係-自然人(借款人)要保人為外國人在台無居住地址/通訊地址<br>
	* 000：不適用
GRP01：無居住/通訊地址
GRP02：有居住/通訊地址
999：無法提供相關資訊
  *
  * @param tWAddrHoldCd 地緣關係-自然人(借款人)要保人為外國人在台無居住地址/通訊地址
	*/
  public void setTWAddrHoldCd(String tWAddrHoldCd) {
    this.tWAddrHoldCd = tWAddrHoldCd;
  }

/**
	* 地緣關係-外國法人客戶在台澎金馬範圍無營業地址/通訊地址<br>
	* 000：不適用
GRF01：無居住/通訊地址
GRF02：有居住/通訊地址
999：無法提供相關資訊
	* @return String
	*/
  public String getTWAddrLegalCd() {
    return this.tWAddrLegalCd == null ? "" : this.tWAddrLegalCd;
  }

/**
	* 地緣關係-外國法人客戶在台澎金馬範圍無營業地址/通訊地址<br>
	* 000：不適用
GRF01：無居住/通訊地址
GRF02：有居住/通訊地址
999：無法提供相關資訊
  *
  * @param tWAddrLegalCd 地緣關係-外國法人客戶在台澎金馬範圍無營業地址/通訊地址
	*/
  public void setTWAddrLegalCd(String tWAddrLegalCd) {
    this.tWAddrLegalCd = tWAddrLegalCd;
  }

/**
	* 存續狀態-AH2<br>
	* 000：不適用
DJP02：籌備處
DJP03：停業/歇業中
DJP01：營業中
999：無法提供相關資訊
	* @return String
	*/
  public String getDurationCd() {
    return this.durationCd == null ? "" : this.durationCd;
  }

/**
	* 存續狀態-AH2<br>
	* 000：不適用
DJP02：籌備處
DJP03：停業/歇業中
DJP01：營業中
999：無法提供相關資訊
  *
  * @param durationCd 存續狀態-AH2
	*/
  public void setDurationCd(String durationCd) {
    this.durationCd = durationCd;
  }

/**
	* 特殊身分<br>
	* 001：是特殊身分
002：非特殊身分
	* @return String
	*/
  public String getSpecialIdentity() {
    return this.specialIdentity == null ? "" : this.specialIdentity;
  }

/**
	* 特殊身分<br>
	* 001：是特殊身分
002：非特殊身分
  *
  * @param specialIdentity 特殊身分
	*/
  public void setSpecialIdentity(String specialIdentity) {
    this.specialIdentity = specialIdentity;
  }

/**
	* 法令規定強制投保之保單及其附屬保險<br>
	* 001:是
002:否
	* @return String
	*/
  public String getLawForceWarranty() {
    return this.lawForceWarranty == null ? "" : this.lawForceWarranty;
  }

/**
	* 法令規定強制投保之保單及其附屬保險<br>
	* 001:是
002:否
  *
  * @param lawForceWarranty 法令規定強制投保之保單及其附屬保險
	*/
  public void setLawForceWarranty(String lawForceWarranty) {
    this.lawForceWarranty = lawForceWarranty;
  }

/**
	* 擔保品-是否提供動產擔保<br>
	* 000：不適用
LCM01：有提供動產擔保
LCM02：無提供動產擔保
999：無法提供相關資訊
	* @return String
	*/
  public String getMovableGrnteeCd() {
    return this.movableGrnteeCd == null ? "" : this.movableGrnteeCd;
  }

/**
	* 擔保品-是否提供動產擔保<br>
	* 000：不適用
LCM01：有提供動產擔保
LCM02：無提供動產擔保
999：無法提供相關資訊
  *
  * @param movableGrnteeCd 擔保品-是否提供動產擔保
	*/
  public void setMovableGrnteeCd(String movableGrnteeCd) {
    this.movableGrnteeCd = movableGrnteeCd;
  }

/**
	* 擔保品-是否提供無記名有價證券擔保<br>
	* 000：不適用
LCS01：有提供無記名有價證券擔保
LCS02：無提供無記名有價證券擔保
999：無法提供相關資訊
	* @return String
	*/
  public String getBearerScursGrnteeCd() {
    return this.bearerScursGrnteeCd == null ? "" : this.bearerScursGrnteeCd;
  }

/**
	* 擔保品-是否提供無記名有價證券擔保<br>
	* 000：不適用
LCS01：有提供無記名有價證券擔保
LCS02：無提供無記名有價證券擔保
999：無法提供相關資訊
  *
  * @param bearerScursGrnteeCd 擔保品-是否提供無記名有價證券擔保
	*/
  public void setBearerScursGrnteeCd(String bearerScursGrnteeCd) {
    this.bearerScursGrnteeCd = bearerScursGrnteeCd;
  }

/**
	* 清償方案-自然人未約定「提前清償違約金」（不綁約）<br>
	* 000：不適用
LLP01：自然人未約定「提前清償違約金」（不綁約）
LLP02：自然人有約定「提前清償違約金」（不綁約）
999：無法提供相關資訊
	* @return String
	*/
  public String getAgreeDefaultFineCd() {
    return this.agreeDefaultFineCd == null ? "" : this.agreeDefaultFineCd;
  }

/**
	* 清償方案-自然人未約定「提前清償違約金」（不綁約）<br>
	* 000：不適用
LLP01：自然人未約定「提前清償違約金」（不綁約）
LLP02：自然人有約定「提前清償違約金」（不綁約）
999：無法提供相關資訊
  *
  * @param agreeDefaultFineCd 清償方案-自然人未約定「提前清償違約金」（不綁約）
	*/
  public void setAgreeDefaultFineCd(String agreeDefaultFineCd) {
    this.agreeDefaultFineCd = agreeDefaultFineCd;
  }

/**
	* 資金用途-資金用途非為購置不動產<br>
	* 000：不適用
LPE01：資金用途非為購置不動產
LPE02：資金用途為購置不動產
	* @return String
	*/
  public String getNonBuyingRealEstateCd() {
    return this.nonBuyingRealEstateCd == null ? "" : this.nonBuyingRealEstateCd;
  }

/**
	* 資金用途-資金用途非為購置不動產<br>
	* 000：不適用
LPE01：資金用途非為購置不動產
LPE02：資金用途為購置不動產
  *
  * @param nonBuyingRealEstateCd 資金用途-資金用途非為購置不動產
	*/
  public void setNonBuyingRealEstateCd(String nonBuyingRealEstateCd) {
    this.nonBuyingRealEstateCd = nonBuyingRealEstateCd;
  }

/**
	* 保證-非利害關係人提供保證<br>
	* 000：不適用
LMR01：非利害關係人提供保證
LMR02：為利害關係人提供保證
999：無法提供相關資訊
	* @return String
	*/
  public String getNonStkHolderGrnteeCd() {
    return this.nonStkHolderGrnteeCd == null ? "" : this.nonStkHolderGrnteeCd;
  }

/**
	* 保證-非利害關係人提供保證<br>
	* 000：不適用
LMR01：非利害關係人提供保證
LMR02：為利害關係人提供保證
999：無法提供相關資訊
  *
  * @param nonStkHolderGrnteeCd 保證-非利害關係人提供保證
	*/
  public void setNonStkHolderGrnteeCd(String nonStkHolderGrnteeCd) {
    this.nonStkHolderGrnteeCd = nonStkHolderGrnteeCd;
  }

/**
	* 多次借款-個人/法人申貸案件達一定筆數以上（含本次貸款申請）<br>
	* 提供額度編號與本案之合計筆數
	* @return Integer
	*/
  public int getReachCase() {
    return this.reachCase;
  }

/**
	* 多次借款-個人/法人申貸案件達一定筆數以上（含本次貸款申請）<br>
	* 提供額度編號與本案之合計筆數
  *
  * @param reachCase 多次借款-個人/法人申貸案件達一定筆數以上（含本次貸款申請）
	*/
  public void setReachCase(int reachCase) {
    this.reachCase = reachCase;
  }

/**
	* 受款帳戶-受款帳戶「非」本人或本人代償金融機構帳戶或履約保證或不動產出賣人帳戶<br>
	* 000：不適用
LRA01：受款帳戶「非」本人或本人代償金融機構帳戶或履約保證或不動產出賣人帳戶
LRA02：受款帳戶為本人或本人代償金融機構帳戶或履約保證或不動產出賣人帳戶
999：無法提供相關資訊
	* @return String
	*/
  public String getAccountTypeCd() {
    return this.accountTypeCd == null ? "" : this.accountTypeCd;
  }

/**
	* 受款帳戶-受款帳戶「非」本人或本人代償金融機構帳戶或履約保證或不動產出賣人帳戶<br>
	* 000：不適用
LRA01：受款帳戶「非」本人或本人代償金融機構帳戶或履約保證或不動產出賣人帳戶
LRA02：受款帳戶為本人或本人代償金融機構帳戶或履約保證或不動產出賣人帳戶
999：無法提供相關資訊
  *
  * @param accountTypeCd 受款帳戶-受款帳戶「非」本人或本人代償金融機構帳戶或履約保證或不動產出賣人帳戶
	*/
  public void setAccountTypeCd(String accountTypeCd) {
    this.accountTypeCd = accountTypeCd;
  }

/**
	* 區別要保人異動為暫時資料/確定資料<br>
	* 暫時：T
確定：S
	* @return String
	*/
  public String getQueryType() {
    return this.queryType == null ? "" : this.queryType;
  }

/**
	* 區別要保人異動為暫時資料/確定資料<br>
	* 暫時：T
確定：S
  *
  * @param queryType 區別要保人異動為暫時資料/確定資料
	*/
  public void setQueryType(String queryType) {
    this.queryType = queryType;
  }

/**
	* 身份別<br>
	* 1:自然人
2:法人
	* @return String
	*/
  public String getIdentityCd() {
    return this.identityCd == null ? "" : this.identityCd;
  }

/**
	* 身份別<br>
	* 1:自然人
2:法人
  *
  * @param identityCd 身份別
	*/
  public void setIdentityCd(String identityCd) {
    this.identityCd = identityCd;
  }

/**
	* Request Id<br>
	* 系統使用;各系統不需處理
	* @return String
	*/
  public String getRspRequestId() {
    return this.rspRequestId == null ? "" : this.rspRequestId;
  }

/**
	* Request Id<br>
	* 系統使用;各系統不需處理
  *
  * @param rspRequestId Request Id
	*/
  public void setRspRequestId(String rspRequestId) {
    this.rspRequestId = rspRequestId;
  }

/**
	* 狀態<br>
	* Success : 表示處理完成，無需登入AML作業
Warning : 表示需登入AML作業
Fail : 表示處理過程有異常，無法正常處理
	* @return String
	*/
  public String getRspStatus() {
    return this.rspStatus == null ? "" : this.rspStatus;
  }

/**
	* 狀態<br>
	* Success : 表示處理完成，無需登入AML作業
Warning : 表示需登入AML作業
Fail : 表示處理過程有異常，無法正常處理
  *
  * @param rspStatus 狀態
	*/
  public void setRspStatus(String rspStatus) {
    this.rspStatus = rspStatus;
  }

/**
	* 狀態代碼<br>
	* 如:0001
	* @return String
	*/
  public String getRspStatusCode() {
    return this.rspStatusCode == null ? "" : this.rspStatusCode;
  }

/**
	* 狀態代碼<br>
	* 如:0001
  *
  * @param rspStatusCode 狀態代碼
	*/
  public void setRspStatusCode(String rspStatusCode) {
    this.rspStatusCode = rspStatusCode;
  }

/**
	* 狀態說明<br>
	* 如：處理完成，無需進入AML作業
	* @return String
	*/
  public String getRspStatusDesc() {
    return this.rspStatusDesc == null ? "" : this.rspStatusDesc;
  }

/**
	* 狀態說明<br>
	* 如：處理完成，無需進入AML作業
  *
  * @param rspStatusDesc 狀態說明
	*/
  public void setRspStatusDesc(String rspStatusDesc) {
    this.rspStatusDesc = rspStatusDesc;
  }

/**
	* 查詢單位<br>
	* 引入Request內容
	* @return String
	*/
  public String getRspUnit() {
    return this.rspUnit == null ? "" : this.rspUnit;
  }

/**
	* 查詢單位<br>
	* 引入Request內容
  *
  * @param rspUnit 查詢單位
	*/
  public void setRspUnit(String rspUnit) {
    this.rspUnit = rspUnit;
  }

/**
	* AML 交易序號<br>
	* 引入Request內容
	* @return String
	*/
  public String getRspTransactionId() {
    return this.rspTransactionId == null ? "" : this.rspTransactionId;
  }

/**
	* AML 交易序號<br>
	* 引入Request內容
  *
  * @param rspTransactionId AML 交易序號
	*/
  public void setRspTransactionId(String rspTransactionId) {
    this.rspTransactionId = rspTransactionId;
  }

/**
	* 保單號碼<br>
	* 引入Request內容
	* @return String
	*/
  public String getRspAcctNo() {
    return this.rspAcctNo == null ? "" : this.rspAcctNo;
  }

/**
	* 保單號碼<br>
	* 引入Request內容
  *
  * @param rspAcctNo 保單號碼
	*/
  public void setRspAcctNo(String rspAcctNo) {
    this.rspAcctNo = rspAcctNo;
  }

/**
	* 案號<br>
	* 引入Request內容
	* @return String
	*/
  public String getRspCaseNo() {
    return this.rspCaseNo == null ? "" : this.rspCaseNo;
  }

/**
	* 案號<br>
	* 引入Request內容
  *
  * @param rspCaseNo 案號
	*/
  public void setRspCaseNo(String rspCaseNo) {
    this.rspCaseNo = rspCaseNo;
  }

/**
	* 投保次數<br>
	* 引入Request內容
	* @return Integer
	*/
  public int getRspInsurCount() {
    return this.rspInsurCount;
  }

/**
	* 投保次數<br>
	* 引入Request內容
  *
  * @param rspInsurCount 投保次數
	*/
  public void setRspInsurCount(int rspInsurCount) {
    this.rspInsurCount = rspInsurCount;
  }

/**
	* 分數<br>
	* 引入Request內容
	* @return BigDecimal
	*/
  public BigDecimal getRspTotalRatingsScore() {
    return this.rspTotalRatingsScore;
  }

/**
	* 分數<br>
	* 引入Request內容
  *
  * @param rspTotalRatingsScore 分數
	*/
  public void setRspTotalRatingsScore(BigDecimal rspTotalRatingsScore) {
    this.rspTotalRatingsScore = rspTotalRatingsScore;
  }

/**
	* 總評級(WLF+CDD)<br>
	* 若Is_Similar="Y"且同筆一案件第一次查詢時填入空白

若Is_Similar="Y"時或同一筆案件第二次查詢時填入
H:高
M:中
L:低
	* @return String
	*/
  public String getRspTotalRatings() {
    return this.rspTotalRatings == null ? "" : this.rspTotalRatings;
  }

/**
	* 總評級(WLF+CDD)<br>
	* 若Is_Similar="Y"且同筆一案件第一次查詢時填入空白

若Is_Similar="Y"時或同一筆案件第二次查詢時填入
H:高
M:中
L:低
  *
  * @param rspTotalRatings 總評級(WLF+CDD)
	*/
  public void setRspTotalRatings(String rspTotalRatings) {
    this.rspTotalRatings = rspTotalRatings;
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
    return "TxAmlRating [logNo=" + logNo + ", unit=" + unit + ", acceptanceUnit=" + acceptanceUnit + ", roleId=" + roleId + ", transactionId=" + transactionId + ", acctNo=" + acctNo
           + ", caseNo=" + caseNo + ", acctId=" + acctId + ", insurCount=" + insurCount + ", birthEstDt=" + birthEstDt + ", sourceId=" + sourceId + ", modifyDate=" + modifyDate
           + ", ocupCd=" + ocupCd + ", orgType=" + orgType + ", bcode=" + bcode + ", ocupNote=" + ocupNote + ", payMethod=" + payMethod + ", payType=" + payType
           + ", channel=" + channel + ", policyType=" + policyType + ", insuranceCurrency=" + insuranceCurrency + ", insuranceAmount=" + insuranceAmount + ", addnCd=" + addnCd + ", insrStakesCd=" + insrStakesCd
           + ", bnfcryNHdrGrpCd=" + bnfcryNHdrGrpCd + ", agentTradeCd=" + agentTradeCd + ", fstPayerNHdrGrpCd=" + fstPayerNHdrGrpCd + ", fstPayerNHdrStksCd=" + fstPayerNHdrStksCd + ", fstPrmOvrseaAcctCd=" + fstPrmOvrseaAcctCd + ", totalAmtCd=" + totalAmtCd
           + ", declinatureCd=" + declinatureCd + ", fstInsuredCd=" + fstInsuredCd + ", tWAddrHoldCd=" + tWAddrHoldCd + ", tWAddrLegalCd=" + tWAddrLegalCd + ", durationCd=" + durationCd + ", specialIdentity=" + specialIdentity
           + ", lawForceWarranty=" + lawForceWarranty + ", movableGrnteeCd=" + movableGrnteeCd + ", bearerScursGrnteeCd=" + bearerScursGrnteeCd + ", agreeDefaultFineCd=" + agreeDefaultFineCd + ", nonBuyingRealEstateCd=" + nonBuyingRealEstateCd + ", nonStkHolderGrnteeCd=" + nonStkHolderGrnteeCd
           + ", reachCase=" + reachCase + ", accountTypeCd=" + accountTypeCd + ", queryType=" + queryType + ", identityCd=" + identityCd + ", rspRequestId=" + rspRequestId + ", rspStatus=" + rspStatus
           + ", rspStatusCode=" + rspStatusCode + ", rspStatusDesc=" + rspStatusDesc + ", rspUnit=" + rspUnit + ", rspTransactionId=" + rspTransactionId + ", rspAcctNo=" + rspAcctNo + ", rspCaseNo=" + rspCaseNo
           + ", rspInsurCount=" + rspInsurCount + ", rspTotalRatingsScore=" + rspTotalRatingsScore + ", rspTotalRatings=" + rspTotalRatings + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
           + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
