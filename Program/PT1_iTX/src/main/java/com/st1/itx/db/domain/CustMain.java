package com.st1.itx.db.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.Id;
import javax.persistence.Column;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * CustMain 客戶資料主檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CustMain`")
public class CustMain implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -4978593393417917685L;

// 客戶識別碼
  @Id
  @Column(name = "`CustUKey`", length = 32)
  private String custUKey = " ";

  // 身份證字號/統一編號
  @Column(name = "`CustId`", length = 10)
  private String custId;

  // 戶號
  /* 無戶號者為0 */
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 單位別
  @Column(name = "`BranchNo`", length = 4)
  private String branchNo;

  // 戶名/公司名稱
  @Column(name = "`CustName`", length = 100)
  private String custName;

  // 出生年月日/設立日期
  @Column(name = "`Birthday`")
  private int birthday = 0;

  // 性別
  /* 1:男性2:女性 */
  @Column(name = "`Sex`", length = 1)
  private String sex;

  // 客戶別
  /* 共用代碼檔00:一般01:員工02:首購03:關企公司04:關企員工05:保戶06:團體戶07:員工二親等09:新二階員工 */
  @Column(name = "`CustTypeCode`", length = 2)
  private String custTypeCode;

  // 行業別
  /* 位數不足6碼時，前補零行業別對照檔CdIndustry */
  @Column(name = "`IndustryCode`", length = 6)
  private String industryCode;

  // 自然人:出生地國籍/法人:註冊地國籍
  /* 國籍對照檔 */
  @Column(name = "`NationalityCode`", length = 2)
  private String nationalityCode;

  // 自然人:居住地國籍/法人:營業地國籍
  /* 國籍對照檔比照ELOAN(2021/08/27上DB)by eric */
  @Column(name = "`BussNationalityCode`", length = 2)
  private String bussNationalityCode;

  // 配偶身份證號/負責人身分證
  @Column(name = "`SpouseId`", length = 10)
  private String spouseId;

  // 配偶姓名/負責人姓名
  @Column(name = "`SpouseName`", length = 100)
  private String spouseName;

  // 戶籍/公司-郵遞區號前三碼
  @Column(name = "`RegZip3`", length = 3)
  private String regZip3;

  // 戶籍/公司-郵遞區號後三碼
  @Column(name = "`RegZip2`", length = 3)
  private String regZip2;

  // 戶籍/公司-縣市代碼
  /* 地區別與鄉鎮區對照檔CdArea */
  @Column(name = "`RegCityCode`", length = 2)
  private String regCityCode;

  // 戶籍/公司-鄉鎮市區代碼
  /* 地區別與鄉鎮區對照檔CdArea */
  @Column(name = "`RegAreaCode`", length = 3)
  private String regAreaCode;

  // 戶籍/公司-路名
  @Column(name = "`RegRoad`", length = 40)
  private String regRoad;

  // 戶籍/公司-段
  @Column(name = "`RegSection`", length = 5)
  private String regSection;

  // 戶籍/公司-巷
  @Column(name = "`RegAlley`", length = 5)
  private String regAlley;

  // 戶籍/公司-弄
  @Column(name = "`RegLane`", length = 5)
  private String regLane;

  // 戶籍/公司-號
  @Column(name = "`RegNum`", length = 5)
  private String regNum;

  // 戶籍/公司-號之
  @Column(name = "`RegNumDash`", length = 5)
  private String regNumDash;

  // 戶籍/公司-樓
  @Column(name = "`RegFloor`", length = 5)
  private String regFloor;

  // 戶籍/公司-樓之
  @Column(name = "`RegFloorDash`", length = 5)
  private String regFloorDash;

  // 通訊-郵遞區號前三碼
  @Column(name = "`CurrZip3`", length = 3)
  private String currZip3;

  // 通訊-郵遞區號後三碼
  @Column(name = "`CurrZip2`", length = 3)
  private String currZip2;

  // 通訊-縣市代碼
  /* 地區別與鄉鎮區對照檔CdArea */
  @Column(name = "`CurrCityCode`", length = 2)
  private String currCityCode;

  // 通訊-鄉鎮市區代碼
  /* 地區別與鄉鎮區對照檔CdArea */
  @Column(name = "`CurrAreaCode`", length = 3)
  private String currAreaCode;

  // 通訊-路名
  @Column(name = "`CurrRoad`", length = 40)
  private String currRoad;

  // 通訊-段
  @Column(name = "`CurrSection`", length = 5)
  private String currSection;

  // 通訊-巷
  @Column(name = "`CurrAlley`", length = 5)
  private String currAlley;

  // 通訊-弄
  @Column(name = "`CurrLane`", length = 5)
  private String currLane;

  // 通訊-號
  @Column(name = "`CurrNum`", length = 5)
  private String currNum;

  // 通訊-號之
  @Column(name = "`CurrNumDash`", length = 5)
  private String currNumDash;

  // 通訊-樓
  @Column(name = "`CurrFloor`", length = 5)
  private String currFloor;

  // 通訊-樓之
  @Column(name = "`CurrFloorDash`", length = 5)
  private String currFloorDash;

  // 身份別
  /* 1:自然人2:法人2021.10.15 by eric */
  @Column(name = "`CuscCd`", length = 1)
  private String cuscCd;

  // 企金別
  /* 共用代碼檔0:個金1:企金2:企金自然人 */
  @Column(name = "`EntCode`", length = 1)
  private String entCode;

  // 員工代號
  /* 若此客戶為員工才放該員工的員工代號 */
  @Column(name = "`EmpNo`", length = 6)
  private String empNo;

  // 英文姓名
  @Column(name = "`EName`", length = 50)
  private String eName;

  // 教育程度代號
  /* 共用代碼檔1:小學以下2:國中3:高中職4:專科學校5:大學6:研究所7:博士 */
  @Column(name = "`EduCode`", length = 1)
  private String eduCode;

  // 自有住宅有無
  /* Y:是N:否 */
  @Column(name = "`OwnedHome`", length = 1)
  private String ownedHome;

  // 任職機構名稱
  @Column(name = "`CurrCompName`", length = 60)
  private String currCompName;

  // 任職機構統編
  @Column(name = "`CurrCompId`", length = 8)
  private String currCompId;

  // 任職機構電話
  @Column(name = "`CurrCompTel`", length = 16)
  private String currCompTel;

  // 職位名稱
  @Column(name = "`JobTitle`", length = 20)
  private String jobTitle;

  // 服務年資
  @Column(name = "`JobTenure`", length = 2)
  private String jobTenure;

  // 年收入
  @Column(name = "`IncomeOfYearly`")
  private int incomeOfYearly = 0;

  // 年收入資料年月
  /* 西元年月 */
  @Column(name = "`IncomeDataDate`", length = 6)
  private String incomeDataDate;

  // 護照號碼
  @Column(name = "`PassportNo`", length = 20)
  private String passportNo;

  // AML職業別
  @Column(name = "`AMLJobCode`", length = 3)
  private String aMLJobCode;

  // AML組織
  @Column(name = "`AMLGroup`", length = 3)
  private String aMLGroup;

  // 原住民姓名
  @Column(name = "`IndigenousName`", length = 100)
  private String indigenousName;

  // 已編額度編號
  @Column(name = "`LastFacmNo`")
  private int lastFacmNo = 0;

  // 已編聯貸案序號
  @Column(name = "`LastSyndNo`")
  private int lastSyndNo = 0;

  // 開放查詢
  /* 1:不開放 2:開放 */
  @Column(name = "`AllowInquire`", length = 1)
  private String allowInquire;

  // 電子信箱
  @Column(name = "`Email`", length = 50)
  private String email;

  // 交易進行記號
  /* 0:1STEP TX 1/2:2STEP TX */
  @Column(name = "`ActFg`")
  private int actFg = 0;

  // 介紹人
  /* 2021/01/07新增欄位(2021/01/14上DB) */
  @Column(name = "`Introducer`", length = 6)
  private String introducer;

  // 房貸專員/企金人員
  /* 2022/02/17新增欄位 by eric原DAT_CU$CUSP.CUSEM2 */
  @Column(name = "`BusinessOfficer`", length = 6)
  private String businessOfficer;

  // 是否為金控「疑似準利害關係人」名單
  /* Y/N2021/08/06新增欄位(2021/08/06上DB)by eric */
  @Column(name = "`IsSuspected`", length = 1)
  private String isSuspected;

  // 是否為金控疑似利害關係人
  /* Y/N2021/08/06新增欄位(2021/08/06上DB)by eric */
  @Column(name = "`IsSuspectedCheck`", length = 1)
  private String isSuspectedCheck;

  // 是否為金控疑似利害關係人_確認狀態
  /* Y/N2021/08/06新增欄位(2021/08/06上DB)by eric */
  @Column(name = "`IsSuspectedCheckType`", length = 1)
  private String isSuspectedCheckType;

  // 是否為授信限制對象
  /* Y/N2022/3/24新增欄位by eric */
  @Column(name = "`IsLimit`", length = 1)
  private String isLimit;

  // 是否為利害關係人
  /* Y/N2022/3/24新增欄位by eric */
  @Column(name = "`IsRelated`", length = 1)
  private String isRelated;

  // 是否為準利害關係人
  /* Y/N2022/3/24新增欄位by eric */
  @Column(name = "`IsLnrelNear`", length = 1)
  private String isLnrelNear;

  // 是否資訊日期
  /* 2022/3/24新增欄位by eric */
  @Column(name = "`IsDate`")
  private int isDate = 0;

  // 資料狀態
  /* 0:已完成建檔1:未完成建檔(2021/08/13上DB)by eric */
  @Column(name = "`DataStatus`")
  private int dataStatus = 0;

  // 建檔身分別
  /* 0:借戶1:保證人2:擔保品提供人3:交易關係人4:借款戶關係人2021/08/24新增欄位(2021/08/25上DB)by eric(2021/09/17)新增4. by昱衡 */
  @Column(name = "`TypeCode`")
  private int typeCode = 0;

  // 站別
  /* 2022/02/17新增欄位 by eric原DAT_CU$CUSP.CUSSTN */
  @Column(name = "`Station`", length = 3)
  private String station;

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
	* 客戶識別碼<br>
	* 
	* @return String
	*/
  public String getCustUKey() {
    return this.custUKey == null ? "" : this.custUKey;
  }

/**
	* 客戶識別碼<br>
	* 
  *
  * @param custUKey 客戶識別碼
	*/
  public void setCustUKey(String custUKey) {
    this.custUKey = custUKey;
  }

/**
	* 身份證字號/統一編號<br>
	* 
	* @return String
	*/
  public String getCustId() {
    return this.custId == null ? "" : this.custId;
  }

/**
	* 身份證字號/統一編號<br>
	* 
  *
  * @param custId 身份證字號/統一編號
	*/
  public void setCustId(String custId) {
    this.custId = custId;
  }

/**
	* 戶號<br>
	* 無戶號者為0
	* @return Integer
	*/
  public int getCustNo() {
    return this.custNo;
  }

/**
	* 戶號<br>
	* 無戶號者為0
  *
  * @param custNo 戶號
	*/
  public void setCustNo(int custNo) {
    this.custNo = custNo;
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
	* 戶名/公司名稱<br>
	* 
	* @return String
	*/
  public String getCustName() {
    return this.custName == null ? "" : this.custName;
  }

/**
	* 戶名/公司名稱<br>
	* 
  *
  * @param custName 戶名/公司名稱
	*/
  public void setCustName(String custName) {
    this.custName = custName;
  }

/**
	* 出生年月日/設立日期<br>
	* 
	* @return Integer
	*/
  public int getBirthday() {
    return StaticTool.bcToRoc(this.birthday);
  }

/**
	* 出生年月日/設立日期<br>
	* 
  *
  * @param birthday 出生年月日/設立日期
  * @throws LogicException when Date Is Warn	*/
  public void setBirthday(int birthday) throws LogicException {
    this.birthday = StaticTool.rocToBc(birthday);
  }

/**
	* 性別<br>
	* 1:男性
2:女性
	* @return String
	*/
  public String getSex() {
    return this.sex == null ? "" : this.sex;
  }

/**
	* 性別<br>
	* 1:男性
2:女性
  *
  * @param sex 性別
	*/
  public void setSex(String sex) {
    this.sex = sex;
  }

/**
	* 客戶別<br>
	* 共用代碼檔
00:一般
01:員工
02:首購
03:關企公司
04:關企員工
05:保戶
06:團體戶
07:員工二親等
09:新二階員工
	* @return String
	*/
  public String getCustTypeCode() {
    return this.custTypeCode == null ? "" : this.custTypeCode;
  }

/**
	* 客戶別<br>
	* 共用代碼檔
00:一般
01:員工
02:首購
03:關企公司
04:關企員工
05:保戶
06:團體戶
07:員工二親等
09:新二階員工
  *
  * @param custTypeCode 客戶別
	*/
  public void setCustTypeCode(String custTypeCode) {
    this.custTypeCode = custTypeCode;
  }

/**
	* 行業別<br>
	* 位數不足6碼時，前補零
行業別對照檔CdIndustry
	* @return String
	*/
  public String getIndustryCode() {
    return this.industryCode == null ? "" : this.industryCode;
  }

/**
	* 行業別<br>
	* 位數不足6碼時，前補零
行業別對照檔CdIndustry
  *
  * @param industryCode 行業別
	*/
  public void setIndustryCode(String industryCode) {
    this.industryCode = industryCode;
  }

/**
	* 自然人:出生地國籍/法人:註冊地國籍<br>
	* 國籍對照檔
	* @return String
	*/
  public String getNationalityCode() {
    return this.nationalityCode == null ? "" : this.nationalityCode;
  }

/**
	* 自然人:出生地國籍/法人:註冊地國籍<br>
	* 國籍對照檔
  *
  * @param nationalityCode 自然人:出生地國籍/法人:註冊地國籍
	*/
  public void setNationalityCode(String nationalityCode) {
    this.nationalityCode = nationalityCode;
  }

/**
	* 自然人:居住地國籍/法人:營業地國籍<br>
	* 國籍對照檔
比照ELOAN
(2021/08/27上DB)by eric
	* @return String
	*/
  public String getBussNationalityCode() {
    return this.bussNationalityCode == null ? "" : this.bussNationalityCode;
  }

/**
	* 自然人:居住地國籍/法人:營業地國籍<br>
	* 國籍對照檔
比照ELOAN
(2021/08/27上DB)by eric
  *
  * @param bussNationalityCode 自然人:居住地國籍/法人:營業地國籍
	*/
  public void setBussNationalityCode(String bussNationalityCode) {
    this.bussNationalityCode = bussNationalityCode;
  }

/**
	* 配偶身份證號/負責人身分證<br>
	* 
	* @return String
	*/
  public String getSpouseId() {
    return this.spouseId == null ? "" : this.spouseId;
  }

/**
	* 配偶身份證號/負責人身分證<br>
	* 
  *
  * @param spouseId 配偶身份證號/負責人身分證
	*/
  public void setSpouseId(String spouseId) {
    this.spouseId = spouseId;
  }

/**
	* 配偶姓名/負責人姓名<br>
	* 
	* @return String
	*/
  public String getSpouseName() {
    return this.spouseName == null ? "" : this.spouseName;
  }

/**
	* 配偶姓名/負責人姓名<br>
	* 
  *
  * @param spouseName 配偶姓名/負責人姓名
	*/
  public void setSpouseName(String spouseName) {
    this.spouseName = spouseName;
  }

/**
	* 戶籍/公司-郵遞區號前三碼<br>
	* 
	* @return String
	*/
  public String getRegZip3() {
    return this.regZip3 == null ? "" : this.regZip3;
  }

/**
	* 戶籍/公司-郵遞區號前三碼<br>
	* 
  *
  * @param regZip3 戶籍/公司-郵遞區號前三碼
	*/
  public void setRegZip3(String regZip3) {
    this.regZip3 = regZip3;
  }

/**
	* 戶籍/公司-郵遞區號後三碼<br>
	* 
	* @return String
	*/
  public String getRegZip2() {
    return this.regZip2 == null ? "" : this.regZip2;
  }

/**
	* 戶籍/公司-郵遞區號後三碼<br>
	* 
  *
  * @param regZip2 戶籍/公司-郵遞區號後三碼
	*/
  public void setRegZip2(String regZip2) {
    this.regZip2 = regZip2;
  }

/**
	* 戶籍/公司-縣市代碼<br>
	* 地區別與鄉鎮區對照檔CdArea
	* @return String
	*/
  public String getRegCityCode() {
    return this.regCityCode == null ? "" : this.regCityCode;
  }

/**
	* 戶籍/公司-縣市代碼<br>
	* 地區別與鄉鎮區對照檔CdArea
  *
  * @param regCityCode 戶籍/公司-縣市代碼
	*/
  public void setRegCityCode(String regCityCode) {
    this.regCityCode = regCityCode;
  }

/**
	* 戶籍/公司-鄉鎮市區代碼<br>
	* 地區別與鄉鎮區對照檔CdArea
	* @return String
	*/
  public String getRegAreaCode() {
    return this.regAreaCode == null ? "" : this.regAreaCode;
  }

/**
	* 戶籍/公司-鄉鎮市區代碼<br>
	* 地區別與鄉鎮區對照檔CdArea
  *
  * @param regAreaCode 戶籍/公司-鄉鎮市區代碼
	*/
  public void setRegAreaCode(String regAreaCode) {
    this.regAreaCode = regAreaCode;
  }

/**
	* 戶籍/公司-路名<br>
	* 
	* @return String
	*/
  public String getRegRoad() {
    return this.regRoad == null ? "" : this.regRoad;
  }

/**
	* 戶籍/公司-路名<br>
	* 
  *
  * @param regRoad 戶籍/公司-路名
	*/
  public void setRegRoad(String regRoad) {
    this.regRoad = regRoad;
  }

/**
	* 戶籍/公司-段<br>
	* 
	* @return String
	*/
  public String getRegSection() {
    return this.regSection == null ? "" : this.regSection;
  }

/**
	* 戶籍/公司-段<br>
	* 
  *
  * @param regSection 戶籍/公司-段
	*/
  public void setRegSection(String regSection) {
    this.regSection = regSection;
  }

/**
	* 戶籍/公司-巷<br>
	* 
	* @return String
	*/
  public String getRegAlley() {
    return this.regAlley == null ? "" : this.regAlley;
  }

/**
	* 戶籍/公司-巷<br>
	* 
  *
  * @param regAlley 戶籍/公司-巷
	*/
  public void setRegAlley(String regAlley) {
    this.regAlley = regAlley;
  }

/**
	* 戶籍/公司-弄<br>
	* 
	* @return String
	*/
  public String getRegLane() {
    return this.regLane == null ? "" : this.regLane;
  }

/**
	* 戶籍/公司-弄<br>
	* 
  *
  * @param regLane 戶籍/公司-弄
	*/
  public void setRegLane(String regLane) {
    this.regLane = regLane;
  }

/**
	* 戶籍/公司-號<br>
	* 
	* @return String
	*/
  public String getRegNum() {
    return this.regNum == null ? "" : this.regNum;
  }

/**
	* 戶籍/公司-號<br>
	* 
  *
  * @param regNum 戶籍/公司-號
	*/
  public void setRegNum(String regNum) {
    this.regNum = regNum;
  }

/**
	* 戶籍/公司-號之<br>
	* 
	* @return String
	*/
  public String getRegNumDash() {
    return this.regNumDash == null ? "" : this.regNumDash;
  }

/**
	* 戶籍/公司-號之<br>
	* 
  *
  * @param regNumDash 戶籍/公司-號之
	*/
  public void setRegNumDash(String regNumDash) {
    this.regNumDash = regNumDash;
  }

/**
	* 戶籍/公司-樓<br>
	* 
	* @return String
	*/
  public String getRegFloor() {
    return this.regFloor == null ? "" : this.regFloor;
  }

/**
	* 戶籍/公司-樓<br>
	* 
  *
  * @param regFloor 戶籍/公司-樓
	*/
  public void setRegFloor(String regFloor) {
    this.regFloor = regFloor;
  }

/**
	* 戶籍/公司-樓之<br>
	* 
	* @return String
	*/
  public String getRegFloorDash() {
    return this.regFloorDash == null ? "" : this.regFloorDash;
  }

/**
	* 戶籍/公司-樓之<br>
	* 
  *
  * @param regFloorDash 戶籍/公司-樓之
	*/
  public void setRegFloorDash(String regFloorDash) {
    this.regFloorDash = regFloorDash;
  }

/**
	* 通訊-郵遞區號前三碼<br>
	* 
	* @return String
	*/
  public String getCurrZip3() {
    return this.currZip3 == null ? "" : this.currZip3;
  }

/**
	* 通訊-郵遞區號前三碼<br>
	* 
  *
  * @param currZip3 通訊-郵遞區號前三碼
	*/
  public void setCurrZip3(String currZip3) {
    this.currZip3 = currZip3;
  }

/**
	* 通訊-郵遞區號後三碼<br>
	* 
	* @return String
	*/
  public String getCurrZip2() {
    return this.currZip2 == null ? "" : this.currZip2;
  }

/**
	* 通訊-郵遞區號後三碼<br>
	* 
  *
  * @param currZip2 通訊-郵遞區號後三碼
	*/
  public void setCurrZip2(String currZip2) {
    this.currZip2 = currZip2;
  }

/**
	* 通訊-縣市代碼<br>
	* 地區別與鄉鎮區對照檔CdArea
	* @return String
	*/
  public String getCurrCityCode() {
    return this.currCityCode == null ? "" : this.currCityCode;
  }

/**
	* 通訊-縣市代碼<br>
	* 地區別與鄉鎮區對照檔CdArea
  *
  * @param currCityCode 通訊-縣市代碼
	*/
  public void setCurrCityCode(String currCityCode) {
    this.currCityCode = currCityCode;
  }

/**
	* 通訊-鄉鎮市區代碼<br>
	* 地區別與鄉鎮區對照檔CdArea
	* @return String
	*/
  public String getCurrAreaCode() {
    return this.currAreaCode == null ? "" : this.currAreaCode;
  }

/**
	* 通訊-鄉鎮市區代碼<br>
	* 地區別與鄉鎮區對照檔CdArea
  *
  * @param currAreaCode 通訊-鄉鎮市區代碼
	*/
  public void setCurrAreaCode(String currAreaCode) {
    this.currAreaCode = currAreaCode;
  }

/**
	* 通訊-路名<br>
	* 
	* @return String
	*/
  public String getCurrRoad() {
    return this.currRoad == null ? "" : this.currRoad;
  }

/**
	* 通訊-路名<br>
	* 
  *
  * @param currRoad 通訊-路名
	*/
  public void setCurrRoad(String currRoad) {
    this.currRoad = currRoad;
  }

/**
	* 通訊-段<br>
	* 
	* @return String
	*/
  public String getCurrSection() {
    return this.currSection == null ? "" : this.currSection;
  }

/**
	* 通訊-段<br>
	* 
  *
  * @param currSection 通訊-段
	*/
  public void setCurrSection(String currSection) {
    this.currSection = currSection;
  }

/**
	* 通訊-巷<br>
	* 
	* @return String
	*/
  public String getCurrAlley() {
    return this.currAlley == null ? "" : this.currAlley;
  }

/**
	* 通訊-巷<br>
	* 
  *
  * @param currAlley 通訊-巷
	*/
  public void setCurrAlley(String currAlley) {
    this.currAlley = currAlley;
  }

/**
	* 通訊-弄<br>
	* 
	* @return String
	*/
  public String getCurrLane() {
    return this.currLane == null ? "" : this.currLane;
  }

/**
	* 通訊-弄<br>
	* 
  *
  * @param currLane 通訊-弄
	*/
  public void setCurrLane(String currLane) {
    this.currLane = currLane;
  }

/**
	* 通訊-號<br>
	* 
	* @return String
	*/
  public String getCurrNum() {
    return this.currNum == null ? "" : this.currNum;
  }

/**
	* 通訊-號<br>
	* 
  *
  * @param currNum 通訊-號
	*/
  public void setCurrNum(String currNum) {
    this.currNum = currNum;
  }

/**
	* 通訊-號之<br>
	* 
	* @return String
	*/
  public String getCurrNumDash() {
    return this.currNumDash == null ? "" : this.currNumDash;
  }

/**
	* 通訊-號之<br>
	* 
  *
  * @param currNumDash 通訊-號之
	*/
  public void setCurrNumDash(String currNumDash) {
    this.currNumDash = currNumDash;
  }

/**
	* 通訊-樓<br>
	* 
	* @return String
	*/
  public String getCurrFloor() {
    return this.currFloor == null ? "" : this.currFloor;
  }

/**
	* 通訊-樓<br>
	* 
  *
  * @param currFloor 通訊-樓
	*/
  public void setCurrFloor(String currFloor) {
    this.currFloor = currFloor;
  }

/**
	* 通訊-樓之<br>
	* 
	* @return String
	*/
  public String getCurrFloorDash() {
    return this.currFloorDash == null ? "" : this.currFloorDash;
  }

/**
	* 通訊-樓之<br>
	* 
  *
  * @param currFloorDash 通訊-樓之
	*/
  public void setCurrFloorDash(String currFloorDash) {
    this.currFloorDash = currFloorDash;
  }

/**
	* 身份別<br>
	* 1:自然人
2:法人
2021.10.15 by eric
	* @return String
	*/
  public String getCuscCd() {
    return this.cuscCd == null ? "" : this.cuscCd;
  }

/**
	* 身份別<br>
	* 1:自然人
2:法人
2021.10.15 by eric
  *
  * @param cuscCd 身份別
	*/
  public void setCuscCd(String cuscCd) {
    this.cuscCd = cuscCd;
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
	* 員工代號<br>
	* 若此客戶為員工
才放該員工的員工代號
	* @return String
	*/
  public String getEmpNo() {
    return this.empNo == null ? "" : this.empNo;
  }

/**
	* 員工代號<br>
	* 若此客戶為員工
才放該員工的員工代號
  *
  * @param empNo 員工代號
	*/
  public void setEmpNo(String empNo) {
    this.empNo = empNo;
  }

/**
	* 英文姓名<br>
	* 
	* @return String
	*/
  public String getEName() {
    return this.eName == null ? "" : this.eName;
  }

/**
	* 英文姓名<br>
	* 
  *
  * @param eName 英文姓名
	*/
  public void setEName(String eName) {
    this.eName = eName;
  }

/**
	* 教育程度代號<br>
	* 共用代碼檔
1:小學以下
2:國中
3:高中職
4:專科學校
5:大學
6:研究所
7:博士
	* @return String
	*/
  public String getEduCode() {
    return this.eduCode == null ? "" : this.eduCode;
  }

/**
	* 教育程度代號<br>
	* 共用代碼檔
1:小學以下
2:國中
3:高中職
4:專科學校
5:大學
6:研究所
7:博士
  *
  * @param eduCode 教育程度代號
	*/
  public void setEduCode(String eduCode) {
    this.eduCode = eduCode;
  }

/**
	* 自有住宅有無<br>
	* Y:是
N:否
	* @return String
	*/
  public String getOwnedHome() {
    return this.ownedHome == null ? "" : this.ownedHome;
  }

/**
	* 自有住宅有無<br>
	* Y:是
N:否
  *
  * @param ownedHome 自有住宅有無
	*/
  public void setOwnedHome(String ownedHome) {
    this.ownedHome = ownedHome;
  }

/**
	* 任職機構名稱<br>
	* 
	* @return String
	*/
  public String getCurrCompName() {
    return this.currCompName == null ? "" : this.currCompName;
  }

/**
	* 任職機構名稱<br>
	* 
  *
  * @param currCompName 任職機構名稱
	*/
  public void setCurrCompName(String currCompName) {
    this.currCompName = currCompName;
  }

/**
	* 任職機構統編<br>
	* 
	* @return String
	*/
  public String getCurrCompId() {
    return this.currCompId == null ? "" : this.currCompId;
  }

/**
	* 任職機構統編<br>
	* 
  *
  * @param currCompId 任職機構統編
	*/
  public void setCurrCompId(String currCompId) {
    this.currCompId = currCompId;
  }

/**
	* 任職機構電話<br>
	* 
	* @return String
	*/
  public String getCurrCompTel() {
    return this.currCompTel == null ? "" : this.currCompTel;
  }

/**
	* 任職機構電話<br>
	* 
  *
  * @param currCompTel 任職機構電話
	*/
  public void setCurrCompTel(String currCompTel) {
    this.currCompTel = currCompTel;
  }

/**
	* 職位名稱<br>
	* 
	* @return String
	*/
  public String getJobTitle() {
    return this.jobTitle == null ? "" : this.jobTitle;
  }

/**
	* 職位名稱<br>
	* 
  *
  * @param jobTitle 職位名稱
	*/
  public void setJobTitle(String jobTitle) {
    this.jobTitle = jobTitle;
  }

/**
	* 服務年資<br>
	* 
	* @return String
	*/
  public String getJobTenure() {
    return this.jobTenure == null ? "" : this.jobTenure;
  }

/**
	* 服務年資<br>
	* 
  *
  * @param jobTenure 服務年資
	*/
  public void setJobTenure(String jobTenure) {
    this.jobTenure = jobTenure;
  }

/**
	* 年收入<br>
	* 
	* @return Integer
	*/
  public int getIncomeOfYearly() {
    return this.incomeOfYearly;
  }

/**
	* 年收入<br>
	* 
  *
  * @param incomeOfYearly 年收入
	*/
  public void setIncomeOfYearly(int incomeOfYearly) {
    this.incomeOfYearly = incomeOfYearly;
  }

/**
	* 年收入資料年月<br>
	* 西元年月
	* @return String
	*/
  public String getIncomeDataDate() {
    return this.incomeDataDate == null ? "" : this.incomeDataDate;
  }

/**
	* 年收入資料年月<br>
	* 西元年月
  *
  * @param incomeDataDate 年收入資料年月
	*/
  public void setIncomeDataDate(String incomeDataDate) {
    this.incomeDataDate = incomeDataDate;
  }

/**
	* 護照號碼<br>
	* 
	* @return String
	*/
  public String getPassportNo() {
    return this.passportNo == null ? "" : this.passportNo;
  }

/**
	* 護照號碼<br>
	* 
  *
  * @param passportNo 護照號碼
	*/
  public void setPassportNo(String passportNo) {
    this.passportNo = passportNo;
  }

/**
	* AML職業別<br>
	* 
	* @return String
	*/
  public String getAMLJobCode() {
    return this.aMLJobCode == null ? "" : this.aMLJobCode;
  }

/**
	* AML職業別<br>
	* 
  *
  * @param aMLJobCode AML職業別
	*/
  public void setAMLJobCode(String aMLJobCode) {
    this.aMLJobCode = aMLJobCode;
  }

/**
	* AML組織<br>
	* 
	* @return String
	*/
  public String getAMLGroup() {
    return this.aMLGroup == null ? "" : this.aMLGroup;
  }

/**
	* AML組織<br>
	* 
  *
  * @param aMLGroup AML組織
	*/
  public void setAMLGroup(String aMLGroup) {
    this.aMLGroup = aMLGroup;
  }

/**
	* 原住民姓名<br>
	* 
	* @return String
	*/
  public String getIndigenousName() {
    return this.indigenousName == null ? "" : this.indigenousName;
  }

/**
	* 原住民姓名<br>
	* 
  *
  * @param indigenousName 原住民姓名
	*/
  public void setIndigenousName(String indigenousName) {
    this.indigenousName = indigenousName;
  }

/**
	* 已編額度編號<br>
	* 
	* @return Integer
	*/
  public int getLastFacmNo() {
    return this.lastFacmNo;
  }

/**
	* 已編額度編號<br>
	* 
  *
  * @param lastFacmNo 已編額度編號
	*/
  public void setLastFacmNo(int lastFacmNo) {
    this.lastFacmNo = lastFacmNo;
  }

/**
	* 已編聯貸案序號<br>
	* 
	* @return Integer
	*/
  public int getLastSyndNo() {
    return this.lastSyndNo;
  }

/**
	* 已編聯貸案序號<br>
	* 
  *
  * @param lastSyndNo 已編聯貸案序號
	*/
  public void setLastSyndNo(int lastSyndNo) {
    this.lastSyndNo = lastSyndNo;
  }

/**
	* 開放查詢<br>
	* 1:不開放 
2:開放
	* @return String
	*/
  public String getAllowInquire() {
    return this.allowInquire == null ? "" : this.allowInquire;
  }

/**
	* 開放查詢<br>
	* 1:不開放 
2:開放
  *
  * @param allowInquire 開放查詢
	*/
  public void setAllowInquire(String allowInquire) {
    this.allowInquire = allowInquire;
  }

/**
	* 電子信箱<br>
	* 
	* @return String
	*/
  public String getEmail() {
    return this.email == null ? "" : this.email;
  }

/**
	* 電子信箱<br>
	* 
  *
  * @param email 電子信箱
	*/
  public void setEmail(String email) {
    this.email = email;
  }

/**
	* 交易進行記號<br>
	* 0:1STEP TX 
1/2:2STEP TX
	* @return Integer
	*/
  public int getActFg() {
    return this.actFg;
  }

/**
	* 交易進行記號<br>
	* 0:1STEP TX 
1/2:2STEP TX
  *
  * @param actFg 交易進行記號
	*/
  public void setActFg(int actFg) {
    this.actFg = actFg;
  }

/**
	* 介紹人<br>
	* 2021/01/07新增欄位(2021/01/14上DB)
	* @return String
	*/
  public String getIntroducer() {
    return this.introducer == null ? "" : this.introducer;
  }

/**
	* 介紹人<br>
	* 2021/01/07新增欄位(2021/01/14上DB)
  *
  * @param introducer 介紹人
	*/
  public void setIntroducer(String introducer) {
    this.introducer = introducer;
  }

/**
	* 房貸專員/企金人員<br>
	* 2022/02/17新增欄位 by eric
原DAT_CU$CUSP.CUSEM2
	* @return String
	*/
  public String getBusinessOfficer() {
    return this.businessOfficer == null ? "" : this.businessOfficer;
  }

/**
	* 房貸專員/企金人員<br>
	* 2022/02/17新增欄位 by eric
原DAT_CU$CUSP.CUSEM2
  *
  * @param businessOfficer 房貸專員/企金人員
	*/
  public void setBusinessOfficer(String businessOfficer) {
    this.businessOfficer = businessOfficer;
  }

/**
	* 是否為金控「疑似準利害關係人」名單<br>
	* Y/N
2021/08/06新增欄位(2021/08/06上DB)by eric
	* @return String
	*/
  public String getIsSuspected() {
    return this.isSuspected == null ? "" : this.isSuspected;
  }

/**
	* 是否為金控「疑似準利害關係人」名單<br>
	* Y/N
2021/08/06新增欄位(2021/08/06上DB)by eric
  *
  * @param isSuspected 是否為金控「疑似準利害關係人」名單
	*/
  public void setIsSuspected(String isSuspected) {
    this.isSuspected = isSuspected;
  }

/**
	* 是否為金控疑似利害關係人<br>
	* Y/N
2021/08/06新增欄位(2021/08/06上DB)by eric
	* @return String
	*/
  public String getIsSuspectedCheck() {
    return this.isSuspectedCheck == null ? "" : this.isSuspectedCheck;
  }

/**
	* 是否為金控疑似利害關係人<br>
	* Y/N
2021/08/06新增欄位(2021/08/06上DB)by eric
  *
  * @param isSuspectedCheck 是否為金控疑似利害關係人
	*/
  public void setIsSuspectedCheck(String isSuspectedCheck) {
    this.isSuspectedCheck = isSuspectedCheck;
  }

/**
	* 是否為金控疑似利害關係人_確認狀態<br>
	* Y/N
2021/08/06新增欄位(2021/08/06上DB)by eric
	* @return String
	*/
  public String getIsSuspectedCheckType() {
    return this.isSuspectedCheckType == null ? "" : this.isSuspectedCheckType;
  }

/**
	* 是否為金控疑似利害關係人_確認狀態<br>
	* Y/N
2021/08/06新增欄位(2021/08/06上DB)by eric
  *
  * @param isSuspectedCheckType 是否為金控疑似利害關係人_確認狀態
	*/
  public void setIsSuspectedCheckType(String isSuspectedCheckType) {
    this.isSuspectedCheckType = isSuspectedCheckType;
  }

/**
	* 是否為授信限制對象<br>
	* Y/N
2022/3/24新增欄位by eric
	* @return String
	*/
  public String getIsLimit() {
    return this.isLimit == null ? "" : this.isLimit;
  }

/**
	* 是否為授信限制對象<br>
	* Y/N
2022/3/24新增欄位by eric
  *
  * @param isLimit 是否為授信限制對象
	*/
  public void setIsLimit(String isLimit) {
    this.isLimit = isLimit;
  }

/**
	* 是否為利害關係人<br>
	* Y/N
2022/3/24新增欄位by eric
	* @return String
	*/
  public String getIsRelated() {
    return this.isRelated == null ? "" : this.isRelated;
  }

/**
	* 是否為利害關係人<br>
	* Y/N
2022/3/24新增欄位by eric
  *
  * @param isRelated 是否為利害關係人
	*/
  public void setIsRelated(String isRelated) {
    this.isRelated = isRelated;
  }

/**
	* 是否為準利害關係人<br>
	* Y/N
2022/3/24新增欄位by eric
	* @return String
	*/
  public String getIsLnrelNear() {
    return this.isLnrelNear == null ? "" : this.isLnrelNear;
  }

/**
	* 是否為準利害關係人<br>
	* Y/N
2022/3/24新增欄位by eric
  *
  * @param isLnrelNear 是否為準利害關係人
	*/
  public void setIsLnrelNear(String isLnrelNear) {
    this.isLnrelNear = isLnrelNear;
  }

/**
	* 是否資訊日期<br>
	* 2022/3/24新增欄位by eric
	* @return Integer
	*/
  public int getIsDate() {
    return StaticTool.bcToRoc(this.isDate);
  }

/**
	* 是否資訊日期<br>
	* 2022/3/24新增欄位by eric
  *
  * @param isDate 是否資訊日期
  * @throws LogicException when Date Is Warn	*/
  public void setIsDate(int isDate) throws LogicException {
    this.isDate = StaticTool.rocToBc(isDate);
  }

/**
	* 資料狀態<br>
	* 0:已完成建檔
1:未完成建檔
(2021/08/13上DB)by eric
	* @return Integer
	*/
  public int getDataStatus() {
    return this.dataStatus;
  }

/**
	* 資料狀態<br>
	* 0:已完成建檔
1:未完成建檔
(2021/08/13上DB)by eric
  *
  * @param dataStatus 資料狀態
	*/
  public void setDataStatus(int dataStatus) {
    this.dataStatus = dataStatus;
  }

/**
	* 建檔身分別<br>
	* 0:借戶
1:保證人
2:擔保品提供人
3:交易關係人
4:借款戶關係人
2021/08/24新增欄位
(2021/08/25上DB)by eric
(2021/09/17)新增4. by昱衡
	* @return Integer
	*/
  public int getTypeCode() {
    return this.typeCode;
  }

/**
	* 建檔身分別<br>
	* 0:借戶
1:保證人
2:擔保品提供人
3:交易關係人
4:借款戶關係人
2021/08/24新增欄位
(2021/08/25上DB)by eric
(2021/09/17)新增4. by昱衡
  *
  * @param typeCode 建檔身分別
	*/
  public void setTypeCode(int typeCode) {
    this.typeCode = typeCode;
  }

/**
	* 站別<br>
	* 2022/02/17新增欄位 by eric
原DAT_CU$CUSP.CUSSTN
	* @return String
	*/
  public String getStation() {
    return this.station == null ? "" : this.station;
  }

/**
	* 站別<br>
	* 2022/02/17新增欄位 by eric
原DAT_CU$CUSP.CUSSTN
  *
  * @param station 站別
	*/
  public void setStation(String station) {
    this.station = station;
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
    return "CustMain [custUKey=" + custUKey + ", custId=" + custId + ", custNo=" + custNo + ", branchNo=" + branchNo + ", custName=" + custName + ", birthday=" + birthday
           + ", sex=" + sex + ", custTypeCode=" + custTypeCode + ", industryCode=" + industryCode + ", nationalityCode=" + nationalityCode + ", bussNationalityCode=" + bussNationalityCode + ", spouseId=" + spouseId
           + ", spouseName=" + spouseName + ", regZip3=" + regZip3 + ", regZip2=" + regZip2 + ", regCityCode=" + regCityCode + ", regAreaCode=" + regAreaCode + ", regRoad=" + regRoad
           + ", regSection=" + regSection + ", regAlley=" + regAlley + ", regLane=" + regLane + ", regNum=" + regNum + ", regNumDash=" + regNumDash + ", regFloor=" + regFloor
           + ", regFloorDash=" + regFloorDash + ", currZip3=" + currZip3 + ", currZip2=" + currZip2 + ", currCityCode=" + currCityCode + ", currAreaCode=" + currAreaCode + ", currRoad=" + currRoad
           + ", currSection=" + currSection + ", currAlley=" + currAlley + ", currLane=" + currLane + ", currNum=" + currNum + ", currNumDash=" + currNumDash + ", currFloor=" + currFloor
           + ", currFloorDash=" + currFloorDash + ", cuscCd=" + cuscCd + ", entCode=" + entCode + ", empNo=" + empNo + ", eName=" + eName + ", eduCode=" + eduCode
           + ", ownedHome=" + ownedHome + ", currCompName=" + currCompName + ", currCompId=" + currCompId + ", currCompTel=" + currCompTel + ", jobTitle=" + jobTitle + ", jobTenure=" + jobTenure
           + ", incomeOfYearly=" + incomeOfYearly + ", incomeDataDate=" + incomeDataDate + ", passportNo=" + passportNo + ", aMLJobCode=" + aMLJobCode + ", aMLGroup=" + aMLGroup + ", indigenousName=" + indigenousName
           + ", lastFacmNo=" + lastFacmNo + ", lastSyndNo=" + lastSyndNo + ", allowInquire=" + allowInquire + ", email=" + email + ", actFg=" + actFg + ", introducer=" + introducer
           + ", businessOfficer=" + businessOfficer + ", isSuspected=" + isSuspected + ", isSuspectedCheck=" + isSuspectedCheck + ", isSuspectedCheckType=" + isSuspectedCheckType + ", isLimit=" + isLimit + ", isRelated=" + isRelated
           + ", isLnrelNear=" + isLnrelNear + ", isDate=" + isDate + ", dataStatus=" + dataStatus + ", typeCode=" + typeCode + ", station=" + station + ", createDate=" + createDate
           + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
