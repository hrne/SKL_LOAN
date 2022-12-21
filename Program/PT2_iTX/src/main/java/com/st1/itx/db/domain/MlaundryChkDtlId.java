package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * MlaundryChkDtl 疑似洗錢樣態檢核明細檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class MlaundryChkDtlId implements Serializable {


  // 入帳日期(統計期間迄日)
  @Column(name = "`EntryDate`")
  private int entryDate = 0;

  // 交易樣態
  @Column(name = "`Factor`")
  private int factor = 0;

  // 戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 明細序號
  @Column(name = "`DtlSeq`")
  private int dtlSeq = 0;

  public MlaundryChkDtlId() {
  }

  public MlaundryChkDtlId(int entryDate, int factor, int custNo, int dtlSeq) {
    this.entryDate = entryDate;
    this.factor = factor;
    this.custNo = custNo;
    this.dtlSeq = dtlSeq;
  }

/**
	* 入帳日期(統計期間迄日)<br>
	* 
	* @return Integer
	*/
  public int getEntryDate() {
    return  StaticTool.bcToRoc(this.entryDate);
  }

/**
	* 入帳日期(統計期間迄日)<br>
	* 
  *
  * @param entryDate 入帳日期(統計期間迄日)
  * @throws LogicException when Date Is Warn	*/
  public void setEntryDate(int entryDate) throws LogicException {
    this.entryDate = StaticTool.rocToBc(entryDate);
  }

/**
	* 交易樣態<br>
	* 
	* @return Integer
	*/
  public int getFactor() {
    return this.factor;
  }

/**
	* 交易樣態<br>
	* 
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

/**
	* 明細序號<br>
	* 
	* @return Integer
	*/
  public int getDtlSeq() {
    return this.dtlSeq;
  }

/**
	* 明細序號<br>
	* 
  *
  * @param dtlSeq 明細序號
	*/
  public void setDtlSeq(int dtlSeq) {
    this.dtlSeq = dtlSeq;
  }


  @Override
  public int hashCode() {
    return Objects.hash(entryDate, factor, custNo, dtlSeq);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    MlaundryChkDtlId mlaundryChkDtlId = (MlaundryChkDtlId) obj;
    return entryDate == mlaundryChkDtlId.entryDate && factor == mlaundryChkDtlId.factor && custNo == mlaundryChkDtlId.custNo && dtlSeq == mlaundryChkDtlId.dtlSeq;
  }

  @Override
  public String toString() {
    return "MlaundryChkDtlId [entryDate=" + entryDate + ", factor=" + factor + ", custNo=" + custNo + ", dtlSeq=" + dtlSeq + "]";
  }
}
