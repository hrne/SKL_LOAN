package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * MonthlyLM014C 月報LM014工作檔C<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class MonthlyLM014CId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1862975998058015875L;

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

	// 關係人別
	/* 0: 非關係人 1:關係人 */
	@Column(name = "`RelsFlag`", length = 1)
	private String relsFlag = " ";

	// 抵押品別
	/* 1:銀行保證 2:有價證券 3:不動產 4:員工貸款 5:動產 */
	@Column(name = "`ClFlag`", length = 1)
	private String clFlag = " ";

	// 案件隸屬單位
	/* 共用代碼檔0:非企金單位 1:企金推展課 */
	@Column(name = "`DepartmentCode`", length = 1)
	private String departmentCode = " ";

	public MonthlyLM014CId() {
	}

	public MonthlyLM014CId(int dataYM, String acctCode, int accountType, String acBookCode, String relsFlag, String clFlag, String departmentCode) {
		this.dataYM = dataYM;
		this.acctCode = acctCode;
		this.accountType = accountType;
		this.acBookCode = acBookCode;
		this.relsFlag = relsFlag;
		this.clFlag = clFlag;
		this.departmentCode = departmentCode;
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

	/**
	 * 關係人別<br>
	 * 0: 非關係人 1:關係人
	 * 
	 * @return String
	 */
	public String getRelsFlag() {
		return this.relsFlag == null ? "" : this.relsFlag;
	}

	/**
	 * 關係人別<br>
	 * 0: 非關係人 1:關係人
	 *
	 * @param relsFlag 關係人別
	 */
	public void setRelsFlag(String relsFlag) {
		this.relsFlag = relsFlag;
	}

	/**
	 * 抵押品別<br>
	 * 1:銀行保證 2:有價證券 3:不動產 4:員工貸款 5:動產
	 * 
	 * @return String
	 */
	public String getClFlag() {
		return this.clFlag == null ? "" : this.clFlag;
	}

	/**
	 * 抵押品別<br>
	 * 1:銀行保證 2:有價證券 3:不動產 4:員工貸款 5:動產
	 *
	 * @param clFlag 抵押品別
	 */
	public void setClFlag(String clFlag) {
		this.clFlag = clFlag;
	}

	/**
	 * 案件隸屬單位<br>
	 * 共用代碼檔 0:非企金單位 1:企金推展課
	 * 
	 * @return String
	 */
	public String getDepartmentCode() {
		return this.departmentCode == null ? "" : this.departmentCode;
	}

	/**
	 * 案件隸屬單位<br>
	 * 共用代碼檔 0:非企金單位 1:企金推展課
	 *
	 * @param departmentCode 案件隸屬單位
	 */
	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dataYM, acctCode, accountType, acBookCode, relsFlag, clFlag, departmentCode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		MonthlyLM014CId monthlyLM014CId = (MonthlyLM014CId) obj;
		return dataYM == monthlyLM014CId.dataYM && acctCode.equals(monthlyLM014CId.acctCode) && accountType == monthlyLM014CId.accountType && acBookCode.equals(monthlyLM014CId.acBookCode)
				&& relsFlag.equals(monthlyLM014CId.relsFlag) && clFlag.equals(monthlyLM014CId.clFlag) && departmentCode.equals(monthlyLM014CId.departmentCode);
	}

	@Override
	public String toString() {
		return "MonthlyLM014CId [dataYM=" + dataYM + ", acctCode=" + acctCode + ", accountType=" + accountType + ", acBookCode=" + acBookCode + ", relsFlag=" + relsFlag + ", clFlag=" + clFlag
				+ ", departmentCode=" + departmentCode + "]";
	}
}
