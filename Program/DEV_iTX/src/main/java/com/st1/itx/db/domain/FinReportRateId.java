package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * FinReportRate 客戶財務報表.財務比率表<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class FinReportRateId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -6417799459829955185L;

// 客戶識別碼
  @Column(name = "`CustUKey`", length = 32)
  private String custUKey = " ";

  // 識別碼
  @Column(name = "`UKey`", length = 32)
  private String uKey = " ";

  public FinReportRateId() {
  }

  public FinReportRateId(String custUKey, String uKey) {
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
    FinReportRateId finReportRateId = (FinReportRateId) obj;
    return custUKey.equals(finReportRateId.custUKey) && uKey.equals(finReportRateId.uKey);
  }

  @Override
  public String toString() {
    return "FinReportRateId [custUKey=" + custUKey + ", uKey=" + uKey + "]";
  }
}
