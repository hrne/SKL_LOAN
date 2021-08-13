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
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * EmpDeductSchedule 員工扣薪日程表<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`EmpDeductSchedule`")
public class EmpDeductSchedule implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 9220474681881759446L;

@EmbeddedId
  private EmpDeductScheduleId empDeductScheduleId;

  // 工作年月
  /* 業績年月 */
  @Column(name = "`WorkMonth`", insertable = false, updatable = false)
  private int workMonth = 0;

  // 流程/制度別
  /* 同CdEmp.AgType1制度別對應扣薪種類於CdCode維護 */
  @Column(name = "`AgType1`", length = 1, insertable = false, updatable = false)
  private String agType1;

  // 入帳日期
  @Column(name = "`EntryDate`")
  private int entryDate = 0;

  // 媒體日期
  @Column(name = "`MediaDate`")
  private int mediaDate = 0;

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


  public EmpDeductScheduleId getEmpDeductScheduleId() {
    return this.empDeductScheduleId;
  }

  public void setEmpDeductScheduleId(EmpDeductScheduleId empDeductScheduleId) {
    this.empDeductScheduleId = empDeductScheduleId;
  }

/**
	* 工作年月<br>
	* 業績年月
	* @return Integer
	*/
  public int getWorkMonth() {
    return this.workMonth;
  }

/**
	* 工作年月<br>
	* 業績年月
  *
  * @param workMonth 工作年月
	*/
  public void setWorkMonth(int workMonth) {
    this.workMonth = workMonth;
  }

/**
	* 流程/制度別<br>
	* 同CdEmp.AgType1制度別
對應扣薪種類於CdCode維護
	* @return String
	*/
  public String getAgType1() {
    return this.agType1 == null ? "" : this.agType1;
  }

/**
	* 流程/制度別<br>
	* 同CdEmp.AgType1制度別
對應扣薪種類於CdCode維護
  *
  * @param agType1 流程/制度別
	*/
  public void setAgType1(String agType1) {
    this.agType1 = agType1;
  }

/**
	* 入帳日期<br>
	* 
	* @return Integer
	*/
  public int getEntryDate() {
    return StaticTool.bcToRoc(this.entryDate);
  }

/**
	* 入帳日期<br>
	* 
  *
  * @param entryDate 入帳日期
  * @throws LogicException when Date Is Warn	*/
  public void setEntryDate(int entryDate) throws LogicException {
    this.entryDate = StaticTool.rocToBc(entryDate);
  }

/**
	* 媒體日期<br>
	* 
	* @return Integer
	*/
  public int getMediaDate() {
    return StaticTool.bcToRoc(this.mediaDate);
  }

/**
	* 媒體日期<br>
	* 
  *
  * @param mediaDate 媒體日期
  * @throws LogicException when Date Is Warn	*/
  public void setMediaDate(int mediaDate) throws LogicException {
    this.mediaDate = StaticTool.rocToBc(mediaDate);
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
    return "EmpDeductSchedule [empDeductScheduleId=" + empDeductScheduleId + ", entryDate=" + entryDate + ", mediaDate=" + mediaDate + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
           + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
