package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * CdRuleCode 管制代碼檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class CdRuleCodeId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -5455032255668131300L;

// 規定管制項目代碼
  @Column(name = "`RuleCode`", length = 4)
  private String ruleCode = " ";

  // 管制生效日
  @Column(name = "`RuleStDate`")
  private int ruleStDate = 0;

  public CdRuleCodeId() {
  }

  public CdRuleCodeId(String ruleCode, int ruleStDate) {
    this.ruleCode = ruleCode;
    this.ruleStDate = ruleStDate;
  }

/**
	* 規定管制項目代碼<br>
	* 
	* @return String
	*/
  public String getRuleCode() {
    return this.ruleCode == null ? "" : this.ruleCode;
  }

/**
	* 規定管制項目代碼<br>
	* 
  *
  * @param ruleCode 規定管制項目代碼
	*/
  public void setRuleCode(String ruleCode) {
    this.ruleCode = ruleCode;
  }

/**
	* 管制生效日<br>
	* 
	* @return Integer
	*/
  public int getRuleStDate() {
    return  StaticTool.bcToRoc(this.ruleStDate);
  }

/**
	* 管制生效日<br>
	* 
  *
  * @param ruleStDate 管制生效日
  * @throws LogicException when Date Is Warn	*/
  public void setRuleStDate(int ruleStDate) throws LogicException {
    this.ruleStDate = StaticTool.rocToBc(ruleStDate);
  }


  @Override
  public int hashCode() {
    return Objects.hash(ruleCode, ruleStDate);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    CdRuleCodeId cdRuleCodeId = (CdRuleCodeId) obj;
    return ruleCode.equals(cdRuleCodeId.ruleCode) && ruleStDate == cdRuleCodeId.ruleStDate;
  }

  @Override
  public String toString() {
    return "CdRuleCodeId [ruleCode=" + ruleCode + ", ruleStDate=" + ruleStDate + "]";
  }
}
