package com.st1.ifx.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "FX_TRAN_DOC")
public class TranDoc implements Serializable {

	private static final long serialVersionUID = -5778365814390282779L;
	@Column(name = "DOC_ID")
	// @GeneratedValue(strategy = GenerationType.AUTO)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FX_TRAN_DOC_SEQ")
	@SequenceGenerator(name = "FX_TRAN_DOC_SEQ", sequenceName = "FX_TRAN_DOC_SEQ", allocationSize = 1)
	@Id
	private Long docId;

	@Column(name = "JNL_ID")
	private Long jnlId;

	@Column(name = "SRH_TXCODE")
	private String srhTxcode;

	@Column(name = "SRH_KINBR")
	private String srhKinbr;

	@Column(name = "SRH_RBRNO")
	private String srhRbrno;

	@Column(name = "SRH_FBRNO")
	private String srhFbrno;

	@Column(name = "SRH_ACBRNO")
	private String srhAcbrno;

	@Column(name = "SRH_PBRNO")
	private String srhPbrno;

	@Column(name = "SRH_CIFKEY")
	private String srhCifkey;

	@Column(name = "SRH_MRKEY")
	private String srhMrkey;

	@Column(name = "SRH_CURRENCY")
	private String srhCurrency;

	@Column(name = "SRH_TXAMT")
	private String srhTxamt;

	@Column(name = "SRH_BATNO")
	private String srhBatno;

	@Column(name = "SRH_BUSDATE")
	private String srhBusdate;

	@Column(name = "SRH_TEMP")
	private String srhTemp;

	@Column(name = "DOC_NAME")
	private String docName;

	@Column(name = "DOC_PROMPT")
	private String docPrompt;

	@Column(name = "DOC_PARAMETER")
	private String docParameter;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "doc")
	@OrderBy("bufIndex asc")
	private List<TranDocBuf> buffers = new ArrayList<TranDocBuf>();

	public void addBuffer(TranDocBuf buf) {
		this.buffers.add(buf);
		if (buf.getDoc() != this) {
			buf.setDoc(this);
		}
	}

	public Long getDocId() {
		return docId;
	}

	public void setDocId(Long docId) {
		this.docId = docId;
	}

	public Long getJnlId() {
		return jnlId;
	}

	public void setJnlId(Long jnlId) {
		this.jnlId = jnlId;
	}

	public String getSrhTxcode() {
		return srhTxcode;
	}

	public void setSrhTxcode(String srhTxcode) {
		this.srhTxcode = srhTxcode;
	}

	public String getSrhKinbr() {
		return srhKinbr;
	}

	public void setSrhKinbr(String srhKinbr) {
		this.srhKinbr = srhKinbr;
	}

	public String getSrhRbrno() {
		return srhRbrno;
	}

	public void setSrhRbrno(String srhRbrno) {
		this.srhRbrno = srhRbrno;
	}

	public String getSrhFbrno() {
		return srhFbrno;
	}

	public void setSrhFbrno(String srhFbrno) {
		this.srhFbrno = srhFbrno;
	}

	public String getSrhAcbrno() {
		return srhAcbrno;
	}

	public void setSrhAcbrno(String srhAcbrno) {
		this.srhAcbrno = srhAcbrno;
	}

	public String getSrhPbrno() {
		return srhPbrno;
	}

	public void setSrhPbrno(String srhPbrno) {
		this.srhPbrno = srhPbrno;
	}

	public String getSrhCifkey() {
		return srhCifkey;
	}

	public void setSrhCifkey(String srhCifkey) {
		this.srhCifkey = srhCifkey;
	}

	public String getSrhMrkey() {
		return srhMrkey;
	}

	public void setSrhMrkey(String srhMrkey) {
		this.srhMrkey = srhMrkey;
	}

	public String getSrhCurrency() {
		return srhCurrency;
	}

	public void setSrhCurrency(String srhCurrency) {
		this.srhCurrency = srhCurrency;
	}

	public String getSrhTxamt() {
		return srhTxamt;
	}

	public void setSrhTxamt(String srhTxamt) {
		this.srhTxamt = srhTxamt;
	}

	public String getSrhBatno() {
		return srhBatno;
	}

	public void setSrhBatno(String srhBatno) {
		this.srhBatno = srhBatno;
	}

	public String getSrhBusdate() {
		return srhBusdate;
	}

	public void setSrhBusdate(String srhBusdate) {
		this.srhBusdate = srhBusdate;
	}

	public String getSrhTemp() {
		return srhTemp;
	}

	public void setSrhTemp(String srhTemp) {
		this.srhTemp = srhTemp;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public String getDocPrompt() {
		return docPrompt;
	}

	public void setDocPrompt(String docPrompt) {
		this.docPrompt = docPrompt;
	}

	public String getDocParameter() {
		return docParameter;
	}

	public void setDocParameter(String docParameter) {
		this.docParameter = docParameter;
	}

	public List<TranDocBuf> getBuffers() {
		return buffers;
	}

	public void setBuffers(List<TranDocBuf> buffers) {
		this.buffers = buffers;
	}

	@Override
	public String toString() {
		return "TranDoc [docId=" + docId + ", jnlId=" + jnlId + ", srhTxcode=" + srhTxcode + ", srhKinbr=" + srhKinbr
				+ ", srhRbrno=" + srhRbrno + ", srhFbrno=" + srhFbrno + ", srhAcbrno=" + srhAcbrno + ", srhPbrno="
				+ srhPbrno + ", srhCifkey=" + srhCifkey + ", srhMrkey=" + srhMrkey + ", srhCurrency=" + srhCurrency
				+ ", srhTxamt=" + srhTxamt + ", srhBatno=" + srhBatno + ", srhBusdate=" + srhBusdate + ", srhTemp="
				+ srhTemp + ", docName=" + docName + ", docPrompt=" + docPrompt + ", docParameter=" + docParameter
				+ ", buffers=" + buffers.size() + "]";
	}

}
