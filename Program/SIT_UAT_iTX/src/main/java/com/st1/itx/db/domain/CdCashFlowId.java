package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * CdCashFlow 現金流量預估資料檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class CdCashFlowId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1327585975523076272L;

// 單位別
	/* CdBranch.BranchNo */
	@Column(name = "`BranchNo`")
	private int branchNo = 0;

	// 年月份
	@Column(name = "`DataYearMonth`")
	private int dataYearMonth = 0;

	// 旬別
	/* 1: 上旬2: 中旬3: 下旬 */
	@Column(name = "`PeriodCode`")
	private int periodCode = 0;

	public CdCashFlowId() {
	}

	public CdCashFlowId(int branchNo, int dataYearMonth, int periodCode) {
		this.branchNo = branchNo;
		this.dataYearMonth = dataYearMonth;
		this.periodCode = periodCode;
	}

	/**
	 * 單位別<br>
	 * CdBranch.BranchNo
	 * 
	 * @return Integer
	 */
	public int getBranchNo() {
		return this.branchNo;
	}

	/**
	 * 單位別<br>
	 * CdBranch.BranchNo
	 *
	 * @param branchNo 單位別
	 */
	public void setBranchNo(int branchNo) {
		this.branchNo = branchNo;
	}

	/**
	 * 年月份<br>
	 * 
	 * @return Integer
	 */
	public int getDataYearMonth() {
		return this.dataYearMonth;
	}

	/**
	 * 年月份<br>
	 * 
	 *
	 * @param dataYearMonth 年月份
	 */
	public void setDataYearMonth(int dataYearMonth) {
		this.dataYearMonth = dataYearMonth;
	}

	/**
	 * 旬別<br>
	 * 1: 上旬 2: 中旬 3: 下旬
	 * 
	 * @return Integer
	 */
	public int getPeriodCode() {
		return this.periodCode;
	}

	/**
	 * 旬別<br>
	 * 1: 上旬 2: 中旬 3: 下旬
	 *
	 * @param periodCode 旬別
	 */
	public void setPeriodCode(int periodCode) {
		this.periodCode = periodCode;
	}

	@Override
	public int hashCode() {
		return Objects.hash(branchNo, dataYearMonth, periodCode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		CdCashFlowId cdCashFlowId = (CdCashFlowId) obj;
		return branchNo == cdCashFlowId.branchNo && dataYearMonth == cdCashFlowId.dataYearMonth && periodCode == cdCashFlowId.periodCode;
	}

	@Override
	public String toString() {
		return "CdCashFlowId [branchNo=" + branchNo + ", dataYearMonth=" + dataYearMonth + ", periodCode=" + periodCode + "]";
	}
}
