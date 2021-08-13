package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * TxAgent 代理人檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class TxAgentId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 477579073128875120L;

// 櫃員
  @Column(name = "`TlrNo`", length = 6)
  private String tlrNo = " ";

  // 代理櫃員
  @Column(name = "`AgentTlrNo`", length = 6)
  private String agentTlrNo = " ";

  public TxAgentId() {
  }

  public TxAgentId(String tlrNo, String agentTlrNo) {
    this.tlrNo = tlrNo;
    this.agentTlrNo = agentTlrNo;
  }

/**
	* 櫃員<br>
	* 
	* @return String
	*/
  public String getTlrNo() {
    return this.tlrNo == null ? "" : this.tlrNo;
  }

/**
	* 櫃員<br>
	* 
  *
  * @param tlrNo 櫃員
	*/
  public void setTlrNo(String tlrNo) {
    this.tlrNo = tlrNo;
  }

/**
	* 代理櫃員<br>
	* 
	* @return String
	*/
  public String getAgentTlrNo() {
    return this.agentTlrNo == null ? "" : this.agentTlrNo;
  }

/**
	* 代理櫃員<br>
	* 
  *
  * @param agentTlrNo 代理櫃員
	*/
  public void setAgentTlrNo(String agentTlrNo) {
    this.agentTlrNo = agentTlrNo;
  }


  @Override
  public int hashCode() {
    return Objects.hash(tlrNo, agentTlrNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    TxAgentId txAgentId = (TxAgentId) obj;
    return tlrNo.equals(txAgentId.tlrNo) && agentTlrNo.equals(txAgentId.agentTlrNo);
  }

  @Override
  public String toString() {
    return "TxAgentId [tlrNo=" + tlrNo + ", agentTlrNo=" + agentTlrNo + "]";
  }
}
