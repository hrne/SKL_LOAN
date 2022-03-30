package com.st1.ifx.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.st1.util.cbl.Cobol;
import com.st1.util.cbl.FieldList;

@FieldList({ "type", "brno", "tlrno", "sbtyp", "sday", "drelcd", "dabrno", "drbrno", "orelcd", "orbrno", "stats" })
@Entity
@Table(name = "FX_SBCTL")
@IdClass(SbctlId.class)
public class Sbctl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1564952332628307847L;

	// "TYPE" SMALLINT NOT NULL,
	@Id
	@Column(name = "TYPE")
	@Cobol("9,1")
	private int type;

	// "BRNO" CHAR(4) NOT NULL,
	@Id
	@Column(name = "BRNO")
	@Cobol("X,4")
	private String brno;

	// "TLRNO" VARCHAR(20) NOT NULL,
	@Id
	@Column(name = "TLRNO")
	@Cobol("X,20")
	private String tlrno;

	// "SBTYP" CHAR(5) NOT NULL,
	@Id
	@Column(name = "SBTYP")
	@Cobol("X,5")
	private String sbtyp;

	// "SDAY" CHAR(8),
	@Column(name = "SDAY")
	@Cobol("X,8")
	private String sday;

	// "DRELCD" SMALLINT,
	@Column(name = "DRELCD")
	@Cobol("9,1")
	private int drelcd;

	// "DABRNO" CHAR(4),
	@Column(name = "DABRNO")
	@Cobol("X,4")
	private String dabrno;

	// "DRBRNO" CHAR(4),
	@Column(name = "DRBRNO")
	@Cobol("X,4")
	private String drbrno;

	// "ORELCD" SMALLINT,
	@Column(name = "ORELCD")
	@Cobol("9,1")
	private int orelcd;

	// "ORBRNO" CHAR(4),
	@Column(name = "ORBRNO")
	@Cobol("X,4")
	private String orbrno;

	// "STATS" SMALLINT
	@Column(name = "STATS")
	@Cobol("9,1")
	private int stats;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getBrno() {
		return brno;
	}

	public void setBrno(String brno) {
		this.brno = brno;
	}

	public String getTlrno() {
		return tlrno;
	}

	public void setTlrno(String tlrno) {
		this.tlrno = tlrno;
	}

	public String getSbtyp() {
		return sbtyp;
	}

	public void setSbtyp(String sbtyp) {
		this.sbtyp = sbtyp;
	}

	public String getSday() {
		return sday;
	}

	public void setSday(String sday) {
		this.sday = sday;
	}

	public int getDrelcd() {
		return drelcd;
	}

	public void setDrelcd(int drelcd) {
		this.drelcd = drelcd;
	}

	public String getDabrno() {
		return dabrno;
	}

	public void setDabrno(String dabrno) {
		this.dabrno = dabrno;
	}

	public String getDrbrno() {
		return drbrno;
	}

	public void setDrbrno(String drbrno) {
		this.drbrno = drbrno;
	}

	public int getOrelcd() {
		return orelcd;
	}

	public void setOrelcd(int orelcd) {
		this.orelcd = orelcd;
	}

	public String getOrbrno() {
		return orbrno;
	}

	public void setOrbrno(String orbrno) {
		this.orbrno = orbrno;
	}

	public int getStats() {
		return stats;
	}

	public void setStats(int stats) {
		this.stats = stats;
	}

	@Override
	public String toString() {
		return "Sbctl [type=" + type + ", brno=" + brno + ", tlrno=" + tlrno + ", sbtyp=" + sbtyp + ", sday=" + sday
				+ ", drelcd=" + drelcd + ", dabrno=" + dabrno + ", drbrno=" + drbrno + ", orelcd=" + orelcd
				+ ", orbrno=" + orbrno + ", stats=" + stats + "]";
	}

}
