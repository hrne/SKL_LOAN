package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * MonthlyLM014B 月報LM014工作檔B<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class MonthlyLM014BId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5408333371044450971L;

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

	// 企金別
	/* 共用代碼檔0:個金1:企金2:企金自然人 */
	@Column(name = "`EntCode`", length = 1)
	private String entCode = " ";

	// 關係人別
	/* 0: 非關係人 1:關係人 */
	@Column(name = "`RelsFlag`", length = 1)
	private String relsFlag = " ";

	// 抵押品別
	/* 1:銀行保證 2:有價證券 3:不動產 4:員工貸款 5:動產 */
	@Column(name = "`ClFlag`", length = 1)
	private String clFlag = " ";

	public MonthlyLM014BId() {
	}

	public MonthlyLM014BId(int dataYM, String acctCode, int accountType, String acBookCode, String entCode, String relsFlag, String clFlag) {
		this.dataYM = dataYM;
		this.acctCode = acctCode;
		this.accountType = accountType;
		this.acBookCode = acBookCode;
		this.entCode = entCode;
		this.relsFlag = relsFlag;
		this.clFlag = clFlag;
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
	 * 企金別<br>
	 * 共用代碼檔 0:個金 1:企金 2:企金自然人
	 * 
	 * @return String
	 */
	public String getEntCode() {
		return this.entCode == null ? "" : this.entCode;
	}

	/**
	 * 企金別<br>
	 * 共用代碼檔 0:個金 1:企金 2:企金自然人
	 *
	 * @param entCode 企金別
	 */
	public void setEntCode(String entCode) {
		this.entCode = entCode;
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

	@Override
	public int hashCode() {
		return Objects.hash(dataYM, acctCode, accountType, acBookCode, entCode, relsFlag, clFlag);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		MonthlyLM014BId monthlyLM014BId = (MonthlyLM014BId) obj;
		return dataYM == monthlyLM014BId.dataYM && acctCode.equals(monthlyLM014BId.acctCode) && accountType == monthlyLM014BId.accountType && acBookCode.equals(monthlyLM014BId.acBookCode)
				&& entCode.equals(monthlyLM014BId.entCode) && relsFlag.equals(monthlyLM014BId.relsFlag) && clFlag.equals(monthlyLM014BId.clFlag);
	}

	@Override
	public String toString() {
		return "MonthlyLM014BId [dataYM=" + dataYM + ", acctCode=" + acctCode + ", accountType=" + accountType + ", acBookCode=" + acBookCode + ", entCode=" + entCode + ", relsFlag=" + relsFlag
				+ ", clFlag=" + clFlag + "]";
	}
}
