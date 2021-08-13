package com.st1.itx.util.common.data;

import javax.persistence.Entity;

@Entity
public class BS020Vo {

	// 借款人戶號
	private int custNo = 0;

	// 額度編號
	private int facmNo = 0;

	// 撥款序號
	private int bormNo = 0;

	public BS020Vo(int custNo, int facmNo, int bormNo) {
		super();
		this.custNo = custNo;
		this.facmNo = facmNo;
		this.bormNo = bormNo;
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
}
