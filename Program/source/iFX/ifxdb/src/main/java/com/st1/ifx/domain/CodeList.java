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
@Table(name = "FX_CODE_LIST")
public class CodeList implements Serializable {
	private static final long serialVersionUID = 4093934652874953038L;

	@Column(name = "ID")
	// @GeneratedValue(strategy = GenerationType.AUTO)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FX_CODE_LIST_SEQ")
	@SequenceGenerator(name = "FX_CODE_LIST_SEQ", sequenceName = "FX_CODE_LIST_SEQ", allocationSize = 1)
	@Id
	private Long id;

	@Column(name = "HELP", length = 30)
	private String help;

	@Column(name = "SEGMENT")
	private String segment;

	// DB2 -> SQL SERVER 不能有這個關鍵字 KEY -> D_KEY
	@Column(name = "XEY", length = 30)
	private String xey;

	// DB2 -> SQL SERVER 不能有這個關鍵字 CONTENT -> D_CONTENT
	@Column(name = "CONTENT", length = 300)
	private String content;

	@Column(name = "UPDATE_DATE")
	private java.sql.Date updateDate;

	@Column(name = "UPDATE_TIME")
	private java.sql.Time updateTime;

	@Column(name = "DISPLAY_ORDER")
	private int displayOrder;

	public void touch() {
		java.util.Date today = new java.util.Date();
		long t = today.getTime();
		this.updateDate = new java.sql.Date(t);
		this.updateTime = new Time(t);
	}

	public String getHelp() {
		return help;
	}

	public void setHelp(String help) {
		this.help = help;
	}

	public String getSegment() {
		return segment;
	}

	public void setSegment(String segment) {
		this.segment = segment;
	}

	public String getXey() {
		return xey;
	}

	public void setXey(String xey) {
		this.xey = xey;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public java.sql.Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(java.sql.Date updateDate) {
		this.updateDate = updateDate;
	}

	public java.sql.Time getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(java.sql.Time updateTime) {
		this.updateTime = updateTime;
	}

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "CodeList [id=" + id + ", help=" + help + ", segment=" + segment + ", key=" + xey + ", content="
				+ content + ", updateDate=" + updateDate + ", updateTime=" + updateTime + ", displayOrder="
				+ displayOrder + "]";
	}

}
