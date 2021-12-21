package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * HlAreaLnYg6Pt 匯出用HlAreaLnYg6Pt<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class HlAreaLnYg6PtId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9157311540917090595L;

// 年月份
	@Column(name = "`WorkYM`", length = 10)
	private String workYM = " ";

	// 單位代號
	@Column(name = "`AreaUnitNo`", length = 6)
	private String areaUnitNo = " ";

	public HlAreaLnYg6PtId() {
	}

	public HlAreaLnYg6PtId(String workYM, String areaUnitNo) {
		this.workYM = workYM;
		this.areaUnitNo = areaUnitNo;
	}

	/**
	 * 年月份<br>
	 * 
	 * @return String
	 */
	public String getWorkYM() {
		return this.workYM == null ? "" : this.workYM;
	}

	/**
	 * 年月份<br>
	 * 
	 *
	 * @param workYM 年月份
	 */
	public void setWorkYM(String workYM) {
		this.workYM = workYM;
	}

	/**
	 * 單位代號<br>
	 * 
	 * @return String
	 */
	public String getAreaUnitNo() {
		return this.areaUnitNo == null ? "" : this.areaUnitNo;
	}

	/**
	 * 單位代號<br>
	 * 
	 *
	 * @param areaUnitNo 單位代號
	 */
	public void setAreaUnitNo(String areaUnitNo) {
		this.areaUnitNo = areaUnitNo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(workYM, areaUnitNo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		HlAreaLnYg6PtId hlAreaLnYg6PtId = (HlAreaLnYg6PtId) obj;
		return workYM.equals(hlAreaLnYg6PtId.workYM) && areaUnitNo.equals(hlAreaLnYg6PtId.areaUnitNo);
	}

	@Override
	public String toString() {
		return "HlAreaLnYg6PtId [workYM=" + workYM + ", areaUnitNo=" + areaUnitNo + "]";
	}
}
