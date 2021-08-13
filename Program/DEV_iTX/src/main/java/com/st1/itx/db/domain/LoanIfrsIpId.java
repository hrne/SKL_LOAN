package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * LoanIfrsIp IFRS9欄位清單9<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class LoanIfrsIpId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -3901365702791432943L;

// 年月份
  @Column(name = "`DataYM`")
  private int dataYM = 0;

  // 戶號
  /* 產檔時顯示空白 */
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 額度編號
  @Column(name = "`FacmNo`")
  private int facmNo = 0;

  public LoanIfrsIpId() {
  }

  public LoanIfrsIpId(int dataYM, int custNo, int facmNo) {
    this.dataYM = dataYM;
    this.custNo = custNo;
    this.facmNo = facmNo;
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
	* 產檔時顯示空白
	* @return Integer
	*/
  public int getCustNo() {
    return this.custNo;
  }

/**
	* 戶號<br>
	* 產檔時顯示空白
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


  @Override
  public int hashCode() {
    return Objects.hash(dataYM, custNo, facmNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    LoanIfrsIpId loanIfrsIpId = (LoanIfrsIpId) obj;
    return dataYM == loanIfrsIpId.dataYM && custNo == loanIfrsIpId.custNo && facmNo == loanIfrsIpId.facmNo;
  }

  @Override
  public String toString() {
    return "LoanIfrsIpId [dataYM=" + dataYM + ", custNo=" + custNo + ", facmNo=" + facmNo + "]";
  }
}
