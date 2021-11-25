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
 * NegTrans 債務協商交易檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`NegTrans`")
public class NegTrans implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -2284379722208849524L;

@EmbeddedId
  private NegTransId negTransId;

  // 會計日期
  @Column(name = "`AcDate`", insertable = false, updatable = false)
  private int acDate = 0;

  // 經辦
  @Column(name = "`TitaTlrNo`", length = 6, insertable = false, updatable = false)
  private String titaTlrNo;

  // 交易序號
  @Column(name = "`TitaTxtNo`", insertable = false, updatable = false)
  private int titaTxtNo = 0;

  // 戶號
  /* 保貸戶須建立客戶主檔 */
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 案件序號
  @Column(name = "`CaseSeq`")
  private int caseSeq = 0;

  // 入帳日期
  @Column(name = "`EntryDate`")
  private int entryDate = 0;

  // 交易狀態
  /* 0:未入帳1:待處理2:已入帳 */
  @Column(name = "`TxStatus`")
  private int txStatus = 0;

  // 交易別
  /* 0:正常-匯入款＋溢收款 &amp;gt;= 期款1:溢繳(預收多期)-匯入款 &amp;gt; 期款2:短繳-匯入款＋溢收款 &amp;lt; 期款3:提前還本-匯入款 &amp;gt;= 5期期款4:結清-匯入款＋溢收款 &amp;gt;=最後一期期款5:提前清償-匯入款＋溢收款 &amp;gt;= 剩餘期款9:未處理※入帳訂正需在撥付產擋前，依反向順序訂正 */
  @Column(name = "`TxKind`", length = 1)
  private String txKind;

  // 交易金額
  /* 暫收金額，轉入金額 */
  @Column(name = "`TxAmt`")
  private BigDecimal txAmt = new BigDecimal("0");

  // 本金餘額
  @Column(name = "`PrincipalBal`")
  private BigDecimal principalBal = new BigDecimal("0");

  // 退還金額
  /* 結清時才有， */
  @Column(name = "`ReturnAmt`")
  private BigDecimal returnAmt = new BigDecimal("0");

  // 新壽攤分
  /* 依債權分配比率計算 */
  @Column(name = "`SklShareAmt`")
  private BigDecimal sklShareAmt = new BigDecimal("0");

  // 撥付金額
  /* 交易金額-退還金額-新壽攤分 */
  @Column(name = "`ApprAmt`")
  private BigDecimal apprAmt = new BigDecimal("0");

  // 撥付製檔日
  /* L5707最大債權撥付產檔時寫入[L5074撥付製檔] */
  @Column(name = "`ExportDate`")
  private int exportDate = 0;

  // 撥付出帳日
  /* L5708最大債權撥付出帳時寫入[L5074撥付出帳] */
  @Column(name = "`ExportAcDate`")
  private int exportAcDate = 0;

  // 暫收抵繳金額
  /* 還款金額減暫收金額，為正時 */
  @Column(name = "`TempRepayAmt`")
  private BigDecimal tempRepayAmt = new BigDecimal("0");

  // 溢收抵繳金額
  /* 暫收金額減還款金額，為負時溢繳款=轉入溢收金額 - 溢收抵繳金額 */
  @Column(name = "`OverRepayAmt`")
  private BigDecimal overRepayAmt = new BigDecimal("0");

  // 本金金額
  /* 還款金額 = 本金+利息 */
  @Column(name = "`PrincipalAmt`")
  private BigDecimal principalAmt = new BigDecimal("0");

  // 利息金額
  @Column(name = "`InterestAmt`")
  private BigDecimal interestAmt = new BigDecimal("0");

  // 轉入溢收金額
  /* 暫收金額減還款金額，為正時 */
  @Column(name = "`OverAmt`")
  private BigDecimal overAmt = new BigDecimal("0");

  // 繳息起日
  /* 根據期數去計算 */
  @Column(name = "`IntStartDate`")
  private int intStartDate = 0;

  // 繳息迄日
  /* 根據期數去計算 */
  @Column(name = "`IntEndDate`")
  private int intEndDate = 0;

  // 還款期數
  @Column(name = "`RepayPeriod`")
  private int repayPeriod = 0;

  // 入帳還款日期
  /* L5702暫收入帳時寫入[L5074入帳還款] */
  @Column(name = "`RepayDate`")
  private int repayDate = 0;

  // 累溢繳款(交易前)
  @Column(name = "`OrgAccuOverAmt`")
  private BigDecimal orgAccuOverAmt = new BigDecimal("0");

  // 累溢繳款(交易後)
  @Column(name = "`AccuOverAmt`")
  private BigDecimal accuOverAmt = new BigDecimal("0");

  // 本次應還期數
  @Column(name = "`ShouldPayPeriod`")
  private int shouldPayPeriod = 0;

  // 期金
  /* NegMain的期金 */
  @Column(name = "`DueAmt`")
  private BigDecimal dueAmt = new BigDecimal("0");

  // 本次交易日
  /* 本次TxTemp */
  @Column(name = "`ThisEntdy`")
  private int thisEntdy = 0;

  // 本次分行別
  /* 本次TxTemp */
  @Column(name = "`ThisKinbr`", length = 4)
  private String thisKinbr;

  // 本次交易員代號
  /* 本次TxTemp */
  @Column(name = "`ThisTlrNo`", length = 6)
  private String thisTlrNo;

  // 本次交易序號
  /* 本次TxTemp */
  @Column(name = "`ThisTxtNo`", length = 8)
  private String thisTxtNo;

  // 本次序號
  /* 本次TxTemp */
  @Column(name = "`ThisSeqNo`", length = 30)
  private String thisSeqNo;

  // 上次交易日
  /* 上次TxTemp */
  @Column(name = "`LastEntdy`")
  private int lastEntdy = 0;

  // 上次分行別
  /* 上次TxTemp */
  @Column(name = "`LastKinbr`", length = 4)
  private String lastKinbr;

  // 上次交易員代號
  /* 上次TxTemp */
  @Column(name = "`LastTlrNo`", length = 6)
  private String lastTlrNo;

  // 上次交易序號
  /* 上次TxTemp */
  @Column(name = "`LastTxtNo`", length = 8)
  private String lastTxtNo;

  // 上次序號
  /* 上次TxTemp */
  @Column(name = "`LastSeqNo`", length = 30)
  private String lastSeqNo;

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


  public NegTransId getNegTransId() {
    return this.negTransId;
  }

  public void setNegTransId(NegTransId negTransId) {
    this.negTransId = negTransId;
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
	* 經辦<br>
	* 
	* @return String
	*/
  public String getTitaTlrNo() {
    return this.titaTlrNo == null ? "" : this.titaTlrNo;
  }

/**
	* 經辦<br>
	* 
  *
  * @param titaTlrNo 經辦
	*/
  public void setTitaTlrNo(String titaTlrNo) {
    this.titaTlrNo = titaTlrNo;
  }

/**
	* 交易序號<br>
	* 
	* @return Integer
	*/
  public int getTitaTxtNo() {
    return this.titaTxtNo;
  }

/**
	* 交易序號<br>
	* 
  *
  * @param titaTxtNo 交易序號
	*/
  public void setTitaTxtNo(int titaTxtNo) {
    this.titaTxtNo = titaTxtNo;
  }

/**
	* 戶號<br>
	* 保貸戶須建立客戶主檔
	* @return Integer
	*/
  public int getCustNo() {
    return this.custNo;
  }

/**
	* 戶號<br>
	* 保貸戶須建立客戶主檔
  *
  * @param custNo 戶號
	*/
  public void setCustNo(int custNo) {
    this.custNo = custNo;
  }

/**
	* 案件序號<br>
	* 
	* @return Integer
	*/
  public int getCaseSeq() {
    return this.caseSeq;
  }

/**
	* 案件序號<br>
	* 
  *
  * @param caseSeq 案件序號
	*/
  public void setCaseSeq(int caseSeq) {
    this.caseSeq = caseSeq;
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
	* 交易狀態<br>
	* 0:未入帳
1:待處理
2:已入帳
	* @return Integer
	*/
  public int getTxStatus() {
    return this.txStatus;
  }

/**
	* 交易狀態<br>
	* 0:未入帳
1:待處理
2:已入帳
  *
  * @param txStatus 交易狀態
	*/
  public void setTxStatus(int txStatus) {
    this.txStatus = txStatus;
  }

/**
	* 交易別<br>
	* 0:正常-匯入款＋溢收款 &amp;gt;= 期款
1:溢繳(預收多期)-匯入款 &amp;gt; 期款
2:短繳-匯入款＋溢收款 &amp;lt; 期款
3:提前還本-匯入款 &amp;gt;= 5期期款
4:結清-匯入款＋溢收款 &amp;gt;=最後一期期款
5:提前清償-匯入款＋溢收款 &amp;gt;= 剩餘期款
9:未處理
※入帳訂正需在撥付產擋前，依反向順序訂正
	* @return String
	*/
  public String getTxKind() {
    return this.txKind == null ? "" : this.txKind;
  }

/**
	* 交易別<br>
	* 0:正常-匯入款＋溢收款 &amp;gt;= 期款
1:溢繳(預收多期)-匯入款 &amp;gt; 期款
2:短繳-匯入款＋溢收款 &amp;lt; 期款
3:提前還本-匯入款 &amp;gt;= 5期期款
4:結清-匯入款＋溢收款 &amp;gt;=最後一期期款
5:提前清償-匯入款＋溢收款 &amp;gt;= 剩餘期款
9:未處理
※入帳訂正需在撥付產擋前，依反向順序訂正
  *
  * @param txKind 交易別
	*/
  public void setTxKind(String txKind) {
    this.txKind = txKind;
  }

/**
	* 交易金額<br>
	* 暫收金額，轉入金額
	* @return BigDecimal
	*/
  public BigDecimal getTxAmt() {
    return this.txAmt;
  }

/**
	* 交易金額<br>
	* 暫收金額，轉入金額
  *
  * @param txAmt 交易金額
	*/
  public void setTxAmt(BigDecimal txAmt) {
    this.txAmt = txAmt;
  }

/**
	* 本金餘額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getPrincipalBal() {
    return this.principalBal;
  }

/**
	* 本金餘額<br>
	* 
  *
  * @param principalBal 本金餘額
	*/
  public void setPrincipalBal(BigDecimal principalBal) {
    this.principalBal = principalBal;
  }

/**
	* 退還金額<br>
	* 結清時才有，
	* @return BigDecimal
	*/
  public BigDecimal getReturnAmt() {
    return this.returnAmt;
  }

/**
	* 退還金額<br>
	* 結清時才有，
  *
  * @param returnAmt 退還金額
	*/
  public void setReturnAmt(BigDecimal returnAmt) {
    this.returnAmt = returnAmt;
  }

/**
	* 新壽攤分<br>
	* 依債權分配比率計算
	* @return BigDecimal
	*/
  public BigDecimal getSklShareAmt() {
    return this.sklShareAmt;
  }

/**
	* 新壽攤分<br>
	* 依債權分配比率計算
  *
  * @param sklShareAmt 新壽攤分
	*/
  public void setSklShareAmt(BigDecimal sklShareAmt) {
    this.sklShareAmt = sklShareAmt;
  }

/**
	* 撥付金額<br>
	* 交易金額-退還金額-新壽攤分
	* @return BigDecimal
	*/
  public BigDecimal getApprAmt() {
    return this.apprAmt;
  }

/**
	* 撥付金額<br>
	* 交易金額-退還金額-新壽攤分
  *
  * @param apprAmt 撥付金額
	*/
  public void setApprAmt(BigDecimal apprAmt) {
    this.apprAmt = apprAmt;
  }

/**
	* 撥付製檔日<br>
	* L5707最大債權撥付產檔時寫入[L5074撥付製檔]
	* @return Integer
	*/
  public int getExportDate() {
    return StaticTool.bcToRoc(this.exportDate);
  }

/**
	* 撥付製檔日<br>
	* L5707最大債權撥付產檔時寫入[L5074撥付製檔]
  *
  * @param exportDate 撥付製檔日
  * @throws LogicException when Date Is Warn	*/
  public void setExportDate(int exportDate) throws LogicException {
    this.exportDate = StaticTool.rocToBc(exportDate);
  }

/**
	* 撥付出帳日<br>
	* L5708最大債權撥付出帳時寫入[L5074撥付出帳]
	* @return Integer
	*/
  public int getExportAcDate() {
    return StaticTool.bcToRoc(this.exportAcDate);
  }

/**
	* 撥付出帳日<br>
	* L5708最大債權撥付出帳時寫入[L5074撥付出帳]
  *
  * @param exportAcDate 撥付出帳日
  * @throws LogicException when Date Is Warn	*/
  public void setExportAcDate(int exportAcDate) throws LogicException {
    this.exportAcDate = StaticTool.rocToBc(exportAcDate);
  }

/**
	* 暫收抵繳金額<br>
	* 還款金額減暫收金額，為正時
	* @return BigDecimal
	*/
  public BigDecimal getTempRepayAmt() {
    return this.tempRepayAmt;
  }

/**
	* 暫收抵繳金額<br>
	* 還款金額減暫收金額，為正時
  *
  * @param tempRepayAmt 暫收抵繳金額
	*/
  public void setTempRepayAmt(BigDecimal tempRepayAmt) {
    this.tempRepayAmt = tempRepayAmt;
  }

/**
	* 溢收抵繳金額<br>
	* 暫收金額減還款金額，為負時
溢繳款=轉入溢收金額 - 溢收抵繳金額
	* @return BigDecimal
	*/
  public BigDecimal getOverRepayAmt() {
    return this.overRepayAmt;
  }

/**
	* 溢收抵繳金額<br>
	* 暫收金額減還款金額，為負時
溢繳款=轉入溢收金額 - 溢收抵繳金額
  *
  * @param overRepayAmt 溢收抵繳金額
	*/
  public void setOverRepayAmt(BigDecimal overRepayAmt) {
    this.overRepayAmt = overRepayAmt;
  }

/**
	* 本金金額<br>
	* 還款金額 = 本金+利息
	* @return BigDecimal
	*/
  public BigDecimal getPrincipalAmt() {
    return this.principalAmt;
  }

/**
	* 本金金額<br>
	* 還款金額 = 本金+利息
  *
  * @param principalAmt 本金金額
	*/
  public void setPrincipalAmt(BigDecimal principalAmt) {
    this.principalAmt = principalAmt;
  }

/**
	* 利息金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getInterestAmt() {
    return this.interestAmt;
  }

/**
	* 利息金額<br>
	* 
  *
  * @param interestAmt 利息金額
	*/
  public void setInterestAmt(BigDecimal interestAmt) {
    this.interestAmt = interestAmt;
  }

/**
	* 轉入溢收金額<br>
	* 暫收金額減還款金額，為正時
	* @return BigDecimal
	*/
  public BigDecimal getOverAmt() {
    return this.overAmt;
  }

/**
	* 轉入溢收金額<br>
	* 暫收金額減還款金額，為正時
  *
  * @param overAmt 轉入溢收金額
	*/
  public void setOverAmt(BigDecimal overAmt) {
    this.overAmt = overAmt;
  }

/**
	* 繳息起日<br>
	* 根據期數去計算
	* @return Integer
	*/
  public int getIntStartDate() {
    return StaticTool.bcToRoc(this.intStartDate);
  }

/**
	* 繳息起日<br>
	* 根據期數去計算
  *
  * @param intStartDate 繳息起日
  * @throws LogicException when Date Is Warn	*/
  public void setIntStartDate(int intStartDate) throws LogicException {
    this.intStartDate = StaticTool.rocToBc(intStartDate);
  }

/**
	* 繳息迄日<br>
	* 根據期數去計算
	* @return Integer
	*/
  public int getIntEndDate() {
    return StaticTool.bcToRoc(this.intEndDate);
  }

/**
	* 繳息迄日<br>
	* 根據期數去計算
  *
  * @param intEndDate 繳息迄日
  * @throws LogicException when Date Is Warn	*/
  public void setIntEndDate(int intEndDate) throws LogicException {
    this.intEndDate = StaticTool.rocToBc(intEndDate);
  }

/**
	* 還款期數<br>
	* 
	* @return Integer
	*/
  public int getRepayPeriod() {
    return this.repayPeriod;
  }

/**
	* 還款期數<br>
	* 
  *
  * @param repayPeriod 還款期數
	*/
  public void setRepayPeriod(int repayPeriod) {
    this.repayPeriod = repayPeriod;
  }

/**
	* 入帳還款日期<br>
	* L5702暫收入帳時寫入[L5074入帳還款]
	* @return Integer
	*/
  public int getRepayDate() {
    return StaticTool.bcToRoc(this.repayDate);
  }

/**
	* 入帳還款日期<br>
	* L5702暫收入帳時寫入[L5074入帳還款]
  *
  * @param repayDate 入帳還款日期
  * @throws LogicException when Date Is Warn	*/
  public void setRepayDate(int repayDate) throws LogicException {
    this.repayDate = StaticTool.rocToBc(repayDate);
  }

/**
	* 累溢繳款(交易前)<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getOrgAccuOverAmt() {
    return this.orgAccuOverAmt;
  }

/**
	* 累溢繳款(交易前)<br>
	* 
  *
  * @param orgAccuOverAmt 累溢繳款(交易前)
	*/
  public void setOrgAccuOverAmt(BigDecimal orgAccuOverAmt) {
    this.orgAccuOverAmt = orgAccuOverAmt;
  }

/**
	* 累溢繳款(交易後)<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getAccuOverAmt() {
    return this.accuOverAmt;
  }

/**
	* 累溢繳款(交易後)<br>
	* 
  *
  * @param accuOverAmt 累溢繳款(交易後)
	*/
  public void setAccuOverAmt(BigDecimal accuOverAmt) {
    this.accuOverAmt = accuOverAmt;
  }

/**
	* 本次應還期數<br>
	* 
	* @return Integer
	*/
  public int getShouldPayPeriod() {
    return this.shouldPayPeriod;
  }

/**
	* 本次應還期數<br>
	* 
  *
  * @param shouldPayPeriod 本次應還期數
	*/
  public void setShouldPayPeriod(int shouldPayPeriod) {
    this.shouldPayPeriod = shouldPayPeriod;
  }

/**
	* 期金<br>
	* NegMain的期金
	* @return BigDecimal
	*/
  public BigDecimal getDueAmt() {
    return this.dueAmt;
  }

/**
	* 期金<br>
	* NegMain的期金
  *
  * @param dueAmt 期金
	*/
  public void setDueAmt(BigDecimal dueAmt) {
    this.dueAmt = dueAmt;
  }

/**
	* 本次交易日<br>
	* 本次TxTemp
	* @return Integer
	*/
  public int getThisEntdy() {
    return StaticTool.bcToRoc(this.thisEntdy);
  }

/**
	* 本次交易日<br>
	* 本次TxTemp
  *
  * @param thisEntdy 本次交易日
  * @throws LogicException when Date Is Warn	*/
  public void setThisEntdy(int thisEntdy) throws LogicException {
    this.thisEntdy = StaticTool.rocToBc(thisEntdy);
  }

/**
	* 本次分行別<br>
	* 本次TxTemp
	* @return String
	*/
  public String getThisKinbr() {
    return this.thisKinbr == null ? "" : this.thisKinbr;
  }

/**
	* 本次分行別<br>
	* 本次TxTemp
  *
  * @param thisKinbr 本次分行別
	*/
  public void setThisKinbr(String thisKinbr) {
    this.thisKinbr = thisKinbr;
  }

/**
	* 本次交易員代號<br>
	* 本次TxTemp
	* @return String
	*/
  public String getThisTlrNo() {
    return this.thisTlrNo == null ? "" : this.thisTlrNo;
  }

/**
	* 本次交易員代號<br>
	* 本次TxTemp
  *
  * @param thisTlrNo 本次交易員代號
	*/
  public void setThisTlrNo(String thisTlrNo) {
    this.thisTlrNo = thisTlrNo;
  }

/**
	* 本次交易序號<br>
	* 本次TxTemp
	* @return String
	*/
  public String getThisTxtNo() {
    return this.thisTxtNo == null ? "" : this.thisTxtNo;
  }

/**
	* 本次交易序號<br>
	* 本次TxTemp
  *
  * @param thisTxtNo 本次交易序號
	*/
  public void setThisTxtNo(String thisTxtNo) {
    this.thisTxtNo = thisTxtNo;
  }

/**
	* 本次序號<br>
	* 本次TxTemp
	* @return String
	*/
  public String getThisSeqNo() {
    return this.thisSeqNo == null ? "" : this.thisSeqNo;
  }

/**
	* 本次序號<br>
	* 本次TxTemp
  *
  * @param thisSeqNo 本次序號
	*/
  public void setThisSeqNo(String thisSeqNo) {
    this.thisSeqNo = thisSeqNo;
  }

/**
	* 上次交易日<br>
	* 上次TxTemp
	* @return Integer
	*/
  public int getLastEntdy() {
    return StaticTool.bcToRoc(this.lastEntdy);
  }

/**
	* 上次交易日<br>
	* 上次TxTemp
  *
  * @param lastEntdy 上次交易日
  * @throws LogicException when Date Is Warn	*/
  public void setLastEntdy(int lastEntdy) throws LogicException {
    this.lastEntdy = StaticTool.rocToBc(lastEntdy);
  }

/**
	* 上次分行別<br>
	* 上次TxTemp
	* @return String
	*/
  public String getLastKinbr() {
    return this.lastKinbr == null ? "" : this.lastKinbr;
  }

/**
	* 上次分行別<br>
	* 上次TxTemp
  *
  * @param lastKinbr 上次分行別
	*/
  public void setLastKinbr(String lastKinbr) {
    this.lastKinbr = lastKinbr;
  }

/**
	* 上次交易員代號<br>
	* 上次TxTemp
	* @return String
	*/
  public String getLastTlrNo() {
    return this.lastTlrNo == null ? "" : this.lastTlrNo;
  }

/**
	* 上次交易員代號<br>
	* 上次TxTemp
  *
  * @param lastTlrNo 上次交易員代號
	*/
  public void setLastTlrNo(String lastTlrNo) {
    this.lastTlrNo = lastTlrNo;
  }

/**
	* 上次交易序號<br>
	* 上次TxTemp
	* @return String
	*/
  public String getLastTxtNo() {
    return this.lastTxtNo == null ? "" : this.lastTxtNo;
  }

/**
	* 上次交易序號<br>
	* 上次TxTemp
  *
  * @param lastTxtNo 上次交易序號
	*/
  public void setLastTxtNo(String lastTxtNo) {
    this.lastTxtNo = lastTxtNo;
  }

/**
	* 上次序號<br>
	* 上次TxTemp
	* @return String
	*/
  public String getLastSeqNo() {
    return this.lastSeqNo == null ? "" : this.lastSeqNo;
  }

/**
	* 上次序號<br>
	* 上次TxTemp
  *
  * @param lastSeqNo 上次序號
	*/
  public void setLastSeqNo(String lastSeqNo) {
    this.lastSeqNo = lastSeqNo;
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
    return "NegTrans [negTransId=" + negTransId + ", custNo=" + custNo + ", caseSeq=" + caseSeq + ", entryDate=" + entryDate
           + ", txStatus=" + txStatus + ", txKind=" + txKind + ", txAmt=" + txAmt + ", principalBal=" + principalBal + ", returnAmt=" + returnAmt + ", sklShareAmt=" + sklShareAmt
           + ", apprAmt=" + apprAmt + ", exportDate=" + exportDate + ", exportAcDate=" + exportAcDate + ", tempRepayAmt=" + tempRepayAmt + ", overRepayAmt=" + overRepayAmt + ", principalAmt=" + principalAmt
           + ", interestAmt=" + interestAmt + ", overAmt=" + overAmt + ", intStartDate=" + intStartDate + ", intEndDate=" + intEndDate + ", repayPeriod=" + repayPeriod + ", repayDate=" + repayDate
           + ", orgAccuOverAmt=" + orgAccuOverAmt + ", accuOverAmt=" + accuOverAmt + ", shouldPayPeriod=" + shouldPayPeriod + ", dueAmt=" + dueAmt + ", thisEntdy=" + thisEntdy + ", thisKinbr=" + thisKinbr
           + ", thisTlrNo=" + thisTlrNo + ", thisTxtNo=" + thisTxtNo + ", thisSeqNo=" + thisSeqNo + ", lastEntdy=" + lastEntdy + ", lastKinbr=" + lastKinbr + ", lastTlrNo=" + lastTlrNo
           + ", lastTxtNo=" + lastTxtNo + ", lastSeqNo=" + lastSeqNo + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo
           + "]";
  }
}
