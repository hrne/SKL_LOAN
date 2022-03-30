package com.st1.ifx.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.st1.util.cbl.Cobol;
import com.st1.util.cbl.FieldList;

@FieldList({ "txcd", "sday", "type", "sbtyp", "secno", "mtxcd", "txdnm", "txdsc", "txdfg", "hcode", "pass", "brset",
		"grbrfgOccurs", "tlrfg", "stats" })
@Entity
@Table(name = "FX_TXCD")
public class Txcd implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6836299116365464596L;

	public Txcd() {
	}

	public void initOccurs() {
		// grbrfgOccurs = new TxcdGrbrfg[10];
		for (int i = 0; i < grbrfgOccurs.length; i++) {
			grbrfgOccurs[i] = new TxcdGrbrfg();
		}
	}

	public void occurs2List() {
		for (int i = 0; i < grbrfgOccurs.length; i++) {
			grbrfgOccurs[i].setFxlvl(i + 1);
			this.addGrbrfgList(grbrfgOccurs[i]);
		}

	}

	@Id
	@Column(name = "TXCD")
	@Cobol("X,5")
	private String txcd;

	@Column(name = "SDAY")
	@Cobol("9,8")
	private String sday;

	@Column(name = "TYPE")
	@Cobol("9,1")
	private int type;

	@Column(name = "SBTYP")
	@Cobol("X,5")
	private String sbtyp;

	@Column(name = "SECNO")
	@Cobol("9,2")
	private int secno;

	@Column(name = "MTXCD")
	@Cobol("X,5")
	private String mtxcd;

	@Column(name = "TXDNM")
	@Cobol("X,40")
	private String txdnm;

	@Column(name = "TXDSC")
	@Cobol("X,80")
	private String txdsc;

	@Column(name = "TXDFG")
	@Cobol("9,1")
	private int txdfg;

	@Column(name = "HCODE")
	@Cobol("9,1")
	private int hcode;

	@Column(name = "PASS")
	@Cobol("9,1")
	private int pass;

	@Column(name = "BRSET")
	@Cobol("9,1")
	private int brset;

	// map to TXCD-GRBRFG for occurs 10 times
	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@OrderBy("fxlvl ASC")
	private List<TxcdGrbrfg> grbrfgList = new ArrayList<TxcdGrbrfg>();

	@Transient
	@Cobol("O,10")
	private TxcdGrbrfg[] grbrfgOccurs = new TxcdGrbrfg[10];

	public void setGrbrfgOccurs(TxcdGrbrfg[] grbrfgOccurs) {
		this.grbrfgOccurs = grbrfgOccurs;
	}

	@Column(name = "TLRFG")
	@Cobol("9,10")
	private String tlrfg; // char(10) for occurs 10 times

	@Column(name = "STATS")
	@Cobol("9,1")
	private int stats;

	public String getTxcd() {
		return txcd;
	}

	public void setTxcd(String txcd) {
		this.txcd = txcd;
	}

	public String getSday() {
		return sday;
	}

	public void setSday(String sday) {
		this.sday = sday;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getSbtyp() {
		return sbtyp;
	}

	public void setSbtyp(String sbtyp) {
		this.sbtyp = sbtyp;
	}

	public String getMtxcd() {
		return mtxcd;
	}

	public void setMtxcd(String mtxcd) {
		this.mtxcd = mtxcd;
	}

	public String getTxdnm() {
		return txdnm;
	}

	public void setTxdnm(String txdnm) {
		this.txdnm = txdnm;
	}

	public String getTxdsc() {
		return txdsc;
	}

	public void setTxdsc(String txdsc) {
		this.txdsc = txdsc;
	}

	public int getTxdfg() {
		return txdfg;
	}

	public void setTxdfg(int txdfg) {
		this.txdfg = txdfg;
	}

	public int getHcode() {
		return hcode;
	}

	public void setHcode(int hcode) {
		this.hcode = hcode;
	}

	public int getPass() {
		return pass;
	}

	public void setPass(int pass) {
		this.pass = pass;
	}

	public int getBrset() {
		return brset;
	}

	public void setBrset(int brset) {
		this.brset = brset;
	}

	public List<TxcdGrbrfg> getGrbrfgList() {
		return grbrfgList;
	}

	public TxcdGrbrfg[] getGrbrfgOccurs() {
		return grbrfgOccurs;
	}

	public void addGrbrfgList(TxcdGrbrfg grbrfg) {
		grbrfgList.add(grbrfg);
		grbrfg.setParent(this);
	}

	public String getTlrfg() {
		return tlrfg;
	}

	public int getTlrfgAtLevel(int index) {
		return Integer.parseInt(tlrfg.substring(index - 1, index));
	}

	public void setTlrfg(String tlrfg) {
		this.tlrfg = tlrfg;
	}

	public int getStats() {
		return stats;
	}

	public void setStats(int stats) {
		this.stats = stats;
	}

	public int getSecno() {
		return secno;
	}

	public void setSecno(int secno) {
		this.secno = secno;
	}

	@Override
	public String toString() {
		return "Txcd [txcd=" + txcd + ", sday=" + sday + ", type=" + type + ", sbtyp=" + sbtyp + ", secno=" + secno
				+ ", mtxcd=" + mtxcd + ", txdnm=" + txdnm + ", txdsc=" + txdsc + ", txdfg=" + txdfg + ", hcode=" + hcode
				+ ", pass=" + pass + ", brset=" + brset + ", grbrfgList=" + grbrfgList + ", grbrfgOccurs="
				+ Arrays.toString(grbrfgOccurs) + ", tlrfg=" + tlrfg + ", stats=" + stats + "]";
	}

}
