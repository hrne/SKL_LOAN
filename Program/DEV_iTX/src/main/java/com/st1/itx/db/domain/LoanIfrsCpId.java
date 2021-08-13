package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * LoanIfrsCp IFRS9資料欄位清單3<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class LoanIfrsCpId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 975638088837984738L;

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

  // 生效日期
  /* 繳息迄日(未繳:撥款日)，YYYYMMDD 例：20100108 */
  @Column(name = "`EffectDate`")
  private int effectDate = 0;

  public LoanIfrsCpId() {
  }

  public LoanIfrsCpId(int dataYM, int custNo, int facmNo, int bormNo, int effectDate) {
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
    LoanIfrsCpId loanIfrsCpId = (LoanIfrsCpId) obj;
    return dataYM == loanIfrsCpId.dataYM && custNo == loanIfrsCpId.custNo && facmNo == loanIfrsCpId.facmNo && bormNo == loanIfrsCpId.bormNo && effectDate == loanIfrsCpId.effectDate;
  }

  @Override
  public String toString() {
    return "LoanIfrsCpId [dataYM=" + dataYM + ", custNo=" + custNo + ", facmNo=" + facmNo + ", bormNo=" + bormNo + ", effectDate=" + effectDate + "]";
  }
}
