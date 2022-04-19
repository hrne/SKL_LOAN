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
 * CollListTmp 法催紀錄清單暫存檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CollListTmp`")
public class CollListTmp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7137008576347985579L;

	@EmbeddedId
	private CollListTmpId collListTmpId;

	// 戶號
	@Column(name = "`CustNo`", insertable = false, updatable = false)
	private int custNo = 0;

	// 額度
	@Column(name = "`FacmNo`", insertable = false, updatable = false)
	private int facmNo = 0;

	// 擔保品代號1
	@Column(name = "`ClCode1`", insertable = false, updatable = false)
	private int clCode1 = 0;

	// 擔保品代號2
	@Column(name = "`ClCode2`", insertable = false, updatable = false)
	private int clCode2 = 0;

	// 擔保品編號
	@Column(name = "`ClNo`", insertable = false, updatable = false)
	private int clNo = 0;

	// 同擔保品戶號
	@Column(name = "`ClCustNo`")
	private int clCustNo = 0;

	// 同擔保品額度
	@Column(name = "`ClFacmNo`")
	private int clFacmNo = 0;

	// 同擔保品序列號
	@Column(name = "`ClRowNo`")
	private int clRowNo = 0;

	// 案件種類
	@Column(name = "`CaseCode`", length = 1)
	private String caseCode;

	// 繳息迄日
	@Column(name = "`PrevIntDate`")
	private int prevIntDate = 0;

	// 應繳息日
	@Column(name = "`NextIntDate`")
	private int nextIntDate = 0;

	// 幣別
	@Column(name = "`CurrencyCode`", length = 3)
	private String currencyCode;

	// 本金餘額
	@Column(name = "`PrinBalance`")
	private BigDecimal prinBalance = new BigDecimal("0");

	// 呆帳餘額
	@Column(name = "`BadDebtBal`")
	private BigDecimal badDebtBal = new BigDecimal("0");

	// 戶況
	@Column(name = "`Status`")
	private int status = 0;

	// 業務科目代號
	@Column(name = "`AcctCode`", length = 3)
	private String acctCode;

	// 額度業務科目
	@Column(name = "`FacAcctCode`", length = 3)
	private String facAcctCode;

	// 展期記號
	/* 空白、1.展期一般2.展期協議 */
	@Column(name = "`RenewCode`", length = 1)
	private String renewCode;

	// 會計日期
	@Column(name = "`AcDate`")
	private int acDate = 0;

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

	public CollListTmpId getCollListTmpId() {
		return this.collListTmpId;
	}

	public void setCollListTmpId(CollListTmpId collListTmpId) {
		this.collListTmpId = collListTmpId;
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
	 * 擔保品代號1<br>
	 * 
	 * @return Integer
	 */
	public int getClCode1() {
		return this.clCode1;
	}

	/**
	 * 擔保品代號1<br>
	 * 
	 *
	 * @param clCode1 擔保品代號1
	 */
	public void setClCode1(int clCode1) {
		this.clCode1 = clCode1;
	}

	/**
	 * 擔保品代號2<br>
	 * 
	 * @return Integer
	 */
	public int getClCode2() {
		return this.clCode2;
	}

	/**
	 * 擔保品代號2<br>
	 * 
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
	 * 同擔保品戶號<br>
	 * 
	 * @return Integer
	 */
	public int getClCustNo() {
		return this.clCustNo;
	}

	/**
	 * 同擔保品戶號<br>
	 * 
	 *
	 * @param clCustNo 同擔保品戶號
	 */
	public void setClCustNo(int clCustNo) {
		this.clCustNo = clCustNo;
	}

	/**
	 * 同擔保品額度<br>
	 * 
	 * @return Integer
	 */
	public int getClFacmNo() {
		return this.clFacmNo;
	}

	/**
	 * 同擔保品額度<br>
	 * 
	 *
	 * @param clFacmNo 同擔保品額度
	 */
	public void setClFacmNo(int clFacmNo) {
		this.clFacmNo = clFacmNo;
	}

	/**
	 * 同擔保品序列號<br>
	 * 
	 * @return Integer
	 */
	public int getClRowNo() {
		return this.clRowNo;
	}

	/**
	 * 同擔保品序列號<br>
	 * 
	 *
	 * @param clRowNo 同擔保品序列號
	 */
	public void setClRowNo(int clRowNo) {
		this.clRowNo = clRowNo;
	}

	/**
	 * 案件種類<br>
	 * 
	 * @return String
	 */
	public String getCaseCode() {
		return this.caseCode == null ? "" : this.caseCode;
	}

	/**
	 * 案件種類<br>
	 * 
	 *
	 * @param caseCode 案件種類
	 */
	public void setCaseCode(String caseCode) {
		this.caseCode = caseCode;
	}

	/**
	 * 繳息迄日<br>
	 * 
	 * @return Integer
	 */
	public int getPrevIntDate() {
		return StaticTool.bcToRoc(this.prevIntDate);
	}

	/**
	 * 繳息迄日<br>
	 * 
	 *
	 * @param prevIntDate 繳息迄日
	 * @throws LogicException when Date Is Warn
	 */
	public void setPrevIntDate(int prevIntDate) throws LogicException {
		this.prevIntDate = StaticTool.rocToBc(prevIntDate);
	}

	/**
	 * 應繳息日<br>
	 * 
	 * @return Integer
	 */
	public int getNextIntDate() {
		return StaticTool.bcToRoc(this.nextIntDate);
	}

	/**
	 * 應繳息日<br>
	 * 
	 *
	 * @param nextIntDate 應繳息日
	 * @throws LogicException when Date Is Warn
	 */
	public void setNextIntDate(int nextIntDate) throws LogicException {
		this.nextIntDate = StaticTool.rocToBc(nextIntDate);
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
	 * 本金餘額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getPrinBalance() {
		return this.prinBalance;
	}

	/**
	 * 本金餘額<br>
	 * 
	 *
	 * @param prinBalance 本金餘額
	 */
	public void setPrinBalance(BigDecimal prinBalance) {
		this.prinBalance = prinBalance;
	}

	/**
	 * 呆帳餘額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getBadDebtBal() {
		return this.badDebtBal;
	}

	/**
	 * 呆帳餘額<br>
	 * 
	 *
	 * @param badDebtBal 呆帳餘額
	 */
	public void setBadDebtBal(BigDecimal badDebtBal) {
		this.badDebtBal = badDebtBal;
	}

	/**
	 * 戶況<br>
	 * 
	 * @return Integer
	 */
	public int getStatus() {
		return this.status;
	}

	/**
	 * 戶況<br>
	 * 
	 *
	 * @param status 戶況
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * 業務科目代號<br>
	 * 
	 * @return String
	 */
	public String getAcctCode() {
		return this.acctCode == null ? "" : this.acctCode;
	}

	/**
	 * 業務科目代號<br>
	 * 
	 *
	 * @param acctCode 業務科目代號
	 */
	public void setAcctCode(String acctCode) {
		this.acctCode = acctCode;
	}

	/**
	 * 額度業務科目<br>
	 * 
	 * @return String
	 */
	public String getFacAcctCode() {
		return this.facAcctCode == null ? "" : this.facAcctCode;
	}

	/**
	 * 額度業務科目<br>
	 * 
	 *
	 * @param facAcctCode 額度業務科目
	 */
	public void setFacAcctCode(String facAcctCode) {
		this.facAcctCode = facAcctCode;
	}

	/**
	 * 展期記號<br>
	 * 空白、1.展期一般2.展期協議
	 * 
	 * @return String
	 */
	public String getRenewCode() {
		return this.renewCode == null ? "" : this.renewCode;
	}

	/**
	 * 展期記號<br>
	 * 空白、1.展期一般2.展期協議
	 *
	 * @param renewCode 展期記號
	 */
	public void setRenewCode(String renewCode) {
		this.renewCode = renewCode;
	}

	/**
	 * 會計日期<br>
	 * 
	 * @return Integer
	 */
	public int getAcDate() {
		return StaticTool.bcToRoc(this.acDate);
	}

	/**
	 * 會計日期<br>
	 * 
	 *
	 * @param acDate 會計日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setAcDate(int acDate) throws LogicException {
		this.acDate = StaticTool.rocToBc(acDate);
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
		return "CollListTmp [collListTmpId=" + collListTmpId + ", clCustNo=" + clCustNo + ", clFacmNo=" + clFacmNo + ", clRowNo=" + clRowNo + ", caseCode=" + caseCode + ", prevIntDate=" + prevIntDate
				+ ", nextIntDate=" + nextIntDate + ", currencyCode=" + currencyCode + ", prinBalance=" + prinBalance + ", badDebtBal=" + badDebtBal + ", status=" + status + ", acctCode=" + acctCode
				+ ", facAcctCode=" + facAcctCode + ", renewCode=" + renewCode + ", acDate=" + acDate + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
				+ ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
