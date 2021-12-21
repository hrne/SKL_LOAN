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
 * SlipMedia 傳票媒體檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`SlipMedia`")
public class SlipMedia implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5650797052733814392L;

	@EmbeddedId
	private SlipMediaId slipMediaId;

	// 單位別
	@Column(name = "`BranchNo`", length = 4, insertable = false, updatable = false)
	private String branchNo;

	// 會計日期
	@Column(name = "`AcDate`", insertable = false, updatable = false)
	private int acDate = 0;

	// 傳票批號
	@Column(name = "`BatchNo`", insertable = false, updatable = false)
	private int batchNo = 0;

	// 帳冊別代碼
	@Column(name = "`AcBookCode`", length = 3, insertable = false, updatable = false)
	private String acBookCode;

	// 媒體檔上傳序號
	/*
	 * 同單位別、會計日期、傳票批號時，根據帳冊別代碼依序給予序號ROW_NUMBER() OVER (PARTITION BY
	 * BranchNo,AcDate,BatchNo ORDER BY AcBookCode)*要注意重複產生傳票媒體時,應先取最後一筆序號為基數
	 */
	@Column(name = "`MediaSeq`", insertable = false, updatable = false)
	private int mediaSeq = 0;

	// 媒體檔傳票號碼
	/* "F"+year+month(123456789ABC)+day+mediaSeq核心傳票媒體上傳序號 */
	@Column(name = "`MediaSlipNo`", length = 10, insertable = false, updatable = false)
	private String mediaSlipNo;

	// 傳票序號
	@Column(name = "`Seq`", insertable = false, updatable = false)
	private int seq = 0;

	// 帳冊別名稱
	@Column(name = "`AcBookItem`", length = 50)
	private String acBookItem;

	// 會計科目代號
	@Column(name = "`AcNoCode`", length = 11)
	private String acNoCode;

	// 子目代號
	@Column(name = "`AcSubCode`", length = 5)
	private String acSubCode;

	// 會計科目名稱
	@Column(name = "`AcNoItem`", length = 40)
	private String acNoItem;

	// 幣別
	@Column(name = "`CurrencyCode`", length = 3)
	private String currencyCode;

	// 借貸別
	/* D:借;C:貸 */
	@Column(name = "`DbCr`", length = 1)
	private String dbCr;

	// 記帳金額
	@Column(name = "`TxAmt`")
	private BigDecimal txAmt = new BigDecimal("0");

	// 會計科目銷帳碼
	@Column(name = "`ReceiveCode`", length = 15)
	private String receiveCode;

	// 部門代號
	@Column(name = "`DeptCode`", length = 6)
	private String deptCode;

	// 傳票摘要
	@Column(name = "`SlipRmk`", length = 40)
	private String slipRmk;

	// 成本月份
	@Column(name = "`CostMonth`", length = 1)
	private String costMonth;

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

	public SlipMediaId getSlipMediaId() {
		return this.slipMediaId;
	}

	public void setSlipMediaId(SlipMediaId slipMediaId) {
		this.slipMediaId = slipMediaId;
	}

	/**
	 * 單位別<br>
	 * 
	 * @return String
	 */
	public String getBranchNo() {
		return this.branchNo == null ? "" : this.branchNo;
	}

	/**
	 * 單位別<br>
	 * 
	 *
	 * @param branchNo 單位別
	 */
	public void setBranchNo(String branchNo) {
		this.branchNo = branchNo;
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
	 * 傳票批號<br>
	 * 
	 * @return Integer
	 */
	public int getBatchNo() {
		return this.batchNo;
	}

	/**
	 * 傳票批號<br>
	 * 
	 *
	 * @param batchNo 傳票批號
	 */
	public void setBatchNo(int batchNo) {
		this.batchNo = batchNo;
	}

	/**
	 * 帳冊別代碼<br>
	 * 
	 * @return String
	 */
	public String getAcBookCode() {
		return this.acBookCode == null ? "" : this.acBookCode;
	}

	/**
	 * 帳冊別代碼<br>
	 * 
	 *
	 * @param acBookCode 帳冊別代碼
	 */
	public void setAcBookCode(String acBookCode) {
		this.acBookCode = acBookCode;
	}

	/**
	 * 媒體檔上傳序號<br>
	 * 同單位別、會計日期、傳票批號時，根據帳冊別代碼依序給予序號 ROW_NUMBER() OVER ( PARTITION BY
	 * BranchNo,AcDate,BatchNo ORDER BY AcBookCode) 要注意重複產生傳票媒體時,應先取最後一筆序號為基數
	 * 
	 * @return Integer
	 */
	public int getMediaSeq() {
		return this.mediaSeq;
	}

	/**
	 * 媒體檔上傳序號<br>
	 * 同單位別、會計日期、傳票批號時，根據帳冊別代碼依序給予序號 ROW_NUMBER() OVER ( PARTITION BY
	 * BranchNo,AcDate,BatchNo ORDER BY AcBookCode) 要注意重複產生傳票媒體時,應先取最後一筆序號為基數
	 *
	 * @param mediaSeq 媒體檔上傳序號
	 */
	public void setMediaSeq(int mediaSeq) {
		this.mediaSeq = mediaSeq;
	}

	/**
	 * 媒體檔傳票號碼<br>
	 * "F"+year+month(123456789ABC)+day+mediaSeq核心傳票媒體上傳序號
	 * 
	 * @return String
	 */
	public String getMediaSlipNo() {
		return this.mediaSlipNo == null ? "" : this.mediaSlipNo;
	}

	/**
	 * 媒體檔傳票號碼<br>
	 * "F"+year+month(123456789ABC)+day+mediaSeq核心傳票媒體上傳序號
	 *
	 * @param mediaSlipNo 媒體檔傳票號碼
	 */
	public void setMediaSlipNo(String mediaSlipNo) {
		this.mediaSlipNo = mediaSlipNo;
	}

	/**
	 * 傳票序號<br>
	 * 
	 * @return Integer
	 */
	public int getSeq() {
		return this.seq;
	}

	/**
	 * 傳票序號<br>
	 * 
	 *
	 * @param seq 傳票序號
	 */
	public void setSeq(int seq) {
		this.seq = seq;
	}

	/**
	 * 帳冊別名稱<br>
	 * 
	 * @return String
	 */
	public String getAcBookItem() {
		return this.acBookItem == null ? "" : this.acBookItem;
	}

	/**
	 * 帳冊別名稱<br>
	 * 
	 *
	 * @param acBookItem 帳冊別名稱
	 */
	public void setAcBookItem(String acBookItem) {
		this.acBookItem = acBookItem;
	}

	/**
	 * 會計科目代號<br>
	 * 
	 * @return String
	 */
	public String getAcNoCode() {
		return this.acNoCode == null ? "" : this.acNoCode;
	}

	/**
	 * 會計科目代號<br>
	 * 
	 *
	 * @param acNoCode 會計科目代號
	 */
	public void setAcNoCode(String acNoCode) {
		this.acNoCode = acNoCode;
	}

	/**
	 * 子目代號<br>
	 * 
	 * @return String
	 */
	public String getAcSubCode() {
		return this.acSubCode == null ? "" : this.acSubCode;
	}

	/**
	 * 子目代號<br>
	 * 
	 *
	 * @param acSubCode 子目代號
	 */
	public void setAcSubCode(String acSubCode) {
		this.acSubCode = acSubCode;
	}

	/**
	 * 會計科目名稱<br>
	 * 
	 * @return String
	 */
	public String getAcNoItem() {
		return this.acNoItem == null ? "" : this.acNoItem;
	}

	/**
	 * 會計科目名稱<br>
	 * 
	 *
	 * @param acNoItem 會計科目名稱
	 */
	public void setAcNoItem(String acNoItem) {
		this.acNoItem = acNoItem;
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
	 * 借貸別<br>
	 * D:借;C:貸
	 * 
	 * @return String
	 */
	public String getDbCr() {
		return this.dbCr == null ? "" : this.dbCr;
	}

	/**
	 * 借貸別<br>
	 * D:借;C:貸
	 *
	 * @param dbCr 借貸別
	 */
	public void setDbCr(String dbCr) {
		this.dbCr = dbCr;
	}

	/**
	 * 記帳金額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getTxAmt() {
		return this.txAmt;
	}

	/**
	 * 記帳金額<br>
	 * 
	 *
	 * @param txAmt 記帳金額
	 */
	public void setTxAmt(BigDecimal txAmt) {
		this.txAmt = txAmt;
	}

	/**
	 * 會計科目銷帳碼<br>
	 * 
	 * @return String
	 */
	public String getReceiveCode() {
		return this.receiveCode == null ? "" : this.receiveCode;
	}

	/**
	 * 會計科目銷帳碼<br>
	 * 
	 *
	 * @param receiveCode 會計科目銷帳碼
	 */
	public void setReceiveCode(String receiveCode) {
		this.receiveCode = receiveCode;
	}

	/**
	 * 部門代號<br>
	 * 
	 * @return String
	 */
	public String getDeptCode() {
		return this.deptCode == null ? "" : this.deptCode;
	}

	/**
	 * 部門代號<br>
	 * 
	 *
	 * @param deptCode 部門代號
	 */
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	/**
	 * 傳票摘要<br>
	 * 
	 * @return String
	 */
	public String getSlipRmk() {
		return this.slipRmk == null ? "" : this.slipRmk;
	}

	/**
	 * 傳票摘要<br>
	 * 
	 *
	 * @param slipRmk 傳票摘要
	 */
	public void setSlipRmk(String slipRmk) {
		this.slipRmk = slipRmk;
	}

	/**
	 * 成本月份<br>
	 * 
	 * @return String
	 */
	public String getCostMonth() {
		return this.costMonth == null ? "" : this.costMonth;
	}

	/**
	 * 成本月份<br>
	 * 
	 *
	 * @param costMonth 成本月份
	 */
	public void setCostMonth(String costMonth) {
		this.costMonth = costMonth;
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
		return "SlipMedia [slipMediaId=" + slipMediaId + ", acBookItem=" + acBookItem + ", acNoCode=" + acNoCode + ", acSubCode=" + acSubCode + ", acNoItem=" + acNoItem + ", currencyCode="
				+ currencyCode + ", dbCr=" + dbCr + ", txAmt=" + txAmt + ", receiveCode=" + receiveCode + ", deptCode=" + deptCode + ", slipRmk=" + slipRmk + ", costMonth=" + costMonth
				+ ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
