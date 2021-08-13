package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * InsuRenewMediaTemp 火險詢價媒體暫存檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class InsuRenewMediaTempId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 1357240506942772739L;

// 火險到期年月
  @Column(name = "`FireInsuMonth`", length = 6)
  private String fireInsuMonth = " ";

  // 保單號碼
  @Column(name = "`InsuNo`", length = 16)
  private String insuNo = " ";

  public InsuRenewMediaTempId() {
  }

  public InsuRenewMediaTempId(String fireInsuMonth, String insuNo) {
    this.fireInsuMonth = fireInsuMonth;
    this.insuNo = insuNo;
  }

/**
	* 火險到期年月<br>
	* 
	* @return String
	*/
  public String getFireInsuMonth() {
    return this.fireInsuMonth == null ? "" : this.fireInsuMonth;
  }

/**
	* 火險到期年月<br>
	* 
  *
  * @param fireInsuMonth 火險到期年月
	*/
  public void setFireInsuMonth(String fireInsuMonth) {
    this.fireInsuMonth = fireInsuMonth;
  }

/**
	* 保單號碼<br>
	* 
	* @return String
	*/
  public String getInsuNo() {
    return this.insuNo == null ? "" : this.insuNo;
  }

/**
	* 保單號碼<br>
	* 
  *
  * @param insuNo 保單號碼
	*/
  public void setInsuNo(String insuNo) {
    this.insuNo = insuNo;
  }


  @Override
  public int hashCode() {
    return Objects.hash(fireInsuMonth, insuNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    InsuRenewMediaTempId insuRenewMediaTempId = (InsuRenewMediaTempId) obj;
    return fireInsuMonth.equals(insuRenewMediaTempId.fireInsuMonth) && insuNo == insuRenewMediaTempId.insuNo;
  }

  @Override
  public String toString() {
    return "InsuRenewMediaTempId [fireInsuMonth=" + fireInsuMonth + ", insuNo=" + insuNo + "]";
  }
}
