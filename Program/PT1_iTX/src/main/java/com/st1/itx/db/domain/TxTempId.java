package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.util.StaticTool;

/**
 * TxTemp 交易暫存<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class TxTempId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3336102838378412668L;

// 交易日
	@Column(name = "`Entdy`")
	private int entdy = 0;

	// 分行別
	@Column(name = "`Kinbr`", length = 4)
	private String kinbr = " ";

	// 交易員代號
	@Column(name = "`TlrNo`", length = 6)
	private String tlrNo = " ";

	// 交易序號
	@Column(name = "`TxtNo`", length = 8)
	private String txtNo = " ";

	// 序號
	@Column(name = "`SeqNo`", length = 30)
	private String seqNo = " ";

	public TxTempId() {
	}

	public TxTempId(int entdy, String kinbr, String tlrNo, String txtNo, String seqNo) throws LogicException {
		this.entdy = StaticTool.rocToBc(entdy);
		this.kinbr = kinbr;
		this.tlrNo = tlrNo;
		this.txtNo = txtNo;
		this.seqNo = seqNo;
	}

	/**
	 * 交易日<br>
	 * 
	 * @return Integer
	 */
	public int getEntdy() {
		return StaticTool.bcToRoc(this.entdy);
	}

	/**
	 * 交易日<br>
	 * 
	 *
	 * @param entdy 交易日
	 * @throws LogicException 123
	 */
	public void setEntdy(int entdy) throws LogicException {
		this.entdy = StaticTool.rocToBc(entdy);
	}

	/**
	 * 分行別<br>
	 * 
	 * @return String
	 */
	public String getKinbr() {
		return this.kinbr == null ? "" : this.kinbr;
	}

	/**
	 * 分行別<br>
	 * 
	 *
	 * @param kinbr 分行別
	 */
	public void setKinbr(String kinbr) {
		this.kinbr = kinbr;
	}

	/**
	 * 交易員代號<br>
	 * 
	 * @return String
	 */
	public String getTlrNo() {
		return this.tlrNo == null ? "" : this.tlrNo;
	}

	/**
	 * 交易員代號<br>
	 * 
	 *
	 * @param tlrNo 交易員代號
	 */
	public void setTlrNo(String tlrNo) {
		this.tlrNo = tlrNo;
	}

	/**
	 * 交易序號<br>
	 * 
	 * @return String
	 */
	public String getTxtNo() {
		return this.txtNo == null ? "" : this.txtNo;
	}

	/**
	 * 交易序號<br>
	 * 
	 *
	 * @param txtNo 交易序號
	 */
	public void setTxtNo(String txtNo) {
		this.txtNo = txtNo;
	}

	/**
	 * 序號<br>
	 * 
	 * @return String
	 */
	public String getSeqNo() {
		return this.seqNo == null ? "" : this.seqNo;
	}

	/**
	 * 序號<br>
	 * 
	 *
	 * @param seqNo 序號
	 */
	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(entdy, kinbr, tlrNo, txtNo, seqNo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		TxTempId txTempId = (TxTempId) obj;
		return entdy == txTempId.entdy && kinbr.equals(txTempId.kinbr) && tlrNo.equals(txTempId.tlrNo) && txtNo.equals(txTempId.txtNo) && seqNo.equals(txTempId.seqNo);
	}

	@Override
	public String toString() {
		return "TxTempId [entdy=" + entdy + ", kinbr=" + kinbr + ", tlrNo=" + tlrNo + ", txtNo=" + txtNo + ", seqNo=" + seqNo + "]";
	}
}