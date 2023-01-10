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
 * LifeRelHead 人壽利關人負責人檔T07、TA07
(使用報表：LM013、LM042、LM050)<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`LifeRelHead`")
public class LifeRelHead implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -240005218157062533L;

@EmbeddedId
  private LifeRelHeadId lifeRelHeadId;

  // 與本公司關之關係
  /* 代碼說明：A：保險業負責人(依據保險業負責人應居被資格條件準則)B：辦理授信之值元C：主要股東(係指具有本公司已發行股份總數10%以上或前十大持股比率或有指派董監事之股東)D：本公司對其有控制與從屬關係之公司(請依公司法第369-1~369-3條、第369-9條、及第369-11條及關係企業合併營業報告書關係企業合併財務報表及關係報告書編製準則第六條之規定)E：本公司放款金額超過一億元以上之對象F：同一關係企業G：內勤職員Z：其他 */
  @Column(name = "`RelWithCompany`", length = 1)
  private String relWithCompany;

  // 負責人身分證/統一編號
  @Column(name = "`HeadId`", length = 10, insertable = false, updatable = false)
  private String headId;

  // 負責人名稱
  @Column(name = "`HeadName`", length = 100)
  private String headName;

  // 關係人職稱
  /* A：董事長B：副董事長C：董事D：監事E：總經理F：副總經理G：協理H：經理I：副理J：其他 */
  @Column(name = "`HeadTitle`", length = 50)
  private String headTitle;

  // 負責人關係人身分證/統一編號
  @Column(name = "`RelId`", length = 10, insertable = false, updatable = false)
  private String relId;

  // 關係人親屬姓名
  @Column(name = "`RelName`", length = 50)
  private String relName;

  // 關係人親屬親等
  /* A：三親等以內血親B：二親等以內姻親C：其他NULL：本人 */
  @Column(name = "`RelKinShip`", length = 1)
  private String relKinShip;

  // 關係人親屬稱謂
  @Column(name = "`RelTitle`", length = 10)
  private String relTitle;

  // 事業負責人身分證/統一編號
  @Column(name = "`BusId`", length = 10, insertable = false, updatable = false)
  private String busId;

  // 事業名稱
  @Column(name = "`BusName`", length = 100)
  private String busName;

  // 事業持股比率
  @Column(name = "`ShareHoldingRatio`")
  private int shareHoldingRatio = 0;

  // 事業擔任職務
  /* 暫不以代號列入：A：董事長B：副董事長C：董事D：監事E：總經理F：副總經理G：協理H：經理I：副理J：其他 */
  @Column(name = "`BusTitle`", length = 50)
  private String busTitle;

  // 核貸金額
  @Column(name = "`LineAmt`")
  private BigDecimal lineAmt = new BigDecimal("0");

  // 放款金額
  @Column(name = "`LoanBalance`")
  private BigDecimal loanBalance = new BigDecimal("0");

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


  public LifeRelHeadId getLifeRelHeadId() {
    return this.lifeRelHeadId;
  }

  public void setLifeRelHeadId(LifeRelHeadId lifeRelHeadId) {
    this.lifeRelHeadId = lifeRelHeadId;
  }

/**
	* 與本公司關之關係<br>
	* 代碼說明：
A：保險業負責人(依據保險業負責人應居被資格條件準則)
B：辦理授信之值元
C：主要股東(係指具有本公司已發行股份總數10%以上或前十大持股比率或有指派董監事之股東)
D：本公司對其有控制與從屬關係之公司(請依公司法第369-1~369-3條、第369-9條、及第369-11條及關係企業合併營業報告書關係企業合併財務報表及關係報告書編製準則第六條之規定)
E：本公司放款金額超過一億元以上之對象
F：同一關係企業
G：內勤職員
Z：其他
	* @return String
	*/
  public String getRelWithCompany() {
    return this.relWithCompany == null ? "" : this.relWithCompany;
  }

/**
	* 與本公司關之關係<br>
	* 代碼說明：
A：保險業負責人(依據保險業負責人應居被資格條件準則)
B：辦理授信之值元
C：主要股東(係指具有本公司已發行股份總數10%以上或前十大持股比率或有指派董監事之股東)
D：本公司對其有控制與從屬關係之公司(請依公司法第369-1~369-3條、第369-9條、及第369-11條及關係企業合併營業報告書關係企業合併財務報表及關係報告書編製準則第六條之規定)
E：本公司放款金額超過一億元以上之對象
F：同一關係企業
G：內勤職員
Z：其他
  *
  * @param relWithCompany 與本公司關之關係
	*/
  public void setRelWithCompany(String relWithCompany) {
    this.relWithCompany = relWithCompany;
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
	* 負責人名稱<br>
	* 
	* @return String
	*/
  public String getHeadName() {
    return this.headName == null ? "" : this.headName;
  }

/**
	* 負責人名稱<br>
	* 
  *
  * @param headName 負責人名稱
	*/
  public void setHeadName(String headName) {
    this.headName = headName;
  }

/**
	* 關係人職稱<br>
	* A：董事長
B：副董事長
C：董事
D：監事
E：總經理
F：副總經理
G：協理
H：經理
I：副理
J：其他
	* @return String
	*/
  public String getHeadTitle() {
    return this.headTitle == null ? "" : this.headTitle;
  }

/**
	* 關係人職稱<br>
	* A：董事長
B：副董事長
C：董事
D：監事
E：總經理
F：副總經理
G：協理
H：經理
I：副理
J：其他
  *
  * @param headTitle 關係人職稱
	*/
  public void setHeadTitle(String headTitle) {
    this.headTitle = headTitle;
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
	* 關係人親屬姓名<br>
	* 
	* @return String
	*/
  public String getRelName() {
    return this.relName == null ? "" : this.relName;
  }

/**
	* 關係人親屬姓名<br>
	* 
  *
  * @param relName 關係人親屬姓名
	*/
  public void setRelName(String relName) {
    this.relName = relName;
  }

/**
	* 關係人親屬親等<br>
	* A：三親等以內血親
B：二親等以內姻親
C：其他
NULL：本人
	* @return String
	*/
  public String getRelKinShip() {
    return this.relKinShip == null ? "" : this.relKinShip;
  }

/**
	* 關係人親屬親等<br>
	* A：三親等以內血親
B：二親等以內姻親
C：其他
NULL：本人
  *
  * @param relKinShip 關係人親屬親等
	*/
  public void setRelKinShip(String relKinShip) {
    this.relKinShip = relKinShip;
  }

/**
	* 關係人親屬稱謂<br>
	* 
	* @return String
	*/
  public String getRelTitle() {
    return this.relTitle == null ? "" : this.relTitle;
  }

/**
	* 關係人親屬稱謂<br>
	* 
  *
  * @param relTitle 關係人親屬稱謂
	*/
  public void setRelTitle(String relTitle) {
    this.relTitle = relTitle;
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
	* 事業名稱<br>
	* 
	* @return String
	*/
  public String getBusName() {
    return this.busName == null ? "" : this.busName;
  }

/**
	* 事業名稱<br>
	* 
  *
  * @param busName 事業名稱
	*/
  public void setBusName(String busName) {
    this.busName = busName;
  }

/**
	* 事業持股比率<br>
	* 
	* @return Integer
	*/
  public int getShareHoldingRatio() {
    return this.shareHoldingRatio;
  }

/**
	* 事業持股比率<br>
	* 
  *
  * @param shareHoldingRatio 事業持股比率
	*/
  public void setShareHoldingRatio(int shareHoldingRatio) {
    this.shareHoldingRatio = shareHoldingRatio;
  }

/**
	* 事業擔任職務<br>
	* 暫不以代號列入：
A：董事長
B：副董事長
C：董事
D：監事
E：總經理
F：副總經理
G：協理
H：經理
I：副理
J：其他
	* @return String
	*/
  public String getBusTitle() {
    return this.busTitle == null ? "" : this.busTitle;
  }

/**
	* 事業擔任職務<br>
	* 暫不以代號列入：
A：董事長
B：副董事長
C：董事
D：監事
E：總經理
F：副總經理
G：協理
H：經理
I：副理
J：其他
  *
  * @param busTitle 事業擔任職務
	*/
  public void setBusTitle(String busTitle) {
    this.busTitle = busTitle;
  }

/**
	* 核貸金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLineAmt() {
    return this.lineAmt;
  }

/**
	* 核貸金額<br>
	* 
  *
  * @param lineAmt 核貸金額
	*/
  public void setLineAmt(BigDecimal lineAmt) {
    this.lineAmt = lineAmt;
  }

/**
	* 放款金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLoanBalance() {
    return this.loanBalance;
  }

/**
	* 放款金額<br>
	* 
  *
  * @param loanBalance 放款金額
	*/
  public void setLoanBalance(BigDecimal loanBalance) {
    this.loanBalance = loanBalance;
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
    return "LifeRelHead [lifeRelHeadId=" + lifeRelHeadId + ", relWithCompany=" + relWithCompany + ", headName=" + headName + ", headTitle=" + headTitle + ", relName=" + relName
           + ", relKinShip=" + relKinShip + ", relTitle=" + relTitle + ", busName=" + busName + ", shareHoldingRatio=" + shareHoldingRatio + ", busTitle=" + busTitle
           + ", lineAmt=" + lineAmt + ", loanBalance=" + loanBalance + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo
           + "]";
  }
}
