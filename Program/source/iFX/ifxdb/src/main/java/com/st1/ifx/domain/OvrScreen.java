package com.st1.ifx.domain;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "FX_OVR_SCREEN")
public class OvrScreen implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7126762246267016102L;

	@Column(name = "ID")
	// @GeneratedValue(strategy = GenerationType.AUTO)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FX_OVR_SCREEN_SEQ")
	@SequenceGenerator(name = "FX_OVR_SCREEN_SEQ", sequenceName = "FX_OVR_SCREEN_SEQ", allocationSize = 1)
	@Id
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OVR_ID")
	private Ovr ovr;

	@Column(name = "INDX")
	private int indx;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "BUF")
	private String buf;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Ovr getOvr() {
		return ovr;
	}

	public void setOvr(Ovr ovr) {
		this.ovr = ovr;
		if (!ovr.getBuffers().contains(this)) {
			ovr.getBuffers().add(this);
		}
	}

	public int getIndx() {
		return indx;
	}

	public void setIndx(int indx) {
		this.indx = indx;
	}

	public String getBuf() {
		return buf;
	}

	public void setBuf(String buf) {
		this.buf = buf;
	}

	@Override
	public String toString() {
		return "OvrScreen [id=" + id + ", index=" + indx + ", buf=" + buf + "]";
	}

}
