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
 * JcicZ061Log 回報協商剩餘債權金額資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicZ061Log`")
public class JcicZ061Log implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5174387289391387363L;

	@EmbeddedId
	private JcicZ061LogId jcicZ061LogId;

	// 流水號
	@Column(name = "`Ukey`", length = 32, insertable = false, updatable = false)
	private String ukey;

	// 交易序號
	@Column(name = "`TxSeq`", length = 18, insertable = false, updatable = false)
	private String txSeq;

	// 交易代碼
	/* A:新增;C:異動 */
	@Column(name = "`TranKey`", length = 1)
	private String tranKey;

	// 信用貸款協商剩餘債權餘額
	@Column(name = "`ExpBalanceAmt`")
	private int expBalanceAmt = 0;

	// 現金卡協商剩餘債權餘額
	@Column(name = "`CashBalanceAmt`")
	private int cashBalanceAmt = 0;

	// 信用卡協商剩餘債權餘額
	@Column(name = "`CreditBalanceAmt`")
	private int creditBalanceAmt = 0;

	// 最大債權金融機構報送註記
	/* Y;N */
	@Column(name = "`MaxMainNote`", length = 1)
	private String maxMainNote;

	// 是否有保證人
	/* Y;N */
	@Column(name = "`IsGuarantor`", length = 1)
	private String isGuarantor;

	// 是否同意債務人申請變更還款條件方案
	/* Y;N */
	@Column(name = "`IsChangePayment`", length = 1)
	private String isChangePayment;

	// 轉出JCIC文字檔日期
	@Column(name = "`OutJcicTxtDate`")
	private int outJcicTxtDate = 0;

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

	public JcicZ061LogId getJcicZ061LogId() {
		return this.jcicZ061LogId;
	}

	public void setJcicZ061LogId(JcicZ061LogId jcicZ061LogId) {
		this.jcicZ061LogId = jcicZ061LogId;
	}

	/**
	 * 流水號<br>
	 * 
	 * @return String
	 */
	public String getUkey() {
		return this.ukey == null ? "" : this.ukey;
	}

	/**
	 * 流水號<br>
	 * 
	 *
	 * @param ukey 流水號
	 */
	public void setUkey(String ukey) {
		this.ukey = ukey;
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
	 * 交易代碼<br>
	 * A:新增;C:異動
	 * 
	 * @return String
	 */
	public String getTranKey() {
		return this.tranKey == null ? "" : this.tranKey;
	}

	/**
	 * 交易代碼<br>
	 * A:新增;C:異動
	 *
	 * @param tranKey 交易代碼
	 */
	public void setTranKey(String tranKey) {
		this.tranKey = tranKey;
	}

	/**
	 * 信用貸款協商剩餘債權餘額<br>
	 * 
	 * @return Integer
	 */
	public int getExpBalanceAmt() {
		return this.expBalanceAmt;
	}

	/**
	 * 信用貸款協商剩餘債權餘額<br>
	 * 
	 *
	 * @param expBalanceAmt 信用貸款協商剩餘債權餘額
	 */
	public void setExpBalanceAmt(int expBalanceAmt) {
		this.expBalanceAmt = expBalanceAmt;
	}

	/**
	 * 現金卡協商剩餘債權餘額<br>
	 * 
	 * @return Integer
	 */
	public int getCashBalanceAmt() {
		return this.cashBalanceAmt;
	}

	/**
	 * 現金卡協商剩餘債權餘額<br>
	 * 
	 *
	 * @param cashBalanceAmt 現金卡協商剩餘債權餘額
	 */
	public void setCashBalanceAmt(int cashBalanceAmt) {
		this.cashBalanceAmt = cashBalanceAmt;
	}

	/**
	 * 信用卡協商剩餘債權餘額<br>
	 * 
	 * @return Integer
	 */
	public int getCreditBalanceAmt() {
		return this.creditBalanceAmt;
	}

	/**
	 * 信用卡協商剩餘債權餘額<br>
	 * 
	 *
	 * @param creditBalanceAmt 信用卡協商剩餘債權餘額
	 */
	public void setCreditBalanceAmt(int creditBalanceAmt) {
		this.creditBalanceAmt = creditBalanceAmt;
	}

	/**
	 * 最大債權金融機構報送註記<br>
	 * Y;N
	 * 
	 * @return String
	 */
	public String getMaxMainNote() {
		return this.maxMainNote == null ? "" : this.maxMainNote;
	}

	/**
	 * 最大債權金融機構報送註記<br>
	 * Y;N
	 *
	 * @param maxMainNote 最大債權金融機構報送註記
	 */
	public void setMaxMainNote(String maxMainNote) {
		this.maxMainNote = maxMainNote;
	}

	/**
	 * 是否有保證人<br>
	 * Y;N
	 * 
	 * @return String
	 */
	public String getIsGuarantor() {
		return this.isGuarantor == null ? "" : this.isGuarantor;
	}

	/**
	 * 是否有保證人<br>
	 * Y;N
	 *
	 * @param isGuarantor 是否有保證人
	 */
	public void setIsGuarantor(String isGuarantor) {
		this.isGuarantor = isGuarantor;
	}

	/**
	 * 是否同意債務人申請變更還款條件方案<br>
	 * Y;N
	 * 
	 * @return String
	 */
	public String getIsChangePayment() {
		return this.isChangePayment == null ? "" : this.isChangePayment;
	}

	/**
	 * 是否同意債務人申請變更還款條件方案<br>
	 * Y;N
	 *
	 * @param isChangePayment 是否同意債務人申請變更還款條件方案
	 */
	public void setIsChangePayment(String isChangePayment) {
		this.isChangePayment = isChangePayment;
	}

	/**
	 * 轉出JCIC文字檔日期<br>
	 * 
	 * @return Integer
	 */
	public int getOutJcicTxtDate() {
		return StaticTool.bcToRoc(this.outJcicTxtDate);
	}

	/**
	 * 轉出JCIC文字檔日期<br>
	 * 
	 *
	 * @param outJcicTxtDate 轉出JCIC文字檔日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setOutJcicTxtDate(int outJcicTxtDate) throws LogicException {
		this.outJcicTxtDate = StaticTool.rocToBc(outJcicTxtDate);
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
		return "JcicZ061Log [jcicZ061LogId=" + jcicZ061LogId + ", tranKey=" + tranKey + ", expBalanceAmt=" + expBalanceAmt + ", cashBalanceAmt=" + cashBalanceAmt + ", creditBalanceAmt="
				+ creditBalanceAmt + ", maxMainNote=" + maxMainNote + ", isGuarantor=" + isGuarantor + ", isChangePayment=" + isChangePayment + ", outJcicTxtDate=" + outJcicTxtDate + ", createDate="
				+ createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
