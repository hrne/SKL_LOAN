package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * MonthlyLM014A 月報LM014工作檔A<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class MonthlyLM014AId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4908277356097295416L;

// 資料年月
	/* 西元年月YYYYMM */
	@Column(name = "`DataYM`")
	private int dataYM = 0;

	// 業務科目代號
	/* CdAcCode會計科子細目設定檔 */
	@Column(name = "`AcctCode`", length = 3)
	private String acctCode = " ";

	// 帳戶類別
	/* 1:個人 2:公司 3:合計*早期資料才有3:合計,近年資料無3:合計 */
	@Column(name = "`AccountType`")
	private int accountType = 0;

	// 帳冊別
	@Column(name = "`AcBookCode`", length = 3)
	private String acBookCode = " ";

	public MonthlyLM014AId() {
	}

	public MonthlyLM014AId(int dataYM, String acctCode, int accountType, String acBookCode) {
		this.dataYM = dataYM;
		this.acctCode = acctCode;
		this.accountType = accountType;
		this.acBookCode = acBookCode;
	}

	/**
	 * 資料年月<br>
	 * 西元年月YYYYMM
	 * 
	 * @return Integer
	 */
	public int getDataYM() {
		return this.dataYM;
	}

	/**
	 * 資料年月<br>
	 * 西元年月YYYYMM
	 *
	 * @param dataYM 資料年月
	 */
	public void setDataYM(int dataYM) {
		this.dataYM = dataYM;
	}

	/**
	 * 業務科目代號<br>
	 * CdAcCode會計科子細目設定檔
	 * 
	 * @return String
	 */
	public String getAcctCode() {
		return this.acctCode == null ? "" : this.acctCode;
	}

	/**
	 * 業務科目代號<br>
	 * CdAcCode會計科子細目設定檔
	 *
	 * @param acctCode 業務科目代號
	 */
	public void setAcctCode(String acctCode) {
		this.acctCode = acctCode;
	}

	/**
	 * 帳戶類別<br>
	 * 1:個人 2:公司 3:合計 早期資料才有3:合計,近年資料無3:合計
	 * 
	 * @return Integer
	 */
	public int getAccountType() {
		return this.accountType;
	}

	/**
	 * 帳戶類別<br>
	 * 1:個人 2:公司 3:合計 早期資料才有3:合計,近年資料無3:合計
	 *
	 * @param accountType 帳戶類別
	 */
	public void setAccountType(int accountType) {
		this.accountType = accountType;
	}

	/**
	 * 帳冊別<br>
	 * 
	 * @return String
	 */
	public String getAcBookCode() {
		return this.acBookCode == null ? "" : this.acBookCode;
	}

	/**
	 * 帳冊別<br>
	 * 
	 *
	 * @param acBookCode 帳冊別
	 */
	public void setAcBookCode(String acBookCode) {
		this.acBookCode = acBookCode;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dataYM, acctCode, accountType, acBookCode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		MonthlyLM014AId monthlyLM014AId = (MonthlyLM014AId) obj;
		return dataYM == monthlyLM014AId.dataYM && acctCode.equals(monthlyLM014AId.acctCode) && accountType == monthlyLM014AId.accountType && acBookCode.equals(monthlyLM014AId.acBookCode);
	}

	@Override
	public String toString() {
		return "MonthlyLM014AId [dataYM=" + dataYM + ", acctCode=" + acctCode + ", accountType=" + accountType + ", acBookCode=" + acBookCode + "]";
	}
}
