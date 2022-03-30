package com.st1.ifx.domain;

import java.io.Serializable;
import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "FX_TRAN_DOC_LOG")
public class TranDocLog implements Serializable {
	private static final long serialVersionUID = 65793360232904685L;

	@Column(name = "ID")
	// @GeneratedValue(strategy = GenerationType.AUTO)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FX_TRAN_DOC_LOG_SEQ")
	@SequenceGenerator(name = "FX_TRAN_DOC_LOG_SEQ", sequenceName = "FX_TRAN_DOC_LOG_SEQ", allocationSize = 1)
	@Id
	private Long id;

	@Column(name = "DOC_ID")
	private Long docId;

	@Column(name = "PRINT_BRN")
	private String printBrno;

	@Column(name = "PRINT_DATE")
	private java.sql.Date printDate;

	@Column(name = "PRINT_TIME")
	private java.sql.Time printTime;

	@Column(name = "PRINT_TLRNO")
	private String printTlrno;

	public void touch() {
		java.util.Date today = new java.util.Date();
		long t = today.getTime();
		this.printDate = new java.sql.Date(t);
		this.printTime = new Time(t);
		// this.audits = new ArrayList<JournalAudit>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDocId() {
		return docId;
	}

	public void setDocId(Long docId) {
		this.docId = docId;
	}

	public String getPrintBrno() {
		return printBrno;
	}

	public void setPrintBrno(String printBrno) {
		this.printBrno = printBrno;
	}

	public java.sql.Date getPrintDate() {
		return printDate;
	}

	public void setPrintDate(java.sql.Date printDate) {
		this.printDate = printDate;
	}

	public java.sql.Time getPrintTime() {
		return printTime;
	}

	public void setPrintTime(java.sql.Time printTime) {
		this.printTime = printTime;
	}

	public String getPrintTlrno() {
		return printTlrno;
	}

	public void setPrintTlrno(String printTlrno) {
		this.printTlrno = printTlrno;
	}

	@Override
	public String toString() {
		return "TranDocLog [id=" + id + ", docId=" + docId + ", printBrno=" + printBrno + ", printDate=" + printDate
				+ ", printTime=" + printTime + ", printTlrno=" + printTlrno + "]";
	}

}
