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
 * CdBonus 介紹人加碼獎勵津貼標準設定<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CdBonus`")
public class CdBonus implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 7167827442004526953L;

@EmbeddedId
  private CdBonusId cdBonusId;

  // 工作年月
  /* 2021/4/12 合併 */
  @Column(name = "`WorkMonth`", insertable = false, updatable = false)
  private int workMonth = 0;

  // 條件記號
  /* 1.篩選條件-計件代碼2.排除條件-商品代號3.金額級距 */
  @Column(name = "`ConditionCode`", insertable = false, updatable = false)
  private int conditionCode = 0;

  // 標準條件
  /* 條件記號=1時為計件代碼1位條件記號=2時為商品代號5位條件記號=3時為金額級距序號2位 */
  @Column(name = "`Condition`", length = 5, insertable = false, updatable = false)
  private String condition;

  // 新貸案件撥貸金額級距-起
  /* 條件記號=3時必須有值，其他0 */
  @Column(name = "`AmtStartRange`")
  private BigDecimal amtStartRange = new BigDecimal("0");

  // 新貸案件撥貸金額級距-止
  /* 條件記號=3時必須有值，其他0 */
  @Column(name = "`AmtEndRange`")
  private BigDecimal amtEndRange = new BigDecimal("0");

  // 獎勵津貼
  /* 條件記號=3時必須有值，其他0 */
  @Column(name = "`Bonus`")
  private BigDecimal bonus = new BigDecimal("0");

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


  public CdBonusId getCdBonusId() {
    return this.cdBonusId;
  }

  public void setCdBonusId(CdBonusId cdBonusId) {
    this.cdBonusId = cdBonusId;
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

/**
	* 新貸案件撥貸金額級距-起<br>
	* 條件記號=3時必須有值，其他0
	* @return BigDecimal
	*/
  public BigDecimal getAmtStartRange() {
    return this.amtStartRange;
  }

/**
	* 新貸案件撥貸金額級距-起<br>
	* 條件記號=3時必須有值，其他0
  *
  * @param amtStartRange 新貸案件撥貸金額級距-起
	*/
  public void setAmtStartRange(BigDecimal amtStartRange) {
    this.amtStartRange = amtStartRange;
  }

/**
	* 新貸案件撥貸金額級距-止<br>
	* 條件記號=3時必須有值，其他0
	* @return BigDecimal
	*/
  public BigDecimal getAmtEndRange() {
    return this.amtEndRange;
  }

/**
	* 新貸案件撥貸金額級距-止<br>
	* 條件記號=3時必須有值，其他0
  *
  * @param amtEndRange 新貸案件撥貸金額級距-止
	*/
  public void setAmtEndRange(BigDecimal amtEndRange) {
    this.amtEndRange = amtEndRange;
  }

/**
	* 獎勵津貼<br>
	* 條件記號=3時必須有值，其他0
	* @return BigDecimal
	*/
  public BigDecimal getBonus() {
    return this.bonus;
  }

/**
	* 獎勵津貼<br>
	* 條件記號=3時必須有值，其他0
  *
  * @param bonus 獎勵津貼
	*/
  public void setBonus(BigDecimal bonus) {
    this.bonus = bonus;
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
    return "CdBonus [cdBonusId=" + cdBonusId + ", amtStartRange=" + amtStartRange + ", amtEndRange=" + amtEndRange + ", bonus=" + bonus
           + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
