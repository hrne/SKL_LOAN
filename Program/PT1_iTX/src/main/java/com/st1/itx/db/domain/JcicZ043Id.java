package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * JcicZ043 回報有擔保債權金額資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicZ043Id implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4398701030944850815L;

// 報送單位代號
	/* 三位文數字 */
	@Column(name = "`SubmitKey`", length = 3)
	private String submitKey = " ";

	// 債務人IDN
	/* 身分證號 */
	@Column(name = "`CustId`", length = 10)
	private String custId = " ";

	// 協商申請日
	@Column(name = "`RcDate`")
	private int rcDate = 0;

	// 最大債權金融機構代號
	/* 三位文數字 */
	@Column(name = "`MaxMainCode`", length = 3)
	private String maxMainCode = " ";

	// 帳號
	@Column(name = "`Account`", length = 50)
	private String account = " ";

	public JcicZ043Id() {
	}

	public JcicZ043Id(String submitKey, String custId, int rcDate, String maxMainCode, String account) {
		this.submitKey = submitKey;
		this.custId = custId;
		this.rcDate = rcDate;
		this.maxMainCode = maxMainCode;
		this.account = account;
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
	 * 身分證號
	 * 
	 * @return String
	 */
	public String getCustId() {
		return this.custId == null ? "" : this.custId;
	}

	/**
	 * 債務人IDN<br>
	 * 身分證號
	 *
	 * @param custId 債務人IDN
	 */
	public void setCustId(String custId) {
		this.custId = custId;
	}

	/**
	 * 協商申請日<br>
	 * 
	 * @return Integer
	 */
	public int getRcDate() {
		return StaticTool.bcToRoc(this.rcDate);
	}

	/**
	 * 協商申請日<br>
	 * 
	 *
	 * @param rcDate 協商申請日
	 * @throws LogicException when Date Is Warn
	 */
	public void setRcDate(int rcDate) throws LogicException {
		this.rcDate = StaticTool.rocToBc(rcDate);
	}

	/**
	 * 最大債權金融機構代號<br>
	 * 三位文數字
	 * 
	 * @return String
	 */
	public String getMaxMainCode() {
		return this.maxMainCode == null ? "" : this.maxMainCode;
	}

	/**
	 * 最大債權金融機構代號<br>
	 * 三位文數字
	 *
	 * @param maxMainCode 最大債權金融機構代號
	 */
	public void setMaxMainCode(String maxMainCode) {
		this.maxMainCode = maxMainCode;
	}

	/**
	 * 帳號<br>
	 * 
	 * @return String
	 */
	public String getAccount() {
		return this.account == null ? "" : this.account;
	}

	/**
	 * 帳號<br>
	 * 
	 *
	 * @param account 帳號
	 */
	public void setAccount(String account) {
		this.account = account;
	}

	@Override
	public int hashCode() {
		return Objects.hash(submitKey, custId, rcDate, maxMainCode, account);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		JcicZ043Id jcicZ043Id = (JcicZ043Id) obj;
		return submitKey.equals(jcicZ043Id.submitKey) && custId.equals(jcicZ043Id.custId) && rcDate == jcicZ043Id.rcDate && maxMainCode.equals(jcicZ043Id.maxMainCode)
				&& account.equals(jcicZ043Id.account);
	}

	@Override
	public String toString() {
		return "JcicZ043Id [submitKey=" + submitKey + ", custId=" + custId + ", rcDate=" + rcDate + ", maxMainCode=" + maxMainCode + ", account=" + account + "]";
	}
}
