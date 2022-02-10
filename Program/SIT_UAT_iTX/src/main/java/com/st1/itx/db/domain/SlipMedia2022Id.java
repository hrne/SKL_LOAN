package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * SlipMedia2022 傳票媒體檔2022年格式<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class SlipMedia2022Id implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

// 傳票號碼
  /* F10+民國年+月份(1碼)+日期+3碼序號 */
  @Column(name = "`MediaSlipNo`", length = 12)
  private String mediaSlipNo = " ";

  // 傳票明細序號
  /* 以相同傳票號碼編立流水號 */
  @Column(name = "`Seq`")
  private int seq = 0;

  public SlipMedia2022Id() {
  }

  public SlipMedia2022Id(String mediaSlipNo, int seq) {
    this.mediaSlipNo = mediaSlipNo;
    this.seq = seq;
  }

/**
	* 傳票號碼<br>
	* F10+民國年+月份(1碼)+日期+3碼序號
	* @return String
	*/
  public String getMediaSlipNo() {
    return this.mediaSlipNo == null ? "" : this.mediaSlipNo;
  }

/**
	* 傳票號碼<br>
	* F10+民國年+月份(1碼)+日期+3碼序號
  *
  * @param mediaSlipNo 傳票號碼
	*/
  public void setMediaSlipNo(String mediaSlipNo) {
    this.mediaSlipNo = mediaSlipNo;
  }

/**
	* 傳票明細序號<br>
	* 以相同傳票號碼編立流水號
	* @return Integer
	*/
  public int getSeq() {
    return this.seq;
  }

/**
	* 傳票明細序號<br>
	* 以相同傳票號碼編立流水號
  *
  * @param seq 傳票明細序號
	*/
  public void setSeq(int seq) {
    this.seq = seq;
  }


  @Override
  public int hashCode() {
    return Objects.hash(mediaSlipNo, seq);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    SlipMedia2022Id slipMedia2022Id = (SlipMedia2022Id) obj;
    return mediaSlipNo.equals(slipMedia2022Id.mediaSlipNo) && seq == slipMedia2022Id.seq;
  }

  @Override
  public String toString() {
    return "SlipMedia2022Id [mediaSlipNo=" + mediaSlipNo + ", seq=" + seq + "]";
  }
}
