package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * BankDeductDtl 銀行扣款明細檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class BankDeductDtlId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -6543499970752575138L;

// 入帳日期
  @Column(name = "`EntryDate`")
  private int entryDate = 0;

  // 戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 額度
  @Column(name = "`FacmNo`")
  private int facmNo = 0;

  // 撥款
  @Column(name = "`BormNo`")
  private int bormNo = 0;

  // 還款類別
  /* CdCode:RepayType1.期款2.部分償還3.結案4.帳管費5.火險費6.契變手續費7.法務費9.其他 */
  @Column(name = "`RepayType`")
  private int repayType = 0;

  // 應繳日
  @Column(name = "`PayIntDate`")
  private int payIntDate = 0;

  public BankDeductDtlId() {
  }

  public BankDeductDtlId(int entryDate, int custNo, int facmNo, int bormNo, int repayType, int payIntDate) {
    this.entryDate = entryDate;
    this.custNo = custNo;
    this.facmNo = facmNo;
    this.bormNo = bormNo;
    this.repayType = repayType;
    this.payIntDate = payIntDate;
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
	* 額度<br>
	* 
	* @return Integer
	*/
  public int getFacmNo() {
    return this.facmNo;
  }

/**
	* 額度<br>
	* 
  *
  * @param facmNo 額度
	*/
  public void setFacmNo(int facmNo) {
    this.facmNo = facmNo;
  }

/**
	* 撥款<br>
	* 
	* @return Integer
	*/
  public int getBormNo() {
    return this.bormNo;
  }

/**
	* 撥款<br>
	* 
  *
  * @param bormNo 撥款
	*/
  public void setBormNo(int bormNo) {
    this.bormNo = bormNo;
  }

/**
	* 還款類別<br>
	* CdCode:RepayType
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
  public int getRepayType() {
    return this.repayType;
  }

/**
	* 還款類別<br>
	* CdCode:RepayType
1.期款
2.部分償還
3.結案
4.帳管費
5.火險費
6.契變手續費
7.法務費
9.其他
  *
  * @param repayType 還款類別
	*/
  public void setRepayType(int repayType) {
    this.repayType = repayType;
  }

/**
	* 應繳日<br>
	* 
	* @return Integer
	*/
  public int getPayIntDate() {
    return  StaticTool.bcToRoc(this.payIntDate);
  }

/**
	* 應繳日<br>
	* 
  *
  * @param payIntDate 應繳日
  * @throws LogicException when Date Is Warn	*/
  public void setPayIntDate(int payIntDate) throws LogicException {
    this.payIntDate = StaticTool.rocToBc(payIntDate);
  }


  @Override
  public int hashCode() {
    return Objects.hash(entryDate, custNo, facmNo, bormNo, repayType, payIntDate);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    BankDeductDtlId bankDeductDtlId = (BankDeductDtlId) obj;
    return entryDate == bankDeductDtlId.entryDate && custNo == bankDeductDtlId.custNo && facmNo == bankDeductDtlId.facmNo && bormNo == bankDeductDtlId.bormNo && repayType == bankDeductDtlId.repayType && payIntDate == bankDeductDtlId.payIntDate;
  }

  @Override
  public String toString() {
    return "BankDeductDtlId [entryDate=" + entryDate + ", custNo=" + custNo + ", facmNo=" + facmNo + ", bormNo=" + bormNo + ", repayType=" + repayType + ", payIntDate=" + payIntDate + "]";
  }
}
