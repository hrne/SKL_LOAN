package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * LoanIfrs9Ap IFRS9欄位清單1<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class LoanIfrs9ApId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8212797341392697785L;

// 年月份
	@Column(name = "`DataYM`")
	private int dataYM = 0;

	// 戶號
	@Column(name = "`CustNo`")
	private int custNo = 0;

	// 額度編號
	@Column(name = "`FacmNo`")
	private int facmNo = 0;

	// 撥款序號
	@Column(name = "`BormNo`")
	private int bormNo = 0;

	public LoanIfrs9ApId() {
	}

	public LoanIfrs9ApId(int dataYM, int custNo, int facmNo, int bormNo) {
		this.dataYM = dataYM;
		this.custNo = custNo;
		this.facmNo = facmNo;
		this.bormNo = bormNo;
	}

	/**
	 * 年月份<br>
	 * 
	 * @return Integer
	 */
	public int getDataYM() {
		return this.dataYM;
	}

	/**
	 * 年月份<br>
	 * 
	 *
	 * @param dataYM 年月份
	 */
	public void setDataYM(int dataYM) {
		this.dataYM = dataYM;
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

	@Override
	public int hashCode() {
		return Objects.hash(dataYM, custNo, facmNo, bormNo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		LoanIfrs9ApId loanIfrs9ApId = (LoanIfrs9ApId) obj;
		return dataYM == loanIfrs9ApId.dataYM && custNo == loanIfrs9ApId.custNo && facmNo == loanIfrs9ApId.facmNo && bormNo == loanIfrs9ApId.bormNo;
	}

	@Override
	public String toString() {
		return "LoanIfrs9ApId [dataYM=" + dataYM + ", custNo=" + custNo + ", facmNo=" + facmNo + ", bormNo=" + bormNo + "]";
	}
}
