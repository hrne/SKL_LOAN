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
 * AchAuthLog ACH授權記錄檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`AchAuthLog`")
public class AchAuthLog implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 5715145210486888838L;

@EmbeddedId
  private AchAuthLogId achAuthLogId;

  // 建檔日期
  @Column(name = "`AuthCreateDate`", insertable = false, updatable = false)
  private int authCreateDate = 0;

  // 戶號
  @Column(name = "`CustNo`", insertable = false, updatable = false)
  private int custNo = 0;

  // 扣款銀行
  @Column(name = "`RepayBank`", length = 3, insertable = false, updatable = false)
  private String repayBank;

  // 扣款帳號
  @Column(name = "`RepayAcct`", length = 14, insertable = false, updatable = false)
  private String repayAcct;

  // 新增或取消
  /* A:新增(恢復授權)D:取消Z:暫停授權 */
  @Column(name = "`CreateFlag`", length = 1, insertable = false, updatable = false)
  private String createFlag;

  // 額度號碼
  /* 使用該扣款帳號之第一個額度 */
  @Column(name = "`FacmNo`")
  private int facmNo = 0;

  // 處理日期
  /* 最新動作之日期時間(Ex.建檔提出產媒體) */
  @Column(name = "`ProcessDate`")
  private int processDate = 0;

  // 核印完成日期時間
  /* 提回並且成功之日期時間 */
  @Column(name = "`StampFinishDate`")
  private int stampFinishDate = 0;

  // 授權狀態
  /* 空白-未授權0-成功新增或取消授權扣款1-印鑑不符2-無此帳號3-委繳戶統一編號不符4-已核印成功在案5-原交易不存在6-電子資料與授權書內容不符7-帳戶已結清8-印鑑不清9-其他A-未收到授權書B-用戶號碼錯誤C-靜止戶D-未收到聲明書E-授權書資料不全F-警示戶G-本帳戶不適用授權扣繳H-已於他行授權扣款I-該用戶已死亡Z-未交易或匯入失敗資料 */
  @Column(name = "`AuthStatus`", length = 1)
  private String authStatus;

  // 授權方式
  /* A:紙本新增O:舊檔轉換 */
  @Column(name = "`AuthMeth`", length = 1)
  private String authMeth;

  // 每筆扣款限額
  /* 媒體檔規格為X(8) */
  @Column(name = "`LimitAmt`")
  private BigDecimal limitAmt = new BigDecimal("0");

  // 媒體碼
  /* 空白:未產生媒體Y:已產生媒體 */
  @Column(name = "`MediaCode`", length = 1)
  private String mediaCode;

  // 批號
  @Column(name = "`BatchNo`", length = 6)
  private String batchNo;

  // 提出日期
  @Column(name = "`PropDate`")
  private int propDate = 0;

  // 提回日期
  @Column(name = "`RetrDate`")
  private int retrDate = 0;

  // 刪除日期
  @Column(name = "`DeleteDate`")
  private int deleteDate = 0;

  // 與借款人關係
  /* 共用代碼檔00本人01夫02妻03父04母05子06女07兄08弟09姊10妹11姪子99其他 */
  @Column(name = "`RelationCode`", length = 2)
  private String relationCode;

  // 第三人帳戶戶名
  @Column(name = "`RelAcctName`", length = 100)
  private String relAcctName;

  // 第三人身分證字號
  @Column(name = "`RelationId`", length = 10)
  private String relationId;

  // 第三人出生日期
  @Column(name = "`RelAcctBirthday`")
  private int relAcctBirthday = 0;

  // 第三人性別
  @Column(name = "`RelAcctGender`", length = 1)
  private String relAcctGender;

  // AML回應碼
  /* 0.非可疑名單/已完成名單確認1.需審查/確認2.為凍結名單/未確定名單 */
  @Column(name = "`AmlRsp`", length = 1)
  private String amlRsp;

  // 建立者櫃員編號
  @Column(name = "`CreateEmpNo`", length = 6)
  private String createEmpNo;

  // 建立日期時間
  @CreatedDate
  @Column(name = "`CreateDate`")
  private java.sql.Timestamp createDate;

  // 修改者櫃員編號
  @Column(name = "`LastUpdateEmpNo`", length = 6)
  private String lastUpdateEmpNo;

  // 修改日期時間
  @LastModifiedDate
  @Column(name = "`LastUpdate`")
  private java.sql.Timestamp lastUpdate;


  public AchAuthLogId getAchAuthLogId() {
    return this.achAuthLogId;
  }

  public void setAchAuthLogId(AchAuthLogId achAuthLogId) {
    this.achAuthLogId = achAuthLogId;
  }

/**
	* 建檔日期<br>
	* 
	* @return Integer
	*/
  public int getAuthCreateDate() {
    return StaticTool.bcToRoc(this.authCreateDate);
  }

/**
	* 建檔日期<br>
	* 
  *
  * @param authCreateDate 建檔日期
  * @throws LogicException when Date Is Warn	*/
  public void setAuthCreateDate(int authCreateDate) throws LogicException {
    this.authCreateDate = StaticTool.rocToBc(authCreateDate);
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
	* 扣款銀行<br>
	* 
	* @return String
	*/
  public String getRepayBank() {
    return this.repayBank == null ? "" : this.repayBank;
  }

/**
	* 扣款銀行<br>
	* 
  *
  * @param repayBank 扣款銀行
	*/
  public void setRepayBank(String repayBank) {
    this.repayBank = repayBank;
  }

/**
	* 扣款帳號<br>
	* 
	* @return String
	*/
  public String getRepayAcct() {
    return this.repayAcct == null ? "" : this.repayAcct;
  }

/**
	* 扣款帳號<br>
	* 
  *
  * @param repayAcct 扣款帳號
	*/
  public void setRepayAcct(String repayAcct) {
    this.repayAcct = repayAcct;
  }

/**
	* 新增或取消<br>
	* A:新增(恢復授權)
D:取消
Z:暫停授權
	* @return String
	*/
  public String getCreateFlag() {
    return this.createFlag == null ? "" : this.createFlag;
  }

/**
	* 新增或取消<br>
	* A:新增(恢復授權)
D:取消
Z:暫停授權
  *
  * @param createFlag 新增或取消
	*/
  public void setCreateFlag(String createFlag) {
    this.createFlag = createFlag;
  }

/**
	* 額度號碼<br>
	* 使用該扣款帳號之第一個額度
	* @return Integer
	*/
  public int getFacmNo() {
    return this.facmNo;
  }

/**
	* 額度號碼<br>
	* 使用該扣款帳號之第一個額度
  *
  * @param facmNo 額度號碼
	*/
  public void setFacmNo(int facmNo) {
    this.facmNo = facmNo;
  }

/**
	* 處理日期<br>
	* 最新動作之日期時間(Ex.建檔提出產媒體)
	* @return Integer
	*/
  public int getProcessDate() {
    return StaticTool.bcToRoc(this.processDate);
  }

/**
	* 處理日期<br>
	* 最新動作之日期時間(Ex.建檔提出產媒體)
  *
  * @param processDate 處理日期
  * @throws LogicException when Date Is Warn	*/
  public void setProcessDate(int processDate) throws LogicException {
    this.processDate = StaticTool.rocToBc(processDate);
  }

/**
	* 核印完成日期時間<br>
	* 提回並且成功之日期時間
	* @return Integer
	*/
  public int getStampFinishDate() {
    return StaticTool.bcToRoc(this.stampFinishDate);
  }

/**
	* 核印完成日期時間<br>
	* 提回並且成功之日期時間
  *
  * @param stampFinishDate 核印完成日期時間
  * @throws LogicException when Date Is Warn	*/
  public void setStampFinishDate(int stampFinishDate) throws LogicException {
    this.stampFinishDate = StaticTool.rocToBc(stampFinishDate);
  }

/**
	* 授權狀態<br>
	* 空白-未授權
0-成功新增或取消授權扣款
1-印鑑不符
2-無此帳號
3-委繳戶統一編號不符
4-已核印成功在案
5-原交易不存在
6-電子資料與授權書內容不符
7-帳戶已結清
8-印鑑不清
9-其他
A-未收到授權書
B-用戶號碼錯誤
C-靜止戶
D-未收到聲明書
E-授權書資料不全
F-警示戶
G-本帳戶不適用授權扣繳
H-已於他行授權扣款
I-該用戶已死亡
Z-未交易或匯入失敗資料
	* @return String
	*/
  public String getAuthStatus() {
    return this.authStatus == null ? "" : this.authStatus;
  }

/**
	* 授權狀態<br>
	* 空白-未授權
0-成功新增或取消授權扣款
1-印鑑不符
2-無此帳號
3-委繳戶統一編號不符
4-已核印成功在案
5-原交易不存在
6-電子資料與授權書內容不符
7-帳戶已結清
8-印鑑不清
9-其他
A-未收到授權書
B-用戶號碼錯誤
C-靜止戶
D-未收到聲明書
E-授權書資料不全
F-警示戶
G-本帳戶不適用授權扣繳
H-已於他行授權扣款
I-該用戶已死亡
Z-未交易或匯入失敗資料
  *
  * @param authStatus 授權狀態
	*/
  public void setAuthStatus(String authStatus) {
    this.authStatus = authStatus;
  }

/**
	* 授權方式<br>
	* A:紙本新增
O:舊檔轉換
	* @return String
	*/
  public String getAuthMeth() {
    return this.authMeth == null ? "" : this.authMeth;
  }

/**
	* 授權方式<br>
	* A:紙本新增
O:舊檔轉換
  *
  * @param authMeth 授權方式
	*/
  public void setAuthMeth(String authMeth) {
    this.authMeth = authMeth;
  }

/**
	* 每筆扣款限額<br>
	* 媒體檔規格為X(8)
	* @return BigDecimal
	*/
  public BigDecimal getLimitAmt() {
    return this.limitAmt;
  }

/**
	* 每筆扣款限額<br>
	* 媒體檔規格為X(8)
  *
  * @param limitAmt 每筆扣款限額
	*/
  public void setLimitAmt(BigDecimal limitAmt) {
    this.limitAmt = limitAmt;
  }

/**
	* 媒體碼<br>
	* 空白:未產生媒體
Y:已產生媒體
	* @return String
	*/
  public String getMediaCode() {
    return this.mediaCode == null ? "" : this.mediaCode;
  }

/**
	* 媒體碼<br>
	* 空白:未產生媒體
Y:已產生媒體
  *
  * @param mediaCode 媒體碼
	*/
  public void setMediaCode(String mediaCode) {
    this.mediaCode = mediaCode;
  }

/**
	* 批號<br>
	* 
	* @return String
	*/
  public String getBatchNo() {
    return this.batchNo == null ? "" : this.batchNo;
  }

/**
	* 批號<br>
	* 
  *
  * @param batchNo 批號
	*/
  public void setBatchNo(String batchNo) {
    this.batchNo = batchNo;
  }

/**
	* 提出日期<br>
	* 
	* @return Integer
	*/
  public int getPropDate() {
    return StaticTool.bcToRoc(this.propDate);
  }

/**
	* 提出日期<br>
	* 
  *
  * @param propDate 提出日期
  * @throws LogicException when Date Is Warn	*/
  public void setPropDate(int propDate) throws LogicException {
    this.propDate = StaticTool.rocToBc(propDate);
  }

/**
	* 提回日期<br>
	* 
	* @return Integer
	*/
  public int getRetrDate() {
    return StaticTool.bcToRoc(this.retrDate);
  }

/**
	* 提回日期<br>
	* 
  *
  * @param retrDate 提回日期
  * @throws LogicException when Date Is Warn	*/
  public void setRetrDate(int retrDate) throws LogicException {
    this.retrDate = StaticTool.rocToBc(retrDate);
  }

/**
	* 刪除日期<br>
	* 
	* @return Integer
	*/
  public int getDeleteDate() {
    return StaticTool.bcToRoc(this.deleteDate);
  }

/**
	* 刪除日期<br>
	* 
  *
  * @param deleteDate 刪除日期
  * @throws LogicException when Date Is Warn	*/
  public void setDeleteDate(int deleteDate) throws LogicException {
    this.deleteDate = StaticTool.rocToBc(deleteDate);
  }

/**
	* 與借款人關係<br>
	* 共用代碼檔
00本人
01夫
02妻
03父
04母
05子
06女
07兄
08弟
09姊
10妹
11姪子
99其他
	* @return String
	*/
  public String getRelationCode() {
    return this.relationCode == null ? "" : this.relationCode;
  }

/**
	* 與借款人關係<br>
	* 共用代碼檔
00本人
01夫
02妻
03父
04母
05子
06女
07兄
08弟
09姊
10妹
11姪子
99其他
  *
  * @param relationCode 與借款人關係
	*/
  public void setRelationCode(String relationCode) {
    this.relationCode = relationCode;
  }

/**
	* 第三人帳戶戶名<br>
	* 
	* @return String
	*/
  public String getRelAcctName() {
    return this.relAcctName == null ? "" : this.relAcctName;
  }

/**
	* 第三人帳戶戶名<br>
	* 
  *
  * @param relAcctName 第三人帳戶戶名
	*/
  public void setRelAcctName(String relAcctName) {
    this.relAcctName = relAcctName;
  }

/**
	* 第三人身分證字號<br>
	* 
	* @return String
	*/
  public String getRelationId() {
    return this.relationId == null ? "" : this.relationId;
  }

/**
	* 第三人身分證字號<br>
	* 
  *
  * @param relationId 第三人身分證字號
	*/
  public void setRelationId(String relationId) {
    this.relationId = relationId;
  }

/**
	* 第三人出生日期<br>
	* 
	* @return Integer
	*/
  public int getRelAcctBirthday() {
    return StaticTool.bcToRoc(this.relAcctBirthday);
  }

/**
	* 第三人出生日期<br>
	* 
  *
  * @param relAcctBirthday 第三人出生日期
  * @throws LogicException when Date Is Warn	*/
  public void setRelAcctBirthday(int relAcctBirthday) throws LogicException {
    this.relAcctBirthday = StaticTool.rocToBc(relAcctBirthday);
  }

/**
	* 第三人性別<br>
	* 
	* @return String
	*/
  public String getRelAcctGender() {
    return this.relAcctGender == null ? "" : this.relAcctGender;
  }

/**
	* 第三人性別<br>
	* 
  *
  * @param relAcctGender 第三人性別
	*/
  public void setRelAcctGender(String relAcctGender) {
    this.relAcctGender = relAcctGender;
  }

/**
	* AML回應碼<br>
	* 0.非可疑名單/已完成名單確認
1.需審查/確認
2.為凍結名單/未確定名單
	* @return String
	*/
  public String getAmlRsp() {
    return this.amlRsp == null ? "" : this.amlRsp;
  }

/**
	* AML回應碼<br>
	* 0.非可疑名單/已完成名單確認
1.需審查/確認
2.為凍結名單/未確定名單
  *
  * @param amlRsp AML回應碼
	*/
  public void setAmlRsp(String amlRsp) {
    this.amlRsp = amlRsp;
  }

/**
	* 建立者櫃員編號<br>
	* 
	* @return String
	*/
  public String getCreateEmpNo() {
    return this.createEmpNo == null ? "" : this.createEmpNo;
  }

/**
	* 建立者櫃員編號<br>
	* 
  *
  * @param createEmpNo 建立者櫃員編號
	*/
  public void setCreateEmpNo(String createEmpNo) {
    this.createEmpNo = createEmpNo;
  }

/**
	* 建立日期時間<br>
	* 
	* @return java.sql.Timestamp
	*/
  public java.sql.Timestamp getCreateDate() {
    return this.createDate;
  }

/**
	* 建立日期時間<br>
	* 
  *
  * @param createDate 建立日期時間
	*/
  public void setCreateDate(java.sql.Timestamp createDate) {
    this.createDate = createDate;
  }

/**
	* 修改者櫃員編號<br>
	* 
	* @return String
	*/
  public String getLastUpdateEmpNo() {
    return this.lastUpdateEmpNo == null ? "" : this.lastUpdateEmpNo;
  }

/**
	* 修改者櫃員編號<br>
	* 
  *
  * @param lastUpdateEmpNo 修改者櫃員編號
	*/
  public void setLastUpdateEmpNo(String lastUpdateEmpNo) {
    this.lastUpdateEmpNo = lastUpdateEmpNo;
  }

/**
	* 修改日期時間<br>
	* 
	* @return java.sql.Timestamp
	*/
  public java.sql.Timestamp getLastUpdate() {
    return this.lastUpdate;
  }

/**
	* 修改日期時間<br>
	* 
  *
  * @param lastUpdate 修改日期時間
	*/
  public void setLastUpdate(java.sql.Timestamp lastUpdate) {
    this.lastUpdate = lastUpdate;
  }


  @Override
  public String toString() {
    return "AchAuthLog [achAuthLogId=" + achAuthLogId + ", facmNo=" + facmNo
           + ", processDate=" + processDate + ", stampFinishDate=" + stampFinishDate + ", authStatus=" + authStatus + ", authMeth=" + authMeth + ", limitAmt=" + limitAmt + ", mediaCode=" + mediaCode
           + ", batchNo=" + batchNo + ", propDate=" + propDate + ", retrDate=" + retrDate + ", deleteDate=" + deleteDate + ", relationCode=" + relationCode + ", relAcctName=" + relAcctName
           + ", relationId=" + relationId + ", relAcctBirthday=" + relAcctBirthday + ", relAcctGender=" + relAcctGender + ", amlRsp=" + amlRsp + ", createEmpNo=" + createEmpNo + ", createDate=" + createDate
           + ", lastUpdateEmpNo=" + lastUpdateEmpNo + ", lastUpdate=" + lastUpdate + "]";
  }
}
