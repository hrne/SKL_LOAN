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
 * LoanIfrsHp IFRS9欄位清單8<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`LoanIfrsHp`")
public class LoanIfrsHp implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -2838876514512573421L;

@EmbeddedId
  private LoanIfrsHpId loanIfrsHpId;

  // 年月份
  @Column(name = "`DataYM`", insertable = false, updatable = false)
  private int dataYM = 0;

  // 戶號
  @Column(name = "`CustNo`", insertable = false, updatable = false)
  private int custNo = 0;

  // 借款人ID / 統編
  @Column(name = "`CustId`", length = 10)
  private String custId;

  // 額度編號
  @Column(name = "`FacmNo`", insertable = false, updatable = false)
  private int facmNo = 0;

  // 核准號碼
  @Column(name = "`ApplNo`")
  private int applNo = 0;

  // 企業戶/個人戶
  /* 1=企業戶(含企金自然人)2=個人戶 */
  @Column(name = "`CustKind`")
  private int custKind = 0;

  // 核准日期
  /* 1.優先取用對保日期2.無對保日採用准駁日3.若無上述二個日期，以空值提供 */
  @Column(name = "`ApproveDate`")
  private int approveDate = 0;

  // 初貸日期
  /* 額度初貸日(若尚未撥款，以空值列示) */
  @Column(name = "`FirstDrawdownDate`")
  private int firstDrawdownDate = 0;

  // 核准金額(台幣)
  @Column(name = "`LineAmt`")
  private BigDecimal lineAmt = new BigDecimal("0");

  // 產品別
  /* 作為群組分類。Ex:1=員工；2=車貸；3＝房貸；4＝政府優惠貸款…etc */
  @Column(name = "`IfrsProdCode`", length = 2)
  private String ifrsProdCode;

  // 可動用餘額(台幣)
  @Column(name = "`AvblBal`")
  private BigDecimal avblBal = new BigDecimal("0");

  // 該筆額度是否可循環動用
  /* 0: 非循環動用  1: 循環動用若註記為1，且本金餘額＜核准金額，但是可動用餘額=0；需確認是否實為循環額度 */
  @Column(name = "`RecycleCode`", length = 1)
  private String recycleCode;

  // 該筆額度是否為不可撤銷
  /* 1=是 0=否 */
  @Column(name = "`IrrevocableFlag`", length = 1)
  private String irrevocableFlag;

  // 主計處行業別代碼
  /* 產檔6碼 */
  @Column(name = "`IndustryCode`", length = 10)
  private String industryCode;

  // 原始認列時時信用評等
  /* 資料來源：與eloan系統對接 */
  @Column(name = "`OriRating`", length = 1)
  private String oriRating;

  // 原始認列時信用評等模型
  /* 資料來源：與eloan系統對接 */
  @Column(name = "`OriModel`", length = 1)
  private String oriModel;

  // 財務報導日時信用評等
  /* 資料來源：與eloan系統對接 */
  @Column(name = "`Rating`", length = 1)
  private String rating;

  // 財務報導日時信用評等模型
  /* 資料來源：與eloan系統對接 */
  @Column(name = "`Model`", length = 1)
  private String model;

  // 違約損失率模型
  /* 1=土地2=房地-台北市3=房地-新北市4=房地-桃園市5=房地-台中市6=房地-台南市7=房地-高雄市8=房地-其他(基隆市、新竹縣、新竹市、苗栗縣、彰化縣、南投縣、雲林縣、嘉義縣、嘉義市、屏東縣、宜蘭縣、花蓮縣、臺東縣、金門縣、澎湖縣、連江縣、南海島、釣魚臺9=股票10=機器設備11=車子12=銀行保證 */
  @Column(name = "`LGDModel`")
  private int lGDModel = 0;

  // 違約損失率
  /* m.nnnnnnnn */
  @Column(name = "`LGD`")
  private BigDecimal lGD = new BigDecimal("0");

  // 核准金額(交易幣)
  @Column(name = "`LineAmtCurr`")
  private BigDecimal lineAmtCurr = new BigDecimal("0");

  // 可動用餘額(交易幣)
  @Column(name = "`AvblBalCurr`")
  private BigDecimal avblBalCurr = new BigDecimal("0");

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


  public LoanIfrsHpId getLoanIfrsHpId() {
    return this.loanIfrsHpId;
  }

  public void setLoanIfrsHpId(LoanIfrsHpId loanIfrsHpId) {
    this.loanIfrsHpId = loanIfrsHpId;
  }

/**
	* 年月份<br>
	* 
	* @return Integer
	*/
  public int getDataYM() {
    return this.dataYM;
  }

/**
	* 年月份<br>
	* 
  *
  * @param dataYM 年月份
	*/
  public void setDataYM(int dataYM) {
    this.dataYM = dataYM;
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
	* 借款人ID / 統編<br>
	* 
	* @return String
	*/
  public String getCustId() {
    return this.custId == null ? "" : this.custId;
  }

/**
	* 借款人ID / 統編<br>
	* 
  *
  * @param custId 借款人ID / 統編
	*/
  public void setCustId(String custId) {
    this.custId = custId;
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
	* 企業戶/個人戶<br>
	* 1=企業戶(含企金自然人)
2=個人戶
	* @return Integer
	*/
  public int getCustKind() {
    return this.custKind;
  }

/**
	* 企業戶/個人戶<br>
	* 1=企業戶(含企金自然人)
2=個人戶
  *
  * @param custKind 企業戶/個人戶
	*/
  public void setCustKind(int custKind) {
    this.custKind = custKind;
  }

/**
	* 核准日期<br>
	* 1.優先取用對保日期
2.無對保日採用准駁日
3.若無上述二個日期，以空值提供
	* @return Integer
	*/
  public int getApproveDate() {
    return StaticTool.bcToRoc(this.approveDate);
  }

/**
	* 核准日期<br>
	* 1.優先取用對保日期
2.無對保日採用准駁日
3.若無上述二個日期，以空值提供
  *
  * @param approveDate 核准日期
  * @throws LogicException when Date Is Warn	*/
  public void setApproveDate(int approveDate) throws LogicException {
    this.approveDate = StaticTool.rocToBc(approveDate);
  }

/**
	* 初貸日期<br>
	* 額度初貸日(若尚未撥款，以空值列示)
	* @return Integer
	*/
  public int getFirstDrawdownDate() {
    return StaticTool.bcToRoc(this.firstDrawdownDate);
  }

/**
	* 初貸日期<br>
	* 額度初貸日(若尚未撥款，以空值列示)
  *
  * @param firstDrawdownDate 初貸日期
  * @throws LogicException when Date Is Warn	*/
  public void setFirstDrawdownDate(int firstDrawdownDate) throws LogicException {
    this.firstDrawdownDate = StaticTool.rocToBc(firstDrawdownDate);
  }

/**
	* 核准金額(台幣)<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLineAmt() {
    return this.lineAmt;
  }

/**
	* 核准金額(台幣)<br>
	* 
  *
  * @param lineAmt 核准金額(台幣)
	*/
  public void setLineAmt(BigDecimal lineAmt) {
    this.lineAmt = lineAmt;
  }

/**
	* 產品別<br>
	* 作為群組分類。Ex:1=員工；2=車貸；3＝房貸；4＝政府優惠貸款…etc
	* @return String
	*/
  public String getIfrsProdCode() {
    return this.ifrsProdCode == null ? "" : this.ifrsProdCode;
  }

/**
	* 產品別<br>
	* 作為群組分類。Ex:1=員工；2=車貸；3＝房貸；4＝政府優惠貸款…etc
  *
  * @param ifrsProdCode 產品別
	*/
  public void setIfrsProdCode(String ifrsProdCode) {
    this.ifrsProdCode = ifrsProdCode;
  }

/**
	* 可動用餘額(台幣)<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getAvblBal() {
    return this.avblBal;
  }

/**
	* 可動用餘額(台幣)<br>
	* 
  *
  * @param avblBal 可動用餘額(台幣)
	*/
  public void setAvblBal(BigDecimal avblBal) {
    this.avblBal = avblBal;
  }

/**
	* 該筆額度是否可循環動用<br>
	* 0: 非循環動用  1: 循環動用
若註記為1，且本金餘額＜核准金額，但是可動用餘額=0；需確認是否實為循環額度
	* @return String
	*/
  public String getRecycleCode() {
    return this.recycleCode == null ? "" : this.recycleCode;
  }

/**
	* 該筆額度是否可循環動用<br>
	* 0: 非循環動用  1: 循環動用
若註記為1，且本金餘額＜核准金額，但是可動用餘額=0；需確認是否實為循環額度
  *
  * @param recycleCode 該筆額度是否可循環動用
	*/
  public void setRecycleCode(String recycleCode) {
    this.recycleCode = recycleCode;
  }

/**
	* 該筆額度是否為不可撤銷<br>
	* 1=是 0=否
	* @return String
	*/
  public String getIrrevocableFlag() {
    return this.irrevocableFlag == null ? "" : this.irrevocableFlag;
  }

/**
	* 該筆額度是否為不可撤銷<br>
	* 1=是 0=否
  *
  * @param irrevocableFlag 該筆額度是否為不可撤銷
	*/
  public void setIrrevocableFlag(String irrevocableFlag) {
    this.irrevocableFlag = irrevocableFlag;
  }

/**
	* 主計處行業別代碼<br>
	* 產檔6碼
	* @return String
	*/
  public String getIndustryCode() {
    return this.industryCode == null ? "" : this.industryCode;
  }

/**
	* 主計處行業別代碼<br>
	* 產檔6碼
  *
  * @param industryCode 主計處行業別代碼
	*/
  public void setIndustryCode(String industryCode) {
    this.industryCode = industryCode;
  }

/**
	* 原始認列時時信用評等<br>
	* 資料來源：與eloan系統對接
	* @return String
	*/
  public String getOriRating() {
    return this.oriRating == null ? "" : this.oriRating;
  }

/**
	* 原始認列時時信用評等<br>
	* 資料來源：與eloan系統對接
  *
  * @param oriRating 原始認列時時信用評等
	*/
  public void setOriRating(String oriRating) {
    this.oriRating = oriRating;
  }

/**
	* 原始認列時信用評等模型<br>
	* 資料來源：與eloan系統對接
	* @return String
	*/
  public String getOriModel() {
    return this.oriModel == null ? "" : this.oriModel;
  }

/**
	* 原始認列時信用評等模型<br>
	* 資料來源：與eloan系統對接
  *
  * @param oriModel 原始認列時信用評等模型
	*/
  public void setOriModel(String oriModel) {
    this.oriModel = oriModel;
  }

/**
	* 財務報導日時信用評等<br>
	* 資料來源：與eloan系統對接
	* @return String
	*/
  public String getRating() {
    return this.rating == null ? "" : this.rating;
  }

/**
	* 財務報導日時信用評等<br>
	* 資料來源：與eloan系統對接
  *
  * @param rating 財務報導日時信用評等
	*/
  public void setRating(String rating) {
    this.rating = rating;
  }

/**
	* 財務報導日時信用評等模型<br>
	* 資料來源：與eloan系統對接
	* @return String
	*/
  public String getModel() {
    return this.model == null ? "" : this.model;
  }

/**
	* 財務報導日時信用評等模型<br>
	* 資料來源：與eloan系統對接
  *
  * @param model 財務報導日時信用評等模型
	*/
  public void setModel(String model) {
    this.model = model;
  }

/**
	* 違約損失率模型<br>
	* 1=土地
2=房地-台北市
3=房地-新北市
4=房地-桃園市
5=房地-台中市
6=房地-台南市
7=房地-高雄市
8=房地-其他(基隆市、新竹縣、新竹市、苗栗縣、彰化縣、南投縣、雲林縣、嘉義縣、嘉義市、屏東縣、宜蘭縣、花蓮縣、臺東縣、金門縣、澎湖縣、連江縣、南海島、釣魚臺
9=股票
10=機器設備
11=車子
12=銀行保證
	* @return Integer
	*/
  public int getLGDModel() {
    return this.lGDModel;
  }

/**
	* 違約損失率模型<br>
	* 1=土地
2=房地-台北市
3=房地-新北市
4=房地-桃園市
5=房地-台中市
6=房地-台南市
7=房地-高雄市
8=房地-其他(基隆市、新竹縣、新竹市、苗栗縣、彰化縣、南投縣、雲林縣、嘉義縣、嘉義市、屏東縣、宜蘭縣、花蓮縣、臺東縣、金門縣、澎湖縣、連江縣、南海島、釣魚臺
9=股票
10=機器設備
11=車子
12=銀行保證
  *
  * @param lGDModel 違約損失率模型
	*/
  public void setLGDModel(int lGDModel) {
    this.lGDModel = lGDModel;
  }

/**
	* 違約損失率<br>
	* m.nnnnnnnn
	* @return BigDecimal
	*/
  public BigDecimal getLGD() {
    return this.lGD;
  }

/**
	* 違約損失率<br>
	* m.nnnnnnnn
  *
  * @param lGD 違約損失率
	*/
  public void setLGD(BigDecimal lGD) {
    this.lGD = lGD;
  }

/**
	* 核准金額(交易幣)<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLineAmtCurr() {
    return this.lineAmtCurr;
  }

/**
	* 核准金額(交易幣)<br>
	* 
  *
  * @param lineAmtCurr 核准金額(交易幣)
	*/
  public void setLineAmtCurr(BigDecimal lineAmtCurr) {
    this.lineAmtCurr = lineAmtCurr;
  }

/**
	* 可動用餘額(交易幣)<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getAvblBalCurr() {
    return this.avblBalCurr;
  }

/**
	* 可動用餘額(交易幣)<br>
	* 
  *
  * @param avblBalCurr 可動用餘額(交易幣)
	*/
  public void setAvblBalCurr(BigDecimal avblBalCurr) {
    this.avblBalCurr = avblBalCurr;
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
    return "LoanIfrsHp [loanIfrsHpId=" + loanIfrsHpId + ", custId=" + custId + ", applNo=" + applNo + ", custKind=" + custKind
           + ", approveDate=" + approveDate + ", firstDrawdownDate=" + firstDrawdownDate + ", lineAmt=" + lineAmt + ", ifrsProdCode=" + ifrsProdCode + ", avblBal=" + avblBal + ", recycleCode=" + recycleCode
           + ", irrevocableFlag=" + irrevocableFlag + ", industryCode=" + industryCode + ", oriRating=" + oriRating + ", oriModel=" + oriModel + ", rating=" + rating + ", model=" + model
           + ", lGDModel=" + lGDModel + ", lGD=" + lGD + ", lineAmtCurr=" + lineAmtCurr + ", avblBalCurr=" + avblBalCurr + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
           + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
