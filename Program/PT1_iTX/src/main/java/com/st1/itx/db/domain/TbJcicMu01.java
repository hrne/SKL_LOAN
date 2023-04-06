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
 * TbJcicMu01 聯徵人員名冊<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`TbJcicMu01`")
public class TbJcicMu01 implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 658045649307384226L;

@EmbeddedId
  private TbJcicMu01Id tbJcicMu01Id;

  // 總行代號
  @Column(name = "`HeadOfficeCode`", length = 3, insertable = false, updatable = false)
  private String headOfficeCode;

  // 分行代號
  @Column(name = "`BranchCode`", length = 4, insertable = false, updatable = false)
  private String branchCode;

  // 資料日期
  @Column(name = "`DataDate`")
  private int dataDate = 0;

  // 員工代號
  @Column(name = "`EmpId`", length = 6, insertable = false, updatable = false)
  private String empId;

  // 職稱
  @Column(name = "`Title`", length = 50)
  private String title;

  // 授權查詢方式
  @Column(name = "`AuthQryType`", length = 1)
  private String authQryType;

  // 使用者代碼
  @Column(name = "`QryUserId`", length = 8)
  private String qryUserId;

  // 授權辦理事項-查詢
  @Column(name = "`AuthItemQuery`", length = 1)
  private String authItemQuery;

  // 授權辦理事項-覆核
  @Column(name = "`AuthItemReview`", length = 1)
  private String authItemReview;

  // 授權辦理事項-其他
  @Column(name = "`AuthItemOther`", length = 1)
  private String authItemOther;

  // 授權起日
  @Column(name = "`AuthStartDay`")
  private int authStartDay = 0;

  // 起日授權主管員工代號
  @Column(name = "`AuthMgrIdS`", length = 6)
  private String authMgrIdS;

  // 授權迄日
  @Column(name = "`AuthEndDay`")
  private int authEndDay = 0;

  // 迄日授權主管員工代號
  @Column(name = "`AuthMgrIdE`", length = 6)
  private String authMgrIdE;

  // E-mail信箱
  @Column(name = "`EmailAccount`", length = 50)
  private String emailAccount;

  // 異動人員ID
  @Column(name = "`ModifyUserId`", length = 10)
  private String modifyUserId;

  // 轉出JCIC文字檔日期
  @Column(name = "`OutJcictxtDate`")
  private int outJcictxtDate = 0;

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


  public TbJcicMu01Id getTbJcicMu01Id() {
    return this.tbJcicMu01Id;
  }

  public void setTbJcicMu01Id(TbJcicMu01Id tbJcicMu01Id) {
    this.tbJcicMu01Id = tbJcicMu01Id;
  }

/**
	* 總行代號<br>
	* 
	* @return String
	*/
  public String getHeadOfficeCode() {
    return this.headOfficeCode == null ? "" : this.headOfficeCode;
  }

/**
	* 總行代號<br>
	* 
  *
  * @param headOfficeCode 總行代號
	*/
  public void setHeadOfficeCode(String headOfficeCode) {
    this.headOfficeCode = headOfficeCode;
  }

/**
	* 分行代號<br>
	* 
	* @return String
	*/
  public String getBranchCode() {
    return this.branchCode == null ? "" : this.branchCode;
  }

/**
	* 分行代號<br>
	* 
  *
  * @param branchCode 分行代號
	*/
  public void setBranchCode(String branchCode) {
    this.branchCode = branchCode;
  }

/**
	* 資料日期<br>
	* 
	* @return Integer
	*/
  public int getDataDate() {
    return StaticTool.bcToRoc(this.dataDate);
  }

/**
	* 資料日期<br>
	* 
  *
  * @param dataDate 資料日期
  * @throws LogicException when Date Is Warn	*/
  public void setDataDate(int dataDate) throws LogicException {
    this.dataDate = StaticTool.rocToBc(dataDate);
  }

/**
	* 員工代號<br>
	* 
	* @return String
	*/
  public String getEmpId() {
    return this.empId == null ? "" : this.empId;
  }

/**
	* 員工代號<br>
	* 
  *
  * @param empId 員工代號
	*/
  public void setEmpId(String empId) {
    this.empId = empId;
  }

/**
	* 職稱<br>
	* 
	* @return String
	*/
  public String getTitle() {
    return this.title == null ? "" : this.title;
  }

/**
	* 職稱<br>
	* 
  *
  * @param title 職稱
	*/
  public void setTitle(String title) {
    this.title = title;
  }

/**
	* 授權查詢方式<br>
	* 
	* @return String
	*/
  public String getAuthQryType() {
    return this.authQryType == null ? "" : this.authQryType;
  }

/**
	* 授權查詢方式<br>
	* 
  *
  * @param authQryType 授權查詢方式
	*/
  public void setAuthQryType(String authQryType) {
    this.authQryType = authQryType;
  }

/**
	* 使用者代碼<br>
	* 
	* @return String
	*/
  public String getQryUserId() {
    return this.qryUserId == null ? "" : this.qryUserId;
  }

/**
	* 使用者代碼<br>
	* 
  *
  * @param qryUserId 使用者代碼
	*/
  public void setQryUserId(String qryUserId) {
    this.qryUserId = qryUserId;
  }

/**
	* 授權辦理事項-查詢<br>
	* 
	* @return String
	*/
  public String getAuthItemQuery() {
    return this.authItemQuery == null ? "" : this.authItemQuery;
  }

/**
	* 授權辦理事項-查詢<br>
	* 
  *
  * @param authItemQuery 授權辦理事項-查詢
	*/
  public void setAuthItemQuery(String authItemQuery) {
    this.authItemQuery = authItemQuery;
  }

/**
	* 授權辦理事項-覆核<br>
	* 
	* @return String
	*/
  public String getAuthItemReview() {
    return this.authItemReview == null ? "" : this.authItemReview;
  }

/**
	* 授權辦理事項-覆核<br>
	* 
  *
  * @param authItemReview 授權辦理事項-覆核
	*/
  public void setAuthItemReview(String authItemReview) {
    this.authItemReview = authItemReview;
  }

/**
	* 授權辦理事項-其他<br>
	* 
	* @return String
	*/
  public String getAuthItemOther() {
    return this.authItemOther == null ? "" : this.authItemOther;
  }

/**
	* 授權辦理事項-其他<br>
	* 
  *
  * @param authItemOther 授權辦理事項-其他
	*/
  public void setAuthItemOther(String authItemOther) {
    this.authItemOther = authItemOther;
  }

/**
	* 授權起日<br>
	* 
	* @return Integer
	*/
  public int getAuthStartDay() {
    return StaticTool.bcToRoc(this.authStartDay);
  }

/**
	* 授權起日<br>
	* 
  *
  * @param authStartDay 授權起日
  * @throws LogicException when Date Is Warn	*/
  public void setAuthStartDay(int authStartDay) throws LogicException {
    this.authStartDay = StaticTool.rocToBc(authStartDay);
  }

/**
	* 起日授權主管員工代號<br>
	* 
	* @return String
	*/
  public String getAuthMgrIdS() {
    return this.authMgrIdS == null ? "" : this.authMgrIdS;
  }

/**
	* 起日授權主管員工代號<br>
	* 
  *
  * @param authMgrIdS 起日授權主管員工代號
	*/
  public void setAuthMgrIdS(String authMgrIdS) {
    this.authMgrIdS = authMgrIdS;
  }

/**
	* 授權迄日<br>
	* 
	* @return Integer
	*/
  public int getAuthEndDay() {
    return StaticTool.bcToRoc(this.authEndDay);
  }

/**
	* 授權迄日<br>
	* 
  *
  * @param authEndDay 授權迄日
  * @throws LogicException when Date Is Warn	*/
  public void setAuthEndDay(int authEndDay) throws LogicException {
    this.authEndDay = StaticTool.rocToBc(authEndDay);
  }

/**
	* 迄日授權主管員工代號<br>
	* 
	* @return String
	*/
  public String getAuthMgrIdE() {
    return this.authMgrIdE == null ? "" : this.authMgrIdE;
  }

/**
	* 迄日授權主管員工代號<br>
	* 
  *
  * @param authMgrIdE 迄日授權主管員工代號
	*/
  public void setAuthMgrIdE(String authMgrIdE) {
    this.authMgrIdE = authMgrIdE;
  }

/**
	* E-mail信箱<br>
	* 
	* @return String
	*/
  public String getEmailAccount() {
    return this.emailAccount == null ? "" : this.emailAccount;
  }

/**
	* E-mail信箱<br>
	* 
  *
  * @param emailAccount E-mail信箱
	*/
  public void setEmailAccount(String emailAccount) {
    this.emailAccount = emailAccount;
  }

/**
	* 異動人員ID<br>
	* 
	* @return String
	*/
  public String getModifyUserId() {
    return this.modifyUserId == null ? "" : this.modifyUserId;
  }

/**
	* 異動人員ID<br>
	* 
  *
  * @param modifyUserId 異動人員ID
	*/
  public void setModifyUserId(String modifyUserId) {
    this.modifyUserId = modifyUserId;
  }

/**
	* 轉出JCIC文字檔日期<br>
	* 
	* @return Integer
	*/
  public int getOutJcictxtDate() {
    return StaticTool.bcToRoc(this.outJcictxtDate);
  }

/**
	* 轉出JCIC文字檔日期<br>
	* 
  *
  * @param outJcictxtDate 轉出JCIC文字檔日期
  * @throws LogicException when Date Is Warn	*/
  public void setOutJcictxtDate(int outJcictxtDate) throws LogicException {
    this.outJcictxtDate = StaticTool.rocToBc(outJcictxtDate);
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
    return "TbJcicMu01 [tbJcicMu01Id=" + tbJcicMu01Id + ", dataDate=" + dataDate + ", title=" + title + ", authQryType=" + authQryType
           + ", qryUserId=" + qryUserId + ", authItemQuery=" + authItemQuery + ", authItemReview=" + authItemReview + ", authItemOther=" + authItemOther + ", authStartDay=" + authStartDay + ", authMgrIdS=" + authMgrIdS
           + ", authEndDay=" + authEndDay + ", authMgrIdE=" + authMgrIdE + ", emailAccount=" + emailAccount + ", modifyUserId=" + modifyUserId + ", outJcictxtDate=" + outJcictxtDate + ", createDate=" + createDate
           + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
