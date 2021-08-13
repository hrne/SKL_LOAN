package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * JcicB095 聯徵不動產擔保品明細-建號附加檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicB095Id implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -8814405770150961051L;

// 資料日期
  @Column(name = "`DataYM`")
  private int dataYM = 0;

  // 擔保品控制編碼
  /* Key,左靠右補空白 */
  @Column(name = "`ClActNo`", length = 50)
  private String clActNo = " ";

  // 擔保品所有權人或代表人IDN/BAN
  /* 左靠，擔保品所有權人或代表人身份證或統一證號 */
  @Column(name = "`OwnerId`", length = 10)
  private String ownerId = " ";

  // 縣市別
  /* Key, */
  @Column(name = "`CityJCICCode`", length = 1)
  private String cityJCICCode = " ";

  // 鄉鎮市區別
  /* Key, */
  @Column(name = "`AreaJCICCode`")
  private int areaJCICCode = 0;

  // 段、小段號
  /* Key, */
  @Column(name = "`IrCode`", length = 4)
  private String irCode = " ";

  // 建號-前五碼
  /* Key, */
  @Column(name = "`BdNo1`")
  private int bdNo1 = 0;

  // 建號-後三碼
  /* Key, */
  @Column(name = "`BdNo2`")
  private int bdNo2 = 0;

  public JcicB095Id() {
  }

  public JcicB095Id(int dataYM, String clActNo, String ownerId, String cityJCICCode, int areaJCICCode, String irCode, int bdNo1, int bdNo2) {
    this.dataYM = dataYM;
    this.clActNo = clActNo;
    this.ownerId = ownerId;
    this.cityJCICCode = cityJCICCode;
    this.areaJCICCode = areaJCICCode;
    this.irCode = irCode;
    this.bdNo1 = bdNo1;
    this.bdNo2 = bdNo2;
  }

/**
	* 資料日期<br>
	* 
	* @return Integer
	*/
  public int getDataYM() {
    return this.dataYM;
  }

/**
	* 資料日期<br>
	* 
  *
  * @param dataYM 資料日期
	*/
  public void setDataYM(int dataYM) {
    this.dataYM = dataYM;
  }

/**
	* 擔保品控制編碼<br>
	* Key,左靠右補空白
	* @return String
	*/
  public String getClActNo() {
    return this.clActNo == null ? "" : this.clActNo;
  }

/**
	* 擔保品控制編碼<br>
	* Key,左靠右補空白
  *
  * @param clActNo 擔保品控制編碼
	*/
  public void setClActNo(String clActNo) {
    this.clActNo = clActNo;
  }

/**
	* 擔保品所有權人或代表人IDN/BAN<br>
	* 左靠，擔保品所有權人或代表人身份證或統一證號
	* @return String
	*/
  public String getOwnerId() {
    return this.ownerId == null ? "" : this.ownerId;
  }

/**
	* 擔保品所有權人或代表人IDN/BAN<br>
	* 左靠，擔保品所有權人或代表人身份證或統一證號
  *
  * @param ownerId 擔保品所有權人或代表人IDN/BAN
	*/
  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }

/**
	* 縣市別<br>
	* Key,
	* @return String
	*/
  public String getCityJCICCode() {
    return this.cityJCICCode == null ? "" : this.cityJCICCode;
  }

/**
	* 縣市別<br>
	* Key,
  *
  * @param cityJCICCode 縣市別
	*/
  public void setCityJCICCode(String cityJCICCode) {
    this.cityJCICCode = cityJCICCode;
  }

/**
	* 鄉鎮市區別<br>
	* Key,
	* @return Integer
	*/
  public int getAreaJCICCode() {
    return this.areaJCICCode;
  }

/**
	* 鄉鎮市區別<br>
	* Key,
  *
  * @param areaJCICCode 鄉鎮市區別
	*/
  public void setAreaJCICCode(int areaJCICCode) {
    this.areaJCICCode = areaJCICCode;
  }

/**
	* 段、小段號<br>
	* Key,
	* @return String
	*/
  public String getIrCode() {
    return this.irCode == null ? "" : this.irCode;
  }

/**
	* 段、小段號<br>
	* Key,
  *
  * @param irCode 段、小段號
	*/
  public void setIrCode(String irCode) {
    this.irCode = irCode;
  }

/**
	* 建號-前五碼<br>
	* Key,
	* @return Integer
	*/
  public int getBdNo1() {
    return this.bdNo1;
  }

/**
	* 建號-前五碼<br>
	* Key,
  *
  * @param bdNo1 建號-前五碼
	*/
  public void setBdNo1(int bdNo1) {
    this.bdNo1 = bdNo1;
  }

/**
	* 建號-後三碼<br>
	* Key,
	* @return Integer
	*/
  public int getBdNo2() {
    return this.bdNo2;
  }

/**
	* 建號-後三碼<br>
	* Key,
  *
  * @param bdNo2 建號-後三碼
	*/
  public void setBdNo2(int bdNo2) {
    this.bdNo2 = bdNo2;
  }


  @Override
  public int hashCode() {
    return Objects.hash(dataYM, clActNo, ownerId, cityJCICCode, areaJCICCode, irCode, bdNo1, bdNo2);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    JcicB095Id jcicB095Id = (JcicB095Id) obj;
    return dataYM == jcicB095Id.dataYM && clActNo.equals(jcicB095Id.clActNo) && ownerId.equals(jcicB095Id.ownerId) && cityJCICCode.equals(jcicB095Id.cityJCICCode) && areaJCICCode == jcicB095Id.areaJCICCode && irCode.equals(jcicB095Id.irCode) && bdNo1 == jcicB095Id.bdNo1 && bdNo2 == jcicB095Id.bdNo2;
  }

  @Override
  public String toString() {
    return "JcicB095Id [dataYM=" + dataYM + ", clActNo=" + clActNo + ", ownerId=" + ownerId + ", cityJCICCode=" + cityJCICCode + ", areaJCICCode=" + areaJCICCode + ", irCode=" + irCode + ", bdNo1=" + bdNo1 + ", bdNo2=" + bdNo2 + "]";
  }
}
