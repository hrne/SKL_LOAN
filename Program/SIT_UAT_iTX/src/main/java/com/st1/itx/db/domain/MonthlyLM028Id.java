package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * MonthlyLM028 月報LM028工作檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class MonthlyLM028Id implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6842651577136666558L;

	// 借款人戶號
	@Column(name = "`LMSACN`")
	private int lMSACN = 0;

	// 額度編號
	@Column(name = "`LMSAPN`")
	private int lMSAPN = 0;

	// 撥款序號
	@Column(name = "`LMSASQ`")
	private int lMSASQ = 0;

	public MonthlyLM028Id() {
	}

	public MonthlyLM028Id(int lMSACN, int lMSAPN, int lMSASQ) {
		this.lMSACN = lMSACN;
		this.lMSAPN = lMSAPN;
		this.lMSASQ = lMSASQ;
	}

	/**
	 * 借款人戶號<br>
	 * 
	 * @return Integer
	 */
	public int getLMSACN() {
		return this.lMSACN;
	}

	/**
	 * 借款人戶號<br>
	 * 
	 *
	 * @param lMSACN 借款人戶號
	 */
	public void setLMSACN(int lMSACN) {
		this.lMSACN = lMSACN;
	}

	/**
	 * 額度編號<br>
	 * 
	 * @return Integer
	 */
	public int getLMSAPN() {
		return this.lMSAPN;
	}

	/**
	 * 額度編號<br>
	 * 
	 *
	 * @param lMSAPN 額度編號
	 */
	public void setLMSAPN(int lMSAPN) {
		this.lMSAPN = lMSAPN;
	}

	/**
	 * 撥款序號<br>
	 * 
	 * @return Integer
	 */
	public int getLMSASQ() {
		return this.lMSASQ;
	}

	/**
	 * 撥款序號<br>
	 * 
	 *
	 * @param lMSASQ 撥款序號
	 */
	public void setLMSASQ(int lMSASQ) {
		this.lMSASQ = lMSASQ;
	}

	@Override
	public int hashCode() {
		return Objects.hash(lMSACN, lMSAPN, lMSASQ);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		MonthlyLM028Id monthlyLM028Id = (MonthlyLM028Id) obj;
		return lMSACN == monthlyLM028Id.lMSACN && lMSAPN == monthlyLM028Id.lMSAPN && lMSASQ == monthlyLM028Id.lMSASQ;
	}

	@Override
	public String toString() {
		return "MonthlyLM028Id [lMSACN=" + lMSACN + ", lMSAPN=" + lMSAPN + ", lMSASQ=" + lMSASQ + "]";
	}
}
