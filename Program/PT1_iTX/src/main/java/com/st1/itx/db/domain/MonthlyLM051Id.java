package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * MonthlyLM051 月報LM051工作檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class MonthlyLM051Id implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 286330326874145188L;

	// 借款人戶號
	@Column(name = "`CustNo`")
	private int custNo = 0;

	// 額度編號
	@Column(name = "`FacmNo`")
	private int facmNo = 0;

	public MonthlyLM051Id() {
	}

	public MonthlyLM051Id(int custNo, int facmNo) {
		this.custNo = custNo;
		this.facmNo = facmNo;
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

	@Override
	public int hashCode() {
		return Objects.hash(custNo, facmNo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		MonthlyLM051Id monthlyLM051Id = (MonthlyLM051Id) obj;
		return custNo == monthlyLM051Id.custNo && facmNo == monthlyLM051Id.facmNo;
	}

	@Override
	public String toString() {
		return "MonthlyLM051Id [custNo=" + custNo + ", facmNo=" + facmNo + "]";
	}
}
