package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * MlaundryDetail 疑似洗錢交易合理性明細檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class MlaundryDetailId implements Serializable {


  // 入帳日期
  @Column(name = "`EntryDate`")
  private int entryDate = 0;

  // 交易樣態
  /* 1:樣態12:樣態23:樣態3 */
  @Column(name = "`Factor`")
  private int factor = 0;

  // 戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

  public MlaundryDetailId() {
  }

  public MlaundryDetailId(int entryDate, int factor, int custNo) {
    this.entryDate = entryDate;
    this.factor = factor;
    this.custNo = custNo;
  }

/**
	* 入帳日期<br>
	* 
	* @return Integer
	*/
  public int getEntryDate() {
    return  StaticTool.bcToRoc(this.entryDate);
  }

/**
	* 入帳日期<br>
	* 
  *
  * @param entryDate 入帳日期
  * @throws LogicException when Date Is Warn	*/
  public void setEntryDate(int entryDate) throws LogicException {
    this.entryDate = StaticTool.rocToBc(entryDate);
  }

/**
	* 交易樣態<br>
	* 1:樣態1
2:樣態2
3:樣態3
	* @return Integer
	*/
  public int getFactor() {
    return this.factor;
  }

/**
	* 交易樣態<br>
	* 1:樣態1
2:樣態2
3:樣態3
  *
  * @param factor 交易樣態
	*/
  public void setFactor(int factor) {
    this.factor = factor;
  }

/**
	* 戶號<br>
	* 
	* @return Integer
	*/
  public int getCustNo() {
    return this.custNo;
  }

/**
	* 戶號<br>
	* 
  *
  * @param custNo 戶號
	*/
  public void setCustNo(int custNo) {
    this.custNo = custNo;
  }


  @Override
  public int hashCode() {
    return Objects.hash(entryDate, factor, custNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    MlaundryDetailId mlaundryDetailId = (MlaundryDetailId) obj;
    return entryDate == mlaundryDetailId.entryDate && factor == mlaundryDetailId.factor && custNo == mlaundryDetailId.custNo;
  }

  @Override
  public String toString() {
    return "MlaundryDetailId [entryDate=" + entryDate + ", factor=" + factor + ", custNo=" + custNo + "]";
  }
}
