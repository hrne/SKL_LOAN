package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * BatxBaseRateChange 整批指標利率調整檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class BatxBaseRateChangeId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -545954008317254044L;

// 調整日期
	@Column(name = "`AdjDate`")
	private int adjDate = 0;

	// 戶號
	@Column(name = "`CustNo`")
	private int custNo = 0;

	// 額度
	@Column(name = "`FacmNo`")
	private int facmNo = 0;

	// 撥款序號
	@Column(name = "`BormNo`")
	private int bormNo = 0;

	public BatxBaseRateChangeId() {
	}

	public BatxBaseRateChangeId(int adjDate, int custNo, int facmNo, int bormNo) {
		this.adjDate = adjDate;
		this.custNo = custNo;
		this.facmNo = facmNo;
		this.bormNo = bormNo;
	}

	/**
	 * 調整日期<br>
	 * 
	 * @return Integer
	 */
	public int getAdjDate() {
		return StaticTool.bcToRoc(this.adjDate);
	}

	/**
	 * 調整日期<br>
	 * 
	 *
	 * @param adjDate 調整日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setAdjDate(int adjDate) throws LogicException {
		this.adjDate = StaticTool.rocToBc(adjDate);
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
	 * 撥款序號<br>
	 * 
	 * @return Integer
	 */
	public int getBormNo() {
		return this.bormNo;
	}

	/**
	 * 撥款序號<br>
	 * 
	 *
	 * @param bormNo 撥款序號
	 */
	public void setBormNo(int bormNo) {
		this.bormNo = bormNo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(adjDate, custNo, facmNo, bormNo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		BatxBaseRateChangeId batxBaseRateChangeId = (BatxBaseRateChangeId) obj;
		return adjDate == batxBaseRateChangeId.adjDate && custNo == batxBaseRateChangeId.custNo && facmNo == batxBaseRateChangeId.facmNo && bormNo == batxBaseRateChangeId.bormNo;
	}

	@Override
	public String toString() {
		return "BatxBaseRateChangeId [adjDate=" + adjDate + ", custNo=" + custNo + ", facmNo=" + facmNo + ", bormNo=" + bormNo + "]";
	}
}
