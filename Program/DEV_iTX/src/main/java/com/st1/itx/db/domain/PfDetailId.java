package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * PfDetail 業績計算明細檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class PfDetailId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6154773029687559626L;

// 業績日期
	/* 業績日期=系統營業日1.撥貸時，為撥款日期，計件代碼變更，為會計日2.部分償還、提前結案，為會計日3.保費檢核追回，為該工作月的業績止日 */
	@Column(name = "`PerfDate`")
	private int perfDate = 0;

	// 戶號
	@Column(name = "`CustNo`")
	private int custNo = 0;

	// 額度編號
	@Column(name = "`FacmNo`")
	private int facmNo = 0;

	// 撥款序號
	@Column(name = "`BormNo`")
	private int bormNo = 0;

	// 交易內容檔序號
	@Column(name = "`BorxNo`")
	private int borxNo = 0;

	public PfDetailId() {
	}

	public PfDetailId(int perfDate, int custNo, int facmNo, int bormNo, int borxNo) {
		this.perfDate = perfDate;
		this.custNo = custNo;
		this.facmNo = facmNo;
		this.bormNo = bormNo;
		this.borxNo = borxNo;
	}

	/**
	 * 業績日期<br>
	 * 業績日期=系統營業日 1.撥貸時，為撥款日期，計件代碼變更，為會計日 2.部分償還、提前結案，為會計日 3.保費檢核追回，為該工作月的業績止日
	 * 
	 * @return Integer
	 */
	public int getPerfDate() {
		return StaticTool.bcToRoc(this.perfDate);
	}

	/**
	 * 業績日期<br>
	 * 業績日期=系統營業日 1.撥貸時，為撥款日期，計件代碼變更，為會計日 2.部分償還、提前結案，為會計日 3.保費檢核追回，為該工作月的業績止日
	 *
	 * @param perfDate 業績日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setPerfDate(int perfDate) throws LogicException {
		this.perfDate = StaticTool.rocToBc(perfDate);
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

	/**
	 * 交易內容檔序號<br>
	 * 
	 * @return Integer
	 */
	public int getBorxNo() {
		return this.borxNo;
	}

	/**
	 * 交易內容檔序號<br>
	 * 
	 *
	 * @param borxNo 交易內容檔序號
	 */
	public void setBorxNo(int borxNo) {
		this.borxNo = borxNo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(perfDate, custNo, facmNo, bormNo, borxNo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		PfDetailId pfDetailId = (PfDetailId) obj;
		return perfDate == pfDetailId.perfDate && custNo == pfDetailId.custNo && facmNo == pfDetailId.facmNo && bormNo == pfDetailId.bormNo && borxNo == pfDetailId.borxNo;
	}

	@Override
	public String toString() {
		return "PfDetailId [perfDate=" + perfDate + ", custNo=" + custNo + ", facmNo=" + facmNo + ", bormNo=" + bormNo + ", borxNo=" + borxNo + "]";
	}
}
