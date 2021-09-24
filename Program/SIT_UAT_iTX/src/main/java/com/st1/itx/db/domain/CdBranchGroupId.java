package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * CdBranchGroup 營業單位課組別檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class CdBranchGroupId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 7977579853393126705L;

// 單位別
  @Column(name = "`BranchNo`", length = 4)
  private String branchNo = " ";

  // 課組別代號
  @Column(name = "`GroupNo`")
  private int groupNo = 0;

  public CdBranchGroupId() {
  }

  public CdBranchGroupId(String branchNo, int groupNo) {
    this.branchNo = branchNo;
    this.groupNo = groupNo;
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
	* 課組別代號<br>
	* 
	* @return Integer
	*/
  public int getGroupNo() {
    return this.groupNo;
  }

/**
	* 課組別代號<br>
	* 
  *
  * @param groupNo 課組別代號
	*/
  public void setGroupNo(int groupNo) {
    this.groupNo = groupNo;
  }


  @Override
  public int hashCode() {
    return Objects.hash(branchNo, groupNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    CdBranchGroupId cdBranchGroupId = (CdBranchGroupId) obj;
    return branchNo.equals(cdBranchGroupId.branchNo) && groupNo == cdBranchGroupId.groupNo;
  }

  @Override
  public String toString() {
    return "CdBranchGroupId [branchNo=" + branchNo + ", groupNo=" + groupNo + "]";
  }
}
