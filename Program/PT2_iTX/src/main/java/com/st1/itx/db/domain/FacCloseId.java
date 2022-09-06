package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * FacClose 清償作業檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class FacCloseId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7636277796977510217L;

// 戶號
	@Column(name = "`CustNo`")
	private int custNo = 0;

	// 清償序號
	@Column(name = "`CloseNo`")
	private int closeNo = 0;

	public FacCloseId() {
	}

	public FacCloseId(int custNo, int closeNo) {
		this.custNo = custNo;
		this.closeNo = closeNo;
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
	 * 清償序號<br>
	 * 
	 * @return Integer
	 */
	public int getCloseNo() {
		return this.closeNo;
	}

	/**
	 * 清償序號<br>
	 * 
	 *
	 * @param closeNo 清償序號
	 */
	public void setCloseNo(int closeNo) {
		this.closeNo = closeNo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(custNo, closeNo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		FacCloseId facCloseId = (FacCloseId) obj;
		return custNo == facCloseId.custNo && closeNo == facCloseId.closeNo;
	}

	@Override
	public String toString() {
		return "FacCloseId [custNo=" + custNo + ", closeNo=" + closeNo + "]";
	}
}
