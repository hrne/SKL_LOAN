package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * JcicZ064 受理更生款項統一收付通知<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicZ064Id implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7040626281066630658L;

// 報送單位代號
	@Column(name = "`SubmitKey`", length = 3)
	private String submitKey = " ";

	// 債務人IDN
	@Column(name = "`CustId`", length = 10)
	private String custId = " ";

	// 款項統一收付申請日
	@Column(name = "`ApplyDate`")
	private int applyDate = 0;

	public JcicZ064Id() {
	}

	public JcicZ064Id(String submitKey, String custId, int applyDate) {
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
		JcicZ064Id jcicZ064Id = (JcicZ064Id) obj;
		return submitKey.equals(jcicZ064Id.submitKey) && custId.equals(jcicZ064Id.custId) && applyDate == jcicZ064Id.applyDate;
	}

	@Override
	public String toString() {
		return "JcicZ064Id [submitKey=" + submitKey + ", custId=" + custId + ", applyDate=" + applyDate + "]";
	}
}
