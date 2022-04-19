package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * JcicZ443Log 前置調解回報有擔保債權金額資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicZ443LogId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3685768957571188659L;

// 流水號
	@Column(name = "`Ukey`", length = 32)
	private String ukey = " ";

	// 交易序號
	@Column(name = "`TxSeq`", length = 18)
	private String txSeq = " ";

	public JcicZ443LogId() {
	}

	public JcicZ443LogId(String ukey, String txSeq) {
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
		JcicZ443LogId jcicZ443LogId = (JcicZ443LogId) obj;
		return ukey.equals(jcicZ443LogId.ukey) && txSeq.equals(jcicZ443LogId.txSeq);
	}

	@Override
	public String toString() {
		return "JcicZ443LogId [ukey=" + ukey + ", txSeq=" + txSeq + "]";
	}
}
