package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * JcicZ069 更生債權金額異動通知資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicZ069Id implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4851951077571659179L;

// 報送單位代號
	@Column(name = "`SubmitKey`", length = 3)
	private String submitKey = " ";

	// 債務人IDN
	/* 目前可輸入四碼 */
	@Column(name = "`CustId`", length = 10)
	private String custId = " ";

	// 款項統一收付申請日
	@Column(name = "`ApplyDate`")
	private int applyDate = 0;

	// 異動債權金機構代號
	@Column(name = "`BankId`", length = 3)
	private String bankId = " ";

	public JcicZ069Id() {
	}

	public JcicZ069Id(String submitKey, String custId, int applyDate, String bankId) {
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
	 * 目前可輸入四碼
	 * 
	 * @return String
	 */
	public String getCustId() {
		return this.custId == null ? "" : this.custId;
	}

	/**
	 * 債務人IDN<br>
	 * 目前可輸入四碼
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
	 * 異動債權金機構代號<br>
	 * 
	 * @return String
	 */
	public String getBankId() {
		return this.bankId == null ? "" : this.bankId;
	}

	/**
	 * 異動債權金機構代號<br>
	 * 
	 *
	 * @param bankId 異動債權金機構代號
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
		JcicZ069Id jcicZ069Id = (JcicZ069Id) obj;
		return submitKey.equals(jcicZ069Id.submitKey) && custId.equals(jcicZ069Id.custId) && applyDate == jcicZ069Id.applyDate && bankId.equals(jcicZ069Id.bankId);
	}

	@Override
	public String toString() {
		return "JcicZ069Id [submitKey=" + submitKey + ", custId=" + custId + ", applyDate=" + applyDate + ", bankId=" + bankId + "]";
	}
}
