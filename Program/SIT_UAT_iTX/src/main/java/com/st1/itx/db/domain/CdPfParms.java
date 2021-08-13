package com.st1.itx.db.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.EmbeddedId;
import javax.persistence.Column;

/**
 * CdPfParms 業績特殊參數設定檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CdPfParms`")
public class CdPfParms implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 6989635912493812682L;

@EmbeddedId
  private CdPfParmsId cdPfParmsId;

  // 條件記號1
  /* 1.排除商品別2.排除部門別3.是否排除15日薪非業績人員4.業績追回時通知員工代碼(email) */
  @Column(name = "`ConditionCode1`", length = 1, insertable = false, updatable = false)
  private String conditionCode1;

  // 條件記號2
  /* 條件記號1為1、2、3時1.業績全部2.換算業績、業務報酬3.介紹獎金4.加碼獎勵津貼5.協辦獎金 */
  @Column(name = "`ConditionCode2`", length = 1, insertable = false, updatable = false)
  private String conditionCode2;

  // 標準條件
  /* 條件記號1=1時為商品別代號條件記號1=2時為部門別代號條件記號1=3時為為空白1位條件記號1=4時為員工代碼 */
  @Column(name = "`Condition`", length = 6, insertable = false, updatable = false)
  private String condition;

  // 有效工作月(起)
  /* 不輸入為0(顯示空白) */
  @Column(name = "`WorkMonthStart`")
  private int workMonthStart = 0;

  // 有效工作月(止)
  /* 需大於有效工作月(起)，或為0(顯示空白) */
  @Column(name = "`WorkMonthEnd`")
  private int workMonthEnd = 0;

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


  public CdPfParmsId getCdPfParmsId() {
    return this.cdPfParmsId;
  }

  public void setCdPfParmsId(CdPfParmsId cdPfParmsId) {
    this.cdPfParmsId = cdPfParmsId;
  }

/**
	* 條件記號1<br>
	* 1.排除商品別
2.排除部門別
3.是否排除15日薪非業績人員
4.業績追回時通知員工代碼(email)
	* @return String
	*/
  public String getConditionCode1() {
    return this.conditionCode1 == null ? "" : this.conditionCode1;
  }

/**
	* 條件記號1<br>
	* 1.排除商品別
2.排除部門別
3.是否排除15日薪非業績人員
4.業績追回時通知員工代碼(email)
  *
  * @param conditionCode1 條件記號1
	*/
  public void setConditionCode1(String conditionCode1) {
    this.conditionCode1 = conditionCode1;
  }

/**
	* 條件記號2<br>
	* 條件記號1為1、2、3時
1.業績全部
2.換算業績、業務報酬
3.介紹獎金
4.加碼獎勵津貼
5.協辦獎金
	* @return String
	*/
  public String getConditionCode2() {
    return this.conditionCode2 == null ? "" : this.conditionCode2;
  }

/**
	* 條件記號2<br>
	* 條件記號1為1、2、3時
1.業績全部
2.換算業績、業務報酬
3.介紹獎金
4.加碼獎勵津貼
5.協辦獎金
  *
  * @param conditionCode2 條件記號2
	*/
  public void setConditionCode2(String conditionCode2) {
    this.conditionCode2 = conditionCode2;
  }

/**
	* 標準條件<br>
	* 條件記號1=1時為商品別代號
條件記號1=2時為部門別代號
條件記號1=3時為為空白1位
條件記號1=4時為員工代碼
	* @return String
	*/
  public String getCondition() {
    return this.condition == null ? "" : this.condition;
  }

/**
	* 標準條件<br>
	* 條件記號1=1時為商品別代號
條件記號1=2時為部門別代號
條件記號1=3時為為空白1位
條件記號1=4時為員工代碼
  *
  * @param condition 標準條件
	*/
  public void setCondition(String condition) {
    this.condition = condition;
  }

/**
	* 有效工作月(起)<br>
	* 不輸入為0(顯示空白)
	* @return Integer
	*/
  public int getWorkMonthStart() {
    return this.workMonthStart;
  }

/**
	* 有效工作月(起)<br>
	* 不輸入為0(顯示空白)
  *
  * @param workMonthStart 有效工作月(起)
	*/
  public void setWorkMonthStart(int workMonthStart) {
    this.workMonthStart = workMonthStart;
  }

/**
	* 有效工作月(止)<br>
	* 需大於有效工作月(起)，或為0(顯示空白)
	* @return Integer
	*/
  public int getWorkMonthEnd() {
    return this.workMonthEnd;
  }

/**
	* 有效工作月(止)<br>
	* 需大於有效工作月(起)，或為0(顯示空白)
  *
  * @param workMonthEnd 有效工作月(止)
	*/
  public void setWorkMonthEnd(int workMonthEnd) {
    this.workMonthEnd = workMonthEnd;
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
    return "CdPfParms [cdPfParmsId=" + cdPfParmsId + ", workMonthStart=" + workMonthStart + ", workMonthEnd=" + workMonthEnd + ", createDate=" + createDate
           + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
