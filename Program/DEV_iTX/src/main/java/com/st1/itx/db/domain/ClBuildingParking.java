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
 * ClBuildingParking 擔保品-建物獨立產權車位檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`ClBuildingParking`")
public class ClBuildingParking implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 7628537108696312326L;

@EmbeddedId
  private ClBuildingParkingId clBuildingParkingId;

  // 擔保品-代號1
  @Column(name = "`ClCode1`", insertable = false, updatable = false)
  private int clCode1 = 0;

  // 擔保品-代號2
  @Column(name = "`ClCode2`", insertable = false, updatable = false)
  private int clCode2 = 0;

  // 擔保品編號
  @Column(name = "`ClNo`", insertable = false, updatable = false)
  private int clNo = 0;

  // 車位建號
  /* 建號格式為5-3 */
  @Column(name = "`ParkingBdNo1`", insertable = false, updatable = false)
  private int parkingBdNo1 = 0;

  // 車位建號(子號)
  /* 建號格式為5-3 */
  @Column(name = "`ParkingBdNo2`", insertable = false, updatable = false)
  private int parkingBdNo2 = 0;

  // 登記面積(坪)
  @Column(name = "`Area`")
  private BigDecimal area = new BigDecimal("0");

  // 價格(元)
  @Column(name = "`Amt`")
  private BigDecimal amt = new BigDecimal("0");

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


  public ClBuildingParkingId getClBuildingParkingId() {
    return this.clBuildingParkingId;
  }

  public void setClBuildingParkingId(ClBuildingParkingId clBuildingParkingId) {
    this.clBuildingParkingId = clBuildingParkingId;
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
	* 車位建號<br>
	* 建號格式為5-3
	* @return Integer
	*/
  public int getParkingBdNo1() {
    return this.parkingBdNo1;
  }

/**
	* 車位建號<br>
	* 建號格式為5-3
  *
  * @param parkingBdNo1 車位建號
	*/
  public void setParkingBdNo1(int parkingBdNo1) {
    this.parkingBdNo1 = parkingBdNo1;
  }

/**
	* 車位建號(子號)<br>
	* 建號格式為5-3
	* @return Integer
	*/
  public int getParkingBdNo2() {
    return this.parkingBdNo2;
  }

/**
	* 車位建號(子號)<br>
	* 建號格式為5-3
  *
  * @param parkingBdNo2 車位建號(子號)
	*/
  public void setParkingBdNo2(int parkingBdNo2) {
    this.parkingBdNo2 = parkingBdNo2;
  }

/**
	* 登記面積(坪)<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getArea() {
    return this.area;
  }

/**
	* 登記面積(坪)<br>
	* 
  *
  * @param area 登記面積(坪)
	*/
  public void setArea(BigDecimal area) {
    this.area = area;
  }

/**
	* 價格(元)<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getAmt() {
    return this.amt;
  }

/**
	* 價格(元)<br>
	* 
  *
  * @param amt 價格(元)
	*/
  public void setAmt(BigDecimal amt) {
    this.amt = amt;
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
    return "ClBuildingParking [clBuildingParkingId=" + clBuildingParkingId + ", area=" + area
           + ", amt=" + amt + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
