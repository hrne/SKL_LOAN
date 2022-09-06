package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * TxDataLog 資料變更紀錄檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class TxDataLogId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3136794105028268337L;

// 會計日期
	@Column(name = "`TxDate`")
	private int txDate = 0;

	// 交易序號
	@Column(name = "`TxSeq`", length = 18)
	private String txSeq = " ";

	// 明細序號
	@Column(name = "`TxSno`")
	private int txSno = 0;

	public TxDataLogId() {
	}

	public TxDataLogId(int txDate, String txSeq, int txSno) {
		this.txDate = txDate;
		this.txSeq = txSeq;
		this.txSno = txSno;
	}

	/**
	 * 會計日期<br>
	 * 
	 * @return Integer
	 */
	public int getTxDate() {
		return StaticTool.bcToRoc(this.txDate);
	}

	/**
	 * 會計日期<br>
	 * 
	 *
	 * @param txDate 會計日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setTxDate(int txDate) throws LogicException {
		this.txDate = StaticTool.rocToBc(txDate);
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

	/**
	 * 明細序號<br>
	 * 
	 * @return Integer
	 */
	public int getTxSno() {
		return this.txSno;
	}

	/**
	 * 明細序號<br>
	 * 
	 *
	 * @param txSno 明細序號
	 */
	public void setTxSno(int txSno) {
		this.txSno = txSno;
	}

	@Override
	public int hashCode() {
		return Objects.hash(txDate, txSeq, txSno);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		TxDataLogId txDataLogId = (TxDataLogId) obj;
		return txDate == txDataLogId.txDate && txSeq.equals(txDataLogId.txSeq) && txSno == txDataLogId.txSno;
	}

	@Override
	public String toString() {
		return "TxDataLogId [txDate=" + txDate + ", txSeq=" + txSeq + ", txSno=" + txSno + "]";
	}
}
