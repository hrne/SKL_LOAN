package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * RptRelationSelf 報表用_金控利害關係人_關係人資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class RptRelationSelfId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2242285181853814091L;

// CusId
	@Column(name = "`CusId`", length = 20)
	private String cusId = " ";

	// STSCD
	@Column(name = "`STSCD`", length = 2)
	private String sTSCD = " ";

	// CusSCD
	@Column(name = "`CusSCD`", length = 2)
	private String cusSCD = " ";

	public RptRelationSelfId() {
	}

	public RptRelationSelfId(String cusId, String sTSCD, String cusSCD) {
		this.cusId = cusId;
		this.sTSCD = sTSCD;
		this.cusSCD = cusSCD;
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
	 * STSCD<br>
	 * 
	 * @return String
	 */
	public String getSTSCD() {
		return this.sTSCD == null ? "" : this.sTSCD;
	}

	/**
	 * STSCD<br>
	 * 
	 *
	 * @param sTSCD STSCD
	 */
	public void setSTSCD(String sTSCD) {
		this.sTSCD = sTSCD;
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

	@Override
	public int hashCode() {
		return Objects.hash(cusId, sTSCD, cusSCD);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		RptRelationSelfId rptRelationSelfId = (RptRelationSelfId) obj;
		return cusId.equals(rptRelationSelfId.cusId) && sTSCD == rptRelationSelfId.sTSCD && cusSCD == rptRelationSelfId.cusSCD;
	}

	@Override
	public String toString() {
		return "RptRelationSelfId [cusId=" + cusId + ", sTSCD=" + sTSCD + ", cusSCD=" + cusSCD + "]";
	}
}
