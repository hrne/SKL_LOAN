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
 * DailyTav 每日暫收款餘額檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`DailyTav`")
public class DailyTav implements Serializable {


  @EmbeddedId
  private DailyTavId dailyTavId;

  // 會計日期
  @Column(name = "`AcDate`", insertable = false, updatable = false)
  private int acDate = 0;

  // 借款人戶號
  @Column(name = "`CustNo`", insertable = false, updatable = false)
  private int custNo = 0;

  // 額度編號
  @Column(name = "`FacmNo`", insertable = false, updatable = false)
  private int facmNo = 0;

  // 額度自用記號
  /* Y:其額度於銷帳檔紀錄的暫收款餘額限定該額度使用N:其額度於銷帳檔紀錄的暫收款餘額可供同戶所有額度使用 */
  @Column(name = "`SelfUseFlag`", length = 1)
  private String selfUseFlag;

  // 暫收款餘額
  @Column(name = "`TavBal`")
  private BigDecimal tavBal = new BigDecimal("0");

  // 最新記號
  /* Y:此筆為該戶號+額度的最新一筆資料N:此筆為歷史資料 */
  @Column(name = "`LatestFlag`", length = 1)
  private String latestFlag;

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


  public DailyTavId getDailyTavId() {
    return this.dailyTavId;
  }

  public void setDailyTavId(DailyTavId dailyTavId) {
    this.dailyTavId = dailyTavId;
  }

/**
	* 會計日期<br>
	* 
	* @return Integer
	*/
  public int getAcDate() {
    return this.acDate;
  }

/**
	* 會計日期<br>
	* 
  *
  * @param acDate 會計日期
	*/
  public void setAcDate(int acDate) {
    this.acDate = acDate;
  }

/**
	* 借款人戶號<br>
	* 
	* @return Integer
	*/
  public int getCustNo() {
    return this.custNo;
  }

/**
	* 借款人戶號<br>
	* 
  *
  * @param custNo 借款人戶號
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
	* 額度自用記號<br>
	* Y:其額度於銷帳檔紀錄的暫收款餘額限定該額度使用
N:其額度於銷帳檔紀錄的暫收款餘額可供同戶所有額度使用
	* @return String
	*/
  public String getSelfUseFlag() {
    return this.selfUseFlag == null ? "" : this.selfUseFlag;
  }

/**
	* 額度自用記號<br>
	* Y:其額度於銷帳檔紀錄的暫收款餘額限定該額度使用
N:其額度於銷帳檔紀錄的暫收款餘額可供同戶所有額度使用
  *
  * @param selfUseFlag 額度自用記號
	*/
  public void setSelfUseFlag(String selfUseFlag) {
    this.selfUseFlag = selfUseFlag;
  }

/**
	* 暫收款餘額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getTavBal() {
    return this.tavBal;
  }

/**
	* 暫收款餘額<br>
	* 
  *
  * @param tavBal 暫收款餘額
	*/
  public void setTavBal(BigDecimal tavBal) {
    this.tavBal = tavBal;
  }

/**
	* 最新記號<br>
	* Y:此筆為該戶號+額度的最新一筆資料
N:此筆為歷史資料
	* @return String
	*/
  public String getLatestFlag() {
    return this.latestFlag == null ? "" : this.latestFlag;
  }

/**
	* 最新記號<br>
	* Y:此筆為該戶號+額度的最新一筆資料
N:此筆為歷史資料
  *
  * @param latestFlag 最新記號
	*/
  public void setLatestFlag(String latestFlag) {
    this.latestFlag = latestFlag;
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
    return "DailyTav [dailyTavId=" + dailyTavId + ", selfUseFlag=" + selfUseFlag + ", tavBal=" + tavBal + ", latestFlag=" + latestFlag
           + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
