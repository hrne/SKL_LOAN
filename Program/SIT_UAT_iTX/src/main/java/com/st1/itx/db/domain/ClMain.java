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
 * ClMain 擔保品主檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`ClMain`")
public class ClMain implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 5662805076091503874L;

@EmbeddedId
  private ClMainId clMainId;

  // 擔保品代號1
  /* 擔保品代號檔CdCl */
  @Column(name = "`ClCode1`", insertable = false, updatable = false)
  private int clCode1 = 0;

  // 擔保品代號2
  /* 擔保品代號檔CdCl */
  @Column(name = "`ClCode2`", insertable = false, updatable = false)
  private int clCode2 = 0;

  // 擔保品編號
  @Column(name = "`ClNo`", insertable = false, updatable = false)
  private int clNo = 0;

  // 客戶識別碼
  @Column(name = "`CustUKey`", length = 32)
  private String custUKey;

  // 擔保品類別代碼
  /* 共用代碼檔 */
  @Column(name = "`ClTypeCode`", length = 3)
  private String clTypeCode;

  // 地區別
  /* 地區別與鄉鎮區對照檔CdArea */
  @Column(name = "`CityCode`", length = 2)
  private String cityCode;

  // 鄉鎮區
  /* 地區別與鄉鎮區對照檔CdArea */
  @Column(name = "`AreaCode`", length = 3)
  private String areaCode;

  // 擔保品狀況碼
  /* 0:未抵押1:已抵押 */
  @Column(name = "`ClStatus`", length = 1)
  private String clStatus;

  // 鑑估日期
  @Column(name = "`EvaDate`")
  private int evaDate = 0;

  // 鑑估總值
  @Column(name = "`EvaAmt`")
  private BigDecimal evaAmt = new BigDecimal("0");

  // 可分配金額
  /* 鑑估總值*貸放成數(四捨五入至個位數)與設定金額比較,較低者為可分配金額同一擔保品在ClFac擔保品關聯檔的分配金額加總需小於ClMain擔保品主檔的可分配金額 */
  @Column(name = "`ShareTotal`")
  private BigDecimal shareTotal = new BigDecimal("0");

  // 是否為聯貸案
  /* Y:是N:否 */
  @Column(name = "`Synd`", length = 1)
  private String synd;

  // 聯貸案類型
  /* 共用代碼檔1:主辦行2:參貸行 */
  @Column(name = "`SyndCode`", length = 1)
  private String syndCode;

  // 處分價格
  @Column(name = "`DispPrice`")
  private BigDecimal dispPrice = new BigDecimal("0");

  // 處分日期
  @Column(name = "`DispDate`")
  private int dispDate = 0;

  // 最新註記
  /* Y:是N:否 */
  @Column(name = "`NewNote`", length = 1)
  private String newNote;

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


  public ClMainId getClMainId() {
    return this.clMainId;
  }

  public void setClMainId(ClMainId clMainId) {
    this.clMainId = clMainId;
  }

/**
	* 擔保品代號1<br>
	* 擔保品代號檔CdCl
	* @return Integer
	*/
  public int getClCode1() {
    return this.clCode1;
  }

/**
	* 擔保品代號1<br>
	* 擔保品代號檔CdCl
  *
  * @param clCode1 擔保品代號1
	*/
  public void setClCode1(int clCode1) {
    this.clCode1 = clCode1;
  }

/**
	* 擔保品代號2<br>
	* 擔保品代號檔CdCl
	* @return Integer
	*/
  public int getClCode2() {
    return this.clCode2;
  }

/**
	* 擔保品代號2<br>
	* 擔保品代號檔CdCl
  *
  * @param clCode2 擔保品代號2
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
	* 客戶識別碼<br>
	* 
	* @return String
	*/
  public String getCustUKey() {
    return this.custUKey == null ? "" : this.custUKey;
  }

/**
	* 客戶識別碼<br>
	* 
  *
  * @param custUKey 客戶識別碼
	*/
  public void setCustUKey(String custUKey) {
    this.custUKey = custUKey;
  }

/**
	* 擔保品類別代碼<br>
	* 共用代碼檔
	* @return String
	*/
  public String getClTypeCode() {
    return this.clTypeCode == null ? "" : this.clTypeCode;
  }

/**
	* 擔保品類別代碼<br>
	* 共用代碼檔
  *
  * @param clTypeCode 擔保品類別代碼
	*/
  public void setClTypeCode(String clTypeCode) {
    this.clTypeCode = clTypeCode;
  }

/**
	* 地區別<br>
	* 地區別與鄉鎮區對照檔CdArea
	* @return String
	*/
  public String getCityCode() {
    return this.cityCode == null ? "" : this.cityCode;
  }

/**
	* 地區別<br>
	* 地區別與鄉鎮區對照檔CdArea
  *
  * @param cityCode 地區別
	*/
  public void setCityCode(String cityCode) {
    this.cityCode = cityCode;
  }

/**
	* 鄉鎮區<br>
	* 地區別與鄉鎮區對照檔CdArea
	* @return String
	*/
  public String getAreaCode() {
    return this.areaCode == null ? "" : this.areaCode;
  }

/**
	* 鄉鎮區<br>
	* 地區別與鄉鎮區對照檔CdArea
  *
  * @param areaCode 鄉鎮區
	*/
  public void setAreaCode(String areaCode) {
    this.areaCode = areaCode;
  }

/**
	* 擔保品狀況碼<br>
	* 0:未抵押
1:已抵押
	* @return String
	*/
  public String getClStatus() {
    return this.clStatus == null ? "" : this.clStatus;
  }

/**
	* 擔保品狀況碼<br>
	* 0:未抵押
1:已抵押
  *
  * @param clStatus 擔保品狀況碼
	*/
  public void setClStatus(String clStatus) {
    this.clStatus = clStatus;
  }

/**
	* 鑑估日期<br>
	* 
	* @return Integer
	*/
  public int getEvaDate() {
    return StaticTool.bcToRoc(this.evaDate);
  }

/**
	* 鑑估日期<br>
	* 
  *
  * @param evaDate 鑑估日期
  * @throws LogicException when Date Is Warn	*/
  public void setEvaDate(int evaDate) throws LogicException {
    this.evaDate = StaticTool.rocToBc(evaDate);
  }

/**
	* 鑑估總值<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getEvaAmt() {
    return this.evaAmt;
  }

/**
	* 鑑估總值<br>
	* 
  *
  * @param evaAmt 鑑估總值
	*/
  public void setEvaAmt(BigDecimal evaAmt) {
    this.evaAmt = evaAmt;
  }

/**
	* 可分配金額<br>
	* 鑑估總值*貸放成數(四捨五入至個位數)與設定金額比較,較低者為可分配金額
同一擔保品在ClFac擔保品關聯檔的分配金額加總需小於ClMain擔保品主檔的可分配金額
	* @return BigDecimal
	*/
  public BigDecimal getShareTotal() {
    return this.shareTotal;
  }

/**
	* 可分配金額<br>
	* 鑑估總值*貸放成數(四捨五入至個位數)與設定金額比較,較低者為可分配金額
同一擔保品在ClFac擔保品關聯檔的分配金額加總需小於ClMain擔保品主檔的可分配金額
  *
  * @param shareTotal 可分配金額
	*/
  public void setShareTotal(BigDecimal shareTotal) {
    this.shareTotal = shareTotal;
  }

/**
	* 是否為聯貸案<br>
	* Y:是
N:否
	* @return String
	*/
  public String getSynd() {
    return this.synd == null ? "" : this.synd;
  }

/**
	* 是否為聯貸案<br>
	* Y:是
N:否
  *
  * @param synd 是否為聯貸案
	*/
  public void setSynd(String synd) {
    this.synd = synd;
  }

/**
	* 聯貸案類型<br>
	* 共用代碼檔
1:主辦行
2:參貸行
	* @return String
	*/
  public String getSyndCode() {
    return this.syndCode == null ? "" : this.syndCode;
  }

/**
	* 聯貸案類型<br>
	* 共用代碼檔
1:主辦行
2:參貸行
  *
  * @param syndCode 聯貸案類型
	*/
  public void setSyndCode(String syndCode) {
    this.syndCode = syndCode;
  }

/**
	* 處分價格<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getDispPrice() {
    return this.dispPrice;
  }

/**
	* 處分價格<br>
	* 
  *
  * @param dispPrice 處分價格
	*/
  public void setDispPrice(BigDecimal dispPrice) {
    this.dispPrice = dispPrice;
  }

/**
	* 處分日期<br>
	* 
	* @return Integer
	*/
  public int getDispDate() {
    return StaticTool.bcToRoc(this.dispDate);
  }

/**
	* 處分日期<br>
	* 
  *
  * @param dispDate 處分日期
  * @throws LogicException when Date Is Warn	*/
  public void setDispDate(int dispDate) throws LogicException {
    this.dispDate = StaticTool.rocToBc(dispDate);
  }

/**
	* 最新註記<br>
	* Y:是
N:否
	* @return String
	*/
  public String getNewNote() {
    return this.newNote == null ? "" : this.newNote;
  }

/**
	* 最新註記<br>
	* Y:是
N:否
  *
  * @param newNote 最新註記
	*/
  public void setNewNote(String newNote) {
    this.newNote = newNote;
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
    return "ClMain [clMainId=" + clMainId + ", custUKey=" + custUKey + ", clTypeCode=" + clTypeCode + ", cityCode=" + cityCode
           + ", areaCode=" + areaCode + ", clStatus=" + clStatus + ", evaDate=" + evaDate + ", evaAmt=" + evaAmt + ", shareTotal=" + shareTotal + ", synd=" + synd
           + ", syndCode=" + syndCode + ", dispPrice=" + dispPrice + ", dispDate=" + dispDate + ", newNote=" + newNote + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
           + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
