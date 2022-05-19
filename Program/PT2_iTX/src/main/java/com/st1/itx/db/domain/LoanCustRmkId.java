package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * LoanCustRmk 帳務備忘錄明細檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class LoanCustRmkId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 3019610490014362560L;

// 借款人戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 會計日期
  /* 新增此筆帳務備忘錄時的系統會計日期 */
  @Column(name = "`AcDate`")
  private int acDate = 0;

  // 備忘錄序號
  @Column(name = "`RmkNo`")
  private int rmkNo = 0;

  public LoanCustRmkId() {
  }

  public LoanCustRmkId(int custNo, int acDate, int rmkNo) {
    this.custNo = custNo;
    this.acDate = acDate;
    this.rmkNo = rmkNo;
  }

/**
	* 借款人戶號<br>
	* 
	* @return Integer
	*/
  public int getCustNo() {
    return this.custNo;
  }

/**
	* 借款人戶號<br>
	* 
  *
  * @param custNo 借款人戶號
	*/
  public void setCustNo(int custNo) {
    this.custNo = custNo;
  }

/**
	* 會計日期<br>
	* 新增此筆帳務備忘錄時的系統會計日期
	* @return Integer
	*/
  public int getAcDate() {
    return  StaticTool.bcToRoc(this.acDate);
  }

/**
	* 會計日期<br>
	* 新增此筆帳務備忘錄時的系統會計日期
  *
  * @param acDate 會計日期
  * @throws LogicException when Date Is Warn	*/
  public void setAcDate(int acDate) throws LogicException {
    this.acDate = StaticTool.rocToBc(acDate);
  }

/**
	* 備忘錄序號<br>
	* 
	* @return Integer
	*/
  public int getRmkNo() {
    return this.rmkNo;
  }

/**
	* 備忘錄序號<br>
	* 
  *
  * @param rmkNo 備忘錄序號
	*/
  public void setRmkNo(int rmkNo) {
    this.rmkNo = rmkNo;
  }


  @Override
  public int hashCode() {
    return Objects.hash(custNo, acDate, rmkNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    LoanCustRmkId loanCustRmkId = (LoanCustRmkId) obj;
    return custNo == loanCustRmkId.custNo && acDate == loanCustRmkId.acDate && rmkNo == loanCustRmkId.rmkNo;
  }

  @Override
  public String toString() {
    return "LoanCustRmkId [custNo=" + custNo + ", acDate=" + acDate + ", rmkNo=" + rmkNo + "]";
  }
}
