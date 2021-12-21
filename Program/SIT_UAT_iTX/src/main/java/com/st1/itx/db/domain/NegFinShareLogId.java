package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * NegFinShareLog 債務協商債權分攤檔歷程檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class NegFinShareLogId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7009002504058452616L;

// 債務人戶號
	/* 保貸戶須建立客戶主檔 */
	@Column(name = "`CustNo`")
	private int custNo = 0;

	// 案件序號
	@Column(name = "`CaseSeq`")
	private int caseSeq = 0;

	// 歷程序號
	@Column(name = "`Seq`")
	private int seq = 0;

	// 債權機構
	@Column(name = "`FinCode`", length = 8)
	private String finCode = " ";

	public NegFinShareLogId() {
	}

	public NegFinShareLogId(int custNo, int caseSeq, int seq, String finCode) {
		this.custNo = custNo;
		this.caseSeq = caseSeq;
		this.seq = seq;
		this.finCode = finCode;
	}

	/**
	 * 債務人戶號<br>
	 * 保貸戶須建立客戶主檔
	 * 
	 * @return Integer
	 */
	public int getCustNo() {
		return this.custNo;
	}

	/**
	 * 債務人戶號<br>
	 * 保貸戶須建立客戶主檔
	 *
	 * @param custNo 債務人戶號
	 */
	public void setCustNo(int custNo) {
		this.custNo = custNo;
	}

	/**
	 * 案件序號<br>
	 * 
	 * @return Integer
	 */
	public int getCaseSeq() {
		return this.caseSeq;
	}

	/**
	 * 案件序號<br>
	 * 
	 *
	 * @param caseSeq 案件序號
	 */
	public void setCaseSeq(int caseSeq) {
		this.caseSeq = caseSeq;
	}

	/**
	 * 歷程序號<br>
	 * 
	 * @return Integer
	 */
	public int getSeq() {
		return this.seq;
	}

	/**
	 * 歷程序號<br>
	 * 
	 *
	 * @param seq 歷程序號
	 */
	public void setSeq(int seq) {
		this.seq = seq;
	}

	/**
	 * 債權機構<br>
	 * 
	 * @return String
	 */
	public String getFinCode() {
		return this.finCode == null ? "" : this.finCode;
	}

	/**
	 * 債權機構<br>
	 * 
	 *
	 * @param finCode 債權機構
	 */
	public void setFinCode(String finCode) {
		this.finCode = finCode;
	}

	@Override
	public int hashCode() {
		return Objects.hash(custNo, caseSeq, seq, finCode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		NegFinShareLogId negFinShareLogId = (NegFinShareLogId) obj;
		return custNo == negFinShareLogId.custNo && caseSeq == negFinShareLogId.caseSeq && seq == negFinShareLogId.seq && finCode.equals(negFinShareLogId.finCode);
	}

	@Override
	public String toString() {
		return "NegFinShareLogId [custNo=" + custNo + ", caseSeq=" + caseSeq + ", seq=" + seq + ", finCode=" + finCode + "]";
	}
}
