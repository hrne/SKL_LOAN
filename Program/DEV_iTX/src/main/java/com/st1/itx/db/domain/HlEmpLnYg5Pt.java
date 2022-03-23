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
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * HlEmpLnYg5Pt 房貨專員目標檔案<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`HlEmpLnYg5Pt`")
public class HlEmpLnYg5Pt implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -8987239038338344577L;

@EmbeddedId
  private HlEmpLnYg5PtId hlEmpLnYg5PtId;

  // 年月份
  /* eric 2022.1.6 */
  @Column(name = "`WorkYM`", insertable = false, updatable = false)
  private int workYM = 0;

  // 員工代號
  /* eric 2022.1.6 */
  @Column(name = "`EmpNo`", length = 6, insertable = false, updatable = false)
  private String empNo;

  // 員工姓名
  /* eric 2022.1.6 */
  @Column(name = "`Fullname`", length = 40)
  private String fullname;

  // 區域中心
  /* eric 2022.1.6 */
  @Column(name = "`AreaCode`", length = 6)
  private String areaCode;

  // 中心中文
  /* eric 2022.1.6 */
  @Column(name = "`AreaItem`", length = 12)
  private String areaItem;

  // 部室代號
  /* eric 2022.1.6 */
  @Column(name = "`DeptCode`", length = 6)
  private String deptCode;

  // 部室中文
  /* eric 2022.1.6 */
  @Column(name = "`DepItem`", length = 12)
  private String depItem;

  // 區部代號
  /* eric 2022.1.6 */
  @Column(name = "`DistCode`", length = 6)
  private String distCode;

  // 區部中文
  /* eric 2022.1.6 */
  @Column(name = "`DistItem`", length = 30)
  private String distItem;

  // 駐在地
  /* eric 2022.1.6 */
  @Column(name = "`StationName`", length = 30)
  private String stationName;

  // 目標金額
  @Column(name = "`GoalAmt`")
  private BigDecimal goalAmt = new BigDecimal("0");

  // 房貸撥款件數
  @Column(name = "`HlAppNum`")
  private BigDecimal hlAppNum = new BigDecimal("0");

  // 房貸撥款金額
  @Column(name = "`HlAppAmt`")
  private BigDecimal hlAppAmt = new BigDecimal("0");

  // 車貸撥款件數
  @Column(name = "`ClAppNum`")
  private BigDecimal clAppNum = new BigDecimal("0");

  // 車貸撥款金額
  @Column(name = "`ClAppAmt`")
  private BigDecimal clAppAmt = new BigDecimal("0");

  // 信義撥款件數
  @Column(name = "`ServiceAppNum`")
  private BigDecimal serviceAppNum = new BigDecimal("0");

  // 信義撥款金額
  @Column(name = "`ServiceAppAmt`")
  private BigDecimal serviceAppAmt = new BigDecimal("0");

  // 資料日期
  /* eric 2022.1.6 */
  @Column(name = "`CalDate`")
  private int calDate = 0;

  // UpdateIdentifier
  /* 固定值1 */
  @Column(name = "`UpNo`")
  private int upNo = 0;

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


  public HlEmpLnYg5PtId getHlEmpLnYg5PtId() {
    return this.hlEmpLnYg5PtId;
  }

  public void setHlEmpLnYg5PtId(HlEmpLnYg5PtId hlEmpLnYg5PtId) {
    this.hlEmpLnYg5PtId = hlEmpLnYg5PtId;
  }

/**
	* 年月份<br>
	* eric 2022.1.6
	* @return Integer
	*/
  public int getWorkYM() {
    return this.workYM;
  }

/**
	* 年月份<br>
	* eric 2022.1.6
  *
  * @param workYM 年月份
	*/
  public void setWorkYM(int workYM) {
    this.workYM = workYM;
  }

/**
	* 員工代號<br>
	* eric 2022.1.6
	* @return String
	*/
  public String getEmpNo() {
    return this.empNo == null ? "" : this.empNo;
  }

/**
	* 員工代號<br>
	* eric 2022.1.6
  *
  * @param empNo 員工代號
	*/
  public void setEmpNo(String empNo) {
    this.empNo = empNo;
  }

/**
	* 員工姓名<br>
	* eric 2022.1.6
	* @return String
	*/
  public String getFullname() {
    return this.fullname == null ? "" : this.fullname;
  }

/**
	* 員工姓名<br>
	* eric 2022.1.6
  *
  * @param fullname 員工姓名
	*/
  public void setFullname(String fullname) {
    this.fullname = fullname;
  }

/**
	* 區域中心<br>
	* eric 2022.1.6
	* @return String
	*/
  public String getAreaCode() {
    return this.areaCode == null ? "" : this.areaCode;
  }

/**
	* 區域中心<br>
	* eric 2022.1.6
  *
  * @param areaCode 區域中心
	*/
  public void setAreaCode(String areaCode) {
    this.areaCode = areaCode;
  }

/**
	* 中心中文<br>
	* eric 2022.1.6
	* @return String
	*/
  public String getAreaItem() {
    return this.areaItem == null ? "" : this.areaItem;
  }

/**
	* 中心中文<br>
	* eric 2022.1.6
  *
  * @param areaItem 中心中文
	*/
  public void setAreaItem(String areaItem) {
    this.areaItem = areaItem;
  }

/**
	* 部室代號<br>
	* eric 2022.1.6
	* @return String
	*/
  public String getDeptCode() {
    return this.deptCode == null ? "" : this.deptCode;
  }

/**
	* 部室代號<br>
	* eric 2022.1.6
  *
  * @param deptCode 部室代號
	*/
  public void setDeptCode(String deptCode) {
    this.deptCode = deptCode;
  }

/**
	* 部室中文<br>
	* eric 2022.1.6
	* @return String
	*/
  public String getDepItem() {
    return this.depItem == null ? "" : this.depItem;
  }

/**
	* 部室中文<br>
	* eric 2022.1.6
  *
  * @param depItem 部室中文
	*/
  public void setDepItem(String depItem) {
    this.depItem = depItem;
  }

/**
	* 區部代號<br>
	* eric 2022.1.6
	* @return String
	*/
  public String getDistCode() {
    return this.distCode == null ? "" : this.distCode;
  }

/**
	* 區部代號<br>
	* eric 2022.1.6
  *
  * @param distCode 區部代號
	*/
  public void setDistCode(String distCode) {
    this.distCode = distCode;
  }

/**
	* 區部中文<br>
	* eric 2022.1.6
	* @return String
	*/
  public String getDistItem() {
    return this.distItem == null ? "" : this.distItem;
  }

/**
	* 區部中文<br>
	* eric 2022.1.6
  *
  * @param distItem 區部中文
	*/
  public void setDistItem(String distItem) {
    this.distItem = distItem;
  }

/**
	* 駐在地<br>
	* eric 2022.1.6
	* @return String
	*/
  public String getStationName() {
    return this.stationName == null ? "" : this.stationName;
  }

/**
	* 駐在地<br>
	* eric 2022.1.6
  *
  * @param stationName 駐在地
	*/
  public void setStationName(String stationName) {
    this.stationName = stationName;
  }

/**
	* 目標金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getGoalAmt() {
    return this.goalAmt;
  }

/**
	* 目標金額<br>
	* 
  *
  * @param goalAmt 目標金額
	*/
  public void setGoalAmt(BigDecimal goalAmt) {
    this.goalAmt = goalAmt;
  }

/**
	* 房貸撥款件數<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getHlAppNum() {
    return this.hlAppNum;
  }

/**
	* 房貸撥款件數<br>
	* 
  *
  * @param hlAppNum 房貸撥款件數
	*/
  public void setHlAppNum(BigDecimal hlAppNum) {
    this.hlAppNum = hlAppNum;
  }

/**
	* 房貸撥款金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getHlAppAmt() {
    return this.hlAppAmt;
  }

/**
	* 房貸撥款金額<br>
	* 
  *
  * @param hlAppAmt 房貸撥款金額
	*/
  public void setHlAppAmt(BigDecimal hlAppAmt) {
    this.hlAppAmt = hlAppAmt;
  }

/**
	* 車貸撥款件數<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getClAppNum() {
    return this.clAppNum;
  }

/**
	* 車貸撥款件數<br>
	* 
  *
  * @param clAppNum 車貸撥款件數
	*/
  public void setClAppNum(BigDecimal clAppNum) {
    this.clAppNum = clAppNum;
  }

/**
	* 車貸撥款金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getClAppAmt() {
    return this.clAppAmt;
  }

/**
	* 車貸撥款金額<br>
	* 
  *
  * @param clAppAmt 車貸撥款金額
	*/
  public void setClAppAmt(BigDecimal clAppAmt) {
    this.clAppAmt = clAppAmt;
  }

/**
	* 信義撥款件數<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getServiceAppNum() {
    return this.serviceAppNum;
  }

/**
	* 信義撥款件數<br>
	* 
  *
  * @param serviceAppNum 信義撥款件數
	*/
  public void setServiceAppNum(BigDecimal serviceAppNum) {
    this.serviceAppNum = serviceAppNum;
  }

/**
	* 信義撥款金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getServiceAppAmt() {
    return this.serviceAppAmt;
  }

/**
	* 信義撥款金額<br>
	* 
  *
  * @param serviceAppAmt 信義撥款金額
	*/
  public void setServiceAppAmt(BigDecimal serviceAppAmt) {
    this.serviceAppAmt = serviceAppAmt;
  }

/**
	* 資料日期<br>
	* eric 2022.1.6
	* @return Integer
	*/
  public int getCalDate() {
    return StaticTool.bcToRoc(this.calDate);
  }

/**
	* 資料日期<br>
	* eric 2022.1.6
  *
  * @param calDate 資料日期
  * @throws LogicException when Date Is Warn	*/
  public void setCalDate(int calDate) throws LogicException {
    this.calDate = StaticTool.rocToBc(calDate);
  }

/**
	* UpdateIdentifier<br>
	* 固定值1
	* @return Integer
	*/
  public int getUpNo() {
    return this.upNo;
  }

/**
	* UpdateIdentifier<br>
	* 固定值1
  *
  * @param upNo UpdateIdentifier
	*/
  public void setUpNo(int upNo) {
    this.upNo = upNo;
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
    return "HlEmpLnYg5Pt [hlEmpLnYg5PtId=" + hlEmpLnYg5PtId + ", fullname=" + fullname + ", areaCode=" + areaCode + ", areaItem=" + areaItem + ", deptCode=" + deptCode
           + ", depItem=" + depItem + ", distCode=" + distCode + ", distItem=" + distItem + ", stationName=" + stationName + ", goalAmt=" + goalAmt + ", hlAppNum=" + hlAppNum
           + ", hlAppAmt=" + hlAppAmt + ", clAppNum=" + clAppNum + ", clAppAmt=" + clAppAmt + ", serviceAppNum=" + serviceAppNum + ", serviceAppAmt=" + serviceAppAmt + ", calDate=" + calDate
           + ", upNo=" + upNo + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
