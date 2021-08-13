package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * BankRmtf 匯款轉帳檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class BankRmtfId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 6128499153218781991L;

// 會計日
  @Column(name = "`AcDate`")
  private int acDate = 0;

  // 批號
  @Column(name = "`BatchNo`", length = 6)
  private String batchNo = " ";

  // 明細序號
  @Column(name = "`DetailSeq`")
  private int detailSeq = 0;

  public BankRmtfId() {
  }

  public BankRmtfId(int acDate, String batchNo, int detailSeq) {
    this.acDate = acDate;
    this.batchNo = batchNo;
    this.detailSeq = detailSeq;
  }

/**
	* 會計日<br>
	* 
	* @return Integer
	*/
  public int getAcDate() {
    return  StaticTool.bcToRoc(this.acDate);
  }

/**
	* 會計日<br>
	* 
  *
  * @param acDate 會計日
  * @throws LogicException when Date Is Warn	*/
  public void setAcDate(int acDate) throws LogicException {
    this.acDate = StaticTool.rocToBc(acDate);
  }

/**
	* 批號<br>
	* 
	* @return String
	*/
  public String getBatchNo() {
    return this.batchNo == null ? "" : this.batchNo;
  }

/**
	* 批號<br>
	* 
  *
  * @param batchNo 批號
	*/
  public void setBatchNo(String batchNo) {
    this.batchNo = batchNo;
  }

/**
	* 明細序號<br>
	* 
	* @return Integer
	*/
  public int getDetailSeq() {
    return this.detailSeq;
  }

/**
	* 明細序號<br>
	* 
  *
  * @param detailSeq 明細序號
	*/
  public void setDetailSeq(int detailSeq) {
    this.detailSeq = detailSeq;
  }


  @Override
  public int hashCode() {
    return Objects.hash(acDate, batchNo, detailSeq);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    BankRmtfId bankRmtfId = (BankRmtfId) obj;
    return acDate == bankRmtfId.acDate && batchNo.equals(bankRmtfId.batchNo) && detailSeq == bankRmtfId.detailSeq;
  }

  @Override
  public String toString() {
    return "BankRmtfId [acDate=" + acDate + ", batchNo=" + batchNo + ", detailSeq=" + detailSeq + "]";
  }
}
