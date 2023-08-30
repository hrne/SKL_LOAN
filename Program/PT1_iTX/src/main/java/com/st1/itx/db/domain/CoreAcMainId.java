package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * CoreAcMain 核心會計總帳檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class CoreAcMainId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -4563246172689845577L;

// 帳冊別
  /* 000:全公司 */
  @Column(name = "`AcBookCode`", length = 3)
  private String acBookCode = " ";

  // 區隔帳冊
  /* 00A:傳統帳冊201:利變年金帳冊 */
  @Column(name = "`AcSubBookCode`", length = 3)
  private String acSubBookCode = " ";

  // 幣別
  @Column(name = "`CurrencyCode`", length = 3)
  private String currencyCode = " ";

  // 科目代號
  @Column(name = "`AcNoCode`", length = 11)
  private String acNoCode = " ";

  // 子目代號
  @Column(name = "`AcSubCode`", length = 5)
  private String acSubCode = " ";

  // 會計日期
  /* 西元年月日 */
  @Column(name = "`AcDate`")
  private int acDate = 0;

  public CoreAcMainId() {
  }

  public CoreAcMainId(String acBookCode, String acSubBookCode, String currencyCode, String acNoCode, String acSubCode, int acDate) {
    this.acBookCode = acBookCode;
    this.acSubBookCode = acSubBookCode;
    this.currencyCode = currencyCode;
    this.acNoCode = acNoCode;
    this.acSubCode = acSubCode;
    this.acDate = acDate;
  }

/**
	* 帳冊別<br>
	* 000:全公司
	* @return String
	*/
  public String getAcBookCode() {
    return this.acBookCode == null ? "" : this.acBookCode;
  }

/**
	* 帳冊別<br>
	* 000:全公司
  *
  * @param acBookCode 帳冊別
	*/
  public void setAcBookCode(String acBookCode) {
    this.acBookCode = acBookCode;
  }

/**
	* 區隔帳冊<br>
	* 00A:傳統帳冊
201:利變年金帳冊
	* @return String
	*/
  public String getAcSubBookCode() {
    return this.acSubBookCode == null ? "" : this.acSubBookCode;
  }

/**
	* 區隔帳冊<br>
	* 00A:傳統帳冊
201:利變年金帳冊
  *
  * @param acSubBookCode 區隔帳冊
	*/
  public void setAcSubBookCode(String acSubBookCode) {
    this.acSubBookCode = acSubBookCode;
  }

/**
	* 幣別<br>
	* 
	* @return String
	*/
  public String getCurrencyCode() {
    return this.currencyCode == null ? "" : this.currencyCode;
  }

/**
	* 幣別<br>
	* 
  *
  * @param currencyCode 幣別
	*/
  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
  }

/**
	* 科目代號<br>
	* 
	* @return String
	*/
  public String getAcNoCode() {
    return this.acNoCode == null ? "" : this.acNoCode;
  }

/**
	* 科目代號<br>
	* 
  *
  * @param acNoCode 科目代號
	*/
  public void setAcNoCode(String acNoCode) {
    this.acNoCode = acNoCode;
  }

/**
	* 子目代號<br>
	* 
	* @return String
	*/
  public String getAcSubCode() {
    return this.acSubCode == null ? "" : this.acSubCode;
  }

/**
	* 子目代號<br>
	* 
  *
  * @param acSubCode 子目代號
	*/
  public void setAcSubCode(String acSubCode) {
    this.acSubCode = acSubCode;
  }

/**
	* 會計日期<br>
	* 西元年月日
	* @return Integer
	*/
  public int getAcDate() {
    return  StaticTool.bcToRoc(this.acDate);
  }

/**
	* 會計日期<br>
	* 西元年月日
  *
  * @param acDate 會計日期
  * @throws LogicException when Date Is Warn	*/
  public void setAcDate(int acDate) throws LogicException {
    this.acDate = StaticTool.rocToBc(acDate);
  }


  @Override
  public int hashCode() {
    return Objects.hash(acBookCode, acSubBookCode, currencyCode, acNoCode, acSubCode, acDate);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    CoreAcMainId coreAcMainId = (CoreAcMainId) obj;
    return acBookCode.equals(coreAcMainId.acBookCode) && acSubBookCode.equals(coreAcMainId.acSubBookCode) && currencyCode.equals(coreAcMainId.currencyCode) && acNoCode.equals(coreAcMainId.acNoCode) && acSubCode.equals(coreAcMainId.acSubCode) && acDate == coreAcMainId.acDate;
  }

  @Override
  public String toString() {
    return "CoreAcMainId [acBookCode=" + acBookCode + ", acSubBookCode=" + acSubBookCode + ", currencyCode=" + currencyCode + ", acNoCode=" + acNoCode + ", acSubCode=" + acSubCode + ", acDate=" + acDate + "]";
  }
}
