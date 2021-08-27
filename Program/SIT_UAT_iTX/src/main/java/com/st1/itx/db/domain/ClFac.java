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

/**
 * ClFac 擔保品與額度關聯檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`ClFac`")
public class ClFac implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 6290702549507426016L;

@EmbeddedId
  private ClFacId clFacId;

  // 擔保品代號1
  /* 擔保品代號檔CdCl */
  @Column(name = "`ClCode1`", insertable = false, updatable = false)
  private int clCode1 = 0;

  // 擔保品代號2
  /* 擔保品代號檔CdCl */
  @Column(name = "`ClCode2`", insertable = false, updatable = false)
  private int clCode2 = 0;

  // 擔保品編號
  @Column(name = "`ClNo`", insertable = false, updatable = false)
  private int clNo = 0;

  // 核准號碼
  /* 規劃調整為ApplNo */
  @Column(name = "`ApproveNo`", insertable = false, updatable = false)
  private int approveNo = 0;

  // 借款人戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 額度編號
  @Column(name = "`FacmNo`")
  private int facmNo = 0;

  // 主要擔保品記號
  /* 是否為主要擔保品(每個核准號碼只能有一筆擔保品為主要擔保品)Y:是N:否 */
  @Column(name = "`MainFlag`", length = 1)
  private String mainFlag;

  // 共用額度記號
  /* 運算用不更新 */
  @Column(name = "`FacShareFlag`")
  private int facShareFlag = 0;

  // 分配金額
  /* 放款餘額的佔用金額，運算用不更新1.先占用單獨擔保品2.再占用共同擔保品3.其他額度優先占用其他擔保品 */
  @Column(name = "`ShareAmt`")
  private BigDecimal shareAmt = new BigDecimal("0");

  // 設定金額
  /* 擔保品與額度綁定時當下的設定金額(擔保品最新的設定金額應到各類擔保品檔查詢) */
  @Column(name = "`OriSettingAmt`")
  private BigDecimal oriSettingAmt = new BigDecimal("0");

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


  public ClFacId getClFacId() {
    return this.clFacId;
  }

  public void setClFacId(ClFacId clFacId) {
    this.clFacId = clFacId;
  }

/**
	* 擔保品代號1<br>
	* 擔保品代號檔CdCl
	* @return Integer
	*/
  public int getClCode1() {
    return this.clCode1;
  }

/**
	* 擔保品代號1<br>
	* 擔保品代號檔CdCl
  *
  * @param clCode1 擔保品代號1
	*/
  public void setClCode1(int clCode1) {
    this.clCode1 = clCode1;
  }

/**
	* 擔保品代號2<br>
	* 擔保品代號檔CdCl
	* @return Integer
	*/
  public int getClCode2() {
    return this.clCode2;
  }

/**
	* 擔保品代號2<br>
	* 擔保品代號檔CdCl
  *
  * @param clCode2 擔保品代號2
	*/
  public void setClCode2(int clCode2) {
    this.clCode2 = clCode2;
  }

/**
	* 擔保品編號<br>
	* 
	* @return Integer
	*/
  public int getClNo() {
    return this.clNo;
  }

/**
	* 擔保品編號<br>
	* 
  *
  * @param clNo 擔保品編號
	*/
  public void setClNo(int clNo) {
    this.clNo = clNo;
  }

/**
	* 核准號碼<br>
	* 規劃調整為ApplNo
	* @return Integer
	*/
  public int getApproveNo() {
    return this.approveNo;
  }

/**
	* 核准號碼<br>
	* 規劃調整為ApplNo
  *
  * @param approveNo 核准號碼
	*/
  public void setApproveNo(int approveNo) {
    this.approveNo = approveNo;
  }

/**
	* 借款人戶號<br>
	* 
	* @return Integer
	*/
  public int getCustNo() {
    return this.custNo;
  }

/**
	* 借款人戶號<br>
	* 
  *
  * @param custNo 借款人戶號
	*/
  public void setCustNo(int custNo) {
    this.custNo = custNo;
  }

/**
	* 額度編號<br>
	* 
	* @return Integer
	*/
  public int getFacmNo() {
    return this.facmNo;
  }

/**
	* 額度編號<br>
	* 
  *
  * @param facmNo 額度編號
	*/
  public void setFacmNo(int facmNo) {
    this.facmNo = facmNo;
  }

/**
	* 主要擔保品記號<br>
	* 是否為主要擔保品(每個核准號碼只能有一筆擔保品為主要擔保品)
Y:是
N:否
	* @return String
	*/
  public String getMainFlag() {
    return this.mainFlag == null ? "" : this.mainFlag;
  }

/**
	* 主要擔保品記號<br>
	* 是否為主要擔保品(每個核准號碼只能有一筆擔保品為主要擔保品)
Y:是
N:否
  *
  * @param mainFlag 主要擔保品記號
	*/
  public void setMainFlag(String mainFlag) {
    this.mainFlag = mainFlag;
  }

/**
	* 共用額度記號<br>
	* 運算用不更新
	* @return Integer
	*/
  public int getFacShareFlag() {
    return this.facShareFlag;
  }

/**
	* 共用額度記號<br>
	* 運算用不更新
  *
  * @param facShareFlag 共用額度記號
	*/
  public void setFacShareFlag(int facShareFlag) {
    this.facShareFlag = facShareFlag;
  }

/**
	* 分配金額<br>
	* 放款餘額的佔用金額，運算用不更新
1.先占用單獨擔保品
2.再占用共同擔保品
3.其他額度優先占用其他擔保品
	* @return BigDecimal
	*/
  public BigDecimal getShareAmt() {
    return this.shareAmt;
  }

/**
	* 分配金額<br>
	* 放款餘額的佔用金額，運算用不更新
1.先占用單獨擔保品
2.再占用共同擔保品
3.其他額度優先占用其他擔保品
  *
  * @param shareAmt 分配金額
	*/
  public void setShareAmt(BigDecimal shareAmt) {
    this.shareAmt = shareAmt;
  }

/**
	* 設定金額<br>
	* 擔保品與額度綁定時當下的設定金額
(擔保品最新的設定金額應到各類擔保品檔查詢)
	* @return BigDecimal
	*/
  public BigDecimal getOriSettingAmt() {
    return this.oriSettingAmt;
  }

/**
	* 設定金額<br>
	* 擔保品與額度綁定時當下的設定金額
(擔保品最新的設定金額應到各類擔保品檔查詢)
  *
  * @param oriSettingAmt 設定金額
	*/
  public void setOriSettingAmt(BigDecimal oriSettingAmt) {
    this.oriSettingAmt = oriSettingAmt;
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
    return "ClFac [clFacId=" + clFacId + ", custNo=" + custNo + ", facmNo=" + facmNo
           + ", mainFlag=" + mainFlag + ", facShareFlag=" + facShareFlag + ", shareAmt=" + shareAmt + ", oriSettingAmt=" + oriSettingAmt + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
           + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
