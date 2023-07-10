package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * FacClose 清償作業檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class FacCloseId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -2125773983445743673L;

// 申請日期
  /* 日曆日 */
  @Column(name = "`ApplDate`")
  private int applDate = 0;

  // 清償序號
  /* 依申請日期由1續編 */
  @Column(name = "`CloseNo`")
  private int closeNo = 0;

  public FacCloseId() {
  }

  public FacCloseId(int applDate, int closeNo) {
    this.applDate = applDate;
    this.closeNo = closeNo;
  }

/**
	* 申請日期<br>
	* 日曆日
	* @return Integer
	*/
  public int getApplDate() {
    return  StaticTool.bcToRoc(this.applDate);
  }

/**
	* 申請日期<br>
	* 日曆日
  *
  * @param applDate 申請日期
  * @throws LogicException when Date Is Warn	*/
  public void setApplDate(int applDate) throws LogicException {
    this.applDate = StaticTool.rocToBc(applDate);
  }

/**
	* 清償序號<br>
	* 依申請日期由1續編
	* @return Integer
	*/
  public int getCloseNo() {
    return this.closeNo;
  }

/**
	* 清償序號<br>
	* 依申請日期由1續編
  *
  * @param closeNo 清償序號
	*/
  public void setCloseNo(int closeNo) {
    this.closeNo = closeNo;
  }


  @Override
  public int hashCode() {
    return Objects.hash(applDate, closeNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    FacCloseId facCloseId = (FacCloseId) obj;
    return applDate == facCloseId.applDate && closeNo == facCloseId.closeNo;
  }

  @Override
  public String toString() {
    return "FacCloseId [applDate=" + applDate + ", closeNo=" + closeNo + "]";
  }
}
