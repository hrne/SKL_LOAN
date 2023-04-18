package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * BatxOthers 其他還款來源檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class BatxOthersId implements Serializable {


  // 會計日期
  /* 資料日期 */
  @Column(name = "`AcDate`")
  private int acDate = 0;

  // 整批批號
  /* 不同櫃員登錄時，抓取總帳檔當日最新之BATX批號+1 */
  @Column(name = "`BatchNo`", length = 6)
  private String batchNo = " ";

  // 明細序號
  @Column(name = "`DetailSeq`")
  private int detailSeq = 0;

  public BatxOthersId() {
  }

  public BatxOthersId(int acDate, String batchNo, int detailSeq) {
    this.acDate = acDate;
    this.batchNo = batchNo;
    this.detailSeq = detailSeq;
  }

/**
	* 會計日期<br>
	* 資料日期
	* @return Integer
	*/
  public int getAcDate() {
    return  StaticTool.bcToRoc(this.acDate);
  }

/**
	* 會計日期<br>
	* 資料日期
  *
  * @param acDate 會計日期
  * @throws LogicException when Date Is Warn	*/
  public void setAcDate(int acDate) throws LogicException {
    this.acDate = StaticTool.rocToBc(acDate);
  }

/**
	* 整批批號<br>
	* 不同櫃員登錄時，抓取總帳檔當日最新之BATX批號+1
	* @return String
	*/
  public String getBatchNo() {
    return this.batchNo == null ? "" : this.batchNo;
  }

/**
	* 整批批號<br>
	* 不同櫃員登錄時，抓取總帳檔當日最新之BATX批號+1
  *
  * @param batchNo 整批批號
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
    BatxOthersId batxOthersId = (BatxOthersId) obj;
    return acDate == batxOthersId.acDate && batchNo.equals(batxOthersId.batchNo) && detailSeq == batxOthersId.detailSeq;
  }

  @Override
  public String toString() {
    return "BatxOthersId [acDate=" + acDate + ", batchNo=" + batchNo + ", detailSeq=" + detailSeq + "]";
  }
}
