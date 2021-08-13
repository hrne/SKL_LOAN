package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * CustRelDetail 客戶關係人/關係企業資料維護明細檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class CustRelDetailId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -4542061575512554258L;

// 客戶識別碼
  @Column(name = "`CustRelMainUKey`", length = 32)
  private String custRelMainUKey = " ";

  // 關聯戶識別碼
  @Column(name = "`Ukey`", length = 32)
  private String ukey = " ";

  public CustRelDetailId() {
  }

  public CustRelDetailId(String custRelMainUKey, String ukey) {
    this.custRelMainUKey = custRelMainUKey;
    this.ukey = ukey;
  }

/**
	* 客戶識別碼<br>
	* 
	* @return String
	*/
  public String getCustRelMainUKey() {
    return this.custRelMainUKey == null ? "" : this.custRelMainUKey;
  }

/**
	* 客戶識別碼<br>
	* 
  *
  * @param custRelMainUKey 客戶識別碼
	*/
  public void setCustRelMainUKey(String custRelMainUKey) {
    this.custRelMainUKey = custRelMainUKey;
  }

/**
	* 關聯戶識別碼<br>
	* 
	* @return String
	*/
  public String getUkey() {
    return this.ukey == null ? "" : this.ukey;
  }

/**
	* 關聯戶識別碼<br>
	* 
  *
  * @param ukey 關聯戶識別碼
	*/
  public void setUkey(String ukey) {
    this.ukey = ukey;
  }


  @Override
  public int hashCode() {
    return Objects.hash(custRelMainUKey, ukey);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    CustRelDetailId custRelDetailId = (CustRelDetailId) obj;
    return custRelMainUKey.equals(custRelDetailId.custRelMainUKey) && ukey.equals(custRelDetailId.ukey);
  }

  @Override
  public String toString() {
    return "CustRelDetailId [custRelMainUKey=" + custRelMainUKey + ", ukey=" + ukey + "]";
  }
}
