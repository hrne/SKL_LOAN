package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * JcicZ052 前置協商相關資料報送例外處理<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicZ052Id implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -302609396835835400L;

// 報送單位代號
	/* 三位文數字 */
	@Column(name = "`SubmitKey`", length = 3)
	private String submitKey = " ";

	// 債務人IDN
	@Column(name = "`CustId`", length = 10)
	private String custId = " ";

	// 協商申請日
	/* YYYYMMDD */
	@Column(name = "`RcDate`")
	private int rcDate = 0;

	public JcicZ052Id() {
	}

	public JcicZ052Id(String submitKey, String custId, int rcDate) {
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
	 * 協商申請日<br>
	 * YYYYMMDD
	 * 
	 * @return Integer
	 */
	public int getRcDate() {
		return StaticTool.bcToRoc(this.rcDate);
	}

	/**
	 * 協商申請日<br>
	 * YYYYMMDD
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
		JcicZ052Id jcicZ052Id = (JcicZ052Id) obj;
		return submitKey.equals(jcicZ052Id.submitKey) && custId.equals(jcicZ052Id.custId) && rcDate == jcicZ052Id.rcDate;
	}

	@Override
	public String toString() {
		return "JcicZ052Id [submitKey=" + submitKey + ", custId=" + custId + ", rcDate=" + rcDate + "]";
	}
}
