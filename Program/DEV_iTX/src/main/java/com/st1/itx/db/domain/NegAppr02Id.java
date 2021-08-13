package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * NegAppr02 一般債權撥付資料檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class NegAppr02Id implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1046874556964479329L;

	// 提兌日
	@Column(name = "`BringUpDate`")
	private int bringUpDate = 0;

	// 債權機構代號
	@Column(name = "`FinCode`", length = 8)
	private String finCode = " ";

	// 資料檔交易序號
	@Column(name = "`TxSeq`", length = 10)
	private String txSeq = " ";

	public NegAppr02Id() {
	}

	public NegAppr02Id(int bringUpDate, String finCode, String txSeq) {
		this.bringUpDate = bringUpDate;
		this.finCode = finCode;
		this.txSeq = txSeq;
	}

	/**
	 * 提兌日<br>
	 * 
	 * @return Integer
	 */
	public int getBringUpDate() {
		return StaticTool.bcToRoc(this.bringUpDate);
	}

	/**
	 * 提兌日<br>
	 * 
	 *
	 * @param bringUpDate 提兌日
	 * @throws LogicException when Date Is Warn
	 */
	public void setBringUpDate(int bringUpDate) throws LogicException {
		this.bringUpDate = StaticTool.rocToBc(bringUpDate);
	}

	/**
	 * 債權機構代號<br>
	 * 
	 * @return String
	 */
	public String getFinCode() {
		return this.finCode == null ? "" : this.finCode;
	}

	/**
	 * 債權機構代號<br>
	 * 
	 *
	 * @param finCode 債權機構代號
	 */
	public void setFinCode(String finCode) {
		this.finCode = finCode;
	}

	/**
	 * 資料檔交易序號<br>
	 * 
	 * @return String
	 */
	public String getTxSeq() {
		return this.txSeq == null ? "" : this.txSeq;
	}

	/**
	 * 資料檔交易序號<br>
	 * 
	 *
	 * @param txSeq 資料檔交易序號
	 */
	public void setTxSeq(String txSeq) {
		this.txSeq = txSeq;
	}

	@Override
	public int hashCode() {
		return Objects.hash(bringUpDate, finCode, txSeq);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		NegAppr02Id negAppr02Id = (NegAppr02Id) obj;
		return bringUpDate == negAppr02Id.bringUpDate && finCode.equals(negAppr02Id.finCode) && txSeq.equals(negAppr02Id.txSeq);
	}

	@Override
	public String toString() {
		return "NegAppr02Id [bringUpDate=" + bringUpDate + ", finCode=" + finCode + ", txSeq=" + txSeq + "]";
	}
}
