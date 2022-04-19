package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * PfReward 介紹、協辦獎金發放檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class PfRewardId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8934826250830249266L;

// 業績日期
	/* SystemParas 系統參數設定檔PerfDate 業績日期1.撥貸放行時，寫入一筆2.業績追回時新增一筆，獎金欄為負值 */
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

	public PfRewardId() {
	}

	public PfRewardId(int perfDate, int custNo, int facmNo, int bormNo) {
		this.perfDate = perfDate;
		this.custNo = custNo;
		this.facmNo = facmNo;
		this.bormNo = bormNo;
	}

	/**
	 * 業績日期<br>
	 * SystemParas 系統參數設定檔 PerfDate 業績日期 1.撥貸放行時，寫入一筆 2.業績追回時新增一筆，獎金欄為負值
	 * 
	 * @return Integer
	 */
	public int getPerfDate() {
		return StaticTool.bcToRoc(this.perfDate);
	}

	/**
	 * 業績日期<br>
	 * SystemParas 系統參數設定檔 PerfDate 業績日期 1.撥貸放行時，寫入一筆 2.業績追回時新增一筆，獎金欄為負值
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

	@Override
	public int hashCode() {
		return Objects.hash(perfDate, custNo, facmNo, bormNo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		PfRewardId pfRewardId = (PfRewardId) obj;
		return perfDate == pfRewardId.perfDate && custNo == pfRewardId.custNo && facmNo == pfRewardId.facmNo && bormNo == pfRewardId.bormNo;
	}

	@Override
	public String toString() {
		return "PfRewardId [perfDate=" + perfDate + ", custNo=" + custNo + ", facmNo=" + facmNo + ", bormNo=" + bormNo + "]";
	}
}
