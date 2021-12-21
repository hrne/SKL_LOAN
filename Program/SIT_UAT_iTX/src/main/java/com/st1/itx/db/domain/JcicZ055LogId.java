package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * JcicZ055Log 消債條例更生案件資料報送<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicZ055LogId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1680291121319071472L;

// 流水號
	@Column(name = "`Ukey`", length = 32)
	private String ukey = " ";

	// 交易序號
	@Column(name = "`TxSeq`", length = 18)
	private String txSeq = " ";

	public JcicZ055LogId() {
	}

	public JcicZ055LogId(String ukey, String txSeq) {
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
		JcicZ055LogId jcicZ055LogId = (JcicZ055LogId) obj;
		return ukey.equals(jcicZ055LogId.ukey) && txSeq.equals(jcicZ055LogId.txSeq);
	}

	@Override
	public String toString() {
		return "JcicZ055LogId [ukey=" + ukey + ", txSeq=" + txSeq + "]";
	}
}
