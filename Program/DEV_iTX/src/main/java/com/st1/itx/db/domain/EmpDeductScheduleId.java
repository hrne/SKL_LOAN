package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * EmpDeductSchedule 員工扣薪日程表<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class EmpDeductScheduleId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -44594186861355502L;

// 工作年月
	/* 業績年月 */
	@Column(name = "`WorkMonth`")
	private int workMonth = 0;

	// 流程別
	/*
	 * 同CdEmp.AgType1制度別(3、4:15日薪)；流程別對應之扣薪種類，建立於CdCode(EmpDeductType)，1-15日薪:12個月，2
	 * -非15日薪:13個工作月
	 */
	@Column(name = "`AgType1`", length = 1)
	private String agType1 = " ";

	public EmpDeductScheduleId() {
	}

	public EmpDeductScheduleId(int workMonth, String agType1) {
		this.workMonth = workMonth;
		this.agType1 = agType1;
	}

	/**
	 * 工作年月<br>
	 * 業績年月
	 * 
	 * @return Integer
	 */
	public int getWorkMonth() {
		return this.workMonth;
	}

	/**
	 * 工作年月<br>
	 * 業績年月
	 *
	 * @param workMonth 工作年月
	 */
	public void setWorkMonth(int workMonth) {
		this.workMonth = workMonth;
	}

	/**
	 * 流程別<br>
	 * 同CdEmp.AgType1制度別(3、4:15日薪)； 流程別對應之扣薪種類，建立於CdCode(EmpDeductType)，
	 * 1-15日薪:12個月，2-非15日薪:13個工作月
	 * 
	 * @return String
	 */
	public String getAgType1() {
		return this.agType1 == null ? "" : this.agType1;
	}

	/**
	 * 流程別<br>
	 * 同CdEmp.AgType1制度別(3、4:15日薪)； 流程別對應之扣薪種類，建立於CdCode(EmpDeductType)，
	 * 1-15日薪:12個月，2-非15日薪:13個工作月
	 *
	 * @param agType1 流程別
	 */
	public void setAgType1(String agType1) {
		this.agType1 = agType1;
	}

	@Override
	public int hashCode() {
		return Objects.hash(workMonth, agType1);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		EmpDeductScheduleId empDeductScheduleId = (EmpDeductScheduleId) obj;
		return workMonth == empDeductScheduleId.workMonth && agType1.equals(empDeductScheduleId.agType1);
	}

	@Override
	public String toString() {
		return "EmpDeductScheduleId [workMonth=" + workMonth + ", agType1=" + agType1 + "]";
	}
}
