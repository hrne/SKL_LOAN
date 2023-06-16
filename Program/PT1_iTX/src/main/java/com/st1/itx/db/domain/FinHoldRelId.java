package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * FinHoldRel 金控利關人名單檔 T044
(使用報表：LM049、LQ005)<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class FinHoldRelId implements Serializable {


  // 會計日期
  @Column(name = "`AcDate`")
  private int acDate = 0;

  // 身分證/統一編號
  @Column(name = "`Id`", length = 10)
  private String id = " ";

  public FinHoldRelId() {
  }

  public FinHoldRelId(int acDate, String id) {
    this.acDate = acDate;
    this.id = id;
  }

/**
	* 會計日期<br>
	* 
	* @return Integer
	*/
  public int getAcDate() {
    return  StaticTool.bcToRoc(this.acDate);
  }

/**
	* 會計日期<br>
	* 
  *
  * @param acDate 會計日期
  * @throws LogicException when Date Is Warn	*/
  public void setAcDate(int acDate) throws LogicException {
    this.acDate = StaticTool.rocToBc(acDate);
  }

/**
	* 身分證/統一編號<br>
	* 
	* @return String
	*/
  public String getId() {
    return this.id == null ? "" : this.id;
  }

/**
	* 身分證/統一編號<br>
	* 
  *
  * @param id 身分證/統一編號
	*/
  public void setId(String id) {
    this.id = id;
  }


  @Override
  public int hashCode() {
    return Objects.hash(acDate, id);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    FinHoldRelId finHoldRelId = (FinHoldRelId) obj;
    return acDate == finHoldRelId.acDate && id.equals(finHoldRelId.id);
  }

  @Override
  public String toString() {
    return "FinHoldRelId [acDate=" + acDate + ", id=" + id + "]";
  }
}
