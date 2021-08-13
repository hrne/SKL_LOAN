package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * PostDeductMedia 郵局扣款媒體檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class PostDeductMediaId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 6613874256102869949L;

// 媒體日期
  @Column(name = "`MediaDate`")
  private int mediaDate = 0;

  // 媒體序號
  @Column(name = "`MediaSeq`")
  private int mediaSeq = 0;

  public PostDeductMediaId() {
  }

  public PostDeductMediaId(int mediaDate, int mediaSeq) {
    this.mediaDate = mediaDate;
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
    return Objects.hash(mediaDate, mediaSeq);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    PostDeductMediaId postDeductMediaId = (PostDeductMediaId) obj;
    return mediaDate == postDeductMediaId.mediaDate && mediaSeq == postDeductMediaId.mediaSeq;
  }

  @Override
  public String toString() {
    return "PostDeductMediaId [mediaDate=" + mediaDate + ", mediaSeq=" + mediaSeq + "]";
  }
}
