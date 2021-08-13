package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * JcicB680 貸款餘額扣除擔保品鑑估值之金額資料檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicB680Id implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 6265884727123211668L;

// 資料日期
  @Column(name = "`DataYM`")
  private int dataYM = 0;

  // 交易代碼
  /* A新增 C異動 D刪除 */
  @Column(name = "`TranCode`", length = 1)
  private String tranCode = " ";

  // 授信戶IDN/BAN
  /* Key,左靠，身份證或統一證號 */
  @Column(name = "`CustId`", length = 10)
  private String custId = " ";

  // 借款人戶號
  /* 戶號(KEY) */
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 額度編號
  /* 呆帳明細資料(KEY)，其他彙計資料放0 */
  @Column(name = "`FacmNo`")
  private int facmNo = 0;

  // 撥款序號
  /* 呆帳明細資料(KEY)，其他彙計資料放0 */
  @Column(name = "`BormNo`")
  private int bormNo = 0;

  public JcicB680Id() {
  }

  public JcicB680Id(int dataYM, String tranCode, String custId, int custNo, int facmNo, int bormNo) {
    this.dataYM = dataYM;
    this.tranCode = tranCode;
    this.custId = custId;
    this.custNo = custNo;
    this.facmNo = facmNo;
    this.bormNo = bormNo;
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
	* 交易代碼<br>
	* A新增 C異動 D刪除
	* @return String
	*/
  public String getTranCode() {
    return this.tranCode == null ? "" : this.tranCode;
  }

/**
	* 交易代碼<br>
	* A新增 C異動 D刪除
  *
  * @param tranCode 交易代碼
	*/
  public void setTranCode(String tranCode) {
    this.tranCode = tranCode;
  }

/**
	* 授信戶IDN/BAN<br>
	* Key,左靠，身份證或統一證號
	* @return String
	*/
  public String getCustId() {
    return this.custId == null ? "" : this.custId;
  }

/**
	* 授信戶IDN/BAN<br>
	* Key,左靠，身份證或統一證號
  *
  * @param custId 授信戶IDN/BAN
	*/
  public void setCustId(String custId) {
    this.custId = custId;
  }

/**
	* 借款人戶號<br>
	* 戶號(KEY)
	* @return Integer
	*/
  public int getCustNo() {
    return this.custNo;
  }

/**
	* 借款人戶號<br>
	* 戶號(KEY)
  *
  * @param custNo 借款人戶號
	*/
  public void setCustNo(int custNo) {
    this.custNo = custNo;
  }

/**
	* 額度編號<br>
	* 呆帳明細資料(KEY)，其他彙計資料放0
	* @return Integer
	*/
  public int getFacmNo() {
    return this.facmNo;
  }

/**
	* 額度編號<br>
	* 呆帳明細資料(KEY)，其他彙計資料放0
  *
  * @param facmNo 額度編號
	*/
  public void setFacmNo(int facmNo) {
    this.facmNo = facmNo;
  }

/**
	* 撥款序號<br>
	* 呆帳明細資料(KEY)，其他彙計資料放0
	* @return Integer
	*/
  public int getBormNo() {
    return this.bormNo;
  }

/**
	* 撥款序號<br>
	* 呆帳明細資料(KEY)，其他彙計資料放0
  *
  * @param bormNo 撥款序號
	*/
  public void setBormNo(int bormNo) {
    this.bormNo = bormNo;
  }


  @Override
  public int hashCode() {
    return Objects.hash(dataYM, tranCode, custId, custNo, facmNo, bormNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    JcicB680Id jcicB680Id = (JcicB680Id) obj;
    return dataYM == jcicB680Id.dataYM && tranCode.equals(jcicB680Id.tranCode) && custId.equals(jcicB680Id.custId) && custNo == jcicB680Id.custNo && facmNo == jcicB680Id.facmNo && bormNo == jcicB680Id.bormNo;
  }

  @Override
  public String toString() {
    return "JcicB680Id [dataYM=" + dataYM + ", tranCode=" + tranCode + ", custId=" + custId + ", custNo=" + custNo + ", facmNo=" + facmNo + ", bormNo=" + bormNo + "]";
  }
}
