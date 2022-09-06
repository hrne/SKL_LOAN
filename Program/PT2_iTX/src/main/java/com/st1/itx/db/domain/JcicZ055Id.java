package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * JcicZ055 消債條例更生案件資料報送<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicZ055Id implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7990405479732413129L;

// 報送單位代號
	/* 3位文數字 */
	@Column(name = "`SubmitKey`", length = 3)
	private String submitKey = " ";

	// 債務人IDN
	@Column(name = "`CustId`", length = 10)
	private String custId = " ";

	// 案件狀態
	/* 1:更生程序開始2:更生撤回3:更生方案認可確定4:更生方案履行完畢5:更生裁定免責確定6:更生調查程序 */
	@Column(name = "`CaseStatus`", length = 1)
	private String caseStatus = " ";

	// 裁定日或履行完畢日或發文日
	/*
	 * 指法院裁定日期(案件狀態為1、3、5)，或債權金融機構認定債務人已依更生方案履行完畢之日期(案件狀態為4)，或發文日期(案件狀態為2、6)
	 * 已YYYMMDD表示，本欄位值應小於或等於資料報送日期
	 */
	@Column(name = "`ClaimDate`")
	private int claimDate = 0;

	// 承審法院代碼
	@Column(name = "`CourtCode`", length = 3)
	private String courtCode = " ";

	public JcicZ055Id() {
	}

	public JcicZ055Id(String submitKey, String custId, String caseStatus, int claimDate, String courtCode) {
		this.submitKey = submitKey;
		this.custId = custId;
		this.caseStatus = caseStatus;
		this.claimDate = claimDate;
		this.courtCode = courtCode;
	}

	/**
	 * 報送單位代號<br>
	 * 3位文數字
	 * 
	 * @return String
	 */
	public String getSubmitKey() {
		return this.submitKey == null ? "" : this.submitKey;
	}

	/**
	 * 報送單位代號<br>
	 * 3位文數字
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
	 * 1:更生程序開始 2:更生撤回 3:更生方案認可確定 4:更生方案履行完畢 5:更生裁定免責確定 6:更生調查程序
	 * 
	 * @return String
	 */
	public String getCaseStatus() {
		return this.caseStatus == null ? "" : this.caseStatus;
	}

	/**
	 * 案件狀態<br>
	 * 1:更生程序開始 2:更生撤回 3:更生方案認可確定 4:更生方案履行完畢 5:更生裁定免責確定 6:更生調查程序
	 *
	 * @param caseStatus 案件狀態
	 */
	public void setCaseStatus(String caseStatus) {
		this.caseStatus = caseStatus;
	}

	/**
	 * 裁定日或履行完畢日或發文日<br>
	 * 指法院裁定日期(案件狀態為1、3、5)，或債權金融機構認定債務人已依更生方案履行完畢之日期(案件狀態為4)，或發文日期(案件狀態為2、6)已YYYMMDD表示，本欄位值應小於或等於資料報送日期
	 * 
	 * @return Integer
	 */
	public int getClaimDate() {
		return StaticTool.bcToRoc(this.claimDate);
	}

	/**
	 * 裁定日或履行完畢日或發文日<br>
	 * 指法院裁定日期(案件狀態為1、3、5)，或債權金融機構認定債務人已依更生方案履行完畢之日期(案件狀態為4)，或發文日期(案件狀態為2、6)已YYYMMDD表示，本欄位值應小於或等於資料報送日期
	 *
	 * @param claimDate 裁定日或履行完畢日或發文日
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
		JcicZ055Id jcicZ055Id = (JcicZ055Id) obj;
		return submitKey.equals(jcicZ055Id.submitKey) && custId.equals(jcicZ055Id.custId) && caseStatus.equals(jcicZ055Id.caseStatus) && claimDate == jcicZ055Id.claimDate
				&& courtCode == jcicZ055Id.courtCode;
	}

	@Override
	public String toString() {
		return "JcicZ055Id [submitKey=" + submitKey + ", custId=" + custId + ", caseStatus=" + caseStatus + ", claimDate=" + claimDate + ", courtCode=" + courtCode + "]";
	}
}
