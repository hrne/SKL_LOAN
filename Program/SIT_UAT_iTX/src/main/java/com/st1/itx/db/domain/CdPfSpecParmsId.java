package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * CdPfSpecParms 業績計算特殊參數設定檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class CdPfSpecParmsId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1072262979599872143L;

// 條件記號
	/* 1:計算介紹人業績時排除商品別2:計算介紹人業績時排徐部門別 */
	@Column(name = "`ConditionCode`", length = 1)
	private String conditionCode = " ";

	// 標準條件
	/* 條件記號=1，寫入商品別代號條件記號=2，寫入部門別代號 */
	@Column(name = "`Condition`", length = 6)
	private String condition = " ";

	public CdPfSpecParmsId() {
	}

	public CdPfSpecParmsId(String conditionCode, String condition) {
		this.conditionCode = conditionCode;
		this.condition = condition;
	}

	/**
	 * 條件記號<br>
	 * 1:計算介紹人業績時排除商品別 2:計算介紹人業績時排徐部門別
	 * 
	 * @return String
	 */
	public String getConditionCode() {
		return this.conditionCode == null ? "" : this.conditionCode;
	}

	/**
	 * 條件記號<br>
	 * 1:計算介紹人業績時排除商品別 2:計算介紹人業績時排徐部門別
	 *
	 * @param conditionCode 條件記號
	 */
	public void setConditionCode(String conditionCode) {
		this.conditionCode = conditionCode;
	}

	/**
	 * 標準條件<br>
	 * 條件記號=1，寫入商品別代號 條件記號=2，寫入部門別代號
	 * 
	 * @return String
	 */
	public String getCondition() {
		return this.condition == null ? "" : this.condition;
	}

	/**
	 * 標準條件<br>
	 * 條件記號=1，寫入商品別代號 條件記號=2，寫入部門別代號
	 *
	 * @param condition 標準條件
	 */
	public void setCondition(String condition) {
		this.condition = condition;
	}

	@Override
	public int hashCode() {
		return Objects.hash(conditionCode, condition);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		CdPfSpecParmsId cdPfSpecParmsId = (CdPfSpecParmsId) obj;
		return conditionCode.equals(cdPfSpecParmsId.conditionCode) && condition.equals(cdPfSpecParmsId.condition);
	}

	@Override
	public String toString() {
		return "CdPfSpecParmsId [conditionCode=" + conditionCode + ", condition=" + condition + "]";
	}
}
