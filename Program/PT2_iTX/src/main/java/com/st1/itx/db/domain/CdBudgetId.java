package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * CdBudget 利息收入預算檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class CdBudgetId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5630553949560361002L;

// 預算年度
	@Column(name = "`Year`")
	private int year = 0;

	// 預算月份
	/* 12個月 */
	@Column(name = "`Month`")
	private int month = 0;

	public CdBudgetId() {
	}

	public CdBudgetId(int year, int month) {
		this.year = year;
		this.month = month;
	}

	/**
	 * 預算年度<br>
	 * 
	 * @return Integer
	 */
	public int getYear() {
		return this.year;
	}

	/**
	 * 預算年度<br>
	 * 
	 *
	 * @param year 預算年度
	 */
	public void setYear(int year) {
		this.year = year;
	}

	/**
	 * 預算月份<br>
	 * 12個月
	 * 
	 * @return Integer
	 */
	public int getMonth() {
		return this.month;
	}

	/**
	 * 預算月份<br>
	 * 12個月
	 *
	 * @param month 預算月份
	 */
	public void setMonth(int month) {
		this.month = month;
	}

	@Override
	public int hashCode() {
		return Objects.hash(year, month);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		CdBudgetId cdBudgetId = (CdBudgetId) obj;
		return year == cdBudgetId.year && month == cdBudgetId.month;
	}

	@Override
	public String toString() {
		return "CdBudgetId [year=" + year + ", month=" + month + "]";
	}
}
