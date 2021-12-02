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
 * Ias34Ep IAS34欄位清單E檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`Ias34Ep`")
public class Ias34Ep implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 1354260503160662172L;

@EmbeddedId
  private Ias34EpId ias34EpId;

  // 資料時點(年月)
  /* YYYYMM */
  @Column(name = "`DataYM`", insertable = false, updatable = false)
  private int dataYM = 0;

  // 戶號
  @Column(name = "`CustNo`", insertable = false, updatable = false)
  private int custNo = 0;

  // 借款人ID / 統編
  @Column(name = "`CustId`", length = 10)
  private String custId;

  // 額度編號(核准號碼)
  @Column(name = "`FacmNo`", insertable = false, updatable = false)
  private int facmNo = 0;

  // 撥款序號
  @Column(name = "`BormNo`", insertable = false, updatable = false)
  private int bormNo = 0;

  // 會計科目
  @Column(name = "`AcCode`", length = 11, insertable = false, updatable = false)
  private String acCode;

  // 狀態
  /* 辨識是否為帳上客戶或為轉呆客戶1=帳上客戶；2=轉呆客戶； */
  @Column(name = "`Status`", insertable = false, updatable = false)
  private int status = 0;

  // 授信行業別
  @Column(name = "`IndustryCode`", length = 6)
  private String industryCode;

  // 擔保品類別
  @Column(name = "`ClTypeJCIC`", length = 2)
  private String clTypeJCIC;

  // 擔保品地區別(郵遞區號)
  @Column(name = "`Zip3`", length = 3)
  private String zip3;

  // 商品利率代碼
  @Column(name = "`ProdNo`", length = 5)
  private String prodNo;

  // 企業戶/個人戶
  /* 1=企業戶2=個人戶 */
  @Column(name = "`CustKind`")
  private int custKind = 0;

  // 資料時點是否符合減損客觀證據
  /* Y=符合減損客觀證據條件N=未符合減損客觀證據條件 */
  @Column(name = "`DerFg`", length = 1)
  private String derFg;

  // 產品別
  @Column(name = "`Ifrs9ProdCode`", length = 2)
  private String ifrs9ProdCode;

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


  public Ias34EpId getIas34EpId() {
    return this.ias34EpId;
  }

  public void setIas34EpId(Ias34EpId ias34EpId) {
    this.ias34EpId = ias34EpId;
  }

/**
	* 資料時點(年月)<br>
	* YYYYMM
	* @return Integer
	*/
  public int getDataYM() {
    return this.dataYM;
  }

/**
	* 資料時點(年月)<br>
	* YYYYMM
  *
  * @param dataYM 資料時點(年月)
	*/
  public void setDataYM(int dataYM) {
    this.dataYM = dataYM;
  }

/**
	* 戶號<br>
	* 
	* @return Integer
	*/
  public int getCustNo() {
    return this.custNo;
  }

/**
	* 戶號<br>
	* 
  *
  * @param custNo 戶號
	*/
  public void setCustNo(int custNo) {
    this.custNo = custNo;
  }

/**
	* 借款人ID / 統編<br>
	* 
	* @return String
	*/
  public String getCustId() {
    return this.custId == null ? "" : this.custId;
  }

/**
	* 借款人ID / 統編<br>
	* 
  *
  * @param custId 借款人ID / 統編
	*/
  public void setCustId(String custId) {
    this.custId = custId;
  }

/**
	* 額度編號(核准號碼)<br>
	* 
	* @return Integer
	*/
  public int getFacmNo() {
    return this.facmNo;
  }

/**
	* 額度編號(核准號碼)<br>
	* 
  *
  * @param facmNo 額度編號(核准號碼)
	*/
  public void setFacmNo(int facmNo) {
    this.facmNo = facmNo;
  }

/**
	* 撥款序號<br>
	* 
	* @return Integer
	*/
  public int getBormNo() {
    return this.bormNo;
  }

/**
	* 撥款序號<br>
	* 
  *
  * @param bormNo 撥款序號
	*/
  public void setBormNo(int bormNo) {
    this.bormNo = bormNo;
  }

/**
	* 會計科目<br>
	* 
	* @return String
	*/
  public String getAcCode() {
    return this.acCode == null ? "" : this.acCode;
  }

/**
	* 會計科目<br>
	* 
  *
  * @param acCode 會計科目
	*/
  public void setAcCode(String acCode) {
    this.acCode = acCode;
  }

/**
	* 狀態<br>
	* 辨識是否為帳上客戶或為轉呆客戶
1=帳上客戶；
2=轉呆客戶；
	* @return Integer
	*/
  public int getStatus() {
    return this.status;
  }

/**
	* 狀態<br>
	* 辨識是否為帳上客戶或為轉呆客戶
1=帳上客戶；
2=轉呆客戶；
  *
  * @param status 狀態
	*/
  public void setStatus(int status) {
    this.status = status;
  }

/**
	* 授信行業別<br>
	* 
	* @return String
	*/
  public String getIndustryCode() {
    return this.industryCode == null ? "" : this.industryCode;
  }

/**
	* 授信行業別<br>
	* 
  *
  * @param industryCode 授信行業別
	*/
  public void setIndustryCode(String industryCode) {
    this.industryCode = industryCode;
  }

/**
	* 擔保品類別<br>
	* 
	* @return String
	*/
  public String getClTypeJCIC() {
    return this.clTypeJCIC == null ? "" : this.clTypeJCIC;
  }

/**
	* 擔保品類別<br>
	* 
  *
  * @param clTypeJCIC 擔保品類別
	*/
  public void setClTypeJCIC(String clTypeJCIC) {
    this.clTypeJCIC = clTypeJCIC;
  }

/**
	* 擔保品地區別(郵遞區號)<br>
	* 
	* @return String
	*/
  public String getZip3() {
    return this.zip3 == null ? "" : this.zip3;
  }

/**
	* 擔保品地區別(郵遞區號)<br>
	* 
  *
  * @param zip3 擔保品地區別(郵遞區號)
	*/
  public void setZip3(String zip3) {
    this.zip3 = zip3;
  }

/**
	* 商品利率代碼<br>
	* 
	* @return String
	*/
  public String getProdNo() {
    return this.prodNo == null ? "" : this.prodNo;
  }

/**
	* 商品利率代碼<br>
	* 
  *
  * @param prodNo 商品利率代碼
	*/
  public void setProdNo(String prodNo) {
    this.prodNo = prodNo;
  }

/**
	* 企業戶/個人戶<br>
	* 1=企業戶
2=個人戶
	* @return Integer
	*/
  public int getCustKind() {
    return this.custKind;
  }

/**
	* 企業戶/個人戶<br>
	* 1=企業戶
2=個人戶
  *
  * @param custKind 企業戶/個人戶
	*/
  public void setCustKind(int custKind) {
    this.custKind = custKind;
  }

/**
	* 資料時點是否符合減損客觀證據<br>
	* Y=符合減損客觀證據條件
N=未符合減損客觀證據條件
	* @return String
	*/
  public String getDerFg() {
    return this.derFg == null ? "" : this.derFg;
  }

/**
	* 資料時點是否符合減損客觀證據<br>
	* Y=符合減損客觀證據條件
N=未符合減損客觀證據條件
  *
  * @param derFg 資料時點是否符合減損客觀證據
	*/
  public void setDerFg(String derFg) {
    this.derFg = derFg;
  }

/**
	* 產品別<br>
	* 
	* @return String
	*/
  public String getIfrs9ProdCode() {
    return this.ifrs9ProdCode == null ? "" : this.ifrs9ProdCode;
  }

/**
	* 產品別<br>
	* 
  *
  * @param ifrs9ProdCode 產品別
	*/
  public void setIfrs9ProdCode(String ifrs9ProdCode) {
    this.ifrs9ProdCode = ifrs9ProdCode;
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
    return "Ias34Ep [ias34EpId=" + ias34EpId + ", custId=" + custId
           + ", industryCode=" + industryCode + ", clTypeJCIC=" + clTypeJCIC + ", zip3=" + zip3 + ", prodNo=" + prodNo + ", custKind=" + custKind
           + ", derFg=" + derFg + ", ifrs9ProdCode=" + ifrs9ProdCode + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo
           + "]";
  }
}
