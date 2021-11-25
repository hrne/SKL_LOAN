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
 * ClBuildingReason 擔保品-建物修改原因檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`ClBuildingReason`")
public class ClBuildingReason implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 4017912610422263558L;

@EmbeddedId
  private ClBuildingReasonId clBuildingReasonId;

  // 擔保品-代號1
  @Column(name = "`ClCode1`", insertable = false, updatable = false)
  private int clCode1 = 0;

  // 擔保品-代號2
  @Column(name = "`ClCode2`", insertable = false, updatable = false)
  private int clCode2 = 0;

  // 擔保品編號
  @Column(name = "`ClNo`", insertable = false, updatable = false)
  private int clNo = 0;

  // 修改原因序號
  @Column(name = "`ReasonSeq`", insertable = false, updatable = false)
  private int reasonSeq = 0;

  // 修改原因
  /* 1:補齊舊資料;2:資料錯誤;3:部分塗銷（持分）;4:部分塗銷（車位）;5:政府機關通知;6:其他： */
  @Column(name = "`Reason`")
  private int reason = 0;

  // 其他原因
  @Column(name = "`OtherReason`", length = 60)
  private String otherReason;

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


  public ClBuildingReasonId getClBuildingReasonId() {
    return this.clBuildingReasonId;
  }

  public void setClBuildingReasonId(ClBuildingReasonId clBuildingReasonId) {
    this.clBuildingReasonId = clBuildingReasonId;
  }

/**
	* 擔保品-代號1<br>
	* 
	* @return Integer
	*/
  public int getClCode1() {
    return this.clCode1;
  }

/**
	* 擔保品-代號1<br>
	* 
  *
  * @param clCode1 擔保品-代號1
	*/
  public void setClCode1(int clCode1) {
    this.clCode1 = clCode1;
  }

/**
	* 擔保品-代號2<br>
	* 
	* @return Integer
	*/
  public int getClCode2() {
    return this.clCode2;
  }

/**
	* 擔保品-代號2<br>
	* 
  *
  * @param clCode2 擔保品-代號2
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
	* 修改原因序號<br>
	* 
	* @return Integer
	*/
  public int getReasonSeq() {
    return this.reasonSeq;
  }

/**
	* 修改原因序號<br>
	* 
  *
  * @param reasonSeq 修改原因序號
	*/
  public void setReasonSeq(int reasonSeq) {
    this.reasonSeq = reasonSeq;
  }

/**
	* 修改原因<br>
	* 1:補齊舊資料;2:資料錯誤;3:部分塗銷（持分）;4:部分塗銷（車位）;5:政府機關通知;6:其他：
	* @return Integer
	*/
  public int getReason() {
    return this.reason;
  }

/**
	* 修改原因<br>
	* 1:補齊舊資料;2:資料錯誤;3:部分塗銷（持分）;4:部分塗銷（車位）;5:政府機關通知;6:其他：
  *
  * @param reason 修改原因
	*/
  public void setReason(int reason) {
    this.reason = reason;
  }

/**
	* 其他原因<br>
	* 
	* @return String
	*/
  public String getOtherReason() {
    return this.otherReason == null ? "" : this.otherReason;
  }

/**
	* 其他原因<br>
	* 
  *
  * @param otherReason 其他原因
	*/
  public void setOtherReason(String otherReason) {
    this.otherReason = otherReason;
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
    return "ClBuildingReason [clBuildingReasonId=" + clBuildingReasonId + ", reason=" + reason + ", otherReason=" + otherReason
           + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
