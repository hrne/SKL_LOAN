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
 * JcicZ454Log 前置調解單獨全數受清償資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicZ454Log`")
public class JcicZ454Log implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2235989421718931041L;

	@EmbeddedId
	private JcicZ454LogId jcicZ454LogId;

	// 流水號
	@Column(name = "`Ukey`", length = 32, insertable = false, updatable = false)
	private String ukey;

	// 交易序號
	@Column(name = "`TxSeq`", length = 18, insertable = false, updatable = false)
	private String txSeq;

	// 交易代碼
	/* A:新增;C:異動;D:刪除 */
	@Column(name = "`TranKey`", length = 1)
	private String tranKey;

	// 單獨全數受清償原因
	/*
	 * CdCOde.PayOffResultA:於調解前已聲請強制執行並獲分配之款項，於日後領取分配款者B:債務人於最高限額抵押權內清償無擔保債務C:
	 * 保證人代為清償債務D:廠商將分期付款產品之款項退回貸款金融機構，並沖抵貸款金融機構債務E:
	 * 車貸及次順位不動產抵押權經債權金融機構處分後收回款項並沖抵貸款金融機構債務
	 */
	@Column(name = "`PayOffResult`", length = 1)
	private String payOffResult;

	// 單獨全數受清償日期
	@Column(name = "`PayOffDate`")
	private int payOffDate = 0;

	// 轉JCIC文字檔日期
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

	public JcicZ454LogId getJcicZ454LogId() {
		return this.jcicZ454LogId;
	}

	public void setJcicZ454LogId(JcicZ454LogId jcicZ454LogId) {
		this.jcicZ454LogId = jcicZ454LogId;
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
	 * A:新增;C:異動;D:刪除
	 * 
	 * @return String
	 */
	public String getTranKey() {
		return this.tranKey == null ? "" : this.tranKey;
	}

	/**
	 * 交易代碼<br>
	 * A:新增;C:異動;D:刪除
	 *
	 * @param tranKey 交易代碼
	 */
	public void setTranKey(String tranKey) {
		this.tranKey = tranKey;
	}

	/**
	 * 單獨全數受清償原因<br>
	 * CdCOde.PayOffResult A:於調解前已聲請強制執行並獲分配之款項，於日後領取分配款者 B:債務人於最高限額抵押權內清償無擔保債務
	 * C:保證人代為清償債務 D:廠商將分期付款產品之款項退回貸款金融機構，並沖抵貸款金融機構債務
	 * E:車貸及次順位不動產抵押權經債權金融機構處分後收回款項並沖抵貸款金融機構債務
	 * 
	 * @return String
	 */
	public String getPayOffResult() {
		return this.payOffResult == null ? "" : this.payOffResult;
	}

	/**
	 * 單獨全數受清償原因<br>
	 * CdCOde.PayOffResult A:於調解前已聲請強制執行並獲分配之款項，於日後領取分配款者 B:債務人於最高限額抵押權內清償無擔保債務
	 * C:保證人代為清償債務 D:廠商將分期付款產品之款項退回貸款金融機構，並沖抵貸款金融機構債務
	 * E:車貸及次順位不動產抵押權經債權金融機構處分後收回款項並沖抵貸款金融機構債務
	 *
	 * @param payOffResult 單獨全數受清償原因
	 */
	public void setPayOffResult(String payOffResult) {
		this.payOffResult = payOffResult;
	}

	/**
	 * 單獨全數受清償日期<br>
	 * 
	 * @return Integer
	 */
	public int getPayOffDate() {
		return StaticTool.bcToRoc(this.payOffDate);
	}

	/**
	 * 單獨全數受清償日期<br>
	 * 
	 *
	 * @param payOffDate 單獨全數受清償日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setPayOffDate(int payOffDate) throws LogicException {
		this.payOffDate = StaticTool.rocToBc(payOffDate);
	}

	/**
	 * 轉JCIC文字檔日期<br>
	 * 
	 * @return Integer
	 */
	public int getOutJcicTxtDate() {
		return StaticTool.bcToRoc(this.outJcicTxtDate);
	}

	/**
	 * 轉JCIC文字檔日期<br>
	 * 
	 *
	 * @param outJcicTxtDate 轉JCIC文字檔日期
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
		return "JcicZ454Log [jcicZ454LogId=" + jcicZ454LogId + ", tranKey=" + tranKey + ", payOffResult=" + payOffResult + ", payOffDate=" + payOffDate + ", outJcicTxtDate=" + outJcicTxtDate
				+ ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
