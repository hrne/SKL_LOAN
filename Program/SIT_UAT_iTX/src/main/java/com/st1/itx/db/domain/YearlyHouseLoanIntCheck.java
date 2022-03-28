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

/**
 * YearlyHouseLoanIntCheck 每年房屋擔保借款繳息檢核檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`YearlyHouseLoanIntCheck`")
public class YearlyHouseLoanIntCheck implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -855361955025937198L;

@EmbeddedId
  private YearlyHouseLoanIntCheckId yearlyHouseLoanIntCheckId;

  // 資料年月
  @Column(name = "`YearMonth`", insertable = false, updatable = false)
  private int yearMonth = 0;

  // 戶號
  @Column(name = "`CustNo`", insertable = false, updatable = false)
  private int custNo = 0;

  // 額度編號
  @Column(name = "`FacmNo`", insertable = false, updatable = false)
  private int facmNo = 0;

  // 資金用途別
  /* 02:購置不動產00:全部 */
  @Column(name = "`UsageCode`", length = 2, insertable = false, updatable = false)
  private String usageCode;

  // 借戶姓名空白
  @Column(name = "`R1`", length = 1)
  private String r1;

  // 統一編號空白
  @Column(name = "`R2`", length = 1)
  private String r2;

  // 貸款帳號空白
  @Column(name = "`R3`", length = 1)
  private String r3;

  // 最初貸款金額為0
  @Column(name = "`R4`", length = 1)
  private String r4;

  // 最初貸款金額大於核准額度
  @Column(name = "`R5`", length = 1)
  private String r5;

  // 最初貸款金額小於放款餘額
  @Column(name = "`R6`", length = 1)
  private String r6;

  // 貸款起日空白
  @Column(name = "`R7`", length = 1)
  private String r7;

  // 貸款迄日空白
  @Column(name = "`R8`", length = 1)
  private String r8;

  // 繳息所屬年月空白
  @Column(name = "`R10`", length = 1)
  private String r10;

  // 繳息金額為0
  @Column(name = "`R11`", length = 1)
  private String r11;

  // 科子細目代號暨說明空白
  @Column(name = "`R12`", length = 1)
  private String r12;

  // 一額度一撥款
  @Column(name = "`C1`", length = 1)
  private String c1;

  // 一額度多撥款
  @Column(name = "`C2`", length = 1)
  private String c2;

  // 多額度多撥款
  @Column(name = "`C3`", length = 1)
  private String c3;

  // 借新還舊件
  @Column(name = "`C4`", length = 1)
  private String c4;

  // 清償件
  @Column(name = "`C5`", length = 1)
  private String c5;

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


  public YearlyHouseLoanIntCheckId getYearlyHouseLoanIntCheckId() {
    return this.yearlyHouseLoanIntCheckId;
  }

  public void setYearlyHouseLoanIntCheckId(YearlyHouseLoanIntCheckId yearlyHouseLoanIntCheckId) {
    this.yearlyHouseLoanIntCheckId = yearlyHouseLoanIntCheckId;
  }

/**
	* 資料年月<br>
	* 
	* @return Integer
	*/
  public int getYearMonth() {
    return this.yearMonth;
  }

/**
	* 資料年月<br>
	* 
  *
  * @param yearMonth 資料年月
	*/
  public void setYearMonth(int yearMonth) {
    this.yearMonth = yearMonth;
  }

/**
	* 戶號<br>
	* 
	* @return Integer
	*/
  public int getCustNo() {
    return this.custNo;
  }

/**
	* 戶號<br>
	* 
  *
  * @param custNo 戶號
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
	* 資金用途別<br>
	* 02:購置不動產
00:全部
	* @return String
	*/
  public String getUsageCode() {
    return this.usageCode == null ? "" : this.usageCode;
  }

/**
	* 資金用途別<br>
	* 02:購置不動產
00:全部
  *
  * @param usageCode 資金用途別
	*/
  public void setUsageCode(String usageCode) {
    this.usageCode = usageCode;
  }

/**
	* 借戶姓名空白<br>
	* 
	* @return String
	*/
  public String getR1() {
    return this.r1 == null ? "" : this.r1;
  }

/**
	* 借戶姓名空白<br>
	* 
  *
  * @param r1 借戶姓名空白
	*/
  public void setR1(String r1) {
    this.r1 = r1;
  }

/**
	* 統一編號空白<br>
	* 
	* @return String
	*/
  public String getR2() {
    return this.r2 == null ? "" : this.r2;
  }

/**
	* 統一編號空白<br>
	* 
  *
  * @param r2 統一編號空白
	*/
  public void setR2(String r2) {
    this.r2 = r2;
  }

/**
	* 貸款帳號空白<br>
	* 
	* @return String
	*/
  public String getR3() {
    return this.r3 == null ? "" : this.r3;
  }

/**
	* 貸款帳號空白<br>
	* 
  *
  * @param r3 貸款帳號空白
	*/
  public void setR3(String r3) {
    this.r3 = r3;
  }

/**
	* 最初貸款金額為0<br>
	* 
	* @return String
	*/
  public String getR4() {
    return this.r4 == null ? "" : this.r4;
  }

/**
	* 最初貸款金額為0<br>
	* 
  *
  * @param r4 最初貸款金額為0
	*/
  public void setR4(String r4) {
    this.r4 = r4;
  }

/**
	* 最初貸款金額大於核准額度<br>
	* 
	* @return String
	*/
  public String getR5() {
    return this.r5 == null ? "" : this.r5;
  }

/**
	* 最初貸款金額大於核准額度<br>
	* 
  *
  * @param r5 最初貸款金額大於核准額度
	*/
  public void setR5(String r5) {
    this.r5 = r5;
  }

/**
	* 最初貸款金額小於放款餘額<br>
	* 
	* @return String
	*/
  public String getR6() {
    return this.r6 == null ? "" : this.r6;
  }

/**
	* 最初貸款金額小於放款餘額<br>
	* 
  *
  * @param r6 最初貸款金額小於放款餘額
	*/
  public void setR6(String r6) {
    this.r6 = r6;
  }

/**
	* 貸款起日空白<br>
	* 
	* @return String
	*/
  public String getR7() {
    return this.r7 == null ? "" : this.r7;
  }

/**
	* 貸款起日空白<br>
	* 
  *
  * @param r7 貸款起日空白
	*/
  public void setR7(String r7) {
    this.r7 = r7;
  }

/**
	* 貸款迄日空白<br>
	* 
	* @return String
	*/
  public String getR8() {
    return this.r8 == null ? "" : this.r8;
  }

/**
	* 貸款迄日空白<br>
	* 
  *
  * @param r8 貸款迄日空白
	*/
  public void setR8(String r8) {
    this.r8 = r8;
  }

/**
	* 繳息所屬年月空白<br>
	* 
	* @return String
	*/
  public String getR10() {
    return this.r10 == null ? "" : this.r10;
  }

/**
	* 繳息所屬年月空白<br>
	* 
  *
  * @param r10 繳息所屬年月空白
	*/
  public void setR10(String r10) {
    this.r10 = r10;
  }

/**
	* 繳息金額為0<br>
	* 
	* @return String
	*/
  public String getR11() {
    return this.r11 == null ? "" : this.r11;
  }

/**
	* 繳息金額為0<br>
	* 
  *
  * @param r11 繳息金額為0
	*/
  public void setR11(String r11) {
    this.r11 = r11;
  }

/**
	* 科子細目代號暨說明空白<br>
	* 
	* @return String
	*/
  public String getR12() {
    return this.r12 == null ? "" : this.r12;
  }

/**
	* 科子細目代號暨說明空白<br>
	* 
  *
  * @param r12 科子細目代號暨說明空白
	*/
  public void setR12(String r12) {
    this.r12 = r12;
  }

/**
	* 一額度一撥款<br>
	* 
	* @return String
	*/
  public String getC1() {
    return this.c1 == null ? "" : this.c1;
  }

/**
	* 一額度一撥款<br>
	* 
  *
  * @param c1 一額度一撥款
	*/
  public void setC1(String c1) {
    this.c1 = c1;
  }

/**
	* 一額度多撥款<br>
	* 
	* @return String
	*/
  public String getC2() {
    return this.c2 == null ? "" : this.c2;
  }

/**
	* 一額度多撥款<br>
	* 
  *
  * @param c2 一額度多撥款
	*/
  public void setC2(String c2) {
    this.c2 = c2;
  }

/**
	* 多額度多撥款<br>
	* 
	* @return String
	*/
  public String getC3() {
    return this.c3 == null ? "" : this.c3;
  }

/**
	* 多額度多撥款<br>
	* 
  *
  * @param c3 多額度多撥款
	*/
  public void setC3(String c3) {
    this.c3 = c3;
  }

/**
	* 借新還舊件<br>
	* 
	* @return String
	*/
  public String getC4() {
    return this.c4 == null ? "" : this.c4;
  }

/**
	* 借新還舊件<br>
	* 
  *
  * @param c4 借新還舊件
	*/
  public void setC4(String c4) {
    this.c4 = c4;
  }

/**
	* 清償件<br>
	* 
	* @return String
	*/
  public String getC5() {
    return this.c5 == null ? "" : this.c5;
  }

/**
	* 清償件<br>
	* 
  *
  * @param c5 清償件
	*/
  public void setC5(String c5) {
    this.c5 = c5;
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
    return "YearlyHouseLoanIntCheck [yearlyHouseLoanIntCheckId=" + yearlyHouseLoanIntCheckId + ", r1=" + r1 + ", r2=" + r2
           + ", r3=" + r3 + ", r4=" + r4 + ", r5=" + r5 + ", r6=" + r6 + ", r7=" + r7 + ", r8=" + r8
           + ", r10=" + r10 + ", r11=" + r11 + ", r12=" + r12 + ", c1=" + c1 + ", c2=" + c2 + ", c3=" + c3
           + ", c4=" + c4 + ", c5=" + c5 + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo
           + "]";
  }
}
