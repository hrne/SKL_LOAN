package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * LoanIfrsFp IFRS9欄位清單6<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class LoanIfrsFpId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 7474681417139038170L;

// 年月份
  @Column(name = "`DataYM`")
  private int dataYM = 0;

  // 戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 協議編號
  /* 依戶號，按建立順序(會計日期)編列序號 */
  @Column(name = "`AgreeNo`")
  private int agreeNo = 0;

  // 協議前後
  /* B=協議前; A=協議後; */
  @Column(name = "`AgreeFg`", length = 1)
  private String agreeFg = " ";

  // 額度編號
  @Column(name = "`FacmNo`")
  private int facmNo = 0;

  // 撥款序號
  @Column(name = "`BormNo`")
  private int bormNo = 0;

  public LoanIfrsFpId() {
  }

  public LoanIfrsFpId(int dataYM, int custNo, int agreeNo, String agreeFg, int facmNo, int bormNo) {
    this.dataYM = dataYM;
    this.custNo = custNo;
    this.agreeNo = agreeNo;
    this.agreeFg = agreeFg;
    this.facmNo = facmNo;
    this.bormNo = bormNo;
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
	* 協議編號<br>
	* 依戶號，按建立順序(會計日期)編列序號
	* @return Integer
	*/
  public int getAgreeNo() {
    return this.agreeNo;
  }

/**
	* 協議編號<br>
	* 依戶號，按建立順序(會計日期)編列序號
  *
  * @param agreeNo 協議編號
	*/
  public void setAgreeNo(int agreeNo) {
    this.agreeNo = agreeNo;
  }

/**
	* 協議前後<br>
	* B=協議前; 
A=協議後;
	* @return String
	*/
  public String getAgreeFg() {
    return this.agreeFg == null ? "" : this.agreeFg;
  }

/**
	* 協議前後<br>
	* B=協議前; 
A=協議後;
  *
  * @param agreeFg 協議前後
	*/
  public void setAgreeFg(String agreeFg) {
    this.agreeFg = agreeFg;
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


  @Override
  public int hashCode() {
    return Objects.hash(dataYM, custNo, agreeNo, agreeFg, facmNo, bormNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    LoanIfrsFpId loanIfrsFpId = (LoanIfrsFpId) obj;
    return dataYM == loanIfrsFpId.dataYM && custNo == loanIfrsFpId.custNo && agreeNo == loanIfrsFpId.agreeNo && agreeFg.equals(loanIfrsFpId.agreeFg) && facmNo == loanIfrsFpId.facmNo && bormNo == loanIfrsFpId.bormNo;
  }

  @Override
  public String toString() {
    return "LoanIfrsFpId [dataYM=" + dataYM + ", custNo=" + custNo + ", agreeNo=" + agreeNo + ", agreeFg=" + agreeFg + ", facmNo=" + facmNo + ", bormNo=" + bormNo + "]";
  }
}
