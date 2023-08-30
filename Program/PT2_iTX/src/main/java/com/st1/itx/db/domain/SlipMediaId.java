package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * SlipMedia 傳票媒體檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class SlipMediaId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 5708026316547809629L;

// 單位別
  @Column(name = "`BranchNo`", length = 4)
  private String branchNo = " ";

  // 會計日期
  @Column(name = "`AcDate`")
  private int acDate = 0;

  // 傳票批號
  @Column(name = "`BatchNo`")
  private int batchNo = 0;

  // 帳冊別代碼
  @Column(name = "`AcBookCode`", length = 3)
  private String acBookCode = " ";

  // 媒體檔上傳序號
  /* 同單位別、會計日期、傳票批號時，根據帳冊別代碼依序給予序號ROW_NUMBER() OVER (PARTITION BY BranchNo,AcDate,BatchNo ORDER BY AcBookCode)*要注意重複產生傳票媒體時,應先取最後一筆序號為基數 */
  @Column(name = "`MediaSeq`")
  private int mediaSeq = 0;

  // 媒體檔傳票號碼
  /* F10+民國年+月份(1碼)+日期+3碼序號*3碼序號，從CdGSeq取號 */
  @Column(name = "`MediaSlipNo`", length = 12)
  private String mediaSlipNo = " ";

  // 傳票序號
  @Column(name = "`Seq`")
  private int seq = 0;

  public SlipMediaId() {
  }

  public SlipMediaId(String branchNo, int acDate, int batchNo, String acBookCode, int mediaSeq, String mediaSlipNo, int seq) {
    this.branchNo = branchNo;
    this.acDate = acDate;
    this.batchNo = batchNo;
    this.acBookCode = acBookCode;
    this.mediaSeq = mediaSeq;
    this.mediaSlipNo = mediaSlipNo;
    this.seq = seq;
  }

/**
	* 單位別<br>
	* 
	* @return String
	*/
  public String getBranchNo() {
    return this.branchNo == null ? "" : this.branchNo;
  }

/**
	* 單位別<br>
	* 
  *
  * @param branchNo 單位別
	*/
  public void setBranchNo(String branchNo) {
    this.branchNo = branchNo;
  }

/**
	* 會計日期<br>
	* 
	* @return Integer
	*/
  public int getAcDate() {
    return  StaticTool.bcToRoc(this.acDate);
  }

/**
	* 會計日期<br>
	* 
  *
  * @param acDate 會計日期
  * @throws LogicException when Date Is Warn	*/
  public void setAcDate(int acDate) throws LogicException {
    this.acDate = StaticTool.rocToBc(acDate);
  }

/**
	* 傳票批號<br>
	* 
	* @return Integer
	*/
  public int getBatchNo() {
    return this.batchNo;
  }

/**
	* 傳票批號<br>
	* 
  *
  * @param batchNo 傳票批號
	*/
  public void setBatchNo(int batchNo) {
    this.batchNo = batchNo;
  }

/**
	* 帳冊別代碼<br>
	* 
	* @return String
	*/
  public String getAcBookCode() {
    return this.acBookCode == null ? "" : this.acBookCode;
  }

/**
	* 帳冊別代碼<br>
	* 
  *
  * @param acBookCode 帳冊別代碼
	*/
  public void setAcBookCode(String acBookCode) {
    this.acBookCode = acBookCode;
  }

/**
	* 媒體檔上傳序號<br>
	* 同單位別、會計日期、傳票批號時，根據帳冊別代碼依序給予序號
ROW_NUMBER() OVER (
PARTITION BY BranchNo,AcDate,BatchNo 
ORDER BY AcBookCode)
*要注意重複產生傳票媒體時,應先取最後一筆序號為基數
	* @return Integer
	*/
  public int getMediaSeq() {
    return this.mediaSeq;
  }

/**
	* 媒體檔上傳序號<br>
	* 同單位別、會計日期、傳票批號時，根據帳冊別代碼依序給予序號
ROW_NUMBER() OVER (
PARTITION BY BranchNo,AcDate,BatchNo 
ORDER BY AcBookCode)
*要注意重複產生傳票媒體時,應先取最後一筆序號為基數
  *
  * @param mediaSeq 媒體檔上傳序號
	*/
  public void setMediaSeq(int mediaSeq) {
    this.mediaSeq = mediaSeq;
  }

/**
	* 媒體檔傳票號碼<br>
	* F10+民國年+月份(1碼)+日期+3碼序號
*3碼序號，從CdGSeq取號
	* @return String
	*/
  public String getMediaSlipNo() {
    return this.mediaSlipNo == null ? "" : this.mediaSlipNo;
  }

/**
	* 媒體檔傳票號碼<br>
	* F10+民國年+月份(1碼)+日期+3碼序號
*3碼序號，從CdGSeq取號
  *
  * @param mediaSlipNo 媒體檔傳票號碼
	*/
  public void setMediaSlipNo(String mediaSlipNo) {
    this.mediaSlipNo = mediaSlipNo;
  }

/**
	* 傳票序號<br>
	* 
	* @return Integer
	*/
  public int getSeq() {
    return this.seq;
  }

/**
	* 傳票序號<br>
	* 
  *
  * @param seq 傳票序號
	*/
  public void setSeq(int seq) {
    this.seq = seq;
  }


  @Override
  public int hashCode() {
    return Objects.hash(branchNo, acDate, batchNo, acBookCode, mediaSeq, mediaSlipNo, seq);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    SlipMediaId slipMediaId = (SlipMediaId) obj;
    return branchNo.equals(slipMediaId.branchNo) && acDate == slipMediaId.acDate && batchNo == slipMediaId.batchNo && acBookCode.equals(slipMediaId.acBookCode) && mediaSeq == slipMediaId.mediaSeq && mediaSlipNo.equals(slipMediaId.mediaSlipNo) && seq == slipMediaId.seq;
  }

  @Override
  public String toString() {
    return "SlipMediaId [branchNo=" + branchNo + ", acDate=" + acDate + ", batchNo=" + batchNo + ", acBookCode=" + acBookCode + ", mediaSeq=" + mediaSeq + ", mediaSlipNo=" + mediaSlipNo + ", seq=" + seq + "]";
  }
}
