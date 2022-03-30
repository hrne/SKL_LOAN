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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "FX_LOCAL_RIM")
public class LocalRim implements Serializable {

	private static final long serialVersionUID = -3286555387150383654L;

	@Column(name = "ID")
	// @GeneratedValue(strategy = GenerationType.AUTO)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FX_LOCAL_RIM_SEQ")
	@SequenceGenerator(name = "FX_LOCAL_RIM_SEQ", sequenceName = "FX_LOCAL_RIM_SEQ", allocationSize = 1)
	@Id
	private Long id;

	@Column(name = "TABLENM", length = 8)
	private String tableName;

	// DB2 -> SQL SERVER 不能有這個關鍵字 KEY -> D_KEY
	@Column(name = "XEY", length = 20)
	private String xey;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "DATA", length = 4450)
	private String data;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getXey() {
		return xey;
	}

	public void setXey(String xey) {
		this.xey = xey;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "LocalRim [id=" + id + ", tableName=" + tableName + ", key=" + xey + ", data=" + data + "]";
	}

}
