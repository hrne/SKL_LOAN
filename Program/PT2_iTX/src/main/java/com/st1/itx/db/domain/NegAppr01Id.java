package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * NegAppr01 最大債權撥付資料檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class NegAppr01Id implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 2140485611568328182L;

// 會計日期
  @Column(name = "`AcDate`")
  private int acDate = 0;

  // 經辦
  @Column(name = "`TitaTlrNo`", length = 6)
  private String titaTlrNo = " ";

  // 交易序號
  @Column(name = "`TitaTxtNo`")
  private int titaTxtNo = 0;

  // 債權機構代號
  @Column(name = "`FinCode`", length = 10)
  private String finCode = " ";

  public NegAppr01Id() {
  }

  public NegAppr01Id(int acDate, String titaTlrNo, int titaTxtNo, String finCode) {
    this.acDate = acDate;
    this.titaTlrNo = titaTlrNo;
    this.titaTxtNo = titaTxtNo;
    this.finCode = finCode;
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
	* 債權機構代號<br>
	* 
	* @return String
	*/
  public String getFinCode() {
    return this.finCode == null ? "" : this.finCode;
  }

/**
	* 債權機構代號<br>
	* 
  *
  * @param finCode 債權機構代號
	*/
  public void setFinCode(String finCode) {
    this.finCode = finCode;
  }


  @Override
  public int hashCode() {
    return Objects.hash(acDate, titaTlrNo, titaTxtNo, finCode);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    NegAppr01Id negAppr01Id = (NegAppr01Id) obj;
    return acDate == negAppr01Id.acDate && titaTlrNo.equals(negAppr01Id.titaTlrNo) && titaTxtNo == negAppr01Id.titaTxtNo && finCode.equals(negAppr01Id.finCode);
  }

  @Override
  public String toString() {
    return "NegAppr01Id [acDate=" + acDate + ", titaTlrNo=" + titaTlrNo + ", titaTxtNo=" + titaTxtNo + ", finCode=" + finCode + "]";
  }
}
