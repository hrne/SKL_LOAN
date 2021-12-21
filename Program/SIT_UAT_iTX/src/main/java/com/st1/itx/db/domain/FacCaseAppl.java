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
 * FacCaseAppl 案件申請檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`FacCaseAppl`")
public class FacCaseAppl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8383136191290903732L;

// 申請號碼
	@Id
	@Column(name = "`ApplNo`")
	private int applNo = 0;

	// 客戶識別碼
	@Column(name = "`CustUKey`", length = 32)
	private String custUKey;

	// 申請日期
	@Column(name = "`ApplDate`")
	private int applDate = 0;

	// 案件編號
	/* 徵審系統案號(eLoan案件編號) */
	@Column(name = "`CreditSysNo`")
	private int creditSysNo = 0;

	// 聯貸案編號
	@Column(name = "`SyndNo`")
	private int syndNo = 0;

	// 申請幣別
	@Column(name = "`CurrencyCode`", length = 3)
	private String currencyCode;

	// 申請金額
	@Column(name = "`ApplAmt`")
	private BigDecimal applAmt = new BigDecimal("0");

	// 申請商品代碼
	@Column(name = "`ProdNo`", length = 5)
	private String prodNo;

	// 估價
	@Column(name = "`Estimate`", length = 6)
	private String estimate;

	// 案件隸屬單位
	/* 共用代碼檔0:非企金單位 1:企金推展課 */
	@Column(name = "`DepartmentCode`", length = 1)
	private String departmentCode;

	// 計件代碼
	/*
	 * 共用代碼檔A:新貸件B:其他額度C:原額度D:新增額度E:展期1:新貸件2:其他額度3:原額度4:新增額度5:展期件6:六個月動支7:服務件8:特殊件9:
	 * 固特利契轉
	 */
	@Column(name = "`PieceCode`", length = 1)
	private String pieceCode;

	// 授信
	@Column(name = "`CreditOfficer`", length = 6)
	private String creditOfficer;

	// 放款專員
	@Column(name = "`LoanOfficer`", length = 6)
	private String loanOfficer;

	// 介紹人
	@Column(name = "`Introducer`", length = 6)
	private String introducer;

	// 協辦人
	@Column(name = "`Coorgnizer`", length = 6)
	private String coorgnizer;

	// 晤談一
	@Column(name = "`InterviewerA`", length = 6)
	private String interviewerA;

	// 晤談二
	@Column(name = "`InterviewerB`", length = 6)
	private String interviewerB;

	// 核決主管
	@Column(name = "`Supervisor`", length = 6)
	private String supervisor;

	// 處理情形
	/* 共用代碼檔0:受理中1:准2:駁 */
	@Column(name = "`ProcessCode`", length = 1)
	private String processCode;

	// 准駁日期
	@Column(name = "`ApproveDate`")
	private int approveDate = 0;

	// 團體戶識別碼
	@Column(name = "`GroupUKey`", length = 32)
	private String groupUKey;

	// 單位別
	@Column(name = "`BranchNo`", length = 4)
	private String branchNo;

	// 是否為授信限制對象
	/* Y:是N:否 */
	@Column(name = "`IsLimit`", length = 1)
	private String isLimit;

	// 是否為利害關係人
	/* Y:是N:否 */
	@Column(name = "`IsRelated`", length = 1)
	private String isRelated;

	// 是否為準利害關係人
	/* Y:是N:否 */
	@Column(name = "`IsLnrelNear`", length = 1)
	private String isLnrelNear;

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
	 * 申請號碼<br>
	 * 
	 * @return Integer
	 */
	public int getApplNo() {
		return this.applNo;
	}

	/**
	 * 申請號碼<br>
	 * 
	 *
	 * @param applNo 申請號碼
	 */
	public void setApplNo(int applNo) {
		this.applNo = applNo;
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
	 * 申請日期<br>
	 * 
	 * @return Integer
	 */
	public int getApplDate() {
		return StaticTool.bcToRoc(this.applDate);
	}

	/**
	 * 申請日期<br>
	 * 
	 *
	 * @param applDate 申請日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setApplDate(int applDate) throws LogicException {
		this.applDate = StaticTool.rocToBc(applDate);
	}

	/**
	 * 案件編號<br>
	 * 徵審系統案號(eLoan案件編號)
	 * 
	 * @return Integer
	 */
	public int getCreditSysNo() {
		return this.creditSysNo;
	}

	/**
	 * 案件編號<br>
	 * 徵審系統案號(eLoan案件編號)
	 *
	 * @param creditSysNo 案件編號
	 */
	public void setCreditSysNo(int creditSysNo) {
		this.creditSysNo = creditSysNo;
	}

	/**
	 * 聯貸案編號<br>
	 * 
	 * @return Integer
	 */
	public int getSyndNo() {
		return this.syndNo;
	}

	/**
	 * 聯貸案編號<br>
	 * 
	 *
	 * @param syndNo 聯貸案編號
	 */
	public void setSyndNo(int syndNo) {
		this.syndNo = syndNo;
	}

	/**
	 * 申請幣別<br>
	 * 
	 * @return String
	 */
	public String getCurrencyCode() {
		return this.currencyCode == null ? "" : this.currencyCode;
	}

	/**
	 * 申請幣別<br>
	 * 
	 *
	 * @param currencyCode 申請幣別
	 */
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	/**
	 * 申請金額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getApplAmt() {
		return this.applAmt;
	}

	/**
	 * 申請金額<br>
	 * 
	 *
	 * @param applAmt 申請金額
	 */
	public void setApplAmt(BigDecimal applAmt) {
		this.applAmt = applAmt;
	}

	/**
	 * 申請商品代碼<br>
	 * 
	 * @return String
	 */
	public String getProdNo() {
		return this.prodNo == null ? "" : this.prodNo;
	}

	/**
	 * 申請商品代碼<br>
	 * 
	 *
	 * @param prodNo 申請商品代碼
	 */
	public void setProdNo(String prodNo) {
		this.prodNo = prodNo;
	}

	/**
	 * 估價<br>
	 * 
	 * @return String
	 */
	public String getEstimate() {
		return this.estimate == null ? "" : this.estimate;
	}

	/**
	 * 估價<br>
	 * 
	 *
	 * @param estimate 估價
	 */
	public void setEstimate(String estimate) {
		this.estimate = estimate;
	}

	/**
	 * 案件隸屬單位<br>
	 * 共用代碼檔 0:非企金單位 1:企金推展課
	 * 
	 * @return String
	 */
	public String getDepartmentCode() {
		return this.departmentCode == null ? "" : this.departmentCode;
	}

	/**
	 * 案件隸屬單位<br>
	 * 共用代碼檔 0:非企金單位 1:企金推展課
	 *
	 * @param departmentCode 案件隸屬單位
	 */
	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	/**
	 * 計件代碼<br>
	 * 共用代碼檔 A:新貸件 B:其他額度 C:原額度 D:新增額度 E:展期 1:新貸件 2:其他額度 3:原額度 4:新增額度 5:展期件 6:六個月動支
	 * 7:服務件 8:特殊件 9:固特利契轉
	 * 
	 * @return String
	 */
	public String getPieceCode() {
		return this.pieceCode == null ? "" : this.pieceCode;
	}

	/**
	 * 計件代碼<br>
	 * 共用代碼檔 A:新貸件 B:其他額度 C:原額度 D:新增額度 E:展期 1:新貸件 2:其他額度 3:原額度 4:新增額度 5:展期件 6:六個月動支
	 * 7:服務件 8:特殊件 9:固特利契轉
	 *
	 * @param pieceCode 計件代碼
	 */
	public void setPieceCode(String pieceCode) {
		this.pieceCode = pieceCode;
	}

	/**
	 * 授信<br>
	 * 
	 * @return String
	 */
	public String getCreditOfficer() {
		return this.creditOfficer == null ? "" : this.creditOfficer;
	}

	/**
	 * 授信<br>
	 * 
	 *
	 * @param creditOfficer 授信
	 */
	public void setCreditOfficer(String creditOfficer) {
		this.creditOfficer = creditOfficer;
	}

	/**
	 * 放款專員<br>
	 * 
	 * @return String
	 */
	public String getLoanOfficer() {
		return this.loanOfficer == null ? "" : this.loanOfficer;
	}

	/**
	 * 放款專員<br>
	 * 
	 *
	 * @param loanOfficer 放款專員
	 */
	public void setLoanOfficer(String loanOfficer) {
		this.loanOfficer = loanOfficer;
	}

	/**
	 * 介紹人<br>
	 * 
	 * @return String
	 */
	public String getIntroducer() {
		return this.introducer == null ? "" : this.introducer;
	}

	/**
	 * 介紹人<br>
	 * 
	 *
	 * @param introducer 介紹人
	 */
	public void setIntroducer(String introducer) {
		this.introducer = introducer;
	}

	/**
	 * 協辦人<br>
	 * 
	 * @return String
	 */
	public String getCoorgnizer() {
		return this.coorgnizer == null ? "" : this.coorgnizer;
	}

	/**
	 * 協辦人<br>
	 * 
	 *
	 * @param coorgnizer 協辦人
	 */
	public void setCoorgnizer(String coorgnizer) {
		this.coorgnizer = coorgnizer;
	}

	/**
	 * 晤談一<br>
	 * 
	 * @return String
	 */
	public String getInterviewerA() {
		return this.interviewerA == null ? "" : this.interviewerA;
	}

	/**
	 * 晤談一<br>
	 * 
	 *
	 * @param interviewerA 晤談一
	 */
	public void setInterviewerA(String interviewerA) {
		this.interviewerA = interviewerA;
	}

	/**
	 * 晤談二<br>
	 * 
	 * @return String
	 */
	public String getInterviewerB() {
		return this.interviewerB == null ? "" : this.interviewerB;
	}

	/**
	 * 晤談二<br>
	 * 
	 *
	 * @param interviewerB 晤談二
	 */
	public void setInterviewerB(String interviewerB) {
		this.interviewerB = interviewerB;
	}

	/**
	 * 核決主管<br>
	 * 
	 * @return String
	 */
	public String getSupervisor() {
		return this.supervisor == null ? "" : this.supervisor;
	}

	/**
	 * 核決主管<br>
	 * 
	 *
	 * @param supervisor 核決主管
	 */
	public void setSupervisor(String supervisor) {
		this.supervisor = supervisor;
	}

	/**
	 * 處理情形<br>
	 * 共用代碼檔 0:受理中 1:准 2:駁
	 * 
	 * @return String
	 */
	public String getProcessCode() {
		return this.processCode == null ? "" : this.processCode;
	}

	/**
	 * 處理情形<br>
	 * 共用代碼檔 0:受理中 1:准 2:駁
	 *
	 * @param processCode 處理情形
	 */
	public void setProcessCode(String processCode) {
		this.processCode = processCode;
	}

	/**
	 * 准駁日期<br>
	 * 
	 * @return Integer
	 */
	public int getApproveDate() {
		return StaticTool.bcToRoc(this.approveDate);
	}

	/**
	 * 准駁日期<br>
	 * 
	 *
	 * @param approveDate 准駁日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setApproveDate(int approveDate) throws LogicException {
		this.approveDate = StaticTool.rocToBc(approveDate);
	}

	/**
	 * 團體戶識別碼<br>
	 * 
	 * @return String
	 */
	public String getGroupUKey() {
		return this.groupUKey == null ? "" : this.groupUKey;
	}

	/**
	 * 團體戶識別碼<br>
	 * 
	 *
	 * @param groupUKey 團體戶識別碼
	 */
	public void setGroupUKey(String groupUKey) {
		this.groupUKey = groupUKey;
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

	/**
	 * 是否為授信限制對象<br>
	 * Y:是 N:否
	 * 
	 * @return String
	 */
	public String getIsLimit() {
		return this.isLimit == null ? "" : this.isLimit;
	}

	/**
	 * 是否為授信限制對象<br>
	 * Y:是 N:否
	 *
	 * @param isLimit 是否為授信限制對象
	 */
	public void setIsLimit(String isLimit) {
		this.isLimit = isLimit;
	}

	/**
	 * 是否為利害關係人<br>
	 * Y:是 N:否
	 * 
	 * @return String
	 */
	public String getIsRelated() {
		return this.isRelated == null ? "" : this.isRelated;
	}

	/**
	 * 是否為利害關係人<br>
	 * Y:是 N:否
	 *
	 * @param isRelated 是否為利害關係人
	 */
	public void setIsRelated(String isRelated) {
		this.isRelated = isRelated;
	}

	/**
	 * 是否為準利害關係人<br>
	 * Y:是 N:否
	 * 
	 * @return String
	 */
	public String getIsLnrelNear() {
		return this.isLnrelNear == null ? "" : this.isLnrelNear;
	}

	/**
	 * 是否為準利害關係人<br>
	 * Y:是 N:否
	 *
	 * @param isLnrelNear 是否為準利害關係人
	 */
	public void setIsLnrelNear(String isLnrelNear) {
		this.isLnrelNear = isLnrelNear;
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
		return "FacCaseAppl [applNo=" + applNo + ", custUKey=" + custUKey + ", applDate=" + applDate + ", creditSysNo=" + creditSysNo + ", syndNo=" + syndNo + ", currencyCode=" + currencyCode
				+ ", applAmt=" + applAmt + ", prodNo=" + prodNo + ", estimate=" + estimate + ", departmentCode=" + departmentCode + ", pieceCode=" + pieceCode + ", creditOfficer=" + creditOfficer
				+ ", loanOfficer=" + loanOfficer + ", introducer=" + introducer + ", coorgnizer=" + coorgnizer + ", interviewerA=" + interviewerA + ", interviewerB=" + interviewerB + ", supervisor="
				+ supervisor + ", processCode=" + processCode + ", approveDate=" + approveDate + ", groupUKey=" + groupUKey + ", branchNo=" + branchNo + ", isLimit=" + isLimit + ", isRelated="
				+ isRelated + ", isLnrelNear=" + isLnrelNear + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo
				+ "]";
	}
}
