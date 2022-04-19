package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Ias39Loan34Data IAS39放款34號公報資料檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class Ias39Loan34DataId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4047442706364402899L;

// 年月份
	@Column(name = "`DataYM`")
	private int dataYM = 0;

	// 戶號
	@Column(name = "`CustNo`")
	private int custNo = 0;

	// 額度編號
	@Column(name = "`FacmNo`")
	private int facmNo = 0;

	// 核准號碼
	@Column(name = "`ApplNo`")
	private int applNo = 0;

	// 撥款序號
	@Column(name = "`BormNo`")
	private int bormNo = 0;

	public Ias39Loan34DataId() {
	}

	public Ias39Loan34DataId(int dataYM, int custNo, int facmNo, int applNo, int bormNo) {
		this.dataYM = dataYM;
		this.custNo = custNo;
		this.facmNo = facmNo;
		this.applNo = applNo;
		this.bormNo = bormNo;
	}

	/**
	 * 年月份<br>
	 * 
	 * @return Integer
	 */
	public int getDataYM() {
		return this.dataYM;
	}

	/**
	 * 年月份<br>
	 * 
	 *
	 * @param dataYM 年月份
	 */
	public void setDataYM(int dataYM) {
		this.dataYM = dataYM;
	}

	/**
	 * 戶號<br>
	 * 
	 * @return Integer
	 */
	public int getCustNo() {
		return this.custNo;
	}

	/**
	 * 戶號<br>
	 * 
	 *
	 * @param custNo 戶號
	 */
	public void setCustNo(int custNo) {
		this.custNo = custNo;
	}

	/**
	 * 額度編號<br>
	 * 
	 * @return Integer
	 */
	public int getFacmNo() {
		return this.facmNo;
	}

	/**
	 * 額度編號<br>
	 * 
	 *
	 * @param facmNo 額度編號
	 */
	public void setFacmNo(int facmNo) {
		this.facmNo = facmNo;
	}

	/**
	 * 核准號碼<br>
	 * 
	 * @return Integer
	 */
	public int getApplNo() {
		return this.applNo;
	}

	/**
	 * 核准號碼<br>
	 * 
	 *
	 * @param applNo 核准號碼
	 */
	public void setApplNo(int applNo) {
		this.applNo = applNo;
	}

	/**
	 * 撥款序號<br>
	 * 
	 * @return Integer
	 */
	public int getBormNo() {
		return this.bormNo;
	}

	/**
	 * 撥款序號<br>
	 * 
	 *
	 * @param bormNo 撥款序號
	 */
	public void setBormNo(int bormNo) {
		this.bormNo = bormNo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dataYM, custNo, facmNo, applNo, bormNo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Ias39Loan34DataId ias39Loan34DataId = (Ias39Loan34DataId) obj;
		return dataYM == ias39Loan34DataId.dataYM && custNo == ias39Loan34DataId.custNo && facmNo == ias39Loan34DataId.facmNo && applNo == ias39Loan34DataId.applNo
				&& bormNo == ias39Loan34DataId.bormNo;
	}

	@Override
	public String toString() {
		return "Ias39Loan34DataId [dataYM=" + dataYM + ", custNo=" + custNo + ", facmNo=" + facmNo + ", applNo=" + applNo + ", bormNo=" + bormNo + "]";
	}
}
