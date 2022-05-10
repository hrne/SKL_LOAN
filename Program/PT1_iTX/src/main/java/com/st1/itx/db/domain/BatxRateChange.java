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
 * BatxRateChange 整批利率調整檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`BatxRateChange`")
public class BatxRateChange implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -1055386299672988675L;

@EmbeddedId
  private BatxRateChangeId batxRateChangeId;

  // 調整日期
  @Column(name = "`AdjDate`", insertable = false, updatable = false)
  private int adjDate = 0;

  // 戶號
  @Column(name = "`CustNo`", insertable = false, updatable = false)
  private int custNo = 0;

  // 額度
  @Column(name = "`FacmNo`", insertable = false, updatable = false)
  private int facmNo = 0;

  // 撥款序號
  @Column(name = "`BormNo`", insertable = false, updatable = false)
  private int bormNo = 0;

  // 作業項目
  /* CdCode.TxKind1:定期機動調整2:指數型利率調整3:機動利率調整4:員工利率調整5:按商品別調整 */
  @Column(name = "`TxKind`")
  private int txKind = 0;

  // 撥款金額
  @Column(name = "`DrawdownAmt`")
  private BigDecimal drawdownAmt = new BigDecimal("0");

  // 地區別
  @Column(name = "`CityCode`", length = 2)
  private String cityCode;

  // 鄉鎮區
  @Column(name = "`AreaCode`", length = 3)
  private String areaCode;

  // 加減碼是否依合約
  /* Y:是N:否 */
  @Column(name = "`IncrFlag`", length = 1)
  private String incrFlag;

  // 調整記號
  /* 1:批次自動調整2:按地區別調整3:人工調整4.批次自動調整(提醒件) (下次利率調整日為到期日、非下次利率調整日調整) */
  @Column(name = "`AdjCode`")
  private int adjCode = 0;

  // 利率輸入記號
  /* 0:未調整1:已調整2:待輸入9:待處理(檢核有誤) */
  @Column(name = "`RateKeyInCode`")
  private int rateKeyInCode = 0;

  // 確認記號
  /* 0:未確認1:確認未放行2:已確認放行L4321維護，確認後Table欄位不可更改 */
  @Column(name = "`ConfirmFlag`")
  private int confirmFlag = 0;

  // 全戶餘額
  @Column(name = "`TotBalance`")
  private BigDecimal totBalance = new BigDecimal("0");

  // 放款餘額
  @Column(name = "`LoanBalance`")
  private BigDecimal loanBalance = new BigDecimal("0");

  // 目前生效日
  /* 調整前借戶利率檔適用利率 */
  @Column(name = "`PresEffDate`")
  private int presEffDate = 0;

  // 本次生效日
  @Column(name = "`CurtEffDate`")
  private int curtEffDate = 0;

  // 調整前下次利率調整日
  @Column(name = "`PreNextAdjDate`")
  private int preNextAdjDate = 0;

  // 調整前下次利率調整週期
  /* 未用 */
  @Column(name = "`PreNextAdjFreq`")
  private int preNextAdjFreq = 0;

  // 繳息迄日
  @Column(name = "`PrevIntDate`")
  private int prevIntDate = 0;

  // 戶別
  /* CdCode.EntCode0:個金1:企金2:企金自然人 */
  @Column(name = "`CustCode`")
  private int custCode = 0;

  // 商品代碼
  @Column(name = "`ProdNo`", length = 5)
  private String prodNo;

  // 加碼利率
  /* 調整前借戶利率檔加碼利率 */
  @Column(name = "`RateIncr`")
  private BigDecimal rateIncr = new BigDecimal("0");

  // 合約利率
  @Column(name = "`ContractRate`")
  private BigDecimal contractRate = new BigDecimal("0");

  // 目前利率
  @Column(name = "`PresentRate`")
  private BigDecimal presentRate = new BigDecimal("0");

  // 擬調利率
  @Column(name = "`ProposalRate`")
  private BigDecimal proposalRate = new BigDecimal("0");

  // 調整後利率
  @Column(name = "`AdjustedRate`")
  private BigDecimal adjustedRate = new BigDecimal("0");

  // 合約指標利率
  /* ContrIndexRate */
  @Column(name = "`ContrBaseRate`")
  private BigDecimal contrBaseRate = new BigDecimal("0");

  // 合約加碼利率
  /* 調整前借戶利率檔加碼利率 */
  @Column(name = "`ContrRateIncr`")
  private BigDecimal contrRateIncr = new BigDecimal("0");

  // 個別加碼利率
  /* 調整前借戶利率檔個別加碼利率 */
  @Column(name = "`IndividualIncr`")
  private BigDecimal individualIncr = new BigDecimal("0");

  // 指標利率代碼
  /* CdCode.ProdBaseRateCode01:保單分紅利率02:郵政儲金利率 99:自訂利率RateType */
  @Column(name = "`BaseRateCode`", length = 2)
  private String baseRateCode;

  // 利率區分
  /* CdCode.FacmRateCode1:機動2:固定3:定期機動 */
  @Column(name = "`RateCode`", length = 1)
  private String rateCode;

  // 本次指標利率
  /* NowIndexRate */
  @Column(name = "`CurrBaseRate`")
  private BigDecimal currBaseRate = new BigDecimal("0");

  // 放款利率變動檔生效日
  /* 利率未變動為零 */
  @Column(name = "`TxEffectDate`")
  private int txEffectDate = 0;

  // 產檔時鍵入預調利率週期
  @Column(name = "`TxRateAdjFreq`")
  private int txRateAdjFreq = 0;

  // jason格式紀錄欄
  @Column(name = "`JsonFields`", length = 2000)
  private String jsonFields;

  // 逾期期數
  @Column(name = "`OvduTerm`")
  private int ovduTerm = 0;

  // 經辦
  @Column(name = "`TitaTlrNo`", length = 6)
  private String titaTlrNo;

  // 交易序號
  @Column(name = "`TitaTxtNo`", length = 8)
  private String titaTxtNo;

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


  public BatxRateChangeId getBatxRateChangeId() {
    return this.batxRateChangeId;
  }

  public void setBatxRateChangeId(BatxRateChangeId batxRateChangeId) {
    this.batxRateChangeId = batxRateChangeId;
  }

/**
	* 調整日期<br>
	* 
	* @return Integer
	*/
  public int getAdjDate() {
    return StaticTool.bcToRoc(this.adjDate);
  }

/**
	* 調整日期<br>
	* 
  *
  * @param adjDate 調整日期
  * @throws LogicException when Date Is Warn	*/
  public void setAdjDate(int adjDate) throws LogicException {
    this.adjDate = StaticTool.rocToBc(adjDate);
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
	* 撥款序號<br>
	* 
	* @return Integer
	*/
  public int getBormNo() {
    return this.bormNo;
  }

/**
	* 撥款序號<br>
	* 
  *
  * @param bormNo 撥款序號
	*/
  public void setBormNo(int bormNo) {
    this.bormNo = bormNo;
  }

/**
	* 作業項目<br>
	* CdCode.TxKind
1:定期機動調整
2:指數型利率調整
3:機動利率調整
4:員工利率調整
5:按商品別調整
	* @return Integer
	*/
  public int getTxKind() {
    return this.txKind;
  }

/**
	* 作業項目<br>
	* CdCode.TxKind
1:定期機動調整
2:指數型利率調整
3:機動利率調整
4:員工利率調整
5:按商品別調整
  *
  * @param txKind 作業項目
	*/
  public void setTxKind(int txKind) {
    this.txKind = txKind;
  }

/**
	* 撥款金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getDrawdownAmt() {
    return this.drawdownAmt;
  }

/**
	* 撥款金額<br>
	* 
  *
  * @param drawdownAmt 撥款金額
	*/
  public void setDrawdownAmt(BigDecimal drawdownAmt) {
    this.drawdownAmt = drawdownAmt;
  }

/**
	* 地區別<br>
	* 
	* @return String
	*/
  public String getCityCode() {
    return this.cityCode == null ? "" : this.cityCode;
  }

/**
	* 地區別<br>
	* 
  *
  * @param cityCode 地區別
	*/
  public void setCityCode(String cityCode) {
    this.cityCode = cityCode;
  }

/**
	* 鄉鎮區<br>
	* 
	* @return String
	*/
  public String getAreaCode() {
    return this.areaCode == null ? "" : this.areaCode;
  }

/**
	* 鄉鎮區<br>
	* 
  *
  * @param areaCode 鄉鎮區
	*/
  public void setAreaCode(String areaCode) {
    this.areaCode = areaCode;
  }

/**
	* 加減碼是否依合約<br>
	* Y:是
N:否
	* @return String
	*/
  public String getIncrFlag() {
    return this.incrFlag == null ? "" : this.incrFlag;
  }

/**
	* 加減碼是否依合約<br>
	* Y:是
N:否
  *
  * @param incrFlag 加減碼是否依合約
	*/
  public void setIncrFlag(String incrFlag) {
    this.incrFlag = incrFlag;
  }

/**
	* 調整記號<br>
	* 1:批次自動調整
2:按地區別調整
3:人工調整
4.批次自動調整(提醒件)
 (下次利率調整日為到期日、非下次利率調整日調整)
	* @return Integer
	*/
  public int getAdjCode() {
    return this.adjCode;
  }

/**
	* 調整記號<br>
	* 1:批次自動調整
2:按地區別調整
3:人工調整
4.批次自動調整(提醒件)
 (下次利率調整日為到期日、非下次利率調整日調整)
  *
  * @param adjCode 調整記號
	*/
  public void setAdjCode(int adjCode) {
    this.adjCode = adjCode;
  }

/**
	* 利率輸入記號<br>
	* 0:未調整
1:已調整
2:待輸入
9:待處理(檢核有誤)
	* @return Integer
	*/
  public int getRateKeyInCode() {
    return this.rateKeyInCode;
  }

/**
	* 利率輸入記號<br>
	* 0:未調整
1:已調整
2:待輸入
9:待處理(檢核有誤)
  *
  * @param rateKeyInCode 利率輸入記號
	*/
  public void setRateKeyInCode(int rateKeyInCode) {
    this.rateKeyInCode = rateKeyInCode;
  }

/**
	* 確認記號<br>
	* 0:未確認
1:確認未放行
2:已確認放行
L4321維護，確認後Table欄位不可更改
	* @return Integer
	*/
  public int getConfirmFlag() {
    return this.confirmFlag;
  }

/**
	* 確認記號<br>
	* 0:未確認
1:確認未放行
2:已確認放行
L4321維護，確認後Table欄位不可更改
  *
  * @param confirmFlag 確認記號
	*/
  public void setConfirmFlag(int confirmFlag) {
    this.confirmFlag = confirmFlag;
  }

/**
	* 全戶餘額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getTotBalance() {
    return this.totBalance;
  }

/**
	* 全戶餘額<br>
	* 
  *
  * @param totBalance 全戶餘額
	*/
  public void setTotBalance(BigDecimal totBalance) {
    this.totBalance = totBalance;
  }

/**
	* 放款餘額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLoanBalance() {
    return this.loanBalance;
  }

/**
	* 放款餘額<br>
	* 
  *
  * @param loanBalance 放款餘額
	*/
  public void setLoanBalance(BigDecimal loanBalance) {
    this.loanBalance = loanBalance;
  }

/**
	* 目前生效日<br>
	* 調整前借戶利率檔適用利率
	* @return Integer
	*/
  public int getPresEffDate() {
    return StaticTool.bcToRoc(this.presEffDate);
  }

/**
	* 目前生效日<br>
	* 調整前借戶利率檔適用利率
  *
  * @param presEffDate 目前生效日
  * @throws LogicException when Date Is Warn	*/
  public void setPresEffDate(int presEffDate) throws LogicException {
    this.presEffDate = StaticTool.rocToBc(presEffDate);
  }

/**
	* 本次生效日<br>
	* 
	* @return Integer
	*/
  public int getCurtEffDate() {
    return StaticTool.bcToRoc(this.curtEffDate);
  }

/**
	* 本次生效日<br>
	* 
  *
  * @param curtEffDate 本次生效日
  * @throws LogicException when Date Is Warn	*/
  public void setCurtEffDate(int curtEffDate) throws LogicException {
    this.curtEffDate = StaticTool.rocToBc(curtEffDate);
  }

/**
	* 調整前下次利率調整日<br>
	* 
	* @return Integer
	*/
  public int getPreNextAdjDate() {
    return StaticTool.bcToRoc(this.preNextAdjDate);
  }

/**
	* 調整前下次利率調整日<br>
	* 
  *
  * @param preNextAdjDate 調整前下次利率調整日
  * @throws LogicException when Date Is Warn	*/
  public void setPreNextAdjDate(int preNextAdjDate) throws LogicException {
    this.preNextAdjDate = StaticTool.rocToBc(preNextAdjDate);
  }

/**
	* 調整前下次利率調整週期<br>
	* 未用
	* @return Integer
	*/
  public int getPreNextAdjFreq() {
    return this.preNextAdjFreq;
  }

/**
	* 調整前下次利率調整週期<br>
	* 未用
  *
  * @param preNextAdjFreq 調整前下次利率調整週期
	*/
  public void setPreNextAdjFreq(int preNextAdjFreq) {
    this.preNextAdjFreq = preNextAdjFreq;
  }

/**
	* 繳息迄日<br>
	* 
	* @return Integer
	*/
  public int getPrevIntDate() {
    return StaticTool.bcToRoc(this.prevIntDate);
  }

/**
	* 繳息迄日<br>
	* 
  *
  * @param prevIntDate 繳息迄日
  * @throws LogicException when Date Is Warn	*/
  public void setPrevIntDate(int prevIntDate) throws LogicException {
    this.prevIntDate = StaticTool.rocToBc(prevIntDate);
  }

/**
	* 戶別<br>
	* CdCode.EntCode
0:個金
1:企金
2:企金自然人
	* @return Integer
	*/
  public int getCustCode() {
    return this.custCode;
  }

/**
	* 戶別<br>
	* CdCode.EntCode
0:個金
1:企金
2:企金自然人
  *
  * @param custCode 戶別
	*/
  public void setCustCode(int custCode) {
    this.custCode = custCode;
  }

/**
	* 商品代碼<br>
	* 
	* @return String
	*/
  public String getProdNo() {
    return this.prodNo == null ? "" : this.prodNo;
  }

/**
	* 商品代碼<br>
	* 
  *
  * @param prodNo 商品代碼
	*/
  public void setProdNo(String prodNo) {
    this.prodNo = prodNo;
  }

/**
	* 加碼利率<br>
	* 調整前借戶利率檔加碼利率
	* @return BigDecimal
	*/
  public BigDecimal getRateIncr() {
    return this.rateIncr;
  }

/**
	* 加碼利率<br>
	* 調整前借戶利率檔加碼利率
  *
  * @param rateIncr 加碼利率
	*/
  public void setRateIncr(BigDecimal rateIncr) {
    this.rateIncr = rateIncr;
  }

/**
	* 合約利率<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getContractRate() {
    return this.contractRate;
  }

/**
	* 合約利率<br>
	* 
  *
  * @param contractRate 合約利率
	*/
  public void setContractRate(BigDecimal contractRate) {
    this.contractRate = contractRate;
  }

/**
	* 目前利率<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getPresentRate() {
    return this.presentRate;
  }

/**
	* 目前利率<br>
	* 
  *
  * @param presentRate 目前利率
	*/
  public void setPresentRate(BigDecimal presentRate) {
    this.presentRate = presentRate;
  }

/**
	* 擬調利率<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getProposalRate() {
    return this.proposalRate;
  }

/**
	* 擬調利率<br>
	* 
  *
  * @param proposalRate 擬調利率
	*/
  public void setProposalRate(BigDecimal proposalRate) {
    this.proposalRate = proposalRate;
  }

/**
	* 調整後利率<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getAdjustedRate() {
    return this.adjustedRate;
  }

/**
	* 調整後利率<br>
	* 
  *
  * @param adjustedRate 調整後利率
	*/
  public void setAdjustedRate(BigDecimal adjustedRate) {
    this.adjustedRate = adjustedRate;
  }

/**
	* 合約指標利率<br>
	* ContrIndexRate
	* @return BigDecimal
	*/
  public BigDecimal getContrBaseRate() {
    return this.contrBaseRate;
  }

/**
	* 合約指標利率<br>
	* ContrIndexRate
  *
  * @param contrBaseRate 合約指標利率
	*/
  public void setContrBaseRate(BigDecimal contrBaseRate) {
    this.contrBaseRate = contrBaseRate;
  }

/**
	* 合約加碼利率<br>
	* 調整前借戶利率檔加碼利率
	* @return BigDecimal
	*/
  public BigDecimal getContrRateIncr() {
    return this.contrRateIncr;
  }

/**
	* 合約加碼利率<br>
	* 調整前借戶利率檔加碼利率
  *
  * @param contrRateIncr 合約加碼利率
	*/
  public void setContrRateIncr(BigDecimal contrRateIncr) {
    this.contrRateIncr = contrRateIncr;
  }

/**
	* 個別加碼利率<br>
	* 調整前借戶利率檔個別加碼利率
	* @return BigDecimal
	*/
  public BigDecimal getIndividualIncr() {
    return this.individualIncr;
  }

/**
	* 個別加碼利率<br>
	* 調整前借戶利率檔個別加碼利率
  *
  * @param individualIncr 個別加碼利率
	*/
  public void setIndividualIncr(BigDecimal individualIncr) {
    this.individualIncr = individualIncr;
  }

/**
	* 指標利率代碼<br>
	* CdCode.ProdBaseRateCode
01:保單分紅利率
02:郵政儲金利率 
99:自訂利率
RateType
	* @return String
	*/
  public String getBaseRateCode() {
    return this.baseRateCode == null ? "" : this.baseRateCode;
  }

/**
	* 指標利率代碼<br>
	* CdCode.ProdBaseRateCode
01:保單分紅利率
02:郵政儲金利率 
99:自訂利率
RateType
  *
  * @param baseRateCode 指標利率代碼
	*/
  public void setBaseRateCode(String baseRateCode) {
    this.baseRateCode = baseRateCode;
  }

/**
	* 利率區分<br>
	* CdCode.FacmRateCode
1:機動
2:固定
3:定期機動
	* @return String
	*/
  public String getRateCode() {
    return this.rateCode == null ? "" : this.rateCode;
  }

/**
	* 利率區分<br>
	* CdCode.FacmRateCode
1:機動
2:固定
3:定期機動
  *
  * @param rateCode 利率區分
	*/
  public void setRateCode(String rateCode) {
    this.rateCode = rateCode;
  }

/**
	* 本次指標利率<br>
	* NowIndexRate
	* @return BigDecimal
	*/
  public BigDecimal getCurrBaseRate() {
    return this.currBaseRate;
  }

/**
	* 本次指標利率<br>
	* NowIndexRate
  *
  * @param currBaseRate 本次指標利率
	*/
  public void setCurrBaseRate(BigDecimal currBaseRate) {
    this.currBaseRate = currBaseRate;
  }

/**
	* 放款利率變動檔生效日<br>
	* 利率未變動為零
	* @return Integer
	*/
  public int getTxEffectDate() {
    return StaticTool.bcToRoc(this.txEffectDate);
  }

/**
	* 放款利率變動檔生效日<br>
	* 利率未變動為零
  *
  * @param txEffectDate 放款利率變動檔生效日
  * @throws LogicException when Date Is Warn	*/
  public void setTxEffectDate(int txEffectDate) throws LogicException {
    this.txEffectDate = StaticTool.rocToBc(txEffectDate);
  }

/**
	* 產檔時鍵入預調利率週期<br>
	* 
	* @return Integer
	*/
  public int getTxRateAdjFreq() {
    return this.txRateAdjFreq;
  }

/**
	* 產檔時鍵入預調利率週期<br>
	* 
  *
  * @param txRateAdjFreq 產檔時鍵入預調利率週期
	*/
  public void setTxRateAdjFreq(int txRateAdjFreq) {
    this.txRateAdjFreq = txRateAdjFreq;
  }

/**
	* jason格式紀錄欄<br>
	* 
	* @return String
	*/
  public String getJsonFields() {
    return this.jsonFields == null ? "" : this.jsonFields;
  }

/**
	* jason格式紀錄欄<br>
	* 
  *
  * @param jsonFields jason格式紀錄欄
	*/
  public void setJsonFields(String jsonFields) {
    this.jsonFields = jsonFields;
  }

/**
	* 逾期期數<br>
	* 
	* @return Integer
	*/
  public int getOvduTerm() {
    return this.ovduTerm;
  }

/**
	* 逾期期數<br>
	* 
  *
  * @param ovduTerm 逾期期數
	*/
  public void setOvduTerm(int ovduTerm) {
    this.ovduTerm = ovduTerm;
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
	* @return String
	*/
  public String getTitaTxtNo() {
    return this.titaTxtNo == null ? "" : this.titaTxtNo;
  }

/**
	* 交易序號<br>
	* 
  *
  * @param titaTxtNo 交易序號
	*/
  public void setTitaTxtNo(String titaTxtNo) {
    this.titaTxtNo = titaTxtNo;
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
    return "BatxRateChange [batxRateChangeId=" + batxRateChangeId + ", txKind=" + txKind + ", drawdownAmt=" + drawdownAmt
           + ", cityCode=" + cityCode + ", areaCode=" + areaCode + ", incrFlag=" + incrFlag + ", adjCode=" + adjCode + ", rateKeyInCode=" + rateKeyInCode + ", confirmFlag=" + confirmFlag
           + ", totBalance=" + totBalance + ", loanBalance=" + loanBalance + ", presEffDate=" + presEffDate + ", curtEffDate=" + curtEffDate + ", preNextAdjDate=" + preNextAdjDate + ", preNextAdjFreq=" + preNextAdjFreq
           + ", prevIntDate=" + prevIntDate + ", custCode=" + custCode + ", prodNo=" + prodNo + ", rateIncr=" + rateIncr + ", contractRate=" + contractRate + ", presentRate=" + presentRate
           + ", proposalRate=" + proposalRate + ", adjustedRate=" + adjustedRate + ", contrBaseRate=" + contrBaseRate + ", contrRateIncr=" + contrRateIncr + ", individualIncr=" + individualIncr + ", baseRateCode=" + baseRateCode
           + ", rateCode=" + rateCode + ", currBaseRate=" + currBaseRate + ", txEffectDate=" + txEffectDate + ", txRateAdjFreq=" + txRateAdjFreq + ", jsonFields=" + jsonFields + ", ovduTerm=" + ovduTerm
           + ", titaTlrNo=" + titaTlrNo + ", titaTxtNo=" + titaTxtNo + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo
           + "]";
  }
}
