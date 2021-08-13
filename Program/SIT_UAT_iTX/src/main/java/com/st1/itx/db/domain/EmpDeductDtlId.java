package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * EmpDeductDtl 員工扣薪明細檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class EmpDeductDtlId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -548534702324679504L;

// 入帳日期
  @Column(name = "`EntryDate`")
  private int entryDate = 0;

  // 戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 入帳扣款別
  /* 0.債協暫收款1.期款2.部分償還3.結案4.帳管費5.火險費6.契變手續費7.法務費9.其他 */
  @Column(name = "`AchRepayCode`")
  private int achRepayCode = 0;

  // 業績年月
  @Column(name = "`PerfMonth`")
  private int perfMonth = 0;

  // 流程別
  @Column(name = "`ProcCode`", length = 1)
  private String procCode = " ";

  // 扣款代碼
  /* 1:扣薪件;2:特約件;3:滯繳件;4:人事特約件;5:房貸扣薪件 */
  @Column(name = "`RepayCode`", length = 1)
  private String repayCode = " ";

  // 科目
  @Column(name = "`AcctCode`", length = 12)
  private String acctCode = " ";

  // 額度編號
  @Column(name = "`FacmNo`")
  private int facmNo = 0;

  // 撥款編號
  @Column(name = "`BormNo`")
  private int bormNo = 0;

  public EmpDeductDtlId() {
  }

  public EmpDeductDtlId(int entryDate, int custNo, int achRepayCode, int perfMonth, String procCode, String repayCode, String acctCode, int facmNo, int bormNo) {
    this.entryDate = entryDate;
    this.custNo = custNo;
    this.achRepayCode = achRepayCode;
    this.perfMonth = perfMonth;
    this.procCode = procCode;
    this.repayCode = repayCode;
    this.acctCode = acctCode;
    this.facmNo = facmNo;
    this.bormNo = bormNo;
  }

/**
	* 入帳日期<br>
	* 
	* @return Integer
	*/
  public int getEntryDate() {
    return this.entryDate;
  }

/**
	* 入帳日期<br>
	* 
  *
  * @param entryDate 入帳日期
	*/
  public void setEntryDate(int entryDate) {
    this.entryDate = entryDate;
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
	* 入帳扣款別<br>
	* 0.債協暫收款
1.期款
2.部分償還
3.結案
4.帳管費
5.火險費
6.契變手續費
7.法務費
9.其他
	* @return Integer
	*/
  public int getAchRepayCode() {
    return this.achRepayCode;
  }

/**
	* 入帳扣款別<br>
	* 0.債協暫收款
1.期款
2.部分償還
3.結案
4.帳管費
5.火險費
6.契變手續費
7.法務費
9.其他
  *
  * @param achRepayCode 入帳扣款別
	*/
  public void setAchRepayCode(int achRepayCode) {
    this.achRepayCode = achRepayCode;
  }

/**
	* 業績年月<br>
	* 
	* @return Integer
	*/
  public int getPerfMonth() {
    return this.perfMonth;
  }

/**
	* 業績年月<br>
	* 
  *
  * @param perfMonth 業績年月
	*/
  public void setPerfMonth(int perfMonth) {
    this.perfMonth = perfMonth;
  }

/**
	* 流程別<br>
	* 
	* @return String
	*/
  public String getProcCode() {
    return this.procCode == null ? "" : this.procCode;
  }

/**
	* 流程別<br>
	* 
  *
  * @param procCode 流程別
	*/
  public void setProcCode(String procCode) {
    this.procCode = procCode;
  }

/**
	* 扣款代碼<br>
	* 1:扣薪件;2:特約件;3:滯繳件;4:人事特約件;5:房貸扣薪件
	* @return String
	*/
  public String getRepayCode() {
    return this.repayCode == null ? "" : this.repayCode;
  }

/**
	* 扣款代碼<br>
	* 1:扣薪件;2:特約件;3:滯繳件;4:人事特約件;5:房貸扣薪件
  *
  * @param repayCode 扣款代碼
	*/
  public void setRepayCode(String repayCode) {
    this.repayCode = repayCode;
  }

/**
	* 科目<br>
	* 
	* @return String
	*/
  public String getAcctCode() {
    return this.acctCode == null ? "" : this.acctCode;
  }

/**
	* 科目<br>
	* 
  *
  * @param acctCode 科目
	*/
  public void setAcctCode(String acctCode) {
    this.acctCode = acctCode;
  }

/**
	* 額度編號<br>
	* 
	* @return Integer
	*/
  public int getFacmNo() {
    return this.facmNo;
  }

/**
	* 額度編號<br>
	* 
  *
  * @param facmNo 額度編號
	*/
  public void setFacmNo(int facmNo) {
    this.facmNo = facmNo;
  }

/**
	* 撥款編號<br>
	* 
	* @return Integer
	*/
  public int getBormNo() {
    return this.bormNo;
  }

/**
	* 撥款編號<br>
	* 
  *
  * @param bormNo 撥款編號
	*/
  public void setBormNo(int bormNo) {
    this.bormNo = bormNo;
  }


  @Override
  public int hashCode() {
    return Objects.hash(entryDate, custNo, achRepayCode, perfMonth, procCode, repayCode, acctCode, facmNo, bormNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    EmpDeductDtlId empDeductDtlId = (EmpDeductDtlId) obj;
    return entryDate == empDeductDtlId.entryDate && custNo == empDeductDtlId.custNo && achRepayCode == empDeductDtlId.achRepayCode && perfMonth == empDeductDtlId.perfMonth && procCode.equals(empDeductDtlId.procCode) && repayCode.equals(empDeductDtlId.repayCode) && acctCode.equals(empDeductDtlId.acctCode) && facmNo == empDeductDtlId.facmNo && bormNo == empDeductDtlId.bormNo;
  }

  @Override
  public String toString() {
    return "EmpDeductDtlId [entryDate=" + entryDate + ", custNo=" + custNo + ", achRepayCode=" + achRepayCode + ", perfMonth=" + perfMonth + ", procCode=" + procCode + ", repayCode=" + repayCode + ", acctCode=" + acctCode + ", facmNo=" + facmNo + ", bormNo=" + bormNo + "]";
  }
}
