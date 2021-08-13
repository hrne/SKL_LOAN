package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * LoanIfrsBp IFRS9欄位清單B檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class LoanIfrsBpId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -2747037835121429403L;

// 年月份
  @Column(name = "`DataYM`")
  private int dataYM = 0;

  // 戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 額度編號
  @Column(name = "`FacmNo`")
  private int facmNo = 0;

  // 撥款序號
  @Column(name = "`BormNo`")
  private int bormNo = 0;

  // 利率欄位生效日
  /* YYYYMMDD 例：20100108 */
  @Column(name = "`EffectDate`")
  private int effectDate = 0;

  public LoanIfrsBpId() {
  }

  public LoanIfrsBpId(int dataYM, int custNo, int facmNo, int bormNo, int effectDate) {
    this.dataYM = dataYM;
    this.custNo = custNo;
    this.facmNo = facmNo;
    this.bormNo = bormNo;
    this.effectDate = effectDate;
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
	* 利率欄位生效日<br>
	* YYYYMMDD 例：20100108
	* @return Integer
	*/
  public int getEffectDate() {
    return this.effectDate;
  }

/**
	* 利率欄位生效日<br>
	* YYYYMMDD 例：20100108
  *
  * @param effectDate 利率欄位生效日
	*/
  public void setEffectDate(int effectDate) {
    this.effectDate = effectDate;
  }


  @Override
  public int hashCode() {
    return Objects.hash(dataYM, custNo, facmNo, bormNo, effectDate);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    LoanIfrsBpId loanIfrsBpId = (LoanIfrsBpId) obj;
    return dataYM == loanIfrsBpId.dataYM && custNo == loanIfrsBpId.custNo && facmNo == loanIfrsBpId.facmNo && bormNo == loanIfrsBpId.bormNo && effectDate == loanIfrsBpId.effectDate;
  }

  @Override
  public String toString() {
    return "LoanIfrsBpId [dataYM=" + dataYM + ", custNo=" + custNo + ", facmNo=" + facmNo + ", bormNo=" + bormNo + ", effectDate=" + effectDate + "]";
  }
}
