package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * JcicZ065 受理更生款項統一收付通知<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicZ065Id implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8229291393257116646L;

// 報送單位代號
	@Column(name = "`SubmitKey`", length = 3)
	private String submitKey = " ";

	// 債務人IDN
	@Column(name = "`CustId`", length = 10)
	private String custId = " ";

	// 款項統一收付申請日
	@Column(name = "`ApplyDate`")
	private int applyDate = 0;

	// 受理款項統一收付之債權金融機構代號
	@Column(name = "`BankId`", length = 10)
	private String bankId = " ";

	public JcicZ065Id() {
	}

	public JcicZ065Id(String submitKey, String custId, int applyDate, String bankId) {
		this.submitKey = submitKey;
		this.custId = custId;
		this.applyDate = applyDate;
		this.bankId = bankId;
	}

	/**
	 * 報送單位代號<br>
	 * 
	 * @return String
	 */
	public String getSubmitKey() {
		return this.submitKey == null ? "" : this.submitKey;
	}

	/**
	 * 報送單位代號<br>
	 * 
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
	 * 款項統一收付申請日<br>
	 * 
	 * @return Integer
	 */
	public int getApplyDate() {
		return StaticTool.bcToRoc(this.applyDate);
	}

	/**
	 * 款項統一收付申請日<br>
	 * 
	 *
	 * @param applyDate 款項統一收付申請日
	 * @throws LogicException when Date Is Warn
	 */
	public void setApplyDate(int applyDate) throws LogicException {
		this.applyDate = StaticTool.rocToBc(applyDate);
	}

	/**
	 * 受理款項統一收付之債權金融機構代號<br>
	 * 
	 * @return String
	 */
	public String getBankId() {
		return this.bankId == null ? "" : this.bankId;
	}

	/**
	 * 受理款項統一收付之債權金融機構代號<br>
	 * 
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
		JcicZ065Id jcicZ065Id = (JcicZ065Id) obj;
		return submitKey.equals(jcicZ065Id.submitKey) && custId.equals(jcicZ065Id.custId) && applyDate == jcicZ065Id.applyDate && bankId.equals(jcicZ065Id.bankId);
	}

	@Override
	public String toString() {
		return "JcicZ065Id [submitKey=" + submitKey + ", custId=" + custId + ", applyDate=" + applyDate + ", bankId=" + bankId + "]";
	}
}
