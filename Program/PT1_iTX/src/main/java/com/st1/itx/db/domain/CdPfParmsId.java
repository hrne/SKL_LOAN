package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * CdPfParms 業績特殊參數設定檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class CdPfParmsId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6334864616061210762L;

// 條件記號1
	/* 1.排除商品別2.排除部門別3.是否排除15日薪非業績人員4.業績追回時通知員工代碼(email)R.業績重算設定 */
	@Column(name = "`ConditionCode1`", length = 1)
	private String conditionCode1 = " ";

	// 條件記號2
	/* 條件記號1為1、2、3時1.業績全部2.換算業績、業務報酬3.介紹獎金4.加碼獎勵津貼5.協辦獎金其他為空白1位 */
	@Column(name = "`ConditionCode2`", length = 1)
	private String conditionCode2 = " ";

	// 標準條件
	/* 條件記號1=1時為商品別代號條件記號1=2時為部門別代號條件記號1=3、R時為為空白1位條件記號1=4時為員工代碼 */
	@Column(name = "`Condition`", length = 6)
	private String condition = " ";

	public CdPfParmsId() {
	}

	public CdPfParmsId(String conditionCode1, String conditionCode2, String condition) {
		this.conditionCode1 = conditionCode1;
		this.conditionCode2 = conditionCode2;
		this.condition = condition;
	}

	/**
	 * 條件記號1<br>
	 * 1.排除商品別 2.排除部門別 3.是否排除15日薪非業績人員 4.業績追回時通知員工代碼(email) R.業績重算設定
	 * 
	 * @return String
	 */
	public String getConditionCode1() {
		return this.conditionCode1 == null ? "" : this.conditionCode1;
	}

	/**
	 * 條件記號1<br>
	 * 1.排除商品別 2.排除部門別 3.是否排除15日薪非業績人員 4.業績追回時通知員工代碼(email) R.業績重算設定
	 *
	 * @param conditionCode1 條件記號1
	 */
	public void setConditionCode1(String conditionCode1) {
		this.conditionCode1 = conditionCode1;
	}

	/**
	 * 條件記號2<br>
	 * 條件記號1為1、2、3時 1.業績全部 2.換算業績、業務報酬 3.介紹獎金 4.加碼獎勵津貼 5.協辦獎金 其他為空白1位
	 * 
	 * @return String
	 */
	public String getConditionCode2() {
		return this.conditionCode2 == null ? "" : this.conditionCode2;
	}

	/**
	 * 條件記號2<br>
	 * 條件記號1為1、2、3時 1.業績全部 2.換算業績、業務報酬 3.介紹獎金 4.加碼獎勵津貼 5.協辦獎金 其他為空白1位
	 *
	 * @param conditionCode2 條件記號2
	 */
	public void setConditionCode2(String conditionCode2) {
		this.conditionCode2 = conditionCode2;
	}

	/**
	 * 標準條件<br>
	 * 條件記號1=1時為商品別代號 條件記號1=2時為部門別代號 條件記號1=3、R時為為空白1位 條件記號1=4時為員工代碼
	 * 
	 * @return String
	 */
	public String getCondition() {
		return this.condition == null ? "" : this.condition;
	}

	/**
	 * 標準條件<br>
	 * 條件記號1=1時為商品別代號 條件記號1=2時為部門別代號 條件記號1=3、R時為為空白1位 條件記號1=4時為員工代碼
	 *
	 * @param condition 標準條件
	 */
	public void setCondition(String condition) {
		this.condition = condition;
	}

	@Override
	public int hashCode() {
		return Objects.hash(conditionCode1, conditionCode2, condition);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		CdPfParmsId cdPfParmsId = (CdPfParmsId) obj;
		return conditionCode1.equals(cdPfParmsId.conditionCode1) && conditionCode2.equals(cdPfParmsId.conditionCode2) && condition.equals(cdPfParmsId.condition);
	}

	@Override
	public String toString() {
		return "CdPfParmsId [conditionCode1=" + conditionCode1 + ", conditionCode2=" + conditionCode2 + ", condition=" + condition + "]";
	}
}
