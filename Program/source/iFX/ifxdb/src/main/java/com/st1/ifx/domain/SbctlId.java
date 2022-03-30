package com.st1.ifx.domain;

import java.io.Serializable;

public class SbctlId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1666089374567558177L;

	private int type;
	private String brno;
	private String tlrno;
	private String sbtyp;

	public SbctlId() {
	}

	public SbctlId(int type, String brno, String tlrno, String sbtyp) {
		this.type = type;
		this.brno = brno;
		this.tlrno = tlrno;
		this.sbtyp = sbtyp;
	}

	public int getType() {
		return type;
	}

	public String getBrno() {
		return brno;
	}

	public String getTlrno() {
		return tlrno;
	}

	public String getSbtyp() {
		return sbtyp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((brno == null) ? 0 : brno.hashCode());
		result = prime * result + ((sbtyp == null) ? 0 : sbtyp.hashCode());
		result = prime * result + ((tlrno == null) ? 0 : tlrno.hashCode());
		result = prime * result + type;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SbctlId other = (SbctlId) obj;
		if (brno == null) {
			if (other.brno != null)
				return false;
		} else if (!brno.equals(other.brno))
			return false;
		if (sbtyp == null) {
			if (other.sbtyp != null)
				return false;
		} else if (!sbtyp.equals(other.sbtyp))
			return false;
		if (tlrno == null) {
			if (other.tlrno != null)
				return false;
		} else if (!tlrno.equals(other.tlrno))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SbctlId [type=" + type + ", brno=" + brno + ", tlrno=" + tlrno + ", sbtyp=" + sbtyp + "]";
	}

}
