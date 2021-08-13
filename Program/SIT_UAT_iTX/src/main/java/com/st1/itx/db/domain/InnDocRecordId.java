package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * InnDocRecord 檔案借閱檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class InnDocRecordId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -359256040910122400L;

	// 借款人戶號
	@Column(name = "`CustNo`")
	private int custNo = 0;

	// 額度號碼
	@Column(name = "`FacmNo`")
	private int facmNo = 0;

	// 申請序號
	@Column(name = "`ApplSeq`", length = 3)
	private String applSeq = " ";

	public InnDocRecordId() {
	}

	public InnDocRecordId(int custNo, int facmNo, String applSeq) {
		this.custNo = custNo;
		this.facmNo = facmNo;
		this.applSeq = applSeq;
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
	 * 額度號碼<br>
	 * 
	 * @return Integer
	 */
	public int getFacmNo() {
		return this.facmNo;
	}

	/**
	 * 額度號碼<br>
	 * 
	 *
	 * @param facmNo 額度號碼
	 */
	public void setFacmNo(int facmNo) {
		this.facmNo = facmNo;
	}

	/**
	 * 申請序號<br>
	 * 
	 * @return String
	 */
	public String getApplSeq() {
		return this.applSeq == null ? "" : this.applSeq;
	}

	/**
	 * 申請序號<br>
	 * 
	 *
	 * @param applSeq 申請序號
	 */
	public void setApplSeq(String applSeq) {
		this.applSeq = applSeq;
	}

	@Override
	public int hashCode() {
		return Objects.hash(custNo, facmNo, applSeq);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		InnDocRecordId innDocRecordId = (InnDocRecordId) obj;
		return custNo == innDocRecordId.custNo && facmNo == innDocRecordId.facmNo && applSeq.equals(innDocRecordId.applSeq);
	}

	@Override
	public String toString() {
		return "InnDocRecordId [custNo=" + custNo + ", facmNo=" + facmNo + ", applSeq=" + applSeq + "]";
	}
}
