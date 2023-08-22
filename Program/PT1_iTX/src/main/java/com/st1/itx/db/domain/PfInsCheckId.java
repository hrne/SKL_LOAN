package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * PfInsCheck 房貸獎勵保費檢核檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class PfInsCheckId implements Serializable {


  // 類別
  /* 0:換算業績、業務報酬1:介紹獎金、協辦獎金2:介紹人加碼獎勵津貼 */
  @Column(name = "`Kind`")
  private int kind = 0;

  // 戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 額度編號
  @Column(name = "`FacmNo`")
  private int facmNo = 0;

  public PfInsCheckId() {
  }

  public PfInsCheckId(int kind, int custNo, int facmNo) {
    this.kind = kind;
    this.custNo = custNo;
    this.facmNo = facmNo;
  }

/**
	* 類別<br>
	* 0:換算業績、業務報酬
1:介紹獎金、協辦獎金
2:介紹人加碼獎勵津貼
	* @return Integer
	*/
  public int getKind() {
    return this.kind;
  }

/**
	* 類別<br>
	* 0:換算業績、業務報酬
1:介紹獎金、協辦獎金
2:介紹人加碼獎勵津貼
  *
  * @param kind 類別
	*/
  public void setKind(int kind) {
    this.kind = kind;
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


  @Override
  public int hashCode() {
    return Objects.hash(kind, custNo, facmNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    PfInsCheckId pfInsCheckId = (PfInsCheckId) obj;
    return kind == pfInsCheckId.kind && custNo == pfInsCheckId.custNo && facmNo == pfInsCheckId.facmNo;
  }

  @Override
  public String toString() {
    return "PfInsCheckId [kind=" + kind + ", custNo=" + custNo + ", facmNo=" + facmNo + "]";
  }
}
