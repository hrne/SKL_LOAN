package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * JcicB093 聯徵動產及貴重物品擔保品明細檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicB093Id implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 1462770456524694173L;

// 資料日期
  @Column(name = "`DataYM`")
  private int dataYM = 0;

  // 擔保品控制編碼
  /* Key,左靠右補空白 */
  @Column(name = "`ClActNo`", length = 50)
  private String clActNo = " ";

  public JcicB093Id() {
  }

  public JcicB093Id(int dataYM, String clActNo) {
    this.dataYM = dataYM;
    this.clActNo = clActNo;
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


  @Override
  public int hashCode() {
    return Objects.hash(dataYM, clActNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    JcicB093Id jcicB093Id = (JcicB093Id) obj;
    return dataYM == jcicB093Id.dataYM && clActNo.equals(jcicB093Id.clActNo);
  }

  @Override
  public String toString() {
    return "JcicB093Id [dataYM=" + dataYM + ", clActNo=" + clActNo + "]";
  }
}
