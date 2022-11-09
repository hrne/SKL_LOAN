package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * AcReceivable 會計銷帳檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class AcReceivableId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 8626321893687912764L;

// 業務科目代號
  /* CdAcCode會計科子細目設定檔 */
  @Column(name = "`AcctCode`", length = 3)
  private String acctCode = " ";

  // 戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 額度編號
  @Column(name = "`FacmNo`")
  private int facmNo = 0;

  // 銷帳編號
  /* 1:暫收款－可抵繳 : ''primary key 不可有null, 放一個空白2:擔保放款、催收款項 : 撥款序號(3)3:會計銷帳科目：系統自編(AC+西元年後兩碼+流水號六碼)4:暫收款－支票：支票帳號(9)-支票號碼(7) 5:未收帳管費：第一筆撥款序號(3) 6:未收契變手續費：契變日期(8,西元)+契變序號(02)7:未收、暫收、暫付、催收火險保費：原保單號碼(17)+批單號碼(1)8:暫付、催收法務費：記錄號碼(8)9:短繳期金：撥款序號(3)10:暫收款－借新還舊: 'FacmNo' + 額度編號(3)11:暫收款-冲正：''11:聯貸手續費:SL-費用代號(2)-流水號(3)-攤提年月(YYYMM) */
  @Column(name = "`RvNo`", length = 30)
  private String rvNo = " ";

  public AcReceivableId() {
  }

  public AcReceivableId(String acctCode, int custNo, int facmNo, String rvNo) {
    this.acctCode = acctCode;
    this.custNo = custNo;
    this.facmNo = facmNo;
    this.rvNo = rvNo;
  }

/**
	* 業務科目代號<br>
	* CdAcCode會計科子細目設定檔
	* @return String
	*/
  public String getAcctCode() {
    return this.acctCode == null ? "" : this.acctCode;
  }

/**
	* 業務科目代號<br>
	* CdAcCode會計科子細目設定檔
  *
  * @param acctCode 業務科目代號
	*/
  public void setAcctCode(String acctCode) {
    this.acctCode = acctCode;
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
	* 額度編號<br>
	* 
	* @return Integer
	*/
  public int getFacmNo() {
    return this.facmNo;
  }

/**
	* 額度編號<br>
	* 
  *
  * @param facmNo 額度編號
	*/
  public void setFacmNo(int facmNo) {
    this.facmNo = facmNo;
  }

/**
	* 銷帳編號<br>
	* 1:暫收款－可抵繳 : ''primary key 不可有null, 放一個空白
2:擔保放款、催收款項 : 撥款序號(3)
3:會計銷帳科目：系統自編(AC+西元年後兩碼+流水號六碼)
4:暫收款－支票：支票帳號(9)-支票號碼(7) 
5:未收帳管費：第一筆撥款序號(3) 
6:未收契變手續費：契變日期(8,西元)+契變序號(02)
7:未收、暫收、暫付、催收火險保費：原保單號碼(17)+批單號碼(1)
8:暫付、催收法務費：記錄號碼(8)
9:短繳期金：撥款序號(3)
10:暫收款－借新還舊: 'FacmNo' + 額度編號(3)
11:暫收款-冲正：''
11:聯貸手續費:SL-費用代號(2)-流水號(3)-攤提年月(YYYMM)
	* @return String
	*/
  public String getRvNo() {
    return this.rvNo == null ? "" : this.rvNo;
  }

/**
	* 銷帳編號<br>
	* 1:暫收款－可抵繳 : ''primary key 不可有null, 放一個空白
2:擔保放款、催收款項 : 撥款序號(3)
3:會計銷帳科目：系統自編(AC+西元年後兩碼+流水號六碼)
4:暫收款－支票：支票帳號(9)-支票號碼(7) 
5:未收帳管費：第一筆撥款序號(3) 
6:未收契變手續費：契變日期(8,西元)+契變序號(02)
7:未收、暫收、暫付、催收火險保費：原保單號碼(17)+批單號碼(1)
8:暫付、催收法務費：記錄號碼(8)
9:短繳期金：撥款序號(3)
10:暫收款－借新還舊: 'FacmNo' + 額度編號(3)
11:暫收款-冲正：''
11:聯貸手續費:SL-費用代號(2)-流水號(3)-攤提年月(YYYMM)
  *
  * @param rvNo 銷帳編號
	*/
  public void setRvNo(String rvNo) {
    this.rvNo = rvNo;
  }


  @Override
  public int hashCode() {
    return Objects.hash(acctCode, custNo, facmNo, rvNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    AcReceivableId acReceivableId = (AcReceivableId) obj;
    return acctCode.equals(acReceivableId.acctCode) && custNo == acReceivableId.custNo && facmNo == acReceivableId.facmNo && rvNo.equals(acReceivableId.rvNo);
  }

  @Override
  public String toString() {
    return "AcReceivableId [acctCode=" + acctCode + ", custNo=" + custNo + ", facmNo=" + facmNo + ", rvNo=" + rvNo + "]";
  }
}
