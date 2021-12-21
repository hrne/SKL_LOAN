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
 * BatxCheque 支票兌現檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`BatxCheque`")
public class BatxCheque implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1176884309850470739L;

	@EmbeddedId
	private BatxChequeId batxChequeId;

	// 會計日期
	@Column(name = "`AcDate`", insertable = false, updatable = false)
	private int acDate = 0;

	// 批號
	@Column(name = "`BatchNo`", length = 6, insertable = false, updatable = false)
	private String batchNo;

	// 支票帳號
	@Column(name = "`ChequeAcct`", length = 9, insertable = false, updatable = false)
	private String chequeAcct;

	// 支票號碼
	@Column(name = "`ChequeNo`", length = 7, insertable = false, updatable = false)
	private String chequeNo;

	// 支票金額
	@Column(name = "`ChequeAmt`")
	private BigDecimal chequeAmt = new BigDecimal("0");

	// 戶號
	@Column(name = "`CustNo`")
	private int custNo = 0;

	// 票據狀況碼
	/* CdCode:ChequeStatusCode0:未處理1:兌現2:退票3:抽票4:兌現未入帳5:即期票 */
	@Column(name = "`StatusCode`", length = 1)
	private String statusCode;

	// 異動日
	@Column(name = "`AdjDate`")
	private int adjDate = 0;

	// 經辦
	@Column(name = "`TitaTlrNo`", length = 6)
	private String titaTlrNo;

	// 交易序號
	/* 批號末兩碼+DetailSeq */
	@Column(name = "`TitaTxtNo`", length = 8)
	private String titaTxtNo;

	// 到期日
	@Column(name = "`ChequeDate`")
	private int chequeDate = 0;

	// 收票日
	@Column(name = "`EntryDate`")
	private int entryDate = 0;

	// 處理代碼
	/* H:成功C:抽/退票 */
	@Column(name = "`ProcessCode`", length = 1)
	private String processCode;

	// 本埠外埠
	/* CdCode:OutsideCode1.本埠2.外埠 */
	@Column(name = "`OutsideCode`", length = 1)
	private String outsideCode;

	// 入媒體
	@Column(name = "`MediaCode`", length = 1)
	private String mediaCode;

	// 行庫代號
	@Column(name = "`BankCode`", length = 7)
	private String bankCode;

	// 媒體批號
	@Column(name = "`MediaBatchNo`", length = 2)
	private String mediaBatchNo;

	// 服務中心別
	@Column(name = "`OfficeCode`", length = 1)
	private String officeCode;

	// 交換區號
	@Column(name = "`ExchangeAreaCode`", length = 2)
	private String exchangeAreaCode;

	// 發票人ID
	@Column(name = "`ChequeId`", length = 10)
	private String chequeId;

	// 發票人姓名
	@Column(name = "`ChequeName`", length = 100)
	private String chequeName;

	// AML回應碼
	/* CdCode:AmlCheckItem0.非可疑名單/已完成名單確認1.需審查/確認2.為凍結名單/未確定名單 */
	@Column(name = "`AmlRsp`", length = 1)
	private String amlRsp;

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

	public BatxChequeId getBatxChequeId() {
		return this.batxChequeId;
	}

	public void setBatxChequeId(BatxChequeId batxChequeId) {
		this.batxChequeId = batxChequeId;
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
	 * 批號<br>
	 * 
	 * @return String
	 */
	public String getBatchNo() {
		return this.batchNo == null ? "" : this.batchNo;
	}

	/**
	 * 批號<br>
	 * 
	 *
	 * @param batchNo 批號
	 */
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	/**
	 * 支票帳號<br>
	 * 
	 * @return String
	 */
	public String getChequeAcct() {
		return this.chequeAcct == null ? "" : this.chequeAcct;
	}

	/**
	 * 支票帳號<br>
	 * 
	 *
	 * @param chequeAcct 支票帳號
	 */
	public void setChequeAcct(String chequeAcct) {
		this.chequeAcct = chequeAcct;
	}

	/**
	 * 支票號碼<br>
	 * 
	 * @return String
	 */
	public String getChequeNo() {
		return this.chequeNo == null ? "" : this.chequeNo;
	}

	/**
	 * 支票號碼<br>
	 * 
	 *
	 * @param chequeNo 支票號碼
	 */
	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}

	/**
	 * 支票金額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getChequeAmt() {
		return this.chequeAmt;
	}

	/**
	 * 支票金額<br>
	 * 
	 *
	 * @param chequeAmt 支票金額
	 */
	public void setChequeAmt(BigDecimal chequeAmt) {
		this.chequeAmt = chequeAmt;
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
	 * 票據狀況碼<br>
	 * CdCode:ChequeStatusCode 0:未處理 1:兌現 2:退票 3:抽票 4:兌現未入帳 5:即期票
	 * 
	 * @return String
	 */
	public String getStatusCode() {
		return this.statusCode == null ? "" : this.statusCode;
	}

	/**
	 * 票據狀況碼<br>
	 * CdCode:ChequeStatusCode 0:未處理 1:兌現 2:退票 3:抽票 4:兌現未入帳 5:即期票
	 *
	 * @param statusCode 票據狀況碼
	 */
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * 異動日<br>
	 * 
	 * @return Integer
	 */
	public int getAdjDate() {
		return StaticTool.bcToRoc(this.adjDate);
	}

	/**
	 * 異動日<br>
	 * 
	 *
	 * @param adjDate 異動日
	 * @throws LogicException when Date Is Warn
	 */
	public void setAdjDate(int adjDate) throws LogicException {
		this.adjDate = StaticTool.rocToBc(adjDate);
	}

	/**
	 * 經辦<br>
	 * 
	 * @return String
	 */
	public String getTitaTlrNo() {
		return this.titaTlrNo == null ? "" : this.titaTlrNo;
	}

	/**
	 * 經辦<br>
	 * 
	 *
	 * @param titaTlrNo 經辦
	 */
	public void setTitaTlrNo(String titaTlrNo) {
		this.titaTlrNo = titaTlrNo;
	}

	/**
	 * 交易序號<br>
	 * 批號末兩碼+DetailSeq
	 * 
	 * @return String
	 */
	public String getTitaTxtNo() {
		return this.titaTxtNo == null ? "" : this.titaTxtNo;
	}

	/**
	 * 交易序號<br>
	 * 批號末兩碼+DetailSeq
	 *
	 * @param titaTxtNo 交易序號
	 */
	public void setTitaTxtNo(String titaTxtNo) {
		this.titaTxtNo = titaTxtNo;
	}

	/**
	 * 到期日<br>
	 * 
	 * @return Integer
	 */
	public int getChequeDate() {
		return StaticTool.bcToRoc(this.chequeDate);
	}

	/**
	 * 到期日<br>
	 * 
	 *
	 * @param chequeDate 到期日
	 * @throws LogicException when Date Is Warn
	 */
	public void setChequeDate(int chequeDate) throws LogicException {
		this.chequeDate = StaticTool.rocToBc(chequeDate);
	}

	/**
	 * 收票日<br>
	 * 
	 * @return Integer
	 */
	public int getEntryDate() {
		return StaticTool.bcToRoc(this.entryDate);
	}

	/**
	 * 收票日<br>
	 * 
	 *
	 * @param entryDate 收票日
	 * @throws LogicException when Date Is Warn
	 */
	public void setEntryDate(int entryDate) throws LogicException {
		this.entryDate = StaticTool.rocToBc(entryDate);
	}

	/**
	 * 處理代碼<br>
	 * H:成功 C:抽/退票
	 * 
	 * @return String
	 */
	public String getProcessCode() {
		return this.processCode == null ? "" : this.processCode;
	}

	/**
	 * 處理代碼<br>
	 * H:成功 C:抽/退票
	 *
	 * @param processCode 處理代碼
	 */
	public void setProcessCode(String processCode) {
		this.processCode = processCode;
	}

	/**
	 * 本埠外埠<br>
	 * CdCode:OutsideCode 1.本埠 2.外埠
	 * 
	 * @return String
	 */
	public String getOutsideCode() {
		return this.outsideCode == null ? "" : this.outsideCode;
	}

	/**
	 * 本埠外埠<br>
	 * CdCode:OutsideCode 1.本埠 2.外埠
	 *
	 * @param outsideCode 本埠外埠
	 */
	public void setOutsideCode(String outsideCode) {
		this.outsideCode = outsideCode;
	}

	/**
	 * 入媒體<br>
	 * 
	 * @return String
	 */
	public String getMediaCode() {
		return this.mediaCode == null ? "" : this.mediaCode;
	}

	/**
	 * 入媒體<br>
	 * 
	 *
	 * @param mediaCode 入媒體
	 */
	public void setMediaCode(String mediaCode) {
		this.mediaCode = mediaCode;
	}

	/**
	 * 行庫代號<br>
	 * 
	 * @return String
	 */
	public String getBankCode() {
		return this.bankCode == null ? "" : this.bankCode;
	}

	/**
	 * 行庫代號<br>
	 * 
	 *
	 * @param bankCode 行庫代號
	 */
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	/**
	 * 媒體批號<br>
	 * 
	 * @return String
	 */
	public String getMediaBatchNo() {
		return this.mediaBatchNo == null ? "" : this.mediaBatchNo;
	}

	/**
	 * 媒體批號<br>
	 * 
	 *
	 * @param mediaBatchNo 媒體批號
	 */
	public void setMediaBatchNo(String mediaBatchNo) {
		this.mediaBatchNo = mediaBatchNo;
	}

	/**
	 * 服務中心別<br>
	 * 
	 * @return String
	 */
	public String getOfficeCode() {
		return this.officeCode == null ? "" : this.officeCode;
	}

	/**
	 * 服務中心別<br>
	 * 
	 *
	 * @param officeCode 服務中心別
	 */
	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}

	/**
	 * 交換區號<br>
	 * 
	 * @return String
	 */
	public String getExchangeAreaCode() {
		return this.exchangeAreaCode == null ? "" : this.exchangeAreaCode;
	}

	/**
	 * 交換區號<br>
	 * 
	 *
	 * @param exchangeAreaCode 交換區號
	 */
	public void setExchangeAreaCode(String exchangeAreaCode) {
		this.exchangeAreaCode = exchangeAreaCode;
	}

	/**
	 * 發票人ID<br>
	 * 
	 * @return String
	 */
	public String getChequeId() {
		return this.chequeId == null ? "" : this.chequeId;
	}

	/**
	 * 發票人ID<br>
	 * 
	 *
	 * @param chequeId 發票人ID
	 */
	public void setChequeId(String chequeId) {
		this.chequeId = chequeId;
	}

	/**
	 * 發票人姓名<br>
	 * 
	 * @return String
	 */
	public String getChequeName() {
		return this.chequeName == null ? "" : this.chequeName;
	}

	/**
	 * 發票人姓名<br>
	 * 
	 *
	 * @param chequeName 發票人姓名
	 */
	public void setChequeName(String chequeName) {
		this.chequeName = chequeName;
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
		return "BatxCheque [batxChequeId=" + batxChequeId + ", chequeAmt=" + chequeAmt + ", custNo=" + custNo + ", statusCode=" + statusCode + ", adjDate=" + adjDate + ", titaTlrNo=" + titaTlrNo
				+ ", titaTxtNo=" + titaTxtNo + ", chequeDate=" + chequeDate + ", entryDate=" + entryDate + ", processCode=" + processCode + ", outsideCode=" + outsideCode + ", mediaCode=" + mediaCode
				+ ", bankCode=" + bankCode + ", mediaBatchNo=" + mediaBatchNo + ", officeCode=" + officeCode + ", exchangeAreaCode=" + exchangeAreaCode + ", chequeId=" + chequeId + ", chequeName="
				+ chequeName + ", amlRsp=" + amlRsp + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
