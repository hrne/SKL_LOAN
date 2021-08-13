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
 * MlaundryRecord 疑似洗錢交易訪談記錄檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`MlaundryRecord`")
public class MlaundryRecord implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 250198364989177373L;

@EmbeddedId
  private MlaundryRecordId mlaundryRecordId;

  // 訪談日期
  @Column(name = "`RecordDate`", insertable = false, updatable = false)
  private int recordDate = 0;

  // 戶號
  @Column(name = "`CustNo`", insertable = false, updatable = false)
  private int custNo = 0;

  // 額度編號
  @Column(name = "`FacmNo`", insertable = false, updatable = false)
  private int facmNo = 0;

  // 撥款序號
  @Column(name = "`BormNo`", insertable = false, updatable = false)
  private int bormNo = 0;

  // 預定還款日期
  @Column(name = "`RepayDate`")
  private int repayDate = 0;

  // 實際還款日期
  @Column(name = "`ActualRepayDate`")
  private int actualRepayDate = 0;

  // 還款金額
  @Column(name = "`RepayAmt`")
  private BigDecimal repayAmt = new BigDecimal("0");

  // 職業別
  @Column(name = "`Career`", length = 20)
  private String career;

  // 年收入(萬)
  @Column(name = "`Income`", length = 30)
  private String income;

  // 還款來源
  /* 01:他行代償02:家人代償03:內部代償04:房屋出售05:土地出售06:投資獲利07:保險滿期金08:退休金09:存款10:不願告知11:其他 */
  @Column(name = "`RepaySource`")
  private int repaySource = 0;

  // 代償銀行
  @Column(name = "`RepayBank`", length = 10)
  private String repayBank;

  // 其他說明
  @Column(name = "`Description`", length = 60)
  private String description;

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


  public MlaundryRecordId getMlaundryRecordId() {
    return this.mlaundryRecordId;
  }

  public void setMlaundryRecordId(MlaundryRecordId mlaundryRecordId) {
    this.mlaundryRecordId = mlaundryRecordId;
  }

/**
	* 訪談日期<br>
	* 
	* @return Integer
	*/
  public int getRecordDate() {
    return StaticTool.bcToRoc(this.recordDate);
  }

/**
	* 訪談日期<br>
	* 
  *
  * @param recordDate 訪談日期
  * @throws LogicException when Date Is Warn	*/
  public void setRecordDate(int recordDate) throws LogicException {
    this.recordDate = StaticTool.rocToBc(recordDate);
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
	* 撥款序號<br>
	* 
	* @return Integer
	*/
  public int getBormNo() {
    return this.bormNo;
  }

/**
	* 撥款序號<br>
	* 
  *
  * @param bormNo 撥款序號
	*/
  public void setBormNo(int bormNo) {
    this.bormNo = bormNo;
  }

/**
	* 預定還款日期<br>
	* 
	* @return Integer
	*/
  public int getRepayDate() {
    return StaticTool.bcToRoc(this.repayDate);
  }

/**
	* 預定還款日期<br>
	* 
  *
  * @param repayDate 預定還款日期
  * @throws LogicException when Date Is Warn	*/
  public void setRepayDate(int repayDate) throws LogicException {
    this.repayDate = StaticTool.rocToBc(repayDate);
  }

/**
	* 實際還款日期<br>
	* 
	* @return Integer
	*/
  public int getActualRepayDate() {
    return StaticTool.bcToRoc(this.actualRepayDate);
  }

/**
	* 實際還款日期<br>
	* 
  *
  * @param actualRepayDate 實際還款日期
  * @throws LogicException when Date Is Warn	*/
  public void setActualRepayDate(int actualRepayDate) throws LogicException {
    this.actualRepayDate = StaticTool.rocToBc(actualRepayDate);
  }

/**
	* 還款金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getRepayAmt() {
    return this.repayAmt;
  }

/**
	* 還款金額<br>
	* 
  *
  * @param repayAmt 還款金額
	*/
  public void setRepayAmt(BigDecimal repayAmt) {
    this.repayAmt = repayAmt;
  }

/**
	* 職業別<br>
	* 
	* @return String
	*/
  public String getCareer() {
    return this.career == null ? "" : this.career;
  }

/**
	* 職業別<br>
	* 
  *
  * @param career 職業別
	*/
  public void setCareer(String career) {
    this.career = career;
  }

/**
	* 年收入(萬)<br>
	* 
	* @return String
	*/
  public String getIncome() {
    return this.income == null ? "" : this.income;
  }

/**
	* 年收入(萬)<br>
	* 
  *
  * @param income 年收入(萬)
	*/
  public void setIncome(String income) {
    this.income = income;
  }

/**
	* 還款來源<br>
	* 01:他行代償
02:家人代償
03:內部代償
04:房屋出售
05:土地出售
06:投資獲利
07:保險滿期金
08:退休金
09:存款
10:不願告知
11:其他
	* @return Integer
	*/
  public int getRepaySource() {
    return this.repaySource;
  }

/**
	* 還款來源<br>
	* 01:他行代償
02:家人代償
03:內部代償
04:房屋出售
05:土地出售
06:投資獲利
07:保險滿期金
08:退休金
09:存款
10:不願告知
11:其他
  *
  * @param repaySource 還款來源
	*/
  public void setRepaySource(int repaySource) {
    this.repaySource = repaySource;
  }

/**
	* 代償銀行<br>
	* 
	* @return String
	*/
  public String getRepayBank() {
    return this.repayBank == null ? "" : this.repayBank;
  }

/**
	* 代償銀行<br>
	* 
  *
  * @param repayBank 代償銀行
	*/
  public void setRepayBank(String repayBank) {
    this.repayBank = repayBank;
  }

/**
	* 其他說明<br>
	* 
	* @return String
	*/
  public String getDescription() {
    return this.description == null ? "" : this.description;
  }

/**
	* 其他說明<br>
	* 
  *
  * @param description 其他說明
	*/
  public void setDescription(String description) {
    this.description = description;
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
    return "MlaundryRecord [mlaundryRecordId=" + mlaundryRecordId + ", repayDate=" + repayDate + ", actualRepayDate=" + actualRepayDate
           + ", repayAmt=" + repayAmt + ", career=" + career + ", income=" + income + ", repaySource=" + repaySource + ", repayBank=" + repayBank + ", description=" + description
           + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
