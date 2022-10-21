package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * ClBatch 擔保品整批匯入檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class ClBatchId implements Serializable {


  // 批號
  /* 核准號碼(7碼)+流水號(3碼) */
  @Column(name = "`GroupNo`", length = 10)
  private String groupNo = " ";

  // 序號
  /* 同一批號的第一筆為1號,依此類推 */
  @Column(name = "`Seq`")
  private int seq = 0;

  public ClBatchId() {
  }

  public ClBatchId(String groupNo, int seq) {
    this.groupNo = groupNo;
    this.seq = seq;
  }

/**
	* 批號<br>
	* 核准號碼(7碼)+流水號(3碼)
	* @return String
	*/
  public String getGroupNo() {
    return this.groupNo == null ? "" : this.groupNo;
  }

/**
	* 批號<br>
	* 核准號碼(7碼)+流水號(3碼)
  *
  * @param groupNo 批號
	*/
  public void setGroupNo(String groupNo) {
    this.groupNo = groupNo;
  }

/**
	* 序號<br>
	* 同一批號的第一筆為1號,依此類推
	* @return Integer
	*/
  public int getSeq() {
    return this.seq;
  }

/**
	* 序號<br>
	* 同一批號的第一筆為1號,依此類推
  *
  * @param seq 序號
	*/
  public void setSeq(int seq) {
    this.seq = seq;
  }


  @Override
  public int hashCode() {
    return Objects.hash(groupNo, seq);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    ClBatchId clBatchId = (ClBatchId) obj;
    return groupNo.equals(clBatchId.groupNo) && seq == clBatchId.seq;
  }

  @Override
  public String toString() {
    return "ClBatchId [groupNo=" + groupNo + ", seq=" + seq + "]";
  }
}
