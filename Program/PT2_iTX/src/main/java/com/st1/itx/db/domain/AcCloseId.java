package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * AcClose 會計業務關帳控制檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class AcCloseId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2668014544008755546L;

// 會計日期
	@Column(name = "`AcDate`")
	private int acDate = 0;

	// 單位別
	@Column(name = "`BranchNo`", length = 4)
	private String branchNo = " ";

	// 業務類別
	/* 01:撥款匯款 (含暫收退還且非退票)02:支票繳款 03:債協09:放款 */
	@Column(name = "`SecNo`", length = 2)
	private String secNo = " ";

	public AcCloseId() {
	}

	public AcCloseId(int acDate, String branchNo, String secNo) {
		this.acDate = acDate;
		this.branchNo = branchNo;
		this.secNo = secNo;
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
	 * 業務類別<br>
	 * 01:撥款匯款 (含暫收退還且非退票) 02:支票繳款 03:債協 09:放款
	 * 
	 * @return String
	 */
	public String getSecNo() {
		return this.secNo == null ? "" : this.secNo;
	}

	/**
	 * 業務類別<br>
	 * 01:撥款匯款 (含暫收退還且非退票) 02:支票繳款 03:債協 09:放款
	 *
	 * @param secNo 業務類別
	 */
	public void setSecNo(String secNo) {
		this.secNo = secNo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(acDate, branchNo, secNo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		AcCloseId acCloseId = (AcCloseId) obj;
		return acDate == acCloseId.acDate && branchNo.equals(acCloseId.branchNo) && secNo.equals(acCloseId.secNo);
	}

	@Override
	public String toString() {
		return "AcCloseId [acDate=" + acDate + ", branchNo=" + branchNo + ", secNo=" + secNo + "]";
	}
}
