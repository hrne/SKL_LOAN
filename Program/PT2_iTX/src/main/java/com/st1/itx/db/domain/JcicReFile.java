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
 * JcicReFile 聯徵回寫紀錄檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicReFile`")
public class JcicReFile implements Serializable {


  @EmbeddedId
  private JcicReFileId jcicReFileId;

  // 報送單位代號
  /* 3位文數字 */
  @Column(name = "`SubmitKey`", length = 3, insertable = false, updatable = false)
  private String submitKey;

  // 報送日期
  /* 西元年月日 */
  @Column(name = "`JcicDate`", insertable = false, updatable = false)
  private int jcicDate = 0;

  // 報送總筆數
  @Column(name = "`ReportTotal`")
  private int reportTotal = 0;

  // 正確筆數
  @Column(name = "`CorrectCount`")
  private int correctCount = 0;

  // 錯誤筆數
  @Column(name = "`MistakeCount`")
  private int mistakeCount = 0;

  // 未回檔筆數
  @Column(name = "`NoBackFileCount`")
  private int noBackFileCount = 0;

  // 未回檔日期
  /* 西元年月日 */
  @Column(name = "`NoBackFileDate`")
  private int noBackFileDate = 0;

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


  public JcicReFileId getJcicReFileId() {
    return this.jcicReFileId;
  }

  public void setJcicReFileId(JcicReFileId jcicReFileId) {
    this.jcicReFileId = jcicReFileId;
  }

/**
	* 報送單位代號<br>
	* 3位文數字
	* @return String
	*/
  public String getSubmitKey() {
    return this.submitKey == null ? "" : this.submitKey;
  }

/**
	* 報送單位代號<br>
	* 3位文數字
  *
  * @param submitKey 報送單位代號
	*/
  public void setSubmitKey(String submitKey) {
    this.submitKey = submitKey;
  }

/**
	* 報送日期<br>
	* 西元年月日
	* @return Integer
	*/
  public int getJcicDate() {
    return StaticTool.bcToRoc(this.jcicDate);
  }

/**
	* 報送日期<br>
	* 西元年月日
  *
  * @param jcicDate 報送日期
  * @throws LogicException when Date Is Warn	*/
  public void setJcicDate(int jcicDate) throws LogicException {
    this.jcicDate = StaticTool.rocToBc(jcicDate);
  }

/**
	* 報送總筆數<br>
	* 
	* @return Integer
	*/
  public int getReportTotal() {
    return this.reportTotal;
  }

/**
	* 報送總筆數<br>
	* 
  *
  * @param reportTotal 報送總筆數
	*/
  public void setReportTotal(int reportTotal) {
    this.reportTotal = reportTotal;
  }

/**
	* 正確筆數<br>
	* 
	* @return Integer
	*/
  public int getCorrectCount() {
    return this.correctCount;
  }

/**
	* 正確筆數<br>
	* 
  *
  * @param correctCount 正確筆數
	*/
  public void setCorrectCount(int correctCount) {
    this.correctCount = correctCount;
  }

/**
	* 錯誤筆數<br>
	* 
	* @return Integer
	*/
  public int getMistakeCount() {
    return this.mistakeCount;
  }

/**
	* 錯誤筆數<br>
	* 
  *
  * @param mistakeCount 錯誤筆數
	*/
  public void setMistakeCount(int mistakeCount) {
    this.mistakeCount = mistakeCount;
  }

/**
	* 未回檔筆數<br>
	* 
	* @return Integer
	*/
  public int getNoBackFileCount() {
    return this.noBackFileCount;
  }

/**
	* 未回檔筆數<br>
	* 
  *
  * @param noBackFileCount 未回檔筆數
	*/
  public void setNoBackFileCount(int noBackFileCount) {
    this.noBackFileCount = noBackFileCount;
  }

/**
	* 未回檔日期<br>
	* 西元年月日
	* @return Integer
	*/
  public int getNoBackFileDate() {
    return StaticTool.bcToRoc(this.noBackFileDate);
  }

/**
	* 未回檔日期<br>
	* 西元年月日
  *
  * @param noBackFileDate 未回檔日期
  * @throws LogicException when Date Is Warn	*/
  public void setNoBackFileDate(int noBackFileDate) throws LogicException {
    this.noBackFileDate = StaticTool.rocToBc(noBackFileDate);
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
    return "JcicReFile [jcicReFileId=" + jcicReFileId + ", reportTotal=" + reportTotal + ", correctCount=" + correctCount + ", mistakeCount=" + mistakeCount + ", noBackFileCount=" + noBackFileCount
           + ", noBackFileDate=" + noBackFileDate + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
