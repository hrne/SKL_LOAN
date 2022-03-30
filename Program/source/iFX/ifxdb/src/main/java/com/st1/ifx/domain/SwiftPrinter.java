package com.st1.ifx.domain;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "FX_SWIFT_PRINTER")
public class SwiftPrinter implements Serializable {
	private static final long serialVersionUID = 345936755774832160L;

	@Id
	@Column(name = "BRN")
	private String brn;

	@Column(name = "PRIMARY_PRINTER")
	private String primaryPrinter;

	@Column(name = "ACK_PRINTER")
	private String ackPrinter;

	@Column(name = "NAK_PRINTER")
	private String nakPrinter;

	@Column(name = "ALT_PRINTER")
	private String altPrinter;

	@Column(name = "PRI_ALLOWIP")
	private String priAllowip;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "ALT_MSG_LIST")
	private String altMsgList;

	// TODO getPrinterByMsgType
	public String getPrinterByMsgType(String msgType, String msgStatus) {
		// altPrinter Check
		String msgcheck = msgType + "_" + msgStatus;
		if (this.altMsgList.indexOf(msgcheck) != -1) {
			return altPrinter;
		} else if (msgStatus.equals("R")) {
			return primaryPrinter;
		} else if (msgStatus.equals("A")) {
			return ackPrinter;
		} else if (msgStatus.equals("N")) {
			return nakPrinter;
		}
		return null;
	}

	public String getBrn() {
		return brn;
	}

	public void setBrn(String brn) {
		this.brn = brn;
	}

	public String getPrimaryPrinter() {
		return primaryPrinter;
	}

	public void setPrimaryPrinter(String primaryPrinter) {
		this.primaryPrinter = primaryPrinter;
	}

	public String getAckPrinter() {
		return ackPrinter;
	}

	public String getNakPrinter() {
		return nakPrinter;
	}

	public void setAckPrinter(String ackPrinter) {
		this.ackPrinter = ackPrinter;
	}

	public void setNakPrinter(String nakPrinter) {
		this.nakPrinter = nakPrinter;
	}

	public String getAltPrinter() {
		return altPrinter;
	}

	public void setAltPrinter(String altPrinter) {
		this.altPrinter = altPrinter;
	}

	public String getPriAllowip() {
		return priAllowip;
	}

	public void setPriAllowip(String priAllowip) {
		this.priAllowip = priAllowip;
	}

	public String getAltMsgList() {
		return altMsgList;
	}

	public void setAltMsgList(String altMsgList) {
		this.altMsgList = altMsgList;
	}

	@Override
	public String toString() {
		return "SwiftPrinter [brn=" + brn + ", primaryPrinter=" + primaryPrinter + ", altPrinter=" + altPrinter
				+ ", ackPrinter=" + ackPrinter + ", nakPrinter=" + nakPrinter + ", priAllowip=" + priAllowip
				+ ", altMsgList=" + altMsgList + "]";
	}

}
