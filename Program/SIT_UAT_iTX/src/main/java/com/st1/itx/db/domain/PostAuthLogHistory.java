package com.st1.itx.db.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * PostAuthLogHistory 郵局授權記錄歷史檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`PostAuthLogHistory`")
public class PostAuthLogHistory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3330579967690334568L;

// 序號
	@Id
	@Column(name = "`LogNo`")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "`PostAuthLogHistory_SEQ`")
	@SequenceGenerator(name = "`PostAuthLogHistory_SEQ`", sequenceName = "`PostAuthLogHistory_SEQ`", allocationSize = 1)
	private Long logNo = 0L;

	// 戶號
	@Column(name = "`CustNo`")
	private int custNo = 0;

	// 額度
	@Column(name = "`FacmNo`")
	private int facmNo = 0;

	// 授權方式
	/* CdCode:AuthCode1期款2火險 */
	@Column(name = "`AuthCode`", length = 1)
	private String authCode;

	// 建檔日期
	/* 日曆日 */
	@Column(name = "`AuthCreateDate`")
	private int authCreateDate = 0;

	// 申請代號，狀態碼
	/* CdCode:AuthApplCode1.申請2.終止3.郵局終止4.誤終止8.恢復授權9.暫停授權 */
	@Column(name = "`AuthApplCode`", length = 1)
	private String authApplCode;

	// 帳戶別
	/* CdCode:PostDepCodeP：存簿G：劃撥 */
	@Column(name = "`PostDepCode`", length = 1)
	private String postDepCode;

	// 儲金帳號
	@Column(name = "`RepayAcct`", length = 14)
	private String repayAcct;

	// 統一編號
	@Column(name = "`CustId`", length = 10)
	private String custId;

	// 帳號碼
	@Column(name = "`RepayAcctSeq`", length = 2)
	private String repayAcctSeq;

	// 處理日期
	/* 日曆日 */
	@Column(name = "`ProcessDate`")
	private int processDate = 0;

	// 核印完成日期
	@Column(name = "`StampFinishDate`")
	private int stampFinishDate = 0;

	// 核印取消日期
	@Column(name = "`StampCancelDate`")
	private int stampCancelDate = 0;

	// 核印註記
	/* CdCode:StampCode1局帳號不符2戶名不符3身分證號不符4印鑑不符9其他 */
	@Column(name = "`StampCode`", length = 1)
	private String stampCode;

	// 媒體碼
	/* 未產出前:空白產出後:"Y" */
	@Column(name = "`PostMediaCode`", length = 1)
	private String postMediaCode;

	// 狀況代號，授權狀態
	/*
	 * CdCode:AuthErrorCode空白:未授權00:成功03:已終止代繳06:凍結警示戶07:支票專戶08:帳號錯誤09:終止戶10:身分證不符11
	 * :轉出戶12:拒絕往來戶13:無此編號14:編號已存在16:管制帳戶17:掛失戶18:異常帳戶19:編號非英數91:期限未扣款98:其他
	 */
	@Column(name = "`AuthErrorCode`", length = 2)
	private String authErrorCode;

	// 媒體檔流水編號
	/* 媒體產出前為0 */
	@Column(name = "`FileSeq`")
	private int fileSeq = 0;

	// 提出日期(媒體產出日)
	/* 日曆日 */
	@Column(name = "`PropDate`")
	private int propDate = 0;

	// 提回日期
	/* 日曆日 */
	@Column(name = "`RetrDate`")
	private int retrDate = 0;

	// 刪除日期/暫停授權日期
	/* 日曆日 */
	@Column(name = "`DeleteDate`")
	private int deleteDate = 0;

	// 與借款人關係
	/* CdCode:RelationCode00本人01夫02妻03父04母05子06女07兄08弟09姊10妹11姪子99其他 */
	@Column(name = "`RelationCode`", length = 2)
	private String relationCode;

	// 第三人帳戶戶名
	@Column(name = "`RelAcctName`", length = 100)
	private String relAcctName;

	// 第三人身分證字號
	@Column(name = "`RelationId`", length = 10)
	private String relationId;

	// 第三人出生日期
	@Column(name = "`RelAcctBirthday`")
	private int relAcctBirthday = 0;

	// 第三人性別
	/* CdCode:Sex */
	@Column(name = "`RelAcctGender`", length = 1)
	private String relAcctGender;

	// AML回應碼
	/* CdCode:AmlCheckItem0.非可疑名單/已完成名單確認1.需審查/確認2.為凍結名單/未確定名單 */
	@Column(name = "`AmlRsp`", length = 1)
	private String amlRsp;

	// 建立者櫃員編號
	@Column(name = "`CreateEmpNo`", length = 6)
	private String createEmpNo;

	// 建檔日期
	@CreatedDate
	@Column(name = "`CreateDate`")
	private java.sql.Timestamp createDate;

	// 修改者櫃員編號
	@Column(name = "`LastUpdateEmpNo`", length = 6)
	private String lastUpdateEmpNo;

	// 異動日期
	@LastModifiedDate
	@Column(name = "`LastUpdate`")
	private java.sql.Timestamp lastUpdate;

	/**
	 * 序號<br>
	 * 
	 * @return Long
	 */
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getLogNo() {
		return this.logNo;
	}

	/**
	 * 序號<br>
	 * 
	 *
	 * @param logNo 序號
	 */
	public void setLogNo(Long logNo) {
		this.logNo = logNo;
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
	 * 額度<br>
	 * 
	 * @return Integer
	 */
	public int getFacmNo() {
		return this.facmNo;
	}

	/**
	 * 額度<br>
	 * 
	 *
	 * @param facmNo 額度
	 */
	public void setFacmNo(int facmNo) {
		this.facmNo = facmNo;
	}

	/**
	 * 授權方式<br>
	 * CdCode:AuthCode 1期款2火險
	 * 
	 * @return String
	 */
	public String getAuthCode() {
		return this.authCode == null ? "" : this.authCode;
	}

	/**
	 * 授權方式<br>
	 * CdCode:AuthCode 1期款2火險
	 *
	 * @param authCode 授權方式
	 */
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	/**
	 * 建檔日期<br>
	 * 日曆日
	 * 
	 * @return Integer
	 */
	public int getAuthCreateDate() {
		return StaticTool.bcToRoc(this.authCreateDate);
	}

	/**
	 * 建檔日期<br>
	 * 日曆日
	 *
	 * @param authCreateDate 建檔日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setAuthCreateDate(int authCreateDate) throws LogicException {
		this.authCreateDate = StaticTool.rocToBc(authCreateDate);
	}

	/**
	 * 申請代號，狀態碼<br>
	 * CdCode:AuthApplCode 1.申請 2.終止 3.郵局終止 4.誤終止 8.恢復授權 9.暫停授權
	 * 
	 * @return String
	 */
	public String getAuthApplCode() {
		return this.authApplCode == null ? "" : this.authApplCode;
	}

	/**
	 * 申請代號，狀態碼<br>
	 * CdCode:AuthApplCode 1.申請 2.終止 3.郵局終止 4.誤終止 8.恢復授權 9.暫停授權
	 *
	 * @param authApplCode 申請代號，狀態碼
	 */
	public void setAuthApplCode(String authApplCode) {
		this.authApplCode = authApplCode;
	}

	/**
	 * 帳戶別<br>
	 * CdCode:PostDepCode P：存簿G：劃撥
	 * 
	 * @return String
	 */
	public String getPostDepCode() {
		return this.postDepCode == null ? "" : this.postDepCode;
	}

	/**
	 * 帳戶別<br>
	 * CdCode:PostDepCode P：存簿G：劃撥
	 *
	 * @param postDepCode 帳戶別
	 */
	public void setPostDepCode(String postDepCode) {
		this.postDepCode = postDepCode;
	}

	/**
	 * 儲金帳號<br>
	 * 
	 * @return String
	 */
	public String getRepayAcct() {
		return this.repayAcct == null ? "" : this.repayAcct;
	}

	/**
	 * 儲金帳號<br>
	 * 
	 *
	 * @param repayAcct 儲金帳號
	 */
	public void setRepayAcct(String repayAcct) {
		this.repayAcct = repayAcct;
	}

	/**
	 * 統一編號<br>
	 * 
	 * @return String
	 */
	public String getCustId() {
		return this.custId == null ? "" : this.custId;
	}

	/**
	 * 統一編號<br>
	 * 
	 *
	 * @param custId 統一編號
	 */
	public void setCustId(String custId) {
		this.custId = custId;
	}

	/**
	 * 帳號碼<br>
	 * 
	 * @return String
	 */
	public String getRepayAcctSeq() {
		return this.repayAcctSeq == null ? "" : this.repayAcctSeq;
	}

	/**
	 * 帳號碼<br>
	 * 
	 *
	 * @param repayAcctSeq 帳號碼
	 */
	public void setRepayAcctSeq(String repayAcctSeq) {
		this.repayAcctSeq = repayAcctSeq;
	}

	/**
	 * 處理日期<br>
	 * 日曆日
	 * 
	 * @return Integer
	 */
	public int getProcessDate() {
		return StaticTool.bcToRoc(this.processDate);
	}

	/**
	 * 處理日期<br>
	 * 日曆日
	 *
	 * @param processDate 處理日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setProcessDate(int processDate) throws LogicException {
		this.processDate = StaticTool.rocToBc(processDate);
	}

	/**
	 * 核印完成日期<br>
	 * 
	 * @return Integer
	 */
	public int getStampFinishDate() {
		return StaticTool.bcToRoc(this.stampFinishDate);
	}

	/**
	 * 核印完成日期<br>
	 * 
	 *
	 * @param stampFinishDate 核印完成日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setStampFinishDate(int stampFinishDate) throws LogicException {
		this.stampFinishDate = StaticTool.rocToBc(stampFinishDate);
	}

	/**
	 * 核印取消日期<br>
	 * 
	 * @return Integer
	 */
	public int getStampCancelDate() {
		return StaticTool.bcToRoc(this.stampCancelDate);
	}

	/**
	 * 核印取消日期<br>
	 * 
	 *
	 * @param stampCancelDate 核印取消日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setStampCancelDate(int stampCancelDate) throws LogicException {
		this.stampCancelDate = StaticTool.rocToBc(stampCancelDate);
	}

	/**
	 * 核印註記<br>
	 * CdCode:StampCode 1局帳號不符 2戶名不符 3身分證號不符 4印鑑不符 9其他
	 * 
	 * @return String
	 */
	public String getStampCode() {
		return this.stampCode == null ? "" : this.stampCode;
	}

	/**
	 * 核印註記<br>
	 * CdCode:StampCode 1局帳號不符 2戶名不符 3身分證號不符 4印鑑不符 9其他
	 *
	 * @param stampCode 核印註記
	 */
	public void setStampCode(String stampCode) {
		this.stampCode = stampCode;
	}

	/**
	 * 媒體碼<br>
	 * 未產出前:空白 產出後:"Y"
	 * 
	 * @return String
	 */
	public String getPostMediaCode() {
		return this.postMediaCode == null ? "" : this.postMediaCode;
	}

	/**
	 * 媒體碼<br>
	 * 未產出前:空白 產出後:"Y"
	 *
	 * @param postMediaCode 媒體碼
	 */
	public void setPostMediaCode(String postMediaCode) {
		this.postMediaCode = postMediaCode;
	}

	/**
	 * 狀況代號，授權狀態<br>
	 * CdCode:AuthErrorCode 空白:未授權 00:成功 03:已終止代繳 06:凍結警示戶 07:支票專戶 08:帳號錯誤 09:終止戶
	 * 10:身分證不符 11:轉出戶 12:拒絕往來戶 13:無此編號 14:編號已存在 16:管制帳戶 17:掛失戶 18:異常帳戶 19:編號非英數
	 * 91:期限未扣款 98:其他
	 * 
	 * @return String
	 */
	public String getAuthErrorCode() {
		return this.authErrorCode == null ? "" : this.authErrorCode;
	}

	/**
	 * 狀況代號，授權狀態<br>
	 * CdCode:AuthErrorCode 空白:未授權 00:成功 03:已終止代繳 06:凍結警示戶 07:支票專戶 08:帳號錯誤 09:終止戶
	 * 10:身分證不符 11:轉出戶 12:拒絕往來戶 13:無此編號 14:編號已存在 16:管制帳戶 17:掛失戶 18:異常帳戶 19:編號非英數
	 * 91:期限未扣款 98:其他
	 *
	 * @param authErrorCode 狀況代號，授權狀態
	 */
	public void setAuthErrorCode(String authErrorCode) {
		this.authErrorCode = authErrorCode;
	}

	/**
	 * 媒體檔流水編號<br>
	 * 媒體產出前為0
	 * 
	 * @return Integer
	 */
	public int getFileSeq() {
		return this.fileSeq;
	}

	/**
	 * 媒體檔流水編號<br>
	 * 媒體產出前為0
	 *
	 * @param fileSeq 媒體檔流水編號
	 */
	public void setFileSeq(int fileSeq) {
		this.fileSeq = fileSeq;
	}

	/**
	 * 提出日期(媒體產出日)<br>
	 * 日曆日
	 * 
	 * @return Integer
	 */
	public int getPropDate() {
		return StaticTool.bcToRoc(this.propDate);
	}

	/**
	 * 提出日期(媒體產出日)<br>
	 * 日曆日
	 *
	 * @param propDate 提出日期(媒體產出日)
	 * @throws LogicException when Date Is Warn
	 */
	public void setPropDate(int propDate) throws LogicException {
		this.propDate = StaticTool.rocToBc(propDate);
	}

	/**
	 * 提回日期<br>
	 * 日曆日
	 * 
	 * @return Integer
	 */
	public int getRetrDate() {
		return StaticTool.bcToRoc(this.retrDate);
	}

	/**
	 * 提回日期<br>
	 * 日曆日
	 *
	 * @param retrDate 提回日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setRetrDate(int retrDate) throws LogicException {
		this.retrDate = StaticTool.rocToBc(retrDate);
	}

	/**
	 * 刪除日期/暫停授權日期<br>
	 * 日曆日
	 * 
	 * @return Integer
	 */
	public int getDeleteDate() {
		return StaticTool.bcToRoc(this.deleteDate);
	}

	/**
	 * 刪除日期/暫停授權日期<br>
	 * 日曆日
	 *
	 * @param deleteDate 刪除日期/暫停授權日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setDeleteDate(int deleteDate) throws LogicException {
		this.deleteDate = StaticTool.rocToBc(deleteDate);
	}

	/**
	 * 與借款人關係<br>
	 * CdCode:RelationCode 00本人 01夫 02妻 03父 04母 05子 06女 07兄 08弟 09姊 10妹 11姪子 99其他
	 * 
	 * @return String
	 */
	public String getRelationCode() {
		return this.relationCode == null ? "" : this.relationCode;
	}

	/**
	 * 與借款人關係<br>
	 * CdCode:RelationCode 00本人 01夫 02妻 03父 04母 05子 06女 07兄 08弟 09姊 10妹 11姪子 99其他
	 *
	 * @param relationCode 與借款人關係
	 */
	public void setRelationCode(String relationCode) {
		this.relationCode = relationCode;
	}

	/**
	 * 第三人帳戶戶名<br>
	 * 
	 * @return String
	 */
	public String getRelAcctName() {
		return this.relAcctName == null ? "" : this.relAcctName;
	}

	/**
	 * 第三人帳戶戶名<br>
	 * 
	 *
	 * @param relAcctName 第三人帳戶戶名
	 */
	public void setRelAcctName(String relAcctName) {
		this.relAcctName = relAcctName;
	}

	/**
	 * 第三人身分證字號<br>
	 * 
	 * @return String
	 */
	public String getRelationId() {
		return this.relationId == null ? "" : this.relationId;
	}

	/**
	 * 第三人身分證字號<br>
	 * 
	 *
	 * @param relationId 第三人身分證字號
	 */
	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	/**
	 * 第三人出生日期<br>
	 * 
	 * @return Integer
	 */
	public int getRelAcctBirthday() {
		return StaticTool.bcToRoc(this.relAcctBirthday);
	}

	/**
	 * 第三人出生日期<br>
	 * 
	 *
	 * @param relAcctBirthday 第三人出生日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setRelAcctBirthday(int relAcctBirthday) throws LogicException {
		this.relAcctBirthday = StaticTool.rocToBc(relAcctBirthday);
	}

	/**
	 * 第三人性別<br>
	 * CdCode:Sex
	 * 
	 * @return String
	 */
	public String getRelAcctGender() {
		return this.relAcctGender == null ? "" : this.relAcctGender;
	}

	/**
	 * 第三人性別<br>
	 * CdCode:Sex
	 *
	 * @param relAcctGender 第三人性別
	 */
	public void setRelAcctGender(String relAcctGender) {
		this.relAcctGender = relAcctGender;
	}

	/**
	 * AML回應碼<br>
	 * CdCode:AmlCheckItem 0.非可疑名單/已完成名單確認 1.需審查/確認 2.為凍結名單/未確定名單
	 * 
	 * @return String
	 */
	public String getAmlRsp() {
		return this.amlRsp == null ? "" : this.amlRsp;
	}

	/**
	 * AML回應碼<br>
	 * CdCode:AmlCheckItem 0.非可疑名單/已完成名單確認 1.需審查/確認 2.為凍結名單/未確定名單
	 *
	 * @param amlRsp AML回應碼
	 */
	public void setAmlRsp(String amlRsp) {
		this.amlRsp = amlRsp;
	}

	/**
	 * 建立者櫃員編號<br>
	 * 
	 * @return String
	 */
	public String getCreateEmpNo() {
		return this.createEmpNo == null ? "" : this.createEmpNo;
	}

	/**
	 * 建立者櫃員編號<br>
	 * 
	 *
	 * @param createEmpNo 建立者櫃員編號
	 */
	public void setCreateEmpNo(String createEmpNo) {
		this.createEmpNo = createEmpNo;
	}

	/**
	 * 建檔日期<br>
	 * 
	 * @return java.sql.Timestamp
	 */
	public java.sql.Timestamp getCreateDate() {
		return this.createDate;
	}

	/**
	 * 建檔日期<br>
	 * 
	 *
	 * @param createDate 建檔日期
	 */
	public void setCreateDate(java.sql.Timestamp createDate) {
		this.createDate = createDate;
	}

	/**
	 * 修改者櫃員編號<br>
	 * 
	 * @return String
	 */
	public String getLastUpdateEmpNo() {
		return this.lastUpdateEmpNo == null ? "" : this.lastUpdateEmpNo;
	}

	/**
	 * 修改者櫃員編號<br>
	 * 
	 *
	 * @param lastUpdateEmpNo 修改者櫃員編號
	 */
	public void setLastUpdateEmpNo(String lastUpdateEmpNo) {
		this.lastUpdateEmpNo = lastUpdateEmpNo;
	}

	/**
	 * 異動日期<br>
	 * 
	 * @return java.sql.Timestamp
	 */
	public java.sql.Timestamp getLastUpdate() {
		return this.lastUpdate;
	}

	/**
	 * 異動日期<br>
	 * 
	 *
	 * @param lastUpdate 異動日期
	 */
	public void setLastUpdate(java.sql.Timestamp lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	@Override
	public String toString() {
		return "PostAuthLogHistory [logNo=" + logNo + ", custNo=" + custNo + ", facmNo=" + facmNo + ", authCode=" + authCode + ", authCreateDate=" + authCreateDate + ", authApplCode=" + authApplCode
				+ ", postDepCode=" + postDepCode + ", repayAcct=" + repayAcct + ", custId=" + custId + ", repayAcctSeq=" + repayAcctSeq + ", processDate=" + processDate + ", stampFinishDate="
				+ stampFinishDate + ", stampCancelDate=" + stampCancelDate + ", stampCode=" + stampCode + ", postMediaCode=" + postMediaCode + ", authErrorCode=" + authErrorCode + ", fileSeq="
				+ fileSeq + ", propDate=" + propDate + ", retrDate=" + retrDate + ", deleteDate=" + deleteDate + ", relationCode=" + relationCode + ", relAcctName=" + relAcctName + ", relationId="
				+ relationId + ", relAcctBirthday=" + relAcctBirthday + ", relAcctGender=" + relAcctGender + ", amlRsp=" + amlRsp + ", createEmpNo=" + createEmpNo + ", createDate=" + createDate
				+ ", lastUpdateEmpNo=" + lastUpdateEmpNo + ", lastUpdate=" + lastUpdate + "]";
	}
}
