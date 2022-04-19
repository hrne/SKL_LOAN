package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * NegQueryCust 債協客戶請求資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class NegQueryCustId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8044157644905087169L;

// 會計日期
	@Column(name = "`AcDate`")
	private int acDate = 0;

	// 身份證字號/統一編號
	/* 保貸戶須建立客戶主檔 */
	@Column(name = "`CustId`", length = 10)
	private String custId = " ";

	public NegQueryCustId() {
	}

	public NegQueryCustId(int acDate, String custId) {
		this.acDate = acDate;
		this.custId = custId;
	}

	/**
	 * 會計日期<br>
	 * 
	 * @return Integer
	 */
	public int getAcDate() {
		return this.acDate;
	}

	/**
	 * 會計日期<br>
	 * 
	 *
	 * @param acDate 會計日期
	 */
	public void setAcDate(int acDate) {
		this.acDate = acDate;
	}

	/**
	 * 身份證字號/統一編號<br>
	 * 保貸戶須建立客戶主檔
	 * 
	 * @return String
	 */
	public String getCustId() {
		return this.custId == null ? "" : this.custId;
	}

	/**
	 * 身份證字號/統一編號<br>
	 * 保貸戶須建立客戶主檔
	 *
	 * @param custId 身份證字號/統一編號
	 */
	public void setCustId(String custId) {
		this.custId = custId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(acDate, custId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		NegQueryCustId negQueryCustId = (NegQueryCustId) obj;
		return acDate == negQueryCustId.acDate && custId.equals(negQueryCustId.custId);
	}

	@Override
	public String toString() {
		return "NegQueryCustId [acDate=" + acDate + ", custId=" + custId + "]";
	}
}
