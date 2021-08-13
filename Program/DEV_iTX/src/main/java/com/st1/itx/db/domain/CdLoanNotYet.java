package com.st1.itx.db.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.Id;
import javax.persistence.Column;

/**
 * CdLoanNotYet 未齊件代碼檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CdLoanNotYet`")
public class CdLoanNotYet implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 3352944788961654085L;

// 未齊件代碼
  @Id
  @Column(name = "`NotYetCode`", length = 2)
  private String notYetCode = " ";

  // 未齊件說明
  @Column(name = "`NotYetItem`", length = 40)
  private String notYetItem;

  // 齊件日期計算日
  @Column(name = "`YetDays`")
  private int yetDays = 0;

  // 啟用記號
  /* Y:啟用 , N:未啟用 */
  @Column(name = "`Enable`", length = 1)
  private String enable;

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
	* 未齊件代碼<br>
	* 
	* @return String
	*/
  public String getNotYetCode() {
    return this.notYetCode == null ? "" : this.notYetCode;
  }

/**
	* 未齊件代碼<br>
	* 
  *
  * @param notYetCode 未齊件代碼
	*/
  public void setNotYetCode(String notYetCode) {
    this.notYetCode = notYetCode;
  }

/**
	* 未齊件說明<br>
	* 
	* @return String
	*/
  public String getNotYetItem() {
    return this.notYetItem == null ? "" : this.notYetItem;
  }

/**
	* 未齊件說明<br>
	* 
  *
  * @param notYetItem 未齊件說明
	*/
  public void setNotYetItem(String notYetItem) {
    this.notYetItem = notYetItem;
  }

/**
	* 齊件日期計算日<br>
	* 
	* @return Integer
	*/
  public int getYetDays() {
    return this.yetDays;
  }

/**
	* 齊件日期計算日<br>
	* 
  *
  * @param yetDays 齊件日期計算日
	*/
  public void setYetDays(int yetDays) {
    this.yetDays = yetDays;
  }

/**
	* 啟用記號<br>
	* Y:啟用 , N:未啟用
	* @return String
	*/
  public String getEnable() {
    return this.enable == null ? "" : this.enable;
  }

/**
	* 啟用記號<br>
	* Y:啟用 , N:未啟用
  *
  * @param enable 啟用記號
	*/
  public void setEnable(String enable) {
    this.enable = enable;
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
    return "CdLoanNotYet [notYetCode=" + notYetCode + ", notYetItem=" + notYetItem + ", yetDays=" + yetDays + ", enable=" + enable + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
           + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
