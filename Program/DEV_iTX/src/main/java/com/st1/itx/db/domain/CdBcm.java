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
 * CdBcm 分公司資料檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CdBcm`")
public class CdBcm implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -4540562971346089334L;

// 單位代號
  /* 10H200:放款審查課10H400:放款服務課10H600:放款管理課10H900:放款推展課10HC00:北部區域中心10HJ00:中部區域中心10HL00:南部區域中心 */
  @Id
  @Column(name = "`UnitCode`", length = 6)
  private String unitCode = " ";

  // 單位名稱
  @Column(name = "`UnitItem`", length = 20)
  private String unitItem;

  // 部室代號
  /* 104000:財務部105000:總務部106000:壽險資訊部107000:研修部108000:保戶關係部109000:團體意外險部10H000:放款部110000:會計部111000:投資企劃部 */
  @Column(name = "`DeptCode`", length = 6)
  private String deptCode;

  // 部室名稱
  @Column(name = "`DeptItem`", length = 20)
  private String deptItem;

  // 區部代號
  /* Dist:District區部代號對照檔? */
  @Column(name = "`DistCode`", length = 6)
  private String distCode;

  // 區部名稱
  @Column(name = "`DistItem`", length = 20)
  private String distItem;

  // 單位經理代號
  @Column(name = "`UnitManager`", length = 6)
  private String unitManager;

  // 部室經理代號
  @Column(name = "`DeptManager`", length = 6)
  private String deptManager;

  // 區部經理代號
  @Column(name = "`DistManager`", length = 6)
  private String distManager;

  // 部室名稱簡寫
  @Column(name = "`ShortDeptItem`", length = 6)
  private String shortDeptItem;

  // 區部名稱簡寫
  @Column(name = "`ShortDistItem`", length = 6)
  private String shortDistItem;

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
	* 單位代號<br>
	* 10H200:放款審查課
10H400:放款服務課
10H600:放款管理課
10H900:放款推展課
10HC00:北部區域中心
10HJ00:中部區域中心
10HL00:南部區域中心
	* @return String
	*/
  public String getUnitCode() {
    return this.unitCode == null ? "" : this.unitCode;
  }

/**
	* 單位代號<br>
	* 10H200:放款審查課
10H400:放款服務課
10H600:放款管理課
10H900:放款推展課
10HC00:北部區域中心
10HJ00:中部區域中心
10HL00:南部區域中心
  *
  * @param unitCode 單位代號
	*/
  public void setUnitCode(String unitCode) {
    this.unitCode = unitCode;
  }

/**
	* 單位名稱<br>
	* 
	* @return String
	*/
  public String getUnitItem() {
    return this.unitItem == null ? "" : this.unitItem;
  }

/**
	* 單位名稱<br>
	* 
  *
  * @param unitItem 單位名稱
	*/
  public void setUnitItem(String unitItem) {
    this.unitItem = unitItem;
  }

/**
	* 部室代號<br>
	* 104000:財務部
105000:總務部
106000:壽險資訊部
107000:研修部
108000:保戶關係部
109000:團體意外險部
10H000:放款部
110000:會計部
111000:投資企劃部
	* @return String
	*/
  public String getDeptCode() {
    return this.deptCode == null ? "" : this.deptCode;
  }

/**
	* 部室代號<br>
	* 104000:財務部
105000:總務部
106000:壽險資訊部
107000:研修部
108000:保戶關係部
109000:團體意外險部
10H000:放款部
110000:會計部
111000:投資企劃部
  *
  * @param deptCode 部室代號
	*/
  public void setDeptCode(String deptCode) {
    this.deptCode = deptCode;
  }

/**
	* 部室名稱<br>
	* 
	* @return String
	*/
  public String getDeptItem() {
    return this.deptItem == null ? "" : this.deptItem;
  }

/**
	* 部室名稱<br>
	* 
  *
  * @param deptItem 部室名稱
	*/
  public void setDeptItem(String deptItem) {
    this.deptItem = deptItem;
  }

/**
	* 區部代號<br>
	* Dist:District
區部代號對照檔?
	* @return String
	*/
  public String getDistCode() {
    return this.distCode == null ? "" : this.distCode;
  }

/**
	* 區部代號<br>
	* Dist:District
區部代號對照檔?
  *
  * @param distCode 區部代號
	*/
  public void setDistCode(String distCode) {
    this.distCode = distCode;
  }

/**
	* 區部名稱<br>
	* 
	* @return String
	*/
  public String getDistItem() {
    return this.distItem == null ? "" : this.distItem;
  }

/**
	* 區部名稱<br>
	* 
  *
  * @param distItem 區部名稱
	*/
  public void setDistItem(String distItem) {
    this.distItem = distItem;
  }

/**
	* 單位經理代號<br>
	* 
	* @return String
	*/
  public String getUnitManager() {
    return this.unitManager == null ? "" : this.unitManager;
  }

/**
	* 單位經理代號<br>
	* 
  *
  * @param unitManager 單位經理代號
	*/
  public void setUnitManager(String unitManager) {
    this.unitManager = unitManager;
  }

/**
	* 部室經理代號<br>
	* 
	* @return String
	*/
  public String getDeptManager() {
    return this.deptManager == null ? "" : this.deptManager;
  }

/**
	* 部室經理代號<br>
	* 
  *
  * @param deptManager 部室經理代號
	*/
  public void setDeptManager(String deptManager) {
    this.deptManager = deptManager;
  }

/**
	* 區部經理代號<br>
	* 
	* @return String
	*/
  public String getDistManager() {
    return this.distManager == null ? "" : this.distManager;
  }

/**
	* 區部經理代號<br>
	* 
  *
  * @param distManager 區部經理代號
	*/
  public void setDistManager(String distManager) {
    this.distManager = distManager;
  }

/**
	* 部室名稱簡寫<br>
	* 
	* @return String
	*/
  public String getShortDeptItem() {
    return this.shortDeptItem == null ? "" : this.shortDeptItem;
  }

/**
	* 部室名稱簡寫<br>
	* 
  *
  * @param shortDeptItem 部室名稱簡寫
	*/
  public void setShortDeptItem(String shortDeptItem) {
    this.shortDeptItem = shortDeptItem;
  }

/**
	* 區部名稱簡寫<br>
	* 
	* @return String
	*/
  public String getShortDistItem() {
    return this.shortDistItem == null ? "" : this.shortDistItem;
  }

/**
	* 區部名稱簡寫<br>
	* 
  *
  * @param shortDistItem 區部名稱簡寫
	*/
  public void setShortDistItem(String shortDistItem) {
    this.shortDistItem = shortDistItem;
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
    return "CdBcm [unitCode=" + unitCode + ", unitItem=" + unitItem + ", deptCode=" + deptCode + ", deptItem=" + deptItem + ", distCode=" + distCode + ", distItem=" + distItem
           + ", unitManager=" + unitManager + ", deptManager=" + deptManager + ", distManager=" + distManager + ", shortDeptItem=" + shortDeptItem + ", shortDistItem=" + shortDistItem + ", createDate=" + createDate
           + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
