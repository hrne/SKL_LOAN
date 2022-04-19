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
 * MlaundryChkDtl 疑似洗錢樣態檢核明細檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`MlaundryChkDtl`")
public class MlaundryChkDtl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4179371798734831772L;

	@EmbeddedId
	private MlaundryChkDtlId mlaundryChkDtlId;

	// 入帳日期(統計期間迄日)
	@Column(name = "`EntryDate`", insertable = false, updatable = false)
	private int entryDate = 0;

	// 交易樣態
	@Column(name = "`Factor`", insertable = false, updatable = false)
	private int factor = 0;

	// 戶號
	@Column(name = "`CustNo`", insertable = false, updatable = false)
	private int custNo = 0;

	// 明細序號
	@Column(name = "`DtlSeq`", insertable = false, updatable = false)
	private int dtlSeq = 0;

	// 明細入帳日期
	@Column(name = "`DtlEntryDate`")
	private int dtlEntryDate = 0;

	// 來源
	@Column(name = "`RepayItem`", length = 10)
	private String repayItem;

	// 摘要代碼
	/* 共用代碼檔 BankRmftCode */
	@Column(name = "`DscptCode`", length = 4)
	private String dscptCode;

	// 交易金額
	@Column(name = "`TxAmt`")
	private BigDecimal txAmt = new BigDecimal("0");

	// 累積筆數
	@Column(name = "`TotalCnt`")
	private int totalCnt = 0;

	// 累積金額
	@Column(name = "`TotalAmt`")
	private BigDecimal totalAmt = new BigDecimal("0");

	// 統計期間起日
	@Column(name = "`StartEntryDate`")
	private int startEntryDate = 0;

	// 建檔日期時間
	/* 產製日期 */
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

	public MlaundryChkDtlId getMlaundryChkDtlId() {
		return this.mlaundryChkDtlId;
	}

	public void setMlaundryChkDtlId(MlaundryChkDtlId mlaundryChkDtlId) {
		this.mlaundryChkDtlId = mlaundryChkDtlId;
	}

	/**
	 * 入帳日期(統計期間迄日)<br>
	 * 
	 * @return Integer
	 */
	public int getEntryDate() {
		return StaticTool.bcToRoc(this.entryDate);
	}

	/**
	 * 入帳日期(統計期間迄日)<br>
	 * 
	 *
	 * @param entryDate 入帳日期(統計期間迄日)
	 * @throws LogicException when Date Is Warn
	 */
	public void setEntryDate(int entryDate) throws LogicException {
		this.entryDate = StaticTool.rocToBc(entryDate);
	}

	/**
	 * 交易樣態<br>
	 * 
	 * @return Integer
	 */
	public int getFactor() {
		return this.factor;
	}

	/**
	 * 交易樣態<br>
	 * 
	 *
	 * @param factor 交易樣態
	 */
	public void setFactor(int factor) {
		this.factor = factor;
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
	 * 明細序號<br>
	 * 
	 * @return Integer
	 */
	public int getDtlSeq() {
		return this.dtlSeq;
	}

	/**
	 * 明細序號<br>
	 * 
	 *
	 * @param dtlSeq 明細序號
	 */
	public void setDtlSeq(int dtlSeq) {
		this.dtlSeq = dtlSeq;
	}

	/**
	 * 明細入帳日期<br>
	 * 
	 * @return Integer
	 */
	public int getDtlEntryDate() {
		return StaticTool.bcToRoc(this.dtlEntryDate);
	}

	/**
	 * 明細入帳日期<br>
	 * 
	 *
	 * @param dtlEntryDate 明細入帳日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setDtlEntryDate(int dtlEntryDate) throws LogicException {
		this.dtlEntryDate = StaticTool.rocToBc(dtlEntryDate);
	}

	/**
	 * 來源<br>
	 * 
	 * @return String
	 */
	public String getRepayItem() {
		return this.repayItem == null ? "" : this.repayItem;
	}

	/**
	 * 來源<br>
	 * 
	 *
	 * @param repayItem 來源
	 */
	public void setRepayItem(String repayItem) {
		this.repayItem = repayItem;
	}

	/**
	 * 摘要代碼<br>
	 * 共用代碼檔 BankRmftCode
	 * 
	 * @return String
	 */
	public String getDscptCode() {
		return this.dscptCode == null ? "" : this.dscptCode;
	}

	/**
	 * 摘要代碼<br>
	 * 共用代碼檔 BankRmftCode
	 *
	 * @param dscptCode 摘要代碼
	 */
	public void setDscptCode(String dscptCode) {
		this.dscptCode = dscptCode;
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
	 * 累積筆數<br>
	 * 
	 * @return Integer
	 */
	public int getTotalCnt() {
		return this.totalCnt;
	}

	/**
	 * 累積筆數<br>
	 * 
	 *
	 * @param totalCnt 累積筆數
	 */
	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}

	/**
	 * 累積金額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getTotalAmt() {
		return this.totalAmt;
	}

	/**
	 * 累積金額<br>
	 * 
	 *
	 * @param totalAmt 累積金額
	 */
	public void setTotalAmt(BigDecimal totalAmt) {
		this.totalAmt = totalAmt;
	}

	/**
	 * 統計期間起日<br>
	 * 
	 * @return Integer
	 */
	public int getStartEntryDate() {
		return StaticTool.bcToRoc(this.startEntryDate);
	}

	/**
	 * 統計期間起日<br>
	 * 
	 *
	 * @param startEntryDate 統計期間起日
	 * @throws LogicException when Date Is Warn
	 */
	public void setStartEntryDate(int startEntryDate) throws LogicException {
		this.startEntryDate = StaticTool.rocToBc(startEntryDate);
	}

	/**
	 * 建檔日期時間<br>
	 * 產製日期
	 * 
	 * @return java.sql.Timestamp
	 */
	public java.sql.Timestamp getCreateDate() {
		return this.createDate;
	}

	/**
	 * 建檔日期時間<br>
	 * 產製日期
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
		return "MlaundryChkDtl [mlaundryChkDtlId=" + mlaundryChkDtlId + ", dtlEntryDate=" + dtlEntryDate + ", repayItem=" + repayItem + ", dscptCode=" + dscptCode + ", txAmt=" + txAmt + ", totalCnt="
				+ totalCnt + ", totalAmt=" + totalAmt + ", startEntryDate=" + startEntryDate + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
				+ ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
