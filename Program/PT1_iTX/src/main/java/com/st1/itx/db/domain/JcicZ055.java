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
 * JcicZ055 消債條例更生案件資料報送<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicZ055`")
public class JcicZ055 implements Serializable {


  @EmbeddedId
  private JcicZ055Id jcicZ055Id;

  // 交易代碼
  /* A:新增C:異動D:刪除 */
  @Column(name = "`TranKey`", length = 1)
  private String tranKey;

  // 債務人IDN
  @Column(name = "`CustId`", length = 10, insertable = false, updatable = false)
  private String custId;

  // 報送單位代號
  /* 3位文數字 */
  @Column(name = "`SubmitKey`", length = 3, insertable = false, updatable = false)
  private String submitKey;

  // 案件狀態
  /* 1:更生程序開始2:更生撤回3:更生方案認可確定4:更生方案履行完畢5:更生裁定免責確定6:更生調查程序 */
  @Column(name = "`CaseStatus`", length = 1, insertable = false, updatable = false)
  private String caseStatus;

  // 裁定日或履行完畢日或發文日
  /* 指法院裁定日期(案件狀態為1、3、5)，或債權金融機構認定債務人已依更生方案履行完畢之日期(案件狀態為4)，或發文日期(案件狀態為2、6)已YYYMMDD表示，本欄位值應小於或等於資料報送日期 */
  @Column(name = "`ClaimDate`", insertable = false, updatable = false)
  private int claimDate = 0;

  // 承審法院代碼
  @Column(name = "`CourtCode`", length = 3, insertable = false, updatable = false)
  private String courtCode;

  // 年度別
  /* 西元年,畫面是民國年 */
  @Column(name = "`Year`")
  private int year = 0;

  // 法院承審股別
  /* 指該案承審股別，中文全形字可輸入中文("股"字不用填寫)4個中文字 */
  @Column(name = "`CourtDiv`", length = 4)
  private String courtDiv;

  // 法院案號
  /* 可輸入中文40個中文字審消債更第2號消債更第4號執消債更第8號數字已全型填寫 */
  @Column(name = "`CourtCaseNo`", length = 40)
  private String courtCaseNo;

  // 更生方案首期應繳款日
  /* 指更生方案約定之首期應還款日期報送時點為案件狀態3時 */
  @Column(name = "`PayDate`")
  private int payDate = 0;

  // 更生方案末期應繳款日
  /* 指更生方案約定之末期應還款日期報送時點為案件狀態3時 */
  @Column(name = "`PayEndDate`")
  private int payEndDate = 0;

  // 更生條件(期數)
  /* 更生方案之期數報送時點為案件狀態3時 */
  @Column(name = "`Period`")
  private int period = 0;

  // 更生條件(利率)
  /* 更生方案之利率，以"XX.XX"表示報送時點為案件狀態3時 */
  @Column(name = "`Rate`")
  private BigDecimal rate = new BigDecimal("0");

  // 原始債權金額
  /* 指報送機構之原始對外債權總金額報送時點為案件狀態1時 */
  @Column(name = "`OutstandAmt`")
  private int outstandAmt = 0;

  // 更生損失金額
  /* 指更生案件債務人一更生方案履行完畢或裁定免責後，債務人免責金額報送時點為案件狀態4、5時 */
  @Column(name = "`SubAmt`")
  private int subAmt = 0;

  // 法院裁定保全處分
  /* Y;N指法院裁定在務人財產保全與否 */
  @Column(name = "`ClaimStatus1`", length = 1)
  private String claimStatus1;

  // 保全處分起始日
  /* 指法院處分起始日期 */
  @Column(name = "`SaveDate`")
  private int saveDate = 0;

  // 法院裁定撤銷保全處分
  /* Y;N */
  @Column(name = "`ClaimStatus2`", length = 1)
  private String claimStatus2;

  // 保全處分撤銷日
  /* 指法院裁定撤銷保全處分日 */
  @Column(name = "`SaveEndDate`")
  private int saveEndDate = 0;

  // 是否依更生條件履行
  /* Y;N指該債務人是否依更生條件履行還款約定，案件狀態為3時本欄位為必填報欄位，首次報送案件狀態3時，本欄位一律填報Y */
  @Column(name = "`IsImplement`", length = 1)
  private String isImplement;

  // 監督人姓名
  /* 各債權金融機構得知監督人姓名時填報 */
  @Column(name = "`InspectName`", length = 10)
  private String inspectName;

  // 轉出JCIC文字檔日期
  @Column(name = "`OutJcicTxtDate`")
  private int outJcicTxtDate = 0;

  // 流水號
  @Column(name = "`Ukey`", length = 32)
  private String ukey;

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

  // 實際報送日期
  @Column(name = "`ActualFilingDate`")
  private int actualFilingDate = 0;

  // 實際報送記號
  @Column(name = "`ActualFilingMark`", length = 3)
  private String actualFilingMark;


  public JcicZ055Id getJcicZ055Id() {
    return this.jcicZ055Id;
  }

  public void setJcicZ055Id(JcicZ055Id jcicZ055Id) {
    this.jcicZ055Id = jcicZ055Id;
  }

/**
	* 交易代碼<br>
	* A:新增
C:異動
D:刪除
	* @return String
	*/
  public String getTranKey() {
    return this.tranKey == null ? "" : this.tranKey;
  }

/**
	* 交易代碼<br>
	* A:新增
C:異動
D:刪除
  *
  * @param tranKey 交易代碼
	*/
  public void setTranKey(String tranKey) {
    this.tranKey = tranKey;
  }

/**
	* 債務人IDN<br>
	* 
	* @return String
	*/
  public String getCustId() {
    return this.custId == null ? "" : this.custId;
  }

/**
	* 債務人IDN<br>
	* 
  *
  * @param custId 債務人IDN
	*/
  public void setCustId(String custId) {
    this.custId = custId;
  }

/**
	* 報送單位代號<br>
	* 3位文數字
	* @return String
	*/
  public String getSubmitKey() {
    return this.submitKey == null ? "" : this.submitKey;
  }

/**
	* 報送單位代號<br>
	* 3位文數字
  *
  * @param submitKey 報送單位代號
	*/
  public void setSubmitKey(String submitKey) {
    this.submitKey = submitKey;
  }

/**
	* 案件狀態<br>
	* 1:更生程序開始
2:更生撤回
3:更生方案認可確定
4:更生方案履行完畢
5:更生裁定免責確定
6:更生調查程序
	* @return String
	*/
  public String getCaseStatus() {
    return this.caseStatus == null ? "" : this.caseStatus;
  }

/**
	* 案件狀態<br>
	* 1:更生程序開始
2:更生撤回
3:更生方案認可確定
4:更生方案履行完畢
5:更生裁定免責確定
6:更生調查程序
  *
  * @param caseStatus 案件狀態
	*/
  public void setCaseStatus(String caseStatus) {
    this.caseStatus = caseStatus;
  }

/**
	* 裁定日或履行完畢日或發文日<br>
	* 指法院裁定日期(案件狀態為1、3、5)，或債權金融機構認定債務人已依更生方案履行完畢之日期(案件狀態為4)，或發文日期(案件狀態為2、6)已YYYMMDD表示，本欄位值應小於或等於資料報送日期
	* @return Integer
	*/
  public int getClaimDate() {
    return StaticTool.bcToRoc(this.claimDate);
  }

/**
	* 裁定日或履行完畢日或發文日<br>
	* 指法院裁定日期(案件狀態為1、3、5)，或債權金融機構認定債務人已依更生方案履行完畢之日期(案件狀態為4)，或發文日期(案件狀態為2、6)已YYYMMDD表示，本欄位值應小於或等於資料報送日期
  *
  * @param claimDate 裁定日或履行完畢日或發文日
  * @throws LogicException when Date Is Warn	*/
  public void setClaimDate(int claimDate) throws LogicException {
    this.claimDate = StaticTool.rocToBc(claimDate);
  }

/**
	* 承審法院代碼<br>
	* 
	* @return String
	*/
  public String getCourtCode() {
    return this.courtCode == null ? "" : this.courtCode;
  }

/**
	* 承審法院代碼<br>
	* 
  *
  * @param courtCode 承審法院代碼
	*/
  public void setCourtCode(String courtCode) {
    this.courtCode = courtCode;
  }

/**
	* 年度別<br>
	* 西元年,畫面是民國年
	* @return Integer
	*/
  public int getYear() {
    return this.year;
  }

/**
	* 年度別<br>
	* 西元年,畫面是民國年
  *
  * @param year 年度別
	*/
  public void setYear(int year) {
    this.year = year;
  }

/**
	* 法院承審股別<br>
	* 指該案承審股別，中文全形字
可輸入中文
("股"字不用填寫)
4個中文字
	* @return String
	*/
  public String getCourtDiv() {
    return this.courtDiv == null ? "" : this.courtDiv;
  }

/**
	* 法院承審股別<br>
	* 指該案承審股別，中文全形字
可輸入中文
("股"字不用填寫)
4個中文字
  *
  * @param courtDiv 法院承審股別
	*/
  public void setCourtDiv(String courtDiv) {
    this.courtDiv = courtDiv;
  }

/**
	* 法院案號<br>
	* 可輸入中文
40個中文字
審消債更第2號
消債更第4號
執消債更第8號
數字已全型填寫
	* @return String
	*/
  public String getCourtCaseNo() {
    return this.courtCaseNo == null ? "" : this.courtCaseNo;
  }

/**
	* 法院案號<br>
	* 可輸入中文
40個中文字
審消債更第2號
消債更第4號
執消債更第8號
數字已全型填寫
  *
  * @param courtCaseNo 法院案號
	*/
  public void setCourtCaseNo(String courtCaseNo) {
    this.courtCaseNo = courtCaseNo;
  }

/**
	* 更生方案首期應繳款日<br>
	* 指更生方案約定之首期應還款日期
報送時點為案件狀態3時
	* @return Integer
	*/
  public int getPayDate() {
    return StaticTool.bcToRoc(this.payDate);
  }

/**
	* 更生方案首期應繳款日<br>
	* 指更生方案約定之首期應還款日期
報送時點為案件狀態3時
  *
  * @param payDate 更生方案首期應繳款日
  * @throws LogicException when Date Is Warn	*/
  public void setPayDate(int payDate) throws LogicException {
    this.payDate = StaticTool.rocToBc(payDate);
  }

/**
	* 更生方案末期應繳款日<br>
	* 指更生方案約定之末期應還款日期
報送時點為案件狀態3時
	* @return Integer
	*/
  public int getPayEndDate() {
    return StaticTool.bcToRoc(this.payEndDate);
  }

/**
	* 更生方案末期應繳款日<br>
	* 指更生方案約定之末期應還款日期
報送時點為案件狀態3時
  *
  * @param payEndDate 更生方案末期應繳款日
  * @throws LogicException when Date Is Warn	*/
  public void setPayEndDate(int payEndDate) throws LogicException {
    this.payEndDate = StaticTool.rocToBc(payEndDate);
  }

/**
	* 更生條件(期數)<br>
	* 更生方案之期數
報送時點為案件狀態3時
	* @return Integer
	*/
  public int getPeriod() {
    return this.period;
  }

/**
	* 更生條件(期數)<br>
	* 更生方案之期數
報送時點為案件狀態3時
  *
  * @param period 更生條件(期數)
	*/
  public void setPeriod(int period) {
    this.period = period;
  }

/**
	* 更生條件(利率)<br>
	* 更生方案之利率，以"XX.XX"表示
報送時點為案件狀態3時
	* @return BigDecimal
	*/
  public BigDecimal getRate() {
    return this.rate;
  }

/**
	* 更生條件(利率)<br>
	* 更生方案之利率，以"XX.XX"表示
報送時點為案件狀態3時
  *
  * @param rate 更生條件(利率)
	*/
  public void setRate(BigDecimal rate) {
    this.rate = rate;
  }

/**
	* 原始債權金額<br>
	* 指報送機構之原始對外債權總金額
報送時點為案件狀態1時
	* @return Integer
	*/
  public int getOutstandAmt() {
    return this.outstandAmt;
  }

/**
	* 原始債權金額<br>
	* 指報送機構之原始對外債權總金額
報送時點為案件狀態1時
  *
  * @param outstandAmt 原始債權金額
	*/
  public void setOutstandAmt(int outstandAmt) {
    this.outstandAmt = outstandAmt;
  }

/**
	* 更生損失金額<br>
	* 指更生案件債務人一更生方案履行完畢或裁定免責後，債務人免責金額
報送時點為案件狀態4、5時
	* @return Integer
	*/
  public int getSubAmt() {
    return this.subAmt;
  }

/**
	* 更生損失金額<br>
	* 指更生案件債務人一更生方案履行完畢或裁定免責後，債務人免責金額
報送時點為案件狀態4、5時
  *
  * @param subAmt 更生損失金額
	*/
  public void setSubAmt(int subAmt) {
    this.subAmt = subAmt;
  }

/**
	* 法院裁定保全處分<br>
	* Y;N
指法院裁定在務人財產保全與否
	* @return String
	*/
  public String getClaimStatus1() {
    return this.claimStatus1 == null ? "" : this.claimStatus1;
  }

/**
	* 法院裁定保全處分<br>
	* Y;N
指法院裁定在務人財產保全與否
  *
  * @param claimStatus1 法院裁定保全處分
	*/
  public void setClaimStatus1(String claimStatus1) {
    this.claimStatus1 = claimStatus1;
  }

/**
	* 保全處分起始日<br>
	* 指法院處分起始日期
	* @return Integer
	*/
  public int getSaveDate() {
    return StaticTool.bcToRoc(this.saveDate);
  }

/**
	* 保全處分起始日<br>
	* 指法院處分起始日期
  *
  * @param saveDate 保全處分起始日
  * @throws LogicException when Date Is Warn	*/
  public void setSaveDate(int saveDate) throws LogicException {
    this.saveDate = StaticTool.rocToBc(saveDate);
  }

/**
	* 法院裁定撤銷保全處分<br>
	* Y;N
	* @return String
	*/
  public String getClaimStatus2() {
    return this.claimStatus2 == null ? "" : this.claimStatus2;
  }

/**
	* 法院裁定撤銷保全處分<br>
	* Y;N
  *
  * @param claimStatus2 法院裁定撤銷保全處分
	*/
  public void setClaimStatus2(String claimStatus2) {
    this.claimStatus2 = claimStatus2;
  }

/**
	* 保全處分撤銷日<br>
	* 指法院裁定撤銷保全處分日
	* @return Integer
	*/
  public int getSaveEndDate() {
    return StaticTool.bcToRoc(this.saveEndDate);
  }

/**
	* 保全處分撤銷日<br>
	* 指法院裁定撤銷保全處分日
  *
  * @param saveEndDate 保全處分撤銷日
  * @throws LogicException when Date Is Warn	*/
  public void setSaveEndDate(int saveEndDate) throws LogicException {
    this.saveEndDate = StaticTool.rocToBc(saveEndDate);
  }

/**
	* 是否依更生條件履行<br>
	* Y;N
指該債務人是否依更生條件履行還款約定，案件狀態為3時本欄位為必填報欄位，首次報送案件狀態3時，本欄位一律填報Y
	* @return String
	*/
  public String getIsImplement() {
    return this.isImplement == null ? "" : this.isImplement;
  }

/**
	* 是否依更生條件履行<br>
	* Y;N
指該債務人是否依更生條件履行還款約定，案件狀態為3時本欄位為必填報欄位，首次報送案件狀態3時，本欄位一律填報Y
  *
  * @param isImplement 是否依更生條件履行
	*/
  public void setIsImplement(String isImplement) {
    this.isImplement = isImplement;
  }

/**
	* 監督人姓名<br>
	* 各債權金融機構得知監督人姓名時填報
	* @return String
	*/
  public String getInspectName() {
    return this.inspectName == null ? "" : this.inspectName;
  }

/**
	* 監督人姓名<br>
	* 各債權金融機構得知監督人姓名時填報
  *
  * @param inspectName 監督人姓名
	*/
  public void setInspectName(String inspectName) {
    this.inspectName = inspectName;
  }

/**
	* 轉出JCIC文字檔日期<br>
	* 
	* @return Integer
	*/
  public int getOutJcicTxtDate() {
    return StaticTool.bcToRoc(this.outJcicTxtDate);
  }

/**
	* 轉出JCIC文字檔日期<br>
	* 
  *
  * @param outJcicTxtDate 轉出JCIC文字檔日期
  * @throws LogicException when Date Is Warn	*/
  public void setOutJcicTxtDate(int outJcicTxtDate) throws LogicException {
    this.outJcicTxtDate = StaticTool.rocToBc(outJcicTxtDate);
  }

/**
	* 流水號<br>
	* 
	* @return String
	*/
  public String getUkey() {
    return this.ukey == null ? "" : this.ukey;
  }

/**
	* 流水號<br>
	* 
  *
  * @param ukey 流水號
	*/
  public void setUkey(String ukey) {
    this.ukey = ukey;
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
	* 實際報送日期<br>
	* 
	* @return Integer
	*/
  public int getActualFilingDate() {
    return StaticTool.bcToRoc(this.actualFilingDate);
  }

/**
	* 實際報送日期<br>
	* 
  *
  * @param actualFilingDate 實際報送日期
  * @throws LogicException when Date Is Warn	*/
  public void setActualFilingDate(int actualFilingDate) throws LogicException {
    this.actualFilingDate = StaticTool.rocToBc(actualFilingDate);
  }

/**
	* 實際報送記號<br>
	* 
	* @return String
	*/
  public String getActualFilingMark() {
    return this.actualFilingMark == null ? "" : this.actualFilingMark;
  }

/**
	* 實際報送記號<br>
	* 
  *
  * @param actualFilingMark 實際報送記號
	*/
  public void setActualFilingMark(String actualFilingMark) {
    this.actualFilingMark = actualFilingMark;
  }


  @Override
  public String toString() {
    return "JcicZ055 [jcicZ055Id=" + jcicZ055Id + ", tranKey=" + tranKey
           + ", year=" + year + ", courtDiv=" + courtDiv + ", courtCaseNo=" + courtCaseNo + ", payDate=" + payDate + ", payEndDate=" + payEndDate + ", period=" + period
           + ", rate=" + rate + ", outstandAmt=" + outstandAmt + ", subAmt=" + subAmt + ", claimStatus1=" + claimStatus1 + ", saveDate=" + saveDate + ", claimStatus2=" + claimStatus2
           + ", saveEndDate=" + saveEndDate + ", isImplement=" + isImplement + ", inspectName=" + inspectName + ", outJcicTxtDate=" + outJcicTxtDate + ", ukey=" + ukey + ", createDate=" + createDate
           + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + ", actualFilingDate=" + actualFilingDate + ", actualFilingMark=" + actualFilingMark + "]";
  }
}
