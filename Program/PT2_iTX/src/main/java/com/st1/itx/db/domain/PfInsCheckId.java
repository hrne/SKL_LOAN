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


  /**
	 * 
	 */
	private static final long serialVersionUID = -4992378645571948623L;

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

  // 檢核工作月
  /* 檢核結果已為Y者不再變動，N則篩選借款書申請日三個月內者再次檢核 */
  @Column(name = "`CheckWorkMonth`")
  private int checkWorkMonth = 0;

  // 業績工作月
  @Column(name = "`PerfWorkMonth`")
  private int perfWorkMonth = 0;

  public PfInsCheckId() {
  }

  public PfInsCheckId(int kind, int custNo, int facmNo, int checkWorkMonth, int perfWorkMonth) {
    this.kind = kind;
    this.custNo = custNo;
    this.facmNo = facmNo;
    this.checkWorkMonth = checkWorkMonth;
    this.perfWorkMonth = perfWorkMonth;
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

/**
	* 檢核工作月<br>
	* 檢核結果已為Y者不再變動，N則篩選借款書申請日三個月內者再次檢核
	* @return Integer
	*/
  public int getCheckWorkMonth() {
    return this.checkWorkMonth;
  }

/**
	* 檢核工作月<br>
	* 檢核結果已為Y者不再變動，N則篩選借款書申請日三個月內者再次檢核
  *
  * @param checkWorkMonth 檢核工作月
	*/
  public void setCheckWorkMonth(int checkWorkMonth) {
    this.checkWorkMonth = checkWorkMonth;
  }

/**
	* 業績工作月<br>
	* 
	* @return Integer
	*/
  public int getPerfWorkMonth() {
    return this.perfWorkMonth;
  }

/**
	* 業績工作月<br>
	* 
  *
  * @param perfWorkMonth 業績工作月
	*/
  public void setPerfWorkMonth(int perfWorkMonth) {
    this.perfWorkMonth = perfWorkMonth;
  }


  @Override
  public int hashCode() {
    return Objects.hash(kind, custNo, facmNo, checkWorkMonth, perfWorkMonth);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    PfInsCheckId pfInsCheckId = (PfInsCheckId) obj;
    return kind == pfInsCheckId.kind && custNo == pfInsCheckId.custNo && facmNo == pfInsCheckId.facmNo && checkWorkMonth == pfInsCheckId.checkWorkMonth && perfWorkMonth == pfInsCheckId.perfWorkMonth;
  }

  @Override
  public String toString() {
    return "PfInsCheckId [kind=" + kind + ", custNo=" + custNo + ", facmNo=" + facmNo + ", checkWorkMonth=" + checkWorkMonth + ", perfWorkMonth=" + perfWorkMonth + "]";
  }
}
