package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * CdAcCode 會計科子細目設定檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class CdAcCodeId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3242146605569101291L;

// 科目代號
	@Column(name = "`AcNoCode`", length = 11)
	private String acNoCode = " ";

	// 子目代號
	@Column(name = "`AcSubCode`", length = 5)
	private String acSubCode = " ";

	// 細目代號
	@Column(name = "`AcDtlCode`", length = 2)
	private String acDtlCode = " ";

	public CdAcCodeId() {
	}

	public CdAcCodeId(String acNoCode, String acSubCode, String acDtlCode) {
		this.acNoCode = acNoCode;
		this.acSubCode = acSubCode;
		this.acDtlCode = acDtlCode;
	}

	/**
	 * 科目代號<br>
	 * 
	 * @return String
	 */
	public String getAcNoCode() {
		return this.acNoCode == null ? "" : this.acNoCode;
	}

	/**
	 * 科目代號<br>
	 * 
	 *
	 * @param acNoCode 科目代號
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
	 * 細目代號<br>
	 * 
	 * @return String
	 */
	public String getAcDtlCode() {
		return this.acDtlCode == null ? "" : this.acDtlCode;
	}

	/**
	 * 細目代號<br>
	 * 
	 *
	 * @param acDtlCode 細目代號
	 */
	public void setAcDtlCode(String acDtlCode) {
		this.acDtlCode = acDtlCode;
	}

	@Override
	public int hashCode() {
		return Objects.hash(acNoCode, acSubCode, acDtlCode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		CdAcCodeId cdAcCodeId = (CdAcCodeId) obj;
		return acNoCode.equals(cdAcCodeId.acNoCode) && acSubCode.equals(cdAcCodeId.acSubCode) && acDtlCode.equals(cdAcCodeId.acDtlCode);
	}

	@Override
	public String toString() {
		return "CdAcCodeId [acNoCode=" + acNoCode + ", acSubCode=" + acSubCode + ", acDtlCode=" + acDtlCode + "]";
	}
}
