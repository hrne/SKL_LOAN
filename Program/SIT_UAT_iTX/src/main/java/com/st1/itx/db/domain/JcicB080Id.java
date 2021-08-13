package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * JcicB080 聯徵授信額度資料檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicB080Id implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 723310786438372802L;

// 資料年月
  @Column(name = "`DataYM`")
  private int dataYM = 0;

  // 總行代號
  /* Key,金融機構總機構之代號，三位數字 */
  @Column(name = "`BankItem`", length = 3)
  private String bankItem = " ";

  // 本階共用額度控制編碼
  /* Key,左靠，填報本階共用額度控制編碼（即授信額度月報之第28欄「上階共用額度控制編碼」），該編碼在同一金融機構內需為唯一(不可重複) */
  @Column(name = "`FacmNo`", length = 50)
  private String facmNo = " ";

  public JcicB080Id() {
  }

  public JcicB080Id(int dataYM, String bankItem, String facmNo) {
    this.dataYM = dataYM;
    this.bankItem = bankItem;
    this.facmNo = facmNo;
  }

/**
	* 資料年月<br>
	* 
	* @return Integer
	*/
  public int getDataYM() {
    return this.dataYM;
  }

/**
	* 資料年月<br>
	* 
  *
  * @param dataYM 資料年月
	*/
  public void setDataYM(int dataYM) {
    this.dataYM = dataYM;
  }

/**
	* 總行代號<br>
	* Key,金融機構總機構之代號，三位數字
	* @return String
	*/
  public String getBankItem() {
    return this.bankItem == null ? "" : this.bankItem;
  }

/**
	* 總行代號<br>
	* Key,金融機構總機構之代號，三位數字
  *
  * @param bankItem 總行代號
	*/
  public void setBankItem(String bankItem) {
    this.bankItem = bankItem;
  }

/**
	* 本階共用額度控制編碼<br>
	* Key,左靠，填報本階共用額度控制編碼（即授信額度月報之第28欄「上階共用額度控制編碼」），該編碼在同一金融機構內需為唯一(不可重複)
	* @return String
	*/
  public String getFacmNo() {
    return this.facmNo == null ? "" : this.facmNo;
  }

/**
	* 本階共用額度控制編碼<br>
	* Key,左靠，填報本階共用額度控制編碼（即授信額度月報之第28欄「上階共用額度控制編碼」），該編碼在同一金融機構內需為唯一(不可重複)
  *
  * @param facmNo 本階共用額度控制編碼
	*/
  public void setFacmNo(String facmNo) {
    this.facmNo = facmNo;
  }


  @Override
  public int hashCode() {
    return Objects.hash(dataYM, bankItem, facmNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    JcicB080Id jcicB080Id = (JcicB080Id) obj;
    return dataYM == jcicB080Id.dataYM && bankItem.equals(jcicB080Id.bankItem) && facmNo.equals(jcicB080Id.facmNo);
  }

  @Override
  public String toString() {
    return "JcicB080Id [dataYM=" + dataYM + ", bankItem=" + bankItem + ", facmNo=" + facmNo + "]";
  }
}
