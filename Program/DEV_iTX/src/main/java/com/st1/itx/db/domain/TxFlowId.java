package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * TxFlow 交易流程控制檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class TxFlowId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 8451595610492535569L;

// 帳務日
  @Column(name = "`Entdy`")
  private int entdy = 0;

  // 流程控制序號
  @Column(name = "`FlowNo`", length = 18)
  private String flowNo = " ";

  public TxFlowId() {
  }

  public TxFlowId(int entdy, String flowNo) {
    this.entdy = entdy;
    this.flowNo = flowNo;
  }

/**
	* 帳務日<br>
	* 
	* @return Integer
	*/
  public int getEntdy() {
    return  StaticTool.bcToRoc(this.entdy);
  }

/**
	* 帳務日<br>
	* 
  *
  * @param entdy 帳務日
  * @throws LogicException when Date Is Warn	*/
  public void setEntdy(int entdy) throws LogicException {
    this.entdy = StaticTool.rocToBc(entdy);
  }

/**
	* 流程控制序號<br>
	* 
	* @return String
	*/
  public String getFlowNo() {
    return this.flowNo == null ? "" : this.flowNo;
  }

/**
	* 流程控制序號<br>
	* 
  *
  * @param flowNo 流程控制序號
	*/
  public void setFlowNo(String flowNo) {
    this.flowNo = flowNo;
  }


  @Override
  public int hashCode() {
    return Objects.hash(entdy, flowNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    TxFlowId txFlowId = (TxFlowId) obj;
    return entdy == txFlowId.entdy && flowNo.equals(txFlowId.flowNo);
  }

  @Override
  public String toString() {
    return "TxFlowId [entdy=" + entdy + ", flowNo=" + flowNo + "]";
  }
}
