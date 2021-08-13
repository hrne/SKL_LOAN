package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * CdAcBook 帳冊別金額設定檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class CdAcBookId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 6824993379958922482L;

// 帳冊別
  /* 共用代碼檔       000：全公司 */
  @Column(name = "`AcBookCode`", length = 3)
  private String acBookCode = " ";

  // 區隔帳冊
  /* 共用代碼檔       201:利變年金帳冊 */
  @Column(name = "`AcSubBookCode`", length = 3)
  private String acSubBookCode = " ";

  public CdAcBookId() {
  }

  public CdAcBookId(String acBookCode, String acSubBookCode) {
    this.acBookCode = acBookCode;
    this.acSubBookCode = acSubBookCode;
  }

/**
	* 帳冊別<br>
	* 共用代碼檔       000：全公司
	* @return String
	*/
  public String getAcBookCode() {
    return this.acBookCode == null ? "" : this.acBookCode;
  }

/**
	* 帳冊別<br>
	* 共用代碼檔       000：全公司
  *
  * @param acBookCode 帳冊別
	*/
  public void setAcBookCode(String acBookCode) {
    this.acBookCode = acBookCode;
  }

/**
	* 區隔帳冊<br>
	* 共用代碼檔       201:利變年金帳冊
	* @return String
	*/
  public String getAcSubBookCode() {
    return this.acSubBookCode == null ? "" : this.acSubBookCode;
  }

/**
	* 區隔帳冊<br>
	* 共用代碼檔       201:利變年金帳冊
  *
  * @param acSubBookCode 區隔帳冊
	*/
  public void setAcSubBookCode(String acSubBookCode) {
    this.acSubBookCode = acSubBookCode;
  }


  @Override
  public int hashCode() {
    return Objects.hash(acBookCode, acSubBookCode);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    CdAcBookId cdAcBookId = (CdAcBookId) obj;
    return acBookCode.equals(cdAcBookId.acBookCode) && acSubBookCode.equals(cdAcBookId.acSubBookCode);
  }

  @Override
  public String toString() {
    return "CdAcBookId [acBookCode=" + acBookCode + ", acSubBookCode=" + acSubBookCode + "]";
  }
}
