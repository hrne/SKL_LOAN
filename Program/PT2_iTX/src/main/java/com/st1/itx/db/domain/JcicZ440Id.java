package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * JcicZ440 前置調解受理申請暨請求回報債權通知資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicZ440Id implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1721296216654749814L;

// 報送單位代號
	/* 三位文數字 */
	@Column(name = "`SubmitKey`", length = 3)
	private String submitKey = " ";

	// 債務人IDN
	@Column(name = "`CustId`", length = 10)
	private String custId = " ";

	// 調解申請日
	@Column(name = "`ApplyDate`")
	private int applyDate = 0;

	// 受理調解機構代號
	/* 三位文數字[受理方式]:1法院名稱代號表(CdCode.CourtCode)[受理方式]:2調解委員會所在郵遞區號 */
	@Column(name = "`CourtCode`", length = 3)
	private String courtCode = " ";

	public JcicZ440Id() {
	}

	public JcicZ440Id(String submitKey, String custId, int applyDate, String courtCode) {
		this.submitKey = submitKey;
		this.custId = custId;
		this.applyDate = applyDate;
		this.courtCode = courtCode;
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
	 * 調解申請日<br>
	 * 
	 * @return Integer
	 */
	public int getApplyDate() {
		return StaticTool.bcToRoc(this.applyDate);
	}

	/**
	 * 調解申請日<br>
	 * 
	 *
	 * @param applyDate 調解申請日
	 * @throws LogicException when Date Is Warn
	 */
	public void setApplyDate(int applyDate) throws LogicException {
		this.applyDate = StaticTool.rocToBc(applyDate);
	}

	/**
	 * 受理調解機構代號<br>
	 * 三位文數字 [受理方式]:1 法院名稱代號表(CdCode.CourtCode) [受理方式]:2 調解委員會所在郵遞區號
	 * 
	 * @return String
	 */
	public String getCourtCode() {
		return this.courtCode == null ? "" : this.courtCode;
	}

	/**
	 * 受理調解機構代號<br>
	 * 三位文數字 [受理方式]:1 法院名稱代號表(CdCode.CourtCode) [受理方式]:2 調解委員會所在郵遞區號
	 *
	 * @param courtCode 受理調解機構代號
	 */
	public void setCourtCode(String courtCode) {
		this.courtCode = courtCode;
	}

	@Override
	public int hashCode() {
		return Objects.hash(submitKey, custId, applyDate, courtCode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		JcicZ440Id jcicZ440Id = (JcicZ440Id) obj;
		return submitKey.equals(jcicZ440Id.submitKey) && custId.equals(jcicZ440Id.custId) && applyDate == jcicZ440Id.applyDate && courtCode == jcicZ440Id.courtCode;
	}

	@Override
	public String toString() {
		return "JcicZ440Id [submitKey=" + submitKey + ", custId=" + custId + ", applyDate=" + applyDate + ", courtCode=" + courtCode + "]";
	}
}
