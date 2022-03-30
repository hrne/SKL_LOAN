package com.st1.ifx.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "FX_MSG_BOX")
public class MsgBox implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5349851405237929953L;

	@Column(name = "ID")
	// @GeneratedValue(strategy = GenerationType.AUTO)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FX_MSG_BOX_SEQ")
	@SequenceGenerator(name = "FX_MSG_BOX_SEQ", sequenceName = "FX_MSG_BOX_SEQ", allocationSize = 1)
	@Id
	private Long id;

	@Column(name = "BRNO")
	private String brno;

	@Column(name = "TLRNO")
	private String tlrno;

	@Column(name = "RCV_DATE")
	private java.sql.Date rcvDate;

	@Column(name = "RCV_TIME")
	private java.sql.Time rcvTime;

	@Column(name = "VALID_TIME")
	private Long validTime;

	@Column(name = "MSGNO")
	private String msgno;

	public boolean isUniqueMessage() {
		return msgno.charAt(0) == 'U';
	}

	public boolean isUniqueSkipmessage() {
		return msgno.charAt(0) == 'S';
	}

	@Column(name = "CONTENT")
	private String content;

	@Column(name = "VIEW_DATE")
	private java.sql.Date viewDate;

	@Column(name = "VIEW_TIME")
	private java.sql.Time viewTime;

	@Column(name = "DONE")
	private char done;

	// @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch =
	// FetchType.EAGER)
	// @JoinColumn(name = "MSG_ID")
	// private MsgCenter msgCenter;
	//
	// public MsgCenter getMsgCenter() {
	// return msgCenter;
	// }
	//
	// public void setMsgCenter(MsgCenter msgCenter) {
	// this.msgCenter = msgCenter;
	// if (!msgCenter.getBoxes().contains(this)) {
	// msgCenter.getBoxes().add(this);
	// }
	// }
	//
	//
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBrno() {
		return brno;
	}

	public void setBrno(String brno) {
		this.brno = brno;
	}

	public String getTlrno() {
		return tlrno;
	}

	public void setTlrno(String tlrno) {
		this.tlrno = tlrno;
	}

	public java.sql.Date getViewDate() {
		return viewDate;
	}

	public void setViewDate(java.sql.Date viewDate) {
		this.viewDate = viewDate;
	}

	public java.sql.Time getViewTime() {
		return viewTime;
	}

	public void setViewTime(java.sql.Time viewTime) {
		this.viewTime = viewTime;
	}

	public char getDone() {
		return done;
	}

	public void setDone(char done) {
		this.done = done;
	}

	public Long getValidTime() {
		return validTime;
	}

	public void setValidTime(Long validTime) {
		this.validTime = validTime;
	}

	public String getMsgno() {
		return msgno;
	}

	public void setMsgno(String msgno) {
		this.msgno = msgno;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public java.sql.Date getRcvDate() {
		return rcvDate;
	}

	public void setRcvDate(java.sql.Date rcvDate) {
		this.rcvDate = rcvDate;
	}

	public java.sql.Time getRcvTime() {
		return rcvTime;
	}

	public void setRcvTime(java.sql.Time rcvTime) {
		this.rcvTime = rcvTime;
	}

	@Override
	public String toString() {
		return "MsgBox [id=" + id + ", brno=" + brno + ", tlrno=" + tlrno + ", rcvDate=" + rcvDate + ", rcvTime="
				+ rcvTime + ", validTime=" + validTime + ", msgno=" + msgno + ", content=" + content + ", viewDate="
				+ viewDate + ", viewTime=" + viewTime + ", done=" + done + "]";
	}

}
