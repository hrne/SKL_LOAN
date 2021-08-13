package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * BankRemit 撥款匯款檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class BankRemitId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 3365059758603566551L;

// 會計日期
  @Column(name = "`AcDate`")
  private int acDate = 0;

  // 整批批號
  @Column(name = "`BatchNo`", length = 6)
  private String batchNo = " ";

  // 經辦
  @Column(name = "`TitaTlrNo`", length = 8)
  private String titaTlrNo = " ";

  // 交易序號
  @Column(name = "`TitaTxtNo`", length = 8)
  private String titaTxtNo = " ";

  public BankRemitId() {
  }

  public BankRemitId(int acDate, String batchNo, String titaTlrNo, String titaTxtNo) {
    this.acDate = acDate;
    this.batchNo = batchNo;
    this.titaTlrNo = titaTlrNo;
    this.titaTxtNo = titaTxtNo;
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
	* 整批批號<br>
	* 
	* @return String
	*/
  public String getBatchNo() {
    return this.batchNo == null ? "" : this.batchNo;
  }

/**
	* 整批批號<br>
	* 
  *
  * @param batchNo 整批批號
	*/
  public void setBatchNo(String batchNo) {
    this.batchNo = batchNo;
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
	* @return String
	*/
  public String getTitaTxtNo() {
    return this.titaTxtNo == null ? "" : this.titaTxtNo;
  }

/**
	* 交易序號<br>
	* 
  *
  * @param titaTxtNo 交易序號
	*/
  public void setTitaTxtNo(String titaTxtNo) {
    this.titaTxtNo = titaTxtNo;
  }


  @Override
  public int hashCode() {
    return Objects.hash(acDate, batchNo, titaTlrNo, titaTxtNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    BankRemitId bankRemitId = (BankRemitId) obj;
    return acDate == bankRemitId.acDate && batchNo.equals(bankRemitId.batchNo) && titaTlrNo.equals(bankRemitId.titaTlrNo) && titaTxtNo.equals(bankRemitId.titaTxtNo);
  }

  @Override
  public String toString() {
    return "BankRemitId [acDate=" + acDate + ", batchNo=" + batchNo + ", titaTlrNo=" + titaTlrNo + ", titaTxtNo=" + titaTxtNo + "]";
  }
}
