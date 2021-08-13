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
 * NegAppr 撥付日期設定<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`NegAppr`")
public class NegAppr implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -8270272865353052619L;

@EmbeddedId
  private NegApprId negApprId;

  // 年月
  @Column(name = "`YyyyMm`", insertable = false, updatable = false)
  private int yyyyMm = 0;

  // 類別
  /* 1:一分2:二分、調解3:更生4:清算 */
  @Column(name = "`KindCode`", insertable = false, updatable = false)
  private int kindCode = 0;

  // 製檔日
  @Column(name = "`ExportDate`")
  private int exportDate = 0;

  // 傳票日
  @Column(name = "`ApprAcDate`")
  private int apprAcDate = 0;

  // 提兌日
  @Column(name = "`BringUpDate`")
  private int bringUpDate = 0;

  // 製檔日記號
  /* 0:未異動1:已處理 */
  @Column(name = "`ExportMark`")
  private int exportMark = 0;

  // 傳票日記號
  /* 0:未異動1:已處理 */
  @Column(name = "`ApprAcMark`")
  private int apprAcMark = 0;

  // 提兌日記號
  /* 0:未異動1:已處理 */
  @Column(name = "`BringUpMark`")
  private int bringUpMark = 0;

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


  public NegApprId getNegApprId() {
    return this.negApprId;
  }

  public void setNegApprId(NegApprId negApprId) {
    this.negApprId = negApprId;
  }

/**
	* 年月<br>
	* 
	* @return Integer
	*/
  public int getYyyyMm() {
    return this.yyyyMm;
  }

/**
	* 年月<br>
	* 
  *
  * @param yyyyMm 年月
	*/
  public void setYyyyMm(int yyyyMm) {
    this.yyyyMm = yyyyMm;
  }

/**
	* 類別<br>
	* 1:一分
2:二分、調解
3:更生
4:清算
	* @return Integer
	*/
  public int getKindCode() {
    return this.kindCode;
  }

/**
	* 類別<br>
	* 1:一分
2:二分、調解
3:更生
4:清算
  *
  * @param kindCode 類別
	*/
  public void setKindCode(int kindCode) {
    this.kindCode = kindCode;
  }

/**
	* 製檔日<br>
	* 
	* @return Integer
	*/
  public int getExportDate() {
    return StaticTool.bcToRoc(this.exportDate);
  }

/**
	* 製檔日<br>
	* 
  *
  * @param exportDate 製檔日
  * @throws LogicException when Date Is Warn	*/
  public void setExportDate(int exportDate) throws LogicException {
    this.exportDate = StaticTool.rocToBc(exportDate);
  }

/**
	* 傳票日<br>
	* 
	* @return Integer
	*/
  public int getApprAcDate() {
    return StaticTool.bcToRoc(this.apprAcDate);
  }

/**
	* 傳票日<br>
	* 
  *
  * @param apprAcDate 傳票日
  * @throws LogicException when Date Is Warn	*/
  public void setApprAcDate(int apprAcDate) throws LogicException {
    this.apprAcDate = StaticTool.rocToBc(apprAcDate);
  }

/**
	* 提兌日<br>
	* 
	* @return Integer
	*/
  public int getBringUpDate() {
    return StaticTool.bcToRoc(this.bringUpDate);
  }

/**
	* 提兌日<br>
	* 
  *
  * @param bringUpDate 提兌日
  * @throws LogicException when Date Is Warn	*/
  public void setBringUpDate(int bringUpDate) throws LogicException {
    this.bringUpDate = StaticTool.rocToBc(bringUpDate);
  }

/**
	* 製檔日記號<br>
	* 0:未異動
1:已處理
	* @return Integer
	*/
  public int getExportMark() {
    return this.exportMark;
  }

/**
	* 製檔日記號<br>
	* 0:未異動
1:已處理
  *
  * @param exportMark 製檔日記號
	*/
  public void setExportMark(int exportMark) {
    this.exportMark = exportMark;
  }

/**
	* 傳票日記號<br>
	* 0:未異動
1:已處理
	* @return Integer
	*/
  public int getApprAcMark() {
    return this.apprAcMark;
  }

/**
	* 傳票日記號<br>
	* 0:未異動
1:已處理
  *
  * @param apprAcMark 傳票日記號
	*/
  public void setApprAcMark(int apprAcMark) {
    this.apprAcMark = apprAcMark;
  }

/**
	* 提兌日記號<br>
	* 0:未異動
1:已處理
	* @return Integer
	*/
  public int getBringUpMark() {
    return this.bringUpMark;
  }

/**
	* 提兌日記號<br>
	* 0:未異動
1:已處理
  *
  * @param bringUpMark 提兌日記號
	*/
  public void setBringUpMark(int bringUpMark) {
    this.bringUpMark = bringUpMark;
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
    return "NegAppr [negApprId=" + negApprId + ", exportDate=" + exportDate + ", apprAcDate=" + apprAcDate + ", bringUpDate=" + bringUpDate + ", exportMark=" + exportMark
           + ", apprAcMark=" + apprAcMark + ", bringUpMark=" + bringUpMark + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo
           + "]";
  }
}
