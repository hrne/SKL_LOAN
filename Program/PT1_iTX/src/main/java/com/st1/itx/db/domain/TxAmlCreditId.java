package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * TxAmlCredit AML定審資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class TxAmlCreditId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1489793552489386088L;

// 定審日期
	/* 對應"AML評級查詢規格" */
	@Column(name = "`DataDt`")
	private int dataDt = 0;

	// 身分證字號
	/* 對應"AML評級查詢規格" */
	@Column(name = "`CustKey`", length = 10)
	private String custKey = " ";

	public TxAmlCreditId() {
	}

	public TxAmlCreditId(int dataDt, String custKey) {
		this.dataDt = dataDt;
		this.custKey = custKey;
	}

	/**
	 * 定審日期<br>
	 * 對應"AML評級查詢規格"
	 * 
	 * @return Integer
	 */
	public int getDataDt() {
		return StaticTool.bcToRoc(this.dataDt);
	}

	/**
	 * 定審日期<br>
	 * 對應"AML評級查詢規格"
	 *
	 * @param dataDt 定審日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setDataDt(int dataDt) throws LogicException {
		this.dataDt = StaticTool.rocToBc(dataDt);
	}

	/**
	 * 身分證字號<br>
	 * 對應"AML評級查詢規格"
	 * 
	 * @return String
	 */
	public String getCustKey() {
		return this.custKey == null ? "" : this.custKey;
	}

	/**
	 * 身分證字號<br>
	 * 對應"AML評級查詢規格"
	 *
	 * @param custKey 身分證字號
	 */
	public void setCustKey(String custKey) {
		this.custKey = custKey;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dataDt, custKey);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		TxAmlCreditId txAmlCreditId = (TxAmlCreditId) obj;
		return dataDt == txAmlCreditId.dataDt && custKey.equals(txAmlCreditId.custKey);
	}

	@Override
	public String toString() {
		return "TxAmlCreditId [dataDt=" + dataDt + ", custKey=" + custKey + "]";
	}
}
