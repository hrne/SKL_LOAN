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
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * LoanNotYet 未齊件管理檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`LoanNotYet`")
public class LoanNotYet implements Serializable {


  @EmbeddedId
  private LoanNotYetId loanNotYetId;

  // 借款人戶號
  @Column(name = "`CustNo`", insertable = false, updatable = false)
  private int custNo = 0;

  // 額度編號
  @Column(name = "`FacmNo`", insertable = false, updatable = false)
  private int facmNo = 0;

  // 未齊件代碼
  /* 共用代碼檔01:代償後謄本02:火險單03:借款申請書04:顧客資料表05:公司章程06:公司執照07:董監名冊08:股東名冊09:會計師簽證或期中報表10:公司戶營業稅或所得稅申報資料11:資金運用計畫書12:土地使用計畫書13:建築執照14:董監會借款決議紀錄15:個人戶所得稅申報資料16:債權憑證補章17:補辦對保手續18:謄本20:定存單99:其他 */
  @Column(name = "`NotYetCode`", length = 2, insertable = false, updatable = false)
  private String notYetCode;

  // 齊件日期
  @Column(name = "`YetDate`")
  private int yetDate = 0;

  // 銷號日期
  @Column(name = "`CloseDate`")
  private int closeDate = 0;

  // 備註
  @Column(name = "`ReMark`", length = 80)
  private String reMark;

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

  // 單位別
  @Column(name = "`BranchNo`", length = 4)
  private String branchNo;


  public LoanNotYetId getLoanNotYetId() {
    return this.loanNotYetId;
  }

  public void setLoanNotYetId(LoanNotYetId loanNotYetId) {
    this.loanNotYetId = loanNotYetId;
  }

/**
	* 借款人戶號<br>
	* 
	* @return Integer
	*/
  public int getCustNo() {
    return this.custNo;
  }

/**
	* 借款人戶號<br>
	* 
  *
  * @param custNo 借款人戶號
	*/
  public void setCustNo(int custNo) {
    this.custNo = custNo;
  }

/**
	* 額度編號<br>
	* 
	* @return Integer
	*/
  public int getFacmNo() {
    return this.facmNo;
  }

/**
	* 額度編號<br>
	* 
  *
  * @param facmNo 額度編號
	*/
  public void setFacmNo(int facmNo) {
    this.facmNo = facmNo;
  }

/**
	* 未齊件代碼<br>
	* 共用代碼檔
01:代償後謄本
02:火險單
03:借款申請書
04:顧客資料表
05:公司章程
06:公司執照
07:董監名冊
08:股東名冊
09:會計師簽證或期中報表
10:公司戶營業稅或所得稅申報資料
11:資金運用計畫書
12:土地使用計畫書
13:建築執照
14:董監會借款決議紀錄
15:個人戶所得稅申報資料
16:債權憑證補章
17:補辦對保手續
18:謄本
20:定存單
99:其他
	* @return String
	*/
  public String getNotYetCode() {
    return this.notYetCode == null ? "" : this.notYetCode;
  }

/**
	* 未齊件代碼<br>
	* 共用代碼檔
01:代償後謄本
02:火險單
03:借款申請書
04:顧客資料表
05:公司章程
06:公司執照
07:董監名冊
08:股東名冊
09:會計師簽證或期中報表
10:公司戶營業稅或所得稅申報資料
11:資金運用計畫書
12:土地使用計畫書
13:建築執照
14:董監會借款決議紀錄
15:個人戶所得稅申報資料
16:債權憑證補章
17:補辦對保手續
18:謄本
20:定存單
99:其他
  *
  * @param notYetCode 未齊件代碼
	*/
  public void setNotYetCode(String notYetCode) {
    this.notYetCode = notYetCode;
  }

/**
	* 齊件日期<br>
	* 
	* @return Integer
	*/
  public int getYetDate() {
    return StaticTool.bcToRoc(this.yetDate);
  }

/**
	* 齊件日期<br>
	* 
  *
  * @param yetDate 齊件日期
  * @throws LogicException when Date Is Warn	*/
  public void setYetDate(int yetDate) throws LogicException {
    this.yetDate = StaticTool.rocToBc(yetDate);
  }

/**
	* 銷號日期<br>
	* 
	* @return Integer
	*/
  public int getCloseDate() {
    return StaticTool.bcToRoc(this.closeDate);
  }

/**
	* 銷號日期<br>
	* 
  *
  * @param closeDate 銷號日期
  * @throws LogicException when Date Is Warn	*/
  public void setCloseDate(int closeDate) throws LogicException {
    this.closeDate = StaticTool.rocToBc(closeDate);
  }

/**
	* 備註<br>
	* 
	* @return String
	*/
  public String getReMark() {
    return this.reMark == null ? "" : this.reMark;
  }

/**
	* 備註<br>
	* 
  *
  * @param reMark 備註
	*/
  public void setReMark(String reMark) {
    this.reMark = reMark;
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


  @Override
  public String toString() {
    return "LoanNotYet [loanNotYetId=" + loanNotYetId + ", yetDate=" + yetDate + ", closeDate=" + closeDate + ", reMark=" + reMark
           + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + ", branchNo=" + branchNo + "]";
  }
}
