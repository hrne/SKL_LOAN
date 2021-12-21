package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * AcMain 會計總帳檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class AcMainId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1699563478594269783L;

// 帳冊別
	/* 000：全公司 */
	@Column(name = "`AcBookCode`", length = 3)
	private String acBookCode = " ";

	// 區隔帳冊
	/* 00A：傳統帳冊 201:利變年金帳冊 */
	@Column(name = "`AcSubBookCode`", length = 3)
	private String acSubBookCode = " ";

	// 單位別
	@Column(name = "`BranchNo`", length = 4)
	private String branchNo = " ";

	// 幣別
	@Column(name = "`CurrencyCode`", length = 3)
	private String currencyCode = " ";

	// 科目代號
	/* CdAcCode會計科子細目設定檔 */
	@Column(name = "`AcNoCode`", length = 11)
	private String acNoCode = " ";

	// 子目代號
	/* CdAcCode會計科子細目設定檔 */
	@Column(name = "`AcSubCode`", length = 5)
	private String acSubCode = " ";

	// 細目代號
	/* CdAcCode會計科子細目設定檔有細目者，另計記細目為&amp;lt;空白&amp;gt;為加總至科子目 */
	@Column(name = "`AcDtlCode`", length = 2)
	private String acDtlCode = " ";

	// 會計日期
	@Column(name = "`AcDate`")
	private int acDate = 0;

	public AcMainId() {
	}

	public AcMainId(String acBookCode, String acSubBookCode, String branchNo, String currencyCode, String acNoCode, String acSubCode, String acDtlCode, int acDate) {
		this.acBookCode = acBookCode;
		this.acSubBookCode = acSubBookCode;
		this.branchNo = branchNo;
		this.currencyCode = currencyCode;
		this.acNoCode = acNoCode;
		this.acSubCode = acSubCode;
		this.acDtlCode = acDtlCode;
		this.acDate = acDate;
	}

	/**
	 * 帳冊別<br>
	 * 000：全公司
	 * 
	 * @return String
	 */
	public String getAcBookCode() {
		return this.acBookCode == null ? "" : this.acBookCode;
	}

	/**
	 * 帳冊別<br>
	 * 000：全公司
	 *
	 * @param acBookCode 帳冊別
	 */
	public void setAcBookCode(String acBookCode) {
		this.acBookCode = acBookCode;
	}

	/**
	 * 區隔帳冊<br>
	 * 00A：傳統帳冊 201:利變年金帳冊
	 * 
	 * @return String
	 */
	public String getAcSubBookCode() {
		return this.acSubBookCode == null ? "" : this.acSubBookCode;
	}

	/**
	 * 區隔帳冊<br>
	 * 00A：傳統帳冊 201:利變年金帳冊
	 *
	 * @param acSubBookCode 區隔帳冊
	 */
	public void setAcSubBookCode(String acSubBookCode) {
		this.acSubBookCode = acSubBookCode;
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
	 * 科目代號<br>
	 * CdAcCode會計科子細目設定檔
	 * 
	 * @return String
	 */
	public String getAcNoCode() {
		return this.acNoCode == null ? "" : this.acNoCode;
	}

	/**
	 * 科目代號<br>
	 * CdAcCode會計科子細目設定檔
	 *
	 * @param acNoCode 科目代號
	 */
	public void setAcNoCode(String acNoCode) {
		this.acNoCode = acNoCode;
	}

	/**
	 * 子目代號<br>
	 * CdAcCode會計科子細目設定檔
	 * 
	 * @return String
	 */
	public String getAcSubCode() {
		return this.acSubCode == null ? "" : this.acSubCode;
	}

	/**
	 * 子目代號<br>
	 * CdAcCode會計科子細目設定檔
	 *
	 * @param acSubCode 子目代號
	 */
	public void setAcSubCode(String acSubCode) {
		this.acSubCode = acSubCode;
	}

	/**
	 * 細目代號<br>
	 * CdAcCode會計科子細目設定檔 有細目者，另計記細目為&amp;lt;空白&amp;gt;為加總至科子目
	 * 
	 * @return String
	 */
	public String getAcDtlCode() {
		return this.acDtlCode == null ? "" : this.acDtlCode;
	}

	/**
	 * 細目代號<br>
	 * CdAcCode會計科子細目設定檔 有細目者，另計記細目為&amp;lt;空白&amp;gt;為加總至科子目
	 *
	 * @param acDtlCode 細目代號
	 */
	public void setAcDtlCode(String acDtlCode) {
		this.acDtlCode = acDtlCode;
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

	@Override
	public int hashCode() {
		return Objects.hash(acBookCode, acSubBookCode, branchNo, currencyCode, acNoCode, acSubCode, acDtlCode, acDate);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		AcMainId acMainId = (AcMainId) obj;
		return acBookCode.equals(acMainId.acBookCode) && acSubBookCode.equals(acMainId.acSubBookCode) && branchNo.equals(acMainId.branchNo) && currencyCode.equals(acMainId.currencyCode)
				&& acNoCode.equals(acMainId.acNoCode) && acSubCode.equals(acMainId.acSubCode) && acDtlCode.equals(acMainId.acDtlCode) && acDate == acMainId.acDate;
	}

	@Override
	public String toString() {
		return "AcMainId [acBookCode=" + acBookCode + ", acSubBookCode=" + acSubBookCode + ", branchNo=" + branchNo + ", currencyCode=" + currencyCode + ", acNoCode=" + acNoCode + ", acSubCode="
				+ acSubCode + ", acDtlCode=" + acDtlCode + ", acDate=" + acDate + "]";
	}
}
