package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * JcicB207 聯徵授信戶基本資料檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicB207Id implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3996800709784479291L;

// 資料年月
	@Column(name = "`DataYM`")
	private int dataYM = 0;

	// 總行代號
	/* Key,金融機構總機構之代號，三位數字 */
	@Column(name = "`BankItem`", length = 3)
	private String bankItem = " ";

	// 授信戶IDN
	/* Key,左靠，身份證或統一證號 */
	@Column(name = "`CustId`", length = 10)
	private String custId = " ";

	public JcicB207Id() {
	}

	public JcicB207Id(int dataYM, String bankItem, String custId) {
		this.dataYM = dataYM;
		this.bankItem = bankItem;
		this.custId = custId;
	}

	/**
	 * 資料年月<br>
	 * 
	 * @return Integer
	 */
	public int getDataYM() {
		return this.dataYM;
	}

	/**
	 * 資料年月<br>
	 * 
	 *
	 * @param dataYM 資料年月
	 */
	public void setDataYM(int dataYM) {
		this.dataYM = dataYM;
	}

	/**
	 * 總行代號<br>
	 * Key,金融機構總機構之代號，三位數字
	 * 
	 * @return String
	 */
	public String getBankItem() {
		return this.bankItem == null ? "" : this.bankItem;
	}

	/**
	 * 總行代號<br>
	 * Key,金融機構總機構之代號，三位數字
	 *
	 * @param bankItem 總行代號
	 */
	public void setBankItem(String bankItem) {
		this.bankItem = bankItem;
	}

	/**
	 * 授信戶IDN<br>
	 * Key,左靠，身份證或統一證號
	 * 
	 * @return String
	 */
	public String getCustId() {
		return this.custId == null ? "" : this.custId;
	}

	/**
	 * 授信戶IDN<br>
	 * Key,左靠，身份證或統一證號
	 *
	 * @param custId 授信戶IDN
	 */
	public void setCustId(String custId) {
		this.custId = custId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dataYM, bankItem, custId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		JcicB207Id jcicB207Id = (JcicB207Id) obj;
		return dataYM == jcicB207Id.dataYM && bankItem.equals(jcicB207Id.bankItem) && custId.equals(jcicB207Id.custId);
	}

	@Override
	public String toString() {
		return "JcicB207Id [dataYM=" + dataYM + ", bankItem=" + bankItem + ", custId=" + custId + "]";
	}
}
