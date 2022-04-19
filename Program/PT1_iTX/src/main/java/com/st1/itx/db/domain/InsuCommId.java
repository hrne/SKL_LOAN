package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * InsuComm 火險佣金檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class InsuCommId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1922062829970285455L;

// 年月份
	@Column(name = "`InsuYearMonth`")
	private int insuYearMonth = 0;

	// 佣金媒體檔序號
	/* 寫入檔之排序 */
	@Column(name = "`InsuCommSeq`")
	private int insuCommSeq = 0;

	public InsuCommId() {
	}

	public InsuCommId(int insuYearMonth, int insuCommSeq) {
		this.insuYearMonth = insuYearMonth;
		this.insuCommSeq = insuCommSeq;
	}

	/**
	 * 年月份<br>
	 * 
	 * @return Integer
	 */
	public int getInsuYearMonth() {
		return this.insuYearMonth;
	}

	/**
	 * 年月份<br>
	 * 
	 *
	 * @param insuYearMonth 年月份
	 */
	public void setInsuYearMonth(int insuYearMonth) {
		this.insuYearMonth = insuYearMonth;
	}

	/**
	 * 佣金媒體檔序號<br>
	 * 寫入檔之排序
	 * 
	 * @return Integer
	 */
	public int getInsuCommSeq() {
		return this.insuCommSeq;
	}

	/**
	 * 佣金媒體檔序號<br>
	 * 寫入檔之排序
	 *
	 * @param insuCommSeq 佣金媒體檔序號
	 */
	public void setInsuCommSeq(int insuCommSeq) {
		this.insuCommSeq = insuCommSeq;
	}

	@Override
	public int hashCode() {
		return Objects.hash(insuYearMonth, insuCommSeq);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		InsuCommId insuCommId = (InsuCommId) obj;
		return insuYearMonth == insuCommId.insuYearMonth && insuCommSeq == insuCommId.insuCommSeq;
	}

	@Override
	public String toString() {
		return "InsuCommId [insuYearMonth=" + insuYearMonth + ", insuCommSeq=" + insuCommSeq + "]";
	}
}
