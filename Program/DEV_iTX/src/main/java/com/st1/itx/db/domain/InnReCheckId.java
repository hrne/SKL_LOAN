package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * InnReCheck 覆審案件明細檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class InnReCheckId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -8252281610986094654L;

// 資料年月
  /* 指定複審名單時為 0 紅字部分2021/11/5審查會議修改 */
  @Column(name = "`YearMonth`")
  private int yearMonth = 0;

  // 條件代碼
  /* 01-個金3000萬以上02-企金3000萬以上03-個金2000萬以上小於3000萬04-個金100萬以上小於2000萬05-企金未達3000萬06-土地追蹤99-指定複審名單 */
  @Column(name = "`ConditionCode`")
  private int conditionCode = 0;

  // 借款人戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 額度號碼
  @Column(name = "`FacmNo`")
  private int facmNo = 0;

  public InnReCheckId() {
  }

  public InnReCheckId(int yearMonth, int conditionCode, int custNo, int facmNo) {
    this.yearMonth = yearMonth;
    this.conditionCode = conditionCode;
    this.custNo = custNo;
    this.facmNo = facmNo;
  }

/**
	* 資料年月<br>
	* 指定複審名單時為 0 紅字部分2021/11/5審查會議修改
	* @return Integer
	*/
  public int getYearMonth() {
    return this.yearMonth;
  }

/**
	* 資料年月<br>
	* 指定複審名單時為 0 紅字部分2021/11/5審查會議修改
  *
  * @param yearMonth 資料年月
	*/
  public void setYearMonth(int yearMonth) {
    this.yearMonth = yearMonth;
  }

/**
	* 條件代碼<br>
	* 01-個金3000萬以上
02-企金3000萬以上
03-個金2000萬以上小於3000萬
04-個金100萬以上小於2000萬
05-企金未達3000萬
06-土地追蹤
99-指定複審名單
	* @return Integer
	*/
  public int getConditionCode() {
    return this.conditionCode;
  }

/**
	* 條件代碼<br>
	* 01-個金3000萬以上
02-企金3000萬以上
03-個金2000萬以上小於3000萬
04-個金100萬以上小於2000萬
05-企金未達3000萬
06-土地追蹤
99-指定複審名單
  *
  * @param conditionCode 條件代碼
	*/
  public void setConditionCode(int conditionCode) {
    this.conditionCode = conditionCode;
  }

/**
	* 借款人戶號<br>
	* 
	* @return Integer
	*/
  public int getCustNo() {
    return this.custNo;
  }

/**
	* 借款人戶號<br>
	* 
  *
  * @param custNo 借款人戶號
	*/
  public void setCustNo(int custNo) {
    this.custNo = custNo;
  }

/**
	* 額度號碼<br>
	* 
	* @return Integer
	*/
  public int getFacmNo() {
    return this.facmNo;
  }

/**
	* 額度號碼<br>
	* 
  *
  * @param facmNo 額度號碼
	*/
  public void setFacmNo(int facmNo) {
    this.facmNo = facmNo;
  }


  @Override
  public int hashCode() {
    return Objects.hash(yearMonth, conditionCode, custNo, facmNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    InnReCheckId innReCheckId = (InnReCheckId) obj;
    return yearMonth == innReCheckId.yearMonth && conditionCode == innReCheckId.conditionCode && custNo == innReCheckId.custNo && facmNo == innReCheckId.facmNo;
  }

  @Override
  public String toString() {
    return "InnReCheckId [yearMonth=" + yearMonth + ", conditionCode=" + conditionCode + ", custNo=" + custNo + ", facmNo=" + facmNo + "]";
  }
}
