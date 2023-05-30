package com.st1.itx.util.common.data;

import java.util.LinkedHashMap;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("checkAmlVo")
@Scope("prototype")
public class CheckAmlVo extends LinkedHashMap<String, String> {

	/**
	 * ****************************************************** 記錄參數 (M.必須有值 /
	 * O.不必須有值)
	 */

	/**
	 * 
	 */
	private static final long serialVersionUID = -389843749725436452L;

	/**
	 * M 帳務日
	 */
	private int Entdy = 0;

	/**
	 * M 單位
	 */
	private String BrNo = "";

	/**
	 * M 相關編號
	 */
	private String RefNo = "";

	/**
	 * ****************************************************** 檢查參數 (M.必須有值 /
	 * O.不必須有值)
	 */

	/**
	 * M 來源系統ID,預設值 Loan
	 */
	private String SrcSystemID = "LOAN";

	/**
	 * M 交易名稱
	 */
	private String TxnID = "ESB0076B";

	/**
	 * M(6) 查詢單位 : 疑似名單審核單位
	 */
	private String Unit = "10HC00";

	/**
	 * O(6) 代辦單位 : 若為網路服務案件，為必填
	 */
	private String AcceptanceUnit = "";

	/**
	 * M(2) 保單角色 :
	 */
	private String RoleId = "";

	/**
	 * M(100) AML 交易序號 : 線別(2)+各線別的唯一Key
	 */
	private String TransactionId = "";

	/**
	 * M(30) 保單號碼/放款案號 : 保單號碼/放款案號
	 */
	private String AcctNo = "";

	/**
	 * M(40) 案號 : 可識別為同一批交易的號碼：案號、ProcessID、SubmitID
	 */
	private String CaseNo = "";

	/**
	 * O(30) 保險證號 : 團保使用
	 */
	private String AcctId = "";

	/**
	 * O(int) 投保次數 : 團保使用
	 */
	private int InsurCount = 0;

	/**
	 * M(100) 姓名 : 全形/半型都可以(以中文為主，也可以是全型英文)先找全名檔
	 */
	private String Name = "";

	/**
	 * O(100) 英文姓名 : 全形/半型都可以
	 */
	private String EnglishName = "";

	/**
	 * O(24) 身份證ID/居留證號碼
	 */
	private String CustKey = "";

	/**
	 * M(1) 身份別 : 1:自然人 2:法人
	 */
	private String IdentityCd = "";

	/**
	 * O(50) 國籍/註冊地(登記地)國籍 : 各系統中有資訊時需提供風險因子項目-國家及地區-國家及地區名稱
	 */
	private String NationalCd = "";

	/**
	 * O(50) 營業地國籍/居住地國籍 : 各系統中有資訊時需提供風險因子項目-國家及地區-國家及地區名稱
	 */
	private String BirthNationCd = "";

	/**
	 * M(1) 性別 : M/F;若是法人填空格(space)
	 */
	private String Sex = "";

	/**
	 * O(8) 個人出生日 : YYYYMMDD，民國(0105);若是法人填空白
	 */
	private String BirthEstDt = "";

	/**
	 * O(300) EMAIL : 此案件發送MAIL的對象，可多組，以分號做區隔，如:test1@test.com;test2@test.com
	 */
	private String NotifyEmail;

	/**
	 * M(10) 查詢者ID : 送出查詢的使用者代號(各系統中的代號 titaVo.getTlrNo())
	 */
	private String QueryId = "";

	/**
	 * M(10) 查詢來源 : 系統代號
	 */
	private String SourceId = "Loan";

	/**
	 * O(14) 異動時間 : YYYYMMDDHHmmss，民國(0105) , 由系統給值
	 */
	private String ModifyDate;

	/**
	 * M(1) 新舊角色 : 新：N 舊：O / 契變線使用，其餘線別請輸入N
	 */
	private String RoleStatus = "N";

	/**
	 * O(1) 要被保人同一人 : 新契約線使用，其餘線別請輸入N
	 */
	private String InsrNHdrSame = "N";

	/**
	 * ****************************************************** 回覆參數
	 */

	/**
	 * TxAmlLog.LogNo
	 */
	private Long LogNo;

	/**
	 * 回覆狀態
	 */
	private String Status;

	/**
	 * 回覆狀態代碼
	 */
	private String StatusCode;

	/**
	 * 回覆狀態說明
	 */
	private String StatusDesc;

	/**
	 * 疑似黑名單分類
	 */
	private String IsSan;

	/**
	 * 是否有相似名單
	 */
	private String IsSimilar;

	/**
	 * 是否為禁制國家
	 */
	private String IsBanNation;

	/**
	 * 檢核狀態
	 */
	private String ConfirmStatus = "";

	/**
	 * 人工確認狀態
	 */
	private String ConfirmCode = "";

	/**
	 * 人工確認人員
	 */
	private String ConfirmEmpNo = "";

	/**
	 * 後續處理
	 */
	private String ConfirmTranCode = "";

	/**
	 * ****************************************************** get / set
	 */

	/**
	 * @return the srcSystemID
	 */
	public String getSrcSystemID() {
		return SrcSystemID;
	}

	/**
	 * @return the txnID
	 */
	public String getTxnID() {
		return TxnID;
	}

	/**
	 * @return the unit
	 */
	public String getUnit() {
		return Unit;
	}

	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		Unit = unit;
	}

	/**
	 * @return the acceptanceUnit
	 */
	public String getAcceptanceUnit() {
		return AcceptanceUnit;
	}

	/**
	 * @param acceptanceUnit the acceptanceUnit to set
	 */
	public void setAcceptanceUnit(String acceptanceUnit) {
		AcceptanceUnit = acceptanceUnit;
	}

	/**
	 * @return the roleId
	 */
	public String getRoleId() {
		return RoleId;
	}

	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId(String roleId) {
		RoleId = roleId;
	}

	/**
	 * @return the transactionId
	 */
	public String getTransactionId() {
		return TransactionId;
	}

	/**
	 * @param transactionId the transactionId to set
	 */
	public void setTransactionId(String transactionId) {
		TransactionId = transactionId;
	}

	/**
	 * @return the acctNo
	 */
	public String getAcctNo() {
		return AcctNo;
	}

	/**
	 * @param acctNo the acctNo to set
	 */
	public void setAcctNo(String acctNo) {
		AcctNo = acctNo;
	}

	/**
	 * @return the caseNo
	 */
	public String getCaseNo() {
		return CaseNo;
	}

	/**
	 * @param caseNo the caseNo to set
	 */
	public void setCaseNo(String caseNo) {
		CaseNo = caseNo;
	}

	/**
	 * @return the acctId
	 */
	public String getAcctId() {
		return AcctId;
	}

	/**
	 * @param acctId the acctId to set
	 */
	public void setAcctId(String acctId) {
		AcctId = acctId;
	}

	/**
	 * @return the insurCount
	 */
	public int getInsurCount() {
		return InsurCount;
	}

	/**
	 * @param insurCount the insurCount to set
	 */
	public void setInsurCount(int insurCount) {
		InsurCount = insurCount;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return Name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		Name = name;
	}

	/**
	 * @return the englishName
	 */
	public String getEnglishName() {
		return EnglishName;
	}

	/**
	 * @param englishName the englishName to set
	 */
	public void setEnglishName(String englishName) {
		EnglishName = englishName;
	}

	/**
	 * @return the custKey
	 */
	public String getCustKey() {
		return CustKey;
	}

	/**
	 * @param custKey the custKey to set
	 */
	public void setCustKey(String custKey) {
		CustKey = custKey;
	}

	/**
	 * @return the identityCd
	 */
	public String getIdentityCd() {
		return IdentityCd;
	}

	/**
	 * @param identityCd the identityCd to set
	 */
	public void setIdentityCd(String identityCd) {
		IdentityCd = identityCd;
	}

	/**
	 * @return the nationalCd
	 */
	public String getNationalCd() {
		return NationalCd;
	}

	/**
	 * @param nationalCd the nationalCd to set
	 */
	public void setNationalCd(String nationalCd) {
		NationalCd = nationalCd;
	}

	/**
	 * @return the birthNationCd
	 */
	public String getBirthNationCd() {
		return BirthNationCd;
	}

	/**
	 * @param birthNationCd the birthNationCd to set
	 */
	public void setBirthNationCd(String birthNationCd) {
		BirthNationCd = birthNationCd;
	}

	/**
	 * @return the sex
	 */
	public String getSex() {
		return Sex;
	}

	/**
	 * @param sex the sex to set
	 */
	public void setSex(String sex) {
		Sex = sex;
	}

	/**
	 * @return the birthEstDt
	 */
	public String getBirthEstDt() {
		return BirthEstDt;
	}

	/**
	 * @param birthEstDt the birthEstDt to set
	 */
	public void setBirthEstDt(String birthEstDt) {
		BirthEstDt = birthEstDt;
	}

	/**
	 * @return the notifyEmail
	 */
	public String getNotifyEmail() {
		return NotifyEmail;
	}

	/**
	 * @param notifyEmail the notifyEmail to set
	 */
	public void setNotifyEmail(String notifyEmail) {
		NotifyEmail = notifyEmail;
	}

	/**
	 * @return the queryId
	 */
	public String getQueryId() {
		return QueryId;
	}

	/**
	 * @param queryId the queryId to set
	 */
	public void setQueryId(String queryId) {
		QueryId = queryId;
	}

	/**
	 * @return the sourceId
	 */
	public String getSourceId() {
		return SourceId;
	}

	/**
	 * @param sourceId the sourceId to set
	 */
	public void setSourceId(String sourceId) {
		SourceId = sourceId;
	}

	/**
	 * @return the modifyDate
	 */
	public String getModifyDate() {
		return ModifyDate;
	}

	/**
	 * @param modifyDate the modifyDate to set
	 */
	public void setModifyDate(String modifyDate) {
		ModifyDate = modifyDate;
	}

	/**
	 * @return the logNo
	 */
	public Long getLogNo() {
		return LogNo;
	}

	/**
	 * @param logNo the logNo to set
	 */
	public void setLogNo(Long logNo) {
		LogNo = logNo;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return Status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		Status = status;
	}

	/**
	 * @return the statusCode
	 */
	public String getStatusCode() {
		return StatusCode;
	}

	/**
	 * @param statusCode the statusCode to set
	 */
	public void setStatusCode(String statusCode) {
		StatusCode = statusCode;
	}

	/**
	 * @return the statusDesc
	 */
	public String getStatusDesc() {
		return StatusDesc;
	}

	/**
	 * @param statusDesc the statusDesc to set
	 */
	public void setStatusDesc(String statusDesc) {
		StatusDesc = statusDesc;
	}

	/**
	 * @return the isSan
	 */
	public String getIsSan() {
		return IsSan;
	}

	/**
	 * @param isSan the isSan to set
	 */
	public void setIsSan(String isSan) {
		IsSan = isSan;
	}

	/**
	 * @return the isSimilar
	 */
	public String getIsSimilar() {
		return IsSimilar;
	}

	/**
	 * @param isSimilar the isSimilar to set
	 */
	public void setIsSimilar(String isSimilar) {
		IsSimilar = isSimilar;
	}

	/**
	 * @return the isBanNation
	 */
	public String getIsBanNation() {
		return IsBanNation;
	}

	/**
	 * @param isBanNation the isBanNation to set
	 */
	public void setIsBanNation(String isBanNation) {
		IsBanNation = isBanNation;
	}

	/**
	 * @return the entdy
	 */
	public int getEntdy() {
		return Entdy;
	}

	/**
	 * @param entdy the entdy to set
	 */
	public void setEntdy(int entdy) {
		Entdy = entdy;
	}

	/**
	 * @return the brNo
	 */
	public String getBrNo() {
		return BrNo;
	}

	/**
	 * @param brNo the brNo to set
	 */
	public void setBrNo(String brNo) {
		BrNo = brNo;
	}

	/**
	 * @return the refNo
	 */
	public String getRefNo() {
		return RefNo;
	}

	/**
	 * @param refNo the refNo to set
	 */
	public void setRefNo(String refNo) {
		RefNo = refNo;
	}

	/**
	 * @return the roleStatus
	 */
	public String getRoleStatus() {
		return RoleStatus;
	}

	/**
	 * @return the insrNHdrSame
	 */
	public String getInsrNHdrSame() {
		return InsrNHdrSame;
	}

	/**
	 * @return the confirmStatus
	 */
	public String getConfirmStatus() {
		return ConfirmStatus;
	}

	/**
	 * @param confirmStatus the confirmStatus to set
	 */
	public void setConfirmStatus(String confirmStatus) {
		ConfirmStatus = confirmStatus;
	}

	/**
	 * @return the confirmCode
	 */
	public String getConfirmCode() {
		return ConfirmCode;
	}

	/**
	 * @param confirmCode the confirmCode to set
	 */
	public void setConfirmCode(String confirmCode) {
		ConfirmCode = confirmCode;
	}

	/**
	 * @return the confirmEmpNo
	 */
	public String getConfirmEmpNo() {
		return ConfirmEmpNo;
	}

	/**
	 * @param confirmEmpNo the confirmEmpNo to set
	 */
	public void setConfirmEmpNo(String confirmEmpNo) {
		ConfirmEmpNo = confirmEmpNo;
	}

	/**
	 * @return the confirmTranCode
	 */
	public String getConfirmTranCode() {
		return ConfirmTranCode;
	}

	/**
	 * @param confirmTranCode the confirmTranCode to set
	 */
	public void setConfirmTranCode(String confirmTranCode) {
		ConfirmTranCode = confirmTranCode;
	}

}
