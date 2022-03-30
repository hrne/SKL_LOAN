package com.st1.ifx.domain;

import java.sql.Date;
import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "FX_CONSOLE_LOG")
public class ConsoleLog {
	@Column(name = "ID")
	// @GeneratedValue(strategy = GenerationType.AUTO)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FX_CONSOLE_LOG_SEQ")
	@SequenceGenerator(name = "FX_CONSOLE_LOG_SEQ", sequenceName = "FX_CONSOLE_LOG_SEQ", allocationSize = 1)
	@Id
	private Long id;

	@Column(name = "ORIG")
	private String orig;

	@Column(name = "BRNO")
	private String brno;

	@Column(name = "DATE")
	private Date date;

	@Column(name = "TIME")
	private Time time;

	@Column(name = "LEVEL")
	private String level;

	@Column(name = "TEXT")
	private String text;

	public void touch() {
		java.util.Date today = new java.util.Date();
		long t = today.getTime();
		date = new java.sql.Date(t);
		time = new Time(t);
	}

	public String getOrig() {
		return orig;
	}

	public void setOrig(String orig) {
		this.orig = orig;
	}

	public String getBrno() {
		return brno;
	}

	public void setBrno(String brno) {
		this.brno = brno;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "ConsoleLog [id=" + id + ", orig=" + orig + ", brno=" + brno + ", date=" + date + ", time=" + time
				+ ", level=" + level + ", text=" + text + "]";
	}

}
