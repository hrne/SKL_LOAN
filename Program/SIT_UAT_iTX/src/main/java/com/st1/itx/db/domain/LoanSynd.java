package com.st1.itx.db.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.Id;
import javax.persistence.Column;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * LoanSynd 聯貸案訂約檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`LoanSynd`")
public class LoanSynd implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -3370639796702902025L;

// 聯貸編號
  /* 自動編號民國年(3)+流水號(3) */
  @Id
  @Column(name = "`SyndNo`")
  private int syndNo = 0;

  // 聯貸名稱
  @Column(name = "`SyndName`", length = 60)
  private String syndName;

  // 主辦行
  @Column(name = "`LeadingBank`", length = 7)
  private String leadingBank;

  // 代理行
  /* 擔保品管理行 */
  @Column(name = "`AgentBank`", length = 7)
  private String agentBank;

  // 簽約日
  @Column(name = "`SigningDate`")
  private int signingDate = 0;

  // 國內或國際聯貸
  /* CdCode.SyndTypeCodeA:國內B:國際 */
  @Column(name = "`SyndTypeCodeFlag`", length = 1)
  private String syndTypeCodeFlag;

  // 參貸費率
  @Column(name = "`PartRate`")
  private BigDecimal partRate = new BigDecimal("0");

  // 幣別
  @Column(name = "`CurrencyCode`", length = 3)
  private String currencyCode;

  // 聯貸總金額
  @Column(name = "`SyndAmt`")
  private BigDecimal syndAmt = new BigDecimal("0");

  // 參貸金額
  @Column(name = "`PartAmt`")
  private BigDecimal partAmt = new BigDecimal("0");

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


/**
	* 聯貸編號<br>
	* 自動編號
民國年(3)+流水號(3)
	* @return Integer
	*/
  public int getSyndNo() {
    return this.syndNo;
  }

/**
	* 聯貸編號<br>
	* 自動編號
民國年(3)+流水號(3)
  *
  * @param syndNo 聯貸編號
	*/
  public void setSyndNo(int syndNo) {
    this.syndNo = syndNo;
  }

/**
	* 聯貸名稱<br>
	* 
	* @return String
	*/
  public String getSyndName() {
    return this.syndName == null ? "" : this.syndName;
  }

/**
	* 聯貸名稱<br>
	* 
  *
  * @param syndName 聯貸名稱
	*/
  public void setSyndName(String syndName) {
    this.syndName = syndName;
  }

/**
	* 主辦行<br>
	* 
	* @return String
	*/
  public String getLeadingBank() {
    return this.leadingBank == null ? "" : this.leadingBank;
  }

/**
	* 主辦行<br>
	* 
  *
  * @param leadingBank 主辦行
	*/
  public void setLeadingBank(String leadingBank) {
    this.leadingBank = leadingBank;
  }

/**
	* 代理行<br>
	* 擔保品管理行
	* @return String
	*/
  public String getAgentBank() {
    return this.agentBank == null ? "" : this.agentBank;
  }

/**
	* 代理行<br>
	* 擔保品管理行
  *
  * @param agentBank 代理行
	*/
  public void setAgentBank(String agentBank) {
    this.agentBank = agentBank;
  }

/**
	* 簽約日<br>
	* 
	* @return Integer
	*/
  public int getSigningDate() {
    return StaticTool.bcToRoc(this.signingDate);
  }

/**
	* 簽約日<br>
	* 
  *
  * @param signingDate 簽約日
  * @throws LogicException when Date Is Warn	*/
  public void setSigningDate(int signingDate) throws LogicException {
    this.signingDate = StaticTool.rocToBc(signingDate);
  }

/**
	* 國內或國際聯貸<br>
	* CdCode.SyndTypeCode
A:國內
B:國際
	* @return String
	*/
  public String getSyndTypeCodeFlag() {
    return this.syndTypeCodeFlag == null ? "" : this.syndTypeCodeFlag;
  }

/**
	* 國內或國際聯貸<br>
	* CdCode.SyndTypeCode
A:國內
B:國際
  *
  * @param syndTypeCodeFlag 國內或國際聯貸
	*/
  public void setSyndTypeCodeFlag(String syndTypeCodeFlag) {
    this.syndTypeCodeFlag = syndTypeCodeFlag;
  }

/**
	* 參貸費率<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getPartRate() {
    return this.partRate;
  }

/**
	* 參貸費率<br>
	* 
  *
  * @param partRate 參貸費率
	*/
  public void setPartRate(BigDecimal partRate) {
    this.partRate = partRate;
  }

/**
	* 幣別<br>
	* 
	* @return String
	*/
  public String getCurrencyCode() {
    return this.currencyCode == null ? "" : this.currencyCode;
  }

/**
	* 幣別<br>
	* 
  *
  * @param currencyCode 幣別
	*/
  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
  }

/**
	* 聯貸總金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getSyndAmt() {
    return this.syndAmt;
  }

/**
	* 聯貸總金額<br>
	* 
  *
  * @param syndAmt 聯貸總金額
	*/
  public void setSyndAmt(BigDecimal syndAmt) {
    this.syndAmt = syndAmt;
  }

/**
	* 參貸金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getPartAmt() {
    return this.partAmt;
  }

/**
	* 參貸金額<br>
	* 
  *
  * @param partAmt 參貸金額
	*/
  public void setPartAmt(BigDecimal partAmt) {
    this.partAmt = partAmt;
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
    return "LoanSynd [syndNo=" + syndNo + ", syndName=" + syndName + ", leadingBank=" + leadingBank + ", agentBank=" + agentBank + ", signingDate=" + signingDate + ", syndTypeCodeFlag=" + syndTypeCodeFlag
           + ", partRate=" + partRate + ", currencyCode=" + currencyCode + ", syndAmt=" + syndAmt + ", partAmt=" + partAmt + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
           + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
