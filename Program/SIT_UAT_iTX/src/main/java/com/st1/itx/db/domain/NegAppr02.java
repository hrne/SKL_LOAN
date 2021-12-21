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
 * NegAppr02 一般債權撥付資料檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`NegAppr02`")
public class NegAppr02 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -439792206497594600L;

	@EmbeddedId
	private NegAppr02Id negAppr02Id;

	// 提兌日
	@Column(name = "`BringUpDate`", insertable = false, updatable = false)
	private int bringUpDate = 0;

	// 債權機構代號
	@Column(name = "`FinCode`", length = 8, insertable = false, updatable = false)
	private String finCode;

	// 資料檔交易序號
	@Column(name = "`TxSeq`", length = 10, insertable = false, updatable = false)
	private String txSeq;

	// 發件單位
	@Column(name = "`SendUnit`", length = 8)
	private String sendUnit;

	// 收件單位
	@Column(name = "`RecvUnit`", length = 8)
	private String recvUnit;

	// 指定入/扣帳日
	@Column(name = "`EntryDate`")
	private int entryDate = 0;

	// 轉帳類別
	@Column(name = "`TransCode`", length = 5)
	private String transCode;

	// 交易金額
	@Column(name = "`TxAmt`")
	private BigDecimal txAmt = new BigDecimal("0");

	// 委託單位
	@Column(name = "`Consign`", length = 8)
	private String consign;

	// 金融機構
	@Column(name = "`FinIns`", length = 7)
	private String finIns;

	// 轉帳帳號
	@Column(name = "`RemitAcct`", length = 16)
	private String remitAcct;

	// 帳戶ID
	@Column(name = "`CustId`", length = 10)
	private String custId;

	// 戶號
	/* 要在BACHTX03寫入 */
	@Column(name = "`CustNo`")
	private int custNo = 0;

	// 還款狀況
	/*
	 * 4001:入/扣帳成功 4505:存款不足 4508:非委託或已終止帳戶 4806:存戶查核資料錯誤 4808:無此帳戶或問題帳戶
	 * 4405:未開卡或額度不足 4705:剃除不轉帳 2999:其他錯誤
	 */
	@Column(name = "`StatusCode`", length = 4)
	private String statusCode;

	// 會計日期
	/* 做整批入帳連動L3210時寫入 */
	@Column(name = "`AcDate`")
	private int acDate = 0;

	// 銷帳編號
	/* 0:正常1:溢繳2:短繳3:大額還本4:結清 */
	@Column(name = "`TxKind`", length = 1)
	private String txKind;

	// 交易狀態
	/* 0:未入專戶1:已入客戶暫收2:已入帳 */
	@Column(name = "`TxStatus`")
	private int txStatus = 0;

	// 交易檔會計日
	/* 暫收解入時寫入 */
	@Column(name = "`NegTransAcDate`")
	private int negTransAcDate = 0;

	// 交易檔經辦
	/* 暫收解入時寫入 */
	@Column(name = "`NegTransTlrNo`", length = 6)
	private String negTransTlrNo;

	// 交易檔序號
	/* 暫收解入時寫入 */
	@Column(name = "`NegTransTxtNo`")
	private int negTransTxtNo = 0;

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

	public NegAppr02Id getNegAppr02Id() {
		return this.negAppr02Id;
	}

	public void setNegAppr02Id(NegAppr02Id negAppr02Id) {
		this.negAppr02Id = negAppr02Id;
	}

	/**
	 * 提兌日<br>
	 * 
	 * @return Integer
	 */
	public int getBringUpDate() {
		return StaticTool.bcToRoc(this.bringUpDate);
	}

	/**
	 * 提兌日<br>
	 * 
	 *
	 * @param bringUpDate 提兌日
	 * @throws LogicException when Date Is Warn
	 */
	public void setBringUpDate(int bringUpDate) throws LogicException {
		this.bringUpDate = StaticTool.rocToBc(bringUpDate);
	}

	/**
	 * 債權機構代號<br>
	 * 
	 * @return String
	 */
	public String getFinCode() {
		return this.finCode == null ? "" : this.finCode;
	}

	/**
	 * 債權機構代號<br>
	 * 
	 *
	 * @param finCode 債權機構代號
	 */
	public void setFinCode(String finCode) {
		this.finCode = finCode;
	}

	/**
	 * 資料檔交易序號<br>
	 * 
	 * @return String
	 */
	public String getTxSeq() {
		return this.txSeq == null ? "" : this.txSeq;
	}

	/**
	 * 資料檔交易序號<br>
	 * 
	 *
	 * @param txSeq 資料檔交易序號
	 */
	public void setTxSeq(String txSeq) {
		this.txSeq = txSeq;
	}

	/**
	 * 發件單位<br>
	 * 
	 * @return String
	 */
	public String getSendUnit() {
		return this.sendUnit == null ? "" : this.sendUnit;
	}

	/**
	 * 發件單位<br>
	 * 
	 *
	 * @param sendUnit 發件單位
	 */
	public void setSendUnit(String sendUnit) {
		this.sendUnit = sendUnit;
	}

	/**
	 * 收件單位<br>
	 * 
	 * @return String
	 */
	public String getRecvUnit() {
		return this.recvUnit == null ? "" : this.recvUnit;
	}

	/**
	 * 收件單位<br>
	 * 
	 *
	 * @param recvUnit 收件單位
	 */
	public void setRecvUnit(String recvUnit) {
		this.recvUnit = recvUnit;
	}

	/**
	 * 指定入/扣帳日<br>
	 * 
	 * @return Integer
	 */
	public int getEntryDate() {
		return StaticTool.bcToRoc(this.entryDate);
	}

	/**
	 * 指定入/扣帳日<br>
	 * 
	 *
	 * @param entryDate 指定入/扣帳日
	 * @throws LogicException when Date Is Warn
	 */
	public void setEntryDate(int entryDate) throws LogicException {
		this.entryDate = StaticTool.rocToBc(entryDate);
	}

	/**
	 * 轉帳類別<br>
	 * 
	 * @return String
	 */
	public String getTransCode() {
		return this.transCode == null ? "" : this.transCode;
	}

	/**
	 * 轉帳類別<br>
	 * 
	 *
	 * @param transCode 轉帳類別
	 */
	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}

	/**
	 * 交易金額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getTxAmt() {
		return this.txAmt;
	}

	/**
	 * 交易金額<br>
	 * 
	 *
	 * @param txAmt 交易金額
	 */
	public void setTxAmt(BigDecimal txAmt) {
		this.txAmt = txAmt;
	}

	/**
	 * 委託單位<br>
	 * 
	 * @return String
	 */
	public String getConsign() {
		return this.consign == null ? "" : this.consign;
	}

	/**
	 * 委託單位<br>
	 * 
	 *
	 * @param consign 委託單位
	 */
	public void setConsign(String consign) {
		this.consign = consign;
	}

	/**
	 * 金融機構<br>
	 * 
	 * @return String
	 */
	public String getFinIns() {
		return this.finIns == null ? "" : this.finIns;
	}

	/**
	 * 金融機構<br>
	 * 
	 *
	 * @param finIns 金融機構
	 */
	public void setFinIns(String finIns) {
		this.finIns = finIns;
	}

	/**
	 * 轉帳帳號<br>
	 * 
	 * @return String
	 */
	public String getRemitAcct() {
		return this.remitAcct == null ? "" : this.remitAcct;
	}

	/**
	 * 轉帳帳號<br>
	 * 
	 *
	 * @param remitAcct 轉帳帳號
	 */
	public void setRemitAcct(String remitAcct) {
		this.remitAcct = remitAcct;
	}

	/**
	 * 帳戶ID<br>
	 * 
	 * @return String
	 */
	public String getCustId() {
		return this.custId == null ? "" : this.custId;
	}

	/**
	 * 帳戶ID<br>
	 * 
	 *
	 * @param custId 帳戶ID
	 */
	public void setCustId(String custId) {
		this.custId = custId;
	}

	/**
	 * 戶號<br>
	 * 要在BACHTX03寫入
	 * 
	 * @return Integer
	 */
	public int getCustNo() {
		return this.custNo;
	}

	/**
	 * 戶號<br>
	 * 要在BACHTX03寫入
	 *
	 * @param custNo 戶號
	 */
	public void setCustNo(int custNo) {
		this.custNo = custNo;
	}

	/**
	 * 還款狀況<br>
	 * 4001:入/扣帳成功 4505:存款不足 4508:非委託或已終止帳戶 4806:存戶查核資料錯誤 4808:無此帳戶或問題帳戶
	 * 4405:未開卡或額度不足 4705:剃除不轉帳 2999:其他錯誤
	 * 
	 * @return String
	 */
	public String getStatusCode() {
		return this.statusCode == null ? "" : this.statusCode;
	}

	/**
	 * 還款狀況<br>
	 * 4001:入/扣帳成功 4505:存款不足 4508:非委託或已終止帳戶 4806:存戶查核資料錯誤 4808:無此帳戶或問題帳戶
	 * 4405:未開卡或額度不足 4705:剃除不轉帳 2999:其他錯誤
	 *
	 * @param statusCode 還款狀況
	 */
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * 會計日期<br>
	 * 做整批入帳連動L3210時寫入
	 * 
	 * @return Integer
	 */
	public int getAcDate() {
		return StaticTool.bcToRoc(this.acDate);
	}

	/**
	 * 會計日期<br>
	 * 做整批入帳連動L3210時寫入
	 *
	 * @param acDate 會計日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setAcDate(int acDate) throws LogicException {
		this.acDate = StaticTool.rocToBc(acDate);
	}

	/**
	 * 銷帳編號<br>
	 * 0:正常 1:溢繳 2:短繳 3:大額還本 4:結清
	 * 
	 * @return String
	 */
	public String getTxKind() {
		return this.txKind == null ? "" : this.txKind;
	}

	/**
	 * 銷帳編號<br>
	 * 0:正常 1:溢繳 2:短繳 3:大額還本 4:結清
	 *
	 * @param txKind 銷帳編號
	 */
	public void setTxKind(String txKind) {
		this.txKind = txKind;
	}

	/**
	 * 交易狀態<br>
	 * 0:未入專戶 1:已入客戶暫收 2:已入帳
	 * 
	 * @return Integer
	 */
	public int getTxStatus() {
		return this.txStatus;
	}

	/**
	 * 交易狀態<br>
	 * 0:未入專戶 1:已入客戶暫收 2:已入帳
	 *
	 * @param txStatus 交易狀態
	 */
	public void setTxStatus(int txStatus) {
		this.txStatus = txStatus;
	}

	/**
	 * 交易檔會計日<br>
	 * 暫收解入時寫入
	 * 
	 * @return Integer
	 */
	public int getNegTransAcDate() {
		return StaticTool.bcToRoc(this.negTransAcDate);
	}

	/**
	 * 交易檔會計日<br>
	 * 暫收解入時寫入
	 *
	 * @param negTransAcDate 交易檔會計日
	 * @throws LogicException when Date Is Warn
	 */
	public void setNegTransAcDate(int negTransAcDate) throws LogicException {
		this.negTransAcDate = StaticTool.rocToBc(negTransAcDate);
	}

	/**
	 * 交易檔經辦<br>
	 * 暫收解入時寫入
	 * 
	 * @return String
	 */
	public String getNegTransTlrNo() {
		return this.negTransTlrNo == null ? "" : this.negTransTlrNo;
	}

	/**
	 * 交易檔經辦<br>
	 * 暫收解入時寫入
	 *
	 * @param negTransTlrNo 交易檔經辦
	 */
	public void setNegTransTlrNo(String negTransTlrNo) {
		this.negTransTlrNo = negTransTlrNo;
	}

	/**
	 * 交易檔序號<br>
	 * 暫收解入時寫入
	 * 
	 * @return Integer
	 */
	public int getNegTransTxtNo() {
		return this.negTransTxtNo;
	}

	/**
	 * 交易檔序號<br>
	 * 暫收解入時寫入
	 *
	 * @param negTransTxtNo 交易檔序號
	 */
	public void setNegTransTxtNo(int negTransTxtNo) {
		this.negTransTxtNo = negTransTxtNo;
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
		return "NegAppr02 [negAppr02Id=" + negAppr02Id + ", sendUnit=" + sendUnit + ", recvUnit=" + recvUnit + ", entryDate=" + entryDate + ", transCode=" + transCode + ", txAmt=" + txAmt
				+ ", consign=" + consign + ", finIns=" + finIns + ", remitAcct=" + remitAcct + ", custId=" + custId + ", custNo=" + custNo + ", statusCode=" + statusCode + ", acDate=" + acDate
				+ ", txKind=" + txKind + ", txStatus=" + txStatus + ", negTransAcDate=" + negTransAcDate + ", negTransTlrNo=" + negTransTlrNo + ", negTransTxtNo=" + negTransTxtNo + ", createDate="
				+ createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
