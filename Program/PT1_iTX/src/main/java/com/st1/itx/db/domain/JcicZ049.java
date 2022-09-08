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
 * JcicZ049 債務清償方案法院認可資料檔案<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicZ049`")
public class JcicZ049 implements Serializable {


  @EmbeddedId
  private JcicZ049Id jcicZ049Id;

  // 交易代碼
  /* A:新增C:異動D:刪除 */
  @Column(name = "`TranKey`", length = 1)
  private String tranKey;

  // 報送單位代號
  /* 三位文數字 */
  @Column(name = "`SubmitKey`", length = 3, insertable = false, updatable = false)
  private String submitKey;

  // 債務人IDN
  @Column(name = "`CustId`", length = 10, insertable = false, updatable = false)
  private String custId;

  // 協商申請日
  @Column(name = "`RcDate`", insertable = false, updatable = false)
  private int rcDate = 0;

  // 案件進度
  /* 1:遞狀聲請2:法院裁定 */
  @Column(name = "`ClaimStatus`")
  private int claimStatus = 0;

  // 遞狀日
  @Column(name = "`ApplyDate`")
  private int applyDate = 0;

  // 承審法院代碼
  /* CdCode.CourtCode */
  @Column(name = "`CourtCode`", length = 3)
  private String courtCode;

  // 年度別
  @Column(name = "`Year`")
  private int year = 0;

  // 法院承審股別
  /* 2個中文字 */
  @Column(name = "`CourtDiv`", length = 6)
  private String courtDiv;

  // 法院案號
  @Column(name = "`CourtCaseNo`", length = 20)
  private String courtCaseNo;

  // 法院認可與否
  /* Y;N */
  @Column(name = "`Approve`", length = 1)
  private String approve;

  // 法院裁定日期
  @Column(name = "`ClaimDate`")
  private int claimDate = 0;

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


  public JcicZ049Id getJcicZ049Id() {
    return this.jcicZ049Id;
  }

  public void setJcicZ049Id(JcicZ049Id jcicZ049Id) {
    this.jcicZ049Id = jcicZ049Id;
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
	* 協商申請日<br>
	* 
	* @return Integer
	*/
  public int getRcDate() {
    return StaticTool.bcToRoc(this.rcDate);
  }

/**
	* 協商申請日<br>
	* 
  *
  * @param rcDate 協商申請日
  * @throws LogicException when Date Is Warn	*/
  public void setRcDate(int rcDate) throws LogicException {
    this.rcDate = StaticTool.rocToBc(rcDate);
  }

/**
	* 案件進度<br>
	* 1:遞狀聲請
2:法院裁定
	* @return Integer
	*/
  public int getClaimStatus() {
    return this.claimStatus;
  }

/**
	* 案件進度<br>
	* 1:遞狀聲請
2:法院裁定
  *
  * @param claimStatus 案件進度
	*/
  public void setClaimStatus(int claimStatus) {
    this.claimStatus = claimStatus;
  }

/**
	* 遞狀日<br>
	* 
	* @return Integer
	*/
  public int getApplyDate() {
    return StaticTool.bcToRoc(this.applyDate);
  }

/**
	* 遞狀日<br>
	* 
  *
  * @param applyDate 遞狀日
  * @throws LogicException when Date Is Warn	*/
  public void setApplyDate(int applyDate) throws LogicException {
    this.applyDate = StaticTool.rocToBc(applyDate);
  }

/**
	* 承審法院代碼<br>
	* CdCode.CourtCode
	* @return String
	*/
  public String getCourtCode() {
    return this.courtCode == null ? "" : this.courtCode;
  }

/**
	* 承審法院代碼<br>
	* CdCode.CourtCode
  *
  * @param courtCode 承審法院代碼
	*/
  public void setCourtCode(String courtCode) {
    this.courtCode = courtCode;
  }

/**
	* 年度別<br>
	* 
	* @return Integer
	*/
  public int getYear() {
    return this.year;
  }

/**
	* 年度別<br>
	* 
  *
  * @param year 年度別
	*/
  public void setYear(int year) {
    this.year = year;
  }

/**
	* 法院承審股別<br>
	* 2個中文字
	* @return String
	*/
  public String getCourtDiv() {
    return this.courtDiv == null ? "" : this.courtDiv;
  }

/**
	* 法院承審股別<br>
	* 2個中文字
  *
  * @param courtDiv 法院承審股別
	*/
  public void setCourtDiv(String courtDiv) {
    this.courtDiv = courtDiv;
  }

/**
	* 法院案號<br>
	* 
	* @return String
	*/
  public String getCourtCaseNo() {
    return this.courtCaseNo == null ? "" : this.courtCaseNo;
  }

/**
	* 法院案號<br>
	* 
  *
  * @param courtCaseNo 法院案號
	*/
  public void setCourtCaseNo(String courtCaseNo) {
    this.courtCaseNo = courtCaseNo;
  }

/**
	* 法院認可與否<br>
	* Y;N
	* @return String
	*/
  public String getApprove() {
    return this.approve == null ? "" : this.approve;
  }

/**
	* 法院認可與否<br>
	* Y;N
  *
  * @param approve 法院認可與否
	*/
  public void setApprove(String approve) {
    this.approve = approve;
  }

/**
	* 法院裁定日期<br>
	* 
	* @return Integer
	*/
  public int getClaimDate() {
    return StaticTool.bcToRoc(this.claimDate);
  }

/**
	* 法院裁定日期<br>
	* 
  *
  * @param claimDate 法院裁定日期
  * @throws LogicException when Date Is Warn	*/
  public void setClaimDate(int claimDate) throws LogicException {
    this.claimDate = StaticTool.rocToBc(claimDate);
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
    return "JcicZ049 [jcicZ049Id=" + jcicZ049Id + ", tranKey=" + tranKey + ", claimStatus=" + claimStatus + ", applyDate=" + applyDate
           + ", courtCode=" + courtCode + ", year=" + year + ", courtDiv=" + courtDiv + ", courtCaseNo=" + courtCaseNo + ", approve=" + approve + ", claimDate=" + claimDate
           + ", outJcicTxtDate=" + outJcicTxtDate + ", ukey=" + ukey + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo
           + ", actualFilingDate=" + actualFilingDate + ", actualFilingMark=" + actualFilingMark + "]";
  }
}
