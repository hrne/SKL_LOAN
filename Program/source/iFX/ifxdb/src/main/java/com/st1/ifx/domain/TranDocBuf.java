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
@Table(name = "FX_TRAN_DOC_BUF")
public class TranDocBuf implements Serializable {

	private static final long serialVersionUID = -7099902668752303626L;

	@Column(name = "ID")
	// @GeneratedValue(strategy = GenerationType.AUTO)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FX_TRAN_DOC_BUF_SEQ")
	@SequenceGenerator(name = "FX_TRAN_DOC_BUF_SEQ", sequenceName = "FX_TRAN_DOC_BUF_SEQ", allocationSize = 1)
	@Id
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DOC_ID")
	private TranDoc doc;

	@Column(name = "BUF_INDEX")
	private int bufIndex;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "BUFFER")
	private String buffer;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getBufIndex() {
		return bufIndex;
	}

	public void setBufIndex(int bufIndex) {
		this.bufIndex = bufIndex;
	}

	public String getBuffer() {
		return buffer;
	}

	public void setBuffer(String buffer) {
		this.buffer = buffer;
	}

	public TranDoc getDoc() {
		return doc;
	}

	public void setDoc(TranDoc doc) {
		this.doc = doc;
		if (!doc.getBuffers().contains(this)) {
			doc.getBuffers().add(this);
		}
	}

	@Override
	public String toString() {
		return "TranDocBuf [id=" + id + ", bufIndex=" + bufIndex + ", buffer=" + buffer + "]";
	}

}
