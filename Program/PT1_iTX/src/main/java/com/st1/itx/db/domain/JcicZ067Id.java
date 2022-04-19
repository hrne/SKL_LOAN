package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * JcicZ067 更生債務人繳款資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicZ067Id implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4793655720035956245L;

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

	// 繳款日期
	@Column(name = "`PayDate`")
	private int payDate = 0;

	public JcicZ067Id() {
	}

	public JcicZ067Id(String submitKey, String custId, int applyDate, int payDate) {
		this.submitKey = submitKey;
		this.custId = custId;
		this.applyDate = applyDate;
		this.payDate = payDate;
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
	 * 繳款日期<br>
	 * 
	 * @return Integer
	 */
	public int getPayDate() {
		return StaticTool.bcToRoc(this.payDate);
	}

	/**
	 * 繳款日期<br>
	 * 
	 *
	 * @param payDate 繳款日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setPayDate(int payDate) throws LogicException {
		this.payDate = StaticTool.rocToBc(payDate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(submitKey, custId, applyDate, payDate);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		JcicZ067Id jcicZ067Id = (JcicZ067Id) obj;
		return submitKey.equals(jcicZ067Id.submitKey) && custId.equals(jcicZ067Id.custId) && applyDate == jcicZ067Id.applyDate && payDate == jcicZ067Id.payDate;
	}

	@Override
	public String toString() {
		return "JcicZ067Id [submitKey=" + submitKey + ", custId=" + custId + ", applyDate=" + applyDate + ", payDate=" + payDate + "]";
	}
}
