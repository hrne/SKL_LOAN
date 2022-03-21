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
 * FacClose 清償作業檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`FacClose`")
public class FacClose implements Serializable {


  @EmbeddedId
  private FacCloseId facCloseId;

  // 戶號
  @Column(name = "`CustNo`", insertable = false, updatable = false)
  private int custNo = 0;

  // 清償序號
  @Column(name = "`CloseNo`", insertable = false, updatable = false)
  private int closeNo = 0;

  // 額度編號
  @Column(name = "`FacmNo`")
  private int facmNo = 0;

  // 登放記號
  /* 1:登錄2:放行 */
  @Column(name = "`ActFlag`")
  private int actFlag = 0;

  // 作業功能
  /* 0:清償(必須為尚未結案)1:請領(已申請者為請領)2:補領(已結案後來申請者)3:補發(已領過者為補發) */
  @Column(name = "`FunCode`", length = 1)
  private String funCode;

  // 車貸
  /* 0:非車貸1:車貸 */
  @Column(name = "`CarLoan`")
  private int carLoan = 0;

  // 申請日期
  /* 自動寫入時為0 */
  @Column(name = "`ApplDate`")
  private int applDate = 0;

  // 結案日期(入帳日期)
  /* 結案登錄更新 */
  @Column(name = "`CloseDate`")
  private int closeDate = 0;

  // 結案區分
  /* 共用代碼檔 CaseCloseCode0:正常1:展期2:借新還舊3:轉催收4:催收戶本人清償5:催收戶保證人代償6:催收戶強制執行7:轉列呆帳8:催收部分轉呆 */
  @Column(name = "`CloseInd`", length = 1)
  private String closeInd;

  // 清償原因
  /* 00:無01:買賣02:自行還清03:軍功教勞工貸款轉貸04:利率過高轉貸05:增貸不准轉貸06:額度內動支不准轉貸07:內部代償08:借新還舊09:其他10:買回11:綁約期還款 */
  @Column(name = "`CloseReasonCode`", length = 2)
  private String closeReasonCode;

  // 還清金額
  @Column(name = "`CloseAmt`")
  private BigDecimal closeAmt = new BigDecimal("0");

  // 是否領取清償證明(Y/N/'')
  @Column(name = "`CollectFlag`", length = 1)
  private String collectFlag;

  // 領取方式
  @Column(name = "`CollectWayCode`", length = 2)
  private String collectWayCode;

  // 領取日期
  @Column(name = "`ReceiveDate`")
  private int receiveDate = 0;

  // 連絡電話1
  @Column(name = "`TelNo1`", length = 15)
  private String telNo1;

  // 連絡電話2
  @Column(name = "`TelNo2`", length = 15)
  private String telNo2;

  // 連絡電話3
  @Column(name = "`TelNo3`", length = 15)
  private String telNo3;

  // 入帳日期
  @Column(name = "`EntryDate`")
  private int entryDate = 0;

  // 塗銷同意書編號
  @Column(name = "`AgreeNo`", length = 10)
  private String agreeNo;

  // 公文編號
  @Column(name = "`DocNo`")
  private int docNo = 0;

  // 銷號欄
  @Column(name = "`ClsNo`", length = 18)
  private String clsNo;

  // 備註
  @Column(name = "`Rmk`", length = 100)
  private String rmk;

  // 擔保品代號1
  /* 擔保品代號檔CdCl */
  @Column(name = "`ClCode1`")
  private int clCode1 = 0;

  // 擔保品代號2
  /* 擔保品代號檔CdCl */
  @Column(name = "`ClCode2`")
  private int clCode2 = 0;

  // 擔保品編號
  @Column(name = "`ClNo`")
  private int clNo = 0;

  // 領取記號
  /* 0:未領取1:已領取 */
  @Column(name = "`ReceiveFg`")
  private int receiveFg = 0;

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


  public FacCloseId getFacCloseId() {
    return this.facCloseId;
  }

  public void setFacCloseId(FacCloseId facCloseId) {
    this.facCloseId = facCloseId;
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
	* 清償序號<br>
	* 
	* @return Integer
	*/
  public int getCloseNo() {
    return this.closeNo;
  }

/**
	* 清償序號<br>
	* 
  *
  * @param closeNo 清償序號
	*/
  public void setCloseNo(int closeNo) {
    this.closeNo = closeNo;
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
	* 登放記號<br>
	* 1:登錄
2:放行
	* @return Integer
	*/
  public int getActFlag() {
    return this.actFlag;
  }

/**
	* 登放記號<br>
	* 1:登錄
2:放行
  *
  * @param actFlag 登放記號
	*/
  public void setActFlag(int actFlag) {
    this.actFlag = actFlag;
  }

/**
	* 作業功能<br>
	* 0:清償(必須為尚未結案)
1:請領(已申請者為請領)
2:補領(已結案後來申請者)
3:補發(已領過者為補發)
	* @return String
	*/
  public String getFunCode() {
    return this.funCode == null ? "" : this.funCode;
  }

/**
	* 作業功能<br>
	* 0:清償(必須為尚未結案)
1:請領(已申請者為請領)
2:補領(已結案後來申請者)
3:補發(已領過者為補發)
  *
  * @param funCode 作業功能
	*/
  public void setFunCode(String funCode) {
    this.funCode = funCode;
  }

/**
	* 車貸<br>
	* 0:非車貸
1:車貸
	* @return Integer
	*/
  public int getCarLoan() {
    return this.carLoan;
  }

/**
	* 車貸<br>
	* 0:非車貸
1:車貸
  *
  * @param carLoan 車貸
	*/
  public void setCarLoan(int carLoan) {
    this.carLoan = carLoan;
  }

/**
	* 申請日期<br>
	* 自動寫入時為0
	* @return Integer
	*/
  public int getApplDate() {
    return StaticTool.bcToRoc(this.applDate);
  }

/**
	* 申請日期<br>
	* 自動寫入時為0
  *
  * @param applDate 申請日期
  * @throws LogicException when Date Is Warn	*/
  public void setApplDate(int applDate) throws LogicException {
    this.applDate = StaticTool.rocToBc(applDate);
  }

/**
	* 結案日期(入帳日期)<br>
	* 結案登錄更新
	* @return Integer
	*/
  public int getCloseDate() {
    return StaticTool.bcToRoc(this.closeDate);
  }

/**
	* 結案日期(入帳日期)<br>
	* 結案登錄更新
  *
  * @param closeDate 結案日期(入帳日期)
  * @throws LogicException when Date Is Warn	*/
  public void setCloseDate(int closeDate) throws LogicException {
    this.closeDate = StaticTool.rocToBc(closeDate);
  }

/**
	* 結案區分<br>
	* 共用代碼檔 CaseCloseCode
0:正常
1:展期
2:借新還舊
3:轉催收
4:催收戶本人清償
5:催收戶保證人代償
6:催收戶強制執行
7:轉列呆帳
8:催收部分轉呆
	* @return String
	*/
  public String getCloseInd() {
    return this.closeInd == null ? "" : this.closeInd;
  }

/**
	* 結案區分<br>
	* 共用代碼檔 CaseCloseCode
0:正常
1:展期
2:借新還舊
3:轉催收
4:催收戶本人清償
5:催收戶保證人代償
6:催收戶強制執行
7:轉列呆帳
8:催收部分轉呆
  *
  * @param closeInd 結案區分
	*/
  public void setCloseInd(String closeInd) {
    this.closeInd = closeInd;
  }

/**
	* 清償原因<br>
	* 00:無
01:買賣
02:自行還清
03:軍功教勞工貸款轉貸
04:利率過高轉貸
05:增貸不准轉貸
06:額度內動支不准轉貸
07:內部代償
08:借新還舊
09:其他
10:買回
11:綁約期還款
	* @return String
	*/
  public String getCloseReasonCode() {
    return this.closeReasonCode == null ? "" : this.closeReasonCode;
  }

/**
	* 清償原因<br>
	* 00:無
01:買賣
02:自行還清
03:軍功教勞工貸款轉貸
04:利率過高轉貸
05:增貸不准轉貸
06:額度內動支不准轉貸
07:內部代償
08:借新還舊
09:其他
10:買回
11:綁約期還款
  *
  * @param closeReasonCode 清償原因
	*/
  public void setCloseReasonCode(String closeReasonCode) {
    this.closeReasonCode = closeReasonCode;
  }

/**
	* 還清金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getCloseAmt() {
    return this.closeAmt;
  }

/**
	* 還清金額<br>
	* 
  *
  * @param closeAmt 還清金額
	*/
  public void setCloseAmt(BigDecimal closeAmt) {
    this.closeAmt = closeAmt;
  }

/**
	* 是否領取清償證明(Y/N/'')<br>
	* 
	* @return String
	*/
  public String getCollectFlag() {
    return this.collectFlag == null ? "" : this.collectFlag;
  }

/**
	* 是否領取清償證明(Y/N/'')<br>
	* 
  *
  * @param collectFlag 是否領取清償證明(Y/N/'')
	*/
  public void setCollectFlag(String collectFlag) {
    this.collectFlag = collectFlag;
  }

/**
	* 領取方式<br>
	* 
	* @return String
	*/
  public String getCollectWayCode() {
    return this.collectWayCode == null ? "" : this.collectWayCode;
  }

/**
	* 領取方式<br>
	* 
  *
  * @param collectWayCode 領取方式
	*/
  public void setCollectWayCode(String collectWayCode) {
    this.collectWayCode = collectWayCode;
  }

/**
	* 領取日期<br>
	* 
	* @return Integer
	*/
  public int getReceiveDate() {
    return StaticTool.bcToRoc(this.receiveDate);
  }

/**
	* 領取日期<br>
	* 
  *
  * @param receiveDate 領取日期
  * @throws LogicException when Date Is Warn	*/
  public void setReceiveDate(int receiveDate) throws LogicException {
    this.receiveDate = StaticTool.rocToBc(receiveDate);
  }

/**
	* 連絡電話1<br>
	* 
	* @return String
	*/
  public String getTelNo1() {
    return this.telNo1 == null ? "" : this.telNo1;
  }

/**
	* 連絡電話1<br>
	* 
  *
  * @param telNo1 連絡電話1
	*/
  public void setTelNo1(String telNo1) {
    this.telNo1 = telNo1;
  }

/**
	* 連絡電話2<br>
	* 
	* @return String
	*/
  public String getTelNo2() {
    return this.telNo2 == null ? "" : this.telNo2;
  }

/**
	* 連絡電話2<br>
	* 
  *
  * @param telNo2 連絡電話2
	*/
  public void setTelNo2(String telNo2) {
    this.telNo2 = telNo2;
  }

/**
	* 連絡電話3<br>
	* 
	* @return String
	*/
  public String getTelNo3() {
    return this.telNo3 == null ? "" : this.telNo3;
  }

/**
	* 連絡電話3<br>
	* 
  *
  * @param telNo3 連絡電話3
	*/
  public void setTelNo3(String telNo3) {
    this.telNo3 = telNo3;
  }

/**
	* 入帳日期<br>
	* 
	* @return Integer
	*/
  public int getEntryDate() {
    return StaticTool.bcToRoc(this.entryDate);
  }

/**
	* 入帳日期<br>
	* 
  *
  * @param entryDate 入帳日期
  * @throws LogicException when Date Is Warn	*/
  public void setEntryDate(int entryDate) throws LogicException {
    this.entryDate = StaticTool.rocToBc(entryDate);
  }

/**
	* 塗銷同意書編號<br>
	* 
	* @return String
	*/
  public String getAgreeNo() {
    return this.agreeNo == null ? "" : this.agreeNo;
  }

/**
	* 塗銷同意書編號<br>
	* 
  *
  * @param agreeNo 塗銷同意書編號
	*/
  public void setAgreeNo(String agreeNo) {
    this.agreeNo = agreeNo;
  }

/**
	* 公文編號<br>
	* 
	* @return Integer
	*/
  public int getDocNo() {
    return this.docNo;
  }

/**
	* 公文編號<br>
	* 
  *
  * @param docNo 公文編號
	*/
  public void setDocNo(int docNo) {
    this.docNo = docNo;
  }

/**
	* 銷號欄<br>
	* 
	* @return String
	*/
  public String getClsNo() {
    return this.clsNo == null ? "" : this.clsNo;
  }

/**
	* 銷號欄<br>
	* 
  *
  * @param clsNo 銷號欄
	*/
  public void setClsNo(String clsNo) {
    this.clsNo = clsNo;
  }

/**
	* 備註<br>
	* 
	* @return String
	*/
  public String getRmk() {
    return this.rmk == null ? "" : this.rmk;
  }

/**
	* 備註<br>
	* 
  *
  * @param rmk 備註
	*/
  public void setRmk(String rmk) {
    this.rmk = rmk;
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
	* 領取記號<br>
	* 0:未領取
1:已領取
	* @return Integer
	*/
  public int getReceiveFg() {
    return this.receiveFg;
  }

/**
	* 領取記號<br>
	* 0:未領取
1:已領取
  *
  * @param receiveFg 領取記號
	*/
  public void setReceiveFg(int receiveFg) {
    this.receiveFg = receiveFg;
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
    return "FacClose [facCloseId=" + facCloseId + ", facmNo=" + facmNo + ", actFlag=" + actFlag + ", funCode=" + funCode + ", carLoan=" + carLoan
           + ", applDate=" + applDate + ", closeDate=" + closeDate + ", closeInd=" + closeInd + ", closeReasonCode=" + closeReasonCode + ", closeAmt=" + closeAmt + ", collectFlag=" + collectFlag
           + ", collectWayCode=" + collectWayCode + ", receiveDate=" + receiveDate + ", telNo1=" + telNo1 + ", telNo2=" + telNo2 + ", telNo3=" + telNo3 + ", entryDate=" + entryDate
           + ", agreeNo=" + agreeNo + ", docNo=" + docNo + ", clsNo=" + clsNo + ", rmk=" + rmk + ", clCode1=" + clCode1 + ", clCode2=" + clCode2
           + ", clNo=" + clNo + ", receiveFg=" + receiveFg + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo
           + "]";
  }
}
