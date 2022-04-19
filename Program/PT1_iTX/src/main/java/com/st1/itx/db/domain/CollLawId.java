package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * CollLaw 法催紀錄法務進度檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class CollLawId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 691479456568819052L;

// 案件種類
	/* 1:法催2:債協 */
	@Column(name = "`CaseCode`", length = 1)
	private String caseCode = " ";

	// 借款人戶號
	@Column(name = "`CustNo`")
	private int custNo = 0;

	// 額度編號
	@Column(name = "`FacmNo`")
	private int facmNo = 0;

	// 作業日期
	@Column(name = "`AcDate`")
	private int acDate = 0;

	// 經辦
	@Column(name = "`TitaTlrNo`", length = 6)
	private String titaTlrNo = " ";

	// 交易序號
	@Column(name = "`TitaTxtNo`", length = 8)
	private String titaTxtNo = " ";

	public CollLawId() {
	}

	public CollLawId(String caseCode, int custNo, int facmNo, int acDate, String titaTlrNo, String titaTxtNo) {
		this.caseCode = caseCode;
		this.custNo = custNo;
		this.facmNo = facmNo;
		this.acDate = acDate;
		this.titaTlrNo = titaTlrNo;
		this.titaTxtNo = titaTxtNo;
	}

	/**
	 * 案件種類<br>
	 * 1:法催 2:債協
	 * 
	 * @return String
	 */
	public String getCaseCode() {
		return this.caseCode == null ? "" : this.caseCode;
	}

	/**
	 * 案件種類<br>
	 * 1:法催 2:債協
	 *
	 * @param caseCode 案件種類
	 */
	public void setCaseCode(String caseCode) {
		this.caseCode = caseCode;
	}

	/**
	 * 借款人戶號<br>
	 * 
	 * @return Integer
	 */
	public int getCustNo() {
		return this.custNo;
	}

	/**
	 * 借款人戶號<br>
	 * 
	 *
	 * @param custNo 借款人戶號
	 */
	public void setCustNo(int custNo) {
		this.custNo = custNo;
	}

	/**
	 * 額度編號<br>
	 * 
	 * @return Integer
	 */
	public int getFacmNo() {
		return this.facmNo;
	}

	/**
	 * 額度編號<br>
	 * 
	 *
	 * @param facmNo 額度編號
	 */
	public void setFacmNo(int facmNo) {
		this.facmNo = facmNo;
	}

	/**
	 * 作業日期<br>
	 * 
	 * @return Integer
	 */
	public int getAcDate() {
		return StaticTool.bcToRoc(this.acDate);
	}

	/**
	 * 作業日期<br>
	 * 
	 *
	 * @param acDate 作業日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setAcDate(int acDate) throws LogicException {
		this.acDate = StaticTool.rocToBc(acDate);
	}

	/**
	 * 經辦<br>
	 * 
	 * @return String
	 */
	public String getTitaTlrNo() {
		return this.titaTlrNo == null ? "" : this.titaTlrNo;
	}

	/**
	 * 經辦<br>
	 * 
	 *
	 * @param titaTlrNo 經辦
	 */
	public void setTitaTlrNo(String titaTlrNo) {
		this.titaTlrNo = titaTlrNo;
	}

	/**
	 * 交易序號<br>
	 * 
	 * @return String
	 */
	public String getTitaTxtNo() {
		return this.titaTxtNo == null ? "" : this.titaTxtNo;
	}

	/**
	 * 交易序號<br>
	 * 
	 *
	 * @param titaTxtNo 交易序號
	 */
	public void setTitaTxtNo(String titaTxtNo) {
		this.titaTxtNo = titaTxtNo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(caseCode, custNo, facmNo, acDate, titaTlrNo, titaTxtNo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		CollLawId collLawId = (CollLawId) obj;
		return caseCode.equals(collLawId.caseCode) && custNo == collLawId.custNo && facmNo == collLawId.facmNo && acDate == collLawId.acDate && titaTlrNo.equals(collLawId.titaTlrNo)
				&& titaTxtNo.equals(collLawId.titaTxtNo);
	}

	@Override
	public String toString() {
		return "CollLawId [caseCode=" + caseCode + ", custNo=" + custNo + ", facmNo=" + facmNo + ", acDate=" + acDate + ", titaTlrNo=" + titaTlrNo + ", titaTxtNo=" + titaTxtNo + "]";
	}
}
