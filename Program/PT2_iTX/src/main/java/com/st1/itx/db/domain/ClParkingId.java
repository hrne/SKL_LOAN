package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * ClParking 擔保品-車位資料檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class ClParkingId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8010070145861457122L;

// 擔保品-代號1
	@Column(name = "`ClCode1`")
	private int clCode1 = 0;

	// 擔保品-代號2
	@Column(name = "`ClCode2`")
	private int clCode2 = 0;

	// 擔保品編號
	@Column(name = "`ClNo`")
	private int clNo = 0;

	// 車位資料序號
	@Column(name = "`ParkingSeqNo`")
	private int parkingSeqNo = 0;

	public ClParkingId() {
	}

	public ClParkingId(int clCode1, int clCode2, int clNo, int parkingSeqNo) {
		this.clCode1 = clCode1;
		this.clCode2 = clCode2;
		this.clNo = clNo;
		this.parkingSeqNo = parkingSeqNo;
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

	@Override
	public int hashCode() {
		return Objects.hash(clCode1, clCode2, clNo, parkingSeqNo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		ClParkingId clParkingId = (ClParkingId) obj;
		return clCode1 == clParkingId.clCode1 && clCode2 == clParkingId.clCode2 && clNo == clParkingId.clNo && parkingSeqNo == clParkingId.parkingSeqNo;
	}

	@Override
	public String toString() {
		return "ClParkingId [clCode1=" + clCode1 + ", clCode2=" + clCode2 + ", clNo=" + clNo + ", parkingSeqNo=" + parkingSeqNo + "]";
	}
}
