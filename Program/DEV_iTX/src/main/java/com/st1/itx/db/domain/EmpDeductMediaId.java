package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * EmpDeductMedia 員工扣薪媒體檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class EmpDeductMediaId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 6334895644699092130L;

// 媒體日期
  @Column(name = "`MediaDate`")
  private int mediaDate = 0;

  // 媒體別
  /* 1:ACH新光2:ACH他行3:郵局4:15日5:非15日 */
  @Column(name = "`MediaKind`", length = 1)
  private String mediaKind = " ";

  // 媒體序號
  @Column(name = "`MediaSeq`")
  private int mediaSeq = 0;

  public EmpDeductMediaId() {
  }

  public EmpDeductMediaId(int mediaDate, String mediaKind, int mediaSeq) {
    this.mediaDate = mediaDate;
    this.mediaKind = mediaKind;
    this.mediaSeq = mediaSeq;
  }

/**
	* 媒體日期<br>
	* 
	* @return Integer
	*/
  public int getMediaDate() {
    return  StaticTool.bcToRoc(this.mediaDate);
  }

/**
	* 媒體日期<br>
	* 
  *
  * @param mediaDate 媒體日期
  * @throws LogicException when Date Is Warn	*/
  public void setMediaDate(int mediaDate) throws LogicException {
    this.mediaDate = StaticTool.rocToBc(mediaDate);
  }

/**
	* 媒體別<br>
	* 1:ACH新光
2:ACH他行
3:郵局
4:15日
5:非15日
	* @return String
	*/
  public String getMediaKind() {
    return this.mediaKind == null ? "" : this.mediaKind;
  }

/**
	* 媒體別<br>
	* 1:ACH新光
2:ACH他行
3:郵局
4:15日
5:非15日
  *
  * @param mediaKind 媒體別
	*/
  public void setMediaKind(String mediaKind) {
    this.mediaKind = mediaKind;
  }

/**
	* 媒體序號<br>
	* 
	* @return Integer
	*/
  public int getMediaSeq() {
    return this.mediaSeq;
  }

/**
	* 媒體序號<br>
	* 
  *
  * @param mediaSeq 媒體序號
	*/
  public void setMediaSeq(int mediaSeq) {
    this.mediaSeq = mediaSeq;
  }


  @Override
  public int hashCode() {
    return Objects.hash(mediaDate, mediaKind, mediaSeq);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    EmpDeductMediaId empDeductMediaId = (EmpDeductMediaId) obj;
    return mediaDate == empDeductMediaId.mediaDate && mediaKind.equals(empDeductMediaId.mediaKind) && mediaSeq == empDeductMediaId.mediaSeq;
  }

  @Override
  public String toString() {
    return "EmpDeductMediaId [mediaDate=" + mediaDate + ", mediaKind=" + mediaKind + ", mediaSeq=" + mediaSeq + "]";
  }
}
