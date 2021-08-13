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
 * Ias39LoanCommit IAS39放款承諾明細檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`Ias39LoanCommit`")
public class Ias39LoanCommit implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 3988979574620856902L;

@EmbeddedId
  private Ias39LoanCommitId ias39LoanCommitId;

  // 年月份
  @Column(name = "`DataYm`", insertable = false, updatable = false)
  private int dataYm = 0;

  // 戶號
  @Column(name = "`CustNo`", insertable = false, updatable = false)
  private int custNo = 0;

  // 額度編號
  @Column(name = "`FacmNo`", insertable = false, updatable = false)
  private int facmNo = 0;

  // 核准號碼
  @Column(name = "`ApplNo`", insertable = false, updatable = false)
  private int applNo = 0;

  // 核准日期
  /* 【對保日期】，若為空值時則取【准駁日期】 */
  @Column(name = "`ApproveDate`")
  private int approveDate = 0;

  // 初貸日期
  @Column(name = "`FirstDrawdownDate`")
  private int firstDrawdownDate = 0;

  // 到期日
  @Column(name = "`MaturityDate`")
  private int maturityDate = 0;

  // 貸款期間年
  @Column(name = "`LoanTermYy`")
  private int loanTermYy = 0;

  // 貸款期間月
  @Column(name = "`LoanTermMm`")
  private int loanTermMm = 0;

  // 貸款期間日
  @Column(name = "`LoanTermDd`")
  private int loanTermDd = 0;

  // 動支期限
  @Column(name = "`UtilDeadline`")
  private int utilDeadline = 0;

  // 循環動用期限
  @Column(name = "`RecycleDeadline`")
  private int recycleDeadline = 0;

  // 核准額度
  @Column(name = "`LineAmt`")
  private BigDecimal lineAmt = new BigDecimal("0");

  // 放款餘額
  @Column(name = "`UtilBal`")
  private BigDecimal utilBal = new BigDecimal("0");

  // 可動用餘額
  /* 當【可循環動用】=1且【循環動用期限】&amp;gt;=月底日→【可動用餘額】=【核准額度】-【放款餘額】當【可循環動用】=0且【動支期限】&amp;gt;=月底日→【可動用餘額】=【核准額度】-【已用額度】 */
  @Column(name = "`AvblBal`")
  private BigDecimal avblBal = new BigDecimal("0");

  // 該筆額度是否可循環動用
  /* 0:非循環動用  1:循環動用 */
  @Column(name = "`RecycleCode`")
  private int recycleCode = 0;

  // 該筆額度是否為不可撤銷
  /* 0:可撤銷  1:不可撤銷 */
  @Column(name = "`IrrevocableFlag`")
  private int irrevocableFlag = 0;

  // 帳冊別
  /* 000:全公司 */
  @Column(name = "`AcBookCode`", length = 3)
  private String acBookCode;

  // 區隔帳冊
  /* 00A:統帳冊                           201:利變年金帳冊 */
  @Column(name = "`AcSubBookCode`", length = 3)
  private String acSubBookCode;

  // 信用風險轉換係數
  /* 以【貸款區間】判斷，轉換成”月數”，不足1個月者視為1個月；當【貸款期間起(月)】&amp;lt;=月數&amp;lt;=【貸款期間迄(月)】，取對應的【風險轉換係數(LCTCCF)】 */
  @Column(name = "`Ccf`")
  private BigDecimal ccf = new BigDecimal("0");

  // 表外曝險金額
  /* 可動用餘額 * 信用風險轉換係數 */
  @Column(name = "`ExpLimitAmt`")
  private BigDecimal expLimitAmt = new BigDecimal("0");

  // 借方：備忘分錄會計科目
  @Column(name = "`DbAcNoCode`", length = 11)
  private String dbAcNoCode;

  // 貸方：備忘分錄會計科目
  @Column(name = "`CrAcNoCode`", length = 11)
  private String crAcNoCode;

  // 已核撥記號
  /* 0:未核撥  1:已核撥 */
  @Column(name = "`DrawdownFg`")
  private int drawdownFg = 0;

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


  public Ias39LoanCommitId getIas39LoanCommitId() {
    return this.ias39LoanCommitId;
  }

  public void setIas39LoanCommitId(Ias39LoanCommitId ias39LoanCommitId) {
    this.ias39LoanCommitId = ias39LoanCommitId;
  }

/**
	* 年月份<br>
	* 
	* @return Integer
	*/
  public int getDataYm() {
    return this.dataYm;
  }

/**
	* 年月份<br>
	* 
  *
  * @param dataYm 年月份
	*/
  public void setDataYm(int dataYm) {
    this.dataYm = dataYm;
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
	* 核准號碼<br>
	* 
	* @return Integer
	*/
  public int getApplNo() {
    return this.applNo;
  }

/**
	* 核准號碼<br>
	* 
  *
  * @param applNo 核准號碼
	*/
  public void setApplNo(int applNo) {
    this.applNo = applNo;
  }

/**
	* 核准日期<br>
	* 【對保日期】，若為空值時則取【准駁日期】
	* @return Integer
	*/
  public int getApproveDate() {
    return StaticTool.bcToRoc(this.approveDate);
  }

/**
	* 核准日期<br>
	* 【對保日期】，若為空值時則取【准駁日期】
  *
  * @param approveDate 核准日期
  * @throws LogicException when Date Is Warn	*/
  public void setApproveDate(int approveDate) throws LogicException {
    this.approveDate = StaticTool.rocToBc(approveDate);
  }

/**
	* 初貸日期<br>
	* 
	* @return Integer
	*/
  public int getFirstDrawdownDate() {
    return StaticTool.bcToRoc(this.firstDrawdownDate);
  }

/**
	* 初貸日期<br>
	* 
  *
  * @param firstDrawdownDate 初貸日期
  * @throws LogicException when Date Is Warn	*/
  public void setFirstDrawdownDate(int firstDrawdownDate) throws LogicException {
    this.firstDrawdownDate = StaticTool.rocToBc(firstDrawdownDate);
  }

/**
	* 到期日<br>
	* 
	* @return Integer
	*/
  public int getMaturityDate() {
    return StaticTool.bcToRoc(this.maturityDate);
  }

/**
	* 到期日<br>
	* 
  *
  * @param maturityDate 到期日
  * @throws LogicException when Date Is Warn	*/
  public void setMaturityDate(int maturityDate) throws LogicException {
    this.maturityDate = StaticTool.rocToBc(maturityDate);
  }

/**
	* 貸款期間年<br>
	* 
	* @return Integer
	*/
  public int getLoanTermYy() {
    return this.loanTermYy;
  }

/**
	* 貸款期間年<br>
	* 
  *
  * @param loanTermYy 貸款期間年
	*/
  public void setLoanTermYy(int loanTermYy) {
    this.loanTermYy = loanTermYy;
  }

/**
	* 貸款期間月<br>
	* 
	* @return Integer
	*/
  public int getLoanTermMm() {
    return this.loanTermMm;
  }

/**
	* 貸款期間月<br>
	* 
  *
  * @param loanTermMm 貸款期間月
	*/
  public void setLoanTermMm(int loanTermMm) {
    this.loanTermMm = loanTermMm;
  }

/**
	* 貸款期間日<br>
	* 
	* @return Integer
	*/
  public int getLoanTermDd() {
    return this.loanTermDd;
  }

/**
	* 貸款期間日<br>
	* 
  *
  * @param loanTermDd 貸款期間日
	*/
  public void setLoanTermDd(int loanTermDd) {
    this.loanTermDd = loanTermDd;
  }

/**
	* 動支期限<br>
	* 
	* @return Integer
	*/
  public int getUtilDeadline() {
    return StaticTool.bcToRoc(this.utilDeadline);
  }

/**
	* 動支期限<br>
	* 
  *
  * @param utilDeadline 動支期限
  * @throws LogicException when Date Is Warn	*/
  public void setUtilDeadline(int utilDeadline) throws LogicException {
    this.utilDeadline = StaticTool.rocToBc(utilDeadline);
  }

/**
	* 循環動用期限<br>
	* 
	* @return Integer
	*/
  public int getRecycleDeadline() {
    return StaticTool.bcToRoc(this.recycleDeadline);
  }

/**
	* 循環動用期限<br>
	* 
  *
  * @param recycleDeadline 循環動用期限
  * @throws LogicException when Date Is Warn	*/
  public void setRecycleDeadline(int recycleDeadline) throws LogicException {
    this.recycleDeadline = StaticTool.rocToBc(recycleDeadline);
  }

/**
	* 核准額度<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLineAmt() {
    return this.lineAmt;
  }

/**
	* 核准額度<br>
	* 
  *
  * @param lineAmt 核准額度
	*/
  public void setLineAmt(BigDecimal lineAmt) {
    this.lineAmt = lineAmt;
  }

/**
	* 放款餘額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getUtilBal() {
    return this.utilBal;
  }

/**
	* 放款餘額<br>
	* 
  *
  * @param utilBal 放款餘額
	*/
  public void setUtilBal(BigDecimal utilBal) {
    this.utilBal = utilBal;
  }

/**
	* 可動用餘額<br>
	* 當【可循環動用】=1且【循環動用期限】&amp;gt;=月底日→【可動用餘額】=【核准額度】-【放款餘額】

當【可循環動用】=0且【動支期限】&amp;gt;=月底日→【可動用餘額】=【核准額度】-【已用額度】
	* @return BigDecimal
	*/
  public BigDecimal getAvblBal() {
    return this.avblBal;
  }

/**
	* 可動用餘額<br>
	* 當【可循環動用】=1且【循環動用期限】&amp;gt;=月底日→【可動用餘額】=【核准額度】-【放款餘額】

當【可循環動用】=0且【動支期限】&amp;gt;=月底日→【可動用餘額】=【核准額度】-【已用額度】
  *
  * @param avblBal 可動用餘額
	*/
  public void setAvblBal(BigDecimal avblBal) {
    this.avblBal = avblBal;
  }

/**
	* 該筆額度是否可循環動用<br>
	* 0:非循環動用  
1:循環動用
	* @return Integer
	*/
  public int getRecycleCode() {
    return this.recycleCode;
  }

/**
	* 該筆額度是否可循環動用<br>
	* 0:非循環動用  
1:循環動用
  *
  * @param recycleCode 該筆額度是否可循環動用
	*/
  public void setRecycleCode(int recycleCode) {
    this.recycleCode = recycleCode;
  }

/**
	* 該筆額度是否為不可撤銷<br>
	* 0:可撤銷  
1:不可撤銷
	* @return Integer
	*/
  public int getIrrevocableFlag() {
    return this.irrevocableFlag;
  }

/**
	* 該筆額度是否為不可撤銷<br>
	* 0:可撤銷  
1:不可撤銷
  *
  * @param irrevocableFlag 該筆額度是否為不可撤銷
	*/
  public void setIrrevocableFlag(int irrevocableFlag) {
    this.irrevocableFlag = irrevocableFlag;
  }

/**
	* 帳冊別<br>
	* 000:全公司
	* @return String
	*/
  public String getAcBookCode() {
    return this.acBookCode == null ? "" : this.acBookCode;
  }

/**
	* 帳冊別<br>
	* 000:全公司
  *
  * @param acBookCode 帳冊別
	*/
  public void setAcBookCode(String acBookCode) {
    this.acBookCode = acBookCode;
  }

/**
	* 區隔帳冊<br>
	* 00A:統帳冊                           201:利變年金帳冊
	* @return String
	*/
  public String getAcSubBookCode() {
    return this.acSubBookCode == null ? "" : this.acSubBookCode;
  }

/**
	* 區隔帳冊<br>
	* 00A:統帳冊                           201:利變年金帳冊
  *
  * @param acSubBookCode 區隔帳冊
	*/
  public void setAcSubBookCode(String acSubBookCode) {
    this.acSubBookCode = acSubBookCode;
  }

/**
	* 信用風險轉換係數<br>
	* 以【貸款區間】判斷，轉換成”月數”，不足1個月者視為1個月；當【貸款期間起(月)】&amp;lt;=月數&amp;lt;=【貸款期間迄(月)】，取對應的【風險轉換係數(LCTCCF)】
	* @return BigDecimal
	*/
  public BigDecimal getCcf() {
    return this.ccf;
  }

/**
	* 信用風險轉換係數<br>
	* 以【貸款區間】判斷，轉換成”月數”，不足1個月者視為1個月；當【貸款期間起(月)】&amp;lt;=月數&amp;lt;=【貸款期間迄(月)】，取對應的【風險轉換係數(LCTCCF)】
  *
  * @param ccf 信用風險轉換係數
	*/
  public void setCcf(BigDecimal ccf) {
    this.ccf = ccf;
  }

/**
	* 表外曝險金額<br>
	* 可動用餘額 * 信用風險轉換係數
	* @return BigDecimal
	*/
  public BigDecimal getExpLimitAmt() {
    return this.expLimitAmt;
  }

/**
	* 表外曝險金額<br>
	* 可動用餘額 * 信用風險轉換係數
  *
  * @param expLimitAmt 表外曝險金額
	*/
  public void setExpLimitAmt(BigDecimal expLimitAmt) {
    this.expLimitAmt = expLimitAmt;
  }

/**
	* 借方：備忘分錄會計科目<br>
	* 
	* @return String
	*/
  public String getDbAcNoCode() {
    return this.dbAcNoCode == null ? "" : this.dbAcNoCode;
  }

/**
	* 借方：備忘分錄會計科目<br>
	* 
  *
  * @param dbAcNoCode 借方：備忘分錄會計科目
	*/
  public void setDbAcNoCode(String dbAcNoCode) {
    this.dbAcNoCode = dbAcNoCode;
  }

/**
	* 貸方：備忘分錄會計科目<br>
	* 
	* @return String
	*/
  public String getCrAcNoCode() {
    return this.crAcNoCode == null ? "" : this.crAcNoCode;
  }

/**
	* 貸方：備忘分錄會計科目<br>
	* 
  *
  * @param crAcNoCode 貸方：備忘分錄會計科目
	*/
  public void setCrAcNoCode(String crAcNoCode) {
    this.crAcNoCode = crAcNoCode;
  }

/**
	* 已核撥記號<br>
	* 0:未核撥  
1:已核撥
	* @return Integer
	*/
  public int getDrawdownFg() {
    return this.drawdownFg;
  }

/**
	* 已核撥記號<br>
	* 0:未核撥  
1:已核撥
  *
  * @param drawdownFg 已核撥記號
	*/
  public void setDrawdownFg(int drawdownFg) {
    this.drawdownFg = drawdownFg;
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
    return "Ias39LoanCommit [ias39LoanCommitId=" + ias39LoanCommitId + ", approveDate=" + approveDate + ", firstDrawdownDate=" + firstDrawdownDate
           + ", maturityDate=" + maturityDate + ", loanTermYy=" + loanTermYy + ", loanTermMm=" + loanTermMm + ", loanTermDd=" + loanTermDd + ", utilDeadline=" + utilDeadline + ", recycleDeadline=" + recycleDeadline
           + ", lineAmt=" + lineAmt + ", utilBal=" + utilBal + ", avblBal=" + avblBal + ", recycleCode=" + recycleCode + ", irrevocableFlag=" + irrevocableFlag + ", acBookCode=" + acBookCode
           + ", acSubBookCode=" + acSubBookCode + ", ccf=" + ccf + ", expLimitAmt=" + expLimitAmt + ", dbAcNoCode=" + dbAcNoCode + ", crAcNoCode=" + crAcNoCode + ", drawdownFg=" + drawdownFg
           + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
