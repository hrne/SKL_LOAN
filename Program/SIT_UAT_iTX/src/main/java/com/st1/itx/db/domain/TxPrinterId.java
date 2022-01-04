package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * TxPrinter 印表機設定檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class TxPrinterId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4308476501928325620L;

// 工作站IP
	@Column(name = "`StanIp`", length = 15)
	private String stanIp = " ";

	// 檔案編號
	@Column(name = "`FileCode`", length = 40)
	private String fileCode = " ";

	public TxPrinterId() {
	}

	public TxPrinterId(String stanIp, String fileCode) {
		this.stanIp = stanIp;
		this.fileCode = fileCode;
	}

	/**
	 * 工作站IP<br>
	 * 
	 * @return String
	 */
	public String getStanIp() {
		return this.stanIp == null ? "" : this.stanIp;
	}

	/**
	 * 工作站IP<br>
	 * 
	 *
	 * @param stanIp 工作站IP
	 */
	public void setStanIp(String stanIp) {
		this.stanIp = stanIp;
	}

	/**
	 * 檔案編號<br>
	 * 
	 * @return String
	 */
	public String getFileCode() {
		return this.fileCode == null ? "" : this.fileCode;
	}

	/**
	 * 檔案編號<br>
	 * 
	 *
	 * @param fileCode 檔案編號
	 */
	public void setFileCode(String fileCode) {
		this.fileCode = fileCode;
	}

	@Override
	public int hashCode() {
		return Objects.hash(stanIp, fileCode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		TxPrinterId txPrinterId = (TxPrinterId) obj;
		return stanIp.equals(txPrinterId.stanIp) && fileCode == txPrinterId.fileCode;
	}

	@Override
	public String toString() {
		return "TxPrinterId [stanIp=" + stanIp + ", fileCode=" + fileCode + "]";
	}
}
