package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * FacShareSub 共用額度副檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class FacShareSubId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4680114319200889486L;

// 共用戶號
	@Column(name = "`ShareCustNo`")
	private int shareCustNo = 0;

	// 共用額度
	@Column(name = "`ShareFacmNo`")
	private int shareFacmNo = 0;

	public FacShareSubId() {
	}

	public FacShareSubId(int shareCustNo, int shareFacmNo) {
		this.shareCustNo = shareCustNo;
		this.shareFacmNo = shareFacmNo;
	}

	/**
	 * 共用戶號<br>
	 * 
	 * @return Integer
	 */
	public int getShareCustNo() {
		return this.shareCustNo;
	}

	/**
	 * 共用戶號<br>
	 * 
	 *
	 * @param shareCustNo 共用戶號
	 */
	public void setShareCustNo(int shareCustNo) {
		this.shareCustNo = shareCustNo;
	}

	/**
	 * 共用額度<br>
	 * 
	 * @return Integer
	 */
	public int getShareFacmNo() {
		return this.shareFacmNo;
	}

	/**
	 * 共用額度<br>
	 * 
	 *
	 * @param shareFacmNo 共用額度
	 */
	public void setShareFacmNo(int shareFacmNo) {
		this.shareFacmNo = shareFacmNo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(shareCustNo, shareFacmNo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		FacShareSubId facShareSubId = (FacShareSubId) obj;
		return shareCustNo == facShareSubId.shareCustNo && shareFacmNo == facShareSubId.shareFacmNo;
	}

	@Override
	public String toString() {
		return "FacShareSubId [shareCustNo=" + shareCustNo + ", shareFacmNo=" + shareFacmNo + "]";
	}
}
