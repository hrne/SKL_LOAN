package com.st1.ifx.domain;

import java.io.Serializable;
import java.sql.Time;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "FX_JOURNAL")
@SecondaryTable(name = "FX_JOURNAL_TITA", pkJoinColumns = { @PrimaryKeyJoinColumn(name = "ID") })
public class Journal implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6228378189677313446L;

	// 轉移SQL SERVER時 ID GenerationType 可能要調整
	@Column(name = "ID")
	// @GeneratedValue(strategy = GenerationType.AUTO)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FX_JOURNAL_SEQ")
	@SequenceGenerator(name = "FX_JOURNAL_SEQ", sequenceName = "FX_JOURNAL_SEQ", allocationSize = 1)
	@Id
	private Long id;

	public Journal() {
		touch();
	}

	@Column(name = "jnlDate")
	private java.sql.Date jnlDate;

	@Column(name = "jnlTime")
	private java.sql.Time jnlTime;

	public void touch() {
		java.util.Date today = new java.util.Date();
		long t = today.getTime();
		this.jnlDate = new java.sql.Date(t);
		this.jnlTime = new Time(t);
		// this.audits = new ArrayList<JournalAudit>();
	}

	@Column(name = "busdate", length = 8)
	private String busdate;

	@Column(name = "calday")
	private int calDay = 0;

	@Column(name = "brn", length = 4)
	private String brn;

	@Column(name = "rbrno", length = 4)
	private String rbrno;

	@Column(name = "fbrno", length = 4)
	private String fbrno;

	@Column(name = "acbrno", length = 4)
	private String acbrno;

	@Column(name = "pbrno", length = 4)
	private String pbrno;

	@Column(name = "tlrno", length = 6)
	private String tlrno;

	@Column(name = "lvel", length = 1)
	private String lvel;

	@Column(name = "txno", length = 8)
	private String txno;

	@Column(name = "supno", length = 6)
	private String supno;

	@Column(name = "txcode", length = 5)
	private String txcode;

	// @OneToMany(mappedBy = "journal")
	// @OrderBy(clause = "date, time")
	// private List<JournalAudit> audits;

	@Column(name = "tranStatus")
	private int tranStatus;

	@Column(name = "tranFlag", length = 1)
	private String tranFlag;

	@Column(name = "msgid", length = 5)
	private String msgid;

	@Column(name = "errmsg", length = 300)
	private String errmsg;

	@Column(name = "mrkey", length = 20)
	private String mrkey;

	@Column(name = "currency", length = 3)
	private String currency;

	@Column(name = "txamt", length = 20)
	private String txamt;

	@Column(name = "cifkey", length = 20)
	private String cifkey;

	@Column(name = "batno", length = 20)
	private String batno;

	// @Lob
	// @Basic(fetch = FetchType.LAZY)
	@Lob
	@Column(table = "FX_JOURNAL_TITA", name = "TITA")
	@Basic(fetch = FetchType.LAZY)
	private String tita;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "tita_resv", length = 8500)
	private String titaResv;

	@Column(name = "OVRED")
	private int ovred; // 0:none, 1:var ovr, 2:host ovr

	@Column(name = "RQSP")
	private String rqsp;

	@Column(name = "SEND_TIMES")
	private int sendTimes; // 0:none, 1:var ovr, 2:host ovr

	@Column(name = "TEMP", length = 20)
	private String temp;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public java.sql.Date getJnlDate() {
		return jnlDate;
	}

	public void setJnlDate(java.sql.Date jnlDate) {
		this.jnlDate = jnlDate;
	}

	public java.sql.Time getJnlTime() {
		return jnlTime;
	}

	public void setJnlTime(java.sql.Time jnlTime) {
		this.jnlTime = jnlTime;
	}

	public String getBusdate() {
		return busdate;
	}

	public void setBusdate(String busdate) {
		this.busdate = busdate;
	}

	public int getCalDay() {
		return calDay;
	}

	public void setCalDay(int calDay) {
		this.calDay = calDay;
	}

	public String getBrn() {
		return brn;
	}

	public void setBrn(String brn) {
		this.brn = brn;
	}

	public String getRbrno() {
		return rbrno;
	}

	public void setRbrno(String rbrno) {
		this.rbrno = rbrno;
	}

	public String getFbrno() {
		return fbrno;
	}

	public void setFbrno(String fbrno) {
		this.fbrno = fbrno;
	}

	public String getAcbrno() {
		return acbrno;
	}

	public void setAcbrno(String acbrno) {
		this.acbrno = acbrno;
	}

	public String getPbrno() {
		return pbrno;
	}

	public void setPbrno(String pbrno) {
		this.pbrno = pbrno;
	}

	public String getTlrno() {
		return tlrno;
	}

	public void setTlrno(String tlrno) {
		this.tlrno = tlrno;
	}

	public String getLvel() {
		return lvel;
	}

	public void setLvel(String lvel) {
		this.lvel = lvel;
	}

	public String getTxno() {
		return txno;
	}

	public void setTxno(String txno) {
		this.txno = txno;
	}

	public String getSupno() {
		return supno;
	}

	public void setSupno(String supno) {
		this.supno = supno;
	}

	public String getTxcode() {
		return txcode;
	}

	public void setTxcode(String txcode) {
		this.txcode = txcode;
	}

	public int getTranStatus() {
		return tranStatus;
	}

	public void setTranStatus(int tranStatus) {
		this.tranStatus = tranStatus;
	}

	public String getTranFlag() {
		return tranFlag;
	}

	public void setTranFlag(String tranFlag) {
		this.tranFlag = tranFlag;
	}

	public String getMsgid() {
		return msgid;
	}

	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public String getMrkey() {
		return mrkey;
	}

	public void setMrkey(String mrkey) {
		this.mrkey = mrkey;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getTxamt() {
		return txamt;
	}

	public void setTxamt(String txamt) {
		this.txamt = txamt;
	}

	public String getCifkey() {
		return cifkey;
	}

	public void setCifkey(String cifkey) {
		this.cifkey = cifkey;
	}

	public String getBatno() {
		return batno;
	}

	public void setBatno(String batno) {
		this.batno = batno;
	}

	public String getTita() {
		return tita;
	}

	public void setTita(String tita) {
		this.tita = tita;
	}

	public String getTitaResv() {
		return titaResv;
	}

	public void setTitaResv(String titaResv) {
		this.titaResv = titaResv;
	}

	public String getRqsp() {
		return rqsp;
	}

	public void setRqsp(String rqsp) {
		this.rqsp = rqsp;
	}

	public int getOvred() {
		return ovred;
	}

	public void setOvred(int ovred) {
		this.ovred = ovred;
	}

	public int getSendTimes() {
		return sendTimes;
	}

	public void setSendTimes(int sendTimes) {
		this.sendTimes = sendTimes;
	}

	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	@Override
	public String toString() {
		return "Journal [id=" + id + ", jnlDate=" + jnlDate + ", jnlTime=" + jnlTime + ", busdate=" + busdate + ", calDay=" + calDay + ", brn=" + brn + ", rbrno=" + rbrno + ", fbrno=" + fbrno
				+ ", acbrno=" + acbrno + ", pbrno=" + pbrno + ", tlrno=" + tlrno + ", lvel=" + lvel + ", txno=" + txno + ", supno=" + supno + ", txcode=" + txcode + ", tranStatus=" + tranStatus
				+ ", tranFlag=" + tranFlag + ", msgid=" + msgid + ", errmsg=" + errmsg + ", mrkey=" + mrkey + ", currency=" + currency + ", txamt=" + txamt + ", cifkey=" + cifkey + ", batno=" + batno
				+ ", tita=" + tita + ", titaResv=" + titaResv + ", ovred=" + ovred + ", rqsp=" + rqsp + ", sendTimes=" + sendTimes + ", temp=" + temp + "]";
	}

}
