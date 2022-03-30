package com.st1.ifx.domain;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "FX_OVR")
public class Ovr implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8325754903502676405L;

	@Column(name = "ID")
	// @GeneratedValue(strategy = GenerationType.AUTO)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FX_OVR_SEQ")
	@SequenceGenerator(name = "FX_OVR_SEQ", sequenceName = "FX_OVR_SEQ", allocationSize = 1)
	@Id
	private Long id;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ovr")
	@OrderBy("indx asc")
	private List<OvrScreen> buffers = new ArrayList<OvrScreen>();

	public void addScreenBuffer(OvrScreen buf) {
		this.buffers.add(buf);
		if (buf.getOvr() != this) {
			buf.setOvr(this);
		}
	}

	public void touch() {
		java.util.Date today = new java.util.Date();
		long t = today.getTime();
		this.dated = new java.sql.Date(t);
		this.timet = new Time(t);
		// this.audits = new ArrayList<JournalAudit>();
	}

	@Column(name = "BRN")
	private String brn;

	@Column(name = "TLRNO")
	private String tlrno;

	@Column(name = "DATED")
	private java.sql.Date dated;

	@Column(name = "TIMET")
	private java.sql.Time timet;

	@Column(name = "TXCD")
	private String txcd;

	@Column(name = "RQSP")
	private String rqsp;

	@Column(name = "RQSP_MSG")
	private String rqspMessage;

	@Column(name = "SUPBRN")
	private String supBrn;

	@Column(name = "SUPNO")
	private String supNo;

	@Column(name = "SUP_TIME")
	private java.sql.Time supTime;

	@Column(name = "STATUS")
	private int status;

	@Column(name = "MESSAGE")
	private String message;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<OvrScreen> getBuffers() {
		return buffers;
	}

	public void setBuffers(List<OvrScreen> buffers) {
		this.buffers = buffers;
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

	public java.sql.Date getDated() {
		return dated;
	}

	public void setDated(java.sql.Date dated) {
		this.dated = dated;
	}

	public java.sql.Time getTimet() {
		return timet;
	}

	public void setTimet(java.sql.Time timet) {
		this.timet = timet;
	}

	public String getTxcd() {
		return txcd;
	}

	public void setTxcd(String txcd) {
		this.txcd = txcd;
	}

	public String getRqsp() {
		return rqsp;
	}

	public void setRqsp(String rqsp) {
		this.rqsp = rqsp;
	}

	public String getSupBrn() {
		return supBrn;
	}

	public void setSupBrn(String supBrn) {
		this.supBrn = supBrn;
	}

	public String getSupNo() {
		return supNo;
	}

	public void setSupNo(String supNo) {
		this.supNo = supNo;
	}

	public Time getSupTime() {
		return supTime;
	}

	public void setSupTime(Time supTime) {
		this.supTime = supTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getRqspMessage() {
		return rqspMessage;
	}

	public void setRqspMessage(String rqspMessage) {
		this.rqspMessage = rqspMessage;
	}

	@Override
	public String toString() {
		return "Ovr [id=" + id + ", buffers=" + buffers + ", brn=" + brn + ", tlrno=" + tlrno + ", dated=" + dated
				+ ", timet=" + timet + ", txcd=" + txcd + ", rqsp=" + rqsp + ", rqspMessage=" + rqspMessage
				+ ", supBrn=" + supBrn + ", supNo=" + supNo + ", supTime=" + supTime + ", status=" + status
				+ ", message=" + message + "]";
	}

}
