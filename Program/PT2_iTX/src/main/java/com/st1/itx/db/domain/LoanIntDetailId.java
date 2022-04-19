package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * LoanIntDetail 計息明細檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class LoanIntDetailId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5471639797659145566L;

// 借款人戶號
	@Column(name = "`CustNo`")
	private int custNo = 0;

	// 額度編號
	@Column(name = "`FacmNo`")
	private int facmNo = 0;

	// 撥款序號
	@Column(name = "`BormNo`")
	private int bormNo = 0;

	// 交易序號-會計日期
	@Column(name = "`AcDate`")
	private int acDate = 0;

	// 交易序號-櫃員別
	@Column(name = "`TlrNo`", length = 6)
	private String tlrNo = " ";

	// 交易序號-流水號
	@Column(name = "`TxtNo`", length = 8)
	private String txtNo = " ";

	// 計息流水號
	@Column(name = "`IntSeq`")
	private int intSeq = 0;

	public LoanIntDetailId() {
	}

	public LoanIntDetailId(int custNo, int facmNo, int bormNo, int acDate, String tlrNo, String txtNo, int intSeq) {
		this.custNo = custNo;
		this.facmNo = facmNo;
		this.bormNo = bormNo;
		this.acDate = acDate;
		this.tlrNo = tlrNo;
		this.txtNo = txtNo;
		this.intSeq = intSeq;
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
	 * 交易序號-會計日期<br>
	 * 
	 * @return Integer
	 */
	public int getAcDate() {
		return StaticTool.bcToRoc(this.acDate);
	}

	/**
	 * 交易序號-會計日期<br>
	 * 
	 *
	 * @param acDate 交易序號-會計日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setAcDate(int acDate) throws LogicException {
		this.acDate = StaticTool.rocToBc(acDate);
	}

	/**
	 * 交易序號-櫃員別<br>
	 * 
	 * @return String
	 */
	public String getTlrNo() {
		return this.tlrNo == null ? "" : this.tlrNo;
	}

	/**
	 * 交易序號-櫃員別<br>
	 * 
	 *
	 * @param tlrNo 交易序號-櫃員別
	 */
	public void setTlrNo(String tlrNo) {
		this.tlrNo = tlrNo;
	}

	/**
	 * 交易序號-流水號<br>
	 * 
	 * @return String
	 */
	public String getTxtNo() {
		return this.txtNo == null ? "" : this.txtNo;
	}

	/**
	 * 交易序號-流水號<br>
	 * 
	 *
	 * @param txtNo 交易序號-流水號
	 */
	public void setTxtNo(String txtNo) {
		this.txtNo = txtNo;
	}

	/**
	 * 計息流水號<br>
	 * 
	 * @return Integer
	 */
	public int getIntSeq() {
		return this.intSeq;
	}

	/**
	 * 計息流水號<br>
	 * 
	 *
	 * @param intSeq 計息流水號
	 */
	public void setIntSeq(int intSeq) {
		this.intSeq = intSeq;
	}

	@Override
	public int hashCode() {
		return Objects.hash(custNo, facmNo, bormNo, acDate, tlrNo, txtNo, intSeq);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		LoanIntDetailId loanIntDetailId = (LoanIntDetailId) obj;
		return custNo == loanIntDetailId.custNo && facmNo == loanIntDetailId.facmNo && bormNo == loanIntDetailId.bormNo && acDate == loanIntDetailId.acDate && tlrNo.equals(loanIntDetailId.tlrNo)
				&& txtNo.equals(loanIntDetailId.txtNo) && intSeq == loanIntDetailId.intSeq;
	}

	@Override
	public String toString() {
		return "LoanIntDetailId [custNo=" + custNo + ", facmNo=" + facmNo + ", bormNo=" + bormNo + ", acDate=" + acDate + ", tlrNo=" + tlrNo + ", txtNo=" + txtNo + ", intSeq=" + intSeq + "]";
	}
}
