package com.st1.itx.db.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.EmbeddedId;
import javax.persistence.Column;

/**
 * InsuRenewMediaTemp 火險詢價媒體暫存檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`InsuRenewMediaTemp`")
public class InsuRenewMediaTemp implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 1060755593172252911L;

@EmbeddedId
  private InsuRenewMediaTempId insuRenewMediaTempId;

  // 火險到期年月
  @Column(name = "`FireInsuMonth`", length = 6, insertable = false, updatable = false)
  private String fireInsuMonth;

  // 回傳碼
  @Column(name = "`ReturnCode`", length = 2)
  private String returnCode;

  // 保險公司代碼
  @Column(name = "`InsuCampCode`", length = 2)
  private String insuCampCode;

  // 提供人統一編號
  @Column(name = "`InsuCustId`", length = 10)
  private String insuCustId;

  // 提供人姓名
  @Column(name = "`InsuCustName`", length = 12)
  private String insuCustName;

  // 借款人統一編號
  @Column(name = "`LoanCustId`", length = 10)
  private String loanCustId;

  // 借款人姓名
  @Column(name = "`LoanCustName`", length = 12)
  private String loanCustName;

  // 郵遞區號
  @Column(name = "`PostalCode`", length = 5)
  private String postalCode;

  // 門牌號碼
  @Column(name = "`Address`", length = 58)
  private String address;

  // 主建物坪數
  @Column(name = "`BuildingSquare`", length = 9)
  private String buildingSquare;

  // 建物結構代碼
  @Column(name = "`BuildingCode`", length = 2)
  private String buildingCode;

  // 建造年份
  @Column(name = "`BuildingYears`", length = 3)
  private String buildingYears;

  // 樓層數
  @Column(name = "`BuildingFloors`", length = 2)
  private String buildingFloors;

  // 屋頂結構代碼
  @Column(name = "`RoofCode`", length = 2)
  private String roofCode;

  // 營業單位別
  @Column(name = "`BusinessUnit`", length = 4)
  private String businessUnit;

  // 押品別１
  @Column(name = "`ClCode1`", length = 1)
  private String clCode1;

  // 押品別２
  @Column(name = "`ClCode2`", length = 2)
  private String clCode2;

  // 押品號碼
  @Column(name = "`ClNo`", length = 7)
  private String clNo;

  // 序號
  @Column(name = "`Seq`", length = 2)
  private String seq;

  // 保單號碼
  @Column(name = "`InsuNo`", length = 16, insertable = false, updatable = false)
  private String insuNo;

  // 保險起日
  @Column(name = "`InsuStartDate`", length = 10)
  private String insuStartDate;

  // 保險迄日
  @Column(name = "`InsuEndDate`", length = 10)
  private String insuEndDate;

  // 火險保額
  @Column(name = "`FireInsuAmt`", length = 11)
  private String fireInsuAmt;

  // 火險保費
  @Column(name = "`FireInsuFee`", length = 7)
  private String fireInsuFee;

  // 地震險保額
  @Column(name = "`EqInsuAmt`", length = 7)
  private String eqInsuAmt;

  // 地震險保費
  @Column(name = "`EqInsuFee`", length = 6)
  private String eqInsuFee;

  // 借款人戶號
  @Column(name = "`CustNo`", length = 7)
  private String custNo;

  // 額度編號
  @Column(name = "`FacmNo`", length = 3)
  private String facmNo;

  // 空白
  @Column(name = "`Space`", length = 4)
  private String space;

  // 傳檔日期
  @Column(name = "`SendDate`", length = 14)
  private String sendDate;

  // 保單號碼(新)
  @Column(name = "`NewInusNo`", length = 16)
  private String newInusNo;

  // 保險起日(新)
  @Column(name = "`NewInsuStartDate`", length = 10)
  private String newInsuStartDate;

  // 保險迄日(新)
  @Column(name = "`NewInsuEndDate`", length = 10)
  private String newInsuEndDate;

  // 火險保額(新)
  @Column(name = "`NewFireInsuAmt`", length = 11)
  private String newFireInsuAmt;

  // 火險保費(新)
  @Column(name = "`NewFireInsuFee`", length = 7)
  private String newFireInsuFee;

  // 地震險保額(新)
  @Column(name = "`NewEqInsuAmt`", length = 8)
  private String newEqInsuAmt;

  // 地震險保費(新)
  @Column(name = "`NewEqInsuFee`", length = 6)
  private String newEqInsuFee;

  // 總保費(新)
  @Column(name = "`NewTotalFee`", length = 7)
  private String newTotalFee;

  // 備註一
  @Column(name = "`Remark1`", length = 16)
  private String remark1;

  // 通訊地址
  @Column(name = "`MailingAddress`", length = 60)
  private String mailingAddress;

  // 備註二
  @Column(name = "`Remark2`", length = 39)
  private String remark2;

  // 新光人壽業務員名稱
  @Column(name = "`SklSalesName`", length = 20)
  private String sklSalesName;

  // 新光人壽單位代號
  @Column(name = "`SklUnitCode`", length = 6)
  private String sklUnitCode;

  // 新光人壽單位中文
  @Column(name = "`SklUnitName`", length = 20)
  private String sklUnitName;

  // 新光人壽業務員代號
  @Column(name = "`SklSalesCode`", length = 6)
  private String sklSalesCode;

  // 新產續保經辦代號
  @Column(name = "`RenewTrlCode`", length = 8)
  private String renewTrlCode;

  // 新產續保單位
  @Column(name = "`RenewUnit`", length = 7)
  private String renewUnit;

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


  public InsuRenewMediaTempId getInsuRenewMediaTempId() {
    return this.insuRenewMediaTempId;
  }

  public void setInsuRenewMediaTempId(InsuRenewMediaTempId insuRenewMediaTempId) {
    this.insuRenewMediaTempId = insuRenewMediaTempId;
  }

/**
	* 火險到期年月<br>
	* 
	* @return String
	*/
  public String getFireInsuMonth() {
    return this.fireInsuMonth == null ? "" : this.fireInsuMonth;
  }

/**
	* 火險到期年月<br>
	* 
  *
  * @param fireInsuMonth 火險到期年月
	*/
  public void setFireInsuMonth(String fireInsuMonth) {
    this.fireInsuMonth = fireInsuMonth;
  }

/**
	* 回傳碼<br>
	* 
	* @return String
	*/
  public String getReturnCode() {
    return this.returnCode == null ? "" : this.returnCode;
  }

/**
	* 回傳碼<br>
	* 
  *
  * @param returnCode 回傳碼
	*/
  public void setReturnCode(String returnCode) {
    this.returnCode = returnCode;
  }

/**
	* 保險公司代碼<br>
	* 
	* @return String
	*/
  public String getInsuCampCode() {
    return this.insuCampCode == null ? "" : this.insuCampCode;
  }

/**
	* 保險公司代碼<br>
	* 
  *
  * @param insuCampCode 保險公司代碼
	*/
  public void setInsuCampCode(String insuCampCode) {
    this.insuCampCode = insuCampCode;
  }

/**
	* 提供人統一編號<br>
	* 
	* @return String
	*/
  public String getInsuCustId() {
    return this.insuCustId == null ? "" : this.insuCustId;
  }

/**
	* 提供人統一編號<br>
	* 
  *
  * @param insuCustId 提供人統一編號
	*/
  public void setInsuCustId(String insuCustId) {
    this.insuCustId = insuCustId;
  }

/**
	* 提供人姓名<br>
	* 
	* @return String
	*/
  public String getInsuCustName() {
    return this.insuCustName == null ? "" : this.insuCustName;
  }

/**
	* 提供人姓名<br>
	* 
  *
  * @param insuCustName 提供人姓名
	*/
  public void setInsuCustName(String insuCustName) {
    this.insuCustName = insuCustName;
  }

/**
	* 借款人統一編號<br>
	* 
	* @return String
	*/
  public String getLoanCustId() {
    return this.loanCustId == null ? "" : this.loanCustId;
  }

/**
	* 借款人統一編號<br>
	* 
  *
  * @param loanCustId 借款人統一編號
	*/
  public void setLoanCustId(String loanCustId) {
    this.loanCustId = loanCustId;
  }

/**
	* 借款人姓名<br>
	* 
	* @return String
	*/
  public String getLoanCustName() {
    return this.loanCustName == null ? "" : this.loanCustName;
  }

/**
	* 借款人姓名<br>
	* 
  *
  * @param loanCustName 借款人姓名
	*/
  public void setLoanCustName(String loanCustName) {
    this.loanCustName = loanCustName;
  }

/**
	* 郵遞區號<br>
	* 
	* @return String
	*/
  public String getPostalCode() {
    return this.postalCode == null ? "" : this.postalCode;
  }

/**
	* 郵遞區號<br>
	* 
  *
  * @param postalCode 郵遞區號
	*/
  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

/**
	* 門牌號碼<br>
	* 
	* @return String
	*/
  public String getAddress() {
    return this.address == null ? "" : this.address;
  }

/**
	* 門牌號碼<br>
	* 
  *
  * @param address 門牌號碼
	*/
  public void setAddress(String address) {
    this.address = address;
  }

/**
	* 主建物坪數<br>
	* 
	* @return String
	*/
  public String getBuildingSquare() {
    return this.buildingSquare == null ? "" : this.buildingSquare;
  }

/**
	* 主建物坪數<br>
	* 
  *
  * @param buildingSquare 主建物坪數
	*/
  public void setBuildingSquare(String buildingSquare) {
    this.buildingSquare = buildingSquare;
  }

/**
	* 建物結構代碼<br>
	* 
	* @return String
	*/
  public String getBuildingCode() {
    return this.buildingCode == null ? "" : this.buildingCode;
  }

/**
	* 建物結構代碼<br>
	* 
  *
  * @param buildingCode 建物結構代碼
	*/
  public void setBuildingCode(String buildingCode) {
    this.buildingCode = buildingCode;
  }

/**
	* 建造年份<br>
	* 
	* @return String
	*/
  public String getBuildingYears() {
    return this.buildingYears == null ? "" : this.buildingYears;
  }

/**
	* 建造年份<br>
	* 
  *
  * @param buildingYears 建造年份
	*/
  public void setBuildingYears(String buildingYears) {
    this.buildingYears = buildingYears;
  }

/**
	* 樓層數<br>
	* 
	* @return String
	*/
  public String getBuildingFloors() {
    return this.buildingFloors == null ? "" : this.buildingFloors;
  }

/**
	* 樓層數<br>
	* 
  *
  * @param buildingFloors 樓層數
	*/
  public void setBuildingFloors(String buildingFloors) {
    this.buildingFloors = buildingFloors;
  }

/**
	* 屋頂結構代碼<br>
	* 
	* @return String
	*/
  public String getRoofCode() {
    return this.roofCode == null ? "" : this.roofCode;
  }

/**
	* 屋頂結構代碼<br>
	* 
  *
  * @param roofCode 屋頂結構代碼
	*/
  public void setRoofCode(String roofCode) {
    this.roofCode = roofCode;
  }

/**
	* 營業單位別<br>
	* 
	* @return String
	*/
  public String getBusinessUnit() {
    return this.businessUnit == null ? "" : this.businessUnit;
  }

/**
	* 營業單位別<br>
	* 
  *
  * @param businessUnit 營業單位別
	*/
  public void setBusinessUnit(String businessUnit) {
    this.businessUnit = businessUnit;
  }

/**
	* 押品別１<br>
	* 
	* @return String
	*/
  public String getClCode1() {
    return this.clCode1 == null ? "" : this.clCode1;
  }

/**
	* 押品別１<br>
	* 
  *
  * @param clCode1 押品別１
	*/
  public void setClCode1(String clCode1) {
    this.clCode1 = clCode1;
  }

/**
	* 押品別２<br>
	* 
	* @return String
	*/
  public String getClCode2() {
    return this.clCode2 == null ? "" : this.clCode2;
  }

/**
	* 押品別２<br>
	* 
  *
  * @param clCode2 押品別２
	*/
  public void setClCode2(String clCode2) {
    this.clCode2 = clCode2;
  }

/**
	* 押品號碼<br>
	* 
	* @return String
	*/
  public String getClNo() {
    return this.clNo == null ? "" : this.clNo;
  }

/**
	* 押品號碼<br>
	* 
  *
  * @param clNo 押品號碼
	*/
  public void setClNo(String clNo) {
    this.clNo = clNo;
  }

/**
	* 序號<br>
	* 
	* @return String
	*/
  public String getSeq() {
    return this.seq == null ? "" : this.seq;
  }

/**
	* 序號<br>
	* 
  *
  * @param seq 序號
	*/
  public void setSeq(String seq) {
    this.seq = seq;
  }

/**
	* 保單號碼<br>
	* 
	* @return String
	*/
  public String getInsuNo() {
    return this.insuNo == null ? "" : this.insuNo;
  }

/**
	* 保單號碼<br>
	* 
  *
  * @param insuNo 保單號碼
	*/
  public void setInsuNo(String insuNo) {
    this.insuNo = insuNo;
  }

/**
	* 保險起日<br>
	* 
	* @return String
	*/
  public String getInsuStartDate() {
    return this.insuStartDate == null ? "" : this.insuStartDate;
  }

/**
	* 保險起日<br>
	* 
  *
  * @param insuStartDate 保險起日
	*/
  public void setInsuStartDate(String insuStartDate) {
    this.insuStartDate = insuStartDate;
  }

/**
	* 保險迄日<br>
	* 
	* @return String
	*/
  public String getInsuEndDate() {
    return this.insuEndDate == null ? "" : this.insuEndDate;
  }

/**
	* 保險迄日<br>
	* 
  *
  * @param insuEndDate 保險迄日
	*/
  public void setInsuEndDate(String insuEndDate) {
    this.insuEndDate = insuEndDate;
  }

/**
	* 火險保額<br>
	* 
	* @return String
	*/
  public String getFireInsuAmt() {
    return this.fireInsuAmt == null ? "" : this.fireInsuAmt;
  }

/**
	* 火險保額<br>
	* 
  *
  * @param fireInsuAmt 火險保額
	*/
  public void setFireInsuAmt(String fireInsuAmt) {
    this.fireInsuAmt = fireInsuAmt;
  }

/**
	* 火險保費<br>
	* 
	* @return String
	*/
  public String getFireInsuFee() {
    return this.fireInsuFee == null ? "" : this.fireInsuFee;
  }

/**
	* 火險保費<br>
	* 
  *
  * @param fireInsuFee 火險保費
	*/
  public void setFireInsuFee(String fireInsuFee) {
    this.fireInsuFee = fireInsuFee;
  }

/**
	* 地震險保額<br>
	* 
	* @return String
	*/
  public String getEqInsuAmt() {
    return this.eqInsuAmt == null ? "" : this.eqInsuAmt;
  }

/**
	* 地震險保額<br>
	* 
  *
  * @param eqInsuAmt 地震險保額
	*/
  public void setEqInsuAmt(String eqInsuAmt) {
    this.eqInsuAmt = eqInsuAmt;
  }

/**
	* 地震險保費<br>
	* 
	* @return String
	*/
  public String getEqInsuFee() {
    return this.eqInsuFee == null ? "" : this.eqInsuFee;
  }

/**
	* 地震險保費<br>
	* 
  *
  * @param eqInsuFee 地震險保費
	*/
  public void setEqInsuFee(String eqInsuFee) {
    this.eqInsuFee = eqInsuFee;
  }

/**
	* 借款人戶號<br>
	* 
	* @return String
	*/
  public String getCustNo() {
    return this.custNo == null ? "" : this.custNo;
  }

/**
	* 借款人戶號<br>
	* 
  *
  * @param custNo 借款人戶號
	*/
  public void setCustNo(String custNo) {
    this.custNo = custNo;
  }

/**
	* 額度編號<br>
	* 
	* @return String
	*/
  public String getFacmNo() {
    return this.facmNo == null ? "" : this.facmNo;
  }

/**
	* 額度編號<br>
	* 
  *
  * @param facmNo 額度編號
	*/
  public void setFacmNo(String facmNo) {
    this.facmNo = facmNo;
  }

/**
	* 空白<br>
	* 
	* @return String
	*/
  public String getSpace() {
    return this.space == null ? "" : this.space;
  }

/**
	* 空白<br>
	* 
  *
  * @param space 空白
	*/
  public void setSpace(String space) {
    this.space = space;
  }

/**
	* 傳檔日期<br>
	* 
	* @return String
	*/
  public String getSendDate() {
    return this.sendDate == null ? "" : this.sendDate;
  }

/**
	* 傳檔日期<br>
	* 
  *
  * @param sendDate 傳檔日期
	*/
  public void setSendDate(String sendDate) {
    this.sendDate = sendDate;
  }

/**
	* 保單號碼(新)<br>
	* 
	* @return String
	*/
  public String getNewInusNo() {
    return this.newInusNo == null ? "" : this.newInusNo;
  }

/**
	* 保單號碼(新)<br>
	* 
  *
  * @param newInusNo 保單號碼(新)
	*/
  public void setNewInusNo(String newInusNo) {
    this.newInusNo = newInusNo;
  }

/**
	* 保險起日(新)<br>
	* 
	* @return String
	*/
  public String getNewInsuStartDate() {
    return this.newInsuStartDate == null ? "" : this.newInsuStartDate;
  }

/**
	* 保險起日(新)<br>
	* 
  *
  * @param newInsuStartDate 保險起日(新)
	*/
  public void setNewInsuStartDate(String newInsuStartDate) {
    this.newInsuStartDate = newInsuStartDate;
  }

/**
	* 保險迄日(新)<br>
	* 
	* @return String
	*/
  public String getNewInsuEndDate() {
    return this.newInsuEndDate == null ? "" : this.newInsuEndDate;
  }

/**
	* 保險迄日(新)<br>
	* 
  *
  * @param newInsuEndDate 保險迄日(新)
	*/
  public void setNewInsuEndDate(String newInsuEndDate) {
    this.newInsuEndDate = newInsuEndDate;
  }

/**
	* 火險保額(新)<br>
	* 
	* @return String
	*/
  public String getNewFireInsuAmt() {
    return this.newFireInsuAmt == null ? "" : this.newFireInsuAmt;
  }

/**
	* 火險保額(新)<br>
	* 
  *
  * @param newFireInsuAmt 火險保額(新)
	*/
  public void setNewFireInsuAmt(String newFireInsuAmt) {
    this.newFireInsuAmt = newFireInsuAmt;
  }

/**
	* 火險保費(新)<br>
	* 
	* @return String
	*/
  public String getNewFireInsuFee() {
    return this.newFireInsuFee == null ? "" : this.newFireInsuFee;
  }

/**
	* 火險保費(新)<br>
	* 
  *
  * @param newFireInsuFee 火險保費(新)
	*/
  public void setNewFireInsuFee(String newFireInsuFee) {
    this.newFireInsuFee = newFireInsuFee;
  }

/**
	* 地震險保額(新)<br>
	* 
	* @return String
	*/
  public String getNewEqInsuAmt() {
    return this.newEqInsuAmt == null ? "" : this.newEqInsuAmt;
  }

/**
	* 地震險保額(新)<br>
	* 
  *
  * @param newEqInsuAmt 地震險保額(新)
	*/
  public void setNewEqInsuAmt(String newEqInsuAmt) {
    this.newEqInsuAmt = newEqInsuAmt;
  }

/**
	* 地震險保費(新)<br>
	* 
	* @return String
	*/
  public String getNewEqInsuFee() {
    return this.newEqInsuFee == null ? "" : this.newEqInsuFee;
  }

/**
	* 地震險保費(新)<br>
	* 
  *
  * @param newEqInsuFee 地震險保費(新)
	*/
  public void setNewEqInsuFee(String newEqInsuFee) {
    this.newEqInsuFee = newEqInsuFee;
  }

/**
	* 總保費(新)<br>
	* 
	* @return String
	*/
  public String getNewTotalFee() {
    return this.newTotalFee == null ? "" : this.newTotalFee;
  }

/**
	* 總保費(新)<br>
	* 
  *
  * @param newTotalFee 總保費(新)
	*/
  public void setNewTotalFee(String newTotalFee) {
    this.newTotalFee = newTotalFee;
  }

/**
	* 備註一<br>
	* 
	* @return String
	*/
  public String getRemark1() {
    return this.remark1 == null ? "" : this.remark1;
  }

/**
	* 備註一<br>
	* 
  *
  * @param remark1 備註一
	*/
  public void setRemark1(String remark1) {
    this.remark1 = remark1;
  }

/**
	* 通訊地址<br>
	* 
	* @return String
	*/
  public String getMailingAddress() {
    return this.mailingAddress == null ? "" : this.mailingAddress;
  }

/**
	* 通訊地址<br>
	* 
  *
  * @param mailingAddress 通訊地址
	*/
  public void setMailingAddress(String mailingAddress) {
    this.mailingAddress = mailingAddress;
  }

/**
	* 備註二<br>
	* 
	* @return String
	*/
  public String getRemark2() {
    return this.remark2 == null ? "" : this.remark2;
  }

/**
	* 備註二<br>
	* 
  *
  * @param remark2 備註二
	*/
  public void setRemark2(String remark2) {
    this.remark2 = remark2;
  }

/**
	* 新光人壽業務員名稱<br>
	* 
	* @return String
	*/
  public String getSklSalesName() {
    return this.sklSalesName == null ? "" : this.sklSalesName;
  }

/**
	* 新光人壽業務員名稱<br>
	* 
  *
  * @param sklSalesName 新光人壽業務員名稱
	*/
  public void setSklSalesName(String sklSalesName) {
    this.sklSalesName = sklSalesName;
  }

/**
	* 新光人壽單位代號<br>
	* 
	* @return String
	*/
  public String getSklUnitCode() {
    return this.sklUnitCode == null ? "" : this.sklUnitCode;
  }

/**
	* 新光人壽單位代號<br>
	* 
  *
  * @param sklUnitCode 新光人壽單位代號
	*/
  public void setSklUnitCode(String sklUnitCode) {
    this.sklUnitCode = sklUnitCode;
  }

/**
	* 新光人壽單位中文<br>
	* 
	* @return String
	*/
  public String getSklUnitName() {
    return this.sklUnitName == null ? "" : this.sklUnitName;
  }

/**
	* 新光人壽單位中文<br>
	* 
  *
  * @param sklUnitName 新光人壽單位中文
	*/
  public void setSklUnitName(String sklUnitName) {
    this.sklUnitName = sklUnitName;
  }

/**
	* 新光人壽業務員代號<br>
	* 
	* @return String
	*/
  public String getSklSalesCode() {
    return this.sklSalesCode == null ? "" : this.sklSalesCode;
  }

/**
	* 新光人壽業務員代號<br>
	* 
  *
  * @param sklSalesCode 新光人壽業務員代號
	*/
  public void setSklSalesCode(String sklSalesCode) {
    this.sklSalesCode = sklSalesCode;
  }

/**
	* 新產續保經辦代號<br>
	* 
	* @return String
	*/
  public String getRenewTrlCode() {
    return this.renewTrlCode == null ? "" : this.renewTrlCode;
  }

/**
	* 新產續保經辦代號<br>
	* 
  *
  * @param renewTrlCode 新產續保經辦代號
	*/
  public void setRenewTrlCode(String renewTrlCode) {
    this.renewTrlCode = renewTrlCode;
  }

/**
	* 新產續保單位<br>
	* 
	* @return String
	*/
  public String getRenewUnit() {
    return this.renewUnit == null ? "" : this.renewUnit;
  }

/**
	* 新產續保單位<br>
	* 
  *
  * @param renewUnit 新產續保單位
	*/
  public void setRenewUnit(String renewUnit) {
    this.renewUnit = renewUnit;
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
    return "InsuRenewMediaTemp [insuRenewMediaTempId=" + insuRenewMediaTempId + ", returnCode=" + returnCode + ", insuCampCode=" + insuCampCode + ", insuCustId=" + insuCustId + ", insuCustName=" + insuCustName + ", loanCustId=" + loanCustId
           + ", loanCustName=" + loanCustName + ", postalCode=" + postalCode + ", address=" + address + ", buildingSquare=" + buildingSquare + ", buildingCode=" + buildingCode + ", buildingYears=" + buildingYears
           + ", buildingFloors=" + buildingFloors + ", roofCode=" + roofCode + ", businessUnit=" + businessUnit + ", clCode1=" + clCode1 + ", clCode2=" + clCode2 + ", clNo=" + clNo
           + ", seq=" + seq + ", insuStartDate=" + insuStartDate + ", insuEndDate=" + insuEndDate + ", fireInsuAmt=" + fireInsuAmt + ", fireInsuFee=" + fireInsuFee
           + ", eqInsuAmt=" + eqInsuAmt + ", eqInsuFee=" + eqInsuFee + ", custNo=" + custNo + ", facmNo=" + facmNo + ", space=" + space + ", sendDate=" + sendDate
           + ", newInusNo=" + newInusNo + ", newInsuStartDate=" + newInsuStartDate + ", newInsuEndDate=" + newInsuEndDate + ", newFireInsuAmt=" + newFireInsuAmt + ", newFireInsuFee=" + newFireInsuFee + ", newEqInsuAmt=" + newEqInsuAmt
           + ", newEqInsuFee=" + newEqInsuFee + ", newTotalFee=" + newTotalFee + ", remark1=" + remark1 + ", mailingAddress=" + mailingAddress + ", remark2=" + remark2 + ", sklSalesName=" + sklSalesName
           + ", sklUnitCode=" + sklUnitCode + ", sklUnitName=" + sklUnitName + ", sklSalesCode=" + sklSalesCode + ", renewTrlCode=" + renewTrlCode + ", renewUnit=" + renewUnit + ", createDate=" + createDate
           + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
