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

/**
 * JcicB095 聯徵不動產擔保品明細-建號附加檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicB095`")
public class JcicB095 implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -3656120443075397190L;

@EmbeddedId
  private JcicB095Id jcicB095Id;

  // 資料日期
  @Column(name = "`DataYM`", insertable = false, updatable = false)
  private int dataYM = 0;

  // 資料別
  /* 95:聯徵不動產擔保品明細建號附加檔資料 */
  @Column(name = "`DataType`", length = 2)
  private String dataType;

  // 總行代號
  /* Key,金融機構總機構之代號，三位數字 */
  @Column(name = "`BankItem`", length = 3)
  private String bankItem;

  // 分行代號
  /* Key,金融機構分支機構之代號，四位數字 */
  @Column(name = "`BranchItem`", length = 4)
  private String branchItem;

  // 空白
  /* 空白 */
  @Column(name = "`Filler4`", length = 2)
  private String filler4;

  // 擔保品控制編碼
  /* Key,左靠右補空白 */
  @Column(name = "`ClActNo`", length = 50, insertable = false, updatable = false)
  private String clActNo;

  // 擔保品所有權人或代表人IDN/BAN
  /* 左靠，擔保品所有權人或代表人身份證或統一證號 */
  @Column(name = "`OwnerId`", length = 10, insertable = false, updatable = false)
  private String ownerId;

  // 縣市別
  /* Key, */
  @Column(name = "`CityJCICCode`", length = 1, insertable = false, updatable = false)
  private String cityJCICCode;

  // 鄉鎮市區別
  /* Key, */
  @Column(name = "`AreaJCICCode`", insertable = false, updatable = false)
  private int areaJCICCode = 0;

  // 段、小段號
  /* Key, */
  @Column(name = "`IrCode`", length = 4, insertable = false, updatable = false)
  private String irCode;

  // 建號-前五碼
  /* Key, */
  @Column(name = "`BdNo1`", insertable = false, updatable = false)
  private int bdNo1 = 0;

  // 建號-後三碼
  /* Key, */
  @Column(name = "`BdNo2`", insertable = false, updatable = false)
  private int bdNo2 = 0;

  // 縣市名稱
  @Column(name = "`CityName`", length = 36)
  private String cityName;

  // 鄉鎮市區名稱
  @Column(name = "`AreaName`", length = 36)
  private String areaName;

  // 村里/街路/段/巷/弄/號/樓
  @Column(name = "`Addr`", length = 228)
  private String addr;

  // 主要用途
  @Column(name = "`BdMainUseCode`", length = 1)
  private String bdMainUseCode;

  // 主要建材(結構體)
  @Column(name = "`BdMtrlCode`", length = 1)
  private String bdMtrlCode;

  // 附屬建物用途
  @Column(name = "`BdSubUsageCode`", length = 6)
  private String bdSubUsageCode;

  // 層數(標的所在樓高)
  @Column(name = "`TotalFloor`")
  private int totalFloor = 0;

  // 層次(標的所在樓層)
  @Column(name = "`FloorNo`", length = 7)
  private String floorNo;

  // 建築完成日期(屋齡)
  /* YYYMMDD */
  @Column(name = "`BdDate`", length = 7)
  private String bdDate;

  // 建物總面積
  /* 單位:平方公尺，填至小數點2位 */
  @Column(name = "`TotalArea`")
  private BigDecimal totalArea = new BigDecimal("0");

  // 主建物(層次)面積
  /* 單位:平方公尺，填至小數點2位 */
  @Column(name = "`FloorArea`")
  private BigDecimal floorArea = new BigDecimal("0");

  // 附屬建物面積
  /* 單位:平方公尺，填至小數點2位 */
  @Column(name = "`BdSubArea`")
  private BigDecimal bdSubArea = new BigDecimal("0");

  // 共同部份持分面積
  /* 單位:平方公尺，填至小數點2位 */
  @Column(name = "`PublicArea`")
  private BigDecimal publicArea = new BigDecimal("0");

  // 空白
  /* 空白 */
  @Column(name = "`Filler33`", length = 44)
  private String filler33;

  // 資料所屬年月
  /* 請填報本筆授信資料所屬年月(民國年) */
  @Column(name = "`JcicDataYM`")
  private int jcicDataYM = 0;

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


  public JcicB095Id getJcicB095Id() {
    return this.jcicB095Id;
  }

  public void setJcicB095Id(JcicB095Id jcicB095Id) {
    this.jcicB095Id = jcicB095Id;
  }

/**
	* 資料日期<br>
	* 
	* @return Integer
	*/
  public int getDataYM() {
    return this.dataYM;
  }

/**
	* 資料日期<br>
	* 
  *
  * @param dataYM 資料日期
	*/
  public void setDataYM(int dataYM) {
    this.dataYM = dataYM;
  }

/**
	* 資料別<br>
	* 95:聯徵不動產擔保品明細建號附加檔資料
	* @return String
	*/
  public String getDataType() {
    return this.dataType == null ? "" : this.dataType;
  }

/**
	* 資料別<br>
	* 95:聯徵不動產擔保品明細建號附加檔資料
  *
  * @param dataType 資料別
	*/
  public void setDataType(String dataType) {
    this.dataType = dataType;
  }

/**
	* 總行代號<br>
	* Key,金融機構總機構之代號，三位數字
	* @return String
	*/
  public String getBankItem() {
    return this.bankItem == null ? "" : this.bankItem;
  }

/**
	* 總行代號<br>
	* Key,金融機構總機構之代號，三位數字
  *
  * @param bankItem 總行代號
	*/
  public void setBankItem(String bankItem) {
    this.bankItem = bankItem;
  }

/**
	* 分行代號<br>
	* Key,金融機構分支機構之代號，四位數字
	* @return String
	*/
  public String getBranchItem() {
    return this.branchItem == null ? "" : this.branchItem;
  }

/**
	* 分行代號<br>
	* Key,金融機構分支機構之代號，四位數字
  *
  * @param branchItem 分行代號
	*/
  public void setBranchItem(String branchItem) {
    this.branchItem = branchItem;
  }

/**
	* 空白<br>
	* 空白
	* @return String
	*/
  public String getFiller4() {
    return this.filler4 == null ? "" : this.filler4;
  }

/**
	* 空白<br>
	* 空白
  *
  * @param filler4 空白
	*/
  public void setFiller4(String filler4) {
    this.filler4 = filler4;
  }

/**
	* 擔保品控制編碼<br>
	* Key,左靠右補空白
	* @return String
	*/
  public String getClActNo() {
    return this.clActNo == null ? "" : this.clActNo;
  }

/**
	* 擔保品控制編碼<br>
	* Key,左靠右補空白
  *
  * @param clActNo 擔保品控制編碼
	*/
  public void setClActNo(String clActNo) {
    this.clActNo = clActNo;
  }

/**
	* 擔保品所有權人或代表人IDN/BAN<br>
	* 左靠，擔保品所有權人或代表人身份證或統一證號
	* @return String
	*/
  public String getOwnerId() {
    return this.ownerId == null ? "" : this.ownerId;
  }

/**
	* 擔保品所有權人或代表人IDN/BAN<br>
	* 左靠，擔保品所有權人或代表人身份證或統一證號
  *
  * @param ownerId 擔保品所有權人或代表人IDN/BAN
	*/
  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }

/**
	* 縣市別<br>
	* Key,
	* @return String
	*/
  public String getCityJCICCode() {
    return this.cityJCICCode == null ? "" : this.cityJCICCode;
  }

/**
	* 縣市別<br>
	* Key,
  *
  * @param cityJCICCode 縣市別
	*/
  public void setCityJCICCode(String cityJCICCode) {
    this.cityJCICCode = cityJCICCode;
  }

/**
	* 鄉鎮市區別<br>
	* Key,
	* @return Integer
	*/
  public int getAreaJCICCode() {
    return this.areaJCICCode;
  }

/**
	* 鄉鎮市區別<br>
	* Key,
  *
  * @param areaJCICCode 鄉鎮市區別
	*/
  public void setAreaJCICCode(int areaJCICCode) {
    this.areaJCICCode = areaJCICCode;
  }

/**
	* 段、小段號<br>
	* Key,
	* @return String
	*/
  public String getIrCode() {
    return this.irCode == null ? "" : this.irCode;
  }

/**
	* 段、小段號<br>
	* Key,
  *
  * @param irCode 段、小段號
	*/
  public void setIrCode(String irCode) {
    this.irCode = irCode;
  }

/**
	* 建號-前五碼<br>
	* Key,
	* @return Integer
	*/
  public int getBdNo1() {
    return this.bdNo1;
  }

/**
	* 建號-前五碼<br>
	* Key,
  *
  * @param bdNo1 建號-前五碼
	*/
  public void setBdNo1(int bdNo1) {
    this.bdNo1 = bdNo1;
  }

/**
	* 建號-後三碼<br>
	* Key,
	* @return Integer
	*/
  public int getBdNo2() {
    return this.bdNo2;
  }

/**
	* 建號-後三碼<br>
	* Key,
  *
  * @param bdNo2 建號-後三碼
	*/
  public void setBdNo2(int bdNo2) {
    this.bdNo2 = bdNo2;
  }

/**
	* 縣市名稱<br>
	* 
	* @return String
	*/
  public String getCityName() {
    return this.cityName == null ? "" : this.cityName;
  }

/**
	* 縣市名稱<br>
	* 
  *
  * @param cityName 縣市名稱
	*/
  public void setCityName(String cityName) {
    this.cityName = cityName;
  }

/**
	* 鄉鎮市區名稱<br>
	* 
	* @return String
	*/
  public String getAreaName() {
    return this.areaName == null ? "" : this.areaName;
  }

/**
	* 鄉鎮市區名稱<br>
	* 
  *
  * @param areaName 鄉鎮市區名稱
	*/
  public void setAreaName(String areaName) {
    this.areaName = areaName;
  }

/**
	* 村里/街路/段/巷/弄/號/樓<br>
	* 
	* @return String
	*/
  public String getAddr() {
    return this.addr == null ? "" : this.addr;
  }

/**
	* 村里/街路/段/巷/弄/號/樓<br>
	* 
  *
  * @param addr 村里/街路/段/巷/弄/號/樓
	*/
  public void setAddr(String addr) {
    this.addr = addr;
  }

/**
	* 主要用途<br>
	* 
	* @return String
	*/
  public String getBdMainUseCode() {
    return this.bdMainUseCode == null ? "" : this.bdMainUseCode;
  }

/**
	* 主要用途<br>
	* 
  *
  * @param bdMainUseCode 主要用途
	*/
  public void setBdMainUseCode(String bdMainUseCode) {
    this.bdMainUseCode = bdMainUseCode;
  }

/**
	* 主要建材(結構體)<br>
	* 
	* @return String
	*/
  public String getBdMtrlCode() {
    return this.bdMtrlCode == null ? "" : this.bdMtrlCode;
  }

/**
	* 主要建材(結構體)<br>
	* 
  *
  * @param bdMtrlCode 主要建材(結構體)
	*/
  public void setBdMtrlCode(String bdMtrlCode) {
    this.bdMtrlCode = bdMtrlCode;
  }

/**
	* 附屬建物用途<br>
	* 
	* @return String
	*/
  public String getBdSubUsageCode() {
    return this.bdSubUsageCode == null ? "" : this.bdSubUsageCode;
  }

/**
	* 附屬建物用途<br>
	* 
  *
  * @param bdSubUsageCode 附屬建物用途
	*/
  public void setBdSubUsageCode(String bdSubUsageCode) {
    this.bdSubUsageCode = bdSubUsageCode;
  }

/**
	* 層數(標的所在樓高)<br>
	* 
	* @return Integer
	*/
  public int getTotalFloor() {
    return this.totalFloor;
  }

/**
	* 層數(標的所在樓高)<br>
	* 
  *
  * @param totalFloor 層數(標的所在樓高)
	*/
  public void setTotalFloor(int totalFloor) {
    this.totalFloor = totalFloor;
  }

/**
	* 層次(標的所在樓層)<br>
	* 
	* @return String
	*/
  public String getFloorNo() {
    return this.floorNo == null ? "" : this.floorNo;
  }

/**
	* 層次(標的所在樓層)<br>
	* 
  *
  * @param floorNo 層次(標的所在樓層)
	*/
  public void setFloorNo(String floorNo) {
    this.floorNo = floorNo;
  }

/**
	* 建築完成日期(屋齡)<br>
	* YYYMMDD
	* @return String
	*/
  public String getBdDate() {
    return this.bdDate == null ? "" : this.bdDate;
  }

/**
	* 建築完成日期(屋齡)<br>
	* YYYMMDD
  *
  * @param bdDate 建築完成日期(屋齡)
	*/
  public void setBdDate(String bdDate) {
    this.bdDate = bdDate;
  }

/**
	* 建物總面積<br>
	* 單位:平方公尺，填至小數點2位
	* @return BigDecimal
	*/
  public BigDecimal getTotalArea() {
    return this.totalArea;
  }

/**
	* 建物總面積<br>
	* 單位:平方公尺，填至小數點2位
  *
  * @param totalArea 建物總面積
	*/
  public void setTotalArea(BigDecimal totalArea) {
    this.totalArea = totalArea;
  }

/**
	* 主建物(層次)面積<br>
	* 單位:平方公尺，填至小數點2位
	* @return BigDecimal
	*/
  public BigDecimal getFloorArea() {
    return this.floorArea;
  }

/**
	* 主建物(層次)面積<br>
	* 單位:平方公尺，填至小數點2位
  *
  * @param floorArea 主建物(層次)面積
	*/
  public void setFloorArea(BigDecimal floorArea) {
    this.floorArea = floorArea;
  }

/**
	* 附屬建物面積<br>
	* 單位:平方公尺，填至小數點2位
	* @return BigDecimal
	*/
  public BigDecimal getBdSubArea() {
    return this.bdSubArea;
  }

/**
	* 附屬建物面積<br>
	* 單位:平方公尺，填至小數點2位
  *
  * @param bdSubArea 附屬建物面積
	*/
  public void setBdSubArea(BigDecimal bdSubArea) {
    this.bdSubArea = bdSubArea;
  }

/**
	* 共同部份持分面積<br>
	* 單位:平方公尺，填至小數點2位
	* @return BigDecimal
	*/
  public BigDecimal getPublicArea() {
    return this.publicArea;
  }

/**
	* 共同部份持分面積<br>
	* 單位:平方公尺，填至小數點2位
  *
  * @param publicArea 共同部份持分面積
	*/
  public void setPublicArea(BigDecimal publicArea) {
    this.publicArea = publicArea;
  }

/**
	* 空白<br>
	* 空白
	* @return String
	*/
  public String getFiller33() {
    return this.filler33 == null ? "" : this.filler33;
  }

/**
	* 空白<br>
	* 空白
  *
  * @param filler33 空白
	*/
  public void setFiller33(String filler33) {
    this.filler33 = filler33;
  }

/**
	* 資料所屬年月<br>
	* 請填報本筆授信資料所屬年月(民國年)
	* @return Integer
	*/
  public int getJcicDataYM() {
    return this.jcicDataYM;
  }

/**
	* 資料所屬年月<br>
	* 請填報本筆授信資料所屬年月(民國年)
  *
  * @param jcicDataYM 資料所屬年月
	*/
  public void setJcicDataYM(int jcicDataYM) {
    this.jcicDataYM = jcicDataYM;
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
    return "JcicB095 [jcicB095Id=" + jcicB095Id + ", dataType=" + dataType + ", bankItem=" + bankItem + ", branchItem=" + branchItem + ", filler4=" + filler4
          
           + ", cityName=" + cityName + ", areaName=" + areaName + ", addr=" + addr + ", bdMainUseCode=" + bdMainUseCode + ", bdMtrlCode=" + bdMtrlCode + ", bdSubUsageCode=" + bdSubUsageCode
           + ", totalFloor=" + totalFloor + ", floorNo=" + floorNo + ", bdDate=" + bdDate + ", totalArea=" + totalArea + ", floorArea=" + floorArea + ", bdSubArea=" + bdSubArea
           + ", publicArea=" + publicArea + ", filler33=" + filler33 + ", jcicDataYM=" + jcicDataYM + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
           + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
