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
 * PfCoOfficer 協辦人員等級檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`PfCoOfficer`")
public class PfCoOfficer implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 1701651541443414728L;

@EmbeddedId
  private PfCoOfficerId pfCoOfficerId;

  // 員工代號
  @Column(name = "`EmpNo`", length = 6, insertable = false, updatable = false)
  private String empNo;

  // 生效日期
  @Column(name = "`EffectiveDate`", insertable = false, updatable = false)
  private int effectiveDate = 0;

  // 停效日期
  /* 3/25新加欄位，舊資料、轉換資料設為2910/12/31 */
  @Column(name = "`IneffectiveDate`")
  private int ineffectiveDate = 0;

  // 單位代號
  /* 4/27新增，紀錄資料新增當下該員工所屬單位代號 */
  @Column(name = "`AreaCode`", length = 6)
  private String areaCode;

  // 區部代號
  /* 4/27新增，紀錄資料新增當下該員工所屬區部代號 */
  @Column(name = "`DistCode`", length = 6)
  private String distCode;

  // 部室代號
  /* 4/27新增，紀錄資料新增當下該員工所屬部室代號 */
  @Column(name = "`DeptCode`", length = 6)
  private String deptCode;

  // 單位中文
  /* 4/27新增，紀錄資料新增當下該員工所屬單位中文 */
  @Column(name = "`AreaItem`", length = 20)
  private String areaItem;

  // 區部中文
  /* 4/27新增，紀錄資料新增當下該員工所屬區部中文 */
  @Column(name = "`DistItem`", length = 20)
  private String distItem;

  // 部室中文
  /* 4/27新增，紀錄資料新增當下該員工所屬部室中文 */
  @Column(name = "`DeptItem`", length = 20)
  private String deptItem;

  // 協辦等級
  /* 1: 初級2: 中級3: 高級 */
  @Column(name = "`EmpClass`", length = 1)
  private String empClass;

  // 初階授信通過
  /* 輸入Y or spacesY: 通過 */
  @Column(name = "`ClassPass`", length = 1)
  private String classPass;

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


  public PfCoOfficerId getPfCoOfficerId() {
    return this.pfCoOfficerId;
  }

  public void setPfCoOfficerId(PfCoOfficerId pfCoOfficerId) {
    this.pfCoOfficerId = pfCoOfficerId;
  }

/**
	* 員工代號<br>
	* 
	* @return String
	*/
  public String getEmpNo() {
    return this.empNo == null ? "" : this.empNo;
  }

/**
	* 員工代號<br>
	* 
  *
  * @param empNo 員工代號
	*/
  public void setEmpNo(String empNo) {
    this.empNo = empNo;
  }

/**
	* 生效日期<br>
	* 
	* @return Integer
	*/
  public int getEffectiveDate() {
    return StaticTool.bcToRoc(this.effectiveDate);
  }

/**
	* 生效日期<br>
	* 
  *
  * @param effectiveDate 生效日期
  * @throws LogicException when Date Is Warn	*/
  public void setEffectiveDate(int effectiveDate) throws LogicException {
    this.effectiveDate = StaticTool.rocToBc(effectiveDate);
  }

/**
	* 停效日期<br>
	* 3/25新加欄位，舊資料、轉換資料設為2910/12/31
	* @return Integer
	*/
  public int getIneffectiveDate() {
    return StaticTool.bcToRoc(this.ineffectiveDate);
  }

/**
	* 停效日期<br>
	* 3/25新加欄位，舊資料、轉換資料設為2910/12/31
  *
  * @param ineffectiveDate 停效日期
  * @throws LogicException when Date Is Warn	*/
  public void setIneffectiveDate(int ineffectiveDate) throws LogicException {
    this.ineffectiveDate = StaticTool.rocToBc(ineffectiveDate);
  }

/**
	* 單位代號<br>
	* 4/27新增，紀錄資料新增當下該員工所屬單位代號
	* @return String
	*/
  public String getAreaCode() {
    return this.areaCode == null ? "" : this.areaCode;
  }

/**
	* 單位代號<br>
	* 4/27新增，紀錄資料新增當下該員工所屬單位代號
  *
  * @param areaCode 單位代號
	*/
  public void setAreaCode(String areaCode) {
    this.areaCode = areaCode;
  }

/**
	* 區部代號<br>
	* 4/27新增，紀錄資料新增當下該員工所屬區部代號
	* @return String
	*/
  public String getDistCode() {
    return this.distCode == null ? "" : this.distCode;
  }

/**
	* 區部代號<br>
	* 4/27新增，紀錄資料新增當下該員工所屬區部代號
  *
  * @param distCode 區部代號
	*/
  public void setDistCode(String distCode) {
    this.distCode = distCode;
  }

/**
	* 部室代號<br>
	* 4/27新增，紀錄資料新增當下該員工所屬部室代號
	* @return String
	*/
  public String getDeptCode() {
    return this.deptCode == null ? "" : this.deptCode;
  }

/**
	* 部室代號<br>
	* 4/27新增，紀錄資料新增當下該員工所屬部室代號
  *
  * @param deptCode 部室代號
	*/
  public void setDeptCode(String deptCode) {
    this.deptCode = deptCode;
  }

/**
	* 單位中文<br>
	* 4/27新增，紀錄資料新增當下該員工所屬單位中文
	* @return String
	*/
  public String getAreaItem() {
    return this.areaItem == null ? "" : this.areaItem;
  }

/**
	* 單位中文<br>
	* 4/27新增，紀錄資料新增當下該員工所屬單位中文
  *
  * @param areaItem 單位中文
	*/
  public void setAreaItem(String areaItem) {
    this.areaItem = areaItem;
  }

/**
	* 區部中文<br>
	* 4/27新增，紀錄資料新增當下該員工所屬區部中文
	* @return String
	*/
  public String getDistItem() {
    return this.distItem == null ? "" : this.distItem;
  }

/**
	* 區部中文<br>
	* 4/27新增，紀錄資料新增當下該員工所屬區部中文
  *
  * @param distItem 區部中文
	*/
  public void setDistItem(String distItem) {
    this.distItem = distItem;
  }

/**
	* 部室中文<br>
	* 4/27新增，紀錄資料新增當下該員工所屬部室中文
	* @return String
	*/
  public String getDeptItem() {
    return this.deptItem == null ? "" : this.deptItem;
  }

/**
	* 部室中文<br>
	* 4/27新增，紀錄資料新增當下該員工所屬部室中文
  *
  * @param deptItem 部室中文
	*/
  public void setDeptItem(String deptItem) {
    this.deptItem = deptItem;
  }

/**
	* 協辦等級<br>
	* 1: 初級
2: 中級
3: 高級
	* @return String
	*/
  public String getEmpClass() {
    return this.empClass == null ? "" : this.empClass;
  }

/**
	* 協辦等級<br>
	* 1: 初級
2: 中級
3: 高級
  *
  * @param empClass 協辦等級
	*/
  public void setEmpClass(String empClass) {
    this.empClass = empClass;
  }

/**
	* 初階授信通過<br>
	* 輸入Y or spaces
Y: 通過
	* @return String
	*/
  public String getClassPass() {
    return this.classPass == null ? "" : this.classPass;
  }

/**
	* 初階授信通過<br>
	* 輸入Y or spaces
Y: 通過
  *
  * @param classPass 初階授信通過
	*/
  public void setClassPass(String classPass) {
    this.classPass = classPass;
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
    return "PfCoOfficer [pfCoOfficerId=" + pfCoOfficerId + ", ineffectiveDate=" + ineffectiveDate + ", areaCode=" + areaCode + ", distCode=" + distCode + ", deptCode=" + deptCode
           + ", areaItem=" + areaItem + ", distItem=" + distItem + ", deptItem=" + deptItem + ", empClass=" + empClass + ", classPass=" + classPass + ", createDate=" + createDate
           + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
