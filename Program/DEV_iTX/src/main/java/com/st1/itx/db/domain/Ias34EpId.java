package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Ias34Ep IAS34欄位清單E檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class Ias34EpId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -384707364378409771L;

// 資料時點(年月)
  /* YYYYMM */
  @Column(name = "`DataYM`")
  private int dataYM = 0;

  // 戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 額度編號(核准號碼)
  @Column(name = "`FacmNo`")
  private int facmNo = 0;

  // 撥款序號
  @Column(name = "`BormNo`")
  private int bormNo = 0;

  // 會計科目
  @Column(name = "`AcCode`", length = 11)
  private String acCode = " ";

  // 狀態
  /* 辨識是否為帳上客戶或為轉呆客戶1=帳上客戶；2=轉呆客戶； */
  @Column(name = "`Status`")
  private int status = 0;

  public Ias34EpId() {
  }

  public Ias34EpId(int dataYM, int custNo, int facmNo, int bormNo, String acCode, int status) {
    this.dataYM = dataYM;
    this.custNo = custNo;
    this.facmNo = facmNo;
    this.bormNo = bormNo;
    this.acCode = acCode;
    this.status = status;
  }

/**
	* 資料時點(年月)<br>
	* YYYYMM
	* @return Integer
	*/
  public int getDataYM() {
    return this.dataYM;
  }

/**
	* 資料時點(年月)<br>
	* YYYYMM
  *
  * @param dataYM 資料時點(年月)
	*/
  public void setDataYM(int dataYM) {
    this.dataYM = dataYM;
  }

/**
	* 戶號<br>
	* 
	* @return Integer
	*/
  public int getCustNo() {
    return this.custNo;
  }

/**
	* 戶號<br>
	* 
  *
  * @param custNo 戶號
	*/
  public void setCustNo(int custNo) {
    this.custNo = custNo;
  }

/**
	* 額度編號(核准號碼)<br>
	* 
	* @return Integer
	*/
  public int getFacmNo() {
    return this.facmNo;
  }

/**
	* 額度編號(核准號碼)<br>
	* 
  *
  * @param facmNo 額度編號(核准號碼)
	*/
  public void setFacmNo(int facmNo) {
    this.facmNo = facmNo;
  }

/**
	* 撥款序號<br>
	* 
	* @return Integer
	*/
  public int getBormNo() {
    return this.bormNo;
  }

/**
	* 撥款序號<br>
	* 
  *
  * @param bormNo 撥款序號
	*/
  public void setBormNo(int bormNo) {
    this.bormNo = bormNo;
  }

/**
	* 會計科目<br>
	* 
	* @return String
	*/
  public String getAcCode() {
    return this.acCode == null ? "" : this.acCode;
  }

/**
	* 會計科目<br>
	* 
  *
  * @param acCode 會計科目
	*/
  public void setAcCode(String acCode) {
    this.acCode = acCode;
  }

/**
	* 狀態<br>
	* 辨識是否為帳上客戶或為轉呆客戶
1=帳上客戶；
2=轉呆客戶；
	* @return Integer
	*/
  public int getStatus() {
    return this.status;
  }

/**
	* 狀態<br>
	* 辨識是否為帳上客戶或為轉呆客戶
1=帳上客戶；
2=轉呆客戶；
  *
  * @param status 狀態
	*/
  public void setStatus(int status) {
    this.status = status;
  }


  @Override
  public int hashCode() {
    return Objects.hash(dataYM, custNo, facmNo, bormNo, acCode, status);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    Ias34EpId ias34EpId = (Ias34EpId) obj;
    return dataYM == ias34EpId.dataYM && custNo == ias34EpId.custNo && facmNo == ias34EpId.facmNo && bormNo == ias34EpId.bormNo && acCode.equals(ias34EpId.acCode) && status == ias34EpId.status;
  }

  @Override
  public String toString() {
    return "Ias34EpId [dataYM=" + dataYM + ", custNo=" + custNo + ", facmNo=" + facmNo + ", bormNo=" + bormNo + ", acCode=" + acCode + ", status=" + status + "]";
  }
}
