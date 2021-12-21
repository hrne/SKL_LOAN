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
 * AcCheque 應收票據資料檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`AcCheque`")
public class AcCheque implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7230892033602856379L;

	@EmbeddedId
	private AcChequeId acChequeId;

	// 資料年月
	@Column(name = "`DataDate`", insertable = false, updatable = false)
	private int dataDate = 0;

	// 入帳單位
	@Column(name = "`UnitCode`", length = 6, insertable = false, updatable = false)
	private String unitCode;

	// 支票流水號
	@Column(name = "`ChequeSeq`", insertable = false, updatable = false)
	private int chequeSeq = 0;

	// 銀行代號
	@Column(name = "`BankNo`")
	private int bankNo = 0;

	// 支票帳號
	@Column(name = "`ChequeAccount`")
	private int chequeAccount = 0;

	// 金額
	@Column(name = "`Amt`")
	private BigDecimal amt = new BigDecimal("0");

	// 支票日期
	@Column(name = "`ChequeDate`")
	private int chequeDate = 0;

	// 建檔日
	@Column(name = "`KeyInDate`")
	private int keyInDate = 0;

	// 預計交換日
	@Column(name = "`ExpectedExchangeDate`")
	private int expectedExchangeDate = 0;

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

	public AcChequeId getAcChequeId() {
		return this.acChequeId;
	}

	public void setAcChequeId(AcChequeId acChequeId) {
		this.acChequeId = acChequeId;
	}

	/**
	 * 資料年月<br>
	 * 
	 * @return Integer
	 */
	public int getDataDate() {
		return StaticTool.bcToRoc(this.dataDate);
	}

	/**
	 * 資料年月<br>
	 * 
	 *
	 * @param dataDate 資料年月
	 * @throws LogicException when Date Is Warn
	 */
	public void setDataDate(int dataDate) throws LogicException {
		this.dataDate = StaticTool.rocToBc(dataDate);
	}

	/**
	 * 入帳單位<br>
	 * 
	 * @return String
	 */
	public String getUnitCode() {
		return this.unitCode == null ? "" : this.unitCode;
	}

	/**
	 * 入帳單位<br>
	 * 
	 *
	 * @param unitCode 入帳單位
	 */
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	/**
	 * 支票流水號<br>
	 * 
	 * @return Integer
	 */
	public int getChequeSeq() {
		return this.chequeSeq;
	}

	/**
	 * 支票流水號<br>
	 * 
	 *
	 * @param chequeSeq 支票流水號
	 */
	public void setChequeSeq(int chequeSeq) {
		this.chequeSeq = chequeSeq;
	}

	/**
	 * 銀行代號<br>
	 * 
	 * @return Integer
	 */
	public int getBankNo() {
		return this.bankNo;
	}

	/**
	 * 銀行代號<br>
	 * 
	 *
	 * @param bankNo 銀行代號
	 */
	public void setBankNo(int bankNo) {
		this.bankNo = bankNo;
	}

	/**
	 * 支票帳號<br>
	 * 
	 * @return Integer
	 */
	public int getChequeAccount() {
		return this.chequeAccount;
	}

	/**
	 * 支票帳號<br>
	 * 
	 *
	 * @param chequeAccount 支票帳號
	 */
	public void setChequeAccount(int chequeAccount) {
		this.chequeAccount = chequeAccount;
	}

	/**
	 * 金額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getAmt() {
		return this.amt;
	}

	/**
	 * 金額<br>
	 * 
	 *
	 * @param amt 金額
	 */
	public void setAmt(BigDecimal amt) {
		this.amt = amt;
	}

	/**
	 * 支票日期<br>
	 * 
	 * @return Integer
	 */
	public int getChequeDate() {
		return StaticTool.bcToRoc(this.chequeDate);
	}

	/**
	 * 支票日期<br>
	 * 
	 *
	 * @param chequeDate 支票日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setChequeDate(int chequeDate) throws LogicException {
		this.chequeDate = StaticTool.rocToBc(chequeDate);
	}

	/**
	 * 建檔日<br>
	 * 
	 * @return Integer
	 */
	public int getKeyInDate() {
		return StaticTool.bcToRoc(this.keyInDate);
	}

	/**
	 * 建檔日<br>
	 * 
	 *
	 * @param keyInDate 建檔日
	 * @throws LogicException when Date Is Warn
	 */
	public void setKeyInDate(int keyInDate) throws LogicException {
		this.keyInDate = StaticTool.rocToBc(keyInDate);
	}

	/**
	 * 預計交換日<br>
	 * 
	 * @return Integer
	 */
	public int getExpectedExchangeDate() {
		return StaticTool.bcToRoc(this.expectedExchangeDate);
	}

	/**
	 * 預計交換日<br>
	 * 
	 *
	 * @param expectedExchangeDate 預計交換日
	 * @throws LogicException when Date Is Warn
	 */
	public void setExpectedExchangeDate(int expectedExchangeDate) throws LogicException {
		this.expectedExchangeDate = StaticTool.rocToBc(expectedExchangeDate);
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
		return "AcCheque [acChequeId=" + acChequeId + ", bankNo=" + bankNo + ", chequeAccount=" + chequeAccount + ", amt=" + amt + ", chequeDate=" + chequeDate + ", keyInDate=" + keyInDate
				+ ", expectedExchangeDate=" + expectedExchangeDate + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo="
				+ lastUpdateEmpNo + "]";
	}
}
