package com.st1.ifx.domain;

import java.io.Serializable;

public class TxcdGrbrfgId implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5679913350627560720L;
	private String txcd;
	private int fxlvl;

	public TxcdGrbrfgId() {
	}

	public TxcdGrbrfgId(String txcd, int fxlvl) {
		this.txcd = txcd;
		this.fxlvl = fxlvl;
	}

	public String getTxcd() {
		return txcd;
	}

	public int getFxlvl() {
		return fxlvl;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + fxlvl;
		result = prime * result + ((txcd == null) ? 0 : txcd.hashCode());
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
		TxcdGrbrfgId other = (TxcdGrbrfgId) obj;
		if (fxlvl != other.fxlvl)
			return false;
		if (txcd == null) {
			if (other.txcd != null)
				return false;
		} else if (!txcd.equals(other.txcd))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TxcdGrbrfgId [txcd=" + txcd + ", fxlvl=" + fxlvl + "]";
	}

}
