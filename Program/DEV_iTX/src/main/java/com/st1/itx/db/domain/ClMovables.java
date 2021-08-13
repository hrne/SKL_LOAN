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
 * ClMovables 擔保品動產檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`ClMovables`")
public class ClMovables implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -6314592044536252074L;

@EmbeddedId
  private ClMovablesId clMovablesId;

  // 擔保品代號1
  /* 擔保品代號檔CdCl */
  @Column(name = "`ClCode1`", insertable = false, updatable = false)
  private int clCode1 = 0;

  // 擔保品代號2
  /* 擔保品代號檔CdCl */
  @Column(name = "`ClCode2`", insertable = false, updatable = false)
  private int clCode2 = 0;

  // 擔保品編號
  @Column(name = "`ClNo`", insertable = false, updatable = false)
  private int clNo = 0;

  // 所有權人統編
  @Column(name = "`OwnerId`", length = 10)
  private String ownerId;

  // 所有權人姓名
  @Column(name = "`OwnerName`", length = 100)
  private String ownerName;

  // 耐用年限
  @Column(name = "`ServiceLife`")
  private int serviceLife = 0;

  // 形式/規格
  @Column(name = "`ProductSpec`", length = 20)
  private String productSpec;

  // 產品代碼/型號
  @Column(name = "`ProductType`", length = 10)
  private String productType;

  // 品牌/廠牌/船名
  @Column(name = "`ProductBrand`", length = 20)
  private String productBrand;

  // 排氣量
  @Column(name = "`ProductCC`", length = 10)
  private String productCC;

  // 顏色
  @Column(name = "`ProductColor`", length = 10)
  private String productColor;

  // 引擎號碼
  @Column(name = "`EngineSN`", length = 50)
  private String engineSN;

  // 牌照號碼
  @Column(name = "`LicenseNo`", length = 10)
  private String licenseNo;

  // 牌照類別
  /* 共用代碼檔1:自用2:營業 */
  @Column(name = "`LicenseTypeCode`", length = 1)
  private String licenseTypeCode;

  // 牌照用途
  /* 共用代碼檔1:一般2:專用 */
  @Column(name = "`LicenseUsageCode`", length = 1)
  private String licenseUsageCode;

  // 發照日期
  @Column(name = "`LiceneIssueDate`")
  private int liceneIssueDate = 0;

  // 製造年月
  @Column(name = "`MfgYearMonth`")
  private int mfgYearMonth = 0;

  // 車別
  /* 共用代碼檔01:小客車02:大客車03:小貨車04:大貨車05:大貨車(砂石車)06:大貨車(混凝土攪拌車)07:代用大客車08:大型特種車(工程車)09:大型特種車(水肥車)10:大型特種車(垃圾車)11:大型特種車(拖吊車)12:大型特種車(捐血車)13:大型特種車(掃街車)14:大型特種車(救濟車)15:大型特種車(清溝車)16:大型特種車(照明車)17:大型特種車(醫療車)18:大型特種車(灑水車)19:大型特種車(工程救險車)20:大型特種車(高空作業車)21:大型特種車(救助器材車)22:大型特種車(電信傳送車)23:大型特種車(廚餘收集車)24:半拖車25:半拖車(砂石車)26:半拖車(混凝土攪拌車)27:全拖車28:曳引車29:重型機器腳踏車30:特種車(子母式垃圾車)31:特種車(水肥車)32:特種車(水箱消防車)33:特種車(垃圾車)34:特種車(拖吊車)35:特種車(捐血車)36:特種車(高空作業車)37:特種車(掃街車)38:特種車(救助器材車)39:特種車(救濟車)40:特種車(廚餘收集車)41:特種車(醫療車)42:特種車(警備車)43:特種車(灑水車)44:輕型拖車(水上摩托車)45:輕型機器腳踏車 */
  @Column(name = "`VehicleTypeCode`", length = 2)
  private String vehicleTypeCode;

  // 車身樣式
  /* 01.---02.平板式03.伸縮平板式04.伸縮鋼架式05.低床平板式06.柵式07.框式08.高壓罐槽體式09.密封式10.常壓罐槽式11.廂式12.傾卸平板式13.傾卸框式14.傾卸密封式15.槽體式16.廂式17.篷式18.鋼架式19.篷式20.雙廂式21.雙層式22.雙層框式23.攪拌式24.罐式25.罐槽體式 */
  @Column(name = "`VehicleStyleCode`", length = 2)
  private String vehicleStyleCode;

  // 監理站
  /* 共用代碼檔206:臺北區監理所基隆監理站235:臺北區監理所板橋監理站238:臺北區監理所247:臺北區監理所蘆洲監理站268:臺北區監理所宜蘭監理站300:新竹區監理所新竹市監理站305:新竹區監理所320:新竹區監理所中壢監理站330:新竹區監理所桃園監理站360:新竹區監理所苗栗監理站406:臺中區監理所臺中市監理站420:臺中區監理所豐原監理站432:臺中區監理所503:臺中區監理所彰化監理站540:臺中區監理所南投監理站545:臺中區監理所埔里監理分站600:嘉義區監理所635:嘉義區監理所東勢監理分站640:嘉義區監理所雲林監理站700:嘉義區監理所臺南監理站721:嘉義區監理所麻豆監理站730:嘉義區監理所新營監理站830:高雄區監理所842:高雄區監理所旗山監理站880:高雄區監理所澎湖監理站891:金門監理所900:高雄區監理所屏東監理站946:高雄區監理所恆春監理分站950:高雄區監理所臺東監理站973:臺北區監理所花蓮監理站981:臺北區監理所玉里監理分站 */
  @Column(name = "`VehicleOfficeCode`", length = 3)
  private String vehicleOfficeCode;

  // 幣別
  /* TWD:新臺幣 */
  @Column(name = "`Currency`", length = 3)
  private String currency;

  // 匯率
  @Column(name = "`ExchangeRate`")
  private BigDecimal exchangeRate = new BigDecimal("0");

  // 投保註記
  /* Y:是N:否 */
  @Column(name = "`Insurance`", length = 1)
  private String insurance;

  // 貸放成數(%)
  @Column(name = "`LoanToValue`")
  private BigDecimal loanToValue = new BigDecimal("0");

  // 殘值
  @Column(name = "`ScrapValue`")
  private BigDecimal scrapValue = new BigDecimal("0");

  // 抵押權註記
  /* 共用代碼檔0:最高限額抵押權1:普通抵押權 */
  @Column(name = "`MtgCode`", length = 1)
  private String mtgCode;

  // 最高限額抵押權之擔保債權種類-票據
  /* Y:是N:否 */
  @Column(name = "`MtgCheck`", length = 1)
  private String mtgCheck;

  // 最高限額抵押權之擔保債權種類-借款
  /* Y:是N:否 */
  @Column(name = "`MtgLoan`", length = 1)
  private String mtgLoan;

  // 最高限額抵押權之擔保債權種類-保證債務
  /* Y:是N:否 */
  @Column(name = "`MtgPledge`", length = 1)
  private String mtgPledge;

  // 設定狀態
  /* 1:設定2:解除 */
  @Column(name = "`SettingStat`", length = 1)
  private String settingStat;

  // 擔保品狀態
  /* 0:正常1:塗銷2:處分3:抵押權確定 */
  @Column(name = "`ClStat`", length = 1)
  private String clStat;

  // 設定日期
  @Column(name = "`SettingDate`")
  private int settingDate = 0;

  // 抵押設定金額
  @Column(name = "`SettingAmt`")
  private BigDecimal settingAmt = new BigDecimal("0");

  // 收件字號
  @Column(name = "`ReceiptNo`", length = 20)
  private String receiptNo;

  // 抵押登記字號
  @Column(name = "`MtgNo`", length = 20)
  private String mtgNo;

  // 抵押收件日
  @Column(name = "`ReceivedDate`")
  private int receivedDate = 0;

  // 抵押登記起日
  @Column(name = "`MortgageIssueStartDate`")
  private int mortgageIssueStartDate = 0;

  // 抵押登記迄日
  @Column(name = "`MortgageIssueEndDate`")
  private int mortgageIssueEndDate = 0;

  // 備註
  @Column(name = "`Remark`", length = 120)
  private String remark;

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


  public ClMovablesId getClMovablesId() {
    return this.clMovablesId;
  }

  public void setClMovablesId(ClMovablesId clMovablesId) {
    this.clMovablesId = clMovablesId;
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
	* 擔保品編號<br>
	* 
	* @return Integer
	*/
  public int getClNo() {
    return this.clNo;
  }

/**
	* 擔保品編號<br>
	* 
  *
  * @param clNo 擔保品編號
	*/
  public void setClNo(int clNo) {
    this.clNo = clNo;
  }

/**
	* 所有權人統編<br>
	* 
	* @return String
	*/
  public String getOwnerId() {
    return this.ownerId == null ? "" : this.ownerId;
  }

/**
	* 所有權人統編<br>
	* 
  *
  * @param ownerId 所有權人統編
	*/
  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }

/**
	* 所有權人姓名<br>
	* 
	* @return String
	*/
  public String getOwnerName() {
    return this.ownerName == null ? "" : this.ownerName;
  }

/**
	* 所有權人姓名<br>
	* 
  *
  * @param ownerName 所有權人姓名
	*/
  public void setOwnerName(String ownerName) {
    this.ownerName = ownerName;
  }

/**
	* 耐用年限<br>
	* 
	* @return Integer
	*/
  public int getServiceLife() {
    return this.serviceLife;
  }

/**
	* 耐用年限<br>
	* 
  *
  * @param serviceLife 耐用年限
	*/
  public void setServiceLife(int serviceLife) {
    this.serviceLife = serviceLife;
  }

/**
	* 形式/規格<br>
	* 
	* @return String
	*/
  public String getProductSpec() {
    return this.productSpec == null ? "" : this.productSpec;
  }

/**
	* 形式/規格<br>
	* 
  *
  * @param productSpec 形式/規格
	*/
  public void setProductSpec(String productSpec) {
    this.productSpec = productSpec;
  }

/**
	* 產品代碼/型號<br>
	* 
	* @return String
	*/
  public String getProductType() {
    return this.productType == null ? "" : this.productType;
  }

/**
	* 產品代碼/型號<br>
	* 
  *
  * @param productType 產品代碼/型號
	*/
  public void setProductType(String productType) {
    this.productType = productType;
  }

/**
	* 品牌/廠牌/船名<br>
	* 
	* @return String
	*/
  public String getProductBrand() {
    return this.productBrand == null ? "" : this.productBrand;
  }

/**
	* 品牌/廠牌/船名<br>
	* 
  *
  * @param productBrand 品牌/廠牌/船名
	*/
  public void setProductBrand(String productBrand) {
    this.productBrand = productBrand;
  }

/**
	* 排氣量<br>
	* 
	* @return String
	*/
  public String getProductCC() {
    return this.productCC == null ? "" : this.productCC;
  }

/**
	* 排氣量<br>
	* 
  *
  * @param productCC 排氣量
	*/
  public void setProductCC(String productCC) {
    this.productCC = productCC;
  }

/**
	* 顏色<br>
	* 
	* @return String
	*/
  public String getProductColor() {
    return this.productColor == null ? "" : this.productColor;
  }

/**
	* 顏色<br>
	* 
  *
  * @param productColor 顏色
	*/
  public void setProductColor(String productColor) {
    this.productColor = productColor;
  }

/**
	* 引擎號碼<br>
	* 
	* @return String
	*/
  public String getEngineSN() {
    return this.engineSN == null ? "" : this.engineSN;
  }

/**
	* 引擎號碼<br>
	* 
  *
  * @param engineSN 引擎號碼
	*/
  public void setEngineSN(String engineSN) {
    this.engineSN = engineSN;
  }

/**
	* 牌照號碼<br>
	* 
	* @return String
	*/
  public String getLicenseNo() {
    return this.licenseNo == null ? "" : this.licenseNo;
  }

/**
	* 牌照號碼<br>
	* 
  *
  * @param licenseNo 牌照號碼
	*/
  public void setLicenseNo(String licenseNo) {
    this.licenseNo = licenseNo;
  }

/**
	* 牌照類別<br>
	* 共用代碼檔
1:自用
2:營業
	* @return String
	*/
  public String getLicenseTypeCode() {
    return this.licenseTypeCode == null ? "" : this.licenseTypeCode;
  }

/**
	* 牌照類別<br>
	* 共用代碼檔
1:自用
2:營業
  *
  * @param licenseTypeCode 牌照類別
	*/
  public void setLicenseTypeCode(String licenseTypeCode) {
    this.licenseTypeCode = licenseTypeCode;
  }

/**
	* 牌照用途<br>
	* 共用代碼檔
1:一般
2:專用
	* @return String
	*/
  public String getLicenseUsageCode() {
    return this.licenseUsageCode == null ? "" : this.licenseUsageCode;
  }

/**
	* 牌照用途<br>
	* 共用代碼檔
1:一般
2:專用
  *
  * @param licenseUsageCode 牌照用途
	*/
  public void setLicenseUsageCode(String licenseUsageCode) {
    this.licenseUsageCode = licenseUsageCode;
  }

/**
	* 發照日期<br>
	* 
	* @return Integer
	*/
  public int getLiceneIssueDate() {
    return StaticTool.bcToRoc(this.liceneIssueDate);
  }

/**
	* 發照日期<br>
	* 
  *
  * @param liceneIssueDate 發照日期
  * @throws LogicException when Date Is Warn	*/
  public void setLiceneIssueDate(int liceneIssueDate) throws LogicException {
    this.liceneIssueDate = StaticTool.rocToBc(liceneIssueDate);
  }

/**
	* 製造年月<br>
	* 
	* @return Integer
	*/
  public int getMfgYearMonth() {
    return this.mfgYearMonth;
  }

/**
	* 製造年月<br>
	* 
  *
  * @param mfgYearMonth 製造年月
	*/
  public void setMfgYearMonth(int mfgYearMonth) {
    this.mfgYearMonth = mfgYearMonth;
  }

/**
	* 車別<br>
	* 共用代碼檔
01:小客車
02:大客車
03:小貨車
04:大貨車
05:大貨車(砂石車)
06:大貨車(混凝土攪拌車)
07:代用大客車
08:大型特種車(工程車)
09:大型特種車(水肥車)
10:大型特種車(垃圾車)
11:大型特種車(拖吊車)
12:大型特種車(捐血車)
13:大型特種車(掃街車)
14:大型特種車(救濟車)
15:大型特種車(清溝車)
16:大型特種車(照明車)
17:大型特種車(醫療車)
18:大型特種車(灑水車)
19:大型特種車(工程救險車)
20:大型特種車(高空作業車)
21:大型特種車(救助器材車)
22:大型特種車(電信傳送車)
23:大型特種車(廚餘收集車)
24:半拖車
25:半拖車(砂石車)
26:半拖車(混凝土攪拌車)
27:全拖車
28:曳引車
29:重型機器腳踏車
30:特種車(子母式垃圾車)
31:特種車(水肥車)
32:特種車(水箱消防車)
33:特種車(垃圾車)
34:特種車(拖吊車)
35:特種車(捐血車)
36:特種車(高空作業車)
37:特種車(掃街車)
38:特種車(救助器材車)
39:特種車(救濟車)
40:特種車(廚餘收集車)
41:特種車(醫療車)
42:特種車(警備車)
43:特種車(灑水車)
44:輕型拖車(水上摩托車)
45:輕型機器腳踏車
	* @return String
	*/
  public String getVehicleTypeCode() {
    return this.vehicleTypeCode == null ? "" : this.vehicleTypeCode;
  }

/**
	* 車別<br>
	* 共用代碼檔
01:小客車
02:大客車
03:小貨車
04:大貨車
05:大貨車(砂石車)
06:大貨車(混凝土攪拌車)
07:代用大客車
08:大型特種車(工程車)
09:大型特種車(水肥車)
10:大型特種車(垃圾車)
11:大型特種車(拖吊車)
12:大型特種車(捐血車)
13:大型特種車(掃街車)
14:大型特種車(救濟車)
15:大型特種車(清溝車)
16:大型特種車(照明車)
17:大型特種車(醫療車)
18:大型特種車(灑水車)
19:大型特種車(工程救險車)
20:大型特種車(高空作業車)
21:大型特種車(救助器材車)
22:大型特種車(電信傳送車)
23:大型特種車(廚餘收集車)
24:半拖車
25:半拖車(砂石車)
26:半拖車(混凝土攪拌車)
27:全拖車
28:曳引車
29:重型機器腳踏車
30:特種車(子母式垃圾車)
31:特種車(水肥車)
32:特種車(水箱消防車)
33:特種車(垃圾車)
34:特種車(拖吊車)
35:特種車(捐血車)
36:特種車(高空作業車)
37:特種車(掃街車)
38:特種車(救助器材車)
39:特種車(救濟車)
40:特種車(廚餘收集車)
41:特種車(醫療車)
42:特種車(警備車)
43:特種車(灑水車)
44:輕型拖車(水上摩托車)
45:輕型機器腳踏車
  *
  * @param vehicleTypeCode 車別
	*/
  public void setVehicleTypeCode(String vehicleTypeCode) {
    this.vehicleTypeCode = vehicleTypeCode;
  }

/**
	* 車身樣式<br>
	* 01.---
02.平板式
03.伸縮平板式
04.伸縮鋼架式
05.低床平板式
06.柵式
07.框式
08.高壓罐槽體式
09.密封式
10.常壓罐槽式
11.廂式
12.傾卸平板式
13.傾卸框式
14.傾卸密封式
15.槽體式
16.廂式
17.篷式
18.鋼架式
19.篷式
20.雙廂式
21.雙層式
22.雙層框式
23.攪拌式
24.罐式
25.罐槽體式
	* @return String
	*/
  public String getVehicleStyleCode() {
    return this.vehicleStyleCode == null ? "" : this.vehicleStyleCode;
  }

/**
	* 車身樣式<br>
	* 01.---
02.平板式
03.伸縮平板式
04.伸縮鋼架式
05.低床平板式
06.柵式
07.框式
08.高壓罐槽體式
09.密封式
10.常壓罐槽式
11.廂式
12.傾卸平板式
13.傾卸框式
14.傾卸密封式
15.槽體式
16.廂式
17.篷式
18.鋼架式
19.篷式
20.雙廂式
21.雙層式
22.雙層框式
23.攪拌式
24.罐式
25.罐槽體式
  *
  * @param vehicleStyleCode 車身樣式
	*/
  public void setVehicleStyleCode(String vehicleStyleCode) {
    this.vehicleStyleCode = vehicleStyleCode;
  }

/**
	* 監理站<br>
	* 共用代碼檔
206:臺北區監理所基隆監理站
235:臺北區監理所板橋監理站
238:臺北區監理所
247:臺北區監理所蘆洲監理站
268:臺北區監理所宜蘭監理站
300:新竹區監理所新竹市監理站
305:新竹區監理所
320:新竹區監理所中壢監理站
330:新竹區監理所桃園監理站
360:新竹區監理所苗栗監理站
406:臺中區監理所臺中市監理站
420:臺中區監理所豐原監理站
432:臺中區監理所
503:臺中區監理所彰化監理站
540:臺中區監理所南投監理站
545:臺中區監理所埔里監理分站
600:嘉義區監理所
635:嘉義區監理所東勢監理分站
640:嘉義區監理所雲林監理站
700:嘉義區監理所臺南監理站
721:嘉義區監理所麻豆監理站
730:嘉義區監理所新營監理站
830:高雄區監理所
842:高雄區監理所旗山監理站
880:高雄區監理所澎湖監理站
891:金門監理所
900:高雄區監理所屏東監理站
946:高雄區監理所恆春監理分站
950:高雄區監理所臺東監理站
973:臺北區監理所花蓮監理站
981:臺北區監理所玉里監理分站
	* @return String
	*/
  public String getVehicleOfficeCode() {
    return this.vehicleOfficeCode == null ? "" : this.vehicleOfficeCode;
  }

/**
	* 監理站<br>
	* 共用代碼檔
206:臺北區監理所基隆監理站
235:臺北區監理所板橋監理站
238:臺北區監理所
247:臺北區監理所蘆洲監理站
268:臺北區監理所宜蘭監理站
300:新竹區監理所新竹市監理站
305:新竹區監理所
320:新竹區監理所中壢監理站
330:新竹區監理所桃園監理站
360:新竹區監理所苗栗監理站
406:臺中區監理所臺中市監理站
420:臺中區監理所豐原監理站
432:臺中區監理所
503:臺中區監理所彰化監理站
540:臺中區監理所南投監理站
545:臺中區監理所埔里監理分站
600:嘉義區監理所
635:嘉義區監理所東勢監理分站
640:嘉義區監理所雲林監理站
700:嘉義區監理所臺南監理站
721:嘉義區監理所麻豆監理站
730:嘉義區監理所新營監理站
830:高雄區監理所
842:高雄區監理所旗山監理站
880:高雄區監理所澎湖監理站
891:金門監理所
900:高雄區監理所屏東監理站
946:高雄區監理所恆春監理分站
950:高雄區監理所臺東監理站
973:臺北區監理所花蓮監理站
981:臺北區監理所玉里監理分站
  *
  * @param vehicleOfficeCode 監理站
	*/
  public void setVehicleOfficeCode(String vehicleOfficeCode) {
    this.vehicleOfficeCode = vehicleOfficeCode;
  }

/**
	* 幣別<br>
	* TWD:新臺幣
	* @return String
	*/
  public String getCurrency() {
    return this.currency == null ? "" : this.currency;
  }

/**
	* 幣別<br>
	* TWD:新臺幣
  *
  * @param currency 幣別
	*/
  public void setCurrency(String currency) {
    this.currency = currency;
  }

/**
	* 匯率<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getExchangeRate() {
    return this.exchangeRate;
  }

/**
	* 匯率<br>
	* 
  *
  * @param exchangeRate 匯率
	*/
  public void setExchangeRate(BigDecimal exchangeRate) {
    this.exchangeRate = exchangeRate;
  }

/**
	* 投保註記<br>
	* Y:是
N:否
	* @return String
	*/
  public String getInsurance() {
    return this.insurance == null ? "" : this.insurance;
  }

/**
	* 投保註記<br>
	* Y:是
N:否
  *
  * @param insurance 投保註記
	*/
  public void setInsurance(String insurance) {
    this.insurance = insurance;
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
	* 殘值<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getScrapValue() {
    return this.scrapValue;
  }

/**
	* 殘值<br>
	* 
  *
  * @param scrapValue 殘值
	*/
  public void setScrapValue(BigDecimal scrapValue) {
    this.scrapValue = scrapValue;
  }

/**
	* 抵押權註記<br>
	* 共用代碼檔
0:最高限額抵押權
1:普通抵押權
	* @return String
	*/
  public String getMtgCode() {
    return this.mtgCode == null ? "" : this.mtgCode;
  }

/**
	* 抵押權註記<br>
	* 共用代碼檔
0:最高限額抵押權
1:普通抵押權
  *
  * @param mtgCode 抵押權註記
	*/
  public void setMtgCode(String mtgCode) {
    this.mtgCode = mtgCode;
  }

/**
	* 最高限額抵押權之擔保債權種類-票據<br>
	* Y:是
N:否
	* @return String
	*/
  public String getMtgCheck() {
    return this.mtgCheck == null ? "" : this.mtgCheck;
  }

/**
	* 最高限額抵押權之擔保債權種類-票據<br>
	* Y:是
N:否
  *
  * @param mtgCheck 最高限額抵押權之擔保債權種類-票據
	*/
  public void setMtgCheck(String mtgCheck) {
    this.mtgCheck = mtgCheck;
  }

/**
	* 最高限額抵押權之擔保債權種類-借款<br>
	* Y:是
N:否
	* @return String
	*/
  public String getMtgLoan() {
    return this.mtgLoan == null ? "" : this.mtgLoan;
  }

/**
	* 最高限額抵押權之擔保債權種類-借款<br>
	* Y:是
N:否
  *
  * @param mtgLoan 最高限額抵押權之擔保債權種類-借款
	*/
  public void setMtgLoan(String mtgLoan) {
    this.mtgLoan = mtgLoan;
  }

/**
	* 最高限額抵押權之擔保債權種類-保證債務<br>
	* Y:是
N:否
	* @return String
	*/
  public String getMtgPledge() {
    return this.mtgPledge == null ? "" : this.mtgPledge;
  }

/**
	* 最高限額抵押權之擔保債權種類-保證債務<br>
	* Y:是
N:否
  *
  * @param mtgPledge 最高限額抵押權之擔保債權種類-保證債務
	*/
  public void setMtgPledge(String mtgPledge) {
    this.mtgPledge = mtgPledge;
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
	* 設定日期<br>
	* 
	* @return Integer
	*/
  public int getSettingDate() {
    return StaticTool.bcToRoc(this.settingDate);
  }

/**
	* 設定日期<br>
	* 
  *
  * @param settingDate 設定日期
  * @throws LogicException when Date Is Warn	*/
  public void setSettingDate(int settingDate) throws LogicException {
    this.settingDate = StaticTool.rocToBc(settingDate);
  }

/**
	* 抵押設定金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getSettingAmt() {
    return this.settingAmt;
  }

/**
	* 抵押設定金額<br>
	* 
  *
  * @param settingAmt 抵押設定金額
	*/
  public void setSettingAmt(BigDecimal settingAmt) {
    this.settingAmt = settingAmt;
  }

/**
	* 收件字號<br>
	* 
	* @return String
	*/
  public String getReceiptNo() {
    return this.receiptNo == null ? "" : this.receiptNo;
  }

/**
	* 收件字號<br>
	* 
  *
  * @param receiptNo 收件字號
	*/
  public void setReceiptNo(String receiptNo) {
    this.receiptNo = receiptNo;
  }

/**
	* 抵押登記字號<br>
	* 
	* @return String
	*/
  public String getMtgNo() {
    return this.mtgNo == null ? "" : this.mtgNo;
  }

/**
	* 抵押登記字號<br>
	* 
  *
  * @param mtgNo 抵押登記字號
	*/
  public void setMtgNo(String mtgNo) {
    this.mtgNo = mtgNo;
  }

/**
	* 抵押收件日<br>
	* 
	* @return Integer
	*/
  public int getReceivedDate() {
    return StaticTool.bcToRoc(this.receivedDate);
  }

/**
	* 抵押收件日<br>
	* 
  *
  * @param receivedDate 抵押收件日
  * @throws LogicException when Date Is Warn	*/
  public void setReceivedDate(int receivedDate) throws LogicException {
    this.receivedDate = StaticTool.rocToBc(receivedDate);
  }

/**
	* 抵押登記起日<br>
	* 
	* @return Integer
	*/
  public int getMortgageIssueStartDate() {
    return StaticTool.bcToRoc(this.mortgageIssueStartDate);
  }

/**
	* 抵押登記起日<br>
	* 
  *
  * @param mortgageIssueStartDate 抵押登記起日
  * @throws LogicException when Date Is Warn	*/
  public void setMortgageIssueStartDate(int mortgageIssueStartDate) throws LogicException {
    this.mortgageIssueStartDate = StaticTool.rocToBc(mortgageIssueStartDate);
  }

/**
	* 抵押登記迄日<br>
	* 
	* @return Integer
	*/
  public int getMortgageIssueEndDate() {
    return StaticTool.bcToRoc(this.mortgageIssueEndDate);
  }

/**
	* 抵押登記迄日<br>
	* 
  *
  * @param mortgageIssueEndDate 抵押登記迄日
  * @throws LogicException when Date Is Warn	*/
  public void setMortgageIssueEndDate(int mortgageIssueEndDate) throws LogicException {
    this.mortgageIssueEndDate = StaticTool.rocToBc(mortgageIssueEndDate);
  }

/**
	* 備註<br>
	* 
	* @return String
	*/
  public String getRemark() {
    return this.remark == null ? "" : this.remark;
  }

/**
	* 備註<br>
	* 
  *
  * @param remark 備註
	*/
  public void setRemark(String remark) {
    this.remark = remark;
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
    return "ClMovables [clMovablesId=" + clMovablesId + ", ownerId=" + ownerId + ", ownerName=" + ownerName + ", serviceLife=" + serviceLife
           + ", productSpec=" + productSpec + ", productType=" + productType + ", productBrand=" + productBrand + ", productCC=" + productCC + ", productColor=" + productColor + ", engineSN=" + engineSN
           + ", licenseNo=" + licenseNo + ", licenseTypeCode=" + licenseTypeCode + ", licenseUsageCode=" + licenseUsageCode + ", liceneIssueDate=" + liceneIssueDate + ", mfgYearMonth=" + mfgYearMonth + ", vehicleTypeCode=" + vehicleTypeCode
           + ", vehicleStyleCode=" + vehicleStyleCode + ", vehicleOfficeCode=" + vehicleOfficeCode + ", currency=" + currency + ", exchangeRate=" + exchangeRate + ", insurance=" + insurance + ", loanToValue=" + loanToValue
           + ", scrapValue=" + scrapValue + ", mtgCode=" + mtgCode + ", mtgCheck=" + mtgCheck + ", mtgLoan=" + mtgLoan + ", mtgPledge=" + mtgPledge + ", settingStat=" + settingStat
           + ", clStat=" + clStat + ", settingDate=" + settingDate + ", settingAmt=" + settingAmt + ", receiptNo=" + receiptNo + ", mtgNo=" + mtgNo + ", receivedDate=" + receivedDate
           + ", mortgageIssueStartDate=" + mortgageIssueStartDate + ", mortgageIssueEndDate=" + mortgageIssueEndDate + ", remark=" + remark + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
           + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
