package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * JcicZ061 回報協商剩餘債權金額資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicZ061Id implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6387741934877112842L;

// 債權金融機構代號
	/* 三位文數字 */
	@Column(name = "`SubmitKey`", length = 3)
	private String submitKey = " ";

	// 債務人IDN
	@Column(name = "`CustId`", length = 10)
	private String custId = " ";

	// 原前置協商申請日
	@Column(name = "`RcDate`")
	private int rcDate = 0;

	// 申請變更還款條件日
	@Column(name = "`ChangePayDate`")
	private int changePayDate = 0;

	// 最大債權金融機構代號
	/* 三位文數字 */
	@Column(name = "`MaxMainCode`", length = 3)
	private String maxMainCode = " ";

	public JcicZ061Id() {
	}

	public JcicZ061Id(String submitKey, String custId, int rcDate, int changePayDate, String maxMainCode) {
		this.submitKey = submitKey;
		this.custId = custId;
		this.rcDate = rcDate;
		this.changePayDate = changePayDate;
		this.maxMainCode = maxMainCode;
	}

	/**
	 * 債權金融機構代號<br>
	 * 三位文數字
	 * 
	 * @return String
	 */
	public String getSubmitKey() {
		return this.submitKey == null ? "" : this.submitKey;
	}

	/**
	 * 債權金融機構代號<br>
	 * 三位文數字
	 *
	 * @param submitKey 債權金融機構代號
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
	 * 原前置協商申請日<br>
	 * 
	 * @return Integer
	 */
	public int getRcDate() {
		return StaticTool.bcToRoc(this.rcDate);
	}

	/**
	 * 原前置協商申請日<br>
	 * 
	 *
	 * @param rcDate 原前置協商申請日
	 * @throws LogicException when Date Is Warn
	 */
	public void setRcDate(int rcDate) throws LogicException {
		this.rcDate = StaticTool.rocToBc(rcDate);
	}

	/**
	 * 申請變更還款條件日<br>
	 * 
	 * @return Integer
	 */
	public int getChangePayDate() {
		return StaticTool.bcToRoc(this.changePayDate);
	}

	/**
	 * 申請變更還款條件日<br>
	 * 
	 *
	 * @param changePayDate 申請變更還款條件日
	 * @throws LogicException when Date Is Warn
	 */
	public void setChangePayDate(int changePayDate) throws LogicException {
		this.changePayDate = StaticTool.rocToBc(changePayDate);
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

	@Override
	public int hashCode() {
		return Objects.hash(submitKey, custId, rcDate, changePayDate, maxMainCode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		JcicZ061Id jcicZ061Id = (JcicZ061Id) obj;
		return submitKey.equals(jcicZ061Id.submitKey) && custId.equals(jcicZ061Id.custId) && rcDate == jcicZ061Id.rcDate && changePayDate == jcicZ061Id.changePayDate
				&& maxMainCode == jcicZ061Id.maxMainCode;
	}

	@Override
	public String toString() {
		return "JcicZ061Id [submitKey=" + submitKey + ", custId=" + custId + ", rcDate=" + rcDate + ", changePayDate=" + changePayDate + ", maxMainCode=" + maxMainCode + "]";
	}
}
