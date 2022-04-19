package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * BankRelationSuspected 是否為疑似準利害關係人檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class BankRelationSuspectedId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3990470644794000481L;

// 自然人姓名
	@Column(name = "`RepCusName`", length = 100)
	private String repCusName = " ";

	// 該自然人擔任董事長之公司統一編號
	@Column(name = "`CustId`", length = 11)
	private String custId = " ";

	public BankRelationSuspectedId() {
	}

	public BankRelationSuspectedId(String repCusName, String custId) {
		this.repCusName = repCusName;
		this.custId = custId;
	}

	/**
	 * 自然人姓名<br>
	 * 
	 * @return String
	 */
	public String getRepCusName() {
		return this.repCusName == null ? "" : this.repCusName;
	}

	/**
	 * 自然人姓名<br>
	 * 
	 *
	 * @param repCusName 自然人姓名
	 */
	public void setRepCusName(String repCusName) {
		this.repCusName = repCusName;
	}

	/**
	 * 該自然人擔任董事長之公司統一編號<br>
	 * 
	 * @return String
	 */
	public String getCustId() {
		return this.custId == null ? "" : this.custId;
	}

	/**
	 * 該自然人擔任董事長之公司統一編號<br>
	 * 
	 *
	 * @param custId 該自然人擔任董事長之公司統一編號
	 */
	public void setCustId(String custId) {
		this.custId = custId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(repCusName, custId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		BankRelationSuspectedId bankRelationSuspectedId = (BankRelationSuspectedId) obj;
		return repCusName.equals(bankRelationSuspectedId.repCusName) && custId.equals(bankRelationSuspectedId.custId);
	}

	@Override
	public String toString() {
		return "BankRelationSuspectedId [repCusName=" + repCusName + ", custId=" + custId + "]";
	}
}
