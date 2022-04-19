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
 * PostAuthLog 郵局授權記錄檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`PostAuthLog`")
public class PostAuthLog implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -6253308214672023446L;

@EmbeddedId
  private PostAuthLogId postAuthLogId;

  // 建檔日期
  @Column(name = "`AuthCreateDate`", insertable = false, updatable = false)
  private int authCreateDate = 0;

  // 申請代號，狀態碼
  /* CdCode.AuthApplCode1:申請2:終止3:郵局終止4:誤終止9:暫停授權(DeleteDate &amp;gt; 0時，顯示用) */
  @Column(name = "`AuthApplCode`", length = 1, insertable = false, updatable = false)
  private String authApplCode;

  // 戶號
  @Column(name = "`CustNo`", insertable = false, updatable = false)
  private int custNo = 0;

  // 帳戶別
  /* CdCode.PostDepCodeP:存簿G:劃撥 */
  @Column(name = "`PostDepCode`", length = 1, insertable = false, updatable = false)
  private String postDepCode;

  // 儲金帳號
  @Column(name = "`RepayAcct`", length = 14, insertable = false, updatable = false)
  private String repayAcct;

  // 授權類別
  /* CdCode.AuthCode1:期款2:火險 */
  @Column(name = "`AuthCode`", length = 1, insertable = false, updatable = false)
  private String authCode;

  // 額度
  @Column(name = "`FacmNo`")
  private int facmNo = 0;

  // 統一編號
  @Column(name = "`CustId`", length = 10)
  private String custId;

  // 每筆扣款限額
  @Column(name = "`LimitAmt`")
  private BigDecimal limitAmt = new BigDecimal("0");

  // 帳號碼
  @Column(name = "`RepayAcctSeq`", length = 2)
  private String repayAcctSeq;

  // 處理日期
  @Column(name = "`ProcessDate`")
  private int processDate = 0;

  // 處理時間
  @Column(name = "`ProcessTime`")
  private int processTime = 0;

  // 處理日期時間
  @Column(name = "`ProcessDateTime`")
  private java.sql.Timestamp processDateTime;

  // 核印完成日期
  @Column(name = "`StampFinishDate`")
  private int stampFinishDate = 0;

  // 核印取消日期
  @Column(name = "`StampCancelDate`")
  private int stampCancelDate = 0;

  // 核印註記
  /* CdCode.StampCode1:帳號不符2:戶名不符3:身分證號不符4:印鑑不符9:其他 */
  @Column(name = "`StampCode`", length = 1)
  private String stampCode;

  // 媒體碼
  /* 空白:未產出前Y:產出後 */
  @Column(name = "`PostMediaCode`", length = 1)
  private String postMediaCode;

  // 狀況代號，授權狀態
  /* CdCode.AuthErrorCode空:再次授權空白:未授權00:成功03:已終止代繳06:凍結警示戶07:支票專戶08:帳號錯誤09:終止戶10:身分證不符11:轉出戶12:拒絕往來戶13:無此編號14:編號已存在16:管制帳戶17:掛失戶18:異常帳戶19:編號非英數91:期限未扣款98:其他 */
  @Column(name = "`AuthErrorCode`", length = 2)
  private String authErrorCode;

  // 授權方式
  /* CdCode.AchAuthCodeA:紙本新增O:舊檔轉換X:紙本終止R:申請恢復 */
  @Column(name = "`AuthMeth`", length = 1)
  private String authMeth;

  // 媒體檔流水編號
  /* 媒體產出前為0 */
  @Column(name = "`FileSeq`")
  private int fileSeq = 0;

  // 提出日期
  /* 媒體產出日 */
  @Column(name = "`PropDate`")
  private int propDate = 0;

  // 提回日期
  @Column(name = "`RetrDate`")
  private int retrDate = 0;

  // 暫停授權日期
  @Column(name = "`DeleteDate`")
  private int deleteDate = 0;

  // 與借款人關係
  /* CdCode.RelationCode00:本人01:夫02:妻03:父04:母05:子06:女07:兄08:弟09:姊10:妹11:姪子99:其他 */
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
  /* CdCode.Sex */
  @Column(name = "`RelAcctGender`", length = 1)
  private String relAcctGender;

  // AML回應碼
  /* CdCode.AmlCheckItem0:非可疑名單/已完成名單確認1:需審查/確認2:為凍結名單/未確定名單 */
  @Column(name = "`AmlRsp`", length = 1)
  private String amlRsp;

  // 交易代號
  @Column(name = "`TitaTxCd`", length = 5)
  private String titaTxCd;

  // 建立者櫃員編號
  @Column(name = "`CreateEmpNo`", length = 6)
  private String createEmpNo;

  // 建檔日期
  @CreatedDate
  @Column(name = "`CreateDate`")
  private java.sql.Timestamp createDate;

  // 修改者櫃員編號
  @Column(name = "`LastUpdateEmpNo`", length = 6)
  private String lastUpdateEmpNo;

  // 異動日期
  @LastModifiedDate
  @Column(name = "`LastUpdate`")
  private java.sql.Timestamp lastUpdate;


  public PostAuthLogId getPostAuthLogId() {
    return this.postAuthLogId;
  }

  public void setPostAuthLogId(PostAuthLogId postAuthLogId) {
    this.postAuthLogId = postAuthLogId;
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
	* 申請代號，狀態碼<br>
	* CdCode.AuthApplCode
1:申請
2:終止
3:郵局終止
4:誤終止
9:暫停授權(DeleteDate &amp;gt; 0時，顯示用)
	* @return String
	*/
  public String getAuthApplCode() {
    return this.authApplCode == null ? "" : this.authApplCode;
  }

/**
	* 申請代號，狀態碼<br>
	* CdCode.AuthApplCode
1:申請
2:終止
3:郵局終止
4:誤終止
9:暫停授權(DeleteDate &amp;gt; 0時，顯示用)
  *
  * @param authApplCode 申請代號，狀態碼
	*/
  public void setAuthApplCode(String authApplCode) {
    this.authApplCode = authApplCode;
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
	* 帳戶別<br>
	* CdCode.PostDepCode
P:存簿
G:劃撥
	* @return String
	*/
  public String getPostDepCode() {
    return this.postDepCode == null ? "" : this.postDepCode;
  }

/**
	* 帳戶別<br>
	* CdCode.PostDepCode
P:存簿
G:劃撥
  *
  * @param postDepCode 帳戶別
	*/
  public void setPostDepCode(String postDepCode) {
    this.postDepCode = postDepCode;
  }

/**
	* 儲金帳號<br>
	* 
	* @return String
	*/
  public String getRepayAcct() {
    return this.repayAcct == null ? "" : this.repayAcct;
  }

/**
	* 儲金帳號<br>
	* 
  *
  * @param repayAcct 儲金帳號
	*/
  public void setRepayAcct(String repayAcct) {
    this.repayAcct = repayAcct;
  }

/**
	* 授權類別<br>
	* CdCode.AuthCode
1:期款
2:火險
	* @return String
	*/
  public String getAuthCode() {
    return this.authCode == null ? "" : this.authCode;
  }

/**
	* 授權類別<br>
	* CdCode.AuthCode
1:期款
2:火險
  *
  * @param authCode 授權類別
	*/
  public void setAuthCode(String authCode) {
    this.authCode = authCode;
  }

/**
	* 額度<br>
	* 
	* @return Integer
	*/
  public int getFacmNo() {
    return this.facmNo;
  }

/**
	* 額度<br>
	* 
  *
  * @param facmNo 額度
	*/
  public void setFacmNo(int facmNo) {
    this.facmNo = facmNo;
  }

/**
	* 統一編號<br>
	* 
	* @return String
	*/
  public String getCustId() {
    return this.custId == null ? "" : this.custId;
  }

/**
	* 統一編號<br>
	* 
  *
  * @param custId 統一編號
	*/
  public void setCustId(String custId) {
    this.custId = custId;
  }

/**
	* 每筆扣款限額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLimitAmt() {
    return this.limitAmt;
  }

/**
	* 每筆扣款限額<br>
	* 
  *
  * @param limitAmt 每筆扣款限額
	*/
  public void setLimitAmt(BigDecimal limitAmt) {
    this.limitAmt = limitAmt;
  }

/**
	* 帳號碼<br>
	* 
	* @return String
	*/
  public String getRepayAcctSeq() {
    return this.repayAcctSeq == null ? "" : this.repayAcctSeq;
  }

/**
	* 帳號碼<br>
	* 
  *
  * @param repayAcctSeq 帳號碼
	*/
  public void setRepayAcctSeq(String repayAcctSeq) {
    this.repayAcctSeq = repayAcctSeq;
  }

/**
	* 處理日期<br>
	* 
	* @return Integer
	*/
  public int getProcessDate() {
    return StaticTool.bcToRoc(this.processDate);
  }

/**
	* 處理日期<br>
	* 
  *
  * @param processDate 處理日期
  * @throws LogicException when Date Is Warn	*/
  public void setProcessDate(int processDate) throws LogicException {
    this.processDate = StaticTool.rocToBc(processDate);
  }

/**
	* 處理時間<br>
	* 
	* @return Integer
	*/
  public int getProcessTime() {
    return this.processTime;
  }

/**
	* 處理時間<br>
	* 
  *
  * @param processTime 處理時間
	*/
  public void setProcessTime(int processTime) {
    this.processTime = processTime;
  }

/**
	* 處理日期時間<br>
	* 
	* @return java.sql.Timestamp
	*/
  public java.sql.Timestamp getProcessDateTime() {
    return this.processDateTime;
  }

/**
	* 處理日期時間<br>
	* 
  *
  * @param processDateTime 處理日期時間
	*/
  public void setProcessDateTime(java.sql.Timestamp processDateTime) {
    this.processDateTime = processDateTime;
  }

/**
	* 核印完成日期<br>
	* 
	* @return Integer
	*/
  public int getStampFinishDate() {
    return StaticTool.bcToRoc(this.stampFinishDate);
  }

/**
	* 核印完成日期<br>
	* 
  *
  * @param stampFinishDate 核印完成日期
  * @throws LogicException when Date Is Warn	*/
  public void setStampFinishDate(int stampFinishDate) throws LogicException {
    this.stampFinishDate = StaticTool.rocToBc(stampFinishDate);
  }

/**
	* 核印取消日期<br>
	* 
	* @return Integer
	*/
  public int getStampCancelDate() {
    return StaticTool.bcToRoc(this.stampCancelDate);
  }

/**
	* 核印取消日期<br>
	* 
  *
  * @param stampCancelDate 核印取消日期
  * @throws LogicException when Date Is Warn	*/
  public void setStampCancelDate(int stampCancelDate) throws LogicException {
    this.stampCancelDate = StaticTool.rocToBc(stampCancelDate);
  }

/**
	* 核印註記<br>
	* CdCode.StampCode
1:帳號不符
2:戶名不符
3:身分證號不符
4:印鑑不符
9:其他
	* @return String
	*/
  public String getStampCode() {
    return this.stampCode == null ? "" : this.stampCode;
  }

/**
	* 核印註記<br>
	* CdCode.StampCode
1:帳號不符
2:戶名不符
3:身分證號不符
4:印鑑不符
9:其他
  *
  * @param stampCode 核印註記
	*/
  public void setStampCode(String stampCode) {
    this.stampCode = stampCode;
  }

/**
	* 媒體碼<br>
	* 空白:未產出前
Y:產出後
	* @return String
	*/
  public String getPostMediaCode() {
    return this.postMediaCode == null ? "" : this.postMediaCode;
  }

/**
	* 媒體碼<br>
	* 空白:未產出前
Y:產出後
  *
  * @param postMediaCode 媒體碼
	*/
  public void setPostMediaCode(String postMediaCode) {
    this.postMediaCode = postMediaCode;
  }

/**
	* 狀況代號，授權狀態<br>
	* CdCode.AuthErrorCode
空:再次授權
空白:未授權
00:成功
03:已終止代繳
06:凍結警示戶
07:支票專戶
08:帳號錯誤
09:終止戶
10:身分證不符
11:轉出戶
12:拒絕往來戶
13:無此編號
14:編號已存在
16:管制帳戶
17:掛失戶
18:異常帳戶
19:編號非英數
91:期限未扣款
98:其他
	* @return String
	*/
  public String getAuthErrorCode() {
    return this.authErrorCode == null ? "" : this.authErrorCode;
  }

/**
	* 狀況代號，授權狀態<br>
	* CdCode.AuthErrorCode
空:再次授權
空白:未授權
00:成功
03:已終止代繳
06:凍結警示戶
07:支票專戶
08:帳號錯誤
09:終止戶
10:身分證不符
11:轉出戶
12:拒絕往來戶
13:無此編號
14:編號已存在
16:管制帳戶
17:掛失戶
18:異常帳戶
19:編號非英數
91:期限未扣款
98:其他
  *
  * @param authErrorCode 狀況代號，授權狀態
	*/
  public void setAuthErrorCode(String authErrorCode) {
    this.authErrorCode = authErrorCode;
  }

/**
	* 授權方式<br>
	* CdCode.AchAuthCode
A:紙本新增
O:舊檔轉換
X:紙本終止
R:申請恢復
	* @return String
	*/
  public String getAuthMeth() {
    return this.authMeth == null ? "" : this.authMeth;
  }

/**
	* 授權方式<br>
	* CdCode.AchAuthCode
A:紙本新增
O:舊檔轉換
X:紙本終止
R:申請恢復
  *
  * @param authMeth 授權方式
	*/
  public void setAuthMeth(String authMeth) {
    this.authMeth = authMeth;
  }

/**
	* 媒體檔流水編號<br>
	* 媒體產出前為0
	* @return Integer
	*/
  public int getFileSeq() {
    return this.fileSeq;
  }

/**
	* 媒體檔流水編號<br>
	* 媒體產出前為0
  *
  * @param fileSeq 媒體檔流水編號
	*/
  public void setFileSeq(int fileSeq) {
    this.fileSeq = fileSeq;
  }

/**
	* 提出日期<br>
	* 媒體產出日
	* @return Integer
	*/
  public int getPropDate() {
    return StaticTool.bcToRoc(this.propDate);
  }

/**
	* 提出日期<br>
	* 媒體產出日
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
	* 暫停授權日期<br>
	* 
	* @return Integer
	*/
  public int getDeleteDate() {
    return StaticTool.bcToRoc(this.deleteDate);
  }

/**
	* 暫停授權日期<br>
	* 
  *
  * @param deleteDate 暫停授權日期
  * @throws LogicException when Date Is Warn	*/
  public void setDeleteDate(int deleteDate) throws LogicException {
    this.deleteDate = StaticTool.rocToBc(deleteDate);
  }

/**
	* 與借款人關係<br>
	* CdCode.RelationCode
00:本人
01:夫
02:妻
03:父
04:母
05:子
06:女
07:兄
08:弟
09:姊
10:妹
11:姪子
99:其他
	* @return String
	*/
  public String getRelationCode() {
    return this.relationCode == null ? "" : this.relationCode;
  }

/**
	* 與借款人關係<br>
	* CdCode.RelationCode
00:本人
01:夫
02:妻
03:父
04:母
05:子
06:女
07:兄
08:弟
09:姊
10:妹
11:姪子
99:其他
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
	* CdCode.Sex
	* @return String
	*/
  public String getRelAcctGender() {
    return this.relAcctGender == null ? "" : this.relAcctGender;
  }

/**
	* 第三人性別<br>
	* CdCode.Sex
  *
  * @param relAcctGender 第三人性別
	*/
  public void setRelAcctGender(String relAcctGender) {
    this.relAcctGender = relAcctGender;
  }

/**
	* AML回應碼<br>
	* CdCode.AmlCheckItem
0:非可疑名單/已完成名單確認
1:需審查/確認
2:為凍結名單/未確定名單
	* @return String
	*/
  public String getAmlRsp() {
    return this.amlRsp == null ? "" : this.amlRsp;
  }

/**
	* AML回應碼<br>
	* CdCode.AmlCheckItem
0:非可疑名單/已完成名單確認
1:需審查/確認
2:為凍結名單/未確定名單
  *
  * @param amlRsp AML回應碼
	*/
  public void setAmlRsp(String amlRsp) {
    this.amlRsp = amlRsp;
  }

/**
	* 交易代號<br>
	* 
	* @return String
	*/
  public String getTitaTxCd() {
    return this.titaTxCd == null ? "" : this.titaTxCd;
  }

/**
	* 交易代號<br>
	* 
  *
  * @param titaTxCd 交易代號
	*/
  public void setTitaTxCd(String titaTxCd) {
    this.titaTxCd = titaTxCd;
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
	* 建檔日期<br>
	* 
	* @return java.sql.Timestamp
	*/
  public java.sql.Timestamp getCreateDate() {
    return this.createDate;
  }

/**
	* 建檔日期<br>
	* 
  *
  * @param createDate 建檔日期
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
	* 異動日期<br>
	* 
	* @return java.sql.Timestamp
	*/
  public java.sql.Timestamp getLastUpdate() {
    return this.lastUpdate;
  }

/**
	* 異動日期<br>
	* 
  *
  * @param lastUpdate 異動日期
	*/
  public void setLastUpdate(java.sql.Timestamp lastUpdate) {
    this.lastUpdate = lastUpdate;
  }


  @Override
  public String toString() {
    return "PostAuthLog [postAuthLogId=" + postAuthLogId
           + ", facmNo=" + facmNo + ", custId=" + custId + ", limitAmt=" + limitAmt + ", repayAcctSeq=" + repayAcctSeq + ", processDate=" + processDate + ", processTime=" + processTime
           + ", processDateTime=" + processDateTime + ", stampFinishDate=" + stampFinishDate + ", stampCancelDate=" + stampCancelDate + ", stampCode=" + stampCode + ", postMediaCode=" + postMediaCode + ", authErrorCode=" + authErrorCode
           + ", authMeth=" + authMeth + ", fileSeq=" + fileSeq + ", propDate=" + propDate + ", retrDate=" + retrDate + ", deleteDate=" + deleteDate + ", relationCode=" + relationCode
           + ", relAcctName=" + relAcctName + ", relationId=" + relationId + ", relAcctBirthday=" + relAcctBirthday + ", relAcctGender=" + relAcctGender + ", amlRsp=" + amlRsp + ", titaTxCd=" + titaTxCd
           + ", createEmpNo=" + createEmpNo + ", createDate=" + createDate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + ", lastUpdate=" + lastUpdate + "]";
  }
}
