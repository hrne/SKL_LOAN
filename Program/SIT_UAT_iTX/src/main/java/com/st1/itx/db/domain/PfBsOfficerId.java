package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * PfBsOfficer 房貸專員業績目標檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class PfBsOfficerId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2257949648954313233L;

// 年月份
	@Column(name = "`WorkMonth`")
	private int workMonth = 0;

	// 員工代號
	@Column(name = "`EmpNo`", length = 6)
	private String empNo = " ";

	public PfBsOfficerId() {
	}

	public PfBsOfficerId(int workMonth, String empNo) {
		this.workMonth = workMonth;
		this.empNo = empNo;
	}

	/**
	 * 年月份<br>
	 * 
	 * @return Integer
	 */
	public int getWorkMonth() {
		return this.workMonth;
	}

	/**
	 * 年月份<br>
	 * 
	 *
	 * @param workMonth 年月份
	 */
	public void setWorkMonth(int workMonth) {
		this.workMonth = workMonth;
	}

	/**
	 * 員工代號<br>
	 * 
	 * @return String
	 */
	public String getEmpNo() {
		return this.empNo == null ? "" : this.empNo;
	}

	/**
	 * 員工代號<br>
	 * 
	 *
	 * @param empNo 員工代號
	 */
	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(workMonth, empNo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		PfBsOfficerId pfBsOfficerId = (PfBsOfficerId) obj;
		return workMonth == pfBsOfficerId.workMonth && empNo.equals(pfBsOfficerId.empNo);
	}

	@Override
	public String toString() {
		return "PfBsOfficerId [workMonth=" + workMonth + ", empNo=" + empNo + "]";
	}
}
