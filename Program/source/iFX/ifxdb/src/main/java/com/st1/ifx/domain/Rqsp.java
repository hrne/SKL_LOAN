package com.st1.ifx.domain;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "FX_RQSP")
public class Rqsp {

	@Column(name = "ID")
	// @GeneratedValue(strategy = GenerationType.AUTO)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FX_RQSP_SEQ")
	@SequenceGenerator(name = "FX_RQSP_SEQ", sequenceName = "FX_RQSP_SEQ", allocationSize = 1)
	@Id
	private Long id;

	@Column(name = "JNL_ID")
	private Long jnlId;

	@Column(name = "SEND_TIMES")
	private int sendTimes;

	@Column(name = "DATED")
	private java.sql.Date dated;

	@Column(name = "TIME")
	private java.sql.Time time;

	@Column(name = "BRNO")
	private String brno;

	@Column(name = "SUPNO")
	private String supno;

	@Column(name = "TLRNO")
	private String tlrno;

	@Column(name = "OVRTYPE")
	private int ovrType;

	@ElementCollection(targetClass = RqspCode.class, fetch = FetchType.EAGER)
	@CollectionTable(name = "FX_RQSP_CODE", joinColumns = @JoinColumn(name = "RQSP_ID"))
	private List<RqspCode> codes;

	public void touch() {
		java.util.Date today = new java.util.Date();
		long t = today.getTime();
		this.dated = new java.sql.Date(t);
		this.time = new Time(t);
		// this.audits = new ArrayList<JournalAudit>();
	}

	public void createCodes(String t) {
		String[] ss = t.split("\n");
		List<RqspCode> list = new ArrayList<RqspCode>();
		for (String s : ss) {
			s = s.trim();
			if (s.length() > 5) {
				list.add(new RqspCode(s.substring(0, 4), s.substring(5), list.size() + 1));
			}
		}
		this.setCodes(list);
		;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getJnlId() {
		return jnlId;
	}

	public void setJnlId(Long jnlId) {
		this.jnlId = jnlId;
	}

	public int getSendTimes() {
		return sendTimes;
	}

	public void setSendTimes(int sendTimes) {
		this.sendTimes = sendTimes;
	}

	public java.sql.Date getDated() {
		return dated;
	}

	public void setDated(java.sql.Date dated) {
		this.dated = dated;
	}

	public java.sql.Time getTime() {
		return time;
	}

	public void setTime(java.sql.Time time) {
		this.time = time;
	}

	public String getBrno() {
		return brno;
	}

	public void setBrno(String brno) {
		this.brno = brno;
	}

	public String getSupno() {
		return supno;
	}

	public void setSupno(String supno) {
		this.supno = supno;
	}

	public String getTlrno() {
		return tlrno;
	}

	public void setTlrno(String tlrno) {
		this.tlrno = tlrno;
	}

	public int getOvrType() {
		return ovrType;
	}

	public void setOvrType(int ovrType) {
		this.ovrType = ovrType;
	}

	public List<RqspCode> getCodes() {
		return codes;
	}

	public void setCodes(List<RqspCode> codes) {
		this.codes = codes;
	}

	@Override
	public String toString() {
		return "Rqsp [id=" + id + ", jnlId=" + jnlId + ", sendTimes=" + sendTimes + ", date=" + dated + ", time=" + time
				+ ", brno=" + brno + ", supno=" + supno + ", tlrno=" + tlrno + ", ovrType=" + ovrType + ", codes="
				+ codes + "]";
	}

}
