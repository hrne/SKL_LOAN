package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * HlAreaLnYg6Pt 區域中心房貸專員業績統計<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class HlAreaLnYg6PtId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -1471276776431107806L;

// 年月份
  /* eric 2022.1.6 */
  @Column(name = "`WorkYM`")
  private int workYM = 0;

  // 單位代號
  /* eric 2022.1.6 */
  @Column(name = "`AreaCode`", length = 6)
  private String areaCode = " ";

  public HlAreaLnYg6PtId() {
  }

  public HlAreaLnYg6PtId(int workYM, String areaCode) {
    this.workYM = workYM;
    this.areaCode = areaCode;
  }

/**
	* 年月份<br>
	* eric 2022.1.6
	* @return Integer
	*/
  public int getWorkYM() {
    return this.workYM;
  }

/**
	* 年月份<br>
	* eric 2022.1.6
  *
  * @param workYM 年月份
	*/
  public void setWorkYM(int workYM) {
    this.workYM = workYM;
  }

/**
	* 單位代號<br>
	* eric 2022.1.6
	* @return String
	*/
  public String getAreaCode() {
    return this.areaCode == null ? "" : this.areaCode;
  }

/**
	* 單位代號<br>
	* eric 2022.1.6
  *
  * @param areaCode 單位代號
	*/
  public void setAreaCode(String areaCode) {
    this.areaCode = areaCode;
  }


  @Override
  public int hashCode() {
    return Objects.hash(workYM, areaCode);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    HlAreaLnYg6PtId hlAreaLnYg6PtId = (HlAreaLnYg6PtId) obj;
    return workYM == hlAreaLnYg6PtId.workYM && areaCode.equals(hlAreaLnYg6PtId.areaCode);
  }

  @Override
  public String toString() {
    return "HlAreaLnYg6PtId [workYM=" + workYM + ", areaCode=" + areaCode + "]";
  }
}
