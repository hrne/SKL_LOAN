package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * TxAmlNotice AML定審通知紀錄檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class TxAmlNoticeId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7539626935227166494L;

// 定審日期
	@Column(name = "`DataDt`")
	private int dataDt = 0;

	// 身分證字號
	@Column(name = "`CustKey`", length = 10)
	private String custKey = " ";

	// 通知序號
	@Column(name = "`ProcessSno`")
	private int processSno = 0;

	public TxAmlNoticeId() {
	}

	public TxAmlNoticeId(int dataDt, String custKey, int processSno) {
		this.dataDt = dataDt;
		this.custKey = custKey;
		this.processSno = processSno;
	}

	/**
	 * 定審日期<br>
	 * 
	 * @return Integer
	 */
	public int getDataDt() {
		return this.dataDt;
	}

	/**
	 * 定審日期<br>
	 * 
	 *
	 * @param dataDt 定審日期
	 */
	public void setDataDt(int dataDt) {
		this.dataDt = dataDt;
	}

	/**
	 * 身分證字號<br>
	 * 
	 * @return String
	 */
	public String getCustKey() {
		return this.custKey == null ? "" : this.custKey;
	}

	/**
	 * 身分證字號<br>
	 * 
	 *
	 * @param custKey 身分證字號
	 */
	public void setCustKey(String custKey) {
		this.custKey = custKey;
	}

	/**
	 * 通知序號<br>
	 * 
	 * @return Integer
	 */
	public int getProcessSno() {
		return this.processSno;
	}

	/**
	 * 通知序號<br>
	 * 
	 *
	 * @param processSno 通知序號
	 */
	public void setProcessSno(int processSno) {
		this.processSno = processSno;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dataDt, custKey, processSno);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		TxAmlNoticeId txAmlNoticeId = (TxAmlNoticeId) obj;
		return dataDt == txAmlNoticeId.dataDt && custKey.equals(txAmlNoticeId.custKey) && processSno == txAmlNoticeId.processSno;
	}

	@Override
	public String toString() {
		return "TxAmlNoticeId [dataDt=" + dataDt + ", custKey=" + custKey + ", processSno=" + processSno + "]";
	}
}
