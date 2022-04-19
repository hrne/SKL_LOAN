package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * FacShareRelation 共同借款人闗係檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class FacShareRelationId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7266229594486773777L;

// 核准號碼
	@Column(name = "`ApplNo`")
	private int applNo = 0;

	// 共同借款人核准號碼
	@Column(name = "`RelApplNo`")
	private int relApplNo = 0;

	public FacShareRelationId() {
	}

	public FacShareRelationId(int applNo, int relApplNo) {
		this.applNo = applNo;
		this.relApplNo = relApplNo;
	}

	/**
	 * 核准號碼<br>
	 * 
	 * @return Integer
	 */
	public int getApplNo() {
		return this.applNo;
	}

	/**
	 * 核准號碼<br>
	 * 
	 *
	 * @param applNo 核准號碼
	 */
	public void setApplNo(int applNo) {
		this.applNo = applNo;
	}

	/**
	 * 共同借款人核准號碼<br>
	 * 
	 * @return Integer
	 */
	public int getRelApplNo() {
		return this.relApplNo;
	}

	/**
	 * 共同借款人核准號碼<br>
	 * 
	 *
	 * @param relApplNo 共同借款人核准號碼
	 */
	public void setRelApplNo(int relApplNo) {
		this.relApplNo = relApplNo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(applNo, relApplNo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		FacShareRelationId facShareRelationId = (FacShareRelationId) obj;
		return applNo == facShareRelationId.applNo && relApplNo == facShareRelationId.relApplNo;
	}

	@Override
	public String toString() {
		return "FacShareRelationId [applNo=" + applNo + ", relApplNo=" + relApplNo + "]";
	}
}
