package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * AcDetail 會計帳務明細檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class AcDetailId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 3930350034284121452L;

// 登放日期
  @Column(name = "`RelDy`")
  private int relDy = 0;

  // 登放序號
  /* 單位別(4)+經辦(6)+交易序號(8) */
  @Column(name = "`RelTxseq`", length = 18)
  private String relTxseq = " ";

  // 分錄序號
  @Column(name = "`AcSeq`")
  private int acSeq = 0;

  public AcDetailId() {
  }

  public AcDetailId(int relDy, String relTxseq, int acSeq) {
    this.relDy = relDy;
    this.relTxseq = relTxseq;
    this.acSeq = acSeq;
  }

/**
	* 登放日期<br>
	* 
	* @return Integer
	*/
  public int getRelDy() {
    return  StaticTool.bcToRoc(this.relDy);
  }

/**
	* 登放日期<br>
	* 
  *
  * @param relDy 登放日期
  * @throws LogicException when Date Is Warn	*/
  public void setRelDy(int relDy) throws LogicException {
    this.relDy = StaticTool.rocToBc(relDy);
  }

/**
	* 登放序號<br>
	* 單位別(4)+經辦(6)+交易序號(8)
	* @return String
	*/
  public String getRelTxseq() {
    return this.relTxseq == null ? "" : this.relTxseq;
  }

/**
	* 登放序號<br>
	* 單位別(4)+經辦(6)+交易序號(8)
  *
  * @param relTxseq 登放序號
	*/
  public void setRelTxseq(String relTxseq) {
    this.relTxseq = relTxseq;
  }

/**
	* 分錄序號<br>
	* 
	* @return Integer
	*/
  public int getAcSeq() {
    return this.acSeq;
  }

/**
	* 分錄序號<br>
	* 
  *
  * @param acSeq 分錄序號
	*/
  public void setAcSeq(int acSeq) {
    this.acSeq = acSeq;
  }


  @Override
  public int hashCode() {
    return Objects.hash(relDy, relTxseq, acSeq);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    AcDetailId acDetailId = (AcDetailId) obj;
    return relDy == acDetailId.relDy && relTxseq.equals(acDetailId.relTxseq) && acSeq == acDetailId.acSeq;
  }

  @Override
  public String toString() {
    return "AcDetailId [relDy=" + relDy + ", relTxseq=" + relTxseq + ", acSeq=" + acSeq + "]";
  }
}
