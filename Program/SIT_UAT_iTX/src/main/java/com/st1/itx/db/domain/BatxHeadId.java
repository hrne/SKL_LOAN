package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * BatxHead 整批入帳總數檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class BatxHeadId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -4358259455511357275L;

// 會計日期
  @Column(name = "`AcDate`")
  private int acDate = 0;

  // 批號
  /* BATX01、02… */
  @Column(name = "`BatchNo`", length = 6)
  private String batchNo = " ";

  public BatxHeadId() {
  }

  public BatxHeadId(int acDate, String batchNo) {
    this.acDate = acDate;
    this.batchNo = batchNo;
  }

/**
	* 會計日期<br>
	* 
	* @return Integer
	*/
  public int getAcDate() {
    return this.acDate;
  }

/**
	* 會計日期<br>
	* 
  *
  * @param acDate 會計日期
	*/
  public void setAcDate(int acDate) {
    this.acDate = acDate;
  }

/**
	* 批號<br>
	* BATX01、02…
	* @return String
	*/
  public String getBatchNo() {
    return this.batchNo == null ? "" : this.batchNo;
  }

/**
	* 批號<br>
	* BATX01、02…
  *
  * @param batchNo 批號
	*/
  public void setBatchNo(String batchNo) {
    this.batchNo = batchNo;
  }


  @Override
  public int hashCode() {
    return Objects.hash(acDate, batchNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    BatxHeadId batxHeadId = (BatxHeadId) obj;
    return acDate == batxHeadId.acDate && batchNo.equals(batxHeadId.batchNo);
  }

  @Override
  public String toString() {
    return "BatxHeadId [acDate=" + acDate + ", batchNo=" + batchNo + "]";
  }
}
