package com.st1.ifx.service;

public class JournalScanReq {
	String opt;
	String order;
	String txno;
	String brn;
	String tlrno;
	String txcode;
	String sysDateFrom;
	String sysDateTo;
	String sysTimeFrom;
	String sysTimeTo;

	String busDateFrom;
	String busDateTo;

	public String seqFrom;
	public String seqTo;

	public String getOpt() {
		return opt;
	}

	public void setOpt(String opt) {
		this.opt = opt;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getTxno() {
		return txno;
	}

	public void setTxno(String txno) {
		this.txno = txno;
	}

	public String getBrn() {
		return brn;
	}

	public void setBrn(String brn) {
		this.brn = brn;
	}

	public String getTlrno() {
		return tlrno;
	}

	public void setTlrno(String tlrno) {
		this.tlrno = tlrno;
	}

	public String getTxcode() {
		return txcode;
	}

	public void setTxcode(String txcode) {
		this.txcode = txcode;
	}

	public String getSysDateFrom() {
		return sysDateFrom;
	}

	public void setSysDateFrom(String sysDateFrom) {
		this.sysDateFrom = sysDateFrom;
	}

	public String getSysDateTo() {
		return sysDateTo;
	}

	public void setSysDateTo(String sysDateTo) {
		this.sysDateTo = sysDateTo;
	}

	public String getSysTimeFrom() {
		return sysTimeFrom;
	}

	public void setSysTimeFrom(String sysTimeFrom) {
		this.sysTimeFrom = sysTimeFrom;
	}

	public String getSysTimeTo() {
		return sysTimeTo;
	}

	public void setSysTimeTo(String sysTimeTo) {
		this.sysTimeTo = sysTimeTo;
	}

	public String getBusDateFrom() {
		return busDateFrom;
	}

	public void setBusDateFrom(String busDateFrom) {
		this.busDateFrom = busDateFrom;
	}

	public String getBusDateTo() {
		return busDateTo;
	}

	public void setBusDateTo(String busDateTo) {
		this.busDateTo = busDateTo;
	}

	public String getSeqFrom() {
		return seqFrom;
	}

	public void setSeqFrom(String seqFrom) {
		this.seqFrom = seqFrom;
	}

	public String getSeqTo() {
		return seqTo;
	}

	public void setSeqTo(String seqTo) {
		this.seqTo = seqTo;
	}

	@Override
	public String toString() {
		return "JournalScanReq [txno=" + txno + ", brn=" + brn + ", tlrno=" + tlrno + ", txcode=" + txcode
				+ ", sysDateFrom=" + sysDateFrom + ", sysDateTo=" + sysDateTo + ", sysTimeFrom=" + sysTimeFrom
				+ ", sysTimeTo=" + sysTimeTo + ", busDateFrom=" + busDateFrom + ", busDateTo=" + busDateTo
				+ ", seqFrom=" + seqFrom + ", seqTo=" + seqTo + "]";
	}

}
