package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * CdComm 雜項代碼檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class CdCommId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 5065314289644705021L;

// 代碼類別
  /* 01:政府補貼利率02:專案放款 */
  @Column(name = "`CdType`", length = 5)
  private String cdType = " ";

  // 代碼項目
  /* 01:補貼息02:放款金額 */
  @Column(name = "`CdItem`", length = 5)
  private String cdItem = " ";

  // 生效日期
  /* 專案放款生效日期固定取1日(02:專案放款) */
  @Column(name = "`EffectDate`")
  private int effectDate = 0;

  public CdCommId() {
  }

  public CdCommId(String cdType, String cdItem, int effectDate) {
    this.cdType = cdType;
    this.cdItem = cdItem;
    this.effectDate = effectDate;
  }

/**
	* 代碼類別<br>
	* 01:政府補貼利率
02:專案放款
	* @return String
	*/
  public String getCdType() {
    return this.cdType == null ? "" : this.cdType;
  }

/**
	* 代碼類別<br>
	* 01:政府補貼利率
02:專案放款
  *
  * @param cdType 代碼類別
	*/
  public void setCdType(String cdType) {
    this.cdType = cdType;
  }

/**
	* 代碼項目<br>
	* 01:補貼息
02:放款金額
	* @return String
	*/
  public String getCdItem() {
    return this.cdItem == null ? "" : this.cdItem;
  }

/**
	* 代碼項目<br>
	* 01:補貼息
02:放款金額
  *
  * @param cdItem 代碼項目
	*/
  public void setCdItem(String cdItem) {
    this.cdItem = cdItem;
  }

/**
	* 生效日期<br>
	* 專案放款生效日期固定取1日(02:專案放款)
	* @return Integer
	*/
  public int getEffectDate() {
    return  StaticTool.bcToRoc(this.effectDate);
  }

/**
	* 生效日期<br>
	* 專案放款生效日期固定取1日(02:專案放款)
  *
  * @param effectDate 生效日期
  * @throws LogicException when Date Is Warn	*/
  public void setEffectDate(int effectDate) throws LogicException {
    this.effectDate = StaticTool.rocToBc(effectDate);
  }


  @Override
  public int hashCode() {
    return Objects.hash(cdType, cdItem, effectDate);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    CdCommId cdCommId = (CdCommId) obj;
    return cdType.equals(cdCommId.cdType) && cdItem.equals(cdCommId.cdItem) && effectDate == cdCommId.effectDate;
  }

  @Override
  public String toString() {
    return "CdCommId [cdType=" + cdType + ", cdItem=" + cdItem + ", effectDate=" + effectDate + "]";
  }
}
