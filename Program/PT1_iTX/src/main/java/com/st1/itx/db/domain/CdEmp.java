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
import javax.persistence.Id;
import javax.persistence.Column;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * CdEmp 員工資料檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CdEmp`")
public class CdEmp implements Serializable {


  // 電腦編號
  /* 員工編號 */
  @Id
  @Column(name = "`EmployeeNo`", length = 10)
  private String employeeNo = " ";

  // 業務員代號
  @Column(name = "`AgentCode`", length = 12)
  private String agentCode;

  // 姓名
  /* 姓名 */
  @Column(name = "`Fullname`", length = 40)
  private String fullname;

  // 單位簡稱
  @Column(name = "`CenterShortName`", length = 10)
  private String centerShortName;

  // 單位名稱
  @Column(name = "`CenterCodeName`", length = 20)
  private String centerCodeName;

  // 區部代號
  @Column(name = "`CenterCode1`", length = 6)
  private String centerCode1;

  // 區部簡稱
  @Column(name = "`CenterCode1Short`", length = 10)
  private String centerCode1Short;

  // 區部名稱
  @Column(name = "`CenterCode1Name`", length = 20)
  private String centerCode1Name;

  // 部室代號
  @Column(name = "`CenterCode2`", length = 6)
  private String centerCode2;

  // 部室簡稱
  @Column(name = "`CenterCode2Short`", length = 10)
  private String centerCode2Short;

  // 部室名稱
  @Column(name = "`CenterCode2Name`", length = 20)
  private String centerCode2Name;

  // 區部代號(駐在單位)
  @Column(name = "`CenterCodeAcc1`", length = 6)
  private String centerCodeAcc1;

  // 區部名稱(駐在單位)
  @Column(name = "`CenterCodeAcc1Name`", length = 20)
  private String centerCodeAcc1Name;

  // 部室代號(駐在單位)
  @Column(name = "`CenterCodeAcc2`", length = 6)
  private String centerCodeAcc2;

  // 部室名稱(駐在單位)
  @Column(name = "`CenterCodeAcc2Name`", length = 20)
  private String centerCodeAcc2Name;

  // 業務線代號
  /* 35:15日薪其他:15日薪 */
  @Column(name = "`CommLineCode`", length = 2)
  private String commLineCode;

  // 業務線別
  @Column(name = "`CommLineType`", length = 1)
  private String commLineType;

  // 介紹人
  @Column(name = "`OrigIntroducerId`", length = 12)
  private String origIntroducerId;

  // 介紹關係碼
  @Column(name = "`IntroducerInd`", length = 1)
  private String introducerInd;

  // 報聘職等
  @Column(name = "`RegisterLevel`", length = 2)
  private String registerLevel;

  // 在職/締約日期
  /* 西元年 */
  @Column(name = "`RegisterDate`")
  private int registerDate = 0;

  // 單位代號
  @Column(name = "`CenterCode`", length = 6)
  private String centerCode;

  // 單位主管
  @Column(name = "`AdministratId`", length = 12)
  private String administratId;

  // 建檔日期
  @Column(name = "`InputDate`")
  private int inputDate = 0;

  // 建檔人
  @Column(name = "`InputUser`", length = 8)
  private String inputUser;

  // 業務人員任用狀況碼
  /* [員工身份別]0:單位報備1:在職2:離職3:解聘4:留職停薪5:退休離職9:未報聘/內勤 */
  @Column(name = "`AgStatusCode`", length = 1)
  private String agStatusCode;

  // 業務人員任用狀況異動日
  /* 西元年 */
  @Column(name = "`AgStatusDate`")
  private int agStatusDate = 0;

  // 作業日期(交易日期)
  @Column(name = "`TranDate`")
  private int tranDate = 0;

  // 作業者
  @Column(name = "`TranUser`", length = 8)
  private String tranUser;

  // 再聘日
  @Column(name = "`ReRegisterDate`")
  private int reRegisterDate = 0;

  // 上層主管
  @Column(name = "`DirectorId`", length = 12)
  private String directorId;

  // 主管_財務
  @Column(name = "`DirectorIdF`", length = 12)
  private String directorIdF;

  // 區主任/上一代主管
  @Column(name = "`IntroducerId`", length = 12)
  private String introducerId;

  // 推介人_財務
  @Column(name = "`IntroducerIdF`", length = 12)
  private String introducerIdF;

  // 業務人員職等
  @Column(name = "`AgLevel`", length = 2)
  private String agLevel;

  // 前次業務人員職等
  @Column(name = "`LastLevel`", length = 2)
  private String lastLevel;

  // 職等異動日
  @Column(name = "`LevelDate`")
  private int levelDate = 0;

  // 最高職等
  @Column(name = "`TopLevel`", length = 2)
  private String topLevel;

  // 任職型態
  @Column(name = "`OccpInd`", length = 1)
  private String occpInd;

  // 責任額
  @Column(name = "`QuotaAmt`")
  private BigDecimal quotaAmt = new BigDecimal("0");

  // 申請登錄類別
  @Column(name = "`ApplType`", length = 1)
  private String applType;

  // 所得稅率
  @Column(name = "`TaxRate`")
  private BigDecimal taxRate = new BigDecimal("0");

  // 勞保等級
  @Column(name = "`SocialInsuClass`")
  private int socialInsuClass = 0;

  // 職等平階起始年月
  @Column(name = "`PromotLevelYM`", length = 7)
  private String promotLevelYM;

  // 晉陞主管年月
  @Column(name = "`DirectorYM`", length = 7)
  private String directorYM;

  // 登錄日期
  @Column(name = "`RecordDate`")
  private int recordDate = 0;

  // 發證日期
  @Column(name = "`ExRecordDate`")
  private int exRecordDate = 0;

  // 證書日期/測驗日期
  @Column(name = "`RxTrDate`")
  private int rxTrDate = 0;

  // 證書字號
  @Column(name = "`ExTrIdent`", length = 16)
  private String exTrIdent;

  // 中專證號
  @Column(name = "`ExTrIdent2`", length = 9)
  private String exTrIdent2;

  // 投資型證號
  @Column(name = "`ExTrIdent3`", length = 12)
  private String exTrIdent3;

  // 投資登錄日期
  @Column(name = "`ExTrDate`")
  private int exTrDate = 0;

  // 報聘前年資(月表示)
  @Column(name = "`RegisterBefore`")
  private int registerBefore = 0;

  // 主管年資
  @Column(name = "`DirectorAfter`")
  private int directorAfter = 0;

  // 免體檢授權碼
  @Column(name = "`MedicalCode`", length = 2)
  private String medicalCode;

  // 換證日期
  @Column(name = "`ExChgDate`")
  private int exChgDate = 0;

  // 註銷日期
  @Column(name = "`ExDelDate`")
  private int exDelDate = 0;

  // 申請業務類別
  @Column(name = "`ApplCode`", length = 10)
  private String applCode;

  // 初次登錄日
  @Column(name = "`FirstRegDate`")
  private int firstRegDate = 0;

  // 業務來源之專案
  @Column(name = "`AginSource`", length = 3)
  private String aginSource;

  // 單位代號
  @Column(name = "`AguiCenter`", length = 9)
  private String aguiCenter;

  // 業務人員身份證字號
  /* 業務人員身分證字號 */
  @Column(name = "`AgentId`", length = 10)
  private String agentId;

  // 業務人員主管
  @Column(name = "`TopId`", length = 12)
  private String topId;

  // 業務人員職級
  @Column(name = "`AgDegree`", length = 2)
  private String agDegree;

  // 收費員指示碼
  @Column(name = "`CollectInd`", length = 1)
  private String collectInd;

  // 制度別
  @Column(name = "`AgType1`", length = 1)
  private String agType1;

  // 單雙合約碼
  @Column(name = "`ContractInd`", length = 1)
  private String contractInd;

  // 單雙合約異動工作月
  @Column(name = "`ContractIndYM`", length = 7)
  private String contractIndYM;

  // 身份別
  @Column(name = "`AgType2`", length = 1)
  private String agType2;

  // 特殊人員碼
  @Column(name = "`AgType3`", length = 1)
  private String agType3;

  // 新舊制別
  @Column(name = "`AgType4`", length = 1)
  private String agType4;

  // 辦事員碼
  @Column(name = "`AginInd1`", length = 1)
  private String aginInd1;

  // 可招攬指示碼
  @Column(name = "`AgPoInd`", length = 1)
  private String agPoInd;

  // 齊件否
  @Column(name = "`AgDocInd`", length = 1)
  private String agDocInd;

  // 新舊人指示碼
  @Column(name = "`NewHireType`", length = 1)
  private String newHireType;

  // 現職指示碼
  /* Y:現職 */
  @Column(name = "`AgCurInd`", length = 1)
  private String agCurInd;

  // 發文類別
  @Column(name = "`AgSendType`", length = 3)
  private String agSendType;

  // 發文文號
  @Column(name = "`AgSendNo`", length = 100)
  private String agSendNo;

  // 任職日期
  @Column(name = "`RegisterDate2`")
  private int registerDate2 = 0;

  // 回任日期
  @Column(name = "`AgReturnDate`")
  private int agReturnDate = 0;

  // 初次轉制日期
  @Column(name = "`AgTransferDateF`")
  private int agTransferDateF = 0;

  // 轉制日期
  @Column(name = "`AgTransferDate`")
  private int agTransferDate = 0;

  // 初次晉升年月
  @Column(name = "`PromotYM`", length = 7)
  private String promotYM;

  // 生效業績年月
  @Column(name = "`PromotYMF`", length = 7)
  private String promotYMF;

  // 職務異動日
  @Column(name = "`AgPostChgDate`")
  private int agPostChgDate = 0;

  // 扶養人數
  @Column(name = "`FamiliesTax`")
  private int familiesTax = 0;

  // 原區主任代號
  @Column(name = "`AgentCodeI`", length = 12)
  private String agentCodeI;

  // 職等_系統
  @Column(name = "`AgLevelSys`", length = 2)
  private String agLevelSys;

  // 內階職務
  @Column(name = "`AgPostIn`", length = 6)
  private String agPostIn;

  // 駐在單位
  @Column(name = "`CenterCodeAcc`", length = 6)
  private String centerCodeAcc;

  // 考核特殊碼
  @Column(name = "`EvalueInd`", length = 1)
  private String evalueInd;

  // 辦法優待碼
  @Column(name = "`EvalueInd1`", length = 1)
  private String evalueInd1;

  // 批次號碼
  @Column(name = "`BatchNo`")
  private BigDecimal batchNo = new BigDecimal("0");

  // 考核年月
  @Column(name = "`EvalueYM`", length = 7)
  private String evalueYM;

  // 轉檔碼
  @Column(name = "`AgTransferCode`", length = 2)
  private String agTransferCode;

  // 出生年月日
  @Column(name = "`Birth`")
  private int birth = 0;

  // 學歷
  @Column(name = "`Education`", length = 1)
  private String education;

  // 勞退狀況
  @Column(name = "`LrInd`", length = 1)
  private String lrInd;

  // 資料處理時間
  @Column(name = "`ProceccDate`")
  private int proceccDate = 0;

  // 離職/停約日
  /* 西元年 */
  @Column(name = "`QuitDate`")
  private int quitDate = 0;

  // 職務
  /* 職務 */
  @Column(name = "`AgPost`", length = 2)
  private String agPost;

  // 職等中文
  /* 職等中文 */
  @Column(name = "`LevelNameChs`", length = 10)
  private String levelNameChs;

  // 勞退碼
  @Column(name = "`LrSystemType`", length = 1)
  private String lrSystemType;

  // 年資_年
  @Column(name = "`SeniorityYY`")
  private int seniorityYY = 0;

  // 年資_月
  @Column(name = "`SeniorityMM`")
  private int seniorityMM = 0;

  // 年資_日
  @Column(name = "`SeniorityDD`")
  private int seniorityDD = 0;

  // 登錄處理事項
  @Column(name = "`AglaProcessInd`", length = 2)
  private String aglaProcessInd;

  // 登錄狀態
  @Column(name = "`StatusCode`", length = 1)
  private String statusCode;

  // 註銷原因
  @Column(name = "`AglaCancelReason`", length = 1)
  private String aglaCancelReason;

  // 利變年金通報日
  @Column(name = "`ISAnnApplDate`")
  private int iSAnnApplDate = 0;

  // 外幣保單登入日
  @Column(name = "`RecordDateC`")
  private int recordDateC = 0;

  // 停招/撤銷原因
  @Column(name = "`StopReason`", length = 20)
  private String stopReason;

  // 停止招攬起日
  @Column(name = "`StopStrDate`")
  private int stopStrDate = 0;

  // 停止招攬迄日
  @Column(name = "`StopEndDate`")
  private int stopEndDate = 0;

  // IFP登錄日
  @Column(name = "`IFPDate`")
  private int iFPDate = 0;

  // 撤銷起日
  @Column(name = "`EffectStrDate`")
  private int effectStrDate = 0;

  // 撤銷迄日
  @Column(name = "`EffectEndDate`")
  private int effectEndDate = 0;

  // 一般年金通報日
  @Column(name = "`AnnApplDate`")
  private int annApplDate = 0;

  // 單位名稱(駐在單位)
  @Column(name = "`CenterCodeAccName`", length = 20)
  private String centerCodeAccName;

  // 重僱碼
  @Column(name = "`ReHireCode`", length = 1)
  private String reHireCode;

  // 特別碼
  @Column(name = "`RSVDAdminCode`", length = 1)
  private String rSVDAdminCode;

  // 帳號
  @Column(name = "`Account`", length = 16)
  private String account;

  // 優體測驗通過日
  @Column(name = "`PRPDate`", length = 15)
  private String pRPDate;

  // 戶籍地址郵遞區號
  @Column(name = "`Zip`", length = 5)
  private String zip;

  // 戶籍地址
  @Column(name = "`Address`", length = 80)
  private String address;

  // 住家電話
  @Column(name = "`PhoneH`", length = 30)
  private String phoneH;

  // 手機電話
  @Column(name = "`PhoneC`", length = 30)
  private String phoneC;

  // 基金銷售資格碼
  @Column(name = "`SalesQualInd`", length = 1)
  private String salesQualInd;

  // 基金銷售資格日
  @Column(name = "`AgsqStartDate`")
  private int agsqStartDate = 0;

  // 原住民羅馬拼音姓名
  @Column(name = "`PinYinNameIndi`", length = 50)
  private String pinYinNameIndi;

  // 電子郵件
  @Column(name = "`Email`", length = 50)
  private String email;

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
	* 電腦編號<br>
	* 員工編號
	* @return String
	*/
  public String getEmployeeNo() {
    return this.employeeNo == null ? "" : this.employeeNo;
  }

/**
	* 電腦編號<br>
	* 員工編號
  *
  * @param employeeNo 電腦編號
	*/
  public void setEmployeeNo(String employeeNo) {
    this.employeeNo = employeeNo;
  }

/**
	* 業務員代號<br>
	* 
	* @return String
	*/
  public String getAgentCode() {
    return this.agentCode == null ? "" : this.agentCode;
  }

/**
	* 業務員代號<br>
	* 
  *
  * @param agentCode 業務員代號
	*/
  public void setAgentCode(String agentCode) {
    this.agentCode = agentCode;
  }

/**
	* 姓名<br>
	* 姓名
	* @return String
	*/
  public String getFullname() {
    return this.fullname == null ? "" : this.fullname;
  }

/**
	* 姓名<br>
	* 姓名
  *
  * @param fullname 姓名
	*/
  public void setFullname(String fullname) {
    this.fullname = fullname;
  }

/**
	* 單位簡稱<br>
	* 
	* @return String
	*/
  public String getCenterShortName() {
    return this.centerShortName == null ? "" : this.centerShortName;
  }

/**
	* 單位簡稱<br>
	* 
  *
  * @param centerShortName 單位簡稱
	*/
  public void setCenterShortName(String centerShortName) {
    this.centerShortName = centerShortName;
  }

/**
	* 單位名稱<br>
	* 
	* @return String
	*/
  public String getCenterCodeName() {
    return this.centerCodeName == null ? "" : this.centerCodeName;
  }

/**
	* 單位名稱<br>
	* 
  *
  * @param centerCodeName 單位名稱
	*/
  public void setCenterCodeName(String centerCodeName) {
    this.centerCodeName = centerCodeName;
  }

/**
	* 區部代號<br>
	* 
	* @return String
	*/
  public String getCenterCode1() {
    return this.centerCode1 == null ? "" : this.centerCode1;
  }

/**
	* 區部代號<br>
	* 
  *
  * @param centerCode1 區部代號
	*/
  public void setCenterCode1(String centerCode1) {
    this.centerCode1 = centerCode1;
  }

/**
	* 區部簡稱<br>
	* 
	* @return String
	*/
  public String getCenterCode1Short() {
    return this.centerCode1Short == null ? "" : this.centerCode1Short;
  }

/**
	* 區部簡稱<br>
	* 
  *
  * @param centerCode1Short 區部簡稱
	*/
  public void setCenterCode1Short(String centerCode1Short) {
    this.centerCode1Short = centerCode1Short;
  }

/**
	* 區部名稱<br>
	* 
	* @return String
	*/
  public String getCenterCode1Name() {
    return this.centerCode1Name == null ? "" : this.centerCode1Name;
  }

/**
	* 區部名稱<br>
	* 
  *
  * @param centerCode1Name 區部名稱
	*/
  public void setCenterCode1Name(String centerCode1Name) {
    this.centerCode1Name = centerCode1Name;
  }

/**
	* 部室代號<br>
	* 
	* @return String
	*/
  public String getCenterCode2() {
    return this.centerCode2 == null ? "" : this.centerCode2;
  }

/**
	* 部室代號<br>
	* 
  *
  * @param centerCode2 部室代號
	*/
  public void setCenterCode2(String centerCode2) {
    this.centerCode2 = centerCode2;
  }

/**
	* 部室簡稱<br>
	* 
	* @return String
	*/
  public String getCenterCode2Short() {
    return this.centerCode2Short == null ? "" : this.centerCode2Short;
  }

/**
	* 部室簡稱<br>
	* 
  *
  * @param centerCode2Short 部室簡稱
	*/
  public void setCenterCode2Short(String centerCode2Short) {
    this.centerCode2Short = centerCode2Short;
  }

/**
	* 部室名稱<br>
	* 
	* @return String
	*/
  public String getCenterCode2Name() {
    return this.centerCode2Name == null ? "" : this.centerCode2Name;
  }

/**
	* 部室名稱<br>
	* 
  *
  * @param centerCode2Name 部室名稱
	*/
  public void setCenterCode2Name(String centerCode2Name) {
    this.centerCode2Name = centerCode2Name;
  }

/**
	* 區部代號(駐在單位)<br>
	* 
	* @return String
	*/
  public String getCenterCodeAcc1() {
    return this.centerCodeAcc1 == null ? "" : this.centerCodeAcc1;
  }

/**
	* 區部代號(駐在單位)<br>
	* 
  *
  * @param centerCodeAcc1 區部代號(駐在單位)
	*/
  public void setCenterCodeAcc1(String centerCodeAcc1) {
    this.centerCodeAcc1 = centerCodeAcc1;
  }

/**
	* 區部名稱(駐在單位)<br>
	* 
	* @return String
	*/
  public String getCenterCodeAcc1Name() {
    return this.centerCodeAcc1Name == null ? "" : this.centerCodeAcc1Name;
  }

/**
	* 區部名稱(駐在單位)<br>
	* 
  *
  * @param centerCodeAcc1Name 區部名稱(駐在單位)
	*/
  public void setCenterCodeAcc1Name(String centerCodeAcc1Name) {
    this.centerCodeAcc1Name = centerCodeAcc1Name;
  }

/**
	* 部室代號(駐在單位)<br>
	* 
	* @return String
	*/
  public String getCenterCodeAcc2() {
    return this.centerCodeAcc2 == null ? "" : this.centerCodeAcc2;
  }

/**
	* 部室代號(駐在單位)<br>
	* 
  *
  * @param centerCodeAcc2 部室代號(駐在單位)
	*/
  public void setCenterCodeAcc2(String centerCodeAcc2) {
    this.centerCodeAcc2 = centerCodeAcc2;
  }

/**
	* 部室名稱(駐在單位)<br>
	* 
	* @return String
	*/
  public String getCenterCodeAcc2Name() {
    return this.centerCodeAcc2Name == null ? "" : this.centerCodeAcc2Name;
  }

/**
	* 部室名稱(駐在單位)<br>
	* 
  *
  * @param centerCodeAcc2Name 部室名稱(駐在單位)
	*/
  public void setCenterCodeAcc2Name(String centerCodeAcc2Name) {
    this.centerCodeAcc2Name = centerCodeAcc2Name;
  }

/**
	* 業務線代號<br>
	* 35:15日薪
其他:15日薪
	* @return String
	*/
  public String getCommLineCode() {
    return this.commLineCode == null ? "" : this.commLineCode;
  }

/**
	* 業務線代號<br>
	* 35:15日薪
其他:15日薪
  *
  * @param commLineCode 業務線代號
	*/
  public void setCommLineCode(String commLineCode) {
    this.commLineCode = commLineCode;
  }

/**
	* 業務線別<br>
	* 
	* @return String
	*/
  public String getCommLineType() {
    return this.commLineType == null ? "" : this.commLineType;
  }

/**
	* 業務線別<br>
	* 
  *
  * @param commLineType 業務線別
	*/
  public void setCommLineType(String commLineType) {
    this.commLineType = commLineType;
  }

/**
	* 介紹人<br>
	* 
	* @return String
	*/
  public String getOrigIntroducerId() {
    return this.origIntroducerId == null ? "" : this.origIntroducerId;
  }

/**
	* 介紹人<br>
	* 
  *
  * @param origIntroducerId 介紹人
	*/
  public void setOrigIntroducerId(String origIntroducerId) {
    this.origIntroducerId = origIntroducerId;
  }

/**
	* 介紹關係碼<br>
	* 
	* @return String
	*/
  public String getIntroducerInd() {
    return this.introducerInd == null ? "" : this.introducerInd;
  }

/**
	* 介紹關係碼<br>
	* 
  *
  * @param introducerInd 介紹關係碼
	*/
  public void setIntroducerInd(String introducerInd) {
    this.introducerInd = introducerInd;
  }

/**
	* 報聘職等<br>
	* 
	* @return String
	*/
  public String getRegisterLevel() {
    return this.registerLevel == null ? "" : this.registerLevel;
  }

/**
	* 報聘職等<br>
	* 
  *
  * @param registerLevel 報聘職等
	*/
  public void setRegisterLevel(String registerLevel) {
    this.registerLevel = registerLevel;
  }

/**
	* 在職/締約日期<br>
	* 西元年
	* @return Integer
	*/
  public int getRegisterDate() {
    return StaticTool.bcToRoc(this.registerDate);
  }

/**
	* 在職/締約日期<br>
	* 西元年
  *
  * @param registerDate 在職/締約日期
  * @throws LogicException when Date Is Warn	*/
  public void setRegisterDate(int registerDate) throws LogicException {
    this.registerDate = StaticTool.rocToBc(registerDate);
  }

/**
	* 單位代號<br>
	* 
	* @return String
	*/
  public String getCenterCode() {
    return this.centerCode == null ? "" : this.centerCode;
  }

/**
	* 單位代號<br>
	* 
  *
  * @param centerCode 單位代號
	*/
  public void setCenterCode(String centerCode) {
    this.centerCode = centerCode;
  }

/**
	* 單位主管<br>
	* 
	* @return String
	*/
  public String getAdministratId() {
    return this.administratId == null ? "" : this.administratId;
  }

/**
	* 單位主管<br>
	* 
  *
  * @param administratId 單位主管
	*/
  public void setAdministratId(String administratId) {
    this.administratId = administratId;
  }

/**
	* 建檔日期<br>
	* 
	* @return Integer
	*/
  public int getInputDate() {
    return StaticTool.bcToRoc(this.inputDate);
  }

/**
	* 建檔日期<br>
	* 
  *
  * @param inputDate 建檔日期
  * @throws LogicException when Date Is Warn	*/
  public void setInputDate(int inputDate) throws LogicException {
    this.inputDate = StaticTool.rocToBc(inputDate);
  }

/**
	* 建檔人<br>
	* 
	* @return String
	*/
  public String getInputUser() {
    return this.inputUser == null ? "" : this.inputUser;
  }

/**
	* 建檔人<br>
	* 
  *
  * @param inputUser 建檔人
	*/
  public void setInputUser(String inputUser) {
    this.inputUser = inputUser;
  }

/**
	* 業務人員任用狀況碼<br>
	* [員工身份別]
0:單位報備
1:在職
2:離職
3:解聘
4:留職停薪
5:退休離職
9:未報聘/內勤
	* @return String
	*/
  public String getAgStatusCode() {
    return this.agStatusCode == null ? "" : this.agStatusCode;
  }

/**
	* 業務人員任用狀況碼<br>
	* [員工身份別]
0:單位報備
1:在職
2:離職
3:解聘
4:留職停薪
5:退休離職
9:未報聘/內勤
  *
  * @param agStatusCode 業務人員任用狀況碼
	*/
  public void setAgStatusCode(String agStatusCode) {
    this.agStatusCode = agStatusCode;
  }

/**
	* 業務人員任用狀況異動日<br>
	* 西元年
	* @return Integer
	*/
  public int getAgStatusDate() {
    return StaticTool.bcToRoc(this.agStatusDate);
  }

/**
	* 業務人員任用狀況異動日<br>
	* 西元年
  *
  * @param agStatusDate 業務人員任用狀況異動日
  * @throws LogicException when Date Is Warn	*/
  public void setAgStatusDate(int agStatusDate) throws LogicException {
    this.agStatusDate = StaticTool.rocToBc(agStatusDate);
  }

/**
	* 作業日期(交易日期)<br>
	* 
	* @return Integer
	*/
  public int getTranDate() {
    return StaticTool.bcToRoc(this.tranDate);
  }

/**
	* 作業日期(交易日期)<br>
	* 
  *
  * @param tranDate 作業日期(交易日期)
  * @throws LogicException when Date Is Warn	*/
  public void setTranDate(int tranDate) throws LogicException {
    this.tranDate = StaticTool.rocToBc(tranDate);
  }

/**
	* 作業者<br>
	* 
	* @return String
	*/
  public String getTranUser() {
    return this.tranUser == null ? "" : this.tranUser;
  }

/**
	* 作業者<br>
	* 
  *
  * @param tranUser 作業者
	*/
  public void setTranUser(String tranUser) {
    this.tranUser = tranUser;
  }

/**
	* 再聘日<br>
	* 
	* @return Integer
	*/
  public int getReRegisterDate() {
    return StaticTool.bcToRoc(this.reRegisterDate);
  }

/**
	* 再聘日<br>
	* 
  *
  * @param reRegisterDate 再聘日
  * @throws LogicException when Date Is Warn	*/
  public void setReRegisterDate(int reRegisterDate) throws LogicException {
    this.reRegisterDate = StaticTool.rocToBc(reRegisterDate);
  }

/**
	* 上層主管<br>
	* 
	* @return String
	*/
  public String getDirectorId() {
    return this.directorId == null ? "" : this.directorId;
  }

/**
	* 上層主管<br>
	* 
  *
  * @param directorId 上層主管
	*/
  public void setDirectorId(String directorId) {
    this.directorId = directorId;
  }

/**
	* 主管_財務<br>
	* 
	* @return String
	*/
  public String getDirectorIdF() {
    return this.directorIdF == null ? "" : this.directorIdF;
  }

/**
	* 主管_財務<br>
	* 
  *
  * @param directorIdF 主管_財務
	*/
  public void setDirectorIdF(String directorIdF) {
    this.directorIdF = directorIdF;
  }

/**
	* 區主任/上一代主管<br>
	* 
	* @return String
	*/
  public String getIntroducerId() {
    return this.introducerId == null ? "" : this.introducerId;
  }

/**
	* 區主任/上一代主管<br>
	* 
  *
  * @param introducerId 區主任/上一代主管
	*/
  public void setIntroducerId(String introducerId) {
    this.introducerId = introducerId;
  }

/**
	* 推介人_財務<br>
	* 
	* @return String
	*/
  public String getIntroducerIdF() {
    return this.introducerIdF == null ? "" : this.introducerIdF;
  }

/**
	* 推介人_財務<br>
	* 
  *
  * @param introducerIdF 推介人_財務
	*/
  public void setIntroducerIdF(String introducerIdF) {
    this.introducerIdF = introducerIdF;
  }

/**
	* 業務人員職等<br>
	* 
	* @return String
	*/
  public String getAgLevel() {
    return this.agLevel == null ? "" : this.agLevel;
  }

/**
	* 業務人員職等<br>
	* 
  *
  * @param agLevel 業務人員職等
	*/
  public void setAgLevel(String agLevel) {
    this.agLevel = agLevel;
  }

/**
	* 前次業務人員職等<br>
	* 
	* @return String
	*/
  public String getLastLevel() {
    return this.lastLevel == null ? "" : this.lastLevel;
  }

/**
	* 前次業務人員職等<br>
	* 
  *
  * @param lastLevel 前次業務人員職等
	*/
  public void setLastLevel(String lastLevel) {
    this.lastLevel = lastLevel;
  }

/**
	* 職等異動日<br>
	* 
	* @return Integer
	*/
  public int getLevelDate() {
    return StaticTool.bcToRoc(this.levelDate);
  }

/**
	* 職等異動日<br>
	* 
  *
  * @param levelDate 職等異動日
  * @throws LogicException when Date Is Warn	*/
  public void setLevelDate(int levelDate) throws LogicException {
    this.levelDate = StaticTool.rocToBc(levelDate);
  }

/**
	* 最高職等<br>
	* 
	* @return String
	*/
  public String getTopLevel() {
    return this.topLevel == null ? "" : this.topLevel;
  }

/**
	* 最高職等<br>
	* 
  *
  * @param topLevel 最高職等
	*/
  public void setTopLevel(String topLevel) {
    this.topLevel = topLevel;
  }

/**
	* 任職型態<br>
	* 
	* @return String
	*/
  public String getOccpInd() {
    return this.occpInd == null ? "" : this.occpInd;
  }

/**
	* 任職型態<br>
	* 
  *
  * @param occpInd 任職型態
	*/
  public void setOccpInd(String occpInd) {
    this.occpInd = occpInd;
  }

/**
	* 責任額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getQuotaAmt() {
    return this.quotaAmt;
  }

/**
	* 責任額<br>
	* 
  *
  * @param quotaAmt 責任額
	*/
  public void setQuotaAmt(BigDecimal quotaAmt) {
    this.quotaAmt = quotaAmt;
  }

/**
	* 申請登錄類別<br>
	* 
	* @return String
	*/
  public String getApplType() {
    return this.applType == null ? "" : this.applType;
  }

/**
	* 申請登錄類別<br>
	* 
  *
  * @param applType 申請登錄類別
	*/
  public void setApplType(String applType) {
    this.applType = applType;
  }

/**
	* 所得稅率<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getTaxRate() {
    return this.taxRate;
  }

/**
	* 所得稅率<br>
	* 
  *
  * @param taxRate 所得稅率
	*/
  public void setTaxRate(BigDecimal taxRate) {
    this.taxRate = taxRate;
  }

/**
	* 勞保等級<br>
	* 
	* @return Integer
	*/
  public int getSocialInsuClass() {
    return this.socialInsuClass;
  }

/**
	* 勞保等級<br>
	* 
  *
  * @param socialInsuClass 勞保等級
	*/
  public void setSocialInsuClass(int socialInsuClass) {
    this.socialInsuClass = socialInsuClass;
  }

/**
	* 職等平階起始年月<br>
	* 
	* @return String
	*/
  public String getPromotLevelYM() {
    return this.promotLevelYM == null ? "" : this.promotLevelYM;
  }

/**
	* 職等平階起始年月<br>
	* 
  *
  * @param promotLevelYM 職等平階起始年月
	*/
  public void setPromotLevelYM(String promotLevelYM) {
    this.promotLevelYM = promotLevelYM;
  }

/**
	* 晉陞主管年月<br>
	* 
	* @return String
	*/
  public String getDirectorYM() {
    return this.directorYM == null ? "" : this.directorYM;
  }

/**
	* 晉陞主管年月<br>
	* 
  *
  * @param directorYM 晉陞主管年月
	*/
  public void setDirectorYM(String directorYM) {
    this.directorYM = directorYM;
  }

/**
	* 登錄日期<br>
	* 
	* @return Integer
	*/
  public int getRecordDate() {
    return StaticTool.bcToRoc(this.recordDate);
  }

/**
	* 登錄日期<br>
	* 
  *
  * @param recordDate 登錄日期
  * @throws LogicException when Date Is Warn	*/
  public void setRecordDate(int recordDate) throws LogicException {
    this.recordDate = StaticTool.rocToBc(recordDate);
  }

/**
	* 發證日期<br>
	* 
	* @return Integer
	*/
  public int getExRecordDate() {
    return StaticTool.bcToRoc(this.exRecordDate);
  }

/**
	* 發證日期<br>
	* 
  *
  * @param exRecordDate 發證日期
  * @throws LogicException when Date Is Warn	*/
  public void setExRecordDate(int exRecordDate) throws LogicException {
    this.exRecordDate = StaticTool.rocToBc(exRecordDate);
  }

/**
	* 證書日期/測驗日期<br>
	* 
	* @return Integer
	*/
  public int getRxTrDate() {
    return StaticTool.bcToRoc(this.rxTrDate);
  }

/**
	* 證書日期/測驗日期<br>
	* 
  *
  * @param rxTrDate 證書日期/測驗日期
  * @throws LogicException when Date Is Warn	*/
  public void setRxTrDate(int rxTrDate) throws LogicException {
    this.rxTrDate = StaticTool.rocToBc(rxTrDate);
  }

/**
	* 證書字號<br>
	* 
	* @return String
	*/
  public String getExTrIdent() {
    return this.exTrIdent == null ? "" : this.exTrIdent;
  }

/**
	* 證書字號<br>
	* 
  *
  * @param exTrIdent 證書字號
	*/
  public void setExTrIdent(String exTrIdent) {
    this.exTrIdent = exTrIdent;
  }

/**
	* 中專證號<br>
	* 
	* @return String
	*/
  public String getExTrIdent2() {
    return this.exTrIdent2 == null ? "" : this.exTrIdent2;
  }

/**
	* 中專證號<br>
	* 
  *
  * @param exTrIdent2 中專證號
	*/
  public void setExTrIdent2(String exTrIdent2) {
    this.exTrIdent2 = exTrIdent2;
  }

/**
	* 投資型證號<br>
	* 
	* @return String
	*/
  public String getExTrIdent3() {
    return this.exTrIdent3 == null ? "" : this.exTrIdent3;
  }

/**
	* 投資型證號<br>
	* 
  *
  * @param exTrIdent3 投資型證號
	*/
  public void setExTrIdent3(String exTrIdent3) {
    this.exTrIdent3 = exTrIdent3;
  }

/**
	* 投資登錄日期<br>
	* 
	* @return Integer
	*/
  public int getExTrDate() {
    return StaticTool.bcToRoc(this.exTrDate);
  }

/**
	* 投資登錄日期<br>
	* 
  *
  * @param exTrDate 投資登錄日期
  * @throws LogicException when Date Is Warn	*/
  public void setExTrDate(int exTrDate) throws LogicException {
    this.exTrDate = StaticTool.rocToBc(exTrDate);
  }

/**
	* 報聘前年資(月表示)<br>
	* 
	* @return Integer
	*/
  public int getRegisterBefore() {
    return this.registerBefore;
  }

/**
	* 報聘前年資(月表示)<br>
	* 
  *
  * @param registerBefore 報聘前年資(月表示)
	*/
  public void setRegisterBefore(int registerBefore) {
    this.registerBefore = registerBefore;
  }

/**
	* 主管年資<br>
	* 
	* @return Integer
	*/
  public int getDirectorAfter() {
    return this.directorAfter;
  }

/**
	* 主管年資<br>
	* 
  *
  * @param directorAfter 主管年資
	*/
  public void setDirectorAfter(int directorAfter) {
    this.directorAfter = directorAfter;
  }

/**
	* 免體檢授權碼<br>
	* 
	* @return String
	*/
  public String getMedicalCode() {
    return this.medicalCode == null ? "" : this.medicalCode;
  }

/**
	* 免體檢授權碼<br>
	* 
  *
  * @param medicalCode 免體檢授權碼
	*/
  public void setMedicalCode(String medicalCode) {
    this.medicalCode = medicalCode;
  }

/**
	* 換證日期<br>
	* 
	* @return Integer
	*/
  public int getExChgDate() {
    return StaticTool.bcToRoc(this.exChgDate);
  }

/**
	* 換證日期<br>
	* 
  *
  * @param exChgDate 換證日期
  * @throws LogicException when Date Is Warn	*/
  public void setExChgDate(int exChgDate) throws LogicException {
    this.exChgDate = StaticTool.rocToBc(exChgDate);
  }

/**
	* 註銷日期<br>
	* 
	* @return Integer
	*/
  public int getExDelDate() {
    return StaticTool.bcToRoc(this.exDelDate);
  }

/**
	* 註銷日期<br>
	* 
  *
  * @param exDelDate 註銷日期
  * @throws LogicException when Date Is Warn	*/
  public void setExDelDate(int exDelDate) throws LogicException {
    this.exDelDate = StaticTool.rocToBc(exDelDate);
  }

/**
	* 申請業務類別<br>
	* 
	* @return String
	*/
  public String getApplCode() {
    return this.applCode == null ? "" : this.applCode;
  }

/**
	* 申請業務類別<br>
	* 
  *
  * @param applCode 申請業務類別
	*/
  public void setApplCode(String applCode) {
    this.applCode = applCode;
  }

/**
	* 初次登錄日<br>
	* 
	* @return Integer
	*/
  public int getFirstRegDate() {
    return StaticTool.bcToRoc(this.firstRegDate);
  }

/**
	* 初次登錄日<br>
	* 
  *
  * @param firstRegDate 初次登錄日
  * @throws LogicException when Date Is Warn	*/
  public void setFirstRegDate(int firstRegDate) throws LogicException {
    this.firstRegDate = StaticTool.rocToBc(firstRegDate);
  }

/**
	* 業務來源之專案<br>
	* 
	* @return String
	*/
  public String getAginSource() {
    return this.aginSource == null ? "" : this.aginSource;
  }

/**
	* 業務來源之專案<br>
	* 
  *
  * @param aginSource 業務來源之專案
	*/
  public void setAginSource(String aginSource) {
    this.aginSource = aginSource;
  }

/**
	* 單位代號<br>
	* 
	* @return String
	*/
  public String getAguiCenter() {
    return this.aguiCenter == null ? "" : this.aguiCenter;
  }

/**
	* 單位代號<br>
	* 
  *
  * @param aguiCenter 單位代號
	*/
  public void setAguiCenter(String aguiCenter) {
    this.aguiCenter = aguiCenter;
  }

/**
	* 業務人員身份證字號<br>
	* 業務人員身分證字號
	* @return String
	*/
  public String getAgentId() {
    return this.agentId == null ? "" : this.agentId;
  }

/**
	* 業務人員身份證字號<br>
	* 業務人員身分證字號
  *
  * @param agentId 業務人員身份證字號
	*/
  public void setAgentId(String agentId) {
    this.agentId = agentId;
  }

/**
	* 業務人員主管<br>
	* 
	* @return String
	*/
  public String getTopId() {
    return this.topId == null ? "" : this.topId;
  }

/**
	* 業務人員主管<br>
	* 
  *
  * @param topId 業務人員主管
	*/
  public void setTopId(String topId) {
    this.topId = topId;
  }

/**
	* 業務人員職級<br>
	* 
	* @return String
	*/
  public String getAgDegree() {
    return this.agDegree == null ? "" : this.agDegree;
  }

/**
	* 業務人員職級<br>
	* 
  *
  * @param agDegree 業務人員職級
	*/
  public void setAgDegree(String agDegree) {
    this.agDegree = agDegree;
  }

/**
	* 收費員指示碼<br>
	* 
	* @return String
	*/
  public String getCollectInd() {
    return this.collectInd == null ? "" : this.collectInd;
  }

/**
	* 收費員指示碼<br>
	* 
  *
  * @param collectInd 收費員指示碼
	*/
  public void setCollectInd(String collectInd) {
    this.collectInd = collectInd;
  }

/**
	* 制度別<br>
	* 
	* @return String
	*/
  public String getAgType1() {
    return this.agType1 == null ? "" : this.agType1;
  }

/**
	* 制度別<br>
	* 
  *
  * @param agType1 制度別
	*/
  public void setAgType1(String agType1) {
    this.agType1 = agType1;
  }

/**
	* 單雙合約碼<br>
	* 
	* @return String
	*/
  public String getContractInd() {
    return this.contractInd == null ? "" : this.contractInd;
  }

/**
	* 單雙合約碼<br>
	* 
  *
  * @param contractInd 單雙合約碼
	*/
  public void setContractInd(String contractInd) {
    this.contractInd = contractInd;
  }

/**
	* 單雙合約異動工作月<br>
	* 
	* @return String
	*/
  public String getContractIndYM() {
    return this.contractIndYM == null ? "" : this.contractIndYM;
  }

/**
	* 單雙合約異動工作月<br>
	* 
  *
  * @param contractIndYM 單雙合約異動工作月
	*/
  public void setContractIndYM(String contractIndYM) {
    this.contractIndYM = contractIndYM;
  }

/**
	* 身份別<br>
	* 
	* @return String
	*/
  public String getAgType2() {
    return this.agType2 == null ? "" : this.agType2;
  }

/**
	* 身份別<br>
	* 
  *
  * @param agType2 身份別
	*/
  public void setAgType2(String agType2) {
    this.agType2 = agType2;
  }

/**
	* 特殊人員碼<br>
	* 
	* @return String
	*/
  public String getAgType3() {
    return this.agType3 == null ? "" : this.agType3;
  }

/**
	* 特殊人員碼<br>
	* 
  *
  * @param agType3 特殊人員碼
	*/
  public void setAgType3(String agType3) {
    this.agType3 = agType3;
  }

/**
	* 新舊制別<br>
	* 
	* @return String
	*/
  public String getAgType4() {
    return this.agType4 == null ? "" : this.agType4;
  }

/**
	* 新舊制別<br>
	* 
  *
  * @param agType4 新舊制別
	*/
  public void setAgType4(String agType4) {
    this.agType4 = agType4;
  }

/**
	* 辦事員碼<br>
	* 
	* @return String
	*/
  public String getAginInd1() {
    return this.aginInd1 == null ? "" : this.aginInd1;
  }

/**
	* 辦事員碼<br>
	* 
  *
  * @param aginInd1 辦事員碼
	*/
  public void setAginInd1(String aginInd1) {
    this.aginInd1 = aginInd1;
  }

/**
	* 可招攬指示碼<br>
	* 
	* @return String
	*/
  public String getAgPoInd() {
    return this.agPoInd == null ? "" : this.agPoInd;
  }

/**
	* 可招攬指示碼<br>
	* 
  *
  * @param agPoInd 可招攬指示碼
	*/
  public void setAgPoInd(String agPoInd) {
    this.agPoInd = agPoInd;
  }

/**
	* 齊件否<br>
	* 
	* @return String
	*/
  public String getAgDocInd() {
    return this.agDocInd == null ? "" : this.agDocInd;
  }

/**
	* 齊件否<br>
	* 
  *
  * @param agDocInd 齊件否
	*/
  public void setAgDocInd(String agDocInd) {
    this.agDocInd = agDocInd;
  }

/**
	* 新舊人指示碼<br>
	* 
	* @return String
	*/
  public String getNewHireType() {
    return this.newHireType == null ? "" : this.newHireType;
  }

/**
	* 新舊人指示碼<br>
	* 
  *
  * @param newHireType 新舊人指示碼
	*/
  public void setNewHireType(String newHireType) {
    this.newHireType = newHireType;
  }

/**
	* 現職指示碼<br>
	* Y:現職
	* @return String
	*/
  public String getAgCurInd() {
    return this.agCurInd == null ? "" : this.agCurInd;
  }

/**
	* 現職指示碼<br>
	* Y:現職
  *
  * @param agCurInd 現職指示碼
	*/
  public void setAgCurInd(String agCurInd) {
    this.agCurInd = agCurInd;
  }

/**
	* 發文類別<br>
	* 
	* @return String
	*/
  public String getAgSendType() {
    return this.agSendType == null ? "" : this.agSendType;
  }

/**
	* 發文類別<br>
	* 
  *
  * @param agSendType 發文類別
	*/
  public void setAgSendType(String agSendType) {
    this.agSendType = agSendType;
  }

/**
	* 發文文號<br>
	* 
	* @return String
	*/
  public String getAgSendNo() {
    return this.agSendNo == null ? "" : this.agSendNo;
  }

/**
	* 發文文號<br>
	* 
  *
  * @param agSendNo 發文文號
	*/
  public void setAgSendNo(String agSendNo) {
    this.agSendNo = agSendNo;
  }

/**
	* 任職日期<br>
	* 
	* @return Integer
	*/
  public int getRegisterDate2() {
    return StaticTool.bcToRoc(this.registerDate2);
  }

/**
	* 任職日期<br>
	* 
  *
  * @param registerDate2 任職日期
  * @throws LogicException when Date Is Warn	*/
  public void setRegisterDate2(int registerDate2) throws LogicException {
    this.registerDate2 = StaticTool.rocToBc(registerDate2);
  }

/**
	* 回任日期<br>
	* 
	* @return Integer
	*/
  public int getAgReturnDate() {
    return StaticTool.bcToRoc(this.agReturnDate);
  }

/**
	* 回任日期<br>
	* 
  *
  * @param agReturnDate 回任日期
  * @throws LogicException when Date Is Warn	*/
  public void setAgReturnDate(int agReturnDate) throws LogicException {
    this.agReturnDate = StaticTool.rocToBc(agReturnDate);
  }

/**
	* 初次轉制日期<br>
	* 
	* @return Integer
	*/
  public int getAgTransferDateF() {
    return StaticTool.bcToRoc(this.agTransferDateF);
  }

/**
	* 初次轉制日期<br>
	* 
  *
  * @param agTransferDateF 初次轉制日期
  * @throws LogicException when Date Is Warn	*/
  public void setAgTransferDateF(int agTransferDateF) throws LogicException {
    this.agTransferDateF = StaticTool.rocToBc(agTransferDateF);
  }

/**
	* 轉制日期<br>
	* 
	* @return Integer
	*/
  public int getAgTransferDate() {
    return StaticTool.bcToRoc(this.agTransferDate);
  }

/**
	* 轉制日期<br>
	* 
  *
  * @param agTransferDate 轉制日期
  * @throws LogicException when Date Is Warn	*/
  public void setAgTransferDate(int agTransferDate) throws LogicException {
    this.agTransferDate = StaticTool.rocToBc(agTransferDate);
  }

/**
	* 初次晉升年月<br>
	* 
	* @return String
	*/
  public String getPromotYM() {
    return this.promotYM == null ? "" : this.promotYM;
  }

/**
	* 初次晉升年月<br>
	* 
  *
  * @param promotYM 初次晉升年月
	*/
  public void setPromotYM(String promotYM) {
    this.promotYM = promotYM;
  }

/**
	* 生效業績年月<br>
	* 
	* @return String
	*/
  public String getPromotYMF() {
    return this.promotYMF == null ? "" : this.promotYMF;
  }

/**
	* 生效業績年月<br>
	* 
  *
  * @param promotYMF 生效業績年月
	*/
  public void setPromotYMF(String promotYMF) {
    this.promotYMF = promotYMF;
  }

/**
	* 職務異動日<br>
	* 
	* @return Integer
	*/
  public int getAgPostChgDate() {
    return StaticTool.bcToRoc(this.agPostChgDate);
  }

/**
	* 職務異動日<br>
	* 
  *
  * @param agPostChgDate 職務異動日
  * @throws LogicException when Date Is Warn	*/
  public void setAgPostChgDate(int agPostChgDate) throws LogicException {
    this.agPostChgDate = StaticTool.rocToBc(agPostChgDate);
  }

/**
	* 扶養人數<br>
	* 
	* @return Integer
	*/
  public int getFamiliesTax() {
    return this.familiesTax;
  }

/**
	* 扶養人數<br>
	* 
  *
  * @param familiesTax 扶養人數
	*/
  public void setFamiliesTax(int familiesTax) {
    this.familiesTax = familiesTax;
  }

/**
	* 原區主任代號<br>
	* 
	* @return String
	*/
  public String getAgentCodeI() {
    return this.agentCodeI == null ? "" : this.agentCodeI;
  }

/**
	* 原區主任代號<br>
	* 
  *
  * @param agentCodeI 原區主任代號
	*/
  public void setAgentCodeI(String agentCodeI) {
    this.agentCodeI = agentCodeI;
  }

/**
	* 職等_系統<br>
	* 
	* @return String
	*/
  public String getAgLevelSys() {
    return this.agLevelSys == null ? "" : this.agLevelSys;
  }

/**
	* 職等_系統<br>
	* 
  *
  * @param agLevelSys 職等_系統
	*/
  public void setAgLevelSys(String agLevelSys) {
    this.agLevelSys = agLevelSys;
  }

/**
	* 內階職務<br>
	* 
	* @return String
	*/
  public String getAgPostIn() {
    return this.agPostIn == null ? "" : this.agPostIn;
  }

/**
	* 內階職務<br>
	* 
  *
  * @param agPostIn 內階職務
	*/
  public void setAgPostIn(String agPostIn) {
    this.agPostIn = agPostIn;
  }

/**
	* 駐在單位<br>
	* 
	* @return String
	*/
  public String getCenterCodeAcc() {
    return this.centerCodeAcc == null ? "" : this.centerCodeAcc;
  }

/**
	* 駐在單位<br>
	* 
  *
  * @param centerCodeAcc 駐在單位
	*/
  public void setCenterCodeAcc(String centerCodeAcc) {
    this.centerCodeAcc = centerCodeAcc;
  }

/**
	* 考核特殊碼<br>
	* 
	* @return String
	*/
  public String getEvalueInd() {
    return this.evalueInd == null ? "" : this.evalueInd;
  }

/**
	* 考核特殊碼<br>
	* 
  *
  * @param evalueInd 考核特殊碼
	*/
  public void setEvalueInd(String evalueInd) {
    this.evalueInd = evalueInd;
  }

/**
	* 辦法優待碼<br>
	* 
	* @return String
	*/
  public String getEvalueInd1() {
    return this.evalueInd1 == null ? "" : this.evalueInd1;
  }

/**
	* 辦法優待碼<br>
	* 
  *
  * @param evalueInd1 辦法優待碼
	*/
  public void setEvalueInd1(String evalueInd1) {
    this.evalueInd1 = evalueInd1;
  }

/**
	* 批次號碼<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getBatchNo() {
    return this.batchNo;
  }

/**
	* 批次號碼<br>
	* 
  *
  * @param batchNo 批次號碼
	*/
  public void setBatchNo(BigDecimal batchNo) {
    this.batchNo = batchNo;
  }

/**
	* 考核年月<br>
	* 
	* @return String
	*/
  public String getEvalueYM() {
    return this.evalueYM == null ? "" : this.evalueYM;
  }

/**
	* 考核年月<br>
	* 
  *
  * @param evalueYM 考核年月
	*/
  public void setEvalueYM(String evalueYM) {
    this.evalueYM = evalueYM;
  }

/**
	* 轉檔碼<br>
	* 
	* @return String
	*/
  public String getAgTransferCode() {
    return this.agTransferCode == null ? "" : this.agTransferCode;
  }

/**
	* 轉檔碼<br>
	* 
  *
  * @param agTransferCode 轉檔碼
	*/
  public void setAgTransferCode(String agTransferCode) {
    this.agTransferCode = agTransferCode;
  }

/**
	* 出生年月日<br>
	* 
	* @return Integer
	*/
  public int getBirth() {
    return StaticTool.bcToRoc(this.birth);
  }

/**
	* 出生年月日<br>
	* 
  *
  * @param birth 出生年月日
  * @throws LogicException when Date Is Warn	*/
  public void setBirth(int birth) throws LogicException {
    this.birth = StaticTool.rocToBc(birth);
  }

/**
	* 學歷<br>
	* 
	* @return String
	*/
  public String getEducation() {
    return this.education == null ? "" : this.education;
  }

/**
	* 學歷<br>
	* 
  *
  * @param education 學歷
	*/
  public void setEducation(String education) {
    this.education = education;
  }

/**
	* 勞退狀況<br>
	* 
	* @return String
	*/
  public String getLrInd() {
    return this.lrInd == null ? "" : this.lrInd;
  }

/**
	* 勞退狀況<br>
	* 
  *
  * @param lrInd 勞退狀況
	*/
  public void setLrInd(String lrInd) {
    this.lrInd = lrInd;
  }

/**
	* 資料處理時間<br>
	* 
	* @return Integer
	*/
  public int getProceccDate() {
    return StaticTool.bcToRoc(this.proceccDate);
  }

/**
	* 資料處理時間<br>
	* 
  *
  * @param proceccDate 資料處理時間
  * @throws LogicException when Date Is Warn	*/
  public void setProceccDate(int proceccDate) throws LogicException {
    this.proceccDate = StaticTool.rocToBc(proceccDate);
  }

/**
	* 離職/停約日<br>
	* 西元年
	* @return Integer
	*/
  public int getQuitDate() {
    return StaticTool.bcToRoc(this.quitDate);
  }

/**
	* 離職/停約日<br>
	* 西元年
  *
  * @param quitDate 離職/停約日
  * @throws LogicException when Date Is Warn	*/
  public void setQuitDate(int quitDate) throws LogicException {
    this.quitDate = StaticTool.rocToBc(quitDate);
  }

/**
	* 職務<br>
	* 職務
	* @return String
	*/
  public String getAgPost() {
    return this.agPost == null ? "" : this.agPost;
  }

/**
	* 職務<br>
	* 職務
  *
  * @param agPost 職務
	*/
  public void setAgPost(String agPost) {
    this.agPost = agPost;
  }

/**
	* 職等中文<br>
	* 職等中文
	* @return String
	*/
  public String getLevelNameChs() {
    return this.levelNameChs == null ? "" : this.levelNameChs;
  }

/**
	* 職等中文<br>
	* 職等中文
  *
  * @param levelNameChs 職等中文
	*/
  public void setLevelNameChs(String levelNameChs) {
    this.levelNameChs = levelNameChs;
  }

/**
	* 勞退碼<br>
	* 
	* @return String
	*/
  public String getLrSystemType() {
    return this.lrSystemType == null ? "" : this.lrSystemType;
  }

/**
	* 勞退碼<br>
	* 
  *
  * @param lrSystemType 勞退碼
	*/
  public void setLrSystemType(String lrSystemType) {
    this.lrSystemType = lrSystemType;
  }

/**
	* 年資_年<br>
	* 
	* @return Integer
	*/
  public int getSeniorityYY() {
    return this.seniorityYY;
  }

/**
	* 年資_年<br>
	* 
  *
  * @param seniorityYY 年資_年
	*/
  public void setSeniorityYY(int seniorityYY) {
    this.seniorityYY = seniorityYY;
  }

/**
	* 年資_月<br>
	* 
	* @return Integer
	*/
  public int getSeniorityMM() {
    return this.seniorityMM;
  }

/**
	* 年資_月<br>
	* 
  *
  * @param seniorityMM 年資_月
	*/
  public void setSeniorityMM(int seniorityMM) {
    this.seniorityMM = seniorityMM;
  }

/**
	* 年資_日<br>
	* 
	* @return Integer
	*/
  public int getSeniorityDD() {
    return this.seniorityDD;
  }

/**
	* 年資_日<br>
	* 
  *
  * @param seniorityDD 年資_日
	*/
  public void setSeniorityDD(int seniorityDD) {
    this.seniorityDD = seniorityDD;
  }

/**
	* 登錄處理事項<br>
	* 
	* @return String
	*/
  public String getAglaProcessInd() {
    return this.aglaProcessInd == null ? "" : this.aglaProcessInd;
  }

/**
	* 登錄處理事項<br>
	* 
  *
  * @param aglaProcessInd 登錄處理事項
	*/
  public void setAglaProcessInd(String aglaProcessInd) {
    this.aglaProcessInd = aglaProcessInd;
  }

/**
	* 登錄狀態<br>
	* 
	* @return String
	*/
  public String getStatusCode() {
    return this.statusCode == null ? "" : this.statusCode;
  }

/**
	* 登錄狀態<br>
	* 
  *
  * @param statusCode 登錄狀態
	*/
  public void setStatusCode(String statusCode) {
    this.statusCode = statusCode;
  }

/**
	* 註銷原因<br>
	* 
	* @return String
	*/
  public String getAglaCancelReason() {
    return this.aglaCancelReason == null ? "" : this.aglaCancelReason;
  }

/**
	* 註銷原因<br>
	* 
  *
  * @param aglaCancelReason 註銷原因
	*/
  public void setAglaCancelReason(String aglaCancelReason) {
    this.aglaCancelReason = aglaCancelReason;
  }

/**
	* 利變年金通報日<br>
	* 
	* @return Integer
	*/
  public int getISAnnApplDate() {
    return StaticTool.bcToRoc(this.iSAnnApplDate);
  }

/**
	* 利變年金通報日<br>
	* 
  *
  * @param iSAnnApplDate 利變年金通報日
  * @throws LogicException when Date Is Warn	*/
  public void setISAnnApplDate(int iSAnnApplDate) throws LogicException {
    this.iSAnnApplDate = StaticTool.rocToBc(iSAnnApplDate);
  }

/**
	* 外幣保單登入日<br>
	* 
	* @return Integer
	*/
  public int getRecordDateC() {
    return StaticTool.bcToRoc(this.recordDateC);
  }

/**
	* 外幣保單登入日<br>
	* 
  *
  * @param recordDateC 外幣保單登入日
  * @throws LogicException when Date Is Warn	*/
  public void setRecordDateC(int recordDateC) throws LogicException {
    this.recordDateC = StaticTool.rocToBc(recordDateC);
  }

/**
	* 停招/撤銷原因<br>
	* 
	* @return String
	*/
  public String getStopReason() {
    return this.stopReason == null ? "" : this.stopReason;
  }

/**
	* 停招/撤銷原因<br>
	* 
  *
  * @param stopReason 停招/撤銷原因
	*/
  public void setStopReason(String stopReason) {
    this.stopReason = stopReason;
  }

/**
	* 停止招攬起日<br>
	* 
	* @return Integer
	*/
  public int getStopStrDate() {
    return StaticTool.bcToRoc(this.stopStrDate);
  }

/**
	* 停止招攬起日<br>
	* 
  *
  * @param stopStrDate 停止招攬起日
  * @throws LogicException when Date Is Warn	*/
  public void setStopStrDate(int stopStrDate) throws LogicException {
    this.stopStrDate = StaticTool.rocToBc(stopStrDate);
  }

/**
	* 停止招攬迄日<br>
	* 
	* @return Integer
	*/
  public int getStopEndDate() {
    return StaticTool.bcToRoc(this.stopEndDate);
  }

/**
	* 停止招攬迄日<br>
	* 
  *
  * @param stopEndDate 停止招攬迄日
  * @throws LogicException when Date Is Warn	*/
  public void setStopEndDate(int stopEndDate) throws LogicException {
    this.stopEndDate = StaticTool.rocToBc(stopEndDate);
  }

/**
	* IFP登錄日<br>
	* 
	* @return Integer
	*/
  public int getIFPDate() {
    return StaticTool.bcToRoc(this.iFPDate);
  }

/**
	* IFP登錄日<br>
	* 
  *
  * @param iFPDate IFP登錄日
  * @throws LogicException when Date Is Warn	*/
  public void setIFPDate(int iFPDate) throws LogicException {
    this.iFPDate = StaticTool.rocToBc(iFPDate);
  }

/**
	* 撤銷起日<br>
	* 
	* @return Integer
	*/
  public int getEffectStrDate() {
    return StaticTool.bcToRoc(this.effectStrDate);
  }

/**
	* 撤銷起日<br>
	* 
  *
  * @param effectStrDate 撤銷起日
  * @throws LogicException when Date Is Warn	*/
  public void setEffectStrDate(int effectStrDate) throws LogicException {
    this.effectStrDate = StaticTool.rocToBc(effectStrDate);
  }

/**
	* 撤銷迄日<br>
	* 
	* @return Integer
	*/
  public int getEffectEndDate() {
    return StaticTool.bcToRoc(this.effectEndDate);
  }

/**
	* 撤銷迄日<br>
	* 
  *
  * @param effectEndDate 撤銷迄日
  * @throws LogicException when Date Is Warn	*/
  public void setEffectEndDate(int effectEndDate) throws LogicException {
    this.effectEndDate = StaticTool.rocToBc(effectEndDate);
  }

/**
	* 一般年金通報日<br>
	* 
	* @return Integer
	*/
  public int getAnnApplDate() {
    return StaticTool.bcToRoc(this.annApplDate);
  }

/**
	* 一般年金通報日<br>
	* 
  *
  * @param annApplDate 一般年金通報日
  * @throws LogicException when Date Is Warn	*/
  public void setAnnApplDate(int annApplDate) throws LogicException {
    this.annApplDate = StaticTool.rocToBc(annApplDate);
  }

/**
	* 單位名稱(駐在單位)<br>
	* 
	* @return String
	*/
  public String getCenterCodeAccName() {
    return this.centerCodeAccName == null ? "" : this.centerCodeAccName;
  }

/**
	* 單位名稱(駐在單位)<br>
	* 
  *
  * @param centerCodeAccName 單位名稱(駐在單位)
	*/
  public void setCenterCodeAccName(String centerCodeAccName) {
    this.centerCodeAccName = centerCodeAccName;
  }

/**
	* 重僱碼<br>
	* 
	* @return String
	*/
  public String getReHireCode() {
    return this.reHireCode == null ? "" : this.reHireCode;
  }

/**
	* 重僱碼<br>
	* 
  *
  * @param reHireCode 重僱碼
	*/
  public void setReHireCode(String reHireCode) {
    this.reHireCode = reHireCode;
  }

/**
	* 特別碼<br>
	* 
	* @return String
	*/
  public String getRSVDAdminCode() {
    return this.rSVDAdminCode == null ? "" : this.rSVDAdminCode;
  }

/**
	* 特別碼<br>
	* 
  *
  * @param rSVDAdminCode 特別碼
	*/
  public void setRSVDAdminCode(String rSVDAdminCode) {
    this.rSVDAdminCode = rSVDAdminCode;
  }

/**
	* 帳號<br>
	* 
	* @return String
	*/
  public String getAccount() {
    return this.account == null ? "" : this.account;
  }

/**
	* 帳號<br>
	* 
  *
  * @param account 帳號
	*/
  public void setAccount(String account) {
    this.account = account;
  }

/**
	* 優體測驗通過日<br>
	* 
	* @return String
	*/
  public String getPRPDate() {
    return this.pRPDate == null ? "" : this.pRPDate;
  }

/**
	* 優體測驗通過日<br>
	* 
  *
  * @param pRPDate 優體測驗通過日
	*/
  public void setPRPDate(String pRPDate) {
    this.pRPDate = pRPDate;
  }

/**
	* 戶籍地址郵遞區號<br>
	* 
	* @return String
	*/
  public String getZip() {
    return this.zip == null ? "" : this.zip;
  }

/**
	* 戶籍地址郵遞區號<br>
	* 
  *
  * @param zip 戶籍地址郵遞區號
	*/
  public void setZip(String zip) {
    this.zip = zip;
  }

/**
	* 戶籍地址<br>
	* 
	* @return String
	*/
  public String getAddress() {
    return this.address == null ? "" : this.address;
  }

/**
	* 戶籍地址<br>
	* 
  *
  * @param address 戶籍地址
	*/
  public void setAddress(String address) {
    this.address = address;
  }

/**
	* 住家電話<br>
	* 
	* @return String
	*/
  public String getPhoneH() {
    return this.phoneH == null ? "" : this.phoneH;
  }

/**
	* 住家電話<br>
	* 
  *
  * @param phoneH 住家電話
	*/
  public void setPhoneH(String phoneH) {
    this.phoneH = phoneH;
  }

/**
	* 手機電話<br>
	* 
	* @return String
	*/
  public String getPhoneC() {
    return this.phoneC == null ? "" : this.phoneC;
  }

/**
	* 手機電話<br>
	* 
  *
  * @param phoneC 手機電話
	*/
  public void setPhoneC(String phoneC) {
    this.phoneC = phoneC;
  }

/**
	* 基金銷售資格碼<br>
	* 
	* @return String
	*/
  public String getSalesQualInd() {
    return this.salesQualInd == null ? "" : this.salesQualInd;
  }

/**
	* 基金銷售資格碼<br>
	* 
  *
  * @param salesQualInd 基金銷售資格碼
	*/
  public void setSalesQualInd(String salesQualInd) {
    this.salesQualInd = salesQualInd;
  }

/**
	* 基金銷售資格日<br>
	* 
	* @return Integer
	*/
  public int getAgsqStartDate() {
    return StaticTool.bcToRoc(this.agsqStartDate);
  }

/**
	* 基金銷售資格日<br>
	* 
  *
  * @param agsqStartDate 基金銷售資格日
  * @throws LogicException when Date Is Warn	*/
  public void setAgsqStartDate(int agsqStartDate) throws LogicException {
    this.agsqStartDate = StaticTool.rocToBc(agsqStartDate);
  }

/**
	* 原住民羅馬拼音姓名<br>
	* 
	* @return String
	*/
  public String getPinYinNameIndi() {
    return this.pinYinNameIndi == null ? "" : this.pinYinNameIndi;
  }

/**
	* 原住民羅馬拼音姓名<br>
	* 
  *
  * @param pinYinNameIndi 原住民羅馬拼音姓名
	*/
  public void setPinYinNameIndi(String pinYinNameIndi) {
    this.pinYinNameIndi = pinYinNameIndi;
  }

/**
	* 電子郵件<br>
	* 
	* @return String
	*/
  public String getEmail() {
    return this.email == null ? "" : this.email;
  }

/**
	* 電子郵件<br>
	* 
  *
  * @param email 電子郵件
	*/
  public void setEmail(String email) {
    this.email = email;
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
    return "CdEmp [employeeNo=" + employeeNo + ", agentCode=" + agentCode + ", fullname=" + fullname + ", centerShortName=" + centerShortName + ", centerCodeName=" + centerCodeName + ", centerCode1=" + centerCode1
           + ", centerCode1Short=" + centerCode1Short + ", centerCode1Name=" + centerCode1Name + ", centerCode2=" + centerCode2 + ", centerCode2Short=" + centerCode2Short + ", centerCode2Name=" + centerCode2Name + ", centerCodeAcc1=" + centerCodeAcc1
           + ", centerCodeAcc1Name=" + centerCodeAcc1Name + ", centerCodeAcc2=" + centerCodeAcc2 + ", centerCodeAcc2Name=" + centerCodeAcc2Name + ", commLineCode=" + commLineCode + ", commLineType=" + commLineType + ", origIntroducerId=" + origIntroducerId
           + ", introducerInd=" + introducerInd + ", registerLevel=" + registerLevel + ", registerDate=" + registerDate + ", centerCode=" + centerCode + ", administratId=" + administratId + ", inputDate=" + inputDate
           + ", inputUser=" + inputUser + ", agStatusCode=" + agStatusCode + ", agStatusDate=" + agStatusDate + ", tranDate=" + tranDate + ", tranUser=" + tranUser + ", reRegisterDate=" + reRegisterDate
           + ", directorId=" + directorId + ", directorIdF=" + directorIdF + ", introducerId=" + introducerId + ", introducerIdF=" + introducerIdF + ", agLevel=" + agLevel + ", lastLevel=" + lastLevel
           + ", levelDate=" + levelDate + ", topLevel=" + topLevel + ", occpInd=" + occpInd + ", quotaAmt=" + quotaAmt + ", applType=" + applType + ", taxRate=" + taxRate
           + ", socialInsuClass=" + socialInsuClass + ", promotLevelYM=" + promotLevelYM + ", directorYM=" + directorYM + ", recordDate=" + recordDate + ", exRecordDate=" + exRecordDate + ", rxTrDate=" + rxTrDate
           + ", exTrIdent=" + exTrIdent + ", exTrIdent2=" + exTrIdent2 + ", exTrIdent3=" + exTrIdent3 + ", exTrDate=" + exTrDate + ", registerBefore=" + registerBefore + ", directorAfter=" + directorAfter
           + ", medicalCode=" + medicalCode + ", exChgDate=" + exChgDate + ", exDelDate=" + exDelDate + ", applCode=" + applCode + ", firstRegDate=" + firstRegDate + ", aginSource=" + aginSource
           + ", aguiCenter=" + aguiCenter + ", agentId=" + agentId + ", topId=" + topId + ", agDegree=" + agDegree + ", collectInd=" + collectInd + ", agType1=" + agType1
           + ", contractInd=" + contractInd + ", contractIndYM=" + contractIndYM + ", agType2=" + agType2 + ", agType3=" + agType3 + ", agType4=" + agType4 + ", aginInd1=" + aginInd1
           + ", agPoInd=" + agPoInd + ", agDocInd=" + agDocInd + ", newHireType=" + newHireType + ", agCurInd=" + agCurInd + ", agSendType=" + agSendType + ", agSendNo=" + agSendNo
           + ", registerDate2=" + registerDate2 + ", agReturnDate=" + agReturnDate + ", agTransferDateF=" + agTransferDateF + ", agTransferDate=" + agTransferDate + ", promotYM=" + promotYM + ", promotYMF=" + promotYMF
           + ", agPostChgDate=" + agPostChgDate + ", familiesTax=" + familiesTax + ", agentCodeI=" + agentCodeI + ", agLevelSys=" + agLevelSys + ", agPostIn=" + agPostIn + ", centerCodeAcc=" + centerCodeAcc
           + ", evalueInd=" + evalueInd + ", evalueInd1=" + evalueInd1 + ", batchNo=" + batchNo + ", evalueYM=" + evalueYM + ", agTransferCode=" + agTransferCode + ", birth=" + birth
           + ", education=" + education + ", lrInd=" + lrInd + ", proceccDate=" + proceccDate + ", quitDate=" + quitDate + ", agPost=" + agPost + ", levelNameChs=" + levelNameChs
           + ", lrSystemType=" + lrSystemType + ", seniorityYY=" + seniorityYY + ", seniorityMM=" + seniorityMM + ", seniorityDD=" + seniorityDD + ", aglaProcessInd=" + aglaProcessInd + ", statusCode=" + statusCode
           + ", aglaCancelReason=" + aglaCancelReason + ", iSAnnApplDate=" + iSAnnApplDate + ", recordDateC=" + recordDateC + ", stopReason=" + stopReason + ", stopStrDate=" + stopStrDate + ", stopEndDate=" + stopEndDate
           + ", iFPDate=" + iFPDate + ", effectStrDate=" + effectStrDate + ", effectEndDate=" + effectEndDate + ", annApplDate=" + annApplDate + ", centerCodeAccName=" + centerCodeAccName + ", reHireCode=" + reHireCode
           + ", rSVDAdminCode=" + rSVDAdminCode + ", account=" + account + ", pRPDate=" + pRPDate + ", zip=" + zip + ", address=" + address + ", phoneH=" + phoneH
           + ", phoneC=" + phoneC + ", salesQualInd=" + salesQualInd + ", agsqStartDate=" + agsqStartDate + ", pinYinNameIndi=" + pinYinNameIndi + ", email=" + email + ", createDate=" + createDate
           + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
