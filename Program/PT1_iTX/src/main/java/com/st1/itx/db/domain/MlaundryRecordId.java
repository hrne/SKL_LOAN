package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * MlaundryRecord 疑似洗錢交易訪談記錄檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class MlaundryRecordId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2605318021689044682L;

	// 訪談日期
	@Column(name = "`RecordDate`")
	private int recordDate = 0;

	// 戶號
	@Column(name = "`CustNo`")
	private int custNo = 0;

	// 額度編號
	@Column(name = "`FacmNo`")
	private int facmNo = 0;

	// 撥款序號
	@Column(name = "`BormNo`")
	private int bormNo = 0;

	public MlaundryRecordId() {
	}

	public MlaundryRecordId(int recordDate, int custNo, int facmNo, int bormNo) {
		this.recordDate = recordDate;
		this.custNo = custNo;
		this.facmNo = facmNo;
		this.bormNo = bormNo;
	}

	/**
	 * 訪談日期<br>
	 * 
	 * @return Integer
	 */
	public int getRecordDate() {
		return StaticTool.bcToRoc(this.recordDate);
	}

	/**
	 * 訪談日期<br>
	 * 
	 *
	 * @param recordDate 訪談日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setRecordDate(int recordDate) throws LogicException {
		this.recordDate = StaticTool.rocToBc(recordDate);
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
		return Objects.hash(recordDate, custNo, facmNo, bormNo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		MlaundryRecordId mlaundryRecordId = (MlaundryRecordId) obj;
		return recordDate == mlaundryRecordId.recordDate && custNo == mlaundryRecordId.custNo && facmNo == mlaundryRecordId.facmNo && bormNo == mlaundryRecordId.bormNo;
	}

	@Override
	public String toString() {
		return "MlaundryRecordId [recordDate=" + recordDate + ", custNo=" + custNo + ", facmNo=" + facmNo + ", bormNo=" + bormNo + "]";
	}
}
