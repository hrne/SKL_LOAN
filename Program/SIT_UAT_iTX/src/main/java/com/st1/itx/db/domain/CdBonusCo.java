package com.st1.itx.db.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.EmbeddedId;
import javax.persistence.Column;

/**
 * CdBonusCo 協辦獎勵津貼標準設定<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CdBonusCo`")
public class CdBonusCo implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -8727265965673782947L;

@EmbeddedId
  private CdBonusCoId cdBonusCoId;

  // 工作年月
  /* 2021/4/12 修改合併 */
  @Column(name = "`WorkMonth`", insertable = false, updatable = false)
  private int workMonth = 0;

  // 條件記號
  /* 1.篩選條件-計件代碼2.協辦等級 */
  @Column(name = "`ConditionCode`", insertable = false, updatable = false)
  private int conditionCode = 0;

  // 標準條件
  /* 條件記號=1時為計件代碼1位(1、2、A、B)條件記號=2時為協辦等級1位(1:初級、2:中級、3:高級) */
  @Column(name = "`Condition`", length = 5, insertable = false, updatable = false)
  private String condition;

  // 標準金額
  /* 條件記號=1時輸入新貸案件撥貸金額60萬以上 */
  @Column(name = "`ConditionAmt`")
  private BigDecimal conditionAmt = new BigDecimal("0");

  // 獎勵津貼
  /* 條件記號=2時輸入初級   800中級 1,000高級 1,200 */
  @Column(name = "`Bonus`")
  private BigDecimal bonus = new BigDecimal("0");

  // 獎勵津貼-初階授信通過
  /* 條件記號=2時輸入初級   900中級 1,100高級 1,300 */
  @Column(name = "`ClassPassBonus`")
  private BigDecimal classPassBonus = new BigDecimal("0");

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


  public CdBonusCoId getCdBonusCoId() {
    return this.cdBonusCoId;
  }

  public void setCdBonusCoId(CdBonusCoId cdBonusCoId) {
    this.cdBonusCoId = cdBonusCoId;
  }

/**
	* 工作年月<br>
	* 2021/4/12 修改合併
	* @return Integer
	*/
  public int getWorkMonth() {
    return this.workMonth;
  }

/**
	* 工作年月<br>
	* 2021/4/12 修改合併
  *
  * @param workMonth 工作年月
	*/
  public void setWorkMonth(int workMonth) {
    this.workMonth = workMonth;
  }

/**
	* 條件記號<br>
	* 1.篩選條件-計件代碼
2.協辦等級
	* @return Integer
	*/
  public int getConditionCode() {
    return this.conditionCode;
  }

/**
	* 條件記號<br>
	* 1.篩選條件-計件代碼
2.協辦等級
  *
  * @param conditionCode 條件記號
	*/
  public void setConditionCode(int conditionCode) {
    this.conditionCode = conditionCode;
  }

/**
	* 標準條件<br>
	* 條件記號=1時為計件代碼1位(1、2、A、B)
條件記號=2時為協辦等級1位(1:初級、2:中級、3:高級)
	* @return String
	*/
  public String getCondition() {
    return this.condition == null ? "" : this.condition;
  }

/**
	* 標準條件<br>
	* 條件記號=1時為計件代碼1位(1、2、A、B)
條件記號=2時為協辦等級1位(1:初級、2:中級、3:高級)
  *
  * @param condition 標準條件
	*/
  public void setCondition(String condition) {
    this.condition = condition;
  }

/**
	* 標準金額<br>
	* 條件記號=1時輸入新貸案件撥貸金額60萬以上
	* @return BigDecimal
	*/
  public BigDecimal getConditionAmt() {
    return this.conditionAmt;
  }

/**
	* 標準金額<br>
	* 條件記號=1時輸入新貸案件撥貸金額60萬以上
  *
  * @param conditionAmt 標準金額
	*/
  public void setConditionAmt(BigDecimal conditionAmt) {
    this.conditionAmt = conditionAmt;
  }

/**
	* 獎勵津貼<br>
	* 條件記號=2時輸入
初級   800
中級 1,000
高級 1,200
	* @return BigDecimal
	*/
  public BigDecimal getBonus() {
    return this.bonus;
  }

/**
	* 獎勵津貼<br>
	* 條件記號=2時輸入
初級   800
中級 1,000
高級 1,200
  *
  * @param bonus 獎勵津貼
	*/
  public void setBonus(BigDecimal bonus) {
    this.bonus = bonus;
  }

/**
	* 獎勵津貼-初階授信通過<br>
	* 條件記號=2時輸入
初級   900
中級 1,100
高級 1,300
	* @return BigDecimal
	*/
  public BigDecimal getClassPassBonus() {
    return this.classPassBonus;
  }

/**
	* 獎勵津貼-初階授信通過<br>
	* 條件記號=2時輸入
初級   900
中級 1,100
高級 1,300
  *
  * @param classPassBonus 獎勵津貼-初階授信通過
	*/
  public void setClassPassBonus(BigDecimal classPassBonus) {
    this.classPassBonus = classPassBonus;
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
    return "CdBonusCo [cdBonusCoId=" + cdBonusCoId + ", conditionAmt=" + conditionAmt + ", bonus=" + bonus + ", classPassBonus=" + classPassBonus
           + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
