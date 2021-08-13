package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * Ias39LGD 違約損失率檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class Ias39LGDId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 844353410177711821L;

// 生效日期
  @Column(name = "`Date`")
  private int date = 0;

  // 類別
  @Column(name = "`Type`", length = 2)
  private String type = " ";

  public Ias39LGDId() {
  }

  public Ias39LGDId(int date, String type) {
    this.date = date;
    this.type = type;
  }

/**
	* 生效日期<br>
	* 
	* @return Integer
	*/
  public int getDate() {
    return  StaticTool.bcToRoc(this.date);
  }

/**
	* 生效日期<br>
	* 
  *
  * @param date 生效日期
  * @throws LogicException when Date Is Warn	*/
  public void setDate(int date) throws LogicException {
    this.date = StaticTool.rocToBc(date);
  }

/**
	* 類別<br>
	* 
	* @return String
	*/
  public String getType() {
    return this.type == null ? "" : this.type;
  }

/**
	* 類別<br>
	* 
  *
  * @param type 類別
	*/
  public void setType(String type) {
    this.type = type;
  }


  @Override
  public int hashCode() {
    return Objects.hash(date, type);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    Ias39LGDId ias39LGDId = (Ias39LGDId) obj;
    return date == ias39LGDId.date && type.equals(ias39LGDId.type);
  }

  @Override
  public String toString() {
    return "Ias39LGDId [date=" + date + ", type=" + type + "]";
  }
}
