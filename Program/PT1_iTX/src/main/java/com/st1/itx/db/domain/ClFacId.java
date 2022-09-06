package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * ClFac 擔保品與額度關聯檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class ClFacId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7358705965384503812L;

// 擔保品代號1
	/* 擔保品代號檔CdCl */
	@Column(name = "`ClCode1`")
	private int clCode1 = 0;

	// 擔保品代號2
	/* 擔保品代號檔CdCl */
	@Column(name = "`ClCode2`")
	private int clCode2 = 0;

	// 擔保品編號
	@Column(name = "`ClNo`")
	private int clNo = 0;

	// 核准號碼
	/* 規劃調整為ApplNo */
	@Column(name = "`ApproveNo`")
	private int approveNo = 0;

	public ClFacId() {
	}

	public ClFacId(int clCode1, int clCode2, int clNo, int approveNo) {
		this.clCode1 = clCode1;
		this.clCode2 = clCode2;
		this.clNo = clNo;
		this.approveNo = approveNo;
	}

	/**
	 * 擔保品代號1<br>
	 * 擔保品代號檔CdCl
	 * 
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
	 * 
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
	 * 核准號碼<br>
	 * 規劃調整為ApplNo
	 * 
	 * @return Integer
	 */
	public int getApproveNo() {
		return this.approveNo;
	}

	/**
	 * 核准號碼<br>
	 * 規劃調整為ApplNo
	 *
	 * @param approveNo 核准號碼
	 */
	public void setApproveNo(int approveNo) {
		this.approveNo = approveNo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(clCode1, clCode2, clNo, approveNo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		ClFacId clFacId = (ClFacId) obj;
		return clCode1 == clFacId.clCode1 && clCode2 == clFacId.clCode2 && clNo == clFacId.clNo && approveNo == clFacId.approveNo;
	}

	@Override
	public String toString() {
		return "ClFacId [clCode1=" + clCode1 + ", clCode2=" + clCode2 + ", clNo=" + clNo + ", approveNo=" + approveNo + "]";
	}
}
