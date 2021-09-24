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
 * CdBranchGroup 營業單位課組別檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CdBranchGroup`")
public class CdBranchGroup implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -6091258869828365240L;

@EmbeddedId
  private CdBranchGroupId cdBranchGroupId;

  // 單位別
  @Column(name = "`BranchNo`", length = 4, insertable = false, updatable = false)
  private String branchNo;

  // 課組別代號
  @Column(name = "`GroupNo`", insertable = false, updatable = false)
  private int groupNo = 0;

  // 課組別說明
  @Column(name = "`GroupItem`", length = 10)
  private String groupItem;

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


  public CdBranchGroupId getCdBranchGroupId() {
    return this.cdBranchGroupId;
  }

  public void setCdBranchGroupId(CdBranchGroupId cdBranchGroupId) {
    this.cdBranchGroupId = cdBranchGroupId;
  }

/**
	* 單位別<br>
	* 
	* @return String
	*/
  public String getBranchNo() {
    return this.branchNo == null ? "" : this.branchNo;
  }

/**
	* 單位別<br>
	* 
  *
  * @param branchNo 單位別
	*/
  public void setBranchNo(String branchNo) {
    this.branchNo = branchNo;
  }

/**
	* 課組別代號<br>
	* 
	* @return Integer
	*/
  public int getGroupNo() {
    return this.groupNo;
  }

/**
	* 課組別代號<br>
	* 
  *
  * @param groupNo 課組別代號
	*/
  public void setGroupNo(int groupNo) {
    this.groupNo = groupNo;
  }

/**
	* 課組別說明<br>
	* 
	* @return String
	*/
  public String getGroupItem() {
    return this.groupItem == null ? "" : this.groupItem;
  }

/**
	* 課組別說明<br>
	* 
  *
  * @param groupItem 課組別說明
	*/
  public void setGroupItem(String groupItem) {
    this.groupItem = groupItem;
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
    return "CdBranchGroup [cdBranchGroupId=" + cdBranchGroupId + ", groupItem=" + groupItem + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
           + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
