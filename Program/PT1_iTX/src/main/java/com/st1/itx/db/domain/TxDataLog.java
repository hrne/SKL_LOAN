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
 * TxDataLog 資料變更紀錄檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`TxDataLog`")
public class TxDataLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5249111202847295539L;

	@EmbeddedId
	private TxDataLogId txDataLogId;

	// 會計日期
	@Column(name = "`TxDate`", insertable = false, updatable = false)
	private int txDate = 0;

	// 交易序號
	@Column(name = "`TxSeq`", length = 18, insertable = false, updatable = false)
	private String txSeq;

	// 明細序號
	@Column(name = "`TxSno`", insertable = false, updatable = false)
	private int txSno = 0;

	// 交易編號/帳號
	@Column(name = "`MrKey`", length = 50)
	private String mrKey;

	// 交易人員
	@Column(name = "`TlrNo`", length = 6)
	private String tlrNo;

	// 交易代號
	@Column(name = "`TranNo`", length = 5)
	private String tranNo;

	// 戶號
	@Column(name = "`CustNo`")
	private int custNo = 0;

	// 額度編號
	@Column(name = "`FacmNo`")
	private int facmNo = 0;

	// 撥款序號
	@Column(name = "`BormNo`")
	private int bormNo = 0;

	// 變更理由
	@Column(name = "`Reason`", length = 120)
	private String reason;

	// 變更內容
	@Column(name = "`Content`", length = 3000)
	private String content;

	// 建檔人員
	@Column(name = "`CreateEmpNo`", length = 6)
	private String createEmpNo;

	// 建檔日期
	@CreatedDate
	@Column(name = "`CreateDate`")
	private java.sql.Timestamp createDate;

	// 最後維護人員
	@Column(name = "`LastUpdateEmpNo`", length = 6)
	private String lastUpdateEmpNo;

	// 最後維護日期
	@LastModifiedDate
	@Column(name = "`LastUpdate`")
	private java.sql.Timestamp lastUpdate;

	public TxDataLogId getTxDataLogId() {
		return this.txDataLogId;
	}

	public void setTxDataLogId(TxDataLogId txDataLogId) {
		this.txDataLogId = txDataLogId;
	}

	/**
	 * 會計日期<br>
	 * 
	 * @return Integer
	 */
	public int getTxDate() {
		return StaticTool.bcToRoc(this.txDate);
	}

	/**
	 * 會計日期<br>
	 * 
	 *
	 * @param txDate 會計日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setTxDate(int txDate) throws LogicException {
		this.txDate = StaticTool.rocToBc(txDate);
	}

	/**
	 * 交易序號<br>
	 * 
	 * @return String
	 */
	public String getTxSeq() {
		return this.txSeq == null ? "" : this.txSeq;
	}

	/**
	 * 交易序號<br>
	 * 
	 *
	 * @param txSeq 交易序號
	 */
	public void setTxSeq(String txSeq) {
		this.txSeq = txSeq;
	}

	/**
	 * 明細序號<br>
	 * 
	 * @return Integer
	 */
	public int getTxSno() {
		return this.txSno;
	}

	/**
	 * 明細序號<br>
	 * 
	 *
	 * @param txSno 明細序號
	 */
	public void setTxSno(int txSno) {
		this.txSno = txSno;
	}

	/**
	 * 交易編號/帳號<br>
	 * 
	 * @return String
	 */
	public String getMrKey() {
		return this.mrKey == null ? "" : this.mrKey;
	}

	/**
	 * 交易編號/帳號<br>
	 * 
	 *
	 * @param mrKey 交易編號/帳號
	 */
	public void setMrKey(String mrKey) {
		this.mrKey = mrKey;
	}

	/**
	 * 交易人員<br>
	 * 
	 * @return String
	 */
	public String getTlrNo() {
		return this.tlrNo == null ? "" : this.tlrNo;
	}

	/**
	 * 交易人員<br>
	 * 
	 *
	 * @param tlrNo 交易人員
	 */
	public void setTlrNo(String tlrNo) {
		this.tlrNo = tlrNo;
	}

	/**
	 * 交易代號<br>
	 * 
	 * @return String
	 */
	public String getTranNo() {
		return this.tranNo == null ? "" : this.tranNo;
	}

	/**
	 * 交易代號<br>
	 * 
	 *
	 * @param tranNo 交易代號
	 */
	public void setTranNo(String tranNo) {
		this.tranNo = tranNo;
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
	 * 撥款序號<br>
	 * 
	 * @return Integer
	 */
	public int getBormNo() {
		return this.bormNo;
	}

	/**
	 * 撥款序號<br>
	 * 
	 *
	 * @param bormNo 撥款序號
	 */
	public void setBormNo(int bormNo) {
		this.bormNo = bormNo;
	}

	/**
	 * 變更理由<br>
	 * 
	 * @return String
	 */
	public String getReason() {
		return this.reason == null ? "" : this.reason;
	}

	/**
	 * 變更理由<br>
	 * 
	 *
	 * @param reason 變更理由
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}

	/**
	 * 變更內容<br>
	 * 
	 * @return String
	 */
	public String getContent() {
		return this.content == null ? "" : this.content;
	}

	/**
	 * 變更內容<br>
	 * 
	 *
	 * @param content 變更內容
	 */
	public void setContent(String content) {
		this.content = content;
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
	 * 最後維護人員<br>
	 * 
	 * @return String
	 */
	public String getLastUpdateEmpNo() {
		return this.lastUpdateEmpNo == null ? "" : this.lastUpdateEmpNo;
	}

	/**
	 * 最後維護人員<br>
	 * 
	 *
	 * @param lastUpdateEmpNo 最後維護人員
	 */
	public void setLastUpdateEmpNo(String lastUpdateEmpNo) {
		this.lastUpdateEmpNo = lastUpdateEmpNo;
	}

	/**
	 * 最後維護日期<br>
	 * 
	 * @return java.sql.Timestamp
	 */
	public java.sql.Timestamp getLastUpdate() {
		return this.lastUpdate;
	}

	/**
	 * 最後維護日期<br>
	 * 
	 *
	 * @param lastUpdate 最後維護日期
	 */
	public void setLastUpdate(java.sql.Timestamp lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	@Override
	public String toString() {
		return "TxDataLog [txDataLogId=" + txDataLogId + ", mrKey=" + mrKey + ", tlrNo=" + tlrNo + ", tranNo=" + tranNo + ", custNo=" + custNo + ", facmNo=" + facmNo + ", bormNo=" + bormNo
				+ ", reason=" + reason + ", content=" + content + ", createEmpNo=" + createEmpNo + ", createDate=" + createDate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + ", lastUpdate=" + lastUpdate
				+ "]";
	}
}
