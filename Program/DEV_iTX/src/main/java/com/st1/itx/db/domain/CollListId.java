package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * CollList 法催紀錄清單檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class CollListId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7433708397993120869L;

// 戶號
	@Column(name = "`CustNo`")
	private int custNo = 0;

	// 額度
	@Column(name = "`FacmNo`")
	private int facmNo = 0;

	public CollListId() {
	}

	public CollListId(int custNo, int facmNo) {
		this.custNo = custNo;
		this.facmNo = facmNo;
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
	 * 額度<br>
	 * 
	 * @return Integer
	 */
	public int getFacmNo() {
		return this.facmNo;
	}

	/**
	 * 額度<br>
	 * 
	 *
	 * @param facmNo 額度
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
		CollListId collListId = (CollListId) obj;
		return custNo == collListId.custNo && facmNo == collListId.facmNo;
	}

	@Override
	public String toString() {
		return "CollListId [custNo=" + custNo + ", facmNo=" + facmNo + "]";
	}
}
