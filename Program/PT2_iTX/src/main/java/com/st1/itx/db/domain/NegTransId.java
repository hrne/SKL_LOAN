package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * NegTrans 債務協商交易檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class NegTransId implements Serializable {


  // 會計日期
  @Column(name = "`AcDate`")
  private int acDate = 0;

  // 經辦
  @Column(name = "`TitaTlrNo`", length = 6)
  private String titaTlrNo = " ";

  // 交易序號
  @Column(name = "`TitaTxtNo`")
  private int titaTxtNo = 0;

  // 戶號
  /* 保貸戶須建立客戶主檔 */
  @Column(name = "`CustNo`")
  private int custNo = 0;

  public NegTransId() {
  }

  public NegTransId(int acDate, String titaTlrNo, int titaTxtNo, int custNo) {
    this.acDate = acDate;
    this.titaTlrNo = titaTlrNo;
    this.titaTxtNo = titaTxtNo;
    this.custNo = custNo;
  }

/**
	* 會計日期<br>
	* 
	* @return Integer
	*/
  public int getAcDate() {
    return  StaticTool.bcToRoc(this.acDate);
  }

/**
	* 會計日期<br>
	* 
  *
  * @param acDate 會計日期
  * @throws LogicException when Date Is Warn	*/
  public void setAcDate(int acDate) throws LogicException {
    this.acDate = StaticTool.rocToBc(acDate);
  }

/**
	* 經辦<br>
	* 
	* @return String
	*/
  public String getTitaTlrNo() {
    return this.titaTlrNo == null ? "" : this.titaTlrNo;
  }

/**
	* 經辦<br>
	* 
  *
  * @param titaTlrNo 經辦
	*/
  public void setTitaTlrNo(String titaTlrNo) {
    this.titaTlrNo = titaTlrNo;
  }

/**
	* 交易序號<br>
	* 
	* @return Integer
	*/
  public int getTitaTxtNo() {
    return this.titaTxtNo;
  }

/**
	* 交易序號<br>
	* 
  *
  * @param titaTxtNo 交易序號
	*/
  public void setTitaTxtNo(int titaTxtNo) {
    this.titaTxtNo = titaTxtNo;
  }

/**
	* 戶號<br>
	* 保貸戶須建立客戶主檔
	* @return Integer
	*/
  public int getCustNo() {
    return this.custNo;
  }

/**
	* 戶號<br>
	* 保貸戶須建立客戶主檔
  *
  * @param custNo 戶號
	*/
  public void setCustNo(int custNo) {
    this.custNo = custNo;
  }


  @Override
  public int hashCode() {
    return Objects.hash(acDate, titaTlrNo, titaTxtNo, custNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    NegTransId negTransId = (NegTransId) obj;
    return acDate == negTransId.acDate && titaTlrNo.equals(negTransId.titaTlrNo) && titaTxtNo == negTransId.titaTxtNo && custNo == negTransId.custNo;
  }

  @Override
  public String toString() {
    return "NegTransId [acDate=" + acDate + ", titaTlrNo=" + titaTlrNo + ", titaTxtNo=" + titaTxtNo + ", custNo=" + custNo + "]";
  }
}
