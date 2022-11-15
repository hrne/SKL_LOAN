package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;

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


  // 會計日期
  @Column(name = "`AcDate`")
  private int acDate = 0;

  // 經辦
  @Column(name = "`TitaTlrNo`", length = 6)
  private String titaTlrNo = " ";

  // 交易序號
  @Column(name = "`TitaTxtNo`", length = 8)
  private String titaTxtNo = " ";

  // 流水號
  /* 舊資料有多筆同一筆會計日期+經辦+交易序號有多筆 */
  @Column(name = "`Seq`")
  private int seq = 0;

  public BankRemitId() {
  }

  public BankRemitId(int acDate, String titaTlrNo, String titaTxtNo, int seq) {
    this.acDate = acDate;
    this.titaTlrNo = titaTlrNo;
    this.titaTxtNo = titaTxtNo;
    this.seq = seq;
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

/**
	* 流水號<br>
	* 舊資料有多筆同一筆會計日期+經辦+交易序號有多筆
	* @return Integer
	*/
  public int getSeq() {
    return this.seq;
  }

/**
	* 流水號<br>
	* 舊資料有多筆同一筆會計日期+經辦+交易序號有多筆
  *
  * @param seq 流水號
	*/
  public void setSeq(int seq) {
    this.seq = seq;
  }


  @Override
  public int hashCode() {
    return Objects.hash(acDate, titaTlrNo, titaTxtNo, seq);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    BankRemitId bankRemitId = (BankRemitId) obj;
    return acDate == bankRemitId.acDate && titaTlrNo.equals(bankRemitId.titaTlrNo) && titaTxtNo.equals(bankRemitId.titaTxtNo) && seq == bankRemitId.seq;
  }

  @Override
  public String toString() {
    return "BankRemitId [acDate=" + acDate + ", titaTlrNo=" + titaTlrNo + ", titaTxtNo=" + titaTxtNo + ", seq=" + seq + "]";
  }
}
