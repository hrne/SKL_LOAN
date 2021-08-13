package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * NegAppr 撥付日期設定<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class NegApprId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 7063397523637195460L;

// 年月
  @Column(name = "`YyyyMm`")
  private int yyyyMm = 0;

  // 類別
  /* 1:一分2:二分、調解3:更生4:清算 */
  @Column(name = "`KindCode`")
  private int kindCode = 0;

  public NegApprId() {
  }

  public NegApprId(int yyyyMm, int kindCode) {
    this.yyyyMm = yyyyMm;
    this.kindCode = kindCode;
  }

/**
	* 年月<br>
	* 
	* @return Integer
	*/
  public int getYyyyMm() {
    return this.yyyyMm;
  }

/**
	* 年月<br>
	* 
  *
  * @param yyyyMm 年月
	*/
  public void setYyyyMm(int yyyyMm) {
    this.yyyyMm = yyyyMm;
  }

/**
	* 類別<br>
	* 1:一分
2:二分、調解
3:更生
4:清算
	* @return Integer
	*/
  public int getKindCode() {
    return this.kindCode;
  }

/**
	* 類別<br>
	* 1:一分
2:二分、調解
3:更生
4:清算
  *
  * @param kindCode 類別
	*/
  public void setKindCode(int kindCode) {
    this.kindCode = kindCode;
  }


  @Override
  public int hashCode() {
    return Objects.hash(yyyyMm, kindCode);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    NegApprId negApprId = (NegApprId) obj;
    return yyyyMm == negApprId.yyyyMm && kindCode == negApprId.kindCode;
  }

  @Override
  public String toString() {
    return "NegApprId [yyyyMm=" + yyyyMm + ", kindCode=" + kindCode + "]";
  }
}
