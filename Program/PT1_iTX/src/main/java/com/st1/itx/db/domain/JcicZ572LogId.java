package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * JcicZ572Log 受理更生款項統一收付款項分配表資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicZ572LogId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5090541590641985452L;

// 流水號
	@Column(name = "`Ukey`", length = 32)
	private String ukey = " ";

	// 交易序號
	@Column(name = "`TxSeq`", length = 18)
	private String txSeq = " ";

	public JcicZ572LogId() {
	}

	public JcicZ572LogId(String ukey, String txSeq) {
		this.ukey = ukey;
		this.txSeq = txSeq;
	}

	/**
	 * 流水號<br>
	 * 
	 * @return String
	 */
	public String getUkey() {
		return this.ukey == null ? "" : this.ukey;
	}

	/**
	 * 流水號<br>
	 * 
	 *
	 * @param ukey 流水號
	 */
	public void setUkey(String ukey) {
		this.ukey = ukey;
	}

	/**
	 * 交易序號<br>
	 * 
	 * @return String
	 */
	public String getTxSeq() {
		return this.txSeq == null ? "" : this.txSeq;
	}

	/**
	 * 交易序號<br>
	 * 
	 *
	 * @param txSeq 交易序號
	 */
	public void setTxSeq(String txSeq) {
		this.txSeq = txSeq;
	}

	@Override
	public int hashCode() {
		return Objects.hash(ukey, txSeq);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		JcicZ572LogId jcicZ572LogId = (JcicZ572LogId) obj;
		return ukey.equals(jcicZ572LogId.ukey) && txSeq.equals(jcicZ572LogId.txSeq);
	}

	@Override
	public String toString() {
		return "JcicZ572LogId [ukey=" + ukey + ", txSeq=" + txSeq + "]";
	}
}
