package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * BankRelationFamily 金控利害關係人_關係人員工之親屬資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class BankRelationFamilyId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -369729932662801063L;

// 借款戶所屬公司名稱
	@Column(name = "`CustName`", length = 70)
	private String custName = " ";

	// 借款戶統編
	@Column(name = "`CustId`", length = 11)
	private String custId = " ";

	// 親屬統編
	@Column(name = "`RelationId`", length = 11)
	private String relationId = " ";

	public BankRelationFamilyId() {
	}

	public BankRelationFamilyId(String custName, String custId, String relationId) {
		this.custName = custName;
		this.custId = custId;
		this.relationId = relationId;
	}

	/**
	 * 借款戶所屬公司名稱<br>
	 * 
	 * @return String
	 */
	public String getCustName() {
		return this.custName == null ? "" : this.custName;
	}

	/**
	 * 借款戶所屬公司名稱<br>
	 * 
	 *
	 * @param custName 借款戶所屬公司名稱
	 */
	public void setCustName(String custName) {
		this.custName = custName;
	}

	/**
	 * 借款戶統編<br>
	 * 
	 * @return String
	 */
	public String getCustId() {
		return this.custId == null ? "" : this.custId;
	}

	/**
	 * 借款戶統編<br>
	 * 
	 *
	 * @param custId 借款戶統編
	 */
	public void setCustId(String custId) {
		this.custId = custId;
	}

	/**
	 * 親屬統編<br>
	 * 
	 * @return String
	 */
	public String getRelationId() {
		return this.relationId == null ? "" : this.relationId;
	}

	/**
	 * 親屬統編<br>
	 * 
	 *
	 * @param relationId 親屬統編
	 */
	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(custName, custId, relationId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		BankRelationFamilyId bankRelationFamilyId = (BankRelationFamilyId) obj;
		return custName.equals(bankRelationFamilyId.custName) && custId.equals(bankRelationFamilyId.custId) && relationId.equals(bankRelationFamilyId.relationId);
	}

	@Override
	public String toString() {
		return "BankRelationFamilyId [custName=" + custName + ", custId=" + custId + ", relationId=" + relationId + "]";
	}
}
