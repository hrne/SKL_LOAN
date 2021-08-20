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
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * PfDetail 業績計算明細檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`PfDetail`")
public class PfDetail implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -2572428859857977623L;

// 序號
  @Id
  @Column(name = "`LogNo`")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "`PfDetail_SEQ`")
  @SequenceGenerator(name = "`PfDetail_SEQ`", sequenceName = "`PfDetail_SEQ`", allocationSize = 1)
  private Long logNo = 0L;

  // 業績日期
  /* 業績日期=系統營業日1.撥貸(計件代碼變更)，為撥款日期2.部分償還、提前結案，為會計日 */
  @Column(name = "`PerfDate`")
  private int perfDate = 0;

  // 戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 額度編號
  @Column(name = "`FacmNo`")
  private int facmNo = 0;

  // 撥款序號
  @Column(name = "`BormNo`")
  private int bormNo = 0;

  // 交易內容檔序號
  /* 訂正序號相同 */
  @Column(name = "`BorxNo`")
  private int borxNo = 0;

  // 還款類別
  /* 0.撥款(計件代碼變更) 2.部分償還 3.提前結案 */
  @Column(name = "`RepayType`")
  private int repayType = 0;

  // 撥款日
  @Column(name = "`DrawdownDate`")
  private int drawdownDate = 0;

  // 撥款金額/追回金額
  /* 撥款、計件代碼變更為正值，部分償還、提前結案為負值(訂正相反) */
  @Column(name = "`DrawdownAmt`")
  private BigDecimal drawdownAmt = new BigDecimal("0");

  // 計件代碼
  @Column(name = "`PieceCode`", length = 1)
  private String pieceCode;

  // 已攤還期數
  @Column(name = "`RepaidPeriod`")
  private int repaidPeriod = 0;

  // 商品代碼
  /* FacMain.ProdNo 商品代碼 */
  @Column(name = "`ProdCode`", length = 5)
  private String prodCode;

  // 案件編號
  /* FacMain.CreditSysNo 徵審系統案號(eLoan案件編號) */
  @Column(name = "`CreditSysNo`")
  private int creditSysNo = 0;

  // 介紹人
  /* FacMain.Introducer 介紹人 */
  @Column(name = "`Introducer`", length = 8)
  private String introducer;

  // 協辦人員編
  @Column(name = "`Coorgnizer`", length = 6)
  private String coorgnizer;

  // 晤談一員編
  @Column(name = "`InterviewerA`", length = 6)
  private String interviewerA;

  // 晤談二員編
  @Column(name = "`InterviewerB`", length = 6)
  private String interviewerB;

  // 業績重算時是否以新員工資料更新介紹人所屬單位Y/N/null
  /* 單位、區部、部室及處經理代號、區經理代號、部經理代號 */
  @Column(name = "`IsReNewEmpUnit`", length = 1)
  private String isReNewEmpUnit;

  // 單位代號
  /* CdEmp.CenterCode 單位代號 */
  @Column(name = "`UnitCode`", length = 6)
  private String unitCode;

  // 區部代號
  /* CdBcm.District 區部 */
  @Column(name = "`DistCode`", length = 6)
  private String distCode;

  // 部室代號
  /* CdBcm.DeptCode 部室代號 */
  @Column(name = "`DeptCode`", length = 6)
  private String deptCode;

  // 處經理代號
  /* CdBcm.UnitManager 處經理代號 */
  @Column(name = "`UnitManager`", length = 8)
  private String unitManager;

  // 區經理代號
  /* CdBcm.DistManager 區經理代號 */
  @Column(name = "`DistManager`", length = 8)
  private String distManager;

  // 部經理代號
  /* CdBcm.DeptManager 部經理代號 */
  @Column(name = "`DeptManager`", length = 8)
  private String deptManager;

  // 介紹單位件數累計計算金額
  /* 同一額度、同一撥款工作月、同一計件代碼，累計計算金額追回未繳足3期期款即結清（含部分還款達60萬之案件)同案件新貸件(代碼2&amp;B)於6個月內撥款，連同(代碼1&amp;A)計算 */
  @Column(name = "`ComputeItAmtFac`")
  private BigDecimal computeItAmtFac = new BigDecimal("0");

  // 介紹單位件數
  /* 0,無計件 1.有計件寫入介紹人業績明細檔時，代碼(2&amp;B)之是否計件寫入(代碼1&amp;A) */
  @Column(name = "`ItPerfCnt`")
  private BigDecimal itPerfCnt = new BigDecimal("0");

  // 介紹人業績金額計算金額
  /* 撥款金額、追回差額(未繳足3期期款即結清、含部分還款達60萬之案件) */
  @Column(name = "`ComputeItAmt`")
  private BigDecimal computeItAmt = new BigDecimal("0");

  // 介紹人業績金額
  /* 單筆計算 */
  @Column(name = "`ItPerfAmt`")
  private BigDecimal itPerfAmt = new BigDecimal("0");

  // 介紹人換算業績
  /* 單筆計算 */
  @Column(name = "`ItPerfEqAmt`")
  private BigDecimal itPerfEqAmt = new BigDecimal("0");

  // 介紹人業務報酬
  /* 單筆計算 */
  @Column(name = "`ItPerfReward`")
  private BigDecimal itPerfReward = new BigDecimal("0");

  // 介紹人介紹獎金計算金額
  /* 同一額度、同一撥款工作月，同一計件代碼，累計計算 */
  @Column(name = "`ComputeItBonusAmt`")
  private BigDecimal computeItBonusAmt = new BigDecimal("0");

  // 介紹人介紹獎金
  /* 依累計金額計算，扣除已計追回撥款後計息期間未逾一個月即結清(含部分還款達60萬元)案件 */
  @Column(name = "`ItBonus`")
  private BigDecimal itBonus = new BigDecimal("0");

  // 介紹人加碼獎勵津貼計算金額
  /* 同一額度、同一撥款工作月，累計計算 */
  @Column(name = "`ComputeAddBonusAmt`")
  private BigDecimal computeAddBonusAmt = new BigDecimal("0");

  // 介紹人加碼獎勵津貼
  /* 依累計金額計算，扣除已計追回撥款後計息期間未逾一個月即結清(含部分還款達60萬元)案件 */
  @Column(name = "`ItAddBonus`")
  private BigDecimal itAddBonus = new BigDecimal("0");

  // 協辦人員協辦獎金計算金額
  /* 同一額度、同一撥款工作月，累計計算 */
  @Column(name = "`ComputeCoBonusAmt`")
  private BigDecimal computeCoBonusAmt = new BigDecimal("0");

  // 協辦人員協辦獎金
  /* 依協辦人員協辦獎金計算金額計算，扣除已計追回撥款後計息期間未逾一個月即結清(含部分還款達60萬元)案件 */
  @Column(name = "`CoorgnizerBonus`")
  private BigDecimal coorgnizerBonus = new BigDecimal("0");

  // 房貸專員
  /* FacMain.BusinessOfficer 放款業務專員 */
  @Column(name = "`BsOfficer`", length = 6)
  private String bsOfficer;

  // 部室代號
  /* 應以PfBsOfficer(房貸專員業績目標檔)計算業績 */
  @Column(name = "`BsDeptCode`", length = 6)
  private String bsDeptCode;

  // 房貸專員件數累計計算金額
  /* 同一額度、同一撥款工作月、同一計件代碼，累計計算金額追回未逾一個月即結清(含部分還款達60萬元)同案件新貸件(代碼2&amp;B)於6個月內撥款，連同(代碼1&amp;A)計算 */
  @Column(name = "`ComputeBsAmtFac`")
  private BigDecimal computeBsAmtFac = new BigDecimal("0");

  // 房貸專員件數
  @Column(name = "`BsPerfCnt`")
  private BigDecimal bsPerfCnt = new BigDecimal("0");

  // 房貸專員計算金額
  /* 撥款金額、追回差額(未繳足3期期款即結清、含部分還款達60萬之案件) */
  @Column(name = "`ComputeBsAmt`")
  private BigDecimal computeBsAmt = new BigDecimal("0");

  // 房貸專員業績金額
  /* 單筆計算 */
  @Column(name = "`BsPerfAmt`")
  private BigDecimal bsPerfAmt = new BigDecimal("0");

  // 工作月
  @Column(name = "`WorkMonth`")
  private int workMonth = 0;

  // 工作季
  @Column(name = "`WorkSeason`")
  private int workSeason = 0;

  // 連同計件代碼
  /* 同案件新貸件(代碼2&amp;B)於6個月內撥款，連同(代碼1&amp;A)計算 */
  @Column(name = "`PieceCodeCombine`", length = 1)
  private String pieceCodeCombine;

  // 理財型房貸(Y/N)
  /* 只寫入首筆撥貸 */
  @Column(name = "`IsProdFinancial`", length = 1)
  private String isProdFinancial;

  // 介紹人是否為15日薪(Y/Null)
  @Column(name = "`IsIntroducerDay15`", length = 1)
  private String isIntroducerDay15;

  // 協辦人員是否為15日薪(Y/Null)
  @Column(name = "`IsCoorgnizerDay15`", length = 1)
  private String isCoorgnizerDay15;

  // 計算業績排除商品別(Y/Null)
  /* 全部業績 */
  @Column(name = "`IsProdExclude1`", length = 1)
  private String isProdExclude1;

  // 計算業績排除商品別(Y/Null)
  /* 換算業績、業務報酬 */
  @Column(name = "`IsProdExclude2`", length = 1)
  private String isProdExclude2;

  // 計算業績排除商品別(Y/Null)
  /* 介紹獎金 */
  @Column(name = "`IsProdExclude3`", length = 1)
  private String isProdExclude3;

  // 計算業績排除商品別(Y/Nnull)
  /* 加碼獎勵津貼 */
  @Column(name = "`IsProdExclude4`", length = 1)
  private String isProdExclude4;

  // 計算業績排除商品別(Y/Null)
  /* 協辦獎金 */
  @Column(name = "`IsProdExclude5`", length = 1)
  private String isProdExclude5;

  // 是否屬排徐部門別(Y/Null)
  /* 全部業績 */
  @Column(name = "`IsDeptExclude1`", length = 1)
  private String isDeptExclude1;

  // 是否屬排徐部門別(Y/Null)
  /* 換算業績、業務報酬 */
  @Column(name = "`IsDeptExclude2`", length = 1)
  private String isDeptExclude2;

  // 是否屬排徐部門別(Y/Null)
  /* 介紹獎金 */
  @Column(name = "`IsDeptExclude3`", length = 1)
  private String isDeptExclude3;

  // 是否屬排徐部門別(Y/Null)
  /* 加碼獎勵津貼 */
  @Column(name = "`IsDeptExclude4`", length = 1)
  private String isDeptExclude4;

  // 是否屬排徐部門別(Y/Null)
  /* 協辦獎金 */
  @Column(name = "`IsDeptExclude5`", length = 1)
  private String isDeptExclude5;

  // 是否屬15日薪被排除(Y/Null/E-屬15日薪未設排除條件)
  /* 全部業績 */
  @Column(name = "`IsDay15Exclude1`", length = 1)
  private String isDay15Exclude1;

  // 是否屬15日薪被排除(Y/Null/E-屬15日薪未設排除條件)
  /* 換算業績、業務報酬 */
  @Column(name = "`IsDay15Exclude2`", length = 1)
  private String isDay15Exclude2;

  // 是否屬15日薪被排除(Y/Null/E-屬15日薪未設排除條件)
  /* 介紹獎金 */
  @Column(name = "`IsDay15Exclude3`", length = 1)
  private String isDay15Exclude3;

  // 是否屬15日薪被排除(Y/Null/E-屬15日薪未設排除條件)
  /* 加碼獎勵津貼 */
  @Column(name = "`IsDay15Exclude4`", length = 1)
  private String isDay15Exclude4;

  // 是否屬15日薪被排除(Y/Null/E-屬15日薪未設排除條件)
  /* 協辦獎金 */
  @Column(name = "`IsDay15Exclude5`", length = 1)
  private String isDay15Exclude5;

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
	* 業績日期<br>
	* 業績日期=系統營業日
1.撥貸(計件代碼變更)，為撥款日期
2.部分償還、提前結案，為會計日
	* @return Integer
	*/
  public int getPerfDate() {
    return StaticTool.bcToRoc(this.perfDate);
  }

/**
	* 業績日期<br>
	* 業績日期=系統營業日
1.撥貸(計件代碼變更)，為撥款日期
2.部分償還、提前結案，為會計日
  *
  * @param perfDate 業績日期
  * @throws LogicException when Date Is Warn	*/
  public void setPerfDate(int perfDate) throws LogicException {
    this.perfDate = StaticTool.rocToBc(perfDate);
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
	* 交易內容檔序號<br>
	* 訂正序號相同
	* @return Integer
	*/
  public int getBorxNo() {
    return this.borxNo;
  }

/**
	* 交易內容檔序號<br>
	* 訂正序號相同
  *
  * @param borxNo 交易內容檔序號
	*/
  public void setBorxNo(int borxNo) {
    this.borxNo = borxNo;
  }

/**
	* 還款類別<br>
	* 0.撥款(計件代碼變更) 2.部分償還 3.提前結案
	* @return Integer
	*/
  public int getRepayType() {
    return this.repayType;
  }

/**
	* 還款類別<br>
	* 0.撥款(計件代碼變更) 2.部分償還 3.提前結案
  *
  * @param repayType 還款類別
	*/
  public void setRepayType(int repayType) {
    this.repayType = repayType;
  }

/**
	* 撥款日<br>
	* 
	* @return Integer
	*/
  public int getDrawdownDate() {
    return StaticTool.bcToRoc(this.drawdownDate);
  }

/**
	* 撥款日<br>
	* 
  *
  * @param drawdownDate 撥款日
  * @throws LogicException when Date Is Warn	*/
  public void setDrawdownDate(int drawdownDate) throws LogicException {
    this.drawdownDate = StaticTool.rocToBc(drawdownDate);
  }

/**
	* 撥款金額/追回金額<br>
	* 撥款、計件代碼變更為正值，部分償還、提前結案為負值(訂正相反)
	* @return BigDecimal
	*/
  public BigDecimal getDrawdownAmt() {
    return this.drawdownAmt;
  }

/**
	* 撥款金額/追回金額<br>
	* 撥款、計件代碼變更為正值，部分償還、提前結案為負值(訂正相反)
  *
  * @param drawdownAmt 撥款金額/追回金額
	*/
  public void setDrawdownAmt(BigDecimal drawdownAmt) {
    this.drawdownAmt = drawdownAmt;
  }

/**
	* 計件代碼<br>
	* 
	* @return String
	*/
  public String getPieceCode() {
    return this.pieceCode == null ? "" : this.pieceCode;
  }

/**
	* 計件代碼<br>
	* 
  *
  * @param pieceCode 計件代碼
	*/
  public void setPieceCode(String pieceCode) {
    this.pieceCode = pieceCode;
  }

/**
	* 已攤還期數<br>
	* 
	* @return Integer
	*/
  public int getRepaidPeriod() {
    return this.repaidPeriod;
  }

/**
	* 已攤還期數<br>
	* 
  *
  * @param repaidPeriod 已攤還期數
	*/
  public void setRepaidPeriod(int repaidPeriod) {
    this.repaidPeriod = repaidPeriod;
  }

/**
	* 商品代碼<br>
	* FacMain.ProdNo 商品代碼
	* @return String
	*/
  public String getProdCode() {
    return this.prodCode == null ? "" : this.prodCode;
  }

/**
	* 商品代碼<br>
	* FacMain.ProdNo 商品代碼
  *
  * @param prodCode 商品代碼
	*/
  public void setProdCode(String prodCode) {
    this.prodCode = prodCode;
  }

/**
	* 案件編號<br>
	* FacMain.CreditSysNo 徵審系統案號(eLoan案件編號)
	* @return Integer
	*/
  public int getCreditSysNo() {
    return this.creditSysNo;
  }

/**
	* 案件編號<br>
	* FacMain.CreditSysNo 徵審系統案號(eLoan案件編號)
  *
  * @param creditSysNo 案件編號
	*/
  public void setCreditSysNo(int creditSysNo) {
    this.creditSysNo = creditSysNo;
  }

/**
	* 介紹人<br>
	* FacMain.Introducer 介紹人
	* @return String
	*/
  public String getIntroducer() {
    return this.introducer == null ? "" : this.introducer;
  }

/**
	* 介紹人<br>
	* FacMain.Introducer 介紹人
  *
  * @param introducer 介紹人
	*/
  public void setIntroducer(String introducer) {
    this.introducer = introducer;
  }

/**
	* 協辦人員編<br>
	* 
	* @return String
	*/
  public String getCoorgnizer() {
    return this.coorgnizer == null ? "" : this.coorgnizer;
  }

/**
	* 協辦人員編<br>
	* 
  *
  * @param coorgnizer 協辦人員編
	*/
  public void setCoorgnizer(String coorgnizer) {
    this.coorgnizer = coorgnizer;
  }

/**
	* 晤談一員編<br>
	* 
	* @return String
	*/
  public String getInterviewerA() {
    return this.interviewerA == null ? "" : this.interviewerA;
  }

/**
	* 晤談一員編<br>
	* 
  *
  * @param interviewerA 晤談一員編
	*/
  public void setInterviewerA(String interviewerA) {
    this.interviewerA = interviewerA;
  }

/**
	* 晤談二員編<br>
	* 
	* @return String
	*/
  public String getInterviewerB() {
    return this.interviewerB == null ? "" : this.interviewerB;
  }

/**
	* 晤談二員編<br>
	* 
  *
  * @param interviewerB 晤談二員編
	*/
  public void setInterviewerB(String interviewerB) {
    this.interviewerB = interviewerB;
  }

/**
	* 業績重算時是否以新員工資料更新介紹人所屬單位Y/N/null<br>
	* 單位、區部、部室及處經理代號、區經理代號、部經理代號
	* @return String
	*/
  public String getIsReNewEmpUnit() {
    return this.isReNewEmpUnit == null ? "" : this.isReNewEmpUnit;
  }

/**
	* 業績重算時是否以新員工資料更新介紹人所屬單位Y/N/null<br>
	* 單位、區部、部室及處經理代號、區經理代號、部經理代號
  *
  * @param isReNewEmpUnit 業績重算時是否以新員工資料更新介紹人所屬單位Y/N/null
	*/
  public void setIsReNewEmpUnit(String isReNewEmpUnit) {
    this.isReNewEmpUnit = isReNewEmpUnit;
  }

/**
	* 單位代號<br>
	* CdEmp.CenterCode 單位代號
	* @return String
	*/
  public String getUnitCode() {
    return this.unitCode == null ? "" : this.unitCode;
  }

/**
	* 單位代號<br>
	* CdEmp.CenterCode 單位代號
  *
  * @param unitCode 單位代號
	*/
  public void setUnitCode(String unitCode) {
    this.unitCode = unitCode;
  }

/**
	* 區部代號<br>
	* CdBcm.District 區部
	* @return String
	*/
  public String getDistCode() {
    return this.distCode == null ? "" : this.distCode;
  }

/**
	* 區部代號<br>
	* CdBcm.District 區部
  *
  * @param distCode 區部代號
	*/
  public void setDistCode(String distCode) {
    this.distCode = distCode;
  }

/**
	* 部室代號<br>
	* CdBcm.DeptCode 部室代號
	* @return String
	*/
  public String getDeptCode() {
    return this.deptCode == null ? "" : this.deptCode;
  }

/**
	* 部室代號<br>
	* CdBcm.DeptCode 部室代號
  *
  * @param deptCode 部室代號
	*/
  public void setDeptCode(String deptCode) {
    this.deptCode = deptCode;
  }

/**
	* 處經理代號<br>
	* CdBcm.UnitManager 處經理代號
	* @return String
	*/
  public String getUnitManager() {
    return this.unitManager == null ? "" : this.unitManager;
  }

/**
	* 處經理代號<br>
	* CdBcm.UnitManager 處經理代號
  *
  * @param unitManager 處經理代號
	*/
  public void setUnitManager(String unitManager) {
    this.unitManager = unitManager;
  }

/**
	* 區經理代號<br>
	* CdBcm.DistManager 區經理代號
	* @return String
	*/
  public String getDistManager() {
    return this.distManager == null ? "" : this.distManager;
  }

/**
	* 區經理代號<br>
	* CdBcm.DistManager 區經理代號
  *
  * @param distManager 區經理代號
	*/
  public void setDistManager(String distManager) {
    this.distManager = distManager;
  }

/**
	* 部經理代號<br>
	* CdBcm.DeptManager 部經理代號
	* @return String
	*/
  public String getDeptManager() {
    return this.deptManager == null ? "" : this.deptManager;
  }

/**
	* 部經理代號<br>
	* CdBcm.DeptManager 部經理代號
  *
  * @param deptManager 部經理代號
	*/
  public void setDeptManager(String deptManager) {
    this.deptManager = deptManager;
  }

/**
	* 介紹單位件數累計計算金額<br>
	* 同一額度、同一撥款工作月、同一計件代碼，累計計算金額
追回未繳足3期期款即結清（含部分還款達60萬之案件)
同案件新貸件(代碼2&amp;B)於6個月內撥款，連同(代碼1&amp;A)計算
	* @return BigDecimal
	*/
  public BigDecimal getComputeItAmtFac() {
    return this.computeItAmtFac;
  }

/**
	* 介紹單位件數累計計算金額<br>
	* 同一額度、同一撥款工作月、同一計件代碼，累計計算金額
追回未繳足3期期款即結清（含部分還款達60萬之案件)
同案件新貸件(代碼2&amp;B)於6個月內撥款，連同(代碼1&amp;A)計算
  *
  * @param computeItAmtFac 介紹單位件數累計計算金額
	*/
  public void setComputeItAmtFac(BigDecimal computeItAmtFac) {
    this.computeItAmtFac = computeItAmtFac;
  }

/**
	* 介紹單位件數<br>
	* 0,無計件 1.有計件
寫入介紹人業績明細檔時，代碼(2&amp;B)之是否計件寫入(代碼1&amp;A)
	* @return BigDecimal
	*/
  public BigDecimal getItPerfCnt() {
    return this.itPerfCnt;
  }

/**
	* 介紹單位件數<br>
	* 0,無計件 1.有計件
寫入介紹人業績明細檔時，代碼(2&amp;B)之是否計件寫入(代碼1&amp;A)
  *
  * @param itPerfCnt 介紹單位件數
	*/
  public void setItPerfCnt(BigDecimal itPerfCnt) {
    this.itPerfCnt = itPerfCnt;
  }

/**
	* 介紹人業績金額計算金額<br>
	* 撥款金額、追回差額(未繳足3期期款即結清、含部分還款達60萬之案件)
	* @return BigDecimal
	*/
  public BigDecimal getComputeItAmt() {
    return this.computeItAmt;
  }

/**
	* 介紹人業績金額計算金額<br>
	* 撥款金額、追回差額(未繳足3期期款即結清、含部分還款達60萬之案件)
  *
  * @param computeItAmt 介紹人業績金額計算金額
	*/
  public void setComputeItAmt(BigDecimal computeItAmt) {
    this.computeItAmt = computeItAmt;
  }

/**
	* 介紹人業績金額<br>
	* 單筆計算
	* @return BigDecimal
	*/
  public BigDecimal getItPerfAmt() {
    return this.itPerfAmt;
  }

/**
	* 介紹人業績金額<br>
	* 單筆計算
  *
  * @param itPerfAmt 介紹人業績金額
	*/
  public void setItPerfAmt(BigDecimal itPerfAmt) {
    this.itPerfAmt = itPerfAmt;
  }

/**
	* 介紹人換算業績<br>
	* 單筆計算
	* @return BigDecimal
	*/
  public BigDecimal getItPerfEqAmt() {
    return this.itPerfEqAmt;
  }

/**
	* 介紹人換算業績<br>
	* 單筆計算
  *
  * @param itPerfEqAmt 介紹人換算業績
	*/
  public void setItPerfEqAmt(BigDecimal itPerfEqAmt) {
    this.itPerfEqAmt = itPerfEqAmt;
  }

/**
	* 介紹人業務報酬<br>
	* 單筆計算
	* @return BigDecimal
	*/
  public BigDecimal getItPerfReward() {
    return this.itPerfReward;
  }

/**
	* 介紹人業務報酬<br>
	* 單筆計算
  *
  * @param itPerfReward 介紹人業務報酬
	*/
  public void setItPerfReward(BigDecimal itPerfReward) {
    this.itPerfReward = itPerfReward;
  }

/**
	* 介紹人介紹獎金計算金額<br>
	* 同一額度、同一撥款工作月，同一計件代碼，累計計算
	* @return BigDecimal
	*/
  public BigDecimal getComputeItBonusAmt() {
    return this.computeItBonusAmt;
  }

/**
	* 介紹人介紹獎金計算金額<br>
	* 同一額度、同一撥款工作月，同一計件代碼，累計計算
  *
  * @param computeItBonusAmt 介紹人介紹獎金計算金額
	*/
  public void setComputeItBonusAmt(BigDecimal computeItBonusAmt) {
    this.computeItBonusAmt = computeItBonusAmt;
  }

/**
	* 介紹人介紹獎金<br>
	* 依累計金額計算，扣除已計
追回撥款後計息期間未逾一個月即結清(含部分還款達60萬元)案件
	* @return BigDecimal
	*/
  public BigDecimal getItBonus() {
    return this.itBonus;
  }

/**
	* 介紹人介紹獎金<br>
	* 依累計金額計算，扣除已計
追回撥款後計息期間未逾一個月即結清(含部分還款達60萬元)案件
  *
  * @param itBonus 介紹人介紹獎金
	*/
  public void setItBonus(BigDecimal itBonus) {
    this.itBonus = itBonus;
  }

/**
	* 介紹人加碼獎勵津貼計算金額<br>
	* 同一額度、同一撥款工作月，累計計算
	* @return BigDecimal
	*/
  public BigDecimal getComputeAddBonusAmt() {
    return this.computeAddBonusAmt;
  }

/**
	* 介紹人加碼獎勵津貼計算金額<br>
	* 同一額度、同一撥款工作月，累計計算
  *
  * @param computeAddBonusAmt 介紹人加碼獎勵津貼計算金額
	*/
  public void setComputeAddBonusAmt(BigDecimal computeAddBonusAmt) {
    this.computeAddBonusAmt = computeAddBonusAmt;
  }

/**
	* 介紹人加碼獎勵津貼<br>
	* 依累計金額計算，扣除已計
追回撥款後計息期間未逾一個月即結清(含部分還款達60萬元)案件
	* @return BigDecimal
	*/
  public BigDecimal getItAddBonus() {
    return this.itAddBonus;
  }

/**
	* 介紹人加碼獎勵津貼<br>
	* 依累計金額計算，扣除已計
追回撥款後計息期間未逾一個月即結清(含部分還款達60萬元)案件
  *
  * @param itAddBonus 介紹人加碼獎勵津貼
	*/
  public void setItAddBonus(BigDecimal itAddBonus) {
    this.itAddBonus = itAddBonus;
  }

/**
	* 協辦人員協辦獎金計算金額<br>
	* 同一額度、同一撥款工作月，累計計算
	* @return BigDecimal
	*/
  public BigDecimal getComputeCoBonusAmt() {
    return this.computeCoBonusAmt;
  }

/**
	* 協辦人員協辦獎金計算金額<br>
	* 同一額度、同一撥款工作月，累計計算
  *
  * @param computeCoBonusAmt 協辦人員協辦獎金計算金額
	*/
  public void setComputeCoBonusAmt(BigDecimal computeCoBonusAmt) {
    this.computeCoBonusAmt = computeCoBonusAmt;
  }

/**
	* 協辦人員協辦獎金<br>
	* 依協辦人員協辦獎金計算金額計算，扣除已計
追回撥款後計息期間未逾一個月即結清(含部分還款達60萬元)案件
	* @return BigDecimal
	*/
  public BigDecimal getCoorgnizerBonus() {
    return this.coorgnizerBonus;
  }

/**
	* 協辦人員協辦獎金<br>
	* 依協辦人員協辦獎金計算金額計算，扣除已計
追回撥款後計息期間未逾一個月即結清(含部分還款達60萬元)案件
  *
  * @param coorgnizerBonus 協辦人員協辦獎金
	*/
  public void setCoorgnizerBonus(BigDecimal coorgnizerBonus) {
    this.coorgnizerBonus = coorgnizerBonus;
  }

/**
	* 房貸專員<br>
	* FacMain.BusinessOfficer 放款業務專員
	* @return String
	*/
  public String getBsOfficer() {
    return this.bsOfficer == null ? "" : this.bsOfficer;
  }

/**
	* 房貸專員<br>
	* FacMain.BusinessOfficer 放款業務專員
  *
  * @param bsOfficer 房貸專員
	*/
  public void setBsOfficer(String bsOfficer) {
    this.bsOfficer = bsOfficer;
  }

/**
	* 部室代號<br>
	* 應以PfBsOfficer(房貸專員業績目標檔)計算業績
	* @return String
	*/
  public String getBsDeptCode() {
    return this.bsDeptCode == null ? "" : this.bsDeptCode;
  }

/**
	* 部室代號<br>
	* 應以PfBsOfficer(房貸專員業績目標檔)計算業績
  *
  * @param bsDeptCode 部室代號
	*/
  public void setBsDeptCode(String bsDeptCode) {
    this.bsDeptCode = bsDeptCode;
  }

/**
	* 房貸專員件數累計計算金額<br>
	* 同一額度、同一撥款工作月、同一計件代碼，累計計算金額
追回未逾一個月即結清(含部分還款達60萬元)
同案件新貸件(代碼2&amp;B)於6個月內撥款，連同(代碼1&amp;A)計算
	* @return BigDecimal
	*/
  public BigDecimal getComputeBsAmtFac() {
    return this.computeBsAmtFac;
  }

/**
	* 房貸專員件數累計計算金額<br>
	* 同一額度、同一撥款工作月、同一計件代碼，累計計算金額
追回未逾一個月即結清(含部分還款達60萬元)
同案件新貸件(代碼2&amp;B)於6個月內撥款，連同(代碼1&amp;A)計算
  *
  * @param computeBsAmtFac 房貸專員件數累計計算金額
	*/
  public void setComputeBsAmtFac(BigDecimal computeBsAmtFac) {
    this.computeBsAmtFac = computeBsAmtFac;
  }

/**
	* 房貸專員件數<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getBsPerfCnt() {
    return this.bsPerfCnt;
  }

/**
	* 房貸專員件數<br>
	* 
  *
  * @param bsPerfCnt 房貸專員件數
	*/
  public void setBsPerfCnt(BigDecimal bsPerfCnt) {
    this.bsPerfCnt = bsPerfCnt;
  }

/**
	* 房貸專員計算金額<br>
	* 撥款金額、追回差額(未繳足3期期款即結清、含部分還款達60萬之案件)
	* @return BigDecimal
	*/
  public BigDecimal getComputeBsAmt() {
    return this.computeBsAmt;
  }

/**
	* 房貸專員計算金額<br>
	* 撥款金額、追回差額(未繳足3期期款即結清、含部分還款達60萬之案件)
  *
  * @param computeBsAmt 房貸專員計算金額
	*/
  public void setComputeBsAmt(BigDecimal computeBsAmt) {
    this.computeBsAmt = computeBsAmt;
  }

/**
	* 房貸專員業績金額<br>
	* 單筆計算
	* @return BigDecimal
	*/
  public BigDecimal getBsPerfAmt() {
    return this.bsPerfAmt;
  }

/**
	* 房貸專員業績金額<br>
	* 單筆計算
  *
  * @param bsPerfAmt 房貸專員業績金額
	*/
  public void setBsPerfAmt(BigDecimal bsPerfAmt) {
    this.bsPerfAmt = bsPerfAmt;
  }

/**
	* 工作月<br>
	* 
	* @return Integer
	*/
  public int getWorkMonth() {
    return this.workMonth;
  }

/**
	* 工作月<br>
	* 
  *
  * @param workMonth 工作月
	*/
  public void setWorkMonth(int workMonth) {
    this.workMonth = workMonth;
  }

/**
	* 工作季<br>
	* 
	* @return Integer
	*/
  public int getWorkSeason() {
    return this.workSeason;
  }

/**
	* 工作季<br>
	* 
  *
  * @param workSeason 工作季
	*/
  public void setWorkSeason(int workSeason) {
    this.workSeason = workSeason;
  }

/**
	* 連同計件代碼<br>
	* 同案件新貸件(代碼2&amp;B)於6個月內撥款，連同(代碼1&amp;A)計算
	* @return String
	*/
  public String getPieceCodeCombine() {
    return this.pieceCodeCombine == null ? "" : this.pieceCodeCombine;
  }

/**
	* 連同計件代碼<br>
	* 同案件新貸件(代碼2&amp;B)於6個月內撥款，連同(代碼1&amp;A)計算
  *
  * @param pieceCodeCombine 連同計件代碼
	*/
  public void setPieceCodeCombine(String pieceCodeCombine) {
    this.pieceCodeCombine = pieceCodeCombine;
  }

/**
	* 理財型房貸(Y/N)<br>
	* 只寫入首筆撥貸
	* @return String
	*/
  public String getIsProdFinancial() {
    return this.isProdFinancial == null ? "" : this.isProdFinancial;
  }

/**
	* 理財型房貸(Y/N)<br>
	* 只寫入首筆撥貸
  *
  * @param isProdFinancial 理財型房貸(Y/N)
	*/
  public void setIsProdFinancial(String isProdFinancial) {
    this.isProdFinancial = isProdFinancial;
  }

/**
	* 介紹人是否為15日薪(Y/Null)<br>
	* 
	* @return String
	*/
  public String getIsIntroducerDay15() {
    return this.isIntroducerDay15 == null ? "" : this.isIntroducerDay15;
  }

/**
	* 介紹人是否為15日薪(Y/Null)<br>
	* 
  *
  * @param isIntroducerDay15 介紹人是否為15日薪(Y/Null)
	*/
  public void setIsIntroducerDay15(String isIntroducerDay15) {
    this.isIntroducerDay15 = isIntroducerDay15;
  }

/**
	* 協辦人員是否為15日薪(Y/Null)<br>
	* 
	* @return String
	*/
  public String getIsCoorgnizerDay15() {
    return this.isCoorgnizerDay15 == null ? "" : this.isCoorgnizerDay15;
  }

/**
	* 協辦人員是否為15日薪(Y/Null)<br>
	* 
  *
  * @param isCoorgnizerDay15 協辦人員是否為15日薪(Y/Null)
	*/
  public void setIsCoorgnizerDay15(String isCoorgnizerDay15) {
    this.isCoorgnizerDay15 = isCoorgnizerDay15;
  }

/**
	* 計算業績排除商品別(Y/Null)<br>
	* 全部業績
	* @return String
	*/
  public String getIsProdExclude1() {
    return this.isProdExclude1 == null ? "" : this.isProdExclude1;
  }

/**
	* 計算業績排除商品別(Y/Null)<br>
	* 全部業績
  *
  * @param isProdExclude1 計算業績排除商品別(Y/Null)
	*/
  public void setIsProdExclude1(String isProdExclude1) {
    this.isProdExclude1 = isProdExclude1;
  }

/**
	* 計算業績排除商品別(Y/Null)<br>
	* 換算業績、業務報酬
	* @return String
	*/
  public String getIsProdExclude2() {
    return this.isProdExclude2 == null ? "" : this.isProdExclude2;
  }

/**
	* 計算業績排除商品別(Y/Null)<br>
	* 換算業績、業務報酬
  *
  * @param isProdExclude2 計算業績排除商品別(Y/Null)
	*/
  public void setIsProdExclude2(String isProdExclude2) {
    this.isProdExclude2 = isProdExclude2;
  }

/**
	* 計算業績排除商品別(Y/Null)<br>
	* 介紹獎金
	* @return String
	*/
  public String getIsProdExclude3() {
    return this.isProdExclude3 == null ? "" : this.isProdExclude3;
  }

/**
	* 計算業績排除商品別(Y/Null)<br>
	* 介紹獎金
  *
  * @param isProdExclude3 計算業績排除商品別(Y/Null)
	*/
  public void setIsProdExclude3(String isProdExclude3) {
    this.isProdExclude3 = isProdExclude3;
  }

/**
	* 計算業績排除商品別(Y/Nnull)<br>
	* 加碼獎勵津貼
	* @return String
	*/
  public String getIsProdExclude4() {
    return this.isProdExclude4 == null ? "" : this.isProdExclude4;
  }

/**
	* 計算業績排除商品別(Y/Nnull)<br>
	* 加碼獎勵津貼
  *
  * @param isProdExclude4 計算業績排除商品別(Y/Nnull)
	*/
  public void setIsProdExclude4(String isProdExclude4) {
    this.isProdExclude4 = isProdExclude4;
  }

/**
	* 計算業績排除商品別(Y/Null)<br>
	* 協辦獎金
	* @return String
	*/
  public String getIsProdExclude5() {
    return this.isProdExclude5 == null ? "" : this.isProdExclude5;
  }

/**
	* 計算業績排除商品別(Y/Null)<br>
	* 協辦獎金
  *
  * @param isProdExclude5 計算業績排除商品別(Y/Null)
	*/
  public void setIsProdExclude5(String isProdExclude5) {
    this.isProdExclude5 = isProdExclude5;
  }

/**
	* 是否屬排徐部門別(Y/Null)<br>
	* 全部業績
	* @return String
	*/
  public String getIsDeptExclude1() {
    return this.isDeptExclude1 == null ? "" : this.isDeptExclude1;
  }

/**
	* 是否屬排徐部門別(Y/Null)<br>
	* 全部業績
  *
  * @param isDeptExclude1 是否屬排徐部門別(Y/Null)
	*/
  public void setIsDeptExclude1(String isDeptExclude1) {
    this.isDeptExclude1 = isDeptExclude1;
  }

/**
	* 是否屬排徐部門別(Y/Null)<br>
	* 換算業績、業務報酬
	* @return String
	*/
  public String getIsDeptExclude2() {
    return this.isDeptExclude2 == null ? "" : this.isDeptExclude2;
  }

/**
	* 是否屬排徐部門別(Y/Null)<br>
	* 換算業績、業務報酬
  *
  * @param isDeptExclude2 是否屬排徐部門別(Y/Null)
	*/
  public void setIsDeptExclude2(String isDeptExclude2) {
    this.isDeptExclude2 = isDeptExclude2;
  }

/**
	* 是否屬排徐部門別(Y/Null)<br>
	* 介紹獎金
	* @return String
	*/
  public String getIsDeptExclude3() {
    return this.isDeptExclude3 == null ? "" : this.isDeptExclude3;
  }

/**
	* 是否屬排徐部門別(Y/Null)<br>
	* 介紹獎金
  *
  * @param isDeptExclude3 是否屬排徐部門別(Y/Null)
	*/
  public void setIsDeptExclude3(String isDeptExclude3) {
    this.isDeptExclude3 = isDeptExclude3;
  }

/**
	* 是否屬排徐部門別(Y/Null)<br>
	* 加碼獎勵津貼
	* @return String
	*/
  public String getIsDeptExclude4() {
    return this.isDeptExclude4 == null ? "" : this.isDeptExclude4;
  }

/**
	* 是否屬排徐部門別(Y/Null)<br>
	* 加碼獎勵津貼
  *
  * @param isDeptExclude4 是否屬排徐部門別(Y/Null)
	*/
  public void setIsDeptExclude4(String isDeptExclude4) {
    this.isDeptExclude4 = isDeptExclude4;
  }

/**
	* 是否屬排徐部門別(Y/Null)<br>
	* 協辦獎金
	* @return String
	*/
  public String getIsDeptExclude5() {
    return this.isDeptExclude5 == null ? "" : this.isDeptExclude5;
  }

/**
	* 是否屬排徐部門別(Y/Null)<br>
	* 協辦獎金
  *
  * @param isDeptExclude5 是否屬排徐部門別(Y/Null)
	*/
  public void setIsDeptExclude5(String isDeptExclude5) {
    this.isDeptExclude5 = isDeptExclude5;
  }

/**
	* 是否屬15日薪被排除(Y/Null/E-屬15日薪未設排除條件)<br>
	* 全部業績
	* @return String
	*/
  public String getIsDay15Exclude1() {
    return this.isDay15Exclude1 == null ? "" : this.isDay15Exclude1;
  }

/**
	* 是否屬15日薪被排除(Y/Null/E-屬15日薪未設排除條件)<br>
	* 全部業績
  *
  * @param isDay15Exclude1 是否屬15日薪被排除(Y/Null/E-屬15日薪未設排除條件)
	*/
  public void setIsDay15Exclude1(String isDay15Exclude1) {
    this.isDay15Exclude1 = isDay15Exclude1;
  }

/**
	* 是否屬15日薪被排除(Y/Null/E-屬15日薪未設排除條件)<br>
	* 換算業績、業務報酬
	* @return String
	*/
  public String getIsDay15Exclude2() {
    return this.isDay15Exclude2 == null ? "" : this.isDay15Exclude2;
  }

/**
	* 是否屬15日薪被排除(Y/Null/E-屬15日薪未設排除條件)<br>
	* 換算業績、業務報酬
  *
  * @param isDay15Exclude2 是否屬15日薪被排除(Y/Null/E-屬15日薪未設排除條件)
	*/
  public void setIsDay15Exclude2(String isDay15Exclude2) {
    this.isDay15Exclude2 = isDay15Exclude2;
  }

/**
	* 是否屬15日薪被排除(Y/Null/E-屬15日薪未設排除條件)<br>
	* 介紹獎金
	* @return String
	*/
  public String getIsDay15Exclude3() {
    return this.isDay15Exclude3 == null ? "" : this.isDay15Exclude3;
  }

/**
	* 是否屬15日薪被排除(Y/Null/E-屬15日薪未設排除條件)<br>
	* 介紹獎金
  *
  * @param isDay15Exclude3 是否屬15日薪被排除(Y/Null/E-屬15日薪未設排除條件)
	*/
  public void setIsDay15Exclude3(String isDay15Exclude3) {
    this.isDay15Exclude3 = isDay15Exclude3;
  }

/**
	* 是否屬15日薪被排除(Y/Null/E-屬15日薪未設排除條件)<br>
	* 加碼獎勵津貼
	* @return String
	*/
  public String getIsDay15Exclude4() {
    return this.isDay15Exclude4 == null ? "" : this.isDay15Exclude4;
  }

/**
	* 是否屬15日薪被排除(Y/Null/E-屬15日薪未設排除條件)<br>
	* 加碼獎勵津貼
  *
  * @param isDay15Exclude4 是否屬15日薪被排除(Y/Null/E-屬15日薪未設排除條件)
	*/
  public void setIsDay15Exclude4(String isDay15Exclude4) {
    this.isDay15Exclude4 = isDay15Exclude4;
  }

/**
	* 是否屬15日薪被排除(Y/Null/E-屬15日薪未設排除條件)<br>
	* 協辦獎金
	* @return String
	*/
  public String getIsDay15Exclude5() {
    return this.isDay15Exclude5 == null ? "" : this.isDay15Exclude5;
  }

/**
	* 是否屬15日薪被排除(Y/Null/E-屬15日薪未設排除條件)<br>
	* 協辦獎金
  *
  * @param isDay15Exclude5 是否屬15日薪被排除(Y/Null/E-屬15日薪未設排除條件)
	*/
  public void setIsDay15Exclude5(String isDay15Exclude5) {
    this.isDay15Exclude5 = isDay15Exclude5;
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
    return "PfDetail [logNo=" + logNo + ", perfDate=" + perfDate + ", custNo=" + custNo + ", facmNo=" + facmNo + ", bormNo=" + bormNo + ", borxNo=" + borxNo
           + ", repayType=" + repayType + ", drawdownDate=" + drawdownDate + ", drawdownAmt=" + drawdownAmt + ", pieceCode=" + pieceCode + ", repaidPeriod=" + repaidPeriod + ", prodCode=" + prodCode
           + ", creditSysNo=" + creditSysNo + ", introducer=" + introducer + ", coorgnizer=" + coorgnizer + ", interviewerA=" + interviewerA + ", interviewerB=" + interviewerB + ", isReNewEmpUnit=" + isReNewEmpUnit
           + ", unitCode=" + unitCode + ", distCode=" + distCode + ", deptCode=" + deptCode + ", unitManager=" + unitManager + ", distManager=" + distManager + ", deptManager=" + deptManager
           + ", computeItAmtFac=" + computeItAmtFac + ", itPerfCnt=" + itPerfCnt + ", computeItAmt=" + computeItAmt + ", itPerfAmt=" + itPerfAmt + ", itPerfEqAmt=" + itPerfEqAmt + ", itPerfReward=" + itPerfReward
           + ", computeItBonusAmt=" + computeItBonusAmt + ", itBonus=" + itBonus + ", computeAddBonusAmt=" + computeAddBonusAmt + ", itAddBonus=" + itAddBonus + ", computeCoBonusAmt=" + computeCoBonusAmt + ", coorgnizerBonus=" + coorgnizerBonus
           + ", bsOfficer=" + bsOfficer + ", bsDeptCode=" + bsDeptCode + ", computeBsAmtFac=" + computeBsAmtFac + ", bsPerfCnt=" + bsPerfCnt + ", computeBsAmt=" + computeBsAmt + ", bsPerfAmt=" + bsPerfAmt
           + ", workMonth=" + workMonth + ", workSeason=" + workSeason + ", pieceCodeCombine=" + pieceCodeCombine + ", isProdFinancial=" + isProdFinancial + ", isIntroducerDay15=" + isIntroducerDay15 + ", isCoorgnizerDay15=" + isCoorgnizerDay15
           + ", isProdExclude1=" + isProdExclude1 + ", isProdExclude2=" + isProdExclude2 + ", isProdExclude3=" + isProdExclude3 + ", isProdExclude4=" + isProdExclude4 + ", isProdExclude5=" + isProdExclude5 + ", isDeptExclude1=" + isDeptExclude1
           + ", isDeptExclude2=" + isDeptExclude2 + ", isDeptExclude3=" + isDeptExclude3 + ", isDeptExclude4=" + isDeptExclude4 + ", isDeptExclude5=" + isDeptExclude5 + ", isDay15Exclude1=" + isDay15Exclude1 + ", isDay15Exclude2=" + isDay15Exclude2
           + ", isDay15Exclude3=" + isDay15Exclude3 + ", isDay15Exclude4=" + isDay15Exclude4 + ", isDay15Exclude5=" + isDay15Exclude5 + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
           + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
