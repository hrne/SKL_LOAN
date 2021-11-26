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
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * InsuOrignal 火險初保檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`InsuOrignal`")
public class InsuOrignal implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 6751954939234637062L;

@EmbeddedId
  private InsuOrignalId insuOrignalId;

  // 擔保品-代號1
  @Column(name = "`ClCode1`", insertable = false, updatable = false)
  private int clCode1 = 0;

  // 擔保品-代號2
  @Column(name = "`ClCode2`", insertable = false, updatable = false)
  private int clCode2 = 0;

  // 擔保品編號
  @Column(name = "`ClNo`", insertable = false, updatable = false)
  private int clNo = 0;

  // 原始保險單號碼
  @Column(name = "`OrigInsuNo`", length = 17, insertable = false, updatable = false)
  private String origInsuNo;

  // 批單號碼
  @Column(name = "`EndoInsuNo`", length = 17, insertable = false, updatable = false)
  private String endoInsuNo;

  // 保險公司
  @Column(name = "`InsuCompany`", length = 2)
  private String insuCompany;

  // 保險類別
  /* CdCode:InsuTypeCode01:住宅火險地震險02:火險03:地震險04:汽車全險05:綜合營造險06:動產火險07:其他 */
  @Column(name = "`InsuTypeCode`", length = 2)
  private String insuTypeCode;

  // 火災險保險金額
  @Column(name = "`FireInsuCovrg`")
  private BigDecimal fireInsuCovrg = new BigDecimal("0");

  // 地震險保險金額
  @Column(name = "`EthqInsuCovrg`")
  private BigDecimal ethqInsuCovrg = new BigDecimal("0");

  // 火災險保費
  @Column(name = "`FireInsuPrem`")
  private BigDecimal fireInsuPrem = new BigDecimal("0");

  // 地震險保費
  @Column(name = "`EthqInsuPrem`")
  private BigDecimal ethqInsuPrem = new BigDecimal("0");

  // 保險起日
  @Column(name = "`InsuStartDate`")
  private int insuStartDate = 0;

  // 保險迄日
  @Column(name = "`InsuEndDate`")
  private int insuEndDate = 0;

  // 住宅險改商業險註記
  @Column(name = "`CommericalFlag`", length = 1)
  private String commericalFlag;

  // 備註
  @Column(name = "`Remark`", length = 50)
  private String remark;

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


  public InsuOrignalId getInsuOrignalId() {
    return this.insuOrignalId;
  }

  public void setInsuOrignalId(InsuOrignalId insuOrignalId) {
    this.insuOrignalId = insuOrignalId;
  }

/**
	* 擔保品-代號1<br>
	* 
	* @return Integer
	*/
  public int getClCode1() {
    return this.clCode1;
  }

/**
	* 擔保品-代號1<br>
	* 
  *
  * @param clCode1 擔保品-代號1
	*/
  public void setClCode1(int clCode1) {
    this.clCode1 = clCode1;
  }

/**
	* 擔保品-代號2<br>
	* 
	* @return Integer
	*/
  public int getClCode2() {
    return this.clCode2;
  }

/**
	* 擔保品-代號2<br>
	* 
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
	* 原始保險單號碼<br>
	* 
	* @return String
	*/
  public String getOrigInsuNo() {
    return this.origInsuNo == null ? "" : this.origInsuNo;
  }

/**
	* 原始保險單號碼<br>
	* 
  *
  * @param origInsuNo 原始保險單號碼
	*/
  public void setOrigInsuNo(String origInsuNo) {
    this.origInsuNo = origInsuNo;
  }

/**
	* 批單號碼<br>
	* 
	* @return String
	*/
  public String getEndoInsuNo() {
    return this.endoInsuNo == null ? "" : this.endoInsuNo;
  }

/**
	* 批單號碼<br>
	* 
  *
  * @param endoInsuNo 批單號碼
	*/
  public void setEndoInsuNo(String endoInsuNo) {
    this.endoInsuNo = endoInsuNo;
  }

/**
	* 保險公司<br>
	* 
	* @return String
	*/
  public String getInsuCompany() {
    return this.insuCompany == null ? "" : this.insuCompany;
  }

/**
	* 保險公司<br>
	* 
  *
  * @param insuCompany 保險公司
	*/
  public void setInsuCompany(String insuCompany) {
    this.insuCompany = insuCompany;
  }

/**
	* 保險類別<br>
	* CdCode:InsuTypeCode
01:住宅火險地震險
02:火險
03:地震險
04:汽車全險
05:綜合營造險
06:動產火險
07:其他
	* @return String
	*/
  public String getInsuTypeCode() {
    return this.insuTypeCode == null ? "" : this.insuTypeCode;
  }

/**
	* 保險類別<br>
	* CdCode:InsuTypeCode
01:住宅火險地震險
02:火險
03:地震險
04:汽車全險
05:綜合營造險
06:動產火險
07:其他
  *
  * @param insuTypeCode 保險類別
	*/
  public void setInsuTypeCode(String insuTypeCode) {
    this.insuTypeCode = insuTypeCode;
  }

/**
	* 火災險保險金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getFireInsuCovrg() {
    return this.fireInsuCovrg;
  }

/**
	* 火災險保險金額<br>
	* 
  *
  * @param fireInsuCovrg 火災險保險金額
	*/
  public void setFireInsuCovrg(BigDecimal fireInsuCovrg) {
    this.fireInsuCovrg = fireInsuCovrg;
  }

/**
	* 地震險保險金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getEthqInsuCovrg() {
    return this.ethqInsuCovrg;
  }

/**
	* 地震險保險金額<br>
	* 
  *
  * @param ethqInsuCovrg 地震險保險金額
	*/
  public void setEthqInsuCovrg(BigDecimal ethqInsuCovrg) {
    this.ethqInsuCovrg = ethqInsuCovrg;
  }

/**
	* 火災險保費<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getFireInsuPrem() {
    return this.fireInsuPrem;
  }

/**
	* 火災險保費<br>
	* 
  *
  * @param fireInsuPrem 火災險保費
	*/
  public void setFireInsuPrem(BigDecimal fireInsuPrem) {
    this.fireInsuPrem = fireInsuPrem;
  }

/**
	* 地震險保費<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getEthqInsuPrem() {
    return this.ethqInsuPrem;
  }

/**
	* 地震險保費<br>
	* 
  *
  * @param ethqInsuPrem 地震險保費
	*/
  public void setEthqInsuPrem(BigDecimal ethqInsuPrem) {
    this.ethqInsuPrem = ethqInsuPrem;
  }

/**
	* 保險起日<br>
	* 
	* @return Integer
	*/
  public int getInsuStartDate() {
    return StaticTool.bcToRoc(this.insuStartDate);
  }

/**
	* 保險起日<br>
	* 
  *
  * @param insuStartDate 保險起日
  * @throws LogicException when Date Is Warn	*/
  public void setInsuStartDate(int insuStartDate) throws LogicException {
    this.insuStartDate = StaticTool.rocToBc(insuStartDate);
  }

/**
	* 保險迄日<br>
	* 
	* @return Integer
	*/
  public int getInsuEndDate() {
    return StaticTool.bcToRoc(this.insuEndDate);
  }

/**
	* 保險迄日<br>
	* 
  *
  * @param insuEndDate 保險迄日
  * @throws LogicException when Date Is Warn	*/
  public void setInsuEndDate(int insuEndDate) throws LogicException {
    this.insuEndDate = StaticTool.rocToBc(insuEndDate);
  }

/**
	* 住宅險改商業險註記<br>
	* 
	* @return String
	*/
  public String getCommericalFlag() {
    return this.commericalFlag == null ? "" : this.commericalFlag;
  }

/**
	* 住宅險改商業險註記<br>
	* 
  *
  * @param commericalFlag 住宅險改商業險註記
	*/
  public void setCommericalFlag(String commericalFlag) {
    this.commericalFlag = commericalFlag;
  }

/**
	* 備註<br>
	* 
	* @return String
	*/
  public String getRemark() {
    return this.remark == null ? "" : this.remark;
  }

/**
	* 備註<br>
	* 
  *
  * @param remark 備註
	*/
  public void setRemark(String remark) {
    this.remark = remark;
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
    return "InsuOrignal [insuOrignalId=" + insuOrignalId + ", insuCompany=" + insuCompany
           + ", insuTypeCode=" + insuTypeCode + ", fireInsuCovrg=" + fireInsuCovrg + ", ethqInsuCovrg=" + ethqInsuCovrg + ", fireInsuPrem=" + fireInsuPrem + ", ethqInsuPrem=" + ethqInsuPrem + ", insuStartDate=" + insuStartDate
           + ", insuEndDate=" + insuEndDate + ", commericalFlag=" + commericalFlag + ", remark=" + remark + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
           + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
