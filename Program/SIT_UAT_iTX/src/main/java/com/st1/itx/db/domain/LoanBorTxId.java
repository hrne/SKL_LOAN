package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * LoanBorTx 放款交易內容檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class LoanBorTxId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8293198287402141220L;

// 借款人戶號
	@Column(name = "`CustNo`")
	private int custNo = 0;

	// 額度編號
	/* 999;聯貸訂約案 */
	@Column(name = "`FacmNo`")
	private int facmNo = 0;

	// 撥款序號
	@Column(name = "`BormNo`")
	private int bormNo = 0;

	// 交易內容檔序號
	@Column(name = "`BorxNo`")
	private int borxNo = 0;

	public LoanBorTxId() {
	}

	public LoanBorTxId(int custNo, int facmNo, int bormNo, int borxNo) {
		this.custNo = custNo;
		this.facmNo = facmNo;
		this.bormNo = bormNo;
		this.borxNo = borxNo;
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
	 * 999;聯貸訂約案
	 * 
	 * @return Integer
	 */
	public int getFacmNo() {
		return this.facmNo;
	}

	/**
	 * 額度編號<br>
	 * 999;聯貸訂約案
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
	 * 交易內容檔序號<br>
	 * 
	 * @return Integer
	 */
	public int getBorxNo() {
		return this.borxNo;
	}

	/**
	 * 交易內容檔序號<br>
	 * 
	 *
	 * @param borxNo 交易內容檔序號
	 */
	public void setBorxNo(int borxNo) {
		this.borxNo = borxNo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(custNo, facmNo, bormNo, borxNo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		LoanBorTxId loanBorTxId = (LoanBorTxId) obj;
		return custNo == loanBorTxId.custNo && facmNo == loanBorTxId.facmNo && bormNo == loanBorTxId.bormNo && borxNo == loanBorTxId.borxNo;
	}

	@Override
	public String toString() {
		return "LoanBorTxId [custNo=" + custNo + ", facmNo=" + facmNo + ", bormNo=" + bormNo + ", borxNo=" + borxNo + "]";
	}
}
