package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * CdGseq 編號編碼檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class CdGseqId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -458861909768246334L;

// 編號日期
  /* 年度編號時月日為0，月份編號時日為0 */
  @Column(name = "`GseqDate`")
  private int gseqDate = 0;

  // 編號方式
  /* 0:不分1:年度編號2:月份編號3:日編號 */
  @Column(name = "`GseqCode`")
  private int gseqCode = 0;

  // 業務類別
  /* 業務自行編制   例：L2-業務作業 */
  @Column(name = "`GseqType`", length = 2)
  private String gseqType = " ";

  // 交易種類
  /* 業務自行編制   例：GseqType="L2"0001:戶號0002:案件申請編號 */
  @Column(name = "`GseqKind`", length = 4)
  private String gseqKind = " ";

  public CdGseqId() {
  }

  public CdGseqId(int gseqDate, int gseqCode, String gseqType, String gseqKind) {
    this.gseqDate = gseqDate;
    this.gseqCode = gseqCode;
    this.gseqType = gseqType;
    this.gseqKind = gseqKind;
  }

/**
	* 編號日期<br>
	* 年度編號時月日為0，月份編號時日為0
	* @return Integer
	*/
  public int getGseqDate() {
    return this.gseqDate;
  }

/**
	* 編號日期<br>
	* 年度編號時月日為0，月份編號時日為0
  *
  * @param gseqDate 編號日期
	*/
  public void setGseqDate(int gseqDate) {
    this.gseqDate = gseqDate;
  }

/**
	* 編號方式<br>
	* 0:不分
1:年度編號
2:月份編號
3:日編號
	* @return Integer
	*/
  public int getGseqCode() {
    return this.gseqCode;
  }

/**
	* 編號方式<br>
	* 0:不分
1:年度編號
2:月份編號
3:日編號
  *
  * @param gseqCode 編號方式
	*/
  public void setGseqCode(int gseqCode) {
    this.gseqCode = gseqCode;
  }

/**
	* 業務類別<br>
	* 業務自行編制   例：L2-業務作業
	* @return String
	*/
  public String getGseqType() {
    return this.gseqType == null ? "" : this.gseqType;
  }

/**
	* 業務類別<br>
	* 業務自行編制   例：L2-業務作業
  *
  * @param gseqType 業務類別
	*/
  public void setGseqType(String gseqType) {
    this.gseqType = gseqType;
  }

/**
	* 交易種類<br>
	* 業務自行編制   
例：
GseqType="L2"
0001:戶號
0002:案件申請編號
	* @return String
	*/
  public String getGseqKind() {
    return this.gseqKind == null ? "" : this.gseqKind;
  }

/**
	* 交易種類<br>
	* 業務自行編制   
例：
GseqType="L2"
0001:戶號
0002:案件申請編號
  *
  * @param gseqKind 交易種類
	*/
  public void setGseqKind(String gseqKind) {
    this.gseqKind = gseqKind;
  }


  @Override
  public int hashCode() {
    return Objects.hash(gseqDate, gseqCode, gseqType, gseqKind);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    CdGseqId cdGseqId = (CdGseqId) obj;
    return gseqDate == cdGseqId.gseqDate && gseqCode == cdGseqId.gseqCode && gseqType.equals(cdGseqId.gseqType) && gseqKind.equals(cdGseqId.gseqKind);
  }

  @Override
  public String toString() {
    return "CdGseqId [gseqDate=" + gseqDate + ", gseqCode=" + gseqCode + ", gseqType=" + gseqType + ", gseqKind=" + gseqKind + "]";
  }
}
