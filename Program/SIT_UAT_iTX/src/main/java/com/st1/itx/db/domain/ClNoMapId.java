package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * ClNoMap 擔保品編號新舊對照檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class ClNoMapId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -4178241152593433010L;

// 原擔保品代號1
  /* 原擔保品 */
  @Column(name = "`GDRID1`")
  private int gDRID1 = 0;

  // 原擔保品代號2
  /* 原擔保品 */
  @Column(name = "`GDRID2`")
  private int gDRID2 = 0;

  // 原擔保品編號
  /* 原擔保品 */
  @Column(name = "`GDRNUM`")
  private int gDRNUM = 0;

  // 原擔保品序號
  /* 原擔保品 */
  @Column(name = "`LGTSEQ`")
  private int lGTSEQ = 0;

  public ClNoMapId() {
  }

  public ClNoMapId(int gDRID1, int gDRID2, int gDRNUM, int lGTSEQ) {
    this.gDRID1 = gDRID1;
    this.gDRID2 = gDRID2;
    this.gDRNUM = gDRNUM;
    this.lGTSEQ = lGTSEQ;
  }

/**
	* 原擔保品代號1<br>
	* 原擔保品
	* @return Integer
	*/
  public int getGDRID1() {
    return this.gDRID1;
  }

/**
	* 原擔保品代號1<br>
	* 原擔保品
  *
  * @param gDRID1 原擔保品代號1
	*/
  public void setGDRID1(int gDRID1) {
    this.gDRID1 = gDRID1;
  }

/**
	* 原擔保品代號2<br>
	* 原擔保品
	* @return Integer
	*/
  public int getGDRID2() {
    return this.gDRID2;
  }

/**
	* 原擔保品代號2<br>
	* 原擔保品
  *
  * @param gDRID2 原擔保品代號2
	*/
  public void setGDRID2(int gDRID2) {
    this.gDRID2 = gDRID2;
  }

/**
	* 原擔保品編號<br>
	* 原擔保品
	* @return Integer
	*/
  public int getGDRNUM() {
    return this.gDRNUM;
  }

/**
	* 原擔保品編號<br>
	* 原擔保品
  *
  * @param gDRNUM 原擔保品編號
	*/
  public void setGDRNUM(int gDRNUM) {
    this.gDRNUM = gDRNUM;
  }

/**
	* 原擔保品序號<br>
	* 原擔保品
	* @return Integer
	*/
  public int getLGTSEQ() {
    return this.lGTSEQ;
  }

/**
	* 原擔保品序號<br>
	* 原擔保品
  *
  * @param lGTSEQ 原擔保品序號
	*/
  public void setLGTSEQ(int lGTSEQ) {
    this.lGTSEQ = lGTSEQ;
  }


  @Override
  public int hashCode() {
    return Objects.hash(gDRID1, gDRID2, gDRNUM, lGTSEQ);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    ClNoMapId clNoMapId = (ClNoMapId) obj;
    return gDRID1 == clNoMapId.gDRID1 && gDRID2 == clNoMapId.gDRID2 && gDRNUM == clNoMapId.gDRNUM && lGTSEQ == clNoMapId.lGTSEQ;
  }

  @Override
  public String toString() {
    return "ClNoMapId [gDRID1=" + gDRID1 + ", gDRID2=" + gDRID2 + ", gDRNUM=" + gDRNUM + ", lGTSEQ=" + lGTSEQ + "]";
  }
}
