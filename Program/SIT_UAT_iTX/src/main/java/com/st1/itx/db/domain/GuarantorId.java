package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Guarantor 保證人檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class GuarantorId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -6782447489145964431L;

// 核准號碼
  @Column(name = "`ApproveNo`")
  private int approveNo = 0;

  // 保證人客戶識別碼
  @Column(name = "`GuaUKey`", length = 32)
  private String guaUKey = " ";

  public GuarantorId() {
  }

  public GuarantorId(int approveNo, String guaUKey) {
    this.approveNo = approveNo;
    this.guaUKey = guaUKey;
  }

/**
	* 核准號碼<br>
	* 
	* @return Integer
	*/
  public int getApproveNo() {
    return this.approveNo;
  }

/**
	* 核准號碼<br>
	* 
  *
  * @param approveNo 核准號碼
	*/
  public void setApproveNo(int approveNo) {
    this.approveNo = approveNo;
  }

/**
	* 保證人客戶識別碼<br>
	* 
	* @return String
	*/
  public String getGuaUKey() {
    return this.guaUKey == null ? "" : this.guaUKey;
  }

/**
	* 保證人客戶識別碼<br>
	* 
  *
  * @param guaUKey 保證人客戶識別碼
	*/
  public void setGuaUKey(String guaUKey) {
    this.guaUKey = guaUKey;
  }


  @Override
  public int hashCode() {
    return Objects.hash(approveNo, guaUKey);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    GuarantorId guarantorId = (GuarantorId) obj;
    return approveNo == guarantorId.approveNo && guaUKey.equals(guarantorId.guaUKey);
  }

  @Override
  public String toString() {
    return "GuarantorId [approveNo=" + approveNo + ", guaUKey=" + guaUKey + "]";
  }
}
