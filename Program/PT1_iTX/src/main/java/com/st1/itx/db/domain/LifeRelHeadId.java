package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * LifeRelHead 人壽利關人負責人檔T07、TA07
(使用報表：LM013、LM042、LM050)<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class LifeRelHeadId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -7826615869471075301L;

// 負責人身分證/統一編號
  @Column(name = "`HeadId`", length = 10)
  private String headId = " ";

  // 負責人關係人身分證/統一編號
  @Column(name = "`RelId`", length = 10)
  private String relId = " ";

  // 事業負責人身分證/統一編號
  @Column(name = "`BusId`", length = 10)
  private String busId = " ";

  // 會計日期
  @Column(name = "`AcDate`")
  private int acDate = 0;

  public LifeRelHeadId() {
  }

  public LifeRelHeadId(String headId, String relId, String busId, int acDate) {
    this.headId = headId;
    this.relId = relId;
    this.busId = busId;
    this.acDate = acDate;
  }

/**
	* 負責人身分證/統一編號<br>
	* 
	* @return String
	*/
  public String getHeadId() {
    return this.headId == null ? "" : this.headId;
  }

/**
	* 負責人身分證/統一編號<br>
	* 
  *
  * @param headId 負責人身分證/統一編號
	*/
  public void setHeadId(String headId) {
    this.headId = headId;
  }

/**
	* 負責人關係人身分證/統一編號<br>
	* 
	* @return String
	*/
  public String getRelId() {
    return this.relId == null ? "" : this.relId;
  }

/**
	* 負責人關係人身分證/統一編號<br>
	* 
  *
  * @param relId 負責人關係人身分證/統一編號
	*/
  public void setRelId(String relId) {
    this.relId = relId;
  }

/**
	* 事業負責人身分證/統一編號<br>
	* 
	* @return String
	*/
  public String getBusId() {
    return this.busId == null ? "" : this.busId;
  }

/**
	* 事業負責人身分證/統一編號<br>
	* 
  *
  * @param busId 事業負責人身分證/統一編號
	*/
  public void setBusId(String busId) {
    this.busId = busId;
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


  @Override
  public int hashCode() {
    return Objects.hash(headId, relId, busId, acDate);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    LifeRelHeadId lifeRelHeadId = (LifeRelHeadId) obj;
    return headId.equals(lifeRelHeadId.headId) && relId.equals(lifeRelHeadId.relId) && busId.equals(lifeRelHeadId.busId) && acDate == lifeRelHeadId.acDate;
  }

  @Override
  public String toString() {
    return "LifeRelHeadId [headId=" + headId + ", relId=" + relId + ", busId=" + busId + ", acDate=" + acDate + "]";
  }
}
