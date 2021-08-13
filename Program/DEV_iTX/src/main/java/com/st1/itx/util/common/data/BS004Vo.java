package com.st1.itx.util.common.data;

import javax.persistence.Entity;

@Entity
public class BS004Vo {

	// 借款人戶號
	private int custNo = 0;

	// 額度編號
	private int facmNo = 0;

	// 撥款序號, 預約序號
	private int bormNo = 0;

	// 身份證字號/統一編號
	private String custId = " ";

	public BS004Vo(int custNo, int facmNo, int bormNo, String custId) {
		super();
		this.custNo = custNo;
		this.facmNo = facmNo;
		this.bormNo = bormNo;
		this.custId = custId;
	}

	public int getCustNo() {
		return custNo;
	}

	public void setCustNo(int custNo) {
		this.custNo = custNo;
	}

	public int getFacmNo() {
		return facmNo;
	}

	public void setFacmNo(int facmNo) {
		this.facmNo = facmNo;
	}

	public int getBormNo() {
		return bormNo;
	}

	public void setBormNo(int bormNo) {
		this.bormNo = bormNo;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}
}
