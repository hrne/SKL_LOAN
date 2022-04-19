package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * HlThreeDetail 房貸換算業績網頁查詢檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class HlThreeDetailId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4705488795525480638L;

// 營業單位別
	@Column(name = "`CusBNo`", length = 2)
	private String cusBNo = " ";

	// 借款人戶號
	@Column(name = "`HlCusNo`")
	private BigDecimal hlCusNo = new BigDecimal("0");

	// 額度編號
	@Column(name = "`AmountNo`", length = 3)
	private String amountNo = " ";

	// 計件代碼
	@Column(name = "`CaseNo`", length = 1)
	private String caseNo = " ";

	public HlThreeDetailId() {
	}

	public HlThreeDetailId(String cusBNo, BigDecimal hlCusNo, String amountNo, String caseNo) {
		this.cusBNo = cusBNo;
		this.hlCusNo = hlCusNo;
		this.amountNo = amountNo;
		this.caseNo = caseNo;
	}

	/**
	 * 營業單位別<br>
	 * 
	 * @return String
	 */
	public String getCusBNo() {
		return this.cusBNo == null ? "" : this.cusBNo;
	}

	/**
	 * 營業單位別<br>
	 * 
	 *
	 * @param cusBNo 營業單位別
	 */
	public void setCusBNo(String cusBNo) {
		this.cusBNo = cusBNo;
	}

	/**
	 * 借款人戶號<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getHlCusNo() {
		return this.hlCusNo;
	}

	/**
	 * 借款人戶號<br>
	 * 
	 *
	 * @param hlCusNo 借款人戶號
	 */
	public void setHlCusNo(BigDecimal hlCusNo) {
		this.hlCusNo = hlCusNo;
	}

	/**
	 * 額度編號<br>
	 * 
	 * @return String
	 */
	public String getAmountNo() {
		return this.amountNo == null ? "" : this.amountNo;
	}

	/**
	 * 額度編號<br>
	 * 
	 *
	 * @param amountNo 額度編號
	 */
	public void setAmountNo(String amountNo) {
		this.amountNo = amountNo;
	}

	/**
	 * 計件代碼<br>
	 * 
	 * @return String
	 */
	public String getCaseNo() {
		return this.caseNo == null ? "" : this.caseNo;
	}

	/**
	 * 計件代碼<br>
	 * 
	 *
	 * @param caseNo 計件代碼
	 */
	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cusBNo, hlCusNo, amountNo, caseNo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		HlThreeDetailId hlThreeDetailId = (HlThreeDetailId) obj;
		return cusBNo.equals(hlThreeDetailId.cusBNo) && hlCusNo == hlThreeDetailId.hlCusNo && amountNo.equals(hlThreeDetailId.amountNo) && caseNo.equals(hlThreeDetailId.caseNo);
	}

	@Override
	public String toString() {
		return "HlThreeDetailId [cusBNo=" + cusBNo + ", hlCusNo=" + hlCusNo + ", amountNo=" + amountNo + ", caseNo=" + caseNo + "]";
	}
}
