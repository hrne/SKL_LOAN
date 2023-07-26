package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * CdCashFlow 現金流量預估資料檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class CdCashFlowId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -6162501782129646749L;

// 單位別
  @Column(name = "`BranchNo`", length = 4)
  private String branchNo = " ";

  // 年月份
  @Column(name = "`DataYearMonth`")
  private int dataYearMonth = 0;

  // 旬別
  /* 1:上旬2:中旬3:下旬 */
  @Column(name = "`TenDayPeriods`")
  private int tenDayPeriods = 0;

  public CdCashFlowId() {
  }

  public CdCashFlowId(String branchNo, int dataYearMonth, int tenDayPeriods) {
    this.branchNo = branchNo;
    this.dataYearMonth = dataYearMonth;
    this.tenDayPeriods = tenDayPeriods;
  }

/**
	* 單位別<br>
	* 
	* @return String
	*/
  public String getBranchNo() {
    return this.branchNo == null ? "" : this.branchNo;
  }

/**
	* 單位別<br>
	* 
  *
  * @param branchNo 單位別
	*/
  public void setBranchNo(String branchNo) {
    this.branchNo = branchNo;
  }

/**
	* 年月份<br>
	* 
	* @return Integer
	*/
  public int getDataYearMonth() {
    return this.dataYearMonth;
  }

/**
	* 年月份<br>
	* 
  *
  * @param dataYearMonth 年月份
	*/
  public void setDataYearMonth(int dataYearMonth) {
    this.dataYearMonth = dataYearMonth;
  }

/**
	* 旬別<br>
	* 1:上旬
2:中旬
3:下旬
	* @return Integer
	*/
  public int getTenDayPeriods() {
    return this.tenDayPeriods;
  }

/**
	* 旬別<br>
	* 1:上旬
2:中旬
3:下旬
  *
  * @param tenDayPeriods 旬別
	*/
  public void setTenDayPeriods(int tenDayPeriods) {
    this.tenDayPeriods = tenDayPeriods;
  }


  @Override
  public int hashCode() {
    return Objects.hash(branchNo, dataYearMonth, tenDayPeriods);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    CdCashFlowId cdCashFlowId = (CdCashFlowId) obj;
    return branchNo.equals(cdCashFlowId.branchNo) && dataYearMonth == cdCashFlowId.dataYearMonth && tenDayPeriods == cdCashFlowId.tenDayPeriods;
  }

  @Override
  public String toString() {
    return "CdCashFlowId [branchNo=" + branchNo + ", dataYearMonth=" + dataYearMonth + ", tenDayPeriods=" + tenDayPeriods + "]";
  }
}
