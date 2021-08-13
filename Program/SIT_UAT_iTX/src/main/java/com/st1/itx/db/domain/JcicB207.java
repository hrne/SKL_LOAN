package com.st1.itx.db.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.EmbeddedId;
import javax.persistence.Column;

/**
 * JcicB207 聯徵授信戶基本資料檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicB207`")
public class JcicB207 implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 5673331022142520635L;

@EmbeddedId
  private JcicB207Id jcicB207Id;

  // 資料年月
  @Column(name = "`DataYM`", insertable = false, updatable = false)
  private int dataYM = 0;

  // 交易代碼
  /* A:新增C:異動(全部整檔報送時可使用此代號)D:刪除 */
  @Column(name = "`TranCode`", length = 1)
  private String tranCode;

  // 總行代號
  /* Key,金融機構總機構之代號，三位數字 */
  @Column(name = "`BankItem`", length = 3, insertable = false, updatable = false)
  private String bankItem;

  // 空白
  /* 備用 */
  @Column(name = "`Filler3`", length = 4)
  private String filler3;

  // 資料日期
  /* 以'YYYMMDD'(民國)表示 */
  @Column(name = "`DataDate`")
  private int dataDate = 0;

  // 授信戶IDN
  /* Key,左靠，身份證或統一證號 */
  @Column(name = "`CustId`", length = 10, insertable = false, updatable = false)
  private String custId;

  // 中文姓名
  /* 左靠，取前10個全形字，不足補英文空白(配合utf8:長度3 bytes) */
  @Column(name = "`CustName`", length = 30)
  private String custName;

  // 英文姓名
  /* 左靠，若無請填空白 */
  @Column(name = "`EName`", length = 20)
  private String eName;

  // 出生日期
  /* 以'YYYMMDD'(民國)表示 */
  @Column(name = "`Birthday`")
  private int birthday = 0;

  // 戶籍地址
  /* 左靠，取前33個全形字，不足補英文空白(配合utf8:長度3 bytes) */
  @Column(name = "`RegAddr`", length = 99)
  private String regAddr;

  // 聯絡地址郵遞區號
  @Column(name = "`CurrZip`", length = 5)
  private String currZip;

  // 聯絡地址
  /* 左靠，取前33個全形字，不足補英文空白(配合utf8:長度3 bytes) */
  @Column(name = "`CurrAddr`", length = 99)
  private String currAddr;

  // 聯絡電話
  /* 左靠右空白，含區域碼，建議格式:02-28225252#1688 */
  @Column(name = "`Tel`", length = 30)
  private String tel;

  // 行動電話
  /* 左靠右空白，含區域碼，建議格式:0951-123456 */
  @Column(name = "`Mobile`", length = 16)
  private String mobile;

  // 空白
  /* 備用 */
  @Column(name = "`Filler14`", length = 5)
  private String filler14;

  // 教育程度代號
  /* 附件二之二，無資料填空白，勿填01:博士 2:碩士 3:大學 4:專科 5:高中高職 6:其他 */
  @Column(name = "`EduCode`", length = 1)
  private String eduCode;

  // 自有住宅有無
  /* Y:有 N:無 */
  @Column(name = "`OwnedHome`", length = 1)
  private String ownedHome;

  // 任職機構名稱
  /* 取前15個全形字，不足補英文空白(配合utf8:長度3 bytes) */
  @Column(name = "`CurrCompName`", length = 45)
  private String currCompName;

  // 任職機構統一編號
  @Column(name = "`CurrCompId`", length = 8)
  private String currCompId;

  // 職業類別
  /* 附件二之一，學生:061410 */
  @Column(name = "`JobCode`", length = 6)
  private String jobCode;

  // 任職機構電話
  /* 左靠右空白，含區域碼，建議格式:02-28225252#1688 */
  @Column(name = "`CurrCompTel`", length = 16)
  private String currCompTel;

  // 職位名稱
  /* 左靠，取前5個全形字，不足補英文空白(配合utf8:長度3 bytes) */
  @Column(name = "`JobTitle`", length = 15)
  private String jobTitle;

  // 服務年資
  /* 右靠左補0，單位:年，不足一年以一年計填1，無資料填空白勿填0 */
  @Column(name = "`JobTenure`", length = 2)
  private String jobTenure;

  // 年收入
  /* 右靠左補0，單位千元，無收入者填000000 */
  @Column(name = "`IncomeOfYearly`")
  private int incomeOfYearly = 0;

  // 年收入資料年月
  /* 以'YYYMMDD'(民國)表示 */
  @Column(name = "`IncomeDataDate`")
  private int incomeDataDate = 0;

  // 性別
  /* 如第五欄填稅籍編號者(即西元出生年月日8位數+英文姓名前2位字母)，本欄位為必要填報項目，M:男 F:女 */
  @Column(name = "`Sex`", length = 1)
  private String sex;

  // 國籍
  /* 如第五欄填稅籍編號者(即西元出生年月日8位數+英文姓名前2位字母)，本欄位為必要填報項目，附件10 */
  @Column(name = "`NationalityCode`", length = 2)
  private String nationalityCode;

  // 護照號碼
  /* 如第五欄填稅籍編號者(即西元出生年月日8位數+英文姓名前2位字母)，本欄位為必要填報項目 */
  @Column(name = "`PassportNo`", length = 20)
  private String passportNo;

  // 舊有稅籍編號
  /* 如第五欄為統一證號且該戶於報送機構曾以稅籍編號建檔者，本欄位為必要填報項目 */
  @Column(name = "`PreTaxNo`", length = 10)
  private String preTaxNo;

  // 中文姓名超逾10個字之全名
  /* 左靠不足補英文空白，如授信戶姓名超逾10個全型字，於本欄填寫全名，如無前述情形填空白 */
  @Column(name = "`FullCustName`", length = 300)
  private String fullCustName;

  // 空白
  /* 備用 */
  @Column(name = "`Filler30`", length = 36)
  private String filler30;

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


  public JcicB207Id getJcicB207Id() {
    return this.jcicB207Id;
  }

  public void setJcicB207Id(JcicB207Id jcicB207Id) {
    this.jcicB207Id = jcicB207Id;
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
	* 交易代碼<br>
	* A:新增
C:異動(全部整檔報送時可使用此代號)
D:刪除
	* @return String
	*/
  public String getTranCode() {
    return this.tranCode == null ? "" : this.tranCode;
  }

/**
	* 交易代碼<br>
	* A:新增
C:異動(全部整檔報送時可使用此代號)
D:刪除
  *
  * @param tranCode 交易代碼
	*/
  public void setTranCode(String tranCode) {
    this.tranCode = tranCode;
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
	* 空白<br>
	* 備用
	* @return String
	*/
  public String getFiller3() {
    return this.filler3 == null ? "" : this.filler3;
  }

/**
	* 空白<br>
	* 備用
  *
  * @param filler3 空白
	*/
  public void setFiller3(String filler3) {
    this.filler3 = filler3;
  }

/**
	* 資料日期<br>
	* 以'YYYMMDD'(民國)表示
	* @return Integer
	*/
  public int getDataDate() {
    return this.dataDate;
  }

/**
	* 資料日期<br>
	* 以'YYYMMDD'(民國)表示
  *
  * @param dataDate 資料日期
	*/
  public void setDataDate(int dataDate) {
    this.dataDate = dataDate;
  }

/**
	* 授信戶IDN<br>
	* Key,左靠，身份證或統一證號
	* @return String
	*/
  public String getCustId() {
    return this.custId == null ? "" : this.custId;
  }

/**
	* 授信戶IDN<br>
	* Key,左靠，身份證或統一證號
  *
  * @param custId 授信戶IDN
	*/
  public void setCustId(String custId) {
    this.custId = custId;
  }

/**
	* 中文姓名<br>
	* 左靠，取前10個全形字，不足補英文空白(配合utf8:長度3 bytes)
	* @return String
	*/
  public String getCustName() {
    return this.custName == null ? "" : this.custName;
  }

/**
	* 中文姓名<br>
	* 左靠，取前10個全形字，不足補英文空白(配合utf8:長度3 bytes)
  *
  * @param custName 中文姓名
	*/
  public void setCustName(String custName) {
    this.custName = custName;
  }

/**
	* 英文姓名<br>
	* 左靠，若無請填空白
	* @return String
	*/
  public String getEName() {
    return this.eName == null ? "" : this.eName;
  }

/**
	* 英文姓名<br>
	* 左靠，若無請填空白
  *
  * @param eName 英文姓名
	*/
  public void setEName(String eName) {
    this.eName = eName;
  }

/**
	* 出生日期<br>
	* 以'YYYMMDD'(民國)表示
	* @return Integer
	*/
  public int getBirthday() {
    return this.birthday;
  }

/**
	* 出生日期<br>
	* 以'YYYMMDD'(民國)表示
  *
  * @param birthday 出生日期
	*/
  public void setBirthday(int birthday) {
    this.birthday = birthday;
  }

/**
	* 戶籍地址<br>
	* 左靠，取前33個全形字，不足補英文空白(配合utf8:長度3 bytes)
	* @return String
	*/
  public String getRegAddr() {
    return this.regAddr == null ? "" : this.regAddr;
  }

/**
	* 戶籍地址<br>
	* 左靠，取前33個全形字，不足補英文空白(配合utf8:長度3 bytes)
  *
  * @param regAddr 戶籍地址
	*/
  public void setRegAddr(String regAddr) {
    this.regAddr = regAddr;
  }

/**
	* 聯絡地址郵遞區號<br>
	* 
	* @return String
	*/
  public String getCurrZip() {
    return this.currZip == null ? "" : this.currZip;
  }

/**
	* 聯絡地址郵遞區號<br>
	* 
  *
  * @param currZip 聯絡地址郵遞區號
	*/
  public void setCurrZip(String currZip) {
    this.currZip = currZip;
  }

/**
	* 聯絡地址<br>
	* 左靠，取前33個全形字，不足補英文空白(配合utf8:長度3 bytes)
	* @return String
	*/
  public String getCurrAddr() {
    return this.currAddr == null ? "" : this.currAddr;
  }

/**
	* 聯絡地址<br>
	* 左靠，取前33個全形字，不足補英文空白(配合utf8:長度3 bytes)
  *
  * @param currAddr 聯絡地址
	*/
  public void setCurrAddr(String currAddr) {
    this.currAddr = currAddr;
  }

/**
	* 聯絡電話<br>
	* 左靠右空白，含區域碼，建議格式:02-28225252#1688
	* @return String
	*/
  public String getTel() {
    return this.tel == null ? "" : this.tel;
  }

/**
	* 聯絡電話<br>
	* 左靠右空白，含區域碼，建議格式:02-28225252#1688
  *
  * @param tel 聯絡電話
	*/
  public void setTel(String tel) {
    this.tel = tel;
  }

/**
	* 行動電話<br>
	* 左靠右空白，含區域碼，建議格式:0951-123456
	* @return String
	*/
  public String getMobile() {
    return this.mobile == null ? "" : this.mobile;
  }

/**
	* 行動電話<br>
	* 左靠右空白，含區域碼，建議格式:0951-123456
  *
  * @param mobile 行動電話
	*/
  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

/**
	* 空白<br>
	* 備用
	* @return String
	*/
  public String getFiller14() {
    return this.filler14 == null ? "" : this.filler14;
  }

/**
	* 空白<br>
	* 備用
  *
  * @param filler14 空白
	*/
  public void setFiller14(String filler14) {
    this.filler14 = filler14;
  }

/**
	* 教育程度代號<br>
	* 附件二之二，無資料填空白，勿填0
1:博士 2:碩士 3:大學 4:專科 5:高中高職 6:其他
	* @return String
	*/
  public String getEduCode() {
    return this.eduCode == null ? "" : this.eduCode;
  }

/**
	* 教育程度代號<br>
	* 附件二之二，無資料填空白，勿填0
1:博士 2:碩士 3:大學 4:專科 5:高中高職 6:其他
  *
  * @param eduCode 教育程度代號
	*/
  public void setEduCode(String eduCode) {
    this.eduCode = eduCode;
  }

/**
	* 自有住宅有無<br>
	* Y:有 N:無
	* @return String
	*/
  public String getOwnedHome() {
    return this.ownedHome == null ? "" : this.ownedHome;
  }

/**
	* 自有住宅有無<br>
	* Y:有 N:無
  *
  * @param ownedHome 自有住宅有無
	*/
  public void setOwnedHome(String ownedHome) {
    this.ownedHome = ownedHome;
  }

/**
	* 任職機構名稱<br>
	* 取前15個全形字，不足補英文空白(配合utf8:長度3 bytes)
	* @return String
	*/
  public String getCurrCompName() {
    return this.currCompName == null ? "" : this.currCompName;
  }

/**
	* 任職機構名稱<br>
	* 取前15個全形字，不足補英文空白(配合utf8:長度3 bytes)
  *
  * @param currCompName 任職機構名稱
	*/
  public void setCurrCompName(String currCompName) {
    this.currCompName = currCompName;
  }

/**
	* 任職機構統一編號<br>
	* 
	* @return String
	*/
  public String getCurrCompId() {
    return this.currCompId == null ? "" : this.currCompId;
  }

/**
	* 任職機構統一編號<br>
	* 
  *
  * @param currCompId 任職機構統一編號
	*/
  public void setCurrCompId(String currCompId) {
    this.currCompId = currCompId;
  }

/**
	* 職業類別<br>
	* 附件二之一，學生:061410
	* @return String
	*/
  public String getJobCode() {
    return this.jobCode == null ? "" : this.jobCode;
  }

/**
	* 職業類別<br>
	* 附件二之一，學生:061410
  *
  * @param jobCode 職業類別
	*/
  public void setJobCode(String jobCode) {
    this.jobCode = jobCode;
  }

/**
	* 任職機構電話<br>
	* 左靠右空白，含區域碼，建議格式:02-28225252#1688
	* @return String
	*/
  public String getCurrCompTel() {
    return this.currCompTel == null ? "" : this.currCompTel;
  }

/**
	* 任職機構電話<br>
	* 左靠右空白，含區域碼，建議格式:02-28225252#1688
  *
  * @param currCompTel 任職機構電話
	*/
  public void setCurrCompTel(String currCompTel) {
    this.currCompTel = currCompTel;
  }

/**
	* 職位名稱<br>
	* 左靠，取前5個全形字，不足補英文空白(配合utf8:長度3 bytes)
	* @return String
	*/
  public String getJobTitle() {
    return this.jobTitle == null ? "" : this.jobTitle;
  }

/**
	* 職位名稱<br>
	* 左靠，取前5個全形字，不足補英文空白(配合utf8:長度3 bytes)
  *
  * @param jobTitle 職位名稱
	*/
  public void setJobTitle(String jobTitle) {
    this.jobTitle = jobTitle;
  }

/**
	* 服務年資<br>
	* 右靠左補0，單位:年，不足一年以一年計填1，無資料填空白勿填0
	* @return String
	*/
  public String getJobTenure() {
    return this.jobTenure == null ? "" : this.jobTenure;
  }

/**
	* 服務年資<br>
	* 右靠左補0，單位:年，不足一年以一年計填1，無資料填空白勿填0
  *
  * @param jobTenure 服務年資
	*/
  public void setJobTenure(String jobTenure) {
    this.jobTenure = jobTenure;
  }

/**
	* 年收入<br>
	* 右靠左補0，單位千元，無收入者填000000
	* @return Integer
	*/
  public int getIncomeOfYearly() {
    return this.incomeOfYearly;
  }

/**
	* 年收入<br>
	* 右靠左補0，單位千元，無收入者填000000
  *
  * @param incomeOfYearly 年收入
	*/
  public void setIncomeOfYearly(int incomeOfYearly) {
    this.incomeOfYearly = incomeOfYearly;
  }

/**
	* 年收入資料年月<br>
	* 以'YYYMMDD'(民國)表示
	* @return Integer
	*/
  public int getIncomeDataDate() {
    return this.incomeDataDate;
  }

/**
	* 年收入資料年月<br>
	* 以'YYYMMDD'(民國)表示
  *
  * @param incomeDataDate 年收入資料年月
	*/
  public void setIncomeDataDate(int incomeDataDate) {
    this.incomeDataDate = incomeDataDate;
  }

/**
	* 性別<br>
	* 如第五欄填稅籍編號者(即西元出生年月日8位數+英文姓名前2位字母)，本欄位為必要填報項目，M:男 F:女
	* @return String
	*/
  public String getSex() {
    return this.sex == null ? "" : this.sex;
  }

/**
	* 性別<br>
	* 如第五欄填稅籍編號者(即西元出生年月日8位數+英文姓名前2位字母)，本欄位為必要填報項目，M:男 F:女
  *
  * @param sex 性別
	*/
  public void setSex(String sex) {
    this.sex = sex;
  }

/**
	* 國籍<br>
	* 如第五欄填稅籍編號者(即西元出生年月日8位數+英文姓名前2位字母)，本欄位為必要填報項目，附件10
	* @return String
	*/
  public String getNationalityCode() {
    return this.nationalityCode == null ? "" : this.nationalityCode;
  }

/**
	* 國籍<br>
	* 如第五欄填稅籍編號者(即西元出生年月日8位數+英文姓名前2位字母)，本欄位為必要填報項目，附件10
  *
  * @param nationalityCode 國籍
	*/
  public void setNationalityCode(String nationalityCode) {
    this.nationalityCode = nationalityCode;
  }

/**
	* 護照號碼<br>
	* 如第五欄填稅籍編號者(即西元出生年月日8位數+英文姓名前2位字母)，本欄位為必要填報項目
	* @return String
	*/
  public String getPassportNo() {
    return this.passportNo == null ? "" : this.passportNo;
  }

/**
	* 護照號碼<br>
	* 如第五欄填稅籍編號者(即西元出生年月日8位數+英文姓名前2位字母)，本欄位為必要填報項目
  *
  * @param passportNo 護照號碼
	*/
  public void setPassportNo(String passportNo) {
    this.passportNo = passportNo;
  }

/**
	* 舊有稅籍編號<br>
	* 如第五欄為統一證號且該戶於報送機構曾以稅籍編號建檔者，本欄位為必要填報項目
	* @return String
	*/
  public String getPreTaxNo() {
    return this.preTaxNo == null ? "" : this.preTaxNo;
  }

/**
	* 舊有稅籍編號<br>
	* 如第五欄為統一證號且該戶於報送機構曾以稅籍編號建檔者，本欄位為必要填報項目
  *
  * @param preTaxNo 舊有稅籍編號
	*/
  public void setPreTaxNo(String preTaxNo) {
    this.preTaxNo = preTaxNo;
  }

/**
	* 中文姓名超逾10個字之全名<br>
	* 左靠不足補英文空白，如授信戶姓名超逾10個全型字，於本欄填寫全名，如無前述情形填空白
	* @return String
	*/
  public String getFullCustName() {
    return this.fullCustName == null ? "" : this.fullCustName;
  }

/**
	* 中文姓名超逾10個字之全名<br>
	* 左靠不足補英文空白，如授信戶姓名超逾10個全型字，於本欄填寫全名，如無前述情形填空白
  *
  * @param fullCustName 中文姓名超逾10個字之全名
	*/
  public void setFullCustName(String fullCustName) {
    this.fullCustName = fullCustName;
  }

/**
	* 空白<br>
	* 備用
	* @return String
	*/
  public String getFiller30() {
    return this.filler30 == null ? "" : this.filler30;
  }

/**
	* 空白<br>
	* 備用
  *
  * @param filler30 空白
	*/
  public void setFiller30(String filler30) {
    this.filler30 = filler30;
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
    return "JcicB207 [jcicB207Id=" + jcicB207Id + ", tranCode=" + tranCode + ", filler3=" + filler3 + ", dataDate=" + dataDate
           + ", custName=" + custName + ", eName=" + eName + ", birthday=" + birthday + ", regAddr=" + regAddr + ", currZip=" + currZip + ", currAddr=" + currAddr
           + ", tel=" + tel + ", mobile=" + mobile + ", filler14=" + filler14 + ", eduCode=" + eduCode + ", ownedHome=" + ownedHome + ", currCompName=" + currCompName
           + ", currCompId=" + currCompId + ", jobCode=" + jobCode + ", currCompTel=" + currCompTel + ", jobTitle=" + jobTitle + ", jobTenure=" + jobTenure + ", incomeOfYearly=" + incomeOfYearly
           + ", incomeDataDate=" + incomeDataDate + ", sex=" + sex + ", nationalityCode=" + nationalityCode + ", passportNo=" + passportNo + ", preTaxNo=" + preTaxNo + ", fullCustName=" + fullCustName
           + ", filler30=" + filler30 + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
