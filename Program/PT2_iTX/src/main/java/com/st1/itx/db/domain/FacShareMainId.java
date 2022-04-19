package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * FacShareMain 共用額度主檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class FacShareMainId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5578156346995999064L;

// 主要戶號
	@Column(name = "`CustNo`")
	private int custNo = 0;

	// 主要案件編號
	@Column(name = "`CreditSysNo`")
	private int creditSysNo = 0;

	public FacShareMainId() {
	}

	public FacShareMainId(int custNo, int creditSysNo) {
		this.custNo = custNo;
		this.creditSysNo = creditSysNo;
	}

	/**
	 * 主要戶號<br>
	 * 
	 * @return Integer
	 */
	public int getCustNo() {
		return this.custNo;
	}

	/**
	 * 主要戶號<br>
	 * 
	 *
	 * @param custNo 主要戶號
	 */
	public void setCustNo(int custNo) {
		this.custNo = custNo;
	}

	/**
	 * 主要案件編號<br>
	 * 
	 * @return Integer
	 */
	public int getCreditSysNo() {
		return this.creditSysNo;
	}

	/**
	 * 主要案件編號<br>
	 * 
	 *
	 * @param creditSysNo 主要案件編號
	 */
	public void setCreditSysNo(int creditSysNo) {
		this.creditSysNo = creditSysNo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(custNo, creditSysNo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		FacShareMainId facShareMainId = (FacShareMainId) obj;
		return custNo == facShareMainId.custNo && creditSysNo == facShareMainId.creditSysNo;
	}

	@Override
	public String toString() {
		return "FacShareMainId [custNo=" + custNo + ", creditSysNo=" + creditSysNo + "]";
	}
}
