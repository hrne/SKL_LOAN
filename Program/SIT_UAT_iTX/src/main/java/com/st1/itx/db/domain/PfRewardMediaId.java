package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * PfRewardMedia 獎金媒體發放檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class PfRewardMediaId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5741244499906090129L;

// 業績日期
	/* 撥款日 */
	@Column(name = "`PerfDate`")
	private int perfDate = 0;

	// 戶號
	@Column(name = "`CustNo`")
	private int custNo = 0;

	// 額度編號
	@Column(name = "`FacmNo`")
	private int facmNo = 0;

	// 撥款序號
	@Column(name = "`BormNo`")
	private int bormNo = 0;

	// 獎金類別
	/*
	 * CdCode.BonusType1:介紹獎金2:放款業務專員津貼3:晤談一人員津貼4:晤談二人員津貼5:協辦獎金6:專業獎勵金7:加碼獎金(
	 * 依照LN270檔-獎勵金發放檔)
	 */
	@Column(name = "`BonusType`")
	private int bonusType = 0;

	public PfRewardMediaId() {
	}

	public PfRewardMediaId(int perfDate, int custNo, int facmNo, int bormNo, int bonusType) {
		this.perfDate = perfDate;
		this.custNo = custNo;
		this.facmNo = facmNo;
		this.bormNo = bormNo;
		this.bonusType = bonusType;
	}

	/**
	 * 業績日期<br>
	 * 撥款日
	 * 
	 * @return Integer
	 */
	public int getPerfDate() {
		return StaticTool.bcToRoc(this.perfDate);
	}

	/**
	 * 業績日期<br>
	 * 撥款日
	 *
	 * @param perfDate 業績日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setPerfDate(int perfDate) throws LogicException {
		this.perfDate = StaticTool.rocToBc(perfDate);
	}

	/**
	 * 戶號<br>
	 * 
	 * @return Integer
	 */
	public int getCustNo() {
		return this.custNo;
	}

	/**
	 * 戶號<br>
	 * 
	 *
	 * @param custNo 戶號
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
	 * 撥款序號<br>
	 * 
	 * @return Integer
	 */
	public int getBormNo() {
		return this.bormNo;
	}

	/**
	 * 撥款序號<br>
	 * 
	 *
	 * @param bormNo 撥款序號
	 */
	public void setBormNo(int bormNo) {
		this.bormNo = bormNo;
	}

	/**
	 * 獎金類別<br>
	 * CdCode.BonusType 1:介紹獎金 2:放款業務專員津貼 3:晤談一人員津貼 4:晤談二人員津貼 5:協辦獎金 6:專業獎勵金 7:加碼獎金
	 * (依照LN270檔-獎勵金發放檔)
	 * 
	 * @return Integer
	 */
	public int getBonusType() {
		return this.bonusType;
	}

	/**
	 * 獎金類別<br>
	 * CdCode.BonusType 1:介紹獎金 2:放款業務專員津貼 3:晤談一人員津貼 4:晤談二人員津貼 5:協辦獎金 6:專業獎勵金 7:加碼獎金
	 * (依照LN270檔-獎勵金發放檔)
	 *
	 * @param bonusType 獎金類別
	 */
	public void setBonusType(int bonusType) {
		this.bonusType = bonusType;
	}

	@Override
	public int hashCode() {
		return Objects.hash(perfDate, custNo, facmNo, bormNo, bonusType);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		PfRewardMediaId pfRewardMediaId = (PfRewardMediaId) obj;
		return perfDate == pfRewardMediaId.perfDate && custNo == pfRewardMediaId.custNo && facmNo == pfRewardMediaId.facmNo && bormNo == pfRewardMediaId.bormNo
				&& bonusType == pfRewardMediaId.bonusType;
	}

	@Override
	public String toString() {
		return "PfRewardMediaId [perfDate=" + perfDate + ", custNo=" + custNo + ", facmNo=" + facmNo + ", bormNo=" + bormNo + ", bonusType=" + bonusType + "]";
	}
}
