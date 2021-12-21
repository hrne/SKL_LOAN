package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * JcicB090 擔保品關聯檔資料檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicB090Id implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1850385436518227774L;

// 資料日期
	@Column(name = "`DataYM`")
	private int dataYM = 0;

	// 授信戶IDN/BAN
	/* Key,左靠，身份證或統一證號 */
	@Column(name = "`CustId`", length = 10)
	private String custId = " ";

	// 擔保品控制編碼
	/* Key,左靠右補空白 */
	@Column(name = "`ClActNo`", length = 50)
	private String clActNo = " ";

	// 額度控制編碼
	/* Key,左靠右補空白,請填本階額度控制編碼或撥款帳號，如屬房貸業務，請填報額度控制編碼 */
	@Column(name = "`FacmNo`", length = 50)
	private String facmNo = " ";

	public JcicB090Id() {
	}

	public JcicB090Id(int dataYM, String custId, String clActNo, String facmNo) {
		this.dataYM = dataYM;
		this.custId = custId;
		this.clActNo = clActNo;
		this.facmNo = facmNo;
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
	 * 授信戶IDN/BAN<br>
	 * Key,左靠，身份證或統一證號
	 * 
	 * @return String
	 */
	public String getCustId() {
		return this.custId == null ? "" : this.custId;
	}

	/**
	 * 授信戶IDN/BAN<br>
	 * Key,左靠，身份證或統一證號
	 *
	 * @param custId 授信戶IDN/BAN
	 */
	public void setCustId(String custId) {
		this.custId = custId;
	}

	/**
	 * 擔保品控制編碼<br>
	 * Key,左靠右補空白
	 * 
	 * @return String
	 */
	public String getClActNo() {
		return this.clActNo == null ? "" : this.clActNo;
	}

	/**
	 * 擔保品控制編碼<br>
	 * Key,左靠右補空白
	 *
	 * @param clActNo 擔保品控制編碼
	 */
	public void setClActNo(String clActNo) {
		this.clActNo = clActNo;
	}

	/**
	 * 額度控制編碼<br>
	 * Key,左靠右補空白,請填本階額度控制編碼或撥款帳號，如屬房貸業務，請填報額度控制編碼
	 * 
	 * @return String
	 */
	public String getFacmNo() {
		return this.facmNo == null ? "" : this.facmNo;
	}

	/**
	 * 額度控制編碼<br>
	 * Key,左靠右補空白,請填本階額度控制編碼或撥款帳號，如屬房貸業務，請填報額度控制編碼
	 *
	 * @param facmNo 額度控制編碼
	 */
	public void setFacmNo(String facmNo) {
		this.facmNo = facmNo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dataYM, custId, clActNo, facmNo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		JcicB090Id jcicB090Id = (JcicB090Id) obj;
		return dataYM == jcicB090Id.dataYM && custId.equals(jcicB090Id.custId) && clActNo.equals(jcicB090Id.clActNo) && facmNo.equals(jcicB090Id.facmNo);
	}

	@Override
	public String toString() {
		return "JcicB090Id [dataYM=" + dataYM + ", custId=" + custId + ", clActNo=" + clActNo + ", facmNo=" + facmNo + "]";
	}
}
