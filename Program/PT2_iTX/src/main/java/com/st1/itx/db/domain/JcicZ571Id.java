package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * JcicZ571 更生款項統一收付回報債權資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicZ571Id implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6545893022637278365L;

// 報送單位代號
	/* 3位文數字 */
	@Column(name = "`SubmitKey`", length = 3)
	private String submitKey = " ";

	// 債務人IDN
	@Column(name = "`CustId`", length = 10)
	private String custId = " ";

	// 申請日期
	@Column(name = "`ApplyDate`")
	private int applyDate = 0;

	// 受理款項統一收付之債權金融機構代號
	/* 3位文數字 */
	@Column(name = "`BankId`", length = 3)
	private String bankId = " ";

	public JcicZ571Id() {
	}

	public JcicZ571Id(String submitKey, String custId, int applyDate, String bankId) {
		this.submitKey = submitKey;
		this.custId = custId;
		this.applyDate = applyDate;
		this.bankId = bankId;
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
	 * 申請日期<br>
	 * 
	 * @return Integer
	 */
	public int getApplyDate() {
		return StaticTool.bcToRoc(this.applyDate);
	}

	/**
	 * 申請日期<br>
	 * 
	 *
	 * @param applyDate 申請日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setApplyDate(int applyDate) throws LogicException {
		this.applyDate = StaticTool.rocToBc(applyDate);
	}

	/**
	 * 受理款項統一收付之債權金融機構代號<br>
	 * 3位文數字
	 * 
	 * @return String
	 */
	public String getBankId() {
		return this.bankId == null ? "" : this.bankId;
	}

	/**
	 * 受理款項統一收付之債權金融機構代號<br>
	 * 3位文數字
	 *
	 * @param bankId 受理款項統一收付之債權金融機構代號
	 */
	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(submitKey, custId, applyDate, bankId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		JcicZ571Id jcicZ571Id = (JcicZ571Id) obj;
		return submitKey.equals(jcicZ571Id.submitKey) && custId.equals(jcicZ571Id.custId) && applyDate == jcicZ571Id.applyDate && bankId == jcicZ571Id.bankId;
	}

	@Override
	public String toString() {
		return "JcicZ571Id [submitKey=" + submitKey + ", custId=" + custId + ", applyDate=" + applyDate + ", bankId=" + bankId + "]";
	}
}
