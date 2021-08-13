package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * AcAcctCheck 會計業務檢核檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class AcAcctCheckId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -908778552491216397L;

	// 會計日期
	@Column(name = "`AcDate`")
	private int acDate = 0;

	// 單位別
	@Column(name = "`BranchNo`", length = 4)
	private String branchNo = " ";

	// 幣別
	@Column(name = "`CurrencyCode`", length = 3)
	private String currencyCode = " ";

	// 業務科目代號
	@Column(name = "`AcctCode`", length = 3)
	private String acctCode = " ";

	public AcAcctCheckId() {
	}

	public AcAcctCheckId(int acDate, String branchNo, String currencyCode, String acctCode) {
		this.acDate = acDate;
		this.branchNo = branchNo;
		this.currencyCode = currencyCode;
		this.acctCode = acctCode;
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

	@Override
	public int hashCode() {
		return Objects.hash(acDate, branchNo, currencyCode, acctCode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		AcAcctCheckId acAcctCheckId = (AcAcctCheckId) obj;
		return acDate == acAcctCheckId.acDate && branchNo.equals(acAcctCheckId.branchNo) && currencyCode.equals(acAcctCheckId.currencyCode) && acctCode.equals(acAcctCheckId.acctCode);
	}

	@Override
	public String toString() {
		return "AcAcctCheckId [acDate=" + acDate + ", branchNo=" + branchNo + ", currencyCode=" + currencyCode + ", acctCode=" + acctCode + "]";
	}
}
