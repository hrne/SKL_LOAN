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
 * LoanIfrsCp IFRS9欄位清單3<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`LoanIfrsCp`")
public class LoanIfrsCp implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 3625763078018612856L;

@EmbeddedId
  private LoanIfrsCpId loanIfrsCpId;

  // 年月份
  @Column(name = "`DataYM`", insertable = false, updatable = false)
  private int dataYM = 0;

  // 戶號
  @Column(name = "`CustNo`", insertable = false, updatable = false)
  private int custNo = 0;

  // 借款人ID / 統編
  @Column(name = "`CustId`", length = 10)
  private String custId;

  // 額度編號
  @Column(name = "`FacmNo`", insertable = false, updatable = false)
  private int facmNo = 0;

  // 撥款序號
  @Column(name = "`BormNo`", insertable = false, updatable = false)
  private int bormNo = 0;

  // 約定還款方式
  /* 1=按期繳息(到期還本)；2=平均攤還本息；3=平均攤還本金；4=到期繳息還本 */
  @Column(name = "`AmortizedCode`", length = 1)
  private String amortizedCode;

  // 繳息週期
  /* 若為到期繳息，則填入0；若按月還本，則填入1；季繳，3；半年，6；年繳,12。 */
  @Column(name = "`PayIntFreq`")
  private int payIntFreq = 0;

  // 還本週期
  /* 若為到期還本，則填入0；若按月還本，則填入1；季繳，3；半年，6；年繳,12。 */
  @Column(name = "`RepayFreq`")
  private int repayFreq = 0;

  // 生效日期
  /* 繳息迄日(未繳:撥款日)，YYYYMMDD 例：20100108 */
  @Column(name = "`EffectDate`", insertable = false, updatable = false)
  private int effectDate = 0;

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


  public LoanIfrsCpId getLoanIfrsCpId() {
    return this.loanIfrsCpId;
  }

  public void setLoanIfrsCpId(LoanIfrsCpId loanIfrsCpId) {
    this.loanIfrsCpId = loanIfrsCpId;
  }

/**
	* 年月份<br>
	* 
	* @return Integer
	*/
  public int getDataYM() {
    return this.dataYM;
  }

/**
	* 年月份<br>
	* 
  *
  * @param dataYM 年月份
	*/
  public void setDataYM(int dataYM) {
    this.dataYM = dataYM;
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
	* 借款人ID / 統編<br>
	* 
	* @return String
	*/
  public String getCustId() {
    return this.custId == null ? "" : this.custId;
  }

/**
	* 借款人ID / 統編<br>
	* 
  *
  * @param custId 借款人ID / 統編
	*/
  public void setCustId(String custId) {
    this.custId = custId;
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
	* 約定還款方式<br>
	* 1=按期繳息(到期還本)；2=平均攤還本息；3=平均攤還本金；4=到期繳息還本
	* @return String
	*/
  public String getAmortizedCode() {
    return this.amortizedCode == null ? "" : this.amortizedCode;
  }

/**
	* 約定還款方式<br>
	* 1=按期繳息(到期還本)；2=平均攤還本息；3=平均攤還本金；4=到期繳息還本
  *
  * @param amortizedCode 約定還款方式
	*/
  public void setAmortizedCode(String amortizedCode) {
    this.amortizedCode = amortizedCode;
  }

/**
	* 繳息週期<br>
	* 若為到期繳息，則填入0；若按月還本，則填入1；季繳，3；半年，6；年繳,12。
	* @return Integer
	*/
  public int getPayIntFreq() {
    return this.payIntFreq;
  }

/**
	* 繳息週期<br>
	* 若為到期繳息，則填入0；若按月還本，則填入1；季繳，3；半年，6；年繳,12。
  *
  * @param payIntFreq 繳息週期
	*/
  public void setPayIntFreq(int payIntFreq) {
    this.payIntFreq = payIntFreq;
  }

/**
	* 還本週期<br>
	* 若為到期還本，則填入0；若按月還本，則填入1；季繳，3；半年，6；年繳,12。
	* @return Integer
	*/
  public int getRepayFreq() {
    return this.repayFreq;
  }

/**
	* 還本週期<br>
	* 若為到期還本，則填入0；若按月還本，則填入1；季繳，3；半年，6；年繳,12。
  *
  * @param repayFreq 還本週期
	*/
  public void setRepayFreq(int repayFreq) {
    this.repayFreq = repayFreq;
  }

/**
	* 生效日期<br>
	* 繳息迄日(未繳:撥款日)，YYYYMMDD 例：20100108
	* @return Integer
	*/
  public int getEffectDate() {
    return this.effectDate;
  }

/**
	* 生效日期<br>
	* 繳息迄日(未繳:撥款日)，YYYYMMDD 例：20100108
  *
  * @param effectDate 生效日期
	*/
  public void setEffectDate(int effectDate) {
    this.effectDate = effectDate;
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
    return "LoanIfrsCp [loanIfrsCpId=" + loanIfrsCpId + ", custId=" + custId + ", amortizedCode=" + amortizedCode
           + ", payIntFreq=" + payIntFreq + ", repayFreq=" + repayFreq + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
           + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
