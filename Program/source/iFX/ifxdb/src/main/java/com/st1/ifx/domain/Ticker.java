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
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "FX_TICKER")
public class Ticker implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9028868329250142603L;

	@Column(name = "ID")
	// @GeneratedValue(strategy = GenerationType.AUTO)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FX_TICKER_SEQ")
	@SequenceGenerator(name = "FX_TICKER_SEQ", sequenceName = "FX_TICKER_SEQ", allocationSize = 1)
	@Id
	private Long id;

	@Column(name = "DATED")
	private java.sql.Date dated;

	@Column(name = "TIME")
	private java.sql.Time time;

	public void touch() {
		java.util.Date today = new java.util.Date();
		long t = today.getTime();
		this.setDated(new java.sql.Date(t));
		this.setTime(new Time(t));
	}

	@Column(name = "BRNO")
	private String brno;

	@Column(name = "TICKNO")
	private String tickno;

	public boolean isUniqueTick() {
		return tickno.charAt(0) == 'U';
	}

	public boolean isUniqueSkiptick() {
		return tickno.charAt(0) == 'S';
	}

	@Column(name = "CONTENT", length = 512)
	private String content;

	@Column(name = "STOP_TIME")
	private Long stopTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getStopTime() {
		return stopTime;
	}

	public void setStopTime(Long stopTime) {
		this.stopTime = stopTime;
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

	public String getTickno() {
		return tickno;
	}

	public void setTickno(String tickno) {
		this.tickno = tickno;
	}

	@Override
	public String toString() {
		return "Ticker [id=" + id + ", date=" + dated + ", time=" + time + ", brno=" + brno + ", tickno=" + tickno
				+ ", content=" + content + ", stopTime=" + stopTime + "]";
	}

}
