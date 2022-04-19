package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * JcicZ044 請求同意債務清償方案通知資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicZ044Id implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5126175986826140283L;

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

	public JcicZ044Id() {
	}

	public JcicZ044Id(String submitKey, String custId, int rcDate) {
		this.submitKey = submitKey;
		this.custId = custId;
		this.rcDate = rcDate;
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

	@Override
	public int hashCode() {
		return Objects.hash(submitKey, custId, rcDate);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		JcicZ044Id jcicZ044Id = (JcicZ044Id) obj;
		return submitKey.equals(jcicZ044Id.submitKey) && custId.equals(jcicZ044Id.custId) && rcDate == jcicZ044Id.rcDate;
	}

	@Override
	public String toString() {
		return "JcicZ044Id [submitKey=" + submitKey + ", custId=" + custId + ", rcDate=" + rcDate + "]";
	}
}
