package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * JcicZ068 更生款項統一收付結案通知資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicZ068Id implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9169472364752702523L;

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

	public JcicZ068Id() {
	}

	public JcicZ068Id(String submitKey, String custId, int applyDate) {
		this.submitKey = submitKey;
		this.custId = custId;
		this.applyDate = applyDate;
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

	@Override
	public int hashCode() {
		return Objects.hash(submitKey, custId, applyDate);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		JcicZ068Id jcicZ068Id = (JcicZ068Id) obj;
		return submitKey.equals(jcicZ068Id.submitKey) && custId.equals(jcicZ068Id.custId) && applyDate == jcicZ068Id.applyDate;
	}

	@Override
	public String toString() {
		return "JcicZ068Id [submitKey=" + submitKey + ", custId=" + custId + ", applyDate=" + applyDate + "]";
	}
}
