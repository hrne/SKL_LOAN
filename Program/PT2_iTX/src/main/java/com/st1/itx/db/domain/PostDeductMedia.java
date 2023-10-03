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
 * PostDeductMedia 郵局扣款媒體檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`PostDeductMedia`")
public class PostDeductMedia implements Serializable {


  @EmbeddedId
  private PostDeductMediaId postDeductMediaId;

  // 媒體日期
  @Column(name = "`MediaDate`", insertable = false, updatable = false)
  private int mediaDate = 0;

  // 媒體序號
  @Column(name = "`MediaSeq`", insertable = false, updatable = false)
  private int mediaSeq = 0;

  // 戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 額度號碼
  @Column(name = "`FacmNo`")
  private int facmNo = 0;

  // 還款類別
  /* CdCode.RepayType0:債協暫收款1:期款2:部分償還3:結案4:帳管費5:火險費6:契變手續費7:法務費9:其他11:債協匯入款(虛擬帳號為9510500NNNNNNN) */
  @Column(name = "`RepayType`")
  private int repayType = 0;

  // 還款金額
  @Column(name = "`RepayAmt`")
  private BigDecimal repayAmt = new BigDecimal("0");

  // 處理說明
  /* 單格空白:成功其他: CdCode ProcCode處理說明 ACH  : 002 + ReturnCode(2) 郵局 : 003 + ReturnCode(2) */
  @Column(name = "`ProcNoteCode`", length = 2)
  private String procNoteCode;

  // 帳戶別
  /* CdCode.PostDepCodeP:存簿G:劃撥 */
  @Column(name = "`PostDepCode`", length = 1)
  private String postDepCode;

  // 委託機構代號
  /* 846:期款53N:火險 */
  @Column(name = "`OutsrcCode`", length = 3)
  private String outsrcCode;

  // 區處代號
  /* 846:期款0001:帳管及契變手續0002:期款53N:火險空白 */
  @Column(name = "`DistCode`", length = 4)
  private String distCode;

  // 轉帳日期
  @Column(name = "`TransDate`")
  private int transDate = 0;

  // 儲金帳號
  @Column(name = "`RepayAcctNo`", length = 14)
  private String repayAcctNo;

  // 用戶編號
  /* 右靠左補空，大寫英數字，不得填寫中文(預計補2位帳號碼+扣款人ID+郵局存款別(POSCDE)+戶號) */
  @Column(name = "`PostUserNo`", length = 20)
  private String postUserNo;

  // 委託機構使用欄
  /* 預計為計息迄日+額度編號+入帳扣款別 */
  @Column(name = "`OutsrcRemark`", length = 20)
  private String outsrcRemark;

  // 會計日期
  /* 提回會計日期 */
  @Column(name = "`AcDate`")
  private int acDate = 0;

  // 批號
  @Column(name = "`BatchNo`", length = 6)
  private String batchNo;

  // 明細序號
  @Column(name = "`DetailSeq`")
  private int detailSeq = 0;

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

  // 身份證字號/統一編號錯誤註記
  /* Y:A:舊資料轉換B:舊資料轉換 */
  @Column(name = "`RelCustIdErrFg`", length = 1)
  private String relCustIdErrFg;

  // 存摺代號
  @Column(name = "`DepCode`", length = 2)
  private String depCode;


  public PostDeductMediaId getPostDeductMediaId() {
    return this.postDeductMediaId;
  }

  public void setPostDeductMediaId(PostDeductMediaId postDeductMediaId) {
    this.postDeductMediaId = postDeductMediaId;
  }

/**
	* 媒體日期<br>
	* 
	* @return Integer
	*/
  public int getMediaDate() {
    return StaticTool.bcToRoc(this.mediaDate);
  }

/**
	* 媒體日期<br>
	* 
  *
  * @param mediaDate 媒體日期
  * @throws LogicException when Date Is Warn	*/
  public void setMediaDate(int mediaDate) throws LogicException {
    this.mediaDate = StaticTool.rocToBc(mediaDate);
  }

/**
	* 媒體序號<br>
	* 
	* @return Integer
	*/
  public int getMediaSeq() {
    return this.mediaSeq;
  }

/**
	* 媒體序號<br>
	* 
  *
  * @param mediaSeq 媒體序號
	*/
  public void setMediaSeq(int mediaSeq) {
    this.mediaSeq = mediaSeq;
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
	* 額度號碼<br>
	* 
	* @return Integer
	*/
  public int getFacmNo() {
    return this.facmNo;
  }

/**
	* 額度號碼<br>
	* 
  *
  * @param facmNo 額度號碼
	*/
  public void setFacmNo(int facmNo) {
    this.facmNo = facmNo;
  }

/**
	* 還款類別<br>
	* CdCode.RepayType
0:債協暫收款
1:期款
2:部分償還
3:結案
4:帳管費
5:火險費
6:契變手續費
7:法務費
9:其他
11:債協匯入款(虛擬帳號為9510500NNNNNNN)
	* @return Integer
	*/
  public int getRepayType() {
    return this.repayType;
  }

/**
	* 還款類別<br>
	* CdCode.RepayType
0:債協暫收款
1:期款
2:部分償還
3:結案
4:帳管費
5:火險費
6:契變手續費
7:法務費
9:其他
11:債協匯入款(虛擬帳號為9510500NNNNNNN)
  *
  * @param repayType 還款類別
	*/
  public void setRepayType(int repayType) {
    this.repayType = repayType;
  }

/**
	* 還款金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getRepayAmt() {
    return this.repayAmt;
  }

/**
	* 還款金額<br>
	* 
  *
  * @param repayAmt 還款金額
	*/
  public void setRepayAmt(BigDecimal repayAmt) {
    this.repayAmt = repayAmt;
  }

/**
	* 處理說明<br>
	* 單格空白:成功
其他: CdCode ProcCode處理說明
 ACH  : 002 + ReturnCode(2)
 郵局 : 003 + ReturnCode(2)
	* @return String
	*/
  public String getProcNoteCode() {
    return this.procNoteCode == null ? "" : this.procNoteCode;
  }

/**
	* 處理說明<br>
	* 單格空白:成功
其他: CdCode ProcCode處理說明
 ACH  : 002 + ReturnCode(2)
 郵局 : 003 + ReturnCode(2)
  *
  * @param procNoteCode 處理說明
	*/
  public void setProcNoteCode(String procNoteCode) {
    this.procNoteCode = procNoteCode;
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
	* 委託機構代號<br>
	* 846:期款
53N:火險
	* @return String
	*/
  public String getOutsrcCode() {
    return this.outsrcCode == null ? "" : this.outsrcCode;
  }

/**
	* 委託機構代號<br>
	* 846:期款
53N:火險
  *
  * @param outsrcCode 委託機構代號
	*/
  public void setOutsrcCode(String outsrcCode) {
    this.outsrcCode = outsrcCode;
  }

/**
	* 區處代號<br>
	* 846:期款
0001:帳管及契變手續
0002:期款
53N:火險
空白
	* @return String
	*/
  public String getDistCode() {
    return this.distCode == null ? "" : this.distCode;
  }

/**
	* 區處代號<br>
	* 846:期款
0001:帳管及契變手續
0002:期款
53N:火險
空白
  *
  * @param distCode 區處代號
	*/
  public void setDistCode(String distCode) {
    this.distCode = distCode;
  }

/**
	* 轉帳日期<br>
	* 
	* @return Integer
	*/
  public int getTransDate() {
    return StaticTool.bcToRoc(this.transDate);
  }

/**
	* 轉帳日期<br>
	* 
  *
  * @param transDate 轉帳日期
  * @throws LogicException when Date Is Warn	*/
  public void setTransDate(int transDate) throws LogicException {
    this.transDate = StaticTool.rocToBc(transDate);
  }

/**
	* 儲金帳號<br>
	* 
	* @return String
	*/
  public String getRepayAcctNo() {
    return this.repayAcctNo == null ? "" : this.repayAcctNo;
  }

/**
	* 儲金帳號<br>
	* 
  *
  * @param repayAcctNo 儲金帳號
	*/
  public void setRepayAcctNo(String repayAcctNo) {
    this.repayAcctNo = repayAcctNo;
  }

/**
	* 用戶編號<br>
	* 右靠左補空，大寫英數字，不得填寫中文(預計補2位帳號碼+扣款人ID+郵局存款別(POSCDE)+戶號)
	* @return String
	*/
  public String getPostUserNo() {
    return this.postUserNo == null ? "" : this.postUserNo;
  }

/**
	* 用戶編號<br>
	* 右靠左補空，大寫英數字，不得填寫中文(預計補2位帳號碼+扣款人ID+郵局存款別(POSCDE)+戶號)
  *
  * @param postUserNo 用戶編號
	*/
  public void setPostUserNo(String postUserNo) {
    this.postUserNo = postUserNo;
  }

/**
	* 委託機構使用欄<br>
	* 預計為計息迄日+額度編號+入帳扣款別
	* @return String
	*/
  public String getOutsrcRemark() {
    return this.outsrcRemark == null ? "" : this.outsrcRemark;
  }

/**
	* 委託機構使用欄<br>
	* 預計為計息迄日+額度編號+入帳扣款別
  *
  * @param outsrcRemark 委託機構使用欄
	*/
  public void setOutsrcRemark(String outsrcRemark) {
    this.outsrcRemark = outsrcRemark;
  }

/**
	* 會計日期<br>
	* 提回會計日期
	* @return Integer
	*/
  public int getAcDate() {
    return StaticTool.bcToRoc(this.acDate);
  }

/**
	* 會計日期<br>
	* 提回會計日期
  *
  * @param acDate 會計日期
  * @throws LogicException when Date Is Warn	*/
  public void setAcDate(int acDate) throws LogicException {
    this.acDate = StaticTool.rocToBc(acDate);
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
	* 明細序號<br>
	* 
	* @return Integer
	*/
  public int getDetailSeq() {
    return this.detailSeq;
  }

/**
	* 明細序號<br>
	* 
  *
  * @param detailSeq 明細序號
	*/
  public void setDetailSeq(int detailSeq) {
    this.detailSeq = detailSeq;
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
	* 身份證字號/統一編號錯誤註記<br>
	* Y:
A:舊資料轉換
B:舊資料轉換
	* @return String
	*/
  public String getRelCustIdErrFg() {
    return this.relCustIdErrFg == null ? "" : this.relCustIdErrFg;
  }

/**
	* 身份證字號/統一編號錯誤註記<br>
	* Y:
A:舊資料轉換
B:舊資料轉換
  *
  * @param relCustIdErrFg 身份證字號/統一編號錯誤註記
	*/
  public void setRelCustIdErrFg(String relCustIdErrFg) {
    this.relCustIdErrFg = relCustIdErrFg;
  }

/**
	* 存摺代號<br>
	* 
	* @return String
	*/
  public String getDepCode() {
    return this.depCode == null ? "" : this.depCode;
  }

/**
	* 存摺代號<br>
	* 
  *
  * @param depCode 存摺代號
	*/
  public void setDepCode(String depCode) {
    this.depCode = depCode;
  }


  @Override
  public String toString() {
    return "PostDeductMedia [postDeductMediaId=" + postDeductMediaId + ", custNo=" + custNo + ", facmNo=" + facmNo + ", repayType=" + repayType + ", repayAmt=" + repayAmt
           + ", procNoteCode=" + procNoteCode + ", postDepCode=" + postDepCode + ", outsrcCode=" + outsrcCode + ", distCode=" + distCode + ", transDate=" + transDate + ", repayAcctNo=" + repayAcctNo
           + ", postUserNo=" + postUserNo + ", outsrcRemark=" + outsrcRemark + ", acDate=" + acDate + ", batchNo=" + batchNo + ", detailSeq=" + detailSeq + ", createDate=" + createDate
           + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + ", relCustIdErrFg=" + relCustIdErrFg + ", depCode=" + depCode + "]";
  }
}
