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
 * ClStock 擔保品股票檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`ClStock`")
public class ClStock implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 2399006464865540470L;

@EmbeddedId
  private ClStockId clStockId;

  // 擔保品代號1
  /* 擔保品代號檔CdCl */
  @Column(name = "`ClCode1`", insertable = false, updatable = false)
  private int clCode1 = 0;

  // 擔保品代號2
  /* 擔保品代號檔CdCl */
  @Column(name = "`ClCode2`", insertable = false, updatable = false)
  private int clCode2 = 0;

  // 擔保品號碼
  @Column(name = "`ClNo`", insertable = false, updatable = false)
  private int clNo = 0;

  // 股票代號
  @Column(name = "`StockCode`", length = 10)
  private String stockCode;

  // 掛牌別
  /* 01:上市02:上櫃03:興櫃04:公開05:非公開 */
  @Column(name = "`ListingType`", length = 2)
  private String listingType;

  // 股票種類
  /* 1:無2:普通股3:特別股 */
  @Column(name = "`StockType`", length = 1)
  private String stockType;

  // 發行公司統一編號
  @Column(name = "`CompanyId`", length = 10)
  private String companyId;

  // 資料年度
  @Column(name = "`DataYear`")
  private int dataYear = 0;

  // 發行股數
  @Column(name = "`IssuedShares`")
  private BigDecimal issuedShares = new BigDecimal("0");

  // 非上市(櫃)每股淨值
  @Column(name = "`NetWorth`")
  private BigDecimal netWorth = new BigDecimal("0");

  // 每股單價鑑估標準
  /* 01: 非上市(櫃)每股淨值02: 每股面額03: 前日收盤價04: 一個月平均價05: 三個月平均價 */
  @Column(name = "`EvaStandard`", length = 2)
  private String evaStandard;

  // 每股面額
  @Column(name = "`ParValue`")
  private BigDecimal parValue = new BigDecimal("0");

  // 一個月平均價
  @Column(name = "`MonthlyAvg`")
  private BigDecimal monthlyAvg = new BigDecimal("0");

  // 前日收盤價
  @Column(name = "`YdClosingPrice`")
  private BigDecimal ydClosingPrice = new BigDecimal("0");

  // 三個月平均價
  @Column(name = "`ThreeMonthAvg`")
  private BigDecimal threeMonthAvg = new BigDecimal("0");

  // 鑑定單價
  @Column(name = "`EvaUnitPrice`")
  private BigDecimal evaUnitPrice = new BigDecimal("0");

  // 股票持有人統編
  @Column(name = "`OwnerId`", length = 10)
  private String ownerId;

  // 股票持有人姓名
  @Column(name = "`OwnerName`", length = 100)
  private String ownerName;

  // 公司內部人職稱
  /* 01:董事長02:副董事長03:常務董事04:董事05:監察人06:總經理07:副總經理08:經理人09:協理10:大股東(持股10%以上)11:其他 */
  @Column(name = "`InsiderJobTitle`", length = 2)
  private String insiderJobTitle;

  // 公司內部人身分註記
  /* 01:本人02:法人代表03:本人配偶04:本人子女05:利用他人名義持有06:法人代表之配偶07:法人代表之子女08:經理人09:本人為金融機構協理 */
  @Column(name = "`InsiderPosition`", length = 2)
  private String insiderPosition;

  // 法定關係人統編
  @Column(name = "`LegalPersonId`", length = 10)
  private String legalPersonId;

  // 貸放成數(%)
  @Column(name = "`LoanToValue`")
  private BigDecimal loanToValue = new BigDecimal("0");

  // 擔保維持率(%)
  @Column(name = "`ClMtr`")
  private BigDecimal clMtr = new BigDecimal("0");

  // 通知追繳維持率(%)
  @Column(name = "`NoticeMtr`")
  private BigDecimal noticeMtr = new BigDecimal("0");

  // 實行職權維持率(%)
  @Column(name = "`ImplementMtr`")
  private BigDecimal implementMtr = new BigDecimal("0");

  // 質權設定書號
  @Column(name = "`PledgeNo`", length = 14)
  private String pledgeNo;

  // 計算維持率
  /* Y:是N:否 */
  @Column(name = "`ComputeMTR`", length = 1)
  private String computeMTR;

  // 設定狀態
  /* 1:設定2:解除 */
  @Column(name = "`SettingStat`", length = 1)
  private String settingStat;

  // 擔保品狀態
  /* 0:正常1:塗銷2:處分3:抵押權確定 */
  @Column(name = "`ClStat`", length = 1)
  private String clStat;

  // 股票設解(質)日期
  @Column(name = "`SettingDate`")
  private int settingDate = 0;

  // 設質股數餘額
  @Column(name = "`SettingBalance`")
  private BigDecimal settingBalance = new BigDecimal("0");

  // 擔保債權確定日期
  @Column(name = "`MtgDate`")
  private int mtgDate = 0;

  // 保管條號碼
  @Column(name = "`CustodyNo`", length = 5)
  private String custodyNo;

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


  public ClStockId getClStockId() {
    return this.clStockId;
  }

  public void setClStockId(ClStockId clStockId) {
    this.clStockId = clStockId;
  }

/**
	* 擔保品代號1<br>
	* 擔保品代號檔CdCl
	* @return Integer
	*/
  public int getClCode1() {
    return this.clCode1;
  }

/**
	* 擔保品代號1<br>
	* 擔保品代號檔CdCl
  *
  * @param clCode1 擔保品代號1
	*/
  public void setClCode1(int clCode1) {
    this.clCode1 = clCode1;
  }

/**
	* 擔保品代號2<br>
	* 擔保品代號檔CdCl
	* @return Integer
	*/
  public int getClCode2() {
    return this.clCode2;
  }

/**
	* 擔保品代號2<br>
	* 擔保品代號檔CdCl
  *
  * @param clCode2 擔保品代號2
	*/
  public void setClCode2(int clCode2) {
    this.clCode2 = clCode2;
  }

/**
	* 擔保品號碼<br>
	* 
	* @return Integer
	*/
  public int getClNo() {
    return this.clNo;
  }

/**
	* 擔保品號碼<br>
	* 
  *
  * @param clNo 擔保品號碼
	*/
  public void setClNo(int clNo) {
    this.clNo = clNo;
  }

/**
	* 股票代號<br>
	* 
	* @return String
	*/
  public String getStockCode() {
    return this.stockCode == null ? "" : this.stockCode;
  }

/**
	* 股票代號<br>
	* 
  *
  * @param stockCode 股票代號
	*/
  public void setStockCode(String stockCode) {
    this.stockCode = stockCode;
  }

/**
	* 掛牌別<br>
	* 01:上市
02:上櫃
03:興櫃
04:公開
05:非公開
	* @return String
	*/
  public String getListingType() {
    return this.listingType == null ? "" : this.listingType;
  }

/**
	* 掛牌別<br>
	* 01:上市
02:上櫃
03:興櫃
04:公開
05:非公開
  *
  * @param listingType 掛牌別
	*/
  public void setListingType(String listingType) {
    this.listingType = listingType;
  }

/**
	* 股票種類<br>
	* 1:無
2:普通股
3:特別股
	* @return String
	*/
  public String getStockType() {
    return this.stockType == null ? "" : this.stockType;
  }

/**
	* 股票種類<br>
	* 1:無
2:普通股
3:特別股
  *
  * @param stockType 股票種類
	*/
  public void setStockType(String stockType) {
    this.stockType = stockType;
  }

/**
	* 發行公司統一編號<br>
	* 
	* @return String
	*/
  public String getCompanyId() {
    return this.companyId == null ? "" : this.companyId;
  }

/**
	* 發行公司統一編號<br>
	* 
  *
  * @param companyId 發行公司統一編號
	*/
  public void setCompanyId(String companyId) {
    this.companyId = companyId;
  }

/**
	* 資料年度<br>
	* 
	* @return Integer
	*/
  public int getDataYear() {
    return this.dataYear;
  }

/**
	* 資料年度<br>
	* 
  *
  * @param dataYear 資料年度
	*/
  public void setDataYear(int dataYear) {
    this.dataYear = dataYear;
  }

/**
	* 發行股數<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getIssuedShares() {
    return this.issuedShares;
  }

/**
	* 發行股數<br>
	* 
  *
  * @param issuedShares 發行股數
	*/
  public void setIssuedShares(BigDecimal issuedShares) {
    this.issuedShares = issuedShares;
  }

/**
	* 非上市(櫃)每股淨值<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getNetWorth() {
    return this.netWorth;
  }

/**
	* 非上市(櫃)每股淨值<br>
	* 
  *
  * @param netWorth 非上市(櫃)每股淨值
	*/
  public void setNetWorth(BigDecimal netWorth) {
    this.netWorth = netWorth;
  }

/**
	* 每股單價鑑估標準<br>
	* 01: 非上市(櫃)每股淨值
02: 每股面額
03: 前日收盤價
04: 一個月平均價
05: 三個月平均價
	* @return String
	*/
  public String getEvaStandard() {
    return this.evaStandard == null ? "" : this.evaStandard;
  }

/**
	* 每股單價鑑估標準<br>
	* 01: 非上市(櫃)每股淨值
02: 每股面額
03: 前日收盤價
04: 一個月平均價
05: 三個月平均價
  *
  * @param evaStandard 每股單價鑑估標準
	*/
  public void setEvaStandard(String evaStandard) {
    this.evaStandard = evaStandard;
  }

/**
	* 每股面額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getParValue() {
    return this.parValue;
  }

/**
	* 每股面額<br>
	* 
  *
  * @param parValue 每股面額
	*/
  public void setParValue(BigDecimal parValue) {
    this.parValue = parValue;
  }

/**
	* 一個月平均價<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getMonthlyAvg() {
    return this.monthlyAvg;
  }

/**
	* 一個月平均價<br>
	* 
  *
  * @param monthlyAvg 一個月平均價
	*/
  public void setMonthlyAvg(BigDecimal monthlyAvg) {
    this.monthlyAvg = monthlyAvg;
  }

/**
	* 前日收盤價<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getYdClosingPrice() {
    return this.ydClosingPrice;
  }

/**
	* 前日收盤價<br>
	* 
  *
  * @param ydClosingPrice 前日收盤價
	*/
  public void setYdClosingPrice(BigDecimal ydClosingPrice) {
    this.ydClosingPrice = ydClosingPrice;
  }

/**
	* 三個月平均價<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getThreeMonthAvg() {
    return this.threeMonthAvg;
  }

/**
	* 三個月平均價<br>
	* 
  *
  * @param threeMonthAvg 三個月平均價
	*/
  public void setThreeMonthAvg(BigDecimal threeMonthAvg) {
    this.threeMonthAvg = threeMonthAvg;
  }

/**
	* 鑑定單價<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getEvaUnitPrice() {
    return this.evaUnitPrice;
  }

/**
	* 鑑定單價<br>
	* 
  *
  * @param evaUnitPrice 鑑定單價
	*/
  public void setEvaUnitPrice(BigDecimal evaUnitPrice) {
    this.evaUnitPrice = evaUnitPrice;
  }

/**
	* 股票持有人統編<br>
	* 
	* @return String
	*/
  public String getOwnerId() {
    return this.ownerId == null ? "" : this.ownerId;
  }

/**
	* 股票持有人統編<br>
	* 
  *
  * @param ownerId 股票持有人統編
	*/
  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }

/**
	* 股票持有人姓名<br>
	* 
	* @return String
	*/
  public String getOwnerName() {
    return this.ownerName == null ? "" : this.ownerName;
  }

/**
	* 股票持有人姓名<br>
	* 
  *
  * @param ownerName 股票持有人姓名
	*/
  public void setOwnerName(String ownerName) {
    this.ownerName = ownerName;
  }

/**
	* 公司內部人職稱<br>
	* 01:董事長
02:副董事長
03:常務董事
04:董事
05:監察人
06:總經理
07:副總經理
08:經理人
09:協理
10:大股東(持股10%以上)
11:其他
	* @return String
	*/
  public String getInsiderJobTitle() {
    return this.insiderJobTitle == null ? "" : this.insiderJobTitle;
  }

/**
	* 公司內部人職稱<br>
	* 01:董事長
02:副董事長
03:常務董事
04:董事
05:監察人
06:總經理
07:副總經理
08:經理人
09:協理
10:大股東(持股10%以上)
11:其他
  *
  * @param insiderJobTitle 公司內部人職稱
	*/
  public void setInsiderJobTitle(String insiderJobTitle) {
    this.insiderJobTitle = insiderJobTitle;
  }

/**
	* 公司內部人身分註記<br>
	* 01:本人
02:法人代表
03:本人配偶
04:本人子女
05:利用他人名義持有
06:法人代表之配偶
07:法人代表之子女
08:經理人
09:本人為金融機構協理
	* @return String
	*/
  public String getInsiderPosition() {
    return this.insiderPosition == null ? "" : this.insiderPosition;
  }

/**
	* 公司內部人身分註記<br>
	* 01:本人
02:法人代表
03:本人配偶
04:本人子女
05:利用他人名義持有
06:法人代表之配偶
07:法人代表之子女
08:經理人
09:本人為金融機構協理
  *
  * @param insiderPosition 公司內部人身分註記
	*/
  public void setInsiderPosition(String insiderPosition) {
    this.insiderPosition = insiderPosition;
  }

/**
	* 法定關係人統編<br>
	* 
	* @return String
	*/
  public String getLegalPersonId() {
    return this.legalPersonId == null ? "" : this.legalPersonId;
  }

/**
	* 法定關係人統編<br>
	* 
  *
  * @param legalPersonId 法定關係人統編
	*/
  public void setLegalPersonId(String legalPersonId) {
    this.legalPersonId = legalPersonId;
  }

/**
	* 貸放成數(%)<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLoanToValue() {
    return this.loanToValue;
  }

/**
	* 貸放成數(%)<br>
	* 
  *
  * @param loanToValue 貸放成數(%)
	*/
  public void setLoanToValue(BigDecimal loanToValue) {
    this.loanToValue = loanToValue;
  }

/**
	* 擔保維持率(%)<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getClMtr() {
    return this.clMtr;
  }

/**
	* 擔保維持率(%)<br>
	* 
  *
  * @param clMtr 擔保維持率(%)
	*/
  public void setClMtr(BigDecimal clMtr) {
    this.clMtr = clMtr;
  }

/**
	* 通知追繳維持率(%)<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getNoticeMtr() {
    return this.noticeMtr;
  }

/**
	* 通知追繳維持率(%)<br>
	* 
  *
  * @param noticeMtr 通知追繳維持率(%)
	*/
  public void setNoticeMtr(BigDecimal noticeMtr) {
    this.noticeMtr = noticeMtr;
  }

/**
	* 實行職權維持率(%)<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getImplementMtr() {
    return this.implementMtr;
  }

/**
	* 實行職權維持率(%)<br>
	* 
  *
  * @param implementMtr 實行職權維持率(%)
	*/
  public void setImplementMtr(BigDecimal implementMtr) {
    this.implementMtr = implementMtr;
  }

/**
	* 質權設定書號<br>
	* 
	* @return String
	*/
  public String getPledgeNo() {
    return this.pledgeNo == null ? "" : this.pledgeNo;
  }

/**
	* 質權設定書號<br>
	* 
  *
  * @param pledgeNo 質權設定書號
	*/
  public void setPledgeNo(String pledgeNo) {
    this.pledgeNo = pledgeNo;
  }

/**
	* 計算維持率<br>
	* Y:是
N:否
	* @return String
	*/
  public String getComputeMTR() {
    return this.computeMTR == null ? "" : this.computeMTR;
  }

/**
	* 計算維持率<br>
	* Y:是
N:否
  *
  * @param computeMTR 計算維持率
	*/
  public void setComputeMTR(String computeMTR) {
    this.computeMTR = computeMTR;
  }

/**
	* 設定狀態<br>
	* 1:設定
2:解除
	* @return String
	*/
  public String getSettingStat() {
    return this.settingStat == null ? "" : this.settingStat;
  }

/**
	* 設定狀態<br>
	* 1:設定
2:解除
  *
  * @param settingStat 設定狀態
	*/
  public void setSettingStat(String settingStat) {
    this.settingStat = settingStat;
  }

/**
	* 擔保品狀態<br>
	* 0:正常
1:塗銷
2:處分
3:抵押權確定
	* @return String
	*/
  public String getClStat() {
    return this.clStat == null ? "" : this.clStat;
  }

/**
	* 擔保品狀態<br>
	* 0:正常
1:塗銷
2:處分
3:抵押權確定
  *
  * @param clStat 擔保品狀態
	*/
  public void setClStat(String clStat) {
    this.clStat = clStat;
  }

/**
	* 股票設解(質)日期<br>
	* 
	* @return Integer
	*/
  public int getSettingDate() {
    return StaticTool.bcToRoc(this.settingDate);
  }

/**
	* 股票設解(質)日期<br>
	* 
  *
  * @param settingDate 股票設解(質)日期
  * @throws LogicException when Date Is Warn	*/
  public void setSettingDate(int settingDate) throws LogicException {
    this.settingDate = StaticTool.rocToBc(settingDate);
  }

/**
	* 設質股數餘額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getSettingBalance() {
    return this.settingBalance;
  }

/**
	* 設質股數餘額<br>
	* 
  *
  * @param settingBalance 設質股數餘額
	*/
  public void setSettingBalance(BigDecimal settingBalance) {
    this.settingBalance = settingBalance;
  }

/**
	* 擔保債權確定日期<br>
	* 
	* @return Integer
	*/
  public int getMtgDate() {
    return StaticTool.bcToRoc(this.mtgDate);
  }

/**
	* 擔保債權確定日期<br>
	* 
  *
  * @param mtgDate 擔保債權確定日期
  * @throws LogicException when Date Is Warn	*/
  public void setMtgDate(int mtgDate) throws LogicException {
    this.mtgDate = StaticTool.rocToBc(mtgDate);
  }

/**
	* 保管條號碼<br>
	* 
	* @return String
	*/
  public String getCustodyNo() {
    return this.custodyNo == null ? "" : this.custodyNo;
  }

/**
	* 保管條號碼<br>
	* 
  *
  * @param custodyNo 保管條號碼
	*/
  public void setCustodyNo(String custodyNo) {
    this.custodyNo = custodyNo;
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
    return "ClStock [clStockId=" + clStockId + ", stockCode=" + stockCode + ", listingType=" + listingType + ", stockType=" + stockType
           + ", companyId=" + companyId + ", dataYear=" + dataYear + ", issuedShares=" + issuedShares + ", netWorth=" + netWorth + ", evaStandard=" + evaStandard + ", parValue=" + parValue
           + ", monthlyAvg=" + monthlyAvg + ", ydClosingPrice=" + ydClosingPrice + ", threeMonthAvg=" + threeMonthAvg + ", evaUnitPrice=" + evaUnitPrice + ", ownerId=" + ownerId + ", ownerName=" + ownerName
           + ", insiderJobTitle=" + insiderJobTitle + ", insiderPosition=" + insiderPosition + ", legalPersonId=" + legalPersonId + ", loanToValue=" + loanToValue + ", clMtr=" + clMtr + ", noticeMtr=" + noticeMtr
           + ", implementMtr=" + implementMtr + ", pledgeNo=" + pledgeNo + ", computeMTR=" + computeMTR + ", settingStat=" + settingStat + ", clStat=" + clStat + ", settingDate=" + settingDate
           + ", settingBalance=" + settingBalance + ", mtgDate=" + mtgDate + ", custodyNo=" + custodyNo + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
           + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
