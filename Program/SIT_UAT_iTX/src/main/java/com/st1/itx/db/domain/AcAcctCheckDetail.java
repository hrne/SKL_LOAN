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
 * AcAcctCheckDetail 會計業務檢核明細檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`AcAcctCheckDetail`")
public class AcAcctCheckDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7222074947547907877L;

	@EmbeddedId
	private AcAcctCheckDetailId acAcctCheckDetailId;

	// 會計日期
	@Column(name = "`AcDate`", insertable = false, updatable = false)
	private int acDate = 0;

	// 單位別
	@Column(name = "`BranchNo`", length = 4, insertable = false, updatable = false)
	private String branchNo;

	// 幣別
	@Column(name = "`CurrencyCode`", length = 3, insertable = false, updatable = false)
	private String currencyCode;

	// 業務科目代號
	@Column(name = "`AcctCode`", length = 3, insertable = false, updatable = false)
	private String acctCode;

	// 業務科目名稱
	@Column(name = "`AcctItem`", length = 20)
	private String acctItem;

	// 戶號
	@Column(name = "`CustNo`", insertable = false, updatable = false)
	private int custNo = 0;

	// 額度號碼
	@Column(name = "`FacmNo`", insertable = false, updatable = false)
	private int facmNo = 0;

	// 撥款序號
	@Column(name = "`BormNo`", insertable = false, updatable = false)
	private int bormNo = 0;

	// 會計帳餘額
	@Column(name = "`AcBal`")
	private BigDecimal acBal = new BigDecimal("0");

	// 業務帳餘額
	@Column(name = "`AcctMasterBal`")
	private BigDecimal acctMasterBal = new BigDecimal("0");

	// 差額
	/* 會計帳餘額-業務帳餘額 */
	@Column(name = "`DiffBal`")
	private BigDecimal diffBal = new BigDecimal("0");

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

	public AcAcctCheckDetailId getAcAcctCheckDetailId() {
		return this.acAcctCheckDetailId;
	}

	public void setAcAcctCheckDetailId(AcAcctCheckDetailId acAcctCheckDetailId) {
		this.acAcctCheckDetailId = acAcctCheckDetailId;
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
	 * 業務科目名稱<br>
	 * 
	 * @return String
	 */
	public String getAcctItem() {
		return this.acctItem == null ? "" : this.acctItem;
	}

	/**
	 * 業務科目名稱<br>
	 * 
	 *
	 * @param acctItem 業務科目名稱
	 */
	public void setAcctItem(String acctItem) {
		this.acctItem = acctItem;
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
	 * 額度號碼<br>
	 * 
	 * @return Integer
	 */
	public int getFacmNo() {
		return this.facmNo;
	}

	/**
	 * 額度號碼<br>
	 * 
	 *
	 * @param facmNo 額度號碼
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
	 * 會計帳餘額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getAcBal() {
		return this.acBal;
	}

	/**
	 * 會計帳餘額<br>
	 * 
	 *
	 * @param acBal 會計帳餘額
	 */
	public void setAcBal(BigDecimal acBal) {
		this.acBal = acBal;
	}

	/**
	 * 業務帳餘額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getAcctMasterBal() {
		return this.acctMasterBal;
	}

	/**
	 * 業務帳餘額<br>
	 * 
	 *
	 * @param acctMasterBal 業務帳餘額
	 */
	public void setAcctMasterBal(BigDecimal acctMasterBal) {
		this.acctMasterBal = acctMasterBal;
	}

	/**
	 * 差額<br>
	 * 會計帳餘額-業務帳餘額
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getDiffBal() {
		return this.diffBal;
	}

	/**
	 * 差額<br>
	 * 會計帳餘額-業務帳餘額
	 *
	 * @param diffBal 差額
	 */
	public void setDiffBal(BigDecimal diffBal) {
		this.diffBal = diffBal;
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
		return "AcAcctCheckDetail [acAcctCheckDetailId=" + acAcctCheckDetailId + ", acctItem=" + acctItem + ", acBal=" + acBal + ", acctMasterBal=" + acctMasterBal + ", diffBal=" + diffBal
				+ ", createEmpNo=" + createEmpNo + ", createDate=" + createDate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + ", lastUpdate=" + lastUpdate + "]";
	}
}
