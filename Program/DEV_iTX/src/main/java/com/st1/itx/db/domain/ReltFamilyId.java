package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * ReltFamily 利害關係人親屬檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class ReltFamilyId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1936704356535678684L;

	// 利害關係人識別碼
	@Column(name = "`ReltUKey`", length = 32)
	private String reltUKey = " ";

	// 序號
	@Column(name = "`ReltSeq`")
	private int reltSeq = 0;

	public ReltFamilyId() {
	}

	public ReltFamilyId(String reltUKey, int reltSeq) {
		this.reltUKey = reltUKey;
		this.reltSeq = reltSeq;
	}

	/**
	 * 利害關係人識別碼<br>
	 * 
	 * @return String
	 */
	public String getReltUKey() {
		return this.reltUKey == null ? "" : this.reltUKey;
	}

	/**
	 * 利害關係人識別碼<br>
	 * 
	 *
	 * @param reltUKey 利害關係人識別碼
	 */
	public void setReltUKey(String reltUKey) {
		this.reltUKey = reltUKey;
	}

	/**
	 * 序號<br>
	 * 
	 * @return Integer
	 */
	public int getReltSeq() {
		return this.reltSeq;
	}

	/**
	 * 序號<br>
	 * 
	 *
	 * @param reltSeq 序號
	 */
	public void setReltSeq(int reltSeq) {
		this.reltSeq = reltSeq;
	}

	@Override
	public int hashCode() {
		return Objects.hash(reltUKey, reltSeq);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		ReltFamilyId reltFamilyId = (ReltFamilyId) obj;
		return reltUKey.equals(reltFamilyId.reltUKey) && reltSeq == reltFamilyId.reltSeq;
	}

	@Override
	public String toString() {
		return "ReltFamilyId [reltUKey=" + reltUKey + ", reltSeq=" + reltSeq + "]";
	}
}
