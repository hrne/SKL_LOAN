package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * MonthlyLM052AssetClass LM052資產分類表<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class MonthlyLM052AssetClassId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 6060313610339822491L;

// 資料年月
  @Column(name = "`YearMonth`")
  private int yearMonth = 0;

  // 資產五分類
  /* 一類：11、12二類：21、22、23三類：3四類：4五類：5折溢價與催收：6應收利息提列：7 */
  @Column(name = "`AssetClassNo`", length = 2)
  private String assetClassNo = " ";

  // 區隔帳冊
  /* 一般：00A利變：201 */
  @Column(name = "`AcSubBookCode`", length = 3)
  private String acSubBookCode = " ";

  public MonthlyLM052AssetClassId() {
  }

  public MonthlyLM052AssetClassId(int yearMonth, String assetClassNo, String acSubBookCode) {
    this.yearMonth = yearMonth;
    this.assetClassNo = assetClassNo;
    this.acSubBookCode = acSubBookCode;
  }

/**
	* 資料年月<br>
	* 
	* @return Integer
	*/
  public int getYearMonth() {
    return this.yearMonth;
  }

/**
	* 資料年月<br>
	* 
  *
  * @param yearMonth 資料年月
	*/
  public void setYearMonth(int yearMonth) {
    this.yearMonth = yearMonth;
  }

/**
	* 資產五分類<br>
	* 一類：11、12
二類：21、22、23
三類：3
四類：4
五類：5
折溢價與催收：6
應收利息提列：7
	* @return String
	*/
  public String getAssetClassNo() {
    return this.assetClassNo == null ? "" : this.assetClassNo;
  }

/**
	* 資產五分類<br>
	* 一類：11、12
二類：21、22、23
三類：3
四類：4
五類：5
折溢價與催收：6
應收利息提列：7
  *
  * @param assetClassNo 資產五分類
	*/
  public void setAssetClassNo(String assetClassNo) {
    this.assetClassNo = assetClassNo;
  }

/**
	* 區隔帳冊<br>
	* 一般：00A
利變：201
	* @return String
	*/
  public String getAcSubBookCode() {
    return this.acSubBookCode == null ? "" : this.acSubBookCode;
  }

/**
	* 區隔帳冊<br>
	* 一般：00A
利變：201
  *
  * @param acSubBookCode 區隔帳冊
	*/
  public void setAcSubBookCode(String acSubBookCode) {
    this.acSubBookCode = acSubBookCode;
  }


  @Override
  public int hashCode() {
    return Objects.hash(yearMonth, assetClassNo, acSubBookCode);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    MonthlyLM052AssetClassId monthlyLM052AssetClassId = (MonthlyLM052AssetClassId) obj;
    return yearMonth == monthlyLM052AssetClassId.yearMonth && assetClassNo.equals(monthlyLM052AssetClassId.assetClassNo) && acSubBookCode.equals(monthlyLM052AssetClassId.acSubBookCode);
  }

  @Override
  public String toString() {
    return "MonthlyLM052AssetClassId [yearMonth=" + yearMonth + ", assetClassNo=" + assetClassNo + ", acSubBookCode=" + acSubBookCode + "]";
  }
}
