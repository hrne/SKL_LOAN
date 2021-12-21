package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * AchAuthLog ACH授權記錄檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class AchAuthLogId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7807549501846701648L;

// 建檔日期
	@Column(name = "`AuthCreateDate`")
	private int authCreateDate = 0;

	// 戶號
	@Column(name = "`CustNo`")
	private int custNo = 0;

	// 扣款銀行
	@Column(name = "`RepayBank`", length = 3)
	private String repayBank = " ";

	// 扣款帳號
	@Column(name = "`RepayAcct`", length = 14)
	private String repayAcct = " ";

	// 新增或取消記號
	/*
	 * A:新增授權D:取消授權Y.刪除(DeleteDate &amp;gt; 0時，顯示用)Z.暫停授權(DeleteDate &amp;gt;
	 * 0時，顯示用)
	 */
	@Column(name = "`CreateFlag`", length = 1)
	private String createFlag = " ";

	public AchAuthLogId() {
	}

	public AchAuthLogId(int authCreateDate, int custNo, String repayBank, String repayAcct, String createFlag) {
		this.authCreateDate = authCreateDate;
		this.custNo = custNo;
		this.repayBank = repayBank;
		this.repayAcct = repayAcct;
		this.createFlag = createFlag;
	}

	/**
	 * 建檔日期<br>
	 * 
	 * @return Integer
	 */
	public int getAuthCreateDate() {
		return StaticTool.bcToRoc(this.authCreateDate);
	}

	/**
	 * 建檔日期<br>
	 * 
	 *
	 * @param authCreateDate 建檔日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setAuthCreateDate(int authCreateDate) throws LogicException {
		this.authCreateDate = StaticTool.rocToBc(authCreateDate);
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
	 * 扣款銀行<br>
	 * 
	 * @return String
	 */
	public String getRepayBank() {
		return this.repayBank == null ? "" : this.repayBank;
	}

	/**
	 * 扣款銀行<br>
	 * 
	 *
	 * @param repayBank 扣款銀行
	 */
	public void setRepayBank(String repayBank) {
		this.repayBank = repayBank;
	}

	/**
	 * 扣款帳號<br>
	 * 
	 * @return String
	 */
	public String getRepayAcct() {
		return this.repayAcct == null ? "" : this.repayAcct;
	}

	/**
	 * 扣款帳號<br>
	 * 
	 *
	 * @param repayAcct 扣款帳號
	 */
	public void setRepayAcct(String repayAcct) {
		this.repayAcct = repayAcct;
	}

	/**
	 * 新增或取消記號<br>
	 * A:新增授權 D:取消授權 Y.刪除(DeleteDate &amp;gt; 0時，顯示用) Z.暫停授權(DeleteDate &amp;gt;
	 * 0時，顯示用)
	 * 
	 * @return String
	 */
	public String getCreateFlag() {
		return this.createFlag == null ? "" : this.createFlag;
	}

	/**
	 * 新增或取消記號<br>
	 * A:新增授權 D:取消授權 Y.刪除(DeleteDate &amp;gt; 0時，顯示用) Z.暫停授權(DeleteDate &amp;gt;
	 * 0時，顯示用)
	 *
	 * @param createFlag 新增或取消記號
	 */
	public void setCreateFlag(String createFlag) {
		this.createFlag = createFlag;
	}

	@Override
	public int hashCode() {
		return Objects.hash(authCreateDate, custNo, repayBank, repayAcct, createFlag);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		AchAuthLogId achAuthLogId = (AchAuthLogId) obj;
		return authCreateDate == achAuthLogId.authCreateDate && custNo == achAuthLogId.custNo && repayBank.equals(achAuthLogId.repayBank) && repayAcct.equals(achAuthLogId.repayAcct)
				&& createFlag.equals(achAuthLogId.createFlag);
	}

	@Override
	public String toString() {
		return "AchAuthLogId [authCreateDate=" + authCreateDate + ", custNo=" + custNo + ", repayBank=" + repayBank + ", repayAcct=" + repayAcct + ", createFlag=" + createFlag + "]";
	}
}
