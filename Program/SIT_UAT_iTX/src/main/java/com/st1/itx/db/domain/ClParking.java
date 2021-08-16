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
 * ClParking 擔保品-車位資料檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`ClParking`")
public class ClParking implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -900232226695219835L;

@EmbeddedId
  private ClParkingId clParkingId;

  // 擔保品-代號1
  @Column(name = "`ClCode1`", insertable = false, updatable = false)
  private int clCode1 = 0;

  // 擔保品-代號2
  @Column(name = "`ClCode2`", insertable = false, updatable = false)
  private int clCode2 = 0;

  // 擔保品編號
  @Column(name = "`ClNo`", insertable = false, updatable = false)
  private int clNo = 0;

  // 車位資料序號
  @Column(name = "`ParkingSeqNo`", insertable = false, updatable = false)
  private int parkingSeqNo = 0;

  // 車位編號
  /* 停車位編號備註*資料轉換時預設"." */
  @Column(name = "`ParkingNo`", length = 20)
  private String parkingNo;

  // 車位數量
  /* 獨立產權車位時只能為為1 */
  @Column(name = "`ParkingQty`")
  private int parkingQty = 0;

  // 停車位型式
  /* CdCode.ParkingTypeCode */
  @Column(name = "`ParkingTypeCode`", length = 1)
  private String parkingTypeCode;

  // 持份比率(分子)
  @Column(name = "`OwnerPart`")
  private BigDecimal ownerPart = new BigDecimal("0");

  // 持份比率(分母)
  @Column(name = "`OwnerTotal`")
  private BigDecimal ownerTotal = new BigDecimal("0");

  // 縣市
  /* 地區別與鄉鎮區對照檔CdArea */
  @Column(name = "`CityCode`", length = 2)
  private String cityCode;

  // 鄉鎮市區
  /* 地區別與鄉鎮區對照檔CdArea */
  @Column(name = "`AreaCode`", length = 3)
  private String areaCode;

  // 段小段代碼
  /* 地段代碼檔CdLandSection */
  @Column(name = "`IrCode`", length = 5)
  private String irCode;

  // 建號
  @Column(name = "`BdNo1`", length = 5)
  private String bdNo1;

  // 建號(子號)
  @Column(name = "`BdNo2`", length = 3)
  private String bdNo2;

  // 地號
  /* 地號格式為4-4 */
  @Column(name = "`LandNo1`", length = 4)
  private String landNo1;

  // 地號(子號)
  /* 地號格式為4-4 */
  @Column(name = "`LandNo2`", length = 4)
  private String landNo2;

  // 車位面積(坪)
  @Column(name = "`ParkingArea`")
  private BigDecimal parkingArea = new BigDecimal("0");

  // 價格(元)
  @Column(name = "`Amount`")
  private BigDecimal amount = new BigDecimal("0");

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


  public ClParkingId getClParkingId() {
    return this.clParkingId;
  }

  public void setClParkingId(ClParkingId clParkingId) {
    this.clParkingId = clParkingId;
  }

/**
	* 擔保品-代號1<br>
	* 
	* @return Integer
	*/
  public int getClCode1() {
    return this.clCode1;
  }

/**
	* 擔保品-代號1<br>
	* 
  *
  * @param clCode1 擔保品-代號1
	*/
  public void setClCode1(int clCode1) {
    this.clCode1 = clCode1;
  }

/**
	* 擔保品-代號2<br>
	* 
	* @return Integer
	*/
  public int getClCode2() {
    return this.clCode2;
  }

/**
	* 擔保品-代號2<br>
	* 
  *
  * @param clCode2 擔保品-代號2
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
	* 車位資料序號<br>
	* 
	* @return Integer
	*/
  public int getParkingSeqNo() {
    return this.parkingSeqNo;
  }

/**
	* 車位資料序號<br>
	* 
  *
  * @param parkingSeqNo 車位資料序號
	*/
  public void setParkingSeqNo(int parkingSeqNo) {
    this.parkingSeqNo = parkingSeqNo;
  }

/**
	* 車位編號<br>
	* 停車位編號備註
*資料轉換時預設"."
	* @return String
	*/
  public String getParkingNo() {
    return this.parkingNo == null ? "" : this.parkingNo;
  }

/**
	* 車位編號<br>
	* 停車位編號備註
*資料轉換時預設"."
  *
  * @param parkingNo 車位編號
	*/
  public void setParkingNo(String parkingNo) {
    this.parkingNo = parkingNo;
  }

/**
	* 車位數量<br>
	* 獨立產權車位時只能為為1
	* @return Integer
	*/
  public int getParkingQty() {
    return this.parkingQty;
  }

/**
	* 車位數量<br>
	* 獨立產權車位時只能為為1
  *
  * @param parkingQty 車位數量
	*/
  public void setParkingQty(int parkingQty) {
    this.parkingQty = parkingQty;
  }

/**
	* 停車位型式<br>
	* CdCode.ParkingTypeCode
	* @return String
	*/
  public String getParkingTypeCode() {
    return this.parkingTypeCode == null ? "" : this.parkingTypeCode;
  }

/**
	* 停車位型式<br>
	* CdCode.ParkingTypeCode
  *
  * @param parkingTypeCode 停車位型式
	*/
  public void setParkingTypeCode(String parkingTypeCode) {
    this.parkingTypeCode = parkingTypeCode;
  }

/**
	* 持份比率(分子)<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getOwnerPart() {
    return this.ownerPart;
  }

/**
	* 持份比率(分子)<br>
	* 
  *
  * @param ownerPart 持份比率(分子)
	*/
  public void setOwnerPart(BigDecimal ownerPart) {
    this.ownerPart = ownerPart;
  }

/**
	* 持份比率(分母)<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getOwnerTotal() {
    return this.ownerTotal;
  }

/**
	* 持份比率(分母)<br>
	* 
  *
  * @param ownerTotal 持份比率(分母)
	*/
  public void setOwnerTotal(BigDecimal ownerTotal) {
    this.ownerTotal = ownerTotal;
  }

/**
	* 縣市<br>
	* 地區別與鄉鎮區對照檔CdArea
	* @return String
	*/
  public String getCityCode() {
    return this.cityCode == null ? "" : this.cityCode;
  }

/**
	* 縣市<br>
	* 地區別與鄉鎮區對照檔CdArea
  *
  * @param cityCode 縣市
	*/
  public void setCityCode(String cityCode) {
    this.cityCode = cityCode;
  }

/**
	* 鄉鎮市區<br>
	* 地區別與鄉鎮區對照檔CdArea
	* @return String
	*/
  public String getAreaCode() {
    return this.areaCode == null ? "" : this.areaCode;
  }

/**
	* 鄉鎮市區<br>
	* 地區別與鄉鎮區對照檔CdArea
  *
  * @param areaCode 鄉鎮市區
	*/
  public void setAreaCode(String areaCode) {
    this.areaCode = areaCode;
  }

/**
	* 段小段代碼<br>
	* 地段代碼檔CdLandSection
	* @return String
	*/
  public String getIrCode() {
    return this.irCode == null ? "" : this.irCode;
  }

/**
	* 段小段代碼<br>
	* 地段代碼檔CdLandSection
  *
  * @param irCode 段小段代碼
	*/
  public void setIrCode(String irCode) {
    this.irCode = irCode;
  }

/**
	* 建號<br>
	* 
	* @return String
	*/
  public String getBdNo1() {
    return this.bdNo1 == null ? "" : this.bdNo1;
  }

/**
	* 建號<br>
	* 
  *
  * @param bdNo1 建號
	*/
  public void setBdNo1(String bdNo1) {
    this.bdNo1 = bdNo1;
  }

/**
	* 建號(子號)<br>
	* 
	* @return String
	*/
  public String getBdNo2() {
    return this.bdNo2 == null ? "" : this.bdNo2;
  }

/**
	* 建號(子號)<br>
	* 
  *
  * @param bdNo2 建號(子號)
	*/
  public void setBdNo2(String bdNo2) {
    this.bdNo2 = bdNo2;
  }

/**
	* 地號<br>
	* 地號格式為4-4
	* @return String
	*/
  public String getLandNo1() {
    return this.landNo1 == null ? "" : this.landNo1;
  }

/**
	* 地號<br>
	* 地號格式為4-4
  *
  * @param landNo1 地號
	*/
  public void setLandNo1(String landNo1) {
    this.landNo1 = landNo1;
  }

/**
	* 地號(子號)<br>
	* 地號格式為4-4
	* @return String
	*/
  public String getLandNo2() {
    return this.landNo2 == null ? "" : this.landNo2;
  }

/**
	* 地號(子號)<br>
	* 地號格式為4-4
  *
  * @param landNo2 地號(子號)
	*/
  public void setLandNo2(String landNo2) {
    this.landNo2 = landNo2;
  }

/**
	* 車位面積(坪)<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getParkingArea() {
    return this.parkingArea;
  }

/**
	* 車位面積(坪)<br>
	* 
  *
  * @param parkingArea 車位面積(坪)
	*/
  public void setParkingArea(BigDecimal parkingArea) {
    this.parkingArea = parkingArea;
  }

/**
	* 價格(元)<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getAmount() {
    return this.amount;
  }

/**
	* 價格(元)<br>
	* 
  *
  * @param amount 價格(元)
	*/
  public void setAmount(BigDecimal amount) {
    this.amount = amount;
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
    return "ClParking [clParkingId=" + clParkingId + ", parkingNo=" + parkingNo + ", parkingQty=" + parkingQty
           + ", parkingTypeCode=" + parkingTypeCode + ", ownerPart=" + ownerPart + ", ownerTotal=" + ownerTotal + ", cityCode=" + cityCode + ", areaCode=" + areaCode + ", irCode=" + irCode
           + ", bdNo1=" + bdNo1 + ", bdNo2=" + bdNo2 + ", landNo1=" + landNo1 + ", landNo2=" + landNo2 + ", parkingArea=" + parkingArea + ", amount=" + amount
           + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
