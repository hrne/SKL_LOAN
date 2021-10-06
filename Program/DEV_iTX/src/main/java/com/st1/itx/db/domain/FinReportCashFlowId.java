package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * FinReportCashFlow 客戶財務報表.現金流量表<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class FinReportCashFlowId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 2883708170827495727L;

// 客戶識別碼
  @Column(name = "`CustUKey`", length = 32)
  private String custUKey = " ";

  // 識別碼
  @Column(name = "`UKey`", length = 32)
  private String uKey = " ";

  public FinReportCashFlowId() {
  }

  public FinReportCashFlowId(String custUKey, String uKey) {
    this.custUKey = custUKey;
    this.uKey = uKey;
  }

/**
	* 客戶識別碼<br>
	* 
	* @return String
	*/
  public String getCustUKey() {
    return this.custUKey == null ? "" : this.custUKey;
  }

/**
	* 客戶識別碼<br>
	* 
  *
  * @param custUKey 客戶識別碼
	*/
  public void setCustUKey(String custUKey) {
    this.custUKey = custUKey;
  }

/**
	* 識別碼<br>
	* 
	* @return String
	*/
  public String getUKey() {
    return this.uKey == null ? "" : this.uKey;
  }

/**
	* 識別碼<br>
	* 
  *
  * @param uKey 識別碼
	*/
  public void setUKey(String uKey) {
    this.uKey = uKey;
  }


  @Override
  public int hashCode() {
    return Objects.hash(custUKey, uKey);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    FinReportCashFlowId finReportCashFlowId = (FinReportCashFlowId) obj;
    return custUKey.equals(finReportCashFlowId.custUKey) && uKey.equals(finReportCashFlowId.uKey);
  }

  @Override
  public String toString() {
    return "FinReportCashFlowId [custUKey=" + custUKey + ", uKey=" + uKey + "]";
  }
}
