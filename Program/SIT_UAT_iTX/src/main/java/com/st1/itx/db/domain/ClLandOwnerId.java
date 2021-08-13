package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * ClLandOwner 擔保品-土地所有權人檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class ClLandOwnerId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5693877768520845983L;

// 擔保品代號1
	@Column(name = "`ClCode1`")
	private int clCode1 = 0;

	// 擔保品代號2
	@Column(name = "`ClCode2`")
	private int clCode2 = 0;

	// 擔保品編號
	@Column(name = "`ClNo`")
	private int clNo = 0;

	// 土地序號
	/* 房地:從1起編土地:固定000 */
	@Column(name = "`LandSeq`")
	private int landSeq = 0;

	// 所有權人統編
	@Column(name = "`OwnerId`", length = 10)
	private String ownerId = " ";

	public ClLandOwnerId() {
	}

	public ClLandOwnerId(int clCode1, int clCode2, int clNo, int landSeq, String ownerId) {
		this.clCode1 = clCode1;
		this.clCode2 = clCode2;
		this.clNo = clNo;
		this.landSeq = landSeq;
		this.ownerId = ownerId;
	}

	/**
	 * 擔保品代號1<br>
	 * 
	 * @return Integer
	 */
	public int getClCode1() {
		return this.clCode1;
	}

	/**
	 * 擔保品代號1<br>
	 * 
	 *
	 * @param clCode1 擔保品代號1
	 */
	public void setClCode1(int clCode1) {
		this.clCode1 = clCode1;
	}

	/**
	 * 擔保品代號2<br>
	 * 
	 * @return Integer
	 */
	public int getClCode2() {
		return this.clCode2;
	}

	/**
	 * 擔保品代號2<br>
	 * 
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
	 * 土地序號<br>
	 * 房地:從1起編 土地:固定000
	 * 
	 * @return Integer
	 */
	public int getLandSeq() {
		return this.landSeq;
	}

	/**
	 * 土地序號<br>
	 * 房地:從1起編 土地:固定000
	 *
	 * @param landSeq 土地序號
	 */
	public void setLandSeq(int landSeq) {
		this.landSeq = landSeq;
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

	@Override
	public int hashCode() {
		return Objects.hash(clCode1, clCode2, clNo, landSeq, ownerId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		ClLandOwnerId clLandOwnerId = (ClLandOwnerId) obj;
		return clCode1 == clLandOwnerId.clCode1 && clCode2 == clLandOwnerId.clCode2 && clNo == clLandOwnerId.clNo && landSeq == clLandOwnerId.landSeq && ownerId.equals(clLandOwnerId.ownerId);
	}

	@Override
	public String toString() {
		return "ClLandOwnerId [clCode1=" + clCode1 + ", clCode2=" + clCode2 + ", clNo=" + clNo + ", landSeq=" + landSeq + ", ownerId=" + ownerId + "]";
	}
}
