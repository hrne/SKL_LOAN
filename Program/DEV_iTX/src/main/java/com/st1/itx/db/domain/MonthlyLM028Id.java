package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * MonthlyLM028 LM028預估現金流量月報工作檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class MonthlyLM028Id implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8626628223314194373L;

// 資料年月
	/* YYYYMM */
	@Column(name = "`DataMonth`")
	private int dataMonth = 0;

	// 借款人戶號
	/* 原LMSACN */
	@Column(name = "`CustNo`")
	private int custNo = 0;

	// 額度編號
	/* 原LMSAPN */
	@Column(name = "`FacmNo`")
	private int facmNo = 0;

	// 撥款序號
	/* 原LMSASQ */
	@Column(name = "`BormNo`")
	private int bormNo = 0;

	public MonthlyLM028Id() {
	}

	public MonthlyLM028Id(int dataMonth, int custNo, int facmNo, int bormNo) {
		this.dataMonth = dataMonth;
		this.custNo = custNo;
		this.facmNo = facmNo;
		this.bormNo = bormNo;
	}

	/**
	 * 資料年月<br>
	 * YYYYMM
	 * 
	 * @return Integer
	 */
	public int getDataMonth() {
		return this.dataMonth;
	}

	/**
	 * 資料年月<br>
	 * YYYYMM
	 *
	 * @param dataMonth 資料年月
	 */
	public void setDataMonth(int dataMonth) {
		this.dataMonth = dataMonth;
	}

	/**
	 * 借款人戶號<br>
	 * 原LMSACN
	 * 
	 * @return Integer
	 */
	public int getCustNo() {
		return this.custNo;
	}

	/**
	 * 借款人戶號<br>
	 * 原LMSACN
	 *
	 * @param custNo 借款人戶號
	 */
	public void setCustNo(int custNo) {
		this.custNo = custNo;
	}

	/**
	 * 額度編號<br>
	 * 原LMSAPN
	 * 
	 * @return Integer
	 */
	public int getFacmNo() {
		return this.facmNo;
	}

	/**
	 * 額度編號<br>
	 * 原LMSAPN
	 *
	 * @param facmNo 額度編號
	 */
	public void setFacmNo(int facmNo) {
		this.facmNo = facmNo;
	}

	/**
	 * 撥款序號<br>
	 * 原LMSASQ
	 * 
	 * @return Integer
	 */
	public int getBormNo() {
		return this.bormNo;
	}

	/**
	 * 撥款序號<br>
	 * 原LMSASQ
	 *
	 * @param bormNo 撥款序號
	 */
	public void setBormNo(int bormNo) {
		this.bormNo = bormNo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dataMonth, custNo, facmNo, bormNo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		MonthlyLM028Id monthlyLM028Id = (MonthlyLM028Id) obj;
		return dataMonth == monthlyLM028Id.dataMonth && custNo == monthlyLM028Id.custNo && facmNo == monthlyLM028Id.facmNo && bormNo == monthlyLM028Id.bormNo;
	}

	@Override
	public String toString() {
		return "MonthlyLM028Id [dataMonth=" + dataMonth + ", custNo=" + custNo + ", facmNo=" + facmNo + ", bormNo=" + bormNo + "]";
	}
}
