package com.st1.itx.db.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Time;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.EmbeddedId;
import javax.persistence.Column;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * CdRuleCode 管制代碼檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CdRuleCode`")
public class CdRuleCode implements Serializable {


  @EmbeddedId
  private CdRuleCodeId cdRuleCodeId;

  // 規定管制項目代碼
  @Column(name = "`RuleCode`", length = 4, insertable = false, updatable = false)
  private String ruleCode;

  // 規定管制項目中文
  @Column(name = "`RuleCodeItem`", length = 30)
  private String ruleCodeItem;

  // 備註
  @Column(name = "`RmkItem`", length = 30)
  private String rmkItem;

  // 管制生效日
  @Column(name = "`RuleStDate`", insertable = false, updatable = false)
  private int ruleStDate = 0;

  // 管制取消日
  @Column(name = "`RuleEdDate`")
  private int ruleEdDate = 0;

  // 是否啟用
  @Column(name = "`EnableMark`", length = 1)
  private String enableMark;

  // 建檔日期時間
  @CreatedDate
  @Column(name = "`CreateDate`")
  private java.sql.Timestamp createDate;

  // 建檔人員
  @Column(name = "`CreateEmpNo`", length = 6)
  private String createEmpNo;

  // 最後更新日期時間
  @LastModifiedDate
  @Column(name = "`LastUpdate`")
  private java.sql.Timestamp lastUpdate;

  // 最後更新人員
  @Column(name = "`LastUpdateEmpNo`", length = 6)
  private String lastUpdateEmpNo;


  public CdRuleCodeId getCdRuleCodeId() {
    return this.cdRuleCodeId;
  }

  public void setCdRuleCodeId(CdRuleCodeId cdRuleCodeId) {
    this.cdRuleCodeId = cdRuleCodeId;
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
	* 規定管制項目中文<br>
	* 
	* @return String
	*/
  public String getRuleCodeItem() {
    return this.ruleCodeItem == null ? "" : this.ruleCodeItem;
  }

/**
	* 規定管制項目中文<br>
	* 
  *
  * @param ruleCodeItem 規定管制項目中文
	*/
  public void setRuleCodeItem(String ruleCodeItem) {
    this.ruleCodeItem = ruleCodeItem;
  }

/**
	* 備註<br>
	* 
	* @return String
	*/
  public String getRmkItem() {
    return this.rmkItem == null ? "" : this.rmkItem;
  }

/**
	* 備註<br>
	* 
  *
  * @param rmkItem 備註
	*/
  public void setRmkItem(String rmkItem) {
    this.rmkItem = rmkItem;
  }

/**
	* 管制生效日<br>
	* 
	* @return Integer
	*/
  public int getRuleStDate() {
    return StaticTool.bcToRoc(this.ruleStDate);
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

/**
	* 管制取消日<br>
	* 
	* @return Integer
	*/
  public int getRuleEdDate() {
    return StaticTool.bcToRoc(this.ruleEdDate);
  }

/**
	* 管制取消日<br>
	* 
  *
  * @param ruleEdDate 管制取消日
  * @throws LogicException when Date Is Warn	*/
  public void setRuleEdDate(int ruleEdDate) throws LogicException {
    this.ruleEdDate = StaticTool.rocToBc(ruleEdDate);
  }

/**
	* 是否啟用<br>
	* 
	* @return String
	*/
  public String getEnableMark() {
    return this.enableMark == null ? "" : this.enableMark;
  }

/**
	* 是否啟用<br>
	* 
  *
  * @param enableMark 是否啟用
	*/
  public void setEnableMark(String enableMark) {
    this.enableMark = enableMark;
  }

/**
	* 建檔日期時間<br>
	* 
	* @return java.sql.Timestamp
	*/
  public java.sql.Timestamp getCreateDate() {
    return this.createDate;
  }

/**
	* 建檔日期時間<br>
	* 
  *
  * @param createDate 建檔日期時間
	*/
  public void setCreateDate(java.sql.Timestamp createDate) {
    this.createDate = createDate;
  }

/**
	* 建檔人員<br>
	* 
	* @return String
	*/
  public String getCreateEmpNo() {
    return this.createEmpNo == null ? "" : this.createEmpNo;
  }

/**
	* 建檔人員<br>
	* 
  *
  * @param createEmpNo 建檔人員
	*/
  public void setCreateEmpNo(String createEmpNo) {
    this.createEmpNo = createEmpNo;
  }

/**
	* 最後更新日期時間<br>
	* 
	* @return java.sql.Timestamp
	*/
  public java.sql.Timestamp getLastUpdate() {
    return this.lastUpdate;
  }

/**
	* 最後更新日期時間<br>
	* 
  *
  * @param lastUpdate 最後更新日期時間
	*/
  public void setLastUpdate(java.sql.Timestamp lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

/**
	* 最後更新人員<br>
	* 
	* @return String
	*/
  public String getLastUpdateEmpNo() {
    return this.lastUpdateEmpNo == null ? "" : this.lastUpdateEmpNo;
  }

/**
	* 最後更新人員<br>
	* 
  *
  * @param lastUpdateEmpNo 最後更新人員
	*/
  public void setLastUpdateEmpNo(String lastUpdateEmpNo) {
    this.lastUpdateEmpNo = lastUpdateEmpNo;
  }


  @Override
  public String toString() {
    return "CdRuleCode [cdRuleCodeId=" + cdRuleCodeId + ", ruleCodeItem=" + ruleCodeItem + ", rmkItem=" + rmkItem + ", ruleEdDate=" + ruleEdDate + ", enableMark=" + enableMark
           + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
