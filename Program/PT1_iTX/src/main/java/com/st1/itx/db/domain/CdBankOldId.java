package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * CdBankOld 舊行庫資料檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class CdBankOldId implements Serializable {


  // 行庫代號
  @Column(name = "`BankCode`", length = 3)
  private String bankCode = " ";

  // 分行代號
  @Column(name = "`BranchCode`", length = 4)
  private String branchCode = " ";

  public CdBankOldId() {
  }

  public CdBankOldId(String bankCode, String branchCode) {
    this.bankCode = bankCode;
    this.branchCode = branchCode;
  }

/**
	* 行庫代號<br>
	* 
	* @return String
	*/
  public String getBankCode() {
    return this.bankCode == null ? "" : this.bankCode;
  }

/**
	* 行庫代號<br>
	* 
  *
  * @param bankCode 行庫代號
	*/
  public void setBankCode(String bankCode) {
    this.bankCode = bankCode;
  }

/**
	* 分行代號<br>
	* 
	* @return String
	*/
  public String getBranchCode() {
    return this.branchCode == null ? "" : this.branchCode;
  }

/**
	* 分行代號<br>
	* 
  *
  * @param branchCode 分行代號
	*/
  public void setBranchCode(String branchCode) {
    this.branchCode = branchCode;
  }


  @Override
  public int hashCode() {
    return Objects.hash(bankCode, branchCode);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    CdBankOldId cdBankOldId = (CdBankOldId) obj;
    return bankCode.equals(cdBankOldId.bankCode) && branchCode.equals(cdBankOldId.branchCode);
  }

  @Override
  public String toString() {
    return "CdBankOldId [bankCode=" + bankCode + ", branchCode=" + branchCode + "]";
  }
}
