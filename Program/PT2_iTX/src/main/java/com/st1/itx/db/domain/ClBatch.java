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
 * ClBatch 擔保品整批匯入檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`ClBatch`")
public class ClBatch implements Serializable {


  @EmbeddedId
  private ClBatchId clBatchId;

  // 批號
  /* 核准號碼(7碼)+流水號(3碼) */
  @Column(name = "`GroupNo`", length = 10, insertable = false, updatable = false)
  private String groupNo;

  // 序號
  /* 同一批號的第一筆為1號,依此類推 */
  @Column(name = "`Seq`", insertable = false, updatable = false)
  private int seq = 0;

  // 核准號碼
  /* L2419畫面輸入的核准號碼 */
  @Column(name = "`ApplNo`")
  private int applNo = 0;

  // 鑑價公司代碼
  /* L2419畫面輸入的鑑價公司代碼 */
  @Column(name = "`EvaCompany`", length = 2)
  private String evaCompany;

  // 鑑價日期
  /* L2419畫面輸入的鑑價日期 */
  @Column(name = "`EvaDate`")
  private int evaDate = 0;

  // 擔保品代號1
  /* L2419上傳檔案中的擔保品代號1*回饋檔中不得修改 */
  @Column(name = "`ClCode1`")
  private int clCode1 = 0;

  // 擔保品代號2
  /* L2419上傳檔案中的擔保品代號2*回饋檔中不得修改 */
  @Column(name = "`ClCode2`")
  private int clCode2 = 0;

  // 擔保品編號
  /* L2419上傳檔案時，由程式取號的擔保品編號*回饋檔中不得修改 */
  @Column(name = "`ClNo`")
  private int clNo = 0;

  // 寫入狀態
  /* 0:未寫入擔保品檔1:已寫入擔保品檔 */
  @Column(name = "`InsertStatus`")
  private int insertStatus = 0;

  // 建檔日期時間
  /* 第一次上傳成功時間 */
  @CreatedDate
  @Column(name = "`CreateDate`")
  private java.sql.Timestamp createDate;

  // 建檔人員
  /* 第一次上傳成功之櫃員 */
  @Column(name = "`CreateEmpNo`", length = 6)
  private String createEmpNo;

  // 最後更新日期時間
  @LastModifiedDate
  @Column(name = "`LastUpdate`")
  private java.sql.Timestamp lastUpdate;

  // 最後更新人員
  @Column(name = "`LastUpdateEmpNo`", length = 6)
  private String lastUpdateEmpNo;


  public ClBatchId getClBatchId() {
    return this.clBatchId;
  }

  public void setClBatchId(ClBatchId clBatchId) {
    this.clBatchId = clBatchId;
  }

/**
	* 批號<br>
	* 核准號碼(7碼)+流水號(3碼)
	* @return String
	*/
  public String getGroupNo() {
    return this.groupNo == null ? "" : this.groupNo;
  }

/**
	* 批號<br>
	* 核准號碼(7碼)+流水號(3碼)
  *
  * @param groupNo 批號
	*/
  public void setGroupNo(String groupNo) {
    this.groupNo = groupNo;
  }

/**
	* 序號<br>
	* 同一批號的第一筆為1號,依此類推
	* @return Integer
	*/
  public int getSeq() {
    return this.seq;
  }

/**
	* 序號<br>
	* 同一批號的第一筆為1號,依此類推
  *
  * @param seq 序號
	*/
  public void setSeq(int seq) {
    this.seq = seq;
  }

/**
	* 核准號碼<br>
	* L2419畫面輸入的核准號碼
	* @return Integer
	*/
  public int getApplNo() {
    return this.applNo;
  }

/**
	* 核准號碼<br>
	* L2419畫面輸入的核准號碼
  *
  * @param applNo 核准號碼
	*/
  public void setApplNo(int applNo) {
    this.applNo = applNo;
  }

/**
	* 鑑價公司代碼<br>
	* L2419畫面輸入的鑑價公司代碼
	* @return String
	*/
  public String getEvaCompany() {
    return this.evaCompany == null ? "" : this.evaCompany;
  }

/**
	* 鑑價公司代碼<br>
	* L2419畫面輸入的鑑價公司代碼
  *
  * @param evaCompany 鑑價公司代碼
	*/
  public void setEvaCompany(String evaCompany) {
    this.evaCompany = evaCompany;
  }

/**
	* 鑑價日期<br>
	* L2419畫面輸入的鑑價日期
	* @return Integer
	*/
  public int getEvaDate() {
    return StaticTool.bcToRoc(this.evaDate);
  }

/**
	* 鑑價日期<br>
	* L2419畫面輸入的鑑價日期
  *
  * @param evaDate 鑑價日期
  * @throws LogicException when Date Is Warn	*/
  public void setEvaDate(int evaDate) throws LogicException {
    this.evaDate = StaticTool.rocToBc(evaDate);
  }

/**
	* 擔保品代號1<br>
	* L2419上傳檔案中的擔保品代號1
*回饋檔中不得修改
	* @return Integer
	*/
  public int getClCode1() {
    return this.clCode1;
  }

/**
	* 擔保品代號1<br>
	* L2419上傳檔案中的擔保品代號1
*回饋檔中不得修改
  *
  * @param clCode1 擔保品代號1
	*/
  public void setClCode1(int clCode1) {
    this.clCode1 = clCode1;
  }

/**
	* 擔保品代號2<br>
	* L2419上傳檔案中的擔保品代號2
*回饋檔中不得修改
	* @return Integer
	*/
  public int getClCode2() {
    return this.clCode2;
  }

/**
	* 擔保品代號2<br>
	* L2419上傳檔案中的擔保品代號2
*回饋檔中不得修改
  *
  * @param clCode2 擔保品代號2
	*/
  public void setClCode2(int clCode2) {
    this.clCode2 = clCode2;
  }

/**
	* 擔保品編號<br>
	* L2419上傳檔案時，由程式取號的擔保品編號
*回饋檔中不得修改
	* @return Integer
	*/
  public int getClNo() {
    return this.clNo;
  }

/**
	* 擔保品編號<br>
	* L2419上傳檔案時，由程式取號的擔保品編號
*回饋檔中不得修改
  *
  * @param clNo 擔保品編號
	*/
  public void setClNo(int clNo) {
    this.clNo = clNo;
  }

/**
	* 寫入狀態<br>
	* 0:未寫入擔保品檔
1:已寫入擔保品檔
	* @return Integer
	*/
  public int getInsertStatus() {
    return this.insertStatus;
  }

/**
	* 寫入狀態<br>
	* 0:未寫入擔保品檔
1:已寫入擔保品檔
  *
  * @param insertStatus 寫入狀態
	*/
  public void setInsertStatus(int insertStatus) {
    this.insertStatus = insertStatus;
  }

/**
	* 建檔日期時間<br>
	* 第一次上傳成功時間
	* @return java.sql.Timestamp
	*/
  public java.sql.Timestamp getCreateDate() {
    return this.createDate;
  }

/**
	* 建檔日期時間<br>
	* 第一次上傳成功時間
  *
  * @param createDate 建檔日期時間
	*/
  public void setCreateDate(java.sql.Timestamp createDate) {
    this.createDate = createDate;
  }

/**
	* 建檔人員<br>
	* 第一次上傳成功之櫃員
	* @return String
	*/
  public String getCreateEmpNo() {
    return this.createEmpNo == null ? "" : this.createEmpNo;
  }

/**
	* 建檔人員<br>
	* 第一次上傳成功之櫃員
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
    return "ClBatch [clBatchId=" + clBatchId + ", applNo=" + applNo + ", evaCompany=" + evaCompany + ", evaDate=" + evaDate + ", clCode1=" + clCode1
           + ", clCode2=" + clCode2 + ", clNo=" + clNo + ", insertStatus=" + insertStatus + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
           + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
