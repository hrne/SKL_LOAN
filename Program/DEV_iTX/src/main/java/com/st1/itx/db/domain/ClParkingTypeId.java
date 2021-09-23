package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * ClParkingType 擔保品-停車位型式檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class ClParkingTypeId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 5960799945803076164L;

// 擔保品-代號1
  @Column(name = "`ClCode1`")
  private int clCode1 = 0;

  // 擔保品-代號2
  @Column(name = "`ClCode2`")
  private int clCode2 = 0;

  // 擔保品編號
  @Column(name = "`ClNo`")
  private int clNo = 0;

  // 停車位型式
  @Column(name = "`ParkingTypeCode`", length = 1)
  private String parkingTypeCode = " ";

  public ClParkingTypeId() {
  }

  public ClParkingTypeId(int clCode1, int clCode2, int clNo, String parkingTypeCode) {
    this.clCode1 = clCode1;
    this.clCode2 = clCode2;
    this.clNo = clNo;
    this.parkingTypeCode = parkingTypeCode;
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


  @Override
  public int hashCode() {
    return Objects.hash(clCode1, clCode2, clNo, parkingTypeCode);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    ClParkingTypeId clParkingTypeId = (ClParkingTypeId) obj;
    return clCode1 == clParkingTypeId.clCode1 && clCode2 == clParkingTypeId.clCode2 && clNo == clParkingTypeId.clNo && parkingTypeCode.equals(clParkingTypeId.parkingTypeCode);
  }

  @Override
  public String toString() {
    return "ClParkingTypeId [clCode1=" + clCode1 + ", clCode2=" + clCode2 + ", clNo=" + clNo + ", parkingTypeCode=" + parkingTypeCode + "]";
  }
}
