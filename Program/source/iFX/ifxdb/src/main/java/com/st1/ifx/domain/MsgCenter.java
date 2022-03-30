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
@Table(name = "FX_MSG_CENTER")
public class MsgCenter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 150541366042643826L;

	public MsgBox makeMsgBox() {
		MsgBox b = new MsgBox();
		b.setBrno(brno);
		b.setTlrno(tlrno);
		b.setDone('N');
		// b.setMsgCenter(this);
		return b;
	}

	@Column(name = "ID")
	// @GeneratedValue(strategy = GenerationType.AUTO)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FX_MSG_CENTER_SEQ")
	@SequenceGenerator(name = "FX_MSG_CENTER_SEQ", sequenceName = "FX_MSG_CENTER_SEQ", allocationSize = 1)
	@Id
	private Long id;

	// @OneToMany(cascade = CascadeType.ALL, mappedBy = "msgCenter")
	// private List<MsgBox> boxes = new ArrayList<MsgBox>();
	// public void addBox(MsgBox box) {
	// this.boxes.add(box);
	//// if (box.getMsgCenter()!= this) {
	//// box.setMsgCenter(this);
	//// }
	// }
	// public List<MsgBox> getBoxes() {
	// return boxes;
	// }
	//
	// public void setBoxes(List<MsgBox> boxes) {
	// this.boxes = boxes;
	// }
	//
	@Column(name = "RCV_DATE")
	private java.sql.Date rcvDate;

	@Column(name = "RCV_TIME")
	private java.sql.Time rcvTime;

	@Column(name = "MSGNO")
	private String msgno;

	public boolean isUniqueMessage() {
		return msgno.charAt(0) == 'U';
	}

	@Column(name = "CONTENT")
	private String content;

	@Column(name = "BRNO")
	private String brno;

	@Column(name = "TLRNO")
	private String tlrno;

	@Column(name = "VALID_TIME")
	private Long validTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	@Override
	public String toString() {
		return "MsgCenter [id=" + id + ", rcvDate=" + rcvDate + ", rcvTime=" + rcvTime + ", msgno=" + msgno
				+ ", content=" + content + ", brno=" + brno + ", tlrno=" + tlrno + ", validTime=" + validTime + "]";
	}

}
