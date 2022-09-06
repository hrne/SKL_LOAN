package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * BatxDetail 整批入帳明細檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class BatxDetailId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4164854825790661700L;

// 會計日期
	@Column(name = "`AcDate`")
	private int acDate = 0;

	// 整批批號
	/* 暫收冲正固定為RESV00 */
	@Column(name = "`BatchNo`", length = 6)
	private String batchNo = " ";

	// 明細序號
	/* 04.支票兌現時，按額度分拆 */
	@Column(name = "`DetailSeq`")
	private int detailSeq = 0;

	public BatxDetailId() {
	}

	public BatxDetailId(int acDate, String batchNo, int detailSeq) {
		this.acDate = acDate;
		this.batchNo = batchNo;
		this.detailSeq = detailSeq;
	}

	/**
	 * 會計日期<br>
	 * 
	 * @return Integer
	 */
	public int getAcDate() {
		return StaticTool.bcToRoc(this.acDate);
	}

	/**
	 * 會計日期<br>
	 * 
	 *
	 * @param acDate 會計日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setAcDate(int acDate) throws LogicException {
		this.acDate = StaticTool.rocToBc(acDate);
	}

	/**
	 * 整批批號<br>
	 * 暫收冲正固定為RESV00
	 * 
	 * @return String
	 */
	public String getBatchNo() {
		return this.batchNo == null ? "" : this.batchNo;
	}

	/**
	 * 整批批號<br>
	 * 暫收冲正固定為RESV00
	 *
	 * @param batchNo 整批批號
	 */
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	/**
	 * 明細序號<br>
	 * 04.支票兌現時，按額度分拆
	 * 
	 * @return Integer
	 */
	public int getDetailSeq() {
		return this.detailSeq;
	}

	/**
	 * 明細序號<br>
	 * 04.支票兌現時，按額度分拆
	 *
	 * @param detailSeq 明細序號
	 */
	public void setDetailSeq(int detailSeq) {
		this.detailSeq = detailSeq;
	}

	@Override
	public int hashCode() {
		return Objects.hash(acDate, batchNo, detailSeq);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		BatxDetailId batxDetailId = (BatxDetailId) obj;
		return acDate == batxDetailId.acDate && batchNo.equals(batxDetailId.batchNo) && detailSeq == batxDetailId.detailSeq;
	}

	@Override
	public String toString() {
		return "BatxDetailId [acDate=" + acDate + ", batchNo=" + batchNo + ", detailSeq=" + detailSeq + "]";
	}
}
