package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * LoanNotYet 未齊件管理檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class LoanNotYetId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1087496537609650986L;

// 借款人戶號
	@Column(name = "`CustNo`")
	private int custNo = 0;

	// 額度編號
	@Column(name = "`FacmNo`")
	private int facmNo = 0;

	// 未齊件代碼
	/*
	 * 共用代碼檔01: 代償後謄本02: 火險單03: 借款申請書04: 顧客資料表05: 公司章程06: 公司執照07: 董監名冊08: 股東名冊09:
	 * 會計師簽證或期中報表10: 公司戶營業稅或所得稅申報資料11: 資金運用計畫書12: 土地使用計畫書13: 建築執照14: 董監會借款決議紀錄15:
	 * 個人戶所得稅申報資料16: 債權憑證補章17: 補辦對保手續18: 謄本20: 定存單99: 其他
	 */
	@Column(name = "`NotYetCode`", length = 2)
	private String notYetCode = " ";

	public LoanNotYetId() {
	}

	public LoanNotYetId(int custNo, int facmNo, String notYetCode) {
		this.custNo = custNo;
		this.facmNo = facmNo;
		this.notYetCode = notYetCode;
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
	 * 未齊件代碼<br>
	 * 共用代碼檔 01: 代償後謄本 02: 火險單 03: 借款申請書 04: 顧客資料表 05: 公司章程 06: 公司執照 07: 董監名冊 08:
	 * 股東名冊 09: 會計師簽證或期中報表 10: 公司戶營業稅或所得稅申報資料 11: 資金運用計畫書 12: 土地使用計畫書 13: 建築執照 14:
	 * 董監會借款決議紀錄 15: 個人戶所得稅申報資料 16: 債權憑證補章 17: 補辦對保手續 18: 謄本 20: 定存單 99: 其他
	 * 
	 * @return String
	 */
	public String getNotYetCode() {
		return this.notYetCode == null ? "" : this.notYetCode;
	}

	/**
	 * 未齊件代碼<br>
	 * 共用代碼檔 01: 代償後謄本 02: 火險單 03: 借款申請書 04: 顧客資料表 05: 公司章程 06: 公司執照 07: 董監名冊 08:
	 * 股東名冊 09: 會計師簽證或期中報表 10: 公司戶營業稅或所得稅申報資料 11: 資金運用計畫書 12: 土地使用計畫書 13: 建築執照 14:
	 * 董監會借款決議紀錄 15: 個人戶所得稅申報資料 16: 債權憑證補章 17: 補辦對保手續 18: 謄本 20: 定存單 99: 其他
	 *
	 * @param notYetCode 未齊件代碼
	 */
	public void setNotYetCode(String notYetCode) {
		this.notYetCode = notYetCode;
	}

	@Override
	public int hashCode() {
		return Objects.hash(custNo, facmNo, notYetCode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		LoanNotYetId loanNotYetId = (LoanNotYetId) obj;
		return custNo == loanNotYetId.custNo && facmNo == loanNotYetId.facmNo && notYetCode.equals(loanNotYetId.notYetCode);
	}

	@Override
	public String toString() {
		return "LoanNotYetId [custNo=" + custNo + ", facmNo=" + facmNo + ", notYetCode=" + notYetCode + "]";
	}
}
