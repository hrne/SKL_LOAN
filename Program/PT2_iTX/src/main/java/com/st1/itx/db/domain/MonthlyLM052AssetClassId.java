package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * MonthlyLM052AssetClass LM052資產分類表<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class MonthlyLM052AssetClassId implements Serializable {


  // 資料年月
  @Column(name = "`YearMonth`")
  private int yearMonth = 0;

  // 資產五分類
  /* 11:一類(正常繳息)12:一類(特定放款資產項目)21/22/23:二類3:三類4:四類5:五類61:擔保放款折溢價62:催收折溢價與催收費用7:應收利息提列 */
  @Column(name = "`AssetClassNo`", length = 2)
  private String assetClassNo = " ";

  // 區隔帳冊
  /* 00A:一般201:利變 */
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
	* 11:一類(正常繳息)
12:一類(特定放款資產項目)
21/22/23:二類
3:三類
4:四類
5:五類
61:擔保放款折溢價
62:催收折溢價與催收費用
7:應收利息提列
	* @return String
	*/
  public String getAssetClassNo() {
    return this.assetClassNo == null ? "" : this.assetClassNo;
  }

/**
	* 資產五分類<br>
	* 11:一類(正常繳息)
12:一類(特定放款資產項目)
21/22/23:二類
3:三類
4:四類
5:五類
61:擔保放款折溢價
62:催收折溢價與催收費用
7:應收利息提列
  *
  * @param assetClassNo 資產五分類
	*/
  public void setAssetClassNo(String assetClassNo) {
    this.assetClassNo = assetClassNo;
  }

/**
	* 區隔帳冊<br>
	* 00A:一般
201:利變
	* @return String
	*/
  public String getAcSubBookCode() {
    return this.acSubBookCode == null ? "" : this.acSubBookCode;
  }

/**
	* 區隔帳冊<br>
	* 00A:一般
201:利變
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
