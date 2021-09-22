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
 * EmpDeductMedia 員工扣薪媒體檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`EmpDeductMedia`")
public class EmpDeductMedia implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -7459562540037063451L;

@EmbeddedId
  private EmpDeductMediaId empDeductMediaId;

  // 媒體日期
  @Column(name = "`MediaDate`", insertable = false, updatable = false)
  private int mediaDate = 0;

  // 媒體別
  /* 4:15日5:非15日 */
  @Column(name = "`MediaKind`", length = 1, insertable = false, updatable = false)
  private String mediaKind;

  // 媒體序號
  @Column(name = "`MediaSeq`", insertable = false, updatable = false)
  private int mediaSeq = 0;

  // 戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 還款類別
  /* CdCode:RepayType1.期款2.部分償還3.結案4.帳管費5.火險費6.契變手續費7.法務費9.其他11.債協匯入款(虛擬帳號為9510500NNNNNNN) */
  @Column(name = "`RepayCode`")
  private int repayCode = 0;

  // 扣款代碼
  /* CdCode:PerfRepayCode1:扣薪件;2:特約件;3:滯繳件;4:人事特約件;5:房貸扣薪件 */
  @Column(name = "`PerfRepayCode`")
  private int perfRepayCode = 0;

  // 還款金額(扣款金額)
  @Column(name = "`RepayAmt`")
  private BigDecimal repayAmt = new BigDecimal("0");

  // 業績年月
  @Column(name = "`PerfMonth`")
  private int perfMonth = 0;

  // 流程別
  @Column(name = "`FlowCode`", length = 1)
  private String flowCode;

  // 單位代號
  @Column(name = "`UnitCode`", length = 6)
  private String unitCode;

  // 身分證統一編號
  @Column(name = "`CustId`", length = 10)
  private String custId;

  // 入帳日期
  @Column(name = "`EntryDate`")
  private int entryDate = 0;

  // 交易金額(實扣金額)
  @Column(name = "`TxAmt`")
  private BigDecimal txAmt = new BigDecimal("0");

  // 失敗原因
  /* 16:扣款失敗17:扣款不足….. */
  @Column(name = "`ErrorCode`", length = 2)
  private String errorCode;

  // 科目
  @Column(name = "`AcctCode`", length = 3)
  private String acctCode;

  // 會計日期
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


  public EmpDeductMediaId getEmpDeductMediaId() {
    return this.empDeductMediaId;
  }

  public void setEmpDeductMediaId(EmpDeductMediaId empDeductMediaId) {
    this.empDeductMediaId = empDeductMediaId;
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
	* 媒體別<br>
	* 4:15日
5:非15日
	* @return String
	*/
  public String getMediaKind() {
    return this.mediaKind == null ? "" : this.mediaKind;
  }

/**
	* 媒體別<br>
	* 4:15日
5:非15日
  *
  * @param mediaKind 媒體別
	*/
  public void setMediaKind(String mediaKind) {
    this.mediaKind = mediaKind;
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
	* 還款類別<br>
	* CdCode:RepayType
1.期款
2.部分償還
3.結案
4.帳管費
5.火險費
6.契變手續費
7.法務費
9.其他
11.債協匯入款(虛擬帳號為9510500NNNNNNN)
	* @return Integer
	*/
  public int getRepayCode() {
    return this.repayCode;
  }

/**
	* 還款類別<br>
	* CdCode:RepayType
1.期款
2.部分償還
3.結案
4.帳管費
5.火險費
6.契變手續費
7.法務費
9.其他
11.債協匯入款(虛擬帳號為9510500NNNNNNN)
  *
  * @param repayCode 還款類別
	*/
  public void setRepayCode(int repayCode) {
    this.repayCode = repayCode;
  }

/**
	* 扣款代碼<br>
	* CdCode:PerfRepayCode
1:扣薪件;2:特約件;3:滯繳件;4:人事特約件;5:房貸扣薪件
	* @return Integer
	*/
  public int getPerfRepayCode() {
    return this.perfRepayCode;
  }

/**
	* 扣款代碼<br>
	* CdCode:PerfRepayCode
1:扣薪件;2:特約件;3:滯繳件;4:人事特約件;5:房貸扣薪件
  *
  * @param perfRepayCode 扣款代碼
	*/
  public void setPerfRepayCode(int perfRepayCode) {
    this.perfRepayCode = perfRepayCode;
  }

/**
	* 還款金額(扣款金額)<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getRepayAmt() {
    return this.repayAmt;
  }

/**
	* 還款金額(扣款金額)<br>
	* 
  *
  * @param repayAmt 還款金額(扣款金額)
	*/
  public void setRepayAmt(BigDecimal repayAmt) {
    this.repayAmt = repayAmt;
  }

/**
	* 業績年月<br>
	* 
	* @return Integer
	*/
  public int getPerfMonth() {
    return this.perfMonth;
  }

/**
	* 業績年月<br>
	* 
  *
  * @param perfMonth 業績年月
	*/
  public void setPerfMonth(int perfMonth) {
    this.perfMonth = perfMonth;
  }

/**
	* 流程別<br>
	* 
	* @return String
	*/
  public String getFlowCode() {
    return this.flowCode == null ? "" : this.flowCode;
  }

/**
	* 流程別<br>
	* 
  *
  * @param flowCode 流程別
	*/
  public void setFlowCode(String flowCode) {
    this.flowCode = flowCode;
  }

/**
	* 單位代號<br>
	* 
	* @return String
	*/
  public String getUnitCode() {
    return this.unitCode == null ? "" : this.unitCode;
  }

/**
	* 單位代號<br>
	* 
  *
  * @param unitCode 單位代號
	*/
  public void setUnitCode(String unitCode) {
    this.unitCode = unitCode;
  }

/**
	* 身分證統一編號<br>
	* 
	* @return String
	*/
  public String getCustId() {
    return this.custId == null ? "" : this.custId;
  }

/**
	* 身分證統一編號<br>
	* 
  *
  * @param custId 身分證統一編號
	*/
  public void setCustId(String custId) {
    this.custId = custId;
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
	* 交易金額(實扣金額)<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getTxAmt() {
    return this.txAmt;
  }

/**
	* 交易金額(實扣金額)<br>
	* 
  *
  * @param txAmt 交易金額(實扣金額)
	*/
  public void setTxAmt(BigDecimal txAmt) {
    this.txAmt = txAmt;
  }

/**
	* 失敗原因<br>
	* 16:扣款失敗17:扣款不足…..
	* @return String
	*/
  public String getErrorCode() {
    return this.errorCode == null ? "" : this.errorCode;
  }

/**
	* 失敗原因<br>
	* 16:扣款失敗17:扣款不足…..
  *
  * @param errorCode 失敗原因
	*/
  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }

/**
	* 科目<br>
	* 
	* @return String
	*/
  public String getAcctCode() {
    return this.acctCode == null ? "" : this.acctCode;
  }

/**
	* 科目<br>
	* 
  *
  * @param acctCode 科目
	*/
  public void setAcctCode(String acctCode) {
    this.acctCode = acctCode;
  }

/**
	* 會計日期<br>
	* 
	* @return Integer
	*/
  public int getAcDate() {
    return StaticTool.bcToRoc(this.acDate);
  }

/**
	* 會計日期<br>
	* 
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


  @Override
  public String toString() {
    return "EmpDeductMedia [empDeductMediaId=" + empDeductMediaId + ", custNo=" + custNo + ", repayCode=" + repayCode + ", perfRepayCode=" + perfRepayCode
           + ", repayAmt=" + repayAmt + ", perfMonth=" + perfMonth + ", flowCode=" + flowCode + ", unitCode=" + unitCode + ", custId=" + custId + ", entryDate=" + entryDate
           + ", txAmt=" + txAmt + ", errorCode=" + errorCode + ", acctCode=" + acctCode + ", acDate=" + acDate + ", batchNo=" + batchNo + ", detailSeq=" + detailSeq
           + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
