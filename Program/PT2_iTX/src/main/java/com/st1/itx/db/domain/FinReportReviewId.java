package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * FinReportReview 客戶財務報表.覆審比率表<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class FinReportReviewId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -5866899562166106293L;

// 客戶識別碼
  @Column(name = "`CustUKey`", length = 32)
  private String custUKey = " ";

  // 識別碼
  @Column(name = "`Ukey`", length = 32)
  private String ukey = " ";

  public FinReportReviewId() {
  }

  public FinReportReviewId(String custUKey, String ukey) {
    this.custUKey = custUKey;
    this.ukey = ukey;
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
  public String getUkey() {
    return this.ukey == null ? "" : this.ukey;
  }

/**
	* 識別碼<br>
	* 
  *
  * @param ukey 識別碼
	*/
  public void setUkey(String ukey) {
    this.ukey = ukey;
  }


  @Override
  public int hashCode() {
    return Objects.hash(custUKey, ukey);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    FinReportReviewId finReportReviewId = (FinReportReviewId) obj;
    return custUKey.equals(finReportReviewId.custUKey) && ukey.equals(finReportReviewId.ukey);
  }

  @Override
  public String toString() {
    return "FinReportReviewId [custUKey=" + custUKey + ", ukey=" + ukey + "]";
  }
}
