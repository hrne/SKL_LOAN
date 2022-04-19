package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * DailyLoanBal 每日放款餘額檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class DailyLoanBalId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 286193218584083007L;

// 資料日期
	/* 1.&amp;lt;餘額、利率、業務科目&amp;gt;變動日2.月底日 */
	@Column(name = "`DataDate`")
	private int dataDate = 0;

	// 戶號
	@Column(name = "`CustNo`")
	private int custNo = 0;

	// 額度編號
	@Column(name = "`FacmNo`")
	private int facmNo = 0;

	// 撥款序號
	@Column(name = "`BormNo`")
	private int bormNo = 0;

	public DailyLoanBalId() {
	}

	public DailyLoanBalId(int dataDate, int custNo, int facmNo, int bormNo) {
		this.dataDate = dataDate;
		this.custNo = custNo;
		this.facmNo = facmNo;
		this.bormNo = bormNo;
	}

	/**
	 * 資料日期<br>
	 * 1.&amp;lt;餘額、利率、業務科目&amp;gt;變動日 2.月底日
	 * 
	 * @return Integer
	 */
	public int getDataDate() {
		return StaticTool.bcToRoc(this.dataDate);
	}

	/**
	 * 資料日期<br>
	 * 1.&amp;lt;餘額、利率、業務科目&amp;gt;變動日 2.月底日
	 *
	 * @param dataDate 資料日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setDataDate(int dataDate) throws LogicException {
		this.dataDate = StaticTool.rocToBc(dataDate);
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

	@Override
	public int hashCode() {
		return Objects.hash(dataDate, custNo, facmNo, bormNo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		DailyLoanBalId dailyLoanBalId = (DailyLoanBalId) obj;
		return dataDate == dailyLoanBalId.dataDate && custNo == dailyLoanBalId.custNo && facmNo == dailyLoanBalId.facmNo && bormNo == dailyLoanBalId.bormNo;
	}

	@Override
	public String toString() {
		return "DailyLoanBalId [dataDate=" + dataDate + ", custNo=" + custNo + ", facmNo=" + facmNo + ", bormNo=" + bormNo + "]";
	}
}
