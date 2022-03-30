package com.st1.ifx.domain;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "HELP_LIST")
@NamedQueries({
		@NamedQuery(name = "HelpList.findActiveHelp", query = "select h from HelpList h where h.help=:help and h.segment=:segment and h.activeDate <= :dt  order by h.activeDate desc, h.version desc") })
public class HelpList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3786825547047464791L;

	@Column(name = "ID")
	// @GeneratedValue(strategy = GenerationType.AUTO)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HELP_LIST_SEQ")
	@SequenceGenerator(name = "HELP_LIST_SEQ", sequenceName = "HELP_LIST_SEQ", allocationSize = 1)
	@Id
	private Long id;

	@Column(name = "HELP", length = 30)
	private String help;

	@Column(name = "SEGMENT", length = 30)
	private String segment;

	@Column(name = "ACTIVE_DATE", length = 8)
	// @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	private String activeDate;

	@Column(name = "VERSION", length = 1)
	private String version;

	@Column(name = "IMPORT_TIME")
	private java.sql.Time importTime;

	@Column(name = "RESIDENT", length = 1)
	private String resident;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "CONTENT", length = 320000)
	private String content;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "JSON", length = 320000)
	private String json;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getActiveDate() {
		return activeDate;
	}

	public void setActiveDate(String activeDate) {
		this.activeDate = activeDate;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public java.sql.Time getImportTime() {
		return importTime;
	}

	public void setImportTime(java.sql.Time importTime) {
		this.importTime = importTime;
	}

	public String getResident() {
		return resident;
	}

	public void setResident(String resident) {
		this.resident = resident;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	@Override
	public String toString() {
		return "HelpList [id=" + id + ", help=" + help + ", segment=" + segment + ", activeDate=" + activeDate
				+ ", version=" + version + ", json=" + json + "]";
	}

}
