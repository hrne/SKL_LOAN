package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * ClBuildingParking 擔保品-建物獨立產權車位檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class ClBuildingParkingId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 298595784752485037L;

// 擔保品-代號1
  @Column(name = "`ClCode1`")
  private int clCode1 = 0;

  // 擔保品-代號2
  @Column(name = "`ClCode2`")
  private int clCode2 = 0;

  // 擔保品編號
  @Column(name = "`ClNo`")
  private int clNo = 0;

  // 車位建號
  /* 建號格式為5-3 */
  @Column(name = "`ParkingBdNo1`")
  private int parkingBdNo1 = 0;

  // 車位建號(子號)
  /* 建號格式為5-3 */
  @Column(name = "`ParkingBdNo2`")
  private int parkingBdNo2 = 0;

  public ClBuildingParkingId() {
  }

  public ClBuildingParkingId(int clCode1, int clCode2, int clNo, int parkingBdNo1, int parkingBdNo2) {
    this.clCode1 = clCode1;
    this.clCode2 = clCode2;
    this.clNo = clNo;
    this.parkingBdNo1 = parkingBdNo1;
    this.parkingBdNo2 = parkingBdNo2;
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


  @Override
  public int hashCode() {
    return Objects.hash(clCode1, clCode2, clNo, parkingBdNo1, parkingBdNo2);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    ClBuildingParkingId clBuildingParkingId = (ClBuildingParkingId) obj;
    return clCode1 == clBuildingParkingId.clCode1 && clCode2 == clBuildingParkingId.clCode2 && clNo == clBuildingParkingId.clNo && parkingBdNo1 == clBuildingParkingId.parkingBdNo1 && parkingBdNo2 == clBuildingParkingId.parkingBdNo2;
  }

  @Override
  public String toString() {
    return "ClBuildingParkingId [clCode1=" + clCode1 + ", clCode2=" + clCode2 + ", clNo=" + clNo + ", parkingBdNo1=" + parkingBdNo1 + ", parkingBdNo2=" + parkingBdNo2 + "]";
  }
}
