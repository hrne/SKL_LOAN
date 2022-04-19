package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * JcicB085 聯徵帳號轉換資料檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicB085Id implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4117991248765390823L;

// 資料日期
	@Column(name = "`DataYM`")
	private int dataYM = 0;

	// 轉換前帳號
	/* Key,左靠，填報本筆撥款轉換前帳號。 */
	@Column(name = "`BefAcctNo`", length = 50)
	private String befAcctNo = " ";

	// 轉換後帳號
	/* Key,左靠，填報本筆撥款轉換後帳號。 */
	@Column(name = "`AftAcctNo`", length = 50)
	private String aftAcctNo = " ";

	public JcicB085Id() {
	}

	public JcicB085Id(int dataYM, String befAcctNo, String aftAcctNo) {
		this.dataYM = dataYM;
		this.befAcctNo = befAcctNo;
		this.aftAcctNo = aftAcctNo;
	}

	/**
	 * 資料日期<br>
	 * 
	 * @return Integer
	 */
	public int getDataYM() {
		return this.dataYM;
	}

	/**
	 * 資料日期<br>
	 * 
	 *
	 * @param dataYM 資料日期
	 */
	public void setDataYM(int dataYM) {
		this.dataYM = dataYM;
	}

	/**
	 * 轉換前帳號<br>
	 * Key,左靠，填報本筆撥款轉換前帳號。
	 * 
	 * @return String
	 */
	public String getBefAcctNo() {
		return this.befAcctNo == null ? "" : this.befAcctNo;
	}

	/**
	 * 轉換前帳號<br>
	 * Key,左靠，填報本筆撥款轉換前帳號。
	 *
	 * @param befAcctNo 轉換前帳號
	 */
	public void setBefAcctNo(String befAcctNo) {
		this.befAcctNo = befAcctNo;
	}

	/**
	 * 轉換後帳號<br>
	 * Key,左靠，填報本筆撥款轉換後帳號。
	 * 
	 * @return String
	 */
	public String getAftAcctNo() {
		return this.aftAcctNo == null ? "" : this.aftAcctNo;
	}

	/**
	 * 轉換後帳號<br>
	 * Key,左靠，填報本筆撥款轉換後帳號。
	 *
	 * @param aftAcctNo 轉換後帳號
	 */
	public void setAftAcctNo(String aftAcctNo) {
		this.aftAcctNo = aftAcctNo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dataYM, befAcctNo, aftAcctNo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		JcicB085Id jcicB085Id = (JcicB085Id) obj;
		return dataYM == jcicB085Id.dataYM && befAcctNo.equals(jcicB085Id.befAcctNo) && aftAcctNo.equals(jcicB085Id.aftAcctNo);
	}

	@Override
	public String toString() {
		return "JcicB085Id [dataYM=" + dataYM + ", befAcctNo=" + befAcctNo + ", aftAcctNo=" + aftAcctNo + "]";
	}
}
