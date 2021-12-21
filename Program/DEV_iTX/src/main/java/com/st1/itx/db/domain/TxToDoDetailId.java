package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * TxToDoDetail 應處理明細檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class TxToDoDetailId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5558323046511718352L;

// 項目
	@Column(name = "`ItemCode`", length = 6)
	private String itemCode = " ";

	// 借款人戶號
	@Column(name = "`CustNo`")
	private int custNo = 0;

	// 額度編號
	@Column(name = "`FacmNo`")
	private int facmNo = 0;

	// 撥款序號
	@Column(name = "`BormNo`")
	private int bormNo = 0;

	// 明細鍵值
	/* 應處理清單檔&amp;lt;應處理清單&amp;gt; */
	@Column(name = "`DtlValue`", length = 30)
	private String dtlValue = " ";

	public TxToDoDetailId() {
	}

	public TxToDoDetailId(String itemCode, int custNo, int facmNo, int bormNo, String dtlValue) {
		this.itemCode = itemCode;
		this.custNo = custNo;
		this.facmNo = facmNo;
		this.bormNo = bormNo;
		this.dtlValue = dtlValue;
	}

	/**
	 * 項目<br>
	 * 
	 * @return String
	 */
	public String getItemCode() {
		return this.itemCode == null ? "" : this.itemCode;
	}

	/**
	 * 項目<br>
	 * 
	 *
	 * @param itemCode 項目
	 */
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
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
	 * 明細鍵值<br>
	 * 應處理清單檔&amp;lt;應處理清單&amp;gt;
	 * 
	 * @return String
	 */
	public String getDtlValue() {
		return this.dtlValue == null ? "" : this.dtlValue;
	}

	/**
	 * 明細鍵值<br>
	 * 應處理清單檔&amp;lt;應處理清單&amp;gt;
	 *
	 * @param dtlValue 明細鍵值
	 */
	public void setDtlValue(String dtlValue) {
		this.dtlValue = dtlValue;
	}

	@Override
	public int hashCode() {
		return Objects.hash(itemCode, custNo, facmNo, bormNo, dtlValue);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		TxToDoDetailId txToDoDetailId = (TxToDoDetailId) obj;
		return itemCode.equals(txToDoDetailId.itemCode) && custNo == txToDoDetailId.custNo && facmNo == txToDoDetailId.facmNo && bormNo == txToDoDetailId.bormNo
				&& dtlValue.equals(txToDoDetailId.dtlValue);
	}

	@Override
	public String toString() {
		return "TxToDoDetailId [itemCode=" + itemCode + ", custNo=" + custNo + ", facmNo=" + facmNo + ", bormNo=" + bormNo + ", dtlValue=" + dtlValue + "]";
	}
}
