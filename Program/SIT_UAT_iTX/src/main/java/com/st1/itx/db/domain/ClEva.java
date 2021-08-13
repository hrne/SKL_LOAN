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
 * ClEva 擔保品重評資料檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`ClEva`")
public class ClEva implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -6240482780846092796L;

@EmbeddedId
  private ClEvaId clEvaId;

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

  // 鑑估序號
  @Column(name = "`EvaNo`", insertable = false, updatable = false)
  private int evaNo = 0;

  // 鑑價日期
  @Column(name = "`EvaDate`")
  private int evaDate = 0;

  // 評估總價
  /* 必須輸入 */
  @Column(name = "`EvaAmt`")
  private BigDecimal evaAmt = new BigDecimal("0");

  // 評估淨值
  /* 可不輸入 */
  @Column(name = "`EvaNetWorth`")
  private BigDecimal evaNetWorth = new BigDecimal("0");

  // 出租評估淨值
  /* 可不輸入 */
  @Column(name = "`RentEvaValue`")
  private BigDecimal rentEvaValue = new BigDecimal("0");

  // 估價公司代碼
  @Column(name = "`EvaCompanyId`", length = 2)
  private String evaCompanyId;

  // 估價公司名稱
  @Column(name = "`EvaCompanyName`", length = 100)
  private String evaCompanyName;

  // 估價人員
  @Column(name = "`EvaEmpno`", length = 6)
  private String evaEmpno;

  // 重評原因
  /* 01:原因102:原因299:其他原因 */
  @Column(name = "`EvaReason`")
  private int evaReason = 0;

  // 其他重評原因
  @Column(name = "`OtherReason`", length = 100)
  private String otherReason;

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


  public ClEvaId getClEvaId() {
    return this.clEvaId;
  }

  public void setClEvaId(ClEvaId clEvaId) {
    this.clEvaId = clEvaId;
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
	* 鑑估序號<br>
	* 
	* @return Integer
	*/
  public int getEvaNo() {
    return this.evaNo;
  }

/**
	* 鑑估序號<br>
	* 
  *
  * @param evaNo 鑑估序號
	*/
  public void setEvaNo(int evaNo) {
    this.evaNo = evaNo;
  }

/**
	* 鑑價日期<br>
	* 
	* @return Integer
	*/
  public int getEvaDate() {
    return StaticTool.bcToRoc(this.evaDate);
  }

/**
	* 鑑價日期<br>
	* 
  *
  * @param evaDate 鑑價日期
  * @throws LogicException when Date Is Warn	*/
  public void setEvaDate(int evaDate) throws LogicException {
    this.evaDate = StaticTool.rocToBc(evaDate);
  }

/**
	* 評估總價<br>
	* 必須輸入
	* @return BigDecimal
	*/
  public BigDecimal getEvaAmt() {
    return this.evaAmt;
  }

/**
	* 評估總價<br>
	* 必須輸入
  *
  * @param evaAmt 評估總價
	*/
  public void setEvaAmt(BigDecimal evaAmt) {
    this.evaAmt = evaAmt;
  }

/**
	* 評估淨值<br>
	* 可不輸入
	* @return BigDecimal
	*/
  public BigDecimal getEvaNetWorth() {
    return this.evaNetWorth;
  }

/**
	* 評估淨值<br>
	* 可不輸入
  *
  * @param evaNetWorth 評估淨值
	*/
  public void setEvaNetWorth(BigDecimal evaNetWorth) {
    this.evaNetWorth = evaNetWorth;
  }

/**
	* 出租評估淨值<br>
	* 可不輸入
	* @return BigDecimal
	*/
  public BigDecimal getRentEvaValue() {
    return this.rentEvaValue;
  }

/**
	* 出租評估淨值<br>
	* 可不輸入
  *
  * @param rentEvaValue 出租評估淨值
	*/
  public void setRentEvaValue(BigDecimal rentEvaValue) {
    this.rentEvaValue = rentEvaValue;
  }

/**
	* 估價公司代碼<br>
	* 
	* @return String
	*/
  public String getEvaCompanyId() {
    return this.evaCompanyId == null ? "" : this.evaCompanyId;
  }

/**
	* 估價公司代碼<br>
	* 
  *
  * @param evaCompanyId 估價公司代碼
	*/
  public void setEvaCompanyId(String evaCompanyId) {
    this.evaCompanyId = evaCompanyId;
  }

/**
	* 估價公司名稱<br>
	* 
	* @return String
	*/
  public String getEvaCompanyName() {
    return this.evaCompanyName == null ? "" : this.evaCompanyName;
  }

/**
	* 估價公司名稱<br>
	* 
  *
  * @param evaCompanyName 估價公司名稱
	*/
  public void setEvaCompanyName(String evaCompanyName) {
    this.evaCompanyName = evaCompanyName;
  }

/**
	* 估價人員<br>
	* 
	* @return String
	*/
  public String getEvaEmpno() {
    return this.evaEmpno == null ? "" : this.evaEmpno;
  }

/**
	* 估價人員<br>
	* 
  *
  * @param evaEmpno 估價人員
	*/
  public void setEvaEmpno(String evaEmpno) {
    this.evaEmpno = evaEmpno;
  }

/**
	* 重評原因<br>
	* 01:原因1
02:原因2
99:其他原因
	* @return Integer
	*/
  public int getEvaReason() {
    return this.evaReason;
  }

/**
	* 重評原因<br>
	* 01:原因1
02:原因2
99:其他原因
  *
  * @param evaReason 重評原因
	*/
  public void setEvaReason(int evaReason) {
    this.evaReason = evaReason;
  }

/**
	* 其他重評原因<br>
	* 
	* @return String
	*/
  public String getOtherReason() {
    return this.otherReason == null ? "" : this.otherReason;
  }

/**
	* 其他重評原因<br>
	* 
  *
  * @param otherReason 其他重評原因
	*/
  public void setOtherReason(String otherReason) {
    this.otherReason = otherReason;
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
    return "ClEva [clEvaId=" + clEvaId + ", evaDate=" + evaDate + ", evaAmt=" + evaAmt
           + ", evaNetWorth=" + evaNetWorth + ", rentEvaValue=" + rentEvaValue + ", evaCompanyId=" + evaCompanyId + ", evaCompanyName=" + evaCompanyName + ", evaEmpno=" + evaEmpno + ", evaReason=" + evaReason
           + ", otherReason=" + otherReason + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
