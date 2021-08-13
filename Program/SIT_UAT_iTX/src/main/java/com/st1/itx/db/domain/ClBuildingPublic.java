package com.st1.itx.db.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.EmbeddedId;
import javax.persistence.Column;

/**
 * ClBuildingPublic 擔保品-建物公設建號檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`ClBuildingPublic`")
public class ClBuildingPublic implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -535437902800398015L;

@EmbeddedId
  private ClBuildingPublicId clBuildingPublicId;

  // 擔保品-代號1
  /* 擔保品代號檔CdCl */
  @Column(name = "`ClCode1`", insertable = false, updatable = false)
  private int clCode1 = 0;

  // 擔保品-代號2
  /* 擔保品代號檔CdCl */
  @Column(name = "`ClCode2`", insertable = false, updatable = false)
  private int clCode2 = 0;

  // 擔保品編號
  @Column(name = "`ClNo`", insertable = false, updatable = false)
  private int clNo = 0;

  // 公設建號
  /* 建號格式為5-3 */
  @Column(name = "`PublicBdNo1`", insertable = false, updatable = false)
  private int publicBdNo1 = 0;

  // 公設建號(子號)
  /* 建號格式為5-3 */
  @Column(name = "`PublicBdNo2`", insertable = false, updatable = false)
  private int publicBdNo2 = 0;

  // 登記面積(坪)
  @Column(name = "`Area`")
  private BigDecimal area = new BigDecimal("0");

  // 所有權人統編
  @Column(name = "`OwnerId`", length = 10)
  private String ownerId;

  // 所有權人姓名
  @Column(name = "`OwnerName`", length = 100)
  private String ownerName;

  // 建檔日期時間
  @CreatedDate
  @Column(name = "`CreateDate`")
  private java.sql.Timestamp createDate;

  // 建檔人員
  @Column(name = "`CreateEmpNo`", length = 6)
  private String createEmpNo;

  // 最後更新日期時間
  @LastModifiedDate
  @Column(name = "`LastUpdate`")
  private java.sql.Timestamp lastUpdate;

  // 最後更新人員
  @Column(name = "`LastUpdateEmpNo`", length = 6)
  private String lastUpdateEmpNo;


  public ClBuildingPublicId getClBuildingPublicId() {
    return this.clBuildingPublicId;
  }

  public void setClBuildingPublicId(ClBuildingPublicId clBuildingPublicId) {
    this.clBuildingPublicId = clBuildingPublicId;
  }

/**
	* 擔保品-代號1<br>
	* 擔保品代號檔CdCl
	* @return Integer
	*/
  public int getClCode1() {
    return this.clCode1;
  }

/**
	* 擔保品-代號1<br>
	* 擔保品代號檔CdCl
  *
  * @param clCode1 擔保品-代號1
	*/
  public void setClCode1(int clCode1) {
    this.clCode1 = clCode1;
  }

/**
	* 擔保品-代號2<br>
	* 擔保品代號檔CdCl
	* @return Integer
	*/
  public int getClCode2() {
    return this.clCode2;
  }

/**
	* 擔保品-代號2<br>
	* 擔保品代號檔CdCl
  *
  * @param clCode2 擔保品-代號2
	*/
  public void setClCode2(int clCode2) {
    this.clCode2 = clCode2;
  }

/**
	* 擔保品編號<br>
	* 
	* @return Integer
	*/
  public int getClNo() {
    return this.clNo;
  }

/**
	* 擔保品編號<br>
	* 
  *
  * @param clNo 擔保品編號
	*/
  public void setClNo(int clNo) {
    this.clNo = clNo;
  }

/**
	* 公設建號<br>
	* 建號格式為5-3
	* @return Integer
	*/
  public int getPublicBdNo1() {
    return this.publicBdNo1;
  }

/**
	* 公設建號<br>
	* 建號格式為5-3
  *
  * @param publicBdNo1 公設建號
	*/
  public void setPublicBdNo1(int publicBdNo1) {
    this.publicBdNo1 = publicBdNo1;
  }

/**
	* 公設建號(子號)<br>
	* 建號格式為5-3
	* @return Integer
	*/
  public int getPublicBdNo2() {
    return this.publicBdNo2;
  }

/**
	* 公設建號(子號)<br>
	* 建號格式為5-3
  *
  * @param publicBdNo2 公設建號(子號)
	*/
  public void setPublicBdNo2(int publicBdNo2) {
    this.publicBdNo2 = publicBdNo2;
  }

/**
	* 登記面積(坪)<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getArea() {
    return this.area;
  }

/**
	* 登記面積(坪)<br>
	* 
  *
  * @param area 登記面積(坪)
	*/
  public void setArea(BigDecimal area) {
    this.area = area;
  }

/**
	* 所有權人統編<br>
	* 
	* @return String
	*/
  public String getOwnerId() {
    return this.ownerId == null ? "" : this.ownerId;
  }

/**
	* 所有權人統編<br>
	* 
  *
  * @param ownerId 所有權人統編
	*/
  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }

/**
	* 所有權人姓名<br>
	* 
	* @return String
	*/
  public String getOwnerName() {
    return this.ownerName == null ? "" : this.ownerName;
  }

/**
	* 所有權人姓名<br>
	* 
  *
  * @param ownerName 所有權人姓名
	*/
  public void setOwnerName(String ownerName) {
    this.ownerName = ownerName;
  }

/**
	* 建檔日期時間<br>
	* 
	* @return java.sql.Timestamp
	*/
  public java.sql.Timestamp getCreateDate() {
    return this.createDate;
  }

/**
	* 建檔日期時間<br>
	* 
  *
  * @param createDate 建檔日期時間
	*/
  public void setCreateDate(java.sql.Timestamp createDate) {
    this.createDate = createDate;
  }

/**
	* 建檔人員<br>
	* 
	* @return String
	*/
  public String getCreateEmpNo() {
    return this.createEmpNo == null ? "" : this.createEmpNo;
  }

/**
	* 建檔人員<br>
	* 
  *
  * @param createEmpNo 建檔人員
	*/
  public void setCreateEmpNo(String createEmpNo) {
    this.createEmpNo = createEmpNo;
  }

/**
	* 最後更新日期時間<br>
	* 
	* @return java.sql.Timestamp
	*/
  public java.sql.Timestamp getLastUpdate() {
    return this.lastUpdate;
  }

/**
	* 最後更新日期時間<br>
	* 
  *
  * @param lastUpdate 最後更新日期時間
	*/
  public void setLastUpdate(java.sql.Timestamp lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

/**
	* 最後更新人員<br>
	* 
	* @return String
	*/
  public String getLastUpdateEmpNo() {
    return this.lastUpdateEmpNo == null ? "" : this.lastUpdateEmpNo;
  }

/**
	* 最後更新人員<br>
	* 
  *
  * @param lastUpdateEmpNo 最後更新人員
	*/
  public void setLastUpdateEmpNo(String lastUpdateEmpNo) {
    this.lastUpdateEmpNo = lastUpdateEmpNo;
  }


  @Override
  public String toString() {
    return "ClBuildingPublic [clBuildingPublicId=" + clBuildingPublicId + ", area=" + area
           + ", ownerId=" + ownerId + ", ownerName=" + ownerName + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo
           + "]";
  }
}
