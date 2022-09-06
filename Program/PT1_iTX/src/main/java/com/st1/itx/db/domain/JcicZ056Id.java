package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * JcicZ056 清算案件資料報送<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicZ056Id implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -596756425168389643L;

// 報送單位代號
	/* 三位文數字 */
	@Column(name = "`SubmitKey`", length = 3)
	private String submitKey = " ";

	// 債務人IDN
	@Column(name = "`CustId`", length = 10)
	private String custId = " ";

	// 案件狀態
	/* A:清算程序開始B:清算程序終止(結)C:清算程序開始同時終止D:清算撤消免責確定E:清算調查程序G:清算撤回H:清算復權 */
	@Column(name = "`CaseStatus`", length = 1)
	private String caseStatus = " ";

	// 裁定日期或發文日期
	@Column(name = "`ClaimDate`")
	private int claimDate = 0;

	// 承審法院代碼
	@Column(name = "`CourtCode`", length = 3)
	private String courtCode = " ";

	public JcicZ056Id() {
	}

	public JcicZ056Id(String submitKey, String custId, String caseStatus, int claimDate, String courtCode) {
		this.submitKey = submitKey;
		this.custId = custId;
		this.caseStatus = caseStatus;
		this.claimDate = claimDate;
		this.courtCode = courtCode;
	}

	/**
	 * 報送單位代號<br>
	 * 三位文數字
	 * 
	 * @return String
	 */
	public String getSubmitKey() {
		return this.submitKey == null ? "" : this.submitKey;
	}

	/**
	 * 報送單位代號<br>
	 * 三位文數字
	 *
	 * @param submitKey 報送單位代號
	 */
	public void setSubmitKey(String submitKey) {
		this.submitKey = submitKey;
	}

	/**
	 * 債務人IDN<br>
	 * 
	 * @return String
	 */
	public String getCustId() {
		return this.custId == null ? "" : this.custId;
	}

	/**
	 * 債務人IDN<br>
	 * 
	 *
	 * @param custId 債務人IDN
	 */
	public void setCustId(String custId) {
		this.custId = custId;
	}

	/**
	 * 案件狀態<br>
	 * A:清算程序開始 B:清算程序終止(結) C:清算程序開始同時終止 D:清算撤消免責確定 E:清算調查程序 G:清算撤回 H:清算復權
	 * 
	 * @return String
	 */
	public String getCaseStatus() {
		return this.caseStatus == null ? "" : this.caseStatus;
	}

	/**
	 * 案件狀態<br>
	 * A:清算程序開始 B:清算程序終止(結) C:清算程序開始同時終止 D:清算撤消免責確定 E:清算調查程序 G:清算撤回 H:清算復權
	 *
	 * @param caseStatus 案件狀態
	 */
	public void setCaseStatus(String caseStatus) {
		this.caseStatus = caseStatus;
	}

	/**
	 * 裁定日期或發文日期<br>
	 * 
	 * @return Integer
	 */
	public int getClaimDate() {
		return StaticTool.bcToRoc(this.claimDate);
	}

	/**
	 * 裁定日期或發文日期<br>
	 * 
	 *
	 * @param claimDate 裁定日期或發文日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setClaimDate(int claimDate) throws LogicException {
		this.claimDate = StaticTool.rocToBc(claimDate);
	}

	/**
	 * 承審法院代碼<br>
	 * 
	 * @return String
	 */
	public String getCourtCode() {
		return this.courtCode == null ? "" : this.courtCode;
	}

	/**
	 * 承審法院代碼<br>
	 * 
	 *
	 * @param courtCode 承審法院代碼
	 */
	public void setCourtCode(String courtCode) {
		this.courtCode = courtCode;
	}

	@Override
	public int hashCode() {
		return Objects.hash(submitKey, custId, caseStatus, claimDate, courtCode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		JcicZ056Id jcicZ056Id = (JcicZ056Id) obj;
		return submitKey.equals(jcicZ056Id.submitKey) && custId.equals(jcicZ056Id.custId) && caseStatus.equals(jcicZ056Id.caseStatus) && claimDate == jcicZ056Id.claimDate
				&& courtCode == jcicZ056Id.courtCode;
	}

	@Override
	public String toString() {
		return "JcicZ056Id [submitKey=" + submitKey + ", custId=" + custId + ", caseStatus=" + caseStatus + ", claimDate=" + claimDate + ", courtCode=" + courtCode + "]";
	}
}
