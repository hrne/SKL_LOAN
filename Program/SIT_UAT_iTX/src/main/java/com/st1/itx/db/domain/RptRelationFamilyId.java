package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * RptRelationFamily 報表用_金控利害關係人_關係人親屬資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class RptRelationFamilyId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3008230813478110002L;

// CusId
	@Column(name = "`CusId`", length = 20)
	private String cusId = " ";

	// CusSCD
	@Column(name = "`CusSCD`", length = 2)
	private String cusSCD = " ";

	// RlbID
	@Column(name = "`RlbID`", length = 20)
	private String rlbID = " ";

	public RptRelationFamilyId() {
	}

	public RptRelationFamilyId(String cusId, String cusSCD, String rlbID) {
		this.cusId = cusId;
		this.cusSCD = cusSCD;
		this.rlbID = rlbID;
	}

	/**
	 * CusId<br>
	 * 
	 * @return String
	 */
	public String getCusId() {
		return this.cusId == null ? "" : this.cusId;
	}

	/**
	 * CusId<br>
	 * 
	 *
	 * @param cusId CusId
	 */
	public void setCusId(String cusId) {
		this.cusId = cusId;
	}

	/**
	 * CusSCD<br>
	 * 
	 * @return String
	 */
	public String getCusSCD() {
		return this.cusSCD == null ? "" : this.cusSCD;
	}

	/**
	 * CusSCD<br>
	 * 
	 *
	 * @param cusSCD CusSCD
	 */
	public void setCusSCD(String cusSCD) {
		this.cusSCD = cusSCD;
	}

	/**
	 * RlbID<br>
	 * 
	 * @return String
	 */
	public String getRlbID() {
		return this.rlbID == null ? "" : this.rlbID;
	}

	/**
	 * RlbID<br>
	 * 
	 *
	 * @param rlbID RlbID
	 */
	public void setRlbID(String rlbID) {
		this.rlbID = rlbID;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cusId, cusSCD, rlbID);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		RptRelationFamilyId rptRelationFamilyId = (RptRelationFamilyId) obj;
		return cusId.equals(rptRelationFamilyId.cusId) && cusSCD == rptRelationFamilyId.cusSCD && rlbID == rptRelationFamilyId.rlbID;
	}

	@Override
	public String toString() {
		return "RptRelationFamilyId [cusId=" + cusId + ", cusSCD=" + cusSCD + ", rlbID=" + rlbID + "]";
	}
}
