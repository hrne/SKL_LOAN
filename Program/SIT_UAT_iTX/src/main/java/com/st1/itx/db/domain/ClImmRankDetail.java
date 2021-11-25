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
 * ClImmRankDetail 擔保品不動產檔設定順位明細檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`ClImmRankDetail`")
public class ClImmRankDetail implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -8315107508158589467L;

@EmbeddedId
  private ClImmRankDetailId clImmRankDetailId;

  // 擔保品代號1
  @Column(name = "`ClCode1`", insertable = false, updatable = false)
  private int clCode1 = 0;

  // 擔保品代號2
  @Column(name = "`ClCode2`", insertable = false, updatable = false)
  private int clCode2 = 0;

  // 擔保品編號
  @Column(name = "`ClNo`", insertable = false, updatable = false)
  private int clNo = 0;

  // 設定順位(1~9)
  @Column(name = "`SettingSeq`", length = 1, insertable = false, updatable = false)
  private String settingSeq;

  // 前一順位債權人
  @Column(name = "`FirstCreditor`", length = 40)
  private String firstCreditor;

  // 前一順位金額
  @Column(name = "`FirstAmt`")
  private BigDecimal firstAmt = new BigDecimal("0");

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


  public ClImmRankDetailId getClImmRankDetailId() {
    return this.clImmRankDetailId;
  }

  public void setClImmRankDetailId(ClImmRankDetailId clImmRankDetailId) {
    this.clImmRankDetailId = clImmRankDetailId;
  }

/**
	* 擔保品代號1<br>
	* 
	* @return Integer
	*/
  public int getClCode1() {
    return this.clCode1;
  }

/**
	* 擔保品代號1<br>
	* 
  *
  * @param clCode1 擔保品代號1
	*/
  public void setClCode1(int clCode1) {
    this.clCode1 = clCode1;
  }

/**
	* 擔保品代號2<br>
	* 
	* @return Integer
	*/
  public int getClCode2() {
    return this.clCode2;
  }

/**
	* 擔保品代號2<br>
	* 
  *
  * @param clCode2 擔保品代號2
	*/
  public void setClCode2(int clCode2) {
    this.clCode2 = clCode2;
  }

/**
	* 擔保品編號<br>
	* 
	* @return Integer
	*/
  public int getClNo() {
    return this.clNo;
  }

/**
	* 擔保品編號<br>
	* 
  *
  * @param clNo 擔保品編號
	*/
  public void setClNo(int clNo) {
    this.clNo = clNo;
  }

/**
	* 設定順位(1~9)<br>
	* 
	* @return String
	*/
  public String getSettingSeq() {
    return this.settingSeq == null ? "" : this.settingSeq;
  }

/**
	* 設定順位(1~9)<br>
	* 
  *
  * @param settingSeq 設定順位(1~9)
	*/
  public void setSettingSeq(String settingSeq) {
    this.settingSeq = settingSeq;
  }

/**
	* 前一順位債權人<br>
	* 
	* @return String
	*/
  public String getFirstCreditor() {
    return this.firstCreditor == null ? "" : this.firstCreditor;
  }

/**
	* 前一順位債權人<br>
	* 
  *
  * @param firstCreditor 前一順位債權人
	*/
  public void setFirstCreditor(String firstCreditor) {
    this.firstCreditor = firstCreditor;
  }

/**
	* 前一順位金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getFirstAmt() {
    return this.firstAmt;
  }

/**
	* 前一順位金額<br>
	* 
  *
  * @param firstAmt 前一順位金額
	*/
  public void setFirstAmt(BigDecimal firstAmt) {
    this.firstAmt = firstAmt;
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
    return "ClImmRankDetail [clImmRankDetailId=" + clImmRankDetailId + ", firstCreditor=" + firstCreditor + ", firstAmt=" + firstAmt
           + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
