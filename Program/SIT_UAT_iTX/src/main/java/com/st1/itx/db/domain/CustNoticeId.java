package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * CustNotice 客戶通知設定檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class CustNoticeId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4499770746958767953L;

// 戶號
	@Column(name = "`CustNo`")
	private int custNo = 0;

	// 額度編號
	@Column(name = "`FacmNo`")
	private int facmNo = 0;

	// 報表代號
	@Column(name = "`FormNo`", length = 10)
	private String formNo = " ";

	public CustNoticeId() {
	}

	public CustNoticeId(int custNo, int facmNo, String formNo) {
		this.custNo = custNo;
		this.facmNo = facmNo;
		this.formNo = formNo;
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
	 * 報表代號<br>
	 * 
	 * @return String
	 */
	public String getFormNo() {
		return this.formNo == null ? "" : this.formNo;
	}

	/**
	 * 報表代號<br>
	 * 
	 *
	 * @param formNo 報表代號
	 */
	public void setFormNo(String formNo) {
		this.formNo = formNo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(custNo, facmNo, formNo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		CustNoticeId custNoticeId = (CustNoticeId) obj;
		return custNo == custNoticeId.custNo && facmNo == custNoticeId.facmNo && formNo.equals(custNoticeId.formNo);
	}

	@Override
	public String toString() {
		return "CustNoticeId [custNo=" + custNo + ", facmNo=" + facmNo + ", formNo=" + formNo + "]";
	}
}
