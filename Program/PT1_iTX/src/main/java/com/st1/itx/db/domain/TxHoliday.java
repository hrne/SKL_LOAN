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
 * TxHoliday 假日檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`TxHoliday`")
public class TxHoliday implements Serializable {


  @EmbeddedId
  private TxHolidayId txHolidayId;

  // 地區別
  @Column(name = "`Country`", length = 2, insertable = false, updatable = false)
  private String country;

  // 日期
  @Column(name = "`Holiday`", insertable = false, updatable = false)
  private int holiday = 0;

  // 假日型態
  /* 1一般例假日 2.特殊狀況假日 */
  @Column(name = "`TypeCode`")
  private int typeCode = 0;

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


  public TxHolidayId getTxHolidayId() {
    return this.txHolidayId;
  }

  public void setTxHolidayId(TxHolidayId txHolidayId) {
    this.txHolidayId = txHolidayId;
  }

/**
	* 地區別<br>
	* 
	* @return String
	*/
  public String getCountry() {
    return this.country == null ? "" : this.country;
  }

/**
	* 地區別<br>
	* 
  *
  * @param country 地區別
	*/
  public void setCountry(String country) {
    this.country = country;
  }

/**
	* 日期<br>
	* 
	* @return Integer
	*/
  public int getHoliday() {
    return StaticTool.bcToRoc(this.holiday);
  }

/**
	* 日期<br>
	* 
  *
  * @param holiday 日期
  * @throws LogicException when Date Is Warn	*/
  public void setHoliday(int holiday) throws LogicException {
    this.holiday = StaticTool.rocToBc(holiday);
  }

/**
	* 假日型態<br>
	* 1一般例假日 2.特殊狀況假日
	* @return Integer
	*/
  public int getTypeCode() {
    return this.typeCode;
  }

/**
	* 假日型態<br>
	* 1一般例假日 2.特殊狀況假日
  *
  * @param typeCode 假日型態
	*/
  public void setTypeCode(int typeCode) {
    this.typeCode = typeCode;
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
    return "TxHoliday [txHolidayId=" + txHolidayId + ", typeCode=" + typeCode + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
           + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
