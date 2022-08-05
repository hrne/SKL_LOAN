package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * JcicMonthlyLoanData 聯徵放款月報資料檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicMonthlyLoanDataId implements Serializable {


  // 資料年月
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

  public JcicMonthlyLoanDataId() {
  }

  public JcicMonthlyLoanDataId(int dataYM, int custNo, int facmNo, int bormNo) {
    this.dataYM = dataYM;
    this.custNo = custNo;
    this.facmNo = facmNo;
    this.bormNo = bormNo;
  }

/**
	* 資料年月<br>
	* 
	* @return Integer
	*/
  public int getDataYM() {
    return this.dataYM;
  }

/**
	* 資料年月<br>
	* 
  *
  * @param dataYM 資料年月
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


  @Override
  public int hashCode() {
    return Objects.hash(dataYM, custNo, facmNo, bormNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    JcicMonthlyLoanDataId jcicMonthlyLoanDataId = (JcicMonthlyLoanDataId) obj;
    return dataYM == jcicMonthlyLoanDataId.dataYM && custNo == jcicMonthlyLoanDataId.custNo && facmNo == jcicMonthlyLoanDataId.facmNo && bormNo == jcicMonthlyLoanDataId.bormNo;
  }

  @Override
  public String toString() {
    return "JcicMonthlyLoanDataId [dataYM=" + dataYM + ", custNo=" + custNo + ", facmNo=" + facmNo + ", bormNo=" + bormNo + "]";
  }
}
