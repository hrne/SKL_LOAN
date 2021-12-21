package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * LoanOverdue 催收呆帳檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class LoanOverdueId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4516525654777860523L;

// 借款人戶號
	@Column(name = "`CustNo`")
	private int custNo = 0;

	// 額度編號
	@Column(name = "`FacmNo`")
	private int facmNo = 0;

	// 撥款序號
	@Column(name = "`BormNo`")
	private int bormNo = 0;

	// 催收序號
	@Column(name = "`OvduNo`")
	private int ovduNo = 0;

	public LoanOverdueId() {
	}

	public LoanOverdueId(int custNo, int facmNo, int bormNo, int ovduNo) {
		this.custNo = custNo;
		this.facmNo = facmNo;
		this.bormNo = bormNo;
		this.ovduNo = ovduNo;
	}

	/**
	 * 借款人戶號<br>
	 * 
	 * @return Integer
	 */
	public int getCustNo() {
		return this.custNo;
	}

	/**
	 * 借款人戶號<br>
	 * 
	 *
	 * @param custNo 借款人戶號
	 */
	public void setCustNo(int custNo) {
		this.custNo = custNo;
	}

	/**
	 * 額度編號<br>
	 * 
	 * @return Integer
	 */
	public int getFacmNo() {
		return this.facmNo;
	}

	/**
	 * 額度編號<br>
	 * 
	 *
	 * @param facmNo 額度編號
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

	/**
	 * 催收序號<br>
	 * 
	 * @return Integer
	 */
	public int getOvduNo() {
		return this.ovduNo;
	}

	/**
	 * 催收序號<br>
	 * 
	 *
	 * @param ovduNo 催收序號
	 */
	public void setOvduNo(int ovduNo) {
		this.ovduNo = ovduNo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(custNo, facmNo, bormNo, ovduNo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		LoanOverdueId loanOverdueId = (LoanOverdueId) obj;
		return custNo == loanOverdueId.custNo && facmNo == loanOverdueId.facmNo && bormNo == loanOverdueId.bormNo && ovduNo == loanOverdueId.ovduNo;
	}

	@Override
	public String toString() {
		return "LoanOverdueId [custNo=" + custNo + ", facmNo=" + facmNo + ", bormNo=" + bormNo + ", ovduNo=" + ovduNo + "]";
	}
}
