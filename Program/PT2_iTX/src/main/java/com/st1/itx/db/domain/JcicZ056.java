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
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * JcicZ056 清算案件資料報送<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicZ056`")
public class JcicZ056 implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 7297283819021334275L;

@EmbeddedId
  private JcicZ056Id jcicZ056Id;

  // 交易代碼
  /* A:新增C:異動D:刪除 */
  @Column(name = "`TranKey`", length = 1)
  private String tranKey;

  // 債務人IDN
  @Column(name = "`CustId`", length = 10, insertable = false, updatable = false)
  private String custId;

  // 報送單位代號
  /* 三位文數字 */
  @Column(name = "`SubmitKey`", length = 3, insertable = false, updatable = false)
  private String submitKey;

  // 案件狀態
  /* A:清算程序開始B:清算程序終止(結)C:清算程序開始同時終止D:清算撤消免責確定E:清算調查程序G:清算撤回H:清算復權 */
  @Column(name = "`CaseStatus`", length = 1, insertable = false, updatable = false)
  private String caseStatus;

  // 裁定日期或發文日期
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
  /* 可輸入中文 */
  @Column(name = "`CourtDiv`", length = 4)
  private String courtDiv;

  // 法院案號
  /* 可輸入中文 */
  @Column(name = "`CourtCaseNo`", length = 40)
  private String courtCaseNo;

  // 法院裁定免責確定
  /* Y;N指該次法院是否裁定債務人免責報送時間點為案件狀態B、C、D時 */
  @Column(name = "`Approve`", length = 1)
  private String approve;

  // 原始債權金額
  /* 指報送機構之原始對外債全總金額報送時間點為案件狀態A、C */
  @Column(name = "`OutstandAmt`")
  private int outstandAmt = 0;

  // 清算損失金額
  /* 清算案件於清算分配後損失之債權金額報送時間點為案件狀態B、C */
  @Column(name = "`SubAmt`")
  private int subAmt = 0;

  // 法院裁定保全處分
  /* Y;N指法院裁定債務人財產保全處分與否 */
  @Column(name = "`ClaimStatus1`", length = 1)
  private String claimStatus1;

  // 保全處分起始日
  @Column(name = "`SaveDate`")
  private int saveDate = 0;

  // 法院裁定撤銷保全處分
  /* Y;N指法院裁定撤銷債務人財產保全處分 */
  @Column(name = "`ClaimStatus2`", length = 1)
  private String claimStatus2;

  // 保全處分撤銷日
  @Column(name = "`SaveEndDate`")
  private int saveEndDate = 0;

  // 管理人姓名
  /* 可輸入中文 */
  @Column(name = "`AdminName`", length = 10)
  private String adminName;

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


  public JcicZ056Id getJcicZ056Id() {
    return this.jcicZ056Id;
  }

  public void setJcicZ056Id(JcicZ056Id jcicZ056Id) {
    this.jcicZ056Id = jcicZ056Id;
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
	* 三位文數字
	* @return String
	*/
  public String getSubmitKey() {
    return this.submitKey == null ? "" : this.submitKey;
  }

/**
	* 報送單位代號<br>
	* 三位文數字
  *
  * @param submitKey 報送單位代號
	*/
  public void setSubmitKey(String submitKey) {
    this.submitKey = submitKey;
  }

/**
	* 案件狀態<br>
	* A:清算程序開始
B:清算程序終止(結)
C:清算程序開始同時終止
D:清算撤消免責確定
E:清算調查程序
G:清算撤回
H:清算復權
	* @return String
	*/
  public String getCaseStatus() {
    return this.caseStatus == null ? "" : this.caseStatus;
  }

/**
	* 案件狀態<br>
	* A:清算程序開始
B:清算程序終止(結)
C:清算程序開始同時終止
D:清算撤消免責確定
E:清算調查程序
G:清算撤回
H:清算復權
  *
  * @param caseStatus 案件狀態
	*/
  public void setCaseStatus(String caseStatus) {
    this.caseStatus = caseStatus;
  }

/**
	* 裁定日期或發文日期<br>
	* 
	* @return Integer
	*/
  public int getClaimDate() {
    return StaticTool.bcToRoc(this.claimDate);
  }

/**
	* 裁定日期或發文日期<br>
	* 
  *
  * @param claimDate 裁定日期或發文日期
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
	* 可輸入中文
	* @return String
	*/
  public String getCourtDiv() {
    return this.courtDiv == null ? "" : this.courtDiv;
  }

/**
	* 法院承審股別<br>
	* 可輸入中文
  *
  * @param courtDiv 法院承審股別
	*/
  public void setCourtDiv(String courtDiv) {
    this.courtDiv = courtDiv;
  }

/**
	* 法院案號<br>
	* 可輸入中文
	* @return String
	*/
  public String getCourtCaseNo() {
    return this.courtCaseNo == null ? "" : this.courtCaseNo;
  }

/**
	* 法院案號<br>
	* 可輸入中文
  *
  * @param courtCaseNo 法院案號
	*/
  public void setCourtCaseNo(String courtCaseNo) {
    this.courtCaseNo = courtCaseNo;
  }

/**
	* 法院裁定免責確定<br>
	* Y;N
指該次法院是否裁定債務人免責
報送時間點為案件狀態B、C、D時
	* @return String
	*/
  public String getApprove() {
    return this.approve == null ? "" : this.approve;
  }

/**
	* 法院裁定免責確定<br>
	* Y;N
指該次法院是否裁定債務人免責
報送時間點為案件狀態B、C、D時
  *
  * @param approve 法院裁定免責確定
	*/
  public void setApprove(String approve) {
    this.approve = approve;
  }

/**
	* 原始債權金額<br>
	* 指報送機構之原始對外債全總金額
報送時間點為案件狀態A、C
	* @return Integer
	*/
  public int getOutstandAmt() {
    return this.outstandAmt;
  }

/**
	* 原始債權金額<br>
	* 指報送機構之原始對外債全總金額
報送時間點為案件狀態A、C
  *
  * @param outstandAmt 原始債權金額
	*/
  public void setOutstandAmt(int outstandAmt) {
    this.outstandAmt = outstandAmt;
  }

/**
	* 清算損失金額<br>
	* 清算案件於清算分配後損失之債權金額
報送時間點為案件狀態B、C
	* @return Integer
	*/
  public int getSubAmt() {
    return this.subAmt;
  }

/**
	* 清算損失金額<br>
	* 清算案件於清算分配後損失之債權金額
報送時間點為案件狀態B、C
  *
  * @param subAmt 清算損失金額
	*/
  public void setSubAmt(int subAmt) {
    this.subAmt = subAmt;
  }

/**
	* 法院裁定保全處分<br>
	* Y;N
指法院裁定債務人財產保全處分與否
	* @return String
	*/
  public String getClaimStatus1() {
    return this.claimStatus1 == null ? "" : this.claimStatus1;
  }

/**
	* 法院裁定保全處分<br>
	* Y;N
指法院裁定債務人財產保全處分與否
  *
  * @param claimStatus1 法院裁定保全處分
	*/
  public void setClaimStatus1(String claimStatus1) {
    this.claimStatus1 = claimStatus1;
  }

/**
	* 保全處分起始日<br>
	* 
	* @return Integer
	*/
  public int getSaveDate() {
    return StaticTool.bcToRoc(this.saveDate);
  }

/**
	* 保全處分起始日<br>
	* 
  *
  * @param saveDate 保全處分起始日
  * @throws LogicException when Date Is Warn	*/
  public void setSaveDate(int saveDate) throws LogicException {
    this.saveDate = StaticTool.rocToBc(saveDate);
  }

/**
	* 法院裁定撤銷保全處分<br>
	* Y;N
指法院裁定撤銷債務人財產保全處分
	* @return String
	*/
  public String getClaimStatus2() {
    return this.claimStatus2 == null ? "" : this.claimStatus2;
  }

/**
	* 法院裁定撤銷保全處分<br>
	* Y;N
指法院裁定撤銷債務人財產保全處分
  *
  * @param claimStatus2 法院裁定撤銷保全處分
	*/
  public void setClaimStatus2(String claimStatus2) {
    this.claimStatus2 = claimStatus2;
  }

/**
	* 保全處分撤銷日<br>
	* 
	* @return Integer
	*/
  public int getSaveEndDate() {
    return StaticTool.bcToRoc(this.saveEndDate);
  }

/**
	* 保全處分撤銷日<br>
	* 
  *
  * @param saveEndDate 保全處分撤銷日
  * @throws LogicException when Date Is Warn	*/
  public void setSaveEndDate(int saveEndDate) throws LogicException {
    this.saveEndDate = StaticTool.rocToBc(saveEndDate);
  }

/**
	* 管理人姓名<br>
	* 可輸入中文
	* @return String
	*/
  public String getAdminName() {
    return this.adminName == null ? "" : this.adminName;
  }

/**
	* 管理人姓名<br>
	* 可輸入中文
  *
  * @param adminName 管理人姓名
	*/
  public void setAdminName(String adminName) {
    this.adminName = adminName;
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
    return "JcicZ056 [jcicZ056Id=" + jcicZ056Id + ", tranKey=" + tranKey
           + ", year=" + year + ", courtDiv=" + courtDiv + ", courtCaseNo=" + courtCaseNo + ", approve=" + approve + ", outstandAmt=" + outstandAmt + ", subAmt=" + subAmt
           + ", claimStatus1=" + claimStatus1 + ", saveDate=" + saveDate + ", claimStatus2=" + claimStatus2 + ", saveEndDate=" + saveEndDate + ", adminName=" + adminName + ", outJcicTxtDate=" + outJcicTxtDate
           + ", ukey=" + ukey + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + ", actualFilingDate=" + actualFilingDate
           + ", actualFilingMark=" + actualFilingMark + "]";
  }
}
