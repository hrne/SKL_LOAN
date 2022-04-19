package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * AcAcctCheckDetail 會計業務檢核明細檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class AcAcctCheckDetailId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7108968394835240656L;

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

	// 戶號
	@Column(name = "`CustNo`")
	private int custNo = 0;

	// 額度號碼
	@Column(name = "`FacmNo`")
	private int facmNo = 0;

	// 撥款序號
	@Column(name = "`BormNo`")
	private int bormNo = 0;

	// 區隔帳冊
	@Column(name = "`AcSubBookCode`", length = 3)
	private String acSubBookCode = " ";

	public AcAcctCheckDetailId() {
	}

	public AcAcctCheckDetailId(int acDate, String branchNo, String currencyCode, String acctCode, int custNo, int facmNo, int bormNo, String acSubBookCode) {
		this.acDate = acDate;
		this.branchNo = branchNo;
		this.currencyCode = currencyCode;
		this.acctCode = acctCode;
		this.custNo = custNo;
		this.facmNo = facmNo;
		this.bormNo = bormNo;
		this.acSubBookCode = acSubBookCode;
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
	 * 區隔帳冊<br>
	 * 
	 * @return String
	 */
	public String getAcSubBookCode() {
		return this.acSubBookCode == null ? "" : this.acSubBookCode;
	}

	/**
	 * 區隔帳冊<br>
	 * 
	 *
	 * @param acSubBookCode 區隔帳冊
	 */
	public void setAcSubBookCode(String acSubBookCode) {
		this.acSubBookCode = acSubBookCode;
	}

	@Override
	public int hashCode() {
		return Objects.hash(acDate, branchNo, currencyCode, acctCode, custNo, facmNo, bormNo, acSubBookCode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		AcAcctCheckDetailId acAcctCheckDetailId = (AcAcctCheckDetailId) obj;
		return acDate == acAcctCheckDetailId.acDate && branchNo.equals(acAcctCheckDetailId.branchNo) && currencyCode.equals(acAcctCheckDetailId.currencyCode)
				&& acctCode.equals(acAcctCheckDetailId.acctCode) && custNo == acAcctCheckDetailId.custNo && facmNo == acAcctCheckDetailId.facmNo && bormNo == acAcctCheckDetailId.bormNo
				&& acSubBookCode.equals(acAcctCheckDetailId.acSubBookCode);
	}

	@Override
	public String toString() {
		return "AcAcctCheckDetailId [acDate=" + acDate + ", branchNo=" + branchNo + ", currencyCode=" + currencyCode + ", acctCode=" + acctCode + ", custNo=" + custNo + ", facmNo=" + facmNo
				+ ", bormNo=" + bormNo + ", acSubBookCode=" + acSubBookCode + "]";
	}
}
