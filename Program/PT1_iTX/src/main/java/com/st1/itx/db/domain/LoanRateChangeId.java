package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * LoanRateChange 放款利率變動檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class LoanRateChangeId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4485972306283201083L;

// 借款人戶號
	@Column(name = "`CustNo`")
	private int custNo = 0;

	// 額度編號
	@Column(name = "`FacmNo`")
	private int facmNo = 0;

	// 撥款序號
	@Column(name = "`BormNo`")
	private int bormNo = 0;

	// 生效日期
	@Column(name = "`EffectDate`")
	private int effectDate = 0;

	public LoanRateChangeId() {
	}

	public LoanRateChangeId(int custNo, int facmNo, int bormNo, int effectDate) {
		this.custNo = custNo;
		this.facmNo = facmNo;
		this.bormNo = bormNo;
		this.effectDate = effectDate;
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
	 * 生效日期<br>
	 * 
	 * @return Integer
	 */
	public int getEffectDate() {
		return StaticTool.bcToRoc(this.effectDate);
	}

	/**
	 * 生效日期<br>
	 * 
	 *
	 * @param effectDate 生效日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setEffectDate(int effectDate) throws LogicException {
		this.effectDate = StaticTool.rocToBc(effectDate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(custNo, facmNo, bormNo, effectDate);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		LoanRateChangeId loanRateChangeId = (LoanRateChangeId) obj;
		return custNo == loanRateChangeId.custNo && facmNo == loanRateChangeId.facmNo && bormNo == loanRateChangeId.bormNo && effectDate == loanRateChangeId.effectDate;
	}

	@Override
	public String toString() {
		return "LoanRateChangeId [custNo=" + custNo + ", facmNo=" + facmNo + ", bormNo=" + bormNo + ", effectDate=" + effectDate + "]";
	}
}
