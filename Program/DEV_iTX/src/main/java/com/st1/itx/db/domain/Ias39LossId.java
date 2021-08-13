package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * Ias39Loss 特殊客觀減損狀況檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class Ias39LossId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 1177150923037165331L;

// 戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 額度編號
  @Column(name = "`FacmNo`")
  private int facmNo = 0;

  // 發生日期
  @Column(name = "`MarkDate`")
  private int markDate = 0;

  public Ias39LossId() {
  }

  public Ias39LossId(int custNo, int facmNo, int markDate) {
    this.custNo = custNo;
    this.facmNo = facmNo;
    this.markDate = markDate;
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
	* 發生日期<br>
	* 
	* @return Integer
	*/
  public int getMarkDate() {
    return  StaticTool.bcToRoc(this.markDate);
  }

/**
	* 發生日期<br>
	* 
  *
  * @param markDate 發生日期
  * @throws LogicException when Date Is Warn	*/
  public void setMarkDate(int markDate) throws LogicException {
    this.markDate = StaticTool.rocToBc(markDate);
  }


  @Override
  public int hashCode() {
    return Objects.hash(custNo, facmNo, markDate);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    Ias39LossId ias39LossId = (Ias39LossId) obj;
    return custNo == ias39LossId.custNo && facmNo == ias39LossId.facmNo && markDate == ias39LossId.markDate;
  }

  @Override
  public String toString() {
    return "Ias39LossId [custNo=" + custNo + ", facmNo=" + facmNo + ", markDate=" + markDate + "]";
  }
}
