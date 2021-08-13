package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * CdBonus 介紹人加碼獎勵津貼標準設定<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class CdBonusId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 6921235934181693174L;

// 工作年月
  /* 2021/4/12 合併 */
  @Column(name = "`WorkMonth`")
  private int workMonth = 0;

  // 條件記號
  /* 1.篩選條件-計件代碼2.排除條件-商品代號3.金額級距 */
  @Column(name = "`ConditionCode`")
  private int conditionCode = 0;

  // 標準條件
  /* 條件記號=1時為計件代碼1位條件記號=2時為商品代號5位條件記號=3時為金額級距序號2位 */
  @Column(name = "`Condition`", length = 5)
  private String condition = " ";

  public CdBonusId() {
  }

  public CdBonusId(int workMonth, int conditionCode, String condition) {
    this.workMonth = workMonth;
    this.conditionCode = conditionCode;
    this.condition = condition;
  }

/**
	* 工作年月<br>
	* 2021/4/12 合併
	* @return Integer
	*/
  public int getWorkMonth() {
    return this.workMonth;
  }

/**
	* 工作年月<br>
	* 2021/4/12 合併
  *
  * @param workMonth 工作年月
	*/
  public void setWorkMonth(int workMonth) {
    this.workMonth = workMonth;
  }

/**
	* 條件記號<br>
	* 1.篩選條件-計件代碼
2.排除條件-商品代號
3.金額級距
	* @return Integer
	*/
  public int getConditionCode() {
    return this.conditionCode;
  }

/**
	* 條件記號<br>
	* 1.篩選條件-計件代碼
2.排除條件-商品代號
3.金額級距
  *
  * @param conditionCode 條件記號
	*/
  public void setConditionCode(int conditionCode) {
    this.conditionCode = conditionCode;
  }

/**
	* 標準條件<br>
	* 條件記號=1時為計件代碼1位
條件記號=2時為商品代號5位
條件記號=3時為金額級距序號2位
	* @return String
	*/
  public String getCondition() {
    return this.condition == null ? "" : this.condition;
  }

/**
	* 標準條件<br>
	* 條件記號=1時為計件代碼1位
條件記號=2時為商品代號5位
條件記號=3時為金額級距序號2位
  *
  * @param condition 標準條件
	*/
  public void setCondition(String condition) {
    this.condition = condition;
  }


  @Override
  public int hashCode() {
    return Objects.hash(workMonth, conditionCode, condition);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    CdBonusId cdBonusId = (CdBonusId) obj;
    return workMonth == cdBonusId.workMonth && conditionCode == cdBonusId.conditionCode && condition.equals(cdBonusId.condition);
  }

  @Override
  public String toString() {
    return "CdBonusId [workMonth=" + workMonth + ", conditionCode=" + conditionCode + ", condition=" + condition + "]";
  }
}
