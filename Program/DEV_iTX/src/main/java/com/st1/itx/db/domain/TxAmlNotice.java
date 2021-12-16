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
 * TxAmlNotice AML定審通知紀錄檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`TxAmlNotice`")
public class TxAmlNotice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2766731380643505519L;

	@EmbeddedId
	private TxAmlNoticeId txAmlNoticeId;

	// 定審日期
	@Column(name = "`DataDt`", insertable = false, updatable = false)
	private int dataDt = 0;

	// 身分證字號
	@Column(name = "`CustKey`", length = 10, insertable = false, updatable = false)
	private String custKey;

	// 通知序號
	@Column(name = "`ProcessSno`", insertable = false, updatable = false)
	private int processSno = 0;

	// 評級
	/* 對應"AML評級查詢規格"H:高M:中L:低 */
	@Column(name = "`ReviewType`", length = 1)
	private String reviewType;

	// 通知類別
	/* 1.高風險,郵寄"定期審查客戶基本資料更新表(房貸自然人)"2.中低風險,郵寄"放款本息攤還表暨息通知單"3.中低風險,簡訊通知 */
	@Column(name = "`ProcessType`", length = 1)
	private String processType;

	// 通知單位
	@Column(name = "`ProcessBrNo`", length = 4)
	private String processBrNo;

	// 最後通知科組別
	@Column(name = "`ProcessGroupNo`", length = 1)
	private String processGroupNo;

	// 通知經辦
	@Column(name = "`ProcessTlrNo`", length = 6)
	private String processTlrNo;

	// 通知日期
	@Column(name = "`ProcessDate`")
	private int processDate = 0;

	// 最後通知簡訊電話
	@Column(name = "`ProcessMobile`", length = 10)
	private String processMobile;

	// 最後通知郵寄地址
	@Column(name = "`ProcessAddress`", length = 100)
	private String processAddress;

	// 最後通知郵寄名稱
	@Column(name = "`ProcessName`", length = 20)
	private String processName;

	// 通知備註
	@Column(name = "`ProcessNote`", length = 100)
	private String processNote;

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

	public TxAmlNoticeId getTxAmlNoticeId() {
		return this.txAmlNoticeId;
	}

	public void setTxAmlNoticeId(TxAmlNoticeId txAmlNoticeId) {
		this.txAmlNoticeId = txAmlNoticeId;
	}

	/**
	 * 定審日期<br>
	 * 
	 * @return Integer
	 */
	public int getDataDt() {
		return this.dataDt;
	}

	/**
	 * 定審日期<br>
	 * 
	 *
	 * @param dataDt 定審日期
	 */
	public void setDataDt(int dataDt) {
		this.dataDt = dataDt;
	}

	/**
	 * 身分證字號<br>
	 * 
	 * @return String
	 */
	public String getCustKey() {
		return this.custKey == null ? "" : this.custKey;
	}

	/**
	 * 身分證字號<br>
	 * 
	 *
	 * @param custKey 身分證字號
	 */
	public void setCustKey(String custKey) {
		this.custKey = custKey;
	}

	/**
	 * 通知序號<br>
	 * 
	 * @return Integer
	 */
	public int getProcessSno() {
		return this.processSno;
	}

	/**
	 * 通知序號<br>
	 * 
	 *
	 * @param processSno 通知序號
	 */
	public void setProcessSno(int processSno) {
		this.processSno = processSno;
	}

	/**
	 * 評級<br>
	 * 對應"AML評級查詢規格" H:高 M:中 L:低
	 * 
	 * @return String
	 */
	public String getReviewType() {
		return this.reviewType == null ? "" : this.reviewType;
	}

	/**
	 * 評級<br>
	 * 對應"AML評級查詢規格" H:高 M:中 L:低
	 *
	 * @param reviewType 評級
	 */
	public void setReviewType(String reviewType) {
		this.reviewType = reviewType;
	}

	/**
	 * 通知類別<br>
	 * 1.高風險,郵寄"定期審查客戶基本資料更新表(房貸自然人)" 2.中低風險,郵寄"放款本息攤還表暨息通知單" 3.中低風險,簡訊通知
	 * 
	 * @return String
	 */
	public String getProcessType() {
		return this.processType == null ? "" : this.processType;
	}

	/**
	 * 通知類別<br>
	 * 1.高風險,郵寄"定期審查客戶基本資料更新表(房貸自然人)" 2.中低風險,郵寄"放款本息攤還表暨息通知單" 3.中低風險,簡訊通知
	 *
	 * @param processType 通知類別
	 */
	public void setProcessType(String processType) {
		this.processType = processType;
	}

	/**
	 * 通知單位<br>
	 * 
	 * @return String
	 */
	public String getProcessBrNo() {
		return this.processBrNo == null ? "" : this.processBrNo;
	}

	/**
	 * 通知單位<br>
	 * 
	 *
	 * @param processBrNo 通知單位
	 */
	public void setProcessBrNo(String processBrNo) {
		this.processBrNo = processBrNo;
	}

	/**
	 * 最後通知科組別<br>
	 * 
	 * @return String
	 */
	public String getProcessGroupNo() {
		return this.processGroupNo == null ? "" : this.processGroupNo;
	}

	/**
	 * 最後通知科組別<br>
	 * 
	 *
	 * @param processGroupNo 最後通知科組別
	 */
	public void setProcessGroupNo(String processGroupNo) {
		this.processGroupNo = processGroupNo;
	}

	/**
	 * 通知經辦<br>
	 * 
	 * @return String
	 */
	public String getProcessTlrNo() {
		return this.processTlrNo == null ? "" : this.processTlrNo;
	}

	/**
	 * 通知經辦<br>
	 * 
	 *
	 * @param processTlrNo 通知經辦
	 */
	public void setProcessTlrNo(String processTlrNo) {
		this.processTlrNo = processTlrNo;
	}

	/**
	 * 通知日期<br>
	 * 
	 * @return Integer
	 */
	public int getProcessDate() {
		return this.processDate;
	}

	/**
	 * 通知日期<br>
	 * 
	 *
	 * @param processDate 通知日期
	 */
	public void setProcessDate(int processDate) {
		this.processDate = processDate;
	}

	/**
	 * 最後通知簡訊電話<br>
	 * 
	 * @return String
	 */
	public String getProcessMobile() {
		return this.processMobile == null ? "" : this.processMobile;
	}

	/**
	 * 最後通知簡訊電話<br>
	 * 
	 *
	 * @param processMobile 最後通知簡訊電話
	 */
	public void setProcessMobile(String processMobile) {
		this.processMobile = processMobile;
	}

	/**
	 * 最後通知郵寄地址<br>
	 * 
	 * @return String
	 */
	public String getProcessAddress() {
		return this.processAddress == null ? "" : this.processAddress;
	}

	/**
	 * 最後通知郵寄地址<br>
	 * 
	 *
	 * @param processAddress 最後通知郵寄地址
	 */
	public void setProcessAddress(String processAddress) {
		this.processAddress = processAddress;
	}

	/**
	 * 最後通知郵寄名稱<br>
	 * 
	 * @return String
	 */
	public String getProcessName() {
		return this.processName == null ? "" : this.processName;
	}

	/**
	 * 最後通知郵寄名稱<br>
	 * 
	 *
	 * @param processName 最後通知郵寄名稱
	 */
	public void setProcessName(String processName) {
		this.processName = processName;
	}

	/**
	 * 通知備註<br>
	 * 
	 * @return String
	 */
	public String getProcessNote() {
		return this.processNote == null ? "" : this.processNote;
	}

	/**
	 * 通知備註<br>
	 * 
	 *
	 * @param processNote 通知備註
	 */
	public void setProcessNote(String processNote) {
		this.processNote = processNote;
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
		return "TxAmlNotice [txAmlNoticeId=" + txAmlNoticeId + ", reviewType=" + reviewType + ", processType=" + processType + ", processBrNo=" + processBrNo + ", processGroupNo=" + processGroupNo
				+ ", processTlrNo=" + processTlrNo + ", processDate=" + processDate + ", processMobile=" + processMobile + ", processAddress=" + processAddress + ", processName=" + processName
				+ ", processNote=" + processNote + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
