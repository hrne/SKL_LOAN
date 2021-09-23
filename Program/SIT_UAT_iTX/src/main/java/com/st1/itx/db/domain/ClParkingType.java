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
 * ClParkingType 擔保品-停車位型式檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`ClParkingType`")
public class ClParkingType implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -5534298441456772634L;

@EmbeddedId
  private ClParkingTypeId clParkingTypeId;

  // 擔保品-代號1
  @Column(name = "`ClCode1`", insertable = false, updatable = false)
  private int clCode1 = 0;

  // 擔保品-代號2
  @Column(name = "`ClCode2`", insertable = false, updatable = false)
  private int clCode2 = 0;

  // 擔保品編號
  @Column(name = "`ClNo`", insertable = false, updatable = false)
  private int clNo = 0;

  // 停車位型式
  @Column(name = "`ParkingTypeCode`", length = 1, insertable = false, updatable = false)
  private String parkingTypeCode;

  // 車位數量
  @Column(name = "`ParkingQty`")
  private int parkingQty = 0;

  // 車位面積(坪)
  @Column(name = "`ParkingArea`")
  private BigDecimal parkingArea = new BigDecimal("0");

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


  public ClParkingTypeId getClParkingTypeId() {
    return this.clParkingTypeId;
  }

  public void setClParkingTypeId(ClParkingTypeId clParkingTypeId) {
    this.clParkingTypeId = clParkingTypeId;
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
	* 停車位型式<br>
	* 
	* @return String
	*/
  public String getParkingTypeCode() {
    return this.parkingTypeCode == null ? "" : this.parkingTypeCode;
  }

/**
	* 停車位型式<br>
	* 
  *
  * @param parkingTypeCode 停車位型式
	*/
  public void setParkingTypeCode(String parkingTypeCode) {
    this.parkingTypeCode = parkingTypeCode;
  }

/**
	* 車位數量<br>
	* 
	* @return Integer
	*/
  public int getParkingQty() {
    return this.parkingQty;
  }

/**
	* 車位數量<br>
	* 
  *
  * @param parkingQty 車位數量
	*/
  public void setParkingQty(int parkingQty) {
    this.parkingQty = parkingQty;
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
    return "ClParkingType [clParkingTypeId=" + clParkingTypeId + ", parkingQty=" + parkingQty + ", parkingArea=" + parkingArea
           + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
