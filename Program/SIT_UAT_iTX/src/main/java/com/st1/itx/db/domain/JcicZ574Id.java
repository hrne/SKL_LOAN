package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * JcicZ574 更生款項統一收付結案通知資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicZ574Id implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -7009011178548952231L;

// 債務人IDN
  @Column(name = "`CustId`", length = 10)
  private String custId = " ";

  // 報送單位代號
  /* 3位文數字 */
  @Column(name = "`SubmitKey`", length = 3)
  private String submitKey = " ";

  // 更生款項統一收付申請日
  @Column(name = "`ApplyDate`")
  private int applyDate = 0;

  public JcicZ574Id() {
  }

  public JcicZ574Id(String custId, String submitKey, int applyDate) {
    this.custId = custId;
    this.submitKey = submitKey;
    this.applyDate = applyDate;
  }

/**
	* 債務人IDN<br>
	* 
	* @return String
	*/
  public String getCustId() {
    return this.custId == null ? "" : this.custId;
  }

/**
	* 債務人IDN<br>
	* 
  *
  * @param custId 債務人IDN
	*/
  public void setCustId(String custId) {
    this.custId = custId;
  }

/**
	* 報送單位代號<br>
	* 3位文數字
	* @return String
	*/
  public String getSubmitKey() {
    return this.submitKey == null ? "" : this.submitKey;
  }

/**
	* 報送單位代號<br>
	* 3位文數字
  *
  * @param submitKey 報送單位代號
	*/
  public void setSubmitKey(String submitKey) {
    this.submitKey = submitKey;
  }

/**
	* 更生款項統一收付申請日<br>
	* 
	* @return Integer
	*/
  public int getApplyDate() {
    return  StaticTool.bcToRoc(this.applyDate);
  }

/**
	* 更生款項統一收付申請日<br>
	* 
  *
  * @param applyDate 更生款項統一收付申請日
  * @throws LogicException when Date Is Warn	*/
  public void setApplyDate(int applyDate) throws LogicException {
    this.applyDate = StaticTool.rocToBc(applyDate);
  }


  @Override
  public int hashCode() {
    return Objects.hash(custId, submitKey, applyDate);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    JcicZ574Id jcicZ574Id = (JcicZ574Id) obj;
    return custId.equals(jcicZ574Id.custId) && submitKey == jcicZ574Id.submitKey && applyDate == jcicZ574Id.applyDate;
  }

  @Override
  public String toString() {
    return "JcicZ574Id [custId=" + custId + ", submitKey=" + submitKey + ", applyDate=" + applyDate + "]";
  }
}
