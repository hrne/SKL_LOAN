package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * MonthlyLM032 逾期案件滾動率明細月報工作檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class MonthlyLM032Id implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -9122478022239506750L;

// 前期資料年月
  @Column(name = "`ADTYMT`")
  private int aDTYMT = 0;

  // 前期戶號
  @Column(name = "`LMSACN`")
  private int lMSACN = 0;

  // 前期額度號碼
  @Column(name = "`LMSAPN`")
  private int lMSAPN = 0;

  public MonthlyLM032Id() {
  }

  public MonthlyLM032Id(int aDTYMT, int lMSACN, int lMSAPN) {
    this.aDTYMT = aDTYMT;
    this.lMSACN = lMSACN;
    this.lMSAPN = lMSAPN;
  }

/**
	* 前期資料年月<br>
	* 
	* @return Integer
	*/
  public int getADTYMT() {
    return this.aDTYMT;
  }

/**
	* 前期資料年月<br>
	* 
  *
  * @param aDTYMT 前期資料年月
	*/
  public void setADTYMT(int aDTYMT) {
    this.aDTYMT = aDTYMT;
  }

/**
	* 前期戶號<br>
	* 
	* @return Integer
	*/
  public int getLMSACN() {
    return this.lMSACN;
  }

/**
	* 前期戶號<br>
	* 
  *
  * @param lMSACN 前期戶號
	*/
  public void setLMSACN(int lMSACN) {
    this.lMSACN = lMSACN;
  }

/**
	* 前期額度號碼<br>
	* 
	* @return Integer
	*/
  public int getLMSAPN() {
    return this.lMSAPN;
  }

/**
	* 前期額度號碼<br>
	* 
  *
  * @param lMSAPN 前期額度號碼
	*/
  public void setLMSAPN(int lMSAPN) {
    this.lMSAPN = lMSAPN;
  }


  @Override
  public int hashCode() {
    return Objects.hash(aDTYMT, lMSACN, lMSAPN);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    MonthlyLM032Id monthlyLM032Id = (MonthlyLM032Id) obj;
    return aDTYMT == monthlyLM032Id.aDTYMT && lMSACN == monthlyLM032Id.lMSACN && lMSAPN == monthlyLM032Id.lMSAPN;
  }

  @Override
  public String toString() {
    return "MonthlyLM032Id [aDTYMT=" + aDTYMT + ", lMSACN=" + lMSACN + ", lMSAPN=" + lMSAPN + "]";
  }
}
