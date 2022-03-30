package com.st1.ifx.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.st1.util.cbl.Cobol;
import com.st1.util.cbl.FieldList;

@FieldList({ "brfg", "acbrno", "chop", "ochop" })
@Entity
@Table(name = "FX_TXCD_GRBRFG")
@IdClass(TxcdGrbrfgId.class)
public class TxcdGrbrfg implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8490058889621202814L;

	@Id
	@Column(name = "TXCD")
	private String txcd;

	@Id
	@Column(name = "FXLVL")
	private int fxlvl;

	@Column(name = "BRFG")
	@Cobol("9,1")
	private int brfg;

	@Column(name = "ACBRNO")
	@Cobol("X,4")
	private String acbrno;

	@Column(name = "CHOP")
	@Cobol("9,1")
	private int chop;

	@Column(name = "OCHOP")
	@Cobol("9,1")
	private int ochop;

	@ManyToOne
	@JoinColumn(name = "TXCD", insertable = false, updatable = false)
	private Txcd parent;

	public Txcd getParent() {
		return parent;
	}

	public void setParent(Txcd parent) {
		this.parent = parent;
		this.txcd = parent.getTxcd();
	}

	public String getTxcd() {
		return txcd;
	}

	public void setTxcd(String txcd) {
		this.txcd = txcd;
	}

	public int getFxlvl() {
		return fxlvl;
	}

	public void setFxlvl(int fxlvl) {
		this.fxlvl = fxlvl;
	}

	public int getBrfg() {
		return brfg;
	}

	public void setBrfg(int brfg) {
		this.brfg = brfg;
	}

	public String getAcbrno() {
		return acbrno;
	}

	public void setAcbrno(String acbrno) {
		this.acbrno = acbrno;
	}

	public int getChop() {
		return chop;
	}

	public void setChop(int chop) {
		this.chop = chop;
	}

	public int getOchop() {
		return ochop;
	}

	public void setOchop(int ochop) {
		this.ochop = ochop;
	}

	@Override
	public String toString() {
		return "TxcdGrbrfg [txcd=" + txcd + ", fxlvl=" + fxlvl + ", brfg=" + brfg + ", acbrno=" + acbrno + ", chop="
				+ chop + ", ochop=" + ochop + "]";
	}

}
