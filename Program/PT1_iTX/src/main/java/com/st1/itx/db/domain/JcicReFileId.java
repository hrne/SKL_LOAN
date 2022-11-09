package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * JcicReFile 聯徵回寫紀錄檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicReFileId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -6349169011282887036L;

// 報送單位代號
  /* 3位文數字 */
  @Column(name = "`SubmitKey`", length = 3)
  private String submitKey = " ";

  // 報送日期
  /* 西元年月日 */
  @Column(name = "`JcicDate`")
  private int jcicDate = 0;

  public JcicReFileId() {
  }

  public JcicReFileId(String submitKey, int jcicDate) {
    this.submitKey = submitKey;
    this.jcicDate = jcicDate;
  }

/**
	* 報送單位代號<br>
	* 3位文數字
	* @return String
	*/
  public String getSubmitKey() {
    return this.submitKey == null ? "" : this.submitKey;
  }

/**
	* 報送單位代號<br>
	* 3位文數字
  *
  * @param submitKey 報送單位代號
	*/
  public void setSubmitKey(String submitKey) {
    this.submitKey = submitKey;
  }

/**
	* 報送日期<br>
	* 西元年月日
	* @return Integer
	*/
  public int getJcicDate() {
    return  StaticTool.bcToRoc(this.jcicDate);
  }

/**
	* 報送日期<br>
	* 西元年月日
  *
  * @param jcicDate 報送日期
  * @throws LogicException when Date Is Warn	*/
  public void setJcicDate(int jcicDate) throws LogicException {
    this.jcicDate = StaticTool.rocToBc(jcicDate);
  }


  @Override
  public int hashCode() {
    return Objects.hash(submitKey, jcicDate);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    JcicReFileId jcicReFileId = (JcicReFileId) obj;
    return submitKey.equals(jcicReFileId.submitKey) && jcicDate == jcicReFileId.jcicDate;
  }

  @Override
  public String toString() {
    return "JcicReFileId [submitKey=" + submitKey + ", jcicDate=" + jcicDate + "]";
  }
}
