package com.st1.itx.db.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

/**
 * PfItDetailAdjust 介紹人業績調整檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`PfItDetailAdjust`")
public class PfItDetailAdjust implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -6747003899740854071L;

// 序號
  @Id
  @Column(name = "`LogNo`")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "`PfItDetailAdjust_SEQ`")
  @SequenceGenerator(name = "`PfItDetailAdjust_SEQ`", sequenceName = "`PfItDetailAdjust_SEQ`", allocationSize = 1)
  private Long logNo = 0L;

  // 戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 額度編號
  @Column(name = "`FacmNo`")
  private int facmNo = 0;

  // 工作月
  @Column(name = "`WorkMonth`")
  private int workMonth = 0;

  // 工作季
  @Column(name = "`WorkSeason`")
  private int workSeason = 0;

  // 調整記號
  /* 0:無調整1:調整本月   2:調整本月及季累計 */
  @Column(name = "`AdjRange`")
  private int adjRange = 0;

  // 調整後換算業績
  @Column(name = "`AdjPerfEqAmt`")
  private BigDecimal adjPerfEqAmt = new BigDecimal("0");

  // 業調整後務報酬
  @Column(name = "`AdjPerfReward`")
  private BigDecimal adjPerfReward = new BigDecimal("0");

  // 調整後業績金額
  @Column(name = "`AdjPerfAmt`")
  private BigDecimal adjPerfAmt = new BigDecimal("0");

  // 調整後是否計件
  /* Y/N */
  @Column(name = "`AdjCntingCode`", length = 1)
  private String adjCntingCode;

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


/**
	* 序號<br>
	* 
	* @return Long
	*/
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long getLogNo() {
    return this.logNo;
  }

/**
	* 序號<br>
	* 
  *
  * @param logNo 序號
	*/
  public void setLogNo(Long logNo) {
    this.logNo = logNo;
  }

/**
	* 戶號<br>
	* 
	* @return Integer
	*/
  public int getCustNo() {
    return this.custNo;
  }

/**
	* 戶號<br>
	* 
  *
  * @param custNo 戶號
	*/
  public void setCustNo(int custNo) {
    this.custNo = custNo;
  }

/**
	* 額度編號<br>
	* 
	* @return Integer
	*/
  public int getFacmNo() {
    return this.facmNo;
  }

/**
	* 額度編號<br>
	* 
  *
  * @param facmNo 額度編號
	*/
  public void setFacmNo(int facmNo) {
    this.facmNo = facmNo;
  }

/**
	* 工作月<br>
	* 
	* @return Integer
	*/
  public int getWorkMonth() {
    return this.workMonth;
  }

/**
	* 工作月<br>
	* 
  *
  * @param workMonth 工作月
	*/
  public void setWorkMonth(int workMonth) {
    this.workMonth = workMonth;
  }

/**
	* 工作季<br>
	* 
	* @return Integer
	*/
  public int getWorkSeason() {
    return this.workSeason;
  }

/**
	* 工作季<br>
	* 
  *
  * @param workSeason 工作季
	*/
  public void setWorkSeason(int workSeason) {
    this.workSeason = workSeason;
  }

/**
	* 調整記號<br>
	* 0:無調整
1:調整本月   
2:調整本月及季累計
	* @return Integer
	*/
  public int getAdjRange() {
    return this.adjRange;
  }

/**
	* 調整記號<br>
	* 0:無調整
1:調整本月   
2:調整本月及季累計
  *
  * @param adjRange 調整記號
	*/
  public void setAdjRange(int adjRange) {
    this.adjRange = adjRange;
  }

/**
	* 調整後換算業績<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getAdjPerfEqAmt() {
    return this.adjPerfEqAmt;
  }

/**
	* 調整後換算業績<br>
	* 
  *
  * @param adjPerfEqAmt 調整後換算業績
	*/
  public void setAdjPerfEqAmt(BigDecimal adjPerfEqAmt) {
    this.adjPerfEqAmt = adjPerfEqAmt;
  }

/**
	* 業調整後務報酬<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getAdjPerfReward() {
    return this.adjPerfReward;
  }

/**
	* 業調整後務報酬<br>
	* 
  *
  * @param adjPerfReward 業調整後務報酬
	*/
  public void setAdjPerfReward(BigDecimal adjPerfReward) {
    this.adjPerfReward = adjPerfReward;
  }

/**
	* 調整後業績金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getAdjPerfAmt() {
    return this.adjPerfAmt;
  }

/**
	* 調整後業績金額<br>
	* 
  *
  * @param adjPerfAmt 調整後業績金額
	*/
  public void setAdjPerfAmt(BigDecimal adjPerfAmt) {
    this.adjPerfAmt = adjPerfAmt;
  }

/**
	* 調整後是否計件<br>
	* Y/N
	* @return String
	*/
  public String getAdjCntingCode() {
    return this.adjCntingCode == null ? "" : this.adjCntingCode;
  }

/**
	* 調整後是否計件<br>
	* Y/N
  *
  * @param adjCntingCode 調整後是否計件
	*/
  public void setAdjCntingCode(String adjCntingCode) {
    this.adjCntingCode = adjCntingCode;
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
    return "PfItDetailAdjust [logNo=" + logNo + ", custNo=" + custNo + ", facmNo=" + facmNo + ", workMonth=" + workMonth + ", workSeason=" + workSeason + ", adjRange=" + adjRange
           + ", adjPerfEqAmt=" + adjPerfEqAmt + ", adjPerfReward=" + adjPerfReward + ", adjPerfAmt=" + adjPerfAmt + ", adjCntingCode=" + adjCntingCode + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
           + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
