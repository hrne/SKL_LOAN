package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * RelsFamily (準)利害關係人親屬檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class RelsFamilyId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3930816742653617096L;

	// (準)利害關係人識別碼
	@Column(name = "`RelsUKey`", length = 32)
	private String relsUKey = " ";

	// 序號
	@Column(name = "`RelsSeq`")
	private int relsSeq = 0;

	public RelsFamilyId() {
	}

	public RelsFamilyId(String relsUKey, int relsSeq) {
		this.relsUKey = relsUKey;
		this.relsSeq = relsSeq;
	}

	/**
	 * (準)利害關係人識別碼<br>
	 * 
	 * @return String
	 */
	public String getRelsUKey() {
		return this.relsUKey == null ? "" : this.relsUKey;
	}

	/**
	 * (準)利害關係人識別碼<br>
	 * 
	 *
	 * @param relsUKey (準)利害關係人識別碼
	 */
	public void setRelsUKey(String relsUKey) {
		this.relsUKey = relsUKey;
	}

	/**
	 * 序號<br>
	 * 
	 * @return Integer
	 */
	public int getRelsSeq() {
		return this.relsSeq;
	}

	/**
	 * 序號<br>
	 * 
	 *
	 * @param relsSeq 序號
	 */
	public void setRelsSeq(int relsSeq) {
		this.relsSeq = relsSeq;
	}

	@Override
	public int hashCode() {
		return Objects.hash(relsUKey, relsSeq);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		RelsFamilyId relsFamilyId = (RelsFamilyId) obj;
		return relsUKey.equals(relsFamilyId.relsUKey) && relsSeq == relsFamilyId.relsSeq;
	}

	@Override
	public String toString() {
		return "RelsFamilyId [relsUKey=" + relsUKey + ", relsSeq=" + relsSeq + "]";
	}
}
