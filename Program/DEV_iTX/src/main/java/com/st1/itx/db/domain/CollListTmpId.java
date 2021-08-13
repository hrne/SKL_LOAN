package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * CollListTmp 法催紀錄清單暫存檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class CollListTmpId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2694008179542631003L;

	// 戶號
	@Column(name = "`CustNo`")
	private int custNo = 0;

	// 額度
	@Column(name = "`FacmNo`")
	private int facmNo = 0;

	// 擔保品代號1
	@Column(name = "`ClCode1`")
	private int clCode1 = 0;

	// 擔保品代號2
	@Column(name = "`ClCode2`")
	private int clCode2 = 0;

	// 擔保品編號
	@Column(name = "`ClNo`")
	private int clNo = 0;

	public CollListTmpId() {
	}

	public CollListTmpId(int custNo, int facmNo, int clCode1, int clCode2, int clNo) {
		this.custNo = custNo;
		this.facmNo = facmNo;
		this.clCode1 = clCode1;
		this.clCode2 = clCode2;
		this.clNo = clNo;
	}

	/**
	 * 戶號<br>
	 * 
	 * @return Integer
	 */
	public int getCustNo() {
		return this.custNo;
	}

	/**
	 * 戶號<br>
	 * 
	 *
	 * @param custNo 戶號
	 */
	public void setCustNo(int custNo) {
		this.custNo = custNo;
	}

	/**
	 * 額度<br>
	 * 
	 * @return Integer
	 */
	public int getFacmNo() {
		return this.facmNo;
	}

	/**
	 * 額度<br>
	 * 
	 *
	 * @param facmNo 額度
	 */
	public void setFacmNo(int facmNo) {
		this.facmNo = facmNo;
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

	@Override
	public int hashCode() {
		return Objects.hash(custNo, facmNo, clCode1, clCode2, clNo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		CollListTmpId collListTmpId = (CollListTmpId) obj;
		return custNo == collListTmpId.custNo && facmNo == collListTmpId.facmNo && clCode1 == collListTmpId.clCode1 && clCode2 == collListTmpId.clCode2 && clNo == collListTmpId.clNo;
	}

	@Override
	public String toString() {
		return "CollListTmpId [custNo=" + custNo + ", facmNo=" + facmNo + ", clCode1=" + clCode1 + ", clCode2=" + clCode2 + ", clNo=" + clNo + "]";
	}
}
