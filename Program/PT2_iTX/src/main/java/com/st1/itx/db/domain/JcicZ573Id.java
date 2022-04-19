package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * JcicZ573 更生債務人繳款資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicZ573Id implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3915830049146108362L;

// 報送單位代號
	/* 三位文數字 */
	@Column(name = "`SubmitKey`", length = 3)
	private String submitKey = " ";

	// 債務人IDN
	/* 目前可輸入四碼 */
	@Column(name = "`CustId`", length = 10)
	private String custId = " ";

	// 申請日期
	@Column(name = "`ApplyDate`")
	private int applyDate = 0;

	// 繳款日期
	@Column(name = "`PayDate`")
	private int payDate = 0;

	public JcicZ573Id() {
	}

	public JcicZ573Id(String submitKey, String custId, int applyDate, int payDate) {
		this.submitKey = submitKey;
		this.custId = custId;
		this.applyDate = applyDate;
		this.payDate = payDate;
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
		JcicZ573Id jcicZ573Id = (JcicZ573Id) obj;
		return submitKey.equals(jcicZ573Id.submitKey) && custId.equals(jcicZ573Id.custId) && applyDate == jcicZ573Id.applyDate && payDate == jcicZ573Id.payDate;
	}

	@Override
	public String toString() {
		return "JcicZ573Id [submitKey=" + submitKey + ", custId=" + custId + ", applyDate=" + applyDate + ", payDate=" + payDate + "]";
	}
}
