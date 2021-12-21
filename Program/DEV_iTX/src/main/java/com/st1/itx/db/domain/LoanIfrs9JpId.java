package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * LoanIfrs9Jp IFRS9欄位清單10<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class LoanIfrs9JpId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6354417294214358609L;

// 年月份
	@Column(name = "`DataYM`")
	private int dataYM = 0;

	// 發生時會計日期年月
	@Column(name = "`AcDateYM`")
	private int acDateYM = 0;

	// 戶號
	@Column(name = "`CustNo`")
	private int custNo = 0;

	// 新額度編號
	@Column(name = "`NewFacmNo`")
	private int newFacmNo = 0;

	// 新撥款序號
	@Column(name = "`NewBormNo`")
	private int newBormNo = 0;

	// 舊額度編號
	@Column(name = "`OldFacmNo`")
	private int oldFacmNo = 0;

	// 舊撥款序號
	@Column(name = "`OldBormNo`")
	private int oldBormNo = 0;

	public LoanIfrs9JpId() {
	}

	public LoanIfrs9JpId(int dataYM, int acDateYM, int custNo, int newFacmNo, int newBormNo, int oldFacmNo, int oldBormNo) {
		this.dataYM = dataYM;
		this.acDateYM = acDateYM;
		this.custNo = custNo;
		this.newFacmNo = newFacmNo;
		this.newBormNo = newBormNo;
		this.oldFacmNo = oldFacmNo;
		this.oldBormNo = oldBormNo;
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
	 * 發生時會計日期年月<br>
	 * 
	 * @return Integer
	 */
	public int getAcDateYM() {
		return this.acDateYM;
	}

	/**
	 * 發生時會計日期年月<br>
	 * 
	 *
	 * @param acDateYM 發生時會計日期年月
	 */
	public void setAcDateYM(int acDateYM) {
		this.acDateYM = acDateYM;
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
	 * 新額度編號<br>
	 * 
	 * @return Integer
	 */
	public int getNewFacmNo() {
		return this.newFacmNo;
	}

	/**
	 * 新額度編號<br>
	 * 
	 *
	 * @param newFacmNo 新額度編號
	 */
	public void setNewFacmNo(int newFacmNo) {
		this.newFacmNo = newFacmNo;
	}

	/**
	 * 新撥款序號<br>
	 * 
	 * @return Integer
	 */
	public int getNewBormNo() {
		return this.newBormNo;
	}

	/**
	 * 新撥款序號<br>
	 * 
	 *
	 * @param newBormNo 新撥款序號
	 */
	public void setNewBormNo(int newBormNo) {
		this.newBormNo = newBormNo;
	}

	/**
	 * 舊額度編號<br>
	 * 
	 * @return Integer
	 */
	public int getOldFacmNo() {
		return this.oldFacmNo;
	}

	/**
	 * 舊額度編號<br>
	 * 
	 *
	 * @param oldFacmNo 舊額度編號
	 */
	public void setOldFacmNo(int oldFacmNo) {
		this.oldFacmNo = oldFacmNo;
	}

	/**
	 * 舊撥款序號<br>
	 * 
	 * @return Integer
	 */
	public int getOldBormNo() {
		return this.oldBormNo;
	}

	/**
	 * 舊撥款序號<br>
	 * 
	 *
	 * @param oldBormNo 舊撥款序號
	 */
	public void setOldBormNo(int oldBormNo) {
		this.oldBormNo = oldBormNo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dataYM, acDateYM, custNo, newFacmNo, newBormNo, oldFacmNo, oldBormNo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		LoanIfrs9JpId loanIfrs9JpId = (LoanIfrs9JpId) obj;
		return dataYM == loanIfrs9JpId.dataYM && acDateYM == loanIfrs9JpId.acDateYM && custNo == loanIfrs9JpId.custNo && newFacmNo == loanIfrs9JpId.newFacmNo && newBormNo == loanIfrs9JpId.newBormNo
				&& oldFacmNo == loanIfrs9JpId.oldFacmNo && oldBormNo == loanIfrs9JpId.oldBormNo;
	}

	@Override
	public String toString() {
		return "LoanIfrs9JpId [dataYM=" + dataYM + ", acDateYM=" + acDateYM + ", custNo=" + custNo + ", newFacmNo=" + newFacmNo + ", newBormNo=" + newBormNo + ", oldFacmNo=" + oldFacmNo
				+ ", oldBormNo=" + oldBormNo + "]";
	}
}
