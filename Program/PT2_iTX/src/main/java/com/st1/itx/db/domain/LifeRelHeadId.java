package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;

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


  // 負責人身分證/統一編號
  @Column(name = "`HeadId`", length = 10)
  private String headId = " ";

  // 負責人關係人身分證/統一編號
  @Column(name = "`RelId`", length = 10)
  private String relId = " ";

  // 事業負責人身分證/統一編號
  @Column(name = "`BusId`", length = 10)
  private String busId = " ";

  public LifeRelHeadId() {
  }

  public LifeRelHeadId(String headId, String relId, String busId) {
    this.headId = headId;
    this.relId = relId;
    this.busId = busId;
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


  @Override
  public int hashCode() {
    return Objects.hash(headId, relId, busId);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    LifeRelHeadId lifeRelHeadId = (LifeRelHeadId) obj;
    return headId.equals(lifeRelHeadId.headId) && relId.equals(lifeRelHeadId.relId) && busId.equals(lifeRelHeadId.busId);
  }

  @Override
  public String toString() {
    return "LifeRelHeadId [headId=" + headId + ", relId=" + relId + ", busId=" + busId + "]";
  }
}
