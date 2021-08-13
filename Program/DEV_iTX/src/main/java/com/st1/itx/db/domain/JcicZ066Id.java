package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * JcicZ066 受理更生款項統一收付通知<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicZ066Id implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1362275968736674660L;

// 報送單位代號
	@Column(name = "`SubmitKey`", length = 3)
	private String submitKey = " ";

	// 債務人IDN
	@Column(name = "`CustId`", length = 10)
	private String custId = " ";

	// 款項統一收付申請日
	@Column(name = "`ApplyDate`")
	private int applyDate = 0;

	// 本分配表首繳日
	@Column(name = "`PayDate`")
	private int payDate = 0;

	// 債權金融機構代號
	@Column(name = "`BankId`", length = 10)
	private String bankId = " ";

	public JcicZ066Id() {
	}

	public JcicZ066Id(String submitKey, String custId, int applyDate, int payDate, String bankId) {
		this.submitKey = submitKey;
		this.custId = custId;
		this.applyDate = applyDate;
		this.payDate = payDate;
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
	 * 本分配表首繳日<br>
	 * 
	 * @return Integer
	 */
	public int getPayDate() {
		return StaticTool.bcToRoc(this.payDate);
	}

	/**
	 * 本分配表首繳日<br>
	 * 
	 *
	 * @param payDate 本分配表首繳日
	 * @throws LogicException when Date Is Warn
	 */
	public void setPayDate(int payDate) throws LogicException {
		this.payDate = StaticTool.rocToBc(payDate);
	}

	/**
	 * 債權金融機構代號<br>
	 * 
	 * @return String
	 */
	public String getBankId() {
		return this.bankId == null ? "" : this.bankId;
	}

	/**
	 * 債權金融機構代號<br>
	 * 
	 *
	 * @param bankId 債權金融機構代號
	 */
	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(submitKey, custId, applyDate, payDate, bankId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		JcicZ066Id jcicZ066Id = (JcicZ066Id) obj;
		return submitKey.equals(jcicZ066Id.submitKey) && custId.equals(jcicZ066Id.custId) && applyDate == jcicZ066Id.applyDate && payDate == jcicZ066Id.payDate && bankId.equals(jcicZ066Id.bankId);
	}

	@Override
	public String toString() {
		return "JcicZ066Id [submitKey=" + submitKey + ", custId=" + custId + ", applyDate=" + applyDate + ", payDate=" + payDate + ", bankId=" + bankId + "]";
	}
}
