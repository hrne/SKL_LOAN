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
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * ForeclosureFee 法拍費用檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`ForeclosureFee`")
public class ForeclosureFee implements Serializable {


  // 記錄號碼
  @Id
  @Column(name = "`RecordNo`")
  private int recordNo = 0;

  // 借款人戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 額度編號
  @Column(name = "`FacmNo`")
  private int facmNo = 0;

  // 收件日期
  @Column(name = "`ReceiveDate`")
  private int receiveDate = 0;

  // 單據日期
  /* 暫付款單據日期 */
  @Column(name = "`DocDate`")
  private int docDate = 0;

  // 起帳日期
  /* 入暫付款的會計日期 */
  @Column(name = "`OpenAcDate`")
  private int openAcDate = 0;

  // 銷號日期
  /* 客戶繳款的會計日期 */
  @Column(name = "`CloseDate`")
  private int closeDate = 0;

  // 法拍費用
  @Column(name = "`Fee`")
  private BigDecimal fee = new BigDecimal("0");

  // 科目
  /* 共用代碼檔01:郵費02:支付命令03:公示送達04:裁定費05:執行費06:測量費07:鑑價費08:刊報費09:假扣押擔保金10:前項結餘11:全額沖銷12:退出納課13:警察陪同費14:查財產費用15:催收沖銷(勿用)99:其它 */
  @Column(name = "`FeeCode`", length = 2)
  private String feeCode;

  // 法務人員
  @Column(name = "`LegalStaff`", length = 6)
  private String legalStaff;

  // 沖銷號碼
  /* 銷帳時新增一筆紀錄號碼(11:全額沖銷、15:催收沖銷)，將此紀錄號碼寫入原被沖銷該筆 */
  @Column(name = "`CloseNo`")
  private int closeNo = 0;

  // 備註
  @Column(name = "`Rmk`", length = 60)
  private String rmk;

  // 件別
  /* 1:暫收抵繳2:匯款3:呆帳戶法務費墊付 */
  @Column(name = "`CaseCode`")
  private int caseCode = 0;

  // 匯款單位
  @Column(name = "`RemitBranch`", length = 3)
  private String remitBranch;

  // 匯款人
  @Column(name = "`Remitter`", length = 10)
  private String remitter;

  // 案號
  @Column(name = "`CaseNo`", length = 3)
  private String caseNo;

  // 轉催收日
  /* ac處理 */
  @Column(name = "`OverdueDate`")
  private int overdueDate = 0;

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
	* 記錄號碼<br>
	* 
	* @return Integer
	*/
  public int getRecordNo() {
    return this.recordNo;
  }

/**
	* 記錄號碼<br>
	* 
  *
  * @param recordNo 記錄號碼
	*/
  public void setRecordNo(int recordNo) {
    this.recordNo = recordNo;
  }

/**
	* 借款人戶號<br>
	* 
	* @return Integer
	*/
  public int getCustNo() {
    return this.custNo;
  }

/**
	* 借款人戶號<br>
	* 
  *
  * @param custNo 借款人戶號
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
	* 收件日期<br>
	* 
	* @return Integer
	*/
  public int getReceiveDate() {
    return StaticTool.bcToRoc(this.receiveDate);
  }

/**
	* 收件日期<br>
	* 
  *
  * @param receiveDate 收件日期
  * @throws LogicException when Date Is Warn	*/
  public void setReceiveDate(int receiveDate) throws LogicException {
    this.receiveDate = StaticTool.rocToBc(receiveDate);
  }

/**
	* 單據日期<br>
	* 暫付款單據日期
	* @return Integer
	*/
  public int getDocDate() {
    return StaticTool.bcToRoc(this.docDate);
  }

/**
	* 單據日期<br>
	* 暫付款單據日期
  *
  * @param docDate 單據日期
  * @throws LogicException when Date Is Warn	*/
  public void setDocDate(int docDate) throws LogicException {
    this.docDate = StaticTool.rocToBc(docDate);
  }

/**
	* 起帳日期<br>
	* 入暫付款的會計日期
	* @return Integer
	*/
  public int getOpenAcDate() {
    return StaticTool.bcToRoc(this.openAcDate);
  }

/**
	* 起帳日期<br>
	* 入暫付款的會計日期
  *
  * @param openAcDate 起帳日期
  * @throws LogicException when Date Is Warn	*/
  public void setOpenAcDate(int openAcDate) throws LogicException {
    this.openAcDate = StaticTool.rocToBc(openAcDate);
  }

/**
	* 銷號日期<br>
	* 客戶繳款的會計日期
	* @return Integer
	*/
  public int getCloseDate() {
    return StaticTool.bcToRoc(this.closeDate);
  }

/**
	* 銷號日期<br>
	* 客戶繳款的會計日期
  *
  * @param closeDate 銷號日期
  * @throws LogicException when Date Is Warn	*/
  public void setCloseDate(int closeDate) throws LogicException {
    this.closeDate = StaticTool.rocToBc(closeDate);
  }

/**
	* 法拍費用<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getFee() {
    return this.fee;
  }

/**
	* 法拍費用<br>
	* 
  *
  * @param fee 法拍費用
	*/
  public void setFee(BigDecimal fee) {
    this.fee = fee;
  }

/**
	* 科目<br>
	* 共用代碼檔
01:郵費
02:支付命令
03:公示送達
04:裁定費
05:執行費
06:測量費
07:鑑價費
08:刊報費
09:假扣押擔保金
10:前項結餘
11:全額沖銷
12:退出納課
13:警察陪同費
14:查財產費用
15:催收沖銷(勿用)
99:其它
	* @return String
	*/
  public String getFeeCode() {
    return this.feeCode == null ? "" : this.feeCode;
  }

/**
	* 科目<br>
	* 共用代碼檔
01:郵費
02:支付命令
03:公示送達
04:裁定費
05:執行費
06:測量費
07:鑑價費
08:刊報費
09:假扣押擔保金
10:前項結餘
11:全額沖銷
12:退出納課
13:警察陪同費
14:查財產費用
15:催收沖銷(勿用)
99:其它
  *
  * @param feeCode 科目
	*/
  public void setFeeCode(String feeCode) {
    this.feeCode = feeCode;
  }

/**
	* 法務人員<br>
	* 
	* @return String
	*/
  public String getLegalStaff() {
    return this.legalStaff == null ? "" : this.legalStaff;
  }

/**
	* 法務人員<br>
	* 
  *
  * @param legalStaff 法務人員
	*/
  public void setLegalStaff(String legalStaff) {
    this.legalStaff = legalStaff;
  }

/**
	* 沖銷號碼<br>
	* 銷帳時新增一筆紀錄號碼(11:全額沖銷、15:催收沖銷)，將此紀錄號碼寫入原被沖銷該筆
	* @return Integer
	*/
  public int getCloseNo() {
    return this.closeNo;
  }

/**
	* 沖銷號碼<br>
	* 銷帳時新增一筆紀錄號碼(11:全額沖銷、15:催收沖銷)，將此紀錄號碼寫入原被沖銷該筆
  *
  * @param closeNo 沖銷號碼
	*/
  public void setCloseNo(int closeNo) {
    this.closeNo = closeNo;
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
	* 件別<br>
	* 1:暫收抵繳
2:匯款
3:呆帳戶法務費墊付
	* @return Integer
	*/
  public int getCaseCode() {
    return this.caseCode;
  }

/**
	* 件別<br>
	* 1:暫收抵繳
2:匯款
3:呆帳戶法務費墊付
  *
  * @param caseCode 件別
	*/
  public void setCaseCode(int caseCode) {
    this.caseCode = caseCode;
  }

/**
	* 匯款單位<br>
	* 
	* @return String
	*/
  public String getRemitBranch() {
    return this.remitBranch == null ? "" : this.remitBranch;
  }

/**
	* 匯款單位<br>
	* 
  *
  * @param remitBranch 匯款單位
	*/
  public void setRemitBranch(String remitBranch) {
    this.remitBranch = remitBranch;
  }

/**
	* 匯款人<br>
	* 
	* @return String
	*/
  public String getRemitter() {
    return this.remitter == null ? "" : this.remitter;
  }

/**
	* 匯款人<br>
	* 
  *
  * @param remitter 匯款人
	*/
  public void setRemitter(String remitter) {
    this.remitter = remitter;
  }

/**
	* 案號<br>
	* 
	* @return String
	*/
  public String getCaseNo() {
    return this.caseNo == null ? "" : this.caseNo;
  }

/**
	* 案號<br>
	* 
  *
  * @param caseNo 案號
	*/
  public void setCaseNo(String caseNo) {
    this.caseNo = caseNo;
  }

/**
	* 轉催收日<br>
	* ac處理
	* @return Integer
	*/
  public int getOverdueDate() {
    return StaticTool.bcToRoc(this.overdueDate);
  }

/**
	* 轉催收日<br>
	* ac處理
  *
  * @param overdueDate 轉催收日
  * @throws LogicException when Date Is Warn	*/
  public void setOverdueDate(int overdueDate) throws LogicException {
    this.overdueDate = StaticTool.rocToBc(overdueDate);
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
    return "ForeclosureFee [recordNo=" + recordNo + ", custNo=" + custNo + ", facmNo=" + facmNo + ", receiveDate=" + receiveDate + ", docDate=" + docDate + ", openAcDate=" + openAcDate
           + ", closeDate=" + closeDate + ", fee=" + fee + ", feeCode=" + feeCode + ", legalStaff=" + legalStaff + ", closeNo=" + closeNo + ", rmk=" + rmk
           + ", caseCode=" + caseCode + ", remitBranch=" + remitBranch + ", remitter=" + remitter + ", caseNo=" + caseNo + ", overdueDate=" + overdueDate + ", createDate=" + createDate
           + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
