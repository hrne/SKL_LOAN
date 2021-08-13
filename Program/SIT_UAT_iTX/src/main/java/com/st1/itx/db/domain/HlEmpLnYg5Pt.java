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
 * HlEmpLnYg5Pt 員工目標檔案<br>
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
	private static final long serialVersionUID = -2788163327417143355L;

@EmbeddedId
  private HlEmpLnYg5PtId hlEmpLnYg5PtId;

  // 年月份
  @Column(name = "`WorkYM`", length = 10, insertable = false, updatable = false)
  private String workYM;

  // 單位代號
  @Column(name = "`AreaUnitNo`", length = 6, insertable = false, updatable = false)
  private String areaUnitNo;

  // 員工代號
  @Column(name = "`HlEmpNo`", length = 6, insertable = false, updatable = false)
  private String hlEmpNo;

  // 員工姓名
  @Column(name = "`HlEmpName`", length = 15)
  private String hlEmpName;

  // 部室代號
  @Column(name = "`DeptNo`", length = 6)
  private String deptNo;

  // 部室中文
  @Column(name = "`DeptName`", length = 20)
  private String deptName;

  // 駐在地
  @Column(name = "`Area`", length = 20)
  private String area;

  // 區部中文
  @Column(name = "`BranchName`", length = 20)
  private String branchName;

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

  // 年月日
  @Column(name = "`CalDate`", length = 10)
  private String calDate;

  // UpdateIdentifier
  @Column(name = "`UpNo`")
  private int upNo = 0;

  // 更新日期
  @Column(name = "`ProcessDate`")
  private int processDate = 0;

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
	* 
	* @return String
	*/
  public String getWorkYM() {
    return this.workYM == null ? "" : this.workYM;
  }

/**
	* 年月份<br>
	* 
  *
  * @param workYM 年月份
	*/
  public void setWorkYM(String workYM) {
    this.workYM = workYM;
  }

/**
	* 單位代號<br>
	* 
	* @return String
	*/
  public String getAreaUnitNo() {
    return this.areaUnitNo == null ? "" : this.areaUnitNo;
  }

/**
	* 單位代號<br>
	* 
  *
  * @param areaUnitNo 單位代號
	*/
  public void setAreaUnitNo(String areaUnitNo) {
    this.areaUnitNo = areaUnitNo;
  }

/**
	* 員工代號<br>
	* 
	* @return String
	*/
  public String getHlEmpNo() {
    return this.hlEmpNo == null ? "" : this.hlEmpNo;
  }

/**
	* 員工代號<br>
	* 
  *
  * @param hlEmpNo 員工代號
	*/
  public void setHlEmpNo(String hlEmpNo) {
    this.hlEmpNo = hlEmpNo;
  }

/**
	* 員工姓名<br>
	* 
	* @return String
	*/
  public String getHlEmpName() {
    return this.hlEmpName == null ? "" : this.hlEmpName;
  }

/**
	* 員工姓名<br>
	* 
  *
  * @param hlEmpName 員工姓名
	*/
  public void setHlEmpName(String hlEmpName) {
    this.hlEmpName = hlEmpName;
  }

/**
	* 部室代號<br>
	* 
	* @return String
	*/
  public String getDeptNo() {
    return this.deptNo == null ? "" : this.deptNo;
  }

/**
	* 部室代號<br>
	* 
  *
  * @param deptNo 部室代號
	*/
  public void setDeptNo(String deptNo) {
    this.deptNo = deptNo;
  }

/**
	* 部室中文<br>
	* 
	* @return String
	*/
  public String getDeptName() {
    return this.deptName == null ? "" : this.deptName;
  }

/**
	* 部室中文<br>
	* 
  *
  * @param deptName 部室中文
	*/
  public void setDeptName(String deptName) {
    this.deptName = deptName;
  }

/**
	* 駐在地<br>
	* 
	* @return String
	*/
  public String getArea() {
    return this.area == null ? "" : this.area;
  }

/**
	* 駐在地<br>
	* 
  *
  * @param area 駐在地
	*/
  public void setArea(String area) {
    this.area = area;
  }

/**
	* 區部中文<br>
	* 
	* @return String
	*/
  public String getBranchName() {
    return this.branchName == null ? "" : this.branchName;
  }

/**
	* 區部中文<br>
	* 
  *
  * @param branchName 區部中文
	*/
  public void setBranchName(String branchName) {
    this.branchName = branchName;
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
	* 年月日<br>
	* 
	* @return String
	*/
  public String getCalDate() {
    return this.calDate == null ? "" : this.calDate;
  }

/**
	* 年月日<br>
	* 
  *
  * @param calDate 年月日
	*/
  public void setCalDate(String calDate) {
    this.calDate = calDate;
  }

/**
	* UpdateIdentifier<br>
	* 
	* @return Integer
	*/
  public int getUpNo() {
    return this.upNo;
  }

/**
	* UpdateIdentifier<br>
	* 
  *
  * @param upNo UpdateIdentifier
	*/
  public void setUpNo(int upNo) {
    this.upNo = upNo;
  }

/**
	* 更新日期<br>
	* 
	* @return Integer
	*/
  public int getProcessDate() {
    return StaticTool.bcToRoc(this.processDate);
  }

/**
	* 更新日期<br>
	* 
  *
  * @param processDate 更新日期
  * @throws LogicException when Date Is Warn	*/
  public void setProcessDate(int processDate) throws LogicException {
    this.processDate = StaticTool.rocToBc(processDate);
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
    return "HlEmpLnYg5Pt [hlEmpLnYg5PtId=" + hlEmpLnYg5PtId + ", hlEmpName=" + hlEmpName + ", deptNo=" + deptNo + ", deptName=" + deptName
           + ", area=" + area + ", branchName=" + branchName + ", goalAmt=" + goalAmt + ", hlAppNum=" + hlAppNum + ", hlAppAmt=" + hlAppAmt + ", clAppNum=" + clAppNum
           + ", clAppAmt=" + clAppAmt + ", serviceAppNum=" + serviceAppNum + ", serviceAppAmt=" + serviceAppAmt + ", calDate=" + calDate + ", upNo=" + upNo + ", processDate=" + processDate
           + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
