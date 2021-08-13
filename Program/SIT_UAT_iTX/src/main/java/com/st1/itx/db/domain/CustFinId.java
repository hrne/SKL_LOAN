package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * CustFin 公司戶財務狀況檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class CustFinId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -7489686728321888944L;

// 客戶識別碼
  @Column(name = "`CustUKey`", length = 32)
  private String custUKey = " ";

  // 年度
  @Column(name = "`DataYear`")
  private int dataYear = 0;

  public CustFinId() {
  }

  public CustFinId(String custUKey, int dataYear) {
    this.custUKey = custUKey;
    this.dataYear = dataYear;
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
	* 年度<br>
	* 
	* @return Integer
	*/
  public int getDataYear() {
    return this.dataYear;
  }

/**
	* 年度<br>
	* 
  *
  * @param dataYear 年度
	*/
  public void setDataYear(int dataYear) {
    this.dataYear = dataYear;
  }


  @Override
  public int hashCode() {
    return Objects.hash(custUKey, dataYear);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    CustFinId custFinId = (CustFinId) obj;
    return custUKey.equals(custFinId.custUKey) && dataYear == custFinId.dataYear;
  }

  @Override
  public String toString() {
    return "CustFinId [custUKey=" + custUKey + ", dataYear=" + dataYear + "]";
  }
}
